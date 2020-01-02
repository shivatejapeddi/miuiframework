package com.android.framework.protobuf;

import com.miui.mishare.DeviceModel.Xiaomi;

final class TextFormatEscaper {

    private interface ByteSequence {
        byte byteAt(int i);

        int size();
    }

    private TextFormatEscaper() {
    }

    static String escapeBytes(ByteSequence input) {
        StringBuilder builder = new StringBuilder(input.size());
        for (int i = 0; i < input.size(); i++) {
            byte b = input.byteAt(i);
            if (b == (byte) 34) {
                builder.append("\\\"");
            } else if (b == Xiaomi.MANUFACTURE_END) {
                builder.append("\\'");
            } else if (b != (byte) 92) {
                switch (b) {
                    case (byte) 7:
                        builder.append("\\a");
                        break;
                    case (byte) 8:
                        builder.append("\\b");
                        break;
                    case (byte) 9:
                        builder.append("\\t");
                        break;
                    case (byte) 10:
                        builder.append("\\n");
                        break;
                    case (byte) 11:
                        builder.append("\\v");
                        break;
                    case (byte) 12:
                        builder.append("\\f");
                        break;
                    case (byte) 13:
                        builder.append("\\r");
                        break;
                    default:
                        if (b >= (byte) 32 && b <= (byte) 126) {
                            builder.append((char) b);
                            break;
                        }
                        builder.append('\\');
                        builder.append((char) (((b >>> 6) & 3) + 48));
                        builder.append((char) (((b >>> 3) & 7) + 48));
                        builder.append((char) ((b & 7) + 48));
                        break;
                        break;
                }
            } else {
                builder.append("\\\\");
            }
        }
        return builder.toString();
    }

    static String escapeBytes(final ByteString input) {
        return escapeBytes(new ByteSequence() {
            public int size() {
                return input.size();
            }

            public byte byteAt(int offset) {
                return input.byteAt(offset);
            }
        });
    }

    static String escapeBytes(final byte[] input) {
        return escapeBytes(new ByteSequence() {
            public int size() {
                return input.length;
            }

            public byte byteAt(int offset) {
                return input[offset];
            }
        });
    }

    static String escapeText(String input) {
        return escapeBytes(ByteString.copyFromUtf8(input));
    }

    static String escapeDoubleQuotesAndBackslashes(String input) {
        return input.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
