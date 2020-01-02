package android.hardware.radio;

import android.hardware.radio.ProgramList.OnCompleteListener;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$TunerCallbackAdapter$V-mJUy8dIlOVjsZ1ckkgn490jFI implements OnCompleteListener {
    private final /* synthetic */ TunerCallbackAdapter f$0;
    private final /* synthetic */ ProgramList f$1;

    public /* synthetic */ -$$Lambda$TunerCallbackAdapter$V-mJUy8dIlOVjsZ1ckkgn490jFI(TunerCallbackAdapter tunerCallbackAdapter, ProgramList programList) {
        this.f$0 = tunerCallbackAdapter;
        this.f$1 = programList;
    }

    public final void onComplete() {
        this.f$0.lambda$setProgramListObserver$1$TunerCallbackAdapter(this.f$1);
    }
}
