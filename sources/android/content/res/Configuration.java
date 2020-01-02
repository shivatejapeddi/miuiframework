package android.content.res;

import android.annotation.UnsupportedAppUsage;
import android.app.WindowConfiguration;
import android.app.slice.Slice;
import android.content.ConfigurationProto;
import android.content.ResourcesConfigurationProto;
import android.hardware.Camera.Parameters;
import android.miui.BiometricConnect;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Build.VERSION;
import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.provider.Telephony.BaseMmsColumns;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Slog;
import android.util.proto.ProtoInputStream;
import android.util.proto.ProtoOutputStream;
import android.util.proto.WireTypeMismatchException;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.IllformedLocaleException;
import java.util.Locale;
import java.util.Locale.Builder;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public final class Configuration implements Parcelable, Comparable<Configuration> {
    public static final int ASSETS_SEQ_UNDEFINED = 0;
    public static final int COLOR_MODE_HDR_MASK = 12;
    public static final int COLOR_MODE_HDR_NO = 4;
    public static final int COLOR_MODE_HDR_SHIFT = 2;
    public static final int COLOR_MODE_HDR_UNDEFINED = 0;
    public static final int COLOR_MODE_HDR_YES = 8;
    public static final int COLOR_MODE_UNDEFINED = 0;
    public static final int COLOR_MODE_WIDE_COLOR_GAMUT_MASK = 3;
    public static final int COLOR_MODE_WIDE_COLOR_GAMUT_NO = 1;
    public static final int COLOR_MODE_WIDE_COLOR_GAMUT_UNDEFINED = 0;
    public static final int COLOR_MODE_WIDE_COLOR_GAMUT_YES = 2;
    public static final Creator<Configuration> CREATOR = new Creator<Configuration>() {
        public Configuration createFromParcel(Parcel source) {
            return new Configuration(source, null);
        }

        public Configuration[] newArray(int size) {
            return new Configuration[size];
        }
    };
    public static final int DENSITY_DPI_ANY = 65534;
    public static final int DENSITY_DPI_NONE = 65535;
    public static final int DENSITY_DPI_UNDEFINED = 0;
    public static final Configuration EMPTY = new Configuration();
    public static final int HARDKEYBOARDHIDDEN_NO = 1;
    public static final int HARDKEYBOARDHIDDEN_UNDEFINED = 0;
    public static final int HARDKEYBOARDHIDDEN_YES = 2;
    public static final int KEYBOARDHIDDEN_NO = 1;
    public static final int KEYBOARDHIDDEN_SOFT = 3;
    public static final int KEYBOARDHIDDEN_UNDEFINED = 0;
    public static final int KEYBOARDHIDDEN_YES = 2;
    public static final int KEYBOARD_12KEY = 3;
    public static final int KEYBOARD_NOKEYS = 1;
    public static final int KEYBOARD_QWERTY = 2;
    public static final int KEYBOARD_UNDEFINED = 0;
    public static final int MNC_ZERO = 65535;
    public static final int NATIVE_CONFIG_COLOR_MODE = 65536;
    public static final int NATIVE_CONFIG_DENSITY = 256;
    public static final int NATIVE_CONFIG_KEYBOARD = 16;
    public static final int NATIVE_CONFIG_KEYBOARD_HIDDEN = 32;
    public static final int NATIVE_CONFIG_LAYOUTDIR = 16384;
    public static final int NATIVE_CONFIG_LOCALE = 4;
    public static final int NATIVE_CONFIG_MCC = 1;
    public static final int NATIVE_CONFIG_MNC = 2;
    public static final int NATIVE_CONFIG_NAVIGATION = 64;
    public static final int NATIVE_CONFIG_ORIENTATION = 128;
    public static final int NATIVE_CONFIG_SCREEN_LAYOUT = 2048;
    public static final int NATIVE_CONFIG_SCREEN_SIZE = 512;
    public static final int NATIVE_CONFIG_SMALLEST_SCREEN_SIZE = 8192;
    public static final int NATIVE_CONFIG_TOUCHSCREEN = 8;
    public static final int NATIVE_CONFIG_UI_MODE = 4096;
    public static final int NATIVE_CONFIG_VERSION = 1024;
    public static final int NAVIGATIONHIDDEN_NO = 1;
    public static final int NAVIGATIONHIDDEN_UNDEFINED = 0;
    public static final int NAVIGATIONHIDDEN_YES = 2;
    public static final int NAVIGATION_DPAD = 2;
    public static final int NAVIGATION_NONAV = 1;
    public static final int NAVIGATION_TRACKBALL = 3;
    public static final int NAVIGATION_UNDEFINED = 0;
    public static final int NAVIGATION_WHEEL = 4;
    public static final int ORIENTATION_LANDSCAPE = 2;
    public static final int ORIENTATION_PORTRAIT = 1;
    @Deprecated
    public static final int ORIENTATION_SQUARE = 3;
    public static final int ORIENTATION_UNDEFINED = 0;
    public static final int SCREENLAYOUT_COMPAT_NEEDED = 268435456;
    public static final int SCREENLAYOUT_LAYOUTDIR_LTR = 64;
    public static final int SCREENLAYOUT_LAYOUTDIR_MASK = 192;
    public static final int SCREENLAYOUT_LAYOUTDIR_RTL = 128;
    public static final int SCREENLAYOUT_LAYOUTDIR_SHIFT = 6;
    public static final int SCREENLAYOUT_LAYOUTDIR_UNDEFINED = 0;
    public static final int SCREENLAYOUT_LONG_MASK = 48;
    public static final int SCREENLAYOUT_LONG_NO = 16;
    public static final int SCREENLAYOUT_LONG_UNDEFINED = 0;
    public static final int SCREENLAYOUT_LONG_YES = 32;
    public static final int SCREENLAYOUT_ROUND_MASK = 768;
    public static final int SCREENLAYOUT_ROUND_NO = 256;
    public static final int SCREENLAYOUT_ROUND_SHIFT = 8;
    public static final int SCREENLAYOUT_ROUND_UNDEFINED = 0;
    public static final int SCREENLAYOUT_ROUND_YES = 512;
    public static final int SCREENLAYOUT_SIZE_LARGE = 3;
    public static final int SCREENLAYOUT_SIZE_MASK = 15;
    public static final int SCREENLAYOUT_SIZE_NORMAL = 2;
    public static final int SCREENLAYOUT_SIZE_SMALL = 1;
    public static final int SCREENLAYOUT_SIZE_UNDEFINED = 0;
    public static final int SCREENLAYOUT_SIZE_XLARGE = 4;
    public static final int SCREENLAYOUT_UNDEFINED = 0;
    public static final int SCREEN_HEIGHT_DP_UNDEFINED = 0;
    public static final int SCREEN_WIDTH_DP_UNDEFINED = 0;
    public static final int SMALLEST_SCREEN_WIDTH_DP_UNDEFINED = 0;
    private static final String TAG = "Configuration";
    public static final int TOUCHSCREEN_FINGER = 3;
    public static final int TOUCHSCREEN_NOTOUCH = 1;
    @Deprecated
    public static final int TOUCHSCREEN_STYLUS = 2;
    public static final int TOUCHSCREEN_UNDEFINED = 0;
    public static final int UI_MODE_NIGHT_MASK = 48;
    public static final int UI_MODE_NIGHT_NO = 16;
    public static final int UI_MODE_NIGHT_UNDEFINED = 0;
    public static final int UI_MODE_NIGHT_YES = 32;
    public static final int UI_MODE_TYPE_APPLIANCE = 5;
    public static final int UI_MODE_TYPE_CAR = 3;
    public static final int UI_MODE_TYPE_DESK = 2;
    public static final int UI_MODE_TYPE_MASK = 15;
    public static final int UI_MODE_TYPE_NORMAL = 1;
    public static final int UI_MODE_TYPE_TELEVISION = 4;
    public static final int UI_MODE_TYPE_UNDEFINED = 0;
    public static final int UI_MODE_TYPE_VR_HEADSET = 7;
    public static final int UI_MODE_TYPE_WATCH = 6;
    private static final String XML_ATTR_APP_BOUNDS = "app_bounds";
    private static final String XML_ATTR_COLOR_MODE = "clrMod";
    private static final String XML_ATTR_DENSITY = "density";
    private static final String XML_ATTR_FONT_SCALE = "fs";
    private static final String XML_ATTR_HARD_KEYBOARD_HIDDEN = "hardKeyHid";
    private static final String XML_ATTR_KEYBOARD = "key";
    private static final String XML_ATTR_KEYBOARD_HIDDEN = "keyHid";
    private static final String XML_ATTR_LOCALES = "locales";
    private static final String XML_ATTR_MCC = "mcc";
    private static final String XML_ATTR_MNC = "mnc";
    private static final String XML_ATTR_NAVIGATION = "nav";
    private static final String XML_ATTR_NAVIGATION_HIDDEN = "navHid";
    private static final String XML_ATTR_ORIENTATION = "ori";
    private static final String XML_ATTR_ROTATION = "rot";
    private static final String XML_ATTR_SCREEN_HEIGHT = "height";
    private static final String XML_ATTR_SCREEN_LAYOUT = "scrLay";
    private static final String XML_ATTR_SCREEN_WIDTH = "width";
    private static final String XML_ATTR_SMALLEST_WIDTH = "sw";
    private static final String XML_ATTR_TOUCHSCREEN = "touch";
    private static final String XML_ATTR_UI_MODE = "ui";
    public int assetsSeq;
    public int colorMode;
    public int compatScreenHeightDp;
    public int compatScreenWidthDp;
    public int compatSmallestScreenWidthDp;
    public int densityDpi;
    public MiuiConfiguration extraConfig;
    public float fontScale;
    public int hardKeyboardHidden;
    public int keyboard;
    public int keyboardHidden;
    @Deprecated
    public Locale locale;
    private LocaleList mLocaleList;
    public int mcc;
    public int mnc;
    public int navigation;
    public int navigationHidden;
    public int orientation;
    public int screenHeightDp;
    public int screenLayout;
    public int screenWidthDp;
    @UnsupportedAppUsage
    public int seq;
    public int smallestScreenWidthDp;
    public int touchscreen;
    public int uiMode;
    @UnsupportedAppUsage
    public boolean userSetLocale;
    public final WindowConfiguration windowConfiguration;

    @Retention(RetentionPolicy.SOURCE)
    public @interface NativeConfig {
    }

    /* synthetic */ Configuration(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public static int resetScreenLayout(int curLayout) {
        return (-268435520 & curLayout) | 36;
    }

    public static int reduceScreenLayout(int curLayout, int longSizeDp, int shortSizeDp) {
        int screenLayoutSize;
        boolean screenLayoutLong;
        boolean screenLayoutCompatNeeded;
        if (longSizeDp < MetricsEvent.ACTION_DELETION_HELPER_REMOVE_CANCEL) {
            screenLayoutSize = 1;
            screenLayoutLong = false;
            screenLayoutCompatNeeded = false;
        } else {
            if (longSizeDp >= 960 && shortSizeDp >= MetricsEvent.ACTION_PERMISSION_DENIED_RECEIVE_WAP_PUSH) {
                screenLayoutSize = 4;
            } else if (longSizeDp < 640 || shortSizeDp < 480) {
                screenLayoutSize = 2;
            } else {
                screenLayoutSize = 3;
            }
            if (shortSizeDp > 321 || longSizeDp > 570) {
                screenLayoutCompatNeeded = true;
            } else {
                screenLayoutCompatNeeded = false;
            }
            if ((longSizeDp * 3) / 5 >= shortSizeDp - 1) {
                screenLayoutLong = true;
            } else {
                screenLayoutLong = false;
            }
        }
        if (!screenLayoutLong) {
            curLayout = (curLayout & -49) | 16;
        }
        if (screenLayoutCompatNeeded) {
            curLayout |= 268435456;
        }
        if (screenLayoutSize < (curLayout & 15)) {
            return (curLayout & -16) | screenLayoutSize;
        }
        return curLayout;
    }

    public static String configurationDiffToString(int diff) {
        ArrayList<String> list = new ArrayList();
        if ((diff & 1) != 0) {
            list.add("CONFIG_MCC");
        }
        if ((diff & 2) != 0) {
            list.add("CONFIG_MNC");
        }
        if ((diff & 4) != 0) {
            list.add("CONFIG_LOCALE");
        }
        if ((diff & 8) != 0) {
            list.add("CONFIG_TOUCHSCREEN");
        }
        if ((diff & 16) != 0) {
            list.add("CONFIG_KEYBOARD");
        }
        if ((diff & 32) != 0) {
            list.add("CONFIG_KEYBOARD_HIDDEN");
        }
        if ((diff & 64) != 0) {
            list.add("CONFIG_NAVIGATION");
        }
        if ((diff & 128) != 0) {
            list.add("CONFIG_ORIENTATION");
        }
        if ((diff & 256) != 0) {
            list.add("CONFIG_SCREEN_LAYOUT");
        }
        if ((diff & 16384) != 0) {
            list.add("CONFIG_COLOR_MODE");
        }
        if ((diff & 512) != 0) {
            list.add("CONFIG_UI_MODE");
        }
        if ((diff & 1024) != 0) {
            list.add("CONFIG_SCREEN_SIZE");
        }
        if ((diff & 2048) != 0) {
            list.add("CONFIG_SMALLEST_SCREEN_SIZE");
        }
        if ((diff & 8192) != 0) {
            list.add("CONFIG_LAYOUT_DIRECTION");
        }
        if ((1073741824 & diff) != 0) {
            list.add("CONFIG_FONT_SCALE");
        }
        if ((Integer.MIN_VALUE & diff) != 0) {
            list.add("CONFIG_ASSETS_PATHS");
        }
        StringBuilder builder = new StringBuilder("{");
        int n = list.size();
        for (int i = 0; i < n; i++) {
            builder.append((String) list.get(i));
            if (i != n - 1) {
                builder.append(", ");
            }
        }
        builder.append("}");
        return builder.toString();
    }

    public boolean isLayoutSizeAtLeast(int size) {
        int cur = this.screenLayout & 15;
        boolean z = false;
        if (cur == 0) {
            return false;
        }
        if (cur >= size) {
            z = true;
        }
        return z;
    }

    public Configuration() {
        this.windowConfiguration = new WindowConfiguration();
        this.extraConfig = new MiuiConfiguration();
        unset();
    }

    public Configuration(Configuration o) {
        this.windowConfiguration = new WindowConfiguration();
        this.extraConfig = new MiuiConfiguration();
        setTo(o);
    }

    private void fixUpLocaleList() {
        LocaleList emptyLocaleList;
        if (this.locale != null || this.mLocaleList.isEmpty()) {
            Locale locale = this.locale;
            if (locale == null || locale.equals(this.mLocaleList.get(0))) {
                return;
            }
        }
        if (this.locale == null) {
            emptyLocaleList = LocaleList.getEmptyLocaleList();
        } else {
            emptyLocaleList = new LocaleList(this.locale);
        }
        this.mLocaleList = emptyLocaleList;
    }

    public void setTo(Configuration o) {
        this.fontScale = o.fontScale;
        this.mcc = o.mcc;
        this.mnc = o.mnc;
        Locale locale = o.locale;
        this.locale = locale == null ? null : (Locale) locale.clone();
        o.fixUpLocaleList();
        this.mLocaleList = o.mLocaleList;
        this.userSetLocale = o.userSetLocale;
        this.touchscreen = o.touchscreen;
        this.keyboard = o.keyboard;
        this.keyboardHidden = o.keyboardHidden;
        this.hardKeyboardHidden = o.hardKeyboardHidden;
        this.navigation = o.navigation;
        this.navigationHidden = o.navigationHidden;
        this.orientation = o.orientation;
        this.screenLayout = o.screenLayout;
        this.colorMode = o.colorMode;
        this.uiMode = o.uiMode;
        this.screenWidthDp = o.screenWidthDp;
        this.screenHeightDp = o.screenHeightDp;
        this.smallestScreenWidthDp = o.smallestScreenWidthDp;
        this.densityDpi = o.densityDpi;
        this.compatScreenWidthDp = o.compatScreenWidthDp;
        this.compatScreenHeightDp = o.compatScreenHeightDp;
        this.compatSmallestScreenWidthDp = o.compatSmallestScreenWidthDp;
        this.assetsSeq = o.assetsSeq;
        this.seq = o.seq;
        this.windowConfiguration.setTo(o.windowConfiguration);
        this.extraConfig.setTo(o.extraConfig);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("{");
        sb.append(this.fontScale);
        String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        sb.append(str);
        int i = this.mcc;
        if (i != 0) {
            sb.append(i);
            sb.append("mcc");
        } else {
            sb.append("?mcc");
        }
        i = this.mnc;
        if (i != 0) {
            sb.append(i);
            sb.append("mnc");
        } else {
            sb.append("?mnc");
        }
        fixUpLocaleList();
        if (this.mLocaleList.isEmpty()) {
            sb.append(" ?localeList");
        } else {
            sb.append(str);
            sb.append(this.mLocaleList);
        }
        i = this.screenLayout & 192;
        if (i == 0) {
            sb.append(" ?layoutDir");
        } else if (i == 64) {
            sb.append(" ldltr");
        } else if (i != 128) {
            sb.append(" layoutDir=");
            sb.append(i >> 6);
        } else {
            sb.append(" ldrtl");
        }
        String str2 = "dp";
        if (this.smallestScreenWidthDp != 0) {
            sb.append(" sw");
            sb.append(this.smallestScreenWidthDp);
            sb.append(str2);
        } else {
            sb.append(" ?swdp");
        }
        if (this.screenWidthDp != 0) {
            sb.append(" w");
            sb.append(this.screenWidthDp);
            sb.append(str2);
        } else {
            sb.append(" ?wdp");
        }
        if (this.screenHeightDp != 0) {
            sb.append(" h");
            sb.append(this.screenHeightDp);
            sb.append(str2);
        } else {
            sb.append(" ?hdp");
        }
        if (this.densityDpi != 0) {
            sb.append(str);
            sb.append(this.densityDpi);
            sb.append("dpi");
        } else {
            sb.append(" ?density");
        }
        int i2 = this.screenLayout & 15;
        if (i2 == 0) {
            sb.append(" ?lsize");
        } else if (i2 == 1) {
            sb.append(" smll");
        } else if (i2 == 2) {
            sb.append(" nrml");
        } else if (i2 == 3) {
            sb.append(" lrg");
        } else if (i2 != 4) {
            sb.append(" layoutSize=");
            sb.append(this.screenLayout & 15);
        } else {
            sb.append(" xlrg");
        }
        i2 = this.screenLayout & 48;
        if (i2 == 0) {
            sb.append(" ?long");
        } else if (i2 != 16) {
            if (i2 != 32) {
                sb.append(" layoutLong=");
                sb.append(this.screenLayout & 48);
            } else {
                sb.append(" long");
            }
        }
        i2 = this.colorMode & 12;
        if (i2 == 0) {
            sb.append(" ?ldr");
        } else if (i2 != 4) {
            if (i2 != 8) {
                sb.append(" dynamicRange=");
                sb.append(this.colorMode & 12);
            } else {
                sb.append(" hdr");
            }
        }
        i2 = this.colorMode & 3;
        if (i2 == 0) {
            sb.append(" ?wideColorGamut");
        } else if (i2 != 1) {
            if (i2 != 2) {
                sb.append(" wideColorGamut=");
                sb.append(this.colorMode & 3);
            } else {
                sb.append(" widecg");
            }
        }
        i2 = this.orientation;
        if (i2 == 0) {
            sb.append(" ?orien");
        } else if (i2 == 1) {
            sb.append(" port");
        } else if (i2 != 2) {
            sb.append(" orien=");
            sb.append(this.orientation);
        } else {
            sb.append(" land");
        }
        switch (this.uiMode & 15) {
            case 0:
                sb.append(" ?uimode");
                break;
            case 1:
                break;
            case 2:
                sb.append(" desk");
                break;
            case 3:
                sb.append(" car");
                break;
            case 4:
                sb.append(" television");
                break;
            case 5:
                sb.append(" appliance");
                break;
            case 6:
                sb.append(" watch");
                break;
            case 7:
                sb.append(" vrheadset");
                break;
            default:
                sb.append(" uimode=");
                sb.append(this.uiMode & 15);
                break;
        }
        i2 = this.uiMode & 48;
        if (i2 == 0) {
            sb.append(" ?night");
        } else if (i2 != 16) {
            if (i2 != 32) {
                sb.append(" night=");
                sb.append(this.uiMode & 48);
            } else {
                sb.append(" night");
            }
        }
        i2 = this.touchscreen;
        if (i2 == 0) {
            sb.append(" ?touch");
        } else if (i2 == 1) {
            sb.append(" -touch");
        } else if (i2 == 2) {
            sb.append(" stylus");
        } else if (i2 != 3) {
            sb.append(" touch=");
            sb.append(this.touchscreen);
        } else {
            sb.append(" finger");
        }
        i2 = this.keyboard;
        if (i2 == 0) {
            sb.append(" ?keyb");
        } else if (i2 == 1) {
            sb.append(" -keyb");
        } else if (i2 == 2) {
            sb.append(" qwerty");
        } else if (i2 != 3) {
            sb.append(" keys=");
            sb.append(this.keyboard);
        } else {
            sb.append(" 12key");
        }
        i2 = this.keyboardHidden;
        String str3 = "/h";
        String str4 = "/v";
        String str5 = "/?";
        String str6 = "/";
        if (i2 == 0) {
            sb.append(str5);
        } else if (i2 == 1) {
            sb.append(str4);
        } else if (i2 == 2) {
            sb.append(str3);
        } else if (i2 != 3) {
            sb.append(str6);
            sb.append(this.keyboardHidden);
        } else {
            sb.append("/s");
        }
        i2 = this.hardKeyboardHidden;
        if (i2 == 0) {
            sb.append(str5);
        } else if (i2 == 1) {
            sb.append(str4);
        } else if (i2 != 2) {
            sb.append(str6);
            sb.append(this.hardKeyboardHidden);
        } else {
            sb.append(str3);
        }
        i2 = this.navigation;
        if (i2 == 0) {
            sb.append(" ?nav");
        } else if (i2 == 1) {
            sb.append(" -nav");
        } else if (i2 == 2) {
            sb.append(" dpad");
        } else if (i2 == 3) {
            sb.append(" tball");
        } else if (i2 != 4) {
            sb.append(" nav=");
            sb.append(this.navigation);
        } else {
            sb.append(" wheel");
        }
        i2 = this.navigationHidden;
        if (i2 == 0) {
            sb.append(str5);
        } else if (i2 == 1) {
            sb.append(str4);
        } else if (i2 != 2) {
            sb.append(str6);
            sb.append(this.navigationHidden);
        } else {
            sb.append(str3);
        }
        sb.append(" winConfig=");
        sb.append(this.windowConfiguration);
        if (this.assetsSeq != 0) {
            sb.append(" as.");
            sb.append(this.assetsSeq);
        }
        if (this.seq != 0) {
            sb.append(" s.");
            sb.append(this.seq);
        }
        sb.append(this.extraConfig.toString());
        sb.append('}');
        return sb.toString();
    }

    public void writeToProto(ProtoOutputStream protoOutputStream, long fieldId, boolean persisted, boolean critical) {
        long token = protoOutputStream.start(fieldId);
        if (!critical) {
            protoOutputStream.write(1108101562369L, this.fontScale);
            protoOutputStream.write(1155346202626L, this.mcc);
            protoOutputStream.write(1155346202627L, this.mnc);
            LocaleList localeList = this.mLocaleList;
            if (localeList != null) {
                localeList.writeToProto(protoOutputStream, 2246267895812L);
            }
            protoOutputStream.write(1155346202629L, this.screenLayout);
            protoOutputStream.write(1155346202630L, this.colorMode);
            protoOutputStream.write((long) ConfigurationProto.TOUCHSCREEN, this.touchscreen);
            protoOutputStream.write(1155346202632L, this.keyboard);
            protoOutputStream.write((long) ConfigurationProto.KEYBOARD_HIDDEN, this.keyboardHidden);
            protoOutputStream.write((long) ConfigurationProto.HARD_KEYBOARD_HIDDEN, this.hardKeyboardHidden);
            protoOutputStream.write((long) ConfigurationProto.NAVIGATION, this.navigation);
            protoOutputStream.write((long) ConfigurationProto.NAVIGATION_HIDDEN, this.navigationHidden);
            protoOutputStream.write((long) ConfigurationProto.UI_MODE, this.uiMode);
            protoOutputStream.write((long) ConfigurationProto.SMALLEST_SCREEN_WIDTH_DP, this.smallestScreenWidthDp);
            protoOutputStream.write((long) ConfigurationProto.DENSITY_DPI, this.densityDpi);
            if (!persisted) {
                WindowConfiguration windowConfiguration = this.windowConfiguration;
                if (windowConfiguration != null) {
                    windowConfiguration.writeToProto(protoOutputStream, 1146756268051L);
                }
            }
        }
        protoOutputStream.write((long) ConfigurationProto.ORIENTATION, this.orientation);
        protoOutputStream.write((long) ConfigurationProto.SCREEN_WIDTH_DP, this.screenWidthDp);
        protoOutputStream.write((long) ConfigurationProto.SCREEN_HEIGHT_DP, this.screenHeightDp);
        protoOutputStream.end(token);
    }

    public void writeToProto(ProtoOutputStream protoOutputStream, long fieldId) {
        writeToProto(protoOutputStream, fieldId, false, false);
    }

    public void writeToProto(ProtoOutputStream protoOutputStream, long fieldId, boolean critical) {
        writeToProto(protoOutputStream, fieldId, false, critical);
    }

    public void readFromProto(ProtoInputStream protoInputStream, long fieldId) throws IOException {
        WireTypeMismatchException wtme;
        String script;
        Throwable th;
        Throwable th2;
        IllformedLocaleException e;
        StringBuilder stringBuilder;
        ProtoInputStream protoInputStream2 = protoInputStream;
        String str = ";script-";
        String str2 = ";variant-";
        String str3 = ";country-";
        String str4 = "readFromProto error building locale with: language-";
        String str5 = TAG;
        String str6 = "";
        long token = protoInputStream.start(fieldId);
        ArrayList list = new ArrayList();
        while (true) {
            long token2;
            try {
                String str7;
                long token3;
                int i = -1;
                if (protoInputStream.nextField() != -1) {
                    try {
                        switch (protoInputStream.getFieldNumber()) {
                            case 1:
                                str7 = str6;
                                token3 = token;
                                this.fontScale = protoInputStream2.readFloat(1108101562369L);
                                continue;
                            case 2:
                                str7 = str6;
                                token3 = token;
                                this.mcc = protoInputStream2.readInt(1155346202626L);
                                continue;
                            case 3:
                                str7 = str6;
                                token3 = token;
                                this.mnc = protoInputStream2.readInt(1155346202627L);
                                continue;
                            case 4:
                                long localeToken = protoInputStream2.start(2246267895812L);
                                String country = str6;
                                String variant = str6;
                                String script2 = str6;
                                String language = str6;
                                while (protoInputStream.nextField() != i) {
                                    try {
                                        try {
                                            int fieldNumber = protoInputStream.getFieldNumber();
                                            if (fieldNumber == 1) {
                                                str7 = str6;
                                                token3 = token;
                                                language = protoInputStream2.readString(1138166333441L);
                                            } else if (fieldNumber == 2) {
                                                str7 = str6;
                                                token3 = token;
                                                country = protoInputStream2.readString(1138166333442L);
                                            } else if (fieldNumber == 3) {
                                                str7 = str6;
                                                token3 = token;
                                                variant = protoInputStream2.readString(1138166333443L);
                                            } else if (fieldNumber != 4) {
                                                str7 = str6;
                                                token3 = token;
                                            } else {
                                                str7 = str6;
                                                token3 = token;
                                                try {
                                                    script2 = protoInputStream2.readString(1138166333444L);
                                                } catch (WireTypeMismatchException e2) {
                                                    wtme = e2;
                                                    str6 = country;
                                                    token = variant;
                                                    script = script2;
                                                    try {
                                                        throw wtme;
                                                    } catch (Throwable th22) {
                                                        th = th22;
                                                    }
                                                } catch (Throwable th222) {
                                                    th = th222;
                                                    str6 = country;
                                                    token = variant;
                                                    script = script2;
                                                    protoInputStream2.end(localeToken);
                                                    list.add(new Builder().setLanguage(language).setRegion(str6).setVariant(token).setScript(script).build());
                                                    throw th;
                                                }
                                            }
                                            str6 = str7;
                                            token = token3;
                                            i = -1;
                                        } catch (WireTypeMismatchException e3) {
                                            wtme = e3;
                                            token3 = token;
                                            str6 = country;
                                            token = variant;
                                            script = script2;
                                            throw wtme;
                                        } catch (Throwable th2222) {
                                            token3 = token;
                                            th = th2222;
                                            str6 = country;
                                            token = variant;
                                            script = script2;
                                            protoInputStream2.end(localeToken);
                                            list.add(new Builder().setLanguage(language).setRegion(str6).setVariant(token).setScript(script).build());
                                            throw th;
                                        }
                                    } catch (WireTypeMismatchException e4) {
                                        wtme = e4;
                                        token3 = token;
                                        str6 = country;
                                        token = variant;
                                        script = script2;
                                        throw wtme;
                                    } catch (Throwable th22222) {
                                        token3 = token;
                                        str6 = country;
                                        token = variant;
                                        script = script2;
                                        th = th22222;
                                        protoInputStream2.end(localeToken);
                                        list.add(new Builder().setLanguage(language).setRegion(str6).setVariant(token).setScript(script).build());
                                        throw th;
                                    }
                                }
                                str7 = str6;
                                token3 = token;
                                try {
                                    protoInputStream2.end(localeToken);
                                    try {
                                        str6 = country;
                                        try {
                                            token = variant;
                                        } catch (IllformedLocaleException e5) {
                                            e = e5;
                                            token = variant;
                                            script = script2;
                                            stringBuilder = new StringBuilder();
                                            stringBuilder.append(str4);
                                            stringBuilder.append(language);
                                            stringBuilder.append(str3);
                                            stringBuilder.append(str6);
                                            stringBuilder.append(str2);
                                            stringBuilder.append(token);
                                            stringBuilder.append(str);
                                            stringBuilder.append(script);
                                            Slog.e(str5, stringBuilder.toString());
                                            str6 = str7;
                                            token = token3;
                                        }
                                        try {
                                            script = script2;
                                            try {
                                                list.add(new Builder().setLanguage(language).setRegion(str6).setVariant(token).setScript(script).build());
                                                continue;
                                            } catch (IllformedLocaleException e6) {
                                                e = e6;
                                            }
                                        } catch (IllformedLocaleException e7) {
                                            e = e7;
                                            script = script2;
                                            stringBuilder = new StringBuilder();
                                            stringBuilder.append(str4);
                                            stringBuilder.append(language);
                                            stringBuilder.append(str3);
                                            stringBuilder.append(str6);
                                            stringBuilder.append(str2);
                                            stringBuilder.append(token);
                                            stringBuilder.append(str);
                                            stringBuilder.append(script);
                                            Slog.e(str5, stringBuilder.toString());
                                            str6 = str7;
                                            token = token3;
                                        }
                                    } catch (IllformedLocaleException e8) {
                                        e = e8;
                                        str6 = country;
                                        token = variant;
                                        script = script2;
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append(str4);
                                        stringBuilder.append(language);
                                        stringBuilder.append(str3);
                                        stringBuilder.append(str6);
                                        stringBuilder.append(str2);
                                        stringBuilder.append(token);
                                        stringBuilder.append(str);
                                        stringBuilder.append(script);
                                        Slog.e(str5, stringBuilder.toString());
                                        str6 = str7;
                                        token = token3;
                                    }
                                } catch (IllformedLocaleException e9) {
                                    IllformedLocaleException illformedLocaleException = e9;
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(str4);
                                    stringBuilder.append(language);
                                    stringBuilder.append(str3);
                                    stringBuilder.append(str6);
                                    stringBuilder.append(str2);
                                    stringBuilder.append(token);
                                    stringBuilder.append(str);
                                    stringBuilder.append(script);
                                    Slog.e(str5, stringBuilder.toString());
                                } catch (Throwable th3) {
                                    th22222 = th3;
                                    token2 = token3;
                                    break;
                                }
                            case 5:
                                this.screenLayout = protoInputStream2.readInt(1155346202629L);
                                str7 = str6;
                                token3 = token;
                                continue;
                            case 6:
                                this.colorMode = protoInputStream2.readInt(1155346202630L);
                                str7 = str6;
                                token3 = token;
                                continue;
                            case 7:
                                this.touchscreen = protoInputStream2.readInt(ConfigurationProto.TOUCHSCREEN);
                                str7 = str6;
                                token3 = token;
                                continue;
                            case 8:
                                this.keyboard = protoInputStream2.readInt(1155346202632L);
                                str7 = str6;
                                token3 = token;
                                continue;
                            case 9:
                                this.keyboardHidden = protoInputStream2.readInt(ConfigurationProto.KEYBOARD_HIDDEN);
                                str7 = str6;
                                token3 = token;
                                continue;
                            case 10:
                                this.hardKeyboardHidden = protoInputStream2.readInt(ConfigurationProto.HARD_KEYBOARD_HIDDEN);
                                str7 = str6;
                                token3 = token;
                                continue;
                            case 11:
                                this.navigation = protoInputStream2.readInt(ConfigurationProto.NAVIGATION);
                                str7 = str6;
                                token3 = token;
                                continue;
                            case 12:
                                this.navigationHidden = protoInputStream2.readInt(ConfigurationProto.NAVIGATION_HIDDEN);
                                str7 = str6;
                                token3 = token;
                                continue;
                            case 13:
                                this.orientation = protoInputStream2.readInt(ConfigurationProto.ORIENTATION);
                                str7 = str6;
                                token3 = token;
                                continue;
                            case 14:
                                this.uiMode = protoInputStream2.readInt(ConfigurationProto.UI_MODE);
                                str7 = str6;
                                token3 = token;
                                continue;
                            case 15:
                                this.screenWidthDp = protoInputStream2.readInt(ConfigurationProto.SCREEN_WIDTH_DP);
                                str7 = str6;
                                token3 = token;
                                continue;
                            case 16:
                                this.screenHeightDp = protoInputStream2.readInt(ConfigurationProto.SCREEN_HEIGHT_DP);
                                str7 = str6;
                                token3 = token;
                                continue;
                            case 17:
                                this.smallestScreenWidthDp = protoInputStream2.readInt(ConfigurationProto.SMALLEST_SCREEN_WIDTH_DP);
                                str7 = str6;
                                token3 = token;
                                continue;
                            case 18:
                                this.densityDpi = protoInputStream2.readInt(ConfigurationProto.DENSITY_DPI);
                                str7 = str6;
                                token3 = token;
                                continue;
                            case 19:
                                try {
                                    this.windowConfiguration.readFromProto(protoInputStream2, 1146756268051L);
                                    str7 = str6;
                                    token3 = token;
                                    continue;
                                } catch (Throwable th4) {
                                    th22222 = th4;
                                    token2 = token;
                                    break;
                                }
                            default:
                                str7 = str6;
                                token3 = token;
                                continue;
                        }
                    } catch (Throwable th5) {
                        th22222 = th5;
                        token2 = token;
                    }
                    if (list.size() > 0) {
                        setLocales(new LocaleList((Locale[]) list.toArray(new Locale[list.size()])));
                    }
                    protoInputStream2.end(token2);
                    throw th22222;
                }
                token3 = token;
                if (list.size() > 0) {
                    setLocales(new LocaleList((Locale[]) list.toArray(new Locale[list.size()])));
                }
                protoInputStream2.end(token3);
                return;
                str6 = str7;
                token = token3;
            } catch (Throwable th6) {
                th22222 = th6;
                token2 = token;
            }
        }
    }

    public void writeResConfigToProto(ProtoOutputStream protoOutputStream, long fieldId, DisplayMetrics metrics) {
        int width;
        int height;
        if (metrics.widthPixels >= metrics.heightPixels) {
            width = metrics.widthPixels;
            height = metrics.heightPixels;
        } else {
            width = metrics.heightPixels;
            height = metrics.widthPixels;
        }
        long token = protoOutputStream.start(fieldId);
        writeToProto(protoOutputStream, 1146756268033L);
        protoOutputStream.write(1155346202626L, VERSION.RESOURCES_SDK_INT);
        protoOutputStream.write(1155346202627L, width);
        protoOutputStream.write((long) ResourcesConfigurationProto.SCREEN_HEIGHT_PX, height);
        protoOutputStream.end(token);
    }

    public static String uiModeToString(int uiMode) {
        switch (uiMode) {
            case 0:
                return "UI_MODE_TYPE_UNDEFINED";
            case 1:
                return "UI_MODE_TYPE_NORMAL";
            case 2:
                return "UI_MODE_TYPE_DESK";
            case 3:
                return "UI_MODE_TYPE_CAR";
            case 4:
                return "UI_MODE_TYPE_TELEVISION";
            case 5:
                return "UI_MODE_TYPE_APPLIANCE";
            case 6:
                return "UI_MODE_TYPE_WATCH";
            case 7:
                return "UI_MODE_TYPE_VR_HEADSET";
            default:
                return Integer.toString(uiMode);
        }
    }

    public void setToDefaults() {
        this.fontScale = 1.0f;
        this.mnc = 0;
        this.mcc = 0;
        this.mLocaleList = LocaleList.getEmptyLocaleList();
        this.locale = null;
        this.userSetLocale = false;
        this.touchscreen = 0;
        this.keyboard = 0;
        this.keyboardHidden = 0;
        this.hardKeyboardHidden = 0;
        this.navigation = 0;
        this.navigationHidden = 0;
        this.orientation = 0;
        this.screenLayout = 0;
        this.colorMode = 0;
        this.uiMode = 0;
        this.compatScreenWidthDp = 0;
        this.screenWidthDp = 0;
        this.compatScreenHeightDp = 0;
        this.screenHeightDp = 0;
        this.compatSmallestScreenWidthDp = 0;
        this.smallestScreenWidthDp = 0;
        this.densityDpi = 0;
        this.assetsSeq = 0;
        this.seq = 0;
        this.windowConfiguration.setToDefaults();
        this.extraConfig.setToDefaults();
    }

    public void unset() {
        setToDefaults();
        this.fontScale = 0.0f;
    }

    @Deprecated
    @UnsupportedAppUsage
    public void makeDefault() {
        setToDefaults();
    }

    public int updateFrom(Configuration delta) {
        int i;
        int i2;
        int i3;
        int changed = 0;
        float f = delta.fontScale;
        if (f > 0.0f && this.fontScale != f) {
            changed = 0 | 1073741824;
            this.fontScale = f;
        }
        int i4 = delta.mcc;
        if (!(i4 == 0 || this.mcc == i4)) {
            changed |= 1;
            this.mcc = i4;
        }
        i4 = delta.mnc;
        if (!(i4 == 0 || this.mnc == i4)) {
            changed |= 2;
            this.mnc = i4;
        }
        fixUpLocaleList();
        delta.fixUpLocaleList();
        if (!(delta.mLocaleList.isEmpty() || this.mLocaleList.equals(delta.mLocaleList))) {
            changed |= 4;
            this.mLocaleList = delta.mLocaleList;
            if (!delta.locale.equals(this.locale)) {
                this.locale = (Locale) delta.locale.clone();
                changed |= 8192;
                setLayoutDirection(this.locale);
            }
        }
        i4 = delta.screenLayout & 192;
        if (i4 != 0) {
            i = this.screenLayout;
            if (i4 != (i & 192)) {
                this.screenLayout = (i & -193) | i4;
                changed |= 8192;
            }
        }
        if (delta.userSetLocale && !(this.userSetLocale && (changed & 4) == 0)) {
            changed |= 4;
            this.userSetLocale = true;
        }
        i = delta.touchscreen;
        if (!(i == 0 || this.touchscreen == i)) {
            changed |= 8;
            this.touchscreen = i;
        }
        i = delta.keyboard;
        if (!(i == 0 || this.keyboard == i)) {
            changed |= 16;
            this.keyboard = i;
        }
        i = delta.keyboardHidden;
        if (!(i == 0 || this.keyboardHidden == i)) {
            changed |= 32;
            this.keyboardHidden = i;
        }
        i = delta.hardKeyboardHidden;
        if (!(i == 0 || this.hardKeyboardHidden == i)) {
            changed |= 32;
            this.hardKeyboardHidden = i;
        }
        i = delta.navigation;
        if (!(i == 0 || this.navigation == i)) {
            changed |= 64;
            this.navigation = i;
        }
        i = delta.navigationHidden;
        if (!(i == 0 || this.navigationHidden == i)) {
            changed |= 32;
            this.navigationHidden = i;
        }
        i = delta.orientation;
        if (!(i == 0 || this.orientation == i)) {
            changed |= 128;
            this.orientation = i;
        }
        i = delta.screenLayout;
        if ((i & 15) != 0) {
            i2 = i & 15;
            i3 = this.screenLayout;
            if (i2 != (i3 & 15)) {
                changed |= 256;
                this.screenLayout = (i & 15) | (i3 & -16);
            }
        }
        i = delta.screenLayout;
        if ((i & 48) != 0) {
            i2 = i & 48;
            i3 = this.screenLayout;
            if (i2 != (i3 & 48)) {
                changed |= 256;
                this.screenLayout = (i & 48) | (i3 & -49);
            }
        }
        i = delta.screenLayout;
        if ((i & 768) != 0) {
            i2 = i & 768;
            i3 = this.screenLayout;
            if (i2 != (i3 & 768)) {
                changed |= 256;
                this.screenLayout = (i & 768) | (i3 & -769);
            }
        }
        i = delta.screenLayout;
        i3 = i & 268435456;
        int i5 = this.screenLayout;
        if (!(i3 == (i5 & 268435456) || i == 0)) {
            changed |= 256;
            this.screenLayout = (i & 268435456) | (-268435457 & i5);
        }
        i = delta.colorMode;
        if ((i & 3) != 0) {
            i2 = i & 3;
            i3 = this.colorMode;
            if (i2 != (i3 & 3)) {
                changed |= 16384;
                this.colorMode = (i & 3) | (i3 & -4);
            }
        }
        i = delta.colorMode;
        if ((i & 12) != 0) {
            i2 = i & 12;
            i3 = this.colorMode;
            if (i2 != (i3 & 12)) {
                changed |= 16384;
                this.colorMode = (i & 12) | (i3 & -13);
            }
        }
        i = delta.uiMode;
        if (i != 0) {
            i2 = this.uiMode;
            if (i2 != i) {
                changed |= 512;
                if ((i & 15) != 0) {
                    this.uiMode = (i & 15) | (i2 & -16);
                }
                i = delta.uiMode;
                if ((i & 48) != 0) {
                    this.uiMode = (i & 48) | (this.uiMode & -49);
                }
            }
        }
        i = delta.screenWidthDp;
        if (!(i == 0 || this.screenWidthDp == i)) {
            changed |= 1024;
            this.screenWidthDp = i;
        }
        i = delta.screenHeightDp;
        if (!(i == 0 || this.screenHeightDp == i)) {
            changed |= 1024;
            this.screenHeightDp = i;
        }
        i = delta.smallestScreenWidthDp;
        if (!(i == 0 || this.smallestScreenWidthDp == i)) {
            changed |= 2048;
            this.smallestScreenWidthDp = i;
        }
        i = delta.densityDpi;
        if (!(i == 0 || this.densityDpi == i)) {
            changed |= 4096;
            this.densityDpi = i;
        }
        i = delta.compatScreenWidthDp;
        if (i != 0) {
            this.compatScreenWidthDp = i;
        }
        i = delta.compatScreenHeightDp;
        if (i != 0) {
            this.compatScreenHeightDp = i;
        }
        i = delta.compatSmallestScreenWidthDp;
        if (i != 0) {
            this.compatSmallestScreenWidthDp = i;
        }
        i = delta.assetsSeq;
        if (!(i == 0 || i == this.assetsSeq)) {
            changed |= Integer.MIN_VALUE;
            this.assetsSeq = i;
        }
        i = delta.seq;
        if (i != 0) {
            this.seq = i;
        }
        if (this.windowConfiguration.updateFrom(delta.windowConfiguration) != 0) {
            changed |= 536870912;
        }
        return changed | this.extraConfig.updateFrom(delta.extraConfig);
    }

    public int diff(Configuration delta) {
        return diff(delta, false, false);
    }

    public int diffPublicOnly(Configuration delta) {
        return diff(delta, false, true);
    }

    public int diff(Configuration delta, boolean compareUndefined, boolean publicOnly) {
        int changed = 0;
        if ((compareUndefined || delta.fontScale > 0.0f) && this.fontScale != delta.fontScale) {
            changed = 0 | 1073741824;
        }
        if ((compareUndefined || delta.mcc != 0) && this.mcc != delta.mcc) {
            changed |= 1;
        }
        if ((compareUndefined || delta.mnc != 0) && this.mnc != delta.mnc) {
            changed |= 2;
        }
        fixUpLocaleList();
        delta.fixUpLocaleList();
        if ((compareUndefined || !delta.mLocaleList.isEmpty()) && !this.mLocaleList.equals(delta.mLocaleList)) {
            changed = (changed | 4) | 8192;
        }
        int deltaScreenLayoutDir = delta.screenLayout & 192;
        if ((compareUndefined || deltaScreenLayoutDir != 0) && deltaScreenLayoutDir != (this.screenLayout & 192)) {
            changed |= 8192;
        }
        if ((compareUndefined || delta.touchscreen != 0) && this.touchscreen != delta.touchscreen) {
            changed |= 8;
        }
        if ((compareUndefined || delta.keyboard != 0) && this.keyboard != delta.keyboard) {
            changed |= 16;
        }
        if ((compareUndefined || delta.keyboardHidden != 0) && this.keyboardHidden != delta.keyboardHidden) {
            changed |= 32;
        }
        if ((compareUndefined || delta.hardKeyboardHidden != 0) && this.hardKeyboardHidden != delta.hardKeyboardHidden) {
            changed |= 32;
        }
        if ((compareUndefined || delta.navigation != 0) && this.navigation != delta.navigation) {
            changed |= 64;
        }
        if ((compareUndefined || delta.navigationHidden != 0) && this.navigationHidden != delta.navigationHidden) {
            changed |= 32;
        }
        if ((compareUndefined || delta.orientation != 0) && this.orientation != delta.orientation) {
            changed |= 128;
        }
        if ((compareUndefined || getScreenLayoutNoDirection(delta.screenLayout) != 0) && getScreenLayoutNoDirection(this.screenLayout) != getScreenLayoutNoDirection(delta.screenLayout)) {
            changed |= 256;
        }
        if ((compareUndefined || (delta.colorMode & 12) != 0) && (this.colorMode & 12) != (delta.colorMode & 12)) {
            changed |= 16384;
        }
        if ((compareUndefined || (delta.colorMode & 3) != 0) && (this.colorMode & 3) != (delta.colorMode & 3)) {
            changed |= 16384;
        }
        if ((compareUndefined || delta.uiMode != 0) && this.uiMode != delta.uiMode) {
            changed |= 512;
        }
        if ((compareUndefined || delta.screenWidthDp != 0) && this.screenWidthDp != delta.screenWidthDp) {
            changed |= 1024;
        }
        if ((compareUndefined || delta.screenHeightDp != 0) && this.screenHeightDp != delta.screenHeightDp) {
            changed |= 1024;
        }
        if ((compareUndefined || delta.smallestScreenWidthDp != 0) && this.smallestScreenWidthDp != delta.smallestScreenWidthDp) {
            changed |= 2048;
        }
        if ((compareUndefined || delta.densityDpi != 0) && this.densityDpi != delta.densityDpi) {
            changed |= 4096;
        }
        if ((compareUndefined || delta.assetsSeq != 0) && this.assetsSeq != delta.assetsSeq) {
            changed |= Integer.MIN_VALUE;
        }
        if (!(publicOnly || this.windowConfiguration.diff(delta.windowConfiguration, compareUndefined) == 0)) {
            changed |= 536870912;
        }
        return changed | this.extraConfig.diff(delta.extraConfig);
    }

    public static boolean needNewResources(int configChanges, int interestingChanges) {
        return (configChanges & ((Integer.MIN_VALUE | interestingChanges) | 1073741824)) != 0 || MiuiConfiguration.needNewResources(configChanges);
    }

    public boolean isOtherSeqNewer(Configuration other) {
        boolean z = false;
        if (other == null) {
            return false;
        }
        int diff = other.seq;
        if (diff == 0) {
            return true;
        }
        int i = this.seq;
        if (i == 0) {
            return true;
        }
        diff -= i;
        if (diff > 65536) {
            return false;
        }
        if (diff > 0) {
            z = true;
        }
        return z;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.fontScale);
        dest.writeInt(this.mcc);
        dest.writeInt(this.mnc);
        fixUpLocaleList();
        dest.writeParcelable(this.mLocaleList, flags);
        if (this.userSetLocale) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(this.touchscreen);
        dest.writeInt(this.keyboard);
        dest.writeInt(this.keyboardHidden);
        dest.writeInt(this.hardKeyboardHidden);
        dest.writeInt(this.navigation);
        dest.writeInt(this.navigationHidden);
        dest.writeInt(this.orientation);
        dest.writeInt(this.screenLayout);
        dest.writeInt(this.colorMode);
        dest.writeInt(this.uiMode);
        dest.writeInt(this.screenWidthDp);
        dest.writeInt(this.screenHeightDp);
        dest.writeInt(this.smallestScreenWidthDp);
        dest.writeInt(this.densityDpi);
        dest.writeInt(this.compatScreenWidthDp);
        dest.writeInt(this.compatScreenHeightDp);
        dest.writeInt(this.compatSmallestScreenWidthDp);
        dest.writeValue(this.windowConfiguration);
        dest.writeInt(this.assetsSeq);
        dest.writeInt(this.seq);
        this.extraConfig.writeToParcel(dest, flags);
    }

    public void readFromParcel(Parcel source) {
        this.fontScale = source.readFloat();
        this.mcc = source.readInt();
        this.mnc = source.readInt();
        this.mLocaleList = (LocaleList) source.readParcelable(LocaleList.class.getClassLoader());
        boolean z = false;
        this.locale = this.mLocaleList.get(0);
        if (source.readInt() == 1) {
            z = true;
        }
        this.userSetLocale = z;
        this.touchscreen = source.readInt();
        this.keyboard = source.readInt();
        this.keyboardHidden = source.readInt();
        this.hardKeyboardHidden = source.readInt();
        this.navigation = source.readInt();
        this.navigationHidden = source.readInt();
        this.orientation = source.readInt();
        this.screenLayout = source.readInt();
        this.colorMode = source.readInt();
        this.uiMode = source.readInt();
        this.screenWidthDp = source.readInt();
        this.screenHeightDp = source.readInt();
        this.smallestScreenWidthDp = source.readInt();
        this.densityDpi = source.readInt();
        this.compatScreenWidthDp = source.readInt();
        this.compatScreenHeightDp = source.readInt();
        this.compatSmallestScreenWidthDp = source.readInt();
        this.windowConfiguration.setTo((WindowConfiguration) source.readValue(null));
        this.assetsSeq = source.readInt();
        this.seq = source.readInt();
        this.extraConfig.readFromParcel(source);
    }

    private Configuration(Parcel source) {
        this.windowConfiguration = new WindowConfiguration();
        this.extraConfig = new MiuiConfiguration();
        readFromParcel(source);
    }

    public int compareTo(Configuration that) {
        float a = this.fontScale;
        float b = that.fontScale;
        if (a < b) {
            return -1;
        }
        if (a > b) {
            return 1;
        }
        int n = this.mcc - that.mcc;
        if (n != 0) {
            return n;
        }
        int n2 = this.mnc - that.mnc;
        if (n2 != 0) {
            return n2;
        }
        int i;
        fixUpLocaleList();
        that.fixUpLocaleList();
        if (this.mLocaleList.isEmpty()) {
            if (!that.mLocaleList.isEmpty()) {
                return 1;
            }
        } else if (that.mLocaleList.isEmpty()) {
            return -1;
        } else {
            n = Math.min(this.mLocaleList.size(), that.mLocaleList.size());
            for (i = 0; i < n; i++) {
                Locale thisLocale = this.mLocaleList.get(i);
                Locale thatLocale = that.mLocaleList.get(i);
                n2 = thisLocale.getLanguage().compareTo(thatLocale.getLanguage());
                if (n2 != 0) {
                    return n2;
                }
                n2 = thisLocale.getCountry().compareTo(thatLocale.getCountry());
                if (n2 != 0) {
                    return n2;
                }
                n2 = thisLocale.getVariant().compareTo(thatLocale.getVariant());
                if (n2 != 0) {
                    return n2;
                }
                n2 = thisLocale.toLanguageTag().compareTo(thatLocale.toLanguageTag());
                if (n2 != 0) {
                    return n2;
                }
            }
            n2 = this.mLocaleList.size() - that.mLocaleList.size();
            if (n2 != 0) {
                return n2;
            }
        }
        n = this.touchscreen - that.touchscreen;
        if (n != 0) {
            return n;
        }
        i = this.keyboard - that.keyboard;
        if (i != 0) {
            return i;
        }
        n = this.keyboardHidden - that.keyboardHidden;
        if (n != 0) {
            return n;
        }
        i = this.hardKeyboardHidden - that.hardKeyboardHidden;
        if (i != 0) {
            return i;
        }
        n = this.navigation - that.navigation;
        if (n != 0) {
            return n;
        }
        i = this.navigationHidden - that.navigationHidden;
        if (i != 0) {
            return i;
        }
        n = this.orientation - that.orientation;
        if (n != 0) {
            return n;
        }
        i = this.colorMode - that.colorMode;
        if (i != 0) {
            return i;
        }
        n = this.screenLayout - that.screenLayout;
        if (n != 0) {
            return n;
        }
        i = this.uiMode - that.uiMode;
        if (i != 0) {
            return i;
        }
        n = this.screenWidthDp - that.screenWidthDp;
        if (n != 0) {
            return n;
        }
        i = this.screenHeightDp - that.screenHeightDp;
        if (i != 0) {
            return i;
        }
        n = this.smallestScreenWidthDp - that.smallestScreenWidthDp;
        if (n != 0) {
            return n;
        }
        i = this.densityDpi - that.densityDpi;
        if (i != 0) {
            return i;
        }
        n = this.assetsSeq - that.assetsSeq;
        if (n != 0) {
            return n;
        }
        n = this.windowConfiguration.compareTo(that.windowConfiguration);
        if (n != 0) {
            return n;
        }
        if (n == 0) {
            n = this.extraConfig.compareTo(that.extraConfig);
        }
        return n;
    }

    public boolean equals(Configuration that) {
        boolean z = false;
        if (that == null) {
            return false;
        }
        if (that == this) {
            return true;
        }
        if (compareTo(that) == 0) {
            z = true;
        }
        return z;
    }

    public boolean equals(Object that) {
        try {
            return equals((Configuration) that);
        } catch (ClassCastException e) {
            return false;
        }
    }

    public int hashCode() {
        return (((((((((((((((((((((((((((((((((((((((17 * 31) + Float.floatToIntBits(this.fontScale)) * 31) + this.mcc) * 31) + this.mnc) * 31) + this.mLocaleList.hashCode()) * 31) + this.touchscreen) * 31) + this.keyboard) * 31) + this.keyboardHidden) * 31) + this.hardKeyboardHidden) * 31) + this.navigation) * 31) + this.navigationHidden) * 31) + this.orientation) * 31) + this.screenLayout) * 31) + this.colorMode) * 31) + this.uiMode) * 31) + this.screenWidthDp) * 31) + this.screenHeightDp) * 31) + this.smallestScreenWidthDp) * 31) + this.densityDpi) * 31) + this.assetsSeq) * 31) + this.extraConfig.hashCode();
    }

    public LocaleList getLocales() {
        fixUpLocaleList();
        return this.mLocaleList;
    }

    public void setLocales(LocaleList locales) {
        this.mLocaleList = locales == null ? LocaleList.getEmptyLocaleList() : locales;
        this.locale = this.mLocaleList.get(0);
        setLayoutDirection(this.locale);
    }

    public void setLocale(Locale loc) {
        LocaleList emptyLocaleList;
        if (loc == null) {
            emptyLocaleList = LocaleList.getEmptyLocaleList();
        } else {
            emptyLocaleList = new LocaleList(loc);
        }
        setLocales(emptyLocaleList);
    }

    public void clearLocales() {
        this.mLocaleList = LocaleList.getEmptyLocaleList();
        this.locale = null;
    }

    public int getLayoutDirection() {
        return (this.screenLayout & 192) == 128 ? 1 : 0;
    }

    public void setLayoutDirection(Locale loc) {
        this.screenLayout = (this.screenLayout & -193) | ((TextUtils.getLayoutDirectionFromLocale(loc) + 1) << 6);
    }

    private static int getScreenLayoutNoDirection(int screenLayout) {
        return screenLayout & -193;
    }

    public boolean isScreenRound() {
        return (this.screenLayout & 768) == 512;
    }

    public boolean isScreenWideColorGamut() {
        return (this.colorMode & 3) == 2;
    }

    public boolean isScreenHdr() {
        return (this.colorMode & 12) == 8;
    }

    public static String localesToResourceQualifier(LocaleList locs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < locs.size(); i++) {
            Locale loc = locs.get(i);
            int l = loc.getLanguage().length();
            if (l != 0) {
                int s = loc.getScript().length();
                int c = loc.getCountry().length();
                int v = loc.getVariant().length();
                if (sb.length() != 0) {
                    sb.append(",");
                }
                if (l == 2 && s == 0 && ((c == 0 || c == 2) && v == 0)) {
                    sb.append(loc.getLanguage());
                    if (c == 2) {
                        sb.append("-r");
                        sb.append(loc.getCountry());
                    }
                } else {
                    sb.append("b+");
                    sb.append(loc.getLanguage());
                    String str = "+";
                    if (s != 0) {
                        sb.append(str);
                        sb.append(loc.getScript());
                    }
                    if (c != 0) {
                        sb.append(str);
                        sb.append(loc.getCountry());
                    }
                    if (v != 0) {
                        sb.append(str);
                        sb.append(loc.getVariant());
                    }
                }
            }
        }
        return sb.toString();
    }

    @UnsupportedAppUsage
    public static String resourceQualifierString(Configuration config) {
        return resourceQualifierString(config, null);
    }

    public static String resourceQualifierString(Configuration config, DisplayMetrics metrics) {
        StringBuilder stringBuilder;
        Iterable parts = new ArrayList();
        if (config.mcc != 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("mcc");
            stringBuilder.append(config.mcc);
            parts.add(stringBuilder.toString());
            if (config.mnc != 0) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("mnc");
                stringBuilder.append(config.mnc);
                parts.add(stringBuilder.toString());
            }
        }
        if (!config.mLocaleList.isEmpty()) {
            String resourceQualifier = localesToResourceQualifier(config.mLocaleList);
            if (!resourceQualifier.isEmpty()) {
                parts.add(resourceQualifier);
            }
        }
        int i = config.screenLayout & 192;
        if (i == 64) {
            parts.add("ldltr");
        } else if (i == 128) {
            parts.add("ldrtl");
        }
        String str = "dp";
        if (config.smallestScreenWidthDp != 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(XML_ATTR_SMALLEST_WIDTH);
            stringBuilder.append(config.smallestScreenWidthDp);
            stringBuilder.append(str);
            parts.add(stringBuilder.toString());
        }
        if (config.screenWidthDp != 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(BiometricConnect.MSG_CB_BUNDLE_DEPTHMAP_W);
            stringBuilder.append(config.screenWidthDp);
            stringBuilder.append(str);
            parts.add(stringBuilder.toString());
        }
        if (config.screenHeightDp != 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(BiometricConnect.MSG_CB_BUNDLE_DEPTHMAP_H);
            stringBuilder.append(config.screenHeightDp);
            stringBuilder.append(str);
            parts.add(stringBuilder.toString());
        }
        i = config.screenLayout & 15;
        if (i == 1) {
            parts.add("small");
        } else if (i == 2) {
            parts.add("normal");
        } else if (i == 3) {
            parts.add(Slice.HINT_LARGE);
        } else if (i == 4) {
            parts.add("xlarge");
        }
        i = config.screenLayout & 48;
        if (i == 16) {
            parts.add("notlong");
        } else if (i == 32) {
            parts.add("long");
        }
        i = config.screenLayout & 768;
        if (i == 256) {
            parts.add("notround");
        } else if (i == 512) {
            parts.add("round");
        }
        i = config.colorMode & 3;
        if (i == 1) {
            parts.add("nowidecg");
        } else if (i == 2) {
            parts.add("widecg");
        }
        i = config.colorMode & 12;
        if (i == 4) {
            parts.add("lowdr");
        } else if (i == 8) {
            parts.add("highdr");
        }
        i = config.orientation;
        if (i == 1) {
            parts.add("port");
        } else if (i == 2) {
            parts.add("land");
        }
        switch (config.uiMode & 15) {
            case 2:
                parts.add("desk");
                break;
            case 3:
                parts.add("car");
                break;
            case 4:
                parts.add("television");
                break;
            case 5:
                parts.add("appliance");
                break;
            case 6:
                parts.add("watch");
                break;
            case 7:
                parts.add("vrheadset");
                break;
        }
        i = config.uiMode & 48;
        if (i == 16) {
            parts.add("notnight");
        } else if (i == 32) {
            parts.add(Parameters.SCENE_MODE_NIGHT);
        }
        i = config.densityDpi;
        if (i != 0) {
            if (i == 120) {
                parts.add("ldpi");
            } else if (i == 160) {
                parts.add("mdpi");
            } else if (i == 213) {
                parts.add("tvdpi");
            } else if (i == 240) {
                parts.add("hdpi");
            } else if (i == 320) {
                parts.add("xhdpi");
            } else if (i == 480) {
                parts.add("xxhdpi");
            } else if (i != 640) {
                switch (i) {
                    case DENSITY_DPI_ANY /*65534*/:
                        parts.add("anydpi");
                        break;
                    case 65535:
                        parts.add("nodpi");
                        break;
                    default:
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(config.densityDpi);
                        stringBuilder.append("dpi");
                        parts.add(stringBuilder.toString());
                        break;
                }
            } else {
                parts.add("xxxhdpi");
            }
        }
        i = config.touchscreen;
        if (i == 1) {
            parts.add("notouch");
        } else if (i == 3) {
            parts.add("finger");
        }
        i = config.keyboardHidden;
        if (i == 1) {
            parts.add("keysexposed");
        } else if (i == 2) {
            parts.add("keyshidden");
        } else if (i == 3) {
            parts.add("keyssoft");
        }
        i = config.keyboard;
        if (i == 1) {
            parts.add("nokeys");
        } else if (i == 2) {
            parts.add("qwerty");
        } else if (i == 3) {
            parts.add("12key");
        }
        i = config.navigationHidden;
        if (i == 1) {
            parts.add("navexposed");
        } else if (i == 2) {
            parts.add("navhidden");
        }
        i = config.navigation;
        if (i == 1) {
            parts.add("nonav");
        } else if (i == 2) {
            parts.add("dpad");
        } else if (i == 3) {
            parts.add("trackball");
        } else if (i == 4) {
            parts.add("wheel");
        }
        if (metrics != null) {
            int height;
            if (metrics.widthPixels >= metrics.heightPixels) {
                i = metrics.widthPixels;
                height = metrics.heightPixels;
            } else {
                i = metrics.heightPixels;
                height = metrics.widthPixels;
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(i);
            stringBuilder2.append("x");
            stringBuilder2.append(height);
            parts.add(stringBuilder2.toString());
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(BaseMmsColumns.MMS_VERSION);
        stringBuilder.append(VERSION.RESOURCES_SDK_INT);
        parts.add(stringBuilder.toString());
        return TextUtils.join((CharSequence) "-", parts);
    }

    @UnsupportedAppUsage
    public static Configuration generateDelta(Configuration base, Configuration change) {
        Configuration delta = new Configuration();
        float f = base.fontScale;
        float f2 = change.fontScale;
        if (f != f2) {
            delta.fontScale = f2;
        }
        int i = base.mcc;
        int i2 = change.mcc;
        if (i != i2) {
            delta.mcc = i2;
        }
        i = base.mnc;
        i2 = change.mnc;
        if (i != i2) {
            delta.mnc = i2;
        }
        base.fixUpLocaleList();
        change.fixUpLocaleList();
        if (!base.mLocaleList.equals(change.mLocaleList)) {
            delta.mLocaleList = change.mLocaleList;
            delta.locale = change.locale;
        }
        i = base.touchscreen;
        i2 = change.touchscreen;
        if (i != i2) {
            delta.touchscreen = i2;
        }
        i = base.keyboard;
        i2 = change.keyboard;
        if (i != i2) {
            delta.keyboard = i2;
        }
        i = base.keyboardHidden;
        i2 = change.keyboardHidden;
        if (i != i2) {
            delta.keyboardHidden = i2;
        }
        i = base.navigation;
        i2 = change.navigation;
        if (i != i2) {
            delta.navigation = i2;
        }
        i = base.navigationHidden;
        i2 = change.navigationHidden;
        if (i != i2) {
            delta.navigationHidden = i2;
        }
        i = base.orientation;
        i2 = change.orientation;
        if (i != i2) {
            delta.orientation = i2;
        }
        i = base.screenLayout & 15;
        i2 = change.screenLayout;
        if (i != (i2 & 15)) {
            delta.screenLayout |= i2 & 15;
        }
        i = base.screenLayout & 192;
        i2 = change.screenLayout;
        if (i != (i2 & 192)) {
            delta.screenLayout |= i2 & 192;
        }
        i = base.screenLayout & 48;
        i2 = change.screenLayout;
        if (i != (i2 & 48)) {
            delta.screenLayout |= i2 & 48;
        }
        i = base.screenLayout & 768;
        i2 = change.screenLayout;
        if (i != (i2 & 768)) {
            delta.screenLayout |= i2 & 768;
        }
        i = base.colorMode & 3;
        i2 = change.colorMode;
        if (i != (i2 & 3)) {
            delta.colorMode |= i2 & 3;
        }
        i = base.colorMode & 12;
        i2 = change.colorMode;
        if (i != (i2 & 12)) {
            delta.colorMode |= i2 & 12;
        }
        i = base.uiMode & 15;
        i2 = change.uiMode;
        if (i != (i2 & 15)) {
            delta.uiMode |= i2 & 15;
        }
        i = base.uiMode & 48;
        i2 = change.uiMode;
        if (i != (i2 & 48)) {
            delta.uiMode |= i2 & 48;
        }
        i = base.screenWidthDp;
        i2 = change.screenWidthDp;
        if (i != i2) {
            delta.screenWidthDp = i2;
        }
        i = base.screenHeightDp;
        i2 = change.screenHeightDp;
        if (i != i2) {
            delta.screenHeightDp = i2;
        }
        i = base.smallestScreenWidthDp;
        i2 = change.smallestScreenWidthDp;
        if (i != i2) {
            delta.smallestScreenWidthDp = i2;
        }
        i = base.densityDpi;
        i2 = change.densityDpi;
        if (i != i2) {
            delta.densityDpi = i2;
        }
        i = base.assetsSeq;
        i2 = change.assetsSeq;
        if (i != i2) {
            delta.assetsSeq = i2;
        }
        if (!base.windowConfiguration.equals(change.windowConfiguration)) {
            delta.windowConfiguration.setTo(change.windowConfiguration);
        }
        if (base.extraConfig.compareTo(change.extraConfig) != 0) {
            delta.extraConfig.setTo(change.extraConfig);
        }
        return delta;
    }

    public static void readXmlAttrs(XmlPullParser parser, Configuration configOut) throws XmlPullParserException, IOException {
        configOut.fontScale = Float.intBitsToFloat(XmlUtils.readIntAttribute(parser, XML_ATTR_FONT_SCALE, 0));
        configOut.mcc = XmlUtils.readIntAttribute(parser, "mcc", 0);
        configOut.mnc = XmlUtils.readIntAttribute(parser, "mnc", 0);
        configOut.mLocaleList = LocaleList.forLanguageTags(XmlUtils.readStringAttribute(parser, XML_ATTR_LOCALES));
        configOut.locale = configOut.mLocaleList.get(0);
        configOut.touchscreen = XmlUtils.readIntAttribute(parser, XML_ATTR_TOUCHSCREEN, 0);
        configOut.keyboard = XmlUtils.readIntAttribute(parser, "key", 0);
        configOut.keyboardHidden = XmlUtils.readIntAttribute(parser, XML_ATTR_KEYBOARD_HIDDEN, 0);
        configOut.hardKeyboardHidden = XmlUtils.readIntAttribute(parser, XML_ATTR_HARD_KEYBOARD_HIDDEN, 0);
        configOut.navigation = XmlUtils.readIntAttribute(parser, XML_ATTR_NAVIGATION, 0);
        configOut.navigationHidden = XmlUtils.readIntAttribute(parser, XML_ATTR_NAVIGATION_HIDDEN, 0);
        configOut.orientation = XmlUtils.readIntAttribute(parser, XML_ATTR_ORIENTATION, 0);
        configOut.screenLayout = XmlUtils.readIntAttribute(parser, XML_ATTR_SCREEN_LAYOUT, 0);
        configOut.colorMode = XmlUtils.readIntAttribute(parser, XML_ATTR_COLOR_MODE, 0);
        configOut.uiMode = XmlUtils.readIntAttribute(parser, XML_ATTR_UI_MODE, 0);
        configOut.screenWidthDp = XmlUtils.readIntAttribute(parser, "width", 0);
        configOut.screenHeightDp = XmlUtils.readIntAttribute(parser, "height", 0);
        configOut.smallestScreenWidthDp = XmlUtils.readIntAttribute(parser, XML_ATTR_SMALLEST_WIDTH, 0);
        configOut.densityDpi = XmlUtils.readIntAttribute(parser, XML_ATTR_DENSITY, 0);
    }

    public static void writeXmlAttrs(XmlSerializer xml, Configuration config) throws IOException {
        XmlUtils.writeIntAttribute(xml, XML_ATTR_FONT_SCALE, Float.floatToIntBits(config.fontScale));
        int i = config.mcc;
        if (i != 0) {
            XmlUtils.writeIntAttribute(xml, "mcc", i);
        }
        i = config.mnc;
        if (i != 0) {
            XmlUtils.writeIntAttribute(xml, "mnc", i);
        }
        config.fixUpLocaleList();
        if (!config.mLocaleList.isEmpty()) {
            XmlUtils.writeStringAttribute(xml, XML_ATTR_LOCALES, config.mLocaleList.toLanguageTags());
        }
        i = config.touchscreen;
        if (i != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_TOUCHSCREEN, i);
        }
        i = config.keyboard;
        if (i != 0) {
            XmlUtils.writeIntAttribute(xml, "key", i);
        }
        i = config.keyboardHidden;
        if (i != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_KEYBOARD_HIDDEN, i);
        }
        i = config.hardKeyboardHidden;
        if (i != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_HARD_KEYBOARD_HIDDEN, i);
        }
        i = config.navigation;
        if (i != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_NAVIGATION, i);
        }
        i = config.navigationHidden;
        if (i != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_NAVIGATION_HIDDEN, i);
        }
        i = config.orientation;
        if (i != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_ORIENTATION, i);
        }
        i = config.screenLayout;
        if (i != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_SCREEN_LAYOUT, i);
        }
        i = config.colorMode;
        if (i != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_COLOR_MODE, i);
        }
        i = config.uiMode;
        if (i != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_UI_MODE, i);
        }
        i = config.screenWidthDp;
        if (i != 0) {
            XmlUtils.writeIntAttribute(xml, "width", i);
        }
        i = config.screenHeightDp;
        if (i != 0) {
            XmlUtils.writeIntAttribute(xml, "height", i);
        }
        i = config.smallestScreenWidthDp;
        if (i != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_SMALLEST_WIDTH, i);
        }
        i = config.densityDpi;
        if (i != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_DENSITY, i);
        }
    }
}
