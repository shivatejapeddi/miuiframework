package android.util;

import android.annotation.UnsupportedAppUsage;
import java.io.UnsupportedEncodingException;

public class Base64 {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int CRLF = 4;
    public static final int DEFAULT = 0;
    public static final int NO_CLOSE = 16;
    public static final int NO_PADDING = 1;
    public static final int NO_WRAP = 2;
    public static final int URL_SAFE = 8;

    static abstract class Coder {
        public int op;
        public byte[] output;

        public abstract int maxOutputSize(int i);

        public abstract boolean process(byte[] bArr, int i, int i2, boolean z);

        Coder() {
        }
    }

    static class Decoder extends Coder {
        private static final int[] DECODE = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private static final int[] DECODE_WEBSAFE = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private static final int EQUALS = -2;
        private static final int SKIP = -1;
        private final int[] alphabet;
        private int state;
        private int value;

        public Decoder(int flags, byte[] output) {
            this.output = output;
            this.alphabet = (flags & 8) == 0 ? DECODE : DECODE_WEBSAFE;
            this.state = 0;
            this.value = 0;
        }

        public int maxOutputSize(int len) {
            return ((len * 3) / 4) + 10;
        }

        public boolean process(byte[] input, int offset, int len, boolean finish) {
            if (this.state == 6) {
                return false;
            }
            int p = offset;
            int len2 = len + offset;
            int state = this.state;
            int value = this.value;
            int op = 0;
            byte[] output = this.output;
            int[] alphabet = this.alphabet;
            while (p < len2) {
                int i;
                if (state == 0) {
                    while (p + 4 <= len2) {
                        i = (((alphabet[input[p] & 255] << 18) | (alphabet[input[p + 1] & 255] << 12)) | (alphabet[input[p + 2] & 255] << 6)) | alphabet[input[p + 3] & 255];
                        value = i;
                        if (i < 0) {
                            break;
                        }
                        output[op + 2] = (byte) value;
                        output[op + 1] = (byte) (value >> 8);
                        output[op] = (byte) (value >> 16);
                        op += 3;
                        p += 4;
                    }
                    if (p >= len2) {
                        break;
                    }
                }
                i = p + 1;
                p = alphabet[input[p] & 255];
                if (state != 0) {
                    if (state != 1) {
                        if (state != 2) {
                            if (state != 3) {
                                if (state != 4) {
                                    if (state == 5 && p != -1) {
                                        this.state = 6;
                                        return false;
                                    }
                                } else if (p == -2) {
                                    state++;
                                } else if (p != -1) {
                                    this.state = 6;
                                    return false;
                                }
                            } else if (p >= 0) {
                                value = (value << 6) | p;
                                output[op + 2] = (byte) value;
                                output[op + 1] = (byte) (value >> 8);
                                output[op] = (byte) (value >> 16);
                                op += 3;
                                state = 0;
                            } else if (p == -2) {
                                output[op + 1] = (byte) (value >> 2);
                                output[op] = (byte) (value >> 10);
                                op += 2;
                                state = 5;
                            } else if (p != -1) {
                                this.state = 6;
                                return false;
                            }
                        } else if (p >= 0) {
                            value = (value << 6) | p;
                            state++;
                        } else if (p == -2) {
                            int op2 = op + 1;
                            output[op] = (byte) (value >> 4);
                            state = 4;
                            op = op2;
                        } else if (p != -1) {
                            this.state = 6;
                            return false;
                        }
                    } else if (p >= 0) {
                        value = (value << 6) | p;
                        state++;
                    } else if (p != -1) {
                        this.state = 6;
                        return false;
                    }
                } else if (p >= 0) {
                    value = p;
                    state++;
                } else if (p != -1) {
                    this.state = 6;
                    return false;
                }
                p = i;
            }
            if (finish) {
                if (state != 0) {
                    int op3;
                    if (state == 1) {
                        this.state = 6;
                        return false;
                    } else if (state == 2) {
                        op3 = op + 1;
                        output[op] = (byte) (value >> 4);
                        op = op3;
                    } else if (state == 3) {
                        op3 = op + 1;
                        output[op] = (byte) (value >> 10);
                        op = op3 + 1;
                        output[op3] = (byte) (value >> 2);
                    } else if (state == 4) {
                        this.state = 6;
                        return false;
                    }
                }
                this.state = state;
                this.op = op;
                return true;
            }
            this.state = state;
            this.value = value;
            this.op = op;
            return true;
        }
    }

