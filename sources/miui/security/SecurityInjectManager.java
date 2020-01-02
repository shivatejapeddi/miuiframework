package miui.security;

public class SecurityInjectManager {
    public static native void blockSelfNetwork(boolean z);

    public static native boolean hookFunctions(long j, int i);

    public static native boolean isNetworkBlocked();

    private SecurityInjectManager() {
    }
}
