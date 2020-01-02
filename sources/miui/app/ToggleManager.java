package miui.app;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.MobileDataUtils;
import android.app.StatusBarManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SyncStatusObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera.Parameters;
import android.location.LocationManager;
import android.media.AudioManager;
import android.miui.R;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkPolicyManager;
import android.net.NetworkTemplate;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiSsid;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IPowerManager;
import android.os.IPowerManager.Stub;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.Vibrator;
import android.preference.PreferenceActivity;
import android.provider.MiuiSettings;
import android.provider.MiuiSettings.AntiSpam;
import android.provider.MiuiSettings.ScreenEffect;
import android.provider.MiuiSettings.SilenceMode;
import android.provider.MiuiSettings.System;
import android.provider.Settings;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import android.view.IWindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.miui.enterprise.RestrictionsHelper;
import com.miui.internal.search.SettingsTree;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import miui.os.Build;
import miui.os.DeviceFeature;
import miui.os.Environment;
import miui.provider.ExtraTelephony;
import miui.provider.ExtraTelephony.QuietModeEnableListener;
import miui.securityspace.CrossUserUtils;
import miui.telephony.TelephonyManager;
import miui.util.AudioManagerHelper;
import miui.util.FeatureParser;
import miui.util.ObjectReference;
import miui.util.ReflectionUtils;

