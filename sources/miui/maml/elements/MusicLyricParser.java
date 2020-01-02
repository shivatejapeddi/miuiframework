package miui.maml.elements;

import android.text.Html;
import android.text.TextUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.regex.Pattern;

public class MusicLyricParser {
    public static final String CRLF = "\r\n";
    private static final int INTERVAL_OF_LAST = 8000;
    private static final int LINE_PARSE_IGNORE = 1;
    private static final int LINE_PARSE_REGULAR = 2;
    private static final int LINE_PARSE_STOP = 0;
    public static final int MAX_VALID_TIME = 18000000;
    private static final String TAG_ALBUM = "al";
    private static final String TAG_ARTIST = "ar";
    private static final String TAG_EDITOR = "by";
    private static final Pattern TAG_EXTRA_LRC = Pattern.compile("<[0-9]{0,2}:[0-9]{0,2}:[0-9]{0,2}>");
    private static final String TAG_OFFSET = "offset";
    private static final String TAG_TITLE = "ti";
    private static final String TAG_VERSION = "ve";

    static class EntityCompator implements Comparator<LyricEntity> {
        EntityCompator() {
        }

        public int compare(LyricEntity obj1, LyricEntity obj2) {
            return obj1.time - obj2.time;
        }
    }

    public static class Lyric {
        private final LyricEntity EMPTY_AFTER;
        private final LyricEntity EMPTY_BEFORE;
        private final ArrayList<LyricEntity> mEntityList;
        private final LyricHeader mHeader;
        private boolean mIsModified;
        private LyricLocator mLyricLocator = new LyricLocator();
        private final long mOpenTime = System.currentTimeMillis();
        private int mOriginHeaderOffset;

        class LyricLine {
            CharSequence lyric;
            int pos;

            LyricLine() {
            }
        }

        class LyricLocator {
            final int CRLF_LENGTH = MusicLyricParser.CRLF.length();
            String mFullLyric;
            ArrayList<LyricLine> mLyricLines;
            int[] mTimeArr;

            LyricLocator() {
            }

            /* Access modifiers changed, original: 0000 */
            public void set(int[] time, ArrayList<CharSequence> lyric) {
                this.mTimeArr = time;
                inflateLyricLines(lyric);
            }

            private void inflateLyricLines(ArrayList<CharSequence> lyricArr) {
                int[] iArr = this.mTimeArr;
                if (iArr == null || lyricArr == null || iArr.length != lyricArr.size()) {
                    this.mTimeArr = null;
                    this.mLyricLines = null;
                    return;
                }
                this.mLyricLines = new ArrayList();
                int i = 0;
                while (i < this.mTimeArr.length) {
                    CharSequence lyric = (CharSequence) lyricArr.get(i);
                    LyricLine line = new LyricLine();
                    line.lyric = lyric;
                    LyricLine last = i > 0 ? (LyricLine) this.mLyricLines.get(i - 1) : null;
                    line.pos = last != null ? (last.pos + last.lyric.length()) + this.CRLF_LENGTH : 0;
                    this.mLyricLines.add(line);
                    i++;
                }
                this.mFullLyric = "";
                for (i = 0; i < this.mLyricLines.size(); i++) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(this.mFullLyric);
                    stringBuilder.append(((LyricLine) this.mLyricLines.get(i)).lyric);
                    stringBuilder.append(MusicLyricParser.CRLF);
                    this.mFullLyric = stringBuilder.toString();
                }
            }

            /* Access modifiers changed, original: 0000 */
            public String getLine(long position) {
                if (this.mTimeArr == null) {
                    return null;
                }
                int num = getLineNumber(position);
                if (num == -1) {
                    return null;
                }
                LyricLine line = (LyricLine) this.mLyricLines.get(num);
                return this.mFullLyric.substring(line.pos, line.pos + line.lyric.length());
            }

            private int getLineNumber(long position) {
                int i = 0;
                while (true) {
                    int[] iArr = this.mTimeArr;
                    if (i >= iArr.length) {
                        return -1;
                    }
                    if (position >= ((long) iArr[i])) {
                        if (position < (i < iArr.length + -1 ? (long) iArr[i + 1] : Long.MAX_VALUE)) {
                            return i;
                        }
                    }
                    i++;
                }
            }

