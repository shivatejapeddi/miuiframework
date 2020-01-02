package android.view;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public abstract class DisplayAddress implements Parcelable {

    public static final class Network extends DisplayAddress {
        public static final Creator<Network> CREATOR = new Creator<Network>() {
            public Network createFromParcel(Parcel in) {
                return new Network(in.readString());
            }

            public Network[] newArray(int size) {
                return new Network[size];
            }
        };
        private final String mMacAddress;

        public boolean equals(Object other) {
            return (other instanceof Network) && this.mMacAddress.equals(((Network) other).mMacAddress);
        }

        public String toString() {
            return this.mMacAddress;
        }

        public int hashCode() {
            return this.mMacAddress.hashCode();
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(this.mMacAddress);
        }

        private Network(String macAddress) {
            this.mMacAddress = macAddress;
        }
    }

    public static final class Physical extends DisplayAddress {
        public static final Creator<Physical> CREATOR = new Creator<Physical>() {
            public Physical createFromParcel(Parcel in) {
                return new Physical(in.readLong());
            }

            public Physical[] newArray(int size) {
                return new Physical[size];
            }
        };
        private static final int MODEL_SHIFT = 8;
        private static final int PORT_MASK = 255;
        private static final long UNKNOWN_MODEL = 0;
        private final long mPhysicalDisplayId;

        public byte getPort() {
            return (byte) ((int) this.mPhysicalDisplayId);
        }

        public Long getModel() {
            long model = this.mPhysicalDisplayId >>> 8;
            return model == 0 ? null : Long.valueOf(model);
        }

        public boolean equals(Object other) {
            return (other instanceof Physical) && this.mPhysicalDisplayId == ((Physical) other).mPhysicalDisplayId;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder("{");
            builder.append("port=");
            builder = builder.append(getPort() & 255);
            Long model = getModel();
            if (model != null) {
                builder.append(", model=0x");
                builder.append(Long.toHexString(model.longValue()));
            }
            builder.append("}");
            return builder.toString();
        }

        public int hashCode() {
            return Long.hashCode(this.mPhysicalDisplayId);
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeLong(this.mPhysicalDisplayId);
        }

        private Physical(long physicalDisplayId) {
            this.mPhysicalDisplayId = physicalDisplayId;
        }
    }

    public static Physical fromPhysicalDisplayId(long physicalDisplayId) {
        return new Physical(physicalDisplayId);
    }

    public static Network fromMacAddress(String macAddress) {
        return new Network(macAddress);
    }

    public int describeContents() {
        return 0;
    }
}
