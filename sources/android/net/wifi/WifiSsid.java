package android.net.wifi;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;
import java.util.Locale;

public class WifiSsid implements Parcelable {
    @UnsupportedAppUsage
    public static final Creator<WifiSsid> CREATOR = new Creator<WifiSsid>() {
        public WifiSsid createFromParcel(Parcel in) {
            WifiSsid ssid = new WifiSsid();
            int length = in.readInt();
            byte[] b = new byte[length];
            in.readByteArray(b);
            ssid.octets.write(b, 0, length);
            return ssid;
        }

        public WifiSsid[] newArray(int size) {
            return new WifiSsid[size];
        }
    };
    private static final int HEX_RADIX = 16;
    @UnsupportedAppUsage
    public static final String NONE = "<unknown ssid>";
    private static final String TAG = "WifiSsid";
    @UnsupportedAppUsage
    public final ByteArrayOutputStream octets;

    /* synthetic */ WifiSsid(AnonymousClass1 x0) {
        this();
    }

    private WifiSsid() {
        this.octets = new ByteArrayOutputStream(32);
    }

    public static WifiSsid createFromByteArray(byte[] ssid) {
        WifiSsid wifiSsid = new WifiSsid();
        if (ssid != null) {
            wifiSsid.octets.write(ssid, 0, ssid.length);
        }
        return wifiSsid;
    }

    @UnsupportedAppUsage
    public static WifiSsid createFromAsciiEncoded(String asciiEncoded) {
        WifiSsid a = new WifiSsid();
        a.convertToBytes(asciiEncoded);
        return a;
    }

    public static WifiSsid createFromHex(String hexStr) {
        WifiSsid a = new WifiSsid();
        if (hexStr == null) {
            return a;
        }
        if (hexStr.startsWith("0x") || hexStr.startsWith("0X")) {
            hexStr = hexStr.substring(2);
        }
        for (int i = 0; i < hexStr.length() - 1; i += 2) {
            int val;
            try {
                val = Integer.parseInt(hexStr.substring(i, i + 2), 16);
            } catch (NumberFormatException e) {
                val = 0;
            }
            a.octets.write(val);
        }
        return a;
    }

    private void convertToBytes(String asciiEncoded) {
        int i = 0;
        while (i < asciiEncoded.length()) {
            char c = asciiEncoded.charAt(i);
            if (c != '\\') {
                this.octets.write(c);
                i++;
            } else {
                i++;
                char charAt = asciiEncoded.charAt(i);
                int i2;
                if (charAt == '\"') {
                    this.octets.write(34);
                    i++;
                } else if (charAt == '\\') {
                    this.octets.write(92);
                    i++;
                } else if (charAt == 'e') {
                    this.octets.write(27);
                    i++;
                } else if (charAt == 'n') {
                    this.octets.write(10);
                    i++;
                } else if (charAt == 'r') {
                    this.octets.write(13);
                    i++;
                } else if (charAt == 't') {
                    this.octets.write(9);
                    i++;
                } else if (charAt != StateProperty.TARGET_X) {
                    switch (charAt) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                            int val = asciiEncoded.charAt(i) - 48;
                            i++;
                            if (asciiEncoded.charAt(i) >= '0' && asciiEncoded.charAt(i) <= '7') {
                                val = ((val * 8) + asciiEncoded.charAt(i)) - 48;
                                i++;
                            }
                            if (asciiEncoded.charAt(i) >= '0' && asciiEncoded.charAt(i) <= '7') {
                                val = ((val * 8) + asciiEncoded.charAt(i)) - 48;
                                i++;
                            }
                            this.octets.write(val);
                            i2 = val;
                            break;
                        default:
                            break;
                    }
                } else {
                    i++;
                    try {
                        i2 = Integer.parseInt(asciiEncoded.substring(i, i + 2), 16);
                    } catch (NumberFormatException e) {
                        i2 = -1;
                    } catch (StringIndexOutOfBoundsException e2) {
                        i2 = -1;
                    }
                    if (i2 < 0) {
                        i2 = Character.digit(asciiEncoded.charAt(i), 16);
                        if (i2 >= 0) {
                            this.octets.write(i2);
                            i++;
                        }
                    } else {
                        this.octets.write(i2);
                        i += 2;
                    }
                }
            }
        }
    }

    public String toString() {
        byte[] ssidBytes = this.octets.toByteArray();
        if (this.octets.size() <= 0 || isArrayAllZeroes(ssidBytes)) {
            return "";
        }
        CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
        CharBuffer out = CharBuffer.allocate(32);
        CoderResult result = decoder.decode(ByteBuffer.wrap(ssidBytes), out, true);
        out.flip();
        if (result.isError()) {
            return NONE;
        }
        return out.toString();
    }

    public boolean equals(Object thatObject) {
        if (this == thatObject) {
            return true;
        }
        if (!(thatObject instanceof WifiSsid)) {
            return false;
        }
        return Arrays.equals(this.octets.toByteArray(), ((WifiSsid) thatObject).octets.toByteArray());
    }

    public int hashCode() {
        return Arrays.hashCode(this.octets.toByteArray());
    }

    private boolean isArrayAllZeroes(byte[] ssidBytes) {
        for (byte b : ssidBytes) {
            if (b != (byte) 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isHidden() {
        return isArrayAllZeroes(this.octets.toByteArray());
    }

    @UnsupportedAppUsage
    public byte[] getOctets() {
        return this.octets.toByteArray();
    }

    public String getHexString() {
        if (this.octets.size() == 0) {
            return NONE;
        }
        String out = "0x";
        byte[] ssidbytes = getOctets();
        for (int i = 0; i < this.octets.size(); i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(out);
            stringBuilder.append(String.format(Locale.US, "%02x", new Object[]{Byte.valueOf(ssidbytes[i])}));
            out = stringBuilder.toString();
        }
        return this.octets.size() > 0 ? out : null;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.octets.size());
        dest.writeByteArray(this.octets.toByteArray());
    }
}
