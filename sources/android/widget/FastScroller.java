package android.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.AnimatorSet.Builder;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.IntProperty;
import android.util.MathUtils;
import android.util.Property;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroupOverlay;
import android.widget.ImageView.ScaleType;
import com.android.internal.R;

class FastScroller {
    private static Property<View, Integer> BOTTOM = new IntProperty<View>("bottom") {
        public void setValue(View object, int value) {
            object.setBottom(value);
        }

        public Integer get(View object) {
            return Integer.valueOf(object.getBottom());
        }
    };
    private static final int DURATION_CROSS_FADE = 50;
    private static final int DURATION_FADE_IN = 150;
    private static final int DURATION_FADE_OUT = 300;
    private static final int DURATION_RESIZE = 100;
    private static final long FADE_TIMEOUT = 1500;
    private static Property<View, Integer> LEFT = new IntProperty<View>("left") {
        public void setValue(View object, int value) {
            object.setLeft(value);
        }

        public Integer get(View object) {
            return Integer.valueOf(object.getLeft());
        }
    };
    private static final int MIN_PAGES = 4;
    private static final int OVERLAY_ABOVE_THUMB = 2;
    private static final int OVERLAY_AT_THUMB = 1;
    private static final int OVERLAY_FLOATING = 0;
    private static final int PREVIEW_LEFT = 0;
    private static final int PREVIEW_RIGHT = 1;
    private static Property<View, Integer> RIGHT = new IntProperty<View>("right") {
        public void setValue(View object, int value) {
            object.setRight(value);
        }

        public Integer get(View object) {
            return Integer.valueOf(object.getRight());
        }
    };
    private static final int STATE_DRAGGING = 2;
    private static final int STATE_NONE = 0;
    private static final int STATE_VISIBLE = 1;
    private static final long TAP_TIMEOUT = ((long) ViewConfiguration.getTapTimeout());
    private static final int THUMB_POSITION_INSIDE = 1;
    private static final int THUMB_POSITION_MIDPOINT = 0;
    private static Property<View, Integer> TOP = new IntProperty<View>("top") {
        public void setValue(View object, int value) {
            object.setTop(value);
        }

        public Integer get(View object) {
            return Integer.valueOf(object.getTop());
        }
    };
    private boolean mAlwaysShow;
    @UnsupportedAppUsage
    private final Rect mContainerRect = new Rect();
    private int mCurrentSection = -1;
    private AnimatorSet mDecorAnimation;
    private final Runnable mDeferHide = new Runnable() {
        public void run() {
            FastScroller.this.setState(0);
        }
    };
    private boolean mEnabled;
    private int mFirstVisibleItem;
    @UnsupportedAppUsage
    private int mHeaderCount;
    private float mInitialTouchY;
    private boolean mLayoutFromRight;
    private final AbsListView mList;
    private Adapter mListAdapter;
    @UnsupportedAppUsage
    private boolean mLongList;
    private boolean mMatchDragPosition;
    @UnsupportedAppUsage
    private final int mMinimumTouchTarget;
    private int mOldChildCount;
    private int mOldItemCount;
    private final ViewGroupOverlay mOverlay;
    private int mOverlayPosition;
    private long mPendingDrag = -1;
    private AnimatorSet mPreviewAnimation;
    private final View mPreviewImage;
    private int mPreviewMinHeight;
    private int mPreviewMinWidth;
    private int mPreviewPadding;
    private final int[] mPreviewResId = new int[2];
    private final TextView mPrimaryText;
    private int mScaledTouchSlop;
    private int mScrollBarStyle;
    private boolean mScrollCompleted;
    private int mScrollbarPosition = -1;
    private final TextView mSecondaryText;
    private SectionIndexer mSectionIndexer;
    private Object[] mSections;
    private boolean mShowingPreview;
    private boolean mShowingPrimary;
    private int mState;
    private final AnimatorListener mSwitchPrimaryListener = new AnimatorListenerAdapter() {
        public void onAnimationEnd(Animator animation) {
            FastScroller fastScroller = FastScroller.this;
            fastScroller.mShowingPrimary = fastScroller.mShowingPrimary ^ 1;
        }
    };
    private final Rect mTempBounds = new Rect();
    private final Rect mTempMargins = new Rect();
    private int mTextAppearance;
    private ColorStateList mTextColor;
    private float mTextSize;
    @UnsupportedAppUsage
    private Drawable mThumbDrawable;
    @UnsupportedAppUsage
    private final ImageView mThumbImage;
    private int mThumbMinHeight;
    private int mThumbMinWidth;
    private float mThumbOffset;
    private int mThumbPosition;
    private float mThumbRange;
    @UnsupportedAppUsage
    private Drawable mTrackDrawable;
    @UnsupportedAppUsage
    private final ImageView mTrackImage;
    private boolean mUpdatingLayout;
    private int mWidth;

    @UnsupportedAppUsage
    public FastScroller(AbsListView listView, int styleResId) {
        this.mList = listView;
        this.mOldItemCount = listView.getCount();
        this.mOldChildCount = listView.getChildCount();
        Context context = listView.getContext();
        this.mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mScrollBarStyle = listView.getScrollBarStyle();
        boolean z = true;
        this.mScrollCompleted = true;
        this.mState = 1;
        if (context.getApplicationInfo().targetSdkVersion < 11) {
            z = false;
        }
        this.mMatchDragPosition = z;
        this.mTrackImage = new ImageView(context);
        this.mTrackImage.setScaleType(ScaleType.FIT_XY);
        this.mThumbImage = new ImageView(context);
        this.mThumbImage.setScaleType(ScaleType.FIT_XY);
        this.mPreviewImage = new View(context);
        this.mPreviewImage.setAlpha(0.0f);
        this.mPrimaryText = createPreviewTextView(context);
        this.mSecondaryText = createPreviewTextView(context);
        this.mMinimumTouchTarget = listView.getResources().getDimensionPixelSize(R.dimen.fast_scroller_minimum_touch_target);
        setStyle(styleResId);
        ViewGroupOverlay overlay = listView.getOverlay();
        this.mOverlay = overlay;
        overlay.add(this.mTrackImage);
        overlay.add(this.mThumbImage);
        overlay.add(this.mPreviewImage);
        overlay.add(this.mPrimaryText);
        overlay.add(this.mSecondaryText);
        getSectionsFromIndexer();
        updateLongList(this.mOldChildCount, this.mOldItemCount);
        setScrollbarPosition(listView.getVerticalScrollbarPosition());
        postAutoHide();
    }

