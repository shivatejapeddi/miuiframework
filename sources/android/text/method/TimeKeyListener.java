package android.text.method;

import android.text.format.DateFormat;
import com.android.internal.annotations.GuardedBy;
import java.util.HashMap;
import java.util.Locale;

public class TimeKeyListener extends NumberKeyListener {
    public static final char[] CHARACTERS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', DateFormat.AM_PM, DateFormat.MINUTE, 'p', ':'};
    private static final String SKELETON_12HOUR = "hms";
    private static final String SKELETON_24HOUR = "Hms";
    private static final String SYMBOLS_TO_IGNORE = "ahHKkms";
    @GuardedBy({"sLock"})
    private static final HashMap<Locale, TimeKeyListener> sInstanceCache = new HashMap();
    private static final Object sLock = new Object();
    private final char[] mCharacters;
    private final boolean mNeedsAdvancedInput;

    public int getInputType() {
        if (this.mNeedsAdvancedInput) {
            return 1;
        }
        return 36;
    }

    /* Access modifiers changed, original: protected */
    public char[] getAcceptedChars() {
        return this.mCharacters;
    }

    @Deprecated
    public TimeKeyListener() {
        this(null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0051  */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x002e  */
    public TimeKeyListener(java.util.Locale r7) {
        /*
        r6 = this;
        r6.<init>();
        r0 = new java.util.LinkedHashSet;
        r0.<init>();
        r1 = android.text.method.NumberKeyListener.addDigits(r0, r7);
        r2 = 1;
        r3 = 0;
        if (r1 == 0) goto L_0x002b;
    L_0x0010:
        r1 = android.text.method.NumberKeyListener.addAmPmChars(r0, r7);
        if (r1 == 0) goto L_0x002b;
    L_0x0016:
        r1 = "ahHKkms";
        r4 = "hms";
        r4 = android.text.method.NumberKeyListener.addFormatCharsFromSkeleton(r0, r7, r4, r1);
        if (r4 == 0) goto L_0x002b;
    L_0x0021:
        r4 = "Hms";
        r1 = android.text.method.NumberKeyListener.addFormatCharsFromSkeleton(r0, r7, r4, r1);
        if (r1 == 0) goto L_0x002b;
    L_0x0029:
        r1 = r2;
        goto L_0x002c;
    L_0x002b:
        r1 = r3;
    L_0x002c:
        if (r1 == 0) goto L_0x0051;
    L_0x002e:
        r4 = android.text.method.NumberKeyListener.collectionToArray(r0);
        r6.mCharacters = r4;
        if (r7 == 0) goto L_0x0045;
    L_0x0036:
        r4 = r7.getLanguage();
        r5 = "en";
        r4 = r5.equals(r4);
        if (r4 == 0) goto L_0x0045;
    L_0x0042:
        r6.mNeedsAdvancedInput = r3;
        goto L_0x0057;
    L_0x0045:
        r3 = CHARACTERS;
        r4 = r6.mCharacters;
        r3 = com.android.internal.util.ArrayUtils.containsAll(r3, r4);
        r2 = r2 ^ r3;
        r6.mNeedsAdvancedInput = r2;
        goto L_0x0057;
    L_0x0051:
        r2 = CHARACTERS;
        r6.mCharacters = r2;
        r6.mNeedsAdvancedInput = r3;
    L_0x0057:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.method.TimeKeyListener.<init>(java.util.Locale):void");
    }

    @Deprecated
    public static TimeKeyListener getInstance() {
        return getInstance(null);
    }

    public static TimeKeyListener getInstance(Locale locale) {
        TimeKeyListener instance;
        synchronized (sLock) {
            instance = (TimeKeyListener) sInstanceCache.get(locale);
            if (instance == null) {
                instance = new TimeKeyListener(locale);
                sInstanceCache.put(locale, instance);
            }
        }
        return instance;
    }
}
