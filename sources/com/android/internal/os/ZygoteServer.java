package com.android.internal.os;

import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.os.SystemClock;
import android.os.Trace;
import android.system.ErrnoException;
import android.system.Os;
import android.util.Log;
import dalvik.system.ZygoteHooks;
import java.io.FileDescriptor;
import java.io.IOException;

class ZygoteServer {
    public static final String TAG = "ZygoteServer";
    private static final String USAP_POOL_SIZE_MAX_DEFAULT = "10";
    private static final int USAP_POOL_SIZE_MAX_LIMIT = 100;
    private static final String USAP_POOL_SIZE_MIN_DEFAULT = "1";
    private static final int USAP_POOL_SIZE_MIN_LIMIT = 1;
    private boolean mCloseSocketFd;
    private boolean mIsFirstPropertyCheck;
    private boolean mIsForkChild;
    private long mLastPropCheckTimestamp;
    private boolean mUsapPoolEnabled;
    private FileDescriptor mUsapPoolEventFD;
    private int mUsapPoolRefillThreshold;
    private int mUsapPoolSizeMax;
    private int mUsapPoolSizeMin;
    private LocalServerSocket mUsapPoolSocket;
    private final boolean mUsapPoolSupported;
    private LocalServerSocket mZygoteSocket;

    ZygoteServer() {
        this.mUsapPoolEnabled = false;
        this.mUsapPoolSizeMax = 0;
        this.mUsapPoolSizeMin = 0;
        this.mUsapPoolRefillThreshold = 0;
        this.mIsFirstPropertyCheck = true;
        this.mLastPropCheckTimestamp = 0;
        this.mUsapPoolEventFD = null;
        this.mZygoteSocket = null;
        this.mUsapPoolSocket = null;
        this.mUsapPoolSupported = false;
    }

    ZygoteServer(boolean isPrimaryZygote) {
        this.mUsapPoolEnabled = false;
        this.mUsapPoolSizeMax = 0;
        this.mUsapPoolSizeMin = 0;
        this.mUsapPoolRefillThreshold = 0;
        this.mIsFirstPropertyCheck = true;
        this.mLastPropCheckTimestamp = 0;
        this.mUsapPoolEventFD = Zygote.getUsapPoolEventFD();
        if (isPrimaryZygote) {
            this.mZygoteSocket = Zygote.createManagedSocketFromInitSocket(Zygote.PRIMARY_SOCKET_NAME);
            this.mUsapPoolSocket = Zygote.createManagedSocketFromInitSocket(Zygote.USAP_POOL_PRIMARY_SOCKET_NAME);
        } else {
            this.mZygoteSocket = Zygote.createManagedSocketFromInitSocket(Zygote.SECONDARY_SOCKET_NAME);
            this.mUsapPoolSocket = Zygote.createManagedSocketFromInitSocket(Zygote.USAP_POOL_SECONDARY_SOCKET_NAME);
        }
        fetchUsapPoolPolicyProps();
        this.mUsapPoolSupported = true;
    }

    /* Access modifiers changed, original: 0000 */
    public void setForkChild() {
        this.mIsForkChild = true;
    }

    public boolean isUsapPoolEnabled() {
        return this.mUsapPoolEnabled;
    }