            /* Access modifiers changed, original: 0000 */
            public String getBeforeLines(long position) {
                if (this.mTimeArr == null) {
                    return null;
                }
                int num = getLineNumber(position);
                if (num <= 0) {
                    return null;
                }
                return this.mFullLyric.substring(0, ((LyricLine) this.mLyricLines.get(num)).pos - this.CRLF_LENGTH);
            }

            /* Access modifiers changed, original: 0000 */
            public String getAfterLines(long position) {
                if (this.mTimeArr == null) {
                    return null;
                }
                int num = getLineNumber(position);
                if (num < 0) {
                    return this.mFullLyric;
                }
                if (num >= this.mTimeArr.length - 1) {
                    return null;
                }
                LyricLine line = (LyricLine) this.mLyricLines.get(num);
                return this.mFullLyric.substring((line.pos + line.lyric.length()) + this.CRLF_LENGTH, this.mFullLyric.length());
            }

            /* Access modifiers changed, original: 0000 */
            public String getLastLine(long position) {
                if (this.mTimeArr == null) {
                    return null;
                }
                int num = getLineNumber(position);
                if (num <= 0) {
                    return null;
                }
                LyricLine last = (LyricLine) this.mLyricLines.get(num - 1);
                return this.mFullLyric.substring(last.pos, last.pos + last.lyric.length());
            }

            /* Access modifiers changed, original: 0000 */
            public String getNextLine(long position) {
                if (this.mTimeArr == null) {
                    return null;
                }
                int num = getLineNumber(position);
                if (num < -1 || num >= this.mTimeArr.length - 1) {
                    return null;
                }
                LyricLine next = (LyricLine) this.mLyricLines.get(num + 1);
                return this.mFullLyric.substring(next.pos, next.pos + next.lyric.length());
            }
        }

        public Lyric(LyricHeader header, ArrayList<LyricEntity> entityList, boolean isModified) {
            this.mHeader = header;
            this.mOriginHeaderOffset = this.mHeader.offset;
            this.mEntityList = entityList;
            this.mIsModified = isModified;
            String str = "\n";
            this.EMPTY_BEFORE = new LyricEntity(-1, str);
            this.EMPTY_AFTER = new LyricEntity(entityList.size(), str);
        }

        public void addOffset(int offset) {
            LyricHeader lyricHeader = this.mHeader;
            lyricHeader.offset += offset;
            this.mIsModified = true;
        }

        public void resetHeaderOffset() {
            this.mHeader.offset = this.mOriginHeaderOffset;
        }

        public boolean isModified() {
            return this.mIsModified;
        }

        public int size() {
            return this.mEntityList.size();
        }

        public LyricShot getLyricShot(long time) {
            int offset = this.mHeader.offset;
            if (((long) (((LyricEntity) this.mEntityList.get(0)).time + offset)) > time) {
                return new LyricShot(0, 0.0d);
            }
            for (int i = 1; i < this.mEntityList.size(); i++) {
                int timeThis = ((LyricEntity) this.mEntityList.get(i)).time + offset;
                if (((long) timeThis) > time) {
                    int timePrev = ((LyricEntity) this.mEntityList.get(i - 1)).time + offset;
                    double percent = 0.0d;
                    if (timeThis > timePrev) {
                        percent = ((double) (time - ((long) timePrev))) / ((double) (timeThis - timePrev));
                    }
                    return new LyricShot(i - 1, percent);
                }
            }
            long timeLast = (long) (((LyricEntity) this.mEntityList.get(size() - 1)).time + offset);
            if (time - timeLast >= 8000) {
                return new LyricShot(this.mEntityList.size(), 0.0d);
            }
            return new LyricShot(size() - 1, ((double) (time - timeLast)) / 8000.0d);
        }

        private long getTimeFromLyricShot(int line, double percent) {
            int maxLine = size() - 1;
            if (line >= maxLine) {
                return ((long) (((LyricEntity) this.mEntityList.get(maxLine)).time + ((line - maxLine) * 8000))) + ((long) (8000.0d * percent));
            }
            return (long) (((double) ((LyricEntity) this.mEntityList.get(line)).time) + (((double) (((LyricEntity) this.mEntityList.get(line + 1)).time - ((LyricEntity) this.mEntityList.get(line)).time)) * percent));
        }

