package com.android.internal.colorextraction.types;

import android.app.WallpaperColors;
import android.app.slice.Slice;
import android.content.Context;
import android.graphics.Color;
import android.miui.BiometricConnect;
import android.net.wifi.WifiEnterpriseConfig;
import android.util.Log;
import android.util.MathUtils;
import android.util.Range;
import com.android.internal.R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.colorextraction.ColorExtractor.GradientColors;
import com.android.internal.graphics.ColorUtils;
import com.miui.mishare.RemoteDevice;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class Tonal implements ExtractionType {
    private static final boolean DEBUG = true;
    private static final float FIT_WEIGHT_H = 1.0f;
    private static final float FIT_WEIGHT_L = 10.0f;
    private static final float FIT_WEIGHT_S = 1.0f;
    public static final int MAIN_COLOR_DARK = -14671580;
    public static final int MAIN_COLOR_LIGHT = -2433824;
    public static final int MAIN_COLOR_REGULAR = -16777216;
    private static final String TAG = "Tonal";
    private final Context mContext;
    private final TonalPalette mGreyPalette;
    private float[] mTmpHSL = new float[3];
    private final ArrayList<TonalPalette> mTonalPalettes;

    @VisibleForTesting
    public static class ColorRange {
        private Range<Float> mHue;
        private Range<Float> mLightness;
        private Range<Float> mSaturation;

        public ColorRange(Range<Float> hue, Range<Float> saturation, Range<Float> lightness) {
            this.mHue = hue;
            this.mSaturation = saturation;
            this.mLightness = lightness;
        }

        public boolean containsColor(float h, float s, float l) {
            if (this.mHue.contains(Float.valueOf(h)) && this.mSaturation.contains(Float.valueOf(s)) && this.mLightness.contains(Float.valueOf(l))) {
                return true;
            }
            return false;
        }

        public float[] getCenter() {
            return new float[]{((Float) this.mHue.getLower()).floatValue() + ((((Float) this.mHue.getUpper()).floatValue() - ((Float) this.mHue.getLower()).floatValue()) / 2.0f), ((Float) this.mSaturation.getLower()).floatValue() + ((((Float) this.mSaturation.getUpper()).floatValue() - ((Float) this.mSaturation.getLower()).floatValue()) / 2.0f), ((Float) this.mLightness.getLower()).floatValue() + ((((Float) this.mLightness.getUpper()).floatValue() - ((Float) this.mLightness.getLower()).floatValue()) / 2.0f)};
        }

        public String toString() {
            return String.format("H: %s, S: %s, L %s", new Object[]{this.mHue, this.mSaturation, this.mLightness});
        }
    }

    @VisibleForTesting
    public static class ConfigParser {
        private final ArrayList<TonalPalette> mTonalPalettes = new ArrayList();

        public ConfigParser(Context context) {
            try {
                XmlPullParser parser = context.getResources().getXml(R.xml.color_extraction);
                for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
                    if (eventType != 0) {
                        if (eventType != 3) {
                            if (eventType != 2) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("Invalid XML event ");
                                stringBuilder.append(eventType);
                                stringBuilder.append(" - ");
                                stringBuilder.append(parser.getName());
                                throw new XmlPullParserException(stringBuilder.toString(), parser, null);
                            } else if (parser.getName().equals("palettes")) {
                                parsePalettes(parser);
                            }
                        }
                    }
                }
            } catch (IOException | XmlPullParserException e) {
                throw new RuntimeException(e);
            }
        }

        public ArrayList<TonalPalette> getTonalPalettes() {
            return this.mTonalPalettes;
        }

        private ColorRange readRange(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(2, null, Slice.SUBTYPE_RANGE);
            float[] h = readFloatArray(parser.getAttributeValue(null, BiometricConnect.MSG_CB_BUNDLE_DEPTHMAP_H));
            float[] s = readFloatArray(parser.getAttributeValue(null, RemoteDevice.KEY_STATUS));
            float[] l = readFloatArray(parser.getAttributeValue(null, "l"));
            if (h != null && s != null && l != null) {
                return new ColorRange(new Range(Float.valueOf(h[0]), Float.valueOf(h[1])), new Range(Float.valueOf(s[0]), Float.valueOf(s[1])), new Range(Float.valueOf(l[0]), Float.valueOf(l[1])));
            }
            throw new XmlPullParserException("Incomplete range tag.", parser, null);
        }

        private void parsePalettes(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(2, null, "palettes");
            while (parser.next() != 3) {
                if (parser.getEventType() == 2) {
                    String name = parser.getName();
                    if (name.equals("palette")) {
                        this.mTonalPalettes.add(readPalette(parser));
                        parser.next();
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Invalid tag: ");
                        stringBuilder.append(name);
                        throw new XmlPullParserException(stringBuilder.toString());
                    }
                }
            }
        }

        private TonalPalette readPalette(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(2, null, "palette");
            float[] h = readFloatArray(parser.getAttributeValue(null, BiometricConnect.MSG_CB_BUNDLE_DEPTHMAP_H));
            float[] s = readFloatArray(parser.getAttributeValue(null, RemoteDevice.KEY_STATUS));
            float[] l = readFloatArray(parser.getAttributeValue(null, "l"));
            if (h != null && s != null && l != null) {
                return new TonalPalette(h, s, l);
            }
            throw new XmlPullParserException("Incomplete range tag.", parser, null);
        }

        private float[] readFloatArray(String attributeValue) throws IOException, XmlPullParserException {
            String str = "";
            String[] tokens = attributeValue.replaceAll(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER, str).replaceAll("\n", str).split(",");
            float[] numbers = new float[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                numbers[i] = Float.parseFloat(tokens[i]);
            }
            return numbers;
        }
    }

    @VisibleForTesting
    public static class TonalPalette {
        public final float[] h;
        public final float[] l;
        public final float maxHue;
        public final float minHue;
        public final float[] s;

        TonalPalette(float[] h, float[] s, float[] l) {
            if (h.length == s.length && s.length == l.length) {
                this.h = h;
                this.s = s;
                this.l = l;
                float minHue = Float.POSITIVE_INFINITY;
                float maxHue = Float.NEGATIVE_INFINITY;
                for (float v : h) {
                    minHue = Math.min(v, minHue);
                    maxHue = Math.max(v, maxHue);
                }
                this.minHue = minHue;
                this.maxHue = maxHue;
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("All arrays should have the same size. h: ");
            stringBuilder.append(Arrays.toString(h));
            stringBuilder.append(" s: ");
            stringBuilder.append(Arrays.toString(s));
            stringBuilder.append(" l: ");
            stringBuilder.append(Arrays.toString(l));
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public Tonal(Context context) {
        this.mTonalPalettes = new ConfigParser(context).getTonalPalettes();
        this.mContext = context;
        this.mGreyPalette = (TonalPalette) this.mTonalPalettes.get(0);
        this.mTonalPalettes.remove(0);
    }

    public void extractInto(WallpaperColors inWallpaperColors, GradientColors outColorsNormal, GradientColors outColorsDark, GradientColors outColorsExtraDark) {
        if (!runTonalExtraction(inWallpaperColors, outColorsNormal, outColorsDark, outColorsExtraDark)) {
            applyFallback(inWallpaperColors, outColorsNormal, outColorsDark, outColorsExtraDark);
        }
    }

    private boolean runTonalExtraction(WallpaperColors inWallpaperColors, GradientColors outColorsNormal, GradientColors outColorsDark, GradientColors outColorsExtraDark) {
        GradientColors gradientColors = outColorsNormal;
        GradientColors gradientColors2 = outColorsDark;
        GradientColors gradientColors3 = outColorsExtraDark;
        if (inWallpaperColors == null) {
            return false;
        }
        List<Color> mainColors = inWallpaperColors.getMainColors();
        int mainColorsSize = mainColors.size();
        int hints = inWallpaperColors.getColorHints();
        boolean supportsDarkText = (hints & 1) != 0;
        if (mainColorsSize == 0) {
            return false;
        }
        Color bestColor = (Color) mainColors.get(0);
        int colorValue = bestColor.toArgb();
        float[] hsl = new float[3];
        ColorUtils.RGBToHSL(Color.red(colorValue), Color.green(colorValue), Color.blue(colorValue), hsl);
        hsl[0] = hsl[0] / 360.0f;
        TonalPalette palette = findTonalPalette(hsl[0], hsl[1]);
        String str = TAG;
        if (palette == null) {
            Log.w(str, "Could not find a tonal palette!");
            return false;
        }
        int fitIndex = bestFit(palette, hsl[0], hsl[1], hsl[2]);
        if (fitIndex == -1) {
            Log.w(str, "Could not find best fit!");
            return false;
        }
        float[] h = fit(palette.h, hsl[0], fitIndex, -8388608, Float.POSITIVE_INFINITY);
        float[] s = fit(palette.s, hsl[1], fitIndex, 0.0f, 1.0f);
        float[] l = fit(palette.l, hsl[2], fitIndex, 0.0f, 1.0f);
        int[] colorPalette = getColorPalette(h, s, l);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Tonal Palette - index: ");
        stringBuilder.append(fitIndex);
        stringBuilder.append(". Main color: ");
        stringBuilder.append(Integer.toHexString(getColorInt(fitIndex, h, s, l)));
        stringBuilder.append("\nColors: ");
        StringBuilder builder = new StringBuilder(stringBuilder.toString());
        for (colorValue = 0; colorValue < h.length; colorValue++) {
            builder.append(Integer.toHexString(getColorInt(colorValue, h, s, l)));
            if (colorValue < h.length - 1) {
                builder.append(", ");
            }
        }
        Log.d(str, builder.toString());
        int primaryIndex = fitIndex;
        colorValue = getColorInt(primaryIndex, h, s, l);
        ColorUtils.colorToHSL(colorValue, this.mTmpHSL);
        float[] fArr = this.mTmpHSL;
        float mainLuminosity = fArr[2];
        ColorUtils.colorToHSL(MAIN_COLOR_LIGHT, fArr);
        float[] fArr2 = this.mTmpHSL;
        if (mainLuminosity > fArr2[2]) {
            return false;
        }
        ColorUtils.colorToHSL(MAIN_COLOR_DARK, fArr2);
        float darkLuminosity = this.mTmpHSL[2];
        if (mainLuminosity < darkLuminosity) {
            return false;
        }
        int primaryIndex2;
        gradientColors.setMainColor(colorValue);
        gradientColors.setSecondaryColor(colorValue);
        gradientColors.setColorPalette(colorPalette);
        if (supportsDarkText) {
            primaryIndex2 = h.length - 1;
        } else if (fitIndex < 2) {
            primaryIndex2 = 0;
        } else {
            primaryIndex2 = Math.min(fitIndex, 3);
        }
        colorValue = getColorInt(primaryIndex2, h, s, l);
        gradientColors2.setMainColor(colorValue);
        gradientColors2.setSecondaryColor(colorValue);
        gradientColors2.setColorPalette(colorPalette);
        if (supportsDarkText) {
            primaryIndex = h.length - 1;
        } else {
            if (fitIndex < 2) {
                primaryIndex = 0;
            } else {
                primaryIndex = 2;
            }
        }
        colorValue = getColorInt(primaryIndex, h, s, l);
        gradientColors3.setMainColor(colorValue);
        gradientColors3.setSecondaryColor(colorValue);
        gradientColors3.setColorPalette(colorPalette);
        gradientColors.setSupportsDarkText(supportsDarkText);
        gradientColors2.setSupportsDarkText(supportsDarkText);
        gradientColors3.setSupportsDarkText(supportsDarkText);
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Gradients: \n\tNormal ");
        stringBuilder2.append(gradientColors);
        stringBuilder2.append("\n\tDark ");
        stringBuilder2.append(gradientColors2);
        stringBuilder2.append("\n\tExtra dark: ");
        stringBuilder2.append(gradientColors3);
        Log.d(str, stringBuilder2.toString());
        return true;
    }

    private void applyFallback(WallpaperColors inWallpaperColors, GradientColors outColorsNormal, GradientColors outColorsDark, GradientColors outColorsExtraDark) {
        applyFallback(inWallpaperColors, outColorsNormal);
        applyFallback(inWallpaperColors, outColorsDark);
        applyFallback(inWallpaperColors, outColorsExtraDark);
    }

    public void applyFallback(WallpaperColors inWallpaperColors, GradientColors outGradientColors) {
        int color;
        boolean light = (inWallpaperColors == null || (inWallpaperColors.getColorHints() & 1) == 0) ? false : true;
        boolean dark = (inWallpaperColors == null || (inWallpaperColors.getColorHints() & 2) == 0) ? false : true;
        boolean inNightMode = (this.mContext.getResources().getConfiguration().uiMode & 48) == 32;
        if (light) {
            color = MAIN_COLOR_LIGHT;
        } else if (dark || inNightMode) {
            color = MAIN_COLOR_DARK;
        } else {
            color = -16777216;
        }
        float[] hsl = new float[3];
        ColorUtils.colorToHSL(color, hsl);
        outGradientColors.setMainColor(color);
        outGradientColors.setSecondaryColor(color);
        outGradientColors.setSupportsDarkText(light);
        outGradientColors.setColorPalette(getColorPalette(findTonalPalette(hsl[0], hsl[1])));
    }

    private int getColorInt(int fitIndex, float[] h, float[] s, float[] l) {
        this.mTmpHSL[0] = fract(h[fitIndex]) * 360.0f;
        float[] fArr = this.mTmpHSL;
        fArr[1] = s[fitIndex];
        fArr[2] = l[fitIndex];
        return ColorUtils.HSLToColor(fArr);
    }

    private int[] getColorPalette(float[] h, float[] s, float[] l) {
        int[] colorPalette = new int[h.length];
        for (int i = 0; i < colorPalette.length; i++) {
            colorPalette[i] = getColorInt(i, h, s, l);
        }
        return colorPalette;
    }

    private int[] getColorPalette(TonalPalette palette) {
        return getColorPalette(palette.h, palette.s, palette.l);
    }

    private static float[] fit(float[] data, float v, int index, float min, float max) {
        float[] fitData = new float[data.length];
        float delta = v - data[index];
        for (int i = 0; i < data.length; i++) {
            fitData[i] = MathUtils.constrain(data[i] + delta, min, max);
        }
        return fitData;
    }

    private static int bestFit(TonalPalette palette, float h, float s, float l) {
        int minErrorIndex = -1;
        float minError = Float.POSITIVE_INFINITY;
        for (int i = 0; i < palette.h.length; i++) {
            float error = ((Math.abs(h - palette.h[i]) * 1.0f) + (Math.abs(s - palette.s[i]) * 1.0f)) + (Math.abs(l - palette.l[i]) * FIT_WEIGHT_L);
            if (error < minError) {
                minError = error;
                minErrorIndex = i;
            }
        }
        return minErrorIndex;
    }

    private TonalPalette findTonalPalette(float h, float s) {
        if (s < 0.05f) {
            return this.mGreyPalette;
        }
        TonalPalette best = null;
        float error = Float.POSITIVE_INFINITY;
        int tonalPalettesCount = this.mTonalPalettes.size();
        int i = 0;
        while (i < tonalPalettesCount) {
            TonalPalette candidate = (TonalPalette) this.mTonalPalettes.get(i);
            if (h < candidate.minHue || h > candidate.maxHue) {
                if (candidate.maxHue <= 1.0f || h < 0.0f || h > fract(candidate.maxHue)) {
                    if (candidate.minHue < 0.0f && h >= fract(candidate.minHue) && h <= 1.0f) {
                        best = candidate;
                        break;
                    }
                    if (h <= candidate.minHue && candidate.minHue - h < error) {
                        best = candidate;
                        error = candidate.minHue - h;
                    } else if (h >= candidate.maxHue && h - candidate.maxHue < error) {
                        best = candidate;
                        error = h - candidate.maxHue;
                    } else if (candidate.maxHue > 1.0f && h >= fract(candidate.maxHue) && h - fract(candidate.maxHue) < error) {
                        best = candidate;
                        error = h - fract(candidate.maxHue);
                    } else if (candidate.minHue < 0.0f && h <= fract(candidate.minHue) && fract(candidate.minHue) - h < error) {
                        best = candidate;
                        error = fract(candidate.minHue) - h;
                    }
                    i++;
                } else {
                    best = candidate;
                    break;
                }
            }
            best = candidate;
            break;
        }
        return best;
    }

    private static float fract(float v) {
        return v - ((float) Math.floor((double) v));
    }
}
