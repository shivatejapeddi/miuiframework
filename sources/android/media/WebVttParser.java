package android.media;

import android.net.wifi.WifiEnterpriseConfig;
import android.provider.BrowserContract.Bookmarks;
import android.provider.Telephony.BaseMmsColumns;
import android.util.Log;
import com.android.internal.app.DumpHeapActivity;
import java.util.Vector;
import miui.maml.elements.MusicLyricParser;

/* compiled from: WebVttRenderer */
class WebVttParser {
    private static final String TAG = "WebVttParser";
    private String mBuffer = "";
    private TextTrackCue mCue;
    private Vector<String> mCueTexts;
    private WebVttCueListener mListener;
    private final Phase mParseCueId = new Phase() {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        static {
            Class cls = WebVttParser.class;
        }

        public void parse(String line) {
            if (line.length() != 0) {
                WebVttParser webVttParser;
                if (line.equals("NOTE") || line.startsWith("NOTE ")) {
                    webVttParser = WebVttParser.this;
                    webVttParser.mPhase = webVttParser.mParseCueText;
                }
                WebVttParser.this.mCue = new TextTrackCue();
                WebVttParser.this.mCueTexts.clear();
                webVttParser = WebVttParser.this;
                webVttParser.mPhase = webVttParser.mParseCueTime;
                if (line.contains("-->")) {
                    WebVttParser.this.mPhase.parse(line);
                } else {
                    WebVttParser.this.mCue.mId = line;
                }
            }
        }
    };
    private final Phase mParseCueText = new Phase() {
        public void parse(String line) {
            if (line.length() == 0) {
                WebVttParser.this.yieldCue();
                WebVttParser webVttParser = WebVttParser.this;
                webVttParser.mPhase = webVttParser.mParseCueId;
                return;
            }
            if (WebVttParser.this.mCue != null) {
                WebVttParser.this.mCueTexts.add(line);
            }
        }
    };
    private final Phase mParseCueTime = new Phase() {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        static {
            Class cls = WebVttParser.class;
        }

        public void parse(String line) {
            String str = line;
            int arrowAt = str.indexOf("-->");
            WebVttParser webVttParser;
            if (arrowAt < 0) {
                WebVttParser.this.mCue = null;
                webVttParser = WebVttParser.this;
                webVttParser.mPhase = webVttParser.mParseCueId;
                return;
            }
            String start;
            int i = 0;
            String start2 = str.substring(0, arrowAt).trim();
            String str2 = "";
            String rest = str.substring(arrowAt + 3).replaceFirst("^\\s+", str2).replaceFirst("\\s+", WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            int spaceAt = rest.indexOf(32);
            String end = spaceAt > 0 ? rest.substring(0, spaceAt) : rest;
            String rest2 = spaceAt > 0 ? rest.substring(spaceAt + 1) : str2;
            WebVttParser.this.mCue.mStartTimeMs = WebVttParser.parseTimestampMs(start2);
            WebVttParser.this.mCue.mEndTimeMs = WebVttParser.parseTimestampMs(end);
            String[] split = rest2.split(" +");
            int length = split.length;
            int i2 = 0;
            while (i2 < length) {
                int arrowAt2;
                int i3;
                String setting = split[i2];
                int colonAt = setting.indexOf(58);
                if (colonAt <= 0) {
                    arrowAt2 = arrowAt;
                    i3 = i;
                    start = start2;
                } else if (colonAt == setting.length() - 1) {
                    arrowAt2 = arrowAt;
                    i3 = i;
                    start = start2;
                } else {
                    String name = setting.substring(i, colonAt);
                    String value = setting.substring(colonAt + 1);
                    if (name.equals("region")) {
                        WebVttParser.this.mCue.mRegionId = value;
                        arrowAt2 = arrowAt;
                        start = start2;
                        i3 = 0;
                    } else {
                        str = "has invalid value";
                        arrowAt2 = arrowAt;
                        String str3 = "cue setting";
                        if (!name.equals("vertical")) {
                            start = start2;
                            start2 = "is not numeric or percentage";
                            if (name.equals("line")) {
                                try {
                                    if (value.endsWith("%")) {
                                        WebVttParser.this.mCue.mSnapToLines = false;
                                        WebVttParser.this.mCue.mLinePosition = Integer.valueOf(WebVttParser.parseIntPercentage(value));
                                    } else if (value.matches(".*[^0-9].*")) {
                                        WebVttParser.this.log_warning(str3, name, "contains an invalid character", value);
                                    } else {
                                        WebVttParser.this.mCue.mSnapToLines = true;
                                        WebVttParser.this.mCue.mLinePosition = Integer.valueOf(Integer.parseInt(value));
                                    }
                                } catch (NumberFormatException e) {
                                    WebVttParser.this.log_warning(str3, name, start2, value);
                                }
                                i3 = 0;
                            } else {
                                i3 = 0;
                                if (name.equals(Bookmarks.POSITION)) {
                                    try {
                                        WebVttParser.this.mCue.mTextPosition = WebVttParser.parseIntPercentage(value);
                                    } catch (NumberFormatException e2) {
                                        WebVttParser.this.log_warning(str3, name, start2, value);
                                    }
                                } else if (name.equals(DumpHeapActivity.KEY_SIZE)) {
                                    try {
                                        WebVttParser.this.mCue.mSize = WebVttParser.parseIntPercentage(value);
                                    } catch (NumberFormatException e3) {
                                        WebVttParser.this.log_warning(str3, name, start2, value);
                                    }
                                } else if (name.equals("align")) {
                                    if (value.equals(BaseMmsColumns.START)) {
                                        WebVttParser.this.mCue.mAlignment = 201;
                                    } else if (value.equals("middle")) {
                                        WebVttParser.this.mCue.mAlignment = 200;
                                    } else if (value.equals("end")) {
                                        WebVttParser.this.mCue.mAlignment = 202;
                                    } else if (value.equals("left")) {
                                        WebVttParser.this.mCue.mAlignment = 203;
                                    } else if (value.equals("right")) {
                                        WebVttParser.this.mCue.mAlignment = 204;
                                    } else {
                                        WebVttParser.this.log_warning(str3, name, str, value);
                                    }
                                }
                            }
                        } else if (value.equals("rl")) {
                            WebVttParser.this.mCue.mWritingDirection = 101;
                            start = start2;
                            i3 = 0;
                        } else if (value.equals("lr")) {
                            WebVttParser.this.mCue.mWritingDirection = 102;
                            start = start2;
                            i3 = 0;
                        } else {
                            WebVttParser.this.log_warning(str3, name, str, value);
                            start = start2;
                            i3 = 0;
                        }
                    }
                }
                i2++;
                str = line;
                i = i3;
                arrowAt = arrowAt2;
                start2 = start;
            }
            start = start2;
            if (!(WebVttParser.this.mCue.mLinePosition == null && WebVttParser.this.mCue.mSize == 100 && WebVttParser.this.mCue.mWritingDirection == 100)) {
                WebVttParser.this.mCue.mRegionId = str2;
            }
            webVttParser = WebVttParser.this;
            webVttParser.mPhase = webVttParser.mParseCueText;
        }
    };
    private final Phase mParseHeader = new Phase() {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        static {
            Class cls = WebVttParser.class;
        }

        /* Access modifiers changed, original: 0000 */
        public TextTrackRegion parseRegion(String s) {
            NumberFormatException e;
            TextTrackRegion region = new TextTrackRegion();
            String[] split = s.split(" +");
            int length = split.length;
            String name = null;
            int i = 0;
            while (i < length) {
                String str;
                String setting = split[i];
                int equalAt = setting.indexOf(61);
                if (equalAt <= 0) {
                    str = name;
                } else if (equalAt == setting.length() - 1) {
                    str = name;
                } else {
                    String name2 = setting.substring(name, equalAt);
                    String value = setting.substring(equalAt + 1);
                    if (name2.equals("id")) {
                        region.mId = value;
                        str = name;
                    } else if (name2.equals("width")) {
                        try {
                            region.mWidth = WebVttParser.parseFloatPercentage(value);
                            str = name;
                        } catch (NumberFormatException e2) {
                            WebVttParser.this.log_warning("region setting", name2, "has invalid value", e2.getMessage(), value);
                            str = null;
                        }
                    } else {
                        String value2 = value;
                        name = name2;
                        String str2 = "lines";
                        String str3 = "region setting";
                        if (name.equals(str2)) {
                            name2 = value2;
                            if (name2.matches(".*[^0-9].*")) {
                                WebVttParser.this.log_warning(str2, name, "contains an invalid character", name2);
                                str = null;
                            } else {
                                try {
                                    region.mLines = Integer.parseInt(name2);
                                } catch (NumberFormatException e3) {
                                    WebVttParser.this.log_warning(str3, name, "is not numeric", name2);
                                }
                                str = null;
                            }
                        } else {
                            name2 = value2;
                            if (name.equals("regionanchor") || name.equals("viewportanchor")) {
                                int commaAt = name2.indexOf(",");
                                if (commaAt < 0) {
                                    WebVttParser.this.log_warning(str3, name, "contains no comma", name2);
                                    str = null;
                                } else {
                                    value2 = name2.substring(0, commaAt);
                                    String anchorY = name2.substring(commaAt + 1);
                                    try {
                                        float x = WebVttParser.parseFloatPercentage(value2);
                                        try {
                                            float y = WebVttParser.parseFloatPercentage(anchorY);
                                            if (name.charAt(0) == 'r') {
                                                region.mAnchorPointX = x;
                                                region.mAnchorPointY = y;
                                            } else {
                                                region.mViewportAnchorPointX = x;
                                                region.mViewportAnchorPointY = y;
                                            }
                                            str = 0;
                                        } catch (NumberFormatException e22) {
                                            e22 = e22;
                                            str = null;
                                            WebVttParser.this.log_warning("region setting", name, "has invalid y component", e22.getMessage(), anchorY);
                                        }
                                    } catch (NumberFormatException e222) {
                                        int i2 = commaAt;
                                        String str4 = name2;
                                        str = null;
                                        str3 = "region setting";
                                        String str5 = "has invalid x component";
                                        String str6 = name;
                                        WebVttParser.this.log_warning(str3, str6, str5, e222.getMessage(), value2);
                                    }
                                }
                            } else if (!name.equals("scroll")) {
                                str = null;
                            } else if (name2.equals("up")) {
                                region.mScrollValue = 301;
                                str = null;
                            } else {
                                WebVttParser.this.log_warning(str3, name, "has invalid value", name2);
                                str = null;
                            }
                        }
                    }
                }
                i++;
                name = str;
            }
            return region;
        }

        public void parse(String line) {
            WebVttParser webVttParser;
            if (line.length() == 0) {
                webVttParser = WebVttParser.this;
                webVttParser.mPhase = webVttParser.mParseCueId;
            } else if (line.contains("-->")) {
                webVttParser = WebVttParser.this;
                webVttParser.mPhase = webVttParser.mParseCueTime;
                WebVttParser.this.mPhase.parse(line);
            } else {
                int colonAt = line.indexOf(58);
                if (colonAt <= 0 || colonAt >= line.length() - 1) {
                    WebVttParser.this.log_warning("meta data header has invalid format", line);
                }
                String name = line.substring(null, colonAt);
                String value = line.substring(colonAt + 1);
                if (name.equals("Region")) {
                    WebVttParser.this.mListener.onRegionParsed(parseRegion(value));
                }
            }
        }
    };
    private final Phase mParseStart = new Phase() {
        public void parse(String line) {
            if (line.startsWith("﻿")) {
                line = line.substring(1);
            }
            WebVttParser webVttParser;
            if (line.equals("WEBVTT") || line.startsWith("WEBVTT ") || line.startsWith("WEBVTT\t")) {
                webVttParser = WebVttParser.this;
                webVttParser.mPhase = webVttParser.mParseHeader;
                return;
            }
            WebVttParser.this.log_warning("Not a WEBVTT header", line);
            webVttParser = WebVttParser.this;
            webVttParser.mPhase = webVttParser.mSkipRest;
        }
    };
    private Phase mPhase = this.mParseStart;
    private final Phase mSkipRest = new Phase() {
        public void parse(String line) {
        }
    };

    /* compiled from: WebVttRenderer */
    interface Phase {
        void parse(String str);
    }

    WebVttParser(WebVttCueListener listener) {
        this.mListener = listener;
        this.mCueTexts = new Vector();
    }

    public static float parseFloatPercentage(String s) throws NumberFormatException {
        if (s.endsWith("%")) {
            s = s.substring(0, s.length() - 1);
            if (s.matches(".*[^0-9.].*")) {
                throw new NumberFormatException("contains an invalid character");
            }
            try {
                float value = Float.parseFloat(s);
                if (value >= 0.0f && value <= 100.0f) {
                    return value;
                }
                throw new NumberFormatException("is out of range");
            } catch (NumberFormatException e) {
                throw new NumberFormatException("is not a number");
            }
        }
        throw new NumberFormatException("does not end in %");
    }

    public static int parseIntPercentage(String s) throws NumberFormatException {
        if (s.endsWith("%")) {
            s = s.substring(0, s.length() - 1);
            if (s.matches(".*[^0-9].*")) {
                throw new NumberFormatException("contains an invalid character");
            }
            try {
                int value = Integer.parseInt(s);
                if (value >= 0 && value <= 100) {
                    return value;
                }
                throw new NumberFormatException("is out of range");
            } catch (NumberFormatException e) {
                throw new NumberFormatException("is not a number");
            }
        }
        throw new NumberFormatException("does not end in %");
    }

    public static long parseTimestampMs(String s) throws NumberFormatException {
        if (s.matches("(\\d+:)?[0-5]\\d:[0-5]\\d\\.\\d{3}")) {
            String[] parts = s.split("\\.", 2);
            long value = 0;
            int i = 0;
            String[] split = parts[0].split(":");
            while (i < split.length) {
                value = (60 * value) + Long.parseLong(split[i]);
                i++;
            }
            return (1000 * value) + Long.parseLong(parts[1]);
        }
        throw new NumberFormatException("has invalid format");
    }

    public static String timeToString(long timeMs) {
        return String.format("%d:%02d:%02d.%03d", new Object[]{Long.valueOf(timeMs / 3600000), Long.valueOf((timeMs / 60000) % 60), Long.valueOf((timeMs / 1000) % 60), Long.valueOf(timeMs % 1000)});
    }

    public void parse(String s) {
        boolean trailingCR = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mBuffer);
        stringBuilder.append(s.replace("\u0000", "�"));
        this.mBuffer = stringBuilder.toString().replace(MusicLyricParser.CRLF, "\n");
        String str = "\r";
        if (this.mBuffer.endsWith(str)) {
            trailingCR = true;
            String str2 = this.mBuffer;
            this.mBuffer = str2.substring(0, str2.length() - 1);
        }
        String[] lines = this.mBuffer.split("[\r\n]");
        for (int i = 0; i < lines.length - 1; i++) {
            this.mPhase.parse(lines[i]);
        }
        this.mBuffer = lines[lines.length - 1];
        if (trailingCR) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(this.mBuffer);
            stringBuilder2.append(str);
            this.mBuffer = stringBuilder2.toString();
        }
    }

    public void eos() {
        if (this.mBuffer.endsWith("\r")) {
            String str = this.mBuffer;
            this.mBuffer = str.substring(0, str.length() - 1);
        }
        this.mPhase.parse(this.mBuffer);
        this.mBuffer = "";
        yieldCue();
        this.mPhase = this.mParseStart;
    }

    public void yieldCue() {
        if (this.mCue != null && this.mCueTexts.size() > 0) {
            this.mCue.mStrings = new String[this.mCueTexts.size()];
            this.mCueTexts.toArray(this.mCue.mStrings);
            this.mCueTexts.clear();
            this.mListener.onCueParsed(this.mCue);
        }
        this.mCue = null;
    }

    private void log_warning(String nameType, String name, String message, String subMessage, String value) {
        String name2 = getClass().getName();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(nameType);
        stringBuilder.append(" '");
        stringBuilder.append(name);
        String str = "' ";
        stringBuilder.append(str);
        stringBuilder.append(message);
        stringBuilder.append(" ('");
        stringBuilder.append(value);
        stringBuilder.append(str);
        stringBuilder.append(subMessage);
        stringBuilder.append(")");
        Log.w(name2, stringBuilder.toString());
    }

    private void log_warning(String nameType, String name, String message, String value) {
        String name2 = getClass().getName();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(nameType);
        stringBuilder.append(" '");
        stringBuilder.append(name);
        stringBuilder.append("' ");
        stringBuilder.append(message);
        stringBuilder.append(" ('");
        stringBuilder.append(value);
        stringBuilder.append("')");
        Log.w(name2, stringBuilder.toString());
    }

    private void log_warning(String message, String value) {
        String name = getClass().getName();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(message);
        stringBuilder.append(" ('");
        stringBuilder.append(value);
        stringBuilder.append("')");
        Log.w(name, stringBuilder.toString());
    }
}
