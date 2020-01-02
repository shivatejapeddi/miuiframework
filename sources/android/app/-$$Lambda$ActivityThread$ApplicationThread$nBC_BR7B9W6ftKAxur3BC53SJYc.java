package android.app;

import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.IBinder;
import android.os.RemoteCallback;
import com.android.internal.util.function.HexConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ActivityThread$ApplicationThread$nBC_BR7B9W6ftKAxur3BC53SJYc implements HexConsumer {
    public static final /* synthetic */ -$$Lambda$ActivityThread$ApplicationThread$nBC_BR7B9W6ftKAxur3BC53SJYc INSTANCE = new -$$Lambda$ActivityThread$ApplicationThread$nBC_BR7B9W6ftKAxur3BC53SJYc();

    private /* synthetic */ -$$Lambda$ActivityThread$ApplicationThread$nBC_BR7B9W6ftKAxur3BC53SJYc() {
    }

    public final void accept(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
        ((ActivityThread) ((ActivityThread) obj)).handlePerformDirectAction((IBinder) obj2, (String) obj3, (Bundle) obj4, (CancellationSignal) obj5, (RemoteCallback) obj6);
    }
}
