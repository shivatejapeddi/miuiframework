package android.media;

class MediaFileInjector {
    public static final int FILE_TYPE_3G2B = 2006;
    public static final int FILE_TYPE_F4V = 2005;
    public static final int FILE_TYPE_FLV = 2000;
    public static final int FILE_TYPE_MOV = 2003;
    public static final int FILE_TYPE_RM = 2001;
    public static final int FILE_TYPE_RMVB = 2002;
    public static final int FILE_TYPE_VOB = 2004;
    private static final int FIRST_MIUI_VIDEO_FILE_TYPE = 2000;
    private static final int LAST_MIUI_VIDEO_FILE_TYPE = 2006;
    public static final int MIUI_FILE_TYPE_BASE = 2000;

    MediaFileInjector() {
    }

    static void addMiuiFileType() {
        MediaFile.addFileType("FLV", 2000, "video/x-flv");
        String str = "video/x-pn-realvideo";
        MediaFile.addFileType("RM", 2001, str);
        MediaFile.addFileType("RMVB", 2002, str);
        MediaFile.addFileType("MOV", 2003, "video/quicktime");
        MediaFile.addFileType("VOB", 2004, "video/x-ms-vob");
        MediaFile.addFileType("F4V", 2005, "video/mp4");
        MediaFile.addFileType("3G2B", 2006, MediaFormat.MIMETYPE_VIDEO_H263);
    }

    static boolean isMIUIVideoFileType(int fileType) {
        return fileType >= 2000 && fileType <= 2006;
    }
}
