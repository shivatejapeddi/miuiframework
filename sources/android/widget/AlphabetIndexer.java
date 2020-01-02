package android.widget;

import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.wifi.WifiEnterpriseConfig;
import android.util.SparseIntArray;
import java.text.Collator;

public class AlphabetIndexer extends DataSetObserver implements SectionIndexer {
    private SparseIntArray mAlphaMap;
    protected CharSequence mAlphabet;
    private String[] mAlphabetArray = new String[this.mAlphabetLength];
    private int mAlphabetLength;
    private Collator mCollator;
    protected int mColumnIndex;
    protected Cursor mDataCursor;

    public AlphabetIndexer(Cursor cursor, int sortedColumnIndex, CharSequence alphabet) {
        int i;
        this.mDataCursor = cursor;
        this.mColumnIndex = sortedColumnIndex;
        this.mAlphabet = alphabet;
        this.mAlphabetLength = alphabet.length();
        int i2 = 0;
        while (true) {
            i = this.mAlphabetLength;
            if (i2 >= i) {
                break;
            }
            this.mAlphabetArray[i2] = Character.toString(this.mAlphabet.charAt(i2));
            i2++;
        }
        this.mAlphaMap = new SparseIntArray(i);
        if (cursor != null) {
            cursor.registerDataSetObserver(this);
        }
        this.mCollator = Collator.getInstance();
        this.mCollator.setStrength(0);
    }

    public Object[] getSections() {
        return this.mAlphabetArray;
    }

    public void setCursor(Cursor cursor) {
        Cursor cursor2 = this.mDataCursor;
        if (cursor2 != null) {
            cursor2.unregisterDataSetObserver(this);
        }
        this.mDataCursor = cursor;
        if (cursor != null) {
            this.mDataCursor.registerDataSetObserver(this);
        }
        this.mAlphaMap.clear();
    }

    /* Access modifiers changed, original: protected */
    public int compare(String word, String letter) {
        String firstLetter;
        if (word.length() == 0) {
            firstLetter = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        } else {
            firstLetter = word.substring(0, 1);
        }
        return this.mCollator.compare(firstLetter, letter);
    }

    /* JADX WARNING: Missing block: B:36:0x0085, code skipped:
            return 0;
     */
    public int getPositionForSection(int r14) {
        /*
        r13 = this;
        r0 = r13.mAlphaMap;
        r1 = r13.mDataCursor;
        r2 = 0;
        if (r1 == 0) goto L_0x0085;
    L_0x0007:
        r3 = r13.mAlphabet;
        if (r3 != 0) goto L_0x000d;
    L_0x000b:
        goto L_0x0085;
    L_0x000d:
        if (r14 > 0) goto L_0x0010;
    L_0x000f:
        return r2;
    L_0x0010:
        r2 = r13.mAlphabetLength;
        if (r14 < r2) goto L_0x0016;
    L_0x0014:
        r14 = r2 + -1;
    L_0x0016:
        r2 = r1.getPosition();
        r3 = r1.getCount();
        r4 = 0;
        r5 = r3;
        r6 = r13.mAlphabet;
        r6 = r6.charAt(r14);
        r7 = java.lang.Character.toString(r6);
        r8 = r6;
        r9 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r10 = r0.get(r8, r9);
        r11 = r10;
        if (r9 == r10) goto L_0x003a;
    L_0x0034:
        if (r11 >= 0) goto L_0x0039;
    L_0x0036:
        r11 = -r11;
        r5 = r11;
        goto L_0x003a;
    L_0x0039:
        return r11;
    L_0x003a:
        if (r14 <= 0) goto L_0x004e;
    L_0x003c:
        r10 = r13.mAlphabet;
        r12 = r14 + -1;
        r10 = r10.charAt(r12);
        r12 = r0.get(r10, r9);
        if (r12 == r9) goto L_0x004e;
    L_0x004a:
        r4 = java.lang.Math.abs(r12);
    L_0x004e:
        r9 = r5 + r4;
        r9 = r9 / 2;
    L_0x0052:
        if (r9 >= r5) goto L_0x007e;
    L_0x0054:
        r1.moveToPosition(r9);
        r10 = r13.mColumnIndex;
        r10 = r1.getString(r10);
        if (r10 != 0) goto L_0x0065;
    L_0x005f:
        if (r9 != 0) goto L_0x0062;
    L_0x0061:
        goto L_0x007e;
    L_0x0062:
        r9 = r9 + -1;
        goto L_0x0052;
    L_0x0065:
        r11 = r13.compare(r10, r7);
        if (r11 == 0) goto L_0x0075;
    L_0x006b:
        if (r11 >= 0) goto L_0x0073;
    L_0x006d:
        r4 = r9 + 1;
        if (r4 < r3) goto L_0x0079;
    L_0x0071:
        r9 = r3;
        goto L_0x007e;
    L_0x0073:
        r5 = r9;
        goto L_0x0079;
    L_0x0075:
        if (r4 != r9) goto L_0x0078;
    L_0x0077:
        goto L_0x007e;
    L_0x0078:
        r5 = r9;
    L_0x0079:
        r12 = r4 + r5;
        r9 = r12 / 2;
        goto L_0x0052;
    L_0x007e:
        r0.put(r8, r9);
        r1.moveToPosition(r2);
        return r9;
    L_0x0085:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.AlphabetIndexer.getPositionForSection(int):int");
    }

    public int getSectionForPosition(int position) {
        int savedCursorPos = this.mDataCursor.getPosition();
        this.mDataCursor.moveToPosition(position);
        String curName = this.mDataCursor.getString(this.mColumnIndex);
        this.mDataCursor.moveToPosition(savedCursorPos);
        for (int i = 0; i < this.mAlphabetLength; i++) {
            if (compare(curName, Character.toString(this.mAlphabet.charAt(i))) == 0) {
                return i;
            }
        }
        return 0;
    }

    public void onChanged() {
        super.onChanged();
        this.mAlphaMap.clear();
    }

    public void onInvalidated() {
        super.onInvalidated();
        this.mAlphaMap.clear();
    }
}
