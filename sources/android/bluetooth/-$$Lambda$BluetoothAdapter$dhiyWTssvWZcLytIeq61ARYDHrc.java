package android.bluetooth;

import android.bluetooth.BluetoothAdapter.OnMetadataChangedListener;
import android.util.Pair;
import java.util.function.Predicate;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$BluetoothAdapter$dhiyWTssvWZcLytIeq61ARYDHrc implements Predicate {
    private final /* synthetic */ OnMetadataChangedListener f$0;

    public /* synthetic */ -$$Lambda$BluetoothAdapter$dhiyWTssvWZcLytIeq61ARYDHrc(OnMetadataChangedListener onMetadataChangedListener) {
        this.f$0 = onMetadataChangedListener;
    }

    public final boolean test(Object obj) {
        return ((OnMetadataChangedListener) ((Pair) obj).first).equals(this.f$0);
    }
}
