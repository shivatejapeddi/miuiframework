package android.location;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public final class GnssReflectingPlane implements Parcelable {
    public static final Creator<GnssReflectingPlane> CREATOR = new Creator<GnssReflectingPlane>() {
        public GnssReflectingPlane createFromParcel(Parcel parcel) {
            return new Builder().setLatitudeDegrees(parcel.readDouble()).setLongitudeDegrees(parcel.readDouble()).setAltitudeMeters(parcel.readDouble()).setAzimuthDegrees(parcel.readDouble()).build();
        }

        public GnssReflectingPlane[] newArray(int i) {
            return new GnssReflectingPlane[i];
        }
    };
    private final double mAltitudeMeters;
    private final double mAzimuthDegrees;
    private final double mLatitudeDegrees;
    private final double mLongitudeDegrees;

    public static final class Builder {
        private double mAltitudeMeters;
        private double mAzimuthDegrees;
        private double mLatitudeDegrees;
        private double mLongitudeDegrees;

        public Builder setLatitudeDegrees(double latitudeDegrees) {
            this.mLatitudeDegrees = latitudeDegrees;
            return this;
        }

        public Builder setLongitudeDegrees(double longitudeDegrees) {
            this.mLongitudeDegrees = longitudeDegrees;
            return this;
        }

        public Builder setAltitudeMeters(double altitudeMeters) {
            this.mAltitudeMeters = altitudeMeters;
            return this;
        }

        public Builder setAzimuthDegrees(double azimuthDegrees) {
            this.mAzimuthDegrees = azimuthDegrees;
            return this;
        }

        public GnssReflectingPlane build() {
            return new GnssReflectingPlane(this, null);
        }
    }

    /* synthetic */ GnssReflectingPlane(Builder x0, AnonymousClass1 x1) {
        this(x0);
    }

    private GnssReflectingPlane(Builder builder) {
        this.mLatitudeDegrees = builder.mLatitudeDegrees;
        this.mLongitudeDegrees = builder.mLongitudeDegrees;
        this.mAltitudeMeters = builder.mAltitudeMeters;
        this.mAzimuthDegrees = builder.mAzimuthDegrees;
    }

    public double getLatitudeDegrees() {
        return this.mLatitudeDegrees;
    }

    public double getLongitudeDegrees() {
        return this.mLongitudeDegrees;
    }

    public double getAltitudeMeters() {
        return this.mAltitudeMeters;
    }

    public double getAzimuthDegrees() {
        return this.mAzimuthDegrees;
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        String format = "   %-29s = %s\n";
        StringBuilder builder = new StringBuilder("ReflectingPlane:\n");
        String str = "   %-29s = %s\n";
        builder.append(String.format(str, new Object[]{"LatitudeDegrees = ", Double.valueOf(this.mLatitudeDegrees)}));
        builder.append(String.format(str, new Object[]{"LongitudeDegrees = ", Double.valueOf(this.mLongitudeDegrees)}));
        builder.append(String.format(str, new Object[]{"AltitudeMeters = ", Double.valueOf(this.mAltitudeMeters)}));
        builder.append(String.format(str, new Object[]{"AzimuthDegrees = ", Double.valueOf(this.mAzimuthDegrees)}));
        return builder.toString();
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeDouble(this.mLatitudeDegrees);
        parcel.writeDouble(this.mLongitudeDegrees);
        parcel.writeDouble(this.mAltitudeMeters);
        parcel.writeDouble(this.mAzimuthDegrees);
    }
}
