package miui.telephony.dtmf;

import android.util.Log;
import java.io.IOException;

public class DTMFUtil {
    private static final int[] DECIBEL_THRESHOLD_LIST = new int[]{23, 32, 31, 34, 37};
    private static final int DEFAULT_SAMPLE_RATE = 8000;
    private static final int FREQUENCE_INDEX_GAP = 5;
    private static final int HEALTHY = 3;
    private static final String LOG_TAG = "DTMFUtil";
    private static final int[] SAMPLE_RATE_LIST = new int[]{8000, 11025, 22050, 32000, 44100};
    private static final int STEP = 256;
    private static final float[] TARGET_FREQUENCIES = new float[]{TARGET_LOW_FREQUENCY, 697.0f, 770.0f, 852.0f, 941.0f, TARGET_HIGH_FREQUENCY, 1209.0f, 1336.0f, 1477.0f, 1633.0f};
    private static final float TARGET_HIGH_FREQUENCY = 1075.0f;
    private static final float TARGET_LOW_FREQUENCY = 600.0f;
    private static final int VERSION = 2;
    private int mHealthy;
    private float[] mPreCalculatedCosines;
    private int mSampleRate;
    private float[] mWnkList;

    public static int getVersion() {
        return 2;
    }

    public static float getTargetLowFrequency() {
        return TARGET_LOW_FREQUENCY;
    }

    public static float getTargetHighFrequency() {
        return TARGET_HIGH_FREQUENCY;
    }

    public DTMFUtil() {
        this(8000, 3);
    }

    public DTMFUtil(int sampleRate, int healthy) {
        float[] fArr = TARGET_FREQUENCIES;
        this.mPreCalculatedCosines = new float[fArr.length];
        this.mWnkList = new float[fArr.length];
        this.mSampleRate = sampleRate;
        initPrecalculatedCosines();
        this.mHealthy = healthy > 0 ? healthy : 3;
    }

    public int getSampleRate() {
        return this.mSampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.mSampleRate = sampleRate;
        initPrecalculatedCosines();
    }

    public int getHealthy() {
        return this.mHealthy;
    }

    public void setHealthy(int healthy) {
        this.mHealthy = healthy;
    }

    private float[] dtmfFrequenciesDetecter(float[] audioDataList) {
        float[] fArr = audioDataList;
        boolean[] detectFrequencies = new boolean[TARGET_FREQUENCIES.length];
        float[] outpair = new float[]{-1.0f, -1.0f};
        int threshold = getDecibelThreshold(this.mSampleRate);
        if (threshold == -1) {
            Log.i(LOG_TAG, "can not get threshold");
            return outpair;
        }
        float[] calculatedDual = new float[TARGET_FREQUENCIES.length];
        int j = 0;
        while (j < TARGET_FREQUENCIES.length) {
            float skn2 = 0.0f;
            float skn1 = 0.0f;
            float skn0 = 0.0f;
            for (float f : fArr) {
                skn2 = skn1;
                skn1 = skn0;
                skn0 = ((this.mPreCalculatedCosines[j] * skn1) - skn2) + f;
            }
            float skn02 = skn0;
            calculatedDual[j] = (float) (Math.log10((double) Math.abs(skn0 - (this.mWnkList[j] * skn1))) * 20.0d);
            if (calculatedDual[j] > ((float) threshold)) {
                detectFrequencies[j] = true;
            }
            j++;
            skn0 = skn02;
        }
        for (int k = 0; k < detectFrequencies.length / 2; k++) {
            j = k + 5;
            float[] fArr2 = TARGET_FREQUENCIES;
            j %= fArr2.length;
            if (detectFrequencies[k] && detectFrequencies[j]) {
                outpair[0] = fArr2[k];
                outpair[1] = fArr2[j];
                if (isValidate(outpair)) {
                    return outpair;
                }
            }
        }
        return null;
    }

    private float[] dtmfDetectAndValidate(byte[] audioDataList, int bitPerSample, boolean sign) throws IOException {
        int segto = 0;
        int vcnt = 0;
        byte[] bufferSegment = new byte[256];
        float[] detectedBuffer = new float[(this.mHealthy * 2)];
        float[] detect = new float[2];
        DTMFAudioInputStream streamReader = new DTMFAudioInputStream(audioDataList);
        DTMFDataConveter Converter = new DTMFDataConveter(bitPerSample, sign);
        while (vcnt < this.mHealthy && segto < audioDataList.length) {
            int length;
            int segfrom = segto;
            if (segto + 256 > audioDataList.length) {
                length = audioDataList.length;
            } else {
                length = segto + 256;
            }
            segto = length;
            streamReader.read(bufferSegment, segto - segfrom);
            detect = dtmfFrequenciesDetecter(Converter.byteToFloat(bufferSegment));
            if (detect != null) {
                detectedBuffer[vcnt * 2] = detect[0];
                detectedBuffer[(vcnt * 2) + 1] = detect[1];
                vcnt++;
            }
        }
        streamReader.close();
        if (vcnt == this.mHealthy) {
            return bufferFilter(detectedBuffer, vcnt * 2);
        }
        return null;
    }

    private void initPrecalculatedCosines() {
        if (this.mSampleRate == 0) {
            Log.i(LOG_TAG, "fail to dispatching funtion initPrecalculatedCosines: you need to set mSampleRate");
            return;
        }
        int i = 0;
        while (true) {
            float[] fArr = TARGET_FREQUENCIES;
            if (i < fArr.length) {
                this.mPreCalculatedCosines[i] = (float) (Math.cos((((double) fArr[i]) * 6.283185307179586d) / ((double) this.mSampleRate)) * 2.0d);
                this.mWnkList[i] = (float) Math.exp((((double) TARGET_FREQUENCIES[i]) * -6.283185307179586d) / ((double) this.mSampleRate));
                i++;
            } else {
                return;
            }
        }
    }

