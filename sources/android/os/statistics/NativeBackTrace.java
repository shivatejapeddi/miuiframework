package android.os.statistics;

class NativeBackTrace {
    private static boolean nativeBacktraceMapUpdated;
    private long mNativePtr;

    private static native void nativeDispose(long j);

    private static native String[] nativeResolve(long j);

    private static native int nativeUpdateBacktraceMap();

    public NativeBackTrace(long nativePtr) {
        this.mNativePtr = nativePtr;
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        dispose();
        super.finalize();
    }

    public void dispose() {
        long j = this.mNativePtr;
        if (j != 0) {
            nativeDispose(j);
            this.mNativePtr = 0;
        }
    }

    public String[] resolve() {
        long j = this.mNativePtr;
        if (j == 0) {
            return StackUtils.emptyStack;
        }
        return nativeResolve(j);
    }

    public static String[] resolve(NativeBackTrace nativeBackTrace) {
        if (nativeBackTrace == null) {
            return null;
        }
        if (!nativeBacktraceMapUpdated) {
            updateBacktraceMap();
            nativeBacktraceMapUpdated = true;
        }
        return nativeBackTrace.resolve();
    }

    public static int updateBacktraceMap() {
        return nativeUpdateBacktraceMap();
    }

    public static void reset() {
        nativeBacktraceMapUpdated = false;
    }
}
