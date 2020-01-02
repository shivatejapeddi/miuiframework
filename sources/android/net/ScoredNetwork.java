package android.net;

import android.annotation.SystemApi;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

@SystemApi
public class ScoredNetwork implements Parcelable {
    public static final String ATTRIBUTES_KEY_BADGING_CURVE = "android.net.attributes.key.BADGING_CURVE";
    public static final String ATTRIBUTES_KEY_HAS_CAPTIVE_PORTAL = "android.net.attributes.key.HAS_CAPTIVE_PORTAL";
    public static final String ATTRIBUTES_KEY_RANKING_SCORE_OFFSET = "android.net.attributes.key.RANKING_SCORE_OFFSET";
    public static final Creator<ScoredNetwork> CREATOR = new Creator<ScoredNetwork>() {
        public ScoredNetwork createFromParcel(Parcel in) {
            return new ScoredNetwork(in, null);
        }

        public ScoredNetwork[] newArray(int size) {
            return new ScoredNetwork[size];
        }
    };
    public final Bundle attributes;
    public final boolean meteredHint;
    public final NetworkKey networkKey;
    public final RssiCurve rssiCurve;

    public ScoredNetwork(NetworkKey networkKey, RssiCurve rssiCurve) {
        this(networkKey, rssiCurve, false);
    }

    public ScoredNetwork(NetworkKey networkKey, RssiCurve rssiCurve, boolean meteredHint) {
        this(networkKey, rssiCurve, meteredHint, null);
    }

    public ScoredNetwork(NetworkKey networkKey, RssiCurve rssiCurve, boolean meteredHint, Bundle attributes) {
        this.networkKey = networkKey;
        this.rssiCurve = rssiCurve;
        this.meteredHint = meteredHint;
        this.attributes = attributes;
    }

    private ScoredNetwork(Parcel in) {
        this.networkKey = (NetworkKey) NetworkKey.CREATOR.createFromParcel(in);
        boolean z = true;
        if (in.readByte() == (byte) 1) {
            this.rssiCurve = (RssiCurve) RssiCurve.CREATOR.createFromParcel(in);
        } else {
            this.rssiCurve = null;
        }
        if (in.readByte() != (byte) 1) {
            z = false;
        }
        this.meteredHint = z;
        this.attributes = in.readBundle();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        this.networkKey.writeToParcel(out, flags);
        if (this.rssiCurve != null) {
            out.writeByte((byte) 1);
            this.rssiCurve.writeToParcel(out, flags);
        } else {
            out.writeByte((byte) 0);
        }
        out.writeByte((byte) this.meteredHint);
        out.writeBundle(this.attributes);
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScoredNetwork that = (ScoredNetwork) o;
        if (!(Objects.equals(this.networkKey, that.networkKey) && Objects.equals(this.rssiCurve, that.rssiCurve) && Objects.equals(Boolean.valueOf(this.meteredHint), Boolean.valueOf(that.meteredHint)) && bundleEquals(this.attributes, that.attributes))) {
            z = false;
        }
        return z;
    }

    /* JADX WARNING: Missing block: B:17:0x003a, code skipped:
            return false;
     */
    private boolean bundleEquals(android.os.Bundle r9, android.os.Bundle r10) {
        /*
        r8 = this;
        r0 = 1;
        if (r9 != r10) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = 0;
        if (r9 == 0) goto L_0x003a;
    L_0x0007:
        if (r10 != 0) goto L_0x000a;
    L_0x0009:
        goto L_0x003a;
    L_0x000a:
        r2 = r9.size();
        r3 = r10.size();
        if (r2 == r3) goto L_0x0015;
    L_0x0014:
        return r1;
    L_0x0015:
        r2 = r9.keySet();
        r3 = r2.iterator();
    L_0x001d:
        r4 = r3.hasNext();
        if (r4 == 0) goto L_0x0039;
    L_0x0023:
        r4 = r3.next();
        r4 = (java.lang.String) r4;
        r5 = r9.get(r4);
        r6 = r10.get(r4);
        r7 = java.util.Objects.equals(r5, r6);
        if (r7 != 0) goto L_0x0038;
    L_0x0037:
        return r1;
    L_0x0038:
        goto L_0x001d;
    L_0x0039:
        return r0;
    L_0x003a:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.ScoredNetwork.bundleEquals(android.os.Bundle, android.os.Bundle):boolean");
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.networkKey, this.rssiCurve, Boolean.valueOf(this.meteredHint), this.attributes});
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ScoredNetwork{networkKey=");
        stringBuilder.append(this.networkKey);
        stringBuilder.append(", rssiCurve=");
        stringBuilder.append(this.rssiCurve);
        stringBuilder.append(", meteredHint=");
        stringBuilder.append(this.meteredHint);
        StringBuilder out = new StringBuilder(stringBuilder.toString());
        Bundle bundle = this.attributes;
        if (!(bundle == null || bundle.isEmpty())) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(", attributes=");
            stringBuilder.append(this.attributes);
            out.append(stringBuilder.toString());
        }
        out.append('}');
        return out.toString();
    }

    public boolean hasRankingScore() {
        if (this.rssiCurve == null) {
            Bundle bundle = this.attributes;
            if (bundle == null || !bundle.containsKey(ATTRIBUTES_KEY_RANKING_SCORE_OFFSET)) {
                return false;
            }
        }
        return true;
    }

    public int calculateRankingScore(int rssi) throws UnsupportedOperationException {
        if (hasRankingScore()) {
            int offset = 0;
            Bundle bundle = this.attributes;
            int i = 0;
            if (bundle != null) {
                offset = 0 + bundle.getInt(ATTRIBUTES_KEY_RANKING_SCORE_OFFSET, 0);
            }
            RssiCurve rssiCurve = this.rssiCurve;
            if (rssiCurve != null) {
                i = rssiCurve.lookupScore(rssi) << 8;
            }
            int score = i;
            try {
                return Math.addExact(score, offset);
            } catch (ArithmeticException e) {
                return score < 0 ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            }
        }
        throw new UnsupportedOperationException("Either rssiCurve or rankingScoreOffset is required to calculate the ranking score");
    }

    public int calculateBadge(int rssi) {
        Bundle bundle = this.attributes;
        if (bundle != null) {
            String str = ATTRIBUTES_KEY_BADGING_CURVE;
            if (bundle.containsKey(str)) {
                return ((RssiCurve) this.attributes.getParcelable(str)).lookupScore(rssi);
            }
        }
        return 0;
    }
}
