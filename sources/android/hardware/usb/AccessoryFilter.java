package android.hardware.usb;

import android.media.midi.MidiDeviceInfo;
import com.android.internal.util.dump.DualDumpOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class AccessoryFilter {
    public final String mManufacturer;
    public final String mModel;
    public final String mVersion;

    public AccessoryFilter(String manufacturer, String model, String version) {
        this.mManufacturer = manufacturer;
        this.mModel = model;
        this.mVersion = version;
    }

    public AccessoryFilter(UsbAccessory accessory) {
        this.mManufacturer = accessory.getManufacturer();
        this.mModel = accessory.getModel();
        this.mVersion = accessory.getVersion();
    }

    public static AccessoryFilter read(XmlPullParser parser) throws XmlPullParserException, IOException {
        String manufacturer = null;
        String model = null;
        String version = null;
        int count = parser.getAttributeCount();
        for (int i = 0; i < count; i++) {
            String name = parser.getAttributeName(i);
            String value = parser.getAttributeValue(i);
            if (MidiDeviceInfo.PROPERTY_MANUFACTURER.equals(name)) {
                manufacturer = value;
            } else if ("model".equals(name)) {
                model = value;
            } else if ("version".equals(name)) {
                version = value;
            }
        }
        return new AccessoryFilter(manufacturer, model, version);
    }

    public void write(XmlSerializer serializer) throws IOException {
        String str = "usb-accessory";
        serializer.startTag(null, str);
        String str2 = this.mManufacturer;
        if (str2 != null) {
            serializer.attribute(null, MidiDeviceInfo.PROPERTY_MANUFACTURER, str2);
        }
        str2 = this.mModel;
        if (str2 != null) {
            serializer.attribute(null, "model", str2);
        }
        str2 = this.mVersion;
        if (str2 != null) {
            serializer.attribute(null, "version", str2);
        }
        serializer.endTag(null, str);
    }

    public boolean matches(UsbAccessory acc) {
        boolean z = false;
        if (this.mManufacturer != null && !acc.getManufacturer().equals(this.mManufacturer)) {
            return false;
        }
        if (this.mModel != null && !acc.getModel().equals(this.mModel)) {
            return false;
        }
        if (this.mVersion == null || acc.getVersion().equals(this.mVersion)) {
            z = true;
        }
        return z;
    }

    public boolean contains(AccessoryFilter accessory) {
        String str = this.mManufacturer;
        boolean z = false;
        if (str != null && !Objects.equals(accessory.mManufacturer, str)) {
            return false;
        }
        str = this.mModel;
        if (str != null && !Objects.equals(accessory.mModel, str)) {
            return false;
        }
        str = this.mVersion;
        if (str == null || Objects.equals(accessory.mVersion, str)) {
            z = true;
        }
        return z;
    }

    public boolean equals(Object obj) {
        String str = this.mManufacturer;
        boolean z = false;
        if (str == null || this.mModel == null || this.mVersion == null) {
            return false;
        }
        if (obj instanceof AccessoryFilter) {
            AccessoryFilter filter = (AccessoryFilter) obj;
            if (str.equals(filter.mManufacturer) && this.mModel.equals(filter.mModel) && this.mVersion.equals(filter.mVersion)) {
                z = true;
            }
            return z;
        } else if (!(obj instanceof UsbAccessory)) {
            return false;
        } else {
            UsbAccessory accessory = (UsbAccessory) obj;
            if (str.equals(accessory.getManufacturer()) && this.mModel.equals(accessory.getModel()) && this.mVersion.equals(accessory.getVersion())) {
                z = true;
            }
            return z;
        }
    }

    public int hashCode() {
        String str = this.mManufacturer;
        int i = 0;
        int hashCode = str == null ? 0 : str.hashCode();
        String str2 = this.mModel;
        hashCode ^= str2 == null ? 0 : str2.hashCode();
        str2 = this.mVersion;
        if (str2 != null) {
            i = str2.hashCode();
        }
        return hashCode ^ i;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AccessoryFilter[mManufacturer=\"");
        stringBuilder.append(this.mManufacturer);
        stringBuilder.append("\", mModel=\"");
        stringBuilder.append(this.mModel);
        stringBuilder.append("\", mVersion=\"");
        stringBuilder.append(this.mVersion);
        stringBuilder.append("\"]");
        return stringBuilder.toString();
    }

    public void dump(DualDumpOutputStream dump, String idName, long id) {
        long token = dump.start(idName, id);
        dump.write(MidiDeviceInfo.PROPERTY_MANUFACTURER, 1138166333441L, this.mManufacturer);
        dump.write("model", 1138166333442L, this.mModel);
        dump.write("version", 1138166333443L, this.mVersion);
        dump.end(token);
    }
}
