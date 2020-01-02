package miui.util;

import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.miui.R;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MiuiSettings;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import miui.os.Build;
import miui.os.SystemProperties;
import miui.view.MiuiHapticFeedbackConstants;

public class HapticFeedbackUtil {
    public static final String EFFECT_KEY_CALCULATOR = "calculator";
    public static final String EFFECT_KEY_CLOCK_PICKER = "clock_picker";
    public static final String EFFECT_KEY_CLOCK_SECOND = "clock_second";
    public static final String EFFECT_KEY_COMPASS_CALIBRATION = "compass_calibration";
    public static final String EFFECT_KEY_COMPASS_NORTH = "compass_north";
    public static final String EFFECT_KEY_FLICK = "flick";
    public static final String EFFECT_KEY_FLICK_LIGHT = "flick_light";
    public static final String EFFECT_KEY_HOLD = "hold";
    public static final String EFFECT_KEY_HOME_DROP_FINISH = "home_drop_finish";
    public static final String EFFECT_KEY_HOME_PICKUP_START = "home_pickup_start";
    public static final String EFFECT_KEY_LONG_PRESS = "long_press";
    public static final String EFFECT_KEY_MESH_HEAVY = "mesh_heavy";
    public static final String EFFECT_KEY_MESH_LIGHT = "mesh_light";
    public static final String EFFECT_KEY_MESH_NORMAL = "mesh_normal";
    public static final String EFFECT_KEY_PICKUP = "pickup";
    public static final String EFFECT_KEY_POPUP_LIGHT = "popup_light";
    public static final String EFFECT_KEY_POPUP_NORMAL = "popup_normal";
    public static final String EFFECT_KEY_RECORDER_DELETE = "recorder_delete";
    public static final String EFFECT_KEY_RECORDER_FINISH = "recorder_finish";
    public static final String EFFECT_KEY_RECORDER_LIST = "recorder_list";
    public static final String EFFECT_KEY_RECORDER_PAUSE = "recorder_pause";
    public static final String EFFECT_KEY_RECORDER_PLAY = "recorder_play";
    public static final String EFFECT_KEY_RECORDER_RECORD = "recorder_record";
    public static final String EFFECT_KEY_RECORDER_RECORD_PAUSE = "recorder_record_pause";
    public static final String EFFECT_KEY_RECORDER_REWIND = "recorder_rewind";
    public static final String EFFECT_KEY_RECORDER_SLIDER = "recorder_slider";
    public static final String EFFECT_KEY_RECORDER_STOP = "recorder_stop";
    public static final String EFFECT_KEY_SCREEN_BUTTON_RECENT_TASK = "screen_button_recent_task";
    public static final String EFFECT_KEY_SCREEN_BUTTON_VOICE_ASSIST = "screen_button_voice_assist";
    public static final String EFFECT_KEY_SCROLL_EDGE = "scroll_edge";
    public static final String EFFECT_KEY_SWITCH = "switch";
    public static final String EFFECT_KEY_TAP_LIGHT = "tap_light";
    public static final String EFFECT_KEY_TAP_NORMAL = "tap_normal";
    public static final String EFFECT_KEY_TORCH_OFF = "torch_off";
    public static final String EFFECT_KEY_TORCH_ON = "torch_on";
    public static final String EFFECT_KEY_TRIGGER_DRAWER = "trigger_drawer";
    public static final String EFFECT_KEY_VIRTUAL_KEY_DOWN = "virtual_key_down";
    public static final String EFFECT_KEY_VIRTUAL_KEY_LONGPRESS = "virtual_key_longpress";
    public static final String EFFECT_KEY_VIRTUAL_KEY_TAP = "virtual_key_tap";
    public static final String EFFECT_KEY_VIRTUAL_KEY_UP = "virtual_key_up";
    private static final int EFFECT_STRENGTH_DEFAULT = -100;
    private static final int EFFECT_STRENGTH_STRONG = 2;
    private static final SparseArray<String> ID_TO_KEY = new SparseArray();
    public static final boolean IS_IMMERSION_ENABLED = false;
    private static final String KEY_VIBRATE_EX_ENABLED = "ro.haptic.vibrate_ex.enabled";
    private static final float[] LEVEL_FACTOR = new float[]{0.5f, 1.0f, 1.5f};
    private static final String[] LEVEL_SUFFIX = new String[]{".weak", ".normal", ".strong"};
    private static final HashMap<String, String> PROPERTY_KEY = new HashMap();
    private static final List<String> PROPERTY_MOTOR_KEY = new ArrayList();
    private static final String TAG = "HapticFeedbackUtil";
    private static final int VIRTUAL_RELEASED = 2;
    private static final HashMap<String, Integer> sPatternId = new HashMap();
    private static final HashMap<String, long[]> sPatterns = new HashMap();
    private final Context mContext;
    private boolean mIsSupportLinearMotorVibrate;
    private boolean mIsSupportZLinearMotorVibrate;
    private int mLevel;
    private SettingsObserver mSettingsObserver;
    private Vibrator mVibrator;

