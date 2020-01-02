package android.view;

import android.animation.StateListAnimator;
import android.annotation.UnsupportedAppUsage;
import android.app.Activity;
import android.app.slice.Slice;
import android.content.AutofillOptions;
import android.content.ClipData;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.Interpolator;
import android.graphics.Interpolator.Result;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Region.Op;
import android.graphics.RenderNode;
import android.graphics.RenderNode.PositionUpdateListener;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.hardware.display.DisplayManagerGlobal;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.BatteryStats.HistoryItem;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Trace;
import android.provider.MiuiSettings.System;
import android.provider.Telephony.BaseMmsColumns;
import android.provider.UserDictionary.Words;
import android.sysprop.DisplayProperties;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.FloatProperty;
import android.util.Log;
import android.util.LongSparseLongArray;
import android.util.Pools.SynchronizedPool;
import android.util.Property;
import android.util.SeempLog;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.StateSet;
import android.util.StatsLogInternal;
import android.util.SuperNotCalledException;
import android.util.TypedValue;
import android.view.AccessibilityIterators.TextSegmentIterator;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.DisplayCutout.ParcelableWrapper;
import android.view.KeyEvent.DispatcherState;
import android.view.ViewDebug.CanvasProvider;
import android.view.ViewDebug.CapturedViewProperty;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewDebug.FlagToString;
import android.view.ViewDebug.IntToString;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.InternalInsetsInfo;
import android.view.Window.OnFrameMetricsAvailableListener;
import android.view.WindowInsetsAnimationListener.InsetsAnimation;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityEventSource;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeIdManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.accessibility.AccessibilityNodeInfo.TouchDelegateInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillManager;
import android.view.autofill.AutofillValue;
import android.view.contentcapture.ContentCaptureManager;
import android.view.contentcapture.ContentCaptureSession;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.view.inspector.InspectionCompanion.UninitializedPropertyMapException;
import android.view.inspector.IntFlagMapping;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import android.widget.Checkable;
import android.widget.ScrollBarDrawable;
import com.android.internal.R;
import com.android.internal.view.TooltipPopup;
import com.android.internal.view.menu.MenuBuilder;
import com.android.internal.widget.ScrollBarUtils;
import com.google.android.collect.Lists;
import com.google.android.collect.Maps;
import com.miui.internal.contentcatcher.IInterceptor;
import com.miui.internal.variable.api.v29.Android_View_View.Extension;
import com.miui.internal.variable.api.v29.Android_View_View.Interface;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import miui.contentcatcher.InterceptorProxy;
import org.apache.miui.commons.lang3.ClassUtils;

public class View implements Callback, KeyEvent.Callback, AccessibilityEventSource {
    public static final int ACCESSIBILITY_CURSOR_POSITION_UNDEFINED = -1;
    public static final int ACCESSIBILITY_LIVE_REGION_ASSERTIVE = 2;
    static final int ACCESSIBILITY_LIVE_REGION_DEFAULT = 0;
    public static final int ACCESSIBILITY_LIVE_REGION_NONE = 0;
    public static final int ACCESSIBILITY_LIVE_REGION_POLITE = 1;
    static final int ALL_RTL_PROPERTIES_RESOLVED = 1610678816;
    public static final Property<View, Float> ALPHA = new FloatProperty<View>("alpha") {
        public void setValue(View object, float value) {
            object.setAlpha(value);
        }

        public Float get(View object) {
            return Float.valueOf(object.getAlpha());
        }
    };
    public static final int AUTOFILL_FLAG_INCLUDE_NOT_IMPORTANT_VIEWS = 1;
    private static final int[] AUTOFILL_HIGHLIGHT_ATTR = new int[]{16844136};
    public static final String AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DATE = "creditCardExpirationDate";
    public static final String AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DAY = "creditCardExpirationDay";
    public static final String AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_MONTH = "creditCardExpirationMonth";
    public static final String AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_YEAR = "creditCardExpirationYear";
    public static final String AUTOFILL_HINT_CREDIT_CARD_NUMBER = "creditCardNumber";
    public static final String AUTOFILL_HINT_CREDIT_CARD_SECURITY_CODE = "creditCardSecurityCode";
    public static final String AUTOFILL_HINT_EMAIL_ADDRESS = "emailAddress";
    public static final String AUTOFILL_HINT_NAME = "name";
    public static final String AUTOFILL_HINT_PASSWORD = "password";
    public static final String AUTOFILL_HINT_PHONE = "phone";
    public static final String AUTOFILL_HINT_POSTAL_ADDRESS = "postalAddress";
    public static final String AUTOFILL_HINT_POSTAL_CODE = "postalCode";
    public static final String AUTOFILL_HINT_USERNAME = "username";
    private static final String AUTOFILL_LOG_TAG = "View.Autofill";
    public static final int AUTOFILL_TYPE_DATE = 4;
    public static final int AUTOFILL_TYPE_LIST = 3;
    public static final int AUTOFILL_TYPE_NONE = 0;
    public static final int AUTOFILL_TYPE_TEXT = 1;
    public static final int AUTOFILL_TYPE_TOGGLE = 2;
    static final int CLICKABLE = 16384;
    private static final String CONTENT_CAPTURE_LOG_TAG = "View.ContentCapture";
    static final int CONTEXT_CLICKABLE = 8388608;
    @UnsupportedAppUsage
    private static final boolean DBG = false;
    private static final boolean DEBUG_CONTENT_CAPTURE = false;
    static final int DEBUG_CORNERS_COLOR = Color.rgb(63, 127, 255);
    static final int DEBUG_CORNERS_SIZE_DIP = 8;
    public static boolean DEBUG_DRAW = false;
    static final int DISABLED = 32;
    public static final int DRAG_FLAG_GLOBAL = 256;
    public static final int DRAG_FLAG_GLOBAL_PERSISTABLE_URI_PERMISSION = 64;
    public static final int DRAG_FLAG_GLOBAL_PREFIX_URI_PERMISSION = 128;
    public static final int DRAG_FLAG_GLOBAL_URI_READ = 1;
    public static final int DRAG_FLAG_GLOBAL_URI_WRITE = 2;
    public static final int DRAG_FLAG_OPAQUE = 512;
    static final int DRAG_MASK = 3;
    static final int DRAWING_CACHE_ENABLED = 32768;
    @Deprecated
    public static final int DRAWING_CACHE_QUALITY_AUTO = 0;
    private static final int[] DRAWING_CACHE_QUALITY_FLAGS = new int[]{0, 524288, 1048576};
    @Deprecated
    public static final int DRAWING_CACHE_QUALITY_HIGH = 1048576;
    @Deprecated
    public static final int DRAWING_CACHE_QUALITY_LOW = 524288;
    static final int DRAWING_CACHE_QUALITY_MASK = 1572864;
    static final int DRAW_MASK = 128;
    static final int DUPLICATE_PARENT_STATE = 4194304;
    protected static final int[] EMPTY_STATE_SET = StateSet.get(0);
    static final int ENABLED = 0;
    protected static final int[] ENABLED_FOCUSED_SELECTED_STATE_SET = StateSet.get(14);
    protected static final int[] ENABLED_FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(15);
    protected static final int[] ENABLED_FOCUSED_STATE_SET = StateSet.get(12);
    protected static final int[] ENABLED_FOCUSED_WINDOW_FOCUSED_STATE_SET = StateSet.get(13);
    static final int ENABLED_MASK = 32;
    protected static final int[] ENABLED_SELECTED_STATE_SET = StateSet.get(10);
    protected static final int[] ENABLED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(11);
    protected static final int[] ENABLED_STATE_SET = StateSet.get(8);
    protected static final int[] ENABLED_WINDOW_FOCUSED_STATE_SET = StateSet.get(9);
    static final int FADING_EDGE_HORIZONTAL = 4096;
    static final int FADING_EDGE_MASK = 12288;
    static final int FADING_EDGE_NONE = 0;
    static final int FADING_EDGE_VERTICAL = 8192;
    static final int FILTER_TOUCHES_WHEN_OBSCURED = 1024;
    public static final int FIND_VIEWS_WITH_ACCESSIBILITY_NODE_PROVIDERS = 4;
    public static final int FIND_VIEWS_WITH_CONTENT_DESCRIPTION = 2;
    public static final int FIND_VIEWS_WITH_TEXT = 1;
    private static final int FITS_SYSTEM_WINDOWS = 2;
    public static final int FOCUSABLE = 1;
    public static final int FOCUSABLES_ALL = 0;
    public static final int FOCUSABLES_TOUCH_MODE = 1;
    public static final int FOCUSABLE_AUTO = 16;
    static final int FOCUSABLE_IN_TOUCH_MODE = 262144;
    private static final int FOCUSABLE_MASK = 17;
    protected static final int[] FOCUSED_SELECTED_STATE_SET = StateSet.get(6);
    protected static final int[] FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(7);
    protected static final int[] FOCUSED_STATE_SET = StateSet.get(4);
    protected static final int[] FOCUSED_WINDOW_FOCUSED_STATE_SET = StateSet.get(5);
    public static final int FOCUS_BACKWARD = 1;
    public static final int FOCUS_DOWN = 130;
    public static final int FOCUS_FORWARD = 2;
    public static final int FOCUS_LEFT = 17;
    public static final int FOCUS_RIGHT = 66;
    public static final int FOCUS_UP = 33;
    public static final int GONE = 8;
    public static final int HAPTIC_FEEDBACK_ENABLED = 268435456;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_AUTO = 0;
    static final int IMPORTANT_FOR_ACCESSIBILITY_DEFAULT = 0;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_NO = 2;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS = 4;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_YES = 1;
    public static final int IMPORTANT_FOR_AUTOFILL_AUTO = 0;
    public static final int IMPORTANT_FOR_AUTOFILL_NO = 2;
    public static final int IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS = 8;
    public static final int IMPORTANT_FOR_AUTOFILL_YES = 1;
    public static final int IMPORTANT_FOR_AUTOFILL_YES_EXCLUDE_DESCENDANTS = 4;
    public static final int INVISIBLE = 4;
    public static final int KEEP_SCREEN_ON = 67108864;
    public static final int LAST_APP_AUTOFILL_ID = 1073741823;
    public static final int LAYER_TYPE_HARDWARE = 2;
    public static final int LAYER_TYPE_NONE = 0;
    public static final int LAYER_TYPE_SOFTWARE = 1;
    private static final int LAYOUT_DIRECTION_DEFAULT = 2;
    private static final int[] LAYOUT_DIRECTION_FLAGS = new int[]{0, 1, 2, 3};
    public static final int LAYOUT_DIRECTION_INHERIT = 2;
    public static final int LAYOUT_DIRECTION_LOCALE = 3;
    public static final int LAYOUT_DIRECTION_LTR = 0;
    static final int LAYOUT_DIRECTION_RESOLVED_DEFAULT = 0;
    public static final int LAYOUT_DIRECTION_RTL = 1;
    public static final int LAYOUT_DIRECTION_UNDEFINED = -1;
    static final int LONG_CLICKABLE = 2097152;
    public static final int MEASURED_HEIGHT_STATE_SHIFT = 16;
    public static final int MEASURED_SIZE_MASK = 16777215;
    public static final int MEASURED_STATE_MASK = -16777216;
    public static final int MEASURED_STATE_TOO_SMALL = 16777216;
    @UnsupportedAppUsage
    public static final int NAVIGATION_BAR_TRANSIENT = 134217728;
    public static final int NAVIGATION_BAR_TRANSLUCENT = Integer.MIN_VALUE;
    public static final int NAVIGATION_BAR_TRANSPARENT = 32768;
    public static final int NAVIGATION_BAR_UNHIDE = 536870912;
    public static final int NOT_FOCUSABLE = 0;
    public static final int NO_ID = -1;
    static final int OPTIONAL_FITS_SYSTEM_WINDOWS = 2048;
    public static final int OVER_SCROLL_ALWAYS = 0;
    public static final int OVER_SCROLL_IF_CONTENT_SCROLLS = 1;
    public static final int OVER_SCROLL_NEVER = 2;
    static final int PARENT_SAVE_DISABLED = 536870912;
    static final int PARENT_SAVE_DISABLED_MASK = 536870912;
    static final int PFLAG2_ACCESSIBILITY_FOCUSED = 67108864;
    static final int PFLAG2_ACCESSIBILITY_LIVE_REGION_MASK = 25165824;
    static final int PFLAG2_ACCESSIBILITY_LIVE_REGION_SHIFT = 23;
    static final int PFLAG2_DRAG_CAN_ACCEPT = 1;
    static final int PFLAG2_DRAG_HOVERED = 2;
    static final int PFLAG2_DRAWABLE_RESOLVED = 1073741824;
    static final int PFLAG2_HAS_TRANSIENT_STATE = Integer.MIN_VALUE;
    static final int PFLAG2_IMPORTANT_FOR_ACCESSIBILITY_MASK = 7340032;
    static final int PFLAG2_IMPORTANT_FOR_ACCESSIBILITY_SHIFT = 20;
    static final int PFLAG2_LAYOUT_DIRECTION_MASK = 12;
    static final int PFLAG2_LAYOUT_DIRECTION_MASK_SHIFT = 2;
    static final int PFLAG2_LAYOUT_DIRECTION_RESOLVED = 32;
    static final int PFLAG2_LAYOUT_DIRECTION_RESOLVED_MASK = 48;
    static final int PFLAG2_LAYOUT_DIRECTION_RESOLVED_RTL = 16;
    static final int PFLAG2_PADDING_RESOLVED = 536870912;
    static final int PFLAG2_SUBTREE_ACCESSIBILITY_STATE_CHANGED = 134217728;
    private static final int[] PFLAG2_TEXT_ALIGNMENT_FLAGS = new int[]{0, 8192, 16384, 24576, 32768, 40960, 49152};
    static final int PFLAG2_TEXT_ALIGNMENT_MASK = 57344;
    static final int PFLAG2_TEXT_ALIGNMENT_MASK_SHIFT = 13;
    static final int PFLAG2_TEXT_ALIGNMENT_RESOLVED = 65536;
    private static final int PFLAG2_TEXT_ALIGNMENT_RESOLVED_DEFAULT = 131072;
    static final int PFLAG2_TEXT_ALIGNMENT_RESOLVED_MASK = 917504;
    static final int PFLAG2_TEXT_ALIGNMENT_RESOLVED_MASK_SHIFT = 17;
    private static final int[] PFLAG2_TEXT_DIRECTION_FLAGS = new int[]{0, 64, 128, 192, 256, 320, 384, 448};
    static final int PFLAG2_TEXT_DIRECTION_MASK = 448;
    static final int PFLAG2_TEXT_DIRECTION_MASK_SHIFT = 6;
    static final int PFLAG2_TEXT_DIRECTION_RESOLVED = 512;
    static final int PFLAG2_TEXT_DIRECTION_RESOLVED_DEFAULT = 1024;
    static final int PFLAG2_TEXT_DIRECTION_RESOLVED_MASK = 7168;
    static final int PFLAG2_TEXT_DIRECTION_RESOLVED_MASK_SHIFT = 10;
    static final int PFLAG2_VIEW_QUICK_REJECTED = 268435456;
    private static final int PFLAG3_ACCESSIBILITY_HEADING = Integer.MIN_VALUE;
    private static final int PFLAG3_AGGREGATED_VISIBLE = 536870912;
    static final int PFLAG3_APPLYING_INSETS = 32;
    static final int PFLAG3_ASSIST_BLOCKED = 16384;
    private static final int PFLAG3_AUTOFILLID_EXPLICITLY_SET = 1073741824;
    static final int PFLAG3_CALLED_SUPER = 16;
    private static final int PFLAG3_CLUSTER = 32768;
    private static final int PFLAG3_FINGER_DOWN = 131072;
    static final int PFLAG3_FITTING_SYSTEM_WINDOWS = 64;
    private static final int PFLAG3_FOCUSED_BY_DEFAULT = 262144;
    private static final int PFLAG3_HAS_OVERLAPPING_RENDERING_FORCED = 16777216;
    static final int PFLAG3_IMPORTANT_FOR_AUTOFILL_MASK = 7864320;
    static final int PFLAG3_IMPORTANT_FOR_AUTOFILL_SHIFT = 19;
    private static final int PFLAG3_IS_AUTOFILLED = 65536;
    static final int PFLAG3_IS_LAID_OUT = 4;
    static final int PFLAG3_MEASURE_NEEDED_BEFORE_LAYOUT = 8;
    static final int PFLAG3_NESTED_SCROLLING_ENABLED = 128;
    static final int PFLAG3_NOTIFY_AUTOFILL_ENTER_ON_LAYOUT = 134217728;
    private static final int PFLAG3_NO_REVEAL_ON_FOCUS = 67108864;
    private static final int PFLAG3_OVERLAPPING_RENDERING_FORCED_VALUE = 8388608;
    private static final int PFLAG3_SCREEN_READER_FOCUSABLE = 268435456;
    static final int PFLAG3_SCROLL_INDICATOR_BOTTOM = 512;
    static final int PFLAG3_SCROLL_INDICATOR_END = 8192;
    static final int PFLAG3_SCROLL_INDICATOR_LEFT = 1024;
    static final int PFLAG3_SCROLL_INDICATOR_RIGHT = 2048;
    static final int PFLAG3_SCROLL_INDICATOR_START = 4096;
    static final int PFLAG3_SCROLL_INDICATOR_TOP = 256;
    static final int PFLAG3_TEMPORARY_DETACH = 33554432;
    static final int PFLAG3_VIEW_IS_ANIMATING_ALPHA = 2;
    static final int PFLAG3_VIEW_IS_ANIMATING_TRANSFORM = 1;
    static final int PFLAG_ACTIVATED = 1073741824;
    static final int PFLAG_ALPHA_SET = 262144;
    static final int PFLAG_ANIMATION_STARTED = 65536;
    private static final int PFLAG_AWAKEN_SCROLL_BARS_ON_ATTACH = 134217728;
    static final int PFLAG_CANCEL_NEXT_UP_EVENT = 67108864;
    static final int PFLAG_DIRTY = 2097152;
    static final int PFLAG_DIRTY_MASK = 2097152;
    static final int PFLAG_DRAWABLE_STATE_DIRTY = 1024;
    static final int PFLAG_DRAWING_CACHE_VALID = 32768;
    static final int PFLAG_DRAWN = 32;
    static final int PFLAG_DRAW_ANIMATION = 64;
    static final int PFLAG_FOCUSED = 2;
    static final int PFLAG_FORCE_LAYOUT = 4096;
    static final int PFLAG_HAS_BOUNDS = 16;
    private static final int PFLAG_HOVERED = 268435456;
    static final int PFLAG_INVALIDATED = Integer.MIN_VALUE;
    static final int PFLAG_IS_ROOT_NAMESPACE = 8;
    static final int PFLAG_LAYOUT_REQUIRED = 8192;
    static final int PFLAG_MEASURED_DIMENSION_SET = 2048;
    private static final int PFLAG_NOTIFY_AUTOFILL_MANAGER_ON_CLICK = 536870912;
    static final int PFLAG_OPAQUE_BACKGROUND = 8388608;
    static final int PFLAG_OPAQUE_MASK = 25165824;
    static final int PFLAG_OPAQUE_SCROLLBARS = 16777216;
    private static final int PFLAG_PREPRESSED = 33554432;
    private static final int PFLAG_PRESSED = 16384;
    static final int PFLAG_REQUEST_TRANSPARENT_REGIONS = 512;
    private static final int PFLAG_SAVE_STATE_CALLED = 131072;
    static final int PFLAG_SCROLL_CONTAINER = 524288;
    static final int PFLAG_SCROLL_CONTAINER_ADDED = 1048576;
    static final int PFLAG_SELECTED = 4;
    static final int PFLAG_SKIP_DRAW = 128;
    static final int PFLAG_WANTS_FOCUS = 1;
    private static final int POPULATING_ACCESSIBILITY_EVENT_TYPES = 172479;
    protected static final int[] PRESSED_ENABLED_FOCUSED_SELECTED_STATE_SET = StateSet.get(30);
    protected static final int[] PRESSED_ENABLED_FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(31);
    protected static final int[] PRESSED_ENABLED_FOCUSED_STATE_SET = StateSet.get(28);
    protected static final int[] PRESSED_ENABLED_FOCUSED_WINDOW_FOCUSED_STATE_SET = StateSet.get(29);
    protected static final int[] PRESSED_ENABLED_SELECTED_STATE_SET = StateSet.get(26);
    protected static final int[] PRESSED_ENABLED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(27);
    protected static final int[] PRESSED_ENABLED_STATE_SET = StateSet.get(24);
    protected static final int[] PRESSED_ENABLED_WINDOW_FOCUSED_STATE_SET = StateSet.get(25);
    protected static final int[] PRESSED_FOCUSED_SELECTED_STATE_SET = StateSet.get(22);
    protected static final int[] PRESSED_FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(23);
    protected static final int[] PRESSED_FOCUSED_STATE_SET = StateSet.get(20);
    protected static final int[] PRESSED_FOCUSED_WINDOW_FOCUSED_STATE_SET = StateSet.get(21);
    protected static final int[] PRESSED_SELECTED_STATE_SET = StateSet.get(18);
    protected static final int[] PRESSED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(19);
    protected static final int[] PRESSED_STATE_SET = StateSet.get(16);
    protected static final int[] PRESSED_WINDOW_FOCUSED_STATE_SET = StateSet.get(17);
    private static final int PROVIDER_BACKGROUND = 0;
    private static final int PROVIDER_BOUNDS = 2;
    private static final int PROVIDER_NONE = 1;
    private static final int PROVIDER_PADDED_BOUNDS = 3;
    public static final int PUBLIC_STATUS_BAR_VISIBILITY_MASK = 16375;
    public static final Property<View, Float> ROTATION = new FloatProperty<View>("rotation") {
        public void setValue(View object, float value) {
            object.setRotation(value);
        }

        public Float get(View object) {
            return Float.valueOf(object.getRotation());
        }
    };
    public static final Property<View, Float> ROTATION_X = new FloatProperty<View>("rotationX") {
        public void setValue(View object, float value) {
            object.setRotationX(value);
        }

        public Float get(View object) {
            return Float.valueOf(object.getRotationX());
        }
    };
    public static final Property<View, Float> ROTATION_Y = new FloatProperty<View>("rotationY") {
        public void setValue(View object, float value) {
            object.setRotationY(value);
        }

        public Float get(View object) {
            return Float.valueOf(object.getRotationY());
        }
    };
    static final int SAVE_DISABLED = 65536;
    static final int SAVE_DISABLED_MASK = 65536;
    public static final Property<View, Float> SCALE_X = new FloatProperty<View>("scaleX") {
        public void setValue(View object, float value) {
            object.setScaleX(value);
        }

        public Float get(View object) {
            return Float.valueOf(object.getScaleX());
        }
    };
    public static final Property<View, Float> SCALE_Y = new FloatProperty<View>("scaleY") {
        public void setValue(View object, float value) {
            object.setScaleY(value);
        }

        public Float get(View object) {
            return Float.valueOf(object.getScaleY());
        }
    };
    public static final int SCREEN_STATE_OFF = 0;
    public static final int SCREEN_STATE_ON = 1;
    static final int SCROLLBARS_HORIZONTAL = 256;
    static final int SCROLLBARS_INSET_MASK = 16777216;
    public static final int SCROLLBARS_INSIDE_INSET = 16777216;
    public static final int SCROLLBARS_INSIDE_OVERLAY = 0;
    static final int SCROLLBARS_MASK = 768;
    static final int SCROLLBARS_NONE = 0;
    public static final int SCROLLBARS_OUTSIDE_INSET = 50331648;
    static final int SCROLLBARS_OUTSIDE_MASK = 33554432;
    public static final int SCROLLBARS_OUTSIDE_OVERLAY = 33554432;
    static final int SCROLLBARS_STYLE_MASK = 50331648;
    static final int SCROLLBARS_VERTICAL = 512;
    public static final int SCROLLBAR_POSITION_DEFAULT = 0;
    public static final int SCROLLBAR_POSITION_LEFT = 1;
    public static final int SCROLLBAR_POSITION_RIGHT = 2;
    public static final int SCROLL_AXIS_HORIZONTAL = 1;
    public static final int SCROLL_AXIS_NONE = 0;
    public static final int SCROLL_AXIS_VERTICAL = 2;
    static final int SCROLL_INDICATORS_NONE = 0;
    static final int SCROLL_INDICATORS_PFLAG3_MASK = 16128;
    static final int SCROLL_INDICATORS_TO_PFLAGS3_LSHIFT = 8;
    public static final int SCROLL_INDICATOR_BOTTOM = 2;
    public static final int SCROLL_INDICATOR_END = 32;
    public static final int SCROLL_INDICATOR_LEFT = 4;
    public static final int SCROLL_INDICATOR_RIGHT = 8;
    public static final int SCROLL_INDICATOR_START = 16;
    public static final int SCROLL_INDICATOR_TOP = 1;
    protected static final int[] SELECTED_STATE_SET = StateSet.get(2);
    protected static final int[] SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(3);
    public static final int SOUND_EFFECTS_ENABLED = 134217728;
    @UnsupportedAppUsage
    public static final int STATUS_BAR_DISABLE_BACK = 4194304;
    public static final int STATUS_BAR_DISABLE_CLOCK = 8388608;
    @UnsupportedAppUsage
    public static final int STATUS_BAR_DISABLE_EXPAND = 65536;
    @UnsupportedAppUsage
    public static final int STATUS_BAR_DISABLE_HOME = 2097152;
    public static final int STATUS_BAR_DISABLE_NOTIFICATION_ALERTS = 262144;
    public static final int STATUS_BAR_DISABLE_NOTIFICATION_ICONS = 131072;
    public static final int STATUS_BAR_DISABLE_NOTIFICATION_TICKER = 524288;
    @UnsupportedAppUsage
    public static final int STATUS_BAR_DISABLE_RECENT = 16777216;
    public static final int STATUS_BAR_DISABLE_SEARCH = 33554432;
    public static final int STATUS_BAR_DISABLE_SYSTEM_INFO = 1048576;
    @Deprecated
    public static final int STATUS_BAR_HIDDEN = 1;
    public static final int STATUS_BAR_TRANSIENT = 67108864;
    public static final int STATUS_BAR_TRANSLUCENT = 1073741824;
    public static final int STATUS_BAR_TRANSPARENT = 8;
    public static final int STATUS_BAR_UNHIDE = 268435456;
    @Deprecated
    public static final int STATUS_BAR_VISIBLE = 0;
    public static final int SYSTEM_UI_CLEARABLE_FLAGS = 7;
    public static final int SYSTEM_UI_FLAG_FULLSCREEN = 4;
    public static final int SYSTEM_UI_FLAG_HIDE_NAVIGATION = 2;
    public static final int SYSTEM_UI_FLAG_IMMERSIVE = 2048;
    public static final int SYSTEM_UI_FLAG_IMMERSIVE_STICKY = 4096;
    public static final int SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN = 1024;
    public static final int SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION = 512;
    public static final int SYSTEM_UI_FLAG_LAYOUT_STABLE = 256;
    public static final int SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR = 16;
    public static final int SYSTEM_UI_FLAG_LIGHT_STATUS_BAR = 8192;
    public static final int SYSTEM_UI_FLAG_LOW_PROFILE = 1;
    public static final int SYSTEM_UI_FLAG_VISIBLE = 0;
    public static final int SYSTEM_UI_LAYOUT_FLAGS = 1536;
    private static final int SYSTEM_UI_RESERVED_LEGACY1 = 16384;
    private static final int SYSTEM_UI_RESERVED_LEGACY2 = 65536;
    public static final int SYSTEM_UI_TRANSPARENT = 32776;
    public static final int TEXT_ALIGNMENT_CENTER = 4;
    private static final int TEXT_ALIGNMENT_DEFAULT = 1;
    public static final int TEXT_ALIGNMENT_GRAVITY = 1;
    public static final int TEXT_ALIGNMENT_INHERIT = 0;
    static final int TEXT_ALIGNMENT_RESOLVED_DEFAULT = 1;
    public static final int TEXT_ALIGNMENT_TEXT_END = 3;
    public static final int TEXT_ALIGNMENT_TEXT_START = 2;
    public static final int TEXT_ALIGNMENT_VIEW_END = 6;
    public static final int TEXT_ALIGNMENT_VIEW_START = 5;
    public static final int TEXT_DIRECTION_ANY_RTL = 2;
    private static final int TEXT_DIRECTION_DEFAULT = 0;
    public static final int TEXT_DIRECTION_FIRST_STRONG = 1;
    public static final int TEXT_DIRECTION_FIRST_STRONG_LTR = 6;
    public static final int TEXT_DIRECTION_FIRST_STRONG_RTL = 7;
    public static final int TEXT_DIRECTION_INHERIT = 0;
    public static final int TEXT_DIRECTION_LOCALE = 5;
    public static final int TEXT_DIRECTION_LTR = 3;
    static final int TEXT_DIRECTION_RESOLVED_DEFAULT = 1;
    public static final int TEXT_DIRECTION_RTL = 4;
    static final int TOOLTIP = 1073741824;
    public static final Property<View, Float> TRANSLATION_X = new FloatProperty<View>("translationX") {
        public void setValue(View object, float value) {
            object.setTranslationX(value);
        }

        public Float get(View object) {
            return Float.valueOf(object.getTranslationX());
        }
    };
    public static final Property<View, Float> TRANSLATION_Y = new FloatProperty<View>("translationY") {
        public void setValue(View object, float value) {
            object.setTranslationY(value);
        }

        public Float get(View object) {
            return Float.valueOf(object.getTranslationY());
        }
    };
    public static final Property<View, Float> TRANSLATION_Z = new FloatProperty<View>("translationZ") {
        public void setValue(View object, float value) {
            object.setTranslationZ(value);
        }

        public Float get(View object) {
            return Float.valueOf(object.getTranslationZ());
        }
    };
    private static final int UNDEFINED_PADDING = Integer.MIN_VALUE;
    protected static final String VIEW_LOG_TAG = "View";
    protected static final int VIEW_STRUCTURE_FOR_ASSIST = 0;
    protected static final int VIEW_STRUCTURE_FOR_AUTOFILL = 1;
    protected static final int VIEW_STRUCTURE_FOR_CONTENT_CAPTURE = 2;
    private static final int[] VISIBILITY_FLAGS = new int[]{0, 4, 8};
    static final int VISIBILITY_MASK = 12;
    public static final int VISIBLE = 0;
    static final int WILL_NOT_CACHE_DRAWING = 131072;
    static final int WILL_NOT_DRAW = 128;
    protected static final int[] WINDOW_FOCUSED_STATE_SET = StateSet.get(1);
    public static final Property<View, Float> X = new FloatProperty<View>("x") {
        public void setValue(View object, float value) {
            object.setX(value);
        }

        public Float get(View object) {
            return Float.valueOf(object.getX());
        }
    };
    public static final Property<View, Float> Y = new FloatProperty<View>("y") {
        public void setValue(View object, float value) {
            object.setY(value);
        }

        public Float get(View object) {
            return Float.valueOf(object.getY());
        }
    };
    public static final Property<View, Float> Z = new FloatProperty<View>("z") {
        public void setValue(View object, float value) {
            object.setZ(value);
        }

        public Float get(View object) {
            return Float.valueOf(object.getZ());
        }
    };
    private static SparseArray<String> mAttributeMap;
    private static boolean sAcceptZeroSizeDragShadow;
    private static boolean sAlwaysAssignFocus;
    private static boolean sAlwaysRemeasureExactly = false;
    private static boolean sAutoFocusableOffUIThreadWontNotifyParents;
    static boolean sBrokenInsetsDispatch;
    protected static boolean sBrokenWindowBackground;
    private static boolean sCanFocusZeroSized;
    static boolean sCascadedDragDrop;
    private static boolean sCompatibilityDone = false;
    private static Paint sDebugPaint;
    public static boolean sDebugViewAttributes = false;
    public static String sDebugViewAttributesApplicationPackage;
    static boolean sHasFocusableExcludeAutoFocusable;
    private static boolean sIgnoreMeasureCache = false;
    private static int sNextAccessibilityViewId;
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    protected static boolean sPreserveMarginParamsInLayoutParamConversion;
    static boolean sTextureViewIgnoresDrawableSetters = false;
    static final ThreadLocal<Rect> sThreadLocal = new ThreadLocal();
    private static boolean sThrowOnInvalidFloatProperties;
    private static boolean sUseBrokenMakeMeasureSpec = false;
    private static boolean sUseDefaultFocusHighlight;
    static boolean sUseZeroUnspecifiedMeasureSpec = false;
    private int mAccessibilityCursorPosition;
    @UnsupportedAppUsage
    AccessibilityDelegate mAccessibilityDelegate;
    private CharSequence mAccessibilityPaneTitle;
    private int mAccessibilityTraversalAfterId;
    private int mAccessibilityTraversalBeforeId;
    @UnsupportedAppUsage
    private int mAccessibilityViewId;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private ViewPropertyAnimator mAnimator;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    AttachInfo mAttachInfo;
    protected WeakReference<Activity> mAttachedActivity;
    private SparseArray<int[]> mAttributeResolutionStacks;
    private SparseIntArray mAttributeSourceResId;
    @ExportedProperty(category = "attributes", hasAdjacentMapping = true)
    public String[] mAttributes;
    private String[] mAutofillHints;
    private AutofillId mAutofillId;
    private int mAutofillViewId;
    @ExportedProperty(deepExport = true, prefix = "bg_")
    @UnsupportedAppUsage
    private Drawable mBackground;
    private RenderNode mBackgroundRenderNode;
    @UnsupportedAppUsage
    private int mBackgroundResource;
    private boolean mBackgroundSizeChanged;
    private TintInfo mBackgroundTint;
    @ExportedProperty(category = "layout")
    @UnsupportedAppUsage(maxTargetSdk = 28)
    protected int mBottom;
    private ContentCaptureSession mCachedContentCaptureSession;
    @UnsupportedAppUsage
    public boolean mCachingFailed;
    @ExportedProperty(category = "drawing")
    Rect mClipBounds;
    private ContentCaptureSession mContentCaptureSession;
    private CharSequence mContentDescription;
    @ExportedProperty(deepExport = true)
    @UnsupportedAppUsage
    protected Context mContext;
    protected Animation mCurrentAnimation;
    private Drawable mDefaultFocusHighlight;
    private Drawable mDefaultFocusHighlightCache;
    boolean mDefaultFocusHighlightEnabled;
    private boolean mDefaultFocusHighlightSizeChanged;
    private int[] mDrawableState;
    @UnsupportedAppUsage
    private Bitmap mDrawingCache;
    private int mDrawingCacheBackgroundColor;
    private int mExplicitStyle;
    protected volatile boolean mFirst;
    private ViewTreeObserver mFloatingTreeObserver;
    @ExportedProperty(deepExport = true, prefix = "fg_")
    private ForegroundInfo mForegroundInfo;
    private ArrayList<FrameMetricsObserver> mFrameMetricsObservers;
    GhostView mGhostView = this;
    boolean mHapticEnabledExplicitly;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private boolean mHasPerformedLongPress;
    private boolean mHoveringTouchDelegate;
    @ExportedProperty(resolveId = true)
    int mID;
    private boolean mIgnoreNextUpEvent;
    private boolean mInContextButtonPress;
    protected final InputEventConsistencyVerifier mInputEventConsistencyVerifier;
    protected boolean mIsWebView;
    @UnsupportedAppUsage
    private SparseArray<Object> mKeyedTags;
    private int mLabelForId;
    private boolean mLastIsOpaque;
    Paint mLayerPaint;
    @ExportedProperty(category = "drawing", mapping = {@IntToString(from = 0, to = "NONE"), @IntToString(from = 1, to = "SOFTWARE"), @IntToString(from = 2, to = "HARDWARE")})
    int mLayerType;
    private Insets mLayoutInsets;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    protected LayoutParams mLayoutParams;
    @ExportedProperty(category = "layout")
    @UnsupportedAppUsage(maxTargetSdk = 28)
    protected int mLeft;
    private boolean mLeftPaddingDefined;
    @UnsupportedAppUsage
    ListenerInfo mListenerInfo;
    private float mLongClickX;
    private float mLongClickY;
    private MatchIdPredicate mMatchIdPredicate;
    private MatchLabelForPredicate mMatchLabelForPredicate;
    private LongSparseLongArray mMeasureCache;
    @ExportedProperty(category = "measurement")
    @UnsupportedAppUsage
    int mMeasuredHeight;
    @ExportedProperty(category = "measurement")
    @UnsupportedAppUsage
    int mMeasuredWidth;
    @ExportedProperty(category = "measurement")
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private int mMinHeight;
    @ExportedProperty(category = "measurement")
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private int mMinWidth;
    private ViewParent mNestedScrollingParent;
    int mNextClusterForwardId;
    private int mNextFocusDownId;
    int mNextFocusForwardId;
    private int mNextFocusLeftId;
    private int mNextFocusRightId;
    private int mNextFocusUpId;
    int mOldHeightMeasureSpec;
    int mOldWidthMeasureSpec;
    ViewOutlineProvider mOutlineProvider;
    private int mOverScrollMode;
    ViewOverlay mOverlay;
    @ExportedProperty(category = "padding")
    @UnsupportedAppUsage
    protected int mPaddingBottom;
    @ExportedProperty(category = "padding")
    @UnsupportedAppUsage
    protected int mPaddingLeft;
    @ExportedProperty(category = "padding")
    @UnsupportedAppUsage
    protected int mPaddingRight;
    @ExportedProperty(category = "padding")
    @UnsupportedAppUsage
    protected int mPaddingTop;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    protected ViewParent mParent;
    private CheckForLongPress mPendingCheckForLongPress;
    @UnsupportedAppUsage
    private CheckForTap mPendingCheckForTap;
    private PerformClick mPerformClick;
    private PointerIcon mPointerIcon;
    @ExportedProperty(flagMapping = {@FlagToString(equals = 4096, mask = 4096, name = "FORCE_LAYOUT"), @FlagToString(equals = 8192, mask = 8192, name = "LAYOUT_REQUIRED"), @FlagToString(equals = 32768, mask = 32768, name = "DRAWING_CACHE_INVALID", outputIf = false), @FlagToString(equals = 32, mask = 32, name = "DRAWN", outputIf = true), @FlagToString(equals = 32, mask = 32, name = "NOT_DRAWN", outputIf = false), @FlagToString(equals = 2097152, mask = 2097152, name = "DIRTY")}, formatToHexString = true)
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123769414)
    public int mPrivateFlags;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123768943)
    int mPrivateFlags2;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 129147060)
    int mPrivateFlags3;
    @UnsupportedAppUsage
    boolean mRecreateDisplayList;
    @UnsupportedAppUsage
    final RenderNode mRenderNode;
    @UnsupportedAppUsage
    private final Resources mResources;
    @ExportedProperty(category = "layout")
    @UnsupportedAppUsage(maxTargetSdk = 28)
    protected int mRight;
    private boolean mRightPaddingDefined;
    private RoundScrollbarRenderer mRoundScrollbarRenderer;
    private HandlerActionQueue mRunQueue;
    @UnsupportedAppUsage
    private ScrollabilityCache mScrollCache;
    private Drawable mScrollIndicatorDrawable;
    @ExportedProperty(category = "scrolling")
    @UnsupportedAppUsage(maxTargetSdk = 28)
    protected int mScrollX;
    @ExportedProperty(category = "scrolling")
    @UnsupportedAppUsage(maxTargetSdk = 28)
    protected int mScrollY;
    private SendViewScrolledAccessibilityEvent mSendViewScrolledAccessibilityEvent;
    private boolean mSendingHoverAccessibilityEvents;
    private int mSourceLayoutId;
    @UnsupportedAppUsage
    String mStartActivityRequestWho;
    private StateListAnimator mStateListAnimator;
    @ExportedProperty(flagMapping = {@FlagToString(equals = 1, mask = 1, name = "LOW_PROFILE"), @FlagToString(equals = 2, mask = 2, name = "HIDE_NAVIGATION"), @FlagToString(equals = 4, mask = 4, name = "FULLSCREEN"), @FlagToString(equals = 256, mask = 256, name = "LAYOUT_STABLE"), @FlagToString(equals = 512, mask = 512, name = "LAYOUT_HIDE_NAVIGATION"), @FlagToString(equals = 1024, mask = 1024, name = "LAYOUT_FULLSCREEN"), @FlagToString(equals = 2048, mask = 2048, name = "IMMERSIVE"), @FlagToString(equals = 4096, mask = 4096, name = "IMMERSIVE_STICKY"), @FlagToString(equals = 8192, mask = 8192, name = "LIGHT_STATUS_BAR"), @FlagToString(equals = 16, mask = 16, name = "LIGHT_NAVIGATION_BAR"), @FlagToString(equals = 65536, mask = 65536, name = "STATUS_BAR_DISABLE_EXPAND"), @FlagToString(equals = 131072, mask = 131072, name = "STATUS_BAR_DISABLE_NOTIFICATION_ICONS"), @FlagToString(equals = 262144, mask = 262144, name = "STATUS_BAR_DISABLE_NOTIFICATION_ALERTS"), @FlagToString(equals = 524288, mask = 524288, name = "STATUS_BAR_DISABLE_NOTIFICATION_TICKER"), @FlagToString(equals = 1048576, mask = 1048576, name = "STATUS_BAR_DISABLE_SYSTEM_INFO"), @FlagToString(equals = 2097152, mask = 2097152, name = "STATUS_BAR_DISABLE_HOME"), @FlagToString(equals = 4194304, mask = 4194304, name = "STATUS_BAR_DISABLE_BACK"), @FlagToString(equals = 8388608, mask = 8388608, name = "STATUS_BAR_DISABLE_CLOCK"), @FlagToString(equals = 16777216, mask = 16777216, name = "STATUS_BAR_DISABLE_RECENT"), @FlagToString(equals = 33554432, mask = 33554432, name = "STATUS_BAR_DISABLE_SEARCH"), @FlagToString(equals = 67108864, mask = 67108864, name = "STATUS_BAR_TRANSIENT"), @FlagToString(equals = 134217728, mask = 134217728, name = "NAVIGATION_BAR_TRANSIENT"), @FlagToString(equals = 268435456, mask = 268435456, name = "STATUS_BAR_UNHIDE"), @FlagToString(equals = 536870912, mask = 536870912, name = "NAVIGATION_BAR_UNHIDE"), @FlagToString(equals = 1073741824, mask = 1073741824, name = "STATUS_BAR_TRANSLUCENT"), @FlagToString(equals = Integer.MIN_VALUE, mask = Integer.MIN_VALUE, name = "NAVIGATION_BAR_TRANSLUCENT"), @FlagToString(equals = 32768, mask = 32768, name = "NAVIGATION_BAR_TRANSPARENT"), @FlagToString(equals = 8, mask = 8, name = "STATUS_BAR_TRANSPARENT")}, formatToHexString = true)
    int mSystemUiVisibility;
    @UnsupportedAppUsage
    protected Object mTag;
    private int[] mTempNestedScrollConsumed;
    TooltipInfo mTooltipInfo;
    @ExportedProperty(category = "layout")
    @UnsupportedAppUsage(maxTargetSdk = 28)
    protected int mTop;
    private TouchDelegate mTouchDelegate;
    private int mTouchSlop;
    @UnsupportedAppUsage
    public TransformationInfo mTransformationInfo;
    int mTransientStateCount;
    private String mTransitionName;
    @UnsupportedAppUsage
    private Bitmap mUnscaledDrawingCache;
    private UnsetPressedState mUnsetPressedState;
    @ExportedProperty(category = "padding")
    protected int mUserPaddingBottom;
    @ExportedProperty(category = "padding")
    int mUserPaddingEnd;
    @ExportedProperty(category = "padding")
    protected int mUserPaddingLeft;
    int mUserPaddingLeftInitial;
    @ExportedProperty(category = "padding")
    protected int mUserPaddingRight;
    int mUserPaddingRightInitial;
    @ExportedProperty(category = "padding")
    int mUserPaddingStart;
    private float mVerticalScrollFactor;
    @UnsupportedAppUsage
    private int mVerticalScrollbarPosition;
    @ExportedProperty(formatToHexString = true)
    @UnsupportedAppUsage(maxTargetSdk = 28)
    int mViewFlags;
    private Handler mVisibilityChangeForAutofillHandler;
    int mWindowAttachCount;

    public interface OnTouchListener {
        boolean onTouch(View view, MotionEvent motionEvent);
    }

    public interface OnAttachStateChangeListener {
        void onViewAttachedToWindow(View view);

        void onViewDetachedFromWindow(View view);
    }

    public static class AccessibilityDelegate {
        public void sendAccessibilityEvent(View host, int eventType) {
            host.sendAccessibilityEventInternal(eventType);
        }

        public boolean performAccessibilityAction(View host, int action, Bundle args) {
            return host.performAccessibilityActionInternal(action, args);
        }

        public void sendAccessibilityEventUnchecked(View host, AccessibilityEvent event) {
            host.sendAccessibilityEventUncheckedInternal(event);
        }

        public boolean dispatchPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
            return host.dispatchPopulateAccessibilityEventInternal(event);
        }

        public void onPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
            host.onPopulateAccessibilityEventInternal(event);
        }

        public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
            host.onInitializeAccessibilityEventInternal(event);
        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfo info) {
            host.onInitializeAccessibilityNodeInfoInternal(info);
        }

        public void addExtraDataToAccessibilityNodeInfo(View host, AccessibilityNodeInfo info, String extraDataKey, Bundle arguments) {
            host.addExtraDataToAccessibilityNodeInfo(info, extraDataKey, arguments);
        }

        public boolean onRequestSendAccessibilityEvent(ViewGroup host, View child, AccessibilityEvent event) {
            return host.onRequestSendAccessibilityEventInternal(child, event);
        }

        public AccessibilityNodeProvider getAccessibilityNodeProvider(View host) {
            return null;
        }

        @UnsupportedAppUsage
        public AccessibilityNodeInfo createAccessibilityNodeInfo(View host) {
            return host.createAccessibilityNodeInfoInternal();
        }
    }

    static final class AttachInfo {
        int mAccessibilityFetchFlags;
        Drawable mAccessibilityFocusDrawable;
        int mAccessibilityWindowId = -1;
        boolean mAlwaysConsumeSystemBars;
        @UnsupportedAppUsage
        float mApplicationScale;
        Drawable mAutofilledDrawable;
        Canvas mCanvas;
        @UnsupportedAppUsage
        final Rect mContentInsets = new Rect();
        boolean mDebugLayout = ((Boolean) DisplayProperties.debug_layout().orElse(Boolean.valueOf(false))).booleanValue();
        int mDisabledSystemUiVisibility;
        Display mDisplay;
        final ParcelableWrapper mDisplayCutout = new ParcelableWrapper(DisplayCutout.NO_CUTOUT);
        @UnsupportedAppUsage
        int mDisplayState = 0;
        public Surface mDragSurface;
        IBinder mDragToken;
        @UnsupportedAppUsage
        long mDrawingTime;
        boolean mForceReportNewAttributes;
        @UnsupportedAppUsage
        final InternalInsetsInfo mGivenInternalInsets = new InternalInsetsInfo();
        int mGlobalSystemUiVisibility = -1;
        @UnsupportedAppUsage
        final Handler mHandler;
        boolean mHandlingPointerEvent;
        boolean mHardwareAccelerated;
        boolean mHardwareAccelerationRequested;
        boolean mHasNonEmptyGivenInternalInsets;
        boolean mHasSystemUiListeners;
        @UnsupportedAppUsage
        boolean mHasWindowFocus;
        IWindowId mIWindowId;
        @UnsupportedAppUsage
        boolean mInTouchMode;
        final int[] mInvalidateChildLocation = new int[2];
        @UnsupportedAppUsage
        boolean mKeepScreenOn;
        @UnsupportedAppUsage
        final DispatcherState mKeyDispatchState = new DispatcherState();
        boolean mNeedsUpdateLightCenter;
        final Rect mOutsets = new Rect();
        final Rect mOverscanInsets = new Rect();
        boolean mOverscanRequested;
        IBinder mPanelParentWindowToken;
        List<RenderNode> mPendingAnimatingRenderNodes;
        final Point mPoint = new Point();
        @UnsupportedAppUsage
        boolean mRecomputeGlobalAttributes;
        final Callbacks mRootCallbacks;
        View mRootView;
        @UnsupportedAppUsage
        boolean mScalingRequired;
        @UnsupportedAppUsage
        final ArrayList<View> mScrollContainers = new ArrayList();
        @UnsupportedAppUsage
        final IWindowSession mSession;
        @UnsupportedAppUsage
        final Rect mStableInsets = new Rect();
        int mSystemUiVisibility;
        final ArrayList<View> mTempArrayList = new ArrayList(24);
        ThreadedRenderer mThreadedRenderer;
        final Rect mTmpInvalRect = new Rect();
        final int[] mTmpLocation = new int[2];
        final Matrix mTmpMatrix = new Matrix();
        final Outline mTmpOutline = new Outline();
        final List<RectF> mTmpRectList = new ArrayList();
        final float[] mTmpTransformLocation = new float[2];
        final RectF mTmpTransformRect = new RectF();
        final RectF mTmpTransformRect1 = new RectF();
        final Transformation mTmpTransformation = new Transformation();
        View mTooltipHost;
        final int[] mTransparentLocation = new int[2];
        @UnsupportedAppUsage
        final ViewTreeObserver mTreeObserver;
        boolean mUnbufferedDispatchRequested;
        boolean mUse32BitDrawingCache;
        View mViewRequestingLayout;
        final ViewRootImpl mViewRootImpl;
        @UnsupportedAppUsage
        boolean mViewScrollChanged;
        @UnsupportedAppUsage
        boolean mViewVisibilityChanged;
        @UnsupportedAppUsage
        final Rect mVisibleInsets = new Rect();
        @UnsupportedAppUsage
        final IWindow mWindow;
        WindowId mWindowId;
        int mWindowLeft;
        final IBinder mWindowToken;
        int mWindowTop;
        int mWindowVisibility;

        interface Callbacks {
            boolean performHapticFeedback(int i, boolean z);

            void playSoundEffect(int i);
        }

        static class InvalidateInfo {
            private static final int POOL_LIMIT = 10;
            private static final SynchronizedPool<InvalidateInfo> sPool = new SynchronizedPool(10);
            @UnsupportedAppUsage
            int bottom;
            @UnsupportedAppUsage
            int left;
            @UnsupportedAppUsage
            int right;
            @UnsupportedAppUsage
            View target;
            @UnsupportedAppUsage
            int top;

            InvalidateInfo() {
            }

            public static InvalidateInfo obtain() {
                InvalidateInfo instance = (InvalidateInfo) sPool.acquire();
                return instance != null ? instance : new InvalidateInfo();
            }

            public void recycle() {
                this.target = null;
                sPool.release(this);
            }
        }

        AttachInfo(IWindowSession session, IWindow window, Display display, ViewRootImpl viewRootImpl, Handler handler, Callbacks effectPlayer, Context context) {
            this.mSession = session;
            this.mWindow = window;
            this.mWindowToken = window.asBinder();
            this.mDisplay = display;
            this.mViewRootImpl = viewRootImpl;
            this.mHandler = handler;
            this.mRootCallbacks = effectPlayer;
            this.mTreeObserver = new ViewTreeObserver(context);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AutofillFlags {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AutofillImportance {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AutofillType {
    }

    public static class BaseSavedState extends AbsSavedState {
        static final int AUTOFILL_ID = 4;
        public static final Creator<BaseSavedState> CREATOR = new ClassLoaderCreator<BaseSavedState>() {
            public BaseSavedState createFromParcel(Parcel in) {
                return new BaseSavedState(in);
            }

            public BaseSavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new BaseSavedState(in, loader);
            }

            public BaseSavedState[] newArray(int size) {
                return new BaseSavedState[size];
            }
        };
        static final int IS_AUTOFILLED = 2;
        static final int START_ACTIVITY_REQUESTED_WHO_SAVED = 1;
        int mAutofillViewId;
        boolean mIsAutofilled;
        int mSavedData;
        String mStartActivityRequestWhoSaved;

        public BaseSavedState(Parcel source) {
            this(source, null);
        }

        public BaseSavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
            this.mSavedData = source.readInt();
            this.mStartActivityRequestWhoSaved = source.readString();
            this.mIsAutofilled = source.readBoolean();
            this.mAutofillViewId = source.readInt();
        }

        public BaseSavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.mSavedData);
            out.writeString(this.mStartActivityRequestWhoSaved);
            out.writeBoolean(this.mIsAutofilled);
            out.writeInt(this.mAutofillViewId);
        }
    }

    private final class CheckForLongPress implements Runnable {
        private int mClassification;
        private boolean mOriginalPressedState;
        private int mOriginalWindowAttachCount;
        private float mX;
        private float mY;

        private CheckForLongPress() {
        }

        /* synthetic */ CheckForLongPress(View x0, AnonymousClass1 x1) {
            this();
        }

        public void run() {
            if (this.mOriginalPressedState == View.this.isPressed() && View.this.mParent != null && this.mOriginalWindowAttachCount == View.this.mWindowAttachCount) {
                View.this.recordGestureClassification(this.mClassification);
                if (View.this.performLongClick(this.mX, this.mY)) {
                    View.this.mHasPerformedLongPress = true;
                }
            }
        }

        public void setAnchor(float x, float y) {
            this.mX = x;
            this.mY = y;
        }

        public void rememberWindowAttachCount() {
            this.mOriginalWindowAttachCount = View.this.mWindowAttachCount;
        }

        public void rememberPressedState() {
            this.mOriginalPressedState = View.this.isPressed();
        }

        public void setClassification(int classification) {
            this.mClassification = classification;
        }
    }

    private final class CheckForTap implements Runnable {
        public float x;
        public float y;

        private CheckForTap() {
        }

        /* synthetic */ CheckForTap(View x0, AnonymousClass1 x1) {
            this();
        }

        public void run() {
            View view = View.this;
            view.mPrivateFlags &= -33554433;
            View.this.setPressed(true, this.x, this.y);
            View.this.checkForLongClick((long) (ViewConfiguration.getLongPressTimeout() - ViewConfiguration.getTapTimeout()), this.x, this.y, 3);
        }
    }

    public interface OnClickListener {
        void onClick(View view);
    }

    private static class DeclaredOnClickListener implements OnClickListener {
        private final View mHostView;
        private final String mMethodName;
        private Context mResolvedContext;
        private Method mResolvedMethod;

        public DeclaredOnClickListener(View hostView, String methodName) {
            this.mHostView = hostView;
            this.mMethodName = methodName;
        }

        public void onClick(View v) {
            if (this.mResolvedMethod == null) {
                resolveMethod(this.mHostView.getContext(), this.mMethodName);
            }
            try {
                this.mResolvedMethod.invoke(this.mResolvedContext, new Object[]{v});
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Could not execute non-public method for android:onClick", e);
            } catch (InvocationTargetException e2) {
                throw new IllegalStateException("Could not execute method for android:onClick", e2);
            }
        }

        private void resolveMethod(Context context, String name) {
            String idText;
            while (context != null) {
                try {
                    if (!context.isRestricted()) {
                        Method method = context.getClass().getMethod(this.mMethodName, new Class[]{View.class});
                        if (method != null) {
                            this.mResolvedMethod = method;
                            this.mResolvedContext = context;
                            return;
                        }
                    }
                } catch (NoSuchMethodException e) {
                }
                if (context instanceof ContextWrapper) {
                    context = ((ContextWrapper) context).getBaseContext();
                } else {
                    context = null;
                }
            }
            int id = this.mHostView.getId();
            if (id == -1) {
                idText = "";
            } else {
                idText = new StringBuilder();
                idText.append(" with id '");
                idText.append(this.mHostView.getContext().getResources().getResourceEntryName(id));
                idText.append("'");
                idText = idText.toString();
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not find method ");
            stringBuilder.append(this.mMethodName);
            stringBuilder.append("(View) in a parent or ancestor Context for android:onClick attribute defined on view ");
            stringBuilder.append(this.mHostView.getClass());
            stringBuilder.append(idText);
            throw new IllegalStateException(stringBuilder.toString());
        }
    }

    public static class DragShadowBuilder {
        @UnsupportedAppUsage
        private final WeakReference<View> mView;

        public DragShadowBuilder(View view) {
            this.mView = new WeakReference(view);
        }

        public DragShadowBuilder() {
            this.mView = new WeakReference(null);
        }

        public final View getView() {
            return (View) this.mView.get();
        }

        public void onProvideShadowMetrics(Point outShadowSize, Point outShadowTouchPoint) {
            View view = (View) this.mView.get();
            if (view != null) {
                outShadowSize.set(view.getWidth(), view.getHeight());
                outShadowTouchPoint.set(outShadowSize.x / 2, outShadowSize.y / 2);
                return;
            }
            Log.e(View.VIEW_LOG_TAG, "Asked for drag thumb metrics but no view");
        }

        public void onDrawShadow(Canvas canvas) {
            View view = (View) this.mView.get();
            if (view != null) {
                view.draw(canvas);
            } else {
                Log.e(View.VIEW_LOG_TAG, "Asked to draw drag shadow but no view");
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DrawingCacheQuality {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FindViewFlags {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FocusDirection {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FocusRealDirection {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Focusable {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FocusableMode {
    }

    private static class ForegroundInfo {
        private boolean mBoundsChanged;
        private Drawable mDrawable;
        private int mGravity;
        private boolean mInsidePadding;
        private final Rect mOverlayBounds;
        private final Rect mSelfBounds;
        private TintInfo mTintInfo;

        private ForegroundInfo() {
            this.mGravity = 119;
            this.mInsidePadding = true;
            this.mBoundsChanged = true;
            this.mSelfBounds = new Rect();
            this.mOverlayBounds = new Rect();
        }

        /* synthetic */ ForegroundInfo(AnonymousClass1 x0) {
            this();
        }
    }

    public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<View> {
        private int mAccessibilityFocusedId;
        private int mAccessibilityHeadingId;
        private int mAccessibilityLiveRegionId;
        private int mAccessibilityPaneTitleId;
        private int mAccessibilityTraversalAfterId;
        private int mAccessibilityTraversalBeforeId;
        private int mActivatedId;
        private int mAlphaId;
        private int mAutofillHintsId;
        private int mBackgroundId;
        private int mBackgroundTintId;
        private int mBackgroundTintModeId;
        private int mBaselineId;
        private int mClickableId;
        private int mContentDescriptionId;
        private int mContextClickableId;
        private int mDefaultFocusHighlightEnabledId;
        private int mDrawingCacheQualityId;
        private int mDuplicateParentStateId;
        private int mElevationId;
        private int mEnabledId;
        private int mFadingEdgeLengthId;
        private int mFilterTouchesWhenObscuredId;
        private int mFitsSystemWindowsId;
        private int mFocusableId;
        private int mFocusableInTouchModeId;
        private int mFocusedByDefaultId;
        private int mFocusedId;
        private int mForceDarkAllowedId;
        private int mForegroundGravityId;
        private int mForegroundId;
        private int mForegroundTintId;
        private int mForegroundTintModeId;
        private int mHapticFeedbackEnabledId;
        private int mIdId;
        private int mImportantForAccessibilityId;
        private int mImportantForAutofillId;
        private int mIsScrollContainerId;
        private int mKeepScreenOnId;
        private int mKeyboardNavigationClusterId;
        private int mLabelForId;
        private int mLayerTypeId;
        private int mLayoutDirectionId;
        private int mLongClickableId;
        private int mMinHeightId;
        private int mMinWidthId;
        private int mNestedScrollingEnabledId;
        private int mNextClusterForwardId;
        private int mNextFocusDownId;
        private int mNextFocusForwardId;
        private int mNextFocusLeftId;
        private int mNextFocusRightId;
        private int mNextFocusUpId;
        private int mOutlineAmbientShadowColorId;
        private int mOutlineProviderId;
        private int mOutlineSpotShadowColorId;
        private int mOverScrollModeId;
        private int mPaddingBottomId;
        private int mPaddingLeftId;
        private int mPaddingRightId;
        private int mPaddingTopId;
        private int mPointerIconId;
        private int mPressedId;
        private boolean mPropertiesMapped = false;
        private int mRawLayoutDirectionId;
        private int mRawTextAlignmentId;
        private int mRawTextDirectionId;
        private int mRequiresFadingEdgeId;
        private int mRotationId;
        private int mRotationXId;
        private int mRotationYId;
        private int mSaveEnabledId;
        private int mScaleXId;
        private int mScaleYId;
        private int mScreenReaderFocusableId;
        private int mScrollIndicatorsId;
        private int mScrollXId;
        private int mScrollYId;
        private int mScrollbarDefaultDelayBeforeFadeId;
        private int mScrollbarFadeDurationId;
        private int mScrollbarSizeId;
        private int mScrollbarStyleId;
        private int mSelectedId;
        private int mSolidColorId;
        private int mSoundEffectsEnabledId;
        private int mStateListAnimatorId;
        private int mTagId;
        private int mTextAlignmentId;
        private int mTextDirectionId;
        private int mTooltipTextId;
        private int mTransformPivotXId;
        private int mTransformPivotYId;
        private int mTransitionNameId;
        private int mTranslationXId;
        private int mTranslationYId;
        private int mTranslationZId;
        private int mVisibilityId;

        public void mapProperties(PropertyMapper propertyMapper) {
            PropertyMapper propertyMapper2 = propertyMapper;
            this.mAccessibilityFocusedId = propertyMapper2.mapBoolean("accessibilityFocused", 0);
            this.mAccessibilityHeadingId = propertyMapper2.mapBoolean("accessibilityHeading", 16844160);
            SparseArray<String> accessibilityLiveRegionEnumMapping = new SparseArray();
            String str = "none";
            accessibilityLiveRegionEnumMapping.put(0, str);
            accessibilityLiveRegionEnumMapping.put(1, "polite");
            accessibilityLiveRegionEnumMapping.put(2, "assertive");
            Objects.requireNonNull(accessibilityLiveRegionEnumMapping);
            this.mAccessibilityLiveRegionId = propertyMapper2.mapIntEnum("accessibilityLiveRegion", 16843758, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(accessibilityLiveRegionEnumMapping));
            this.mAccessibilityPaneTitleId = propertyMapper2.mapObject("accessibilityPaneTitle", 16844156);
            this.mAccessibilityTraversalAfterId = propertyMapper2.mapResourceId("accessibilityTraversalAfter", 16843986);
            this.mAccessibilityTraversalBeforeId = propertyMapper2.mapResourceId("accessibilityTraversalBefore", 16843985);
            this.mActivatedId = propertyMapper2.mapBoolean("activated", 0);
            this.mAlphaId = propertyMapper2.mapFloat("alpha", 16843551);
            this.mAutofillHintsId = propertyMapper2.mapObject("autofillHints", 16844118);
            this.mBackgroundId = propertyMapper2.mapObject("background", 16842964);
            this.mBackgroundTintId = propertyMapper2.mapObject("backgroundTint", 16843883);
            this.mBackgroundTintModeId = propertyMapper2.mapObject("backgroundTintMode", 16843884);
            this.mBaselineId = propertyMapper2.mapInt("baseline", 16843548);
            this.mClickableId = propertyMapper2.mapBoolean("clickable", 16842981);
            this.mContentDescriptionId = propertyMapper2.mapObject("contentDescription", 16843379);
            this.mContextClickableId = propertyMapper2.mapBoolean("contextClickable", 16844007);
            this.mDefaultFocusHighlightEnabledId = propertyMapper2.mapBoolean("defaultFocusHighlightEnabled", 16844130);
            SparseArray<String> drawingCacheQualityEnumMapping = new SparseArray();
            String str2 = "auto";
            drawingCacheQualityEnumMapping.put(0, str2);
            drawingCacheQualityEnumMapping.put(524288, System.POWER_MODE_VALUE_LOW);
            drawingCacheQualityEnumMapping.put(1048576, System.POWER_MODE_VALUE_HIGH);
            Objects.requireNonNull(drawingCacheQualityEnumMapping);
            this.mDrawingCacheQualityId = propertyMapper2.mapIntEnum("drawingCacheQuality", 16842984, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(drawingCacheQualityEnumMapping));
            this.mDuplicateParentStateId = propertyMapper2.mapBoolean("duplicateParentState", 16842985);
            this.mElevationId = propertyMapper2.mapFloat("elevation", 16843840);
            this.mEnabledId = propertyMapper2.mapBoolean("enabled", 16842766);
            this.mFadingEdgeLengthId = propertyMapper2.mapInt("fadingEdgeLength", 16842976);
            this.mFilterTouchesWhenObscuredId = propertyMapper2.mapBoolean("filterTouchesWhenObscured", 16843460);
            this.mFitsSystemWindowsId = propertyMapper2.mapBoolean("fitsSystemWindows", 16842973);
            SparseArray<String> focusableEnumMapping = new SparseArray();
            focusableEnumMapping.put(0, "false");
            focusableEnumMapping.put(1, "true");
            focusableEnumMapping.put(16, str2);
            Objects.requireNonNull(focusableEnumMapping);
            this.mFocusableId = propertyMapper2.mapIntEnum("focusable", 16842970, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(focusableEnumMapping));
            this.mFocusableInTouchModeId = propertyMapper2.mapBoolean("focusableInTouchMode", 16842971);
            this.mFocusedId = propertyMapper2.mapBoolean("focused", 0);
            this.mFocusedByDefaultId = propertyMapper2.mapBoolean("focusedByDefault", 16844100);
            this.mForceDarkAllowedId = propertyMapper2.mapBoolean("forceDarkAllowed", 16844172);
            this.mForegroundId = propertyMapper2.mapObject("foreground", 16843017);
            this.mForegroundGravityId = propertyMapper2.mapGravity("foregroundGravity", 16843264);
            this.mForegroundTintId = propertyMapper2.mapObject("foregroundTint", 16843885);
            this.mForegroundTintModeId = propertyMapper2.mapObject("foregroundTintMode", 16843886);
            this.mHapticFeedbackEnabledId = propertyMapper2.mapBoolean("hapticFeedbackEnabled", 16843358);
            this.mIdId = propertyMapper2.mapResourceId("id", 16842960);
            SparseArray<String> importantForAccessibilityEnumMapping = new SparseArray();
            importantForAccessibilityEnumMapping.put(0, str2);
            importantForAccessibilityEnumMapping.put(1, "yes");
            importantForAccessibilityEnumMapping.put(2, "no");
            importantForAccessibilityEnumMapping.put(4, "noHideDescendants");
            Objects.requireNonNull(importantForAccessibilityEnumMapping);
            this.mImportantForAccessibilityId = propertyMapper2.mapIntEnum("importantForAccessibility", 16843690, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(importantForAccessibilityEnumMapping));
            SparseArray<String> importantForAutofillEnumMapping = new SparseArray();
            importantForAutofillEnumMapping.put(0, str2);
            importantForAutofillEnumMapping.put(1, "yes");
            importantForAutofillEnumMapping.put(2, "no");
            importantForAutofillEnumMapping.put(4, "yesExcludeDescendants");
            importantForAutofillEnumMapping.put(8, "noExcludeDescendants");
            Objects.requireNonNull(importantForAutofillEnumMapping);
            this.mImportantForAutofillId = propertyMapper2.mapIntEnum("importantForAutofill", 16844120, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(importantForAutofillEnumMapping));
            this.mIsScrollContainerId = propertyMapper2.mapBoolean("isScrollContainer", 16843342);
            this.mKeepScreenOnId = propertyMapper2.mapBoolean("keepScreenOn", 16843286);
            this.mKeyboardNavigationClusterId = propertyMapper2.mapBoolean("keyboardNavigationCluster", 16844096);
            this.mLabelForId = propertyMapper2.mapResourceId("labelFor", 16843718);
            SparseArray<String> layerTypeEnumMapping = new SparseArray();
            layerTypeEnumMapping.put(0, str);
            layerTypeEnumMapping.put(1, "software");
            layerTypeEnumMapping.put(2, "hardware");
            Objects.requireNonNull(layerTypeEnumMapping);
            this.mLayerTypeId = propertyMapper2.mapIntEnum("layerType", 16843604, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(layerTypeEnumMapping));
            SparseArray<String> layoutDirectionEnumMapping = new SparseArray();
            String str3 = "ltr";
            layoutDirectionEnumMapping.put(0, str3);
            String str4 = "rtl";
            layoutDirectionEnumMapping.put(1, str4);
            Objects.requireNonNull(layoutDirectionEnumMapping);
            this.mLayoutDirectionId = propertyMapper2.mapIntEnum("layoutDirection", 16843698, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(layoutDirectionEnumMapping));
            this.mLongClickableId = propertyMapper2.mapBoolean("longClickable", 16842982);
            this.mMinHeightId = propertyMapper2.mapInt("minHeight", 16843072);
            this.mMinWidthId = propertyMapper2.mapInt("minWidth", 16843071);
            this.mNestedScrollingEnabledId = propertyMapper2.mapBoolean("nestedScrollingEnabled", 16843830);
            this.mNextClusterForwardId = propertyMapper2.mapResourceId("nextClusterForward", 16844098);
            this.mNextFocusDownId = propertyMapper2.mapResourceId("nextFocusDown", 16842980);
            this.mNextFocusForwardId = propertyMapper2.mapResourceId("nextFocusForward", 16843580);
            this.mNextFocusLeftId = propertyMapper2.mapResourceId("nextFocusLeft", 16842977);
            this.mNextFocusRightId = propertyMapper2.mapResourceId("nextFocusRight", 16842978);
            this.mNextFocusUpId = propertyMapper2.mapResourceId("nextFocusUp", 16842979);
            this.mOutlineAmbientShadowColorId = propertyMapper2.mapColor("outlineAmbientShadowColor", 16844162);
            this.mOutlineProviderId = propertyMapper2.mapObject("outlineProvider", 16843960);
            this.mOutlineSpotShadowColorId = propertyMapper2.mapColor("outlineSpotShadowColor", 16844161);
            SparseArray<String> overScrollModeEnumMapping = new SparseArray();
            overScrollModeEnumMapping.put(0, "always");
            overScrollModeEnumMapping.put(1, "ifContentScrolls");
            overScrollModeEnumMapping.put(2, "never");
            Objects.requireNonNull(overScrollModeEnumMapping);
            this.mOverScrollModeId = propertyMapper2.mapIntEnum("overScrollMode", 16843457, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(overScrollModeEnumMapping));
            this.mPaddingBottomId = propertyMapper2.mapInt("paddingBottom", 16842969);
            this.mPaddingLeftId = propertyMapper2.mapInt("paddingLeft", 16842966);
            this.mPaddingRightId = propertyMapper2.mapInt("paddingRight", 16842968);
            this.mPaddingTopId = propertyMapper2.mapInt("paddingTop", 16842967);
            this.mPointerIconId = propertyMapper2.mapObject("pointerIcon", 16844041);
            this.mPressedId = propertyMapper2.mapBoolean("pressed", 0);
            SparseArray<String> rawLayoutDirectionEnumMapping = new SparseArray();
            rawLayoutDirectionEnumMapping.put(0, str3);
            rawLayoutDirectionEnumMapping.put(1, str4);
            String str5 = "inherit";
            rawLayoutDirectionEnumMapping.put(2, str5);
            String str6 = Words.LOCALE;
            rawLayoutDirectionEnumMapping.put(3, str6);
            Objects.requireNonNull(rawLayoutDirectionEnumMapping);
            this.mRawLayoutDirectionId = propertyMapper2.mapIntEnum("rawLayoutDirection", 0, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(rawLayoutDirectionEnumMapping));
            rawLayoutDirectionEnumMapping = new SparseArray();
            rawLayoutDirectionEnumMapping.put(0, str5);
            rawLayoutDirectionEnumMapping.put(1, "gravity");
            rawLayoutDirectionEnumMapping.put(2, "textStart");
            rawLayoutDirectionEnumMapping.put(3, "textEnd");
            rawLayoutDirectionEnumMapping.put(4, "center");
            rawLayoutDirectionEnumMapping.put(5, "viewStart");
            rawLayoutDirectionEnumMapping.put(6, "viewEnd");
            Objects.requireNonNull(rawLayoutDirectionEnumMapping);
            this.mRawTextAlignmentId = propertyMapper2.mapIntEnum("rawTextAlignment", 0, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(rawLayoutDirectionEnumMapping));
            accessibilityLiveRegionEnumMapping = new SparseArray();
            accessibilityLiveRegionEnumMapping.put(0, str5);
            accessibilityLiveRegionEnumMapping.put(1, "firstStrong");
            accessibilityLiveRegionEnumMapping.put(2, "anyRtl");
            accessibilityLiveRegionEnumMapping.put(3, str3);
            accessibilityLiveRegionEnumMapping.put(4, str4);
            accessibilityLiveRegionEnumMapping.put(5, str6);
            accessibilityLiveRegionEnumMapping.put(6, "firstStrongLtr");
            accessibilityLiveRegionEnumMapping.put(7, "firstStrongRtl");
            Objects.requireNonNull(accessibilityLiveRegionEnumMapping);
            this.mRawTextDirectionId = propertyMapper2.mapIntEnum("rawTextDirection", 0, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(accessibilityLiveRegionEnumMapping));
            IntFlagMapping requiresFadingEdgeFlagMapping = new IntFlagMapping();
            requiresFadingEdgeFlagMapping.add(4096, 4096, Slice.HINT_HORIZONTAL);
            requiresFadingEdgeFlagMapping.add(12288, 0, str);
            requiresFadingEdgeFlagMapping.add(8192, 8192, "vertical");
            Objects.requireNonNull(requiresFadingEdgeFlagMapping);
            this.mRequiresFadingEdgeId = propertyMapper2.mapIntFlag("requiresFadingEdge", 16843685, new -$$Lambda$gFNlJIKfxqleu304aRWP5R5v1yY(requiresFadingEdgeFlagMapping));
            this.mRotationId = propertyMapper2.mapFloat("rotation", 16843558);
            this.mRotationXId = propertyMapper2.mapFloat("rotationX", 16843559);
            this.mRotationYId = propertyMapper2.mapFloat("rotationY", 16843560);
            this.mSaveEnabledId = propertyMapper2.mapBoolean("saveEnabled", 16842983);
            this.mScaleXId = propertyMapper2.mapFloat("scaleX", 16843556);
            this.mScaleYId = propertyMapper2.mapFloat("scaleY", 16843557);
            this.mScreenReaderFocusableId = propertyMapper2.mapBoolean("screenReaderFocusable", 16844148);
            IntFlagMapping scrollIndicatorsFlagMapping = new IntFlagMapping();
            scrollIndicatorsFlagMapping.add(2, 2, "bottom");
            scrollIndicatorsFlagMapping.add(32, 32, "end");
            scrollIndicatorsFlagMapping.add(4, 4, "left");
            scrollIndicatorsFlagMapping.add(-1, 0, str);
            scrollIndicatorsFlagMapping.add(8, 8, "right");
            scrollIndicatorsFlagMapping.add(16, 16, BaseMmsColumns.START);
            scrollIndicatorsFlagMapping.add(1, 1, "top");
            Objects.requireNonNull(scrollIndicatorsFlagMapping);
            this.mScrollIndicatorsId = propertyMapper2.mapIntFlag("scrollIndicators", 16844006, new -$$Lambda$gFNlJIKfxqleu304aRWP5R5v1yY(scrollIndicatorsFlagMapping));
            this.mScrollXId = propertyMapper2.mapInt("scrollX", 16842962);
            this.mScrollYId = propertyMapper2.mapInt("scrollY", 16842963);
            this.mScrollbarDefaultDelayBeforeFadeId = propertyMapper2.mapInt("scrollbarDefaultDelayBeforeFade", 16843433);
            this.mScrollbarFadeDurationId = propertyMapper2.mapInt("scrollbarFadeDuration", 16843432);
            this.mScrollbarSizeId = propertyMapper2.mapInt("scrollbarSize", 16842851);
            rawLayoutDirectionEnumMapping = new SparseArray();
            rawLayoutDirectionEnumMapping.put(0, "insideOverlay");
            rawLayoutDirectionEnumMapping.put(16777216, "insideInset");
            rawLayoutDirectionEnumMapping.put(33554432, "outsideOverlay");
            rawLayoutDirectionEnumMapping.put(50331648, "outsideInset");
            Objects.requireNonNull(rawLayoutDirectionEnumMapping);
            this.mScrollbarStyleId = propertyMapper2.mapIntEnum("scrollbarStyle", 16842879, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(rawLayoutDirectionEnumMapping));
            this.mSelectedId = propertyMapper2.mapBoolean(Slice.HINT_SELECTED, 0);
            this.mSolidColorId = propertyMapper2.mapColor("solidColor", 16843594);
            this.mSoundEffectsEnabledId = propertyMapper2.mapBoolean("soundEffectsEnabled", 16843285);
            this.mStateListAnimatorId = propertyMapper2.mapObject("stateListAnimator", 16843848);
            this.mTagId = propertyMapper2.mapObject("tag", 16842961);
            SparseArray<String> textAlignmentEnumMapping = new SparseArray();
            textAlignmentEnumMapping.put(1, "gravity");
            textAlignmentEnumMapping.put(2, "textStart");
            textAlignmentEnumMapping.put(3, "textEnd");
            textAlignmentEnumMapping.put(4, "center");
            textAlignmentEnumMapping.put(5, "viewStart");
            textAlignmentEnumMapping.put(6, "viewEnd");
            Objects.requireNonNull(textAlignmentEnumMapping);
            this.mTextAlignmentId = propertyMapper2.mapIntEnum("textAlignment", 16843697, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(textAlignmentEnumMapping));
            rawLayoutDirectionEnumMapping = new SparseArray();
            rawLayoutDirectionEnumMapping.put(1, "firstStrong");
            rawLayoutDirectionEnumMapping.put(2, "anyRtl");
            rawLayoutDirectionEnumMapping.put(3, str3);
            rawLayoutDirectionEnumMapping.put(4, str4);
            rawLayoutDirectionEnumMapping.put(5, str6);
            rawLayoutDirectionEnumMapping.put(6, "firstStrongLtr");
            rawLayoutDirectionEnumMapping.put(7, "firstStrongRtl");
            Objects.requireNonNull(rawLayoutDirectionEnumMapping);
            this.mTextDirectionId = propertyMapper2.mapIntEnum("textDirection", 0, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(rawLayoutDirectionEnumMapping));
            this.mTooltipTextId = propertyMapper2.mapObject("tooltipText", 16844084);
            this.mTransformPivotXId = propertyMapper2.mapFloat("transformPivotX", 16843552);
            this.mTransformPivotYId = propertyMapper2.mapFloat("transformPivotY", 16843553);
            this.mTransitionNameId = propertyMapper2.mapObject("transitionName", 16843776);
            this.mTranslationXId = propertyMapper2.mapFloat("translationX", 16843554);
            this.mTranslationYId = propertyMapper2.mapFloat("translationY", 16843555);
            this.mTranslationZId = propertyMapper2.mapFloat("translationZ", 16843770);
            overScrollModeEnumMapping = new SparseArray();
            overScrollModeEnumMapping.put(0, CalendarColumns.VISIBLE);
            overScrollModeEnumMapping.put(4, "invisible");
            overScrollModeEnumMapping.put(8, "gone");
            Objects.requireNonNull(overScrollModeEnumMapping);
            this.mVisibilityId = propertyMapper2.mapIntEnum("visibility", 16842972, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(overScrollModeEnumMapping));
            this.mPropertiesMapped = true;
        }

        public void readProperties(View node, PropertyReader propertyReader) {
            if (this.mPropertiesMapped) {
                propertyReader.readBoolean(this.mAccessibilityFocusedId, node.isAccessibilityFocused());
                propertyReader.readBoolean(this.mAccessibilityHeadingId, node.isAccessibilityHeading());
                propertyReader.readIntEnum(this.mAccessibilityLiveRegionId, node.getAccessibilityLiveRegion());
                propertyReader.readObject(this.mAccessibilityPaneTitleId, node.getAccessibilityPaneTitle());
                propertyReader.readResourceId(this.mAccessibilityTraversalAfterId, node.getAccessibilityTraversalAfter());
                propertyReader.readResourceId(this.mAccessibilityTraversalBeforeId, node.getAccessibilityTraversalBefore());
                propertyReader.readBoolean(this.mActivatedId, node.isActivated());
                propertyReader.readFloat(this.mAlphaId, node.getAlpha());
                propertyReader.readObject(this.mAutofillHintsId, node.getAutofillHints());
                propertyReader.readObject(this.mBackgroundId, node.getBackground());
                propertyReader.readObject(this.mBackgroundTintId, node.getBackgroundTintList());
                propertyReader.readObject(this.mBackgroundTintModeId, node.getBackgroundTintMode());
                propertyReader.readInt(this.mBaselineId, node.getBaseline());
                propertyReader.readBoolean(this.mClickableId, node.isClickable());
                propertyReader.readObject(this.mContentDescriptionId, node.getContentDescription());
                propertyReader.readBoolean(this.mContextClickableId, node.isContextClickable());
                propertyReader.readBoolean(this.mDefaultFocusHighlightEnabledId, node.getDefaultFocusHighlightEnabled());
                propertyReader.readIntEnum(this.mDrawingCacheQualityId, node.getDrawingCacheQuality());
                propertyReader.readBoolean(this.mDuplicateParentStateId, node.isDuplicateParentStateEnabled());
                propertyReader.readFloat(this.mElevationId, node.getElevation());
                propertyReader.readBoolean(this.mEnabledId, node.isEnabled());
                propertyReader.readInt(this.mFadingEdgeLengthId, node.getFadingEdgeLength());
                propertyReader.readBoolean(this.mFilterTouchesWhenObscuredId, node.getFilterTouchesWhenObscured());
                propertyReader.readBoolean(this.mFitsSystemWindowsId, node.getFitsSystemWindows());
                propertyReader.readIntEnum(this.mFocusableId, node.getFocusable());
                propertyReader.readBoolean(this.mFocusableInTouchModeId, node.isFocusableInTouchMode());
                propertyReader.readBoolean(this.mFocusedId, node.isFocused());
                propertyReader.readBoolean(this.mFocusedByDefaultId, node.isFocusedByDefault());
                propertyReader.readBoolean(this.mForceDarkAllowedId, node.isForceDarkAllowed());
                propertyReader.readObject(this.mForegroundId, node.getForeground());
                propertyReader.readGravity(this.mForegroundGravityId, node.getForegroundGravity());
                propertyReader.readObject(this.mForegroundTintId, node.getForegroundTintList());
                propertyReader.readObject(this.mForegroundTintModeId, node.getForegroundTintMode());
                propertyReader.readBoolean(this.mHapticFeedbackEnabledId, node.isHapticFeedbackEnabled());
                propertyReader.readResourceId(this.mIdId, node.getId());
                propertyReader.readIntEnum(this.mImportantForAccessibilityId, node.getImportantForAccessibility());
                propertyReader.readIntEnum(this.mImportantForAutofillId, node.getImportantForAutofill());
                propertyReader.readBoolean(this.mIsScrollContainerId, node.isScrollContainer());
                propertyReader.readBoolean(this.mKeepScreenOnId, node.getKeepScreenOn());
                propertyReader.readBoolean(this.mKeyboardNavigationClusterId, node.isKeyboardNavigationCluster());
                propertyReader.readResourceId(this.mLabelForId, node.getLabelFor());
                propertyReader.readIntEnum(this.mLayerTypeId, node.getLayerType());
                propertyReader.readIntEnum(this.mLayoutDirectionId, node.getLayoutDirection());
                propertyReader.readBoolean(this.mLongClickableId, node.isLongClickable());
                propertyReader.readInt(this.mMinHeightId, node.getMinimumHeight());
                propertyReader.readInt(this.mMinWidthId, node.getMinimumWidth());
                propertyReader.readBoolean(this.mNestedScrollingEnabledId, node.isNestedScrollingEnabled());
                propertyReader.readResourceId(this.mNextClusterForwardId, node.getNextClusterForwardId());
                propertyReader.readResourceId(this.mNextFocusDownId, node.getNextFocusDownId());
                propertyReader.readResourceId(this.mNextFocusForwardId, node.getNextFocusForwardId());
                propertyReader.readResourceId(this.mNextFocusLeftId, node.getNextFocusLeftId());
                propertyReader.readResourceId(this.mNextFocusRightId, node.getNextFocusRightId());
                propertyReader.readResourceId(this.mNextFocusUpId, node.getNextFocusUpId());
                propertyReader.readColor(this.mOutlineAmbientShadowColorId, node.getOutlineAmbientShadowColor());
                propertyReader.readObject(this.mOutlineProviderId, node.getOutlineProvider());
                propertyReader.readColor(this.mOutlineSpotShadowColorId, node.getOutlineSpotShadowColor());
                propertyReader.readIntEnum(this.mOverScrollModeId, node.getOverScrollMode());
                propertyReader.readInt(this.mPaddingBottomId, node.getPaddingBottom());
                propertyReader.readInt(this.mPaddingLeftId, node.getPaddingLeft());
                propertyReader.readInt(this.mPaddingRightId, node.getPaddingRight());
                propertyReader.readInt(this.mPaddingTopId, node.getPaddingTop());
                propertyReader.readObject(this.mPointerIconId, node.getPointerIcon());
                propertyReader.readBoolean(this.mPressedId, node.isPressed());
                propertyReader.readIntEnum(this.mRawLayoutDirectionId, node.getRawLayoutDirection());
                propertyReader.readIntEnum(this.mRawTextAlignmentId, node.getRawTextAlignment());
                propertyReader.readIntEnum(this.mRawTextDirectionId, node.getRawTextDirection());
                propertyReader.readIntFlag(this.mRequiresFadingEdgeId, node.getFadingEdge());
                propertyReader.readFloat(this.mRotationId, node.getRotation());
                propertyReader.readFloat(this.mRotationXId, node.getRotationX());
                propertyReader.readFloat(this.mRotationYId, node.getRotationY());
                propertyReader.readBoolean(this.mSaveEnabledId, node.isSaveEnabled());
                propertyReader.readFloat(this.mScaleXId, node.getScaleX());
                propertyReader.readFloat(this.mScaleYId, node.getScaleY());
                propertyReader.readBoolean(this.mScreenReaderFocusableId, node.isScreenReaderFocusable());
                propertyReader.readIntFlag(this.mScrollIndicatorsId, node.getScrollIndicators());
                propertyReader.readInt(this.mScrollXId, node.getScrollX());
                propertyReader.readInt(this.mScrollYId, node.getScrollY());
                propertyReader.readInt(this.mScrollbarDefaultDelayBeforeFadeId, node.getScrollBarDefaultDelayBeforeFade());
                propertyReader.readInt(this.mScrollbarFadeDurationId, node.getScrollBarFadeDuration());
                propertyReader.readInt(this.mScrollbarSizeId, node.getScrollBarSize());
                propertyReader.readIntEnum(this.mScrollbarStyleId, node.getScrollBarStyle());
                propertyReader.readBoolean(this.mSelectedId, node.isSelected());
                propertyReader.readColor(this.mSolidColorId, node.getSolidColor());
                propertyReader.readBoolean(this.mSoundEffectsEnabledId, node.isSoundEffectsEnabled());
                propertyReader.readObject(this.mStateListAnimatorId, node.getStateListAnimator());
                propertyReader.readObject(this.mTagId, node.getTag());
                propertyReader.readIntEnum(this.mTextAlignmentId, node.getTextAlignment());
                propertyReader.readIntEnum(this.mTextDirectionId, node.getTextDirection());
                propertyReader.readObject(this.mTooltipTextId, node.getTooltipText());
                propertyReader.readFloat(this.mTransformPivotXId, node.getPivotX());
                propertyReader.readFloat(this.mTransformPivotYId, node.getPivotY());
                propertyReader.readObject(this.mTransitionNameId, node.getTransitionName());
                propertyReader.readFloat(this.mTranslationXId, node.getTranslationX());
                propertyReader.readFloat(this.mTranslationYId, node.getTranslationY());
                propertyReader.readFloat(this.mTranslationZId, node.getTranslationZ());
                propertyReader.readIntEnum(this.mVisibilityId, node.getVisibility());
                return;
            }
            throw new UninitializedPropertyMapException();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface LayerType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface LayoutDir {
    }

    static class ListenerInfo {
        OnApplyWindowInsetsListener mOnApplyWindowInsetsListener;
        private CopyOnWriteArrayList<OnAttachStateChangeListener> mOnAttachStateChangeListeners;
        OnCapturedPointerListener mOnCapturedPointerListener;
        @UnsupportedAppUsage
        public OnClickListener mOnClickListener;
        protected OnContextClickListener mOnContextClickListener;
        @UnsupportedAppUsage
        protected OnCreateContextMenuListener mOnCreateContextMenuListener;
        @UnsupportedAppUsage
        private OnDragListener mOnDragListener;
        @UnsupportedAppUsage
        protected OnFocusChangeListener mOnFocusChangeListener;
        @UnsupportedAppUsage
        private OnGenericMotionListener mOnGenericMotionListener;
        @UnsupportedAppUsage
        private OnHoverListener mOnHoverListener;
        @UnsupportedAppUsage
        private OnKeyListener mOnKeyListener;
        private ArrayList<OnLayoutChangeListener> mOnLayoutChangeListeners;
        @UnsupportedAppUsage
        protected OnLongClickListener mOnLongClickListener;
        protected OnScrollChangeListener mOnScrollChangeListener;
        private OnSystemUiVisibilityChangeListener mOnSystemUiVisibilityChangeListener;
        @UnsupportedAppUsage
        private OnTouchListener mOnTouchListener;
        public PositionUpdateListener mPositionUpdateListener;
        private List<Rect> mSystemGestureExclusionRects;
        private ArrayList<OnUnhandledKeyEventListener> mUnhandledKeyListeners;
        private WindowInsetsAnimationListener mWindowInsetsAnimationListener;

        ListenerInfo() {
        }
    }

    private static class MatchIdPredicate implements Predicate<View> {
        public int mId;

        private MatchIdPredicate() {
        }

        /* synthetic */ MatchIdPredicate(AnonymousClass1 x0) {
            this();
        }

        public boolean test(View view) {
            return view.mID == this.mId;
        }
    }

    private static class MatchLabelForPredicate implements Predicate<View> {
        private int mLabeledId;

        private MatchLabelForPredicate() {
        }

        /* synthetic */ MatchLabelForPredicate(AnonymousClass1 x0) {
            this();
        }

        public boolean test(View view) {
            return view.mLabelForId == this.mLabeledId;
        }
    }

    public static class MeasureSpec {
        public static final int AT_MOST = Integer.MIN_VALUE;
        public static final int EXACTLY = 1073741824;
        private static final int MODE_MASK = -1073741824;
        private static final int MODE_SHIFT = 30;
        public static final int UNSPECIFIED = 0;

        @Retention(RetentionPolicy.SOURCE)
        public @interface MeasureSpecMode {
        }

        public static int makeMeasureSpec(int size, int mode) {
            if (View.sUseBrokenMakeMeasureSpec) {
                return size + mode;
            }
            return (View.LAST_APP_AUTOFILL_ID & size) | (-1073741824 & mode);
        }

        @UnsupportedAppUsage
        public static int makeSafeMeasureSpec(int size, int mode) {
            if (View.sUseZeroUnspecifiedMeasureSpec && mode == 0) {
                return 0;
            }
            return makeMeasureSpec(size, mode);
        }

        public static int getMode(int measureSpec) {
            return -1073741824 & measureSpec;
        }

        public static int getSize(int measureSpec) {
            return View.LAST_APP_AUTOFILL_ID & measureSpec;
        }

        static int adjust(int measureSpec, int delta) {
            int mode = getMode(measureSpec);
            int size = getSize(measureSpec);
            if (mode == 0) {
                return makeMeasureSpec(size, 0);
            }
            size += delta;
            if (size < 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("MeasureSpec.adjust: new size would be negative! (");
                stringBuilder.append(size);
                stringBuilder.append(") spec: ");
                stringBuilder.append(toString(measureSpec));
                stringBuilder.append(" delta: ");
                stringBuilder.append(delta);
                Log.e(View.VIEW_LOG_TAG, stringBuilder.toString());
                size = 0;
            }
            return makeMeasureSpec(size, mode);
        }

        public static String toString(int measureSpec) {
            int mode = getMode(measureSpec);
            int size = getSize(measureSpec);
            StringBuilder sb = new StringBuilder("MeasureSpec: ");
            if (mode == 0) {
                sb.append("UNSPECIFIED ");
            } else if (mode == 1073741824) {
                sb.append("EXACTLY ");
            } else if (mode == Integer.MIN_VALUE) {
                sb.append("AT_MOST ");
            } else {
                sb.append(mode);
                sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            }
            sb.append(size);
            return sb.toString();
        }
    }

    public interface OnApplyWindowInsetsListener {
        WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets);
    }

    public interface OnCapturedPointerListener {
        boolean onCapturedPointer(View view, MotionEvent motionEvent);
    }

    public interface OnContextClickListener {
        boolean onContextClick(View view);
    }

    public interface OnCreateContextMenuListener {
        void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenuInfo contextMenuInfo);
    }

    public interface OnDragListener {
        boolean onDrag(View view, DragEvent dragEvent);
    }

    public interface OnFocusChangeListener {
        void onFocusChange(View view, boolean z);
    }

    public interface OnGenericMotionListener {
        boolean onGenericMotion(View view, MotionEvent motionEvent);
    }

    public interface OnHoverListener {
        boolean onHover(View view, MotionEvent motionEvent);
    }

    public interface OnKeyListener {
        boolean onKey(View view, int i, KeyEvent keyEvent);
    }

    public interface OnLayoutChangeListener {
        void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8);
    }

    public interface OnLongClickListener {
        boolean onLongClick(View view);
    }

    public interface OnScrollChangeListener {
        void onScrollChange(View view, int i, int i2, int i3, int i4);
    }

    public interface OnSystemUiVisibilityChangeListener {
        void onSystemUiVisibilityChange(int i);
    }

    public interface OnUnhandledKeyEventListener {
        boolean onUnhandledKeyEvent(View view, KeyEvent keyEvent);
    }

    private final class PerformClick implements Runnable {
        private PerformClick() {
        }

        /* synthetic */ PerformClick(View x0, AnonymousClass1 x1) {
            this();
        }

        public void run() {
            View.this.recordGestureClassification(1);
            View.this.performClickInternal();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ResolvedLayoutDir {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ScrollBarStyle {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ScrollIndicators {
    }

    private static class ScrollabilityCache implements Runnable {
        public static final int DRAGGING_HORIZONTAL_SCROLL_BAR = 2;
        public static final int DRAGGING_VERTICAL_SCROLL_BAR = 1;
        public static final int FADING = 2;
        public static final int NOT_DRAGGING = 0;
        public static final int OFF = 0;
        public static final int ON = 1;
        private static final float[] OPAQUE = new float[]{255.0f};
        private static final float[] TRANSPARENT = new float[]{0.0f};
        public boolean fadeScrollBars;
        public long fadeStartTime;
        public int fadingEdgeLength;
        @UnsupportedAppUsage
        public View host;
        public float[] interpolatorValues;
        private int mLastColor;
        public final Rect mScrollBarBounds = new Rect();
        public float mScrollBarDraggingPos = 0.0f;
        public int mScrollBarDraggingState = 0;
        public final Rect mScrollBarTouchBounds = new Rect();
        public final Matrix matrix;
        public final Paint paint;
        @UnsupportedAppUsage
        public ScrollBarDrawable scrollBar;
        public int scrollBarDefaultDelayBeforeFade;
        public int scrollBarFadeDuration;
        public final Interpolator scrollBarInterpolator = new Interpolator(1, 2);
        public int scrollBarMinTouchTarget;
        public int scrollBarSize;
        public Shader shader;
        @UnsupportedAppUsage
        public int state = 0;

        public ScrollabilityCache(ViewConfiguration configuration, View host) {
            this.fadingEdgeLength = configuration.getScaledFadingEdgeLength();
            this.scrollBarSize = configuration.getScaledScrollBarSize();
            this.scrollBarMinTouchTarget = configuration.getScaledMinScrollbarTouchTarget();
            this.scrollBarDefaultDelayBeforeFade = ViewConfiguration.getScrollDefaultDelay();
            this.scrollBarFadeDuration = ViewConfiguration.getScrollBarFadeDuration();
            this.paint = new Paint();
            this.matrix = new Matrix();
            this.shader = new LinearGradient(0.0f, 0.0f, 0.0f, 1.0f, -16777216, 0, TileMode.CLAMP);
            this.paint.setShader(this.shader);
            this.paint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
            this.host = host;
        }

        public void setFadeColor(int color) {
            if (color != this.mLastColor) {
                this.mLastColor = color;
                if (color != 0) {
                    this.shader = new LinearGradient(0.0f, 0.0f, 0.0f, 1.0f, color | -16777216, color & 16777215, TileMode.CLAMP);
                    this.paint.setShader(this.shader);
                    this.paint.setXfermode(null);
                    return;
                }
                this.shader = new LinearGradient(0.0f, 0.0f, 0.0f, 1.0f, -16777216, 0, TileMode.CLAMP);
                this.paint.setShader(this.shader);
                this.paint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
            }
        }

        public void run() {
            long now = AnimationUtils.currentAnimationTimeMillis();
            if (now >= this.fadeStartTime) {
                int nextFrame = (int) now;
                Interpolator interpolator = this.scrollBarInterpolator;
                int framesCount = 0 + 1;
                interpolator.setKeyFrame(0, nextFrame, OPAQUE);
                interpolator.setKeyFrame(framesCount, nextFrame + this.scrollBarFadeDuration, TRANSPARENT);
                this.state = 2;
                this.host.invalidate(true);
            }
        }
    }

    private class SendViewScrolledAccessibilityEvent implements Runnable {
        public int mDeltaX;
        public int mDeltaY;
        public volatile boolean mIsPending;

        private SendViewScrolledAccessibilityEvent() {
        }

        /* synthetic */ SendViewScrolledAccessibilityEvent(View x0, AnonymousClass1 x1) {
            this();
        }

        public void post(int dx, int dy) {
            this.mDeltaX += dx;
            this.mDeltaY += dy;
            if (!this.mIsPending) {
                this.mIsPending = true;
                View.this.postDelayed(this, ViewConfiguration.getSendRecurringAccessibilityEventsInterval());
            }
        }

        public void run() {
            if (AccessibilityManager.getInstance(View.this.mContext).isEnabled()) {
                AccessibilityEvent event = AccessibilityEvent.obtain(4096);
                event.setScrollDeltaX(this.mDeltaX);
                event.setScrollDeltaY(this.mDeltaY);
                View.this.sendAccessibilityEventUnchecked(event);
            }
            reset();
        }

        private void reset() {
            this.mIsPending = false;
            this.mDeltaX = 0;
            this.mDeltaY = 0;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TextAlignment {
    }

    static class TintInfo {
        BlendMode mBlendMode;
        boolean mHasTintList;
        boolean mHasTintMode;
        ColorStateList mTintList;

        TintInfo() {
        }
    }

    private static class TooltipInfo {
        int mAnchorX;
        int mAnchorY;
        Runnable mHideTooltipRunnable;
        int mHoverSlop;
        Runnable mShowTooltipRunnable;
        boolean mTooltipFromLongClick;
        TooltipPopup mTooltipPopup;
        CharSequence mTooltipText;

        private TooltipInfo() {
        }

        /* synthetic */ TooltipInfo(AnonymousClass1 x0) {
            this();
        }

        private boolean updateAnchorPos(MotionEvent event) {
            int newAnchorX = (int) event.getX();
            int newAnchorY = (int) event.getY();
            if (Math.abs(newAnchorX - this.mAnchorX) <= this.mHoverSlop && Math.abs(newAnchorY - this.mAnchorY) <= this.mHoverSlop) {
                return false;
            }
            this.mAnchorX = newAnchorX;
            this.mAnchorY = newAnchorY;
            return true;
        }

        private void clearAnchorPos() {
            this.mAnchorX = Integer.MAX_VALUE;
            this.mAnchorY = Integer.MAX_VALUE;
        }
    }

    static class TransformationInfo {
        @ExportedProperty
        private float mAlpha = 1.0f;
        private Matrix mInverseMatrix;
        private final Matrix mMatrix = new Matrix();
        float mTransitionAlpha = 1.0f;

        TransformationInfo() {
        }
    }

    private final class UnsetPressedState implements Runnable {
        private UnsetPressedState() {
        }

        /* synthetic */ UnsetPressedState(View x0, AnonymousClass1 x1) {
            this();
        }

        public void run() {
            View.this.setPressed(false);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewStructureType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Visibility {
    }

    private static class VisibilityChangeForAutofillHandler extends Handler {
        private final AutofillManager mAfm;
        private final View mView;

        /* synthetic */ VisibilityChangeForAutofillHandler(AutofillManager x0, View x1, AnonymousClass1 x2) {
            this(x0, x1);
        }

        private VisibilityChangeForAutofillHandler(AutofillManager afm, View view) {
            this.mAfm = afm;
            this.mView = view;
        }

        public void handleMessage(Message msg) {
            AutofillManager autofillManager = this.mAfm;
            View view = this.mView;
            autofillManager.notifyViewVisibilityChanged(view, view.isShown());
        }
    }

    static {
        Extension.get().bindOriginal(new Interface() {
            public void refreshDrawableState(View view) {
                view.originalRefreshDrawableState();
            }

            public int[] onCreateDrawableState(View view, int i) {
                return view.originalOnCreateDrawableState(i);
            }

            public void init(View view, Context context, AttributeSet attributeSet, int i, int i1) {
            }
        });
    }

    public View(Context context) {
        Resources resources = null;
        this.mCurrentAnimation = null;
        boolean z = false;
        this.mRecreateDisplayList = false;
        this.mID = -1;
        this.mAutofillViewId = -1;
        this.mAccessibilityViewId = -1;
        this.mAccessibilityCursorPosition = -1;
        this.mTag = null;
        this.mTransientStateCount = 0;
        this.mClipBounds = null;
        this.mPaddingLeft = 0;
        this.mPaddingRight = 0;
        this.mLabelForId = -1;
        this.mAccessibilityTraversalBeforeId = -1;
        this.mAccessibilityTraversalAfterId = -1;
        this.mLeftPaddingDefined = false;
        this.mRightPaddingDefined = false;
        this.mOldWidthMeasureSpec = Integer.MIN_VALUE;
        this.mOldHeightMeasureSpec = Integer.MIN_VALUE;
        this.mLongClickX = Float.NaN;
        this.mLongClickY = Float.NaN;
        this.mDrawableState = null;
        this.mOutlineProvider = ViewOutlineProvider.BACKGROUND;
        this.mNextFocusLeftId = -1;
        this.mNextFocusRightId = -1;
        this.mNextFocusUpId = -1;
        this.mNextFocusDownId = -1;
        this.mNextFocusForwardId = -1;
        this.mNextClusterForwardId = -1;
        this.mDefaultFocusHighlightEnabled = true;
        this.mPendingCheckForTap = null;
        this.mTouchDelegate = null;
        this.mHoveringTouchDelegate = false;
        this.mDrawingCacheBackgroundColor = 0;
        this.mAnimator = null;
        this.mLayerType = 0;
        this.mInputEventConsistencyVerifier = InputEventConsistencyVerifier.isInstrumentationEnabled() ? new InputEventConsistencyVerifier(this, 0) : null;
        this.mSourceLayoutId = 0;
        this.mFirst = true;
        this.mIsWebView = false;
        this.mContext = context;
        if (context != null) {
            resources = context.getResources();
        }
        this.mResources = resources;
        this.mViewFlags = 402653200;
        this.mPrivateFlags2 = 140296;
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setOverScrollMode(1);
        this.mUserPaddingStart = Integer.MIN_VALUE;
        this.mUserPaddingEnd = Integer.MIN_VALUE;
        this.mRenderNode = RenderNode.create(getClass().getName(), new ViewAnimationHostBridge(this));
        if (!(sCompatibilityDone || context == null)) {
            int targetSdkVersion = context.getApplicationInfo().targetSdkVersion;
            sUseBrokenMakeMeasureSpec = targetSdkVersion <= 17;
            sIgnoreMeasureCache = targetSdkVersion < 19;
            Canvas.sCompatibilityRestore = targetSdkVersion < 23;
            Canvas.sCompatibilitySetBitmap = targetSdkVersion < 26;
            Canvas.setCompatibilityVersion(targetSdkVersion);
            sUseZeroUnspecifiedMeasureSpec = targetSdkVersion < 23;
            sAlwaysRemeasureExactly = targetSdkVersion <= 23;
            sTextureViewIgnoresDrawableSetters = targetSdkVersion <= 23;
            sPreserveMarginParamsInLayoutParamConversion = targetSdkVersion >= 24;
            sCascadedDragDrop = targetSdkVersion < 24;
            sHasFocusableExcludeAutoFocusable = targetSdkVersion < 26;
            sAutoFocusableOffUIThreadWontNotifyParents = targetSdkVersion < 26;
            sUseDefaultFocusHighlight = context.getResources().getBoolean(R.bool.config_useDefaultFocusHighlight);
            sThrowOnInvalidFloatProperties = targetSdkVersion >= 28;
            sCanFocusZeroSized = targetSdkVersion < 28;
            sAlwaysAssignFocus = targetSdkVersion < 28;
            sAcceptZeroSizeDragShadow = targetSdkVersion < 28;
            boolean z2 = ViewRootImpl.sNewInsetsMode != 2 || targetSdkVersion < 29;
            sBrokenInsetsDispatch = z2;
            if (targetSdkVersion < 29) {
                z = true;
            }
            sBrokenWindowBackground = z;
            sCompatibilityDone = true;
        }
        this.mIsWebView = InterceptorProxy.checkAndInitWebView(this, Looper.myLooper());
    }

    public View(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public View(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    /* JADX WARNING: Missing block: B:199:0x0725, code skipped:
            if (r7 >= 14) goto L_0x08b8;
     */
    public View(android.content.Context r52, android.util.AttributeSet r53, int r54, int r55) {
        /*
        r51 = this;
        r8 = r51;
        r9 = r52;
        r10 = r53;
        r51.<init>(r52);
        r0 = android.content.res.Resources.getAttributeSetSourceResId(r53);
        r8.mSourceLayoutId = r0;
        r0 = com.android.internal.R.styleable.View;
        r11 = r54;
        r12 = r55;
        r13 = r9.obtainStyledAttributes(r10, r0, r11, r12);
        r0 = r52.getTheme();
        r8.retrieveExplicitStyle(r0, r10);
        r3 = com.android.internal.R.styleable.View;
        r1 = r51;
        r2 = r52;
        r4 = r53;
        r5 = r13;
        r6 = r54;
        r7 = r55;
        r1.saveAttributeDataForStyleable(r2, r3, r4, r5, r6, r7);
        r0 = sDebugViewAttributes;
        if (r0 == 0) goto L_0x0037;
    L_0x0034:
        r8.saveAttributeData(r10, r13);
    L_0x0037:
        r0 = 0;
        r1 = -1;
        r2 = -1;
        r3 = -1;
        r4 = -1;
        r5 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r6 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r7 = -1;
        r14 = -1;
        r15 = -1;
        r16 = 0;
        r17 = 0;
        r18 = 0;
        r19 = 0;
        r20 = 0;
        r21 = 0;
        r22 = 0;
        r23 = 0;
        r24 = 0;
        r25 = 0;
        r26 = 0;
        r27 = 0;
        r28 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r29 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r30 = 0;
        r31 = 0;
        r32 = r0;
        r0 = r8.mOverScrollMode;
        r33 = 0;
        r34 = 0;
        r35 = 0;
        r36 = 0;
        r37 = 0;
        r38 = 0;
        r39 = r0;
        r0 = r52.getApplicationInfo();
        r40 = r7;
        r7 = r0.targetSdkVersion;
        r0 = r16 | 16;
        r16 = r17 | 16;
        r10 = r13.getIndexCount();
        r17 = 0;
        r12 = r0;
        r11 = r16;
        r41 = r21;
        r42 = r22;
        r48 = r23;
        r49 = r24;
        r45 = r25;
        r46 = r26;
        r47 = r27;
        r43 = r28;
        r44 = r29;
        r16 = r1;
        r24 = r14;
        r23 = r15;
        r1 = r17;
        r21 = r18;
        r22 = r19;
        r14 = r39;
        r17 = r2;
        r18 = r3;
        r19 = r4;
        r15 = r5;
        r5 = r32;
        r4 = r37;
        r3 = r38;
        r2 = r40;
    L_0x00b9:
        if (r1 >= r10) goto L_0x08c4;
    L_0x00bb:
        r26 = r10;
        r10 = r13.getIndex(r1);
        r0 = 109; // 0x6d float:1.53E-43 double:5.4E-322;
        r28 = r2;
        if (r10 == r0) goto L_0x0893;
    L_0x00c7:
        switch(r10) {
            case 8: goto L_0x086e;
            case 9: goto L_0x0862;
            case 10: goto L_0x0857;
            case 11: goto L_0x0848;
            case 12: goto L_0x0839;
            case 13: goto L_0x082c;
            case 14: goto L_0x0819;
            case 15: goto L_0x0806;
            case 16: goto L_0x07f7;
            case 17: goto L_0x07e4;
            case 18: goto L_0x07d5;
            case 19: goto L_0x07b1;
            case 20: goto L_0x0792;
            case 21: goto L_0x0776;
            case 22: goto L_0x075d;
            case 23: goto L_0x0741;
            case 24: goto L_0x071f;
            default: goto L_0x00ca;
        };
    L_0x00ca:
        switch(r10) {
            case 26: goto L_0x0712;
            case 27: goto L_0x0705;
            case 28: goto L_0x06f8;
            case 29: goto L_0x06eb;
            case 30: goto L_0x06d2;
            case 31: goto L_0x06b8;
            case 32: goto L_0x069e;
            case 33: goto L_0x0681;
            case 34: goto L_0x0666;
            case 35: goto L_0x0651;
            case 36: goto L_0x0644;
            case 37: goto L_0x0637;
            case 38: goto L_0x0621;
            case 39: goto L_0x0605;
            case 40: goto L_0x05ea;
            case 41: goto L_0x05d0;
            case 42: goto L_0x05ad;
            case 43: goto L_0x058b;
            case 44: goto L_0x057e;
            default: goto L_0x00cd;
        };
    L_0x00cd:
        r2 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
        r0 = 0;
        switch(r10) {
            case 48: goto L_0x0570;
            case 49: goto L_0x0557;
            case 50: goto L_0x0548;
            case 51: goto L_0x053b;
            case 52: goto L_0x052e;
            case 53: goto L_0x051d;
            case 54: goto L_0x050c;
            case 55: goto L_0x04f9;
            case 56: goto L_0x04e6;
            case 57: goto L_0x04d5;
            case 58: goto L_0x04c4;
            case 59: goto L_0x04b3;
            case 60: goto L_0x04a6;
            case 61: goto L_0x0499;
            case 62: goto L_0x048a;
            case 63: goto L_0x0483;
            case 64: goto L_0x0475;
            case 65: goto L_0x0459;
            case 66: goto L_0x043d;
            case 67: goto L_0x041d;
            case 68: goto L_0x03ff;
            case 69: goto L_0x03e1;
            case 70: goto L_0x03d3;
            case 71: goto L_0x03c5;
            case 72: goto L_0x03b4;
            case 73: goto L_0x03a7;
            case 74: goto L_0x0399;
            case 75: goto L_0x0388;
            case 76: goto L_0x0376;
            case 77: goto L_0x0356;
            case 78: goto L_0x0330;
            case 79: goto L_0x031b;
            case 80: goto L_0x02ff;
            case 81: goto L_0x02ef;
            case 82: goto L_0x02e1;
            case 83: goto L_0x02d3;
            case 84: goto L_0x02b4;
            case 85: goto L_0x0299;
            case 86: goto L_0x0270;
            case 87: goto L_0x025c;
            case 88: goto L_0x024f;
            case 89: goto L_0x0239;
            case 90: goto L_0x022a;
            case 91: goto L_0x0216;
            case 92: goto L_0x0209;
            case 93: goto L_0x01f5;
            case 94: goto L_0x0178;
            case 95: goto L_0x015e;
            case 96: goto L_0x0144;
            case 97: goto L_0x012a;
            case 98: goto L_0x0111;
            case 99: goto L_0x0103;
            case 100: goto L_0x00f6;
            case 101: goto L_0x00e9;
            case 102: goto L_0x00d9;
            default: goto L_0x00d3;
        };
    L_0x00d3:
        r40 = r3;
        r50 = r4;
        goto L_0x08b8;
    L_0x00d9:
        r0 = r8.mRenderNode;
        r2 = 1;
        r2 = r13.getBoolean(r10, r2);
        r0.setForceDarkAllowed(r2);
        r40 = r3;
        r50 = r4;
        goto L_0x08b8;
    L_0x00e9:
        r0 = r13.getColor(r10, r2);
        r8.setOutlineAmbientShadowColor(r0);
        r40 = r3;
        r50 = r4;
        goto L_0x08b8;
    L_0x00f6:
        r0 = r13.getColor(r10, r2);
        r8.setOutlineSpotShadowColor(r0);
        r40 = r3;
        r50 = r4;
        goto L_0x08b8;
    L_0x0103:
        r0 = 0;
        r0 = r13.getBoolean(r10, r0);
        r8.setAccessibilityHeading(r0);
        r40 = r3;
        r50 = r4;
        goto L_0x08b8;
    L_0x0111:
        r0 = r13.peekValue(r10);
        if (r0 == 0) goto L_0x0124;
    L_0x0117:
        r0 = r13.getString(r10);
        r8.setAccessibilityPaneTitle(r0);
        r40 = r3;
        r50 = r4;
        goto L_0x08b8;
    L_0x0124:
        r40 = r3;
        r50 = r4;
        goto L_0x08b8;
    L_0x012a:
        r0 = r13.peekValue(r10);
        if (r0 == 0) goto L_0x013e;
    L_0x0130:
        r0 = 0;
        r0 = r13.getBoolean(r10, r0);
        r8.setScreenReaderFocusable(r0);
        r40 = r3;
        r50 = r4;
        goto L_0x08b8;
    L_0x013e:
        r40 = r3;
        r50 = r4;
        goto L_0x08b8;
    L_0x0144:
        r0 = r13.peekValue(r10);
        if (r0 == 0) goto L_0x0158;
    L_0x014a:
        r0 = 1;
        r0 = r13.getBoolean(r10, r0);
        r8.setDefaultFocusHighlightEnabled(r0);
        r40 = r3;
        r50 = r4;
        goto L_0x08b8;
    L_0x0158:
        r40 = r3;
        r50 = r4;
        goto L_0x08b8;
    L_0x015e:
        r0 = r13.peekValue(r10);
        if (r0 == 0) goto L_0x0172;
    L_0x0164:
        r0 = 0;
        r0 = r13.getInt(r10, r0);
        r8.setImportantForAutofill(r0);
        r40 = r3;
        r50 = r4;
        goto L_0x08b8;
    L_0x0172:
        r40 = r3;
        r50 = r4;
        goto L_0x08b8;
    L_0x0178:
        r0 = r13.peekValue(r10);
        if (r0 == 0) goto L_0x01ef;
    L_0x017e:
        r2 = 0;
        r29 = 0;
        r0 = r13.getType(r10);
        r32 = r2;
        r2 = 1;
        if (r0 != r2) goto L_0x01aa;
    L_0x018a:
        r0 = 0;
        r2 = r13.getResourceId(r10, r0);
        r0 = r13.getTextArray(r10);	 Catch:{ NotFoundException -> 0x0195 }
        r2 = r0;
        goto L_0x01a5;
    L_0x0195:
        r0 = move-exception;
        r25 = r0;
        r0 = r25;
        r0 = r51.getResources();
        r29 = r0.getString(r2);
        r2 = r32;
    L_0x01a5:
        r32 = r2;
        r0 = r29;
        goto L_0x01b0;
    L_0x01aa:
        r29 = r13.getString(r10);
        r0 = r29;
    L_0x01b0:
        if (r32 != 0) goto L_0x01c9;
    L_0x01b2:
        if (r0 == 0) goto L_0x01bf;
    L_0x01b4:
        r2 = ",";
        r32 = r0.split(r2);
        r25 = r0;
        r0 = r32;
        goto L_0x01cd;
    L_0x01bf:
        r2 = new java.lang.IllegalArgumentException;
        r25 = r0;
        r0 = "Could not resolve autofillHints";
        r2.<init>(r0);
        throw r2;
    L_0x01c9:
        r25 = r0;
        r0 = r32;
    L_0x01cd:
        r2 = r0.length;
        r2 = new java.lang.String[r2];
        r40 = r3;
        r3 = r0.length;
        r27 = 0;
        r50 = r4;
        r4 = r27;
    L_0x01d9:
        if (r4 >= r3) goto L_0x01ea;
    L_0x01db:
        r27 = r0[r4];
        r27 = r27.toString();
        r27 = r27.trim();
        r2[r4] = r27;
        r4 = r4 + 1;
        goto L_0x01d9;
    L_0x01ea:
        r8.setAutofillHints(r2);
        goto L_0x08b8;
    L_0x01ef:
        r40 = r3;
        r50 = r4;
        goto L_0x08b8;
    L_0x01f5:
        r40 = r3;
        r50 = r4;
        r0 = r13.peekValue(r10);
        if (r0 == 0) goto L_0x08b8;
    L_0x01ff:
        r0 = 1;
        r0 = r13.getBoolean(r10, r0);
        r8.setFocusedByDefault(r0);
        goto L_0x08b8;
    L_0x0209:
        r40 = r3;
        r50 = r4;
        r0 = -1;
        r0 = r13.getResourceId(r10, r0);
        r8.mNextClusterForwardId = r0;
        goto L_0x08b8;
    L_0x0216:
        r40 = r3;
        r50 = r4;
        r0 = r13.peekValue(r10);
        if (r0 == 0) goto L_0x08b8;
    L_0x0220:
        r0 = 1;
        r0 = r13.getBoolean(r10, r0);
        r8.setKeyboardNavigationCluster(r0);
        goto L_0x08b8;
    L_0x022a:
        r40 = r3;
        r50 = r4;
        r0 = -1;
        r0 = r13.getDimensionPixelSize(r10, r0);
        r23 = r0;
        r2 = r28;
        goto L_0x08be;
    L_0x0239:
        r40 = r3;
        r50 = r4;
        r0 = -1;
        r0 = r13.getDimensionPixelSize(r10, r0);
        r8.mUserPaddingLeftInitial = r0;
        r8.mUserPaddingRightInitial = r0;
        r2 = 1;
        r3 = 1;
        r24 = r0;
        r4 = r2;
        r2 = r28;
        goto L_0x08be;
    L_0x024f:
        r40 = r3;
        r50 = r4;
        r0 = r13.getText(r10);
        r8.setTooltipText(r0);
        goto L_0x08b8;
    L_0x025c:
        r40 = r3;
        r50 = r4;
        r0 = r13.peekValue(r10);
        if (r0 == 0) goto L_0x08b8;
    L_0x0266:
        r0 = 1;
        r0 = r13.getBoolean(r10, r0);
        r8.forceHasOverlappingRendering(r0);
        goto L_0x08b8;
    L_0x0270:
        r40 = r3;
        r50 = r4;
        r0 = 0;
        r0 = r13.getResourceId(r10, r0);
        if (r0 == 0) goto L_0x0289;
        r2 = r52.getResources();
        r2 = android.view.PointerIcon.load(r2, r0);
        r8.setPointerIcon(r2);
        goto L_0x08b8;
    L_0x0289:
        r2 = 1;
        r3 = r13.getInt(r10, r2);
        if (r3 == r2) goto L_0x0297;
    L_0x0290:
        r2 = android.view.PointerIcon.getSystemIcon(r9, r3);
        r8.setPointerIcon(r2);
    L_0x0297:
        goto L_0x08b8;
    L_0x0299:
        r40 = r3;
        r50 = r4;
        r0 = 0;
        r0 = r13.getBoolean(r10, r0);
        if (r0 == 0) goto L_0x08b8;
    L_0x02a4:
        r0 = 8388608; // 0x800000 float:1.17549435E-38 double:4.144523E-317;
        r0 = r0 | r12;
        r2 = 8388608; // 0x800000 float:1.17549435E-38 double:4.144523E-317;
        r2 = r2 | r11;
        r12 = r0;
        r11 = r2;
        r2 = r28;
        r3 = r40;
        r4 = r50;
        goto L_0x08be;
    L_0x02b4:
        r40 = r3;
        r50 = r4;
        r0 = 0;
        r0 = r13.getInt(r10, r0);
        r0 = r0 << 8;
        r0 = r0 & 16128;
        if (r0 == 0) goto L_0x08b8;
    L_0x02c3:
        r2 = r8.mPrivateFlags3;
        r2 = r2 | r0;
        r8.mPrivateFlags3 = r2;
        r2 = 1;
        r34 = r2;
        r2 = r28;
        r3 = r40;
        r4 = r50;
        goto L_0x08be;
    L_0x02d3:
        r40 = r3;
        r50 = r4;
        r0 = -1;
        r0 = r13.getResourceId(r10, r0);
        r8.setAccessibilityTraversalAfter(r0);
        goto L_0x08b8;
    L_0x02e1:
        r40 = r3;
        r50 = r4;
        r0 = -1;
        r0 = r13.getResourceId(r10, r0);
        r8.setAccessibilityTraversalBefore(r0);
        goto L_0x08b8;
    L_0x02ef:
        r40 = r3;
        r50 = r4;
        r0 = 81;
        r2 = 0;
        r0 = r13.getInt(r0, r2);
        r8.setOutlineProviderFromAttribute(r0);
        goto L_0x08b8;
    L_0x02ff:
        r40 = r3;
        r50 = r4;
        r0 = 23;
        if (r7 >= r0) goto L_0x030b;
    L_0x0307:
        r0 = r8 instanceof android.widget.FrameLayout;
        if (r0 == 0) goto L_0x08b8;
        r0 = -1;
        r0 = r13.getInt(r10, r0);
        r2 = 0;
        r0 = android.graphics.drawable.Drawable.parseBlendMode(r0, r2);
        r8.setForegroundTintBlendMode(r0);
        goto L_0x08b8;
    L_0x031b:
        r40 = r3;
        r50 = r4;
        r0 = 23;
        if (r7 >= r0) goto L_0x0327;
    L_0x0323:
        r0 = r8 instanceof android.widget.FrameLayout;
        if (r0 == 0) goto L_0x08b8;
    L_0x0327:
        r0 = r13.getColorStateList(r10);
        r8.setForegroundTintList(r0);
        goto L_0x08b8;
    L_0x0330:
        r40 = r3;
        r50 = r4;
        r0 = r8.mBackgroundTint;
        if (r0 != 0) goto L_0x033f;
    L_0x0338:
        r0 = new android.view.View$TintInfo;
        r0.<init>();
        r8.mBackgroundTint = r0;
    L_0x033f:
        r0 = r8.mBackgroundTint;
        r2 = 78;
        r3 = -1;
        r2 = r13.getInt(r2, r3);
        r3 = 0;
        r2 = android.graphics.drawable.Drawable.parseBlendMode(r2, r3);
        r0.mBlendMode = r2;
        r0 = r8.mBackgroundTint;
        r2 = 1;
        r0.mHasTintMode = r2;
        goto L_0x08b8;
    L_0x0356:
        r40 = r3;
        r50 = r4;
        r0 = r8.mBackgroundTint;
        if (r0 != 0) goto L_0x0365;
    L_0x035e:
        r0 = new android.view.View$TintInfo;
        r0.<init>();
        r8.mBackgroundTint = r0;
    L_0x0365:
        r0 = r8.mBackgroundTint;
        r2 = 77;
        r2 = r13.getColorStateList(r2);
        r0.mTintList = r2;
        r0 = r8.mBackgroundTint;
        r2 = 1;
        r0.mHasTintList = r2;
        goto L_0x08b8;
    L_0x0376:
        r40 = r3;
        r50 = r4;
        r0 = 0;
        r0 = r13.getResourceId(r10, r0);
        r0 = android.animation.AnimatorInflater.loadStateListAnimator(r9, r0);
        r8.setStateListAnimator(r0);
        goto L_0x08b8;
    L_0x0388:
        r40 = r3;
        r50 = r4;
        r0 = r13.getDimension(r10, r0);
        r2 = 1;
        r49 = r0;
        r30 = r2;
        r2 = r28;
        goto L_0x08be;
    L_0x0399:
        r40 = r3;
        r50 = r4;
        r0 = 0;
        r0 = r13.getBoolean(r10, r0);
        r8.setNestedScrollingEnabled(r0);
        goto L_0x08b8;
    L_0x03a7:
        r40 = r3;
        r50 = r4;
        r0 = r13.getString(r10);
        r8.setTransitionName(r0);
        goto L_0x08b8;
    L_0x03b4:
        r40 = r3;
        r50 = r4;
        r0 = r13.getDimension(r10, r0);
        r2 = 1;
        r48 = r0;
        r30 = r2;
        r2 = r28;
        goto L_0x08be;
    L_0x03c5:
        r40 = r3;
        r50 = r4;
        r0 = 0;
        r0 = r13.getInt(r10, r0);
        r8.setAccessibilityLiveRegion(r0);
        goto L_0x08b8;
    L_0x03d3:
        r40 = r3;
        r50 = r4;
        r0 = -1;
        r0 = r13.getResourceId(r10, r0);
        r8.setLabelFor(r0);
        goto L_0x08b8;
    L_0x03e1:
        r40 = r3;
        r50 = r4;
        r0 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r2 = r13.getDimensionPixelSize(r10, r0);
        if (r2 == r0) goto L_0x03f0;
    L_0x03ed:
        r27 = 1;
        goto L_0x03f2;
    L_0x03f0:
        r27 = 0;
    L_0x03f2:
        r0 = r27;
        r36 = r0;
        r6 = r2;
        r2 = r28;
        r3 = r40;
        r4 = r50;
        goto L_0x08be;
    L_0x03ff:
        r40 = r3;
        r50 = r4;
        r0 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r2 = r13.getDimensionPixelSize(r10, r0);
        if (r2 == r0) goto L_0x040e;
    L_0x040b:
        r27 = 1;
        goto L_0x0410;
    L_0x040e:
        r27 = 0;
    L_0x0410:
        r0 = r27;
        r35 = r0;
        r15 = r2;
        r2 = r28;
        r3 = r40;
        r4 = r50;
        goto L_0x08be;
    L_0x041d:
        r40 = r3;
        r50 = r4;
        r0 = r8.mPrivateFlags2;
        r0 = r0 & -61;
        r8.mPrivateFlags2 = r0;
        r0 = -1;
        r2 = r13.getInt(r10, r0);
        if (r2 == r0) goto L_0x0433;
    L_0x042e:
        r0 = LAYOUT_DIRECTION_FLAGS;
        r0 = r0[r2];
        goto L_0x0434;
    L_0x0433:
        r0 = 2;
    L_0x0434:
        r3 = r8.mPrivateFlags2;
        r4 = r0 << 2;
        r3 = r3 | r4;
        r8.mPrivateFlags2 = r3;
        goto L_0x08b8;
    L_0x043d:
        r40 = r3;
        r50 = r4;
        r0 = r8.mPrivateFlags2;
        r2 = -57345; // 0xffffffffffff1fff float:NaN double:NaN;
        r0 = r0 & r2;
        r8.mPrivateFlags2 = r0;
        r0 = 1;
        r0 = r13.getInt(r10, r0);
        r2 = r8.mPrivateFlags2;
        r3 = PFLAG2_TEXT_ALIGNMENT_FLAGS;
        r3 = r3[r0];
        r2 = r2 | r3;
        r8.mPrivateFlags2 = r2;
        goto L_0x08b8;
    L_0x0459:
        r40 = r3;
        r50 = r4;
        r0 = r8.mPrivateFlags2;
        r0 = r0 & -449;
        r8.mPrivateFlags2 = r0;
        r0 = -1;
        r2 = r13.getInt(r10, r0);
        if (r2 == r0) goto L_0x08b8;
    L_0x046a:
        r0 = r8.mPrivateFlags2;
        r3 = PFLAG2_TEXT_DIRECTION_FLAGS;
        r3 = r3[r2];
        r0 = r0 | r3;
        r8.mPrivateFlags2 = r0;
        goto L_0x08b8;
    L_0x0475:
        r40 = r3;
        r50 = r4;
        r0 = 0;
        r0 = r13.getInt(r10, r0);
        r8.setImportantForAccessibility(r0);
        goto L_0x08b8;
    L_0x0483:
        r40 = r3;
        r50 = r4;
        r0 = 0;
        goto L_0x0729;
    L_0x048a:
        r40 = r3;
        r50 = r4;
        r0 = 0;
        r0 = r13.getInt(r10, r0);
        r2 = 0;
        r8.setLayerType(r0, r2);
        goto L_0x08b8;
    L_0x0499:
        r40 = r3;
        r50 = r4;
        r0 = -1;
        r0 = r13.getResourceId(r10, r0);
        r8.mNextFocusForwardId = r0;
        goto L_0x08b8;
    L_0x04a6:
        r40 = r3;
        r50 = r4;
        r0 = 0;
        r0 = r13.getInt(r10, r0);
        r8.mVerticalScrollbarPosition = r0;
        goto L_0x08b8;
    L_0x04b3:
        r40 = r3;
        r50 = r4;
        r0 = r13.getFloat(r10, r0);
        r2 = 1;
        r47 = r0;
        r30 = r2;
        r2 = r28;
        goto L_0x08be;
    L_0x04c4:
        r40 = r3;
        r50 = r4;
        r0 = r13.getFloat(r10, r0);
        r2 = 1;
        r46 = r0;
        r30 = r2;
        r2 = r28;
        goto L_0x08be;
    L_0x04d5:
        r40 = r3;
        r50 = r4;
        r0 = r13.getFloat(r10, r0);
        r2 = 1;
        r45 = r0;
        r30 = r2;
        r2 = r28;
        goto L_0x08be;
    L_0x04e6:
        r40 = r3;
        r50 = r4;
        r0 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = r13.getFloat(r10, r0);
        r2 = 1;
        r44 = r0;
        r30 = r2;
        r2 = r28;
        goto L_0x08be;
    L_0x04f9:
        r40 = r3;
        r50 = r4;
        r0 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = r13.getFloat(r10, r0);
        r2 = 1;
        r43 = r0;
        r30 = r2;
        r2 = r28;
        goto L_0x08be;
    L_0x050c:
        r40 = r3;
        r50 = r4;
        r0 = r13.getDimension(r10, r0);
        r2 = 1;
        r42 = r0;
        r30 = r2;
        r2 = r28;
        goto L_0x08be;
    L_0x051d:
        r40 = r3;
        r50 = r4;
        r0 = r13.getDimension(r10, r0);
        r2 = 1;
        r41 = r0;
        r30 = r2;
        r2 = r28;
        goto L_0x08be;
    L_0x052e:
        r40 = r3;
        r50 = r4;
        r0 = r13.getDimension(r10, r0);
        r8.setPivotY(r0);
        goto L_0x08b8;
    L_0x053b:
        r40 = r3;
        r50 = r4;
        r0 = r13.getDimension(r10, r0);
        r8.setPivotX(r0);
        goto L_0x08b8;
    L_0x0548:
        r40 = r3;
        r50 = r4;
        r0 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = r13.getFloat(r10, r0);
        r8.setAlpha(r0);
        goto L_0x08b8;
    L_0x0557:
        r40 = r3;
        r50 = r4;
        r0 = 0;
        r0 = r13.getBoolean(r10, r0);
        if (r0 == 0) goto L_0x08b8;
    L_0x0562:
        r0 = r12 | 1024;
        r2 = r11 | 1024;
        r12 = r0;
        r11 = r2;
        r2 = r28;
        r3 = r40;
        r4 = r50;
        goto L_0x08be;
    L_0x0570:
        r40 = r3;
        r50 = r4;
        r0 = 1;
        r0 = r13.getInt(r10, r0);
        r14 = r0;
        r2 = r28;
        goto L_0x08be;
    L_0x057e:
        r40 = r3;
        r50 = r4;
        r0 = r13.getString(r10);
        r8.setContentDescription(r0);
        goto L_0x08b8;
    L_0x058b:
        r40 = r3;
        r50 = r4;
        r0 = r52.isRestricted();
        if (r0 != 0) goto L_0x05a5;
    L_0x0595:
        r0 = r13.getString(r10);
        if (r0 == 0) goto L_0x08b8;
    L_0x059b:
        r2 = new android.view.View$DeclaredOnClickListener;
        r2.<init>(r8, r0);
        r8.setOnClickListener(r2);
        goto L_0x08b8;
    L_0x05a5:
        r0 = new java.lang.IllegalStateException;
        r2 = "The android:onClick attribute cannot be used within a restricted context";
        r0.<init>(r2);
        throw r0;
    L_0x05ad:
        r40 = r3;
        r50 = r4;
        r0 = 0;
        r0 = r13.getBoolean(r10, r0);
        r8.mHapticEnabledExplicitly = r0;
        r0 = 1;
        r0 = r13.getBoolean(r10, r0);
        if (r0 != 0) goto L_0x08b8;
    L_0x05bf:
        r0 = -268435457; // 0xffffffffefffffff float:-1.5845632E29 double:NaN;
        r0 = r0 & r12;
        r2 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
        r2 = r2 | r11;
        r12 = r0;
        r11 = r2;
        r2 = r28;
        r3 = r40;
        r4 = r50;
        goto L_0x08be;
    L_0x05d0:
        r40 = r3;
        r50 = r4;
        r0 = 1;
        r2 = 0;
        r2 = r13.getBoolean(r10, r2);
        if (r2 == 0) goto L_0x05e0;
    L_0x05dc:
        r2 = 1;
        r8.setScrollContainer(r2);
    L_0x05e0:
        r21 = r0;
        r2 = r28;
        r3 = r40;
        r4 = r50;
        goto L_0x08be;
    L_0x05ea:
        r40 = r3;
        r50 = r4;
        r2 = 0;
        r0 = r13.getBoolean(r10, r2);
        if (r0 == 0) goto L_0x08b8;
    L_0x05f5:
        r0 = 67108864; // 0x4000000 float:1.5046328E-36 double:3.31561842E-316;
        r0 = r0 | r12;
        r2 = 67108864; // 0x4000000 float:1.5046328E-36 double:3.31561842E-316;
        r2 = r2 | r11;
        r12 = r0;
        r11 = r2;
        r2 = r28;
        r3 = r40;
        r4 = r50;
        goto L_0x08be;
    L_0x0605:
        r40 = r3;
        r50 = r4;
        r0 = 1;
        r0 = r13.getBoolean(r10, r0);
        if (r0 != 0) goto L_0x08b8;
    L_0x0610:
        r0 = -134217729; // 0xfffffffff7ffffff float:-1.0384593E34 double:NaN;
        r0 = r0 & r12;
        r2 = 134217728; // 0x8000000 float:3.85186E-34 double:6.63123685E-316;
        r2 = r2 | r11;
        r12 = r0;
        r11 = r2;
        r2 = r28;
        r3 = r40;
        r4 = r50;
        goto L_0x08be;
    L_0x0621:
        r40 = r3;
        r50 = r4;
        r0 = 23;
        if (r7 >= r0) goto L_0x062d;
    L_0x0629:
        r0 = r8 instanceof android.widget.FrameLayout;
        if (r0 == 0) goto L_0x08b8;
    L_0x062d:
        r0 = 0;
        r0 = r13.getInt(r10, r0);
        r8.setForegroundGravity(r0);
        goto L_0x08b8;
    L_0x0637:
        r40 = r3;
        r50 = r4;
        r0 = 0;
        r0 = r13.getDimensionPixelSize(r10, r0);
        r8.mMinHeight = r0;
        goto L_0x08b8;
    L_0x0644:
        r40 = r3;
        r50 = r4;
        r0 = 0;
        r0 = r13.getDimensionPixelSize(r10, r0);
        r8.mMinWidth = r0;
        goto L_0x08b8;
    L_0x0651:
        r40 = r3;
        r50 = r4;
        r0 = 23;
        if (r7 >= r0) goto L_0x065d;
    L_0x0659:
        r0 = r8 instanceof android.widget.FrameLayout;
        if (r0 == 0) goto L_0x08b8;
    L_0x065d:
        r0 = r13.getDrawable(r10);
        r8.setForeground(r0);
        goto L_0x08b8;
    L_0x0666:
        r40 = r3;
        r50 = r4;
        r0 = 0;
        r0 = r13.getBoolean(r10, r0);
        if (r0 == 0) goto L_0x08b8;
    L_0x0671:
        r0 = 4194304; // 0x400000 float:5.877472E-39 double:2.0722615E-317;
        r0 = r0 | r12;
        r2 = 4194304; // 0x400000 float:5.877472E-39 double:2.0722615E-317;
        r2 = r2 | r11;
        r12 = r0;
        r11 = r2;
        r2 = r28;
        r3 = r40;
        r4 = r50;
        goto L_0x08be;
    L_0x0681:
        r40 = r3;
        r50 = r4;
        r0 = 0;
        r0 = r13.getInt(r10, r0);
        if (r0 == 0) goto L_0x08b8;
    L_0x068c:
        r2 = DRAWING_CACHE_QUALITY_FLAGS;
        r2 = r2[r0];
        r2 = r2 | r12;
        r3 = 1572864; // 0x180000 float:2.204052E-39 double:7.77098E-318;
        r3 = r3 | r11;
        r12 = r2;
        r11 = r3;
        r2 = r28;
        r3 = r40;
        r4 = r50;
        goto L_0x08be;
    L_0x069e:
        r40 = r3;
        r50 = r4;
        r0 = 1;
        r0 = r13.getBoolean(r10, r0);
        if (r0 != 0) goto L_0x08b8;
    L_0x06a9:
        r0 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
        r2 = r12 | r0;
        r0 = r0 | r11;
        r11 = r0;
        r12 = r2;
        r2 = r28;
        r3 = r40;
        r4 = r50;
        goto L_0x08be;
    L_0x06b8:
        r40 = r3;
        r50 = r4;
        r0 = 0;
        r0 = r13.getBoolean(r10, r0);
        if (r0 == 0) goto L_0x08b8;
    L_0x06c3:
        r0 = 2097152; // 0x200000 float:2.938736E-39 double:1.0361308E-317;
        r2 = r12 | r0;
        r0 = r0 | r11;
        r11 = r0;
        r12 = r2;
        r2 = r28;
        r3 = r40;
        r4 = r50;
        goto L_0x08be;
    L_0x06d2:
        r40 = r3;
        r50 = r4;
        r0 = 0;
        r0 = r13.getBoolean(r10, r0);
        if (r0 == 0) goto L_0x08b8;
    L_0x06dd:
        r0 = r12 | 16384;
        r2 = r11 | 16384;
        r12 = r0;
        r11 = r2;
        r2 = r28;
        r3 = r40;
        r4 = r50;
        goto L_0x08be;
    L_0x06eb:
        r40 = r3;
        r50 = r4;
        r0 = -1;
        r0 = r13.getResourceId(r10, r0);
        r8.mNextFocusDownId = r0;
        goto L_0x08b8;
    L_0x06f8:
        r40 = r3;
        r50 = r4;
        r0 = -1;
        r0 = r13.getResourceId(r10, r0);
        r8.mNextFocusUpId = r0;
        goto L_0x08b8;
    L_0x0705:
        r40 = r3;
        r50 = r4;
        r0 = -1;
        r0 = r13.getResourceId(r10, r0);
        r8.mNextFocusRightId = r0;
        goto L_0x08b8;
    L_0x0712:
        r40 = r3;
        r50 = r4;
        r0 = -1;
        r0 = r13.getResourceId(r10, r0);
        r8.mNextFocusLeftId = r0;
        goto L_0x08b8;
    L_0x071f:
        r40 = r3;
        r50 = r4;
        r0 = 14;
        if (r7 < r0) goto L_0x0729;
    L_0x0727:
        goto L_0x08b8;
    L_0x0729:
        r0 = 0;
        r0 = r13.getInt(r10, r0);
        if (r0 == 0) goto L_0x08b8;
    L_0x0730:
        r2 = r12 | r0;
        r3 = r11 | 12288;
        r8.initializeFadingEdgeInternal(r13);
        r12 = r2;
        r11 = r3;
        r2 = r28;
        r3 = r40;
        r4 = r50;
        goto L_0x08be;
    L_0x0741:
        r40 = r3;
        r50 = r4;
        r0 = 0;
        r0 = r13.getInt(r10, r0);
        if (r0 == 0) goto L_0x08b8;
    L_0x074c:
        r2 = r12 | r0;
        r3 = r11 | 768;
        r4 = 1;
        r12 = r2;
        r11 = r3;
        r33 = r4;
        r2 = r28;
        r3 = r40;
        r4 = r50;
        goto L_0x08be;
    L_0x075d:
        r40 = r3;
        r50 = r4;
        r0 = 0;
        r0 = r13.getBoolean(r10, r0);
        if (r0 == 0) goto L_0x08b8;
    L_0x0768:
        r0 = r12 | 2;
        r2 = r11 | 2;
        r12 = r0;
        r11 = r2;
        r2 = r28;
        r3 = r40;
        r4 = r50;
        goto L_0x08be;
    L_0x0776:
        r40 = r3;
        r50 = r4;
        r0 = 0;
        r0 = r13.getInt(r10, r0);
        if (r0 == 0) goto L_0x08b8;
    L_0x0781:
        r2 = VISIBILITY_FLAGS;
        r2 = r2[r0];
        r2 = r2 | r12;
        r3 = r11 | 12;
        r12 = r2;
        r11 = r3;
        r2 = r28;
        r3 = r40;
        r4 = r50;
        goto L_0x08be;
    L_0x0792:
        r40 = r3;
        r50 = r4;
        r0 = 0;
        r0 = r13.getBoolean(r10, r0);
        if (r0 == 0) goto L_0x08b8;
    L_0x079d:
        r0 = r12 & -17;
        r2 = 262145; // 0x40001 float:3.67343E-40 double:1.29517E-318;
        r0 = r0 | r2;
        r2 = 262161; // 0x40011 float:3.67366E-40 double:1.295247E-318;
        r2 = r2 | r11;
        r12 = r0;
        r11 = r2;
        r2 = r28;
        r3 = r40;
        r4 = r50;
        goto L_0x08be;
    L_0x07b1:
        r40 = r3;
        r50 = r4;
        r0 = r12 & -18;
        r2 = r8.getFocusableAttribute(r13);
        r0 = r0 | r2;
        r2 = r0 & 16;
        if (r2 != 0) goto L_0x07cc;
    L_0x07c0:
        r2 = r11 | 17;
        r12 = r0;
        r11 = r2;
        r2 = r28;
        r3 = r40;
        r4 = r50;
        goto L_0x08be;
    L_0x07cc:
        r12 = r0;
        r2 = r28;
        r3 = r40;
        r4 = r50;
        goto L_0x08be;
    L_0x07d5:
        r40 = r3;
        r50 = r4;
        r0 = -1;
        r0 = r13.getDimensionPixelSize(r10, r0);
        r19 = r0;
        r2 = r28;
        goto L_0x08be;
    L_0x07e4:
        r40 = r3;
        r50 = r4;
        r0 = -1;
        r0 = r13.getDimensionPixelSize(r10, r0);
        r8.mUserPaddingRightInitial = r0;
        r2 = 1;
        r18 = r0;
        r3 = r2;
        r2 = r28;
        goto L_0x08be;
    L_0x07f7:
        r40 = r3;
        r50 = r4;
        r0 = -1;
        r0 = r13.getDimensionPixelSize(r10, r0);
        r17 = r0;
        r2 = r28;
        goto L_0x08be;
    L_0x0806:
        r40 = r3;
        r50 = r4;
        r0 = -1;
        r0 = r13.getDimensionPixelSize(r10, r0);
        r8.mUserPaddingLeftInitial = r0;
        r2 = 1;
        r16 = r0;
        r4 = r2;
        r2 = r28;
        goto L_0x08be;
    L_0x0819:
        r40 = r3;
        r50 = r4;
        r0 = -1;
        r0 = r13.getDimensionPixelSize(r10, r0);
        r8.mUserPaddingLeftInitial = r0;
        r8.mUserPaddingRightInitial = r0;
        r2 = 1;
        r3 = 1;
        r4 = r2;
        r2 = r0;
        goto L_0x08be;
    L_0x082c:
        r40 = r3;
        r50 = r4;
        r0 = r13.getDrawable(r10);
        r5 = r0;
        r2 = r28;
        goto L_0x08be;
    L_0x0839:
        r40 = r3;
        r50 = r4;
        r0 = 0;
        r0 = r13.getDimensionPixelOffset(r10, r0);
        r20 = r0;
        r2 = r28;
        goto L_0x08be;
    L_0x0848:
        r40 = r3;
        r50 = r4;
        r0 = 0;
        r0 = r13.getDimensionPixelOffset(r10, r0);
        r22 = r0;
        r2 = r28;
        goto L_0x08be;
    L_0x0857:
        r40 = r3;
        r50 = r4;
        r0 = r13.getText(r10);
        r8.mTag = r0;
        goto L_0x08b8;
    L_0x0862:
        r40 = r3;
        r50 = r4;
        r0 = -1;
        r0 = r13.getResourceId(r10, r0);
        r8.mID = r0;
        goto L_0x08b8;
    L_0x086e:
        r40 = r3;
        r50 = r4;
        r2 = 0;
        r0 = r13.getInt(r10, r2);
        if (r0 == 0) goto L_0x088a;
    L_0x0879:
        r2 = 50331648; // 0x3000000 float:3.761582E-37 double:2.4867138E-316;
        r3 = r0 & r2;
        r3 = r3 | r12;
        r2 = r2 | r11;
        r31 = r0;
        r11 = r2;
        r12 = r3;
        r2 = r28;
        r3 = r40;
        r4 = r50;
        goto L_0x08be;
    L_0x088a:
        r31 = r0;
        r2 = r28;
        r3 = r40;
        r4 = r50;
        goto L_0x08be;
    L_0x0893:
        r40 = r3;
        r50 = r4;
        r0 = 23;
        if (r7 >= r0) goto L_0x089f;
    L_0x089b:
        r0 = r8 instanceof android.widget.FrameLayout;
        if (r0 == 0) goto L_0x08b8;
    L_0x089f:
        r0 = r8.mForegroundInfo;
        if (r0 != 0) goto L_0x08ab;
    L_0x08a3:
        r0 = new android.view.View$ForegroundInfo;
        r2 = 0;
        r0.<init>(r2);
        r8.mForegroundInfo = r0;
    L_0x08ab:
        r0 = r8.mForegroundInfo;
        r2 = r0.mInsidePadding;
        r2 = r13.getBoolean(r10, r2);
        r0.mInsidePadding = r2;
    L_0x08b8:
        r2 = r28;
        r3 = r40;
        r4 = r50;
    L_0x08be:
        r1 = r1 + 1;
        r10 = r26;
        goto L_0x00b9;
    L_0x08c4:
        r28 = r2;
        r40 = r3;
        r50 = r4;
        r26 = r10;
        r2 = 0;
        r8.setOverScrollMode(r14);
        r8.mUserPaddingStart = r15;
        r8.mUserPaddingEnd = r6;
        if (r5 == 0) goto L_0x08d9;
    L_0x08d6:
        r8.setBackground(r5);
    L_0x08d9:
        r4 = r50;
        r8.mLeftPaddingDefined = r4;
        r3 = r40;
        r8.mRightPaddingDefined = r3;
        if (r28 < 0) goto L_0x08f5;
    L_0x08e3:
        r0 = r28;
        r17 = r28;
        r1 = r28;
        r19 = r28;
        r10 = r28;
        r8.mUserPaddingLeftInitial = r10;
        r8.mUserPaddingRightInitial = r10;
        r2 = r1;
        r1 = r24;
        goto L_0x0915;
    L_0x08f5:
        r10 = r28;
        if (r24 < 0) goto L_0x0906;
    L_0x08f9:
        r16 = r24;
        r18 = r24;
        r1 = r24;
        r8.mUserPaddingLeftInitial = r1;
        r8.mUserPaddingRightInitial = r1;
        r0 = r16;
        goto L_0x090a;
    L_0x0906:
        r1 = r24;
        r0 = r16;
    L_0x090a:
        if (r23 < 0) goto L_0x0913;
    L_0x090c:
        r17 = r23;
        r19 = r23;
        r2 = r18;
        goto L_0x0915;
    L_0x0913:
        r2 = r18;
    L_0x0915:
        r16 = r51.isRtlCompatibilityMode();
        if (r16 == 0) goto L_0x0940;
    L_0x091b:
        r16 = r1;
        r1 = r8.mLeftPaddingDefined;
        if (r1 != 0) goto L_0x0924;
    L_0x0921:
        if (r35 == 0) goto L_0x0924;
    L_0x0923:
        r0 = r15;
    L_0x0924:
        if (r0 < 0) goto L_0x0928;
    L_0x0926:
        r1 = r0;
        goto L_0x092a;
    L_0x0928:
        r1 = r8.mUserPaddingLeftInitial;
    L_0x092a:
        r8.mUserPaddingLeftInitial = r1;
        r1 = r8.mRightPaddingDefined;
        if (r1 != 0) goto L_0x0933;
    L_0x0930:
        if (r36 == 0) goto L_0x0933;
    L_0x0932:
        r2 = r6;
    L_0x0933:
        if (r2 < 0) goto L_0x0937;
    L_0x0935:
        r1 = r2;
        goto L_0x0939;
    L_0x0937:
        r1 = r8.mUserPaddingRightInitial;
    L_0x0939:
        r8.mUserPaddingRightInitial = r1;
        r18 = r2;
        r40 = r3;
        goto L_0x0962;
    L_0x0940:
        r16 = r1;
        if (r35 != 0) goto L_0x094a;
    L_0x0944:
        if (r36 == 0) goto L_0x0947;
    L_0x0946:
        goto L_0x094a;
    L_0x0947:
        r27 = 0;
        goto L_0x094c;
    L_0x094a:
        r27 = 1;
    L_0x094c:
        r1 = r27;
        r40 = r3;
        r3 = r8.mLeftPaddingDefined;
        if (r3 == 0) goto L_0x0958;
    L_0x0954:
        if (r1 != 0) goto L_0x0958;
    L_0x0956:
        r8.mUserPaddingLeftInitial = r0;
    L_0x0958:
        r3 = r8.mRightPaddingDefined;
        if (r3 == 0) goto L_0x0960;
    L_0x095c:
        if (r1 != 0) goto L_0x0960;
    L_0x095e:
        r8.mUserPaddingRightInitial = r2;
    L_0x0960:
        r18 = r2;
    L_0x0962:
        r1 = r8.mUserPaddingLeftInitial;
        if (r17 < 0) goto L_0x0969;
    L_0x0966:
        r2 = r17;
        goto L_0x096b;
    L_0x0969:
        r2 = r8.mPaddingTop;
    L_0x096b:
        r3 = r8.mUserPaddingRightInitial;
        r24 = r0;
        if (r19 < 0) goto L_0x0974;
    L_0x0971:
        r0 = r19;
        goto L_0x0976;
    L_0x0974:
        r0 = r8.mPaddingBottom;
    L_0x0976:
        r8.internalSetPadding(r1, r2, r3, r0);
        if (r11 == 0) goto L_0x097e;
    L_0x097b:
        r8.setFlags(r12, r11);
    L_0x097e:
        if (r33 == 0) goto L_0x0983;
    L_0x0980:
        r8.initializeScrollbarsInternal(r13);
    L_0x0983:
        if (r34 == 0) goto L_0x0988;
    L_0x0985:
        r51.initializeScrollIndicatorsInternal();
    L_0x0988:
        r13.recycle();
        if (r31 == 0) goto L_0x0990;
    L_0x098d:
        r51.recomputePadding();
    L_0x0990:
        if (r22 != 0) goto L_0x099a;
    L_0x0992:
        if (r20 == 0) goto L_0x0995;
    L_0x0994:
        goto L_0x099a;
    L_0x0995:
        r2 = r20;
        r3 = r22;
        goto L_0x09a1;
    L_0x099a:
        r2 = r20;
        r3 = r22;
        r8.scrollTo(r3, r2);
    L_0x09a1:
        if (r30 == 0) goto L_0x09e1;
    L_0x09a3:
        r1 = r41;
        r8.setTranslationX(r1);
        r20 = r7;
        r7 = r42;
        r8.setTranslationY(r7);
        r22 = r7;
        r7 = r48;
        r8.setTranslationZ(r7);
        r27 = r7;
        r7 = r49;
        r8.setElevation(r7);
        r28 = r7;
        r7 = r45;
        r8.setRotation(r7);
        r29 = r7;
        r7 = r46;
        r8.setRotationX(r7);
        r32 = r7;
        r7 = r47;
        r8.setRotationY(r7);
        r37 = r7;
        r7 = r43;
        r8.setScaleX(r7);
        r38 = r7;
        r7 = r44;
        r8.setScaleY(r7);
        goto L_0x09f5;
    L_0x09e1:
        r20 = r7;
        r1 = r41;
        r22 = r42;
        r38 = r43;
        r7 = r44;
        r29 = r45;
        r32 = r46;
        r37 = r47;
        r27 = r48;
        r28 = r49;
    L_0x09f5:
        if (r21 != 0) goto L_0x09ff;
    L_0x09f7:
        r0 = r12 & 512;
        if (r0 == 0) goto L_0x09ff;
    L_0x09fb:
        r0 = 1;
        r8.setScrollContainer(r0);
    L_0x09ff:
        r51.computeOpaqueFlags();
        r0 = com.miui.internal.variable.api.v29.Android_View_View.Extension.get();
        r0 = r0.getExtension();
        if (r0 == 0) goto L_0x0a37;
    L_0x0a0c:
        r0 = com.miui.internal.variable.api.v29.Android_View_View.Extension.get();
        r0 = r0.getExtension();
        r0 = r0.asInterface();
        r0 = (com.miui.internal.variable.api.v29.Android_View_View.Interface) r0;
        r41 = r1;
        r1 = r0;
        r25 = r2;
        r2 = r51;
        r39 = r40;
        r40 = r3;
        r3 = r52;
        r42 = r4;
        r4 = r53;
        r43 = r5;
        r5 = r54;
        r44 = r6;
        r6 = r55;
        r1.init(r2, r3, r4, r5, r6);
        goto L_0x0a45;
    L_0x0a37:
        r41 = r1;
        r25 = r2;
        r42 = r4;
        r43 = r5;
        r44 = r6;
        r39 = r40;
        r40 = r3;
    L_0x0a45:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.View.<init>(android.content.Context, android.util.AttributeSet, int, int):void");
    }

    public int[] getAttributeResolutionStack(int attribute) {
        if (sDebugViewAttributes) {
            SparseArray sparseArray = this.mAttributeResolutionStacks;
            if (!(sparseArray == null || sparseArray.get(attribute) == null)) {
                int[] attributeResolutionStack = (int[]) this.mAttributeResolutionStacks.get(attribute);
                int stackSize = attributeResolutionStack.length;
                if (this.mSourceLayoutId != 0) {
                    stackSize++;
                }
                int currentIndex = 0;
                int[] stack = new int[stackSize];
                int i = this.mSourceLayoutId;
                if (i != 0) {
                    stack[0] = i;
                    currentIndex = 0 + 1;
                }
                for (int i2 : attributeResolutionStack) {
                    stack[currentIndex] = i2;
                    currentIndex++;
                }
                return stack;
            }
        }
        return new int[0];
    }

    public Map<Integer, Integer> getAttributeSourceResourceMap() {
        HashMap<Integer, Integer> map = new HashMap();
        if (!sDebugViewAttributes || this.mAttributeSourceResId == null) {
            return map;
        }
        for (int i = 0; i < this.mAttributeSourceResId.size(); i++) {
            map.put(Integer.valueOf(this.mAttributeSourceResId.keyAt(i)), Integer.valueOf(this.mAttributeSourceResId.valueAt(i)));
        }
        return map;
    }

    public int getExplicitStyle() {
        if (sDebugViewAttributes) {
            return this.mExplicitStyle;
        }
        return 0;
    }

    @UnsupportedAppUsage
    View() {
        this.mCurrentAnimation = null;
        this.mRecreateDisplayList = false;
        this.mID = -1;
        this.mAutofillViewId = -1;
        this.mAccessibilityViewId = -1;
        this.mAccessibilityCursorPosition = -1;
        this.mTag = null;
        this.mTransientStateCount = 0;
        this.mClipBounds = null;
        this.mPaddingLeft = 0;
        this.mPaddingRight = 0;
        this.mLabelForId = -1;
        this.mAccessibilityTraversalBeforeId = -1;
        this.mAccessibilityTraversalAfterId = -1;
        this.mLeftPaddingDefined = false;
        this.mRightPaddingDefined = false;
        this.mOldWidthMeasureSpec = Integer.MIN_VALUE;
        this.mOldHeightMeasureSpec = Integer.MIN_VALUE;
        this.mLongClickX = Float.NaN;
        this.mLongClickY = Float.NaN;
        this.mDrawableState = null;
        this.mOutlineProvider = ViewOutlineProvider.BACKGROUND;
        this.mNextFocusLeftId = -1;
        this.mNextFocusRightId = -1;
        this.mNextFocusUpId = -1;
        this.mNextFocusDownId = -1;
        this.mNextFocusForwardId = -1;
        this.mNextClusterForwardId = -1;
        this.mDefaultFocusHighlightEnabled = true;
        this.mPendingCheckForTap = null;
        this.mTouchDelegate = null;
        this.mHoveringTouchDelegate = false;
        this.mDrawingCacheBackgroundColor = 0;
        this.mAnimator = null;
        this.mLayerType = 0;
        this.mInputEventConsistencyVerifier = InputEventConsistencyVerifier.isInstrumentationEnabled() ? new InputEventConsistencyVerifier(this, 0) : null;
        this.mSourceLayoutId = 0;
        this.mFirst = true;
        this.mIsWebView = false;
        this.mResources = null;
        this.mRenderNode = RenderNode.create(getClass().getName(), new ViewAnimationHostBridge(this));
    }

    /* Access modifiers changed, original: final */
    public final boolean debugDraw() {
        if (!DEBUG_DRAW) {
            AttachInfo attachInfo = this.mAttachInfo;
            if (attachInfo == null || !attachInfo.mDebugLayout) {
                return false;
            }
        }
        return true;
    }

    private static SparseArray<String> getAttributeMap() {
        if (mAttributeMap == null) {
            mAttributeMap = new SparseArray();
        }
        return mAttributeMap;
    }

    private void retrieveExplicitStyle(Theme theme, AttributeSet attrs) {
        if (sDebugViewAttributes) {
            this.mExplicitStyle = theme.getExplicitStyle(attrs);
        }
    }

    public final void saveAttributeDataForStyleable(Context context, int[] styleable, AttributeSet attrs, TypedArray t, int defStyleAttr, int defStyleRes) {
        if (sDebugViewAttributes) {
            int[] attributeResolutionStack = context.getTheme().getAttributeResolutionStack(defStyleAttr, defStyleRes, this.mExplicitStyle);
            if (this.mAttributeResolutionStacks == null) {
                this.mAttributeResolutionStacks = new SparseArray();
            }
            if (this.mAttributeSourceResId == null) {
                this.mAttributeSourceResId = new SparseIntArray();
            }
            int indexCount = t.getIndexCount();
            for (int j = 0; j < indexCount; j++) {
                int index = t.getIndex(j);
                this.mAttributeSourceResId.append(styleable[index], t.getSourceResourceId(index, 0));
                this.mAttributeResolutionStacks.append(styleable[index], attributeResolutionStack);
            }
        }
    }

    private void saveAttributeData(AttributeSet attrs, TypedArray t) {
        AttributeSet attributeSet = attrs;
        TypedArray typedArray = t;
        int attrsCount = attributeSet == null ? 0 : attrs.getAttributeCount();
        int indexCount = t.getIndexCount();
        String[] attributes = new String[((attrsCount + indexCount) * 2)];
        int i = 0;
        for (int j = 0; j < attrsCount; j++) {
            attributes[i] = attributeSet.getAttributeName(j);
            attributes[i + 1] = attributeSet.getAttributeValue(j);
            i += 2;
        }
        Resources res = t.getResources();
        SparseArray<String> attributeMap = getAttributeMap();
        int i2 = i;
        for (int j2 = 0; j2 < indexCount; j2++) {
            int index = typedArray.getIndex(j2);
            if (typedArray.hasValueOrEmpty(index)) {
                int resourceId = typedArray.getResourceId(index, 0);
                if (resourceId != 0) {
                    String resourceName = (String) attributeMap.get(resourceId);
                    if (resourceName == null) {
                        try {
                            resourceName = res.getResourceName(resourceId);
                        } catch (NotFoundException e) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("0x");
                            stringBuilder.append(Integer.toHexString(resourceId));
                            resourceName = stringBuilder.toString();
                        }
                        attributeMap.put(resourceId, resourceName);
                    }
                    attributes[i2] = resourceName;
                    attributes[i2 + 1] = typedArray.getString(index);
                    i2 += 2;
                }
            }
        }
        String[] trimmed = new String[i2];
        System.arraycopy(attributes, 0, trimmed, 0, i2);
        this.mAttributes = trimmed;
    }

    public String toString() {
        StringBuilder out = new StringBuilder(128);
        out.append(getClass().getName());
        out.append('{');
        out.append(Integer.toHexString(System.identityHashCode(this)));
        out.append(' ');
        int i = this.mViewFlags & 12;
        char c = 'I';
        char c2 = 'V';
        char c3 = ClassUtils.PACKAGE_SEPARATOR_CHAR;
        if (i == 0) {
            out.append('V');
        } else if (i == 4) {
            out.append('I');
        } else if (i != 8) {
            out.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
        } else {
            out.append('G');
        }
        char c4 = 'F';
        out.append((this.mViewFlags & 1) == 1 ? 'F' : ClassUtils.PACKAGE_SEPARATOR_CHAR);
        out.append((this.mViewFlags & 32) == 0 ? DateFormat.DAY : ClassUtils.PACKAGE_SEPARATOR_CHAR);
        out.append((this.mViewFlags & 128) == 128 ? ClassUtils.PACKAGE_SEPARATOR_CHAR : 'D');
        char c5 = 'H';
        out.append((this.mViewFlags & 256) != 0 ? 'H' : ClassUtils.PACKAGE_SEPARATOR_CHAR);
        if ((this.mViewFlags & 512) == 0) {
            c2 = ClassUtils.PACKAGE_SEPARATOR_CHAR;
        }
        out.append(c2);
        out.append((this.mViewFlags & 16384) != 0 ? 'C' : ClassUtils.PACKAGE_SEPARATOR_CHAR);
        out.append((this.mViewFlags & 2097152) != 0 ? DateFormat.STANDALONE_MONTH : ClassUtils.PACKAGE_SEPARATOR_CHAR);
        out.append((this.mViewFlags & 8388608) != 0 ? 'X' : ClassUtils.PACKAGE_SEPARATOR_CHAR);
        out.append(' ');
        out.append((this.mPrivateFlags & 8) != 0 ? 'R' : ClassUtils.PACKAGE_SEPARATOR_CHAR);
        if ((this.mPrivateFlags & 2) == 0) {
            c4 = ClassUtils.PACKAGE_SEPARATOR_CHAR;
        }
        out.append(c4);
        out.append((this.mPrivateFlags & 4) != 0 ? 'S' : ClassUtils.PACKAGE_SEPARATOR_CHAR);
        int i2 = this.mPrivateFlags;
        if ((33554432 & i2) != 0) {
            out.append('p');
        } else {
            out.append((i2 & 16384) != 0 ? 'P' : ClassUtils.PACKAGE_SEPARATOR_CHAR);
        }
        if ((this.mPrivateFlags & 268435456) == 0) {
            c5 = ClassUtils.PACKAGE_SEPARATOR_CHAR;
        }
        out.append(c5);
        out.append((this.mPrivateFlags & 1073741824) != 0 ? DateFormat.CAPITAL_AM_PM : ClassUtils.PACKAGE_SEPARATOR_CHAR);
        if ((this.mPrivateFlags & Integer.MIN_VALUE) == 0) {
            c = ClassUtils.PACKAGE_SEPARATOR_CHAR;
        }
        out.append(c);
        if ((this.mPrivateFlags & 2097152) != 0) {
            c3 = 'D';
        }
        out.append(c3);
        out.append(' ');
        out.append(this.mLeft);
        out.append(',');
        out.append(this.mTop);
        out.append('-');
        out.append(this.mRight);
        out.append(',');
        out.append(this.mBottom);
        i2 = getId();
        if (i2 != -1) {
            out.append(" #");
            out.append(Integer.toHexString(i2));
            Resources r = this.mResources;
            if (i2 > 0 && Resources.resourceHasPackage(i2) && r != null) {
                String pkgname;
                i = -16777216 & i2;
                if (i == 16777216) {
                    pkgname = "android";
                } else if (i != 2130706432) {
                    try {
                        pkgname = r.getResourcePackageName(i2);
                    } catch (NotFoundException e) {
                    }
                } else {
                    pkgname = "app";
                }
                String typename = r.getResourceTypeName(i2);
                String entryname = r.getResourceEntryName(i2);
                out.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                out.append(pkgname);
                out.append(":");
                out.append(typename);
                out.append("/");
                out.append(entryname);
            }
        }
        if (this.mAutofillId != null) {
            out.append(" aid=");
            out.append(this.mAutofillId);
        }
        out.append("}");
        return out.toString();
    }

    /* Access modifiers changed, original: protected */
    public void initializeFadingEdge(TypedArray a) {
        TypedArray arr = this.mContext.obtainStyledAttributes(R.styleable.View);
        initializeFadingEdgeInternal(arr);
        arr.recycle();
    }

    /* Access modifiers changed, original: protected */
    public void initializeFadingEdgeInternal(TypedArray a) {
        initScrollCache();
        this.mScrollCache.fadingEdgeLength = a.getDimensionPixelSize(25, ViewConfiguration.get(this.mContext).getScaledFadingEdgeLength());
    }

    public int getVerticalFadingEdgeLength() {
        if (isVerticalFadingEdgeEnabled()) {
            ScrollabilityCache cache = this.mScrollCache;
            if (cache != null) {
                return cache.fadingEdgeLength;
            }
        }
        return 0;
    }

    public void setFadingEdgeLength(int length) {
        initScrollCache();
        this.mScrollCache.fadingEdgeLength = length;
    }

    public int getHorizontalFadingEdgeLength() {
        if (isHorizontalFadingEdgeEnabled()) {
            ScrollabilityCache cache = this.mScrollCache;
            if (cache != null) {
                return cache.fadingEdgeLength;
            }
        }
        return 0;
    }

    public int getVerticalScrollbarWidth() {
        ScrollabilityCache cache = this.mScrollCache;
        if (cache == null) {
            return 0;
        }
        ScrollBarDrawable scrollBar = cache.scrollBar;
        if (scrollBar == null) {
            return 0;
        }
        int size = scrollBar.getSize(1);
        if (size <= 0) {
            size = cache.scrollBarSize;
        }
        return size;
    }

    /* Access modifiers changed, original: protected */
    public int getHorizontalScrollbarHeight() {
        ScrollabilityCache cache = this.mScrollCache;
        if (cache == null) {
            return 0;
        }
        ScrollBarDrawable scrollBar = cache.scrollBar;
        if (scrollBar == null) {
            return 0;
        }
        int size = scrollBar.getSize(false);
        if (size <= 0) {
            size = cache.scrollBarSize;
        }
        return size;
    }

    /* Access modifiers changed, original: protected */
    public void initializeScrollbars(TypedArray a) {
        TypedArray arr = this.mContext.obtainStyledAttributes(R.styleable.View);
        initializeScrollbarsInternal(arr);
        arr.recycle();
    }

    private void initializeScrollBarDrawable() {
        initScrollCache();
        if (this.mScrollCache.scrollBar == null) {
            this.mScrollCache.scrollBar = new ScrollBarDrawable();
            this.mScrollCache.scrollBar.setState(getDrawableState());
            this.mScrollCache.scrollBar.setCallback(this);
        }
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public void initializeScrollbarsInternal(TypedArray a) {
        initScrollCache();
        ScrollabilityCache scrollabilityCache = this.mScrollCache;
        if (scrollabilityCache.scrollBar == null) {
            scrollabilityCache.scrollBar = new ScrollBarDrawable();
            scrollabilityCache.scrollBar.setState(getDrawableState());
            scrollabilityCache.scrollBar.setCallback(this);
        }
        boolean fadeScrollbars = a.getBoolean(true, true);
        if (!fadeScrollbars) {
            scrollabilityCache.state = 1;
        }
        scrollabilityCache.fadeScrollBars = fadeScrollbars;
        scrollabilityCache.scrollBarFadeDuration = a.getInt(45, ViewConfiguration.getScrollBarFadeDuration());
        scrollabilityCache.scrollBarDefaultDelayBeforeFade = a.getInt(46, ViewConfiguration.getScrollDefaultDelay());
        scrollabilityCache.scrollBarSize = a.getDimensionPixelSize(1, ViewConfiguration.get(this.mContext).getScaledScrollBarSize());
        scrollabilityCache.scrollBar.setHorizontalTrackDrawable(a.getDrawable(4));
        Drawable thumb = a.getDrawable(2);
        if (thumb != null) {
            scrollabilityCache.scrollBar.setHorizontalThumbDrawable(thumb);
        }
        if (a.getBoolean(true, false)) {
            scrollabilityCache.scrollBar.setAlwaysDrawHorizontalTrack(true);
        }
        Drawable track = a.getDrawable(5);
        scrollabilityCache.scrollBar.setVerticalTrackDrawable(track);
        thumb = a.getDrawable(3);
        if (thumb != null) {
            scrollabilityCache.scrollBar.setVerticalThumbDrawable(thumb);
        }
        if (a.getBoolean(7, false)) {
            scrollabilityCache.scrollBar.setAlwaysDrawVerticalTrack(true);
        }
        int layoutDirection = getLayoutDirection();
        if (track != null) {
            track.setLayoutDirection(layoutDirection);
        }
        if (thumb != null) {
            thumb.setLayoutDirection(layoutDirection);
        }
        resolvePadding();
    }

    public void setVerticalScrollbarThumbDrawable(Drawable drawable) {
        initializeScrollBarDrawable();
        this.mScrollCache.scrollBar.setVerticalThumbDrawable(drawable);
    }

    public void setVerticalScrollbarTrackDrawable(Drawable drawable) {
        initializeScrollBarDrawable();
        this.mScrollCache.scrollBar.setVerticalTrackDrawable(drawable);
    }

    public void setHorizontalScrollbarThumbDrawable(Drawable drawable) {
        initializeScrollBarDrawable();
        this.mScrollCache.scrollBar.setHorizontalThumbDrawable(drawable);
    }

    public void setHorizontalScrollbarTrackDrawable(Drawable drawable) {
        initializeScrollBarDrawable();
        this.mScrollCache.scrollBar.setHorizontalTrackDrawable(drawable);
    }

    public Drawable getVerticalScrollbarThumbDrawable() {
        ScrollabilityCache scrollabilityCache = this.mScrollCache;
        return scrollabilityCache != null ? scrollabilityCache.scrollBar.getVerticalThumbDrawable() : null;
    }

    public Drawable getVerticalScrollbarTrackDrawable() {
        ScrollabilityCache scrollabilityCache = this.mScrollCache;
        return scrollabilityCache != null ? scrollabilityCache.scrollBar.getVerticalTrackDrawable() : null;
    }

    public Drawable getHorizontalScrollbarThumbDrawable() {
        ScrollabilityCache scrollabilityCache = this.mScrollCache;
        return scrollabilityCache != null ? scrollabilityCache.scrollBar.getHorizontalThumbDrawable() : null;
    }

    public Drawable getHorizontalScrollbarTrackDrawable() {
        ScrollabilityCache scrollabilityCache = this.mScrollCache;
        return scrollabilityCache != null ? scrollabilityCache.scrollBar.getHorizontalTrackDrawable() : null;
    }

    private void initializeScrollIndicatorsInternal() {
        if (this.mScrollIndicatorDrawable == null) {
            this.mScrollIndicatorDrawable = this.mContext.getDrawable(R.drawable.scroll_indicator_material);
        }
    }

    private void initScrollCache() {
        if (this.mScrollCache == null) {
            this.mScrollCache = new ScrollabilityCache(ViewConfiguration.get(this.mContext), this);
        }
    }

    @UnsupportedAppUsage
    private ScrollabilityCache getScrollCache() {
        initScrollCache();
        return this.mScrollCache;
    }

    public void setVerticalScrollbarPosition(int position) {
        if (this.mVerticalScrollbarPosition != position) {
            this.mVerticalScrollbarPosition = position;
            computeOpaqueFlags();
            resolvePadding();
        }
    }

    public int getVerticalScrollbarPosition() {
        return this.mVerticalScrollbarPosition;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isOnScrollbar(float x, float y) {
        if (this.mScrollCache == null) {
            return false;
        }
        x += (float) getScrollX();
        y += (float) getScrollY();
        boolean canScrollVertically = computeVerticalScrollRange() > computeVerticalScrollExtent();
        if (isVerticalScrollBarEnabled() && !isVerticalScrollBarHidden() && canScrollVertically) {
            Rect touchBounds = this.mScrollCache.mScrollBarTouchBounds;
            getVerticalScrollBarBounds(null, touchBounds);
            if (touchBounds.contains((int) x, (int) y)) {
                return true;
            }
        }
        boolean canScrollHorizontally = computeHorizontalScrollRange() > computeHorizontalScrollExtent();
        if (isHorizontalScrollBarEnabled() && canScrollHorizontally) {
            Rect touchBounds2 = this.mScrollCache.mScrollBarTouchBounds;
            getHorizontalScrollBarBounds(null, touchBounds2);
            if (touchBounds2.contains((int) x, (int) y)) {
                return true;
            }
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public boolean isOnScrollbarThumb(float x, float y) {
        return isOnVerticalScrollbarThumb(x, y) || isOnHorizontalScrollbarThumb(x, y);
    }

    private boolean isOnVerticalScrollbarThumb(float x, float y) {
        if (this.mScrollCache == null || !isVerticalScrollBarEnabled() || isVerticalScrollBarHidden()) {
            return false;
        }
        int range = computeVerticalScrollRange();
        int extent = computeVerticalScrollExtent();
        if (range > extent) {
            x += (float) getScrollX();
            y += (float) getScrollY();
            Rect bounds = this.mScrollCache.mScrollBarBounds;
            Rect touchBounds = this.mScrollCache.mScrollBarTouchBounds;
            getVerticalScrollBarBounds(bounds, touchBounds);
            int offset = computeVerticalScrollOffset();
            int thumbLength = ScrollBarUtils.getThumbLength(bounds.height(), bounds.width(), extent, range);
            int thumbTop = bounds.top + ScrollBarUtils.getThumbOffset(bounds.height(), thumbLength, extent, range, offset);
            int adjust = Math.max(this.mScrollCache.scrollBarMinTouchTarget - thumbLength, 0) / 2;
            if (x >= ((float) touchBounds.left) && x <= ((float) touchBounds.right) && y >= ((float) (thumbTop - adjust)) && y <= ((float) ((thumbTop + thumbLength) + adjust))) {
                return true;
            }
        }
        return false;
    }

    private boolean isOnHorizontalScrollbarThumb(float x, float y) {
        if (this.mScrollCache == null || !isHorizontalScrollBarEnabled()) {
            return false;
        }
        int range = computeHorizontalScrollRange();
        int extent = computeHorizontalScrollExtent();
        if (range > extent) {
            x += (float) getScrollX();
            y += (float) getScrollY();
            Rect bounds = this.mScrollCache.mScrollBarBounds;
            Rect touchBounds = this.mScrollCache.mScrollBarTouchBounds;
            getHorizontalScrollBarBounds(bounds, touchBounds);
            int offset = computeHorizontalScrollOffset();
            int thumbLength = ScrollBarUtils.getThumbLength(bounds.width(), bounds.height(), extent, range);
            int thumbLeft = bounds.left + ScrollBarUtils.getThumbOffset(bounds.width(), thumbLength, extent, range, offset);
            int adjust = Math.max(this.mScrollCache.scrollBarMinTouchTarget - thumbLength, 0) / 2;
            if (x >= ((float) (thumbLeft - adjust)) && x <= ((float) ((thumbLeft + thumbLength) + adjust)) && y >= ((float) touchBounds.top) && y <= ((float) touchBounds.bottom)) {
                return true;
            }
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public boolean isDraggingScrollBar() {
        ScrollabilityCache scrollabilityCache = this.mScrollCache;
        return (scrollabilityCache == null || scrollabilityCache.mScrollBarDraggingState == 0) ? false : true;
    }

    public void setScrollIndicators(int indicators) {
        setScrollIndicators(indicators, 63);
    }

    public void setScrollIndicators(int indicators, int mask) {
        mask = (mask << 8) & SCROLL_INDICATORS_PFLAG3_MASK;
        indicators = (indicators << 8) & mask;
        int i = this.mPrivateFlags3;
        int updatedFlags = ((~mask) & i) | indicators;
        if (i != updatedFlags) {
            this.mPrivateFlags3 = updatedFlags;
            if (indicators != 0) {
                initializeScrollIndicatorsInternal();
            }
            invalidate();
        }
    }

    public int getScrollIndicators() {
        return (this.mPrivateFlags3 & SCROLL_INDICATORS_PFLAG3_MASK) >>> 8;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public ListenerInfo getListenerInfo() {
        ListenerInfo listenerInfo = this.mListenerInfo;
        if (listenerInfo != null) {
            return listenerInfo;
        }
        this.mListenerInfo = new ListenerInfo();
        return this.mListenerInfo;
    }

    public void setOnScrollChangeListener(OnScrollChangeListener l) {
        getListenerInfo().mOnScrollChangeListener = l;
    }

    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        getListenerInfo().mOnFocusChangeListener = l;
    }

    public void addOnLayoutChangeListener(OnLayoutChangeListener listener) {
        ListenerInfo li = getListenerInfo();
        if (li.mOnLayoutChangeListeners == null) {
            li.mOnLayoutChangeListeners = new ArrayList();
        }
        if (!li.mOnLayoutChangeListeners.contains(listener)) {
            li.mOnLayoutChangeListeners.add(listener);
        }
    }

    public void removeOnLayoutChangeListener(OnLayoutChangeListener listener) {
        ListenerInfo li = this.mListenerInfo;
        if (li != null && li.mOnLayoutChangeListeners != null) {
            li.mOnLayoutChangeListeners.remove(listener);
        }
    }

    public void addOnAttachStateChangeListener(OnAttachStateChangeListener listener) {
        ListenerInfo li = getListenerInfo();
        if (li.mOnAttachStateChangeListeners == null) {
            li.mOnAttachStateChangeListeners = new CopyOnWriteArrayList();
        }
        li.mOnAttachStateChangeListeners.add(listener);
    }

    public void removeOnAttachStateChangeListener(OnAttachStateChangeListener listener) {
        ListenerInfo li = this.mListenerInfo;
        if (li != null && li.mOnAttachStateChangeListeners != null) {
            li.mOnAttachStateChangeListeners.remove(listener);
        }
    }

    public OnFocusChangeListener getOnFocusChangeListener() {
        ListenerInfo li = this.mListenerInfo;
        return li != null ? li.mOnFocusChangeListener : null;
    }

    public void setOnClickListener(OnClickListener l) {
        if (!isClickable()) {
            setClickable(true);
        }
        getListenerInfo().mOnClickListener = l;
    }

    public boolean hasOnClickListeners() {
        ListenerInfo li = this.mListenerInfo;
        return (li == null || li.mOnClickListener == null) ? false : true;
    }

    public void setOnLongClickListener(OnLongClickListener l) {
        if (!isLongClickable()) {
            setLongClickable(true);
        }
        getListenerInfo().mOnLongClickListener = l;
    }

    public void setOnContextClickListener(OnContextClickListener l) {
        if (!isContextClickable()) {
            setContextClickable(true);
        }
        getListenerInfo().mOnContextClickListener = l;
    }

    public void setOnCreateContextMenuListener(OnCreateContextMenuListener l) {
        if (!isLongClickable()) {
            setLongClickable(true);
        }
        getListenerInfo().mOnCreateContextMenuListener = l;
    }

    public void addFrameMetricsListener(Window window, OnFrameMetricsAvailableListener listener, Handler handler) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo == null) {
            if (this.mFrameMetricsObservers == null) {
                this.mFrameMetricsObservers = new ArrayList();
            }
            this.mFrameMetricsObservers.add(new FrameMetricsObserver(window, handler.getLooper(), listener));
        } else if (attachInfo.mThreadedRenderer != null) {
            if (this.mFrameMetricsObservers == null) {
                this.mFrameMetricsObservers = new ArrayList();
            }
            FrameMetricsObserver fmo = new FrameMetricsObserver(window, handler.getLooper(), listener);
            this.mFrameMetricsObservers.add(fmo);
            this.mAttachInfo.mThreadedRenderer.addFrameMetricsObserver(fmo);
        } else {
            Log.w(VIEW_LOG_TAG, "View not hardware-accelerated. Unable to observe frame stats");
        }
    }

    public void removeFrameMetricsListener(OnFrameMetricsAvailableListener listener) {
        ThreadedRenderer renderer = getThreadedRenderer();
        FrameMetricsObserver fmo = findFrameMetricsObserver(listener);
        if (fmo != null) {
            ArrayList arrayList = this.mFrameMetricsObservers;
            if (arrayList != null) {
                arrayList.remove(fmo);
                if (renderer != null) {
                    renderer.removeFrameMetricsObserver(fmo);
                    return;
                }
                return;
            }
            return;
        }
        throw new IllegalArgumentException("attempt to remove OnFrameMetricsAvailableListener that was never added");
    }

    private void registerPendingFrameMetricsObservers() {
        if (this.mFrameMetricsObservers != null) {
            ThreadedRenderer renderer = getThreadedRenderer();
            if (renderer != null) {
                Iterator it = this.mFrameMetricsObservers.iterator();
                while (it.hasNext()) {
                    renderer.addFrameMetricsObserver((FrameMetricsObserver) it.next());
                }
                return;
            }
            Log.w(VIEW_LOG_TAG, "View not hardware-accelerated. Unable to observe frame stats");
        }
    }

    private FrameMetricsObserver findFrameMetricsObserver(OnFrameMetricsAvailableListener listener) {
        if (this.mFrameMetricsObservers != null) {
            for (int i = 0; i < this.mFrameMetricsObservers.size(); i++) {
                FrameMetricsObserver observer = (FrameMetricsObserver) this.mFrameMetricsObservers.get(i);
                if (observer.mListener == listener) {
                    return observer;
                }
            }
        }
        return null;
    }

    public void setNotifyAutofillManagerOnClick(boolean notify) {
        if (notify) {
            this.mPrivateFlags |= 536870912;
        } else {
            this.mPrivateFlags &= -536870913;
        }
    }

    private void notifyAutofillManagerOnClick() {
        if ((this.mPrivateFlags & 536870912) != 0) {
            try {
                getAutofillManager().notifyViewClicked(this);
            } finally {
                this.mPrivateFlags = -536870913 & this.mPrivateFlags;
            }
        }
    }

    private boolean performClickInternal() {
        notifyAutofillManagerOnClick();
        return performClick();
    }

    public boolean performClick() {
        boolean result;
        notifyAutofillManagerOnClick();
        ListenerInfo li = this.mListenerInfo;
        if (li == null || li.mOnClickListener == null) {
            result = false;
        } else {
            playSoundEffect(0);
            li.mOnClickListener.onClick(this);
            result = true;
        }
        sendAccessibilityEvent(1);
        notifyEnterOrExitForAutoFillIfNeeded(true);
        return result;
    }

    public boolean callOnClick() {
        ListenerInfo li = this.mListenerInfo;
        if (li == null || li.mOnClickListener == null) {
            return false;
        }
        li.mOnClickListener.onClick(this);
        return true;
    }

    public boolean performLongClick() {
        return performLongClickInternal(this.mLongClickX, this.mLongClickY);
    }

    public boolean performLongClick(float x, float y) {
        this.mLongClickX = x;
        this.mLongClickY = y;
        boolean handled = performLongClick();
        this.mLongClickX = Float.NaN;
        this.mLongClickY = Float.NaN;
        return handled;
    }

    private boolean performLongClickInternal(float x, float y) {
        sendAccessibilityEvent(2);
        boolean handled = false;
        ListenerInfo li = this.mListenerInfo;
        if (!(li == null || li.mOnLongClickListener == null)) {
            handled = li.mOnLongClickListener.onLongClick(this);
        }
        if (!handled) {
            boolean isAnchored = (Float.isNaN(x) || Float.isNaN(y)) ? false : true;
            handled = isAnchored ? showContextMenu(x, y) : showContextMenu();
        }
        if ((this.mViewFlags & 1073741824) == 1073741824 && !handled) {
            handled = showLongClickTooltip((int) x, (int) y);
        }
        if (handled) {
            performHapticFeedback(0);
        }
        return handled;
    }

    public boolean performContextClick(float x, float y) {
        return performContextClick();
    }

    public boolean performContextClick() {
        sendAccessibilityEvent(8388608);
        boolean handled = false;
        ListenerInfo li = this.mListenerInfo;
        if (!(li == null || li.mOnContextClickListener == null)) {
            handled = li.mOnContextClickListener.onContextClick(this);
        }
        if (handled) {
            performHapticFeedback(6);
        }
        return handled;
    }

    /* Access modifiers changed, original: protected */
    public boolean performButtonActionOnTouchDown(MotionEvent event) {
        if (!event.isFromSource(8194) || (event.getButtonState() & 2) == 0) {
            return false;
        }
        showContextMenu(event.getX(), event.getY());
        this.mPrivateFlags |= 67108864;
        return true;
    }

    public boolean showContextMenu() {
        return getParent().showContextMenuForChild(this);
    }

    public boolean showContextMenu(float x, float y) {
        return getParent().showContextMenuForChild(this, x, y);
    }

    public ActionMode startActionMode(ActionMode.Callback callback) {
        return startActionMode(callback, 0);
    }

    public ActionMode startActionMode(ActionMode.Callback callback, int type) {
        ViewParent parent = getParent();
        if (parent == null) {
            return null;
        }
        try {
            return parent.startActionModeForChild(this, callback, type);
        } catch (AbstractMethodError e) {
            return parent.startActionModeForChild(this, callback);
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public void startActivityForResult(Intent intent, int requestCode) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("@android:view:");
        stringBuilder.append(System.identityHashCode(this));
        this.mStartActivityRequestWho = stringBuilder.toString();
        getContext().startActivityForResult(this.mStartActivityRequestWho, intent, requestCode, null);
    }

    public boolean dispatchActivityResult(String who, int requestCode, int resultCode, Intent data) {
        String str = this.mStartActivityRequestWho;
        if (str == null || !str.equals(who)) {
            return false;
        }
        onActivityResult(requestCode, resultCode, data);
        this.mStartActivityRequestWho = null;
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public void setOnKeyListener(OnKeyListener l) {
        getListenerInfo().mOnKeyListener = l;
    }

    public void setOnTouchListener(OnTouchListener l) {
        getListenerInfo().mOnTouchListener = l;
    }

    public void setOnGenericMotionListener(OnGenericMotionListener l) {
        getListenerInfo().mOnGenericMotionListener = l;
    }

    public void setOnHoverListener(OnHoverListener l) {
        getListenerInfo().mOnHoverListener = l;
    }

    public void setOnDragListener(OnDragListener l) {
        getListenerInfo().mOnDragListener = l;
    }

    /* Access modifiers changed, original: 0000 */
    public void handleFocusGainInternal(int direction, Rect previouslyFocusedRect) {
        int i = this.mPrivateFlags;
        if ((i & 2) == 0) {
            this.mPrivateFlags = i | 2;
            View oldFocus = this.mAttachInfo != null ? getRootView().findFocus() : null;
            ViewParent viewParent = this.mParent;
            if (viewParent != null) {
                viewParent.requestChildFocus(this, this);
                updateFocusedInCluster(oldFocus, direction);
            }
            AttachInfo attachInfo = this.mAttachInfo;
            if (attachInfo != null) {
                attachInfo.mTreeObserver.dispatchOnGlobalFocusChange(oldFocus, this);
            }
            onFocusChanged(true, direction, previouslyFocusedRect);
            refreshDrawableState();
        }
    }

    public final void setRevealOnFocusHint(boolean revealOnFocus) {
        if (revealOnFocus) {
            this.mPrivateFlags3 &= -67108865;
        } else {
            this.mPrivateFlags3 |= 67108864;
        }
    }

    public final boolean getRevealOnFocusHint() {
        return (this.mPrivateFlags3 & 67108864) == 0;
    }

    public void getHotspotBounds(Rect outRect) {
        Drawable background = getBackground();
        if (background != null) {
            background.getHotspotBounds(outRect);
        } else {
            getBoundsOnScreen(outRect);
        }
    }

    public boolean requestRectangleOnScreen(Rect rectangle) {
        return requestRectangleOnScreen(rectangle, false);
    }

    public boolean requestRectangleOnScreen(Rect rectangle, boolean immediate) {
        if (this.mParent == null) {
            return false;
        }
        View child = this;
        AttachInfo attachInfo = this.mAttachInfo;
        RectF position = attachInfo != null ? attachInfo.mTmpTransformRect : new RectF();
        position.set(rectangle);
        ViewParent parent = this.mParent;
        boolean scrolled = false;
        while (parent != null) {
            rectangle.set((int) position.left, (int) position.top, (int) position.right, (int) position.bottom);
            scrolled |= parent.requestChildRectangleOnScreen(child, rectangle, immediate);
            if (!(parent instanceof View)) {
                break;
            }
            position.offset((float) (child.mLeft - child.getScrollX()), (float) (child.mTop - child.getScrollY()));
            child = (View) parent;
            parent = child.getParent();
        }
        return scrolled;
    }

    public void clearFocus() {
        boolean refocus = sAlwaysAssignFocus || !isInTouchMode();
        clearFocusInternal(null, true, refocus);
    }

    /* Access modifiers changed, original: 0000 */
    public void clearFocusInternal(View focused, boolean propagate, boolean refocus) {
        int i = this.mPrivateFlags;
        if ((i & 2) != 0) {
            this.mPrivateFlags = i & -3;
            clearParentsWantFocus();
            if (propagate) {
                ViewParent viewParent = this.mParent;
                if (viewParent != null) {
                    viewParent.clearChildFocus(this);
                }
            }
            onFocusChanged(false, 0, null);
            refreshDrawableState();
            if (!propagate) {
                return;
            }
            if (!refocus || !rootViewRequestFocus()) {
                notifyGlobalFocusCleared(this);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void notifyGlobalFocusCleared(View oldFocus) {
        if (oldFocus != null) {
            AttachInfo attachInfo = this.mAttachInfo;
            if (attachInfo != null) {
                attachInfo.mTreeObserver.dispatchOnGlobalFocusChange(oldFocus, null);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean rootViewRequestFocus() {
        View root = getRootView();
        return root != null && root.requestFocus();
    }

    /* Access modifiers changed, original: 0000 */
    public void unFocus(View focused) {
        clearFocusInternal(focused, false, false);
    }

    @ExportedProperty(category = "focus")
    public boolean hasFocus() {
        return (this.mPrivateFlags & 2) != 0;
    }

    public boolean hasFocusable() {
        return hasFocusable(sHasFocusableExcludeAutoFocusable ^ 1, false);
    }

    public boolean hasExplicitFocusable() {
        return hasFocusable(false, true);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean hasFocusable(boolean allowAutoFocus, boolean dispatchExplicit) {
        if (!isFocusableInTouchMode()) {
            for (ViewParent p = this.mParent; p instanceof ViewGroup; p = p.getParent()) {
                if (((ViewGroup) p).shouldBlockFocusForTouchscreen()) {
                    return false;
                }
            }
        }
        int i = this.mViewFlags;
        if ((i & 12) != 0 || (i & 32) != 0) {
            return false;
        }
        if ((allowAutoFocus || getFocusable() != 16) && isFocusable()) {
            return true;
        }
        return false;
    }

    /* Access modifiers changed, original: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        if (gainFocus) {
            sendAccessibilityEvent(8);
        } else {
            notifyViewAccessibilityStateChangedIfNeeded(0);
        }
        switchDefaultFocusHighlight();
        if (gainFocus) {
            AttachInfo attachInfo = this.mAttachInfo;
            if (attachInfo != null && attachInfo.mHasWindowFocus) {
                notifyFocusChangeToInputMethodManager(true);
            }
        } else {
            if (isPressed()) {
                setPressed(false);
            }
            AttachInfo attachInfo2 = this.mAttachInfo;
            if (attachInfo2 != null && attachInfo2.mHasWindowFocus) {
                notifyFocusChangeToInputMethodManager(false);
            }
            onFocusLost();
        }
        invalidate(true);
        ListenerInfo li = this.mListenerInfo;
        if (!(li == null || li.mOnFocusChangeListener == null)) {
            li.mOnFocusChangeListener.onFocusChange(this, gainFocus);
        }
        AttachInfo attachInfo3 = this.mAttachInfo;
        if (attachInfo3 != null) {
            attachInfo3.mKeyDispatchState.reset(this);
        }
        notifyEnterOrExitForAutoFillIfNeeded(gainFocus);
    }

    private void notifyFocusChangeToInputMethodManager(boolean hasFocus) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(InputMethodManager.class);
        if (imm != null) {
            if (hasFocus) {
                imm.focusIn(this);
            } else {
                imm.focusOut(this);
            }
        }
    }

    public void notifyEnterOrExitForAutoFillIfNeeded(boolean enter) {
        if (canNotifyAutofillEnterExitEvent()) {
            AutofillManager afm = getAutofillManager();
            if (afm == null) {
                return;
            }
            if (enter && isFocused()) {
                if (!isLaidOut()) {
                    this.mPrivateFlags3 |= 134217728;
                } else if (isVisibleToUser()) {
                    afm.notifyViewEntered(this);
                }
            } else if (!enter && !isFocused()) {
                afm.notifyViewExited(this);
            }
        }
    }

    public void setAccessibilityPaneTitle(CharSequence accessibilityPaneTitle) {
        if (!TextUtils.equals(accessibilityPaneTitle, this.mAccessibilityPaneTitle)) {
            this.mAccessibilityPaneTitle = accessibilityPaneTitle;
            notifyViewAccessibilityStateChangedIfNeeded(8);
        }
    }

    public CharSequence getAccessibilityPaneTitle() {
        return this.mAccessibilityPaneTitle;
    }

    private boolean isAccessibilityPane() {
        return this.mAccessibilityPaneTitle != null;
    }

    public void sendAccessibilityEvent(int eventType) {
        AccessibilityDelegate accessibilityDelegate = this.mAccessibilityDelegate;
        if (accessibilityDelegate != null) {
            accessibilityDelegate.sendAccessibilityEvent(this, eventType);
        } else {
            sendAccessibilityEventInternal(eventType);
        }
    }

    public void announceForAccessibility(CharSequence text) {
        if (AccessibilityManager.getInstance(this.mContext).isEnabled() && this.mParent != null) {
            AccessibilityEvent event = AccessibilityEvent.obtain(16384);
            onInitializeAccessibilityEvent(event);
            event.getText().add(text);
            event.setContentDescription(null);
            this.mParent.requestSendAccessibilityEvent(this, event);
        }
    }

    public void sendAccessibilityEventInternal(int eventType) {
        if (AccessibilityManager.getInstance(this.mContext).isEnabled()) {
            sendAccessibilityEventUnchecked(AccessibilityEvent.obtain(eventType));
        }
    }

    public void sendAccessibilityEventUnchecked(AccessibilityEvent event) {
        AccessibilityDelegate accessibilityDelegate = this.mAccessibilityDelegate;
        if (accessibilityDelegate != null) {
            accessibilityDelegate.sendAccessibilityEventUnchecked(this, event);
        } else {
            sendAccessibilityEventUncheckedInternal(event);
        }
    }

    public void sendAccessibilityEventUncheckedInternal(AccessibilityEvent event) {
        boolean isWindowDisappearedEvent = true;
        if (!(event.getEventType() == 32) || (32 & event.getContentChangeTypes()) == 0) {
            isWindowDisappearedEvent = false;
        }
        if (isShown() || isWindowDisappearedEvent) {
            onInitializeAccessibilityEvent(event);
            if ((event.getEventType() & POPULATING_ACCESSIBILITY_EVENT_TYPES) != 0) {
                dispatchPopulateAccessibilityEvent(event);
            }
            if (getParent() != null) {
                getParent().requestSendAccessibilityEvent(this, event);
            }
        }
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityDelegate accessibilityDelegate = this.mAccessibilityDelegate;
        if (accessibilityDelegate != null) {
            return accessibilityDelegate.dispatchPopulateAccessibilityEvent(this, event);
        }
        return dispatchPopulateAccessibilityEventInternal(event);
    }

    public boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        onPopulateAccessibilityEvent(event);
        return false;
    }

    public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityDelegate accessibilityDelegate = this.mAccessibilityDelegate;
        if (accessibilityDelegate != null) {
            accessibilityDelegate.onPopulateAccessibilityEvent(this, event);
        } else {
            onPopulateAccessibilityEventInternal(event);
        }
    }

    public void onPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        if (event.getEventType() == 32 && isAccessibilityPane()) {
            event.getText().add(getAccessibilityPaneTitle());
        }
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityDelegate accessibilityDelegate = this.mAccessibilityDelegate;
        if (accessibilityDelegate != null) {
            accessibilityDelegate.onInitializeAccessibilityEvent(this, event);
        } else {
            onInitializeAccessibilityEventInternal(event);
        }
    }

    @UnsupportedAppUsage
    public void onInitializeAccessibilityEventInternal(AccessibilityEvent event) {
        event.setSource(this);
        event.setClassName(getAccessibilityClassName());
        event.setPackageName(getContext().getPackageName());
        event.setEnabled(isEnabled());
        event.setContentDescription(this.mContentDescription);
        int eventType = event.getEventType();
        if (eventType == 8) {
            AttachInfo attachInfo = this.mAttachInfo;
            ArrayList<View> focusablesTempList = attachInfo != null ? attachInfo.mTempArrayList : new ArrayList();
            getRootView().addFocusables(focusablesTempList, 2, 0);
            event.setItemCount(focusablesTempList.size());
            event.setCurrentItemIndex(focusablesTempList.indexOf(this));
            if (this.mAttachInfo != null) {
                focusablesTempList.clear();
            }
        } else if (eventType == 8192) {
            CharSequence text = getIterableTextForAccessibility();
            if (text != null && text.length() > 0) {
                event.setFromIndex(getAccessibilitySelectionStart());
                event.setToIndex(getAccessibilitySelectionEnd());
                event.setItemCount(text.length());
            }
        }
    }

    public AccessibilityNodeInfo createAccessibilityNodeInfo() {
        AccessibilityDelegate accessibilityDelegate = this.mAccessibilityDelegate;
        if (accessibilityDelegate != null) {
            return accessibilityDelegate.createAccessibilityNodeInfo(this);
        }
        return createAccessibilityNodeInfoInternal();
    }

    public AccessibilityNodeInfo createAccessibilityNodeInfoInternal() {
        AccessibilityNodeProvider provider = getAccessibilityNodeProvider();
        if (provider != null) {
            return provider.createAccessibilityNodeInfo(-1);
        }
        AccessibilityNodeInfo info = AccessibilityNodeInfo.obtain(this);
        onInitializeAccessibilityNodeInfo(info);
        return info;
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        AccessibilityDelegate accessibilityDelegate = this.mAccessibilityDelegate;
        if (accessibilityDelegate != null) {
            accessibilityDelegate.onInitializeAccessibilityNodeInfo(this, info);
        } else {
            onInitializeAccessibilityNodeInfoInternal(info);
        }
    }

    @UnsupportedAppUsage
    public void getBoundsOnScreen(Rect outRect) {
        getBoundsOnScreen(outRect, false);
    }

    @UnsupportedAppUsage
    public void getBoundsOnScreen(Rect outRect, boolean clipToParent) {
        RectF position = this.mAttachInfo;
        if (position != null) {
            position = position.mTmpTransformRect;
            position.set(0.0f, 0.0f, (float) (this.mRight - this.mLeft), (float) (this.mBottom - this.mTop));
            mapRectFromViewToScreenCoords(position, clipToParent);
            outRect.set(Math.round(position.left), Math.round(position.top), Math.round(position.right), Math.round(position.bottom));
        }
    }

    public void mapRectFromViewToScreenCoords(RectF rect, boolean clipToParent) {
        if (!hasIdentityMatrix()) {
            getMatrix().mapRect(rect);
        }
        rect.offset((float) this.mLeft, (float) this.mTop);
        ViewParent parent = this.mParent;
        while (parent instanceof View) {
            View parentView = (View) parent;
            rect.offset((float) (-parentView.mScrollX), (float) (-parentView.mScrollY));
            if (clipToParent) {
                rect.left = Math.max(rect.left, 0.0f);
                rect.top = Math.max(rect.top, 0.0f);
                rect.right = Math.min(rect.right, (float) parentView.getWidth());
                rect.bottom = Math.min(rect.bottom, (float) parentView.getHeight());
            }
            if (!parentView.hasIdentityMatrix()) {
                parentView.getMatrix().mapRect(rect);
            }
            rect.offset((float) parentView.mLeft, (float) parentView.mTop);
            parent = parentView.mParent;
        }
        if (parent instanceof ViewRootImpl) {
            rect.offset(0.0f, (float) (-((ViewRootImpl) parent).mCurScrollY));
        }
        rect.offset((float) this.mAttachInfo.mWindowLeft, (float) this.mAttachInfo.mWindowTop);
    }

    public CharSequence getAccessibilityClassName() {
        return View.class.getName();
    }

    public void onProvideStructure(ViewStructure structure) {
        onProvideStructure(structure, 0, 0);
    }

    public void onProvideAutofillStructure(ViewStructure structure, int flags) {
        onProvideStructure(structure, 1, flags);
    }

    /* Access modifiers changed, original: protected */
    public void onProvideStructure(ViewStructure structure, int viewFor, int flags) {
        int autofillType;
        int ignoredParentLeft;
        int ignoredParentTop;
        ViewStructure viewStructure = structure;
        int i = viewFor;
        int id = this.mID;
        String str = null;
        if (id == -1 || isViewIdGenerated(id)) {
            viewStructure.setId(id, null, null, null);
        } else {
            String entry;
            String type;
            String pkg;
            try {
                Resources res = getResources();
                entry = res.getResourceEntryName(id);
                type = res.getResourceTypeName(id);
                str = res.getResourcePackageName(id);
                pkg = str;
            } catch (NotFoundException e) {
                type = str;
                entry = str;
                pkg = str;
            }
            viewStructure.setId(id, pkg, type, entry);
        }
        if (i == 1 || i == 2) {
            autofillType = getAutofillType();
            if (autofillType != 0) {
                viewStructure.setAutofillType(autofillType);
                viewStructure.setAutofillHints(getAutofillHints());
                viewStructure.setAutofillValue(getAutofillValue());
            }
            viewStructure.setImportantForAutofill(getImportantForAutofill());
        }
        autofillType = 0;
        int ignoredParentTop2 = 0;
        if (i == 1 && (flags & 1) == 0) {
            View parentGroup = null;
            ViewParent viewParent = getParent();
            if (viewParent instanceof View) {
                parentGroup = (View) viewParent;
            }
            while (parentGroup != null && !parentGroup.isImportantForAutofill()) {
                autofillType += parentGroup.mLeft;
                ignoredParentTop2 += parentGroup.mTop;
                viewParent = parentGroup.getParent();
                if (!(viewParent instanceof View)) {
                    ignoredParentLeft = autofillType;
                    ignoredParentTop = ignoredParentTop2;
                    break;
                }
                parentGroup = (View) viewParent;
            }
        }
        ignoredParentLeft = autofillType;
        ignoredParentTop = ignoredParentTop2;
        autofillType = this.mLeft;
        ignoredParentTop2 = ignoredParentLeft + autofillType;
        int i2 = this.mTop;
        structure.setDimens(ignoredParentTop2, ignoredParentTop + i2, this.mScrollX, this.mScrollY, this.mRight - autofillType, this.mBottom - i2);
        if (i == 0) {
            if (!hasIdentityMatrix()) {
                viewStructure.setTransformation(getMatrix());
            }
            viewStructure.setElevation(getZ());
        }
        viewStructure.setVisibility(getVisibility());
        viewStructure.setEnabled(isEnabled());
        if (isClickable()) {
            viewStructure.setClickable(true);
        }
        if (isFocusable()) {
            viewStructure.setFocusable(true);
        }
        if (isFocused()) {
            viewStructure.setFocused(true);
        }
        if (isAccessibilityFocused()) {
            viewStructure.setAccessibilityFocused(true);
        }
        if (isSelected()) {
            viewStructure.setSelected(true);
        }
        if (isActivated()) {
            viewStructure.setActivated(true);
        }
        if (isLongClickable()) {
            viewStructure.setLongClickable(true);
        }
        if (this instanceof Checkable) {
            viewStructure.setCheckable(true);
            if (((Checkable) this).isChecked()) {
                viewStructure.setChecked(true);
            }
        }
        if (isOpaque()) {
            viewStructure.setOpaque(true);
        }
        if (isContextClickable()) {
            viewStructure.setContextClickable(true);
        }
        viewStructure.setClassName(getAccessibilityClassName().toString());
        viewStructure.setContentDescription(getContentDescription());
    }

    public void onProvideVirtualStructure(ViewStructure structure) {
        onProvideVirtualStructureCompat(structure, false);
    }

    private void onProvideVirtualStructureCompat(ViewStructure structure, boolean forAutofill) {
        AccessibilityNodeProvider provider = getAccessibilityNodeProvider();
        if (provider != null) {
            if (forAutofill) {
                String str = AUTOFILL_LOG_TAG;
                if (Log.isLoggable(str, 2)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("onProvideVirtualStructureCompat() for ");
                    stringBuilder.append(this);
                    Log.v(str, stringBuilder.toString());
                }
            }
            AccessibilityNodeInfo info = createAccessibilityNodeInfo();
            structure.setChildCount(1);
            populateVirtualStructure(structure.newChild(null), provider, info, forAutofill);
            info.recycle();
        }
    }

    public void onProvideAutofillVirtualStructure(ViewStructure structure, int flags) {
        if (this.mContext.isAutofillCompatibilityEnabled()) {
            onProvideVirtualStructureCompat(structure, true);
        }
    }

    public void autofill(AutofillValue value) {
    }

    public void autofill(SparseArray<AutofillValue> values) {
        if (this.mContext.isAutofillCompatibilityEnabled()) {
            AccessibilityNodeProvider provider = getAccessibilityNodeProvider();
            if (provider != null) {
                int valueCount = values.size();
                for (int i = 0; i < valueCount; i++) {
                    AutofillValue value = (AutofillValue) values.valueAt(i);
                    if (value.isText()) {
                        int virtualId = values.keyAt(i);
                        CharSequence text = value.getTextValue();
                        Bundle arguments = new Bundle();
                        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
                        provider.performAction(virtualId, 2097152, arguments);
                    }
                }
            }
        }
    }

    public final AutofillId getAutofillId() {
        if (this.mAutofillId == null) {
            this.mAutofillId = new AutofillId(getAutofillViewId());
        }
        return this.mAutofillId;
    }

    public void setAutofillId(AutofillId id) {
        String str = AUTOFILL_LOG_TAG;
        if (Log.isLoggable(str, 2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("setAutofill(): from ");
            stringBuilder.append(this.mAutofillId);
            stringBuilder.append(" to ");
            stringBuilder.append(id);
            Log.v(str, stringBuilder.toString());
        }
        if (isAttachedToWindow()) {
            throw new IllegalStateException("Cannot set autofill id when view is attached");
        } else if (id != null && !id.isNonVirtual()) {
            throw new IllegalStateException("Cannot set autofill id assigned to virtual views");
        } else if (id != null || (this.mPrivateFlags3 & 1073741824) != 0) {
            this.mAutofillId = id;
            if (id != null) {
                this.mAutofillViewId = id.getViewId();
                this.mPrivateFlags3 = 1073741824 | this.mPrivateFlags3;
            } else {
                this.mAutofillViewId = -1;
                this.mPrivateFlags3 &= -1073741825;
            }
        }
    }

    public int getAutofillType() {
        return 0;
    }

    @ExportedProperty
    public String[] getAutofillHints() {
        return this.mAutofillHints;
    }

    public boolean isAutofilled() {
        return (this.mPrivateFlags3 & 65536) != 0;
    }

    public AutofillValue getAutofillValue() {
        return null;
    }

    @ExportedProperty(mapping = {@IntToString(from = 0, to = "auto"), @IntToString(from = 1, to = "yes"), @IntToString(from = 2, to = "no"), @IntToString(from = 4, to = "yesExcludeDescendants"), @IntToString(from = 8, to = "noExcludeDescendants")})
    public int getImportantForAutofill() {
        return (this.mPrivateFlags3 & PFLAG3_IMPORTANT_FOR_AUTOFILL_MASK) >> 19;
    }

    public void setImportantForAutofill(int mode) {
        this.mPrivateFlags3 &= -7864321;
        this.mPrivateFlags3 |= (mode << 19) & PFLAG3_IMPORTANT_FOR_AUTOFILL_MASK;
    }

    public final boolean isImportantForAutofill() {
        String str;
        String str2;
        int parentImportance;
        StringBuilder stringBuilder;
        ViewParent parent = this.mParent;
        while (true) {
            boolean z = parent instanceof View;
            str = "View (";
            str2 = AUTOFILL_LOG_TAG;
            if (z) {
                parentImportance = ((View) parent).getImportantForAutofill();
                if (parentImportance != 8 && parentImportance != 4) {
                    parent = parent.getParent();
                }
            } else {
                parentImportance = getImportantForAutofill();
                if (parentImportance == 4 || parentImportance == 1) {
                    return true;
                }
                if (parentImportance == 8 || parentImportance == 2) {
                    if (Log.isLoggable(str2, 2)) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str);
                        stringBuilder.append(this);
                        stringBuilder.append(") is not important for autofill because its importance is ");
                        stringBuilder.append(parentImportance);
                        Log.v(str2, stringBuilder.toString());
                    }
                    return false;
                } else if (parentImportance != 0) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("invalid autofill importance (");
                    stringBuilder2.append(parentImportance);
                    stringBuilder2.append(" on view ");
                    stringBuilder2.append(this);
                    Log.w(str2, stringBuilder2.toString());
                    return false;
                } else {
                    int id = this.mID;
                    if (!(id == -1 || isViewIdGenerated(id))) {
                        Resources res = getResources();
                        String entry = null;
                        String pkg = null;
                        try {
                            entry = res.getResourceEntryName(id);
                            pkg = res.getResourcePackageName(id);
                        } catch (NotFoundException e) {
                        }
                        if (!(entry == null || pkg == null || !pkg.equals(this.mContext.getPackageName()))) {
                            return true;
                        }
                    }
                    if (getAutofillHints() != null) {
                        return true;
                    }
                    return false;
                }
            }
        }
        if (Log.isLoggable(str2, 2)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(this);
            stringBuilder.append(") is not important for autofill because parent ");
            stringBuilder.append(parent);
            stringBuilder.append("'s importance is ");
            stringBuilder.append(parentImportance);
            Log.v(str2, stringBuilder.toString());
        }
        return false;
    }

    public void setContentCaptureSession(ContentCaptureSession contentCaptureSession) {
        this.mContentCaptureSession = contentCaptureSession;
    }

    public final ContentCaptureSession getContentCaptureSession() {
        ContentCaptureSession contentCaptureSession = this.mCachedContentCaptureSession;
        if (contentCaptureSession != null) {
            return contentCaptureSession;
        }
        this.mCachedContentCaptureSession = getAndCacheContentCaptureSession();
        return this.mCachedContentCaptureSession;
    }

    private ContentCaptureSession getAndCacheContentCaptureSession() {
        ContentCaptureSession contentCaptureSession = this.mContentCaptureSession;
        if (contentCaptureSession != null) {
            return contentCaptureSession;
        }
        contentCaptureSession = null;
        ViewParent viewParent = this.mParent;
        if (viewParent instanceof View) {
            contentCaptureSession = ((View) viewParent).getContentCaptureSession();
        }
        if (contentCaptureSession != null) {
            return contentCaptureSession;
        }
        ContentCaptureManager ccm = (ContentCaptureManager) this.mContext.getSystemService(ContentCaptureManager.class);
        return ccm == null ? null : ccm.getMainContentCaptureSession();
    }

    private AutofillManager getAutofillManager() {
        return (AutofillManager) this.mContext.getSystemService(AutofillManager.class);
    }

    private boolean isAutofillable() {
        boolean z = false;
        if (getAutofillType() == 0) {
            return false;
        }
        if (!isImportantForAutofill()) {
            AutofillOptions options = this.mContext.getAutofillOptions();
            if (options == null || !options.isAugmentedAutofillEnabled(this.mContext)) {
                return false;
            }
            AutofillManager afm = getAutofillManager();
            if (afm == null) {
                return false;
            }
            afm.notifyViewEnteredForAugmentedAutofill(this);
        }
        if (getAutofillViewId() > LAST_APP_AUTOFILL_ID) {
            z = true;
        }
        return z;
    }

    public boolean canNotifyAutofillEnterExitEvent() {
        return isAutofillable() && isAttachedToWindow();
    }

    private void populateVirtualStructure(ViewStructure structure, AccessibilityNodeProvider provider, AccessibilityNodeInfo info, boolean forAutofill) {
        int inputType;
        String str = null;
        structure.setId(AccessibilityNodeInfo.getVirtualDescendantId(info.getSourceNodeId()), null, null, info.getViewIdResourceName());
        Rect rect = structure.getTempRect();
        info.getBoundsInParent(rect);
        structure.setDimens(rect.left, rect.top, 0, 0, rect.width(), rect.height());
        structure.setVisibility(0);
        structure.setEnabled(info.isEnabled());
        if (info.isClickable()) {
            structure.setClickable(true);
        }
        if (info.isFocusable()) {
            structure.setFocusable(true);
        }
        if (info.isFocused()) {
            structure.setFocused(true);
        }
        if (info.isAccessibilityFocused()) {
            structure.setAccessibilityFocused(true);
        }
        if (info.isSelected()) {
            structure.setSelected(true);
        }
        if (info.isLongClickable()) {
            structure.setLongClickable(true);
        }
        if (info.isCheckable()) {
            structure.setCheckable(true);
            if (info.isChecked()) {
                structure.setChecked(true);
            }
        }
        if (info.isContextClickable()) {
            structure.setContextClickable(true);
        }
        if (forAutofill) {
            structure.setAutofillId(new AutofillId(getAutofillId(), AccessibilityNodeInfo.getVirtualDescendantId(info.getSourceNodeId())));
        }
        CharSequence cname = info.getClassName();
        if (cname != null) {
            str = cname.toString();
        }
        structure.setClassName(str);
        structure.setContentDescription(info.getContentDescription());
        if (forAutofill) {
            int maxTextLength = info.getMaxTextLength();
            if (maxTextLength != -1) {
                structure.setMaxTextLength(maxTextLength);
            }
            structure.setHint(info.getHintText());
        }
        CharSequence text = info.getText();
        boolean hasText = (text == null && info.getError() == null) ? false : true;
        if (hasText) {
            structure.setText(text, info.getTextSelectionStart(), info.getTextSelectionEnd());
        }
        if (forAutofill) {
            if (info.isEditable()) {
                structure.setDataIsSensitive(true);
                if (hasText) {
                    structure.setAutofillType(1);
                    structure.setAutofillValue(AutofillValue.forText(text));
                }
                inputType = info.getInputType();
                if (inputType == 0 && info.isPassword()) {
                    inputType = 129;
                }
                structure.setInputType(inputType);
            } else {
                structure.setDataIsSensitive(false);
            }
        }
        inputType = info.getChildCount();
        if (inputType > 0) {
            structure.setChildCount(inputType);
            for (int i = 0; i < inputType; i++) {
                if (AccessibilityNodeInfo.getVirtualDescendantId(info.getChildNodeIds().get(i)) == -1) {
                    Log.e(VIEW_LOG_TAG, "Virtual view pointing to its host. Ignoring");
                } else {
                    AccessibilityNodeInfo cinfo = provider.createAccessibilityNodeInfo(AccessibilityNodeInfo.getVirtualDescendantId(info.getChildId(i)));
                    populateVirtualStructure(structure.newChild(i), provider, cinfo, forAutofill);
                    cinfo.recycle();
                }
            }
        }
    }

    public void dispatchProvideStructure(ViewStructure structure) {
        dispatchProvideStructure(structure, 0, 0);
    }

    public void dispatchProvideAutofillStructure(ViewStructure structure, int flags) {
        dispatchProvideStructure(structure, 1, flags);
    }

    private void dispatchProvideStructure(ViewStructure structure, int viewFor, int flags) {
        if (viewFor == 1) {
            structure.setAutofillId(getAutofillId());
            onProvideAutofillStructure(structure, flags);
            onProvideAutofillVirtualStructure(structure, flags);
        } else if (isAssistBlocked()) {
            structure.setClassName(getAccessibilityClassName().toString());
            structure.setAssistBlocked(true);
        } else {
            onProvideStructure(structure);
            onProvideVirtualStructure(structure);
        }
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        Rect bounds = this.mAttachInfo;
        if (bounds != null) {
            View rootView;
            View label;
            bounds = bounds.mTmpInvalRect;
            getDrawingRect(bounds);
            info.setBoundsInParent(bounds);
            getBoundsOnScreen(bounds, true);
            info.setBoundsInScreen(bounds);
            ViewParent parent = getParentForAccessibility();
            if (parent instanceof View) {
                info.setParent((View) parent);
            }
            if (this.mID != -1) {
                rootView = getRootView();
                if (rootView == null) {
                    rootView = this;
                }
                label = rootView.findLabelForView(this, this.mID);
                if (label != null) {
                    info.setLabeledBy(label);
                }
                if ((this.mAttachInfo.mAccessibilityFetchFlags & 16) != 0 && Resources.resourceHasPackage(this.mID)) {
                    try {
                        info.setViewIdResourceName(getResources().getResourceName(this.mID));
                    } catch (NotFoundException e) {
                    }
                }
            }
            if (this.mLabelForId != -1) {
                rootView = getRootView();
                if (rootView == null) {
                    rootView = this;
                }
                label = rootView.findViewInsideOutShouldExist(this, this.mLabelForId);
                if (label != null) {
                    info.setLabelFor(label);
                }
            }
            if (this.mAccessibilityTraversalBeforeId != -1) {
                rootView = getRootView();
                if (rootView == null) {
                    rootView = this;
                }
                label = rootView.findViewInsideOutShouldExist(this, this.mAccessibilityTraversalBeforeId);
                if (label != null && label.includeForAccessibility()) {
                    info.setTraversalBefore(label);
                }
            }
            if (this.mAccessibilityTraversalAfterId != -1) {
                rootView = getRootView();
                if (rootView == null) {
                    rootView = this;
                }
                View next = rootView.findViewInsideOutShouldExist(this, this.mAccessibilityTraversalAfterId);
                if (next != null && next.includeForAccessibility()) {
                    info.setTraversalAfter(next);
                }
            }
            info.setVisibleToUser(isVisibleToUser());
            info.setImportantForAccessibility(isImportantForAccessibility());
            info.setPackageName(this.mContext.getPackageName());
            info.setClassName(getAccessibilityClassName());
            info.setContentDescription(getContentDescription());
            info.setEnabled(isEnabled());
            info.setClickable(isClickable());
            info.setFocusable(isFocusable());
            info.setScreenReaderFocusable(isScreenReaderFocusable());
            info.setFocused(isFocused());
            info.setAccessibilityFocused(isAccessibilityFocused());
            info.setSelected(isSelected());
            info.setLongClickable(isLongClickable());
            info.setContextClickable(isContextClickable());
            info.setLiveRegion(getAccessibilityLiveRegion());
            TooltipInfo tooltipInfo = this.mTooltipInfo;
            if (!(tooltipInfo == null || tooltipInfo.mTooltipText == null)) {
                AccessibilityAction accessibilityAction;
                info.setTooltipText(this.mTooltipInfo.mTooltipText);
                if (this.mTooltipInfo.mTooltipPopup == null) {
                    accessibilityAction = AccessibilityAction.ACTION_SHOW_TOOLTIP;
                } else {
                    accessibilityAction = AccessibilityAction.ACTION_HIDE_TOOLTIP;
                }
                info.addAction(accessibilityAction);
            }
            info.addAction(4);
            info.addAction(8);
            if (isFocusable()) {
                if (isFocused()) {
                    info.addAction(2);
                } else {
                    info.addAction(1);
                }
            }
            if (isAccessibilityFocused()) {
                info.addAction(128);
            } else {
                info.addAction(64);
            }
            if (isClickable() && isEnabled()) {
                info.addAction(16);
            }
            if (isLongClickable() && isEnabled()) {
                info.addAction(32);
            }
            if (isContextClickable() && isEnabled()) {
                info.addAction(AccessibilityAction.ACTION_CONTEXT_CLICK);
            }
            CharSequence text = getIterableTextForAccessibility();
            if (text != null && text.length() > 0) {
                info.setTextSelection(getAccessibilitySelectionStart(), getAccessibilitySelectionEnd());
                info.addAction(131072);
                info.addAction(256);
                info.addAction(512);
                info.setMovementGranularities(11);
            }
            info.addAction(AccessibilityAction.ACTION_SHOW_ON_SCREEN);
            populateAccessibilityNodeInfoDrawingOrderInParent(info);
            info.setPaneTitle(this.mAccessibilityPaneTitle);
            info.setHeading(isAccessibilityHeading());
            TouchDelegate touchDelegate = this.mTouchDelegate;
            if (touchDelegate != null) {
                info.setTouchDelegateInfo(touchDelegate.getTouchDelegateInfo());
            }
        }
    }

    public void addExtraDataToAccessibilityNodeInfo(AccessibilityNodeInfo info, String extraDataKey, Bundle arguments) {
    }

    private void populateAccessibilityNodeInfoDrawingOrderInParent(AccessibilityNodeInfo info) {
        if ((this.mPrivateFlags & 16) == 0) {
            info.setDrawingOrder(0);
            return;
        }
        int drawingOrderInParent = 1;
        View viewAtDrawingLevel = this;
        View parent = getParentForAccessibility();
        while (viewAtDrawingLevel != parent) {
            ViewParent currentParent = viewAtDrawingLevel.getParent();
            if (!(currentParent instanceof ViewGroup)) {
                drawingOrderInParent = 0;
                break;
            }
            ViewGroup parentGroup = (ViewGroup) currentParent;
            int childCount = parentGroup.getChildCount();
            if (childCount > 1) {
                List<View> preorderedList = parentGroup.buildOrderedChildList();
                if (preorderedList != null) {
                    for (int i = 0; i < preorderedList.indexOf(viewAtDrawingLevel); i++) {
                        drawingOrderInParent += numViewsForAccessibility((View) preorderedList.get(i));
                    }
                } else {
                    int childIndex = parentGroup.indexOfChild(viewAtDrawingLevel);
                    boolean customOrder = parentGroup.isChildrenDrawingOrderEnabled();
                    int childDrawIndex = (childIndex < 0 || !customOrder) ? childIndex : parentGroup.getChildDrawingOrder(childCount, childIndex);
                    int numChildrenToIterate = customOrder ? childCount : childDrawIndex;
                    if (childDrawIndex != 0) {
                        int i2 = 0;
                        while (i2 < numChildrenToIterate) {
                            if ((customOrder ? parentGroup.getChildDrawingOrder(childCount, i2) : i2) < childDrawIndex) {
                                drawingOrderInParent += numViewsForAccessibility(parentGroup.getChildAt(i2));
                            }
                            i2++;
                        }
                    }
                }
            }
            viewAtDrawingLevel = (View) currentParent;
        }
        info.setDrawingOrder(drawingOrderInParent);
    }

    private static int numViewsForAccessibility(View view) {
        if (view != null) {
            if (view.includeForAccessibility()) {
                return 1;
            }
            if (view instanceof ViewGroup) {
                return ((ViewGroup) view).getNumChildrenForAccessibility();
            }
        }
        return 0;
    }

    private View findLabelForView(View view, int labeledId) {
        if (this.mMatchLabelForPredicate == null) {
            this.mMatchLabelForPredicate = new MatchLabelForPredicate();
        }
        this.mMatchLabelForPredicate.mLabeledId = labeledId;
        return findViewByPredicateInsideOut(view, this.mMatchLabelForPredicate);
    }

    public boolean isVisibleToUserForAutofill(int virtualId) {
        if (!this.mContext.isAutofillCompatibilityEnabled()) {
            return true;
        }
        AccessibilityNodeProvider provider = getAccessibilityNodeProvider();
        if (provider != null) {
            AccessibilityNodeInfo node = provider.createAccessibilityNodeInfo(virtualId);
            if (node != null) {
                return node.isVisibleToUser();
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("isVisibleToUserForAutofill(");
        stringBuilder.append(virtualId);
        stringBuilder.append("): no provider");
        Log.w(VIEW_LOG_TAG, stringBuilder.toString());
        return false;
    }

    @UnsupportedAppUsage
    public boolean isVisibleToUser() {
        return isVisibleToUser(null);
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public boolean isVisibleToUser(Rect boundInView) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo == null || attachInfo.mWindowVisibility != 0) {
            return false;
        }
        View current = this;
        while (current instanceof View) {
            View view = current;
            if (view.getAlpha() <= 0.0f || view.getTransitionAlpha() <= 0.0f || view.getVisibility() != 0) {
                return false;
            }
            current = view.mParent;
        }
        Rect visibleRect = this.mAttachInfo.mTmpInvalRect;
        Point offset = this.mAttachInfo.mPoint;
        if (!getGlobalVisibleRect(visibleRect, offset)) {
            return false;
        }
        if (boundInView == null) {
            return true;
        }
        visibleRect.offset(-offset.x, -offset.y);
        return boundInView.intersect(visibleRect);
    }

    public AccessibilityDelegate getAccessibilityDelegate() {
        return this.mAccessibilityDelegate;
    }

    public void setAccessibilityDelegate(AccessibilityDelegate delegate) {
        this.mAccessibilityDelegate = delegate;
    }

    public AccessibilityNodeProvider getAccessibilityNodeProvider() {
        AccessibilityDelegate accessibilityDelegate = this.mAccessibilityDelegate;
        if (accessibilityDelegate != null) {
            return accessibilityDelegate.getAccessibilityNodeProvider(this);
        }
        return null;
    }

    @UnsupportedAppUsage
    public int getAccessibilityViewId() {
        if (this.mAccessibilityViewId == -1) {
            int i = sNextAccessibilityViewId;
            sNextAccessibilityViewId = i + 1;
            this.mAccessibilityViewId = i;
        }
        return this.mAccessibilityViewId;
    }

    public int getAutofillViewId() {
        if (this.mAutofillViewId == -1) {
            this.mAutofillViewId = this.mContext.getNextAutofillId();
        }
        return this.mAutofillViewId;
    }

    public int getAccessibilityWindowId() {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            return attachInfo.mAccessibilityWindowId;
        }
        return -1;
    }

    @ExportedProperty(category = "accessibility")
    public CharSequence getContentDescription() {
        return this.mContentDescription;
    }

    @RemotableViewMethod
    public void setContentDescription(CharSequence contentDescription) {
        CharSequence charSequence = this.mContentDescription;
        if (charSequence == null) {
            if (contentDescription == null) {
                return;
            }
        } else if (charSequence.equals(contentDescription)) {
            return;
        }
        this.mContentDescription = contentDescription;
        boolean nonEmptyDesc = contentDescription != null && contentDescription.length() > 0;
        if (nonEmptyDesc && getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
            notifySubtreeAccessibilityStateChangedIfNeeded();
        } else {
            notifyViewAccessibilityStateChangedIfNeeded(4);
        }
    }

    @RemotableViewMethod
    public void setAccessibilityTraversalBefore(int beforeId) {
        if (this.mAccessibilityTraversalBeforeId != beforeId) {
            this.mAccessibilityTraversalBeforeId = beforeId;
            notifyViewAccessibilityStateChangedIfNeeded(0);
        }
    }

    public int getAccessibilityTraversalBefore() {
        return this.mAccessibilityTraversalBeforeId;
    }

    @RemotableViewMethod
    public void setAccessibilityTraversalAfter(int afterId) {
        if (this.mAccessibilityTraversalAfterId != afterId) {
            this.mAccessibilityTraversalAfterId = afterId;
            notifyViewAccessibilityStateChangedIfNeeded(0);
        }
    }

    public int getAccessibilityTraversalAfter() {
        return this.mAccessibilityTraversalAfterId;
    }

    @ExportedProperty(category = "accessibility")
    public int getLabelFor() {
        return this.mLabelForId;
    }

    @RemotableViewMethod
    public void setLabelFor(int id) {
        if (this.mLabelForId != id) {
            this.mLabelForId = id;
            if (this.mLabelForId != -1 && this.mID == -1) {
                this.mID = generateViewId();
            }
            notifyViewAccessibilityStateChangedIfNeeded(0);
        }
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public void onFocusLost() {
        resetPressedState();
    }

    private void resetPressedState() {
        if ((this.mViewFlags & 32) != 32 && isPressed()) {
            setPressed(false);
            if (!this.mHasPerformedLongPress) {
                removeLongPressCallback();
            }
        }
    }

    @ExportedProperty(category = "focus")
    public boolean isFocused() {
        return (this.mPrivateFlags & 2) != 0;
    }

    public View findFocus() {
        return (this.mPrivateFlags & 2) != 0 ? this : null;
    }

    public boolean isScrollContainer() {
        return (this.mPrivateFlags & 1048576) != 0;
    }

    public void setScrollContainer(boolean isScrollContainer) {
        if (isScrollContainer) {
            AttachInfo attachInfo = this.mAttachInfo;
            if (attachInfo != null && (this.mPrivateFlags & 1048576) == 0) {
                attachInfo.mScrollContainers.add(this);
                this.mPrivateFlags = 1048576 | this.mPrivateFlags;
            }
            this.mPrivateFlags |= 524288;
            return;
        }
        if ((1048576 & this.mPrivateFlags) != 0) {
            this.mAttachInfo.mScrollContainers.remove(this);
        }
        this.mPrivateFlags &= -1572865;
    }

    @Deprecated
    public int getDrawingCacheQuality() {
        return this.mViewFlags & DRAWING_CACHE_QUALITY_MASK;
    }

    @Deprecated
    public void setDrawingCacheQuality(int quality) {
        setFlags(quality, DRAWING_CACHE_QUALITY_MASK);
    }

    public boolean getKeepScreenOn() {
        return (this.mViewFlags & 67108864) != 0;
    }

    public void setKeepScreenOn(boolean keepScreenOn) {
        setFlags(keepScreenOn ? 67108864 : 0, 67108864);
    }

    public int getNextFocusLeftId() {
        return this.mNextFocusLeftId;
    }

    public void setNextFocusLeftId(int nextFocusLeftId) {
        this.mNextFocusLeftId = nextFocusLeftId;
    }

    public int getNextFocusRightId() {
        return this.mNextFocusRightId;
    }

    public void setNextFocusRightId(int nextFocusRightId) {
        this.mNextFocusRightId = nextFocusRightId;
    }

    public int getNextFocusUpId() {
        return this.mNextFocusUpId;
    }

    public void setNextFocusUpId(int nextFocusUpId) {
        this.mNextFocusUpId = nextFocusUpId;
    }

    public int getNextFocusDownId() {
        return this.mNextFocusDownId;
    }

    public void setNextFocusDownId(int nextFocusDownId) {
        this.mNextFocusDownId = nextFocusDownId;
    }

    public int getNextFocusForwardId() {
        return this.mNextFocusForwardId;
    }

    public void setNextFocusForwardId(int nextFocusForwardId) {
        this.mNextFocusForwardId = nextFocusForwardId;
    }

    public int getNextClusterForwardId() {
        return this.mNextClusterForwardId;
    }

    public void setNextClusterForwardId(int nextClusterForwardId) {
        this.mNextClusterForwardId = nextClusterForwardId;
    }

    public boolean isShown() {
        View current = this;
        while ((current.mViewFlags & 12) == 0) {
            ViewParent parent = current.mParent;
            if (parent == null) {
                return false;
            }
            if (!(parent instanceof View)) {
                return true;
            }
            current = (View) parent;
        }
        return false;
    }

    /* Access modifiers changed, original: protected */
    @Deprecated
    public boolean fitSystemWindows(Rect insets) {
        int i = this.mPrivateFlags3;
        if ((i & 32) != 0) {
            return fitSystemWindowsInt(insets);
        }
        if (insets == null) {
            return false;
        }
        try {
            this.mPrivateFlags3 = i | 64;
            boolean isConsumed = dispatchApplyWindowInsets(new WindowInsets(insets)).isConsumed();
            return isConsumed;
        } finally {
            this.mPrivateFlags3 &= -65;
        }
    }

    private boolean fitSystemWindowsInt(Rect insets) {
        if ((this.mViewFlags & 2) != 2) {
            return false;
        }
        this.mUserPaddingStart = Integer.MIN_VALUE;
        this.mUserPaddingEnd = Integer.MIN_VALUE;
        Rect localInsets = (Rect) sThreadLocal.get();
        if (localInsets == null) {
            localInsets = new Rect();
            sThreadLocal.set(localInsets);
        }
        boolean res = computeFitSystemWindows(insets, localInsets);
        this.mUserPaddingLeftInitial = localInsets.left;
        this.mUserPaddingRightInitial = localInsets.right;
        internalSetPadding(localInsets.left, localInsets.top, localInsets.right, localInsets.bottom);
        return res;
    }

    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if ((this.mPrivateFlags3 & 64) == 0) {
            if (fitSystemWindows(insets.getSystemWindowInsetsAsRect())) {
                return insets.consumeSystemWindowInsets();
            }
        } else if (fitSystemWindowsInt(insets.getSystemWindowInsetsAsRect())) {
            return insets.consumeSystemWindowInsets();
        }
        return insets;
    }

    public void setOnApplyWindowInsetsListener(OnApplyWindowInsetsListener listener) {
        getListenerInfo().mOnApplyWindowInsetsListener = listener;
    }

    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
        try {
            this.mPrivateFlags3 |= 32;
            WindowInsets onApplyWindowInsets;
            if (this.mListenerInfo == null || this.mListenerInfo.mOnApplyWindowInsetsListener == null) {
                onApplyWindowInsets = onApplyWindowInsets(insets);
                this.mPrivateFlags3 &= -33;
                return onApplyWindowInsets;
            }
            onApplyWindowInsets = this.mListenerInfo.mOnApplyWindowInsetsListener.onApplyWindowInsets(this, insets);
            return onApplyWindowInsets;
        } finally {
            this.mPrivateFlags3 &= -33;
        }
    }

    public void setWindowInsetsAnimationListener(WindowInsetsAnimationListener listener) {
        getListenerInfo().mWindowInsetsAnimationListener = listener;
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchWindowInsetsAnimationStarted(InsetsAnimation animation) {
        ListenerInfo listenerInfo = this.mListenerInfo;
        if (listenerInfo != null && listenerInfo.mWindowInsetsAnimationListener != null) {
            this.mListenerInfo.mWindowInsetsAnimationListener.onStarted(animation);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public WindowInsets dispatchWindowInsetsAnimationProgress(WindowInsets insets) {
        ListenerInfo listenerInfo = this.mListenerInfo;
        if (listenerInfo == null || listenerInfo.mWindowInsetsAnimationListener == null) {
            return insets;
        }
        return this.mListenerInfo.mWindowInsetsAnimationListener.onProgress(insets);
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchWindowInsetsAnimationFinished(InsetsAnimation animation) {
        ListenerInfo listenerInfo = this.mListenerInfo;
        if (listenerInfo != null && listenerInfo.mWindowInsetsAnimationListener != null) {
            this.mListenerInfo.mWindowInsetsAnimationListener.onFinished(animation);
        }
    }

    public void setSystemGestureExclusionRects(List<Rect> rects) {
        if (!rects.isEmpty() || this.mListenerInfo != null) {
            ListenerInfo info = getListenerInfo();
            if (rects.isEmpty()) {
                info.mSystemGestureExclusionRects = null;
                if (info.mPositionUpdateListener != null) {
                    this.mRenderNode.removePositionUpdateListener(info.mPositionUpdateListener);
                }
            } else {
                info.mSystemGestureExclusionRects = rects;
                if (info.mPositionUpdateListener == null) {
                    info.mPositionUpdateListener = new PositionUpdateListener() {
                        public void positionChanged(long n, int l, int t, int r, int b) {
                            View.this.postUpdateSystemGestureExclusionRects();
                        }

                        public void positionLost(long frameNumber) {
                            View.this.postUpdateSystemGestureExclusionRects();
                        }
                    };
                    this.mRenderNode.addPositionUpdateListener(info.mPositionUpdateListener);
                }
            }
            postUpdateSystemGestureExclusionRects();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void postUpdateSystemGestureExclusionRects() {
        Handler h = getHandler();
        if (h != null) {
            h.postAtFrontOfQueue(new -$$Lambda$WlJa6OPA72p3gYtA3nVKC7Z1tGY(this));
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void updateSystemGestureExclusionRects() {
        AttachInfo ai = this.mAttachInfo;
        if (ai != null) {
            ai.mViewRootImpl.updateSystemGestureExclusionRectsForView(this);
        }
    }

    public List<Rect> getSystemGestureExclusionRects() {
        ListenerInfo info = this.mListenerInfo;
        if (info != null) {
            List<Rect> list = info.mSystemGestureExclusionRects;
            if (list != null) {
                return list;
            }
        }
        return Collections.emptyList();
    }

    public void getLocationInSurface(int[] location) {
        getLocationInWindow(location);
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null && attachInfo.mViewRootImpl != null) {
            location[0] = location[0] + this.mAttachInfo.mViewRootImpl.mWindowAttributes.surfaceInsets.left;
            location[1] = location[1] + this.mAttachInfo.mViewRootImpl.mWindowAttributes.surfaceInsets.top;
        }
    }

    public WindowInsets getRootWindowInsets() {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            return attachInfo.mViewRootImpl.getWindowInsets(false);
        }
        return null;
    }

    public WindowInsetsController getWindowInsetsController() {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            return attachInfo.mViewRootImpl.getInsetsController();
        }
        return null;
    }

    /* Access modifiers changed, original: protected */
    @Deprecated
    @UnsupportedAppUsage
    public boolean computeFitSystemWindows(Rect inoutInsets, Rect outLocalInsets) {
        WindowInsets innerInsets = computeSystemWindowInsets(new WindowInsets(inoutInsets), outLocalInsets);
        inoutInsets.set(innerInsets.getSystemWindowInsetsAsRect());
        return innerInsets.isSystemWindowInsetsConsumed();
    }

    public WindowInsets computeSystemWindowInsets(WindowInsets in, Rect outLocalInsets) {
        if ((this.mViewFlags & 2048) != 0) {
            AttachInfo attachInfo = this.mAttachInfo;
            if (attachInfo != null && ((attachInfo.mSystemUiVisibility & 1536) != 0 || this.mAttachInfo.mOverscanRequested)) {
                outLocalInsets.set(this.mAttachInfo.mOverscanInsets);
                return in.inset(outLocalInsets);
            }
        }
        outLocalInsets.set(in.getSystemWindowInsetsAsRect());
        return in.consumeSystemWindowInsets().inset(outLocalInsets);
    }

    public void setFitsSystemWindows(boolean fitSystemWindows) {
        setFlags(fitSystemWindows ? 2 : 0, 2);
    }

    @ExportedProperty
    public boolean getFitsSystemWindows() {
        return (this.mViewFlags & 2) == 2;
    }

    @UnsupportedAppUsage
    public boolean fitsSystemWindows() {
        return getFitsSystemWindows();
    }

    @Deprecated
    public void requestFitSystemWindows() {
        ViewParent viewParent = this.mParent;
        if (viewParent != null) {
            viewParent.requestFitSystemWindows();
        }
    }

    public void requestApplyInsets() {
        requestFitSystemWindows();
    }

    @UnsupportedAppUsage
    public void makeOptionalFitsSystemWindows() {
        setFlags(2048, 2048);
    }

    public void getOutsets(Rect outOutsetRect) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            outOutsetRect.set(attachInfo.mOutsets);
        } else {
            outOutsetRect.setEmpty();
        }
    }

    @ExportedProperty(mapping = {@IntToString(from = 0, to = "VISIBLE"), @IntToString(from = 4, to = "INVISIBLE"), @IntToString(from = 8, to = "GONE")})
    public int getVisibility() {
        return this.mViewFlags & 12;
    }

    @RemotableViewMethod
    public void setVisibility(int visibility) {
        setFlags(visibility, 12);
    }

    @ExportedProperty
    public boolean isEnabled() {
        return (this.mViewFlags & 32) == 0;
    }

    @RemotableViewMethod
    public void setEnabled(boolean enabled) {
        if (enabled != isEnabled()) {
            setFlags(enabled ? 0 : 32, 32);
            refreshDrawableState();
            invalidate(true);
            if (!enabled) {
                cancelPendingInputEvents();
            }
        }
    }

    public void setFocusable(boolean focusable) {
        setFocusable((int) focusable);
    }

    public void setFocusable(int focusable) {
        if ((focusable & 17) == 0) {
            setFlags(0, 262144);
        }
        setFlags(focusable, 17);
    }

    public void setFocusableInTouchMode(boolean focusableInTouchMode) {
        setFlags(focusableInTouchMode ? 262144 : 0, 262144);
        if (focusableInTouchMode) {
            setFlags(1, 17);
        }
    }

    public void setAutofillHints(String... autofillHints) {
        if (autofillHints == null || autofillHints.length == 0) {
            this.mAutofillHints = null;
        } else {
            this.mAutofillHints = autofillHints;
        }
    }

    public void setAutofilled(boolean isAutofilled) {
        if (isAutofilled != isAutofilled()) {
            if (isAutofilled) {
                this.mPrivateFlags3 |= 65536;
            } else {
                this.mPrivateFlags3 &= -65537;
            }
            invalidate();
        }
    }

    public void setSoundEffectsEnabled(boolean soundEffectsEnabled) {
        setFlags(soundEffectsEnabled ? 134217728 : 0, 134217728);
    }

    @ExportedProperty
    public boolean isSoundEffectsEnabled() {
        return 134217728 == (this.mViewFlags & 134217728);
    }

    public void setHapticFeedbackEnabled(boolean hapticFeedbackEnabled) {
        setFlags(hapticFeedbackEnabled ? 268435456 : 0, 268435456);
        this.mHapticEnabledExplicitly = hapticFeedbackEnabled;
    }

    @ExportedProperty
    public boolean isHapticFeedbackEnabled() {
        return 268435456 == (this.mViewFlags & 268435456);
    }

    @ExportedProperty(category = "layout", mapping = {@IntToString(from = 0, to = "LTR"), @IntToString(from = 1, to = "RTL"), @IntToString(from = 2, to = "INHERIT"), @IntToString(from = 3, to = "LOCALE")})
    public int getRawLayoutDirection() {
        return (this.mPrivateFlags2 & 12) >> 2;
    }

    @RemotableViewMethod
    public void setLayoutDirection(int layoutDirection) {
        if (getRawLayoutDirection() != layoutDirection) {
            this.mPrivateFlags2 &= -13;
            resetRtlProperties();
            this.mPrivateFlags2 |= (layoutDirection << 2) & 12;
            resolveRtlPropertiesIfNeeded();
            requestLayout();
            invalidate(true);
        }
    }

    @ExportedProperty(category = "layout", mapping = {@IntToString(from = 0, to = "RESOLVED_DIRECTION_LTR"), @IntToString(from = 1, to = "RESOLVED_DIRECTION_RTL")})
    public int getLayoutDirection() {
        int i = 0;
        if (getContext().getApplicationInfo().targetSdkVersion < 17) {
            this.mPrivateFlags2 |= 32;
            return 0;
        }
        if ((this.mPrivateFlags2 & 16) == 16) {
            i = 1;
        }
        return i;
    }

    @ExportedProperty(category = "layout")
    @UnsupportedAppUsage
    public boolean isLayoutRtl() {
        return getLayoutDirection() == 1;
    }

    @ExportedProperty(category = "layout")
    public boolean hasTransientState() {
        return (this.mPrivateFlags2 & Integer.MIN_VALUE) == Integer.MIN_VALUE;
    }

    public void setHasTransientState(boolean hasTransientState) {
        int i;
        boolean oldHasTransientState = hasTransientState();
        if (hasTransientState) {
            i = this.mTransientStateCount + 1;
        } else {
            i = this.mTransientStateCount - 1;
        }
        this.mTransientStateCount = i;
        i = this.mTransientStateCount;
        String str = VIEW_LOG_TAG;
        int i2 = 0;
        if (i < 0) {
            this.mTransientStateCount = 0;
            Log.e(str, "hasTransientState decremented below 0: unmatched pair of setHasTransientState calls");
        } else if ((hasTransientState && i == 1) || (!hasTransientState && this.mTransientStateCount == 0)) {
            int i3 = this.mPrivateFlags2 & Integer.MAX_VALUE;
            if (hasTransientState) {
                i2 = Integer.MIN_VALUE;
            }
            this.mPrivateFlags2 = i3 | i2;
            boolean newHasTransientState = hasTransientState();
            ViewParent viewParent = this.mParent;
            if (viewParent != null && newHasTransientState != oldHasTransientState) {
                try {
                    viewParent.childHasTransientStateChanged(this, newHasTransientState);
                } catch (AbstractMethodError e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(this.mParent.getClass().getSimpleName());
                    stringBuilder.append(" does not fully implement ViewParent");
                    Log.e(str, stringBuilder.toString(), e);
                }
            }
        }
    }

    public boolean isAttachedToWindow() {
        return this.mAttachInfo != null;
    }

    public boolean isLaidOut() {
        return (this.mPrivateFlags3 & 4) == 4;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isLayoutValid() {
        return isLaidOut() && (this.mPrivateFlags & 4096) == 0;
    }

    public void setWillNotDraw(boolean willNotDraw) {
        setFlags(willNotDraw ? 128 : 0, 128);
    }

    @ExportedProperty(category = "drawing")
    public boolean willNotDraw() {
        return (this.mViewFlags & 128) == 128;
    }

    @Deprecated
    public void setWillNotCacheDrawing(boolean willNotCacheDrawing) {
        setFlags(willNotCacheDrawing ? 131072 : 0, 131072);
    }

    @ExportedProperty(category = "drawing")
    @Deprecated
    public boolean willNotCacheDrawing() {
        return (this.mViewFlags & 131072) == 131072;
    }

    @ExportedProperty
    public boolean isClickable() {
        return (this.mViewFlags & 16384) == 16384;
    }

    public void setClickable(boolean clickable) {
        setFlags(clickable ? 16384 : 0, 16384);
    }

    public boolean isLongClickable() {
        return (this.mViewFlags & 2097152) == 2097152;
    }

    public void setLongClickable(boolean longClickable) {
        setFlags(longClickable ? 2097152 : 0, 2097152);
    }

    public boolean isContextClickable() {
        return (this.mViewFlags & 8388608) == 8388608;
    }

    public void setContextClickable(boolean contextClickable) {
        setFlags(contextClickable ? 8388608 : 0, 8388608);
    }

    private void setPressed(boolean pressed, float x, float y) {
        if (pressed) {
            drawableHotspotChanged(x, y);
        }
        setPressed(pressed);
    }

    public void setPressed(boolean pressed) {
        boolean z = true;
        if (pressed == ((this.mPrivateFlags & 16384) == 16384)) {
            z = false;
        }
        boolean needsRefresh = z;
        if (pressed) {
            this.mPrivateFlags = 16384 | this.mPrivateFlags;
        } else {
            this.mPrivateFlags &= -16385;
        }
        if (needsRefresh) {
            refreshDrawableState();
        }
        dispatchSetPressed(pressed);
    }

    /* Access modifiers changed, original: protected */
    public void dispatchSetPressed(boolean pressed) {
    }

    @ExportedProperty
    public boolean isPressed() {
        return (this.mPrivateFlags & 16384) == 16384;
    }

    public boolean isAssistBlocked() {
        return (this.mPrivateFlags3 & 16384) != 0;
    }

    @UnsupportedAppUsage
    public void setAssistBlocked(boolean enabled) {
        if (enabled) {
            this.mPrivateFlags3 |= 16384;
        } else {
            this.mPrivateFlags3 &= -16385;
        }
    }

    public boolean isSaveEnabled() {
        return (this.mViewFlags & 65536) != 65536;
    }

    public void setSaveEnabled(boolean enabled) {
        setFlags(enabled ? 0 : 65536, 65536);
    }

    @ExportedProperty
    public boolean getFilterTouchesWhenObscured() {
        return (this.mViewFlags & 1024) != 0;
    }

    public void setFilterTouchesWhenObscured(boolean enabled) {
        setFlags(enabled ? 1024 : 0, 1024);
    }

    public boolean isSaveFromParentEnabled() {
        return (this.mViewFlags & 536870912) != 536870912;
    }

    public void setSaveFromParentEnabled(boolean enabled) {
        setFlags(enabled ? 0 : 536870912, 536870912);
    }

    @ExportedProperty(category = "focus")
    public final boolean isFocusable() {
        return 1 == (this.mViewFlags & 1);
    }

    @ExportedProperty(category = "focus", mapping = {@IntToString(from = 0, to = "NOT_FOCUSABLE"), @IntToString(from = 1, to = "FOCUSABLE"), @IntToString(from = 16, to = "FOCUSABLE_AUTO")})
    public int getFocusable() {
        int i = this.mViewFlags;
        return (i & 16) > 0 ? 16 : i & 1;
    }

    @ExportedProperty(category = "focus")
    public final boolean isFocusableInTouchMode() {
        return 262144 == (this.mViewFlags & 262144);
    }

    public boolean isScreenReaderFocusable() {
        return (this.mPrivateFlags3 & 268435456) != 0;
    }

    public void setScreenReaderFocusable(boolean screenReaderFocusable) {
        updatePflags3AndNotifyA11yIfChanged(268435456, screenReaderFocusable);
    }

    public boolean isAccessibilityHeading() {
        return (this.mPrivateFlags3 & Integer.MIN_VALUE) != 0;
    }

    public void setAccessibilityHeading(boolean isHeading) {
        updatePflags3AndNotifyA11yIfChanged(Integer.MIN_VALUE, isHeading);
    }

    private void updatePflags3AndNotifyA11yIfChanged(int mask, boolean newValue) {
        int pflags3 = this.mPrivateFlags3;
        if (newValue) {
            pflags3 |= mask;
        } else {
            pflags3 &= ~mask;
        }
        if (pflags3 != this.mPrivateFlags3) {
            this.mPrivateFlags3 = pflags3;
            notifyViewAccessibilityStateChangedIfNeeded(0);
        }
    }

    public View focusSearch(int direction) {
        ViewParent viewParent = this.mParent;
        if (viewParent != null) {
            return viewParent.focusSearch(this, direction);
        }
        return null;
    }

    @ExportedProperty(category = "focus")
    public final boolean isKeyboardNavigationCluster() {
        return (this.mPrivateFlags3 & 32768) != 0;
    }

    /* Access modifiers changed, original: 0000 */
    public View findKeyboardNavigationCluster() {
        ViewParent viewParent = this.mParent;
        if (viewParent instanceof View) {
            View cluster = ((View) viewParent).findKeyboardNavigationCluster();
            if (cluster != null) {
                return cluster;
            }
            if (isKeyboardNavigationCluster()) {
                return this;
            }
        }
        return null;
    }

    public void setKeyboardNavigationCluster(boolean isCluster) {
        if (isCluster) {
            this.mPrivateFlags3 |= 32768;
        } else {
            this.mPrivateFlags3 &= -32769;
        }
    }

    public final void setFocusedInCluster() {
        setFocusedInCluster(findKeyboardNavigationCluster());
    }

    private void setFocusedInCluster(View cluster) {
        if (this instanceof ViewGroup) {
            ((ViewGroup) this).mFocusedInCluster = null;
        }
        if (cluster != this) {
            View child = this;
            for (View parent = this.mParent; parent instanceof ViewGroup; parent = parent.getParent()) {
                ((ViewGroup) parent).mFocusedInCluster = child;
                if (parent == cluster) {
                    break;
                }
                child = parent;
            }
        }
    }

    private void updateFocusedInCluster(View oldFocus, int direction) {
        if (oldFocus != null) {
            View oldCluster = oldFocus.findKeyboardNavigationCluster();
            if (oldCluster != findKeyboardNavigationCluster()) {
                oldFocus.setFocusedInCluster(oldCluster);
                if (!(oldFocus.mParent instanceof ViewGroup)) {
                    return;
                }
                if (direction == 2 || direction == 1) {
                    ((ViewGroup) oldFocus.mParent).clearFocusedInCluster(oldFocus);
                } else if ((oldFocus instanceof ViewGroup) && ((ViewGroup) oldFocus).getDescendantFocusability() == 262144 && ViewRootImpl.isViewDescendantOf(this, oldFocus)) {
                    ((ViewGroup) oldFocus.mParent).clearFocusedInCluster(oldFocus);
                }
            }
        }
    }

    @ExportedProperty(category = "focus")
    public final boolean isFocusedByDefault() {
        return (this.mPrivateFlags3 & 262144) != 0;
    }

    public void setFocusedByDefault(boolean isFocusedByDefault) {
        if (isFocusedByDefault != ((this.mPrivateFlags3 & 262144) != 0)) {
            if (isFocusedByDefault) {
                this.mPrivateFlags3 |= 262144;
            } else {
                this.mPrivateFlags3 &= -262145;
            }
            ViewParent viewParent = this.mParent;
            if (viewParent instanceof ViewGroup) {
                if (isFocusedByDefault) {
                    ((ViewGroup) viewParent).setDefaultFocus(this);
                } else {
                    ((ViewGroup) viewParent).clearDefaultFocus(this);
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean hasDefaultFocus() {
        return isFocusedByDefault();
    }

    public View keyboardNavigationClusterSearch(View currentCluster, int direction) {
        if (isKeyboardNavigationCluster()) {
            currentCluster = this;
        }
        if (isRootNamespace()) {
            return FocusFinder.getInstance().findNextKeyboardNavigationCluster(this, currentCluster, direction);
        }
        ViewParent viewParent = this.mParent;
        if (viewParent != null) {
            return viewParent.keyboardNavigationClusterSearch(currentCluster, direction);
        }
        return null;
    }

    public boolean dispatchUnhandledMove(View focused, int direction) {
        return false;
    }

    public void setDefaultFocusHighlightEnabled(boolean defaultFocusHighlightEnabled) {
        this.mDefaultFocusHighlightEnabled = defaultFocusHighlightEnabled;
    }

    @ExportedProperty(category = "focus")
    public final boolean getDefaultFocusHighlightEnabled() {
        return this.mDefaultFocusHighlightEnabled;
    }

    /* Access modifiers changed, original: 0000 */
    public View findUserSetNextFocus(View root, int direction) {
        int i;
        if (direction != 1) {
            if (direction == 2) {
                i = this.mNextFocusForwardId;
                if (i == -1) {
                    return null;
                }
                return findViewInsideOutShouldExist(root, i);
            } else if (direction == 17) {
                i = this.mNextFocusLeftId;
                if (i == -1) {
                    return null;
                }
                return findViewInsideOutShouldExist(root, i);
            } else if (direction == 33) {
                i = this.mNextFocusUpId;
                if (i == -1) {
                    return null;
                }
                return findViewInsideOutShouldExist(root, i);
            } else if (direction == 66) {
                i = this.mNextFocusRightId;
                if (i == -1) {
                    return null;
                }
                return findViewInsideOutShouldExist(root, i);
            } else if (direction != 130) {
                return null;
            } else {
                i = this.mNextFocusDownId;
                if (i == -1) {
                    return null;
                }
                return findViewInsideOutShouldExist(root, i);
            }
        } else if (this.mID == -1) {
            return null;
        } else {
            i = this.mID;
            return root.findViewByPredicateInsideOut(this, new Predicate<View>() {
                public boolean test(View t) {
                    return t.mNextFocusForwardId == i;
                }
            });
        }
    }

    /* Access modifiers changed, original: 0000 */
    public View findUserSetNextKeyboardNavigationCluster(View root, int direction) {
        if (direction != 1) {
            if (direction != 2) {
                return null;
            }
            int i = this.mNextClusterForwardId;
            if (i == -1) {
                return null;
            }
            return findViewInsideOutShouldExist(root, i);
        } else if (this.mID == -1) {
            return null;
        } else {
            return root.findViewByPredicateInsideOut(this, new -$$Lambda$View$7kZ4TXHKswReUMQB8098MEBcx_U(this.mID));
        }
    }

    static /* synthetic */ boolean lambda$findUserSetNextKeyboardNavigationCluster$0(int id, View t) {
        return t.mNextClusterForwardId == id;
    }

    private View findViewInsideOutShouldExist(View root, int id) {
        if (this.mMatchIdPredicate == null) {
            this.mMatchIdPredicate = new MatchIdPredicate();
        }
        View result = this.mMatchIdPredicate;
        result.mId = id;
        result = root.findViewByPredicateInsideOut(this, result);
        if (result == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("couldn't find view with id ");
            stringBuilder.append(id);
            Log.w(VIEW_LOG_TAG, stringBuilder.toString());
        }
        return result;
    }

    public ArrayList<View> getFocusables(int direction) {
        ArrayList<View> result = new ArrayList(24);
        addFocusables(result, direction);
        return result;
    }

    public void addFocusables(ArrayList<View> views, int direction) {
        addFocusables(views, direction, isInTouchMode());
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (views == null || !canTakeFocus()) {
            return;
        }
        if ((focusableMode & 1) != 1 || isFocusableInTouchMode()) {
            views.add(this);
        }
    }

    public void addKeyboardNavigationClusters(Collection<View> views, int direction) {
        if (isKeyboardNavigationCluster() && hasFocusable()) {
            views.add(this);
        }
    }

    public void findViewsWithText(ArrayList<View> outViews, CharSequence searched, int flags) {
        if (getAccessibilityNodeProvider() != null) {
            if ((flags & 4) != 0) {
                outViews.add(this);
            }
        } else if ((flags & 2) != 0 && searched != null && searched.length() > 0) {
            CharSequence charSequence = this.mContentDescription;
            if (charSequence != null && charSequence.length() > 0) {
                if (this.mContentDescription.toString().toLowerCase().contains(searched.toString().toLowerCase())) {
                    outViews.add(this);
                }
            }
        }
    }

    public ArrayList<View> getTouchables() {
        ArrayList<View> result = new ArrayList();
        addTouchables(result);
        return result;
    }

    public void addTouchables(ArrayList<View> views) {
        int viewFlags = this.mViewFlags;
        if (((viewFlags & 16384) == 16384 || (viewFlags & 2097152) == 2097152 || (viewFlags & 8388608) == 8388608) && (viewFlags & 32) == 0) {
            views.add(this);
        }
    }

    public boolean isAccessibilityFocused() {
        return (this.mPrivateFlags2 & 67108864) != 0;
    }

    /* JADX WARNING: Missing block: B:15:0x003c, code skipped:
            return false;
     */
    @android.annotation.UnsupportedAppUsage
    public boolean requestAccessibilityFocus() {
        /*
        r5 = this;
        r0 = r5.mContext;
        r0 = android.view.accessibility.AccessibilityManager.getInstance(r0);
        r1 = r0.isEnabled();
        r2 = 0;
        if (r1 == 0) goto L_0x003c;
    L_0x000d:
        r1 = r0.isTouchExplorationEnabled();
        if (r1 != 0) goto L_0x0014;
    L_0x0013:
        goto L_0x003c;
    L_0x0014:
        r1 = r5.mViewFlags;
        r1 = r1 & 12;
        if (r1 == 0) goto L_0x001b;
    L_0x001a:
        return r2;
    L_0x001b:
        r1 = r5.mPrivateFlags2;
        r3 = 67108864; // 0x4000000 float:1.5046328E-36 double:3.31561842E-316;
        r4 = r1 & r3;
        if (r4 != 0) goto L_0x003b;
    L_0x0023:
        r1 = r1 | r3;
        r5.mPrivateFlags2 = r1;
        r1 = r5.getViewRootImpl();
        if (r1 == 0) goto L_0x0030;
    L_0x002c:
        r2 = 0;
        r1.setAccessibilityFocus(r5, r2);
    L_0x0030:
        r5.invalidate();
        r2 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
        r5.sendAccessibilityEvent(r2);
        r2 = 1;
        return r2;
    L_0x003b:
        return r2;
    L_0x003c:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.View.requestAccessibilityFocus():boolean");
    }

    @UnsupportedAppUsage
    public void clearAccessibilityFocus() {
        clearAccessibilityFocusNoCallbacks(0);
        ViewRootImpl viewRootImpl = getViewRootImpl();
        if (viewRootImpl != null) {
            View focusHost = viewRootImpl.getAccessibilityFocusedHost();
            if (focusHost != null && ViewRootImpl.isViewDescendantOf(focusHost, this)) {
                viewRootImpl.setAccessibilityFocus(null, null);
            }
        }
    }

    private void sendAccessibilityHoverEvent(int eventType) {
        View source = this;
        while (!source.includeForAccessibility()) {
            ViewParent parent = source.getParent();
            if (parent instanceof View) {
                source = (View) parent;
            } else {
                return;
            }
        }
        source.sendAccessibilityEvent(eventType);
    }

    /* Access modifiers changed, original: 0000 */
    public void clearAccessibilityFocusNoCallbacks(int action) {
        int i = this.mPrivateFlags2;
        if ((67108864 & i) != 0) {
            this.mPrivateFlags2 = i & -67108865;
            invalidate();
            if (AccessibilityManager.getInstance(this.mContext).isEnabled()) {
                AccessibilityEvent event = AccessibilityEvent.obtain(65536);
                event.setAction(action);
                AccessibilityDelegate accessibilityDelegate = this.mAccessibilityDelegate;
                if (accessibilityDelegate != null) {
                    accessibilityDelegate.sendAccessibilityEventUnchecked(this, event);
                } else {
                    sendAccessibilityEventUnchecked(event);
                }
            }
        }
    }

    public final boolean requestFocus() {
        return requestFocus(130);
    }

    public boolean restoreFocusInCluster(int direction) {
        if (restoreDefaultFocus()) {
            return true;
        }
        return requestFocus(direction);
    }

    public boolean restoreFocusNotInCluster() {
        return requestFocus(130);
    }

    public boolean restoreDefaultFocus() {
        return requestFocus(130);
    }

    public final boolean requestFocus(int direction) {
        return requestFocus(direction, null);
    }

    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        return requestFocusNoSearch(direction, previouslyFocusedRect);
    }

    private boolean requestFocusNoSearch(int direction, Rect previouslyFocusedRect) {
        if (!canTakeFocus()) {
            return false;
        }
        if ((isInTouchMode() && 262144 != (this.mViewFlags & 262144)) || hasAncestorThatBlocksDescendantFocus()) {
            return false;
        }
        if (isLayoutValid()) {
            clearParentsWantFocus();
        } else {
            this.mPrivateFlags |= 1;
        }
        handleFocusGainInternal(direction, previouslyFocusedRect);
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    public void clearParentsWantFocus() {
        ViewParent viewParent = this.mParent;
        if (viewParent instanceof View) {
            View view = (View) viewParent;
            view.mPrivateFlags &= -2;
            ((View) viewParent).clearParentsWantFocus();
        }
    }

    public final boolean requestFocusFromTouch() {
        if (isInTouchMode()) {
            ViewRootImpl viewRoot = getViewRootImpl();
            if (viewRoot != null) {
                viewRoot.ensureTouchMode(false);
            }
        }
        return requestFocus(130);
    }

    private boolean hasAncestorThatBlocksDescendantFocus() {
        boolean focusableInTouchMode = isFocusableInTouchMode();
        ViewParent ancestor = this.mParent;
        while (ancestor instanceof ViewGroup) {
            ViewGroup vgAncestor = (ViewGroup) ancestor;
            if (vgAncestor.getDescendantFocusability() == 393216 || (!focusableInTouchMode && vgAncestor.shouldBlockFocusForTouchscreen())) {
                return true;
            }
            ancestor = vgAncestor.getParent();
        }
        return false;
    }

    @ExportedProperty(category = "accessibility", mapping = {@IntToString(from = 0, to = "auto"), @IntToString(from = 1, to = "yes"), @IntToString(from = 2, to = "no"), @IntToString(from = 4, to = "noHideDescendants")})
    public int getImportantForAccessibility() {
        return (this.mPrivateFlags2 & PFLAG2_IMPORTANT_FOR_ACCESSIBILITY_MASK) >> 20;
    }

    public void setAccessibilityLiveRegion(int mode) {
        if (mode != getAccessibilityLiveRegion()) {
            this.mPrivateFlags2 &= -25165825;
            this.mPrivateFlags2 |= (mode << 23) & 25165824;
            notifyViewAccessibilityStateChangedIfNeeded(0);
        }
    }

    public int getAccessibilityLiveRegion() {
        return (this.mPrivateFlags2 & 25165824) >> 23;
    }

    public void setImportantForAccessibility(int mode) {
        int oldMode = getImportantForAccessibility();
        if (mode != oldMode) {
            boolean oldIncludeForAccessibility = true;
            boolean hideDescendants = mode == 4;
            if (mode == 2 || hideDescendants) {
                View focusHost = findAccessibilityFocusHost(hideDescendants);
                if (focusHost != null) {
                    focusHost.clearAccessibilityFocus();
                }
            }
            boolean maySkipNotify = oldMode == 0 || mode == 0;
            if (!(maySkipNotify && includeForAccessibility())) {
                oldIncludeForAccessibility = false;
            }
            this.mPrivateFlags2 &= -7340033;
            this.mPrivateFlags2 |= (mode << 20) & PFLAG2_IMPORTANT_FOR_ACCESSIBILITY_MASK;
            if (maySkipNotify && oldIncludeForAccessibility == includeForAccessibility()) {
                notifyViewAccessibilityStateChangedIfNeeded(0);
            } else {
                notifySubtreeAccessibilityStateChangedIfNeeded();
            }
        }
    }

    private View findAccessibilityFocusHost(boolean searchDescendants) {
        if (isAccessibilityFocusedViewOrHost()) {
            return this;
        }
        if (searchDescendants) {
            ViewRootImpl viewRoot = getViewRootImpl();
            if (viewRoot != null) {
                View focusHost = viewRoot.getAccessibilityFocusedHost();
                if (focusHost != null && ViewRootImpl.isViewDescendantOf(focusHost, this)) {
                    return focusHost;
                }
            }
        }
        return null;
    }

    public boolean isImportantForAccessibility() {
        int mode = (this.mPrivateFlags2 & PFLAG2_IMPORTANT_FOR_ACCESSIBILITY_MASK) >> 20;
        boolean z = false;
        if (mode == 2 || mode == 4) {
            return false;
        }
        for (ViewParent parent = this.mParent; parent instanceof View; parent = parent.getParent()) {
            if (((View) parent).getImportantForAccessibility() == 4) {
                return false;
            }
        }
        if (mode == 1 || isActionableForAccessibility() || hasListenersForAccessibility() || getAccessibilityNodeProvider() != null || getAccessibilityLiveRegion() != 0 || isAccessibilityPane()) {
            z = true;
        }
        return z;
    }

    public ViewParent getParentForAccessibility() {
        View parentView = this.mParent;
        if (!(parentView instanceof View)) {
            return null;
        }
        if (parentView.includeForAccessibility()) {
            return this.mParent;
        }
        return this.mParent.getParentForAccessibility();
    }

    /* Access modifiers changed, original: 0000 */
    public View getSelfOrParentImportantForA11y() {
        if (isImportantForAccessibility()) {
            return this;
        }
        ViewParent parent = getParentForAccessibility();
        if (parent instanceof View) {
            return (View) parent;
        }
        return null;
    }

    public void addChildrenForAccessibility(ArrayList<View> arrayList) {
    }

    @UnsupportedAppUsage
    public boolean includeForAccessibility() {
        AttachInfo attachInfo = this.mAttachInfo;
        boolean z = false;
        if (attachInfo == null) {
            return false;
        }
        if ((attachInfo.mAccessibilityFetchFlags & 8) != 0 || isImportantForAccessibility()) {
            z = true;
        }
        return z;
    }

    public boolean isActionableForAccessibility() {
        return isClickable() || isLongClickable() || isFocusable();
    }

    private boolean hasListenersForAccessibility() {
        ListenerInfo info = getListenerInfo();
        return (this.mTouchDelegate == null && info.mOnKeyListener == null && info.mOnTouchListener == null && info.mOnGenericMotionListener == null && info.mOnHoverListener == null && info.mOnDragListener == null) ? false : true;
    }

    @UnsupportedAppUsage
    public void notifyViewAccessibilityStateChangedIfNeeded(int changeType) {
        if (AccessibilityManager.getInstance(this.mContext).isEnabled() && this.mAttachInfo != null) {
            String str = " does not fully implement ViewParent";
            String str2 = VIEW_LOG_TAG;
            AccessibilityEvent event;
            if (changeType != 1 && isAccessibilityPane() && (getVisibility() == 0 || changeType == 32)) {
                event = AccessibilityEvent.obtain();
                onInitializeAccessibilityEvent(event);
                event.setEventType(32);
                event.setContentChangeTypes(changeType);
                event.setSource(this);
                onPopulateAccessibilityEvent(event);
                ViewParent viewParent = this.mParent;
                if (viewParent != null) {
                    try {
                        viewParent.requestSendAccessibilityEvent(this, event);
                    } catch (AbstractMethodError e) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(this.mParent.getClass().getSimpleName());
                        stringBuilder.append(str);
                        Log.e(str2, stringBuilder.toString(), e);
                    }
                }
                return;
            }
            if (getAccessibilityLiveRegion() != 0) {
                event = AccessibilityEvent.obtain();
                event.setEventType(2048);
                event.setContentChangeTypes(changeType);
                sendAccessibilityEventUnchecked(event);
            } else {
                ViewParent viewParent2 = this.mParent;
                if (viewParent2 != null) {
                    try {
                        viewParent2.notifySubtreeAccessibilityStateChanged(this, this, changeType);
                    } catch (AbstractMethodError e2) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(this.mParent.getClass().getSimpleName());
                        stringBuilder2.append(str);
                        Log.e(str2, stringBuilder2.toString(), e2);
                    }
                }
            }
        }
    }

    @UnsupportedAppUsage
    public void notifySubtreeAccessibilityStateChangedIfNeeded() {
        if (AccessibilityManager.getInstance(this.mContext).isEnabled() && this.mAttachInfo != null) {
            int i = this.mPrivateFlags2;
            if ((i & 134217728) == 0) {
                this.mPrivateFlags2 = i | 134217728;
                ViewParent viewParent = this.mParent;
                if (viewParent != null) {
                    try {
                        viewParent.notifySubtreeAccessibilityStateChanged(this, this, 1);
                    } catch (AbstractMethodError e) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(this.mParent.getClass().getSimpleName());
                        stringBuilder.append(" does not fully implement ViewParent");
                        Log.e(VIEW_LOG_TAG, stringBuilder.toString(), e);
                    }
                }
            }
        }
    }

    public void setTransitionVisibility(int visibility) {
        this.mViewFlags = (this.mViewFlags & -13) | visibility;
    }

    /* Access modifiers changed, original: 0000 */
    public void resetSubtreeAccessibilityStateChanged() {
        this.mPrivateFlags2 &= -134217729;
    }

    public boolean dispatchNestedPrePerformAccessibilityAction(int action, Bundle arguments) {
        for (ViewParent p = getParent(); p != null; p = p.getParent()) {
            if (p.onNestedPrePerformAccessibilityAction(this, action, arguments)) {
                return true;
            }
        }
        return false;
    }

    public boolean performAccessibilityAction(int action, Bundle arguments) {
        AccessibilityDelegate accessibilityDelegate = this.mAccessibilityDelegate;
        if (accessibilityDelegate != null) {
            return accessibilityDelegate.performAccessibilityAction(this, action, arguments);
        }
        return performAccessibilityActionInternal(action, arguments);
    }

    @UnsupportedAppUsage
    public boolean performAccessibilityActionInternal(int action, Bundle arguments) {
        if (isNestedScrollingEnabled() && ((action == 8192 || action == 4096 || action == 16908344 || action == 16908345 || action == 16908346 || action == 16908347) && dispatchNestedPrePerformAccessibilityAction(action, arguments))) {
            return true;
        }
        String str = AccessibilityNodeInfo.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN;
        int granularity = AccessibilityNodeInfo.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT;
        TooltipInfo tooltipInfo;
        switch (action) {
            case 1:
                if (!hasFocus()) {
                    getViewRootImpl().ensureTouchMode(false);
                    return requestFocus();
                }
                break;
            case 2:
                if (hasFocus()) {
                    clearFocus();
                    return isFocused() ^ 1;
                }
                break;
            case 4:
                if (!isSelected()) {
                    setSelected(true);
                    return isSelected();
                }
                break;
            case 8:
                if (isSelected()) {
                    setSelected(false);
                    return isSelected() ^ 1;
                }
                break;
            case 16:
                if (isClickable()) {
                    performClickInternal();
                    return true;
                }
                break;
            case 32:
                if (isLongClickable()) {
                    performLongClick();
                    return true;
                }
                break;
            case 64:
                if (!isAccessibilityFocused()) {
                    return requestAccessibilityFocus();
                }
                break;
            case 128:
                if (isAccessibilityFocused()) {
                    clearAccessibilityFocus();
                    return true;
                }
                break;
            case 256:
                if (arguments != null) {
                    return traverseAtGranularity(arguments.getInt(granularity), true, arguments.getBoolean(str));
                }
                break;
            case 512:
                if (arguments != null) {
                    return traverseAtGranularity(arguments.getInt(granularity), false, arguments.getBoolean(str));
                }
                break;
            case 131072:
                if (getIterableTextForAccessibility() == null) {
                    return false;
                }
                int start;
                granularity = -1;
                if (arguments != null) {
                    start = arguments.getInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_START_INT, -1);
                } else {
                    start = -1;
                }
                if (arguments != null) {
                    granularity = arguments.getInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_END_INT, -1);
                }
                if (!(getAccessibilitySelectionStart() == start && getAccessibilitySelectionEnd() == granularity) && start == granularity) {
                    setAccessibilitySelection(start, granularity);
                    notifyViewAccessibilityStateChangedIfNeeded(0);
                    return true;
                }
            case 16908342:
                Rect r = this.mAttachInfo;
                if (r != null) {
                    r = r.mTmpInvalRect;
                    getDrawingRect(r);
                    return requestRectangleOnScreen(r, true);
                }
                break;
            case 16908348:
                if (isContextClickable()) {
                    performContextClick();
                    return true;
                }
                break;
            case 16908356:
                tooltipInfo = this.mTooltipInfo;
                if (tooltipInfo == null || tooltipInfo.mTooltipPopup == null) {
                    return showLongClickTooltip(0, 0);
                }
                return false;
            case 16908357:
                tooltipInfo = this.mTooltipInfo;
                if (tooltipInfo == null || tooltipInfo.mTooltipPopup == null) {
                    return false;
                }
                hideTooltip();
                return true;
        }
        return false;
    }

    private boolean traverseAtGranularity(int granularity, boolean forward, boolean extendSelection) {
        CharSequence text = getIterableTextForAccessibility();
        if (text == null || text.length() == 0) {
            return false;
        }
        TextSegmentIterator iterator = getIteratorForGranularity(granularity);
        if (iterator == null) {
            return false;
        }
        int current = getAccessibilitySelectionEnd();
        if (current == -1) {
            current = forward ? 0 : text.length();
        }
        int[] range = forward ? iterator.following(current) : iterator.preceding(current);
        if (range == null) {
            return false;
        }
        int selectionStart;
        int selectionStart2;
        int action;
        int segmentStart = range[0];
        int segmentEnd = range[1];
        if (extendSelection && isAccessibilitySelectionExtendable()) {
            selectionStart = getAccessibilitySelectionStart();
            if (selectionStart == -1) {
                selectionStart = forward ? segmentStart : segmentEnd;
            }
            int i = selectionStart;
            selectionStart = forward ? segmentEnd : segmentStart;
            selectionStart2 = i;
        } else {
            selectionStart2 = forward ? segmentEnd : segmentStart;
            selectionStart = selectionStart2;
        }
        setAccessibilitySelection(selectionStart2, selectionStart);
        if (forward) {
            action = 256;
        } else {
            action = 512;
        }
        sendViewTextTraversedAtGranularityEvent(action, granularity, segmentStart, segmentEnd);
        return true;
    }

    @UnsupportedAppUsage
    public CharSequence getIterableTextForAccessibility() {
        return getContentDescription();
    }

    public boolean isAccessibilitySelectionExtendable() {
        return false;
    }

    public int getAccessibilitySelectionStart() {
        return this.mAccessibilityCursorPosition;
    }

    public int getAccessibilitySelectionEnd() {
        return getAccessibilitySelectionStart();
    }

    public void setAccessibilitySelection(int start, int end) {
        if (start != end || end != this.mAccessibilityCursorPosition) {
            if (start < 0 || start != end || end > getIterableTextForAccessibility().length()) {
                this.mAccessibilityCursorPosition = -1;
            } else {
                this.mAccessibilityCursorPosition = start;
            }
            sendAccessibilityEvent(8192);
        }
    }

    private void sendViewTextTraversedAtGranularityEvent(int action, int granularity, int fromIndex, int toIndex) {
        if (this.mParent != null) {
            AccessibilityEvent event = AccessibilityEvent.obtain(131072);
            onInitializeAccessibilityEvent(event);
            onPopulateAccessibilityEvent(event);
            event.setFromIndex(fromIndex);
            event.setToIndex(toIndex);
            event.setAction(action);
            event.setMovementGranularity(granularity);
            this.mParent.requestSendAccessibilityEvent(this, event);
        }
    }

    @UnsupportedAppUsage
    public TextSegmentIterator getIteratorForGranularity(int granularity) {
        CharSequence text;
        if (granularity == 1) {
            text = getIterableTextForAccessibility();
            if (text != null && text.length() > 0) {
                CharacterTextSegmentIterator iterator = CharacterTextSegmentIterator.getInstance(this.mContext.getResources().getConfiguration().locale);
                iterator.initialize(text.toString());
                return iterator;
            }
        } else if (granularity == 2) {
            text = getIterableTextForAccessibility();
            if (text != null && text.length() > 0) {
                WordTextSegmentIterator iterator2 = WordTextSegmentIterator.getInstance(this.mContext.getResources().getConfiguration().locale);
                iterator2.initialize(text.toString());
                return iterator2;
            }
        } else if (granularity == 8) {
            text = getIterableTextForAccessibility();
            if (text != null && text.length() > 0) {
                ParagraphTextSegmentIterator iterator3 = ParagraphTextSegmentIterator.getInstance();
                iterator3.initialize(text.toString());
                return iterator3;
            }
        }
        return null;
    }

    public final boolean isTemporarilyDetached() {
        return (this.mPrivateFlags3 & 33554432) != 0;
    }

    public void dispatchStartTemporaryDetach() {
        this.mPrivateFlags3 |= 33554432;
        notifyEnterOrExitForAutoFillIfNeeded(false);
        onStartTemporaryDetach();
    }

    public void onStartTemporaryDetach() {
        removeUnsetPressCallback();
        this.mPrivateFlags |= 67108864;
    }

    public void dispatchFinishTemporaryDetach() {
        this.mPrivateFlags3 &= -33554433;
        onFinishTemporaryDetach();
        if (hasWindowFocus() && hasFocus()) {
            notifyFocusChangeToInputMethodManager(true);
        }
        notifyEnterOrExitForAutoFillIfNeeded(true);
    }

    public void onFinishTemporaryDetach() {
    }

    public DispatcherState getKeyDispatcherState() {
        AttachInfo attachInfo = this.mAttachInfo;
        return attachInfo != null ? attachInfo.mKeyDispatchState : null;
    }

    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        return onKeyPreIme(event.getKeyCode(), event);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        InputEventConsistencyVerifier inputEventConsistencyVerifier = this.mInputEventConsistencyVerifier;
        if (inputEventConsistencyVerifier != null) {
            inputEventConsistencyVerifier.onKeyEvent(event, 0);
        }
        ListenerInfo li = this.mListenerInfo;
        if (li != null && li.mOnKeyListener != null && (this.mViewFlags & 32) == 0 && li.mOnKeyListener.onKey(this, event.getKeyCode(), event)) {
            return true;
        }
        AttachInfo attachInfo = this.mAttachInfo;
        if (event.dispatch(this, attachInfo != null ? attachInfo.mKeyDispatchState : null, this)) {
            return true;
        }
        InputEventConsistencyVerifier inputEventConsistencyVerifier2 = this.mInputEventConsistencyVerifier;
        if (inputEventConsistencyVerifier2 != null) {
            inputEventConsistencyVerifier2.onUnhandledEvent(event, 0);
        }
        return false;
    }

    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        return onKeyShortcut(event.getKeyCode(), event);
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.isTargetAccessibilityFocus()) {
            if (!isAccessibilityFocusedViewOrHost()) {
                return false;
            }
            event.setTargetAccessibilityFocus(false);
        }
        boolean result = false;
        InputEventConsistencyVerifier inputEventConsistencyVerifier = this.mInputEventConsistencyVerifier;
        if (inputEventConsistencyVerifier != null) {
            inputEventConsistencyVerifier.onTouchEvent(event, 0);
        }
        int actionMasked = event.getActionMasked();
        if (actionMasked == 0) {
            SeempLog.record(3);
            stopNestedScroll();
        }
        if (onFilterTouchEventForSecurity(event)) {
            if ((this.mViewFlags & 32) == 0 && handleScrollBarDragging(event)) {
                result = true;
            }
            ListenerInfo li = this.mListenerInfo;
            if (li != null && li.mOnTouchListener != null && (this.mViewFlags & 32) == 0 && li.mOnTouchListener.onTouch(this, event)) {
                result = true;
            }
            if (!result && onTouchEvent(event)) {
                result = true;
            }
        }
        if (!result) {
            InputEventConsistencyVerifier inputEventConsistencyVerifier2 = this.mInputEventConsistencyVerifier;
            if (inputEventConsistencyVerifier2 != null) {
                inputEventConsistencyVerifier2.onUnhandledEvent(event, 0);
            }
        }
        if (actionMasked == 1 || actionMasked == 3 || (actionMasked == 0 && !result)) {
            stopNestedScroll();
        }
        return result;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isAccessibilityFocusedViewOrHost() {
        return isAccessibilityFocused() || (getViewRootImpl() != null && getViewRootImpl().getAccessibilityFocusedHost() == this);
    }

    /* Access modifiers changed, original: protected */
    public boolean canReceivePointerEvents() {
        return (this.mViewFlags & 12) == 0 || getAnimation() != null;
    }

    public boolean onFilterTouchEventForSecurity(MotionEvent event) {
        if ((this.mViewFlags & 1024) == 0 || (event.getFlags() & 1) == 0) {
            return true;
        }
        return false;
    }

    public boolean dispatchTrackballEvent(MotionEvent event) {
        InputEventConsistencyVerifier inputEventConsistencyVerifier = this.mInputEventConsistencyVerifier;
        if (inputEventConsistencyVerifier != null) {
            inputEventConsistencyVerifier.onTrackballEvent(event, 0);
        }
        return onTrackballEvent(event);
    }

    public boolean dispatchCapturedPointerEvent(MotionEvent event) {
        if (!hasPointerCapture()) {
            return false;
        }
        ListenerInfo li = this.mListenerInfo;
        if (li == null || li.mOnCapturedPointerListener == null || !li.mOnCapturedPointerListener.onCapturedPointer(this, event)) {
            return onCapturedPointerEvent(event);
        }
        return true;
    }

    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        InputEventConsistencyVerifier inputEventConsistencyVerifier = this.mInputEventConsistencyVerifier;
        if (inputEventConsistencyVerifier != null) {
            inputEventConsistencyVerifier.onGenericMotionEvent(event, 0);
        }
        if ((event.getSource() & 2) != 0) {
            int action = event.getAction();
            if (action == 9 || action == 7 || action == 10) {
                if (dispatchHoverEvent(event)) {
                    return true;
                }
            } else if (dispatchGenericPointerEvent(event)) {
                return true;
            }
        } else if (dispatchGenericFocusedEvent(event)) {
            return true;
        }
        if (dispatchGenericMotionEventInternal(event)) {
            return true;
        }
        InputEventConsistencyVerifier inputEventConsistencyVerifier2 = this.mInputEventConsistencyVerifier;
        if (inputEventConsistencyVerifier2 != null) {
            inputEventConsistencyVerifier2.onUnhandledEvent(event, 0);
        }
        return false;
    }

    private boolean dispatchGenericMotionEventInternal(MotionEvent event) {
        ListenerInfo li = this.mListenerInfo;
        if ((li != null && li.mOnGenericMotionListener != null && (this.mViewFlags & 32) == 0 && li.mOnGenericMotionListener.onGenericMotion(this, event)) || onGenericMotionEvent(event)) {
            return true;
        }
        int actionButton = event.getActionButton();
        int actionMasked = event.getActionMasked();
        if (actionMasked != 11) {
            if (actionMasked == 12 && this.mInContextButtonPress && (actionButton == 32 || actionButton == 2)) {
                this.mInContextButtonPress = false;
                this.mIgnoreNextUpEvent = true;
            }
        } else if (isContextClickable() && !this.mInContextButtonPress && !this.mHasPerformedLongPress && ((actionButton == 32 || actionButton == 2) && performContextClick(event.getX(), event.getY()))) {
            this.mInContextButtonPress = true;
            setPressed(true, event.getX(), event.getY());
            removeTapCallback();
            removeLongPressCallback();
            return true;
        }
        InputEventConsistencyVerifier inputEventConsistencyVerifier = this.mInputEventConsistencyVerifier;
        if (inputEventConsistencyVerifier != null) {
            inputEventConsistencyVerifier.onUnhandledEvent(event, 0);
        }
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean dispatchHoverEvent(MotionEvent event) {
        ListenerInfo li = this.mListenerInfo;
        if (li == null || li.mOnHoverListener == null || (this.mViewFlags & 32) != 0 || !li.mOnHoverListener.onHover(this, event)) {
            return onHoverEvent(event);
        }
        return true;
    }

    /* Access modifiers changed, original: protected */
    public boolean hasHoveredChild() {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean pointInHoveredChild(MotionEvent event) {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean dispatchGenericPointerEvent(MotionEvent event) {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean dispatchGenericFocusedEvent(MotionEvent event) {
        return false;
    }

    @UnsupportedAppUsage
    public final boolean dispatchPointerEvent(MotionEvent event) {
        if (!event.isTouchEvent()) {
            return dispatchGenericMotionEvent(event);
        }
        dispatchTouchEventToContentCatcher(event);
        return dispatchTouchEvent(event);
    }

    public void dispatchWindowFocusChanged(boolean hasFocus) {
        onWindowFocusChanged(hasFocus);
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus) {
            if (isPressed()) {
                setPressed(false);
            }
            this.mPrivateFlags3 &= -131073;
            if ((this.mPrivateFlags & 2) != 0) {
                notifyFocusChangeToInputMethodManager(false);
            }
            removeLongPressCallback();
            removeTapCallback();
            onFocusLost();
        } else if ((this.mPrivateFlags & 2) != 0) {
            notifyFocusChangeToInputMethodManager(true);
        }
        refreshDrawableState();
    }

    public boolean hasWindowFocus() {
        AttachInfo attachInfo = this.mAttachInfo;
        return attachInfo != null && attachInfo.mHasWindowFocus;
    }

    /* Access modifiers changed, original: protected */
    public void dispatchVisibilityChanged(View changedView, int visibility) {
        onVisibilityChanged(changedView, visibility);
    }

    /* Access modifiers changed, original: protected */
    public void onVisibilityChanged(View changedView, int visibility) {
    }

    public void dispatchDisplayHint(int hint) {
        onDisplayHint(hint);
    }

    /* Access modifiers changed, original: protected */
    public void onDisplayHint(int hint) {
    }

    public void dispatchWindowVisibilityChanged(int visibility) {
        onWindowVisibilityChanged(visibility);
    }

    /* Access modifiers changed, original: protected */
    public void onWindowVisibilityChanged(int visibility) {
        if (visibility == 0) {
            initialAwakenScrollBars();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean dispatchVisibilityAggregated(boolean isVisible) {
        boolean thisVisible = getVisibility() == 0;
        if (thisVisible || !isVisible) {
            onVisibilityAggregated(isVisible);
        }
        if (thisVisible && isVisible) {
            return true;
        }
        return false;
    }

    public void onVisibilityAggregated(boolean isVisible) {
        int i;
        boolean oldVisible = (this.mPrivateFlags3 & 536870912) != 0;
        if (isVisible) {
            i = 536870912 | this.mPrivateFlags3;
        } else {
            i = this.mPrivateFlags3 & -536870913;
        }
        this.mPrivateFlags3 = i;
        if (isVisible && this.mAttachInfo != null) {
            initialAwakenScrollBars();
        }
        Drawable dr = this.mBackground;
        if (!(dr == null || isVisible == dr.isVisible())) {
            dr.setVisible(isVisible, false);
        }
        Drawable hl = this.mDefaultFocusHighlight;
        if (!(hl == null || isVisible == hl.isVisible())) {
            hl.setVisible(isVisible, false);
        }
        ForegroundInfo foregroundInfo = this.mForegroundInfo;
        Drawable fg = foregroundInfo != null ? foregroundInfo.mDrawable : null;
        if (!(fg == null || isVisible == fg.isVisible())) {
            fg.setVisible(isVisible, false);
        }
        if (isAutofillable()) {
            AutofillManager afm = getAutofillManager();
            if (afm != null && getAutofillViewId() > LAST_APP_AUTOFILL_ID) {
                Handler handler = this.mVisibilityChangeForAutofillHandler;
                if (handler != null) {
                    handler.removeMessages(0);
                }
                if (isVisible) {
                    afm.notifyViewVisibilityChanged(this, true);
                } else {
                    if (this.mVisibilityChangeForAutofillHandler == null) {
                        this.mVisibilityChangeForAutofillHandler = new VisibilityChangeForAutofillHandler(afm, this, null);
                    }
                    this.mVisibilityChangeForAutofillHandler.obtainMessage(0, this).sendToTarget();
                }
            }
        }
        if (isAccessibilityPane() && isVisible != oldVisible) {
            int i2;
            if (isVisible) {
                i2 = 16;
            } else {
                i2 = 32;
            }
            notifyViewAccessibilityStateChangedIfNeeded(i2);
        }
    }

    public int getWindowVisibility() {
        AttachInfo attachInfo = this.mAttachInfo;
        return attachInfo != null ? attachInfo.mWindowVisibility : 8;
    }

    public void getWindowVisibleDisplayFrame(Rect outRect) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            try {
                attachInfo.mSession.getDisplayFrame(this.mAttachInfo.mWindow, outRect);
                Rect insets = this.mAttachInfo.mVisibleInsets;
                outRect.left += insets.left;
                outRect.top += insets.top;
                outRect.right -= insets.right;
                outRect.bottom -= insets.bottom;
                return;
            } catch (RemoteException e) {
                return;
            }
        }
        DisplayManagerGlobal.getInstance().getRealDisplay(0).getRectSize(outRect);
    }

    @UnsupportedAppUsage
    public void getWindowDisplayFrame(Rect outRect) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            try {
                attachInfo.mSession.getDisplayFrame(this.mAttachInfo.mWindow, outRect);
                return;
            } catch (RemoteException e) {
                return;
            }
        }
        DisplayManagerGlobal.getInstance().getRealDisplay(0).getRectSize(outRect);
    }

    public void dispatchConfigurationChanged(Configuration newConfig) {
        onConfigurationChanged(newConfig);
    }

    /* Access modifiers changed, original: protected */
    public void onConfigurationChanged(Configuration newConfig) {
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchCollectViewAttributes(AttachInfo attachInfo, int visibility) {
        performCollectViewAttributes(attachInfo, visibility);
    }

    /* Access modifiers changed, original: 0000 */
    public void performCollectViewAttributes(AttachInfo attachInfo, int visibility) {
        if ((visibility & 12) == 0) {
            if ((this.mViewFlags & 67108864) == 67108864) {
                attachInfo.mKeepScreenOn = true;
            }
            attachInfo.mSystemUiVisibility |= this.mSystemUiVisibility;
            ListenerInfo li = this.mListenerInfo;
            if (li != null && li.mOnSystemUiVisibilityChangeListener != null) {
                attachInfo.mHasSystemUiListeners = true;
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void needGlobalAttributesUpdate(boolean force) {
        AttachInfo ai = this.mAttachInfo;
        if (ai != null && !ai.mRecomputeGlobalAttributes) {
            if (force || ai.mKeepScreenOn || ai.mSystemUiVisibility != 0 || ai.mHasSystemUiListeners) {
                ai.mRecomputeGlobalAttributes = true;
            }
        }
    }

    @ExportedProperty
    public boolean isInTouchMode() {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            return attachInfo.mInTouchMode;
        }
        return ViewRootImpl.isInTouchMode();
    }

    @CapturedViewProperty
    public final Context getContext() {
        return this.mContext;
    }

    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        SeempLog.record(4);
        if (KeyEvent.isConfirmKey(keyCode)) {
            if ((this.mViewFlags & 32) == 32) {
                return true;
            }
            if (event.getRepeatCount() == 0) {
                int i = this.mViewFlags;
                boolean clickable = (i & 16384) == 16384 || (i & 2097152) == 2097152;
                if (clickable || (this.mViewFlags & 1073741824) == 1073741824) {
                    float x = ((float) getWidth()) / 2.0f;
                    float y = ((float) getHeight()) / 2.0f;
                    if (clickable) {
                        setPressed(true, x, y);
                    }
                    checkForLongClick((long) ViewConfiguration.getLongPressTimeout(), x, y, 0);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        SeempLog.record(5);
        if (KeyEvent.isConfirmKey(keyCode)) {
            int i = this.mViewFlags;
            if ((i & 32) == 32) {
                return true;
            }
            if ((i & 16384) == 16384 && isPressed()) {
                setPressed(false);
                if (!this.mHasPerformedLongPress) {
                    removeLongPressCallback();
                    if (!event.isCanceled()) {
                        return performClickInternal();
                    }
                }
            }
        }
        return false;
    }

    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return false;
    }

    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onCheckIsTextEditor() {
        return false;
    }

    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return null;
    }

    public boolean checkInputConnectionProxy(View view) {
        return false;
    }

    public void createContextMenu(ContextMenu menu) {
        ContextMenuInfo menuInfo = getContextMenuInfo();
        ((MenuBuilder) menu).setCurrentMenuInfo(menuInfo);
        onCreateContextMenu(menu);
        ListenerInfo li = this.mListenerInfo;
        if (!(li == null || li.mOnCreateContextMenuListener == null)) {
            li.mOnCreateContextMenuListener.onCreateContextMenu(menu, this, menuInfo);
        }
        ((MenuBuilder) menu).setCurrentMenuInfo(null);
        ViewParent viewParent = this.mParent;
        if (viewParent != null) {
            viewParent.createContextMenu(menu);
        }
    }

    /* Access modifiers changed, original: protected */
    public ContextMenuInfo getContextMenuInfo() {
        return null;
    }

    /* Access modifiers changed, original: protected */
    public void onCreateContextMenu(ContextMenu menu) {
    }

    public boolean onTrackballEvent(MotionEvent event) {
        return false;
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        return false;
    }

    private boolean dispatchTouchExplorationHoverEvent(MotionEvent event) {
        AccessibilityManager manager = AccessibilityManager.getInstance(this.mContext);
        if (!manager.isEnabled() || !manager.isTouchExplorationEnabled()) {
            return false;
        }
        boolean oldHoveringTouchDelegate = this.mHoveringTouchDelegate;
        int action = event.getActionMasked();
        boolean pointInDelegateRegion = false;
        boolean handled = false;
        TouchDelegateInfo info = this.mTouchDelegate.getTouchDelegateInfo();
        for (int i = 0; i < info.getRegionCount(); i++) {
            if (info.getRegionAt(i).contains((int) event.getX(), (int) event.getY())) {
                pointInDelegateRegion = true;
            }
        }
        if (oldHoveringTouchDelegate) {
            if (action == 10 || (action == 7 && (pointInHoveredChild(event) || !pointInDelegateRegion))) {
                this.mHoveringTouchDelegate = false;
            }
        } else if ((action == 9 || action == 7) && !pointInHoveredChild(event) && pointInDelegateRegion) {
            this.mHoveringTouchDelegate = true;
        }
        if (action != 7) {
            if (action != 9) {
                if (action == 10 && oldHoveringTouchDelegate) {
                    this.mTouchDelegate.onTouchExplorationHoverEvent(event);
                }
            } else if (!oldHoveringTouchDelegate && this.mHoveringTouchDelegate) {
                handled = this.mTouchDelegate.onTouchExplorationHoverEvent(event);
            }
        } else if (oldHoveringTouchDelegate && this.mHoveringTouchDelegate) {
            handled = this.mTouchDelegate.onTouchExplorationHoverEvent(event);
        } else if (!oldHoveringTouchDelegate && this.mHoveringTouchDelegate) {
            MotionEvent eventNoHistory = event.getHistorySize() == 0 ? event : MotionEvent.obtainNoHistory(event);
            eventNoHistory.setAction(9);
            handled = this.mTouchDelegate.onTouchExplorationHoverEvent(eventNoHistory);
            eventNoHistory.setAction(action);
            handled |= this.mTouchDelegate.onTouchExplorationHoverEvent(eventNoHistory);
        } else if (oldHoveringTouchDelegate && !this.mHoveringTouchDelegate) {
            boolean hoverExitPending = event.isHoverExitPending();
            event.setHoverExitPending(true);
            this.mTouchDelegate.onTouchExplorationHoverEvent(event);
            MotionEvent eventNoHistory2 = event.getHistorySize() == 0 ? event : MotionEvent.obtainNoHistory(event);
            eventNoHistory2.setHoverExitPending(hoverExitPending);
            eventNoHistory2.setAction(10);
            this.mTouchDelegate.onTouchExplorationHoverEvent(eventNoHistory2);
        }
        return handled;
    }

    public boolean onHoverEvent(MotionEvent event) {
        if (this.mTouchDelegate != null && dispatchTouchExplorationHoverEvent(event)) {
            return true;
        }
        int action = event.getActionMasked();
        if (this.mSendingHoverAccessibilityEvents) {
            if (action == 10 || (action == 7 && !pointInView(event.getX(), event.getY()))) {
                this.mSendingHoverAccessibilityEvents = false;
                sendAccessibilityHoverEvent(256);
            }
        } else if ((action == 9 || action == 7) && !hasHoveredChild() && pointInView(event.getX(), event.getY())) {
            sendAccessibilityHoverEvent(128);
            this.mSendingHoverAccessibilityEvents = true;
        }
        if ((action == 9 || action == 7) && event.isFromSource(8194) && isOnScrollbar(event.getX(), event.getY())) {
            awakenScrollBars();
        }
        if (!isHoverable() && !isHovered()) {
            return false;
        }
        if (action == 9) {
            setHovered(true);
        } else if (action == 10) {
            setHovered(false);
        }
        dispatchGenericMotionEventInternal(event);
        return true;
    }

    private boolean isHoverable() {
        int viewFlags = this.mViewFlags;
        boolean z = false;
        if ((viewFlags & 32) == 32) {
            return false;
        }
        if ((viewFlags & 16384) == 16384 || (viewFlags & 2097152) == 2097152 || (viewFlags & 8388608) == 8388608) {
            z = true;
        }
        return z;
    }

    @ExportedProperty
    public boolean isHovered() {
        return (this.mPrivateFlags & 268435456) != 0;
    }

    public void setHovered(boolean hovered) {
        int i;
        if (hovered) {
            i = this.mPrivateFlags;
            if ((i & 268435456) == 0) {
                this.mPrivateFlags = 268435456 | i;
                refreshDrawableState();
                onHoverChanged(true);
                return;
            }
            return;
        }
        i = this.mPrivateFlags;
        if ((268435456 & i) != 0) {
            this.mPrivateFlags = -268435457 & i;
            refreshDrawableState();
            onHoverChanged(false);
        }
    }

    public void onHoverChanged(boolean hovered) {
    }

    /* Access modifiers changed, original: protected */
    public boolean handleScrollBarDragging(MotionEvent event) {
        MotionEvent motionEvent = event;
        if (this.mScrollCache == null) {
            return false;
        }
        float x = event.getX();
        float y = event.getY();
        int action = event.getAction();
        if ((this.mScrollCache.mScrollBarDraggingState != 0 || action == 0) && motionEvent.isFromSource(8194) && motionEvent.isButtonPressed(1)) {
            if (action != 0) {
                if (action == 2) {
                    if (this.mScrollCache.mScrollBarDraggingState == 0) {
                        return false;
                    }
                    int range;
                    int offset;
                    int extent;
                    int height;
                    if (this.mScrollCache.mScrollBarDraggingState == 1) {
                        Rect bounds = this.mScrollCache.mScrollBarBounds;
                        getVerticalScrollBarBounds(bounds, null);
                        range = computeVerticalScrollRange();
                        offset = computeVerticalScrollOffset();
                        extent = computeVerticalScrollExtent();
                        int thumbLength = ScrollBarUtils.getThumbLength(bounds.height(), bounds.width(), extent, range);
                        int thumbOffset = ScrollBarUtils.getThumbOffset(bounds.height(), thumbLength, extent, range, offset);
                        float maxThumbOffset = (float) (bounds.height() - thumbLength);
                        float newThumbOffset = Math.min(Math.max(((float) thumbOffset) + (y - this.mScrollCache.mScrollBarDraggingPos), 0.0f), maxThumbOffset);
                        height = getHeight();
                        if (Math.round(newThumbOffset) == thumbOffset || maxThumbOffset <= 0.0f || height <= 0 || extent <= 0) {
                        } else {
                            int newY = Math.round((((float) (range - extent)) / (((float) extent) / ((float) height))) * (newThumbOffset / maxThumbOffset));
                            if (newY != getScrollY()) {
                                this.mScrollCache.mScrollBarDraggingPos = y;
                                setScrollY(newY);
                            }
                        }
                        return true;
                    } else if (this.mScrollCache.mScrollBarDraggingState == 2) {
                        Rect bounds2 = this.mScrollCache.mScrollBarBounds;
                        getHorizontalScrollBarBounds(bounds2, null);
                        int range2 = computeHorizontalScrollRange();
                        height = computeHorizontalScrollOffset();
                        range = computeHorizontalScrollExtent();
                        offset = ScrollBarUtils.getThumbLength(bounds2.width(), bounds2.height(), range, range2);
                        extent = ScrollBarUtils.getThumbOffset(bounds2.width(), offset, range, range2, height);
                        float maxThumbOffset2 = (float) (bounds2.width() - offset);
                        float newThumbOffset2 = Math.min(Math.max(((float) extent) + (x - this.mScrollCache.mScrollBarDraggingPos), 0.0f), maxThumbOffset2);
                        int width = getWidth();
                        if (Math.round(newThumbOffset2) == extent || maxThumbOffset2 <= 0.0f || width <= 0 || range <= 0) {
                        } else {
                            Rect rect = bounds2;
                            bounds2 = Math.round((((float) (range2 - range)) / (((float) range) / ((float) width))) * (newThumbOffset2 / maxThumbOffset2));
                            if (bounds2 != getScrollX()) {
                                this.mScrollCache.mScrollBarDraggingPos = x;
                                setScrollX(bounds2);
                            }
                        }
                        return true;
                    }
                }
                this.mScrollCache.mScrollBarDraggingState = 0;
                return false;
            }
            if (this.mScrollCache.state == 0) {
                return false;
            }
            ScrollabilityCache scrollabilityCache;
            if (isOnVerticalScrollbarThumb(x, y)) {
                scrollabilityCache = this.mScrollCache;
                scrollabilityCache.mScrollBarDraggingState = 1;
                scrollabilityCache.mScrollBarDraggingPos = y;
                return true;
            }
            if (isOnHorizontalScrollbarThumb(x, y)) {
                scrollabilityCache = this.mScrollCache;
                scrollabilityCache.mScrollBarDraggingState = 2;
                scrollabilityCache.mScrollBarDraggingPos = x;
                return true;
            }
            this.mScrollCache.mScrollBarDraggingState = 0;
            return false;
        }
        this.mScrollCache.mScrollBarDraggingState = 0;
        return false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        SeempLog.record(3);
        float x = event.getX();
        float y = event.getY();
        int viewFlags = this.mViewFlags;
        int action = event.getAction();
        boolean z = false;
        boolean z2 = (viewFlags & 16384) == 16384 || (viewFlags & 2097152) == 2097152 || (viewFlags & 8388608) == 8388608;
        boolean clickable = z2;
        if ((viewFlags & 32) == 32) {
            if (action == 1 && (this.mPrivateFlags & 16384) != 0) {
                setPressed(false);
            }
            this.mPrivateFlags3 &= -131073;
            return clickable;
        }
        TouchDelegate touchDelegate = this.mTouchDelegate;
        if (touchDelegate == null) {
            MotionEvent motionEvent = event;
        } else if (touchDelegate.onTouchEvent(event)) {
            return true;
        }
        if (!clickable && (viewFlags & 1073741824) != 1073741824) {
            return false;
        }
        if (action == 0) {
            if (event.getSource() == 4098) {
                this.mPrivateFlags3 |= 131072;
            }
            this.mHasPerformedLongPress = false;
            if (!clickable) {
                checkForLongClick((long) ViewConfiguration.getLongPressTimeout(), x, y, 3);
                z2 = true;
            } else if (performButtonActionOnTouchDown(event)) {
                z2 = true;
            } else if (isInScrollingContainer()) {
                this.mPrivateFlags |= 33554432;
                if (this.mPendingCheckForTap == null) {
                    this.mPendingCheckForTap = new CheckForTap(this, null);
                }
                this.mPendingCheckForTap.x = event.getX();
                this.mPendingCheckForTap.y = event.getY();
                postDelayed(this.mPendingCheckForTap, (long) ViewConfiguration.getTapTimeout());
                z2 = true;
            } else {
                setPressed(true, x, y);
                checkForLongClick((long) ViewConfiguration.getLongPressTimeout(), x, y, 3);
                z2 = true;
                performHapticFeedback(1, 4);
            }
        } else if (action == 1) {
            this.mPrivateFlags3 &= -131073;
            if ((viewFlags & 1073741824) == 1073741824) {
                handleTooltipUp();
            }
            if (clickable) {
                boolean prepressed = (this.mPrivateFlags & 33554432) != 0;
                if ((this.mPrivateFlags & 16384) != 0 || prepressed) {
                    z2 = false;
                    if (isFocusable() && isFocusableInTouchMode() && !isFocused()) {
                        z2 = requestFocus();
                    }
                    if (prepressed) {
                        setPressed(true, x, y);
                    }
                    if (!(this.mHasPerformedLongPress || this.mIgnoreNextUpEvent)) {
                        removeLongPressCallback();
                        if (!z2) {
                            if (this.mPerformClick == null) {
                                this.mPerformClick = new PerformClick(this, null);
                            }
                            if (!post(this.mPerformClick)) {
                                performClickInternal();
                            }
                        }
                    }
                    if (this.mUnsetPressedState == null) {
                        this.mUnsetPressedState = new UnsetPressedState(this, null);
                    }
                    if (prepressed) {
                        postDelayed(this.mUnsetPressedState, (long) ViewConfiguration.getPressedStateDuration());
                    } else if (!post(this.mUnsetPressedState)) {
                        this.mUnsetPressedState.run();
                    }
                    removeTapCallback();
                }
                this.mIgnoreNextUpEvent = false;
                z2 = true;
            } else {
                removeTapCallback();
                removeLongPressCallback();
                this.mInContextButtonPress = false;
                this.mHasPerformedLongPress = false;
                this.mIgnoreNextUpEvent = false;
                z2 = true;
            }
        } else if (action == 2) {
            int touchSlop;
            int motionClassification;
            if (clickable) {
                drawableHotspotChanged(x, y);
            }
            int motionClassification2 = event.getClassification();
            boolean ambiguousGesture = motionClassification2 == 1;
            int touchSlop2 = this.mTouchSlop;
            if (ambiguousGesture && hasPendingLongPressCallback()) {
                float ambiguousMultiplier = ViewConfiguration.getAmbiguousGestureMultiplier();
                if (pointInView(x, y, (float) touchSlop2)) {
                    touchSlop = touchSlop2;
                    motionClassification = motionClassification2;
                } else {
                    removeLongPressCallback();
                    motionClassification = motionClassification2;
                    touchSlop = touchSlop2;
                    checkForLongClick(((long) (((float) ViewConfiguration.getLongPressTimeout()) * ambiguousMultiplier)) - (event.getEventTime() - event.getDownTime()), x, y, 3);
                }
                touchSlop = (int) (((float) touchSlop) * ambiguousMultiplier);
            } else {
                touchSlop = touchSlop2;
                motionClassification = motionClassification2;
            }
            if (!pointInView(x, y, (float) touchSlop)) {
                removeTapCallback();
                removeLongPressCallback();
                if ((this.mPrivateFlags & 16384) != 0) {
                    setPressed(false);
                }
                this.mPrivateFlags3 &= -131073;
            }
            if (motionClassification == 2) {
                z = true;
            }
            if (z && hasPendingLongPressCallback()) {
                removeLongPressCallback();
                checkForLongClick(0, x, y, 4);
                z2 = true;
            } else {
                z2 = true;
            }
        } else if (action != 3) {
            z2 = true;
        } else {
            if (clickable) {
                setPressed(false);
            }
            removeTapCallback();
            removeLongPressCallback();
            this.mInContextButtonPress = false;
            this.mHasPerformedLongPress = false;
            this.mIgnoreNextUpEvent = false;
            this.mPrivateFlags3 &= -131073;
            z2 = true;
        }
        return z2;
    }

    @UnsupportedAppUsage
    public boolean isInScrollingContainer() {
        ViewParent p = getParent();
        while (p != null && (p instanceof ViewGroup)) {
            if (((ViewGroup) p).shouldDelayChildPressedState()) {
                return true;
            }
            p = p.getParent();
        }
        return false;
    }

    private void removeLongPressCallback() {
        CheckForLongPress checkForLongPress = this.mPendingCheckForLongPress;
        if (checkForLongPress != null) {
            removeCallbacks(checkForLongPress);
        }
    }

    private boolean hasPendingLongPressCallback() {
        if (this.mPendingCheckForLongPress == null) {
            return false;
        }
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo == null) {
            return false;
        }
        return attachInfo.mHandler.hasCallbacks(this.mPendingCheckForLongPress);
    }

    @UnsupportedAppUsage
    private void removePerformClickCallback() {
        PerformClick performClick = this.mPerformClick;
        if (performClick != null) {
            removeCallbacks(performClick);
        }
    }

    private void removeUnsetPressCallback() {
        if ((this.mPrivateFlags & 16384) != 0 && this.mUnsetPressedState != null) {
            setPressed(false);
            removeCallbacks(this.mUnsetPressedState);
        }
    }

    private void removeTapCallback() {
        CheckForTap checkForTap = this.mPendingCheckForTap;
        if (checkForTap != null) {
            this.mPrivateFlags &= -33554433;
            removeCallbacks(checkForTap);
        }
    }

    public void cancelLongPress() {
        removeLongPressCallback();
        removeTapCallback();
    }

    public void setTouchDelegate(TouchDelegate delegate) {
        this.mTouchDelegate = delegate;
    }

    public TouchDelegate getTouchDelegate() {
        return this.mTouchDelegate;
    }

    public final void requestUnbufferedDispatch(MotionEvent event) {
        int action = event.getAction();
        if (this.mAttachInfo != null && ((action == 0 || action == 2) && event.isTouchEvent())) {
            this.mAttachInfo.mUnbufferedDispatchRequested = true;
        }
    }

    private boolean hasSize() {
        return this.mBottom > this.mTop && this.mRight > this.mLeft;
    }

    private boolean canTakeFocus() {
        int i = this.mViewFlags;
        if ((i & 12) == 0 && (i & 1) == 1 && (i & 32) == 0 && (sCanFocusZeroSized || !isLayoutValid() || hasSize())) {
            return true;
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public void setFlags(int flags, int mask) {
        boolean accessibilityEnabled = AccessibilityManager.getInstance(this.mContext).isEnabled();
        boolean oldIncludeForAccessibility = accessibilityEnabled && includeForAccessibility();
        int old = this.mViewFlags;
        this.mViewFlags = (this.mViewFlags & (~mask)) | (flags & mask);
        int i = this.mViewFlags;
        int changed = i ^ old;
        if (changed != 0) {
            ViewParent viewParent;
            AttachInfo attachInfo;
            int privateFlags = this.mPrivateFlags;
            boolean shouldNotifyFocusableAvailable = false;
            int focusableChangedByAuto = 0;
            if (!((i & 16) == 0 || (changed & HistoryItem.EVENT_TEMP_WHITELIST_FINISH) == 0)) {
                if ((i & 16384) != 0) {
                    i = 1;
                } else {
                    i = 0;
                }
                this.mViewFlags = (this.mViewFlags & -2) | i;
                focusableChangedByAuto = (old & 1) ^ (i & 1);
                changed = (changed & -2) | focusableChangedByAuto;
            }
            if (!((changed & 1) == 0 || (privateFlags & 16) == 0)) {
                if ((old & 1) == 1 && (privateFlags & 2) != 0) {
                    clearFocus();
                    ViewParent viewParent2 = this.mParent;
                    if (viewParent2 instanceof ViewGroup) {
                        ((ViewGroup) viewParent2).clearFocusedInCluster();
                    }
                } else if ((old & 1) == 0 && (privateFlags & 2) == 0 && this.mParent != null) {
                    ViewRootImpl viewRootImpl = getViewRootImpl();
                    if (!sAutoFocusableOffUIThreadWontNotifyParents || focusableChangedByAuto == 0 || viewRootImpl == null || viewRootImpl.mThread == Thread.currentThread()) {
                        shouldNotifyFocusableAvailable = canTakeFocus();
                    }
                }
            }
            i = flags & 12;
            if (i == 0 && (changed & 12) != 0) {
                this.mPrivateFlags |= 32;
                invalidate(true);
                needGlobalAttributesUpdate(true);
                shouldNotifyFocusableAvailable = hasSize();
            }
            if ((changed & 32) != 0) {
                if ((this.mViewFlags & 32) == 0) {
                    shouldNotifyFocusableAvailable = canTakeFocus();
                } else if (isFocused()) {
                    clearFocus();
                }
            }
            if (shouldNotifyFocusableAvailable) {
                viewParent = this.mParent;
                if (viewParent != null) {
                    viewParent.focusableViewAvailable(this);
                }
            }
            if ((changed & 8) != 0) {
                needGlobalAttributesUpdate(false);
                requestLayout();
                if ((this.mViewFlags & 12) == 8) {
                    if (hasFocus()) {
                        clearFocus();
                        viewParent = this.mParent;
                        if (viewParent instanceof ViewGroup) {
                            ((ViewGroup) viewParent).clearFocusedInCluster();
                        }
                    }
                    clearAccessibilityFocus();
                    destroyDrawingCache();
                    viewParent = this.mParent;
                    if (viewParent instanceof View) {
                        ((View) viewParent).invalidate(true);
                    }
                    this.mPrivateFlags |= 32;
                }
                attachInfo = this.mAttachInfo;
                if (attachInfo != null) {
                    attachInfo.mViewVisibilityChanged = true;
                }
            }
            if ((changed & 4) != 0) {
                needGlobalAttributesUpdate(false);
                this.mPrivateFlags |= 32;
                if ((this.mViewFlags & 12) == 4 && getRootView() != this) {
                    if (hasFocus()) {
                        clearFocus();
                        viewParent = this.mParent;
                        if (viewParent instanceof ViewGroup) {
                            ((ViewGroup) viewParent).clearFocusedInCluster();
                        }
                    }
                    clearAccessibilityFocus();
                }
                attachInfo = this.mAttachInfo;
                if (attachInfo != null) {
                    attachInfo.mViewVisibilityChanged = true;
                }
            }
            if ((changed & 12) != 0) {
                if (!(i == 0 || this.mAttachInfo == null)) {
                    cleanupDraw();
                }
                viewParent = this.mParent;
                if (viewParent instanceof ViewGroup) {
                    ViewGroup parent = (ViewGroup) viewParent;
                    parent.onChildVisibilityChanged(this, changed & 12, i);
                    parent.invalidate(true);
                } else if (viewParent != null) {
                    viewParent.invalidateChild(this, null);
                }
                if (this.mAttachInfo != null) {
                    dispatchVisibilityChanged(this, i);
                    if (this.mParent != null && getWindowVisibility() == 0) {
                        viewParent = this.mParent;
                        if (!(viewParent instanceof ViewGroup) || ((ViewGroup) viewParent).isShown()) {
                            dispatchVisibilityAggregated(i == 0);
                        }
                    }
                    notifySubtreeAccessibilityStateChangedIfNeeded();
                }
            }
            if ((131072 & changed) != 0) {
                destroyDrawingCache();
            }
            if ((32768 & changed) != 0) {
                destroyDrawingCache();
                this.mPrivateFlags &= -32769;
                invalidateParentCaches();
            }
            if ((DRAWING_CACHE_QUALITY_MASK & changed) != 0) {
                destroyDrawingCache();
                this.mPrivateFlags &= -32769;
            }
            if ((changed & 128) != 0) {
                if ((this.mViewFlags & 128) != 0) {
                    if (this.mBackground == null && this.mDefaultFocusHighlight == null) {
                        ForegroundInfo foregroundInfo = this.mForegroundInfo;
                        if (foregroundInfo == null || foregroundInfo.mDrawable == null) {
                            this.mPrivateFlags |= 128;
                        }
                    }
                    this.mPrivateFlags &= -129;
                } else {
                    this.mPrivateFlags &= -129;
                }
                requestLayout();
                invalidate(true);
            }
            if (!((67108864 & changed) == 0 || this.mParent == null)) {
                AttachInfo attachInfo2 = this.mAttachInfo;
                if (!(attachInfo2 == null || attachInfo2.mRecomputeGlobalAttributes)) {
                    this.mParent.recomputeViewAttributes(this);
                }
            }
            if (accessibilityEnabled) {
                if (isAccessibilityPane()) {
                    changed &= -13;
                }
                if ((changed & 1) == 0 && (changed & 12) == 0 && (changed & 16384) == 0 && (2097152 & changed) == 0 && (8388608 & changed) == 0) {
                    if ((changed & 32) != 0) {
                        notifyViewAccessibilityStateChangedIfNeeded(0);
                    }
                } else if (oldIncludeForAccessibility != includeForAccessibility()) {
                    notifySubtreeAccessibilityStateChangedIfNeeded();
                } else {
                    notifyViewAccessibilityStateChangedIfNeeded(0);
                }
            }
        }
    }

    public void bringToFront() {
        ViewParent viewParent = this.mParent;
        if (viewParent != null) {
            viewParent.bringChildToFront(this);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        notifySubtreeAccessibilityStateChangedIfNeeded();
        if (AccessibilityManager.getInstance(this.mContext).isEnabled()) {
            postSendViewScrolledAccessibilityEventCallback(l - oldl, t - oldt);
        }
        this.mBackgroundSizeChanged = true;
        this.mDefaultFocusHighlightSizeChanged = true;
        ForegroundInfo foregroundInfo = this.mForegroundInfo;
        if (foregroundInfo != null) {
            foregroundInfo.mBoundsChanged = true;
        }
        AttachInfo ai = this.mAttachInfo;
        if (ai != null) {
            ai.mViewScrollChanged = true;
        }
        ListenerInfo listenerInfo = this.mListenerInfo;
        if (listenerInfo != null && listenerInfo.mOnScrollChangeListener != null) {
            this.mListenerInfo.mOnScrollChangeListener.onScrollChange(this, l, t, oldl, oldt);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
    }

    /* Access modifiers changed, original: protected */
    public void dispatchDraw(Canvas canvas) {
    }

    public final ViewParent getParent() {
        return this.mParent;
    }

    public void setScrollX(int value) {
        scrollTo(value, this.mScrollY);
    }

    public void setScrollY(int value) {
        scrollTo(this.mScrollX, value);
    }

    public final int getScrollX() {
        return this.mScrollX;
    }

    public final int getScrollY() {
        return this.mScrollY;
    }

    @ExportedProperty(category = "layout")
    public final int getWidth() {
        return this.mRight - this.mLeft;
    }

    @ExportedProperty(category = "layout")
    public final int getHeight() {
        return this.mBottom - this.mTop;
    }

    public void getDrawingRect(Rect outRect) {
        int i = this.mScrollX;
        outRect.left = i;
        int i2 = this.mScrollY;
        outRect.top = i2;
        outRect.right = i + (this.mRight - this.mLeft);
        outRect.bottom = i2 + (this.mBottom - this.mTop);
    }

    public final int getMeasuredWidth() {
        return this.mMeasuredWidth & 16777215;
    }

    @ExportedProperty(category = "measurement", flagMapping = {@FlagToString(equals = 16777216, mask = -16777216, name = "MEASURED_STATE_TOO_SMALL")})
    public final int getMeasuredWidthAndState() {
        return this.mMeasuredWidth;
    }

    public final int getMeasuredHeight() {
        return this.mMeasuredHeight & 16777215;
    }

    @ExportedProperty(category = "measurement", flagMapping = {@FlagToString(equals = 16777216, mask = -16777216, name = "MEASURED_STATE_TOO_SMALL")})
    public final int getMeasuredHeightAndState() {
        return this.mMeasuredHeight;
    }

    public final int getMeasuredState() {
        return (this.mMeasuredWidth & -16777216) | ((this.mMeasuredHeight >> 16) & -256);
    }

    public Matrix getMatrix() {
        ensureTransformationInfo();
        Matrix matrix = this.mTransformationInfo.mMatrix;
        this.mRenderNode.getMatrix(matrix);
        return matrix;
    }

    @UnsupportedAppUsage
    public final boolean hasIdentityMatrix() {
        return this.mRenderNode.hasIdentityMatrix();
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void ensureTransformationInfo() {
        if (this.mTransformationInfo == null) {
            this.mTransformationInfo = new TransformationInfo();
        }
    }

    @UnsupportedAppUsage
    public final Matrix getInverseMatrix() {
        ensureTransformationInfo();
        if (this.mTransformationInfo.mInverseMatrix == null) {
            this.mTransformationInfo.mInverseMatrix = new Matrix();
        }
        Matrix matrix = this.mTransformationInfo.mInverseMatrix;
        this.mRenderNode.getInverseMatrix(matrix);
        return matrix;
    }

    public float getCameraDistance() {
        return this.mRenderNode.getCameraDistance() * ((float) this.mResources.getDisplayMetrics().densityDpi);
    }

    public void setCameraDistance(float distance) {
        float dpi = (float) this.mResources.getDisplayMetrics().densityDpi;
        invalidateViewProperty(true, false);
        this.mRenderNode.setCameraDistance(Math.abs(distance) / dpi);
        invalidateViewProperty(false, false);
        invalidateParentIfNeededAndWasQuickRejected();
    }

    @ExportedProperty(category = "drawing")
    public float getRotation() {
        return this.mRenderNode.getRotationZ();
    }

    public void setRotation(float rotation) {
        if (rotation != getRotation()) {
            invalidateViewProperty(true, false);
            this.mRenderNode.setRotationZ(rotation);
            invalidateViewProperty(false, true);
            invalidateParentIfNeededAndWasQuickRejected();
            notifySubtreeAccessibilityStateChangedIfNeeded();
        }
    }

    @ExportedProperty(category = "drawing")
    public float getRotationY() {
        return this.mRenderNode.getRotationY();
    }

    public void setRotationY(float rotationY) {
        if (rotationY != getRotationY()) {
            invalidateViewProperty(true, false);
            this.mRenderNode.setRotationY(rotationY);
            invalidateViewProperty(false, true);
            invalidateParentIfNeededAndWasQuickRejected();
            notifySubtreeAccessibilityStateChangedIfNeeded();
        }
    }

    @ExportedProperty(category = "drawing")
    public float getRotationX() {
        return this.mRenderNode.getRotationX();
    }

    public void setRotationX(float rotationX) {
        if (rotationX != getRotationX()) {
            invalidateViewProperty(true, false);
            this.mRenderNode.setRotationX(rotationX);
            invalidateViewProperty(false, true);
            invalidateParentIfNeededAndWasQuickRejected();
            notifySubtreeAccessibilityStateChangedIfNeeded();
        }
    }

    @ExportedProperty(category = "drawing")
    public float getScaleX() {
        return this.mRenderNode.getScaleX();
    }

    public void setScaleX(float scaleX) {
        if (scaleX != getScaleX()) {
            scaleX = sanitizeFloatPropertyValue(scaleX, "scaleX");
            invalidateViewProperty(true, false);
            this.mRenderNode.setScaleX(scaleX);
            invalidateViewProperty(false, true);
            invalidateParentIfNeededAndWasQuickRejected();
            notifySubtreeAccessibilityStateChangedIfNeeded();
        }
    }

    @ExportedProperty(category = "drawing")
    public float getScaleY() {
        return this.mRenderNode.getScaleY();
    }

    public void setScaleY(float scaleY) {
        if (scaleY != getScaleY()) {
            scaleY = sanitizeFloatPropertyValue(scaleY, "scaleY");
            invalidateViewProperty(true, false);
            this.mRenderNode.setScaleY(scaleY);
            invalidateViewProperty(false, true);
            invalidateParentIfNeededAndWasQuickRejected();
            notifySubtreeAccessibilityStateChangedIfNeeded();
        }
    }

    @ExportedProperty(category = "drawing")
    public float getPivotX() {
        return this.mRenderNode.getPivotX();
    }

    public void setPivotX(float pivotX) {
        if (!this.mRenderNode.isPivotExplicitlySet() || pivotX != getPivotX()) {
            invalidateViewProperty(true, false);
            this.mRenderNode.setPivotX(pivotX);
            invalidateViewProperty(false, true);
            invalidateParentIfNeededAndWasQuickRejected();
        }
    }

    @ExportedProperty(category = "drawing")
    public float getPivotY() {
        return this.mRenderNode.getPivotY();
    }

    public void setPivotY(float pivotY) {
        if (!this.mRenderNode.isPivotExplicitlySet() || pivotY != getPivotY()) {
            invalidateViewProperty(true, false);
            this.mRenderNode.setPivotY(pivotY);
            invalidateViewProperty(false, true);
            invalidateParentIfNeededAndWasQuickRejected();
        }
    }

    public boolean isPivotSet() {
        return this.mRenderNode.isPivotExplicitlySet();
    }

    public void resetPivot() {
        if (this.mRenderNode.resetPivot()) {
            invalidateViewProperty(false, false);
        }
    }

    @ExportedProperty(category = "drawing")
    public float getAlpha() {
        TransformationInfo transformationInfo = this.mTransformationInfo;
        return transformationInfo != null ? transformationInfo.mAlpha : 1.0f;
    }

    public void forceHasOverlappingRendering(boolean hasOverlappingRendering) {
        this.mPrivateFlags3 |= 16777216;
        if (hasOverlappingRendering) {
            this.mPrivateFlags3 |= 8388608;
        } else {
            this.mPrivateFlags3 &= -8388609;
        }
    }

    public final boolean getHasOverlappingRendering() {
        int i = this.mPrivateFlags3;
        if ((16777216 & i) != 0) {
            return (i & 8388608) != 0;
        } else {
            return hasOverlappingRendering();
        }
    }

    @ExportedProperty(category = "drawing")
    public boolean hasOverlappingRendering() {
        return true;
    }

    public void setAlpha(float alpha) {
        ensureTransformationInfo();
        if (this.mTransformationInfo.mAlpha != alpha) {
            setAlphaInternal(alpha);
            if (onSetAlpha((int) (255.0f * alpha))) {
                this.mPrivateFlags |= 262144;
                invalidateParentCaches();
                invalidate(true);
                return;
            }
            this.mPrivateFlags &= -262145;
            invalidateViewProperty(true, false);
            this.mRenderNode.setAlpha(getFinalAlpha());
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123768435)
    public boolean setAlphaNoInvalidation(float alpha) {
        ensureTransformationInfo();
        if (this.mTransformationInfo.mAlpha != alpha) {
            setAlphaInternal(alpha);
            if (onSetAlpha((int) (255.0f * alpha))) {
                this.mPrivateFlags |= 262144;
                return true;
            }
            this.mPrivateFlags &= -262145;
            this.mRenderNode.setAlpha(getFinalAlpha());
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public void setAlphaInternal(float alpha) {
        float oldAlpha = this.mTransformationInfo.mAlpha;
        this.mTransformationInfo.mAlpha = alpha;
        int i = 1;
        int i2 = alpha == 0.0f ? 1 : 0;
        if (oldAlpha != 0.0f) {
            i = 0;
        }
        if ((i2 ^ i) != 0) {
            notifySubtreeAccessibilityStateChangedIfNeeded();
        }
    }

    public void setTransitionAlpha(float alpha) {
        ensureTransformationInfo();
        if (this.mTransformationInfo.mTransitionAlpha != alpha) {
            this.mTransformationInfo.mTransitionAlpha = alpha;
            this.mPrivateFlags &= -262145;
            invalidateViewProperty(true, false);
            this.mRenderNode.setAlpha(getFinalAlpha());
        }
    }

    private float getFinalAlpha() {
        TransformationInfo transformationInfo = this.mTransformationInfo;
        if (transformationInfo != null) {
            return transformationInfo.mAlpha * this.mTransformationInfo.mTransitionAlpha;
        }
        return 1.0f;
    }

    @ExportedProperty(category = "drawing")
    public float getTransitionAlpha() {
        TransformationInfo transformationInfo = this.mTransformationInfo;
        return transformationInfo != null ? transformationInfo.mTransitionAlpha : 1.0f;
    }

    public void setForceDarkAllowed(boolean allow) {
        if (this.mRenderNode.setForceDarkAllowed(allow)) {
            invalidate();
        }
    }

    @ExportedProperty(category = "drawing")
    public boolean isForceDarkAllowed() {
        return this.mRenderNode.isForceDarkAllowed();
    }

    @CapturedViewProperty
    public final int getTop() {
        return this.mTop;
    }

    public final void setTop(int top) {
        if (top != this.mTop) {
            int yLoc;
            int minTop;
            boolean matrixIsIdentity = hasIdentityMatrix();
            if (!matrixIsIdentity) {
                invalidate(true);
            } else if (this.mAttachInfo != null) {
                yLoc = this.mTop;
                if (top < yLoc) {
                    minTop = top;
                    yLoc = top - yLoc;
                } else {
                    minTop = this.mTop;
                    yLoc = 0;
                }
                invalidate(0, yLoc, this.mRight - this.mLeft, this.mBottom - minTop);
            }
            yLoc = this.mRight - this.mLeft;
            minTop = this.mBottom - this.mTop;
            this.mTop = top;
            this.mRenderNode.setTop(this.mTop);
            sizeChange(yLoc, this.mBottom - this.mTop, yLoc, minTop);
            if (!matrixIsIdentity) {
                this.mPrivateFlags |= 32;
                invalidate(true);
            }
            this.mBackgroundSizeChanged = true;
            this.mDefaultFocusHighlightSizeChanged = true;
            ForegroundInfo foregroundInfo = this.mForegroundInfo;
            if (foregroundInfo != null) {
                foregroundInfo.mBoundsChanged = true;
            }
            invalidateParentIfNeeded();
            if ((this.mPrivateFlags2 & 268435456) == 268435456) {
                invalidateParentIfNeeded();
            }
        }
    }

    @CapturedViewProperty
    public final int getBottom() {
        return this.mBottom;
    }

    public boolean isDirty() {
        return (this.mPrivateFlags & 2097152) != 0;
    }

    public final void setBottom(int bottom) {
        if (bottom != this.mBottom) {
            int maxBottom;
            boolean matrixIsIdentity = hasIdentityMatrix();
            if (!matrixIsIdentity) {
                invalidate(true);
            } else if (this.mAttachInfo != null) {
                if (bottom < this.mBottom) {
                    maxBottom = this.mBottom;
                } else {
                    maxBottom = bottom;
                }
                invalidate(0, 0, this.mRight - this.mLeft, maxBottom - this.mTop);
            }
            maxBottom = this.mRight - this.mLeft;
            int oldHeight = this.mBottom - this.mTop;
            this.mBottom = bottom;
            this.mRenderNode.setBottom(this.mBottom);
            sizeChange(maxBottom, this.mBottom - this.mTop, maxBottom, oldHeight);
            if (!matrixIsIdentity) {
                this.mPrivateFlags |= 32;
                invalidate(true);
            }
            this.mBackgroundSizeChanged = true;
            this.mDefaultFocusHighlightSizeChanged = true;
            ForegroundInfo foregroundInfo = this.mForegroundInfo;
            if (foregroundInfo != null) {
                foregroundInfo.mBoundsChanged = true;
            }
            invalidateParentIfNeeded();
            if ((this.mPrivateFlags2 & 268435456) == 268435456) {
                invalidateParentIfNeeded();
            }
        }
    }

    @CapturedViewProperty
    public final int getLeft() {
        return this.mLeft;
    }

    public final void setLeft(int left) {
        if (left != this.mLeft) {
            int xLoc;
            int minLeft;
            boolean matrixIsIdentity = hasIdentityMatrix();
            if (!matrixIsIdentity) {
                invalidate(true);
            } else if (this.mAttachInfo != null) {
                xLoc = this.mLeft;
                if (left < xLoc) {
                    minLeft = left;
                    xLoc = left - xLoc;
                } else {
                    minLeft = this.mLeft;
                    xLoc = 0;
                }
                invalidate(xLoc, 0, this.mRight - minLeft, this.mBottom - this.mTop);
            }
            xLoc = this.mRight - this.mLeft;
            minLeft = this.mBottom - this.mTop;
            this.mLeft = left;
            this.mRenderNode.setLeft(left);
            sizeChange(this.mRight - this.mLeft, minLeft, xLoc, minLeft);
            if (!matrixIsIdentity) {
                this.mPrivateFlags |= 32;
                invalidate(true);
            }
            this.mBackgroundSizeChanged = true;
            this.mDefaultFocusHighlightSizeChanged = true;
            ForegroundInfo foregroundInfo = this.mForegroundInfo;
            if (foregroundInfo != null) {
                foregroundInfo.mBoundsChanged = true;
            }
            invalidateParentIfNeeded();
            if ((this.mPrivateFlags2 & 268435456) == 268435456) {
                invalidateParentIfNeeded();
            }
        }
    }

    @CapturedViewProperty
    public final int getRight() {
        return this.mRight;
    }

    public final void setRight(int right) {
        if (right != this.mRight) {
            int maxRight;
            boolean matrixIsIdentity = hasIdentityMatrix();
            if (!matrixIsIdentity) {
                invalidate(true);
            } else if (this.mAttachInfo != null) {
                if (right < this.mRight) {
                    maxRight = this.mRight;
                } else {
                    maxRight = right;
                }
                invalidate(0, 0, maxRight - this.mLeft, this.mBottom - this.mTop);
            }
            maxRight = this.mRight - this.mLeft;
            int height = this.mBottom - this.mTop;
            this.mRight = right;
            this.mRenderNode.setRight(this.mRight);
            sizeChange(this.mRight - this.mLeft, height, maxRight, height);
            if (!matrixIsIdentity) {
                this.mPrivateFlags |= 32;
                invalidate(true);
            }
            this.mBackgroundSizeChanged = true;
            this.mDefaultFocusHighlightSizeChanged = true;
            ForegroundInfo foregroundInfo = this.mForegroundInfo;
            if (foregroundInfo != null) {
                foregroundInfo.mBoundsChanged = true;
            }
            invalidateParentIfNeeded();
            if ((this.mPrivateFlags2 & 268435456) == 268435456) {
                invalidateParentIfNeeded();
            }
        }
    }

    private static float sanitizeFloatPropertyValue(float value, String propertyName) {
        return sanitizeFloatPropertyValue(value, propertyName, -3.4028235E38f, Float.MAX_VALUE);
    }

    private static float sanitizeFloatPropertyValue(float value, String propertyName, float min, float max) {
        if (value >= min && value <= max) {
            return value;
        }
        String str = "' to ";
        String str2 = "Cannot set '";
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        if (value < min || value == Float.NEGATIVE_INFINITY) {
            if (!sThrowOnInvalidFloatProperties) {
                return min;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(propertyName);
            stringBuilder.append(str);
            stringBuilder.append(value);
            stringBuilder.append(", the value must be >= ");
            stringBuilder.append(min);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (value > max || value == Float.POSITIVE_INFINITY) {
            if (!sThrowOnInvalidFloatProperties) {
                return max;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(propertyName);
            stringBuilder.append(str);
            stringBuilder.append(value);
            stringBuilder.append(", the value must be <= ");
            stringBuilder.append(max);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (!Float.isNaN(value)) {
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("How do you get here?? ");
            stringBuilder2.append(value);
            throw new IllegalStateException(stringBuilder2.toString());
        } else if (!sThrowOnInvalidFloatProperties) {
            return 0.0f;
        } else {
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str2);
            stringBuilder2.append(propertyName);
            stringBuilder2.append("' to Float.NaN");
            throw new IllegalArgumentException(stringBuilder2.toString());
        }
    }

    @ExportedProperty(category = "drawing")
    public float getX() {
        return ((float) this.mLeft) + getTranslationX();
    }

    public void setX(float x) {
        setTranslationX(x - ((float) this.mLeft));
    }

    @ExportedProperty(category = "drawing")
    public float getY() {
        return ((float) this.mTop) + getTranslationY();
    }

    public void setY(float y) {
        setTranslationY(y - ((float) this.mTop));
    }

    @ExportedProperty(category = "drawing")
    public float getZ() {
        return getElevation() + getTranslationZ();
    }

    public void setZ(float z) {
        setTranslationZ(z - getElevation());
    }

    @ExportedProperty(category = "drawing")
    public float getElevation() {
        return this.mRenderNode.getElevation();
    }

    public void setElevation(float elevation) {
        if (elevation != getElevation()) {
            elevation = sanitizeFloatPropertyValue(elevation, "elevation");
            invalidateViewProperty(true, false);
            this.mRenderNode.setElevation(elevation);
            invalidateViewProperty(false, true);
            invalidateParentIfNeededAndWasQuickRejected();
        }
    }

    @ExportedProperty(category = "drawing")
    public float getTranslationX() {
        return this.mRenderNode.getTranslationX();
    }

    public void setTranslationX(float translationX) {
        if (translationX != getTranslationX()) {
            invalidateViewProperty(true, false);
            this.mRenderNode.setTranslationX(translationX);
            invalidateViewProperty(false, true);
            invalidateParentIfNeededAndWasQuickRejected();
            notifySubtreeAccessibilityStateChangedIfNeeded();
        }
    }

    @ExportedProperty(category = "drawing")
    public float getTranslationY() {
        return this.mRenderNode.getTranslationY();
    }

    public void setTranslationY(float translationY) {
        if (translationY != getTranslationY()) {
            invalidateViewProperty(true, false);
            this.mRenderNode.setTranslationY(translationY);
            invalidateViewProperty(false, true);
            invalidateParentIfNeededAndWasQuickRejected();
            notifySubtreeAccessibilityStateChangedIfNeeded();
        }
    }

    @ExportedProperty(category = "drawing")
    public float getTranslationZ() {
        return this.mRenderNode.getTranslationZ();
    }

    public void setTranslationZ(float translationZ) {
        if (translationZ != getTranslationZ()) {
            translationZ = sanitizeFloatPropertyValue(translationZ, "translationZ");
            invalidateViewProperty(true, false);
            this.mRenderNode.setTranslationZ(translationZ);
            invalidateViewProperty(false, true);
            invalidateParentIfNeededAndWasQuickRejected();
        }
    }

    public void setAnimationMatrix(Matrix matrix) {
        invalidateViewProperty(true, false);
        this.mRenderNode.setAnimationMatrix(matrix);
        invalidateViewProperty(false, true);
        invalidateParentIfNeededAndWasQuickRejected();
    }

    public Matrix getAnimationMatrix() {
        return this.mRenderNode.getAnimationMatrix();
    }

    public StateListAnimator getStateListAnimator() {
        return this.mStateListAnimator;
    }

    public void setStateListAnimator(StateListAnimator stateListAnimator) {
        StateListAnimator stateListAnimator2 = this.mStateListAnimator;
        if (stateListAnimator2 != stateListAnimator) {
            if (stateListAnimator2 != null) {
                stateListAnimator2.setTarget(null);
            }
            this.mStateListAnimator = stateListAnimator;
            if (stateListAnimator != null) {
                stateListAnimator.setTarget(this);
                if (isAttachedToWindow()) {
                    stateListAnimator.setState(getDrawableState());
                }
            }
        }
    }

    public final boolean getClipToOutline() {
        return this.mRenderNode.getClipToOutline();
    }

    public void setClipToOutline(boolean clipToOutline) {
        damageInParent();
        if (getClipToOutline() != clipToOutline) {
            this.mRenderNode.setClipToOutline(clipToOutline);
        }
    }

    private void setOutlineProviderFromAttribute(int providerInt) {
        if (providerInt == 0) {
            setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        } else if (providerInt == 1) {
            setOutlineProvider(null);
        } else if (providerInt == 2) {
            setOutlineProvider(ViewOutlineProvider.BOUNDS);
        } else if (providerInt == 3) {
            setOutlineProvider(ViewOutlineProvider.PADDED_BOUNDS);
        }
    }

    public void setOutlineProvider(ViewOutlineProvider provider) {
        this.mOutlineProvider = provider;
        invalidateOutline();
    }

    public ViewOutlineProvider getOutlineProvider() {
        return this.mOutlineProvider;
    }

    public void invalidateOutline() {
        rebuildOutline();
        notifySubtreeAccessibilityStateChangedIfNeeded();
        invalidateViewProperty(false, false);
    }

    private void rebuildOutline() {
        Outline outline = this.mAttachInfo;
        if (outline != null) {
            if (this.mOutlineProvider == null) {
                this.mRenderNode.setOutline(null);
            } else {
                outline = outline.mTmpOutline;
                outline.setEmpty();
                outline.setAlpha(1.0f);
                this.mOutlineProvider.getOutline(this, outline);
                this.mRenderNode.setOutline(outline);
            }
        }
    }

    @ExportedProperty(category = "drawing")
    public boolean hasShadow() {
        return this.mRenderNode.hasShadow();
    }

    public void setOutlineSpotShadowColor(int color) {
        if (this.mRenderNode.setSpotShadowColor(color)) {
            invalidateViewProperty(true, true);
        }
    }

    public int getOutlineSpotShadowColor() {
        return this.mRenderNode.getSpotShadowColor();
    }

    public void setOutlineAmbientShadowColor(int color) {
        if (this.mRenderNode.setAmbientShadowColor(color)) {
            invalidateViewProperty(true, true);
        }
    }

    public int getOutlineAmbientShadowColor() {
        return this.mRenderNode.getAmbientShadowColor();
    }

    public void setRevealClip(boolean shouldClip, float x, float y, float radius) {
        this.mRenderNode.setRevealClip(shouldClip, x, y, radius);
        invalidateViewProperty(false, false);
    }

    public void getHitRect(Rect outRect) {
        if (!hasIdentityMatrix()) {
            RectF tmpRect = this.mAttachInfo;
            if (tmpRect != null) {
                tmpRect = tmpRect.mTmpTransformRect;
                tmpRect.set(0.0f, 0.0f, (float) getWidth(), (float) getHeight());
                getMatrix().mapRect(tmpRect);
                outRect.set(((int) tmpRect.left) + this.mLeft, ((int) tmpRect.top) + this.mTop, ((int) tmpRect.right) + this.mLeft, ((int) tmpRect.bottom) + this.mTop);
                return;
            }
        }
        outRect.set(this.mLeft, this.mTop, this.mRight, this.mBottom);
    }

    /* Access modifiers changed, original: final */
    public final boolean pointInView(float localX, float localY) {
        return pointInView(localX, localY, 0.0f);
    }

    @UnsupportedAppUsage
    public boolean pointInView(float localX, float localY, float slop) {
        return localX >= (-slop) && localY >= (-slop) && localX < ((float) (this.mRight - this.mLeft)) + slop && localY < ((float) (this.mBottom - this.mTop)) + slop;
    }

    public void getFocusedRect(Rect r) {
        getDrawingRect(r);
    }

    public boolean getGlobalVisibleRect(Rect r, Point globalOffset) {
        int width = this.mRight - this.mLeft;
        int height = this.mBottom - this.mTop;
        boolean z = false;
        if (width <= 0 || height <= 0) {
            return false;
        }
        r.set(0, 0, width, height);
        if (globalOffset != null) {
            globalOffset.set(-this.mScrollX, -this.mScrollY);
        }
        ViewParent viewParent = this.mParent;
        if (viewParent == null || viewParent.getChildVisibleRect(this, r, globalOffset)) {
            z = true;
        }
        return z;
    }

    public final boolean getGlobalVisibleRect(Rect r) {
        return getGlobalVisibleRect(r, null);
    }

    public final boolean getLocalVisibleRect(Rect r) {
        AttachInfo attachInfo = this.mAttachInfo;
        Point offset = attachInfo != null ? attachInfo.mPoint : new Point();
        if (!getGlobalVisibleRect(r, offset)) {
            return false;
        }
        r.offset(-offset.x, -offset.y);
        return true;
    }

    public void offsetTopAndBottom(int offset) {
        if (offset != 0) {
            boolean matrixIsIdentity = hasIdentityMatrix();
            if (!matrixIsIdentity) {
                invalidateViewProperty(false, false);
            } else if (isHardwareAccelerated()) {
                invalidateViewProperty(false, false);
            } else {
                ViewParent p = this.mParent;
                if (p != null) {
                    Rect r = this.mAttachInfo;
                    if (r != null) {
                        int minTop;
                        int maxBottom;
                        int yLoc;
                        r = r.mTmpInvalRect;
                        if (offset < 0) {
                            minTop = this.mTop + offset;
                            maxBottom = this.mBottom;
                            yLoc = offset;
                        } else {
                            minTop = this.mTop;
                            maxBottom = this.mBottom + offset;
                            yLoc = 0;
                        }
                        r.set(0, yLoc, this.mRight - this.mLeft, maxBottom - minTop);
                        p.invalidateChild(this, r);
                    }
                }
            }
            this.mTop += offset;
            this.mBottom += offset;
            this.mRenderNode.offsetTopAndBottom(offset);
            if (isHardwareAccelerated()) {
                invalidateViewProperty(false, false);
                invalidateParentIfNeededAndWasQuickRejected();
            } else {
                if (!matrixIsIdentity) {
                    invalidateViewProperty(false, true);
                }
                invalidateParentIfNeeded();
            }
            notifySubtreeAccessibilityStateChangedIfNeeded();
        }
    }

    public void offsetLeftAndRight(int offset) {
        if (offset != 0) {
            boolean matrixIsIdentity = hasIdentityMatrix();
            if (!matrixIsIdentity) {
                invalidateViewProperty(false, false);
            } else if (isHardwareAccelerated()) {
                invalidateViewProperty(false, false);
            } else {
                ViewParent p = this.mParent;
                if (p != null) {
                    Rect r = this.mAttachInfo;
                    if (r != null) {
                        int minLeft;
                        int maxRight;
                        r = r.mTmpInvalRect;
                        if (offset < 0) {
                            minLeft = this.mLeft + offset;
                            maxRight = this.mRight;
                        } else {
                            minLeft = this.mLeft;
                            maxRight = this.mRight + offset;
                        }
                        r.set(0, 0, maxRight - minLeft, this.mBottom - this.mTop);
                        p.invalidateChild(this, r);
                    }
                }
            }
            this.mLeft += offset;
            this.mRight += offset;
            this.mRenderNode.offsetLeftAndRight(offset);
            if (isHardwareAccelerated()) {
                invalidateViewProperty(false, false);
                invalidateParentIfNeededAndWasQuickRejected();
            } else {
                if (!matrixIsIdentity) {
                    invalidateViewProperty(false, true);
                }
                invalidateParentIfNeeded();
            }
            notifySubtreeAccessibilityStateChangedIfNeeded();
        }
    }

    @ExportedProperty(deepExport = true, prefix = "layout_")
    public LayoutParams getLayoutParams() {
        return this.mLayoutParams;
    }

    public void setLayoutParams(LayoutParams params) {
        if (params != null) {
            this.mLayoutParams = params;
            resolveLayoutParams();
            ViewParent viewParent = this.mParent;
            if (viewParent instanceof ViewGroup) {
                ((ViewGroup) viewParent).onSetLayoutParams(this, params);
            }
            requestLayout();
            return;
        }
        throw new NullPointerException("Layout parameters cannot be null");
    }

    public void resolveLayoutParams() {
        LayoutParams layoutParams = this.mLayoutParams;
        if (layoutParams != null) {
            layoutParams.resolveLayoutDirection(getLayoutDirection());
        }
    }

    public void scrollTo(int x, int y) {
        if (this.mScrollX != x || this.mScrollY != y) {
            int oldX = this.mScrollX;
            int oldY = this.mScrollY;
            this.mScrollX = x;
            this.mScrollY = y;
            invalidateParentCaches();
            onScrollChanged(this.mScrollX, this.mScrollY, oldX, oldY);
            if (!awakenScrollBars()) {
                postInvalidateOnAnimation();
            }
        }
    }

    public void scrollBy(int x, int y) {
        scrollTo(this.mScrollX + x, this.mScrollY + y);
    }

    /* Access modifiers changed, original: protected */
    public boolean awakenScrollBars() {
        ScrollabilityCache scrollabilityCache = this.mScrollCache;
        if (scrollabilityCache == null || !awakenScrollBars(scrollabilityCache.scrollBarDefaultDelayBeforeFade, true)) {
            return false;
        }
        return true;
    }

    private boolean initialAwakenScrollBars() {
        ScrollabilityCache scrollabilityCache = this.mScrollCache;
        if (scrollabilityCache == null || !awakenScrollBars(scrollabilityCache.scrollBarDefaultDelayBeforeFade * 4, true)) {
            return false;
        }
        return true;
    }

    /* Access modifiers changed, original: protected */
    public boolean awakenScrollBars(int startDelay) {
        return awakenScrollBars(startDelay, true);
    }

    /* Access modifiers changed, original: protected */
    public boolean awakenScrollBars(int startDelay, boolean invalidate) {
        ScrollabilityCache scrollCache = this.mScrollCache;
        if (scrollCache == null || !scrollCache.fadeScrollBars) {
            return false;
        }
        if (scrollCache.scrollBar == null) {
            scrollCache.scrollBar = new ScrollBarDrawable();
            scrollCache.scrollBar.setState(getDrawableState());
            scrollCache.scrollBar.setCallback(this);
        }
        if (!isHorizontalScrollBarEnabled() && !isVerticalScrollBarEnabled()) {
            return false;
        }
        if (invalidate) {
            postInvalidateOnAnimation();
        }
        if (scrollCache.state == 0) {
            startDelay = Math.max(750, startDelay);
        }
        long fadeStartTime = AnimationUtils.currentAnimationTimeMillis() + ((long) startDelay);
        scrollCache.fadeStartTime = fadeStartTime;
        scrollCache.state = 1;
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            attachInfo.mHandler.removeCallbacks(scrollCache);
            this.mAttachInfo.mHandler.postAtTime(scrollCache, fadeStartTime);
        }
        return true;
    }

    private boolean skipInvalidate() {
        if ((this.mViewFlags & 12) != 0 && this.mCurrentAnimation == null) {
            ViewParent viewParent = this.mParent;
            if (!((viewParent instanceof ViewGroup) && ((ViewGroup) viewParent).isViewTransitioning(this))) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public void invalidate(Rect dirty) {
        int scrollX = this.mScrollX;
        int scrollY = this.mScrollY;
        invalidateInternal(dirty.left - scrollX, dirty.top - scrollY, dirty.right - scrollX, dirty.bottom - scrollY, true, false);
    }

    @Deprecated
    public void invalidate(int l, int t, int r, int b) {
        int scrollX = this.mScrollX;
        int scrollY = this.mScrollY;
        invalidateInternal(l - scrollX, t - scrollY, r - scrollX, b - scrollY, true, false);
    }

    public void invalidate() {
        invalidate(true);
    }

    @UnsupportedAppUsage
    public void invalidate(boolean invalidateCache) {
        invalidateInternal(0, 0, this.mRight - this.mLeft, this.mBottom - this.mTop, invalidateCache, true);
    }

    /* Access modifiers changed, original: 0000 */
    public void invalidateInternal(int l, int t, int r, int b, boolean invalidateCache, boolean fullInvalidate) {
        GhostView ghostView = this.mGhostView;
        if (ghostView != null) {
            ghostView.invalidate(true);
        } else if (!skipInvalidate()) {
            this.mCachedContentCaptureSession = null;
            int i = this.mPrivateFlags;
            if ((i & 48) == 48 || ((invalidateCache && (i & 32768) == 32768) || (this.mPrivateFlags & Integer.MIN_VALUE) != Integer.MIN_VALUE || (fullInvalidate && isOpaque() != this.mLastIsOpaque))) {
                if (fullInvalidate) {
                    this.mLastIsOpaque = isOpaque();
                    this.mPrivateFlags &= -33;
                }
                this.mPrivateFlags |= 2097152;
                if (invalidateCache) {
                    this.mPrivateFlags |= Integer.MIN_VALUE;
                    this.mPrivateFlags &= -32769;
                }
                AttachInfo ai = this.mAttachInfo;
                ViewParent p = this.mParent;
                if (p != null && ai != null && l < r && t < b) {
                    Rect damage = ai.mTmpInvalRect;
                    damage.set(l, t, r, b);
                    p.invalidateChild(this, damage);
                }
                Drawable drawable = this.mBackground;
                if (drawable != null && drawable.isProjected()) {
                    View receiver = getProjectionReceiver();
                    if (receiver != null) {
                        receiver.damageInParent();
                    }
                }
            }
        }
    }

    private View getProjectionReceiver() {
        ViewParent p = getParent();
        while (p != null && (p instanceof View)) {
            View v = (View) p;
            if (v.isProjectionReceiver()) {
                return v;
            }
            p = p.getParent();
        }
        return null;
    }

    private boolean isProjectionReceiver() {
        return this.mBackground != null;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void invalidateViewProperty(boolean invalidateParent, boolean forceRedraw) {
        if (isHardwareAccelerated() && this.mRenderNode.hasDisplayList() && (this.mPrivateFlags & 64) == 0) {
            damageInParent();
            return;
        }
        if (invalidateParent) {
            invalidateParentCaches();
        }
        if (forceRedraw) {
            this.mPrivateFlags |= 32;
        }
        invalidate(false);
    }

    /* Access modifiers changed, original: protected */
    public void damageInParent() {
        ViewParent viewParent = this.mParent;
        if (viewParent != null && this.mAttachInfo != null) {
            viewParent.onDescendantInvalidated(this, this);
        }
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public void invalidateParentCaches() {
        ViewParent viewParent = this.mParent;
        if (viewParent instanceof View) {
            View view = (View) viewParent;
            view.mPrivateFlags |= Integer.MIN_VALUE;
        }
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public void invalidateParentIfNeeded() {
        if (isHardwareAccelerated()) {
            ViewParent viewParent = this.mParent;
            if (viewParent instanceof View) {
                ((View) viewParent).invalidate(true);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void invalidateParentIfNeededAndWasQuickRejected() {
        if ((this.mPrivateFlags2 & 268435456) != 0) {
            invalidateParentIfNeeded();
        }
    }

    @ExportedProperty(category = "drawing")
    public boolean isOpaque() {
        return (this.mPrivateFlags & 25165824) == 25165824 && getFinalAlpha() >= 1.0f;
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public void computeOpaqueFlags() {
        Drawable drawable = this.mBackground;
        if (drawable == null || drawable.getOpacity() != -1) {
            this.mPrivateFlags &= -8388609;
        } else {
            this.mPrivateFlags |= 8388608;
        }
        int flags = this.mViewFlags;
        if (((flags & 512) == 0 && (flags & 256) == 0) || (flags & 50331648) == 0 || (50331648 & flags) == 33554432) {
            this.mPrivateFlags |= 16777216;
        } else {
            this.mPrivateFlags &= -16777217;
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean hasOpaqueScrollbars() {
        return (this.mPrivateFlags & 16777216) == 16777216;
    }

    public Handler getHandler() {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            return attachInfo.mHandler;
        }
        return null;
    }

    private HandlerActionQueue getRunQueue() {
        if (this.mRunQueue == null) {
            this.mRunQueue = new HandlerActionQueue();
        }
        return this.mRunQueue;
    }

    @UnsupportedAppUsage
    public ViewRootImpl getViewRootImpl() {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            return attachInfo.mViewRootImpl;
        }
        return null;
    }

    @UnsupportedAppUsage
    public ThreadedRenderer getThreadedRenderer() {
        AttachInfo attachInfo = this.mAttachInfo;
        return attachInfo != null ? attachInfo.mThreadedRenderer : null;
    }

    public boolean post(Runnable action) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            return attachInfo.mHandler.post(action);
        }
        getRunQueue().post(action);
        return true;
    }

    public boolean postDelayed(Runnable action, long delayMillis) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            return attachInfo.mHandler.postDelayed(action, delayMillis);
        }
        getRunQueue().postDelayed(action, delayMillis);
        return true;
    }

    public void postOnAnimation(Runnable action) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            attachInfo.mViewRootImpl.mChoreographer.postCallback(1, action, null);
        } else {
            getRunQueue().post(action);
        }
    }

    public void postOnAnimationDelayed(Runnable action, long delayMillis) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            attachInfo.mViewRootImpl.mChoreographer.postCallbackDelayed(1, action, null, delayMillis);
        } else {
            getRunQueue().postDelayed(action, delayMillis);
        }
    }

    public boolean removeCallbacks(Runnable action) {
        if (action != null) {
            AttachInfo attachInfo = this.mAttachInfo;
            if (attachInfo != null) {
                attachInfo.mHandler.removeCallbacks(action);
                attachInfo.mViewRootImpl.mChoreographer.removeCallbacks(1, action, null);
            }
            getRunQueue().removeCallbacks(action);
        }
        return true;
    }

    public void postInvalidate() {
        postInvalidateDelayed(0);
    }

    public void postInvalidate(int left, int top, int right, int bottom) {
        postInvalidateDelayed(0, left, top, right, bottom);
    }

    public void postInvalidateDelayed(long delayMilliseconds) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            attachInfo.mViewRootImpl.dispatchInvalidateDelayed(this, delayMilliseconds);
        }
    }

    public void postInvalidateDelayed(long delayMilliseconds, int left, int top, int right, int bottom) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            InvalidateInfo info = InvalidateInfo.obtain();
            info.target = this;
            info.left = left;
            info.top = top;
            info.right = right;
            info.bottom = bottom;
            attachInfo.mViewRootImpl.dispatchInvalidateRectDelayed(info, delayMilliseconds);
        }
    }

    public void postInvalidateOnAnimation() {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            attachInfo.mViewRootImpl.dispatchInvalidateOnAnimation(this);
        }
    }

    public void postInvalidateOnAnimation(int left, int top, int right, int bottom) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            InvalidateInfo info = InvalidateInfo.obtain();
            info.target = this;
            info.left = left;
            info.top = top;
            info.right = right;
            info.bottom = bottom;
            attachInfo.mViewRootImpl.dispatchInvalidateRectOnAnimation(info);
        }
    }

    private void postSendViewScrolledAccessibilityEventCallback(int dx, int dy) {
        if (this.mSendViewScrolledAccessibilityEvent == null) {
            this.mSendViewScrolledAccessibilityEvent = new SendViewScrolledAccessibilityEvent(this, null);
        }
        this.mSendViewScrolledAccessibilityEvent.post(dx, dy);
    }

    public void computeScroll() {
    }

    public boolean isHorizontalFadingEdgeEnabled() {
        return (this.mViewFlags & 4096) == 4096;
    }

    public void setHorizontalFadingEdgeEnabled(boolean horizontalFadingEdgeEnabled) {
        if (isHorizontalFadingEdgeEnabled() != horizontalFadingEdgeEnabled) {
            if (horizontalFadingEdgeEnabled) {
                initScrollCache();
            }
            this.mViewFlags ^= 4096;
        }
    }

    public boolean isVerticalFadingEdgeEnabled() {
        return (this.mViewFlags & 8192) == 8192;
    }

    public void setVerticalFadingEdgeEnabled(boolean verticalFadingEdgeEnabled) {
        if (isVerticalFadingEdgeEnabled() != verticalFadingEdgeEnabled) {
            if (verticalFadingEdgeEnabled) {
                initScrollCache();
            }
            this.mViewFlags ^= 8192;
        }
    }

    public int getFadingEdge() {
        return this.mViewFlags & 12288;
    }

    public int getFadingEdgeLength() {
        ScrollabilityCache scrollabilityCache = this.mScrollCache;
        if (scrollabilityCache == null || (this.mViewFlags & 12288) == 0) {
            return 0;
        }
        return scrollabilityCache.fadingEdgeLength;
    }

    /* Access modifiers changed, original: protected */
    public float getTopFadingEdgeStrength() {
        return computeVerticalScrollOffset() > 0 ? 1.0f : 0.0f;
    }

    /* Access modifiers changed, original: protected */
    public float getBottomFadingEdgeStrength() {
        return computeVerticalScrollOffset() + computeVerticalScrollExtent() < computeVerticalScrollRange() ? 1.0f : 0.0f;
    }

    /* Access modifiers changed, original: protected */
    public float getLeftFadingEdgeStrength() {
        return computeHorizontalScrollOffset() > 0 ? 1.0f : 0.0f;
    }

    /* Access modifiers changed, original: protected */
    public float getRightFadingEdgeStrength() {
        return computeHorizontalScrollOffset() + computeHorizontalScrollExtent() < computeHorizontalScrollRange() ? 1.0f : 0.0f;
    }

    public boolean isHorizontalScrollBarEnabled() {
        return (this.mViewFlags & 256) == 256;
    }

    public void setHorizontalScrollBarEnabled(boolean horizontalScrollBarEnabled) {
        if (isHorizontalScrollBarEnabled() != horizontalScrollBarEnabled) {
            this.mViewFlags ^= 256;
            computeOpaqueFlags();
            resolvePadding();
        }
    }

    public boolean isVerticalScrollBarEnabled() {
        return (this.mViewFlags & 512) == 512;
    }

    public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
        if (isVerticalScrollBarEnabled() != verticalScrollBarEnabled) {
            this.mViewFlags ^= 512;
            computeOpaqueFlags();
            resolvePadding();
        }
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public void recomputePadding() {
        internalSetPadding(this.mUserPaddingLeft, this.mPaddingTop, this.mUserPaddingRight, this.mUserPaddingBottom);
    }

    public void setScrollbarFadingEnabled(boolean fadeScrollbars) {
        initScrollCache();
        ScrollabilityCache scrollabilityCache = this.mScrollCache;
        scrollabilityCache.fadeScrollBars = fadeScrollbars;
        if (fadeScrollbars) {
            scrollabilityCache.state = 0;
        } else {
            scrollabilityCache.state = 1;
        }
    }

    public boolean isScrollbarFadingEnabled() {
        ScrollabilityCache scrollabilityCache = this.mScrollCache;
        return scrollabilityCache != null && scrollabilityCache.fadeScrollBars;
    }

    public int getScrollBarDefaultDelayBeforeFade() {
        ScrollabilityCache scrollabilityCache = this.mScrollCache;
        if (scrollabilityCache == null) {
            return ViewConfiguration.getScrollDefaultDelay();
        }
        return scrollabilityCache.scrollBarDefaultDelayBeforeFade;
    }

    public void setScrollBarDefaultDelayBeforeFade(int scrollBarDefaultDelayBeforeFade) {
        getScrollCache().scrollBarDefaultDelayBeforeFade = scrollBarDefaultDelayBeforeFade;
    }

    public int getScrollBarFadeDuration() {
        ScrollabilityCache scrollabilityCache = this.mScrollCache;
        if (scrollabilityCache == null) {
            return ViewConfiguration.getScrollBarFadeDuration();
        }
        return scrollabilityCache.scrollBarFadeDuration;
    }

    public void setScrollBarFadeDuration(int scrollBarFadeDuration) {
        getScrollCache().scrollBarFadeDuration = scrollBarFadeDuration;
    }

    public int getScrollBarSize() {
        ScrollabilityCache scrollabilityCache = this.mScrollCache;
        if (scrollabilityCache == null) {
            return ViewConfiguration.get(this.mContext).getScaledScrollBarSize();
        }
        return scrollabilityCache.scrollBarSize;
    }

    public void setScrollBarSize(int scrollBarSize) {
        getScrollCache().scrollBarSize = scrollBarSize;
    }

    public void setScrollBarStyle(int style) {
        int i = this.mViewFlags;
        if (style != (i & 50331648)) {
            this.mViewFlags = (i & -50331649) | (50331648 & style);
            computeOpaqueFlags();
            resolvePadding();
        }
    }

    @ExportedProperty(mapping = {@IntToString(from = 0, to = "INSIDE_OVERLAY"), @IntToString(from = 16777216, to = "INSIDE_INSET"), @IntToString(from = 33554432, to = "OUTSIDE_OVERLAY"), @IntToString(from = 50331648, to = "OUTSIDE_INSET")})
    public int getScrollBarStyle() {
        return this.mViewFlags & 50331648;
    }

    /* Access modifiers changed, original: protected */
    public int computeHorizontalScrollRange() {
        return getWidth();
    }

    /* Access modifiers changed, original: protected */
    public int computeHorizontalScrollOffset() {
        return this.mScrollX;
    }

    /* Access modifiers changed, original: protected */
    public int computeHorizontalScrollExtent() {
        return getWidth();
    }

    /* Access modifiers changed, original: protected */
    public int computeVerticalScrollRange() {
        return getHeight();
    }

    /* Access modifiers changed, original: protected */
    public int computeVerticalScrollOffset() {
        return this.mScrollY;
    }

    /* Access modifiers changed, original: protected */
    public int computeVerticalScrollExtent() {
        return getHeight();
    }

    public boolean canScrollHorizontally(int direction) {
        int offset = computeHorizontalScrollOffset();
        int range = computeHorizontalScrollRange() - computeHorizontalScrollExtent();
        boolean z = false;
        if (range == 0) {
            return false;
        }
        if (direction < 0) {
            if (offset > 0) {
                z = true;
            }
            return z;
        }
        if (offset < range - 1) {
            z = true;
        }
        return z;
    }

    public boolean canScrollVertically(int direction) {
        int offset = computeVerticalScrollOffset();
        int range = computeVerticalScrollRange() - computeVerticalScrollExtent();
        boolean z = false;
        if (range == 0) {
            return false;
        }
        if (direction < 0) {
            if (offset > 0) {
                z = true;
            }
            return z;
        }
        if (offset < range - 1) {
            z = true;
        }
        return z;
    }

    /* Access modifiers changed, original: 0000 */
    public void getScrollIndicatorBounds(Rect out) {
        int i = this.mScrollX;
        out.left = i;
        out.right = (i + this.mRight) - this.mLeft;
        i = this.mScrollY;
        out.top = i;
        out.bottom = (i + this.mBottom) - this.mTop;
    }

    private void onDrawScrollIndicators(Canvas c) {
        if ((this.mPrivateFlags3 & SCROLL_INDICATORS_PFLAG3_MASK) != 0) {
            Drawable dr = this.mScrollIndicatorDrawable;
            if (dr != null) {
                int leftRtl;
                int rightRtl;
                int h = dr.getIntrinsicHeight();
                int w = dr.getIntrinsicWidth();
                Rect rect = this.mAttachInfo.mTmpInvalRect;
                getScrollIndicatorBounds(rect);
                if ((this.mPrivateFlags3 & 256) != 0 && canScrollVertically(-1)) {
                    dr.setBounds(rect.left, rect.top, rect.right, rect.top + h);
                    dr.draw(c);
                }
                if ((this.mPrivateFlags3 & 512) != 0 && canScrollVertically(1)) {
                    dr.setBounds(rect.left, rect.bottom - h, rect.right, rect.bottom);
                    dr.draw(c);
                }
                if (getLayoutDirection() == 1) {
                    leftRtl = 8192;
                    rightRtl = 4096;
                } else {
                    leftRtl = 4096;
                    rightRtl = 8192;
                }
                if ((this.mPrivateFlags3 & (leftRtl | 1024)) != 0 && canScrollHorizontally(-1)) {
                    dr.setBounds(rect.left, rect.top, rect.left + w, rect.bottom);
                    dr.draw(c);
                }
                if ((this.mPrivateFlags3 & (rightRtl | 2048)) != 0 && canScrollHorizontally(1)) {
                    dr.setBounds(rect.right - w, rect.top, rect.right, rect.bottom);
                    dr.draw(c);
                }
            }
        }
    }

    private void getHorizontalScrollBarBounds(Rect drawBounds, Rect touchBounds) {
        Rect bounds = drawBounds != null ? drawBounds : touchBounds;
        if (bounds != null) {
            int verticalScrollBarGap = 0;
            int inside = (this.mViewFlags & 33554432) == 0 ? -1 : 0;
            boolean drawVerticalScrollBar = isVerticalScrollBarEnabled() && !isVerticalScrollBarHidden();
            int size = getHorizontalScrollbarHeight();
            if (drawVerticalScrollBar) {
                verticalScrollBarGap = getVerticalScrollbarWidth();
            }
            int width = this.mRight - this.mLeft;
            int height = this.mBottom - this.mTop;
            bounds.top = ((this.mScrollY + height) - size) - (this.mUserPaddingBottom & inside);
            int i = this.mScrollX;
            bounds.left = (this.mPaddingLeft & inside) + i;
            bounds.right = ((i + width) - (this.mUserPaddingRight & inside)) - verticalScrollBarGap;
            bounds.bottom = bounds.top + size;
            if (touchBounds != null) {
                if (touchBounds != bounds) {
                    touchBounds.set(bounds);
                }
                i = this.mScrollCache.scrollBarMinTouchTarget;
                if (touchBounds.height() < i) {
                    touchBounds.bottom = Math.min(touchBounds.bottom + ((i - touchBounds.height()) / 2), this.mScrollY + height);
                    touchBounds.top = touchBounds.bottom - i;
                }
                if (touchBounds.width() < i) {
                    touchBounds.left -= (i - touchBounds.width()) / 2;
                    touchBounds.right = touchBounds.left + i;
                }
            }
        }
    }

    private void getVerticalScrollBarBounds(Rect bounds, Rect touchBounds) {
        if (this.mRoundScrollbarRenderer == null) {
            getStraightVerticalScrollBarBounds(bounds, touchBounds);
        } else {
            getRoundVerticalScrollBarBounds(bounds != null ? bounds : touchBounds);
        }
    }

    private void getRoundVerticalScrollBarBounds(Rect bounds) {
        int width = this.mRight - this.mLeft;
        int height = this.mBottom - this.mTop;
        bounds.left = this.mScrollX;
        bounds.top = this.mScrollY;
        bounds.right = bounds.left + width;
        bounds.bottom = this.mScrollY + height;
    }

    private void getStraightVerticalScrollBarBounds(Rect drawBounds, Rect touchBounds) {
        Rect bounds = drawBounds != null ? drawBounds : touchBounds;
        if (bounds != null) {
            int inside = (this.mViewFlags & 33554432) == 0 ? -1 : 0;
            int size = getVerticalScrollbarWidth();
            int verticalScrollbarPosition = this.mVerticalScrollbarPosition;
            if (verticalScrollbarPosition == 0) {
                verticalScrollbarPosition = isLayoutRtl() ? 1 : 2;
            }
            int width = this.mRight - this.mLeft;
            int height = this.mBottom - this.mTop;
            if (verticalScrollbarPosition != 1) {
                bounds.left = ((this.mScrollX + width) - size) - (this.mUserPaddingRight & inside);
            } else {
                bounds.left = this.mScrollX + (this.mUserPaddingLeft & inside);
            }
            bounds.top = this.mScrollY + (this.mPaddingTop & inside);
            bounds.right = bounds.left + size;
            bounds.bottom = (this.mScrollY + height) - (this.mUserPaddingBottom & inside);
            if (touchBounds != null) {
                if (touchBounds != bounds) {
                    touchBounds.set(bounds);
                }
                int minTouchTarget = this.mScrollCache.scrollBarMinTouchTarget;
                if (touchBounds.width() < minTouchTarget) {
                    int adjust = (minTouchTarget - touchBounds.width()) / 2;
                    if (verticalScrollbarPosition == 2) {
                        touchBounds.right = Math.min(touchBounds.right + adjust, this.mScrollX + width);
                        touchBounds.left = touchBounds.right - minTouchTarget;
                    } else {
                        touchBounds.left = Math.max(touchBounds.left + adjust, this.mScrollX);
                        touchBounds.right = touchBounds.left + minTouchTarget;
                    }
                }
                if (touchBounds.height() < minTouchTarget) {
                    touchBounds.top -= (minTouchTarget - touchBounds.height()) / 2;
                    touchBounds.bottom = touchBounds.top + minTouchTarget;
                }
            }
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void onDrawScrollBars(Canvas canvas) {
        ScrollabilityCache cache = this.mScrollCache;
        Canvas canvas2;
        if (cache != null) {
            int state = cache.state;
            if (state != 0) {
                boolean invalidate;
                if (state == 2) {
                    if (cache.interpolatorValues == null) {
                        cache.interpolatorValues = new float[1];
                    }
                    float[] values = cache.interpolatorValues;
                    if (cache.scrollBarInterpolator.timeToValues(values) == Result.FREEZE_END) {
                        cache.state = 0;
                    } else {
                        cache.scrollBar.mutate().setAlpha(Math.round(values[0]));
                    }
                    invalidate = true;
                } else {
                    cache.scrollBar.mutate().setAlpha(255);
                    invalidate = false;
                }
                boolean drawHorizontalScrollBar = isHorizontalScrollBarEnabled();
                boolean z = isVerticalScrollBarEnabled() && !isVerticalScrollBarHidden();
                boolean drawVerticalScrollBar = z;
                if (this.mRoundScrollbarRenderer == null) {
                    canvas2 = canvas;
                    if (drawVerticalScrollBar || drawHorizontalScrollBar) {
                        Rect bounds;
                        Drawable scrollBar;
                        Drawable scrollBar2 = cache.scrollBar;
                        if (drawHorizontalScrollBar) {
                            scrollBar2.setParameters(computeHorizontalScrollRange(), computeHorizontalScrollOffset(), computeHorizontalScrollExtent(), false);
                            Rect bounds2 = cache.mScrollBarBounds;
                            getHorizontalScrollBarBounds(bounds2, null);
                            bounds = bounds2;
                            scrollBar = scrollBar2;
                            onDrawHorizontalScrollBar(canvas, scrollBar2, bounds2.left, bounds2.top, bounds2.right, bounds2.bottom);
                            if (invalidate) {
                                invalidate(bounds);
                            }
                        } else {
                            scrollBar = scrollBar2;
                        }
                        if (drawVerticalScrollBar) {
                            scrollBar.setParameters(computeVerticalScrollRange(), computeVerticalScrollOffset(), computeVerticalScrollExtent(), true);
                            bounds = cache.mScrollBarBounds;
                            getVerticalScrollBarBounds(bounds, null);
                            onDrawVerticalScrollBar(canvas, scrollBar, bounds.left, bounds.top, bounds.right, bounds.bottom);
                            if (invalidate) {
                                invalidate(bounds);
                            }
                        }
                    }
                } else if (drawVerticalScrollBar) {
                    Rect bounds3 = cache.mScrollBarBounds;
                    getVerticalScrollBarBounds(bounds3, null);
                    this.mRoundScrollbarRenderer.drawRoundScrollbars(canvas, ((float) cache.scrollBar.getAlpha()) / 255.0f, bounds3);
                    if (invalidate) {
                        invalidate();
                    }
                } else {
                    canvas2 = canvas;
                }
            } else {
                return;
            }
        }
        canvas2 = canvas;
    }

    /* Access modifiers changed, original: protected */
    public boolean isVerticalScrollBarHidden() {
        return false;
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public void onDrawHorizontalScrollBar(Canvas canvas, Drawable scrollBar, int l, int t, int r, int b) {
        scrollBar.setBounds(l, t, r, b);
        scrollBar.draw(canvas);
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public void onDrawVerticalScrollBar(Canvas canvas, Drawable scrollBar, int l, int t, int r, int b) {
        scrollBar.setBounds(l, t, r, b);
        scrollBar.draw(canvas);
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void assignParent(ViewParent parent) {
        if (this.mParent == null) {
            this.mParent = parent;
        } else if (parent == null) {
            this.mParent = null;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("view ");
            stringBuilder.append(this);
            stringBuilder.append(" being added, but it already has a parent");
            throw new RuntimeException(stringBuilder.toString());
        }
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        if ((this.mPrivateFlags & 512) != 0) {
            this.mParent.requestTransparentRegion(this);
        }
        this.mPrivateFlags3 &= -5;
        jumpDrawablesToCurrentState();
        AccessibilityNodeIdManager.getInstance().registerViewWithId(this, getAccessibilityViewId());
        resetSubtreeAccessibilityStateChanged();
        rebuildOutline();
        if (isFocused()) {
            notifyFocusChangeToInputMethodManager(true);
        }
    }

    public boolean resolveRtlPropertiesIfNeeded() {
        if (!needRtlPropertiesResolution()) {
            return false;
        }
        if (!isLayoutDirectionResolved()) {
            resolveLayoutDirection();
            resolveLayoutParams();
        }
        if (!isTextDirectionResolved()) {
            resolveTextDirection();
        }
        if (!isTextAlignmentResolved()) {
            resolveTextAlignment();
        }
        if (!areDrawablesResolved()) {
            resolveDrawables();
        }
        if (!isPaddingResolved()) {
            resolvePadding();
        }
        onRtlPropertiesChanged(getLayoutDirection());
        return true;
    }

    public void resetRtlProperties() {
        resetResolvedLayoutDirection();
        resetResolvedTextDirection();
        resetResolvedTextAlignment();
        resetResolvedPadding();
        resetResolvedDrawables();
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchScreenStateChanged(int screenState) {
        onScreenStateChanged(screenState);
    }

    public void onScreenStateChanged(int screenState) {
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchMovedToDisplay(Display display, Configuration config) {
        AttachInfo attachInfo = this.mAttachInfo;
        attachInfo.mDisplay = display;
        attachInfo.mDisplayState = display.getState();
        onMovedToDisplay(display.getDisplayId(), config);
    }

    public void onMovedToDisplay(int displayId, Configuration config) {
    }

    @UnsupportedAppUsage
    private boolean hasRtlSupport() {
        return this.mContext.getApplicationInfo().hasRtlSupport();
    }

    private boolean isRtlCompatibilityMode() {
        return getContext().getApplicationInfo().targetSdkVersion < 17 || !hasRtlSupport();
    }

    private boolean needRtlPropertiesResolution() {
        return (this.mPrivateFlags2 & ALL_RTL_PROPERTIES_RESOLVED) != ALL_RTL_PROPERTIES_RESOLVED;
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
    }

    public boolean resolveLayoutDirection() {
        this.mPrivateFlags2 &= -49;
        if (hasRtlSupport()) {
            int i = this.mPrivateFlags2;
            int i2 = (i & 12) >> 2;
            if (i2 == 1) {
                this.mPrivateFlags2 = i | 16;
            } else if (i2 != 2) {
                if (i2 == 3 && 1 == TextUtils.getLayoutDirectionFromLocale(Locale.getDefault())) {
                    this.mPrivateFlags2 |= 16;
                }
            } else if (!canResolveLayoutDirection()) {
                return false;
            } else {
                try {
                    if (!this.mParent.isLayoutDirectionResolved()) {
                        return false;
                    }
                    if (this.mParent.getLayoutDirection() == 1) {
                        this.mPrivateFlags2 |= 16;
                    }
                } catch (AbstractMethodError e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(this.mParent.getClass().getSimpleName());
                    stringBuilder.append(" does not fully implement ViewParent");
                    Log.e(VIEW_LOG_TAG, stringBuilder.toString(), e);
                }
            }
        }
        this.mPrivateFlags2 |= 32;
        return true;
    }

    public boolean canResolveLayoutDirection() {
        if (getRawLayoutDirection() != 2) {
            return true;
        }
        ViewParent viewParent = this.mParent;
        if (viewParent != null) {
            try {
                return viewParent.canResolveLayoutDirection();
            } catch (AbstractMethodError e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.mParent.getClass().getSimpleName());
                stringBuilder.append(" does not fully implement ViewParent");
                Log.e(VIEW_LOG_TAG, stringBuilder.toString(), e);
            }
        }
        return false;
    }

    public void resetResolvedLayoutDirection() {
        this.mPrivateFlags2 &= -49;
    }

    public boolean isLayoutDirectionInherited() {
        return getRawLayoutDirection() == 2;
    }

    public boolean isLayoutDirectionResolved() {
        return (this.mPrivateFlags2 & 32) == 32;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public boolean isPaddingResolved() {
        return (this.mPrivateFlags2 & 536870912) == 536870912;
    }

    @UnsupportedAppUsage
    public void resolvePadding() {
        int resolvedLayoutDirection = getLayoutDirection();
        if (!isRtlCompatibilityMode()) {
            int i;
            if (!(this.mBackground == null || (this.mLeftPaddingDefined && this.mRightPaddingDefined))) {
                Rect padding = (Rect) sThreadLocal.get();
                if (padding == null) {
                    padding = new Rect();
                    sThreadLocal.set(padding);
                }
                this.mBackground.getPadding(padding);
                if (!this.mLeftPaddingDefined) {
                    this.mUserPaddingLeftInitial = padding.left;
                }
                if (!this.mRightPaddingDefined) {
                    this.mUserPaddingRightInitial = padding.right;
                }
            }
            if (resolvedLayoutDirection != 1) {
                i = this.mUserPaddingStart;
                if (i != Integer.MIN_VALUE) {
                    this.mUserPaddingLeft = i;
                } else {
                    this.mUserPaddingLeft = this.mUserPaddingLeftInitial;
                }
                i = this.mUserPaddingEnd;
                if (i != Integer.MIN_VALUE) {
                    this.mUserPaddingRight = i;
                } else {
                    this.mUserPaddingRight = this.mUserPaddingRightInitial;
                }
            } else {
                i = this.mUserPaddingStart;
                if (i != Integer.MIN_VALUE) {
                    this.mUserPaddingRight = i;
                } else {
                    this.mUserPaddingRight = this.mUserPaddingRightInitial;
                }
                i = this.mUserPaddingEnd;
                if (i != Integer.MIN_VALUE) {
                    this.mUserPaddingLeft = i;
                } else {
                    this.mUserPaddingLeft = this.mUserPaddingLeftInitial;
                }
            }
            i = this.mUserPaddingBottom;
            if (i < 0) {
                i = this.mPaddingBottom;
            }
            this.mUserPaddingBottom = i;
        }
        internalSetPadding(this.mUserPaddingLeft, this.mPaddingTop, this.mUserPaddingRight, this.mUserPaddingBottom);
        onRtlPropertiesChanged(resolvedLayoutDirection);
        this.mPrivateFlags2 |= 536870912;
    }

    public void resetResolvedPadding() {
        resetResolvedPaddingInternal();
    }

    /* Access modifiers changed, original: 0000 */
    public void resetResolvedPaddingInternal() {
        this.mPrivateFlags2 &= -536870913;
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public void onDetachedFromWindowInternal() {
        this.mPrivateFlags &= -67108865;
        this.mPrivateFlags3 &= -5;
        this.mPrivateFlags3 &= -33554433;
        removeUnsetPressCallback();
        removeLongPressCallback();
        removePerformClickCallback();
        cancel(this.mSendViewScrolledAccessibilityEvent);
        stopNestedScroll();
        jumpDrawablesToCurrentState();
        destroyDrawingCache();
        cleanupDraw();
        this.mCurrentAnimation = null;
        if ((this.mViewFlags & 1073741824) == 1073741824) {
            hideTooltip();
        }
        AccessibilityNodeIdManager.getInstance().unregisterViewWithId(getAccessibilityViewId());
    }

    private void cleanupDraw() {
        resetDisplayList();
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            attachInfo.mViewRootImpl.cancelInvalidate(this);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void invalidateInheritedLayoutMode(int layoutModeOfRoot) {
    }

    /* Access modifiers changed, original: protected */
    public int getWindowAttachCount() {
        return this.mWindowAttachCount;
    }

    public IBinder getWindowToken() {
        AttachInfo attachInfo = this.mAttachInfo;
        return attachInfo != null ? attachInfo.mWindowToken : null;
    }

    public WindowId getWindowId() {
        AttachInfo ai = this.mAttachInfo;
        if (ai == null) {
            return null;
        }
        if (ai.mWindowId == null) {
            try {
                ai.mIWindowId = ai.mSession.getWindowId(ai.mWindowToken);
                if (ai.mIWindowId != null) {
                    ai.mWindowId = new WindowId(ai.mIWindowId);
                }
            } catch (RemoteException e) {
            }
        }
        return ai.mWindowId;
    }

    public IBinder getApplicationWindowToken() {
        AttachInfo ai = this.mAttachInfo;
        if (ai == null) {
            return null;
        }
        IBinder appWindowToken = ai.mPanelParentWindowToken;
        if (appWindowToken == null) {
            appWindowToken = ai.mWindowToken;
        }
        return appWindowToken;
    }

    public Display getDisplay() {
        AttachInfo attachInfo = this.mAttachInfo;
        return attachInfo != null ? attachInfo.mDisplay : null;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public IWindowSession getWindowSession() {
        AttachInfo attachInfo = this.mAttachInfo;
        return attachInfo != null ? attachInfo.mSession : null;
    }

    /* Access modifiers changed, original: protected */
    public IWindow getWindow() {
        AttachInfo attachInfo = this.mAttachInfo;
        return attachInfo != null ? attachInfo.mWindow : null;
    }

    /* Access modifiers changed, original: 0000 */
    public int combineVisibility(int vis1, int vis2) {
        return Math.max(vis1, vis2);
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public void dispatchAttachedToWindow(AttachInfo info, int visibility) {
        this.mAttachInfo = info;
        notifyConfirmedWebView(true);
        ViewOverlay viewOverlay = this.mOverlay;
        if (viewOverlay != null) {
            viewOverlay.getOverlayView().dispatchAttachedToWindow(info, visibility);
        }
        this.mWindowAttachCount++;
        this.mPrivateFlags |= 1024;
        CopyOnWriteArrayList<OnAttachStateChangeListener> listeners = null;
        if (this.mFloatingTreeObserver != null) {
            info.mTreeObserver.merge(this.mFloatingTreeObserver);
            this.mFloatingTreeObserver = null;
        }
        registerPendingFrameMetricsObservers();
        if ((this.mPrivateFlags & 524288) != 0) {
            this.mAttachInfo.mScrollContainers.add(this);
            this.mPrivateFlags |= 1048576;
        }
        HandlerActionQueue handlerActionQueue = this.mRunQueue;
        if (handlerActionQueue != null) {
            handlerActionQueue.executeActions(info.mHandler);
            this.mRunQueue = null;
        }
        performCollectViewAttributes(this.mAttachInfo, visibility);
        onAttachedToWindow();
        ListenerInfo li = this.mListenerInfo;
        if (li != null) {
            listeners = li.mOnAttachStateChangeListeners;
        }
        if (listeners != null && listeners.size() > 0) {
            Iterator it = listeners.iterator();
            while (it.hasNext()) {
                ((OnAttachStateChangeListener) it.next()).onViewAttachedToWindow(this);
            }
        }
        int vis = info.mWindowVisibility;
        if (vis != 8) {
            onWindowVisibilityChanged(vis);
            if (isShown()) {
                onVisibilityAggregated(vis == 0);
            }
        }
        onVisibilityChanged(this, visibility);
        if ((this.mPrivateFlags & 1024) != 0) {
            refreshDrawableState();
        }
        needGlobalAttributesUpdate(false);
        notifyEnterOrExitForAutoFillIfNeeded(true);
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public void dispatchDetachedFromWindow() {
        notifyConfirmedWebView(false);
        AttachInfo info = this.mAttachInfo;
        if (!(info == null || info.mWindowVisibility == 8)) {
            onWindowVisibilityChanged(8);
            if (isShown()) {
                onVisibilityAggregated(false);
            }
        }
        onDetachedFromWindow();
        onDetachedFromWindowInternal();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(InputMethodManager.class);
        if (imm != null) {
            imm.onViewDetachedFromWindow(this);
        }
        ListenerInfo li = this.mListenerInfo;
        CopyOnWriteArrayList<OnAttachStateChangeListener> listeners = li != null ? li.mOnAttachStateChangeListeners : null;
        if (listeners != null && listeners.size() > 0) {
            Iterator it = listeners.iterator();
            while (it.hasNext()) {
                ((OnAttachStateChangeListener) it.next()).onViewDetachedFromWindow(this);
            }
        }
        if ((this.mPrivateFlags & 1048576) != 0) {
            this.mAttachInfo.mScrollContainers.remove(this);
            this.mPrivateFlags &= -1048577;
        }
        this.mAttachInfo = null;
        ViewOverlay viewOverlay = this.mOverlay;
        if (viewOverlay != null) {
            viewOverlay.getOverlayView().dispatchDetachedFromWindow();
        }
        notifyEnterOrExitForAutoFillIfNeeded(false);
    }

    public final void cancelPendingInputEvents() {
        dispatchCancelPendingInputEvents();
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchCancelPendingInputEvents() {
        this.mPrivateFlags3 &= -17;
        onCancelPendingInputEvents();
        if ((this.mPrivateFlags3 & 16) != 16) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("View ");
            stringBuilder.append(getClass().getSimpleName());
            stringBuilder.append(" did not call through to super.onCancelPendingInputEvents()");
            throw new SuperNotCalledException(stringBuilder.toString());
        }
    }

    public void onCancelPendingInputEvents() {
        removePerformClickCallback();
        cancelLongPress();
        this.mPrivateFlags3 |= 16;
    }

    public void saveHierarchyState(SparseArray<Parcelable> container) {
        dispatchSaveInstanceState(container);
    }

    /* Access modifiers changed, original: protected */
    public void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        if (this.mID != -1 && (this.mViewFlags & 65536) == 0) {
            this.mPrivateFlags &= -131073;
            Parcelable state = onSaveInstanceState();
            if ((this.mPrivateFlags & 131072) == 0) {
                throw new IllegalStateException("Derived class did not call super.onSaveInstanceState()");
            } else if (state != null) {
                container.put(this.mID, state);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public Parcelable onSaveInstanceState() {
        this.mPrivateFlags |= 131072;
        if (this.mStartActivityRequestWho == null && !isAutofilled() && this.mAutofillViewId <= LAST_APP_AUTOFILL_ID) {
            return BaseSavedState.EMPTY_STATE;
        }
        BaseSavedState state = new BaseSavedState(AbsSavedState.EMPTY_STATE);
        if (this.mStartActivityRequestWho != null) {
            state.mSavedData |= 1;
        }
        if (isAutofilled()) {
            state.mSavedData |= 2;
        }
        if (this.mAutofillViewId > LAST_APP_AUTOFILL_ID) {
            state.mSavedData |= 4;
        }
        state.mStartActivityRequestWhoSaved = this.mStartActivityRequestWho;
        state.mIsAutofilled = isAutofilled();
        state.mAutofillViewId = this.mAutofillViewId;
        return state;
    }

    public void restoreHierarchyState(SparseArray<Parcelable> container) {
        dispatchRestoreInstanceState(container);
    }

    /* Access modifiers changed, original: protected */
    public void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        int i = this.mID;
        if (i != -1) {
            Parcelable state = (Parcelable) container.get(i);
            if (state != null) {
                this.mPrivateFlags &= -131073;
                onRestoreInstanceState(state);
                if ((this.mPrivateFlags & 131072) == 0) {
                    throw new IllegalStateException("Derived class did not call super.onRestoreInstanceState()");
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onRestoreInstanceState(Parcelable state) {
        this.mPrivateFlags |= 131072;
        StringBuilder stringBuilder;
        if (state != null && !(state instanceof AbsSavedState)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Wrong state class, expecting View State but received ");
            stringBuilder.append(state.getClass().toString());
            stringBuilder.append(" instead. This usually happens when two views of different type have the same id in the same hierarchy. This view's id is ");
            stringBuilder.append(ViewDebug.resolveId(this.mContext, getId()));
            stringBuilder.append(". Make sure other views do not use the same id.");
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (state != null && (state instanceof BaseSavedState)) {
            BaseSavedState baseState = (BaseSavedState) state;
            if ((baseState.mSavedData & 1) != 0) {
                this.mStartActivityRequestWho = baseState.mStartActivityRequestWhoSaved;
            }
            if ((baseState.mSavedData & 2) != 0) {
                setAutofilled(baseState.mIsAutofilled);
            }
            if ((baseState.mSavedData & 4) != 0) {
                BaseSavedState baseSavedState = (BaseSavedState) state;
                baseSavedState.mSavedData &= -5;
                if ((this.mPrivateFlags3 & 1073741824) != 0) {
                    String str = AUTOFILL_LOG_TAG;
                    if (Log.isLoggable(str, 3)) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("onRestoreInstanceState(): not setting autofillId to ");
                        stringBuilder.append(baseState.mAutofillViewId);
                        stringBuilder.append(" because view explicitly set it to ");
                        stringBuilder.append(this.mAutofillId);
                        Log.d(str, stringBuilder.toString());
                        return;
                    }
                    return;
                }
                this.mAutofillViewId = baseState.mAutofillViewId;
                this.mAutofillId = null;
            }
        }
    }

    public long getDrawingTime() {
        AttachInfo attachInfo = this.mAttachInfo;
        return attachInfo != null ? attachInfo.mDrawingTime : 0;
    }

    public void setDuplicateParentStateEnabled(boolean enabled) {
        setFlags(enabled ? 4194304 : 0, 4194304);
    }

    public boolean isDuplicateParentStateEnabled() {
        return (this.mViewFlags & 4194304) == 4194304;
    }

    public void setLayerType(int layerType, Paint paint) {
        if (layerType < 0 || layerType > 2) {
            throw new IllegalArgumentException("Layer type can only be one of: LAYER_TYPE_NONE, LAYER_TYPE_SOFTWARE or LAYER_TYPE_HARDWARE");
        } else if (this.mRenderNode.setLayerType(layerType)) {
            if (layerType != 1) {
                destroyDrawingCache();
            }
            this.mLayerType = layerType;
            this.mLayerPaint = this.mLayerType == 0 ? null : paint;
            this.mRenderNode.setLayerPaint(this.mLayerPaint);
            invalidateParentCaches();
            invalidate(true);
        } else {
            setLayerPaint(paint);
        }
    }

    public void setLayerPaint(Paint paint) {
        int layerType = getLayerType();
        if (layerType != 0) {
            this.mLayerPaint = paint;
            if (layerType != 2) {
                invalidate();
            } else if (this.mRenderNode.setLayerPaint(paint)) {
                invalidateViewProperty(false, false);
            }
        }
    }

    public int getLayerType() {
        return this.mLayerType;
    }

    public void buildLayer() {
        if (this.mLayerType != 0) {
            AttachInfo attachInfo = this.mAttachInfo;
            if (attachInfo == null) {
                throw new IllegalStateException("This view must be attached to a window first");
            } else if (getWidth() != 0 && getHeight() != 0) {
                int i = this.mLayerType;
                if (i == 1) {
                    buildDrawingCache(true);
                } else if (i == 2) {
                    updateDisplayListIfDirty();
                    if (attachInfo.mThreadedRenderer != null && this.mRenderNode.hasDisplayList()) {
                        attachInfo.mThreadedRenderer.buildLayer(this.mRenderNode);
                    }
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public void destroyHardwareResources() {
        ViewOverlay viewOverlay = this.mOverlay;
        if (viewOverlay != null) {
            viewOverlay.getOverlayView().destroyHardwareResources();
        }
        GhostView ghostView = this.mGhostView;
        if (ghostView != null) {
            ghostView.destroyHardwareResources();
        }
    }

    @Deprecated
    public void setDrawingCacheEnabled(boolean enabled) {
        int i = 0;
        this.mCachingFailed = false;
        if (enabled) {
            i = 32768;
        }
        setFlags(i, 32768);
    }

    @ExportedProperty(category = "drawing")
    @Deprecated
    public boolean isDrawingCacheEnabled() {
        return (this.mViewFlags & 32768) == 32768;
    }

    public void outputDirtyFlags(String indent, boolean clear, int clearMask) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(indent);
        stringBuilder.append(this);
        stringBuilder.append("             DIRTY(");
        stringBuilder.append(this.mPrivateFlags & 2097152);
        stringBuilder.append(") DRAWN(");
        stringBuilder.append(this.mPrivateFlags & 32);
        stringBuilder.append(") CACHE_VALID(");
        stringBuilder.append(this.mPrivateFlags & 32768);
        stringBuilder.append(") INVALIDATED(");
        stringBuilder.append(this.mPrivateFlags & Integer.MIN_VALUE);
        stringBuilder.append(")");
        Log.d(VIEW_LOG_TAG, stringBuilder.toString());
        if (clear) {
            this.mPrivateFlags &= clearMask;
        }
        if (this instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) this;
            int count = parent.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = parent.getChildAt(i);
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(indent);
                stringBuilder2.append("  ");
                child.outputDirtyFlags(stringBuilder2.toString(), clear, clearMask);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void dispatchGetDisplayList() {
    }

    public boolean canHaveDisplayList() {
        AttachInfo attachInfo = this.mAttachInfo;
        return (attachInfo == null || attachInfo.mThreadedRenderer == null) ? false : true;
    }

    @UnsupportedAppUsage
    public RenderNode updateDisplayListIfDirty() {
        RenderNode renderNode = this.mRenderNode;
        if (!canHaveDisplayList()) {
            return renderNode;
        }
        if ((this.mPrivateFlags & 32768) != 0 && renderNode.hasDisplayList() && !this.mRecreateDisplayList) {
            this.mPrivateFlags |= 32800;
            this.mPrivateFlags &= -2097153;
        } else if (!renderNode.hasDisplayList() || this.mRecreateDisplayList) {
            this.mRecreateDisplayList = true;
            int width = this.mRight - this.mLeft;
            int height = this.mBottom - this.mTop;
            int layerType = getLayerType();
            RecordingCanvas canvas = renderNode.beginRecording(width, height);
            if (layerType == 1) {
                try {
                    buildDrawingCache(true);
                    Bitmap cache = getDrawingCache(true);
                    if (cache != null) {
                        canvas.drawBitmap(cache, 0.0f, 0.0f, this.mLayerPaint);
                    }
                } catch (Throwable th) {
                    renderNode.endRecording();
                    setDisplayListProperties(renderNode);
                }
            } else {
                computeScroll();
                canvas.translate((float) (-this.mScrollX), (float) (-this.mScrollY));
                this.mPrivateFlags |= 32800;
                this.mPrivateFlags &= -2097153;
                if ((this.mPrivateFlags & 128) == 128) {
                    dispatchDraw(canvas);
                    drawAutofilledHighlight(canvas);
                    if (!(this.mOverlay == null || this.mOverlay.isEmpty())) {
                        this.mOverlay.getOverlayView().draw(canvas);
                    }
                    if (debugDraw()) {
                        debugDrawFocus(canvas);
                    }
                } else {
                    draw(canvas);
                }
            }
            renderNode.endRecording();
            setDisplayListProperties(renderNode);
        } else {
            this.mPrivateFlags |= 32800;
            this.mPrivateFlags &= -2097153;
            dispatchGetDisplayList();
            return renderNode;
        }
        return renderNode;
    }

    @UnsupportedAppUsage
    private void resetDisplayList() {
        this.mRenderNode.discardDisplayList();
        RenderNode renderNode = this.mBackgroundRenderNode;
        if (renderNode != null) {
            renderNode.discardDisplayList();
        }
    }

    @Deprecated
    public Bitmap getDrawingCache() {
        return getDrawingCache(false);
    }

    @Deprecated
    public Bitmap getDrawingCache(boolean autoScale) {
        int i = this.mViewFlags;
        if ((i & 131072) == 131072) {
            return null;
        }
        if ((i & 32768) == 32768) {
            buildDrawingCache(autoScale);
        }
        return autoScale ? this.mDrawingCache : this.mUnscaledDrawingCache;
    }

    @Deprecated
    public void destroyDrawingCache() {
        Bitmap bitmap = this.mDrawingCache;
        if (bitmap != null) {
            bitmap.recycle();
            this.mDrawingCache = null;
        }
        bitmap = this.mUnscaledDrawingCache;
        if (bitmap != null) {
            bitmap.recycle();
            this.mUnscaledDrawingCache = null;
        }
    }

    @Deprecated
    public void setDrawingCacheBackgroundColor(int color) {
        if (color != this.mDrawingCacheBackgroundColor) {
            this.mDrawingCacheBackgroundColor = color;
            this.mPrivateFlags &= -32769;
        }
    }

    @Deprecated
    public int getDrawingCacheBackgroundColor() {
        return this.mDrawingCacheBackgroundColor;
    }

    @Deprecated
    public void buildDrawingCache() {
        buildDrawingCache(false);
    }

    @Deprecated
    public void buildDrawingCache(boolean autoScale) {
        if ((this.mPrivateFlags & 32768) != 0) {
            if (autoScale) {
                if (this.mDrawingCache != null) {
                    return;
                }
            } else if (this.mUnscaledDrawingCache != null) {
                return;
            }
        }
        if (Trace.isTagEnabled(8)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("buildDrawingCache/SW Layer for ");
            stringBuilder.append(getClass().getSimpleName());
            Trace.traceBegin(8, stringBuilder.toString());
        }
        try {
            buildDrawingCacheImpl(autoScale);
        } finally {
            Trace.traceEnd(8);
        }
    }

    private void buildDrawingCacheImpl(boolean autoScale) {
        this.mCachingFailed = false;
        int width = this.mRight - this.mLeft;
        int height = this.mBottom - this.mTop;
        AttachInfo attachInfo = this.mAttachInfo;
        boolean scalingRequired = attachInfo != null && attachInfo.mScalingRequired;
        if (autoScale && scalingRequired) {
            width = (int) ((((float) width) * attachInfo.mApplicationScale) + 0.5f);
            height = (int) ((((float) height) * attachInfo.mApplicationScale) + 0.5f);
        }
        int drawingCacheBackgroundColor = this.mDrawingCacheBackgroundColor;
        boolean opaque = drawingCacheBackgroundColor != 0 || isOpaque();
        boolean use32BitCache = attachInfo != null && attachInfo.mUse32BitDrawingCache;
        int i = width * height;
        int i2 = (!opaque || use32BitCache) ? 4 : 2;
        long projectedBitmapSize = (long) (i * i2);
        long drawingCacheSize = (long) ViewConfiguration.get(this.mContext).getScaledMaximumDrawingCacheSize();
        int i3;
        boolean z;
        boolean z2;
        if (width <= 0 || height <= 0) {
            i3 = drawingCacheBackgroundColor;
            z = opaque;
        } else if (projectedBitmapSize > drawingCacheSize) {
            z2 = scalingRequired;
            i3 = drawingCacheBackgroundColor;
            z = opaque;
        } else {
            Config quality;
            Canvas canvas;
            boolean clear = true;
            Bitmap bitmap = autoScale ? this.mDrawingCache : this.mUnscaledDrawingCache;
            if (!(bitmap != null && bitmap.getWidth() == width && bitmap.getHeight() == height)) {
                if (opaque) {
                    quality = use32BitCache ? Config.ARGB_8888 : Config.RGB_565;
                } else {
                    int i4 = this.mViewFlags;
                    quality = Config.ARGB_8888;
                }
                if (bitmap != null) {
                    bitmap.recycle();
                }
                try {
                    boolean z3;
                    bitmap = Bitmap.createBitmap(this.mResources.getDisplayMetrics(), width, height, quality);
                    bitmap.setDensity(getResources().getDisplayMetrics().densityDpi);
                    if (autoScale) {
                        try {
                            this.mDrawingCache = bitmap;
                        } catch (OutOfMemoryError e) {
                            z2 = scalingRequired;
                            i3 = drawingCacheBackgroundColor;
                            z = opaque;
                        }
                    } else {
                        this.mUnscaledDrawingCache = bitmap;
                    }
                    if (opaque && use32BitCache) {
                        z3 = false;
                        bitmap.setHasAlpha(false);
                    } else {
                        z3 = false;
                    }
                    if (drawingCacheBackgroundColor != 0) {
                        z3 = true;
                    }
                    clear = z3;
                } catch (OutOfMemoryError e2) {
                    z2 = scalingRequired;
                    i3 = drawingCacheBackgroundColor;
                    z = opaque;
                    if (autoScale) {
                        this.mDrawingCache = null;
                    } else {
                        this.mUnscaledDrawingCache = null;
                    }
                    this.mCachingFailed = true;
                    return;
                }
            }
            if (attachInfo != null) {
                canvas = attachInfo.mCanvas;
                if (canvas == null) {
                    canvas = new Canvas();
                }
                canvas.setBitmap(bitmap);
                attachInfo.mCanvas = null;
            } else {
                canvas = new Canvas(bitmap);
            }
            if (clear) {
                bitmap.eraseColor(drawingCacheBackgroundColor);
            }
            computeScroll();
            quality = canvas.save();
            if (autoScale && scalingRequired) {
                scalingRequired = attachInfo.mApplicationScale;
                canvas.scale(scalingRequired, scalingRequired);
            }
            canvas.translate((float) (-this.mScrollX), (float) (-this.mScrollY));
            this.mPrivateFlags |= 32;
            AttachInfo attachInfo2 = this.mAttachInfo;
            if (!(attachInfo2 != null && attachInfo2.mHardwareAccelerated && this.mLayerType == 0)) {
                this.mPrivateFlags |= 32768;
            }
            int i5 = this.mPrivateFlags;
            if ((i5 & 128) == 128) {
                this.mPrivateFlags = i5 & -2097153;
                dispatchDraw(canvas);
                drawAutofilledHighlight(canvas);
                ViewOverlay viewOverlay = this.mOverlay;
                if (!(viewOverlay == null || viewOverlay.isEmpty())) {
                    this.mOverlay.getOverlayView().draw(canvas);
                }
            } else {
                draw(canvas);
            }
            canvas.restoreToCount(quality);
            canvas.setBitmap(null);
            if (attachInfo != null) {
                attachInfo.mCanvas = canvas;
            }
            return;
        }
        if (width > 0 && height > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getClass().getSimpleName());
            stringBuilder.append(" not displayed because it is too large to fit into a software layer (or drawing cache), needs ");
            stringBuilder.append(projectedBitmapSize);
            stringBuilder.append(" bytes, only ");
            stringBuilder.append(drawingCacheSize);
            stringBuilder.append(" available");
            Log.w(VIEW_LOG_TAG, stringBuilder.toString());
        }
        destroyDrawingCache();
        this.mCachingFailed = true;
    }

    @UnsupportedAppUsage
    public Bitmap createSnapshot(CanvasProvider canvasProvider, boolean skipChildren) {
        int width = this.mRight - this.mLeft;
        int height = this.mBottom - this.mTop;
        AttachInfo attachInfo = this.mAttachInfo;
        float scale = attachInfo != null ? attachInfo.mApplicationScale : 1.0f;
        width = (int) ((((float) width) * scale) + 0.5f);
        height = (int) ((((float) height) * scale) + 0.5f);
        Canvas oldCanvas = null;
        Canvas canvas = true;
        int i = width > 0 ? width : 1;
        if (height > 0) {
            canvas = height;
        }
        try {
            canvas = canvasProvider.getCanvas(this, i, canvas);
            if (attachInfo != null) {
                oldCanvas = attachInfo.mCanvas;
                attachInfo.mCanvas = null;
            }
            computeScroll();
            i = canvas.save();
            canvas.scale(scale, scale);
            canvas.translate((float) (-this.mScrollX), (float) (-this.mScrollY));
            int flags = this.mPrivateFlags;
            this.mPrivateFlags &= -2097153;
            if ((this.mPrivateFlags & 128) == 128) {
                dispatchDraw(canvas);
                drawAutofilledHighlight(canvas);
                if (!(this.mOverlay == null || this.mOverlay.isEmpty())) {
                    this.mOverlay.getOverlayView().draw(canvas);
                }
            } else {
                draw(canvas);
            }
            this.mPrivateFlags = flags;
            canvas.restoreToCount(i);
            Bitmap createBitmap = canvasProvider.createBitmap();
            return createBitmap;
        } finally {
            if (oldCanvas != null) {
                attachInfo.mCanvas = oldCanvas;
            }
        }
    }

    public boolean isInEditMode() {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean isPaddingOffsetRequired() {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public int getLeftPaddingOffset() {
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public int getRightPaddingOffset() {
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public int getTopPaddingOffset() {
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public int getBottomPaddingOffset() {
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public int getFadeTop(boolean offsetRequired) {
        int top = this.mPaddingTop;
        if (offsetRequired) {
            return top + getTopPaddingOffset();
        }
        return top;
    }

    /* Access modifiers changed, original: protected */
    public int getFadeHeight(boolean offsetRequired) {
        int padding = this.mPaddingTop;
        if (offsetRequired) {
            padding += getTopPaddingOffset();
        }
        return ((this.mBottom - this.mTop) - this.mPaddingBottom) - padding;
    }

    @ExportedProperty(category = "drawing")
    public boolean isHardwareAccelerated() {
        AttachInfo attachInfo = this.mAttachInfo;
        return attachInfo != null && attachInfo.mHardwareAccelerated;
    }

    public void setClipBounds(Rect clipBounds) {
        Rect rect = this.mClipBounds;
        if (clipBounds != rect && (clipBounds == null || !clipBounds.equals(rect))) {
            if (clipBounds != null) {
                rect = this.mClipBounds;
                if (rect == null) {
                    this.mClipBounds = new Rect(clipBounds);
                } else {
                    rect.set(clipBounds);
                }
            } else {
                this.mClipBounds = null;
            }
            this.mRenderNode.setClipRect(this.mClipBounds);
            invalidateViewProperty(false, false);
        }
    }

    public Rect getClipBounds() {
        Rect rect = this.mClipBounds;
        return rect != null ? new Rect(rect) : null;
    }

    public boolean getClipBounds(Rect outRect) {
        Rect rect = this.mClipBounds;
        if (rect == null) {
            return false;
        }
        outRect.set(rect);
        return true;
    }

    private boolean applyLegacyAnimation(ViewGroup parent, long drawingTime, Animation a, boolean scalingRequired) {
        Transformation invalidationTransform;
        ViewGroup viewGroup = parent;
        long j = drawingTime;
        Animation animation = a;
        int flags = viewGroup.mGroupFlags;
        if (!a.isInitialized()) {
            animation.initialize(this.mRight - this.mLeft, this.mBottom - this.mTop, parent.getWidth(), parent.getHeight());
            animation.initializeInvalidateRegion(0, 0, this.mRight - this.mLeft, this.mBottom - this.mTop);
            AttachInfo attachInfo = this.mAttachInfo;
            if (attachInfo != null) {
                animation.setListenerHandler(attachInfo.mHandler);
            }
            onAnimationStart();
        }
        Transformation t = parent.getChildTransformation();
        boolean more = animation.getTransformation(j, t, 1.0f);
        if (!scalingRequired || this.mAttachInfo.mApplicationScale == 1.0f) {
            invalidationTransform = t;
        } else {
            if (viewGroup.mInvalidationTransformation == null) {
                viewGroup.mInvalidationTransformation = new Transformation();
            }
            Transformation invalidationTransform2 = viewGroup.mInvalidationTransformation;
            animation.getTransformation(j, invalidationTransform2, 1.0f);
            invalidationTransform = invalidationTransform2;
        }
        if (more) {
            if (a.willChangeBounds()) {
                if (viewGroup.mInvalidateRegion == null) {
                    viewGroup.mInvalidateRegion = new RectF();
                }
                RectF region = viewGroup.mInvalidateRegion;
                RectF region2 = region;
                a.getInvalidateRegion(0, 0, this.mRight - this.mLeft, this.mBottom - this.mTop, region, invalidationTransform);
                viewGroup.mPrivateFlags |= 64;
                int left = this.mLeft + ((int) region2.left);
                int top = this.mTop + ((int) region2.top);
                viewGroup.invalidate(left, top, ((int) (region2.width() + 0.5f)) + left, ((int) (region2.height() + 0.5f)) + top);
            } else if ((flags & 144) == 128) {
                viewGroup.mGroupFlags |= 4;
            } else if ((flags & 4) == 0) {
                viewGroup.mPrivateFlags |= 64;
                viewGroup.invalidate(this.mLeft, this.mTop, this.mRight, this.mBottom);
            }
        }
        return more;
    }

    /* Access modifiers changed, original: 0000 */
    public void setDisplayListProperties(RenderNode renderNode) {
        if (renderNode != null) {
            renderNode.setHasOverlappingRendering(getHasOverlappingRendering());
            ViewParent viewParent = this.mParent;
            boolean z = (viewParent instanceof ViewGroup) && ((ViewGroup) viewParent).getClipChildren();
            renderNode.setClipToBounds(z);
            float alpha = 1.0f;
            ViewParent viewParent2 = this.mParent;
            if ((viewParent2 instanceof ViewGroup) && (((ViewGroup) viewParent2).mGroupFlags & 2048) != 0) {
                ViewGroup parentVG = this.mParent;
                Transformation t = parentVG.getChildTransformation();
                if (parentVG.getChildStaticTransformation(this, t)) {
                    int transformType = t.getTransformationType();
                    if (transformType != 0) {
                        if ((transformType & 1) != 0) {
                            alpha = t.getAlpha();
                        }
                        if ((transformType & 2) != 0) {
                            renderNode.setStaticMatrix(t.getMatrix());
                        }
                    }
                }
            }
            if (this.mTransformationInfo != null) {
                alpha *= getFinalAlpha();
                if (alpha < 1.0f && onSetAlpha((int) (1132396544 * alpha))) {
                    alpha = 1.0f;
                }
                renderNode.setAlpha(alpha);
            } else if (alpha < 1.0f) {
                renderNode.setAlpha(alpha);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x017c  */
    /* JADX WARNING: Removed duplicated region for block: B:96:0x0179  */
    /* JADX WARNING: Removed duplicated region for block: B:116:0x01d1  */
    /* JADX WARNING: Removed duplicated region for block: B:115:0x01ce  */
    /* JADX WARNING: Removed duplicated region for block: B:127:0x0217  */
    /* JADX WARNING: Removed duplicated region for block: B:118:0x01d5  */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x0258  */
    /* JADX WARNING: Removed duplicated region for block: B:139:0x0251  */
    /* JADX WARNING: Removed duplicated region for block: B:151:0x02e3  */
    /* JADX WARNING: Removed duplicated region for block: B:143:0x0266  */
    /* JADX WARNING: Removed duplicated region for block: B:154:0x02f4  */
    /* JADX WARNING: Removed duplicated region for block: B:176:0x0358  */
    /* JADX WARNING: Removed duplicated region for block: B:170:0x0337  */
    /* JADX WARNING: Removed duplicated region for block: B:193:0x03a7  */
    /* JADX WARNING: Removed duplicated region for block: B:192:0x03a1  */
    public boolean draw(android.graphics.Canvas r32, android.view.ViewGroup r33, long r34) {
        /*
        r31 = this;
        r6 = r31;
        r7 = r32;
        r8 = r33;
        r9 = r32.isHardwareAccelerated();
        r0 = r6.mAttachInfo;
        r11 = 1;
        if (r0 == 0) goto L_0x0017;
    L_0x000f:
        r0 = r0.mHardwareAccelerated;
        if (r0 == 0) goto L_0x0017;
    L_0x0013:
        if (r9 == 0) goto L_0x0017;
    L_0x0015:
        r0 = r11;
        goto L_0x0018;
    L_0x0017:
        r0 = 0;
    L_0x0018:
        r12 = r0;
        r13 = 0;
        r14 = r31.hasIdentityMatrix();
        r15 = r8.mGroupFlags;
        r0 = r15 & 256;
        if (r0 == 0) goto L_0x0031;
    L_0x0024:
        r0 = r33.getChildTransformation();
        r0.clear();
        r0 = r8.mGroupFlags;
        r0 = r0 & -257;
        r8.mGroupFlags = r0;
    L_0x0031:
        r16 = 0;
        r17 = 0;
        r0 = r6.mAttachInfo;
        if (r0 == 0) goto L_0x003f;
    L_0x0039:
        r0 = r0.mScalingRequired;
        if (r0 == 0) goto L_0x003f;
    L_0x003d:
        r0 = r11;
        goto L_0x0040;
    L_0x003f:
        r0 = 0;
    L_0x0040:
        r18 = r0;
        r5 = r31.getAnimation();
        if (r5 == 0) goto L_0x0067;
    L_0x0048:
        r0 = r31;
        r1 = r33;
        r2 = r34;
        r4 = r5;
        r19 = r5;
        r5 = r18;
        r13 = r0.applyLegacyAnimation(r1, r2, r4, r5);
        r17 = r19.willChangeTransformationMatrix();
        if (r17 == 0) goto L_0x0062;
    L_0x005d:
        r0 = r6.mPrivateFlags3;
        r0 = r0 | r11;
        r6.mPrivateFlags3 = r0;
    L_0x0062:
        r16 = r33.getChildTransformation();
        goto L_0x009c;
    L_0x0067:
        r19 = r5;
        r0 = r6.mPrivateFlags3;
        r0 = r0 & r11;
        r1 = 0;
        if (r0 == 0) goto L_0x007a;
    L_0x006f:
        r0 = r6.mRenderNode;
        r0.setAnimationMatrix(r1);
        r0 = r6.mPrivateFlags3;
        r0 = r0 & -2;
        r6.mPrivateFlags3 = r0;
    L_0x007a:
        if (r12 != 0) goto L_0x009c;
    L_0x007c:
        r0 = r15 & 2048;
        if (r0 == 0) goto L_0x009c;
    L_0x0080:
        r0 = r33.getChildTransformation();
        r2 = r8.getChildStaticTransformation(r6, r0);
        if (r2 == 0) goto L_0x009c;
    L_0x008a:
        r3 = r0.getTransformationType();
        if (r3 == 0) goto L_0x0091;
    L_0x0090:
        r1 = r0;
    L_0x0091:
        r16 = r1;
        r1 = r3 & 2;
        if (r1 == 0) goto L_0x0099;
    L_0x0097:
        r1 = r11;
        goto L_0x009a;
    L_0x0099:
        r1 = 0;
    L_0x009a:
        r17 = r1;
    L_0x009c:
        r0 = r14 ^ 1;
        r17 = r17 | r0;
        r0 = r6.mPrivateFlags;
        r0 = r0 | 32;
        r6.mPrivateFlags = r0;
        if (r17 != 0) goto L_0x00d0;
    L_0x00a8:
        r0 = r15 & 2049;
        if (r0 != r11) goto L_0x00d0;
    L_0x00ac:
        r0 = r6.mLeft;
        r1 = (float) r0;
        r0 = r6.mTop;
        r2 = (float) r0;
        r0 = r6.mRight;
        r3 = (float) r0;
        r0 = r6.mBottom;
        r4 = (float) r0;
        r5 = android.graphics.Canvas.EdgeType.BW;
        r0 = r32;
        r0 = r0.quickReject(r1, r2, r3, r4, r5);
        if (r0 == 0) goto L_0x00d0;
    L_0x00c2:
        r0 = r6.mPrivateFlags;
        r0 = r0 & 64;
        if (r0 != 0) goto L_0x00d0;
    L_0x00c8:
        r0 = r6.mPrivateFlags2;
        r1 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
        r0 = r0 | r1;
        r6.mPrivateFlags2 = r0;
        return r13;
    L_0x00d0:
        r0 = r6.mPrivateFlags2;
        r1 = -268435457; // 0xffffffffefffffff float:-1.5845632E29 double:NaN;
        r0 = r0 & r1;
        r6.mPrivateFlags2 = r0;
        if (r9 == 0) goto L_0x00ee;
    L_0x00da:
        r0 = r6.mPrivateFlags;
        r1 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r0 = r0 & r1;
        if (r0 == 0) goto L_0x00e3;
    L_0x00e1:
        r0 = r11;
        goto L_0x00e4;
    L_0x00e3:
        r0 = 0;
    L_0x00e4:
        r6.mRecreateDisplayList = r0;
        r0 = r6.mPrivateFlags;
        r1 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r0 = r0 & r1;
        r6.mPrivateFlags = r0;
    L_0x00ee:
        r0 = 0;
        r1 = 0;
        r2 = r31.getLayerType();
        if (r2 == r11) goto L_0x00fd;
    L_0x00f6:
        if (r12 != 0) goto L_0x00f9;
    L_0x00f8:
        goto L_0x00fd;
    L_0x00f9:
        r5 = r1;
        r20 = r2;
        goto L_0x010a;
    L_0x00fd:
        if (r2 == 0) goto L_0x0103;
    L_0x00ff:
        r2 = 1;
        r6.buildDrawingCache(r11);
    L_0x0103:
        r1 = r6.getDrawingCache(r11);
        r5 = r1;
        r20 = r2;
    L_0x010a:
        if (r12 == 0) goto L_0x011c;
    L_0x010c:
        r0 = r31.updateDisplayListIfDirty();
        r1 = r0.hasDisplayList();
        if (r1 != 0) goto L_0x011a;
    L_0x0116:
        r0 = 0;
        r12 = 0;
        r4 = r0;
        goto L_0x011d;
    L_0x011a:
        r4 = r0;
        goto L_0x011d;
    L_0x011c:
        r4 = r0;
    L_0x011d:
        r0 = 0;
        r1 = 0;
        if (r12 != 0) goto L_0x012b;
    L_0x0121:
        r31.computeScroll();
        r0 = r6.mScrollX;
        r1 = r6.mScrollY;
        r3 = r0;
        r2 = r1;
        goto L_0x012d;
    L_0x012b:
        r3 = r0;
        r2 = r1;
    L_0x012d:
        if (r5 == 0) goto L_0x0133;
    L_0x012f:
        if (r12 != 0) goto L_0x0133;
    L_0x0131:
        r0 = r11;
        goto L_0x0134;
    L_0x0133:
        r0 = 0;
    L_0x0134:
        r21 = r0;
        if (r5 != 0) goto L_0x013c;
    L_0x0138:
        if (r12 != 0) goto L_0x013c;
    L_0x013a:
        r0 = r11;
        goto L_0x013d;
    L_0x013c:
        r0 = 0;
    L_0x013d:
        r22 = r0;
        r0 = -1;
        if (r12 == 0) goto L_0x0144;
    L_0x0142:
        if (r16 == 0) goto L_0x0148;
    L_0x0144:
        r0 = r32.save();
    L_0x0148:
        r23 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        if (r22 == 0) goto L_0x0158;
    L_0x014c:
        r1 = r6.mLeft;
        r1 = r1 - r3;
        r1 = (float) r1;
        r11 = r6.mTop;
        r11 = r11 - r2;
        r11 = (float) r11;
        r7.translate(r1, r11);
        goto L_0x0176;
    L_0x0158:
        if (r12 != 0) goto L_0x0163;
    L_0x015a:
        r1 = r6.mLeft;
        r1 = (float) r1;
        r11 = r6.mTop;
        r11 = (float) r11;
        r7.translate(r1, r11);
    L_0x0163:
        if (r18 == 0) goto L_0x0176;
    L_0x0165:
        if (r12 == 0) goto L_0x016b;
    L_0x0167:
        r0 = r32.save();
    L_0x016b:
        r1 = r6.mAttachInfo;
        r1 = r1.mApplicationScale;
        r1 = r23 / r1;
        r7.scale(r1, r1);
        r11 = r0;
        goto L_0x0177;
    L_0x0176:
        r11 = r0;
    L_0x0177:
        if (r12 == 0) goto L_0x017c;
    L_0x0179:
        r0 = r23;
        goto L_0x0185;
    L_0x017c:
        r0 = r31.getAlpha();
        r1 = r31.getTransitionAlpha();
        r0 = r0 * r1;
    L_0x0185:
        r24 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
        r10 = 262144; // 0x40000 float:3.67342E-40 double:1.295163E-318;
        if (r16 != 0) goto L_0x01bd;
    L_0x018b:
        r25 = (r0 > r23 ? 1 : (r0 == r23 ? 0 : -1));
        if (r25 < 0) goto L_0x01bd;
    L_0x018f:
        r25 = r31.hasIdentityMatrix();
        if (r25 == 0) goto L_0x01bd;
    L_0x0195:
        r1 = r6.mPrivateFlags3;
        r1 = r1 & 2;
        if (r1 == 0) goto L_0x019c;
    L_0x019b:
        goto L_0x01bd;
    L_0x019c:
        r1 = r6.mPrivateFlags;
        r1 = r1 & r10;
        if (r1 != r10) goto L_0x01af;
    L_0x01a1:
        r1 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r6.onSetAlpha(r1);
        r1 = r6.mPrivateFlags;
        r26 = -262145; // 0xfffffffffffbffff float:NaN double:NaN;
        r1 = r1 & r26;
        r6.mPrivateFlags = r1;
    L_0x01af:
        r27 = r9;
        r26 = r11;
        r29 = r13;
        r28 = r14;
        r9 = r2;
        r11 = r3;
        r14 = r4;
        r13 = r5;
        goto L_0x02f2;
    L_0x01bd:
        if (r16 != 0) goto L_0x01c9;
    L_0x01bf:
        if (r14 != 0) goto L_0x01c2;
    L_0x01c1:
        goto L_0x01c9;
    L_0x01c2:
        r26 = r5;
        r27 = r9;
        r9 = r0;
        goto L_0x0233;
    L_0x01c9:
        r1 = 0;
        r26 = 0;
        if (r22 == 0) goto L_0x01d1;
    L_0x01ce:
        r1 = -r3;
        r10 = -r2;
        goto L_0x01d3;
    L_0x01d1:
        r10 = r26;
    L_0x01d3:
        if (r16 == 0) goto L_0x0217;
    L_0x01d5:
        if (r17 == 0) goto L_0x0203;
    L_0x01d7:
        if (r12 == 0) goto L_0x01e5;
    L_0x01d9:
        r26 = r5;
        r5 = r16.getMatrix();
        r4.setAnimationMatrix(r5);
        r27 = r9;
        goto L_0x01fc;
    L_0x01e5:
        r26 = r5;
        r5 = -r1;
        r5 = (float) r5;
        r27 = r9;
        r9 = -r10;
        r9 = (float) r9;
        r7.translate(r5, r9);
        r5 = r16.getMatrix();
        r7.concat(r5);
        r5 = (float) r1;
        r9 = (float) r10;
        r7.translate(r5, r9);
    L_0x01fc:
        r5 = r8.mGroupFlags;
        r5 = r5 | 256;
        r8.mGroupFlags = r5;
        goto L_0x0207;
    L_0x0203:
        r26 = r5;
        r27 = r9;
    L_0x0207:
        r5 = r16.getAlpha();
        r9 = (r5 > r23 ? 1 : (r5 == r23 ? 0 : -1));
        if (r9 >= 0) goto L_0x021b;
    L_0x020f:
        r0 = r0 * r5;
        r9 = r8.mGroupFlags;
        r9 = r9 | 256;
        r8.mGroupFlags = r9;
        goto L_0x021b;
    L_0x0217:
        r26 = r5;
        r27 = r9;
    L_0x021b:
        if (r14 != 0) goto L_0x0232;
    L_0x021d:
        if (r12 != 0) goto L_0x0232;
    L_0x021f:
        r5 = -r1;
        r5 = (float) r5;
        r9 = -r10;
        r9 = (float) r9;
        r7.translate(r5, r9);
        r5 = r31.getMatrix();
        r7.concat(r5);
        r5 = (float) r1;
        r9 = (float) r10;
        r7.translate(r5, r9);
    L_0x0232:
        r9 = r0;
    L_0x0233:
        r0 = (r9 > r23 ? 1 : (r9 == r23 ? 0 : -1));
        if (r0 < 0) goto L_0x024d;
    L_0x0237:
        r0 = r6.mPrivateFlags3;
        r0 = r0 & 2;
        if (r0 == 0) goto L_0x023e;
    L_0x023d:
        goto L_0x024d;
    L_0x023e:
        r30 = r9;
        r29 = r13;
        r28 = r14;
        r13 = r26;
        r9 = r2;
        r14 = r4;
        r26 = r11;
        r11 = r3;
        goto L_0x02f0;
    L_0x024d:
        r0 = (r9 > r23 ? 1 : (r9 == r23 ? 0 : -1));
        if (r0 >= 0) goto L_0x0258;
    L_0x0251:
        r0 = r6.mPrivateFlags3;
        r0 = r0 | 2;
        r6.mPrivateFlags3 = r0;
        goto L_0x025e;
    L_0x0258:
        r0 = r6.mPrivateFlags3;
        r0 = r0 & -3;
        r6.mPrivateFlags3 = r0;
    L_0x025e:
        r0 = r8.mGroupFlags;
        r0 = r0 | 256;
        r8.mGroupFlags = r0;
        if (r21 != 0) goto L_0x02e3;
    L_0x0266:
        r0 = r9 * r24;
        r10 = (int) r0;
        r0 = r6.onSetAlpha(r10);
        if (r0 != 0) goto L_0x02ce;
    L_0x026f:
        if (r12 == 0) goto L_0x028d;
    L_0x0271:
        r0 = r31.getAlpha();
        r0 = r0 * r9;
        r1 = r31.getTransitionAlpha();
        r0 = r0 * r1;
        r4.setAlpha(r0);
        r30 = r9;
        r29 = r13;
        r28 = r14;
        r13 = r26;
        r9 = r2;
        r14 = r4;
        r26 = r11;
        r11 = r3;
        goto L_0x02e2;
    L_0x028d:
        if (r20 != 0) goto L_0x02c0;
    L_0x028f:
        r1 = (float) r3;
        r5 = (float) r2;
        r0 = r31.getWidth();
        r0 = r0 + r3;
        r0 = (float) r0;
        r28 = r31.getHeight();
        r29 = r0;
        r0 = r2 + r28;
        r0 = (float) r0;
        r28 = r29;
        r29 = r0;
        r0 = r32;
        r30 = r9;
        r9 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r9 = r2;
        r2 = r5;
        r5 = r3;
        r3 = r28;
        r28 = r14;
        r14 = r4;
        r4 = r29;
        r29 = r13;
        r13 = r26;
        r26 = r11;
        r11 = r5;
        r5 = r10;
        r0.saveLayerAlpha(r1, r2, r3, r4, r5);
        goto L_0x02e2;
    L_0x02c0:
        r30 = r9;
        r29 = r13;
        r28 = r14;
        r13 = r26;
        r9 = r2;
        r14 = r4;
        r26 = r11;
        r11 = r3;
        goto L_0x02e2;
    L_0x02ce:
        r30 = r9;
        r29 = r13;
        r28 = r14;
        r13 = r26;
        r9 = r2;
        r14 = r4;
        r26 = r11;
        r11 = r3;
        r0 = r6.mPrivateFlags;
        r1 = 262144; // 0x40000 float:3.67342E-40 double:1.295163E-318;
        r0 = r0 | r1;
        r6.mPrivateFlags = r0;
    L_0x02e2:
        goto L_0x02f0;
    L_0x02e3:
        r30 = r9;
        r29 = r13;
        r28 = r14;
        r13 = r26;
        r9 = r2;
        r14 = r4;
        r26 = r11;
        r11 = r3;
    L_0x02f0:
        r0 = r30;
    L_0x02f2:
        if (r12 != 0) goto L_0x0332;
    L_0x02f4:
        r1 = r15 & 1;
        if (r1 == 0) goto L_0x032b;
    L_0x02f8:
        if (r13 != 0) goto L_0x032b;
    L_0x02fa:
        if (r22 == 0) goto L_0x030c;
    L_0x02fc:
        r1 = r31.getWidth();
        r3 = r11 + r1;
        r1 = r31.getHeight();
        r2 = r9 + r1;
        r7.clipRect(r11, r9, r3, r2);
        goto L_0x032b;
    L_0x030c:
        if (r18 == 0) goto L_0x031f;
    L_0x030e:
        if (r13 != 0) goto L_0x0312;
    L_0x0310:
        r3 = 0;
        goto L_0x0320;
    L_0x0312:
        r1 = r13.getWidth();
        r2 = r13.getHeight();
        r3 = 0;
        r7.clipRect(r3, r3, r1, r2);
        goto L_0x032b;
    L_0x031f:
        r3 = 0;
    L_0x0320:
        r1 = r31.getWidth();
        r2 = r31.getHeight();
        r7.clipRect(r3, r3, r1, r2);
    L_0x032b:
        r1 = r6.mClipBounds;
        if (r1 == 0) goto L_0x0332;
    L_0x032f:
        r7.clipRect(r1);
    L_0x0332:
        r1 = -2097153; // 0xffffffffffdfffff float:NaN double:NaN;
        if (r21 != 0) goto L_0x0358;
    L_0x0337:
        if (r12 == 0) goto L_0x0345;
    L_0x0339:
        r2 = r6.mPrivateFlags;
        r1 = r1 & r2;
        r6.mPrivateFlags = r1;
        r1 = r7;
        r1 = (android.graphics.RecordingCanvas) r1;
        r1.drawRenderNode(r14);
        goto L_0x039f;
    L_0x0345:
        r2 = r6.mPrivateFlags;
        r3 = r2 & 128;
        r4 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        if (r3 != r4) goto L_0x0354;
    L_0x034d:
        r1 = r1 & r2;
        r6.mPrivateFlags = r1;
        r31.dispatchDraw(r32);
        goto L_0x039f;
    L_0x0354:
        r31.draw(r32);
        goto L_0x039f;
    L_0x0358:
        if (r13 == 0) goto L_0x039f;
    L_0x035a:
        r2 = r6.mPrivateFlags;
        r1 = r1 & r2;
        r6.mPrivateFlags = r1;
        r1 = 0;
        if (r20 == 0) goto L_0x0386;
    L_0x0362:
        r2 = r6.mLayerPaint;
        if (r2 != 0) goto L_0x0367;
    L_0x0366:
        goto L_0x0386;
    L_0x0367:
        r2 = r2.getAlpha();
        r3 = (r0 > r23 ? 1 : (r0 == r23 ? 0 : -1));
        if (r3 >= 0) goto L_0x0377;
    L_0x036f:
        r3 = r6.mLayerPaint;
        r4 = (float) r2;
        r4 = r4 * r0;
        r4 = (int) r4;
        r3.setAlpha(r4);
    L_0x0377:
        r3 = r6.mLayerPaint;
        r7.drawBitmap(r13, r1, r1, r3);
        r1 = (r0 > r23 ? 1 : (r0 == r23 ? 0 : -1));
        if (r1 >= 0) goto L_0x039f;
    L_0x0380:
        r1 = r6.mLayerPaint;
        r1.setAlpha(r2);
        goto L_0x039f;
    L_0x0386:
        r2 = r8.mCachePaint;
        if (r2 != 0) goto L_0x0396;
    L_0x038a:
        r3 = new android.graphics.Paint;
        r3.<init>();
        r2 = r3;
        r3 = 0;
        r2.setDither(r3);
        r8.mCachePaint = r2;
    L_0x0396:
        r3 = r0 * r24;
        r3 = (int) r3;
        r2.setAlpha(r3);
        r7.drawBitmap(r13, r1, r1, r2);
    L_0x039f:
        if (r26 < 0) goto L_0x03a7;
    L_0x03a1:
        r1 = r26;
        r7.restoreToCount(r1);
        goto L_0x03a9;
    L_0x03a7:
        r1 = r26;
    L_0x03a9:
        r2 = r19;
        if (r2 == 0) goto L_0x03bf;
    L_0x03ad:
        if (r29 != 0) goto L_0x03bf;
    L_0x03af:
        if (r27 != 0) goto L_0x03bc;
    L_0x03b1:
        r3 = r2.getFillAfter();
        if (r3 != 0) goto L_0x03bc;
    L_0x03b7:
        r3 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r6.onSetAlpha(r3);
    L_0x03bc:
        r8.finishAnimatingView(r6, r2);
    L_0x03bf:
        if (r29 == 0) goto L_0x03d4;
    L_0x03c1:
        if (r27 == 0) goto L_0x03d4;
    L_0x03c3:
        r3 = r2.hasAlpha();
        if (r3 == 0) goto L_0x03d4;
    L_0x03c9:
        r3 = r6.mPrivateFlags;
        r4 = 262144; // 0x40000 float:3.67342E-40 double:1.295163E-318;
        r3 = r3 & r4;
        if (r3 != r4) goto L_0x03d4;
    L_0x03d0:
        r3 = 1;
        r6.invalidate(r3);
    L_0x03d4:
        r3 = 0;
        r6.mRecreateDisplayList = r3;
        return r29;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.View.draw(android.graphics.Canvas, android.view.ViewGroup, long):boolean");
    }

    static Paint getDebugPaint() {
        if (sDebugPaint == null) {
            sDebugPaint = new Paint();
            sDebugPaint.setAntiAlias(false);
        }
        return sDebugPaint;
    }

    /* Access modifiers changed, original: final */
    public final int dipsToPixels(int dips) {
        return (int) ((((float) dips) * getContext().getResources().getDisplayMetrics().density) + 0.5f);
    }

    private final void debugDrawFocus(Canvas canvas) {
        if (isFocused()) {
            int cornerSquareSize = dipsToPixels(8);
            int l = this.mScrollX;
            int r = (this.mRight + l) - this.mLeft;
            int t = this.mScrollY;
            int b = (this.mBottom + t) - this.mTop;
            Paint paint = getDebugPaint();
            paint.setColor(DEBUG_CORNERS_COLOR);
            paint.setStyle(Style.FILL);
            Canvas canvas2 = canvas;
            Paint paint2 = paint;
            canvas2.drawRect((float) l, (float) t, (float) (l + cornerSquareSize), (float) (t + cornerSquareSize), paint2);
            canvas2 = canvas;
            canvas2.drawRect((float) (r - cornerSquareSize), (float) t, (float) r, (float) (t + cornerSquareSize), paint2);
            canvas.drawRect((float) l, (float) (b - cornerSquareSize), (float) (l + cornerSquareSize), (float) b, paint2);
            canvas.drawRect((float) (r - cornerSquareSize), (float) (b - cornerSquareSize), (float) r, (float) b, paint2);
            paint.setStyle(Style.STROKE);
            canvas2 = canvas;
            canvas2.drawLine((float) l, (float) t, (float) r, (float) b, paint2);
            canvas2.drawLine((float) l, (float) b, (float) r, (float) t, paint2);
        }
    }

    public void draw(Canvas canvas) {
        Canvas canvas2 = canvas;
        int privateFlags = this.mPrivateFlags;
        this.mPrivateFlags = (-2097153 & privateFlags) | 32;
        drawBackground(canvas);
        int viewFlags = this.mViewFlags;
        boolean horizontalEdges = (viewFlags & 4096) != 0;
        boolean verticalEdges = (viewFlags & 8192) != 0;
        if (verticalEdges || horizontalEdges) {
            int paddingLeft;
            boolean drawRight;
            float topFadeStrength;
            int length;
            int topSaveCount;
            int bottomSaveCount;
            int left;
            int bottom;
            float fadeHeight;
            int solidColor;
            int length2;
            int left2;
            float length3;
            int bottom2;
            float bottomFadeStrength = 0.0f;
            float leftFadeStrength = 0.0f;
            float rightFadeStrength = 0.0f;
            int paddingLeft2 = this.mPaddingLeft;
            boolean offsetRequired = isPaddingOffsetRequired();
            if (offsetRequired) {
                paddingLeft = paddingLeft2 + getLeftPaddingOffset();
            } else {
                paddingLeft = paddingLeft2;
            }
            paddingLeft2 = this.mScrollX + paddingLeft;
            boolean drawTop = false;
            int right = (((this.mRight + paddingLeft2) - this.mLeft) - this.mPaddingRight) - paddingLeft;
            int top = this.mScrollY + getFadeTop(offsetRequired);
            int bottom3 = top + getFadeHeight(offsetRequired);
            boolean z;
            boolean bottom4;
            if (offsetRequired) {
                z = offsetRequired;
                privateFlags = right + getRightPaddingOffset();
                right = bottom3 + getBottomPaddingOffset();
                bottom4 = z;
            } else {
                z = offsetRequired;
                privateFlags = right;
                right = bottom3;
                bottom4 = z;
            }
            ScrollabilityCache scrollabilityCache = this.mScrollCache;
            boolean drawBottom = false;
            float fadeHeight2 = (float) scrollabilityCache.fadingEdgeLength;
            boolean drawLeft = false;
            int length4 = (int) fadeHeight2;
            if (verticalEdges) {
                drawRight = false;
                topFadeStrength = 0.0f;
                if (top + length4 > right - length4) {
                    length4 = (right - top) / 2;
                }
            } else {
                drawRight = false;
                topFadeStrength = 0.0f;
            }
            if (!horizontalEdges || paddingLeft2 + length4 <= privateFlags - length4) {
                length = length4;
            } else {
                length = (privateFlags - paddingLeft2) / 2;
            }
            if (verticalEdges) {
                verticalEdges = Math.max(0.0f, Math.min(1.0f, getTopFadingEdgeStrength()));
                drawTop = verticalEdges * fadeHeight2 > 1.0f;
                topFadeStrength = verticalEdges;
                bottomFadeStrength = Math.max(0.0f, Math.min(1.0f, getBottomFadingEdgeStrength()));
                drawBottom = bottomFadeStrength * fadeHeight2 > true;
            }
            if (horizontalEdges) {
                leftFadeStrength = Math.max(0.0f, Math.min(1.0f, getLeftFadingEdgeStrength()));
                verticalEdges = leftFadeStrength * fadeHeight2 > 1.0f;
                rightFadeStrength = Math.max(0.0f, Math.min(1.0f, getRightFadingEdgeStrength()));
                drawRight = rightFadeStrength * fadeHeight2 > 1.0f;
                drawLeft = verticalEdges;
            }
            int saveCount = canvas.getSaveCount();
            length4 = -1;
            int bottomSaveCount2 = -1;
            int leftSaveCount = -1;
            int solidColor2 = getSolidColor();
            if (solidColor2 == 0) {
                if (drawTop) {
                    topSaveCount = -1;
                    length4 = canvas2.saveUnclippedLayer(paddingLeft2, top, privateFlags, top + length);
                } else {
                    topSaveCount = -1;
                }
                if (drawBottom) {
                    topSaveCount = length4;
                    bottomSaveCount2 = canvas2.saveUnclippedLayer(paddingLeft2, right - length, privateFlags, right);
                } else {
                    topSaveCount = length4;
                }
                if (drawLeft) {
                    leftSaveCount = canvas2.saveUnclippedLayer(paddingLeft2, top, paddingLeft2 + length, right);
                }
                if (drawRight) {
                    bottomSaveCount = bottomSaveCount2;
                    length4 = leftSaveCount;
                    bottomSaveCount2 = canvas2.saveUnclippedLayer(privateFlags - length, top, privateFlags, right);
                    leftSaveCount = saveCount;
                    saveCount = topSaveCount;
                } else {
                    bottomSaveCount = bottomSaveCount2;
                    length4 = leftSaveCount;
                    bottomSaveCount2 = -1;
                    leftSaveCount = saveCount;
                    saveCount = topSaveCount;
                }
            } else {
                topSaveCount = -1;
                scrollabilityCache.setFadeColor(solidColor2);
                bottomSaveCount = -1;
                length4 = -1;
                bottomSaveCount2 = -1;
                leftSaveCount = saveCount;
                saveCount = topSaveCount;
            }
            onDraw(canvas);
            dispatchDraw(canvas);
            int topSaveCount2 = saveCount;
            Paint p = scrollabilityCache.paint;
            topSaveCount = bottomSaveCount;
            Matrix matrix = scrollabilityCache.matrix;
            float bottomFadeStrength2 = bottomFadeStrength;
            Shader fade = scrollabilityCache.shader;
            int i;
            float f;
            if (drawRight) {
                left = paddingLeft2;
                matrix.setScale(1.0f, fadeHeight2 * rightFadeStrength);
                matrix.postRotate(90.0f);
                matrix.postTranslate((float) privateFlags, (float) top);
                fade.setLocalMatrix(matrix);
                p.setShader(fade);
                if (solidColor2 == 0) {
                    canvas2.restoreUnclippedLayer(bottomSaveCount2, p);
                    bottom = right;
                    fadeHeight = fadeHeight2;
                    solidColor = solidColor2;
                    i = bottomSaveCount2;
                    length2 = length;
                    f = rightFadeStrength;
                    left2 = left;
                    length3 = 1.0f;
                    bottomSaveCount2 = top;
                    left = privateFlags;
                    privateFlags = length4;
                } else {
                    bottomSaveCount2 = top;
                    left2 = left;
                    left = privateFlags;
                    fadeHeight = fadeHeight2;
                    bottom = right;
                    privateFlags = length4;
                    solidColor = solidColor2;
                    length2 = length;
                    length3 = 1.0f;
                    canvas.drawRect((float) (privateFlags - length), (float) top, (float) privateFlags, (float) right, p);
                }
            } else {
                bottom = right;
                solidColor = solidColor2;
                left = privateFlags;
                ScrollabilityCache scrollabilityCache2 = scrollabilityCache;
                i = bottomSaveCount2;
                length2 = length;
                f = rightFadeStrength;
                length3 = 1.0f;
                bottomSaveCount2 = top;
                left2 = paddingLeft2;
                fadeHeight = fadeHeight2;
                privateFlags = length4;
            }
            if (drawLeft) {
                matrix.setScale(length3, fadeHeight * leftFadeStrength);
                matrix.postRotate(-90.0f);
                matrix.postTranslate((float) left2, (float) bottomSaveCount2);
                fade.setLocalMatrix(matrix);
                p.setShader(fade);
                if (solidColor == 0) {
                    canvas2.restoreUnclippedLayer(privateFlags, p);
                    bottom2 = bottom;
                } else {
                    solidColor2 = bottom;
                    bottom2 = solidColor2;
                    canvas.drawRect((float) left2, (float) bottomSaveCount2, (float) (left2 + length2), (float) solidColor2, p);
                }
            } else {
                bottom2 = bottom;
            }
            if (drawBottom) {
                matrix.setScale(length3, fadeHeight * bottomFadeStrength2);
                matrix.postRotate(180.0f);
                solidColor2 = bottom2;
                matrix.postTranslate((float) left2, (float) solidColor2);
                fade.setLocalMatrix(matrix);
                p.setShader(fade);
                if (solidColor == 0) {
                    length4 = topSaveCount;
                    canvas2.restoreUnclippedLayer(length4, p);
                    length = solidColor2;
                    bottom = privateFlags;
                    privateFlags = left;
                    left = length4;
                } else {
                    int right2 = left;
                    privateFlags = right2;
                    left = topSaveCount;
                    canvas.drawRect((float) left2, (float) (solidColor2 - length2), (float) right2, (float) solidColor2, p);
                }
            } else {
                privateFlags = left;
                length = bottom2;
                left = topSaveCount;
            }
            if (drawTop) {
                matrix.setScale(1.0f, fadeHeight * topFadeStrength);
                matrix.postTranslate((float) left2, (float) bottomSaveCount2);
                fade.setLocalMatrix(matrix);
                p.setShader(fade);
                if (solidColor == 0) {
                    solidColor2 = topSaveCount2;
                    canvas2.restoreUnclippedLayer(solidColor2, p);
                    topSaveCount = solidColor2;
                } else {
                    canvas.drawRect((float) left2, (float) bottomSaveCount2, (float) privateFlags, (float) (bottomSaveCount2 + length2), p);
                }
            }
            canvas2.restoreToCount(leftSaveCount);
            drawAutofilledHighlight(canvas);
            ViewOverlay viewOverlay = this.mOverlay;
            if (!(viewOverlay == null || viewOverlay.isEmpty())) {
                this.mOverlay.getOverlayView().dispatchDraw(canvas2);
            }
            onDrawForeground(canvas);
            if (debugDraw()) {
                debugDrawFocus(canvas);
            }
            return;
        }
        onDraw(canvas);
        dispatchDraw(canvas);
        drawAutofilledHighlight(canvas);
        ViewOverlay viewOverlay2 = this.mOverlay;
        if (!(viewOverlay2 == null || viewOverlay2.isEmpty())) {
            this.mOverlay.getOverlayView().dispatchDraw(canvas2);
        }
        onDrawForeground(canvas);
        drawDefaultFocusHighlight(canvas);
        if (debugDraw()) {
            debugDrawFocus(canvas);
        }
    }

    @UnsupportedAppUsage
    private void drawBackground(Canvas canvas) {
        Drawable background = this.mBackground;
        if (background != null) {
            setBackgroundBounds();
            if (canvas.isHardwareAccelerated()) {
                AttachInfo attachInfo = this.mAttachInfo;
                if (!(attachInfo == null || attachInfo.mThreadedRenderer == null)) {
                    this.mBackgroundRenderNode = getDrawableRenderNode(background, this.mBackgroundRenderNode);
                    RenderNode renderNode = this.mBackgroundRenderNode;
                    if (renderNode != null && renderNode.hasDisplayList()) {
                        setBackgroundRenderNodeProperties(renderNode);
                        ((RecordingCanvas) canvas).drawRenderNode(renderNode);
                        return;
                    }
                }
            }
            int scrollX = this.mScrollX;
            int scrollY = this.mScrollY;
            if ((scrollX | scrollY) == 0) {
                background.draw(canvas);
            } else {
                canvas.translate((float) scrollX, (float) scrollY);
                background.draw(canvas);
                canvas.translate((float) (-scrollX), (float) (-scrollY));
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setBackgroundBounds() {
        if (this.mBackgroundSizeChanged) {
            Drawable drawable = this.mBackground;
            if (drawable != null) {
                drawable.setBounds(0, 0, this.mRight - this.mLeft, this.mBottom - this.mTop);
                this.mBackgroundSizeChanged = false;
                rebuildOutline();
            }
        }
    }

    private void setBackgroundRenderNodeProperties(RenderNode renderNode) {
        renderNode.setTranslationX((float) this.mScrollX);
        renderNode.setTranslationY((float) this.mScrollY);
    }

    private RenderNode getDrawableRenderNode(Drawable drawable, RenderNode renderNode) {
        if (renderNode == null) {
            renderNode = RenderNode.create(drawable.getClass().getName(), new ViewAnimationHostBridge(this));
            renderNode.setUsageHint(1);
        }
        Rect bounds = drawable.getBounds();
        RecordingCanvas canvas = renderNode.beginRecording(bounds.width(), bounds.height());
        canvas.translate((float) (-bounds.left), (float) (-bounds.top));
        try {
            drawable.draw(canvas);
            renderNode.setLeftTopRightBottom(bounds.left, bounds.top, bounds.right, bounds.bottom);
            renderNode.setProjectBackwards(drawable.isProjected());
            renderNode.setProjectionReceiver(true);
            renderNode.setClipToBounds(false);
            return renderNode;
        } finally {
            renderNode.endRecording();
        }
    }

    public ViewOverlay getOverlay() {
        if (this.mOverlay == null) {
            this.mOverlay = new ViewOverlay(this.mContext, this);
        }
        return this.mOverlay;
    }

    @ExportedProperty(category = "drawing")
    public int getSolidColor() {
        return 0;
    }

    private static String printFlags(int flags) {
        StringBuilder stringBuilder;
        String output = "";
        int numFlags = 0;
        if ((flags & 1) == 1) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(output);
            stringBuilder.append("TAKES_FOCUS");
            output = stringBuilder.toString();
            numFlags = 0 + 1;
        }
        int i = flags & 12;
        String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        if (i == 4) {
            if (numFlags > 0) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(output);
                stringBuilder.append(str);
                output = stringBuilder.toString();
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(output);
            stringBuilder.append("INVISIBLE");
            return stringBuilder.toString();
        } else if (i != 8) {
            return output;
        } else {
            if (numFlags > 0) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(output);
                stringBuilder.append(str);
                output = stringBuilder.toString();
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(output);
            stringBuilder.append("GONE");
            return stringBuilder.toString();
        }
    }

    private static String printPrivateFlags(int privateFlags) {
        StringBuilder stringBuilder;
        String output = "";
        int numFlags = 0;
        if ((privateFlags & 1) == 1) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(output);
            stringBuilder.append("WANTS_FOCUS");
            output = stringBuilder.toString();
            numFlags = 0 + 1;
        }
        int i = privateFlags & 2;
        String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        if (i == 2) {
            if (numFlags > 0) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(output);
                stringBuilder.append(str);
                output = stringBuilder.toString();
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(output);
            stringBuilder.append("FOCUSED");
            output = stringBuilder.toString();
            numFlags++;
        }
        if ((privateFlags & 4) == 4) {
            if (numFlags > 0) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(output);
                stringBuilder.append(str);
                output = stringBuilder.toString();
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(output);
            stringBuilder.append("SELECTED");
            output = stringBuilder.toString();
            numFlags++;
        }
        if ((privateFlags & 8) == 8) {
            if (numFlags > 0) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(output);
                stringBuilder.append(str);
                output = stringBuilder.toString();
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(output);
            stringBuilder.append("IS_ROOT_NAMESPACE");
            output = stringBuilder.toString();
            numFlags++;
        }
        if ((privateFlags & 16) == 16) {
            if (numFlags > 0) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(output);
                stringBuilder.append(str);
                output = stringBuilder.toString();
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(output);
            stringBuilder.append("HAS_BOUNDS");
            output = stringBuilder.toString();
            numFlags++;
        }
        if ((privateFlags & 32) != 32) {
            return output;
        }
        if (numFlags > 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(output);
            stringBuilder.append(str);
            output = stringBuilder.toString();
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(output);
        stringBuilder.append("DRAWN");
        return stringBuilder.toString();
    }

    public boolean isLayoutRequested() {
        return (this.mPrivateFlags & 4096) == 4096;
    }

    public static boolean isLayoutModeOptical(Object o) {
        return (o instanceof ViewGroup) && ((ViewGroup) o).isLayoutModeOptical();
    }

    private boolean setOpticalFrame(int left, int top, int right, int bottom) {
        ViewParent viewParent = this.mParent;
        Insets parentInsets = viewParent instanceof View ? ((View) viewParent).getOpticalInsets() : Insets.NONE;
        Insets childInsets = getOpticalInsets();
        return setFrame((parentInsets.left + left) - childInsets.left, (parentInsets.top + top) - childInsets.top, (parentInsets.left + right) + childInsets.right, (parentInsets.top + bottom) + childInsets.bottom);
    }

    public void layout(int l, int t, int r, int b) {
        View oldL;
        if ((this.mPrivateFlags3 & 8) != 0) {
            onMeasure(this.mOldWidthMeasureSpec, this.mOldHeightMeasureSpec);
            this.mPrivateFlags3 &= -9;
        }
        int oldL2 = this.mLeft;
        int oldT = this.mTop;
        int oldB = this.mBottom;
        int oldR = this.mRight;
        boolean changed = isLayoutModeOptical(this.mParent) ? setOpticalFrame(l, t, r, b) : setFrame(l, t, r, b);
        RoundScrollbarRenderer roundScrollbarRenderer = null;
        int i;
        if (changed || (this.mPrivateFlags & 8192) == 8192) {
            onLayout(changed, l, t, r, b);
            if (!shouldDrawRoundScrollbar()) {
                this.mRoundScrollbarRenderer = null;
            } else if (this.mRoundScrollbarRenderer == null) {
                this.mRoundScrollbarRenderer = new RoundScrollbarRenderer(this);
            }
            this.mPrivateFlags &= -8193;
            ListenerInfo li = this.mListenerInfo;
            if (li == null || li.mOnLayoutChangeListeners == null) {
                i = oldL2;
                oldL = null;
            } else {
                int i2;
                int numListeners;
                ArrayList<OnLayoutChangeListener> listenersCopy;
                ListenerInfo li2;
                ArrayList<OnLayoutChangeListener> listenersCopy2 = (ArrayList) li.mOnLayoutChangeListeners.clone();
                int numListeners2 = listenersCopy2.size();
                int i3 = 0;
                while (i3 < numListeners2) {
                    i2 = i3;
                    numListeners = numListeners2;
                    listenersCopy = listenersCopy2;
                    li2 = li;
                    i = oldL2;
                    RoundScrollbarRenderer roundScrollbarRenderer2 = roundScrollbarRenderer;
                    ((OnLayoutChangeListener) listenersCopy2.get(i3)).onLayoutChange(this, l, t, r, b, oldL2, oldT, oldR, oldB);
                    i3 = i2 + 1;
                    roundScrollbarRenderer = roundScrollbarRenderer2;
                    numListeners2 = numListeners;
                    listenersCopy2 = listenersCopy;
                    li = li2;
                    oldL2 = i;
                }
                i2 = i3;
                numListeners = numListeners2;
                listenersCopy = listenersCopy2;
                li2 = li;
                i = oldL2;
                oldL = roundScrollbarRenderer;
            }
        } else {
            i = oldL2;
            oldL = null;
        }
        boolean wasLayoutValid = isLayoutValid();
        this.mPrivateFlags &= -4097;
        this.mPrivateFlags3 |= 4;
        if (wasLayoutValid || !isFocused()) {
            int i4 = this.mPrivateFlags;
            if ((i4 & 1) != 0) {
                this.mPrivateFlags = i4 & -2;
                View focused = findFocus();
                if (!(focused == null || restoreDefaultFocus() || hasParentWantsFocus())) {
                    focused.clearFocusInternal(oldL, true, false);
                }
            }
        } else {
            this.mPrivateFlags &= -2;
            if (canTakeFocus()) {
                clearParentsWantFocus();
            } else if (getViewRootImpl() == null || !getViewRootImpl().isInLayout()) {
                clearFocusInternal(oldL, true, false);
                clearParentsWantFocus();
            } else if (!hasParentWantsFocus()) {
                clearFocusInternal(oldL, true, false);
            }
        }
        int i5 = this.mPrivateFlags3;
        if ((134217728 & i5) != 0) {
            this.mPrivateFlags3 = i5 & -134217729;
            notifyEnterOrExitForAutoFillIfNeeded(true);
        }
    }

    private boolean hasParentWantsFocus() {
        ViewParent parent = this.mParent;
        while (parent instanceof ViewGroup) {
            ViewGroup pv = (ViewGroup) parent;
            if ((pv.mPrivateFlags & 1) != 0) {
                return true;
            }
            parent = pv.mParent;
        }
        return false;
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public boolean setFrame(int left, int top, int right, int bottom) {
        int i = left;
        int i2 = top;
        int i3 = right;
        int i4 = bottom;
        boolean changed = false;
        if (!(this.mLeft == i && this.mRight == i3 && this.mTop == i2 && this.mBottom == i4)) {
            changed = true;
            int drawn = this.mPrivateFlags & 32;
            int oldWidth = this.mRight - this.mLeft;
            int oldHeight = this.mBottom - this.mTop;
            int newWidth = i3 - i;
            int newHeight = i4 - i2;
            boolean sizeChanged = (newWidth == oldWidth && newHeight == oldHeight) ? false : true;
            invalidate(sizeChanged);
            this.mLeft = i;
            this.mTop = i2;
            this.mRight = i3;
            this.mBottom = i4;
            this.mRenderNode.setLeftTopRightBottom(this.mLeft, this.mTop, this.mRight, this.mBottom);
            this.mPrivateFlags |= 16;
            if (sizeChanged) {
                sizeChange(newWidth, newHeight, oldWidth, oldHeight);
            }
            if ((this.mViewFlags & 12) == 0 || this.mGhostView != null) {
                this.mPrivateFlags |= 32;
                invalidate(sizeChanged);
                invalidateParentCaches();
            }
            this.mPrivateFlags |= drawn;
            this.mBackgroundSizeChanged = true;
            this.mDefaultFocusHighlightSizeChanged = true;
            ForegroundInfo foregroundInfo = this.mForegroundInfo;
            if (foregroundInfo != null) {
                foregroundInfo.mBoundsChanged = true;
            }
            notifySubtreeAccessibilityStateChangedIfNeeded();
        }
        return changed;
    }

    public final void setLeftTopRightBottom(int left, int top, int right, int bottom) {
        setFrame(left, top, right, bottom);
    }

    private void sizeChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);
        ViewOverlay viewOverlay = this.mOverlay;
        if (viewOverlay != null) {
            viewOverlay.getOverlayView().setRight(newWidth);
            this.mOverlay.getOverlayView().setBottom(newHeight);
        }
        if (!sCanFocusZeroSized && isLayoutValid()) {
            ViewParent viewParent = this.mParent;
            if (!((viewParent instanceof ViewGroup) && ((ViewGroup) viewParent).isLayoutSuppressed())) {
                if (newWidth <= 0 || newHeight <= 0) {
                    if (hasFocus()) {
                        clearFocus();
                        viewParent = this.mParent;
                        if (viewParent instanceof ViewGroup) {
                            ((ViewGroup) viewParent).clearFocusedInCluster();
                        }
                    }
                    clearAccessibilityFocus();
                } else if ((oldWidth <= 0 || oldHeight <= 0) && this.mParent != null && canTakeFocus()) {
                    this.mParent.focusableViewAvailable(this);
                }
            }
        }
        rebuildOutline();
    }

    /* Access modifiers changed, original: protected */
    public void onFinishInflate() {
    }

    public Resources getResources() {
        return this.mResources;
    }

    public void invalidateDrawable(Drawable drawable) {
        if (verifyDrawable(drawable)) {
            Rect dirty = drawable.getDirtyBounds();
            int scrollX = this.mScrollX;
            int scrollY = this.mScrollY;
            invalidate(dirty.left + scrollX, dirty.top + scrollY, dirty.right + scrollX, dirty.bottom + scrollY);
            rebuildOutline();
        }
    }

    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        if (verifyDrawable(who) && what != null) {
            long delay = when - SystemClock.uptimeMillis();
            AttachInfo attachInfo = this.mAttachInfo;
            if (attachInfo != null) {
                attachInfo.mViewRootImpl.mChoreographer.postCallbackDelayed(1, what, who, Choreographer.subtractFrameDelay(delay));
                return;
            }
            getRunQueue().postDelayed(what, delay);
        }
    }

    public void unscheduleDrawable(Drawable who, Runnable what) {
        if (verifyDrawable(who) && what != null) {
            AttachInfo attachInfo = this.mAttachInfo;
            if (attachInfo != null) {
                attachInfo.mViewRootImpl.mChoreographer.removeCallbacks(1, what, who);
            }
            getRunQueue().removeCallbacks(what);
        }
    }

    public void unscheduleDrawable(Drawable who) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null && who != null) {
            attachInfo.mViewRootImpl.mChoreographer.removeCallbacks(1, null, who);
        }
    }

    /* Access modifiers changed, original: protected */
    public void resolveDrawables() {
        if (isLayoutDirectionResolved() || getRawLayoutDirection() != 2) {
            int layoutDirection = isLayoutDirectionResolved() ? getLayoutDirection() : getRawLayoutDirection();
            Drawable drawable = this.mBackground;
            if (drawable != null) {
                drawable.setLayoutDirection(layoutDirection);
            }
            ForegroundInfo foregroundInfo = this.mForegroundInfo;
            if (!(foregroundInfo == null || foregroundInfo.mDrawable == null)) {
                this.mForegroundInfo.mDrawable.setLayoutDirection(layoutDirection);
            }
            drawable = this.mDefaultFocusHighlight;
            if (drawable != null) {
                drawable.setLayoutDirection(layoutDirection);
            }
            this.mPrivateFlags2 |= 1073741824;
            onResolveDrawables(layoutDirection);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean areDrawablesResolved() {
        return (this.mPrivateFlags2 & 1073741824) == 1073741824;
    }

    public void onResolveDrawables(int layoutDirection) {
    }

    /* Access modifiers changed, original: protected */
    public void resetResolvedDrawables() {
        resetResolvedDrawablesInternal();
    }

    /* Access modifiers changed, original: 0000 */
    public void resetResolvedDrawablesInternal() {
        this.mPrivateFlags2 &= -1073741825;
    }

    /* Access modifiers changed, original: protected */
    public boolean verifyDrawable(Drawable who) {
        if (who != this.mBackground) {
            ForegroundInfo foregroundInfo = this.mForegroundInfo;
            if ((foregroundInfo == null || foregroundInfo.mDrawable != who) && this.mDefaultFocusHighlight != who) {
                return false;
            }
        }
        return true;
    }

    /* Access modifiers changed, original: protected */
    public void drawableStateChanged() {
        int[] state = getDrawableState();
        boolean changed = false;
        Drawable bg = this.mBackground;
        if (bg != null && bg.isStateful()) {
            changed = false | bg.setState(state);
        }
        Drawable hl = this.mDefaultFocusHighlight;
        if (hl != null && hl.isStateful()) {
            changed |= hl.setState(state);
        }
        ForegroundInfo foregroundInfo = this.mForegroundInfo;
        Drawable fg = foregroundInfo != null ? foregroundInfo.mDrawable : null;
        if (fg != null && fg.isStateful()) {
            changed |= fg.setState(state);
        }
        Drawable scrollBar = this.mScrollCache;
        if (scrollBar != null) {
            scrollBar = scrollBar.scrollBar;
            if (scrollBar != null && scrollBar.isStateful()) {
                int i = (!scrollBar.setState(state) || this.mScrollCache.state == 0) ? 0 : 1;
                changed |= i;
            }
        }
        StateListAnimator stateListAnimator = this.mStateListAnimator;
        if (stateListAnimator != null) {
            stateListAnimator.setState(state);
        }
        if (changed) {
            invalidate();
        }
    }

    public void drawableHotspotChanged(float x, float y) {
        Drawable drawable = this.mBackground;
        if (drawable != null) {
            drawable.setHotspot(x, y);
        }
        drawable = this.mDefaultFocusHighlight;
        if (drawable != null) {
            drawable.setHotspot(x, y);
        }
        ForegroundInfo foregroundInfo = this.mForegroundInfo;
        if (!(foregroundInfo == null || foregroundInfo.mDrawable == null)) {
            this.mForegroundInfo.mDrawable.setHotspot(x, y);
        }
        dispatchDrawableHotspotChanged(x, y);
    }

    public void dispatchDrawableHotspotChanged(float x, float y) {
    }

    public void refreshDrawableState() {
        if (Extension.get().getExtension() != null) {
            ((Interface) Extension.get().getExtension().asInterface()).refreshDrawableState(this);
        } else {
            originalRefreshDrawableState();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void originalRefreshDrawableState() {
        this.mPrivateFlags |= 1024;
        drawableStateChanged();
        ViewParent parent = this.mParent;
        if (parent != null) {
            parent.childDrawableStateChanged(this);
        }
    }

    private Drawable getDefaultFocusHighlightDrawable() {
        if (this.mDefaultFocusHighlightCache == null) {
            TypedArray ta = this.mContext;
            if (ta != null) {
                ta = ta.obtainStyledAttributes(new int[]{16843534});
                this.mDefaultFocusHighlightCache = ta.getDrawable(0);
                ta.recycle();
            }
        }
        return this.mDefaultFocusHighlightCache;
    }

    private void setDefaultFocusHighlight(Drawable highlight) {
        this.mDefaultFocusHighlight = highlight;
        boolean z = true;
        this.mDefaultFocusHighlightSizeChanged = true;
        if (highlight != null) {
            int i = this.mPrivateFlags;
            if ((i & 128) != 0) {
                this.mPrivateFlags = i & -129;
            }
            highlight.setLayoutDirection(getLayoutDirection());
            if (highlight.isStateful()) {
                highlight.setState(getDrawableState());
            }
            if (isAttachedToWindow()) {
                if (!(getWindowVisibility() == 0 && isShown())) {
                    z = false;
                }
                highlight.setVisible(z, false);
            }
            highlight.setCallback(this);
        } else if ((this.mViewFlags & 128) != 0 && this.mBackground == null) {
            ForegroundInfo foregroundInfo = this.mForegroundInfo;
            if (foregroundInfo == null || foregroundInfo.mDrawable == null) {
                this.mPrivateFlags |= 128;
            }
        }
        invalidate();
    }

    public boolean isDefaultFocusHighlightNeeded(Drawable background, Drawable foreground) {
        boolean lackFocusState = ((background != null && background.isStateful() && background.hasFocusStateSpecified()) || (foreground != null && foreground.isStateful() && foreground.hasFocusStateSpecified())) ? false : true;
        if (!isInTouchMode() && getDefaultFocusHighlightEnabled() && lackFocusState && isAttachedToWindow() && sUseDefaultFocusHighlight) {
            return true;
        }
        return false;
    }

    private void switchDefaultFocusHighlight() {
        if (isFocused()) {
            boolean needed = this.mBackground;
            ForegroundInfo foregroundInfo = this.mForegroundInfo;
            needed = isDefaultFocusHighlightNeeded(needed, foregroundInfo == null ? null : foregroundInfo.mDrawable);
            boolean active = this.mDefaultFocusHighlight != null;
            if (needed && !active) {
                setDefaultFocusHighlight(getDefaultFocusHighlightDrawable());
            } else if (!needed && active) {
                setDefaultFocusHighlight(null);
            }
        }
    }

    private void drawDefaultFocusHighlight(Canvas canvas) {
        Drawable drawable = this.mDefaultFocusHighlight;
        if (drawable != null) {
            if (this.mDefaultFocusHighlightSizeChanged) {
                this.mDefaultFocusHighlightSizeChanged = false;
                int l = this.mScrollX;
                int r = (this.mRight + l) - this.mLeft;
                int t = this.mScrollY;
                drawable.setBounds(l, t, r, (this.mBottom + t) - this.mTop);
            }
            this.mDefaultFocusHighlight.draw(canvas);
        }
    }

    public final int[] getDrawableState() {
        int[] iArr = this.mDrawableState;
        if (iArr != null && (this.mPrivateFlags & 1024) == 0) {
            return iArr;
        }
        this.mDrawableState = onCreateDrawableState(0);
        this.mPrivateFlags &= -1025;
        return this.mDrawableState;
    }

    /* Access modifiers changed, original: protected */
    public int[] onCreateDrawableState(int extraSpace) {
        if (Extension.get().getExtension() != null) {
            return ((Interface) Extension.get().getExtension().asInterface()).onCreateDrawableState(this, extraSpace);
        }
        return originalOnCreateDrawableState(extraSpace);
    }

    /* Access modifiers changed, original: 0000 */
    public int[] originalOnCreateDrawableState(int extraSpace) {
        if ((this.mViewFlags & 4194304) == 4194304) {
            ViewParent viewParent = this.mParent;
            if (viewParent instanceof View) {
                return ((View) viewParent).onCreateDrawableState(extraSpace);
            }
        }
        int privateFlags = this.mPrivateFlags;
        int viewStateIndex = 0;
        if ((privateFlags & 16384) != 0) {
            viewStateIndex = 0 | 16;
        }
        if ((this.mViewFlags & 32) == 0) {
            viewStateIndex |= 8;
        }
        if (isFocused()) {
            viewStateIndex |= 4;
        }
        if ((privateFlags & 4) != 0) {
            viewStateIndex |= 2;
        }
        if (hasWindowFocus()) {
            viewStateIndex |= 1;
        }
        if ((1073741824 & privateFlags) != 0) {
            viewStateIndex |= 32;
        }
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null && attachInfo.mHardwareAccelerationRequested && ThreadedRenderer.isAvailable()) {
            viewStateIndex |= 64;
        }
        if ((268435456 & privateFlags) != 0) {
            viewStateIndex |= 128;
        }
        int privateFlags2 = this.mPrivateFlags2;
        if ((privateFlags2 & 1) != 0) {
            viewStateIndex |= 256;
        }
        if ((privateFlags2 & 2) != 0) {
            viewStateIndex |= 512;
        }
        int[] drawableState = StateSet.get(viewStateIndex);
        if (extraSpace == 0) {
            return drawableState;
        }
        int[] fullState;
        if (drawableState != null) {
            fullState = new int[(drawableState.length + extraSpace)];
            System.arraycopy(drawableState, 0, fullState, 0, drawableState.length);
        } else {
            fullState = new int[extraSpace];
        }
        return fullState;
    }

    protected static int[] mergeDrawableStates(int[] baseState, int[] additionalState) {
        int i = baseState.length - 1;
        while (i >= 0 && baseState[i] == 0) {
            i--;
        }
        System.arraycopy(additionalState, 0, baseState, i + 1, additionalState.length);
        return baseState;
    }

    public void jumpDrawablesToCurrentState() {
        Drawable drawable = this.mBackground;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
        StateListAnimator stateListAnimator = this.mStateListAnimator;
        if (stateListAnimator != null) {
            stateListAnimator.jumpToCurrentState();
        }
        drawable = this.mDefaultFocusHighlight;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
        ForegroundInfo foregroundInfo = this.mForegroundInfo;
        if (foregroundInfo != null && foregroundInfo.mDrawable != null) {
            this.mForegroundInfo.mDrawable.jumpToCurrentState();
        }
    }

    @RemotableViewMethod
    public void setBackgroundColor(int color) {
        Drawable drawable = this.mBackground;
        if (drawable instanceof ColorDrawable) {
            ((ColorDrawable) drawable.mutate()).setColor(color);
            computeOpaqueFlags();
            this.mBackgroundResource = 0;
            return;
        }
        setBackground(new ColorDrawable(color));
    }

    @RemotableViewMethod
    public void setBackgroundResource(int resid) {
        if (resid == 0 || resid != this.mBackgroundResource) {
            Drawable d = null;
            if (resid != 0) {
                d = this.mContext.getDrawable(resid);
            }
            setBackground(d);
            this.mBackgroundResource = resid;
        }
    }

    public void setBackground(Drawable background) {
        setBackgroundDrawable(background);
    }

    @Deprecated
    public void setBackgroundDrawable(Drawable background) {
        computeOpaqueFlags();
        Drawable drawable = this.mBackground;
        if (background != drawable) {
            boolean requestLayout = false;
            this.mBackgroundResource = 0;
            if (drawable != null) {
                if (isAttachedToWindow()) {
                    this.mBackground.setVisible(false, false);
                }
                this.mBackground.setCallback(null);
                unscheduleDrawable(this.mBackground);
            }
            if (background != null) {
                Rect padding = (Rect) sThreadLocal.get();
                if (padding == null) {
                    padding = new Rect();
                    sThreadLocal.set(padding);
                }
                resetResolvedDrawablesInternal();
                background.setLayoutDirection(getLayoutDirection());
                if (background.getPadding(padding)) {
                    resetResolvedPaddingInternal();
                    if (background.getLayoutDirection() != 1) {
                        this.mUserPaddingLeftInitial = padding.left;
                        this.mUserPaddingRightInitial = padding.right;
                        internalSetPadding(padding.left, padding.top, padding.right, padding.bottom);
                    } else {
                        this.mUserPaddingLeftInitial = padding.right;
                        this.mUserPaddingRightInitial = padding.left;
                        internalSetPadding(padding.right, padding.top, padding.left, padding.bottom);
                    }
                    this.mLeftPaddingDefined = false;
                    this.mRightPaddingDefined = false;
                }
                Drawable drawable2 = this.mBackground;
                if (!(drawable2 != null && drawable2.getMinimumHeight() == background.getMinimumHeight() && this.mBackground.getMinimumWidth() == background.getMinimumWidth())) {
                    requestLayout = true;
                }
                this.mBackground = background;
                if (background.isStateful()) {
                    background.setState(getDrawableState());
                }
                if (isAttachedToWindow()) {
                    boolean z = getWindowVisibility() == 0 && isShown();
                    background.setVisible(z, false);
                }
                applyBackgroundTint();
                background.setCallback(this);
                int i = this.mPrivateFlags;
                if ((i & 128) != 0) {
                    this.mPrivateFlags = i & -129;
                    requestLayout = true;
                }
            } else {
                this.mBackground = null;
                if ((this.mViewFlags & 128) != 0 && this.mDefaultFocusHighlight == null) {
                    ForegroundInfo foregroundInfo = this.mForegroundInfo;
                    if (foregroundInfo == null || foregroundInfo.mDrawable == null) {
                        this.mPrivateFlags |= 128;
                    }
                }
                requestLayout = true;
            }
            computeOpaqueFlags();
            if (requestLayout) {
                requestLayout();
            }
            this.mBackgroundSizeChanged = true;
            invalidate(true);
            invalidateOutline();
        }
    }

    public Drawable getBackground() {
        return this.mBackground;
    }

    public void setBackgroundTintList(ColorStateList tint) {
        if (this.mBackgroundTint == null) {
            this.mBackgroundTint = new TintInfo();
        }
        TintInfo tintInfo = this.mBackgroundTint;
        tintInfo.mTintList = tint;
        tintInfo.mHasTintList = true;
        applyBackgroundTint();
    }

    public ColorStateList getBackgroundTintList() {
        TintInfo tintInfo = this.mBackgroundTint;
        return tintInfo != null ? tintInfo.mTintList : null;
    }

    public void setBackgroundTintMode(Mode tintMode) {
        BlendMode mode = null;
        if (tintMode != null) {
            mode = BlendMode.fromValue(tintMode.nativeInt);
        }
        setBackgroundTintBlendMode(mode);
    }

    public void setBackgroundTintBlendMode(BlendMode blendMode) {
        if (this.mBackgroundTint == null) {
            this.mBackgroundTint = new TintInfo();
        }
        TintInfo tintInfo = this.mBackgroundTint;
        tintInfo.mBlendMode = blendMode;
        tintInfo.mHasTintMode = true;
        applyBackgroundTint();
    }

    public Mode getBackgroundTintMode() {
        TintInfo tintInfo = this.mBackgroundTint;
        if (tintInfo == null || tintInfo.mBlendMode == null) {
            return null;
        }
        return BlendMode.blendModeToPorterDuffMode(this.mBackgroundTint.mBlendMode);
    }

    public BlendMode getBackgroundTintBlendMode() {
        TintInfo tintInfo = this.mBackgroundTint;
        return tintInfo != null ? tintInfo.mBlendMode : null;
    }

    private void applyBackgroundTint() {
        if (this.mBackground != null && this.mBackgroundTint != null) {
            TintInfo tintInfo = this.mBackgroundTint;
            if (tintInfo.mHasTintList || tintInfo.mHasTintMode) {
                this.mBackground = this.mBackground.mutate();
                if (tintInfo.mHasTintList) {
                    this.mBackground.setTintList(tintInfo.mTintList);
                }
                if (tintInfo.mHasTintMode) {
                    this.mBackground.setTintBlendMode(tintInfo.mBlendMode);
                }
                if (this.mBackground.isStateful()) {
                    this.mBackground.setState(getDrawableState());
                }
            }
        }
    }

    public Drawable getForeground() {
        ForegroundInfo foregroundInfo = this.mForegroundInfo;
        return foregroundInfo != null ? foregroundInfo.mDrawable : null;
    }

    public void setForeground(Drawable foreground) {
        if (this.mForegroundInfo == null) {
            if (foreground != null) {
                this.mForegroundInfo = new ForegroundInfo();
            } else {
                return;
            }
        }
        if (foreground != this.mForegroundInfo.mDrawable) {
            if (this.mForegroundInfo.mDrawable != null) {
                if (isAttachedToWindow()) {
                    this.mForegroundInfo.mDrawable.setVisible(false, false);
                }
                this.mForegroundInfo.mDrawable.setCallback(null);
                unscheduleDrawable(this.mForegroundInfo.mDrawable);
            }
            this.mForegroundInfo.mDrawable = foreground;
            boolean z = true;
            this.mForegroundInfo.mBoundsChanged = true;
            if (foreground != null) {
                int i = this.mPrivateFlags;
                if ((i & 128) != 0) {
                    this.mPrivateFlags = i & -129;
                }
                foreground.setLayoutDirection(getLayoutDirection());
                if (foreground.isStateful()) {
                    foreground.setState(getDrawableState());
                }
                applyForegroundTint();
                if (isAttachedToWindow()) {
                    if (!(getWindowVisibility() == 0 && isShown())) {
                        z = false;
                    }
                    foreground.setVisible(z, false);
                }
                foreground.setCallback(this);
            } else if ((this.mViewFlags & 128) != 0 && this.mBackground == null && this.mDefaultFocusHighlight == null) {
                this.mPrivateFlags |= 128;
            }
            requestLayout();
            invalidate();
        }
    }

    public boolean isForegroundInsidePadding() {
        ForegroundInfo foregroundInfo = this.mForegroundInfo;
        return foregroundInfo != null ? foregroundInfo.mInsidePadding : true;
    }

    public int getForegroundGravity() {
        ForegroundInfo foregroundInfo = this.mForegroundInfo;
        if (foregroundInfo != null) {
            return foregroundInfo.mGravity;
        }
        return 8388659;
    }

    public void setForegroundGravity(int gravity) {
        if (this.mForegroundInfo == null) {
            this.mForegroundInfo = new ForegroundInfo();
        }
        if (this.mForegroundInfo.mGravity != gravity) {
            if ((Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK & gravity) == 0) {
                gravity |= Gravity.START;
            }
            if ((gravity & 112) == 0) {
                gravity |= 48;
            }
            this.mForegroundInfo.mGravity = gravity;
            requestLayout();
        }
    }

    public void setForegroundTintList(ColorStateList tint) {
        if (this.mForegroundInfo == null) {
            this.mForegroundInfo = new ForegroundInfo();
        }
        if (this.mForegroundInfo.mTintInfo == null) {
            this.mForegroundInfo.mTintInfo = new TintInfo();
        }
        this.mForegroundInfo.mTintInfo.mTintList = tint;
        this.mForegroundInfo.mTintInfo.mHasTintList = true;
        applyForegroundTint();
    }

    public ColorStateList getForegroundTintList() {
        ForegroundInfo foregroundInfo = this.mForegroundInfo;
        return (foregroundInfo == null || foregroundInfo.mTintInfo == null) ? null : this.mForegroundInfo.mTintInfo.mTintList;
    }

    public void setForegroundTintMode(Mode tintMode) {
        BlendMode mode = null;
        if (tintMode != null) {
            mode = BlendMode.fromValue(tintMode.nativeInt);
        }
        setForegroundTintBlendMode(mode);
    }

    public void setForegroundTintBlendMode(BlendMode blendMode) {
        if (this.mForegroundInfo == null) {
            this.mForegroundInfo = new ForegroundInfo();
        }
        if (this.mForegroundInfo.mTintInfo == null) {
            this.mForegroundInfo.mTintInfo = new TintInfo();
        }
        this.mForegroundInfo.mTintInfo.mBlendMode = blendMode;
        this.mForegroundInfo.mTintInfo.mHasTintMode = true;
        applyForegroundTint();
    }

    public Mode getForegroundTintMode() {
        ForegroundInfo foregroundInfo = this.mForegroundInfo;
        BlendMode blendMode = (foregroundInfo == null || foregroundInfo.mTintInfo == null) ? null : this.mForegroundInfo.mTintInfo.mBlendMode;
        if (blendMode != null) {
            return BlendMode.blendModeToPorterDuffMode(blendMode);
        }
        return null;
    }

    public BlendMode getForegroundTintBlendMode() {
        ForegroundInfo foregroundInfo = this.mForegroundInfo;
        return (foregroundInfo == null || foregroundInfo.mTintInfo == null) ? null : this.mForegroundInfo.mTintInfo.mBlendMode;
    }

    private void applyForegroundTint() {
        ForegroundInfo foregroundInfo = this.mForegroundInfo;
        if (foregroundInfo != null && foregroundInfo.mDrawable != null && this.mForegroundInfo.mTintInfo != null) {
            TintInfo tintInfo = this.mForegroundInfo.mTintInfo;
            if (tintInfo.mHasTintList || tintInfo.mHasTintMode) {
                ForegroundInfo foregroundInfo2 = this.mForegroundInfo;
                foregroundInfo2.mDrawable = foregroundInfo2.mDrawable.mutate();
                if (tintInfo.mHasTintList) {
                    this.mForegroundInfo.mDrawable.setTintList(tintInfo.mTintList);
                }
                if (tintInfo.mHasTintMode) {
                    this.mForegroundInfo.mDrawable.setTintBlendMode(tintInfo.mBlendMode);
                }
                if (this.mForegroundInfo.mDrawable.isStateful()) {
                    this.mForegroundInfo.mDrawable.setState(getDrawableState());
                }
            }
        }
    }

    private Drawable getAutofilledDrawable() {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo == null) {
            return null;
        }
        if (attachInfo.mAutofilledDrawable == null) {
            Context rootContext = getRootView().getContext();
            TypedArray a = rootContext.getTheme().obtainStyledAttributes(AUTOFILL_HIGHLIGHT_ATTR);
            int attributeResourceId = a.getResourceId(0, 0);
            this.mAttachInfo.mAutofilledDrawable = rootContext.getDrawable(attributeResourceId);
            a.recycle();
        }
        return this.mAttachInfo.mAutofilledDrawable;
    }

    private void drawAutofilledHighlight(Canvas canvas) {
        if (isAutofilled()) {
            Drawable autofilledHighlight = getAutofilledDrawable();
            if (autofilledHighlight != null) {
                autofilledHighlight.setBounds(0, 0, getWidth(), getHeight());
                autofilledHighlight.draw(canvas);
            }
        }
    }

    public void onDrawForeground(Canvas canvas) {
        onDrawScrollIndicators(canvas);
        onDrawScrollBars(canvas);
        ForegroundInfo foregroundInfo = this.mForegroundInfo;
        Drawable foreground = foregroundInfo != null ? foregroundInfo.mDrawable : null;
        if (foreground != null) {
            if (this.mForegroundInfo.mBoundsChanged) {
                this.mForegroundInfo.mBoundsChanged = false;
                Rect selfBounds = this.mForegroundInfo.mSelfBounds;
                Rect overlayBounds = this.mForegroundInfo.mOverlayBounds;
                if (this.mForegroundInfo.mInsidePadding) {
                    selfBounds.set(0, 0, getWidth(), getHeight());
                } else {
                    selfBounds.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
                }
                Gravity.apply(this.mForegroundInfo.mGravity, foreground.getIntrinsicWidth(), foreground.getIntrinsicHeight(), selfBounds, overlayBounds, getLayoutDirection());
                foreground.setBounds(overlayBounds);
            }
            foreground.draw(canvas);
        }
    }

    public void setPadding(int left, int top, int right, int bottom) {
        resetResolvedPaddingInternal();
        this.mUserPaddingStart = Integer.MIN_VALUE;
        this.mUserPaddingEnd = Integer.MIN_VALUE;
        this.mUserPaddingLeftInitial = left;
        this.mUserPaddingRightInitial = right;
        this.mLeftPaddingDefined = true;
        this.mRightPaddingDefined = true;
        internalSetPadding(left, top, right, bottom);
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123768420)
    public void internalSetPadding(int left, int top, int right, int bottom) {
        this.mUserPaddingLeft = left;
        this.mUserPaddingRight = right;
        this.mUserPaddingBottom = bottom;
        int viewFlags = this.mViewFlags;
        boolean changed = false;
        if ((viewFlags & 768) != 0) {
            int i = 0;
            if ((viewFlags & 512) != 0) {
                int offset = (viewFlags & 16777216) == 0 ? 0 : getVerticalScrollbarWidth();
                int i2 = this.mVerticalScrollbarPosition;
                if (i2 != 0) {
                    if (i2 == 1) {
                        left += offset;
                    } else if (i2 == 2) {
                        right += offset;
                    }
                } else if (isLayoutRtl()) {
                    left += offset;
                } else {
                    right += offset;
                }
            }
            if ((viewFlags & 256) != 0) {
                if ((viewFlags & 16777216) != 0) {
                    i = getHorizontalScrollbarHeight();
                }
                bottom += i;
            }
        }
        if (this.mPaddingLeft != left) {
            changed = true;
            this.mPaddingLeft = left;
        }
        if (this.mPaddingTop != top) {
            changed = true;
            this.mPaddingTop = top;
        }
        if (this.mPaddingRight != right) {
            changed = true;
            this.mPaddingRight = right;
        }
        if (this.mPaddingBottom != bottom) {
            changed = true;
            this.mPaddingBottom = bottom;
        }
        if (changed) {
            requestLayout();
            invalidateOutline();
        }
    }

    public void setPaddingRelative(int start, int top, int end, int bottom) {
        resetResolvedPaddingInternal();
        this.mUserPaddingStart = start;
        this.mUserPaddingEnd = end;
        this.mLeftPaddingDefined = true;
        this.mRightPaddingDefined = true;
        if (getLayoutDirection() != 1) {
            this.mUserPaddingLeftInitial = start;
            this.mUserPaddingRightInitial = end;
            internalSetPadding(start, top, end, bottom);
            return;
        }
        this.mUserPaddingLeftInitial = end;
        this.mUserPaddingRightInitial = start;
        internalSetPadding(end, top, start, bottom);
    }

    public int getSourceLayoutResId() {
        return this.mSourceLayoutId;
    }

    public int getPaddingTop() {
        return this.mPaddingTop;
    }

    public int getPaddingBottom() {
        return this.mPaddingBottom;
    }

    public int getPaddingLeft() {
        if (!isPaddingResolved()) {
            resolvePadding();
        }
        return this.mPaddingLeft;
    }

    public int getPaddingStart() {
        if (!isPaddingResolved()) {
            resolvePadding();
        }
        return getLayoutDirection() == 1 ? this.mPaddingRight : this.mPaddingLeft;
    }

    public int getPaddingRight() {
        if (!isPaddingResolved()) {
            resolvePadding();
        }
        return this.mPaddingRight;
    }

    public int getPaddingEnd() {
        if (!isPaddingResolved()) {
            resolvePadding();
        }
        return getLayoutDirection() == 1 ? this.mPaddingLeft : this.mPaddingRight;
    }

    public boolean isPaddingRelative() {
        return (this.mUserPaddingStart == Integer.MIN_VALUE && this.mUserPaddingEnd == Integer.MIN_VALUE) ? false : true;
    }

    /* Access modifiers changed, original: 0000 */
    public Insets computeOpticalInsets() {
        Drawable drawable = this.mBackground;
        return drawable == null ? Insets.NONE : drawable.getOpticalInsets();
    }

    @UnsupportedAppUsage
    public void resetPaddingToInitialValues() {
        if (isRtlCompatibilityMode()) {
            this.mPaddingLeft = this.mUserPaddingLeftInitial;
            this.mPaddingRight = this.mUserPaddingRightInitial;
            return;
        }
        int i;
        if (isLayoutRtl()) {
            i = this.mUserPaddingEnd;
            if (i < 0) {
                i = this.mUserPaddingLeftInitial;
            }
            this.mPaddingLeft = i;
            i = this.mUserPaddingStart;
            if (i < 0) {
                i = this.mUserPaddingRightInitial;
            }
            this.mPaddingRight = i;
        } else {
            i = this.mUserPaddingStart;
            if (i < 0) {
                i = this.mUserPaddingLeftInitial;
            }
            this.mPaddingLeft = i;
            i = this.mUserPaddingEnd;
            if (i < 0) {
                i = this.mUserPaddingRightInitial;
            }
            this.mPaddingRight = i;
        }
    }

    public Insets getOpticalInsets() {
        if (this.mLayoutInsets == null) {
            this.mLayoutInsets = computeOpticalInsets();
        }
        return this.mLayoutInsets;
    }

    public void setOpticalInsets(Insets insets) {
        this.mLayoutInsets = insets;
    }

    public void setSelected(boolean selected) {
        if (((this.mPrivateFlags & 4) != 0) != selected) {
            this.mPrivateFlags = (this.mPrivateFlags & -5) | (selected ? 4 : 0);
            if (!selected) {
                resetPressedState();
            }
            invalidate(true);
            refreshDrawableState();
            dispatchSetSelected(selected);
            if (selected) {
                sendAccessibilityEvent(4);
            } else {
                notifyViewAccessibilityStateChangedIfNeeded(0);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void dispatchSetSelected(boolean selected) {
    }

    @ExportedProperty
    public boolean isSelected() {
        return (this.mPrivateFlags & 4) != 0;
    }

    public void setActivated(boolean activated) {
        int i = 1073741824;
        if (((this.mPrivateFlags & 1073741824) != 0) != activated) {
            int i2 = this.mPrivateFlags & -1073741825;
            if (!activated) {
                i = 0;
            }
            this.mPrivateFlags = i2 | i;
            invalidate(true);
            refreshDrawableState();
            dispatchSetActivated(activated);
        }
    }

    /* Access modifiers changed, original: protected */
    public void dispatchSetActivated(boolean activated) {
    }

    @ExportedProperty
    public boolean isActivated() {
        return (this.mPrivateFlags & 1073741824) != 0;
    }

    public ViewTreeObserver getViewTreeObserver() {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            return attachInfo.mTreeObserver;
        }
        if (this.mFloatingTreeObserver == null) {
            this.mFloatingTreeObserver = new ViewTreeObserver(this.mContext);
        }
        return this.mFloatingTreeObserver;
    }

    public View getRootView() {
        View v = this.mAttachInfo;
        if (v != null) {
            v = v.mRootView;
            if (v != null) {
                return v;
            }
        }
        v = this;
        while (true) {
            ViewParent viewParent = v.mParent;
            if (viewParent == null || !(viewParent instanceof View)) {
                return v;
            }
            v = (View) viewParent;
        }
        return v;
    }

    @UnsupportedAppUsage
    public boolean toGlobalMotionEvent(MotionEvent ev) {
        AttachInfo info = this.mAttachInfo;
        if (info == null) {
            return false;
        }
        Matrix m = info.mTmpMatrix;
        m.set(Matrix.IDENTITY_MATRIX);
        transformMatrixToGlobal(m);
        ev.transform(m);
        return true;
    }

    @UnsupportedAppUsage
    public boolean toLocalMotionEvent(MotionEvent ev) {
        AttachInfo info = this.mAttachInfo;
        if (info == null) {
            return false;
        }
        Matrix m = info.mTmpMatrix;
        m.set(Matrix.IDENTITY_MATRIX);
        transformMatrixToLocal(m);
        ev.transform(m);
        return true;
    }

    public void transformMatrixToGlobal(Matrix matrix) {
        ViewParent parent = this.mParent;
        if (parent instanceof View) {
            View vp = (View) parent;
            vp.transformMatrixToGlobal(matrix);
            matrix.preTranslate((float) (-vp.mScrollX), (float) (-vp.mScrollY));
        } else if (parent instanceof ViewRootImpl) {
            ViewRootImpl vr = (ViewRootImpl) parent;
            vr.transformMatrixToGlobal(matrix);
            matrix.preTranslate(0.0f, (float) (-vr.mCurScrollY));
        }
        matrix.preTranslate((float) this.mLeft, (float) this.mTop);
        if (!hasIdentityMatrix()) {
            matrix.preConcat(getMatrix());
        }
    }

    public void transformMatrixToLocal(Matrix matrix) {
        ViewParent parent = this.mParent;
        if (parent instanceof View) {
            View vp = (View) parent;
            vp.transformMatrixToLocal(matrix);
            matrix.postTranslate((float) vp.mScrollX, (float) vp.mScrollY);
        } else if (parent instanceof ViewRootImpl) {
            ViewRootImpl vr = (ViewRootImpl) parent;
            vr.transformMatrixToLocal(matrix);
            matrix.postTranslate(0.0f, (float) vr.mCurScrollY);
        }
        matrix.postTranslate((float) (-this.mLeft), (float) (-this.mTop));
        if (!hasIdentityMatrix()) {
            matrix.postConcat(getInverseMatrix());
        }
    }

    @ExportedProperty(category = "layout", indexMapping = {@IntToString(from = 0, to = "x"), @IntToString(from = 1, to = "y")})
    @UnsupportedAppUsage
    public int[] getLocationOnScreen() {
        int[] location = new int[2];
        getLocationOnScreen(location);
        return location;
    }

    public void getLocationOnScreen(int[] outLocation) {
        getLocationInWindow(outLocation);
        AttachInfo info = this.mAttachInfo;
        if (info != null) {
            outLocation[0] = outLocation[0] + info.mWindowLeft;
            outLocation[1] = outLocation[1] + info.mWindowTop;
        }
    }

    public void getLocationInWindow(int[] outLocation) {
        if (outLocation == null || outLocation.length < 2) {
            throw new IllegalArgumentException("outLocation must be an array of two integers");
        }
        outLocation[0] = 0;
        outLocation[1] = 0;
        transformFromViewToWindowSpace(outLocation);
    }

    public void transformFromViewToWindowSpace(int[] inOutLocation) {
        if (inOutLocation == null || inOutLocation.length < 2) {
            throw new IllegalArgumentException("inOutLocation must be an array of two integers");
        }
        float[] position = this.mAttachInfo;
        if (position == null) {
            inOutLocation[1] = 0;
            inOutLocation[0] = 0;
            return;
        }
        position = position.mTmpTransformLocation;
        position[0] = (float) inOutLocation[0];
        position[1] = (float) inOutLocation[1];
        if (!hasIdentityMatrix()) {
            getMatrix().mapPoints(position);
        }
        position[0] = position[0] + ((float) this.mLeft);
        position[1] = position[1] + ((float) this.mTop);
        ViewParent viewParent = this.mParent;
        while (viewParent instanceof View) {
            View view = (View) viewParent;
            position[0] = position[0] - ((float) view.mScrollX);
            position[1] = position[1] - ((float) view.mScrollY);
            if (!view.hasIdentityMatrix()) {
                view.getMatrix().mapPoints(position);
            }
            position[0] = position[0] + ((float) view.mLeft);
            position[1] = position[1] + ((float) view.mTop);
            viewParent = view.mParent;
        }
        if (viewParent instanceof ViewRootImpl) {
            position[1] = position[1] - ((float) ((ViewRootImpl) viewParent).mCurScrollY);
        }
        inOutLocation[0] = Math.round(position[0]);
        inOutLocation[1] = Math.round(position[1]);
    }

    /* Access modifiers changed, original: protected */
    public <T extends View> T findViewTraversal(int id) {
        if (id == this.mID) {
            return this;
        }
        return null;
    }

    /* Access modifiers changed, original: protected */
    public <T extends View> T findViewWithTagTraversal(Object tag) {
        if (tag == null || !tag.equals(this.mTag)) {
            return null;
        }
        return this;
    }

    /* Access modifiers changed, original: protected */
    public <T extends View> T findViewByPredicateTraversal(Predicate<View> predicate, View childToSkip) {
        if (predicate.test(this)) {
            return this;
        }
        return null;
    }

    public final <T extends View> T findViewById(int id) {
        if (id == -1) {
            return null;
        }
        return findViewTraversal(id);
    }

    public final <T extends View> T requireViewById(int id) {
        T view = findViewById(id);
        if (view != null) {
            return view;
        }
        throw new IllegalArgumentException("ID does not reference a View inside this View");
    }

    public <T extends View> T findViewByAccessibilityIdTraversal(int accessibilityId) {
        if (getAccessibilityViewId() == accessibilityId) {
            return this;
        }
        return null;
    }

    public <T extends View> T findViewByAutofillIdTraversal(int autofillId) {
        if (getAutofillViewId() == autofillId) {
            return this;
        }
        return null;
    }

    public final <T extends View> T findViewWithTag(Object tag) {
        if (tag == null) {
            return null;
        }
        return findViewWithTagTraversal(tag);
    }

    public final <T extends View> T findViewByPredicate(Predicate<View> predicate) {
        return findViewByPredicateTraversal(predicate, null);
    }

    public final <T extends View> T findViewByPredicateInsideOut(View start, Predicate<View> predicate) {
        View childToSkip = null;
        while (true) {
            T view = start.findViewByPredicateTraversal(predicate, childToSkip);
            if (view != null || start == this) {
                return view;
            }
            ViewParent parent = start.getParent();
            if (parent != null && (parent instanceof View)) {
                childToSkip = start;
                start = (View) parent;
            }
        }
        return null;
    }

    public void setId(int id) {
        this.mID = id;
        if (this.mID == -1 && this.mLabelForId != -1) {
            this.mID = generateViewId();
        }
    }

    public void setIsRootNamespace(boolean isRoot) {
        if (isRoot) {
            this.mPrivateFlags |= 8;
        } else {
            this.mPrivateFlags &= -9;
        }
    }

    @UnsupportedAppUsage
    public boolean isRootNamespace() {
        return (this.mPrivateFlags & 8) != 0;
    }

    @CapturedViewProperty
    public int getId() {
        return this.mID;
    }

    public long getUniqueDrawingId() {
        return this.mRenderNode.getUniqueId();
    }

    @ExportedProperty
    public Object getTag() {
        return this.mTag;
    }

    public void setTag(Object tag) {
        this.mTag = tag;
    }

    public Object getTag(int key) {
        SparseArray sparseArray = this.mKeyedTags;
        if (sparseArray != null) {
            return sparseArray.get(key);
        }
        return null;
    }

    public void setTag(int key, Object tag) {
        if ((key >>> 24) >= 2) {
            setKeyedTag(key, tag);
            return;
        }
        throw new IllegalArgumentException("The key must be an application-specific resource id.");
    }

    @UnsupportedAppUsage
    public void setTagInternal(int key, Object tag) {
        if ((key >>> 24) == 1) {
            setKeyedTag(key, tag);
            return;
        }
        throw new IllegalArgumentException("The key must be a framework-specific resource id.");
    }

    private void setKeyedTag(int key, Object tag) {
        if (this.mKeyedTags == null) {
            this.mKeyedTags = new SparseArray(2);
        }
        this.mKeyedTags.put(key, tag);
    }

    @UnsupportedAppUsage
    public void debug() {
        debug(0);
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public void debug(int depth) {
        StringBuilder stringBuilder;
        String output = debugIndent(depth - 1);
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(output);
        stringBuilder2.append("+ ");
        stringBuilder2.append(this);
        output = stringBuilder2.toString();
        int id = getId();
        String str = ")";
        if (id != -1) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(output);
            stringBuilder3.append(" (id=");
            stringBuilder3.append(id);
            stringBuilder3.append(str);
            output = stringBuilder3.toString();
        }
        Object tag = getTag();
        if (tag != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(output);
            stringBuilder.append(" (tag=");
            stringBuilder.append(tag);
            stringBuilder.append(str);
            output = stringBuilder.toString();
        }
        str = VIEW_LOG_TAG;
        Log.d(str, output);
        if ((this.mPrivateFlags & 2) != 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(debugIndent(depth));
            stringBuilder.append(" FOCUSED");
            Log.d(str, stringBuilder.toString());
        }
        output = debugIndent(depth);
        stringBuilder = new StringBuilder();
        stringBuilder.append(output);
        stringBuilder.append("frame={");
        stringBuilder.append(this.mLeft);
        String str2 = ", ";
        stringBuilder.append(str2);
        stringBuilder.append(this.mTop);
        stringBuilder.append(str2);
        stringBuilder.append(this.mRight);
        stringBuilder.append(str2);
        stringBuilder.append(this.mBottom);
        stringBuilder.append("} scroll={");
        stringBuilder.append(this.mScrollX);
        stringBuilder.append(str2);
        stringBuilder.append(this.mScrollY);
        stringBuilder.append("} ");
        Log.d(str, stringBuilder.toString());
        String str3 = "}";
        if (!(this.mPaddingLeft == 0 && this.mPaddingTop == 0 && this.mPaddingRight == 0 && this.mPaddingBottom == 0)) {
            output = debugIndent(depth);
            stringBuilder = new StringBuilder();
            stringBuilder.append(output);
            stringBuilder.append("padding={");
            stringBuilder.append(this.mPaddingLeft);
            stringBuilder.append(str2);
            stringBuilder.append(this.mPaddingTop);
            stringBuilder.append(str2);
            stringBuilder.append(this.mPaddingRight);
            stringBuilder.append(str2);
            stringBuilder.append(this.mPaddingBottom);
            stringBuilder.append(str3);
            Log.d(str, stringBuilder.toString());
        }
        output = debugIndent(depth);
        stringBuilder = new StringBuilder();
        stringBuilder.append(output);
        stringBuilder.append("mMeasureWidth=");
        stringBuilder.append(this.mMeasuredWidth);
        stringBuilder.append(" mMeasureHeight=");
        stringBuilder.append(this.mMeasuredHeight);
        Log.d(str, stringBuilder.toString());
        output = debugIndent(depth);
        LayoutParams layoutParams = this.mLayoutParams;
        if (layoutParams == null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(output);
            stringBuilder.append("BAD! no layout params");
            output = stringBuilder.toString();
        } else {
            output = layoutParams.debug(output);
        }
        Log.d(str, output);
        output = debugIndent(depth);
        stringBuilder = new StringBuilder();
        stringBuilder.append(output);
        stringBuilder.append("flags={");
        output = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append(output);
        stringBuilder.append(printFlags(this.mViewFlags));
        output = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append(output);
        stringBuilder.append(str3);
        Log.d(str, stringBuilder.toString());
        output = debugIndent(depth);
        stringBuilder = new StringBuilder();
        stringBuilder.append(output);
        stringBuilder.append("privateFlags={");
        output = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append(output);
        stringBuilder.append(printPrivateFlags(this.mPrivateFlags));
        output = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append(output);
        stringBuilder.append(str3);
        Log.d(str, stringBuilder.toString());
    }

    protected static String debugIndent(int depth) {
        StringBuilder spaces = new StringBuilder(((depth * 2) + 3) * 2);
        for (int i = 0; i < (depth * 2) + 3; i++) {
            spaces.append(' ');
            spaces.append(' ');
        }
        return spaces.toString();
    }

    @ExportedProperty(category = "layout")
    public int getBaseline() {
        return -1;
    }

    public boolean isInLayout() {
        ViewRootImpl viewRoot = getViewRootImpl();
        return viewRoot != null && viewRoot.isInLayout();
    }

    public void requestLayout() {
        LongSparseLongArray longSparseLongArray = this.mMeasureCache;
        if (longSparseLongArray != null) {
            longSparseLongArray.clear();
        }
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null && attachInfo.mViewRequestingLayout == null) {
            ViewRootImpl viewRoot = getViewRootImpl();
            if (viewRoot == null || !viewRoot.isInLayout() || viewRoot.requestLayoutDuringLayout(this)) {
                this.mAttachInfo.mViewRequestingLayout = this;
            } else {
                return;
            }
        }
        this.mPrivateFlags |= 4096;
        this.mPrivateFlags |= Integer.MIN_VALUE;
        ViewParent viewParent = this.mParent;
        if (!(viewParent == null || viewParent.isLayoutRequested())) {
            this.mParent.requestLayout();
        }
        attachInfo = this.mAttachInfo;
        if (attachInfo != null && attachInfo.mViewRequestingLayout == this) {
            this.mAttachInfo.mViewRequestingLayout = null;
        }
    }

    public void forceLayout() {
        LongSparseLongArray longSparseLongArray = this.mMeasureCache;
        if (longSparseLongArray != null) {
            longSparseLongArray.clear();
        }
        this.mPrivateFlags |= 4096;
        this.mPrivateFlags |= Integer.MIN_VALUE;
    }

    /* JADX WARNING: Removed duplicated region for block: B:62:0x010c  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x00ee  */
    public final void measure(int r20, int r21) {
        /*
        r19 = this;
        r0 = r19;
        r1 = isLayoutModeOptical(r19);
        r2 = r0.mParent;
        r2 = isLayoutModeOptical(r2);
        if (r1 == r2) goto L_0x0033;
    L_0x000e:
        r2 = r19.getOpticalInsets();
        r3 = r2.left;
        r4 = r2.right;
        r3 = r3 + r4;
        r4 = r2.top;
        r5 = r2.bottom;
        r4 = r4 + r5;
        if (r1 == 0) goto L_0x0020;
    L_0x001e:
        r5 = -r3;
        goto L_0x0021;
    L_0x0020:
        r5 = r3;
    L_0x0021:
        r6 = r20;
        r5 = android.view.View.MeasureSpec.adjust(r6, r5);
        if (r1 == 0) goto L_0x002b;
    L_0x0029:
        r6 = -r4;
        goto L_0x002c;
    L_0x002b:
        r6 = r4;
    L_0x002c:
        r7 = r21;
        r6 = android.view.View.MeasureSpec.adjust(r7, r6);
        goto L_0x0039;
    L_0x0033:
        r6 = r20;
        r7 = r21;
        r5 = r6;
        r6 = r7;
    L_0x0039:
        r2 = (long) r5;
        r4 = 32;
        r2 = r2 << r4;
        r7 = (long) r6;
        r9 = 4294967295; // 0xffffffff float:NaN double:2.1219957905E-314;
        r7 = r7 & r9;
        r2 = r2 | r7;
        r7 = r0.mMeasureCache;
        if (r7 != 0) goto L_0x0051;
    L_0x0049:
        r7 = new android.util.LongSparseLongArray;
        r8 = 2;
        r7.<init>(r8);
        r0.mMeasureCache = r7;
    L_0x0051:
        r7 = r0.mPrivateFlags;
        r8 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r7 = r7 & r8;
        r11 = 1;
        r12 = 0;
        if (r7 != r8) goto L_0x005c;
    L_0x005a:
        r7 = r11;
        goto L_0x005d;
    L_0x005c:
        r7 = r12;
    L_0x005d:
        r8 = r0.mOldWidthMeasureSpec;
        if (r5 != r8) goto L_0x0068;
    L_0x0061:
        r8 = r0.mOldHeightMeasureSpec;
        if (r6 == r8) goto L_0x0066;
    L_0x0065:
        goto L_0x0068;
    L_0x0066:
        r8 = r12;
        goto L_0x0069;
    L_0x0068:
        r8 = r11;
    L_0x0069:
        r13 = android.view.View.MeasureSpec.getMode(r5);
        r14 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r13 != r14) goto L_0x0079;
    L_0x0071:
        r13 = android.view.View.MeasureSpec.getMode(r6);
        if (r13 != r14) goto L_0x0079;
    L_0x0077:
        r13 = r11;
        goto L_0x007a;
    L_0x0079:
        r13 = r12;
    L_0x007a:
        r14 = r19.getMeasuredWidth();
        r15 = android.view.View.MeasureSpec.getSize(r5);
        if (r14 != r15) goto L_0x0090;
    L_0x0084:
        r14 = r19.getMeasuredHeight();
        r15 = android.view.View.MeasureSpec.getSize(r6);
        if (r14 != r15) goto L_0x0090;
    L_0x008e:
        r14 = r11;
        goto L_0x0091;
    L_0x0090:
        r14 = r12;
    L_0x0091:
        if (r8 == 0) goto L_0x009c;
    L_0x0093:
        r15 = sAlwaysRemeasureExactly;
        if (r15 != 0) goto L_0x009b;
    L_0x0097:
        if (r13 == 0) goto L_0x009b;
    L_0x0099:
        if (r14 != 0) goto L_0x009c;
    L_0x009b:
        goto L_0x009d;
    L_0x009c:
        r11 = r12;
    L_0x009d:
        if (r7 != 0) goto L_0x00a6;
    L_0x009f:
        if (r11 == 0) goto L_0x00a2;
    L_0x00a1:
        goto L_0x00a6;
    L_0x00a2:
        r15 = r7;
        r16 = r8;
        goto L_0x00f2;
    L_0x00a6:
        r12 = r0.mPrivateFlags;
        r12 = r12 & -2049;
        r0.mPrivateFlags = r12;
        r19.resolveRtlPropertiesIfNeeded();
        if (r7 == 0) goto L_0x00b3;
    L_0x00b1:
        r12 = -1;
        goto L_0x00b9;
    L_0x00b3:
        r12 = r0.mMeasureCache;
        r12 = r12.indexOfKey(r2);
    L_0x00b9:
        if (r12 < 0) goto L_0x00da;
    L_0x00bb:
        r15 = sIgnoreMeasureCache;
        if (r15 == 0) goto L_0x00c3;
    L_0x00bf:
        r15 = r7;
        r16 = r8;
        goto L_0x00dd;
    L_0x00c3:
        r15 = r0.mMeasureCache;
        r9 = r15.valueAt(r12);
        r15 = r7;
        r16 = r8;
        r7 = r9 >> r4;
        r7 = (int) r7;
        r8 = (int) r9;
        r0.setMeasuredDimensionRaw(r7, r8);
        r7 = r0.mPrivateFlags3;
        r7 = r7 | 8;
        r0.mPrivateFlags3 = r7;
        goto L_0x00e6;
    L_0x00da:
        r15 = r7;
        r16 = r8;
    L_0x00dd:
        r0.onMeasure(r5, r6);
        r7 = r0.mPrivateFlags3;
        r7 = r7 & -9;
        r0.mPrivateFlags3 = r7;
    L_0x00e6:
        r7 = r0.mPrivateFlags;
        r8 = r7 & 2048;
        r9 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;
        if (r8 != r9) goto L_0x010c;
    L_0x00ee:
        r7 = r7 | 8192;
        r0.mPrivateFlags = r7;
    L_0x00f2:
        r0.mOldWidthMeasureSpec = r5;
        r0.mOldHeightMeasureSpec = r6;
        r7 = r0.mMeasureCache;
        r8 = r0.mMeasuredWidth;
        r8 = (long) r8;
        r8 = r8 << r4;
        r4 = r0.mMeasuredHeight;
        r10 = r1;
        r0 = (long) r4;
        r17 = 4294967295; // 0xffffffff float:NaN double:2.1219957905E-314;
        r0 = r0 & r17;
        r0 = r0 | r8;
        r7.put(r2, r0);
        return;
    L_0x010c:
        r10 = r1;
        r0 = new java.lang.IllegalStateException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r4 = "View with id ";
        r1.append(r4);
        r4 = r19.getId();
        r1.append(r4);
        r4 = ": ";
        r1.append(r4);
        r4 = r19.getClass();
        r4 = r4.getName();
        r1.append(r4);
        r4 = "#onMeasure() did not set the measured dimension by calling setMeasuredDimension()";
        r1.append(r4);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.View.measure(int, int):void");
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    /* Access modifiers changed, original: protected|final */
    public final void setMeasuredDimension(int measuredWidth, int measuredHeight) {
        boolean optical = isLayoutModeOptical(this);
        if (optical != isLayoutModeOptical(this.mParent)) {
            Insets insets = getOpticalInsets();
            int opticalWidth = insets.left + insets.right;
            int opticalHeight = insets.top + insets.bottom;
            measuredWidth += optical ? opticalWidth : -opticalWidth;
            measuredHeight += optical ? opticalHeight : -opticalHeight;
        }
        setMeasuredDimensionRaw(measuredWidth, measuredHeight);
    }

    private void setMeasuredDimensionRaw(int measuredWidth, int measuredHeight) {
        this.mMeasuredWidth = measuredWidth;
        this.mMeasuredHeight = measuredHeight;
        this.mPrivateFlags |= 2048;
    }

    public static int combineMeasuredStates(int curState, int newState) {
        return curState | newState;
    }

    public static int resolveSize(int size, int measureSpec) {
        return resolveSizeAndState(size, measureSpec, 0) & 16777215;
    }

    public static int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode != Integer.MIN_VALUE) {
            if (specMode != 1073741824) {
                result = size;
            } else {
                result = specSize;
            }
        } else if (specSize < size) {
            result = 16777216 | specSize;
        } else {
            result = size;
        }
        return (-16777216 & childMeasuredState) | result;
    }

    public static int getDefaultSize(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode != Integer.MIN_VALUE) {
            if (specMode == 0) {
                return size;
            }
            if (specMode != 1073741824) {
                return result;
            }
        }
        return specSize;
    }

    /* Access modifiers changed, original: protected */
    public int getSuggestedMinimumHeight() {
        Drawable drawable = this.mBackground;
        return drawable == null ? this.mMinHeight : Math.max(this.mMinHeight, drawable.getMinimumHeight());
    }

    /* Access modifiers changed, original: protected */
    public int getSuggestedMinimumWidth() {
        Drawable drawable = this.mBackground;
        return drawable == null ? this.mMinWidth : Math.max(this.mMinWidth, drawable.getMinimumWidth());
    }

    public int getMinimumHeight() {
        return this.mMinHeight;
    }

    @RemotableViewMethod
    public void setMinimumHeight(int minHeight) {
        this.mMinHeight = minHeight;
        requestLayout();
    }

    public int getMinimumWidth() {
        return this.mMinWidth;
    }

    public void setMinimumWidth(int minWidth) {
        this.mMinWidth = minWidth;
        requestLayout();
    }

    public Animation getAnimation() {
        return this.mCurrentAnimation;
    }

    public void startAnimation(Animation animation) {
        animation.setStartTime(-1);
        setAnimation(animation);
        invalidateParentCaches();
        invalidate(true);
    }

    public void clearAnimation() {
        Animation animation = this.mCurrentAnimation;
        if (animation != null) {
            animation.detach();
        }
        this.mCurrentAnimation = null;
        invalidateParentIfNeeded();
    }

    public void setAnimation(Animation animation) {
        this.mCurrentAnimation = animation;
        if (animation != null) {
            AttachInfo attachInfo = this.mAttachInfo;
            if (attachInfo != null && attachInfo.mDisplayState == 1 && animation.getStartTime() == -1) {
                animation.setStartTime(AnimationUtils.currentAnimationTimeMillis());
            }
            animation.reset();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onAnimationStart() {
        this.mPrivateFlags |= 65536;
    }

    /* Access modifiers changed, original: protected */
    public void onAnimationEnd() {
        this.mPrivateFlags &= -65537;
    }

    /* Access modifiers changed, original: protected */
    public boolean onSetAlpha(int alpha) {
        return false;
    }

    @UnsupportedAppUsage
    public boolean gatherTransparentRegion(Region region) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (!(region == null || attachInfo == null)) {
            if ((this.mPrivateFlags & 128) == 0) {
                int[] location = attachInfo.mTransparentLocation;
                getLocationInWindow(location);
                int shadowOffset = getZ() > 0.0f ? (int) getZ() : 0;
                region.op(location[0] - shadowOffset, location[1] - shadowOffset, ((location[0] + this.mRight) - this.mLeft) + shadowOffset, ((location[1] + this.mBottom) - this.mTop) + (shadowOffset * 3), Op.DIFFERENCE);
            } else {
                Drawable drawable = this.mBackground;
                if (!(drawable == null || drawable.getOpacity() == -2)) {
                    applyDrawableToTransparentRegion(this.mBackground, region);
                }
                ForegroundInfo foregroundInfo = this.mForegroundInfo;
                if (!(foregroundInfo == null || foregroundInfo.mDrawable == null || this.mForegroundInfo.mDrawable.getOpacity() == -2)) {
                    applyDrawableToTransparentRegion(this.mForegroundInfo.mDrawable, region);
                }
                drawable = this.mDefaultFocusHighlight;
                if (!(drawable == null || drawable.getOpacity() == -2)) {
                    applyDrawableToTransparentRegion(this.mDefaultFocusHighlight, region);
                }
            }
        }
        return true;
    }

    public void playSoundEffect(int soundConstant) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null && attachInfo.mRootCallbacks != null && isSoundEffectsEnabled()) {
            this.mAttachInfo.mRootCallbacks.playSoundEffect(soundConstant);
        }
    }

    public boolean performHapticFeedback(int feedbackConstant) {
        return performHapticFeedback(feedbackConstant, 0);
    }

    public boolean performHapticFeedback(int feedbackConstant, int flags) {
        boolean z = false;
        if (this.mAttachInfo == null) {
            return false;
        }
        if ((flags & 4) != 0 && !this.mHapticEnabledExplicitly) {
            return false;
        }
        if ((flags & 1) == 0 && !isHapticFeedbackEnabled()) {
            return false;
        }
        Callbacks callbacks = this.mAttachInfo.mRootCallbacks;
        if ((flags & 2) != 0) {
            z = true;
        }
        return callbacks.performHapticFeedback(feedbackConstant, z);
    }

    public void setSystemUiVisibility(int visibility) {
        if (visibility != this.mSystemUiVisibility) {
            this.mSystemUiVisibility = visibility;
            if (this.mParent != null) {
                AttachInfo attachInfo = this.mAttachInfo;
                if (attachInfo != null && !attachInfo.mRecomputeGlobalAttributes) {
                    this.mParent.recomputeViewAttributes(this);
                }
            }
        }
    }

    public int getSystemUiVisibility() {
        return this.mSystemUiVisibility;
    }

    public int getWindowSystemUiVisibility() {
        AttachInfo attachInfo = this.mAttachInfo;
        return attachInfo != null ? attachInfo.mSystemUiVisibility : 0;
    }

    public void onWindowSystemUiVisibilityChanged(int visible) {
    }

    public void dispatchWindowSystemUiVisiblityChanged(int visible) {
        onWindowSystemUiVisibilityChanged(visible);
    }

    public void setOnSystemUiVisibilityChangeListener(OnSystemUiVisibilityChangeListener l) {
        getListenerInfo().mOnSystemUiVisibilityChangeListener = l;
        if (this.mParent != null) {
            AttachInfo attachInfo = this.mAttachInfo;
            if (attachInfo != null && !attachInfo.mRecomputeGlobalAttributes) {
                this.mParent.recomputeViewAttributes(this);
            }
        }
    }

    public void dispatchSystemUiVisibilityChanged(int visibility) {
        ListenerInfo li = this.mListenerInfo;
        if (li != null && li.mOnSystemUiVisibilityChangeListener != null) {
            li.mOnSystemUiVisibilityChangeListener.onSystemUiVisibilityChange(visibility & PUBLIC_STATUS_BAR_VISIBILITY_MASK);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean updateLocalSystemUiVisibility(int localValue, int localChanges) {
        int i = this.mSystemUiVisibility;
        int val = ((~localChanges) & i) | (localValue & localChanges);
        if (val == i) {
            return false;
        }
        setSystemUiVisibility(val);
        return true;
    }

    @UnsupportedAppUsage
    public void setDisabledSystemUiVisibility(int flags) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null && attachInfo.mDisabledSystemUiVisibility != flags) {
            this.mAttachInfo.mDisabledSystemUiVisibility = flags;
            ViewParent viewParent = this.mParent;
            if (viewParent != null) {
                viewParent.recomputeViewAttributes(this);
            }
        }
    }

    @Deprecated
    public final boolean startDrag(ClipData data, DragShadowBuilder shadowBuilder, Object myLocalState, int flags) {
        return startDragAndDrop(data, shadowBuilder, myLocalState, flags);
    }

    /* JADX WARNING: Removed duplicated region for block: B:85:0x0179  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x0179  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0187  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0187  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x0179  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0187  */
    public final boolean startDragAndDrop(android.content.ClipData r24, android.view.View.DragShadowBuilder r25, java.lang.Object r26, int r27) {
        /*
        r23 = this;
        r1 = r23;
        r12 = r24;
        r13 = r25;
        r0 = r1.mAttachInfo;
        r14 = "View";
        r15 = 0;
        if (r0 != 0) goto L_0x0014;
    L_0x000d:
        r0 = "startDragAndDrop called on a detached view.";
        android.util.Log.w(r14, r0);
        return r15;
    L_0x0014:
        r0 = r0.mViewRootImpl;
        r0 = r0.mSurface;
        r0 = r0.isValid();
        if (r0 != 0) goto L_0x0025;
    L_0x001e:
        r0 = "startDragAndDrop called with an invalid surface.";
        android.util.Log.w(r14, r0);
        return r15;
    L_0x0025:
        r0 = 1;
        if (r12 == 0) goto L_0x0035;
    L_0x0028:
        r11 = r27;
        r2 = r11 & 256;
        if (r2 == 0) goto L_0x0030;
    L_0x002e:
        r2 = r0;
        goto L_0x0031;
    L_0x0030:
        r2 = r15;
    L_0x0031:
        r12.prepareToLeaveProcess(r2);
        goto L_0x0037;
    L_0x0035:
        r11 = r27;
    L_0x0037:
        r2 = new android.graphics.Point;
        r2.<init>();
        r10 = r2;
        r2 = new android.graphics.Point;
        r2.<init>();
        r9 = r2;
        r13.onProvideShadowMetrics(r10, r9);
        r2 = r10.x;
        if (r2 < 0) goto L_0x0199;
    L_0x004a:
        r2 = r10.y;
        if (r2 < 0) goto L_0x0199;
    L_0x004e:
        r2 = r9.x;
        if (r2 < 0) goto L_0x0199;
    L_0x0052:
        r2 = r9.y;
        if (r2 < 0) goto L_0x0199;
    L_0x0056:
        r2 = r10.x;
        if (r2 == 0) goto L_0x005e;
    L_0x005a:
        r2 = r10.y;
        if (r2 != 0) goto L_0x0066;
    L_0x005e:
        r2 = sAcceptZeroSizeDragShadow;
        if (r2 == 0) goto L_0x0191;
    L_0x0062:
        r10.x = r0;
        r10.y = r0;
    L_0x0066:
        r2 = r1.mAttachInfo;
        r8 = r2.mViewRootImpl;
        r2 = new android.view.SurfaceSession;
        r2.<init>();
        r7 = r2;
        r2 = new android.view.SurfaceControl$Builder;
        r2.<init>(r7);
        r3 = "drag surface";
        r2 = r2.setName(r3);
        r3 = r8.getSurfaceControl();
        r2 = r2.setParent(r3);
        r3 = r10.x;
        r4 = r10.y;
        r2 = r2.setBufferSize(r3, r4);
        r3 = -3;
        r2 = r2.setFormat(r3);
        r6 = r2.build();
        r2 = new android.view.Surface;
        r2.<init>();
        r5 = r2;
        r5.copyFrom(r6);
        r16 = 0;
        r2 = 0;
        r2 = r5.lockCanvas(r2);	 Catch:{ Exception -> 0x0161, all -> 0x0154 }
        r4 = r2;
        r2 = android.graphics.PorterDuff.Mode.CLEAR;	 Catch:{ all -> 0x013f }
        r4.drawColor(r15, r2);	 Catch:{ all -> 0x013f }
        r13.onDrawShadow(r4);	 Catch:{ all -> 0x013f }
        r5.unlockCanvasAndPost(r4);	 Catch:{ Exception -> 0x0161, all -> 0x0154 }
        r8.getLastTouchPoint(r10);	 Catch:{ Exception -> 0x0161, all -> 0x0154 }
        r2 = r1.mAttachInfo;	 Catch:{ Exception -> 0x0161, all -> 0x0154 }
        r2 = r2.mSession;	 Catch:{ Exception -> 0x0161, all -> 0x0154 }
        r3 = r1.mAttachInfo;	 Catch:{ Exception -> 0x0161, all -> 0x0154 }
        r3 = r3.mWindow;	 Catch:{ Exception -> 0x0161, all -> 0x0154 }
        r17 = r8.getLastTouchSource();	 Catch:{ Exception -> 0x0161, all -> 0x0154 }
        r0 = r10.x;	 Catch:{ Exception -> 0x0161, all -> 0x0154 }
        r0 = (float) r0;	 Catch:{ Exception -> 0x0161, all -> 0x0154 }
        r15 = r10.y;	 Catch:{ Exception -> 0x0161, all -> 0x0154 }
        r15 = (float) r15;	 Catch:{ Exception -> 0x0161, all -> 0x0154 }
        r19 = r4;
        r4 = r9.x;	 Catch:{ Exception -> 0x0161, all -> 0x0154 }
        r4 = (float) r4;	 Catch:{ Exception -> 0x0161, all -> 0x0154 }
        r20 = r4;
        r4 = r9.y;	 Catch:{ Exception -> 0x0161, all -> 0x0154 }
        r4 = (float) r4;
        r12 = r19;
        r19 = r20;
        r20 = r4;
        r4 = r27;
        r13 = r5;
        r5 = r6;
        r21 = r6;
        r6 = r17;
        r17 = r7;
        r7 = r0;
        r22 = r14;
        r14 = r8;
        r8 = r15;
        r15 = r9;
        r9 = r19;
        r19 = r10;
        r10 = r20;
        r11 = r24;
        r0 = r2.performDrag(r3, r4, r5, r6, r7, r8, r9, r10, r11);	 Catch:{ Exception -> 0x013b, all -> 0x0137 }
        r2 = r0;
        if (r2 == 0) goto L_0x0122;
    L_0x00f5:
        r0 = r1.mAttachInfo;	 Catch:{ Exception -> 0x011b, all -> 0x0114 }
        r0 = r0.mDragSurface;	 Catch:{ Exception -> 0x011b, all -> 0x0114 }
        if (r0 == 0) goto L_0x0102;
    L_0x00fb:
        r0 = r1.mAttachInfo;	 Catch:{ Exception -> 0x011b, all -> 0x0114 }
        r0 = r0.mDragSurface;	 Catch:{ Exception -> 0x011b, all -> 0x0114 }
        r0.release();	 Catch:{ Exception -> 0x011b, all -> 0x0114 }
    L_0x0102:
        r0 = r1.mAttachInfo;	 Catch:{ Exception -> 0x011b, all -> 0x0114 }
        r0.mDragSurface = r13;	 Catch:{ Exception -> 0x011b, all -> 0x0114 }
        r0 = r1.mAttachInfo;	 Catch:{ Exception -> 0x011b, all -> 0x0114 }
        r0.mDragToken = r2;	 Catch:{ Exception -> 0x011b, all -> 0x0114 }
        r3 = r26;
        r14.setLocalDragState(r3);	 Catch:{ Exception -> 0x0112, all -> 0x0110 }
        goto L_0x0124;
    L_0x0110:
        r0 = move-exception;
        goto L_0x0117;
    L_0x0112:
        r0 = move-exception;
        goto L_0x011e;
    L_0x0114:
        r0 = move-exception;
        r3 = r26;
    L_0x0117:
        r16 = r2;
        goto L_0x0185;
    L_0x011b:
        r0 = move-exception;
        r3 = r26;
    L_0x011e:
        r16 = r2;
        goto L_0x016f;
    L_0x0122:
        r3 = r26;
    L_0x0124:
        if (r2 == 0) goto L_0x0129;
    L_0x0126:
        r18 = 1;
        goto L_0x012b;
    L_0x0129:
        r18 = 0;
    L_0x012b:
        if (r2 != 0) goto L_0x0130;
    L_0x012d:
        r13.destroy();
    L_0x0130:
        r17.kill();
        r13.destroy();
        return r18;
    L_0x0137:
        r0 = move-exception;
        r3 = r26;
        goto L_0x0185;
    L_0x013b:
        r0 = move-exception;
        r3 = r26;
        goto L_0x016f;
    L_0x013f:
        r0 = move-exception;
        r3 = r26;
        r12 = r4;
        r13 = r5;
        r21 = r6;
        r17 = r7;
        r15 = r9;
        r19 = r10;
        r22 = r14;
        r14 = r8;
        r13.unlockCanvasAndPost(r12);	 Catch:{ Exception -> 0x0152 }
        throw r0;	 Catch:{ Exception -> 0x0152 }
    L_0x0152:
        r0 = move-exception;
        goto L_0x016f;
    L_0x0154:
        r0 = move-exception;
        r3 = r26;
        r13 = r5;
        r21 = r6;
        r17 = r7;
        r14 = r8;
        r15 = r9;
        r19 = r10;
        goto L_0x0185;
    L_0x0161:
        r0 = move-exception;
        r3 = r26;
        r13 = r5;
        r21 = r6;
        r17 = r7;
        r15 = r9;
        r19 = r10;
        r22 = r14;
        r14 = r8;
    L_0x016f:
        r2 = "Unable to initiate drag";
        r4 = r22;
        android.util.Log.e(r4, r2, r0);	 Catch:{ all -> 0x0184 }
        if (r16 != 0) goto L_0x017c;
    L_0x0179:
        r13.destroy();
    L_0x017c:
        r17.kill();
        r13.destroy();
        r2 = 0;
        return r2;
    L_0x0184:
        r0 = move-exception;
    L_0x0185:
        if (r16 != 0) goto L_0x018a;
    L_0x0187:
        r13.destroy();
    L_0x018a:
        r17.kill();
        r13.destroy();
        throw r0;
    L_0x0191:
        r0 = new java.lang.IllegalStateException;
        r2 = "Drag shadow dimensions must be positive";
        r0.<init>(r2);
        throw r0;
    L_0x0199:
        r3 = r26;
        r15 = r9;
        r19 = r10;
        r0 = new java.lang.IllegalStateException;
        r2 = "Drag shadow dimensions must not be negative";
        r0.<init>(r2);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.View.startDragAndDrop(android.content.ClipData, android.view.View$DragShadowBuilder, java.lang.Object, int):boolean");
    }

    public final void cancelDragAndDrop() {
        AttachInfo attachInfo = this.mAttachInfo;
        String str = VIEW_LOG_TAG;
        if (attachInfo == null) {
            Log.w(str, "cancelDragAndDrop called on a detached view.");
            return;
        }
        if (attachInfo.mDragToken != null) {
            try {
                this.mAttachInfo.mSession.cancelDragAndDrop(this.mAttachInfo.mDragToken, false);
            } catch (Exception e) {
                Log.e(str, "Unable to cancel drag", e);
            }
            this.mAttachInfo.mDragToken = null;
        } else {
            Log.e(str, "No active drag to cancel");
        }
    }

    public final void updateDragShadow(DragShadowBuilder shadowBuilder) {
        AttachInfo attachInfo = this.mAttachInfo;
        String str = VIEW_LOG_TAG;
        if (attachInfo == null) {
            Log.w(str, "updateDragShadow called on a detached view.");
            return;
        }
        if (attachInfo.mDragToken != null) {
            Canvas canvas;
            try {
                canvas = this.mAttachInfo.mDragSurface.lockCanvas(null);
                canvas.drawColor(0, Mode.CLEAR);
                shadowBuilder.onDrawShadow(canvas);
                this.mAttachInfo.mDragSurface.unlockCanvasAndPost(canvas);
            } catch (Exception e) {
                Log.e(str, "Unable to update drag shadow", e);
            } catch (Throwable th) {
                this.mAttachInfo.mDragSurface.unlockCanvasAndPost(canvas);
            }
        } else {
            Log.e(str, "No active drag");
        }
    }

    public final boolean startMovingTask(float startX, float startY) {
        try {
            return this.mAttachInfo.mSession.startMovingTask(this.mAttachInfo.mWindow, startX, startY);
        } catch (RemoteException e) {
            Log.e(VIEW_LOG_TAG, "Unable to start moving", e);
            return false;
        }
    }

    public void finishMovingTask() {
        try {
            this.mAttachInfo.mSession.finishMovingTask(this.mAttachInfo.mWindow);
        } catch (RemoteException e) {
            Log.e(VIEW_LOG_TAG, "Unable to finish moving", e);
        }
    }

    public boolean onDragEvent(DragEvent event) {
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean dispatchDragEnterExitInPreN(DragEvent event) {
        return callDragEventHandler(event);
    }

    public boolean dispatchDragEvent(DragEvent event) {
        event.mEventHandlerWasCalled = true;
        if (event.mAction == 2 || event.mAction == 3) {
            getViewRootImpl().setDragFocus(this, event);
        }
        return callDragEventHandler(event);
    }

    /* Access modifiers changed, original: final */
    public final boolean callDragEventHandler(DragEvent event) {
        boolean result;
        ListenerInfo li = this.mListenerInfo;
        if (li == null || li.mOnDragListener == null || (this.mViewFlags & 32) != 0 || !li.mOnDragListener.onDrag(this, event)) {
            result = onDragEvent(event);
        } else {
            result = true;
        }
        int i = event.mAction;
        if (i == 4) {
            this.mPrivateFlags2 &= -4;
            refreshDrawableState();
        } else if (i == 5) {
            this.mPrivateFlags2 |= 2;
            refreshDrawableState();
        } else if (i == 6) {
            this.mPrivateFlags2 &= -3;
            refreshDrawableState();
        }
        return result;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean canAcceptDrag() {
        return (this.mPrivateFlags2 & 1) != 0;
    }

    @UnsupportedAppUsage
    public void onCloseSystemDialogs(String reason) {
    }

    @UnsupportedAppUsage
    public void applyDrawableToTransparentRegion(Drawable dr, Region region) {
        Region r = dr.getTransparentRegion();
        Rect db = dr.getBounds();
        AttachInfo attachInfo = this.mAttachInfo;
        if (r == null || attachInfo == null) {
            region.op(db, Op.DIFFERENCE);
            return;
        }
        int w = getRight() - getLeft();
        int h = getBottom() - getTop();
        if (db.left > 0) {
            r.op(0, 0, db.left, h, Op.UNION);
        }
        if (db.right < w) {
            r.op(db.right, 0, w, h, Op.UNION);
        }
        if (db.top > 0) {
            r.op(0, 0, w, db.top, Op.UNION);
        }
        if (db.bottom < h) {
            r.op(0, db.bottom, w, h, Op.UNION);
        }
        int[] location = attachInfo.mTransparentLocation;
        getLocationInWindow(location);
        r.translate(location[0], location[1]);
        region.op(r, Op.INTERSECT);
    }

    private void checkForLongClick(long delay, float x, float y, int classification) {
        int i = this.mViewFlags;
        if ((i & 2097152) == 2097152 || (i & 1073741824) == 1073741824) {
            this.mHasPerformedLongPress = false;
            if (this.mPendingCheckForLongPress == null) {
                this.mPendingCheckForLongPress = new CheckForLongPress(this, null);
            }
            this.mPendingCheckForLongPress.setAnchor(x, y);
            this.mPendingCheckForLongPress.rememberWindowAttachCount();
            this.mPendingCheckForLongPress.rememberPressedState();
            this.mPendingCheckForLongPress.setClassification(classification);
            postDelayed(this.mPendingCheckForLongPress, delay);
        }
    }

    public static View inflate(Context context, int resource, ViewGroup root) {
        return LayoutInflater.from(context).inflate(resource, root);
    }

    /* Access modifiers changed, original: protected */
    public boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        int maxOverScrollX2;
        int maxOverScrollY2;
        boolean clampedX;
        boolean clampedY;
        int overScrollMode = this.mOverScrollMode;
        boolean canScrollHorizontal = computeHorizontalScrollRange() > computeHorizontalScrollExtent();
        boolean canScrollVertical = computeVerticalScrollRange() > computeVerticalScrollExtent();
        boolean overScrollHorizontal = overScrollMode == 0 || (overScrollMode == 1 && canScrollHorizontal);
        boolean overScrollVertical = overScrollMode == 0 || (overScrollMode == 1 && canScrollVertical);
        int newScrollX = scrollX + deltaX;
        if (overScrollHorizontal) {
            maxOverScrollX2 = maxOverScrollX;
        } else {
            maxOverScrollX2 = 0;
        }
        int newScrollY = scrollY + deltaY;
        if (overScrollVertical) {
            maxOverScrollY2 = maxOverScrollY;
        } else {
            maxOverScrollY2 = 0;
        }
        int left = -maxOverScrollX2;
        int right = maxOverScrollX2 + scrollRangeX;
        int top = -maxOverScrollY2;
        int bottom = maxOverScrollY2 + scrollRangeY;
        if (newScrollX > right) {
            newScrollX = right;
            clampedX = true;
        } else if (newScrollX < left) {
            newScrollX = left;
            clampedX = true;
        } else {
            clampedX = false;
        }
        if (newScrollY > bottom) {
            newScrollY = bottom;
            clampedY = true;
        } else if (newScrollY < top) {
            newScrollY = top;
            clampedY = true;
        } else {
            clampedY = false;
        }
        onOverScrolled(newScrollX, newScrollY, clampedX, clampedY);
        return clampedX || clampedY;
    }

    /* Access modifiers changed, original: protected */
    public void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
    }

    public int getOverScrollMode() {
        return this.mOverScrollMode;
    }

    public void setOverScrollMode(int overScrollMode) {
        if (overScrollMode == 0 || overScrollMode == 1 || overScrollMode == 2) {
            this.mOverScrollMode = overScrollMode;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid overscroll mode ");
        stringBuilder.append(overScrollMode);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public void setNestedScrollingEnabled(boolean enabled) {
        if (enabled) {
            this.mPrivateFlags3 |= 128;
            return;
        }
        stopNestedScroll();
        this.mPrivateFlags3 &= -129;
    }

    public boolean isNestedScrollingEnabled() {
        return (this.mPrivateFlags3 & 128) == 128;
    }

    public boolean startNestedScroll(int axes) {
        if (hasNestedScrollingParent()) {
            return true;
        }
        if (isNestedScrollingEnabled()) {
            View child = this;
            for (ViewParent p = getParent(); p != null; p = p.getParent()) {
                try {
                    if (p.onStartNestedScroll(child, this, axes)) {
                        this.mNestedScrollingParent = p;
                        p.onNestedScrollAccepted(child, this, axes);
                        return true;
                    }
                } catch (AbstractMethodError e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("ViewParent ");
                    stringBuilder.append(p);
                    stringBuilder.append(" does not implement interface method onStartNestedScroll");
                    Log.e(VIEW_LOG_TAG, stringBuilder.toString(), e);
                }
                if (p instanceof View) {
                    child = (View) p;
                }
            }
        }
        return false;
    }

    public void stopNestedScroll() {
        ViewParent viewParent = this.mNestedScrollingParent;
        if (viewParent != null) {
            viewParent.onStopNestedScroll(this);
            this.mNestedScrollingParent = null;
        }
    }

    public boolean hasNestedScrollingParent() {
        return this.mNestedScrollingParent != null;
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        if (isNestedScrollingEnabled() && this.mNestedScrollingParent != null) {
            if (dxConsumed != 0 || dyConsumed != 0 || dxUnconsumed != 0 || dyUnconsumed != 0) {
                int startX = 0;
                int startY = 0;
                if (offsetInWindow != null) {
                    getLocationInWindow(offsetInWindow);
                    startX = offsetInWindow[0];
                    startY = offsetInWindow[1];
                }
                this.mNestedScrollingParent.onNestedScroll(this, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
                if (offsetInWindow != null) {
                    getLocationInWindow(offsetInWindow);
                    offsetInWindow[0] = offsetInWindow[0] - startX;
                    offsetInWindow[1] = offsetInWindow[1] - startY;
                }
                return true;
            } else if (offsetInWindow != null) {
                offsetInWindow[0] = 0;
                offsetInWindow[1] = 0;
            }
        }
        return false;
    }

    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        if (isNestedScrollingEnabled() && this.mNestedScrollingParent != null) {
            boolean z = true;
            if (dx != 0 || dy != 0) {
                int startX = 0;
                int startY = 0;
                if (offsetInWindow != null) {
                    getLocationInWindow(offsetInWindow);
                    startX = offsetInWindow[0];
                    startY = offsetInWindow[1];
                }
                if (consumed == null) {
                    if (this.mTempNestedScrollConsumed == null) {
                        this.mTempNestedScrollConsumed = new int[2];
                    }
                    consumed = this.mTempNestedScrollConsumed;
                }
                consumed[0] = 0;
                consumed[1] = 0;
                this.mNestedScrollingParent.onNestedPreScroll(this, dx, dy, consumed);
                if (offsetInWindow != null) {
                    getLocationInWindow(offsetInWindow);
                    offsetInWindow[0] = offsetInWindow[0] - startX;
                    offsetInWindow[1] = offsetInWindow[1] - startY;
                }
                if (consumed[0] == 0 && consumed[1] == 0) {
                    z = false;
                }
                return z;
            } else if (offsetInWindow != null) {
                offsetInWindow[0] = 0;
                offsetInWindow[1] = 0;
            }
        }
        return false;
    }

    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        if (isNestedScrollingEnabled()) {
            ViewParent viewParent = this.mNestedScrollingParent;
            if (viewParent != null) {
                return viewParent.onNestedFling(this, velocityX, velocityY, consumed);
            }
        }
        return false;
    }

    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        if (isNestedScrollingEnabled()) {
            ViewParent viewParent = this.mNestedScrollingParent;
            if (viewParent != null) {
                return viewParent.onNestedPreFling(this, velocityX, velocityY);
            }
        }
        return false;
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public float getVerticalScrollFactor() {
        if (this.mVerticalScrollFactor == 0.0f) {
            TypedValue outValue = new TypedValue();
            if (this.mContext.getTheme().resolveAttribute(16842829, outValue, true)) {
                this.mVerticalScrollFactor = outValue.getDimension(this.mContext.getResources().getDisplayMetrics());
            } else {
                throw new IllegalStateException("Expected theme to define listPreferredItemHeight.");
            }
        }
        return this.mVerticalScrollFactor;
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public float getHorizontalScrollFactor() {
        return getVerticalScrollFactor();
    }

    @ExportedProperty(category = "text", mapping = {@IntToString(from = 0, to = "INHERIT"), @IntToString(from = 1, to = "FIRST_STRONG"), @IntToString(from = 2, to = "ANY_RTL"), @IntToString(from = 3, to = "LTR"), @IntToString(from = 4, to = "RTL"), @IntToString(from = 5, to = "LOCALE"), @IntToString(from = 6, to = "FIRST_STRONG_LTR"), @IntToString(from = 7, to = "FIRST_STRONG_RTL")})
    @UnsupportedAppUsage
    public int getRawTextDirection() {
        return (this.mPrivateFlags2 & 448) >> 6;
    }

    public void setTextDirection(int textDirection) {
        if (getRawTextDirection() != textDirection) {
            this.mPrivateFlags2 &= -449;
            resetResolvedTextDirection();
            this.mPrivateFlags2 |= (textDirection << 6) & 448;
            resolveTextDirection();
            onRtlPropertiesChanged(getLayoutDirection());
            requestLayout();
            invalidate(true);
        }
    }

    @ExportedProperty(category = "text", mapping = {@IntToString(from = 0, to = "INHERIT"), @IntToString(from = 1, to = "FIRST_STRONG"), @IntToString(from = 2, to = "ANY_RTL"), @IntToString(from = 3, to = "LTR"), @IntToString(from = 4, to = "RTL"), @IntToString(from = 5, to = "LOCALE"), @IntToString(from = 6, to = "FIRST_STRONG_LTR"), @IntToString(from = 7, to = "FIRST_STRONG_RTL")})
    public int getTextDirection() {
        return (this.mPrivateFlags2 & PFLAG2_TEXT_DIRECTION_RESOLVED_MASK) >> 10;
    }

    public boolean resolveTextDirection() {
        StringBuilder stringBuilder;
        String parentResolvedDirection = " does not fully implement ViewParent";
        String str = VIEW_LOG_TAG;
        this.mPrivateFlags2 &= -7681;
        if (hasRtlSupport()) {
            int textDirection = getRawTextDirection();
            switch (textDirection) {
                case 0:
                    if (canResolveTextDirection()) {
                        try {
                            if (this.mParent.isTextDirectionResolved()) {
                                int parentResolvedDirection2;
                                try {
                                    parentResolvedDirection2 = this.mParent.getTextDirection();
                                } catch (AbstractMethodError e) {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(this.mParent.getClass().getSimpleName());
                                    stringBuilder.append(parentResolvedDirection);
                                    Log.e(str, stringBuilder.toString(), e);
                                    parentResolvedDirection2 = 3;
                                }
                                switch (parentResolvedDirection2) {
                                    case 1:
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                    case 6:
                                    case 7:
                                        this.mPrivateFlags2 |= parentResolvedDirection2 << 10;
                                        break;
                                    default:
                                        this.mPrivateFlags2 |= 1024;
                                        break;
                                }
                            }
                            this.mPrivateFlags2 |= 1024;
                            return false;
                        } catch (AbstractMethodError e2) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(this.mParent.getClass().getSimpleName());
                            stringBuilder.append(parentResolvedDirection);
                            Log.e(str, stringBuilder.toString(), e2);
                            this.mPrivateFlags2 |= 1536;
                            return true;
                        }
                    }
                    this.mPrivateFlags2 |= 1024;
                    return false;
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    this.mPrivateFlags2 |= textDirection << 10;
                    break;
                default:
                    this.mPrivateFlags2 |= 1024;
                    break;
            }
        }
        this.mPrivateFlags2 |= 1024;
        this.mPrivateFlags2 |= 512;
        return true;
    }

    public boolean canResolveTextDirection() {
        if (getRawTextDirection() != 0) {
            return true;
        }
        ViewParent viewParent = this.mParent;
        if (viewParent != null) {
            try {
                return viewParent.canResolveTextDirection();
            } catch (AbstractMethodError e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.mParent.getClass().getSimpleName());
                stringBuilder.append(" does not fully implement ViewParent");
                Log.e(VIEW_LOG_TAG, stringBuilder.toString(), e);
            }
        }
        return false;
    }

    public void resetResolvedTextDirection() {
        this.mPrivateFlags2 &= -7681;
        this.mPrivateFlags2 |= 1024;
    }

    public boolean isTextDirectionInherited() {
        return getRawTextDirection() == 0;
    }

    public boolean isTextDirectionResolved() {
        return (this.mPrivateFlags2 & 512) == 512;
    }

    @ExportedProperty(category = "text", mapping = {@IntToString(from = 0, to = "INHERIT"), @IntToString(from = 1, to = "GRAVITY"), @IntToString(from = 2, to = "TEXT_START"), @IntToString(from = 3, to = "TEXT_END"), @IntToString(from = 4, to = "CENTER"), @IntToString(from = 5, to = "VIEW_START"), @IntToString(from = 6, to = "VIEW_END")})
    @UnsupportedAppUsage
    public int getRawTextAlignment() {
        return (this.mPrivateFlags2 & PFLAG2_TEXT_ALIGNMENT_MASK) >> 13;
    }

    public void setTextAlignment(int textAlignment) {
        if (textAlignment != getRawTextAlignment()) {
            this.mPrivateFlags2 &= -57345;
            resetResolvedTextAlignment();
            this.mPrivateFlags2 |= (textAlignment << 13) & PFLAG2_TEXT_ALIGNMENT_MASK;
            resolveTextAlignment();
            onRtlPropertiesChanged(getLayoutDirection());
            requestLayout();
            invalidate(true);
        }
    }

    @ExportedProperty(category = "text", mapping = {@IntToString(from = 0, to = "INHERIT"), @IntToString(from = 1, to = "GRAVITY"), @IntToString(from = 2, to = "TEXT_START"), @IntToString(from = 3, to = "TEXT_END"), @IntToString(from = 4, to = "CENTER"), @IntToString(from = 5, to = "VIEW_START"), @IntToString(from = 6, to = "VIEW_END")})
    public int getTextAlignment() {
        return (this.mPrivateFlags2 & PFLAG2_TEXT_ALIGNMENT_RESOLVED_MASK) >> 17;
    }

    public boolean resolveTextAlignment() {
        String parentResolvedTextAlignment = " does not fully implement ViewParent";
        String str = VIEW_LOG_TAG;
        this.mPrivateFlags2 &= -983041;
        if (hasRtlSupport()) {
            int textAlignment = getRawTextAlignment();
            switch (textAlignment) {
                case 0:
                    if (canResolveTextAlignment()) {
                        try {
                            if (this.mParent.isTextAlignmentResolved()) {
                                int parentResolvedTextAlignment2;
                                try {
                                    parentResolvedTextAlignment2 = this.mParent.getTextAlignment();
                                } catch (AbstractMethodError e) {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append(this.mParent.getClass().getSimpleName());
                                    stringBuilder.append(parentResolvedTextAlignment);
                                    Log.e(str, stringBuilder.toString(), e);
                                    parentResolvedTextAlignment2 = 1;
                                }
                                switch (parentResolvedTextAlignment2) {
                                    case 1:
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                    case 6:
                                        this.mPrivateFlags2 |= parentResolvedTextAlignment2 << 17;
                                        break;
                                    default:
                                        this.mPrivateFlags2 |= 131072;
                                        break;
                                }
                            }
                            this.mPrivateFlags2 = 131072 | this.mPrivateFlags2;
                            return false;
                        } catch (AbstractMethodError e2) {
                            StringBuilder stringBuilder2 = new StringBuilder();
                            stringBuilder2.append(this.mParent.getClass().getSimpleName());
                            stringBuilder2.append(parentResolvedTextAlignment);
                            Log.e(str, stringBuilder2.toString(), e2);
                            this.mPrivateFlags2 |= 196608;
                            return true;
                        }
                    }
                    this.mPrivateFlags2 |= 131072;
                    return false;
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    this.mPrivateFlags2 |= textAlignment << 17;
                    break;
                default:
                    this.mPrivateFlags2 |= 131072;
                    break;
            }
        }
        this.mPrivateFlags2 |= 131072;
        this.mPrivateFlags2 |= 65536;
        return true;
    }

    public boolean canResolveTextAlignment() {
        if (getRawTextAlignment() != 0) {
            return true;
        }
        ViewParent viewParent = this.mParent;
        if (viewParent != null) {
            try {
                return viewParent.canResolveTextAlignment();
            } catch (AbstractMethodError e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.mParent.getClass().getSimpleName());
                stringBuilder.append(" does not fully implement ViewParent");
                Log.e(VIEW_LOG_TAG, stringBuilder.toString(), e);
            }
        }
        return false;
    }

    public void resetResolvedTextAlignment() {
        this.mPrivateFlags2 &= -983041;
        this.mPrivateFlags2 |= 131072;
    }

    public boolean isTextAlignmentInherited() {
        return getRawTextAlignment() == 0;
    }

    public boolean isTextAlignmentResolved() {
        return (this.mPrivateFlags2 & 65536) == 65536;
    }

    public static int generateViewId() {
        while (true) {
            int result = sNextGeneratedId.get();
            int newValue = result + 1;
            if (newValue > 16777215) {
                newValue = 1;
            }
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    private static boolean isViewIdGenerated(int id) {
        return (-16777216 & id) == 0 && (16777215 & id) != 0;
    }

    public void captureTransitioningViews(List<View> transitioningViews) {
        if (getVisibility() == 0) {
            transitioningViews.add(this);
        }
    }

    public void findNamedViews(Map<String, View> namedElements) {
        if (getVisibility() == 0 || this.mGhostView != null) {
            String transitionName = getTransitionName();
            if (transitionName != null) {
                namedElements.put(transitionName, this);
            }
        }
    }

    public PointerIcon onResolvePointerIcon(MotionEvent event, int pointerIndex) {
        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);
        if (isDraggingScrollBar() || isOnScrollbarThumb(x, y)) {
            return PointerIcon.getSystemIcon(this.mContext, 1000);
        }
        return this.mPointerIcon;
    }

    public void setPointerIcon(PointerIcon pointerIcon) {
        this.mPointerIcon = pointerIcon;
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null && !attachInfo.mHandlingPointerEvent) {
            try {
                this.mAttachInfo.mSession.updatePointerIcon(this.mAttachInfo.mWindow);
            } catch (RemoteException e) {
            }
        }
    }

    public PointerIcon getPointerIcon() {
        return this.mPointerIcon;
    }

    public boolean hasPointerCapture() {
        ViewRootImpl viewRootImpl = getViewRootImpl();
        if (viewRootImpl == null) {
            return false;
        }
        return viewRootImpl.hasPointerCapture();
    }

    public void requestPointerCapture() {
        ViewRootImpl viewRootImpl = getViewRootImpl();
        if (viewRootImpl != null) {
            viewRootImpl.requestPointerCapture(true);
        }
    }

    public void releasePointerCapture() {
        ViewRootImpl viewRootImpl = getViewRootImpl();
        if (viewRootImpl != null) {
            viewRootImpl.requestPointerCapture(false);
        }
    }

    public void onPointerCaptureChange(boolean hasCapture) {
    }

    public void dispatchPointerCaptureChanged(boolean hasCapture) {
        onPointerCaptureChange(hasCapture);
    }

    public boolean onCapturedPointerEvent(MotionEvent event) {
        return false;
    }

    public void setOnCapturedPointerListener(OnCapturedPointerListener l) {
        getListenerInfo().mOnCapturedPointerListener = l;
    }

    private void recordGestureClassification(int classification) {
        if (classification != 0) {
            StatsLogInternal.write(177, getClass().getName(), classification);
        }
    }

    public ViewPropertyAnimator animate() {
        if (this.mAnimator == null) {
            this.mAnimator = new ViewPropertyAnimator(this);
        }
        return this.mAnimator;
    }

    public final void setTransitionName(String transitionName) {
        this.mTransitionName = transitionName;
    }

    @ExportedProperty
    public String getTransitionName() {
        return this.mTransitionName;
    }

    public void requestKeyboardShortcuts(List<KeyboardShortcutGroup> list, int deviceId) {
    }

    @UnsupportedAppUsage
    private void cancel(SendViewScrolledAccessibilityEvent callback) {
        if (callback != null && callback.mIsPending) {
            removeCallbacks(callback);
            callback.reset();
        }
    }

    private static void dumpFlags() {
        HashMap<String, String> found = Maps.newHashMap();
        try {
            for (Field field : View.class.getDeclaredFields()) {
                int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
                    if (field.getType().equals(Integer.TYPE)) {
                        dumpFlag(found, field.getName(), field.getInt(null));
                    } else if (field.getType().equals(int[].class)) {
                        int[] values = (int[]) field.get(null);
                        for (int i = 0; i < values.length; i++) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(field.getName());
                            stringBuilder.append("[");
                            stringBuilder.append(i);
                            stringBuilder.append("]");
                            dumpFlag(found, stringBuilder.toString(), values[i]);
                        }
                    }
                }
            }
            ArrayList<String> keys = Lists.newArrayList();
            keys.addAll(found.keySet());
            Collections.sort(keys);
            Iterator it = keys.iterator();
            while (it.hasNext()) {
                Log.d(VIEW_LOG_TAG, (String) found.get((String) it.next()));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void dumpFlag(HashMap<String, String> found, String name, int value) {
        String bits = String.format("%32s", new Object[]{Integer.toBinaryString(value)}).replace('0', ' ');
        int prefix = name.indexOf(95);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(prefix > 0 ? name.substring(0, prefix) : name);
        stringBuilder.append(bits);
        stringBuilder.append(name);
        String key = stringBuilder.toString();
        String output = new StringBuilder();
        output.append(bits);
        output.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        output.append(name);
        found.put(key, output.toString());
    }

    public void encode(ViewHierarchyEncoder stream) {
        stream.beginObject(this);
        encodeProperties(stream);
        stream.endObject();
    }

    /* Access modifiers changed, original: protected */
    public void encodeProperties(ViewHierarchyEncoder stream) {
        Object resolveId = ViewDebug.resolveId(getContext(), this.mID);
        String str = "id";
        if (resolveId instanceof String) {
            stream.addProperty(str, (String) resolveId);
        } else {
            stream.addProperty(str, this.mID);
        }
        TransformationInfo transformationInfo = this.mTransformationInfo;
        stream.addProperty("misc:transformation.alpha", transformationInfo != null ? transformationInfo.mAlpha : 0.0f);
        stream.addProperty("misc:transitionName", getTransitionName());
        stream.addProperty("layout:left", this.mLeft);
        stream.addProperty("layout:right", this.mRight);
        stream.addProperty("layout:top", this.mTop);
        stream.addProperty("layout:bottom", this.mBottom);
        stream.addProperty("layout:width", getWidth());
        stream.addProperty("layout:height", getHeight());
        stream.addProperty("layout:layoutDirection", getLayoutDirection());
        stream.addProperty("layout:layoutRtl", isLayoutRtl());
        stream.addProperty("layout:hasTransientState", hasTransientState());
        stream.addProperty("layout:baseline", getBaseline());
        LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            stream.addPropertyKey("layoutParams");
            layoutParams.encode(stream);
        }
        stream.addProperty("scrolling:scrollX", this.mScrollX);
        stream.addProperty("scrolling:scrollY", this.mScrollY);
        stream.addProperty("padding:paddingLeft", this.mPaddingLeft);
        stream.addProperty("padding:paddingRight", this.mPaddingRight);
        stream.addProperty("padding:paddingTop", this.mPaddingTop);
        stream.addProperty("padding:paddingBottom", this.mPaddingBottom);
        stream.addProperty("padding:userPaddingRight", this.mUserPaddingRight);
        stream.addProperty("padding:userPaddingLeft", this.mUserPaddingLeft);
        stream.addProperty("padding:userPaddingBottom", this.mUserPaddingBottom);
        stream.addProperty("padding:userPaddingStart", this.mUserPaddingStart);
        stream.addProperty("padding:userPaddingEnd", this.mUserPaddingEnd);
        stream.addProperty("measurement:minHeight", this.mMinHeight);
        stream.addProperty("measurement:minWidth", this.mMinWidth);
        stream.addProperty("measurement:measuredWidth", this.mMeasuredWidth);
        stream.addProperty("measurement:measuredHeight", this.mMeasuredHeight);
        stream.addProperty("drawing:elevation", getElevation());
        stream.addProperty("drawing:translationX", getTranslationX());
        stream.addProperty("drawing:translationY", getTranslationY());
        stream.addProperty("drawing:translationZ", getTranslationZ());
        stream.addProperty("drawing:rotation", getRotation());
        stream.addProperty("drawing:rotationX", getRotationX());
        stream.addProperty("drawing:rotationY", getRotationY());
        stream.addProperty("drawing:scaleX", getScaleX());
        stream.addProperty("drawing:scaleY", getScaleY());
        stream.addProperty("drawing:pivotX", getPivotX());
        stream.addProperty("drawing:pivotY", getPivotY());
        Rect rect = this.mClipBounds;
        stream.addProperty("drawing:clipBounds", rect == null ? null : rect.toString());
        stream.addProperty("drawing:opaque", isOpaque());
        stream.addProperty("drawing:alpha", getAlpha());
        stream.addProperty("drawing:transitionAlpha", getTransitionAlpha());
        stream.addProperty("drawing:shadow", hasShadow());
        stream.addProperty("drawing:solidColor", getSolidColor());
        stream.addProperty("drawing:layerType", this.mLayerType);
        stream.addProperty("drawing:willNotDraw", willNotDraw());
        stream.addProperty("drawing:hardwareAccelerated", isHardwareAccelerated());
        stream.addProperty("drawing:willNotCacheDrawing", willNotCacheDrawing());
        stream.addProperty("drawing:drawingCacheEnabled", isDrawingCacheEnabled());
        stream.addProperty("drawing:overlappingRendering", hasOverlappingRendering());
        stream.addProperty("drawing:outlineAmbientShadowColor", getOutlineAmbientShadowColor());
        stream.addProperty("drawing:outlineSpotShadowColor", getOutlineSpotShadowColor());
        stream.addProperty("focus:hasFocus", hasFocus());
        stream.addProperty("focus:isFocused", isFocused());
        stream.addProperty("focus:focusable", getFocusable());
        stream.addProperty("focus:isFocusable", isFocusable());
        stream.addProperty("focus:isFocusableInTouchMode", isFocusableInTouchMode());
        stream.addProperty("misc:clickable", isClickable());
        stream.addProperty("misc:pressed", isPressed());
        stream.addProperty("misc:selected", isSelected());
        stream.addProperty("misc:touchMode", isInTouchMode());
        stream.addProperty("misc:hovered", isHovered());
        stream.addProperty("misc:activated", isActivated());
        stream.addProperty("misc:visibility", getVisibility());
        stream.addProperty("misc:fitsSystemWindows", getFitsSystemWindows());
        stream.addProperty("misc:filterTouchesWhenObscured", getFilterTouchesWhenObscured());
        stream.addProperty("misc:enabled", isEnabled());
        stream.addProperty("misc:soundEffectsEnabled", isSoundEffectsEnabled());
        stream.addProperty("misc:hapticFeedbackEnabled", isHapticFeedbackEnabled());
        Theme theme = getContext().getTheme();
        if (theme != null) {
            stream.addPropertyKey("theme");
            theme.encode(stream);
        }
        String[] strArr = this.mAttributes;
        int n = strArr != null ? strArr.length : 0;
        stream.addProperty("meta:__attrCount__", n / 2);
        for (int i = 0; i < n; i += 2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("meta:__attr__");
            stringBuilder.append(this.mAttributes[i]);
            stream.addProperty(stringBuilder.toString(), this.mAttributes[i + 1]);
        }
        stream.addProperty("misc:scrollBarStyle", getScrollBarStyle());
        stream.addProperty("text:textDirection", getTextDirection());
        stream.addProperty("text:textAlignment", getTextAlignment());
        CharSequence contentDescription = getContentDescription();
        stream.addProperty("accessibility:contentDescription", contentDescription == null ? "" : contentDescription.toString());
        stream.addProperty("accessibility:labelFor", getLabelFor());
        stream.addProperty("accessibility:importantForAccessibility", getImportantForAccessibility());
    }

    /* Access modifiers changed, original: 0000 */
    public boolean shouldDrawRoundScrollbar() {
        boolean z = false;
        if (!this.mResources.getConfiguration().isScreenRound() || this.mAttachInfo == null) {
            return false;
        }
        View rootView = getRootView();
        WindowInsets insets = getRootWindowInsets();
        int height = getHeight();
        int width = getWidth();
        int displayHeight = rootView.getHeight();
        int displayWidth = rootView.getWidth();
        if (height != displayHeight || width != displayWidth) {
            return false;
        }
        getLocationInWindow(this.mAttachInfo.mTmpLocation);
        if (this.mAttachInfo.mTmpLocation[0] == insets.getStableInsetLeft() && this.mAttachInfo.mTmpLocation[1] == insets.getStableInsetTop()) {
            z = true;
        }
        return z;
    }

    public void setTooltipText(CharSequence tooltipText) {
        if (TextUtils.isEmpty(tooltipText)) {
            setFlags(0, 1073741824);
            hideTooltip();
            this.mTooltipInfo = null;
            return;
        }
        setFlags(1073741824, 1073741824);
        if (this.mTooltipInfo == null) {
            this.mTooltipInfo = new TooltipInfo();
            TooltipInfo tooltipInfo = this.mTooltipInfo;
            tooltipInfo.mShowTooltipRunnable = new -$$Lambda$View$llq76MkPXP4bNcb9oJt_msw0fnQ(this);
            tooltipInfo.mHideTooltipRunnable = new -$$Lambda$QI1s392qW8l6mC24bcy9050SkuY(this);
            tooltipInfo.mHoverSlop = ViewConfiguration.get(this.mContext).getScaledHoverSlop();
            this.mTooltipInfo.clearAnchorPos();
        }
        this.mTooltipInfo.mTooltipText = tooltipText;
    }

    @UnsupportedAppUsage
    public void setTooltip(CharSequence tooltipText) {
        setTooltipText(tooltipText);
    }

    public CharSequence getTooltipText() {
        TooltipInfo tooltipInfo = this.mTooltipInfo;
        return tooltipInfo != null ? tooltipInfo.mTooltipText : null;
    }

    public CharSequence getTooltip() {
        return getTooltipText();
    }

    private boolean showTooltip(int x, int y, boolean fromLongClick) {
        if (this.mAttachInfo == null || this.mTooltipInfo == null) {
            return false;
        }
        if ((fromLongClick && (this.mViewFlags & 32) != 0) || TextUtils.isEmpty(this.mTooltipInfo.mTooltipText)) {
            return false;
        }
        hideTooltip();
        TooltipInfo tooltipInfo = this.mTooltipInfo;
        tooltipInfo.mTooltipFromLongClick = fromLongClick;
        tooltipInfo.mTooltipPopup = new TooltipPopup(getContext());
        this.mTooltipInfo.mTooltipPopup.show(this, x, y, (this.mPrivateFlags3 & 131072) == 131072, this.mTooltipInfo.mTooltipText);
        this.mAttachInfo.mTooltipHost = this;
        notifyViewAccessibilityStateChangedIfNeeded(0);
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void hideTooltip() {
        TooltipInfo tooltipInfo = this.mTooltipInfo;
        if (tooltipInfo != null) {
            removeCallbacks(tooltipInfo.mShowTooltipRunnable);
            if (this.mTooltipInfo.mTooltipPopup != null) {
                this.mTooltipInfo.mTooltipPopup.hide();
                tooltipInfo = this.mTooltipInfo;
                tooltipInfo.mTooltipPopup = null;
                tooltipInfo.mTooltipFromLongClick = false;
                tooltipInfo.clearAnchorPos();
                AttachInfo attachInfo = this.mAttachInfo;
                if (attachInfo != null) {
                    attachInfo.mTooltipHost = null;
                }
                notifyViewAccessibilityStateChangedIfNeeded(0);
            }
        }
    }

    private boolean showLongClickTooltip(int x, int y) {
        removeCallbacks(this.mTooltipInfo.mShowTooltipRunnable);
        removeCallbacks(this.mTooltipInfo.mHideTooltipRunnable);
        return showTooltip(x, y, true);
    }

    private boolean showHoverTooltip() {
        return showTooltip(this.mTooltipInfo.mAnchorX, this.mTooltipInfo.mAnchorY, false);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean dispatchTooltipHoverEvent(MotionEvent event) {
        if (this.mTooltipInfo == null) {
            return false;
        }
        int action = event.getAction();
        if (action != 7) {
            if (action == 10) {
                this.mTooltipInfo.clearAnchorPos();
                if (!this.mTooltipInfo.mTooltipFromLongClick) {
                    hideTooltip();
                }
            }
        } else if ((this.mViewFlags & 1073741824) == 1073741824) {
            if (!this.mTooltipInfo.mTooltipFromLongClick && this.mTooltipInfo.updateAnchorPos(event)) {
                if (this.mTooltipInfo.mTooltipPopup == null) {
                    removeCallbacks(this.mTooltipInfo.mShowTooltipRunnable);
                    postDelayed(this.mTooltipInfo.mShowTooltipRunnable, (long) ViewConfiguration.getHoverTooltipShowTimeout());
                }
                if ((getWindowSystemUiVisibility() & 1) == 1) {
                    action = ViewConfiguration.getHoverTooltipHideShortTimeout();
                } else {
                    action = ViewConfiguration.getHoverTooltipHideTimeout();
                }
                removeCallbacks(this.mTooltipInfo.mHideTooltipRunnable);
                postDelayed(this.mTooltipInfo.mHideTooltipRunnable, (long) action);
            }
            return true;
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public void handleTooltipKey(KeyEvent event) {
        int action = event.getAction();
        if (action != 0) {
            if (action == 1) {
                handleTooltipUp();
            }
        } else if (event.getRepeatCount() == 0) {
            hideTooltip();
        }
    }

    private void handleTooltipUp() {
        TooltipInfo tooltipInfo = this.mTooltipInfo;
        if (tooltipInfo != null && tooltipInfo.mTooltipPopup != null) {
            removeCallbacks(this.mTooltipInfo.mHideTooltipRunnable);
            postDelayed(this.mTooltipInfo.mHideTooltipRunnable, (long) ViewConfiguration.getLongPressTooltipHideTimeout());
        }
    }

    private int getFocusableAttribute(TypedArray attributes) {
        TypedValue val = new TypedValue();
        if (!attributes.getValue(19, val)) {
            return 16;
        }
        if (val.type != 18) {
            return val.data;
        }
        return val.data == 0 ? 0 : 1;
    }

    public View getTooltipView() {
        TooltipInfo tooltipInfo = this.mTooltipInfo;
        if (tooltipInfo == null || tooltipInfo.mTooltipPopup == null) {
            return null;
        }
        return this.mTooltipInfo.mTooltipPopup.getContentView();
    }

    public static boolean isDefaultFocusHighlightEnabled() {
        return sUseDefaultFocusHighlight;
    }

    /* Access modifiers changed, original: 0000 */
    public View dispatchUnhandledKeyEvent(KeyEvent evt) {
        if (onUnhandledKeyEvent(evt)) {
            return this;
        }
        return null;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean onUnhandledKeyEvent(KeyEvent event) {
        ListenerInfo listenerInfo = this.mListenerInfo;
        if (!(listenerInfo == null || listenerInfo.mUnhandledKeyListeners == null)) {
            for (int i = this.mListenerInfo.mUnhandledKeyListeners.size() - 1; i >= 0; i--) {
                if (((OnUnhandledKeyEventListener) this.mListenerInfo.mUnhandledKeyListeners.get(i)).onUnhandledKeyEvent(this, event)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean hasUnhandledKeyListener() {
        ListenerInfo listenerInfo = this.mListenerInfo;
        return (listenerInfo == null || listenerInfo.mUnhandledKeyListeners == null || this.mListenerInfo.mUnhandledKeyListeners.isEmpty()) ? false : true;
    }

    public void addOnUnhandledKeyEventListener(OnUnhandledKeyEventListener listener) {
        ArrayList<OnUnhandledKeyEventListener> listeners = getListenerInfo().mUnhandledKeyListeners;
        if (listeners == null) {
            listeners = new ArrayList();
            getListenerInfo().mUnhandledKeyListeners = listeners;
        }
        listeners.add(listener);
        if (listeners.size() == 1) {
            ViewParent viewParent = this.mParent;
            if (viewParent instanceof ViewGroup) {
                ((ViewGroup) viewParent).incrementChildUnhandledKeyListeners();
            }
        }
    }

    public void removeOnUnhandledKeyEventListener(OnUnhandledKeyEventListener listener) {
        ListenerInfo listenerInfo = this.mListenerInfo;
        if (listenerInfo != null && listenerInfo.mUnhandledKeyListeners != null && !this.mListenerInfo.mUnhandledKeyListeners.isEmpty()) {
            this.mListenerInfo.mUnhandledKeyListeners.remove(listener);
            if (this.mListenerInfo.mUnhandledKeyListeners.isEmpty()) {
                this.mListenerInfo.mUnhandledKeyListeners = null;
                ViewParent viewParent = this.mParent;
                if (viewParent instanceof ViewGroup) {
                    ((ViewGroup) viewParent).decrementChildUnhandledKeyListeners();
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public Activity getAttachedActivity() {
        Context viewContext = getContext();
        while (!(viewContext instanceof Activity) && (viewContext instanceof ContextWrapper)) {
            Context preViewContext = viewContext;
            viewContext = ((ContextWrapper) viewContext).getBaseContext();
            if (preViewContext == viewContext) {
                return null;
            }
        }
        if (viewContext instanceof Activity) {
            return (Activity) viewContext;
        }
        return null;
    }

    public Activity getAttachedActivityInstance() {
        if (this.mFirst) {
            synchronized (this) {
                if (this.mFirst) {
                    this.mAttachedActivity = new WeakReference(getAttachedActivity());
                    this.mFirst = false;
                }
            }
        }
        WeakReference weakReference = this.mAttachedActivity;
        return weakReference == null ? null : (Activity) weakReference.get();
    }

    private boolean notifyWebView(View rootView, boolean isLoad) {
        if (rootView != null) {
            Activity activity = rootView.getAttachedActivityInstance();
            IInterceptor interceptor = activity != null ? activity.getInterceptor() : null;
            if (interceptor != null) {
                interceptor.notifyWebView(this, isLoad);
                return true;
            }
        }
        return false;
    }

    public void notifyConfirmedWebView(boolean isLoad) {
        String str = "ContentCatcher";
        if (this.mIsWebView) {
            AttachInfo attachInfo = this.mAttachInfo;
            if (attachInfo != null && attachInfo.mRootView != null) {
                try {
                    if (!notifyWebView(this.mAttachInfo.mRootView, isLoad)) {
                        Log.w(str, "Failed to notify a WebView");
                    }
                } catch (Exception e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("View.notifyConfirmedWebView-Exception: ");
                    stringBuilder.append(e);
                    Log.e(str, stringBuilder.toString());
                }
            }
        }
    }

    public final void dispatchTouchEventToContentCatcher(MotionEvent event) {
        Activity attachedActivity = getAttachedActivityInstance();
        if (attachedActivity != null) {
            IInterceptor interceptor = attachedActivity.getInterceptor();
            if (interceptor != null) {
                interceptor.dispatchTouchEvent(event, this, attachedActivity);
            }
        }
    }
}
