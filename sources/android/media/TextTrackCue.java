package android.media;

import android.app.slice.Slice;
import android.media.SubtitleTrack.Cue;
import android.net.wifi.WifiEnterpriseConfig;
import android.provider.Telephony.BaseMmsColumns;
import java.util.Arrays;

/* compiled from: WebVttRenderer */
class TextTrackCue extends Cue {
    static final int ALIGNMENT_END = 202;
    static final int ALIGNMENT_LEFT = 203;
    static final int ALIGNMENT_MIDDLE = 200;
    static final int ALIGNMENT_RIGHT = 204;
    static final int ALIGNMENT_START = 201;
    private static final String TAG = "TTCue";
    static final int WRITING_DIRECTION_HORIZONTAL = 100;
    static final int WRITING_DIRECTION_VERTICAL_LR = 102;
    static final int WRITING_DIRECTION_VERTICAL_RL = 101;
    int mAlignment;
    boolean mAutoLinePosition;
    String mId;
    Integer mLinePosition;
    TextTrackCueSpan[][] mLines;
    boolean mPauseOnExit = false;
    TextTrackRegion mRegion;
    String mRegionId;
    int mSize;
    boolean mSnapToLines;
    String[] mStrings;
    int mTextPosition;
    int mWritingDirection = 100;

    TextTrackCue() {
        String str = "";
        this.mId = str;
        this.mRegionId = str;
        this.mSnapToLines = true;
        this.mLinePosition = null;
        this.mTextPosition = 50;
        this.mSize = 100;
        this.mAlignment = 200;
        this.mLines = null;
        this.mRegion = null;
    }

    public boolean equals(Object o) {
        if (!(o instanceof TextTrackCue)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        try {
            TextTrackCue cue = (TextTrackCue) o;
            boolean res = this.mId.equals(cue.mId) && this.mPauseOnExit == cue.mPauseOnExit && this.mWritingDirection == cue.mWritingDirection && this.mRegionId.equals(cue.mRegionId) && this.mSnapToLines == cue.mSnapToLines && this.mAutoLinePosition == cue.mAutoLinePosition && ((this.mAutoLinePosition || ((this.mLinePosition != null && this.mLinePosition.equals(cue.mLinePosition)) || (this.mLinePosition == null && cue.mLinePosition == null))) && this.mTextPosition == cue.mTextPosition && this.mSize == cue.mSize && this.mAlignment == cue.mAlignment && this.mLines.length == cue.mLines.length);
            if (res) {
                for (int line = 0; line < this.mLines.length; line++) {
                    if (!Arrays.equals(this.mLines[line], cue.mLines[line])) {
                        return false;
                    }
                }
            }
            return res;
        } catch (IncompatibleClassChangeError e) {
            return false;
        }
    }

    public StringBuilder appendStringsToBuilder(StringBuilder builder) {
        String str = "null";
        if (this.mStrings == null) {
            builder.append(str);
        } else {
            builder.append("[");
            boolean first = true;
            for (String s : this.mStrings) {
                if (!first) {
                    builder.append(", ");
                }
                if (s == null) {
                    builder.append(str);
                } else {
                    String str2 = "\"";
                    builder.append(str2);
                    builder.append(s);
                    builder.append(str2);
                }
                first = false;
            }
            builder.append("]");
        }
        return builder;
    }

    public StringBuilder appendLinesToBuilder(StringBuilder builder) {
        StringBuilder stringBuilder = builder;
        String str = "null";
        if (this.mLines == null) {
            stringBuilder.append(str);
        } else {
            stringBuilder.append("[");
            TextTrackCueSpan[][] textTrackCueSpanArr = this.mLines;
            int length = textTrackCueSpanArr.length;
            boolean first = true;
            int first2 = 0;
            while (first2 < length) {
                String str2;
                TextTrackCueSpan[][] textTrackCueSpanArr2;
                TextTrackCueSpan[] spans = textTrackCueSpanArr[first2];
                if (!first) {
                    stringBuilder.append(", ");
                }
                if (spans == null) {
                    stringBuilder.append(str);
                    str2 = str;
                    textTrackCueSpanArr2 = textTrackCueSpanArr;
                } else {
                    String str3 = "\"";
                    stringBuilder.append(str3);
                    int length2 = spans.length;
                    long lastTimestamp = -1;
                    boolean innerFirst = true;
                    int innerFirst2 = 0;
                    while (innerFirst2 < length2) {
                        TextTrackCueSpan span = spans[innerFirst2];
                        if (!innerFirst) {
                            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                        }
                        str2 = str;
                        textTrackCueSpanArr2 = textTrackCueSpanArr;
                        if (span.mTimestampMs != lastTimestamp) {
                            stringBuilder.append("<");
                            stringBuilder.append(WebVttParser.timeToString(span.mTimestampMs));
                            stringBuilder.append(">");
                            lastTimestamp = span.mTimestampMs;
                        }
                        stringBuilder.append(span.mText);
                        innerFirst = false;
                        innerFirst2++;
                        str = str2;
                        textTrackCueSpanArr = textTrackCueSpanArr2;
                    }
                    str2 = str;
                    textTrackCueSpanArr2 = textTrackCueSpanArr;
                    stringBuilder.append(str3);
                }
                first = false;
                first2++;
                str = str2;
                textTrackCueSpanArr = textTrackCueSpanArr2;
            }
            stringBuilder.append("]");
        }
        return stringBuilder;
    }

    public String toString() {
        String str;
        Object obj;
        StringBuilder res = new StringBuilder();
        res.append(WebVttParser.timeToString(this.mStartTimeMs));
        res.append(" --> ");
        res.append(WebVttParser.timeToString(this.mEndTimeMs));
        res.append(" {id:\"");
        res.append(this.mId);
        res.append("\", pauseOnExit:");
        res.append(this.mPauseOnExit);
        res.append(", direction:");
        int i = this.mWritingDirection;
        String str2 = "INVALID";
        if (i == 100) {
            str = Slice.HINT_HORIZONTAL;
        } else if (i == 102) {
            str = "vertical_lr";
        } else if (i == 101) {
            str = "vertical_rl";
        } else {
            str = str2;
        }
        res.append(str);
        res.append(", regionId:\"");
        res.append(this.mRegionId);
        res.append("\", snapToLines:");
        res.append(this.mSnapToLines);
        res.append(", linePosition:");
        if (this.mAutoLinePosition) {
            obj = "auto";
        } else {
            obj = this.mLinePosition;
        }
        res.append(obj);
        res.append(", textPosition:");
        res.append(this.mTextPosition);
        res.append(", size:");
        res.append(this.mSize);
        res.append(", alignment:");
        i = this.mAlignment;
        if (i == 202) {
            str2 = "end";
        } else if (i == 203) {
            str2 = "left";
        } else if (i == 200) {
            str2 = "middle";
        } else if (i == 204) {
            str2 = "right";
        } else if (i == 201) {
            str2 = BaseMmsColumns.START;
        }
        res.append(str2);
        res.append(", text:");
        appendStringsToBuilder(res).append("}");
        return res.toString();
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public void onTime(long timeMs) {
        for (TextTrackCueSpan[] line : this.mLines) {
            for (TextTrackCueSpan span : r0[r3]) {
                span.mEnabled = timeMs >= span.mTimestampMs;
            }
        }
    }
}