        public void correctLyric(LyricShot lyricShot, int lineIndex, double percent) {
            int maxLine = size();
            if (lineIndex >= 0 && lineIndex <= maxLine && lyricShot.lineIndex >= 0 && lyricShot.lineIndex <= maxLine) {
                long currentTime = getTimeFromLyricShot(lyricShot.lineIndex, lyricShot.percent);
                long newTime = getTimeFromLyricShot(lineIndex, percent);
                boolean isOffsetDelay = true;
                if (lineIndex > lyricShot.lineIndex || (lineIndex == lyricShot.lineIndex && percent > lyricShot.percent)) {
                    isOffsetDelay = false;
                }
                if (!isOffsetDelay && currentTime > newTime) {
                    return;
                }
                if (!isOffsetDelay || currentTime >= newTime) {
                    addOffset((int) (currentTime - newTime));
                }
            }
        }

        public LyricEntity getLyricContent(int index) {
            if (index < 0) {
                return this.EMPTY_BEFORE;
            }
            if (index >= this.mEntityList.size()) {
                return this.EMPTY_AFTER;
            }
            return (LyricEntity) this.mEntityList.get(index);
        }

        public long getOpenTime() {
            return this.mOpenTime;
        }

        public ArrayList<CharSequence> getStringArr() {
            if (this.mEntityList.isEmpty()) {
                return null;
            }
            ArrayList<CharSequence> lyricArr = new ArrayList(this.mEntityList.size());
            Iterator it = this.mEntityList.iterator();
            while (it.hasNext()) {
                lyricArr.add(((LyricEntity) it.next()).lyric);
            }
            return lyricArr;
        }

        public int[] getTimeArr() {
            if (this.mEntityList.isEmpty()) {
                return null;
            }
            int[] timeArr = new int[this.mEntityList.size()];
            int i = 0;
            Iterator it = this.mEntityList.iterator();
            while (it.hasNext()) {
                int i2 = i + 1;
                timeArr[i] = ((LyricEntity) it.next()).time + this.mHeader.offset;
                i = i2;
            }
            return timeArr;
        }

        public void recycleContent() {
            this.mEntityList.clear();
        }

        public void decorate() {
            if (!this.mEntityList.isEmpty()) {
                ArrayList<LyricEntity> el = this.mEntityList;
                int len = el.size();
                if (len > 0 && !((LyricEntity) el.get(0)).isDecorated()) {
                    for (int i = 0; i < len; i++) {
                        ((LyricEntity) el.get(i)).decorate();
                    }
                }
            }
        }

        public void set(int[] time, ArrayList<CharSequence> lyric) {
            this.mLyricLocator.set(time, lyric);
        }

        public String getLine(long position) {
            return this.mLyricLocator.getLine(position);
        }

        public String getBeforeLines(long position) {
            return this.mLyricLocator.getBeforeLines(position);
        }

        public String getAfterLines(long position) {
            return this.mLyricLocator.getAfterLines(position);
        }

        public String getLastLine(long position) {
            return this.mLyricLocator.getLastLine(position);
        }