    private float[] bufferFilter(float[] buffer, int len) {
        if (buffer.length <= 2) {
            return buffer;
        }
        int i;
        float[] ret = new float[2];
        float low = buffer[0];
        float high = buffer[1];
        for (i = 0; i < buffer.length / 2; i += 2) {
            if (low != buffer[i]) {
                low = buffer[i];
            }
        }
        for (i = 1; i < buffer.length / 2; i += 2) {
            if (high != buffer[i]) {
                high = buffer[i];
            }
        }
        if (low != buffer[0] || high != buffer[1]) {
            return null;
        }
        ret[0] = buffer[0];
        ret[1] = buffer[1];
        return ret;
    }

    public float[] parseFrequency(byte[] byteBuffer, int bitPerSample, boolean sign) throws IOException {
        if (byteBuffer != null) {
            return dtmfDetectAndValidate(byteBuffer, bitPerSample, sign);
        }
        Log.i(LOG_TAG, "parameter error: null");
        return null;
    }

    private int getDecibelThreshold(int sampleRate) {
        int length = SAMPLE_RATE_LIST.length;
        int length2 = DECIBEL_THRESHOLD_LIST.length;
        String str = LOG_TAG;
        if (length != length2) {
            Log.i(str, "the number of SAMPLE_RATE_LIST and DECIBEL_THRESHOLD_LIST can not match");
            return -1;
        }
        length = 0;
        while (true) {
            int[] iArr = SAMPLE_RATE_LIST;
            if (length < iArr.length && sampleRate != iArr[length]) {
                length++;
            }
        }
        if (length < SAMPLE_RATE_LIST.length) {
            return DECIBEL_THRESHOLD_LIST[length];
        }
        Log.i(str, "can not find db threshold");
        return -1;
    }

    private boolean isValidate(float[] detect) {
        if (detect == null) {
            Log.i(LOG_TAG, "null parameter");
            return false;
        } else if (detect[1] - detect[0] < 268.0f) {
            return false;
        } else {
            int check1 = 0;
            int check2 = 0;
            int i = 0;
            while (true) {
                float[] fArr = TARGET_FREQUENCIES;
                if (i >= fArr.length) {
                    break;
                }
                if (detect[0] == fArr[i]) {
                    check1 = i;
                }
                if (detect[1] == TARGET_FREQUENCIES[i]) {
                    check2 = i;
                }
                i++;
            }
            if (check2 - check1 == 5) {
                return true;
            }
            return false;
        }
    }

    private static float[] getAudioFloatBuffer(float[] dualFrequence, int sampleRate, int time) {
        float[] fArr = dualFrequence;
        int i = sampleRate;
        int sampleSize = (i * time) / 1000;
        if (fArr.length != 2) {
            Log.i(LOG_TAG, "parameter buffer is null");
            return null;
        }
        double amplitudeF0 = 0.5d;
        double twoPiF0 = ((double) fArr[0]) * 6.283185307179586d;
        double twoPiF1 = ((double) fArr[1]) * 6.283185307179586d;
        float[] buffer = new float[sampleSize];
        int sample = 0;
        while (sample < buffer.length) {
            int sampleSize2 = sampleSize;
            double amplitudeF02 = amplitudeF0;
            double time_detail = ((double) sample) / ((double) i);
            buffer[sample] = (float) ((Math.sin(twoPiF0 * time_detail) * 0.5d) + (Math.sin(twoPiF1 * time_detail) * 0.5d));
            sample++;
            fArr = dualFrequence;
            i = sampleRate;
            sampleSize = sampleSize2;
            amplitudeF0 = amplitudeF02;
        }
        return buffer;
    }

    public static float[] getDualFrequence(char button) {
        float[] hzPair = new float[2];
        if (button == '#') {
            hzPair[0] = 941.0f;
            hzPair[1] = 1477.0f;
        } else if (button != '*') {
            switch (button) {
                case '0':
                    hzPair[0] = 941.0f;
                    hzPair[1] = 1336.0f;
                    break;
                case '1':
                    hzPair[0] = 697.0f;
                    hzPair[1] = 1209.0f;
                    break;
                case '2':
                    hzPair[0] = 697.0f;
                    hzPair[1] = 1336.0f;
                    break;
                case '3':
                    hzPair[0] = 697.0f;
                    hzPair[1] = 1477.0f;
                    break;
                case '4':
                    hzPair[0] = 770.0f;
                    hzPair[1] = 1209.0f;
                    break;
                case '5':
                    hzPair[0] = 770.0f;
                    hzPair[1] = 1336.0f;
                    break;
                case '6':
                    hzPair[0] = 770.0f;
                    hzPair[1] = 1477.0f;
                    break;
                case '7':
                    hzPair[0] = 852.0f;
                    hzPair[1] = 1209.0f;
                    break;
                case '8':
                    hzPair[0] = 852.0f;
                    hzPair[1] = 1336.0f;
                    break;
                case '9':
                    hzPair[0] = 852.0f;
                    hzPair[1] = 1477.0f;
                    break;
                default:
                    hzPair[1] = -1.0f;
                    hzPair[0] = -1.0f;
                    break;
            }
        } else {
            hzPair[0] = 941.0f;
            hzPair[1] = 1209.0f;
        }
        return hzPair;
    }
}
