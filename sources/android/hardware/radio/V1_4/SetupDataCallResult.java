package android.hardware.radio.V1_4;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class SetupDataCallResult {
    public int active;
    public ArrayList<String> addresses = new ArrayList();
    public int cause;
    public int cid;
    public ArrayList<String> dnses = new ArrayList();
    public ArrayList<String> gateways = new ArrayList();
    public String ifname = new String();
    public int mtu;
    public ArrayList<String> pcscf = new ArrayList();
    public int suggestedRetryTime;
    public int type;

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != SetupDataCallResult.class) {
            return false;
        }
        SetupDataCallResult other = (SetupDataCallResult) otherObject;
        if (this.cause == other.cause && this.suggestedRetryTime == other.suggestedRetryTime && this.cid == other.cid && this.active == other.active && this.type == other.type && HidlSupport.deepEquals(this.ifname, other.ifname) && HidlSupport.deepEquals(this.addresses, other.addresses) && HidlSupport.deepEquals(this.dnses, other.dnses) && HidlSupport.deepEquals(this.gateways, other.gateways) && HidlSupport.deepEquals(this.pcscf, other.pcscf) && this.mtu == other.mtu) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.cause))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.suggestedRetryTime))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.cid))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.active))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.type))), Integer.valueOf(HidlSupport.deepHashCode(this.ifname)), Integer.valueOf(HidlSupport.deepHashCode(this.addresses)), Integer.valueOf(HidlSupport.deepHashCode(this.dnses)), Integer.valueOf(HidlSupport.deepHashCode(this.gateways)), Integer.valueOf(HidlSupport.deepHashCode(this.pcscf)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.mtu)))});
    }

    public final String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append(".cause = ");
        builder.append(DataCallFailCause.toString(this.cause));
        builder.append(", .suggestedRetryTime = ");
        builder.append(this.suggestedRetryTime);
        builder.append(", .cid = ");
        builder.append(this.cid);
        builder.append(", .active = ");
        builder.append(DataConnActiveStatus.toString(this.active));
        builder.append(", .type = ");
        builder.append(PdpProtocolType.toString(this.type));
        builder.append(", .ifname = ");
        builder.append(this.ifname);
        builder.append(", .addresses = ");
        builder.append(this.addresses);
        builder.append(", .dnses = ");
        builder.append(this.dnses);
        builder.append(", .gateways = ");
        builder.append(this.gateways);
        builder.append(", .pcscf = ");
        builder.append(this.pcscf);
        builder.append(", .mtu = ");
        builder.append(this.mtu);
        builder.append("}");
        return builder.toString();
    }

    public final void readFromParcel(HwParcel parcel) {
        readEmbeddedFromParcel(parcel, parcel.readBuffer(112), 0);
    }

    public static final ArrayList<SetupDataCallResult> readVectorFromParcel(HwParcel parcel) {
        ArrayList<SetupDataCallResult> _hidl_vec = new ArrayList();
        HwBlob _hidl_blob = parcel.readBuffer(16);
        int _hidl_vec_size = _hidl_blob.getInt32(8);
        HwBlob childBlob = parcel.readEmbeddedBuffer((long) (_hidl_vec_size * 112), _hidl_blob.handle(), 0, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            SetupDataCallResult _hidl_vec_element = new SetupDataCallResult();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, (long) (_hidl_index_0 * 112));
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        int _hidl_index_0;
        String _hidl_vec_element;
        HwBlob hwBlob = _hidl_blob;
        this.cause = hwBlob.getInt32(_hidl_offset + 0);
        this.suggestedRetryTime = hwBlob.getInt32(_hidl_offset + 4);
        this.cid = hwBlob.getInt32(_hidl_offset + 8);
        this.active = hwBlob.getInt32(_hidl_offset + 12);
        this.type = hwBlob.getInt32(_hidl_offset + 16);
        this.ifname = hwBlob.getString(_hidl_offset + 24);
        parcel.readEmbeddedBuffer((long) (this.ifname.getBytes().length + 1), _hidl_blob.handle(), (_hidl_offset + 24) + 0, false);
        int _hidl_vec_size = hwBlob.getInt32((_hidl_offset + 40) + 8);
        HwBlob childBlob = parcel.readEmbeddedBuffer((long) (_hidl_vec_size * 16), _hidl_blob.handle(), (_hidl_offset + 40) + 0, true);
        this.addresses.clear();
        for (_hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec_element = new String();
            _hidl_vec_element = childBlob.getString((long) (_hidl_index_0 * 16));
            parcel.readEmbeddedBuffer((long) (_hidl_vec_element.getBytes().length + 1), childBlob.handle(), (long) ((_hidl_index_0 * 16) + 0), false);
            this.addresses.add(_hidl_vec_element);
        }
        _hidl_vec_size = hwBlob.getInt32((_hidl_offset + 56) + 8);
        childBlob = parcel.readEmbeddedBuffer((long) (_hidl_vec_size * 16), _hidl_blob.handle(), (_hidl_offset + 56) + 0, true);
        this.dnses.clear();
        for (_hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec_element = new String();
            _hidl_vec_element = childBlob.getString((long) (_hidl_index_0 * 16));
            parcel.readEmbeddedBuffer((long) (_hidl_vec_element.getBytes().length + 1), childBlob.handle(), (long) ((_hidl_index_0 * 16) + 0), false);
            this.dnses.add(_hidl_vec_element);
        }
        _hidl_vec_size = hwBlob.getInt32((_hidl_offset + 72) + 8);
        childBlob = parcel.readEmbeddedBuffer((long) (_hidl_vec_size * 16), _hidl_blob.handle(), (_hidl_offset + 72) + 0, true);
        this.gateways.clear();
        for (_hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec_element = new String();
            _hidl_vec_element = childBlob.getString((long) (_hidl_index_0 * 16));
            parcel.readEmbeddedBuffer((long) (_hidl_vec_element.getBytes().length + 1), childBlob.handle(), (long) ((_hidl_index_0 * 16) + 0), false);
            this.gateways.add(_hidl_vec_element);
        }
        int _hidl_vec_size2 = hwBlob.getInt32((_hidl_offset + 88) + 8);
        HwBlob childBlob2 = parcel.readEmbeddedBuffer((long) (_hidl_vec_size2 * 16), _hidl_blob.handle(), (_hidl_offset + 88) + 0, true);
        this.pcscf.clear();
        for (int _hidl_index_02 = 0; _hidl_index_02 < _hidl_vec_size2; _hidl_index_02++) {
            String _hidl_vec_element2 = new String();
            _hidl_vec_element2 = childBlob2.getString((long) (_hidl_index_02 * 16));
            parcel.readEmbeddedBuffer((long) (_hidl_vec_element2.getBytes().length + 1), childBlob2.handle(), (long) ((_hidl_index_02 * 16) + 0), false);
            this.pcscf.add(_hidl_vec_element2);
        }
        this.mtu = hwBlob.getInt32(_hidl_offset + 104);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(112);
        writeEmbeddedToBlob(_hidl_blob, 0);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<SetupDataCallResult> _hidl_vec) {
        HwBlob _hidl_blob = new HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8, _hidl_vec_size);
        _hidl_blob.putBool(12, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 112);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            ((SetupDataCallResult) _hidl_vec.get(_hidl_index_0)).writeEmbeddedToBlob(childBlob, (long) (_hidl_index_0 * 112));
        }
        _hidl_blob.putBlob(0, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
        int _hidl_index_0;
        HwBlob hwBlob = _hidl_blob;
        hwBlob.putInt32(_hidl_offset + 0, this.cause);
        hwBlob.putInt32(_hidl_offset + 4, this.suggestedRetryTime);
        hwBlob.putInt32(_hidl_offset + 8, this.cid);
        hwBlob.putInt32(_hidl_offset + 12, this.active);
        hwBlob.putInt32(_hidl_offset + 16, this.type);
        hwBlob.putString(_hidl_offset + 24, this.ifname);
        int _hidl_vec_size = this.addresses.size();
        hwBlob.putInt32((_hidl_offset + 40) + 8, _hidl_vec_size);
        hwBlob.putBool((_hidl_offset + 40) + 12, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 16);
        for (_hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            childBlob.putString((long) (_hidl_index_0 * 16), (String) this.addresses.get(_hidl_index_0));
        }
        hwBlob.putBlob((_hidl_offset + 40) + 0, childBlob);
        _hidl_vec_size = this.dnses.size();
        hwBlob.putInt32((_hidl_offset + 56) + 8, _hidl_vec_size);
        hwBlob.putBool((_hidl_offset + 56) + 12, false);
        childBlob = new HwBlob(_hidl_vec_size * 16);
        for (_hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            childBlob.putString((long) (_hidl_index_0 * 16), (String) this.dnses.get(_hidl_index_0));
        }
        hwBlob.putBlob((_hidl_offset + 56) + 0, childBlob);
        _hidl_vec_size = this.gateways.size();
        hwBlob.putInt32((_hidl_offset + 72) + 8, _hidl_vec_size);
        hwBlob.putBool((_hidl_offset + 72) + 12, false);
        childBlob = new HwBlob(_hidl_vec_size * 16);
        for (_hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            childBlob.putString((long) (_hidl_index_0 * 16), (String) this.gateways.get(_hidl_index_0));
        }
        hwBlob.putBlob((_hidl_offset + 72) + 0, childBlob);
        _hidl_vec_size = this.pcscf.size();
        hwBlob.putInt32((_hidl_offset + 88) + 8, _hidl_vec_size);
        hwBlob.putBool((_hidl_offset + 88) + 12, false);
        HwBlob childBlob2 = new HwBlob(_hidl_vec_size * 16);
        for (int _hidl_index_02 = 0; _hidl_index_02 < _hidl_vec_size; _hidl_index_02++) {
            childBlob2.putString((long) (_hidl_index_02 * 16), (String) this.pcscf.get(_hidl_index_02));
        }
        hwBlob.putBlob((_hidl_offset + 88) + 0, childBlob2);
        hwBlob.putInt32(_hidl_offset + 104, this.mtu);
    }
}
