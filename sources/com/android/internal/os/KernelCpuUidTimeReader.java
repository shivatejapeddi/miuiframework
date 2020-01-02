package com.android.internal.os;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.SystemClock;
import android.os.statistics.PerfEventConstants;
import android.util.IntArray;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.KernelCpuProcStringReader.ProcFileIterator;
import java.nio.CharBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class KernelCpuUidTimeReader<T> {
    protected static final boolean DEBUG = false;
    private static final long DEFAULT_MIN_TIME_BETWEEN_READ = 1000;
    private long mLastReadTimeMs = 0;
    final SparseArray<T> mLastTimes = new SparseArray();
    private long mMinTimeBetweenRead = 1000;
    final KernelCpuProcStringReader mReader;
    final String mTag = getClass().getSimpleName();
    final boolean mThrottle;

    public interface Callback<T> {
        void onUidCpuTime(int i, T t);
    }

    public static class KernelCpuUidActiveTimeReader extends KernelCpuUidTimeReader<Long> {
        private long[] mBuffer;
        private int mCores = 0;

        public KernelCpuUidActiveTimeReader(boolean throttle) {
            super(KernelCpuProcStringReader.getActiveTimeReaderInstance(), throttle);
        }

        @VisibleForTesting
        public KernelCpuUidActiveTimeReader(KernelCpuProcStringReader reader, boolean throttle) {
            super(reader, throttle);
        }

        /* Access modifiers changed, original: 0000 */
        /* JADX WARNING: Missing block: B:30:0x00a2, code skipped:
            if (r0 != null) goto L_0x00a4;
     */
        /* JADX WARNING: Missing block: B:31:0x00a4, code skipped:
            $closeResource(r1, r0);
     */
        public void readDeltaImpl(com.android.internal.os.KernelCpuUidTimeReader.Callback<java.lang.Long> r12) {
            /*
            r11 = this;
            r0 = r11.mReader;
            r1 = r11.mThrottle;
            r1 = r1 ^ 1;
            r0 = r0.open(r1);
            r1 = 0;
            r2 = r11.checkPrecondition(r0);	 Catch:{ all -> 0x009f }
            if (r2 != 0) goto L_0x0017;
        L_0x0011:
            if (r0 == 0) goto L_0x0016;
        L_0x0013:
            $closeResource(r1, r0);
        L_0x0016:
            return;
        L_0x0017:
            r2 = r0.nextLine();	 Catch:{ all -> 0x009f }
            r3 = r2;
            if (r2 == 0) goto L_0x009b;
        L_0x001e:
            r2 = r11.mBuffer;	 Catch:{ all -> 0x009f }
            r2 = com.android.internal.os.KernelCpuProcStringReader.asLongs(r3, r2);	 Catch:{ all -> 0x009f }
            r4 = r11.mBuffer;	 Catch:{ all -> 0x009f }
            r4 = r4.length;	 Catch:{ all -> 0x009f }
            if (r2 == r4) goto L_0x0044;
        L_0x0029:
            r2 = r11.mTag;	 Catch:{ all -> 0x009f }
            r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x009f }
            r4.<init>();	 Catch:{ all -> 0x009f }
            r5 = "Invalid line: ";
            r4.append(r5);	 Catch:{ all -> 0x009f }
            r5 = r3.toString();	 Catch:{ all -> 0x009f }
            r4.append(r5);	 Catch:{ all -> 0x009f }
            r4 = r4.toString();	 Catch:{ all -> 0x009f }
            android.util.Slog.wtf(r2, r4);	 Catch:{ all -> 0x009f }
            goto L_0x0017;
        L_0x0044:
            r2 = r11.mBuffer;	 Catch:{ all -> 0x009f }
            r4 = 0;
            r4 = r2[r4];	 Catch:{ all -> 0x009f }
            r2 = (int) r4;	 Catch:{ all -> 0x009f }
            r4 = r11.mBuffer;	 Catch:{ all -> 0x009f }
            r4 = sumActiveTime(r4);	 Catch:{ all -> 0x009f }
            r6 = 0;
            r8 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
            if (r8 <= 0) goto L_0x0099;
        L_0x0056:
            r8 = r11.mLastTimes;	 Catch:{ all -> 0x009f }
            r9 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x009f }
            r8 = r8.get(r2, r9);	 Catch:{ all -> 0x009f }
            r8 = (java.lang.Long) r8;	 Catch:{ all -> 0x009f }
            r8 = r8.longValue();	 Catch:{ all -> 0x009f }
            r8 = r4 - r8;
            r10 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1));
            if (r10 <= 0) goto L_0x007f;
        L_0x006c:
            r6 = r11.mLastTimes;	 Catch:{ all -> 0x009f }
            r7 = java.lang.Long.valueOf(r4);	 Catch:{ all -> 0x009f }
            r6.put(r2, r7);	 Catch:{ all -> 0x009f }
            if (r12 == 0) goto L_0x0099;
        L_0x0077:
            r6 = java.lang.Long.valueOf(r8);	 Catch:{ all -> 0x009f }
            r12.onUidCpuTime(r2, r6);	 Catch:{ all -> 0x009f }
            goto L_0x0099;
        L_0x007f:
            r6 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1));
            if (r6 >= 0) goto L_0x0099;
        L_0x0083:
            r6 = r11.mTag;	 Catch:{ all -> 0x009f }
            r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x009f }
            r7.<init>();	 Catch:{ all -> 0x009f }
            r10 = "Negative delta from active time proc: ";
            r7.append(r10);	 Catch:{ all -> 0x009f }
            r7.append(r8);	 Catch:{ all -> 0x009f }
            r7 = r7.toString();	 Catch:{ all -> 0x009f }
            android.util.Slog.e(r6, r7);	 Catch:{ all -> 0x009f }
        L_0x0099:
            goto L_0x0017;
        L_0x009b:
            $closeResource(r1, r0);
            return;
        L_0x009f:
            r1 = move-exception;
            throw r1;	 Catch:{ all -> 0x00a1 }
        L_0x00a1:
            r2 = move-exception;
            if (r0 == 0) goto L_0x00a7;
        L_0x00a4:
            $closeResource(r1, r0);
        L_0x00a7:
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.KernelCpuUidTimeReader$KernelCpuUidActiveTimeReader.readDeltaImpl(com.android.internal.os.KernelCpuUidTimeReader$Callback):void");
        }

        private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
            if (x0 != null) {
                try {
                    x1.close();
                    return;
                } catch (Throwable th) {
                    x0.addSuppressed(th);
                    return;
                }
            }
            x1.close();
        }

        /* Access modifiers changed, original: 0000 */
        /* JADX WARNING: Missing block: B:23:0x0065, code skipped:
            if (r0 != null) goto L_0x0067;
     */
        /* JADX WARNING: Missing block: B:24:0x0067, code skipped:
            $closeResource(r1, r0);
     */
        public void readAbsoluteImpl(com.android.internal.os.KernelCpuUidTimeReader.Callback<java.lang.Long> r9) {
            /*
            r8 = this;
            r0 = r8.mReader;
            r1 = r8.mThrottle;
            r1 = r1 ^ 1;
            r0 = r0.open(r1);
            r1 = 0;
            r2 = r8.checkPrecondition(r0);	 Catch:{ all -> 0x0062 }
            if (r2 != 0) goto L_0x0017;
        L_0x0011:
            if (r0 == 0) goto L_0x0016;
        L_0x0013:
            $closeResource(r1, r0);
        L_0x0016:
            return;
        L_0x0017:
            r2 = r0.nextLine();	 Catch:{ all -> 0x0062 }
            r3 = r2;
            if (r2 == 0) goto L_0x005e;
        L_0x001e:
            r2 = r8.mBuffer;	 Catch:{ all -> 0x0062 }
            r2 = com.android.internal.os.KernelCpuProcStringReader.asLongs(r3, r2);	 Catch:{ all -> 0x0062 }
            r4 = r8.mBuffer;	 Catch:{ all -> 0x0062 }
            r4 = r4.length;	 Catch:{ all -> 0x0062 }
            if (r2 == r4) goto L_0x0044;
        L_0x0029:
            r2 = r8.mTag;	 Catch:{ all -> 0x0062 }
            r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0062 }
            r4.<init>();	 Catch:{ all -> 0x0062 }
            r5 = "Invalid line: ";
            r4.append(r5);	 Catch:{ all -> 0x0062 }
            r5 = r3.toString();	 Catch:{ all -> 0x0062 }
            r4.append(r5);	 Catch:{ all -> 0x0062 }
            r4 = r4.toString();	 Catch:{ all -> 0x0062 }
            android.util.Slog.wtf(r2, r4);	 Catch:{ all -> 0x0062 }
            goto L_0x0017;
        L_0x0044:
            r2 = r8.mBuffer;	 Catch:{ all -> 0x0062 }
            r4 = sumActiveTime(r2);	 Catch:{ all -> 0x0062 }
            r6 = 0;
            r2 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
            if (r2 <= 0) goto L_0x005d;
        L_0x0050:
            r2 = r8.mBuffer;	 Catch:{ all -> 0x0062 }
            r6 = 0;
            r6 = r2[r6];	 Catch:{ all -> 0x0062 }
            r2 = (int) r6;	 Catch:{ all -> 0x0062 }
            r6 = java.lang.Long.valueOf(r4);	 Catch:{ all -> 0x0062 }
            r9.onUidCpuTime(r2, r6);	 Catch:{ all -> 0x0062 }
        L_0x005d:
            goto L_0x0017;
        L_0x005e:
            $closeResource(r1, r0);
            return;
        L_0x0062:
            r1 = move-exception;
            throw r1;	 Catch:{ all -> 0x0064 }
        L_0x0064:
            r2 = move-exception;
            if (r0 == 0) goto L_0x006a;
        L_0x0067:
            $closeResource(r1, r0);
        L_0x006a:
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.KernelCpuUidTimeReader$KernelCpuUidActiveTimeReader.readAbsoluteImpl(com.android.internal.os.KernelCpuUidTimeReader$Callback):void");
        }

        private static long sumActiveTime(long[] times) {
            double sum = 0.0d;
            for (int i = 1; i < times.length; i++) {
                sum += (((double) times[i]) * 10.0d) / ((double) i);
            }
            return (long) sum;
        }

        private boolean checkPrecondition(ProcFileIterator iter) {
            if (iter == null || !iter.hasNextLine()) {
                return false;
            }
            CharBuffer line = iter.nextLine();
            if (this.mCores > 0) {
                return true;
            }
            String str = line.toString();
            String str2 = "Malformed uid_concurrent_active_time line: ";
            String str3;
            if (str.startsWith("cpus:")) {
                int cores = Integer.parseInt(str.substring(5).trim(), 10);
                if (cores <= 0) {
                    str3 = this.mTag;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(str2);
                    stringBuilder.append(line);
                    Slog.wtf(str3, stringBuilder.toString());
                    return false;
                }
                this.mCores = cores;
                this.mBuffer = new long[(this.mCores + 1)];
                return true;
            }
            str3 = this.mTag;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str2);
            stringBuilder2.append(line);
            Slog.wtf(str3, stringBuilder2.toString());
            return false;
        }
    }

    public static class KernelCpuUidClusterTimeReader extends KernelCpuUidTimeReader<long[]> {
        private long[] mBuffer;
        private int[] mCoresOnClusters;
        private long[] mCurTime;
        private long[] mDeltaTime;
        private int mNumClusters;
        private int mNumCores;

        /*  JADX ERROR: JadxRuntimeException in pass: SSATransform
            jadx.core.utils.exceptions.JadxRuntimeException: Not initialized variable reg: 6, insn: 0x0024: INVOKE  (r5 int) = (r6 java.nio.CharBuffer), (r5 long[]) com.android.internal.os.KernelCpuProcStringReader.asLongs(java.nio.CharBuffer, long[]):int type: STATIC, block:B:10:0x0022, method: com.android.internal.os.KernelCpuUidTimeReader.KernelCpuUidClusterTimeReader.readDeltaImpl(com.android.internal.os.KernelCpuUidTimeReader$Callback):void
            	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:162)
            	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:184)
            	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:184)
            	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:184)
            	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:133)
            	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
            	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
            	at java.util.ArrayList.forEach(Unknown Source)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
            	at java.util.ArrayList.forEach(Unknown Source)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
            	at jadx.core.ProcessClass.process(ProcessClass.java:32)
            	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
            	at java.lang.Iterable.forEach(Unknown Source)
            	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
            	at jadx.core.ProcessClass.process(ProcessClass.java:37)
            	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
            	at jadx.api.JavaClass.decompile(JavaClass.java:62)
            */
        void readDeltaImpl(com.android.internal.os.KernelCpuUidTimeReader.Callback<long[]> r19) {
            /*
            r18 = this;
            r1 = r18;
            r2 = r19;
            r0 = r1.mReader;
            r3 = r1.mThrottle;
            r4 = 1;
            r3 = r3 ^ r4;
            r3 = r0.open(r3);
            r0 = 0;
            r5 = r1.checkPrecondition(r3);	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            if (r5 != 0) goto L_0x001b;
            if (r3 == 0) goto L_0x001a;
            $closeResource(r0, r3);
            return;
            r5 = r3.nextLine();	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r6 = r5;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            if (r5 == 0) goto L_0x00d2;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r5 = r1.mBuffer;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r5 = com.android.internal.os.KernelCpuProcStringReader.asLongs(r6, r5);	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r7 = r1.mBuffer;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r7 = r7.length;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            if (r5 == r7) goto L_0x0048;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r5 = r1.mTag;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r7.<init>();	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r8 = "Invalid line: ";	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r7.append(r8);	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r8 = r6.toString();	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r7.append(r8);	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r7 = r7.toString();	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            android.util.Slog.wtf(r5, r7);	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            goto L_0x001b;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r5 = r1.mBuffer;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r7 = 0;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r8 = r5[r7];	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r5 = (int) r8;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r8 = r1.mLastTimes;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r8 = r8.get(r5);	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r8 = (long[]) r8;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            if (r8 != 0) goto L_0x0062;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r9 = r1.mNumClusters;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r9 = new long[r9];	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r8 = r9;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r9 = r1.mLastTimes;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r9.put(r5, r8);	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r18.sumClusterTime();	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r9 = 1;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r10 = 0;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r11 = r7;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r12 = r1.mNumClusters;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            if (r11 >= r12) goto L_0x00b5;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r12 = r1.mDeltaTime;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r13 = r1.mCurTime;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r13 = r13[r11];	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r15 = r8[r11];	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r13 = r13 - r15;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r12[r11] = r13;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r12 = r1.mDeltaTime;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r12 = r12[r11];	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r14 = 0;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r12 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1));	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            if (r12 >= 0) goto L_0x00a0;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r12 = r1.mTag;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r13 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r13.<init>();	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r4 = "Negative delta from cluster time proc: ";	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r13.append(r4);	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r4 = r1.mDeltaTime;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r17 = r8;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r7 = r4[r11];	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r13.append(r7);	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r4 = r13.toString();	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            android.util.Slog.e(r12, r4);	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r4 = 0;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r9 = r4;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            goto L_0x00a2;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r17 = r8;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r4 = r1.mDeltaTime;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r7 = r4[r11];	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r4 = (r7 > r14 ? 1 : (r7 == r14 ? 0 : -1));	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            if (r4 <= 0) goto L_0x00ac;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r4 = 1;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            goto L_0x00ad;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r4 = 0;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r10 = r10 | r4;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r11 = r11 + 1;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r8 = r17;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r4 = 1;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r7 = 0;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            goto L_0x0068;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r17 = r8;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            if (r10 == 0) goto L_0x00cd;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            if (r9 == 0) goto L_0x00cd;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r4 = r1.mCurTime;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r7 = r1.mNumClusters;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r8 = r17;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r11 = 0;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            java.lang.System.arraycopy(r4, r11, r8, r11, r7);	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            if (r2 == 0) goto L_0x00cf;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r4 = r1.mDeltaTime;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r2.onUidCpuTime(r5, r4);	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            goto L_0x00cf;
            r8 = r17;
            r4 = 1;
            goto L_0x001b;
            $closeResource(r0, r3);
            return;
            r0 = move-exception;
            r4 = r0;
            throw r4;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r0 = move-exception;
            r5 = r0;
            if (r3 == 0) goto L_0x00e0;
            $closeResource(r4, r3);
            throw r5;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.KernelCpuUidTimeReader$KernelCpuUidClusterTimeReader.readDeltaImpl(com.android.internal.os.KernelCpuUidTimeReader$Callback):void");
        }

        public KernelCpuUidClusterTimeReader(boolean throttle) {
            super(KernelCpuProcStringReader.getClusterTimeReaderInstance(), throttle);
        }

        @VisibleForTesting
        public KernelCpuUidClusterTimeReader(KernelCpuProcStringReader reader, boolean throttle) {
            super(reader, throttle);
        }

        private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
            if (x0 != null) {
                try {
                    x1.close();
                    return;
                } catch (Throwable th) {
                    x0.addSuppressed(th);
                    return;
                }
            }
            x1.close();
        }

        /* Access modifiers changed, original: 0000 */
        /* JADX WARNING: Missing block: B:21:0x005a, code skipped:
            if (r0 != null) goto L_0x005c;
     */
        /* JADX WARNING: Missing block: B:22:0x005c, code skipped:
            $closeResource(r1, r0);
     */
        public void readAbsoluteImpl(com.android.internal.os.KernelCpuUidTimeReader.Callback<long[]> r7) {
            /*
            r6 = this;
            r0 = r6.mReader;
            r1 = r6.mThrottle;
            r1 = r1 ^ 1;
            r0 = r0.open(r1);
            r1 = 0;
            r2 = r6.checkPrecondition(r0);	 Catch:{ all -> 0x0057 }
            if (r2 != 0) goto L_0x0017;
        L_0x0011:
            if (r0 == 0) goto L_0x0016;
        L_0x0013:
            $closeResource(r1, r0);
        L_0x0016:
            return;
        L_0x0017:
            r2 = r0.nextLine();	 Catch:{ all -> 0x0057 }
            r3 = r2;
            if (r2 == 0) goto L_0x0053;
        L_0x001e:
            r2 = r6.mBuffer;	 Catch:{ all -> 0x0057 }
            r2 = com.android.internal.os.KernelCpuProcStringReader.asLongs(r3, r2);	 Catch:{ all -> 0x0057 }
            r4 = r6.mBuffer;	 Catch:{ all -> 0x0057 }
            r4 = r4.length;	 Catch:{ all -> 0x0057 }
            if (r2 == r4) goto L_0x0044;
        L_0x0029:
            r2 = r6.mTag;	 Catch:{ all -> 0x0057 }
            r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0057 }
            r4.<init>();	 Catch:{ all -> 0x0057 }
            r5 = "Invalid line: ";
            r4.append(r5);	 Catch:{ all -> 0x0057 }
            r5 = r3.toString();	 Catch:{ all -> 0x0057 }
            r4.append(r5);	 Catch:{ all -> 0x0057 }
            r4 = r4.toString();	 Catch:{ all -> 0x0057 }
            android.util.Slog.wtf(r2, r4);	 Catch:{ all -> 0x0057 }
            goto L_0x0017;
        L_0x0044:
            r6.sumClusterTime();	 Catch:{ all -> 0x0057 }
            r2 = r6.mBuffer;	 Catch:{ all -> 0x0057 }
            r4 = 0;
            r4 = r2[r4];	 Catch:{ all -> 0x0057 }
            r2 = (int) r4;	 Catch:{ all -> 0x0057 }
            r4 = r6.mCurTime;	 Catch:{ all -> 0x0057 }
            r7.onUidCpuTime(r2, r4);	 Catch:{ all -> 0x0057 }
            goto L_0x0017;
        L_0x0053:
            $closeResource(r1, r0);
            return;
        L_0x0057:
            r1 = move-exception;
            throw r1;	 Catch:{ all -> 0x0059 }
        L_0x0059:
            r2 = move-exception;
            if (r0 == 0) goto L_0x005f;
        L_0x005c:
            $closeResource(r1, r0);
        L_0x005f:
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.KernelCpuUidTimeReader$KernelCpuUidClusterTimeReader.readAbsoluteImpl(com.android.internal.os.KernelCpuUidTimeReader$Callback):void");
        }

        private void sumClusterTime() {
            int core = 1;
            for (int i = 0; i < this.mNumClusters; i++) {
                double sum = 0.0d;
                int j = 1;
                while (j <= this.mCoresOnClusters[i]) {
                    sum += (((double) this.mBuffer[core]) * 10.0d) / ((double) j);
                    j++;
                    core++;
                }
                this.mCurTime[i] = (long) sum;
            }
        }

        private boolean checkPrecondition(ProcFileIterator iter) {
            if (iter == null || !iter.hasNextLine()) {
                return false;
            }
            CharBuffer line = iter.nextLine();
            if (this.mNumClusters > 0) {
                return true;
            }
            String[] lineArray = line.toString().split(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            String str = "Malformed uid_concurrent_policy_time line: ";
            String str2;
            if (lineArray.length % 2 != 0) {
                str2 = this.mTag;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(line);
                Slog.wtf(str2, stringBuilder.toString());
                return false;
            }
            int[] clusters = new int[(lineArray.length / 2)];
            int cores = 0;
            int i = 0;
            while (i < clusters.length) {
                if (lineArray[i * 2].startsWith(PerfEventConstants.FIELD_SCHED_POLICY)) {
                    clusters[i] = Integer.parseInt(lineArray[(i * 2) + 1], 10);
                    cores += clusters[i];
                    i++;
                } else {
                    str2 = this.mTag;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(str);
                    stringBuilder2.append(line);
                    Slog.wtf(str2, stringBuilder2.toString());
                    return false;
                }
            }
            this.mNumClusters = clusters.length;
            this.mNumCores = cores;
            this.mCoresOnClusters = clusters;
            this.mBuffer = new long[(cores + 1)];
            int i2 = this.mNumClusters;
            this.mCurTime = new long[i2];
            this.mDeltaTime = new long[i2];
            return true;
        }
    }

    public static class KernelCpuUidFreqTimeReader extends KernelCpuUidTimeReader<long[]> {
        private static final int MAX_ERROR_COUNT = 5;
        private static final String UID_TIMES_PROC_FILE = "/proc/uid_time_in_state";
        private boolean mAllUidTimesAvailable;
        private long[] mBuffer;
        private long[] mCpuFreqs;
        private long[] mCurTimes;
        private long[] mDeltaTimes;
        private int mErrors;
        private int mFreqCount;
        private boolean mPerClusterTimesAvailable;
        private final Path mProcFilePath;

        /*  JADX ERROR: JadxRuntimeException in pass: SSATransform
            jadx.core.utils.exceptions.JadxRuntimeException: Not initialized variable reg: 6, insn: 0x0024: INVOKE  (r5 int) = (r6 java.nio.CharBuffer), (r5 long[]) com.android.internal.os.KernelCpuProcStringReader.asLongs(java.nio.CharBuffer, long[]):int type: STATIC, block:B:10:0x0022, method: com.android.internal.os.KernelCpuUidTimeReader.KernelCpuUidFreqTimeReader.readDeltaImpl(com.android.internal.os.KernelCpuUidTimeReader$Callback):void
            	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:162)
            	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:184)
            	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:184)
            	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:184)
            	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:133)
            	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
            	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
            	at java.util.ArrayList.forEach(Unknown Source)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
            	at java.util.ArrayList.forEach(Unknown Source)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
            	at jadx.core.ProcessClass.process(ProcessClass.java:32)
            	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
            	at java.lang.Iterable.forEach(Unknown Source)
            	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
            	at jadx.core.ProcessClass.process(ProcessClass.java:37)
            	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
            	at jadx.api.JavaClass.decompile(JavaClass.java:62)
            */
        void readDeltaImpl(com.android.internal.os.KernelCpuUidTimeReader.Callback<long[]> r19) {
            /*
            r18 = this;
            r1 = r18;
            r2 = r19;
            r0 = r1.mReader;
            r3 = r1.mThrottle;
            r4 = 1;
            r3 = r3 ^ r4;
            r3 = r0.open(r3);
            r0 = 0;
            r5 = r1.checkPrecondition(r3);	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            if (r5 != 0) goto L_0x001b;
            if (r3 == 0) goto L_0x001a;
            $closeResource(r0, r3);
            return;
            r5 = r3.nextLine();	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r6 = r5;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            if (r5 == 0) goto L_0x00d2;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r5 = r1.mBuffer;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r5 = com.android.internal.os.KernelCpuProcStringReader.asLongs(r6, r5);	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r7 = r1.mBuffer;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r7 = r7.length;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            if (r5 == r7) goto L_0x0048;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r5 = r1.mTag;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r7.<init>();	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r8 = "Invalid line: ";	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r7.append(r8);	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r8 = r6.toString();	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r7.append(r8);	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r7 = r7.toString();	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            android.util.Slog.wtf(r5, r7);	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            goto L_0x001b;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r5 = r1.mBuffer;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r7 = 0;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r8 = r5[r7];	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r5 = (int) r8;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r8 = r1.mLastTimes;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r8 = r8.get(r5);	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r8 = (long[]) r8;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            if (r8 != 0) goto L_0x0062;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r9 = r1.mFreqCount;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r9 = new long[r9];	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r8 = r9;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r9 = r1.mLastTimes;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r9.put(r5, r8);	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r18.copyToCurTimes();	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r9 = 0;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r10 = 1;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r11 = r7;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r12 = r1.mFreqCount;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            if (r11 >= r12) goto L_0x00b5;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r12 = r1.mDeltaTimes;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r13 = r1.mCurTimes;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r13 = r13[r11];	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r15 = r8[r11];	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r13 = r13 - r15;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r12[r11] = r13;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r12 = r1.mDeltaTimes;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r12 = r12[r11];	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r14 = 0;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r12 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1));	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            if (r12 >= 0) goto L_0x00a0;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r12 = r1.mTag;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r13 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r13.<init>();	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r4 = "Negative delta from freq time proc: ";	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r13.append(r4);	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r4 = r1.mDeltaTimes;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r17 = r8;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r7 = r4[r11];	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r13.append(r7);	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r4 = r13.toString();	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            android.util.Slog.e(r12, r4);	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r4 = 0;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r10 = r4;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            goto L_0x00a2;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r17 = r8;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r4 = r1.mDeltaTimes;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r7 = r4[r11];	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r4 = (r7 > r14 ? 1 : (r7 == r14 ? 0 : -1));	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            if (r4 <= 0) goto L_0x00ac;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r4 = 1;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            goto L_0x00ad;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r4 = 0;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r9 = r9 | r4;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r11 = r11 + 1;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r8 = r17;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r4 = 1;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r7 = 0;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            goto L_0x0068;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r17 = r8;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            if (r9 == 0) goto L_0x00cd;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            if (r10 == 0) goto L_0x00cd;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r4 = r1.mCurTimes;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r7 = r1.mFreqCount;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r8 = r17;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r11 = 0;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            java.lang.System.arraycopy(r4, r11, r8, r11, r7);	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            if (r2 == 0) goto L_0x00cf;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r4 = r1.mDeltaTimes;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r2.onUidCpuTime(r5, r4);	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            goto L_0x00cf;
            r8 = r17;
            r4 = 1;
            goto L_0x001b;
            $closeResource(r0, r3);
            return;
            r0 = move-exception;
            r4 = r0;
            throw r4;	 Catch:{ all -> 0x00d6, all -> 0x00d9 }
            r0 = move-exception;
            r5 = r0;
            if (r3 == 0) goto L_0x00e0;
            $closeResource(r4, r3);
            throw r5;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.KernelCpuUidTimeReader$KernelCpuUidFreqTimeReader.readDeltaImpl(com.android.internal.os.KernelCpuUidTimeReader$Callback):void");
        }

        public KernelCpuUidFreqTimeReader(boolean throttle) {
            this(UID_TIMES_PROC_FILE, KernelCpuProcStringReader.getFreqTimeReaderInstance(), throttle);
        }

        @VisibleForTesting
        public KernelCpuUidFreqTimeReader(String procFile, KernelCpuProcStringReader reader, boolean throttle) {
            super(reader, throttle);
            this.mFreqCount = 0;
            this.mErrors = 0;
            this.mAllUidTimesAvailable = true;
            this.mProcFilePath = Paths.get(procFile, new String[0]);
        }

        public boolean perClusterTimesAvailable() {
            return this.mPerClusterTimesAvailable;
        }

        public boolean allUidTimesAvailable() {
            return this.mAllUidTimesAvailable;
        }

        public SparseArray<long[]> getAllUidCpuFreqTimeMs() {
            return this.mLastTimes;
        }

        /* JADX WARNING: Missing block: B:33:0x0077, code skipped:
            if (r4 != null) goto L_0x0079;
     */
        /* JADX WARNING: Missing block: B:35:?, code skipped:
            $closeResource(r5, r4);
     */
        public long[] readFreqs(com.android.internal.os.PowerProfile r8) {
            /*
            r7 = this;
            com.android.internal.util.Preconditions.checkNotNull(r8);
            r0 = r7.mCpuFreqs;
            if (r0 == 0) goto L_0x0008;
        L_0x0007:
            return r0;
        L_0x0008:
            r0 = r7.mAllUidTimesAvailable;
            r1 = 0;
            if (r0 != 0) goto L_0x000e;
        L_0x000d:
            return r1;
        L_0x000e:
            r0 = android.os.StrictMode.allowThreadDiskReadsMask();
            r2 = 0;
            r3 = 1;
            r4 = r7.mProcFilePath;	 Catch:{ IOException -> 0x007f }
            r4 = java.nio.file.Files.newBufferedReader(r4);	 Catch:{ IOException -> 0x007f }
            r5 = r4.readLine();	 Catch:{ all -> 0x0074 }
            r5 = r7.readFreqs(r5);	 Catch:{ all -> 0x0074 }
            if (r5 != 0) goto L_0x002c;
            $closeResource(r1, r4);	 Catch:{ IOException -> 0x007f }
            android.os.StrictMode.setThreadPolicyMask(r0);
            return r1;
        L_0x002c:
            $closeResource(r1, r4);	 Catch:{ IOException -> 0x007f }
            android.os.StrictMode.setThreadPolicyMask(r0);
            r1 = r7.extractClusterInfoFromProcFileFreqs();
            r4 = r8.getNumCpuClusters();
            r5 = r1.size();
            if (r5 != r4) goto L_0x0057;
        L_0x0041:
            r7.mPerClusterTimesAvailable = r3;
            r3 = 0;
        L_0x0044:
            if (r3 >= r4) goto L_0x0056;
        L_0x0046:
            r5 = r1.get(r3);
            r6 = r8.getNumSpeedStepsInCpuCluster(r3);
            if (r5 == r6) goto L_0x0053;
        L_0x0050:
            r7.mPerClusterTimesAvailable = r2;
            goto L_0x0056;
        L_0x0053:
            r3 = r3 + 1;
            goto L_0x0044;
        L_0x0056:
            goto L_0x0059;
        L_0x0057:
            r7.mPerClusterTimesAvailable = r2;
        L_0x0059:
            r2 = r7.mTag;
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r5 = "mPerClusterTimesAvailable=";
            r3.append(r5);
            r5 = r7.mPerClusterTimesAvailable;
            r3.append(r5);
            r3 = r3.toString();
            android.util.Slog.i(r2, r3);
            r2 = r7.mCpuFreqs;
            return r2;
        L_0x0074:
            r5 = move-exception;
            throw r5;	 Catch:{ all -> 0x0076 }
        L_0x0076:
            r6 = move-exception;
            if (r4 == 0) goto L_0x007c;
        L_0x0079:
            $closeResource(r5, r4);	 Catch:{ IOException -> 0x007f }
        L_0x007c:
            throw r6;	 Catch:{ IOException -> 0x007f }
        L_0x007d:
            r1 = move-exception;
            goto L_0x00a5;
        L_0x007f:
            r4 = move-exception;
            r5 = r7.mErrors;	 Catch:{ all -> 0x007d }
            r5 = r5 + r3;
            r7.mErrors = r5;	 Catch:{ all -> 0x007d }
            r3 = 5;
            if (r5 < r3) goto L_0x008a;
        L_0x0088:
            r7.mAllUidTimesAvailable = r2;	 Catch:{ all -> 0x007d }
        L_0x008a:
            r2 = r7.mTag;	 Catch:{ all -> 0x007d }
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x007d }
            r3.<init>();	 Catch:{ all -> 0x007d }
            r5 = "Failed to read /proc/uid_time_in_state: ";
            r3.append(r5);	 Catch:{ all -> 0x007d }
            r3.append(r4);	 Catch:{ all -> 0x007d }
            r3 = r3.toString();	 Catch:{ all -> 0x007d }
            android.util.Slog.e(r2, r3);	 Catch:{ all -> 0x007d }
            android.os.StrictMode.setThreadPolicyMask(r0);
            return r1;
        L_0x00a5:
            android.os.StrictMode.setThreadPolicyMask(r0);
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.KernelCpuUidTimeReader$KernelCpuUidFreqTimeReader.readFreqs(com.android.internal.os.PowerProfile):long[]");
        }

        private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
            if (x0 != null) {
                try {
                    x1.close();
                    return;
                } catch (Throwable th) {
                    x0.addSuppressed(th);
                    return;
                }
            }
            x1.close();
        }

        private long[] readFreqs(String line) {
            if (line == null) {
                return null;
            }
            String[] lineArray = line.split(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            if (lineArray.length <= 1) {
                String str = this.mTag;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Malformed freq line: ");
                stringBuilder.append(line);
                Slog.wtf(str, stringBuilder.toString());
                return null;
            }
            this.mFreqCount = lineArray.length - 1;
            int i = this.mFreqCount;
            this.mCpuFreqs = new long[i];
            this.mCurTimes = new long[i];
            this.mDeltaTimes = new long[i];
            this.mBuffer = new long[(i + 1)];
            for (i = 0; i < this.mFreqCount; i++) {
                this.mCpuFreqs[i] = Long.parseLong(lineArray[i + 1], 10);
            }
            return this.mCpuFreqs;
        }

        /* Access modifiers changed, original: 0000 */
        /* JADX WARNING: Missing block: B:21:0x005a, code skipped:
            if (r0 != null) goto L_0x005c;
     */
        /* JADX WARNING: Missing block: B:22:0x005c, code skipped:
            $closeResource(r1, r0);
     */
        public void readAbsoluteImpl(com.android.internal.os.KernelCpuUidTimeReader.Callback<long[]> r7) {
            /*
            r6 = this;
            r0 = r6.mReader;
            r1 = r6.mThrottle;
            r1 = r1 ^ 1;
            r0 = r0.open(r1);
            r1 = 0;
            r2 = r6.checkPrecondition(r0);	 Catch:{ all -> 0x0057 }
            if (r2 != 0) goto L_0x0017;
        L_0x0011:
            if (r0 == 0) goto L_0x0016;
        L_0x0013:
            $closeResource(r1, r0);
        L_0x0016:
            return;
        L_0x0017:
            r2 = r0.nextLine();	 Catch:{ all -> 0x0057 }
            r3 = r2;
            if (r2 == 0) goto L_0x0053;
        L_0x001e:
            r2 = r6.mBuffer;	 Catch:{ all -> 0x0057 }
            r2 = com.android.internal.os.KernelCpuProcStringReader.asLongs(r3, r2);	 Catch:{ all -> 0x0057 }
            r4 = r6.mBuffer;	 Catch:{ all -> 0x0057 }
            r4 = r4.length;	 Catch:{ all -> 0x0057 }
            if (r2 == r4) goto L_0x0044;
        L_0x0029:
            r2 = r6.mTag;	 Catch:{ all -> 0x0057 }
            r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0057 }
            r4.<init>();	 Catch:{ all -> 0x0057 }
            r5 = "Invalid line: ";
            r4.append(r5);	 Catch:{ all -> 0x0057 }
            r5 = r3.toString();	 Catch:{ all -> 0x0057 }
            r4.append(r5);	 Catch:{ all -> 0x0057 }
            r4 = r4.toString();	 Catch:{ all -> 0x0057 }
            android.util.Slog.wtf(r2, r4);	 Catch:{ all -> 0x0057 }
            goto L_0x0017;
        L_0x0044:
            r6.copyToCurTimes();	 Catch:{ all -> 0x0057 }
            r2 = r6.mBuffer;	 Catch:{ all -> 0x0057 }
            r4 = 0;
            r4 = r2[r4];	 Catch:{ all -> 0x0057 }
            r2 = (int) r4;	 Catch:{ all -> 0x0057 }
            r4 = r6.mCurTimes;	 Catch:{ all -> 0x0057 }
            r7.onUidCpuTime(r2, r4);	 Catch:{ all -> 0x0057 }
            goto L_0x0017;
        L_0x0053:
            $closeResource(r1, r0);
            return;
        L_0x0057:
            r1 = move-exception;
            throw r1;	 Catch:{ all -> 0x0059 }
        L_0x0059:
            r2 = move-exception;
            if (r0 == 0) goto L_0x005f;
        L_0x005c:
            $closeResource(r1, r0);
        L_0x005f:
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.KernelCpuUidTimeReader$KernelCpuUidFreqTimeReader.readAbsoluteImpl(com.android.internal.os.KernelCpuUidTimeReader$Callback):void");
        }

        private void copyToCurTimes() {
            for (int i = 0; i < this.mFreqCount; i++) {
                this.mCurTimes[i] = this.mBuffer[i + 1] * 10;
            }
        }

        private boolean checkPrecondition(ProcFileIterator iter) {
            boolean z = false;
            if (iter == null || !iter.hasNextLine()) {
                return false;
            }
            CharBuffer line = iter.nextLine();
            if (this.mCpuFreqs != null) {
                return true;
            }
            if (readFreqs(line.toString()) != null) {
                z = true;
            }
            return z;
        }

        private IntArray extractClusterInfoFromProcFileFreqs() {
            IntArray numClusterFreqs = new IntArray();
            int freqsFound = 0;
            int i = 0;
            while (true) {
                int i2 = this.mFreqCount;
                if (i >= i2) {
                    return numClusterFreqs;
                }
                freqsFound++;
                if (i + 1 != i2) {
                    long[] jArr = this.mCpuFreqs;
                    if (jArr[i + 1] > jArr[i]) {
                        i++;
                    }
                }
                numClusterFreqs.add(freqsFound);
                freqsFound = 0;
                i++;
            }
        }
    }

    public static class KernelCpuUidUserSysTimeReader extends KernelCpuUidTimeReader<long[]> {
        private static final String REMOVE_UID_PROC_FILE = "/proc/uid_cputime/remove_uid_range";
        private final long[] mBuffer;
        private final long[] mUsrSysTime;

        public KernelCpuUidUserSysTimeReader(boolean throttle) {
            super(KernelCpuProcStringReader.getUserSysTimeReaderInstance(), throttle);
            this.mBuffer = new long[4];
            this.mUsrSysTime = new long[2];
        }

        @VisibleForTesting
        public KernelCpuUidUserSysTimeReader(KernelCpuProcStringReader reader, boolean throttle) {
            super(reader, throttle);
            this.mBuffer = new long[4];
            this.mUsrSysTime = new long[2];
        }

        /* Access modifiers changed, original: 0000 */
        /* JADX WARNING: Removed duplicated region for block: B:16:0x007f A:{Catch:{ all -> 0x00f0, all -> 0x00f3 }} */
        public void readDeltaImpl(com.android.internal.os.KernelCpuUidTimeReader.Callback<long[]> r20) {
            /*
            r19 = this;
            r1 = r19;
            r2 = r20;
            r0 = " s=";
            r3 = r1.mReader;
            r4 = r1.mThrottle;
            r5 = 1;
            r4 = r4 ^ r5;
            r3 = r3.open(r4);
            r4 = 0;
            if (r3 != 0) goto L_0x0019;
        L_0x0013:
            if (r3 == 0) goto L_0x0018;
        L_0x0015:
            $closeResource(r4, r3);
        L_0x0018:
            return;
        L_0x0019:
            r6 = r3.nextLine();	 Catch:{ all -> 0x00f0 }
            r7 = r6;
            if (r6 == 0) goto L_0x00eb;
        L_0x0020:
            r6 = r1.mBuffer;	 Catch:{ all -> 0x00f0 }
            r6 = com.android.internal.os.KernelCpuProcStringReader.asLongs(r7, r6);	 Catch:{ all -> 0x00f0 }
            r8 = 3;
            if (r6 >= r8) goto L_0x0044;
        L_0x0029:
            r6 = r1.mTag;	 Catch:{ all -> 0x00f0 }
            r8 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00f0 }
            r8.<init>();	 Catch:{ all -> 0x00f0 }
            r9 = "Invalid line: ";
            r8.append(r9);	 Catch:{ all -> 0x00f0 }
            r9 = r7.toString();	 Catch:{ all -> 0x00f0 }
            r8.append(r9);	 Catch:{ all -> 0x00f0 }
            r8 = r8.toString();	 Catch:{ all -> 0x00f0 }
            android.util.Slog.wtf(r6, r8);	 Catch:{ all -> 0x00f0 }
            goto L_0x0019;
        L_0x0044:
            r6 = r1.mBuffer;	 Catch:{ all -> 0x00f0 }
            r8 = 0;
            r9 = r6[r8];	 Catch:{ all -> 0x00f0 }
            r6 = (int) r9;	 Catch:{ all -> 0x00f0 }
            r9 = r1.mLastTimes;	 Catch:{ all -> 0x00f0 }
            r9 = r9.get(r6);	 Catch:{ all -> 0x00f0 }
            r9 = (long[]) r9;	 Catch:{ all -> 0x00f0 }
            r10 = 2;
            if (r9 != 0) goto L_0x005d;
        L_0x0055:
            r11 = new long[r10];	 Catch:{ all -> 0x00f0 }
            r9 = r11;
            r11 = r1.mLastTimes;	 Catch:{ all -> 0x00f0 }
            r11.put(r6, r9);	 Catch:{ all -> 0x00f0 }
        L_0x005d:
            r11 = r1.mBuffer;	 Catch:{ all -> 0x00f0 }
            r11 = r11[r5];	 Catch:{ all -> 0x00f0 }
            r13 = r1.mBuffer;	 Catch:{ all -> 0x00f0 }
            r13 = r13[r10];	 Catch:{ all -> 0x00f0 }
            r10 = r1.mUsrSysTime;	 Catch:{ all -> 0x00f0 }
            r15 = r9[r8];	 Catch:{ all -> 0x00f0 }
            r15 = r11 - r15;
            r10[r8] = r15;	 Catch:{ all -> 0x00f0 }
            r10 = r1.mUsrSysTime;	 Catch:{ all -> 0x00f0 }
            r15 = r9[r5];	 Catch:{ all -> 0x00f0 }
            r15 = r13 - r15;
            r10[r5] = r15;	 Catch:{ all -> 0x00f0 }
            r10 = r1.mUsrSysTime;	 Catch:{ all -> 0x00f0 }
            r15 = r10[r8];	 Catch:{ all -> 0x00f0 }
            r17 = 0;
            r10 = (r15 > r17 ? 1 : (r15 == r17 ? 0 : -1));
            if (r10 < 0) goto L_0x00a9;
        L_0x007f:
            r10 = r1.mUsrSysTime;	 Catch:{ all -> 0x00f0 }
            r15 = r10[r5];	 Catch:{ all -> 0x00f0 }
            r10 = (r15 > r17 ? 1 : (r15 == r17 ? 0 : -1));
            if (r10 >= 0) goto L_0x0088;
        L_0x0087:
            goto L_0x00a9;
        L_0x0088:
            r10 = r1.mUsrSysTime;	 Catch:{ all -> 0x00f0 }
            r15 = r10[r8];	 Catch:{ all -> 0x00f0 }
            r10 = (r15 > r17 ? 1 : (r15 == r17 ? 0 : -1));
            if (r10 > 0) goto L_0x009c;
        L_0x0090:
            r10 = r1.mUsrSysTime;	 Catch:{ all -> 0x00f0 }
            r15 = r10[r5];	 Catch:{ all -> 0x00f0 }
            r10 = (r15 > r17 ? 1 : (r15 == r17 ? 0 : -1));
            if (r10 <= 0) goto L_0x0099;
        L_0x0098:
            goto L_0x009c;
        L_0x0099:
            r17 = r6;
            goto L_0x00e2;
        L_0x009c:
            if (r2 == 0) goto L_0x00a6;
        L_0x009e:
            r10 = r1.mUsrSysTime;	 Catch:{ all -> 0x00f0 }
            r2.onUidCpuTime(r6, r10);	 Catch:{ all -> 0x00f0 }
            r17 = r6;
            goto L_0x00e2;
        L_0x00a6:
            r17 = r6;
            goto L_0x00e2;
        L_0x00a9:
            r10 = r1.mTag;	 Catch:{ all -> 0x00f0 }
            r15 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00f0 }
            r15.<init>();	 Catch:{ all -> 0x00f0 }
            r4 = "Negative user/sys time delta for UID=";
            r15.append(r4);	 Catch:{ all -> 0x00f0 }
            r15.append(r6);	 Catch:{ all -> 0x00f0 }
            r4 = "\nPrev times: u=";
            r15.append(r4);	 Catch:{ all -> 0x00f0 }
            r17 = r6;
            r5 = r9[r8];	 Catch:{ all -> 0x00f0 }
            r15.append(r5);	 Catch:{ all -> 0x00f0 }
            r15.append(r0);	 Catch:{ all -> 0x00f0 }
            r4 = 1;
            r5 = r9[r4];	 Catch:{ all -> 0x00f0 }
            r15.append(r5);	 Catch:{ all -> 0x00f0 }
            r5 = " Curr times: u=";
            r15.append(r5);	 Catch:{ all -> 0x00f0 }
            r15.append(r11);	 Catch:{ all -> 0x00f0 }
            r15.append(r0);	 Catch:{ all -> 0x00f0 }
            r15.append(r13);	 Catch:{ all -> 0x00f0 }
            r5 = r15.toString();	 Catch:{ all -> 0x00f0 }
            android.util.Slog.e(r10, r5);	 Catch:{ all -> 0x00f0 }
        L_0x00e2:
            r9[r8] = r11;	 Catch:{ all -> 0x00f0 }
            r4 = 1;
            r9[r4] = r13;	 Catch:{ all -> 0x00f0 }
            r5 = r4;
            r4 = 0;
            goto L_0x0019;
        L_0x00eb:
            r0 = 0;
            $closeResource(r0, r3);
            return;
        L_0x00f0:
            r0 = move-exception;
            r4 = r0;
            throw r4;	 Catch:{ all -> 0x00f3 }
        L_0x00f3:
            r0 = move-exception;
            r5 = r0;
            $closeResource(r4, r3);
            throw r5;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.KernelCpuUidTimeReader$KernelCpuUidUserSysTimeReader.readDeltaImpl(com.android.internal.os.KernelCpuUidTimeReader$Callback):void");
        }

        private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
            if (x0 != null) {
                try {
                    x1.close();
                    return;
                } catch (Throwable th) {
                    x0.addSuppressed(th);
                    return;
                }
            }
            x1.close();
        }

        /* Access modifiers changed, original: 0000 */
        /* JADX WARNING: Missing block: B:19:0x0062, code skipped:
            $closeResource(r1, r0);
     */
        public void readAbsoluteImpl(com.android.internal.os.KernelCpuUidTimeReader.Callback<long[]> r9) {
            /*
            r8 = this;
            r0 = r8.mReader;
            r1 = r8.mThrottle;
            r2 = 1;
            r1 = r1 ^ r2;
            r0 = r0.open(r1);
            r1 = 0;
            if (r0 != 0) goto L_0x0013;
        L_0x000d:
            if (r0 == 0) goto L_0x0012;
        L_0x000f:
            $closeResource(r1, r0);
        L_0x0012:
            return;
        L_0x0013:
            r3 = r0.nextLine();	 Catch:{ all -> 0x005f }
            r4 = r3;
            if (r3 == 0) goto L_0x005b;
        L_0x001a:
            r3 = r8.mBuffer;	 Catch:{ all -> 0x005f }
            r3 = com.android.internal.os.KernelCpuProcStringReader.asLongs(r4, r3);	 Catch:{ all -> 0x005f }
            r5 = 3;
            if (r3 >= r5) goto L_0x003e;
        L_0x0023:
            r3 = r8.mTag;	 Catch:{ all -> 0x005f }
            r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x005f }
            r5.<init>();	 Catch:{ all -> 0x005f }
            r6 = "Invalid line: ";
            r5.append(r6);	 Catch:{ all -> 0x005f }
            r6 = r4.toString();	 Catch:{ all -> 0x005f }
            r5.append(r6);	 Catch:{ all -> 0x005f }
            r5 = r5.toString();	 Catch:{ all -> 0x005f }
            android.util.Slog.wtf(r3, r5);	 Catch:{ all -> 0x005f }
            goto L_0x0013;
        L_0x003e:
            r3 = r8.mUsrSysTime;	 Catch:{ all -> 0x005f }
            r5 = r8.mBuffer;	 Catch:{ all -> 0x005f }
            r5 = r5[r2];	 Catch:{ all -> 0x005f }
            r7 = 0;
            r3[r7] = r5;	 Catch:{ all -> 0x005f }
            r3 = r8.mUsrSysTime;	 Catch:{ all -> 0x005f }
            r5 = r8.mBuffer;	 Catch:{ all -> 0x005f }
            r6 = 2;
            r5 = r5[r6];	 Catch:{ all -> 0x005f }
            r3[r2] = r5;	 Catch:{ all -> 0x005f }
            r3 = r8.mBuffer;	 Catch:{ all -> 0x005f }
            r5 = r3[r7];	 Catch:{ all -> 0x005f }
            r3 = (int) r5;	 Catch:{ all -> 0x005f }
            r5 = r8.mUsrSysTime;	 Catch:{ all -> 0x005f }
            r9.onUidCpuTime(r3, r5);	 Catch:{ all -> 0x005f }
            goto L_0x0013;
        L_0x005b:
            $closeResource(r1, r0);
            return;
        L_0x005f:
            r1 = move-exception;
            throw r1;	 Catch:{ all -> 0x0061 }
        L_0x0061:
            r2 = move-exception;
            $closeResource(r1, r0);
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.KernelCpuUidTimeReader$KernelCpuUidUserSysTimeReader.readAbsoluteImpl(com.android.internal.os.KernelCpuUidTimeReader$Callback):void");
        }

        public void removeUid(int uid) {
            super.removeUid(uid);
            removeUidsFromKernelModule(uid, uid);
        }

        public void removeUidsInRange(int startUid, int endUid) {
            super.removeUidsInRange(startUid, endUid);
            removeUidsFromKernelModule(startUid, endUid);
        }

        /* JADX WARNING: Missing block: B:13:?, code skipped:
            $closeResource(r2, r1);
     */
        private void removeUidsFromKernelModule(int r6, int r7) {
            /*
            r5 = this;
            r0 = r5.mTag;
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = "Removing uids ";
            r1.append(r2);
            r1.append(r6);
            r2 = "-";
            r1.append(r2);
            r1.append(r7);
            r1 = r1.toString();
            android.util.Slog.d(r0, r1);
            r0 = android.os.StrictMode.allowThreadDiskWritesMask();
            r1 = new java.io.FileWriter;	 Catch:{ IOException -> 0x004f }
            r3 = "/proc/uid_cputime/remove_uid_range";
            r1.<init>(r3);	 Catch:{ IOException -> 0x004f }
            r3 = 0;
            r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0046 }
            r4.<init>();	 Catch:{ all -> 0x0046 }
            r4.append(r6);	 Catch:{ all -> 0x0046 }
            r4.append(r2);	 Catch:{ all -> 0x0046 }
            r4.append(r7);	 Catch:{ all -> 0x0046 }
            r2 = r4.toString();	 Catch:{ all -> 0x0046 }
            r1.write(r2);	 Catch:{ all -> 0x0046 }
            r1.flush();	 Catch:{ all -> 0x0046 }
            $closeResource(r3, r1);	 Catch:{ IOException -> 0x004f }
            goto L_0x0074;
        L_0x0046:
            r2 = move-exception;
            throw r2;	 Catch:{ all -> 0x0048 }
        L_0x0048:
            r3 = move-exception;
            $closeResource(r2, r1);	 Catch:{ IOException -> 0x004f }
            throw r3;	 Catch:{ IOException -> 0x004f }
        L_0x004d:
            r1 = move-exception;
            goto L_0x0079;
        L_0x004f:
            r1 = move-exception;
            r2 = r5.mTag;	 Catch:{ all -> 0x004d }
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x004d }
            r3.<init>();	 Catch:{ all -> 0x004d }
            r4 = "failed to remove uids ";
            r3.append(r4);	 Catch:{ all -> 0x004d }
            r3.append(r6);	 Catch:{ all -> 0x004d }
            r4 = " - ";
            r3.append(r4);	 Catch:{ all -> 0x004d }
            r3.append(r7);	 Catch:{ all -> 0x004d }
            r4 = " from uid_cputime module";
            r3.append(r4);	 Catch:{ all -> 0x004d }
            r3 = r3.toString();	 Catch:{ all -> 0x004d }
            android.util.Slog.e(r2, r3, r1);	 Catch:{ all -> 0x004d }
        L_0x0074:
            android.os.StrictMode.setThreadPolicyMask(r0);
            return;
        L_0x0079:
            android.os.StrictMode.setThreadPolicyMask(r0);
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.KernelCpuUidTimeReader$KernelCpuUidUserSysTimeReader.removeUidsFromKernelModule(int, int):void");
        }
    }

    public abstract void readAbsoluteImpl(Callback<T> callback);

    public abstract void readDeltaImpl(Callback<T> callback);

    KernelCpuUidTimeReader(KernelCpuProcStringReader reader, boolean throttle) {
        this.mReader = reader;
        this.mThrottle = throttle;
    }

    public void readDelta(Callback<T> cb) {
        if (this.mThrottle) {
            long currTimeMs = SystemClock.elapsedRealtime();
            if (currTimeMs >= this.mLastReadTimeMs + this.mMinTimeBetweenRead) {
                readDeltaImpl(cb);
                this.mLastReadTimeMs = currTimeMs;
                return;
            }
            return;
        }
        readDeltaImpl(cb);
    }

    public void readAbsolute(Callback<T> cb) {
        if (this.mThrottle) {
            long currTimeMs = SystemClock.elapsedRealtime();
            if (currTimeMs >= this.mLastReadTimeMs + this.mMinTimeBetweenRead) {
                readAbsoluteImpl(cb);
                this.mLastReadTimeMs = currTimeMs;
                return;
            }
            return;
        }
        readAbsoluteImpl(cb);
    }

    public void removeUid(int uid) {
        this.mLastTimes.delete(uid);
    }

    public void removeUidsInRange(int startUid, int endUid) {
        if (endUid < startUid) {
            String str = this.mTag;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("start UID ");
            stringBuilder.append(startUid);
            stringBuilder.append(" > end UID ");
            stringBuilder.append(endUid);
            Slog.e(str, stringBuilder.toString());
            return;
        }
        this.mLastTimes.put(startUid, null);
        this.mLastTimes.put(endUid, null);
        int firstIndex = this.mLastTimes.indexOfKey(startUid);
        this.mLastTimes.removeAtRange(firstIndex, (this.mLastTimes.indexOfKey(endUid) - firstIndex) + 1);
    }

    public void setThrottle(long minTimeBetweenRead) {
        if (this.mThrottle && minTimeBetweenRead >= 0) {
            this.mMinTimeBetweenRead = minTimeBetweenRead;
        }
    }
}
