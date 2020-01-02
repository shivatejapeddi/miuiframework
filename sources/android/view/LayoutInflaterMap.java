package android.view;

import android.content.Context;
import android.miui.R;
import android.util.SparseArray;
import miui.os.Environment;
import miui.util.ResourceMapper;

public class LayoutInflaterMap {
    private static volatile SparseArray<Integer> sLayoutMap;
    private static final int[] sLayoutPairs = new int[]{R.layout.android_simple_list_item_2_single_choice, R.layout.miui_simple_list_item_2_single_choice};

    private static boolean needResolveReference(int layoutId) {
        return (-16777216 & layoutId) != 16777216;
    }

    private static void buildLayoutMap(Context context) {
        if (sLayoutMap == null) {
            synchronized (LayoutInflaterMap.class) {
                if (sLayoutMap == null) {
                    SparseArray<Integer> layoutMap = new SparseArray();
                    int i = 0;
                    while (i < sLayoutPairs.length) {
                        layoutMap.put(needResolveReference(sLayoutPairs[i]) ? ResourceMapper.resolveReference(context.getResources(), sLayoutPairs[i]) : sLayoutPairs[i], Integer.valueOf(sLayoutPairs[i + 1]));
                        i += 2;
                    }
                    sLayoutMap = layoutMap;
                }
            }
        }
    }

    static int getResourceId(Context context, int resource) {
        int newResource = resource;
        if (!Environment.isUsingMiui(context)) {
            return newResource;
        }
        buildLayoutMap(context);
        Integer layout = (Integer) sLayoutMap.get(resource);
        if (layout != null) {
            return layout.intValue();
        }
        return newResource;
    }
}
