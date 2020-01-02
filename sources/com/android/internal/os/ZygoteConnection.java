package com.android.internal.os;

import android.annotation.UnsupportedAppUsage;
import android.content.pm.ApplicationInfo;
import android.metrics.LogMaker;
import android.net.Credentials;
import android.net.LocalSocket;
import android.os.Parcel;
import android.os.Process;
import android.os.Trace;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import android.util.StatsLogInternal;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import dalvik.system.VMRuntime;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import libcore.io.IoUtils;

class ZygoteConnection {
    private static final String TAG = "Zygote";
    private final String abiList;
    private boolean isEof;
    @UnsupportedAppUsage
    private final LocalSocket mSocket;
    @UnsupportedAppUsage
    private final DataOutputStream mSocketOutStream;
    private final BufferedReader mSocketReader;
    @UnsupportedAppUsage
    private final Credentials peer;

    private static class HiddenApiUsageLogger implements dalvik.system.VMRuntime.HiddenApiUsageLogger {
        private static HiddenApiUsageLogger sInstance = new HiddenApiUsageLogger();
        private int mHiddenApiAccessLogSampleRate = 0;
        private int mHiddenApiAccessStatslogSampleRate = 0;
        private final MetricsLogger mMetricsLogger = new MetricsLogger();

        private HiddenApiUsageLogger() {
        }

        public static void setHiddenApiAccessLogSampleRates(int sampleRate, int newSampleRate) {
            if (sampleRate != -1) {
                sInstance.mHiddenApiAccessLogSampleRate = sampleRate;
            }
            if (newSampleRate != -1) {
                sInstance.mHiddenApiAccessStatslogSampleRate = newSampleRate;
            }
        }

        public static HiddenApiUsageLogger getInstance() {
            return sInstance;
        }

        public void hiddenApiUsed(int sampledValue, String packageName, String signature, int accessMethod, boolean accessDenied) {
            if (sampledValue < this.mHiddenApiAccessLogSampleRate) {
                logUsage(packageName, signature, accessMethod, accessDenied);
            }
            if (sampledValue < this.mHiddenApiAccessStatslogSampleRate) {
                newLogUsage(signature, accessMethod, accessDenied);
            }
        }

        private void logUsage(String packageName, String signature, int accessMethod, boolean accessDenied) {
            int accessMethodMetric = 0;
            if (accessMethod == 0) {
                accessMethodMetric = 0;
            } else if (accessMethod == 1) {
                accessMethodMetric = 1;
            } else if (accessMethod == 2) {
                accessMethodMetric = 2;
            } else if (accessMethod == 3) {
                accessMethodMetric = 3;
            }
            LogMaker logMaker = new LogMaker((int) MetricsEvent.ACTION_HIDDEN_API_ACCESSED).setPackageName(packageName).addTaggedData(MetricsEvent.FIELD_HIDDEN_API_SIGNATURE, signature).addTaggedData(MetricsEvent.FIELD_HIDDEN_API_ACCESS_METHOD, Integer.valueOf(accessMethodMetric));
            if (accessDenied) {
                logMaker.addTaggedData(MetricsEvent.FIELD_HIDDEN_API_ACCESS_DENIED, Integer.valueOf(1));
            }
            this.mMetricsLogger.write(logMaker);
        }

        private void newLogUsage(String signature, int accessMethod, boolean accessDenied) {
            int accessMethodProto = 0;
            if (accessMethod == 0) {
                accessMethodProto = 0;
            } else if (accessMethod == 1) {
                accessMethodProto = 1;
            } else if (accessMethod == 2) {
                accessMethodProto = 2;
            } else if (accessMethod == 3) {
                accessMethodProto = 3;
            }
            StatsLogInternal.write(178, Process.myUid(), signature, accessMethodProto, accessDenied);
        }
    }

