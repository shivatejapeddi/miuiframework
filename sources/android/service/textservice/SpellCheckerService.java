package android.service.textservice;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.text.TextUtils;
import android.text.method.WordIterator;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import com.android.internal.textservice.ISpellCheckerService;
import com.android.internal.textservice.ISpellCheckerServiceCallback;
import com.android.internal.textservice.ISpellCheckerSession.Stub;
import com.android.internal.textservice.ISpellCheckerSessionListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;

public abstract class SpellCheckerService extends Service {
    private static final boolean DBG = false;
    public static final String SERVICE_INTERFACE = "android.service.textservice.SpellCheckerService";
    private static final String TAG = SpellCheckerService.class.getSimpleName();
    private final SpellCheckerServiceBinder mBinder = new SpellCheckerServiceBinder(this);

    private static class InternalISpellCheckerSession extends Stub {
        private final Bundle mBundle;
        private ISpellCheckerSessionListener mListener;
        private final String mLocale;
        private final Session mSession;

        public InternalISpellCheckerSession(String locale, ISpellCheckerSessionListener listener, Bundle bundle, Session session) {
            this.mListener = listener;
            this.mSession = session;
            this.mLocale = locale;
            this.mBundle = bundle;
            session.setInternalISpellCheckerSession(this);
        }

        public void onGetSuggestionsMultiple(TextInfo[] textInfos, int suggestionsLimit, boolean sequentialWords) {
            int pri = Process.getThreadPriority(Process.myTid());
            try {
                Process.setThreadPriority(10);
                this.mListener.onGetSuggestions(this.mSession.onGetSuggestionsMultiple(textInfos, suggestionsLimit, sequentialWords));
            } catch (RemoteException e) {
            } catch (Throwable th) {
                Process.setThreadPriority(pri);
            }
            Process.setThreadPriority(pri);
        }

        public void onGetSentenceSuggestionsMultiple(TextInfo[] textInfos, int suggestionsLimit) {
            try {
                this.mListener.onGetSentenceSuggestions(this.mSession.onGetSentenceSuggestionsMultiple(textInfos, suggestionsLimit));
            } catch (RemoteException e) {
            }
        }

        public void onCancel() {
            int pri = Process.getThreadPriority(Process.myTid());
            try {
                Process.setThreadPriority(10);
                this.mSession.onCancel();
            } finally {
                Process.setThreadPriority(pri);
            }
        }

        public void onClose() {
            int pri = Process.getThreadPriority(Process.myTid());
            try {
                Process.setThreadPriority(10);
                this.mSession.onClose();
            } finally {
                Process.setThreadPriority(pri);
                this.mListener = null;
            }
        }

        public String getLocale() {
            return this.mLocale;
        }

        public Bundle getBundle() {
            return this.mBundle;
        }
    }

    private static class SentenceLevelAdapter {
        public static final SentenceSuggestionsInfo[] EMPTY_SENTENCE_SUGGESTIONS_INFOS = new SentenceSuggestionsInfo[0];
        private static final SuggestionsInfo EMPTY_SUGGESTIONS_INFO = new SuggestionsInfo(0, null);
        private final WordIterator mWordIterator;

        public static class SentenceTextInfoParams {
            final ArrayList<SentenceWordItem> mItems;
            final TextInfo mOriginalTextInfo;
            final int mSize;

            public SentenceTextInfoParams(TextInfo ti, ArrayList<SentenceWordItem> items) {
                this.mOriginalTextInfo = ti;
                this.mItems = items;
                this.mSize = items.size();
            }
        }

        public static class SentenceWordItem {
            public final int mLength;
            public final int mStart;
            public final TextInfo mTextInfo;

            public SentenceWordItem(TextInfo ti, int start, int end) {
                this.mTextInfo = ti;
                this.mStart = start;
                this.mLength = end - start;
            }
        }

        public SentenceLevelAdapter(Locale locale) {
            this.mWordIterator = new WordIterator(locale);
        }

