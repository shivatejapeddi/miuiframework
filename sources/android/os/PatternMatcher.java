package android.os;

import android.os.Parcelable.Creator;
import android.util.proto.ProtoOutputStream;
import org.apache.miui.commons.lang3.ClassUtils;

public class PatternMatcher implements Parcelable {
    public static final Creator<PatternMatcher> CREATOR = new Creator<PatternMatcher>() {
        public PatternMatcher createFromParcel(Parcel source) {
            return new PatternMatcher(source);
        }

        public PatternMatcher[] newArray(int size) {
            return new PatternMatcher[size];
        }
    };
    private static final int MAX_PATTERN_STORAGE = 2048;
    private static final int NO_MATCH = -1;
    private static final int PARSED_MODIFIER_ONE_OR_MORE = -8;
    private static final int PARSED_MODIFIER_RANGE_START = -5;
    private static final int PARSED_MODIFIER_RANGE_STOP = -6;
    private static final int PARSED_MODIFIER_ZERO_OR_MORE = -7;
    private static final int PARSED_TOKEN_CHAR_ANY = -4;
    private static final int PARSED_TOKEN_CHAR_SET_INVERSE_START = -2;
    private static final int PARSED_TOKEN_CHAR_SET_START = -1;
    private static final int PARSED_TOKEN_CHAR_SET_STOP = -3;
    public static final int PATTERN_ADVANCED_GLOB = 3;
    public static final int PATTERN_LITERAL = 0;
    public static final int PATTERN_PREFIX = 1;
    public static final int PATTERN_SIMPLE_GLOB = 2;
    private static final String TAG = "PatternMatcher";
    private static final int TOKEN_TYPE_ANY = 1;
    private static final int TOKEN_TYPE_INVERSE_SET = 3;
    private static final int TOKEN_TYPE_LITERAL = 0;
    private static final int TOKEN_TYPE_SET = 2;
    private static final int[] sParsedPatternScratch = new int[2048];
    private final int[] mParsedPattern;
    private final String mPattern;
    private final int mType;

    public PatternMatcher(String pattern, int type) {
        this.mPattern = pattern;
        this.mType = type;
        if (this.mType == 3) {
            this.mParsedPattern = parseAndVerifyAdvancedPattern(pattern);
        } else {
            this.mParsedPattern = null;
        }
    }

    public final String getPath() {
        return this.mPattern;
    }

    public final int getType() {
        return this.mType;
    }

    public boolean match(String str) {
        return matchPattern(str, this.mPattern, this.mParsedPattern, this.mType);
    }