    private class SettingsObserver extends ContentObserver {
        SettingsObserver(Handler handler) {
            super(handler);
        }

        /* Access modifiers changed, original: 0000 */
        public void observe() {
            HapticFeedbackUtil.this.mContext.getContentResolver().registerContentObserver(System.getUriFor(MiuiSettings.System.HAPTIC_FEEDBACK_LEVEL), false, this);
            HapticFeedbackUtil.this.updateSettings();
        }

        /* Access modifiers changed, original: 0000 */
        public void unobserve() {
            HapticFeedbackUtil.this.mContext.getContentResolver().unregisterContentObserver(this);
        }

        public void onChange(boolean selfChange) {
            HapticFeedbackUtil.this.updateSettings();
        }
    }

    static {
        SparseArray sparseArray = ID_TO_KEY;
        String str = EFFECT_KEY_VIRTUAL_KEY_DOWN;
        sparseArray.put(1, str);
        sparseArray = ID_TO_KEY;
        String str2 = EFFECT_KEY_VIRTUAL_KEY_LONGPRESS;
        sparseArray.put(0, str2);
        sparseArray = ID_TO_KEY;
        String str3 = EFFECT_KEY_VIRTUAL_KEY_TAP;
        sparseArray.put(3, str3);
        ID_TO_KEY.put(2, EFFECT_KEY_VIRTUAL_KEY_UP);
        sparseArray = ID_TO_KEY;
        String str4 = EFFECT_KEY_TAP_NORMAL;
        sparseArray.put(268435456, str4);
        sparseArray = ID_TO_KEY;
        String str5 = EFFECT_KEY_TAP_LIGHT;
        sparseArray.put(268435457, str5);
        sparseArray = ID_TO_KEY;
        String str6 = EFFECT_KEY_FLICK;
        sparseArray.put(268435458, str6);
        sparseArray = ID_TO_KEY;
        String str7 = EFFECT_KEY_FLICK_LIGHT;
        sparseArray.put(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_FLICK_LIGHT, str7);
        sparseArray = ID_TO_KEY;
        String str8 = EFFECT_KEY_SWITCH;
        sparseArray.put(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_SWITCH, str8);
        sparseArray = ID_TO_KEY;
        String str9 = EFFECT_KEY_MESH_HEAVY;
        sparseArray.put(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_MESH_HEAVY, str9);
        sparseArray = ID_TO_KEY;
        String str10 = EFFECT_KEY_MESH_NORMAL;
        sparseArray.put(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_MESH_NORMAL, str10);
        sparseArray = ID_TO_KEY;
        String str11 = EFFECT_KEY_MESH_LIGHT;
        sparseArray.put(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_MESH_LIGHT, str11);
        sparseArray = ID_TO_KEY;
        String str12 = EFFECT_KEY_LONG_PRESS;
        sparseArray.put(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_LONG_PRESS, str12);
        sparseArray = ID_TO_KEY;
        String str13 = EFFECT_KEY_POPUP_NORMAL;
        sparseArray.put(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_POPUP_NORMAL, str13);
        sparseArray = ID_TO_KEY;
        String str14 = EFFECT_KEY_POPUP_LIGHT;
        sparseArray.put(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_POPUP_LIGHT, str14);
        String str15 = str14;
        ID_TO_KEY.put(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_PICK_UP, EFFECT_KEY_PICKUP);
        sparseArray = ID_TO_KEY;
        str14 = EFFECT_KEY_SCROLL_EDGE;
        sparseArray.put(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_SCROLL_EDGE, str14);
        String str16 = str13;
        ID_TO_KEY.put(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_TRIGGER_DRAWER, EFFECT_KEY_TRIGGER_DRAWER);
        ID_TO_KEY.put(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_HOLD, EFFECT_KEY_HOLD);
        PROPERTY_KEY.put(str, "sys.haptic.down");
        PROPERTY_KEY.put(str2, "sys.haptic.long.press");
        PROPERTY_KEY.put(str3, "sys.haptic.tap.normal");
        PROPERTY_KEY.put(EFFECT_KEY_VIRTUAL_KEY_UP, "sys.haptic.up");
        PROPERTY_KEY.put(str4, "sys.haptic.tap.normal");
        PROPERTY_KEY.put(str5, "sys.haptic.tap.light");
        PROPERTY_KEY.put(str6, "sys.haptic.flick");
        PROPERTY_KEY.put(str7, "sys.haptic.flick.light");
        PROPERTY_KEY.put(str8, "sys.haptic.switch");
        PROPERTY_KEY.put(str9, "sys.haptic.mesh.heavy");
        PROPERTY_KEY.put(str10, "sys.haptic.mesh.normal");
        PROPERTY_KEY.put(str11, "sys.haptic.mesh.light");
        PROPERTY_KEY.put(str12, "sys.haptic.long.press");
        PROPERTY_KEY.put(EFFECT_KEY_PICKUP, "sys.haptic.pickup");
        PROPERTY_KEY.put(str14, "sys.haptic.scroll.edge");
        String str17 = str16;
        PROPERTY_KEY.put(str17, "sys.haptic.popup.normal");
        str16 = str14;
        str14 = str15;
        PROPERTY_KEY.put(str14, "sys.haptic.popup.light");
        str15 = str2;
        PROPERTY_KEY.put(EFFECT_KEY_TRIGGER_DRAWER, "sys.haptic.trigger.drawer");
        PROPERTY_KEY.put(EFFECT_KEY_HOLD, "sys.haptic.hold");
        PROPERTY_MOTOR_KEY.add(str4);
        PROPERTY_MOTOR_KEY.add(str5);
        PROPERTY_MOTOR_KEY.add(str6);
        PROPERTY_MOTOR_KEY.add(str7);
        PROPERTY_MOTOR_KEY.add(str8);
        PROPERTY_MOTOR_KEY.add(str9);
        PROPERTY_MOTOR_KEY.add(str10);
        PROPERTY_MOTOR_KEY.add(str11);
        PROPERTY_MOTOR_KEY.add(str12);
        PROPERTY_MOTOR_KEY.add(str17);
        PROPERTY_MOTOR_KEY.add(str14);
        PROPERTY_MOTOR_KEY.add(EFFECT_KEY_TRIGGER_DRAWER);
        PROPERTY_MOTOR_KEY.add(EFFECT_KEY_HOLD);
        PROPERTY_MOTOR_KEY.add(str);
        PROPERTY_MOTOR_KEY.add(str3);
        PROPERTY_MOTOR_KEY.add(str15);
        PROPERTY_MOTOR_KEY.add(str16);
    }

