package android.util.proto;

import android.provider.Telephony.BaseMmsColumns;
import android.util.Log;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import org.apache.miui.commons.lang3.ClassUtils;

public final class ProtoOutputStream extends ProtoStream {
    public static final String TAG = "ProtoOutputStream";
    private EncodedBuffer mBuffer;
    private boolean mCompacted;
    private int mCopyBegin;
    private int mDepth;
    private long mExpectedObjectToken;
    private int mNextObjectId;
    private OutputStream mStream;

    public ProtoOutputStream() {
        this(0);
    }

    public ProtoOutputStream(int chunkSize) {
        this.mNextObjectId = -1;
        this.mBuffer = new EncodedBuffer(chunkSize);
    }

    public ProtoOutputStream(OutputStream stream) {
        this();
        this.mStream = stream;
    }

    public ProtoOutputStream(FileDescriptor fd) {
        this(new FileOutputStream(fd));
    }

    public int getRawSize() {
        if (this.mCompacted) {
            return getBytes().length;
        }
        return this.mBuffer.getSize();
    }

    /* JADX WARNING: Missing block: B:9:0x003f, code skipped:
            writeRepeatedSInt64Impl(r0, (long) r9);
     */
    /* JADX WARNING: Missing block: B:10:0x0045, code skipped:
            writeRepeatedSInt32Impl(r0, (int) r9);
     */
    /* JADX WARNING: Missing block: B:11:0x004b, code skipped:
            writeRepeatedSFixed64Impl(r0, (long) r9);
     */
    /* JADX WARNING: Missing block: B:12:0x0051, code skipped:
            writeRepeatedSFixed32Impl(r0, (int) r9);
     */
    /* JADX WARNING: Missing block: B:13:0x0057, code skipped:
            writeRepeatedEnumImpl(r0, (int) r9);
     */
    /* JADX WARNING: Missing block: B:14:0x005d, code skipped:
            writeRepeatedUInt32Impl(r0, (int) r9);
     */
    /* JADX WARNING: Missing block: B:16:0x0065, code skipped:
            if (r9 == 0.0d) goto L_0x0068;
     */
    /* JADX WARNING: Missing block: B:17:0x0068, code skipped:
            r2 = false;
     */
    /* JADX WARNING: Missing block: B:18:0x0069, code skipped:
            writeRepeatedBoolImpl(r0, r2);
     */
    /* JADX WARNING: Missing block: B:19:0x006e, code skipped:
            writeRepeatedFixed32Impl(r0, (int) r9);
     */
    /* JADX WARNING: Missing block: B:20:0x0074, code skipped:
            writeRepeatedFixed64Impl(r0, (long) r9);
     */
    /* JADX WARNING: Missing block: B:21:0x007a, code skipped:
            writeRepeatedInt32Impl(r0, (int) r9);
     */
    /* JADX WARNING: Missing block: B:22:0x0080, code skipped:
            writeRepeatedUInt64Impl(r0, (long) r9);
     */
    /* JADX WARNING: Missing block: B:23:0x0086, code skipped:
            writeRepeatedInt64Impl(r0, (long) r9);
     */
    /* JADX WARNING: Missing block: B:24:0x008b, code skipped:
            writeRepeatedFloatImpl(r0, (float) r9);
     */
    /* JADX WARNING: Missing block: B:25:0x0090, code skipped:
            writeRepeatedDoubleImpl(r0, r9);
     */
    /* JADX WARNING: Missing block: B:44:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:45:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:46:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:47:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:48:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:49:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:50:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:51:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:52:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:53:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:54:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:55:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:56:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:57:?, code skipped:
            return;
     */
    public void write(long r7, double r9) {
        /*
        r6 = this;
        r6.assertNotCompacted();
        r0 = (int) r7;
        r1 = 17587891077120; // 0xfff00000000 float:0.0 double:8.689572764003E-311;
        r1 = r1 & r7;
        r3 = 32;
        r1 = r1 >> r3;
        r1 = (int) r1;
        r2 = 1;
        r3 = 0;
        r4 = 0;
        switch(r1) {
            case 257: goto L_0x00da;
            case 258: goto L_0x00d5;
            case 259: goto L_0x00d0;
            case 260: goto L_0x00cb;
            case 261: goto L_0x00c6;
            case 262: goto L_0x00c1;
            case 263: goto L_0x00bc;
            case 264: goto L_0x00b2;
            default: goto L_0x0015;
        };
    L_0x0015:
        switch(r1) {
            case 269: goto L_0x00ad;
            case 270: goto L_0x00a8;
            case 271: goto L_0x00a3;
            case 272: goto L_0x009e;
            case 273: goto L_0x0099;
            case 274: goto L_0x0094;
            default: goto L_0x0018;
        };
    L_0x0018:
        switch(r1) {
            case 513: goto L_0x0090;
            case 514: goto L_0x008b;
            case 515: goto L_0x0086;
            case 516: goto L_0x0080;
            case 517: goto L_0x007a;
            case 518: goto L_0x0074;
            case 519: goto L_0x006e;
            case 520: goto L_0x0063;
            default: goto L_0x001b;
        };
    L_0x001b:
        switch(r1) {
            case 525: goto L_0x005d;
            case 526: goto L_0x0057;
            case 527: goto L_0x0051;
            case 528: goto L_0x004b;
            case 529: goto L_0x0045;
            case 530: goto L_0x003f;
            default: goto L_0x001e;
        };
    L_0x001e:
        switch(r1) {
            case 1281: goto L_0x0090;
            case 1282: goto L_0x008b;
            case 1283: goto L_0x0086;
            case 1284: goto L_0x0080;
            case 1285: goto L_0x007a;
            case 1286: goto L_0x0074;
            case 1287: goto L_0x006e;
            case 1288: goto L_0x0063;
            default: goto L_0x0021;
        };
    L_0x0021:
        switch(r1) {
            case 1293: goto L_0x005d;
            case 1294: goto L_0x0057;
            case 1295: goto L_0x0051;
            case 1296: goto L_0x004b;
            case 1297: goto L_0x0045;
            case 1298: goto L_0x003f;
            default: goto L_0x0024;
        };
    L_0x0024:
        r1 = new java.lang.IllegalArgumentException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Attempt to call write(long, double) with ";
        r2.append(r3);
        r3 = android.util.proto.ProtoStream.getFieldIdString(r7);
        r2.append(r3);
        r2 = r2.toString();
        r1.<init>(r2);
        throw r1;
    L_0x003f:
        r1 = (long) r9;
        r6.writeRepeatedSInt64Impl(r0, r1);
        goto L_0x00de;
    L_0x0045:
        r1 = (int) r9;
        r6.writeRepeatedSInt32Impl(r0, r1);
        goto L_0x00de;
    L_0x004b:
        r1 = (long) r9;
        r6.writeRepeatedSFixed64Impl(r0, r1);
        goto L_0x00de;
    L_0x0051:
        r1 = (int) r9;
        r6.writeRepeatedSFixed32Impl(r0, r1);
        goto L_0x00de;
    L_0x0057:
        r1 = (int) r9;
        r6.writeRepeatedEnumImpl(r0, r1);
        goto L_0x00de;
    L_0x005d:
        r1 = (int) r9;
        r6.writeRepeatedUInt32Impl(r0, r1);
        goto L_0x00de;
    L_0x0063:
        r1 = (r9 > r4 ? 1 : (r9 == r4 ? 0 : -1));
        if (r1 == 0) goto L_0x0068;
    L_0x0067:
        goto L_0x0069;
    L_0x0068:
        r2 = r3;
    L_0x0069:
        r6.writeRepeatedBoolImpl(r0, r2);
        goto L_0x00de;
    L_0x006e:
        r1 = (int) r9;
        r6.writeRepeatedFixed32Impl(r0, r1);
        goto L_0x00de;
    L_0x0074:
        r1 = (long) r9;
        r6.writeRepeatedFixed64Impl(r0, r1);
        goto L_0x00de;
    L_0x007a:
        r1 = (int) r9;
        r6.writeRepeatedInt32Impl(r0, r1);
        goto L_0x00de;
    L_0x0080:
        r1 = (long) r9;
        r6.writeRepeatedUInt64Impl(r0, r1);
        goto L_0x00de;
    L_0x0086:
        r1 = (long) r9;
        r6.writeRepeatedInt64Impl(r0, r1);
        goto L_0x00de;
    L_0x008b:
        r1 = (float) r9;
        r6.writeRepeatedFloatImpl(r0, r1);
        goto L_0x00de;
    L_0x0090:
        r6.writeRepeatedDoubleImpl(r0, r9);
        goto L_0x00de;
    L_0x0094:
        r1 = (long) r9;
        r6.writeSInt64Impl(r0, r1);
        goto L_0x00de;
    L_0x0099:
        r1 = (int) r9;
        r6.writeSInt32Impl(r0, r1);
        goto L_0x00de;
    L_0x009e:
        r1 = (long) r9;
        r6.writeSFixed64Impl(r0, r1);
        goto L_0x00de;
    L_0x00a3:
        r1 = (int) r9;
        r6.writeSFixed32Impl(r0, r1);
        goto L_0x00de;
    L_0x00a8:
        r1 = (int) r9;
        r6.writeEnumImpl(r0, r1);
        goto L_0x00de;
    L_0x00ad:
        r1 = (int) r9;
        r6.writeUInt32Impl(r0, r1);
        goto L_0x00de;
    L_0x00b2:
        r1 = (r9 > r4 ? 1 : (r9 == r4 ? 0 : -1));
        if (r1 == 0) goto L_0x00b7;
    L_0x00b6:
        goto L_0x00b8;
    L_0x00b7:
        r2 = r3;
    L_0x00b8:
        r6.writeBoolImpl(r0, r2);
        goto L_0x00de;
    L_0x00bc:
        r1 = (int) r9;
        r6.writeFixed32Impl(r0, r1);
        goto L_0x00de;
    L_0x00c1:
        r1 = (long) r9;
        r6.writeFixed64Impl(r0, r1);
        goto L_0x00de;
    L_0x00c6:
        r1 = (int) r9;
        r6.writeInt32Impl(r0, r1);
        goto L_0x00de;
    L_0x00cb:
        r1 = (long) r9;
        r6.writeUInt64Impl(r0, r1);
        goto L_0x00de;
    L_0x00d0:
        r1 = (long) r9;
        r6.writeInt64Impl(r0, r1);
        goto L_0x00de;
    L_0x00d5:
        r1 = (float) r9;
        r6.writeFloatImpl(r0, r1);
        goto L_0x00de;
    L_0x00da:
        r6.writeDoubleImpl(r0, r9);
    L_0x00de:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.proto.ProtoOutputStream.write(long, double):void");
    }

    /* JADX WARNING: Missing block: B:9:0x003e, code skipped:
            writeRepeatedSInt64Impl(r0, (long) r8);
     */
    /* JADX WARNING: Missing block: B:10:0x0044, code skipped:
            writeRepeatedSInt32Impl(r0, (int) r8);
     */
    /* JADX WARNING: Missing block: B:11:0x004a, code skipped:
            writeRepeatedSFixed64Impl(r0, (long) r8);
     */
    /* JADX WARNING: Missing block: B:12:0x0050, code skipped:
            writeRepeatedSFixed32Impl(r0, (int) r8);
     */
    /* JADX WARNING: Missing block: B:13:0x0056, code skipped:
            writeRepeatedEnumImpl(r0, (int) r8);
     */
    /* JADX WARNING: Missing block: B:14:0x005c, code skipped:
            writeRepeatedUInt32Impl(r0, (int) r8);
     */
    /* JADX WARNING: Missing block: B:16:0x0064, code skipped:
            if (r8 == 0.0f) goto L_0x0067;
     */
    /* JADX WARNING: Missing block: B:17:0x0067, code skipped:
            r2 = false;
     */
    /* JADX WARNING: Missing block: B:18:0x0068, code skipped:
            writeRepeatedBoolImpl(r0, r2);
     */
    /* JADX WARNING: Missing block: B:19:0x006d, code skipped:
            writeRepeatedFixed32Impl(r0, (int) r8);
     */
    /* JADX WARNING: Missing block: B:20:0x0073, code skipped:
            writeRepeatedFixed64Impl(r0, (long) r8);
     */
    /* JADX WARNING: Missing block: B:21:0x0079, code skipped:
            writeRepeatedInt32Impl(r0, (int) r8);
     */
    /* JADX WARNING: Missing block: B:22:0x007f, code skipped:
            writeRepeatedUInt64Impl(r0, (long) r8);
     */
    /* JADX WARNING: Missing block: B:23:0x0085, code skipped:
            writeRepeatedInt64Impl(r0, (long) r8);
     */
    /* JADX WARNING: Missing block: B:24:0x008a, code skipped:
            writeRepeatedFloatImpl(r0, r8);
     */
    /* JADX WARNING: Missing block: B:25:0x008e, code skipped:
            writeRepeatedDoubleImpl(r0, (double) r8);
     */
    /* JADX WARNING: Missing block: B:44:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:45:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:46:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:47:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:48:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:49:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:50:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:51:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:52:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:53:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:54:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:55:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:56:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:57:?, code skipped:
            return;
     */
    public void write(long r6, float r8) {
        /*
        r5 = this;
        r5.assertNotCompacted();
        r0 = (int) r6;
        r1 = 17587891077120; // 0xfff00000000 float:0.0 double:8.689572764003E-311;
        r1 = r1 & r6;
        r3 = 32;
        r1 = r1 >> r3;
        r1 = (int) r1;
        r2 = 1;
        r3 = 0;
        r4 = 0;
        switch(r1) {
            case 257: goto L_0x00d8;
            case 258: goto L_0x00d4;
            case 259: goto L_0x00cf;
            case 260: goto L_0x00ca;
            case 261: goto L_0x00c5;
            case 262: goto L_0x00c0;
            case 263: goto L_0x00bb;
            case 264: goto L_0x00b1;
            default: goto L_0x0014;
        };
    L_0x0014:
        switch(r1) {
            case 269: goto L_0x00ac;
            case 270: goto L_0x00a7;
            case 271: goto L_0x00a2;
            case 272: goto L_0x009d;
            case 273: goto L_0x0098;
            case 274: goto L_0x0093;
            default: goto L_0x0017;
        };
    L_0x0017:
        switch(r1) {
            case 513: goto L_0x008e;
            case 514: goto L_0x008a;
            case 515: goto L_0x0085;
            case 516: goto L_0x007f;
            case 517: goto L_0x0079;
            case 518: goto L_0x0073;
            case 519: goto L_0x006d;
            case 520: goto L_0x0062;
            default: goto L_0x001a;
        };
    L_0x001a:
        switch(r1) {
            case 525: goto L_0x005c;
            case 526: goto L_0x0056;
            case 527: goto L_0x0050;
            case 528: goto L_0x004a;
            case 529: goto L_0x0044;
            case 530: goto L_0x003e;
            default: goto L_0x001d;
        };
    L_0x001d:
        switch(r1) {
            case 1281: goto L_0x008e;
            case 1282: goto L_0x008a;
            case 1283: goto L_0x0085;
            case 1284: goto L_0x007f;
            case 1285: goto L_0x0079;
            case 1286: goto L_0x0073;
            case 1287: goto L_0x006d;
            case 1288: goto L_0x0062;
            default: goto L_0x0020;
        };
    L_0x0020:
        switch(r1) {
            case 1293: goto L_0x005c;
            case 1294: goto L_0x0056;
            case 1295: goto L_0x0050;
            case 1296: goto L_0x004a;
            case 1297: goto L_0x0044;
            case 1298: goto L_0x003e;
            default: goto L_0x0023;
        };
    L_0x0023:
        r1 = new java.lang.IllegalArgumentException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Attempt to call write(long, float) with ";
        r2.append(r3);
        r3 = android.util.proto.ProtoStream.getFieldIdString(r6);
        r2.append(r3);
        r2 = r2.toString();
        r1.<init>(r2);
        throw r1;
    L_0x003e:
        r1 = (long) r8;
        r5.writeRepeatedSInt64Impl(r0, r1);
        goto L_0x00dd;
    L_0x0044:
        r1 = (int) r8;
        r5.writeRepeatedSInt32Impl(r0, r1);
        goto L_0x00dd;
    L_0x004a:
        r1 = (long) r8;
        r5.writeRepeatedSFixed64Impl(r0, r1);
        goto L_0x00dd;
    L_0x0050:
        r1 = (int) r8;
        r5.writeRepeatedSFixed32Impl(r0, r1);
        goto L_0x00dd;
    L_0x0056:
        r1 = (int) r8;
        r5.writeRepeatedEnumImpl(r0, r1);
        goto L_0x00dd;
    L_0x005c:
        r1 = (int) r8;
        r5.writeRepeatedUInt32Impl(r0, r1);
        goto L_0x00dd;
    L_0x0062:
        r1 = (r8 > r4 ? 1 : (r8 == r4 ? 0 : -1));
        if (r1 == 0) goto L_0x0067;
    L_0x0066:
        goto L_0x0068;
    L_0x0067:
        r2 = r3;
    L_0x0068:
        r5.writeRepeatedBoolImpl(r0, r2);
        goto L_0x00dd;
    L_0x006d:
        r1 = (int) r8;
        r5.writeRepeatedFixed32Impl(r0, r1);
        goto L_0x00dd;
    L_0x0073:
        r1 = (long) r8;
        r5.writeRepeatedFixed64Impl(r0, r1);
        goto L_0x00dd;
    L_0x0079:
        r1 = (int) r8;
        r5.writeRepeatedInt32Impl(r0, r1);
        goto L_0x00dd;
    L_0x007f:
        r1 = (long) r8;
        r5.writeRepeatedUInt64Impl(r0, r1);
        goto L_0x00dd;
    L_0x0085:
        r1 = (long) r8;
        r5.writeRepeatedInt64Impl(r0, r1);
        goto L_0x00dd;
    L_0x008a:
        r5.writeRepeatedFloatImpl(r0, r8);
        goto L_0x00dd;
    L_0x008e:
        r1 = (double) r8;
        r5.writeRepeatedDoubleImpl(r0, r1);
        goto L_0x00dd;
    L_0x0093:
        r1 = (long) r8;
        r5.writeSInt64Impl(r0, r1);
        goto L_0x00dd;
    L_0x0098:
        r1 = (int) r8;
        r5.writeSInt32Impl(r0, r1);
        goto L_0x00dd;
    L_0x009d:
        r1 = (long) r8;
        r5.writeSFixed64Impl(r0, r1);
        goto L_0x00dd;
    L_0x00a2:
        r1 = (int) r8;
        r5.writeSFixed32Impl(r0, r1);
        goto L_0x00dd;
    L_0x00a7:
        r1 = (int) r8;
        r5.writeEnumImpl(r0, r1);
        goto L_0x00dd;
    L_0x00ac:
        r1 = (int) r8;
        r5.writeUInt32Impl(r0, r1);
        goto L_0x00dd;
    L_0x00b1:
        r1 = (r8 > r4 ? 1 : (r8 == r4 ? 0 : -1));
        if (r1 == 0) goto L_0x00b6;
    L_0x00b5:
        goto L_0x00b7;
    L_0x00b6:
        r2 = r3;
    L_0x00b7:
        r5.writeBoolImpl(r0, r2);
        goto L_0x00dd;
    L_0x00bb:
        r1 = (int) r8;
        r5.writeFixed32Impl(r0, r1);
        goto L_0x00dd;
    L_0x00c0:
        r1 = (long) r8;
        r5.writeFixed64Impl(r0, r1);
        goto L_0x00dd;
    L_0x00c5:
        r1 = (int) r8;
        r5.writeInt32Impl(r0, r1);
        goto L_0x00dd;
    L_0x00ca:
        r1 = (long) r8;
        r5.writeUInt64Impl(r0, r1);
        goto L_0x00dd;
    L_0x00cf:
        r1 = (long) r8;
        r5.writeInt64Impl(r0, r1);
        goto L_0x00dd;
    L_0x00d4:
        r5.writeFloatImpl(r0, r8);
        goto L_0x00dd;
    L_0x00d8:
        r1 = (double) r8;
        r5.writeDoubleImpl(r0, r1);
    L_0x00dd:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.proto.ProtoOutputStream.write(long, float):void");
    }

    /* JADX WARNING: Missing block: B:9:0x003d, code skipped:
            writeRepeatedSInt64Impl(r0, (long) r7);
     */
    /* JADX WARNING: Missing block: B:10:0x0043, code skipped:
            writeRepeatedSInt32Impl(r0, r7);
     */
    /* JADX WARNING: Missing block: B:11:0x0048, code skipped:
            writeRepeatedSFixed64Impl(r0, (long) r7);
     */
    /* JADX WARNING: Missing block: B:12:0x004e, code skipped:
            writeRepeatedSFixed32Impl(r0, r7);
     */
    /* JADX WARNING: Missing block: B:13:0x0053, code skipped:
            writeRepeatedEnumImpl(r0, r7);
     */
    /* JADX WARNING: Missing block: B:14:0x0058, code skipped:
            writeRepeatedUInt32Impl(r0, r7);
     */
    /* JADX WARNING: Missing block: B:15:0x005d, code skipped:
            if (r7 == 0) goto L_0x0060;
     */
    /* JADX WARNING: Missing block: B:16:0x0060, code skipped:
            r2 = false;
     */
    /* JADX WARNING: Missing block: B:17:0x0061, code skipped:
            writeRepeatedBoolImpl(r0, r2);
     */
    /* JADX WARNING: Missing block: B:18:0x0066, code skipped:
            writeRepeatedFixed32Impl(r0, r7);
     */
    /* JADX WARNING: Missing block: B:19:0x006b, code skipped:
            writeRepeatedFixed64Impl(r0, (long) r7);
     */
    /* JADX WARNING: Missing block: B:20:0x0071, code skipped:
            writeRepeatedInt32Impl(r0, r7);
     */
    /* JADX WARNING: Missing block: B:21:0x0076, code skipped:
            writeRepeatedUInt64Impl(r0, (long) r7);
     */
    /* JADX WARNING: Missing block: B:22:0x007b, code skipped:
            writeRepeatedInt64Impl(r0, (long) r7);
     */
    /* JADX WARNING: Missing block: B:23:0x0080, code skipped:
            writeRepeatedFloatImpl(r0, (float) r7);
     */
    /* JADX WARNING: Missing block: B:24:0x0085, code skipped:
            writeRepeatedDoubleImpl(r0, (double) r7);
     */
    /* JADX WARNING: Missing block: B:42:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:43:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:44:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:45:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:46:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:47:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:48:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:49:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:50:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:51:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:52:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:53:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:54:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:55:?, code skipped:
            return;
     */
    public void write(long r5, int r7) {
        /*
        r4 = this;
        r4.assertNotCompacted();
        r0 = (int) r5;
        r1 = 17587891077120; // 0xfff00000000 float:0.0 double:8.689572764003E-311;
        r1 = r1 & r5;
        r3 = 32;
        r1 = r1 >> r3;
        r1 = (int) r1;
        r2 = 1;
        r3 = 0;
        switch(r1) {
            case 257: goto L_0x00c8;
            case 258: goto L_0x00c3;
            case 259: goto L_0x00be;
            case 260: goto L_0x00b9;
            case 261: goto L_0x00b5;
            case 262: goto L_0x00b0;
            case 263: goto L_0x00ac;
            case 264: goto L_0x00a4;
            default: goto L_0x0013;
        };
    L_0x0013:
        switch(r1) {
            case 269: goto L_0x00a0;
            case 270: goto L_0x009c;
            case 271: goto L_0x0098;
            case 272: goto L_0x0093;
            case 273: goto L_0x008f;
            case 274: goto L_0x008a;
            default: goto L_0x0016;
        };
    L_0x0016:
        switch(r1) {
            case 513: goto L_0x0085;
            case 514: goto L_0x0080;
            case 515: goto L_0x007b;
            case 516: goto L_0x0076;
            case 517: goto L_0x0071;
            case 518: goto L_0x006b;
            case 519: goto L_0x0066;
            case 520: goto L_0x005d;
            default: goto L_0x0019;
        };
    L_0x0019:
        switch(r1) {
            case 525: goto L_0x0058;
            case 526: goto L_0x0053;
            case 527: goto L_0x004e;
            case 528: goto L_0x0048;
            case 529: goto L_0x0043;
            case 530: goto L_0x003d;
            default: goto L_0x001c;
        };
    L_0x001c:
        switch(r1) {
            case 1281: goto L_0x0085;
            case 1282: goto L_0x0080;
            case 1283: goto L_0x007b;
            case 1284: goto L_0x0076;
            case 1285: goto L_0x0071;
            case 1286: goto L_0x006b;
            case 1287: goto L_0x0066;
            case 1288: goto L_0x005d;
            default: goto L_0x001f;
        };
    L_0x001f:
        switch(r1) {
            case 1293: goto L_0x0058;
            case 1294: goto L_0x0053;
            case 1295: goto L_0x004e;
            case 1296: goto L_0x0048;
            case 1297: goto L_0x0043;
            case 1298: goto L_0x003d;
            default: goto L_0x0022;
        };
    L_0x0022:
        r1 = new java.lang.IllegalArgumentException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Attempt to call write(long, int) with ";
        r2.append(r3);
        r3 = android.util.proto.ProtoStream.getFieldIdString(r5);
        r2.append(r3);
        r2 = r2.toString();
        r1.<init>(r2);
        throw r1;
    L_0x003d:
        r1 = (long) r7;
        r4.writeRepeatedSInt64Impl(r0, r1);
        goto L_0x00cd;
    L_0x0043:
        r4.writeRepeatedSInt32Impl(r0, r7);
        goto L_0x00cd;
    L_0x0048:
        r1 = (long) r7;
        r4.writeRepeatedSFixed64Impl(r0, r1);
        goto L_0x00cd;
    L_0x004e:
        r4.writeRepeatedSFixed32Impl(r0, r7);
        goto L_0x00cd;
    L_0x0053:
        r4.writeRepeatedEnumImpl(r0, r7);
        goto L_0x00cd;
    L_0x0058:
        r4.writeRepeatedUInt32Impl(r0, r7);
        goto L_0x00cd;
    L_0x005d:
        if (r7 == 0) goto L_0x0060;
    L_0x005f:
        goto L_0x0061;
    L_0x0060:
        r2 = r3;
    L_0x0061:
        r4.writeRepeatedBoolImpl(r0, r2);
        goto L_0x00cd;
    L_0x0066:
        r4.writeRepeatedFixed32Impl(r0, r7);
        goto L_0x00cd;
    L_0x006b:
        r1 = (long) r7;
        r4.writeRepeatedFixed64Impl(r0, r1);
        goto L_0x00cd;
    L_0x0071:
        r4.writeRepeatedInt32Impl(r0, r7);
        goto L_0x00cd;
    L_0x0076:
        r1 = (long) r7;
        r4.writeRepeatedUInt64Impl(r0, r1);
        goto L_0x00cd;
    L_0x007b:
        r1 = (long) r7;
        r4.writeRepeatedInt64Impl(r0, r1);
        goto L_0x00cd;
    L_0x0080:
        r1 = (float) r7;
        r4.writeRepeatedFloatImpl(r0, r1);
        goto L_0x00cd;
    L_0x0085:
        r1 = (double) r7;
        r4.writeRepeatedDoubleImpl(r0, r1);
        goto L_0x00cd;
    L_0x008a:
        r1 = (long) r7;
        r4.writeSInt64Impl(r0, r1);
        goto L_0x00cd;
    L_0x008f:
        r4.writeSInt32Impl(r0, r7);
        goto L_0x00cd;
    L_0x0093:
        r1 = (long) r7;
        r4.writeSFixed64Impl(r0, r1);
        goto L_0x00cd;
    L_0x0098:
        r4.writeSFixed32Impl(r0, r7);
        goto L_0x00cd;
    L_0x009c:
        r4.writeEnumImpl(r0, r7);
        goto L_0x00cd;
    L_0x00a0:
        r4.writeUInt32Impl(r0, r7);
        goto L_0x00cd;
    L_0x00a4:
        if (r7 == 0) goto L_0x00a7;
    L_0x00a6:
        goto L_0x00a8;
    L_0x00a7:
        r2 = r3;
    L_0x00a8:
        r4.writeBoolImpl(r0, r2);
        goto L_0x00cd;
    L_0x00ac:
        r4.writeFixed32Impl(r0, r7);
        goto L_0x00cd;
    L_0x00b0:
        r1 = (long) r7;
        r4.writeFixed64Impl(r0, r1);
        goto L_0x00cd;
    L_0x00b5:
        r4.writeInt32Impl(r0, r7);
        goto L_0x00cd;
    L_0x00b9:
        r1 = (long) r7;
        r4.writeUInt64Impl(r0, r1);
        goto L_0x00cd;
    L_0x00be:
        r1 = (long) r7;
        r4.writeInt64Impl(r0, r1);
        goto L_0x00cd;
    L_0x00c3:
        r1 = (float) r7;
        r4.writeFloatImpl(r0, r1);
        goto L_0x00cd;
    L_0x00c8:
        r1 = (double) r7;
        r4.writeDoubleImpl(r0, r1);
    L_0x00cd:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.proto.ProtoOutputStream.write(long, int):void");
    }

    /* JADX WARNING: Missing block: B:9:0x003f, code skipped:
            writeRepeatedSInt64Impl(r0, r9);
     */
    /* JADX WARNING: Missing block: B:10:0x0044, code skipped:
            writeRepeatedSInt32Impl(r0, (int) r9);
     */
    /* JADX WARNING: Missing block: B:11:0x004a, code skipped:
            writeRepeatedSFixed64Impl(r0, r9);
     */
    /* JADX WARNING: Missing block: B:12:0x004f, code skipped:
            writeRepeatedSFixed32Impl(r0, (int) r9);
     */
    /* JADX WARNING: Missing block: B:13:0x0055, code skipped:
            writeRepeatedEnumImpl(r0, (int) r9);
     */
    /* JADX WARNING: Missing block: B:14:0x005b, code skipped:
            writeRepeatedUInt32Impl(r0, (int) r9);
     */
    /* JADX WARNING: Missing block: B:16:0x0063, code skipped:
            if (r9 == 0) goto L_0x0066;
     */
    /* JADX WARNING: Missing block: B:17:0x0066, code skipped:
            r2 = false;
     */
    /* JADX WARNING: Missing block: B:18:0x0067, code skipped:
            writeRepeatedBoolImpl(r0, r2);
     */
    /* JADX WARNING: Missing block: B:19:0x006c, code skipped:
            writeRepeatedFixed32Impl(r0, (int) r9);
     */
    /* JADX WARNING: Missing block: B:20:0x0072, code skipped:
            writeRepeatedFixed64Impl(r0, r9);
     */
    /* JADX WARNING: Missing block: B:21:0x0077, code skipped:
            writeRepeatedInt32Impl(r0, (int) r9);
     */
    /* JADX WARNING: Missing block: B:22:0x007d, code skipped:
            writeRepeatedUInt64Impl(r0, r9);
     */
    /* JADX WARNING: Missing block: B:23:0x0081, code skipped:
            writeRepeatedInt64Impl(r0, r9);
     */
    /* JADX WARNING: Missing block: B:24:0x0085, code skipped:
            writeRepeatedFloatImpl(r0, (float) r9);
     */
    /* JADX WARNING: Missing block: B:25:0x008a, code skipped:
            writeRepeatedDoubleImpl(r0, (double) r9);
     */
    /* JADX WARNING: Missing block: B:44:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:45:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:46:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:47:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:48:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:49:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:50:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:51:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:52:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:53:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:54:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:55:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:56:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:57:?, code skipped:
            return;
     */
    public void write(long r7, long r9) {
        /*
        r6 = this;
        r6.assertNotCompacted();
        r0 = (int) r7;
        r1 = 17587891077120; // 0xfff00000000 float:0.0 double:8.689572764003E-311;
        r1 = r1 & r7;
        r3 = 32;
        r1 = r1 >> r3;
        r1 = (int) r1;
        r2 = 1;
        r3 = 0;
        r4 = 0;
        switch(r1) {
            case 257: goto L_0x00d0;
            case 258: goto L_0x00cb;
            case 259: goto L_0x00c7;
            case 260: goto L_0x00c3;
            case 261: goto L_0x00be;
            case 262: goto L_0x00ba;
            case 263: goto L_0x00b5;
            case 264: goto L_0x00ab;
            default: goto L_0x0015;
        };
    L_0x0015:
        switch(r1) {
            case 269: goto L_0x00a6;
            case 270: goto L_0x00a1;
            case 271: goto L_0x009c;
            case 272: goto L_0x0098;
            case 273: goto L_0x0093;
            case 274: goto L_0x008f;
            default: goto L_0x0018;
        };
    L_0x0018:
        switch(r1) {
            case 513: goto L_0x008a;
            case 514: goto L_0x0085;
            case 515: goto L_0x0081;
            case 516: goto L_0x007d;
            case 517: goto L_0x0077;
            case 518: goto L_0x0072;
            case 519: goto L_0x006c;
            case 520: goto L_0x0061;
            default: goto L_0x001b;
        };
    L_0x001b:
        switch(r1) {
            case 525: goto L_0x005b;
            case 526: goto L_0x0055;
            case 527: goto L_0x004f;
            case 528: goto L_0x004a;
            case 529: goto L_0x0044;
            case 530: goto L_0x003f;
            default: goto L_0x001e;
        };
    L_0x001e:
        switch(r1) {
            case 1281: goto L_0x008a;
            case 1282: goto L_0x0085;
            case 1283: goto L_0x0081;
            case 1284: goto L_0x007d;
            case 1285: goto L_0x0077;
            case 1286: goto L_0x0072;
            case 1287: goto L_0x006c;
            case 1288: goto L_0x0061;
            default: goto L_0x0021;
        };
    L_0x0021:
        switch(r1) {
            case 1293: goto L_0x005b;
            case 1294: goto L_0x0055;
            case 1295: goto L_0x004f;
            case 1296: goto L_0x004a;
            case 1297: goto L_0x0044;
            case 1298: goto L_0x003f;
            default: goto L_0x0024;
        };
    L_0x0024:
        r1 = new java.lang.IllegalArgumentException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Attempt to call write(long, long) with ";
        r2.append(r3);
        r3 = android.util.proto.ProtoStream.getFieldIdString(r7);
        r2.append(r3);
        r2 = r2.toString();
        r1.<init>(r2);
        throw r1;
    L_0x003f:
        r6.writeRepeatedSInt64Impl(r0, r9);
        goto L_0x00d5;
    L_0x0044:
        r1 = (int) r9;
        r6.writeRepeatedSInt32Impl(r0, r1);
        goto L_0x00d5;
    L_0x004a:
        r6.writeRepeatedSFixed64Impl(r0, r9);
        goto L_0x00d5;
    L_0x004f:
        r1 = (int) r9;
        r6.writeRepeatedSFixed32Impl(r0, r1);
        goto L_0x00d5;
    L_0x0055:
        r1 = (int) r9;
        r6.writeRepeatedEnumImpl(r0, r1);
        goto L_0x00d5;
    L_0x005b:
        r1 = (int) r9;
        r6.writeRepeatedUInt32Impl(r0, r1);
        goto L_0x00d5;
    L_0x0061:
        r1 = (r9 > r4 ? 1 : (r9 == r4 ? 0 : -1));
        if (r1 == 0) goto L_0x0066;
    L_0x0065:
        goto L_0x0067;
    L_0x0066:
        r2 = r3;
    L_0x0067:
        r6.writeRepeatedBoolImpl(r0, r2);
        goto L_0x00d5;
    L_0x006c:
        r1 = (int) r9;
        r6.writeRepeatedFixed32Impl(r0, r1);
        goto L_0x00d5;
    L_0x0072:
        r6.writeRepeatedFixed64Impl(r0, r9);
        goto L_0x00d5;
    L_0x0077:
        r1 = (int) r9;
        r6.writeRepeatedInt32Impl(r0, r1);
        goto L_0x00d5;
    L_0x007d:
        r6.writeRepeatedUInt64Impl(r0, r9);
        goto L_0x00d5;
    L_0x0081:
        r6.writeRepeatedInt64Impl(r0, r9);
        goto L_0x00d5;
    L_0x0085:
        r1 = (float) r9;
        r6.writeRepeatedFloatImpl(r0, r1);
        goto L_0x00d5;
    L_0x008a:
        r1 = (double) r9;
        r6.writeRepeatedDoubleImpl(r0, r1);
        goto L_0x00d5;
    L_0x008f:
        r6.writeSInt64Impl(r0, r9);
        goto L_0x00d5;
    L_0x0093:
        r1 = (int) r9;
        r6.writeSInt32Impl(r0, r1);
        goto L_0x00d5;
    L_0x0098:
        r6.writeSFixed64Impl(r0, r9);
        goto L_0x00d5;
    L_0x009c:
        r1 = (int) r9;
        r6.writeSFixed32Impl(r0, r1);
        goto L_0x00d5;
    L_0x00a1:
        r1 = (int) r9;
        r6.writeEnumImpl(r0, r1);
        goto L_0x00d5;
    L_0x00a6:
        r1 = (int) r9;
        r6.writeUInt32Impl(r0, r1);
        goto L_0x00d5;
    L_0x00ab:
        r1 = (r9 > r4 ? 1 : (r9 == r4 ? 0 : -1));
        if (r1 == 0) goto L_0x00b0;
    L_0x00af:
        goto L_0x00b1;
    L_0x00b0:
        r2 = r3;
    L_0x00b1:
        r6.writeBoolImpl(r0, r2);
        goto L_0x00d5;
    L_0x00b5:
        r1 = (int) r9;
        r6.writeFixed32Impl(r0, r1);
        goto L_0x00d5;
    L_0x00ba:
        r6.writeFixed64Impl(r0, r9);
        goto L_0x00d5;
    L_0x00be:
        r1 = (int) r9;
        r6.writeInt32Impl(r0, r1);
        goto L_0x00d5;
    L_0x00c3:
        r6.writeUInt64Impl(r0, r9);
        goto L_0x00d5;
    L_0x00c7:
        r6.writeInt64Impl(r0, r9);
        goto L_0x00d5;
    L_0x00cb:
        r1 = (float) r9;
        r6.writeFloatImpl(r0, r1);
        goto L_0x00d5;
    L_0x00d0:
        r1 = (double) r9;
        r6.writeDoubleImpl(r0, r1);
    L_0x00d5:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.proto.ProtoOutputStream.write(long, long):void");
    }

    public void write(long fieldId, boolean val) {
        assertNotCompacted();
        int id = (int) fieldId;
        int i = (int) ((17587891077120L & fieldId) >> 32);
        if (i == 264) {
            writeBoolImpl(id, val);
        } else if (i == 520 || i == MetricsEvent.ROTATION_SUGGESTION_SHOWN) {
            writeRepeatedBoolImpl(id, val);
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Attempt to call write(long, boolean) with ");
            stringBuilder.append(ProtoStream.getFieldIdString(fieldId));
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public void write(long fieldId, String val) {
        assertNotCompacted();
        int id = (int) fieldId;
        int i = (int) ((17587891077120L & fieldId) >> 32);
        if (i == 265) {
            writeStringImpl(id, val);
        } else if (i == 521 || i == MetricsEvent.AUTOFILL_INVALID_PERMISSION) {
            writeRepeatedStringImpl(id, val);
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Attempt to call write(long, String) with ");
            stringBuilder.append(ProtoStream.getFieldIdString(fieldId));
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public void write(long fieldId, byte[] val) {
        assertNotCompacted();
        int id = (int) fieldId;
        int i = (int) ((17587891077120L & fieldId) >> 32);
        if (i == 267) {
            writeObjectImpl(id, val);
        } else if (i != 268) {
            if (i != 523) {
                if (i != 524) {
                    if (i != 1291) {
                        if (i != 1292) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Attempt to call write(long, byte[]) with ");
                            stringBuilder.append(ProtoStream.getFieldIdString(fieldId));
                            throw new IllegalArgumentException(stringBuilder.toString());
                        }
                    }
                }
                writeRepeatedBytesImpl(id, val);
                return;
            }
            writeRepeatedObjectImpl(id, val);
        } else {
            writeBytesImpl(id, val);
        }
    }

    public long start(long fieldId) {
        assertNotCompacted();
        int id = (int) fieldId;
        if ((ProtoStream.FIELD_TYPE_MASK & fieldId) == ProtoStream.FIELD_TYPE_MESSAGE) {
            long count = ProtoStream.FIELD_COUNT_MASK & fieldId;
            if (count == 1099511627776L) {
                return startObjectImpl(id, false);
            }
            if (count == ProtoStream.FIELD_COUNT_REPEATED || count == ProtoStream.FIELD_COUNT_PACKED) {
                return startObjectImpl(id, true);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Attempt to call start(long) with ");
        stringBuilder.append(ProtoStream.getFieldIdString(fieldId));
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public void end(long token) {
        endObjectImpl(token, ProtoStream.getRepeatedFromToken(token));
    }

    @Deprecated
    public void writeDouble(long fieldId, double val) {
        assertNotCompacted();
        writeDoubleImpl(checkFieldId(fieldId, 0), val);
    }

    private void writeDoubleImpl(int id, double val) {
        if (val != 0.0d) {
            writeTag(id, 1);
            this.mBuffer.writeRawFixed64(Double.doubleToLongBits(val));
        }
    }

    @Deprecated
    public void writeRepeatedDouble(long fieldId, double val) {
        assertNotCompacted();
        writeRepeatedDoubleImpl(checkFieldId(fieldId, 0), val);
    }

    private void writeRepeatedDoubleImpl(int id, double val) {
        writeTag(id, 1);
        this.mBuffer.writeRawFixed64(Double.doubleToLongBits(val));
    }

    @Deprecated
    public void writePackedDouble(long fieldId, double[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 0);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            writeKnownLengthHeader(id, N * 8);
            for (int i = 0; i < N; i++) {
                this.mBuffer.writeRawFixed64(Double.doubleToLongBits(val[i]));
            }
        }
    }

    @Deprecated
    public void writeFloat(long fieldId, float val) {
        assertNotCompacted();
        writeFloatImpl(checkFieldId(fieldId, 0), val);
    }

    private void writeFloatImpl(int id, float val) {
        if (val != 0.0f) {
            writeTag(id, 5);
            this.mBuffer.writeRawFixed32(Float.floatToIntBits(val));
        }
    }

    @Deprecated
    public void writeRepeatedFloat(long fieldId, float val) {
        assertNotCompacted();
        writeRepeatedFloatImpl(checkFieldId(fieldId, 0), val);
    }

    private void writeRepeatedFloatImpl(int id, float val) {
        writeTag(id, 5);
        this.mBuffer.writeRawFixed32(Float.floatToIntBits(val));
    }

    @Deprecated
    public void writePackedFloat(long fieldId, float[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 0);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            writeKnownLengthHeader(id, N * 4);
            for (int i = 0; i < N; i++) {
                this.mBuffer.writeRawFixed32(Float.floatToIntBits(val[i]));
            }
        }
    }

    private void writeUnsignedVarintFromSignedInt(int val) {
        if (val >= 0) {
            this.mBuffer.writeRawVarint32(val);
        } else {
            this.mBuffer.writeRawVarint64((long) val);
        }
    }

    @Deprecated
    public void writeInt32(long fieldId, int val) {
        assertNotCompacted();
        writeInt32Impl(checkFieldId(fieldId, 0), val);
    }

    private void writeInt32Impl(int id, int val) {
        if (val != 0) {
            writeTag(id, 0);
            writeUnsignedVarintFromSignedInt(val);
        }
    }

    @Deprecated
    public void writeRepeatedInt32(long fieldId, int val) {
        assertNotCompacted();
        writeRepeatedInt32Impl(checkFieldId(fieldId, 0), val);
    }

    private void writeRepeatedInt32Impl(int id, int val) {
        writeTag(id, 0);
        writeUnsignedVarintFromSignedInt(val);
    }

    @Deprecated
    public void writePackedInt32(long fieldId, int[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 0);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            int i;
            int size = 0;
            for (i = 0; i < N; i++) {
                int v = val[i];
                size += v >= 0 ? EncodedBuffer.getRawVarint32Size(v) : 10;
            }
            writeKnownLengthHeader(id, size);
            for (i = 0; i < N; i++) {
                writeUnsignedVarintFromSignedInt(val[i]);
            }
        }
    }

    @Deprecated
    public void writeInt64(long fieldId, long val) {
        assertNotCompacted();
        writeInt64Impl(checkFieldId(fieldId, 0), val);
    }

    private void writeInt64Impl(int id, long val) {
        if (val != 0) {
            writeTag(id, 0);
            this.mBuffer.writeRawVarint64(val);
        }
    }

    @Deprecated
    public void writeRepeatedInt64(long fieldId, long val) {
        assertNotCompacted();
        writeRepeatedInt64Impl(checkFieldId(fieldId, 0), val);
    }

    private void writeRepeatedInt64Impl(int id, long val) {
        writeTag(id, 0);
        this.mBuffer.writeRawVarint64(val);
    }

    @Deprecated
    public void writePackedInt64(long fieldId, long[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 0);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            int i;
            int size = 0;
            for (i = 0; i < N; i++) {
                size += EncodedBuffer.getRawVarint64Size(val[i]);
            }
            writeKnownLengthHeader(id, size);
            for (i = 0; i < N; i++) {
                this.mBuffer.writeRawVarint64(val[i]);
            }
        }
    }

    @Deprecated
    public void writeUInt32(long fieldId, int val) {
        assertNotCompacted();
        writeUInt32Impl(checkFieldId(fieldId, 0), val);
    }

    private void writeUInt32Impl(int id, int val) {
        if (val != 0) {
            writeTag(id, 0);
            this.mBuffer.writeRawVarint32(val);
        }
    }

    @Deprecated
    public void writeRepeatedUInt32(long fieldId, int val) {
        assertNotCompacted();
        writeRepeatedUInt32Impl(checkFieldId(fieldId, 0), val);
    }

    private void writeRepeatedUInt32Impl(int id, int val) {
        writeTag(id, 0);
        this.mBuffer.writeRawVarint32(val);
    }

    @Deprecated
    public void writePackedUInt32(long fieldId, int[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 0);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            int i;
            int size = 0;
            for (i = 0; i < N; i++) {
                size += EncodedBuffer.getRawVarint32Size(val[i]);
            }
            writeKnownLengthHeader(id, size);
            for (i = 0; i < N; i++) {
                this.mBuffer.writeRawVarint32(val[i]);
            }
        }
    }

    @Deprecated
    public void writeUInt64(long fieldId, long val) {
        assertNotCompacted();
        writeUInt64Impl(checkFieldId(fieldId, 0), val);
    }

    private void writeUInt64Impl(int id, long val) {
        if (val != 0) {
            writeTag(id, 0);
            this.mBuffer.writeRawVarint64(val);
        }
    }

    @Deprecated
    public void writeRepeatedUInt64(long fieldId, long val) {
        assertNotCompacted();
        writeRepeatedUInt64Impl(checkFieldId(fieldId, 0), val);
    }

    private void writeRepeatedUInt64Impl(int id, long val) {
        writeTag(id, 0);
        this.mBuffer.writeRawVarint64(val);
    }

    @Deprecated
    public void writePackedUInt64(long fieldId, long[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 0);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            int i;
            int size = 0;
            for (i = 0; i < N; i++) {
                size += EncodedBuffer.getRawVarint64Size(val[i]);
            }
            writeKnownLengthHeader(id, size);
            for (i = 0; i < N; i++) {
                this.mBuffer.writeRawVarint64(val[i]);
            }
        }
    }

    @Deprecated
    public void writeSInt32(long fieldId, int val) {
        assertNotCompacted();
        writeSInt32Impl(checkFieldId(fieldId, 0), val);
    }

    private void writeSInt32Impl(int id, int val) {
        if (val != 0) {
            writeTag(id, 0);
            this.mBuffer.writeRawZigZag32(val);
        }
    }

    @Deprecated
    public void writeRepeatedSInt32(long fieldId, int val) {
        assertNotCompacted();
        writeRepeatedSInt32Impl(checkFieldId(fieldId, 0), val);
    }

    private void writeRepeatedSInt32Impl(int id, int val) {
        writeTag(id, 0);
        this.mBuffer.writeRawZigZag32(val);
    }

    @Deprecated
    public void writePackedSInt32(long fieldId, int[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 0);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            int i;
            int size = 0;
            for (i = 0; i < N; i++) {
                size += EncodedBuffer.getRawZigZag32Size(val[i]);
            }
            writeKnownLengthHeader(id, size);
            for (i = 0; i < N; i++) {
                this.mBuffer.writeRawZigZag32(val[i]);
            }
        }
    }

    @Deprecated
    public void writeSInt64(long fieldId, long val) {
        assertNotCompacted();
        writeSInt64Impl(checkFieldId(fieldId, 0), val);
    }

    private void writeSInt64Impl(int id, long val) {
        if (val != 0) {
            writeTag(id, 0);
            this.mBuffer.writeRawZigZag64(val);
        }
    }

    @Deprecated
    public void writeRepeatedSInt64(long fieldId, long val) {
        assertNotCompacted();
        writeRepeatedSInt64Impl(checkFieldId(fieldId, 0), val);
    }

    private void writeRepeatedSInt64Impl(int id, long val) {
        writeTag(id, 0);
        this.mBuffer.writeRawZigZag64(val);
    }

    @Deprecated
    public void writePackedSInt64(long fieldId, long[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 0);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            int i;
            int size = 0;
            for (i = 0; i < N; i++) {
                size += EncodedBuffer.getRawZigZag64Size(val[i]);
            }
            writeKnownLengthHeader(id, size);
            for (i = 0; i < N; i++) {
                this.mBuffer.writeRawZigZag64(val[i]);
            }
        }
    }

    @Deprecated
    public void writeFixed32(long fieldId, int val) {
        assertNotCompacted();
        writeFixed32Impl(checkFieldId(fieldId, 0), val);
    }

    private void writeFixed32Impl(int id, int val) {
        if (val != 0) {
            writeTag(id, 5);
            this.mBuffer.writeRawFixed32(val);
        }
    }

    @Deprecated
    public void writeRepeatedFixed32(long fieldId, int val) {
        assertNotCompacted();
        writeRepeatedFixed32Impl(checkFieldId(fieldId, 0), val);
    }

    private void writeRepeatedFixed32Impl(int id, int val) {
        writeTag(id, 5);
        this.mBuffer.writeRawFixed32(val);
    }

    @Deprecated
    public void writePackedFixed32(long fieldId, int[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 0);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            writeKnownLengthHeader(id, N * 4);
            for (int i = 0; i < N; i++) {
                this.mBuffer.writeRawFixed32(val[i]);
            }
        }
    }

    @Deprecated
    public void writeFixed64(long fieldId, long val) {
        assertNotCompacted();
        writeFixed64Impl(checkFieldId(fieldId, 0), val);
    }

    private void writeFixed64Impl(int id, long val) {
        if (val != 0) {
            writeTag(id, 1);
            this.mBuffer.writeRawFixed64(val);
        }
    }

    @Deprecated
    public void writeRepeatedFixed64(long fieldId, long val) {
        assertNotCompacted();
        writeRepeatedFixed64Impl(checkFieldId(fieldId, 0), val);
    }

    private void writeRepeatedFixed64Impl(int id, long val) {
        writeTag(id, 1);
        this.mBuffer.writeRawFixed64(val);
    }

    @Deprecated
    public void writePackedFixed64(long fieldId, long[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 0);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            writeKnownLengthHeader(id, N * 8);
            for (int i = 0; i < N; i++) {
                this.mBuffer.writeRawFixed64(val[i]);
            }
        }
    }

    @Deprecated
    public void writeSFixed32(long fieldId, int val) {
        assertNotCompacted();
        writeSFixed32Impl(checkFieldId(fieldId, 0), val);
    }

    private void writeSFixed32Impl(int id, int val) {
        if (val != 0) {
            writeTag(id, 5);
            this.mBuffer.writeRawFixed32(val);
        }
    }

    @Deprecated
    public void writeRepeatedSFixed32(long fieldId, int val) {
        assertNotCompacted();
        writeRepeatedSFixed32Impl(checkFieldId(fieldId, 0), val);
    }

    private void writeRepeatedSFixed32Impl(int id, int val) {
        writeTag(id, 5);
        this.mBuffer.writeRawFixed32(val);
    }

    @Deprecated
    public void writePackedSFixed32(long fieldId, int[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 0);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            writeKnownLengthHeader(id, N * 4);
            for (int i = 0; i < N; i++) {
                this.mBuffer.writeRawFixed32(val[i]);
            }
        }
    }

    @Deprecated
    public void writeSFixed64(long fieldId, long val) {
        assertNotCompacted();
        writeSFixed64Impl(checkFieldId(fieldId, 0), val);
    }

    private void writeSFixed64Impl(int id, long val) {
        if (val != 0) {
            writeTag(id, 1);
            this.mBuffer.writeRawFixed64(val);
        }
    }

    @Deprecated
    public void writeRepeatedSFixed64(long fieldId, long val) {
        assertNotCompacted();
        writeRepeatedSFixed64Impl(checkFieldId(fieldId, 0), val);
    }

    private void writeRepeatedSFixed64Impl(int id, long val) {
        writeTag(id, 1);
        this.mBuffer.writeRawFixed64(val);
    }

    @Deprecated
    public void writePackedSFixed64(long fieldId, long[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 0);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            writeKnownLengthHeader(id, N * 8);
            for (int i = 0; i < N; i++) {
                this.mBuffer.writeRawFixed64(val[i]);
            }
        }
    }

    @Deprecated
    public void writeBool(long fieldId, boolean val) {
        assertNotCompacted();
        writeBoolImpl(checkFieldId(fieldId, 0), val);
    }

    private void writeBoolImpl(int id, boolean val) {
        if (val) {
            writeTag(id, 0);
            this.mBuffer.writeRawByte((byte) 1);
        }
    }

    @Deprecated
    public void writeRepeatedBool(long fieldId, boolean val) {
        assertNotCompacted();
        writeRepeatedBoolImpl(checkFieldId(fieldId, 0), val);
    }

    private void writeRepeatedBoolImpl(int id, boolean val) {
        writeTag(id, 0);
        this.mBuffer.writeRawByte((byte) val);
    }

    @Deprecated
    public void writePackedBool(long fieldId, boolean[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 0);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            writeKnownLengthHeader(id, N);
            for (int i = 0; i < N; i++) {
                this.mBuffer.writeRawByte((byte) val[i]);
            }
        }
    }

    @Deprecated
    public void writeString(long fieldId, String val) {
        assertNotCompacted();
        writeStringImpl(checkFieldId(fieldId, 0), val);
    }

    private void writeStringImpl(int id, String val) {
        if (val != null && val.length() > 0) {
            writeUtf8String(id, val);
        }
    }

    @Deprecated
    public void writeRepeatedString(long fieldId, String val) {
        assertNotCompacted();
        writeRepeatedStringImpl(checkFieldId(fieldId, 0), val);
    }

    private void writeRepeatedStringImpl(int id, String val) {
        if (val == null || val.length() == 0) {
            writeKnownLengthHeader(id, 0);
        } else {
            writeUtf8String(id, val);
        }
    }

    private void writeUtf8String(int id, String val) {
        try {
            byte[] buf = val.getBytes("UTF-8");
            writeKnownLengthHeader(id, buf.length);
            this.mBuffer.writeRawBuffer(buf);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("not possible");
        }
    }

    @Deprecated
    public void writeBytes(long fieldId, byte[] val) {
        assertNotCompacted();
        writeBytesImpl(checkFieldId(fieldId, 0), val);
    }

    private void writeBytesImpl(int id, byte[] val) {
        if (val != null && val.length > 0) {
            writeKnownLengthHeader(id, val.length);
            this.mBuffer.writeRawBuffer(val);
        }
    }

    @Deprecated
    public void writeRepeatedBytes(long fieldId, byte[] val) {
        assertNotCompacted();
        writeRepeatedBytesImpl(checkFieldId(fieldId, 0), val);
    }

    private void writeRepeatedBytesImpl(int id, byte[] val) {
        writeKnownLengthHeader(id, val == null ? 0 : val.length);
        this.mBuffer.writeRawBuffer(val);
    }

    @Deprecated
    public void writeEnum(long fieldId, int val) {
        assertNotCompacted();
        writeEnumImpl(checkFieldId(fieldId, 0), val);
    }

    private void writeEnumImpl(int id, int val) {
        if (val != 0) {
            writeTag(id, 0);
            writeUnsignedVarintFromSignedInt(val);
        }
    }

    @Deprecated
    public void writeRepeatedEnum(long fieldId, int val) {
        assertNotCompacted();
        writeRepeatedEnumImpl(checkFieldId(fieldId, 0), val);
    }

    private void writeRepeatedEnumImpl(int id, int val) {
        writeTag(id, 0);
        writeUnsignedVarintFromSignedInt(val);
    }

    @Deprecated
    public void writePackedEnum(long fieldId, int[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 0);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            int i;
            int size = 0;
            for (i = 0; i < N; i++) {
                int v = val[i];
                size += v >= 0 ? EncodedBuffer.getRawVarint32Size(v) : 10;
            }
            writeKnownLengthHeader(id, size);
            for (i = 0; i < N; i++) {
                writeUnsignedVarintFromSignedInt(val[i]);
            }
        }
    }

    @Deprecated
    public long startObject(long fieldId) {
        assertNotCompacted();
        return startObjectImpl(checkFieldId(fieldId, 0), false);
    }

    @Deprecated
    public void endObject(long token) {
        assertNotCompacted();
        endObjectImpl(token, false);
    }

    @Deprecated
    public long startRepeatedObject(long fieldId) {
        assertNotCompacted();
        return startObjectImpl(checkFieldId(fieldId, 0), true);
    }

    @Deprecated
    public void endRepeatedObject(long token) {
        assertNotCompacted();
        endObjectImpl(token, true);
    }

    private long startObjectImpl(int id, boolean repeated) {
        writeTag(id, 2);
        int sizePos = this.mBuffer.getWritePos();
        this.mDepth++;
        this.mNextObjectId--;
        this.mBuffer.writeRawFixed32((int) (this.mExpectedObjectToken >> 32));
        this.mBuffer.writeRawFixed32((int) this.mExpectedObjectToken);
        long old = this.mExpectedObjectToken;
        this.mExpectedObjectToken = ProtoStream.makeToken(getTagSize(id), repeated, this.mDepth, this.mNextObjectId, sizePos);
        return this.mExpectedObjectToken;
    }

    private void endObjectImpl(long token, boolean repeated) {
        int depth = ProtoStream.getDepthFromToken(token);
        boolean expectedRepeated = ProtoStream.getRepeatedFromToken(token);
        int sizePos = ProtoStream.getOffsetFromToken(token);
        int childRawSize = (this.mBuffer.getWritePos() - sizePos) - 8;
        if (repeated != expectedRepeated) {
            if (repeated) {
                throw new IllegalArgumentException("endRepeatedObject called where endObject should have been");
            }
            throw new IllegalArgumentException("endObject called where endRepeatedObject should have been");
        } else if ((this.mDepth & 511) == depth && this.mExpectedObjectToken == token) {
            this.mExpectedObjectToken = (((long) this.mBuffer.getRawFixed32At(sizePos)) << 32) | (4294967295L & ((long) this.mBuffer.getRawFixed32At(sizePos + 4)));
            this.mDepth--;
            if (childRawSize > 0) {
                this.mBuffer.editRawFixed32(sizePos, -childRawSize);
                this.mBuffer.editRawFixed32(sizePos + 4, -1);
            } else if (repeated) {
                this.mBuffer.editRawFixed32(sizePos, 0);
                this.mBuffer.editRawFixed32(sizePos + 4, 0);
            } else {
                this.mBuffer.rewindWriteTo(sizePos - ProtoStream.getTagSizeFromToken(token));
            }
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Mismatched startObject/endObject calls. Current depth ");
            stringBuilder.append(this.mDepth);
            stringBuilder.append(" token=");
            stringBuilder.append(ProtoStream.token2String(token));
            stringBuilder.append(" expectedToken=");
            stringBuilder.append(ProtoStream.token2String(this.mExpectedObjectToken));
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    @Deprecated
    public void writeObject(long fieldId, byte[] value) {
        assertNotCompacted();
        writeObjectImpl(checkFieldId(fieldId, 0), value);
    }

    /* Access modifiers changed, original: 0000 */
    public void writeObjectImpl(int id, byte[] value) {
        if (value != null && value.length != 0) {
            writeKnownLengthHeader(id, value.length);
            this.mBuffer.writeRawBuffer(value);
        }
    }

    @Deprecated
    public void writeRepeatedObject(long fieldId, byte[] value) {
        assertNotCompacted();
        writeRepeatedObjectImpl(checkFieldId(fieldId, 0), value);
    }

    /* Access modifiers changed, original: 0000 */
    public void writeRepeatedObjectImpl(int id, byte[] value) {
        writeKnownLengthHeader(id, value == null ? 0 : value.length);
        this.mBuffer.writeRawBuffer(value);
    }

    public static long makeFieldId(int id, long fieldFlags) {
        return (((long) id) & 4294967295L) | fieldFlags;
    }

    public static int checkFieldId(long fieldId, long expectedFlags) {
        long j = fieldId;
        long fieldCount = j & ProtoStream.FIELD_COUNT_MASK;
        long fieldType = j & ProtoStream.FIELD_TYPE_MASK;
        long expectedCount = expectedFlags & ProtoStream.FIELD_COUNT_MASK;
        long expectedType = expectedFlags & ProtoStream.FIELD_TYPE_MASK;
        StringBuilder stringBuilder;
        if (((int) j) == 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid proto field ");
            stringBuilder.append((int) j);
            stringBuilder.append(" fieldId=");
            stringBuilder.append(Long.toHexString(fieldId));
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (fieldType == expectedType && (fieldCount == expectedCount || (fieldCount == ProtoStream.FIELD_COUNT_PACKED && expectedCount == ProtoStream.FIELD_COUNT_REPEATED))) {
            return (int) j;
        } else {
            String countString = ProtoStream.getFieldCountString(fieldCount);
            String typeString = ProtoStream.getFieldTypeString(fieldType);
            String str = BaseMmsColumns.START;
            String str2 = "write";
            if (typeString == null || countString == null) {
                stringBuilder = new StringBuilder();
                if (expectedType == ProtoStream.FIELD_TYPE_MESSAGE) {
                    stringBuilder.append(str);
                } else {
                    stringBuilder.append(str2);
                }
                stringBuilder.append(ProtoStream.getFieldCountString(expectedCount));
                stringBuilder.append(ProtoStream.getFieldTypeString(expectedType));
                stringBuilder.append(" called with an invalid fieldId: 0x");
                stringBuilder.append(Long.toHexString(fieldId));
                stringBuilder.append(". The proto field ID might be ");
                stringBuilder.append((int) j);
                stringBuilder.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            StringBuilder sb = new StringBuilder();
            if (expectedType == ProtoStream.FIELD_TYPE_MESSAGE) {
                stringBuilder = sb;
                stringBuilder.append(str);
            } else {
                stringBuilder = sb;
                stringBuilder.append(str2);
            }
            stringBuilder.append(ProtoStream.getFieldCountString(expectedCount));
            stringBuilder.append(ProtoStream.getFieldTypeString(expectedType));
            stringBuilder.append(" called for field ");
            stringBuilder.append((int) j);
            stringBuilder.append(" which should be used with ");
            if (fieldType == ProtoStream.FIELD_TYPE_MESSAGE) {
                stringBuilder.append(str);
            } else {
                stringBuilder.append(str2);
            }
            stringBuilder.append(countString);
            stringBuilder.append(typeString);
            if (fieldCount == ProtoStream.FIELD_COUNT_PACKED) {
                stringBuilder.append(" or writeRepeated");
                stringBuilder.append(typeString);
            }
            stringBuilder.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    private static int getTagSize(int id) {
        return EncodedBuffer.getRawVarint32Size(id << 3);
    }

    public void writeTag(int id, int wireType) {
        this.mBuffer.writeRawVarint32((id << 3) | wireType);
    }

    private void writeKnownLengthHeader(int id, int size) {
        writeTag(id, 2);
        this.mBuffer.writeRawFixed32(size);
        this.mBuffer.writeRawFixed32(size);
    }

    private void assertNotCompacted() {
        if (this.mCompacted) {
            throw new IllegalArgumentException("write called after compact");
        }
    }

    public byte[] getBytes() {
        compactIfNecessary();
        EncodedBuffer encodedBuffer = this.mBuffer;
        return encodedBuffer.getBytes(encodedBuffer.getReadableSize());
    }

    private void compactIfNecessary() {
        if (!this.mCompacted) {
            if (this.mDepth == 0) {
                this.mBuffer.startEditing();
                int readableSize = this.mBuffer.getReadableSize();
                editEncodedSize(readableSize);
                this.mBuffer.rewindRead();
                compactSizes(readableSize);
                int i = this.mCopyBegin;
                if (i < readableSize) {
                    this.mBuffer.writeFromThisBuffer(i, readableSize - i);
                }
                this.mBuffer.startEditing();
                this.mCompacted = true;
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Trying to compact with ");
            stringBuilder.append(this.mDepth);
            stringBuilder.append(" missing calls to endObject");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    private int editEncodedSize(int rawSize) {
        int tagPos;
        StringBuilder stringBuilder;
        int objectEnd = this.mBuffer.getReadPos() + rawSize;
        int encodedSize = 0;
        while (true) {
            int readPos = this.mBuffer.getReadPos();
            tagPos = readPos;
            if (readPos < objectEnd) {
                readPos = readRawTag();
                encodedSize += EncodedBuffer.getRawVarint32Size(readPos);
                int wireType = readPos & 7;
                if (wireType == 0) {
                    while (true) {
                        encodedSize++;
                        if ((this.mBuffer.readRawByte() & 128) == 0) {
                            break;
                        }
                    }
                } else if (wireType == 1) {
                    encodedSize += 8;
                    this.mBuffer.skipRead(8);
                } else if (wireType == 2) {
                    int childRawSize = this.mBuffer.readRawFixed32();
                    int childEncodedSizePos = this.mBuffer.getReadPos();
                    int childEncodedSize = this.mBuffer.readRawFixed32();
                    if (childRawSize < 0) {
                        childEncodedSize = editEncodedSize(-childRawSize);
                        this.mBuffer.editRawFixed32(childEncodedSizePos, childEncodedSize);
                    } else if (childEncodedSize == childRawSize) {
                        this.mBuffer.skipRead(childRawSize);
                    } else {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Pre-computed size where the precomputed size and the raw size in the buffer don't match! childRawSize=");
                        stringBuilder2.append(childRawSize);
                        stringBuilder2.append(" childEncodedSize=");
                        stringBuilder2.append(childEncodedSize);
                        stringBuilder2.append(" childEncodedSizePos=");
                        stringBuilder2.append(childEncodedSizePos);
                        throw new RuntimeException(stringBuilder2.toString());
                    }
                    encodedSize += EncodedBuffer.getRawVarint32Size(childEncodedSize) + childEncodedSize;
                } else if (wireType == 3 || wireType == 4) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("groups not supported at index ");
                    stringBuilder.append(tagPos);
                } else if (wireType == 5) {
                    encodedSize += 4;
                    this.mBuffer.skipRead(4);
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("editEncodedSize Bad tag tag=0x");
                    stringBuilder.append(Integer.toHexString(readPos));
                    stringBuilder.append(" wireType=");
                    stringBuilder.append(wireType);
                    stringBuilder.append(" -- ");
                    stringBuilder.append(this.mBuffer.getDebugString());
                    throw new ProtoParseException(stringBuilder.toString());
                }
            }
            return encodedSize;
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("groups not supported at index ");
        stringBuilder.append(tagPos);
        throw new RuntimeException(stringBuilder.toString());
    }

    private void compactSizes(int rawSize) {
        int tagPos;
        StringBuilder stringBuilder;
        int objectEnd = this.mBuffer.getReadPos() + rawSize;
        while (true) {
            int readPos = this.mBuffer.getReadPos();
            tagPos = readPos;
            if (readPos < objectEnd) {
                readPos = readRawTag();
                int wireType = readPos & 7;
                if (wireType == 0) {
                    while ((this.mBuffer.readRawByte() & 128) != 0) {
                    }
                } else if (wireType == 1) {
                    this.mBuffer.skipRead(8);
                } else if (wireType == 2) {
                    EncodedBuffer encodedBuffer = this.mBuffer;
                    encodedBuffer.writeFromThisBuffer(this.mCopyBegin, encodedBuffer.getReadPos() - this.mCopyBegin);
                    int childRawSize = this.mBuffer.readRawFixed32();
                    int childEncodedSize = this.mBuffer.readRawFixed32();
                    this.mBuffer.writeRawVarint32(childEncodedSize);
                    this.mCopyBegin = this.mBuffer.getReadPos();
                    if (childRawSize >= 0) {
                        this.mBuffer.skipRead(childEncodedSize);
                    } else {
                        compactSizes(-childRawSize);
                    }
                } else if (wireType == 3 || wireType == 4) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("groups not supported at index ");
                    stringBuilder.append(tagPos);
                } else if (wireType == 5) {
                    this.mBuffer.skipRead(4);
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("compactSizes Bad tag tag=0x");
                    stringBuilder.append(Integer.toHexString(readPos));
                    stringBuilder.append(" wireType=");
                    stringBuilder.append(wireType);
                    stringBuilder.append(" -- ");
                    stringBuilder.append(this.mBuffer.getDebugString());
                    throw new ProtoParseException(stringBuilder.toString());
                }
            }
            return;
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("groups not supported at index ");
        stringBuilder.append(tagPos);
        throw new RuntimeException(stringBuilder.toString());
    }

    public void flush() {
        if (this.mStream != null && this.mDepth == 0 && !this.mCompacted) {
            compactIfNecessary();
            byte[] data = this.mBuffer;
            try {
                this.mStream.write(data.getBytes(data.getReadableSize()));
                this.mStream.flush();
            } catch (IOException ex) {
                throw new RuntimeException("Error flushing proto to stream", ex);
            }
        }
    }

    private int readRawTag() {
        if (this.mBuffer.getReadPos() == this.mBuffer.getReadableSize()) {
            return 0;
        }
        return (int) this.mBuffer.readRawUnsigned();
    }

    public void dump(String tag) {
        Log.d(tag, this.mBuffer.getDebugString());
        this.mBuffer.dumpBuffers(tag);
    }
}
