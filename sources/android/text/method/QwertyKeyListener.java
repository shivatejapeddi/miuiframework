package android.text.method;

import android.text.AutoText;
import android.text.Editable;
import android.text.NoCopySpan;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.TextKeyListener.Capitalize;
import android.util.SparseArray;
import android.view.View;

public class QwertyKeyListener extends BaseKeyListener {
    private static SparseArray<String> PICKER_SETS = new SparseArray();
    private static QwertyKeyListener sFullKeyboardInstance;
    private static QwertyKeyListener[] sInstance = new QwertyKeyListener[(Capitalize.values().length * 2)];
    private Capitalize mAutoCap;
    private boolean mAutoText;
    private boolean mFullKeyboard;

    static class Replaced implements NoCopySpan {
        private char[] mText;

        public Replaced(char[] text) {
            this.mText = text;
        }
    }

    static {
        PICKER_SETS.put(65, "ÀÁÂÄÆÃÅĄĀ");
        PICKER_SETS.put(67, "ÇĆČ");
        PICKER_SETS.put(68, "Ď");
        PICKER_SETS.put(69, "ÈÉÊËĘĚĒ");
        PICKER_SETS.put(71, "Ğ");
        PICKER_SETS.put(76, "Ł");
        PICKER_SETS.put(73, "ÌÍÎÏĪİ");
        PICKER_SETS.put(78, "ÑŃŇ");
        PICKER_SETS.put(79, "ØŒÕÒÓÔÖŌ");
        PICKER_SETS.put(82, "Ř");
        PICKER_SETS.put(83, "ŚŠŞ");
        PICKER_SETS.put(84, "Ť");
        PICKER_SETS.put(85, "ÙÚÛÜŮŪ");
        PICKER_SETS.put(89, "ÝŸ");
        PICKER_SETS.put(90, "ŹŻŽ");
        PICKER_SETS.put(97, "àáâäæãåąā");
        PICKER_SETS.put(99, "çćč");
        PICKER_SETS.put(100, "ď");
        PICKER_SETS.put(101, "èéêëęěē");
        PICKER_SETS.put(103, "ğ");
        PICKER_SETS.put(105, "ìíîïīı");
        PICKER_SETS.put(108, "ł");
        PICKER_SETS.put(110, "ñńň");
        PICKER_SETS.put(111, "øœõòóôöō");
        PICKER_SETS.put(114, "ř");
        PICKER_SETS.put(115, "§ßśšş");
        PICKER_SETS.put(116, "ť");
        PICKER_SETS.put(117, "ùúûüůū");
        PICKER_SETS.put(121, "ýÿ");
        PICKER_SETS.put(122, "źżž");
        PICKER_SETS.put(61185, "…¥•®©±[]{}\\|");
        PICKER_SETS.put(47, "\\");
        PICKER_SETS.put(49, "¹½⅓¼⅛");
        PICKER_SETS.put(50, "²⅔");
        PICKER_SETS.put(51, "³¾⅜");
        PICKER_SETS.put(52, "⁴");
        PICKER_SETS.put(53, "⅝");
        PICKER_SETS.put(55, "⅞");
        PICKER_SETS.put(48, "ⁿ∅");
        PICKER_SETS.put(36, "¢£€¥₣₤₱");
        PICKER_SETS.put(37, "‰");
        PICKER_SETS.put(42, "†‡");
        PICKER_SETS.put(45, "–—");
        PICKER_SETS.put(43, "±");
        PICKER_SETS.put(40, "[{<");
        PICKER_SETS.put(41, "]}>");
        PICKER_SETS.put(33, "¡");
        PICKER_SETS.put(34, "“”«»˝");
        PICKER_SETS.put(63, "¿");
        PICKER_SETS.put(44, "‚„");
        PICKER_SETS.put(61, "≠≈∞");
        PICKER_SETS.put(60, "≤«‹");
        PICKER_SETS.put(62, "≥»›");
    }

    private QwertyKeyListener(Capitalize cap, boolean autoText, boolean fullKeyboard) {
        this.mAutoCap = cap;
        this.mAutoText = autoText;
        this.mFullKeyboard = fullKeyboard;
    }

    public QwertyKeyListener(Capitalize cap, boolean autoText) {
        this(cap, autoText, false);
    }

