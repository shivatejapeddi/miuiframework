package android.view.animation;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.content.res.XmlResourceParser;
import android.os.BatteryManager;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Xml;
import com.android.internal.R;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimationUtils {
    private static final int SEQUENTIALLY = 1;
    private static final int TOGETHER = 0;
    private static ThreadLocal<AnimationState> sAnimationState = new ThreadLocal<AnimationState>() {
        /* Access modifiers changed, original: protected */
        public AnimationState initialValue() {
            return new AnimationState();
        }
    };

    private static class AnimationState {
        boolean animationClockLocked;
        long currentVsyncTimeMillis;
        long lastReportedTimeMillis;

        private AnimationState() {
        }

        /* synthetic */ AnimationState(AnonymousClass1 x0) {
            this();
        }
    }

    public static void lockAnimationClock(long vsyncMillis) {
        AnimationState state = (AnimationState) sAnimationState.get();
        state.animationClockLocked = true;
        state.currentVsyncTimeMillis = vsyncMillis;
    }

    public static void unlockAnimationClock() {
        ((AnimationState) sAnimationState.get()).animationClockLocked = false;
    }

    public static long currentAnimationTimeMillis() {
        AnimationState state = (AnimationState) sAnimationState.get();
        if (state.animationClockLocked) {
            return Math.max(state.currentVsyncTimeMillis, state.lastReportedTimeMillis);
        }
        state.lastReportedTimeMillis = SystemClock.uptimeMillis();
        return state.lastReportedTimeMillis;
    }

    public static Animation loadAnimation(Context context, int id) throws NotFoundException {
        StringBuilder stringBuilder;
        NotFoundException rnf;
        String str = "Can't load animation resource ID #0x";
        XmlResourceParser parser = null;
        try {
            parser = context.getResources().getAnimation(id);
            str = createAnimationFromXml(context, parser);
            if (parser != null) {
                parser.close();
            }
            return str;
        } catch (XmlPullParserException ex) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(Integer.toHexString(id));
            rnf = new NotFoundException(stringBuilder.toString());
            rnf.initCause(ex);
            throw rnf;
        } catch (IOException ex2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(Integer.toHexString(id));
            rnf = new NotFoundException(stringBuilder.toString());
            rnf.initCause(ex2);
            throw rnf;
        } catch (Throwable th) {
            if (parser != null) {
                parser.close();
            }
        }
    }

    private static Animation createAnimationFromXml(Context c, XmlPullParser parser) throws XmlPullParserException, IOException {
        return createAnimationFromXml(c, parser, null, Xml.asAttributeSet(parser));
    }

    private static Animation createAnimationFromXml(Context c, XmlPullParser parser, AnimationSet parent, AttributeSet attrs) throws XmlPullParserException, IOException {
        Animation anim = null;
        int depth = parser.getDepth();
        while (true) {
            int next = parser.next();
            int type = next;
            if ((next != 3 || parser.getDepth() > depth) && type != 1) {
                if (type == 2) {
                    String name = parser.getName();
                    if (name.equals("set")) {
                        anim = new AnimationSet(c, attrs);
                        createAnimationFromXml(c, parser, (AnimationSet) anim, attrs);
                    } else if (name.equals("alpha")) {
                        anim = new AlphaAnimation(c, attrs);
                    } else if (name.equals(BatteryManager.EXTRA_SCALE)) {
                        anim = new ScaleAnimation(c, attrs);
                    } else if (name.equals("rotate")) {
                        anim = new RotateAnimation(c, attrs);
                    } else if (name.equals("translate")) {
                        anim = new TranslateAnimation(c, attrs);
                    } else if (name.equals("cliprect")) {
                        anim = new ClipRectAnimation(c, attrs);
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unknown animation name: ");
                        stringBuilder.append(parser.getName());
                        throw new RuntimeException(stringBuilder.toString());
                    }
                    if (parent != null) {
                        parent.addAnimation(anim);
                    }
                }
            }
        }
        return anim;
    }

    public static LayoutAnimationController loadLayoutAnimation(Context context, int id) throws NotFoundException {
        StringBuilder stringBuilder;
        NotFoundException rnf;
        String str = "Can't load animation resource ID #0x";
        XmlResourceParser parser = null;
        try {
            parser = context.getResources().getAnimation(id);
            str = createLayoutAnimationFromXml(context, parser);
            if (parser != null) {
                parser.close();
            }
            return str;
        } catch (XmlPullParserException ex) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(Integer.toHexString(id));
            rnf = new NotFoundException(stringBuilder.toString());
            rnf.initCause(ex);
            throw rnf;
        } catch (IOException ex2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(Integer.toHexString(id));
            rnf = new NotFoundException(stringBuilder.toString());
            rnf.initCause(ex2);
            throw rnf;
        } catch (Throwable th) {
            if (parser != null) {
                parser.close();
            }
        }
    }

    private static LayoutAnimationController createLayoutAnimationFromXml(Context c, XmlPullParser parser) throws XmlPullParserException, IOException {
        return createLayoutAnimationFromXml(c, parser, Xml.asAttributeSet(parser));
    }

    private static LayoutAnimationController createLayoutAnimationFromXml(Context c, XmlPullParser parser, AttributeSet attrs) throws XmlPullParserException, IOException {
        LayoutAnimationController controller = null;
        int depth = parser.getDepth();
        while (true) {
            int next = parser.next();
            int type = next;
            if ((next != 3 || parser.getDepth() > depth) && type != 1) {
                if (type == 2) {
                    LayoutAnimationController layoutAnimationController;
                    String name = parser.getName();
                    if ("layoutAnimation".equals(name)) {
                        layoutAnimationController = new LayoutAnimationController(c, attrs);
                    } else if ("gridLayoutAnimation".equals(name)) {
                        layoutAnimationController = new GridLayoutAnimationController(c, attrs);
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unknown layout animation name: ");
                        stringBuilder.append(name);
                        throw new RuntimeException(stringBuilder.toString());
                    }
                    controller = layoutAnimationController;
                }
            }
        }
        return controller;
    }

    public static Animation makeInAnimation(Context c, boolean fromLeft) {
        Animation a;
        if (fromLeft) {
            a = loadAnimation(c, 17432578);
        } else {
            a = loadAnimation(c, R.anim.slide_in_right);
        }
        a.setInterpolator(new DecelerateInterpolator());
        a.setStartTime(currentAnimationTimeMillis());
        return a;
    }

    public static Animation makeOutAnimation(Context c, boolean toRight) {
        Animation a;
        if (toRight) {
            a = loadAnimation(c, 17432579);
        } else {
            a = loadAnimation(c, R.anim.slide_out_left);
        }
        a.setInterpolator(new AccelerateInterpolator());
        a.setStartTime(currentAnimationTimeMillis());
        return a;
    }

    public static Animation makeInChildBottomAnimation(Context c) {
        Animation a = loadAnimation(c, R.anim.slide_in_child_bottom);
        a.setInterpolator(new AccelerateInterpolator());
        a.setStartTime(currentAnimationTimeMillis());
        return a;
    }

    public static Interpolator loadInterpolator(Context context, int id) throws NotFoundException {
        StringBuilder stringBuilder;
        NotFoundException rnf;
        String str = "Can't load animation resource ID #0x";
        XmlResourceParser parser = null;
        try {
            parser = context.getResources().getAnimation(id);
            str = createInterpolatorFromXml(context.getResources(), context.getTheme(), parser);
            if (parser != null) {
                parser.close();
            }
            return str;
        } catch (XmlPullParserException ex) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(Integer.toHexString(id));
            rnf = new NotFoundException(stringBuilder.toString());
            rnf.initCause(ex);
            throw rnf;
        } catch (IOException ex2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(Integer.toHexString(id));
            rnf = new NotFoundException(stringBuilder.toString());
            rnf.initCause(ex2);
            throw rnf;
        } catch (Throwable th) {
            if (parser != null) {
                parser.close();
            }
        }
    }

    public static Interpolator loadInterpolator(Resources res, Theme theme, int id) throws NotFoundException {
        StringBuilder stringBuilder;
        NotFoundException rnf;
        String str = "Can't load animation resource ID #0x";
        XmlResourceParser parser = null;
        try {
            parser = res.getAnimation(id);
            str = createInterpolatorFromXml(res, theme, parser);
            if (parser != null) {
                parser.close();
            }
            return str;
        } catch (XmlPullParserException ex) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(Integer.toHexString(id));
            rnf = new NotFoundException(stringBuilder.toString());
            rnf.initCause(ex);
            throw rnf;
        } catch (IOException ex2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(Integer.toHexString(id));
            rnf = new NotFoundException(stringBuilder.toString());
            rnf.initCause(ex2);
            throw rnf;
        } catch (Throwable th) {
            if (parser != null) {
                parser.close();
            }
        }
    }

    private static Interpolator createInterpolatorFromXml(Resources res, Theme theme, XmlPullParser parser) throws XmlPullParserException, IOException {
        BaseInterpolator interpolator = null;
        int depth = parser.getDepth();
        while (true) {
            int next = parser.next();
            int type = next;
            if ((next != 3 || parser.getDepth() > depth) && type != 1) {
                if (type == 2) {
                    BaseInterpolator linearInterpolator;
                    AttributeSet attrs = Xml.asAttributeSet(parser);
                    String name = parser.getName();
                    if (name.equals("linearInterpolator")) {
                        linearInterpolator = new LinearInterpolator();
                    } else if (name.equals("accelerateInterpolator")) {
                        linearInterpolator = new AccelerateInterpolator(res, theme, attrs);
                    } else if (name.equals("decelerateInterpolator")) {
                        linearInterpolator = new DecelerateInterpolator(res, theme, attrs);
                    } else if (name.equals("accelerateDecelerateInterpolator")) {
                        linearInterpolator = new AccelerateDecelerateInterpolator();
                    } else if (name.equals("cycleInterpolator")) {
                        linearInterpolator = new CycleInterpolator(res, theme, attrs);
                    } else if (name.equals("anticipateInterpolator")) {
                        linearInterpolator = new AnticipateInterpolator(res, theme, attrs);
                    } else if (name.equals("overshootInterpolator")) {
                        linearInterpolator = new OvershootInterpolator(res, theme, attrs);
                    } else if (name.equals("anticipateOvershootInterpolator")) {
                        linearInterpolator = new AnticipateOvershootInterpolator(res, theme, attrs);
                    } else if (name.equals("bounceInterpolator")) {
                        linearInterpolator = new BounceInterpolator();
                    } else if (name.equals("pathInterpolator")) {
                        linearInterpolator = new PathInterpolator(res, theme, attrs);
                    } else if (name.equals("springInterpolator")) {
                        linearInterpolator = new SpringInterpolator(res, theme, attrs);
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unknown interpolator name: ");
                        stringBuilder.append(parser.getName());
                        throw new RuntimeException(stringBuilder.toString());
                    }
                    interpolator = linearInterpolator;
                }
            }
        }
        return interpolator;
    }
}
