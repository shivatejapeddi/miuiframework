package android.inputmethodservice;

import android.miui.R;
import android.os.Build;
import android.util.Log;
import java.util.HashSet;

public class MiuiBottomConfig {
    private static final String TAG = "MiuiBottomConfig";
    public static HashSet<String> sBigChinDevices = new HashSet();
    public static HashSet<String> sMiddleChinDevices = new HashSet();
    public static HashSet<String> sSmallChinDevices = new HashSet();

    static {
        sSmallChinDevices.add("andromeda");
        sSmallChinDevices.add("cepheus");
        sSmallChinDevices.add("crux");
        sSmallChinDevices.add("pyxis");
        sSmallChinDevices.add("vela");
        sSmallChinDevices.add("begonia");
        sSmallChinDevices.add("grus");
        sSmallChinDevices.add("perseus");
        sSmallChinDevices.add("raphael");
        sSmallChinDevices.add("davinci");
        sMiddleChinDevices.add("dipper");
        sMiddleChinDevices.add("onc");
        sMiddleChinDevices.add("sirius");
        sMiddleChinDevices.add("olive");
        sMiddleChinDevices.add("platina");
        sMiddleChinDevices.add("lavender");
        sMiddleChinDevices.add("ginkgo");
        sMiddleChinDevices.add("olivelite");
        sMiddleChinDevices.add("equuleus");
        sMiddleChinDevices.add("ursa");
        sMiddleChinDevices.add("tulip");
        sMiddleChinDevices.add("violet");
        sMiddleChinDevices.add("lotus");
        sBigChinDevices.add("chiron");
        sBigChinDevices.add("ysl");
        sBigChinDevices.add("vince");
        sBigChinDevices.add("cactus");
        sBigChinDevices.add("cereus");
        sBigChinDevices.add("nitrogen");
        sBigChinDevices.add("wayne");
        sBigChinDevices.add("whyred");
        sBigChinDevices.add("laurus");
        sBigChinDevices.add("beryllium");
        sBigChinDevices.add("daisy");
        sBigChinDevices.add("rosy");
        sBigChinDevices.add("pine");
        sBigChinDevices.add("polaris");
        sBigChinDevices.add("willow");
        sBigChinDevices.add("lithium");
    }

    public static int getLayoutForDevice() {
        boolean contains = sBigChinDevices.contains(Build.DEVICE);
        String str = TAG;
        if (contains || sMiddleChinDevices.contains(Build.DEVICE)) {
            Log.d(str, "use low bottom");
            return R.layout.input_bottom_low;
        }
        Log.d(str, "use normal bottom");
        return R.layout.input_bottom;
    }
}