    private void updateAppearance() {
        int width = 0;
        this.mTrackImage.setImageDrawable(this.mTrackDrawable);
        Drawable drawable = this.mTrackDrawable;
        if (drawable != null) {
            width = Math.max(0, drawable.getIntrinsicWidth());
        }
        this.mThumbImage.setImageDrawable(this.mThumbDrawable);
        this.mThumbImage.setMinimumWidth(this.mThumbMinWidth);
        this.mThumbImage.setMinimumHeight(this.mThumbMinHeight);
        drawable = this.mThumbDrawable;
        if (drawable != null) {
            width = Math.max(width, drawable.getIntrinsicWidth());
        }
        this.mWidth = Math.max(width, this.mThumbMinWidth);
        int i = this.mTextAppearance;
        if (i != 0) {
            this.mPrimaryText.setTextAppearance(i);
            this.mSecondaryText.setTextAppearance(this.mTextAppearance);
        }
        ColorStateList colorStateList = this.mTextColor;
        if (colorStateList != null) {
            this.mPrimaryText.setTextColor(colorStateList);
            this.mSecondaryText.setTextColor(this.mTextColor);
        }
        float f = this.mTextSize;
        if (f > 0.0f) {
            this.mPrimaryText.setTextSize(0, f);
            this.mSecondaryText.setTextSize(0, this.mTextSize);
        }
        i = this.mPreviewPadding;
        this.mPrimaryText.setIncludeFontPadding(false);
        this.mPrimaryText.setPadding(i, i, i, i);
        this.mSecondaryText.setIncludeFontPadding(false);
        this.mSecondaryText.setPadding(i, i, i, i);
        refreshDrawablePressedState();
    }

    public void setStyle(int resId) {
        TypedArray ta = this.mList.getContext().obtainStyledAttributes(null, R.styleable.FastScroll, 16843767, resId);
        int N = ta.getIndexCount();
        for (int i = 0; i < N; i++) {
            int index = ta.getIndex(i);
            switch (index) {
                case 0:
                    this.mTextAppearance = ta.getResourceId(index, 0);
                    break;
                case 1:
                    this.mTextSize = (float) ta.getDimensionPixelSize(index, 0);
                    break;
                case 2:
                    this.mTextColor = ta.getColorStateList(index);
                    break;
                case 3:
                    this.mPreviewPadding = ta.getDimensionPixelSize(index, 0);
                    break;
                case 4:
                    this.mPreviewMinWidth = ta.getDimensionPixelSize(index, 0);
                    break;
                case 5:
                    this.mPreviewMinHeight = ta.getDimensionPixelSize(index, 0);
                    break;
                case 6:
                    this.mThumbPosition = ta.getInt(index, 0);
                    break;
                case 7:
                    this.mPreviewResId[0] = ta.getResourceId(index, 0);
                    break;
                case 8:
                    this.mPreviewResId[1] = ta.getResourceId(index, 0);
                    break;
                case 9:
                    this.mOverlayPosition = ta.getInt(index, 0);
                    break;
                case 10:
                    this.mThumbDrawable = ta.getDrawable(index);
                    break;
                case 11:
                    this.mThumbMinHeight = ta.getDimensionPixelSize(index, 0);
                    break;
                case 12:
                    this.mThumbMinWidth = ta.getDimensionPixelSize(index, 0);
                    break;
                case 13:
                    this.mTrackDrawable = ta.getDrawable(index);
                    break;
                default:
                    break;
            }
        }
        ta.recycle();
        updateAppearance();
    }

    @UnsupportedAppUsage
    public void remove() {
        this.mOverlay.remove(this.mTrackImage);
        this.mOverlay.remove(this.mThumbImage);
        this.mOverlay.remove(this.mPreviewImage);
        this.mOverlay.remove(this.mPrimaryText);
        this.mOverlay.remove(this.mSecondaryText);
    }

    public void setEnabled(boolean enabled) {
        if (this.mEnabled != enabled) {
            this.mEnabled = enabled;
            onStateDependencyChanged(true);
        }
    }

    public boolean isEnabled() {
        return this.mEnabled && (this.mLongList || this.mAlwaysShow);
    }

    public void setAlwaysShow(boolean alwaysShow) {
        if (this.mAlwaysShow != alwaysShow) {
            this.mAlwaysShow = alwaysShow;
            onStateDependencyChanged(false);
        }
    }

    public boolean isAlwaysShowEnabled() {
        return this.mAlwaysShow;
    }

    private void onStateDependencyChanged(boolean peekIfEnabled) {
        if (!isEnabled()) {
            stop();
        } else if (isAlwaysShowEnabled()) {
            setState(1);
        } else if (this.mState == 1) {
            postAutoHide();
        } else if (peekIfEnabled) {
            setState(1);
            postAutoHide();
        }
        this.mList.resolvePadding();
    }

    public void setScrollBarStyle(int style) {
        if (this.mScrollBarStyle != style) {
            this.mScrollBarStyle = style;
            updateLayout();
        }
    }

    public void stop() {
        setState(0);
    }

