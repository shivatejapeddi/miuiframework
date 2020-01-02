package miui.view;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.MobileDataUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.miui.R;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.AnrMonitor;
import android.os.Handler;
import android.os.SystemProperties;
import android.provider.MiuiSettings.System;
import android.provider.Settings;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import com.miui.internal.search.SettingsTree;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import miui.os.Build;
import miui.telephony.TelephonyManager;
import org.json.JSONObject;

public class MiuiSecurityPermissionHandler {
    private static final boolean DEBUG = true;
    private static final int LISTEN_MODE_ACCOUNT = 1;
    private static final int LISTEN_MODE_WIFI = 2;
    private static final int NETWORK_ERROR = -2;
    private static final int PERMISSION_ACCOUNT_WHITELIST = 1;
    private static final int PERMISSION_ERROR = -1;
    private static final int PERMISSION_IMEIACCOUNT_WHITELIST = 3;
    private static final int PERMISSION_IMEI_WHITELIST = 2;
    private static final int POST_VERIFICATION_REQUEST = 0;
    private static final int POST_VERIFICATION_WATER_MARKER = 1;
    private static String TAG = "MiuiPermission";
    private static final int WATERMARKER_ACCOUNT_WHITELIST = 1;
    private static final int WATERMARKER_IMEI_ACCOUNT_WHITELIST = 3;
    private static final int WATERMARKER_IMEI_WHITELIST = 2;
    private static final int WATERMARKER_SHOW = 0;
    private static String sDefaultUrl = "https://update.miui.com/updates/mi-vip.php";
    private static String sGlobalUrl = "https://update.intl.miui.com/updates/mi-vip.php";
    private boolean mBootComplete = false;
    private ContentObserver mContentObserver;
    private final Context mContext;
    private int mMiuiSecurityImeiFlag = 0;
    private boolean mNeedAddAccount = false;
    private boolean mNeedListenAccount = false;
    private boolean mOpenWifiOnce = false;
    private boolean mPermissionListenAccount = false;
    private View mPermissionView;
    private PermissionViewCallback mPermissionViewCallback;
    private final Object mPermissionViewLock = new Object();
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                    NetworkInfo info = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                    if (info != null && State.CONNECTED == info.getState() && info.isAvailable()) {
                        MiuiSecurityPermissionHandler.this.updateWaterMarkerAccount();
                        String access$100 = MiuiSecurityPermissionHandler.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(info.getType());
                        stringBuilder.append(" Connected!");
                        Log.i(access$100, stringBuilder.toString());
                    }
                }
                synchronized (this) {
                    if (action.equals("miui.intent.action.FINISH_BOOTING")) {
                        if (MiuiSecurityPermissionHandler.this.mNeedAddAccount) {
                            MiuiSecurityPermissionHandler.this.mPermissionViewCallback.onAddAccount();
                        }
                        if (MiuiSecurityPermissionHandler.this.mPermissionListenAccount) {
                            MiuiSecurityPermissionHandler.this.mPermissionViewCallback.onListenAccount(1);
                        }
                        MiuiSecurityPermissionHandler.this.mBootComplete = true;
                    }
                }
                if (action.equals(Intent.ACTION_SHUTDOWN)) {
                    Account account = MiuiSecurityPermissionHandler.this.loadAccountId();
                    if ((MiuiSecurityPermissionHandler.this.mRetPermission == 1 && account != null) || MiuiSecurityPermissionHandler.this.mRetPermission == 2 || MiuiSecurityPermissionHandler.this.mRetPermission == 3) {
                        String access$1002 = MiuiSecurityPermissionHandler.TAG;
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("MIUI_ACCOUNT_LOGIN_CHECK: mRetPermission:");
                        stringBuilder2.append(MiuiSecurityPermissionHandler.this.mRetPermission);
                        stringBuilder2.append(" account: ");
                        stringBuilder2.append(account);
                        Log.i(access$1002, stringBuilder2.toString());
                        Global.putInt(MiuiSecurityPermissionHandler.this.mContext.getContentResolver(), System.MIUI_ACCOUNT_LOGIN_CHECK, 1);
                    } else {
                        Global.putInt(MiuiSecurityPermissionHandler.this.mContext.getContentResolver(), System.MIUI_ACCOUNT_LOGIN_CHECK, 0);
                    }
                }
            }
        }
    };
    private int mRetPermission = -2;
    private int mRetWater = -2;
    private int responseResult = -2;

    public interface PermissionViewCallback {
        void onAddAccount();

        void onHideWaterMarker();

        void onListenAccount(int i);

        void onListenPermission();

        void onShowWaterMarker();

        void onUnListenAccount(int i);
    }

    public MiuiSecurityPermissionHandler(Context context, PermissionViewCallback callback) {
        this.mContext = context;
        if (Build.IS_PRIVATE_BUILD || Build.IS_PRIVATE_WATER_MARKER) {
            registerPermissionViewCallback(callback);
            registerNetWReceiver(context);
            mayBringUpPermissionView();
        }
    }

    private void registerNetWReceiver(Context ctx) {
        IntentFilter filter = new IntentFilter();
        filter.setPriority(1000);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("miui.intent.action.FINISH_BOOTING");
        filter.addAction(Intent.ACTION_SHUTDOWN);
        ctx.registerReceiver(this.mReceiver, filter);
    }

    private void doWaterMarker() {
        this.mMiuiSecurityImeiFlag = Global.getInt(this.mContext.getContentResolver(), System.MIUI_PERMISSION_CHECK, 0);
        if ((this.mMiuiSecurityImeiFlag & 2) == 0) {
            final Account account = loadAccountId();
            this.mRetWater = postVerificationWaterMarker(account == null ? null : account.name);
            int i = this.mRetWater;
            if (i == -2 || i == 0) {
                if (Secure.getInt(this.mContext.getContentResolver(), "device_provisioned", 0) != 0) {
                    synchronized (this) {
                        if (-2 == this.mRetWater || this.mRetWater == 0) {
                            this.mPermissionViewCallback.onShowWaterMarker();
                        }
                    }
                } else {
                    final Handler handler = new Handler(this.mContext.getMainLooper());
                    this.mContentObserver = new ContentObserver(null) {
                        public void onChange(boolean selfChange) {
                            super.onChange(selfChange);
                            handler.post(new Runnable() {
                                public void run() {
                                    boolean z = false;
                                    if (Secure.getInt(MiuiSecurityPermissionHandler.this.mContext.getContentResolver(), "device_provisioned", 0) != 0) {
                                        z = true;
                                    }
                                    if (z) {
                                        synchronized (this) {
                                            Account accountInfo = account;
                                            if (accountInfo == null) {
                                                accountInfo = MiuiSecurityPermissionHandler.this.loadAccountId();
                                            }
                                            MiuiSecurityPermissionHandler.this.mRetWater = MiuiSecurityPermissionHandler.this.postVerificationWaterMarker(accountInfo == null ? null : accountInfo.name);
                                            if (-2 == MiuiSecurityPermissionHandler.this.mRetWater || MiuiSecurityPermissionHandler.this.mRetWater == 0) {
                                                MiuiSecurityPermissionHandler.this.mPermissionViewCallback.onShowWaterMarker();
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    };
                    this.mContext.getContentResolver().registerContentObserver(Secure.getUriFor("device_provisioned"), false, this.mContentObserver);
                }
            }
        }
    }

    private void doPermissionView() {
        String str = "device_provisioned";
        boolean isLogin = true;
        boolean provisioned = Secure.getInt(this.mContext.getContentResolver(), str, 0) != 0;
        if (Global.getInt(this.mContext.getContentResolver(), System.MIUI_ACCOUNT_LOGIN_CHECK, 0) == 0) {
            isLogin = false;
        }
        if (!provisioned) {
            final Handler handler = new Handler(this.mContext.getMainLooper());
            this.mContentObserver = new ContentObserver(null) {
                public void onChange(boolean selfChange) {
                    super.onChange(selfChange);
                    handler.post(new Runnable() {
                        public void run() {
                            boolean z = false;
                            if (Secure.getInt(MiuiSecurityPermissionHandler.this.mContext.getContentResolver(), "device_provisioned", 0) != 0) {
                                z = true;
                            }
                            if (z) {
                                if (!isLogin) {
                                    MiuiSecurityPermissionHandler.this.createPermissionView();
                                }
                                MiuiSecurityPermissionHandler.this.mContext.getContentResolver().unregisterContentObserver(MiuiSecurityPermissionHandler.this.mContentObserver);
                            }
                        }
                    });
                }
            };
            this.mContext.getContentResolver().registerContentObserver(Secure.getUriFor(str), false, this.mContentObserver);
        } else if (!isLogin) {
            createPermissionView();
        }
    }

    public void mayBringUpPermissionView() {
        if (Build.IS_PRIVATE_BUILD) {
            doWaterMarker();
            doPermissionView();
        } else if (Build.IS_PRIVATE_WATER_MARKER) {
            doWaterMarker();
        }
    }

    public void createPermissionView() {
        this.mMiuiSecurityImeiFlag = Global.getInt(this.mContext.getContentResolver(), System.MIUI_PERMISSION_CHECK, 0);
        if ((this.mMiuiSecurityImeiFlag & 1) == 0) {
            final WindowManager wm = (WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE);
            Log.d(TAG, "createPermissionView!");
            synchronized (this.mPermissionViewLock) {
                if (this.mPermissionView == null) {
                    this.mPermissionView = View.inflate(this.mContext, R.layout.security_permission_view, null);
                    LayoutParams layoutParams = new LayoutParams(-1, -1, 2016, 84018432, 1);
                    layoutParams.setTitle("Permission");
                    wm.addView(this.mPermissionView, layoutParams);
                }
            }
            final Button action = (Button) this.mPermissionView.findViewById(R.id.action_button);
            Button wifi = (Button) this.mPermissionView.findViewById(R.id.open_wifi_settings_button);
            action.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    final Account account = MiuiSecurityPermissionHandler.this.loadAccountId();
                    action.setEnabled(false);
                    new Thread() {
                        public void run() {
                            MiuiSecurityPermissionHandler miuiSecurityPermissionHandler = MiuiSecurityPermissionHandler.this;
                            View access$1200 = MiuiSecurityPermissionHandler.this.mPermissionView;
                            Button button = action;
                            Account account = account;
                            miuiSecurityPermissionHandler.postVerificationRequest(access$1200, button, account == null ? "null" : account.name);
                            if (MiuiSecurityPermissionHandler.this.mRetPermission == -2 || MiuiSecurityPermissionHandler.this.mRetPermission == -1) {
                                synchronized (this) {
                                    if (account == null) {
                                        if (MiuiSecurityPermissionHandler.this.mBootComplete) {
                                            MiuiSecurityPermissionHandler.this.mPermissionViewCallback.onListenAccount(1);
                                            MiuiSecurityPermissionHandler.this.mPermissionViewCallback.onAddAccount();
                                        } else {
                                            MiuiSecurityPermissionHandler.this.mPermissionListenAccount = true;
                                            MiuiSecurityPermissionHandler.this.mNeedAddAccount = true;
                                        }
                                    }
                                }
                            }
                        }
                    }.start();
                }
            });
            wifi.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    new Thread() {
                        public void run() {
                            synchronized (MiuiSecurityPermissionHandler.this.mPermissionViewLock) {
                                try {
                                    if (MiuiSecurityPermissionHandler.this.mPermissionView != null) {
                                        Log.d(MiuiSecurityPermissionHandler.TAG, "wifi OnClick remove View!");
                                        MiuiSecurityPermissionHandler.this.mPermissionViewCallback.onListenAccount(2);
                                        wm.removeView(MiuiSecurityPermissionHandler.this.mPermissionView);
                                        MiuiSecurityPermissionHandler.this.mPermissionView = null;
                                    }
                                } catch (Exception e) {
                                    String access$100 = MiuiSecurityPermissionHandler.TAG;
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("wifi OnClick  removeView ex: ");
                                    stringBuilder.append(e);
                                    Log.d(access$100, stringBuilder.toString());
                                }
                            }
                            MiuiSecurityPermissionHandler.this.onOpenWifiSettingsButtonClicked();
                        }
                    }.start();
                }
            });
        }
    }

    private void onOpenWifiSettingsButtonClicked() {
        PackageManager packageManager = this.mContext.getPackageManager();
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        intent.setPackage(SettingsTree.SETTINGS_PACKAGE);
        if (packageManager.queryIntentActivities(intent, null).size() > 0) {
            intent.putExtra("extra_show_on_finddevice_keyguard", true);
            intent.setFlags(268468224);
            this.mContext.startActivity(intent);
        }
    }

    private Account loadAccountId() {
        Account[] accounts = AccountManager.get(this.mContext).getAccountsByType("com.xiaomi");
        if (accounts == null || accounts.length <= 0) {
            return null;
        }
        return accounts[0];
    }

    private byte[] generateRawKey(String key) {
        byte[] rawKey = Base64.decode(key.getBytes(), 0);
        if (rawKey.length % 8 == 0) {
            return rawKey;
        }
        byte[] ret = new byte[((rawKey.length + 8) - (rawKey.length % 8))];
        for (int i = 0; i < rawKey.length; i++) {
            ret[i] = rawKey[i];
        }
        return ret;
    }

    private String decryptData(String data) {
        SecretKeySpec keySpec = new SecretKeySpec(generateRawKey("ODQ4NWFmYjdhNGE="), KeyProperties.KEY_ALGORITHM_AES);
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(2, keySpec, new IvParameterSpec("0102030405060708".getBytes()));
            return new String(cipher.doFinal(Base64.decode(data, 0)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void processMiuiSecurityImeiFlag(int retPermission, int retWater) {
        if (this.mMiuiSecurityImeiFlag != 3) {
            if (retPermission == 2 || retPermission == 3) {
                this.mMiuiSecurityImeiFlag |= 1;
            }
            if (retWater == 2 || retWater == 3) {
                this.mMiuiSecurityImeiFlag = 2 | this.mMiuiSecurityImeiFlag;
            }
            Global.putInt(this.mContext.getContentResolver(), System.MIUI_PERMISSION_CHECK, this.mMiuiSecurityImeiFlag);
        }
    }

    private void processResult(String response) {
        try {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("response:");
            stringBuilder.append(response);
            Log.i(str, stringBuilder.toString());
            JSONObject obj = new JSONObject(response);
            int Auth = obj.getInt("Auth");
            int watermark = obj.getInt("Watermark");
            this.mRetPermission = Auth;
            this.mRetWater = watermark;
            processMiuiSecurityImeiFlag(Auth, watermark);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int processWatermarResult(String response) {
        try {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("response:");
            stringBuilder.append(response);
            Log.i(str, stringBuilder.toString());
            JSONObject obj = new JSONObject(response);
            int Auth = obj.getInt("Auth");
            int watermark = obj.getInt("Watermark");
            processMiuiSecurityImeiFlag(Auth, watermark);
            return watermark;
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        }
    }

    private void enableWifiAndData() {
        this.mOpenWifiOnce = true;
        MobileDataUtils.getInstance().enableMobileData(this.mContext, true);
        WifiManager wm = (WifiManager) this.mContext.getSystemService("wifi");
        if (!wm.isWifiEnabled()) {
            wm.setWifiEnabled(true);
        }
    }

    private void postVerificationRequest(final View parentView, final Button btn, String actName) {
        Runnable anonymousClass7;
        try {
            postVerificationResult(actName, 0);
            anonymousClass7 = new Runnable() {
                /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
                public void run() {
                    /*
                    r7 = this;
                    r0 = r4;
                    r1 = 1;
                    r0.setEnabled(r1);
                    r0 = miui.view.MiuiSecurityPermissionHandler.this;
                    r0 = r0.loadAccountId();
                    r1 = miui.view.MiuiSecurityPermissionHandler.this;
                    r1 = r1.mRetPermission;
                    if (r1 > 0) goto L_0x003b;
                L_0x0014:
                    if (r0 != 0) goto L_0x0017;
                L_0x0016:
                    goto L_0x003b;
                L_0x0017:
                    r1 = miui.view.MiuiSecurityPermissionHandler.this;
                    r1 = r1.mRetPermission;
                    r2 = -1;
                    if (r1 != r2) goto L_0x0029;
                L_0x0020:
                    r1 = r4;
                    r2 = 286130424; // 0x110e00f8 float:1.1202123E-28 double:1.413672127E-315;
                    r1.setText(r2);
                    goto L_0x008e;
                L_0x0029:
                    r1 = miui.view.MiuiSecurityPermissionHandler.this;
                    r1 = r1.mRetPermission;
                    r2 = -2;
                    if (r1 != r2) goto L_0x008e;
                L_0x0032:
                    r1 = r4;
                    r2 = 286130426; // 0x110e00fa float:1.1202126E-28 double:1.413672137E-315;
                    r1.setText(r2);
                    goto L_0x008e;
                L_0x003b:
                    r1 = r3;
                    r1 = r1.getContext();
                    r2 = "window";
                    r1 = r1.getSystemService(r2);
                    r1 = (android.view.WindowManager) r1;
                    r2 = miui.view.MiuiSecurityPermissionHandler.this;
                    r2 = r2.mPermissionViewLock;
                    monitor-enter(r2);
                    r3 = miui.view.MiuiSecurityPermissionHandler.this;	 Catch:{ Exception -> 0x0073 }
                    r3 = r3.mPermissionView;	 Catch:{ Exception -> 0x0073 }
                    if (r3 == 0) goto L_0x0070;
                L_0x0058:
                    r3 = miui.view.MiuiSecurityPermissionHandler.TAG;	 Catch:{ Exception -> 0x0073 }
                    r4 = "verify button OnClick  removeView!";
                    android.util.Log.d(r3, r4);	 Catch:{ Exception -> 0x0073 }
                    r3 = miui.view.MiuiSecurityPermissionHandler.this;	 Catch:{ Exception -> 0x0073 }
                    r3 = r3.mPermissionView;	 Catch:{ Exception -> 0x0073 }
                    r1.removeView(r3);	 Catch:{ Exception -> 0x0073 }
                    r3 = miui.view.MiuiSecurityPermissionHandler.this;	 Catch:{ Exception -> 0x0073 }
                    r4 = 0;
                    r3.mPermissionView = r4;	 Catch:{ Exception -> 0x0073 }
                L_0x0070:
                    goto L_0x008c;
                L_0x0071:
                    r3 = move-exception;
                    goto L_0x008f;
                L_0x0073:
                    r3 = move-exception;
                    r4 = miui.view.MiuiSecurityPermissionHandler.TAG;	 Catch:{ all -> 0x0071 }
                    r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0071 }
                    r5.<init>();	 Catch:{ all -> 0x0071 }
                    r6 = "verify button OnClick  removeView ex: ";
                    r5.append(r6);	 Catch:{ all -> 0x0071 }
                    r5.append(r3);	 Catch:{ all -> 0x0071 }
                    r5 = r5.toString();	 Catch:{ all -> 0x0071 }
                    android.util.Log.d(r4, r5);	 Catch:{ all -> 0x0071 }
                L_0x008c:
                    monitor-exit(r2);	 Catch:{ all -> 0x0071 }
                L_0x008e:
                    return;
                L_0x008f:
                    monitor-exit(r2);	 Catch:{ all -> 0x0071 }
                    throw r3;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: miui.view.MiuiSecurityPermissionHandler$AnonymousClass7.run():void");
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
            if (!this.mOpenWifiOnce) {
                btn.post(new Runnable() {
                    public void run() {
                        ((TextView) parentView.findViewById(R.id.hint_text)).setText((int) R.string.security_permission_wifi_and_data_opened_text);
                        MiuiSecurityPermissionHandler.this.enableWifiAndData();
                    }
                });
            }
            anonymousClass7 = /* anonymous class already generated */;
        } catch (Throwable th) {
            btn.post(/* anonymous class already generated */);
        }
        btn.post(anonymousClass7);
    }

    private int postVerificationWaterMarker(String actName) {
        try {
            postVerificationResult(actName, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.responseResult;
    }

    private void postVerificationResult(String actName, int type) throws IOException {
        String str = actName;
        int i = type;
        String str2 = "&sid=1&device=";
        String str3 = "uid=";
        OutputStreamWriter writer = null;
        InputStreamReader reader = null;
        HttpURLConnection conn = null;
        String modDevice = SystemProperties.get("ro.product.mod_device", null);
        modDevice = TextUtils.isEmpty(modDevice) ? Build.DEVICE : modDevice;
        try {
            conn = (HttpURLConnection) (Build.IS_INTERNATIONAL_BUILD ? new URL(sGlobalUrl) : new URL(sDefaultUrl)).openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            writer = new OutputStreamWriter(conn.getOutputStream());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str3);
            stringBuilder.append(str);
            stringBuilder.append(str2);
            stringBuilder.append(modDevice);
            writer.write(stringBuilder.toString());
            String str4 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str3);
            stringBuilder2.append(str);
            stringBuilder2.append(str2);
            stringBuilder2.append(modDevice);
            Log.i(str4, stringBuilder2.toString());
            appendImei(writer);
            writer.flush();
            writer.close();
            writer = null;
            if (conn.getResponseCode() == 200) {
                reader = new InputStreamReader(conn.getInputStream());
                char[] buffer = new char[4096];
                int idx = 0;
                while (true) {
                    int read = reader.read(buffer, idx, 4096 - idx);
                    int cnt = read;
                    if (read == -1) {
                        break;
                    }
                    idx += cnt;
                }
                reader.close();
                reader = null;
                if (i == 1) {
                    this.responseResult = processWatermarResult(decryptData(new String(buffer, 0, idx)));
                } else if (i == 0) {
                    processResult(decryptData(new String(buffer, 0, idx)));
                    str3 = TAG;
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("mRetPermission:");
                    stringBuilder3.append(this.mRetPermission);
                    stringBuilder3.append("mRetWater ");
                    stringBuilder3.append(this.mRetWater);
                    Log.i(str3, stringBuilder3.toString());
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            conn.disconnect();
        } catch (Exception e3) {
            throw e3;
        } catch (Throwable th) {
            Throwable th2 = th;
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e22) {
                    e22.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e222) {
                    e222.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private void appendImei(OutputStreamWriter writer) {
        IOException e;
        List<String> imeis = TelephonyManager.getDefault().getImeiList();
        String imei2 = null;
        if (imeis != null && imeis.size() != 0) {
            String imei1 = hashSHA1((String) imeis.get(0));
            if (imeis.size() > 1) {
                imei2 = hashSHA1((String) imeis.get(1));
            }
            if (imei1 != null) {
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("&imei1=");
                    stringBuilder.append(imei1);
                    writer.append(stringBuilder.toString());
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            if (imei2 != null) {
                e2 = new StringBuilder();
                e2.append("&imei2=");
                e2.append(imei2);
                writer.append(e2.toString());
            }
        }
    }

    public static String hashSHA1(String original) {
        String str = null;
        if (TextUtils.isEmpty(original)) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA1");
            digest.update(original.getBytes());
            str = Base64.encodeToString(digest.digest(), 8).substring(0, 16);
            return str;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return str;
        }
    }

    private void updateWaterMarkerAccount() {
        final Account account = loadAccountId();
        new Thread() {
            public void run() {
                try {
                    synchronized (this) {
                        for (int time = 10; time > 0; time--) {
                            MiuiSecurityPermissionHandler.this.mRetWater = MiuiSecurityPermissionHandler.this.postVerificationWaterMarker(account == null ? null : account.name);
                            if (-2 != MiuiSecurityPermissionHandler.this.mRetWater) {
                                break;
                            }
                            Log.d(MiuiSecurityPermissionHandler.TAG, " updateWaterMarkerAccount postVerificationWaterMarker NETWORK_ERROR!");
                            wait(AnrMonitor.MESSAGE_EXECUTION_TIMEOUT);
                        }
                        String access$100 = MiuiSecurityPermissionHandler.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(" updateWaterMarkerAccount! mRetWater:");
                        stringBuilder.append(MiuiSecurityPermissionHandler.this.mRetWater);
                        Log.d(access$100, stringBuilder.toString());
                        if (-2 != MiuiSecurityPermissionHandler.this.mRetWater) {
                            if (MiuiSecurityPermissionHandler.this.mRetWater != 0) {
                                MiuiSecurityPermissionHandler.this.mPermissionViewCallback.onHideWaterMarker();
                            }
                        }
                        MiuiSecurityPermissionHandler.this.mPermissionViewCallback.onShowWaterMarker();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void handleAccountLogin() {
        this.mPermissionViewCallback.onUnListenAccount(1);
        int i = this.mRetWater;
        if (i == -2) {
            updateWaterMarkerAccount();
        } else if (i == 0) {
            updateWaterMarkerAccount();
        } else if (i == 1) {
            updateWaterMarkerAccount();
        } else if (i == 2 || i != 3) {
        }
        if (Build.IS_PRIVATE_BUILD) {
            i = this.mRetPermission;
            if (i == -2) {
                createPermissionView();
            } else if (i == -1) {
                createPermissionView();
            } else if (i == 1) {
                createPermissionView();
            }
        }
    }

    public void handleAccountLogout() {
        if (Build.IS_PRIVATE_BUILD) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("handleAccountLogout mRetPermission:");
            stringBuilder.append(this.mRetPermission);
            Log.d(str, stringBuilder.toString());
            try {
                postVerificationResult(null, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            int i = this.mRetPermission;
            if (i == -2) {
                this.mPermissionViewCallback.onListenAccount(1);
            } else if (i == -1) {
                this.mPermissionViewCallback.onListenAccount(1);
            } else if (i == 1) {
                this.mPermissionViewCallback.onListenAccount(1);
            }
        }
    }

    public void handleWifiSettingFinish() {
        if (Build.IS_PRIVATE_BUILD) {
            Log.d(TAG, "handleWifiSettingFinish!");
            this.mPermissionViewCallback.onUnListenAccount(2);
            createPermissionView();
        }
    }

    public void registerPermissionViewCallback(PermissionViewCallback callback) {
        this.mPermissionViewCallback = callback;
    }
}
