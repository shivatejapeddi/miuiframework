package miui.maml.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.HashSet;

public class FilenameExtFilter implements FilenameFilter {
    private HashSet<String> mExts = new HashSet();

    public FilenameExtFilter(String[] exts) {
        if (exts != null) {
            this.mExts.addAll(Arrays.asList(exts));
        }
    }

    public boolean contains(String ext) {
        return this.mExts.contains(ext.toLowerCase());
    }

    public boolean accept(File dir, String filename) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(dir);
        stringBuilder.append(File.separator);
        stringBuilder.append(filename);
        if (new File(stringBuilder.toString()).isDirectory()) {
            return true;
        }
        int dotPosition = filename.lastIndexOf(46);
        if (dotPosition != -1) {
            return contains(((String) filename.subSequence(dotPosition + 1, filename.length())).toLowerCase());
        }
        return false;
    }
}
