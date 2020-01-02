package android.service.autofill;

import android.os.Parcelable;
import android.util.Log;
import android.util.Pair;
import android.view.autofill.Helper;
import android.widget.RemoteViews;
import java.util.ArrayList;

public abstract class InternalTransformation implements Transformation, Parcelable {
    private static final String TAG = "InternalTransformation";

    public abstract void apply(ValueFinder valueFinder, RemoteViews remoteViews, int i) throws Exception;

    public static boolean batchApply(ValueFinder finder, RemoteViews template, ArrayList<Pair<Integer, InternalTransformation>> transformations) {
        int size = transformations.size();
        boolean z = Helper.sDebug;
        String str = TAG;
        if (z) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getPresentation(): applying ");
            stringBuilder.append(size);
            stringBuilder.append(" transformations");
            Log.d(str, stringBuilder.toString());
        }
        int i = 0;
        while (i < size) {
            Pair<Integer, InternalTransformation> pair = (Pair) transformations.get(i);
            int id = ((Integer) pair.first).intValue();
            InternalTransformation transformation = pair.second;
            String str2 = ": ";
            if (Helper.sDebug) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("#");
                stringBuilder2.append(i);
                stringBuilder2.append(str2);
                stringBuilder2.append(transformation);
                Log.d(str, stringBuilder2.toString());
            }
            try {
                transformation.apply(finder, template, id);
                i++;
            } catch (Exception e) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("Could not apply transformation ");
                stringBuilder3.append(transformation);
                stringBuilder3.append(str2);
                stringBuilder3.append(e.getClass());
                Log.e(str, stringBuilder3.toString());
                return false;
            }
        }
        return true;
    }
}
