package android.animation;

import android.content.Context;
import android.content.res.ConfigurationBoundResourceCache;
import android.content.res.ConstantState;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Log;
import android.util.PathParser;
import android.util.PathParser.PathData;
import android.util.StateSet;
import android.util.TypedValue;
import android.util.Xml;
import android.view.InflateException;
import android.view.animation.AnimationUtils;
import android.view.animation.BaseInterpolator;
import android.view.animation.Interpolator;
import com.android.ims.ImsConfig;
import com.android.internal.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatorInflater {
    private static final boolean DBG_ANIMATOR_INFLATER = false;
    private static final int SEQUENTIALLY = 1;
    private static final String TAG = "AnimatorInflater";
    private static final int TOGETHER = 0;
    private static final int VALUE_TYPE_COLOR = 3;
    private static final int VALUE_TYPE_FLOAT = 0;
    private static final int VALUE_TYPE_INT = 1;
    private static final int VALUE_TYPE_PATH = 2;
    private static final int VALUE_TYPE_UNDEFINED = 4;
    private static final TypedValue sTmpTypedValue = new TypedValue();

    private static class PathDataEvaluator implements TypeEvaluator<PathData> {
        private final PathData mPathData;

        private PathDataEvaluator() {
            this.mPathData = new PathData();
        }

        public PathData evaluate(float fraction, PathData startPathData, PathData endPathData) {
            if (PathParser.interpolatePathData(this.mPathData, startPathData, endPathData, fraction)) {
                return this.mPathData;
            }
            throw new IllegalArgumentException("Can't interpolate between two incompatible pathData");
        }
    }

    public static Animator loadAnimator(Context context, int id) throws NotFoundException {
        return loadAnimator(context.getResources(), context.getTheme(), id);
    }

    public static Animator loadAnimator(Resources resources, Theme theme, int id) throws NotFoundException {
        return loadAnimator(resources, theme, id, 1.0f);
    }

    public static Animator loadAnimator(Resources resources, Theme theme, int id, float pathErrorScale) throws NotFoundException {
        StringBuilder stringBuilder;
        NotFoundException rnf;
        String str = "Can't load animation resource ID #0x";
        ConfigurationBoundResourceCache<Animator> animatorCache = resources.getAnimatorCache();
        Animator animator = (Animator) animatorCache.getInstance((long) id, resources, theme);
        if (animator != null) {
            return animator;
        }
        XmlResourceParser parser = null;
        try {
            parser = resources.getAnimation(id);
            animator = createAnimatorFromXml(resources, theme, parser, pathErrorScale);
            if (animator != null) {
                animator.appendChangingConfigurations(getChangingConfigs(resources, id));
                ConstantState<Animator> constantState = animator.createConstantState();
                if (constantState != null) {
                    animatorCache.put((long) id, theme, constantState);
                    animator = (Animator) constantState.newInstance(resources, theme);
                }
            }
            if (parser != null) {
                parser.close();
            }
            return animator;
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

    public static StateListAnimator loadStateListAnimator(Context context, int id) throws NotFoundException {
        StringBuilder stringBuilder;
        NotFoundException rnf;
        String str = "Can't load state list animator resource ID #0x";
        Resources resources = context.getResources();
        ConfigurationBoundResourceCache<StateListAnimator> cache = resources.getStateListAnimatorCache();
        Theme theme = context.getTheme();
        StateListAnimator animator = (StateListAnimator) cache.getInstance((long) id, resources, theme);
        if (animator != null) {
            return animator;
        }
        XmlResourceParser parser = null;
        try {
            parser = resources.getAnimation(id);
            animator = createStateListAnimatorFromXml(context, parser, Xml.asAttributeSet(parser));
            if (animator != null) {
                animator.appendChangingConfigurations(getChangingConfigs(resources, id));
                ConstantState<StateListAnimator> constantState = animator.createConstantState();
                if (constantState != null) {
                    cache.put((long) id, theme, constantState);
                    animator = (StateListAnimator) constantState.newInstance(resources, theme);
                }
            }
            if (parser != null) {
                parser.close();
            }
            return animator;
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

    private static StateListAnimator createStateListAnimatorFromXml(Context context, XmlPullParser parser, AttributeSet attributeSet) throws IOException, XmlPullParserException {
        StateListAnimator stateListAnimator = new StateListAnimator();
        while (true) {
            int type = parser.next();
            if (type == 1) {
                break;
            } else if (type != 2) {
                if (type == 3) {
                    break;
                }
            } else {
                Animator animator = null;
                if (ImsConfig.EXTRA_CHANGED_ITEM.equals(parser.getName())) {
                    int attributeCount = parser.getAttributeCount();
                    int[] states = new int[attributeCount];
                    int stateIndex = 0;
                    for (int i = 0; i < attributeCount; i++) {
                        int attrName = attributeSet.getAttributeNameResource(i);
                        if (attrName == 16843213) {
                            animator = loadAnimator(context, attributeSet.getAttributeResourceValue(i, 0));
                        } else {
                            int i2;
                            int stateIndex2 = stateIndex + 1;
                            if (attributeSet.getAttributeBooleanValue(i, false)) {
                                i2 = attrName;
                            } else {
                                i2 = -attrName;
                            }
                            states[stateIndex] = i2;
                            stateIndex = stateIndex2;
                        }
                    }
                    if (animator == null) {
                        animator = createAnimatorFromXml(context.getResources(), context.getTheme(), parser, 1.0f);
                    }
                    if (animator != null) {
                        stateListAnimator.addState(StateSet.trimStateSet(states, stateIndex), animator);
                    } else {
                        throw new NotFoundException("animation state item must have a valid animation");
                    }
                }
                continue;
            }
        }
        return stateListAnimator;
    }

    private static PropertyValuesHolder getPVH(TypedArray styledAttributes, int valueType, int valueFromId, int valueToId, String propertyName) {
        int valueType2;
        PropertyValuesHolder returnValue;
        TypedArray typedArray = styledAttributes;
        int i = valueFromId;
        int i2 = valueToId;
        String str = propertyName;
        TypedValue tvFrom = typedArray.peekValue(i);
        boolean hasFrom = tvFrom != null;
        int fromType = hasFrom ? tvFrom.type : 0;
        TypedValue tvTo = typedArray.peekValue(i2);
        boolean hasTo = tvTo != null;
        int toType = hasTo ? tvTo.type : 0;
        int i3 = valueType;
        if (i3 != 4) {
            valueType2 = i3;
        } else if ((hasFrom && isColorType(fromType)) || (hasTo && isColorType(toType))) {
            valueType2 = 3;
        } else {
            valueType2 = 0;
        }
        boolean getFloats = valueType2 == 0;
        TypedValue typedValue;
        int toType2;
        PropertyValuesHolder propertyValuesHolder;
        int i4;
        if (valueType2 == 2) {
            String fromString = typedArray.getString(i);
            String toString = typedArray.getString(i2);
            PathData nodesFrom = fromString == null ? null : new PathData(fromString);
            if (toString == null) {
                TypedValue typedValue2 = tvFrom;
                tvFrom = null;
            } else {
                tvFrom = new PathData(toString);
            }
            if (nodesFrom == null && tvFrom == null) {
                typedValue = tvTo;
                toType2 = toType;
                propertyValuesHolder = null;
            } else {
                if (nodesFrom != null) {
                    propertyValuesHolder = null;
                    TypeEvaluator tvTo2 = new PathDataEvaluator();
                    if (tvFrom == null) {
                        toType2 = toType;
                        returnValue = PropertyValuesHolder.ofObject(str, tvTo2, nodesFrom);
                    } else if (PathParser.canMorph(nodesFrom, tvFrom)) {
                        returnValue = PropertyValuesHolder.ofObject(str, tvTo2, nodesFrom, tvFrom);
                        toType2 = toType;
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(" Can't morph from ");
                        stringBuilder.append(fromString);
                        stringBuilder.append(" to ");
                        stringBuilder.append(toString);
                        throw new InflateException(stringBuilder.toString());
                    }
                }
                toType2 = toType;
                propertyValuesHolder = null;
                if (tvFrom != null) {
                    returnValue = PropertyValuesHolder.ofObject(str, new PathDataEvaluator(), tvFrom);
                }
                toType = valueToId;
                i4 = toType2;
            }
            returnValue = propertyValuesHolder;
            toType = valueToId;
            i4 = toType2;
        } else {
            typedValue = tvTo;
            toType2 = toType;
            propertyValuesHolder = null;
            TypeEvaluator evaluator = null;
            if (valueType2 == 3) {
                evaluator = ArgbEvaluator.getInstance();
            }
            float valueTo;
            if (!getFloats) {
                toType = valueToId;
                i4 = toType2;
                int valueTo2;
                int i5;
                if (hasFrom) {
                    int valueFrom;
                    if (fromType == 5) {
                        valueFrom = (int) typedArray.getDimension(i, 0.0f);
                    } else if (isColorType(fromType)) {
                        valueFrom = typedArray.getColor(i, 0);
                    } else {
                        valueFrom = typedArray.getInt(i, 0);
                    }
                    if (hasTo) {
                        if (i4 == 5) {
                            valueTo2 = (int) typedArray.getDimension(toType, 0.0f);
                            i5 = 0;
                        } else if (isColorType(i4)) {
                            i5 = 0;
                            valueTo2 = typedArray.getColor(toType, 0);
                        } else {
                            i5 = 0;
                            valueTo2 = typedArray.getInt(toType, 0);
                        }
                        returnValue = PropertyValuesHolder.ofInt(str, valueFrom, valueTo2);
                    } else {
                        returnValue = PropertyValuesHolder.ofInt(str, valueFrom);
                    }
                } else if (hasTo) {
                    if (i4 == 5) {
                        valueTo2 = (int) typedArray.getDimension(toType, 0.0f);
                        i5 = 0;
                    } else if (isColorType(i4)) {
                        i5 = 0;
                        valueTo2 = typedArray.getColor(toType, 0);
                    } else {
                        i5 = 0;
                        valueTo2 = typedArray.getInt(toType, 0);
                    }
                    returnValue = PropertyValuesHolder.ofInt(str, valueTo2);
                } else {
                    returnValue = propertyValuesHolder;
                }
            } else if (hasFrom) {
                float valueFrom2;
                if (fromType == 5) {
                    valueFrom2 = typedArray.getDimension(i, 0.0f);
                } else {
                    valueFrom2 = typedArray.getFloat(i, 0.0f);
                }
                if (hasTo) {
                    if (toType2 == 5) {
                        valueTo = typedArray.getDimension(valueToId, 0.0f);
                    } else {
                        valueTo = typedArray.getFloat(valueToId, 0.0f);
                    }
                    returnValue = PropertyValuesHolder.ofFloat(str, valueFrom2, valueTo);
                } else {
                    toType = valueToId;
                    i4 = toType2;
                    returnValue = PropertyValuesHolder.ofFloat(str, valueFrom2);
                }
            } else {
                toType = valueToId;
                if (toType2 == 5) {
                    valueTo = typedArray.getDimension(toType, 0.0f);
                } else {
                    valueTo = typedArray.getFloat(toType, 0.0f);
                }
                returnValue = PropertyValuesHolder.ofFloat(str, valueTo);
            }
            if (!(returnValue == null || evaluator == null)) {
                returnValue.setEvaluator(evaluator);
            }
        }
        return returnValue;
    }

    private static void parseAnimatorFromTypeArray(ValueAnimator anim, TypedArray arrayAnimator, TypedArray arrayObjectAnimator, float pixelSize) {
        long duration = (long) arrayAnimator.getInt(1, 300);
        long startDelay = (long) arrayAnimator.getInt(2, 0);
        int valueType = arrayAnimator.getInt(7, 4);
        if (valueType == 4) {
            valueType = inferValueTypeFromValues(arrayAnimator, 5, 6);
        }
        if (getPVH(arrayAnimator, valueType, 5, 6, "") != null) {
            anim.setValues(getPVH(arrayAnimator, valueType, 5, 6, ""));
        }
        anim.setDuration(duration);
        anim.setStartDelay(startDelay);
        if (arrayAnimator.hasValue(3)) {
            anim.setRepeatCount(arrayAnimator.getInt(3, 0));
        }
        if (arrayAnimator.hasValue(4)) {
            anim.setRepeatMode(arrayAnimator.getInt(4, 1));
        }
        if (arrayObjectAnimator != null) {
            setupObjectAnimator(anim, arrayObjectAnimator, valueType, pixelSize);
        }
    }

    private static TypeEvaluator setupAnimatorForPath(ValueAnimator anim, TypedArray arrayAnimator) {
        String fromString = arrayAnimator.getString(5);
        String toString = arrayAnimator.getString(6);
        PathData pathDataFrom = fromString == null ? null : new PathData(fromString);
        PathData pathDataTo = toString == null ? null : new PathData(toString);
        if (pathDataFrom != null) {
            if (pathDataTo != null) {
                anim.setObjectValues(pathDataFrom, pathDataTo);
                if (!PathParser.canMorph(pathDataFrom, pathDataTo)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(arrayAnimator.getPositionDescription());
                    stringBuilder.append(" Can't morph from ");
                    stringBuilder.append(fromString);
                    stringBuilder.append(" to ");
                    stringBuilder.append(toString);
                    throw new InflateException(stringBuilder.toString());
                }
            }
            anim.setObjectValues(pathDataFrom);
            return new PathDataEvaluator();
        } else if (pathDataTo == null) {
            return null;
        } else {
            anim.setObjectValues(pathDataTo);
            return new PathDataEvaluator();
        }
    }

    private static void setupObjectAnimator(ValueAnimator anim, TypedArray arrayObjectAnimator, int valueType, float pixelSize) {
        TypedArray typedArray = arrayObjectAnimator;
        int valueType2 = valueType;
        ObjectAnimator oa = (ObjectAnimator) anim;
        String pathData = typedArray.getString(1);
        if (pathData != null) {
            String propertyXName = typedArray.getString(2);
            String propertyYName = typedArray.getString(3);
            if (valueType2 == 2 || valueType2 == 4) {
                valueType2 = 0;
            }
            if (propertyXName == null && propertyYName == null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(arrayObjectAnimator.getPositionDescription());
                stringBuilder.append(" propertyXName or propertyYName is needed for PathData");
                throw new InflateException(stringBuilder.toString());
            }
            Keyframes xKeyframes;
            Keyframes yKeyframes;
            PathKeyframes keyframeSet = KeyframeSet.ofPath(PathParser.createPathFromPathData(pathData), 0.5f * pixelSize);
            if (valueType2 == 0) {
                xKeyframes = keyframeSet.createXFloatKeyframes();
                yKeyframes = keyframeSet.createYFloatKeyframes();
            } else {
                xKeyframes = keyframeSet.createXIntKeyframes();
                yKeyframes = keyframeSet.createYIntKeyframes();
            }
            PropertyValuesHolder x = null;
            PropertyValuesHolder y = null;
            if (propertyXName != null) {
                x = PropertyValuesHolder.ofKeyframes(propertyXName, xKeyframes);
            }
            if (propertyYName != null) {
                y = PropertyValuesHolder.ofKeyframes(propertyYName, yKeyframes);
            }
            if (x == null) {
                oa.setValues(y);
                return;
            } else if (y == null) {
                oa.setValues(x);
                return;
            } else {
                oa.setValues(x, y);
                return;
            }
        }
        oa.setPropertyName(typedArray.getString(0));
    }

    private static void setupValues(ValueAnimator anim, TypedArray arrayAnimator, boolean getFloats, boolean hasFrom, int fromType, boolean hasTo, int toType) {
        if (getFloats) {
            if (hasFrom) {
                float valueFrom;
                if (fromType == 5) {
                    valueFrom = arrayAnimator.getDimension(5, 0.0f);
                } else {
                    valueFrom = arrayAnimator.getFloat(5, 0.0f);
                }
                if (hasTo) {
                    float valueTo;
                    if (toType == 5) {
                        valueTo = arrayAnimator.getDimension(6, 0.0f);
                    } else {
                        valueTo = arrayAnimator.getFloat(6, 0.0f);
                    }
                    anim.setFloatValues(valueFrom, valueTo);
                    return;
                }
                anim.setFloatValues(valueFrom);
                return;
            }
            float valueTo2;
            if (toType == 5) {
                valueTo2 = arrayAnimator.getDimension(6, 0.0f);
            } else {
                valueTo2 = arrayAnimator.getFloat(6, 0.0f);
            }
            anim.setFloatValues(valueTo2);
        } else if (hasFrom) {
            int valueFrom2;
            if (fromType == 5) {
                valueFrom2 = (int) arrayAnimator.getDimension(5, 0.0f);
            } else if (isColorType(fromType)) {
                valueFrom2 = arrayAnimator.getColor(5, 0);
            } else {
                valueFrom2 = arrayAnimator.getInt(5, 0);
            }
            if (hasTo) {
                int valueTo3;
                if (toType == 5) {
                    valueTo3 = (int) arrayAnimator.getDimension(6, 0.0f);
                } else if (isColorType(toType)) {
                    valueTo3 = arrayAnimator.getColor(6, 0);
                } else {
                    valueTo3 = arrayAnimator.getInt(6, 0);
                }
                anim.setIntValues(valueFrom2, valueTo3);
                return;
            }
            anim.setIntValues(valueFrom2);
        } else if (hasTo) {
            int valueTo4;
            if (toType == 5) {
                valueTo4 = (int) arrayAnimator.getDimension(6, 0.0f);
            } else if (isColorType(toType)) {
                valueTo4 = arrayAnimator.getColor(6, 0);
            } else {
                valueTo4 = arrayAnimator.getInt(6, 0);
            }
            anim.setIntValues(valueTo4);
        }
    }

    private static Animator createAnimatorFromXml(Resources res, Theme theme, XmlPullParser parser, float pixelSize) throws XmlPullParserException, IOException {
        return createAnimatorFromXml(res, theme, parser, Xml.asAttributeSet(parser), null, 0, pixelSize);
    }

    private static Animator createAnimatorFromXml(Resources res, Theme theme, XmlPullParser parser, AttributeSet attrs, AnimatorSet parent, int sequenceOrdering, float pixelSize) throws XmlPullParserException, IOException {
        Resources resources = res;
        Theme theme2 = theme;
        AttributeSet attributeSet = attrs;
        AnimatorSet animatorSet = parent;
        float f = pixelSize;
        Animator anim = null;
        int depth = parser.getDepth();
        ArrayList<Animator> childAnims = null;
        while (true) {
            int next = parser.next();
            int type = next;
            XmlPullParser xmlPullParser;
            if (next != 3 || parser.getDepth() > depth) {
                if (type == 1) {
                    xmlPullParser = parser;
                    break;
                } else if (type == 2) {
                    String name = parser.getName();
                    boolean gotValues = false;
                    if (name.equals("objectAnimator")) {
                        anim = loadObjectAnimator(resources, theme2, attributeSet, f);
                        xmlPullParser = parser;
                    } else if (name.equals("animator")) {
                        anim = loadAnimator(resources, theme2, attributeSet, null, f);
                        xmlPullParser = parser;
                    } else if (name.equals("set")) {
                        TypedArray a;
                        Animator anim2 = new AnimatorSet();
                        if (theme2 != null) {
                            a = theme2.obtainStyledAttributes(attributeSet, R.styleable.AnimatorSet, 0, 0);
                        } else {
                            a = resources.obtainAttributes(attributeSet, R.styleable.AnimatorSet);
                        }
                        anim2.appendChangingConfigurations(a.getChangingConfigurations());
                        TypedArray a2 = a;
                        Animator anim3 = anim2;
                        createAnimatorFromXml(res, theme, parser, attrs, (AnimatorSet) anim2, a.getInt(0, 0), pixelSize);
                        a2.recycle();
                        anim = anim3;
                    } else if (name.equals("propertyValuesHolder")) {
                        PropertyValuesHolder[] values = loadValues(resources, theme2, parser, Xml.asAttributeSet(parser));
                        if (!(values == null || anim == null || !(anim instanceof ValueAnimator))) {
                            ((ValueAnimator) anim).setValues(values);
                        }
                        gotValues = true;
                    } else {
                        xmlPullParser = parser;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unknown animator name: ");
                        stringBuilder.append(parser.getName());
                        throw new RuntimeException(stringBuilder.toString());
                    }
                    if (!(animatorSet == null || gotValues)) {
                        if (childAnims == null) {
                            childAnims = new ArrayList();
                        }
                        childAnims.add(anim);
                    }
                }
            } else {
                xmlPullParser = parser;
                break;
            }
        }
        if (!(animatorSet == null || childAnims == null)) {
            Animator[] animsArray = new Animator[childAnims.size()];
            int index = 0;
            Iterator it = childAnims.iterator();
            while (it.hasNext()) {
                int index2 = index + 1;
                animsArray[index] = (Animator) it.next();
                index = index2;
            }
            if (sequenceOrdering == 0) {
                animatorSet.playTogether(animsArray);
            } else {
                animatorSet.playSequentially(animsArray);
            }
        }
        return anim;
    }

    private static PropertyValuesHolder[] loadValues(Resources res, Theme theme, XmlPullParser parser, AttributeSet attrs) throws XmlPullParserException, IOException {
        PropertyValuesHolder[] valuesArray;
        int valueType;
        ArrayList<PropertyValuesHolder> values = null;
        while (true) {
            int eventType = parser.getEventType();
            int type = eventType;
            if (eventType == 3 || type == 1) {
                valuesArray = null;
            } else if (type != 2) {
                parser.next();
            } else {
                if (parser.getName().equals("propertyValuesHolder")) {
                    TypedArray a;
                    if (theme != null) {
                        a = theme.obtainStyledAttributes(attrs, R.styleable.PropertyValuesHolder, 0, 0);
                    } else {
                        a = res.obtainAttributes(attrs, R.styleable.PropertyValuesHolder);
                    }
                    String propertyName = a.getString(3);
                    valueType = a.getInt(2, 4);
                    PropertyValuesHolder pvh = loadPvh(res, theme, parser, propertyName, valueType);
                    if (pvh == null) {
                        pvh = getPVH(a, valueType, 0, 1, propertyName);
                    }
                    if (pvh != null) {
                        if (values == null) {
                            values = new ArrayList();
                        }
                        values.add(pvh);
                    }
                    a.recycle();
                }
                parser.next();
            }
        }
        valuesArray = null;
        if (values != null) {
            int count = values.size();
            valuesArray = new PropertyValuesHolder[count];
            for (valueType = 0; valueType < count; valueType++) {
                valuesArray[valueType] = (PropertyValuesHolder) values.get(valueType);
            }
        }
        return valuesArray;
    }

    private static int inferValueTypeOfKeyframe(Resources res, Theme theme, AttributeSet attrs) {
        TypedArray a;
        int valueType;
        boolean hasValue = false;
        if (theme != null) {
            a = theme.obtainStyledAttributes(attrs, R.styleable.Keyframe, 0, 0);
        } else {
            a = res.obtainAttributes(attrs, R.styleable.Keyframe);
        }
        TypedValue keyframeValue = a.peekValue(0);
        if (keyframeValue != null) {
            hasValue = true;
        }
        if (hasValue && isColorType(keyframeValue.type)) {
            valueType = 3;
        } else {
            valueType = 0;
        }
        a.recycle();
        return valueType;
    }

    private static int inferValueTypeFromValues(TypedArray styledAttributes, int valueFromId, int valueToId) {
        TypedValue tvFrom = styledAttributes.peekValue(valueFromId);
        boolean hasTo = true;
        int toType = 0;
        boolean hasFrom = tvFrom != null;
        int fromType = hasFrom ? tvFrom.type : 0;
        TypedValue tvTo = styledAttributes.peekValue(valueToId);
        if (tvTo == null) {
            hasTo = false;
        }
        if (hasTo) {
            toType = tvTo.type;
        }
        if ((hasFrom && isColorType(fromType)) || (hasTo && isColorType(toType))) {
            return 3;
        }
        return 0;
    }

    private static void dumpKeyframes(Object[] keyframes, String header) {
        if (keyframes != null && keyframes.length != 0) {
            String str = TAG;
            Log.d(str, header);
            int count = keyframes.length;
            for (int i = 0; i < count; i++) {
                Keyframe keyframe = keyframes[i];
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Keyframe ");
                stringBuilder.append(i);
                stringBuilder.append(": fraction ");
                Object obj = "null";
                stringBuilder.append(keyframe.getFraction() < 0.0f ? obj : Float.valueOf(keyframe.getFraction()));
                stringBuilder.append(", , value : ");
                if (keyframe.hasValue()) {
                    obj = keyframe.getValue();
                }
                stringBuilder.append(obj);
                Log.d(str, stringBuilder.toString());
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:55:0x0111  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0045  */
    private static android.animation.PropertyValuesHolder loadPvh(android.content.res.Resources r19, android.content.res.Resources.Theme r20, org.xmlpull.v1.XmlPullParser r21, java.lang.String r22, int r23) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r0 = r19;
        r1 = r20;
        r2 = 0;
        r3 = 0;
        r4 = r23;
    L_0x0008:
        r5 = r21.next();
        r6 = r5;
        r7 = 3;
        if (r5 == r7) goto L_0x0043;
    L_0x0010:
        r5 = 1;
        if (r6 == r5) goto L_0x0043;
    L_0x0013:
        r5 = r21.getName();
        r7 = "keyframe";
        r7 = r5.equals(r7);
        if (r7 == 0) goto L_0x0042;
    L_0x001f:
        r7 = 4;
        if (r4 != r7) goto L_0x002a;
    L_0x0022:
        r7 = android.util.Xml.asAttributeSet(r21);
        r4 = inferValueTypeOfKeyframe(r0, r1, r7);
    L_0x002a:
        r7 = android.util.Xml.asAttributeSet(r21);
        r7 = loadKeyframe(r0, r1, r7, r4);
        if (r7 == 0) goto L_0x003f;
    L_0x0034:
        if (r3 != 0) goto L_0x003c;
    L_0x0036:
        r8 = new java.util.ArrayList;
        r8.<init>();
        r3 = r8;
    L_0x003c:
        r3.add(r7);
    L_0x003f:
        r21.next();
    L_0x0042:
        goto L_0x0008;
    L_0x0043:
        if (r3 == 0) goto L_0x0111;
    L_0x0045:
        r5 = r3.size();
        r8 = r5;
        if (r5 <= 0) goto L_0x010e;
    L_0x004c:
        r5 = 0;
        r9 = r3.get(r5);
        r9 = (android.animation.Keyframe) r9;
        r10 = r8 + -1;
        r10 = r3.get(r10);
        r10 = (android.animation.Keyframe) r10;
        r11 = r10.getFraction();
        r12 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r13 = (r11 > r12 ? 1 : (r11 == r12 ? 0 : -1));
        r14 = 0;
        if (r13 >= 0) goto L_0x007b;
    L_0x0066:
        r13 = (r11 > r14 ? 1 : (r11 == r14 ? 0 : -1));
        if (r13 >= 0) goto L_0x006e;
    L_0x006a:
        r10.setFraction(r12);
        goto L_0x007b;
    L_0x006e:
        r13 = r3.size();
        r15 = createNewKeyframe(r10, r12);
        r3.add(r13, r15);
        r8 = r8 + 1;
    L_0x007b:
        r13 = r9.getFraction();
        r15 = (r13 > r14 ? 1 : (r13 == r14 ? 0 : -1));
        if (r15 == 0) goto L_0x0094;
    L_0x0083:
        r15 = (r13 > r14 ? 1 : (r13 == r14 ? 0 : -1));
        if (r15 >= 0) goto L_0x008b;
    L_0x0087:
        r9.setFraction(r14);
        goto L_0x0094;
    L_0x008b:
        r15 = createNewKeyframe(r9, r14);
        r3.add(r5, r15);
        r8 = r8 + 1;
    L_0x0094:
        r5 = new android.animation.Keyframe[r8];
        r3.toArray(r5);
        r15 = 0;
    L_0x009a:
        if (r15 >= r8) goto L_0x00fd;
    L_0x009c:
        r7 = r5[r15];
        r16 = r7.getFraction();
        r16 = (r16 > r14 ? 1 : (r16 == r14 ? 0 : -1));
        if (r16 >= 0) goto L_0x00ef;
    L_0x00a6:
        if (r15 != 0) goto L_0x00ae;
    L_0x00a8:
        r7.setFraction(r14);
        r16 = r14;
        goto L_0x00f1;
    L_0x00ae:
        r14 = r8 + -1;
        if (r15 != r14) goto L_0x00b8;
    L_0x00b2:
        r7.setFraction(r12);
        r16 = 0;
        goto L_0x00f1;
    L_0x00b8:
        r14 = r15;
        r17 = r15;
        r18 = r14 + 1;
        r0 = r17;
        r12 = r18;
    L_0x00c1:
        r1 = r8 + -1;
        if (r12 >= r1) goto L_0x00d8;
    L_0x00c5:
        r1 = r5[r12];
        r1 = r1.getFraction();
        r16 = 0;
        r1 = (r1 > r16 ? 1 : (r1 == r16 ? 0 : -1));
        if (r1 < 0) goto L_0x00d2;
    L_0x00d1:
        goto L_0x00da;
    L_0x00d2:
        r0 = r12;
        r12 = r12 + 1;
        r1 = r20;
        goto L_0x00c1;
    L_0x00d8:
        r16 = 0;
    L_0x00da:
        r1 = r0 + 1;
        r1 = r5[r1];
        r1 = r1.getFraction();
        r12 = r14 + -1;
        r12 = r5[r12];
        r12 = r12.getFraction();
        r1 = r1 - r12;
        distributeKeyframes(r5, r1, r14, r0);
        goto L_0x00f1;
    L_0x00ef:
        r16 = r14;
    L_0x00f1:
        r15 = r15 + 1;
        r7 = 3;
        r12 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = r19;
        r1 = r20;
        r14 = r16;
        goto L_0x009a;
    L_0x00fd:
        r0 = r22;
        r2 = android.animation.PropertyValuesHolder.ofKeyframe(r0, r5);
        r1 = 3;
        if (r4 != r1) goto L_0x0113;
    L_0x0106:
        r1 = android.animation.ArgbEvaluator.getInstance();
        r2.setEvaluator(r1);
        goto L_0x0113;
    L_0x010e:
        r0 = r22;
        goto L_0x0113;
    L_0x0111:
        r0 = r22;
    L_0x0113:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.animation.AnimatorInflater.loadPvh(android.content.res.Resources, android.content.res.Resources$Theme, org.xmlpull.v1.XmlPullParser, java.lang.String, int):android.animation.PropertyValuesHolder");
    }

    private static Keyframe createNewKeyframe(Keyframe sampleKeyframe, float fraction) {
        if (sampleKeyframe.getType() == Float.TYPE) {
            return Keyframe.ofFloat(fraction);
        }
        if (sampleKeyframe.getType() == Integer.TYPE) {
            return Keyframe.ofInt(fraction);
        }
        return Keyframe.ofObject(fraction);
    }

    private static void distributeKeyframes(Keyframe[] keyframes, float gap, int startIndex, int endIndex) {
        float increment = gap / ((float) ((endIndex - startIndex) + 2));
        for (int i = startIndex; i <= endIndex; i++) {
            keyframes[i].setFraction(keyframes[i - 1].getFraction() + increment);
        }
    }

    private static Keyframe loadKeyframe(Resources res, Theme theme, AttributeSet attrs, int valueType) throws XmlPullParserException, IOException {
        TypedArray a;
        if (theme != null) {
            a = theme.obtainStyledAttributes(attrs, R.styleable.Keyframe, 0, 0);
        } else {
            a = res.obtainAttributes(attrs, R.styleable.Keyframe);
        }
        Keyframe keyframe = null;
        float fraction = a.getFloat(3, -1.0f);
        TypedValue keyframeValue = a.peekValue(0);
        boolean hasValue = keyframeValue != null;
        if (valueType == 4) {
            if (hasValue && isColorType(keyframeValue.type)) {
                valueType = 3;
            } else {
                valueType = 0;
            }
        }
        if (!hasValue) {
            Keyframe ofFloat;
            if (valueType == 0) {
                ofFloat = Keyframe.ofFloat(fraction);
            } else {
                ofFloat = Keyframe.ofInt(fraction);
            }
            keyframe = ofFloat;
        } else if (valueType == 0) {
            keyframe = Keyframe.ofFloat(fraction, a.getFloat(0, 0.0f));
        } else if (valueType == 1 || valueType == 3) {
            keyframe = Keyframe.ofInt(fraction, a.getInt(0, 0));
        }
        int resID = a.getResourceId(1, 0);
        if (resID > 0) {
            keyframe.setInterpolator(AnimationUtils.loadInterpolator(res, theme, resID));
        }
        a.recycle();
        return keyframe;
    }

    private static ObjectAnimator loadObjectAnimator(Resources res, Theme theme, AttributeSet attrs, float pathErrorScale) throws NotFoundException {
        ObjectAnimator anim = new ObjectAnimator();
        loadAnimator(res, theme, attrs, anim, pathErrorScale);
        return anim;
    }

    private static ValueAnimator loadAnimator(Resources res, Theme theme, AttributeSet attrs, ValueAnimator anim, float pathErrorScale) throws NotFoundException {
        TypedArray arrayAnimator;
        TypedArray arrayObjectAnimator = null;
        if (theme != null) {
            arrayAnimator = theme.obtainStyledAttributes(attrs, R.styleable.Animator, 0, 0);
        } else {
            arrayAnimator = res.obtainAttributes(attrs, R.styleable.Animator);
        }
        if (anim != null) {
            if (theme != null) {
                arrayObjectAnimator = theme.obtainStyledAttributes(attrs, R.styleable.PropertyAnimator, 0, 0);
            } else {
                arrayObjectAnimator = res.obtainAttributes(attrs, R.styleable.PropertyAnimator);
            }
            anim.appendChangingConfigurations(arrayObjectAnimator.getChangingConfigurations());
        }
        if (anim == null) {
            anim = new ValueAnimator();
        }
        anim.appendChangingConfigurations(arrayAnimator.getChangingConfigurations());
        parseAnimatorFromTypeArray(anim, arrayAnimator, arrayObjectAnimator, pathErrorScale);
        int resID = arrayAnimator.getResourceId(0, 0);
        if (resID > 0) {
            Interpolator interpolator = AnimationUtils.loadInterpolator(res, theme, resID);
            if (interpolator instanceof BaseInterpolator) {
                anim.appendChangingConfigurations(((BaseInterpolator) interpolator).getChangingConfiguration());
            }
            anim.setInterpolator(interpolator);
        }
        arrayAnimator.recycle();
        if (arrayObjectAnimator != null) {
            arrayObjectAnimator.recycle();
        }
        return anim;
    }

    private static int getChangingConfigs(Resources resources, int id) {
        int i;
        synchronized (sTmpTypedValue) {
            resources.getValue(id, sTmpTypedValue, true);
            i = sTmpTypedValue.changingConfigurations;
        }
        return i;
    }

    private static boolean isColorType(int type) {
        return type >= 28 && type <= 31;
    }
}
