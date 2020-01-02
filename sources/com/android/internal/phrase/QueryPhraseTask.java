package com.android.internal.phrase;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class QueryPhraseTask extends AsyncTask<Void, Void, ArrayList<String>> {
    private static final String TAG = "QueryPhraseTask";
    private WeakReference<Context> mContextRef;
    private WeakReference<QueryPhraseListener> mQueryPhraseListenerRef;

    public QueryPhraseTask(Context context, QueryPhraseListener queryPhraseListener) {
        this.mContextRef = new WeakReference(context);
        this.mQueryPhraseListenerRef = new WeakReference(queryPhraseListener);
    }

    /* Access modifiers changed, original: protected|varargs */
    public ArrayList<String> doInBackground(Void... voids) {
        Context context = (Context) this.mContextRef.get();
        ArrayList<String> phrases = new ArrayList();
        if (context == null || isCancelled()) {
            return phrases;
        }
        return queryPhrase(context);
    }

    /* Access modifiers changed, original: protected */
    public void onPostExecute(ArrayList<String> phrases) {
        QueryPhraseListener queryPhraseListener = (QueryPhraseListener) this.mQueryPhraseListenerRef.get();
        if (phrases != null && queryPhraseListener != null) {
            queryPhraseListener.onPostExecute(phrases);
        }
    }

    private static ArrayList<String> queryPhrase(Context context) {
        Cursor cursor = null;
        ArrayList<String> phraseList = new ArrayList();
        try {
            cursor = context.getContentResolver().query(Phrases.WORDS_CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String word = cursor.getString(cursor.getColumnIndex(Phrases.WORDS));
                    if (!TextUtils.isEmpty(word)) {
                        phraseList.add(word);
                    }
                }
            } else {
                Log.e(TAG, "fail to query Phrases.Phrase.WORDS_CONTENT_URI");
            }
            if (cursor != null) {
                cursor.close();
            }
            return phraseList;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
