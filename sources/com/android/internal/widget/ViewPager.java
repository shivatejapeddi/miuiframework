package com.android.internal.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.util.MathUtils;
import android.view.AbsSavedState;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.animation.Interpolator;
import android.view.inspector.InspectionCompanion.UninitializedPropertyMapException;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import android.widget.EdgeEffect;
import android.widget.Scroller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ViewPager extends ViewGroup {
    private static final int CLOSE_ENOUGH = 2;
    private static final Comparator<ItemInfo> COMPARATOR = new Comparator<ItemInfo>() {
        public int compare(ItemInfo lhs, ItemInfo rhs) {
            return lhs.position - rhs.position;
        }
    };
    private static final boolean DEBUG = false;
    private static final int DEFAULT_GUTTER_SIZE = 16;
    private static final int DEFAULT_OFFSCREEN_PAGES = 1;
    private static final int DRAW_ORDER_DEFAULT = 0;
    private static final int DRAW_ORDER_FORWARD = 1;
    private static final int DRAW_ORDER_REVERSE = 2;
    private static final int INVALID_POINTER = -1;
    private static final int[] LAYOUT_ATTRS = new int[]{16842931};
    private static final int MAX_SCROLL_X = 16777216;
    private static final int MAX_SETTLE_DURATION = 600;
    private static final int MIN_DISTANCE_FOR_FLING = 25;
    private static final int MIN_FLING_VELOCITY = 400;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final String TAG = "ViewPager";
    private static final boolean USE_CACHE = false;
    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            t -= 1.0f;
            return ((((t * t) * t) * t) * t) + 1.0f;
        }
    };
    private static final ViewPositionComparator sPositionComparator = new ViewPositionComparator();
    private int mActivePointerId;
    private PagerAdapter mAdapter;
    private OnAdapterChangeListener mAdapterChangeListener;
    private int mBottomPageBounds;
    private boolean mCalledSuper;
    private int mChildHeightMeasureSpec;
    private int mChildWidthMeasureSpec;
    private final int mCloseEnough;
    private int mCurItem;
    private int mDecorChildCount;
    private final int mDefaultGutterSize;
    private int mDrawingOrder;
    private ArrayList<View> mDrawingOrderedChildren;
    private final Runnable mEndScrollRunnable;
    private int mExpectedAdapterCount;
    private boolean mFirstLayout;
    private float mFirstOffset;
    private final int mFlingDistance;
    private int mGutterSize;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private OnPageChangeListener mInternalPageChangeListener;
    private boolean mIsBeingDragged;
    private boolean mIsUnableToDrag;
    private final ArrayList<ItemInfo> mItems;
    private float mLastMotionX;
    private float mLastMotionY;
    private float mLastOffset;
    private final EdgeEffect mLeftEdge;
    private int mLeftIncr;
    private Drawable mMarginDrawable;
    private final int mMaximumVelocity;
    private final int mMinimumVelocity;
    private PagerObserver mObserver;
    private int mOffscreenPageLimit;
    private OnPageChangeListener mOnPageChangeListener;
    private int mPageMargin;
    private PageTransformer mPageTransformer;
    private boolean mPopulatePending;
    private Parcelable mRestoredAdapterState;
    private ClassLoader mRestoredClassLoader;
    private int mRestoredCurItem;
    private final EdgeEffect mRightEdge;
    private int mScrollState;
    private final Scroller mScroller;
    private boolean mScrollingCacheEnabled;
    private final ItemInfo mTempItem;
    private final Rect mTempRect;
    private int mTopPageBounds;
    private final int mTouchSlop;
    private VelocityTracker mVelocityTracker;

    public interface OnPageChangeListener {
        @UnsupportedAppUsage
        void onPageScrollStateChanged(int i);

        @UnsupportedAppUsage
        void onPageScrolled(int i, float f, int i2);

        @UnsupportedAppUsage
        void onPageSelected(int i);
    }

    interface Decor {
    }

    static class ItemInfo {
        Object object;
        float offset;
        int position;
        boolean scrolling;
        float widthFactor;

        ItemInfo() {
        }
    }

    public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
        int childIndex;
        public int gravity;
        public boolean isDecor;
        boolean needsMeasure;
        int position;
        float widthFactor = 0.0f;

        public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<LayoutParams> {
            private int mLayout_gravityId;
            private boolean mPropertiesMapped = false;

            public void mapProperties(PropertyMapper propertyMapper) {
                this.mLayout_gravityId = propertyMapper.mapGravity("layout_gravity", 16842931);
                this.mPropertiesMapped = true;
            }

            public void readProperties(LayoutParams node, PropertyReader propertyReader) {
                if (this.mPropertiesMapped) {
                    propertyReader.readGravity(this.mLayout_gravityId, node.gravity);
                    return;
                }
                throw new UninitializedPropertyMapException();
            }
        }

        public LayoutParams() {
            super(-1, -1);
        }

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs, ViewPager.LAYOUT_ATTRS);
            this.gravity = a.getInteger(0, 48);
            a.recycle();
        }
    }

    interface OnAdapterChangeListener {
        void onAdapterChanged(PagerAdapter pagerAdapter, PagerAdapter pagerAdapter2);
    }

    public interface PageTransformer {
        void transformPage(View view, float f);
    }

    private class PagerObserver extends DataSetObserver {
        private PagerObserver() {
        }

        /* synthetic */ PagerObserver(ViewPager x0, AnonymousClass1 x1) {
            this();
        }

        public void onChanged() {
            ViewPager.this.dataSetChanged();
        }

        public void onInvalidated() {
            ViewPager.this.dataSetChanged();
        }
    }

    public static class SavedState extends AbsSavedState {
        public static final Creator<SavedState> CREATOR = new ClassLoaderCreator<SavedState>() {
            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, null);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        Parcelable adapterState;
        ClassLoader loader;
        int position;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.position);
            out.writeParcelable(this.adapterState, flags);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("FragmentPager.SavedState{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
            stringBuilder.append(" position=");
            stringBuilder.append(this.position);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

        SavedState(Parcel in, ClassLoader loader) {
            super(in, loader);
            if (loader == null) {
                loader = getClass().getClassLoader();
            }
            this.position = in.readInt();
            this.adapterState = in.readParcelable(loader);
            this.loader = loader;
        }
    }

    public static class SimpleOnPageChangeListener implements OnPageChangeListener {
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
        }

        public void onPageScrollStateChanged(int state) {
        }
    }

    static class ViewPositionComparator implements Comparator<View> {
        ViewPositionComparator() {
        }

        public int compare(View lhs, View rhs) {
            LayoutParams llp = (LayoutParams) lhs.getLayoutParams();
            LayoutParams rlp = (LayoutParams) rhs.getLayoutParams();
            if (llp.isDecor == rlp.isDecor) {
                return llp.position - rlp.position;
            }
            return llp.isDecor ? 1 : -1;
        }
    }

    public ViewPager(Context context) {
        this(context, null);
    }

    public ViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ViewPager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mItems = new ArrayList();
        this.mTempItem = new ItemInfo();
        this.mTempRect = new Rect();
        this.mRestoredCurItem = -1;
        this.mRestoredAdapterState = null;
        this.mRestoredClassLoader = null;
        this.mLeftIncr = -1;
        this.mFirstOffset = -3.4028235E38f;
        this.mLastOffset = Float.MAX_VALUE;
        this.mOffscreenPageLimit = 1;
        this.mActivePointerId = -1;
        this.mFirstLayout = true;
        this.mEndScrollRunnable = new Runnable() {
            public void run() {
                ViewPager.this.setScrollState(0);
                ViewPager.this.populate();
            }
        };
        this.mScrollState = 0;
        setWillNotDraw(false);
        setDescendantFocusability(262144);
        setFocusable(true);
        this.mScroller = new Scroller(context, sInterpolator);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        float density = context.getResources().getDisplayMetrics().density;
        this.mTouchSlop = configuration.getScaledPagingTouchSlop();
        this.mMinimumVelocity = (int) (400.0f * density);
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        this.mLeftEdge = new EdgeEffect(context);
        this.mRightEdge = new EdgeEffect(context);
        this.mFlingDistance = (int) (25.0f * density);
        this.mCloseEnough = (int) (2.0f * density);
        this.mDefaultGutterSize = (int) (16.0f * density);
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        removeCallbacks(this.mEndScrollRunnable);
        super.onDetachedFromWindow();
    }

    private void setScrollState(int newState) {
        if (this.mScrollState != newState) {
            this.mScrollState = newState;
            if (this.mPageTransformer != null) {
                enableLayers(newState != 0);
            }
            OnPageChangeListener onPageChangeListener = this.mOnPageChangeListener;
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageScrollStateChanged(newState);
            }
        }
    }

    public void setAdapter(PagerAdapter adapter) {
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter != null) {
            pagerAdapter.unregisterDataSetObserver(this.mObserver);
            this.mAdapter.startUpdate((ViewGroup) this);
            for (int i = 0; i < this.mItems.size(); i++) {
                ItemInfo ii = (ItemInfo) this.mItems.get(i);
                this.mAdapter.destroyItem((ViewGroup) this, ii.position, ii.object);
            }
            this.mAdapter.finishUpdate((ViewGroup) this);
            this.mItems.clear();
            removeNonDecorViews();
            this.mCurItem = 0;
            scrollTo(0, 0);
        }
        pagerAdapter = this.mAdapter;
        this.mAdapter = adapter;
        this.mExpectedAdapterCount = 0;
        if (this.mAdapter != null) {
            if (this.mObserver == null) {
                this.mObserver = new PagerObserver(this, null);
            }
            this.mAdapter.registerDataSetObserver(this.mObserver);
            this.mPopulatePending = false;
            boolean wasFirstLayout = this.mFirstLayout;
            this.mFirstLayout = true;
            this.mExpectedAdapterCount = this.mAdapter.getCount();
            if (this.mRestoredCurItem >= 0) {
                this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
                setCurrentItemInternal(this.mRestoredCurItem, false, true);
                this.mRestoredCurItem = -1;
                this.mRestoredAdapterState = null;
                this.mRestoredClassLoader = null;
            } else if (wasFirstLayout) {
                requestLayout();
            } else {
                populate();
            }
        }
        OnAdapterChangeListener onAdapterChangeListener = this.mAdapterChangeListener;
        if (onAdapterChangeListener != null && pagerAdapter != adapter) {
            onAdapterChangeListener.onAdapterChanged(pagerAdapter, adapter);
        }
    }

    private void removeNonDecorViews() {
        int i = 0;
        while (i < getChildCount()) {
            if (!((LayoutParams) getChildAt(i).getLayoutParams()).isDecor) {
                removeViewAt(i);
                i--;
            }
            i++;
        }
    }

    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }

    /* Access modifiers changed, original: 0000 */
    public void setOnAdapterChangeListener(OnAdapterChangeListener listener) {
        this.mAdapterChangeListener = listener;
    }

    private int getPaddedWidth() {
        return (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
    }

    public void setCurrentItem(int item) {
        this.mPopulatePending = false;
        setCurrentItemInternal(item, this.mFirstLayout ^ 1, false);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        this.mPopulatePending = false;
        setCurrentItemInternal(item, smoothScroll, false);
    }

    @UnsupportedAppUsage
    public int getCurrentItem() {
        return this.mCurItem;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean setCurrentItemInternal(int item, boolean smoothScroll, boolean always) {
        return setCurrentItemInternal(item, smoothScroll, always, 0);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean setCurrentItemInternal(int item, boolean smoothScroll, boolean always, int velocity) {
        PagerAdapter pagerAdapter = this.mAdapter;
        boolean dispatchSelected = false;
        if (pagerAdapter == null || pagerAdapter.getCount() <= 0) {
            setScrollingCacheEnabled(false);
            return false;
        }
        item = MathUtils.constrain(item, 0, this.mAdapter.getCount() - 1);
        if (always || this.mCurItem != item || this.mItems.size() == 0) {
            int pageLimit = this.mOffscreenPageLimit;
            int i = this.mCurItem;
            if (item > i + pageLimit || item < i - pageLimit) {
                for (i = 0; i < this.mItems.size(); i++) {
                    ((ItemInfo) this.mItems.get(i)).scrolling = true;
                }
            }
            if (this.mCurItem != item) {
                dispatchSelected = true;
            }
            if (this.mFirstLayout) {
                OnPageChangeListener onPageChangeListener;
                this.mCurItem = item;
                if (dispatchSelected) {
                    onPageChangeListener = this.mOnPageChangeListener;
                    if (onPageChangeListener != null) {
                        onPageChangeListener.onPageSelected(item);
                    }
                }
                if (dispatchSelected) {
                    onPageChangeListener = this.mInternalPageChangeListener;
                    if (onPageChangeListener != null) {
                        onPageChangeListener.onPageSelected(item);
                    }
                }
                requestLayout();
            } else {
                populate(item);
                scrollToItem(item, smoothScroll, velocity, dispatchSelected);
            }
            return true;
        }
        setScrollingCacheEnabled(false);
        return false;
    }

    private void scrollToItem(int position, boolean smoothScroll, int velocity, boolean dispatchSelected) {
        int destX = getLeftEdgeForItem(position);
        if (smoothScroll) {
            OnPageChangeListener onPageChangeListener;
            smoothScrollTo(destX, 0, velocity);
            if (dispatchSelected) {
                onPageChangeListener = this.mOnPageChangeListener;
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageSelected(position);
                }
            }
            if (dispatchSelected) {
                onPageChangeListener = this.mInternalPageChangeListener;
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageSelected(position);
                    return;
                }
                return;
            }
            return;
        }
        OnPageChangeListener onPageChangeListener2;
        if (dispatchSelected) {
            onPageChangeListener2 = this.mOnPageChangeListener;
            if (onPageChangeListener2 != null) {
                onPageChangeListener2.onPageSelected(position);
            }
        }
        if (dispatchSelected) {
            onPageChangeListener2 = this.mInternalPageChangeListener;
            if (onPageChangeListener2 != null) {
                onPageChangeListener2.onPageSelected(position);
            }
        }
        completeScroll(false);
        scrollTo(destX, 0);
        pageScrolled(destX);
    }

    private int getLeftEdgeForItem(int position) {
        ItemInfo info = infoForPosition(position);
        if (info == null) {
            return 0;
        }
        int width = getPaddedWidth();
        int scaledOffset = (int) (((float) width) * MathUtils.constrain(info.offset, this.mFirstOffset, this.mLastOffset));
        if (isLayoutRtl()) {
            return (16777216 - ((int) ((((float) width) * info.widthFactor) + 1056964608))) - scaledOffset;
        }
        return scaledOffset;
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mOnPageChangeListener = listener;
    }

    public void setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer) {
        int i = 1;
        boolean hasTransformer = transformer != null;
        boolean needsPopulate = hasTransformer != (this.mPageTransformer != null);
        this.mPageTransformer = transformer;
        setChildrenDrawingOrderEnabled(hasTransformer);
        if (hasTransformer) {
            if (reverseDrawingOrder) {
                i = 2;
            }
            this.mDrawingOrder = i;
        } else {
            this.mDrawingOrder = 0;
        }
        if (needsPopulate) {
            populate();
        }
    }

    /* Access modifiers changed, original: protected */
    public int getChildDrawingOrder(int childCount, int i) {
        return ((LayoutParams) ((View) this.mDrawingOrderedChildren.get(this.mDrawingOrder == 2 ? (childCount - 1) - i : i)).getLayoutParams()).childIndex;
    }

    /* Access modifiers changed, original: 0000 */
    public OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener listener) {
        OnPageChangeListener oldListener = this.mInternalPageChangeListener;
        this.mInternalPageChangeListener = listener;
        return oldListener;
    }

    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }

    public void setOffscreenPageLimit(int limit) {
        if (limit < 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Requested offscreen page limit ");
            stringBuilder.append(limit);
            stringBuilder.append(" too small; defaulting to ");
            stringBuilder.append(1);
            Log.w(TAG, stringBuilder.toString());
            limit = 1;
        }
        if (limit != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = limit;
            populate();
        }
    }

    public void setPageMargin(int marginPixels) {
        int oldMargin = this.mPageMargin;
        this.mPageMargin = marginPixels;
        int width = getWidth();
        recomputeScrollPosition(width, width, marginPixels, oldMargin);
        requestLayout();
    }

    public int getPageMargin() {
        return this.mPageMargin;
    }

    public void setPageMarginDrawable(Drawable d) {
        this.mMarginDrawable = d;
        if (d != null) {
            refreshDrawableState();
        }
        setWillNotDraw(d == null);
        invalidate();
    }

    public void setPageMarginDrawable(int resId) {
        setPageMarginDrawable(getContext().getDrawable(resId));
    }

    /* Access modifiers changed, original: protected */
    public boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == this.mMarginDrawable;
    }

    /* Access modifiers changed, original: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable marginDrawable = this.mMarginDrawable;
        if (marginDrawable != null && marginDrawable.isStateful() && marginDrawable.setState(getDrawableState())) {
            invalidateDrawable(marginDrawable);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public float distanceInfluenceForSnapDuration(float f) {
        return (float) Math.sin((double) ((float) (((double) (f - 0.5f)) * 0.4712389167638204d)));
    }

    /* Access modifiers changed, original: 0000 */
    public void smoothScrollTo(int x, int y) {
        smoothScrollTo(x, y, 0);
    }

    /* Access modifiers changed, original: 0000 */
    public void smoothScrollTo(int x, int y, int velocity) {
        if (getChildCount() == 0) {
            setScrollingCacheEnabled(false);
            return;
        }
        int sx = getScrollX();
        int sy = getScrollY();
        int dx = x - sx;
        int dy = y - sy;
        if (dx == 0 && dy == 0) {
            completeScroll(false);
            populate();
            setScrollState(0);
            return;
        }
        int duration;
        setScrollingCacheEnabled(true);
        setScrollState(2);
        int width = getPaddedWidth();
        int halfWidth = width / 2;
        float distance = ((float) halfWidth) + (((float) halfWidth) * distanceInfluenceForSnapDuration(Math.min(1.0f, (((float) Math.abs(dx)) * 1.0f) / ((float) width))));
        int velocity2 = Math.abs(velocity);
        if (velocity2 > 0) {
            duration = Math.round(Math.abs(distance / ((float) velocity2)) * 1000.0f) * 4;
        } else {
            duration = (int) ((1.0f + (((float) Math.abs(dx)) / (((float) this.mPageMargin) + (((float) width) * this.mAdapter.getPageWidth(this.mCurItem))))) * 100.0f);
        }
        this.mScroller.startScroll(sx, sy, dx, dy, Math.min(duration, 600));
        postInvalidateOnAnimation();
    }

    /* Access modifiers changed, original: 0000 */
    public ItemInfo addNewItem(int position, int index) {
        ItemInfo ii = new ItemInfo();
        ii.position = position;
        ii.object = this.mAdapter.instantiateItem((ViewGroup) this, position);
        ii.widthFactor = this.mAdapter.getPageWidth(position);
        if (index < 0 || index >= this.mItems.size()) {
            this.mItems.add(ii);
        } else {
            this.mItems.add(index, ii);
        }
        return ii;
    }

    /* Access modifiers changed, original: 0000 */
    public void dataSetChanged() {
        int adapterCount = this.mAdapter.getCount();
        this.mExpectedAdapterCount = adapterCount;
        boolean needPopulate = this.mItems.size() < (this.mOffscreenPageLimit * 2) + 1 && this.mItems.size() < adapterCount;
        int newCurrItem = this.mCurItem;
        boolean isUpdating = false;
        int i = 0;
        while (i < this.mItems.size()) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            int newPos = this.mAdapter.getItemPosition(ii.object);
            if (newPos != -1) {
                if (newPos == -2) {
                    this.mItems.remove(i);
                    i--;
                    if (!isUpdating) {
                        this.mAdapter.startUpdate((ViewGroup) this);
                        isUpdating = true;
                    }
                    this.mAdapter.destroyItem((ViewGroup) this, ii.position, ii.object);
                    needPopulate = true;
                    if (this.mCurItem == ii.position) {
                        newCurrItem = Math.max(0, Math.min(this.mCurItem, adapterCount - 1));
                        needPopulate = true;
                    }
                } else if (ii.position != newPos) {
                    if (ii.position == this.mCurItem) {
                        newCurrItem = newPos;
                    }
                    ii.position = newPos;
                    needPopulate = true;
                }
            }
            i++;
        }
        if (isUpdating) {
            this.mAdapter.finishUpdate((ViewGroup) this);
        }
        Collections.sort(this.mItems, COMPARATOR);
        if (needPopulate) {
            i = getChildCount();
            for (int i2 = 0; i2 < i; i2++) {
                LayoutParams lp = (LayoutParams) getChildAt(i2).getLayoutParams();
                if (!lp.isDecor) {
                    lp.widthFactor = 0.0f;
                }
            }
            setCurrentItemInternal(newCurrItem, false, true);
            requestLayout();
        }
    }

    public void populate() {
        populate(this.mCurItem);
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:118:0x01ed  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:122:0x01fc  */
    /* JADX WARNING: Removed duplicated region for block: B:121:0x01f9  */
    /* JADX WARNING: Removed duplicated region for block: B:125:0x020c  */
    /* JADX WARNING: Removed duplicated region for block: B:136:0x023f  */
    public void populate(int r22) {
        /*
        r21 = this;
        r1 = r21;
        r2 = r22;
        r0 = 0;
        r3 = 2;
        r4 = r1.mCurItem;
        if (r4 == r2) goto L_0x001d;
    L_0x000a:
        if (r4 >= r2) goto L_0x000f;
    L_0x000c:
        r4 = 66;
        goto L_0x0011;
    L_0x000f:
        r4 = 17;
    L_0x0011:
        r3 = r4;
        r4 = r1.mCurItem;
        r0 = r1.infoForPosition(r4);
        r1.mCurItem = r2;
        r4 = r3;
        r3 = r0;
        goto L_0x001f;
    L_0x001d:
        r4 = r3;
        r3 = r0;
    L_0x001f:
        r0 = r1.mAdapter;
        if (r0 != 0) goto L_0x0027;
    L_0x0023:
        r21.sortChildDrawingOrder();
        return;
    L_0x0027:
        r0 = r1.mPopulatePending;
        if (r0 == 0) goto L_0x002f;
    L_0x002b:
        r21.sortChildDrawingOrder();
        return;
    L_0x002f:
        r0 = r21.getWindowToken();
        if (r0 != 0) goto L_0x0036;
    L_0x0035:
        return;
    L_0x0036:
        r0 = r1.mAdapter;
        r0.startUpdate(r1);
        r5 = r1.mOffscreenPageLimit;
        r0 = 0;
        r6 = r1.mCurItem;
        r6 = r6 - r5;
        r6 = java.lang.Math.max(r0, r6);
        r0 = r1.mAdapter;
        r7 = r0.getCount();
        r0 = r7 + -1;
        r8 = r1.mCurItem;
        r8 = r8 + r5;
        r8 = java.lang.Math.min(r0, r8);
        r0 = r1.mExpectedAdapterCount;
        if (r7 != r0) goto L_0x028f;
    L_0x0058:
        r0 = -1;
        r9 = 0;
        r0 = 0;
    L_0x005b:
        r10 = r1.mItems;
        r10 = r10.size();
        if (r0 >= r10) goto L_0x007c;
    L_0x0063:
        r10 = r1.mItems;
        r10 = r10.get(r0);
        r10 = (com.android.internal.widget.ViewPager.ItemInfo) r10;
        r11 = r10.position;
        r12 = r1.mCurItem;
        if (r11 < r12) goto L_0x0079;
    L_0x0071:
        r11 = r10.position;
        r12 = r1.mCurItem;
        if (r11 != r12) goto L_0x007c;
    L_0x0077:
        r9 = r10;
        goto L_0x007c;
    L_0x0079:
        r0 = r0 + 1;
        goto L_0x005b;
    L_0x007c:
        if (r9 != 0) goto L_0x0086;
    L_0x007e:
        if (r7 <= 0) goto L_0x0086;
    L_0x0080:
        r10 = r1.mCurItem;
        r9 = r1.addNewItem(r10, r0);
    L_0x0086:
        if (r9 == 0) goto L_0x01ed;
    L_0x0088:
        r12 = 0;
        r13 = r0 + -1;
        if (r13 < 0) goto L_0x0096;
    L_0x008d:
        r14 = r1.mItems;
        r14 = r14.get(r13);
        r14 = (com.android.internal.widget.ViewPager.ItemInfo) r14;
        goto L_0x0097;
    L_0x0096:
        r14 = 0;
    L_0x0097:
        r15 = r21.getPaddedWidth();
        r16 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r15 > 0) goto L_0x00a3;
    L_0x009f:
        r18 = r0;
        r10 = 0;
        goto L_0x00b1;
    L_0x00a3:
        r11 = r9.widthFactor;
        r11 = r16 - r11;
        r10 = r21.getPaddingLeft();
        r10 = (float) r10;
        r18 = r0;
        r0 = (float) r15;
        r10 = r10 / r0;
        r10 = r10 + r11;
    L_0x00b1:
        r0 = r10;
        r10 = r1.mCurItem;
        r10 = r10 + -1;
        r11 = r18;
    L_0x00b8:
        if (r10 < 0) goto L_0x0129;
    L_0x00ba:
        r18 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1));
        if (r18 < 0) goto L_0x00ee;
    L_0x00be:
        if (r10 >= r6) goto L_0x00ee;
    L_0x00c0:
        if (r14 != 0) goto L_0x00c6;
    L_0x00c2:
        r19 = r0;
        goto L_0x012b;
    L_0x00c6:
        r19 = r0;
        r0 = r14.position;
        if (r10 != r0) goto L_0x0122;
    L_0x00cc:
        r0 = r14.scrolling;
        if (r0 != 0) goto L_0x0122;
    L_0x00d0:
        r0 = r1.mItems;
        r0.remove(r13);
        r0 = r1.mAdapter;
        r2 = r14.object;
        r0.destroyItem(r1, r10, r2);
        r13 = r13 + -1;
        r11 = r11 + -1;
        if (r13 < 0) goto L_0x00eb;
    L_0x00e2:
        r0 = r1.mItems;
        r0 = r0.get(r13);
        r0 = (com.android.internal.widget.ViewPager.ItemInfo) r0;
        goto L_0x00ec;
    L_0x00eb:
        r0 = 0;
    L_0x00ec:
        r14 = r0;
        goto L_0x0122;
    L_0x00ee:
        r19 = r0;
        if (r14 == 0) goto L_0x0109;
    L_0x00f2:
        r0 = r14.position;
        if (r10 != r0) goto L_0x0109;
    L_0x00f6:
        r0 = r14.widthFactor;
        r12 = r12 + r0;
        r13 = r13 + -1;
        if (r13 < 0) goto L_0x0106;
    L_0x00fd:
        r0 = r1.mItems;
        r0 = r0.get(r13);
        r0 = (com.android.internal.widget.ViewPager.ItemInfo) r0;
        goto L_0x0107;
    L_0x0106:
        r0 = 0;
    L_0x0107:
        r14 = r0;
        goto L_0x0122;
    L_0x0109:
        r0 = r13 + 1;
        r0 = r1.addNewItem(r10, r0);
        r2 = r0.widthFactor;
        r12 = r12 + r2;
        r11 = r11 + 1;
        if (r13 < 0) goto L_0x011f;
    L_0x0116:
        r2 = r1.mItems;
        r2 = r2.get(r13);
        r2 = (com.android.internal.widget.ViewPager.ItemInfo) r2;
        goto L_0x0120;
    L_0x011f:
        r2 = 0;
    L_0x0120:
        r0 = r2;
        r14 = r0;
    L_0x0122:
        r10 = r10 + -1;
        r2 = r22;
        r0 = r19;
        goto L_0x00b8;
    L_0x0129:
        r19 = r0;
    L_0x012b:
        r0 = r9.widthFactor;
        r2 = r11 + 1;
        r10 = (r0 > r16 ? 1 : (r0 == r16 ? 0 : -1));
        if (r10 >= 0) goto L_0x01e3;
    L_0x0133:
        r10 = r1.mItems;
        r10 = r10.size();
        if (r2 >= r10) goto L_0x0144;
    L_0x013b:
        r10 = r1.mItems;
        r10 = r10.get(r2);
        r10 = (com.android.internal.widget.ViewPager.ItemInfo) r10;
        goto L_0x0145;
    L_0x0144:
        r10 = 0;
    L_0x0145:
        if (r15 > 0) goto L_0x0149;
    L_0x0147:
        r13 = 0;
        goto L_0x0152;
    L_0x0149:
        r13 = r21.getPaddingRight();
        r13 = (float) r13;
        r14 = (float) r15;
        r13 = r13 / r14;
        r13 = r13 + r16;
        r14 = r1.mCurItem;
        r14 = r14 + 1;
    L_0x0157:
        if (r14 >= r7) goto L_0x01dd;
    L_0x0159:
        r16 = (r0 > r13 ? 1 : (r0 == r13 ? 0 : -1));
        if (r16 < 0) goto L_0x0196;
    L_0x015d:
        if (r14 <= r8) goto L_0x0196;
    L_0x015f:
        if (r10 != 0) goto L_0x0167;
    L_0x0161:
        r16 = r5;
        r20 = r6;
        goto L_0x01e1;
    L_0x0167:
        r16 = r5;
        r5 = r10.position;
        if (r14 != r5) goto L_0x0193;
    L_0x016d:
        r5 = r10.scrolling;
        if (r5 != 0) goto L_0x0193;
    L_0x0171:
        r5 = r1.mItems;
        r5.remove(r2);
        r5 = r1.mAdapter;
        r20 = r6;
        r6 = r10.object;
        r5.destroyItem(r1, r14, r6);
        r5 = r1.mItems;
        r5 = r5.size();
        if (r2 >= r5) goto L_0x0190;
    L_0x0187:
        r5 = r1.mItems;
        r5 = r5.get(r2);
        r5 = (com.android.internal.widget.ViewPager.ItemInfo) r5;
        goto L_0x0191;
    L_0x0190:
        r5 = 0;
    L_0x0191:
        r10 = r5;
        goto L_0x01d5;
    L_0x0193:
        r20 = r6;
        goto L_0x01d5;
    L_0x0196:
        r16 = r5;
        r20 = r6;
        if (r10 == 0) goto L_0x01b9;
    L_0x019c:
        r5 = r10.position;
        if (r14 != r5) goto L_0x01b9;
    L_0x01a0:
        r5 = r10.widthFactor;
        r0 = r0 + r5;
        r2 = r2 + 1;
        r5 = r1.mItems;
        r5 = r5.size();
        if (r2 >= r5) goto L_0x01b6;
    L_0x01ad:
        r5 = r1.mItems;
        r5 = r5.get(r2);
        r5 = (com.android.internal.widget.ViewPager.ItemInfo) r5;
        goto L_0x01b7;
    L_0x01b6:
        r5 = 0;
    L_0x01b7:
        r10 = r5;
        goto L_0x01d5;
    L_0x01b9:
        r5 = r1.addNewItem(r14, r2);
        r2 = r2 + 1;
        r6 = r5.widthFactor;
        r0 = r0 + r6;
        r6 = r1.mItems;
        r6 = r6.size();
        if (r2 >= r6) goto L_0x01d3;
    L_0x01ca:
        r6 = r1.mItems;
        r6 = r6.get(r2);
        r6 = (com.android.internal.widget.ViewPager.ItemInfo) r6;
        goto L_0x01d4;
    L_0x01d3:
        r6 = 0;
    L_0x01d4:
        r10 = r6;
    L_0x01d5:
        r14 = r14 + 1;
        r5 = r16;
        r6 = r20;
        goto L_0x0157;
    L_0x01dd:
        r16 = r5;
        r20 = r6;
    L_0x01e1:
        r14 = r10;
        goto L_0x01e7;
    L_0x01e3:
        r16 = r5;
        r20 = r6;
    L_0x01e7:
        r1.calculatePageOffsets(r9, r11, r3);
        r18 = r11;
        goto L_0x01f3;
    L_0x01ed:
        r18 = r0;
        r16 = r5;
        r20 = r6;
    L_0x01f3:
        r0 = r1.mAdapter;
        r2 = r1.mCurItem;
        if (r9 == 0) goto L_0x01fc;
    L_0x01f9:
        r11 = r9.object;
        goto L_0x01fd;
    L_0x01fc:
        r11 = 0;
    L_0x01fd:
        r0.setPrimaryItem(r1, r2, r11);
        r0 = r1.mAdapter;
        r0.finishUpdate(r1);
        r0 = r21.getChildCount();
        r2 = 0;
    L_0x020a:
        if (r2 >= r0) goto L_0x0236;
    L_0x020c:
        r5 = r1.getChildAt(r2);
        r6 = r5.getLayoutParams();
        r6 = (com.android.internal.widget.ViewPager.LayoutParams) r6;
        r6.childIndex = r2;
        r10 = r6.isDecor;
        if (r10 != 0) goto L_0x0232;
    L_0x021c:
        r10 = r6.widthFactor;
        r11 = 0;
        r10 = (r10 > r11 ? 1 : (r10 == r11 ? 0 : -1));
        if (r10 != 0) goto L_0x0233;
    L_0x0223:
        r10 = r1.infoForChild(r5);
        if (r10 == 0) goto L_0x0233;
    L_0x0229:
        r12 = r10.widthFactor;
        r6.widthFactor = r12;
        r12 = r10.position;
        r6.position = r12;
        goto L_0x0233;
    L_0x0232:
        r11 = 0;
    L_0x0233:
        r2 = r2 + 1;
        goto L_0x020a;
    L_0x0236:
        r21.sortChildDrawingOrder();
        r2 = r21.hasFocus();
        if (r2 == 0) goto L_0x028e;
    L_0x023f:
        r2 = r21.findFocus();
        if (r2 == 0) goto L_0x024c;
    L_0x0245:
        r11 = r1.infoForAnyChild(r2);
        r17 = r11;
        goto L_0x024e;
    L_0x024c:
        r17 = 0;
    L_0x024e:
        r5 = r17;
        if (r5 == 0) goto L_0x0258;
    L_0x0252:
        r6 = r5.position;
        r10 = r1.mCurItem;
        if (r6 == r10) goto L_0x028e;
    L_0x0258:
        r6 = 0;
    L_0x0259:
        r10 = r21.getChildCount();
        if (r6 >= r10) goto L_0x028e;
    L_0x025f:
        r10 = r1.getChildAt(r6);
        r5 = r1.infoForChild(r10);
        if (r5 == 0) goto L_0x028b;
    L_0x0269:
        r11 = r5.position;
        r12 = r1.mCurItem;
        if (r11 != r12) goto L_0x028b;
    L_0x026f:
        if (r2 != 0) goto L_0x0273;
    L_0x0271:
        r11 = 0;
        goto L_0x0284;
    L_0x0273:
        r11 = r1.mTempRect;
        r12 = r1.mTempRect;
        r2.getFocusedRect(r12);
        r12 = r1.mTempRect;
        r1.offsetDescendantRectToMyCoords(r2, r12);
        r12 = r1.mTempRect;
        r1.offsetRectIntoDescendantCoords(r10, r12);
    L_0x0284:
        r12 = r10.requestFocus(r4, r11);
        if (r12 == 0) goto L_0x028b;
    L_0x028a:
        goto L_0x028e;
    L_0x028b:
        r6 = r6 + 1;
        goto L_0x0259;
    L_0x028e:
        return;
    L_0x028f:
        r16 = r5;
        r20 = r6;
        r0 = r21.getResources();	 Catch:{ NotFoundException -> 0x02a0 }
        r2 = r21.getId();	 Catch:{ NotFoundException -> 0x02a0 }
        r0 = r0.getResourceName(r2);	 Catch:{ NotFoundException -> 0x02a0 }
        goto L_0x02a9;
    L_0x02a0:
        r0 = move-exception;
        r2 = r21.getId();
        r0 = java.lang.Integer.toHexString(r2);
    L_0x02a9:
        r2 = new java.lang.IllegalStateException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: ";
        r5.append(r6);
        r6 = r1.mExpectedAdapterCount;
        r5.append(r6);
        r6 = ", found: ";
        r5.append(r6);
        r5.append(r7);
        r6 = " Pager id: ";
        r5.append(r6);
        r5.append(r0);
        r6 = " Pager class: ";
        r5.append(r6);
        r6 = r21.getClass();
        r5.append(r6);
        r6 = " Problematic adapter: ";
        r5.append(r6);
        r6 = r1.mAdapter;
        r6 = r6.getClass();
        r5.append(r6);
        r5 = r5.toString();
        r2.<init>(r5);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.widget.ViewPager.populate(int):void");
    }

    private void sortChildDrawingOrder() {
        if (this.mDrawingOrder != 0) {
            ArrayList arrayList = this.mDrawingOrderedChildren;
            if (arrayList == null) {
                this.mDrawingOrderedChildren = new ArrayList();
            } else {
                arrayList.clear();
            }
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                this.mDrawingOrderedChildren.add(getChildAt(i));
            }
            Collections.sort(this.mDrawingOrderedChildren, sPositionComparator);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0056 A:{LOOP_END, LOOP:2: B:18:0x0052->B:20:0x0056} */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x009e A:{LOOP_END, LOOP:5: B:33:0x009a->B:35:0x009e} */
    private void calculatePageOffsets(com.android.internal.widget.ViewPager.ItemInfo r12, int r13, com.android.internal.widget.ViewPager.ItemInfo r14) {
        /*
        r11 = this;
        r0 = r11.mAdapter;
        r0 = r0.getCount();
        r1 = r11.getPaddedWidth();
        if (r1 <= 0) goto L_0x0012;
    L_0x000c:
        r2 = r11.mPageMargin;
        r2 = (float) r2;
        r3 = (float) r1;
        r2 = r2 / r3;
        goto L_0x0013;
    L_0x0012:
        r2 = 0;
    L_0x0013:
        if (r14 == 0) goto L_0x00b2;
    L_0x0015:
        r3 = r14.position;
        r4 = r12.position;
        if (r3 >= r4) goto L_0x006a;
    L_0x001b:
        r4 = 0;
        r5 = r14.offset;
        r6 = r14.widthFactor;
        r5 = r5 + r6;
        r5 = r5 + r2;
        r6 = r3 + 1;
    L_0x0024:
        r7 = r12.position;
        if (r6 > r7) goto L_0x00b2;
    L_0x0028:
        r7 = r11.mItems;
        r7 = r7.size();
        if (r4 >= r7) goto L_0x00b2;
    L_0x0030:
        r7 = r11.mItems;
        r7 = r7.get(r4);
        r7 = (com.android.internal.widget.ViewPager.ItemInfo) r7;
    L_0x0038:
        r8 = r7.position;
        if (r6 <= r8) goto L_0x0052;
    L_0x003c:
        r8 = r11.mItems;
        r8 = r8.size();
        r8 = r8 + -1;
        if (r4 >= r8) goto L_0x0052;
    L_0x0046:
        r4 = r4 + 1;
        r8 = r11.mItems;
        r8 = r8.get(r4);
        r7 = r8;
        r7 = (com.android.internal.widget.ViewPager.ItemInfo) r7;
        goto L_0x0038;
    L_0x0052:
        r8 = r7.position;
        if (r6 >= r8) goto L_0x0061;
    L_0x0056:
        r8 = r11.mAdapter;
        r8 = r8.getPageWidth(r6);
        r8 = r8 + r2;
        r5 = r5 + r8;
        r6 = r6 + 1;
        goto L_0x0052;
    L_0x0061:
        r7.offset = r5;
        r8 = r7.widthFactor;
        r8 = r8 + r2;
        r5 = r5 + r8;
        r6 = r6 + 1;
        goto L_0x0024;
    L_0x006a:
        r4 = r12.position;
        if (r3 <= r4) goto L_0x00b2;
    L_0x006e:
        r4 = r11.mItems;
        r4 = r4.size();
        r4 = r4 + -1;
        r5 = r14.offset;
        r6 = r3 + -1;
    L_0x007a:
        r7 = r12.position;
        if (r6 < r7) goto L_0x00b2;
    L_0x007e:
        if (r4 < 0) goto L_0x00b2;
    L_0x0080:
        r7 = r11.mItems;
        r7 = r7.get(r4);
        r7 = (com.android.internal.widget.ViewPager.ItemInfo) r7;
    L_0x0088:
        r8 = r7.position;
        if (r6 >= r8) goto L_0x009a;
    L_0x008c:
        if (r4 <= 0) goto L_0x009a;
    L_0x008e:
        r4 = r4 + -1;
        r8 = r11.mItems;
        r8 = r8.get(r4);
        r7 = r8;
        r7 = (com.android.internal.widget.ViewPager.ItemInfo) r7;
        goto L_0x0088;
    L_0x009a:
        r8 = r7.position;
        if (r6 <= r8) goto L_0x00a9;
    L_0x009e:
        r8 = r11.mAdapter;
        r8 = r8.getPageWidth(r6);
        r8 = r8 + r2;
        r5 = r5 - r8;
        r6 = r6 + -1;
        goto L_0x009a;
    L_0x00a9:
        r8 = r7.widthFactor;
        r8 = r8 + r2;
        r5 = r5 - r8;
        r7.offset = r5;
        r6 = r6 + -1;
        goto L_0x007a;
    L_0x00b2:
        r3 = r11.mItems;
        r3 = r3.size();
        r4 = r12.offset;
        r5 = r12.position;
        r5 = r5 + -1;
        r6 = r12.position;
        if (r6 != 0) goto L_0x00c5;
    L_0x00c2:
        r6 = r12.offset;
        goto L_0x00c8;
    L_0x00c5:
        r6 = -8388609; // 0xffffffffff7fffff float:-3.4028235E38 double:NaN;
    L_0x00c8:
        r11.mFirstOffset = r6;
        r6 = r12.position;
        r7 = r0 + -1;
        r8 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        if (r6 != r7) goto L_0x00d9;
    L_0x00d2:
        r6 = r12.offset;
        r7 = r12.widthFactor;
        r6 = r6 + r7;
        r6 = r6 - r8;
        goto L_0x00dc;
    L_0x00d9:
        r6 = 2139095039; // 0x7f7fffff float:3.4028235E38 double:1.056853372E-314;
    L_0x00dc:
        r11.mLastOffset = r6;
        r6 = r13 + -1;
    L_0x00e0:
        if (r6 < 0) goto L_0x010b;
    L_0x00e2:
        r7 = r11.mItems;
        r7 = r7.get(r6);
        r7 = (com.android.internal.widget.ViewPager.ItemInfo) r7;
    L_0x00ea:
        r9 = r7.position;
        if (r5 <= r9) goto L_0x00fa;
    L_0x00ee:
        r9 = r11.mAdapter;
        r10 = r5 + -1;
        r5 = r9.getPageWidth(r5);
        r5 = r5 + r2;
        r4 = r4 - r5;
        r5 = r10;
        goto L_0x00ea;
    L_0x00fa:
        r9 = r7.widthFactor;
        r9 = r9 + r2;
        r4 = r4 - r9;
        r7.offset = r4;
        r9 = r7.position;
        if (r9 != 0) goto L_0x0106;
    L_0x0104:
        r11.mFirstOffset = r4;
    L_0x0106:
        r6 = r6 + -1;
        r5 = r5 + -1;
        goto L_0x00e0;
    L_0x010b:
        r6 = r12.offset;
        r7 = r12.widthFactor;
        r6 = r6 + r7;
        r6 = r6 + r2;
        r4 = r12.position;
        r4 = r4 + 1;
        r5 = r13 + 1;
    L_0x0117:
        if (r5 >= r3) goto L_0x0148;
    L_0x0119:
        r7 = r11.mItems;
        r7 = r7.get(r5);
        r7 = (com.android.internal.widget.ViewPager.ItemInfo) r7;
    L_0x0121:
        r9 = r7.position;
        if (r4 >= r9) goto L_0x0131;
    L_0x0125:
        r9 = r11.mAdapter;
        r10 = r4 + 1;
        r4 = r9.getPageWidth(r4);
        r4 = r4 + r2;
        r6 = r6 + r4;
        r4 = r10;
        goto L_0x0121;
    L_0x0131:
        r9 = r7.position;
        r10 = r0 + -1;
        if (r9 != r10) goto L_0x013d;
    L_0x0137:
        r9 = r7.widthFactor;
        r9 = r9 + r6;
        r9 = r9 - r8;
        r11.mLastOffset = r9;
    L_0x013d:
        r7.offset = r6;
        r9 = r7.widthFactor;
        r9 = r9 + r2;
        r6 = r6 + r9;
        r5 = r5 + 1;
        r4 = r4 + 1;
        goto L_0x0117;
    L_0x0148:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.widget.ViewPager.calculatePageOffsets(com.android.internal.widget.ViewPager$ItemInfo, int, com.android.internal.widget.ViewPager$ItemInfo):void");
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.position = this.mCurItem;
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter != null) {
            ss.adapterState = pagerAdapter.saveState();
        }
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState ss = (SavedState) state;
            super.onRestoreInstanceState(ss.getSuperState());
            PagerAdapter pagerAdapter = this.mAdapter;
            if (pagerAdapter != null) {
                pagerAdapter.restoreState(ss.adapterState, ss.loader);
                setCurrentItemInternal(ss.position, false, true);
            } else {
                this.mRestoredCurItem = ss.position;
                this.mRestoredAdapterState = ss.adapterState;
                this.mRestoredClassLoader = ss.loader;
            }
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        if (!checkLayoutParams(params)) {
            params = generateLayoutParams(params);
        }
        LayoutParams lp = (LayoutParams) params;
        lp.isDecor |= child instanceof Decor;
        if (!this.mInLayout) {
            super.addView(child, index, params);
        } else if (lp.isDecor) {
            throw new IllegalStateException("Cannot add pager decor view during layout");
        } else {
            lp.needsMeasure = true;
            addViewInLayout(child, index, params);
        }
    }

    public Object getCurrent() {
        ItemInfo itemInfo = infoForPosition(getCurrentItem());
        return itemInfo == null ? null : itemInfo.object;
    }

    public void removeView(View view) {
        if (this.mInLayout) {
            removeViewInLayout(view);
        } else {
            super.removeView(view);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public ItemInfo infoForChild(View child) {
        for (int i = 0; i < this.mItems.size(); i++) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            if (this.mAdapter.isViewFromObject(child, ii.object)) {
                return ii;
            }
        }
        return null;
    }

    /* Access modifiers changed, original: 0000 */
    public ItemInfo infoForAnyChild(View child) {
        while (true) {
            Object parent = child.getParent();
            ViewParent parent2 = parent;
            if (parent == this) {
                return infoForChild(child);
            }
            if (parent2 != null && (parent2 instanceof View)) {
                child = (View) parent2;
            }
        }
        return null;
    }

    /* Access modifiers changed, original: 0000 */
    public ItemInfo infoForPosition(int position) {
        for (int i = 0; i < this.mItems.size(); i++) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            if (ii.position == position) {
                return ii;
            }
        }
        return null;
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxGutterSize;
        int widthSize;
        setMeasuredDimension(View.getDefaultSize(0, widthMeasureSpec), View.getDefaultSize(0, heightMeasureSpec));
        int measuredWidth = getMeasuredWidth();
        int maxGutterSize2 = measuredWidth / 10;
        this.mGutterSize = Math.min(maxGutterSize2, this.mDefaultGutterSize);
        int childWidthSize = (measuredWidth - getPaddingLeft()) - getPaddingRight();
        int childHeightSize = (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom();
        int size = getChildCount();
        int i = 0;
        while (i < size) {
            int measuredWidth2;
            int heightMode;
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp == null || !lp.isDecor) {
                    measuredWidth2 = measuredWidth;
                    maxGutterSize = maxGutterSize2;
                } else {
                    int hgrav = lp.gravity & 7;
                    int vgrav = lp.gravity & 112;
                    int widthMode = Integer.MIN_VALUE;
                    maxGutterSize = Integer.MIN_VALUE;
                    boolean consumeVertical = vgrav == 48 || vgrav == 80;
                    boolean z = hgrav == 3 || hgrav == 5;
                    boolean consumeHorizontal = z;
                    if (consumeVertical) {
                        widthMode = 1073741824;
                    } else if (consumeHorizontal) {
                        maxGutterSize = 1073741824;
                    }
                    int widthSize2 = childWidthSize;
                    int heightSize = childHeightSize;
                    measuredWidth2 = measuredWidth;
                    if (lp.width != -2) {
                        widthMode = 1073741824;
                        if (lp.width != -1) {
                            widthSize = lp.width;
                        } else {
                            widthSize = widthSize2;
                        }
                    } else {
                        widthSize = widthSize2;
                    }
                    if (lp.height == -2) {
                        heightMode = maxGutterSize;
                        measuredWidth = heightSize;
                    } else if (lp.height != -1) {
                        measuredWidth = lp.height;
                        heightMode = 1073741824;
                    } else {
                        heightMode = 1073741824;
                        measuredWidth = heightSize;
                    }
                    maxGutterSize = maxGutterSize2;
                    widthSize2 = widthSize;
                    child.measure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(measuredWidth, heightMode));
                    if (consumeVertical) {
                        childHeightSize -= child.getMeasuredHeight();
                    } else if (consumeHorizontal) {
                        childWidthSize -= child.getMeasuredWidth();
                    }
                }
            } else {
                measuredWidth2 = measuredWidth;
                maxGutterSize = maxGutterSize2;
            }
            i++;
            widthSize = widthMeasureSpec;
            heightMode = heightMeasureSpec;
            maxGutterSize2 = maxGutterSize;
            measuredWidth = measuredWidth2;
        }
        maxGutterSize = maxGutterSize2;
        this.mChildWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, 1073741824);
        this.mChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, 1073741824);
        this.mInLayout = true;
        populate();
        this.mInLayout = false;
        widthSize = getChildCount();
        for (measuredWidth = 0; measuredWidth < widthSize; measuredWidth++) {
            View child2 = getChildAt(measuredWidth);
            if (child2.getVisibility() != 8) {
                LayoutParams lp2 = (LayoutParams) child2.getLayoutParams();
                if (lp2 == null || !lp2.isDecor) {
                    child2.measure(MeasureSpec.makeMeasureSpec((int) (((float) childWidthSize) * lp2.widthFactor), 1073741824), this.mChildHeightMeasureSpec);
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw) {
            int i = this.mPageMargin;
            recomputeScrollPosition(w, oldw, i, i);
        }
    }

    private void recomputeScrollPosition(int width, int oldWidth, int margin, int oldMargin) {
        int i = width;
        if (oldWidth <= 0 || this.mItems.isEmpty()) {
            ItemInfo ii = infoForPosition(this.mCurItem);
            int scrollPos = (int) (((float) ((i - getPaddingLeft()) - getPaddingRight())) * (ii != null ? Math.min(ii.offset, this.mLastOffset) : 0.0f));
            if (scrollPos != getScrollX()) {
                completeScroll(false);
                scrollTo(scrollPos, getScrollY());
                return;
            }
            return;
        }
        int newOffsetPixels = (int) (((float) (((i - getPaddingLeft()) - getPaddingRight()) + margin)) * (((float) getScrollX()) / ((float) (((oldWidth - getPaddingLeft()) - getPaddingRight()) + oldMargin))));
        scrollTo(newOffsetPixels, getScrollY());
        if (!this.mScroller.isFinished()) {
            this.mScroller.startScroll(newOffsetPixels, 0, (int) (infoForPosition(this.mCurItem).offset * ((float) i)), 0, this.mScroller.getDuration() - this.mScroller.timePassed());
        }
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int i;
        int childLeft;
        int paddingTop;
        int paddingLeft;
        boolean z;
        int count = getChildCount();
        int width = r - l;
        int height = b - t;
        int paddingLeft2 = getPaddingLeft();
        int paddingTop2 = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int scrollX = getScrollX();
        int decorCount = 0;
        int i2 = 0;
        while (true) {
            i = 8;
            if (i2 >= count) {
                break;
            }
            View child = getChildAt(i2);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int childLeft2;
                if (lp.isDecor) {
                    int hgrav = lp.gravity & 7;
                    childLeft2 = 0;
                    childLeft = lp.gravity & 112;
                    if (hgrav == 1) {
                        lp = Math.max((width - child.getMeasuredWidth()) / 2, paddingLeft2);
                    } else if (hgrav == 3) {
                        lp = paddingLeft2;
                        paddingLeft2 += child.getMeasuredWidth();
                    } else if (hgrav != 5) {
                        lp = paddingLeft2;
                    } else {
                        lp = (width - paddingRight) - child.getMeasuredWidth();
                        paddingRight += child.getMeasuredWidth();
                    }
                    childLeft2 = paddingLeft2;
                    if (childLeft == 16) {
                        paddingLeft2 = Math.max((height - child.getMeasuredHeight()) / 2, paddingTop2);
                    } else if (childLeft == 48) {
                        paddingLeft2 = paddingTop2;
                        paddingTop2 += child.getMeasuredHeight();
                    } else if (childLeft != 80) {
                        paddingLeft2 = paddingTop2;
                    } else {
                        paddingLeft2 = (height - paddingBottom) - child.getMeasuredHeight();
                        paddingBottom += child.getMeasuredHeight();
                    }
                    lp += scrollX;
                    paddingTop = paddingTop2;
                    child.layout(lp, paddingLeft2, child.getMeasuredWidth() + lp, paddingLeft2 + child.getMeasuredHeight());
                    decorCount++;
                    paddingLeft2 = childLeft2;
                    paddingTop2 = paddingTop;
                } else {
                    childLeft2 = 0;
                }
            }
            i2++;
        }
        i2 = (width - paddingLeft2) - paddingRight;
        int i3 = 0;
        while (i3 < count) {
            int count2;
            View child2 = getChildAt(i3);
            if (child2.getVisibility() == i) {
                count2 = count;
                paddingTop = width;
                paddingLeft = paddingLeft2;
            } else {
                LayoutParams lp2 = (LayoutParams) child2.getLayoutParams();
                if (lp2.isDecor) {
                    count2 = count;
                    paddingTop = width;
                    paddingLeft = paddingLeft2;
                } else {
                    ItemInfo ii = infoForChild(child2);
                    if (ii == null) {
                        count2 = count;
                        paddingTop = width;
                        paddingLeft = paddingLeft2;
                    } else {
                        if (lp2.needsMeasure) {
                            lp2.needsMeasure = false;
                            count2 = count;
                            paddingTop = width;
                            child2.measure(MeasureSpec.makeMeasureSpec((int) (((float) i2) * lp2.widthFactor), 1073741824), MeasureSpec.makeMeasureSpec((height - paddingTop2) - paddingBottom, 1073741824));
                        } else {
                            count2 = count;
                            paddingTop = width;
                        }
                        count = child2.getMeasuredWidth();
                        width = (int) (((float) i2) * ii.offset);
                        if (isLayoutRtl()) {
                            childLeft = ((16777216 - paddingRight) - width) - count;
                        } else {
                            childLeft = paddingLeft2 + width;
                        }
                        width = childLeft + count;
                        int childMeasuredWidth = count;
                        paddingLeft = paddingLeft2;
                        count = paddingTop2;
                        child2.layout(childLeft, count, width, count + child2.getMeasuredHeight());
                    }
                }
            }
            i3++;
            count = count2;
            width = paddingTop;
            paddingLeft2 = paddingLeft;
            i = 8;
        }
        paddingTop = width;
        paddingLeft = paddingLeft2;
        this.mTopPageBounds = paddingTop2;
        this.mBottomPageBounds = height - paddingBottom;
        this.mDecorChildCount = decorCount;
        if (this.mFirstLayout) {
            z = false;
            scrollToItem(this.mCurItem, false, 0, false);
        } else {
            z = false;
        }
        this.mFirstLayout = z;
    }

    public void computeScroll() {
        if (this.mScroller.isFinished() || !this.mScroller.computeScrollOffset()) {
            completeScroll(true);
            return;
        }
        int oldX = getScrollX();
        int oldY = getScrollY();
        int x = this.mScroller.getCurrX();
        int y = this.mScroller.getCurrY();
        if (!(oldX == x && oldY == y)) {
            scrollTo(x, y);
            if (!pageScrolled(x)) {
                this.mScroller.abortAnimation();
                scrollTo(0, y);
            }
        }
        postInvalidateOnAnimation();
    }

    private boolean pageScrolled(int scrollX) {
        String str = "onPageScrolled did not call superclass implementation";
        if (this.mItems.size() == 0) {
            this.mCalledSuper = false;
            onPageScrolled(0, 0.0f, 0);
            if (this.mCalledSuper) {
                return false;
            }
            throw new IllegalStateException(str);
        }
        int scrollStart;
        if (isLayoutRtl()) {
            scrollStart = 16777216 - scrollX;
        } else {
            scrollStart = scrollX;
        }
        ItemInfo ii = infoForFirstVisiblePage();
        int width = getPaddedWidth();
        int i = this.mPageMargin;
        int widthWithMargin = width + i;
        float marginOffset = ((float) i) / ((float) width);
        int currentPage = ii.position;
        float pageOffset = ((((float) scrollStart) / ((float) width)) - ii.offset) / (ii.widthFactor + marginOffset);
        int offsetPixels = (int) (((float) widthWithMargin) * pageOffset);
        this.mCalledSuper = false;
        onPageScrolled(currentPage, pageOffset, offsetPixels);
        if (this.mCalledSuper) {
            return true;
        }
        throw new IllegalStateException(str);
    }

    /* Access modifiers changed, original: protected */
    public void onPageScrolled(int position, float offset, int offsetPixels) {
        int scrollX;
        int paddingLeft;
        int paddingRight;
        int i = position;
        float f = offset;
        int i2 = offsetPixels;
        if (this.mDecorChildCount > 0) {
            scrollX = getScrollX();
            paddingLeft = getPaddingLeft();
            paddingRight = getPaddingRight();
            int width = getWidth();
            int childCount = getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                View child = getChildAt(i3);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp.isDecor) {
                    int childLeft;
                    int hgrav = lp.gravity & 7;
                    if (hgrav == 1) {
                        childLeft = Math.max((width - child.getMeasuredWidth()) / 2, paddingLeft);
                    } else if (hgrav == 3) {
                        childLeft = paddingLeft;
                        paddingLeft += child.getWidth();
                    } else if (hgrav != 5) {
                        childLeft = paddingLeft;
                    } else {
                        childLeft = (width - paddingRight) - child.getMeasuredWidth();
                        paddingRight += child.getMeasuredWidth();
                    }
                    int childOffset = (childLeft + scrollX) - child.getLeft();
                    if (childOffset != 0) {
                        child.offsetLeftAndRight(childOffset);
                    }
                }
            }
        }
        OnPageChangeListener onPageChangeListener = this.mOnPageChangeListener;
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrolled(i, f, i2);
        }
        onPageChangeListener = this.mInternalPageChangeListener;
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrolled(i, f, i2);
        }
        if (this.mPageTransformer != null) {
            scrollX = getScrollX();
            paddingLeft = getChildCount();
            for (paddingRight = 0; paddingRight < paddingLeft; paddingRight++) {
                View child2 = getChildAt(paddingRight);
                if (!((LayoutParams) child2.getLayoutParams()).isDecor) {
                    this.mPageTransformer.transformPage(child2, ((float) (child2.getLeft() - scrollX)) / ((float) getPaddedWidth()));
                }
            }
        }
        this.mCalledSuper = true;
    }

    private void completeScroll(boolean postEvents) {
        int oldX;
        boolean needPopulate = this.mScrollState == 2;
        if (needPopulate) {
            setScrollingCacheEnabled(false);
            this.mScroller.abortAnimation();
            oldX = getScrollX();
            int oldY = getScrollY();
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            if (!(oldX == x && oldY == y)) {
                scrollTo(x, y);
            }
        }
        this.mPopulatePending = false;
        for (oldX = 0; oldX < this.mItems.size(); oldX++) {
            ItemInfo ii = (ItemInfo) this.mItems.get(oldX);
            if (ii.scrolling) {
                needPopulate = true;
                ii.scrolling = false;
            }
        }
        if (!needPopulate) {
            return;
        }
        if (postEvents) {
            postOnAnimation(this.mEndScrollRunnable);
        } else {
            this.mEndScrollRunnable.run();
        }
    }

    private boolean isGutterDrag(float x, float dx) {
        return (x < ((float) this.mGutterSize) && dx > 0.0f) || (x > ((float) (getWidth() - this.mGutterSize)) && dx < 0.0f);
    }

    private void enableLayers(boolean enable) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setLayerType(enable ? 2 : 0, null);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        MotionEvent motionEvent = ev;
        int action = ev.getAction() & 255;
        if (action == 3 || action == 1) {
            this.mIsBeingDragged = false;
            this.mIsUnableToDrag = false;
            this.mActivePointerId = -1;
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null) {
                velocityTracker.recycle();
                this.mVelocityTracker = null;
            }
            return false;
        }
        if (action != 0) {
            if (this.mIsBeingDragged) {
                return true;
            }
            if (this.mIsUnableToDrag) {
                return false;
            }
        }
        float x;
        if (action == 0) {
            x = ev.getX();
            this.mInitialMotionX = x;
            this.mLastMotionX = x;
            x = ev.getY();
            this.mInitialMotionY = x;
            this.mLastMotionY = x;
            this.mActivePointerId = motionEvent.getPointerId(0);
            this.mIsUnableToDrag = false;
            this.mScroller.computeScrollOffset();
            if (this.mScrollState != 2 || Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) <= this.mCloseEnough) {
                completeScroll(false);
                this.mIsBeingDragged = false;
            } else {
                this.mScroller.abortAnimation();
                this.mPopulatePending = false;
                populate();
                this.mIsBeingDragged = true;
                requestParentDisallowInterceptTouchEvent(true);
                setScrollState(1);
            }
        } else if (action == 2) {
            int activePointerId = this.mActivePointerId;
            if (activePointerId != -1) {
                float y;
                int pointerIndex = motionEvent.findPointerIndex(activePointerId);
                float x2 = motionEvent.getX(pointerIndex);
                float dx = x2 - this.mLastMotionX;
                float xDiff = Math.abs(dx);
                float y2 = motionEvent.getY(pointerIndex);
                float yDiff = Math.abs(y2 - this.mInitialMotionY);
                if (dx == 0.0f || isGutterDrag(this.mLastMotionX, dx)) {
                    y = y2;
                } else {
                    y = y2;
                    if (canScroll(this, false, (int) dx, (int) x2, (int) y2)) {
                        this.mLastMotionX = x2;
                        this.mLastMotionY = y;
                        this.mIsUnableToDrag = true;
                        return false;
                    }
                }
                if (xDiff > ((float) this.mTouchSlop) && 0.5f * xDiff > yDiff) {
                    this.mIsBeingDragged = true;
                    requestParentDisallowInterceptTouchEvent(true);
                    setScrollState(1);
                    if (dx > 0.0f) {
                        x = this.mInitialMotionX + ((float) this.mTouchSlop);
                    } else {
                        x = this.mInitialMotionX - ((float) this.mTouchSlop);
                    }
                    this.mLastMotionX = x;
                    this.mLastMotionY = y;
                    setScrollingCacheEnabled(true);
                } else if (yDiff > ((float) this.mTouchSlop)) {
                    this.mIsUnableToDrag = true;
                }
                if (this.mIsBeingDragged && performDrag(x2)) {
                    postInvalidateOnAnimation();
                }
            }
        } else if (action == 6) {
            onSecondaryPointerUp(ev);
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        return this.mIsBeingDragged;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        MotionEvent motionEvent = ev;
        if (ev.getAction() == 0 && ev.getEdgeFlags() != 0) {
            return false;
        }
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter == null || pagerAdapter.getCount() == 0) {
            return false;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        boolean needsInvalidate = false;
        int action = ev.getAction() & 255;
        float x;
        float yDiff;
        if (action == 0) {
            this.mScroller.abortAnimation();
            this.mPopulatePending = false;
            populate();
            x = ev.getX();
            this.mInitialMotionX = x;
            this.mLastMotionX = x;
            x = ev.getY();
            this.mInitialMotionY = x;
            this.mLastMotionY = x;
            this.mActivePointerId = motionEvent.getPointerId(0);
        } else if (action != 1) {
            int pointerIndex;
            if (action == 2) {
                if (!this.mIsBeingDragged) {
                    pointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    x = motionEvent.getX(pointerIndex);
                    float xDiff = Math.abs(x - this.mLastMotionX);
                    float y = motionEvent.getY(pointerIndex);
                    yDiff = Math.abs(y - this.mLastMotionY);
                    if (xDiff > ((float) this.mTouchSlop) && xDiff > yDiff) {
                        this.mIsBeingDragged = true;
                        requestParentDisallowInterceptTouchEvent(true);
                        float f = this.mInitialMotionX;
                        if (x - f > 0.0f) {
                            f += (float) this.mTouchSlop;
                        } else {
                            f -= (float) this.mTouchSlop;
                        }
                        this.mLastMotionX = f;
                        this.mLastMotionY = y;
                        setScrollState(1);
                        setScrollingCacheEnabled(true);
                        ViewParent parent = getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        }
                    }
                }
                if (this.mIsBeingDragged) {
                    needsInvalidate = false | performDrag(motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId)));
                }
            } else if (action != 3) {
                if (action == 5) {
                    pointerIndex = ev.getActionIndex();
                    this.mLastMotionX = motionEvent.getX(pointerIndex);
                    this.mActivePointerId = motionEvent.getPointerId(pointerIndex);
                } else if (action == 6) {
                    onSecondaryPointerUp(ev);
                    this.mLastMotionX = motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId));
                }
            } else if (this.mIsBeingDragged) {
                scrollToItem(this.mCurItem, true, 0, false);
                this.mActivePointerId = -1;
                endDrag();
                this.mLeftEdge.onRelease();
                this.mRightEdge.onRelease();
                needsInvalidate = true;
            }
        } else if (this.mIsBeingDragged) {
            float nextPageOffset;
            VelocityTracker velocityTracker = this.mVelocityTracker;
            velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
            action = (int) velocityTracker.getXVelocity(this.mActivePointerId);
            this.mPopulatePending = true;
            yDiff = ((float) getScrollStart()) / ((float) getPaddedWidth());
            ItemInfo ii = infoForFirstVisiblePage();
            int currentPage = ii.position;
            if (isLayoutRtl()) {
                nextPageOffset = (ii.offset - yDiff) / ii.widthFactor;
            } else {
                nextPageOffset = (yDiff - ii.offset) / ii.widthFactor;
            }
            setCurrentItemInternal(determineTargetPage(currentPage, nextPageOffset, action, (int) (motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId)) - this.mInitialMotionX)), true, true, action);
            this.mActivePointerId = -1;
            endDrag();
            this.mLeftEdge.onRelease();
            this.mRightEdge.onRelease();
            needsInvalidate = true;
        }
        if (needsInvalidate) {
            postInvalidateOnAnimation();
        }
        return true;
    }

    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    private boolean performDrag(float x) {
        EdgeEffect startEdge;
        EdgeEffect endEdge;
        float scrollStart;
        float startBound;
        float endBound;
        float targetScrollX;
        float f = x;
        boolean needsInvalidate = false;
        int width = getPaddedWidth();
        float deltaX = this.mLastMotionX - f;
        this.mLastMotionX = f;
        if (isLayoutRtl()) {
            startEdge = this.mRightEdge;
            endEdge = this.mLeftEdge;
        } else {
            startEdge = this.mLeftEdge;
            endEdge = this.mRightEdge;
        }
        float nextScrollX = ((float) getScrollX()) + deltaX;
        if (isLayoutRtl()) {
            scrollStart = 1.6777216E7f - nextScrollX;
        } else {
            scrollStart = nextScrollX;
        }
        ItemInfo startItem = (ItemInfo) this.mItems.get(0);
        boolean startAbsolute = startItem.position == 0;
        if (startAbsolute) {
            startBound = startItem.offset * ((float) width);
        } else {
            startBound = ((float) width) * this.mFirstOffset;
        }
        ArrayList arrayList = this.mItems;
        ItemInfo endItem = (ItemInfo) arrayList.get(arrayList.size() - 1);
        boolean endAbsolute = endItem.position == this.mAdapter.getCount() - 1;
        if (endAbsolute) {
            endBound = endItem.offset * ((float) width);
        } else {
            endBound = ((float) width) * this.mLastOffset;
        }
        if (scrollStart < startBound) {
            if (startAbsolute) {
                startEdge.onPull(Math.abs(startBound - scrollStart) / ((float) width));
                needsInvalidate = true;
            }
            f = startBound;
        } else if (scrollStart > endBound) {
            if (endAbsolute) {
                f = scrollStart - endBound;
                endEdge.onPull(Math.abs(f) / ((float) width));
                needsInvalidate = true;
            }
            f = endBound;
        } else {
            f = scrollStart;
        }
        if (isLayoutRtl()) {
            targetScrollX = 1.6777216E7f - f;
        } else {
            targetScrollX = f;
        }
        this.mLastMotionX += targetScrollX - ((float) ((int) targetScrollX));
        scrollTo((int) targetScrollX, getScrollY());
        pageScrolled((int) targetScrollX);
        return needsInvalidate;
    }

    private ItemInfo infoForFirstVisiblePage() {
        int startOffset = getScrollStart();
        int width = getPaddedWidth();
        float marginOffset = 0.0f;
        float scrollOffset = width > 0 ? ((float) startOffset) / ((float) width) : 0.0f;
        if (width > 0) {
            marginOffset = ((float) this.mPageMargin) / ((float) width);
        }
        int lastPos = -1;
        float lastOffset = 0.0f;
        float lastWidth = 0.0f;
        boolean first = true;
        ItemInfo lastItem = null;
        int N = this.mItems.size();
        int i = 0;
        while (i < N) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            if (!(first || ii.position == lastPos + 1)) {
                ii = this.mTempItem;
                ii.offset = (lastOffset + lastWidth) + marginOffset;
                ii.position = lastPos + 1;
                ii.widthFactor = this.mAdapter.getPageWidth(ii.position);
                i--;
            }
            float offset = ii.offset;
            float startBound = offset;
            if (!first && scrollOffset < startBound) {
                return lastItem;
            }
            if (scrollOffset >= (ii.widthFactor + offset) + marginOffset) {
                int startOffset2 = startOffset;
                if (i != this.mItems.size() - 1) {
                    first = false;
                    lastPos = ii.position;
                    lastOffset = offset;
                    lastWidth = ii.widthFactor;
                    lastItem = ii;
                    i++;
                    startOffset = startOffset2;
                }
            }
            return ii;
        }
        return lastItem;
    }

    private int getScrollStart() {
        if (isLayoutRtl()) {
            return 16777216 - getScrollX();
        }
        return getScrollX();
    }

    private int determineTargetPage(int currentPage, float pageOffset, int velocity, int deltaX) {
        int targetPage;
        if (Math.abs(deltaX) <= this.mFlingDistance || Math.abs(velocity) <= this.mMinimumVelocity) {
            targetPage = (int) (((float) currentPage) - (((float) this.mLeftIncr) * (pageOffset + (currentPage >= this.mCurItem ? 0.4f : 0.6f))));
        } else {
            targetPage = currentPage - (velocity < 0 ? this.mLeftIncr : 0);
        }
        if (this.mItems.size() <= 0) {
            return targetPage;
        }
        ItemInfo firstItem = (ItemInfo) this.mItems.get(0);
        ArrayList arrayList = this.mItems;
        return MathUtils.constrain(targetPage, firstItem.position, ((ItemInfo) arrayList.get(arrayList.size() - 1)).position);
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x00a9  */
    /* JADX WARNING: Missing block: B:7:0x0015, code skipped:
            if (r3.getCount() > 1) goto L_0x0024;
     */
    public void draw(android.graphics.Canvas r9) {
        /*
        r8 = this;
        super.draw(r9);
        r0 = 0;
        r1 = r8.getOverScrollMode();
        if (r1 == 0) goto L_0x0024;
    L_0x000a:
        r2 = 1;
        if (r1 != r2) goto L_0x0018;
    L_0x000d:
        r3 = r8.mAdapter;
        if (r3 == 0) goto L_0x0018;
    L_0x0011:
        r3 = r3.getCount();
        if (r3 <= r2) goto L_0x0018;
    L_0x0017:
        goto L_0x0024;
    L_0x0018:
        r2 = r8.mLeftEdge;
        r2.finish();
        r2 = r8.mRightEdge;
        r2.finish();
        goto L_0x00a7;
    L_0x0024:
        r2 = r8.mLeftEdge;
        r2 = r2.isFinished();
        if (r2 != 0) goto L_0x0064;
    L_0x002c:
        r2 = r9.save();
        r3 = r8.getHeight();
        r4 = r8.getPaddingTop();
        r3 = r3 - r4;
        r4 = r8.getPaddingBottom();
        r3 = r3 - r4;
        r4 = r8.getWidth();
        r5 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
        r9.rotate(r5);
        r5 = -r3;
        r6 = r8.getPaddingTop();
        r5 = r5 + r6;
        r5 = (float) r5;
        r6 = r8.mFirstOffset;
        r7 = (float) r4;
        r6 = r6 * r7;
        r9.translate(r5, r6);
        r5 = r8.mLeftEdge;
        r5.setSize(r3, r4);
        r5 = r8.mLeftEdge;
        r5 = r5.draw(r9);
        r0 = r0 | r5;
        r9.restoreToCount(r2);
    L_0x0064:
        r2 = r8.mRightEdge;
        r2 = r2.isFinished();
        if (r2 != 0) goto L_0x00a7;
    L_0x006c:
        r2 = r9.save();
        r3 = r8.getWidth();
        r4 = r8.getHeight();
        r5 = r8.getPaddingTop();
        r4 = r4 - r5;
        r5 = r8.getPaddingBottom();
        r4 = r4 - r5;
        r5 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
        r9.rotate(r5);
        r5 = r8.getPaddingTop();
        r5 = -r5;
        r5 = (float) r5;
        r6 = r8.mLastOffset;
        r7 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = r6 + r7;
        r6 = -r6;
        r7 = (float) r3;
        r6 = r6 * r7;
        r9.translate(r5, r6);
        r5 = r8.mRightEdge;
        r5.setSize(r4, r3);
        r5 = r8.mRightEdge;
        r5 = r5.draw(r9);
        r0 = r0 | r5;
        r9.restoreToCount(r2);
    L_0x00a7:
        if (r0 == 0) goto L_0x00ac;
    L_0x00a9:
        r8.postInvalidateOnAnimation();
    L_0x00ac:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.widget.ViewPager.draw(android.graphics.Canvas):void");
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Canvas canvas2;
        if (this.mPageMargin <= 0 || this.mMarginDrawable == null || this.mItems.size() <= 0 || this.mAdapter == null) {
            canvas2 = canvas;
            return;
        }
        float marginOffset;
        int scrollX = getScrollX();
        int width = getWidth();
        float marginOffset2 = ((float) this.mPageMargin) / ((float) width);
        int itemIndex = 0;
        ItemInfo ii = (ItemInfo) this.mItems.get(0);
        float offset = ii.offset;
        int itemCount = this.mItems.size();
        int firstPos = ii.position;
        int lastPos = ((ItemInfo) this.mItems.get(itemCount - 1)).position;
        int pos = firstPos;
        while (pos < lastPos) {
            float itemOffset;
            float widthFactor;
            float left;
            ItemInfo ii2;
            float offset2;
            while (pos > ii.position && itemIndex < itemCount) {
                itemIndex++;
                ii = (ItemInfo) this.mItems.get(itemIndex);
            }
            if (pos == ii.position) {
                itemOffset = ii.offset;
                widthFactor = ii.widthFactor;
            } else {
                itemOffset = offset;
                widthFactor = this.mAdapter.getPageWidth(pos);
            }
            float scaledOffset = ((float) width) * itemOffset;
            if (isLayoutRtl()) {
                left = 1.6777216E7f - scaledOffset;
            } else {
                left = (((float) width) * widthFactor) + scaledOffset;
            }
            offset = (itemOffset + widthFactor) + marginOffset2;
            int i = this.mPageMargin;
            marginOffset = marginOffset2;
            int itemIndex2 = itemIndex;
            if (((float) i) + left > ((float) scrollX)) {
                ii2 = ii;
                offset2 = offset;
                this.mMarginDrawable.setBounds((int) left, this.mTopPageBounds, (int) ((((float) i) + left) + 0.5f), this.mBottomPageBounds);
                this.mMarginDrawable.draw(canvas);
            } else {
                canvas2 = canvas;
                ii2 = ii;
                offset2 = offset;
            }
            if (left <= ((float) (scrollX + width))) {
                pos++;
                marginOffset2 = marginOffset;
                itemIndex = itemIndex2;
                ii = ii2;
                offset = offset2;
            } else {
                return;
            }
        }
        canvas2 = canvas;
        marginOffset = marginOffset2;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = ev.getActionIndex();
        if (ev.getPointerId(pointerIndex) == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mLastMotionX = ev.getX(newPointerIndex);
            this.mActivePointerId = ev.getPointerId(newPointerIndex);
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null) {
                velocityTracker.clear();
            }
        }
    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private void setScrollingCacheEnabled(boolean enabled) {
        if (this.mScrollingCacheEnabled != enabled) {
            this.mScrollingCacheEnabled = enabled;
        }
    }

    public boolean canScrollHorizontally(int direction) {
        boolean z = false;
        if (this.mAdapter == null) {
            return false;
        }
        int width = getPaddedWidth();
        int scrollX = getScrollX();
        if (direction < 0) {
            if (scrollX > ((int) (((float) width) * this.mFirstOffset))) {
                z = true;
            }
            return z;
        } else if (direction <= 0) {
            return false;
        } else {
            if (scrollX < ((int) (((float) width) * this.mLastOffset))) {
                z = true;
            }
            return z;
        }
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Missing block: B:18:0x0065, code skipped:
            if (r15.canScrollHorizontally(-r17) != false) goto L_0x006b;
     */
    public boolean canScroll(android.view.View r15, boolean r16, int r17, int r18, int r19) {
        /*
        r14 = this;
        r0 = r15;
        r1 = r0 instanceof android.view.ViewGroup;
        r2 = 1;
        if (r1 == 0) goto L_0x005c;
    L_0x0006:
        r1 = r0;
        r1 = (android.view.ViewGroup) r1;
        r3 = r15.getScrollX();
        r4 = r15.getScrollY();
        r5 = r1.getChildCount();
        r6 = r5 + -1;
    L_0x0017:
        if (r6 < 0) goto L_0x005c;
    L_0x0019:
        r13 = r1.getChildAt(r6);
        r7 = r18 + r3;
        r8 = r13.getLeft();
        if (r7 < r8) goto L_0x0059;
    L_0x0025:
        r7 = r18 + r3;
        r8 = r13.getRight();
        if (r7 >= r8) goto L_0x0059;
    L_0x002d:
        r7 = r19 + r4;
        r8 = r13.getTop();
        if (r7 < r8) goto L_0x0059;
    L_0x0035:
        r7 = r19 + r4;
        r8 = r13.getBottom();
        if (r7 >= r8) goto L_0x0059;
    L_0x003d:
        r9 = 1;
        r7 = r18 + r3;
        r8 = r13.getLeft();
        r11 = r7 - r8;
        r7 = r19 + r4;
        r8 = r13.getTop();
        r12 = r7 - r8;
        r7 = r14;
        r8 = r13;
        r10 = r17;
        r7 = r7.canScroll(r8, r9, r10, r11, r12);
        if (r7 == 0) goto L_0x0059;
    L_0x0058:
        return r2;
    L_0x0059:
        r6 = r6 + -1;
        goto L_0x0017;
    L_0x005c:
        if (r16 == 0) goto L_0x0068;
    L_0x005e:
        r1 = r17;
        r3 = -r1;
        r3 = r15.canScrollHorizontally(r3);
        if (r3 == 0) goto L_0x006a;
    L_0x0067:
        goto L_0x006b;
    L_0x0068:
        r1 = r17;
    L_0x006a:
        r2 = 0;
    L_0x006b:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.widget.ViewPager.canScroll(android.view.View, boolean, int, int, int):boolean");
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event) || executeKeyEvent(event);
    }

    public boolean executeKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return false;
        }
        int keyCode = event.getKeyCode();
        if (keyCode == 21) {
            return arrowScroll(17);
        }
        if (keyCode == 22) {
            return arrowScroll(66);
        }
        if (keyCode != 61) {
            return false;
        }
        if (event.hasNoModifiers()) {
            return arrowScroll(2);
        }
        if (event.hasModifiers(1)) {
            return arrowScroll(1);
        }
        return false;
    }

    public boolean arrowScroll(int direction) {
        boolean isChild;
        View currentFocused = findFocus();
        if (currentFocused == this) {
            currentFocused = null;
        } else if (currentFocused != null) {
            isChild = false;
            for (ViewPager parent = currentFocused.getParent(); parent instanceof ViewGroup; parent = parent.getParent()) {
                if (parent == this) {
                    isChild = true;
                    break;
                }
            }
            if (!isChild) {
                StringBuilder sb = new StringBuilder();
                sb.append(currentFocused.getClass().getSimpleName());
                for (ViewParent parent2 = currentFocused.getParent(); parent2 instanceof ViewGroup; parent2 = parent2.getParent()) {
                    sb.append(" => ");
                    sb.append(parent2.getClass().getSimpleName());
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("arrowScroll tried to find focus based on non-child current focused view ");
                stringBuilder.append(sb.toString());
                Log.e(TAG, stringBuilder.toString());
                currentFocused = null;
            }
        }
        isChild = false;
        View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, direction);
        if (nextFocused == null || nextFocused == currentFocused) {
            if (direction == 17 || direction == 1) {
                isChild = pageLeft();
            } else if (direction == 66 || direction == 2) {
                isChild = pageRight();
            }
        } else if (direction == 17) {
            isChild = (currentFocused == null || getChildRectInPagerCoordinates(this.mTempRect, nextFocused).left < getChildRectInPagerCoordinates(this.mTempRect, currentFocused).left) ? nextFocused.requestFocus() : pageLeft();
        } else if (direction == 66) {
            isChild = (currentFocused == null || getChildRectInPagerCoordinates(this.mTempRect, nextFocused).left > getChildRectInPagerCoordinates(this.mTempRect, currentFocused).left) ? nextFocused.requestFocus() : pageRight();
        }
        if (isChild) {
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
        }
        return isChild;
    }

    private Rect getChildRectInPagerCoordinates(Rect outRect, View child) {
        if (outRect == null) {
            outRect = new Rect();
        }
        if (child == null) {
            outRect.set(0, 0, 0, 0);
            return outRect;
        }
        outRect.left = child.getLeft();
        outRect.right = child.getRight();
        outRect.top = child.getTop();
        outRect.bottom = child.getBottom();
        ViewGroup parent = child.getParent();
        while ((parent instanceof ViewGroup) && parent != this) {
            ViewGroup group = parent;
            outRect.left += group.getLeft();
            outRect.right += group.getRight();
            outRect.top += group.getTop();
            outRect.bottom += group.getBottom();
            parent = group.getParent();
        }
        return outRect;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean pageLeft() {
        return setCurrentItemInternal(this.mCurItem + this.mLeftIncr, true, false);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean pageRight() {
        return setCurrentItemInternal(this.mCurItem - this.mLeftIncr, true, false);
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        if (layoutDirection == 0) {
            this.mLeftIncr = -1;
        } else {
            this.mLeftIncr = 1;
        }
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        int focusableCount = views.size();
        int descendantFocusability = getDescendantFocusability();
        if (descendantFocusability != 393216) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child.getVisibility() == 0) {
                    ItemInfo ii = infoForChild(child);
                    if (ii != null && ii.position == this.mCurItem) {
                        child.addFocusables(views, direction, focusableMode);
                    }
                }
            }
        }
        if ((descendantFocusability == 262144 && focusableCount != views.size()) || !isFocusable()) {
            return;
        }
        if ((focusableMode & 1) != 1 || !isInTouchMode() || isFocusableInTouchMode()) {
            views.add(this);
        }
    }

    public void addTouchables(ArrayList<View> views) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                ItemInfo ii = infoForChild(child);
                if (ii != null && ii.position == this.mCurItem) {
                    child.addTouchables(views);
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        int index;
        int increment;
        int end;
        int count = getChildCount();
        if ((direction & 2) != 0) {
            index = 0;
            increment = 1;
            end = count;
        } else {
            index = count - 1;
            increment = -1;
            end = -1;
        }
        for (int i = index; i != end; i += increment) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                ItemInfo ii = infoForChild(child);
                if (ii != null && ii.position == this.mCurItem && child.requestFocus(direction, previouslyFocusedRect)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* Access modifiers changed, original: protected */
    public android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    /* Access modifiers changed, original: protected */
    public android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return generateDefaultLayoutParams();
    }

    /* Access modifiers changed, original: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return (p instanceof LayoutParams) && super.checkLayoutParams(p);
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(ViewPager.class.getName());
        event.setScrollable(canScroll());
        if (event.getEventType() == 4096) {
            PagerAdapter pagerAdapter = this.mAdapter;
            if (pagerAdapter != null) {
                event.setItemCount(pagerAdapter.getCount());
                event.setFromIndex(this.mCurItem);
                event.setToIndex(this.mCurItem);
            }
        }
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(ViewPager.class.getName());
        info.setScrollable(canScroll());
        if (canScrollHorizontally(1)) {
            info.addAction(AccessibilityAction.ACTION_SCROLL_FORWARD);
            info.addAction(AccessibilityAction.ACTION_SCROLL_RIGHT);
        }
        if (canScrollHorizontally(-1)) {
            info.addAction(AccessibilityAction.ACTION_SCROLL_BACKWARD);
            info.addAction(AccessibilityAction.ACTION_SCROLL_LEFT);
        }
    }

    public boolean performAccessibilityAction(int action, Bundle args) {
        if (super.performAccessibilityAction(action, args)) {
            return true;
        }
        if (action != 4096) {
            if (action == 8192 || action == 16908345) {
                if (!canScrollHorizontally(-1)) {
                    return false;
                }
                setCurrentItem(this.mCurItem - 1);
                return true;
            } else if (action != 16908347) {
                return false;
            }
        }
        if (!canScrollHorizontally(1)) {
            return false;
        }
        setCurrentItem(this.mCurItem + 1);
        return true;
    }

    private boolean canScroll() {
        PagerAdapter pagerAdapter = this.mAdapter;
        return pagerAdapter != null && pagerAdapter.getCount() > 1;
    }
}
