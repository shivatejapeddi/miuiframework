package org.apache.http.conn.ssl;

import android.text.format.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.security.auth.x500.X500Principal;
import org.apache.miui.commons.lang3.ClassUtils;

@Deprecated
final class AndroidDistinguishedNameParser {
    private int beg;
    private char[] chars;
    private int cur;
    private final String dn;
    private int end;
    private final int length = this.dn.length();
    private int pos;

    public AndroidDistinguishedNameParser(X500Principal principal) {
        this.dn = principal.getName("RFC2253");
    }

    private String nextAT() {
        int i;
        while (true) {
            i = this.pos;
            if (i >= this.length || this.chars[i] != ' ') {
                i = this.pos;
            } else {
                this.pos = i + 1;
            }
        }
        i = this.pos;
        if (i == this.length) {
            return null;
        }
        char[] cArr;
        this.beg = i;
        this.pos = i + 1;
        while (true) {
            i = this.pos;
            if (i >= this.length) {
                break;
            }
            cArr = this.chars;
            if (cArr[i] == '=' || cArr[i] == ' ') {
                break;
            }
            this.pos = i + 1;
        }
        i = this.pos;
        String str = "Unexpected end of DN: ";
        StringBuilder stringBuilder;
        if (i < this.length) {
            char[] cArr2;
            int i2;
            this.end = i;
            if (this.chars[i] == ' ') {
                while (true) {
                    i = this.pos;
                    if (i >= this.length) {
                        break;
                    }
                    cArr = this.chars;
                    if (cArr[i] == '=' || cArr[i] != ' ') {
                        break;
                    }
                    this.pos = i + 1;
                }
                cArr2 = this.chars;
                i2 = this.pos;
                if (cArr2[i2] != '=' || i2 == this.length) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append(this.dn);
                    throw new IllegalStateException(stringBuilder.toString());
                }
            }
            this.pos++;
            while (true) {
                i = this.pos;
                if (i >= this.length || this.chars[i] != ' ') {
                    i = this.end;
                    i2 = this.beg;
                } else {
                    this.pos = i + 1;
                }
            }
            i = this.end;
            i2 = this.beg;
            if (i - i2 > 4) {
                cArr2 = this.chars;
                if (cArr2[i2 + 3] == ClassUtils.PACKAGE_SEPARATOR_CHAR && (cArr2[i2] == 'O' || cArr2[i2] == 'o')) {
                    cArr2 = this.chars;
                    i2 = this.beg;
                    if (cArr2[i2 + 1] == 'I' || cArr2[i2 + 1] == 'i') {
                        cArr2 = this.chars;
                        i2 = this.beg;
                        if (cArr2[i2 + 2] == 'D' || cArr2[i2 + 2] == DateFormat.DATE) {
                            this.beg += 4;
                        }
                    }
                }
            }
            cArr = this.chars;
            int i3 = this.beg;
            return new String(cArr, i3, this.end - i3);
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(this.dn);
        throw new IllegalStateException(stringBuilder.toString());
    }

    private String quotedAV() {
        this.pos++;
        this.beg = this.pos;
        this.end = this.beg;
        while (true) {
            int i = this.pos;
            if (i != this.length) {
                char[] cArr = this.chars;
                if (cArr[i] == '\"') {
                    int i2;
                    this.pos = i + 1;
                    while (true) {
                        i = this.pos;
                        if (i >= this.length || this.chars[i] != ' ') {
                            cArr = this.chars;
                            i2 = this.beg;
                        } else {
                            this.pos = i + 1;
                        }
                    }
                    cArr = this.chars;
                    i2 = this.beg;
                    return new String(cArr, i2, this.end - i2);
                }
                if (cArr[i] == '\\') {
                    cArr[this.end] = getEscaped();
                } else {
                    cArr[this.end] = cArr[i];
                }
                this.pos++;
                this.end++;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpected end of DN: ");
                stringBuilder.append(this.dn);
                throw new IllegalStateException(stringBuilder.toString());
            }
        }
    }