    public void setScrollbarPosition(int position) {
        boolean z = true;
        if (position == 0) {
            position = this.mList.isLayoutRtl() ? true : true;
        }
        if (this.mScrollbarPosition != position) {
            this.mScrollbarPosition = position;
            if (position == 1) {
                z = false;
            }
            this.mLayoutFromRight = z;
            this.mPreviewImage.setBackgroundResource(this.mPreviewResId[this.mLayoutFromRight]);
            int textMinWidth = Math.max(0, (this.mPreviewMinWidth - this.mPreviewImage.getPaddingLeft()) - this.mPreviewImage.getPaddingRight());
            this.mPrimaryText.setMinimumWidth(textMinWidth);
            this.mSecondaryText.setMinimumWidth(textMinWidth);
            int textMinHeight = Math.max(0, (this.mPreviewMinHeight - this.mPreviewImage.getPaddingTop()) - this.mPreviewImage.getPaddingBottom());
            this.mPrimaryText.setMinimumHeight(textMinHeight);
            this.mSecondaryText.setMinimumHeight(textMinHeight);
            updateLayout();
        }
    }

    public int getWidth() {
        return this.mWidth;
    }

    @UnsupportedAppUsage
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        updateLayout();
    }

    public void onItemCountChanged(int childCount, int itemCount) {
        if (this.mOldItemCount != itemCount || this.mOldChildCount != childCount) {
            this.mOldItemCount = itemCount;
            this.mOldChildCount = childCount;
            if ((itemCount - childCount > 0) && this.mState != 2) {
                setThumbPos(getPosFromItemCount(this.mList.getFirstVisiblePosition(), childCount, itemCount));
            }
            updateLongList(childCount, itemCount);
        }
    }

    private void updateLongList(int childCount, int itemCount) {
        boolean longList = childCount > 0 && itemCount / childCount >= 4;
        if (this.mLongList != longList) {
            this.mLongList = longList;
            onStateDependencyChanged(false);
        }
    }

    private TextView createPreviewTextView(Context context) {
        LayoutParams params = new LayoutParams(-2, -2);
        TextView textView = new TextView(context);
        textView.setLayoutParams(params);
        textView.setSingleLine(true);
        textView.setEllipsize(TruncateAt.MIDDLE);
        textView.setGravity(17);
        textView.setAlpha(0.0f);
        textView.setLayoutDirection(this.mList.getLayoutDirection());
        return textView;
    }

    public void updateLayout() {
        if (!this.mUpdatingLayout) {
            this.mUpdatingLayout = true;
            updateContainerRect();
            layoutThumb();
            layoutTrack();
            updateOffsetAndRange();
            Rect bounds = this.mTempBounds;
            measurePreview(this.mPrimaryText, bounds);
            applyLayout(this.mPrimaryText, bounds);
            measurePreview(this.mSecondaryText, bounds);
            applyLayout(this.mSecondaryText, bounds);
            if (this.mPreviewImage != null) {
                bounds.left -= this.mPreviewImage.getPaddingLeft();
                bounds.top -= this.mPreviewImage.getPaddingTop();
                bounds.right += this.mPreviewImage.getPaddingRight();
                bounds.bottom += this.mPreviewImage.getPaddingBottom();
                applyLayout(this.mPreviewImage, bounds);
            }
            this.mUpdatingLayout = false;
        }
    }

    private void applyLayout(View view, Rect bounds) {
        view.layout(bounds.left, bounds.top, bounds.right, bounds.bottom);
        view.setPivotX(this.mLayoutFromRight ? (float) (bounds.right - bounds.left) : 0.0f);
    }

    private void measurePreview(View v, Rect out) {
        Rect margins = this.mTempMargins;
        margins.left = this.mPreviewImage.getPaddingLeft();
        margins.top = this.mPreviewImage.getPaddingTop();
        margins.right = this.mPreviewImage.getPaddingRight();
        margins.bottom = this.mPreviewImage.getPaddingBottom();
        if (this.mOverlayPosition == 0) {
            measureFloating(v, margins, out);
        } else {
            measureViewToSide(v, this.mThumbImage, margins, out);
        }
    }

    private void measureViewToSide(View view, View adjacent, Rect margins, Rect out) {
        int marginLeft;
        int marginTop;
        int marginRight;
        int maxWidth;
        int right;
        int left;
        Rect rect = margins;
        if (rect == null) {
            marginLeft = 0;
            marginTop = 0;
            marginRight = 0;
        } else {
            marginLeft = rect.left;
            marginTop = rect.top;
            marginRight = rect.right;
        }
        Rect container = this.mContainerRect;
        int containerWidth = container.width();
        if (adjacent == null) {
            maxWidth = containerWidth;
        } else if (this.mLayoutFromRight) {
            maxWidth = adjacent.getLeft();
        } else {
            maxWidth = containerWidth - adjacent.getRight();
        }
        int adjMaxHeight = Math.max(0, container.height());
        int adjMaxWidth = Math.max(0, (maxWidth - marginLeft) - marginRight);
        view.measure(MeasureSpec.makeMeasureSpec(adjMaxWidth, Integer.MIN_VALUE), MeasureSpec.makeSafeMeasureSpec(adjMaxHeight, 0));
        int width = Math.min(adjMaxWidth, view.getMeasuredWidth());
        if (this.mLayoutFromRight) {
            right = (adjacent == null ? container.right : adjacent.getLeft()) - marginRight;
            left = right - width;
        } else {
            left = (adjacent == null ? container.left : adjacent.getRight()) + marginLeft;
            right = left + width;
        }
        int top = marginTop;
        out.set(left, top, right, top + view.getMeasuredHeight());
    }

    private void measureFloating(View preview, Rect margins, Rect out) {
        int marginLeft;
        int marginTop;
        int marginRight;
        Rect rect = margins;
        if (rect == null) {
            marginLeft = 0;
            marginTop = 0;
            marginRight = 0;
        } else {
            marginLeft = rect.left;
            marginTop = rect.top;
            marginRight = rect.right;
        }
        Rect container = this.mContainerRect;
        int containerWidth = container.width();
        View view = preview;
        view.measure(MeasureSpec.makeMeasureSpec(Math.max(0, (containerWidth - marginLeft) - marginRight), Integer.MIN_VALUE), MeasureSpec.makeSafeMeasureSpec(Math.max(0, container.height()), 0));
        int containerHeight = container.height();
        int width = preview.getMeasuredWidth();
        int top = ((containerHeight / 10) + marginTop) + container.top;
        int left = ((containerWidth - width) / 2) + container.left;
        marginTop = out;
        marginTop.set(left, top, left + width, preview.getMeasuredHeight() + top);
    }

    private void updateContainerRect() {
        AbsListView list = this.mList;
        list.resolvePadding();
        Rect container = this.mContainerRect;
        container.left = 0;
        container.top = 0;
        container.right = list.getWidth();
        container.bottom = list.getHeight();
        int scrollbarStyle = this.mScrollBarStyle;
        if (scrollbarStyle == 16777216 || scrollbarStyle == 0) {
            container.left += list.getPaddingLeft();
            container.top += list.getPaddingTop();
            container.right -= list.getPaddingRight();
            container.bottom -= list.getPaddingBottom();
            if (scrollbarStyle == 16777216) {
                int width = getWidth();
                if (this.mScrollbarPosition == 2) {
                    container.right += width;
                } else {
                    container.left -= width;
                }
            }
        }
    }

    private void layoutThumb() {
        Rect bounds = this.mTempBounds;
        measureViewToSide(this.mThumbImage, null, null, bounds);
        applyLayout(this.mThumbImage, bounds);
    }

    private void layoutTrack() {
        int top;
        int bottom;
        View track = this.mTrackImage;
        View thumb = this.mThumbImage;
        Rect container = this.mContainerRect;
        track.measure(MeasureSpec.makeMeasureSpec(Math.max(0, container.width()), Integer.MIN_VALUE), MeasureSpec.makeSafeMeasureSpec(Math.max(0, container.height()), 0));
        if (this.mThumbPosition == 1) {
            top = container.top;
            bottom = container.bottom;
        } else {
            top = thumb.getHeight() / 2;
            int i = container.top + top;
            bottom = container.bottom - top;
            top = i;
        }
        int trackWidth = track.getMeasuredWidth();
        int left = thumb.getLeft() + ((thumb.getWidth() - trackWidth) / 2);
        track.layout(left, top, left + trackWidth, bottom);
    }

    private void updateOffsetAndRange() {
        float min;
        float max;
        View trackImage = this.mTrackImage;
        View thumbImage = this.mThumbImage;
        if (this.mThumbPosition == 1) {
            float halfThumbHeight = ((float) thumbImage.getHeight()) / 2.0f;
            min = ((float) trackImage.getTop()) + halfThumbHeight;
            max = ((float) trackImage.getBottom()) - halfThumbHeight;
        } else {
            min = (float) trackImage.getTop();
            max = (float) trackImage.getBottom();
        }
        this.mThumbOffset = min;
        this.mThumbRange = max - min;
    }

    @UnsupportedAppUsage
    private void setState(int state) {
        this.mList.removeCallbacks(this.mDeferHide);
        if (this.mAlwaysShow && state == 0) {
            state = 1;
        }
        if (state != this.mState) {
            if (state == 0) {
                transitionToHidden();
            } else if (state == 1) {
                transitionToVisible();
            } else if (state == 2) {
                if (transitionPreviewLayout(this.mCurrentSection)) {
                    transitionToDragging();
                } else {
                    transitionToVisible();
                }
            }
            this.mState = state;
            refreshDrawablePressedState();
        }
    }

    private void refreshDrawablePressedState() {
        boolean isPressed = this.mState == 2;
        this.mThumbImage.setPressed(isPressed);
        this.mTrackImage.setPressed(isPressed);
    }

    private void transitionToHidden() {
        AnimatorSet animatorSet = this.mDecorAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        Animator fadeOut = groupAnimatorOfFloat(View.ALPHA, 0.0f, this.mThumbImage, this.mTrackImage, this.mPreviewImage, this.mPrimaryText, this.mSecondaryText).setDuration(300);
        Animator slideOut = groupAnimatorOfFloat(View.TRANSLATION_X, (float) (this.mLayoutFromRight ? this.mThumbImage.getWidth() : -this.mThumbImage.getWidth()), this.mThumbImage, this.mTrackImage).setDuration(300);
        this.mDecorAnimation = new AnimatorSet();
        this.mDecorAnimation.playTogether(fadeOut, slideOut);
        this.mDecorAnimation.start();
        this.mShowingPreview = false;
    }

    private void transitionToVisible() {
        AnimatorSet animatorSet = this.mDecorAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        Animator fadeIn = groupAnimatorOfFloat(View.ALPHA, 1.0f, this.mThumbImage, this.mTrackImage).setDuration(150);
        Animator fadeOut = groupAnimatorOfFloat(View.ALPHA, 0.0f, this.mPreviewImage, this.mPrimaryText, this.mSecondaryText).setDuration(300);
        Animator slideIn = groupAnimatorOfFloat(View.TRANSLATION_X, 0.0f, this.mThumbImage, this.mTrackImage).setDuration(150);
        this.mDecorAnimation = new AnimatorSet();
        this.mDecorAnimation.playTogether(fadeIn, fadeOut, slideIn);
        this.mDecorAnimation.start();
        this.mShowingPreview = false;
    }

    private void transitionToDragging() {
        AnimatorSet animatorSet = this.mDecorAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        Animator fadeIn = groupAnimatorOfFloat(View.ALPHA, 1.0f, this.mThumbImage, this.mTrackImage, this.mPreviewImage).setDuration(150);
        Animator slideIn = groupAnimatorOfFloat(View.TRANSLATION_X, 0.0f, this.mThumbImage, this.mTrackImage).setDuration(150);
        this.mDecorAnimation = new AnimatorSet();
        this.mDecorAnimation.playTogether(fadeIn, slideIn);
        this.mDecorAnimation.start();
        this.mShowingPreview = true;
    }

    private void postAutoHide() {
        this.mList.removeCallbacks(this.mDeferHide);
        this.mList.postDelayed(this.mDeferHide, FADE_TIMEOUT);
    }

    public void onScroll(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean z = false;
        if (isEnabled()) {
            if (totalItemCount - visibleItemCount > 0) {
                z = true;
            }
            if (z && this.mState != 2) {
                setThumbPos(getPosFromItemCount(firstVisibleItem, visibleItemCount, totalItemCount));
            }
            this.mScrollCompleted = true;
            if (this.mFirstVisibleItem != firstVisibleItem) {
                this.mFirstVisibleItem = firstVisibleItem;
                if (this.mState != 2) {
                    setState(1);
                    postAutoHide();
                }
            }
            return;
        }
        setState(0);
    }

    private void getSectionsFromIndexer() {
        this.mSectionIndexer = null;
        Adapter adapter = this.mList.getAdapter();
        if (adapter instanceof HeaderViewListAdapter) {
            this.mHeaderCount = ((HeaderViewListAdapter) adapter).getHeadersCount();
            adapter = ((HeaderViewListAdapter) adapter).getWrappedAdapter();
        }
        if (adapter instanceof ExpandableListConnector) {
            ExpandableListAdapter expAdapter = ((ExpandableListConnector) adapter).getAdapter();
            if (expAdapter instanceof SectionIndexer) {
                this.mSectionIndexer = (SectionIndexer) expAdapter;
                this.mListAdapter = adapter;
                this.mSections = this.mSectionIndexer.getSections();
            }
        } else if (adapter instanceof SectionIndexer) {
            this.mListAdapter = adapter;
            this.mSectionIndexer = (SectionIndexer) adapter;
            this.mSections = this.mSectionIndexer.getSections();
        } else {
            this.mListAdapter = adapter;
            this.mSections = null;
        }
    }

    public void onSectionsChanged() {
        this.mListAdapter = null;
    }

    private void scrollTo(float position) {
        int sectionIndex;
        this.mScrollCompleted = false;
        int count = this.mList.getCount();
        Object[] sections = this.mSections;
        int sectionCount = sections == null ? 0 : sections.length;
        if (sections == null || sectionCount <= 1) {
            int i = sectionCount;
            int index = MathUtils.constrain((int) (((float) count) * position), 0, count - 1);
            AbsListView absListView = this.mList;
            if (absListView instanceof ExpandableListView) {
                ExpandableListView expList = (ExpandableListView) absListView;
                expList.setSelectionFromTop(expList.getFlatListPosition(ExpandableListView.getPackedPositionForGroup(this.mHeaderCount + index)), 0);
            } else if (absListView instanceof ListView) {
                ((ListView) absListView).setSelectionFromTop(this.mHeaderCount + index, 0);
            } else {
                absListView.setSelection(this.mHeaderCount + index);
            }
            sectionIndex = -1;
        } else {
            int sections2;
            int exactSection = MathUtils.constrain((int) (((float) sectionCount) * position), 0, sectionCount - 1);
            int targetSection = exactSection;
            int targetIndex = this.mSectionIndexer.getPositionForSection(targetSection);
            sectionIndex = targetSection;
            int nextIndex = count;
            int prevIndex = targetIndex;
            int prevSection = targetSection;
            int nextSection = targetSection + 1;
            if (targetSection < sectionCount - 1) {
                nextIndex = this.mSectionIndexer.getPositionForSection(targetSection + 1);
            }
            if (nextIndex == targetIndex) {
                while (targetSection > 0) {
                    targetSection--;
                    prevIndex = this.mSectionIndexer.getPositionForSection(targetSection);
                    if (prevIndex == targetIndex) {
                        if (targetSection == 0) {
                            sectionIndex = 0;
                            break;
                        }
                    }
                    prevSection = targetSection;
                    sectionIndex = targetSection;
                    break;
                }
            }
            int nextNextSection = nextSection + 1;
            while (nextNextSection < sectionCount && this.mSectionIndexer.getPositionForSection(nextNextSection) == nextIndex) {
                nextNextSection++;
                nextSection++;
            }
            float prevPosition = ((float) prevSection) / ((float) sectionCount);
            float nextPosition = ((float) nextSection) / ((float) sectionCount);
            float snapThreshold;
            if (count == 0) {
                snapThreshold = Float.MAX_VALUE;
                Object[] objArr = sections;
            } else {
                snapThreshold = 0.125f / ((float) count);
            }
            if (prevSection != exactSection || position - prevPosition >= snapThreshold) {
                sections2 = ((int) ((((float) (nextIndex - prevIndex)) * (position - prevPosition)) / (nextPosition - prevPosition))) + prevIndex;
            } else {
                sections2 = prevIndex;
            }
            sections = MathUtils.constrain(sections2, (int) 0.0f, count - 1);
            AbsListView absListView2 = this.mList;
            if (absListView2 instanceof ExpandableListView) {
                ExpandableListView expList2 = (ExpandableListView) absListView2;
                expList2.setSelectionFromTop(expList2.getFlatListPosition(ExpandableListView.getPackedPositionForGroup(this.mHeaderCount + sections)), 0);
            } else {
                int i2 = exactSection;
                if (absListView2 instanceof ListView) {
                    ((ListView) absListView2).setSelectionFromTop(this.mHeaderCount + sections, 0);
                } else {
                    absListView2.setSelection(this.mHeaderCount + sections);
                }
            }
        }
        if (this.mCurrentSection != sectionIndex) {
            this.mCurrentSection = sectionIndex;
            boolean hasPreview = transitionPreviewLayout(sectionIndex);
            if (!this.mShowingPreview && hasPreview) {
                transitionToDragging();
            } else if (this.mShowingPreview && !hasPreview) {
                transitionToVisible();
            }
        }
    }

    private boolean transitionPreviewLayout(int sectionIndex) {
        TextView showing;
        TextView target;
        int i = sectionIndex;
        Object[] sections = this.mSections;
        CharSequence text = null;
        if (sections != null && i >= 0 && i < sections.length) {
            Object section = sections[i];
            if (section != null) {
                text = section.toString();
            }
        }
        Rect bounds = this.mTempBounds;
        View preview = this.mPreviewImage;
        if (this.mShowingPrimary) {
            showing = this.mPrimaryText;
            target = this.mSecondaryText;
        } else {
            showing = this.mSecondaryText;
            target = this.mPrimaryText;
        }
        target.setText(text);
        measurePreview(target, bounds);
        applyLayout(target, bounds);
        AnimatorSet animatorSet = this.mPreviewAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        Animator showTarget = animateAlpha(target, 1.0f).setDuration(50);
        Animator hideShowing = animateAlpha(showing, 0.0f).setDuration(50);
        hideShowing.addListener(this.mSwitchPrimaryListener);
        bounds.left -= preview.getPaddingLeft();
        bounds.top -= preview.getPaddingTop();
        bounds.right += preview.getPaddingRight();
        bounds.bottom += preview.getPaddingBottom();
        Animator resizePreview = animateBounds(preview, bounds);
        resizePreview.setDuration(100);
        this.mPreviewAnimation = new AnimatorSet();
        Builder builder = this.mPreviewAnimation.play(hideShowing).with(showTarget);
        builder.with(resizePreview);
        int previewWidth = (preview.getWidth() - preview.getPaddingLeft()) - preview.getPaddingRight();
        int targetWidth = target.getWidth();
        if (targetWidth > previewWidth) {
            target.setScaleX(((float) previewWidth) / ((float) targetWidth));
            builder.with(animateScaleX(target, 1.0f).setDuration(100));
        } else {
            target.setScaleX(1.0f);
        }
        i = showing.getWidth();
        if (i > targetWidth) {
            float scale = ((float) targetWidth) / ((float) i);
            builder.with(animateScaleX(showing, scale).setDuration(100));
        }
        this.mPreviewAnimation.start();
        return TextUtils.isEmpty(text) ^ 1;
    }

    private void setThumbPos(float position) {
        float previewPos;
        float thumbMiddle = (this.mThumbRange * position) + this.mThumbOffset;
        ImageView imageView = this.mThumbImage;
        imageView.setTranslationY(thumbMiddle - (((float) imageView.getHeight()) / 2.0f));
        View previewImage = this.mPreviewImage;
        float previewHalfHeight = ((float) previewImage.getHeight()) / 2.0f;
        int i = this.mOverlayPosition;
        if (i == 1) {
            previewPos = thumbMiddle;
        } else if (i != 2) {
            previewPos = 0.0f;
        } else {
            previewPos = thumbMiddle - previewHalfHeight;
        }
        Rect container = this.mContainerRect;
        float previewTop = MathUtils.constrain(previewPos, ((float) container.top) + previewHalfHeight, ((float) container.bottom) - previewHalfHeight) - previewHalfHeight;
        previewImage.setTranslationY(previewTop);
        this.mPrimaryText.setTranslationY(previewTop);
        this.mSecondaryText.setTranslationY(previewTop);
    }

    private float getPosFromMotionEvent(float y) {
        float f = this.mThumbRange;
        if (f <= 0.0f) {
            return 0.0f;
        }
        return MathUtils.constrain((y - this.mThumbOffset) / f, 0.0f, 1.0f);
    }

    /* JADX WARNING: Removed duplicated region for block: B:55:0x00f1  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00f0 A:{RETURN} */
    private float getPosFromItemCount(int r19, int r20, int r21) {
        /*
        r18 = this;
        r0 = r18;
        r1 = r19;
        r2 = r20;
        r3 = r21;
        r4 = r0.mSectionIndexer;
        if (r4 == 0) goto L_0x0010;
    L_0x000c:
        r5 = r0.mListAdapter;
        if (r5 != 0) goto L_0x0013;
    L_0x0010:
        r18.getSectionsFromIndexer();
    L_0x0013:
        r5 = 0;
        if (r2 == 0) goto L_0x00f7;
    L_0x0016:
        if (r3 != 0) goto L_0x001a;
    L_0x0018:
        goto L_0x00f7;
    L_0x001a:
        r6 = 0;
        if (r4 == 0) goto L_0x0026;
    L_0x001d:
        r7 = r0.mSections;
        if (r7 == 0) goto L_0x0026;
    L_0x0021:
        r7 = r7.length;
        if (r7 <= 0) goto L_0x0026;
    L_0x0024:
        r7 = 1;
        goto L_0x0027;
    L_0x0026:
        r7 = r6;
    L_0x0027:
        if (r7 == 0) goto L_0x00ee;
    L_0x0029:
        r8 = r0.mMatchDragPosition;
        if (r8 != 0) goto L_0x002f;
    L_0x002d:
        goto L_0x00ee;
    L_0x002f:
        r8 = r0.mHeaderCount;
        r1 = r1 - r8;
        if (r1 >= 0) goto L_0x0035;
    L_0x0034:
        return r5;
    L_0x0035:
        r3 = r3 - r8;
        r5 = r0.mList;
        r5 = r5.getChildAt(r6);
        if (r5 == 0) goto L_0x0058;
    L_0x003e:
        r6 = r5.getHeight();
        if (r6 != 0) goto L_0x0045;
    L_0x0044:
        goto L_0x0058;
    L_0x0045:
        r6 = r0.mList;
        r6 = r6.getPaddingTop();
        r8 = r5.getTop();
        r6 = r6 - r8;
        r6 = (float) r6;
        r8 = r5.getHeight();
        r8 = (float) r8;
        r6 = r6 / r8;
        goto L_0x0059;
    L_0x0058:
        r6 = 0;
    L_0x0059:
        r8 = r4.getSectionForPosition(r1);
        r9 = r4.getPositionForSection(r8);
        r10 = r0.mSections;
        r10 = r10.length;
        r11 = r10 + -1;
        if (r8 >= r11) goto L_0x0077;
    L_0x0068:
        r11 = r8 + 1;
        if (r11 >= r10) goto L_0x0073;
    L_0x006c:
        r11 = r8 + 1;
        r11 = r4.getPositionForSection(r11);
        goto L_0x0075;
    L_0x0073:
        r11 = r3 + -1;
    L_0x0075:
        r11 = r11 - r9;
        goto L_0x0079;
    L_0x0077:
        r11 = r3 - r9;
    L_0x0079:
        if (r11 != 0) goto L_0x007d;
    L_0x007b:
        r12 = 0;
        goto L_0x0083;
    L_0x007d:
        r12 = (float) r1;
        r12 = r12 + r6;
        r13 = (float) r9;
        r12 = r12 - r13;
        r13 = (float) r11;
        r12 = r12 / r13;
    L_0x0083:
        r13 = (float) r8;
        r13 = r13 + r12;
        r14 = (float) r10;
        r13 = r13 / r14;
        if (r1 <= 0) goto L_0x00e9;
    L_0x0089:
        r14 = r1 + r2;
        if (r14 != r3) goto L_0x00e9;
    L_0x008d:
        r14 = r0.mList;
        r15 = r2 + -1;
        r14 = r14.getChildAt(r15);
        r15 = r0.mList;
        r15 = r15.getPaddingBottom();
        r19 = r1;
        r1 = r0.mList;
        r1 = r1.getClipToPadding();
        if (r1 == 0) goto L_0x00bb;
    L_0x00a5:
        r1 = r14.getHeight();
        r21 = r1;
        r1 = r0.mList;
        r1 = r1.getHeight();
        r1 = r1 - r15;
        r16 = r14.getTop();
        r1 = r1 - r16;
        r0 = r21;
        goto L_0x00d0;
    L_0x00bb:
        r1 = r14.getHeight();
        r1 = r1 + r15;
        r21 = r1;
        r1 = r0.mList;
        r1 = r1.getHeight();
        r16 = r14.getTop();
        r1 = r1 - r16;
        r0 = r21;
    L_0x00d0:
        if (r1 <= 0) goto L_0x00e4;
    L_0x00d2:
        if (r0 <= 0) goto L_0x00e4;
    L_0x00d4:
        r16 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r16 = r16 - r13;
        r21 = r3;
        r3 = (float) r1;
        r17 = r1;
        r1 = (float) r0;
        r3 = r3 / r1;
        r16 = r16 * r3;
        r13 = r13 + r16;
        goto L_0x00ed;
    L_0x00e4:
        r17 = r1;
        r21 = r3;
        goto L_0x00ed;
    L_0x00e9:
        r19 = r1;
        r21 = r3;
    L_0x00ed:
        return r13;
    L_0x00ee:
        if (r2 != r3) goto L_0x00f1;
    L_0x00f0:
        return r5;
    L_0x00f1:
        r0 = (float) r1;
        r5 = r3 - r2;
        r5 = (float) r5;
        r0 = r0 / r5;
        return r0;
    L_0x00f7:
        return r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.FastScroller.getPosFromItemCount(int, int, int):float");
    }

    private void cancelFling() {
        MotionEvent cancelFling = MotionEvent.obtain(null, 0, 3, 0.0f, 0.0f, 0);
        this.mList.onTouchEvent(cancelFling);
        cancelFling.recycle();
    }

    private void cancelPendingDrag() {
        this.mPendingDrag = -1;
    }

    private void startPendingDrag() {
        this.mPendingDrag = SystemClock.uptimeMillis() + TAP_TIMEOUT;
    }

    private void beginDrag() {
        this.mPendingDrag = -1;
        setState(2);
        if (this.mListAdapter == null && this.mList != null) {
            getSectionsFromIndexer();
        }
        AbsListView absListView = this.mList;
        if (absListView != null) {
            absListView.requestDisallowInterceptTouchEvent(true);
            this.mList.reportScrollStateChange(1);
        }
        cancelFling();
    }

    /* JADX WARNING: Missing block: B:9:0x0015, code skipped:
            if (r0 != 3) goto L_0x006f;
     */
    @android.annotation.UnsupportedAppUsage
    public boolean onInterceptTouchEvent(android.view.MotionEvent r7) {
        /*
        r6 = this;
        r0 = r6.isEnabled();
        r1 = 0;
        if (r0 != 0) goto L_0x0008;
    L_0x0007:
        return r1;
    L_0x0008:
        r0 = r7.getActionMasked();
        r2 = 1;
        if (r0 == 0) goto L_0x004f;
    L_0x000f:
        if (r0 == r2) goto L_0x004b;
    L_0x0011:
        r2 = 2;
        if (r0 == r2) goto L_0x0018;
    L_0x0014:
        r2 = 3;
        if (r0 == r2) goto L_0x004b;
    L_0x0017:
        goto L_0x006f;
    L_0x0018:
        r0 = r7.getX();
        r2 = r7.getY();
        r0 = r6.isPointInside(r0, r2);
        if (r0 != 0) goto L_0x002a;
    L_0x0026:
        r6.cancelPendingDrag();
        goto L_0x006f;
    L_0x002a:
        r2 = r6.mPendingDrag;
        r4 = 0;
        r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r0 < 0) goto L_0x006f;
    L_0x0032:
        r4 = android.os.SystemClock.uptimeMillis();
        r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r0 > 0) goto L_0x006f;
    L_0x003a:
        r6.beginDrag();
        r0 = r6.mInitialTouchY;
        r0 = r6.getPosFromMotionEvent(r0);
        r6.scrollTo(r0);
        r1 = r6.onTouchEvent(r7);
        return r1;
    L_0x004b:
        r6.cancelPendingDrag();
        goto L_0x006f;
    L_0x004f:
        r0 = r7.getX();
        r3 = r7.getY();
        r0 = r6.isPointInside(r0, r3);
        if (r0 == 0) goto L_0x006f;
    L_0x005d:
        r0 = r6.mList;
        r0 = r0.isInScrollingContainer();
        if (r0 != 0) goto L_0x0066;
    L_0x0065:
        return r2;
    L_0x0066:
        r0 = r7.getY();
        r6.mInitialTouchY = r0;
        r6.startPendingDrag();
    L_0x006f:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.FastScroller.onInterceptTouchEvent(android.view.MotionEvent):boolean");
    }

    public boolean onInterceptHoverEvent(MotionEvent ev) {
        if (!isEnabled()) {
            return false;
        }
        int actionMasked = ev.getActionMasked();
        if ((actionMasked == 9 || actionMasked == 7) && this.mState == 0 && isPointInside(ev.getX(), ev.getY())) {
            setState(1);
            postAutoHide();
        }
        return false;
    }

    public PointerIcon onResolvePointerIcon(MotionEvent event, int pointerIndex) {
        if (this.mState == 2 || isPointInside(event.getX(), event.getY())) {
            return PointerIcon.getSystemIcon(this.mList.getContext(), 1000);
        }
        return null;
    }

    @UnsupportedAppUsage
    public boolean onTouchEvent(MotionEvent me) {
        if (!isEnabled()) {
            return false;
        }
        int actionMasked = me.getActionMasked();
        if (actionMasked != 0) {
            float pos;
            if (actionMasked == 1) {
                if (this.mPendingDrag >= 0) {
                    beginDrag();
                    pos = getPosFromMotionEvent(me.getY());
                    setThumbPos(pos);
                    scrollTo(pos);
                }
                if (this.mState == 2) {
                    AbsListView absListView = this.mList;
                    if (absListView != null) {
                        absListView.requestDisallowInterceptTouchEvent(false);
                        this.mList.reportScrollStateChange(0);
                    }
                    setState(1);
                    postAutoHide();
                    return true;
                }
            } else if (actionMasked == 2) {
                if (this.mPendingDrag >= 0 && Math.abs(me.getY() - this.mInitialTouchY) > ((float) this.mScaledTouchSlop)) {
                    beginDrag();
                }
                if (this.mState == 2) {
                    pos = getPosFromMotionEvent(me.getY());
                    setThumbPos(pos);
                    if (this.mScrollCompleted) {
                        scrollTo(pos);
                    }
                    return true;
                }
            } else if (actionMasked == 3) {
                cancelPendingDrag();
            }
        } else if (isPointInside(me.getX(), me.getY()) && !this.mList.isInScrollingContainer()) {
            beginDrag();
            return true;
        }
        return false;
    }

    private boolean isPointInside(float x, float y) {
        return isPointInsideX(x) && (this.mTrackDrawable != null || isPointInsideY(y));
    }

    private boolean isPointInsideX(float x) {
        float offset = this.mThumbImage.getTranslationX();
        float right = ((float) this.mThumbImage.getRight()) + offset;
        float targetSizeDiff = ((float) this.mMinimumTouchTarget) - (right - (((float) this.mThumbImage.getLeft()) + offset));
        float adjust = 0.0f;
        if (targetSizeDiff > 0.0f) {
            adjust = targetSizeDiff;
        }
        boolean z = true;
        if (this.mLayoutFromRight) {
            if (x < ((float) this.mThumbImage.getLeft()) - adjust) {
                z = false;
            }
            return z;
        }
        if (x > ((float) this.mThumbImage.getRight()) + adjust) {
            z = false;
        }
        return z;
    }

    private boolean isPointInsideY(float y) {
        float offset = this.mThumbImage.getTranslationY();
        float top = ((float) this.mThumbImage.getTop()) + offset;
        float bottom = ((float) this.mThumbImage.getBottom()) + offset;
        float targetSizeDiff = ((float) this.mMinimumTouchTarget) - (bottom - top);
        float adjust = 0.0f;
        if (targetSizeDiff > 0.0f) {
            adjust = targetSizeDiff / 2.0f;
        }
        return y >= top - adjust && y <= bottom + adjust;
    }

    private static Animator groupAnimatorOfFloat(Property<View, Float> property, float value, View... views) {
        AnimatorSet animSet = new AnimatorSet();
        Builder builder = null;
        for (int i = views.length - 1; i >= 0; i--) {
            Animator anim = ObjectAnimator.ofFloat(views[i], (Property) property, value);
            if (builder == null) {
                builder = animSet.play(anim);
            } else {
                builder.with(anim);
            }
        }
        return animSet;
    }

    private static Animator animateScaleX(View v, float target) {
        return ObjectAnimator.ofFloat((Object) v, View.SCALE_X, target);
    }

    private static Animator animateAlpha(View v, float alpha) {
        return ObjectAnimator.ofFloat((Object) v, View.ALPHA, alpha);
    }

    private static Animator animateBounds(View v, Rect bounds) {
        PropertyValuesHolder left = PropertyValuesHolder.ofInt(LEFT, bounds.left);
        PropertyValuesHolder top = PropertyValuesHolder.ofInt(TOP, bounds.top);
        PropertyValuesHolder right = PropertyValuesHolder.ofInt(RIGHT, bounds.right);
        PropertyValuesHolder bottom = PropertyValuesHolder.ofInt(BOTTOM, bounds.bottom);
        return ObjectAnimator.ofPropertyValuesHolder(v, left, top, right, bottom);
    }
}
