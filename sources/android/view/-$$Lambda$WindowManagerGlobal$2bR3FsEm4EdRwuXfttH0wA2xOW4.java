package android.view;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$WindowManagerGlobal$2bR3FsEm4EdRwuXfttH0wA2xOW4 implements Runnable {
    private final /* synthetic */ ViewRootImpl f$0;
    private final /* synthetic */ boolean f$1;

    public /* synthetic */ -$$Lambda$WindowManagerGlobal$2bR3FsEm4EdRwuXfttH0wA2xOW4(ViewRootImpl viewRootImpl, boolean z) {
        this.f$0 = viewRootImpl;
        this.f$1 = z;
    }

    public final void run() {
        this.f$0.setWindowStopped(this.f$1);
    }
}
