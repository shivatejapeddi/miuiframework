package android.view;

import android.animation.LayoutTransition;
import android.animation.LayoutTransition.TransitionListener;
import android.annotation.UnsupportedAppUsage;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Region.Op;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pools.SimplePool;
import android.util.Pools.SynchronizedPool;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.ActionMode.Callback;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.CanvasProvider;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewDebug.FlagToString;
import android.view.ViewDebug.IntToString;
import android.view.WindowInsetsAnimationListener.InsetsAnimation;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LayoutAnimationController.AnimationParameters;
import android.view.animation.Transformation;
import android.view.autofill.Helper;
import android.view.inspector.InspectionCompanion.UninitializedPropertyMapException;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import com.android.internal.R;
import com.miui.internal.variable.api.v29.Android_View_ViewGroup.Extension;
import com.miui.internal.variable.api.v29.Android_View_ViewGroup.Interface;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public abstract class ViewGroup extends View implements ViewParent, ViewManager {
    private static final int ARRAY_CAPACITY_INCREMENT = 12;
    private static final int ARRAY_INITIAL_CAPACITY = 12;
    private static final int CHILD_LEFT_INDEX = 0;
    private static final int CHILD_TOP_INDEX = 1;
    protected static final int CLIP_TO_PADDING_MASK = 34;
    @UnsupportedAppUsage
    private static final boolean DBG = false;
    private static final int[] DESCENDANT_FOCUSABILITY_FLAGS = new int[]{131072, 262144, 393216};
    private static final int FLAG_ADD_STATES_FROM_CHILDREN = 8192;
    @Deprecated
    private static final int FLAG_ALWAYS_DRAWN_WITH_CACHE = 16384;
    @Deprecated
    private static final int FLAG_ANIMATION_CACHE = 64;
    static final int FLAG_ANIMATION_DONE = 16;
    @Deprecated
    private static final int FLAG_CHILDREN_DRAWN_WITH_CACHE = 32768;
    static final int FLAG_CLEAR_TRANSFORMATION = 256;
    static final int FLAG_CLIP_CHILDREN = 1;
    private static final int FLAG_CLIP_TO_PADDING = 2;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123983692)
    protected static final int FLAG_DISALLOW_INTERCEPT = 524288;
    static final int FLAG_INVALIDATE_REQUIRED = 4;
    static final int FLAG_IS_TRANSITION_GROUP = 16777216;
    static final int FLAG_IS_TRANSITION_GROUP_SET = 33554432;
    private static final int FLAG_LAYOUT_MODE_WAS_EXPLICITLY_SET = 8388608;
    private static final int FLAG_MASK_FOCUSABILITY = 393216;
    private static final int FLAG_NOTIFY_ANIMATION_LISTENER = 512;
    private static final int FLAG_NOTIFY_CHILDREN_ON_DRAWABLE_STATE_CHANGE = 65536;
    static final int FLAG_OPTIMIZE_INVALIDATE = 128;
    private static final int FLAG_PADDING_NOT_NULL = 32;
    private static final int FLAG_PREVENT_DISPATCH_ATTACHED_TO_WINDOW = 4194304;
    private static final int FLAG_RUN_ANIMATION = 8;
    private static final int FLAG_SHOW_CONTEXT_MENU_WITH_COORDS = 536870912;
    private static final int FLAG_SPLIT_MOTION_EVENTS = 2097152;
    private static final int FLAG_START_ACTION_MODE_FOR_CHILD_IS_NOT_TYPED = 268435456;
    private static final int FLAG_START_ACTION_MODE_FOR_CHILD_IS_TYPED = 134217728;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123769647)
    protected static final int FLAG_SUPPORT_STATIC_TRANSFORMATIONS = 2048;
    static final int FLAG_TOUCHSCREEN_BLOCKS_FOCUS = 67108864;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123769377)
    protected static final int FLAG_USE_CHILD_DRAWING_ORDER = 1024;
    public static final int FOCUS_AFTER_DESCENDANTS = 262144;
    public static final int FOCUS_BEFORE_DESCENDANTS = 131072;
    public static final int FOCUS_BLOCK_DESCENDANTS = 393216;
    public static final int LAYOUT_MODE_CLIP_BOUNDS = 0;
    public static int LAYOUT_MODE_DEFAULT = 0;
    public static final int LAYOUT_MODE_OPTICAL_BOUNDS = 1;
    private static final int LAYOUT_MODE_UNDEFINED = -1;
    @Deprecated
    public static final int PERSISTENT_ALL_CACHES = 3;
    @Deprecated
    public static final int PERSISTENT_ANIMATION_CACHE = 1;
    @Deprecated
    public static final int PERSISTENT_NO_CACHE = 0;
    @Deprecated
    public static final int PERSISTENT_SCROLLING_CACHE = 2;
    private static final ActionMode SENTINEL_ACTION_MODE = new ActionMode() {
        public void setTitle(CharSequence title) {
        }

        public void setTitle(int resId) {
        }

        public void setSubtitle(CharSequence subtitle) {
        }

        public void setSubtitle(int resId) {
        }

        public void setCustomView(View view) {
        }

        public void invalidate() {
        }

        public void finish() {
        }

        public Menu getMenu() {
            return null;
        }

        public CharSequence getTitle() {
            return null;
        }

        public CharSequence getSubtitle() {
            return null;
        }

        public View getCustomView() {
            return null;
        }

        public MenuInflater getMenuInflater() {
            return null;
        }
    };
    private static final String TAG = "ViewGroup";
    private static float[] sDebugLines;
    private AnimationListener mAnimationListener;
    Paint mCachePaint;
    @ExportedProperty(category = "layout")
    private int mChildCountWithTransientState;
    private Transformation mChildTransformation;
    int mChildUnhandledKeyListeners;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private View[] mChildren;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private int mChildrenCount;
    private HashSet<View> mChildrenInterestedInDrag;
    private View mCurrentDragChild;
    private DragEvent mCurrentDragStartEvent;
    private View mDefaultFocus;
    @UnsupportedAppUsage
    protected ArrayList<View> mDisappearingChildren;
    private HoverTarget mFirstHoverTarget;
    @UnsupportedAppUsage
    private TouchTarget mFirstTouchTarget;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private View mFocused;
    View mFocusedInCluster;
    @ExportedProperty(flagMapping = {@FlagToString(equals = 1, mask = 1, name = "CLIP_CHILDREN"), @FlagToString(equals = 2, mask = 2, name = "CLIP_TO_PADDING"), @FlagToString(equals = 32, mask = 32, name = "PADDING_NOT_NULL")}, formatToHexString = true)
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123769411)
    protected int mGroupFlags;
    private boolean mHoveredSelf;
    RectF mInvalidateRegion;
    Transformation mInvalidationTransformation;
    private boolean mIsInterestedInDrag;
    @ExportedProperty(category = "events")
    private int mLastTouchDownIndex;
    @ExportedProperty(category = "events")
    private long mLastTouchDownTime;
    @ExportedProperty(category = "events")
    private float mLastTouchDownX;
    @ExportedProperty(category = "events")
    private float mLastTouchDownY;
    private LayoutAnimationController mLayoutAnimationController;
    private boolean mLayoutCalledWhileSuppressed;
    private int mLayoutMode;
    private TransitionListener mLayoutTransitionListener;
    private PointF mLocalPoint;
    private int mNestedScrollAxes;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123768704)
    protected OnHierarchyChangeListener mOnHierarchyChangeListener;
    @UnsupportedAppUsage
    protected int mPersistentDrawingCache;
    private ArrayList<View> mPreSortedChildren;
    boolean mSuppressLayout;
    private float[] mTempPoint;
    private View mTooltipHoverTarget;
    private boolean mTooltipHoveredSelf;
    private List<Integer> mTransientIndices;
    private List<View> mTransientViews;
    private LayoutTransition mTransition;
    private ArrayList<View> mTransitioningViews;
    private ArrayList<View> mVisibilityChangingChildren;

    static class ChildListForAccessibility {
        private static final int MAX_POOL_SIZE = 32;
        private static final SynchronizedPool<ChildListForAccessibility> sPool = new SynchronizedPool(32);
        private final ArrayList<View> mChildren = new ArrayList();
        private final ArrayList<ViewLocationHolder> mHolders = new ArrayList();

        ChildListForAccessibility() {
        }

        public static ChildListForAccessibility obtain(ViewGroup parent, boolean sort) {
            ChildListForAccessibility list = (ChildListForAccessibility) sPool.acquire();
            if (list == null) {
                list = new ChildListForAccessibility();
            }
            list.init(parent, sort);
            return list;
        }

        public void recycle() {
            clear();
            sPool.release(this);
        }

        public int getChildCount() {
            return this.mChildren.size();
        }

        public View getChildAt(int index) {
            return (View) this.mChildren.get(index);
        }

        private void init(ViewGroup parent, boolean sort) {
            ArrayList<View> children = this.mChildren;
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                children.add(parent.getChildAt(i));
            }
            if (sort) {
                int i2;
                ArrayList<ViewLocationHolder> holders = this.mHolders;
                for (i2 = 0; i2 < childCount; i2++) {
                    holders.add(ViewLocationHolder.obtain(parent, (View) children.get(i2)));
                }
                sort(holders);
                for (i2 = 0; i2 < childCount; i2++) {
                    ViewLocationHolder holder = (ViewLocationHolder) holders.get(i2);
                    children.set(i2, holder.mView);
                    holder.recycle();
                }
                holders.clear();
            }
        }

        private void sort(ArrayList<ViewLocationHolder> holders) {
            try {
                ViewLocationHolder.setComparisonStrategy(1);
                Collections.sort(holders);
            } catch (IllegalArgumentException e) {
                ViewLocationHolder.setComparisonStrategy(2);
                Collections.sort(holders);
            }
        }

        private void clear() {
            this.mChildren.clear();
        }
    }

    private static class ChildListForAutofill extends ArrayList<View> {
        private static final int MAX_POOL_SIZE = 32;
        private static final SimplePool<ChildListForAutofill> sPool = new SimplePool(32);

        private ChildListForAutofill() {
        }

        public static ChildListForAutofill obtain() {
            ChildListForAutofill list = (ChildListForAutofill) sPool.acquire();
            if (list == null) {
                return new ChildListForAutofill();
            }
            return list;
        }

        public void recycle() {
            clear();
            sPool.release(this);
        }
    }

    private static final class HoverTarget {
        private static final int MAX_RECYCLED = 32;
        private static HoverTarget sRecycleBin;
        private static final Object sRecycleLock = new Object[0];
        private static int sRecycledCount;
        public View child;
        public HoverTarget next;

        private HoverTarget() {
        }

        public static HoverTarget obtain(View child) {
            if (child != null) {
                HoverTarget target;
                synchronized (sRecycleLock) {
                    if (sRecycleBin == null) {
                        target = new HoverTarget();
                    } else {
                        target = sRecycleBin;
                        sRecycleBin = target.next;
                        sRecycledCount--;
                        target.next = null;
                    }
                }
                target.child = child;
                return target;
            }
            throw new IllegalArgumentException("child must be non-null");
        }

        public void recycle() {
            if (this.child != null) {
                synchronized (sRecycleLock) {
                    if (sRecycledCount < 32) {
                        this.next = sRecycleBin;
                        sRecycleBin = this;
                        sRecycledCount++;
                    } else {
                        this.next = null;
                    }
                    this.child = null;
                }
                return;
            }
            throw new IllegalStateException("already recycled once");
        }
    }

    private static class Impl implements Interface {
        private Impl() {
        }

        /* synthetic */ Impl(AnonymousClass1 x0) {
            this();
        }

        public void addInArray(ViewGroup viewGroup, View view, int i) {
            viewGroup.originalAddInArray(view, i);
        }

        public void removeFromArray(ViewGroup viewGroup, int i) {
            viewGroup.originalRemoveFromArray(i);
        }

        public void removeFromArray(ViewGroup viewGroup, int i, int i1) {
            viewGroup.originalRemoveFromArray(i, i1);
        }

        public void onChildVisibilityChanged(ViewGroup viewGroup, View view, int i, int i1) {
            viewGroup.originalOnChildVisibilityChanged(view, i, i1);
        }

        public boolean resolveLayoutDirection(ViewGroup viewGroup) {
            return viewGroup.originalResolveLayoutDirection();
        }

        public void init(ViewGroup viewGroup, Context context, AttributeSet attributeSet, int i, int i1) {
        }
    }

    public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<ViewGroup> {
        private int mAddStatesFromChildrenId;
        private int mAlwaysDrawnWithCacheId;
        private int mAnimationCacheId;
        private int mClipChildrenId;
        private int mClipToPaddingId;
        private int mDescendantFocusabilityId;
        private int mLayoutAnimationId;
        private int mLayoutModeId;
        private int mPersistentDrawingCacheId;
        private boolean mPropertiesMapped = false;
        private int mSplitMotionEventsId;
        private int mTouchscreenBlocksFocusId;
        private int mTransitionGroupId;

        public void mapProperties(PropertyMapper propertyMapper) {
            this.mAddStatesFromChildrenId = propertyMapper.mapBoolean("addStatesFromChildren", 16842992);
            this.mAlwaysDrawnWithCacheId = propertyMapper.mapBoolean("alwaysDrawnWithCache", 16842991);
            this.mAnimationCacheId = propertyMapper.mapBoolean("animationCache", 16842989);
            this.mClipChildrenId = propertyMapper.mapBoolean("clipChildren", 16842986);
            this.mClipToPaddingId = propertyMapper.mapBoolean("clipToPadding", 16842987);
            SparseArray<String> descendantFocusabilityEnumMapping = new SparseArray();
            descendantFocusabilityEnumMapping.put(131072, "beforeDescendants");
            descendantFocusabilityEnumMapping.put(262144, "afterDescendants");
            descendantFocusabilityEnumMapping.put(393216, "blocksDescendants");
            Objects.requireNonNull(descendantFocusabilityEnumMapping);
            this.mDescendantFocusabilityId = propertyMapper.mapIntEnum("descendantFocusability", 16842993, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(descendantFocusabilityEnumMapping));
            this.mLayoutAnimationId = propertyMapper.mapObject("layoutAnimation", 16842988);
            SparseArray<String> layoutModeEnumMapping = new SparseArray();
            layoutModeEnumMapping.put(0, "clipBounds");
            layoutModeEnumMapping.put(1, "opticalBounds");
            Objects.requireNonNull(layoutModeEnumMapping);
            this.mLayoutModeId = propertyMapper.mapIntEnum("layoutMode", 16843738, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(layoutModeEnumMapping));
            SparseArray<String> persistentDrawingCacheEnumMapping = new SparseArray();
            persistentDrawingCacheEnumMapping.put(0, "none");
            persistentDrawingCacheEnumMapping.put(1, AnimationProperty.PROPERTY_NAME);
            persistentDrawingCacheEnumMapping.put(2, "scrolling");
            persistentDrawingCacheEnumMapping.put(3, "all");
            Objects.requireNonNull(persistentDrawingCacheEnumMapping);
            this.mPersistentDrawingCacheId = propertyMapper.mapIntEnum("persistentDrawingCache", 16842990, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(persistentDrawingCacheEnumMapping));
            this.mSplitMotionEventsId = propertyMapper.mapBoolean("splitMotionEvents", 16843503);
            this.mTouchscreenBlocksFocusId = propertyMapper.mapBoolean("touchscreenBlocksFocus", 16843919);
            this.mTransitionGroupId = propertyMapper.mapBoolean("transitionGroup", 16843777);
            this.mPropertiesMapped = true;
        }

        public void readProperties(ViewGroup node, PropertyReader propertyReader) {
            if (this.mPropertiesMapped) {
                propertyReader.readBoolean(this.mAddStatesFromChildrenId, node.addStatesFromChildren());
                propertyReader.readBoolean(this.mAlwaysDrawnWithCacheId, node.isAlwaysDrawnWithCacheEnabled());
                propertyReader.readBoolean(this.mAnimationCacheId, node.isAnimationCacheEnabled());
                propertyReader.readBoolean(this.mClipChildrenId, node.getClipChildren());
                propertyReader.readBoolean(this.mClipToPaddingId, node.getClipToPadding());
                propertyReader.readIntEnum(this.mDescendantFocusabilityId, node.getDescendantFocusability());
                propertyReader.readObject(this.mLayoutAnimationId, node.getLayoutAnimation());
                propertyReader.readIntEnum(this.mLayoutModeId, node.getLayoutMode());
                propertyReader.readIntEnum(this.mPersistentDrawingCacheId, node.getPersistentDrawingCache());
                propertyReader.readBoolean(this.mSplitMotionEventsId, node.isMotionEventSplittingEnabled());
                propertyReader.readBoolean(this.mTouchscreenBlocksFocusId, node.getTouchscreenBlocksFocus());
                propertyReader.readBoolean(this.mTransitionGroupId, node.isTransitionGroup());
                return;
            }
            throw new UninitializedPropertyMapException();
        }
    }

    public static class LayoutParams {
        @Deprecated
        public static final int FILL_PARENT = -1;
        public static final int MATCH_PARENT = -1;
        public static final int WRAP_CONTENT = -2;
        @ExportedProperty(category = "layout", mapping = {@IntToString(from = -1, to = "MATCH_PARENT"), @IntToString(from = -2, to = "WRAP_CONTENT")})
        public int height;
        public AnimationParameters layoutAnimationParameters;
        @ExportedProperty(category = "layout", mapping = {@IntToString(from = -1, to = "MATCH_PARENT"), @IntToString(from = -2, to = "WRAP_CONTENT")})
        public int width;

        public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<LayoutParams> {
            private int mLayout_heightId;
            private int mLayout_widthId;
            private boolean mPropertiesMapped = false;

            public void mapProperties(PropertyMapper propertyMapper) {
                SparseArray<String> layout_heightEnumMapping = new SparseArray();
                String str = "wrap_content";
                layout_heightEnumMapping.put(-2, str);
                String str2 = "match_parent";
                layout_heightEnumMapping.put(-1, str2);
                Objects.requireNonNull(layout_heightEnumMapping);
                this.mLayout_heightId = propertyMapper.mapIntEnum("layout_height", 16842997, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(layout_heightEnumMapping));
                SparseArray<String> layout_widthEnumMapping = new SparseArray();
                layout_widthEnumMapping.put(-2, str);
                layout_widthEnumMapping.put(-1, str2);
                Objects.requireNonNull(layout_widthEnumMapping);
                this.mLayout_widthId = propertyMapper.mapIntEnum("layout_width", 16842996, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(layout_widthEnumMapping));
                this.mPropertiesMapped = true;
            }

            public void readProperties(LayoutParams node, PropertyReader propertyReader) {
                if (this.mPropertiesMapped) {
                    propertyReader.readIntEnum(this.mLayout_heightId, node.height);
                    propertyReader.readIntEnum(this.mLayout_widthId, node.width);
                    return;
                }
                throw new UninitializedPropertyMapException();
            }
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.ViewGroup_Layout);
            setBaseAttributes(a, 0, 1);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public LayoutParams(LayoutParams source) {
            this.width = source.width;
            this.height = source.height;
        }

        @UnsupportedAppUsage
        LayoutParams() {
        }

        /* Access modifiers changed, original: protected */
        public void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
            this.width = a.getLayoutDimension(widthAttr, "layout_width");
            this.height = a.getLayoutDimension(heightAttr, "layout_height");
        }

        public void resolveLayoutDirection(int layoutDirection) {
        }

        public String debug(String output) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(output);
            stringBuilder.append("ViewGroup.LayoutParams={ width=");
            stringBuilder.append(sizeToString(this.width));
            stringBuilder.append(", height=");
            stringBuilder.append(sizeToString(this.height));
            stringBuilder.append(" }");
            return stringBuilder.toString();
        }

        public void onDebugDraw(View view, Canvas canvas, Paint paint) {
        }

        protected static String sizeToString(int size) {
            if (size == -2) {
                return "wrap-content";
            }
            if (size == -1) {
                return "match-parent";
            }
            return String.valueOf(size);
        }

        /* Access modifiers changed, original: 0000 */
        public void encode(ViewHierarchyEncoder encoder) {
            encoder.beginObject(this);
            encodeProperties(encoder);
            encoder.endObject();
        }

        /* Access modifiers changed, original: protected */
        public void encodeProperties(ViewHierarchyEncoder encoder) {
            encoder.addProperty("width", this.width);
            encoder.addProperty("height", this.height);
        }
    }

    public static class MarginLayoutParams extends LayoutParams {
        public static final int DEFAULT_MARGIN_RELATIVE = Integer.MIN_VALUE;
        private static final int DEFAULT_MARGIN_RESOLVED = 0;
        private static final int LAYOUT_DIRECTION_MASK = 3;
        private static final int LEFT_MARGIN_UNDEFINED_MASK = 4;
        private static final int NEED_RESOLUTION_MASK = 32;
        private static final int RIGHT_MARGIN_UNDEFINED_MASK = 8;
        private static final int RTL_COMPATIBILITY_MODE_MASK = 16;
        private static final int UNDEFINED_MARGIN = Integer.MIN_VALUE;
        @ExportedProperty(category = "layout")
        public int bottomMargin;
        @ExportedProperty(category = "layout")
        @UnsupportedAppUsage
        private int endMargin;
        @ExportedProperty(category = "layout")
        public int leftMargin;
        @ExportedProperty(category = "layout", flagMapping = {@FlagToString(equals = 3, mask = 3, name = "LAYOUT_DIRECTION"), @FlagToString(equals = 4, mask = 4, name = "LEFT_MARGIN_UNDEFINED_MASK"), @FlagToString(equals = 8, mask = 8, name = "RIGHT_MARGIN_UNDEFINED_MASK"), @FlagToString(equals = 16, mask = 16, name = "RTL_COMPATIBILITY_MODE_MASK"), @FlagToString(equals = 32, mask = 32, name = "NEED_RESOLUTION_MASK")}, formatToHexString = true)
        byte mMarginFlags;
        @ExportedProperty(category = "layout")
        public int rightMargin;
        @ExportedProperty(category = "layout")
        @UnsupportedAppUsage
        private int startMargin;
        @ExportedProperty(category = "layout")
        public int topMargin;

        public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<MarginLayoutParams> {
            private int mLayout_marginBottomId;
            private int mLayout_marginLeftId;
            private int mLayout_marginRightId;
            private int mLayout_marginTopId;
            private boolean mPropertiesMapped = false;

            public void mapProperties(PropertyMapper propertyMapper) {
                this.mLayout_marginBottomId = propertyMapper.mapInt("layout_marginBottom", 16843002);
                this.mLayout_marginLeftId = propertyMapper.mapInt("layout_marginLeft", 16842999);
                this.mLayout_marginRightId = propertyMapper.mapInt("layout_marginRight", 16843001);
                this.mLayout_marginTopId = propertyMapper.mapInt("layout_marginTop", 16843000);
                this.mPropertiesMapped = true;
            }

            public void readProperties(MarginLayoutParams node, PropertyReader propertyReader) {
                if (this.mPropertiesMapped) {
                    propertyReader.readInt(this.mLayout_marginBottomId, node.bottomMargin);
                    propertyReader.readInt(this.mLayout_marginLeftId, node.leftMargin);
                    propertyReader.readInt(this.mLayout_marginRightId, node.rightMargin);
                    propertyReader.readInt(this.mLayout_marginTopId, node.topMargin);
                    return;
                }
                throw new UninitializedPropertyMapException();
            }
        }

        public MarginLayoutParams(Context c, AttributeSet attrs) {
            this.startMargin = Integer.MIN_VALUE;
            this.endMargin = Integer.MIN_VALUE;
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.ViewGroup_MarginLayout);
            setBaseAttributes(a, 0, 1);
            int margin = a.getDimensionPixelSize(2, -1);
            if (margin >= 0) {
                this.leftMargin = margin;
                this.topMargin = margin;
                this.rightMargin = margin;
                this.bottomMargin = margin;
            } else {
                int horizontalMargin = a.getDimensionPixelSize(9, -1);
                int verticalMargin = a.getDimensionPixelSize(10, -1);
                if (horizontalMargin >= 0) {
                    this.leftMargin = horizontalMargin;
                    this.rightMargin = horizontalMargin;
                } else {
                    this.leftMargin = a.getDimensionPixelSize(3, Integer.MIN_VALUE);
                    if (this.leftMargin == Integer.MIN_VALUE) {
                        this.mMarginFlags = (byte) (this.mMarginFlags | 4);
                        this.leftMargin = 0;
                    }
                    this.rightMargin = a.getDimensionPixelSize(5, Integer.MIN_VALUE);
                    if (this.rightMargin == Integer.MIN_VALUE) {
                        this.mMarginFlags = (byte) (this.mMarginFlags | 8);
                        this.rightMargin = 0;
                    }
                }
                this.startMargin = a.getDimensionPixelSize(7, Integer.MIN_VALUE);
                this.endMargin = a.getDimensionPixelSize(8, Integer.MIN_VALUE);
                if (verticalMargin >= 0) {
                    this.topMargin = verticalMargin;
                    this.bottomMargin = verticalMargin;
                } else {
                    this.topMargin = a.getDimensionPixelSize(4, 0);
                    this.bottomMargin = a.getDimensionPixelSize(6, 0);
                }
                if (isMarginRelative()) {
                    this.mMarginFlags = (byte) (this.mMarginFlags | 32);
                }
            }
            boolean hasRtlSupport = c.getApplicationInfo().hasRtlSupport();
            if (c.getApplicationInfo().targetSdkVersion < 17 || !hasRtlSupport) {
                this.mMarginFlags = (byte) (this.mMarginFlags | 16);
            }
            this.mMarginFlags = (byte) (0 | this.mMarginFlags);
            a.recycle();
        }

        public MarginLayoutParams(int width, int height) {
            super(width, height);
            this.startMargin = Integer.MIN_VALUE;
            this.endMargin = Integer.MIN_VALUE;
            this.mMarginFlags = (byte) (this.mMarginFlags | 4);
            this.mMarginFlags = (byte) (this.mMarginFlags | 8);
            this.mMarginFlags = (byte) (this.mMarginFlags & -33);
            this.mMarginFlags = (byte) (this.mMarginFlags & -17);
        }

        public MarginLayoutParams(MarginLayoutParams source) {
            this.startMargin = Integer.MIN_VALUE;
            this.endMargin = Integer.MIN_VALUE;
            this.width = source.width;
            this.height = source.height;
            this.leftMargin = source.leftMargin;
            this.topMargin = source.topMargin;
            this.rightMargin = source.rightMargin;
            this.bottomMargin = source.bottomMargin;
            this.startMargin = source.startMargin;
            this.endMargin = source.endMargin;
            this.mMarginFlags = source.mMarginFlags;
        }

        public MarginLayoutParams(LayoutParams source) {
            super(source);
            this.startMargin = Integer.MIN_VALUE;
            this.endMargin = Integer.MIN_VALUE;
            this.mMarginFlags = (byte) (this.mMarginFlags | 4);
            this.mMarginFlags = (byte) (this.mMarginFlags | 8);
            this.mMarginFlags = (byte) (this.mMarginFlags & -33);
            this.mMarginFlags = (byte) (this.mMarginFlags & -17);
        }

        public final void copyMarginsFrom(MarginLayoutParams source) {
            this.leftMargin = source.leftMargin;
            this.topMargin = source.topMargin;
            this.rightMargin = source.rightMargin;
            this.bottomMargin = source.bottomMargin;
            this.startMargin = source.startMargin;
            this.endMargin = source.endMargin;
            this.mMarginFlags = source.mMarginFlags;
        }

        public void setMargins(int left, int top, int right, int bottom) {
            this.leftMargin = left;
            this.topMargin = top;
            this.rightMargin = right;
            this.bottomMargin = bottom;
            this.mMarginFlags = (byte) (this.mMarginFlags & -5);
            this.mMarginFlags = (byte) (this.mMarginFlags & -9);
            if (isMarginRelative()) {
                this.mMarginFlags = (byte) (this.mMarginFlags | 32);
            } else {
                this.mMarginFlags = (byte) (this.mMarginFlags & -33);
            }
        }

        @UnsupportedAppUsage
        public void setMarginsRelative(int start, int top, int end, int bottom) {
            this.startMargin = start;
            this.topMargin = top;
            this.endMargin = end;
            this.bottomMargin = bottom;
            this.mMarginFlags = (byte) (this.mMarginFlags | 32);
        }

        public void setMarginStart(int start) {
            this.startMargin = start;
            this.mMarginFlags = (byte) (this.mMarginFlags | 32);
        }

        public int getMarginStart() {
            int i = this.startMargin;
            if (i != Integer.MIN_VALUE) {
                return i;
            }
            if ((this.mMarginFlags & 32) == 32) {
                doResolveMargins();
            }
            if ((this.mMarginFlags & 3) != 1) {
                return this.leftMargin;
            }
            return this.rightMargin;
        }

        public void setMarginEnd(int end) {
            this.endMargin = end;
            this.mMarginFlags = (byte) (this.mMarginFlags | 32);
        }

        public int getMarginEnd() {
            int i = this.endMargin;
            if (i != Integer.MIN_VALUE) {
                return i;
            }
            if ((this.mMarginFlags & 32) == 32) {
                doResolveMargins();
            }
            if ((this.mMarginFlags & 3) != 1) {
                return this.rightMargin;
            }
            return this.leftMargin;
        }

        public boolean isMarginRelative() {
            return (this.startMargin == Integer.MIN_VALUE && this.endMargin == Integer.MIN_VALUE) ? false : true;
        }

        public void setLayoutDirection(int layoutDirection) {
            if (layoutDirection == 0 || layoutDirection == 1) {
                byte b = this.mMarginFlags;
                if (layoutDirection != (b & 3)) {
                    this.mMarginFlags = (byte) (b & -4);
                    this.mMarginFlags = (byte) (this.mMarginFlags | (layoutDirection & 3));
                    if (isMarginRelative()) {
                        this.mMarginFlags = (byte) (this.mMarginFlags | 32);
                    } else {
                        this.mMarginFlags = (byte) (this.mMarginFlags & -33);
                    }
                }
            }
        }

        public int getLayoutDirection() {
            return this.mMarginFlags & 3;
        }

        public void resolveLayoutDirection(int layoutDirection) {
            setLayoutDirection(layoutDirection);
            if (isMarginRelative() && (this.mMarginFlags & 32) == 32) {
                doResolveMargins();
            }
        }

        private void doResolveMargins() {
            byte b = this.mMarginFlags;
            int i;
            if ((b & 16) == 16) {
                if ((b & 4) == 4) {
                    i = this.startMargin;
                    if (i > Integer.MIN_VALUE) {
                        this.leftMargin = i;
                    }
                }
                if ((this.mMarginFlags & 8) == 8) {
                    i = this.endMargin;
                    if (i > Integer.MIN_VALUE) {
                        this.rightMargin = i;
                    }
                }
            } else if ((b & 3) != 1) {
                i = this.startMargin;
                if (i <= Integer.MIN_VALUE) {
                    i = 0;
                }
                this.leftMargin = i;
                i = this.endMargin;
                if (i <= Integer.MIN_VALUE) {
                    i = 0;
                }
                this.rightMargin = i;
            } else {
                i = this.endMargin;
                if (i <= Integer.MIN_VALUE) {
                    i = 0;
                }
                this.leftMargin = i;
                i = this.startMargin;
                if (i <= Integer.MIN_VALUE) {
                    i = 0;
                }
                this.rightMargin = i;
            }
            this.mMarginFlags = (byte) (this.mMarginFlags & -33);
        }

        public boolean isLayoutRtl() {
            return (this.mMarginFlags & 3) == 1;
        }

        public void onDebugDraw(View view, Canvas canvas, Paint paint) {
            Insets oi = View.isLayoutModeOptical(view.mParent) ? view.getOpticalInsets() : Insets.NONE;
            ViewGroup.fillDifference(canvas, view.getLeft() + oi.left, view.getTop() + oi.top, view.getRight() - oi.right, view.getBottom() - oi.bottom, this.leftMargin, this.topMargin, this.rightMargin, this.bottomMargin, paint);
        }

        /* Access modifiers changed, original: protected */
        public void encodeProperties(ViewHierarchyEncoder encoder) {
            super.encodeProperties(encoder);
            encoder.addProperty("leftMargin", this.leftMargin);
            encoder.addProperty("topMargin", this.topMargin);
            encoder.addProperty("rightMargin", this.rightMargin);
            encoder.addProperty("bottomMargin", this.bottomMargin);
            encoder.addProperty("startMargin", this.startMargin);
            encoder.addProperty("endMargin", this.endMargin);
        }
    }

    public interface OnHierarchyChangeListener {
        void onChildViewAdded(View view, View view2);

        void onChildViewRemoved(View view, View view2);
    }

    private static final class TouchTarget {
        public static final int ALL_POINTER_IDS = -1;
        private static final int MAX_RECYCLED = 32;
        private static TouchTarget sRecycleBin;
        private static final Object sRecycleLock = new Object[0];
        private static int sRecycledCount;
        @UnsupportedAppUsage
        public View child;
        public TouchTarget next;
        public int pointerIdBits;

        @UnsupportedAppUsage
        private TouchTarget() {
        }

        public static TouchTarget obtain(View child, int pointerIdBits) {
            if (child != null) {
                TouchTarget target;
                synchronized (sRecycleLock) {
                    if (sRecycleBin == null) {
                        target = new TouchTarget();
                    } else {
                        target = sRecycleBin;
                        sRecycleBin = target.next;
                        sRecycledCount--;
                        target.next = null;
                    }
                }
                target.child = child;
                target.pointerIdBits = pointerIdBits;
                return target;
            }
            throw new IllegalArgumentException("child must be non-null");
        }

        public void recycle() {
            if (this.child != null) {
                synchronized (sRecycleLock) {
                    if (sRecycledCount < 32) {
                        this.next = sRecycleBin;
                        sRecycleBin = this;
                        sRecycledCount++;
                    } else {
                        this.next = null;
                    }
                    this.child = null;
                }
                return;
            }
            throw new IllegalStateException("already recycled once");
        }
    }

    static class ViewLocationHolder implements Comparable<ViewLocationHolder> {
        public static final int COMPARISON_STRATEGY_LOCATION = 2;
        public static final int COMPARISON_STRATEGY_STRIPE = 1;
        private static final int MAX_POOL_SIZE = 32;
        private static int sComparisonStrategy = 1;
        private static final SynchronizedPool<ViewLocationHolder> sPool = new SynchronizedPool(32);
        private int mLayoutDirection;
        private final Rect mLocation = new Rect();
        private ViewGroup mRoot;
        public View mView;

        ViewLocationHolder() {
        }

        public static ViewLocationHolder obtain(ViewGroup root, View view) {
            ViewLocationHolder holder = (ViewLocationHolder) sPool.acquire();
            if (holder == null) {
                holder = new ViewLocationHolder();
            }
            holder.init(root, view);
            return holder;
        }

        public static void setComparisonStrategy(int strategy) {
            sComparisonStrategy = strategy;
        }

        public void recycle() {
            clear();
            sPool.release(this);
        }

        public int compareTo(ViewLocationHolder another) {
            if (another == null) {
                return 1;
            }
            int boundsResult = compareBoundsOfTree(this, another);
            if (boundsResult != 0) {
                return boundsResult;
            }
            return this.mView.getAccessibilityViewId() - another.mView.getAccessibilityViewId();
        }

        private static int compareBoundsOfTree(ViewLocationHolder holder1, ViewLocationHolder holder2) {
            int leftDifference;
            if (sComparisonStrategy == 1) {
                if (holder1.mLocation.bottom - holder2.mLocation.top <= 0) {
                    return -1;
                }
                if (holder1.mLocation.top - holder2.mLocation.bottom >= 0) {
                    return 1;
                }
            }
            if (holder1.mLayoutDirection == 0) {
                leftDifference = holder1.mLocation.left - holder2.mLocation.left;
                if (leftDifference != 0) {
                    return leftDifference;
                }
            }
            leftDifference = holder1.mLocation.right - holder2.mLocation.right;
            if (leftDifference != 0) {
                return -leftDifference;
            }
            leftDifference = holder1.mLocation.top - holder2.mLocation.top;
            if (leftDifference != 0) {
                return leftDifference;
            }
            int heightDiference = holder1.mLocation.height() - holder2.mLocation.height();
            if (heightDiference != 0) {
                return -heightDiference;
            }
            int widthDifference = holder1.mLocation.width() - holder2.mLocation.width();
            if (widthDifference != 0) {
                return -widthDifference;
            }
            Rect view1Bounds = new Rect();
            Rect view2Bounds = new Rect();
            Rect tempRect = new Rect();
            holder1.mView.getBoundsOnScreen(view1Bounds, true);
            holder2.mView.getBoundsOnScreen(view2Bounds, true);
            View child1 = holder1.mView.findViewByPredicateTraversal(new -$$Lambda$ViewGroup$ViewLocationHolder$QbO7cM0ULKe25a7bfXG3VH6DB0c(tempRect, view1Bounds), null);
            View child2 = holder2.mView.findViewByPredicateTraversal(new -$$Lambda$ViewGroup$ViewLocationHolder$AjKvqdj7SGGIzA5qrlZUuu71jl8(tempRect, view2Bounds), null);
            if (child1 != null && child2 != null) {
                return compareBoundsOfTree(obtain(holder1.mRoot, child1), obtain(holder1.mRoot, child2));
            }
            if (child1 != null) {
                return 1;
            }
            if (child2 != null) {
                return -1;
            }
            return 0;
        }

        private void init(ViewGroup root, View view) {
            Rect viewLocation = this.mLocation;
            view.getDrawingRect(viewLocation);
            root.offsetDescendantRectToMyCoords(view, viewLocation);
            this.mView = view;
            this.mRoot = root;
            this.mLayoutDirection = root.getLayoutDirection();
        }

        private void clear() {
            this.mView = null;
            this.mRoot = null;
            this.mLocation.set(0, 0, 0, 0);
            this.mRoot = null;
        }
    }

    public abstract void onLayout(boolean z, int i, int i2, int i3, int i4);

    static {
        Extension.get().bindOriginal(new Impl());
    }

    public ViewGroup(Context context) {
        this(context, null);
    }

    public ViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mLastTouchDownIndex = -1;
        this.mLayoutMode = -1;
        this.mSuppressLayout = false;
        this.mLayoutCalledWhileSuppressed = false;
        this.mChildCountWithTransientState = 0;
        this.mTransientIndices = null;
        this.mTransientViews = null;
        this.mChildUnhandledKeyListeners = 0;
        this.mLayoutTransitionListener = new TransitionListener() {
            public void startTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
                if (transitionType == 3) {
                    ViewGroup.this.startViewTransition(view);
                }
            }

            public void endTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
                if (ViewGroup.this.mLayoutCalledWhileSuppressed && !transition.isChangingLayout()) {
                    ViewGroup.this.requestLayout();
                    ViewGroup.this.mLayoutCalledWhileSuppressed = false;
                }
                if (transitionType == 3 && ViewGroup.this.mTransitioningViews != null) {
                    ViewGroup.this.endViewTransition(view);
                }
            }
        };
        initViewGroup();
        initFromAttributes(context, attrs, defStyleAttr, defStyleRes);
        if (Extension.get().getExtension() != null) {
            ((Interface) Extension.get().getExtension().asInterface()).init(this, context, attrs, defStyleAttr, defStyleRes);
        }
    }

    private void initViewGroup() {
        if (!debugDraw()) {
            setFlags(128, 128);
        }
        this.mGroupFlags |= 1;
        this.mGroupFlags |= 2;
        this.mGroupFlags |= 16;
        this.mGroupFlags |= 64;
        this.mGroupFlags |= 16384;
        if (this.mContext.getApplicationInfo().targetSdkVersion >= 11) {
            this.mGroupFlags |= 2097152;
        }
        setDescendantFocusability(131072);
        this.mChildren = new View[12];
        this.mChildrenCount = 0;
        this.mPersistentDrawingCache = 2;
    }

    private void initFromAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewGroup, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.ViewGroup, attrs, a, defStyleAttr, defStyleRes);
        int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case 0:
                    setClipChildren(a.getBoolean(attr, true));
                    break;
                case 1:
                    setClipToPadding(a.getBoolean(attr, true));
                    break;
                case 2:
                    int id = a.getResourceId(attr, -1);
                    if (id <= 0) {
                        break;
                    }
                    setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this.mContext, id));
                    break;
                case 3:
                    setAnimationCacheEnabled(a.getBoolean(attr, true));
                    break;
                case 4:
                    setPersistentDrawingCache(a.getInt(attr, 2));
                    break;
                case 5:
                    setAlwaysDrawnWithCacheEnabled(a.getBoolean(attr, true));
                    break;
                case 6:
                    setAddStatesFromChildren(a.getBoolean(attr, false));
                    break;
                case 7:
                    setDescendantFocusability(DESCENDANT_FOCUSABILITY_FLAGS[a.getInt(attr, 0)]);
                    break;
                case 8:
                    setMotionEventSplittingEnabled(a.getBoolean(attr, false));
                    break;
                case 9:
                    if (!a.getBoolean(attr, false)) {
                        break;
                    }
                    setLayoutTransition(new LayoutTransition());
                    break;
                case 10:
                    setLayoutMode(a.getInt(attr, -1));
                    break;
                case 11:
                    setTransitionGroup(a.getBoolean(attr, false));
                    break;
                case 12:
                    setTouchscreenBlocksFocus(a.getBoolean(attr, false));
                    break;
                default:
                    break;
            }
        }
        a.recycle();
    }

    @ExportedProperty(category = "focus", mapping = {@IntToString(from = 131072, to = "FOCUS_BEFORE_DESCENDANTS"), @IntToString(from = 262144, to = "FOCUS_AFTER_DESCENDANTS"), @IntToString(from = 393216, to = "FOCUS_BLOCK_DESCENDANTS")})
    public int getDescendantFocusability() {
        return this.mGroupFlags & 393216;
    }

    public void setDescendantFocusability(int focusability) {
        if (focusability == 131072 || focusability == 262144 || focusability == 393216) {
            this.mGroupFlags &= -393217;
            this.mGroupFlags |= 393216 & focusability;
            return;
        }
        throw new IllegalArgumentException("must be one of FOCUS_BEFORE_DESCENDANTS, FOCUS_AFTER_DESCENDANTS, FOCUS_BLOCK_DESCENDANTS");
    }

    /* Access modifiers changed, original: 0000 */
    public void handleFocusGainInternal(int direction, Rect previouslyFocusedRect) {
        View view = this.mFocused;
        if (view != null) {
            view.unFocus(this);
            this.mFocused = null;
            this.mFocusedInCluster = null;
        }
        super.handleFocusGainInternal(direction, previouslyFocusedRect);
    }

    public void requestChildFocus(View child, View focused) {
        if (getDescendantFocusability() != 393216) {
            super.unFocus(focused);
            View view = this.mFocused;
            if (view != child) {
                if (view != null) {
                    view.unFocus(focused);
                }
                this.mFocused = child;
            }
            if (this.mParent != null) {
                this.mParent.requestChildFocus(this, focused);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setDefaultFocus(View child) {
        View view = this.mDefaultFocus;
        if (view == null || !view.isFocusedByDefault()) {
            this.mDefaultFocus = child;
            if (this.mParent instanceof ViewGroup) {
                ((ViewGroup) this.mParent).setDefaultFocus(this);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void clearDefaultFocus(View child) {
        View view = this.mDefaultFocus;
        if (view == child || view == null || !view.isFocusedByDefault()) {
            this.mDefaultFocus = null;
            for (int i = 0; i < this.mChildrenCount; i++) {
                View sibling = this.mChildren[i];
                if (sibling.isFocusedByDefault()) {
                    this.mDefaultFocus = sibling;
                    return;
                }
                if (this.mDefaultFocus == null && sibling.hasDefaultFocus()) {
                    this.mDefaultFocus = sibling;
                }
            }
            if (this.mParent instanceof ViewGroup) {
                ((ViewGroup) this.mParent).clearDefaultFocus(this);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean hasDefaultFocus() {
        return this.mDefaultFocus != null || super.hasDefaultFocus();
    }

    /* Access modifiers changed, original: 0000 */
    public void clearFocusedInCluster(View child) {
        if (this.mFocusedInCluster == child) {
            clearFocusedInCluster();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void clearFocusedInCluster() {
        ViewParent top = findKeyboardNavigationCluster();
        ViewParent parent = this;
        do {
            ((ViewGroup) parent).mFocusedInCluster = null;
            if (parent != top) {
                parent = parent.getParent();
            } else {
                return;
            }
        } while (parent instanceof ViewGroup);
    }

    public void focusableViewAvailable(View v) {
        if (this.mParent != null && getDescendantFocusability() != 393216 && (this.mViewFlags & 12) == 0) {
            if (!isFocusableInTouchMode() && shouldBlockFocusForTouchscreen()) {
                return;
            }
            if (!isFocused() || getDescendantFocusability() == 262144) {
                this.mParent.focusableViewAvailable(v);
            }
        }
    }

    public boolean showContextMenuForChild(View originalView) {
        boolean z = false;
        if (isShowingContextMenuWithCoords()) {
            return false;
        }
        if (this.mParent != null && this.mParent.showContextMenuForChild(originalView)) {
            z = true;
        }
        return z;
    }

    public final boolean isShowingContextMenuWithCoords() {
        return (this.mGroupFlags & 536870912) != 0;
    }

    public boolean showContextMenuForChild(View originalView, float x, float y) {
        boolean z;
        try {
            this.mGroupFlags |= 536870912;
            z = true;
            if (showContextMenuForChild(originalView)) {
                return z;
            }
            this.mGroupFlags = -536870913 & this.mGroupFlags;
            if (this.mParent == null || !this.mParent.showContextMenuForChild(originalView, x, y)) {
                z = false;
            }
            return z;
        } finally {
            z = this.mGroupFlags;
            this.mGroupFlags = -536870913 & z;
        }
    }

    public ActionMode startActionModeForChild(View originalView, Callback callback) {
        int i = this.mGroupFlags;
        if ((134217728 & i) != 0) {
            return SENTINEL_ACTION_MODE;
        }
        try {
            this.mGroupFlags = i | 268435456;
            ActionMode startActionModeForChild = startActionModeForChild(originalView, callback, 0);
            return startActionModeForChild;
        } finally {
            this.mGroupFlags = -268435457 & this.mGroupFlags;
        }
    }

    public ActionMode startActionModeForChild(View originalView, Callback callback, int type) {
        int i = this.mGroupFlags;
        if ((268435456 & i) == 0 && type == 0) {
            try {
                this.mGroupFlags = i | 134217728;
                ActionMode mode = startActionModeForChild(originalView, callback);
                if (mode != SENTINEL_ACTION_MODE) {
                    return mode;
                }
            } finally {
                this.mGroupFlags = -134217729 & this.mGroupFlags;
            }
        }
        if (this.mParent == null) {
            return null;
        }
        try {
            return this.mParent.startActionModeForChild(originalView, callback, type);
        } catch (AbstractMethodError e) {
            return this.mParent.startActionModeForChild(originalView, callback);
        }
    }

    public boolean dispatchActivityResult(String who, int requestCode, int resultCode, Intent data) {
        if (super.dispatchActivityResult(who, requestCode, resultCode, data)) {
            return true;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (getChildAt(i).dispatchActivityResult(who, requestCode, resultCode, data)) {
                return true;
            }
        }
        return false;
    }

    public View focusSearch(View focused, int direction) {
        if (isRootNamespace()) {
            return FocusFinder.getInstance().findNextFocus(this, focused, direction);
        }
        if (this.mParent != null) {
            return this.mParent.focusSearch(focused, direction);
        }
        return null;
    }

    public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
        return false;
    }

    public boolean requestSendAccessibilityEvent(View child, AccessibilityEvent event) {
        ViewParent parent = this.mParent;
        if (parent != null && onRequestSendAccessibilityEvent(child, event)) {
            return parent.requestSendAccessibilityEvent(this, event);
        }
        return false;
    }

    public boolean onRequestSendAccessibilityEvent(View child, AccessibilityEvent event) {
        if (this.mAccessibilityDelegate != null) {
            return this.mAccessibilityDelegate.onRequestSendAccessibilityEvent(this, child, event);
        }
        return onRequestSendAccessibilityEventInternal(child, event);
    }

    public boolean onRequestSendAccessibilityEventInternal(View child, AccessibilityEvent event) {
        return true;
    }

    public void childHasTransientStateChanged(View child, boolean childHasTransientState) {
        boolean oldHasTransientState = hasTransientState();
        if (childHasTransientState) {
            this.mChildCountWithTransientState++;
        } else {
            this.mChildCountWithTransientState--;
        }
        boolean newHasTransientState = hasTransientState();
        if (this.mParent != null && oldHasTransientState != newHasTransientState) {
            try {
                this.mParent.childHasTransientStateChanged(this, newHasTransientState);
            } catch (AbstractMethodError e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.mParent.getClass().getSimpleName());
                stringBuilder.append(" does not fully implement ViewParent");
                Log.e(TAG, stringBuilder.toString(), e);
            }
        }
    }

    public boolean hasTransientState() {
        return this.mChildCountWithTransientState > 0 || super.hasTransientState();
    }

    public boolean dispatchUnhandledMove(View focused, int direction) {
        View view = this.mFocused;
        return view != null && view.dispatchUnhandledMove(focused, direction);
    }

    public void clearChildFocus(View child) {
        this.mFocused = null;
        if (this.mParent != null) {
            this.mParent.clearChildFocus(this);
        }
    }

    public void clearFocus() {
        if (this.mFocused == null) {
            super.clearFocus();
            return;
        }
        View focused = this.mFocused;
        this.mFocused = null;
        focused.clearFocus();
    }

    /* Access modifiers changed, original: 0000 */
    public void unFocus(View focused) {
        View view = this.mFocused;
        if (view == null) {
            super.unFocus(focused);
            return;
        }
        view.unFocus(focused);
        this.mFocused = null;
    }

    public View getFocusedChild() {
        return this.mFocused;
    }

    /* Access modifiers changed, original: 0000 */
    public View getDeepestFocusedChild() {
        View v = this;
        while (true) {
            View view = null;
            if (v == null) {
                return null;
            }
            if (v.isFocused()) {
                return v;
            }
            if (v instanceof ViewGroup) {
                view = ((ViewGroup) v).getFocusedChild();
            }
            v = view;
        }
    }

    public boolean hasFocus() {
        return ((this.mPrivateFlags & 2) == 0 && this.mFocused == null) ? false : true;
    }

    public View findFocus() {
        if (isFocused()) {
            return this;
        }
        View view = this.mFocused;
        if (view != null) {
            return view.findFocus();
        }
        return null;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean hasFocusable(boolean allowAutoFocus, boolean dispatchExplicit) {
        if ((this.mViewFlags & 12) != 0) {
            return false;
        }
        if ((allowAutoFocus || getFocusable() != 16) && isFocusable()) {
            return true;
        }
        if (getDescendantFocusability() != 393216) {
            return hasFocusableChild(dispatchExplicit);
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean hasFocusableChild(boolean dispatchExplicit) {
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            View child = children[i];
            if ((dispatchExplicit && child.hasExplicitFocusable()) || (!dispatchExplicit && child.hasFocusable())) {
                return true;
            }
        }
        return false;
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        int focusableCount = views.size();
        int descendantFocusability = getDescendantFocusability();
        boolean blockFocusForTouchscreen = shouldBlockFocusForTouchscreen();
        boolean focusSelf = isFocusableInTouchMode() || !blockFocusForTouchscreen;
        if (descendantFocusability == 393216) {
            if (focusSelf) {
                super.addFocusables(views, direction, focusableMode);
            }
            return;
        }
        if (blockFocusForTouchscreen) {
            focusableMode |= 1;
        }
        if (descendantFocusability == 131072 && focusSelf) {
            super.addFocusables(views, direction, focusableMode);
        }
        int count = 0;
        View[] children = new View[this.mChildrenCount];
        for (int i = 0; i < this.mChildrenCount; i++) {
            View child = this.mChildren[i];
            if ((child.mViewFlags & 12) == 0) {
                int count2 = count + 1;
                children[count] = child;
                count = count2;
            }
        }
        FocusFinder.sort(children, 0, count, this, isLayoutRtl());
        for (int i2 = 0; i2 < count; i2++) {
            children[i2].addFocusables(views, direction, focusableMode);
        }
        if (descendantFocusability == 262144 && focusSelf && focusableCount == views.size()) {
            super.addFocusables(views, direction, focusableMode);
        }
    }

    public void addKeyboardNavigationClusters(Collection<View> views, int direction) {
        int focusableCount = views.size();
        if (isKeyboardNavigationCluster()) {
            boolean blockedFocus = getTouchscreenBlocksFocus();
            try {
                setTouchscreenBlocksFocusNoRefocus(false);
                super.addKeyboardNavigationClusters(views, direction);
            } finally {
                setTouchscreenBlocksFocusNoRefocus(blockedFocus);
            }
        } else {
            super.addKeyboardNavigationClusters(views, direction);
        }
        if (focusableCount == views.size() && getDescendantFocusability() != 393216) {
            int count = 0;
            View[] visibleChildren = new View[this.mChildrenCount];
            for (int i = 0; i < this.mChildrenCount; i++) {
                View child = this.mChildren[i];
                if ((child.mViewFlags & 12) == 0) {
                    int count2 = count + 1;
                    visibleChildren[count] = child;
                    count = count2;
                }
            }
            FocusFinder.sort(visibleChildren, 0, count, this, isLayoutRtl());
            for (int i2 = 0; i2 < count; i2++) {
                visibleChildren[i2].addKeyboardNavigationClusters(views, direction);
            }
        }
    }

    public void setTouchscreenBlocksFocus(boolean touchscreenBlocksFocus) {
        if (touchscreenBlocksFocus) {
            this.mGroupFlags |= 67108864;
            if (hasFocus() && !isKeyboardNavigationCluster() && !getDeepestFocusedChild().isFocusableInTouchMode()) {
                View newFocus = focusSearch(2);
                if (newFocus != null) {
                    newFocus.requestFocus();
                    return;
                }
                return;
            }
            return;
        }
        this.mGroupFlags &= -67108865;
    }

    private void setTouchscreenBlocksFocusNoRefocus(boolean touchscreenBlocksFocus) {
        if (touchscreenBlocksFocus) {
            this.mGroupFlags |= 67108864;
        } else {
            this.mGroupFlags &= -67108865;
        }
    }

    @ExportedProperty(category = "focus")
    public boolean getTouchscreenBlocksFocus() {
        return (this.mGroupFlags & 67108864) != 0;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean shouldBlockFocusForTouchscreen() {
        return getTouchscreenBlocksFocus() && this.mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN) && (!isKeyboardNavigationCluster() || (!hasFocus() && findKeyboardNavigationCluster() == this));
    }

    public void findViewsWithText(ArrayList<View> outViews, CharSequence text, int flags) {
        super.findViewsWithText(outViews, text, flags);
        int childrenCount = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < childrenCount; i++) {
            View child = children[i];
            if ((child.mViewFlags & 12) == 0 && (child.mPrivateFlags & 8) == 0) {
                child.findViewsWithText(outViews, text, flags);
            }
        }
    }

    public View findViewByAccessibilityIdTraversal(int accessibilityId) {
        View foundView = super.findViewByAccessibilityIdTraversal(accessibilityId);
        if (foundView != null) {
            return foundView;
        }
        if (getAccessibilityNodeProvider() != null) {
            return null;
        }
        int childrenCount = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < childrenCount; i++) {
            foundView = children[i].findViewByAccessibilityIdTraversal(accessibilityId);
            if (foundView != null) {
                return foundView;
            }
        }
        return null;
    }

    public View findViewByAutofillIdTraversal(int autofillId) {
        View foundView = super.findViewByAutofillIdTraversal(autofillId);
        if (foundView != null) {
            return foundView;
        }
        int childrenCount = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < childrenCount; i++) {
            foundView = children[i].findViewByAutofillIdTraversal(autofillId);
            if (foundView != null) {
                return foundView;
            }
        }
        return null;
    }

    public void dispatchWindowFocusChanged(boolean hasFocus) {
        super.dispatchWindowFocusChanged(hasFocus);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchWindowFocusChanged(hasFocus);
        }
    }

    public void addTouchables(ArrayList<View> views) {
        super.addTouchables(views);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            View child = children[i];
            if ((child.mViewFlags & 12) == 0) {
                child.addTouchables(views);
            }
        }
    }

    @UnsupportedAppUsage
    public void makeOptionalFitsSystemWindows() {
        super.makeOptionalFitsSystemWindows();
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].makeOptionalFitsSystemWindows();
        }
    }

    public void dispatchDisplayHint(int hint) {
        super.dispatchDisplayHint(hint);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchDisplayHint(hint);
        }
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public void onChildVisibilityChanged(View child, int oldVisibility, int newVisibility) {
        if (Extension.get().getExtension() != null) {
            ((Interface) Extension.get().getExtension().asInterface()).onChildVisibilityChanged(this, child, oldVisibility, newVisibility);
        } else {
            originalOnChildVisibilityChanged(child, oldVisibility, newVisibility);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void originalOnChildVisibilityChanged(View child, int oldVisibility, int newVisibility) {
        LayoutTransition layoutTransition = this.mTransition;
        if (layoutTransition != null) {
            if (newVisibility == 0) {
                layoutTransition.showChild(this, child, oldVisibility);
            } else {
                layoutTransition.hideChild(this, child, newVisibility);
                ArrayList arrayList = this.mTransitioningViews;
                if (arrayList != null && arrayList.contains(child)) {
                    if (this.mVisibilityChangingChildren == null) {
                        this.mVisibilityChangingChildren = new ArrayList();
                    }
                    this.mVisibilityChangingChildren.add(child);
                    addDisappearingView(child);
                }
            }
        }
        if (newVisibility == 0 && this.mCurrentDragStartEvent != null && !this.mChildrenInterestedInDrag.contains(child)) {
            notifyChildOfDragStart(child);
        }
    }

    /* Access modifiers changed, original: protected */
    public void dispatchVisibilityChanged(View changedView, int visibility) {
        super.dispatchVisibilityChanged(changedView, visibility);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchVisibilityChanged(changedView, visibility);
        }
    }

    public void dispatchWindowVisibilityChanged(int visibility) {
        super.dispatchWindowVisibilityChanged(visibility);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchWindowVisibilityChanged(visibility);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean dispatchVisibilityAggregated(boolean isVisible) {
        isVisible = super.dispatchVisibilityAggregated(isVisible);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            if (children[i].getVisibility() == 0) {
                children[i].dispatchVisibilityAggregated(isVisible);
            }
        }
        return isVisible;
    }

    public void dispatchConfigurationChanged(Configuration newConfig) {
        super.dispatchConfigurationChanged(newConfig);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchConfigurationChanged(newConfig);
        }
    }

    public void recomputeViewAttributes(View child) {
        if (this.mAttachInfo != null && !this.mAttachInfo.mRecomputeGlobalAttributes) {
            ViewParent parent = this.mParent;
            if (parent != null) {
                parent.recomputeViewAttributes(this);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchCollectViewAttributes(AttachInfo attachInfo, int visibility) {
        if ((visibility & 12) == 0) {
            super.dispatchCollectViewAttributes(attachInfo, visibility);
            int count = this.mChildrenCount;
            View[] children = this.mChildren;
            for (int i = 0; i < count; i++) {
                View child = children[i];
                child.dispatchCollectViewAttributes(attachInfo, (child.mViewFlags & 12) | visibility);
            }
        }
    }

    public void bringChildToFront(View child) {
        int index = indexOfChild(child);
        if (index >= 0) {
            removeFromArray(index);
            addInArray(child, this.mChildrenCount);
            child.mParent = this;
            requestLayout();
            invalidate();
        }
    }

    private PointF getLocalPoint() {
        if (this.mLocalPoint == null) {
            this.mLocalPoint = new PointF();
        }
        return this.mLocalPoint;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean dispatchDragEnterExitInPreN(DragEvent event) {
        if (event.mAction == 6) {
            View view = this.mCurrentDragChild;
            if (view != null) {
                view.dispatchDragEnterExitInPreN(event);
                this.mCurrentDragChild = null;
            }
        }
        return this.mIsInterestedInDrag && super.dispatchDragEnterExitInPreN(event);
    }

    public boolean dispatchDragEvent(DragEvent event) {
        boolean retval = false;
        float tx = event.mX;
        float ty = event.mY;
        ClipData td = event.mClipData;
        PointF localPoint = getLocalPoint();
        int i = event.mAction;
        if (i == 1) {
            this.mCurrentDragChild = null;
            this.mCurrentDragStartEvent = DragEvent.obtain(event);
            HashSet hashSet = this.mChildrenInterestedInDrag;
            if (hashSet == null) {
                this.mChildrenInterestedInDrag = new HashSet();
            } else {
                hashSet.clear();
            }
            i = this.mChildrenCount;
            View[] children = this.mChildren;
            int i2 = 0;
            while (i2 < i) {
                View child = children[i2];
                child.mPrivateFlags2 &= -4;
                if (child.getVisibility() == 0 && notifyChildOfDragStart(children[i2])) {
                    retval = true;
                }
                i2++;
            }
            this.mIsInterestedInDrag = super.dispatchDragEvent(event);
            if (this.mIsInterestedInDrag) {
                retval = true;
            }
            if (retval) {
                return retval;
            }
            this.mCurrentDragStartEvent.recycle();
            this.mCurrentDragStartEvent = null;
            return retval;
        } else if (i == 2 || i == 3) {
            View target = findFrontmostDroppableChildAt(event.mX, event.mY, localPoint);
            if (target != this.mCurrentDragChild) {
                if (sCascadedDragDrop) {
                    int action = event.mAction;
                    event.mX = 0.0f;
                    event.mY = 0.0f;
                    event.mClipData = null;
                    View view = this.mCurrentDragChild;
                    if (view != null) {
                        event.mAction = 6;
                        view.dispatchDragEnterExitInPreN(event);
                    }
                    if (target != null) {
                        event.mAction = 5;
                        target.dispatchDragEnterExitInPreN(event);
                    }
                    event.mAction = action;
                    event.mX = tx;
                    event.mY = ty;
                    event.mClipData = td;
                }
                this.mCurrentDragChild = target;
            }
            if (target == null && this.mIsInterestedInDrag) {
                target = this;
            }
            if (target == null) {
                return false;
            }
            if (target == this) {
                return super.dispatchDragEvent(event);
            }
            event.mX = localPoint.x;
            event.mY = localPoint.y;
            retval = target.dispatchDragEvent(event);
            event.mX = tx;
            event.mY = ty;
            if (!this.mIsInterestedInDrag) {
                return retval;
            }
            boolean eventWasConsumed;
            if (sCascadedDragDrop) {
                eventWasConsumed = retval;
            } else {
                eventWasConsumed = event.mEventHandlerWasCalled;
            }
            if (eventWasConsumed) {
                return retval;
            }
            return super.dispatchDragEvent(event);
        } else if (i != 4) {
            return false;
        } else {
            HashSet<View> childrenInterestedInDrag = this.mChildrenInterestedInDrag;
            if (childrenInterestedInDrag != null) {
                Iterator it = childrenInterestedInDrag.iterator();
                while (it.hasNext()) {
                    if (((View) it.next()).dispatchDragEvent(event)) {
                        retval = true;
                    }
                }
                childrenInterestedInDrag.clear();
            }
            DragEvent dragEvent = this.mCurrentDragStartEvent;
            if (dragEvent != null) {
                dragEvent.recycle();
                this.mCurrentDragStartEvent = null;
            }
            if (!this.mIsInterestedInDrag) {
                return retval;
            }
            if (super.dispatchDragEvent(event)) {
                retval = true;
            }
            this.mIsInterestedInDrag = false;
            return retval;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public View findFrontmostDroppableChildAt(float x, float y, PointF outLocalPoint) {
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = count - 1; i >= 0; i--) {
            View child = children[i];
            if (child.canAcceptDrag() && isTransformedTouchPointInView(x, y, child, outLocalPoint)) {
                return child;
            }
        }
        return null;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean notifyChildOfDragStart(View child) {
        float tx = this.mCurrentDragStartEvent.mX;
        float ty = this.mCurrentDragStartEvent.mY;
        float[] point = getTempPoint();
        point[0] = tx;
        point[1] = ty;
        transformPointToViewLocal(point, child);
        boolean canAccept = this.mCurrentDragStartEvent;
        canAccept.mX = point[0];
        canAccept.mY = point[1];
        canAccept = child.dispatchDragEvent(canAccept);
        DragEvent dragEvent = this.mCurrentDragStartEvent;
        dragEvent.mX = tx;
        dragEvent.mY = ty;
        dragEvent.mEventHandlerWasCalled = false;
        if (canAccept) {
            this.mChildrenInterestedInDrag.add(child);
            if (!child.canAcceptDrag()) {
                child.mPrivateFlags2 |= 1;
                child.refreshDrawableState();
            }
        }
        return canAccept;
    }

    public void dispatchWindowSystemUiVisiblityChanged(int visible) {
        super.dispatchWindowSystemUiVisiblityChanged(visible);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchWindowSystemUiVisiblityChanged(visible);
        }
    }

    public void dispatchSystemUiVisibilityChanged(int visible) {
        super.dispatchSystemUiVisibilityChanged(visible);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchSystemUiVisibilityChanged(visible);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean updateLocalSystemUiVisibility(int localValue, int localChanges) {
        boolean changed = super.updateLocalSystemUiVisibility(localValue, localChanges);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            changed |= children[i].updateLocalSystemUiVisibility(localValue, localChanges);
        }
        return changed;
    }

    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if ((this.mPrivateFlags & 18) == 18) {
            return super.dispatchKeyEventPreIme(event);
        }
        View view = this.mFocused;
        if (view == null || (view.mPrivateFlags & 16) != 16) {
            return false;
        }
        return this.mFocused.dispatchKeyEventPreIme(event);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (this.mInputEventConsistencyVerifier != null) {
            this.mInputEventConsistencyVerifier.onKeyEvent(event, 1);
        }
        if ((this.mPrivateFlags & 18) != 18) {
            View view = this.mFocused;
            if (view != null && (view.mPrivateFlags & 16) == 16 && this.mFocused.dispatchKeyEvent(event)) {
                return true;
            }
        } else if (super.dispatchKeyEvent(event)) {
            return true;
        }
        if (this.mInputEventConsistencyVerifier != null) {
            this.mInputEventConsistencyVerifier.onUnhandledEvent(event, 1);
        }
        return false;
    }

    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        if ((this.mPrivateFlags & 18) == 18) {
            return super.dispatchKeyShortcutEvent(event);
        }
        View view = this.mFocused;
        if (view == null || (view.mPrivateFlags & 16) != 16) {
            return false;
        }
        return this.mFocused.dispatchKeyShortcutEvent(event);
    }

    public boolean dispatchTrackballEvent(MotionEvent event) {
        if (this.mInputEventConsistencyVerifier != null) {
            this.mInputEventConsistencyVerifier.onTrackballEvent(event, 1);
        }
        if ((this.mPrivateFlags & 18) != 18) {
            View view = this.mFocused;
            if (view != null && (view.mPrivateFlags & 16) == 16 && this.mFocused.dispatchTrackballEvent(event)) {
                return true;
            }
        } else if (super.dispatchTrackballEvent(event)) {
            return true;
        }
        if (this.mInputEventConsistencyVerifier != null) {
            this.mInputEventConsistencyVerifier.onUnhandledEvent(event, 1);
        }
        return false;
    }

    public boolean dispatchCapturedPointerEvent(MotionEvent event) {
        if ((this.mPrivateFlags & 18) != 18) {
            View view = this.mFocused;
            if (view != null && (view.mPrivateFlags & 16) == 16 && this.mFocused.dispatchCapturedPointerEvent(event)) {
                return true;
            }
        } else if (super.dispatchCapturedPointerEvent(event)) {
            return true;
        }
        return false;
    }

    public void dispatchPointerCaptureChanged(boolean hasCapture) {
        exitHoverTargets();
        super.dispatchPointerCaptureChanged(hasCapture);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchPointerCaptureChanged(hasCapture);
        }
    }

    public PointerIcon onResolvePointerIcon(MotionEvent event, int pointerIndex) {
        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);
        if (isOnScrollbarThumb(x, y) || isDraggingScrollBar()) {
            return PointerIcon.getSystemIcon(this.mContext, 1000);
        }
        int childrenCount = this.mChildrenCount;
        if (childrenCount != 0) {
            ArrayList<View> preorderedList = buildOrderedChildList();
            boolean customOrder = preorderedList == null && isChildrenDrawingOrderEnabled();
            View[] children = this.mChildren;
            for (int i = childrenCount - 1; i >= 0; i--) {
                View child = getAndVerifyPreorderedView(preorderedList, children, getAndVerifyPreorderedIndex(childrenCount, i, customOrder));
                if (child.canReceivePointerEvents() && isTransformedTouchPointInView(x, y, child, null)) {
                    PointerIcon pointerIcon = dispatchResolvePointerIcon(event, pointerIndex, child);
                    if (pointerIcon != null) {
                        if (preorderedList != null) {
                            preorderedList.clear();
                        }
                        return pointerIcon;
                    }
                }
            }
            if (preorderedList != null) {
                preorderedList.clear();
            }
        }
        return super.onResolvePointerIcon(event, pointerIndex);
    }

    private PointerIcon dispatchResolvePointerIcon(MotionEvent event, int pointerIndex, View child) {
        if (child.hasIdentityMatrix()) {
            float offsetX = (float) (this.mScrollX - child.mLeft);
            float offsetY = (float) (this.mScrollY - child.mTop);
            event.offsetLocation(offsetX, offsetY);
            PointerIcon pointerIcon = child.onResolvePointerIcon(event, pointerIndex);
            event.offsetLocation(-offsetX, -offsetY);
            return pointerIcon;
        }
        MotionEvent transformedEvent = getTransformedMotionEvent(event, child);
        PointerIcon pointerIcon2 = child.onResolvePointerIcon(transformedEvent, pointerIndex);
        transformedEvent.recycle();
        return pointerIcon2;
    }

    private int getAndVerifyPreorderedIndex(int childrenCount, int i, boolean customOrder) {
        if (!customOrder) {
            return i;
        }
        int childIndex = getChildDrawingOrder(childrenCount, i);
        if (childIndex < childrenCount) {
            return childIndex;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getChildDrawingOrder() returned invalid index ");
        stringBuilder.append(childIndex);
        stringBuilder.append(" (child count is ");
        stringBuilder.append(childrenCount);
        stringBuilder.append(")");
        throw new IndexOutOfBoundsException(stringBuilder.toString());
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x009e  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x009b  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00b3  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00a5  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00e7  */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x00e2 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x011a  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x016c  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0163  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x01c1  */
    public boolean dispatchHoverEvent(android.view.MotionEvent r26) {
        /*
        r25 = this;
        r0 = r25;
        r1 = r26;
        r2 = r26.getAction();
        r3 = r25.onInterceptHoverEvent(r26);
        r1.setAction(r2);
        r4 = r26;
        r5 = 0;
        r6 = r0.mFirstHoverTarget;
        r7 = 0;
        r0.mFirstHoverTarget = r7;
        r12 = 10;
        if (r3 != 0) goto L_0x0114;
    L_0x001b:
        if (r2 == r12) goto L_0x0114;
    L_0x001d:
        r13 = r26.getX();
        r14 = r26.getY();
        r15 = r0.mChildrenCount;
        if (r15 == 0) goto L_0x0111;
    L_0x0029:
        r8 = r25.buildOrderedChildList();
        if (r8 != 0) goto L_0x0038;
    L_0x002f:
        r16 = r25.isChildrenDrawingOrderEnabled();
        if (r16 == 0) goto L_0x0038;
    L_0x0035:
        r16 = 1;
        goto L_0x003a;
    L_0x0038:
        r16 = 0;
    L_0x003a:
        r17 = r16;
        r11 = r0.mChildren;
        r18 = 0;
        r19 = r15 + -1;
        r23 = r19;
        r19 = r4;
        r4 = r23;
        r24 = r18;
        r18 = r5;
        r5 = r24;
    L_0x004e:
        if (r4 < 0) goto L_0x0103;
    L_0x0050:
        r12 = r17;
        r10 = r0.getAndVerifyPreorderedIndex(r15, r4, r12);
        r9 = getAndVerifyPreorderedView(r8, r11, r10);
        r20 = r9.canReceivePointerEvents();
        if (r20 == 0) goto L_0x00f2;
    L_0x0060:
        r20 = r0.isTransformedTouchPointInView(r13, r14, r9, r7);
        if (r20 != 0) goto L_0x006c;
    L_0x0066:
        r20 = r3;
        r21 = r6;
        goto L_0x00f6;
    L_0x006c:
        r20 = r6;
        r21 = 0;
        r7 = r20;
        r20 = r3;
        r3 = r21;
    L_0x0076:
        if (r7 != 0) goto L_0x0080;
    L_0x0078:
        r7 = android.view.ViewGroup.HoverTarget.obtain(r9);
        r21 = 0;
        r3 = 0;
        goto L_0x0099;
    L_0x0080:
        r21 = r6;
        r6 = r7.child;
        if (r6 != r9) goto L_0x00ea;
    L_0x0086:
        if (r3 == 0) goto L_0x008f;
    L_0x0088:
        r6 = r7.next;
        r3.next = r6;
        r6 = r21;
        goto L_0x0091;
    L_0x008f:
        r6 = r7.next;
    L_0x0091:
        r22 = r3;
        r3 = 0;
        r7.next = r3;
        r21 = 1;
    L_0x0099:
        if (r5 == 0) goto L_0x009e;
    L_0x009b:
        r5.next = r7;
        goto L_0x00a0;
    L_0x009e:
        r0.mFirstHoverTarget = r7;
    L_0x00a0:
        r5 = r7;
        r3 = 9;
        if (r2 != r3) goto L_0x00b3;
    L_0x00a5:
        if (r21 != 0) goto L_0x00b0;
    L_0x00a7:
        r3 = r0.dispatchTransformedGenericPointerEvent(r1, r9);
        r18 = r18 | r3;
        r22 = r5;
        goto L_0x00e0;
    L_0x00b0:
        r22 = r5;
        goto L_0x00e0;
    L_0x00b3:
        r3 = 7;
        if (r2 != r3) goto L_0x00de;
    L_0x00b6:
        if (r21 != 0) goto L_0x00d5;
    L_0x00b8:
        r3 = obtainMotionEventNoHistoryOrSelf(r19);
        r22 = r5;
        r5 = 9;
        r3.setAction(r5);
        r5 = r0.dispatchTransformedGenericPointerEvent(r3, r9);
        r5 = r18 | r5;
        r3.setAction(r2);
        r18 = r0.dispatchTransformedGenericPointerEvent(r3, r9);
        r18 = r5 | r18;
        r19 = r3;
        goto L_0x00e0;
    L_0x00d5:
        r22 = r5;
        r3 = r0.dispatchTransformedGenericPointerEvent(r1, r9);
        r18 = r18 | r3;
        goto L_0x00e0;
    L_0x00de:
        r22 = r5;
    L_0x00e0:
        if (r18 == 0) goto L_0x00e7;
    L_0x00e2:
        r4 = r19;
        r5 = r22;
        goto L_0x010b;
    L_0x00e7:
        r5 = r22;
        goto L_0x00f8;
    L_0x00ea:
        r22 = r3;
        r3 = r7;
        r7 = r7.next;
        r6 = r21;
        goto L_0x0076;
    L_0x00f2:
        r20 = r3;
        r21 = r6;
    L_0x00f6:
        r6 = r21;
    L_0x00f8:
        r4 = r4 + -1;
        r17 = r12;
        r3 = r20;
        r7 = 0;
        r12 = 10;
        goto L_0x004e;
    L_0x0103:
        r20 = r3;
        r21 = r6;
        r12 = r17;
        r4 = r19;
    L_0x010b:
        if (r8 == 0) goto L_0x0118;
    L_0x010d:
        r8.clear();
        goto L_0x0118;
    L_0x0111:
        r20 = r3;
        goto L_0x0116;
    L_0x0114:
        r20 = r3;
    L_0x0116:
        r18 = r5;
    L_0x0118:
        if (r6 == 0) goto L_0x0150;
    L_0x011a:
        r3 = r6.child;
        r5 = 10;
        if (r2 != r5) goto L_0x0129;
    L_0x0120:
        r5 = r0.dispatchTransformedGenericPointerEvent(r1, r3);
        r5 = r18 | r5;
        r18 = r5;
        goto L_0x0149;
    L_0x0129:
        r5 = 7;
        if (r2 != r5) goto L_0x013a;
    L_0x012c:
        r5 = r26.isHoverExitPending();
        r7 = 1;
        r1.setHoverExitPending(r7);
        r0.dispatchTransformedGenericPointerEvent(r1, r3);
        r1.setHoverExitPending(r5);
    L_0x013a:
        r4 = obtainMotionEventNoHistoryOrSelf(r4);
        r5 = 10;
        r4.setAction(r5);
        r0.dispatchTransformedGenericPointerEvent(r4, r3);
        r4.setAction(r2);
    L_0x0149:
        r5 = r6.next;
        r6.recycle();
        r6 = r5;
        goto L_0x0118;
    L_0x0150:
        if (r18 != 0) goto L_0x015e;
    L_0x0152:
        r3 = 10;
        if (r2 == r3) goto L_0x015e;
    L_0x0156:
        r3 = r26.isHoverExitPending();
        if (r3 != 0) goto L_0x015e;
    L_0x015c:
        r3 = 1;
        goto L_0x015f;
    L_0x015e:
        r3 = 0;
    L_0x015f:
        r5 = r0.mHoveredSelf;
        if (r3 != r5) goto L_0x016c;
    L_0x0163:
        if (r3 == 0) goto L_0x01bf;
    L_0x0165:
        r5 = super.dispatchHoverEvent(r26);
        r18 = r18 | r5;
        goto L_0x01bf;
    L_0x016c:
        if (r5 == 0) goto L_0x0191;
    L_0x016e:
        r5 = 10;
        if (r2 != r5) goto L_0x0179;
    L_0x0172:
        r5 = super.dispatchHoverEvent(r26);
        r18 = r18 | r5;
        goto L_0x018e;
    L_0x0179:
        r5 = 7;
        if (r2 != r5) goto L_0x017f;
    L_0x017c:
        super.dispatchHoverEvent(r26);
    L_0x017f:
        r4 = obtainMotionEventNoHistoryOrSelf(r4);
        r5 = 10;
        r4.setAction(r5);
        super.dispatchHoverEvent(r4);
        r4.setAction(r2);
    L_0x018e:
        r5 = 0;
        r0.mHoveredSelf = r5;
    L_0x0191:
        if (r3 == 0) goto L_0x01bf;
    L_0x0193:
        r5 = 9;
        if (r2 != r5) goto L_0x01a1;
    L_0x0197:
        r5 = super.dispatchHoverEvent(r26);
        r18 = r18 | r5;
        r5 = 1;
        r0.mHoveredSelf = r5;
        goto L_0x01bf;
    L_0x01a1:
        r5 = 7;
        if (r2 != r5) goto L_0x01bf;
    L_0x01a4:
        r4 = obtainMotionEventNoHistoryOrSelf(r4);
        r5 = 9;
        r4.setAction(r5);
        r5 = super.dispatchHoverEvent(r4);
        r5 = r18 | r5;
        r4.setAction(r2);
        r7 = super.dispatchHoverEvent(r4);
        r18 = r5 | r7;
        r5 = 1;
        r0.mHoveredSelf = r5;
    L_0x01bf:
        if (r4 == r1) goto L_0x01c4;
    L_0x01c1:
        r4.recycle();
    L_0x01c4:
        return r18;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.ViewGroup.dispatchHoverEvent(android.view.MotionEvent):boolean");
    }

    private void exitHoverTargets() {
        if (this.mHoveredSelf || this.mFirstHoverTarget != null) {
            long now = SystemClock.uptimeMillis();
            MotionEvent event = MotionEvent.obtain(now, now, 10, 0.0f, 0.0f, 0);
            event.setSource(4098);
            dispatchHoverEvent(event);
            event.recycle();
        }
    }

    private void cancelHoverTarget(View view) {
        HoverTarget predecessor = null;
        HoverTarget target = this.mFirstHoverTarget;
        while (target != null) {
            HoverTarget next = target.next;
            if (target.child == view) {
                if (predecessor == null) {
                    this.mFirstHoverTarget = next;
                } else {
                    predecessor.next = next;
                }
                target.recycle();
                long now = SystemClock.uptimeMillis();
                MotionEvent event = MotionEvent.obtain(now, now, 10, 0.0f, 0.0f, 0);
                event.setSource(4098);
                view.dispatchHoverEvent(event);
                event.recycle();
                return;
            }
            predecessor = target;
            target = next;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean dispatchTooltipHoverEvent(MotionEvent event) {
        MotionEvent motionEvent = event;
        int action = event.getAction();
        View view;
        if (action != 7) {
            if (action != 9 && action == 10) {
                view = this.mTooltipHoverTarget;
                if (view != null) {
                    view.dispatchTooltipHoverEvent(motionEvent);
                    this.mTooltipHoverTarget = null;
                } else if (this.mTooltipHoveredSelf) {
                    super.dispatchTooltipHoverEvent(event);
                    this.mTooltipHoveredSelf = false;
                }
            }
            return false;
        }
        view = null;
        int childrenCount = this.mChildrenCount;
        if (childrenCount != 0) {
            float x = event.getX();
            float y = event.getY();
            ArrayList<View> preorderedList = buildOrderedChildList();
            boolean customOrder = preorderedList == null && isChildrenDrawingOrderEnabled();
            View[] children = this.mChildren;
            for (int i = childrenCount - 1; i >= 0; i--) {
                View child = getAndVerifyPreorderedView(preorderedList, children, getAndVerifyPreorderedIndex(childrenCount, i, customOrder));
                if (child.canReceivePointerEvents() && isTransformedTouchPointInView(x, y, child, null) && dispatchTooltipHoverEvent(motionEvent, child)) {
                    view = child;
                    break;
                }
            }
            if (preorderedList != null) {
                preorderedList.clear();
            }
        }
        View view2 = this.mTooltipHoverTarget;
        if (view2 != view) {
            if (view2 != null) {
                motionEvent.setAction(10);
                this.mTooltipHoverTarget.dispatchTooltipHoverEvent(motionEvent);
                motionEvent.setAction(action);
            }
            this.mTooltipHoverTarget = view;
        }
        if (this.mTooltipHoverTarget != null) {
            if (this.mTooltipHoveredSelf) {
                this.mTooltipHoveredSelf = false;
                motionEvent.setAction(10);
                super.dispatchTooltipHoverEvent(event);
                motionEvent.setAction(action);
            }
            return true;
        }
        this.mTooltipHoveredSelf = super.dispatchTooltipHoverEvent(event);
        return this.mTooltipHoveredSelf;
    }

    private boolean dispatchTooltipHoverEvent(MotionEvent event, View child) {
        if (child.hasIdentityMatrix()) {
            float offsetX = (float) (this.mScrollX - child.mLeft);
            float offsetY = (float) (this.mScrollY - child.mTop);
            event.offsetLocation(offsetX, offsetY);
            boolean result = child.dispatchTooltipHoverEvent(event);
            event.offsetLocation(-offsetX, -offsetY);
            return result;
        }
        MotionEvent transformedEvent = getTransformedMotionEvent(event, child);
        boolean result2 = child.dispatchTooltipHoverEvent(transformedEvent);
        transformedEvent.recycle();
        return result2;
    }

    private void exitTooltipHoverTargets() {
        if (this.mTooltipHoveredSelf || this.mTooltipHoverTarget != null) {
            long now = SystemClock.uptimeMillis();
            MotionEvent event = MotionEvent.obtain(now, now, 10, 0.0f, 0.0f, 0);
            event.setSource(4098);
            dispatchTooltipHoverEvent(event);
            event.recycle();
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean hasHoveredChild() {
        return this.mFirstHoverTarget != null;
    }

    /* Access modifiers changed, original: protected */
    public boolean pointInHoveredChild(MotionEvent event) {
        if (this.mFirstHoverTarget != null) {
            return isTransformedTouchPointInView(event.getX(), event.getY(), this.mFirstHoverTarget.child, null);
        }
        return false;
    }

    public void addChildrenForAccessibility(ArrayList<View> outChildren) {
        if (getAccessibilityNodeProvider() == null) {
            ChildListForAccessibility children = ChildListForAccessibility.obtain(this, true);
            try {
                int childrenCount = children.getChildCount();
                for (int i = 0; i < childrenCount; i++) {
                    View child = children.getChildAt(i);
                    if ((child.mViewFlags & 12) == 0) {
                        if (child.includeForAccessibility()) {
                            outChildren.add(child);
                        } else {
                            child.addChildrenForAccessibility(outChildren);
                        }
                    }
                }
            } finally {
                children.recycle();
            }
        }
    }

    public boolean onInterceptHoverEvent(MotionEvent event) {
        if (event.isFromSource(8194)) {
            int action = event.getAction();
            float x = event.getX();
            float y = event.getY();
            if ((action == 7 || action == 9) && isOnScrollbar(x, y)) {
                return true;
            }
        }
        return false;
    }

    private static MotionEvent obtainMotionEventNoHistoryOrSelf(MotionEvent event) {
        if (event.getHistorySize() == 0) {
            return event;
        }
        return MotionEvent.obtainNoHistory(event);
    }

    /* Access modifiers changed, original: protected */
    public boolean dispatchGenericPointerEvent(MotionEvent event) {
        int childrenCount = this.mChildrenCount;
        if (childrenCount != 0) {
            float x = event.getX();
            float y = event.getY();
            ArrayList<View> preorderedList = buildOrderedChildList();
            boolean customOrder = preorderedList == null && isChildrenDrawingOrderEnabled();
            View[] children = this.mChildren;
            for (int i = childrenCount - 1; i >= 0; i--) {
                View child = getAndVerifyPreorderedView(preorderedList, children, getAndVerifyPreorderedIndex(childrenCount, i, customOrder));
                if (child.canReceivePointerEvents() && isTransformedTouchPointInView(x, y, child, null) && dispatchTransformedGenericPointerEvent(event, child)) {
                    if (preorderedList != null) {
                        preorderedList.clear();
                    }
                    return true;
                }
            }
            if (preorderedList != null) {
                preorderedList.clear();
            }
        }
        return super.dispatchGenericPointerEvent(event);
    }

    /* Access modifiers changed, original: protected */
    public boolean dispatchGenericFocusedEvent(MotionEvent event) {
        if ((this.mPrivateFlags & 18) == 18) {
            return super.dispatchGenericFocusedEvent(event);
        }
        View view = this.mFocused;
        if (view == null || (view.mPrivateFlags & 16) != 16) {
            return false;
        }
        return this.mFocused.dispatchGenericMotionEvent(event);
    }

    private boolean dispatchTransformedGenericPointerEvent(MotionEvent event, View child) {
        if (child.hasIdentityMatrix()) {
            float offsetX = (float) (this.mScrollX - child.mLeft);
            float offsetY = (float) (this.mScrollY - child.mTop);
            event.offsetLocation(offsetX, offsetY);
            boolean handled = child.dispatchGenericMotionEvent(event);
            event.offsetLocation(-offsetX, -offsetY);
            return handled;
        }
        MotionEvent transformedEvent = getTransformedMotionEvent(event, child);
        boolean handled2 = child.dispatchGenericMotionEvent(transformedEvent);
        transformedEvent.recycle();
        return handled2;
    }

    private MotionEvent getTransformedMotionEvent(MotionEvent event, View child) {
        float offsetX = (float) (this.mScrollX - child.mLeft);
        float offsetY = (float) (this.mScrollY - child.mTop);
        MotionEvent transformedEvent = MotionEvent.obtain(event);
        transformedEvent.offsetLocation(offsetX, offsetY);
        if (!child.hasIdentityMatrix()) {
            transformedEvent.transform(child.getInverseMatrix());
        }
        return transformedEvent;
    }

    /* JADX WARNING: Removed duplicated region for block: B:114:0x01f4  */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x01ed  */
    public boolean dispatchTouchEvent(android.view.MotionEvent r30) {
        /*
        r29 = this;
        r0 = r29;
        r1 = r30;
        r2 = r0.mInputEventConsistencyVerifier;
        r3 = 1;
        if (r2 == 0) goto L_0x000e;
    L_0x0009:
        r2 = r0.mInputEventConsistencyVerifier;
        r2.onTouchEvent(r1, r3);
    L_0x000e:
        r2 = r30.isTargetAccessibilityFocus();
        r4 = 0;
        if (r2 == 0) goto L_0x001e;
    L_0x0015:
        r2 = r29.isAccessibilityFocusedViewOrHost();
        if (r2 == 0) goto L_0x001e;
    L_0x001b:
        r1.setTargetAccessibilityFocus(r4);
    L_0x001e:
        r2 = 0;
        r5 = r29.onFilterTouchEventForSecurity(r30);
        if (r5 == 0) goto L_0x0252;
    L_0x0025:
        r5 = r30.getAction();
        r6 = r5 & 255;
        if (r6 != 0) goto L_0x0033;
    L_0x002d:
        r29.cancelAndClearTouchTargets(r30);
        r29.resetTouchState();
    L_0x0033:
        if (r6 == 0) goto L_0x003c;
    L_0x0035:
        r7 = r0.mFirstTouchTarget;
        if (r7 == 0) goto L_0x003a;
    L_0x0039:
        goto L_0x003c;
    L_0x003a:
        r7 = 1;
        goto L_0x0053;
    L_0x003c:
        r7 = r0.mGroupFlags;
        r8 = 524288; // 0x80000 float:7.34684E-40 double:2.590327E-318;
        r7 = r7 & r8;
        if (r7 == 0) goto L_0x0045;
    L_0x0043:
        r7 = r3;
        goto L_0x0046;
    L_0x0045:
        r7 = r4;
    L_0x0046:
        if (r7 != 0) goto L_0x0051;
    L_0x0048:
        r8 = r29.onInterceptTouchEvent(r30);
        r1.setAction(r5);
        r7 = r8;
        goto L_0x0052;
    L_0x0051:
        r7 = 0;
    L_0x0053:
        if (r7 != 0) goto L_0x0059;
    L_0x0055:
        r8 = r0.mFirstTouchTarget;
        if (r8 == 0) goto L_0x005c;
    L_0x0059:
        r1.setTargetAccessibilityFocus(r4);
    L_0x005c:
        r8 = resetCancelNextUpFlag(r29);
        if (r8 != 0) goto L_0x0068;
    L_0x0062:
        r8 = 3;
        if (r6 != r8) goto L_0x0066;
    L_0x0065:
        goto L_0x0068;
    L_0x0066:
        r8 = r4;
        goto L_0x0069;
    L_0x0068:
        r8 = r3;
    L_0x0069:
        r9 = r0.mGroupFlags;
        r10 = 2097152; // 0x200000 float:2.938736E-39 double:1.0361308E-317;
        r9 = r9 & r10;
        if (r9 == 0) goto L_0x0072;
    L_0x0070:
        r9 = r3;
        goto L_0x0073;
    L_0x0072:
        r9 = r4;
    L_0x0073:
        r10 = 0;
        r11 = 0;
        r12 = 7;
        if (r8 != 0) goto L_0x01e0;
    L_0x0078:
        if (r7 != 0) goto L_0x01e0;
    L_0x007a:
        r15 = r30.isTargetAccessibilityFocus();
        if (r15 == 0) goto L_0x0085;
    L_0x0080:
        r15 = r29.findChildWithAccessibilityFocus();
        goto L_0x0086;
    L_0x0085:
        r15 = 0;
    L_0x0086:
        if (r6 == 0) goto L_0x0099;
    L_0x0088:
        if (r9 == 0) goto L_0x008d;
    L_0x008a:
        r13 = 5;
        if (r6 == r13) goto L_0x0099;
    L_0x008d:
        if (r6 != r12) goto L_0x0090;
    L_0x008f:
        goto L_0x0099;
    L_0x0090:
        r18 = r2;
        r3 = r4;
        r19 = r5;
        r22 = r11;
        goto L_0x01e7;
    L_0x0099:
        r13 = r30.getActionIndex();
        if (r9 == 0) goto L_0x00a6;
    L_0x009f:
        r16 = r1.getPointerId(r13);
        r16 = r3 << r16;
        goto L_0x00a8;
    L_0x00a6:
        r16 = -1;
    L_0x00a8:
        r17 = r16;
        r12 = r17;
        r0.removePointersFromTouchTargets(r12);
        r3 = r0.mChildrenCount;
        if (r10 != 0) goto L_0x01bd;
    L_0x00b3:
        if (r3 == 0) goto L_0x01bd;
    L_0x00b5:
        r4 = r1.getX(r13);
        r14 = r1.getY(r13);
        r18 = r2;
        r2 = r29.buildTouchDispatchChildList();
        if (r2 != 0) goto L_0x00ce;
    L_0x00c5:
        r19 = r29.isChildrenDrawingOrderEnabled();
        if (r19 == 0) goto L_0x00ce;
    L_0x00cb:
        r19 = 1;
        goto L_0x00d0;
    L_0x00ce:
        r19 = 0;
    L_0x00d0:
        r20 = r19;
        r19 = r5;
        r5 = r0.mChildren;
        r21 = r3 + -1;
        r28 = r21;
        r21 = r10;
        r10 = r28;
    L_0x00de:
        if (r10 < 0) goto L_0x01a5;
    L_0x00e0:
        r22 = r11;
        r11 = r20;
        r20 = r13;
        r13 = r0.getAndVerifyPreorderedIndex(r3, r10, r11);
        r23 = r10;
        r10 = getAndVerifyPreorderedView(r2, r5, r13);
        if (r15 == 0) goto L_0x0101;
    L_0x00f2:
        if (r15 == r10) goto L_0x00fe;
    L_0x00f4:
        r27 = r3;
        r25 = r4;
        r24 = r11;
        r4 = r14;
        r3 = 0;
        goto L_0x0194;
    L_0x00fe:
        r15 = 0;
        r23 = r3 + -1;
    L_0x0101:
        r24 = r10.canReceivePointerEvents();
        if (r24 == 0) goto L_0x0185;
    L_0x0107:
        r24 = r11;
        r11 = 0;
        r25 = r0.isTransformedTouchPointInView(r4, r14, r10, r11);
        if (r25 != 0) goto L_0x011a;
    L_0x0110:
        r27 = r3;
        r25 = r4;
        r4 = r14;
        r26 = r15;
        r3 = 0;
        goto L_0x018f;
    L_0x011a:
        r11 = r0.getTouchTarget(r10);
        if (r11 == 0) goto L_0x012f;
    L_0x0120:
        r25 = r4;
        r4 = r11.pointerIdBits;
        r4 = r4 | r12;
        r11.pointerIdBits = r4;
        r27 = r3;
        r21 = r11;
        r4 = r14;
        r3 = 0;
        goto L_0x01b3;
    L_0x012f:
        r25 = r4;
        resetCancelNextUpFlag(r10);
        r4 = 0;
        r21 = r0.dispatchTransformedTouchEvent(r1, r4, r10, r12);
        if (r21 == 0) goto L_0x0179;
    L_0x013b:
        r4 = r14;
        r26 = r15;
        r14 = r30.getDownTime();
        r0.mLastTouchDownTime = r14;
        if (r2 == 0) goto L_0x015e;
    L_0x0146:
        r14 = 0;
    L_0x0147:
        if (r14 >= r3) goto L_0x015b;
    L_0x0149:
        r15 = r5[r13];
        r27 = r3;
        r3 = r0.mChildren;
        r3 = r3[r14];
        if (r15 != r3) goto L_0x0156;
    L_0x0153:
        r0.mLastTouchDownIndex = r14;
        goto L_0x015d;
    L_0x0156:
        r14 = r14 + 1;
        r3 = r27;
        goto L_0x0147;
    L_0x015b:
        r27 = r3;
    L_0x015d:
        goto L_0x0162;
    L_0x015e:
        r27 = r3;
        r0.mLastTouchDownIndex = r13;
    L_0x0162:
        r3 = r30.getX();
        r0.mLastTouchDownX = r3;
        r3 = r30.getY();
        r0.mLastTouchDownY = r3;
        r21 = r0.addTouchTarget(r10, r12);
        r11 = 1;
        r22 = r11;
        r15 = r26;
        r3 = 0;
        goto L_0x01b3;
    L_0x0179:
        r27 = r3;
        r4 = r14;
        r26 = r15;
        r3 = 0;
        r1.setTargetAccessibilityFocus(r3);
        r21 = r11;
        goto L_0x0194;
    L_0x0185:
        r27 = r3;
        r25 = r4;
        r24 = r11;
        r4 = r14;
        r26 = r15;
        r3 = 0;
    L_0x018f:
        r1.setTargetAccessibilityFocus(r3);
        r15 = r26;
    L_0x0194:
        r10 = -1;
        r11 = r23 + -1;
        r14 = r4;
        r10 = r11;
        r13 = r20;
        r11 = r22;
        r20 = r24;
        r4 = r25;
        r3 = r27;
        goto L_0x00de;
    L_0x01a5:
        r27 = r3;
        r25 = r4;
        r23 = r10;
        r22 = r11;
        r4 = r14;
        r24 = r20;
        r3 = 0;
        r20 = r13;
    L_0x01b3:
        if (r2 == 0) goto L_0x01b8;
    L_0x01b5:
        r2.clear();
    L_0x01b8:
        r10 = r21;
        r11 = r22;
        goto L_0x01ca;
    L_0x01bd:
        r18 = r2;
        r27 = r3;
        r3 = r4;
        r19 = r5;
        r22 = r11;
        r20 = r13;
        r11 = r22;
    L_0x01ca:
        if (r10 != 0) goto L_0x01e9;
    L_0x01cc:
        r2 = r0.mFirstTouchTarget;
        if (r2 == 0) goto L_0x01e9;
    L_0x01d0:
        r2 = r0.mFirstTouchTarget;
        r10 = r2;
    L_0x01d3:
        r2 = r10.next;
        if (r2 == 0) goto L_0x01da;
    L_0x01d7:
        r10 = r10.next;
        goto L_0x01d3;
    L_0x01da:
        r2 = r10.pointerIdBits;
        r2 = r2 | r12;
        r10.pointerIdBits = r2;
        goto L_0x01e9;
    L_0x01e0:
        r18 = r2;
        r3 = r4;
        r19 = r5;
        r22 = r11;
    L_0x01e7:
        r11 = r22;
    L_0x01e9:
        r2 = r0.mFirstTouchTarget;
        if (r2 != 0) goto L_0x01f4;
    L_0x01ed:
        r2 = -1;
        r3 = 0;
        r2 = r0.dispatchTransformedTouchEvent(r1, r8, r3, r2);
        goto L_0x0231;
    L_0x01f4:
        r2 = 0;
        r4 = r0.mFirstTouchTarget;
    L_0x01f7:
        if (r4 == 0) goto L_0x022f;
    L_0x01f9:
        r5 = r4.next;
        if (r11 == 0) goto L_0x0203;
    L_0x01fd:
        if (r4 != r10) goto L_0x0203;
    L_0x01ff:
        r12 = 1;
        r18 = r12;
        goto L_0x022c;
    L_0x0203:
        r12 = r4.child;
        r12 = resetCancelNextUpFlag(r12);
        if (r12 != 0) goto L_0x0210;
    L_0x020b:
        if (r7 == 0) goto L_0x020e;
    L_0x020d:
        goto L_0x0210;
    L_0x020e:
        r12 = r3;
        goto L_0x0211;
    L_0x0210:
        r12 = 1;
    L_0x0211:
        r13 = r4.child;
        r14 = r4.pointerIdBits;
        r13 = r0.dispatchTransformedTouchEvent(r1, r12, r13, r14);
        if (r13 == 0) goto L_0x021e;
    L_0x021b:
        r13 = 1;
        r18 = r13;
    L_0x021e:
        if (r12 == 0) goto L_0x022c;
    L_0x0220:
        if (r2 != 0) goto L_0x0225;
    L_0x0222:
        r0.mFirstTouchTarget = r5;
        goto L_0x0227;
    L_0x0225:
        r2.next = r5;
    L_0x0227:
        r4.recycle();
        r4 = r5;
        goto L_0x01f7;
    L_0x022c:
        r2 = r4;
        r4 = r5;
        goto L_0x01f7;
    L_0x022f:
        r2 = r18;
    L_0x0231:
        if (r8 != 0) goto L_0x024e;
    L_0x0233:
        r3 = 1;
        if (r6 == r3) goto L_0x024e;
    L_0x0236:
        r3 = 7;
        if (r6 != r3) goto L_0x023a;
    L_0x0239:
        goto L_0x024e;
    L_0x023a:
        if (r9 == 0) goto L_0x0254;
    L_0x023c:
        r3 = 6;
        if (r6 != r3) goto L_0x0254;
    L_0x023f:
        r3 = r30.getActionIndex();
        r4 = r1.getPointerId(r3);
        r5 = 1;
        r4 = r5 << r4;
        r0.removePointersFromTouchTargets(r4);
        goto L_0x0254;
    L_0x024e:
        r29.resetTouchState();
        goto L_0x0254;
    L_0x0252:
        r18 = r2;
    L_0x0254:
        if (r2 != 0) goto L_0x0260;
    L_0x0256:
        r3 = r0.mInputEventConsistencyVerifier;
        if (r3 == 0) goto L_0x0260;
    L_0x025a:
        r3 = r0.mInputEventConsistencyVerifier;
        r4 = 1;
        r3.onUnhandledEvent(r1, r4);
    L_0x0260:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.ViewGroup.dispatchTouchEvent(android.view.MotionEvent):boolean");
    }

    public ArrayList<View> buildTouchDispatchChildList() {
        return buildOrderedChildList();
    }

    private View findChildWithAccessibilityFocus() {
        ViewRootImpl viewRoot = getViewRootImpl();
        if (viewRoot == null) {
            return null;
        }
        View current = viewRoot.getAccessibilityFocusedHost();
        if (current == null) {
            return null;
        }
        View parent = current.getParent();
        while (parent instanceof View) {
            if (parent == this) {
                return current;
            }
            current = parent;
            parent = current.getParent();
        }
        return null;
    }

    private void resetTouchState() {
        clearTouchTargets();
        resetCancelNextUpFlag(this);
        this.mGroupFlags &= -524289;
        this.mNestedScrollAxes = 0;
    }

    private static boolean resetCancelNextUpFlag(View view) {
        if (view == null || (view.mPrivateFlags & 67108864) == 0) {
            return false;
        }
        view.mPrivateFlags &= -67108865;
        return true;
    }

    private void clearTouchTargets() {
        TouchTarget target = this.mFirstTouchTarget;
        if (target != null) {
            do {
                TouchTarget next = target.next;
                target.recycle();
                target = next;
            } while (target != null);
            this.mFirstTouchTarget = null;
        }
    }

    private void cancelAndClearTouchTargets(MotionEvent event) {
        if (this.mFirstTouchTarget != null) {
            boolean syntheticEvent = false;
            if (event == null) {
                long now = SystemClock.uptimeMillis();
                event = MotionEvent.obtain(now, now, 3, 0.0f, 0.0f, 0);
                event.setSource(4098);
                syntheticEvent = true;
            }
            for (TouchTarget target = this.mFirstTouchTarget; target != null; target = target.next) {
                resetCancelNextUpFlag(target.child);
                dispatchTransformedTouchEvent(event, true, target.child, target.pointerIdBits);
            }
            clearTouchTargets();
            if (syntheticEvent) {
                event.recycle();
            }
        }
    }

    private TouchTarget getTouchTarget(View child) {
        for (TouchTarget target = this.mFirstTouchTarget; target != null; target = target.next) {
            if (target.child == child) {
                return target;
            }
        }
        return null;
    }

    private TouchTarget addTouchTarget(View child, int pointerIdBits) {
        TouchTarget target = TouchTarget.obtain(child, pointerIdBits);
        target.next = this.mFirstTouchTarget;
        this.mFirstTouchTarget = target;
        return target;
    }

    private void removePointersFromTouchTargets(int pointerIdBits) {
        TouchTarget predecessor = null;
        TouchTarget target = this.mFirstTouchTarget;
        while (target != null) {
            TouchTarget next = target.next;
            if ((target.pointerIdBits & pointerIdBits) != 0) {
                target.pointerIdBits &= ~pointerIdBits;
                if (target.pointerIdBits == 0) {
                    if (predecessor == null) {
                        this.mFirstTouchTarget = next;
                    } else {
                        predecessor.next = next;
                    }
                    target.recycle();
                    target = next;
                }
            }
            predecessor = target;
            target = next;
        }
    }

    @UnsupportedAppUsage
    private void cancelTouchTarget(View view) {
        TouchTarget predecessor = null;
        TouchTarget target = this.mFirstTouchTarget;
        while (target != null) {
            TouchTarget next = target.next;
            if (target.child == view) {
                if (predecessor == null) {
                    this.mFirstTouchTarget = next;
                } else {
                    predecessor.next = next;
                }
                target.recycle();
                long now = SystemClock.uptimeMillis();
                MotionEvent event = MotionEvent.obtain(now, now, 3, 0.0f, 0.0f, 0);
                event.setSource(4098);
                view.dispatchTouchEvent(event);
                event.recycle();
                return;
            }
            predecessor = target;
            target = next;
        }
    }

    private float[] getTempPoint() {
        if (this.mTempPoint == null) {
            this.mTempPoint = new float[2];
        }
        return this.mTempPoint;
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public boolean isTransformedTouchPointInView(float x, float y, View child, PointF outLocalPoint) {
        float[] point = getTempPoint();
        point[0] = x;
        point[1] = y;
        transformPointToViewLocal(point, child);
        boolean isInView = child.pointInView(point[0], point[1]);
        if (isInView && outLocalPoint != null) {
            outLocalPoint.set(point[0], point[1]);
        }
        return isInView;
    }

    @UnsupportedAppUsage
    public void transformPointToViewLocal(float[] point, View child) {
        point[0] = point[0] + ((float) (this.mScrollX - child.mLeft));
        point[1] = point[1] + ((float) (this.mScrollY - child.mTop));
        if (!child.hasIdentityMatrix()) {
            child.getInverseMatrix().mapPoints(point);
        }
    }

    private boolean dispatchTransformedTouchEvent(MotionEvent event, boolean cancel, View child, int desiredPointerIdBits) {
        int oldAction = event.getAction();
        if (cancel || oldAction == 3) {
            boolean handled;
            event.setAction(3);
            if (child == null) {
                handled = super.dispatchTouchEvent(event);
            } else {
                handled = child.dispatchTouchEvent(event);
            }
            event.setAction(oldAction);
            return handled;
        }
        int oldPointerIdBits = event.getPointerIdBits();
        int newPointerIdBits = oldPointerIdBits & desiredPointerIdBits;
        if (newPointerIdBits == 0) {
            return false;
        }
        MotionEvent transformedEvent;
        boolean handled2;
        if (newPointerIdBits != oldPointerIdBits) {
            transformedEvent = event.split(newPointerIdBits);
        } else if (child == null || child.hasIdentityMatrix()) {
            boolean handled3;
            if (child == null) {
                handled3 = super.dispatchTouchEvent(event);
            } else {
                float offsetX = (float) (this.mScrollX - child.mLeft);
                float offsetY = (float) (this.mScrollY - child.mTop);
                event.offsetLocation(offsetX, offsetY);
                boolean handled4 = child.dispatchTouchEvent(event);
                event.offsetLocation(-offsetX, -offsetY);
                handled3 = handled4;
            }
            return handled3;
        } else {
            transformedEvent = MotionEvent.obtain(event);
        }
        if (child == null) {
            handled2 = super.dispatchTouchEvent(transformedEvent);
        } else {
            transformedEvent.offsetLocation((float) (this.mScrollX - child.mLeft), (float) (this.mScrollY - child.mTop));
            if (!child.hasIdentityMatrix()) {
                transformedEvent.transform(child.getInverseMatrix());
            }
            handled2 = child.dispatchTouchEvent(transformedEvent);
        }
        transformedEvent.recycle();
        return handled2;
    }

    public void setMotionEventSplittingEnabled(boolean split) {
        if (split) {
            this.mGroupFlags |= 2097152;
        } else {
            this.mGroupFlags &= -2097153;
        }
    }

    public boolean isMotionEventSplittingEnabled() {
        return (this.mGroupFlags & 2097152) == 2097152;
    }

    public boolean isTransitionGroup() {
        int i = this.mGroupFlags;
        boolean z = false;
        if ((33554432 & i) != 0) {
            if ((i & 16777216) != 0) {
                z = true;
            }
            return z;
        }
        ViewOutlineProvider outlineProvider = getOutlineProvider();
        if (!(getBackground() == null && getTransitionName() == null && (outlineProvider == null || outlineProvider == ViewOutlineProvider.BACKGROUND))) {
            z = true;
        }
        return z;
    }

    public void setTransitionGroup(boolean isTransitionGroup) {
        this.mGroupFlags |= 33554432;
        if (isTransitionGroup) {
            this.mGroupFlags |= 16777216;
        } else {
            this.mGroupFlags &= -16777217;
        }
    }

    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (disallowIntercept != ((this.mGroupFlags & 524288) != 0)) {
            if (disallowIntercept) {
                this.mGroupFlags |= 524288;
            } else {
                this.mGroupFlags &= -524289;
            }
            if (this.mParent != null) {
                this.mParent.requestDisallowInterceptTouchEvent(disallowIntercept);
            }
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.isFromSource(8194) && ev.getAction() == 0 && ev.isButtonPressed(1) && isOnScrollbarThumb(ev.getX(), ev.getY())) {
            return true;
        }
        return false;
    }

    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        boolean result;
        int descendantFocusability = getDescendantFocusability();
        boolean took;
        if (descendantFocusability == 131072) {
            took = super.requestFocus(direction, previouslyFocusedRect);
            result = took ? took : onRequestFocusInDescendants(direction, previouslyFocusedRect);
        } else if (descendantFocusability == 262144) {
            took = onRequestFocusInDescendants(direction, previouslyFocusedRect);
            result = took ? took : super.requestFocus(direction, previouslyFocusedRect);
        } else if (descendantFocusability == 393216) {
            result = super.requestFocus(direction, previouslyFocusedRect);
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("descendant focusability must be one of FOCUS_BEFORE_DESCENDANTS, FOCUS_AFTER_DESCENDANTS, FOCUS_BLOCK_DESCENDANTS but is ");
            stringBuilder.append(descendantFocusability);
            throw new IllegalStateException(stringBuilder.toString());
        }
        if (result && !isLayoutValid() && (this.mPrivateFlags & 1) == 0) {
            this.mPrivateFlags |= 1;
        }
        return result;
    }

    /* Access modifiers changed, original: protected */
    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        int index;
        int increment;
        int end;
        int count = this.mChildrenCount;
        if ((direction & 2) != 0) {
            index = 0;
            increment = 1;
            end = count;
        } else {
            index = count - 1;
            increment = -1;
            end = -1;
        }
        View[] children = this.mChildren;
        for (int i = index; i != end; i += increment) {
            View child = children[i];
            if ((child.mViewFlags & 12) == 0 && child.requestFocus(direction, previouslyFocusedRect)) {
                return true;
            }
        }
        return false;
    }

    public boolean restoreDefaultFocus() {
        if (this.mDefaultFocus == null || getDescendantFocusability() == 393216 || (this.mDefaultFocus.mViewFlags & 12) != 0 || !this.mDefaultFocus.restoreDefaultFocus()) {
            return super.restoreDefaultFocus();
        }
        return true;
    }

    public boolean restoreFocusInCluster(int direction) {
        if (!isKeyboardNavigationCluster()) {
            return restoreFocusInClusterInternal(direction);
        }
        boolean blockedFocus = getTouchscreenBlocksFocus();
        try {
            setTouchscreenBlocksFocusNoRefocus(false);
            boolean restoreFocusInClusterInternal = restoreFocusInClusterInternal(direction);
            return restoreFocusInClusterInternal;
        } finally {
            setTouchscreenBlocksFocusNoRefocus(blockedFocus);
        }
    }

    private boolean restoreFocusInClusterInternal(int direction) {
        if (this.mFocusedInCluster == null || getDescendantFocusability() == 393216 || (this.mFocusedInCluster.mViewFlags & 12) != 0 || !this.mFocusedInCluster.restoreFocusInCluster(direction)) {
            return super.restoreFocusInCluster(direction);
        }
        return true;
    }

    public boolean restoreFocusNotInCluster() {
        if (this.mFocusedInCluster != null) {
            return restoreFocusInCluster(130);
        }
        if (isKeyboardNavigationCluster() || (this.mViewFlags & 12) != 0) {
            return false;
        }
        int descendentFocusability = getDescendantFocusability();
        if (descendentFocusability == 393216) {
            return super.requestFocus(130, null);
        }
        if (descendentFocusability == 131072 && super.requestFocus(130, null)) {
            return true;
        }
        for (int i = 0; i < this.mChildrenCount; i++) {
            View child = this.mChildren[i];
            if (!child.isKeyboardNavigationCluster() && child.restoreFocusNotInCluster()) {
                return true;
            }
        }
        if (descendentFocusability != 262144 || hasFocusableChild(false)) {
            return false;
        }
        return super.requestFocus(130, null);
    }

    public void dispatchStartTemporaryDetach() {
        super.dispatchStartTemporaryDetach();
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchStartTemporaryDetach();
        }
    }

    public void dispatchFinishTemporaryDetach() {
        super.dispatchFinishTemporaryDetach();
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchFinishTemporaryDetach();
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dispatchAttachedToWindow(AttachInfo info, int visibility) {
        int i;
        this.mGroupFlags |= 4194304;
        super.dispatchAttachedToWindow(info, visibility);
        this.mGroupFlags &= -4194305;
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (i = 0; i < count; i++) {
            View child = children[i];
            child.dispatchAttachedToWindow(info, combineVisibility(visibility, child.getVisibility()));
        }
        i = this.mTransientIndices;
        i = i == 0 ? 0 : i.size();
        for (int i2 = 0; i2 < i; i2++) {
            View view = (View) this.mTransientViews.get(i2);
            view.dispatchAttachedToWindow(info, combineVisibility(visibility, view.getVisibility()));
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchScreenStateChanged(int screenState) {
        super.dispatchScreenStateChanged(screenState);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchScreenStateChanged(screenState);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchMovedToDisplay(Display display, Configuration config) {
        super.dispatchMovedToDisplay(display, config);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchMovedToDisplay(display, config);
        }
    }

    public boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        boolean handled = false;
        if (includeForAccessibility()) {
            handled = super.dispatchPopulateAccessibilityEventInternal(event);
            if (handled) {
                return handled;
            }
        }
        ChildListForAccessibility children = ChildListForAccessibility.obtain(this, true);
        try {
            int childCount = children.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = children.getChildAt(i);
                if ((child.mViewFlags & 12) == 0) {
                    handled = child.dispatchPopulateAccessibilityEvent(event);
                    if (handled) {
                        return handled;
                    }
                }
            }
            children.recycle();
            return false;
        } finally {
            children.recycle();
        }
    }

    public void dispatchProvideStructure(ViewStructure structure) {
        super.dispatchProvideStructure(structure);
        if (!isAssistBlocked() && structure.getChildCount() == 0) {
            int childrenCount = this.mChildrenCount;
            if (childrenCount > 0) {
                if (isLaidOut()) {
                    structure.setChildCount(childrenCount);
                    ArrayList<View> preorderedList = buildOrderedChildList();
                    boolean customOrder = preorderedList == null && isChildrenDrawingOrderEnabled();
                    for (int i = 0; i < childrenCount; i++) {
                        int childIndex;
                        try {
                            childIndex = getAndVerifyPreorderedIndex(childrenCount, i, customOrder);
                        } catch (IndexOutOfBoundsException e) {
                            childIndex = i;
                            if (this.mContext.getApplicationInfo().targetSdkVersion < 23) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("Bad getChildDrawingOrder while collecting assist @ ");
                                stringBuilder.append(i);
                                stringBuilder.append(" of ");
                                stringBuilder.append(childrenCount);
                                Log.w(TAG, stringBuilder.toString(), e);
                                customOrder = false;
                                if (i > 0) {
                                    int j;
                                    int j2;
                                    int[] permutation = new int[childrenCount];
                                    SparseBooleanArray usedIndices = new SparseBooleanArray();
                                    for (j = 0; j < i; j++) {
                                        permutation[j] = getChildDrawingOrder(childrenCount, j);
                                        usedIndices.put(permutation[j], true);
                                    }
                                    j = 0;
                                    for (j2 = i; j2 < childrenCount; j2++) {
                                        while (usedIndices.get(j, false)) {
                                            j++;
                                        }
                                        permutation[j2] = j;
                                        j++;
                                    }
                                    preorderedList = new ArrayList(childrenCount);
                                    for (j2 = 0; j2 < childrenCount; j2++) {
                                        preorderedList.add(this.mChildren[permutation[j2]]);
                                    }
                                }
                            } else {
                                throw e;
                            }
                        }
                        getAndVerifyPreorderedView(preorderedList, this.mChildren, childIndex).dispatchProvideStructure(structure.newChild(i));
                    }
                    if (preorderedList != null) {
                        preorderedList.clear();
                    }
                    return;
                }
                if (Helper.sVerbose) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("dispatchProvideStructure(): not laid out, ignoring ");
                    stringBuilder2.append(childrenCount);
                    stringBuilder2.append(" children of ");
                    stringBuilder2.append(getAccessibilityViewId());
                    Log.v("View", stringBuilder2.toString());
                }
            }
        }
    }

    public void dispatchProvideAutofillStructure(ViewStructure structure, int flags) {
        super.dispatchProvideAutofillStructure(structure, flags);
        if (structure.getChildCount() == 0) {
            if (isLaidOut()) {
                ChildListForAutofill children = getChildrenForAutofill(flags);
                int childrenCount = children.size();
                structure.setChildCount(childrenCount);
                for (int i = 0; i < childrenCount; i++) {
                    ((View) children.get(i)).dispatchProvideAutofillStructure(structure.newChild(i), flags);
                }
                children.recycle();
                return;
            }
            if (Helper.sVerbose) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("dispatchProvideAutofillStructure(): not laid out, ignoring ");
                stringBuilder.append(this.mChildrenCount);
                stringBuilder.append(" children of ");
                stringBuilder.append(getAutofillId());
                Log.v("View", stringBuilder.toString());
            }
        }
    }

    private ChildListForAutofill getChildrenForAutofill(int flags) {
        ChildListForAutofill children = ChildListForAutofill.obtain();
        populateChildrenForAutofill(children, flags);
        return children;
    }

    private void populateChildrenForAutofill(ArrayList<View> list, int flags) {
        int childrenCount = this.mChildrenCount;
        if (childrenCount > 0) {
            ArrayList<View> preorderedList = buildOrderedChildList();
            boolean customOrder = preorderedList == null && isChildrenDrawingOrderEnabled();
            for (int i = 0; i < childrenCount; i++) {
                int childIndex = getAndVerifyPreorderedIndex(childrenCount, i, customOrder);
                View child = preorderedList == null ? this.mChildren[childIndex] : (View) preorderedList.get(childIndex);
                if ((flags & 1) != 0 || child.isImportantForAutofill()) {
                    list.add(child);
                } else if (child instanceof ViewGroup) {
                    ((ViewGroup) child).populateChildrenForAutofill(list, flags);
                }
            }
        }
    }

    private static View getAndVerifyPreorderedView(ArrayList<View> preorderedList, View[] children, int childIndex) {
        if (preorderedList == null) {
            return children[childIndex];
        }
        View child = (View) preorderedList.get(childIndex);
        if (child != null) {
            return child;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid preorderedList contained null child at index ");
        stringBuilder.append(childIndex);
        throw new RuntimeException(stringBuilder.toString());
    }

    @UnsupportedAppUsage
    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        if (getAccessibilityNodeProvider() == null && this.mAttachInfo != null) {
            ArrayList<View> childrenForAccessibility = this.mAttachInfo.mTempArrayList;
            childrenForAccessibility.clear();
            addChildrenForAccessibility(childrenForAccessibility);
            int childrenForAccessibilityCount = childrenForAccessibility.size();
            for (int i = 0; i < childrenForAccessibilityCount; i++) {
                info.addChildUnchecked((View) childrenForAccessibility.get(i));
            }
            childrenForAccessibility.clear();
        }
    }

    public CharSequence getAccessibilityClassName() {
        return ViewGroup.class.getName();
    }

    public void notifySubtreeAccessibilityStateChanged(View child, View source, int changeType) {
        if (getAccessibilityLiveRegion() != 0) {
            notifyViewAccessibilityStateChangedIfNeeded(1);
        } else if (this.mParent != null) {
            try {
                this.mParent.notifySubtreeAccessibilityStateChanged(this, source, changeType);
            } catch (AbstractMethodError e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.mParent.getClass().getSimpleName());
                stringBuilder.append(" does not fully implement ViewParent");
                Log.e("View", stringBuilder.toString(), e);
            }
        }
    }

    public void notifySubtreeAccessibilityStateChangedIfNeeded() {
        if (AccessibilityManager.getInstance(this.mContext).isEnabled() && this.mAttachInfo != null) {
            if (!(getImportantForAccessibility() == 4 || isImportantForAccessibility() || getChildCount() <= 0)) {
                ViewParent a11yParent = getParentForAccessibility();
                if (a11yParent instanceof View) {
                    ((View) a11yParent).notifySubtreeAccessibilityStateChangedIfNeeded();
                    return;
                }
            }
            super.notifySubtreeAccessibilityStateChangedIfNeeded();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void resetSubtreeAccessibilityStateChanged() {
        super.resetSubtreeAccessibilityStateChanged();
        View[] children = this.mChildren;
        int childCount = this.mChildrenCount;
        for (int i = 0; i < childCount; i++) {
            children[i].resetSubtreeAccessibilityStateChanged();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public int getNumChildrenForAccessibility() {
        int numChildrenForAccessibility = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.includeForAccessibility()) {
                numChildrenForAccessibility++;
            } else if (child instanceof ViewGroup) {
                numChildrenForAccessibility += ((ViewGroup) child).getNumChildrenForAccessibility();
            }
        }
        return numChildrenForAccessibility;
    }

    public boolean onNestedPrePerformAccessibilityAction(View target, int action, Bundle args) {
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dispatchDetachedFromWindow() {
        int i;
        cancelAndClearTouchTargets(null);
        exitHoverTargets();
        exitTooltipHoverTargets();
        int transientCount = 0;
        this.mLayoutCalledWhileSuppressed = false;
        this.mChildrenInterestedInDrag = null;
        this.mIsInterestedInDrag = false;
        DragEvent dragEvent = this.mCurrentDragStartEvent;
        if (dragEvent != null) {
            dragEvent.recycle();
            this.mCurrentDragStartEvent = null;
        }
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (i = 0; i < count; i++) {
            children[i].dispatchDetachedFromWindow();
        }
        clearDisappearingChildren();
        if (this.mTransientViews != null) {
            transientCount = this.mTransientIndices.size();
        }
        for (i = 0; i < transientCount; i++) {
            ((View) this.mTransientViews.get(i)).dispatchDetachedFromWindow();
        }
        super.dispatchDetachedFromWindow();
    }

    /* Access modifiers changed, original: protected */
    public void internalSetPadding(int left, int top, int right, int bottom) {
        super.internalSetPadding(left, top, right, bottom);
        if ((((this.mPaddingLeft | this.mPaddingTop) | this.mPaddingRight) | this.mPaddingBottom) != 0) {
            this.mGroupFlags |= 32;
        } else {
            this.mGroupFlags &= -33;
        }
    }

    /* Access modifiers changed, original: protected */
    public void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        super.dispatchSaveInstanceState(container);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            View c = children[i];
            if ((c.mViewFlags & 536870912) != 536870912) {
                c.dispatchSaveInstanceState(container);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void dispatchFreezeSelfOnly(SparseArray<Parcelable> container) {
        super.dispatchSaveInstanceState(container);
    }

    /* Access modifiers changed, original: protected */
    public void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        super.dispatchRestoreInstanceState(container);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            View c = children[i];
            if ((c.mViewFlags & 536870912) != 536870912) {
                c.dispatchRestoreInstanceState(container);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void dispatchThawSelfOnly(SparseArray<Parcelable> container) {
        super.dispatchRestoreInstanceState(container);
    }

    /* Access modifiers changed, original: protected */
    @Deprecated
    public void setChildrenDrawingCacheEnabled(boolean enabled) {
        if (enabled || (this.mPersistentDrawingCache & 3) != 3) {
            View[] children = this.mChildren;
            int count = this.mChildrenCount;
            for (int i = 0; i < count; i++) {
                children[i].setDrawingCacheEnabled(enabled);
            }
        }
    }

    public Bitmap createSnapshot(CanvasProvider canvasProvider, boolean skipChildren) {
        int count = this.mChildrenCount;
        int[] visibilities = null;
        if (skipChildren) {
            visibilities = new int[count];
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                visibilities[i] = child.getVisibility();
                if (visibilities[i] == 0) {
                    child.mViewFlags = (child.mViewFlags & -13) | 4;
                }
            }
        }
        int i2;
        View child2;
        try {
            Bitmap createSnapshot = super.createSnapshot(canvasProvider, skipChildren);
            if (skipChildren) {
                for (i2 = 0; i2 < count; i2++) {
                    child2 = getChildAt(i2);
                    child2.mViewFlags = (child2.mViewFlags & -13) | (visibilities[i2] & 12);
                }
            }
            return createSnapshot;
        } catch (Throwable th) {
            if (skipChildren) {
                for (i2 = 0; i2 < count; i2++) {
                    child2 = getChildAt(i2);
                    child2.mViewFlags = (child2.mViewFlags & -13) | (visibilities[i2] & 12);
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isLayoutModeOptical() {
        return this.mLayoutMode == 1;
    }

    /* Access modifiers changed, original: 0000 */
    public Insets computeOpticalInsets() {
        if (!isLayoutModeOptical()) {
            return Insets.NONE;
        }
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;
        for (int i = 0; i < this.mChildrenCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                Insets insets = child.getOpticalInsets();
                left = Math.max(left, insets.left);
                top = Math.max(top, insets.top);
                right = Math.max(right, insets.right);
                bottom = Math.max(bottom, insets.bottom);
            }
        }
        return Insets.of(left, top, right, bottom);
    }

    private static void fillRect(Canvas canvas, Paint paint, int x1, int y1, int x2, int y2) {
        if (x1 != x2 && y1 != y2) {
            int tmp;
            if (x1 > x2) {
                tmp = x1;
                x1 = x2;
                x2 = tmp;
            }
            if (y1 > y2) {
                tmp = y1;
                y1 = y2;
                y2 = tmp;
            }
            canvas.drawRect((float) x1, (float) y1, (float) x2, (float) y2, paint);
        }
    }

    private static int sign(int x) {
        return x >= 0 ? 1 : -1;
    }

    private static void drawCorner(Canvas c, Paint paint, int x1, int y1, int dx, int dy, int lw) {
        fillRect(c, paint, x1, y1, x1 + dx, y1 + (sign(dy) * lw));
        fillRect(c, paint, x1, y1, x1 + (sign(dx) * lw), y1 + dy);
    }

    private static void drawRectCorners(Canvas canvas, int x1, int y1, int x2, int y2, Paint paint, int lineLength, int lineWidth) {
        int i = lineLength;
        Canvas canvas2 = canvas;
        Paint paint2 = paint;
        int i2 = x1;
        int i3 = lineLength;
        int i4 = lineWidth;
        drawCorner(canvas2, paint2, i2, y1, i3, lineLength, i4);
        drawCorner(canvas2, paint2, i2, y2, i3, -i, i4);
        drawCorner(canvas2, paint2, x2, y1, -i, lineLength, i4);
        drawCorner(canvas, paint, x2, y2, -i, -i, lineWidth);
    }

    private static void fillDifference(Canvas canvas, int x2, int y2, int x3, int y3, int dx1, int dy1, int dx2, int dy2, Paint paint) {
        int x1 = x2 - dx1;
        int x4 = x3 + dx2;
        int y4 = y3 + dy2;
        Canvas canvas2 = canvas;
        Paint paint2 = paint;
        int i = x1;
        fillRect(canvas2, paint2, i, y2 - dy1, x4, y2);
        int i2 = y2;
        int i3 = y3;
        fillRect(canvas2, paint2, i, i2, x2, i3);
        int i4 = x4;
        fillRect(canvas2, paint2, x3, i2, i4, i3);
        fillRect(canvas2, paint2, x1, y3, i4, y4);
    }

    /* Access modifiers changed, original: protected */
    public void onDebugDrawMargins(Canvas canvas, Paint paint) {
        for (int i = 0; i < getChildCount(); i++) {
            View c = getChildAt(i);
            c.getLayoutParams().onDebugDraw(c, canvas, paint);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDebugDraw(Canvas canvas) {
        Paint paint = View.getDebugPaint();
        paint.setColor(-65536);
        paint.setStyle(Style.STROKE);
        for (int i = 0; i < getChildCount(); i++) {
            View c = getChildAt(i);
            if (c.getVisibility() != 8) {
                Insets insets = c.getOpticalInsets();
                drawRect(canvas, paint, insets.left + c.getLeft(), insets.top + c.getTop(), (c.getRight() - insets.right) - 1, (c.getBottom() - insets.bottom) - 1);
            }
        }
        paint.setColor(Color.argb(63, 255, 0, 255));
        paint.setStyle(Style.FILL);
        onDebugDrawMargins(canvas, paint);
        paint.setColor(DEBUG_CORNERS_COLOR);
        paint.setStyle(Style.FILL);
        int lineLength = dipsToPixels(8);
        int lineWidth = dipsToPixels(1);
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            View c2 = getChildAt(i2);
            if (c2.getVisibility() != 8) {
                drawRectCorners(canvas, c2.getLeft(), c2.getTop(), c2.getRight(), c2.getBottom(), paint, lineLength, lineWidth);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void dispatchDraw(Canvas canvas) {
        int flags;
        Canvas canvas2 = canvas;
        boolean usingRenderNodeProperties = canvas2.isRecordingFor(this.mRenderNode);
        int childrenCount = this.mChildrenCount;
        View[] children = this.mChildren;
        int flags2 = this.mGroupFlags;
        if ((flags2 & 8) != 0 && canAnimate()) {
            boolean buildCache = isHardwareAccelerated() ^ true;
            for (int i = 0; i < childrenCount; i++) {
                View child = children[i];
                if ((child.mViewFlags & 12) == 0) {
                    attachLayoutAnimationParameters(child, child.getLayoutParams(), i, childrenCount);
                    bindLayoutAnimation(child);
                }
            }
            LayoutAnimationController controller = this.mLayoutAnimationController;
            if (controller.willOverlap()) {
                this.mGroupFlags |= 128;
            }
            controller.start();
            this.mGroupFlags &= -9;
            this.mGroupFlags &= -17;
            AnimationListener animationListener = this.mAnimationListener;
            if (animationListener != null) {
                animationListener.onAnimationStart(controller.getAnimation());
            }
        }
        int clipSaveCount = 0;
        boolean customOrder = false;
        boolean clipToPadding = (flags2 & 34) == 34;
        if (clipToPadding) {
            clipSaveCount = canvas2.save(2);
            canvas2.clipRect(this.mScrollX + this.mPaddingLeft, this.mScrollY + this.mPaddingTop, ((this.mScrollX + this.mRight) - this.mLeft) - this.mPaddingRight, ((this.mScrollY + this.mBottom) - this.mTop) - this.mPaddingBottom);
        }
        this.mPrivateFlags &= -65;
        this.mGroupFlags &= -5;
        long drawingTime = getDrawingTime();
        if (usingRenderNodeProperties) {
            canvas.insertReorderBarrier();
        }
        List list = this.mTransientIndices;
        int transientCount = list == null ? 0 : list.size();
        int transientIndex = transientCount != 0 ? 0 : -1;
        ArrayList<View> preorderedList = usingRenderNodeProperties ? null : buildOrderedChildList();
        if (preorderedList == null && isChildrenDrawingOrderEnabled()) {
            customOrder = true;
        }
        boolean more = false;
        int i2 = 0;
        while (i2 < childrenCount) {
            View transientChild;
            while (transientIndex >= 0 && ((Integer) this.mTransientIndices.get(transientIndex)).intValue() == i2) {
                transientChild = (View) this.mTransientViews.get(transientIndex);
                flags = flags2;
                if ((transientChild.mViewFlags & 12) == 0 || transientChild.getAnimation() != null) {
                    more |= drawChild(canvas2, transientChild, drawingTime);
                }
                transientIndex++;
                if (transientIndex >= transientCount) {
                    transientIndex = -1;
                }
                flags2 = flags;
            }
            flags = flags2;
            transientChild = getAndVerifyPreorderedView(preorderedList, children, getAndVerifyPreorderedIndex(childrenCount, i2, customOrder));
            int childrenCount2 = childrenCount;
            if ((transientChild.mViewFlags & 12) == 0 || transientChild.getAnimation() != null) {
                more |= drawChild(canvas2, transientChild, drawingTime);
            }
            i2++;
            flags2 = flags;
            childrenCount = childrenCount2;
        }
        flags = flags2;
        while (transientIndex >= 0) {
            View transientChild2 = (View) this.mTransientViews.get(transientIndex);
            if ((transientChild2.mViewFlags & 12) == 0 || transientChild2.getAnimation() != null) {
                more |= drawChild(canvas2, transientChild2, drawingTime);
            }
            transientIndex++;
            if (transientIndex >= transientCount) {
                break;
            }
        }
        if (preorderedList != null) {
            preorderedList.clear();
        }
        if (this.mDisappearingChildren != null) {
            ArrayList<View> disappearingChildren = this.mDisappearingChildren;
            for (int i3 = disappearingChildren.size() - 1; i3 >= 0; i3--) {
                more |= drawChild(canvas2, (View) disappearingChildren.get(i3), drawingTime);
            }
        }
        if (usingRenderNodeProperties) {
            canvas.insertInorderBarrier();
        }
        if (debugDraw()) {
            onDebugDraw(canvas);
        }
        if (clipToPadding) {
            canvas2.restoreToCount(clipSaveCount);
        }
        childrenCount = this.mGroupFlags;
        if ((childrenCount & 4) == 4) {
            invalidate(true);
        }
        if ((childrenCount & 16) == 0 && (childrenCount & 512) == 0) {
            LayoutAnimationController layoutAnimationController = this.mLayoutAnimationController;
            if (layoutAnimationController != null && layoutAnimationController.isDone() && !more) {
                this.mGroupFlags |= 512;
                post(new Runnable() {
                    public void run() {
                        ViewGroup.this.notifyAnimationListener();
                    }
                });
            }
        }
    }

    public ViewGroupOverlay getOverlay() {
        if (this.mOverlay == null) {
            this.mOverlay = new ViewGroupOverlay(this.mContext, this);
        }
        return (ViewGroupOverlay) this.mOverlay;
    }

    /* Access modifiers changed, original: protected */
    public int getChildDrawingOrder(int childCount, int drawingPosition) {
        return drawingPosition;
    }

    public final int getChildDrawingOrder(int drawingPosition) {
        return getChildDrawingOrder(getChildCount(), drawingPosition);
    }

    private boolean hasChildWithZ() {
        for (int i = 0; i < this.mChildrenCount; i++) {
            if (this.mChildren[i].getZ() != 0.0f) {
                return true;
            }
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public ArrayList<View> buildOrderedChildList() {
        int childrenCount = this.mChildrenCount;
        if (childrenCount <= 1 || !hasChildWithZ()) {
            return null;
        }
        ArrayList arrayList = this.mPreSortedChildren;
        if (arrayList == null) {
            this.mPreSortedChildren = new ArrayList(childrenCount);
        } else {
            arrayList.clear();
            this.mPreSortedChildren.ensureCapacity(childrenCount);
        }
        boolean customOrder = isChildrenDrawingOrderEnabled();
        for (int i = 0; i < childrenCount; i++) {
            View nextChild = this.mChildren[getAndVerifyPreorderedIndex(childrenCount, i, customOrder)];
            float currentZ = nextChild.getZ();
            int insertIndex = i;
            while (insertIndex > 0 && ((View) this.mPreSortedChildren.get(insertIndex - 1)).getZ() > currentZ) {
                insertIndex--;
            }
            this.mPreSortedChildren.add(insertIndex, nextChild);
        }
        return this.mPreSortedChildren;
    }

    private void notifyAnimationListener() {
        this.mGroupFlags &= -513;
        this.mGroupFlags |= 16;
        if (this.mAnimationListener != null) {
            post(new Runnable() {
                public void run() {
                    ViewGroup.this.mAnimationListener.onAnimationEnd(ViewGroup.this.mLayoutAnimationController.getAnimation());
                }
            });
        }
        invalidate(true);
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public void dispatchGetDisplayList() {
        int i;
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (i = 0; i < count; i++) {
            View child = children[i];
            if ((child.mViewFlags & 12) == 0 || child.getAnimation() != null) {
                recreateChildDisplayList(child);
            }
        }
        i = this.mTransientViews == null ? 0 : this.mTransientIndices.size();
        for (int i2 = 0; i2 < i; i2++) {
            View child2 = (View) this.mTransientViews.get(i2);
            if ((child2.mViewFlags & 12) == 0 || child2.getAnimation() != null) {
                recreateChildDisplayList(child2);
            }
        }
        if (this.mOverlay != null) {
            recreateChildDisplayList(this.mOverlay.getOverlayView());
        }
        if (this.mDisappearingChildren != null) {
            ArrayList<View> disappearingChildren = this.mDisappearingChildren;
            int disappearingCount = disappearingChildren.size();
            for (int i3 = 0; i3 < disappearingCount; i3++) {
                recreateChildDisplayList((View) disappearingChildren.get(i3));
            }
        }
    }

    private void recreateChildDisplayList(View child) {
        child.mRecreateDisplayList = (child.mPrivateFlags & Integer.MIN_VALUE) != 0;
        child.mPrivateFlags &= Integer.MAX_VALUE;
        child.updateDisplayListIfDirty();
        child.mRecreateDisplayList = false;
    }

    /* Access modifiers changed, original: protected */
    public boolean drawChild(Canvas canvas, View child, long drawingTime) {
        return child.draw(canvas, this, drawingTime);
    }

    /* Access modifiers changed, original: 0000 */
    public void getScrollIndicatorBounds(Rect out) {
        super.getScrollIndicatorBounds(out);
        if ((this.mGroupFlags & 34) == 34) {
            out.left += this.mPaddingLeft;
            out.right -= this.mPaddingRight;
            out.top += this.mPaddingTop;
            out.bottom -= this.mPaddingBottom;
        }
    }

    @ExportedProperty(category = "drawing")
    public boolean getClipChildren() {
        return (this.mGroupFlags & 1) != 0;
    }

    public void setClipChildren(boolean clipChildren) {
        if (clipChildren != ((this.mGroupFlags & 1) == 1)) {
            setBooleanFlag(1, clipChildren);
            for (int i = 0; i < this.mChildrenCount; i++) {
                View child = getChildAt(i);
                if (child.mRenderNode != null) {
                    child.mRenderNode.setClipToBounds(clipChildren);
                }
            }
            invalidate(true);
        }
    }

    public void setClipToPadding(boolean clipToPadding) {
        if (hasBooleanFlag(2) != clipToPadding) {
            setBooleanFlag(2, clipToPadding);
            invalidate(true);
        }
    }

    @ExportedProperty(category = "drawing")
    public boolean getClipToPadding() {
        return hasBooleanFlag(2);
    }

    public void dispatchSetSelected(boolean selected) {
        View[] children = this.mChildren;
        int count = this.mChildrenCount;
        for (int i = 0; i < count; i++) {
            children[i].setSelected(selected);
        }
    }

    public void dispatchSetActivated(boolean activated) {
        View[] children = this.mChildren;
        int count = this.mChildrenCount;
        for (int i = 0; i < count; i++) {
            children[i].setActivated(activated);
        }
    }

    /* Access modifiers changed, original: protected */
    public void dispatchSetPressed(boolean pressed) {
        View[] children = this.mChildren;
        int count = this.mChildrenCount;
        for (int i = 0; i < count; i++) {
            View child = children[i];
            if (!(pressed && (child.isClickable() || child.isLongClickable()))) {
                child.setPressed(pressed);
            }
        }
    }

    public void dispatchDrawableHotspotChanged(float x, float y) {
        int count = this.mChildrenCount;
        if (count != 0) {
            View[] children = this.mChildren;
            for (int i = 0; i < count; i++) {
                View child = children[i];
                boolean nonActionable = (child.isClickable() || child.isLongClickable()) ? false : true;
                boolean duplicatesState = (child.mViewFlags & 4194304) != 0;
                if (nonActionable || duplicatesState) {
                    float[] point = getTempPoint();
                    point[0] = x;
                    point[1] = y;
                    transformPointToViewLocal(point, child);
                    child.drawableHotspotChanged(point[0], point[1]);
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchCancelPendingInputEvents() {
        super.dispatchCancelPendingInputEvents();
        View[] children = this.mChildren;
        int count = this.mChildrenCount;
        for (int i = 0; i < count; i++) {
            children[i].dispatchCancelPendingInputEvents();
        }
    }

    /* Access modifiers changed, original: protected */
    public void setStaticTransformationsEnabled(boolean enabled) {
        setBooleanFlag(2048, enabled);
    }

    /* Access modifiers changed, original: protected */
    public boolean getChildStaticTransformation(View child, Transformation t) {
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public Transformation getChildTransformation() {
        if (this.mChildTransformation == null) {
            this.mChildTransformation = new Transformation();
        }
        return this.mChildTransformation;
    }

    /* Access modifiers changed, original: protected */
    public <T extends View> T findViewTraversal(int id) {
        if (id == this.mID) {
            return this;
        }
        View[] where = this.mChildren;
        int len = this.mChildrenCount;
        for (int i = 0; i < len; i++) {
            View v = where[i];
            if ((v.mPrivateFlags & 8) == 0) {
                v = v.findViewById(id);
                if (v != null) {
                    return v;
                }
            }
        }
        return null;
    }

    /* Access modifiers changed, original: protected */
    public <T extends View> T findViewWithTagTraversal(Object tag) {
        if (tag != null && tag.equals(this.mTag)) {
            return this;
        }
        View[] where = this.mChildren;
        int len = this.mChildrenCount;
        for (int i = 0; i < len; i++) {
            View v = where[i];
            if ((v.mPrivateFlags & 8) == 0) {
                v = v.findViewWithTag(tag);
                if (v != null) {
                    return v;
                }
            }
        }
        return null;
    }

    /* Access modifiers changed, original: protected */
    public <T extends View> T findViewByPredicateTraversal(Predicate<View> predicate, View childToSkip) {
        if (predicate.test(this)) {
            return this;
        }
        View[] where = this.mChildren;
        int len = this.mChildrenCount;
        for (int i = 0; i < len; i++) {
            View v = where[i];
            if (v != childToSkip && (v.mPrivateFlags & 8) == 0) {
                v = v.findViewByPredicate(predicate);
                if (v != null) {
                    return v;
                }
            }
        }
        return null;
    }

    @UnsupportedAppUsage
    public void addTransientView(View view, int index) {
        if (index >= 0 && view != null) {
            if (view.mParent == null) {
                if (this.mTransientIndices == null) {
                    this.mTransientIndices = new ArrayList();
                    this.mTransientViews = new ArrayList();
                }
                int oldSize = this.mTransientIndices.size();
                if (oldSize > 0) {
                    int insertionIndex = 0;
                    while (insertionIndex < oldSize && index >= ((Integer) this.mTransientIndices.get(insertionIndex)).intValue()) {
                        insertionIndex++;
                    }
                    this.mTransientIndices.add(insertionIndex, Integer.valueOf(index));
                    this.mTransientViews.add(insertionIndex, view);
                } else {
                    this.mTransientIndices.add(Integer.valueOf(index));
                    this.mTransientViews.add(view);
                }
                view.mParent = this;
                if (this.mAttachInfo != null) {
                    view.dispatchAttachedToWindow(this.mAttachInfo, this.mViewFlags & 12);
                }
                invalidate(true);
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The specified view already has a parent ");
            stringBuilder.append(view.mParent);
            throw new IllegalStateException(stringBuilder.toString());
        }
    }

    @UnsupportedAppUsage
    public void removeTransientView(View view) {
        int size = this.mTransientViews;
        if (size != 0) {
            size = size.size();
            for (int i = 0; i < size; i++) {
                if (view == this.mTransientViews.get(i)) {
                    this.mTransientViews.remove(i);
                    this.mTransientIndices.remove(i);
                    view.mParent = null;
                    if (view.mAttachInfo != null) {
                        view.dispatchDetachedFromWindow();
                    }
                    invalidate(true);
                    return;
                }
            }
        }
    }

    @UnsupportedAppUsage
    public int getTransientViewCount() {
        List list = this.mTransientIndices;
        return list == null ? 0 : list.size();
    }

    public int getTransientViewIndex(int position) {
        if (position >= 0) {
            List list = this.mTransientIndices;
            if (list != null && position < list.size()) {
                return ((Integer) this.mTransientIndices.get(position)).intValue();
            }
        }
        return -1;
    }

    @UnsupportedAppUsage
    public View getTransientView(int position) {
        List list = this.mTransientViews;
        if (list == null || position >= list.size()) {
            return null;
        }
        return (View) this.mTransientViews.get(position);
    }

    public void addView(View child) {
        addView(child, -1);
    }

    public void addView(View child, int index) {
        if (child != null) {
            LayoutParams params = child.getLayoutParams();
            if (params == null) {
                params = generateDefaultLayoutParams();
                if (params == null) {
                    throw new IllegalArgumentException("generateDefaultLayoutParams() cannot return null");
                }
            }
            addView(child, index, params);
            return;
        }
        throw new IllegalArgumentException("Cannot add a null child view to a ViewGroup");
    }

    public void addView(View child, int width, int height) {
        LayoutParams params = generateDefaultLayoutParams();
        params.width = width;
        params.height = height;
        addView(child, -1, params);
    }

    public void addView(View child, LayoutParams params) {
        addView(child, -1, params);
    }

    public void addView(View child, int index, LayoutParams params) {
        if (child != null) {
            requestLayout();
            invalidate(true);
            addViewInner(child, index, params, false);
            return;
        }
        throw new IllegalArgumentException("Cannot add a null child view to a ViewGroup");
    }

    public void updateViewLayout(View view, LayoutParams params) {
        StringBuilder stringBuilder;
        if (!checkLayoutParams(params)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid LayoutParams supplied to ");
            stringBuilder.append(this);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (view.mParent == this) {
            view.setLayoutParams(params);
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Given view not a child of ");
            stringBuilder.append(this);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean checkLayoutParams(LayoutParams p) {
        return p != null;
    }

    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        this.mOnHierarchyChangeListener = listener;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dispatchViewAdded(View child) {
        onViewAdded(child);
        OnHierarchyChangeListener onHierarchyChangeListener = this.mOnHierarchyChangeListener;
        if (onHierarchyChangeListener != null) {
            onHierarchyChangeListener.onChildViewAdded(this, child);
        }
    }

    public void onViewAdded(View child) {
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dispatchViewRemoved(View child) {
        onViewRemoved(child);
        OnHierarchyChangeListener onHierarchyChangeListener = this.mOnHierarchyChangeListener;
        if (onHierarchyChangeListener != null) {
            onHierarchyChangeListener.onChildViewRemoved(this, child);
        }
    }

    public void onViewRemoved(View child) {
    }

    private void clearCachedLayoutMode() {
        if (!hasBooleanFlag(8388608)) {
            this.mLayoutMode = -1;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        clearCachedLayoutMode();
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearCachedLayoutMode();
    }

    /* Access modifiers changed, original: protected */
    public void destroyHardwareResources() {
        super.destroyHardwareResources();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).destroyHardwareResources();
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean addViewInLayout(View child, int index, LayoutParams params) {
        return addViewInLayout(child, index, params, false);
    }

    /* Access modifiers changed, original: protected */
    public boolean addViewInLayout(View child, int index, LayoutParams params, boolean preventRequestLayout) {
        if (child != null) {
            child.mParent = null;
            addViewInner(child, index, params, preventRequestLayout);
            child.mPrivateFlags = (child.mPrivateFlags & -2097153) | 32;
            return true;
        }
        throw new IllegalArgumentException("Cannot add a null child view to a ViewGroup");
    }

    /* Access modifiers changed, original: protected */
    public void cleanupLayoutState(View child) {
        child.mPrivateFlags &= -4097;
    }

    private void addViewInner(View child, int index, LayoutParams params, boolean preventRequestLayout) {
        LayoutTransition layoutTransition = this.mTransition;
        if (layoutTransition != null) {
            layoutTransition.cancel(3);
        }
        if (child.getParent() == null) {
            layoutTransition = this.mTransition;
            if (layoutTransition != null) {
                layoutTransition.addChild(this, child);
            }
            if (!checkLayoutParams(params)) {
                params = generateLayoutParams(params);
            }
            if (preventRequestLayout) {
                child.mLayoutParams = params;
            } else {
                child.setLayoutParams(params);
            }
            if (index < 0) {
                index = this.mChildrenCount;
            }
            addInArray(child, index);
            if (preventRequestLayout) {
                child.assignParent(this);
            } else {
                child.mParent = this;
            }
            if (child.hasUnhandledKeyListener()) {
                incrementChildUnhandledKeyListeners();
            }
            if (child.hasFocus()) {
                requestChildFocus(child, child.findFocus());
            }
            AttachInfo ai = this.mAttachInfo;
            if (ai != null && (this.mGroupFlags & 4194304) == 0) {
                boolean lastKeepOn = ai.mKeepScreenOn;
                ai.mKeepScreenOn = false;
                child.dispatchAttachedToWindow(this.mAttachInfo, this.mViewFlags & 12);
                if (ai.mKeepScreenOn) {
                    needGlobalAttributesUpdate(true);
                }
                ai.mKeepScreenOn = lastKeepOn;
            }
            if (child.isLayoutDirectionInherited()) {
                child.resetRtlProperties();
            }
            dispatchViewAdded(child);
            if ((child.mViewFlags & 4194304) == 4194304) {
                this.mGroupFlags |= 65536;
            }
            if (child.hasTransientState()) {
                childHasTransientStateChanged(child, true);
            }
            if (child.getVisibility() != 8) {
                notifySubtreeAccessibilityStateChangedIfNeeded();
            }
            int transientCount = this.mTransientIndices;
            if (transientCount != 0) {
                transientCount = transientCount.size();
                for (int i = 0; i < transientCount; i++) {
                    int oldIndex = ((Integer) this.mTransientIndices.get(i)).intValue();
                    if (index <= oldIndex) {
                        this.mTransientIndices.set(i, Integer.valueOf(oldIndex + 1));
                    }
                }
            }
            if (this.mCurrentDragStartEvent != null && child.getVisibility() == 0) {
                notifyChildOfDragStart(child);
            }
            if (child.hasDefaultFocus()) {
                setDefaultFocus(child);
            }
            touchAccessibilityNodeProviderIfNeeded(child);
            return;
        }
        throw new IllegalStateException("The specified child already has a parent. You must call removeView() on the child's parent first.");
    }

    private void touchAccessibilityNodeProviderIfNeeded(View child) {
        if (this.mContext.isAutofillCompatibilityEnabled()) {
            child.getAccessibilityNodeProvider();
        }
    }

    private void addInArray(View child, int index) {
        if (Extension.get().getExtension() != null) {
            ((Interface) Extension.get().getExtension().asInterface()).addInArray(this, child, index);
        } else {
            originalAddInArray(child, index);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void originalAddInArray(View child, int index) {
        View[] children = this.mChildren;
        int count = this.mChildrenCount;
        int size = children.length;
        int i;
        if (index == count) {
            if (size == count) {
                this.mChildren = new View[(size + 12)];
                System.arraycopy(children, 0, this.mChildren, 0, size);
                children = this.mChildren;
            }
            i = this.mChildrenCount;
            this.mChildrenCount = i + 1;
            children[i] = child;
        } else if (index < count) {
            if (size == count) {
                this.mChildren = new View[(size + 12)];
                System.arraycopy(children, 0, this.mChildren, 0, index);
                System.arraycopy(children, index, this.mChildren, index + 1, count - index);
                children = this.mChildren;
            } else {
                System.arraycopy(children, index, children, index + 1, count - index);
            }
            children[index] = child;
            this.mChildrenCount++;
            i = this.mLastTouchDownIndex;
            if (i >= index) {
                this.mLastTouchDownIndex = i + 1;
            }
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("index=");
            stringBuilder.append(index);
            stringBuilder.append(" count=");
            stringBuilder.append(count);
            throw new IndexOutOfBoundsException(stringBuilder.toString());
        }
    }

    private void removeFromArray(int index) {
        if (Extension.get().getExtension() != null) {
            ((Interface) Extension.get().getExtension().asInterface()).removeFromArray(this, index);
        } else {
            originalRemoveFromArray(index);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void originalRemoveFromArray(int index) {
        View[] children = this.mChildren;
        ArrayList arrayList = this.mTransitioningViews;
        if (arrayList == null || !arrayList.contains(children[index])) {
            children[index].mParent = null;
        }
        int count = this.mChildrenCount;
        int i;
        if (index == count - 1) {
            i = this.mChildrenCount - 1;
            this.mChildrenCount = i;
            children[i] = null;
        } else if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException();
        } else {
            System.arraycopy(children, index + 1, children, index, (count - index) - 1);
            i = this.mChildrenCount - 1;
            this.mChildrenCount = i;
            children[i] = null;
        }
        int i2 = this.mLastTouchDownIndex;
        if (i2 == index) {
            this.mLastTouchDownTime = 0;
            this.mLastTouchDownIndex = -1;
        } else if (i2 > index) {
            this.mLastTouchDownIndex = i2 - 1;
        }
    }

    private void removeFromArray(int start, int count) {
        if (Extension.get().getExtension() != null) {
            ((Interface) Extension.get().getExtension().asInterface()).removeFromArray(this, start, count);
        } else {
            originalRemoveFromArray(start, count);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void originalRemoveFromArray(int start, int count) {
        View[] children = this.mChildren;
        int childrenCount = this.mChildrenCount;
        start = Math.max(0, start);
        int end = Math.min(childrenCount, start + count);
        if (start != end) {
            int i;
            if (end == childrenCount) {
                for (i = start; i < end; i++) {
                    children[i].mParent = null;
                    children[i] = null;
                }
            } else {
                for (i = start; i < end; i++) {
                    children[i].mParent = null;
                }
                System.arraycopy(children, end, children, start, childrenCount - end);
                for (i = childrenCount - (end - start); i < childrenCount; i++) {
                    children[i] = null;
                }
            }
            this.mChildrenCount -= end - start;
        }
    }

    private void bindLayoutAnimation(View child) {
        child.setAnimation(this.mLayoutAnimationController.getAnimationForView(child));
    }

    /* Access modifiers changed, original: protected */
    public void attachLayoutAnimationParameters(View child, LayoutParams params, int index, int count) {
        AnimationParameters animationParams = params.layoutAnimationParameters;
        if (animationParams == null) {
            animationParams = new AnimationParameters();
            params.layoutAnimationParameters = animationParams;
        }
        animationParams.count = count;
        animationParams.index = index;
    }

    public void removeView(View view) {
        if (removeViewInternal(view)) {
            requestLayout();
            invalidate(true);
        }
    }

    public void removeViewInLayout(View view) {
        removeViewInternal(view);
    }

    public void removeViewsInLayout(int start, int count) {
        removeViewsInternal(start, count);
    }

    public void removeViewAt(int index) {
        removeViewInternal(index, getChildAt(index));
        requestLayout();
        invalidate(true);
    }

    public void removeViews(int start, int count) {
        removeViewsInternal(start, count);
        requestLayout();
        invalidate(true);
    }

    private boolean removeViewInternal(View view) {
        int index = indexOfChild(view);
        if (index < 0) {
            return false;
        }
        removeViewInternal(index, view);
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0044  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0053  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x005a  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x005f  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0076  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x007e  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0086  */
    /* JADX WARNING: Removed duplicated region for block: B:51:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00a6  */
    private void removeViewInternal(int r7, android.view.View r8) {
        /*
        r6 = this;
        r0 = r6.mTransition;
        if (r0 == 0) goto L_0x0007;
    L_0x0004:
        r0.removeChild(r6, r8);
    L_0x0007:
        r0 = 0;
        r1 = r6.mFocused;
        if (r8 != r1) goto L_0x0011;
    L_0x000c:
        r1 = 0;
        r8.unFocus(r1);
        r0 = 1;
    L_0x0011:
        r1 = r6.mFocusedInCluster;
        if (r8 != r1) goto L_0x0018;
    L_0x0015:
        r6.clearFocusedInCluster(r8);
    L_0x0018:
        r8.clearAccessibilityFocus();
        r6.cancelTouchTarget(r8);
        r6.cancelHoverTarget(r8);
        r1 = r8.getAnimation();
        if (r1 != 0) goto L_0x003a;
    L_0x0027:
        r1 = r6.mTransitioningViews;
        if (r1 == 0) goto L_0x0032;
    L_0x002b:
        r1 = r1.contains(r8);
        if (r1 == 0) goto L_0x0032;
    L_0x0031:
        goto L_0x003a;
    L_0x0032:
        r1 = r8.mAttachInfo;
        if (r1 == 0) goto L_0x003d;
    L_0x0036:
        r8.dispatchDetachedFromWindow();
        goto L_0x003d;
    L_0x003a:
        r6.addDisappearingView(r8);
    L_0x003d:
        r1 = r8.hasTransientState();
        r2 = 0;
        if (r1 == 0) goto L_0x0047;
    L_0x0044:
        r6.childHasTransientStateChanged(r8, r2);
    L_0x0047:
        r6.needGlobalAttributesUpdate(r2);
        r6.removeFromArray(r7);
        r1 = r8.hasUnhandledKeyListener();
        if (r1 == 0) goto L_0x0056;
    L_0x0053:
        r6.decrementChildUnhandledKeyListeners();
    L_0x0056:
        r1 = r6.mDefaultFocus;
        if (r8 != r1) goto L_0x005d;
    L_0x005a:
        r6.clearDefaultFocus(r8);
    L_0x005d:
        if (r0 == 0) goto L_0x006b;
    L_0x005f:
        r6.clearChildFocus(r8);
        r1 = r6.rootViewRequestFocus();
        if (r1 != 0) goto L_0x006b;
    L_0x0068:
        r6.notifyGlobalFocusCleared(r6);
    L_0x006b:
        r6.dispatchViewRemoved(r8);
        r1 = r8.getVisibility();
        r3 = 8;
        if (r1 == r3) goto L_0x0079;
    L_0x0076:
        r6.notifySubtreeAccessibilityStateChangedIfNeeded();
    L_0x0079:
        r1 = r6.mTransientIndices;
        if (r1 != 0) goto L_0x007e;
    L_0x007d:
        goto L_0x0082;
    L_0x007e:
        r2 = r1.size();
    L_0x0082:
        r1 = r2;
        r2 = 0;
    L_0x0084:
        if (r2 >= r1) goto L_0x00a2;
    L_0x0086:
        r3 = r6.mTransientIndices;
        r3 = r3.get(r2);
        r3 = (java.lang.Integer) r3;
        r3 = r3.intValue();
        if (r7 >= r3) goto L_0x009f;
    L_0x0094:
        r4 = r6.mTransientIndices;
        r5 = r3 + -1;
        r5 = java.lang.Integer.valueOf(r5);
        r4.set(r2, r5);
    L_0x009f:
        r2 = r2 + 1;
        goto L_0x0084;
    L_0x00a2:
        r2 = r6.mCurrentDragStartEvent;
        if (r2 == 0) goto L_0x00ab;
    L_0x00a6:
        r2 = r6.mChildrenInterestedInDrag;
        r2.remove(r8);
    L_0x00ab:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.ViewGroup.removeViewInternal(int, android.view.View):void");
    }

    public void setLayoutTransition(LayoutTransition transition) {
        LayoutTransition previousTransition;
        if (this.mTransition != null) {
            previousTransition = this.mTransition;
            previousTransition.cancel();
            previousTransition.removeTransitionListener(this.mLayoutTransitionListener);
        }
        this.mTransition = transition;
        previousTransition = this.mTransition;
        if (previousTransition != null) {
            previousTransition.addTransitionListener(this.mLayoutTransitionListener);
        }
    }

    public LayoutTransition getLayoutTransition() {
        return this.mTransition;
    }

    /* JADX WARNING: Removed duplicated region for block: B:46:0x0063 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0060  */
    private void removeViewsInternal(int r11, int r12) {
        /*
        r10 = this;
        r0 = r11 + r12;
        if (r11 < 0) goto L_0x0083;
    L_0x0004:
        if (r12 < 0) goto L_0x0083;
    L_0x0006:
        r1 = r10.mChildrenCount;
        if (r0 > r1) goto L_0x0083;
    L_0x000a:
        r1 = r10.mFocused;
        r2 = r10.mAttachInfo;
        r3 = 0;
        if (r2 == 0) goto L_0x0013;
    L_0x0011:
        r2 = 1;
        goto L_0x0014;
    L_0x0013:
        r2 = r3;
    L_0x0014:
        r4 = 0;
        r5 = 0;
        r6 = r10.mChildren;
        r7 = r11;
    L_0x0019:
        if (r7 >= r0) goto L_0x006c;
    L_0x001b:
        r8 = r6[r7];
        r9 = r10.mTransition;
        if (r9 == 0) goto L_0x0024;
    L_0x0021:
        r9.removeChild(r10, r8);
    L_0x0024:
        if (r8 != r1) goto L_0x002b;
    L_0x0026:
        r9 = 0;
        r8.unFocus(r9);
        r4 = 1;
    L_0x002b:
        r9 = r10.mDefaultFocus;
        if (r8 != r9) goto L_0x0030;
    L_0x002f:
        r5 = r8;
    L_0x0030:
        r9 = r10.mFocusedInCluster;
        if (r8 != r9) goto L_0x0037;
    L_0x0034:
        r10.clearFocusedInCluster(r8);
    L_0x0037:
        r8.clearAccessibilityFocus();
        r10.cancelTouchTarget(r8);
        r10.cancelHoverTarget(r8);
        r9 = r8.getAnimation();
        if (r9 != 0) goto L_0x0057;
    L_0x0046:
        r9 = r10.mTransitioningViews;
        if (r9 == 0) goto L_0x0051;
    L_0x004a:
        r9 = r9.contains(r8);
        if (r9 == 0) goto L_0x0051;
    L_0x0050:
        goto L_0x0057;
    L_0x0051:
        if (r2 == 0) goto L_0x005a;
    L_0x0053:
        r8.dispatchDetachedFromWindow();
        goto L_0x005a;
    L_0x0057:
        r10.addDisappearingView(r8);
    L_0x005a:
        r9 = r8.hasTransientState();
        if (r9 == 0) goto L_0x0063;
    L_0x0060:
        r10.childHasTransientStateChanged(r8, r3);
    L_0x0063:
        r10.needGlobalAttributesUpdate(r3);
        r10.dispatchViewRemoved(r8);
        r7 = r7 + 1;
        goto L_0x0019;
    L_0x006c:
        r10.removeFromArray(r11, r12);
        if (r5 == 0) goto L_0x0074;
    L_0x0071:
        r10.clearDefaultFocus(r5);
    L_0x0074:
        if (r4 == 0) goto L_0x0082;
    L_0x0076:
        r10.clearChildFocus(r1);
        r3 = r10.rootViewRequestFocus();
        if (r3 != 0) goto L_0x0082;
    L_0x007f:
        r10.notifyGlobalFocusCleared(r1);
    L_0x0082:
        return;
    L_0x0083:
        r1 = new java.lang.IndexOutOfBoundsException;
        r1.<init>();
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.ViewGroup.removeViewsInternal(int, int):void");
    }

    public void removeAllViews() {
        removeAllViewsInLayout();
        requestLayout();
        invalidate(true);
    }

    /* JADX WARNING: Removed duplicated region for block: B:41:0x0057 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0054  */
    public void removeAllViewsInLayout() {
        /*
        r10 = this;
        r0 = r10.mChildrenCount;
        if (r0 > 0) goto L_0x0005;
    L_0x0004:
        return;
    L_0x0005:
        r1 = r10.mChildren;
        r2 = 0;
        r10.mChildrenCount = r2;
        r3 = r10.mFocused;
        r4 = r10.mAttachInfo;
        if (r4 == 0) goto L_0x0012;
    L_0x0010:
        r4 = 1;
        goto L_0x0013;
    L_0x0012:
        r4 = r2;
    L_0x0013:
        r5 = 0;
        r10.needGlobalAttributesUpdate(r2);
        r6 = r0 + -1;
    L_0x0019:
        if (r6 < 0) goto L_0x0061;
    L_0x001b:
        r7 = r1[r6];
        r8 = r10.mTransition;
        if (r8 == 0) goto L_0x0024;
    L_0x0021:
        r8.removeChild(r10, r7);
    L_0x0024:
        r8 = 0;
        if (r7 != r3) goto L_0x002b;
    L_0x0027:
        r7.unFocus(r8);
        r5 = 1;
    L_0x002b:
        r7.clearAccessibilityFocus();
        r10.cancelTouchTarget(r7);
        r10.cancelHoverTarget(r7);
        r9 = r7.getAnimation();
        if (r9 != 0) goto L_0x004b;
    L_0x003a:
        r9 = r10.mTransitioningViews;
        if (r9 == 0) goto L_0x0045;
    L_0x003e:
        r9 = r9.contains(r7);
        if (r9 == 0) goto L_0x0045;
    L_0x0044:
        goto L_0x004b;
    L_0x0045:
        if (r4 == 0) goto L_0x004e;
    L_0x0047:
        r7.dispatchDetachedFromWindow();
        goto L_0x004e;
    L_0x004b:
        r10.addDisappearingView(r7);
    L_0x004e:
        r9 = r7.hasTransientState();
        if (r9 == 0) goto L_0x0057;
    L_0x0054:
        r10.childHasTransientStateChanged(r7, r2);
    L_0x0057:
        r10.dispatchViewRemoved(r7);
        r7.mParent = r8;
        r1[r6] = r8;
        r6 = r6 + -1;
        goto L_0x0019;
    L_0x0061:
        r2 = r10.mDefaultFocus;
        if (r2 == 0) goto L_0x0068;
    L_0x0065:
        r10.clearDefaultFocus(r2);
    L_0x0068:
        r2 = r10.mFocusedInCluster;
        if (r2 == 0) goto L_0x006f;
    L_0x006c:
        r10.clearFocusedInCluster(r2);
    L_0x006f:
        if (r5 == 0) goto L_0x007d;
    L_0x0071:
        r10.clearChildFocus(r3);
        r2 = r10.rootViewRequestFocus();
        if (r2 != 0) goto L_0x007d;
    L_0x007a:
        r10.notifyGlobalFocusCleared(r3);
    L_0x007d:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.ViewGroup.removeAllViewsInLayout():void");
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0048  */
    public void removeDetachedView(android.view.View r2, boolean r3) {
        /*
        r1 = this;
        r0 = r1.mTransition;
        if (r0 == 0) goto L_0x0007;
    L_0x0004:
        r0.removeChild(r1, r2);
    L_0x0007:
        r0 = r1.mFocused;
        if (r2 != r0) goto L_0x000e;
    L_0x000b:
        r2.clearFocus();
    L_0x000e:
        r0 = r1.mDefaultFocus;
        if (r2 != r0) goto L_0x0015;
    L_0x0012:
        r1.clearDefaultFocus(r2);
    L_0x0015:
        r0 = r1.mFocusedInCluster;
        if (r2 != r0) goto L_0x001c;
    L_0x0019:
        r1.clearFocusedInCluster(r2);
    L_0x001c:
        r2.clearAccessibilityFocus();
        r1.cancelTouchTarget(r2);
        r1.cancelHoverTarget(r2);
        if (r3 == 0) goto L_0x002d;
    L_0x0027:
        r0 = r2.getAnimation();
        if (r0 != 0) goto L_0x0037;
    L_0x002d:
        r0 = r1.mTransitioningViews;
        if (r0 == 0) goto L_0x003b;
    L_0x0031:
        r0 = r0.contains(r2);
        if (r0 == 0) goto L_0x003b;
    L_0x0037:
        r1.addDisappearingView(r2);
        goto L_0x0042;
    L_0x003b:
        r0 = r2.mAttachInfo;
        if (r0 == 0) goto L_0x0042;
    L_0x003f:
        r2.dispatchDetachedFromWindow();
    L_0x0042:
        r0 = r2.hasTransientState();
        if (r0 == 0) goto L_0x004c;
    L_0x0048:
        r0 = 0;
        r1.childHasTransientStateChanged(r2, r0);
    L_0x004c:
        r1.dispatchViewRemoved(r2);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.ViewGroup.removeDetachedView(android.view.View, boolean):void");
    }

    /* Access modifiers changed, original: protected */
    public void attachViewToParent(View child, int index, LayoutParams params) {
        child.mLayoutParams = params;
        if (index < 0) {
            index = this.mChildrenCount;
        }
        addInArray(child, index);
        child.mParent = this;
        child.mPrivateFlags = (((child.mPrivateFlags & -2097153) & -32769) | 32) | Integer.MIN_VALUE;
        this.mPrivateFlags |= Integer.MIN_VALUE;
        if (child.hasFocus()) {
            requestChildFocus(child, child.findFocus());
        }
        boolean z = isAttachedToWindow() && getWindowVisibility() == 0 && isShown();
        dispatchVisibilityAggregated(z);
        notifySubtreeAccessibilityStateChangedIfNeeded();
    }

    /* Access modifiers changed, original: protected */
    public void detachViewFromParent(View child) {
        removeFromArray(indexOfChild(child));
    }

    /* Access modifiers changed, original: protected */
    public void detachViewFromParent(int index) {
        removeFromArray(index);
    }

    /* Access modifiers changed, original: protected */
    public void detachViewsFromParent(int start, int count) {
        removeFromArray(start, count);
    }

    /* Access modifiers changed, original: protected */
    public void detachAllViewsFromParent() {
        int count = this.mChildrenCount;
        if (count > 0) {
            View[] children = this.mChildren;
            this.mChildrenCount = 0;
            for (int i = count - 1; i >= 0; i--) {
                children[i].mParent = null;
                children[i] = null;
            }
        }
    }

    public void onDescendantInvalidated(View child, View target) {
        this.mPrivateFlags |= target.mPrivateFlags & 64;
        if ((target.mPrivateFlags & -2097153) != 0) {
            this.mPrivateFlags = (this.mPrivateFlags & -2097153) | 2097152;
            this.mPrivateFlags &= -32769;
        }
        if (this.mLayerType == 1) {
            this.mPrivateFlags |= -2145386496;
            target = this;
        }
        if (this.mParent != null) {
            this.mParent.onDescendantInvalidated(this, target);
        }
    }

    @Deprecated
    public final void invalidateChild(View child, Rect dirty) {
        View view = child;
        Rect rect = dirty;
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo == null || !attachInfo.mHardwareAccelerated) {
            ViewParent parent = this;
            if (attachInfo != null) {
                boolean z = true;
                boolean drawAnimation = (view.mPrivateFlags & 64) != 0;
                Matrix childMatrix = child.getMatrix();
                if (view.mLayerType != 0) {
                    this.mPrivateFlags |= Integer.MIN_VALUE;
                    this.mPrivateFlags &= -32769;
                }
                int[] location = attachInfo.mInvalidateChildLocation;
                location[0] = view.mLeft;
                location[1] = view.mTop;
                if (!(childMatrix.isIdentity() && (this.mGroupFlags & 2048) == 0)) {
                    Matrix transformMatrix;
                    RectF boundingRect = attachInfo.mTmpTransformRect;
                    boundingRect.set(rect);
                    if ((this.mGroupFlags & 2048) != 0) {
                        Transformation t = attachInfo.mTmpTransformation;
                        if (getChildStaticTransformation(view, t)) {
                            transformMatrix = attachInfo.mTmpMatrix;
                            transformMatrix.set(t.getMatrix());
                            if (!childMatrix.isIdentity()) {
                                transformMatrix.preConcat(childMatrix);
                            }
                        } else {
                            transformMatrix = childMatrix;
                        }
                    } else {
                        transformMatrix = childMatrix;
                    }
                    transformMatrix.mapRect(boundingRect);
                    rect.set((int) Math.floor((double) boundingRect.left), (int) Math.floor((double) boundingRect.top), (int) Math.ceil((double) boundingRect.right), (int) Math.ceil((double) boundingRect.bottom));
                }
                while (true) {
                    Matrix childMatrix2;
                    View view2 = null;
                    if (parent instanceof View) {
                        view2 = (View) parent;
                    }
                    if (drawAnimation) {
                        if (view2 != null) {
                            view2.mPrivateFlags |= 64;
                        } else if (parent instanceof ViewRootImpl) {
                            ((ViewRootImpl) parent).mIsAnimating = z;
                        }
                    }
                    if (!(view2 == null || (view2.mPrivateFlags & 2097152) == 2097152)) {
                        view2.mPrivateFlags = (view2.mPrivateFlags & -2097153) | 2097152;
                    }
                    parent = parent.invalidateChildInParent(location, rect);
                    if (view2 != null) {
                        Matrix m = view2.getMatrix();
                        if (m.isIdentity()) {
                            childMatrix2 = childMatrix;
                        } else {
                            RectF boundingRect2 = attachInfo.mTmpTransformRect;
                            boundingRect2.set(rect);
                            m.mapRect(boundingRect2);
                            childMatrix2 = childMatrix;
                            rect.set((int) Math.floor((double) boundingRect2.left), (int) Math.floor((double) boundingRect2.top), (int) Math.ceil((double) boundingRect2.right), (int) Math.ceil((double) boundingRect2.bottom));
                        }
                    } else {
                        childMatrix2 = childMatrix;
                    }
                    if (parent == null) {
                        break;
                    }
                    childMatrix = childMatrix2;
                    z = true;
                }
            }
            return;
        }
        onDescendantInvalidated(view, view);
    }

    @Deprecated
    public ViewParent invalidateChildInParent(int[] location, Rect dirty) {
        if ((this.mPrivateFlags & 32800) == 0) {
            return null;
        }
        int i = this.mGroupFlags;
        if ((i & 144) != 128) {
            dirty.offset(location[0] - this.mScrollX, location[1] - this.mScrollY);
            if ((this.mGroupFlags & 1) == 0) {
                dirty.union(0, 0, this.mRight - this.mLeft, this.mBottom - this.mTop);
            }
            i = this.mLeft;
            int top = this.mTop;
            if ((this.mGroupFlags & 1) == 1 && !dirty.intersect(0, 0, this.mRight - i, this.mBottom - top)) {
                dirty.setEmpty();
            }
            location[0] = i;
            location[1] = top;
        } else {
            if ((i & 1) == 1) {
                dirty.set(0, 0, this.mRight - this.mLeft, this.mBottom - this.mTop);
            } else {
                dirty.union(0, 0, this.mRight - this.mLeft, this.mBottom - this.mTop);
            }
            location[0] = this.mLeft;
            location[1] = this.mTop;
            this.mPrivateFlags &= -33;
        }
        this.mPrivateFlags &= -32769;
        if (this.mLayerType != 0) {
            this.mPrivateFlags |= Integer.MIN_VALUE;
        }
        return this.mParent;
    }

    public final void offsetDescendantRectToMyCoords(View descendant, Rect rect) {
        offsetRectBetweenParentAndChild(descendant, rect, true, false);
    }

    public final void offsetRectIntoDescendantCoords(View descendant, Rect rect) {
        offsetRectBetweenParentAndChild(descendant, rect, false, false);
    }

    /* Access modifiers changed, original: 0000 */
    public void offsetRectBetweenParentAndChild(View descendant, Rect rect, boolean offsetFromChildToParent, boolean clipToBounds) {
        if (descendant != this) {
            View theParent = descendant.mParent;
            while (theParent != null && (theParent instanceof View) && theParent != this) {
                View p;
                if (offsetFromChildToParent) {
                    rect.offset(descendant.mLeft - descendant.mScrollX, descendant.mTop - descendant.mScrollY);
                    if (clipToBounds) {
                        p = theParent;
                        if (!rect.intersect(0, 0, p.mRight - p.mLeft, p.mBottom - p.mTop)) {
                            rect.setEmpty();
                        }
                    }
                } else {
                    if (clipToBounds) {
                        p = theParent;
                        if (!rect.intersect(0, 0, p.mRight - p.mLeft, p.mBottom - p.mTop)) {
                            rect.setEmpty();
                        }
                    }
                    rect.offset(descendant.mScrollX - descendant.mLeft, descendant.mScrollY - descendant.mTop);
                }
                descendant = theParent;
                theParent = descendant.mParent;
            }
            if (theParent == this) {
                if (offsetFromChildToParent) {
                    rect.offset(descendant.mLeft - descendant.mScrollX, descendant.mTop - descendant.mScrollY);
                } else {
                    rect.offset(descendant.mScrollX - descendant.mLeft, descendant.mScrollY - descendant.mTop);
                }
                return;
            }
            throw new IllegalArgumentException("parameter must be a descendant of this view");
        }
    }

    @UnsupportedAppUsage
    public void offsetChildrenTopAndBottom(int offset) {
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        boolean invalidate = false;
        for (int i = 0; i < count; i++) {
            View v = children[i];
            v.mTop += offset;
            v.mBottom += offset;
            if (v.mRenderNode != null) {
                invalidate = true;
                v.mRenderNode.offsetTopAndBottom(offset);
            }
        }
        if (invalidate) {
            invalidateViewProperty(false, false);
        }
        notifySubtreeAccessibilityStateChangedIfNeeded();
    }

    public boolean getChildVisibleRect(View child, Rect r, Point offset) {
        return getChildVisibleRect(child, r, offset, false);
    }

    public boolean getChildVisibleRect(View child, Rect r, Point offset, boolean forceParentCheck) {
        RectF rect = this.mAttachInfo != null ? this.mAttachInfo.mTmpTransformRect : new RectF();
        rect.set(r);
        if (!child.hasIdentityMatrix()) {
            child.getMatrix().mapRect(rect);
        }
        int dx = child.mLeft - this.mScrollX;
        int dy = child.mTop - this.mScrollY;
        rect.offset((float) dx, (float) dy);
        if (offset != null) {
            if (!child.hasIdentityMatrix()) {
                float[] position;
                if (this.mAttachInfo != null) {
                    position = this.mAttachInfo.mTmpTransformLocation;
                } else {
                    position = new float[2];
                }
                position[0] = (float) offset.x;
                position[1] = (float) offset.y;
                child.getMatrix().mapPoints(position);
                offset.x = Math.round(position[0]);
                offset.y = Math.round(position[1]);
            }
            offset.x += dx;
            offset.y += dy;
        }
        int width = this.mRight - this.mLeft;
        int height = this.mBottom - this.mTop;
        boolean rectIsVisible = true;
        if (this.mParent == null || ((this.mParent instanceof ViewGroup) && ((ViewGroup) this.mParent).getClipChildren())) {
            rectIsVisible = rect.intersect(0.0f, 0.0f, (float) width, (float) height);
        }
        if ((forceParentCheck || rectIsVisible) && (this.mGroupFlags & 34) == 34) {
            rectIsVisible = rect.intersect((float) this.mPaddingLeft, (float) this.mPaddingTop, (float) (width - this.mPaddingRight), (float) (height - this.mPaddingBottom));
        }
        if ((forceParentCheck || rectIsVisible) && this.mClipBounds != null) {
            rectIsVisible = rect.intersect((float) this.mClipBounds.left, (float) this.mClipBounds.top, (float) this.mClipBounds.right, (float) this.mClipBounds.bottom);
        }
        r.set((int) Math.floor((double) rect.left), (int) Math.floor((double) rect.top), (int) Math.ceil((double) rect.right), (int) Math.ceil((double) rect.bottom));
        if ((!forceParentCheck && !rectIsVisible) || this.mParent == null) {
            return rectIsVisible;
        }
        if (this.mParent instanceof ViewGroup) {
            return ((ViewGroup) this.mParent).getChildVisibleRect(this, r, offset, forceParentCheck);
        }
        return this.mParent.getChildVisibleRect(this, r, offset);
    }

    public final void layout(int l, int t, int r, int b) {
        if (!this.mSuppressLayout) {
            LayoutTransition layoutTransition = this.mTransition;
            if (layoutTransition == null || !layoutTransition.isChangingLayout()) {
                layoutTransition = this.mTransition;
                if (layoutTransition != null) {
                    layoutTransition.layoutChange(this);
                }
                super.layout(l, t, r, b);
                return;
            }
        }
        this.mLayoutCalledWhileSuppressed = true;
    }

    /* Access modifiers changed, original: protected */
    public boolean canAnimate() {
        return this.mLayoutAnimationController != null;
    }

    public void startLayoutAnimation() {
        if (this.mLayoutAnimationController != null) {
            this.mGroupFlags |= 8;
            requestLayout();
        }
    }

    public void scheduleLayoutAnimation() {
        this.mGroupFlags |= 8;
    }

    public void setLayoutAnimation(LayoutAnimationController controller) {
        this.mLayoutAnimationController = controller;
        if (this.mLayoutAnimationController != null) {
            this.mGroupFlags |= 8;
        }
    }

    public LayoutAnimationController getLayoutAnimation() {
        return this.mLayoutAnimationController;
    }

    @Deprecated
    public boolean isAnimationCacheEnabled() {
        return (this.mGroupFlags & 64) == 64;
    }

    @Deprecated
    public void setAnimationCacheEnabled(boolean enabled) {
        setBooleanFlag(64, enabled);
    }

    @Deprecated
    public boolean isAlwaysDrawnWithCacheEnabled() {
        return (this.mGroupFlags & 16384) == 16384;
    }

    @Deprecated
    public void setAlwaysDrawnWithCacheEnabled(boolean always) {
        setBooleanFlag(16384, always);
    }

    /* Access modifiers changed, original: protected */
    @Deprecated
    public boolean isChildrenDrawnWithCacheEnabled() {
        return (this.mGroupFlags & 32768) == 32768;
    }

    /* Access modifiers changed, original: protected */
    @Deprecated
    public void setChildrenDrawnWithCacheEnabled(boolean enabled) {
        setBooleanFlag(32768, enabled);
    }

    /* Access modifiers changed, original: protected */
    @ExportedProperty(category = "drawing")
    public boolean isChildrenDrawingOrderEnabled() {
        return (this.mGroupFlags & 1024) == 1024;
    }

    /* Access modifiers changed, original: protected */
    public void setChildrenDrawingOrderEnabled(boolean enabled) {
        setBooleanFlag(1024, enabled);
    }

    private boolean hasBooleanFlag(int flag) {
        return (this.mGroupFlags & flag) == flag;
    }

    private void setBooleanFlag(int flag, boolean value) {
        if (value) {
            this.mGroupFlags |= flag;
        } else {
            this.mGroupFlags &= ~flag;
        }
    }

    @ExportedProperty(category = "drawing", mapping = {@IntToString(from = 0, to = "NONE"), @IntToString(from = 1, to = "ANIMATION"), @IntToString(from = 2, to = "SCROLLING"), @IntToString(from = 3, to = "ALL")})
    @Deprecated
    public int getPersistentDrawingCache() {
        return this.mPersistentDrawingCache;
    }

    @Deprecated
    public void setPersistentDrawingCache(int drawingCacheToKeep) {
        this.mPersistentDrawingCache = drawingCacheToKeep & 3;
    }

    private void setLayoutMode(int layoutMode, boolean explicitly) {
        this.mLayoutMode = layoutMode;
        setBooleanFlag(8388608, explicitly);
    }

    /* Access modifiers changed, original: 0000 */
    public void invalidateInheritedLayoutMode(int layoutModeOfRoot) {
        int i = this.mLayoutMode;
        if (i != -1 && i != layoutModeOfRoot && !hasBooleanFlag(8388608)) {
            setLayoutMode(-1, false);
            int N = getChildCount();
            for (i = 0; i < N; i++) {
                getChildAt(i).invalidateInheritedLayoutMode(layoutModeOfRoot);
            }
        }
    }

    public int getLayoutMode() {
        if (this.mLayoutMode == -1) {
            setLayoutMode(this.mParent instanceof ViewGroup ? ((ViewGroup) this.mParent).getLayoutMode() : LAYOUT_MODE_DEFAULT, false);
        }
        return this.mLayoutMode;
    }

    public void setLayoutMode(int layoutMode) {
        if (this.mLayoutMode != layoutMode) {
            invalidateInheritedLayoutMode(layoutMode);
            setLayoutMode(layoutMode, layoutMode != -1);
            requestLayout();
        }
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* Access modifiers changed, original: protected */
    public LayoutParams generateLayoutParams(LayoutParams p) {
        return p;
    }

    /* Access modifiers changed, original: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    /* Access modifiers changed, original: protected */
    public void debug(int depth) {
        String output;
        StringBuilder stringBuilder;
        super.debug(depth);
        String str = "View";
        if (this.mFocused != null) {
            output = View.debugIndent(depth);
            stringBuilder = new StringBuilder();
            stringBuilder.append(output);
            stringBuilder.append("mFocused");
            Log.d(str, stringBuilder.toString());
            this.mFocused.debug(depth + 1);
        }
        if (this.mDefaultFocus != null) {
            output = View.debugIndent(depth);
            stringBuilder = new StringBuilder();
            stringBuilder.append(output);
            stringBuilder.append("mDefaultFocus");
            Log.d(str, stringBuilder.toString());
            this.mDefaultFocus.debug(depth + 1);
        }
        if (this.mFocusedInCluster != null) {
            output = View.debugIndent(depth);
            stringBuilder = new StringBuilder();
            stringBuilder.append(output);
            stringBuilder.append("mFocusedInCluster");
            Log.d(str, stringBuilder.toString());
            this.mFocusedInCluster.debug(depth + 1);
        }
        if (this.mChildrenCount != 0) {
            output = View.debugIndent(depth);
            stringBuilder = new StringBuilder();
            stringBuilder.append(output);
            stringBuilder.append("{");
            Log.d(str, stringBuilder.toString());
        }
        int count = this.mChildrenCount;
        for (int i = 0; i < count; i++) {
            this.mChildren[i].debug(depth + 1);
        }
        if (this.mChildrenCount != 0) {
            String output2 = View.debugIndent(depth);
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(output2);
            stringBuilder2.append("}");
            Log.d(str, stringBuilder2.toString());
        }
    }

    public int indexOfChild(View child) {
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            if (children[i] == child) {
                return i;
            }
        }
        return -1;
    }

    public int getChildCount() {
        return this.mChildrenCount;
    }

    public View getChildAt(int index) {
        if (index < 0 || index >= this.mChildrenCount) {
            return null;
        }
        return this.mChildren[index];
    }

    /* Access modifiers changed, original: protected */
    public void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
        int size = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < size; i++) {
            View child = children[i];
            if ((child.mViewFlags & 12) != 8) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        LayoutParams lp = child.getLayoutParams();
        child.measure(getChildMeasureSpec(parentWidthMeasureSpec, this.mPaddingLeft + this.mPaddingRight, lp.width), getChildMeasureSpec(parentHeightMeasureSpec, this.mPaddingTop + this.mPaddingBottom, lp.height));
    }

    /* Access modifiers changed, original: protected */
    public void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        child.measure(getChildMeasureSpec(parentWidthMeasureSpec, (((this.mPaddingLeft + this.mPaddingRight) + lp.leftMargin) + lp.rightMargin) + widthUsed, lp.width), getChildMeasureSpec(parentHeightMeasureSpec, (((this.mPaddingTop + this.mPaddingBottom) + lp.topMargin) + lp.bottomMargin) + heightUsed, lp.height));
    }

    public static int getChildMeasureSpec(int spec, int padding, int childDimension) {
        int specMode = MeasureSpec.getMode(spec);
        int i = 0;
        int size = Math.max(0, MeasureSpec.getSize(spec) - padding);
        int resultSize = 0;
        int resultMode = 0;
        if (specMode != Integer.MIN_VALUE) {
            if (specMode != 0) {
                if (specMode == 1073741824) {
                    if (childDimension >= 0) {
                        resultSize = childDimension;
                        resultMode = 1073741824;
                    } else if (childDimension == -1) {
                        resultSize = size;
                        resultMode = 1073741824;
                    } else if (childDimension == -2) {
                        resultSize = size;
                        resultMode = Integer.MIN_VALUE;
                    }
                }
            } else if (childDimension >= 0) {
                resultSize = childDimension;
                resultMode = 1073741824;
            } else if (childDimension == -1) {
                if (!View.sUseZeroUnspecifiedMeasureSpec) {
                    i = size;
                }
                resultSize = i;
                resultMode = 0;
            } else if (childDimension == -2) {
                if (!View.sUseZeroUnspecifiedMeasureSpec) {
                    i = size;
                }
                resultSize = i;
                resultMode = 0;
            }
        } else if (childDimension >= 0) {
            resultSize = childDimension;
            resultMode = 1073741824;
        } else if (childDimension == -1) {
            resultSize = size;
            resultMode = Integer.MIN_VALUE;
        } else if (childDimension == -2) {
            resultSize = size;
            resultMode = Integer.MIN_VALUE;
        }
        return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
    }

    public void clearDisappearingChildren() {
        ArrayList<View> disappearingChildren = this.mDisappearingChildren;
        if (disappearingChildren != null) {
            int count = disappearingChildren.size();
            for (int i = 0; i < count; i++) {
                View view = (View) disappearingChildren.get(i);
                if (view.mAttachInfo != null) {
                    view.dispatchDetachedFromWindow();
                }
                view.clearAnimation();
            }
            disappearingChildren.clear();
            invalidate();
        }
    }

    private void addDisappearingView(View v) {
        ArrayList<View> disappearingChildren = this.mDisappearingChildren;
        if (disappearingChildren == null) {
            ArrayList<View> arrayList = new ArrayList();
            this.mDisappearingChildren = arrayList;
            disappearingChildren = arrayList;
        }
        disappearingChildren.add(v);
    }

    /* Access modifiers changed, original: 0000 */
    public void finishAnimatingView(View view, Animation animation) {
        ArrayList<View> disappearingChildren = this.mDisappearingChildren;
        if (disappearingChildren != null && disappearingChildren.contains(view)) {
            disappearingChildren.remove(view);
            if (view.mAttachInfo != null) {
                view.dispatchDetachedFromWindow();
            }
            view.clearAnimation();
            this.mGroupFlags |= 4;
        }
        if (!(animation == null || animation.getFillAfter())) {
            view.clearAnimation();
        }
        if ((view.mPrivateFlags & 65536) == 65536) {
            view.onAnimationEnd();
            view.mPrivateFlags &= -65537;
            this.mGroupFlags |= 4;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isViewTransitioning(View view) {
        ArrayList arrayList = this.mTransitioningViews;
        return arrayList != null && arrayList.contains(view);
    }

    public void startViewTransition(View view) {
        if (view.mParent == this) {
            if (this.mTransitioningViews == null) {
                this.mTransitioningViews = new ArrayList();
            }
            this.mTransitioningViews.add(view);
        }
    }

    public void endViewTransition(View view) {
        ArrayList arrayList = this.mTransitioningViews;
        if (arrayList != null) {
            arrayList.remove(view);
            ArrayList<View> disappearingChildren = this.mDisappearingChildren;
            if (disappearingChildren != null && disappearingChildren.contains(view)) {
                disappearingChildren.remove(view);
                ArrayList arrayList2 = this.mVisibilityChangingChildren;
                if (arrayList2 == null || !arrayList2.contains(view)) {
                    if (view.mAttachInfo != null) {
                        view.dispatchDetachedFromWindow();
                    }
                    if (view.mParent != null) {
                        view.mParent = null;
                    }
                } else {
                    this.mVisibilityChangingChildren.remove(view);
                }
                invalidate();
            }
        }
    }

    public void suppressLayout(boolean suppress) {
        this.mSuppressLayout = suppress;
        if (!suppress && this.mLayoutCalledWhileSuppressed) {
            requestLayout();
            this.mLayoutCalledWhileSuppressed = false;
        }
    }

    public boolean isLayoutSuppressed() {
        return this.mSuppressLayout;
    }

    public boolean gatherTransparentRegion(Region region) {
        boolean z = false;
        boolean meOpaque = (this.mPrivateFlags & 512) == 0;
        if (meOpaque && region == null) {
            return true;
        }
        super.gatherTransparentRegion(region);
        int childrenCount = this.mChildrenCount;
        boolean noneOfTheChildrenAreTransparent = true;
        if (childrenCount > 0) {
            ArrayList<View> preorderedList = buildOrderedChildList();
            boolean customOrder = preorderedList == null && isChildrenDrawingOrderEnabled();
            View[] children = this.mChildren;
            for (int i = 0; i < childrenCount; i++) {
                View child = getAndVerifyPreorderedView(preorderedList, children, getAndVerifyPreorderedIndex(childrenCount, i, customOrder));
                if (((child.mViewFlags & 12) == 0 || child.getAnimation() != null) && !child.gatherTransparentRegion(region)) {
                    noneOfTheChildrenAreTransparent = false;
                }
            }
            if (preorderedList != null) {
                preorderedList.clear();
            }
        }
        if (meOpaque || noneOfTheChildrenAreTransparent) {
            z = true;
        }
        return z;
    }

    public void requestTransparentRegion(View child) {
        if (child != null) {
            child.mPrivateFlags |= 512;
            if (this.mParent != null) {
                this.mParent.requestTransparentRegion(this);
            }
        }
    }

    public void subtractObscuredTouchableRegion(Region touchableRegion, View view) {
        int childrenCount = this.mChildrenCount;
        ArrayList<View> preorderedList = buildTouchDispatchChildList();
        boolean customOrder = preorderedList == null && isChildrenDrawingOrderEnabled();
        View[] children = this.mChildren;
        for (int i = childrenCount - 1; i >= 0; i--) {
            View child = getAndVerifyPreorderedView(preorderedList, children, getAndVerifyPreorderedIndex(childrenCount, i, customOrder));
            if (child == view) {
                break;
            }
            if (child.canReceivePointerEvents()) {
                applyOpToRegionByBounds(touchableRegion, child, Op.DIFFERENCE);
            }
        }
        applyOpToRegionByBounds(touchableRegion, this, Op.INTERSECT);
        ViewParent parent = getParent();
        if (parent != null) {
            parent.subtractObscuredTouchableRegion(touchableRegion, this);
        }
    }

    private static void applyOpToRegionByBounds(Region region, View view, Op op) {
        int[] locationInWindow = new int[2];
        view.getLocationInWindow(locationInWindow);
        int x = locationInWindow[0];
        int y = locationInWindow[1];
        region.op(x, y, x + view.getWidth(), y + view.getHeight(), op);
    }

    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
        insets = super.dispatchApplyWindowInsets(insets);
        if (View.sBrokenInsetsDispatch) {
            return brokenDispatchApplyWindowInsets(insets);
        }
        return newDispatchApplyWindowInsets(insets);
    }

    private WindowInsets brokenDispatchApplyWindowInsets(WindowInsets insets) {
        if (!insets.isConsumed()) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                insets = getChildAt(i).dispatchApplyWindowInsets(insets);
                if (insets.isConsumed()) {
                    break;
                }
            }
        }
        return insets;
    }

    private WindowInsets newDispatchApplyWindowInsets(WindowInsets insets) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).dispatchApplyWindowInsets(insets);
        }
        return insets;
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchWindowInsetsAnimationStarted(InsetsAnimation animation) {
        super.dispatchWindowInsetsAnimationStarted(animation);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).dispatchWindowInsetsAnimationStarted(animation);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public WindowInsets dispatchWindowInsetsAnimationProgress(WindowInsets insets) {
        insets = super.dispatchWindowInsetsAnimationProgress(insets);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).dispatchWindowInsetsAnimationProgress(insets);
        }
        return insets;
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchWindowInsetsAnimationFinished(InsetsAnimation animation) {
        super.dispatchWindowInsetsAnimationFinished(animation);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).dispatchWindowInsetsAnimationFinished(animation);
        }
    }

    public AnimationListener getLayoutAnimationListener() {
        return this.mAnimationListener;
    }

    /* Access modifiers changed, original: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        int i = this.mGroupFlags;
        if ((65536 & i) == 0) {
            return;
        }
        if ((i & 8192) == 0) {
            View[] children = this.mChildren;
            int count = this.mChildrenCount;
            for (int i2 = 0; i2 < count; i2++) {
                View child = children[i2];
                if ((child.mViewFlags & 4194304) != 0) {
                    child.refreshDrawableState();
                }
            }
            return;
        }
        throw new IllegalStateException("addStateFromChildren cannot be enabled if a child has duplicateParentState set to true");
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        View[] children = this.mChildren;
        int count = this.mChildrenCount;
        for (int i = 0; i < count; i++) {
            children[i].jumpDrawablesToCurrentState();
        }
    }

    /* Access modifiers changed, original: protected */
    public int[] onCreateDrawableState(int extraSpace) {
        if ((this.mGroupFlags & 8192) == 0) {
            return super.onCreateDrawableState(extraSpace);
        }
        int need = 0;
        int n = getChildCount();
        for (int i = 0; i < n; i++) {
            int[] childState = getChildAt(i).getDrawableState();
            if (childState != null) {
                need += childState.length;
            }
        }
        int[] state = super.onCreateDrawableState(extraSpace + need);
        for (int i2 = 0; i2 < n; i2++) {
            int[] childState2 = getChildAt(i2).getDrawableState();
            if (childState2 != null) {
                state = View.mergeDrawableStates(state, childState2);
            }
        }
        return state;
    }

    public void setAddStatesFromChildren(boolean addsStates) {
        if (addsStates) {
            this.mGroupFlags |= 8192;
        } else {
            this.mGroupFlags &= -8193;
        }
        refreshDrawableState();
    }

    public boolean addStatesFromChildren() {
        return (this.mGroupFlags & 8192) != 0;
    }

    public void childDrawableStateChanged(View child) {
        if ((this.mGroupFlags & 8192) != 0) {
            refreshDrawableState();
        }
    }

    public void setLayoutAnimationListener(AnimationListener animationListener) {
        this.mAnimationListener = animationListener;
    }

    public void requestTransitionStart(LayoutTransition transition) {
        ViewRootImpl viewAncestor = getViewRootImpl();
        if (viewAncestor != null) {
            viewAncestor.requestTransitionStart(transition);
        }
    }

    public boolean resolveRtlPropertiesIfNeeded() {
        boolean result = super.resolveRtlPropertiesIfNeeded();
        if (result) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.isLayoutDirectionInherited()) {
                    child.resolveRtlPropertiesIfNeeded();
                }
            }
        }
        return result;
    }

    public boolean resolveLayoutDirection() {
        if (Extension.get().getExtension() != null) {
            return ((Interface) Extension.get().getExtension().asInterface()).resolveLayoutDirection(this);
        }
        return originalResolveLayoutDirection();
    }

    /* Access modifiers changed, original: 0000 */
    public boolean originalResolveLayoutDirection() {
        boolean result = super.resolveLayoutDirection();
        if (result) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.isLayoutDirectionInherited()) {
                    child.resolveLayoutDirection();
                }
            }
        }
        return result;
    }

    public boolean resolveTextDirection() {
        boolean result = super.resolveTextDirection();
        if (result) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.isTextDirectionInherited()) {
                    child.resolveTextDirection();
                }
            }
        }
        return result;
    }

    public boolean resolveTextAlignment() {
        boolean result = super.resolveTextAlignment();
        if (result) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.isTextAlignmentInherited()) {
                    child.resolveTextAlignment();
                }
            }
        }
        return result;
    }

    @UnsupportedAppUsage
    public void resolvePadding() {
        super.resolvePadding();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.isLayoutDirectionInherited() && !child.isPaddingResolved()) {
                child.resolvePadding();
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void resolveDrawables() {
        super.resolveDrawables();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.isLayoutDirectionInherited() && !child.areDrawablesResolved()) {
                child.resolveDrawables();
            }
        }
    }

    public void resolveLayoutParams() {
        super.resolveLayoutParams();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).resolveLayoutParams();
        }
    }

    public void resetResolvedLayoutDirection() {
        super.resetResolvedLayoutDirection();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.isLayoutDirectionInherited()) {
                child.resetResolvedLayoutDirection();
            }
        }
    }

    public void resetResolvedTextDirection() {
        super.resetResolvedTextDirection();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.isTextDirectionInherited()) {
                child.resetResolvedTextDirection();
            }
        }
    }

    public void resetResolvedTextAlignment() {
        super.resetResolvedTextAlignment();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.isTextAlignmentInherited()) {
                child.resetResolvedTextAlignment();
            }
        }
    }

    public void resetResolvedPadding() {
        super.resetResolvedPadding();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.isLayoutDirectionInherited()) {
                child.resetResolvedPadding();
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void resetResolvedDrawables() {
        super.resetResolvedDrawables();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.isLayoutDirectionInherited()) {
                child.resetResolvedDrawables();
            }
        }
    }

    public boolean shouldDelayChildPressedState() {
        return true;
    }

    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return false;
    }

    public void onNestedScrollAccepted(View child, View target, int axes) {
        this.mNestedScrollAxes = axes;
    }

    public void onStopNestedScroll(View child) {
        stopNestedScroll();
        this.mNestedScrollAxes = 0;
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, null);
    }

    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        dispatchNestedPreScroll(dx, dy, consumed, null);
    }

    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    public int getNestedScrollAxes() {
        return this.mNestedScrollAxes;
    }

    /* Access modifiers changed, original: protected */
    public void onSetLayoutParams(View child, LayoutParams layoutParams) {
        requestLayout();
    }

    public void captureTransitioningViews(List<View> transitioningViews) {
        if (getVisibility() == 0) {
            if (isTransitionGroup()) {
                transitioningViews.add(this);
            } else {
                int count = getChildCount();
                for (int i = 0; i < count; i++) {
                    getChildAt(i).captureTransitioningViews(transitioningViews);
                }
            }
        }
    }

    public void findNamedViews(Map<String, View> namedElements) {
        if (getVisibility() == 0 || this.mGhostView != null) {
            super.findNamedViews(namedElements);
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                getChildAt(i).findNamedViews(namedElements);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean hasUnhandledKeyListener() {
        return this.mChildUnhandledKeyListeners > 0 || super.hasUnhandledKeyListener();
    }

    /* Access modifiers changed, original: 0000 */
    public void incrementChildUnhandledKeyListeners() {
        this.mChildUnhandledKeyListeners++;
        if (this.mChildUnhandledKeyListeners == 1 && (this.mParent instanceof ViewGroup)) {
            ((ViewGroup) this.mParent).incrementChildUnhandledKeyListeners();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void decrementChildUnhandledKeyListeners() {
        this.mChildUnhandledKeyListeners--;
        if (this.mChildUnhandledKeyListeners == 0 && (this.mParent instanceof ViewGroup)) {
            ((ViewGroup) this.mParent).decrementChildUnhandledKeyListeners();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public View dispatchUnhandledKeyEvent(KeyEvent evt) {
        if (!hasUnhandledKeyListener()) {
            return null;
        }
        ArrayList<View> orderedViews = buildOrderedChildList();
        int i;
        View consumer;
        if (orderedViews != null) {
            try {
                for (i = orderedViews.size() - 1; i >= 0; i--) {
                    consumer = ((View) orderedViews.get(i)).dispatchUnhandledKeyEvent(evt);
                    if (consumer != null) {
                        return consumer;
                    }
                }
                orderedViews.clear();
            } finally {
                orderedViews.clear();
            }
        } else {
            for (i = getChildCount() - 1; i >= 0; i--) {
                consumer = getChildAt(i).dispatchUnhandledKeyEvent(evt);
                if (consumer != null) {
                    return consumer;
                }
            }
        }
        if (onUnhandledKeyEvent(evt)) {
            return this;
        }
        return null;
    }

    private static void drawRect(Canvas canvas, Paint paint, int x1, int y1, int x2, int y2) {
        if (sDebugLines == null) {
            sDebugLines = new float[16];
        }
        float[] fArr = sDebugLines;
        fArr[0] = (float) x1;
        fArr[1] = (float) y1;
        fArr[2] = (float) x2;
        fArr[3] = (float) y1;
        fArr[4] = (float) x2;
        fArr[5] = (float) y1;
        fArr[6] = (float) x2;
        fArr[7] = (float) y2;
        fArr[8] = (float) x2;
        fArr[9] = (float) y2;
        fArr[10] = (float) x1;
        fArr[11] = (float) y2;
        fArr[12] = (float) x1;
        fArr[13] = (float) y2;
        fArr[14] = (float) x1;
        fArr[15] = (float) y1;
        canvas.drawLines(fArr, paint);
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public void encodeProperties(ViewHierarchyEncoder encoder) {
        super.encodeProperties(encoder);
        encoder.addProperty("focus:descendantFocusability", getDescendantFocusability());
        encoder.addProperty("drawing:clipChildren", getClipChildren());
        encoder.addProperty("drawing:clipToPadding", getClipToPadding());
        encoder.addProperty("drawing:childrenDrawingOrderEnabled", isChildrenDrawingOrderEnabled());
        encoder.addProperty("drawing:persistentDrawingCache", getPersistentDrawingCache());
        int n = getChildCount();
        encoder.addProperty("meta:__childCount__", (short) n);
        for (int i = 0; i < n; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("meta:__child__");
            stringBuilder.append(i);
            encoder.addPropertyKey(stringBuilder.toString());
            getChildAt(i).encode(encoder);
        }
    }
}
