package android.widget;

import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.method.WordIterator;
import android.text.style.SpellCheckSpan;
import android.text.style.SuggestionSpan;
import android.util.Log;
import android.util.LruCache;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SpellCheckerSession.SpellCheckerSessionListener;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import java.util.Locale;

public class SpellChecker implements SpellCheckerSessionListener {
    public static final int AVERAGE_WORD_LENGTH = 7;
    private static final boolean DBG = false;
    public static final int MAX_NUMBER_OF_WORDS = 50;
    private static final int MIN_SENTENCE_LENGTH = 50;
    private static final int SPELL_PAUSE_DURATION = 400;
    private static final int SUGGESTION_SPAN_CACHE_SIZE = 10;
    private static final String TAG = SpellChecker.class.getSimpleName();
    private static final int USE_SPAN_RANGE = -1;
    public static final int WORD_ITERATOR_INTERVAL = 350;
    final int mCookie;
    private Locale mCurrentLocale;
    private int[] mIds;
    private boolean mIsSentenceSpellCheckSupported;
    private int mLength;
    private int mSpanSequenceCounter = 0;
    private SpellCheckSpan[] mSpellCheckSpans;
    SpellCheckerSession mSpellCheckerSession;
    private SpellParser[] mSpellParsers = new SpellParser[0];
    private Runnable mSpellRunnable;
    private final LruCache<Long, SuggestionSpan> mSuggestionSpanCache = new LruCache(10);
    private TextServicesManager mTextServicesManager;
    private final TextView mTextView;
    private WordIterator mWordIterator;

    private class SpellParser {
        private Object mRange;

        private SpellParser() {
            this.mRange = new Object();
        }

        /* synthetic */ SpellParser(SpellChecker x0, AnonymousClass1 x1) {
            this();
        }

        public void parse(int start, int end) {
            int parseEnd;
            int max = SpellChecker.this.mTextView.length();
            if (end > max) {
                String access$300 = SpellChecker.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Parse invalid region, from ");
                stringBuilder.append(start);
                stringBuilder.append(" to ");
                stringBuilder.append(end);
                Log.w(access$300, stringBuilder.toString());
                parseEnd = max;
            } else {
                parseEnd = end;
            }
            if (parseEnd > start) {
                setRangeSpan((Editable) SpellChecker.this.mTextView.getText(), start, parseEnd);
                parse();
            }
        }

        public boolean isFinished() {
            return ((Editable) SpellChecker.this.mTextView.getText()).getSpanStart(this.mRange) < 0;
        }

        public void stop() {
            removeRangeSpan((Editable) SpellChecker.this.mTextView.getText());
        }

        private void setRangeSpan(Editable editable, int start, int end) {
            editable.setSpan(this.mRange, start, end, 33);
        }

        private void removeRangeSpan(Editable editable) {
            editable.removeSpan(this.mRange);
        }

