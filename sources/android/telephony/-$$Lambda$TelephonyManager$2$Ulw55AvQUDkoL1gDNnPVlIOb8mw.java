package android.telephony;

import android.os.ParcelableException;
import android.telephony.TelephonyManager.CellInfoCallback;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$TelephonyManager$2$Ulw55AvQUDkoL1gDNnPVlIOb8mw implements Runnable {
    private final /* synthetic */ CellInfoCallback f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ ParcelableException f$2;

    public /* synthetic */ -$$Lambda$TelephonyManager$2$Ulw55AvQUDkoL1gDNnPVlIOb8mw(CellInfoCallback cellInfoCallback, int i, ParcelableException parcelableException) {
        this.f$0 = cellInfoCallback;
        this.f$1 = i;
        this.f$2 = parcelableException;
    }

    public final void run() {
        this.f$0.onError(this.f$1, this.f$2.getCause());
    }
}
