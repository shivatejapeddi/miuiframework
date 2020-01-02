package android.media.audiofx;

import java.util.UUID;

public class MiSound extends AudioEffect {
    public static final int EARS_COMPENSATION_DISABLE = 0;
    public static final int EARS_COMPENSATION_ENABLE = 1;
    private static final UUID EFFECT_TYPE_MISOUND = UUID.fromString("5b8e36a5-144a-4c38-b1d7-0002a5d5c51b");
    public static final int EQ_SOUND_PRESET_DEFAULT = 0;
    public static final int EQ_SOUND_PRESET_SIXTY = 3;
    public static final int EQ_SOUND_PRESET_THIRTY = 1;
    public static final int EQ_SOUND_PRESET_THIRTY_SIXTY = 2;
    private static final int MISOUND_BAND_MAX_NUMBER = 6;
    public static final int MISOUND_HEADSET_EM001 = 15;
    public static final int MISOUND_HEADSET_EM006 = 18;
    public static final int MISOUND_HEADSET_EM007 = 16;
    public static final int MISOUND_HEADSET_EM013 = 19;
    public static final int MISOUND_HEADSET_EM015 = 20;
    public static final int MISOUND_HEADSET_EM017 = 21;
    public static final int MISOUND_HEADSET_EM018 = 22;
    public static final int MISOUND_HEADSET_EM019 = 23;
    public static final int MISOUND_HEADSET_EM303 = 13;
    public static final int MISOUND_HEADSET_EM304 = 14;
    public static final int MISOUND_HEADSET_GENERAL = 5;
    public static final int MISOUND_HEADSET_GENERAL_INEAR = 6;
    public static final int MISOUND_HEADSET_HM004 = 17;
    public static final int MISOUND_HEADSET_MEP100 = 1;
    public static final int MISOUND_HEADSET_MEP200 = 2;
    public static final int MISOUND_HEADSET_MK101 = 7;
    public static final int MISOUND_HEADSET_MK301 = 8;
    public static final int MISOUND_HEADSET_MK303 = 9;
    public static final int MISOUND_HEADSET_MO701 = 10;
    public static final int MISOUND_HEADSET_MR102 = 11;
    public static final int MISOUND_HEADSET_NONE = 0;
    public static final int MISOUND_HEADSET_PHD = 12;
    public static final int MISOUND_HEADSET_PISTON100 = 3;
    public static final int MISOUND_HEADSET_PISTON200 = 4;
    public static final int MISOUND_MODE_MOVIE = 1;
    public static final int MISOUND_MODE_MOVIE_CINEMA = 0;
    public static final int MISOUND_MODE_MOVIE_CINIMA = 0;
    public static final int MISOUND_MODE_MOVIE_STUDIO = 1;
    public static final int MISOUND_MODE_MUSIC = 0;
    private static final int MISOUND_PARAM_EQ_COMPENSATION_ENABLE = 9;
    private static final int MISOUND_PARAM_EQ_COMPENSATION_EXAMPLE = 14;
    private static final int MISOUND_PARAM_EQ_COMPENSATION_LEFT = 10;
    private static final int MISOUND_PARAM_EQ_COMPENSATION_LEFT_SAVE = 12;
    private static final int MISOUND_PARAM_EQ_COMPENSATION_RIGHT = 11;
    private static final int MISOUND_PARAM_EQ_COMPENSATION_RIGHT_SAVE = 13;
    private static final int MISOUND_PARAM_EQ_LEVEL = 2;
    private static final int MISOUND_PARAM_HEADSET_TYPE = 1;
    private static final int MISOUND_PARAM_HIFI_MODE = 8;
    private static final int MISOUND_PARAM_MODE = 3;
    private static final int MISOUND_PARAM_MOVIE = 5;
    private static final int MISOUND_PARAM_MOVIE_MODE = 7;
    private static final int MISOUND_PARAM_MUSIC = 4;
    private static final String TAG = "MiSound";

    public MiSound(int priority, int audioSession) {
        super(EFFECT_TYPE_NULL, EFFECT_TYPE_MISOUND, priority, audioSession);
    }

