package android.view;

import android.graphics.Picture;
import java.util.concurrent.Callable;
import java.util.function.Function;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ViewDebug$hyDSYptlxuUTTyRIONqWzWWVDB0 implements Function {
    private final /* synthetic */ Callable f$0;

    public /* synthetic */ -$$Lambda$ViewDebug$hyDSYptlxuUTTyRIONqWzWWVDB0(Callable callable) {
        this.f$0 = callable;
    }

    public final Object apply(Object obj) {
        return ViewDebug.lambda$startRenderingCommandsCapture$4(this.f$0, (Picture) obj);
    }
}
