package android.content.res;

import android.text.Spanned;

public final class ResourceId {
    public static boolean isValid(int id) {
        return (id == -1 || (-16777216 & id) == 0 || (Spanned.SPAN_PRIORITY & id) == 0) ? false : true;
    }
}
