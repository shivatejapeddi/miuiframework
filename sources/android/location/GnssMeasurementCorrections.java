package android.location;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SystemApi
public final class GnssMeasurementCorrections implements Parcelable {
    public static final Creator<GnssMeasurementCorrections> CREATOR = new Creator<GnssMeasurementCorrections>() {
        public GnssMeasurementCorrections createFromParcel(Parcel parcel) {
            Builder gnssMeasurementCorrectons = new Builder().setLatitudeDegrees(parcel.readDouble()).setLongitudeDegrees(parcel.readDouble()).setAltitudeMeters(parcel.readDouble()).setHorizontalPositionUncertaintyMeters(parcel.readDouble()).setVerticalPositionUncertaintyMeters(parcel.readDouble()).setToaGpsNanosecondsOfWeek(parcel.readLong());
            List<GnssSingleSatCorrection> singleSatCorrectionList = new ArrayList();
            parcel.readTypedList(singleSatCorrectionList, GnssSingleSatCorrection.CREATOR);
            gnssMeasurementCorrectons.setSingleSatelliteCorrectionList(singleSatCorrectionList);
            return gnssMeasurementCorrectons.build();
        }

        public GnssMeasurementCorrections[] newArray(int i) {
            return new GnssMeasurementCorrections[i];
        }
    };
    private final double mAltitudeMeters;
    private final double mHorizontalPositionUncertaintyMeters;
    private final double mLatitudeDegrees;
    private final double mLongitudeDegrees;
    private final List<GnssSingleSatCorrection> mSingleSatCorrectionList;
    private final long mToaGpsNanosecondsOfWeek;
    private final double mVerticalPositionUncertaintyMeters;

    public static final class Builder {
        private double mAltitudeMeters;
        private double mHorizontalPositionUncertaintyMeters;
        private double mLatitudeDegrees;
        private double mLongitudeDegrees;
        private List<GnssSingleSatCorrection> mSingleSatCorrectionList;
        private long mToaGpsNanosecondsOfWeek;
        private double mVerticalPositionUncertaintyMeters;

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

        public Builder setHorizontalPositionUncertaintyMeters(double horizontalPositionUncertaintyMeters) {
            this.mHorizontalPositionUncertaintyMeters = horizontalPositionUncertaintyMeters;
            return this;
        }

        public Builder setVerticalPositionUncertaintyMeters(double verticalPositionUncertaintyMeters) {
            this.mVerticalPositionUncertaintyMeters = verticalPositionUncertaintyMeters;
            return this;
        }

        public Builder setToaGpsNanosecondsOfWeek(long toaGpsNanosecondsOfWeek) {
            this.mToaGpsNanosecondsOfWeek = toaGpsNanosecondsOfWeek;
            return this;
        }

        public Builder setSingleSatelliteCorrectionList(List<GnssSingleSatCorrection> singleSatCorrectionList) {
            this.mSingleSatCorrectionList = singleSatCorrectionList;
            return this;
        }

        public GnssMeasurementCorrections build() {
            return new GnssMeasurementCorrections(this, null);
        }
    }

    /* synthetic */ GnssMeasurementCorrections(Builder x0, AnonymousClass1 x1) {
        this(x0);
    }

    private GnssMeasurementCorrections(Builder builder) {
        this.mLatitudeDegrees = builder.mLatitudeDegrees;
        this.mLongitudeDegrees = builder.mLongitudeDegrees;
        this.mAltitudeMeters = builder.mAltitudeMeters;
        this.mHorizontalPositionUncertaintyMeters = builder.mHorizontalPositionUncertaintyMeters;
        this.mVerticalPositionUncertaintyMeters = builder.mVerticalPositionUncertaintyMeters;
        this.mToaGpsNanosecondsOfWeek = builder.mToaGpsNanosecondsOfWeek;
        List<GnssSingleSatCorrection> singleSatCorrList = builder.mSingleSatCorrectionList;
        boolean z = (singleSatCorrList == null || singleSatCorrList.isEmpty()) ? false : true;
        Preconditions.checkArgument(z);
        this.mSingleSatCorrectionList = Collections.unmodifiableList(new ArrayList(singleSatCorrList));
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

    public double getHorizontalPositionUncertaintyMeters() {
        return this.mHorizontalPositionUncertaintyMeters;
    }

    public double getVerticalPositionUncertaintyMeters() {
        return this.mVerticalPositionUncertaintyMeters;
    }

    public long getToaGpsNanosecondsOfWeek() {
        return this.mToaGpsNanosecondsOfWeek;
    }

    public List<GnssSingleSatCorrection> getSingleSatelliteCorrectionList() {
        return this.mSingleSatCorrectionList;
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        String format = "   %-29s = %s\n";
        StringBuilder builder = new StringBuilder("GnssMeasurementCorrections:\n");
        String str = "   %-29s = %s\n";
        builder.append(String.format(str, new Object[]{"LatitudeDegrees = ", Double.valueOf(this.mLatitudeDegrees)}));
        builder.append(String.format(str, new Object[]{"LongitudeDegrees = ", Double.valueOf(this.mLongitudeDegrees)}));
        builder.append(String.format(str, new Object[]{"AltitudeMeters = ", Double.valueOf(this.mAltitudeMeters)}));
        builder.append(String.format(str, new Object[]{"HorizontalPositionUncertaintyMeters = ", Double.valueOf(this.mHorizontalPositionUncertaintyMeters)}));
        builder.append(String.format(str, new Object[]{"VerticalPositionUncertaintyMeters = ", Double.valueOf(this.mVerticalPositionUncertaintyMeters)}));
        builder.append(String.format(str, new Object[]{"ToaGpsNanosecondsOfWeek = ", Long.valueOf(this.mToaGpsNanosecondsOfWeek)}));
        builder.append(String.format(str, new Object[]{"mSingleSatCorrectionList = ", this.mSingleSatCorrectionList}));
        return builder.toString();
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeDouble(this.mLatitudeDegrees);
        parcel.writeDouble(this.mLongitudeDegrees);
        parcel.writeDouble(this.mAltitudeMeters);
        parcel.writeDouble(this.mHorizontalPositionUncertaintyMeters);
        parcel.writeDouble(this.mVerticalPositionUncertaintyMeters);
        parcel.writeLong(this.mToaGpsNanosecondsOfWeek);
        parcel.writeTypedList(this.mSingleSatCorrectionList);
    }
}
