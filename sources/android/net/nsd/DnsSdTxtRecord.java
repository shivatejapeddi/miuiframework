package android.net.nsd;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class DnsSdTxtRecord implements Parcelable {
    public static final Creator<DnsSdTxtRecord> CREATOR = new Creator<DnsSdTxtRecord>() {
        public DnsSdTxtRecord createFromParcel(Parcel in) {
            DnsSdTxtRecord info = new DnsSdTxtRecord();
            in.readByteArray(info.mData);
            return info;
        }

        public DnsSdTxtRecord[] newArray(int size) {
            return new DnsSdTxtRecord[size];
        }
    };
    private static final byte mSeperator = (byte) 61;
    private byte[] mData;

    public DnsSdTxtRecord() {
        this.mData = new byte[0];
    }

    public DnsSdTxtRecord(byte[] data) {
        this.mData = (byte[]) data.clone();
    }

    public DnsSdTxtRecord(DnsSdTxtRecord src) {
        if (src != null) {
            byte[] bArr = src.mData;
            if (bArr != null) {
                this.mData = (byte[]) bArr.clone();
            }
        }
    }

    public void set(String key, String value) {
        byte[] valBytes;
        int valLen;
        if (value != null) {
            valBytes = value.getBytes();
            valLen = valBytes.length;
        } else {
            valBytes = null;
            valLen = 0;
        }
        try {
            byte[] keyBytes = key.getBytes("US-ASCII");
            int i = 0;
            while (i < keyBytes.length) {
                if (keyBytes[i] != mSeperator) {
                    i++;
                } else {
                    throw new IllegalArgumentException("= is not a valid character in key");
                }
            }
            if (keyBytes.length + valLen < 255) {
                i = remove(key);
                if (i == -1) {
                    i = keyCount();
                }
                insert(keyBytes, valBytes, i);
                return;
            }
            throw new IllegalArgumentException("Key and Value length cannot exceed 255 bytes");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("key should be US-ASCII");
        }
    }

    public String get(String key) {
        byte[] val = getValue(key);
        return val != null ? new String(val) : null;
    }

    public int remove(String key) {
        int avStart = 0;
        int i = 0;
        while (true) {
            int avLen = this.mData;
            if (avStart >= avLen.length) {
                return -1;
            }
            avLen = avLen[avStart];
            if (key.length() > avLen || !((key.length() == avLen || this.mData[(key.length() + avStart) + 1] == mSeperator) && key.compareToIgnoreCase(new String(this.mData, avStart + 1, key.length())) == 0)) {
                avStart += (avLen + 1) & 255;
                i++;
            } else {
                byte[] oldBytes = this.mData;
                this.mData = new byte[((oldBytes.length - avLen) - 1)];
                System.arraycopy(oldBytes, 0, this.mData, 0, avStart);
                System.arraycopy(oldBytes, (avStart + avLen) + 1, this.mData, avStart, ((oldBytes.length - avStart) - avLen) - 1);
                return i;
            }
        }
    }

    public int keyCount() {
        int count = 0;
        int nextKey = 0;
        while (true) {
            byte[] bArr = this.mData;
            if (nextKey >= bArr.length) {
                return count;
            }
            nextKey += (bArr[nextKey] + 1) & 255;
            count++;
        }
    }

    public boolean contains(String key) {
        int i = 0;
        while (true) {
            String key2 = getKey(i);
            String s = key2;
            if (key2 == null) {
                return false;
            }
            if (key.compareToIgnoreCase(s) == 0) {
                return true;
            }
            i++;
        }
    }

    public int size() {
        return this.mData.length;
    }

    public byte[] getRawData() {
        return (byte[]) this.mData.clone();
    }

    private void insert(byte[] keyBytes, byte[] value, int index) {
        int i;
        byte[] oldBytes = this.mData;
        int valLen = value != null ? value.length : 0;
        int insertion = 0;
        for (i = 0; i < index; i++) {
            byte[] bArr = this.mData;
            if (insertion >= bArr.length) {
                break;
            }
            insertion += (bArr[insertion] + 1) & 255;
        }
        i = (keyBytes.length + valLen) + (value != null ? 1 : 0);
        int newLen = (oldBytes.length + i) + 1;
        this.mData = new byte[newLen];
        System.arraycopy(oldBytes, 0, this.mData, 0, insertion);
        int secondHalfLen = oldBytes.length - insertion;
        System.arraycopy(oldBytes, insertion, this.mData, newLen - secondHalfLen, secondHalfLen);
        byte[] bArr2 = this.mData;
        bArr2[insertion] = (byte) i;
        System.arraycopy(keyBytes, 0, bArr2, insertion + 1, keyBytes.length);
        if (value != null) {
            bArr2 = this.mData;
            bArr2[(insertion + 1) + keyBytes.length] = mSeperator;
            System.arraycopy(value, 0, bArr2, (keyBytes.length + insertion) + 2, valLen);
        }
    }

    private String getKey(int index) {
        int i;
        int avStart = 0;
        for (i = 0; i < index; i++) {
            byte[] bArr = this.mData;
            if (avStart >= bArr.length) {
                break;
            }
            avStart += bArr[avStart] + 1;
        }
        i = this.mData;
        if (avStart >= i.length) {
            return null;
        }
        i = i[avStart];
        int aLen = 0;
        while (aLen < i && this.mData[(avStart + aLen) + 1] != mSeperator) {
            aLen++;
        }
        return new String(this.mData, avStart + 1, aLen);
    }

    private byte[] getValue(int index) {
        int i;
        int avStart = 0;
        for (i = 0; i < index; i++) {
            byte[] bArr = this.mData;
            if (avStart >= bArr.length) {
                break;
            }
            avStart += bArr[avStart] + 1;
        }
        i = this.mData;
        if (avStart >= i.length) {
            return null;
        }
        i = i[avStart];
        for (int aLen = 0; aLen < i; aLen++) {
            byte[] bArr2 = this.mData;
            if (bArr2[(avStart + aLen) + 1] == mSeperator) {
                byte[] value = new byte[((i - aLen) - 1)];
                System.arraycopy(bArr2, (avStart + aLen) + 2, value, 0, (i - aLen) - 1);
                return value;
            }
        }
        return null;
    }

    private String getValueAsString(int index) {
        byte[] value = getValue(index);
        return value != null ? new String(value) : null;
    }

    private byte[] getValue(String forKey) {
        int i = 0;
        while (true) {
            String key = getKey(i);
            String s = key;
            if (key == null) {
                return null;
            }
            if (forKey.compareToIgnoreCase(s) == 0) {
                return getValue(i);
            }
            i++;
        }
    }

    public String toString() {
        String result = null;
        int i = 0;
        while (true) {
            String key = getKey(i);
            String a = key;
            if (key == null) {
                break;
            }
            key = new StringBuilder();
            key.append("{");
            key.append(a);
            key = key.toString();
            String val = getValueAsString(i);
            String str = "}";
            StringBuilder stringBuilder;
            if (val != null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(key);
                stringBuilder.append("=");
                stringBuilder.append(val);
                stringBuilder.append(str);
                key = stringBuilder.toString();
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append(key);
                stringBuilder.append(str);
                key = stringBuilder.toString();
            }
            if (result == null) {
                result = key;
            } else {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(result);
                stringBuilder2.append(", ");
                stringBuilder2.append(key);
                result = stringBuilder2.toString();
            }
            i++;
        }
        return result != null ? result : "";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof DnsSdTxtRecord) {
            return Arrays.equals(((DnsSdTxtRecord) o).mData, this.mData);
        }
        return false;
    }

    public int hashCode() {
        return Arrays.hashCode(this.mData);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(this.mData);
    }
}