    public String toString() {
        String type = "? ";
        int i = this.mType;
        if (i == 0) {
            type = "LITERAL: ";
        } else if (i == 1) {
            type = "PREFIX: ";
        } else if (i == 2) {
            type = "GLOB: ";
        } else if (i == 3) {
            type = "ADVANCED: ";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PatternMatcher{");
        stringBuilder.append(type);
        stringBuilder.append(this.mPattern);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public void writeToProto(ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1138166333441L, this.mPattern);
        proto.write(1159641169922L, this.mType);
        proto.end(token);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mPattern);
        dest.writeInt(this.mType);
        dest.writeIntArray(this.mParsedPattern);
    }

    public PatternMatcher(Parcel src) {
        this.mPattern = src.readString();
        this.mType = src.readInt();
        this.mParsedPattern = src.createIntArray();
    }

    static boolean matchPattern(String match, String pattern, int[] parsedPattern, int type) {
        if (match == null) {
            return false;
        }
        if (type == 0) {
            return pattern.equals(match);
        }
        if (type == 1) {
            return match.startsWith(pattern);
        }
        if (type == 2) {
            return matchGlobPattern(pattern, match);
        }
        if (type == 3) {
            return matchAdvancedPattern(parsedPattern, match);
        }
        return false;
    }

    static boolean matchGlobPattern(String pattern, String match) {
        int NP = pattern.length();
        boolean z = false;
        if (NP <= 0) {
            if (match.length() <= 0) {
                z = true;
            }
            return z;
        }
        int NM = match.length();
        int ip = 0;
        int im = 0;
        char nextChar = pattern.charAt(0);
        while (ip < NP && im < NM) {
            char c = nextChar;
            ip++;
            nextChar = ip < NP ? pattern.charAt(ip) : 0;
            boolean escaped = c == '\\';
            if (escaped) {
                c = nextChar;
                ip++;
                nextChar = ip < NP ? pattern.charAt(ip) : 0;
            }
            if (nextChar == '*') {
                if (escaped || c != ClassUtils.PACKAGE_SEPARATOR_CHAR) {
                    while (match.charAt(im) == c) {
                        im++;
                        if (im >= NM) {
                            break;
                        }
                    }
                    ip++;
                    nextChar = ip < NP ? pattern.charAt(ip) : 0;
                } else if (ip >= NP - 1) {
                    return true;
                } else {
                    ip++;
                    nextChar = pattern.charAt(ip);
                    if (nextChar == '\\') {
                        ip++;
                        nextChar = ip < NP ? pattern.charAt(ip) : 0;
                    }
                    while (match.charAt(im) != nextChar) {
                        im++;
                        if (im >= NM) {
                            break;
                        }
                    }
                    if (im == NM) {
                        return false;
                    }
                    ip++;
                    nextChar = ip < NP ? pattern.charAt(ip) : 0;
                    im++;
                }
            } else if (c != ClassUtils.PACKAGE_SEPARATOR_CHAR && match.charAt(im) != c) {
                return false;
            } else {
                im++;
            }
        }
        if (ip < NP || im < NM) {
            return ip == NP + -2 && pattern.charAt(ip) == ClassUtils.PACKAGE_SEPARATOR_CHAR && pattern.charAt(ip + 1) == '*';
        } else {
            return true;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:77:0x0160  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x0160  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x0160  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x0160  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x0160  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x0160  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x0160  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0121  */
    static synchronized int[] parseAndVerifyAdvancedPattern(java.lang.String r17) {
        /*
        r1 = r17;
        r2 = android.os.PatternMatcher.class;
        monitor-enter(r2);
        r0 = 0;
        r3 = r17.length();	 Catch:{ all -> 0x01fb }
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
    L_0x000e:
        if (r0 >= r3) goto L_0x01e9;
    L_0x0010:
        r8 = 2045; // 0x7fd float:2.866E-42 double:1.0104E-320;
        if (r4 > r8) goto L_0x01e1;
    L_0x0014:
        r8 = r1.charAt(r0);	 Catch:{ all -> 0x01fb }
        r9 = 0;
        r10 = 42;
        r11 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        if (r8 == r10) goto L_0x00fc;
    L_0x001f:
        r10 = 43;
        if (r8 == r10) goto L_0x00db;
    L_0x0023:
        r10 = 46;
        if (r8 == r10) goto L_0x00d0;
    L_0x0027:
        r10 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        if (r8 == r10) goto L_0x00ab;
    L_0x002b:
        if (r8 == r11) goto L_0x009d;
    L_0x002d:
        r10 = -2;
        r12 = -1;
        switch(r8) {
            case 91: goto L_0x0076;
            case 92: goto L_0x005e;
            case 93: goto L_0x0037;
            default: goto L_0x0032;
        };	 Catch:{ all -> 0x01fb }
    L_0x0032:
        r9 = 1;
        r12 = r4;
        r4 = r0;
        goto L_0x011f;
    L_0x0037:
        if (r5 != 0) goto L_0x003e;
    L_0x0039:
        r9 = 1;
        r12 = r4;
        r4 = r0;
        goto L_0x011f;
    L_0x003e:
        r13 = sParsedPatternScratch;	 Catch:{ all -> 0x01fb }
        r14 = r4 + -1;
        r13 = r13[r14];	 Catch:{ all -> 0x01fb }
        if (r13 == r12) goto L_0x0056;
    L_0x0046:
        if (r13 == r10) goto L_0x0056;
    L_0x0048:
        r10 = sParsedPatternScratch;	 Catch:{ all -> 0x01fb }
        r12 = r4 + 1;
        r14 = -3;
        r10[r4] = r14;	 Catch:{ all -> 0x01fb }
        r4 = 0;
        r5 = 0;
        r7 = r5;
        r5 = r4;
        r4 = r0;
        goto L_0x011f;
    L_0x0056:
        r10 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x01fb }
        r11 = "You must define characters in a set.";
        r10.<init>(r11);	 Catch:{ all -> 0x01fb }
        throw r10;	 Catch:{ all -> 0x01fb }
    L_0x005e:
        r10 = r0 + 1;
        if (r10 >= r3) goto L_0x006e;
    L_0x0062:
        r0 = r0 + 1;
        r10 = r1.charAt(r0);	 Catch:{ all -> 0x01fb }
        r8 = r10;
        r9 = 1;
        r12 = r4;
        r4 = r0;
        goto L_0x011f;
    L_0x006e:
        r10 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x01fb }
        r11 = "Escape found at end of pattern!";
        r10.<init>(r11);	 Catch:{ all -> 0x01fb }
        throw r10;	 Catch:{ all -> 0x01fb }
    L_0x0076:
        if (r5 == 0) goto L_0x007d;
    L_0x0078:
        r9 = 1;
        r12 = r4;
        r4 = r0;
        goto L_0x011f;
    L_0x007d:
        r11 = r0 + 1;
        r11 = r1.charAt(r11);	 Catch:{ all -> 0x01fb }
        r13 = 94;
        if (r11 != r13) goto L_0x0091;
    L_0x0087:
        r11 = sParsedPatternScratch;	 Catch:{ all -> 0x01fb }
        r12 = r4 + 1;
        r11[r4] = r10;	 Catch:{ all -> 0x01fb }
        r0 = r0 + 1;
        r4 = r12;
        goto L_0x0098;
    L_0x0091:
        r10 = sParsedPatternScratch;	 Catch:{ all -> 0x01fb }
        r11 = r4 + 1;
        r10[r4] = r12;	 Catch:{ all -> 0x01fb }
        r4 = r11;
    L_0x0098:
        r0 = r0 + 1;
        r5 = 1;
        goto L_0x000e;
    L_0x009d:
        if (r6 == 0) goto L_0x011d;
    L_0x009f:
        r10 = sParsedPatternScratch;	 Catch:{ all -> 0x01fb }
        r12 = r4 + 1;
        r13 = -6;
        r10[r4] = r13;	 Catch:{ all -> 0x01fb }
        r4 = 0;
        r6 = r4;
        r4 = r0;
        goto L_0x011f;
    L_0x00ab:
        if (r5 != 0) goto L_0x011d;
    L_0x00ad:
        if (r4 == 0) goto L_0x00c8;
    L_0x00af:
        r10 = sParsedPatternScratch;	 Catch:{ all -> 0x01fb }
        r12 = r4 + -1;
        r10 = r10[r12];	 Catch:{ all -> 0x01fb }
        r10 = isParsedModifier(r10);	 Catch:{ all -> 0x01fb }
        if (r10 != 0) goto L_0x00c8;
    L_0x00bb:
        r10 = sParsedPatternScratch;	 Catch:{ all -> 0x01fb }
        r12 = r4 + 1;
        r13 = -5;
        r10[r4] = r13;	 Catch:{ all -> 0x01fb }
        r0 = r0 + 1;
        r4 = 1;
        r6 = r4;
        r4 = r0;
        goto L_0x011f;
    L_0x00c8:
        r10 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x01fb }
        r11 = "Modifier must follow a token.";
        r10.<init>(r11);	 Catch:{ all -> 0x01fb }
        throw r10;	 Catch:{ all -> 0x01fb }
    L_0x00d0:
        if (r5 != 0) goto L_0x011d;
    L_0x00d2:
        r10 = sParsedPatternScratch;	 Catch:{ all -> 0x01fb }
        r12 = r4 + 1;
        r13 = -4;
        r10[r4] = r13;	 Catch:{ all -> 0x01fb }
        r4 = r0;
        goto L_0x011f;
    L_0x00db:
        if (r5 != 0) goto L_0x011d;
    L_0x00dd:
        if (r4 == 0) goto L_0x00f4;
    L_0x00df:
        r10 = sParsedPatternScratch;	 Catch:{ all -> 0x01fb }
        r12 = r4 + -1;
        r10 = r10[r12];	 Catch:{ all -> 0x01fb }
        r10 = isParsedModifier(r10);	 Catch:{ all -> 0x01fb }
        if (r10 != 0) goto L_0x00f4;
    L_0x00eb:
        r10 = sParsedPatternScratch;	 Catch:{ all -> 0x01fb }
        r12 = r4 + 1;
        r13 = -8;
        r10[r4] = r13;	 Catch:{ all -> 0x01fb }
        r4 = r0;
        goto L_0x011f;
    L_0x00f4:
        r10 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x01fb }
        r11 = "Modifier must follow a token.";
        r10.<init>(r11);	 Catch:{ all -> 0x01fb }
        throw r10;	 Catch:{ all -> 0x01fb }
    L_0x00fc:
        if (r5 != 0) goto L_0x011d;
    L_0x00fe:
        if (r4 == 0) goto L_0x0115;
    L_0x0100:
        r10 = sParsedPatternScratch;	 Catch:{ all -> 0x01fb }
        r12 = r4 + -1;
        r10 = r10[r12];	 Catch:{ all -> 0x01fb }
        r10 = isParsedModifier(r10);	 Catch:{ all -> 0x01fb }
        if (r10 != 0) goto L_0x0115;
    L_0x010c:
        r10 = sParsedPatternScratch;	 Catch:{ all -> 0x01fb }
        r12 = r4 + 1;
        r13 = -7;
        r10[r4] = r13;	 Catch:{ all -> 0x01fb }
        r4 = r0;
        goto L_0x011f;
    L_0x0115:
        r10 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x01fb }
        r11 = "Modifier must follow a token.";
        r10.<init>(r11);	 Catch:{ all -> 0x01fb }
        throw r10;	 Catch:{ all -> 0x01fb }
    L_0x011d:
        r12 = r4;
        r4 = r0;
    L_0x011f:
        if (r5 == 0) goto L_0x0160;
    L_0x0121:
        if (r7 == 0) goto L_0x012d;
    L_0x0123:
        r0 = sParsedPatternScratch;	 Catch:{ all -> 0x01fb }
        r10 = r12 + 1;
        r0[r12] = r8;	 Catch:{ all -> 0x01fb }
        r0 = 0;
        r7 = r0;
        goto L_0x01db;
    L_0x012d:
        r0 = r4 + 2;
        if (r0 >= r3) goto L_0x0151;
    L_0x0131:
        r0 = r4 + 1;
        r0 = r1.charAt(r0);	 Catch:{ all -> 0x01fb }
        r10 = 45;
        if (r0 != r10) goto L_0x0151;
    L_0x013b:
        r0 = r4 + 2;
        r0 = r1.charAt(r0);	 Catch:{ all -> 0x01fb }
        r10 = 93;
        if (r0 == r10) goto L_0x0151;
    L_0x0145:
        r0 = 1;
        r7 = sParsedPatternScratch;	 Catch:{ all -> 0x01fb }
        r10 = r12 + 1;
        r7[r12] = r8;	 Catch:{ all -> 0x01fb }
        r4 = r4 + 1;
        r7 = r0;
        goto L_0x01db;
    L_0x0151:
        r0 = sParsedPatternScratch;	 Catch:{ all -> 0x01fb }
        r10 = r12 + 1;
        r0[r12] = r8;	 Catch:{ all -> 0x01fb }
        r0 = sParsedPatternScratch;	 Catch:{ all -> 0x01fb }
        r11 = r10 + 1;
        r0[r10] = r8;	 Catch:{ all -> 0x01fb }
        r10 = r11;
        goto L_0x01db;
    L_0x0160:
        if (r6 == 0) goto L_0x01d1;
    L_0x0162:
        r0 = r1.indexOf(r11, r4);	 Catch:{ all -> 0x01fb }
        r10 = r0;
        if (r10 < 0) goto L_0x01c9;
    L_0x0169:
        r0 = r1.substring(r4, r10);	 Catch:{ all -> 0x01fb }
        r11 = r0;
        r0 = 44;
        r0 = r11.indexOf(r0);	 Catch:{ all -> 0x01fb }
        r13 = r0;
        if (r13 >= 0) goto L_0x017d;
    L_0x0177:
        r0 = java.lang.Integer.parseInt(r11);	 Catch:{ NumberFormatException -> 0x01c0 }
        r14 = r0;
        goto L_0x019c;
    L_0x017d:
        r0 = 0;
        r0 = r11.substring(r0, r13);	 Catch:{ NumberFormatException -> 0x01c0 }
        r0 = java.lang.Integer.parseInt(r0);	 Catch:{ NumberFormatException -> 0x01c0 }
        r14 = r11.length();	 Catch:{ NumberFormatException -> 0x01c0 }
        r14 = r14 + -1;
        if (r13 != r14) goto L_0x0192;
    L_0x018e:
        r14 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        goto L_0x019c;
    L_0x0192:
        r14 = r13 + 1;
        r14 = r11.substring(r14);	 Catch:{ NumberFormatException -> 0x01c0 }
        r14 = java.lang.Integer.parseInt(r14);	 Catch:{ NumberFormatException -> 0x01c0 }
    L_0x019c:
        if (r0 > r14) goto L_0x01b6;
    L_0x019e:
        r15 = sParsedPatternScratch;	 Catch:{ NumberFormatException -> 0x01c0 }
        r16 = r12 + 1;
        r15[r12] = r0;	 Catch:{ NumberFormatException -> 0x01b2 }
        r12 = sParsedPatternScratch;	 Catch:{ NumberFormatException -> 0x01b2 }
        r15 = r16 + 1;
        r12[r16] = r14;	 Catch:{ NumberFormatException -> 0x01af }
        r0 = r10;
        r4 = r15;
        goto L_0x000e;
    L_0x01af:
        r0 = move-exception;
        r12 = r15;
        goto L_0x01c1;
    L_0x01b2:
        r0 = move-exception;
        r12 = r16;
        goto L_0x01c1;
    L_0x01b6:
        r15 = new java.lang.IllegalArgumentException;	 Catch:{ NumberFormatException -> 0x01c0 }
        r16 = r0;
        r0 = "Range quantifier minimum is greater than maximum";
        r15.<init>(r0);	 Catch:{ NumberFormatException -> 0x01c0 }
        throw r15;	 Catch:{ NumberFormatException -> 0x01c0 }
    L_0x01c0:
        r0 = move-exception;
    L_0x01c1:
        r14 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x01fb }
        r15 = "Range number format incorrect";
        r14.<init>(r15, r0);	 Catch:{ all -> 0x01fb }
        throw r14;	 Catch:{ all -> 0x01fb }
    L_0x01c9:
        r0 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x01fb }
        r11 = "Range not ended with '}'";
        r0.<init>(r11);	 Catch:{ all -> 0x01fb }
        throw r0;	 Catch:{ all -> 0x01fb }
    L_0x01d1:
        if (r9 == 0) goto L_0x01da;
    L_0x01d3:
        r0 = sParsedPatternScratch;	 Catch:{ all -> 0x01fb }
        r10 = r12 + 1;
        r0[r12] = r8;	 Catch:{ all -> 0x01fb }
        goto L_0x01db;
    L_0x01da:
        r10 = r12;
        r0 = r4 + 1;
        r4 = r10;
        goto L_0x000e;
    L_0x01e1:
        r8 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x01fb }
        r9 = "Pattern is too large!";
        r8.<init>(r9);	 Catch:{ all -> 0x01fb }
        throw r8;	 Catch:{ all -> 0x01fb }
    L_0x01e9:
        if (r5 != 0) goto L_0x01f3;
    L_0x01eb:
        r8 = sParsedPatternScratch;	 Catch:{ all -> 0x01fb }
        r8 = java.util.Arrays.copyOf(r8, r4);	 Catch:{ all -> 0x01fb }
        monitor-exit(r2);
        return r8;
    L_0x01f3:
        r8 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x01fb }
        r9 = "Set was not terminated!";
        r8.<init>(r9);	 Catch:{ all -> 0x01fb }
        throw r8;	 Catch:{ all -> 0x01fb }
    L_0x01fb:
        r0 = move-exception;
        monitor-exit(r2);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.PatternMatcher.parseAndVerifyAdvancedPattern(java.lang.String):int[]");
    }

    private static boolean isParsedModifier(int parsedChar) {
        return parsedChar == -8 || parsedChar == -7 || parsedChar == -6 || parsedChar == -5;
    }

    static boolean matchAdvancedPattern(int[] parsedPattern, String match) {
        int[] iArr = parsedPattern;
        int ip = 0;
        int LP = iArr.length;
        int LM = match.length();
        int charSetStart = 0;
        int charSetEnd = 0;
        int im = 0;
        while (true) {
            boolean z = true;
            if (ip < LP) {
                int charSetStart2;
                int charSetEnd2;
                int tokenType;
                int ip2;
                int minRepetition;
                int maxRepetition;
                int patternChar = iArr[ip];
                if (patternChar == -4) {
                    ip++;
                    charSetStart2 = charSetStart;
                    charSetEnd2 = charSetEnd;
                    tokenType = 1;
                } else if (patternChar == -2 || patternChar == -1) {
                    int tokenType2;
                    if (patternChar == -1) {
                        tokenType2 = 2;
                    } else {
                        tokenType2 = 3;
                    }
                    charSetStart = ip + 1;
                    while (true) {
                        ip++;
                        if (ip >= LP || iArr[ip] == -3) {
                            charSetEnd = ip - 1;
                            ip++;
                            charSetStart2 = charSetStart;
                            charSetEnd2 = charSetEnd;
                            tokenType = tokenType2;
                        }
                    }
                    charSetEnd = ip - 1;
                    ip++;
                    charSetStart2 = charSetStart;
                    charSetEnd2 = charSetEnd;
                    tokenType = tokenType2;
                } else {
                    charSetStart = ip;
                    ip++;
                    charSetStart2 = charSetStart;
                    charSetEnd2 = charSetEnd;
                    tokenType = 0;
                }
                int i;
                if (ip >= LP) {
                    ip2 = ip;
                    minRepetition = 1;
                    maxRepetition = 1;
                    i = patternChar;
                } else {
                    patternChar = iArr[ip];
                    if (patternChar == -8) {
                        ip2 = ip + 1;
                        minRepetition = 1;
                        maxRepetition = Integer.MAX_VALUE;
                    } else if (patternChar == -7) {
                        ip2 = ip + 1;
                        minRepetition = 0;
                        maxRepetition = Integer.MAX_VALUE;
                        i = patternChar;
                    } else if (patternChar != -5) {
                        ip2 = ip;
                        minRepetition = 1;
                        maxRepetition = 1;
                        i = patternChar;
                    } else {
                        ip++;
                        charSetStart = iArr[ip];
                        ip++;
                        ip2 = ip + 2;
                        maxRepetition = iArr[ip];
                        minRepetition = charSetStart;
                        i = patternChar;
                    }
                }
                if (minRepetition > maxRepetition) {
                    return false;
                }
                ip = matchChars(match, im, LM, tokenType, minRepetition, maxRepetition, parsedPattern, charSetStart2, charSetEnd2);
                if (ip == -1) {
                    return false;
                }
                im += ip;
                charSetStart = charSetStart2;
                charSetEnd = charSetEnd2;
                ip = ip2;
            } else {
                if (ip < LP || im < LM) {
                    z = false;
                }
                return z;
            }
        }
    }

    private static int matchChars(String match, int im, int lm, int tokenType, int minRepetition, int maxRepetition, int[] parsedPattern, int tokenStart, int tokenEnd) {
        int matched = 0;
        while (matched < maxRepetition) {
            if (!matchChar(match, im + matched, lm, tokenType, parsedPattern, tokenStart, tokenEnd)) {
                break;
            }
            matched++;
        }
        return matched < minRepetition ? -1 : matched;
    }

    private static boolean matchChar(String match, int im, int lm, int tokenType, int[] parsedPattern, int tokenStart, int tokenEnd) {
        boolean z = false;
        if (im >= lm) {
            return false;
        }
        if (tokenType == 0) {
            if (match.charAt(im) == parsedPattern[tokenStart]) {
                z = true;
            }
            return z;
        } else if (tokenType == 1) {
            return true;
        } else {
            int i;
            char matchChar;
            if (tokenType == 2) {
                i = tokenStart;
                while (i < tokenEnd) {
                    matchChar = match.charAt(im);
                    if (matchChar >= parsedPattern[i] && matchChar <= parsedPattern[i + 1]) {
                        return true;
                    }
                    i += 2;
                }
                return false;
            } else if (tokenType != 3) {
                return false;
            } else {
                i = tokenStart;
                while (i < tokenEnd) {
                    matchChar = match.charAt(im);
                    if (matchChar >= parsedPattern[i] && matchChar <= parsedPattern[i + 1]) {
                        return false;
                    }
                    i += 2;
                }
                return true;
            }
        }
    }
}