    static class Encoder extends Coder {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private static final byte[] ENCODE = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 43, (byte) 47};
        private static final byte[] ENCODE_WEBSAFE = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 45, (byte) 95};
        public static final int LINE_GROUPS = 19;
        private final byte[] alphabet;
        private int count;
        public final boolean do_cr;
        public final boolean do_newline;
        public final boolean do_padding;
        private final byte[] tail;
        int tailLen;

        static {
            Class cls = Base64.class;
        }

        public Encoder(int flags, byte[] output) {
            this.output = output;
            boolean z = true;
            this.do_padding = (flags & 1) == 0;
            this.do_newline = (flags & 2) == 0;
            if ((flags & 4) == 0) {
                z = false;
            }
            this.do_cr = z;
            this.alphabet = (flags & 8) == 0 ? ENCODE : ENCODE_WEBSAFE;
            this.tail = new byte[2];
            this.tailLen = 0;
            this.count = this.do_newline ? 19 : -1;
        }

        public int maxOutputSize(int len) {
            return ((len * 8) / 5) + 10;
        }

        /* JADX WARNING: Removed duplicated region for block: B:14:0x0062  */
        /* JADX WARNING: Removed duplicated region for block: B:22:0x00a0  */
        /* JADX WARNING: Removed duplicated region for block: B:75:0x01df  */
        /* JADX WARNING: Removed duplicated region for block: B:29:0x00f6  */
        /* JADX WARNING: Removed duplicated region for block: B:14:0x0062  */
        /* JADX WARNING: Removed duplicated region for block: B:22:0x00a0  */
        /* JADX WARNING: Removed duplicated region for block: B:29:0x00f6  */
        /* JADX WARNING: Removed duplicated region for block: B:75:0x01df  */
        public boolean process(byte[] r19, int r20, int r21, boolean r22) {
            /*
            r18 = this;
            r0 = r18;
            r1 = r0.alphabet;
            r2 = r0.output;
            r3 = 0;
            r4 = r0.count;
            r5 = r20;
            r6 = r21 + r20;
            r7 = -1;
            r8 = r0.tailLen;
            r9 = 2;
            r10 = 1;
            if (r8 == 0) goto L_0x0059;
        L_0x0014:
            r11 = 0;
            if (r8 == r10) goto L_0x0038;
        L_0x0017:
            if (r8 == r9) goto L_0x001a;
        L_0x0019:
            goto L_0x005a;
        L_0x001a:
            r8 = r5 + 1;
            if (r8 > r6) goto L_0x005a;
        L_0x001e:
            r8 = r0.tail;
            r12 = r8[r11];
            r12 = r12 & 255;
            r12 = r12 << 16;
            r8 = r8[r10];
            r8 = r8 & 255;
            r8 = r8 << 8;
            r8 = r8 | r12;
            r12 = r5 + 1;
            r5 = r19[r5];
            r5 = r5 & 255;
            r7 = r8 | r5;
            r0.tailLen = r11;
            goto L_0x005b;
        L_0x0038:
            r8 = r5 + 2;
            if (r8 > r6) goto L_0x005a;
        L_0x003c:
            r8 = r0.tail;
            r8 = r8[r11];
            r8 = r8 & 255;
            r8 = r8 << 16;
            r12 = r5 + 1;
            r5 = r19[r5];
            r5 = r5 & 255;
            r5 = r5 << 8;
            r5 = r5 | r8;
            r8 = r12 + 1;
            r12 = r19[r12];
            r12 = r12 & 255;
            r7 = r5 | r12;
            r0.tailLen = r11;
            r12 = r8;
            goto L_0x005b;
        L_0x005a:
            r12 = r5;
        L_0x005b:
            r5 = -1;
            r8 = 13;
            r11 = 10;
            if (r7 == r5) goto L_0x009c;
        L_0x0062:
            r5 = r3 + 1;
            r13 = r7 >> 18;
            r13 = r13 & 63;
            r13 = r1[r13];
            r2[r3] = r13;
            r3 = r5 + 1;
            r13 = r7 >> 12;
            r13 = r13 & 63;
            r13 = r1[r13];
            r2[r5] = r13;
            r5 = r3 + 1;
            r13 = r7 >> 6;
            r13 = r13 & 63;
            r13 = r1[r13];
            r2[r3] = r13;
            r3 = r5 + 1;
            r13 = r7 & 63;
            r13 = r1[r13];
            r2[r5] = r13;
            r4 = r4 + -1;
            if (r4 != 0) goto L_0x009c;
        L_0x008c:
            r5 = r0.do_cr;
            if (r5 == 0) goto L_0x0095;
        L_0x0090:
            r5 = r3 + 1;
            r2[r3] = r8;
            r3 = r5;
        L_0x0095:
            r5 = r3 + 1;
            r2[r3] = r11;
            r4 = 19;
            r3 = r5;
        L_0x009c:
            r5 = r12 + 3;
            if (r5 > r6) goto L_0x00f4;
        L_0x00a0:
            r5 = r19[r12];
            r5 = r5 & 255;
            r5 = r5 << 16;
            r13 = r12 + 1;
            r13 = r19[r13];
            r13 = r13 & 255;
            r13 = r13 << 8;
            r5 = r5 | r13;
            r13 = r12 + 2;
            r13 = r19[r13];
            r13 = r13 & 255;
            r7 = r5 | r13;
            r5 = r7 >> 18;
            r5 = r5 & 63;
            r5 = r1[r5];
            r2[r3] = r5;
            r5 = r3 + 1;
            r13 = r7 >> 12;
            r13 = r13 & 63;
            r13 = r1[r13];
            r2[r5] = r13;
            r5 = r3 + 2;
            r13 = r7 >> 6;
            r13 = r13 & 63;
            r13 = r1[r13];
            r2[r5] = r13;
            r5 = r3 + 3;
            r13 = r7 & 63;
            r13 = r1[r13];
            r2[r5] = r13;
            r12 = r12 + 3;
            r3 = r3 + 4;
            r4 = r4 + -1;
            if (r4 != 0) goto L_0x009c;
        L_0x00e3:
            r5 = r0.do_cr;
            if (r5 == 0) goto L_0x00ec;
        L_0x00e7:
            r5 = r3 + 1;
            r2[r3] = r8;
            r3 = r5;
        L_0x00ec:
            r5 = r3 + 1;
            r2[r3] = r11;
            r4 = 19;
            r3 = r5;
            goto L_0x009c;
        L_0x00f4:
            if (r22 == 0) goto L_0x01df;
        L_0x00f6:
            r5 = r0.tailLen;
            r13 = r12 - r5;
            r14 = r6 + -1;
            r15 = 61;
            if (r13 != r14) goto L_0x0150;
        L_0x0100:
            r9 = 0;
            if (r5 <= 0) goto L_0x010f;
        L_0x0103:
            r5 = r0.tail;
            r13 = r9 + 1;
            r5 = r5[r9];
            r17 = r12;
            r12 = r5;
            r5 = r17;
            goto L_0x0114;
        L_0x010f:
            r5 = r12 + 1;
            r12 = r19[r12];
            r13 = r9;
        L_0x0114:
            r9 = r12 & 255;
            r7 = r9 << 4;
            r9 = r0.tailLen;
            r9 = r9 - r13;
            r0.tailLen = r9;
            r9 = r3 + 1;
            r12 = r7 >> 6;
            r12 = r12 & 63;
            r12 = r1[r12];
            r2[r3] = r12;
            r3 = r9 + 1;
            r12 = r7 & 63;
            r12 = r1[r12];
            r2[r9] = r12;
            r9 = r0.do_padding;
            if (r9 == 0) goto L_0x013b;
        L_0x0133:
            r9 = r3 + 1;
            r2[r3] = r15;
            r3 = r9 + 1;
            r2[r9] = r15;
        L_0x013b:
            r9 = r0.do_newline;
            if (r9 == 0) goto L_0x014d;
        L_0x013f:
            r9 = r0.do_cr;
            if (r9 == 0) goto L_0x0148;
        L_0x0143:
            r9 = r3 + 1;
            r2[r3] = r8;
            r3 = r9;
        L_0x0148:
            r8 = r3 + 1;
            r2[r3] = r11;
            r3 = r8;
        L_0x014d:
            r12 = r5;
            goto L_0x01dd;
        L_0x0150:
            r13 = r12 - r5;
            r14 = r6 + -2;
            if (r13 != r14) goto L_0x01c5;
        L_0x0156:
            r13 = 0;
            if (r5 <= r10) goto L_0x0166;
        L_0x0159:
            r5 = r0.tail;
            r14 = r13 + 1;
            r5 = r5[r13];
            r13 = r14;
            r17 = r12;
            r12 = r5;
            r5 = r17;
            goto L_0x016a;
        L_0x0166:
            r5 = r12 + 1;
            r12 = r19[r12];
        L_0x016a:
            r12 = r12 & 255;
            r12 = r12 << r11;
            r14 = r0.tailLen;
            if (r14 <= 0) goto L_0x0179;
        L_0x0171:
            r14 = r0.tail;
            r16 = r13 + 1;
            r13 = r14[r13];
            r14 = r5;
            goto L_0x0180;
        L_0x0179:
            r14 = r5 + 1;
            r5 = r19[r5];
            r16 = r13;
            r13 = r5;
        L_0x0180:
            r5 = r13 & 255;
            r5 = r5 << r9;
            r5 = r5 | r12;
            r7 = r0.tailLen;
            r7 = r7 - r16;
            r0.tailLen = r7;
            r7 = r3 + 1;
            r9 = r5 >> 12;
            r9 = r9 & 63;
            r9 = r1[r9];
            r2[r3] = r9;
            r3 = r7 + 1;
            r9 = r5 >> 6;
            r9 = r9 & 63;
            r9 = r1[r9];
            r2[r7] = r9;
            r7 = r3 + 1;
            r9 = r5 & 63;
            r9 = r1[r9];
            r2[r3] = r9;
            r3 = r0.do_padding;
            if (r3 == 0) goto L_0x01af;
        L_0x01aa:
            r3 = r7 + 1;
            r2[r7] = r15;
            goto L_0x01b0;
        L_0x01af:
            r3 = r7;
        L_0x01b0:
            r7 = r0.do_newline;
            if (r7 == 0) goto L_0x01c2;
        L_0x01b4:
            r7 = r0.do_cr;
            if (r7 == 0) goto L_0x01bd;
        L_0x01b8:
            r7 = r3 + 1;
            r2[r3] = r8;
            r3 = r7;
        L_0x01bd:
            r7 = r3 + 1;
            r2[r3] = r11;
            r3 = r7;
        L_0x01c2:
            r7 = r5;
            r12 = r14;
            goto L_0x01dd;
        L_0x01c5:
            r5 = r0.do_newline;
            if (r5 == 0) goto L_0x01dd;
        L_0x01c9:
            if (r3 <= 0) goto L_0x01dd;
        L_0x01cb:
            r5 = 19;
            if (r4 == r5) goto L_0x01dd;
        L_0x01cf:
            r5 = r0.do_cr;
            if (r5 == 0) goto L_0x01d8;
        L_0x01d3:
            r5 = r3 + 1;
            r2[r3] = r8;
            r3 = r5;
        L_0x01d8:
            r5 = r3 + 1;
            r2[r3] = r11;
            r3 = r5;
            goto L_0x020c;
        L_0x01df:
            r5 = r6 + -1;
            if (r12 != r5) goto L_0x01f0;
        L_0x01e3:
            r5 = r0.tail;
            r8 = r0.tailLen;
            r9 = r8 + 1;
            r0.tailLen = r9;
            r9 = r19[r12];
            r5[r8] = r9;
            goto L_0x020c;
        L_0x01f0:
            r5 = r6 + -2;
            if (r12 != r5) goto L_0x020c;
        L_0x01f4:
            r5 = r0.tail;
            r8 = r0.tailLen;
            r9 = r8 + 1;
            r0.tailLen = r9;
            r9 = r19[r12];
            r5[r8] = r9;
            r8 = r0.tailLen;
            r9 = r8 + 1;
            r0.tailLen = r9;
            r9 = r12 + 1;
            r9 = r19[r9];
            r5[r8] = r9;
        L_0x020c:
            r0.op = r3;
            r0.count = r4;
            return r10;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.util.Base64$Encoder.process(byte[], int, int, boolean):boolean");
        }
    }

    public static byte[] decode(String str, int flags) {
        return decode(str.getBytes(), flags);
    }

    public static byte[] decode(byte[] input, int flags) {
        return decode(input, 0, input.length, flags);
    }

    public static byte[] decode(byte[] input, int offset, int len, int flags) {
        Decoder decoder = new Decoder(flags, new byte[((len * 3) / 4)]);
        if (!decoder.process(input, offset, len, true)) {
            throw new IllegalArgumentException("bad base-64");
        } else if (decoder.op == decoder.output.length) {
            return decoder.output;
        } else {
            byte[] temp = new byte[decoder.op];
            System.arraycopy(decoder.output, 0, temp, 0, decoder.op);
            return temp;
        }
    }

    public static String encodeToString(byte[] input, int flags) {
        try {
            return new String(encode(input, flags), "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static String encodeToString(byte[] input, int offset, int len, int flags) {
        try {
            return new String(encode(input, offset, len, flags), "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static byte[] encode(byte[] input, int flags) {
        return encode(input, 0, input.length, flags);
    }

    public static byte[] encode(byte[] input, int offset, int len, int flags) {
        int i;
        Encoder encoder = new Encoder(flags, null);
        int output_len = (len / 3) * 4;
        int i2 = 2;
        if (!encoder.do_padding) {
            i = len % 3;
            if (i != 0) {
                if (i == 1) {
                    output_len += 2;
                } else if (i == 2) {
                    output_len += 3;
                }
            }
        } else if (len % 3 > 0) {
            output_len += 4;
        }
        if (encoder.do_newline && len > 0) {
            i = ((len - 1) / 57) + 1;
            if (!encoder.do_cr) {
                i2 = 1;
            }
            output_len += i * i2;
        }
        encoder.output = new byte[output_len];
        encoder.process(input, offset, len, true);
        return encoder.output;
    }

    @UnsupportedAppUsage
    private Base64() {
    }
}