    /* Access modifiers changed, original: 0000 */
    public void registerServerSocketAtAbstractName(String socketName) {
        if (this.mZygoteSocket == null) {
            try {
                this.mZygoteSocket = new LocalServerSocket(socketName);
                this.mCloseSocketFd = false;
            } catch (IOException ex) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error binding to abstract socket '");
                stringBuilder.append(socketName);
                stringBuilder.append("'");
                throw new RuntimeException(stringBuilder.toString(), ex);
            }
        }
    }

    private ZygoteConnection acceptCommandPeer(String abiList) {
        try {
            return createNewConnection(this.mZygoteSocket.accept(), abiList);
        } catch (IOException ex) {
            throw new RuntimeException("IOException during accept()", ex);
        }
    }

    /* Access modifiers changed, original: protected */
    public ZygoteConnection createNewConnection(LocalSocket socket, String abiList) throws IOException {
        return new ZygoteConnection(socket, abiList);
    }

    /* Access modifiers changed, original: 0000 */
    public void closeServerSocket() {
        String str = TAG;
        try {
            if (this.mZygoteSocket != null) {
                FileDescriptor fd = this.mZygoteSocket.getFileDescriptor();
                this.mZygoteSocket.close();
                if (fd != null && this.mCloseSocketFd) {
                    Os.close(fd);
                }
            }
        } catch (IOException ex) {
            Log.e(str, "Zygote:  error closing sockets", ex);
        } catch (ErrnoException ex2) {
            Log.e(str, "Zygote:  error closing descriptor", ex2);
        }
        this.mZygoteSocket = null;
    }

    /* Access modifiers changed, original: 0000 */
    public FileDescriptor getZygoteSocketFileDescriptor() {
        return this.mZygoteSocket.getFileDescriptor();
    }

    private void fetchUsapPoolPolicyProps() {
        if (this.mUsapPoolSupported) {
            String str = USAP_POOL_SIZE_MAX_DEFAULT;
            String usapPoolSizeMaxPropString = Zygote.getConfigurationProperty(ZygoteConfig.USAP_POOL_SIZE_MAX, str);
            if (!usapPoolSizeMaxPropString.isEmpty()) {
                this.mUsapPoolSizeMax = Integer.min(Integer.parseInt(usapPoolSizeMaxPropString), 100);
            }
            String str2 = "1";
            String usapPoolSizeMinPropString = Zygote.getConfigurationProperty(ZygoteConfig.USAP_POOL_SIZE_MIN, str2);
            if (!usapPoolSizeMinPropString.isEmpty()) {
                this.mUsapPoolSizeMin = Integer.max(Integer.parseInt(usapPoolSizeMinPropString), 1);
            }
            String usapPoolRefillThresholdPropString = Zygote.getConfigurationProperty(ZygoteConfig.USAP_POOL_REFILL_THRESHOLD, Integer.toString(this.mUsapPoolSizeMax / 2));
            if (!usapPoolRefillThresholdPropString.isEmpty()) {
                this.mUsapPoolRefillThreshold = Integer.min(Integer.parseInt(usapPoolRefillThresholdPropString), this.mUsapPoolSizeMax);
            }
            if (this.mUsapPoolSizeMin >= this.mUsapPoolSizeMax) {
                Log.w(TAG, "The max size of the USAP pool must be greater than the minimum size.  Restoring default values.");
                this.mUsapPoolSizeMax = Integer.parseInt(str);
                this.mUsapPoolSizeMin = Integer.parseInt(str2);
                this.mUsapPoolRefillThreshold = this.mUsapPoolSizeMax / 2;
            }
        }
    }

    private void fetchUsapPoolPolicyPropsWithMinInterval() {
        long currentTimestamp = SystemClock.elapsedRealtime();
        if (this.mIsFirstPropertyCheck || currentTimestamp - this.mLastPropCheckTimestamp >= 60000) {
            this.mIsFirstPropertyCheck = false;
            this.mLastPropCheckTimestamp = currentTimestamp;
            fetchUsapPoolPolicyProps();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public Runnable fillUsapPool(int[] sessionSocketRawFDs) {
        Trace.traceBegin(64, "Zygote:FillUsapPool");
        fetchUsapPoolPolicyPropsWithMinInterval();
        int usapPoolCount = Zygote.getUsapPoolCount();
        int numUsapsToSpawn = this.mUsapPoolSizeMax - usapPoolCount;
        if (usapPoolCount < this.mUsapPoolSizeMin || numUsapsToSpawn >= this.mUsapPoolRefillThreshold) {
            ZygoteHooks.preFork();
            Zygote.resetNicePriority();
            while (true) {
                int usapPoolCount2 = usapPoolCount + 1;
                if (usapPoolCount >= this.mUsapPoolSizeMax) {
                    ZygoteHooks.postForkCommon();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Filled the USAP pool. New USAPs: ");
                    stringBuilder.append(numUsapsToSpawn);
                    Log.i(Zygote.PRIMARY_SOCKET_NAME, stringBuilder.toString());
                    break;
                }
                Runnable caller = Zygote.forkUsap(this.mUsapPoolSocket, sessionSocketRawFDs);
                if (caller != null) {
                    return caller;
                }
                usapPoolCount = usapPoolCount2;
            }
        }
        Trace.traceEnd(64);
        return null;
    }

    /* Access modifiers changed, original: 0000 */
    public Runnable setUsapPoolStatus(boolean newStatus, LocalSocket sessionSocket) {
        boolean z = this.mUsapPoolSupported;
        String str = TAG;
        if (!z) {
            Log.w(str, "Attempting to enable a USAP pool for a Zygote that doesn't support it.");
            return null;
        } else if (this.mUsapPoolEnabled == newStatus) {
            return null;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("USAP Pool status change: ");
            stringBuilder.append(newStatus ? "ENABLED" : "DISABLED");
            Log.i(str, stringBuilder.toString());
            this.mUsapPoolEnabled = newStatus;
            if (newStatus) {
                return fillUsapPool(new int[]{sessionSocket.getFileDescriptor().getInt$()});
            }
            Zygote.emptyUsapPool();
            return null;
        }
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Missing block: B:71:0x01ab, code skipped:
            if (r11 == false) goto L_0x01cc;
     */
    /* JADX WARNING: Missing block: B:72:0x01ad, code skipped:
            r6 = fillUsapPool(r2.subList(1, r2.size()).stream().mapToInt(com.android.internal.os.-$$Lambda$ZygoteServer$NJVbduCrCzDq0RHpPga7lyCk4eE.INSTANCE).toArray());
     */
    /* JADX WARNING: Missing block: B:73:0x01c9, code skipped:
            if (r6 == null) goto L_0x01cc;
     */
    /* JADX WARNING: Missing block: B:74:0x01cb, code skipped:
            return r6;
     */
    public java.lang.Runnable runSelectLoop(java.lang.String r19) {
        /*
        r18 = this;
        r1 = r18;
        r0 = new java.util.ArrayList;
        r0.<init>();
        r2 = r0;
        r0 = new java.util.ArrayList;
        r0.<init>();
        r3 = r0;
        r0 = r1.mZygoteSocket;
        r0 = r0.getFileDescriptor();
        r2.add(r0);
        r0 = 0;
        r3.add(r0);
    L_0x001b:
        r18.fetchUsapPoolPolicyPropsWithMinInterval();
        r0 = 0;
        r4 = 0;
        r5 = r1.mUsapPoolEnabled;
        r6 = 1;
        if (r5 == 0) goto L_0x0034;
    L_0x0025:
        r0 = com.android.internal.os.Zygote.getUsapPipeFDs();
        r5 = r2.size();
        r5 = r5 + r6;
        r7 = r0.length;
        r5 = r5 + r7;
        r4 = new android.system.StructPollfd[r5];
        r5 = r0;
        goto L_0x003b;
    L_0x0034:
        r5 = r2.size();
        r4 = new android.system.StructPollfd[r5];
        r5 = r0;
    L_0x003b:
        r0 = 0;
        r7 = r2.iterator();
    L_0x0040:
        r8 = r7.hasNext();
        if (r8 == 0) goto L_0x0062;
    L_0x0046:
        r8 = r7.next();
        r8 = (java.io.FileDescriptor) r8;
        r9 = new android.system.StructPollfd;
        r9.<init>();
        r4[r0] = r9;
        r9 = r4[r0];
        r9.fd = r8;
        r9 = r4[r0];
        r10 = android.system.OsConstants.POLLIN;
        r10 = (short) r10;
        r9.events = r10;
        r0 = r0 + 1;
        goto L_0x0040;
    L_0x0062:
        r7 = r0;
        r8 = r1.mUsapPoolEnabled;
        r9 = 0;
        if (r8 == 0) goto L_0x00a5;
    L_0x0068:
        r8 = new android.system.StructPollfd;
        r8.<init>();
        r4[r0] = r8;
        r8 = r4[r0];
        r10 = r1.mUsapPoolEventFD;
        r8.fd = r10;
        r8 = r4[r0];
        r10 = android.system.OsConstants.POLLIN;
        r10 = (short) r10;
        r8.events = r10;
        r0 = r0 + 1;
        r8 = r5.length;
        r10 = r0;
        r0 = r9;
    L_0x0081:
        if (r0 >= r8) goto L_0x00a6;
    L_0x0083:
        r11 = r5[r0];
        r12 = new java.io.FileDescriptor;
        r12.<init>();
        r12.setInt$(r11);
        r13 = new android.system.StructPollfd;
        r13.<init>();
        r4[r10] = r13;
        r13 = r4[r10];
        r13.fd = r12;
        r13 = r4[r10];
        r14 = android.system.OsConstants.POLLIN;
        r14 = (short) r14;
        r13.events = r14;
        r10 = r10 + 1;
        r0 = r0 + 1;
        goto L_0x0081;
    L_0x00a5:
        r10 = r0;
    L_0x00a6:
        r8 = -1;
        android.system.Os.poll(r4, r8);	 Catch:{ ErrnoException -> 0x01ce }
        r0 = 0;
        r11 = r0;
    L_0x00ad:
        r10 = r10 + r8;
        if (r10 < 0) goto L_0x01ab;
    L_0x00b0:
        r0 = r4[r10];
        r0 = r0.revents;
        r12 = android.system.OsConstants.POLLIN;
        r0 = r0 & r12;
        if (r0 != 0) goto L_0x00bb;
    L_0x00b9:
        goto L_0x01a7;
    L_0x00bb:
        if (r10 != 0) goto L_0x00cd;
    L_0x00bd:
        r0 = r18.acceptCommandPeer(r19);
        r3.add(r0);
        r12 = r0.getFileDescriptor();
        r2.add(r12);
        goto L_0x01a7;
    L_0x00cd:
        r12 = "ZygoteServer";
        if (r10 >= r7) goto L_0x012f;
    L_0x00d1:
        r0 = r3.get(r10);	 Catch:{ Exception -> 0x010b }
        r0 = (com.android.internal.os.ZygoteConnection) r0;	 Catch:{ Exception -> 0x010b }
        r13 = r0.processOneCommand(r1);	 Catch:{ Exception -> 0x010b }
        r14 = r1.mIsForkChild;	 Catch:{ Exception -> 0x010b }
        if (r14 == 0) goto L_0x00ed;
    L_0x00df:
        if (r13 == 0) goto L_0x00e5;
        r1.mIsForkChild = r9;
        return r13;
    L_0x00e5:
        r14 = new java.lang.IllegalStateException;	 Catch:{ Exception -> 0x010b }
        r15 = "command == null";
        r14.<init>(r15);	 Catch:{ Exception -> 0x010b }
        throw r14;	 Catch:{ Exception -> 0x010b }
    L_0x00ed:
        if (r13 != 0) goto L_0x0101;
    L_0x00ef:
        r14 = r0.isClosedByPeer();	 Catch:{ Exception -> 0x010b }
        if (r14 == 0) goto L_0x00fe;
    L_0x00f5:
        r0.closeSocket();	 Catch:{ Exception -> 0x010b }
        r3.remove(r10);	 Catch:{ Exception -> 0x010b }
        r2.remove(r10);	 Catch:{ Exception -> 0x010b }
    L_0x00fe:
        r1.mIsForkChild = r9;
        goto L_0x0123;
    L_0x0101:
        r14 = new java.lang.IllegalStateException;	 Catch:{ Exception -> 0x010b }
        r15 = "command != null";
        r14.<init>(r15);	 Catch:{ Exception -> 0x010b }
        throw r14;	 Catch:{ Exception -> 0x010b }
    L_0x0109:
        r0 = move-exception;
        goto L_0x012c;
    L_0x010b:
        r0 = move-exception;
        r13 = r1.mIsForkChild;	 Catch:{ all -> 0x0109 }
        if (r13 != 0) goto L_0x0125;
    L_0x0110:
        r13 = "Exception executing zygote command: ";
        android.util.Slog.e(r12, r13, r0);	 Catch:{ all -> 0x0109 }
        r12 = r3.remove(r10);	 Catch:{ all -> 0x0109 }
        r12 = (com.android.internal.os.ZygoteConnection) r12;	 Catch:{ all -> 0x0109 }
        r12.closeSocket();	 Catch:{ all -> 0x0109 }
        r2.remove(r10);	 Catch:{ all -> 0x0109 }
        goto L_0x00fe;
    L_0x0123:
        goto L_0x01a7;
    L_0x0125:
        r6 = "Caught post-fork exception in child process.";
        android.util.Log.e(r12, r6, r0);	 Catch:{ all -> 0x0109 }
        throw r0;	 Catch:{ all -> 0x0109 }
    L_0x012c:
        r1.mIsForkChild = r9;
        throw r0;
    L_0x012f:
        r13 = -1;
        r0 = 8;
        r15 = new byte[r0];	 Catch:{ Exception -> 0x0172 }
        r8 = r4[r10];	 Catch:{ Exception -> 0x0172 }
        r8 = r8.fd;	 Catch:{ Exception -> 0x0172 }
        r6 = r15.length;	 Catch:{ Exception -> 0x0172 }
        r6 = android.system.Os.read(r8, r15, r9, r6);	 Catch:{ Exception -> 0x0172 }
        if (r6 != r0) goto L_0x015d;
    L_0x0140:
        r0 = new java.io.DataInputStream;	 Catch:{ Exception -> 0x0172 }
        r8 = new java.io.ByteArrayInputStream;	 Catch:{ Exception -> 0x0172 }
        r8.<init>(r15);	 Catch:{ Exception -> 0x0172 }
        r0.<init>(r8);	 Catch:{ Exception -> 0x0172 }
        r16 = r0.readLong();	 Catch:{ Exception -> 0x0172 }
        r12 = r16;
        if (r10 <= r7) goto L_0x0158;
    L_0x0154:
        r0 = (int) r12;
        com.android.internal.os.Zygote.removeUsapTableEntry(r0);
    L_0x0158:
        r11 = 1;
        r6 = 1;
        r8 = -1;
        goto L_0x00ad;
    L_0x015d:
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0172 }
        r0.<init>();	 Catch:{ Exception -> 0x0172 }
        r8 = "Incomplete read from USAP management FD of size ";
        r0.append(r8);	 Catch:{ Exception -> 0x0172 }
        r0.append(r6);	 Catch:{ Exception -> 0x0172 }
        r0 = r0.toString();	 Catch:{ Exception -> 0x0172 }
        android.util.Log.e(r12, r0);	 Catch:{ Exception -> 0x0172 }
        goto L_0x01a7;
    L_0x0172:
        r0 = move-exception;
        if (r10 != r7) goto L_0x018e;
    L_0x0175:
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r8 = "Failed to read from USAP pool event FD: ";
        r6.append(r8);
        r8 = r0.getMessage();
        r6.append(r8);
        r6 = r6.toString();
        android.util.Log.e(r12, r6);
        goto L_0x01a6;
    L_0x018e:
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r8 = "Failed to read from USAP reporting pipe: ";
        r6.append(r8);
        r8 = r0.getMessage();
        r6.append(r8);
        r6 = r6.toString();
        android.util.Log.e(r12, r6);
    L_0x01a7:
        r6 = 1;
        r8 = -1;
        goto L_0x00ad;
    L_0x01ab:
        if (r11 == 0) goto L_0x01cc;
        r0 = r2.size();
        r6 = 1;
        r0 = r2.subList(r6, r0);
        r0 = r0.stream();
        r6 = com.android.internal.os.-$$Lambda$ZygoteServer$NJVbduCrCzDq0RHpPga7lyCk4eE.INSTANCE;
        r0 = r0.mapToInt(r6);
        r0 = r0.toArray();
        r6 = r1.fillUsapPool(r0);
        if (r6 == 0) goto L_0x01cc;
    L_0x01cb:
        return r6;
    L_0x01cc:
        goto L_0x001b;
    L_0x01ce:
        r0 = move-exception;
        r6 = r0;
        r0 = r6;
        r6 = new java.lang.RuntimeException;
        r8 = "poll failed";
        r6.<init>(r8, r0);
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.ZygoteServer.runSelectLoop(java.lang.String):java.lang.Runnable");
    }
}
