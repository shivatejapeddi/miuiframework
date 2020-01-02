package android.telephony;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.telephony.RILConstants;

public class RadioAccessFamily implements Parcelable {
    private static final int CDMA = 72;
    public static final Creator<RadioAccessFamily> CREATOR = new Creator<RadioAccessFamily>() {
        public RadioAccessFamily createFromParcel(Parcel in) {
            return new RadioAccessFamily(in.readInt(), in.readInt());
        }

        public RadioAccessFamily[] newArray(int size) {
            return new RadioAccessFamily[size];
        }
    };
    private static final int EVDO = 10288;
    private static final int GSM = 32771;
    private static final int HS = 17280;
    private static final int LTE = 266240;
    private static final int NR = 524288;
    public static final int RAF_1xRTT = 64;
    public static final int RAF_EDGE = 2;
    public static final int RAF_EHRPD = 8192;
    public static final int RAF_EVDO_0 = 16;
    public static final int RAF_EVDO_A = 32;
    public static final int RAF_EVDO_B = 2048;
    public static final int RAF_GPRS = 1;
    public static final int RAF_GSM = 32768;
    public static final int RAF_HSDPA = 128;
    public static final int RAF_HSPA = 512;
    public static final int RAF_HSPAP = 16384;
    public static final int RAF_HSUPA = 256;
    public static final int RAF_IS95A = 8;
    public static final int RAF_IS95B = 8;
    public static final int RAF_LTE = 4096;
    public static final int RAF_LTE_CA = 262144;
    public static final int RAF_NR = 524288;
    public static final int RAF_TD_SCDMA = 65536;
    public static final int RAF_UMTS = 4;
    public static final int RAF_UNKNOWN = 0;
    private static final int WCDMA = 17284;
    private int mPhoneId;
    private int mRadioAccessFamily;

    @UnsupportedAppUsage
    public RadioAccessFamily(int phoneId, int radioAccessFamily) {
        this.mPhoneId = phoneId;
        this.mRadioAccessFamily = radioAccessFamily;
    }

    @UnsupportedAppUsage
    public int getPhoneId() {
        return this.mPhoneId;
    }

    @UnsupportedAppUsage
    public int getRadioAccessFamily() {
        return this.mRadioAccessFamily;
    }

    public String toString() {
        String ret = new StringBuilder();
        ret.append("{ mPhoneId = ");
        ret.append(this.mPhoneId);
        ret.append(", mRadioAccessFamily = ");
        ret.append(this.mRadioAccessFamily);
        ret.append("}");
        return ret.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel outParcel, int flags) {
        outParcel.writeInt(this.mPhoneId);
        outParcel.writeInt(this.mRadioAccessFamily);
    }

    @UnsupportedAppUsage
    public static int getRafFromNetworkType(int type) {
        switch (type) {
            case 0:
                return 50055;
            case 1:
                return 32771;
            case 2:
                return WCDMA;
            case 3:
                return 50055;
            case 4:
                return 10360;
            case 5:
                return 72;
            case 6:
                return EVDO;
            case 7:
                return 60415;
            case 8:
                return 276600;
            case 9:
                return 316295;
            case 10:
                return 326655;
            case 11:
                return 266240;
            case 12:
                return 283524;
            case 13:
                return 65536;
            case 14:
                return 82820;
            case 15:
                return 331776;
            case 16:
                return 98307;
            case 17:
                return 364547;
            case 18:
                return 115591;
            case 19:
                return 349060;
            case 20:
                return 381831;
            case 21:
                return 125951;
            case 22:
                return 392191;
            case 23:
                return 524288;
            case 24:
                return 790528;
            case 25:
                return 800888;
            case 26:
                return 840583;
            case 27:
                return 850943;
            case 28:
                return 807812;
            case 29:
                return 856064;
            case 30:
                return 888835;
            case 31:
                return 873348;
            case 32:
                return 906119;
            case 33:
                return 916479;
            default:
                return 0;
        }
    }

    private static int getAdjustedRaf(int raf) {
        raf = (raf & 32771) > 0 ? 32771 | raf : raf;
        raf = (raf & WCDMA) > 0 ? raf | WCDMA : raf;
        raf = (raf & 72) > 0 ? raf | 72 : raf;
        raf = (raf & EVDO) > 0 ? raf | EVDO : raf;
        raf = (raf & 266240) > 0 ? 266240 | raf : raf;
        return (raf & 524288) > 0 ? 524288 | raf : raf;
    }