        public void parse() {
            int start;
            int wordEnd;
            Editable editable = (Editable) SpellChecker.this.mTextView.getText();
            if (SpellChecker.this.mIsSentenceSpellCheckSupported) {
                start = Math.max(0, editable.getSpanStart(this.mRange) - 50);
            } else {
                start = editable.getSpanStart(this.mRange);
            }
            int end = editable.getSpanEnd(this.mRange);
            int wordIteratorWindowEnd = Math.min(end, start + 350);
            SpellChecker.this.mWordIterator.setCharSequence(editable, start, wordIteratorWindowEnd);
            int wordStart = SpellChecker.this.mWordIterator.preceding(start);
            if (wordStart == -1) {
                wordEnd = SpellChecker.this.mWordIterator.following(start);
                if (wordEnd != -1) {
                    wordStart = SpellChecker.this.mWordIterator.getBeginning(wordEnd);
                }
            } else {
                wordEnd = SpellChecker.this.mWordIterator.getEnd(wordStart);
            }
            if (wordEnd == -1) {
                removeRangeSpan(editable);
                return;
            }
            int spellCheckStart;
            SpellCheckSpan[] spellCheckSpans = (SpellCheckSpan[]) editable.getSpans(start - 1, end + 1, SpellCheckSpan.class);
            SuggestionSpan[] suggestionSpans = (SuggestionSpan[]) editable.getSpans(start - 1, end + 1, SuggestionSpan.class);
            int wordCount = 0;
            boolean scheduleOtherSpellCheck = false;
            int spellCheckEnd;
            int wordEnd2;
            if (SpellChecker.this.mIsSentenceSpellCheckSupported) {
                if (wordIteratorWindowEnd < end) {
                    scheduleOtherSpellCheck = true;
                }
                int spellCheckEnd2 = SpellChecker.this.mWordIterator.preceding(wordIteratorWindowEnd);
                boolean correct = spellCheckEnd2 != -1;
                if (correct) {
                    spellCheckEnd2 = SpellChecker.this.mWordIterator.getEnd(spellCheckEnd2);
                    correct = spellCheckEnd2 != -1;
                }
                if (correct) {
                    spellCheckStart = wordStart;
                    boolean createSpellCheckSpan = true;
                    spellCheckEnd = spellCheckEnd2;
                    spellCheckEnd2 = 0;
                    while (true) {
                        int wordIteratorWindowEnd2 = wordIteratorWindowEnd;
                        if (spellCheckEnd2 >= SpellChecker.this.mLength) {
                            wordEnd2 = wordEnd;
                            break;
                        }
                        wordIteratorWindowEnd = SpellChecker.this.mSpellCheckSpans[spellCheckEnd2];
                        int wordStart2 = wordStart;
                        if (SpellChecker.this.mIds[spellCheckEnd2] >= 0) {
                            if (wordIteratorWindowEnd.isSpellCheckInProgress() == 0) {
                                wordStart = editable.getSpanStart(wordIteratorWindowEnd);
                                wordEnd2 = wordEnd;
                                wordEnd = editable.getSpanEnd(wordIteratorWindowEnd);
                                if (wordEnd >= spellCheckStart && spellCheckEnd >= wordStart) {
                                    if (wordStart <= spellCheckStart && spellCheckEnd <= wordEnd) {
                                        createSpellCheckSpan = false;
                                        break;
                                    }
                                    editable.removeSpan(wordIteratorWindowEnd);
                                    spellCheckStart = Math.min(wordStart, spellCheckStart);
                                    spellCheckEnd = Math.max(wordEnd, spellCheckEnd);
                                }
                            } else {
                                wordEnd2 = wordEnd;
                            }
                        } else {
                            wordEnd2 = wordEnd;
                        }
                        spellCheckEnd2++;
                        wordIteratorWindowEnd = wordIteratorWindowEnd2;
                        wordStart = wordStart2;
                        wordEnd = wordEnd2;
                    }
                    if (spellCheckEnd >= start) {
                        if (spellCheckEnd <= spellCheckStart) {
                            String access$300 = SpellChecker.TAG;
                            wordIteratorWindowEnd = new StringBuilder();
                            wordIteratorWindowEnd.append("Trying to spellcheck invalid region, from ");
                            wordIteratorWindowEnd.append(start);
                            wordIteratorWindowEnd.append(" to ");
                            wordIteratorWindowEnd.append(end);
                            Log.w(access$300, wordIteratorWindowEnd.toString());
                        } else if (createSpellCheckSpan) {
                            SpellChecker.this.addSpellCheckSpan(editable, spellCheckStart, spellCheckEnd);
                        }
                    }
                    spellCheckStart = spellCheckEnd;
                    wordEnd = wordEnd2;
                } else {
                    removeRangeSpan(editable);
                    return;
                }
            }
            wordEnd2 = wordEnd;
            spellCheckStart = wordStart;
            while (spellCheckStart <= end) {
                if (wordEnd >= start && wordEnd > spellCheckStart) {
                    if (wordCount >= 50) {
                        scheduleOtherSpellCheck = true;
                        break;
                    }
                    if (spellCheckStart < start && wordEnd > start) {
                        removeSpansAt(editable, start, spellCheckSpans);
                        removeSpansAt(editable, start, suggestionSpans);
                    }
                    if (spellCheckStart < end && wordEnd > end) {
                        removeSpansAt(editable, end, spellCheckSpans);
                        removeSpansAt(editable, end, suggestionSpans);
                    }
                    boolean createSpellCheckSpan2 = true;
                    if (wordEnd == start) {
                        for (int spanEnd : spellCheckSpans) {
                            if (editable.getSpanEnd(spanEnd) == start) {
                                createSpellCheckSpan2 = false;
                                break;
                            }
                        }
                    }
                    if (spellCheckStart == end) {
                        for (int spanEnd2 : spellCheckSpans) {
                            if (editable.getSpanStart(spanEnd2) == end) {
                                createSpellCheckSpan2 = false;
                                break;
                            }
                        }
                    }
                    if (createSpellCheckSpan2) {
                        SpellChecker.this.addSpellCheckSpan(editable, spellCheckStart, wordEnd);
                    }
                    wordCount++;
                }
                wordStart = wordEnd;
                spellCheckEnd = SpellChecker.this.mWordIterator.following(wordEnd);
                if (wordIteratorWindowEnd >= end || (spellCheckEnd != -1 && spellCheckEnd < wordIteratorWindowEnd)) {
                    wordEnd = spellCheckEnd;
                } else {
                    wordIteratorWindowEnd = Math.min(end, wordStart + 350);
                    SpellChecker.this.mWordIterator.setCharSequence(editable, wordStart, wordIteratorWindowEnd);
                    wordEnd = SpellChecker.this.mWordIterator.following(wordStart);
                }
                if (wordEnd == -1) {
                    break;
                }
                spellCheckStart = SpellChecker.this.mWordIterator.getBeginning(wordEnd);
                if (spellCheckStart == -1) {
                    break;
                }
            }
            if (!scheduleOtherSpellCheck || spellCheckStart == -1 || spellCheckStart > end) {
                removeRangeSpan(editable);
            } else {
                setRangeSpan(editable, spellCheckStart, end);
            }
            SpellChecker.this.spellCheck();
        }

