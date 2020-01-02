package android.graphics;

import android.graphics.PorterDuff.Mode;

public enum BlendMode {
    CLEAR(0),
    SRC(1),
    DST(2),
    SRC_OVER(3),
    DST_OVER(4),
    SRC_IN(5),
    DST_IN(6),
    SRC_OUT(7),
    DST_OUT(8),
    SRC_ATOP(9),
    DST_ATOP(10),
    XOR(11),
    PLUS(12),
    MODULATE(13),
    SCREEN(14),
    OVERLAY(15),
    DARKEN(16),
    LIGHTEN(17),
    COLOR_DODGE(18),
    COLOR_BURN(19),
    HARD_LIGHT(20),
    SOFT_LIGHT(21),
    DIFFERENCE(22),
    EXCLUSION(23),
    MULTIPLY(24),
    HUE(25),
    SATURATION(26),
    COLOR(27),
    LUMINOSITY(28);
    
    private static final BlendMode[] BLEND_MODES = null;
    private final Xfermode mXfermode;

    static {
        BLEND_MODES = values();
    }

    public static BlendMode fromValue(int value) {
        for (BlendMode mode : BLEND_MODES) {
            if (mode.mXfermode.porterDuffMode == value) {
                return mode;
            }
        }
        return null;
    }

    public static int toValue(BlendMode mode) {
        return mode.getXfermode().porterDuffMode;
    }

    public static Mode blendModeToPorterDuffMode(BlendMode mode) {
        if (mode == null) {
            return null;
        }
        switch (mode) {
            case CLEAR:
                return Mode.CLEAR;
            case SRC:
                return Mode.SRC;
            case DST:
                return Mode.DST;
            case SRC_OVER:
                return Mode.SRC_OVER;
            case DST_OVER:
                return Mode.DST_OVER;
            case SRC_IN:
                return Mode.SRC_IN;
            case DST_IN:
                return Mode.DST_IN;
            case SRC_OUT:
                return Mode.SRC_OUT;
            case DST_OUT:
                return Mode.DST_OUT;
            case SRC_ATOP:
                return Mode.SRC_ATOP;
            case DST_ATOP:
                return Mode.DST_ATOP;
            case XOR:
                return Mode.XOR;
            case DARKEN:
                return Mode.DARKEN;
            case LIGHTEN:
                return Mode.LIGHTEN;
            case MODULATE:
                return Mode.MULTIPLY;
            case SCREEN:
                return Mode.SCREEN;
            case PLUS:
                return Mode.ADD;
            case OVERLAY:
                return Mode.OVERLAY;
            default:
                return null;
        }
    }

    private BlendMode(int mode) {
        this.mXfermode = new Xfermode();
        this.mXfermode.porterDuffMode = mode;
    }

    public Xfermode getXfermode() {
        return this.mXfermode;
    }
}
