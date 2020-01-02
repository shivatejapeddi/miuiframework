package android.view;

import android.app.ActivityThread;
import android.os.SystemProperties;

public class ChoreographerInjector {
    private static final boolean DEBUG_TAG = false;
    private static final boolean FRAME_OPTS = SystemProperties.getBoolean("persist.sys.frame_opts", false);
    private static final String MONITOR_PACKAGE = getMonitorPackage();
    private static final String TAG = "ChoreographerInjector";
    private static final double[] increaseCountArr = new double[]{0.5d, 0.25d, 0.125d, 1.0d};
    private static double increaseRatio;
    private static int index;
    private static int insertFrameCounter = 0;
    private static final Choreographer mChoreographer = Choreographer.getInstance();

    static {
        double random = Math.random();
        double[] dArr = increaseCountArr;
        index = (int) (random * ((double) dArr.length));
        increaseRatio = dArr[index];
    }

    private ChoreographerInjector() {
    }

    public static void monitorFrame() {
        if (FRAME_OPTS && ActivityThread.currentPackageName() != null && MONITOR_PACKAGE.equals(ActivityThread.currentPackageName())) {
            double d = increaseRatio;
            if (d >= 1.0d) {
                for (int i = 0; ((double) i) < increaseRatio; i++) {
                    mChoreographer.doCallbacks(1, System.nanoTime());
                }
            } else if (d > 0.0d) {
                int i2 = insertFrameCounter;
                if (((double) i2) == (1.0d / d) - 1.0d) {
                    mChoreographer.doCallbacks(1, System.nanoTime());
                    insertFrameCounter = 0;
                    return;
                }
                insertFrameCounter = i2 + 1;
            }
        }
    }

    private static String getMonitorPackage() {
        try {
            char[] c = "乃乏乍与乁乎乔乕乔乕与乡乢久乎乃么乭乁乒之".toCharArray();
            for (int i = 0; i < c.length; i++) {
                c[i] = (char) (c[i] ^ 20000);
            }
            return new String(c);
        } catch (Exception e) {
            return "null";
        }
    }
}
