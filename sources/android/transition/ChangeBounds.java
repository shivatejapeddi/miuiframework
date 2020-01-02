package android.transition;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.RectEvaluator;
import android.animation.TypeConverter;
import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.BrowserContract.Bookmarks;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.R;
import java.util.Map;

public class ChangeBounds extends Transition {
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private static final Property<View, PointF> BOTTOM_RIGHT_ONLY_PROPERTY;
    private static final Property<ViewBounds, PointF> BOTTOM_RIGHT_PROPERTY;
    private static final Property<Drawable, PointF> DRAWABLE_ORIGIN_PROPERTY = new Property<Drawable, PointF>(PointF.class, "boundsOrigin") {
        private Rect mBounds = new Rect();

        public void set(Drawable object, PointF value) {
            object.copyBounds(this.mBounds);
            this.mBounds.offsetTo(Math.round(value.x), Math.round(value.y));
            object.setBounds(this.mBounds);
        }

        public PointF get(Drawable object) {
            object.copyBounds(this.mBounds);
            return new PointF((float) this.mBounds.left, (float) this.mBounds.top);
        }
    };
    private static final String LOG_TAG = "ChangeBounds";
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private static final Property<View, PointF> POSITION_PROPERTY = new Property<View, PointF>(PointF.class, Bookmarks.POSITION) {
        public void set(View view, PointF topLeft) {
            int left = Math.round(topLeft.x);
            int top = Math.round(topLeft.y);
            view.setLeftTopRightBottom(left, top, view.getWidth() + left, view.getHeight() + top);
        }

        public PointF get(View view) {
            return null;
        }
    };
    private static final String PROPNAME_BOUNDS = "android:changeBounds:bounds";
    private static final String PROPNAME_CLIP = "android:changeBounds:clip";
    private static final String PROPNAME_PARENT = "android:changeBounds:parent";
    private static final String PROPNAME_WINDOW_X = "android:changeBounds:windowX";
    private static final String PROPNAME_WINDOW_Y = "android:changeBounds:windowY";
    private static final Property<View, PointF> TOP_LEFT_ONLY_PROPERTY;
    private static final Property<ViewBounds, PointF> TOP_LEFT_PROPERTY;
    private static RectEvaluator sRectEvaluator = new RectEvaluator();
    private static final String[] sTransitionProperties = new String[]{PROPNAME_BOUNDS, PROPNAME_CLIP, PROPNAME_PARENT, PROPNAME_WINDOW_X, PROPNAME_WINDOW_Y};
    boolean mReparent;
    boolean mResizeClip;
    int[] tempLocation;

    private static class ViewBounds {
        private int mBottom;
        private int mBottomRightCalls;
        private int mLeft;
        private int mRight;
        private int mTop;
        private int mTopLeftCalls;
        private View mView;

        public ViewBounds(View view) {
            this.mView = view;
        }

        public void setTopLeft(PointF topLeft) {
            this.mLeft = Math.round(topLeft.x);
            this.mTop = Math.round(topLeft.y);
            this.mTopLeftCalls++;
            if (this.mTopLeftCalls == this.mBottomRightCalls) {
                setLeftTopRightBottom();
            }
        }

        public void setBottomRight(PointF bottomRight) {
            this.mRight = Math.round(bottomRight.x);
            this.mBottom = Math.round(bottomRight.y);
            this.mBottomRightCalls++;
            if (this.mTopLeftCalls == this.mBottomRightCalls) {
                setLeftTopRightBottom();
            }
        }

        private void setLeftTopRightBottom() {
            this.mView.setLeftTopRightBottom(this.mLeft, this.mTop, this.mRight, this.mBottom);
            this.mTopLeftCalls = 0;
            this.mBottomRightCalls = 0;
        }
    }

