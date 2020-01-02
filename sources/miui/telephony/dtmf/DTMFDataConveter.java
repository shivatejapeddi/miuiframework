package miui.telephony.dtmf;

import android.util.Log;

public class DTMFDataConveter {
    private static final String LOG_TAG = "DTMFDataConveter";
    private int mBitPerSample;
    private boolean mSign;

    public DTMFDataConveter() {
        this(8, true);
    }

    public DTMFDataConveter(int bitPerSample, boolean sign) {
        setBitPerSample(bitPerSample);
        setSign(sign);
    }

    public int getBitPerSample() {
        return this.mBitPerSample;
    }

    public void setBitPerSample(int bitPerSample) {
        this.mBitPerSample = bitPerSample;
    }

    public boolean getSign() {
        return this.mSign;
    }

    public void setSign(boolean sign) {
        this.mSign = sign;
    }

    public float[] byteToFloat(byte[] byteList) {
        if (byteList == null) {
            Log.i(LOG_TAG, "bit mode can not match");
            return null;
        }
        float[] floatlist = new float[(byteList.length / (getBitPerSample() / 8))];
        for (int i = 0; i < floatlist.length; i++) {
            if (getBitPerSample() == 8) {
                if (this.mSign) {
                    floatlist[i] = ((float) byteList[i]) / 127.0f;
                } else {
                    floatlist[i] = ((float) ((byteList[i] & 255) - 127)) * 0.007874016f;
                }
            } else if (getBitPerSample() == 16) {
                if (this.mSign) {
                    floatlist[i] = ((float) ((short) ((byteList[i * 2] & 255) | (byteList[(i * 2) + 1] << 8)))) * 3.051851E-5f;
                } else {
                    floatlist[i] = ((float) (((byteList[i * 2] & 255) | ((byteList[(i * 2) + 1] & 255) << 8)) - 32767)) * 3.051851E-5f;
                }
            }
        }
        return floatlist;
    }

    public byte[] floatToByte(float[] floatList) {
        if (floatList == null) {
            Log.i(LOG_TAG, "bit mode can not match");
            return null;
        }
        byte[] bytelist = new byte[(floatList.length * (getBitPerSample() / 8))];
        for (int i = 0; i < floatList.length; i++) {
            if (getBitPerSample() == 8) {
                if (this.mSign) {
                    bytelist[i] = (byte) ((int) (floatList[i] * 127.0f));
                } else {
                    bytelist[i] = (byte) ((int) ((floatList[i] * 127.0f) + 127.0f));
                }
            } else if (getBitPerSample() == 16) {
                int x;
                if (this.mSign) {
                    x = (int) (((double) floatList[i]) * 32767.0d);
                    bytelist[i * 2] = (byte) x;
                    bytelist[(i * 2) + 1] = (byte) (x >>> 8);
                } else {
                    x = ((int) (((double) floatList[i]) * 32767.0d)) + 32767;
                    bytelist[i * 2] = (byte) x;
                    bytelist[(i * 2) + 1] = (byte) (x >>> 8);
                }
            }
        }
        return bytelist;
    }
}