    public void setMode(int mode) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        checkStatus(setParameter(3, mode));
    }

    public int getMode() throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        int[] value = new int[1];
        checkStatus(getParameter(3, value));
        return value[0];
    }

    public void setMusic(int enable) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        checkStatus(setParameter(4, enable));
    }

    public int getMusic() throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        int[] value = new int[1];
        checkStatus(getParameter(4, value));
        return value[0];
    }

    public void setMovie(int enable) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        checkStatus(setParameter(5, enable));
    }

    public int getMovie() throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        int[] value = new int[1];
        checkStatus(getParameter(5, value));
        return value[0];
    }

    public void setHeadsetType(int type) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        checkStatus(setParameter(1, type));
    }

    public int getHeadsetType() throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        int[] value = new int[1];
        checkStatus(getParameter(1, value));
        return value[0];
    }

    public void setLevel(int band, float level) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        checkStatus(setParameter(new int[]{2, band}, String.valueOf(level).getBytes()));
    }

    public float getLevel(int band) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        param = new int[2];
        byte[] value = new byte[10];
        param[0] = 2;
        param[1] = band;
        checkStatus(getParameter(param, value));
        return new Float(new String(value)).floatValue();
    }

    public void setEarsCompensationOn(int enable) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        checkStatus(setParameter(9, enable));
    }

    public int getEarsCompensationOn() throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        int[] value = new int[1];
        checkStatus(getParameter(9, value));
        return value[0];
    }

    public void setEarsCompensationEQExample(int example) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        checkStatus(setParameter(14, example));
    }

    public int getEarsCompensationEQExample() throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        int[] value = new int[1];
        checkStatus(getParameter(14, value));
        return value[0];
    }

    public void setEarsCompensationEQLeft(int band, float level) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        int[] param = new int[2];
        param[0] = 10;
        if (band > 6) {
            band = 6;
        }
        param[1] = band;
        checkStatus(setParameter(param, String.valueOf(level).getBytes()));
    }

    public void setEarsCompensationEQLeftSave(int band, float level) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        int[] param = new int[2];
        param[0] = 12;
        if (band > 6) {
            band = 6;
        }
        param[1] = band;
        checkStatus(setParameter(param, String.valueOf(level).getBytes()));
    }

    public void setEarsCompensationAllEQLeft(float[] level, int band_num) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        if (band_num != 6) {
            band_num = 6;
        }
        for (int index = 0; index < band_num; index++) {
            if (index < band_num - 1) {
                setEarsCompensationEQLeftSave(index + 1, level[index]);
            } else if (index == band_num - 1) {
                setEarsCompensationEQLeft(index + 1, level[index]);
            }
        }
    }

    public float getEarsCompensationEQLeft(int band) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        param = new int[2];
        byte[] value = new byte[10];
        param[0] = 10;
        if (band > 6) {
            band = 6;
        }
        param[1] = band;
        checkStatus(getParameter(param, value));
        return new Float(new String(value)).floatValue();
    }

    public float[] getEarsCompensationAllEQLeft() throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        float[] eq_value = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        for (int index = 0; index < 6; index++) {
            eq_value[index] = getEarsCompensationEQLeft(index + 1);
        }
        return eq_value;
    }

    public void setEarsCompensationEQRight(int band, float level) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        int[] param = new int[2];
        param[0] = 11;
        if (band > 6) {
            band = 6;
        }
        param[1] = band;
        checkStatus(setParameter(param, String.valueOf(level).getBytes()));
    }

    public void setEarsCompensationEQRightSave(int band, float level) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        int[] param = new int[2];
        param[0] = 13;
        if (band > 6) {
            band = 6;
        }
        param[1] = band;
        checkStatus(setParameter(param, String.valueOf(level).getBytes()));
    }

    public void setEarsCompensationAllEQRight(float[] level, int band_num) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        if (band_num != 6) {
            band_num = 6;
        }
        for (int index = 0; index < band_num; index++) {
            if (index < band_num - 1) {
                setEarsCompensationEQRightSave(index + 1, level[index]);
            } else if (index == band_num - 1) {
                setEarsCompensationEQRight(index + 1, level[index]);
            }
        }
    }

    public float getEarsCompensationEQRight(int band) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        param = new int[2];
        byte[] value = new byte[10];
        param[0] = 11;
        if (band > 6) {
            band = 6;
        }
        param[1] = band;
        checkStatus(getParameter(param, value));
        return new Float(new String(value)).floatValue();
    }

    public float[] getEarsCompensationAllEQRight() throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        float[] eq_value = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        for (int index = 0; index < 6; index++) {
            eq_value[index] = getEarsCompensationEQRight(index + 1);
        }
        return eq_value;
    }

    public void setMovieMode(int mode) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        checkStatus(setParameter(7, mode));
    }

    public int getMovieMode() throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        int[] value = new int[1];
        checkStatus(getParameter(7, value));
        return value[0];
    }

    public void setHifiMode(int mode) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
        checkStatus(setParameter(8, mode));
    }
}
