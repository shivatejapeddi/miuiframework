package miui.theme;

import android.util.Log;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import miui.content.res.ThemeNativeUtils;
import miui.util.IOUtils;

public class ThemeFileUtils {
    private static final String TAG = "ThemeFileUtils";

    public static boolean copy(String src, String dest) {
        Path path = null;
        try {
            path = Files.copy(Paths.get(src, new String[0]), Paths.get(dest, new String[0]), new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
        } catch (IOException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to copy( ");
            stringBuilder.append(src);
            stringBuilder.append(" to + ");
            stringBuilder.append(dest);
            stringBuilder.append(" ): ");
            stringBuilder.append(e);
            Log.e(TAG, stringBuilder.toString());
        }
        if (path != null) {
            return true;
        }
        return false;
    }

    public static boolean remove(String path) {
        try {
            deleteContentsAndDir(path);
            return true;
        } catch (IOException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to remove( ");
            stringBuilder.append(path);
            stringBuilder.append(" ): ");
            stringBuilder.append(e);
            Log.e(TAG, stringBuilder.toString());
            return false;
        }
    }

    private static void deleteContentsAndDir(String path) throws IOException {
        deleteContents(path);
        Files.deleteIfExists(Paths.get(path, new String[0]));
    }

    public static boolean mkdirs(String dirPath) {
        Path dir = Paths.get(dirPath, new String[0]);
        if (Files.exists(dir, new LinkOption[0])) {
            return false;
        }
        try {
            Files.createDirectories(dir, new FileAttribute[0]);
            ThemeNativeUtils.updateFilePermissionWithThemeContext(dirPath);
            return true;
        } catch (IOException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to mkdirs( ");
            stringBuilder.append(dirPath);
            stringBuilder.append(" ): ");
            stringBuilder.append(e);
            Log.e(TAG, stringBuilder.toString());
            return false;
        }
    }

    public static void write(String fileName, String data) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = Files.newBufferedWriter(Paths.get(fileName, new String[0]), StandardCharsets.UTF_8, new OpenOption[0]);
            writer.write(data);
            writer.flush();
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    public static void link(String oldPath, String newPath) throws IOException {
        Files.createSymbolicLink(Paths.get(newPath, new String[0]), Paths.get(oldPath, new String[0]), new FileAttribute[0]);
    }

    public static void deleteContents(String dirPath) {
        final Path dir = Paths.get(dirPath, new String[0]);
        try {
            if (Files.isDirectory(dir, new LinkOption[0])) {
                Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    public FileVisitResult postVisitDirectory(Path path, IOException exc) throws IOException {
                        if (!Files.isSameFile(dir, path)) {
                            Files.delete(path);
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
        } catch (IOException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to deleteContentsExceptDir error ( ");
            stringBuilder.append(dirPath);
            stringBuilder.append(" ) : ");
            stringBuilder.append(e);
            Log.e(TAG, stringBuilder.toString());
        }
    }
}
