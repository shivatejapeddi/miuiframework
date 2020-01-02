package android.os;

import java.util.Comparator;
import java.util.Map.Entry;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$BinderProxy$ProxyMap$aKNUVKkR8bNu2XRFxaO2PW1AFBA implements Comparator {
    public static final /* synthetic */ -$$Lambda$BinderProxy$ProxyMap$aKNUVKkR8bNu2XRFxaO2PW1AFBA INSTANCE = new -$$Lambda$BinderProxy$ProxyMap$aKNUVKkR8bNu2XRFxaO2PW1AFBA();

    private /* synthetic */ -$$Lambda$BinderProxy$ProxyMap$aKNUVKkR8bNu2XRFxaO2PW1AFBA() {
    }

    public final int compare(Object obj, Object obj2) {
        return ((Integer) ((Entry) obj2).getValue()).compareTo((Integer) ((Entry) obj).getValue());
    }
}
