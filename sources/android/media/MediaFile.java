package android.media;

import android.annotation.UnsupportedAppUsage;
import android.content.ClipDescription;
import android.content.ContentResolver;
import android.mtp.MtpConstants;
import com.android.internal.widget.MessagingMessage;
import java.util.HashMap;
import libcore.net.MimeUtils;

public class MediaFile {
    @Deprecated
    @UnsupportedAppUsage
    private static final int FIRST_AUDIO_FILE_TYPE = 1;
    @Deprecated
    @UnsupportedAppUsage
    private static final int LAST_AUDIO_FILE_TYPE = 10;
    @Deprecated
    @UnsupportedAppUsage
    private static final HashMap<String, MediaFileType> sFileTypeMap = new HashMap();
    @Deprecated
    @UnsupportedAppUsage
    private static final HashMap<String, Integer> sFileTypeToFormatMap = new HashMap();
    @UnsupportedAppUsage
    private static final HashMap<Integer, String> sFormatToMimeTypeMap = new HashMap();
    @UnsupportedAppUsage
    private static final HashMap<String, Integer> sMimeTypeToFormatMap = new HashMap();

    @Deprecated
    public static class MediaFileType {
        @UnsupportedAppUsage
        public final int fileType;
        @UnsupportedAppUsage
        public final String mimeType;

        MediaFileType(int fileType, String mimeType) {
            this.fileType = fileType;
            this.mimeType = mimeType;
        }
    }

