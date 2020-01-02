package com.miui.mishare.app.util;

import android.miui.R;
import android.text.TextUtils;
import java.util.HashMap;

public class MiShareFileIconUtil {
    private static HashMap<String, Integer> sFileExtToIcons = new HashMap();

    static {
        addItem(new String[]{"txt", "text", "html"}, R.drawable.ic_txt);
        addItem(new String[]{"pdf"}, R.drawable.ic_pdf);
        addItem(new String[]{"doc", "docx", "rtf"}, R.drawable.ic_word);
        addItem(new String[]{"xlsx", "xls", "csv"}, R.drawable.ic_excel);
        addItem(new String[]{"ppt", "pptx"}, R.drawable.ic_ppt);
        addItem(new String[]{"wps"}, R.drawable.ic_wps);
        addItem(new String[]{"rar", "zip", "7z", "tar", "gz"}, R.drawable.ic_zip);
        addItem(new String[]{"mp3", "wma", "aac", "flac", "ape", "m4a", "wav", "amr"}, R.drawable.ic_audio);
        addItem(new String[]{"avi", "wmv", "mov", "mkv", "ts", "mp4", "rmvb", "webm", "flv"}, R.drawable.ic_video);
        addItem(new String[]{"jpg", "jpeg", "png", "bmp", "tif", "raw", "gif", "webp", "wbmp"}, R.drawable.ic_image);
        addItem(new String[]{"apk"}, R.drawable.ic_apk);
        addItem(new String[]{"exe"}, R.drawable.ic_exe);
        addItem(new String[]{"ps"}, R.drawable.ic_ps);
    }

    private static void addItem(String[] exts, int resId) {
        if (exts != null) {
            for (String ext : exts) {
                if (!TextUtils.isEmpty(ext)) {
                    sFileExtToIcons.put(ext.toLowerCase(), Integer.valueOf(resId));
                }
            }
        }
    }

    public static int getFileIconResId(String ext) {
        boolean isEmpty = TextUtils.isEmpty(ext);
        int i = R.drawable.ic_file_default;
        if (isEmpty) {
            return R.drawable.ic_file_default;
        }
        Integer i2 = (Integer) sFileExtToIcons.get(ext.toLowerCase());
        if (i2 != null) {
            i = i2.intValue();
        }
        return i;
    }
}
