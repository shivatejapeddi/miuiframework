package android.os;

import com.android.internal.os.BinderInternal.WorkSourceProvider;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$Binder$IYUHVkWouPK_9CG2s8VwyWBt5_I implements WorkSourceProvider {
    public static final /* synthetic */ -$$Lambda$Binder$IYUHVkWouPK_9CG2s8VwyWBt5_I INSTANCE = new -$$Lambda$Binder$IYUHVkWouPK_9CG2s8VwyWBt5_I();

    private /* synthetic */ -$$Lambda$Binder$IYUHVkWouPK_9CG2s8VwyWBt5_I() {
    }

    public final int resolveWorkSourceUid(int i) {
        return Binder.getCallingUid();
    }
}