    static {
        String str = MediaFormat.MIMETYPE_AUDIO_MPEG;
        addFileType(12297, str);
        addFileType(12296, "audio/x-wav");
        addFileType(MtpConstants.FORMAT_WMA, "audio/x-ms-wma");
        addFileType(MtpConstants.FORMAT_OGG, "audio/ogg");
        addFileType(MtpConstants.FORMAT_AAC, "audio/aac");
        addFileType(MtpConstants.FORMAT_FLAC, MediaFormat.MIMETYPE_AUDIO_FLAC);
        addFileType(12295, "audio/x-aiff");
        addFileType(MtpConstants.FORMAT_MP2, str);
        addFileType(12299, "video/mpeg");
        addFileType(MtpConstants.FORMAT_MP4_CONTAINER, "video/mp4");
        addFileType(MtpConstants.FORMAT_3GP_CONTAINER, MediaFormat.MIMETYPE_VIDEO_H263);
        addFileType(MtpConstants.FORMAT_3GP_CONTAINER, "video/3gpp2");
        addFileType(12298, "video/avi");
        addFileType(MtpConstants.FORMAT_WMV, "video/x-ms-wmv");
        str = "video/x-ms-asf";
        addFileType(12300, str);
        addFileType(MtpConstants.FORMAT_EXIF_JPEG, "image/jpeg");
        addFileType(MtpConstants.FORMAT_GIF, "image/gif");
        addFileType(MtpConstants.FORMAT_PNG, "image/png");
        addFileType(MtpConstants.FORMAT_BMP, "image/x-ms-bmp");
        addFileType(MtpConstants.FORMAT_HEIF, "image/heif");
        addFileType(MtpConstants.FORMAT_DNG, "image/x-adobe-dng");
        String str2 = "image/tiff";
        addFileType(MtpConstants.FORMAT_TIFF, str2);
        addFileType(MtpConstants.FORMAT_TIFF, "image/x-canon-cr2");
        addFileType(MtpConstants.FORMAT_TIFF, "image/x-nikon-nrw");
        addFileType(MtpConstants.FORMAT_TIFF, "image/x-sony-arw");
        addFileType(MtpConstants.FORMAT_TIFF, "image/x-panasonic-rw2");
        addFileType(MtpConstants.FORMAT_TIFF, "image/x-olympus-orf");
        addFileType(MtpConstants.FORMAT_TIFF, "image/x-pentax-pef");
        addFileType(MtpConstants.FORMAT_TIFF, "image/x-samsung-srw");
        addFileType(MtpConstants.FORMAT_TIFF_EP, str2);
        addFileType(MtpConstants.FORMAT_TIFF_EP, "image/x-nikon-nef");
        addFileType(MtpConstants.FORMAT_JP2, "image/jp2");
        addFileType(MtpConstants.FORMAT_JPX, "image/jpx");
        addFileType(MtpConstants.FORMAT_M3U_PLAYLIST, "audio/x-mpegurl");
        addFileType(MtpConstants.FORMAT_PLS_PLAYLIST, "audio/x-scpls");
        addFileType(MtpConstants.FORMAT_WPL_PLAYLIST, "application/vnd.ms-wpl");
        addFileType(MtpConstants.FORMAT_ASX_PLAYLIST, str);
        addFileType(12292, ClipDescription.MIMETYPE_TEXT_PLAIN);
        addFileType(12293, ClipDescription.MIMETYPE_TEXT_HTML);
        addFileType(MtpConstants.FORMAT_XML_DOCUMENT, "text/xml");
        addFileType(MtpConstants.FORMAT_MS_WORD_DOCUMENT, "application/msword");
        addFileType(MtpConstants.FORMAT_MS_WORD_DOCUMENT, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        addFileType(MtpConstants.FORMAT_MS_EXCEL_SPREADSHEET, "application/vnd.ms-excel");
        addFileType(MtpConstants.FORMAT_MS_EXCEL_SPREADSHEET, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        addFileType(MtpConstants.FORMAT_MS_POWERPOINT_PRESENTATION, "application/vnd.ms-powerpoint");
        addFileType(MtpConstants.FORMAT_MS_POWERPOINT_PRESENTATION, "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        MediaFileInjector.addMiuiFileType();
    }

    @Deprecated
    @UnsupportedAppUsage
    static void addFileType(String extension, int fileType, String mimeType) {
    }

    private static void addFileType(int mtpFormatCode, String mimeType) {
        if (!sMimeTypeToFormatMap.containsKey(mimeType)) {
            sMimeTypeToFormatMap.put(mimeType, Integer.valueOf(mtpFormatCode));
        }
        if (!sFormatToMimeTypeMap.containsKey(Integer.valueOf(mtpFormatCode))) {
            sFormatToMimeTypeMap.put(Integer.valueOf(mtpFormatCode), mimeType);
        }
    }

    @Deprecated
    @UnsupportedAppUsage
    public static boolean isAudioFileType(int fileType) {
        return false;
    }

    @Deprecated
    @UnsupportedAppUsage
    public static boolean isVideoFileType(int fileType) {
        return false;
    }

    @Deprecated
    @UnsupportedAppUsage
    public static boolean isImageFileType(int fileType) {
        return false;
    }

    @Deprecated
    @UnsupportedAppUsage
    public static boolean isPlayListFileType(int fileType) {
        return false;
    }

    @Deprecated
    @UnsupportedAppUsage
    public static boolean isDrmFileType(int fileType) {
        return false;
    }

    @Deprecated
    @UnsupportedAppUsage
    public static MediaFileType getFileType(String path) {
        return null;
    }

    public static boolean isExifMimeType(String mimeType) {
        return isImageMimeType(mimeType);
    }

    public static boolean isAudioMimeType(String mimeType) {
        return normalizeMimeType(mimeType).startsWith("audio/");
    }

    public static boolean isVideoMimeType(String mimeType) {
        return normalizeMimeType(mimeType).startsWith("video/");
    }

    public static boolean isImageMimeType(String mimeType) {
        return normalizeMimeType(mimeType).startsWith(MessagingMessage.IMAGE_MIME_TYPE_PREFIX);
    }

    public static boolean isPlayListMimeType(java.lang.String r8) {
        /*
        r0 = normalizeMimeType(r8);
        r1 = r0.hashCode();
        r2 = 0;
        r3 = 5;
        r4 = 4;
        r5 = 3;
        r6 = 2;
        r7 = 1;
        switch(r1) {
            case -1165508903: goto L_0x0044;
            case -979095690: goto L_0x003a;
            case -622808459: goto L_0x0030;
            case -432766831: goto L_0x0026;
            case 264230524: goto L_0x001c;
            case 1872259501: goto L_0x0012;
            default: goto L_0x0011;
        };
    L_0x0011:
        goto L_0x004e;
    L_0x0012:
        r1 = "application/vnd.ms-wpl";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0011;
    L_0x001a:
        r0 = r2;
        goto L_0x004f;
    L_0x001c:
        r1 = "audio/x-mpegurl";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0011;
    L_0x0024:
        r0 = r7;
        goto L_0x004f;
    L_0x0026:
        r1 = "audio/mpegurl";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0011;
    L_0x002e:
        r0 = r6;
        goto L_0x004f;
    L_0x0030:
        r1 = "application/vnd.apple.mpegurl";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0011;
    L_0x0038:
        r0 = r4;
        goto L_0x004f;
    L_0x003a:
        r1 = "application/x-mpegurl";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0011;
    L_0x0042:
        r0 = r5;
        goto L_0x004f;
    L_0x0044:
        r1 = "audio/x-scpls";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0011;
    L_0x004c:
        r0 = r3;
        goto L_0x004f;
    L_0x004e:
        r0 = -1;
    L_0x004f:
        if (r0 == 0) goto L_0x005c;
    L_0x0051:
        if (r0 == r7) goto L_0x005c;
    L_0x0053:
        if (r0 == r6) goto L_0x005c;
    L_0x0055:
        if (r0 == r5) goto L_0x005c;
    L_0x0057:
        if (r0 == r4) goto L_0x005c;
    L_0x0059:
        if (r0 == r3) goto L_0x005c;
    L_0x005b:
        return r2;
    L_0x005c:
        return r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaFile.isPlayListMimeType(java.lang.String):boolean");
    }

    public static boolean isDrmMimeType(String mimeType) {
        return normalizeMimeType(mimeType).equals("application/x-android-drm-fl");
    }

    @UnsupportedAppUsage
    public static String getFileTitle(String path) {
        int lastSlash = path.lastIndexOf(47);
        if (lastSlash >= 0) {
            lastSlash++;
            if (lastSlash < path.length()) {
                path = path.substring(lastSlash);
            }
        }
        int lastDot = path.lastIndexOf(46);
        if (lastDot > 0) {
            return path.substring(0, lastDot);
        }
        return path;
    }

    public static String getFileExtension(String path) {
        if (path == null) {
            return null;
        }
        int lastDot = path.lastIndexOf(46);
        if (lastDot >= 0) {
            return path.substring(lastDot + 1);
        }
        return null;
    }

    @Deprecated
    @UnsupportedAppUsage
    public static int getFileTypeForMimeType(String mimeType) {
        return 0;
    }

    public static String getMimeType(String path, int formatCode) {
        String mimeType = getMimeTypeForFile(path);
        if (ContentResolver.MIME_TYPE_DEFAULT.equals(mimeType)) {
            return getMimeTypeForFormatCode(formatCode);
        }
        return mimeType;
    }

    @UnsupportedAppUsage
    public static String getMimeTypeForFile(String path) {
        String mimeType = MimeUtils.guessMimeTypeFromExtension(getFileExtension(path));
        return mimeType != null ? mimeType : ContentResolver.MIME_TYPE_DEFAULT;
    }

    public static String getMimeTypeForFormatCode(int formatCode) {
        String mimeType = (String) sFormatToMimeTypeMap.get(Integer.valueOf(formatCode));
        return mimeType != null ? mimeType : ContentResolver.MIME_TYPE_DEFAULT;
    }

    public static int getFormatCode(String path, String mimeType) {
        int formatCode = getFormatCodeForMimeType(mimeType);
        if (formatCode != 12288) {
            return formatCode;
        }
        return getFormatCodeForFile(path);
    }

    public static int getFormatCodeForFile(String path) {
        return getFormatCodeForMimeType(getMimeTypeForFile(path));
    }

    public static int getFormatCodeForMimeType(String mimeType) {
        if (mimeType == null) {
            return 12288;
        }
        Integer value = (Integer) sMimeTypeToFormatMap.get(mimeType);
        if (value != null) {
            return value.intValue();
        }
        mimeType = normalizeMimeType(mimeType);
        value = (Integer) sMimeTypeToFormatMap.get(mimeType);
        if (value != null) {
            return value.intValue();
        }
        if (mimeType.startsWith("audio/")) {
            return MtpConstants.FORMAT_UNDEFINED_AUDIO;
        }
        if (mimeType.startsWith("video/")) {
            return MtpConstants.FORMAT_UNDEFINED_VIDEO;
        }
        if (mimeType.startsWith(MessagingMessage.IMAGE_MIME_TYPE_PREFIX)) {
            return 14336;
        }
        return 12288;
    }

    private static String normalizeMimeType(String mimeType) {
        String extension = MimeUtils.guessExtensionFromMimeType(mimeType);
        if (extension != null) {
            String extensionMimeType = MimeUtils.guessMimeTypeFromExtension(extension);
            if (extensionMimeType != null) {
                return extensionMimeType;
            }
        }
        return mimeType != null ? mimeType : ContentResolver.MIME_TYPE_DEFAULT;
    }
}
