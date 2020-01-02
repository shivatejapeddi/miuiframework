package android.net;

import android.net.ConnectivityManager.OnTetheringEventCallback;
import com.android.internal.util.FunctionalUtils.ThrowingRunnable;
import java.util.concurrent.Executor;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ConnectivityManager$3$BfAvTRJTF0an2PdeqkENEBULYBU implements ThrowingRunnable {
    private final /* synthetic */ Executor f$0;
    private final /* synthetic */ OnTetheringEventCallback f$1;
    private final /* synthetic */ Network f$2;

    public /* synthetic */ -$$Lambda$ConnectivityManager$3$BfAvTRJTF0an2PdeqkENEBULYBU(Executor executor, OnTetheringEventCallback onTetheringEventCallback, Network network) {
        this.f$0 = executor;
        this.f$1 = onTetheringEventCallback;
        this.f$2 = network;
    }

    public final void runOrThrow() {
        this.f$0.execute(new -$$Lambda$ConnectivityManager$3$Hh_etCA-vVs2IV58umWLOd1O4yk(this.f$1, this.f$2));
    }
}