        private SentenceTextInfoParams getSplitWords(TextInfo originalTextInfo) {
            WordIterator wordIterator = this.mWordIterator;
            CharSequence originalText = originalTextInfo.getText();
            int cookie = originalTextInfo.getCookie();
            int end = originalText.length();
            ArrayList<SentenceWordItem> wordItems = new ArrayList();
            wordIterator.setCharSequence(originalText, 0, originalText.length());
            int wordEnd = wordIterator.following(0);
            int wordEnd2 = wordEnd;
            int wordStart = wordIterator.getBeginning(wordEnd);
            while (wordStart <= end && wordEnd2 != -1 && wordStart != -1) {
                if (wordEnd2 >= 0 && wordEnd2 > wordStart) {
                    CharSequence query = originalText.subSequence(wordStart, wordEnd2);
                    wordItems.add(new SentenceWordItem(new TextInfo(query, 0, query.length(), cookie, query.hashCode()), wordStart, wordEnd2));
                }
                wordEnd2 = wordIterator.following(wordEnd2);
                if (wordEnd2 == -1) {
                    break;
                }
                wordStart = wordIterator.getBeginning(wordEnd2);
            }
            return new SentenceTextInfoParams(originalTextInfo, wordItems);
        }

        /* JADX WARNING: Missing block: B:24:0x005f, code skipped:
            return null;
     */
        public static android.view.textservice.SentenceSuggestionsInfo reconstructSuggestions(android.service.textservice.SpellCheckerService.SentenceLevelAdapter.SentenceTextInfoParams r13, android.view.textservice.SuggestionsInfo[] r14) {
            /*
            r0 = 0;
            if (r14 == 0) goto L_0x005f;
        L_0x0003:
            r1 = r14.length;
            if (r1 != 0) goto L_0x0007;
        L_0x0006:
            goto L_0x005f;
        L_0x0007:
            if (r13 != 0) goto L_0x000a;
        L_0x0009:
            return r0;
        L_0x000a:
            r0 = r13.mOriginalTextInfo;
            r0 = r0.getCookie();
            r1 = r13.mOriginalTextInfo;
            r1 = r1.getSequence();
            r2 = r13.mSize;
            r3 = new int[r2];
            r4 = new int[r2];
            r5 = new android.view.textservice.SuggestionsInfo[r2];
            r6 = 0;
        L_0x001f:
            if (r6 >= r2) goto L_0x0059;
        L_0x0021:
            r7 = r13.mItems;
            r7 = r7.get(r6);
            r7 = (android.service.textservice.SpellCheckerService.SentenceLevelAdapter.SentenceWordItem) r7;
            r8 = 0;
            r9 = 0;
        L_0x002b:
            r10 = r14.length;
            if (r9 >= r10) goto L_0x0046;
        L_0x002e:
            r10 = r14[r9];
            if (r10 == 0) goto L_0x0043;
        L_0x0032:
            r11 = r10.getSequence();
            r12 = r7.mTextInfo;
            r12 = r12.getSequence();
            if (r11 != r12) goto L_0x0043;
        L_0x003e:
            r8 = r10;
            r8.setCookieAndSequence(r0, r1);
            goto L_0x0046;
        L_0x0043:
            r9 = r9 + 1;
            goto L_0x002b;
        L_0x0046:
            r9 = r7.mStart;
            r3[r6] = r9;
            r9 = r7.mLength;
            r4[r6] = r9;
            if (r8 == 0) goto L_0x0052;
        L_0x0050:
            r9 = r8;
            goto L_0x0054;
        L_0x0052:
            r9 = EMPTY_SUGGESTIONS_INFO;
        L_0x0054:
            r5[r6] = r9;
            r6 = r6 + 1;
            goto L_0x001f;
        L_0x0059:
            r6 = new android.view.textservice.SentenceSuggestionsInfo;
            r6.<init>(r5, r3, r4);
            return r6;
        L_0x005f:
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.service.textservice.SpellCheckerService$SentenceLevelAdapter.reconstructSuggestions(android.service.textservice.SpellCheckerService$SentenceLevelAdapter$SentenceTextInfoParams, android.view.textservice.SuggestionsInfo[]):android.view.textservice.SentenceSuggestionsInfo");
        }
    }

    public static abstract class Session {
        private InternalISpellCheckerSession mInternalSession;
        private volatile SentenceLevelAdapter mSentenceLevelAdapter;

        public abstract void onCreate();

        public abstract SuggestionsInfo onGetSuggestions(TextInfo textInfo, int i);

        public final void setInternalISpellCheckerSession(InternalISpellCheckerSession session) {
            this.mInternalSession = session;
        }

        public SuggestionsInfo[] onGetSuggestionsMultiple(TextInfo[] textInfos, int suggestionsLimit, boolean sequentialWords) {
            int length = textInfos.length;
            SuggestionsInfo[] retval = new SuggestionsInfo[length];
            for (int i = 0; i < length; i++) {
                retval[i] = onGetSuggestions(textInfos[i], suggestionsLimit);
                retval[i].setCookieAndSequence(textInfos[i].getCookie(), textInfos[i].getSequence());
            }
            return retval;
        }

        public SentenceSuggestionsInfo[] onGetSentenceSuggestionsMultiple(TextInfo[] textInfos, int suggestionsLimit) {
            if (textInfos == null || textInfos.length == 0) {
                return SentenceLevelAdapter.EMPTY_SENTENCE_SUGGESTIONS_INFOS;
            }
            if (this.mSentenceLevelAdapter == null) {
                synchronized (this) {
                    if (this.mSentenceLevelAdapter == null) {
                        String localeStr = getLocale();
                        if (!TextUtils.isEmpty(localeStr)) {
                            this.mSentenceLevelAdapter = new SentenceLevelAdapter(new Locale(localeStr));
                        }
                    }
                }
            }
            if (this.mSentenceLevelAdapter == null) {
                return SentenceLevelAdapter.EMPTY_SENTENCE_SUGGESTIONS_INFOS;
            }
            int infosSize = textInfos.length;
            SentenceSuggestionsInfo[] retval = new SentenceSuggestionsInfo[infosSize];
            for (int i = 0; i < infosSize; i++) {
                SentenceTextInfoParams textInfoParams = this.mSentenceLevelAdapter.getSplitWords(textInfos[i]);
                ArrayList<SentenceWordItem> mItems = textInfoParams.mItems;
                int itemsSize = mItems.size();
                TextInfo[] splitTextInfos = new TextInfo[itemsSize];
                for (int j = 0; j < itemsSize; j++) {
                    splitTextInfos[j] = ((SentenceWordItem) mItems.get(j)).mTextInfo;
                }
                retval[i] = SentenceLevelAdapter.reconstructSuggestions(textInfoParams, onGetSuggestionsMultiple(splitTextInfos, suggestionsLimit, true));
            }
            return retval;
        }

        public void onCancel() {
        }

        public void onClose() {
        }

        public String getLocale() {
            return this.mInternalSession.getLocale();
        }

        public Bundle getBundle() {
            return this.mInternalSession.getBundle();
        }
    }

    private static class SpellCheckerServiceBinder extends ISpellCheckerService.Stub {
        private final WeakReference<SpellCheckerService> mInternalServiceRef;

        public SpellCheckerServiceBinder(SpellCheckerService service) {
            this.mInternalServiceRef = new WeakReference(service);
        }

        public void getISpellCheckerSession(String locale, ISpellCheckerSessionListener listener, Bundle bundle, ISpellCheckerServiceCallback callback) {
            InternalISpellCheckerSession internalSession;
            SpellCheckerService service = (SpellCheckerService) this.mInternalServiceRef.get();
            if (service == null) {
                internalSession = null;
            } else {
                Session session = service.createSession();
                InternalISpellCheckerSession internalSession2 = new InternalISpellCheckerSession(locale, listener, bundle, session);
                session.onCreate();
                internalSession = internalSession2;
            }
            try {
                callback.onSessionCreated(internalSession);
            } catch (RemoteException e) {
            }
        }
    }

    public abstract Session createSession();

    public final IBinder onBind(Intent intent) {
        return this.mBinder;
    }
}