        private <T> void removeSpansAt(Editable editable, int offset, T[] spans) {
            for (T span : spans) {
                if (editable.getSpanStart(span) <= offset && editable.getSpanEnd(span) >= offset) {
                    editable.removeSpan(span);
                }
            }
        }
    }

    public SpellChecker(TextView textView) {
        this.mTextView = textView;
        this.mIds = ArrayUtils.newUnpaddedIntArray(1);
        this.mSpellCheckSpans = new SpellCheckSpan[this.mIds.length];
        setLocale(this.mTextView.getSpellCheckerLocale());
        this.mCookie = hashCode();
    }

    /* Access modifiers changed, original: 0000 */
    public void resetSession() {
        closeSession();
        this.mTextServicesManager = this.mTextView.getTextServicesManagerForUser();
        if (this.mCurrentLocale == null || this.mTextServicesManager == null || this.mTextView.length() == 0 || !this.mTextServicesManager.isSpellCheckerEnabled() || this.mTextServicesManager.getCurrentSpellCheckerSubtype(true) == null) {
            this.mSpellCheckerSession = null;
        } else {
            this.mSpellCheckerSession = this.mTextServicesManager.newSpellCheckerSession(null, this.mCurrentLocale, this, false);
            this.mIsSentenceSpellCheckSupported = true;
        }
        for (int i = 0; i < this.mLength; i++) {
            this.mIds[i] = -1;
        }
        this.mLength = 0;
        TextView textView = this.mTextView;
        textView.removeMisspelledSpans((Editable) textView.getText());
        this.mSuggestionSpanCache.evictAll();
    }

    private void setLocale(Locale locale) {
        this.mCurrentLocale = locale;
        resetSession();
        if (locale != null) {
            this.mWordIterator = new WordIterator(locale);
        }
        this.mTextView.onLocaleChanged();
    }

    private boolean isSessionActive() {
        return this.mSpellCheckerSession != null;
    }

    public void closeSession() {
        SpellCheckerSession spellCheckerSession = this.mSpellCheckerSession;
        if (spellCheckerSession != null) {
            spellCheckerSession.close();
        }
        for (SpellParser stop : this.mSpellParsers) {
            stop.stop();
        }
        Runnable runnable = this.mSpellRunnable;
        if (runnable != null) {
            this.mTextView.removeCallbacks(runnable);
        }
    }

