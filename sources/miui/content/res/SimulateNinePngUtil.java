package miui.content.res;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.zip.CRC32;

public class SimulateNinePngUtil {
    private static byte[] PNG_HEAD_FORMAT = new byte[]{(byte) -119, (byte) 80, (byte) 78, (byte) 71, (byte) 13, (byte) 10, (byte) 26, (byte) 10};

    private static class NinePathInputStream extends InputStream {
        private int mCount = 0;
        private byte[] mExtraHeaderData;
        private InputStream mInputStream;

        public NinePathInputStream(InputStream is, byte[] header) {
            this.mInputStream = is;
            this.mExtraHeaderData = header;
            this.mCount = 0;
        }

        public int read() throws IOException {
            int i = this.mCount;
            byte[] bArr = this.mExtraHeaderData;
            if (i >= bArr.length) {
                return this.mInputStream.read();
            }
            this.mCount = i + 1;
            return bArr[i];
        }

        /* JADX WARNING: Removed duplicated region for block: B:10:? A:{SYNTHETIC, RETURN} */
        /* JADX WARNING: Removed duplicated region for block: B:6:0x001d  */
        public int read(byte[] r6, int r7, int r8) throws java.io.IOException {
            /*
            r5 = this;
            r0 = r6.length;
            checkOffsetAndCount(r0, r7, r8);
            r0 = 0;
        L_0x0005:
            r1 = r5.mCount;
            r2 = r5.mExtraHeaderData;
            r3 = r2.length;
            if (r1 >= r3) goto L_0x001b;
        L_0x000c:
            if (r0 >= r8) goto L_0x001b;
        L_0x000e:
            r3 = r0 + 1;
            r0 = r0 + r7;
            r4 = r1 + 1;
            r5.mCount = r4;
            r1 = r2[r1];
            r6[r0] = r1;
            r0 = r3;
            goto L_0x0005;
        L_0x001b:
            if (r0 >= r8) goto L_0x0028;
        L_0x001d:
            r1 = r5.mInputStream;
            r2 = r7 + r0;
            r3 = r8 - r0;
            r1 = r1.read(r6, r2, r3);
            r0 = r0 + r1;
        L_0x0028:
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: miui.content.res.SimulateNinePngUtil$NinePathInputStream.read(byte[], int, int):int");
        }

        public void close() throws IOException {
            InputStream inputStream = this.mInputStream;
            if (inputStream != null) {
                inputStream.close();
            }
        }

        private static void checkOffsetAndCount(int arrayLength, int offset, int count) {
            if ((offset | count) < 0 || offset > arrayLength || arrayLength - offset < count) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("arrayLength=");
                stringBuilder.append(arrayLength);
                stringBuilder.append("offset=");
                stringBuilder.append(offset);
                stringBuilder.append("count=");
                stringBuilder.append(count);
                throw new ArrayIndexOutOfBoundsException(stringBuilder.toString());
            }
        }
    }

    public static InputStream convertIntoNinePngStream(InputStream pngInputStream) {
        try {
            byte[] srcHeader = new byte[41];
            int n = pngInputStream.read(srcHeader);
            if (n <= 0) {
                srcHeader = null;
            } else if (n < srcHeader.length) {
                srcHeader = Arrays.copyOf(srcHeader, n);
            }
            byte[] nineHeader = convertIntoNinePngData(srcHeader);
            if (nineHeader != null) {
                return new NinePathInputStream(pngInputStream, nineHeader);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] convertIntoNinePngData(byte[] srcData) {
        if (srcData == null || srcData.length < 41 || !isPngFormat(srcData)) {
            return null;
        }
        if (isNinePngFormat(srcData)) {
            return srcData;
        }
        int i;
        byte[] ninePatchChunk = getNinePatchChunk(srcData);
        byte[] destData = new byte[((srcData.length + 12) + ninePatchChunk.length)];
        for (int i2 = 0; i2 < 33; i2++) {
            destData[i2] = srcData[i2];
        }
        convertBytesFromIntByBigEndian(destData, 33, ninePatchChunk.length);
        fillNinePngFormatMark(destData);
        for (i = 0; i < ninePatchChunk.length; i++) {
            destData[i + 41] = ninePatchChunk[i];
        }
        i = ninePatchChunk.length + 41;
        CRC32 crc = new CRC32();
        crc.update(destData, 37, ninePatchChunk.length + 4);
        convertBytesFromIntByBigEndian(destData, i, (int) crc.getValue());
        for (int i3 = 0; i3 < srcData.length - 33; i3++) {
            destData[(i + 4) + i3] = srcData[i3 + 33];
        }
        return destData;
    }

    private static boolean isPngFormat(byte[] data) {
        int i = 0;
        while (true) {
            byte[] bArr = PNG_HEAD_FORMAT;
            if (i >= bArr.length) {
                return true;
            }
            if (data[i] != bArr[i]) {
                return false;
            }
            i++;
        }
    }

    private static boolean isNinePngFormat(byte[] data) {
        return data != null && data.length > 40 && data[37] == (byte) 110 && data[38] == (byte) 112 && data[39] == (byte) 84 && data[40] == (byte) 99;
    }

    private static void fillNinePngFormatMark(byte[] data) {
        data[37] = (byte) 110;
        data[38] = (byte) 112;
        data[39] = (byte) 84;
        data[40] = (byte) 99;
    }

    private static byte[] getNinePatchChunk(byte[] srcImageData) {
        int width = convertByteToIntByBigEndian(srcImageData, 16);
        int height = convertByteToIntByBigEndian(srcImageData, 20);
        byte[] chunk = new byte[52];
        chunk[0] = (byte) 1;
        chunk[1] = (byte) 2;
        chunk[2] = (byte) 2;
        chunk[3] = (byte) 1;
        convertBytesFromIntByBigEndian(chunk, 36, width);
        convertBytesFromIntByBigEndian(chunk, 44, height);
        convertBytesFromIntByBigEndian(chunk, 48, computePatchColor(srcImageData));
        return chunk;
    }

    private static int computePatchColor(byte[] srcImageData) {
        return 1;
    }

    private static void convertBytesFromIntByBigEndian(byte[] data, int start, int N) {
        data[start + 0] = (byte) ((N >>> 24) & 255);
        data[start + 1] = (byte) ((N >> 16) & 255);
        data[start + 2] = (byte) ((N >> 8) & 255);
        data[start + 3] = (byte) (N & 255);
    }

    private static int convertByteToIntByBigEndian(byte[] data, int start) {
        return (((0 + ((data[start + 0] & 255) << 24)) + ((data[start + 1] & 255) << 16)) + ((data[start + 2] & 255) << 8)) + (data[start + 3] & 255);
    }
}