public class ToggleManager {
    public static final int ALPHA_DEFAULT = 255;
    public static final int ALPHA_HALF = 125;
    public static final String AUTO_BRIGHTNESS_OPTIMIZE_STRATEGY = FeatureParser.getString("auto_brightness_optimize_strategy");
    public static final String COMPONENT_NAME_MIDROP_STARTUP_RECEIVER = "com.xiaomi.midrop.startup.StartupReceiver";
    public static final int DEFAULT_BACKLIGHT = ((int) (((float) PowerManager.BRIGHTNESS_ON) * 0.4f));
    private static final int EXECUTE_TOGGLE_BLUETOOTH_DELAY_TIME = 500;
    private static final String KEY_POWER_MODE_OPEN = "POWER_SAVE_MODE_OPEN";
    public static final int MAXIMUM_BACKLIGHT = PowerManager.BRIGHTNESS_ON;
    public static final String META_DATA_KEY_FRAGMENT_CLASS = "com.android.settings.FRAGMENT_CLASS";
    private static final String METHOD_CHANGE_POWER_MODE = "changePowerMode";
    public static final int MINIMUM_BACKLIGHT = Resources.getSystem().getInteger(R.integer.android_config_screenBrightnessSettingMinimum);
    public static final String MIUI_BRIGHTNESS_OPT_STRATEGY = "miui";
    private static final int MSG_UPDATE_SYNC_TOGGLE = 2;
    private static final int MSG_VERIFY_BLUETOOTH_STATE = 1;
    public static final String PINECONE_BRIGHTNESS_OPT_STRATEGY = "pinecone";
    public static final String PKG_NAME_MIDROP = "com.xiaomi.midrop";
    private static final String POWER_MODE_URI_PATH = "content://com.miui.powercenter.powersaver";
    private static final String PROCESS_NAME_SYSTEM_UI = "com.android.systemui";
    public static final int RANGE = (MAXIMUM_BACKLIGHT - MINIMUM_BACKLIGHT);
    private static final String SETTINGS_MIDROP = "key_midrop_enabled";
    public static final boolean SUPPORT_AUTO_BRIGHTNESS_OPTIMIZE = DeviceFeature.SUPPORT_AUTO_BRIGHTNESS_OPTIMIZE;
    static final String TAG = "ToggleManager";
    public static final int TOGGLE_ADVANCED_SYNC = 19;
    public static final int TOGGLE_AUTO_BRIGHTNESS = 22;
    public static final int TOGGLE_BATTERY_SAVER = 30;
    public static final int TOGGLE_BLUETOOTH = 2;
    public static final int TOGGLE_BRIGHTNESS = 4;
    public static final int TOGGLE_CAST = 28;
    public static final int TOGGLE_COUNT = 32;
    public static final int TOGGLE_DATA = 1;
    public static final int TOGGLE_DIVIDER = 0;
    public static final int TOGGLE_DRIVE_MODE = 21;
    public static final int TOGGLE_EDIT = 29;
    public static final int TOGGLE_FLIGHT_MODE = 9;
    public static final int TOGGLE_GPS = 7;
    public static final int TOGGLE_LOCK = 10;
    public static final int TOGGLE_MIDROP = 27;
    public static final int TOGGLE_NETWORK_TYPE = 17;
    public static final int TOGGLE_PAPER_MODE = 26;
    public static final int TOGGLE_POWER_MODE = 23;
    public static final int TOGGLE_QUIET_MODE = 25;
    public static final int TOGGLE_REBOOT = 12;
    public static final int TOGGLE_RINGER = 5;
    public static final int TOGGLE_ROTATE = 3;
    public static final int TOGGLE_SCREENSHOT = 18;
    public static final int TOGGLE_SCREEN_BUTTON = 20;
    public static final int TOGGLE_SHUTDOWN = 13;
    public static final int TOGGLE_SYNC = 8;
    public static final int TOGGLE_TORCH = 11;
    public static final int TOGGLE_VIBRATE = 6;
    public static final int TOGGLE_WIFI = 15;
    public static final int TOGGLE_WIFI_AP = 24;
    public static final boolean USE_SCREEN_AUTO_BRIGHTNESS_ADJUSTMENT = SystemProperties.getBoolean("persist.power.useautobrightadj", true);
    private static final int VERIFY_BLUETOOTH_STATE_DELAY_TIME = 1000;
    private static WifiApEnabler mWifiApEnabler;
    private static WifiManager mWifiManager;
    private static int sCurrentUserId = 0;
    private static volatile boolean sHasCast = false;
    private static volatile boolean sHasGpsFeature = false;
    private static boolean sHasMiDrop;
    private static volatile boolean sHasMobileData = false;
    private static volatile boolean sHasVibrator = false;
    private static int[] sLongClickActions = new int[32];
    private static final int[] sRemoveByMultiUserList = new int[]{24, 27};
    private static volatile boolean sStaticFieldsInited = false;
    private static boolean[] sToggleDisabled = new boolean[32];
    private static int[] sToggleGeneralImages = new int[32];
    private static int[] sToggleImages = new int[32];
    private static ToggleManager sToggleManager = null;
    private static int[] sToggleNames = new int[32];
    private static int[] sToggleOffImages = new int[32];
    private static int[] sToggleOnImages = new int[32];
    private static boolean[] sToggleStatus = new boolean[32];
    private static HashMap<Integer, Object> sToggleStatusNames = new HashMap();
    private static HashMap<String, Integer> sToggleStringToId = new HashMap();
    private static volatile boolean sWifiApAvailable = false;
    private boolean mAccelerometer;
    private final ContentObserver mAccelerometerRotationObserver;
    private boolean mBatterySaveMode;
    private final ContentObserver mBatterySaverObserver;
    private Handler mBgHandler;
    private HandlerThread mBgThread;
    private BluetoothAdapter mBluetoothAdapter = null;
    private boolean mBluetoothDelay;
    private boolean mBluetoothEnable;
    private boolean mBrightnessAutoAvailable;
    private float mBrightnessAutoLevel;
    private boolean mBrightnessAutoMode;
    private int mBrightnessManualLevel;
    private final ContentObserver mBrightnessObserver;
    private BroadcastReceiver mBroadcastReceiver;
    private Context mContext;
    private boolean mFlightMode;
    private final ContentObserver mFlightModeObserver;
    private boolean mGpsEnable;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int i = msg.what;
            boolean z = true;
            if (i == 1) {
                ToggleManager.this.verifyBluetoothState();
            } else if (i == 2) {
                ToggleManager toggleManager = ToggleManager.this;
                if (msg.arg1 != 1) {
                    z = false;
                }
                toggleManager.updateSyncToggle(z);
            }
        }
    };
    private boolean mIsSimMissing;
    private final ContentObserver mLocationAllowedObserver;
    private final LocationManager mLocationManager;
    private boolean mMiDropChanging;
    private final ContentObserver mMiDropObserver;
    private boolean mMobileDataEnable;
    private final ContentObserver mMobileDataEnableObserver;
    private MobileDataUtils mMobileDataUtils;
    private boolean mMobilePolicyEnable;
    private final ContentObserver mMobilePolicyEnableObserver;
    private BroadcastReceiver mPackageChangeReceiver;
    private boolean mPaperMode;
    private final ContentObserver mPaperModeObserver;
    private String mPowerMode;
    private final ContentObserver mPowerModeObserver;
    private boolean mQuietMode;
    private final QuietModeEnableListener mQuietModeObserver;
    private final ContentResolver mResolver;
    private final Resources mResource;
    private boolean mScreenButtonDisabled;
    private final ContentObserver mScreenButtonStateObserver;
    private Object mStatusChangeListenerHandle;
    private final SyncStatusObserver mSyncStatusObserver;
    private final ContentObserver mTogglOrderObserver;
    private List<WeakReference<OnToggleChangedListener>> mToggleChangedListener;
    private List<OnToggleOrderChangedListener> mToggleOrderChangedListener;
    private boolean mTorchEnable;
    private final ContentObserver mTorchEnableObserver;
    private final Runnable mUpdateSyncStateRunnable;
    private final ContentObserver mVibrateEnableObserver;
    boolean mWifiChanging;
    boolean mWifiConnected;
    boolean mWifiEnable;
    String mWifiSsid;
    private int mZenMode;

    public interface OnToggleChangedListener {
        void OnToggleChanged(int i);
    }

    public interface OnToggleOrderChangedListener {
        void OnToggleOrderChanged();
    }

    private final class WorkHandler extends Handler {
        private static final int MSG_APPLY_BRIGHTNESS = 3;
        private static final int MSG_TOGGLE_BLUETOOTH = 2;
        private static final int MSG_TOGGLE_SYNC = 5;
        private static final int MSG_TOGGLE_WIFI = 1;
        private static final int MSG_UPDATE_DATA = 6;
        private static final int MSG_UPDATE_SYNC = 4;

        public WorkHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            boolean z = false;
            ToggleManager toggleManager;
            switch (msg.what) {
                case 1:
                    WifiManager access$200 = ToggleManager.mWifiManager;
                    if (msg.arg1 == 1) {
                        z = true;
                    }
                    access$200.setWifiEnabled(z);
                    return;
                case 2:
                    toggleManager = ToggleManager.this;
                    if (msg.arg1 == 1) {
                        z = true;
                    }
                    toggleManager.toggleBluetooth(z);
                    return;
                case 3:
                    toggleManager = ToggleManager.this;
                    int i = msg.arg1;
                    if (msg.arg2 == 1) {
                        z = true;
                    }
                    toggleManager.applyBrightnessIntenal(i, z);
                    return;
                case 4:
                    ToggleManager.this.mHandler.obtainMessage(2, ToggleManager.this.isSyncOn(), 0).sendToTarget();
                    return;
                case 5:
                    ToggleManager.this.toggleSyncIntenal();
                    return;
                case 6:
                    ToggleManager.this.updateDataToggle();
                    return;
                default:
                    return;
            }
        }
    }

    static {
        int i;
        sToggleStringToId.put("bluetooth", Integer.valueOf(2));
        sToggleStringToId.put("brightness_mode", Integer.valueOf(22));
        sToggleStringToId.put("data", Integer.valueOf(1));
        sToggleStringToId.put("flight_mode", Integer.valueOf(9));
        sToggleStringToId.put(LocationManager.GPS_PROVIDER, Integer.valueOf(7));
        sToggleStringToId.put("lock", Integer.valueOf(10));
        sToggleStringToId.put(System.POWER_MODE, Integer.valueOf(23));
        sToggleStringToId.put("quiet_mode", Integer.valueOf(25));
        sToggleStringToId.put("rotate", Integer.valueOf(3));
        sToggleStringToId.put("ringer_mode", Integer.valueOf(5));
        sToggleStringToId.put("screenshot", Integer.valueOf(18));
        sToggleStringToId.put("screen_button", Integer.valueOf(20));
        sToggleStringToId.put("sync_mode", Integer.valueOf(8));
        sToggleStringToId.put(Parameters.FLASH_MODE_TORCH, Integer.valueOf(11));
        sToggleStringToId.put("vibration_mode", Integer.valueOf(6));
        sToggleStringToId.put("wifi", Integer.valueOf(15));
        sToggleStringToId.put("wifi_ap", Integer.valueOf(24));
        sToggleStringToId.put("paper_mode", Integer.valueOf(26));
        sToggleStringToId.put("midrop", Integer.valueOf(27));
        sToggleStringToId.put("cast", Integer.valueOf(28));
        sToggleStringToId.put("battery_safer", Integer.valueOf(30));
        sToggleStringToId.put("edit", Integer.valueOf(29));
        int[] iArr = sToggleNames;
        iArr[22] = R.string.status_bar_toggle_auto_brightness;
        iArr[2] = R.string.status_bar_toggle_bluetooth;
        iArr[1] = R.string.status_bar_toggle_data;
        iArr[9] = R.string.status_bar_toggle_flight_mode;
        iArr[7] = R.string.status_bar_toggle_gps;
        iArr[10] = R.string.status_bar_toggle_lock;
        iArr[5] = R.string.status_bar_toggle_mute;
        iArr[23] = R.string.status_bar_toggle_power_mode;
        iArr[3] = R.string.status_bar_toggle_rotate;
        iArr[20] = R.string.status_bar_toggle_screen_button;
        iArr[18] = R.string.status_bar_toggle_screenshot;
        iArr[8] = R.string.status_bar_toggle_sync;
        iArr[11] = R.string.status_bar_toggle_torch;
        iArr[6] = R.string.status_bar_toggle_vibrate;
        iArr[15] = R.string.status_bar_toggle_wifi;
        iArr[24] = R.string.status_bar_toggle_wifi_ap;
        iArr[25] = R.string.status_bar_toggle_quiet_mode;
        iArr[26] = R.string.status_bar_toggle_paper_mode;
        iArr[27] = R.string.status_bar_toggle_midrop;
        iArr[28] = R.string.status_bar_toggle_cast;
        iArr[30] = R.string.status_bar_toggle_battery_saver;
        iArr[29] = R.string.status_bar_toggle_edit;
        for (int i2 = 0; i2 < 32; i2++) {
            sToggleStatusNames.put(Integer.valueOf(i2), Integer.valueOf(sToggleNames[i2]));
        }
        int[] iArr2 = sLongClickActions;
        iArr2[22] = R.string.status_bar_toggle_brightness_action;
        iArr2[1] = R.string.status_bar_toggle_data_action;
        iArr2[2] = R.string.status_bar_toggle_bluetooth_action;
        iArr2[9] = R.string.status_bar_toggle_flight_mode_action;
        iArr2[7] = R.string.status_bar_toggle_gps_action;
        iArr2[5] = R.string.status_bar_toggle_mute_action;
        iArr2[23] = R.string.status_bar_toggle_power_mode_action;
        iArr2[3] = R.string.status_bar_toggle_rotate_action;
        iArr2[8] = R.string.status_bar_toggle_sync_action;
        iArr2[6] = R.string.status_bar_toggle_vibrate_action;
        iArr2[15] = R.string.status_bar_toggle_wifi_action;
        iArr2[24] = R.string.status_bar_toggle_wifi_ap_action;
        iArr2[25] = R.string.status_bar_toggle_quiet_mode_action;
        iArr2[26] = R.string.status_bar_toggle_paper_mode_action;
        iArr2[27] = R.string.status_bar_toggle_midrop_action;
        iArr2[30] = R.string.status_bar_toggle_battery_saver_action;
        iArr2 = sToggleImages;
        iArr2[22] = R.drawable.status_bar_toggle_brightness_auto;
        iArr2[2] = R.drawable.status_bar_toggle_bluetooth_on;
        iArr2[1] = R.drawable.status_bar_toggle_data_on;
        iArr2[9] = R.drawable.status_bar_toggle_flight_mode_on;
        iArr2[7] = R.drawable.status_bar_toggle_gps_on;
        iArr2[5] = R.drawable.status_bar_toggle_mute_on;
        iArr2[23] = R.drawable.status_bar_toggle_power_high_on;
        iArr2[3] = R.drawable.status_bar_toggle_rotate_lock_on;
        iArr2[20] = R.drawable.status_bar_toggle_screen_button_disabled;
        iArr2[8] = R.drawable.status_bar_toggle_sync_on;
        iArr2[11] = R.drawable.status_bar_toggle_torch_on;
        iArr2[6] = R.drawable.status_bar_toggle_vibrate_on;
        iArr2[15] = R.drawable.status_bar_toggle_wifi_on;
        iArr2[24] = R.drawable.status_bar_toggle_wifi_ap_on;
        iArr2[25] = R.drawable.status_bar_toggle_quiet_mode_on;
        iArr2[26] = R.drawable.status_bar_toggle_paper_mode_on;
        iArr2[27] = R.drawable.status_bar_toggle_midrop_on;
        iArr2[30] = R.drawable.status_bar_toggle_battery_saver_on;
        iArr2[10] = R.drawable.status_bar_toggle_lock;
        iArr2[18] = R.drawable.status_bar_toggle_screenshot;
        iArr2[28] = R.drawable.status_bar_toggle_cast_off;
        iArr2[29] = R.drawable.status_bar_toggle_edit_on;
        for (i = 0; i < 32; i++) {
            sToggleGeneralImages[i] = sToggleImages[i];
        }
        iArr2 = sToggleGeneralImages;
        iArr2[10] = R.drawable.status_bar_toggle_lock_on;
        iArr2[18] = R.drawable.status_bar_toggle_screenshot_on;
        iArr2[28] = R.drawable.status_bar_toggle_cast_on;
        for (i = 0; i < 32; i++) {
            sToggleOnImages[i] = sToggleImages[i];
        }
        iArr2 = sToggleOffImages;
        iArr2[22] = R.drawable.status_bar_toggle_brightness_manual;
        iArr2[2] = R.drawable.status_bar_toggle_bluetooth_off;
        iArr2[1] = R.drawable.status_bar_toggle_data_off;
        iArr2[9] = R.drawable.status_bar_toggle_flight_mode_off;
        iArr2[7] = R.drawable.status_bar_toggle_gps_off;
        iArr2[5] = R.drawable.status_bar_toggle_mute_off;
        iArr2[23] = R.drawable.status_bar_toggle_power_high_off;
        iArr2[3] = R.drawable.status_bar_toggle_rotate_lock_off;
        iArr2[20] = R.drawable.status_bar_toggle_screen_button_enabled;
        iArr2[8] = R.drawable.status_bar_toggle_sync_off;
        iArr2[11] = R.drawable.status_bar_toggle_torch_off;
        iArr2[6] = R.drawable.status_bar_toggle_vibrate_off;
        iArr2[15] = R.drawable.status_bar_toggle_wifi_off;
        iArr2[24] = R.drawable.status_bar_toggle_wifi_ap_off;
        iArr2[25] = R.drawable.status_bar_toggle_quiet_mode_off;
        iArr2[26] = R.drawable.status_bar_toggle_paper_mode_off;
        iArr2[27] = R.drawable.status_bar_toggle_midrop_off;
        iArr2[30] = R.drawable.status_bar_toggle_battery_saver_off;
        iArr2[10] = R.drawable.status_bar_toggle_lock;
        iArr2[18] = R.drawable.status_bar_toggle_screenshot;
        iArr2[28] = R.drawable.status_bar_toggle_cast_off;
        iArr2[29] = R.drawable.status_bar_toggle_edit_off;
    }

    public static ToggleManager createInstance(Context context) {
        return createInstance(context, UserHandle.myUserId());
    }

    public static ToggleManager createInstance(Context context, int userId) {
        if (sToggleManager == null) {
            sToggleManager = new ToggleManager(context.getApplicationContext(), userId);
        }
        return sToggleManager;
    }

    private ToggleManager(Context context, int userId) {
        boolean wifiAvailable = false;
        this.mIsSimMissing = false;
        this.mPackageChangeReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                boolean equals = Intent.ACTION_PACKAGE_ADDED.equals(action);
                String str = ToggleManager.PKG_NAME_MIDROP;
                if (equals) {
                    if (!ToggleManager.sHasMiDrop && str.equals(intent.getData().getSchemeSpecificPart())) {
                        ToggleManager.sHasMiDrop = true;
                    }
                } else if (Intent.ACTION_PACKAGE_REMOVED.equals(action) && ToggleManager.sHasMiDrop && str.equals(intent.getData().getSchemeSpecificPart())) {
                    ToggleManager.sHasMiDrop = false;
                }
            }
        };
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action) || WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action) || Intent.ACTION_LOCALE_CHANGED.equals(action)) {
                    ToggleManager.this.updateWifiToggle(intent);
                } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                    if (!ToggleManager.this.mBgHandler.hasMessages(2)) {
                        ToggleManager.this.updateBluetoothToggle();
                    }
                } else if (AudioManager.RINGER_MODE_CHANGED_ACTION.equals(action)) {
                    ToggleManager.this.updateRingerToggle();
                    ToggleManager.this.updateVibrateToggle();
                } else if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(action)) {
                    ToggleManager.this.updateFlightModeToggle();
                } else if (action.equals("android.intent.action.SIM_STATE_CHANGED")) {
                    ToggleManager.this.mIsSimMissing = TelephonyManager.getDefault().hasIccCard() ^ 1;
                    ToggleManager.this.postUpdateDataToggleAsync();
                }
            }
        };
        this.mTogglOrderObserver = new ContentObserver(this.mHandler) {
            public void onChange(boolean selfChange) {
                if (ToggleManager.this.mToggleOrderChangedListener.size() > 0) {
                    for (OnToggleOrderChangedListener toggleOrderChangedListener : ToggleManager.this.mToggleOrderChangedListener) {
                        toggleOrderChangedListener.OnToggleOrderChanged();
                    }
                }
            }
        };
        this.mFlightModeObserver = new ContentObserver(this.mHandler) {
            public void onChange(boolean selfChange) {
                ToggleManager.this.updateFlightModeToggle();
            }
        };
        this.mMobileDataEnableObserver = new ContentObserver(this.mHandler) {
            public void onChange(boolean selfChange) {
                ToggleManager.this.mMobileDataUtils.onMobileDataChange(ToggleManager.this.mContext);
                ToggleManager.this.postUpdateDataToggleAsync();
            }
        };
        this.mMobilePolicyEnableObserver = new ContentObserver(this.mHandler) {
            public void onChange(boolean selfChange) {
                ToggleManager toggleManager = ToggleManager.this;
                boolean z = true;
                if (Secure.getIntForUser(toggleManager.mResolver, MiuiSettings.Secure.MOBILE_POLICY, 1, ToggleManager.sCurrentUserId) != 1) {
                    z = false;
                }
                toggleManager.mMobilePolicyEnable = z;
                ToggleManager.this.postUpdateDataToggleAsync();
            }
        };
        this.mTorchEnableObserver = new ContentObserver(this.mHandler) {
            public void onChange(boolean selfChange) {
                ToggleManager.this.updateTorchToggle();
            }
        };
        this.mScreenButtonStateObserver = new ContentObserver(this.mHandler) {
            public void onChange(boolean selfChange) {
                ToggleManager.this.updateScreenButtonState();
            }
        };
        this.mLocationAllowedObserver = new ContentObserver(this.mHandler) {
            public void onChange(boolean selfChange) {
                ToggleManager.this.updateGpsToggle();
            }
        };
        this.mAccelerometerRotationObserver = new ContentObserver(this.mHandler) {
            public void onChange(boolean selfChange) {
                ToggleManager.this.updateAccelerometerToggle();
            }
        };
        this.mBrightnessObserver = new ContentObserver(this.mHandler) {
            public void onChange(boolean selfChange) {
                ToggleManager.this.queryBrightnessStatus();
                ToggleManager.this.updateBrightnessToggle();
            }
        };
        this.mPowerModeObserver = new ContentObserver(this.mHandler) {
            public void onChange(boolean selfChange) {
                ToggleManager.this.updatePowerModeToggle();
            }
        };
        this.mBatterySaverObserver = new ContentObserver(this.mHandler) {
            public void onChange(boolean selfChange) {
                ToggleManager.this.updateBatterySaverToggle();
            }
        };
        this.mQuietModeObserver = new QuietModeEnableListener() {
            public void onQuietModeEnableChange(boolean isMode) {
                ToggleManager.this.updateQuietModeToggle();
            }
        };
        this.mPaperModeObserver = new ContentObserver(this.mHandler) {
            public void onChange(boolean selfChange) {
                ToggleManager.this.updatePaperModeToggle();
            }
        };
        this.mMiDropObserver = new ContentObserver(this.mHandler) {
            public void onChange(boolean selfChange) {
                ToggleManager.this.updateMiDropToggle();
            }
        };
        this.mVibrateEnableObserver = new ContentObserver(this.mHandler) {
            public void onChange(boolean selfChange) {
                ToggleManager.this.updateVibrateToggle();
            }
        };
        this.mUpdateSyncStateRunnable = new Runnable() {
            public void run() {
                ToggleManager.this.updateSyncToggle();
            }
        };
        this.mSyncStatusObserver = new SyncStatusObserver() {
            public void onStatusChanged(int which) {
                ToggleManager.this.mHandler.removeCallbacks(ToggleManager.this.mUpdateSyncStateRunnable);
                ToggleManager.this.mHandler.postDelayed(ToggleManager.this.mUpdateSyncStateRunnable, 300);
            }
        };
        this.mWifiEnable = false;
        this.mWifiConnected = false;
        this.mWifiChanging = false;
        this.mWifiSsid = null;
        this.mBluetoothDelay = false;
        this.mContext = context;
        this.mBgThread = new HandlerThread(TAG, 10);
        this.mBgThread.start();
        this.mBgHandler = new WorkHandler(this.mBgThread.getLooper());
        sCurrentUserId = userId;
        this.mResolver = this.mContext.getContentResolver();
        this.mResource = this.mContext.getResources();
        this.mToggleChangedListener = new ArrayList();
        this.mToggleOrderChangedListener = new ArrayList();
        this.mMobileDataUtils = MobileDataUtils.getInstance();
        mWifiManager = (WifiManager) this.mContext.getSystemService("wifi");
        this.mLocationManager = (LocationManager) context.getSystemService("location");
        ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        this.mBrightnessAutoAvailable = this.mResource.getBoolean(R.bool.android_config_automatic_brightness_available);
        if (connectivityManager.getTetherableWifiRegexs().length != 0) {
            wifiAvailable = true;
        }
        if (wifiAvailable) {
            mWifiApEnabler = new WifiApEnabler(context, this);
        }
        registerListener(PROCESS_NAME_SYSTEM_UI.equals(context.getApplicationInfo().packageName));
        if (SilenceMode.isSupported) {
            sLongClickActions[25] = R.string.status_bar_toggle_silent_mode_action;
        }
    }

    private void registerListener(boolean isSystemUI) {
        int userId = isSystemUI ? -1 : UserHandle.myUserId();
        UserHandle user = isSystemUI ? UserHandle.ALL : new UserHandle(userId);
        IntentFilter filter = new IntentFilter();
        filter.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        filter.addAction(Intent.ACTION_LOCALE_CHANGED);
        filter.addAction("android.intent.action.SIM_STATE_CHANGED");
        this.mContext.registerReceiverAsUser(this.mBroadcastReceiver, user, filter, null, null);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        this.mContext.registerReceiverAsUser(this.mPackageChangeReceiver, user, intentFilter, null, null);
        this.mResolver.registerContentObserver(Settings.System.getUriFor(System.STATUS_BAR_TOGGLE_LIST), false, this.mTogglOrderObserver, userId);
        this.mResolver.registerContentObserver(Settings.System.getUriFor(System.STATUS_BAR_TOGGLE_PAGE), false, this.mTogglOrderObserver, userId);
        this.mStatusChangeListenerHandle = ContentResolver.addStatusChangeListener(Integer.MAX_VALUE, this.mSyncStatusObserver);
        this.mResolver.registerContentObserver(Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION), false, this.mAccelerometerRotationObserver, userId);
        this.mResolver.registerContentObserver(Global.getUriFor("airplane_mode_on"), false, this.mFlightModeObserver);
        this.mResolver.registerContentObserver(Secure.getUriFor("location_providers_allowed"), false, this.mLocationAllowedObserver, userId);
        this.mMobileDataUtils.registerContentObserver(this.mContext, this.mMobileDataEnableObserver);
        this.mResolver.registerContentObserver(Secure.getUriFor(MiuiSettings.Secure.MOBILE_POLICY), false, this.mMobilePolicyEnableObserver, userId);
        if (!SUPPORT_AUTO_BRIGHTNESS_OPTIMIZE) {
            this.mResolver.registerContentObserver(Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS), false, this.mBrightnessObserver, userId);
            this.mResolver.registerContentObserver(Settings.System.getUriFor(Settings.System.SCREEN_AUTO_BRIGHTNESS_ADJ), false, this.mBrightnessObserver, userId);
        }
        this.mResolver.registerContentObserver(Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE), false, this.mBrightnessObserver, userId);
        this.mResolver.registerContentObserver(Secure.getUriFor("screen_buttons_state"), false, this.mScreenButtonStateObserver, userId);
        this.mResolver.registerContentObserver(Global.getUriFor(System.TORCH_STATE), false, this.mTorchEnableObserver, -1);
        this.mResolver.registerContentObserver(Settings.System.getUriFor("vibrate_in_silent"), false, this.mVibrateEnableObserver, userId);
        this.mResolver.registerContentObserver(Settings.System.getUriFor(System.VIBRATE_IN_NORMAL), false, this.mVibrateEnableObserver, userId);
        this.mResolver.registerContentObserver(Settings.System.getUriFor(System.POWER_MODE), false, this.mPowerModeObserver, userId);
        this.mResolver.registerContentObserver(Settings.System.getUriFor(KEY_POWER_MODE_OPEN), false, this.mBatterySaverObserver, userId);
        ExtraTelephony.registerQuietModeEnableListener(this.mContext, this.mQuietModeObserver);
        this.mResolver.registerContentObserver(Settings.System.getUriFor(ScreenEffect.SCREEN_PAPER_MODE_ENABLED), false, this.mPaperModeObserver, userId);
        this.mResolver.registerContentObserver(Global.getUriFor("key_midrop_enabled"), false, this.mMiDropObserver, userId);
        updateAllToggles(sCurrentUserId);
    }

    public void updateAllToggles(int userId) {
        sCurrentUserId = userId;
        queryBrightnessStatus();
        updateBluetoothToggle();
        updateRingerToggle();
        updateWifiToggle(null);
        updateSyncToggle();
        updateAccelerometerToggle();
        updateFlightModeToggle();
        updateGpsToggle();
        this.mMobilePolicyEnableObserver.onChange(true);
        updateBrightnessToggle();
        updateScreenButtonState();
        updateTorchToggle();
        updateVibrateToggle();
        updatePowerModeToggle();
        updateBatterySaverToggle();
        updateQuietModeToggle();
        updatePaperModeToggle();
        updateMiDropToggle();
    }

    public void onDestroy() {
        this.mContext.unregisterReceiver(this.mBroadcastReceiver);
        this.mContext.unregisterReceiver(this.mPackageChangeReceiver);
        this.mResolver.unregisterContentObserver(this.mTogglOrderObserver);
        this.mResolver.unregisterContentObserver(this.mMobileDataEnableObserver);
        this.mResolver.unregisterContentObserver(this.mMobilePolicyEnableObserver);
        this.mResolver.unregisterContentObserver(this.mTorchEnableObserver);
        this.mResolver.unregisterContentObserver(this.mScreenButtonStateObserver);
        this.mResolver.unregisterContentObserver(this.mLocationAllowedObserver);
        this.mResolver.unregisterContentObserver(this.mAccelerometerRotationObserver);
        this.mResolver.unregisterContentObserver(this.mBrightnessObserver);
        this.mResolver.unregisterContentObserver(this.mVibrateEnableObserver);
        this.mResolver.unregisterContentObserver(this.mPowerModeObserver);
        this.mResolver.unregisterContentObserver(this.mBatterySaverObserver);
        ExtraTelephony.unRegisterQuietModeEnableListener(this.mContext, this.mQuietModeObserver);
        this.mResolver.unregisterContentObserver(this.mPaperModeObserver);
        ContentResolver.removeStatusChangeListener(this.mStatusChangeListenerHandle);
        this.mToggleChangedListener.clear();
        this.mToggleOrderChangedListener.clear();
        WifiApEnabler wifiApEnabler = mWifiApEnabler;
        if (wifiApEnabler != null) {
            wifiApEnabler.unregisterReceiver();
        }
        this.mBgThread.quitSafely();
    }

    public static boolean isValid(Context context, int id) {
        return id >= 0 && id < 32 && getName(id) != 0;
    }

    public static ArrayList<Integer> getUserSelectedToggleOrder(Context context) {
        return getUserSelectedToggleOrder(context, getUserId(context));
    }

    public static ArrayList<Integer> getUserSelectedToggleOrder(Context context, boolean listStyle) {
        return getUserSelectedToggleOrder(context, listStyle, getUserId(context));
    }

    public static ArrayList<Integer> getUserSelectedToggleOrder(Context context, int userId) {
        return getUserSelectedToggleOrder(context, isListStyle(context, userId), userId);
    }

    public static ArrayList<Integer> getUserSelectedToggleOrder(Context context, boolean listStyle, int userId) {
        String settingID;
        ArrayList<Integer> result = new ArrayList();
        if (listStyle) {
            settingID = System.STATUS_BAR_TOGGLE_LIST;
        } else {
            settingID = System.STATUS_BAR_TOGGLE_PAGE;
        }
        String toggleList = Settings.System.getStringForUser(context.getContentResolver(), settingID, userId);
        if (!TextUtils.isEmpty(toggleList)) {
            String[] toggles = toggleList.split(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            int i = 0;
            while (i < toggles.length) {
                try {
                    int id = Integer.valueOf(toggles[i]).intValue();
                    if (getName(id) != 0) {
                        result.add(Integer.valueOf(id));
                    }
                    i++;
                } catch (Exception e) {
                    result.clear();
                }
            }
        }
        validateToggleOrder(context, result, listStyle, userId);
        return result;
    }

    public static ArrayList<Integer> getDefaultToggleOrder(Context context) {
        int userId = getUserId(context);
        ArrayList<Integer> result = new ArrayList();
        validateToggleOrder(context, result, isListStyle(context, userId), userId);
        return result;
    }

    public static ArrayList<Integer> getAllToggles(Context context) {
        ArrayList<Integer> list = new ArrayList(31);
        list.add(Integer.valueOf(9));
        list.add(Integer.valueOf(2));
        list.add(Integer.valueOf(25));
        list.add(Integer.valueOf(18));
        list.add(Integer.valueOf(3));
        list.add(Integer.valueOf(6));
        list.add(Integer.valueOf(5));
        list.add(Integer.valueOf(7));
        list.add(Integer.valueOf(26));
        list.add(Integer.valueOf(15));
        list.add(Integer.valueOf(1));
        list.add(Integer.valueOf(22));
        list.add(Integer.valueOf(11));
        list.add(Integer.valueOf(20));
        list.add(Integer.valueOf(10));
        list.add(Integer.valueOf(23));
        list.add(Integer.valueOf(24));
        list.add(Integer.valueOf(8));
        list.add(Integer.valueOf(27));
        list.add(Integer.valueOf(28));
        list.add(Integer.valueOf(30));
        filterToggle(context, list, getUserId(context));
        return list;
    }

    public void setUserSelectedToggleOrder(ArrayList<Integer> list) {
        setUserSelectedToggleOrderStatic(this.mContext, list);
    }

    public static void setUserSelectedToggleOrderStatic(Context ctx, ArrayList<Integer> list) {
        setUserSelectedToggleOrderStatic(ctx, list, isListStyle(ctx));
    }

    public static void setUserSelectedToggleOrderStatic(Context ctx, ArrayList<Integer> list, boolean isListStyle) {
        String settingId;
        if (isListStyle) {
            settingId = System.STATUS_BAR_TOGGLE_LIST;
        } else {
            settingId = System.STATUS_BAR_TOGGLE_PAGE;
        }
        validateToggleOrder(ctx, list, isListStyle, getUserId(ctx));
        StringBuilder toggleList = new StringBuilder(96);
        for (int i = 0; i < list.size(); i++) {
            toggleList.append(((Integer) list.get(i)).toString());
            toggleList.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        }
        Settings.System.putStringForUser(ctx.getContentResolver(), settingId, toggleList.toString(), getUserId(ctx));
    }

    public static boolean isListStyle(Context context) {
        return isListStyle(context, getUserId(context));
    }

    public static boolean isListStyle(Context context, int userId) {
        return Settings.System.getIntForUser(context.getContentResolver(), System.STATUS_BAR_STYLE, 0, userId) == 0;
    }

    private static int getUserId(Context context) {
        return PROCESS_NAME_SYSTEM_UI.equals(context.getApplicationInfo().packageName) ? sCurrentUserId : UserHandle.myUserId();
    }

    public static int getEditFixedPosition(Context context) {
        return context.getResources().getInteger(R.integer.edit_fixed_position);
    }

    private static String getToggleOrderSettingID(Context context) {
        if (isListStyle(context)) {
            return System.STATUS_BAR_TOGGLE_LIST;
        }
        return System.STATUS_BAR_TOGGLE_PAGE;
    }

    public static void validateToggleOrder(Context context, ArrayList<Integer> list, boolean isListStyle, int userId) {
        if (isListStyle) {
            validateToggleList(context, list, userId);
        } else {
            validateTogglePage(context, list, userId);
        }
    }

    private static void validateToggleList(Context context, ArrayList<Integer> list, int userId) {
        addIfUnselected(list, 1);
        addIfUnselected(list, 15);
        addIfUnselected(list, 11);
        addIfUnselected(list, 5);
        addIfUnselected(list, 18);
        addIfUnselected(list, 2);
        addIfUnselected(list, 22);
        addIfUnselected(list, 9);
        addIfUnselected(list, 10);
        addIfUnselected(list, 3);
        addIfUnselected(list, 27);
        addIfUnselected(list, 7);
        addIfUnselected(list, 6);
        addIfUnselected(list, 24);
        addIfUnselected(list, 26);
        addIfUnselected(list, 8);
        addIfUnselected(list, 25);
        addIfUnselected(list, 23);
        addIfUnselected(list, 20);
        addIfUnselected(list, 28);
        addIfUnselected(list, 30);
        filterToggle(context, list, userId);
        validateEditPositionInList(context, list);
    }

    private static void validateTogglePage(Context context, ArrayList<Integer> list, int userId) {
        addIfUnselected(list, 5);
        addIfUnselected(list, 3);
        addIfUnselected(list, 10);
        addIfUnselected(list, 2);
        addIfUnselected(list, 1);
        addIfUnselected(list, 15);
        addIfUnselected(list, 27);
        addIfUnselected(list, 22);
        addIfUnselected(list, 11);
        addIfUnselected(list, 9);
        addIfUnselected(list, 18);
        addIfUnselected(list, 6);
        addIfUnselected(list, 28);
        addIfUnselected(list, 26);
        addIfUnselected(list, 25);
        addIfUnselected(list, 7);
        addIfUnselected(list, 20);
        addIfUnselected(list, 23);
        addIfUnselected(list, 24);
        addIfUnselected(list, 8);
        addIfUnselected(list, 30);
        filterToggle(context, list, userId);
        validateEditPositionInPage(context, list);
    }

    private static void filterToggle(Context context, ArrayList<Integer> list, int userId) {
        context = context.getApplicationContext();
        synchronized (ToggleManager.class) {
            if (!sStaticFieldsInited) {
                try {
                    sHasVibrator = ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).hasVibrator();
                    sHasCast = context.getPackageManager().queryIntentActivities(getCastIntent(), 851968).size() > 0;
                    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
                    sWifiApAvailable = connectivityManager.getTetherableWifiRegexs().length != 0;
                    sHasMobileData = connectivityManager.isNetworkSupported(0);
                    sHasGpsFeature = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
                } catch (Exception e) {
                }
                try {
                    sHasMiDrop = context.getPackageManager().getApplicationInfo(PKG_NAME_MIDROP, 0) != null;
                } catch (NameNotFoundException e2) {
                }
                sStaticFieldsInited = true;
            }
        }
        if (!FeatureParser.getBoolean("support_power_mode", false)) {
            list.remove(Integer.valueOf(23));
        }
        if (!sWifiApAvailable) {
            list.remove(Integer.valueOf(24));
        }
        if (!Build.hasCameraFlash(context)) {
            list.remove(Integer.valueOf(11));
        }
        if (!sHasMobileData) {
            list.remove(Integer.valueOf(1));
            list.remove(Integer.valueOf(25));
        }
        if (!sHasVibrator) {
            list.remove(Integer.valueOf(6));
        }
        if (!sHasGpsFeature) {
            list.remove(Integer.valueOf(7));
        }
        if (!FeatureParser.getBoolean("support_screen_paper_mode", false)) {
            list.remove(Integer.valueOf(26));
        }
        if (!sHasMiDrop || Build.IS_INTERNATIONAL_BUILD) {
            list.remove(Integer.valueOf(27));
        }
        if (!sHasCast) {
            list.remove(Integer.valueOf(28));
        }
        if (!(isMiPad() ^ true)) {
            list.remove(Integer.valueOf(30));
        }
        filterToggleByUser(context, list, userId);
    }

    private static void filterToggleByUser(Context context, ArrayList<Integer> list, int userId) {
        if (userId != 0) {
            for (int index : sRemoveByMultiUserList) {
                list.remove(Integer.valueOf(index));
            }
        }
    }

    private static void validateEditPositionInList(Context context, List<Integer> list) {
        Integer valueOf = Integer.valueOf(29);
        if (list.indexOf(valueOf) != getEditFixedPosition(context) || list.lastIndexOf(valueOf) != getEditFixedPosition(context)) {
            while (list.contains(valueOf)) {
                list.remove(list.indexOf(valueOf));
            }
            if (list.size() <= getEditFixedPosition(context)) {
                list.add(valueOf);
            } else {
                list.add(getEditFixedPosition(context), valueOf);
            }
        }
    }

    private static void validateEditPositionInPage(Context context, List<Integer> list) {
        Integer valueOf = Integer.valueOf(29);
        if (list.indexOf(valueOf) != list.lastIndexOf(valueOf) || list.indexOf(valueOf) != list.size() - 1) {
            while (list.contains(valueOf)) {
                list.remove(list.indexOf(valueOf));
            }
            list.add(valueOf);
        }
    }

    private static void addIfUnselected(ArrayList<Integer> list, int id) {
        if (!list.contains(Integer.valueOf(id))) {
            list.add(Integer.valueOf(id));
        }
    }

    public void setOnToggleChangedListener(OnToggleChangedListener l) {
        this.mToggleChangedListener.add(new WeakReference(l));
    }

    public void setOnToggleOrderChangeListener(OnToggleOrderChangedListener l) {
        this.mToggleOrderChangedListener.add(l);
    }

    public void removeToggleChangedListener(OnToggleChangedListener l) {
        for (int i = this.mToggleChangedListener.size() - 1; i >= 0; i--) {
            WeakReference<OnToggleChangedListener> item = (WeakReference) this.mToggleChangedListener.get(i);
            if (item.get() == null || l.equals(item.get())) {
                this.mToggleChangedListener.remove(i);
            }
        }
    }

    public void removeToggleOrderChangeListener(OnToggleOrderChangedListener l) {
        this.mToggleOrderChangedListener.remove(l);
    }

    public static int getToggleIdFromString(String toggleString) {
        if (sToggleStringToId.containsKey(toggleString)) {
            return ((Integer) sToggleStringToId.get(toggleString)).intValue();
        }
        return -1;
    }

    public static String getToggleStringFromId(int toggleId) {
        for (Entry<String, Integer> entry : sToggleStringToId.entrySet()) {
            if (toggleId == ((Integer) entry.getValue()).intValue()) {
                return (String) entry.getKey();
            }
        }
        return null;
    }

    public static int getName(int id) {
        return sToggleNames[id];
    }

    public static String getStatusName(int id, Resources res) {
        Object statusName = sToggleStatusNames.get(Integer.valueOf(id));
        if (statusName instanceof Integer) {
            return res.getString(((Integer) statusName).intValue());
        }
        return statusName.toString();
    }

    public static int getImage(int id) {
        return sToggleImages[id];
    }

    public static boolean getStatus(int id) {
        return sToggleStatus[id];
    }

    public static boolean isDisabled(int id) {
        return sToggleDisabled[id];
    }

    public static int getImageResource(int id, boolean isOpen) {
        return isOpen ? sToggleOnImages[id] : sToggleOffImages[id];
    }

    /* Access modifiers changed, original: protected */
    public void updateToggleStatus(int id, boolean isOpen) {
        sToggleStatus[id] = isOpen;
    }

    /* Access modifiers changed, original: protected */
    public void updateToggleDisabled(int id, boolean disabled) {
        sToggleDisabled[id] = disabled;
    }

    /* Access modifiers changed, original: protected */
    public void updateToggleStatusName(int id, Object statusName) {
        sToggleStatusNames.put(Integer.valueOf(id), statusName);
    }

    public static Drawable getImageDrawable(int id, Context context) {
        return getImageDrawable(id, getStatus(id), context);
    }

    public static Drawable getImageDrawable(int id, boolean isOpen, Context context) {
        Drawable drawable = context.getResources().getDrawable(getImageResource(id, isOpen));
        if (isOpen) {
            return drawable;
        }
        Drawable bgDrawable = context.getResources().getDrawable(R.drawable.status_bar_toggle_off_bg);
        Bitmap combined = Bitmap.createBitmap(bgDrawable.getIntrinsicWidth(), bgDrawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(combined);
        bgDrawable.setBounds(0, 0, bgDrawable.getIntrinsicWidth(), bgDrawable.getIntrinsicHeight());
        bgDrawable.draw(canvas);
        drawable.setBounds((bgDrawable.getIntrinsicWidth() - drawable.getIntrinsicWidth()) / 2, (bgDrawable.getIntrinsicHeight() - drawable.getIntrinsicHeight()) / 2, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return new BitmapDrawable(context.getResources(), combined);
    }

    public static int getGeneralImage(int id) {
        return sToggleGeneralImages[id];
    }

    public static void initDrawable(int id, Drawable drawable) {
    }

    public static void updateImageView(int id, ImageView imageView) {
        updateImageView(id, imageView, 0);
    }

    public static void updateImageView(int id, ImageView imageView, int color) {
        if (imageView != null) {
            Drawable drawable = getImageDrawable(id, imageView.getContext());
            if (Color.alpha(color) != 0) {
                drawable.setColorFilter(color, Mode.SRC_IN);
            }
            imageView.setImageDrawable(drawable);
            initDrawable(id, drawable);
        }
    }

    public static void updateTextView(int id, TextView textView) {
        if (textView != null) {
            textView.setText(getStatusName(id, textView.getResources()));
        }
    }

    public boolean startLongClickAction(int id) {
        if (18 == id) {
            return longClickScreenshot();
        }
        if (1 == id && (isDisabled(id) || CrossUserUtils.getCurrentUserId() != 0)) {
            return false;
        }
        int resId = sLongClickActions[id];
        if (resId == 0) {
            return false;
        }
        String action = this.mContext.getResources().getString(resId);
        if (action == null) {
            return false;
        }
        ComponentName component = ComponentName.unflattenFromString(action);
        if (component == null) {
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(component);
        if (1 == id) {
            intent.putExtra(":miui:starting_window_label", "");
        }
        intent.setFlags(335544320);
        try {
            this.mContext.startActivityAsUser(intent, UserHandle.CURRENT);
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("start activity exception, component = ");
            stringBuilder.append(component);
            Log.e(TAG, stringBuilder.toString());
        }
        return true;
    }

    private void queryBrightnessStatus() {
        boolean z = false;
        if (this.mBrightnessAutoAvailable) {
            if (1 == Settings.System.getIntForUser(this.mResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, 0, sCurrentUserId)) {
                z = true;
            }
        }
        this.mBrightnessAutoMode = z;
        this.mBrightnessManualLevel = Settings.System.getIntForUser(this.mResolver, Settings.System.SCREEN_BRIGHTNESS, DEFAULT_BACKLIGHT, sCurrentUserId);
        this.mBrightnessAutoLevel = ((Settings.System.getFloatForUser(this.mResolver, Settings.System.SCREEN_AUTO_BRIGHTNESS_ADJ, 0.0f, sCurrentUserId) + 1.0f) * ((float) RANGE)) / 2.0f;
    }

    /* Access modifiers changed, original: protected */
    public void updateToggleImage(int toggleId, int resId) {
        sToggleImages[toggleId] = resId;
        if (this.mToggleChangedListener.size() > 0) {
            for (int i = this.mToggleChangedListener.size() - 1; i >= 0; i--) {
                OnToggleChangedListener l = (OnToggleChangedListener) ((WeakReference) this.mToggleChangedListener.get(i)).get();
                if (l == null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("listener is null:");
                    stringBuilder.append(i);
                    Log.w(TAG, stringBuilder.toString());
                    this.mToggleChangedListener.remove(i);
                } else {
                    l.OnToggleChanged(toggleId);
                }
            }
        }
    }

    public boolean performToggle(int id) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("performToggle:");
        stringBuilder.append(id);
        stringBuilder.append(" state:");
        stringBuilder.append(isValid(this.mContext, id) ? Boolean.valueOf(sToggleStatus[id]) : "");
        Log.d(TAG, stringBuilder.toString());
        switch (id) {
            case 1:
                return toggleData();
            case 2:
                toggleBluetooth();
                return false;
            case 3:
                toggleAccelerometer();
                return false;
            case 5:
                toggleRinger();
                return false;
            case 6:
                toggleVibrate();
                return false;
            case 7:
                toggleGps();
                return false;
            case 8:
                toggleSync();
                return false;
            case 9:
                toggleFlightMode();
                return false;
            case 10:
                ((PowerManager) this.mContext.getSystemService(Context.POWER_SERVICE)).goToSleep(SystemClock.uptimeMillis());
                return false;
            case 11:
                toggleTorch();
                return false;
            case 15:
                toggleWifi();
                return false;
            case 18:
                toggleScreenshot();
                return true;
            case 20:
                toggleScreenButtonState();
                return false;
            case 22:
                toggleAutoBrightness();
                return false;
            case 23:
                togglePowerMode();
                return false;
            case 24:
                WifiApEnabler wifiApEnabler = mWifiApEnabler;
                if (wifiApEnabler == null) {
                    return false;
                }
                wifiApEnabler.toggleWifiAp();
                return false;
            case 25:
                toggleQuietMode();
                return false;
            case 26:
                togglePaperMode();
                return false;
            case 27:
                toggleMiDrop();
                return false;
            case 28:
                toggleCast();
                return true;
            case 29:
                toggleEdit();
                return true;
            case 30:
                toggleBatterySaverToggle();
                return false;
            default:
                return false;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void toggleCast() {
        try {
            this.mContext.startActivityAsUser(getCastIntent(), UserHandle.CURRENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Intent getCastIntent() {
        Intent intent = new Intent("miui.intent.action.MIPLAY");
        intent.addFlags(268435456);
        intent.addFlags(536870912);
        return intent;
    }

    /* Access modifiers changed, original: 0000 */
    public void toggleWifi() {
        if (!isDisabled(15)) {
            boolean enable = mWifiManager.getWifiState() != 3;
            int wifiApState = mWifiManager.getWifiApState();
            if (enable && !ConnectivityManagerReflector.getWifiStaSapConcurrency(mWifiManager) && (wifiApState == 12 || wifiApState == 13)) {
                WifiApEnabler wifiApEnabler = mWifiApEnabler;
                if (wifiApEnabler != null) {
                    wifiApEnabler.setSoftapEnabled(false);
                }
            }
            this.mBgHandler.removeMessages(1);
            this.mBgHandler.obtainMessage(1, enable ? 1 : 0, 0).sendToTarget();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void updateWifiToggle(Intent intent) {
        int i;
        int wifiState = -1;
        String action = intent != null ? intent.getAction() : "";
        if (intent != null) {
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
                wifiState = intent.getIntExtra("wifi_state", 4);
                boolean z = wifiState == 3 || wifiState == 2;
                this.mWifiEnable = z;
                z = wifiState == 2 || wifiState == 0;
                this.mWifiChanging = z;
                updateMiDropToggle(false);
            } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
                NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                boolean z2 = networkInfo != null && networkInfo.isConnected();
                this.mWifiConnected = z2;
                if (this.mWifiConnected) {
                    WifiInfo info = (WifiInfo) intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
                    if (info == null) {
                        info = mWifiManager.getConnectionInfo();
                    }
                    if (info != null) {
                        this.mWifiSsid = removeDoubleQuotes(getSsid(info));
                    } else {
                        this.mWifiSsid = null;
                    }
                    this.mWifiEnable = true;
                    this.mWifiChanging = false;
                } else {
                    this.mWifiSsid = null;
                }
            }
        }
        Log.d(TAG, String.format("updateWifiToggle wifiState=%d mWifiConnected=%b action=%s", new Object[]{Integer.valueOf(wifiState), Boolean.valueOf(this.mWifiConnected), action}));
        Object obj = this.mWifiSsid;
        if (obj == null) {
            obj = this.mContext.getResources().getString(R.string.status_bar_toggle_wifi);
        }
        updateToggleStatusName(15, obj);
        updateToggleStatus(15, this.mWifiEnable);
        updateToggleDisabled(15, this.mWifiChanging);
        if (this.mWifiEnable) {
            i = R.drawable.status_bar_toggle_wifi_on;
        } else {
            i = R.drawable.status_bar_toggle_wifi_off;
        }
        updateToggleImage(15, i);
    }

    private String removeDoubleQuotes(String string) {
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        int length = string.length();
        if (length > 1 && string.charAt(0) == '\"' && string.charAt(length - 1) == '\"') {
            return string.substring(1, length - 1);
        }
        return string;
    }

    private String getSsid(WifiInfo info) {
        String ssid = info.getSSID();
        if (ssid != null && !WifiSsid.NONE.equals(ssid)) {
            return ssid;
        }
        List<WifiConfiguration> networks = mWifiManager.getConfiguredNetworks();
        if (networks != null) {
            int length = networks.size();
            for (int i = 0; i < length; i++) {
                if (((WifiConfiguration) networks.get(i)).networkId == info.getNetworkId()) {
                    return ((WifiConfiguration) networks.get(i)).SSID;
                }
            }
        }
        return null;
    }

    private boolean ensureBluetoothAdapter() {
        if (this.mBluetoothAdapter == null) {
            this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        return this.mBluetoothAdapter != null;
    }

    private void toggleBluetooth() {
        int i;
        this.mBluetoothEnable ^= 1;
        updateToggleStatus(2, this.mBluetoothEnable);
        if (this.mBluetoothEnable) {
            i = R.drawable.status_bar_toggle_bluetooth_on;
        } else {
            i = R.drawable.status_bar_toggle_bluetooth_off;
        }
        updateToggleImage(2, i);
        this.mHandler.removeMessages(1);
        this.mBgHandler.removeMessages(2);
        this.mBgHandler.sendMessageDelayed(this.mBgHandler.obtainMessage(2, this.mBluetoothEnable, 0), this.mBluetoothDelay ? 500 : 0);
    }

    private void toggleBluetooth(boolean enable) {
        if (ensureBluetoothAdapter()) {
            int state = this.mBluetoothAdapter.getState();
            boolean changing = state == 11 || state == 13;
            if (changing) {
                this.mBgHandler.sendMessageDelayed(this.mBgHandler.obtainMessage(2, enable, 0), 100);
            } else if (enable && state != 12) {
                this.mBluetoothDelay = true;
                this.mBluetoothAdapter.enable();
            } else if (enable || state == 10) {
                this.mBluetoothDelay = false;
            } else {
                this.mBluetoothDelay = true;
                this.mBluetoothAdapter.disable();
            }
        }
    }

    private void verifyBluetoothState() {
        if (ensureBluetoothAdapter()) {
            boolean enable = this.mBluetoothAdapter.isEnabled() || this.mBluetoothAdapter.getState() == 11;
            if (this.mBluetoothEnable != enable) {
                int i;
                this.mBluetoothEnable = enable;
                updateToggleStatus(2, this.mBluetoothEnable);
                if (this.mBluetoothEnable) {
                    i = R.drawable.status_bar_toggle_bluetooth_on;
                } else {
                    i = R.drawable.status_bar_toggle_bluetooth_off;
                }
                updateToggleImage(2, i);
            }
        }
    }

    private void updateBluetoothToggle() {
        if (ensureBluetoothAdapter()) {
            int state = this.mBluetoothAdapter.getState();
            boolean enable = this.mBluetoothAdapter.isEnabled() || state == 11;
            this.mBluetoothEnable = this.mBluetoothDelay ? this.mBluetoothEnable : enable;
            updateToggleStatus(2, this.mBluetoothEnable);
            updateToggleDisabled(2, false);
            if (state == 12 || state == 10) {
                int i;
                if (this.mBluetoothEnable) {
                    i = R.drawable.status_bar_toggle_bluetooth_on;
                } else {
                    i = R.drawable.status_bar_toggle_bluetooth_off;
                }
                updateToggleImage(2, i);
                if (this.mBluetoothDelay) {
                    this.mBluetoothDelay = false;
                    this.mHandler.removeMessages(1);
                    this.mHandler.sendEmptyMessageDelayed(1, 1000);
                }
            }
        }
    }

    private void updateBrightnessToggle() {
        int autoResId;
        if (this.mBrightnessAutoMode) {
            autoResId = R.drawable.status_bar_toggle_brightness_auto;
        } else {
            autoResId = R.drawable.status_bar_toggle_brightness_manual;
        }
        updateToggleStatus(22, this.mBrightnessAutoMode);
        updateToggleImage(22, autoResId);
    }

    private void toggleAutoBrightness() {
        if (this.mBrightnessAutoMode) {
            this.mBrightnessAutoMode = false;
        } else {
            this.mBrightnessAutoMode = this.mBrightnessAutoAvailable;
        }
        if (!this.mBrightnessAutoMode) {
            if (PINECONE_BRIGHTNESS_OPT_STRATEGY.equals(AUTO_BRIGHTNESS_OPTIMIZE_STRATEGY)) {
                ObjectReference<Integer> reference = ReflectionUtils.tryCallMethod(Stub.asInterface(ServiceManager.getService(Context.POWER_SERVICE)), "getScreenBrightness", Integer.class, new Object[0]);
                if (reference != null) {
                    Settings.System.putIntForUser(this.mResolver, Settings.System.SCREEN_BRIGHTNESS, ((Integer) reference.get()).intValue(), sCurrentUserId);
                }
            }
        }
        setBrightnessMode();
    }

    private void setBrightnessMode() {
        int i;
        ContentResolver contentResolver = this.mResolver;
        if (this.mBrightnessAutoMode) {
            i = 1;
        } else {
            i = 0;
        }
        Settings.System.putIntForUser(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, i, sCurrentUserId);
    }

    public int getCurBrightness() {
        if (!this.mBrightnessAutoMode || !USE_SCREEN_AUTO_BRIGHTNESS_ADJUSTMENT) {
            return this.mBrightnessManualLevel - MINIMUM_BACKLIGHT;
        }
        if (!PINECONE_BRIGHTNESS_OPT_STRATEGY.equals(AUTO_BRIGHTNESS_OPTIMIZE_STRATEGY)) {
            return (int) this.mBrightnessAutoLevel;
        }
        Context context = this.mContext;
        String str = Context.POWER_SERVICE;
        int defaultBrightness = ((PowerManager) context.getSystemService(str)).getDefaultScreenBrightnessSetting();
        IPowerManager pm = Stub.asInterface(ServiceManager.getService(str));
        ObjectReference<Integer> reference = null;
        if (pm != null) {
            reference = ReflectionUtils.tryCallMethod(pm, "getScreenBrightness", Integer.class, new Object[0]);
        } else {
            Slog.d(TAG, "pm is null");
        }
        return (reference == null ? defaultBrightness : ((Integer) reference.get()).intValue()) - MINIMUM_BACKLIGHT;
    }

    public void applyBrightness(int brightness, boolean write) {
        this.mBgHandler.removeMessages(3);
        this.mBgHandler.obtainMessage(3, brightness, write).sendToTarget();
    }

    private void applyBrightnessIntenal(int brightness, boolean write) {
        if (this.mBrightnessAutoMode && USE_SCREEN_AUTO_BRIGHTNESS_ADJUSTMENT) {
            if (PINECONE_BRIGHTNESS_OPT_STRATEGY.equals(AUTO_BRIGHTNESS_OPTIMIZE_STRATEGY)) {
                CompatibilityP.setTemporaryScreenBrightness(MINIMUM_BACKLIGHT + brightness);
                return;
            }
            float valf = ((((float) brightness) * 2.0f) / ((float) RANGE)) - 1.0f;
            CompatibilityP.setTemporaryScreenAutoBrightness(valf);
            if (write) {
                Settings.System.putFloatForUser(this.mResolver, Settings.System.SCREEN_AUTO_BRIGHTNESS_ADJ, valf, sCurrentUserId);
                return;
            }
            return;
        }
        int brightnessValue = MINIMUM_BACKLIGHT + brightness;
        CompatibilityP.setTemporaryScreenBrightness(brightnessValue);
        if (write) {
            Settings.System.putIntForUser(this.mResolver, Settings.System.SCREEN_BRIGHTNESS, brightnessValue, sCurrentUserId);
        }
    }

    private boolean toggleData() {
        if (isDisabled(1)) {
            return false;
        }
        if (this.mMobilePolicyEnable) {
            this.mMobileDataEnable = 1 ^ this.mMobileDataEnable;
            this.mMobileDataUtils.enableMobileData(this.mContext, this.mMobileDataEnable);
            return false;
        }
        String subscriberId = this.mMobileDataUtils.getSubscriberId(this.mContext);
        if (TextUtils.isEmpty(subscriberId)) {
            return false;
        }
        Parcelable template = NetworkTemplate.buildTemplateMobileAll(subscriberId);
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(AntiSpam.ANTISPAM_PKG, "com.miui.networkassistant.ui.activity.NetworkOverLimitActivity"));
        intent.addFlags(268435456);
        intent.putExtra(NetworkPolicyManager.EXTRA_NETWORK_TEMPLATE, template);
        this.mContext.startActivityAsUser(intent, UserHandle.CURRENT);
        return true;
    }

    private void postUpdateDataToggleAsync() {
        this.mBgHandler.removeMessages(6);
        this.mBgHandler.sendEmptyMessage(6);
    }

    private void updateDataToggle() {
        this.mMobileDataEnable = this.mMobileDataUtils.isMobileEnable(this.mContext);
        this.mHandler.post(new Runnable() {
            public void run() {
                boolean z = false;
                boolean isDataEnabled = ToggleManager.this.mMobileDataEnable && ToggleManager.this.mMobilePolicyEnable && !ToggleManager.this.mFlightMode && !ToggleManager.this.mIsSimMissing;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("mMobileDataEnable=");
                stringBuilder.append(ToggleManager.this.mMobileDataEnable);
                stringBuilder.append(";mMobilePolicyEnable=");
                stringBuilder.append(ToggleManager.this.mMobilePolicyEnable);
                stringBuilder.append(";mFlightMode=");
                stringBuilder.append(ToggleManager.this.mFlightMode);
                stringBuilder.append(";mIsSimMissing=");
                stringBuilder.append(ToggleManager.this.mIsSimMissing);
                Log.d(ToggleManager.TAG, stringBuilder.toString());
                ToggleManager.this.updateToggleStatus(1, isDataEnabled);
                ToggleManager toggleManager = ToggleManager.this;
                if (toggleManager.mFlightMode || ToggleManager.this.mIsSimMissing) {
                    z = true;
                }
                toggleManager.updateToggleDisabled(1, z);
                ToggleManager.this.updateToggleImage(1, isDataEnabled ? R.drawable.status_bar_toggle_data_on : R.drawable.status_bar_toggle_data_off);
            }
        });
    }

    private void toggleAccelerometer() {
        try {
            IWindowManager wm = IWindowManager.Stub.asInterface(ServiceManager.getService(Context.WINDOW_SERVICE));
            if (this.mAccelerometer) {
                int rotation = getRotation(wm);
                if (!(rotation == 0 || 2 == rotation)) {
                    showToast((int) R.string.screen_rotation_freeze_message, 1);
                }
                wm.freezeRotation(-1);
                return;
            }
            wm.thawRotation();
        } catch (RemoteException e) {
        }
    }

    private int getRotation(IWindowManager wm) {
        ObjectReference<Integer> reference;
        if (VERSION.SDK_INT < 26) {
            reference = ReflectionUtils.tryCallMethod(wm, "getRotation", Integer.class, new Object[0]);
        } else {
            reference = ReflectionUtils.tryCallMethod(wm, "getDefaultDisplayRotation", Integer.class, new Object[0]);
        }
        return ((Integer) reference.get()).intValue();
    }

    private void updateAccelerometerToggle() {
        int i;
        boolean z = false;
        if (Settings.System.getIntForUser(this.mResolver, Settings.System.ACCELEROMETER_ROTATION, 0, sCurrentUserId) != 0) {
            z = true;
        }
        this.mAccelerometer = z;
        updateToggleStatus(3, this.mAccelerometer ^ 1);
        if (this.mAccelerometer) {
            i = R.drawable.status_bar_toggle_rotate_lock_off;
        } else {
            i = R.drawable.status_bar_toggle_rotate_lock_on;
        }
        updateToggleImage(3, i);
    }

    private void toggleRinger() {
        int mode = 4;
        if (SilenceMode.isSupported) {
            boolean enabled = this.mZenMode != 4;
            SilenceMode.setSilenceMode(this.mContext, enabled ? 4 : 0, null);
            if (!enabled) {
                mode = 0;
            }
            SilenceMode.reportRingerModeInfo(SilenceMode.MISTAT_SILENCE_DND, SilenceMode.MISTAT_RINGERMODE_LIST[mode], "0", System.currentTimeMillis());
            return;
        }
        AudioManagerHelper.toggleSilent(this.mContext, 4);
    }

    public void updateRingerToggle() {
        int i;
        this.mZenMode = SilenceMode.getZenMode(this.mContext);
        boolean silentEnabled = SilenceMode.isSupported ? this.mZenMode == 4 : AudioManagerHelper.isSilentEnabled(this.mContext);
        updateToggleStatus(5, silentEnabled);
        if (silentEnabled) {
            i = R.drawable.status_bar_toggle_mute_on;
        } else {
            i = R.drawable.status_bar_toggle_mute_off;
        }
        updateToggleImage(5, i);
    }

    private void showToast(int resId, int length) {
        showToast(this.mContext.getString(resId), length);
    }

    private void showToast(CharSequence msg, int length) {
        Toast toast = Toast.makeText(this.mContext, msg, length);
        toast.setType(2006);
        LayoutParams windowParams = toast.getWindowParams();
        windowParams.privateFlags |= 16;
        toast.show();
    }

    private void toggleVibrate() {
        AudioManagerHelper.toggleVibrateSetting(this.mContext);
    }

    public void updateVibrateToggle() {
        int i;
        boolean isVibrateEnabled = AudioManagerHelper.isVibrateEnabled(this.mContext);
        updateToggleStatus(6, isVibrateEnabled);
        if (isVibrateEnabled) {
            i = R.drawable.status_bar_toggle_vibrate_on;
        } else {
            i = R.drawable.status_bar_toggle_vibrate_off;
        }
        updateToggleImage(6, i);
    }

    private void toggleGps() {
        this.mLocationManager.setLocationEnabledForUser(this.mGpsEnable ^ 1, new UserHandle(sCurrentUserId));
    }

    private void updateGpsToggle() {
        int i;
        this.mGpsEnable = this.mLocationManager.isProviderEnabledForUser(LocationManager.GPS_PROVIDER, new UserHandle(sCurrentUserId));
        updateToggleStatus(7, this.mGpsEnable);
        if (this.mGpsEnable) {
            i = R.drawable.status_bar_toggle_gps_on;
        } else {
            i = R.drawable.status_bar_toggle_gps_off;
        }
        updateToggleImage(7, i);
    }

    private void toggleSync() {
        this.mBgHandler.removeMessages(5);
        this.mBgHandler.sendEmptyMessage(5);
    }

    private void toggleSyncIntenal() {
        try {
            Method method = ContentResolver.class.getMethod("setMasterSyncAutomaticallyAsUser", new Class[]{Boolean.TYPE, Integer.TYPE});
            Object[] objArr = new Object[2];
            objArr[0] = Boolean.valueOf(!isSyncOn());
            objArr[1] = Integer.valueOf(sCurrentUserId);
            method.invoke(null, objArr);
        } catch (Exception e) {
            Log.i(TAG, "setMasterSyncAutomaticallyAsUser not found.");
            ContentResolver.setMasterSyncAutomatically(1 ^ ContentResolver.getMasterSyncAutomatically());
        }
    }

    private void updateSyncToggle() {
        this.mBgHandler.removeMessages(4);
        this.mBgHandler.sendEmptyMessage(4);
    }

    private void updateSyncToggle(boolean isSyncOn) {
        int i;
        updateToggleStatus(8, isSyncOn);
        if (isSyncOn) {
            i = R.drawable.status_bar_toggle_sync_on;
        } else {
            i = R.drawable.status_bar_toggle_sync_off;
        }
        updateToggleImage(8, i);
    }

    private boolean isSyncOn() {
        try {
            return ((Boolean) ContentResolver.class.getMethod("getMasterSyncAutomaticallyAsUser", new Class[]{Integer.TYPE}).invoke(null, new Object[]{Integer.valueOf(sCurrentUserId)})).booleanValue();
        } catch (Exception e) {
            Log.i(TAG, "getMasterSyncAutomaticallyAsUser not found.");
            return ContentResolver.getMasterSyncAutomatically();
        }
    }

    private void toggleFlightMode() {
        if (!RestrictionsHelper.hasAirplaneRestriction(this.mContext)) {
            this.mFlightMode ^= 1;
            Global.putInt(this.mResolver, "airplane_mode_on", this.mFlightMode);
            Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
            intent.addFlags(536870912);
            intent.putExtra("state", this.mFlightMode);
            this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
        }
    }

    private void updateFlightModeToggle() {
        int i;
        boolean z = false;
        if (Global.getInt(this.mResolver, "airplane_mode_on", 0) != 0) {
            z = true;
        }
        this.mFlightMode = z;
        updateToggleStatus(9, this.mFlightMode);
        if (this.mFlightMode) {
            i = R.drawable.status_bar_toggle_flight_mode_on;
        } else {
            i = R.drawable.status_bar_toggle_flight_mode_off;
        }
        updateToggleImage(9, i);
        postUpdateDataToggleAsync();
    }

    private void updateTorchToggle() {
        int i;
        boolean z = false;
        if (Global.getInt(this.mContext.getContentResolver(), System.TORCH_STATE, 0) == 1) {
            z = true;
        }
        this.mTorchEnable = z;
        updateToggleStatus(11, this.mTorchEnable);
        if (this.mTorchEnable) {
            i = R.drawable.status_bar_toggle_torch_on;
        } else {
            i = R.drawable.status_bar_toggle_torch_off;
        }
        updateToggleImage(11, i);
    }

    private void toggleTorch() {
        Intent intent = new Intent("miui.intent.action.TOGGLE_TORCH");
        intent.putExtra("miui.intent.extra.IS_TOGGLE", true);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.CURRENT);
    }

    private void togglePowerMode() {
        String str = this.mPowerMode;
        String str2 = System.POWER_MODE_VALUE_HIGH;
        if (str2.equals(str)) {
            this.mPowerMode = "middle";
        } else {
            this.mPowerMode = str2;
        }
        SystemProperties.set(System.POWER_MODE_KEY_PROPERTY, this.mPowerMode);
        Settings.System.putStringForUser(this.mResolver, System.POWER_MODE, this.mPowerMode, sCurrentUserId);
        this.mContext.sendBroadcastAsUser(new Intent("miui.intent.action.POWER_MODE_CHANGE"), UserHandle.CURRENT);
    }

    private void updatePowerModeToggle() {
        int i;
        this.mPowerMode = Settings.System.getStringForUser(this.mResolver, System.POWER_MODE, sCurrentUserId);
        if (TextUtils.isEmpty(this.mPowerMode)) {
            this.mPowerMode = "middle";
        }
        boolean isHigh = System.POWER_MODE_VALUE_HIGH.equals(this.mPowerMode);
        updateToggleStatus(23, isHigh);
        if (isHigh) {
            i = R.drawable.status_bar_toggle_power_high_on;
        } else {
            i = R.drawable.status_bar_toggle_power_high_off;
        }
        updateToggleImage(23, i);
    }

    private void toggleBatterySaverToggle() {
        this.mBatterySaveMode ^= 1;
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_POWER_MODE_OPEN, this.mBatterySaveMode);
        this.mResolver.call(maybeAddUserId(Uri.parse(POWER_MODE_URI_PATH), sCurrentUserId), METHOD_CHANGE_POWER_MODE, null, bundle);
    }

    private void updateBatterySaverToggle() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("updateBatterySaverToggle() old mode=");
        stringBuilder.append(this.mBatterySaveMode);
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Log.d(str, stringBuilder2);
        boolean z = false;
        if (Settings.System.getIntForUser(this.mResolver, KEY_POWER_MODE_OPEN, 0, sCurrentUserId) != 0) {
            z = true;
        }
        this.mBatterySaveMode = z;
        stringBuilder = new StringBuilder();
        stringBuilder.append("updateBatterySaverToggle() new mode=");
        stringBuilder.append(this.mBatterySaveMode);
        Log.d(str, stringBuilder.toString());
        updateToggleStatus(30, this.mBatterySaveMode);
        updateToggleImage(30, getImageResource(30, this.mBatterySaveMode));
    }

    private void toggleQuietMode() {
        int mode = 1;
        if (SilenceMode.isSupported) {
            boolean enabled = this.mZenMode != 1;
            SilenceMode.setSilenceMode(this.mContext, enabled ? 1 : 0, null);
            if (!enabled) {
                mode = 0;
            }
            SilenceMode.reportRingerModeInfo(SilenceMode.MISTAT_SILENCE_DND, SilenceMode.MISTAT_RINGERMODE_LIST[mode], "0", System.currentTimeMillis());
            return;
        }
        this.mQuietMode ^= 1;
        AntiSpam.setQuietMode(this.mContext, this.mQuietMode);
    }

    private void updateQuietModeToggle() {
        int i;
        this.mZenMode = SilenceMode.getZenMode(this.mContext);
        boolean z = true;
        if (!SilenceMode.isSupported) {
            z = AntiSpam.isQuietModeEnable(this.mContext);
        } else if (this.mZenMode != 1) {
            z = false;
        }
        this.mQuietMode = z;
        updateToggleStatus(25, this.mQuietMode);
        if (this.mQuietMode) {
            i = R.drawable.status_bar_toggle_quiet_mode_on;
        } else {
            i = R.drawable.status_bar_toggle_quiet_mode_off;
        }
        updateToggleImage(25, i);
    }

    private void togglePaperMode() {
        this.mPaperMode ^= 1;
        Settings.System.putIntForUser(this.mResolver, ScreenEffect.SCREEN_PAPER_MODE_ENABLED, this.mPaperMode, sCurrentUserId);
    }

    private void updatePaperModeToggle() {
        int i;
        boolean z = false;
        if (Settings.System.getIntForUser(this.mResolver, ScreenEffect.SCREEN_PAPER_MODE_ENABLED, 0, sCurrentUserId) != 0) {
            z = true;
        }
        this.mPaperMode = z;
        updateToggleStatus(26, this.mPaperMode);
        if (this.mPaperMode) {
            i = R.drawable.status_bar_toggle_paper_mode_on;
        } else {
            i = R.drawable.status_bar_toggle_paper_mode_off;
        }
        updateToggleImage(26, i);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean useWifiApForMiDrop() {
        if (!sHasMiDrop) {
            try {
                sHasMiDrop = this.mContext.getPackageManager().getApplicationInfo(PKG_NAME_MIDROP, 0) != null;
            } catch (NameNotFoundException e) {
            }
        }
        if (mWifiApEnabler == null || !sHasMiDrop) {
            return false;
        }
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isDisplayMiDropOn() {
        int settings = Global.getInt(this.mResolver, "key_midrop_enabled", 0);
        if (settings == 2 || (settings == 1 && useWifiApForMiDrop() && mWifiApEnabler.isWifiApOn())) {
            return true;
        }
        return false;
    }

    private void toggleMiDrop() {
        if (!isDisabled(27)) {
            this.mMiDropChanging = true;
            boolean isMiDropOn = isDisplayMiDropOn();
            Intent intent = new Intent(isMiDropOn ? "miui.intent.action.midrop_off" : "miui.intent.action.midrop_on");
            intent.setComponent(new ComponentName(PKG_NAME_MIDROP, COMPONENT_NAME_MIDROP_STARTUP_RECEIVER));
            intent.addFlags(268435456);
            this.mContext.sendBroadcastAsUser(intent, UserHandle.CURRENT);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("MiDrop: toggle MiDrop to ");
            stringBuilder.append(isMiDropOn ? "OFF" : "ON");
            Log.d(TAG, stringBuilder.toString());
            updateMiDropToggle(true);
        }
    }

    private void updateMiDropToggle() {
        boolean z = false;
        int setting = Global.getInt(this.mResolver, "key_midrop_enabled", 0);
        if (setting == 2 || setting == 3) {
            z = true;
        }
        this.mMiDropChanging = z;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MiDrop: isMiDropDisabled:");
        stringBuilder.append(isDisabled(27));
        stringBuilder.append(" isWifiAPDisabled:");
        stringBuilder.append(isDisabled(24));
        stringBuilder.append(" mMiDropChanging:");
        stringBuilder.append(this.mMiDropChanging);
        stringBuilder.append(" setting:");
        stringBuilder.append(setting);
        Log.d(TAG, stringBuilder.toString());
        updateMiDropToggle(true);
    }

    /* Access modifiers changed, original: 0000 */
    public void updateMiDropToggle(boolean updateWifiAp) {
        int i;
        boolean z = false;
        if (updateWifiAp && useWifiApForMiDrop()) {
            mWifiApEnabler.updateWifiApToggle(false);
        }
        boolean isMiDropOn = isDisplayMiDropOn();
        boolean isWifiApDisabled = useWifiApForMiDrop() ? mWifiApEnabler.isWifiApDisabled() : false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MiDrop: updateMiDropToggle(boolean) isMiDropOn = ");
        stringBuilder.append(isMiDropOn);
        stringBuilder.append(" isWifiApDisabled = ");
        stringBuilder.append(isWifiApDisabled);
        stringBuilder.append(" mMiDropChanging = ");
        stringBuilder.append(this.mMiDropChanging);
        stringBuilder.append(" mWifiChanging = ");
        stringBuilder.append(this.mWifiChanging);
        Log.d(TAG, stringBuilder.toString());
        if (isWifiApDisabled || this.mMiDropChanging || this.mWifiChanging) {
            z = true;
        }
        updateToggleDisabled(27, z);
        updateToggleStatus(27, isMiDropOn);
        if (isMiDropOn) {
            i = R.drawable.status_bar_toggle_midrop_on;
        } else {
            i = R.drawable.status_bar_toggle_midrop_off;
        }
        updateToggleImage(27, i);
    }

    private void toggleScreenshot() {
        ((StatusBarManager) this.mContext.getSystemService(Context.STATUS_BAR_SERVICE)).collapsePanels();
        this.mContext.sendBroadcastAsUser(new Intent("android.intent.action.CAPTURE_SCREENSHOT"), UserHandle.CURRENT);
    }

    private boolean longClickScreenshot() {
        String path = null;
        File screenShotFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Screenshots");
        if (screenShotFolder.exists() && screenShotFolder.isDirectory()) {
            File[] files = screenShotFolder.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String filename) {
                    filename = filename.toLowerCase();
                    if (filename.endsWith("png") || filename.endsWith("jpg") || filename.endsWith("jpeg")) {
                        return true;
                    }
                    return false;
                }
            });
            if (files == null) {
                return false;
            }
            long maxLastModifed = 0;
            String path2 = null;
            for (File file : files) {
                if (file.lastModified() > maxLastModifed) {
                    maxLastModifed = file.lastModified();
                    path2 = file.getAbsolutePath();
                }
            }
            path = path2;
        }
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(new File(path)), "image/*");
        intent.setFlags(268435456);
        this.mContext.startActivityAsUser(intent, UserHandle.CURRENT);
        return true;
    }

    private void toggleScreenButtonState() {
        this.mScreenButtonDisabled ^= 1;
        ContentResolver contentResolver = this.mResolver;
        int i = sCurrentUserId;
        String str = MiuiSettings.Secure.SCREEN_BUTTONS_HAS_BEEN_DISABLED;
        int intForUser = Secure.getIntForUser(contentResolver, str, 0, i);
        i = R.string.auto_disable_screenbuttons_disable_toast_text;
        if (intForUser == 0) {
            Secure.putIntForUser(this.mResolver, str, 1, sCurrentUserId);
            AlertDialog dialog = new Builder(this.mContext, miui.R.style.Theme_Light_Dialog_Alert).setMessage((int) R.string.auto_disable_screenbuttons_disable_toast_text).setPositiveButton(17039370, null).create();
            dialog.getWindow().setType(2010);
            dialog.getWindow().addPrivateFlags(16);
            dialog.show();
        } else {
            if (!this.mScreenButtonDisabled) {
                i = R.string.auto_disable_screenbuttons_enable_toast_text;
            }
            showToast(i, 0);
        }
        Secure.putIntForUser(this.mResolver, "screen_buttons_state", this.mScreenButtonDisabled, sCurrentUserId);
    }

    private void toggleEdit() {
        String extraShowFragment;
        if (isListStyle(this.mContext)) {
            extraShowFragment = "com.android.settings.ToggleArrangementFragment";
        } else {
            extraShowFragment = "com.android.settings.TogglePositionFragment";
        }
        Intent intent = new Intent();
        intent.setFlags(335544320);
        intent.setAction(Intent.ACTION_MAIN);
        intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, extraShowFragment);
        intent.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true);
        intent.setClassName(SettingsTree.SETTINGS_PACKAGE, "com.android.settings.SubSettings");
        try {
            this.mContext.startActivityAsUser(intent, UserHandle.CURRENT);
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("toggleEdit() Exception=");
            stringBuilder.append(e);
            Log.e(TAG, stringBuilder.toString());
        }
    }

    private void updateScreenButtonState() {
        int i;
        boolean z = false;
        if (Secure.getIntForUser(this.mResolver, "screen_buttons_state", 0, sCurrentUserId) != 0) {
            z = true;
        }
        this.mScreenButtonDisabled = z;
        updateToggleStatus(20, this.mScreenButtonDisabled);
        if (this.mScreenButtonDisabled) {
            i = R.drawable.status_bar_toggle_screen_button_disabled;
        } else {
            i = R.drawable.status_bar_toggle_screen_button_enabled;
        }
        updateToggleImage(20, i);
    }

    public boolean isBrightnessAutoMode() {
        return this.mBrightnessAutoMode;
    }

    public static boolean uriHasUserId(Uri uri) {
        if (uri == null) {
            return false;
        }
        return TextUtils.isEmpty(uri.getUserInfo()) ^ 1;
    }

    public static Uri maybeAddUserId(Uri uri, int userId) {
        if (VERSION.SDK_INT < 21) {
            return uri;
        }
        if (uri == null) {
            return null;
        }
        if (userId != -2) {
            if ("content".equals(uri.getScheme()) && !uriHasUserId(uri)) {
                Uri.Builder builder = uri.buildUpon();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("");
                stringBuilder.append(userId);
                stringBuilder.append("@");
                stringBuilder.append(uri.getEncodedAuthority());
                builder.encodedAuthority(stringBuilder.toString());
                return builder.build();
            }
        }
        return uri;
    }

    private static boolean isMiPad() {
        return Build.IS_TABLET;
    }

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        int i;
        pw.println("  - ToggleManager ------");
        pw.println("  - wifi ---");
        pw.print("  mWifiEnable=");
        pw.println(this.mWifiEnable);
        pw.print("  mWifiConnected=");
        pw.println(this.mWifiConnected);
        pw.print("  mWifiChanging=");
        pw.println(this.mWifiChanging);
        pw.print("  mWifiSsid=");
        pw.println(this.mWifiSsid);
        pw.println("  - data ---");
        pw.print("  mMobileDataEnable=");
        pw.println(this.mMobileDataEnable);
        pw.print("  mMobilePolicyEnable=");
        pw.println(this.mMobilePolicyEnable);
        pw.print("  mIsSimMissing=");
        pw.println(this.mIsSimMissing);
        pw.print("  mFlightMode=");
        pw.println(this.mFlightMode);
        pw.println("  - toggles ---");
        for (i = 0; i < 32; i++) {
            pw.print("  Toggle:");
            pw.print(i);
            pw.print("  Status:");
            pw.println(sToggleStatus[i]);
        }
        if (this.mToggleChangedListener.size() > 0) {
            pw.println("  - listeners ---");
            for (i = this.mToggleChangedListener.size() - 1; i >= 0; i--) {
                OnToggleChangedListener l = (OnToggleChangedListener) ((WeakReference) this.mToggleChangedListener.get(i)).get();
                pw.print("  listener:");
                pw.println(l);
            }
        }
    }
}
