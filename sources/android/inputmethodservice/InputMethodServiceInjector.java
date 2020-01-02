package android.inputmethodservice;

import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Rect;
import android.inputmethodservice.InputMethodClipboardPhrasePopupView.ClipboardJsonUtil;
import android.inputmethodservice.InputMethodService.Insets;
import android.miui.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.RemoteCallback;
import android.os.RemoteCallback.OnResultListener;
import android.os.SystemProperties;
import android.provider.MiuiSettings;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.WindowManager.BadTokenException;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

class InputMethodServiceInjector {
    private static final String FUNCTION_CLIPBOARD = "clipboard_phrase";
    private static final String FUNCTION_SWITCH = "switch_input_method";
    private static final String FUNCTION_VOICE = "voice_input";
    private static final String FUNCTION_XIAOAI = "xiaoai_input";
    private static final String NO_FUNCTION = "no_function";
    public static final String TAG = "InputMethodService";
    private static int mBottomAreaHeight;
    private static InputMethodCloudClipboardPopupView mCloudClipboardPopupView;
    private static BottomViewHelper sBottomViewHelper;
    private static boolean sCanPopCloudClipboardViewLeft = false;
    private static boolean sCanPopCloudClipboardViewRight = false;
    private static boolean sCanShowMiuiBottom = false;
    private static InputMethodClipboardPhrasePopupView sClipboardWindow;
    private static String sCloudClipboardContent;
    private static CloudClipboardContentObserver sCloudClipboardContentObserver = null;
    private static boolean sFloatEnable = false;
    private static HashMap<String, Integer> sImeLastMiui10Version = new HashMap();
    private static HashMap<String, Integer> sImeMinVersionSupport = new HashMap();
    private static InputMethodFunctionSelectObserver sInputMethodFunctionSelectObserver = null;
    private static int sIsImeSupport = -1;
    private static boolean sIsMiuiBottomEnabled = false;
    private static int sIsMiuiBottomSupport = -1;
    private static boolean sIsScreenLandscape = false;
    private static ImageView sLeftButton;
    private static LinearLayout sLeftLayout;
    private static String sLeftSelectedKey;
    private static MiuiBottomStatusObserver sMiuiBottomStatusObserver = null;
    private static boolean sNavigationBarFullScreenValue = false;
    private static boolean sNeedCloudClipboardContentShown = false;
    private static boolean sNeedHandleComputeInsets = false;
    private static ImageView sRightButton;
    private static LinearLayout sRightLayout;
    private static String sRightSelectedKey;
    private static int sScreenHeight = -1;
    private static int sScreenHeightWithoutStatusBar = -1;
    private static int sStatusBarHeight = -1;
    private static InputMethodSwitchPopupView sSwitchWindow;
    private static Insets sTmpInsets;
    private static int sVersionCode = -1;

    static class BottomViewHelper {
        public ViewGroup mFullscreenArea;
        public String mImePackageName;
        public InputMethodManager mImm;
        public LayoutInflater mInflater;
        public ViewGroup mInputFrame;
        public InputMethodService mInputMethodService;
        public ViewGroup mMiuiBottomArea;
        public View mRootView;

        public BottomViewHelper(LayoutInflater inflater, ViewGroup fullscreenArea, ViewGroup inputFrame, View rootView, ViewGroup miuiBottomArea, InputMethodManager imm, String imePackageName, InputMethodService inputMethodService) {
            this.mFullscreenArea = fullscreenArea;
            this.mInputFrame = inputFrame;
            this.mRootView = rootView;
            this.mMiuiBottomArea = miuiBottomArea;
            this.mImm = imm;
            this.mImePackageName = imePackageName;
            this.mInputMethodService = inputMethodService;
            this.mInflater = inflater;
        }
    }

    private static class CloudClipboardContentObserver extends ContentObserver {
        private Context mContext;

