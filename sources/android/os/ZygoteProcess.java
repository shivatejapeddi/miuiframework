package android.os;

import android.content.pm.ApplicationInfo;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.net.LocalSocketAddress.Namespace;
import android.os.Process.ProcessStartResult;
import android.util.Log;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.Zygote;
import com.android.internal.os.ZygoteConfig;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ZygoteProcess {
    private static final String[] INVALID_USAP_FLAGS = new String[]{"--query-abi-list", "--get-pid", "--preload-default", "--preload-package", "--preload-app", "--start-child-zygote", "--set-api-blacklist-exemptions", "--hidden-api-log-sampling-rate", "--hidden-api-statslog-sampling-rate", "--invoke-with"};
    private static final String LOG_TAG = "ZygoteProcess";
    private static final String USAP_POOL_ENABLED_DEFAULT = "false";
    private static final int ZYGOTE_CONNECT_RETRY_DELAY_MS = 50;
    private static final int ZYGOTE_CONNECT_TIMEOUT_MS = 20000;
    static final int ZYGOTE_RETRY_MILLIS = 500;
    private List<String> mApiBlacklistExemptions;
    private int mHiddenApiAccessLogSampleRate;
    private int mHiddenApiAccessStatslogSampleRate;
    private boolean mIsFirstPropCheck;
    private long mLastPropCheckTimestamp;
    private final Object mLock;
    private boolean mUsapPoolEnabled;
    private final LocalSocketAddress mUsapPoolSecondarySocketAddress;
    private final LocalSocketAddress mUsapPoolSocketAddress;
    private final LocalSocketAddress mZygoteSecondarySocketAddress;
    private final LocalSocketAddress mZygoteSocketAddress;
    private ZygoteState primaryZygoteState;
    private ZygoteState secondaryZygoteState;

    private static class ZygoteState implements AutoCloseable {
        private final List<String> mAbiList;
        private boolean mClosed;
        final LocalSocketAddress mUsapSocketAddress;
        final DataInputStream mZygoteInputStream;
        final BufferedWriter mZygoteOutputWriter;
        private final LocalSocket mZygoteSessionSocket;
        final LocalSocketAddress mZygoteSocketAddress;

        private ZygoteState(LocalSocketAddress zygoteSocketAddress, LocalSocketAddress usapSocketAddress, LocalSocket zygoteSessionSocket, DataInputStream zygoteInputStream, BufferedWriter zygoteOutputWriter, List<String> abiList) {
            this.mZygoteSocketAddress = zygoteSocketAddress;
            this.mUsapSocketAddress = usapSocketAddress;
            this.mZygoteSessionSocket = zygoteSessionSocket;
            this.mZygoteInputStream = zygoteInputStream;
            this.mZygoteOutputWriter = zygoteOutputWriter;
            this.mAbiList = abiList;
        }

        static ZygoteState connect(LocalSocketAddress zygoteSocketAddress, LocalSocketAddress usapSocketAddress) throws IOException {
            LocalSocket zygoteSessionSocket = new LocalSocket();
            if (zygoteSocketAddress != null) {
                try {
                    zygoteSessionSocket.connect(zygoteSocketAddress);
                    DataInputStream zygoteInputStream = new DataInputStream(zygoteSessionSocket.getInputStream());
                    BufferedWriter zygoteOutputWriter = new BufferedWriter(new OutputStreamWriter(zygoteSessionSocket.getOutputStream()), 256);
                    return new ZygoteState(zygoteSocketAddress, usapSocketAddress, zygoteSessionSocket, zygoteInputStream, zygoteOutputWriter, ZygoteProcess.getAbiList(zygoteOutputWriter, zygoteInputStream));
                } catch (IOException ex) {
                    try {
                        zygoteSessionSocket.close();
                    } catch (IOException e) {
                    }
                    throw ex;
                }
            }
            throw new IllegalArgumentException("zygoteSocketAddress can't be null");
        }

        /* Access modifiers changed, original: 0000 */
        public LocalSocket getUsapSessionSocket() throws IOException {
            LocalSocket usapSessionSocket = new LocalSocket();
            usapSessionSocket.connect(this.mUsapSocketAddress);
            return usapSessionSocket;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean matches(String abi) {
            return this.mAbiList.contains(abi);
        }

        public void close() {
            try {
                this.mZygoteSessionSocket.close();
            } catch (IOException ex) {
                Log.e(ZygoteProcess.LOG_TAG, "I/O exception on routine close", ex);
            }
            this.mClosed = true;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean isClosed() {
            return this.mClosed;
        }
    }

    public ZygoteProcess() {
        this.mLock = new Object();
        this.mApiBlacklistExemptions = Collections.emptyList();
        this.mUsapPoolEnabled = false;
        this.mIsFirstPropCheck = true;
        this.mLastPropCheckTimestamp = 0;
        this.mZygoteSocketAddress = new LocalSocketAddress(Zygote.PRIMARY_SOCKET_NAME, Namespace.RESERVED);
        this.mZygoteSecondarySocketAddress = new LocalSocketAddress(Zygote.SECONDARY_SOCKET_NAME, Namespace.RESERVED);
        this.mUsapPoolSocketAddress = new LocalSocketAddress(Zygote.USAP_POOL_PRIMARY_SOCKET_NAME, Namespace.RESERVED);
        this.mUsapPoolSecondarySocketAddress = new LocalSocketAddress(Zygote.USAP_POOL_SECONDARY_SOCKET_NAME, Namespace.RESERVED);
    }

    public ZygoteProcess(LocalSocketAddress primarySocketAddress, LocalSocketAddress secondarySocketAddress) {
        this.mLock = new Object();
        this.mApiBlacklistExemptions = Collections.emptyList();
        this.mUsapPoolEnabled = false;
        this.mIsFirstPropCheck = true;
        this.mLastPropCheckTimestamp = 0;
        this.mZygoteSocketAddress = primarySocketAddress;
        this.mZygoteSecondarySocketAddress = secondarySocketAddress;
        this.mUsapPoolSocketAddress = null;
        this.mUsapPoolSecondarySocketAddress = null;
    }

    public LocalSocketAddress getPrimarySocketAddress() {
        return this.mZygoteSocketAddress;
    }

    public final ProcessStartResult start(String processClass, String niceName, int uid, int gid, int[] gids, int runtimeFlags, int mountExternal, int targetSdkVersion, String seInfo, String abi, String instructionSet, String appDataDir, String invokeWith, String packageName, boolean useUsapPool, String[] zygoteArgs) {
        if (fetchUsapPoolEnabledPropWithMinInterval()) {
            informZygotesOfUsapPoolStatus();
        }
        try {
            return startViaZygote(processClass, niceName, uid, gid, gids, runtimeFlags, mountExternal, targetSdkVersion, seInfo, abi, instructionSet, appDataDir, invokeWith, false, packageName, useUsapPool, zygoteArgs);
        } catch (ZygoteStartFailedEx ex) {
            ZygoteStartFailedEx ex2 = ex2;
            String str = "Starting VM process through Zygote failed";
            Log.e(LOG_TAG, str);
            throw new RuntimeException(str, ex2);
        }
    }

    @GuardedBy({"mLock"})
    private static List<String> getAbiList(BufferedWriter writer, DataInputStream inputStream) throws IOException {
        writer.write("1");
        writer.newLine();
        writer.write("--query-abi-list");
        writer.newLine();
        writer.flush();
        byte[] bytes = new byte[inputStream.readInt()];
        inputStream.readFully(bytes);
        return Arrays.asList(new String(bytes, StandardCharsets.US_ASCII).split(","));
    }

    @GuardedBy({"mLock"})
    private ProcessStartResult zygoteSendArgsAndGetResult(ZygoteState zygoteState, boolean useUsapPool, ArrayList<String> args) throws ZygoteStartFailedEx {
        String arg;
        Iterator it = args.iterator();
        while (it.hasNext()) {
            arg = (String) it.next();
            if (arg.indexOf(10) >= 0) {
                throw new ZygoteStartFailedEx("Embedded newlines not allowed");
            } else if (arg.indexOf(13) >= 0) {
                throw new ZygoteStartFailedEx("Embedded carriage returns not allowed");
            }
        }
        String msgStr = new StringBuilder();
        msgStr.append(args.size());
        arg = "\n";
        msgStr.append(arg);
        msgStr.append(String.join(arg, args));
        msgStr.append(arg);
        msgStr = msgStr.toString();
        if (useUsapPool && this.mUsapPoolEnabled && canAttemptUsap(args)) {
            try {
                return attemptUsapSendArgsAndGetResult(zygoteState, msgStr);
            } catch (IOException ex) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("IO Exception while communicating with USAP pool - ");
                stringBuilder.append(ex.getMessage());
                Log.e(LOG_TAG, stringBuilder.toString());
            }
        }
        return attemptZygoteSendArgsAndGetResult(zygoteState, msgStr);
    }

    private ProcessStartResult attemptZygoteSendArgsAndGetResult(ZygoteState zygoteState, String msgStr) throws ZygoteStartFailedEx {
        try {
            BufferedWriter zygoteWriter = zygoteState.mZygoteOutputWriter;
            DataInputStream zygoteInputStream = zygoteState.mZygoteInputStream;
            zygoteWriter.write(msgStr);
            zygoteWriter.flush();
            ProcessStartResult result = new ProcessStartResult();
            result.pid = zygoteInputStream.readInt();
            result.usingWrapper = zygoteInputStream.readBoolean();
            if (result.pid >= 0) {
                return result;
            }
            throw new ZygoteStartFailedEx("fork() failed");
        } catch (IOException ex) {
            zygoteState.close();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("IO Exception while communicating with Zygote - ");
            stringBuilder.append(ex.toString());
            Log.e(LOG_TAG, stringBuilder.toString());
            throw new ZygoteStartFailedEx(ex);
        }
    }

    /* JADX WARNING: Missing block: B:13:0x0045, code skipped:
            if (r0 != null) goto L_0x0047;
     */
    /* JADX WARNING: Missing block: B:15:?, code skipped:
            r0.close();
     */
    /* JADX WARNING: Missing block: B:16:0x004b, code skipped:
            r3 = move-exception;
     */
    /* JADX WARNING: Missing block: B:17:0x004c, code skipped:
            r1.addSuppressed(r3);
     */
    private android.os.Process.ProcessStartResult attemptUsapSendArgsAndGetResult(android.os.ZygoteProcess.ZygoteState r7, java.lang.String r8) throws android.os.ZygoteStartFailedEx, java.io.IOException {
        /*
        r6 = this;
        r0 = r7.getUsapSessionSocket();
        r1 = new java.io.BufferedWriter;	 Catch:{ all -> 0x0042 }
        r2 = new java.io.OutputStreamWriter;	 Catch:{ all -> 0x0042 }
        r3 = r0.getOutputStream();	 Catch:{ all -> 0x0042 }
        r2.<init>(r3);	 Catch:{ all -> 0x0042 }
        r3 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
        r1.<init>(r2, r3);	 Catch:{ all -> 0x0042 }
        r2 = new java.io.DataInputStream;	 Catch:{ all -> 0x0042 }
        r3 = r0.getInputStream();	 Catch:{ all -> 0x0042 }
        r2.<init>(r3);	 Catch:{ all -> 0x0042 }
        r1.write(r8);	 Catch:{ all -> 0x0042 }
        r1.flush();	 Catch:{ all -> 0x0042 }
        r3 = new android.os.Process$ProcessStartResult;	 Catch:{ all -> 0x0042 }
        r3.<init>();	 Catch:{ all -> 0x0042 }
        r4 = r2.readInt();	 Catch:{ all -> 0x0042 }
        r3.pid = r4;	 Catch:{ all -> 0x0042 }
        r4 = 0;
        r3.usingWrapper = r4;	 Catch:{ all -> 0x0042 }
        r4 = r3.pid;	 Catch:{ all -> 0x0042 }
        if (r4 < 0) goto L_0x003a;
        r0.close();
        return r3;
    L_0x003a:
        r4 = new android.os.ZygoteStartFailedEx;	 Catch:{ all -> 0x0042 }
        r5 = "USAP specialization failed";
        r4.<init>(r5);	 Catch:{ all -> 0x0042 }
        throw r4;	 Catch:{ all -> 0x0042 }
    L_0x0042:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x0044 }
    L_0x0044:
        r2 = move-exception;
        if (r0 == 0) goto L_0x004f;
    L_0x0047:
        r0.close();	 Catch:{ all -> 0x004b }
        goto L_0x004f;
    L_0x004b:
        r3 = move-exception;
        r1.addSuppressed(r3);
    L_0x004f:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.ZygoteProcess.attemptUsapSendArgsAndGetResult(android.os.ZygoteProcess$ZygoteState, java.lang.String):android.os.Process$ProcessStartResult");
    }

    private static boolean canAttemptUsap(ArrayList<String> args) {
        Iterator it = args.iterator();
        while (it.hasNext()) {
            String flag = (String) it.next();
            for (String badFlag : INVALID_USAP_FLAGS) {
                if (flag.startsWith(badFlag)) {
                    return false;
                }
            }
            if (flag.startsWith("--nice-name=")) {
                String niceName = flag.substring(12);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("wrap.");
                stringBuilder.append(niceName);
                String property_value = SystemProperties.get(stringBuilder.toString());
                if (!(property_value == null || property_value.length() == 0)) {
                    return false;
                }
            }
        }
        return true;
    }

    private ProcessStartResult startViaZygote(String processClass, String niceName, int uid, int gid, int[] gids, int runtimeFlags, int mountExternal, int targetSdkVersion, String seInfo, String abi, String instructionSet, String appDataDir, String invokeWith, boolean startChildZygote, String packageName, boolean useUsapPool, String[] extraArgs) throws ZygoteStartFailedEx {
        Throwable th;
        String str = niceName;
        int[] iArr = gids;
        int i = mountExternal;
        String str2 = seInfo;
        String str3 = instructionSet;
        String str4 = appDataDir;
        String str5 = invokeWith;
        String str6 = packageName;
        String[] strArr = extraArgs;
        ArrayList<String> argsForZygote = new ArrayList();
        argsForZygote.add("--runtime-args");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("--setuid=");
        stringBuilder.append(uid);
        argsForZygote.add(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("--setgid=");
        stringBuilder.append(gid);
        argsForZygote.add(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("--runtime-flags=");
        stringBuilder.append(runtimeFlags);
        argsForZygote.add(stringBuilder.toString());
        if (i == 1) {
            argsForZygote.add("--mount-external-default");
        } else if (i == 2) {
            argsForZygote.add("--mount-external-read");
        } else if (i == 3) {
            argsForZygote.add("--mount-external-write");
        } else if (i == 6) {
            argsForZygote.add("--mount-external-full");
        } else if (i == 5) {
            argsForZygote.add("--mount-external-installer");
        } else if (i == 4) {
            argsForZygote.add("--mount-external-legacy");
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("--target-sdk-version=");
        stringBuilder.append(targetSdkVersion);
        argsForZygote.add(stringBuilder.toString());
        if (iArr != null && iArr.length > 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("--setgroups=");
            i = iArr.length;
            int i2 = 0;
            while (i2 < i) {
                int sz;
                if (i2 != 0) {
                    sz = i;
                    stringBuilder.append(',');
                } else {
                    sz = i;
                }
                stringBuilder.append(iArr[i2]);
                i2++;
                i = sz;
            }
            argsForZygote.add(stringBuilder.toString());
        }
        if (str != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("--nice-name=");
            stringBuilder.append(str);
            argsForZygote.add(stringBuilder.toString());
        }
        if (str2 != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("--seinfo=");
            stringBuilder.append(str2);
            argsForZygote.add(stringBuilder.toString());
        }
        if (str3 != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("--instruction-set=");
            stringBuilder.append(str3);
            argsForZygote.add(stringBuilder.toString());
        }
        if (str4 != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("--app-data-dir=");
            stringBuilder.append(str4);
            argsForZygote.add(stringBuilder.toString());
        }
        if (str5 != null) {
            argsForZygote.add("--invoke-with");
            argsForZygote.add(str5);
        }
        if (startChildZygote) {
            argsForZygote.add("--start-child-zygote");
        }
        if (str6 != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("--package-name=");
            stringBuilder.append(str6);
            argsForZygote.add(stringBuilder.toString());
        }
        argsForZygote.add(processClass);
        if (strArr != null) {
            Collections.addAll(argsForZygote, strArr);
        }
        synchronized (this.mLock) {
            try {
                ProcessStartResult zygoteSendArgsAndGetResult = zygoteSendArgsAndGetResult(openZygoteSocketIfNeeded(abi), useUsapPool, argsForZygote);
                return zygoteSendArgsAndGetResult;
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    private boolean fetchUsapPoolEnabledProp() {
        boolean origVal = this.mUsapPoolEnabled;
        String str = USAP_POOL_ENABLED_DEFAULT;
        String str2 = ZygoteConfig.USAP_POOL_ENABLED;
        if (!Zygote.getConfigurationProperty(str2, str).isEmpty()) {
            this.mUsapPoolEnabled = Zygote.getConfigurationPropertyBoolean(str2, Boolean.valueOf(Boolean.parseBoolean(str)));
        }
        boolean valueChanged = origVal != this.mUsapPoolEnabled;
        if (valueChanged) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("usapPoolEnabled = ");
            stringBuilder.append(this.mUsapPoolEnabled);
            Log.i(LOG_TAG, stringBuilder.toString());
        }
        return valueChanged;
    }

    private boolean fetchUsapPoolEnabledPropWithMinInterval() {
        long currentTimestamp = SystemClock.elapsedRealtime();
        if (SystemProperties.get("dalvik.vm.boot-image", "").endsWith("apex.art") && currentTimestamp <= 15000) {
            return false;
        }
        if (!this.mIsFirstPropCheck && currentTimestamp - this.mLastPropCheckTimestamp < 60000) {
            return false;
        }
        this.mIsFirstPropCheck = false;
        this.mLastPropCheckTimestamp = currentTimestamp;
        return fetchUsapPoolEnabledProp();
    }

    public void close() {
        ZygoteState zygoteState = this.primaryZygoteState;
        if (zygoteState != null) {
            zygoteState.close();
        }
        zygoteState = this.secondaryZygoteState;
        if (zygoteState != null) {
            zygoteState.close();
        }
    }

    public void establishZygoteConnectionForAbi(String abi) {
        try {
            synchronized (this.mLock) {
                openZygoteSocketIfNeeded(abi);
            }
        } catch (ZygoteStartFailedEx ex) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to connect to zygote for abi: ");
            stringBuilder.append(abi);
            throw new RuntimeException(stringBuilder.toString(), ex);
        }
    }

    public int getZygotePid(String abi) {
        try {
            int parseInt;
            synchronized (this.mLock) {
                ZygoteState state = openZygoteSocketIfNeeded(abi);
                state.mZygoteOutputWriter.write("1");
                state.mZygoteOutputWriter.newLine();
                state.mZygoteOutputWriter.write("--get-pid");
                state.mZygoteOutputWriter.newLine();
                state.mZygoteOutputWriter.flush();
                byte[] bytes = new byte[state.mZygoteInputStream.readInt()];
                state.mZygoteInputStream.readFully(bytes);
                parseInt = Integer.parseInt(new String(bytes, StandardCharsets.US_ASCII));
            }
            return parseInt;
        } catch (Exception ex) {
            throw new RuntimeException("Failure retrieving pid", ex);
        }
    }

    public boolean setApiBlacklistExemptions(List<String> exemptions) {
        boolean ok;
        synchronized (this.mLock) {
            this.mApiBlacklistExemptions = exemptions;
            ok = maybeSetApiBlacklistExemptions(this.primaryZygoteState, true);
            if (ok) {
                ok = maybeSetApiBlacklistExemptions(this.secondaryZygoteState, true);
            }
        }
        return ok;
    }

    public void setHiddenApiAccessLogSampleRate(int rate) {
        synchronized (this.mLock) {
            this.mHiddenApiAccessLogSampleRate = rate;
            maybeSetHiddenApiAccessLogSampleRate(this.primaryZygoteState);
            maybeSetHiddenApiAccessLogSampleRate(this.secondaryZygoteState);
        }
    }

    public void setHiddenApiAccessStatslogSampleRate(int rate) {
        synchronized (this.mLock) {
            this.mHiddenApiAccessStatslogSampleRate = rate;
            maybeSetHiddenApiAccessStatslogSampleRate(this.primaryZygoteState);
            maybeSetHiddenApiAccessStatslogSampleRate(this.secondaryZygoteState);
        }
    }

    @GuardedBy({"mLock"})
    private boolean maybeSetApiBlacklistExemptions(ZygoteState state, boolean sendIfEmpty) {
        String str = LOG_TAG;
        if (state == null || state.isClosed()) {
            Slog.e(str, "Can't set API blacklist exemptions: no zygote connection");
            return false;
        } else if (!sendIfEmpty && this.mApiBlacklistExemptions.isEmpty()) {
            return true;
        } else {
            try {
                int i;
                state.mZygoteOutputWriter.write(Integer.toString(this.mApiBlacklistExemptions.size() + 1));
                state.mZygoteOutputWriter.newLine();
                state.mZygoteOutputWriter.write("--set-api-blacklist-exemptions");
                state.mZygoteOutputWriter.newLine();
                for (i = 0; i < this.mApiBlacklistExemptions.size(); i++) {
                    state.mZygoteOutputWriter.write((String) this.mApiBlacklistExemptions.get(i));
                    state.mZygoteOutputWriter.newLine();
                }
                state.mZygoteOutputWriter.flush();
                i = state.mZygoteInputStream.readInt();
                if (i != 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to set API blacklist exemptions; status ");
                    stringBuilder.append(i);
                    Slog.e(str, stringBuilder.toString());
                }
                return true;
            } catch (IOException ioe) {
                Slog.e(str, "Failed to set API blacklist exemptions", ioe);
                this.mApiBlacklistExemptions = Collections.emptyList();
                return false;
            }
        }
    }

    private void maybeSetHiddenApiAccessLogSampleRate(ZygoteState state) {
        String str = LOG_TAG;
        if (state != null && !state.isClosed() && this.mHiddenApiAccessLogSampleRate != -1) {
            try {
                state.mZygoteOutputWriter.write(Integer.toString(1));
                state.mZygoteOutputWriter.newLine();
                BufferedWriter bufferedWriter = state.mZygoteOutputWriter;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("--hidden-api-log-sampling-rate=");
                stringBuilder.append(this.mHiddenApiAccessLogSampleRate);
                bufferedWriter.write(stringBuilder.toString());
                state.mZygoteOutputWriter.newLine();
                state.mZygoteOutputWriter.flush();
                int status = state.mZygoteInputStream.readInt();
                if (status != 0) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to set hidden API log sampling rate; status ");
                    stringBuilder.append(status);
                    Slog.e(str, stringBuilder.toString());
                }
            } catch (IOException ioe) {
                Slog.e(str, "Failed to set hidden API log sampling rate", ioe);
            }
        }
    }

    private void maybeSetHiddenApiAccessStatslogSampleRate(ZygoteState state) {
        String str = LOG_TAG;
        if (state != null && !state.isClosed() && this.mHiddenApiAccessStatslogSampleRate != -1) {
            try {
                state.mZygoteOutputWriter.write(Integer.toString(1));
                state.mZygoteOutputWriter.newLine();
                BufferedWriter bufferedWriter = state.mZygoteOutputWriter;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("--hidden-api-statslog-sampling-rate=");
                stringBuilder.append(this.mHiddenApiAccessStatslogSampleRate);
                bufferedWriter.write(stringBuilder.toString());
                state.mZygoteOutputWriter.newLine();
                state.mZygoteOutputWriter.flush();
                int status = state.mZygoteInputStream.readInt();
                if (status != 0) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to set hidden API statslog sampling rate; status ");
                    stringBuilder.append(status);
                    Slog.e(str, stringBuilder.toString());
                }
            } catch (IOException ioe) {
                Slog.e(str, "Failed to set hidden API statslog sampling rate", ioe);
            }
        }
    }

    @GuardedBy({"mLock"})
    private void attemptConnectionToPrimaryZygote() throws IOException {
        ZygoteState zygoteState = this.primaryZygoteState;
        if (zygoteState == null || zygoteState.isClosed()) {
            this.primaryZygoteState = ZygoteState.connect(this.mZygoteSocketAddress, this.mUsapPoolSocketAddress);
            maybeSetApiBlacklistExemptions(this.primaryZygoteState, false);
            maybeSetHiddenApiAccessLogSampleRate(this.primaryZygoteState);
            maybeSetHiddenApiAccessStatslogSampleRate(this.primaryZygoteState);
        }
    }

    @GuardedBy({"mLock"})
    private void attemptConnectionToSecondaryZygote() throws IOException {
        ZygoteState zygoteState = this.secondaryZygoteState;
        if (zygoteState == null || zygoteState.isClosed()) {
            this.secondaryZygoteState = ZygoteState.connect(this.mZygoteSecondarySocketAddress, this.mUsapPoolSecondarySocketAddress);
            maybeSetApiBlacklistExemptions(this.secondaryZygoteState, false);
            maybeSetHiddenApiAccessLogSampleRate(this.secondaryZygoteState);
            maybeSetHiddenApiAccessStatslogSampleRate(this.secondaryZygoteState);
        }
    }

    @GuardedBy({"mLock"})
    private ZygoteState openZygoteSocketIfNeeded(String abi) throws ZygoteStartFailedEx {
        try {
            attemptConnectionToPrimaryZygote();
            if (this.primaryZygoteState.matches(abi)) {
                return this.primaryZygoteState;
            }
            if (this.mZygoteSecondarySocketAddress != null) {
                attemptConnectionToSecondaryZygote();
                if (this.secondaryZygoteState.matches(abi)) {
                    return this.secondaryZygoteState;
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported zygote ABI: ");
            stringBuilder.append(abi);
            throw new ZygoteStartFailedEx(stringBuilder.toString());
        } catch (IOException ioe) {
            throw new ZygoteStartFailedEx("Error connecting to zygote", ioe);
        }
    }

    public boolean preloadApp(ApplicationInfo appInfo, String abi) throws ZygoteStartFailedEx, IOException {
        boolean z;
        synchronized (this.mLock) {
            ZygoteState state = openZygoteSocketIfNeeded(abi);
            state.mZygoteOutputWriter.write("2");
            state.mZygoteOutputWriter.newLine();
            state.mZygoteOutputWriter.write("--preload-app");
            state.mZygoteOutputWriter.newLine();
            Parcel parcel = Parcel.obtain();
            z = false;
            appInfo.writeToParcel(parcel, 0);
            String encodedParcelData = Base64.getEncoder().encodeToString(parcel.marshall());
            parcel.recycle();
            state.mZygoteOutputWriter.write(encodedParcelData);
            state.mZygoteOutputWriter.newLine();
            state.mZygoteOutputWriter.flush();
            if (state.mZygoteInputStream.readInt() == 0) {
                z = true;
            }
        }
        return z;
    }

    public boolean preloadPackageForAbi(String packagePath, String libsPath, String libFileName, String cacheKey, String abi) throws ZygoteStartFailedEx, IOException {
        boolean z;
        synchronized (this.mLock) {
            ZygoteState state = openZygoteSocketIfNeeded(abi);
            state.mZygoteOutputWriter.write("5");
            state.mZygoteOutputWriter.newLine();
            state.mZygoteOutputWriter.write("--preload-package");
            state.mZygoteOutputWriter.newLine();
            state.mZygoteOutputWriter.write(packagePath);
            state.mZygoteOutputWriter.newLine();
            state.mZygoteOutputWriter.write(libsPath);
            state.mZygoteOutputWriter.newLine();
            state.mZygoteOutputWriter.write(libFileName);
            state.mZygoteOutputWriter.newLine();
            state.mZygoteOutputWriter.write(cacheKey);
            state.mZygoteOutputWriter.newLine();
            state.mZygoteOutputWriter.flush();
            z = state.mZygoteInputStream.readInt() == 0;
        }
        return z;
    }

    public boolean preloadDefault(String abi) throws ZygoteStartFailedEx, IOException {
        boolean z;
        synchronized (this.mLock) {
            ZygoteState state = openZygoteSocketIfNeeded(abi);
            state.mZygoteOutputWriter.write("1");
            state.mZygoteOutputWriter.newLine();
            state.mZygoteOutputWriter.write("--preload-default");
            state.mZygoteOutputWriter.newLine();
            state.mZygoteOutputWriter.flush();
            z = state.mZygoteInputStream.readInt() == 0;
        }
        return z;
    }

    public static void waitForConnectionToZygote(String zygoteSocketName) {
        waitForConnectionToZygote(new LocalSocketAddress(zygoteSocketName, Namespace.RESERVED));
    }

    public static void waitForConnectionToZygote(LocalSocketAddress zygoteSocketAddress) {
        int n = 400;
        while (true) {
            String str = LOG_TAG;
            if (n >= 0) {
                try {
                    ZygoteState.connect(zygoteSocketAddress, null).close();
                    return;
                } catch (IOException ioe) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Got error connecting to zygote, retrying. msg= ");
                    stringBuilder.append(ioe.getMessage());
                    Log.w(str, stringBuilder.toString());
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                    }
                    n--;
                }
            } else {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Failed to connect to Zygote through socket ");
                stringBuilder2.append(zygoteSocketAddress.getName());
                Slog.wtf(str, stringBuilder2.toString());
                return;
            }
        }
    }

    private void informZygotesOfUsapPoolStatus() {
        String command = new StringBuilder();
        command.append("1\n--usap-pool-enabled=");
        command.append(this.mUsapPoolEnabled);
        command.append("\n");
        command = command.toString();
        synchronized (this.mLock) {
            try {
                attemptConnectionToPrimaryZygote();
                this.primaryZygoteState.mZygoteOutputWriter.write(command);
                this.primaryZygoteState.mZygoteOutputWriter.flush();
                try {
                    if (this.mZygoteSecondarySocketAddress != null) {
                        try {
                            attemptConnectionToSecondaryZygote();
                            this.secondaryZygoteState.mZygoteOutputWriter.write(command);
                            this.secondaryZygoteState.mZygoteOutputWriter.flush();
                            this.secondaryZygoteState.mZygoteInputStream.readInt();
                        } catch (IOException ioe) {
                            throw new IllegalStateException("USAP pool state change cause an irrecoverable error", ioe);
                        } catch (IOException e) {
                        }
                    }
                    this.primaryZygoteState.mZygoteInputStream.readInt();
                } catch (IOException ioe2) {
                    throw new IllegalStateException("USAP pool state change cause an irrecoverable error", ioe2);
                } catch (Throwable th) {
                }
            } catch (IOException ioe22) {
                this.mUsapPoolEnabled = !this.mUsapPoolEnabled;
                String str = LOG_TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to inform zygotes of USAP pool status: ");
                stringBuilder.append(ioe22.getMessage());
                Log.w(str, stringBuilder.toString());
            }
        }
    }

    public ChildZygoteProcess startChildZygote(String processClass, String niceName, int uid, int gid, int[] gids, int runtimeFlags, String seInfo, String abi, String acceptedAbiList, String instructionSet, int uidRangeStart, int uidRangeEnd) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(processClass);
        stringBuilder.append("/");
        stringBuilder.append(UUID.randomUUID().toString());
        LocalSocketAddress serverAddress = new LocalSocketAddress(stringBuilder.toString());
        r0 = new String[4];
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(Zygote.CHILD_ZYGOTE_SOCKET_NAME_ARG);
        stringBuilder2.append(serverAddress.getName());
        r0[0] = stringBuilder2.toString();
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(Zygote.CHILD_ZYGOTE_ABI_LIST_ARG);
        stringBuilder2.append(acceptedAbiList);
        r0[1] = stringBuilder2.toString();
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(Zygote.CHILD_ZYGOTE_UID_RANGE_START);
        stringBuilder2.append(uidRangeStart);
        r0[2] = stringBuilder2.toString();
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(Zygote.CHILD_ZYGOTE_UID_RANGE_END);
        stringBuilder2.append(uidRangeEnd);
        r0[3] = stringBuilder2.toString();
        try {
            return new ChildZygoteProcess(serverAddress, startViaZygote(processClass, niceName, uid, gid, gids, runtimeFlags, 0, 0, seInfo, abi, instructionSet, null, null, true, null, false, r0).pid);
        } catch (ZygoteStartFailedEx e) {
            throw new RuntimeException("Starting child-zygote through Zygote failed", e);
        }
    }
}
