package android.media;

import android.net.wifi.WifiEnterpriseConfig;
import android.util.Log;

/* compiled from: WebVttRenderer */
class Tokenizer {
    private static final String TAG = "Tokenizer";
    private TokenizerPhase mDataTokenizer = new DataTokenizer();
    private int mHandledLen;
    private String mLine;
    private OnTokenListener mListener;
    private TokenizerPhase mPhase;
    private TokenizerPhase mTagTokenizer = new TagTokenizer();

    /* compiled from: WebVttRenderer */
    interface TokenizerPhase {
        TokenizerPhase start();

        void tokenize();
    }

    /* compiled from: WebVttRenderer */
    class DataTokenizer implements TokenizerPhase {
        private StringBuilder mData;

        DataTokenizer() {
        }

        public TokenizerPhase start() {
            this.mData = new StringBuilder();
            return this;
        }

        private boolean replaceEscape(String escape, String replacement, int pos) {
            if (!Tokenizer.this.mLine.startsWith(escape, pos)) {
                return false;
            }
            this.mData.append(Tokenizer.this.mLine.substring(Tokenizer.this.mHandledLen, pos));
            this.mData.append(replacement);
            Tokenizer.this.mHandledLen = escape.length() + pos;
            int pos2 = Tokenizer.this.mHandledLen - 1;
            return true;
        }

        public void tokenize() {
            int end = Tokenizer.this.mLine.length();
            int pos = Tokenizer.this.mHandledLen;
            while (pos < Tokenizer.this.mLine.length()) {
                if (Tokenizer.this.mLine.charAt(pos) == '&') {
                    if (!(replaceEscape("&amp;", "&", pos) || replaceEscape("&lt;", "<", pos) || replaceEscape("&gt;", ">", pos) || replaceEscape("&lrm;", "‎", pos) || replaceEscape("&rlm;", "‏", pos) || replaceEscape("&nbsp;", " ", pos))) {
                    }
                } else if (Tokenizer.this.mLine.charAt(pos) == '<') {
                    end = pos;
                    Tokenizer tokenizer = Tokenizer.this;
                    tokenizer.mPhase = tokenizer.mTagTokenizer.start();
                    break;
                }
                pos++;
            }
            this.mData.append(Tokenizer.this.mLine.substring(Tokenizer.this.mHandledLen, end));
            Tokenizer.this.mListener.onData(this.mData.toString());
            StringBuilder stringBuilder = this.mData;
            stringBuilder.delete(0, stringBuilder.length());
            Tokenizer.this.mHandledLen = end;
        }
    }

    /* compiled from: WebVttRenderer */
    interface OnTokenListener {
        void onData(String str);

        void onEnd(String str);

        void onLineEnd();

        void onStart(String str, String[] strArr, String str2);

        void onTimeStamp(long j);
    }

    /* compiled from: WebVttRenderer */
    class TagTokenizer implements TokenizerPhase {
        private String mAnnotation;
        private boolean mAtAnnotation;
        private String mName;

        TagTokenizer() {
        }

        public TokenizerPhase start() {
            String str = "";
            this.mAnnotation = str;
            this.mName = str;
            this.mAtAnnotation = false;
            return this;
        }

        public void tokenize() {
            if (!this.mAtAnnotation) {
                Tokenizer.this.mHandledLen = Tokenizer.this.mHandledLen + 1;
            }
            if (Tokenizer.this.mHandledLen < Tokenizer.this.mLine.length()) {
                String[] parts;
                if (this.mAtAnnotation || Tokenizer.this.mLine.charAt(Tokenizer.this.mHandledLen) == '/') {
                    parts = Tokenizer.this.mLine.substring(Tokenizer.this.mHandledLen).split(">");
                } else {
                    parts = Tokenizer.this.mLine.substring(Tokenizer.this.mHandledLen).split("[\t\f >]");
                }
                String part = Tokenizer.this.mLine.substring(Tokenizer.this.mHandledLen, Tokenizer.this.mHandledLen + parts[0].length());
                Tokenizer.access$112(Tokenizer.this, parts[0].length());
                if (this.mAtAnnotation) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(this.mAnnotation);
                    stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                    stringBuilder.append(part);
                    this.mAnnotation = stringBuilder.toString();
                } else {
                    this.mName = part;
                }
            }
            this.mAtAnnotation = true;
            if (Tokenizer.this.mHandledLen < Tokenizer.this.mLine.length() && Tokenizer.this.mLine.charAt(Tokenizer.this.mHandledLen) == '>') {
                yield_tag();
                Tokenizer tokenizer = Tokenizer.this;
                tokenizer.mPhase = tokenizer.mDataTokenizer.start();
                Tokenizer.this.mHandledLen = Tokenizer.this.mHandledLen + 1;
            }
        }

        private void yield_tag() {
            if (this.mName.startsWith("/")) {
                Tokenizer.this.mListener.onEnd(this.mName.substring(1));
            } else if (this.mName.length() <= 0 || !Character.isDigit(this.mName.charAt(0))) {
                String str = this.mAnnotation;
                String str2 = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
                this.mAnnotation = str.replaceAll("\\s+", str2);
                if (this.mAnnotation.startsWith(str2)) {
                    this.mAnnotation = this.mAnnotation.substring(1);
                }
                if (this.mAnnotation.endsWith(str2)) {
                    str = this.mAnnotation;
                    this.mAnnotation = str.substring(0, str.length() - 1);
                }
                String[] classes = null;
                int dotAt = this.mName.indexOf(46);
                if (dotAt >= 0) {
                    classes = this.mName.substring(dotAt + 1).split("\\.");
                    this.mName = this.mName.substring(0, dotAt);
                }
                Tokenizer.this.mListener.onStart(this.mName, classes, this.mAnnotation);
            } else {
                try {
                    Tokenizer.this.mListener.onTimeStamp(WebVttParser.parseTimestampMs(this.mName));
                } catch (NumberFormatException e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("invalid timestamp tag: <");
                    stringBuilder.append(this.mName);
                    stringBuilder.append(">");
                    Log.d(Tokenizer.TAG, stringBuilder.toString());
                }
            }
        }
    }

    static /* synthetic */ int access$112(Tokenizer x0, int x1) {
        int i = x0.mHandledLen + x1;
        x0.mHandledLen = i;
        return i;
    }

    Tokenizer(OnTokenListener listener) {
        reset();
        this.mListener = listener;
    }

    /* Access modifiers changed, original: 0000 */
    public void reset() {
        this.mPhase = this.mDataTokenizer.start();
    }

    /* Access modifiers changed, original: 0000 */
    public void tokenize(String s) {
        this.mHandledLen = 0;
        this.mLine = s;
        while (this.mHandledLen < this.mLine.length()) {
            this.mPhase.tokenize();
        }
        if (!(this.mPhase instanceof TagTokenizer)) {
            this.mListener.onLineEnd();
        }
    }
}
