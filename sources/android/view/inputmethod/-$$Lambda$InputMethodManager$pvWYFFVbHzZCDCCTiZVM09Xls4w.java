package android.view.inputmethod;

import java.util.function.ToIntFunction;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$InputMethodManager$pvWYFFVbHzZCDCCTiZVM09Xls4w implements ToIntFunction {
    public static final /* synthetic */ -$$Lambda$InputMethodManager$pvWYFFVbHzZCDCCTiZVM09Xls4w INSTANCE = new -$$Lambda$InputMethodManager$pvWYFFVbHzZCDCCTiZVM09Xls4w();

    private /* synthetic */ -$$Lambda$InputMethodManager$pvWYFFVbHzZCDCCTiZVM09Xls4w() {
    }

    public final int applyAsInt(Object obj) {
        return (((InputMethodInfo) obj).isSystem() ^ 1);
    }
}
