package android.os;

import android.annotation.UnsupportedAppUsage;
import android.util.Printer;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import java.io.PrintWriter;

public final class Looper {
    private static final String TAG = "Looper";
    @UnsupportedAppUsage
    private static Looper sMainLooper;
    private static Observer sObserver;
    @UnsupportedAppUsage
    static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal();
    private Printer mLogging;
    @UnsupportedAppUsage
    private MessageMonitor mMonitor = new MessageMonitor();
    @UnsupportedAppUsage
    final MessageQueue mQueue;
    private long mSlowDeliveryThresholdMs;
    private long mSlowDispatchThresholdMs;
    final Thread mThread = Thread.currentThread();
    private long mTraceTag;

    public interface Observer {
        void dispatchingThrewException(Object obj, Message message, Exception exception);

        Object messageDispatchStarting();

        void messageDispatched(Object obj, Message message);
    }

    public static void prepare() {
        prepare(true);
    }

    private static void prepare(boolean quitAllowed) {
        if (sThreadLocal.get() == null) {
            sThreadLocal.set(new Looper(quitAllowed));
            return;
        }
        throw new RuntimeException("Only one Looper may be created per thread");
    }

    public static void prepareMainLooper() {
        prepare(false);
        synchronized (Looper.class) {
            if (sMainLooper == null) {
                sMainLooper = myLooper();
            } else {
                throw new IllegalStateException("The main Looper has already been prepared.");
            }
        }
    }

    public static Looper getMainLooper() {
        Looper looper;
        synchronized (Looper.class) {
            looper = sMainLooper;
        }
        return looper;
    }

    public static void setObserver(Observer observer) {
        sObserver = observer;
    }

