package android.util.proto;

import java.io.IOException;

public class ProtoUtils {
    public static void toAggStatsProto(ProtoOutputStream proto, long fieldId, long min, long average, long max) {
        long aggStatsToken = proto.start(fieldId);
        proto.write(1112396529665L, min);
        proto.write(1112396529666L, average);
        proto.write(1112396529667L, max);
        proto.end(aggStatsToken);
    }

    public static void toDuration(ProtoOutputStream proto, long fieldId, long startMs, long endMs) {
        long token = proto.start(fieldId);
        proto.write(1112396529665L, startMs);
        proto.write(1112396529666L, endMs);
        proto.end(token);
    }

    public static void writeBitWiseFlagsToProtoEnum(ProtoOutputStream proto, long fieldId, int flags, int[] origEnums, int[] protoEnums) {
        if (protoEnums.length == origEnums.length) {
            int len = origEnums.length;
            for (int i = 0; i < len; i++) {
                if (origEnums[i] == 0 && flags == 0) {
                    proto.write(fieldId, protoEnums[i]);
                    return;
                }
                if ((origEnums[i] & flags) != 0) {
                    proto.write(fieldId, protoEnums[i]);
                }
            }
            return;
        }
        throw new IllegalArgumentException("The length of origEnums must match protoEnums");
    }

    public static String currentFieldToString(ProtoInputStream proto) throws IOException {
        StringBuilder sb = new StringBuilder();
        int fieldNumber = proto.getFieldNumber();
        int wireType = proto.getWireType();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Offset : 0x");
        stringBuilder.append(Integer.toHexString(proto.getOffset()));
        sb.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("\nField Number : 0x");
        stringBuilder.append(Integer.toHexString(proto.getFieldNumber()));
        sb.append(stringBuilder.toString());
        sb.append("\nWire Type : ");
        String str = "\nField Value : 0x";
        long fieldConstant;
        StringBuilder stringBuilder2;
        if (wireType == 0) {
            sb.append("varint");
            fieldConstant = ProtoStream.makeFieldId(fieldNumber, 1112396529664L);
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str);
            stringBuilder2.append(Long.toHexString(proto.readLong(fieldConstant)));
            sb.append(stringBuilder2.toString());
        } else if (wireType == 1) {
            sb.append("fixed64");
            fieldConstant = ProtoStream.makeFieldId(fieldNumber, 1125281431552L);
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str);
            stringBuilder2.append(Long.toHexString(proto.readLong(fieldConstant)));
            sb.append(stringBuilder2.toString());
        } else if (wireType == 2) {
            sb.append("length delimited");
            long fieldConstant2 = ProtoStream.makeFieldId(fieldNumber, 1151051235328L);
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("\nField Bytes : ");
            stringBuilder3.append(proto.readBytes(fieldConstant2));
            sb.append(stringBuilder3.toString());
        } else if (wireType == 3) {
            sb.append("start group");
        } else if (wireType == 4) {
            sb.append("end group");
        } else if (wireType != 5) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("unknown(");
            stringBuilder.append(proto.getWireType());
            stringBuilder.append(")");
            sb.append(stringBuilder.toString());
        } else {
            sb.append("fixed32");
            fieldConstant = ProtoStream.makeFieldId(fieldNumber, 1129576398848L);
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str);
            stringBuilder2.append(Integer.toHexString(proto.readInt(fieldConstant)));
            sb.append(stringBuilder2.toString());
        }
        return sb.toString();
    }
}
