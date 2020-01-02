package android.hardware.radio.V1_4;

import android.internal.hidl.safe_union.V1_0.Monostate;
import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class DataRegStateResult {
    public android.hardware.radio.V1_2.DataRegStateResult base = new android.hardware.radio.V1_2.DataRegStateResult();
    public NrIndicators nrIndicators = new NrIndicators();
    public VopsInfo vopsInfo = new VopsInfo();

    public static final class VopsInfo {
        private byte hidl_d;
        private Object hidl_o;

        public static final class hidl_discriminator {
            public static final byte lteVopsInfo = (byte) 1;
            public static final byte noinit = (byte) 0;

            public static final String getName(byte value) {
                if (value == (byte) 0) {
                    return "noinit";
                }
                if (value != (byte) 1) {
                    return "Unknown";
                }
                return "lteVopsInfo";
            }

            private hidl_discriminator() {
            }
        }

        public VopsInfo() {
            this.hidl_d = (byte) 0;
            this.hidl_o = null;
            this.hidl_o = new Monostate();
        }

        public void noinit(Monostate noinit) {
            this.hidl_d = (byte) 0;
            this.hidl_o = noinit;
        }

        public Monostate noinit() {
            Object obj;
            if (this.hidl_d != (byte) 0) {
                obj = this.hidl_o;
                String className = obj != null ? obj.getClass().getName() : "null";
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Read access to inactive union components is disallowed. Discriminator value is ");
                stringBuilder.append(this.hidl_d);
                stringBuilder.append(" (corresponding to ");
                stringBuilder.append(hidl_discriminator.getName(this.hidl_d));
                stringBuilder.append("), and hidl_o is of type ");
                stringBuilder.append(className);
                stringBuilder.append(".");
                throw new IllegalStateException(stringBuilder.toString());
            }
            obj = this.hidl_o;
            if (obj == null || Monostate.class.isInstance(obj)) {
                return (Monostate) this.hidl_o;
            }
            throw new Error("Union is in a corrupted state.");
        }

        public void lteVopsInfo(LteVopsInfo lteVopsInfo) {
            this.hidl_d = (byte) 1;
            this.hidl_o = lteVopsInfo;
        }

        public LteVopsInfo lteVopsInfo() {
            Object obj;
            if (this.hidl_d != (byte) 1) {
                obj = this.hidl_o;
                String className = obj != null ? obj.getClass().getName() : "null";
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Read access to inactive union components is disallowed. Discriminator value is ");
                stringBuilder.append(this.hidl_d);
                stringBuilder.append(" (corresponding to ");
                stringBuilder.append(hidl_discriminator.getName(this.hidl_d));
                stringBuilder.append("), and hidl_o is of type ");
                stringBuilder.append(className);
                stringBuilder.append(".");
                throw new IllegalStateException(stringBuilder.toString());
            }
            obj = this.hidl_o;
            if (obj == null || LteVopsInfo.class.isInstance(obj)) {
                return (LteVopsInfo) this.hidl_o;
            }
            throw new Error("Union is in a corrupted state.");
        }

        public byte getDiscriminator() {
            return this.hidl_d;
        }

        public final boolean equals(Object otherObject) {
            if (this == otherObject) {
                return true;
            }
            if (otherObject == null || otherObject.getClass() != VopsInfo.class) {
                return false;
            }
            VopsInfo other = (VopsInfo) otherObject;
            if (this.hidl_d == other.hidl_d && HidlSupport.deepEquals(this.hidl_o, other.hidl_o)) {
                return true;
            }
            return false;
        }

        public final int hashCode() {
            return Objects.hash(new Object[]{Integer.valueOf(HidlSupport.deepHashCode(this.hidl_o)), Integer.valueOf(Objects.hashCode(Byte.valueOf(this.hidl_d)))});
        }

        public final String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("{");
            byte b = this.hidl_d;
            if (b == (byte) 0) {
                builder.append(".noinit = ");
                builder.append(noinit());
            } else if (b == (byte) 1) {
                builder.append(".lteVopsInfo = ");
                builder.append(lteVopsInfo());
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown union discriminator (value: ");
                stringBuilder.append(this.hidl_d);
                stringBuilder.append(").");
                throw new Error(stringBuilder.toString());
            }
            builder.append("}");
            return builder.toString();
        }

        public final void readFromParcel(HwParcel parcel) {
            readEmbeddedFromParcel(parcel, parcel.readBuffer(3), 0);
        }

        public static final ArrayList<VopsInfo> readVectorFromParcel(HwParcel parcel) {
            ArrayList<VopsInfo> _hidl_vec = new ArrayList();
            HwBlob _hidl_blob = parcel.readBuffer(16);
            int _hidl_vec_size = _hidl_blob.getInt32(8);
            HwBlob childBlob = parcel.readEmbeddedBuffer((long) (_hidl_vec_size * 3), _hidl_blob.handle(), 0, true);
            _hidl_vec.clear();
            for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                VopsInfo _hidl_vec_element = new VopsInfo();
                _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, (long) (_hidl_index_0 * 3));
                _hidl_vec.add(_hidl_vec_element);
            }
            return _hidl_vec;
        }

        public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
            this.hidl_d = _hidl_blob.getInt8(0 + _hidl_offset);
            byte b = this.hidl_d;
            if (b == (byte) 0) {
                this.hidl_o = new Monostate();
                ((Monostate) this.hidl_o).readEmbeddedFromParcel(parcel, _hidl_blob, 1 + _hidl_offset);
            } else if (b == (byte) 1) {
                this.hidl_o = new LteVopsInfo();
                ((LteVopsInfo) this.hidl_o).readEmbeddedFromParcel(parcel, _hidl_blob, 1 + _hidl_offset);
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown union discriminator (value: ");
                stringBuilder.append(this.hidl_d);
                stringBuilder.append(").");
                throw new IllegalStateException(stringBuilder.toString());
            }
        }

        public final void writeToParcel(HwParcel parcel) {
            HwBlob _hidl_blob = new HwBlob(3);
            writeEmbeddedToBlob(_hidl_blob, 0);
            parcel.writeBuffer(_hidl_blob);
        }

        public static final void writeVectorToParcel(HwParcel parcel, ArrayList<VopsInfo> _hidl_vec) {
            HwBlob _hidl_blob = new HwBlob(16);
            int _hidl_vec_size = _hidl_vec.size();
            _hidl_blob.putInt32(8, _hidl_vec_size);
            _hidl_blob.putBool(12, false);
            HwBlob childBlob = new HwBlob(_hidl_vec_size * 3);
            for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                ((VopsInfo) _hidl_vec.get(_hidl_index_0)).writeEmbeddedToBlob(childBlob, (long) (_hidl_index_0 * 3));
            }
            _hidl_blob.putBlob(0, childBlob);
            parcel.writeBuffer(_hidl_blob);
        }

        public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
            _hidl_blob.putInt8(0 + _hidl_offset, this.hidl_d);
            byte b = this.hidl_d;
            if (b == (byte) 0) {
                noinit().writeEmbeddedToBlob(_hidl_blob, 1 + _hidl_offset);
            } else if (b == (byte) 1) {
                lteVopsInfo().writeEmbeddedToBlob(_hidl_blob, 1 + _hidl_offset);
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown union discriminator (value: ");
                stringBuilder.append(this.hidl_d);
                stringBuilder.append(").");
                throw new Error(stringBuilder.toString());
            }
        }
    }

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != DataRegStateResult.class) {
            return false;
        }
        DataRegStateResult other = (DataRegStateResult) otherObject;
        if (HidlSupport.deepEquals(this.base, other.base) && HidlSupport.deepEquals(this.vopsInfo, other.vopsInfo) && HidlSupport.deepEquals(this.nrIndicators, other.nrIndicators)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(HidlSupport.deepHashCode(this.base)), Integer.valueOf(HidlSupport.deepHashCode(this.vopsInfo)), Integer.valueOf(HidlSupport.deepHashCode(this.nrIndicators))});
    }

    public final String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append(".base = ");
        builder.append(this.base);
        builder.append(", .vopsInfo = ");
        builder.append(this.vopsInfo);
        builder.append(", .nrIndicators = ");
        builder.append(this.nrIndicators);
        builder.append("}");
        return builder.toString();
    }

    public final void readFromParcel(HwParcel parcel) {
        readEmbeddedFromParcel(parcel, parcel.readBuffer(112), 0);
    }

    public static final ArrayList<DataRegStateResult> readVectorFromParcel(HwParcel parcel) {
        ArrayList<DataRegStateResult> _hidl_vec = new ArrayList();
        HwBlob _hidl_blob = parcel.readBuffer(16);
        int _hidl_vec_size = _hidl_blob.getInt32(8);
        HwBlob childBlob = parcel.readEmbeddedBuffer((long) (_hidl_vec_size * 112), _hidl_blob.handle(), 0, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            DataRegStateResult _hidl_vec_element = new DataRegStateResult();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, (long) (_hidl_index_0 * 112));
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.base.readEmbeddedFromParcel(parcel, _hidl_blob, 0 + _hidl_offset);
        this.vopsInfo.readEmbeddedFromParcel(parcel, _hidl_blob, 104 + _hidl_offset);
        this.nrIndicators.readEmbeddedFromParcel(parcel, _hidl_blob, 107 + _hidl_offset);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(112);
        writeEmbeddedToBlob(_hidl_blob, 0);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<DataRegStateResult> _hidl_vec) {
        HwBlob _hidl_blob = new HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8, _hidl_vec_size);
        _hidl_blob.putBool(12, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 112);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            ((DataRegStateResult) _hidl_vec.get(_hidl_index_0)).writeEmbeddedToBlob(childBlob, (long) (_hidl_index_0 * 112));
        }
        _hidl_blob.putBlob(0, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
        this.base.writeEmbeddedToBlob(_hidl_blob, 0 + _hidl_offset);
        this.vopsInfo.writeEmbeddedToBlob(_hidl_blob, 104 + _hidl_offset);
        this.nrIndicators.writeEmbeddedToBlob(_hidl_blob, 107 + _hidl_offset);
    }
}
