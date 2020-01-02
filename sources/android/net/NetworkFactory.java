package android.net;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.AsyncChannel;
import com.android.internal.util.IndentingPrintWriter;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class NetworkFactory extends Handler {
    private static final int BASE = 536576;
    public static final int CMD_CANCEL_REQUEST = 536577;
    public static final int CMD_REQUEST_NETWORK = 536576;
    private static final int CMD_SET_FILTER = 536579;
    private static final int CMD_SET_SCORE = 536578;
    private static final boolean DBG = true;
    public static final int EVENT_UNFULFILLABLE_REQUEST = 536580;
    private static final boolean VDBG = false;
    private final String LOG_TAG;
    private AsyncChannel mAsyncChannel;
    private NetworkCapabilities mCapabilityFilter;
    private final Context mContext;
    private Messenger mMessenger = null;
    private final SparseArray<NetworkRequestInfo> mNetworkRequests = new SparseArray();
    private final ArrayList<Message> mPreConnectedQueue = new ArrayList();
    private int mRefCount = 0;
    private int mScore;
    private int mSerialNumber;

    private class NetworkRequestInfo {
        public int factorySerialNumber;
        public final NetworkRequest request;
        public boolean requested = false;
        public int score;

        NetworkRequestInfo(NetworkRequest request, int score, int factorySerialNumber) {
            this.request = request;
            this.score = score;
            this.factorySerialNumber = factorySerialNumber;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{");
            stringBuilder.append(this.request);
            stringBuilder.append(", score=");
            stringBuilder.append(this.score);
            stringBuilder.append(", requested=");
            stringBuilder.append(this.requested);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static class SerialNumber {
        public static final int NONE = -1;
        public static final int VPN = -2;
        private static final AtomicInteger sNetworkFactorySerialNumber = new AtomicInteger(1);

        public static final int nextSerialNumber() {
            return sNetworkFactorySerialNumber.getAndIncrement();
        }
    }

    @UnsupportedAppUsage
    public NetworkFactory(Looper looper, Context context, String logTag, NetworkCapabilities filter) {
        super(looper);
        this.LOG_TAG = logTag;
        this.mContext = context;
        this.mCapabilityFilter = filter;
    }

    public void register() {
        log("Registering NetworkFactory");
        if (this.mMessenger == null) {
            this.mMessenger = new Messenger((Handler) this);
            this.mSerialNumber = ConnectivityManager.from(this.mContext).registerNetworkFactory(this.mMessenger, this.LOG_TAG);
        }
    }

    public void unregister() {
        log("Unregistering NetworkFactory");
        if (this.mMessenger != null) {
            ConnectivityManager.from(this.mContext).unregisterNetworkFactory(this.mMessenger);
            this.mMessenger = null;
        }
    }

    public void handleMessage(Message msg) {
        int i = msg.what;
        AsyncChannel asyncChannel;
        if (i != AsyncChannel.CMD_CHANNEL_FULL_CONNECTION) {
            switch (i) {
                case AsyncChannel.CMD_CHANNEL_DISCONNECT /*69635*/:
                    asyncChannel = this.mAsyncChannel;
                    if (asyncChannel != null) {
                        asyncChannel.disconnect();
                        this.mAsyncChannel = null;
                        return;
                    }
                    return;
                case AsyncChannel.CMD_CHANNEL_DISCONNECTED /*69636*/:
                    log("NetworkFactory channel lost");
                    this.mAsyncChannel = null;
                    return;
                default:
                    switch (i) {
                        case 536576:
                            handleAddRequest((NetworkRequest) msg.obj, msg.arg1, msg.arg2);
                            return;
                        case CMD_CANCEL_REQUEST /*536577*/:
                            handleRemoveRequest((NetworkRequest) msg.obj);
                            return;
                        case CMD_SET_SCORE /*536578*/:
                            handleSetScore(msg.arg1);
                            return;
                        case CMD_SET_FILTER /*536579*/:
                            handleSetFilter((NetworkCapabilities) msg.obj);
                            return;
                        default:
                            return;
                    }
            }
        } else if (this.mAsyncChannel != null) {
            log("Received new connection while already connected!");
        } else {
            asyncChannel = new AsyncChannel();
            asyncChannel.connected(null, this, msg.replyTo);
            asyncChannel.replyToMessage(msg, (int) AsyncChannel.CMD_CHANNEL_FULLY_CONNECTED, 0);
            this.mAsyncChannel = asyncChannel;
            Iterator it = this.mPreConnectedQueue.iterator();
            while (it.hasNext()) {
                asyncChannel.sendMessage((Message) it.next());
            }
            this.mPreConnectedQueue.clear();
        }
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public void handleAddRequest(NetworkRequest request, int score) {
        handleAddRequest(request, score, -1);
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public void handleAddRequest(NetworkRequest request, int score, int servingFactorySerialNumber) {
        NetworkRequestInfo n = (NetworkRequestInfo) this.mNetworkRequests.get(request.requestId);
        if (n == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("got request ");
            stringBuilder.append(request);
            stringBuilder.append(" with score ");
            stringBuilder.append(score);
            stringBuilder.append(" and serial ");
            stringBuilder.append(servingFactorySerialNumber);
            log(stringBuilder.toString());
            n = new NetworkRequestInfo(request, score, servingFactorySerialNumber);
            this.mNetworkRequests.put(n.request.requestId, n);
        } else {
            n.score = score;
            n.factorySerialNumber = servingFactorySerialNumber;
        }
        evalRequest(n);
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public void handleRemoveRequest(NetworkRequest request) {
        NetworkRequestInfo n = (NetworkRequestInfo) this.mNetworkRequests.get(request.requestId);
        if (n != null) {
            this.mNetworkRequests.remove(request.requestId);
            if (n.requested) {
                releaseNetworkFor(n.request);
            }
        }
    }

    private void handleSetScore(int score) {
        this.mScore = score;
        evalRequests();
    }

    private void handleSetFilter(NetworkCapabilities netCap) {
        this.mCapabilityFilter = netCap;
        evalRequests();
    }

    public boolean acceptRequest(NetworkRequest request, int score) {
        return true;
    }

    private void evalRequest(NetworkRequestInfo n) {
        if (shouldNeedNetworkFor(n)) {
            needNetworkFor(n.request, n.score);
            n.requested = true;
        } else if (shouldReleaseNetworkFor(n)) {
            releaseNetworkFor(n.request);
            n.requested = false;
        }
    }

    private boolean shouldNeedNetworkFor(NetworkRequestInfo n) {
        return !n.requested && ((n.score < this.mScore || n.factorySerialNumber == this.mSerialNumber) && n.request.networkCapabilities.satisfiedByNetworkCapabilities(this.mCapabilityFilter) && acceptRequest(n.request, n.score));
    }

    private boolean shouldReleaseNetworkFor(NetworkRequestInfo n) {
        return n.requested && !((n.score <= this.mScore || n.factorySerialNumber == this.mSerialNumber) && n.request.networkCapabilities.satisfiedByNetworkCapabilities(this.mCapabilityFilter) && acceptRequest(n.request, n.score));
    }

    private void evalRequests() {
        for (int i = 0; i < this.mNetworkRequests.size(); i++) {
            evalRequest((NetworkRequestInfo) this.mNetworkRequests.valueAt(i));
        }
    }

    /* Access modifiers changed, original: protected */
    public void reevaluateAllRequests() {
        post(new -$$Lambda$NetworkFactory$HfslgqyaKc_n0wXX5_qRYVZoGfI(this));
    }

    public /* synthetic */ void lambda$reevaluateAllRequests$0$NetworkFactory() {
        evalRequests();
    }

    /* Access modifiers changed, original: protected */
    public void releaseRequestAsUnfulfillableByAnyFactory(NetworkRequest r) {
        post(new -$$Lambda$NetworkFactory$quULWy1SjqmEQiqq5nzlBuGzTss(this, r));
    }

    public /* synthetic */ void lambda$releaseRequestAsUnfulfillableByAnyFactory$1$NetworkFactory(NetworkRequest r) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("releaseRequestAsUnfulfillableByAnyFactory: ");
        stringBuilder.append(r);
        log(stringBuilder.toString());
        Message msg = obtainMessage(EVENT_UNFULFILLABLE_REQUEST, r);
        AsyncChannel asyncChannel = this.mAsyncChannel;
        if (asyncChannel != null) {
            asyncChannel.sendMessage(msg);
        } else {
            this.mPreConnectedQueue.add(msg);
        }
    }

    /* Access modifiers changed, original: protected */
    public void startNetwork() {
    }

    /* Access modifiers changed, original: protected */
    public void stopNetwork() {
    }

    /* Access modifiers changed, original: protected */
    public void needNetworkFor(NetworkRequest networkRequest, int score) {
        int i = this.mRefCount + 1;
        this.mRefCount = i;
        if (i == 1) {
            startNetwork();
        }
    }

    /* Access modifiers changed, original: protected */
    public void releaseNetworkFor(NetworkRequest networkRequest) {
        int i = this.mRefCount - 1;
        this.mRefCount = i;
        if (i == 0) {
            stopNetwork();
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public void setScoreFilter(int score) {
        sendMessage(obtainMessage(CMD_SET_SCORE, score, 0));
    }

    public void setCapabilityFilter(NetworkCapabilities netCap) {
        sendMessage(obtainMessage(CMD_SET_FILTER, new NetworkCapabilities(netCap)));
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public int getRequestCount() {
        return this.mNetworkRequests.size();
    }

    public int getSerialNumber() {
        return this.mSerialNumber;
    }

    /* Access modifiers changed, original: protected */
    public void log(String s) {
        Log.d(this.LOG_TAG, s);
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        IndentingPrintWriter pw = new IndentingPrintWriter(writer, "  ");
        pw.println(toString());
        pw.increaseIndent();
        for (int i = 0; i < this.mNetworkRequests.size(); i++) {
            pw.println(this.mNetworkRequests.valueAt(i));
        }
        pw.decreaseIndent();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        sb.append(this.LOG_TAG);
        sb.append(" - mSerialNumber=");
        sb.append(this.mSerialNumber);
        sb.append(", ScoreFilter=");
        sb.append(this.mScore);
        sb.append(", Filter=");
        sb.append(this.mCapabilityFilter);
        sb.append(", requests=");
        sb.append(this.mNetworkRequests.size());
        sb.append(", refCount=");
        sb.append(this.mRefCount);
        return sb.append("}").toString();
    }
}
