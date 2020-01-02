package android.provider;

import android.media.ExifInterface;
import android.os.Bundle;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MetadataReader {
    private static final String[] DEFAULT_EXIF_TAGS = new String[]{"FNumber", ExifInterface.TAG_COPYRIGHT, "DateTime", ExifInterface.TAG_EXPOSURE_TIME, ExifInterface.TAG_FOCAL_LENGTH, "FNumber", ExifInterface.TAG_GPS_LATITUDE, ExifInterface.TAG_GPS_LATITUDE_REF, ExifInterface.TAG_GPS_LONGITUDE, ExifInterface.TAG_GPS_LONGITUDE_REF, ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.TAG_IMAGE_WIDTH, "ISOSpeedRatings", ExifInterface.TAG_MAKE, ExifInterface.TAG_MODEL, ExifInterface.TAG_ORIENTATION, ExifInterface.TAG_SHUTTER_SPEED_VALUE};
    private static final String JPEG_MIME_TYPE = "image/jpeg";
    private static final String JPG_MIME_TYPE = "image/jpg";
    private static final int TYPE_DOUBLE = 1;
    private static final int TYPE_INT = 0;
    private static final Map<String, Integer> TYPE_MAPPING = new HashMap();
    private static final int TYPE_STRING = 2;

    private MetadataReader() {
    }

    static {
        Map map = TYPE_MAPPING;
        Integer valueOf = Integer.valueOf(2);
        map.put(ExifInterface.TAG_ARTIST, valueOf);
        map = TYPE_MAPPING;
        Integer valueOf2 = Integer.valueOf(0);
        map.put(ExifInterface.TAG_BITS_PER_SAMPLE, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_COMPRESSION, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_COPYRIGHT, valueOf);
        TYPE_MAPPING.put("DateTime", valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_IMAGE_DESCRIPTION, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_IMAGE_LENGTH, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_IMAGE_WIDTH, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_MAKE, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_MODEL, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_ORIENTATION, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_PHOTOMETRIC_INTERPRETATION, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_PLANAR_CONFIGURATION, valueOf2);
        map = TYPE_MAPPING;
        Integer valueOf3 = Integer.valueOf(1);
        map.put(ExifInterface.TAG_PRIMARY_CHROMATICITIES, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_REFERENCE_BLACK_WHITE, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_RESOLUTION_UNIT, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_ROWS_PER_STRIP, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_SAMPLES_PER_PIXEL, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_SOFTWARE, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_STRIP_BYTE_COUNTS, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_STRIP_OFFSETS, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_TRANSFER_FUNCTION, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_WHITE_POINT, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_X_RESOLUTION, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_Y_CB_CR_COEFFICIENTS, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_Y_CB_CR_POSITIONING, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_Y_CB_CR_SUB_SAMPLING, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_Y_RESOLUTION, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_APERTURE_VALUE, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_BRIGHTNESS_VALUE, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_CFA_PATTERN, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_COLOR_SPACE, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_COMPONENTS_CONFIGURATION, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_COMPRESSED_BITS_PER_PIXEL, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_CONTRAST, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_CUSTOM_RENDERED, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_DATETIME_DIGITIZED, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_DATETIME_ORIGINAL, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_DEVICE_SETTING_DESCRIPTION, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_DIGITAL_ZOOM_RATIO, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_EXIF_VERSION, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_EXPOSURE_BIAS_VALUE, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_EXPOSURE_INDEX, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_EXPOSURE_MODE, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_EXPOSURE_PROGRAM, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_EXPOSURE_TIME, valueOf3);
        TYPE_MAPPING.put("FNumber", valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_FILE_SOURCE, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_FLASH, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_FLASH_ENERGY, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_FLASHPIX_VERSION, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_FOCAL_LENGTH, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_FOCAL_LENGTH_IN_35MM_FILM, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_FOCAL_PLANE_RESOLUTION_UNIT, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_FOCAL_PLANE_X_RESOLUTION, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_FOCAL_PLANE_Y_RESOLUTION, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_GAIN_CONTROL, valueOf2);
        TYPE_MAPPING.put("ISOSpeedRatings", valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_IMAGE_UNIQUE_ID, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_LIGHT_SOURCE, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_MAKER_NOTE, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_MAX_APERTURE_VALUE, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_METERING_MODE, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_NEW_SUBFILE_TYPE, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_OECF, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_PIXEL_X_DIMENSION, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_PIXEL_Y_DIMENSION, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_RELATED_SOUND_FILE, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_SATURATION, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_SCENE_CAPTURE_TYPE, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_SCENE_TYPE, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_SENSING_METHOD, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_SHARPNESS, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_SHUTTER_SPEED_VALUE, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_SPATIAL_FREQUENCY_RESPONSE, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_SPECTRAL_SENSITIVITY, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_SUBFILE_TYPE, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_SUBSEC_TIME, valueOf);
        TYPE_MAPPING.put("SubSecTimeDigitized", valueOf);
        TYPE_MAPPING.put("SubSecTimeOriginal", valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_SUBJECT_AREA, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_SUBJECT_DISTANCE, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_SUBJECT_DISTANCE_RANGE, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_SUBJECT_LOCATION, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_USER_COMMENT, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_WHITE_BALANCE, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_ALTITUDE, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_ALTITUDE_REF, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_AREA_INFORMATION, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_DOP, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_DATESTAMP, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_DEST_BEARING, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_DEST_BEARING_REF, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_DEST_DISTANCE, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_DEST_DISTANCE_REF, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_DEST_LATITUDE, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_DEST_LATITUDE_REF, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_DEST_LONGITUDE, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_DEST_LONGITUDE_REF, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_DIFFERENTIAL, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_IMG_DIRECTION, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_IMG_DIRECTION_REF, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_LATITUDE, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_LATITUDE_REF, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_LONGITUDE, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_LONGITUDE_REF, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_MAP_DATUM, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_MEASURE_MODE, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_PROCESSING_METHOD, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_SATELLITES, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_SPEED, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_SPEED_REF, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_STATUS, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_TIMESTAMP, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_TRACK, valueOf3);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_TRACK_REF, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_GPS_VERSION_ID, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_INTEROPERABILITY_INDEX, valueOf);
        TYPE_MAPPING.put(ExifInterface.TAG_THUMBNAIL_IMAGE_LENGTH, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_THUMBNAIL_IMAGE_WIDTH, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_DNG_VERSION, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_DEFAULT_CROP_SIZE, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_ORF_PREVIEW_IMAGE_START, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_ORF_PREVIEW_IMAGE_LENGTH, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_ORF_ASPECT_FRAME, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_RW2_SENSOR_BOTTOM_BORDER, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_RW2_SENSOR_LEFT_BORDER, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_RW2_SENSOR_RIGHT_BORDER, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_RW2_SENSOR_TOP_BORDER, valueOf2);
        TYPE_MAPPING.put(ExifInterface.TAG_RW2_ISO, valueOf2);
    }

    public static boolean isSupportedMimeType(String mimeType) {
        return JPG_MIME_TYPE.equals(mimeType) || JPEG_MIME_TYPE.equals(mimeType);
    }

    public static void getMetadata(Bundle metadata, InputStream stream, String mimeType, String[] tags) throws IOException {
        List<String> metadataTypes = new ArrayList();
        if (isSupportedMimeType(mimeType)) {
            Bundle exifData = getExifData(stream, tags);
            if (exifData.size() > 0) {
                String str = DocumentsContract.METADATA_EXIF;
                metadata.putBundle(str, exifData);
                metadataTypes.add(str);
            }
        }
        metadata.putStringArray(DocumentsContract.METADATA_TYPES, (String[]) metadataTypes.toArray(new String[metadataTypes.size()]));
    }

    private static Bundle getExifData(InputStream stream, String[] tags) throws IOException {
        if (tags == null) {
            tags = DEFAULT_EXIF_TAGS;
        }
        ExifInterface exifInterface = new ExifInterface(stream);
        Bundle exif = new Bundle();
        for (String tag : tags) {
            if (((Integer) TYPE_MAPPING.get(tag)).equals(Integer.valueOf(0))) {
                int data = exifInterface.getAttributeInt(tag, Integer.MIN_VALUE);
                if (data != Integer.MIN_VALUE) {
                    exif.putInt(tag, data);
                }
            } else if (((Integer) TYPE_MAPPING.get(tag)).equals(Integer.valueOf(1))) {
                double data2 = exifInterface.getAttributeDouble(tag, Double.MIN_VALUE);
                if (data2 != Double.MIN_VALUE) {
                    exif.putDouble(tag, data2);
                }
            } else if (((Integer) TYPE_MAPPING.get(tag)).equals(Integer.valueOf(2))) {
                String data3 = exifInterface.getAttribute(tag);
                if (data3 != null) {
                    exif.putString(tag, data3);
                }
            }
        }
        return exif;
    }
}
