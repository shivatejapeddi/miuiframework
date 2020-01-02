package android.media;

import android.annotation.UnsupportedAppUsage;
import android.content.res.AssetManager.AssetInputStream;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiEnterpriseConfig;
import android.opengl.GLES30;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.text.format.Time;
import android.util.Log;
import android.util.Pair;
import com.android.internal.telephony.gsm.SmsCbConstants;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import libcore.io.IoUtils;
import libcore.io.Streams;

public class ExifInterface {
    private static final Charset ASCII = Charset.forName("US-ASCII");
    private static final int[] BITS_PER_SAMPLE_GREYSCALE_1 = new int[]{4};
    private static final int[] BITS_PER_SAMPLE_GREYSCALE_2 = new int[]{8};
    private static final int[] BITS_PER_SAMPLE_RGB = new int[]{8, 8, 8};
    private static final short BYTE_ALIGN_II = (short) 18761;
    private static final short BYTE_ALIGN_MM = (short) 19789;
    private static final int DATA_DEFLATE_ZIP = 8;
    private static final int DATA_HUFFMAN_COMPRESSED = 2;
    private static final int DATA_JPEG = 6;
    private static final int DATA_JPEG_COMPRESSED = 7;
    private static final int DATA_LOSSY_JPEG = 34892;
    private static final int DATA_PACK_BITS_COMPRESSED = 32773;
    private static final int DATA_UNCOMPRESSED = 1;
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final byte[] EXIF_ASCII_PREFIX = new byte[]{(byte) 65, (byte) 83, (byte) 67, (byte) 73, (byte) 73, (byte) 0, (byte) 0, (byte) 0};
    private static final ExifTag[] EXIF_POINTER_TAGS = new ExifTag[]{new ExifTag(TAG_SUB_IFD_POINTER, 330, 4, null), new ExifTag(TAG_EXIF_IFD_POINTER, 34665, 4, null), new ExifTag(TAG_GPS_INFO_IFD_POINTER, (int) GLES30.GL_DRAW_BUFFER0, 4, null), new ExifTag(TAG_INTEROPERABILITY_IFD_POINTER, 40965, 4, null), new ExifTag(TAG_ORF_CAMERA_SETTINGS_IFD_POINTER, 8224, 1, null), new ExifTag(TAG_ORF_IMAGE_PROCESSING_IFD_POINTER, 8256, 1, null)};
    private static final ExifTag[][] EXIF_TAGS;
    private static final byte[] HEIF_BRAND_HEIC = new byte[]{(byte) 104, (byte) 101, (byte) 105, (byte) 99};
    private static final byte[] HEIF_BRAND_MIF1 = new byte[]{(byte) 109, (byte) 105, (byte) 102, (byte) 49};
    private static final byte[] HEIF_TYPE_FTYP = new byte[]{(byte) 102, (byte) 116, (byte) 121, (byte) 112};
    private static final byte[] IDENTIFIER_EXIF_APP1 = "Exif\u0000\u0000".getBytes(ASCII);
    private static final byte[] IDENTIFIER_XMP_APP1 = "http://ns.adobe.com/xap/1.0/\u0000".getBytes(ASCII);
    private static final ExifTag[] IFD_EXIF_TAGS = new ExifTag[]{new ExifTag(TAG_EXPOSURE_TIME, 33434, 5, null), new ExifTag("FNumber", 33437, 5, null), new ExifTag(TAG_EXPOSURE_PROGRAM, 34850, 3, null), new ExifTag(TAG_SPECTRAL_SENSITIVITY, (int) GLES30.GL_MAX_DRAW_BUFFERS, 2, null), new ExifTag("ISOSpeedRatings", (int) GLES30.GL_DRAW_BUFFER2, 3, null), new ExifTag(TAG_OECF, (int) GLES30.GL_DRAW_BUFFER3, 7, null), new ExifTag(TAG_EXIF_VERSION, 36864, 2, null), new ExifTag(TAG_DATETIME_ORIGINAL, 36867, 2, null), new ExifTag(TAG_DATETIME_DIGITIZED, 36868, 2, null), new ExifTag(TAG_OFFSET_TIME, 36880, 2, null), new ExifTag(TAG_OFFSET_TIME_ORIGINAL, 36881, 2, null), new ExifTag(TAG_OFFSET_TIME_DIGITIZED, 36882, 2, null), new ExifTag(TAG_COMPONENTS_CONFIGURATION, 37121, 7, null), new ExifTag(TAG_COMPRESSED_BITS_PER_PIXEL, 37122, 5, null), new ExifTag(TAG_SHUTTER_SPEED_VALUE, 37377, 10, null), new ExifTag(TAG_APERTURE_VALUE, 37378, 5, null), new ExifTag(TAG_BRIGHTNESS_VALUE, 37379, 10, null), new ExifTag(TAG_EXPOSURE_BIAS_VALUE, 37380, 10, null), new ExifTag(TAG_MAX_APERTURE_VALUE, 37381, 5, null), new ExifTag(TAG_SUBJECT_DISTANCE, 37382, 5, null), new ExifTag(TAG_METERING_MODE, 37383, 3, null), new ExifTag(TAG_LIGHT_SOURCE, 37384, 3, null), new ExifTag(TAG_FLASH, 37385, 3, null), new ExifTag(TAG_FOCAL_LENGTH, 37386, 5, null), new ExifTag(TAG_SUBJECT_AREA, 37396, 3, null), new ExifTag(TAG_MAKER_NOTE, 37500, 7, null), new ExifTag(TAG_USER_COMMENT, 37510, 7, null), new ExifTag(TAG_SUBSEC_TIME, 37520, 2, null), new ExifTag("SubSecTimeOriginal", 37521, 2, null), new ExifTag("SubSecTimeDigitized", 37522, 2, null), new ExifTag(TAG_FLASHPIX_VERSION, 40960, 7, null), new ExifTag(TAG_COLOR_SPACE, 40961, 3, null), new ExifTag(TAG_PIXEL_X_DIMENSION, 40962, 3, 4, null), new ExifTag(TAG_PIXEL_Y_DIMENSION, 40963, 3, 4, null), new ExifTag(TAG_RELATED_SOUND_FILE, 40964, 2, null), new ExifTag(TAG_INTEROPERABILITY_IFD_POINTER, 40965, 4, null), new ExifTag(TAG_FLASH_ENERGY, 41483, 5, null), new ExifTag(TAG_SPATIAL_FREQUENCY_RESPONSE, 41484, 7, null), new ExifTag(TAG_FOCAL_PLANE_X_RESOLUTION, 41486, 5, null), new ExifTag(TAG_FOCAL_PLANE_Y_RESOLUTION, 41487, 5, null), new ExifTag(TAG_FOCAL_PLANE_RESOLUTION_UNIT, 41488, 3, null), new ExifTag(TAG_SUBJECT_LOCATION, 41492, 3, null), new ExifTag(TAG_EXPOSURE_INDEX, 41493, 5, null), new ExifTag(TAG_SENSING_METHOD, 41495, 3, null), new ExifTag(TAG_FILE_SOURCE, 41728, 7, null), new ExifTag(TAG_SCENE_TYPE, 41729, 7, null), new ExifTag(TAG_CFA_PATTERN, 41730, 7, null), new ExifTag(TAG_CUSTOM_RENDERED, 41985, 3, null), new ExifTag(TAG_EXPOSURE_MODE, 41986, 3, null), new ExifTag(TAG_WHITE_BALANCE, 41987, 3, null), new ExifTag(TAG_DIGITAL_ZOOM_RATIO, 41988, 5, null), new ExifTag(TAG_FOCAL_LENGTH_IN_35MM_FILM, 41989, 3, null), new ExifTag(TAG_SCENE_CAPTURE_TYPE, 41990, 3, null), new ExifTag(TAG_GAIN_CONTROL, 41991, 3, null), new ExifTag(TAG_CONTRAST, 41992, 3, null), new ExifTag(TAG_SATURATION, 41993, 3, null), new ExifTag(TAG_SHARPNESS, 41994, 3, null), new ExifTag(TAG_DEVICE_SETTING_DESCRIPTION, 41995, 7, null), new ExifTag(TAG_SUBJECT_DISTANCE_RANGE, 41996, 3, null), new ExifTag(TAG_IMAGE_UNIQUE_ID, 42016, 2, null), new ExifTag(TAG_DNG_VERSION, 50706, 1, null), new ExifTag(TAG_DEFAULT_CROP_SIZE, 50720, 3, 4, null)};
    private static final int IFD_FORMAT_BYTE = 1;
    private static final int[] IFD_FORMAT_BYTES_PER_FORMAT = new int[]{0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8, 1};
    private static final int IFD_FORMAT_DOUBLE = 12;
    private static final int IFD_FORMAT_IFD = 13;
    private static final String[] IFD_FORMAT_NAMES = new String[]{"", "BYTE", "STRING", "USHORT", "ULONG", "URATIONAL", "SBYTE", "UNDEFINED", "SSHORT", "SLONG", "SRATIONAL", "SINGLE", "DOUBLE"};
    private static final int IFD_FORMAT_SBYTE = 6;
    private static final int IFD_FORMAT_SINGLE = 11;
    private static final int IFD_FORMAT_SLONG = 9;
    private static final int IFD_FORMAT_SRATIONAL = 10;
    private static final int IFD_FORMAT_SSHORT = 8;
    private static final int IFD_FORMAT_STRING = 2;
    private static final int IFD_FORMAT_ULONG = 4;
    private static final int IFD_FORMAT_UNDEFINED = 7;
    private static final int IFD_FORMAT_URATIONAL = 5;
    private static final int IFD_FORMAT_USHORT = 3;
    private static final ExifTag[] IFD_GPS_TAGS = new ExifTag[]{new ExifTag(TAG_GPS_VERSION_ID, 0, 1, null), new ExifTag(TAG_GPS_LATITUDE_REF, 1, 2, null), new ExifTag(TAG_GPS_LATITUDE, 2, 5, null), new ExifTag(TAG_GPS_LONGITUDE_REF, 3, 2, null), new ExifTag(TAG_GPS_LONGITUDE, 4, 5, null), new ExifTag(TAG_GPS_ALTITUDE_REF, 5, 1, null), new ExifTag(TAG_GPS_ALTITUDE, 6, 5, null), new ExifTag(TAG_GPS_TIMESTAMP, 7, 5, null), new ExifTag(TAG_GPS_SATELLITES, 8, 2, null), new ExifTag(TAG_GPS_STATUS, 9, 2, null), new ExifTag(TAG_GPS_MEASURE_MODE, 10, 2, null), new ExifTag(TAG_GPS_DOP, 11, 5, null), new ExifTag(TAG_GPS_SPEED_REF, 12, 2, null), new ExifTag(TAG_GPS_SPEED, 13, 5, null), new ExifTag(TAG_GPS_TRACK_REF, 14, 2, null), new ExifTag(TAG_GPS_TRACK, 15, 5, null), new ExifTag(TAG_GPS_IMG_DIRECTION_REF, 16, 2, null), new ExifTag(TAG_GPS_IMG_DIRECTION, 17, 5, null), new ExifTag(TAG_GPS_MAP_DATUM, 18, 2, null), new ExifTag(TAG_GPS_DEST_LATITUDE_REF, 19, 2, null), new ExifTag(TAG_GPS_DEST_LATITUDE, 20, 5, null), new ExifTag(TAG_GPS_DEST_LONGITUDE_REF, 21, 2, null), new ExifTag(TAG_GPS_DEST_LONGITUDE, 22, 5, null), new ExifTag(TAG_GPS_DEST_BEARING_REF, 23, 2, null), new ExifTag(TAG_GPS_DEST_BEARING, 24, 5, null), new ExifTag(TAG_GPS_DEST_DISTANCE_REF, 25, 2, null), new ExifTag(TAG_GPS_DEST_DISTANCE, 26, 5, null), new ExifTag(TAG_GPS_PROCESSING_METHOD, 27, 7, null), new ExifTag(TAG_GPS_AREA_INFORMATION, 28, 7, null), new ExifTag(TAG_GPS_DATESTAMP, 29, 2, null), new ExifTag(TAG_GPS_DIFFERENTIAL, 30, 3, null)};
    private static final ExifTag[] IFD_INTEROPERABILITY_TAGS = new ExifTag[]{new ExifTag(TAG_INTEROPERABILITY_INDEX, 1, 2, null)};
    private static final int IFD_OFFSET = 8;
    private static final ExifTag[] IFD_THUMBNAIL_TAGS = new ExifTag[]{new ExifTag(TAG_NEW_SUBFILE_TYPE, 254, 4, null), new ExifTag(TAG_SUBFILE_TYPE, 255, 4, null), new ExifTag(TAG_THUMBNAIL_IMAGE_WIDTH, 256, 3, 4, null), new ExifTag(TAG_THUMBNAIL_IMAGE_LENGTH, 257, 3, 4, null), new ExifTag(TAG_BITS_PER_SAMPLE, 258, 3, null), new ExifTag(TAG_COMPRESSION, 259, 3, null), new ExifTag(TAG_PHOTOMETRIC_INTERPRETATION, 262, 3, null), new ExifTag(TAG_IMAGE_DESCRIPTION, 270, 2, null), new ExifTag(TAG_MAKE, 271, 2, null), new ExifTag(TAG_MODEL, 272, 2, null), new ExifTag(TAG_STRIP_OFFSETS, 273, 3, 4, null), new ExifTag(TAG_ORIENTATION, 274, 3, null), new ExifTag(TAG_SAMPLES_PER_PIXEL, 277, 3, null), new ExifTag(TAG_ROWS_PER_STRIP, 278, 3, 4, null), new ExifTag(TAG_STRIP_BYTE_COUNTS, 279, 3, 4, null), new ExifTag(TAG_X_RESOLUTION, 282, 5, null), new ExifTag(TAG_Y_RESOLUTION, 283, 5, null), new ExifTag(TAG_PLANAR_CONFIGURATION, 284, 3, null), new ExifTag(TAG_RESOLUTION_UNIT, 296, 3, null), new ExifTag(TAG_TRANSFER_FUNCTION, 301, 3, null), new ExifTag(TAG_SOFTWARE, 305, 2, null), new ExifTag("DateTime", 306, 2, null), new ExifTag(TAG_ARTIST, 315, 2, null), new ExifTag(TAG_WHITE_POINT, 318, 5, null), new ExifTag(TAG_PRIMARY_CHROMATICITIES, 319, 5, null), new ExifTag(TAG_SUB_IFD_POINTER, 330, 4, null), new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT, 513, 4, null), new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, 514, 4, null), new ExifTag(TAG_Y_CB_CR_COEFFICIENTS, 529, 5, null), new ExifTag(TAG_Y_CB_CR_SUB_SAMPLING, 530, 3, null), new ExifTag(TAG_Y_CB_CR_POSITIONING, 531, 3, null), new ExifTag(TAG_REFERENCE_BLACK_WHITE, 532, 5, null), new ExifTag(TAG_COPYRIGHT, 33432, 2, null), new ExifTag(TAG_EXIF_IFD_POINTER, 34665, 4, null), new ExifTag(TAG_GPS_INFO_IFD_POINTER, (int) GLES30.GL_DRAW_BUFFER0, 4, null), new ExifTag(TAG_DNG_VERSION, 50706, 1, null), new ExifTag(TAG_DEFAULT_CROP_SIZE, 50720, 3, 4, null)};
    private static final ExifTag[] IFD_TIFF_TAGS = new ExifTag[]{new ExifTag(TAG_NEW_SUBFILE_TYPE, 254, 4, null), new ExifTag(TAG_SUBFILE_TYPE, 255, 4, null), new ExifTag(TAG_IMAGE_WIDTH, 256, 3, 4, null), new ExifTag(TAG_IMAGE_LENGTH, 257, 3, 4, null), new ExifTag(TAG_BITS_PER_SAMPLE, 258, 3, null), new ExifTag(TAG_COMPRESSION, 259, 3, null), new ExifTag(TAG_PHOTOMETRIC_INTERPRETATION, 262, 3, null), new ExifTag(TAG_IMAGE_DESCRIPTION, 270, 2, null), new ExifTag(TAG_MAKE, 271, 2, null), new ExifTag(TAG_MODEL, 272, 2, null), new ExifTag(TAG_STRIP_OFFSETS, 273, 3, 4, null), new ExifTag(TAG_ORIENTATION, 274, 3, null), new ExifTag(TAG_SAMPLES_PER_PIXEL, 277, 3, null), new ExifTag(TAG_ROWS_PER_STRIP, 278, 3, 4, null), new ExifTag(TAG_STRIP_BYTE_COUNTS, 279, 3, 4, null), new ExifTag(TAG_X_RESOLUTION, 282, 5, null), new ExifTag(TAG_Y_RESOLUTION, 283, 5, null), new ExifTag(TAG_PLANAR_CONFIGURATION, 284, 3, null), new ExifTag(TAG_RESOLUTION_UNIT, 296, 3, null), new ExifTag(TAG_TRANSFER_FUNCTION, 301, 3, null), new ExifTag(TAG_SOFTWARE, 305, 2, null), new ExifTag("DateTime", 306, 2, null), new ExifTag(TAG_ARTIST, 315, 2, null), new ExifTag(TAG_WHITE_POINT, 318, 5, null), new ExifTag(TAG_PRIMARY_CHROMATICITIES, 319, 5, null), new ExifTag(TAG_SUB_IFD_POINTER, 330, 4, null), new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT, 513, 4, null), new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, 514, 4, null), new ExifTag(TAG_Y_CB_CR_COEFFICIENTS, 529, 5, null), new ExifTag(TAG_Y_CB_CR_SUB_SAMPLING, 530, 3, null), new ExifTag(TAG_Y_CB_CR_POSITIONING, 531, 3, null), new ExifTag(TAG_REFERENCE_BLACK_WHITE, 532, 5, null), new ExifTag(TAG_COPYRIGHT, 33432, 2, null), new ExifTag(TAG_EXIF_IFD_POINTER, 34665, 4, null), new ExifTag(TAG_GPS_INFO_IFD_POINTER, (int) GLES30.GL_DRAW_BUFFER0, 4, null), new ExifTag(TAG_RW2_SENSOR_TOP_BORDER, 4, 4, null), new ExifTag(TAG_RW2_SENSOR_LEFT_BORDER, 5, 4, null), new ExifTag(TAG_RW2_SENSOR_BOTTOM_BORDER, 6, 4, null), new ExifTag(TAG_RW2_SENSOR_RIGHT_BORDER, 7, 4, null), new ExifTag(TAG_RW2_ISO, 23, 3, null), new ExifTag(TAG_RW2_JPG_FROM_RAW, 46, 7, null), new ExifTag(TAG_XMP, 700, 1, null)};
    private static final int IFD_TYPE_EXIF = 1;
    private static final int IFD_TYPE_GPS = 2;
    private static final int IFD_TYPE_INTEROPERABILITY = 3;
    private static final int IFD_TYPE_ORF_CAMERA_SETTINGS = 7;
    private static final int IFD_TYPE_ORF_IMAGE_PROCESSING = 8;
    private static final int IFD_TYPE_ORF_MAKER_NOTE = 6;
    private static final int IFD_TYPE_PEF = 9;
    private static final int IFD_TYPE_PREVIEW = 5;
    private static final int IFD_TYPE_PRIMARY = 0;
    private static final int IFD_TYPE_THUMBNAIL = 4;
    private static final int IMAGE_TYPE_ARW = 1;
    private static final int IMAGE_TYPE_CR2 = 2;
    private static final int IMAGE_TYPE_DNG = 3;
    private static final int IMAGE_TYPE_HEIF = 12;
    private static final int IMAGE_TYPE_JPEG = 4;
    private static final int IMAGE_TYPE_NEF = 5;
    private static final int IMAGE_TYPE_NRW = 6;
    private static final int IMAGE_TYPE_ORF = 7;
    private static final int IMAGE_TYPE_PEF = 8;
    private static final int IMAGE_TYPE_RAF = 9;
    private static final int IMAGE_TYPE_RW2 = 10;
    private static final int IMAGE_TYPE_SRW = 11;
    private static final int IMAGE_TYPE_UNKNOWN = 0;
    private static final ExifTag JPEG_INTERCHANGE_FORMAT_LENGTH_TAG = new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, 514, 4, null);
    private static final ExifTag JPEG_INTERCHANGE_FORMAT_TAG = new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT, 513, 4, null);
    private static final byte[] JPEG_SIGNATURE = new byte[]{(byte) -1, MARKER_SOI, (byte) -1};
    private static final byte MARKER = (byte) -1;
    private static final byte MARKER_APP1 = (byte) -31;
    private static final byte MARKER_COM = (byte) -2;
    private static final byte MARKER_EOI = (byte) -39;
    private static final byte MARKER_SOF0 = (byte) -64;
    private static final byte MARKER_SOF1 = (byte) -63;
    private static final byte MARKER_SOF10 = (byte) -54;
    private static final byte MARKER_SOF11 = (byte) -53;
    private static final byte MARKER_SOF13 = (byte) -51;
    private static final byte MARKER_SOF14 = (byte) -50;
    private static final byte MARKER_SOF15 = (byte) -49;
    private static final byte MARKER_SOF2 = (byte) -62;
    private static final byte MARKER_SOF3 = (byte) -61;
    private static final byte MARKER_SOF5 = (byte) -59;
    private static final byte MARKER_SOF6 = (byte) -58;
    private static final byte MARKER_SOF7 = (byte) -57;
    private static final byte MARKER_SOF9 = (byte) -55;
    private static final byte MARKER_SOI = (byte) -40;
    private static final byte MARKER_SOS = (byte) -38;
    private static final int MAX_THUMBNAIL_SIZE = 512;
    private static final ExifTag[] ORF_CAMERA_SETTINGS_TAGS = new ExifTag[]{new ExifTag(TAG_ORF_PREVIEW_IMAGE_START, 257, 4, null), new ExifTag(TAG_ORF_PREVIEW_IMAGE_LENGTH, 258, 4, null)};
    private static final ExifTag[] ORF_IMAGE_PROCESSING_TAGS = new ExifTag[]{new ExifTag(TAG_ORF_ASPECT_FRAME, (int) SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_IMMEDIATE_OBSERVED, 3, null)};
    private static final byte[] ORF_MAKER_NOTE_HEADER_1 = new byte[]{(byte) 79, (byte) 76, (byte) 89, (byte) 77, (byte) 80, (byte) 0};
    private static final int ORF_MAKER_NOTE_HEADER_1_SIZE = 8;
    private static final byte[] ORF_MAKER_NOTE_HEADER_2 = new byte[]{(byte) 79, (byte) 76, (byte) 89, (byte) 77, (byte) 80, (byte) 85, (byte) 83, (byte) 0, (byte) 73, (byte) 73};
    private static final int ORF_MAKER_NOTE_HEADER_2_SIZE = 12;
    private static final ExifTag[] ORF_MAKER_NOTE_TAGS = new ExifTag[]{new ExifTag(TAG_ORF_THUMBNAIL_IMAGE, 256, 7, null), new ExifTag(TAG_ORF_CAMERA_SETTINGS_IFD_POINTER, 8224, 4, null), new ExifTag(TAG_ORF_IMAGE_PROCESSING_IFD_POINTER, 8256, 4, null)};
    private static final short ORF_SIGNATURE_1 = (short) 20306;
    private static final short ORF_SIGNATURE_2 = (short) 21330;
    public static final int ORIENTATION_FLIP_HORIZONTAL = 2;
    public static final int ORIENTATION_FLIP_VERTICAL = 4;
    public static final int ORIENTATION_NORMAL = 1;
    public static final int ORIENTATION_ROTATE_180 = 3;
    public static final int ORIENTATION_ROTATE_270 = 8;
    public static final int ORIENTATION_ROTATE_90 = 6;
    public static final int ORIENTATION_TRANSPOSE = 5;
    public static final int ORIENTATION_TRANSVERSE = 7;
    public static final int ORIENTATION_UNDEFINED = 0;
    private static final int ORIGINAL_RESOLUTION_IMAGE = 0;
    private static final int PEF_MAKER_NOTE_SKIP_SIZE = 6;
    private static final String PEF_SIGNATURE = "PENTAX";
    private static final ExifTag[] PEF_TAGS = new ExifTag[]{new ExifTag(TAG_COLOR_SPACE, 55, 3, null)};
    private static final int PHOTOMETRIC_INTERPRETATION_BLACK_IS_ZERO = 1;
    private static final int PHOTOMETRIC_INTERPRETATION_RGB = 2;
    private static final int PHOTOMETRIC_INTERPRETATION_WHITE_IS_ZERO = 0;
    private static final int PHOTOMETRIC_INTERPRETATION_YCBCR = 6;
    private static final int RAF_INFO_SIZE = 160;
    private static final int RAF_JPEG_LENGTH_VALUE_SIZE = 4;
    private static final int RAF_OFFSET_TO_JPEG_IMAGE_OFFSET = 84;
    private static final String RAF_SIGNATURE = "FUJIFILMCCD-RAW";
    private static final int REDUCED_RESOLUTION_IMAGE = 1;
    private static final short RW2_SIGNATURE = (short) 85;
    private static final int SIGNATURE_CHECK_SIZE = 5000;
    private static final byte START_CODE = (byte) 42;
    private static final String TAG = "ExifInterface";
    @Deprecated
    public static final String TAG_APERTURE = "FNumber";
    public static final String TAG_APERTURE_VALUE = "ApertureValue";
    public static final String TAG_ARTIST = "Artist";
    public static final String TAG_BITS_PER_SAMPLE = "BitsPerSample";
    public static final String TAG_BRIGHTNESS_VALUE = "BrightnessValue";
    public static final String TAG_CFA_PATTERN = "CFAPattern";
    public static final String TAG_COLOR_SPACE = "ColorSpace";
    public static final String TAG_COMPONENTS_CONFIGURATION = "ComponentsConfiguration";
    public static final String TAG_COMPRESSED_BITS_PER_PIXEL = "CompressedBitsPerPixel";
    public static final String TAG_COMPRESSION = "Compression";
    public static final String TAG_CONTRAST = "Contrast";
    public static final String TAG_COPYRIGHT = "Copyright";
    public static final String TAG_CUSTOM_RENDERED = "CustomRendered";
    public static final String TAG_DATETIME = "DateTime";
    public static final String TAG_DATETIME_DIGITIZED = "DateTimeDigitized";
    public static final String TAG_DATETIME_ORIGINAL = "DateTimeOriginal";
    public static final String TAG_DEFAULT_CROP_SIZE = "DefaultCropSize";
    public static final String TAG_DEVICE_SETTING_DESCRIPTION = "DeviceSettingDescription";
    public static final String TAG_DIGITAL_ZOOM_RATIO = "DigitalZoomRatio";
    public static final String TAG_DNG_VERSION = "DNGVersion";
    private static final String TAG_EXIF_IFD_POINTER = "ExifIFDPointer";
    public static final String TAG_EXIF_VERSION = "ExifVersion";
    public static final String TAG_EXPOSURE_BIAS_VALUE = "ExposureBiasValue";
    public static final String TAG_EXPOSURE_INDEX = "ExposureIndex";
    public static final String TAG_EXPOSURE_MODE = "ExposureMode";
    public static final String TAG_EXPOSURE_PROGRAM = "ExposureProgram";
    public static final String TAG_EXPOSURE_TIME = "ExposureTime";
    public static final String TAG_FILE_SOURCE = "FileSource";
    public static final String TAG_FLASH = "Flash";
    public static final String TAG_FLASHPIX_VERSION = "FlashpixVersion";
    public static final String TAG_FLASH_ENERGY = "FlashEnergy";
    public static final String TAG_FOCAL_LENGTH = "FocalLength";
    public static final String TAG_FOCAL_LENGTH_IN_35MM_FILM = "FocalLengthIn35mmFilm";
    public static final String TAG_FOCAL_PLANE_RESOLUTION_UNIT = "FocalPlaneResolutionUnit";
    public static final String TAG_FOCAL_PLANE_X_RESOLUTION = "FocalPlaneXResolution";
    public static final String TAG_FOCAL_PLANE_Y_RESOLUTION = "FocalPlaneYResolution";
    public static final String TAG_F_NUMBER = "FNumber";
    public static final String TAG_GAIN_CONTROL = "GainControl";
    public static final String TAG_GPS_ALTITUDE = "GPSAltitude";
    public static final String TAG_GPS_ALTITUDE_REF = "GPSAltitudeRef";
    public static final String TAG_GPS_AREA_INFORMATION = "GPSAreaInformation";
    public static final String TAG_GPS_DATESTAMP = "GPSDateStamp";
    public static final String TAG_GPS_DEST_BEARING = "GPSDestBearing";
    public static final String TAG_GPS_DEST_BEARING_REF = "GPSDestBearingRef";
    public static final String TAG_GPS_DEST_DISTANCE = "GPSDestDistance";
    public static final String TAG_GPS_DEST_DISTANCE_REF = "GPSDestDistanceRef";
    public static final String TAG_GPS_DEST_LATITUDE = "GPSDestLatitude";
    public static final String TAG_GPS_DEST_LATITUDE_REF = "GPSDestLatitudeRef";
    public static final String TAG_GPS_DEST_LONGITUDE = "GPSDestLongitude";
    public static final String TAG_GPS_DEST_LONGITUDE_REF = "GPSDestLongitudeRef";
    public static final String TAG_GPS_DIFFERENTIAL = "GPSDifferential";
    public static final String TAG_GPS_DOP = "GPSDOP";
    public static final String TAG_GPS_IMG_DIRECTION = "GPSImgDirection";
    public static final String TAG_GPS_IMG_DIRECTION_REF = "GPSImgDirectionRef";
    private static final String TAG_GPS_INFO_IFD_POINTER = "GPSInfoIFDPointer";
    public static final String TAG_GPS_LATITUDE = "GPSLatitude";
    public static final String TAG_GPS_LATITUDE_REF = "GPSLatitudeRef";
    public static final String TAG_GPS_LONGITUDE = "GPSLongitude";
    public static final String TAG_GPS_LONGITUDE_REF = "GPSLongitudeRef";
    public static final String TAG_GPS_MAP_DATUM = "GPSMapDatum";
    public static final String TAG_GPS_MEASURE_MODE = "GPSMeasureMode";
    public static final String TAG_GPS_PROCESSING_METHOD = "GPSProcessingMethod";
    public static final String TAG_GPS_SATELLITES = "GPSSatellites";
    public static final String TAG_GPS_SPEED = "GPSSpeed";
    public static final String TAG_GPS_SPEED_REF = "GPSSpeedRef";
    public static final String TAG_GPS_STATUS = "GPSStatus";
    public static final String TAG_GPS_TIMESTAMP = "GPSTimeStamp";
    public static final String TAG_GPS_TRACK = "GPSTrack";
    public static final String TAG_GPS_TRACK_REF = "GPSTrackRef";
    public static final String TAG_GPS_VERSION_ID = "GPSVersionID";
    private static final String TAG_HAS_THUMBNAIL = "HasThumbnail";
    public static final String TAG_IMAGE_DESCRIPTION = "ImageDescription";
    public static final String TAG_IMAGE_LENGTH = "ImageLength";
    public static final String TAG_IMAGE_UNIQUE_ID = "ImageUniqueID";
    public static final String TAG_IMAGE_WIDTH = "ImageWidth";
    private static final String TAG_INTEROPERABILITY_IFD_POINTER = "InteroperabilityIFDPointer";
    public static final String TAG_INTEROPERABILITY_INDEX = "InteroperabilityIndex";
    @Deprecated
    public static final String TAG_ISO = "ISOSpeedRatings";
    public static final String TAG_ISO_SPEED_RATINGS = "ISOSpeedRatings";
    public static final String TAG_JPEG_INTERCHANGE_FORMAT = "JPEGInterchangeFormat";
    public static final String TAG_JPEG_INTERCHANGE_FORMAT_LENGTH = "JPEGInterchangeFormatLength";
    public static final String TAG_LIGHT_SOURCE = "LightSource";
    public static final String TAG_MAKE = "Make";
    public static final String TAG_MAKER_NOTE = "MakerNote";
    public static final String TAG_MAX_APERTURE_VALUE = "MaxApertureValue";
    public static final String TAG_METERING_MODE = "MeteringMode";
    public static final String TAG_MODEL = "Model";
    public static final String TAG_NEW_SUBFILE_TYPE = "NewSubfileType";
    public static final String TAG_OECF = "OECF";
    public static final String TAG_OFFSET_TIME = "OffsetTime";
    public static final String TAG_OFFSET_TIME_DIGITIZED = "OffsetTimeDigitized";
    public static final String TAG_OFFSET_TIME_ORIGINAL = "OffsetTimeOriginal";
    public static final String TAG_ORF_ASPECT_FRAME = "AspectFrame";
    private static final String TAG_ORF_CAMERA_SETTINGS_IFD_POINTER = "CameraSettingsIFDPointer";
    private static final String TAG_ORF_IMAGE_PROCESSING_IFD_POINTER = "ImageProcessingIFDPointer";
    public static final String TAG_ORF_PREVIEW_IMAGE_LENGTH = "PreviewImageLength";
    public static final String TAG_ORF_PREVIEW_IMAGE_START = "PreviewImageStart";
    public static final String TAG_ORF_THUMBNAIL_IMAGE = "ThumbnailImage";
    public static final String TAG_ORIENTATION = "Orientation";
    public static final String TAG_PHOTOMETRIC_INTERPRETATION = "PhotometricInterpretation";
    public static final String TAG_PIXEL_X_DIMENSION = "PixelXDimension";
    public static final String TAG_PIXEL_Y_DIMENSION = "PixelYDimension";
    public static final String TAG_PLANAR_CONFIGURATION = "PlanarConfiguration";
    public static final String TAG_PRIMARY_CHROMATICITIES = "PrimaryChromaticities";
    private static final ExifTag TAG_RAF_IMAGE_SIZE = new ExifTag(TAG_STRIP_OFFSETS, 273, 3, null);
    public static final String TAG_REFERENCE_BLACK_WHITE = "ReferenceBlackWhite";
    public static final String TAG_RELATED_SOUND_FILE = "RelatedSoundFile";
    public static final String TAG_RESOLUTION_UNIT = "ResolutionUnit";
    public static final String TAG_ROWS_PER_STRIP = "RowsPerStrip";
    public static final String TAG_RW2_ISO = "ISO";
    public static final String TAG_RW2_JPG_FROM_RAW = "JpgFromRaw";
    public static final String TAG_RW2_SENSOR_BOTTOM_BORDER = "SensorBottomBorder";
    public static final String TAG_RW2_SENSOR_LEFT_BORDER = "SensorLeftBorder";
    public static final String TAG_RW2_SENSOR_RIGHT_BORDER = "SensorRightBorder";
    public static final String TAG_RW2_SENSOR_TOP_BORDER = "SensorTopBorder";
    public static final String TAG_SAMPLES_PER_PIXEL = "SamplesPerPixel";
    public static final String TAG_SATURATION = "Saturation";
    public static final String TAG_SCENE_CAPTURE_TYPE = "SceneCaptureType";
    public static final String TAG_SCENE_TYPE = "SceneType";
    public static final String TAG_SENSING_METHOD = "SensingMethod";
    public static final String TAG_SHARPNESS = "Sharpness";
    public static final String TAG_SHUTTER_SPEED_VALUE = "ShutterSpeedValue";
    public static final String TAG_SOFTWARE = "Software";
    public static final String TAG_SPATIAL_FREQUENCY_RESPONSE = "SpatialFrequencyResponse";
    public static final String TAG_SPECTRAL_SENSITIVITY = "SpectralSensitivity";
    public static final String TAG_STRIP_BYTE_COUNTS = "StripByteCounts";
    public static final String TAG_STRIP_OFFSETS = "StripOffsets";
    public static final String TAG_SUBFILE_TYPE = "SubfileType";
    public static final String TAG_SUBJECT_AREA = "SubjectArea";
    public static final String TAG_SUBJECT_DISTANCE = "SubjectDistance";
    public static final String TAG_SUBJECT_DISTANCE_RANGE = "SubjectDistanceRange";
    public static final String TAG_SUBJECT_LOCATION = "SubjectLocation";
    public static final String TAG_SUBSEC_TIME = "SubSecTime";
    public static final String TAG_SUBSEC_TIME_DIG = "SubSecTimeDigitized";
    public static final String TAG_SUBSEC_TIME_DIGITIZED = "SubSecTimeDigitized";
    public static final String TAG_SUBSEC_TIME_ORIG = "SubSecTimeOriginal";
    public static final String TAG_SUBSEC_TIME_ORIGINAL = "SubSecTimeOriginal";
    private static final String TAG_SUB_IFD_POINTER = "SubIFDPointer";
    private static final String TAG_THUMBNAIL_DATA = "ThumbnailData";
    public static final String TAG_THUMBNAIL_IMAGE_LENGTH = "ThumbnailImageLength";
    public static final String TAG_THUMBNAIL_IMAGE_WIDTH = "ThumbnailImageWidth";
    private static final String TAG_THUMBNAIL_LENGTH = "ThumbnailLength";
    private static final String TAG_THUMBNAIL_OFFSET = "ThumbnailOffset";
    public static final String TAG_TRANSFER_FUNCTION = "TransferFunction";
    public static final String TAG_USER_COMMENT = "UserComment";
    public static final String TAG_WHITE_BALANCE = "WhiteBalance";
    public static final String TAG_WHITE_POINT = "WhitePoint";
    public static final String TAG_XMP = "Xmp";
    public static final String TAG_X_RESOLUTION = "XResolution";
    public static final String TAG_Y_CB_CR_COEFFICIENTS = "YCbCrCoefficients";
    public static final String TAG_Y_CB_CR_POSITIONING = "YCbCrPositioning";
    public static final String TAG_Y_CB_CR_SUB_SAMPLING = "YCbCrSubSampling";
    public static final String TAG_Y_RESOLUTION = "YResolution";
    public static final int WHITEBALANCE_AUTO = 0;
    public static final int WHITEBALANCE_MANUAL = 1;
    private static final HashMap<Integer, Integer> sExifPointerTagMap = new HashMap();
    private static final HashMap[] sExifTagMapsForReading;
    private static final HashMap[] sExifTagMapsForWriting;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private static SimpleDateFormat sFormatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
    private static SimpleDateFormat sFormatterTz = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss XXX");
    private static final Pattern sGpsTimestampPattern = Pattern.compile("^([0-9][0-9]):([0-9][0-9]):([0-9][0-9])$");
    private static final Pattern sNonZeroTimePattern = Pattern.compile(".*[1-9].*");
    private static final HashSet<String> sTagSetForCompatibility = new HashSet(Arrays.asList(new String[]{"FNumber", TAG_DIGITAL_ZOOM_RATIO, TAG_EXPOSURE_TIME, TAG_SUBJECT_DISTANCE, TAG_GPS_TIMESTAMP}));
    private AssetInputStream mAssetInputStream;
    @UnsupportedAppUsage
    private final HashMap[] mAttributes;
    private ByteOrder mExifByteOrder = ByteOrder.BIG_ENDIAN;
    private int mExifOffset;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private String mFilename;
    private Set<Integer> mHandledIfdOffsets;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private boolean mHasThumbnail;
    private boolean mIsInputStream;
    private boolean mIsSupportedFile;
    private int mMimeType;
    private boolean mModified;
    private int mOrfMakerNoteOffset;
    private int mOrfThumbnailLength;
    private int mOrfThumbnailOffset;
    private int mRw2JpgFromRawOffset;
    private FileDescriptor mSeekableFileDescriptor;
    private byte[] mThumbnailBytes;
    private int mThumbnailCompression;
    private int mThumbnailLength;
    private int mThumbnailOffset;

    private static class ByteOrderedDataInputStream extends InputStream implements DataInput {
        private static final ByteOrder BIG_ENDIAN = ByteOrder.BIG_ENDIAN;
        private static final ByteOrder LITTLE_ENDIAN = ByteOrder.LITTLE_ENDIAN;
        private ByteOrder mByteOrder;
        private DataInputStream mDataInputStream;
        private InputStream mInputStream;
        private final int mLength;
        private int mPosition;

        public ByteOrderedDataInputStream(InputStream in) throws IOException {
            this.mByteOrder = ByteOrder.BIG_ENDIAN;
            this.mInputStream = in;
            this.mDataInputStream = new DataInputStream(in);
            this.mLength = this.mDataInputStream.available();
            this.mPosition = 0;
            this.mDataInputStream.mark(this.mLength);
        }

        public ByteOrderedDataInputStream(byte[] bytes) throws IOException {
            this(new ByteArrayInputStream(bytes));
        }

        public void setByteOrder(ByteOrder byteOrder) {
            this.mByteOrder = byteOrder;
        }

        public void seek(long byteCount) throws IOException {
            int i = this.mPosition;
            if (((long) i) > byteCount) {
                this.mPosition = 0;
                this.mDataInputStream.reset();
                this.mDataInputStream.mark(this.mLength);
            } else {
                byteCount -= (long) i;
            }
            if (skipBytes((int) byteCount) != ((int) byteCount)) {
                throw new IOException("Couldn't seek up to the byteCount");
            }
        }

        public int peek() {
            return this.mPosition;
        }

        public int available() throws IOException {
            return this.mDataInputStream.available();
        }

        public int read() throws IOException {
            this.mPosition++;
            return this.mDataInputStream.read();
        }

        public int readUnsignedByte() throws IOException {
            this.mPosition++;
            return this.mDataInputStream.readUnsignedByte();
        }

        public String readLine() throws IOException {
            Log.d(ExifInterface.TAG, "Currently unsupported");
            return null;
        }

        public boolean readBoolean() throws IOException {
            this.mPosition++;
            return this.mDataInputStream.readBoolean();
        }

        public char readChar() throws IOException {
            this.mPosition += 2;
            return this.mDataInputStream.readChar();
        }

        public String readUTF() throws IOException {
            this.mPosition += 2;
            return this.mDataInputStream.readUTF();
        }

        public void readFully(byte[] buffer, int offset, int length) throws IOException {
            this.mPosition += length;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            } else if (this.mDataInputStream.read(buffer, offset, length) != length) {
                throw new IOException("Couldn't read up to the length of buffer");
            }
        }

        public void readFully(byte[] buffer) throws IOException {
            this.mPosition += buffer.length;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            } else if (this.mDataInputStream.read(buffer, 0, buffer.length) != buffer.length) {
                throw new IOException("Couldn't read up to the length of buffer");
            }
        }

        public byte readByte() throws IOException {
            this.mPosition++;
            if (this.mPosition <= this.mLength) {
                int ch = this.mDataInputStream.read();
                if (ch >= 0) {
                    return (byte) ch;
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        public short readShort() throws IOException {
            this.mPosition += 2;
            if (this.mPosition <= this.mLength) {
                int ch1 = this.mDataInputStream.read();
                int ch2 = this.mDataInputStream.read();
                if ((ch1 | ch2) >= 0) {
                    ByteOrder byteOrder = this.mByteOrder;
                    if (byteOrder == LITTLE_ENDIAN) {
                        return (short) ((ch2 << 8) + ch1);
                    }
                    if (byteOrder == BIG_ENDIAN) {
                        return (short) ((ch1 << 8) + ch2);
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Invalid byte order: ");
                    stringBuilder.append(this.mByteOrder);
                    throw new IOException(stringBuilder.toString());
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        public int readInt() throws IOException {
            this.mPosition += 4;
            if (this.mPosition <= this.mLength) {
                int ch1 = this.mDataInputStream.read();
                int ch2 = this.mDataInputStream.read();
                int ch3 = this.mDataInputStream.read();
                int ch4 = this.mDataInputStream.read();
                if ((((ch1 | ch2) | ch3) | ch4) >= 0) {
                    ByteOrder byteOrder = this.mByteOrder;
                    if (byteOrder == LITTLE_ENDIAN) {
                        return (((ch4 << 24) + (ch3 << 16)) + (ch2 << 8)) + ch1;
                    }
                    if (byteOrder == BIG_ENDIAN) {
                        return (((ch1 << 24) + (ch2 << 16)) + (ch3 << 8)) + ch4;
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Invalid byte order: ");
                    stringBuilder.append(this.mByteOrder);
                    throw new IOException(stringBuilder.toString());
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        public int skipBytes(int byteCount) throws IOException {
            int totalSkip = Math.min(byteCount, this.mLength - this.mPosition);
            int skipped = 0;
            while (skipped < totalSkip) {
                skipped += this.mDataInputStream.skipBytes(totalSkip - skipped);
            }
            this.mPosition += skipped;
            return skipped;
        }

        public int readUnsignedShort() throws IOException {
            this.mPosition += 2;
            if (this.mPosition <= this.mLength) {
                int ch1 = this.mDataInputStream.read();
                int ch2 = this.mDataInputStream.read();
                if ((ch1 | ch2) >= 0) {
                    ByteOrder byteOrder = this.mByteOrder;
                    if (byteOrder == LITTLE_ENDIAN) {
                        return (ch2 << 8) + ch1;
                    }
                    if (byteOrder == BIG_ENDIAN) {
                        return (ch1 << 8) + ch2;
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Invalid byte order: ");
                    stringBuilder.append(this.mByteOrder);
                    throw new IOException(stringBuilder.toString());
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        public long readUnsignedInt() throws IOException {
            return ((long) readInt()) & 4294967295L;
        }

        public long readLong() throws IOException {
            this.mPosition += 8;
            if (this.mPosition <= this.mLength) {
                int ch1 = this.mDataInputStream.read();
                int ch2 = this.mDataInputStream.read();
                int ch3 = this.mDataInputStream.read();
                int ch4 = this.mDataInputStream.read();
                int ch5 = this.mDataInputStream.read();
                int ch6 = this.mDataInputStream.read();
                int ch7 = this.mDataInputStream.read();
                int ch8 = this.mDataInputStream.read();
                if ((((((((ch1 | ch2) | ch3) | ch4) | ch5) | ch6) | ch7) | ch8) >= 0) {
                    ByteOrder byteOrder = this.mByteOrder;
                    if (byteOrder == LITTLE_ENDIAN) {
                        return (((((((((long) ch8) << 56) + (((long) ch7) << 48)) + (((long) ch6) << 40)) + (((long) ch5) << 32)) + (((long) ch4) << 24)) + (((long) ch3) << 16)) + (((long) ch2) << 8)) + ((long) ch1);
                    }
                    int ch22 = ch2;
                    if (byteOrder == BIG_ENDIAN) {
                        return (((((((((long) ch1) << 56) + (((long) ch22) << 48)) + (((long) ch3) << 40)) + (((long) ch4) << 32)) + (((long) ch5) << 24)) + (((long) ch6) << 16)) + (((long) ch7) << 8)) + ((long) ch8);
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Invalid byte order: ");
                    stringBuilder.append(this.mByteOrder);
                    throw new IOException(stringBuilder.toString());
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        public float readFloat() throws IOException {
            return Float.intBitsToFloat(readInt());
        }

        public double readDouble() throws IOException {
            return Double.longBitsToDouble(readLong());
        }

        public int getLength() {
            return this.mLength;
        }
    }

    private static class ByteOrderedDataOutputStream extends FilterOutputStream {
        private ByteOrder mByteOrder;
        private final OutputStream mOutputStream;

        public ByteOrderedDataOutputStream(OutputStream out, ByteOrder byteOrder) {
            super(out);
            this.mOutputStream = out;
            this.mByteOrder = byteOrder;
        }

        public void setByteOrder(ByteOrder byteOrder) {
            this.mByteOrder = byteOrder;
        }

        public void write(byte[] bytes) throws IOException {
            this.mOutputStream.write(bytes);
        }

        public void write(byte[] bytes, int offset, int length) throws IOException {
            this.mOutputStream.write(bytes, offset, length);
        }

        public void writeByte(int val) throws IOException {
            this.mOutputStream.write(val);
        }

        public void writeShort(short val) throws IOException {
            if (this.mByteOrder == ByteOrder.LITTLE_ENDIAN) {
                this.mOutputStream.write((val >>> 0) & 255);
                this.mOutputStream.write((val >>> 8) & 255);
            } else if (this.mByteOrder == ByteOrder.BIG_ENDIAN) {
                this.mOutputStream.write((val >>> 8) & 255);
                this.mOutputStream.write((val >>> 0) & 255);
            }
        }

        public void writeInt(int val) throws IOException {
            if (this.mByteOrder == ByteOrder.LITTLE_ENDIAN) {
                this.mOutputStream.write((val >>> 0) & 255);
                this.mOutputStream.write((val >>> 8) & 255);
                this.mOutputStream.write((val >>> 16) & 255);
                this.mOutputStream.write((val >>> 24) & 255);
            } else if (this.mByteOrder == ByteOrder.BIG_ENDIAN) {
                this.mOutputStream.write((val >>> 24) & 255);
                this.mOutputStream.write((val >>> 16) & 255);
                this.mOutputStream.write((val >>> 8) & 255);
                this.mOutputStream.write((val >>> 0) & 255);
            }
        }

        public void writeUnsignedShort(int val) throws IOException {
            writeShort((short) val);
        }

        public void writeUnsignedInt(long val) throws IOException {
            writeInt((int) val);
        }
    }

    private static class ExifAttribute {
        public static final long BYTES_OFFSET_UNKNOWN = -1;
        public final byte[] bytes;
        public final long bytesOffset;
        public final int format;
        public final int numberOfComponents;

        /* synthetic */ ExifAttribute(int x0, int x1, long x2, byte[] x3, AnonymousClass1 x4) {
            this(x0, x1, x2, x3);
        }

        private ExifAttribute(int format, int numberOfComponents, byte[] bytes) {
            this(format, numberOfComponents, -1, bytes);
        }

        private ExifAttribute(int format, int numberOfComponents, long bytesOffset, byte[] bytes) {
            this.format = format;
            this.numberOfComponents = numberOfComponents;
            this.bytesOffset = bytesOffset;
            this.bytes = bytes;
        }

        public static ExifAttribute createUShort(int[] values, ByteOrder byteOrder) {
            ByteBuffer buffer = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[3] * values.length)]);
            buffer.order(byteOrder);
            for (int value : values) {
                buffer.putShort((short) value);
            }
            return new ExifAttribute(3, values.length, buffer.array());
        }

        public static ExifAttribute createUShort(int value, ByteOrder byteOrder) {
            return createUShort(new int[]{value}, byteOrder);
        }

        public static ExifAttribute createULong(long[] values, ByteOrder byteOrder) {
            ByteBuffer buffer = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[4] * values.length)]);
            buffer.order(byteOrder);
            for (long value : values) {
                buffer.putInt((int) value);
            }
            return new ExifAttribute(4, values.length, buffer.array());
        }

        public static ExifAttribute createULong(long value, ByteOrder byteOrder) {
            return createULong(new long[]{value}, byteOrder);
        }

        public static ExifAttribute createSLong(int[] values, ByteOrder byteOrder) {
            ByteBuffer buffer = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[9] * values.length)]);
            buffer.order(byteOrder);
            for (int value : values) {
                buffer.putInt(value);
            }
            return new ExifAttribute(9, values.length, buffer.array());
        }

        public static ExifAttribute createSLong(int value, ByteOrder byteOrder) {
            return createSLong(new int[]{value}, byteOrder);
        }

        public static ExifAttribute createByte(String value) {
            byte[] ascii;
            if (value.length() != 1 || value.charAt(0) < '0' || value.charAt(0) > '1') {
                ascii = value.getBytes(ExifInterface.ASCII);
                return new ExifAttribute(1, ascii.length, ascii);
            }
            ascii = new byte[]{(byte) (value.charAt(0) - 48)};
            return new ExifAttribute(1, ascii.length, ascii);
        }

        public static ExifAttribute createString(String value) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(value);
            stringBuilder.append(0);
            byte[] ascii = stringBuilder.toString().getBytes(ExifInterface.ASCII);
            return new ExifAttribute(2, ascii.length, ascii);
        }

        public static ExifAttribute createURational(Rational[] values, ByteOrder byteOrder) {
            ByteBuffer buffer = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[5] * values.length)]);
            buffer.order(byteOrder);
            for (Rational value : values) {
                buffer.putInt((int) value.numerator);
                buffer.putInt((int) value.denominator);
            }
            return new ExifAttribute(5, values.length, buffer.array());
        }

        public static ExifAttribute createURational(Rational value, ByteOrder byteOrder) {
            return createURational(new Rational[]{value}, byteOrder);
        }

        public static ExifAttribute createSRational(Rational[] values, ByteOrder byteOrder) {
            ByteBuffer buffer = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[10] * values.length)]);
            buffer.order(byteOrder);
            for (Rational value : values) {
                buffer.putInt((int) value.numerator);
                buffer.putInt((int) value.denominator);
            }
            return new ExifAttribute(10, values.length, buffer.array());
        }

        public static ExifAttribute createSRational(Rational value, ByteOrder byteOrder) {
            return createSRational(new Rational[]{value}, byteOrder);
        }

        public static ExifAttribute createDouble(double[] values, ByteOrder byteOrder) {
            ByteBuffer buffer = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[12] * values.length)]);
            buffer.order(byteOrder);
            for (double value : values) {
                buffer.putDouble(value);
            }
            return new ExifAttribute(12, values.length, buffer.array());
        }

        public static ExifAttribute createDouble(double value, ByteOrder byteOrder) {
            return createDouble(new double[]{value}, byteOrder);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("(");
            stringBuilder.append(ExifInterface.IFD_FORMAT_NAMES[this.format]);
            stringBuilder.append(", data length:");
            stringBuilder.append(this.bytes.length);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }

        /* JADX WARNING: Removed duplicated region for block: B:58:0x00ef A:{Catch:{ IOException -> 0x014b }} */
        private java.lang.Object getValue(java.nio.ByteOrder r18) {
            /*
            r17 = this;
            r1 = r17;
            r2 = 0;
            r0 = new android.media.ExifInterface$ByteOrderedDataInputStream;	 Catch:{ IOException -> 0x014d }
            r3 = r1.bytes;	 Catch:{ IOException -> 0x014d }
            r0.<init>(r3);	 Catch:{ IOException -> 0x014d }
            r3 = r18;
            r0.setByteOrder(r3);	 Catch:{ IOException -> 0x014b }
            r4 = r1.format;	 Catch:{ IOException -> 0x014b }
            r5 = 0;
            switch(r4) {
                case 1: goto L_0x011c;
                case 2: goto L_0x00c9;
                case 3: goto L_0x00b6;
                case 4: goto L_0x00a3;
                case 5: goto L_0x0085;
                case 6: goto L_0x011c;
                case 7: goto L_0x00c9;
                case 8: goto L_0x0072;
                case 9: goto L_0x005f;
                case 10: goto L_0x003d;
                case 11: goto L_0x0029;
                case 12: goto L_0x0016;
                default: goto L_0x0015;
            };	 Catch:{ IOException -> 0x014b }
        L_0x0015:
            return r2;
        L_0x0016:
            r4 = r1.numberOfComponents;	 Catch:{ IOException -> 0x014b }
            r4 = new double[r4];	 Catch:{ IOException -> 0x014b }
        L_0x001b:
            r6 = r1.numberOfComponents;	 Catch:{ IOException -> 0x014b }
            if (r5 >= r6) goto L_0x0028;
        L_0x001f:
            r6 = r0.readDouble();	 Catch:{ IOException -> 0x014b }
            r4[r5] = r6;	 Catch:{ IOException -> 0x014b }
            r5 = r5 + 1;
            goto L_0x001b;
        L_0x0028:
            return r4;
        L_0x0029:
            r4 = r1.numberOfComponents;	 Catch:{ IOException -> 0x014b }
            r4 = new double[r4];	 Catch:{ IOException -> 0x014b }
        L_0x002e:
            r6 = r1.numberOfComponents;	 Catch:{ IOException -> 0x014b }
            if (r5 >= r6) goto L_0x003c;
        L_0x0032:
            r6 = r0.readFloat();	 Catch:{ IOException -> 0x014b }
            r6 = (double) r6;	 Catch:{ IOException -> 0x014b }
            r4[r5] = r6;	 Catch:{ IOException -> 0x014b }
            r5 = r5 + 1;
            goto L_0x002e;
        L_0x003c:
            return r4;
        L_0x003d:
            r4 = r1.numberOfComponents;	 Catch:{ IOException -> 0x014b }
            r4 = new android.media.ExifInterface.Rational[r4];	 Catch:{ IOException -> 0x014b }
        L_0x0042:
            r6 = r1.numberOfComponents;	 Catch:{ IOException -> 0x014b }
            if (r5 >= r6) goto L_0x005e;
        L_0x0046:
            r6 = r0.readInt();	 Catch:{ IOException -> 0x014b }
            r13 = (long) r6;	 Catch:{ IOException -> 0x014b }
            r6 = r0.readInt();	 Catch:{ IOException -> 0x014b }
            r10 = (long) r6;	 Catch:{ IOException -> 0x014b }
            r6 = new android.media.ExifInterface$Rational;	 Catch:{ IOException -> 0x014b }
            r12 = 0;
            r7 = r6;
            r8 = r13;
            r15 = r10;
            r7.<init>(r8, r10, r12);	 Catch:{ IOException -> 0x014b }
            r4[r5] = r6;	 Catch:{ IOException -> 0x014b }
            r5 = r5 + 1;
            goto L_0x0042;
        L_0x005e:
            return r4;
        L_0x005f:
            r4 = r1.numberOfComponents;	 Catch:{ IOException -> 0x014b }
            r4 = new int[r4];	 Catch:{ IOException -> 0x014b }
        L_0x0064:
            r6 = r1.numberOfComponents;	 Catch:{ IOException -> 0x014b }
            if (r5 >= r6) goto L_0x0071;
        L_0x0068:
            r6 = r0.readInt();	 Catch:{ IOException -> 0x014b }
            r4[r5] = r6;	 Catch:{ IOException -> 0x014b }
            r5 = r5 + 1;
            goto L_0x0064;
        L_0x0071:
            return r4;
        L_0x0072:
            r4 = r1.numberOfComponents;	 Catch:{ IOException -> 0x014b }
            r4 = new int[r4];	 Catch:{ IOException -> 0x014b }
        L_0x0077:
            r6 = r1.numberOfComponents;	 Catch:{ IOException -> 0x014b }
            if (r5 >= r6) goto L_0x0084;
        L_0x007b:
            r6 = r0.readShort();	 Catch:{ IOException -> 0x014b }
            r4[r5] = r6;	 Catch:{ IOException -> 0x014b }
            r5 = r5 + 1;
            goto L_0x0077;
        L_0x0084:
            return r4;
        L_0x0085:
            r4 = r1.numberOfComponents;	 Catch:{ IOException -> 0x014b }
            r4 = new android.media.ExifInterface.Rational[r4];	 Catch:{ IOException -> 0x014b }
        L_0x008a:
            r6 = r1.numberOfComponents;	 Catch:{ IOException -> 0x014b }
            if (r5 >= r6) goto L_0x00a2;
        L_0x008e:
            r8 = r0.readUnsignedInt();	 Catch:{ IOException -> 0x014b }
            r10 = r0.readUnsignedInt();	 Catch:{ IOException -> 0x014b }
            r6 = new android.media.ExifInterface$Rational;	 Catch:{ IOException -> 0x014b }
            r12 = 0;
            r7 = r6;
            r7.<init>(r8, r10, r12);	 Catch:{ IOException -> 0x014b }
            r4[r5] = r6;	 Catch:{ IOException -> 0x014b }
            r5 = r5 + 1;
            goto L_0x008a;
        L_0x00a2:
            return r4;
        L_0x00a3:
            r4 = r1.numberOfComponents;	 Catch:{ IOException -> 0x014b }
            r4 = new long[r4];	 Catch:{ IOException -> 0x014b }
        L_0x00a8:
            r6 = r1.numberOfComponents;	 Catch:{ IOException -> 0x014b }
            if (r5 >= r6) goto L_0x00b5;
        L_0x00ac:
            r6 = r0.readUnsignedInt();	 Catch:{ IOException -> 0x014b }
            r4[r5] = r6;	 Catch:{ IOException -> 0x014b }
            r5 = r5 + 1;
            goto L_0x00a8;
        L_0x00b5:
            return r4;
        L_0x00b6:
            r4 = r1.numberOfComponents;	 Catch:{ IOException -> 0x014b }
            r4 = new int[r4];	 Catch:{ IOException -> 0x014b }
        L_0x00bb:
            r6 = r1.numberOfComponents;	 Catch:{ IOException -> 0x014b }
            if (r5 >= r6) goto L_0x00c8;
        L_0x00bf:
            r6 = r0.readUnsignedShort();	 Catch:{ IOException -> 0x014b }
            r4[r5] = r6;	 Catch:{ IOException -> 0x014b }
            r5 = r5 + 1;
            goto L_0x00bb;
        L_0x00c8:
            return r4;
        L_0x00c9:
            r4 = 0;
            r6 = r1.numberOfComponents;	 Catch:{ IOException -> 0x014b }
            r7 = android.media.ExifInterface.EXIF_ASCII_PREFIX;	 Catch:{ IOException -> 0x014b }
            r7 = r7.length;	 Catch:{ IOException -> 0x014b }
            if (r6 < r7) goto L_0x00f5;
        L_0x00d3:
            r6 = 1;
        L_0x00d5:
            r7 = android.media.ExifInterface.EXIF_ASCII_PREFIX;	 Catch:{ IOException -> 0x014b }
            r7 = r7.length;	 Catch:{ IOException -> 0x014b }
            if (r5 >= r7) goto L_0x00ed;
        L_0x00dc:
            r7 = r1.bytes;	 Catch:{ IOException -> 0x014b }
            r7 = r7[r5];	 Catch:{ IOException -> 0x014b }
            r8 = android.media.ExifInterface.EXIF_ASCII_PREFIX;	 Catch:{ IOException -> 0x014b }
            r8 = r8[r5];	 Catch:{ IOException -> 0x014b }
            if (r7 == r8) goto L_0x00ea;
        L_0x00e8:
            r6 = 0;
            goto L_0x00ed;
        L_0x00ea:
            r5 = r5 + 1;
            goto L_0x00d5;
        L_0x00ed:
            if (r6 == 0) goto L_0x00f5;
        L_0x00ef:
            r5 = android.media.ExifInterface.EXIF_ASCII_PREFIX;	 Catch:{ IOException -> 0x014b }
            r5 = r5.length;	 Catch:{ IOException -> 0x014b }
            r4 = r5;
        L_0x00f5:
            r5 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x014b }
            r5.<init>();	 Catch:{ IOException -> 0x014b }
        L_0x00fa:
            r6 = r1.numberOfComponents;	 Catch:{ IOException -> 0x014b }
            if (r4 >= r6) goto L_0x0117;
        L_0x00fe:
            r6 = r1.bytes;	 Catch:{ IOException -> 0x014b }
            r6 = r6[r4];	 Catch:{ IOException -> 0x014b }
            if (r6 != 0) goto L_0x0105;
        L_0x0104:
            goto L_0x0117;
        L_0x0105:
            r7 = 32;
            if (r6 < r7) goto L_0x010e;
        L_0x0109:
            r7 = (char) r6;	 Catch:{ IOException -> 0x014b }
            r5.append(r7);	 Catch:{ IOException -> 0x014b }
            goto L_0x0113;
        L_0x010e:
            r7 = 63;
            r5.append(r7);	 Catch:{ IOException -> 0x014b }
            r4 = r4 + 1;
            goto L_0x00fa;
        L_0x0117:
            r2 = r5.toString();	 Catch:{ IOException -> 0x014b }
            return r2;
        L_0x011c:
            r4 = r1.bytes;	 Catch:{ IOException -> 0x014b }
            r4 = r4.length;	 Catch:{ IOException -> 0x014b }
            r6 = 1;
            if (r4 != r6) goto L_0x013f;
        L_0x0122:
            r4 = r1.bytes;	 Catch:{ IOException -> 0x014b }
            r4 = r4[r5];	 Catch:{ IOException -> 0x014b }
            if (r4 < 0) goto L_0x013f;
        L_0x0128:
            r4 = r1.bytes;	 Catch:{ IOException -> 0x014b }
            r4 = r4[r5];	 Catch:{ IOException -> 0x014b }
            if (r4 > r6) goto L_0x013f;
        L_0x012e:
            r4 = new java.lang.String;	 Catch:{ IOException -> 0x014b }
            r6 = new char[r6];	 Catch:{ IOException -> 0x014b }
            r7 = r1.bytes;	 Catch:{ IOException -> 0x014b }
            r7 = r7[r5];	 Catch:{ IOException -> 0x014b }
            r7 = r7 + 48;
            r7 = (char) r7;	 Catch:{ IOException -> 0x014b }
            r6[r5] = r7;	 Catch:{ IOException -> 0x014b }
            r4.<init>(r6);	 Catch:{ IOException -> 0x014b }
            return r4;
        L_0x013f:
            r4 = new java.lang.String;	 Catch:{ IOException -> 0x014b }
            r5 = r1.bytes;	 Catch:{ IOException -> 0x014b }
            r6 = android.media.ExifInterface.ASCII;	 Catch:{ IOException -> 0x014b }
            r4.<init>(r5, r6);	 Catch:{ IOException -> 0x014b }
            return r4;
        L_0x014b:
            r0 = move-exception;
            goto L_0x0150;
        L_0x014d:
            r0 = move-exception;
            r3 = r18;
        L_0x0150:
            r4 = "ExifInterface";
            r5 = "IOException occurred during reading a value";
            android.util.Log.w(r4, r5, r0);
            return r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.ExifInterface$ExifAttribute.getValue(java.nio.ByteOrder):java.lang.Object");
        }

        public double getDoubleValue(ByteOrder byteOrder) {
            Object value = getValue(byteOrder);
            if (value == null) {
                throw new NumberFormatException("NULL can't be converted to a double value");
            } else if (value instanceof String) {
                return Double.parseDouble((String) value);
            } else {
                String str = "There are more than one component";
                if (value instanceof long[]) {
                    long[] array = (long[]) value;
                    if (array.length == 1) {
                        return (double) array[0];
                    }
                    throw new NumberFormatException(str);
                } else if (value instanceof int[]) {
                    int[] array2 = (int[]) value;
                    if (array2.length == 1) {
                        return (double) array2[0];
                    }
                    throw new NumberFormatException(str);
                } else if (value instanceof double[]) {
                    double[] array3 = (double[]) value;
                    if (array3.length == 1) {
                        return array3[0];
                    }
                    throw new NumberFormatException(str);
                } else if (value instanceof Rational[]) {
                    Rational[] array4 = (Rational[]) value;
                    if (array4.length == 1) {
                        return array4[0].calculate();
                    }
                    throw new NumberFormatException(str);
                } else {
                    throw new NumberFormatException("Couldn't find a double value");
                }
            }
        }

        public int getIntValue(ByteOrder byteOrder) {
            Object value = getValue(byteOrder);
            if (value == null) {
                throw new NumberFormatException("NULL can't be converted to a integer value");
            } else if (value instanceof String) {
                return Integer.parseInt((String) value);
            } else {
                String str = "There are more than one component";
                if (value instanceof long[]) {
                    long[] array = (long[]) value;
                    if (array.length == 1) {
                        return (int) array[0];
                    }
                    throw new NumberFormatException(str);
                } else if (value instanceof int[]) {
                    int[] array2 = (int[]) value;
                    if (array2.length == 1) {
                        return array2[0];
                    }
                    throw new NumberFormatException(str);
                } else {
                    throw new NumberFormatException("Couldn't find a integer value");
                }
            }
        }

        public String getStringValue(ByteOrder byteOrder) {
            Object value = getValue(byteOrder);
            if (value == null) {
                return null;
            }
            if (value instanceof String) {
                return (String) value;
            }
            StringBuilder stringBuilder = new StringBuilder();
            String str = ",";
            int i;
            if (value instanceof long[]) {
                long[] array = (long[]) value;
                for (i = 0; i < array.length; i++) {
                    stringBuilder.append(array[i]);
                    if (i + 1 != array.length) {
                        stringBuilder.append(str);
                    }
                }
                return stringBuilder.toString();
            } else if (value instanceof int[]) {
                int[] array2 = (int[]) value;
                for (i = 0; i < array2.length; i++) {
                    stringBuilder.append(array2[i]);
                    if (i + 1 != array2.length) {
                        stringBuilder.append(str);
                    }
                }
                return stringBuilder.toString();
            } else if (value instanceof double[]) {
                double[] array3 = (double[]) value;
                for (i = 0; i < array3.length; i++) {
                    stringBuilder.append(array3[i]);
                    if (i + 1 != array3.length) {
                        stringBuilder.append(str);
                    }
                }
                return stringBuilder.toString();
            } else if (!(value instanceof Rational[])) {
                return null;
            } else {
                Rational[] array4 = (Rational[]) value;
                for (i = 0; i < array4.length; i++) {
                    stringBuilder.append(array4[i].numerator);
                    stringBuilder.append('/');
                    stringBuilder.append(array4[i].denominator);
                    if (i + 1 != array4.length) {
                        stringBuilder.append(str);
                    }
                }
                return stringBuilder.toString();
            }
        }

        public int size() {
            return ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[this.format] * this.numberOfComponents;
        }
    }

    private static class ExifTag {
        public final String name;
        public final int number;
        public final int primaryFormat;
        public final int secondaryFormat;

        /* synthetic */ ExifTag(String x0, int x1, int x2, int x3, AnonymousClass1 x4) {
            this(x0, x1, x2, x3);
        }

        private ExifTag(String name, int number, int format) {
            this.name = name;
            this.number = number;
            this.primaryFormat = format;
            this.secondaryFormat = -1;
        }

        private ExifTag(String name, int number, int primaryFormat, int secondaryFormat) {
            this.name = name;
            this.number = number;
            this.primaryFormat = primaryFormat;
            this.secondaryFormat = secondaryFormat;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface IfdType {
    }

    private static class Rational {
        public final long denominator;
        public final long numerator;

        /* synthetic */ Rational(long x0, long x1, AnonymousClass1 x2) {
            this(x0, x1);
        }

        private Rational(long numerator, long denominator) {
            if (denominator == 0) {
                this.numerator = 0;
                this.denominator = 1;
                return;
            }
            this.numerator = numerator;
            this.denominator = denominator;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.numerator);
            stringBuilder.append("/");
            stringBuilder.append(this.denominator);
            return stringBuilder.toString();
        }

        public double calculate() {
            return ((double) this.numerator) / ((double) this.denominator);
        }
    }

    static {
        r3 = new ExifTag[10][];
        ExifTag[] exifTagArr = IFD_TIFF_TAGS;
        r3[0] = exifTagArr;
        r3[1] = IFD_EXIF_TAGS;
        r3[2] = IFD_GPS_TAGS;
        r3[3] = IFD_INTEROPERABILITY_TAGS;
        r3[4] = IFD_THUMBNAIL_TAGS;
        r3[5] = exifTagArr;
        r3[6] = ORF_MAKER_NOTE_TAGS;
        r3[7] = ORF_CAMERA_SETTINGS_TAGS;
        r3[8] = ORF_IMAGE_PROCESSING_TAGS;
        r3[9] = PEF_TAGS;
        EXIF_TAGS = r3;
        ExifTag[][] exifTagArr2 = EXIF_TAGS;
        sExifTagMapsForReading = new HashMap[exifTagArr2.length];
        sExifTagMapsForWriting = new HashMap[exifTagArr2.length];
        sFormatter.setTimeZone(TimeZone.getTimeZone(Time.TIMEZONE_UTC));
        sFormatterTz.setTimeZone(TimeZone.getTimeZone(Time.TIMEZONE_UTC));
        for (int ifdType = 0; ifdType < EXIF_TAGS.length; ifdType++) {
            sExifTagMapsForReading[ifdType] = new HashMap();
            sExifTagMapsForWriting[ifdType] = new HashMap();
            for (ExifTag tag : EXIF_TAGS[ifdType]) {
                sExifTagMapsForReading[ifdType].put(Integer.valueOf(tag.number), tag);
                sExifTagMapsForWriting[ifdType].put(tag.name, tag);
            }
        }
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[0].number), Integer.valueOf(5));
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[1].number), Integer.valueOf(1));
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[2].number), Integer.valueOf(2));
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[3].number), Integer.valueOf(3));
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[4].number), Integer.valueOf(7));
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[5].number), Integer.valueOf(8));
    }

    public ExifInterface(File file) throws IOException {
        ExifTag[][] exifTagArr = EXIF_TAGS;
        this.mAttributes = new HashMap[exifTagArr.length];
        this.mHandledIfdOffsets = new HashSet(exifTagArr.length);
        if (file != null) {
            initForFilename(file.getAbsolutePath());
            return;
        }
        throw new NullPointerException("file cannot be null");
    }

    public ExifInterface(String filename) throws IOException {
        ExifTag[][] exifTagArr = EXIF_TAGS;
        this.mAttributes = new HashMap[exifTagArr.length];
        this.mHandledIfdOffsets = new HashSet(exifTagArr.length);
        if (filename != null) {
            initForFilename(filename);
            return;
        }
        throw new NullPointerException("filename cannot be null");
    }

    public ExifInterface(FileDescriptor fileDescriptor) throws IOException {
        ExifTag[][] exifTagArr = EXIF_TAGS;
        this.mAttributes = new HashMap[exifTagArr.length];
        this.mHandledIfdOffsets = new HashSet(exifTagArr.length);
        if (fileDescriptor != null) {
            this.mAssetInputStream = null;
            this.mFilename = null;
            boolean isFdOwner = false;
            if (isSeekableFD(fileDescriptor)) {
                this.mSeekableFileDescriptor = fileDescriptor;
                try {
                    fileDescriptor = Os.dup(fileDescriptor);
                    isFdOwner = true;
                } catch (ErrnoException e) {
                    throw e.rethrowAsIOException();
                }
            }
            this.mSeekableFileDescriptor = null;
            this.mIsInputStream = false;
            FileInputStream in = null;
            try {
                in = new FileInputStream(fileDescriptor, isFdOwner);
                loadAttributes(in);
            } finally {
                IoUtils.closeQuietly(in);
            }
        } else {
            throw new NullPointerException("fileDescriptor cannot be null");
        }
    }

    public ExifInterface(InputStream inputStream) throws IOException {
        ExifTag[][] exifTagArr = EXIF_TAGS;
        this.mAttributes = new HashMap[exifTagArr.length];
        this.mHandledIfdOffsets = new HashSet(exifTagArr.length);
        if (inputStream != null) {
            this.mFilename = null;
            if (inputStream instanceof AssetInputStream) {
                this.mAssetInputStream = (AssetInputStream) inputStream;
                this.mSeekableFileDescriptor = null;
            } else if ((inputStream instanceof FileInputStream) && isSeekableFD(((FileInputStream) inputStream).getFD())) {
                this.mAssetInputStream = null;
                this.mSeekableFileDescriptor = ((FileInputStream) inputStream).getFD();
            } else {
                this.mAssetInputStream = null;
                this.mSeekableFileDescriptor = null;
            }
            this.mIsInputStream = true;
            loadAttributes(inputStream);
            return;
        }
        throw new NullPointerException("inputStream cannot be null");
    }

    private ExifAttribute getExifAttribute(String tag) {
        if (tag != null) {
            for (int i = 0; i < EXIF_TAGS.length; i++) {
                Object value = this.mAttributes[i].get(tag);
                if (value != null) {
                    return (ExifAttribute) value;
                }
            }
            return null;
        }
        throw new NullPointerException("tag shouldn't be null");
    }

    public String getAttribute(String tag) {
        if (tag != null) {
            ExifAttribute attribute = getExifAttribute(tag);
            String str = null;
            if (attribute == null) {
                return null;
            }
            if (!sTagSetForCompatibility.contains(tag)) {
                return attribute.getStringValue(this.mExifByteOrder);
            }
            if (!tag.equals(TAG_GPS_TIMESTAMP)) {
                try {
                    str = Double.toString(attribute.getDoubleValue(this.mExifByteOrder));
                    return str;
                } catch (NumberFormatException e) {
                    return str;
                }
            } else if (attribute.format != 5 && attribute.format != 10) {
                return null;
            } else {
                if (((Rational[]) attribute.getValue(this.mExifByteOrder)).length != 3) {
                    return null;
                }
                return String.format("%02d:%02d:%02d", new Object[]{Integer.valueOf((int) (((float) ((Rational[]) attribute.getValue(this.mExifByteOrder))[0].numerator) / ((float) ((Rational[]) attribute.getValue(this.mExifByteOrder))[0].denominator))), Integer.valueOf((int) (((float) ((Rational[]) attribute.getValue(this.mExifByteOrder))[1].numerator) / ((float) ((Rational[]) attribute.getValue(this.mExifByteOrder))[1].denominator))), Integer.valueOf((int) (((float) ((Rational[]) attribute.getValue(this.mExifByteOrder))[2].numerator) / ((float) ((Rational[]) attribute.getValue(this.mExifByteOrder))[2].denominator)))});
            }
        }
        throw new NullPointerException("tag shouldn't be null");
    }

    public int getAttributeInt(String tag, int defaultValue) {
        if (tag != null) {
            ExifAttribute exifAttribute = getExifAttribute(tag);
            if (exifAttribute == null) {
                return defaultValue;
            }
            try {
                return exifAttribute.getIntValue(this.mExifByteOrder);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        throw new NullPointerException("tag shouldn't be null");
    }

    public double getAttributeDouble(String tag, double defaultValue) {
        if (tag != null) {
            ExifAttribute exifAttribute = getExifAttribute(tag);
            if (exifAttribute == null) {
                return defaultValue;
            }
            try {
                return exifAttribute.getDoubleValue(this.mExifByteOrder);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        throw new NullPointerException("tag shouldn't be null");
    }

    public void setAttribute(String tag, String value) {
        ExifInterface exifInterface = this;
        String str = tag;
        String value2 = value;
        if (str != null) {
            StringBuilder stringBuilder;
            int i = 2;
            String str2 = TAG;
            int i2 = 1;
            if (value2 != null && sTagSetForCompatibility.contains(str)) {
                String str3 = " : ";
                String str4 = "Invalid value for ";
                if (str.equals(TAG_GPS_TIMESTAMP)) {
                    Matcher m = sGpsTimestampPattern.matcher(value2);
                    if (m.find()) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(Integer.parseInt(m.group(1)));
                        str4 = "/1,";
                        stringBuilder2.append(str4);
                        stringBuilder2.append(Integer.parseInt(m.group(2)));
                        stringBuilder2.append(str4);
                        stringBuilder2.append(Integer.parseInt(m.group(3)));
                        stringBuilder2.append("/1");
                        value2 = stringBuilder2.toString();
                    } else {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str4);
                        stringBuilder.append(str);
                        stringBuilder.append(str3);
                        stringBuilder.append(value2);
                        Log.w(str2, stringBuilder.toString());
                        return;
                    }
                }
                try {
                    double doubleValue = Double.parseDouble(value);
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append((long) (10000.0d * doubleValue));
                    stringBuilder3.append("/10000");
                    value2 = stringBuilder3.toString();
                } catch (NumberFormatException e) {
                    StringBuilder stringBuilder4 = new StringBuilder();
                    stringBuilder4.append(str4);
                    stringBuilder4.append(str);
                    stringBuilder4.append(str3);
                    stringBuilder4.append(value2);
                    Log.w(str2, stringBuilder4.toString());
                    return;
                }
            }
            int i3 = 0;
            while (i3 < EXIF_TAGS.length) {
                int i4;
                int i5;
                if (i3 != 4 || exifInterface.mHasThumbnail) {
                    Object obj = sExifTagMapsForWriting[i3].get(str);
                    Object obj2;
                    if (obj != null) {
                        if (value2 != null) {
                            int dataFormat;
                            ExifTag exifTag = (ExifTag) obj;
                            Pair<Integer, Integer> guess = guessDataFormat(value2);
                            if (exifTag.primaryFormat == ((Integer) guess.first).intValue() || exifTag.primaryFormat == ((Integer) guess.second).intValue()) {
                                dataFormat = exifTag.primaryFormat;
                            } else if (exifTag.secondaryFormat != -1 && (exifTag.secondaryFormat == ((Integer) guess.first).intValue() || exifTag.secondaryFormat == ((Integer) guess.second).intValue())) {
                                dataFormat = exifTag.secondaryFormat;
                            } else if (exifTag.primaryFormat == i2 || exifTag.primaryFormat == 7 || exifTag.primaryFormat == i) {
                                dataFormat = exifTag.primaryFormat;
                            } else if (DEBUG) {
                                String str5;
                                StringBuilder stringBuilder5 = new StringBuilder();
                                stringBuilder5.append("Given tag (");
                                stringBuilder5.append(str);
                                stringBuilder5.append(") value didn't match with one of expected formats: ");
                                stringBuilder5.append(IFD_FORMAT_NAMES[exifTag.primaryFormat]);
                                String str6 = "";
                                String str7 = ", ";
                                if (exifTag.secondaryFormat == -1) {
                                    str5 = str6;
                                } else {
                                    StringBuilder stringBuilder6 = new StringBuilder();
                                    stringBuilder6.append(str7);
                                    stringBuilder6.append(IFD_FORMAT_NAMES[exifTag.secondaryFormat]);
                                    str5 = stringBuilder6.toString();
                                }
                                stringBuilder5.append(str5);
                                stringBuilder5.append(" (guess: ");
                                stringBuilder5.append(IFD_FORMAT_NAMES[((Integer) guess.first).intValue()]);
                                if (((Integer) guess.second).intValue() != -1) {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(str7);
                                    stringBuilder.append(IFD_FORMAT_NAMES[((Integer) guess.second).intValue()]);
                                    str6 = stringBuilder.toString();
                                }
                                stringBuilder5.append(str6);
                                stringBuilder5.append(")");
                                Log.d(str2, stringBuilder5.toString());
                                i4 = i2;
                                i5 = i3;
                            } else {
                                i4 = i2;
                                i5 = i3;
                            }
                            i = 0;
                            String str8 = "/";
                            String str9 = ",";
                            ExifTag exifTag2;
                            String[] values;
                            int[] intArray;
                            String[] numbers;
                            switch (dataFormat) {
                                case 1:
                                    i4 = i2;
                                    i5 = i3;
                                    obj2 = obj;
                                    exifTag2 = exifTag;
                                    exifInterface.mAttributes[i5].put(str, ExifAttribute.createByte(value2));
                                    break;
                                case 2:
                                case 7:
                                    i4 = i2;
                                    i5 = i3;
                                    obj2 = obj;
                                    exifTag2 = exifTag;
                                    exifInterface.mAttributes[i5].put(str, ExifAttribute.createString(value2));
                                    break;
                                case 3:
                                    i4 = i2;
                                    i5 = i3;
                                    obj2 = obj;
                                    exifTag2 = exifTag;
                                    values = value2.split(str9);
                                    intArray = new int[values.length];
                                    for (i3 = 0; i3 < values.length; i3++) {
                                        intArray[i3] = Integer.parseInt(values[i3]);
                                    }
                                    exifInterface.mAttributes[i5].put(str, ExifAttribute.createUShort(intArray, exifInterface.mExifByteOrder));
                                    break;
                                case 4:
                                    i4 = i2;
                                    i5 = i3;
                                    obj2 = obj;
                                    exifTag2 = exifTag;
                                    values = value2.split(str9);
                                    long[] longArray = new long[values.length];
                                    for (i3 = 0; i3 < values.length; i3++) {
                                        longArray[i3] = Long.parseLong(values[i3]);
                                    }
                                    exifInterface.mAttributes[i5].put(str, ExifAttribute.createULong(longArray, exifInterface.mExifByteOrder));
                                    break;
                                case 5:
                                    i5 = i3;
                                    values = value2.split(str9);
                                    Rational[] rationalArray = new Rational[values.length];
                                    i3 = 0;
                                    while (i3 < values.length) {
                                        numbers = values[i3].split(str8);
                                        obj2 = obj;
                                        exifTag2 = exifTag;
                                        rationalArray[i3] = new Rational((long) Double.parseDouble(numbers[0]), (long) Double.parseDouble(numbers[1]), null);
                                        i3++;
                                        obj = obj2;
                                        exifTag = exifTag2;
                                    }
                                    exifTag2 = exifTag;
                                    i4 = 1;
                                    exifInterface.mAttributes[i5].put(str, ExifAttribute.createURational(rationalArray, exifInterface.mExifByteOrder));
                                    break;
                                case 9:
                                    i5 = i3;
                                    values = value2.split(str9);
                                    intArray = new int[values.length];
                                    for (i3 = 0; i3 < values.length; i3++) {
                                        intArray[i3] = Integer.parseInt(values[i3]);
                                    }
                                    exifInterface.mAttributes[i5].put(str, ExifAttribute.createSLong(intArray, exifInterface.mExifByteOrder));
                                    i4 = 1;
                                    break;
                                case 10:
                                    numbers = value2.split(str9);
                                    Rational[] rationalArray2 = new Rational[numbers.length];
                                    int j = 0;
                                    while (j < numbers.length) {
                                        String[] numbers2 = numbers[j].split(str8);
                                        i5 = i3;
                                        rationalArray2[j] = new Rational((long) Double.parseDouble(numbers2[i]), (long) Double.parseDouble(numbers2[i2]), null);
                                        j++;
                                        i = 0;
                                        i2 = 1;
                                        i3 = i5;
                                    }
                                    i5 = i3;
                                    exifInterface = this;
                                    exifInterface.mAttributes[i5].put(str, ExifAttribute.createSRational(rationalArray2, exifInterface.mExifByteOrder));
                                    i4 = 1;
                                    break;
                                case 12:
                                    values = value2.split(str9);
                                    double[] doubleArray = new double[values.length];
                                    for (int j2 = 0; j2 < values.length; j2++) {
                                        doubleArray[j2] = Double.parseDouble(values[j2]);
                                    }
                                    exifInterface.mAttributes[i3].put(str, ExifAttribute.createDouble(doubleArray, exifInterface.mExifByteOrder));
                                    i4 = i2;
                                    i5 = i3;
                                    break;
                                default:
                                    i4 = i2;
                                    i5 = i3;
                                    if (!DEBUG) {
                                        break;
                                    }
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("Data format isn't one of expected formats: ");
                                    stringBuilder.append(dataFormat);
                                    Log.d(str2, stringBuilder.toString());
                                    break;
                            }
                        }
                        exifInterface.mAttributes[i3].remove(str);
                        i4 = i2;
                        i5 = i3;
                    } else {
                        i4 = i2;
                        i5 = i3;
                        obj2 = obj;
                    }
                } else {
                    i4 = i2;
                    i5 = i3;
                }
                i3 = i5 + 1;
                i2 = i4;
                i = 2;
            }
            return;
        }
        throw new NullPointerException("tag shouldn't be null");
    }

    private boolean updateAttribute(String tag, ExifAttribute value) {
        boolean updated = false;
        for (int i = 0; i < EXIF_TAGS.length; i++) {
            if (this.mAttributes[i].containsKey(tag)) {
                this.mAttributes[i].put(tag, value);
                updated = true;
            }
        }
        return updated;
    }

    private void removeAttribute(String tag) {
        for (int i = 0; i < EXIF_TAGS.length; i++) {
            this.mAttributes[i].remove(tag);
        }
    }

    /* JADX WARNING: Failed to extract finally block: empty outs */
    private void loadAttributes(java.io.InputStream r5) throws java.io.IOException {
        /*
        r4 = this;
        if (r5 == 0) goto L_0x007b;
    L_0x0002:
        r0 = 0;
        r1 = r0;
    L_0x0004:
        r2 = EXIF_TAGS;	 Catch:{ IOException | OutOfMemoryError -> 0x005c, IOException | OutOfMemoryError -> 0x005c }
        r2 = r2.length;	 Catch:{ IOException | OutOfMemoryError -> 0x005c, IOException | OutOfMemoryError -> 0x005c }
        if (r1 >= r2) goto L_0x0015;
    L_0x0009:
        r2 = r4.mAttributes;	 Catch:{ IOException | OutOfMemoryError -> 0x005c, IOException | OutOfMemoryError -> 0x005c }
        r3 = new java.util.HashMap;	 Catch:{ IOException | OutOfMemoryError -> 0x005c, IOException | OutOfMemoryError -> 0x005c }
        r3.<init>();	 Catch:{ IOException | OutOfMemoryError -> 0x005c, IOException | OutOfMemoryError -> 0x005c }
        r2[r1] = r3;	 Catch:{ IOException | OutOfMemoryError -> 0x005c, IOException | OutOfMemoryError -> 0x005c }
        r1 = r1 + 1;
        goto L_0x0004;
    L_0x0015:
        r1 = new java.io.BufferedInputStream;	 Catch:{ IOException | OutOfMemoryError -> 0x005c, IOException | OutOfMemoryError -> 0x005c }
        r2 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
        r1.<init>(r5, r2);	 Catch:{ IOException | OutOfMemoryError -> 0x005c, IOException | OutOfMemoryError -> 0x005c }
        r5 = r1;
        r1 = r5;
        r1 = (java.io.BufferedInputStream) r1;	 Catch:{ IOException | OutOfMemoryError -> 0x005c, IOException | OutOfMemoryError -> 0x005c }
        r1 = r4.getMimeType(r1);	 Catch:{ IOException | OutOfMemoryError -> 0x005c, IOException | OutOfMemoryError -> 0x005c }
        r4.mMimeType = r1;	 Catch:{ IOException | OutOfMemoryError -> 0x005c, IOException | OutOfMemoryError -> 0x005c }
        r1 = new android.media.ExifInterface$ByteOrderedDataInputStream;	 Catch:{ IOException | OutOfMemoryError -> 0x005c, IOException | OutOfMemoryError -> 0x005c }
        r1.<init>(r5);	 Catch:{ IOException | OutOfMemoryError -> 0x005c, IOException | OutOfMemoryError -> 0x005c }
        r2 = r4.mMimeType;	 Catch:{ IOException | OutOfMemoryError -> 0x005c, IOException | OutOfMemoryError -> 0x005c }
        switch(r2) {
            case 0: goto L_0x0045;
            case 1: goto L_0x0045;
            case 2: goto L_0x0045;
            case 3: goto L_0x0045;
            case 4: goto L_0x0041;
            case 5: goto L_0x0045;
            case 6: goto L_0x0045;
            case 7: goto L_0x003d;
            case 8: goto L_0x0045;
            case 9: goto L_0x0039;
            case 10: goto L_0x0035;
            case 11: goto L_0x0045;
            case 12: goto L_0x0031;
            default: goto L_0x0030;
        };	 Catch:{ IOException | OutOfMemoryError -> 0x005c, IOException | OutOfMemoryError -> 0x005c }
    L_0x0030:
        goto L_0x0049;
    L_0x0031:
        r4.getHeifAttributes(r1);	 Catch:{ IOException | OutOfMemoryError -> 0x005c, IOException | OutOfMemoryError -> 0x005c }
        goto L_0x0049;
    L_0x0035:
        r4.getRw2Attributes(r1);	 Catch:{ IOException | OutOfMemoryError -> 0x005c, IOException | OutOfMemoryError -> 0x005c }
        goto L_0x0049;
    L_0x0039:
        r4.getRafAttributes(r1);	 Catch:{ IOException | OutOfMemoryError -> 0x005c, IOException | OutOfMemoryError -> 0x005c }
        goto L_0x0049;
    L_0x003d:
        r4.getOrfAttributes(r1);	 Catch:{ IOException | OutOfMemoryError -> 0x005c, IOException | OutOfMemoryError -> 0x005c }
        goto L_0x0049;
    L_0x0041:
        r4.getJpegAttributes(r1, r0, r0);	 Catch:{ IOException | OutOfMemoryError -> 0x005c, IOException | OutOfMemoryError -> 0x005c }
        goto L_0x0049;
    L_0x0045:
        r4.getRawAttributes(r1);	 Catch:{ IOException | OutOfMemoryError -> 0x005c, IOException | OutOfMemoryError -> 0x005c }
    L_0x0049:
        r4.setThumbnailData(r1);	 Catch:{ IOException | OutOfMemoryError -> 0x005c, IOException | OutOfMemoryError -> 0x005c }
        r2 = 1;
        r4.mIsSupportedFile = r2;	 Catch:{ IOException | OutOfMemoryError -> 0x005c, IOException | OutOfMemoryError -> 0x005c }
        r4.addDefaultValuesForCompatibility();
        r0 = DEBUG;
        if (r0 == 0) goto L_0x006f;
    L_0x0056:
        r4.printAttributes();
        goto L_0x006f;
    L_0x005a:
        r0 = move-exception;
        goto L_0x0070;
    L_0x005c:
        r1 = move-exception;
        r4.mIsSupportedFile = r0;	 Catch:{ all -> 0x005a }
        r0 = "ExifInterface";
        r2 = "Invalid image: ExifInterface got an unsupported image format file(ExifInterface supports JPEG and some RAW image formats only) or a corrupted JPEG file to ExifInterface.";
        android.util.Log.w(r0, r2, r1);	 Catch:{ all -> 0x005a }
        r4.addDefaultValuesForCompatibility();
        r0 = DEBUG;
        if (r0 == 0) goto L_0x006f;
    L_0x006e:
        goto L_0x0056;
    L_0x006f:
        return;
    L_0x0070:
        r4.addDefaultValuesForCompatibility();
        r1 = DEBUG;
        if (r1 == 0) goto L_0x007a;
    L_0x0077:
        r4.printAttributes();
    L_0x007a:
        throw r0;
    L_0x007b:
        r0 = new java.lang.NullPointerException;
        r1 = "inputstream shouldn't be null";
        r0.<init>(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.ExifInterface.loadAttributes(java.io.InputStream):void");
    }

    private static boolean isSeekableFD(FileDescriptor fd) throws IOException {
        try {
            Os.lseek(fd, 0, OsConstants.SEEK_CUR);
            return true;
        } catch (ErrnoException e) {
            return false;
        }
    }

    private void printAttributes() {
        for (int i = 0; i < this.mAttributes.length; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The size of tag group[");
            stringBuilder.append(i);
            stringBuilder.append("]: ");
            stringBuilder.append(this.mAttributes[i].size());
            String stringBuilder2 = stringBuilder.toString();
            String str = TAG;
            Log.d(str, stringBuilder2);
            for (Entry entry : this.mAttributes[i].entrySet()) {
                ExifAttribute tagValue = (ExifAttribute) entry.getValue();
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("tagName: ");
                stringBuilder3.append(entry.getKey());
                stringBuilder3.append(", tagType: ");
                stringBuilder3.append(tagValue.toString());
                stringBuilder3.append(", tagValue: '");
                stringBuilder3.append(tagValue.getStringValue(this.mExifByteOrder));
                stringBuilder3.append("'");
                Log.d(str, stringBuilder3.toString());
            }
        }
    }

    public void saveAttributes() throws IOException {
        if (!this.mIsSupportedFile || this.mMimeType != 4) {
            throw new IOException("ExifInterface only supports saving attributes on JPEG formats.");
        } else if (this.mIsInputStream || (this.mSeekableFileDescriptor == null && this.mFilename == null)) {
            throw new IOException("ExifInterface does not support saving attributes for the current input.");
        } else {
            this.mModified = true;
            this.mThumbnailBytes = getThumbnail();
            FileInputStream in = null;
            FileOutputStream out = null;
            File tempFile = null;
            try {
                if (this.mFilename != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(this.mFilename);
                    stringBuilder.append(".tmp");
                    tempFile = new File(stringBuilder.toString());
                    if (!new File(this.mFilename).renameTo(tempFile)) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Could'nt rename to ");
                        stringBuilder2.append(tempFile.getAbsolutePath());
                        throw new IOException(stringBuilder2.toString());
                    }
                } else if (this.mSeekableFileDescriptor != null) {
                    tempFile = File.createTempFile("temp", "jpg");
                    Os.lseek(this.mSeekableFileDescriptor, 0, OsConstants.SEEK_SET);
                    in = new FileInputStream(this.mSeekableFileDescriptor);
                    out = new FileOutputStream(tempFile);
                    Streams.copy(in, out);
                }
                IoUtils.closeQuietly(in);
                IoUtils.closeQuietly(out);
                out = null;
                try {
                    in = new FileInputStream(tempFile);
                    if (this.mFilename != null) {
                        out = new FileOutputStream(this.mFilename);
                    } else if (this.mSeekableFileDescriptor != null) {
                        Os.lseek(this.mSeekableFileDescriptor, 0, OsConstants.SEEK_SET);
                        out = new FileOutputStream(this.mSeekableFileDescriptor);
                    }
                    saveJpegAttributes(in, out);
                    IoUtils.closeQuietly(in);
                    IoUtils.closeQuietly(out);
                    tempFile.delete();
                    this.mThumbnailBytes = null;
                } catch (ErrnoException e) {
                    throw e.rethrowAsIOException();
                } catch (Throwable th) {
                    IoUtils.closeQuietly(null);
                    IoUtils.closeQuietly(null);
                    tempFile.delete();
                }
            } catch (ErrnoException e2) {
                throw e2.rethrowAsIOException();
            } catch (Throwable th2) {
                IoUtils.closeQuietly(null);
                IoUtils.closeQuietly(null);
            }
        }
    }

    public boolean hasThumbnail() {
        return this.mHasThumbnail;
    }

    public boolean hasAttribute(String tag) {
        return getExifAttribute(tag) != null;
    }

    public byte[] getThumbnail() {
        int i = this.mThumbnailCompression;
        if (i == 6 || i == 7) {
            return getThumbnailBytes();
        }
        return null;
    }

    public byte[] getThumbnailBytes() {
        String str = TAG;
        if (!this.mHasThumbnail) {
            return null;
        }
        byte[] bArr = this.mThumbnailBytes;
        if (bArr != null) {
            return bArr;
        }
        InputStream in = null;
        byte[] buffer;
        try {
            if (this.mAssetInputStream != null) {
                in = this.mAssetInputStream;
                if (in.markSupported()) {
                    in.reset();
                } else {
                    Log.d(str, "Cannot read thumbnail from inputstream without mark/reset support");
                    IoUtils.closeQuietly(in);
                    return null;
                }
            } else if (this.mFilename != null) {
                in = new FileInputStream(this.mFilename);
            } else if (this.mSeekableFileDescriptor != null) {
                FileDescriptor fileDescriptor = Os.dup(this.mSeekableFileDescriptor);
                Os.lseek(fileDescriptor, 0, OsConstants.SEEK_SET);
                in = new FileInputStream(fileDescriptor, true);
            }
            if (in != null) {
                String str2 = "Corrupted image";
                if (in.skip((long) this.mThumbnailOffset) == ((long) this.mThumbnailOffset)) {
                    buffer = new byte[this.mThumbnailLength];
                    if (in.read(buffer) == this.mThumbnailLength) {
                        this.mThumbnailBytes = buffer;
                        return buffer;
                    }
                    throw new IOException(str2);
                }
                throw new IOException(str2);
            }
            throw new FileNotFoundException();
        } catch (ErrnoException | IOException e) {
            buffer = e;
            Log.d(str, "Encountered exception while getting thumbnail", buffer);
            return null;
        } finally {
            IoUtils.closeQuietly(in);
        }
    }

    public Bitmap getThumbnailBitmap() {
        if (!this.mHasThumbnail) {
            return null;
        }
        if (this.mThumbnailBytes == null) {
            this.mThumbnailBytes = getThumbnailBytes();
        }
        int i = this.mThumbnailCompression;
        if (i == 6 || i == 7) {
            return BitmapFactory.decodeByteArray(this.mThumbnailBytes, 0, this.mThumbnailLength);
        }
        if (i == 1) {
            int[] rgbValues = new int[(this.mThumbnailBytes.length / 3)];
            for (int i2 = 0; i2 < rgbValues.length; i2++) {
                byte[] bArr = this.mThumbnailBytes;
                rgbValues[i2] = (((bArr[i2 * 3] << 16) + (byte) 0) + (bArr[(i2 * 3) + 1] << 8)) + bArr[(i2 * 3) + 2];
            }
            ExifAttribute imageLengthAttribute = (ExifAttribute) this.mAttributes[4].get(TAG_IMAGE_LENGTH);
            ExifAttribute imageWidthAttribute = (ExifAttribute) this.mAttributes[4].get(TAG_IMAGE_WIDTH);
            if (!(imageLengthAttribute == null || imageWidthAttribute == null)) {
                return Bitmap.createBitmap(rgbValues, imageWidthAttribute.getIntValue(this.mExifByteOrder), imageLengthAttribute.getIntValue(this.mExifByteOrder), Config.ARGB_8888);
            }
        }
        return null;
    }

    public boolean isThumbnailCompressed() {
        if (!this.mHasThumbnail) {
            return false;
        }
        int i = this.mThumbnailCompression;
        if (i == 6 || i == 7) {
            return true;
        }
        return false;
    }

    public long[] getThumbnailRange() {
        if (this.mModified) {
            throw new IllegalStateException("The underlying file has been modified since being parsed");
        } else if (!this.mHasThumbnail) {
            return null;
        } else {
            return new long[]{(long) this.mThumbnailOffset, (long) this.mThumbnailLength};
        }
    }

    public long[] getAttributeRange(String tag) {
        if (tag == null) {
            throw new NullPointerException("tag shouldn't be null");
        } else if (this.mModified) {
            throw new IllegalStateException("The underlying file has been modified since being parsed");
        } else {
            if (getExifAttribute(tag) == null) {
                return null;
            }
            return new long[]{getExifAttribute(tag).bytesOffset, (long) getExifAttribute(tag).bytes.length};
        }
    }

    public byte[] getAttributeBytes(String tag) {
        if (tag != null) {
            ExifAttribute attribute = getExifAttribute(tag);
            if (attribute != null) {
                return attribute.bytes;
            }
            return null;
        }
        throw new NullPointerException("tag shouldn't be null");
    }

    public boolean getLatLong(float[] output) {
        String latValue = getAttribute(TAG_GPS_LATITUDE);
        String latRef = getAttribute(TAG_GPS_LATITUDE_REF);
        String lngValue = getAttribute(TAG_GPS_LONGITUDE);
        String lngRef = getAttribute(TAG_GPS_LONGITUDE_REF);
        if (!(latValue == null || latRef == null || lngValue == null || lngRef == null)) {
            try {
                output[0] = convertRationalLatLonToFloat(latValue, latRef);
                output[1] = convertRationalLatLonToFloat(lngValue, lngRef);
                return true;
            } catch (IllegalArgumentException e) {
            }
        }
        return false;
    }

    public double getAltitude(double defaultValue) {
        double altitude = getAttributeDouble(TAG_GPS_ALTITUDE, -1.0d);
        int i = -1;
        int ref = getAttributeInt(TAG_GPS_ALTITUDE_REF, -1);
        if (altitude < 0.0d || ref < 0) {
            return defaultValue;
        }
        if (ref != 1) {
            i = 1;
        }
        return ((double) i) * altitude;
    }

    @UnsupportedAppUsage
    public long getDateTime() {
        return parseDateTime(getAttribute("DateTime"), getAttribute(TAG_SUBSEC_TIME), getAttribute(TAG_OFFSET_TIME));
    }

    public long getDateTimeDigitized() {
        return parseDateTime(getAttribute(TAG_DATETIME_DIGITIZED), getAttribute("SubSecTimeDigitized"), getAttribute(TAG_OFFSET_TIME_DIGITIZED));
    }

    @UnsupportedAppUsage
    public long getDateTimeOriginal() {
        return parseDateTime(getAttribute(TAG_DATETIME_ORIGINAL), getAttribute("SubSecTimeOriginal"), getAttribute(TAG_OFFSET_TIME_ORIGINAL));
    }

    private static long parseDateTime(String dateTimeString, String subSecs, String offsetString) {
        if (dateTimeString == null || !sNonZeroTimePattern.matcher(dateTimeString).matches()) {
            return -1;
        }
        try {
            Date datetime = sFormatter.parse(dateTimeString, new ParsePosition(0));
            if (offsetString != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(dateTimeString);
                stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                stringBuilder.append(offsetString);
                datetime = sFormatterTz.parse(stringBuilder.toString(), new ParsePosition(0));
            }
            if (datetime == null) {
                return -1;
            }
            long msecs = datetime.getTime();
            if (subSecs != null) {
                try {
                    long sub = Long.parseLong(subSecs);
                    while (sub > 1000) {
                        sub /= 10;
                    }
                    msecs += sub;
                } catch (NumberFormatException e) {
                }
            }
            return msecs;
        } catch (IllegalArgumentException e2) {
            return -1;
        }
    }

    @UnsupportedAppUsage
    public long getGpsDateTime() {
        String date = getAttribute(TAG_GPS_DATESTAMP);
        String time = getAttribute(TAG_GPS_TIMESTAMP);
        if (date == null || time == null || (!sNonZeroTimePattern.matcher(date).matches() && !sNonZeroTimePattern.matcher(time).matches())) {
            return -1;
        }
        String dateTimeString = new StringBuilder();
        dateTimeString.append(date);
        dateTimeString.append(' ');
        dateTimeString.append(time);
        try {
            Date datetime = sFormatter.parse(dateTimeString.toString(), new ParsePosition(0));
            if (datetime == null) {
                return -1;
            }
            return datetime.getTime();
        } catch (IllegalArgumentException e) {
            return -1;
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public static float convertRationalLatLonToFloat(String rationalString, String ref) {
        String[] pair = "/";
        try {
            String[] parts = rationalString.split(",");
            String[] pair2 = parts[0].split(pair);
            double degrees = Double.parseDouble(pair2[0].trim()) / Double.parseDouble(pair2[1].trim());
            pair2 = parts[1].split(pair);
            double minutes = Double.parseDouble(pair2[0].trim()) / Double.parseDouble(pair2[1].trim());
            pair = parts[2].split(pair);
            double result = ((minutes / 60.0d) + degrees) + ((Double.parseDouble(pair[0].trim()) / Double.parseDouble(pair[1].trim())) / 3600.0d);
            if (ref.equals("S") || ref.equals("W")) {
                return (float) (-result);
            }
            return (float) result;
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            throw new IllegalArgumentException();
        }
    }

    private void initForFilename(String filename) throws IOException {
        FileInputStream in = null;
        this.mAssetInputStream = null;
        this.mFilename = filename;
        this.mIsInputStream = false;
        try {
            in = new FileInputStream(filename);
            if (isSeekableFD(in.getFD())) {
                this.mSeekableFileDescriptor = in.getFD();
            } else {
                this.mSeekableFileDescriptor = null;
            }
            loadAttributes(in);
        } finally {
            IoUtils.closeQuietly(in);
        }
    }

    private int getMimeType(BufferedInputStream in) throws IOException {
        in.mark(5000);
        byte[] signatureCheckBytes = new byte[5000];
        in.read(signatureCheckBytes);
        in.reset();
        if (isJpegFormat(signatureCheckBytes)) {
            return 4;
        }
        if (isRafFormat(signatureCheckBytes)) {
            return 9;
        }
        if (isHeifFormat(signatureCheckBytes)) {
            return 12;
        }
        if (isOrfFormat(signatureCheckBytes)) {
            return 7;
        }
        if (isRw2Format(signatureCheckBytes)) {
            return 10;
        }
        return 0;
    }

    private static boolean isJpegFormat(byte[] signatureCheckBytes) throws IOException {
        int i = 0;
        while (true) {
            byte[] bArr = JPEG_SIGNATURE;
            if (i >= bArr.length) {
                return true;
            }
            if (signatureCheckBytes[i] != bArr[i]) {
                return false;
            }
            i++;
        }
    }

    private boolean isRafFormat(byte[] signatureCheckBytes) throws IOException {
        byte[] rafSignatureBytes = RAF_SIGNATURE.getBytes();
        for (int i = 0; i < rafSignatureBytes.length; i++) {
            if (signatureCheckBytes[i] != rafSignatureBytes[i]) {
                return false;
            }
        }
        return true;
    }

    private boolean isHeifFormat(byte[] signatureCheckBytes) throws IOException {
        byte[] bArr = signatureCheckBytes;
        ByteOrderedDataInputStream signatureInputStream = null;
        try {
            signatureInputStream = new ByteOrderedDataInputStream(bArr);
            signatureInputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
            long chunkSize = (long) signatureInputStream.readInt();
            byte[] chunkType = new byte[4];
            signatureInputStream.read(chunkType);
            if (Arrays.equals(chunkType, HEIF_TYPE_FTYP)) {
                long chunkDataOffset = 8;
                if (chunkSize == 1) {
                    chunkSize = signatureInputStream.readLong();
                    if (chunkSize < 16) {
                        signatureInputStream.close();
                        return false;
                    }
                    chunkDataOffset = 8 + 8;
                }
                if (chunkSize > ((long) bArr.length)) {
                    chunkSize = (long) bArr.length;
                }
                long chunkDataSize = chunkSize - chunkDataOffset;
                if (chunkDataSize < 8) {
                    signatureInputStream.close();
                    return false;
                }
                byte[] brand = new byte[4];
                boolean isMif1 = false;
                boolean isHeic = false;
                for (long i = 0; i < chunkDataSize / 4; i++) {
                    if (signatureInputStream.read(brand) != brand.length) {
                        signatureInputStream.close();
                        return false;
                    }
                    if (i != 1) {
                        if (Arrays.equals(brand, HEIF_BRAND_MIF1)) {
                            isMif1 = true;
                        } else if (Arrays.equals(brand, HEIF_BRAND_HEIC)) {
                            isHeic = true;
                        }
                        if (isMif1 && isHeic) {
                            signatureInputStream.close();
                            return true;
                        }
                    }
                }
                signatureInputStream.close();
                return false;
            }
            signatureInputStream.close();
            return false;
        } catch (Exception e) {
            if (DEBUG) {
                Log.d(TAG, "Exception parsing HEIF file type box.", e);
            }
            if (signatureInputStream == null) {
            }
        } catch (Throwable th) {
            if (signatureInputStream != null) {
                signatureInputStream.close();
            }
        }
    }

    private boolean isOrfFormat(byte[] signatureCheckBytes) throws IOException {
        ByteOrderedDataInputStream signatureInputStream = new ByteOrderedDataInputStream(signatureCheckBytes);
        this.mExifByteOrder = readByteOrder(signatureInputStream);
        signatureInputStream.setByteOrder(this.mExifByteOrder);
        short orfSignature = signatureInputStream.readShort();
        if (orfSignature == ORF_SIGNATURE_1 || orfSignature == ORF_SIGNATURE_2) {
            return true;
        }
        return false;
    }

    private boolean isRw2Format(byte[] signatureCheckBytes) throws IOException {
        ByteOrderedDataInputStream signatureInputStream = new ByteOrderedDataInputStream(signatureCheckBytes);
        this.mExifByteOrder = readByteOrder(signatureInputStream);
        signatureInputStream.setByteOrder(this.mExifByteOrder);
        if (signatureInputStream.readShort() == RW2_SIGNATURE) {
            return true;
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:71:0x00f8 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00cc  */
    private void getJpegAttributes(android.media.ExifInterface.ByteOrderedDataInputStream r24, int r25, int r26) throws java.io.IOException {
        /*
        r23 = this;
        r0 = r23;
        r1 = r24;
        r2 = r26;
        r3 = DEBUG;
        r4 = "ExifInterface";
        if (r3 == 0) goto L_0x0021;
    L_0x000c:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r5 = "getJpegAttributes starting with: ";
        r3.append(r5);
        r3.append(r1);
        r3 = r3.toString();
        android.util.Log.d(r4, r3);
    L_0x0021:
        r3 = java.nio.ByteOrder.BIG_ENDIAN;
        r1.setByteOrder(r3);
        r3 = r25;
        r5 = (long) r3;
        r1.seek(r5);
        r5 = r25;
        r6 = r24.readByte();
        r7 = r6;
        r8 = "Invalid marker: ";
        r9 = -1;
        if (r6 != r9) goto L_0x01e9;
    L_0x0038:
        r6 = 1;
        r5 = r5 + r6;
        r10 = r24.readByte();
        r11 = -40;
        if (r10 != r11) goto L_0x01ce;
    L_0x0042:
        r5 = r5 + r6;
    L_0x0043:
        r7 = r24.readByte();
        if (r7 != r9) goto L_0x01b1;
    L_0x0049:
        r5 = r5 + 1;
        r7 = r24.readByte();
        r8 = DEBUG;
        if (r8 == 0) goto L_0x006d;
    L_0x0053:
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r10 = "Found JPEG segment indicator: ";
        r8.append(r10);
        r10 = r7 & 255;
        r10 = java.lang.Integer.toHexString(r10);
        r8.append(r10);
        r8 = r8.toString();
        android.util.Log.d(r4, r8);
    L_0x006d:
        r5 = r5 + r6;
        r8 = -39;
        if (r7 == r8) goto L_0x01ab;
    L_0x0072:
        r8 = -38;
        if (r7 != r8) goto L_0x0078;
    L_0x0076:
        goto L_0x01ab;
    L_0x0078:
        r8 = r24.readUnsignedShort();
        r8 = r8 + -2;
        r5 = r5 + 2;
        r10 = DEBUG;
        if (r10 == 0) goto L_0x00ad;
    L_0x0084:
        r10 = new java.lang.StringBuilder;
        r10.<init>();
        r11 = "JPEG segment: ";
        r10.append(r11);
        r11 = r7 & 255;
        r11 = java.lang.Integer.toHexString(r11);
        r10.append(r11);
        r11 = " (length: ";
        r10.append(r11);
        r11 = r8 + 2;
        r10.append(r11);
        r11 = ")";
        r10.append(r11);
        r10 = r10.toString();
        android.util.Log.d(r4, r10);
    L_0x00ad:
        r10 = "Invalid length";
        if (r8 < 0) goto L_0x01a5;
    L_0x00b1:
        r11 = -31;
        if (r7 == r11) goto L_0x012c;
    L_0x00b5:
        r11 = -2;
        if (r7 == r11) goto L_0x0100;
    L_0x00b8:
        switch(r7) {
            case -64: goto L_0x00c6;
            case -63: goto L_0x00c6;
            case -62: goto L_0x00c6;
            case -61: goto L_0x00c6;
            default: goto L_0x00bb;
        };
    L_0x00bb:
        switch(r7) {
            case -59: goto L_0x00c6;
            case -58: goto L_0x00c6;
            case -57: goto L_0x00c6;
            default: goto L_0x00be;
        };
    L_0x00be:
        switch(r7) {
            case -55: goto L_0x00c6;
            case -54: goto L_0x00c6;
            case -53: goto L_0x00c6;
            default: goto L_0x00c1;
        };
    L_0x00c1:
        switch(r7) {
            case -51: goto L_0x00c6;
            case -50: goto L_0x00c6;
            case -49: goto L_0x00c6;
            default: goto L_0x00c4;
        };
    L_0x00c4:
        goto L_0x0186;
    L_0x00c6:
        r11 = r1.skipBytes(r6);
        if (r11 != r6) goto L_0x00f8;
    L_0x00cc:
        r11 = r0.mAttributes;
        r11 = r11[r2];
        r12 = r24.readUnsignedShort();
        r12 = (long) r12;
        r14 = r0.mExifByteOrder;
        r12 = android.media.ExifInterface.ExifAttribute.createULong(r12, r14);
        r13 = "ImageLength";
        r11.put(r13, r12);
        r11 = r0.mAttributes;
        r11 = r11[r2];
        r12 = r24.readUnsignedShort();
        r12 = (long) r12;
        r14 = r0.mExifByteOrder;
        r12 = android.media.ExifInterface.ExifAttribute.createULong(r12, r14);
        r13 = "ImageWidth";
        r11.put(r13, r12);
        r8 = r8 + -5;
        goto L_0x0186;
    L_0x00f8:
        r4 = new java.io.IOException;
        r6 = "Invalid SOFx";
        r4.<init>(r6);
        throw r4;
    L_0x0100:
        r11 = new byte[r8];
        r12 = r1.read(r11);
        if (r12 != r8) goto L_0x0124;
    L_0x0108:
        r8 = 0;
        r12 = "UserComment";
        r13 = r0.getAttribute(r12);
        if (r13 != 0) goto L_0x0186;
    L_0x0111:
        r13 = r0.mAttributes;
        r13 = r13[r6];
        r14 = new java.lang.String;
        r15 = ASCII;
        r14.<init>(r11, r15);
        r14 = android.media.ExifInterface.ExifAttribute.createString(r14);
        r13.put(r12, r14);
        goto L_0x0186;
    L_0x0124:
        r4 = new java.io.IOException;
        r6 = "Invalid exif";
        r4.<init>(r6);
        throw r4;
    L_0x012c:
        r11 = r5;
        r12 = new byte[r8];
        r1.readFully(r12);
        r5 = r5 + r8;
        r8 = 0;
        r13 = IDENTIFIER_EXIF_APP1;
        r13 = com.android.internal.util.ArrayUtils.startsWith(r12, r13);
        if (r13 == 0) goto L_0x014e;
    L_0x013c:
        r13 = IDENTIFIER_EXIF_APP1;
        r14 = r13.length;
        r14 = r14 + r11;
        r14 = (long) r14;
        r13 = r13.length;
        r6 = r12.length;
        r6 = java.util.Arrays.copyOfRange(r12, r13, r6);
        r0.readExifSegment(r6, r2);
        r13 = (int) r14;
        r0.mExifOffset = r13;
        goto L_0x0185;
    L_0x014e:
        r6 = IDENTIFIER_XMP_APP1;
        r6 = com.android.internal.util.ArrayUtils.startsWith(r12, r6);
        if (r6 == 0) goto L_0x0185;
    L_0x0156:
        r6 = IDENTIFIER_XMP_APP1;
        r13 = r6.length;
        r13 = r13 + r11;
        r13 = (long) r13;
        r6 = r6.length;
        r15 = r12.length;
        r6 = java.util.Arrays.copyOfRange(r12, r6, r15);
        r15 = "Xmp";
        r16 = r0.getAttribute(r15);
        if (r16 != 0) goto L_0x0184;
    L_0x0169:
        r9 = r0.mAttributes;
        r16 = 0;
        r9 = r9[r16];
        r2 = new android.media.ExifInterface$ExifAttribute;
        r17 = 1;
        r3 = r6.length;
        r22 = 0;
        r16 = r2;
        r18 = r3;
        r19 = r13;
        r21 = r6;
        r16.<init>(r17, r18, r19, r21, r22);
        r9.put(r15, r2);
    L_0x0184:
        goto L_0x0186;
    L_0x0186:
        if (r8 < 0) goto L_0x019f;
    L_0x0188:
        r2 = r1.skipBytes(r8);
        if (r2 != r8) goto L_0x0197;
    L_0x018e:
        r5 = r5 + r8;
        r3 = r25;
        r2 = r26;
        r6 = 1;
        r9 = -1;
        goto L_0x0043;
    L_0x0197:
        r2 = new java.io.IOException;
        r3 = "Invalid JPEG segment";
        r2.<init>(r3);
        throw r2;
    L_0x019f:
        r2 = new java.io.IOException;
        r2.<init>(r10);
        throw r2;
    L_0x01a5:
        r2 = new java.io.IOException;
        r2.<init>(r10);
        throw r2;
    L_0x01ab:
        r2 = r0.mExifByteOrder;
        r1.setByteOrder(r2);
        return;
    L_0x01b1:
        r2 = new java.io.IOException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Invalid marker:";
        r3.append(r4);
        r4 = r7 & 255;
        r4 = java.lang.Integer.toHexString(r4);
        r3.append(r4);
        r3 = r3.toString();
        r2.<init>(r3);
        throw r2;
    L_0x01ce:
        r2 = new java.io.IOException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r3.append(r8);
        r4 = r7 & 255;
        r4 = java.lang.Integer.toHexString(r4);
        r3.append(r4);
        r3 = r3.toString();
        r2.<init>(r3);
        throw r2;
    L_0x01e9:
        r2 = new java.io.IOException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r3.append(r8);
        r4 = r7 & 255;
        r4 = java.lang.Integer.toHexString(r4);
        r3.append(r4);
        r3 = r3.toString();
        r2.<init>(r3);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.ExifInterface.getJpegAttributes(android.media.ExifInterface$ByteOrderedDataInputStream, int, int):void");
    }

    private void getRawAttributes(ByteOrderedDataInputStream in) throws IOException {
        parseTiffHeaders(in, in.available());
        readImageFileDirectory(in, 0);
        updateImageSizeValues(in, 0);
        updateImageSizeValues(in, 5);
        updateImageSizeValues(in, 4);
        validateImages(in);
        if (this.mMimeType == 8) {
            ExifAttribute makerNoteAttribute = (ExifAttribute) this.mAttributes[1].get(TAG_MAKER_NOTE);
            if (makerNoteAttribute != null) {
                ByteOrderedDataInputStream makerNoteDataInputStream = new ByteOrderedDataInputStream(makerNoteAttribute.bytes);
                makerNoteDataInputStream.setByteOrder(this.mExifByteOrder);
                makerNoteDataInputStream.seek(6);
                readImageFileDirectory(makerNoteDataInputStream, 9);
                HashMap hashMap = this.mAttributes[9];
                String str = TAG_COLOR_SPACE;
                ExifAttribute colorSpaceAttribute = (ExifAttribute) hashMap.get(str);
                if (colorSpaceAttribute != null) {
                    this.mAttributes[1].put(str, colorSpaceAttribute);
                }
            }
        }
    }

    private void getRafAttributes(ByteOrderedDataInputStream in) throws IOException {
        ByteOrderedDataInputStream byteOrderedDataInputStream = in;
        byteOrderedDataInputStream.skipBytes(84);
        byte[] jpegOffsetBytes = new byte[4];
        byte[] cfaHeaderOffsetBytes = new byte[4];
        byteOrderedDataInputStream.read(jpegOffsetBytes);
        byteOrderedDataInputStream.skipBytes(4);
        byteOrderedDataInputStream.read(cfaHeaderOffsetBytes);
        int rafJpegOffset = ByteBuffer.wrap(jpegOffsetBytes).getInt();
        int rafCfaHeaderOffset = ByteBuffer.wrap(cfaHeaderOffsetBytes).getInt();
        getJpegAttributes(byteOrderedDataInputStream, rafJpegOffset, 5);
        byteOrderedDataInputStream.seek((long) rafCfaHeaderOffset);
        byteOrderedDataInputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
        int numberOfDirectoryEntry = in.readInt();
        boolean z = DEBUG;
        String str = TAG;
        if (z) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("numberOfDirectoryEntry: ");
            stringBuilder.append(numberOfDirectoryEntry);
            Log.d(str, stringBuilder.toString());
        }
        for (int i = 0; i < numberOfDirectoryEntry; i++) {
            int tagNumber = in.readUnsignedShort();
            int numberOfBytes = in.readUnsignedShort();
            if (tagNumber == TAG_RAF_IMAGE_SIZE.number) {
                int imageLength = in.readShort();
                int imageWidth = in.readShort();
                ExifAttribute imageLengthAttribute = ExifAttribute.createUShort(imageLength, this.mExifByteOrder);
                ExifAttribute imageWidthAttribute = ExifAttribute.createUShort(imageWidth, this.mExifByteOrder);
                this.mAttributes[0].put(TAG_IMAGE_LENGTH, imageLengthAttribute);
                this.mAttributes[0].put(TAG_IMAGE_WIDTH, imageWidthAttribute);
                if (DEBUG) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Updated to length: ");
                    stringBuilder2.append(imageLength);
                    stringBuilder2.append(", width: ");
                    stringBuilder2.append(imageWidth);
                    Log.d(str, stringBuilder2.toString());
                }
                return;
            }
            byteOrderedDataInputStream.skipBytes(numberOfBytes);
        }
    }

    private void getHeifAttributes(ByteOrderedDataInputStream in) throws IOException {
        Throwable th;
        final ByteOrderedDataInputStream byteOrderedDataInputStream = in;
        String str = "yes";
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            int orientation;
            int parseInt;
            retriever.setDataSource(new MediaDataSource() {
                long mPosition;

                public void close() throws IOException {
                }

                public int readAt(long position, byte[] buffer, int offset, int size) throws IOException {
                    if (size == 0) {
                        return 0;
                    }
                    if (position < 0) {
                        return -1;
                    }
                    try {
                        if (this.mPosition != position) {
                            if (this.mPosition >= 0 && position >= this.mPosition + ((long) byteOrderedDataInputStream.available())) {
                                return -1;
                            }
                            byteOrderedDataInputStream.seek(position);
                            this.mPosition = position;
                        }
                        if (size > byteOrderedDataInputStream.available()) {
                            size = byteOrderedDataInputStream.available();
                        }
                        int bytesRead = byteOrderedDataInputStream.read(buffer, offset, size);
                        if (bytesRead >= 0) {
                            this.mPosition += (long) bytesRead;
                            return bytesRead;
                        }
                    } catch (IOException e) {
                    }
                    this.mPosition = -1;
                    return -1;
                }

                public long getSize() throws IOException {
                    return -1;
                }
            });
            String exifOffsetStr = retriever.extractMetadata(33);
            String exifLengthStr = retriever.extractMetadata(34);
            String hasImage = retriever.extractMetadata(26);
            String hasVideo = retriever.extractMetadata(17);
            String width = null;
            String height = null;
            String rotation = null;
            String METADATA_VALUE_YES = str;
            if (str.equals(hasImage)) {
                width = retriever.extractMetadata(29);
                height = retriever.extractMetadata(30);
                rotation = retriever.extractMetadata(31);
            } else if (str.equals(hasVideo)) {
                width = retriever.extractMetadata(18);
                height = retriever.extractMetadata(19);
                rotation = retriever.extractMetadata(24);
            }
            if (width != null) {
                this.mAttributes[0].put(TAG_IMAGE_WIDTH, ExifAttribute.createUShort(Integer.parseInt(width), this.mExifByteOrder));
            }
            if (height != null) {
                this.mAttributes[0].put(TAG_IMAGE_LENGTH, ExifAttribute.createUShort(Integer.parseInt(height), this.mExifByteOrder));
            }
            if (rotation != null) {
                orientation = 1;
                parseInt = Integer.parseInt(rotation);
                if (parseInt == 90) {
                    orientation = 6;
                } else if (parseInt == 180) {
                    orientation = 3;
                } else if (parseInt == 270) {
                    orientation = 8;
                }
                this.mAttributes[0].put(TAG_ORIENTATION, ExifAttribute.createUShort(orientation, this.mExifByteOrder));
            }
            if (exifOffsetStr == null || exifLengthStr == null) {
            } else {
                orientation = Integer.parseInt(exifOffsetStr);
                parseInt = Integer.parseInt(exifLengthStr);
                if (parseInt > 6) {
                    try {
                        byteOrderedDataInputStream.seek((long) orientation);
                        byte[] identifier = new byte[6];
                        byte[] bArr;
                        if (byteOrderedDataInputStream.read(identifier) == 6) {
                            orientation += 6;
                            parseInt -= 6;
                            if (Arrays.equals(identifier, IDENTIFIER_EXIF_APP1)) {
                                byte[] bytes = new byte[parseInt];
                                if (byteOrderedDataInputStream.read(bytes) == parseInt) {
                                    readExifSegment(bytes, 0);
                                } else {
                                    throw new IOException("Can't read exif");
                                }
                            }
                            bArr = identifier;
                            throw new IOException("Invalid identifier");
                        }
                        bArr = identifier;
                        throw new IOException("Can't read identifier");
                    } catch (Throwable th2) {
                        th = th2;
                        retriever.release();
                        throw th;
                    }
                }
                throw new IOException("Invalid exif length");
            }
            if (DEBUG) {
                str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Heif meta: ");
                stringBuilder.append(width);
                stringBuilder.append("x");
                stringBuilder.append(height);
                stringBuilder.append(", rotation ");
                stringBuilder.append(rotation);
                Log.d(str, stringBuilder.toString());
            }
            retriever.release();
        } catch (Throwable th3) {
            th = th3;
            retriever.release();
            throw th;
        }
    }

    private void getOrfAttributes(ByteOrderedDataInputStream in) throws IOException {
        getRawAttributes(in);
        ExifAttribute makerNoteAttribute = (ExifAttribute) this.mAttributes[1].get(TAG_MAKER_NOTE);
        if (makerNoteAttribute != null) {
            ByteOrderedDataInputStream makerNoteDataInputStream = new ByteOrderedDataInputStream(makerNoteAttribute.bytes);
            makerNoteDataInputStream.setByteOrder(this.mExifByteOrder);
            byte[] makerNoteHeader1Bytes = new byte[ORF_MAKER_NOTE_HEADER_1.length];
            makerNoteDataInputStream.readFully(makerNoteHeader1Bytes);
            makerNoteDataInputStream.seek(0);
            byte[] makerNoteHeader2Bytes = new byte[ORF_MAKER_NOTE_HEADER_2.length];
            makerNoteDataInputStream.readFully(makerNoteHeader2Bytes);
            if (Arrays.equals(makerNoteHeader1Bytes, ORF_MAKER_NOTE_HEADER_1)) {
                makerNoteDataInputStream.seek(8);
            } else if (Arrays.equals(makerNoteHeader2Bytes, ORF_MAKER_NOTE_HEADER_2)) {
                makerNoteDataInputStream.seek(12);
            }
            readImageFileDirectory(makerNoteDataInputStream, 6);
            ExifAttribute imageLengthAttribute = (ExifAttribute) this.mAttributes[7].get(TAG_ORF_PREVIEW_IMAGE_START);
            ExifAttribute bitsPerSampleAttribute = (ExifAttribute) this.mAttributes[7].get(TAG_ORF_PREVIEW_IMAGE_LENGTH);
            if (!(imageLengthAttribute == null || bitsPerSampleAttribute == null)) {
                this.mAttributes[5].put(TAG_JPEG_INTERCHANGE_FORMAT, imageLengthAttribute);
                this.mAttributes[5].put(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, bitsPerSampleAttribute);
            }
            ExifAttribute aspectFrameAttribute = (ExifAttribute) this.mAttributes[8].get(TAG_ORF_ASPECT_FRAME);
            if (aspectFrameAttribute != null) {
                int[] aspectFrameValues = new int[4];
                aspectFrameValues = (int[]) aspectFrameAttribute.getValue(this.mExifByteOrder);
                if (aspectFrameValues[2] > aspectFrameValues[0] && aspectFrameValues[3] > aspectFrameValues[1]) {
                    int primaryImageWidth = (aspectFrameValues[2] - aspectFrameValues[0]) + 1;
                    int primaryImageLength = (aspectFrameValues[3] - aspectFrameValues[1]) + 1;
                    if (primaryImageWidth < primaryImageLength) {
                        primaryImageWidth += primaryImageLength;
                        primaryImageLength = primaryImageWidth - primaryImageLength;
                        primaryImageWidth -= primaryImageLength;
                    }
                    ExifAttribute primaryImageWidthAttribute = ExifAttribute.createUShort(primaryImageWidth, this.mExifByteOrder);
                    ExifAttribute primaryImageLengthAttribute = ExifAttribute.createUShort(primaryImageLength, this.mExifByteOrder);
                    this.mAttributes[0].put(TAG_IMAGE_WIDTH, primaryImageWidthAttribute);
                    this.mAttributes[0].put(TAG_IMAGE_LENGTH, primaryImageLengthAttribute);
                }
            }
        }
    }

    private void getRw2Attributes(ByteOrderedDataInputStream in) throws IOException {
        getRawAttributes(in);
        if (((ExifAttribute) this.mAttributes[0].get(TAG_RW2_JPG_FROM_RAW)) != null) {
            getJpegAttributes(in, this.mRw2JpgFromRawOffset, 5);
        }
        ExifAttribute rw2IsoAttribute = (ExifAttribute) this.mAttributes[0].get(TAG_RW2_ISO);
        String str = "ISOSpeedRatings";
        ExifAttribute exifIsoAttribute = (ExifAttribute) this.mAttributes[1].get(str);
        if (rw2IsoAttribute != null && exifIsoAttribute == null) {
            this.mAttributes[1].put(str, rw2IsoAttribute);
        }
    }

    private void saveJpegAttributes(InputStream inputStream, OutputStream outputStream) throws IOException {
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("saveJpegAttributes starting with (inputStream: ");
            stringBuilder.append(inputStream);
            stringBuilder.append(", outputStream: ");
            stringBuilder.append(outputStream);
            stringBuilder.append(")");
            Log.d(TAG, stringBuilder.toString());
        }
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        ByteOrderedDataOutputStream dataOutputStream = new ByteOrderedDataOutputStream(outputStream, ByteOrder.BIG_ENDIAN);
        String str = "Invalid marker";
        if (dataInputStream.readByte() == (byte) -1) {
            dataOutputStream.writeByte(-1);
            if (dataInputStream.readByte() == MARKER_SOI) {
                dataOutputStream.writeByte(-40);
                dataOutputStream.writeByte(-1);
                dataOutputStream.writeByte(-31);
                writeExifSegment(dataOutputStream, 6);
                byte[] bytes = new byte[4096];
                while (dataInputStream.readByte() == (byte) -1) {
                    byte marker = dataInputStream.readByte();
                    if (marker == MARKER_EOI || marker == MARKER_SOS) {
                        dataOutputStream.writeByte(-1);
                        dataOutputStream.writeByte(marker);
                        Streams.copy(dataInputStream, dataOutputStream);
                        return;
                    }
                    String str2 = "Invalid length";
                    int length;
                    int read;
                    if (marker != MARKER_APP1) {
                        dataOutputStream.writeByte(-1);
                        dataOutputStream.writeByte(marker);
                        length = dataInputStream.readUnsignedShort();
                        dataOutputStream.writeUnsignedShort(length);
                        length -= 2;
                        if (length >= 0) {
                            while (length > 0) {
                                read = dataInputStream.read(bytes, 0, Math.min(length, bytes.length));
                                int read2 = read;
                                if (read < 0) {
                                    break;
                                }
                                dataOutputStream.write(bytes, 0, read2);
                                length -= read2;
                            }
                        } else {
                            throw new IOException(str2);
                        }
                    }
                    length = dataInputStream.readUnsignedShort() - 2;
                    if (length >= 0) {
                        byte[] identifier = new byte[6];
                        if (length >= 6) {
                            if (dataInputStream.read(identifier) != 6) {
                                throw new IOException("Invalid exif");
                            } else if (Arrays.equals(identifier, IDENTIFIER_EXIF_APP1)) {
                                if (dataInputStream.skipBytes(length - 6) != length - 6) {
                                    throw new IOException(str2);
                                }
                            }
                        }
                        dataOutputStream.writeByte(-1);
                        dataOutputStream.writeByte(marker);
                        dataOutputStream.writeUnsignedShort(length + 2);
                        if (length >= 6) {
                            length -= 6;
                            dataOutputStream.write(identifier);
                        }
                        while (length > 0) {
                            read = dataInputStream.read(bytes, 0, Math.min(length, bytes.length));
                            int read3 = read;
                            if (read < 0) {
                                break;
                            }
                            dataOutputStream.write(bytes, 0, read3);
                            length -= read3;
                        }
                    } else {
                        throw new IOException(str2);
                    }
                }
                throw new IOException(str);
            }
            throw new IOException(str);
        }
        throw new IOException(str);
    }

    private void readExifSegment(byte[] exifBytes, int imageType) throws IOException {
        ByteOrderedDataInputStream dataInputStream = new ByteOrderedDataInputStream(exifBytes);
        parseTiffHeaders(dataInputStream, exifBytes.length);
        readImageFileDirectory(dataInputStream, imageType);
    }

    private void addDefaultValuesForCompatibility() {
        String str;
        String valueOfDateTimeOriginal = getAttribute(TAG_DATETIME_ORIGINAL);
        if (valueOfDateTimeOriginal != null) {
            str = "DateTime";
            if (getAttribute(str) == null) {
                this.mAttributes[0].put(str, ExifAttribute.createString(valueOfDateTimeOriginal));
            }
        }
        str = TAG_IMAGE_WIDTH;
        if (getAttribute(str) == null) {
            this.mAttributes[0].put(str, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        str = TAG_IMAGE_LENGTH;
        if (getAttribute(str) == null) {
            this.mAttributes[0].put(str, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        str = TAG_ORIENTATION;
        if (getAttribute(str) == null) {
            this.mAttributes[0].put(str, ExifAttribute.createUShort(0, this.mExifByteOrder));
        }
        String str2 = TAG_LIGHT_SOURCE;
        if (getAttribute(str2) == null) {
            this.mAttributes[1].put(str2, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
    }

    private ByteOrder readByteOrder(ByteOrderedDataInputStream dataInputStream) throws IOException {
        short byteOrder = dataInputStream.readShort();
        String str = TAG;
        if (byteOrder == BYTE_ALIGN_II) {
            if (DEBUG) {
                Log.d(str, "readExifSegment: Byte Align II");
            }
            return ByteOrder.LITTLE_ENDIAN;
        } else if (byteOrder == BYTE_ALIGN_MM) {
            if (DEBUG) {
                Log.d(str, "readExifSegment: Byte Align MM");
            }
            return ByteOrder.BIG_ENDIAN;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid byte order: ");
            stringBuilder.append(Integer.toHexString(byteOrder));
            throw new IOException(stringBuilder.toString());
        }
    }

    private void parseTiffHeaders(ByteOrderedDataInputStream dataInputStream, int exifBytesLength) throws IOException {
        this.mExifByteOrder = readByteOrder(dataInputStream);
        dataInputStream.setByteOrder(this.mExifByteOrder);
        int startCode = dataInputStream.readUnsignedShort();
        int i = this.mMimeType;
        if (i == 7 || i == 10 || startCode == 42) {
            i = dataInputStream.readInt();
            StringBuilder stringBuilder;
            if (i < 8 || i >= exifBytesLength) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid first Ifd offset: ");
                stringBuilder.append(i);
                throw new IOException(stringBuilder.toString());
            }
            i -= 8;
            if (i > 0 && dataInputStream.skipBytes(i) != i) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Couldn't jump to first Ifd: ");
                stringBuilder.append(i);
                throw new IOException(stringBuilder.toString());
            }
            return;
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Invalid start code: ");
        stringBuilder2.append(Integer.toHexString(startCode));
        throw new IOException(stringBuilder2.toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:46:0x013d  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0132  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0132  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x013d  */
    private void readImageFileDirectory(android.media.ExifInterface.ByteOrderedDataInputStream r30, int r31) throws java.io.IOException {
        /*
        r29 = this;
        r0 = r29;
        r1 = r30;
        r2 = r31;
        r3 = r0.mHandledIfdOffsets;
        r4 = r30.mPosition;
        r4 = java.lang.Integer.valueOf(r4);
        r3.add(r4);
        r3 = r30.mPosition;
        r4 = 2;
        r3 = r3 + r4;
        r5 = r30.mLength;
        if (r3 <= r5) goto L_0x0020;
    L_0x001f:
        return;
    L_0x0020:
        r3 = r30.readShort();
        r5 = r30.mPosition;
        r6 = r3 * 12;
        r5 = r5 + r6;
        r6 = r30.mLength;
        if (r5 > r6) goto L_0x0414;
    L_0x0031:
        if (r3 > 0) goto L_0x0037;
    L_0x0033:
        r22 = r3;
        goto L_0x0416;
    L_0x0037:
        r5 = DEBUG;
        r6 = "ExifInterface";
        if (r5 == 0) goto L_0x0052;
    L_0x003d:
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r7 = "numberOfDirectoryEntry: ";
        r5.append(r7);
        r5.append(r3);
        r5 = r5.toString();
        android.util.Log.d(r6, r5);
    L_0x0052:
        r5 = 0;
    L_0x0053:
        r9 = 0;
        r10 = 5;
        r12 = 4;
        if (r5 >= r3) goto L_0x037c;
    L_0x0058:
        r13 = r30.readUnsignedShort();
        r15 = r30.readUnsignedShort();
        r14 = r30.readInt();
        r16 = r30.peek();
        r7 = r16 + 4;
        r7 = (long) r7;
        r16 = sExifTagMapsForReading;
        r12 = r16[r2];
        r4 = java.lang.Integer.valueOf(r13);
        r4 = r12.get(r4);
        r4 = (android.media.ExifInterface.ExifTag) r4;
        r12 = DEBUG;
        r11 = 3;
        if (r12 == 0) goto L_0x00b0;
    L_0x007e:
        r10 = new java.lang.Object[r10];
        r12 = java.lang.Integer.valueOf(r31);
        r10[r9] = r12;
        r12 = java.lang.Integer.valueOf(r13);
        r16 = 1;
        r10[r16] = r12;
        if (r4 == 0) goto L_0x0093;
    L_0x0090:
        r12 = r4.name;
        goto L_0x0094;
    L_0x0093:
        r12 = 0;
    L_0x0094:
        r20 = 2;
        r10[r20] = r12;
        r12 = java.lang.Integer.valueOf(r15);
        r10[r11] = r12;
        r12 = java.lang.Integer.valueOf(r14);
        r19 = 4;
        r10[r19] = r12;
        r12 = "ifdType: %d, tagNumber: %d, tagName: %s, dataFormat: %d, numberOfComponents: %d";
        r10 = java.lang.String.format(r12, r10);
        android.util.Log.d(r6, r10);
    L_0x00b0:
        r22 = 0;
        r10 = 0;
        if (r4 != 0) goto L_0x00d3;
    L_0x00b5:
        r12 = DEBUG;
        if (r12 == 0) goto L_0x00d0;
    L_0x00b9:
        r12 = new java.lang.StringBuilder;
        r12.<init>();
        r9 = "Skip the tag entry since tag number is not defined: ";
        r12.append(r9);
        r12.append(r13);
        r9 = r12.toString();
        android.util.Log.d(r6, r9);
        r24 = r10;
        goto L_0x012e;
    L_0x00d0:
        r24 = r10;
        goto L_0x012e;
    L_0x00d3:
        if (r15 <= 0) goto L_0x0114;
    L_0x00d5:
        r9 = IFD_FORMAT_BYTES_PER_FORMAT;
        r12 = r9.length;
        if (r15 < r12) goto L_0x00dd;
    L_0x00da:
        r24 = r10;
        goto L_0x0116;
    L_0x00dd:
        r11 = (long) r14;
        r9 = r9[r15];
        r24 = r10;
        r9 = (long) r9;
        r22 = r11 * r9;
        r9 = 0;
        r11 = (r22 > r9 ? 1 : (r22 == r9 ? 0 : -1));
        if (r11 < 0) goto L_0x00f9;
    L_0x00eb:
        r9 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r9 = (r22 > r9 ? 1 : (r22 == r9 ? 0 : -1));
        if (r9 <= 0) goto L_0x00f3;
    L_0x00f2:
        goto L_0x00f9;
    L_0x00f3:
        r10 = 1;
        r24 = r10;
        r9 = r22;
        goto L_0x0130;
    L_0x00f9:
        r9 = DEBUG;
        if (r9 == 0) goto L_0x0111;
    L_0x00fd:
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = "Skip the tag entry since the number of components is invalid: ";
        r9.append(r10);
        r9.append(r14);
        r9 = r9.toString();
        android.util.Log.d(r6, r9);
    L_0x0111:
        r9 = r22;
        goto L_0x0130;
    L_0x0114:
        r24 = r10;
    L_0x0116:
        r9 = DEBUG;
        if (r9 == 0) goto L_0x012e;
    L_0x011a:
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = "Skip the tag entry since data format is invalid: ";
        r9.append(r10);
        r9.append(r15);
        r9 = r9.toString();
        android.util.Log.d(r6, r9);
    L_0x012e:
        r9 = r22;
    L_0x0130:
        if (r24 != 0) goto L_0x013d;
    L_0x0132:
        r1.seek(r7);
        r22 = r3;
        r23 = r5;
        r28 = 2;
        goto L_0x0371;
    L_0x013d:
        r11 = 4;
        r11 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1));
        r12 = "Compression";
        if (r11 <= 0) goto L_0x0207;
    L_0x0145:
        r11 = r30.readInt();
        r22 = DEBUG;
        if (r22 == 0) goto L_0x0167;
    L_0x014d:
        r22 = r3;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r23 = r5;
        r5 = "seek to data offset: ";
        r3.append(r5);
        r3.append(r11);
        r3 = r3.toString();
        android.util.Log.d(r6, r3);
        goto L_0x016b;
    L_0x0167:
        r22 = r3;
        r23 = r5;
    L_0x016b:
        r3 = r0.mMimeType;
        r5 = 7;
        if (r3 != r5) goto L_0x01c8;
    L_0x0170:
        r3 = r4.name;
        r5 = "MakerNote";
        if (r3 != r5) goto L_0x017d;
    L_0x0176:
        r0.mOrfMakerNoteOffset = r11;
        r26 = r14;
        r25 = r15;
        goto L_0x01d8;
    L_0x017d:
        r3 = 6;
        if (r2 != r3) goto L_0x01c3;
    L_0x0180:
        r5 = r4.name;
        r3 = "ThumbnailImage";
        if (r5 != r3) goto L_0x01c3;
    L_0x0186:
        r0.mOrfThumbnailOffset = r11;
        r0.mOrfThumbnailLength = r14;
        r3 = r0.mExifByteOrder;
        r5 = 6;
        r3 = android.media.ExifInterface.ExifAttribute.createUShort(r5, r3);
        r5 = r0.mOrfThumbnailOffset;
        r26 = r14;
        r25 = r15;
        r14 = (long) r5;
        r5 = r0.mExifByteOrder;
        r5 = android.media.ExifInterface.ExifAttribute.createULong(r14, r5);
        r14 = r0.mOrfThumbnailLength;
        r14 = (long) r14;
        r2 = r0.mExifByteOrder;
        r2 = android.media.ExifInterface.ExifAttribute.createULong(r14, r2);
        r14 = r0.mAttributes;
        r15 = 4;
        r14 = r14[r15];
        r14.put(r12, r3);
        r14 = r0.mAttributes;
        r14 = r14[r15];
        r15 = "JPEGInterchangeFormat";
        r14.put(r15, r5);
        r14 = r0.mAttributes;
        r15 = 4;
        r14 = r14[r15];
        r15 = "JPEGInterchangeFormatLength";
        r14.put(r15, r2);
        goto L_0x01d8;
    L_0x01c3:
        r26 = r14;
        r25 = r15;
        goto L_0x01d8;
    L_0x01c8:
        r26 = r14;
        r25 = r15;
        r2 = 10;
        if (r3 != r2) goto L_0x01d8;
    L_0x01d0:
        r2 = r4.name;
        r3 = "JpgFromRaw";
        if (r2 != r3) goto L_0x01d8;
    L_0x01d6:
        r0.mRw2JpgFromRawOffset = r11;
    L_0x01d8:
        r2 = (long) r11;
        r2 = r2 + r9;
        r5 = r30.mLength;
        r14 = (long) r5;
        r2 = (r2 > r14 ? 1 : (r2 == r14 ? 0 : -1));
        if (r2 > 0) goto L_0x01e8;
    L_0x01e3:
        r2 = (long) r11;
        r1.seek(r2);
        goto L_0x020f;
    L_0x01e8:
        r2 = DEBUG;
        if (r2 == 0) goto L_0x0200;
    L_0x01ec:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Skip the tag entry since data offset is invalid: ";
        r2.append(r3);
        r2.append(r11);
        r2 = r2.toString();
        android.util.Log.d(r6, r2);
    L_0x0200:
        r1.seek(r7);
        r28 = 2;
        goto L_0x0371;
    L_0x0207:
        r22 = r3;
        r23 = r5;
        r26 = r14;
        r25 = r15;
    L_0x020f:
        r2 = sExifPointerTagMap;
        r3 = java.lang.Integer.valueOf(r13);
        r2 = r2.get(r3);
        r2 = (java.lang.Integer) r2;
        r3 = DEBUG;
        if (r3 == 0) goto L_0x023c;
    L_0x021f:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r5 = "nextIfdType: ";
        r3.append(r5);
        r3.append(r2);
        r5 = " byteCount: ";
        r3.append(r5);
        r3.append(r9);
        r3 = r3.toString();
        android.util.Log.d(r6, r3);
    L_0x023c:
        r3 = 8;
        if (r2 == 0) goto L_0x02fc;
    L_0x0240:
        r11 = -1;
        r5 = r25;
        r14 = 3;
        if (r5 == r14) goto L_0x0266;
    L_0x0247:
        r14 = 4;
        if (r5 == r14) goto L_0x0261;
    L_0x024a:
        if (r5 == r3) goto L_0x025b;
    L_0x024c:
        r3 = 9;
        if (r5 == r3) goto L_0x0255;
    L_0x0250:
        r3 = 13;
        if (r5 == r3) goto L_0x0255;
    L_0x0254:
        goto L_0x026c;
    L_0x0255:
        r3 = r30.readInt();
        r11 = (long) r3;
        goto L_0x026c;
    L_0x025b:
        r3 = r30.readShort();
        r11 = (long) r3;
        goto L_0x026c;
    L_0x0261:
        r11 = r30.readUnsignedInt();
        goto L_0x026c;
    L_0x0266:
        r3 = r30.readUnsignedShort();
        r11 = (long) r3;
    L_0x026c:
        r3 = DEBUG;
        if (r3 == 0) goto L_0x028b;
    L_0x0270:
        r15 = 2;
        r3 = new java.lang.Object[r15];
        r14 = java.lang.Long.valueOf(r11);
        r19 = 0;
        r3[r19] = r14;
        r14 = r4.name;
        r16 = 1;
        r3[r16] = r14;
        r14 = "Offset: %d, tagName: %s";
        r3 = java.lang.String.format(r14, r3);
        android.util.Log.d(r6, r3);
        goto L_0x028c;
    L_0x028b:
        r15 = 2;
    L_0x028c:
        r16 = 0;
        r3 = (r11 > r16 ? 1 : (r11 == r16 ? 0 : -1));
        if (r3 <= 0) goto L_0x02db;
    L_0x0292:
        r3 = r30.mLength;
        r21 = r13;
        r13 = (long) r3;
        r3 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1));
        if (r3 >= 0) goto L_0x02dd;
    L_0x029d:
        r3 = r0.mHandledIfdOffsets;
        r13 = (int) r11;
        r13 = java.lang.Integer.valueOf(r13);
        r3 = r3.contains(r13);
        if (r3 != 0) goto L_0x02b5;
    L_0x02aa:
        r1.seek(r11);
        r3 = r2.intValue();
        r0.readImageFileDirectory(r1, r3);
        goto L_0x02f5;
    L_0x02b5:
        r3 = DEBUG;
        if (r3 == 0) goto L_0x02f5;
    L_0x02b9:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r13 = "Skip jump into the IFD since it has already been read: IfdType ";
        r3.append(r13);
        r3.append(r2);
        r13 = " (at ";
        r3.append(r13);
        r3.append(r11);
        r13 = ")";
        r3.append(r13);
        r3 = r3.toString();
        android.util.Log.d(r6, r3);
        goto L_0x02f5;
    L_0x02db:
        r21 = r13;
    L_0x02dd:
        r3 = DEBUG;
        if (r3 == 0) goto L_0x02f5;
    L_0x02e1:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r13 = "Skip jump into the IFD since its offset is invalid: ";
        r3.append(r13);
        r3.append(r11);
        r3 = r3.toString();
        android.util.Log.d(r6, r3);
    L_0x02f5:
        r1.seek(r7);
        r28 = r15;
        goto L_0x0371;
    L_0x02fc:
        r21 = r13;
        r5 = r25;
        r15 = 2;
        r11 = r30.peek();
        r13 = (int) r9;
        r13 = new byte[r13];
        r1.readFully(r13);
        r25 = new android.media.ExifInterface$ExifAttribute;
        r27 = r4;
        r3 = (long) r11;
        r20 = 0;
        r14 = r25;
        r28 = r15;
        r15 = r5;
        r16 = r26;
        r17 = r3;
        r19 = r13;
        r14.<init>(r15, r16, r17, r19, r20);
        r3 = r25;
        r4 = r0.mAttributes;
        r4 = r4[r31];
        r14 = r27;
        r15 = r14.name;
        r4.put(r15, r3);
        r4 = r14.name;
        r15 = "DNGVersion";
        if (r4 != r15) goto L_0x0336;
    L_0x0333:
        r4 = 3;
        r0.mMimeType = r4;
    L_0x0336:
        r4 = r14.name;
        r15 = "Make";
        if (r4 == r15) goto L_0x0342;
    L_0x033c:
        r4 = r14.name;
        r15 = "Model";
        if (r4 != r15) goto L_0x0350;
    L_0x0342:
        r4 = r0.mExifByteOrder;
        r4 = r3.getStringValue(r4);
        r15 = "PENTAX";
        r4 = r4.contains(r15);
        if (r4 != 0) goto L_0x035f;
    L_0x0350:
        r4 = r14.name;
        if (r4 != r12) goto L_0x0363;
    L_0x0354:
        r4 = r0.mExifByteOrder;
        r4 = r3.getIntValue(r4);
        r12 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        if (r4 != r12) goto L_0x0363;
    L_0x035f:
        r4 = 8;
        r0.mMimeType = r4;
    L_0x0363:
        r4 = r30.peek();
        r12 = r2;
        r15 = r3;
        r2 = (long) r4;
        r2 = (r2 > r7 ? 1 : (r2 == r7 ? 0 : -1));
        if (r2 == 0) goto L_0x0371;
    L_0x036e:
        r1.seek(r7);
    L_0x0371:
        r5 = r23 + 1;
        r5 = (short) r5;
        r2 = r31;
        r3 = r22;
        r4 = r28;
        goto L_0x0053;
    L_0x037c:
        r22 = r3;
        r23 = r5;
        r2 = r30.peek();
        r3 = 4;
        r2 = r2 + r3;
        r3 = r30.mLength;
        if (r2 > r3) goto L_0x0413;
    L_0x038c:
        r2 = r30.readInt();
        r3 = DEBUG;
        if (r3 == 0) goto L_0x03a8;
    L_0x0394:
        r3 = 1;
        r3 = new java.lang.Object[r3];
        r4 = java.lang.Integer.valueOf(r2);
        r5 = 0;
        r3[r5] = r4;
        r4 = "nextIfdOffset: %d";
        r3 = java.lang.String.format(r4, r3);
        android.util.Log.d(r6, r3);
    L_0x03a8:
        r3 = (long) r2;
        r7 = 0;
        r3 = (r3 > r7 ? 1 : (r3 == r7 ? 0 : -1));
        if (r3 <= 0) goto L_0x03fb;
    L_0x03af:
        r3 = r30.mLength;
        if (r2 >= r3) goto L_0x03fb;
    L_0x03b5:
        r3 = r0.mHandledIfdOffsets;
        r4 = java.lang.Integer.valueOf(r2);
        r3 = r3.contains(r4);
        if (r3 != 0) goto L_0x03e2;
    L_0x03c1:
        r3 = (long) r2;
        r1.seek(r3);
        r3 = r0.mAttributes;
        r4 = 4;
        r3 = r3[r4];
        r3 = r3.isEmpty();
        if (r3 == 0) goto L_0x03d4;
    L_0x03d0:
        r0.readImageFileDirectory(r1, r4);
        goto L_0x0413;
    L_0x03d4:
        r3 = r0.mAttributes;
        r3 = r3[r10];
        r3 = r3.isEmpty();
        if (r3 == 0) goto L_0x0413;
    L_0x03de:
        r0.readImageFileDirectory(r1, r10);
        goto L_0x0413;
    L_0x03e2:
        r3 = DEBUG;
        if (r3 == 0) goto L_0x0413;
    L_0x03e6:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Stop reading file since re-reading an IFD may cause an infinite loop: ";
        r3.append(r4);
        r3.append(r2);
        r3 = r3.toString();
        android.util.Log.d(r6, r3);
        goto L_0x0413;
    L_0x03fb:
        r3 = DEBUG;
        if (r3 == 0) goto L_0x0413;
    L_0x03ff:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Stop reading file since a wrong offset may cause an infinite loop: ";
        r3.append(r4);
        r3.append(r2);
        r3 = r3.toString();
        android.util.Log.d(r6, r3);
    L_0x0413:
        return;
    L_0x0414:
        r22 = r3;
    L_0x0416:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.ExifInterface.readImageFileDirectory(android.media.ExifInterface$ByteOrderedDataInputStream, int):void");
    }

    private void retrieveJpegImageSize(ByteOrderedDataInputStream in, int imageType) throws IOException {
        ExifAttribute imageWidthAttribute = (ExifAttribute) this.mAttributes[imageType].get(TAG_IMAGE_WIDTH);
        if (((ExifAttribute) this.mAttributes[imageType].get(TAG_IMAGE_LENGTH)) == null || imageWidthAttribute == null) {
            ExifAttribute jpegInterchangeFormatAttribute = (ExifAttribute) this.mAttributes[imageType].get(TAG_JPEG_INTERCHANGE_FORMAT);
            if (jpegInterchangeFormatAttribute != null) {
                getJpegAttributes(in, jpegInterchangeFormatAttribute.getIntValue(this.mExifByteOrder), imageType);
            }
        }
    }

    private void setThumbnailData(ByteOrderedDataInputStream in) throws IOException {
        HashMap thumbnailData = this.mAttributes[4];
        ExifAttribute compressionAttribute = (ExifAttribute) thumbnailData.get(TAG_COMPRESSION);
        if (compressionAttribute != null) {
            this.mThumbnailCompression = compressionAttribute.getIntValue(this.mExifByteOrder);
            int i = this.mThumbnailCompression;
            if (i != 1) {
                if (i == 6) {
                    handleThumbnailFromJfif(in, thumbnailData);
                    return;
                } else if (i != 7) {
                    return;
                }
            }
            if (isSupportedDataType(thumbnailData)) {
                handleThumbnailFromStrips(in, thumbnailData);
                return;
            }
            return;
        }
        handleThumbnailFromJfif(in, thumbnailData);
    }

    private void handleThumbnailFromJfif(ByteOrderedDataInputStream in, HashMap thumbnailData) throws IOException {
        ExifAttribute jpegInterchangeFormatAttribute = (ExifAttribute) thumbnailData.get(TAG_JPEG_INTERCHANGE_FORMAT);
        ExifAttribute jpegInterchangeFormatLengthAttribute = (ExifAttribute) thumbnailData.get(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH);
        if (jpegInterchangeFormatAttribute != null && jpegInterchangeFormatLengthAttribute != null) {
            int thumbnailOffset = jpegInterchangeFormatAttribute.getIntValue(this.mExifByteOrder);
            int thumbnailLength = Math.min(jpegInterchangeFormatLengthAttribute.getIntValue(this.mExifByteOrder), in.getLength() - thumbnailOffset);
            int i = this.mMimeType;
            if (i == 4 || i == 9 || i == 10) {
                thumbnailOffset += this.mExifOffset;
            } else if (i == 7) {
                thumbnailOffset += this.mOrfMakerNoteOffset;
            }
            if (DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Setting thumbnail attributes with offset: ");
                stringBuilder.append(thumbnailOffset);
                stringBuilder.append(", length: ");
                stringBuilder.append(thumbnailLength);
                Log.d(TAG, stringBuilder.toString());
            }
            if (thumbnailOffset > 0 && thumbnailLength > 0) {
                this.mHasThumbnail = true;
                this.mThumbnailOffset = thumbnailOffset;
                this.mThumbnailLength = thumbnailLength;
                this.mThumbnailCompression = 6;
                if (this.mFilename == null && this.mAssetInputStream == null && this.mSeekableFileDescriptor == null) {
                    byte[] thumbnailBytes = new byte[thumbnailLength];
                    in.seek((long) thumbnailOffset);
                    in.readFully(thumbnailBytes);
                    this.mThumbnailBytes = thumbnailBytes;
                }
            }
        }
    }

    private void handleThumbnailFromStrips(ByteOrderedDataInputStream in, HashMap thumbnailData) throws IOException {
        ByteOrderedDataInputStream byteOrderedDataInputStream = in;
        HashMap hashMap = thumbnailData;
        ExifAttribute stripOffsetsAttribute = (ExifAttribute) hashMap.get(TAG_STRIP_OFFSETS);
        ExifAttribute stripByteCountsAttribute = (ExifAttribute) hashMap.get(TAG_STRIP_BYTE_COUNTS);
        if (stripOffsetsAttribute == null || stripByteCountsAttribute == null) {
        } else {
            long[] stripOffsets = convertToLongArray(stripOffsetsAttribute.getValue(this.mExifByteOrder));
            long[] stripByteCounts = convertToLongArray(stripByteCountsAttribute.getValue(this.mExifByteOrder));
            String str = TAG;
            if (stripOffsets == null) {
                Log.w(str, "stripOffsets should not be null.");
            } else if (stripByteCounts == null) {
                Log.w(str, "stripByteCounts should not be null.");
            } else {
                byte[] totalStripBytes = new byte[((int) Arrays.stream(stripByteCounts).sum())];
                int bytesRead = 0;
                int bytesAdded = 0;
                int i = 0;
                while (i < stripOffsets.length) {
                    int stripByteCount = (int) stripByteCounts[i];
                    int skipBytes = ((int) stripOffsets[i]) - bytesRead;
                    if (skipBytes < 0) {
                        Log.d(str, "Invalid strip offset value");
                    }
                    ExifAttribute stripOffsetsAttribute2 = stripOffsetsAttribute;
                    byteOrderedDataInputStream.seek((long) skipBytes);
                    bytesRead += skipBytes;
                    byte[] stripBytes = new byte[stripByteCount];
                    byteOrderedDataInputStream.read(stripBytes);
                    bytesRead += stripByteCount;
                    System.arraycopy(stripBytes, 0, totalStripBytes, bytesAdded, stripBytes.length);
                    bytesAdded += stripBytes.length;
                    i++;
                    byteOrderedDataInputStream = in;
                    hashMap = thumbnailData;
                    stripOffsetsAttribute = stripOffsetsAttribute2;
                }
                this.mHasThumbnail = true;
                this.mThumbnailBytes = totalStripBytes;
                this.mThumbnailLength = totalStripBytes.length;
            }
        }
    }

    private boolean isSupportedDataType(HashMap thumbnailData) throws IOException {
        ExifAttribute bitsPerSampleAttribute = (ExifAttribute) thumbnailData.get(TAG_BITS_PER_SAMPLE);
        if (bitsPerSampleAttribute != null) {
            int[] bitsPerSampleValue = (int[]) bitsPerSampleAttribute.getValue(this.mExifByteOrder);
            if (Arrays.equals(BITS_PER_SAMPLE_RGB, bitsPerSampleValue)) {
                return true;
            }
            if (this.mMimeType == 3) {
                ExifAttribute photometricInterpretationAttribute = (ExifAttribute) thumbnailData.get(TAG_PHOTOMETRIC_INTERPRETATION);
                if (photometricInterpretationAttribute != null) {
                    int photometricInterpretationValue = photometricInterpretationAttribute.getIntValue(this.mExifByteOrder);
                    if ((photometricInterpretationValue == 1 && Arrays.equals(bitsPerSampleValue, BITS_PER_SAMPLE_GREYSCALE_2)) || (photometricInterpretationValue == 6 && Arrays.equals(bitsPerSampleValue, BITS_PER_SAMPLE_RGB))) {
                        return true;
                    }
                }
            }
        }
        if (DEBUG) {
            Log.d(TAG, "Unsupported data type value");
        }
        return false;
    }

    private boolean isThumbnail(HashMap map) throws IOException {
        ExifAttribute imageLengthAttribute = (ExifAttribute) map.get(TAG_IMAGE_LENGTH);
        ExifAttribute imageWidthAttribute = (ExifAttribute) map.get(TAG_IMAGE_WIDTH);
        if (!(imageLengthAttribute == null || imageWidthAttribute == null)) {
            int imageLengthValue = imageLengthAttribute.getIntValue(this.mExifByteOrder);
            int imageWidthValue = imageWidthAttribute.getIntValue(this.mExifByteOrder);
            if (imageLengthValue <= 512 && imageWidthValue <= 512) {
                return true;
            }
        }
        return false;
    }

    private void validateImages(InputStream in) throws IOException {
        swapBasedOnImageSize(0, 5);
        swapBasedOnImageSize(0, 4);
        swapBasedOnImageSize(5, 4);
        ExifAttribute pixelXDimAttribute = (ExifAttribute) this.mAttributes[1].get(TAG_PIXEL_X_DIMENSION);
        ExifAttribute pixelYDimAttribute = (ExifAttribute) this.mAttributes[1].get(TAG_PIXEL_Y_DIMENSION);
        if (!(pixelXDimAttribute == null || pixelYDimAttribute == null)) {
            this.mAttributes[0].put(TAG_IMAGE_WIDTH, pixelXDimAttribute);
            this.mAttributes[0].put(TAG_IMAGE_LENGTH, pixelYDimAttribute);
        }
        if (this.mAttributes[4].isEmpty() && isThumbnail(this.mAttributes[5])) {
            HashMap[] hashMapArr = this.mAttributes;
            hashMapArr[4] = hashMapArr[5];
            hashMapArr[5] = new HashMap();
        }
        if (!isThumbnail(this.mAttributes[4])) {
            Log.d(TAG, "No image meets the size requirements of a thumbnail image.");
        }
    }

    private void updateImageSizeValues(ByteOrderedDataInputStream in, int imageType) throws IOException {
        ExifAttribute defaultCropSizeAttribute = (ExifAttribute) this.mAttributes[imageType].get(TAG_DEFAULT_CROP_SIZE);
        ExifAttribute topBorderAttribute = (ExifAttribute) this.mAttributes[imageType].get(TAG_RW2_SENSOR_TOP_BORDER);
        ExifAttribute leftBorderAttribute = (ExifAttribute) this.mAttributes[imageType].get(TAG_RW2_SENSOR_LEFT_BORDER);
        ExifAttribute bottomBorderAttribute = (ExifAttribute) this.mAttributes[imageType].get(TAG_RW2_SENSOR_BOTTOM_BORDER);
        ExifAttribute rightBorderAttribute = (ExifAttribute) this.mAttributes[imageType].get(TAG_RW2_SENSOR_RIGHT_BORDER);
        String str = TAG_IMAGE_LENGTH;
        String str2 = TAG_IMAGE_WIDTH;
        if (defaultCropSizeAttribute != null) {
            ExifAttribute defaultCropSizeXAttribute;
            ExifAttribute defaultCropSizeYAttribute;
            if (defaultCropSizeAttribute.format == 5) {
                Rational[] defaultCropSizeValue = (Rational[]) defaultCropSizeAttribute.getValue(this.mExifByteOrder);
                defaultCropSizeXAttribute = ExifAttribute.createURational(defaultCropSizeValue[0], this.mExifByteOrder);
                defaultCropSizeYAttribute = ExifAttribute.createURational(defaultCropSizeValue[1], this.mExifByteOrder);
            } else {
                int[] defaultCropSizeValue2 = (int[]) defaultCropSizeAttribute.getValue(this.mExifByteOrder);
                defaultCropSizeXAttribute = ExifAttribute.createUShort(defaultCropSizeValue2[0], this.mExifByteOrder);
                defaultCropSizeYAttribute = ExifAttribute.createUShort(defaultCropSizeValue2[1], this.mExifByteOrder);
            }
            this.mAttributes[imageType].put(str2, defaultCropSizeXAttribute);
            this.mAttributes[imageType].put(str, defaultCropSizeYAttribute);
            ExifAttribute exifAttribute = defaultCropSizeAttribute;
        } else if (topBorderAttribute == null || leftBorderAttribute == null || bottomBorderAttribute == null || rightBorderAttribute == null) {
            retrieveJpegImageSize(in, imageType);
        } else {
            int topBorderValue = topBorderAttribute.getIntValue(this.mExifByteOrder);
            int bottomBorderValue = bottomBorderAttribute.getIntValue(this.mExifByteOrder);
            int rightBorderValue = rightBorderAttribute.getIntValue(this.mExifByteOrder);
            int leftBorderValue = leftBorderAttribute.getIntValue(this.mExifByteOrder);
            if (bottomBorderValue <= topBorderValue || rightBorderValue <= leftBorderValue) {
                return;
            }
            int width = rightBorderValue - leftBorderValue;
            ExifAttribute imageLengthAttribute = ExifAttribute.createUShort(bottomBorderValue - topBorderValue, this.mExifByteOrder);
            ExifAttribute imageWidthAttribute = ExifAttribute.createUShort(width, this.mExifByteOrder);
            this.mAttributes[imageType].put(str, imageLengthAttribute);
            this.mAttributes[imageType].put(str2, imageWidthAttribute);
        }
    }

    private int writeExifSegment(ByteOrderedDataOutputStream dataOutputStream, int exifOffsetFromBeginning) throws IOException {
        int size;
        int ifdType;
        int i;
        ByteOrderedDataOutputStream byteOrderedDataOutputStream = dataOutputStream;
        ExifTag[][] exifTagArr = EXIF_TAGS;
        int[] ifdOffsets = new int[exifTagArr.length];
        int[] ifdDataSizes = new int[exifTagArr.length];
        for (ExifTag tag : EXIF_POINTER_TAGS) {
            removeAttribute(tag.name);
        }
        removeAttribute(JPEG_INTERCHANGE_FORMAT_TAG.name);
        removeAttribute(JPEG_INTERCHANGE_FORMAT_LENGTH_TAG.name);
        for (ifdType = 0; ifdType < EXIF_TAGS.length; ifdType++) {
            for (Entry entry : this.mAttributes[ifdType].entrySet().toArray()) {
                if (entry.getValue() == null) {
                    this.mAttributes[ifdType].remove(entry.getKey());
                }
            }
        }
        if (!this.mAttributes[1].isEmpty()) {
            this.mAttributes[0].put(EXIF_POINTER_TAGS[1].name, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        int i2 = 2;
        if (!this.mAttributes[2].isEmpty()) {
            this.mAttributes[0].put(EXIF_POINTER_TAGS[2].name, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        if (!this.mAttributes[3].isEmpty()) {
            this.mAttributes[1].put(EXIF_POINTER_TAGS[3].name, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        int i3 = 4;
        if (this.mHasThumbnail) {
            this.mAttributes[4].put(JPEG_INTERCHANGE_FORMAT_TAG.name, ExifAttribute.createULong(0, this.mExifByteOrder));
            this.mAttributes[4].put(JPEG_INTERCHANGE_FORMAT_LENGTH_TAG.name, ExifAttribute.createULong((long) this.mThumbnailLength, this.mExifByteOrder));
        }
        for (ifdType = 0; ifdType < EXIF_TAGS.length; ifdType++) {
            int sum = 0;
            for (Entry entry2 : this.mAttributes[ifdType].entrySet()) {
                size = ((ExifAttribute) entry2.getValue()).size();
                if (size > 4) {
                    sum += size;
                }
            }
            ifdDataSizes[ifdType] = ifdDataSizes[ifdType] + sum;
        }
        ifdType = 8;
        for (size = 0; size < EXIF_TAGS.length; size++) {
            if (!this.mAttributes[size].isEmpty()) {
                ifdOffsets[size] = ifdType;
                ifdType += (((this.mAttributes[size].size() * 12) + 2) + 4) + ifdDataSizes[size];
            }
        }
        if (this.mHasThumbnail) {
            size = ifdType;
            this.mAttributes[4].put(JPEG_INTERCHANGE_FORMAT_TAG.name, ExifAttribute.createULong((long) size, this.mExifByteOrder));
            this.mThumbnailOffset = exifOffsetFromBeginning + size;
            ifdType += this.mThumbnailLength;
        }
        size = ifdType + 8;
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("totalSize length: ");
            stringBuilder.append(size);
            String stringBuilder2 = stringBuilder.toString();
            String str = TAG;
            Log.d(str, stringBuilder2);
            for (i = 0; i < EXIF_TAGS.length; i++) {
                Log.d(str, String.format("index: %d, offsets: %d, tag count: %d, data sizes: %d", new Object[]{Integer.valueOf(i), Integer.valueOf(ifdOffsets[i]), Integer.valueOf(this.mAttributes[i].size()), Integer.valueOf(ifdDataSizes[i])}));
            }
        }
        if (!this.mAttributes[1].isEmpty()) {
            this.mAttributes[0].put(EXIF_POINTER_TAGS[1].name, ExifAttribute.createULong((long) ifdOffsets[1], this.mExifByteOrder));
        }
        if (!this.mAttributes[2].isEmpty()) {
            this.mAttributes[0].put(EXIF_POINTER_TAGS[2].name, ExifAttribute.createULong((long) ifdOffsets[2], this.mExifByteOrder));
        }
        if (!this.mAttributes[3].isEmpty()) {
            this.mAttributes[1].put(EXIF_POINTER_TAGS[3].name, ExifAttribute.createULong((long) ifdOffsets[3], this.mExifByteOrder));
        }
        byteOrderedDataOutputStream.writeUnsignedShort(size);
        byteOrderedDataOutputStream.write(IDENTIFIER_EXIF_APP1);
        byteOrderedDataOutputStream.writeShort(this.mExifByteOrder == ByteOrder.BIG_ENDIAN ? BYTE_ALIGN_MM : BYTE_ALIGN_II);
        byteOrderedDataOutputStream.setByteOrder(this.mExifByteOrder);
        byteOrderedDataOutputStream.writeUnsignedShort(42);
        byteOrderedDataOutputStream.writeUnsignedInt(8);
        int i4 = 0;
        while (i4 < EXIF_TAGS.length) {
            if (!this.mAttributes[i4].isEmpty()) {
                byteOrderedDataOutputStream.writeUnsignedShort(this.mAttributes[i4].size());
                i = ((ifdOffsets[i4] + i2) + (this.mAttributes[i4].size() * 12)) + i3;
                for (Entry entry3 : this.mAttributes[i4].entrySet()) {
                    int tagNumber = ((ExifTag) sExifTagMapsForWriting[i4].get(entry3.getKey())).number;
                    ExifAttribute attribute = (ExifAttribute) entry3.getValue();
                    i2 = attribute.size();
                    byteOrderedDataOutputStream.writeUnsignedShort(tagNumber);
                    byteOrderedDataOutputStream.writeUnsignedShort(attribute.format);
                    byteOrderedDataOutputStream.writeInt(attribute.numberOfComponents);
                    if (i2 > i3) {
                        byteOrderedDataOutputStream.writeUnsignedInt((long) i);
                        i += i2;
                    } else {
                        byteOrderedDataOutputStream.write(attribute.bytes);
                        int i5 = 4;
                        if (i2 < 4) {
                            i3 = i2;
                            while (i3 < i5) {
                                byteOrderedDataOutputStream.writeByte(0);
                                i3++;
                                i5 = 4;
                            }
                        }
                    }
                    i3 = 4;
                }
                if (i4 != 0 || this.mAttributes[4].isEmpty()) {
                    byteOrderedDataOutputStream.writeUnsignedInt(0);
                } else {
                    byteOrderedDataOutputStream.writeUnsignedInt((long) ifdOffsets[4]);
                }
                for (Entry entry32 : this.mAttributes[i4].entrySet()) {
                    ExifAttribute attribute2 = (ExifAttribute) entry32.getValue();
                    if (attribute2.bytes.length > 4) {
                        byteOrderedDataOutputStream.write(attribute2.bytes, 0, attribute2.bytes.length);
                    }
                }
            }
            i4++;
            i2 = 2;
            i3 = 4;
        }
        if (this.mHasThumbnail) {
            byteOrderedDataOutputStream.write(getThumbnailBytes());
        }
        byteOrderedDataOutputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
        return size;
    }

    private static Pair<Integer, Integer> guessDataFormat(String entryValue) {
        String[] entryValues = ",";
        boolean contains = entryValue.contains(entryValues);
        Integer valueOf = Integer.valueOf(2);
        Integer valueOf2 = Integer.valueOf(-1);
        if (contains) {
            entryValues = entryValue.split(entryValues);
            Pair<Integer, Integer> dataFormat = guessDataFormat(entryValues[0]);
            if (((Integer) dataFormat.first).intValue() == 2) {
                return dataFormat;
            }
            for (int i = 1; i < entryValues.length; i++) {
                Pair<Integer, Integer> guessDataFormat = guessDataFormat(entryValues[i]);
                int first = -1;
                int second = -1;
                if (guessDataFormat.first == dataFormat.first || guessDataFormat.second == dataFormat.first) {
                    first = ((Integer) dataFormat.first).intValue();
                }
                if (((Integer) dataFormat.second).intValue() != -1 && (guessDataFormat.first == dataFormat.second || guessDataFormat.second == dataFormat.second)) {
                    second = ((Integer) dataFormat.second).intValue();
                }
                if (first == -1 && second == -1) {
                    return new Pair(valueOf, valueOf2);
                }
                if (first == -1) {
                    dataFormat = new Pair(Integer.valueOf(second), valueOf2);
                } else if (second == -1) {
                    dataFormat = new Pair(Integer.valueOf(first), valueOf2);
                }
            }
            return dataFormat;
        }
        entryValues = "/";
        if (entryValue.contains(entryValues)) {
            entryValues = entryValue.split(entryValues);
            if (entryValues.length == 2) {
                try {
                    long numerator = (long) Double.parseDouble(entryValues[0]);
                    long denominator = (long) Double.parseDouble(entryValues[1]);
                    if (numerator >= 0) {
                        if (denominator >= 0) {
                            if (numerator <= 2147483647L) {
                                if (denominator <= 2147483647L) {
                                    return new Pair(Integer.valueOf(10), Integer.valueOf(5));
                                }
                            }
                            return new Pair(Integer.valueOf(5), valueOf2);
                        }
                    }
                    return new Pair(Integer.valueOf(10), valueOf2);
                } catch (NumberFormatException e) {
                }
            }
            return new Pair(valueOf, valueOf2);
        }
        try {
            Long longValue = Long.valueOf(Long.parseLong(entryValue));
            if (longValue.longValue() >= 0 && longValue.longValue() <= 65535) {
                return new Pair(Integer.valueOf(3), Integer.valueOf(4));
            }
            if (longValue.longValue() < 0) {
                return new Pair(Integer.valueOf(9), valueOf2);
            }
            return new Pair(Integer.valueOf(4), valueOf2);
        } catch (NumberFormatException e2) {
            try {
                Double.parseDouble(entryValue);
                return new Pair(Integer.valueOf(12), valueOf2);
            } catch (NumberFormatException e3) {
                return new Pair(valueOf, valueOf2);
            }
        }
    }

    private void swapBasedOnImageSize(int firstIfdType, int secondIfdType) throws IOException {
        boolean isEmpty = this.mAttributes[firstIfdType].isEmpty();
        String str = TAG;
        if (isEmpty || this.mAttributes[secondIfdType].isEmpty()) {
            if (DEBUG) {
                Log.d(str, "Cannot perform swap since only one image data exists");
            }
            return;
        }
        HashMap hashMap = this.mAttributes[firstIfdType];
        String str2 = TAG_IMAGE_LENGTH;
        ExifAttribute firstImageLengthAttribute = (ExifAttribute) hashMap.get(str2);
        HashMap hashMap2 = this.mAttributes[firstIfdType];
        String str3 = TAG_IMAGE_WIDTH;
        ExifAttribute firstImageWidthAttribute = (ExifAttribute) hashMap2.get(str3);
        ExifAttribute secondImageLengthAttribute = (ExifAttribute) this.mAttributes[secondIfdType].get(str2);
        ExifAttribute secondImageWidthAttribute = (ExifAttribute) this.mAttributes[secondIfdType].get(str3);
        if (firstImageLengthAttribute == null || firstImageWidthAttribute == null) {
            if (DEBUG) {
                Log.d(str, "First image does not contain valid size information");
            }
        } else if (secondImageLengthAttribute != null && secondImageWidthAttribute != null) {
            int firstImageLengthValue = firstImageLengthAttribute.getIntValue(this.mExifByteOrder);
            int firstImageWidthValue = firstImageWidthAttribute.getIntValue(this.mExifByteOrder);
            int secondImageLengthValue = secondImageLengthAttribute.getIntValue(this.mExifByteOrder);
            int secondImageWidthValue = secondImageWidthAttribute.getIntValue(this.mExifByteOrder);
            if (firstImageLengthValue < secondImageLengthValue && firstImageWidthValue < secondImageWidthValue) {
                HashMap[] hashMapArr = this.mAttributes;
                HashMap tempMap = hashMapArr[firstIfdType];
                hashMapArr[firstIfdType] = hashMapArr[secondIfdType];
                hashMapArr[secondIfdType] = tempMap;
            }
        } else if (DEBUG) {
            Log.d(str, "Second image does not contain valid size information");
        }
    }

    private boolean containsMatch(byte[] mainBytes, byte[] findBytes) {
        int i = 0;
        while (i < mainBytes.length - findBytes.length) {
            int j = 0;
            while (j < findBytes.length && mainBytes[i + j] == findBytes[j]) {
                if (j == findBytes.length - 1) {
                    return true;
                }
                j++;
            }
            i++;
        }
        return false;
    }

    private static long[] convertToLongArray(Object inputObj) {
        if (inputObj instanceof int[]) {
            int[] input = (int[]) inputObj;
            long[] result = new long[input.length];
            for (int i = 0; i < input.length; i++) {
                result[i] = (long) input[i];
            }
            return result;
        } else if (inputObj instanceof long[]) {
            return (long[]) inputObj;
        } else {
            return null;
        }
    }
}
