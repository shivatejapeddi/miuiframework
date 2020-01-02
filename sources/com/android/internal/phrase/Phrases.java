package com.android.internal.phrase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

public class Phrases {
    public static final String AUTHORITY = "miui.phrase";
    public static Uri CONTENT_URI = Uri.parse("content://miui.phrase/phrase");
    private static String PHRASE_ADD_ACTIVITY_NAME = "com.miui.phrase.AddPhraseActivity";
    private static String PHRASE_EDIT_ACTIVITY_NAME = "com.miui.phrase.PhraseEditActivity";
    private static String PHRASE_PACKAGE_NAME = "com.miui.phrase";
    public static String WORDS = "words";
    public static Uri WORDS_CONTENT_URI = Uri.parse("content://miui.phrase/phrase/words");

    public static class Utils {
        private static String EXTRA_PHRASE_LIST = "phrase_list";

        private Utils() {
        }

        public static Intent getAddPhraseIntent() {
            return new Intent().setClassName(Phrases.PHRASE_PACKAGE_NAME, Phrases.PHRASE_ADD_ACTIVITY_NAME);
        }

        public static boolean isAddPhraseActivity(Context context) {
            boolean z = false;
            if (context == null || !(context instanceof Activity)) {
                return false;
            }
            if (TextUtils.equals(context.getPackageName(), Phrases.PHRASE_PACKAGE_NAME) && TextUtils.equals(context.getClass().getName(), Phrases.PHRASE_ADD_ACTIVITY_NAME)) {
                z = true;
            }
            return z;
        }

        public static Intent getPhraseEditIntent() {
            return new Intent().setClassName(Phrases.PHRASE_PACKAGE_NAME, Phrases.PHRASE_EDIT_ACTIVITY_NAME);
        }

        public static void startAddPhraseActivity(Context context, String phrase) {
            Intent intent = getAddPhraseIntent();
            intent.addFlags(268435456);
            if (!TextUtils.isEmpty(phrase)) {
                intent.putExtra(EXTRA_PHRASE_LIST, phrase);
            }
            context.startActivity(intent);
        }
    }
}