        public String getNextLine(long position) {
            return this.mLyricLocator.getNextLine(position);
        }
    }

    public static class LyricEntity {
        private static final String HTML_BR_PATTERN = "%s<br/>";
        public CharSequence lyric;
        public int time;

        public LyricEntity(int t, String str) {
            this.time = t;
            this.lyric = str;
        }

        public void decorate() {
            this.lyric = Html.fromHtml(String.format(HTML_BR_PATTERN, new Object[]{this.lyric}));
        }

        public boolean isDecorated() {
            return !(this.lyric instanceof String);
        }
    }

    public static class LyricHeader {
        public String album;
        public String artist;
        public String editor;
        public int offset;
        public String title;
        public String version;
    }

    public static class LyricShot {
        public int lineIndex;
        public double percent;

        public LyricShot(int index, double p) {
            this.lineIndex = index;
            this.percent = p;
        }
    }

    private static void correctTime(Lyric lyric) {
        if (lyric != null) {
            ArrayList<LyricEntity> el = lyric.mEntityList;
            int size = el.size();
            if (size > 1 && ((LyricEntity) el.get(0)).time == ((LyricEntity) el.get(1)).time) {
                ((LyricEntity) el.get(0)).time = ((LyricEntity) el.get(1)).time / 2;
            }
            for (int i = 1; i < size - 1; i++) {
                if (((LyricEntity) el.get(i)).time == ((LyricEntity) el.get(i + 1)).time) {
                    ((LyricEntity) el.get(i)).time = (((LyricEntity) el.get(i - 1)).time + ((LyricEntity) el.get(i + 1)).time) / 2;
                }
            }
        }
    }

    public static Lyric parseLyric(String raw) {
        Lyric lyric = null;
        if (raw == null) {
            return null;
        }
        try {
            lyric = doParse(raw);
            correctTime(lyric);
            return lyric;
        } catch (Exception e) {
            e.printStackTrace();
            return lyric;
        }
    }

    private static Lyric doParse(String raw) throws IOException {
        boolean needModify = false;
        LyricHeader header = new LyricHeader();
        ArrayList<LyricEntity> entityList = new ArrayList();
        String[] lines = raw.split(CRLF);
        if (lines != null) {
            for (String line : lines) {
                int lineParseRet = parseLine(line, header, entityList);
                if (lineParseRet == 0) {
                    break;
                }
                if (lineParseRet == 1) {
                    needModify = true;
                }
            }
        }
        if (entityList.isEmpty()) {
            return null;
        }
        Collections.sort(entityList, new EntityCompator());
        return new Lyric(header, entityList, needModify);
    }

    private static int parseLine(String str, LyricHeader header, ArrayList<LyricEntity> entityList) {
        String str2 = str.trim();
        if (TextUtils.isEmpty(str2)) {
            return 1;
        }
        str2 = TAG_EXTRA_LRC.matcher(str2).replaceAll("");
        String[] entityStr = "]";
        int indexOfLastTag = str2.lastIndexOf(entityStr);
        if (indexOfLastTag == -1) {
            return 1;
        }
        String content = str2.substring(indexOfLastTag + 1);
        String str3 = "[";
        int indexOfLeftTag = str2.indexOf(str3);
        if (indexOfLeftTag == -1) {
            return 1;
        }
        LyricHeader lyricHeader;
        ArrayList<LyricEntity> arrayList;
        int lineParseRet = 2;
        for (String s : str2.substring(indexOfLeftTag, indexOfLastTag).split(entityStr)) {
            String s2;
            if (s2.startsWith(str3)) {
                s2 = s2.substring(1);
                String[] values = s2.split(":");
                if (values.length >= 2) {
                    if (TextUtils.isDigitsOnly(values[0])) {
                        lineParseRet = parseEntity(values, entityList, content);
                        lyricHeader = header;
                    } else {
                        arrayList = entityList;
                        lineParseRet = parseHeader(s2, header);
                    }
                }
            }
            lyricHeader = header;
            arrayList = entityList;
        }
        lyricHeader = header;
        arrayList = entityList;
        return lineParseRet;
    }

    private static int parseHeader(String str, LyricHeader header) {
        int indexOfTag = str.indexOf(":");
        if (indexOfTag < 0 || indexOfTag >= str.length() - 1) {
            return 1;
        }
        int lineParseRet = 2;
        String tag = str.substring(null, indexOfTag);
        String value = str.substring(indexOfTag + 1);
        if (tag.equals(TAG_ALBUM)) {
            header.album = value;
        } else if (tag.equals(TAG_ARTIST)) {
            header.artist = value;
        } else if (tag.equals(TAG_TITLE)) {
            header.title = value;
        } else if (tag.equals(TAG_EDITOR)) {
            header.editor = value;
        } else if (tag.equals(TAG_VERSION)) {
            header.version = value;
        } else if (tag.equals("offset")) {
            try {
                header.offset = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                lineParseRet = 1;
            }
        } else {
            lineParseRet = 1;
        }
        return lineParseRet;
    }

    private static int parseEntity(String[] values, ArrayList<LyricEntity> entityList, String content) {
        try {
            int time = (int) (Double.parseDouble(values[values.length - 1]) * 1000.0d);
            int second = 0;
            int weight = 60;
            for (int i = values.length - 2; i >= 0; i--) {
                int val = Integer.parseInt(values[i]) * weight;
                weight *= 60;
                second += val;
            }
            time += second * 1000;
            if (time >= MAX_VALID_TIME) {
                return 2;
            }
            entityList.add(new LyricEntity(time, content));
            return 2;
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}
