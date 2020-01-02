package android.graphics.drawable;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.util.AttributeSet;
import android.view.InflateException;
import com.miui.internal.search.Function;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class DrawableInflater {
    private static final HashMap<String, Constructor<? extends Drawable>> CONSTRUCTOR_MAP = new HashMap();
    @UnsupportedAppUsage
    private final ClassLoader mClassLoader;
    private final Resources mRes;

    public static Drawable loadDrawable(Context context, int id) {
        return loadDrawable(context.getResources(), context.getTheme(), id);
    }

    public static Drawable loadDrawable(Resources resources, Theme theme, int id) {
        return resources.getDrawable(id, theme);
    }

    public DrawableInflater(Resources res, ClassLoader classLoader) {
        this.mRes = res;
        this.mClassLoader = classLoader;
    }

    public Drawable inflateFromXml(String name, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        return inflateFromXmlForDensity(name, parser, attrs, 0, theme);
    }

    /* Access modifiers changed, original: 0000 */
    public Drawable inflateFromXmlForDensity(String name, XmlPullParser parser, AttributeSet attrs, int density, Theme theme) throws XmlPullParserException, IOException {
        if (name.equals("drawable")) {
            name = attrs.getAttributeValue(null, Function.CLASS);
            if (name == null) {
                throw new InflateException("<drawable> tag must specify class attribute");
            }
        }
        Drawable drawable = inflateFromTag(name);
        if (drawable == null) {
            drawable = inflateFromClass(name);
        }
        drawable.setSrcDensityOverride(density);
        drawable.inflate(this.mRes, parser, attrs, theme);
        return drawable;
    }

    private android.graphics.drawable.Drawable inflateFromTag(java.lang.String r2) {
        /*
        r1 = this;
        r0 = r2.hashCode();
        switch(r0) {
            case -2024464016: goto L_0x00e4;
            case -1724158635: goto L_0x00d9;
            case -1671889043: goto L_0x00ce;
            case -1493546681: goto L_0x00c3;
            case -1388777169: goto L_0x00b8;
            case -930826704: goto L_0x00ad;
            case -925180581: goto L_0x00a1;
            case -820387517: goto L_0x0095;
            case -510364471: goto L_0x008b;
            case -94197862: goto L_0x0081;
            case 3056464: goto L_0x0075;
            case 94842723: goto L_0x006a;
            case 100360477: goto L_0x005e;
            case 109250890: goto L_0x0051;
            case 109399969: goto L_0x0044;
            case 160680263: goto L_0x0039;
            case 1191572447: goto L_0x002d;
            case 1442046129: goto L_0x0021;
            case 2013827269: goto L_0x0015;
            case 2118620333: goto L_0x0009;
            default: goto L_0x0007;
        };
    L_0x0007:
        goto L_0x00ee;
    L_0x0009:
        r0 = "animated-vector";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x0011:
        r0 = 10;
        goto L_0x00ef;
    L_0x0015:
        r0 = "animated-rotate";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x001d:
        r0 = 14;
        goto L_0x00ef;
    L_0x0021:
        r0 = "animated-image";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x0029:
        r0 = 19;
        goto L_0x00ef;
    L_0x002d:
        r0 = "selector";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x0036:
        r0 = 0;
        goto L_0x00ef;
    L_0x0039:
        r0 = "level-list";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x0041:
        r0 = 2;
        goto L_0x00ef;
    L_0x0044:
        r0 = "shape";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x004d:
        r0 = 8;
        goto L_0x00ef;
    L_0x0051:
        r0 = "scale";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x005a:
        r0 = 11;
        goto L_0x00ef;
    L_0x005e:
        r0 = "inset";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x0066:
        r0 = 16;
        goto L_0x00ef;
    L_0x006a:
        r0 = "color";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x0072:
        r0 = 7;
        goto L_0x00ef;
    L_0x0075:
        r0 = "clip";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x007d:
        r0 = 12;
        goto L_0x00ef;
    L_0x0081:
        r0 = "layer-list";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x0089:
        r0 = 3;
        goto L_0x00ef;
    L_0x008b:
        r0 = "animated-selector";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x0093:
        r0 = 1;
        goto L_0x00ef;
    L_0x0095:
        r0 = "vector";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x009e:
        r0 = 9;
        goto L_0x00ef;
    L_0x00a1:
        r0 = "rotate";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x00aa:
        r0 = 13;
        goto L_0x00ef;
    L_0x00ad:
        r0 = "ripple";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x00b6:
        r0 = 5;
        goto L_0x00ef;
    L_0x00b8:
        r0 = "bitmap";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x00c0:
        r0 = 17;
        goto L_0x00ef;
    L_0x00c3:
        r0 = "animation-list";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x00cb:
        r0 = 15;
        goto L_0x00ef;
    L_0x00ce:
        r0 = "nine-patch";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x00d6:
        r0 = 18;
        goto L_0x00ef;
    L_0x00d9:
        r0 = "transition";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x00e2:
        r0 = 4;
        goto L_0x00ef;
    L_0x00e4:
        r0 = "adaptive-icon";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0007;
    L_0x00ec:
        r0 = 6;
        goto L_0x00ef;
    L_0x00ee:
        r0 = -1;
    L_0x00ef:
        switch(r0) {
            case 0: goto L_0x0166;
            case 1: goto L_0x0160;
            case 2: goto L_0x015a;
            case 3: goto L_0x0154;
            case 4: goto L_0x014e;
            case 5: goto L_0x0148;
            case 6: goto L_0x0142;
            case 7: goto L_0x013c;
            case 8: goto L_0x0136;
            case 9: goto L_0x0130;
            case 10: goto L_0x012a;
            case 11: goto L_0x0124;
            case 12: goto L_0x011e;
            case 13: goto L_0x0118;
            case 14: goto L_0x0112;
            case 15: goto L_0x010c;
            case 16: goto L_0x0106;
            case 17: goto L_0x0100;
            case 18: goto L_0x00fa;
            case 19: goto L_0x00f4;
            default: goto L_0x00f2;
        };
    L_0x00f2:
        r0 = 0;
        return r0;
    L_0x00f4:
        r0 = new android.graphics.drawable.AnimatedImageDrawable;
        r0.<init>();
        return r0;
    L_0x00fa:
        r0 = new android.graphics.drawable.NinePatchDrawable;
        r0.<init>();
        return r0;
    L_0x0100:
        r0 = new android.graphics.drawable.BitmapDrawable;
        r0.<init>();
        return r0;
    L_0x0106:
        r0 = new android.graphics.drawable.InsetDrawable;
        r0.<init>();
        return r0;
    L_0x010c:
        r0 = new android.graphics.drawable.AnimationDrawable;
        r0.<init>();
        return r0;
    L_0x0112:
        r0 = new android.graphics.drawable.AnimatedRotateDrawable;
        r0.<init>();
        return r0;
    L_0x0118:
        r0 = new android.graphics.drawable.RotateDrawable;
        r0.<init>();
        return r0;
    L_0x011e:
        r0 = new android.graphics.drawable.ClipDrawable;
        r0.<init>();
        return r0;
    L_0x0124:
        r0 = new android.graphics.drawable.ScaleDrawable;
        r0.<init>();
        return r0;
    L_0x012a:
        r0 = new android.graphics.drawable.AnimatedVectorDrawable;
        r0.<init>();
        return r0;
    L_0x0130:
        r0 = new android.graphics.drawable.VectorDrawable;
        r0.<init>();
        return r0;
    L_0x0136:
        r0 = new android.graphics.drawable.GradientDrawable;
        r0.<init>();
        return r0;
    L_0x013c:
        r0 = new android.graphics.drawable.ColorDrawable;
        r0.<init>();
        return r0;
    L_0x0142:
        r0 = new android.graphics.drawable.AdaptiveIconDrawable;
        r0.<init>();
        return r0;
    L_0x0148:
        r0 = new android.graphics.drawable.RippleDrawable;
        r0.<init>();
        return r0;
    L_0x014e:
        r0 = new android.graphics.drawable.TransitionDrawable;
        r0.<init>();
        return r0;
    L_0x0154:
        r0 = new android.graphics.drawable.LayerDrawable;
        r0.<init>();
        return r0;
    L_0x015a:
        r0 = new android.graphics.drawable.LevelListDrawable;
        r0.<init>();
        return r0;
    L_0x0160:
        r0 = new android.graphics.drawable.AnimatedStateListDrawable;
        r0.<init>();
        return r0;
    L_0x0166:
        r0 = new android.graphics.drawable.StateListDrawable;
        r0.<init>();
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.drawable.DrawableInflater.inflateFromTag(java.lang.String):android.graphics.drawable.Drawable");
    }

    private Drawable inflateFromClass(String className) {
        StringBuilder stringBuilder;
        InflateException ie;
        try {
            Constructor<? extends Drawable> constructor;
            synchronized (CONSTRUCTOR_MAP) {
                constructor = (Constructor) CONSTRUCTOR_MAP.get(className);
                if (constructor == null) {
                    constructor = this.mClassLoader.loadClass(className).asSubclass(Drawable.class).getConstructor(new Class[0]);
                    CONSTRUCTOR_MAP.put(className, constructor);
                }
            }
            return (Drawable) constructor.newInstance(new Object[0]);
        } catch (NoSuchMethodException e) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Error inflating class ");
            stringBuilder.append(className);
            ie = new InflateException(stringBuilder.toString());
            ie.initCause(e);
            throw ie;
        } catch (ClassCastException e2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Class is not a Drawable ");
            stringBuilder.append(className);
            ie = new InflateException(stringBuilder.toString());
            ie.initCause(e2);
            throw ie;
        } catch (ClassNotFoundException e3) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Class not found ");
            stringBuilder.append(className);
            ie = new InflateException(stringBuilder.toString());
            ie.initCause(e3);
            throw ie;
        } catch (Exception e4) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Error inflating class ");
            stringBuilder.append(className);
            ie = new InflateException(stringBuilder.toString());
            ie.initCause(e4);
            throw ie;
        }
    }
}
