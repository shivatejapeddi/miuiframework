package android.bluetooth.le;

import android.bluetooth.BluetoothUuid;
import android.os.ParcelUuid;
import android.util.SparseArray;
import java.util.List;
import java.util.Map;

public final class ScanRecord {
    private static final int DATA_TYPE_FLAGS = 1;
    private static final int DATA_TYPE_LOCAL_NAME_COMPLETE = 9;
    private static final int DATA_TYPE_LOCAL_NAME_SHORT = 8;
    private static final int DATA_TYPE_MANUFACTURER_SPECIFIC_DATA = 255;
    private static final int DATA_TYPE_SERVICE_DATA_128_BIT = 33;
    private static final int DATA_TYPE_SERVICE_DATA_16_BIT = 22;
    private static final int DATA_TYPE_SERVICE_DATA_32_BIT = 32;
    private static final int DATA_TYPE_SERVICE_SOLICITATION_UUIDS_128_BIT = 21;
    private static final int DATA_TYPE_SERVICE_SOLICITATION_UUIDS_16_BIT = 20;
    private static final int DATA_TYPE_SERVICE_SOLICITATION_UUIDS_32_BIT = 31;
    private static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_COMPLETE = 7;
    private static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_PARTIAL = 6;
    private static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_COMPLETE = 3;
    private static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_PARTIAL = 2;
    private static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_COMPLETE = 5;
    private static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_PARTIAL = 4;
    private static final int DATA_TYPE_TRANSPORT_DISCOVERY_DATA = 38;
    private static final int DATA_TYPE_TX_POWER_LEVEL = 10;
    private static final String TAG = "ScanRecord";
    private final int mAdvertiseFlags;
    private final byte[] mBytes;
    private final String mDeviceName;
    private final SparseArray<byte[]> mManufacturerSpecificData;
    private final Map<ParcelUuid, byte[]> mServiceData;
    private final List<ParcelUuid> mServiceSolicitationUuids;
    private final List<ParcelUuid> mServiceUuids;
    private final byte[] mTDSData;
    private final int mTxPowerLevel;

    public int getAdvertiseFlags() {
        return this.mAdvertiseFlags;
    }

    public List<ParcelUuid> getServiceUuids() {
        return this.mServiceUuids;
    }

    public List<ParcelUuid> getServiceSolicitationUuids() {
        return this.mServiceSolicitationUuids;
    }

    public SparseArray<byte[]> getManufacturerSpecificData() {
        return this.mManufacturerSpecificData;
    }

    public byte[] getManufacturerSpecificData(int manufacturerId) {
        SparseArray sparseArray = this.mManufacturerSpecificData;
        if (sparseArray == null) {
            return null;
        }
        return (byte[]) sparseArray.get(manufacturerId);
    }

    public Map<ParcelUuid, byte[]> getServiceData() {
        return this.mServiceData;
    }

    public byte[] getServiceData(ParcelUuid serviceDataUuid) {
        if (serviceDataUuid != null) {
            Map map = this.mServiceData;
            if (map != null) {
                return (byte[]) map.get(serviceDataUuid);
            }
        }
        return null;
    }

    public int getTxPowerLevel() {
        return this.mTxPowerLevel;
    }

    public String getDeviceName() {
        return this.mDeviceName;
    }

    public byte[] getTDSData() {
        return this.mTDSData;
    }

    public byte[] getBytes() {
        return this.mBytes;
    }