    static {
        String str = "topLeft";
        TOP_LEFT_PROPERTY = new Property<ViewBounds, PointF>(PointF.class, str) {
            public void set(ViewBounds viewBounds, PointF topLeft) {
                viewBounds.setTopLeft(topLeft);
            }

            public PointF get(ViewBounds viewBounds) {
                return null;
            }
        };
        String str2 = "bottomRight";
        BOTTOM_RIGHT_PROPERTY = new Property<ViewBounds, PointF>(PointF.class, str2) {
            public void set(ViewBounds viewBounds, PointF bottomRight) {
                viewBounds.setBottomRight(bottomRight);
            }

            public PointF get(ViewBounds viewBounds) {
                return null;
            }
        };
        BOTTOM_RIGHT_ONLY_PROPERTY = new Property<View, PointF>(PointF.class, str2) {
            public void set(View view, PointF bottomRight) {
                view.setLeftTopRightBottom(view.getLeft(), view.getTop(), Math.round(bottomRight.x), Math.round(bottomRight.y));
            }

            public PointF get(View view) {
                return null;
            }
        };
        TOP_LEFT_ONLY_PROPERTY = new Property<View, PointF>(PointF.class, str) {
            public void set(View view, PointF topLeft) {
                view.setLeftTopRightBottom(Math.round(topLeft.x), Math.round(topLeft.y), view.getRight(), view.getBottom());
            }

            public PointF get(View view) {
                return null;
            }
        };
    }

    public ChangeBounds() {
        this.tempLocation = new int[2];
        this.mResizeClip = false;
        this.mReparent = false;
    }

