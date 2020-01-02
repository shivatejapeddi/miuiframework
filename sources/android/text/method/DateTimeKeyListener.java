package android.text.method;

import android.text.format.DateFormat;
import com.android.internal.annotations.GuardedBy;
import java.util.HashMap;
import java.util.Locale;

public class DateTimeKeyListener extends NumberKeyListener {
    public static final char[] CHARACTERS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', DateFormat.AM_PM, DateFormat.MINUTE, 'p', ':', '/', '-', ' '};
    private static final String SKELETON_12HOUR = "yMdhms";
    private static final String SKELETON_24HOUR = "yMdHms";
    private static final String SYMBOLS_TO_IGNORE = "yMLdahHKkms";
    @GuardedBy({"sLock"})
    private static final HashMap<Locale, DateTimeKeyListener> sInstanceCache = new HashMap();
    private static final Object sLock = new Object();
    private final char[] mCharacters;
    private final boolean mNeedsAdvancedInput;

    public int getInputType() {
        if (this.mNeedsAdvancedInput) {
            return 1;
        }
        return 4;
    }

    /* Access modifiers changed, original: protected */
    public char[] getAcceptedChars() {
        return this.mCharacters;
    }

    @Deprecated
    public DateTimeKeyListener() {
        this(null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0053  */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0030  */
    public DateTimeKeyListener(java.util.Locale r7) {
        /*
        r6 = this;
        r6.<init>();
        r0 = new java.util.LinkedHashSet;
        r0.<init>();
        r1 = android.text.method.NumberKeyListener.addDigits(r0, r7);
        r2 = 1;
        r3 = 0;
        if (r1 == 0) goto L_0x002d;
    L_0x0010:
        r1 = android.text.method.NumberKeyListener.addAmPmChars(r0, r7);
        if (r1 == 0) goto L_0x002d;
    L_0x0016:
        r1 = "yMLdahHKkms";
        r4 = "yMdhms";
        r4 = android.text.method.NumberKeyListener.addFormatCharsFromSkeleton(r0, r7, r4, r1);
        if (r4 == 0) goto L_0x002d;
    L_0x0022:
        r4 = "yMdHms";
        r1 = android.text.method.NumberKeyListener.addFormatCharsFromSkeleton(r0, r7, r4, r1);
        if (r1 == 0) goto L_0x002d;
    L_0x002b:
        r1 = r2;
        goto L_0x002e;
    L_0x002d:
        r1 = r3;
    L_0x002e:
        if (r1 == 0) goto L_0x0053;
    L_0x0030:
        r4 = android.text.method.NumberKeyListener.collectionToArray(r0);
        r6.mCharacters = r4;
        if (r7 == 0) goto L_0x0047;
    L_0x0038:
        r4 = r7.getLanguage();
        r5 = "en";
        r4 = r5.equals(r4);
        if (r4 == 0) goto L_0x0047;
    L_0x0044:
        r6.mNeedsAdvancedInput = r3;
        goto L_0x0059;
    L_0x0047:
        r3 = CHARACTERS;
        r4 = r6.mCharacters;
        r3 = com.android.internal.util.ArrayUtils.containsAll(r3, r4);
        r2 = r2 ^ r3;
        r6.mNeedsAdvancedInput = r2;
        goto L_0x0059;
    L_0x0053:
        r2 = CHARACTERS;
        r6.mCharacters = r2;
        r6.mNeedsAdvancedInput = r3;
    L_0x0059:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.method.DateTimeKeyListener.<init>(java.util.Locale):void");
    }

    @Deprecated
    public static DateTimeKeyListener getInstance() {
        return getInstance(null);
    }

    public static DateTimeKeyListener getInstance(Locale locale) {
        DateTimeKeyListener instance;
        synchronized (sLock) {
            instance = (DateTimeKeyListener) sInstanceCache.get(locale);
            if (instance == null) {
                instance = new DateTimeKeyListener(locale);
                sInstanceCache.put(locale, instance);
            }
        }
        return instance;
    }
}