    private int nextSpellCheckSpanIndex() {
        int i = 0;
        while (true) {
            int i2 = this.mLength;
            if (i >= i2) {
                this.mIds = GrowingArrayUtils.append(this.mIds, i2, 0);
                this.mSpellCheckSpans = (SpellCheckSpan[]) GrowingArrayUtils.append(this.mSpellCheckSpans, this.mLength, new SpellCheckSpan());
                this.mLength++;
                return this.mLength - 1;
            } else if (this.mIds[i] < 0) {
                return i;
            } else {
                i++;
            }
        }
    }

    private void addSpellCheckSpan(Editable editable, int start, int end) {
        int index = nextSpellCheckSpanIndex();
        SpellCheckSpan spellCheckSpan = this.mSpellCheckSpans[index];
        editable.setSpan(spellCheckSpan, start, end, 33);
        spellCheckSpan.setSpellCheckInProgress(false);
        int[] iArr = this.mIds;
        int i = this.mSpanSequenceCounter;
        this.mSpanSequenceCounter = i + 1;
        iArr[index] = i;
    }

    public void onSpellCheckSpanRemoved(SpellCheckSpan spellCheckSpan) {
        for (int i = 0; i < this.mLength; i++) {
            if (this.mSpellCheckSpans[i] == spellCheckSpan) {
                this.mIds[i] = -1;
                return;
            }
        }
    }

