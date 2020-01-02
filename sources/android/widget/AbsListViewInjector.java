package android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.util.TypedValue;
import java.lang.reflect.Field;
import java.util.Objects;
import miui.os.Environment;

public class AbsListViewInjector {
    private static final int MINIMUM_VELOCITY_IN_DP = 180;
    private static final String TAG = "AbsListViewInjector";
    private static final Field mMinimumVelocity;

    static class DummyEdgeEffect extends EdgeEffect {
        DummyEdgeEffect(Context context) {
            super(context);
        }

        public void onPull(float deltaDistance) {
        }

        public void onPull(float deltaDistance, float displacement) {
        }

        public void onRelease() {
        }

        public void onAbsorb(int velocity) {
        }

        public boolean draw(Canvas canvas) {
            return false;
        }
    }

    public static boolean needFinishActionMode(AbsListView listView) {
        return !Environment.isUsingMiui(listView.getContext()) && listView.getCheckedItemCount() == 0;
    }

    private static boolean isSpringOverscrollEnabled(AbsListView listView) {
        return listView.getOverScrollMode() != 2 && listView.mUsingMiuiTheme;
    }

    static boolean overScrollBy(AbsListView absListView, int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        int scrollY2;
        int deltaY2;
        int i = maxOverScrollY;
        if (isTouchEvent && isSpringOverscrollEnabled(absListView)) {
            int scrollY3;
            int scrollDirection = Integer.signum(scrollY);
            if (Math.abs(scrollY) >= i) {
                scrollY3 = scrollDirection * i;
            } else {
                scrollY3 = scrollY;
            }
            scrollY2 = scrollY3;
            deltaY2 = (int) (((float) deltaY) * overScrollWeight(scrollY3, i));
        } else {
            scrollY2 = scrollY;
            deltaY2 = deltaY;
        }
        return absListView.superOverScrollBy(deltaX, deltaY2, scrollX, scrollY2, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    static void onInit(AbsListView absListView) {
        absListView.mDefaultOverscrollDistance = absListView.mOverscrollDistance;
        absListView.mDefaultOverflingDistance = absListView.mOverflingDistance;
        setMinimumVelocity(absListView, (int) TypedValue.applyDimension(1, 180.0f, absListView.getResources().getDisplayMetrics()));
        absListView.mUsingMiuiTheme = Environment.isUsingMiui(absListView.getContext());
        if (isSpringOverscrollEnabled(absListView)) {
            setupSpring(absListView);
        }
    }

    static void onLayout(AbsListView absListView, boolean changed, int l, int t, int r, int b) {
        if (isSpringOverscrollEnabled(absListView)) {
            absListView.mOverflingDistance = b - t;
            absListView.mOverscrollDistance = b - t;
        }
    }

    private static void setupSpring(AbsListView absListView) {
        Objects.requireNonNull(absListView);
        absListView.mFlingRunnable = new OverFlingRunnable();
        absListView.mEdgeGlowTop = new DummyEdgeEffect(absListView.getContext());
        absListView.mEdgeGlowBottom = new DummyEdgeEffect(absListView.getContext());
        if (absListView.isLaidOut() && !absListView.isLayoutRequested()) {
            absListView.mOverflingDistance = absListView.getHeight();
            absListView.mOverscrollDistance = absListView.getHeight();
        }
    }

    private static float overScrollWeight(int scrollY, int max) {
        return ((float) (-Math.pow((double) (Math.abs(((float) scrollY) / ((float) max)) - 1.0f), 3.0d))) / 1.5f;
    }

    static void startOverfling(AbsListView absListView, OverFlingRunnable flingRunnable, int initialVelocity) {
        AbsListView absListView2 = absListView;
        OverFlingRunnable overFlingRunnable = flingRunnable;
        overFlingRunnable.mScroller.setInterpolator(null);
        int scrollY = absListView.getScrollY();
        if (scrollY == 0) {
            OverScrollLogger.debug("startOverfling: unknown direction, start normal fling with velocity %d", Integer.valueOf(initialVelocity));
            flingRunnable.start(initialVelocity);
            return;
        }
        if (Integer.signum(initialVelocity) * scrollY > 0) {
            OverScrollLogger.debug("startOverfling: fling to boundary with velocity %d", Integer.valueOf(initialVelocity));
            int i = scrollY;
            overFlingRunnable.mScroller.fling(0, i, 0, (int) (((float) initialVelocity) * overScrollWeight(scrollY, absListView2.mOverflingDistance)), 0, 0, 0, 0, 0, absListView2.mOverflingDistance);
        } else {
            int min;
            int max;
            int i2 = initialVelocity;
            OverScrollLogger.debug("startOverfling: fling to content with velocity %d, current scrollY %d", Integer.valueOf(initialVelocity), Integer.valueOf(absListView.getScrollY()));
            if (scrollY > 0) {
                min = Integer.MIN_VALUE;
                max = 0;
            } else {
                min = 0;
                max = Integer.MAX_VALUE;
            }
            overFlingRunnable.mScroller.fling(0, scrollY, 0, initialVelocity, 0, 0, min, max, 0, absListView2.mOverflingDistance);
        }
        absListView2.mTouchMode = 6;
        absListView.invalidate();
        absListView.postOnAnimation(flingRunnable);
    }

    static {
        Field minimumVelocity;
        try {
            minimumVelocity = AbsListView.class.getDeclaredField("mMinimumVelocity");
            minimumVelocity.setAccessible(true);
        } catch (NoSuchFieldException e) {
            Log.w(TAG, "reflect mMinimumVelocity, skip");
            minimumVelocity = null;
        }
        mMinimumVelocity = minimumVelocity;
    }

    private static void setMinimumVelocity(AbsListView listView, int minimumVelocity) {
        Field field = mMinimumVelocity;
        String str = TAG;
        if (field != null) {
            try {
                field.set(listView, Integer.valueOf(minimumVelocity));
                return;
            } catch (IllegalAccessException e) {
                Log.w(str, "set mMinimumVelocity failed");
                return;
            }
        }
        Log.w(str, "no mMinimumVelocity field, skipping");
    }

    static void doAnimationFrame(AbsListView absListView, OverFlingRunnable flingRunnable) {
        AbsListView absListView2 = absListView;
        OverFlingRunnable overFlingRunnable = flingRunnable;
        if (absListView2.mTouchMode == 6) {
            OverScroller scroller = overFlingRunnable.mScroller;
            if (scroller.computeScrollOffset()) {
                int scrollY = absListView.getScrollY();
                int currY = scroller.getCurrY();
                int deltaY = currY - scrollY;
                boolean safeOverfling = scroller.getFinalY() == 0;
                OverScrollLogger.debug("overfling scrollY: %d, currY: %d, springBack: %b", Integer.valueOf(scrollY), Integer.valueOf(currY), Boolean.valueOf(safeOverfling));
                if (scrollY != 0 || safeOverfling) {
                    int deltaY2;
                    if (safeOverfling || Integer.signum(currY) * scrollY >= 0) {
                        deltaY2 = deltaY;
                    } else {
                        deltaY2 = -scrollY;
                    }
                    absListView.overScrollBy(0, deltaY2, 0, scrollY, 0, 0, 0, absListView2.mOverflingDistance, false);
                    deltaY = deltaY2;
                }
                if (safeOverfling || absListView.getScrollY() != 0) {
                    absListView.invalidate();
                    absListView.postOnAnimation(flingRunnable);
                    return;
                }
                OverScrollLogger.debug("scrollY fully consumed, do normal fling");
                int velocity = Integer.signum(deltaY) * ((int) scroller.getCurrVelocity());
                scroller.abortAnimation();
                overFlingRunnable.start(velocity);
                return;
            }
            OverScrollLogger.debug("overfling finish.");
            flingRunnable.endFling();
            return;
        }
        flingRunnable.superDoAnimationFrame();
    }
}
