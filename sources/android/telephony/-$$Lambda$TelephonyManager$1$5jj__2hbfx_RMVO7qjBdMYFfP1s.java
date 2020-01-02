package android.telephony;

import android.os.ParcelableException;
import android.telephony.TelephonyManager.CellInfoCallback;
import com.android.internal.util.FunctionalUtils.ThrowingRunnable;
import java.util.concurrent.Executor;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$TelephonyManager$1$5jj__2hbfx_RMVO7qjBdMYFfP1s implements ThrowingRunnable {
    private final /* synthetic */ Executor f$0;
    private final /* synthetic */ CellInfoCallback f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ ParcelableException f$3;

    public /* synthetic */ -$$Lambda$TelephonyManager$1$5jj__2hbfx_RMVO7qjBdMYFfP1s(Executor executor, CellInfoCallback cellInfoCallback, int i, ParcelableException parcelableException) {
        this.f$0 = executor;
        this.f$1 = cellInfoCallback;
        this.f$2 = i;
        this.f$3 = parcelableException;
    }

    public final void runOrThrow() {
        this.f$0.execute(new -$$Lambda$TelephonyManager$1$DUDjwoHWG36BPTvbfvZqnIO3Y88(this.f$1, this.f$2, this.f$3));
    }
}
