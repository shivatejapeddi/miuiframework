package android.content;

import android.annotation.UnsupportedAppUsage;
import android.app.SearchManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class SearchRecentSuggestionsProvider extends ContentProvider {
    public static final int DATABASE_MODE_2LINES = 2;
    public static final int DATABASE_MODE_QUERIES = 1;
    private static final int DATABASE_VERSION = 512;
    private static final String NULL_COLUMN = "query";
    private static final String ORDER_BY = "date DESC";
    private static final String TAG = "SuggestionsProvider";
    private static final int URI_MATCH_SUGGEST = 1;
    private static final String sDatabaseName = "suggestions.db";
    private static final String sSuggestions = "suggestions";
    private String mAuthority;
    private int mMode;
    private SQLiteOpenHelper mOpenHelper;
    private String mSuggestSuggestionClause;
    @UnsupportedAppUsage
    private String[] mSuggestionProjection;
    private Uri mSuggestionsUri;
    private boolean mTwoLineDisplay;
    private UriMatcher mUriMatcher;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private int mNewVersion;

        public DatabaseHelper(Context context, int newVersion) {
            super(context, SearchRecentSuggestionsProvider.sDatabaseName, null, newVersion);
            this.mNewVersion = newVersion;
        }

        public void onCreate(SQLiteDatabase db) {
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE suggestions (_id INTEGER PRIMARY KEY,display1 TEXT UNIQUE ON CONFLICT REPLACE");
            if ((this.mNewVersion & 2) != 0) {
                builder.append(",display2 TEXT");
            }
            builder.append(",query TEXT,date LONG);");
            db.execSQL(builder.toString());
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Upgrading database from version ");
            stringBuilder.append(oldVersion);
            stringBuilder.append(" to ");
            stringBuilder.append(newVersion);
            stringBuilder.append(", which will destroy all old data");
            Log.w(SearchRecentSuggestionsProvider.TAG, stringBuilder.toString());
            db.execSQL("DROP TABLE IF EXISTS suggestions");
            onCreate(db);
        }
    }

    /* Access modifiers changed, original: protected */
    public void setupSuggestions(String authority, int mode) {
        if (TextUtils.isEmpty(authority) || (mode & 1) == 0) {
            throw new IllegalArgumentException();
        }
        this.mTwoLineDisplay = (mode & 2) != 0;
        this.mAuthority = new String(authority);
        this.mMode = mode;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("content://");
        stringBuilder.append(this.mAuthority);
        stringBuilder.append("/suggestions");
        this.mSuggestionsUri = Uri.parse(stringBuilder.toString());
        this.mUriMatcher = new UriMatcher(-1);
        this.mUriMatcher.addURI(this.mAuthority, SearchManager.SUGGEST_URI_PATH_QUERY, 1);
        if (this.mTwoLineDisplay) {
            this.mSuggestSuggestionClause = "display1 LIKE ? OR display2 LIKE ?";
            this.mSuggestionProjection = new String[]{"0 AS suggest_format", "'android.resource://system/17301578' AS suggest_icon_1", "display1 AS suggest_text_1", "display2 AS suggest_text_2", "query AS suggest_intent_query", "_id"};
            return;
        }
        this.mSuggestSuggestionClause = "display1 LIKE ?";
        this.mSuggestionProjection = new String[]{"0 AS suggest_format", "'android.resource://system/17301578' AS suggest_icon_1", "display1 AS suggest_text_1", "query AS suggest_intent_query", "_id"};
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();
        String str = "Unknown Uri";
        if (uri.getPathSegments().size() == 1) {
            String str2 = "suggestions";
            if (((String) uri.getPathSegments().get(0)).equals(str2)) {
                int count = db.delete(str2, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            }
            throw new IllegalArgumentException(str);
        }
        throw new IllegalArgumentException(str);
    }

    public String getType(Uri uri) {
        if (this.mUriMatcher.match(uri) == 1) {
            return SearchManager.SUGGEST_MIME_TYPE;
        }
        int length = uri.getPathSegments().size();
        if (length >= 1 && ((String) uri.getPathSegments().get(0)).equals("suggestions")) {
            if (length == 1) {
                return "vnd.android.cursor.dir/suggestion";
            }
            if (length == 2) {
                return "vnd.android.cursor.item/suggestion";
            }
        }
        throw new IllegalArgumentException("Unknown Uri");
    }

    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();
        int length = uri.getPathSegments().size();
        String str = "Unknown Uri";
        if (length >= 1) {
            long rowID = -1;
            Uri newUri = null;
            String str2 = "suggestions";
            if (((String) uri.getPathSegments().get(0)).equals(str2) && length == 1) {
                rowID = db.insert(str2, "query", values);
                if (rowID > 0) {
                    newUri = Uri.withAppendedPath(this.mSuggestionsUri, String.valueOf(rowID));
                }
            }
            if (rowID >= 0) {
                getContext().getContentResolver().notifyChange(newUri, null);
                return newUri;
            }
            throw new IllegalArgumentException(str);
        }
        throw new IllegalArgumentException(str);
    }

    public boolean onCreate() {
        if (this.mAuthority != null) {
            int mWorkingDbVersion = this.mMode;
            if (mWorkingDbVersion != 0) {
                this.mOpenHelper = new DatabaseHelper(getContext(), mWorkingDbVersion + 512);
                return true;
            }
        }
        throw new IllegalArgumentException("Provider not configured");
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Uri uri2 = uri;
        Object obj = projection;
        String str = selection;
        SQLiteDatabase db = this.mOpenHelper.getReadableDatabase();
        String suggestSelection;
        if (this.mUriMatcher.match(uri2) == 1) {
            String[] myArgs;
            if (TextUtils.isEmpty(selectionArgs[0])) {
                suggestSelection = null;
                myArgs = null;
            } else {
                suggestSelection = new StringBuilder();
                String str2 = "%";
                suggestSelection.append(str2);
                suggestSelection.append(selectionArgs[0]);
                suggestSelection.append(str2);
                suggestSelection = suggestSelection.toString();
                String[] myArgs2 = this.mTwoLineDisplay ? new String[]{suggestSelection, suggestSelection} : new String[]{suggestSelection};
                suggestSelection = this.mSuggestSuggestionClause;
                myArgs = myArgs2;
            }
            Cursor c = db.query("suggestions", this.mSuggestionProjection, suggestSelection, myArgs, null, null, "date DESC", null);
            c.setNotificationUri(getContext().getContentResolver(), uri2);
            return c;
        }
        int length = uri.getPathSegments().size();
        suggestSelection = "Unknown Uri";
        if (length == 1 || length == 2) {
            String base = (String) uri.getPathSegments().get(0);
            if (base.equals("suggestions")) {
                String[] useProjection;
                if (obj == null || obj.length <= 0) {
                    useProjection = null;
                } else {
                    String[] useProjection2 = new String[(obj.length + 1)];
                    System.arraycopy(obj, 0, useProjection2, 0, obj.length);
                    useProjection2[obj.length] = "_id AS _id";
                    useProjection = useProjection2;
                }
                StringBuilder whereClause = new StringBuilder(256);
                if (length == 2) {
                    whereClause.append("(_id = ");
                    whereClause.append((String) uri.getPathSegments().get(1));
                    whereClause.append(")");
                }
                if (str != null && selection.length() > 0) {
                    if (whereClause.length() > 0) {
                        whereClause.append(" AND ");
                    }
                    whereClause.append('(');
                    whereClause.append(str);
                    whereClause.append(')');
                }
                Cursor c2 = db.query(base, useProjection, whereClause.toString(), selectionArgs, null, null, sortOrder, null);
                c2.setNotificationUri(getContext().getContentResolver(), uri2);
                return c2;
            }
            throw new IllegalArgumentException(suggestSelection);
        }
        throw new IllegalArgumentException(suggestSelection);
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