    public void onSelectionChanged() {
        spellCheck();
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x003b A:{RETURN} */
    public void spellCheck(int r8, int r9) {
        /*
        r7 = this;
        r0 = r7.mTextView;
        r0 = r0.getSpellCheckerLocale();
        r1 = r7.isSessionActive();
        r2 = 0;
        if (r0 == 0) goto L_0x002b;
    L_0x000d:
        r3 = r7.mCurrentLocale;
        if (r3 == 0) goto L_0x002b;
    L_0x0011:
        r3 = r3.equals(r0);
        if (r3 != 0) goto L_0x0018;
    L_0x0017:
        goto L_0x002b;
    L_0x0018:
        r3 = r7.mTextServicesManager;
        if (r3 == 0) goto L_0x0024;
    L_0x001c:
        r3 = r3.isSpellCheckerEnabled();
        if (r3 == 0) goto L_0x0024;
    L_0x0022:
        r3 = 1;
        goto L_0x0025;
    L_0x0024:
        r3 = r2;
    L_0x0025:
        if (r1 == r3) goto L_0x0039;
    L_0x0027:
        r7.resetSession();
        goto L_0x0039;
    L_0x002b:
        r7.setLocale(r0);
        r8 = 0;
        r3 = r7.mTextView;
        r3 = r3.getText();
        r9 = r3.length();
    L_0x0039:
        if (r1 != 0) goto L_0x003c;
    L_0x003b:
        return;
    L_0x003c:
        r3 = r7.mSpellParsers;
        r3 = r3.length;
        r4 = 0;
    L_0x0040:
        if (r4 >= r3) goto L_0x0053;
    L_0x0042:
        r5 = r7.mSpellParsers;
        r5 = r5[r4];
        r6 = r5.isFinished();
        if (r6 == 0) goto L_0x0050;
    L_0x004c:
        r5.parse(r8, r9);
        return;
    L_0x0050:
        r4 = r4 + 1;
        goto L_0x0040;
    L_0x0053:
        r4 = r3 + 1;
        r4 = new android.widget.SpellChecker.SpellParser[r4];
        r5 = r7.mSpellParsers;
        java.lang.System.arraycopy(r5, r2, r4, r2, r3);
        r7.mSpellParsers = r4;
        r2 = new android.widget.SpellChecker$SpellParser;
        r5 = 0;
        r2.<init>(r7, r5);
        r5 = r7.mSpellParsers;
        r5[r3] = r2;
        r2.parse(r8, r9);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.SpellChecker.spellCheck(int, int):void");
    }

    private void spellCheck() {
        if (this.mSpellCheckerSession != null) {
            Editable editable = (Editable) this.mTextView.getText();
            int selectionStart = Selection.getSelectionStart(editable);
            int selectionEnd = Selection.getSelectionEnd(editable);
            TextInfo[] textInfos = new TextInfo[this.mLength];
            int textInfosCount = 0;
            int i = 0;
            while (true) {
                boolean z = false;
                if (i >= this.mLength) {
                    break;
                }
                SpellCheckSpan spellCheckSpan = this.mSpellCheckSpans[i];
                if (this.mIds[i] >= 0 && !spellCheckSpan.isSpellCheckInProgress()) {
                    int start = editable.getSpanStart(spellCheckSpan);
                    int end = editable.getSpanEnd(spellCheckSpan);
                    boolean isEditing;
                    if (selectionStart == end + 1 && WordIterator.isMidWordPunctuation(this.mCurrentLocale, Character.codePointBefore(editable, end + 1))) {
                        isEditing = false;
                    } else if (this.mIsSentenceSpellCheckSupported) {
                        if (selectionEnd <= start || selectionStart > end) {
                            z = true;
                        }
                        isEditing = z;
                    } else {
                        if (selectionEnd < start || selectionStart > end) {
                            z = true;
                        }
                        isEditing = z;
                    }
                    if (start >= 0 && end > start && isEditing) {
                        spellCheckSpan.setSpellCheckInProgress(true);
                        TextInfo textInfo = new TextInfo(editable, start, end, this.mCookie, this.mIds[i]);
                        int textInfosCount2 = textInfosCount + 1;
                        textInfos[textInfosCount] = textInfo;
                        textInfosCount = textInfosCount2;
                    }
                }
                i++;
            }
            if (textInfosCount > 0) {
                if (textInfosCount < textInfos.length) {
                    TextInfo[] textInfosCopy = new TextInfo[textInfosCount];
                    System.arraycopy(textInfos, 0, textInfosCopy, 0, textInfosCount);
                    textInfos = textInfosCopy;
                }
                if (this.mIsSentenceSpellCheckSupported) {
                    this.mSpellCheckerSession.getSentenceSuggestions(textInfos, 5);
                } else {
                    this.mSpellCheckerSession.getSuggestions(textInfos, 5, false);
                }
            }
        }
    }

    private SpellCheckSpan onGetSuggestionsInternal(SuggestionsInfo suggestionsInfo, int offset, int length) {
        int i = offset;
        int i2 = length;
        if (suggestionsInfo == null || suggestionsInfo.getCookie() != this.mCookie) {
            return null;
        }
        Editable editable = (Editable) this.mTextView.getText();
        int sequenceNumber = suggestionsInfo.getSequence();
        for (int k = 0; k < this.mLength; k++) {
            if (sequenceNumber == this.mIds[k]) {
                int attributes = suggestionsInfo.getSuggestionsAttributes();
                boolean z = false;
                boolean isInDictionary = (attributes & 1) > 0;
                if ((attributes & 2) > 0) {
                    z = true;
                }
                boolean looksLikeTypo = z;
                SpellCheckSpan spellCheckSpan = this.mSpellCheckSpans[k];
                if (!isInDictionary && looksLikeTypo) {
                    createMisspelledSuggestionSpan(editable, suggestionsInfo, spellCheckSpan, offset, length);
                } else if (this.mIsSentenceSpellCheckSupported) {
                    int start;
                    int end;
                    int spellCheckSpanStart = editable.getSpanStart(spellCheckSpan);
                    int spellCheckSpanEnd = editable.getSpanEnd(spellCheckSpan);
                    if (i == -1 || i2 == -1) {
                        start = spellCheckSpanStart;
                        end = spellCheckSpanEnd;
                    } else {
                        start = spellCheckSpanStart + i;
                        end = start + i2;
                    }
                    if (spellCheckSpanStart < 0 || spellCheckSpanEnd <= spellCheckSpanStart || end <= start) {
                    } else {
                        Long key = Long.valueOf(TextUtils.packRangeInLong(start, end));
                        SuggestionSpan tempSuggestionSpan = (SuggestionSpan) this.mSuggestionSpanCache.get(key);
                        if (tempSuggestionSpan != null) {
                            editable.removeSpan(tempSuggestionSpan);
                            this.mSuggestionSpanCache.remove(key);
                        }
                    }
                }
                return spellCheckSpan;
            }
        }
        return null;
    }

    public void onGetSuggestions(SuggestionsInfo[] results) {
        Editable editable = (Editable) this.mTextView.getText();
        for (SpellCheckSpan spellCheckSpan : results) {
            SpellCheckSpan spellCheckSpan2 = onGetSuggestionsInternal(spellCheckSpan2, -1, -1);
            if (spellCheckSpan2 != null) {
                editable.removeSpan(spellCheckSpan2);
            }
        }
        scheduleNewSpellCheck();
    }

    public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] results) {
        Editable editable = (Editable) this.mTextView.getText();
        for (SentenceSuggestionsInfo ssi : results) {
            if (ssi != null) {
                SpellCheckSpan spellCheckSpan = null;
                for (int j = 0; j < ssi.getSuggestionsCount(); j++) {
                    SuggestionsInfo suggestionsInfo = ssi.getSuggestionsInfoAt(j);
                    if (suggestionsInfo != null) {
                        SpellCheckSpan scs = onGetSuggestionsInternal(suggestionsInfo, ssi.getOffsetAt(j), ssi.getLengthAt(j));
                        if (spellCheckSpan == null && scs != null) {
                            spellCheckSpan = scs;
                        }
                    }
                }
                if (spellCheckSpan != null) {
                    editable.removeSpan(spellCheckSpan);
                }
            }
        }
        scheduleNewSpellCheck();
    }

    private void scheduleNewSpellCheck() {
        Runnable runnable = this.mSpellRunnable;
        if (runnable == null) {
            this.mSpellRunnable = new Runnable() {
                public void run() {
                    int length = SpellChecker.this.mSpellParsers.length;
                    int i = 0;
                    while (i < length) {
                        SpellParser spellParser = SpellChecker.this.mSpellParsers[i];
                        if (spellParser.isFinished()) {
                            i++;
                        } else {
                            spellParser.parse();
                            return;
                        }
                    }
                }
            };
        } else {
            this.mTextView.removeCallbacks(runnable);
        }
        this.mTextView.postDelayed(this.mSpellRunnable, 400);
    }

    private void createMisspelledSuggestionSpan(Editable editable, SuggestionsInfo suggestionsInfo, SpellCheckSpan spellCheckSpan, int offset, int length) {
        int spellCheckSpanStart = editable.getSpanStart(spellCheckSpan);
        int spellCheckSpanEnd = editable.getSpanEnd(spellCheckSpan);
        if (spellCheckSpanStart >= 0 && spellCheckSpanEnd > spellCheckSpanStart) {
            int start;
            int end;
            String[] suggestions;
            if (offset == -1 || length == -1) {
                start = spellCheckSpanStart;
                end = spellCheckSpanEnd;
            } else {
                start = spellCheckSpanStart + offset;
                end = start + length;
            }
            int suggestionsCount = suggestionsInfo.getSuggestionsCount();
            if (suggestionsCount > 0) {
                suggestions = new String[suggestionsCount];
                for (int i = 0; i < suggestionsCount; i++) {
                    suggestions[i] = suggestionsInfo.getSuggestionAt(i);
                }
            } else {
                suggestions = (String[]) ArrayUtils.emptyArray(String.class);
            }
            SuggestionSpan suggestionSpan = new SuggestionSpan(this.mTextView.getContext(), suggestions, 3);
            if (this.mIsSentenceSpellCheckSupported) {
                Long key = Long.valueOf(TextUtils.packRangeInLong(start, end));
                SuggestionSpan tempSuggestionSpan = (SuggestionSpan) this.mSuggestionSpanCache.get(key);
                if (tempSuggestionSpan != null) {
                    editable.removeSpan(tempSuggestionSpan);
                }
                this.mSuggestionSpanCache.put(key, suggestionSpan);
            }
            editable.setSpan(suggestionSpan, start, end, 33);
            this.mTextView.invalidateRegion(start, end, false);
        }
    }

    public static boolean haveWordBoundariesChanged(Editable editable, int start, int end, int spanStart, int spanEnd) {
        if (spanEnd != start && spanStart != end) {
            return true;
        }
        if (spanEnd == start && start < editable.length()) {
            return Character.isLetterOrDigit(Character.codePointAt(editable, start));
        }
        if (spanStart != end || end <= 0) {
            return false;
        }
        return Character.isLetterOrDigit(Character.codePointBefore(editable, end));
    }
}
