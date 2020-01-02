package android.graphics;

import libcore.util.NativeAllocationRegistry;

public class ColorFilter {
    private Runnable mCleaner;
    private long mNativeInstance;

    private static class NoImagePreloadHolder {
        public static final NativeAllocationRegistry sRegistry = NativeAllocationRegistry.createMalloced(ColorFilter.class.getClassLoader(), ColorFilter.nativeGetFinalizer());

        private NoImagePreloadHolder() {
        }
    }

    private static native long nativeGetFinalizer();

    /* Access modifiers changed, original: 0000 */
    public long createNativeInstance() {
        return 0;
    }

    /* Access modifiers changed, original: 0000 */
    public void discardNativeInstance() {
        if (this.mNativeInstance != 0) {
            this.mCleaner.run();
            this.mCleaner = null;
            this.mNativeInstance = 0;
        }
    }

    public long getNativeInstance() {
        if (this.mNativeInstance == 0) {
            this.mNativeInstance = createNativeInstance();
            if (this.mNativeInstance != 0) {
                this.mCleaner = NoImagePreloadHolder.sRegistry.registerNativeAllocation(this, this.mNativeInstance);
            }
        }
        return this.mNativeInstance;
    }
}
