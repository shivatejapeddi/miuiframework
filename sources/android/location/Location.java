package android.location;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.util.Printer;
import android.util.TimeUtils;
import java.text.DecimalFormat;
import java.util.StringTokenizer;

public class Location implements Parcelable {
    public static final Creator<Location> CREATOR = new Creator<Location>() {
        public Location createFromParcel(Parcel in) {
            Location l = new Location(in.readString());
            l.mTime = in.readLong();
            l.mElapsedRealtimeNanos = in.readLong();
            l.mElapsedRealtimeUncertaintyNanos = in.readDouble();
            l.mFieldsMask = in.readInt();
            l.mLatitude = in.readDouble();
            l.mLongitude = in.readDouble();
            l.mAltitude = in.readDouble();
            l.mSpeed = in.readFloat();
            l.mBearing = in.readFloat();
            l.mHorizontalAccuracyMeters = in.readFloat();
            l.mVerticalAccuracyMeters = in.readFloat();
            l.mSpeedAccuracyMetersPerSecond = in.readFloat();
            l.mBearingAccuracyDegrees = in.readFloat();
            l.mExtras = Bundle.setDefusable(in.readBundle(), true);
            return l;
        }

        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
    public static final String EXTRA_COARSE_LOCATION = "coarseLocation";
    public static final String EXTRA_NO_GPS_LOCATION = "noGPSLocation";
    public static final int FORMAT_DEGREES = 0;
    public static final int FORMAT_MINUTES = 1;
    public static final int FORMAT_SECONDS = 2;
    private static final int HAS_ALTITUDE_MASK = 1;
    private static final int HAS_BEARING_ACCURACY_MASK = 128;
    private static final int HAS_BEARING_MASK = 4;
    private static final int HAS_ELAPSED_REALTIME_UNCERTAINTY_MASK = 256;
    private static final int HAS_HORIZONTAL_ACCURACY_MASK = 8;
    private static final int HAS_MOCK_PROVIDER_MASK = 16;
    private static final int HAS_SPEED_ACCURACY_MASK = 64;
    private static final int HAS_SPEED_MASK = 2;
    private static final int HAS_VERTICAL_ACCURACY_MASK = 32;
    private static ThreadLocal<BearingDistanceCache> sBearingDistanceCache = new ThreadLocal<BearingDistanceCache>() {
        /* Access modifiers changed, original: protected */
        public BearingDistanceCache initialValue() {
            return new BearingDistanceCache();
        }
    };
    private double mAltitude = 0.0d;
    private float mBearing = 0.0f;
    private float mBearingAccuracyDegrees = 0.0f;
    @UnsupportedAppUsage
    private long mElapsedRealtimeNanos = 0;
    private double mElapsedRealtimeUncertaintyNanos = 0.0d;
    private Bundle mExtras = null;
    private int mFieldsMask = 0;
    private float mHorizontalAccuracyMeters = 0.0f;
    private double mLatitude = 0.0d;
    private double mLongitude = 0.0d;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private String mProvider;
    private float mSpeed = 0.0f;
    private float mSpeedAccuracyMetersPerSecond = 0.0f;
    private long mTime = 0;
    private float mVerticalAccuracyMeters = 0.0f;

    private static class BearingDistanceCache {
        private float mDistance;
        private float mFinalBearing;
        private float mInitialBearing;
        private double mLat1;
        private double mLat2;
        private double mLon1;
        private double mLon2;

        private BearingDistanceCache() {
            this.mLat1 = 0.0d;
            this.mLon1 = 0.0d;
            this.mLat2 = 0.0d;
            this.mLon2 = 0.0d;
            this.mDistance = 0.0f;
            this.mInitialBearing = 0.0f;
            this.mFinalBearing = 0.0f;
        }

        /* synthetic */ BearingDistanceCache(AnonymousClass1 x0) {
            this();
        }
    }

    public Location(String provider) {
        this.mProvider = provider;
    }

    public Location(Location l) {
        set(l);
    }

    public void set(Location l) {
        this.mProvider = l.mProvider;
        this.mTime = l.mTime;
        this.mElapsedRealtimeNanos = l.mElapsedRealtimeNanos;
        this.mElapsedRealtimeUncertaintyNanos = l.mElapsedRealtimeUncertaintyNanos;
        this.mFieldsMask = l.mFieldsMask;
        this.mLatitude = l.mLatitude;
        this.mLongitude = l.mLongitude;
        this.mAltitude = l.mAltitude;
        this.mSpeed = l.mSpeed;
        this.mBearing = l.mBearing;
        this.mHorizontalAccuracyMeters = l.mHorizontalAccuracyMeters;
        this.mVerticalAccuracyMeters = l.mVerticalAccuracyMeters;
        this.mSpeedAccuracyMetersPerSecond = l.mSpeedAccuracyMetersPerSecond;
        this.mBearingAccuracyDegrees = l.mBearingAccuracyDegrees;
        Bundle bundle = l.mExtras;
        this.mExtras = bundle == null ? null : new Bundle(bundle);
    }

    public void reset() {
        this.mProvider = null;
        this.mTime = 0;
        this.mElapsedRealtimeNanos = 0;
        this.mElapsedRealtimeUncertaintyNanos = 0.0d;
        this.mFieldsMask = 0;
        this.mLatitude = 0.0d;
        this.mLongitude = 0.0d;
        this.mAltitude = 0.0d;
        this.mSpeed = 0.0f;
        this.mBearing = 0.0f;
        this.mHorizontalAccuracyMeters = 0.0f;
        this.mVerticalAccuracyMeters = 0.0f;
        this.mSpeedAccuracyMetersPerSecond = 0.0f;
        this.mBearingAccuracyDegrees = 0.0f;
        this.mExtras = null;
    }

    public static String convert(double coordinate, int outputType) {
        StringBuilder stringBuilder;
        if (coordinate < -180.0d || coordinate > 180.0d || Double.isNaN(coordinate)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("coordinate=");
            stringBuilder.append(coordinate);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (outputType == 0 || outputType == 1 || outputType == 2) {
            StringBuilder sb = new StringBuilder();
            if (coordinate < 0.0d) {
                sb.append('-');
                coordinate = -coordinate;
            }
            DecimalFormat df = new DecimalFormat("###.#####");
            if (outputType == 1 || outputType == 2) {
                int degrees = (int) Math.floor(coordinate);
                sb.append(degrees);
                sb.append(':');
                coordinate = (coordinate - ((double) degrees)) * 60.0d;
                if (outputType == 2) {
                    int minutes = (int) Math.floor(coordinate);
                    sb.append(minutes);
                    sb.append(':');
                    coordinate = (coordinate - ((double) minutes)) * 60.0d;
                }
            }
            sb.append(df.format(coordinate));
            return sb.toString();
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("outputType=");
            stringBuilder.append(outputType);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public static double convert(String coordinate) {
        boolean z;
        StringTokenizer stringTokenizer;
        StringBuilder stringBuilder;
        String str = coordinate;
        if (str != null) {
            boolean negative;
            String coordinate2;
            if (str.charAt(0) == '-') {
                negative = true;
                coordinate2 = str.substring(1);
            } else {
                negative = false;
                coordinate2 = str;
            }
            StringTokenizer st = new StringTokenizer(coordinate2, ":");
            int tokens = st.countTokens();
            String val = "coordinate=";
            if (tokens >= 1) {
                try {
                    str = st.nextToken();
                    if (tokens == 1) {
                        try {
                            val = Double.parseDouble(str);
                            return negative ? -val : val;
                        } catch (NumberFormatException e) {
                            z = negative;
                            stringTokenizer = st;
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(val);
                            stringBuilder.append(coordinate2);
                            throw new IllegalArgumentException(stringBuilder.toString());
                        }
                    }
                    double min;
                    String minutes = st.nextToken();
                    int deg = Integer.parseInt(str);
                    double sec = 0.0d;
                    boolean secPresent = false;
                    if (st.hasMoreTokens()) {
                        min = (double) Integer.parseInt(minutes);
                        sec = Double.parseDouble(st.nextToken());
                        secPresent = true;
                    } else {
                        min = Double.parseDouble(minutes);
                    }
                    boolean z2 = negative && deg == 180 && min == 0.0d && sec == 0.0d;
                    boolean isNegative180 = z2;
                    boolean z3;
                    if (((double) deg) < 0.0d) {
                        z = negative;
                    } else if (deg <= 179 || isNegative180) {
                        if (min < 0.0d || min >= 60.0d) {
                            z = negative;
                        } else if (secPresent && min > 59.0d) {
                            z3 = isNegative180;
                            z = negative;
                        } else if (sec < 0.0d || sec >= 60.0d) {
                            z = negative;
                            try {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(val);
                                stringBuilder.append(coordinate2);
                                throw new IllegalArgumentException(stringBuilder.toString());
                            } catch (NumberFormatException e2) {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(val);
                                stringBuilder.append(coordinate2);
                                throw new IllegalArgumentException(stringBuilder.toString());
                            }
                        } else {
                            double d;
                            st = (((((double) deg) * 3600.0d) + (60.0d * min)) + sec) / 4660134898793709568;
                            if (negative) {
                                d = -st;
                            } else {
                                z = negative;
                                d = st;
                            }
                            return d;
                        }
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(val);
                        stringBuilder.append(coordinate2);
                        throw new IllegalArgumentException(stringBuilder.toString());
                    } else {
                        z3 = isNegative180;
                        z = negative;
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(val);
                    stringBuilder.append(coordinate2);
                    throw new IllegalArgumentException(stringBuilder.toString());
                } catch (NumberFormatException e3) {
                    z = negative;
                    stringTokenizer = st;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(val);
                    stringBuilder.append(coordinate2);
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(val);
            stringBuilder2.append(coordinate2);
            throw new IllegalArgumentException(stringBuilder2.toString());
        }
        throw new NullPointerException("coordinate");
    }

    private static void computeDistanceAndBearing(double lat1, double lon1, double lat2, double lon2, BearingDistanceCache results) {
        double lat22;
        BearingDistanceCache bearingDistanceCache = results;
        double lat12 = lat1 * 0.017453292519943295d;
        double lat23 = lat2 * 0.017453292519943295d;
        double lon12 = lon1 * 0.017453292519943295d;
        double lon22 = 0.017453292519943295d * lon2;
        double f = (6378137.0d - 6356752.3142d) / 6378137.0d;
        double aSqMinusBSqOverBSq = ((6378137.0d * 6378137.0d) - (6356752.3142d * 6356752.3142d)) / (6356752.3142d * 6356752.3142d);
        double L = lon22 - lon12;
        double A = 0.0d;
        double U1 = Math.atan((1.0d - f) * Math.tan(lat12));
        double U2 = Math.atan((1.0d - f) * Math.tan(lat23));
        double cosU1 = Math.cos(U1);
        double cosU2 = Math.cos(U2);
        lat1 = 6378137.0d;
        double sinU1 = Math.sin(U1);
        double sinU2 = Math.sin(U2);
        double cosU1cosU2 = cosU1 * cosU2;
        double sinU1sinU2 = sinU1 * sinU2;
        double sigma = 0.0d;
        double deltaSigma = 0.0d;
        double sinSigma = 0.0d;
        double cosLambda = 0.0d;
        double sinLambda = 0.0d;
        double lambda = L;
        lon1 = lon22;
        int iter = 0;
        while (iter < 20) {
            double lambdaOrig = lambda;
            cosLambda = Math.cos(lambda);
            sinLambda = Math.sin(lambda);
            double t1 = cosU2 * sinLambda;
            double t2 = (cosU1 * sinU2) - ((sinU1 * cosU2) * cosLambda);
            lat2 = lon12;
            lon12 = Math.sqrt((t1 * t1) + (t2 * t2));
            lat22 = lat23;
            lat23 = sinU1sinU2 + (cosU1cosU2 * cosLambda);
            sigma = Math.atan2(lon12, lat23);
            double d = 0.0d;
            if (lon12 == 0.0d) {
                sinSigma = 0.0d;
            } else {
                sinSigma = (cosU1cosU2 * sinLambda) / lon12;
            }
            double cosSqAlpha = 1.0d - (sinSigma * sinSigma);
            if (cosSqAlpha != 0.0d) {
                d = lat23 - ((sinU1sinU2 * 2.0d) / cosSqAlpha);
            }
            double cos2SM = d;
            d = cosSqAlpha * aSqMinusBSqOverBSq;
            A = ((d / 16384.0d) * (((((320.0d - (175.0d * d)) * d) - 0.005859375d) * d) + 4096.0d)) + 1.0d;
            double B = (d / 1024.0d) * (((((74.0d - (47.0d * d)) * d) - 0.03125d) * d) + 256.0d);
            double C = ((f / 16.0d) * cosSqAlpha) * (((4.0d - (3.0d * cosSqAlpha)) * f) + 4.0d);
            double cos2SMSq = cos2SM * cos2SM;
            deltaSigma = (B * lon12) * (cos2SM + ((B / 4.0d) * ((((cos2SMSq * 2.0d) - 4.0d) * lat23) - ((((B / 6.0d) * cos2SM) * (((lon12 * 4.0d) * lon12) - 1.5d)) * ((4.0d * cos2SMSq) - 1.5d)))));
            lambda = L + ((((1.0d - C) * f) * sinSigma) * (sigma + ((C * lon12) * (cos2SM + ((C * lat23) * (((2.0d * cos2SM) * cos2SM) - 4.0d))))));
            if (Math.abs((lambda - lambdaOrig) / lambda) < 1.0E-12d) {
                d = lat23;
                break;
            }
            iter++;
            d = lat23;
            sinSigma = lon12;
            lat23 = lat22;
            lon12 = lat2;
        }
        lat22 = lat23;
        lat2 = lon12;
        float distance = (float) ((6356752.3142d * A) * (sigma - deltaSigma));
        bearingDistanceCache.mDistance = distance;
        int MAXITERS = 20;
        float initialBearing = (float) (((double) ((float) Math.atan2(cosU2 * sinLambda, (cosU1 * sinU2) - ((sinU1 * cosU2) * cosLambda)))) * 57.29577951308232d);
        bearingDistanceCache.mInitialBearing = initialBearing;
        initialBearing = (float) (((double) ((float) Math.atan2(cosU1 * sinLambda, ((-sinU1) * cosU2) + ((cosU1 * sinU2) * cosLambda)))) * 57.29577951308232d);
        bearingDistanceCache.mFinalBearing = initialBearing;
        bearingDistanceCache.mLat1 = lat12;
        bearingDistanceCache.mLat2 = lat22;
        bearingDistanceCache.mLon1 = lat2;
        bearingDistanceCache.mLon2 = lon1;
    }

    public static void distanceBetween(double startLatitude, double startLongitude, double endLatitude, double endLongitude, float[] results) {
        float[] fArr = results;
        if (fArr == null || fArr.length < 1) {
            throw new IllegalArgumentException("results is null or has length < 1");
        }
        BearingDistanceCache cache = (BearingDistanceCache) sBearingDistanceCache.get();
        computeDistanceAndBearing(startLatitude, startLongitude, endLatitude, endLongitude, cache);
        fArr[0] = cache.mDistance;
        if (fArr.length > 1) {
            fArr[1] = cache.mInitialBearing;
            if (fArr.length > 2) {
                fArr[2] = cache.mFinalBearing;
            }
        }
    }

    public float distanceTo(Location dest) {
        BearingDistanceCache cache = (BearingDistanceCache) sBearingDistanceCache.get();
        if (!(this.mLatitude == cache.mLat1 && this.mLongitude == cache.mLon1 && dest.mLatitude == cache.mLat2 && dest.mLongitude == cache.mLon2)) {
            computeDistanceAndBearing(this.mLatitude, this.mLongitude, dest.mLatitude, dest.mLongitude, cache);
        }
        return cache.mDistance;
    }

    public float bearingTo(Location dest) {
        BearingDistanceCache cache = (BearingDistanceCache) sBearingDistanceCache.get();
        if (!(this.mLatitude == cache.mLat1 && this.mLongitude == cache.mLon1 && dest.mLatitude == cache.mLat2 && dest.mLongitude == cache.mLon2)) {
            computeDistanceAndBearing(this.mLatitude, this.mLongitude, dest.mLatitude, dest.mLongitude, cache);
        }
        return cache.mInitialBearing;
    }

    public String getProvider() {
        return this.mProvider;
    }

    public void setProvider(String provider) {
        this.mProvider = provider;
    }

    public long getTime() {
        return this.mTime;
    }

    public void setTime(long time) {
        this.mTime = time;
    }

    public long getElapsedRealtimeNanos() {
        return this.mElapsedRealtimeNanos;
    }

    public void setElapsedRealtimeNanos(long time) {
        this.mElapsedRealtimeNanos = time;
    }

    public double getElapsedRealtimeUncertaintyNanos() {
        return this.mElapsedRealtimeUncertaintyNanos;
    }

    public void setElapsedRealtimeUncertaintyNanos(double time) {
        this.mElapsedRealtimeUncertaintyNanos = time;
        this.mFieldsMask |= 256;
    }

    public boolean hasElapsedRealtimeUncertaintyNanos() {
        return (this.mFieldsMask & 256) != 0;
    }

    public double getLatitude() {
        return this.mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public double getLongitude() {
        return this.mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }

    public boolean hasAltitude() {
        return (this.mFieldsMask & 1) != 0;
    }

    public double getAltitude() {
        return this.mAltitude;
    }

    public void setAltitude(double altitude) {
        this.mAltitude = altitude;
        this.mFieldsMask |= 1;
    }

    @Deprecated
    public void removeAltitude() {
        this.mAltitude = 0.0d;
        this.mFieldsMask &= -2;
    }

    public boolean hasSpeed() {
        return (this.mFieldsMask & 2) != 0;
    }

    public float getSpeed() {
        return this.mSpeed;
    }

    public void setSpeed(float speed) {
        this.mSpeed = speed;
        this.mFieldsMask |= 2;
    }

    @Deprecated
    public void removeSpeed() {
        this.mSpeed = 0.0f;
        this.mFieldsMask &= -3;
    }

    public boolean hasBearing() {
        return (this.mFieldsMask & 4) != 0;
    }

    public float getBearing() {
        return this.mBearing;
    }

    public void setBearing(float bearing) {
        while (bearing < 0.0f) {
            bearing += 360.0f;
        }
        while (bearing >= 360.0f) {
            bearing -= 360.0f;
        }
        this.mBearing = bearing;
        this.mFieldsMask |= 4;
    }

    @Deprecated
    public void removeBearing() {
        this.mBearing = 0.0f;
        this.mFieldsMask &= -5;
    }

    public boolean hasAccuracy() {
        return (this.mFieldsMask & 8) != 0;
    }

    public float getAccuracy() {
        return this.mHorizontalAccuracyMeters;
    }

    public void setAccuracy(float horizontalAccuracy) {
        this.mHorizontalAccuracyMeters = horizontalAccuracy;
        this.mFieldsMask |= 8;
    }

    @Deprecated
    public void removeAccuracy() {
        this.mHorizontalAccuracyMeters = 0.0f;
        this.mFieldsMask &= -9;
    }

    public boolean hasVerticalAccuracy() {
        return (this.mFieldsMask & 32) != 0;
    }

    public float getVerticalAccuracyMeters() {
        return this.mVerticalAccuracyMeters;
    }

    public void setVerticalAccuracyMeters(float verticalAccuracyMeters) {
        this.mVerticalAccuracyMeters = verticalAccuracyMeters;
        this.mFieldsMask |= 32;
    }

    @Deprecated
    public void removeVerticalAccuracy() {
        this.mVerticalAccuracyMeters = 0.0f;
        this.mFieldsMask &= -33;
    }

    public boolean hasSpeedAccuracy() {
        return (this.mFieldsMask & 64) != 0;
    }

    public float getSpeedAccuracyMetersPerSecond() {
        return this.mSpeedAccuracyMetersPerSecond;
    }

    public void setSpeedAccuracyMetersPerSecond(float speedAccuracyMeterPerSecond) {
        this.mSpeedAccuracyMetersPerSecond = speedAccuracyMeterPerSecond;
        this.mFieldsMask |= 64;
    }

    @Deprecated
    public void removeSpeedAccuracy() {
        this.mSpeedAccuracyMetersPerSecond = 0.0f;
        this.mFieldsMask &= -65;
    }

    public boolean hasBearingAccuracy() {
        return (this.mFieldsMask & 128) != 0;
    }

    public float getBearingAccuracyDegrees() {
        return this.mBearingAccuracyDegrees;
    }

    public void setBearingAccuracyDegrees(float bearingAccuracyDegrees) {
        this.mBearingAccuracyDegrees = bearingAccuracyDegrees;
        this.mFieldsMask |= 128;
    }

    @Deprecated
    public void removeBearingAccuracy() {
        this.mBearingAccuracyDegrees = 0.0f;
        this.mFieldsMask &= -129;
    }

    @SystemApi
    public boolean isComplete() {
        if (this.mProvider == null || !hasAccuracy() || this.mTime == 0 || this.mElapsedRealtimeNanos == 0) {
            return false;
        }
        return true;
    }

    @SystemApi
    public void makeComplete() {
        if (this.mProvider == null) {
            this.mProvider = "?";
        }
        if (!hasAccuracy()) {
            this.mFieldsMask |= 8;
            this.mHorizontalAccuracyMeters = 100.0f;
        }
        if (this.mTime == 0) {
            this.mTime = System.currentTimeMillis();
        }
        if (this.mElapsedRealtimeNanos == 0) {
            this.mElapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
        }
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public void setExtras(Bundle extras) {
        this.mExtras = extras == null ? null : new Bundle(extras);
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Location[");
        s.append(this.mProvider);
        s.append(String.format(" %.6f,%.6f", new Object[]{Double.valueOf(this.mLatitude), Double.valueOf(this.mLongitude)}));
        if (hasAccuracy()) {
            s.append(String.format(" hAcc=%.0f", new Object[]{Float.valueOf(this.mHorizontalAccuracyMeters)}));
        } else {
            s.append(" hAcc=???");
        }
        if (this.mTime == 0) {
            s.append(" t=?!?");
        }
        if (this.mElapsedRealtimeNanos == 0) {
            s.append(" et=?!?");
        } else {
            s.append(" et=");
            TimeUtils.formatDuration(this.mElapsedRealtimeNanos / TimeUtils.NANOS_PER_MS, s);
        }
        if (hasElapsedRealtimeUncertaintyNanos()) {
            s.append(" etAcc=");
            TimeUtils.formatDuration((long) (this.mElapsedRealtimeUncertaintyNanos / 1000000.0d), s);
        }
        if (hasAltitude()) {
            s.append(" alt=");
            s.append(this.mAltitude);
        }
        if (hasSpeed()) {
            s.append(" vel=");
            s.append(this.mSpeed);
        }
        if (hasBearing()) {
            s.append(" bear=");
            s.append(this.mBearing);
        }
        if (hasVerticalAccuracy()) {
            s.append(String.format(" vAcc=%.0f", new Object[]{Float.valueOf(this.mVerticalAccuracyMeters)}));
        } else {
            s.append(" vAcc=???");
        }
        if (hasSpeedAccuracy()) {
            s.append(String.format(" sAcc=%.0f", new Object[]{Float.valueOf(this.mSpeedAccuracyMetersPerSecond)}));
        } else {
            s.append(" sAcc=???");
        }
        if (hasBearingAccuracy()) {
            s.append(String.format(" bAcc=%.0f", new Object[]{Float.valueOf(this.mBearingAccuracyDegrees)}));
        } else {
            s.append(" bAcc=???");
        }
        if (isFromMockProvider()) {
            s.append(" mock");
        }
        if (this.mExtras != null) {
            s.append(" {");
            s.append(this.mExtras);
            s.append('}');
        }
        s.append(']');
        return s.toString();
    }

    public void dump(Printer pw, String prefix) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append(toString());
        pw.println(stringBuilder.toString());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.mProvider);
        parcel.writeLong(this.mTime);
        parcel.writeLong(this.mElapsedRealtimeNanos);
        parcel.writeDouble(this.mElapsedRealtimeUncertaintyNanos);
        parcel.writeInt(this.mFieldsMask);
        parcel.writeDouble(this.mLatitude);
        parcel.writeDouble(this.mLongitude);
        parcel.writeDouble(this.mAltitude);
        parcel.writeFloat(this.mSpeed);
        parcel.writeFloat(this.mBearing);
        parcel.writeFloat(this.mHorizontalAccuracyMeters);
        parcel.writeFloat(this.mVerticalAccuracyMeters);
        parcel.writeFloat(this.mSpeedAccuracyMetersPerSecond);
        parcel.writeFloat(this.mBearingAccuracyDegrees);
        parcel.writeBundle(this.mExtras);
    }

    public Location getExtraLocation(String key) {
        Parcelable value = this.mExtras;
        if (value != null) {
            value = value.getParcelable(key);
            if (value instanceof Location) {
                return (Location) value;
            }
        }
        return null;
    }

    @UnsupportedAppUsage
    public void setExtraLocation(String key, Location value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putParcelable(key, value);
    }

    public boolean isFromMockProvider() {
        return (this.mFieldsMask & 16) != 0;
    }

    @SystemApi
    public void setIsFromMockProvider(boolean isFromMockProvider) {
        if (isFromMockProvider) {
            this.mFieldsMask |= 16;
        } else {
            this.mFieldsMask &= -17;
        }
    }
}
