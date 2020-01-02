package android.hardware.radio.V1_4;

import android.hardware.radio.V1_2.CellConnectionStatus;
import android.hardware.radio.V1_2.CellInfoCdma;
import android.hardware.radio.V1_2.CellInfoGsm;
import android.hardware.radio.V1_2.CellInfoTdscdma;
import android.hardware.radio.V1_2.CellInfoWcdma;
import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CellInfo {
    public int connectionStatus;
    public Info info = new Info();
    public boolean isRegistered;

    public static final class Info {
        private byte hidl_d;
        private Object hidl_o;

        public static final class hidl_discriminator {
            public static final byte cdma = (byte) 1;
            public static final byte gsm = (byte) 0;
            public static final byte lte = (byte) 4;
            public static final byte nr = (byte) 5;
            public static final byte tdscdma = (byte) 3;
            public static final byte wcdma = (byte) 2;

            public static final String getName(byte value) {
                if (value == (byte) 0) {
                    return "gsm";
                }
                if (value == (byte) 1) {
                    return "cdma";
                }
                if (value == (byte) 2) {
                    return "wcdma";
                }
                if (value == (byte) 3) {
                    return "tdscdma";
                }
                if (value == (byte) 4) {
                    return "lte";
                }
                if (value != (byte) 5) {
                    return "Unknown";
                }
                return "nr";
            }

            private hidl_discriminator() {
            }
        }

        public Info() {
            this.hidl_d = (byte) 0;
            this.hidl_o = null;
            this.hidl_o = new CellInfoGsm();
        }

        public void gsm(CellInfoGsm gsm) {
            this.hidl_d = (byte) 0;
            this.hidl_o = gsm;
        }

        public CellInfoGsm gsm() {
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
            if (obj == null || CellInfoGsm.class.isInstance(obj)) {
                return (CellInfoGsm) this.hidl_o;
            }
            throw new Error("Union is in a corrupted state.");
        }

        public void cdma(CellInfoCdma cdma) {
            this.hidl_d = (byte) 1;
            this.hidl_o = cdma;
        }

        public CellInfoCdma cdma() {
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
            if (obj == null || CellInfoCdma.class.isInstance(obj)) {
                return (CellInfoCdma) this.hidl_o;
            }
            throw new Error("Union is in a corrupted state.");
        }

        public void wcdma(CellInfoWcdma wcdma) {
            this.hidl_d = (byte) 2;
            this.hidl_o = wcdma;
        }

        public CellInfoWcdma wcdma() {
            Object obj;
            if (this.hidl_d != (byte) 2) {
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
            if (obj == null || CellInfoWcdma.class.isInstance(obj)) {
                return (CellInfoWcdma) this.hidl_o;
            }
            throw new Error("Union is in a corrupted state.");
        }

        public void tdscdma(CellInfoTdscdma tdscdma) {
            this.hidl_d = (byte) 3;
            this.hidl_o = tdscdma;
        }

        public CellInfoTdscdma tdscdma() {
            Object obj;
            if (this.hidl_d != (byte) 3) {
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
            if (obj == null || CellInfoTdscdma.class.isInstance(obj)) {
                return (CellInfoTdscdma) this.hidl_o;
            }
            throw new Error("Union is in a corrupted state.");
        }

        public void lte(CellInfoLte lte) {
            this.hidl_d = (byte) 4;
            this.hidl_o = lte;
        }

        public CellInfoLte lte() {
            Object obj;
            if (this.hidl_d != (byte) 4) {
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
            if (obj == null || CellInfoLte.class.isInstance(obj)) {
                return (CellInfoLte) this.hidl_o;
            }
            throw new Error("Union is in a corrupted state.");
        }

        public void nr(CellInfoNr nr) {
            this.hidl_d = (byte) 5;
            this.hidl_o = nr;
        }

        public CellInfoNr nr() {
            Object obj;
            if (this.hidl_d != (byte) 5) {
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
            if (obj == null || CellInfoNr.class.isInstance(obj)) {
                return (CellInfoNr) this.hidl_o;
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
            if (otherObject == null || otherObject.getClass() != Info.class) {
                return false;
            }
            Info other = (Info) otherObject;
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
                builder.append(".gsm = ");
                builder.append(gsm());
            } else if (b == (byte) 1) {
                builder.append(".cdma = ");
                builder.append(cdma());
            } else if (b == (byte) 2) {
                builder.append(".wcdma = ");
                builder.append(wcdma());
            } else if (b == (byte) 3) {
                builder.append(".tdscdma = ");
                builder.append(tdscdma());
            } else if (b == (byte) 4) {
                builder.append(".lte = ");
                builder.append(lte());
            } else if (b == (byte) 5) {
                builder.append(".nr = ");
                builder.append(nr());
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
            readEmbeddedFromParcel(parcel, parcel.readBuffer(128), 0);
        }

        public static final ArrayList<Info> readVectorFromParcel(HwParcel parcel) {
            ArrayList<Info> _hidl_vec = new ArrayList();
            HwBlob _hidl_blob = parcel.readBuffer(16);
            int _hidl_vec_size = _hidl_blob.getInt32(8);
            HwBlob childBlob = parcel.readEmbeddedBuffer((long) (_hidl_vec_size * 128), _hidl_blob.handle(), 0, true);
            _hidl_vec.clear();
            for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                Info _hidl_vec_element = new Info();
                _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, (long) (_hidl_index_0 * 128));
                _hidl_vec.add(_hidl_vec_element);
            }
            return _hidl_vec;
        }

        public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
            this.hidl_d = _hidl_blob.getInt8(0 + _hidl_offset);
            byte b = this.hidl_d;
            if (b == (byte) 0) {
                this.hidl_o = new CellInfoGsm();
                ((CellInfoGsm) this.hidl_o).readEmbeddedFromParcel(parcel, _hidl_blob, 8 + _hidl_offset);
            } else if (b == (byte) 1) {
                this.hidl_o = new CellInfoCdma();
                ((CellInfoCdma) this.hidl_o).readEmbeddedFromParcel(parcel, _hidl_blob, 8 + _hidl_offset);
            } else if (b == (byte) 2) {
                this.hidl_o = new CellInfoWcdma();
                ((CellInfoWcdma) this.hidl_o).readEmbeddedFromParcel(parcel, _hidl_blob, 8 + _hidl_offset);
            } else if (b == (byte) 3) {
                this.hidl_o = new CellInfoTdscdma();
                ((CellInfoTdscdma) this.hidl_o).readEmbeddedFromParcel(parcel, _hidl_blob, 8 + _hidl_offset);
            } else if (b == (byte) 4) {
                this.hidl_o = new CellInfoLte();
                ((CellInfoLte) this.hidl_o).readEmbeddedFromParcel(parcel, _hidl_blob, 8 + _hidl_offset);
            } else if (b == (byte) 5) {
                this.hidl_o = new CellInfoNr();
                ((CellInfoNr) this.hidl_o).readEmbeddedFromParcel(parcel, _hidl_blob, 8 + _hidl_offset);
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown union discriminator (value: ");
                stringBuilder.append(this.hidl_d);
                stringBuilder.append(").");
                throw new IllegalStateException(stringBuilder.toString());
            }
        }

        public final void writeToParcel(HwParcel parcel) {
            HwBlob _hidl_blob = new HwBlob(128);
            writeEmbeddedToBlob(_hidl_blob, 0);
            parcel.writeBuffer(_hidl_blob);
        }

        public static final void writeVectorToParcel(HwParcel parcel, ArrayList<Info> _hidl_vec) {
            HwBlob _hidl_blob = new HwBlob(16);
            int _hidl_vec_size = _hidl_vec.size();
            _hidl_blob.putInt32(8, _hidl_vec_size);
            _hidl_blob.putBool(12, false);
            HwBlob childBlob = new HwBlob(_hidl_vec_size * 128);
            for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                ((Info) _hidl_vec.get(_hidl_index_0)).writeEmbeddedToBlob(childBlob, (long) (_hidl_index_0 * 128));
            }
            _hidl_blob.putBlob(0, childBlob);
            parcel.writeBuffer(_hidl_blob);
        }

        public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
            _hidl_blob.putInt8(0 + _hidl_offset, this.hidl_d);
            byte b = this.hidl_d;
            if (b == (byte) 0) {
                gsm().writeEmbeddedToBlob(_hidl_blob, 8 + _hidl_offset);
            } else if (b == (byte) 1) {
                cdma().writeEmbeddedToBlob(_hidl_blob, 8 + _hidl_offset);
            } else if (b == (byte) 2) {
                wcdma().writeEmbeddedToBlob(_hidl_blob, 8 + _hidl_offset);
            } else if (b == (byte) 3) {
                tdscdma().writeEmbeddedToBlob(_hidl_blob, 8 + _hidl_offset);
            } else if (b == (byte) 4) {
                lte().writeEmbeddedToBlob(_hidl_blob, 8 + _hidl_offset);
            } else if (b == (byte) 5) {
                nr().writeEmbeddedToBlob(_hidl_blob, 8 + _hidl_offset);
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
        if (otherObject == null || otherObject.getClass() != CellInfo.class) {
            return false;
        }
        CellInfo other = (CellInfo) otherObject;
        if (this.isRegistered == other.isRegistered && this.connectionStatus == other.connectionStatus && HidlSupport.deepEquals(this.info, other.info)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.isRegistered))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.connectionStatus))), Integer.valueOf(HidlSupport.deepHashCode(this.info))});
    }

    public final String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append(".isRegistered = ");
        builder.append(this.isRegistered);
        builder.append(", .connectionStatus = ");
        builder.append(CellConnectionStatus.toString(this.connectionStatus));
        builder.append(", .info = ");
        builder.append(this.info);
        builder.append("}");
        return builder.toString();
    }

    public final void readFromParcel(HwParcel parcel) {
        readEmbeddedFromParcel(parcel, parcel.readBuffer(136), 0);
    }

    public static final ArrayList<CellInfo> readVectorFromParcel(HwParcel parcel) {
        ArrayList<CellInfo> _hidl_vec = new ArrayList();
        HwBlob _hidl_blob = parcel.readBuffer(16);
        int _hidl_vec_size = _hidl_blob.getInt32(8);
        HwBlob childBlob = parcel.readEmbeddedBuffer((long) (_hidl_vec_size * 136), _hidl_blob.handle(), 0, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            CellInfo _hidl_vec_element = new CellInfo();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, (long) (_hidl_index_0 * 136));
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.isRegistered = _hidl_blob.getBool(0 + _hidl_offset);
        this.connectionStatus = _hidl_blob.getInt32(4 + _hidl_offset);
        this.info.readEmbeddedFromParcel(parcel, _hidl_blob, 8 + _hidl_offset);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(136);
        writeEmbeddedToBlob(_hidl_blob, 0);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<CellInfo> _hidl_vec) {
        HwBlob _hidl_blob = new HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8, _hidl_vec_size);
        _hidl_blob.putBool(12, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 136);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            ((CellInfo) _hidl_vec.get(_hidl_index_0)).writeEmbeddedToBlob(childBlob, (long) (_hidl_index_0 * 136));
        }
        _hidl_blob.putBlob(0, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putBool(0 + _hidl_offset, this.isRegistered);
        _hidl_blob.putInt32(4 + _hidl_offset, this.connectionStatus);
        this.info.writeEmbeddedToBlob(_hidl_blob, 8 + _hidl_offset);
    }
}
