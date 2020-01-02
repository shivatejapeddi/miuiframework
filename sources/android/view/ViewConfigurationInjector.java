package android.view;

import android.content.Context;
import android.miui.R;
import android.util.SparseArray;
import miui.os.Environment;

public class ViewConfigurationInjector {
    private static final SparseArray<ViewConfiguration> sConfigrations = new SparseArray(2);

    static ViewConfiguration get(Context context) {
        if (!Environment.isUsingMiui(context)) {
            return null;
        }
        return (ViewConfiguration) sConfigrations.get((int) (context.getResources().getDisplayMetrics().density * 100.0f));
    }

    static boolean needMiuiConfiguration(Context context) {
        return Environment.isUsingMiui(context);
    }

    static void put(Context context, ViewConfiguration configuration) {
        if (Environment.isUsingMiui(context)) {
            sConfigrations.put((int) (context.getResources().getDisplayMetrics().density * 100.0f), configuration);
        }
    }

    static int getOverScrollDistance(Context context, int defaultValue) {
        if (Environment.isUsingMiui(context)) {
            return (int) (context.getResources().getDimension(R.dimen.over_scroll_distance) + 0.5f);
        }
        return defaultValue;
    }

    static int getOverFlingDistance(Context context, int defaultValue) {
        if (Environment.isUsingMiui(context)) {
            return (int) (context.getResources().getDimension(R.dimen.over_fling_distance) + 0.5f);
        }
        return defaultValue;
    }
}
