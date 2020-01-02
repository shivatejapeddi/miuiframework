package android.app.servertransaction;

import android.app.ActivityTaskManager;
import android.app.ClientTransactionHandler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.Trace;

public class TopResumedActivityChangeItem extends ClientTransactionItem {
    public static final Creator<TopResumedActivityChangeItem> CREATOR = new Creator<TopResumedActivityChangeItem>() {
        public TopResumedActivityChangeItem createFromParcel(Parcel in) {
            return new TopResumedActivityChangeItem(in, null);
        }

        public TopResumedActivityChangeItem[] newArray(int size) {
            return new TopResumedActivityChangeItem[size];
        }
    };
    private boolean mOnTop;

    /* synthetic */ TopResumedActivityChangeItem(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public void execute(ClientTransactionHandler client, IBinder token, PendingTransactionActions pendingActions) {
        String str = "topResumedActivityChangeItem";
        Trace.traceBegin(64, str);
        client.handleTopResumedActivityChanged(token, this.mOnTop, str);
        Trace.traceEnd(64);
    }

    public void postExecute(ClientTransactionHandler client, IBinder token, PendingTransactionActions pendingActions) {
        if (!this.mOnTop) {
            try {
                ActivityTaskManager.getService().activityTopResumedStateLost();
            } catch (RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        }
    }

    private TopResumedActivityChangeItem() {
    }

    public static TopResumedActivityChangeItem obtain(boolean onTop) {
        TopResumedActivityChangeItem instance = (TopResumedActivityChangeItem) ObjectPool.obtain(TopResumedActivityChangeItem.class);
        if (instance == null) {
            instance = new TopResumedActivityChangeItem();
        }
        instance.mOnTop = onTop;
        return instance;
    }

    public void recycle() {
        this.mOnTop = false;
        ObjectPool.recycle(this);
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(this.mOnTop);
    }

    private TopResumedActivityChangeItem(Parcel in) {
        this.mOnTop = in.readBoolean();
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (this.mOnTop != ((TopResumedActivityChangeItem) o).mOnTop) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (17 * 31) + this.mOnTop;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("TopResumedActivityChangeItem{onTop=");
        stringBuilder.append(this.mOnTop);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
