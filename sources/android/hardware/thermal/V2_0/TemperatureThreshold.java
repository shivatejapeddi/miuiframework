package android.hardware.thermal.V2_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public final class TemperatureThreshold {
    public float[] coldThrottlingThresholds = new float[7];
    public float[] hotThrottlingThresholds = new float[7];
    public String name = new String();
    public int type;
    public float vrThrottlingThreshold;

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != TemperatureThreshold.class) {
            return false;
        }
        TemperatureThreshold other = (TemperatureThreshold) otherObject;
        if (this.type == other.type && HidlSupport.deepEquals(this.name, other.name) && HidlSupport.deepEquals(this.hotThrottlingThresholds, other.hotThrottlingThresholds) && HidlSupport.deepEquals(this.coldThrottlingThresholds, other.coldThrottlingThresholds) && this.vrThrottlingThreshold == other.vrThrottlingThreshold) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.type))), Integer.valueOf(HidlSupport.deepHashCode(this.name)), Integer.valueOf(HidlSupport.deepHashCode(this.hotThrottlingThresholds)), Integer.valueOf(HidlSupport.deepHashCode(this.coldThrottlingThresholds)), Integer.valueOf(HidlSupport.deepHashCode(Float.valueOf(this.vrThrottlingThreshold)))});
    }

    public final String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append(".type = ");
        builder.append(TemperatureType.toString(this.type));
        builder.append(", .name = ");
        builder.append(this.name);
        builder.append(", .hotThrottlingThresholds = ");
        builder.append(Arrays.toString(this.hotThrottlingThresholds));
        builder.append(", .coldThrottlingThresholds = ");
        builder.append(Arrays.toString(this.coldThrottlingThresholds));
        builder.append(", .vrThrottlingThreshold = ");
        builder.append(this.vrThrottlingThreshold);
        builder.append("}");
        return builder.toString();
    }

    public final void readFromParcel(HwParcel parcel) {
        readEmbeddedFromParcel(parcel, parcel.readBuffer(88), 0);
    }

    public static final ArrayList<TemperatureThreshold> readVectorFromParcel(HwParcel parcel) {
        ArrayList<TemperatureThreshold> _hidl_vec = new ArrayList();
        HwBlob _hidl_blob = parcel.readBuffer(16);
        int _hidl_vec_size = _hidl_blob.getInt32(8);
        HwBlob childBlob = parcel.readEmbeddedBuffer((long) (_hidl_vec_size * 88), _hidl_blob.handle(), 0, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            TemperatureThreshold _hidl_vec_element = new TemperatureThreshold();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, (long) (_hidl_index_0 * 88));
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        HwBlob hwBlob = _hidl_blob;
        this.type = hwBlob.getInt32(_hidl_offset + 0);
        this.name = hwBlob.getString(_hidl_offset + 8);
        parcel.readEmbeddedBuffer((long) (this.name.getBytes().length + 1), _hidl_blob.handle(), (_hidl_offset + 8) + 0, false);
        hwBlob.copyToFloatArray(_hidl_offset + 24, this.hotThrottlingThresholds, 7);
        hwBlob.copyToFloatArray(_hidl_offset + 52, this.coldThrottlingThresholds, 7);
        this.vrThrottlingThreshold = hwBlob.getFloat(_hidl_offset + 80);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(88);
        writeEmbeddedToBlob(_hidl_blob, 0);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<TemperatureThreshold> _hidl_vec) {
        HwBlob _hidl_blob = new HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8, _hidl_vec_size);
        _hidl_blob.putBool(12, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 88);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            ((TemperatureThreshold) _hidl_vec.get(_hidl_index_0)).writeEmbeddedToBlob(childBlob, (long) (_hidl_index_0 * 88));
        }
        _hidl_blob.putBlob(0, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(0 + _hidl_offset, this.type);
        _hidl_blob.putString(8 + _hidl_offset, this.name);
        long _hidl_array_offset_0 = 24 + _hidl_offset;
        float[] _hidl_array_item_0 = this.hotThrottlingThresholds;
        String str = "Array element is not of the expected length";
        if (_hidl_array_item_0 == null || _hidl_array_item_0.length != 7) {
            throw new IllegalArgumentException(str);
        }
        _hidl_blob.putFloatArray(_hidl_array_offset_0, _hidl_array_item_0);
        _hidl_array_offset_0 = 52 + _hidl_offset;
        _hidl_array_item_0 = this.coldThrottlingThresholds;
        if (_hidl_array_item_0 == null || _hidl_array_item_0.length != 7) {
            throw new IllegalArgumentException(str);
        }
        _hidl_blob.putFloatArray(_hidl_array_offset_0, _hidl_array_item_0);
        _hidl_blob.putFloat(80 + _hidl_offset, this.vrThrottlingThreshold);
    }
}