    /* JADX WARNING: Removed duplicated region for block: B:68:0x01a7  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x01d5  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x01b4  */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x0227 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x01df  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x01a7  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x01b4  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x01d5  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x01df  */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x0227 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x00b9  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x00b6  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00df  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00da  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00e6  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00f7 A:{SYNTHETIC, Splitter:B:42:0x00f7} */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0126  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x011f A:{Catch:{ Exception -> 0x010c, all -> 0x00fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0131  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0193  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0141  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x01a7  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x01d5  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x01b4  */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x0227 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x01df  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x0264  */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x0255 A:{SYNTHETIC, Splitter:B:81:0x0255} */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x0264  */
    public static void loop() {
        /*
        r1 = myLooper();
        if (r1 == 0) goto L_0x0268;
    L_0x0006:
        r2 = r1.mQueue;
        android.os.Binder.clearCallingIdentity();
        r3 = android.os.Binder.clearCallingIdentity();
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r5 = "log.looper.";
        r0.append(r5);
        r5 = android.os.Process.myUid();
        r0.append(r5);
        r5 = ".";
        r0.append(r5);
        r5 = java.lang.Thread.currentThread();
        r5 = r5.getName();
        r0.append(r5);
        r5 = ".slow";
        r0.append(r5);
        r0 = r0.toString();
        r5 = 0;
        r6 = android.os.SystemProperties.getInt(r0, r5);
        r0 = 0;
        r7 = r0;
    L_0x0041:
        r15 = r2.next();
        if (r15 != 0) goto L_0x0048;
    L_0x0047:
        return;
    L_0x0048:
        r14 = r1.mLogging;
        r0 = " ";
        if (r14 == 0) goto L_0x0076;
    L_0x004e:
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = ">>>>> Dispatching to ";
        r8.append(r9);
        r9 = r15.target;
        r8.append(r9);
        r8.append(r0);
        r9 = r15.callback;
        r8.append(r9);
        r9 = ": ";
        r8.append(r9);
        r9 = r15.what;
        r8.append(r9);
        r8 = r8.toString();
        r14.println(r8);
    L_0x0076:
        r12 = sObserver;
        r8 = r1.getMessageMonitor();
        r9 = r15.monitorInfo;
        r8.markDispatch(r15, r9);
        r10 = r1.mTraceTag;
        r8 = r1.mSlowDispatchThresholdMs;
        r16 = r8;
        r8 = r1.mSlowDeliveryThresholdMs;
        if (r6 <= 0) goto L_0x0096;
    L_0x008b:
        r18 = r8;
        r8 = (long) r6;
        r16 = r8;
        r8 = (long) r6;
        r24 = r8;
        r26 = r16;
        goto L_0x009c;
    L_0x0096:
        r18 = r8;
        r26 = r16;
        r24 = r18;
    L_0x009c:
        r8 = 0;
        r13 = (r24 > r8 ? 1 : (r24 == r8 ? 0 : -1));
        r16 = 1;
        if (r13 <= 0) goto L_0x00af;
    L_0x00a4:
        r28 = r6;
        r5 = r15.when;
        r5 = (r5 > r8 ? 1 : (r5 == r8 ? 0 : -1));
        if (r5 <= 0) goto L_0x00b1;
    L_0x00ac:
        r5 = r16;
        goto L_0x00b2;
    L_0x00af:
        r28 = r6;
    L_0x00b1:
        r5 = 0;
    L_0x00b2:
        r6 = (r26 > r8 ? 1 : (r26 == r8 ? 0 : -1));
        if (r6 <= 0) goto L_0x00b9;
    L_0x00b6:
        r6 = r16;
        goto L_0x00ba;
    L_0x00b9:
        r6 = 0;
    L_0x00ba:
        if (r5 != 0) goto L_0x00c1;
    L_0x00bc:
        if (r6 == 0) goto L_0x00bf;
    L_0x00be:
        goto L_0x00c1;
    L_0x00bf:
        r16 = 0;
    L_0x00c1:
        r29 = r16;
        r30 = r6;
        r13 = (r10 > r8 ? 1 : (r10 == r8 ? 0 : -1));
        if (r13 == 0) goto L_0x00d8;
    L_0x00c9:
        r13 = android.os.Trace.isTagEnabled(r10);
        if (r13 == 0) goto L_0x00d8;
    L_0x00cf:
        r13 = r15.target;
        r13 = r13.getTraceName(r15);
        android.os.Trace.traceBegin(r10, r13);
    L_0x00d8:
        if (r29 == 0) goto L_0x00df;
    L_0x00da:
        r16 = android.os.SystemClock.uptimeMillis();
        goto L_0x00e1;
    L_0x00df:
        r16 = r8;
    L_0x00e1:
        r31 = r16;
        r13 = 0;
        if (r12 == 0) goto L_0x00ea;
    L_0x00e6:
        r13 = r12.messageDispatchStarting();
    L_0x00ea:
        r8 = r15.workSourceUid;
        r33 = android.os.ThreadLocalWorkSource.setUid(r8);
        r8 = r15.target;	 Catch:{ Exception -> 0x0244, all -> 0x0234 }
        r8.dispatchMessage(r15);	 Catch:{ Exception -> 0x0244, all -> 0x0234 }
        if (r12 == 0) goto L_0x011d;
    L_0x00f7:
        r12.messageDispatched(r13, r15);	 Catch:{ Exception -> 0x010c, all -> 0x00fb }
        goto L_0x011d;
    L_0x00fb:
        r0 = move-exception;
        r35 = r1;
        r36 = r2;
        r37 = r5;
        r18 = r7;
        r38 = r10;
        r1 = r12;
        r2 = r13;
        r7 = r14;
        r10 = r15;
        goto L_0x025b;
    L_0x010c:
        r0 = move-exception;
        r35 = r1;
        r36 = r2;
        r37 = r5;
        r18 = r7;
        r38 = r10;
        r1 = r12;
        r2 = r13;
        r7 = r14;
        r10 = r15;
        goto L_0x0253;
    L_0x011d:
        if (r30 == 0) goto L_0x0126;
    L_0x011f:
        r8 = android.os.SystemClock.uptimeMillis();	 Catch:{ Exception -> 0x010c, all -> 0x00fb }
        r20 = r8;
        goto L_0x0128;
    L_0x0126:
        r20 = 0;
    L_0x0128:
        android.os.ThreadLocalWorkSource.restore(r33);
        r8 = 0;
        r8 = (r10 > r8 ? 1 : (r10 == r8 ? 0 : -1));
        if (r8 == 0) goto L_0x0134;
    L_0x0131:
        android.os.Trace.traceEnd(r10);
    L_0x0134:
        r8 = r1.getMessageMonitor();
        r9 = r15.monitorInfo;
        r8.markFinish(r15, r9);
        r8 = "Looper";
        if (r5 == 0) goto L_0x0193;
    L_0x0141:
        if (r7 == 0) goto L_0x0170;
    L_0x0143:
        r35 = r1;
        r36 = r2;
        r1 = r15.when;
        r1 = r31 - r1;
        r16 = 10;
        r1 = (r1 > r16 ? 1 : (r1 == r16 ? 0 : -1));
        if (r1 > 0) goto L_0x0163;
    L_0x0151:
        r1 = "Drained";
        android.util.Slog.w(r8, r1);
        r1 = 0;
        r37 = r5;
        r5 = r8;
        r38 = r10;
        r2 = r13;
        r7 = r14;
        r40 = r15;
        r8 = r1;
        r1 = r12;
        goto L_0x01a5;
    L_0x0163:
        r37 = r5;
        r18 = r7;
        r5 = r8;
        r38 = r10;
        r1 = r12;
        r2 = r13;
        r7 = r14;
        r40 = r15;
        goto L_0x01a3;
    L_0x0170:
        r35 = r1;
        r36 = r2;
        r1 = r15.when;
        r16 = "delivery";
        r37 = r5;
        r5 = r8;
        r8 = r24;
        r38 = r10;
        r10 = r1;
        r1 = r12;
        r2 = r13;
        r12 = r31;
        r18 = r7;
        r7 = r14;
        r14 = r16;
        r40 = r15;
        r8 = showSlowLog(r8, r10, r12, r14, r15);
        if (r8 == 0) goto L_0x01a3;
    L_0x0191:
        r8 = 1;
        goto L_0x01a5;
    L_0x0193:
        r35 = r1;
        r36 = r2;
        r37 = r5;
        r18 = r7;
        r5 = r8;
        r38 = r10;
        r1 = r12;
        r2 = r13;
        r7 = r14;
        r40 = r15;
    L_0x01a3:
        r8 = r18;
    L_0x01a5:
        if (r6 == 0) goto L_0x01b2;
    L_0x01a7:
        r22 = "dispatch";
        r16 = r26;
        r18 = r31;
        r23 = r40;
        showSlowLog(r16, r18, r20, r22, r23);
    L_0x01b2:
        if (r7 == 0) goto L_0x01d5;
    L_0x01b4:
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = "<<<<< Finished to ";
        r9.append(r10);
        r10 = r40;
        r11 = r10.target;
        r9.append(r11);
        r9.append(r0);
        r11 = r10.callback;
        r9.append(r11);
        r9 = r9.toString();
        r7.println(r9);
        goto L_0x01d7;
    L_0x01d5:
        r10 = r40;
    L_0x01d7:
        r11 = android.os.Binder.clearCallingIdentity();
        r9 = (r3 > r11 ? 1 : (r3 == r11 ? 0 : -1));
        if (r9 == 0) goto L_0x0227;
    L_0x01df:
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r13 = "Thread identity changed from 0x";
        r9.append(r13);
        r13 = java.lang.Long.toHexString(r3);
        r9.append(r13);
        r13 = " to 0x";
        r9.append(r13);
        r13 = java.lang.Long.toHexString(r11);
        r9.append(r13);
        r13 = " while dispatching to ";
        r9.append(r13);
        r13 = r10.target;
        r13 = r13.getClass();
        r13 = r13.getName();
        r9.append(r13);
        r9.append(r0);
        r0 = r10.callback;
        r9.append(r0);
        r0 = " what=";
        r9.append(r0);
        r0 = r10.what;
        r9.append(r0);
        r0 = r9.toString();
        android.util.Log.wtf(r5, r0);
    L_0x0227:
        r10.recycleUnchecked();
        r7 = r8;
        r6 = r28;
        r1 = r35;
        r2 = r36;
        r5 = 0;
        goto L_0x0041;
    L_0x0234:
        r0 = move-exception;
        r35 = r1;
        r36 = r2;
        r37 = r5;
        r18 = r7;
        r38 = r10;
        r1 = r12;
        r2 = r13;
        r7 = r14;
        r10 = r15;
        goto L_0x025b;
    L_0x0244:
        r0 = move-exception;
        r35 = r1;
        r36 = r2;
        r37 = r5;
        r18 = r7;
        r38 = r10;
        r1 = r12;
        r2 = r13;
        r7 = r14;
        r10 = r15;
    L_0x0253:
        if (r1 == 0) goto L_0x0258;
    L_0x0255:
        r1.dispatchingThrewException(r2, r10, r0);	 Catch:{ all -> 0x025a }
        throw r0;	 Catch:{ all -> 0x025a }
    L_0x025a:
        r0 = move-exception;
    L_0x025b:
        android.os.ThreadLocalWorkSource.restore(r33);
        r8 = 0;
        r5 = (r38 > r8 ? 1 : (r38 == r8 ? 0 : -1));
        if (r5 == 0) goto L_0x0267;
    L_0x0264:
        android.os.Trace.traceEnd(r38);
    L_0x0267:
        throw r0;
    L_0x0268:
        r35 = r1;
        r0 = new java.lang.RuntimeException;
        r1 = "No Looper; Looper.prepare() wasn't called on this thread.";
        r0.<init>(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.Looper.loop():void");
    }

    private static boolean showSlowLog(long threshold, long measureStart, long measureEnd, String what, Message msg) {
        long actualTime = measureEnd - measureStart;
        if (actualTime < threshold) {
            return false;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Slow ");
        stringBuilder.append(what);
        stringBuilder.append(" took ");
        stringBuilder.append(actualTime);
        stringBuilder.append("ms ");
        stringBuilder.append(Thread.currentThread().getName());
        stringBuilder.append(" h=");
        stringBuilder.append(msg.target.getClass().getName());
        stringBuilder.append(" c=");
        stringBuilder.append(msg.callback);
        stringBuilder.append(" m=");
        stringBuilder.append(msg.what);
        Slog.w(TAG, stringBuilder.toString());
        return true;
    }

    public static Looper myLooper() {
        return (Looper) sThreadLocal.get();
    }

    public static MessageQueue myQueue() {
        return myLooper().mQueue;
    }

    private Looper(boolean quitAllowed) {
        this.mQueue = new MessageQueue(quitAllowed);
        this.mMonitor.setThreadNameAndTid(this.mThread.getName(), Process.myTid());
        this.mMonitor.enableMonitorMessage(true);
    }

    public MessageMonitor getMessageMonitor() {
        return this.mMonitor;
    }

    public boolean isCurrentThread() {
        return Thread.currentThread() == this.mThread;
    }

    public void setMessageLogging(Printer printer) {
        this.mLogging = printer;
    }

    @UnsupportedAppUsage
    public void setTraceTag(long traceTag) {
        this.mTraceTag = traceTag;
    }

    public void setSlowLogThresholdMs(long slowDispatchThresholdMs, long slowDeliveryThresholdMs) {
        this.mSlowDispatchThresholdMs = slowDispatchThresholdMs;
        this.mSlowDeliveryThresholdMs = slowDeliveryThresholdMs;
    }

    public void quit() {
        this.mQueue.quit(false);
    }

    public void quitSafely() {
        this.mQueue.quit(true);
    }

    public Thread getThread() {
        return this.mThread;
    }

    public MessageQueue getQueue() {
        return this.mQueue;
    }

    public void dump(Printer pw, String prefix) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append(toString());
        pw.println(stringBuilder.toString());
        MessageQueue messageQueue = this.mQueue;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(prefix);
        String str = "  ";
        stringBuilder2.append(str);
        messageQueue.dump(pw, stringBuilder2.toString(), null);
        MessageMonitor messageMonitor = this.mMonitor;
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(prefix);
        stringBuilder2.append(str);
        pw.println(messageMonitor.dumpAll(stringBuilder2.toString()));
    }

    public void dump(Printer pw, String prefix, Handler handler) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append(toString());
        pw.println(stringBuilder.toString());
        MessageQueue messageQueue = this.mQueue;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(prefix);
        stringBuilder2.append("  ");
        messageQueue.dump(pw, stringBuilder2.toString(), handler);
    }

    public void dumpAllLoopers(PrintWriter pw, String prefix) {
        this.mMonitor.dumpAllLoopers(pw, prefix);
    }

    public void writeToProto(ProtoOutputStream proto, long fieldId) {
        long looperToken = proto.start(fieldId);
        proto.write(1138166333441L, this.mThread.getName());
        proto.write(1112396529666L, this.mThread.getId());
        MessageQueue messageQueue = this.mQueue;
        if (messageQueue != null) {
            messageQueue.writeToProto(proto, 1146756268035L);
        }
        proto.end(looperToken);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Looper (");
        stringBuilder.append(this.mThread.getName());
        stringBuilder.append(", tid ");
        stringBuilder.append(this.mThread.getId());
        stringBuilder.append(") {");
        stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