    public HapticFeedbackUtil(Context c, boolean onceOnly) {
        int i;
        Resources sr = Resources.getSystem();
        int config_keyboardTapVibePattern = "android";
        String str = "array";
        int config_longPressVibePattern = sr.getIdentifier("config_longPressVibePattern", str, config_keyboardTapVibePattern);
        int config_virtualKeyVibePattern = sr.getIdentifier("config_virtualKeyVibePattern", str, config_keyboardTapVibePattern);
        config_keyboardTapVibePattern = sr.getIdentifier("config_keyboardTapVibePattern", str, config_keyboardTapVibePattern);
        sPatternId.put(EFFECT_KEY_COMPASS_NORTH, Integer.valueOf(config_longPressVibePattern));
        sPatternId.put(EFFECT_KEY_HOME_PICKUP_START, Integer.valueOf(config_longPressVibePattern));
        HashMap hashMap = sPatternId;
        Integer valueOf = Integer.valueOf(R.array.vibration_recorder_minor_action);
        hashMap.put(EFFECT_KEY_RECORDER_DELETE, valueOf);
        sPatternId.put(EFFECT_KEY_RECORDER_FINISH, valueOf);
        sPatternId.put(EFFECT_KEY_RECORDER_LIST, valueOf);
        hashMap = sPatternId;
        valueOf = Integer.valueOf(R.array.vibration_recorder_major_action);
        hashMap.put(EFFECT_KEY_RECORDER_PAUSE, valueOf);
        sPatternId.put(EFFECT_KEY_RECORDER_PLAY, valueOf);
        sPatternId.put(EFFECT_KEY_RECORDER_RECORD, valueOf);
        sPatternId.put(EFFECT_KEY_RECORDER_RECORD_PAUSE, valueOf);
        sPatternId.put(EFFECT_KEY_RECORDER_REWIND, Integer.valueOf(R.array.vibration_recorder_rewind));
        sPatternId.put(EFFECT_KEY_RECORDER_SLIDER, Integer.valueOf(R.array.vibration_recorder_slider));
        sPatternId.put(EFFECT_KEY_RECORDER_STOP, valueOf);
        sPatternId.put(EFFECT_KEY_SCREEN_BUTTON_RECENT_TASK, Integer.valueOf(config_longPressVibePattern));
        sPatternId.put(EFFECT_KEY_SCREEN_BUTTON_VOICE_ASSIST, Integer.valueOf(config_longPressVibePattern));
        sPatternId.put(EFFECT_KEY_TORCH_OFF, Integer.valueOf(R.array.vibration_torch_off));
        sPatternId.put(EFFECT_KEY_TORCH_ON, Integer.valueOf(R.array.vibration_torch_on));
        sPatternId.put(EFFECT_KEY_VIRTUAL_KEY_LONGPRESS, Integer.valueOf(config_longPressVibePattern));
        sPatternId.put(EFFECT_KEY_VIRTUAL_KEY_DOWN, Integer.valueOf(config_virtualKeyVibePattern));
        sPatternId.put(EFFECT_KEY_VIRTUAL_KEY_TAP, Integer.valueOf(config_keyboardTapVibePattern));
        sPatternId.put(EFFECT_KEY_VIRTUAL_KEY_UP, Integer.valueOf(R.array.vibration_virtual_key_up));
        if (Build.IS_MIUI) {
            i = MiuiSettings.System.HAPTIC_FEEDBACK_LEVEL_DEFAULT;
        } else {
            i = SystemProperties.getInt("ro.haptic.default_level", 1);
        }
        this.mLevel = i;
        this.mIsSupportLinearMotorVibrate = false;
        this.mIsSupportZLinearMotorVibrate = false;
        this.mContext = c;
        this.mVibrator = (Vibrator) this.mContext.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.IS_MIUI) {
            if (onceOnly) {
                updateSettings();
            } else {
                this.mSettingsObserver = new SettingsObserver(new Handler());
                this.mSettingsObserver.observe();
            }
            this.mIsSupportLinearMotorVibrate = isSupportLinearMotorVibrate();
            this.mIsSupportZLinearMotorVibrate = isSupportZLinearMotorVibrate();
        }
    }

    public void release() {
        SettingsObserver settingsObserver = this.mSettingsObserver;
        if (settingsObserver != null) {
            settingsObserver.unobserve();
        }
    }

    public boolean isSupportedEffect(int effectId) {
        return effectId <= 3 || isSupportMotorEffect((String) ID_TO_KEY.get(effectId));
    }

    private boolean isSupportMotorEffect(String key) {
        return this.mIsSupportLinearMotorVibrate && !TextUtils.isEmpty(key) && PROPERTY_MOTOR_KEY.contains(key);
    }

    public void updateSettings() {
        this.mLevel = MiuiSettings.System.getHapticFeedbackLevel(this.mContext);
        this.mLevel = Math.min(2, Math.max(0, this.mLevel));
        sPatterns.clear();
    }

    public void updateImmersionSettings(boolean forceUpdate) {
    }

    public boolean performHapticFeedback(int effectId, boolean always) {
        return performHapticFeedback((String) ID_TO_KEY.get(effectId), always);
    }

    public boolean performHapticFeedback(String key, boolean always) {
        boolean isEmpty = TextUtils.isEmpty(key);
        String str = TAG;
        if (isEmpty) {
            Log.w(str, "fail to get key");
            return false;
        }
        if (!sPatterns.containsKey(key)) {
            sPatterns.put(key, loadPattern(key));
        }
        long[] pattern = (long[]) sPatterns.get(key);
        if (pattern == null || pattern.length == 0) {
            Log.w(str, "vibrate: null or empty pattern");
            return false;
        }
        int i = 2;
        if (!isSupportMotorEffect(key)) {
            if (!(Build.DEVICE.equals("andromeda") || this.mIsSupportLinearMotorVibrate)) {
                i = -100;
            }
            return performHapticFeedback(key, always, i);
        } else if (pattern.length >= 2) {
            return performHapticFeedback(key, always, (int) pattern[1]);
        } else {
            Log.w(str, "fail to read strength id");
            return false;
        }
    }

    public boolean performHapticFeedback(int effectId, boolean always, int effectStrength) {
        return performHapticFeedback((String) ID_TO_KEY.get(effectId), always, effectStrength);
    }

    private boolean isHapticsDisable() {
        return Build.IS_MIUI && MiuiSettings.System.isHapticFeedbackDisabled(this.mContext);
    }

    public boolean performHapticFeedback(String key, boolean always, int effectStrength) {
        boolean z = false;
        if (!this.mVibrator.hasVibrator()) {
            return false;
        }
        if (!always && isHapticsDisable()) {
            return false;
        }
        if (!sPatterns.containsKey(key)) {
            sPatterns.put(key, loadPattern(key));
        }
        long[] pattern = (long[]) sPatterns.get(key);
        if (pattern == null || pattern.length == 0) {
            Log.w(TAG, "vibrate: null or empty pattern");
            return false;
        }
        Vibrator vibrator = this.mVibrator;
        if (this.mIsSupportLinearMotorVibrate || this.mIsSupportZLinearMotorVibrate) {
            z = true;
        }
        VibrateUtils.vibrate(vibrator, z, pattern, effectStrength, -100);
        return true;
    }

    public void stop() {
    }

    private long[] loadPattern(String key) {
        long[] result = null;
        String propertyKey = (String) PROPERTY_KEY.get(key);
        if (propertyKey != null) {
            result = stringToLongArray(SystemProperties.get(propertyKey));
            if (result == null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(propertyKey);
                stringBuilder.append(LEVEL_SUFFIX[this.mLevel]);
                result = stringToLongArray(SystemProperties.get(stringBuilder.toString()));
            }
        }
        if (result != null || !sPatternId.containsKey(key)) {
            return result;
        }
        int id = ((Integer) sPatternId.get(key)).intValue();
        try {
            id = ResourceMapper.resolveReference(this.mContext.getResources(), id);
        } catch (Exception e) {
        }
        return getLongIntArray(this.mContext.getResources(), id);
    }

    private long[] getLongIntArray(Resources r, int resid) {
        int[] ar = r.getIntArray(resid);
        if (ar == null) {
            return null;
        }
        long[] out = new long[ar.length];
        for (int i = 0; i < ar.length; i++) {
            out[i] = (long) (((float) ar[i]) * LEVEL_FACTOR[this.mLevel]);
        }
        return out;
    }

    private long[] stringToLongArray(String pattern) {
        if (TextUtils.isEmpty(pattern)) {
            return null;
        }
        String[] splitStr = pattern.split(",");
        int los = splitStr.length;
        long[] returnByte = new long[los];
        for (int i = 0; i < los; i++) {
            returnByte[i] = Long.parseLong(splitStr[i].trim());
        }
        return returnByte;
    }

    public static boolean isSupportLinearMotorVibrate() {
        return "linear".equals(SystemProperties.get("sys.haptic.motor"));
    }

    private static boolean isSupportZLinearMotorVibrate() {
        return "zlinear".equals(SystemProperties.get("sys.haptic.motor"));
    }

    public static boolean isSupportLinearMotorVibrate(int flag) {
        if (isSupportLinearMotorVibrate()) {
            String effectKey = (String) ID_TO_KEY.get(flag);
            if (PROPERTY_MOTOR_KEY.contains(effectKey) && !TextUtils.isEmpty(SystemProperties.get((String) PROPERTY_KEY.get(effectKey)))) {
                return true;
            }
        }
        return false;
    }

    public boolean performExtHapticFeedback(int effectId) {
        if (!this.mIsSupportLinearMotorVibrate || isHapticsDisable()) {
            return false;
        }
        return VibrateUtils.vibrateExt(this.mVibrator, effectId);
    }

    public boolean performExtHapticFeedback(Uri uri) {
        if (!this.mIsSupportLinearMotorVibrate || isHapticsDisable()) {
            return false;
        }
        return VibrateUtils.vibrateExt(this.mVibrator, uri, this.mContext);
    }
}
