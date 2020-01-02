package android.graphics;

public class MaskFilter {
    long native_instance;

    private static native void nativeDestructor(long j);

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        nativeDestructor(this.native_instance);
        this.native_instance = 0;
    }
}
