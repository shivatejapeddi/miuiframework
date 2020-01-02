package android.app.servertransaction;

import android.annotation.UnsupportedAppUsage;
import android.app.ClientTransactionHandler;
import android.app.IApplicationThread;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import com.android.internal.annotations.VisibleForTesting;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClientTransaction implements Parcelable, ObjectPoolItem {
    public static final Creator<ClientTransaction> CREATOR = new Creator<ClientTransaction>() {
        public ClientTransaction createFromParcel(Parcel in) {
            return new ClientTransaction(in, null);
        }

        public ClientTransaction[] newArray(int size) {
            return new ClientTransaction[size];
        }
    };
    @UnsupportedAppUsage
    private List<ClientTransactionItem> mActivityCallbacks;
    private IBinder mActivityToken;
    private IApplicationThread mClient;
    private ActivityLifecycleItem mLifecycleStateRequest;

    /* synthetic */ ClientTransaction(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public IApplicationThread getClient() {
        return this.mClient;
    }

    public void addCallback(ClientTransactionItem activityCallback) {
        if (this.mActivityCallbacks == null) {
            this.mActivityCallbacks = new ArrayList();
        }
        this.mActivityCallbacks.add(activityCallback);
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public List<ClientTransactionItem> getCallbacks() {
        return this.mActivityCallbacks;
    }

    @UnsupportedAppUsage
    public IBinder getActivityToken() {
        return this.mActivityToken;
    }

    @VisibleForTesting
    @UnsupportedAppUsage
    public ActivityLifecycleItem getLifecycleStateRequest() {
        return this.mLifecycleStateRequest;
    }

    public void setLifecycleStateRequest(ActivityLifecycleItem stateRequest) {
        this.mLifecycleStateRequest = stateRequest;
    }

    public void preExecute(ClientTransactionHandler clientTransactionHandler) {
        int size = this.mActivityCallbacks;
        if (size != 0) {
            size = size.size();
            for (int i = 0; i < size; i++) {
                ((ClientTransactionItem) this.mActivityCallbacks.get(i)).preExecute(clientTransactionHandler, this.mActivityToken);
            }
        }
        ActivityLifecycleItem activityLifecycleItem = this.mLifecycleStateRequest;
        if (activityLifecycleItem != null) {
            activityLifecycleItem.preExecute(clientTransactionHandler, this.mActivityToken);
        }
    }

    public void schedule() throws RemoteException {
        this.mClient.scheduleTransaction(this);
    }

    private ClientTransaction() {
    }

    public static ClientTransaction obtain(IApplicationThread client, IBinder activityToken) {
        ClientTransaction instance = (ClientTransaction) ObjectPool.obtain(ClientTransaction.class);
        if (instance == null) {
            instance = new ClientTransaction();
        }
        instance.mClient = client;
        instance.mActivityToken = activityToken;
        return instance;
    }

    public void recycle() {
        int size = this.mActivityCallbacks;
        if (size != 0) {
            size = size.size();
            for (int i = 0; i < size; i++) {
                ((ClientTransactionItem) this.mActivityCallbacks.get(i)).recycle();
            }
            this.mActivityCallbacks.clear();
        }
        ActivityLifecycleItem activityLifecycleItem = this.mLifecycleStateRequest;
        if (activityLifecycleItem != null) {
            activityLifecycleItem.recycle();
            this.mLifecycleStateRequest = null;
        }
        this.mClient = null;
        this.mActivityToken = null;
        ObjectPool.recycle(this);
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStrongBinder(this.mClient.asBinder());
        boolean writeActivityCallbacks = true;
        boolean writeActivityToken = this.mActivityToken != null;
        dest.writeBoolean(writeActivityToken);
        if (writeActivityToken) {
            dest.writeStrongBinder(this.mActivityToken);
        }
        dest.writeParcelable(this.mLifecycleStateRequest, flags);
        if (this.mActivityCallbacks == null) {
            writeActivityCallbacks = false;
        }
        dest.writeBoolean(writeActivityCallbacks);
        if (writeActivityCallbacks) {
            dest.writeParcelableList(this.mActivityCallbacks, flags);
        }
    }

    private ClientTransaction(Parcel in) {
        this.mClient = (IApplicationThread) in.readStrongBinder();
        if (in.readBoolean()) {
            this.mActivityToken = in.readStrongBinder();
        }
        this.mLifecycleStateRequest = (ActivityLifecycleItem) in.readParcelable(getClass().getClassLoader());
        if (in.readBoolean()) {
            this.mActivityCallbacks = new ArrayList();
            in.readParcelableList(this.mActivityCallbacks, getClass().getClassLoader());
        }
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientTransaction other = (ClientTransaction) o;
        if (!(Objects.equals(this.mActivityCallbacks, other.mActivityCallbacks) && Objects.equals(this.mLifecycleStateRequest, other.mLifecycleStateRequest) && this.mClient == other.mClient && this.mActivityToken == other.mActivityToken)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((17 * 31) + Objects.hashCode(this.mActivityCallbacks)) * 31) + Objects.hashCode(this.mLifecycleStateRequest);
    }

    public void dump(String prefix, PrintWriter pw) {
        pw.append(prefix).println("ClientTransaction{");
        pw.append(prefix).print("  callbacks=[");
        List list = this.mActivityCallbacks;
        int size = list != null ? list.size() : 0;
        if (size > 0) {
            pw.println();
            for (int i = 0; i < size; i++) {
                pw.append(prefix).append("    ").println(((ClientTransactionItem) this.mActivityCallbacks.get(i)).toString());
            }
            pw.append(prefix).println("  ]");
        } else {
            pw.println("]");
        }
        PrintWriter append = pw.append(prefix).append("  stateRequest=");
        ActivityLifecycleItem activityLifecycleItem = this.mLifecycleStateRequest;
        append.println(activityLifecycleItem != null ? activityLifecycleItem.toString() : null);
        pw.append(prefix).println("}");
    }

    public String toShortString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ClientTransaction{");
        List<ClientTransactionItem> callbacks = getCallbacks();
        if (callbacks != null && callbacks.size() > 0) {
            sb.append(" callbacks=[");
            for (ClientTransactionItem item : callbacks) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(item.getClass().getName());
                stringBuilder.append(",");
                sb.append(stringBuilder.toString());
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]");
        }
        ActivityLifecycleItem lifecycleItem = getLifecycleStateRequest();
        if (lifecycleItem != null) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(" lifecycleRequest=");
            stringBuilder2.append(lifecycleItem.getClass().getName());
            sb.append(stringBuilder2.toString());
        }
        sb.append(" }");
        return sb.toString();
    }
}
