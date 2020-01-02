package android.util.proto;

import android.net.wifi.WifiEnterpriseConfig;
import com.android.internal.logging.EventLogTags;

public abstract class ProtoStream {
    public static final long FIELD_COUNT_MASK = 16492674416640L;
    public static final long FIELD_COUNT_PACKED = 5497558138880L;
    public static final long FIELD_COUNT_REPEATED = 2199023255552L;
    public static final int FIELD_COUNT_SHIFT = 40;
    public static final long FIELD_COUNT_SINGLE = 1099511627776L;
    public static final long FIELD_COUNT_UNKNOWN = 0;
    public static final int FIELD_ID_MASK = -8;
    public static final int FIELD_ID_SHIFT = 3;
    public static final long FIELD_TYPE_BOOL = 34359738368L;
    public static final long FIELD_TYPE_BYTES = 51539607552L;
    public static final long FIELD_TYPE_DOUBLE = 4294967296L;
    public static final long FIELD_TYPE_ENUM = 60129542144L;
    public static final long FIELD_TYPE_FIXED32 = 30064771072L;
    public static final long FIELD_TYPE_FIXED64 = 25769803776L;
    public static final long FIELD_TYPE_FLOAT = 8589934592L;
    public static final long FIELD_TYPE_INT32 = 21474836480L;
    public static final long FIELD_TYPE_INT64 = 12884901888L;
    public static final long FIELD_TYPE_MASK = 1095216660480L;
    public static final long FIELD_TYPE_MESSAGE = 47244640256L;
    protected static final String[] FIELD_TYPE_NAMES = new String[]{"Double", "Float", "Int64", "UInt64", "Int32", "Fixed64", "Fixed32", "Bool", "String", "Group", "Message", "Bytes", "UInt32", "Enum", "SFixed32", "SFixed64", "SInt32", "SInt64"};
    public static final long FIELD_TYPE_SFIXED32 = 64424509440L;
    public static final long FIELD_TYPE_SFIXED64 = 68719476736L;
    public static final int FIELD_TYPE_SHIFT = 32;
    public static final long FIELD_TYPE_SINT32 = 73014444032L;
    public static final long FIELD_TYPE_SINT64 = 77309411328L;
    public static final long FIELD_TYPE_STRING = 38654705664L;
    public static final long FIELD_TYPE_UINT32 = 55834574848L;
    public static final long FIELD_TYPE_UINT64 = 17179869184L;
    public static final long FIELD_TYPE_UNKNOWN = 0;
    public static final int WIRE_TYPE_END_GROUP = 4;
    public static final int WIRE_TYPE_FIXED32 = 5;
    public static final int WIRE_TYPE_FIXED64 = 1;
    public static final int WIRE_TYPE_LENGTH_DELIMITED = 2;
    public static final int WIRE_TYPE_MASK = 7;
    public static final int WIRE_TYPE_START_GROUP = 3;
    public static final int WIRE_TYPE_VARINT = 0;

    public static String getFieldTypeString(long fieldType) {
        int index = ((int) ((FIELD_TYPE_MASK & fieldType) >>> 32)) - 1;
        if (index >= 0) {
            String[] strArr = FIELD_TYPE_NAMES;
            if (index < strArr.length) {
                return strArr[index];
            }
        }
        return null;
    }

    public static String getFieldCountString(long fieldCount) {
        if (fieldCount == 1099511627776L) {
            return "";
        }
        if (fieldCount == FIELD_COUNT_REPEATED) {
            return "Repeated";
        }
        if (fieldCount == FIELD_COUNT_PACKED) {
            return "Packed";
        }
        return null;
    }

    public static String getWireTypeString(int wireType) {
        if (wireType == 0) {
            return "Varint";
        }
        if (wireType == 1) {
            return "Fixed64";
        }
        if (wireType == 2) {
            return "Length Delimited";
        }
        if (wireType == 3) {
            return "Start Group";
        }
        if (wireType == 4) {
            return "End Group";
        }
        if (wireType != 5) {
            return null;
        }
        return "Fixed32";
    }

    public static String getFieldIdString(long fieldId) {
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        long fieldCount = FIELD_COUNT_MASK & fieldId;
        String countString = getFieldCountString(fieldCount);
        if (countString == null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("fieldCount=");
            stringBuilder.append(fieldCount);
            countString = stringBuilder.toString();
        }
        if (countString.length() > 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(countString);
            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            countString = stringBuilder.toString();
        }
        long fieldType = FIELD_TYPE_MASK & fieldId;
        String typeString = getFieldTypeString(fieldType);
        if (typeString == null) {
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("fieldType=");
            stringBuilder2.append(fieldType);
            typeString = stringBuilder2.toString();
        }
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(countString);
        stringBuilder2.append(typeString);
        stringBuilder2.append(" tag=");
        stringBuilder2.append((int) fieldId);
        stringBuilder2.append(" fieldId=0x");
        stringBuilder2.append(Long.toHexString(fieldId));
        return stringBuilder2.toString();
    }

    public static long makeFieldId(int id, long fieldFlags) {
        return (((long) id) & 4294967295L) | fieldFlags;
    }

    public static long makeToken(int tagSize, boolean repeated, int depth, int objectId, int offset) {
        return (((((((long) tagSize) & 7) << 61) | (repeated ? 1152921504606846976L : 0)) | ((511 & ((long) depth)) << 51)) | ((524287 & ((long) objectId)) << 32)) | (4294967295L & ((long) offset));
    }

    public static int getTagSizeFromToken(long token) {
        return (int) ((token >> 61) & 7);
    }

    public static boolean getRepeatedFromToken(long token) {
        return ((token >> 60) & 1) != 0;
    }

    public static int getDepthFromToken(long token) {
        return (int) ((token >> 51) & 511);
    }

    public static int getObjectIdFromToken(long token) {
        return (int) ((token >> 32) & 524287);
    }

    public static int getOffsetFromToken(long token) {
        return (int) token;
    }

    public static int convertObjectIdToOrdinal(int objectId) {
        return EventLogTags.SYSUI_VIEW_VISIBILITY - objectId;
    }

    public static String token2String(long token) {
        if (token == 0) {
            return "Token(0)";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Token(val=0x");
        stringBuilder.append(Long.toHexString(token));
        stringBuilder.append(" depth=");
        stringBuilder.append(getDepthFromToken(token));
        stringBuilder.append(" object=");
        stringBuilder.append(convertObjectIdToOrdinal(getObjectIdFromToken(token)));
        stringBuilder.append(" tagSize=");
        stringBuilder.append(getTagSizeFromToken(token));
        stringBuilder.append(" offset=");
        stringBuilder.append(getOffsetFromToken(token));
        stringBuilder.append(')');
        return stringBuilder.toString();
    }
}
