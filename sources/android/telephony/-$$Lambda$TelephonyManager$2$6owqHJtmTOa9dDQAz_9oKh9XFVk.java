package android.telephony;

import android.os.ParcelableException;
import android.telephony.TelephonyManager.CellInfoCallback;
import com.android.internal.util.FunctionalUtils.ThrowingRunnable;
import java.util.concurrent.Executor;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$TelephonyManager$2$6owqHJtmTOa9dDQAz_9oKh9XFVk implements ThrowingRunnable {
    private final /* synthetic */ Executor f$0;
    private final /* synthetic */ CellInfoCallback f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ ParcelableException f$3;

    public /* synthetic */ -$$Lambda$TelephonyManager$2$6owqHJtmTOa9dDQAz_9oKh9XFVk(Executor executor, CellInfoCallback cellInfoCallback, int i, ParcelableException parcelableException) {
        this.f$0 = executor;
        this.f$1 = cellInfoCallback;
        this.f$2 = i;
        this.f$3 = parcelableException;
    }

    public final void runOrThrow() {
        this.f$0.execute(new -$$Lambda$TelephonyManager$2$Ulw55AvQUDkoL1gDNnPVlIOb8mw(this.f$1, this.f$2, this.f$3));
    }
}
