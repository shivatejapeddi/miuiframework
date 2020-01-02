package android.app.servertransaction;

import android.annotation.UnsupportedAppUsage;
import android.app.ClientTransactionHandler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.Trace;
import com.android.internal.content.ReferrerIntent;
import java.util.List;
import java.util.Objects;

public class NewIntentItem extends ClientTransactionItem {
    public static final Creator<NewIntentItem> CREATOR = new Creator<NewIntentItem>() {
        public NewIntentItem createFromParcel(Parcel in) {
            return new NewIntentItem(in, null);
        }

        public NewIntentItem[] newArray(int size) {
            return new NewIntentItem[size];
        }
    };
    @UnsupportedAppUsage
    private List<ReferrerIntent> mIntents;
    private boolean mResume;

    /* synthetic */ NewIntentItem(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public int getPostExecutionState() {
        return this.mResume ? 3 : -1;
    }

    public void execute(ClientTransactionHandler client, IBinder token, PendingTransactionActions pendingActions) {
        Trace.traceBegin(64, "activityNewIntent");
        client.handleNewIntent(token, this.mIntents);
        Trace.traceEnd(64);
    }

    private NewIntentItem() {
    }

    public static NewIntentItem obtain(List<ReferrerIntent> intents, boolean resume) {
        NewIntentItem instance = (NewIntentItem) ObjectPool.obtain(NewIntentItem.class);
        if (instance == null) {
            instance = new NewIntentItem();
        }
        instance.mIntents = intents;
        instance.mResume = resume;
        return instance;
    }

    public void recycle() {
        this.mIntents = null;
        this.mResume = false;
        ObjectPool.recycle(this);
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(this.mResume);
        dest.writeTypedList(this.mIntents, flags);
    }

    private NewIntentItem(Parcel in) {
        this.mResume = in.readBoolean();
        this.mIntents = in.createTypedArrayList(ReferrerIntent.CREATOR);
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NewIntentItem other = (NewIntentItem) o;
        if (!(this.mResume == other.mResume && Objects.equals(this.mIntents, other.mIntents))) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((17 * 31) + this.mResume) * 31) + this.mIntents.hashCode();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("NewIntentItem{intents=");
        stringBuilder.append(this.mIntents);
        stringBuilder.append(",resume=");
        stringBuilder.append(this.mResume);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
