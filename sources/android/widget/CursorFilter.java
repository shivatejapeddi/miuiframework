package android.widget;

import android.database.Cursor;

class CursorFilter extends Filter {
    CursorFilterClient mClient;

    interface CursorFilterClient {
        void changeCursor(Cursor cursor);

        CharSequence convertToString(Cursor cursor);

        Cursor getCursor();

        Cursor runQueryOnBackgroundThread(CharSequence charSequence);
    }

    CursorFilter(CursorFilterClient client) {
        this.mClient = client;
    }

    public CharSequence convertResultToString(Object resultValue) {
        return this.mClient.convertToString((Cursor) resultValue);
    }

    /* Access modifiers changed, original: protected */
    public FilterResults performFiltering(CharSequence constraint) {
        Cursor cursor = this.mClient.runQueryOnBackgroundThread(constraint);
        FilterResults results = new FilterResults();
        if (cursor != null) {
            results.count = cursor.getCount();
            results.values = cursor;
        } else {
            results.count = 0;
            results.values = null;
        }
        return results;
    }

    /* Access modifiers changed, original: protected */
    public void publishResults(CharSequence constraint, FilterResults results) {
        Cursor oldCursor = this.mClient.getCursor();
        if (results.values != null && results.values != oldCursor) {
            this.mClient.changeCursor((Cursor) results.values);
        }
    }
}
