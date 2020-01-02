package android.os;

import java.io.PrintStream;

public class Broadcaster {
    private Registration mReg;

    private class Registration {
        Registration next;
        Registration prev;
        int senderWhat;
        int[] targetWhats;
        Handler[] targets;

        private Registration() {
        }
    }

    /* JADX WARNING: Missing block: B:32:0x009b, code skipped:
            return;
     */
    @android.annotation.UnsupportedAppUsage
    public void request(int r9, android.os.Handler r10, int r11) {
        /*
        r8 = this;
        monitor-enter(r8);
        r0 = 0;
        r1 = r8.mReg;	 Catch:{ all -> 0x009c }
        r2 = 0;
        r3 = 0;
        r4 = 1;
        if (r1 != 0) goto L_0x0029;
    L_0x0009:
        r1 = new android.os.Broadcaster$Registration;	 Catch:{ all -> 0x009c }
        r1.<init>();	 Catch:{ all -> 0x009c }
        r0 = r1;
        r0.senderWhat = r9;	 Catch:{ all -> 0x009c }
        r1 = new android.os.Handler[r4];	 Catch:{ all -> 0x009c }
        r0.targets = r1;	 Catch:{ all -> 0x009c }
        r1 = new int[r4];	 Catch:{ all -> 0x009c }
        r0.targetWhats = r1;	 Catch:{ all -> 0x009c }
        r1 = r0.targets;	 Catch:{ all -> 0x009c }
        r1[r3] = r10;	 Catch:{ all -> 0x009c }
        r1 = r0.targetWhats;	 Catch:{ all -> 0x009c }
        r1[r3] = r11;	 Catch:{ all -> 0x009c }
        r8.mReg = r0;	 Catch:{ all -> 0x009c }
        r0.next = r0;	 Catch:{ all -> 0x009c }
        r0.prev = r0;	 Catch:{ all -> 0x009c }
        goto L_0x009a;
    L_0x0029:
        r1 = r8.mReg;	 Catch:{ all -> 0x009c }
        r0 = r1;
    L_0x002c:
        r5 = r0.senderWhat;	 Catch:{ all -> 0x009c }
        if (r5 < r9) goto L_0x0031;
    L_0x0030:
        goto L_0x0036;
    L_0x0031:
        r5 = r0.next;	 Catch:{ all -> 0x009c }
        r0 = r5;
        if (r0 != r1) goto L_0x002c;
    L_0x0036:
        r5 = r0.senderWhat;	 Catch:{ all -> 0x009c }
        if (r5 == r9) goto L_0x0065;
    L_0x003a:
        r3 = new android.os.Broadcaster$Registration;	 Catch:{ all -> 0x009c }
        r3.<init>();	 Catch:{ all -> 0x009c }
        r2 = r3;
        r2.senderWhat = r9;	 Catch:{ all -> 0x009c }
        r3 = new android.os.Handler[r4];	 Catch:{ all -> 0x009c }
        r2.targets = r3;	 Catch:{ all -> 0x009c }
        r3 = new int[r4];	 Catch:{ all -> 0x009c }
        r2.targetWhats = r3;	 Catch:{ all -> 0x009c }
        r2.next = r0;	 Catch:{ all -> 0x009c }
        r3 = r0.prev;	 Catch:{ all -> 0x009c }
        r2.prev = r3;	 Catch:{ all -> 0x009c }
        r3 = r0.prev;	 Catch:{ all -> 0x009c }
        r3.next = r2;	 Catch:{ all -> 0x009c }
        r0.prev = r2;	 Catch:{ all -> 0x009c }
        r3 = r8.mReg;	 Catch:{ all -> 0x009c }
        if (r0 != r3) goto L_0x0062;
    L_0x005a:
        r3 = r0.senderWhat;	 Catch:{ all -> 0x009c }
        r4 = r2.senderWhat;	 Catch:{ all -> 0x009c }
        if (r3 <= r4) goto L_0x0062;
    L_0x0060:
        r8.mReg = r2;	 Catch:{ all -> 0x009c }
    L_0x0062:
        r0 = r2;
        r2 = 0;
        goto L_0x0092;
    L_0x0065:
        r2 = r0.targets;	 Catch:{ all -> 0x009c }
        r2 = r2.length;	 Catch:{ all -> 0x009c }
        r4 = r0.targets;	 Catch:{ all -> 0x009c }
        r5 = r0.targetWhats;	 Catch:{ all -> 0x009c }
        r6 = 0;
    L_0x006d:
        if (r6 >= r2) goto L_0x007c;
    L_0x006f:
        r7 = r4[r6];	 Catch:{ all -> 0x009c }
        if (r7 != r10) goto L_0x0079;
    L_0x0073:
        r7 = r5[r6];	 Catch:{ all -> 0x009c }
        if (r7 != r11) goto L_0x0079;
    L_0x0077:
        monitor-exit(r8);	 Catch:{ all -> 0x009c }
        return;
    L_0x0079:
        r6 = r6 + 1;
        goto L_0x006d;
    L_0x007c:
        r6 = r2 + 1;
        r6 = new android.os.Handler[r6];	 Catch:{ all -> 0x009c }
        r0.targets = r6;	 Catch:{ all -> 0x009c }
        r6 = r0.targets;	 Catch:{ all -> 0x009c }
        java.lang.System.arraycopy(r4, r3, r6, r3, r2);	 Catch:{ all -> 0x009c }
        r6 = r2 + 1;
        r6 = new int[r6];	 Catch:{ all -> 0x009c }
        r0.targetWhats = r6;	 Catch:{ all -> 0x009c }
        r6 = r0.targetWhats;	 Catch:{ all -> 0x009c }
        java.lang.System.arraycopy(r5, r3, r6, r3, r2);	 Catch:{ all -> 0x009c }
    L_0x0092:
        r3 = r0.targets;	 Catch:{ all -> 0x009c }
        r3[r2] = r10;	 Catch:{ all -> 0x009c }
        r3 = r0.targetWhats;	 Catch:{ all -> 0x009c }
        r3[r2] = r11;	 Catch:{ all -> 0x009c }
    L_0x009a:
        monitor-exit(r8);	 Catch:{ all -> 0x009c }
        return;
    L_0x009c:
        r0 = move-exception;
        monitor-exit(r8);	 Catch:{ all -> 0x009c }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.Broadcaster.request(int, android.os.Handler, int):void");
    }

    /* JADX WARNING: Missing block: B:27:0x0058, code skipped:
            return;
     */
    @android.annotation.UnsupportedAppUsage
    public void cancelRequest(int r10, android.os.Handler r11, int r12) {
        /*
        r9 = this;
        monitor-enter(r9);
        r0 = r9.mReg;	 Catch:{ all -> 0x0059 }
        r1 = r0;
        if (r1 != 0) goto L_0x0008;
    L_0x0006:
        monitor-exit(r9);	 Catch:{ all -> 0x0059 }
        return;
    L_0x0008:
        r2 = r1.senderWhat;	 Catch:{ all -> 0x0059 }
        if (r2 < r10) goto L_0x000d;
    L_0x000c:
        goto L_0x0012;
    L_0x000d:
        r2 = r1.next;	 Catch:{ all -> 0x0059 }
        r1 = r2;
        if (r1 != r0) goto L_0x0008;
    L_0x0012:
        r2 = r1.senderWhat;	 Catch:{ all -> 0x0059 }
        if (r2 != r10) goto L_0x0057;
    L_0x0016:
        r2 = r1.targets;	 Catch:{ all -> 0x0059 }
        r3 = r1.targetWhats;	 Catch:{ all -> 0x0059 }
        r4 = r2.length;	 Catch:{ all -> 0x0059 }
        r5 = 0;
    L_0x001c:
        if (r5 >= r4) goto L_0x0057;
    L_0x001e:
        r6 = r2[r5];	 Catch:{ all -> 0x0059 }
        if (r6 != r11) goto L_0x0054;
    L_0x0022:
        r6 = r3[r5];	 Catch:{ all -> 0x0059 }
        if (r6 != r12) goto L_0x0054;
    L_0x0026:
        r6 = r4 + -1;
        r6 = new android.os.Handler[r6];	 Catch:{ all -> 0x0059 }
        r1.targets = r6;	 Catch:{ all -> 0x0059 }
        r6 = r4 + -1;
        r6 = new int[r6];	 Catch:{ all -> 0x0059 }
        r1.targetWhats = r6;	 Catch:{ all -> 0x0059 }
        if (r5 <= 0) goto L_0x003f;
    L_0x0034:
        r6 = r1.targets;	 Catch:{ all -> 0x0059 }
        r7 = 0;
        java.lang.System.arraycopy(r2, r7, r6, r7, r5);	 Catch:{ all -> 0x0059 }
        r6 = r1.targetWhats;	 Catch:{ all -> 0x0059 }
        java.lang.System.arraycopy(r3, r7, r6, r7, r5);	 Catch:{ all -> 0x0059 }
    L_0x003f:
        r6 = r4 - r5;
        r6 = r6 + -1;
        if (r6 == 0) goto L_0x0057;
    L_0x0045:
        r7 = r5 + 1;
        r8 = r1.targets;	 Catch:{ all -> 0x0059 }
        java.lang.System.arraycopy(r2, r7, r8, r5, r6);	 Catch:{ all -> 0x0059 }
        r7 = r5 + 1;
        r8 = r1.targetWhats;	 Catch:{ all -> 0x0059 }
        java.lang.System.arraycopy(r3, r7, r8, r5, r6);	 Catch:{ all -> 0x0059 }
        goto L_0x0057;
    L_0x0054:
        r5 = r5 + 1;
        goto L_0x001c;
    L_0x0057:
        monitor-exit(r9);	 Catch:{ all -> 0x0059 }
        return;
    L_0x0059:
        r0 = move-exception;
        monitor-exit(r9);	 Catch:{ all -> 0x0059 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.Broadcaster.cancelRequest(int, android.os.Handler, int):void");
    }

    public void dumpRegistrations() {
        synchronized (this) {
            Registration start = this.mReg;
            PrintStream printStream = System.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Broadcaster ");
            stringBuilder.append(this);
            stringBuilder.append(" {");
            printStream.println(stringBuilder.toString());
            if (start != null) {
                Registration r = start;
                do {
                    PrintStream printStream2 = System.out;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("    senderWhat=");
                    stringBuilder2.append(r.senderWhat);
                    printStream2.println(stringBuilder2.toString());
                    int n = r.targets.length;
                    for (int i = 0; i < n; i++) {
                        PrintStream printStream3 = System.out;
                        StringBuilder stringBuilder3 = new StringBuilder();
                        stringBuilder3.append("        [");
                        stringBuilder3.append(r.targetWhats[i]);
                        stringBuilder3.append("] ");
                        stringBuilder3.append(r.targets[i]);
                        printStream3.println(stringBuilder3.toString());
                    }
                    r = r.next;
                } while (r != start);
            }
            System.out.println("}");
        }
    }

    /* JADX WARNING: Missing block: B:18:0x0037, code skipped:
            return;
     */
    @android.annotation.UnsupportedAppUsage
    public void broadcast(android.os.Message r11) {
        /*
        r10 = this;
        monitor-enter(r10);
        r0 = r10.mReg;	 Catch:{ all -> 0x0038 }
        if (r0 != 0) goto L_0x0007;
    L_0x0005:
        monitor-exit(r10);	 Catch:{ all -> 0x0038 }
        return;
    L_0x0007:
        r0 = r11.what;	 Catch:{ all -> 0x0038 }
        r1 = r10.mReg;	 Catch:{ all -> 0x0038 }
        r2 = r1;
    L_0x000c:
        r3 = r2.senderWhat;	 Catch:{ all -> 0x0038 }
        if (r3 < r0) goto L_0x0011;
    L_0x0010:
        goto L_0x0016;
    L_0x0011:
        r3 = r2.next;	 Catch:{ all -> 0x0038 }
        r2 = r3;
        if (r2 != r1) goto L_0x000c;
    L_0x0016:
        r3 = r2.senderWhat;	 Catch:{ all -> 0x0038 }
        if (r3 != r0) goto L_0x0036;
    L_0x001a:
        r3 = r2.targets;	 Catch:{ all -> 0x0038 }
        r4 = r2.targetWhats;	 Catch:{ all -> 0x0038 }
        r5 = r3.length;	 Catch:{ all -> 0x0038 }
        r6 = 0;
    L_0x0020:
        if (r6 >= r5) goto L_0x0036;
    L_0x0022:
        r7 = r3[r6];	 Catch:{ all -> 0x0038 }
        r8 = android.os.Message.obtain();	 Catch:{ all -> 0x0038 }
        r8.copyFrom(r11);	 Catch:{ all -> 0x0038 }
        r9 = r4[r6];	 Catch:{ all -> 0x0038 }
        r8.what = r9;	 Catch:{ all -> 0x0038 }
        r7.sendMessage(r8);	 Catch:{ all -> 0x0038 }
        r6 = r6 + 1;
        goto L_0x0020;
    L_0x0036:
        monitor-exit(r10);	 Catch:{ all -> 0x0038 }
        return;
    L_0x0038:
        r0 = move-exception;
        monitor-exit(r10);	 Catch:{ all -> 0x0038 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.Broadcaster.broadcast(android.os.Message):void");
    }
}