    ZygoteConnection(LocalSocket socket, String abiList) throws IOException {
        this.mSocket = socket;
        this.abiList = abiList;
        this.mSocketOutStream = new DataOutputStream(socket.getOutputStream());
        this.mSocketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()), 256);
        this.mSocket.setSoTimeout(1000);
        try {
            this.peer = this.mSocket.getPeerCredentials();
            this.isEof = false;
        } catch (IOException ex) {
            Log.e(TAG, "Cannot read peer credentials", ex);
            throw ex;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public FileDescriptor getFileDescriptor() {
        return this.mSocket.getFileDescriptor();
    }

    /* Access modifiers changed, original: 0000 */
    public Runnable processOneCommand(ZygoteServer zygoteServer) {
        Throwable th;
        ZygoteServer zygoteServer2 = zygoteServer;
        try {
            String[] args = Zygote.readArgumentList(this.mSocketReader);
            FileDescriptor[] descriptors = this.mSocket.getAncillaryFileDescriptors();
            if (args == null) {
                this.isEof = true;
                return null;
            }
            ZygoteArguments parsedArgs = new ZygoteArguments(args);
            if (parsedArgs.mAbiListQuery) {
                handleAbiListQuery();
                return null;
            } else if (parsedArgs.mPidQuery) {
                handlePidQuery();
                return null;
            } else if (parsedArgs.mUsapPoolStatusSpecified) {
                return handleUsapPoolStatusChange(zygoteServer2, parsedArgs.mUsapPoolEnabled);
            } else {
                if (parsedArgs.mPreloadDefault) {
                    handlePreload();
                    return null;
                } else if (parsedArgs.mPreloadPackage != null) {
                    handlePreloadPackage(parsedArgs.mPreloadPackage, parsedArgs.mPreloadPackageLibs, parsedArgs.mPreloadPackageLibFileName, parsedArgs.mPreloadPackageCacheKey);
                    return null;
                } else if (canPreloadApp() && parsedArgs.mPreloadApp != null) {
                    byte[] rawParcelData = Base64.getDecoder().decode(parsedArgs.mPreloadApp);
                    Parcel appInfoParcel = Parcel.obtain();
                    appInfoParcel.unmarshall(rawParcelData, 0, rawParcelData.length);
                    appInfoParcel.setDataPosition(0);
                    ApplicationInfo appInfo = (ApplicationInfo) ApplicationInfo.CREATOR.createFromParcel(appInfoParcel);
                    appInfoParcel.recycle();
                    if (appInfo != null) {
                        handlePreloadApp(appInfo);
                        return null;
                    }
                    throw new IllegalArgumentException("Failed to deserialize --preload-app");
                } else if (parsedArgs.mApiBlacklistExemptions != null) {
                    return handleApiBlacklistExemptions(zygoteServer2, parsedArgs.mApiBlacklistExemptions);
                } else {
                    ZygoteConnection zygoteConnection;
                    String[] strArr;
                    int i;
                    FileDescriptor[] args2;
                    if (parsedArgs.mHiddenApiAccessLogSampleRate != -1) {
                        zygoteConnection = this;
                        strArr = args;
                        i = -1;
                    } else if (parsedArgs.mHiddenApiAccessStatslogSampleRate != -1) {
                        zygoteConnection = this;
                        strArr = args;
                        args2 = descriptors;
                        i = -1;
                    } else if (parsedArgs.mPermittedCapabilities == 0 && parsedArgs.mEffectiveCapabilities == 0) {
                        FileDescriptor childPipeFd;
                        FileDescriptor serverPipeFd;
                        FileDescriptor serverPipeFd2;
                        Zygote.applyUidSecurityPolicy(parsedArgs, this.peer);
                        Zygote.applyInvokeWithSecurityPolicy(parsedArgs, this.peer);
                        Zygote.applyDebuggerSystemProperty(parsedArgs);
                        Zygote.applyInvokeWithSystemProperty(parsedArgs);
                        int[][] rlimits = null;
                        if (parsedArgs.mRLimits != null) {
                            rlimits = (int[][]) parsedArgs.mRLimits.toArray(Zygote.INT_ARRAY_2D);
                        }
                        if (parsedArgs.mInvokeWith != null) {
                            try {
                                FileDescriptor[] pipeFds = Os.pipe2(OsConstants.O_CLOEXEC);
                                childPipeFd = pipeFds[1];
                                serverPipeFd = pipeFds[0];
                                Os.fcntlInt(childPipeFd, OsConstants.F_SETFD, 0);
                                int[] fdsToIgnore = new int[]{childPipeFd.getInt$(), serverPipeFd.getInt$()};
                                serverPipeFd2 = serverPipeFd;
                                serverPipeFd = childPipeFd;
                                childPipeFd = fdsToIgnore;
                            } catch (ErrnoException errnoEx) {
                                throw new IllegalStateException("Unable to set up pipe for invoke-with", errnoEx);
                            }
                        }
                        serverPipeFd2 = null;
                        serverPipeFd = null;
                        childPipeFd = null;
                        int[] fdsToClose = new int[]{-1, -1};
                        FileDescriptor fd = this.mSocket.getFileDescriptor();
                        if (fd != null) {
                            fdsToClose[0] = fd.getInt$();
                        }
                        FileDescriptor fd2 = zygoteServer.getZygoteSocketFileDescriptor();
                        if (fd2 != null) {
                            fdsToClose[1] = fd2.getInt$();
                        }
                        i = -1;
                        FileDescriptor fd3 = null;
                        FileDescriptor[] descriptors2 = descriptors;
                        FileDescriptor serverPipeFd3 = serverPipeFd2;
                        int pid = Zygote.forkAndSpecialize(parsedArgs.mUid, parsedArgs.mGid, parsedArgs.mGids, parsedArgs.mRuntimeFlags, rlimits, parsedArgs.mMountExternal, parsedArgs.mSeInfo, parsedArgs.mNiceName, fdsToClose, childPipeFd, parsedArgs.mStartChildZygote, parsedArgs.mInstructionSet, parsedArgs.mAppDataDir, parsedArgs.mTargetSdkVersion);
                        if (pid == 0) {
                            try {
                                zygoteServer.setForkChild();
                                zygoteServer.closeServerSocket();
                                IoUtils.closeQuietly(serverPipeFd3);
                            } catch (Throwable th2) {
                                th = th2;
                                args2 = descriptors2;
                                IoUtils.closeQuietly(serverPipeFd);
                                IoUtils.closeQuietly(serverPipeFd3);
                                throw th;
                            }
                            try {
                                try {
                                    Runnable handleChildProc = handleChildProc(parsedArgs, descriptors2, serverPipeFd, parsedArgs.mStartChildZygote);
                                    IoUtils.closeQuietly(serverPipeFd);
                                    IoUtils.closeQuietly(null);
                                    return handleChildProc;
                                } catch (Throwable th3) {
                                    th = th3;
                                    serverPipeFd3 = null;
                                    IoUtils.closeQuietly(serverPipeFd);
                                    IoUtils.closeQuietly(serverPipeFd3);
                                    throw th;
                                }
                            } catch (Throwable th4) {
                                th = th4;
                                args2 = descriptors2;
                                serverPipeFd3 = null;
                                IoUtils.closeQuietly(serverPipeFd);
                                IoUtils.closeQuietly(serverPipeFd3);
                                throw th;
                            }
                        }
                        FileDescriptor serverPipeFd4;
                        args2 = descriptors2;
                        try {
                            IoUtils.closeQuietly(serverPipeFd);
                            serverPipeFd = null;
                            serverPipeFd4 = serverPipeFd3;
                        } catch (Throwable th5) {
                            th = th5;
                            descriptors = serverPipeFd3;
                            IoUtils.closeQuietly(serverPipeFd);
                            IoUtils.closeQuietly(serverPipeFd3);
                            throw th;
                        }
                        try {
                            handleParentProc(pid, args2, serverPipeFd4);
                            IoUtils.closeQuietly(null);
                            IoUtils.closeQuietly(serverPipeFd4);
                            return null;
                        } catch (Throwable th6) {
                            th = th6;
                            serverPipeFd3 = serverPipeFd4;
                            IoUtils.closeQuietly(serverPipeFd);
                            IoUtils.closeQuietly(serverPipeFd3);
                            throw th;
                        }
                    } else {
                        strArr = args;
                        args2 = descriptors;
                        i = -1;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Client may not specify capabilities: permitted=0x");
                        stringBuilder.append(Long.toHexString(parsedArgs.mPermittedCapabilities));
                        stringBuilder.append(", effective=0x");
                        stringBuilder.append(Long.toHexString(parsedArgs.mEffectiveCapabilities));
                        throw new ZygoteSecurityException(stringBuilder.toString());
                    }
                    return zygoteConnection.handleHiddenApiAccessLogSampleRate(zygoteServer, parsedArgs.mHiddenApiAccessLogSampleRate, parsedArgs.mHiddenApiAccessStatslogSampleRate);
                }
            }
        } catch (IOException ex) {
            ZygoteServer zygoteServer3 = zygoteServer2;
            throw new IllegalStateException("IOException on command socket", ex);
        }
    }

    private void handleAbiListQuery() {
        try {
            byte[] abiListBytes = this.abiList.getBytes(StandardCharsets.US_ASCII);
            this.mSocketOutStream.writeInt(abiListBytes.length);
            this.mSocketOutStream.write(abiListBytes);
        } catch (IOException ioe) {
            throw new IllegalStateException("Error writing to command socket", ioe);
        }
    }

    private void handlePidQuery() {
        try {
            byte[] pidStringBytes = String.valueOf(Process.myPid()).getBytes(StandardCharsets.US_ASCII);
            this.mSocketOutStream.writeInt(pidStringBytes.length);
            this.mSocketOutStream.write(pidStringBytes);
        } catch (IOException ioe) {
            throw new IllegalStateException("Error writing to command socket", ioe);
        }
    }

    private void handlePreload() {
        try {
            if (isPreloadComplete()) {
                this.mSocketOutStream.writeInt(1);
                return;
            }
            preload();
            this.mSocketOutStream.writeInt(0);
        } catch (IOException ioe) {
            throw new IllegalStateException("Error writing to command socket", ioe);
        }
    }

    private Runnable stateChangeWithUsapPoolReset(ZygoteServer zygoteServer, Runnable stateChangeCode) {
        try {
            boolean isUsapPoolEnabled = zygoteServer.isUsapPoolEnabled();
            String str = TAG;
            if (isUsapPoolEnabled) {
                Log.i(str, "Emptying USAP Pool due to state change.");
                Zygote.emptyUsapPool();
            }
            stateChangeCode.run();
            if (zygoteServer.isUsapPoolEnabled()) {
                Runnable fpResult = zygoteServer.fillUsapPool(new int[]{this.mSocket.getFileDescriptor().getInt$()});
                if (fpResult != null) {
                    zygoteServer.setForkChild();
                    return fpResult;
                }
                Log.i(str, "Finished refilling USAP Pool after state change.");
            }
            this.mSocketOutStream.writeInt(0);
            return null;
        } catch (IOException ioe) {
            throw new IllegalStateException("Error writing to command socket", ioe);
        }
    }

    private Runnable handleApiBlacklistExemptions(ZygoteServer zygoteServer, String[] exemptions) {
        return stateChangeWithUsapPoolReset(zygoteServer, new -$$Lambda$ZygoteConnection$xjqM7qW7vAjTqh2tR5XRF5Vn5mk(exemptions));
    }

    private Runnable handleUsapPoolStatusChange(ZygoteServer zygoteServer, boolean newStatus) {
        try {
            Runnable fpResult = zygoteServer.setUsapPoolStatus(newStatus, this.mSocket);
            if (fpResult == null) {
                this.mSocketOutStream.writeInt(0);
            } else {
                zygoteServer.setForkChild();
            }
            return fpResult;
        } catch (IOException ioe) {
            throw new IllegalStateException("Error writing to command socket", ioe);
        }
    }

    private Runnable handleHiddenApiAccessLogSampleRate(ZygoteServer zygoteServer, int samplingRate, int statsdSamplingRate) {
        return stateChangeWithUsapPoolReset(zygoteServer, new -$$Lambda$ZygoteConnection$KxVsZ-s4KsanePOHCU5JcuypPik(samplingRate, statsdSamplingRate));
    }

    static /* synthetic */ void lambda$handleHiddenApiAccessLogSampleRate$1(int samplingRate, int statsdSamplingRate) {
        ZygoteInit.setHiddenApiAccessLogSampleRate(Math.max(samplingRate, statsdSamplingRate));
        HiddenApiUsageLogger.setHiddenApiAccessLogSampleRates(samplingRate, statsdSamplingRate);
        ZygoteInit.setHiddenApiUsageLogger(HiddenApiUsageLogger.getInstance());
    }

    /* Access modifiers changed, original: protected */
    public void preload() {
        ZygoteInit.lazyPreload();
    }

    /* Access modifiers changed, original: protected */
    public boolean isPreloadComplete() {
        return ZygoteInit.isPreloadComplete();
    }

    /* Access modifiers changed, original: protected */
    public DataOutputStream getSocketOutputStream() {
        return this.mSocketOutStream;
    }

    /* Access modifiers changed, original: protected */
    public void handlePreloadPackage(String packagePath, String libsPath, String libFileName, String cacheKey) {
        throw new RuntimeException("Zygote does not support package preloading");
    }

    /* Access modifiers changed, original: protected */
    public boolean canPreloadApp() {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public void handlePreloadApp(ApplicationInfo aInfo) {
        throw new RuntimeException("Zygote does not support app preloading");
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void closeSocket() {
        try {
            this.mSocket.close();
        } catch (IOException ex) {
            Log.e(TAG, "Exception while closing command socket in parent", ex);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isClosedByPeer() {
        return this.isEof;
    }

    private Runnable handleChildProc(ZygoteArguments parsedArgs, FileDescriptor[] descriptors, FileDescriptor pipeFd, boolean isZygote) {
        closeSocket();
        if (descriptors != null) {
            int i = 0;
            try {
                Os.dup2(descriptors[0], OsConstants.STDIN_FILENO);
                Os.dup2(descriptors[1], OsConstants.STDOUT_FILENO);
                Os.dup2(descriptors[2], OsConstants.STDERR_FILENO);
                int length = descriptors.length;
                while (i < length) {
                    IoUtils.closeQuietly(descriptors[i]);
                    i++;
                }
            } catch (ErrnoException ex) {
                Log.e(TAG, "Error reopening stdio", ex);
            }
        }
        if (parsedArgs.mNiceName != null) {
            Process.setArgV0(parsedArgs.mNiceName);
        }
        Trace.traceEnd(64);
        if (parsedArgs.mInvokeWith != null) {
            WrapperInit.execApplication(parsedArgs.mInvokeWith, parsedArgs.mNiceName, parsedArgs.mTargetSdkVersion, VMRuntime.getCurrentInstructionSet(), pipeFd, parsedArgs.mRemainingArgs);
            throw new IllegalStateException("WrapperInit.execApplication unexpectedly returned");
        } else if (isZygote) {
            return ZygoteInit.childZygoteInit(parsedArgs.mTargetSdkVersion, parsedArgs.mRemainingArgs, null);
        } else {
            return ZygoteInit.zygoteInit(parsedArgs.mTargetSdkVersion, parsedArgs.mRemainingArgs, null);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:50:0x00c9  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00c9  */
    private void handleParentProc(int r27, java.io.FileDescriptor[] r28, java.io.FileDescriptor r29) {
        /*
        r26 = this;
        r1 = r26;
        r2 = r27;
        r3 = r28;
        r4 = r29;
        r5 = "Error reading pid from wrapped process, child may have died";
        r6 = "Zygote";
        if (r2 <= 0) goto L_0x0011;
    L_0x000e:
        r26.setChildPgid(r27);
    L_0x0011:
        r0 = 0;
        if (r3 == 0) goto L_0x0020;
    L_0x0014:
        r7 = r3.length;
        r8 = r0;
    L_0x0016:
        if (r8 >= r7) goto L_0x0020;
    L_0x0018:
        r9 = r3[r8];
        libcore.io.IoUtils.closeQuietly(r9);
        r8 = r8 + 1;
        goto L_0x0016;
    L_0x0020:
        r7 = 0;
        if (r4 == 0) goto L_0x0109;
    L_0x0023:
        if (r2 <= 0) goto L_0x0109;
    L_0x0025:
        r8 = -1;
        r9 = 4;
        r10 = 1;
        r11 = new android.system.StructPollfd[r10];	 Catch:{ Exception -> 0x00c0 }
        r12 = new android.system.StructPollfd;	 Catch:{ Exception -> 0x00c0 }
        r12.<init>();	 Catch:{ Exception -> 0x00c0 }
        r11[r0] = r12;	 Catch:{ Exception -> 0x00c0 }
        r12 = 4;
        r12 = new byte[r12];	 Catch:{ Exception -> 0x00c0 }
        r13 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
        r14 = 0;
        r15 = java.lang.System.nanoTime();	 Catch:{ Exception -> 0x00c0 }
    L_0x003b:
        r10 = r12.length;	 Catch:{ Exception -> 0x00c0 }
        if (r14 >= r10) goto L_0x009a;
    L_0x003e:
        if (r13 <= 0) goto L_0x009a;
    L_0x0040:
        r10 = r11[r0];	 Catch:{ Exception -> 0x00c0 }
        r10.fd = r4;	 Catch:{ Exception -> 0x00c0 }
        r10 = r11[r0];	 Catch:{ Exception -> 0x00c0 }
        r0 = android.system.OsConstants.POLLIN;	 Catch:{ Exception -> 0x00c0 }
        r0 = (short) r0;	 Catch:{ Exception -> 0x00c0 }
        r10.events = r0;	 Catch:{ Exception -> 0x00c0 }
        r0 = 0;
        r10 = r11[r0];	 Catch:{ Exception -> 0x00c0 }
        r10.revents = r0;	 Catch:{ Exception -> 0x00c0 }
        r10 = r11[r0];	 Catch:{ Exception -> 0x00c0 }
        r0 = 0;
        r10.userData = r0;	 Catch:{ Exception -> 0x00c0 }
        r0 = android.system.Os.poll(r11, r13);	 Catch:{ Exception -> 0x00c0 }
        r19 = java.lang.System.nanoTime();	 Catch:{ Exception -> 0x00c0 }
        r21 = r19 - r15;
        r23 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r10 = r7;
        r25 = r8;
        r7 = r21 / r23;
        r7 = (int) r7;	 Catch:{ Exception -> 0x00bc }
        r13 = 30000 - r7;
        if (r0 <= 0) goto L_0x0089;
    L_0x006c:
        r8 = 0;
        r3 = r11[r8];	 Catch:{ Exception -> 0x00bc }
        r3 = r3.revents;	 Catch:{ Exception -> 0x00bc }
        r18 = android.system.OsConstants.POLLIN;	 Catch:{ Exception -> 0x00bc }
        r3 = r3 & r18;
        if (r3 == 0) goto L_0x009d;
    L_0x0077:
        r3 = 1;
        r17 = android.system.Os.read(r4, r12, r14, r3);	 Catch:{ Exception -> 0x00bc }
        if (r17 < 0) goto L_0x0081;
    L_0x007e:
        r14 = r14 + r17;
        goto L_0x0092;
    L_0x0081:
        r3 = new java.lang.RuntimeException;	 Catch:{ Exception -> 0x00bc }
        r8 = "Some error";
        r3.<init>(r8);	 Catch:{ Exception -> 0x00bc }
        throw r3;	 Catch:{ Exception -> 0x00bc }
    L_0x0089:
        r3 = 1;
        r8 = 0;
        if (r0 != 0) goto L_0x0092;
    L_0x008d:
        r3 = "Timed out waiting for child.";
        android.util.Log.w(r6, r3);	 Catch:{ Exception -> 0x00bc }
    L_0x0092:
        r3 = r28;
        r0 = r8;
        r7 = r10;
        r8 = r25;
        r10 = 1;
        goto L_0x003b;
    L_0x009a:
        r10 = r7;
        r25 = r8;
    L_0x009d:
        r0 = r12.length;	 Catch:{ Exception -> 0x00bc }
        if (r14 != r0) goto L_0x00b0;
    L_0x00a0:
        r0 = new java.io.DataInputStream;	 Catch:{ Exception -> 0x00bc }
        r3 = new java.io.ByteArrayInputStream;	 Catch:{ Exception -> 0x00bc }
        r3.<init>(r12);	 Catch:{ Exception -> 0x00bc }
        r0.<init>(r3);	 Catch:{ Exception -> 0x00bc }
        r3 = r0.readInt();	 Catch:{ Exception -> 0x00bc }
        r8 = r3;
        goto L_0x00b2;
    L_0x00b0:
        r8 = r25;
    L_0x00b2:
        r0 = -1;
        if (r8 != r0) goto L_0x00bb;
    L_0x00b5:
        android.util.Log.w(r6, r5);	 Catch:{ Exception -> 0x00b9 }
        goto L_0x00bb;
    L_0x00b9:
        r0 = move-exception;
        goto L_0x00c4;
    L_0x00bb:
        goto L_0x00c7;
    L_0x00bc:
        r0 = move-exception;
        r8 = r25;
        goto L_0x00c4;
    L_0x00c0:
        r0 = move-exception;
        r10 = r7;
        r25 = r8;
    L_0x00c4:
        android.util.Log.w(r6, r5, r0);
    L_0x00c7:
        if (r8 <= 0) goto L_0x010a;
    L_0x00c9:
        r0 = r8;
    L_0x00ca:
        if (r0 <= 0) goto L_0x00d3;
    L_0x00cc:
        if (r0 == r2) goto L_0x00d3;
    L_0x00ce:
        r0 = android.os.Process.getParentPid(r0);
        goto L_0x00ca;
    L_0x00d3:
        if (r0 <= 0) goto L_0x00ec;
    L_0x00d5:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r5 = "Wrapped process has pid ";
        r3.append(r5);
        r3.append(r8);
        r3 = r3.toString();
        android.util.Log.i(r6, r3);
        r2 = r8;
        r7 = 1;
        goto L_0x010b;
    L_0x00ec:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r5 = "Wrapped process reported a pid that is not a child of the process that we forked: childPid=";
        r3.append(r5);
        r3.append(r2);
        r5 = " innerPid=";
        r3.append(r5);
        r3.append(r8);
        r3 = r3.toString();
        android.util.Log.w(r6, r3);
        goto L_0x010a;
    L_0x0109:
        r10 = r7;
    L_0x010a:
        r7 = r10;
    L_0x010b:
        r0 = r1.mSocketOutStream;	 Catch:{ IOException -> 0x0117 }
        r0.writeInt(r2);	 Catch:{ IOException -> 0x0117 }
        r0 = r1.mSocketOutStream;	 Catch:{ IOException -> 0x0117 }
        r0.writeBoolean(r7);	 Catch:{ IOException -> 0x0117 }
        return;
    L_0x0117:
        r0 = move-exception;
        r3 = new java.lang.IllegalStateException;
        r5 = "Error writing to command socket";
        r3.<init>(r5, r0);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.ZygoteConnection.handleParentProc(int, java.io.FileDescriptor[], java.io.FileDescriptor):void");
    }

    private void setChildPgid(int pid) {
        try {
            Os.setpgid(pid, Os.getpgid(this.peer.getPid()));
        } catch (ErrnoException e) {
            Log.i(TAG, "Zygote: setpgid failed. This is normal if peer is not in our session");
        }
    }
}