    private String hexAV() {
        int i = this.pos;
        String str = "Unexpected end of DN: ";
        if (i + 4 < this.length) {
            this.beg = i;
            this.pos = i + 1;
            while (true) {
                i = this.pos;
                if (i == this.length) {
                    break;
                }
                char[] cArr = this.chars;
                if (cArr[i] == '+' || cArr[i] == ',' || cArr[i] == ';') {
                    break;
                } else if (cArr[i] == ' ') {
                    this.end = i;
                    this.pos = i + 1;
                    while (true) {
                        i = this.pos;
                        if (i >= this.length || this.chars[i] != ' ') {
                            break;
                        }
                        this.pos = i + 1;
                    }
                } else {
                    if (cArr[i] >= DateFormat.CAPITAL_AM_PM && cArr[i] <= 'F') {
                        cArr[i] = (char) (cArr[i] + 32);
                    }
                    this.pos++;
                }
            }
            this.end = this.pos;
            i = this.end;
            int p = this.beg;
            i -= p;
            if (i < 5 || (i & 1) == 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(this.dn);
                throw new IllegalStateException(stringBuilder.toString());
            }
            byte[] encoded = new byte[(i / 2)];
            p++;
            for (int i2 = 0; i2 < encoded.length; i2++) {
                encoded[i2] = (byte) getByte(p);
                p += 2;
            }
            return new String(this.chars, this.beg, i);
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(str);
        stringBuilder2.append(this.dn);
        throw new IllegalStateException(stringBuilder2.toString());
    }

    /* JADX WARNING: Missing block: B:30:0x009b, code skipped:
            r1 = r8.chars;
            r2 = r8.beg;
     */
    /* JADX WARNING: Missing block: B:31:0x00a7, code skipped:
            return new java.lang.String(r1, r2, r8.cur - r2);
     */
    private java.lang.String escapedAV() {
        /*
        r8 = this;
        r0 = r8.pos;
        r8.beg = r0;
        r8.end = r0;
    L_0x0006:
        r0 = r8.pos;
        r1 = r8.length;
        if (r0 < r1) goto L_0x0019;
    L_0x000c:
        r0 = new java.lang.String;
        r1 = r8.chars;
        r2 = r8.beg;
        r3 = r8.end;
        r3 = r3 - r2;
        r0.<init>(r1, r2, r3);
        return r0;
    L_0x0019:
        r1 = r8.chars;
        r2 = r1[r0];
        r3 = 44;
        r4 = 43;
        r5 = 59;
        r6 = 32;
        if (r2 == r6) goto L_0x0060;
    L_0x0027:
        if (r2 == r5) goto L_0x0053;
    L_0x0029:
        r5 = 92;
        if (r2 == r5) goto L_0x0040;
    L_0x002d:
        if (r2 == r4) goto L_0x0053;
    L_0x002f:
        if (r2 == r3) goto L_0x0053;
    L_0x0031:
        r2 = r8.end;
        r3 = r2 + 1;
        r8.end = r3;
        r3 = r1[r0];
        r1[r2] = r3;
        r0 = r0 + 1;
        r8.pos = r0;
        goto L_0x0006;
    L_0x0040:
        r0 = r8.end;
        r2 = r0 + 1;
        r8.end = r2;
        r2 = r8.getEscaped();
        r1[r0] = r2;
        r0 = r8.pos;
        r0 = r0 + 1;
        r8.pos = r0;
        goto L_0x0006;
    L_0x0053:
        r0 = new java.lang.String;
        r1 = r8.chars;
        r2 = r8.beg;
        r3 = r8.end;
        r3 = r3 - r2;
        r0.<init>(r1, r2, r3);
        return r0;
    L_0x0060:
        r2 = r8.end;
        r8.cur = r2;
        r0 = r0 + 1;
        r8.pos = r0;
        r0 = r2 + 1;
        r8.end = r0;
        r1[r2] = r6;
    L_0x006e:
        r0 = r8.pos;
        r1 = r8.length;
        if (r0 >= r1) goto L_0x0087;
    L_0x0074:
        r1 = r8.chars;
        r2 = r1[r0];
        if (r2 != r6) goto L_0x0087;
    L_0x007a:
        r2 = r8.end;
        r7 = r2 + 1;
        r8.end = r7;
        r1[r2] = r6;
        r0 = r0 + 1;
        r8.pos = r0;
        goto L_0x006e;
    L_0x0087:
        r0 = r8.pos;
        r1 = r8.length;
        if (r0 == r1) goto L_0x009b;
    L_0x008d:
        r1 = r8.chars;
        r2 = r1[r0];
        if (r2 == r3) goto L_0x009b;
    L_0x0093:
        r2 = r1[r0];
        if (r2 == r4) goto L_0x009b;
    L_0x0097:
        r0 = r1[r0];
        if (r0 != r5) goto L_0x0006;
    L_0x009b:
        r0 = new java.lang.String;
        r1 = r8.chars;
        r2 = r8.beg;
        r3 = r8.cur;
        r3 = r3 - r2;
        r0.<init>(r1, r2, r3);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.conn.ssl.AndroidDistinguishedNameParser.escapedAV():java.lang.String");
    }

    private char getEscaped() {
        this.pos++;
        int i = this.pos;
        if (i != this.length) {
            char c = this.chars[i];
            if (!(c == ' ' || c == '%' || c == '\\' || c == '_' || c == '\"' || c == '#')) {
                switch (c) {
                    case '*':
                    case '+':
                    case ',':
                        break;
                    default:
                        switch (c) {
                            case ';':
                            case '<':
                            case '=':
                            case '>':
                                break;
                            default:
                                return getUTF8();
                        }
                }
            }
            return this.chars[this.pos];
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unexpected end of DN: ");
        stringBuilder.append(this.dn);
        throw new IllegalStateException(stringBuilder.toString());
    }

    private char getUTF8() {
        int res = getByte(this.pos);
        this.pos++;
        if (res < 128) {
            return (char) res;
        }
        if (res < 192 || res > 247) {
            return '?';
        }
        int count;
        if (res <= 223) {
            count = 1;
            res &= 31;
        } else if (res <= 239) {
            count = 2;
            res &= 15;
        } else {
            count = 3;
            res &= 7;
        }
        for (int i = 0; i < count; i++) {
            this.pos++;
            int i2 = this.pos;
            if (i2 == this.length || this.chars[i2] != '\\') {
                return '?';
            }
            this.pos = i2 + 1;
            i2 = getByte(this.pos);
            this.pos++;
            if ((i2 & 192) != 128) {
                return '?';
            }
            res = (res << 6) + (i2 & 63);
        }
        return (char) res;
    }

    private int getByte(int position) {
        String str = "Malformed DN: ";
        if (position + 1 < this.length) {
            StringBuilder stringBuilder;
            int b1 = this.chars[position];
            if (b1 >= 48 && b1 <= 57) {
                b1 -= 48;
            } else if (b1 >= 97 && b1 <= 102) {
                b1 -= 87;
            } else if (b1 < 65 || b1 > 70) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(this.dn);
                throw new IllegalStateException(stringBuilder.toString());
            } else {
                b1 -= 55;
            }
            int b2 = this.chars[position + 1];
            if (b2 >= 48 && b2 <= 57) {
                b2 -= 48;
            } else if (b2 >= 97 && b2 <= 102) {
                b2 -= 87;
            } else if (b2 < 65 || b2 > 70) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(this.dn);
                throw new IllegalStateException(stringBuilder.toString());
            } else {
                b2 -= 55;
            }
            return (b1 << 4) + b2;
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(str);
        stringBuilder2.append(this.dn);
        throw new IllegalStateException(stringBuilder2.toString());
    }

    public String findMostSpecific(String attributeType) {
        this.pos = 0;
        this.beg = 0;
        this.end = 0;
        this.cur = 0;
        this.chars = this.dn.toCharArray();
        String attType = nextAT();
        if (attType == null) {
            return null;
        }
        while (true) {
            String attValue = "";
            int i = this.pos;
            if (i == this.length) {
                return null;
            }
            char c = this.chars[i];
            if (c == '\"') {
                attValue = quotedAV();
            } else if (c == '#') {
                attValue = hexAV();
            } else if (!(c == '+' || c == ',' || c == ';')) {
                attValue = escapedAV();
            }
            if (attributeType.equalsIgnoreCase(attType)) {
                return attValue;
            }
            i = this.pos;
            if (i >= this.length) {
                return null;
            }
            char[] cArr = this.chars;
            String str = "Malformed DN: ";
            StringBuilder stringBuilder;
            if (cArr[i] == ',' || cArr[i] == ';' || cArr[i] == '+') {
                this.pos++;
                attType = nextAT();
                if (attType == null) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append(this.dn);
                    throw new IllegalStateException(stringBuilder.toString());
                }
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(this.dn);
                throw new IllegalStateException(stringBuilder.toString());
            }
        }
    }

    public List<String> getAllMostSpecificFirst(String attributeType) {
        this.pos = 0;
        this.beg = 0;
        this.end = 0;
        this.cur = 0;
        this.chars = this.dn.toCharArray();
        List<String> result = Collections.emptyList();
        String attType = nextAT();
        if (attType == null) {
            return result;
        }
        while (true) {
            int i = this.pos;
            if (i >= this.length) {
                break;
            }
            String attValue = "";
            char c = this.chars[i];
            if (c == '\"') {
                attValue = quotedAV();
            } else if (c == '#') {
                attValue = hexAV();
            } else if (!(c == '+' || c == ',' || c == ';')) {
                attValue = escapedAV();
            }
            if (attributeType.equalsIgnoreCase(attType)) {
                if (result.isEmpty()) {
                    result = new ArrayList();
                }
                result.add(attValue);
            }
            i = this.pos;
            if (i >= this.length) {
                break;
            }
            char[] cArr = this.chars;
            String str = "Malformed DN: ";
            StringBuilder stringBuilder;
            if (cArr[i] == ',' || cArr[i] == ';' || cArr[i] == '+') {
                this.pos++;
                attType = nextAT();
                if (attType == null) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append(this.dn);
                    throw new IllegalStateException(stringBuilder.toString());
                }
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(this.dn);
                throw new IllegalStateException(stringBuilder.toString());
            }
        }
        return result;
    }
}
