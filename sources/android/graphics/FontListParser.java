package android.graphics;

import android.annotation.UnsupportedAppUsage;
import android.graphics.fonts.FontVariationAxis;
import android.media.tv.TvContract.PreviewPrograms;
import android.security.KeyChain;
import android.speech.tts.TextToSpeech.Engine;
import android.text.FontConfig;
import android.text.FontConfig.Alias;
import android.text.FontConfig.Family;
import android.text.FontConfig.Font;
import android.util.Xml;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class FontListParser {
    private static final Pattern FILENAME_WHITESPACE_PATTERN = Pattern.compile("^[ \\n\\r\\t]+|[ \\n\\r\\t]+$");

    @UnsupportedAppUsage
    public static FontConfig parse(InputStream in) throws XmlPullParserException, IOException {
        return parse(in, "/system/fonts");
    }

    public static FontConfig parse(InputStream in, String fontDir) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(in, null);
            parser.nextTag();
            FontConfig readFamilies = readFamilies(parser, fontDir);
            return readFamilies;
        } finally {
            in.close();
        }
    }

    private static FontConfig readFamilies(XmlPullParser parser, String fontDir) throws XmlPullParserException, IOException {
        List<Family> families = new ArrayList();
        List<Alias> aliases = new ArrayList();
        parser.require(2, null, "familyset");
        while (parser.next() != 3) {
            if (parser.getEventType() == 2) {
                String tag = parser.getName();
                if (tag.equals("family")) {
                    families.add(readFamily(parser, fontDir));
                } else if (tag.equals(KeyChain.EXTRA_ALIAS)) {
                    aliases.add(readAlias(parser));
                } else {
                    skip(parser);
                }
            }
        }
        return new FontConfig((Family[]) families.toArray(new Family[families.size()]), (Alias[]) aliases.toArray(new Alias[aliases.size()]));
    }

    public static Family readFamily(XmlPullParser parser, String fontDir) throws XmlPullParserException, IOException {
        String name = parser.getAttributeValue(null, "name");
        String lang = parser.getAttributeValue("", "lang");
        String variant = parser.getAttributeValue(null, Engine.KEY_PARAM_VARIANT);
        List<Font> fonts = new ArrayList();
        while (parser.next() != 3) {
            if (parser.getEventType() == 2) {
                if (parser.getName().equals("font")) {
                    fonts.add(readFont(parser, fontDir));
                } else {
                    skip(parser);
                }
            }
        }
        int intVariant = 0;
        if (variant != null) {
            if (variant.equals("compact")) {
                intVariant = 1;
            } else if (variant.equals("elegant")) {
                intVariant = 2;
            }
        }
        return new Family(name, (Font[]) fonts.toArray(new Font[fonts.size()]), lang, intVariant);
    }

    private static Font readFont(XmlPullParser parser, String fontDir) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser = parser;
        String indexStr = xmlPullParser.getAttributeValue(null, "index");
        int index = indexStr == null ? 0 : Integer.parseInt(indexStr);
        List<FontVariationAxis> axes = new ArrayList();
        String weightStr = xmlPullParser.getAttributeValue(null, PreviewPrograms.COLUMN_WEIGHT);
        int weight = weightStr == null ? 400 : Integer.parseInt(weightStr);
        boolean isItalic = "italic".equals(xmlPullParser.getAttributeValue(null, TtmlUtils.TAG_STYLE));
        String fallbackFor = xmlPullParser.getAttributeValue(null, "fallbackFor");
        StringBuilder filename = new StringBuilder();
        while (parser.next() != 3) {
            if (parser.getEventType() == 4) {
                filename.append(parser.getText());
            }
            if (parser.getEventType() == 2) {
                if (parser.getName().equals("axis")) {
                    axes.add(readAxis(parser));
                } else {
                    skip(parser);
                }
            }
        }
        String sanitizedName = FILENAME_WHITESPACE_PATTERN.matcher(filename).replaceAll("");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(fontDir);
        stringBuilder.append(sanitizedName);
        return new Font(stringBuilder.toString(), index, (FontVariationAxis[]) axes.toArray(new FontVariationAxis[axes.size()]), weight, isItalic, fallbackFor);
    }

    private static FontVariationAxis readAxis(XmlPullParser parser) throws XmlPullParserException, IOException {
        String tagStr = parser.getAttributeValue(null, "tag");
        String styleValueStr = parser.getAttributeValue(null, "stylevalue");
        skip(parser);
        return new FontVariationAxis(tagStr, Float.parseFloat(styleValueStr));
    }

    public static Alias readAlias(XmlPullParser parser) throws XmlPullParserException, IOException {
        int weight;
        String name = parser.getAttributeValue(null, "name");
        String toName = parser.getAttributeValue(null, "to");
        String weightStr = parser.getAttributeValue(null, PreviewPrograms.COLUMN_WEIGHT);
        if (weightStr == null) {
            weight = 400;
        } else {
            weight = Integer.parseInt(weightStr);
        }
        skip(parser);
        return new Alias(name, toName, weight);
    }

    public static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        int depth = 1;
        while (depth > 0) {
            int next = parser.next();
            if (next == 2) {
                depth++;
            } else if (next == 3) {
                depth--;
            }
        }
    }
}
