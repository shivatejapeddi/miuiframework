package android.net;

import android.text.TextUtils;
import com.android.internal.util.BitUtils;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.List;

public abstract class DnsPacket {
    public static final int ANSECTION = 1;
    public static final int ARSECTION = 3;
    public static final int NSSECTION = 2;
    private static final int NUM_SECTIONS = 4;
    public static final int QDSECTION = 0;
    private static final String TAG = DnsPacket.class.getSimpleName();
    protected final DnsHeader mHeader;
    protected final List<DnsRecord>[] mRecords;

    public class DnsHeader {
        private static final String TAG = "DnsHeader";
        public final int flags;
        public final int id;
        private final int[] mRecordCount = new int[4];
        public final int rcode = (this.flags & 15);

        DnsHeader(ByteBuffer buf) throws BufferUnderflowException {
            this.id = BitUtils.uint16(buf.getShort());
            this.flags = BitUtils.uint16(buf.getShort());
            for (int i = 0; i < 4; i++) {
                this.mRecordCount[i] = BitUtils.uint16(buf.getShort());
            }
        }

        public int getRecordCount(int type) {
            return this.mRecordCount[type];
        }
    }

    public class DnsRecord {
        private static final int MAXLABELCOUNT = 128;
        private static final int MAXLABELSIZE = 63;
        private static final int MAXNAMESIZE = 255;
        private static final int NAME_COMPRESSION = 192;
        private static final int NAME_NORMAL = 0;
        private static final String TAG = "DnsRecord";
        private final DecimalFormat byteFormat = new DecimalFormat();
        public final String dName;
        private final byte[] mRdata;
        public final int nsClass;
        public final int nsType;
        private final FieldPosition pos = new FieldPosition(0);
        public final long ttl;

        DnsRecord(int recordType, ByteBuffer buf) throws BufferUnderflowException, ParseException {
            this.dName = parseName(buf, 0);
            if (this.dName.length() <= 255) {
                this.nsType = BitUtils.uint16(buf.getShort());
                this.nsClass = BitUtils.uint16(buf.getShort());
                if (recordType != 0) {
                    this.ttl = BitUtils.uint32(buf.getInt());
                    this.mRdata = new byte[BitUtils.uint16(buf.getShort())];
                    buf.get(this.mRdata);
                    return;
                }
                this.ttl = 0;
                this.mRdata = null;
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Parse name fail, name size is too long: ");
            stringBuilder.append(this.dName.length());
            throw new ParseException(stringBuilder.toString());
        }

        public byte[] getRR() {
            byte[] bArr = this.mRdata;
            return bArr == null ? null : (byte[]) bArr.clone();
        }

        private String labelToString(byte[] label) {
            StringBuffer sb = new StringBuffer();
            for (int b : label) {
                int b2 = BitUtils.uint8(b2);
                if (b2 <= 32 || b2 >= 127) {
                    sb.append('\\');
                    this.byteFormat.format((long) b2, sb, this.pos);
                } else if (b2 == 34 || b2 == 46 || b2 == 59 || b2 == 92 || b2 == 40 || b2 == 41 || b2 == 64 || b2 == 36) {
                    sb.append('\\');
                    sb.append((char) b2);
                } else {
                    sb.append((char) b2);
                }
            }
            return sb.toString();
        }

        private String parseName(ByteBuffer buf, int depth) throws BufferUnderflowException, ParseException {
            if (depth <= 128) {
                int len = BitUtils.uint8(buf.get());
                int mask = len & 192;
                if (len == 0) {
                    return "";
                }
                String pointed;
                if (mask != 0 && mask != 192) {
                    throw new ParseException("Parse name fail, bad label type");
                } else if (mask == 192) {
                    int offset = ((len & -193) << 8) + BitUtils.uint8(buf.get());
                    int oldPos = buf.position();
                    if (offset < oldPos - 2) {
                        buf.position(offset);
                        pointed = parseName(buf, depth + 1);
                        buf.position(oldPos);
                        return pointed;
                    }
                    throw new ParseException("Parse compression name fail, invalid compression");
                } else {
                    byte[] label = new byte[len];
                    buf.get(label);
                    String head = labelToString(label);
                    if (head.length() <= 63) {
                        String str;
                        pointed = parseName(buf, depth + 1);
                        if (TextUtils.isEmpty(pointed)) {
                            str = head;
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(head);
                            stringBuilder.append(".");
                            stringBuilder.append(pointed);
                            str = stringBuilder.toString();
                        }
                        return str;
                    }
                    throw new ParseException("Parse name fail, invalid label length");
                }
            }
            throw new ParseException("Failed to parse name, too many labels");
        }
    }

    protected DnsPacket(byte[] data) throws ParseException {
        if (data != null) {
            try {
                ByteBuffer buffer = ByteBuffer.wrap(data);
                this.mHeader = new DnsHeader(buffer);
                this.mRecords = new ArrayList[4];
                for (int i = 0; i < 4; i++) {
                    int count = this.mHeader.getRecordCount(i);
                    if (count > 0) {
                        this.mRecords[i] = new ArrayList(count);
                    }
                    int j = 0;
                    while (j < count) {
                        try {
                            this.mRecords[i].add(new DnsRecord(i, buffer));
                            j++;
                        } catch (BufferUnderflowException e) {
                            throw new ParseException("Parse record fail", e);
                        }
                    }
                }
                return;
            } catch (BufferUnderflowException e2) {
                throw new ParseException("Parse Header fail, bad input data", e2);
            }
        }
        throw new ParseException("Parse header failed, null input data");
    }
}
