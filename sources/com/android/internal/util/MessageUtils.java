package com.android.internal.util;

import android.util.SparseArray;

public class MessageUtils {
    private static final boolean DBG = false;
    public static final String[] DEFAULT_PREFIXES = new String[]{"CMD_", "EVENT_"};
    private static final String TAG = MessageUtils.class.getSimpleName();

    public static class DuplicateConstantError extends Error {
        private DuplicateConstantError() {
        }

        public DuplicateConstantError(String name1, String name2, int value) {
            super(String.format("Duplicate constant value: both %s and %s = %d", new Object[]{name1, name2, Integer.valueOf(value)}));
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x006f A:{Splitter:B:14:0x0047, ExcHandler: IllegalAccessException | SecurityException (e java.lang.Throwable)} */
    /* JADX WARNING: Failed to process nested try/catch */
    public static android.util.SparseArray<java.lang.String> findMessageNames(java.lang.Class[] r19, java.lang.String[] r20) {
        /*
        r1 = r19;
        r2 = r20;
        r0 = new android.util.SparseArray;
        r0.<init>();
        r3 = r0;
        r4 = r1.length;
        r6 = 0;
    L_0x000c:
        if (r6 >= r4) goto L_0x00a1;
    L_0x000e:
        r7 = r1[r6];
        r8 = r7.getName();
        r0 = r7.getDeclaredFields();	 Catch:{ SecurityException -> 0x007f }
        r9 = r0;
        r10 = r9.length;
        r11 = 0;
    L_0x001c:
        if (r11 >= r10) goto L_0x0099;
    L_0x001e:
        r12 = r9[r11];
        r13 = r12.getModifiers();
        r0 = java.lang.reflect.Modifier.isStatic(r13);
        r14 = 1;
        r0 = r0 ^ r14;
        r15 = java.lang.reflect.Modifier.isFinal(r13);
        r15 = r15 ^ r14;
        r0 = r0 | r15;
        if (r0 == 0) goto L_0x0033;
    L_0x0032:
        goto L_0x0078;
    L_0x0033:
        r15 = r12.getName();
        r5 = r2.length;
        r14 = 0;
    L_0x0039:
        if (r14 >= r5) goto L_0x0078;
    L_0x003b:
        r1 = r2[r14];
        r0 = r15.startsWith(r1);
        if (r0 != 0) goto L_0x0044;
    L_0x0043:
        goto L_0x0071;
    L_0x0044:
        r17 = r1;
        r1 = 1;
        r12.setAccessible(r1);	 Catch:{ IllegalAccessException | SecurityException -> 0x006f, IllegalAccessException | SecurityException -> 0x006f }
        r0 = 0;
        r0 = r12.getInt(r0);	 Catch:{ ExceptionInInitializerError | IllegalArgumentException -> 0x006d, ExceptionInInitializerError | IllegalArgumentException -> 0x006d }
        r16 = r3.get(r0);	 Catch:{ IllegalAccessException | SecurityException -> 0x006f, IllegalAccessException | SecurityException -> 0x006f }
        r16 = (java.lang.String) r16;	 Catch:{ IllegalAccessException | SecurityException -> 0x006f, IllegalAccessException | SecurityException -> 0x006f }
        r18 = r16;
        r1 = r18;
        if (r1 == 0) goto L_0x0069;
    L_0x005c:
        r18 = r1.equals(r15);	 Catch:{ IllegalAccessException | SecurityException -> 0x006f, IllegalAccessException | SecurityException -> 0x006f }
        if (r18 == 0) goto L_0x0063;
    L_0x0062:
        goto L_0x0069;
    L_0x0063:
        r2 = new com.android.internal.util.MessageUtils$DuplicateConstantError;	 Catch:{ IllegalAccessException | SecurityException -> 0x006f, IllegalAccessException | SecurityException -> 0x006f }
        r2.<init>(r15, r1, r0);	 Catch:{ IllegalAccessException | SecurityException -> 0x006f, IllegalAccessException | SecurityException -> 0x006f }
        throw r2;	 Catch:{ IllegalAccessException | SecurityException -> 0x006f, IllegalAccessException | SecurityException -> 0x006f }
    L_0x0069:
        r3.put(r0, r15);	 Catch:{ IllegalAccessException | SecurityException -> 0x006f, IllegalAccessException | SecurityException -> 0x006f }
        goto L_0x0071;
    L_0x006d:
        r0 = move-exception;
        goto L_0x0078;
    L_0x006f:
        r0 = move-exception;
    L_0x0071:
        r14 = r14 + 1;
        r1 = r19;
        r2 = r20;
        goto L_0x0039;
    L_0x0078:
        r11 = r11 + 1;
        r1 = r19;
        r2 = r20;
        goto L_0x001c;
    L_0x007f:
        r0 = move-exception;
        r1 = r0;
        r0 = r1;
        r1 = TAG;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r5 = "Can't list fields of class ";
        r2.append(r5);
        r2.append(r8);
        r2 = r2.toString();
        android.util.Log.e(r1, r2);
    L_0x0099:
        r6 = r6 + 1;
        r1 = r19;
        r2 = r20;
        goto L_0x000c;
    L_0x00a1:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.util.MessageUtils.findMessageNames(java.lang.Class[], java.lang.String[]):android.util.SparseArray");
    }

    public static SparseArray<String> findMessageNames(Class[] classNames) {
        return findMessageNames(classNames, DEFAULT_PREFIXES);
    }
}