    public static QwertyKeyListener getInstance(boolean autoText, Capitalize cap) {
        int off = (cap.ordinal() * 2) + autoText;
        QwertyKeyListener[] qwertyKeyListenerArr = sInstance;
        if (qwertyKeyListenerArr[off] == null) {
            qwertyKeyListenerArr[off] = new QwertyKeyListener(cap, autoText);
        }
        return sInstance[off];
    }

    public static QwertyKeyListener getInstanceForFullKeyboard() {
        if (sFullKeyboardInstance == null) {
            sFullKeyboardInstance = new QwertyKeyListener(Capitalize.NONE, false, true);
        }
        return sFullKeyboardInstance;
    }

    public int getInputType() {
        return BaseKeyListener.makeTextContentType(this.mAutoCap, this.mAutoText);
    }

    /* JADX WARNING: Missing block: B:161:0x0297, code skipped:
            if (r25.hasModifiers(2) != false) goto L_0x029d;
     */
    public boolean onKeyDown(android.view.View r22, android.text.Editable r23, int r24, android.view.KeyEvent r25) {
        /*
        r21 = this;
        r7 = r21;
        r8 = r22;
        r9 = r23;
        r10 = r25;
        r0 = 0;
        if (r8 == 0) goto L_0x0019;
    L_0x000b:
        r1 = android.text.method.TextKeyListener.getInstance();
        r2 = r22.getContext();
        r0 = r1.getPrefs(r2);
        r11 = r0;
        goto L_0x001a;
    L_0x0019:
        r11 = r0;
    L_0x001a:
        r0 = android.text.Selection.getSelectionStart(r23);
        r1 = android.text.Selection.getSelectionEnd(r23);
        r2 = java.lang.Math.min(r0, r1);
        r3 = java.lang.Math.max(r0, r1);
        r12 = 0;
        if (r2 < 0) goto L_0x0033;
    L_0x002d:
        if (r3 >= 0) goto L_0x0030;
    L_0x002f:
        goto L_0x0033;
    L_0x0030:
        r13 = r2;
        r14 = r3;
        goto L_0x003a;
    L_0x0033:
        r3 = r12;
        r2 = r12;
        android.text.Selection.setSelection(r9, r12, r12);
        r13 = r2;
        r14 = r3;
    L_0x003a:
        r0 = android.text.method.TextKeyListener.ACTIVE;
        r15 = r9.getSpanStart(r0);
        r0 = android.text.method.TextKeyListener.ACTIVE;
        r6 = r9.getSpanEnd(r0);
        r0 = android.text.method.MetaKeyKeyListener.getMetaState(r9, r10);
        r5 = r10.getUnicodeChar(r0);
        r0 = r7.mFullKeyboard;
        r16 = 1;
        if (r0 != 0) goto L_0x0093;
    L_0x0054:
        r0 = r25.getRepeatCount();
        if (r0 <= 0) goto L_0x0090;
    L_0x005a:
        if (r13 != r14) goto L_0x0090;
    L_0x005c:
        if (r13 <= 0) goto L_0x0090;
    L_0x005e:
        r1 = r13 + -1;
        r4 = r9.charAt(r1);
        if (r4 == r5) goto L_0x0070;
    L_0x0066:
        r1 = java.lang.Character.toUpperCase(r5);
        if (r4 != r1) goto L_0x006d;
    L_0x006c:
        goto L_0x0070;
    L_0x006d:
        r12 = r5;
        r10 = r6;
        goto L_0x0095;
    L_0x0070:
        if (r8 == 0) goto L_0x008b;
    L_0x0072:
        r17 = 0;
        r1 = r21;
        r2 = r22;
        r3 = r23;
        r18 = r4;
        r12 = r5;
        r5 = r17;
        r10 = r6;
        r6 = r0;
        r1 = r1.showCharacterPicker(r2, r3, r4, r5, r6);
        if (r1 == 0) goto L_0x0095;
    L_0x0087:
        android.text.method.MetaKeyKeyListener.resetMetaState(r23);
        return r16;
    L_0x008b:
        r18 = r4;
        r12 = r5;
        r10 = r6;
        goto L_0x0095;
    L_0x0090:
        r12 = r5;
        r10 = r6;
        goto L_0x0095;
    L_0x0093:
        r12 = r5;
        r10 = r6;
    L_0x0095:
        r0 = 61185; // 0xef01 float:8.5738E-41 double:3.02294E-319;
        if (r12 != r0) goto L_0x00ae;
    L_0x009a:
        if (r8 == 0) goto L_0x00aa;
    L_0x009c:
        r4 = 61185; // 0xef01 float:8.5738E-41 double:3.02294E-319;
        r5 = 1;
        r6 = 1;
        r1 = r21;
        r2 = r22;
        r3 = r23;
        r1.showCharacterPicker(r2, r3, r4, r5, r6);
    L_0x00aa:
        android.text.method.MetaKeyKeyListener.resetMetaState(r23);
        return r16;
    L_0x00ae:
        r0 = 61184; // 0xef00 float:8.5737E-41 double:3.0229E-319;
        if (r12 != r0) goto L_0x00e6;
    L_0x00b3:
        r0 = 16;
        if (r13 != r14) goto L_0x00ce;
    L_0x00b7:
        r1 = r14;
    L_0x00b8:
        if (r1 <= 0) goto L_0x00cf;
    L_0x00ba:
        r2 = r14 - r1;
        r3 = 4;
        if (r2 >= r3) goto L_0x00cf;
    L_0x00bf:
        r2 = r1 + -1;
        r2 = r9.charAt(r2);
        r2 = java.lang.Character.digit(r2, r0);
        if (r2 < 0) goto L_0x00cf;
    L_0x00cb:
        r1 = r1 + -1;
        goto L_0x00b8;
    L_0x00ce:
        r1 = r13;
    L_0x00cf:
        r2 = -1;
        r3 = android.text.TextUtils.substring(r9, r1, r14);	 Catch:{ NumberFormatException -> 0x00da }
        r0 = java.lang.Integer.parseInt(r3, r0);	 Catch:{ NumberFormatException -> 0x00da }
        r2 = r0;
        goto L_0x00db;
    L_0x00da:
        r0 = move-exception;
        if (r2 < 0) goto L_0x00e4;
    L_0x00de:
        r13 = r1;
        android.text.Selection.setSelection(r9, r13, r14);
        r5 = r2;
        goto L_0x00e7;
    L_0x00e4:
        r5 = 0;
        goto L_0x00e7;
    L_0x00e6:
        r5 = r12;
    L_0x00e7:
        r0 = 10;
        if (r5 == 0) goto L_0x0283;
    L_0x00eb:
        r3 = 0;
        r4 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r4 = r4 & r5;
        if (r4 == 0) goto L_0x00f6;
    L_0x00f1:
        r3 = 1;
        r4 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r5 = r5 & r4;
    L_0x00f6:
        if (r15 != r13) goto L_0x0119;
    L_0x00f8:
        if (r10 != r14) goto L_0x0119;
    L_0x00fa:
        r4 = 0;
        r6 = r14 - r13;
        r6 = r6 + -1;
        if (r6 != 0) goto L_0x010e;
    L_0x0101:
        r6 = r9.charAt(r13);
        r12 = android.view.KeyEvent.getDeadChar(r6, r5);
        if (r12 == 0) goto L_0x010e;
    L_0x010b:
        r5 = r12;
        r4 = 1;
        r3 = 0;
    L_0x010e:
        if (r4 != 0) goto L_0x0119;
    L_0x0110:
        android.text.Selection.setSelection(r9, r14);
        r6 = android.text.method.TextKeyListener.ACTIVE;
        r9.removeSpan(r6);
        r13 = r14;
    L_0x0119:
        r4 = r11 & 1;
        if (r4 == 0) goto L_0x0162;
    L_0x011d:
        r4 = java.lang.Character.isLowerCase(r5);
        if (r4 == 0) goto L_0x0162;
    L_0x0123:
        r4 = r7.mAutoCap;
        r4 = android.text.method.TextKeyListener.shouldCap(r4, r9, r13);
        if (r4 == 0) goto L_0x0162;
    L_0x012b:
        r4 = android.text.method.TextKeyListener.CAPPED;
        r4 = r9.getSpanEnd(r4);
        r6 = android.text.method.TextKeyListener.CAPPED;
        r6 = r9.getSpanFlags(r6);
        if (r4 != r13) goto L_0x0148;
    L_0x0139:
        r12 = r6 >> 16;
        r17 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        r12 = r12 & r17;
        if (r12 != r5) goto L_0x0148;
    L_0x0142:
        r12 = android.text.method.TextKeyListener.CAPPED;
        r9.removeSpan(r12);
        goto L_0x0162;
    L_0x0148:
        r6 = r5 << 16;
        r5 = java.lang.Character.toUpperCase(r5);
        if (r13 != 0) goto L_0x0159;
    L_0x0150:
        r12 = android.text.method.TextKeyListener.CAPPED;
        r1 = r6 | 17;
        r2 = 0;
        r9.setSpan(r12, r2, r2, r1);
        goto L_0x0162;
    L_0x0159:
        r1 = android.text.method.TextKeyListener.CAPPED;
        r2 = r13 + -1;
        r12 = r6 | 33;
        r9.setSpan(r1, r2, r13, r12);
    L_0x0162:
        if (r13 == r14) goto L_0x0167;
    L_0x0164:
        android.text.Selection.setSelection(r9, r14);
    L_0x0167:
        r1 = OLD_SEL_START;
        r2 = 17;
        r9.setSpan(r1, r13, r13, r2);
        r1 = (char) r5;
        r1 = java.lang.String.valueOf(r1);
        r9.replace(r13, r14, r1);
        r1 = OLD_SEL_START;
        r1 = r9.getSpanStart(r1);
        r2 = android.text.Selection.getSelectionEnd(r23);
        if (r1 >= r2) goto L_0x0193;
    L_0x0182:
        r4 = android.text.method.TextKeyListener.LAST_TYPED;
        r6 = 33;
        r9.setSpan(r4, r1, r2, r6);
        if (r3 == 0) goto L_0x0193;
    L_0x018b:
        android.text.Selection.setSelection(r9, r1, r2);
        r4 = android.text.method.TextKeyListener.ACTIVE;
        r9.setSpan(r4, r1, r2, r6);
    L_0x0193:
        android.text.method.MetaKeyKeyListener.adjustMetaAfterKeypress(r23);
        r4 = r11 & 2;
        r6 = 22;
        r12 = 32;
        if (r4 == 0) goto L_0x0224;
    L_0x019e:
        r4 = r7.mAutoText;
        if (r4 == 0) goto L_0x0224;
    L_0x01a2:
        if (r5 == r12) goto L_0x01c8;
    L_0x01a4:
        r4 = 9;
        if (r5 == r4) goto L_0x01c8;
    L_0x01a8:
        if (r5 == r0) goto L_0x01c8;
    L_0x01aa:
        r0 = 44;
        if (r5 == r0) goto L_0x01c8;
    L_0x01ae:
        r0 = 46;
        if (r5 == r0) goto L_0x01c8;
    L_0x01b2:
        r0 = 33;
        if (r5 == r0) goto L_0x01c8;
    L_0x01b6:
        r0 = 63;
        if (r5 == r0) goto L_0x01c8;
    L_0x01ba:
        r0 = 34;
        if (r5 == r0) goto L_0x01c8;
    L_0x01be:
        r0 = java.lang.Character.getType(r5);
        if (r0 != r6) goto L_0x01c5;
    L_0x01c4:
        goto L_0x01c8;
    L_0x01c5:
        r19 = r2;
        goto L_0x0226;
    L_0x01c8:
        r0 = android.text.method.TextKeyListener.INHIBIT_REPLACEMENT;
        r0 = r9.getSpanEnd(r0);
        if (r0 == r1) goto L_0x0221;
    L_0x01d0:
        r0 = r1;
    L_0x01d1:
        if (r0 <= 0) goto L_0x01e7;
    L_0x01d3:
        r4 = r0 + -1;
        r4 = r9.charAt(r4);
        r14 = 39;
        if (r4 == r14) goto L_0x01e4;
    L_0x01dd:
        r14 = java.lang.Character.isLetter(r4);
        if (r14 != 0) goto L_0x01e4;
    L_0x01e3:
        goto L_0x01e7;
    L_0x01e4:
        r0 = r0 + -1;
        goto L_0x01d1;
    L_0x01e7:
        r4 = r7.getReplacement(r9, r0, r1, r8);
        if (r4 == 0) goto L_0x021e;
    L_0x01ed:
        r14 = r23.length();
        r6 = android.text.method.QwertyKeyListener.Replaced.class;
        r12 = 0;
        r6 = r9.getSpans(r12, r14, r6);
        r6 = (android.text.method.QwertyKeyListener.Replaced[]) r6;
        r12 = 0;
    L_0x01fb:
        r14 = r6.length;
        if (r12 >= r14) goto L_0x0206;
    L_0x01fe:
        r14 = r6[r12];
        r9.removeSpan(r14);
        r12 = r12 + 1;
        goto L_0x01fb;
    L_0x0206:
        r12 = r1 - r0;
        r12 = new char[r12];
        r14 = 0;
        android.text.TextUtils.getChars(r9, r0, r1, r12, r14);
        r14 = new android.text.method.QwertyKeyListener$Replaced;
        r14.<init>(r12);
        r19 = r2;
        r2 = 33;
        r9.setSpan(r14, r0, r1, r2);
        r9.replace(r0, r1, r4);
        goto L_0x0226;
    L_0x021e:
        r19 = r2;
        goto L_0x0226;
    L_0x0221:
        r19 = r2;
        goto L_0x0226;
    L_0x0224:
        r19 = r2;
    L_0x0226:
        r0 = r11 & 4;
        if (r0 == 0) goto L_0x0280;
    L_0x022a:
        r0 = r7.mAutoText;
        if (r0 == 0) goto L_0x0280;
    L_0x022e:
        r2 = android.text.Selection.getSelectionEnd(r23);
        r0 = r2 + -3;
        if (r0 < 0) goto L_0x0282;
    L_0x0236:
        r0 = r2 + -1;
        r0 = r9.charAt(r0);
        r4 = 32;
        if (r0 != r4) goto L_0x0282;
    L_0x0240:
        r0 = r2 + -2;
        r0 = r9.charAt(r0);
        if (r0 != r4) goto L_0x0282;
    L_0x0248:
        r0 = r2 + -3;
        r0 = r9.charAt(r0);
        r4 = r2 + -3;
    L_0x0250:
        if (r4 <= 0) goto L_0x026a;
    L_0x0252:
        r6 = 34;
        if (r0 == r6) goto L_0x025f;
    L_0x0256:
        r6 = java.lang.Character.getType(r0);
        r12 = 22;
        if (r6 != r12) goto L_0x026a;
    L_0x025e:
        goto L_0x0261;
    L_0x025f:
        r12 = 22;
    L_0x0261:
        r6 = r4 + -1;
        r0 = r9.charAt(r6);
        r4 = r4 + -1;
        goto L_0x0250;
    L_0x026a:
        r4 = java.lang.Character.isLetter(r0);
        if (r4 != 0) goto L_0x0276;
    L_0x0270:
        r4 = java.lang.Character.isDigit(r0);
        if (r4 == 0) goto L_0x0282;
    L_0x0276:
        r4 = r2 + -2;
        r6 = r2 + -1;
        r12 = ".";
        r9.replace(r4, r6, r12);
        goto L_0x0282;
    L_0x0280:
        r2 = r19;
    L_0x0282:
        return r16;
    L_0x0283:
        r1 = 67;
        r2 = r24;
        if (r2 != r1) goto L_0x031c;
    L_0x0289:
        r1 = r25.hasNoModifiers();
        if (r1 != 0) goto L_0x029a;
    L_0x028f:
        r1 = 2;
        r3 = r25;
        r4 = r10;
        r1 = r3.hasModifiers(r1);
        if (r1 == 0) goto L_0x031f;
    L_0x0299:
        goto L_0x029d;
    L_0x029a:
        r3 = r25;
        r4 = r10;
    L_0x029d:
        if (r13 != r14) goto L_0x031f;
    L_0x029f:
        r1 = 1;
        r6 = android.text.method.TextKeyListener.LAST_TYPED;
        r6 = r9.getSpanEnd(r6);
        if (r6 != r13) goto L_0x02b1;
    L_0x02a8:
        r6 = r13 + -1;
        r6 = r9.charAt(r6);
        if (r6 == r0) goto L_0x02b1;
    L_0x02b0:
        r1 = 2;
    L_0x02b1:
        r0 = r13 - r1;
        r6 = android.text.method.QwertyKeyListener.Replaced.class;
        r0 = r9.getSpans(r0, r13, r6);
        r0 = (android.text.method.QwertyKeyListener.Replaced[]) r0;
        r6 = r0.length;
        if (r6 <= 0) goto L_0x0317;
    L_0x02be:
        r6 = 0;
        r10 = r0[r6];
        r10 = r9.getSpanStart(r10);
        r12 = r0[r6];
        r12 = r9.getSpanEnd(r12);
        r19 = r1;
        r1 = new java.lang.String;
        r20 = r0[r6];
        r6 = r20.mText;
        r1.<init>(r6);
        r6 = 0;
        r6 = r0[r6];
        r9.removeSpan(r6);
        if (r13 < r12) goto L_0x030d;
    L_0x02e0:
        r6 = android.text.method.TextKeyListener.INHIBIT_REPLACEMENT;
        r20 = r0;
        r0 = 34;
        r9.setSpan(r6, r12, r12, r0);
        r9.replace(r10, r12, r1);
        r0 = android.text.method.TextKeyListener.INHIBIT_REPLACEMENT;
        r0 = r9.getSpanStart(r0);
        r6 = r0 + -1;
        if (r6 < 0) goto L_0x0302;
    L_0x02f6:
        r6 = android.text.method.TextKeyListener.INHIBIT_REPLACEMENT;
        r12 = r0 + -1;
        r17 = r1;
        r1 = 33;
        r9.setSpan(r6, r12, r0, r1);
        goto L_0x0309;
    L_0x0302:
        r17 = r1;
        r1 = android.text.method.TextKeyListener.INHIBIT_REPLACEMENT;
        r9.removeSpan(r1);
    L_0x0309:
        android.text.method.MetaKeyKeyListener.adjustMetaAfterKeypress(r23);
        return r16;
    L_0x030d:
        r20 = r0;
        android.text.method.MetaKeyKeyListener.adjustMetaAfterKeypress(r23);
        r0 = super.onKeyDown(r22, r23, r24, r25);
        return r0;
    L_0x0317:
        r20 = r0;
        r19 = r1;
        goto L_0x031f;
    L_0x031c:
        r3 = r25;
        r4 = r10;
    L_0x031f:
        r0 = super.onKeyDown(r22, r23, r24, r25);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.method.QwertyKeyListener.onKeyDown(android.view.View, android.text.Editable, int, android.view.KeyEvent):boolean");
    }

    private String getReplacement(CharSequence src, int start, int end, View view) {
        String out;
        int len = end - start;
        boolean changecase = false;
        String replacement = AutoText.get(src, start, end, view);
        if (replacement == null) {
            replacement = AutoText.get(TextUtils.substring(src, start, end).toLowerCase(), 0, end - start, view);
            changecase = true;
            if (replacement == null) {
                return null;
            }
        }
        int caps = 0;
        if (changecase) {
            for (int j = start; j < end; j++) {
                if (Character.isUpperCase(src.charAt(j))) {
                    caps++;
                }
            }
        }
        if (caps == 0) {
            out = replacement;
        } else if (caps == 1) {
            out = toTitleCase(replacement);
        } else if (caps == len) {
            out = replacement.toUpperCase();
        } else {
            out = toTitleCase(replacement);
        }
        if (out.length() == len && TextUtils.regionMatches(src, start, out, 0, len)) {
            return null;
        }
        return out;
    }

    public static void markAsReplaced(Spannable content, int start, int end, String original) {
        Replaced[] repl = (Replaced[]) content.getSpans(0, content.length(), Replaced.class);
        for (Object removeSpan : repl) {
            content.removeSpan(removeSpan);
        }
        int a = original.length();
        char[] orig = new char[a];
        original.getChars(0, a, orig, 0);
        content.setSpan(new Replaced(orig), start, end, 33);
    }

    private boolean showCharacterPicker(View view, Editable content, char c, boolean insert, int count) {
        String set = (String) PICKER_SETS.get(c);
        if (set == null) {
            return false;
        }
        if (count == 1) {
            new CharacterPickerDialog(view.getContext(), view, content, set, insert).show();
        }
        return true;
    }

    private static String toTitleCase(String src) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Character.toUpperCase(src.charAt(0)));
        stringBuilder.append(src.substring(1));
        return stringBuilder.toString();
    }
}
