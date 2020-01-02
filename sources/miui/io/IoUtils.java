package miui.io;

@Deprecated
public class IoUtils {
    public static void closeQuietly(AutoCloseable closeable) {
        libcore.io.IoUtils.closeQuietly(closeable);
    }
}