    public ChangeBounds(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.tempLocation = new int[2];
        this.mResizeClip = false;
        this.mReparent = false;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChangeBounds);
        boolean resizeClip = a.getBoolean(0, false);
        a.recycle();
        setResizeClip(resizeClip);
    }

    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    public void setResizeClip(boolean resizeClip) {
        this.mResizeClip = resizeClip;
    }

    public boolean getResizeClip() {
        return this.mResizeClip;
    }

    @Deprecated
    public void setReparent(boolean reparent) {
        this.mReparent = reparent;
    }

    private void captureValues(TransitionValues values) {
        View view = values.view;
        if (view.isLaidOut() || view.getWidth() != 0 || view.getHeight() != 0) {
            values.values.put(PROPNAME_BOUNDS, new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()));
            values.values.put(PROPNAME_PARENT, values.view.getParent());
            if (this.mReparent) {
                values.view.getLocationInWindow(this.tempLocation);
                values.values.put(PROPNAME_WINDOW_X, Integer.valueOf(this.tempLocation[0]));
                values.values.put(PROPNAME_WINDOW_Y, Integer.valueOf(this.tempLocation[1]));
            }
            if (this.mResizeClip) {
                values.values.put(PROPNAME_CLIP, view.getClipBounds());
            }
        }
    }

    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    private boolean parentMatches(View startParent, View endParent) {
        if (!this.mReparent) {
            return true;
        }
        boolean z = true;
        TransitionValues endValues = getMatchedTransitionValues(startParent, true);
        if (endValues == null) {
            if (startParent != endParent) {
                z = false;
            }
            return z;
        }
        if (endParent != endValues.view) {
            z = false;
        }
        return z;
    }

    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        Animator animator;
        TransitionValues transitionValues = startValues;
        TransitionValues transitionValues2 = endValues;
        TransitionValues transitionValues3;
        if (transitionValues == null) {
            transitionValues3 = transitionValues2;
            animator = null;
        } else if (transitionValues2 == null) {
            transitionValues3 = transitionValues2;
            animator = null;
        } else {
            Map<String, Object> startParentVals = transitionValues.values;
            Map<String, Object> endParentVals = transitionValues2.values;
            String str = PROPNAME_PARENT;
            ViewGroup startParent = (ViewGroup) startParentVals.get(str);
            ViewGroup endParent = (ViewGroup) endParentVals.get(str);
            Map<String, Object> map;
            Map<String, Object> map2;
            ViewGroup viewGroup;
            ViewGroup viewGroup2;
            if (startParent == null) {
                map = startParentVals;
                map2 = endParentVals;
                viewGroup = startParent;
                viewGroup2 = endParent;
                transitionValues3 = transitionValues2;
            } else if (endParent == null) {
                map = startParentVals;
                map2 = endParentVals;
                viewGroup = startParent;
                viewGroup2 = endParent;
                transitionValues3 = transitionValues2;
            } else {
                View view = transitionValues2.view;
                int startLeft;
                ChangeBounds endClip;
                int startWidth;
                View view2;
                TransitionValues transitionValues4;
                if (parentMatches(startParent, endParent)) {
                    Map map3 = transitionValues.values;
                    String str2 = PROPNAME_BOUNDS;
                    Rect startBounds = (Rect) map3.get(str2);
                    Rect endBounds = (Rect) transitionValues2.values.get(str2);
                    startLeft = startBounds.left;
                    int endLeft = endBounds.left;
                    int startTop = startBounds.top;
                    int endTop = endBounds.top;
                    int startRight = startBounds.right;
                    int endRight = endBounds.right;
                    startParentVals = startBounds.bottom;
                    int endBottom = endBounds.bottom;
                    startParent = startRight - startLeft;
                    endParent = startParentVals - startTop;
                    int endWidth = endRight - endLeft;
                    int endHeight = endBottom - endTop;
                    Map map4 = transitionValues.values;
                    String str3 = PROPNAME_CLIP;
                    Rect startClip = (Rect) map4.get(str3);
                    View view3 = view;
                    Rect endClip2 = (Rect) transitionValues2.values.get(str3);
                    view = 0;
                    if (!((startParent == null || endParent == null) && (endWidth == 0 || endHeight == 0))) {
                        if (!(startLeft == endLeft && startTop == endTop)) {
                            view = 0 + 1;
                        }
                        if (!(startRight == endRight && startParentVals == endBottom)) {
                            view++;
                        }
                    }
                    if (!(startClip == null || startClip.equals(endClip2)) || (startClip == null && endClip2 != null)) {
                        view++;
                    }
                    Rect startClip2;
                    Rect endClip3;
                    int i;
                    int endRight2;
                    int i2;
                    int i3;
                    int i4;
                    int i5;
                    Map<String, Object> map5;
                    int i6;
                    if (view > null) {
                        if (view3.getParent() instanceof ViewGroup) {
                            final ViewGroup parent = (ViewGroup) view3.getParent();
                            startClip2 = startClip;
                            parent.suppressLayout(true);
                            endClip3 = endClip2;
                            endClip = this;
                            endClip.addListener(new TransitionListenerAdapter() {
                                boolean mCanceled = false;

                                public void onTransitionCancel(Transition transition) {
                                    parent.suppressLayout(false);
                                    this.mCanceled = true;
                                }

                                public void onTransitionEnd(Transition transition) {
                                    if (!this.mCanceled) {
                                        parent.suppressLayout(false);
                                    }
                                    transition.removeListener(this);
                                }

                                public void onTransitionPause(Transition transition) {
                                    parent.suppressLayout(false);
                                }

                                public void onTransitionResume(Transition transition) {
                                    parent.suppressLayout(true);
                                }
                            });
                        } else {
                            startClip2 = startClip;
                            endClip3 = endClip2;
                            endClip = this;
                        }
                        Object startClip3;
                        int startRight2;
                        int i7;
                        if (endClip.mResizeClip) {
                            int endLeft2;
                            int startTop2;
                            i = endWidth;
                            ViewGroup viewGroup3 = endParent;
                            startClip3 = view3;
                            startWidth = startParent;
                            startParent = Math.max(startWidth, endWidth);
                            startRight2 = startRight;
                            endRight2 = endRight;
                            startClip3.setLeftTopRightBottom(startLeft, startTop, startLeft + startParent, startTop + Math.max(endParent, endHeight));
                            if (startLeft == endLeft && startTop == endTop) {
                                endLeft2 = endLeft;
                                startTop2 = startTop;
                                i2 = startLeft;
                                startLeft = 0;
                            } else {
                                ObjectAnimator positionAnimator = null;
                                startTop2 = startTop;
                                endLeft2 = endLeft;
                                startLeft = ObjectAnimator.ofObject(startClip3, POSITION_PROPERTY, null, getPathMotion().getPath((float) startLeft, (float) startTop, (float) endLeft, (float) endTop));
                            }
                            startTop = endClip3;
                            if (startClip2 == null) {
                                endLeft = 0;
                                endRight = new Rect(0, 0, startWidth, endParent);
                            } else {
                                endLeft = 0;
                                endRight = startClip2;
                            }
                            if (endClip3 == null) {
                                endLeft = new Rect(endLeft, endLeft, endWidth, endHeight);
                            } else {
                                endLeft = endClip3;
                            }
                            Object obj;
                            if (endRight.equals(endLeft)) {
                                Object obj2 = endLeft;
                                obj = endRight;
                                i3 = endTop;
                                i4 = endHeight;
                                i5 = endWidth;
                                map5 = startParentVals;
                                i7 = startRight2;
                                i6 = endLeft2;
                                endLeft2 = startParent;
                                startParent = startLeft;
                                startParentVals = null;
                            } else {
                                startClip3.setClipBounds(endRight);
                                ObjectAnimator clipAnimator = null;
                                startTop2 = endTop;
                                Rect endClip4 = endLeft;
                                startRight2 = startWidth;
                                endClip3 = endClip4;
                                AnimatorListener animatorListener = startRight;
                                obj = endRight;
                                endRight = startClip3;
                                i3 = startTop2;
                                startTop2 = startParentVals;
                                startParentVals = ObjectAnimator.ofObject(startClip3, "clipBounds", sRectEvaluator, endRight, endLeft);
                                endTop = endLeft2;
                                endLeft2 = startParent;
                                startParent = startLeft;
                                startLeft = i3;
                                endHeight = endRight2;
                                endWidth = endBottom;
                                startRight = new AnimatorListenerAdapter() {
                                    private boolean mIsCanceled;

                                    public void onAnimationCancel(Animator animation) {
                                        this.mIsCanceled = true;
                                    }

                                    public void onAnimationEnd(Animator animation) {
                                        if (!this.mIsCanceled) {
                                            endRight.setClipBounds(startTop);
                                            endRight.setLeftTopRightBottom(endTop, startLeft, endHeight, endWidth);
                                        }
                                    }
                                };
                                startParentVals.addListener(animatorListener);
                            }
                            endHeight = TransitionUtils.mergeAnimators(startParent, startParentVals);
                        } else {
                            view2 = view3;
                            view2.setLeftTopRightBottom(startLeft, startTop, startRight, startParentVals);
                            View view4;
                            if (view != 2) {
                                startRight2 = endHeight;
                                i = endWidth;
                                view4 = view2;
                                i2 = startParent;
                                endRight2 = endParent;
                                if (startLeft != endLeft) {
                                    startClip3 = view4;
                                } else if (startTop != endTop) {
                                    startClip3 = view4;
                                } else {
                                    startClip3 = view4;
                                    endHeight = ObjectAnimator.ofObject(startClip3, BOTTOM_RIGHT_ONLY_PROPERTY, null, getPathMotion().getPath((float) startRight, (float) startParentVals, (float) endRight, (float) endBottom));
                                    i7 = startRight;
                                    i6 = endLeft;
                                    i3 = endTop;
                                    map5 = startParentVals;
                                    i4 = startRight2;
                                    endParent = endRight2;
                                    i5 = i;
                                    startRight2 = i2;
                                    endRight2 = endRight;
                                    i = startTop;
                                    i2 = startLeft;
                                }
                                endHeight = ObjectAnimator.ofObject(startClip3, TOP_LEFT_ONLY_PROPERTY, null, getPathMotion().getPath((float) startLeft, (float) startTop, (float) endLeft, (float) endTop));
                                i7 = startRight;
                                i6 = endLeft;
                                i3 = endTop;
                                map5 = startParentVals;
                                i4 = startRight2;
                                endParent = endRight2;
                                i5 = i;
                                startRight2 = i2;
                                endRight2 = endRight;
                                i = startTop;
                                i2 = startLeft;
                            } else if (startParent == endWidth && endParent == endHeight) {
                                int numChanges = view;
                                startRight2 = endHeight;
                                endRight2 = endParent;
                                i = endWidth;
                                endHeight = ObjectAnimator.ofObject((Object) view2, POSITION_PROPERTY, null, getPathMotion().getPath((float) startLeft, (float) startTop, (float) endLeft, (float) endTop));
                                i7 = startRight;
                                i6 = endLeft;
                                i3 = endTop;
                                i2 = startLeft;
                                map5 = startParentVals;
                                i4 = startRight2;
                                endParent = endRight2;
                                i5 = i;
                                endRight2 = endRight;
                                i = startTop;
                                startRight2 = startParent;
                            } else {
                                startRight2 = endHeight;
                                i = endWidth;
                                endRight2 = endParent;
                                view3 = view;
                                final Object endHeight2 = new ViewBounds(view2);
                                i2 = startParent;
                                Path topLeftPath = getPathMotion().getPath((float) startLeft, (float) startTop, (float) endLeft, (float) endTop);
                                ObjectAnimator topLeftAnimator = ObjectAnimator.ofObject(endHeight2, TOP_LEFT_PROPERTY, (TypeConverter) null, topLeftPath);
                                view4 = view2;
                                ObjectAnimator bottomRightAnimator = ObjectAnimator.ofObject(endHeight2, BOTTOM_RIGHT_PROPERTY, (TypeConverter) null, getPathMotion().getPath((float) startRight, (float) startParentVals, (float) endRight, (float) endBottom));
                                startParent = new AnimatorSet();
                                startParent.playTogether(topLeftAnimator, bottomRightAnimator);
                                endParent = startParent;
                                startParent.addListener(new AnimatorListenerAdapter() {
                                    private ViewBounds mViewBounds = endHeight2;
                                });
                                i7 = startRight;
                                i6 = endLeft;
                                i3 = endTop;
                                map5 = startParentVals;
                                endHeight = endParent;
                                i4 = startRight2;
                                endParent = endRight2;
                                i5 = i;
                                startRight2 = i2;
                                endRight2 = endRight;
                                i = startTop;
                                i2 = startLeft;
                            }
                        }
                        return endHeight;
                    }
                    i6 = endLeft;
                    endRight2 = endRight;
                    i = startTop;
                    i3 = endTop;
                    i2 = startLeft;
                    i4 = endHeight;
                    i5 = endWidth;
                    startClip2 = startClip;
                    endClip3 = endClip2;
                    map5 = startParentVals;
                    ViewGroup viewGroup4 = startParent;
                    endHeight = sceneRoot;
                    transitionValues4 = startValues;
                    transitionValues3 = endValues;
                } else {
                    map = startParentVals;
                    map2 = endParentVals;
                    viewGroup = startParent;
                    viewGroup2 = endParent;
                    view2 = view;
                    sceneRoot.getLocationInWindow(this.tempLocation);
                    transitionValues4 = startValues;
                    Map map6 = transitionValues4.values;
                    str = PROPNAME_WINDOW_X;
                    startWidth = ((Integer) map6.get(str)).intValue() - this.tempLocation[0];
                    map6 = transitionValues4.values;
                    String str4 = PROPNAME_WINDOW_Y;
                    startParentVals = ((Integer) map6.get(str4)).intValue() - this.tempLocation[1];
                    transitionValues3 = endValues;
                    startParent = ((Integer) transitionValues3.values.get(str)).intValue() - this.tempLocation[0];
                    Map<String, Object> endParent2 = ((Integer) transitionValues3.values.get(str4)).intValue() - this.tempLocation[1];
                    if (!(startWidth == startParent && startParentVals == endParent2)) {
                        int width = view2.getWidth();
                        startLeft = view2.getHeight();
                        Bitmap bitmap = Bitmap.createBitmap(width, startLeft, Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        view2.draw(canvas);
                        BitmapDrawable drawable = new BitmapDrawable(bitmap);
                        drawable.setBounds(startWidth, startParentVals, startWidth + width, startParentVals + startLeft);
                        float transitionAlpha = view2.getTransitionAlpha();
                        view2.setTransitionAlpha(0.0f);
                        sceneRoot.getOverlay().add(drawable);
                        Canvas canvas2 = canvas;
                        Bitmap bitmap2 = bitmap;
                        int height = startLeft;
                        Path topLeftPath2 = getPathMotion().getPath((float) startWidth, (float) startParentVals, (float) startParent, (float) endParent2);
                        PropertyValuesHolder origin = PropertyValuesHolder.ofObject(DRAWABLE_ORIGIN_PROPERTY, null, topLeftPath2);
                        ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(drawable, origin);
                        BitmapDrawable drawable2 = drawable;
                        final ViewGroup viewGroup5 = sceneRoot;
                        AnonymousClass10 anonymousClass10 = r0;
                        final BitmapDrawable bitmapDrawable = drawable2;
                        ObjectAnimator anim2 = anim;
                        final View view5 = view2;
                        final float f = transitionAlpha;
                        AnonymousClass10 anonymousClass102 = new AnimatorListenerAdapter() {
                            public void onAnimationEnd(Animator animation) {
                                viewGroup5.getOverlay().remove(bitmapDrawable);
                                view5.setTransitionAlpha(f);
                            }
                        };
                        anim2.addListener(anonymousClass10);
                        return anim2;
                    }
                }
                return null;
            }
            return null;
        }
        return animator;
    }
}