    public static int getHighestRafCapability(int raf) {
        if ((266240 & raf) > 0) {
            return 3;
        }
        if (((raf & WCDMA) | 27568) > 0) {
            return 2;
        }
        if ((32771 | (raf & 72)) > 0) {
            return 1;
        }
        return 0;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public static int getNetworkTypeFromRaf(int raf) {
        switch (getAdjustedRaf(raf)) {
            case 72:
                return 5;
            case EVDO /*10288*/:
                return 6;
            case 10360:
                return 4;
            case WCDMA /*17284*/:
                return 2;
            case 32771:
                return 1;
            case 50055:
                return 0;
            case 60415:
                return 7;
            case 65536:
                return 13;
            case 82820:
                return 14;
            case 98307:
                return 16;
            case 115591:
                return 18;
            case 125951:
                return 21;
            case 266240:
                return 11;
            case 276600:
                return 8;
            case 283524:
                return 12;
            case 316295:
                return 9;
            case 326655:
                return 10;
            case 331776:
                return 15;
            case 349060:
                return 19;
            case 364547:
                return 17;
            case 381831:
                return 20;
            case 392191:
                return 22;
            case 524288:
                return 23;
            case 790528:
                return 24;
            case 800888:
                return 25;
            case 807812:
                return 28;
            case 840583:
                return 26;
            case 850943:
                return 27;
            case 856064:
                return 29;
            case 873348:
                return 31;
            case 888835:
                return 30;
            case 906119:
                return 32;
            case 916479:
                return 33;
            default:
                return RILConstants.PREFERRED_NETWORK_MODE;
        }
    }

    public static int singleRafTypeFromString(java.lang.String r7) {
        /*
        r0 = r7.hashCode();
        r1 = 16;
        r2 = 4;
        r3 = 2;
        r4 = 1;
        r5 = 0;
        r6 = 8;
        switch(r0) {
            case -2039427040: goto L_0x0107;
            case -908593671: goto L_0x00fd;
            case 2315: goto L_0x00f2;
            case 2500: goto L_0x00e7;
            case 70881: goto L_0x00dc;
            case 75709: goto L_0x00d1;
            case 2063797: goto L_0x00c6;
            case 2123197: goto L_0x00bc;
            case 2140412: goto L_0x00b1;
            case 2194666: goto L_0x00a6;
            case 2227260: goto L_0x009a;
            case 2608919: goto L_0x008f;
            case 47955627: goto L_0x0084;
            case 65949251: goto L_0x0078;
            case 69034058: goto L_0x006d;
            case 69045140: goto L_0x0061;
            case 69050395: goto L_0x0055;
            case 69946171: goto L_0x004a;
            case 69946172: goto L_0x003f;
            case 82410124: goto L_0x0033;
            case 2056938925: goto L_0x0028;
            case 2056938942: goto L_0x001d;
            case 2056938943: goto L_0x0011;
            default: goto L_0x000f;
        };
    L_0x000f:
        goto L_0x0112;
    L_0x0011:
        r0 = "EVDO_B";
        r0 = r7.equals(r0);
        if (r0 == 0) goto L_0x000f;
    L_0x0019:
        r0 = 11;
        goto L_0x0113;
    L_0x001d:
        r0 = "EVDO_A";
        r0 = r7.equals(r0);
        if (r0 == 0) goto L_0x000f;
    L_0x0025:
        r0 = 7;
        goto L_0x0113;
    L_0x0028:
        r0 = "EVDO_0";
        r0 = r7.equals(r0);
        if (r0 == 0) goto L_0x000f;
    L_0x0030:
        r0 = 6;
        goto L_0x0113;
    L_0x0033:
        r0 = "WCDMA";
        r0 = r7.equals(r0);
        if (r0 == 0) goto L_0x000f;
    L_0x003b:
        r0 = 20;
        goto L_0x0113;
    L_0x003f:
        r0 = "IS95B";
        r0 = r7.equals(r0);
        if (r0 == 0) goto L_0x000f;
    L_0x0047:
        r0 = r2;
        goto L_0x0113;
    L_0x004a:
        r0 = "IS95A";
        r0 = r7.equals(r0);
        if (r0 == 0) goto L_0x000f;
    L_0x0052:
        r0 = 3;
        goto L_0x0113;
    L_0x0055:
        r0 = "HSUPA";
        r0 = r7.equals(r0);
        if (r0 == 0) goto L_0x000f;
    L_0x005d:
        r0 = 9;
        goto L_0x0113;
    L_0x0061:
        r0 = "HSPAP";
        r0 = r7.equals(r0);
        if (r0 == 0) goto L_0x000f;
    L_0x0069:
        r0 = 14;
        goto L_0x0113;
    L_0x006d:
        r0 = "HSDPA";
        r0 = r7.equals(r0);
        if (r0 == 0) goto L_0x000f;
    L_0x0075:
        r0 = r6;
        goto L_0x0113;
    L_0x0078:
        r0 = "EHRPD";
        r0 = r7.equals(r0);
        if (r0 == 0) goto L_0x000f;
    L_0x0080:
        r0 = 12;
        goto L_0x0113;
    L_0x0084:
        r0 = "1XRTT";
        r0 = r7.equals(r0);
        if (r0 == 0) goto L_0x000f;
    L_0x008c:
        r0 = 5;
        goto L_0x0113;
    L_0x008f:
        r0 = "UMTS";
        r0 = r7.equals(r0);
        if (r0 == 0) goto L_0x000f;
    L_0x0097:
        r0 = r3;
        goto L_0x0113;
    L_0x009a:
        r0 = "HSPA";
        r0 = r7.equals(r0);
        if (r0 == 0) goto L_0x000f;
    L_0x00a2:
        r0 = 10;
        goto L_0x0113;
    L_0x00a6:
        r0 = "GPRS";
        r0 = r7.equals(r0);
        if (r0 == 0) goto L_0x000f;
    L_0x00ae:
        r0 = r5;
        goto L_0x0113;
    L_0x00b1:
        r0 = "EVDO";
        r0 = r7.equals(r0);
        if (r0 == 0) goto L_0x000f;
    L_0x00b9:
        r0 = 19;
        goto L_0x0113;
    L_0x00bc:
        r0 = "EDGE";
        r0 = r7.equals(r0);
        if (r0 == 0) goto L_0x000f;
    L_0x00c4:
        r0 = r4;
        goto L_0x0113;
    L_0x00c6:
        r0 = "CDMA";
        r0 = r7.equals(r0);
        if (r0 == 0) goto L_0x000f;
    L_0x00ce:
        r0 = 18;
        goto L_0x0113;
    L_0x00d1:
        r0 = "LTE";
        r0 = r7.equals(r0);
        if (r0 == 0) goto L_0x000f;
    L_0x00d9:
        r0 = 13;
        goto L_0x0113;
    L_0x00dc:
        r0 = "GSM";
        r0 = r7.equals(r0);
        if (r0 == 0) goto L_0x000f;
    L_0x00e4:
        r0 = 15;
        goto L_0x0113;
    L_0x00e7:
        r0 = "NR";
        r0 = r7.equals(r0);
        if (r0 == 0) goto L_0x000f;
    L_0x00ef:
        r0 = 22;
        goto L_0x0113;
    L_0x00f2:
        r0 = "HS";
        r0 = r7.equals(r0);
        if (r0 == 0) goto L_0x000f;
    L_0x00fa:
        r0 = 17;
        goto L_0x0113;
    L_0x00fd:
        r0 = "TD_SCDMA";
        r0 = r7.equals(r0);
        if (r0 == 0) goto L_0x000f;
    L_0x0105:
        r0 = r1;
        goto L_0x0113;
    L_0x0107:
        r0 = "LTE_CA";
        r0 = r7.equals(r0);
        if (r0 == 0) goto L_0x000f;
    L_0x010f:
        r0 = 21;
        goto L_0x0113;
    L_0x0112:
        r0 = -1;
    L_0x0113:
        switch(r0) {
            case 0: goto L_0x0150;
            case 1: goto L_0x014f;
            case 2: goto L_0x014e;
            case 3: goto L_0x014d;
            case 4: goto L_0x014c;
            case 5: goto L_0x0149;
            case 6: goto L_0x0148;
            case 7: goto L_0x0145;
            case 8: goto L_0x0142;
            case 9: goto L_0x013f;
            case 10: goto L_0x013c;
            case 11: goto L_0x0139;
            case 12: goto L_0x0136;
            case 13: goto L_0x0133;
            case 14: goto L_0x0130;
            case 15: goto L_0x012c;
            case 16: goto L_0x0129;
            case 17: goto L_0x0126;
            case 18: goto L_0x0123;
            case 19: goto L_0x0120;
            case 20: goto L_0x011d;
            case 21: goto L_0x011a;
            case 22: goto L_0x0117;
            default: goto L_0x0116;
        };
    L_0x0116:
        return r5;
    L_0x0117:
        r0 = 524288; // 0x80000 float:7.34684E-40 double:2.590327E-318;
        return r0;
    L_0x011a:
        r0 = 262144; // 0x40000 float:3.67342E-40 double:1.295163E-318;
        return r0;
    L_0x011d:
        r0 = 17284; // 0x4384 float:2.422E-41 double:8.5394E-320;
        return r0;
    L_0x0120:
        r0 = 10288; // 0x2830 float:1.4417E-41 double:5.083E-320;
        return r0;
    L_0x0123:
        r0 = 72;
        return r0;
    L_0x0126:
        r0 = 17280; // 0x4380 float:2.4214E-41 double:8.5375E-320;
        return r0;
    L_0x0129:
        r0 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
        return r0;
    L_0x012c:
        r0 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
        return r0;
    L_0x0130:
        r0 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        return r0;
    L_0x0133:
        r0 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        return r0;
    L_0x0136:
        r0 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        return r0;
    L_0x0139:
        r0 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;
        return r0;
    L_0x013c:
        r0 = 512; // 0x200 float:7.175E-43 double:2.53E-321;
        return r0;
    L_0x013f:
        r0 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
        return r0;
    L_0x0142:
        r0 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        return r0;
    L_0x0145:
        r0 = 32;
        return r0;
    L_0x0148:
        return r1;
    L_0x0149:
        r0 = 64;
        return r0;
    L_0x014c:
        return r6;
    L_0x014d:
        return r6;
    L_0x014e:
        return r2;
    L_0x014f:
        return r3;
    L_0x0150:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.telephony.RadioAccessFamily.singleRafTypeFromString(java.lang.String):int");
    }

    public static int rafTypeFromString(String rafList) {
        int result = 0;
        for (String raf : rafList.toUpperCase().split("\\|")) {
            int rafType = singleRafTypeFromString(raf.trim());
            if (rafType == 0) {
                return rafType;
            }
            result |= rafType;
        }
        return result;
    }
}