        public CloudClipboardContentObserver(Handler handler, Context context) {
            super(handler);
            this.mContext = context;
        }

        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            InputMethodServiceInjector.sNeedCloudClipboardContentShown = true;
            InputMethodServiceInjector.sCloudClipboardContent = InputMethodServiceInjector.getCloudClipboardContent(this.mContext);
            if (InputMethodServiceInjector.sBottomViewHelper.mInputMethodService.isInputViewShown()) {
                InputMethodServiceInjector.addCloudClipboardPopView(this.mContext);
            }
        }
    }

    private static class InputMethodFunctionSelectObserver extends ContentObserver {
        private Context mContext;

        public InputMethodFunctionSelectObserver(Handler handler, Context context) {
            super(handler);
            this.mContext = context;
        }

        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            InputMethodServiceInjector.setFunctionChanged(this.mContext);
        }
    }

    private static class MiuiBottomLayoutListener implements OnGlobalLayoutListener {
        private Context mContext;
        private ViewGroup mMiuiBottomArea;

        public MiuiBottomLayoutListener(ViewGroup miuiBottomArea, Context context) {
            this.mMiuiBottomArea = miuiBottomArea;
            this.mContext = context;
        }

        public void onGlobalLayout() {
            InputMethodServiceInjector.changeViewForMiuiBottom(this.mMiuiBottomArea, this.mContext);
        }
    }

    private static class MiuiBottomStatusObserver extends ContentObserver {
        private Context mContext;

        public MiuiBottomStatusObserver(Context ctx, Handler handler) {
            super(handler);
            this.mContext = ctx;
        }

        public void onChange(boolean selfChange) {
            String str = InputMethodServiceInjector.TAG;
            super.onChange(selfChange);
            try {
                boolean oldMiuiBottomEnabled = InputMethodServiceInjector.sIsMiuiBottomEnabled;
                InputMethodServiceInjector.sIsMiuiBottomEnabled = InputMethodServiceInjector.isMiuiBottomEnabled(this.mContext);
                InputMethodServiceInjector.sNavigationBarFullScreenValue = InputMethodServiceInjector.isNavigationBarFullScreen(this.mContext);
                if (!oldMiuiBottomEnabled && InputMethodServiceInjector.sIsMiuiBottomEnabled) {
                    Log.i(str, "Add miui bottom view.");
                    InputMethodServiceInjector.addMiuiBottomViewInner(this.mContext);
                }
                InputMethodServiceInjector.updateMiuiBottomEnableValue(this.mContext);
            } catch (Exception e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("fail to read user miui bottom choice, ");
                stringBuilder.append(e);
                Log.e(str, stringBuilder.toString());
            }
        }
    }

    private static class MiuiClipboardPhraseListener implements OnClickListener {
        private View mButtonView;
        private Context mContext;
        private InputMethodService mInputMethodService;
        private boolean mIsLeft;

        public MiuiClipboardPhraseListener(Context context, InputMethodService inputMethodService, View buttonView, boolean isLeft) {
            this.mContext = context;
            this.mButtonView = buttonView;
            this.mInputMethodService = inputMethodService;
            this.mIsLeft = isLeft;
        }

        public void onClick(View v) {
            if (InputMethodServiceInjector.sSwitchWindow != null && InputMethodServiceInjector.sSwitchWindow.isShowing()) {
                return;
            }
            if (InputMethodServiceInjector.sClipboardWindow == null || !InputMethodServiceInjector.sClipboardWindow.isShowing()) {
                InputMethodServiceInjector.sClipboardWindow = new InputMethodClipboardPhrasePopupView(this.mContext, this.mInputMethodService.getCurrentInputConnection(), InputMethodServiceInjector.mBottomAreaHeight, this.mIsLeft, InputMethodServiceInjector.sCloudClipboardContent);
                InputMethodServiceInjector.sClipboardWindow.initPopupWindow(this.mButtonView);
                InputMethodAnalyticsUtil.addInputMethodAnalytics(this.mContext, InputMethodServiceInjector.FUNCTION_CLIPBOARD);
            }
        }
    }

    private static class MiuiSwitchInputMethodListener implements OnClickListener {
        private View mButtonView;
        private Context mContext;
        private InputMethodManager mImm;
        private List<InputMethodInfo> mInputMethodInstalledList;
        private InputMethodService mInputMethodService;
        private boolean mIsLeft;
        private Set<String> mSupportImeSet = InputMethodServiceInjector.sImeMinVersionSupport.keySet();

        public MiuiSwitchInputMethodListener(InputMethodService inputMethodService, InputMethodManager imm, Context context, View buttonView, boolean isLeft) {
            this.mImm = imm;
            this.mInputMethodService = inputMethodService;
            this.mContext = context;
            this.mButtonView = buttonView;
            this.mIsLeft = isLeft;
        }

        public void onClick(View v) {
            if (InputMethodServiceInjector.sSwitchWindow != null && InputMethodServiceInjector.sSwitchWindow.isShowing()) {
                return;
            }
            if (InputMethodServiceInjector.sClipboardWindow == null || !InputMethodServiceInjector.sClipboardWindow.isShowing()) {
                this.mInputMethodInstalledList = this.mImm.getEnabledInputMethodList();
                deleteNotSupportIme();
                InputMethodServiceInjector.sSwitchWindow = new InputMethodSwitchPopupView(this.mContext, this.mInputMethodInstalledList, this.mInputMethodService, InputMethodServiceInjector.mBottomAreaHeight, this.mIsLeft);
                InputMethodServiceInjector.sSwitchWindow.initPopupWindow();
                InputMethodServiceInjector.sSwitchWindow.showAtLocation(this.mButtonView, 0, 0, 0);
                InputMethodAnalyticsUtil.addInputMethodAnalytics(this.mContext, InputMethodServiceInjector.FUNCTION_SWITCH);
            }
        }

        private void deleteNotSupportIme() {
            Iterator iterator = this.mInputMethodInstalledList.iterator();
            while (iterator.hasNext()) {
                if (!this.mSupportImeSet.contains(((InputMethodInfo) iterator.next()).getPackageName())) {
                    iterator.remove();
                }
            }
        }
    }

    private static class MiuiVoiceInputListener implements OnClickListener {
        private Context mContext;

        public MiuiVoiceInputListener(Context context) {
            this.mContext = context;
        }

        public void onClick(View v) {
            Intent intent = new Intent("miui.intent.action.START_IME_VOICE_INPUT");
            intent.setPackage(this.mContext.getPackageName());
            this.mContext.sendBroadcast(intent);
            InputMethodAnalyticsUtil.addInputMethodAnalytics(this.mContext, InputMethodServiceInjector.FUNCTION_VOICE);
        }
    }

    private static class MiuiXiaoAiInputListener implements OnTouchListener {
        private Context mContext;

        public MiuiXiaoAiInputListener(Context context) {
            this.mContext = context;
        }

        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            Intent intent;
            if (action == 0) {
                action = (InputMethodServiceInjector.getScreenHeightWithoutStatusBar(this.mContext) - InputMethodServiceInjector.sBottomViewHelper.mMiuiBottomArea.getHeight()) - InputMethodServiceInjector.sTmpInsets.visibleTopInsets;
                intent = new Intent("com.xiaomi.inputmethod.ACTION_INPUT_METHOD_BUTTON_DOWN");
                intent.putExtra("down_time", System.currentTimeMillis());
                intent.putExtra("input_method_height", action);
                intent.putExtra("input_method_bottom_bar_height", InputMethodServiceInjector.sBottomViewHelper.mMiuiBottomArea.getHeight());
                intent.putExtra("inputMethodCallBack", new RemoteCallback(new OnResultListener() {
                    public void onResult(Bundle result) {
                        String textFromXiaoAi = (String) result.get("textFromXiaoAi");
                        String textFromXiaoAiComposing = (String) result.get("composingFromXiaoAi");
                        InputConnection inputConnection = InputMethodServiceInjector.sBottomViewHelper.mInputMethodService.getCurrentInputConnection();
                        if (!TextUtils.isEmpty(textFromXiaoAi)) {
                            inputConnection.commitText(textFromXiaoAi, 1);
                        } else if (!TextUtils.isEmpty(textFromXiaoAiComposing)) {
                            inputConnection.setComposingText(textFromXiaoAiComposing, 1);
                        }
                    }
                }));
                InputMethodServiceInjector.startXiaoAiService(intent, this.mContext);
                InputMethodAnalyticsUtil.addInputMethodAnalytics(this.mContext, InputMethodServiceInjector.FUNCTION_XIAOAI);
            } else if (action == 1) {
                intent = new Intent("com.xiaomi.inputmethod.ACTION_INPUT_METHOD_BUTTON_UP");
                intent.putExtra("up_time", System.currentTimeMillis());
                intent.putExtra("message_is_send", true);
                InputMethodServiceInjector.startXiaoAiService(intent, this.mContext);
            }
            return true;
        }
    }

    InputMethodServiceInjector() {
    }

    static {
        String str = "";
        sLeftSelectedKey = str;
        sRightSelectedKey = str;
        sCloudClipboardContent = str;
        String str2 = "com.iflytek.inputmethod.miui";
        sImeMinVersionSupport.put(str2, Integer.valueOf(7912));
        HashMap hashMap = sImeMinVersionSupport;
        Integer valueOf = Integer.valueOf(MetricsEvent.PROVISIONING_CANCELLED);
        str = "com.sohu.inputmethod.sogou.xiaomi";
        hashMap.put(str, valueOf);
        hashMap = sImeMinVersionSupport;
        Integer valueOf2 = Integer.valueOf(MetricsEvent.ACTION_SELECT_SUPPORT_FRAGMENT);
        String str3 = "com.baidu.input_mi";
        hashMap.put(str3, valueOf2);
        sImeLastMiui10Version.put(str2, Integer.valueOf(7913));
        sImeLastMiui10Version.put(str, valueOf);
        sImeLastMiui10Version.put(str3, valueOf2);
    }

    static void onBadTokenException(InputMethodService ims, BadTokenException e) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Catch a BadTokeException: ");
        stringBuilder.append(e.getMessage());
        Log.d(TAG, stringBuilder.toString());
        Context context = ims;
        ims.mWindow = new SoftInputWindow(context, "InputMethod", ims.mTheme, null, null, ims.mDispatcherState, 2011, 80, false);
        ims.initViews();
        ims.mWindow.getWindow().setLayout(-1, -2);
    }

    public static void addMiuiBottomView(LayoutInflater inflater, ViewGroup fullscreenArea, ViewGroup inputFrame, View rootView, ViewGroup miuiBottomArea, InputMethodManager imm, InputMethodService inputMethodService) {
        if (checkMiuiBottomSupport()) {
            Context context = inputMethodService.getApplicationContext();
            String imePackageName = context.getPackageName();
            if (!isImeSupport(context)) {
                return;
            }
            if (isScreenLandscape(context)) {
                sIsScreenLandscape = true;
                sCanShowMiuiBottom = false;
                sNeedHandleComputeInsets = false;
                maybeDismissPopupWindow();
                return;
            }
            sIsScreenLandscape = false;
            sBottomViewHelper = new BottomViewHelper(inflater, fullscreenArea, inputFrame, rootView, miuiBottomArea, imm, imePackageName, inputMethodService);
            sCloudClipboardContent = getCloudClipboardContent(context);
            registerObserver(context);
            sIsMiuiBottomEnabled = isMiuiBottomEnabled(context);
            if (sIsMiuiBottomEnabled) {
                sNavigationBarFullScreenValue = isNavigationBarFullScreen(context);
                updateMiuiBottomEnableValue(context);
                addMiuiBottomViewInner(context);
                return;
            }
            Log.i(TAG, "MiuiBottom is not enabled ");
        }
    }

    private static void addMiuiBottomViewInner(Context context) {
        ViewGroup viewGroup = sBottomViewHelper.mMiuiBottomArea;
        String str = TAG;
        if (viewGroup == null) {
            Log.e(str, "MiuiBottomArea is null.");
        } else if (sBottomViewHelper.mMiuiBottomArea.getChildCount() != 0) {
            Log.i(str, "MiuiBottomArea only can add once");
        } else {
            View view = sBottomViewHelper.mInflater.inflate(MiuiBottomConfig.getLayoutForDevice(), sBottomViewHelper.mMiuiBottomArea, false);
            Integer lastMiui10Version = (Integer) sImeLastMiui10Version.get(context.getPackageName());
            if (lastMiui10Version != null && getVersionCode(context) <= lastMiui10Version.intValue()) {
                view.setBackgroundColor(context.getResources().getColor(R.color.input_bottom_background_color_old));
            }
            sLeftButton = (ImageView) view.findViewById(R.id.input_bottom_button_left);
            sRightButton = (ImageView) view.findViewById(R.id.input_bottom_button_right);
            sLeftLayout = (LinearLayout) view.findViewById(R.id.input_bottom_layout_left);
            sRightLayout = (LinearLayout) view.findViewById(R.id.input_bottom_layout_right);
            setFunctionChanged(context);
            setPrimaryClipChangedListener(context);
            sBottomViewHelper.mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new MiuiBottomLayoutListener(sBottomViewHelper.mMiuiBottomArea, context));
            sBottomViewHelper.mMiuiBottomArea.addView(view);
        }
    }

    private static void registerObserver(Context context) {
        if (sMiuiBottomStatusObserver == null) {
            sMiuiBottomStatusObserver = new MiuiBottomStatusObserver(context, new Handler());
            context.getContentResolver().registerContentObserver(Secure.getUriFor(MiuiSettings.Secure.ENABLE_MIUI_IME_BOTTOM_VIEW), false, sMiuiBottomStatusObserver, context.getUserId());
            context.getContentResolver().registerContentObserver(Global.getUriFor(MiuiSettings.Global.FORCE_FSG_NAV_BAR), false, sMiuiBottomStatusObserver, context.getUserId());
        }
        if (sInputMethodFunctionSelectObserver == null) {
            sInputMethodFunctionSelectObserver = new InputMethodFunctionSelectObserver(new Handler(), context);
            context.getContentResolver().registerContentObserver(Secure.getUriFor(MiuiSettings.Secure.FULL_SCREEN_KEYBOARD_LEFT_FUNCTION), false, sInputMethodFunctionSelectObserver, context.getUserId());
            context.getContentResolver().registerContentObserver(Secure.getUriFor(MiuiSettings.Secure.FULL_SCREEN_KEYBOARD_RIGHT_FUNCTION), false, sInputMethodFunctionSelectObserver, context.getUserId());
        }
        if (sCloudClipboardContentObserver == null) {
            sCloudClipboardContentObserver = new CloudClipboardContentObserver(new Handler(), context);
            context.getContentResolver().registerContentObserver(Secure.getUriFor(MiuiSettings.Secure.CLOUD_CLIPBOARD_CONTENT_SAVED), false, sCloudClipboardContentObserver, context.getUserId());
        }
    }

    private static void changeViewForMiuiBottom(ViewGroup miuiBottomArea, Context context) {
        boolean z = sCanShowMiuiBottom;
        String str = TAG;
        if (z) {
            miuiBottomArea.setVisibility(0);
            LayoutParams inputFrameLp = (LayoutParams) sBottomViewHelper.mInputFrame.getLayoutParams();
            int rootViewHeight = sBottomViewHelper.mRootView.getMeasuredHeight();
            int fullScreenHeight = sBottomViewHelper.mFullscreenArea.getMeasuredHeight();
            int inputAreaHeight = sBottomViewHelper.mInputFrame.getMeasuredHeight();
            miuiBottomArea.measure(0, 0);
            int miuiBottomHeight = miuiBottomArea.getMeasuredHeight();
            mBottomAreaHeight = miuiBottomHeight;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("rootViewHeight : ");
            stringBuilder.append(rootViewHeight);
            stringBuilder.append(" fullScreenHeight:");
            stringBuilder.append(fullScreenHeight);
            stringBuilder.append(" inputAreaHeight:");
            stringBuilder.append(inputAreaHeight);
            stringBuilder.append(" miuiBottomHeight:");
            stringBuilder.append(miuiBottomHeight);
            Log.i(str, stringBuilder.toString());
            if (isInputFrameFullScreen(inputFrameLp, inputAreaHeight, context)) {
                inputFrameLp.height = 0;
                inputFrameLp.weight = 1.0f;
                sBottomViewHelper.mInputFrame.setLayoutParams(inputFrameLp);
                Log.i(str, "set  mInputFrame weight =1");
            } else if (rootViewHeight != 0) {
                int heightLeftForMIUI = (rootViewHeight - fullScreenHeight) - inputAreaHeight;
                stringBuilder = new StringBuilder();
                stringBuilder.append("heightLeftForMIUI ");
                stringBuilder.append(heightLeftForMIUI);
                Log.i(str, stringBuilder.toString());
                if (heightLeftForMIUI < miuiBottomHeight) {
                    int fullScreenAreaNewHeight = fullScreenHeight - (miuiBottomHeight - heightLeftForMIUI);
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("fullScreenArea New Height ");
                    stringBuilder2.append(fullScreenAreaNewHeight);
                    Log.i(str, stringBuilder2.toString());
                    LayoutParams layoutParams = (LayoutParams) sBottomViewHelper.mFullscreenArea.getLayoutParams();
                    layoutParams.height = fullScreenAreaNewHeight;
                    sBottomViewHelper.mFullscreenArea.setLayoutParams(layoutParams);
                }
            }
            return;
        }
        Log.i(str, "Can't show Miui bottom view.");
        miuiBottomArea.setVisibility(8);
    }

    private static void updateMiuiBottomEnableValue(Context context) {
        boolean isMiuiBottomSupport = isImeSupport(context);
        boolean z = true;
        boolean z2 = isMiuiBottomSupport && sIsMiuiBottomEnabled && !sIsScreenLandscape && sNavigationBarFullScreenValue && !sFloatEnable;
        sCanShowMiuiBottom = z2;
        if (!(isMiuiBottomSupport && sIsMiuiBottomEnabled && !sIsScreenLandscape && sNavigationBarFullScreenValue)) {
            z = false;
        }
        sNeedHandleComputeInsets = z;
    }

    private static void setFunctionChanged(android.content.Context r20) {
        /*
        r6 = r20;
        r0 = r20.getContentResolver();
        r1 = "full_screen_keyboard_left_function";
        r0 = android.provider.Settings.Secure.getString(r0, r1);
        sLeftSelectedKey = r0;
        r0 = r20.getContentResolver();
        r1 = "full_screen_keyboard_right_function";
        r0 = android.provider.Settings.Secure.getString(r0, r1);
        sRightSelectedKey = r0;
        r0 = sLeftSelectedKey;
        r0 = android.text.TextUtils.isEmpty(r0);
        r7 = "InputMethodService";
        r8 = "no_function";
        r9 = "xiaoai_input";
        r10 = "voice_input";
        r12 = 285671615; // 0x110700bf float:1.0649852E-28 double:1.41140531E-315;
        r13 = "clipboard_phrase";
        r14 = "switch_input_method";
        r15 = 285671616; // 0x110700c0 float:1.0649853E-28 double:1.411405315E-315;
        r5 = 4;
        r4 = 3;
        r3 = 2;
        r2 = 1;
        r1 = 0;
        r11 = 0;
        if (r0 != 0) goto L_0x0163;
    L_0x003d:
        r0 = sLeftSelectedKey;
        r16 = r0.hashCode();
        switch(r16) {
            case 274034301: goto L_0x0067;
            case 858745218: goto L_0x005f;
            case 1030413522: goto L_0x0057;
            case 1037140118: goto L_0x004f;
            case 1283065377: goto L_0x0047;
            default: goto L_0x0046;
        };
    L_0x0046:
        goto L_0x006f;
    L_0x0047:
        r0 = r0.equals(r14);
        if (r0 == 0) goto L_0x0046;
    L_0x004d:
        r0 = r3;
        goto L_0x0070;
    L_0x004f:
        r0 = r0.equals(r8);
        if (r0 == 0) goto L_0x0046;
    L_0x0055:
        r0 = r5;
        goto L_0x0070;
    L_0x0057:
        r0 = r0.equals(r9);
        if (r0 == 0) goto L_0x0046;
    L_0x005d:
        r0 = r2;
        goto L_0x0070;
    L_0x005f:
        r0 = r0.equals(r13);
        if (r0 == 0) goto L_0x0046;
    L_0x0065:
        r0 = r4;
        goto L_0x0070;
    L_0x0067:
        r0 = r0.equals(r10);
        if (r0 == 0) goto L_0x0046;
    L_0x006d:
        r0 = r11;
        goto L_0x0070;
    L_0x006f:
        r0 = -1;
    L_0x0070:
        if (r0 == 0) goto L_0x013a;
    L_0x0072:
        if (r0 == r2) goto L_0x0112;
    L_0x0074:
        if (r0 == r3) goto L_0x00d5;
    L_0x0076:
        if (r0 == r4) goto L_0x00a9;
    L_0x0078:
        if (r0 == r5) goto L_0x0093;
    L_0x007a:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r3 = "Error leftSelectedFunction : ";
        r0.append(r3);
        r3 = sLeftSelectedKey;
        r0.append(r3);
        r0 = r0.toString();
        android.util.Log.e(r7, r0);
        r12 = r1;
        goto L_0x0162;
    L_0x0093:
        sCanPopCloudClipboardViewLeft = r11;
        r0 = sLeftLayout;
        r3 = 8;
        r0.setVisibility(r3);
        r0 = sLeftLayout;
        r0.setOnClickListener(r1);
        r0 = sLeftLayout;
        r0.setOnTouchListener(r1);
        r12 = r1;
        goto L_0x0162;
    L_0x00a9:
        sCanPopCloudClipboardViewLeft = r2;
        r0 = sLeftLayout;
        r0.setVisibility(r11);
        r0 = sLeftLayout;
        r0.setOnTouchListener(r1);
        r0 = sLeftButton;
        r3 = r20.getResources();
        r3 = r3.getDrawable(r12);
        r0.setBackground(r3);
        r0 = new android.inputmethodservice.InputMethodServiceInjector$MiuiClipboardPhraseListener;
        r3 = sBottomViewHelper;
        r3 = r3.mInputMethodService;
        r4 = sLeftLayout;
        r0.<init>(r6, r3, r4, r2);
        r3 = sLeftLayout;
        r3.setOnClickListener(r0);
        r12 = r1;
        goto L_0x0162;
    L_0x00d5:
        sCanPopCloudClipboardViewLeft = r11;
        r0 = sLeftLayout;
        r0.setVisibility(r11);
        r0 = sLeftLayout;
        r0.setOnTouchListener(r1);
        r0 = sLeftButton;
        r3 = r20.getResources();
        r3 = r3.getDrawable(r15);
        r0.setBackground(r3);
        r17 = new android.inputmethodservice.InputMethodServiceInjector$MiuiSwitchInputMethodListener;
        r0 = sBottomViewHelper;
        r3 = r0.mInputMethodService;
        r0 = sBottomViewHelper;
        r4 = r0.mImm;
        r18 = sLeftLayout;
        r19 = 1;
        r0 = r17;
        r12 = r1;
        r1 = r3;
        r3 = r2;
        r2 = r4;
        r4 = 2;
        r3 = r20;
        r4 = r18;
        r5 = r19;
        r0.<init>(r1, r2, r3, r4, r5);
        r1 = sLeftLayout;
        r1.setOnClickListener(r0);
        goto L_0x0162;
    L_0x0112:
        r12 = r1;
        sCanPopCloudClipboardViewLeft = r11;
        r0 = sLeftLayout;
        r0.setVisibility(r11);
        r0 = sLeftLayout;
        r0.setOnClickListener(r12);
        r0 = sLeftButton;
        r1 = r20.getResources();
        r2 = 285671618; // 0x110700c2 float:1.0649856E-28 double:1.411405324E-315;
        r1 = r1.getDrawable(r2);
        r0.setBackground(r1);
        r0 = new android.inputmethodservice.InputMethodServiceInjector$MiuiXiaoAiInputListener;
        r0.<init>(r6);
        r1 = sLeftLayout;
        r1.setOnTouchListener(r0);
        goto L_0x0162;
    L_0x013a:
        r12 = r1;
        sCanPopCloudClipboardViewLeft = r11;
        r0 = sLeftLayout;
        r0.setVisibility(r11);
        r0 = sLeftLayout;
        r0.setOnTouchListener(r12);
        r0 = sLeftButton;
        r1 = r20.getResources();
        r2 = 285671617; // 0x110700c1 float:1.0649855E-28 double:1.41140532E-315;
        r1 = r1.getDrawable(r2);
        r0.setBackground(r1);
        r0 = new android.inputmethodservice.InputMethodServiceInjector$MiuiVoiceInputListener;
        r0.<init>(r6);
        r1 = sLeftLayout;
        r1.setOnClickListener(r0);
    L_0x0162:
        goto L_0x018e;
    L_0x0163:
        r12 = r1;
        sCanPopCloudClipboardViewLeft = r11;
        r0 = sLeftButton;
        r1 = r20.getResources();
        r1 = r1.getDrawable(r15);
        r0.setBackground(r1);
        r16 = new android.inputmethodservice.InputMethodServiceInjector$MiuiSwitchInputMethodListener;
        r0 = sBottomViewHelper;
        r1 = r0.mInputMethodService;
        r0 = sBottomViewHelper;
        r2 = r0.mImm;
        r4 = sLeftLayout;
        r5 = 1;
        r0 = r16;
        r3 = r20;
        r0.<init>(r1, r2, r3, r4, r5);
        r1 = sLeftLayout;
        r1.setOnClickListener(r0);
        sLeftSelectedKey = r14;
    L_0x018e:
        r0 = sRightSelectedKey;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 != 0) goto L_0x02b3;
    L_0x0196:
        r0 = sRightSelectedKey;
        r1 = r0.hashCode();
        switch(r1) {
            case 274034301: goto L_0x01c0;
            case 858745218: goto L_0x01b8;
            case 1030413522: goto L_0x01b0;
            case 1037140118: goto L_0x01a8;
            case 1283065377: goto L_0x01a0;
            default: goto L_0x019f;
        };
    L_0x019f:
        goto L_0x01c8;
    L_0x01a0:
        r0 = r0.equals(r14);
        if (r0 == 0) goto L_0x019f;
    L_0x01a6:
        r0 = 2;
        goto L_0x01c9;
    L_0x01a8:
        r0 = r0.equals(r8);
        if (r0 == 0) goto L_0x019f;
    L_0x01ae:
        r0 = 4;
        goto L_0x01c9;
    L_0x01b0:
        r0 = r0.equals(r9);
        if (r0 == 0) goto L_0x019f;
    L_0x01b6:
        r0 = 1;
        goto L_0x01c9;
    L_0x01b8:
        r0 = r0.equals(r13);
        if (r0 == 0) goto L_0x019f;
    L_0x01be:
        r0 = 3;
        goto L_0x01c9;
    L_0x01c0:
        r0 = r0.equals(r10);
        if (r0 == 0) goto L_0x019f;
    L_0x01c6:
        r0 = r11;
        goto L_0x01c9;
    L_0x01c8:
        r0 = -1;
    L_0x01c9:
        if (r0 == 0) goto L_0x028b;
    L_0x01cb:
        r1 = 1;
        if (r0 == r1) goto L_0x0264;
    L_0x01ce:
        r2 = 2;
        if (r0 == r2) goto L_0x0232;
    L_0x01d1:
        r2 = 3;
        if (r0 == r2) goto L_0x0204;
    L_0x01d4:
        r2 = 4;
        if (r0 == r2) goto L_0x01ef;
    L_0x01d7:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Error rightSelectedFunction : ";
        r0.append(r1);
        r1 = sRightSelectedKey;
        r0.append(r1);
        r0 = r0.toString();
        android.util.Log.e(r7, r0);
        goto L_0x02b2;
    L_0x01ef:
        sCanPopCloudClipboardViewRight = r11;
        r0 = sRightLayout;
        r1 = 8;
        r0.setVisibility(r1);
        r0 = sRightLayout;
        r0.setOnClickListener(r12);
        r0 = sRightLayout;
        r0.setOnTouchListener(r12);
        goto L_0x02b2;
    L_0x0204:
        sCanPopCloudClipboardViewRight = r1;
        r0 = sRightLayout;
        r0.setVisibility(r11);
        r0 = sRightLayout;
        r0.setOnTouchListener(r12);
        r0 = sRightButton;
        r1 = r20.getResources();
        r2 = 285671615; // 0x110700bf float:1.0649852E-28 double:1.41140531E-315;
        r1 = r1.getDrawable(r2);
        r0.setBackground(r1);
        r0 = new android.inputmethodservice.InputMethodServiceInjector$MiuiClipboardPhraseListener;
        r1 = sBottomViewHelper;
        r1 = r1.mInputMethodService;
        r2 = sRightLayout;
        r0.<init>(r6, r1, r2, r11);
        r1 = sRightLayout;
        r1.setOnClickListener(r0);
        goto L_0x02b2;
    L_0x0232:
        sCanPopCloudClipboardViewRight = r11;
        r0 = sRightLayout;
        r0.setVisibility(r11);
        r0 = sRightLayout;
        r0.setOnTouchListener(r12);
        r0 = sRightButton;
        r1 = r20.getResources();
        r1 = r1.getDrawable(r15);
        r0.setBackground(r1);
        r7 = new android.inputmethodservice.InputMethodServiceInjector$MiuiSwitchInputMethodListener;
        r0 = sBottomViewHelper;
        r1 = r0.mInputMethodService;
        r0 = sBottomViewHelper;
        r2 = r0.mImm;
        r4 = sRightLayout;
        r5 = 0;
        r0 = r7;
        r3 = r20;
        r0.<init>(r1, r2, r3, r4, r5);
        r1 = sRightLayout;
        r1.setOnClickListener(r0);
        goto L_0x02b2;
    L_0x0264:
        sCanPopCloudClipboardViewRight = r11;
        r0 = sRightLayout;
        r0.setVisibility(r11);
        r0 = sRightLayout;
        r0.setOnClickListener(r12);
        r0 = sRightButton;
        r1 = r20.getResources();
        r2 = 285671618; // 0x110700c2 float:1.0649856E-28 double:1.411405324E-315;
        r1 = r1.getDrawable(r2);
        r0.setBackground(r1);
        r0 = new android.inputmethodservice.InputMethodServiceInjector$MiuiXiaoAiInputListener;
        r0.<init>(r6);
        r1 = sRightLayout;
        r1.setOnTouchListener(r0);
        goto L_0x02b2;
    L_0x028b:
        sCanPopCloudClipboardViewRight = r11;
        r0 = sRightLayout;
        r0.setVisibility(r11);
        r0 = sRightLayout;
        r0.setOnTouchListener(r12);
        r0 = sRightButton;
        r1 = r20.getResources();
        r2 = 285671617; // 0x110700c1 float:1.0649855E-28 double:1.41140532E-315;
        r1 = r1.getDrawable(r2);
        r0.setBackground(r1);
        r0 = new android.inputmethodservice.InputMethodServiceInjector$MiuiVoiceInputListener;
        r0.<init>(r6);
        r1 = sRightLayout;
        r1.setOnClickListener(r0);
    L_0x02b2:
        goto L_0x02d8;
    L_0x02b3:
        r1 = 1;
        sCanPopCloudClipboardViewRight = r1;
        r0 = sRightButton;
        r1 = r20.getResources();
        r2 = 285671615; // 0x110700bf float:1.0649852E-28 double:1.41140531E-315;
        r1 = r1.getDrawable(r2);
        r0.setBackground(r1);
        r0 = new android.inputmethodservice.InputMethodServiceInjector$MiuiClipboardPhraseListener;
        r1 = sBottomViewHelper;
        r1 = r1.mInputMethodService;
        r2 = sRightLayout;
        r0.<init>(r6, r1, r2, r11);
        r1 = sRightLayout;
        r1.setOnClickListener(r0);
        sRightSelectedKey = r13;
    L_0x02d8:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.inputmethodservice.InputMethodServiceInjector.setFunctionChanged(android.content.Context):void");
    }

    private static boolean isMiuiBottomEnabled(Context context) {
        return Secure.getInt(context.getContentResolver(), MiuiSettings.Secure.ENABLE_MIUI_IME_BOTTOM_VIEW, 1) != 0;
    }

    private static boolean isNavigationBarFullScreen(Context context) {
        return MiuiSettings.Global.getBoolean(context.getContentResolver(), MiuiSettings.Global.FORCE_FSG_NAV_BAR);
    }

    private static boolean isScreenLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == 2;
    }

    private static String getCloudClipboardContent(Context context) {
        return Secure.getString(context.getContentResolver(), MiuiSettings.Secure.CLOUD_CLIPBOARD_CONTENT_SAVED);
    }

    private static void startXiaoAiService(Intent intent, Context context) {
        try {
            intent.setPackage("com.miui.voiceassist");
            context.startService(intent);
        } catch (Exception e) {
            Log.e(TAG, "XiaoAi service not found");
        }
    }

    public static void setPrimaryClipChangedListener(final Context context) {
        final ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            clipboardManager.addPrimaryClipChangedListener(new OnPrimaryClipChangedListener() {
                public void onPrimaryClipChanged() {
                    HandlerThread handlerThread = new HandlerThread("setPrimaryClipChangedListener");
                    handlerThread.start();
                    new Handler(handlerThread.getLooper()).post(new Runnable() {
                        public void run() {
                            ClipData data = clipboardManager.getPrimaryClip();
                            if (data != null && data.getItemCount() > 0) {
                                Item item = data.getItemAt(null);
                                if (item != null && !TextUtils.isEmpty(item.getText())) {
                                    ClipboardJsonUtil.addClipboard(item.getText().toString(), context);
                                }
                            }
                        }
                    });
                    handlerThread.quitSafely();
                }
            });
        }
    }

    private static boolean isInputFrameFullScreen(LayoutParams inputFrameLp, int inputAreaHeight, Context context) {
        if (inputFrameLp.height == -1) {
            return true;
        }
        if (inputAreaHeight == 0 || inputAreaHeight < getScreenHeightWithoutStatusBar(context)) {
            return false;
        }
        return true;
    }

    private static int getScreenHeightWithoutStatusBar(Context context) {
        if (sScreenHeightWithoutStatusBar == -1) {
            sScreenHeightWithoutStatusBar = getScreenHeight(context) - getStatusBarHeight(context);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ScreenHeightWithoutStatusBar : ");
            stringBuilder.append(sScreenHeightWithoutStatusBar);
            Log.i(TAG, stringBuilder.toString());
        }
        return sScreenHeightWithoutStatusBar;
    }

    private static int getScreenHeight(Context context) {
        if (sScreenHeight == -1) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getRealMetrics(dm);
            sScreenHeight = dm.heightPixels;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ScreenHeight : ");
            stringBuilder.append(sScreenHeightWithoutStatusBar);
            Log.i(TAG, stringBuilder.toString());
        }
        return sScreenHeight;
    }

    private static int getStatusBarHeight(Context context) {
        if (sStatusBarHeight == -1) {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            sStatusBarHeight = 0;
            if (resourceId > 0) {
                sStatusBarHeight = resources.getDimensionPixelSize(resourceId);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("sStatusBarHeight : ");
            stringBuilder.append(sStatusBarHeight);
            Log.i(TAG, stringBuilder.toString());
        }
        return sStatusBarHeight;
    }

    public static void afterComputeInsets(Insets mTmpInsets, InputMethodService inputMethodService) {
        if (sNeedHandleComputeInsets && sBottomViewHelper != null) {
            Context context = inputMethodService.getApplicationContext();
            Rect rect = mTmpInsets.touchableRegion.getBounds();
            sTmpInsets = mTmpInsets;
            if (mTmpInsets.contentTopInsets > (getScreenHeightWithoutStatusBar(context) - sBottomViewHelper.mMiuiBottomArea.getHeight()) - 20) {
                if (!sFloatEnable) {
                    sFloatEnable = true;
                    updateMiuiBottomEnableValue(context);
                    changeViewForMiuiBottom(sBottomViewHelper.mMiuiBottomArea, context);
                }
            } else if (sFloatEnable) {
                sFloatEnable = false;
                updateMiuiBottomEnableValue(context);
                changeViewForMiuiBottom(sBottomViewHelper.mMiuiBottomArea, context);
            }
            int screenHeight = getScreenHeight(context);
            if (sCanShowMiuiBottom && rect.bottom != screenHeight) {
                rect.bottom = screenHeight;
                mTmpInsets.touchableRegion.set(rect);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Reset touchable region Info.touchableRegion.set ");
                stringBuilder.append(mTmpInsets.touchableRegion.toString());
                Log.i(TAG, stringBuilder.toString());
            }
        }
    }

    public static void onWindowShown(InputMethodService inputMethodService) {
        if (sNeedCloudClipboardContentShown) {
            addCloudClipboardPopView(inputMethodService.getApplicationContext());
        }
    }

    public static void onWindowHidden(InputMethodService inputMethodService) {
        if (sCanShowMiuiBottom) {
            maybeDismissPopupWindow();
            if (!TextUtils.isEmpty(sLeftSelectedKey) || !TextUtils.isEmpty(sRightSelectedKey)) {
                String str = sLeftSelectedKey;
                String str2 = FUNCTION_XIAOAI;
                if (TextUtils.equals(str, str2) || TextUtils.equals(sRightSelectedKey, str2)) {
                    startXiaoAiService(new Intent("com.xiaomi.inputmethod.ACTION_INPUT_METHOD_BUTTON_DISAPPEAR"), inputMethodService.getApplicationContext());
                }
            }
        }
    }

    private static void addCloudClipboardPopView(Context context) {
        if (TextUtils.isEmpty(sCloudClipboardContent)) {
            Log.e(TAG, "cloud clipboard content is null");
        } else {
            boolean isLeft = false;
            if (sCanPopCloudClipboardViewLeft || sCanPopCloudClipboardViewRight) {
                if (sCanPopCloudClipboardViewLeft) {
                    isLeft = true;
                }
                if (sCanPopCloudClipboardViewRight) {
                    isLeft = false;
                }
                InputMethodCloudClipboardPopupView inputMethodCloudClipboardPopupView = mCloudClipboardPopupView;
                if (inputMethodCloudClipboardPopupView != null && inputMethodCloudClipboardPopupView.isShowing()) {
                    mCloudClipboardPopupView.dismiss();
                }
                mCloudClipboardPopupView = new InputMethodCloudClipboardPopupView(context, sBottomViewHelper.mInputMethodService.getCurrentInputConnection(), mBottomAreaHeight, isLeft, sCloudClipboardContent);
                mCloudClipboardPopupView.initPopupWindow();
                if (isLeft) {
                    mCloudClipboardPopupView.showAtLocation((View) sLeftLayout, 0, 0, 0);
                } else {
                    mCloudClipboardPopupView.showAtLocation((View) sRightLayout, 0, 0, 0);
                }
                sNeedCloudClipboardContentShown = false;
            }
        }
    }

    private static void maybeDismissPopupWindow() {
        InputMethodClipboardPhrasePopupView inputMethodClipboardPhrasePopupView = sClipboardWindow;
        if (inputMethodClipboardPhrasePopupView != null) {
            inputMethodClipboardPhrasePopupView.dismiss();
        }
        InputMethodSwitchPopupView inputMethodSwitchPopupView = sSwitchWindow;
        if (inputMethodSwitchPopupView != null) {
            inputMethodSwitchPopupView.dismiss();
        }
    }

    private static boolean checkMiuiBottomSupport() {
        if (sIsMiuiBottomSupport == -1) {
            sIsMiuiBottomSupport = SystemProperties.getInt("ro.miui.support_miui_ime_bottom", 0);
        }
        if (sIsMiuiBottomSupport == 1) {
            return true;
        }
        return false;
    }

    public static void onDestroy(Context context) {
        if (checkMiuiBottomSupport()) {
            if (sMiuiBottomStatusObserver != null) {
                context.getContentResolver().unregisterContentObserver(sMiuiBottomStatusObserver);
                sMiuiBottomStatusObserver = null;
            }
            if (sInputMethodFunctionSelectObserver != null) {
                context.getContentResolver().unregisterContentObserver(sInputMethodFunctionSelectObserver);
                sInputMethodFunctionSelectObserver = null;
            }
            if (sCloudClipboardContentObserver != null) {
                context.getContentResolver().unregisterContentObserver(sCloudClipboardContentObserver);
                sCloudClipboardContentObserver = null;
            }
        }
    }

    private static boolean isImeSupport(Context context) {
        String currentPackage = sIsImeSupport;
        boolean z = false;
        if (currentPackage == -1) {
            Integer minSupportVersionCode = (Integer) sImeMinVersionSupport.get(context.getPackageName());
            if (minSupportVersionCode == null || getVersionCode(context) < minSupportVersionCode.intValue()) {
                sIsImeSupport = 0;
                return false;
            }
            sIsImeSupport = 1;
            return true;
        }
        if (currentPackage == 1) {
            z = true;
        }
        return z;
    }

    public static int getVersionCode(Context context) {
        if (sVersionCode == -1) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                if (packageInfo != null) {
                    sVersionCode = packageInfo.versionCode;
                }
            } catch (NameNotFoundException e) {
                Log.e(TAG, "check Ime Version failed", e);
            }
        }
        return sVersionCode;
    }
}
