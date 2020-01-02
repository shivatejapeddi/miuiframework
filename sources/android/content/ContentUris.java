package android.content;

import android.net.Uri;
import android.net.Uri.Builder;
import java.util.List;

public class ContentUris {
    public static long parseId(Uri contentUri) {
        String last = contentUri.getLastPathSegment();
        return last == null ? -1 : Long.parseLong(last);
    }

    public static Builder appendId(Builder builder, long id) {
        return builder.appendEncodedPath(String.valueOf(id));
    }

    public static Uri withAppendedId(Uri contentUri, long id) {
        return appendId(contentUri.buildUpon(), id).build();
    }

    public static Uri removeId(Uri contentUri) {
        String last = contentUri.getLastPathSegment();
        if (last != null) {
            Long.parseLong(last);
            List<String> segments = contentUri.getPathSegments();
            Builder builder = contentUri.buildUpon();
            builder.path(null);
            for (int i = 0; i < segments.size() - 1; i++) {
                builder.appendPath((String) segments.get(i));
            }
            return builder.build();
        }
        throw new IllegalArgumentException("No path segments to remove");
    }
}
