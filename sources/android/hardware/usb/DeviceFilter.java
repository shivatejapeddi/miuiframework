package android.hardware.usb;

import android.util.Slog;
import com.android.internal.util.dump.DualDumpOutputStream;
import com.miui.internal.search.Function;
import java.io.IOException;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class DeviceFilter {
    private static final String TAG = DeviceFilter.class.getSimpleName();
    public final int mClass;
    public final String mManufacturerName;
    public final int mProductId;
    public final String mProductName;
    public final int mProtocol;
    public final String mSerialNumber;
    public final int mSubclass;
    public final int mVendorId;

    public DeviceFilter(int vid, int pid, int clasz, int subclass, int protocol, String manufacturer, String product, String serialnum) {
        this.mVendorId = vid;
        this.mProductId = pid;
        this.mClass = clasz;
        this.mSubclass = subclass;
        this.mProtocol = protocol;
        this.mManufacturerName = manufacturer;
        this.mProductName = product;
        this.mSerialNumber = serialnum;
    }

    public DeviceFilter(UsbDevice device) {
        this.mVendorId = device.getVendorId();
        this.mProductId = device.getProductId();
        this.mClass = device.getDeviceClass();
        this.mSubclass = device.getDeviceSubclass();
        this.mProtocol = device.getDeviceProtocol();
        this.mManufacturerName = device.getManufacturerName();
        this.mProductName = device.getProductName();
        this.mSerialNumber = device.getSerialNumber();
    }

    public static DeviceFilter read(XmlPullParser parser) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser = parser;
        int count = parser.getAttributeCount();
        int i = 0;
        String serialNumber = null;
        String productName = null;
        String manufacturerName = null;
        int deviceProtocol = -1;
        int deviceSubclass = -1;
        int deviceClass = -1;
        int productId = -1;
        int vendorId = -1;
        while (i < count) {
            String name = xmlPullParser.getAttributeName(i);
            String value = xmlPullParser.getAttributeValue(i);
            if ("manufacturer-name".equals(name)) {
                manufacturerName = value;
            } else if ("product-name".equals(name)) {
                productName = value;
            } else if ("serial-number".equals(name)) {
                serialNumber = value;
            } else {
                int radix;
                String value2;
                if (value == null || value.length() <= 2 || value.charAt(0) != '0' || !(value.charAt(1) == StateProperty.TARGET_X || value.charAt(1) == 'X')) {
                    radix = 10;
                    value2 = value;
                } else {
                    radix = 16;
                    value2 = value.substring(2);
                }
                try {
                    int intValue = Integer.parseInt(value2, radix);
                    if ("vendor-id".equals(name)) {
                        vendorId = intValue;
                    } else if ("product-id".equals(name)) {
                        productId = intValue;
                    } else if (Function.CLASS.equals(name)) {
                        deviceClass = intValue;
                    } else if ("subclass".equals(name)) {
                        deviceSubclass = intValue;
                    } else if ("protocol".equals(name)) {
                        deviceProtocol = intValue;
                    }
                } catch (NumberFormatException e) {
                    NumberFormatException e2 = e2;
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("invalid number for field ");
                    stringBuilder.append(name);
                    Slog.e(str, stringBuilder.toString(), e2);
                }
            }
            i++;
            xmlPullParser = parser;
        }
        return new DeviceFilter(vendorId, productId, deviceClass, deviceSubclass, deviceProtocol, manufacturerName, productName, serialNumber);
    }

    public void write(XmlSerializer serializer) throws IOException {
        String str = "usb-device";
        serializer.startTag(null, str);
        int i = this.mVendorId;
        if (i != -1) {
            serializer.attribute(null, "vendor-id", Integer.toString(i));
        }
        i = this.mProductId;
        if (i != -1) {
            serializer.attribute(null, "product-id", Integer.toString(i));
        }
        i = this.mClass;
        if (i != -1) {
            serializer.attribute(null, Function.CLASS, Integer.toString(i));
        }
        i = this.mSubclass;
        if (i != -1) {
            serializer.attribute(null, "subclass", Integer.toString(i));
        }
        i = this.mProtocol;
        if (i != -1) {
            serializer.attribute(null, "protocol", Integer.toString(i));
        }
        String str2 = this.mManufacturerName;
        if (str2 != null) {
            serializer.attribute(null, "manufacturer-name", str2);
        }
        str2 = this.mProductName;
        if (str2 != null) {
            serializer.attribute(null, "product-name", str2);
        }
        str2 = this.mSerialNumber;
        if (str2 != null) {
            serializer.attribute(null, "serial-number", str2);
        }
        serializer.endTag(null, str);
    }

    private boolean matches(int clasz, int subclass, int protocol) {
        int i = this.mClass;
        if (i == -1 || clasz == i) {
            i = this.mSubclass;
            if (i == -1 || subclass == i) {
                i = this.mProtocol;
                if (i == -1 || protocol == i) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean matches(UsbDevice device) {
        if (this.mVendorId != -1 && device.getVendorId() != this.mVendorId) {
            return false;
        }
        if (this.mProductId != -1 && device.getProductId() != this.mProductId) {
            return false;
        }
        if (this.mManufacturerName != null && device.getManufacturerName() == null) {
            return false;
        }
        if (this.mProductName != null && device.getProductName() == null) {
            return false;
        }
        if (this.mSerialNumber != null && device.getSerialNumber() == null) {
            return false;
        }
        if (this.mManufacturerName != null && device.getManufacturerName() != null && !this.mManufacturerName.equals(device.getManufacturerName())) {
            return false;
        }
        if (this.mProductName != null && device.getProductName() != null && !this.mProductName.equals(device.getProductName())) {
            return false;
        }
        if (this.mSerialNumber != null && device.getSerialNumber() != null && !this.mSerialNumber.equals(device.getSerialNumber())) {
            return false;
        }
        if (matches(device.getDeviceClass(), device.getDeviceSubclass(), device.getDeviceProtocol())) {
            return true;
        }
        int count = device.getInterfaceCount();
        for (int i = 0; i < count; i++) {
            UsbInterface intf = device.getInterface(i);
            if (matches(intf.getInterfaceClass(), intf.getInterfaceSubclass(), intf.getInterfaceProtocol())) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(DeviceFilter device) {
        int i = this.mVendorId;
        if (i != -1 && device.mVendorId != i) {
            return false;
        }
        i = this.mProductId;
        if (i != -1 && device.mProductId != i) {
            return false;
        }
        String str = this.mManufacturerName;
        if (str != null && !Objects.equals(str, device.mManufacturerName)) {
            return false;
        }
        str = this.mProductName;
        if (str != null && !Objects.equals(str, device.mProductName)) {
            return false;
        }
        str = this.mSerialNumber;
        if (str == null || Objects.equals(str, device.mSerialNumber)) {
            return matches(device.mClass, device.mSubclass, device.mProtocol);
        }
        return false;
    }

    /* JADX WARNING: Missing block: B:52:0x0072, code skipped:
            if (r3.equals(r0) == false) goto L_0x0090;
     */
    /* JADX WARNING: Missing block: B:58:0x0080, code skipped:
            if (r3.equals(r0) == false) goto L_0x0090;
     */
    public boolean equals(java.lang.Object r10) {
        /*
        r9 = this;
        r0 = r9.mVendorId;
        r1 = -1;
        r2 = 0;
        if (r0 == r1) goto L_0x013b;
    L_0x0006:
        r3 = r9.mProductId;
        if (r3 == r1) goto L_0x013b;
    L_0x000a:
        r4 = r9.mClass;
        if (r4 == r1) goto L_0x013b;
    L_0x000e:
        r5 = r9.mSubclass;
        if (r5 == r1) goto L_0x013b;
    L_0x0012:
        r6 = r9.mProtocol;
        if (r6 != r1) goto L_0x0018;
    L_0x0016:
        goto L_0x013b;
    L_0x0018:
        r1 = r10 instanceof android.hardware.usb.DeviceFilter;
        r7 = 1;
        if (r1 == 0) goto L_0x0093;
    L_0x001d:
        r1 = r10;
        r1 = (android.hardware.usb.DeviceFilter) r1;
        r8 = r1.mVendorId;
        if (r8 != r0) goto L_0x0092;
    L_0x0024:
        r0 = r1.mProductId;
        if (r0 != r3) goto L_0x0092;
    L_0x0028:
        r0 = r1.mClass;
        if (r0 != r4) goto L_0x0092;
    L_0x002c:
        r0 = r1.mSubclass;
        if (r0 != r5) goto L_0x0092;
    L_0x0030:
        r0 = r1.mProtocol;
        if (r0 == r6) goto L_0x0035;
    L_0x0034:
        goto L_0x0092;
    L_0x0035:
        r0 = r1.mManufacturerName;
        if (r0 == 0) goto L_0x003d;
    L_0x0039:
        r0 = r9.mManufacturerName;
        if (r0 == 0) goto L_0x0065;
    L_0x003d:
        r0 = r1.mManufacturerName;
        if (r0 != 0) goto L_0x0045;
    L_0x0041:
        r0 = r9.mManufacturerName;
        if (r0 != 0) goto L_0x0065;
    L_0x0045:
        r0 = r1.mProductName;
        if (r0 == 0) goto L_0x004d;
    L_0x0049:
        r0 = r9.mProductName;
        if (r0 == 0) goto L_0x0065;
    L_0x004d:
        r0 = r1.mProductName;
        if (r0 != 0) goto L_0x0055;
    L_0x0051:
        r0 = r9.mProductName;
        if (r0 != 0) goto L_0x0065;
    L_0x0055:
        r0 = r1.mSerialNumber;
        if (r0 == 0) goto L_0x005d;
    L_0x0059:
        r0 = r9.mSerialNumber;
        if (r0 == 0) goto L_0x0065;
    L_0x005d:
        r0 = r1.mSerialNumber;
        if (r0 != 0) goto L_0x0066;
    L_0x0061:
        r0 = r9.mSerialNumber;
        if (r0 == 0) goto L_0x0066;
    L_0x0065:
        return r2;
    L_0x0066:
        r0 = r1.mManufacturerName;
        if (r0 == 0) goto L_0x0074;
    L_0x006a:
        r3 = r9.mManufacturerName;
        if (r3 == 0) goto L_0x0074;
    L_0x006e:
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x0090;
    L_0x0074:
        r0 = r1.mProductName;
        if (r0 == 0) goto L_0x0082;
    L_0x0078:
        r3 = r9.mProductName;
        if (r3 == 0) goto L_0x0082;
    L_0x007c:
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x0090;
    L_0x0082:
        r0 = r1.mSerialNumber;
        if (r0 == 0) goto L_0x0091;
    L_0x0086:
        r3 = r9.mSerialNumber;
        if (r3 == 0) goto L_0x0091;
    L_0x008a:
        r0 = r3.equals(r0);
        if (r0 != 0) goto L_0x0091;
    L_0x0090:
        return r2;
    L_0x0091:
        return r7;
    L_0x0092:
        return r2;
    L_0x0093:
        r0 = r10 instanceof android.hardware.usb.UsbDevice;
        if (r0 == 0) goto L_0x013a;
    L_0x0097:
        r0 = r10;
        r0 = (android.hardware.usb.UsbDevice) r0;
        r1 = r0.getVendorId();
        r3 = r9.mVendorId;
        if (r1 != r3) goto L_0x0139;
    L_0x00a2:
        r1 = r0.getProductId();
        r3 = r9.mProductId;
        if (r1 != r3) goto L_0x0139;
    L_0x00aa:
        r1 = r0.getDeviceClass();
        r3 = r9.mClass;
        if (r1 != r3) goto L_0x0139;
    L_0x00b2:
        r1 = r0.getDeviceSubclass();
        r3 = r9.mSubclass;
        if (r1 != r3) goto L_0x0139;
    L_0x00ba:
        r1 = r0.getDeviceProtocol();
        r3 = r9.mProtocol;
        if (r1 == r3) goto L_0x00c4;
    L_0x00c2:
        goto L_0x0139;
    L_0x00c4:
        r1 = r9.mManufacturerName;
        if (r1 == 0) goto L_0x00ce;
    L_0x00c8:
        r1 = r0.getManufacturerName();
        if (r1 == 0) goto L_0x0100;
    L_0x00ce:
        r1 = r9.mManufacturerName;
        if (r1 != 0) goto L_0x00d8;
    L_0x00d2:
        r1 = r0.getManufacturerName();
        if (r1 != 0) goto L_0x0100;
    L_0x00d8:
        r1 = r9.mProductName;
        if (r1 == 0) goto L_0x00e2;
    L_0x00dc:
        r1 = r0.getProductName();
        if (r1 == 0) goto L_0x0100;
    L_0x00e2:
        r1 = r9.mProductName;
        if (r1 != 0) goto L_0x00ec;
    L_0x00e6:
        r1 = r0.getProductName();
        if (r1 != 0) goto L_0x0100;
    L_0x00ec:
        r1 = r9.mSerialNumber;
        if (r1 == 0) goto L_0x00f6;
    L_0x00f0:
        r1 = r0.getSerialNumber();
        if (r1 == 0) goto L_0x0100;
    L_0x00f6:
        r1 = r9.mSerialNumber;
        if (r1 != 0) goto L_0x0101;
    L_0x00fa:
        r1 = r0.getSerialNumber();
        if (r1 == 0) goto L_0x0101;
    L_0x0100:
        return r2;
    L_0x0101:
        r1 = r0.getManufacturerName();
        if (r1 == 0) goto L_0x0113;
    L_0x0107:
        r1 = r9.mManufacturerName;
        r3 = r0.getManufacturerName();
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0137;
    L_0x0113:
        r1 = r0.getProductName();
        if (r1 == 0) goto L_0x0125;
    L_0x0119:
        r1 = r9.mProductName;
        r3 = r0.getProductName();
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0137;
    L_0x0125:
        r1 = r0.getSerialNumber();
        if (r1 == 0) goto L_0x0138;
    L_0x012b:
        r1 = r9.mSerialNumber;
        r3 = r0.getSerialNumber();
        r1 = r1.equals(r3);
        if (r1 != 0) goto L_0x0138;
    L_0x0137:
        return r2;
    L_0x0138:
        return r7;
    L_0x0139:
        return r2;
    L_0x013a:
        return r2;
    L_0x013b:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.usb.DeviceFilter.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        return ((this.mVendorId << 16) | this.mProductId) ^ (((this.mClass << 16) | (this.mSubclass << 8)) | this.mProtocol);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DeviceFilter[mVendorId=");
        stringBuilder.append(this.mVendorId);
        stringBuilder.append(",mProductId=");
        stringBuilder.append(this.mProductId);
        stringBuilder.append(",mClass=");
        stringBuilder.append(this.mClass);
        stringBuilder.append(",mSubclass=");
        stringBuilder.append(this.mSubclass);
        stringBuilder.append(",mProtocol=");
        stringBuilder.append(this.mProtocol);
        stringBuilder.append(",mManufacturerName=");
        stringBuilder.append(this.mManufacturerName);
        stringBuilder.append(",mProductName=");
        stringBuilder.append(this.mProductName);
        stringBuilder.append(",mSerialNumber=");
        stringBuilder.append(this.mSerialNumber);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public void dump(DualDumpOutputStream dump, String idName, long id) {
        long token = dump.start(idName, id);
        dump.write("vendor_id", 1120986464257L, this.mVendorId);
        dump.write("product_id", 1120986464258L, this.mProductId);
        dump.write(Function.CLASS, 1120986464259L, this.mClass);
        dump.write("subclass", 1120986464260L, this.mSubclass);
        dump.write("protocol", 1120986464261L, this.mProtocol);
        dump.write("manufacturer_name", 1138166333446L, this.mManufacturerName);
        dump.write("product_name", 1138166333447L, this.mProductName);
        dump.write("serial_number", 1138166333448L, this.mSerialNumber);
        dump.end(token);
    }
}
