package android.inputmethodservice;

import android.os.ResultReceiver;
import com.android.internal.util.function.TriConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$0tnQSRQlZ73hLobz1ZfjUIoiCl0 implements TriConsumer {
    public static final /* synthetic */ -$$Lambda$0tnQSRQlZ73hLobz1ZfjUIoiCl0 INSTANCE = new -$$Lambda$0tnQSRQlZ73hLobz1ZfjUIoiCl0();

    private /* synthetic */ -$$Lambda$0tnQSRQlZ73hLobz1ZfjUIoiCl0() {
    }

    public final void accept(Object obj, Object obj2, Object obj3) {
        ((CallbackImpl) obj).hideSoftInput(((Integer) obj2).intValue(), (ResultReceiver) obj3);
    }
}
