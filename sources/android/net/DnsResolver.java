package android.net;

import android.net.DnsPacket.DnsRecord;
import android.net.util.DnsUtils;
import android.os.CancellationSignal;
import android.os.Looper;
import android.os.MessageQueue;
import android.system.ErrnoException;
import android.util.Log;
import java.io.FileDescriptor;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public final class DnsResolver {
    public static final int CLASS_IN = 1;
    public static final int ERROR_PARSE = 0;
    public static final int ERROR_SYSTEM = 1;
    private static final int FD_EVENTS = 5;
    public static final int FLAG_EMPTY = 0;
    public static final int FLAG_NO_CACHE_LOOKUP = 4;
    public static final int FLAG_NO_CACHE_STORE = 2;
    public static final int FLAG_NO_RETRY = 1;
    private static final int MAXPACKET = 8192;
    private static final int NETID_UNSET = 0;
    private static final int SLEEP_TIME_MS = 2;
    private static final String TAG = "DnsResolver";
    public static final int TYPE_A = 1;
    public static final int TYPE_AAAA = 28;
    private static final DnsResolver sInstance = new DnsResolver();

    public interface Callback<T> {
        void onAnswer(T t, int i);

        void onError(DnsException dnsException);
    }

    private static class DnsAddressAnswer extends DnsPacket {
        private static final boolean DBG = false;
        private static final String TAG = "DnsResolver.DnsAddressAnswer";
        private final int mQueryType;

        DnsAddressAnswer(byte[] data) throws ParseException {
            super(data);
            if ((this.mHeader.flags & 32768) == 0) {
                throw new ParseException("Not an answer packet");
            } else if (this.mHeader.getRecordCount(0) != 0) {
                this.mQueryType = ((DnsRecord) this.mRecords[0].get(0)).nsType;
            } else {
                throw new ParseException("No question found");
            }
        }

        public List<InetAddress> getAddresses() {
            List<InetAddress> results = new ArrayList();
            if (this.mHeader.getRecordCount(1) == 0) {
                return results;
            }
            for (DnsRecord ansSec : this.mRecords[1]) {
                int nsType = ansSec.nsType;
                if (nsType == this.mQueryType) {
                    if (nsType == 1 || nsType == 28) {
                        try {
                            results.add(InetAddress.getByAddress(ansSec.getRR()));
                        } catch (UnknownHostException e) {
                        }
                    }
                }
            }
            return results;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @interface DnsError {
    }

    public static class DnsException extends Exception {
        public final int code;

        DnsException(int code, Throwable cause) {
            super(cause);
            this.code = code;
        }
    }

    public static final class DnsResponse {
        public final byte[] answerbuf;
        public final int rcode;

        public DnsResponse(byte[] answerbuf, int rcode) {
            this.answerbuf = answerbuf;
            this.rcode = rcode;
        }
    }

    private class InetAddressAnswerAccumulator implements Callback<byte[]> {
        private final List<InetAddress> mAllAnswers;
        private DnsException mDnsException;
        private final Network mNetwork;
        private int mRcode;
        private int mReceivedAnswerCount = 0;
        private final int mTargetAnswerCount;
        private final Callback<? super List<InetAddress>> mUserCallback;

        InetAddressAnswerAccumulator(Network network, int size, Callback<? super List<InetAddress>> callback) {
            this.mNetwork = network;
            this.mTargetAnswerCount = size;
            this.mAllAnswers = new ArrayList();
            this.mUserCallback = callback;
        }

        private boolean maybeReportError() {
            int i = this.mRcode;
            if (i != 0) {
                this.mUserCallback.onAnswer(this.mAllAnswers, i);
                return true;
            }
            DnsException dnsException = this.mDnsException;
            if (dnsException == null) {
                return false;
            }
            this.mUserCallback.onError(dnsException);
            return true;
        }

        private void maybeReportAnswer() {
            int i = this.mReceivedAnswerCount + 1;
            this.mReceivedAnswerCount = i;
            if (i == this.mTargetAnswerCount) {
                if (!this.mAllAnswers.isEmpty() || !maybeReportError()) {
                    this.mUserCallback.onAnswer(DnsUtils.rfc6724Sort(this.mNetwork, this.mAllAnswers), this.mRcode);
                }
            }
        }

        public void onAnswer(byte[] answer, int rcode) {
            if (this.mReceivedAnswerCount == 0 || rcode == 0) {
                this.mRcode = rcode;
            }
            try {
                this.mAllAnswers.addAll(new DnsAddressAnswer(answer).getAddresses());
            } catch (ParseException e) {
                this.mDnsException = new DnsException(0, e);
            }
            maybeReportAnswer();
        }

        public void onError(DnsException error) {
            this.mDnsException = error;
            maybeReportAnswer();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @interface QueryClass {
    }

    @Retention(RetentionPolicy.SOURCE)
    @interface QueryFlag {
    }

    @Retention(RetentionPolicy.SOURCE)
    @interface QueryType {
    }

    public static DnsResolver getInstance() {
        return sInstance;
    }

    private DnsResolver() {
    }

    public void rawQuery(Network network, byte[] query, int flags, Executor executor, CancellationSignal cancellationSignal, Callback<? super byte[]> callback) {
        if (cancellationSignal == null || !cancellationSignal.isCanceled()) {
            int netIdForResolv;
            Object lock = new Object();
            if (network != null) {
                try {
                    netIdForResolv = network.getNetIdForResolv();
                } catch (ErrnoException e) {
                    executor.execute(new -$$Lambda$DnsResolver$h2SsAzA5_rVr-mzxppK8PJLQe98(callback, e));
                    return;
                }
            }
            netIdForResolv = 0;
            FileDescriptor queryfd = NetworkUtils.resNetworkSend(netIdForResolv, query, query.length, flags);
            synchronized (lock) {
                registerFDListener(executor, queryfd, callback, cancellationSignal, lock);
                if (cancellationSignal == null) {
                    return;
                }
                addCancellationSignal(cancellationSignal, queryfd, lock);
            }
        }
    }

    public void rawQuery(Network network, String domain, int nsClass, int nsType, int flags, Executor executor, CancellationSignal cancellationSignal, Callback<? super byte[]> callback) {
        ErrnoException e;
        Throwable th;
        CancellationSignal cancellationSignal2 = cancellationSignal;
        if (cancellationSignal2 == null || !cancellationSignal.isCanceled()) {
            int netIdForResolv;
            Object lock = new Object();
            if (network != null) {
                try {
                    netIdForResolv = network.getNetIdForResolv();
                } catch (ErrnoException e2) {
                    e = e2;
                    String str = domain;
                    int i = nsClass;
                    int i2 = nsType;
                    int i3 = flags;
                }
            } else {
                netIdForResolv = 0;
            }
            try {
                FileDescriptor queryfd = NetworkUtils.resNetworkQuery(netIdForResolv, domain, nsClass, nsType, flags);
                synchronized (lock) {
                    try {
                        registerFDListener(executor, queryfd, callback, cancellationSignal, lock);
                        if (cancellationSignal2 == null) {
                            return;
                        }
                        addCancellationSignal(cancellationSignal2, queryfd, lock);
                    } catch (Throwable th2) {
                        th = th2;
                        throw th;
                    }
                }
            } catch (ErrnoException e3) {
                e = e3;
                executor.execute(new -$$Lambda$DnsResolver$GTAgQiExADAzbCx0WiV_97W72-g(callback, e));
            }
        }
    }

    public void query(Network network, String domain, int flags, Executor executor, CancellationSignal cancellationSignal, Callback<? super List<InetAddress>> callback) {
        FileDescriptor v6fd;
        Throwable th;
        Network network2;
        String str = domain;
        int i = flags;
        Executor executor2 = executor;
        CancellationSignal cancellationSignal2 = cancellationSignal;
        Callback<? super List<InetAddress>> callback2 = callback;
        if (cancellationSignal2 == null || !cancellationSignal.isCanceled()) {
            Network network3;
            Object lock = new Object();
            if (network != null) {
                network3 = network;
            } else {
                try {
                    network3 = NetworkUtils.getDnsNetwork();
                } catch (ErrnoException e) {
                    executor2.execute(new -$$Lambda$DnsResolver$vvKhya16sREGcN1Gxnqgw-LBoV4(callback2, e));
                    return;
                }
            }
            Network queryNetwork = network3;
            boolean queryIpv6 = DnsUtils.haveIpv6(queryNetwork);
            boolean queryIpv4 = DnsUtils.haveIpv4(queryNetwork);
            if (queryIpv6 || queryIpv4) {
                FileDescriptor v4fd;
                int queryCount;
                int queryCount2 = 0;
                if (queryIpv6) {
                    try {
                        queryCount2 = 0 + 1;
                        v6fd = NetworkUtils.resNetworkQuery(queryNetwork.getNetIdForResolv(), str, 1, 28, i);
                    } catch (ErrnoException e2) {
                        executor2.execute(new -$$Lambda$DnsResolver$uxb9gSgrd6Qyj9SLhCAtOvpxa3I(callback2, e2));
                        return;
                    }
                }
                v6fd = null;
                try {
                    Thread.sleep(2);
                } catch (InterruptedException ex) {
                    InterruptedException ex2 = ex2;
                    Thread.currentThread().interrupt();
                }
                if (queryIpv4) {
                    try {
                        v4fd = NetworkUtils.resNetworkQuery(queryNetwork.getNetIdForResolv(), str, 1, 1, i);
                        queryCount = queryCount2 + 1;
                    } catch (ErrnoException e22) {
                        if (queryIpv6) {
                            NetworkUtils.resNetworkCancel(v6fd);
                        }
                        executor2.execute(new -$$Lambda$DnsResolver$t5xp-fS_zTQ564hG-PIaWJdBP8c(callback2, e22));
                        return;
                    }
                }
                v4fd = null;
                queryCount = queryCount2;
                InetAddressAnswerAccumulator accumulator = new InetAddressAnswerAccumulator(queryNetwork, queryCount, callback2);
                synchronized (lock) {
                    if (queryIpv6) {
                        try {
                            registerFDListener(executor, v6fd, accumulator, cancellationSignal, lock);
                        } catch (Throwable th2) {
                            th = th2;
                            queryCount2 = queryCount;
                            network2 = queryNetwork;
                        }
                    }
                    if (queryIpv4) {
                        queryCount2 = queryCount;
                        try {
                            registerFDListener(executor, v4fd, accumulator, cancellationSignal, lock);
                        } catch (Throwable th3) {
                            th = th3;
                            throw th;
                        }
                    }
                    network2 = queryNetwork;
                    if (cancellationSignal2 == null) {
                        return;
                    }
                    cancellationSignal2.setOnCancelListener(new -$$Lambda$DnsResolver$DW9jYL2ZOH6BjebIVPhZIrrhoD8(this, lock, queryIpv4, v4fd, queryIpv6, v6fd));
                    return;
                }
            }
            executor2.execute(new -$$Lambda$DnsResolver$kjq9c3feWPGKUPV3AzJBFi1GUvw(callback2));
        }
    }

    public /* synthetic */ void lambda$query$6$DnsResolver(Object lock, boolean queryIpv4, FileDescriptor v4fd, boolean queryIpv6, FileDescriptor v6fd) {
        synchronized (lock) {
            if (queryIpv4) {
                try {
                    cancelQuery(v4fd);
                } catch (Throwable th) {
                }
            }
            if (queryIpv6) {
                cancelQuery(v6fd);
            }
        }
    }

    public void query(Network network, String domain, int nsType, int flags, Executor executor, CancellationSignal cancellationSignal, Callback<? super List<InetAddress>> callback) {
        ErrnoException e;
        CancellationSignal cancellationSignal2 = cancellationSignal;
        Callback<? super List<InetAddress>> callback2 = callback;
        if (cancellationSignal2 == null || !cancellationSignal.isCanceled()) {
            Network network2;
            Object lock = new Object();
            if (network != null) {
                network2 = network;
            } else {
                try {
                    network2 = NetworkUtils.getDnsNetwork();
                } catch (ErrnoException e2) {
                    e = e2;
                    String str = domain;
                    int i = nsType;
                    int i2 = flags;
                    executor.execute(new -$$Lambda$DnsResolver$wc3_cnx2aezlAHvMEbQVFaTPAcE(callback2, e));
                }
            }
            Network queryNetwork = network2;
            try {
                FileDescriptor queryfd = NetworkUtils.resNetworkQuery(queryNetwork.getNetIdForResolv(), domain, 1, nsType, flags);
                InetAddressAnswerAccumulator accumulator = new InetAddressAnswerAccumulator(queryNetwork, 1, callback2);
                synchronized (lock) {
                    registerFDListener(executor, queryfd, accumulator, cancellationSignal, lock);
                    if (cancellationSignal2 == null) {
                        return;
                    }
                    addCancellationSignal(cancellationSignal2, queryfd, lock);
                }
            } catch (ErrnoException e3) {
                e = e3;
                executor.execute(new -$$Lambda$DnsResolver$wc3_cnx2aezlAHvMEbQVFaTPAcE(callback2, e));
            }
        }
    }

    private void registerFDListener(Executor executor, FileDescriptor queryfd, Callback<? super byte[]> answerCallback, CancellationSignal cancellationSignal, Object lock) {
        MessageQueue mainThreadMessageQueue = Looper.getMainLooper().getQueue();
        mainThreadMessageQueue.addOnFileDescriptorEventListener(queryfd, 5, new -$$Lambda$DnsResolver$kxKi6qjPYeR_SIipxW4tYpxyM50(mainThreadMessageQueue, executor, lock, cancellationSignal, answerCallback));
    }

    static /* synthetic */ int lambda$registerFDListener$9(MessageQueue mainThreadMessageQueue, Executor executor, Object lock, CancellationSignal cancellationSignal, Callback answerCallback, FileDescriptor fd, int events) {
        mainThreadMessageQueue.removeOnFileDescriptorEventListener(fd);
        executor.execute(new -$$Lambda$DnsResolver$hIO7FFv0AXN6Nj0Dzka-LD8S870(lock, cancellationSignal, fd, answerCallback));
        return 0;
    }

    static /* synthetic */ void lambda$registerFDListener$8(Object lock, CancellationSignal cancellationSignal, FileDescriptor fd, Callback answerCallback) {
        DnsResponse resp = null;
        ErrnoException exception = null;
        synchronized (lock) {
            if (cancellationSignal != null) {
                if (cancellationSignal.isCanceled()) {
                    return;
                }
            }
            try {
                resp = NetworkUtils.resNetworkResult(fd);
            } catch (ErrnoException e) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("resNetworkResult:");
                stringBuilder.append(e.toString());
                Log.e(str, stringBuilder.toString());
                exception = e;
            }
        }
        if (exception != null) {
            answerCallback.onError(new DnsException(1, exception));
        } else {
            answerCallback.onAnswer(resp.answerbuf, resp.rcode);
        }
    }

    private void cancelQuery(FileDescriptor queryfd) {
        if (queryfd.valid()) {
            Looper.getMainLooper().getQueue().removeOnFileDescriptorEventListener(queryfd);
            NetworkUtils.resNetworkCancel(queryfd);
        }
    }

    private void addCancellationSignal(CancellationSignal cancellationSignal, FileDescriptor queryfd, Object lock) {
        cancellationSignal.setOnCancelListener(new -$$Lambda$DnsResolver$05nTktlCCI7FQsULCMbgIrjmrGc(this, lock, queryfd));
    }

    public /* synthetic */ void lambda$addCancellationSignal$10$DnsResolver(Object lock, FileDescriptor queryfd) {
        synchronized (lock) {
            cancelQuery(queryfd);
        }
    }
}