    private ScanRecord(List<ParcelUuid> serviceUuids, List<ParcelUuid> serviceSolicitationUuids, SparseArray<byte[]> manufacturerData, Map<ParcelUuid, byte[]> serviceData, int advertiseFlags, int txPowerLevel, String localName, byte[] tdsData, byte[] bytes) {
        this.mServiceSolicitationUuids = serviceSolicitationUuids;
        this.mServiceUuids = serviceUuids;
        this.mManufacturerSpecificData = manufacturerData;
        this.mServiceData = serviceData;
        this.mDeviceName = localName;
        this.mAdvertiseFlags = advertiseFlags;
        this.mTxPowerLevel = txPowerLevel;
        this.mTDSData = tdsData;
        this.mBytes = bytes;
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x0063 A:{Catch:{ Exception -> 0x00c9 }} */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0061 A:{Catch:{ Exception -> 0x00c9 }} */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00e1  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00dd  */
    @android.annotation.UnsupportedAppUsage
    public static android.bluetooth.le.ScanRecord parseFromBytes(byte[] r22) {
        /*
        r11 = r22;
        if (r11 != 0) goto L_0x0006;
    L_0x0004:
        r0 = 0;
        return r0;
    L_0x0006:
        r0 = 0;
        r1 = -1;
        r2 = new java.util.ArrayList;
        r2.<init>();
        r3 = new java.util.ArrayList;
        r3.<init>();
        r12 = r3;
        r3 = 0;
        r4 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r5 = new android.util.SparseArray;
        r5.<init>();
        r13 = r5;
        r5 = new android.util.ArrayMap;
        r5.<init>();
        r14 = r5;
        r5 = 0;
        r15 = r1;
        r17 = r3;
        r16 = r4;
        r18 = r5;
        r1 = r0;
    L_0x002b:
        r0 = r11.length;	 Catch:{ Exception -> 0x00fe }
        if (r1 >= r0) goto L_0x00d5;
    L_0x002e:
        r3 = r1 + 1;
        r0 = r11[r1];	 Catch:{ Exception -> 0x00cf }
        r1 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r0 = r0 & r1;
        if (r0 != 0) goto L_0x003b;
    L_0x0037:
        r19 = r3;
        goto L_0x00d7;
    L_0x003b:
        r4 = r0 + -1;
        r5 = r3 + 1;
        r3 = r11[r3];	 Catch:{ Exception -> 0x00c9 }
        r3 = r3 & r1;
        r6 = 38;
        if (r3 == r6) goto L_0x00be;
    L_0x0046:
        if (r3 == r1) goto L_0x00a7;
    L_0x0048:
        r6 = 16;
        r7 = 4;
        r8 = 2;
        switch(r3) {
            case 1: goto L_0x00a2;
            case 2: goto L_0x009e;
            case 3: goto L_0x009e;
            case 4: goto L_0x009a;
            case 5: goto L_0x009a;
            case 6: goto L_0x0096;
            case 7: goto L_0x0096;
            case 8: goto L_0x008a;
            case 9: goto L_0x008a;
            case 10: goto L_0x0085;
            default: goto L_0x004f;
        };	 Catch:{ Exception -> 0x00c9 }
    L_0x004f:
        switch(r3) {
            case 20: goto L_0x0081;
            case 21: goto L_0x007d;
            case 22: goto L_0x005c;
            default: goto L_0x0052;
        };	 Catch:{ Exception -> 0x00c9 }
    L_0x0052:
        switch(r3) {
            case 31: goto L_0x0057;
            case 32: goto L_0x005c;
            case 33: goto L_0x005c;
            default: goto L_0x0055;
        };	 Catch:{ Exception -> 0x00c9 }
    L_0x0055:
        goto L_0x00c5;
    L_0x0057:
        parseServiceSolicitationUuid(r11, r5, r4, r7, r12);	 Catch:{ Exception -> 0x00c9 }
        goto L_0x00c5;
    L_0x005c:
        r1 = 2;
        r6 = 32;
        if (r3 != r6) goto L_0x0063;
    L_0x0061:
        r1 = 4;
        goto L_0x0069;
    L_0x0063:
        r6 = 33;
        if (r3 != r6) goto L_0x0069;
    L_0x0067:
        r1 = 16;
    L_0x0069:
        r6 = extractBytes(r11, r5, r1);	 Catch:{ Exception -> 0x00c9 }
        r7 = android.bluetooth.BluetoothUuid.parseUuidFrom(r6);	 Catch:{ Exception -> 0x00c9 }
        r8 = r5 + r1;
        r9 = r4 - r1;
        r8 = extractBytes(r11, r8, r9);	 Catch:{ Exception -> 0x00c9 }
        r14.put(r7, r8);	 Catch:{ Exception -> 0x00c9 }
        goto L_0x00c5;
    L_0x007d:
        parseServiceSolicitationUuid(r11, r5, r4, r6, r12);	 Catch:{ Exception -> 0x00c9 }
        goto L_0x00c5;
    L_0x0081:
        parseServiceSolicitationUuid(r11, r5, r4, r8, r12);	 Catch:{ Exception -> 0x00c9 }
        goto L_0x00c5;
    L_0x0085:
        r1 = r11[r5];	 Catch:{ Exception -> 0x00c9 }
        r16 = r1;
        goto L_0x00c5;
    L_0x008a:
        r1 = new java.lang.String;	 Catch:{ Exception -> 0x00c9 }
        r6 = extractBytes(r11, r5, r4);	 Catch:{ Exception -> 0x00c9 }
        r1.<init>(r6);	 Catch:{ Exception -> 0x00c9 }
        r17 = r1;
        goto L_0x00c5;
    L_0x0096:
        parseServiceUuid(r11, r5, r4, r6, r2);	 Catch:{ Exception -> 0x00c9 }
        goto L_0x00c5;
    L_0x009a:
        parseServiceUuid(r11, r5, r4, r7, r2);	 Catch:{ Exception -> 0x00c9 }
        goto L_0x00c5;
    L_0x009e:
        parseServiceUuid(r11, r5, r4, r8, r2);	 Catch:{ Exception -> 0x00c9 }
        goto L_0x00c5;
    L_0x00a2:
        r6 = r11[r5];	 Catch:{ Exception -> 0x00c9 }
        r15 = r6 & 255;
        goto L_0x00c5;
    L_0x00a7:
        r6 = r5 + 1;
        r6 = r11[r6];	 Catch:{ Exception -> 0x00c9 }
        r6 = r6 & r1;
        r6 = r6 << 8;
        r7 = r11[r5];	 Catch:{ Exception -> 0x00c9 }
        r1 = r1 & r7;
        r6 = r6 + r1;
        r1 = r5 + 2;
        r7 = r4 + -2;
        r1 = extractBytes(r11, r1, r7);	 Catch:{ Exception -> 0x00c9 }
        r13.put(r6, r1);	 Catch:{ Exception -> 0x00c9 }
        goto L_0x00c5;
    L_0x00be:
        r1 = extractBytes(r11, r5, r4);	 Catch:{ Exception -> 0x00c9 }
        r18 = r1;
    L_0x00c5:
        r1 = r5 + r4;
        goto L_0x002b;
    L_0x00c9:
        r0 = move-exception;
        r20 = r2;
        r19 = r5;
        goto L_0x0103;
    L_0x00cf:
        r0 = move-exception;
        r20 = r2;
        r19 = r3;
        goto L_0x0103;
    L_0x00d5:
        r19 = r1;
    L_0x00d7:
        r0 = r2.isEmpty();	 Catch:{ Exception -> 0x00fa }
        if (r0 == 0) goto L_0x00e1;
    L_0x00dd:
        r0 = 0;
        r20 = r0;
        goto L_0x00e3;
    L_0x00e1:
        r20 = r2;
    L_0x00e3:
        r0 = new android.bluetooth.le.ScanRecord;	 Catch:{ Exception -> 0x00f8 }
        r1 = r0;
        r2 = r20;
        r3 = r12;
        r4 = r13;
        r5 = r14;
        r6 = r15;
        r7 = r16;
        r8 = r17;
        r9 = r18;
        r10 = r22;
        r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9, r10);	 Catch:{ Exception -> 0x00f8 }
        return r0;
    L_0x00f8:
        r0 = move-exception;
        goto L_0x0103;
    L_0x00fa:
        r0 = move-exception;
        r20 = r2;
        goto L_0x0103;
    L_0x00fe:
        r0 = move-exception;
        r19 = r1;
        r20 = r2;
    L_0x0103:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "unable to parse scan record: ";
        r1.append(r2);
        r2 = java.util.Arrays.toString(r22);
        r1.append(r2);
        r1 = r1.toString();
        r2 = "ScanRecord";
        android.util.Log.e(r2, r1);
        r21 = new android.bluetooth.le.ScanRecord;
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = -1;
        r7 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r8 = 0;
        r9 = 0;
        r1 = r21;
        r10 = r22;
        r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9, r10);
        return r21;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.bluetooth.le.ScanRecord.parseFromBytes(byte[]):android.bluetooth.le.ScanRecord");
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ScanRecord [mAdvertiseFlags=");
        stringBuilder.append(this.mAdvertiseFlags);
        stringBuilder.append(", mServiceUuids=");
        stringBuilder.append(this.mServiceUuids);
        stringBuilder.append(", mServiceSolicitationUuids=");
        stringBuilder.append(this.mServiceSolicitationUuids);
        stringBuilder.append(", mManufacturerSpecificData=");
        stringBuilder.append(BluetoothLeUtils.toString(this.mManufacturerSpecificData));
        stringBuilder.append(", mServiceData=");
        stringBuilder.append(BluetoothLeUtils.toString(this.mServiceData));
        stringBuilder.append(", mTxPowerLevel=");
        stringBuilder.append(this.mTxPowerLevel);
        stringBuilder.append(", mDeviceName=");
        stringBuilder.append(this.mDeviceName);
        stringBuilder.append(", mTDSData=");
        stringBuilder.append(BluetoothLeUtils.toString(this.mTDSData));
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private static int parseServiceUuid(byte[] scanRecord, int currentPos, int dataLength, int uuidLength, List<ParcelUuid> serviceUuids) {
        while (dataLength > 0) {
            serviceUuids.add(BluetoothUuid.parseUuidFrom(extractBytes(scanRecord, currentPos, uuidLength)));
            dataLength -= uuidLength;
            currentPos += uuidLength;
        }
        return currentPos;
    }

    private static int parseServiceSolicitationUuid(byte[] scanRecord, int currentPos, int dataLength, int uuidLength, List<ParcelUuid> serviceSolicitationUuids) {
        while (dataLength > 0) {
            serviceSolicitationUuids.add(BluetoothUuid.parseUuidFrom(extractBytes(scanRecord, currentPos, uuidLength)));
            dataLength -= uuidLength;
            currentPos += uuidLength;
        }
        return currentPos;
    }

    private static byte[] extractBytes(byte[] scanRecord, int start, int length) {
        byte[] bytes = new byte[length];
        System.arraycopy(scanRecord, start, bytes, 0, length);
        return bytes;
    }
}
