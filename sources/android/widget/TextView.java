package android.widget;

import android.Manifest.permission;
import android.R;
import android.annotation.UnsupportedAppUsage;
import android.app.PendingIntent.CanceledException;
import android.app.RemoteAction;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.UndoManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.BaseCanvas;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Insets;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.icu.text.DecimalFormatSymbols;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.ParcelableParcel;
import android.os.Process;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Downloads.Impl;
import android.provider.Settings.Global;
import android.text.BoringLayout;
import android.text.BoringLayout.Metrics;
import android.text.DynamicLayout;
import android.text.Editable;
import android.text.Editable.Factory;
import android.text.GetChars;
import android.text.GraphicsOperations;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.InputType;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.ParcelableSpan;
import android.text.PrecomputedText;
import android.text.PrecomputedText.Params;
import android.text.Selection;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.StaticLayout;
import android.text.StaticLayout.Builder;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.text.method.AllCapsTransformationMethod;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.DateKeyListener;
import android.text.method.DateTimeKeyListener;
import android.text.method.DialerKeyListener;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.text.method.LinkMovementMethod;
import android.text.method.MetaKeyKeyListener;
import android.text.method.MovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.text.method.TextKeyListener;
import android.text.method.TextKeyListener.Capitalize;
import android.text.method.TimeKeyListener;
import android.text.method.TransformationMethod;
import android.text.method.TransformationMethod2;
import android.text.method.WordIterator;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ParagraphStyle;
import android.text.style.SpellCheckSpan;
import android.text.style.SuggestionSpan;
import android.text.style.URLSpan;
import android.text.style.UpdateAppearance;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.IntArray;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.AccessibilityIterators.TextSegmentIterator;
import android.view.ActionMode.Callback;
import android.view.Choreographer;
import android.view.Choreographer.FrameCallback;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.KeyEvent.DispatcherState;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.CapturedViewProperty;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewDebug.IntToString;
import android.view.ViewHierarchyEncoder;
import android.view.ViewRootImpl;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.animation.AnimationUtils;
import android.view.autofill.AutofillManager;
import android.view.autofill.AutofillValue;
import android.view.autofill.Helper;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CorrectionInfo;
import android.view.inputmethod.CursorAnchorInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.view.inspector.InspectionCompanion.UninitializedPropertyMapException;
import android.view.inspector.IntFlagMapping;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import android.view.textclassifier.TextClassification;
import android.view.textclassifier.TextClassification.Request;
import android.view.textclassifier.TextClassificationContext;
import android.view.textclassifier.TextClassificationManager;
import android.view.textclassifier.TextClassifier;
import android.view.textclassifier.TextLinks.TextLinkSpan;
import android.view.textservice.SpellCheckerSubtype;
import android.view.textservice.TextServicesManager;
import android.widget.RemoteViews.RemoteView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.internal.util.FastMath;
import com.android.internal.util.Preconditions;
import com.android.internal.widget.EditableInputConnection;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import libcore.util.EmptyArray;
import miui.util.TypefaceUtils;
import org.xmlpull.v1.XmlPullParserException;

@RemoteView
public class TextView extends View implements OnPreDrawListener {
    static final int ACCESSIBILITY_ACTION_PROCESS_TEXT_START_ID = 268435712;
    private static final int ACCESSIBILITY_ACTION_SHARE = 268435456;
    private static final int ANIMATED_SCROLL_GAP = 250;
    public static final int AUTO_SIZE_TEXT_TYPE_NONE = 0;
    public static final int AUTO_SIZE_TEXT_TYPE_UNIFORM = 1;
    private static final int CHANGE_WATCHER_PRIORITY = 100;
    static final boolean DEBUG_EXTRACT = false;
    private static final int DECIMAL = 4;
    private static final int DEFAULT_AUTO_SIZE_GRANULARITY_IN_PX = 1;
    private static final int DEFAULT_AUTO_SIZE_MAX_TEXT_SIZE_IN_SP = 112;
    private static final int DEFAULT_AUTO_SIZE_MIN_TEXT_SIZE_IN_SP = 12;
    private static final int DEFAULT_TYPEFACE = -1;
    private static final int DEVICE_PROVISIONED_NO = 1;
    private static final int DEVICE_PROVISIONED_UNKNOWN = 0;
    private static final int DEVICE_PROVISIONED_YES = 2;
    private static final int ELLIPSIZE_END = 3;
    private static final int ELLIPSIZE_MARQUEE = 4;
    private static final int ELLIPSIZE_MIDDLE = 2;
    private static final int ELLIPSIZE_NONE = 0;
    private static final int ELLIPSIZE_NOT_SET = -1;
    private static final int ELLIPSIZE_START = 1;
    private static final Spanned EMPTY_SPANNED = new SpannedString("");
    private static final int EMS = 1;
    private static final int FLOATING_TOOLBAR_SELECT_ALL_REFRESH_DELAY = 500;
    static final int ID_ASSIST = 16908353;
    static final int ID_AUTOFILL = 16908355;
    static final int ID_COPY = 16908321;
    static final int ID_CUT = 16908320;
    static final int ID_PASTE = 16908322;
    static final int ID_PASTE_AS_PLAIN_TEXT = 16908337;
    static final int ID_REDO = 16908339;
    static final int ID_REPLACE = 16908340;
    static final int ID_SELECT_ALL = 16908319;
    static final int ID_SHARE = 16908341;
    static final int ID_UNDO = 16908338;
    private static final int KEY_DOWN_HANDLED_BY_KEY_LISTENER = 1;
    private static final int KEY_DOWN_HANDLED_BY_MOVEMENT_METHOD = 2;
    private static final int KEY_EVENT_HANDLED = -1;
    private static final int KEY_EVENT_NOT_HANDLED = 0;
    @UnsupportedAppUsage
    private static final int LINES = 1;
    static final String LOG_TAG = "TextView";
    private static final int MARQUEE_FADE_NORMAL = 0;
    private static final int MARQUEE_FADE_SWITCH_SHOW_ELLIPSIS = 1;
    private static final int MARQUEE_FADE_SWITCH_SHOW_FADE = 2;
    private static final int MONOSPACE = 3;
    private static final int[] MULTILINE_STATE_SET = new int[]{16843597};
    private static final InputFilter[] NO_FILTERS = new InputFilter[0];
    private static final int PIXELS = 2;
    static final int PROCESS_TEXT_REQUEST_CODE = 100;
    private static final int SANS = 1;
    private static final int SERIF = 2;
    private static final int SIGNED = 2;
    private static final float[] TEMP_POSITION = new float[2];
    private static final RectF TEMP_RECTF = new RectF();
    @VisibleForTesting
    public static final Metrics UNKNOWN_BORING = new Metrics();
    private static final float UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE = -1.0f;
    static final int VERY_WIDE = 1048576;
    private static final SparseIntArray sAppearanceValues = new SparseIntArray();
    static long sLastCutCopyOrTextChangedTime;
    @UnsupportedAppUsage
    private boolean mAllowTransformationLengthChange;
    private int mAutoLinkMask;
    private float mAutoSizeMaxTextSizeInPx;
    private float mAutoSizeMinTextSizeInPx;
    private float mAutoSizeStepGranularityInPx;
    private int[] mAutoSizeTextSizesInPx;
    private int mAutoSizeTextType;
    @UnsupportedAppUsage
    private Metrics mBoring;
    private int mBreakStrategy;
    @UnsupportedAppUsage
    private BufferType mBufferType;
    @UnsupportedAppUsage
    private ChangeWatcher mChangeWatcher;
    @UnsupportedAppUsage
    private CharWrapper mCharWrapper;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private int mCurHintTextColor;
    @ExportedProperty(category = "text")
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private int mCurTextColor;
    private volatile Locale mCurrentSpellCheckerLocaleCache;
    private Drawable mCursorDrawable;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    int mCursorDrawableRes;
    private int mDeferScroll;
    @UnsupportedAppUsage
    private int mDesiredHeightAtMeasure;
    private int mDeviceProvisionedState;
    @UnsupportedAppUsage
    Drawables mDrawables;
    @UnsupportedAppUsage
    private Factory mEditableFactory;
    @UnsupportedAppUsage
    private Editor mEditor;
    private TruncateAt mEllipsize;
    private InputFilter[] mFilters;
    private boolean mFreezesText;
    @ExportedProperty(category = "text")
    @UnsupportedAppUsage
    private int mGravity;
    private boolean mHasPresetAutoSizeValues;
    @UnsupportedAppUsage
    int mHighlightColor;
    @UnsupportedAppUsage
    private final Paint mHighlightPaint;
    private Path mHighlightPath;
    @UnsupportedAppUsage
    private boolean mHighlightPathBogus;
    private CharSequence mHint;
    @UnsupportedAppUsage
    private Metrics mHintBoring;
    @UnsupportedAppUsage
    private Layout mHintLayout;
    private ColorStateList mHintTextColor;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private boolean mHorizontallyScrolling;
    private int mHyphenationFrequency;
    @UnsupportedAppUsage
    private boolean mIncludePad;
    private int mJustificationMode;
    private int mLastLayoutDirection;
    private long mLastScroll;
    @UnsupportedAppUsage
    private Layout mLayout;
    private ColorStateList mLinkTextColor;
    private boolean mLinksClickable;
    private boolean mListenerChanged;
    @UnsupportedAppUsage(trackingBug = 123769451)
    private ArrayList<TextWatcher> mListeners;
    private boolean mLocalesChanged;
    @UnsupportedAppUsage(trackingBug = 124050217)
    private Marquee mMarquee;
    @UnsupportedAppUsage
    private int mMarqueeFadeMode;
    private int mMarqueeRepeatLimit;
    @UnsupportedAppUsage
    private int mMaxMode;
    @UnsupportedAppUsage
    private int mMaxWidth;
    @UnsupportedAppUsage
    private int mMaxWidthMode;
    @UnsupportedAppUsage
    private int mMaximum;
    @UnsupportedAppUsage
    private int mMinMode;
    @UnsupportedAppUsage
    private int mMinWidth;
    @UnsupportedAppUsage
    private int mMinWidthMode;
    @UnsupportedAppUsage
    private int mMinimum;
    private MovementMethod mMovement;
    private boolean mNeedsAutoSizeText;
    @UnsupportedAppUsage
    private int mOldMaxMode;
    @UnsupportedAppUsage
    private int mOldMaximum;
    private boolean mPreDrawListenerDetached;
    private boolean mPreDrawRegistered;
    private PrecomputedText mPrecomputed;
    private boolean mPreventDefaultMovement;
    @UnsupportedAppUsage
    private boolean mRestartMarquee;
    @UnsupportedAppUsage
    private BoringLayout mSavedHintLayout;
    @UnsupportedAppUsage
    private BoringLayout mSavedLayout;
    @UnsupportedAppUsage
    private Layout mSavedMarqueeModeLayout;
    private Scroller mScroller;
    private int mShadowColor;
    @UnsupportedAppUsage
    private float mShadowDx;
    @UnsupportedAppUsage
    private float mShadowDy;
    @UnsupportedAppUsage
    private float mShadowRadius;
    @UnsupportedAppUsage
    private boolean mSingleLine;
    @UnsupportedAppUsage
    private float mSpacingAdd;
    @UnsupportedAppUsage
    private float mSpacingMult;
    private Spannable mSpannable;
    @UnsupportedAppUsage
    private Spannable.Factory mSpannableFactory;
    private Rect mTempRect;
    private TextPaint mTempTextPaint;
    @ExportedProperty(category = "text")
    @UnsupportedAppUsage
    private CharSequence mText;
    private TextClassificationContext mTextClassificationContext;
    private TextClassifier mTextClassificationSession;
    private TextClassifier mTextClassifier;
    private ColorStateList mTextColor;
    @UnsupportedAppUsage
    private TextDirectionHeuristic mTextDir;
    int mTextEditSuggestionContainerLayout;
    int mTextEditSuggestionHighlightStyle;
    int mTextEditSuggestionItemLayout;
    private int mTextId;
    private UserHandle mTextOperationUser;
    @UnsupportedAppUsage
    private final TextPaint mTextPaint;
    private Drawable mTextSelectHandle;
    private Drawable mTextSelectHandleLeft;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    int mTextSelectHandleLeftRes;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    int mTextSelectHandleRes;
    private Drawable mTextSelectHandleRight;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    int mTextSelectHandleRightRes;
    private boolean mTextSetFromXmlOrResourceId;
    private TransformationMethod mTransformation;
    @UnsupportedAppUsage
    private CharSequence mTransformed;
    boolean mUseFallbackLineSpacing;
    private final boolean mUseInternationalizedInput;
    @UnsupportedAppUsage
    private boolean mUserSetTextScaleX;

    public interface OnEditorActionListener {
        boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent);
    }

    /* renamed from: android.widget.TextView$4 */
    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$android$text$Layout$Alignment = new int[Alignment.values().length];

        static {
            try {
                $SwitchMap$android$text$Layout$Alignment[Alignment.ALIGN_LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$text$Layout$Alignment[Alignment.ALIGN_RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$text$Layout$Alignment[Alignment.ALIGN_NORMAL.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$text$Layout$Alignment[Alignment.ALIGN_OPPOSITE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$android$text$Layout$Alignment[Alignment.ALIGN_CENTER.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AutoSizeTextType {
    }

    public enum BufferType {
        NORMAL,
        SPANNABLE,
        EDITABLE
    }

    private class ChangeWatcher implements TextWatcher, SpanWatcher {
        private CharSequence mBeforeText;

        private ChangeWatcher() {
        }

        /* synthetic */ ChangeWatcher(TextView x0, AnonymousClass1 x1) {
            this();
        }

        public void beforeTextChanged(CharSequence buffer, int start, int before, int after) {
            if (AccessibilityManager.getInstance(TextView.this.mContext).isEnabled() && TextView.this.mTransformed != null) {
                this.mBeforeText = TextView.this.mTransformed.toString();
            }
            TextView.this.sendBeforeTextChanged(buffer, start, before, after);
        }

        public void onTextChanged(CharSequence buffer, int start, int before, int after) {
            TextView.this.handleTextChanged(buffer, start, before, after);
            if (!AccessibilityManager.getInstance(TextView.this.mContext).isEnabled()) {
                return;
            }
            if (TextView.this.isFocused() || (TextView.this.isSelected() && TextView.this.isShown())) {
                TextView.this.sendAccessibilityEventTypeViewTextChanged(this.mBeforeText, start, before, after);
                this.mBeforeText = null;
            }
        }

        public void afterTextChanged(Editable buffer) {
            TextView.this.sendAfterTextChanged(buffer);
            if (MetaKeyKeyListener.getMetaState((CharSequence) buffer, 2048) != 0) {
                MetaKeyKeyListener.stopSelecting(TextView.this, buffer);
            }
        }

        public void onSpanChanged(Spannable buf, Object what, int s, int e, int st, int en) {
            TextView.this.spanChange(buf, what, s, st, e, en);
        }

        public void onSpanAdded(Spannable buf, Object what, int s, int e) {
            TextView.this.spanChange(buf, what, -1, s, -1, e);
        }

        public void onSpanRemoved(Spannable buf, Object what, int s, int e) {
            TextView.this.spanChange(buf, what, s, -1, e, -1);
        }
    }

    private static class CharWrapper implements CharSequence, GetChars, GraphicsOperations {
        private char[] mChars;
        private int mLength;
        private int mStart;

        public CharWrapper(char[] chars, int start, int len) {
            this.mChars = chars;
            this.mStart = start;
            this.mLength = len;
        }

        /* Access modifiers changed, original: 0000 */
        public void set(char[] chars, int start, int len) {
            this.mChars = chars;
            this.mStart = start;
            this.mLength = len;
        }

        public int length() {
            return this.mLength;
        }

        public char charAt(int off) {
            return this.mChars[this.mStart + off];
        }

        public String toString() {
            return new String(this.mChars, this.mStart, this.mLength);
        }

        public CharSequence subSequence(int start, int end) {
            if (start >= 0 && end >= 0) {
                int i = this.mLength;
                if (start <= i && end <= i) {
                    return new String(this.mChars, this.mStart + start, end - start);
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(start);
            stringBuilder.append(", ");
            stringBuilder.append(end);
            throw new IndexOutOfBoundsException(stringBuilder.toString());
        }

        public void getChars(int start, int end, char[] buf, int off) {
            if (start >= 0 && end >= 0) {
                int i = this.mLength;
                if (start <= i && end <= i) {
                    System.arraycopy(this.mChars, this.mStart + start, buf, off, end - start);
                    return;
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(start);
            stringBuilder.append(", ");
            stringBuilder.append(end);
            throw new IndexOutOfBoundsException(stringBuilder.toString());
        }

        public void drawText(BaseCanvas c, int start, int end, float x, float y, Paint p) {
            c.drawText(this.mChars, start + this.mStart, end - start, x, y, p);
        }

        public void drawTextRun(BaseCanvas c, int start, int end, int contextStart, int contextEnd, float x, float y, boolean isRtl, Paint p) {
            int count = end - start;
            int contextCount = contextEnd - contextStart;
            char[] cArr = this.mChars;
            int i = this.mStart;
            c.drawTextRun(cArr, start + i, count, contextStart + i, contextCount, x, y, isRtl, p);
        }

        public float measureText(int start, int end, Paint p) {
            return p.measureText(this.mChars, this.mStart + start, end - start);
        }

        public int getTextWidths(int start, int end, float[] widths, Paint p) {
            return p.getTextWidths(this.mChars, this.mStart + start, end - start, widths);
        }

        public float getTextRunAdvances(int start, int end, int contextStart, int contextEnd, boolean isRtl, float[] advances, int advancesIndex, Paint p) {
            int count = end - start;
            int contextCount = contextEnd - contextStart;
            char[] cArr = this.mChars;
            int i = this.mStart;
            return p.getTextRunAdvances(cArr, start + i, count, contextStart + i, contextCount, isRtl, advances, advancesIndex);
        }

        public int getTextRunCursor(int contextStart, int contextEnd, boolean isRtl, int offset, int cursorOpt, Paint p) {
            int contextCount = contextEnd - contextStart;
            char[] cArr = this.mChars;
            int i = this.mStart;
            return p.getTextRunCursor(cArr, contextStart + i, contextCount, isRtl, offset + i, cursorOpt);
        }
    }

    static class Drawables {
        static final int BOTTOM = 3;
        static final int DRAWABLE_LEFT = 1;
        static final int DRAWABLE_NONE = -1;
        static final int DRAWABLE_RIGHT = 0;
        static final int LEFT = 0;
        static final int RIGHT = 2;
        static final int TOP = 1;
        BlendMode mBlendMode;
        final Rect mCompoundRect = new Rect();
        Drawable mDrawableEnd;
        Drawable mDrawableError;
        int mDrawableHeightEnd;
        int mDrawableHeightError;
        int mDrawableHeightLeft;
        int mDrawableHeightRight;
        int mDrawableHeightStart;
        int mDrawableHeightTemp;
        Drawable mDrawableLeftInitial;
        int mDrawablePadding;
        Drawable mDrawableRightInitial;
        int mDrawableSaved = -1;
        int mDrawableSizeBottom;
        int mDrawableSizeEnd;
        int mDrawableSizeError;
        int mDrawableSizeLeft;
        int mDrawableSizeRight;
        int mDrawableSizeStart;
        int mDrawableSizeTemp;
        int mDrawableSizeTop;
        Drawable mDrawableStart;
        Drawable mDrawableTemp;
        int mDrawableWidthBottom;
        int mDrawableWidthTop;
        boolean mHasTint;
        boolean mHasTintMode;
        boolean mIsRtlCompatibilityMode;
        boolean mOverride;
        final Drawable[] mShowing = new Drawable[4];
        ColorStateList mTintList;

        public Drawables(Context context) {
            boolean z = context.getApplicationInfo().targetSdkVersion < 17 || !context.getApplicationInfo().hasRtlSupport();
            this.mIsRtlCompatibilityMode = z;
            this.mOverride = false;
        }

        public boolean hasMetadata() {
            return this.mDrawablePadding != 0 || this.mHasTintMode || this.mHasTint;
        }

        public boolean resolveWithLayoutDirection(int layoutDirection) {
            Drawable[] drawableArr = this.mShowing;
            Drawable previousLeft = drawableArr[0];
            Drawable previousRight = drawableArr[2];
            drawableArr[0] = this.mDrawableLeftInitial;
            drawableArr[2] = this.mDrawableRightInitial;
            if (this.mIsRtlCompatibilityMode) {
                Drawable drawable = this.mDrawableStart;
                if (drawable != null && drawableArr[0] == null) {
                    drawableArr[0] = drawable;
                    this.mDrawableSizeLeft = this.mDrawableSizeStart;
                    this.mDrawableHeightLeft = this.mDrawableHeightStart;
                }
                Drawable drawable2 = this.mDrawableEnd;
                if (drawable2 != null) {
                    Drawable[] drawableArr2 = this.mShowing;
                    if (drawableArr2[2] == null) {
                        drawableArr2[2] = drawable2;
                        this.mDrawableSizeRight = this.mDrawableSizeEnd;
                        this.mDrawableHeightRight = this.mDrawableHeightEnd;
                    }
                }
            } else if (layoutDirection != 1) {
                if (this.mOverride) {
                    drawableArr[0] = this.mDrawableStart;
                    this.mDrawableSizeLeft = this.mDrawableSizeStart;
                    this.mDrawableHeightLeft = this.mDrawableHeightStart;
                    drawableArr[2] = this.mDrawableEnd;
                    this.mDrawableSizeRight = this.mDrawableSizeEnd;
                    this.mDrawableHeightRight = this.mDrawableHeightEnd;
                }
            } else if (this.mOverride) {
                drawableArr[2] = this.mDrawableStart;
                this.mDrawableSizeRight = this.mDrawableSizeStart;
                this.mDrawableHeightRight = this.mDrawableHeightStart;
                drawableArr[0] = this.mDrawableEnd;
                this.mDrawableSizeLeft = this.mDrawableSizeEnd;
                this.mDrawableHeightLeft = this.mDrawableHeightEnd;
            }
            applyErrorDrawableIfNeeded(layoutDirection);
            drawableArr = this.mShowing;
            if (drawableArr[0] == previousLeft && drawableArr[2] == previousRight) {
                return false;
            }
            return true;
        }

        public void setErrorDrawable(Drawable dr, TextView tv) {
            Drawable drawable = this.mDrawableError;
            if (!(drawable == dr || drawable == null)) {
                drawable.setCallback(null);
            }
            this.mDrawableError = dr;
            if (this.mDrawableError != null) {
                Rect compoundRect = this.mCompoundRect;
                this.mDrawableError.setState(tv.getDrawableState());
                this.mDrawableError.copyBounds(compoundRect);
                this.mDrawableError.setCallback(tv);
                this.mDrawableSizeError = compoundRect.width();
                this.mDrawableHeightError = compoundRect.height();
                return;
            }
            this.mDrawableHeightError = 0;
            this.mDrawableSizeError = 0;
        }

        private void applyErrorDrawableIfNeeded(int layoutDirection) {
            int i = this.mDrawableSaved;
            if (i == 0) {
                this.mShowing[2] = this.mDrawableTemp;
                this.mDrawableSizeRight = this.mDrawableSizeTemp;
                this.mDrawableHeightRight = this.mDrawableHeightTemp;
            } else if (i == 1) {
                this.mShowing[0] = this.mDrawableTemp;
                this.mDrawableSizeLeft = this.mDrawableSizeTemp;
                this.mDrawableHeightLeft = this.mDrawableHeightTemp;
            }
            Drawable drawable = this.mDrawableError;
            if (drawable == null) {
                return;
            }
            if (layoutDirection != 1) {
                this.mDrawableSaved = 0;
                Drawable[] drawableArr = this.mShowing;
                this.mDrawableTemp = drawableArr[2];
                this.mDrawableSizeTemp = this.mDrawableSizeRight;
                this.mDrawableHeightTemp = this.mDrawableHeightRight;
                drawableArr[2] = drawable;
                this.mDrawableSizeRight = this.mDrawableSizeError;
                this.mDrawableHeightRight = this.mDrawableHeightError;
                return;
            }
            this.mDrawableSaved = 1;
            Drawable[] drawableArr2 = this.mShowing;
            this.mDrawableTemp = drawableArr2[0];
            this.mDrawableSizeTemp = this.mDrawableSizeLeft;
            this.mDrawableHeightTemp = this.mDrawableHeightLeft;
            drawableArr2[0] = drawable;
            this.mDrawableSizeLeft = this.mDrawableSizeError;
            this.mDrawableHeightLeft = this.mDrawableHeightError;
        }
    }

    public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<TextView> {
        private int mAutoLinkId;
        private int mAutoSizeMaxTextSizeId;
        private int mAutoSizeMinTextSizeId;
        private int mAutoSizeStepGranularityId;
        private int mAutoSizeTextTypeId;
        private int mBreakStrategyId;
        private int mCursorVisibleId;
        private int mDrawableBlendModeId;
        private int mDrawablePaddingId;
        private int mDrawableTintId;
        private int mDrawableTintModeId;
        private int mElegantTextHeightId;
        private int mEllipsizeId;
        private int mFallbackLineSpacingId;
        private int mFirstBaselineToTopHeightId;
        private int mFontFeatureSettingsId;
        private int mFreezesTextId;
        private int mGravityId;
        private int mHintId;
        private int mHyphenationFrequencyId;
        private int mImeActionIdId;
        private int mImeActionLabelId;
        private int mImeOptionsId;
        private int mIncludeFontPaddingId;
        private int mInputTypeId;
        private int mJustificationModeId;
        private int mLastBaselineToBottomHeightId;
        private int mLetterSpacingId;
        private int mLineHeightId;
        private int mLineSpacingExtraId;
        private int mLineSpacingMultiplierId;
        private int mLinksClickableId;
        private int mMarqueeRepeatLimitId;
        private int mMaxEmsId;
        private int mMaxHeightId;
        private int mMaxLinesId;
        private int mMaxWidthId;
        private int mMinEmsId;
        private int mMinLinesId;
        private int mMinWidthId;
        private int mPrivateImeOptionsId;
        private boolean mPropertiesMapped = false;
        private int mScrollHorizontallyId;
        private int mShadowColorId;
        private int mShadowDxId;
        private int mShadowDyId;
        private int mShadowRadiusId;
        private int mSingleLineId;
        private int mTextAllCapsId;
        private int mTextColorHighlightId;
        private int mTextColorHintId;
        private int mTextColorId;
        private int mTextColorLinkId;
        private int mTextId;
        private int mTextIsSelectableId;
        private int mTextScaleXId;
        private int mTextSizeId;
        private int mTypefaceId;

        public void mapProperties(PropertyMapper propertyMapper) {
            PropertyMapper propertyMapper2 = propertyMapper;
            IntFlagMapping autoLinkFlagMapping = new IntFlagMapping();
            autoLinkFlagMapping.add(2, 2, "email");
            autoLinkFlagMapping.add(8, 8, "map");
            String str = "phone";
            autoLinkFlagMapping.add(4, 4, str);
            autoLinkFlagMapping.add(1, 1, "web");
            Objects.requireNonNull(autoLinkFlagMapping);
            this.mAutoLinkId = propertyMapper2.mapIntFlag("autoLink", 16842928, new -$$Lambda$gFNlJIKfxqleu304aRWP5R5v1yY(autoLinkFlagMapping));
            this.mAutoSizeMaxTextSizeId = propertyMapper2.mapInt("autoSizeMaxTextSize", 16844102);
            this.mAutoSizeMinTextSizeId = propertyMapper2.mapInt("autoSizeMinTextSize", 16844088);
            this.mAutoSizeStepGranularityId = propertyMapper2.mapInt("autoSizeStepGranularity", 16844086);
            SparseArray<String> autoSizeTextTypeEnumMapping = new SparseArray();
            String str2 = "none";
            autoSizeTextTypeEnumMapping.put(0, str2);
            autoSizeTextTypeEnumMapping.put(1, "uniform");
            Objects.requireNonNull(autoSizeTextTypeEnumMapping);
            this.mAutoSizeTextTypeId = propertyMapper2.mapIntEnum("autoSizeTextType", 16844085, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(autoSizeTextTypeEnumMapping));
            SparseArray<String> breakStrategyEnumMapping = new SparseArray();
            breakStrategyEnumMapping.put(0, "simple");
            breakStrategyEnumMapping.put(1, "high_quality");
            breakStrategyEnumMapping.put(2, "balanced");
            Objects.requireNonNull(breakStrategyEnumMapping);
            this.mBreakStrategyId = propertyMapper2.mapIntEnum("breakStrategy", 16843997, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(breakStrategyEnumMapping));
            this.mCursorVisibleId = propertyMapper2.mapBoolean("cursorVisible", 16843090);
            this.mDrawableBlendModeId = propertyMapper2.mapObject("drawableBlendMode", 80);
            this.mDrawablePaddingId = propertyMapper2.mapInt("drawablePadding", 16843121);
            this.mDrawableTintId = propertyMapper2.mapObject("drawableTint", 16843990);
            this.mDrawableTintModeId = propertyMapper2.mapObject("drawableTintMode", 16843991);
            this.mElegantTextHeightId = propertyMapper2.mapBoolean("elegantTextHeight", 16843869);
            this.mEllipsizeId = propertyMapper2.mapObject("ellipsize", 16842923);
            this.mFallbackLineSpacingId = propertyMapper2.mapBoolean("fallbackLineSpacing", 16844155);
            this.mFirstBaselineToTopHeightId = propertyMapper2.mapInt("firstBaselineToTopHeight", 16844157);
            this.mFontFeatureSettingsId = propertyMapper2.mapObject("fontFeatureSettings", 16843959);
            this.mFreezesTextId = propertyMapper2.mapBoolean("freezesText", 16843116);
            this.mGravityId = propertyMapper2.mapGravity("gravity", 16842927);
            this.mHintId = propertyMapper2.mapObject(Impl.COLUMN_FILE_NAME_HINT, 16843088);
            SparseArray<String> hyphenationFrequencyEnumMapping = new SparseArray();
            hyphenationFrequencyEnumMapping.put(0, str2);
            String str3 = "normal";
            hyphenationFrequencyEnumMapping.put(1, str3);
            hyphenationFrequencyEnumMapping.put(2, "full");
            Objects.requireNonNull(hyphenationFrequencyEnumMapping);
            this.mHyphenationFrequencyId = propertyMapper2.mapIntEnum("hyphenationFrequency", 16843998, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(hyphenationFrequencyEnumMapping));
            this.mImeActionIdId = propertyMapper2.mapInt("imeActionId", 16843366);
            this.mImeActionLabelId = propertyMapper2.mapObject("imeActionLabel", 16843365);
            IntFlagMapping imeOptionsFlagMapping = new IntFlagMapping();
            imeOptionsFlagMapping.add(255, 6, "actionDone");
            imeOptionsFlagMapping.add(255, 2, "actionGo");
            imeOptionsFlagMapping.add(255, 5, "actionNext");
            imeOptionsFlagMapping.add(255, 1, "actionNone");
            imeOptionsFlagMapping.add(255, 7, "actionPrevious");
            imeOptionsFlagMapping.add(255, 3, "actionSearch");
            imeOptionsFlagMapping.add(255, 4, "actionSend");
            imeOptionsFlagMapping.add(255, 0, "actionUnspecified");
            imeOptionsFlagMapping.add(Integer.MIN_VALUE, Integer.MIN_VALUE, "flagForceAscii");
            imeOptionsFlagMapping.add(134217728, 134217728, "flagNavigateNext");
            imeOptionsFlagMapping.add(67108864, 67108864, "flagNavigatePrevious");
            imeOptionsFlagMapping.add(536870912, 536870912, "flagNoAccessoryAction");
            imeOptionsFlagMapping.add(1073741824, 1073741824, "flagNoEnterAction");
            imeOptionsFlagMapping.add(268435456, 268435456, "flagNoExtractUi");
            imeOptionsFlagMapping.add(33554432, 33554432, "flagNoFullscreen");
            imeOptionsFlagMapping.add(16777216, 16777216, "flagNoPersonalizedLearning");
            imeOptionsFlagMapping.add(-1, 0, str3);
            Objects.requireNonNull(imeOptionsFlagMapping);
            this.mImeOptionsId = propertyMapper2.mapIntFlag("imeOptions", 16843364, new -$$Lambda$gFNlJIKfxqleu304aRWP5R5v1yY(imeOptionsFlagMapping));
            this.mIncludeFontPaddingId = propertyMapper2.mapBoolean("includeFontPadding", 16843103);
            IntFlagMapping inputTypeFlagMapping = new IntFlagMapping();
            inputTypeFlagMapping.add(4095, 20, "date");
            inputTypeFlagMapping.add(4095, 4, TextClassifier.TYPE_DATE_TIME);
            inputTypeFlagMapping.add(-1, 0, str2);
            inputTypeFlagMapping.add(4095, 2, "number");
            inputTypeFlagMapping.add(16773135, 8194, "numberDecimal");
            inputTypeFlagMapping.add(4095, 18, "numberPassword");
            inputTypeFlagMapping.add(16773135, 4098, "numberSigned");
            inputTypeFlagMapping.add(4095, 3, str);
            inputTypeFlagMapping.add(4095, 1, "text");
            inputTypeFlagMapping.add(16773135, 65537, "textAutoComplete");
            inputTypeFlagMapping.add(16773135, 32769, "textAutoCorrect");
            inputTypeFlagMapping.add(16773135, 4097, "textCapCharacters");
            inputTypeFlagMapping.add(16773135, 16385, "textCapSentences");
            inputTypeFlagMapping.add(16773135, 8193, "textCapWords");
            inputTypeFlagMapping.add(4095, 33, "textEmailAddress");
            inputTypeFlagMapping.add(4095, 49, "textEmailSubject");
            inputTypeFlagMapping.add(4095, 177, "textFilter");
            inputTypeFlagMapping.add(16773135, 262145, "textImeMultiLine");
            inputTypeFlagMapping.add(4095, 81, "textLongMessage");
            inputTypeFlagMapping.add(16773135, 131073, "textMultiLine");
            inputTypeFlagMapping.add(16773135, ConnectivityManager.CALLBACK_PRECHECK, "textNoSuggestions");
            inputTypeFlagMapping.add(4095, 129, "textPassword");
            inputTypeFlagMapping.add(4095, 97, "textPersonName");
            inputTypeFlagMapping.add(4095, 193, "textPhonetic");
            inputTypeFlagMapping.add(4095, 113, "textPostalAddress");
            inputTypeFlagMapping.add(4095, 65, "textShortMessage");
            inputTypeFlagMapping.add(4095, 17, "textUri");
            inputTypeFlagMapping.add(4095, 145, "textVisiblePassword");
            inputTypeFlagMapping.add(4095, 161, "textWebEditText");
            inputTypeFlagMapping.add(4095, 209, "textWebEmailAddress");
            inputTypeFlagMapping.add(4095, 225, "textWebPassword");
            inputTypeFlagMapping.add(4095, 36, "time");
            Objects.requireNonNull(inputTypeFlagMapping);
            this.mInputTypeId = propertyMapper2.mapIntFlag("inputType", 16843296, new -$$Lambda$gFNlJIKfxqleu304aRWP5R5v1yY(inputTypeFlagMapping));
            SparseArray<String> justificationModeEnumMapping = new SparseArray();
            justificationModeEnumMapping.put(0, str2);
            justificationModeEnumMapping.put(1, "inter_word");
            Objects.requireNonNull(justificationModeEnumMapping);
            this.mJustificationModeId = propertyMapper2.mapIntEnum("justificationMode", 16844135, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(justificationModeEnumMapping));
            this.mLastBaselineToBottomHeightId = propertyMapper2.mapInt("lastBaselineToBottomHeight", 16844158);
            this.mLetterSpacingId = propertyMapper2.mapFloat("letterSpacing", 16843958);
            this.mLineHeightId = propertyMapper2.mapInt("lineHeight", 16844159);
            this.mLineSpacingExtraId = propertyMapper2.mapFloat("lineSpacingExtra", 16843287);
            this.mLineSpacingMultiplierId = propertyMapper2.mapFloat("lineSpacingMultiplier", 16843288);
            this.mLinksClickableId = propertyMapper2.mapBoolean("linksClickable", 16842929);
            this.mMarqueeRepeatLimitId = propertyMapper2.mapInt("marqueeRepeatLimit", 16843293);
            this.mMaxEmsId = propertyMapper2.mapInt("maxEms", 16843095);
            this.mMaxHeightId = propertyMapper2.mapInt("maxHeight", 16843040);
            this.mMaxLinesId = propertyMapper2.mapInt("maxLines", 16843091);
            this.mMaxWidthId = propertyMapper2.mapInt("maxWidth", 16843039);
            this.mMinEmsId = propertyMapper2.mapInt("minEms", 16843098);
            this.mMinLinesId = propertyMapper2.mapInt("minLines", 16843094);
            this.mMinWidthId = propertyMapper2.mapInt("minWidth", 16843071);
            this.mPrivateImeOptionsId = propertyMapper2.mapObject("privateImeOptions", 16843299);
            this.mScrollHorizontallyId = propertyMapper2.mapBoolean("scrollHorizontally", 16843099);
            this.mShadowColorId = propertyMapper2.mapColor("shadowColor", 16843105);
            this.mShadowDxId = propertyMapper2.mapFloat("shadowDx", 16843106);
            this.mShadowDyId = propertyMapper2.mapFloat("shadowDy", 16843107);
            this.mShadowRadiusId = propertyMapper2.mapFloat("shadowRadius", 16843108);
            this.mSingleLineId = propertyMapper2.mapBoolean("singleLine", 16843101);
            this.mTextId = propertyMapper2.mapObject("text", 16843087);
            this.mTextAllCapsId = propertyMapper2.mapBoolean("textAllCaps", 16843660);
            this.mTextColorId = propertyMapper2.mapObject("textColor", 16842904);
            this.mTextColorHighlightId = propertyMapper2.mapColor("textColorHighlight", 16842905);
            this.mTextColorHintId = propertyMapper2.mapObject("textColorHint", 16842906);
            this.mTextColorLinkId = propertyMapper2.mapObject("textColorLink", 16842907);
            this.mTextIsSelectableId = propertyMapper2.mapBoolean("textIsSelectable", 16843542);
            this.mTextScaleXId = propertyMapper2.mapFloat("textScaleX", 16843089);
            this.mTextSizeId = propertyMapper2.mapFloat("textSize", 16842901);
            this.mTypefaceId = propertyMapper2.mapObject("typeface", 16842902);
            this.mPropertiesMapped = true;
        }

        public void readProperties(TextView node, PropertyReader propertyReader) {
            if (this.mPropertiesMapped) {
                propertyReader.readIntFlag(this.mAutoLinkId, node.getAutoLinkMask());
                propertyReader.readInt(this.mAutoSizeMaxTextSizeId, node.getAutoSizeMaxTextSize());
                propertyReader.readInt(this.mAutoSizeMinTextSizeId, node.getAutoSizeMinTextSize());
                propertyReader.readInt(this.mAutoSizeStepGranularityId, node.getAutoSizeStepGranularity());
                propertyReader.readIntEnum(this.mAutoSizeTextTypeId, node.getAutoSizeTextType());
                propertyReader.readIntEnum(this.mBreakStrategyId, node.getBreakStrategy());
                propertyReader.readBoolean(this.mCursorVisibleId, node.isCursorVisible());
                propertyReader.readObject(this.mDrawableBlendModeId, node.getCompoundDrawableTintBlendMode());
                propertyReader.readInt(this.mDrawablePaddingId, node.getCompoundDrawablePadding());
                propertyReader.readObject(this.mDrawableTintId, node.getCompoundDrawableTintList());
                propertyReader.readObject(this.mDrawableTintModeId, node.getCompoundDrawableTintMode());
                propertyReader.readBoolean(this.mElegantTextHeightId, node.isElegantTextHeight());
                propertyReader.readObject(this.mEllipsizeId, node.getEllipsize());
                propertyReader.readBoolean(this.mFallbackLineSpacingId, node.isFallbackLineSpacing());
                propertyReader.readInt(this.mFirstBaselineToTopHeightId, node.getFirstBaselineToTopHeight());
                propertyReader.readObject(this.mFontFeatureSettingsId, node.getFontFeatureSettings());
                propertyReader.readBoolean(this.mFreezesTextId, node.getFreezesText());
                propertyReader.readGravity(this.mGravityId, node.getGravity());
                propertyReader.readObject(this.mHintId, node.getHint());
                propertyReader.readIntEnum(this.mHyphenationFrequencyId, node.getHyphenationFrequency());
                propertyReader.readInt(this.mImeActionIdId, node.getImeActionId());
                propertyReader.readObject(this.mImeActionLabelId, node.getImeActionLabel());
                propertyReader.readIntFlag(this.mImeOptionsId, node.getImeOptions());
                propertyReader.readBoolean(this.mIncludeFontPaddingId, node.getIncludeFontPadding());
                propertyReader.readIntFlag(this.mInputTypeId, node.getInputType());
                propertyReader.readIntEnum(this.mJustificationModeId, node.getJustificationMode());
                propertyReader.readInt(this.mLastBaselineToBottomHeightId, node.getLastBaselineToBottomHeight());
                propertyReader.readFloat(this.mLetterSpacingId, node.getLetterSpacing());
                propertyReader.readInt(this.mLineHeightId, node.getLineHeight());
                propertyReader.readFloat(this.mLineSpacingExtraId, node.getLineSpacingExtra());
                propertyReader.readFloat(this.mLineSpacingMultiplierId, node.getLineSpacingMultiplier());
                propertyReader.readBoolean(this.mLinksClickableId, node.getLinksClickable());
                propertyReader.readInt(this.mMarqueeRepeatLimitId, node.getMarqueeRepeatLimit());
                propertyReader.readInt(this.mMaxEmsId, node.getMaxEms());
                propertyReader.readInt(this.mMaxHeightId, node.getMaxHeight());
                propertyReader.readInt(this.mMaxLinesId, node.getMaxLines());
                propertyReader.readInt(this.mMaxWidthId, node.getMaxWidth());
                propertyReader.readInt(this.mMinEmsId, node.getMinEms());
                propertyReader.readInt(this.mMinLinesId, node.getMinLines());
                propertyReader.readInt(this.mMinWidthId, node.getMinWidth());
                propertyReader.readObject(this.mPrivateImeOptionsId, node.getPrivateImeOptions());
                propertyReader.readBoolean(this.mScrollHorizontallyId, node.isHorizontallyScrollable());
                propertyReader.readColor(this.mShadowColorId, node.getShadowColor());
                propertyReader.readFloat(this.mShadowDxId, node.getShadowDx());
                propertyReader.readFloat(this.mShadowDyId, node.getShadowDy());
                propertyReader.readFloat(this.mShadowRadiusId, node.getShadowRadius());
                propertyReader.readBoolean(this.mSingleLineId, node.isSingleLine());
                propertyReader.readObject(this.mTextId, node.getText());
                propertyReader.readBoolean(this.mTextAllCapsId, node.isAllCaps());
                propertyReader.readObject(this.mTextColorId, node.getTextColors());
                propertyReader.readColor(this.mTextColorHighlightId, node.getHighlightColor());
                propertyReader.readObject(this.mTextColorHintId, node.getHintTextColors());
                propertyReader.readObject(this.mTextColorLinkId, node.getLinkTextColors());
                propertyReader.readBoolean(this.mTextIsSelectableId, node.isTextSelectable());
                propertyReader.readFloat(this.mTextScaleXId, node.getTextScaleX());
                propertyReader.readFloat(this.mTextSizeId, node.getTextSize());
                propertyReader.readObject(this.mTypefaceId, node.getTypeface());
                return;
            }
            throw new UninitializedPropertyMapException();
        }
    }

    private static final class Marquee {
        private static final int MARQUEE_DELAY = 1200;
        private static final float MARQUEE_DELTA_MAX = 0.07f;
        private static final int MARQUEE_DP_PER_SECOND = 30;
        private static final byte MARQUEE_RUNNING = (byte) 2;
        private static final byte MARQUEE_STARTING = (byte) 1;
        private static final byte MARQUEE_STOPPED = (byte) 0;
        private final Choreographer mChoreographer;
        private float mFadeStop;
        private float mGhostOffset;
        private float mGhostStart;
        private long mLastAnimationMs;
        private float mMaxFadeScroll;
        private float mMaxScroll;
        private final float mPixelsPerMs;
        private int mRepeatLimit;
        private FrameCallback mRestartCallback = new FrameCallback() {
            public void doFrame(long frameTimeNanos) {
                if (Marquee.this.mStatus == (byte) 2) {
                    if (Marquee.this.mRepeatLimit >= 0) {
                        Marquee.this.mRepeatLimit = Marquee.this.mRepeatLimit - 1;
                    }
                    Marquee marquee = Marquee.this;
                    marquee.start(marquee.mRepeatLimit);
                }
            }
        };
        private float mScroll;
        private FrameCallback mStartCallback = new FrameCallback() {
            public void doFrame(long frameTimeNanos) {
                Marquee.this.mStatus = (byte) 2;
                Marquee marquee = Marquee.this;
                marquee.mLastAnimationMs = marquee.mChoreographer.getFrameTime();
                Marquee.this.tick();
            }
        };
        private byte mStatus = (byte) 0;
        private FrameCallback mTickCallback = new FrameCallback() {
            public void doFrame(long frameTimeNanos) {
                Marquee.this.tick();
            }
        };
        private final WeakReference<TextView> mView;

        Marquee(TextView v) {
            this.mPixelsPerMs = (30.0f * v.getContext().getResources().getDisplayMetrics().density) / 1000.0f;
            this.mView = new WeakReference(v);
            this.mChoreographer = Choreographer.getInstance();
        }

        /* Access modifiers changed, original: 0000 */
        public void tick() {
            if (this.mStatus == (byte) 2) {
                this.mChoreographer.removeFrameCallback(this.mTickCallback);
                TextView textView = (TextView) this.mView.get();
                if (textView != null && (textView.isFocused() || textView.isSelected())) {
                    long currentMs = this.mChoreographer.getFrameTime();
                    long deltaMs = currentMs - this.mLastAnimationMs;
                    this.mLastAnimationMs = currentMs;
                    this.mScroll += ((float) deltaMs) * this.mPixelsPerMs;
                    float f = this.mScroll;
                    float f2 = this.mMaxScroll;
                    if (f > f2) {
                        this.mScroll = f2;
                        this.mChoreographer.postFrameCallbackDelayed(this.mRestartCallback, 1200);
                    } else {
                        this.mChoreographer.postFrameCallback(this.mTickCallback);
                    }
                    textView.invalidate();
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void stop() {
            this.mStatus = (byte) 0;
            this.mChoreographer.removeFrameCallback(this.mStartCallback);
            this.mChoreographer.removeFrameCallback(this.mRestartCallback);
            this.mChoreographer.removeFrameCallback(this.mTickCallback);
            resetScroll();
        }

        private void resetScroll() {
            this.mScroll = 0.0f;
            TextView textView = (TextView) this.mView.get();
            if (textView != null) {
                textView.invalidate();
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void start(int repeatLimit) {
            if (repeatLimit == 0) {
                stop();
                return;
            }
            this.mRepeatLimit = repeatLimit;
            TextView textView = (TextView) this.mView.get();
            if (!(textView == null || textView.mLayout == null)) {
                this.mStatus = (byte) 1;
                this.mScroll = 0.0f;
                int textWidth = (textView.getWidth() - textView.getCompoundPaddingLeft()) - textView.getCompoundPaddingRight();
                float lineWidth = textView.mLayout.getLineWidth(0);
                float gap = ((float) textWidth) / 3.0f;
                this.mGhostStart = (lineWidth - ((float) textWidth)) + gap;
                float f = this.mGhostStart;
                this.mMaxScroll = ((float) textWidth) + f;
                this.mGhostOffset = lineWidth + gap;
                this.mFadeStop = (((float) textWidth) / 6.0f) + lineWidth;
                this.mMaxFadeScroll = (f + lineWidth) + lineWidth;
                textView.invalidate();
                this.mChoreographer.postFrameCallback(this.mStartCallback);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public float getGhostOffset() {
            return this.mGhostOffset;
        }

        /* Access modifiers changed, original: 0000 */
        public float getScroll() {
            return this.mScroll;
        }

        /* Access modifiers changed, original: 0000 */
        public float getMaxFadeScroll() {
            return this.mMaxFadeScroll;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean shouldDrawLeftFade() {
            return this.mScroll <= this.mFadeStop;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean shouldDrawGhost() {
            return this.mStatus == (byte) 2 && this.mScroll > this.mGhostStart;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean isRunning() {
            return this.mStatus == (byte) 2;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean isStopped() {
            return this.mStatus == (byte) 0;
        }
    }

    public static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, null);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        ParcelableParcel editorState;
        CharSequence error;
        boolean frozenWithFocus;
        int selEnd;
        int selStart;
        @UnsupportedAppUsage
        CharSequence text;

        /* synthetic */ SavedState(Parcel x0, AnonymousClass1 x1) {
            this(x0);
        }

        SavedState(Parcelable superState) {
            super(superState);
            this.selStart = -1;
            this.selEnd = -1;
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.selStart);
            out.writeInt(this.selEnd);
            out.writeInt(this.frozenWithFocus);
            TextUtils.writeToParcel(this.text, out, flags);
            if (this.error == null) {
                out.writeInt(0);
            } else {
                out.writeInt(1);
                TextUtils.writeToParcel(this.error, out, flags);
            }
            if (this.editorState == null) {
                out.writeInt(0);
                return;
            }
            out.writeInt(1);
            this.editorState.writeToParcel(out, flags);
        }

        public String toString() {
            StringBuilder stringBuilder;
            String str = new StringBuilder();
            str.append("TextView.SavedState{");
            str.append(Integer.toHexString(System.identityHashCode(this)));
            str.append(" start=");
            str.append(this.selStart);
            str.append(" end=");
            str.append(this.selEnd);
            str = str.toString();
            if (this.text != null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(" text=");
                stringBuilder.append(this.text);
                str = stringBuilder.toString();
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

        private SavedState(Parcel in) {
            super(in);
            this.selStart = -1;
            this.selEnd = -1;
            this.selStart = in.readInt();
            this.selEnd = in.readInt();
            this.frozenWithFocus = in.readInt() != 0;
            this.text = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            if (in.readInt() != 0) {
                this.error = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            }
            if (in.readInt() != 0) {
                this.editorState = (ParcelableParcel) ParcelableParcel.CREATOR.createFromParcel(in);
            }
        }
    }

    private static class TextAppearanceAttributes {
        boolean mAllCaps;
        boolean mElegant;
        boolean mFallbackLineSpacing;
        String mFontFamily;
        boolean mFontFamilyExplicit;
        String mFontFeatureSettings;
        Typeface mFontTypeface;
        String mFontVariationSettings;
        int mFontWeight;
        boolean mHasElegant;
        boolean mHasFallbackLineSpacing;
        boolean mHasLetterSpacing;
        float mLetterSpacing;
        int mShadowColor;
        float mShadowDx;
        float mShadowDy;
        float mShadowRadius;
        ColorStateList mTextColor;
        int mTextColorHighlight;
        ColorStateList mTextColorHint;
        ColorStateList mTextColorLink;
        LocaleList mTextLocales;
        int mTextSize;
        int mTextStyle;
        int mTypefaceIndex;

        private TextAppearanceAttributes() {
            this.mTextColorHighlight = 0;
            this.mTextColor = null;
            this.mTextColorHint = null;
            this.mTextColorLink = null;
            this.mTextSize = -1;
            this.mTextLocales = null;
            this.mFontFamily = null;
            this.mFontTypeface = null;
            this.mFontFamilyExplicit = false;
            this.mTypefaceIndex = -1;
            this.mTextStyle = 0;
            this.mFontWeight = -1;
            this.mAllCaps = false;
            this.mShadowColor = 0;
            this.mShadowDx = 0.0f;
            this.mShadowDy = 0.0f;
            this.mShadowRadius = 0.0f;
            this.mHasElegant = false;
            this.mElegant = false;
            this.mHasFallbackLineSpacing = false;
            this.mFallbackLineSpacing = false;
            this.mHasLetterSpacing = false;
            this.mLetterSpacing = 0.0f;
            this.mFontFeatureSettings = null;
            this.mFontVariationSettings = null;
        }

        /* synthetic */ TextAppearanceAttributes(AnonymousClass1 x0) {
            this();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("TextAppearanceAttributes {\n    mTextColorHighlight:");
            stringBuilder.append(this.mTextColorHighlight);
            stringBuilder.append("\n    mTextColor:");
            stringBuilder.append(this.mTextColor);
            stringBuilder.append("\n    mTextColorHint:");
            stringBuilder.append(this.mTextColorHint);
            stringBuilder.append("\n    mTextColorLink:");
            stringBuilder.append(this.mTextColorLink);
            stringBuilder.append("\n    mTextSize:");
            stringBuilder.append(this.mTextSize);
            stringBuilder.append("\n    mTextLocales:");
            stringBuilder.append(this.mTextLocales);
            stringBuilder.append("\n    mFontFamily:");
            stringBuilder.append(this.mFontFamily);
            stringBuilder.append("\n    mFontTypeface:");
            stringBuilder.append(this.mFontTypeface);
            stringBuilder.append("\n    mFontFamilyExplicit:");
            stringBuilder.append(this.mFontFamilyExplicit);
            stringBuilder.append("\n    mTypefaceIndex:");
            stringBuilder.append(this.mTypefaceIndex);
            stringBuilder.append("\n    mTextStyle:");
            stringBuilder.append(this.mTextStyle);
            stringBuilder.append("\n    mFontWeight:");
            stringBuilder.append(this.mFontWeight);
            stringBuilder.append("\n    mAllCaps:");
            stringBuilder.append(this.mAllCaps);
            stringBuilder.append("\n    mShadowColor:");
            stringBuilder.append(this.mShadowColor);
            stringBuilder.append("\n    mShadowDx:");
            stringBuilder.append(this.mShadowDx);
            stringBuilder.append("\n    mShadowDy:");
            stringBuilder.append(this.mShadowDy);
            stringBuilder.append("\n    mShadowRadius:");
            stringBuilder.append(this.mShadowRadius);
            stringBuilder.append("\n    mHasElegant:");
            stringBuilder.append(this.mHasElegant);
            stringBuilder.append("\n    mElegant:");
            stringBuilder.append(this.mElegant);
            stringBuilder.append("\n    mHasFallbackLineSpacing:");
            stringBuilder.append(this.mHasFallbackLineSpacing);
            stringBuilder.append("\n    mFallbackLineSpacing:");
            stringBuilder.append(this.mFallbackLineSpacing);
            stringBuilder.append("\n    mHasLetterSpacing:");
            stringBuilder.append(this.mHasLetterSpacing);
            stringBuilder.append("\n    mLetterSpacing:");
            stringBuilder.append(this.mLetterSpacing);
            stringBuilder.append("\n    mFontFeatureSettings:");
            stringBuilder.append(this.mFontFeatureSettings);
            stringBuilder.append("\n    mFontVariationSettings:");
            stringBuilder.append(this.mFontVariationSettings);
            stringBuilder.append("\n}");
            return stringBuilder.toString();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface XMLTypefaceAttr {
    }

    static {
        sAppearanceValues.put(6, 4);
        sAppearanceValues.put(5, 3);
        sAppearanceValues.put(7, 5);
        sAppearanceValues.put(8, 6);
        sAppearanceValues.put(2, 0);
        sAppearanceValues.put(96, 19);
        sAppearanceValues.put(3, 1);
        sAppearanceValues.put(75, 12);
        sAppearanceValues.put(4, 2);
        sAppearanceValues.put(95, 18);
        sAppearanceValues.put(72, 11);
        sAppearanceValues.put(36, 7);
        sAppearanceValues.put(37, 8);
        sAppearanceValues.put(38, 9);
        sAppearanceValues.put(39, 10);
        sAppearanceValues.put(76, 13);
        sAppearanceValues.put(91, 17);
        sAppearanceValues.put(77, 14);
        sAppearanceValues.put(78, 15);
        sAppearanceValues.put(90, 16);
    }

    public static void preloadFontCache() {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT);
        p.measureText("H");
    }

    public TextView(Context context) {
        this(context, null);
    }

    public TextView(Context context, AttributeSet attrs) {
        this(context, attrs, 16842884);
    }

    public TextView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    /* JADX WARNING: Removed duplicated region for block: B:303:0x0b5d  */
    /* JADX WARNING: Removed duplicated region for block: B:302:0x0b55  */
    /* JADX WARNING: Removed duplicated region for block: B:306:0x0b66  */
    /* JADX WARNING: Removed duplicated region for block: B:309:0x0b85  */
    /* JADX WARNING: Removed duplicated region for block: B:308:0x0b6f  */
    /* JADX WARNING: Removed duplicated region for block: B:313:0x0ba0  */
    /* JADX WARNING: Removed duplicated region for block: B:312:0x0b99  */
    /* JADX WARNING: Removed duplicated region for block: B:316:0x0ba8  */
    /* JADX WARNING: Removed duplicated region for block: B:318:0x0bac  */
    /* JADX WARNING: Removed duplicated region for block: B:321:0x0bb7  */
    /* JADX WARNING: Removed duplicated region for block: B:320:0x0bb1  */
    /* JADX WARNING: Removed duplicated region for block: B:340:0x0c13  */
    /* JADX WARNING: Removed duplicated region for block: B:363:0x0c7d  */
    /* JADX WARNING: Removed duplicated region for block: B:366:0x0c8a  */
    /* JADX WARNING: Removed duplicated region for block: B:370:0x0c98  */
    /* JADX WARNING: Removed duplicated region for block: B:369:0x0c93  */
    /* JADX WARNING: Removed duplicated region for block: B:391:0x0ceb  */
    /* JADX WARNING: Removed duplicated region for block: B:373:0x0c9f  */
    /* JADX WARNING: Removed duplicated region for block: B:394:0x0cf2  */
    /* JADX WARNING: Removed duplicated region for block: B:397:0x0cf9  */
    /* JADX WARNING: Removed duplicated region for block: B:441:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:400:0x0d00  */
    /* JADX WARNING: Removed duplicated region for block: B:243:0x0a60  */
    /* JADX WARNING: Removed duplicated region for block: B:242:0x0a52  */
    /* JADX WARNING: Removed duplicated region for block: B:248:0x0a7d  */
    /* JADX WARNING: Removed duplicated region for block: B:245:0x0a6c  */
    /* JADX WARNING: Removed duplicated region for block: B:256:0x0a9d  */
    /* JADX WARNING: Removed duplicated region for block: B:255:0x0a91  */
    /* JADX WARNING: Removed duplicated region for block: B:259:0x0aad  */
    /* JADX WARNING: Removed duplicated region for block: B:258:0x0aa3  */
    /* JADX WARNING: Removed duplicated region for block: B:261:0x0ab2  */
    /* JADX WARNING: Removed duplicated region for block: B:285:0x0b25  */
    /* JADX WARNING: Removed duplicated region for block: B:272:0x0af0  */
    /* JADX WARNING: Removed duplicated region for block: B:299:0x0b4e  */
    /* JADX WARNING: Removed duplicated region for block: B:293:0x0b39  */
    /* JADX WARNING: Removed duplicated region for block: B:302:0x0b55  */
    /* JADX WARNING: Removed duplicated region for block: B:303:0x0b5d  */
    /* JADX WARNING: Removed duplicated region for block: B:306:0x0b66  */
    /* JADX WARNING: Removed duplicated region for block: B:308:0x0b6f  */
    /* JADX WARNING: Removed duplicated region for block: B:309:0x0b85  */
    /* JADX WARNING: Removed duplicated region for block: B:312:0x0b99  */
    /* JADX WARNING: Removed duplicated region for block: B:313:0x0ba0  */
    /* JADX WARNING: Removed duplicated region for block: B:316:0x0ba8  */
    /* JADX WARNING: Removed duplicated region for block: B:318:0x0bac  */
    /* JADX WARNING: Removed duplicated region for block: B:320:0x0bb1  */
    /* JADX WARNING: Removed duplicated region for block: B:321:0x0bb7  */
    /* JADX WARNING: Removed duplicated region for block: B:324:0x0bcf  */
    /* JADX WARNING: Removed duplicated region for block: B:329:0x0bdb  */
    /* JADX WARNING: Removed duplicated region for block: B:334:0x0be9  */
    /* JADX WARNING: Removed duplicated region for block: B:340:0x0c13  */
    /* JADX WARNING: Removed duplicated region for block: B:363:0x0c7d  */
    /* JADX WARNING: Removed duplicated region for block: B:366:0x0c8a  */
    /* JADX WARNING: Removed duplicated region for block: B:369:0x0c93  */
    /* JADX WARNING: Removed duplicated region for block: B:370:0x0c98  */
    /* JADX WARNING: Removed duplicated region for block: B:373:0x0c9f  */
    /* JADX WARNING: Removed duplicated region for block: B:391:0x0ceb  */
    /* JADX WARNING: Removed duplicated region for block: B:394:0x0cf2  */
    /* JADX WARNING: Removed duplicated region for block: B:397:0x0cf9  */
    /* JADX WARNING: Removed duplicated region for block: B:400:0x0d00  */
    /* JADX WARNING: Removed duplicated region for block: B:441:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Missing block: B:296:0x0b47, code skipped:
            if ((r0.mInputType & 4095) == 129) goto L_0x0b52;
     */
    public TextView(android.content.Context r69, android.util.AttributeSet r70, int r71, int r72) {
        /*
        r68 = this;
        r8 = r68;
        r9 = r69;
        r10 = r70;
        r11 = r71;
        r12 = r72;
        r13 = "Failure reading input extras";
        r14 = "TextView";
        r68.<init>(r69, r70, r71, r72);
        r0 = android.text.Editable.Factory.getInstance();
        r8.mEditableFactory = r0;
        r0 = android.text.Spannable.Factory.getInstance();
        r8.mSpannableFactory = r0;
        r15 = 3;
        r8.mMarqueeRepeatLimit = r15;
        r7 = -1;
        r8.mLastLayoutDirection = r7;
        r6 = 0;
        r8.mMarqueeFadeMode = r6;
        r0 = android.widget.TextView.BufferType.NORMAL;
        r8.mBufferType = r0;
        r8.mLocalesChanged = r6;
        r8.mListenerChanged = r6;
        r0 = 8388659; // 0x800033 float:1.1755015E-38 double:4.144548E-317;
        r8.mGravity = r0;
        r5 = 1;
        r8.mLinksClickable = r5;
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r8.mSpacingMult = r4;
        r0 = 0;
        r8.mSpacingAdd = r0;
        r0 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r8.mMaximum = r0;
        r8.mMaxMode = r5;
        r8.mMinimum = r6;
        r8.mMinMode = r5;
        r1 = r8.mMaximum;
        r8.mOldMaximum = r1;
        r1 = r8.mMaxMode;
        r8.mOldMaxMode = r1;
        r8.mMaxWidth = r0;
        r3 = 2;
        r8.mMaxWidthMode = r3;
        r8.mMinWidth = r6;
        r8.mMinWidthMode = r3;
        r8.mDesiredHeightAtMeasure = r7;
        r8.mIncludePad = r5;
        r8.mDeferScroll = r7;
        r0 = NO_FILTERS;
        r8.mFilters = r0;
        r0 = 1714664933; // 0x6633b5e5 float:2.1216474E23 double:8.471570375E-315;
        r8.mHighlightColor = r0;
        r8.mHighlightPathBogus = r5;
        r8.mDeviceProvisionedState = r6;
        r8.mAutoSizeTextType = r6;
        r8.mNeedsAutoSizeText = r6;
        r2 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r8.mAutoSizeStepGranularityInPx = r2;
        r8.mAutoSizeMinTextSizeInPx = r2;
        r8.mAutoSizeMaxTextSizeInPx = r2;
        r0 = libcore.util.EmptyArray.INT;
        r8.mAutoSizeTextSizesInPx = r0;
        r8.mHasPresetAutoSizeValues = r6;
        r8.mTextSetFromXmlOrResourceId = r6;
        r8.mTextId = r6;
        r0 = r68.getImportantForAutofill();
        if (r0 != 0) goto L_0x008d;
    L_0x008a:
        r8.setImportantForAutofill(r5);
    L_0x008d:
        r1 = "";
        r8.setTextInternal(r1);
        r16 = r68.getResources();
        r15 = r16.getCompatibilityInfo();
        r0 = new android.text.TextPaint;
        r0.<init>(r5);
        r8.mTextPaint = r0;
        r0 = r8.mTextPaint;
        r2 = r16.getDisplayMetrics();
        r2 = r2.density;
        r0.density = r2;
        r0 = r8.mTextPaint;
        r2 = r15.applicationScale;
        r0.setCompatibilityScaling(r2);
        r0 = new android.graphics.Paint;
        r0.<init>(r5);
        r8.mHighlightPaint = r0;
        r0 = r8.mHighlightPaint;
        r2 = r15.applicationScale;
        r0.setCompatibilityScaling(r2);
        r0 = r68.getDefaultMovementMethod();
        r8.mMovement = r0;
        r2 = 0;
        r8.mTransformation = r2;
        r0 = new android.widget.TextView$TextAppearanceAttributes;
        r0.<init>(r2);
        r19 = r0;
        r0 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
        r0 = android.content.res.ColorStateList.valueOf(r0);
        r6 = r19;
        r6.mTextColor = r0;
        r0 = 15;
        r6.mTextSize = r0;
        r7 = 0;
        r8.mBreakStrategy = r7;
        r8.mHyphenationFrequency = r7;
        r8.mJustificationMode = r7;
        r20 = r15;
        r15 = r69.getTheme();
        r0 = com.android.internal.R.styleable.TextViewAppearance;
        r0 = r15.obtainStyledAttributes(r10, r0, r11, r12);
        r21 = com.android.internal.R.styleable.TextViewAppearance;
        r22 = r1;
        r1 = r68;
        r18 = r13;
        r13 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r2 = r69;
        r3 = r21;
        r4 = r70;
        r13 = r5;
        r5 = r0;
        r13 = r7;
        r7 = r6;
        r6 = r71;
        r25 = r7;
        r13 = -1;
        r7 = r72;
        r1.saveAttributeDataForStyleable(r2, r3, r4, r5, r6, r7);
        r1 = 0;
        r2 = 0;
        r7 = r0.getResourceId(r2, r13);
        r0.recycle();
        if (r7 == r13) goto L_0x0132;
    L_0x011a:
        r2 = com.android.internal.R.styleable.TextAppearance;
        r26 = r15.obtainStyledAttributes(r7, r2);
        r3 = com.android.internal.R.styleable.TextAppearance;
        r4 = 0;
        r6 = 0;
        r1 = r68;
        r2 = r69;
        r5 = r26;
        r27 = r7;
        r1.saveAttributeDataForStyleable(r2, r3, r4, r5, r6, r7);
        r7 = r26;
        goto L_0x0135;
    L_0x0132:
        r27 = r7;
        r7 = r1;
    L_0x0135:
        if (r7 == 0) goto L_0x0143;
    L_0x0137:
        r6 = r25;
        r1 = 0;
        r8.readTextAppearance(r9, r7, r6, r1);
        r6.mFontFamilyExplicit = r1;
        r7.recycle();
        goto L_0x0145;
    L_0x0143:
        r6 = r25;
    L_0x0145:
        r25 = r68.getDefaultEditable();
        r26 = 0;
        r28 = 0;
        r29 = 0;
        r30 = 0;
        r31 = 0;
        r32 = -1;
        r33 = 0;
        r34 = 0;
        r35 = 0;
        r36 = 0;
        r37 = 0;
        r38 = 0;
        r39 = 0;
        r40 = 0;
        r41 = 0;
        r42 = 0;
        r43 = 0;
        r44 = -1;
        r45 = 0;
        r46 = -1;
        r47 = "";
        r48 = 0;
        r49 = 0;
        r50 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r51 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r52 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r53 = 0;
        r1 = com.android.internal.R.styleable.TextView;
        r5 = r15.obtainStyledAttributes(r10, r1, r11, r12);
        r3 = com.android.internal.R.styleable.TextView;
        r1 = r68;
        r2 = r69;
        r4 = r70;
        r54 = r5;
        r13 = r6;
        r6 = r71;
        r56 = r7;
        r7 = r72;
        r1.saveAttributeDataForStyleable(r2, r3, r4, r5, r6, r7);
        r0 = -1;
        r1 = -1;
        r2 = -1;
        r3 = r54;
        r4 = 1;
        r8.readTextAppearance(r9, r3, r13, r4);
        r4 = r3.getIndexCount();
        r5 = 0;
        r6 = 0;
        r64 = r0;
        r65 = r1;
        r66 = r2;
        r7 = r30;
        r9 = r31;
        r10 = r34;
        r59 = r35;
        r57 = r36;
        r60 = r37;
        r58 = r38;
        r61 = r39;
        r62 = r40;
        r63 = r41;
        r2 = r42;
        r1 = r43;
        r35 = r44;
        r12 = r45;
        r31 = r46;
        r11 = r49;
        r34 = r53;
        r30 = r29;
        r36 = r33;
        r29 = r47;
        r33 = r13;
        r13 = r32;
        r32 = r26;
        r26 = r48;
        r67 = r28;
        r28 = r5;
        r5 = r25;
        r25 = r15;
        r15 = r67;
    L_0x01e8:
        if (r6 >= r4) goto L_0x088a;
    L_0x01ea:
        r37 = r4;
        r4 = r3.getIndex(r6);
        if (r4 == 0) goto L_0x0865;
    L_0x01f2:
        r0 = 67;
        if (r4 == r0) goto L_0x0852;
    L_0x01f6:
        r0 = 70;
        if (r4 == r0) goto L_0x0840;
    L_0x01fa:
        r0 = 71;
        if (r4 == r0) goto L_0x082e;
    L_0x01fe:
        r0 = 73;
        if (r4 == r0) goto L_0x0819;
    L_0x0202:
        r0 = 74;
        if (r4 == r0) goto L_0x0803;
    L_0x0206:
        r0 = 97;
        if (r4 == r0) goto L_0x07f0;
    L_0x020a:
        r0 = 98;
        if (r4 == r0) goto L_0x07dd;
    L_0x020e:
        switch(r4) {
            case 9: goto L_0x07c8;
            case 10: goto L_0x07b4;
            case 11: goto L_0x07a1;
            case 12: goto L_0x078e;
            case 13: goto L_0x077a;
            case 14: goto L_0x0766;
            case 15: goto L_0x0752;
            case 16: goto L_0x073e;
            case 17: goto L_0x072d;
            case 18: goto L_0x0712;
            case 19: goto L_0x0701;
            case 20: goto L_0x06ec;
            case 21: goto L_0x06cb;
            case 22: goto L_0x06b7;
            case 23: goto L_0x06a3;
            case 24: goto L_0x068f;
            case 25: goto L_0x067b;
            case 26: goto L_0x0667;
            case 27: goto L_0x0653;
            case 28: goto L_0x063f;
            case 29: goto L_0x062b;
            case 30: goto L_0x0608;
            case 31: goto L_0x05f6;
            case 32: goto L_0x05e6;
            case 33: goto L_0x05d4;
            case 34: goto L_0x05a7;
            case 35: goto L_0x0591;
            default: goto L_0x0211;
        };
    L_0x0211:
        switch(r4) {
            case 40: goto L_0x057f;
            case 41: goto L_0x056c;
            case 42: goto L_0x055a;
            case 43: goto L_0x0547;
            case 44: goto L_0x0535;
            case 45: goto L_0x0523;
            case 46: goto L_0x0511;
            case 47: goto L_0x04f8;
            case 48: goto L_0x04e5;
            case 49: goto L_0x04d2;
            case 50: goto L_0x04bf;
            case 51: goto L_0x04ac;
            case 52: goto L_0x049a;
            case 53: goto L_0x0480;
            case 54: goto L_0x0468;
            case 55: goto L_0x044f;
            case 56: goto L_0x043b;
            case 57: goto L_0x0424;
            case 58: goto L_0x03f1;
            case 59: goto L_0x03cb;
            case 60: goto L_0x03ab;
            case 61: goto L_0x0385;
            case 62: goto L_0x0370;
            case 63: goto L_0x035b;
            case 64: goto L_0x0346;
            default: goto L_0x0214;
        };
    L_0x0214:
        switch(r4) {
            case 79: goto L_0x0335;
            case 80: goto L_0x0320;
            case 81: goto L_0x030b;
            case 82: goto L_0x02f6;
            case 83: goto L_0x02da;
            case 84: goto L_0x02c7;
            case 85: goto L_0x02b6;
            case 86: goto L_0x028b;
            case 87: goto L_0x027a;
            case 88: goto L_0x0269;
            case 89: goto L_0x0256;
            default: goto L_0x0217;
        };
    L_0x0217:
        switch(r4) {
            case 92: goto L_0x0246;
            case 93: goto L_0x0236;
            case 94: goto L_0x0226;
            default: goto L_0x021a;
        };
    L_0x021a:
        r38 = r6;
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x0226:
        r38 = r6;
        r6 = -1;
        r0 = r3.getDimensionPixelSize(r4, r6);
        r66 = r0;
        r0 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087f;
    L_0x0236:
        r38 = r6;
        r6 = -1;
        r0 = r3.getDimensionPixelSize(r4, r6);
        r65 = r0;
        r0 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087f;
    L_0x0246:
        r38 = r6;
        r6 = -1;
        r0 = r3.getDimensionPixelSize(r4, r6);
        r64 = r0;
        r0 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087f;
    L_0x0256:
        r38 = r6;
        r6 = 0;
        r0 = r3.getInt(r4, r6);
        r8.mJustificationMode = r0;
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x0269:
        r38 = r6;
        r6 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r0 = r3.getDimension(r4, r6);
        r51 = r0;
        r0 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087f;
    L_0x027a:
        r38 = r6;
        r6 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r0 = r3.getDimension(r4, r6);
        r50 = r0;
        r0 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087f;
    L_0x028b:
        r38 = r6;
        r6 = 0;
        r0 = r3.getResourceId(r4, r6);
        if (r0 <= 0) goto L_0x02ac;
    L_0x0294:
        r6 = r3.getResources();
        r6 = r6.obtainTypedArray(r0);
        r8.setupAutoSizeUniformPresetSizes(r6);
        r6.recycle();
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x02ac:
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x02b6:
        r38 = r6;
        r6 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r0 = r3.getDimension(r4, r6);
        r52 = r0;
        r0 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087f;
    L_0x02c7:
        r38 = r6;
        r6 = 0;
        r0 = r3.getInt(r4, r6);
        r8.mAutoSizeTextType = r0;
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x02da:
        r38 = r6;
        r68.createEditorIfNeeded();
        r0 = r8.mEditor;
        r39 = r11;
        r6 = 1;
        r11 = r3.getBoolean(r4, r6);
        r0.mAllowUndo = r11;
        r6 = r36;
        r11 = r39;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x02f6:
        r38 = r6;
        r39 = r11;
        r6 = 0;
        r0 = r3.getInt(r4, r6);
        r8.mHyphenationFrequency = r0;
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x030b:
        r38 = r6;
        r39 = r11;
        r6 = 0;
        r0 = r3.getInt(r4, r6);
        r8.mBreakStrategy = r0;
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x0320:
        r38 = r6;
        r39 = r11;
        r6 = -1;
        r0 = r3.getInt(r4, r6);
        r0 = android.graphics.drawable.Drawable.parseBlendMode(r0, r2);
        r2 = r0;
        r0 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087f;
    L_0x0335:
        r38 = r6;
        r39 = r11;
        r0 = r3.getColorStateList(r4);
        r63 = r0;
        r0 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087f;
    L_0x0346:
        r38 = r6;
        r39 = r11;
        r6 = 0;
        r0 = r3.getResourceId(r4, r6);
        r8.mTextSelectHandleRes = r0;
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x035b:
        r38 = r6;
        r39 = r11;
        r6 = 0;
        r0 = r3.getResourceId(r4, r6);
        r8.mTextSelectHandleRightRes = r0;
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x0370:
        r38 = r6;
        r39 = r11;
        r6 = 0;
        r0 = r3.getResourceId(r4, r6);
        r8.mTextSelectHandleLeftRes = r0;
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x0385:
        r38 = r6;
        r39 = r11;
        r68.createEditorIfNeeded();
        r0 = r8.mEditor;
        r0.createInputContentTypeIfNeeded();
        r0 = r8.mEditor;
        r0 = r0.mInputContentType;
        r6 = r8.mEditor;
        r6 = r6.mInputContentType;
        r6 = r6.imeActionId;
        r6 = r3.getInt(r4, r6);
        r0.imeActionId = r6;
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x03ab:
        r38 = r6;
        r39 = r11;
        r68.createEditorIfNeeded();
        r0 = r8.mEditor;
        r0.createInputContentTypeIfNeeded();
        r0 = r8.mEditor;
        r0 = r0.mInputContentType;
        r6 = r3.getText(r4);
        r0.imeActionLabel = r6;
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x03cb:
        r38 = r6;
        r39 = r11;
        r68.createEditorIfNeeded();
        r0 = r8.mEditor;
        r0.createInputContentTypeIfNeeded();
        r0 = r8.mEditor;
        r0 = r0.mInputContentType;
        r6 = r8.mEditor;
        r6 = r6.mInputContentType;
        r6 = r6.imeOptions;
        r6 = r3.getInt(r4, r6);
        r0.imeOptions = r6;
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x03f1:
        r38 = r6;
        r39 = r11;
        r6 = 0;
        r0 = r3.getResourceId(r4, r6);	 Catch:{ XmlPullParserException -> 0x0410, IOException -> 0x03fe }
        r8.setInputExtras(r0);	 Catch:{ XmlPullParserException -> 0x0410, IOException -> 0x03fe }
        goto L_0x0418;
    L_0x03fe:
        r0 = move-exception;
        r6 = r18;
        android.util.Log.w(r14, r6, r0);
        r6 = r36;
        r11 = r39;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x0410:
        r0 = move-exception;
        r6 = r18;
        android.util.Log.w(r14, r6, r0);
        r18 = r6;
    L_0x0418:
        r6 = r36;
        r11 = r39;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x0424:
        r38 = r6;
        r39 = r11;
        r6 = r18;
        r0 = r3.getString(r4);
        r8.setPrivateImeOptions(r0);
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x043b:
        r38 = r6;
        r39 = r11;
        r6 = r18;
        r11 = 0;
        r34 = r3.getInt(r4, r11);
        r0 = r12;
        r12 = r35;
        r11 = r39;
        r35 = r14;
        goto L_0x087f;
    L_0x044f:
        r38 = r6;
        r39 = r11;
        r6 = r18;
        r0 = r8.mMarqueeRepeatLimit;
        r0 = r3.getInt(r4, r0);
        r8.setMarqueeRepeatLimit(r0);
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x0468:
        r38 = r6;
        r39 = r11;
        r6 = r18;
        r0 = r8.mSpacingMult;
        r0 = r3.getFloat(r4, r0);
        r8.mSpacingMult = r0;
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x0480:
        r38 = r6;
        r39 = r11;
        r6 = r18;
        r0 = r8.mSpacingAdd;
        r0 = (int) r0;
        r0 = r3.getDimensionPixelSize(r4, r0);
        r0 = (float) r0;
        r8.mSpacingAdd = r0;
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x049a:
        r38 = r6;
        r39 = r11;
        r6 = r18;
        r0 = r3.getDimensionPixelSize(r4, r1);
        r1 = r0;
        r0 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087f;
    L_0x04ac:
        r38 = r6;
        r39 = r11;
        r6 = r18;
        r0 = r3.getDrawable(r4);
        r60 = r0;
        r0 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087f;
    L_0x04bf:
        r38 = r6;
        r39 = r11;
        r6 = r18;
        r0 = r3.getDrawable(r4);
        r59 = r0;
        r0 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087f;
    L_0x04d2:
        r38 = r6;
        r39 = r11;
        r6 = r18;
        r0 = r3.getDrawable(r4);
        r58 = r0;
        r0 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087f;
    L_0x04e5:
        r38 = r6;
        r39 = r11;
        r6 = r18;
        r0 = r3.getDrawable(r4);
        r57 = r0;
        r0 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087f;
    L_0x04f8:
        r38 = r6;
        r39 = r11;
        r6 = r18;
        r11 = 0;
        r0 = r3.getBoolean(r4, r11);
        r8.mFreezesText = r0;
        r6 = r36;
        r11 = r39;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x0511:
        r38 = r6;
        r39 = r11;
        r6 = r18;
        r0 = r3.getBoolean(r4, r5);
        r5 = r0;
        r0 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087f;
    L_0x0523:
        r38 = r6;
        r39 = r11;
        r6 = r18;
        r0 = r3.getBoolean(r4, r9);
        r9 = r0;
        r0 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087f;
    L_0x0535:
        r38 = r6;
        r39 = r11;
        r6 = r18;
        r0 = r3.getInt(r4, r13);
        r13 = r0;
        r0 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087f;
    L_0x0547:
        r38 = r6;
        r39 = r11;
        r6 = r18;
        r0 = r3.getText(r4);
        r32 = r0;
        r0 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087f;
    L_0x055a:
        r38 = r6;
        r39 = r11;
        r6 = r18;
        r0 = r3.getBoolean(r4, r7);
        r7 = r0;
        r0 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087f;
    L_0x056c:
        r38 = r6;
        r39 = r11;
        r6 = r18;
        r0 = r3.getText(r4);
        r30 = r0;
        r0 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087f;
    L_0x057f:
        r38 = r6;
        r39 = r11;
        r6 = r18;
        r0 = r3.getInt(r4, r15);
        r15 = r0;
        r0 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087f;
    L_0x0591:
        r38 = r6;
        r39 = r11;
        r6 = r18;
        r11 = -1;
        r0 = r3.getInt(r4, r11);
        r31 = r0;
        r0 = r12;
        r12 = r35;
        r11 = r39;
        r35 = r14;
        goto L_0x087f;
    L_0x05a7:
        r38 = r6;
        r39 = r11;
        r6 = r18;
        r11 = 1;
        r0 = r3.getBoolean(r4, r11);
        if (r0 != 0) goto L_0x05c6;
    L_0x05b4:
        r11 = 0;
        r8.setIncludeFontPadding(r11);
        r18 = r6;
        r6 = r36;
        r11 = r39;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x05c6:
        r18 = r6;
        r6 = r36;
        r11 = r39;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x05d4:
        r38 = r6;
        r39 = r11;
        r6 = r18;
        r0 = r3.getBoolean(r4, r10);
        r10 = r0;
        r0 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087f;
    L_0x05e6:
        r38 = r6;
        r39 = r11;
        r6 = r18;
        r0 = r3.getBoolean(r4, r12);
        r12 = r35;
        r35 = r14;
        goto L_0x087f;
    L_0x05f6:
        r38 = r6;
        r39 = r11;
        r6 = r18;
        r0 = r3.getBoolean(r4, r11);
        r11 = r0;
        r0 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087f;
    L_0x0608:
        r38 = r6;
        r6 = r18;
        r6 = 0;
        r0 = r3.getBoolean(r4, r6);
        if (r0 == 0) goto L_0x0621;
    L_0x0613:
        r6 = 1;
        r8.setHorizontallyScrolling(r6);
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x0621:
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x062b:
        r38 = r6;
        r6 = -1;
        r0 = r3.getInt(r4, r6);
        r8.setMinEms(r0);
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x063f:
        r38 = r6;
        r6 = -1;
        r0 = r3.getDimensionPixelSize(r4, r6);
        r8.setWidth(r0);
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x0653:
        r38 = r6;
        r6 = -1;
        r0 = r3.getInt(r4, r6);
        r8.setEms(r0);
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x0667:
        r38 = r6;
        r6 = -1;
        r0 = r3.getInt(r4, r6);
        r8.setMaxEms(r0);
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x067b:
        r38 = r6;
        r6 = -1;
        r0 = r3.getInt(r4, r6);
        r8.setMinLines(r0);
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x068f:
        r38 = r6;
        r6 = -1;
        r0 = r3.getDimensionPixelSize(r4, r6);
        r8.setHeight(r0);
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x06a3:
        r38 = r6;
        r6 = -1;
        r0 = r3.getInt(r4, r6);
        r8.setLines(r0);
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x06b7:
        r38 = r6;
        r6 = -1;
        r0 = r3.getInt(r4, r6);
        r8.setMaxLines(r0);
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x06cb:
        r38 = r6;
        r6 = 1;
        r0 = r3.getBoolean(r4, r6);
        if (r0 != 0) goto L_0x06e2;
    L_0x06d4:
        r6 = 0;
        r8.setCursorVisible(r6);
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x06e2:
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x06ec:
        r38 = r6;
        r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = r3.getFloat(r4, r6);
        r8.setTextScaleX(r0);
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x0701:
        r38 = r6;
        r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = r3.getText(r4);
        r26 = r0;
        r0 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087f;
    L_0x0712:
        r38 = r6;
        r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = 1;
        r28 = r0;
        r6 = 0;
        r0 = r3.getResourceId(r4, r6);
        r8.mTextId = r0;
        r0 = r3.getText(r4);
        r29 = r0;
        r0 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087f;
    L_0x072d:
        r38 = r6;
        r6 = r36;
        r0 = r3.getInt(r4, r6);
        r36 = r0;
        r0 = r12;
        r12 = r35;
        r35 = r14;
        goto L_0x087f;
    L_0x073e:
        r38 = r6;
        r6 = r36;
        r36 = r12;
        r12 = -1;
        r0 = r3.getDimensionPixelSize(r4, r12);
        r8.setMinHeight(r0);
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x0752:
        r38 = r6;
        r6 = r36;
        r36 = r12;
        r12 = -1;
        r0 = r3.getDimensionPixelSize(r4, r12);
        r8.setMinWidth(r0);
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x0766:
        r38 = r6;
        r6 = r36;
        r36 = r12;
        r12 = -1;
        r0 = r3.getDimensionPixelSize(r4, r12);
        r8.setMaxHeight(r0);
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x077a:
        r38 = r6;
        r6 = r36;
        r36 = r12;
        r12 = -1;
        r0 = r3.getDimensionPixelSize(r4, r12);
        r8.setMaxWidth(r0);
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x078e:
        r38 = r6;
        r6 = r36;
        r36 = r12;
        r12 = 1;
        r0 = r3.getBoolean(r4, r12);
        r8.mLinksClickable = r0;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x07a1:
        r38 = r6;
        r6 = r36;
        r36 = r12;
        r12 = 0;
        r0 = r3.getInt(r4, r12);
        r8.mAutoLinkMask = r0;
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x07b4:
        r38 = r6;
        r6 = r36;
        r36 = r12;
        r12 = -1;
        r0 = r3.getInt(r4, r12);
        r8.setGravity(r0);
        r12 = r35;
        r35 = r14;
        goto L_0x087b;
    L_0x07c8:
        r38 = r6;
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r0 = r3.getInt(r4, r12);
        r12 = r0;
        r35 = r14;
        r0 = r36;
        r36 = r6;
        goto L_0x087f;
    L_0x07dd:
        r38 = r6;
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        r14 = 0;
        r0 = r3.getResourceId(r4, r14);
        r8.mTextEditSuggestionHighlightStyle = r0;
        goto L_0x087b;
    L_0x07f0:
        r38 = r6;
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        r14 = 0;
        r0 = r3.getResourceId(r4, r14);
        r8.mTextEditSuggestionContainerLayout = r0;
        goto L_0x087b;
    L_0x0803:
        r38 = r6;
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        r0 = r3.getDrawable(r4);
        r62 = r0;
        r0 = r36;
        r36 = r6;
        goto L_0x087f;
    L_0x0819:
        r38 = r6;
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        r0 = r3.getDrawable(r4);
        r61 = r0;
        r0 = r36;
        r36 = r6;
        goto L_0x087f;
    L_0x082e:
        r38 = r6;
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        r14 = 0;
        r0 = r3.getResourceId(r4, r14);
        r8.mTextEditSuggestionItemLayout = r0;
        goto L_0x087b;
    L_0x0840:
        r38 = r6;
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        r14 = 0;
        r0 = r3.getResourceId(r4, r14);
        r8.mCursorDrawableRes = r0;
        goto L_0x087b;
    L_0x0852:
        r38 = r6;
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        r14 = 0;
        r0 = r3.getBoolean(r4, r14);
        r8.setTextIsSelectable(r0);
        goto L_0x087b;
    L_0x0865:
        r38 = r6;
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r35 = r14;
        r0 = r68.isEnabled();
        r0 = r3.getBoolean(r4, r0);
        r8.setEnabled(r0);
    L_0x087b:
        r0 = r36;
        r36 = r6;
    L_0x087f:
        r6 = r38 + 1;
        r14 = r35;
        r4 = r37;
        r35 = r12;
        r12 = r0;
        goto L_0x01e8;
    L_0x088a:
        r37 = r4;
        r38 = r6;
        r6 = r36;
        r36 = r12;
        r12 = r35;
        r3.recycle();
        r4 = android.widget.TextView.BufferType.EDITABLE;
        r54 = r3;
        r14 = r34;
        r3 = r14 & 4095;
        r18 = r4;
        r4 = 129; // 0x81 float:1.81E-43 double:6.37E-322;
        if (r3 != r4) goto L_0x08a7;
    L_0x08a5:
        r0 = 1;
        goto L_0x08a8;
    L_0x08a7:
        r0 = 0;
    L_0x08a8:
        r24 = r0;
        r0 = 225; // 0xe1 float:3.15E-43 double:1.11E-321;
        if (r3 != r0) goto L_0x08b0;
    L_0x08ae:
        r0 = 1;
        goto L_0x08b1;
    L_0x08b0:
        r0 = 0;
    L_0x08b1:
        r34 = r0;
        r4 = 18;
        if (r3 != r4) goto L_0x08b9;
    L_0x08b7:
        r0 = 1;
        goto L_0x08ba;
    L_0x08b9:
        r0 = 0;
    L_0x08ba:
        r38 = r0;
        r0 = r69.getApplicationInfo();
        r4 = r0.targetSdkVersion;
        r0 = 26;
        if (r4 < r0) goto L_0x08c8;
    L_0x08c6:
        r0 = 1;
        goto L_0x08c9;
    L_0x08c8:
        r0 = 0;
    L_0x08c9:
        r8.mUseInternationalizedInput = r0;
        r0 = 28;
        if (r4 < r0) goto L_0x08d1;
    L_0x08cf:
        r0 = 1;
        goto L_0x08d2;
    L_0x08d1:
        r0 = 0;
    L_0x08d2:
        r8.mUseFallbackLineSpacing = r0;
        if (r32 == 0) goto L_0x093c;
    L_0x08d6:
        r0 = r32.toString();	 Catch:{ ClassNotFoundException -> 0x0931 }
        r0 = java.lang.Class.forName(r0);	 Catch:{ ClassNotFoundException -> 0x0931 }
        r23 = r0;
        r68.createEditorIfNeeded();	 Catch:{ InstantiationException -> 0x0926, IllegalAccessException -> 0x091b }
        r0 = r8.mEditor;	 Catch:{ InstantiationException -> 0x0926, IllegalAccessException -> 0x091b }
        r40 = r23.newInstance();	 Catch:{ InstantiationException -> 0x0926, IllegalAccessException -> 0x091b }
        r41 = r3;
        r3 = r40;
        r3 = (android.text.method.KeyListener) r3;	 Catch:{ InstantiationException -> 0x0917, IllegalAccessException -> 0x0913 }
        r0.mKeyListener = r3;	 Catch:{ InstantiationException -> 0x0917, IllegalAccessException -> 0x0913 }
        r0 = r8.mEditor;	 Catch:{ IncompatibleClassChangeError -> 0x0906 }
        if (r14 == 0) goto L_0x08f9;
    L_0x08f7:
        r3 = r14;
        goto L_0x0901;
    L_0x08f9:
        r3 = r8.mEditor;	 Catch:{ IncompatibleClassChangeError -> 0x0906 }
        r3 = r3.mKeyListener;	 Catch:{ IncompatibleClassChangeError -> 0x0906 }
        r3 = r3.getInputType();	 Catch:{ IncompatibleClassChangeError -> 0x0906 }
    L_0x0901:
        r0.mInputType = r3;	 Catch:{ IncompatibleClassChangeError -> 0x0906 }
        r40 = r4;
        goto L_0x090e;
    L_0x0906:
        r0 = move-exception;
        r3 = r8.mEditor;
        r40 = r4;
        r4 = 1;
        r3.mInputType = r4;
    L_0x090e:
        r42 = r7;
        r3 = 2;
        goto L_0x0a08;
    L_0x0913:
        r0 = move-exception;
        r40 = r4;
        goto L_0x0920;
    L_0x0917:
        r0 = move-exception;
        r40 = r4;
        goto L_0x092b;
    L_0x091b:
        r0 = move-exception;
        r41 = r3;
        r40 = r4;
    L_0x0920:
        r3 = new java.lang.RuntimeException;
        r3.<init>(r0);
        throw r3;
    L_0x0926:
        r0 = move-exception;
        r41 = r3;
        r40 = r4;
    L_0x092b:
        r3 = new java.lang.RuntimeException;
        r3.<init>(r0);
        throw r3;
    L_0x0931:
        r0 = move-exception;
        r41 = r3;
        r40 = r4;
        r3 = new java.lang.RuntimeException;
        r3.<init>(r0);
        throw r3;
    L_0x093c:
        r41 = r3;
        r40 = r4;
        if (r30 == 0) goto L_0x095f;
    L_0x0942:
        r68.createEditorIfNeeded();
        r0 = r8.mEditor;
        r3 = r30.toString();
        r3 = android.text.method.DigitsKeyListener.getInstance(r3);
        r0.mKeyListener = r3;
        r0 = r8.mEditor;
        if (r14 == 0) goto L_0x0957;
    L_0x0955:
        r3 = r14;
        goto L_0x0958;
    L_0x0957:
        r3 = 1;
    L_0x0958:
        r0.mInputType = r3;
        r42 = r7;
        r3 = 2;
        goto L_0x0a08;
    L_0x095f:
        if (r14 == 0) goto L_0x0971;
    L_0x0961:
        r3 = 1;
        r8.setInputType(r14, r3);
        r0 = isMultilineInputType(r14);
        r0 = r0 ^ r3;
        r42 = r7;
        r4 = r18;
        r3 = 2;
        goto L_0x0a4e;
    L_0x0971:
        if (r7 == 0) goto L_0x098e;
    L_0x0973:
        r68.createEditorIfNeeded();
        r0 = r8.mEditor;
        r3 = android.text.method.DialerKeyListener.getInstance();
        r0.mKeyListener = r3;
        r0 = r8.mEditor;
        r3 = 3;
        r4 = r3;
        r0.mInputType = r3;
        r14 = r4;
        r42 = r7;
        r4 = r18;
        r0 = r36;
        r3 = 2;
        goto L_0x0a4e;
    L_0x098e:
        if (r15 == 0) goto L_0x09c0;
    L_0x0990:
        r68.createEditorIfNeeded();
        r0 = r8.mEditor;
        r3 = r15 & 2;
        if (r3 == 0) goto L_0x099b;
    L_0x0999:
        r3 = 1;
        goto L_0x099c;
    L_0x099b:
        r3 = 0;
    L_0x099c:
        r4 = r15 & 4;
        if (r4 == 0) goto L_0x09a2;
    L_0x09a0:
        r4 = 1;
        goto L_0x09a3;
    L_0x09a2:
        r4 = 0;
    L_0x09a3:
        r42 = r7;
        r7 = 0;
        r3 = android.text.method.DigitsKeyListener.getInstance(r7, r3, r4);
        r0.mKeyListener = r3;
        r0 = r8.mEditor;
        r0 = r0.mKeyListener;
        r0 = r0.getInputType();
        r3 = r8.mEditor;
        r3.mInputType = r0;
        r14 = r0;
        r4 = r18;
        r0 = r36;
        r3 = 2;
        goto L_0x0a4e;
    L_0x09c0:
        r42 = r7;
        r7 = 0;
        if (r9 != 0) goto L_0x0a1e;
    L_0x09c5:
        r3 = -1;
        if (r13 == r3) goto L_0x09ca;
    L_0x09c8:
        r3 = 2;
        goto L_0x0a1f;
    L_0x09ca:
        if (r5 == 0) goto L_0x09de;
    L_0x09cc:
        r68.createEditorIfNeeded();
        r0 = r8.mEditor;
        r3 = android.text.method.TextKeyListener.getInstance();
        r0.mKeyListener = r3;
        r0 = r8.mEditor;
        r3 = 1;
        r0.mInputType = r3;
        r3 = 2;
        goto L_0x0a08;
    L_0x09de:
        r0 = r68.isTextSelectable();
        if (r0 == 0) goto L_0x09fa;
    L_0x09e4:
        r0 = r8.mEditor;
        if (r0 == 0) goto L_0x09ed;
    L_0x09e8:
        r0.mKeyListener = r7;
        r3 = 0;
        r0.mInputType = r3;
    L_0x09ed:
        r4 = android.widget.TextView.BufferType.SPANNABLE;
        r0 = android.text.method.ArrowKeyMovementMethod.getInstance();
        r8.setMovementMethod(r0);
        r0 = r36;
        r3 = 2;
        goto L_0x0a4e;
    L_0x09fa:
        r0 = r8.mEditor;
        if (r0 == 0) goto L_0x0a00;
    L_0x09fe:
        r0.mKeyListener = r7;
    L_0x0a00:
        if (r6 == 0) goto L_0x0a18;
    L_0x0a02:
        r3 = 1;
        if (r6 == r3) goto L_0x0a12;
    L_0x0a05:
        r3 = 2;
        if (r6 == r3) goto L_0x0a0d;
    L_0x0a08:
        r4 = r18;
        r0 = r36;
        goto L_0x0a4e;
    L_0x0a0d:
        r4 = android.widget.TextView.BufferType.EDITABLE;
        r0 = r36;
        goto L_0x0a4e;
    L_0x0a12:
        r3 = 2;
        r4 = android.widget.TextView.BufferType.SPANNABLE;
        r0 = r36;
        goto L_0x0a4e;
    L_0x0a18:
        r3 = 2;
        r4 = android.widget.TextView.BufferType.NORMAL;
        r0 = r36;
        goto L_0x0a4e;
    L_0x0a1e:
        r3 = 2;
    L_0x0a1f:
        r0 = 1;
        r4 = 1;
        if (r13 == r4) goto L_0x0a35;
    L_0x0a23:
        if (r13 == r3) goto L_0x0a30;
    L_0x0a25:
        r4 = 3;
        if (r13 == r4) goto L_0x0a2b;
    L_0x0a28:
        r4 = android.text.method.TextKeyListener.Capitalize.NONE;
        goto L_0x0a3a;
    L_0x0a2b:
        r4 = android.text.method.TextKeyListener.Capitalize.CHARACTERS;
        r0 = r0 | 4096;
        goto L_0x0a3a;
    L_0x0a30:
        r4 = android.text.method.TextKeyListener.Capitalize.WORDS;
        r0 = r0 | 8192;
        goto L_0x0a3a;
    L_0x0a35:
        r4 = android.text.method.TextKeyListener.Capitalize.SENTENCES;
        r0 = r0 | 16384;
    L_0x0a3a:
        r68.createEditorIfNeeded();
        r7 = r8.mEditor;
        r14 = android.text.method.TextKeyListener.getInstance(r9, r4);
        r7.mKeyListener = r14;
        r7 = r8.mEditor;
        r7.mInputType = r0;
        r14 = r0;
        r4 = r18;
        r0 = r36;
    L_0x0a4e:
        r7 = r8.mEditor;
        if (r7 == 0) goto L_0x0a60;
    L_0x0a52:
        r18 = r5;
        r23 = r6;
        r3 = r24;
        r5 = r34;
        r6 = r38;
        r7.adjustInputType(r11, r3, r5, r6);
        goto L_0x0a6a;
    L_0x0a60:
        r18 = r5;
        r23 = r6;
        r3 = r24;
        r5 = r34;
        r6 = r38;
    L_0x0a6a:
        if (r10 == 0) goto L_0x0a7d;
    L_0x0a6c:
        r68.createEditorIfNeeded();
        r7 = r8.mEditor;
        r24 = r9;
        r9 = 1;
        r7.mSelectAllOnFocus = r9;
        r7 = android.widget.TextView.BufferType.NORMAL;
        if (r4 != r7) goto L_0x0a7f;
    L_0x0a7a:
        r4 = android.widget.TextView.BufferType.SPANNABLE;
        goto L_0x0a7f;
    L_0x0a7d:
        r24 = r9;
    L_0x0a7f:
        r7 = r63;
        if (r7 != 0) goto L_0x0a8d;
    L_0x0a83:
        if (r2 == 0) goto L_0x0a86;
    L_0x0a85:
        goto L_0x0a8d;
    L_0x0a86:
        r36 = r7;
        r34 = r10;
        r10 = r69;
        goto L_0x0ab8;
    L_0x0a8d:
        r9 = r8.mDrawables;
        if (r9 != 0) goto L_0x0a9d;
    L_0x0a91:
        r9 = new android.widget.TextView$Drawables;
        r34 = r10;
        r10 = r69;
        r9.<init>(r10);
        r8.mDrawables = r9;
        goto L_0x0aa1;
    L_0x0a9d:
        r34 = r10;
        r10 = r69;
    L_0x0aa1:
        if (r7 == 0) goto L_0x0aad;
    L_0x0aa3:
        r9 = r8.mDrawables;
        r9.mTintList = r7;
        r36 = r7;
        r7 = 1;
        r9.mHasTint = r7;
        goto L_0x0ab0;
    L_0x0aad:
        r36 = r7;
        r7 = 1;
    L_0x0ab0:
        if (r2 == 0) goto L_0x0ab8;
    L_0x0ab2:
        r9 = r8.mDrawables;
        r9.mBlendMode = r2;
        r9.mHasTintMode = r7;
    L_0x0ab8:
        r38 = r2;
        r43 = r13;
        r7 = r57;
        r9 = r58;
        r2 = r59;
        r13 = r60;
        r8.setCompoundDrawablesWithIntrinsicBounds(r2, r7, r13, r9);
        r44 = r2;
        r45 = r7;
        r2 = r61;
        r7 = r62;
        r8.setRelativeDrawablesIfNeeded(r2, r7);
        r8.setCompoundDrawablePadding(r1);
        r8.setInputTypeSingleLine(r0);
        r8.applySingleLine(r0, r0, r0);
        if (r0 == 0) goto L_0x0aeb;
    L_0x0add:
        r46 = r68.getKeyListener();
        if (r46 != 0) goto L_0x0aeb;
    L_0x0ae3:
        r46 = r1;
        r1 = -1;
        if (r12 != r1) goto L_0x0aed;
    L_0x0ae8:
        r1 = 3;
        r12 = r1;
        goto L_0x0aed;
    L_0x0aeb:
        r46 = r1;
    L_0x0aed:
        r1 = 1;
        if (r12 == r1) goto L_0x0b25;
    L_0x0af0:
        r1 = 2;
        if (r12 == r1) goto L_0x0b1f;
    L_0x0af3:
        r1 = 3;
        if (r12 == r1) goto L_0x0b19;
    L_0x0af6:
        r1 = 4;
        if (r12 == r1) goto L_0x0afa;
    L_0x0af9:
        goto L_0x0b2b;
    L_0x0afa:
        r1 = android.view.ViewConfiguration.get(r69);
        r1 = r1.isFadingMarqueeEnabled();
        if (r1 == 0) goto L_0x0b0c;
    L_0x0b04:
        r1 = 1;
        r8.setHorizontalFadingEdgeEnabled(r1);
        r1 = 0;
        r8.mMarqueeFadeMode = r1;
        goto L_0x0b13;
    L_0x0b0c:
        r1 = 0;
        r8.setHorizontalFadingEdgeEnabled(r1);
        r1 = 1;
        r8.mMarqueeFadeMode = r1;
    L_0x0b13:
        r1 = android.text.TextUtils.TruncateAt.MARQUEE;
        r8.setEllipsize(r1);
        goto L_0x0b2b;
    L_0x0b19:
        r1 = android.text.TextUtils.TruncateAt.END;
        r8.setEllipsize(r1);
        goto L_0x0b2b;
    L_0x0b1f:
        r1 = android.text.TextUtils.TruncateAt.MIDDLE;
        r8.setEllipsize(r1);
        goto L_0x0b2b;
    L_0x0b25:
        r1 = android.text.TextUtils.TruncateAt.START;
        r8.setEllipsize(r1);
    L_0x0b2b:
        if (r11 != 0) goto L_0x0b36;
    L_0x0b2d:
        if (r3 != 0) goto L_0x0b36;
    L_0x0b2f:
        if (r5 != 0) goto L_0x0b36;
    L_0x0b31:
        if (r6 == 0) goto L_0x0b34;
    L_0x0b33:
        goto L_0x0b36;
    L_0x0b34:
        r1 = 0;
        goto L_0x0b37;
    L_0x0b36:
        r1 = 1;
    L_0x0b37:
        if (r1 != 0) goto L_0x0b4e;
    L_0x0b39:
        r47 = r0;
        r0 = r8.mEditor;
        if (r0 == 0) goto L_0x0b4a;
    L_0x0b3f:
        r0 = r0.mInputType;
        r0 = r0 & 4095;
        r48 = r2;
        r2 = 129; // 0x81 float:1.81E-43 double:6.37E-322;
        if (r0 != r2) goto L_0x0b4c;
    L_0x0b49:
        goto L_0x0b52;
    L_0x0b4a:
        r48 = r2;
    L_0x0b4c:
        r0 = 0;
        goto L_0x0b53;
    L_0x0b4e:
        r47 = r0;
        r48 = r2;
    L_0x0b52:
        r0 = 1;
    L_0x0b53:
        if (r0 == 0) goto L_0x0b5d;
    L_0x0b55:
        r17 = r3;
        r2 = r33;
        r3 = 3;
        r2.mTypefaceIndex = r3;
        goto L_0x0b61;
    L_0x0b5d:
        r17 = r3;
        r2 = r33;
    L_0x0b61:
        r8.applyTextAppearance(r2);
        if (r1 == 0) goto L_0x0b6d;
    L_0x0b66:
        r3 = android.text.method.PasswordTransformationMethod.getInstance();
        r8.setTransformationMethod(r3);
    L_0x0b6d:
        if (r31 < 0) goto L_0x0b85;
    L_0x0b6f:
        r33 = r0;
        r3 = 1;
        r0 = new android.text.InputFilter[r3];
        r3 = new android.text.InputFilter$LengthFilter;
        r35 = r1;
        r1 = r31;
        r3.<init>(r1);
        r19 = 0;
        r0[r19] = r3;
        r8.setFilters(r0);
        goto L_0x0b90;
    L_0x0b85:
        r33 = r0;
        r35 = r1;
        r1 = r31;
        r0 = NO_FILTERS;
        r8.setFilters(r0);
    L_0x0b90:
        r3 = r29;
        r8.setText(r3, r4);
        r0 = r8.mText;
        if (r0 != 0) goto L_0x0ba0;
    L_0x0b99:
        r29 = r1;
        r1 = r22;
        r8.mText = r1;
        goto L_0x0ba4;
    L_0x0ba0:
        r29 = r1;
        r1 = r22;
    L_0x0ba4:
        r0 = r8.mTransformed;
        if (r0 != 0) goto L_0x0baa;
    L_0x0ba8:
        r8.mTransformed = r1;
    L_0x0baa:
        if (r28 == 0) goto L_0x0baf;
    L_0x0bac:
        r1 = 1;
        r8.mTextSetFromXmlOrResourceId = r1;
    L_0x0baf:
        if (r26 == 0) goto L_0x0bb7;
    L_0x0bb1:
        r1 = r26;
        r8.setHint(r1);
        goto L_0x0bb9;
    L_0x0bb7:
        r1 = r26;
    L_0x0bb9:
        r0 = com.android.internal.R.styleable.View;
        r26 = r1;
        r22 = r2;
        r49 = r11;
        r1 = r70;
        r11 = r71;
        r2 = r72;
        r0 = r10.obtainStyledAttributes(r1, r0, r11, r2);
        r1 = r8.mMovement;
        if (r1 != 0) goto L_0x0bd8;
    L_0x0bcf:
        r1 = r68.getKeyListener();
        if (r1 == 0) goto L_0x0bd6;
    L_0x0bd5:
        goto L_0x0bd8;
    L_0x0bd6:
        r1 = 0;
        goto L_0x0bd9;
    L_0x0bd8:
        r1 = 1;
    L_0x0bd9:
        if (r1 != 0) goto L_0x0be5;
    L_0x0bdb:
        r31 = r68.isClickable();
        if (r31 == 0) goto L_0x0be2;
    L_0x0be1:
        goto L_0x0be5;
    L_0x0be2:
        r31 = 0;
        goto L_0x0be7;
    L_0x0be5:
        r31 = 1;
    L_0x0be7:
        if (r1 != 0) goto L_0x0bf3;
    L_0x0be9:
        r53 = r68.isLongClickable();
        if (r53 == 0) goto L_0x0bf0;
    L_0x0bef:
        goto L_0x0bf3;
    L_0x0bf0:
        r53 = 0;
        goto L_0x0bf5;
    L_0x0bf3:
        r53 = 1;
    L_0x0bf5:
        r54 = r68.getFocusable();
        r55 = r1;
        r1 = r0.getIndexCount();
        r37 = 0;
        r2 = r37;
        r37 = r4;
        r4 = r31;
        r31 = r3;
        r3 = r54;
        r67 = r53;
        r53 = r5;
        r5 = r67;
    L_0x0c11:
        if (r2 >= r1) goto L_0x0c6e;
    L_0x0c13:
        r54 = r1;
        r1 = r0.getIndex(r2);
        r57 = r6;
        r6 = 19;
        if (r1 == r6) goto L_0x0c3e;
    L_0x0c1f:
        r6 = 30;
        if (r1 == r6) goto L_0x0c35;
    L_0x0c23:
        r6 = 31;
        if (r1 == r6) goto L_0x0c2c;
    L_0x0c27:
        r59 = r7;
        r7 = 18;
        goto L_0x0c65;
    L_0x0c2c:
        r5 = r0.getBoolean(r1, r5);
        r59 = r7;
        r7 = 18;
        goto L_0x0c65;
    L_0x0c35:
        r4 = r0.getBoolean(r1, r4);
        r59 = r7;
        r7 = 18;
        goto L_0x0c65;
    L_0x0c3e:
        r6 = new android.util.TypedValue;
        r6.<init>();
        r58 = r0.getValue(r1, r6);
        if (r58 == 0) goto L_0x0c5f;
    L_0x0c49:
        r58 = r1;
        r1 = r6.type;
        r59 = r7;
        r7 = 18;
        if (r1 != r7) goto L_0x0c5b;
    L_0x0c53:
        r1 = r6.data;
        if (r1 != 0) goto L_0x0c59;
    L_0x0c57:
        r1 = 0;
        goto L_0x0c5d;
    L_0x0c59:
        r1 = 1;
        goto L_0x0c5d;
    L_0x0c5b:
        r1 = r6.data;
    L_0x0c5d:
        r3 = r1;
        goto L_0x0c65;
    L_0x0c5f:
        r58 = r1;
        r59 = r7;
        r7 = 18;
    L_0x0c65:
        r2 = r2 + 1;
        r1 = r54;
        r6 = r57;
        r7 = r59;
        goto L_0x0c11;
    L_0x0c6e:
        r54 = r1;
        r57 = r6;
        r59 = r7;
        r0.recycle();
        r1 = r68.getFocusable();
        if (r3 == r1) goto L_0x0c80;
    L_0x0c7d:
        r8.setFocusable(r3);
    L_0x0c80:
        r8.setClickable(r4);
        r8.setLongClickable(r5);
        r1 = r8.mEditor;
        if (r1 == 0) goto L_0x0c8d;
    L_0x0c8a:
        r1.prepareCursorControllers();
    L_0x0c8d:
        r1 = r68.getImportantForAccessibility();
        if (r1 != 0) goto L_0x0c98;
    L_0x0c93:
        r1 = 1;
        r8.setImportantForAccessibility(r1);
        goto L_0x0c99;
    L_0x0c98:
        r1 = 1;
    L_0x0c99:
        r2 = r68.supportsAutoSizeText();
        if (r2 == 0) goto L_0x0ceb;
    L_0x0c9f:
        r2 = r8.mAutoSizeTextType;
        if (r2 != r1) goto L_0x0cee;
    L_0x0ca3:
        r1 = r8.mHasPresetAutoSizeValues;
        if (r1 != 0) goto L_0x0ce7;
    L_0x0ca7:
        r1 = r68.getResources();
        r1 = r1.getDisplayMetrics();
        r2 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r6 = (r50 > r2 ? 1 : (r50 == r2 ? 0 : -1));
        if (r6 != 0) goto L_0x0cbf;
    L_0x0cb5:
        r6 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r7 = 2;
        r50 = android.util.TypedValue.applyDimension(r7, r6, r1);
        r6 = r50;
        goto L_0x0cc2;
    L_0x0cbf:
        r7 = 2;
        r6 = r50;
    L_0x0cc2:
        r19 = (r51 > r2 ? 1 : (r51 == r2 ? 0 : -1));
        if (r19 != 0) goto L_0x0ccf;
    L_0x0cc6:
        r2 = 1121976320; // 0x42e00000 float:112.0 double:5.54329955E-315;
        r51 = android.util.TypedValue.applyDimension(r7, r2, r1);
        r2 = r51;
        goto L_0x0cd1;
    L_0x0ccf:
        r2 = r51;
    L_0x0cd1:
        r7 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r7 = (r52 > r7 ? 1 : (r52 == r7 ? 0 : -1));
        if (r7 != 0) goto L_0x0cdc;
    L_0x0cd7:
        r52 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r7 = r52;
        goto L_0x0cde;
    L_0x0cdc:
        r7 = r52;
    L_0x0cde:
        r8.validateAndSetAutoSizeTextTypeUniformConfiguration(r6, r2, r7);
        r51 = r2;
        r50 = r6;
        r52 = r7;
    L_0x0ce7:
        r68.setupAutoSizeText();
        goto L_0x0cee;
    L_0x0ceb:
        r1 = 0;
        r8.mAutoSizeTextType = r1;
    L_0x0cee:
        r1 = r64;
        if (r1 < 0) goto L_0x0cf5;
    L_0x0cf2:
        r8.setFirstBaselineToTopHeight(r1);
    L_0x0cf5:
        r2 = r65;
        if (r2 < 0) goto L_0x0cfc;
    L_0x0cf9:
        r8.setLastBaselineToBottomHeight(r2);
    L_0x0cfc:
        r6 = r66;
        if (r6 < 0) goto L_0x0d03;
    L_0x0d00:
        r8.setLineHeight(r6);
    L_0x0d03:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.TextView.<init>(android.content.Context, android.util.AttributeSet, int, int):void");
    }

    private void setTextInternal(CharSequence text) {
        this.mText = text;
        PrecomputedText precomputedText = null;
        this.mSpannable = text instanceof Spannable ? (Spannable) text : null;
        if (text instanceof PrecomputedText) {
            precomputedText = (PrecomputedText) text;
        }
        this.mPrecomputed = precomputedText;
    }

    public void setAutoSizeTextTypeWithDefaults(int autoSizeTextType) {
        if (!supportsAutoSizeText()) {
            return;
        }
        if (autoSizeTextType == 0) {
            clearAutoSizeConfiguration();
        } else if (autoSizeTextType == 1) {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            validateAndSetAutoSizeTextTypeUniformConfiguration(TypedValue.applyDimension(2, 12.0f, displayMetrics), TypedValue.applyDimension(2, 112.0f, displayMetrics), 1.0f);
            if (setupAutoSizeText()) {
                autoSizeText();
                invalidate();
            }
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown auto-size text type: ");
            stringBuilder.append(autoSizeTextType);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public void setAutoSizeTextTypeUniformWithConfiguration(int autoSizeMinTextSize, int autoSizeMaxTextSize, int autoSizeStepGranularity, int unit) {
        if (supportsAutoSizeText()) {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            validateAndSetAutoSizeTextTypeUniformConfiguration(TypedValue.applyDimension(unit, (float) autoSizeMinTextSize, displayMetrics), TypedValue.applyDimension(unit, (float) autoSizeMaxTextSize, displayMetrics), TypedValue.applyDimension(unit, (float) autoSizeStepGranularity, displayMetrics));
            if (setupAutoSizeText()) {
                autoSizeText();
                invalidate();
            }
        }
    }

    public void setAutoSizeTextTypeUniformWithPresetSizes(int[] presetSizes, int unit) {
        if (supportsAutoSizeText()) {
            int presetSizesLength = presetSizes.length;
            if (presetSizesLength > 0) {
                int[] presetSizesInPx = new int[presetSizesLength];
                if (unit == 0) {
                    presetSizesInPx = Arrays.copyOf(presetSizes, presetSizesLength);
                } else {
                    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                    for (int i = 0; i < presetSizesLength; i++) {
                        presetSizesInPx[i] = Math.round(TypedValue.applyDimension(unit, (float) presetSizes[i], displayMetrics));
                    }
                }
                this.mAutoSizeTextSizesInPx = cleanupAutoSizePresetSizes(presetSizesInPx);
                if (!setupAutoSizeUniformPresetSizesConfiguration()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("None of the preset sizes is valid: ");
                    stringBuilder.append(Arrays.toString(presetSizes));
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
            this.mHasPresetAutoSizeValues = false;
            if (setupAutoSizeText()) {
                autoSizeText();
                invalidate();
            }
        }
    }

    public int getAutoSizeTextType() {
        return this.mAutoSizeTextType;
    }

    public int getAutoSizeStepGranularity() {
        return Math.round(this.mAutoSizeStepGranularityInPx);
    }

    public int getAutoSizeMinTextSize() {
        return Math.round(this.mAutoSizeMinTextSizeInPx);
    }

    public int getAutoSizeMaxTextSize() {
        return Math.round(this.mAutoSizeMaxTextSizeInPx);
    }

    public int[] getAutoSizeTextAvailableSizes() {
        return this.mAutoSizeTextSizesInPx;
    }

    private void setupAutoSizeUniformPresetSizes(TypedArray textSizes) {
        int textSizesLength = textSizes.length();
        int[] parsedSizes = new int[textSizesLength];
        if (textSizesLength > 0) {
            for (int i = 0; i < textSizesLength; i++) {
                parsedSizes[i] = textSizes.getDimensionPixelSize(i, -1);
            }
            this.mAutoSizeTextSizesInPx = cleanupAutoSizePresetSizes(parsedSizes);
            setupAutoSizeUniformPresetSizesConfiguration();
        }
    }

    private boolean setupAutoSizeUniformPresetSizesConfiguration() {
        int sizesLength = this.mAutoSizeTextSizesInPx.length;
        this.mHasPresetAutoSizeValues = sizesLength > 0;
        if (this.mHasPresetAutoSizeValues) {
            this.mAutoSizeTextType = 1;
            int[] iArr = this.mAutoSizeTextSizesInPx;
            this.mAutoSizeMinTextSizeInPx = (float) iArr[0];
            this.mAutoSizeMaxTextSizeInPx = (float) iArr[sizesLength - 1];
            this.mAutoSizeStepGranularityInPx = -1.0f;
        }
        return this.mHasPresetAutoSizeValues;
    }

    private void validateAndSetAutoSizeTextTypeUniformConfiguration(float autoSizeMinTextSizeInPx, float autoSizeMaxTextSizeInPx, float autoSizeStepGranularityInPx) {
        String str = "px) is less or equal to (0px)";
        StringBuilder stringBuilder;
        if (autoSizeMinTextSizeInPx <= 0.0f) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Minimum auto-size text size (");
            stringBuilder.append(autoSizeMinTextSizeInPx);
            stringBuilder.append(str);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (autoSizeMaxTextSizeInPx <= autoSizeMinTextSizeInPx) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Maximum auto-size text size (");
            stringBuilder.append(autoSizeMaxTextSizeInPx);
            stringBuilder.append("px) is less or equal to minimum auto-size text size (");
            stringBuilder.append(autoSizeMinTextSizeInPx);
            stringBuilder.append("px)");
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (autoSizeStepGranularityInPx > 0.0f) {
            this.mAutoSizeTextType = 1;
            this.mAutoSizeMinTextSizeInPx = autoSizeMinTextSizeInPx;
            this.mAutoSizeMaxTextSizeInPx = autoSizeMaxTextSizeInPx;
            this.mAutoSizeStepGranularityInPx = autoSizeStepGranularityInPx;
            this.mHasPresetAutoSizeValues = false;
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("The auto-size step granularity (");
            stringBuilder.append(autoSizeStepGranularityInPx);
            stringBuilder.append(str);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    private void clearAutoSizeConfiguration() {
        this.mAutoSizeTextType = 0;
        this.mAutoSizeMinTextSizeInPx = -1.0f;
        this.mAutoSizeMaxTextSizeInPx = -1.0f;
        this.mAutoSizeStepGranularityInPx = -1.0f;
        this.mAutoSizeTextSizesInPx = EmptyArray.INT;
        this.mNeedsAutoSizeText = false;
    }

    private int[] cleanupAutoSizePresetSizes(int[] presetValues) {
        if (presetValuesLength == 0) {
            return presetValues;
        }
        int[] iArr;
        Arrays.sort(presetValues);
        IntArray uniqueValidSizes = new IntArray();
        for (int currentPresetValue : presetValues) {
            if (currentPresetValue > 0 && uniqueValidSizes.binarySearch(currentPresetValue) < 0) {
                uniqueValidSizes.add(currentPresetValue);
            }
        }
        if (presetValuesLength == uniqueValidSizes.size()) {
            iArr = presetValues;
        } else {
            iArr = uniqueValidSizes.toArray();
        }
        return iArr;
    }

    private boolean setupAutoSizeText() {
        if (supportsAutoSizeText() && this.mAutoSizeTextType == 1) {
            if (!this.mHasPresetAutoSizeValues || this.mAutoSizeTextSizesInPx.length == 0) {
                int autoSizeValuesLength = ((int) Math.floor((double) ((this.mAutoSizeMaxTextSizeInPx - this.mAutoSizeMinTextSizeInPx) / this.mAutoSizeStepGranularityInPx))) + 1;
                int[] autoSizeTextSizesInPx = new int[autoSizeValuesLength];
                for (int i = 0; i < autoSizeValuesLength; i++) {
                    autoSizeTextSizesInPx[i] = Math.round(this.mAutoSizeMinTextSizeInPx + (((float) i) * this.mAutoSizeStepGranularityInPx));
                }
                this.mAutoSizeTextSizesInPx = cleanupAutoSizePresetSizes(autoSizeTextSizesInPx);
            }
            this.mNeedsAutoSizeText = true;
        } else {
            this.mNeedsAutoSizeText = false;
        }
        return this.mNeedsAutoSizeText;
    }

    private int[] parseDimensionArray(TypedArray dimens) {
        if (dimens == null) {
            return null;
        }
        int[] result = new int[dimens.length()];
        for (int i = 0; i < result.length; i++) {
            result[i] = dimens.getDimensionPixelSize(i, 0);
        }
        return result;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 100) {
            return;
        }
        if (resultCode != -1 || data == null) {
            Spannable spannable = this.mSpannable;
            if (spannable != null) {
                Selection.setSelection(spannable, getSelectionEnd());
                return;
            }
            return;
        }
        CharSequence result = data.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
        if (result == null) {
            return;
        }
        if (isTextEditable()) {
            replaceSelectionWithText(result);
            Editor editor = this.mEditor;
            if (editor != null) {
                editor.refreshTextActionMode();
            }
        } else if (result.length() > 0) {
            Toast.makeText(getContext(), String.valueOf(result), 1).show();
        }
    }

    private void setTypefaceFromAttrs(Typeface typeface, String familyName, int typefaceIndex, int style, int weight) {
        if (typeface == null && familyName != null) {
            resolveStyleAndSetTypeface(Typeface.create(familyName, (int) null), style, weight);
        } else if (typeface != null) {
            resolveStyleAndSetTypeface(typeface, style, weight);
        } else if (typefaceIndex == 1) {
            resolveStyleAndSetTypeface(Typeface.SANS_SERIF, style, weight);
        } else if (typefaceIndex == 2) {
            resolveStyleAndSetTypeface(Typeface.SERIF, style, weight);
        } else if (typefaceIndex != 3) {
            resolveStyleAndSetTypeface(null, style, weight);
        } else {
            resolveStyleAndSetTypeface(Typeface.MONOSPACE, style, weight);
        }
    }

    private void resolveStyleAndSetTypeface(Typeface typeface, int style, int weight) {
        if (weight >= 0) {
            setTypeface(Typeface.create(typeface, Math.min(1000, weight), (style & 2) != 0));
        } else {
            setTypeface(typeface, style);
        }
    }

    private void setRelativeDrawablesIfNeeded(Drawable start, Drawable end) {
        boolean hasRelativeDrawables = (start == null && end == null) ? false : true;
        if (hasRelativeDrawables) {
            Drawables dr = this.mDrawables;
            if (dr == null) {
                Drawables drawables = new Drawables(getContext());
                dr = drawables;
                this.mDrawables = drawables;
            }
            this.mDrawables.mOverride = true;
            Rect compoundRect = dr.mCompoundRect;
            int[] state = getDrawableState();
            if (start != null) {
                start.setBounds(0, 0, start.getIntrinsicWidth(), start.getIntrinsicHeight());
                start.setState(state);
                start.copyBounds(compoundRect);
                start.setCallback(this);
                dr.mDrawableStart = start;
                dr.mDrawableSizeStart = compoundRect.width();
                dr.mDrawableHeightStart = compoundRect.height();
            } else {
                dr.mDrawableHeightStart = 0;
                dr.mDrawableSizeStart = 0;
            }
            if (end != null) {
                end.setBounds(0, 0, end.getIntrinsicWidth(), end.getIntrinsicHeight());
                end.setState(state);
                end.copyBounds(compoundRect);
                end.setCallback(this);
                dr.mDrawableEnd = end;
                dr.mDrawableSizeEnd = compoundRect.width();
                dr.mDrawableHeightEnd = compoundRect.height();
            } else {
                dr.mDrawableHeightEnd = 0;
                dr.mDrawableSizeEnd = 0;
            }
            resetResolvedDrawables();
            resolveDrawables();
            applyCompoundDrawableTint();
        }
    }

    @RemotableViewMethod
    public void setEnabled(boolean enabled) {
        if (enabled != isEnabled()) {
            InputMethodManager imm;
            if (!enabled) {
                imm = getInputMethodManager();
                if (imm != null && imm.isActive(this)) {
                    imm.hideSoftInputFromWindow(getWindowToken(), 0);
                }
            }
            super.setEnabled(enabled);
            if (enabled) {
                imm = getInputMethodManager();
                if (imm != null) {
                    imm.restartInput(this);
                }
            }
            Editor editor = this.mEditor;
            if (editor != null) {
                editor.invalidateTextDisplayList();
                this.mEditor.prepareCursorControllers();
                this.mEditor.makeBlink();
            }
        }
    }

    public void setTypeface(Typeface tf, int style) {
        float f = 0.0f;
        boolean useFakeBold = false;
        if (style > 0) {
            if (tf == null) {
                tf = Typeface.defaultFromStyle(style);
            } else {
                tf = Typeface.create(tf, style);
            }
            setTypeface(tf);
            tf = getTypeface();
            int need = (~(tf != null ? tf.getStyle() : 0)) & style;
            if (!(TypefaceUtils.isUsingMiFont(tf) || (need & 1) == 0)) {
                useFakeBold = true;
            }
            this.mTextPaint.setFakeBoldText(useFakeBold);
            TextPaint textPaint = this.mTextPaint;
            if ((need & 2) != 0) {
                f = -0.25f;
            }
            textPaint.setTextSkewX(f);
            return;
        }
        this.mTextPaint.setFakeBoldText(false);
        this.mTextPaint.setTextSkewX(0.0f);
        setTypeface(tf);
    }

    /* Access modifiers changed, original: protected */
    public boolean getDefaultEditable() {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public MovementMethod getDefaultMovementMethod() {
        return null;
    }

    @CapturedViewProperty
    public CharSequence getText() {
        return this.mText;
    }

    public int length() {
        return this.mText.length();
    }

    public Editable getEditableText() {
        CharSequence charSequence = this.mText;
        return charSequence instanceof Editable ? (Editable) charSequence : null;
    }

    @VisibleForTesting
    public CharSequence getTransformed() {
        return this.mTransformed;
    }

    public int getLineHeight() {
        return FastMath.round((((float) this.mTextPaint.getFontMetricsInt(null)) * this.mSpacingMult) + this.mSpacingAdd);
    }

    public final Layout getLayout() {
        return this.mLayout;
    }

    /* Access modifiers changed, original: final */
    @UnsupportedAppUsage
    public final Layout getHintLayout() {
        return this.mHintLayout;
    }

    public final UndoManager getUndoManager() {
        throw new UnsupportedOperationException("not implemented");
    }

    @VisibleForTesting
    public final Editor getEditorForTesting() {
        return this.mEditor;
    }

    public final void setUndoManager(UndoManager undoManager, String tag) {
        throw new UnsupportedOperationException("not implemented");
    }

    public final KeyListener getKeyListener() {
        Editor editor = this.mEditor;
        return editor == null ? null : editor.mKeyListener;
    }

    public void setKeyListener(KeyListener input) {
        this.mListenerChanged = true;
        setKeyListenerOnly(input);
        fixFocusableAndClickableSettings();
        if (input != null) {
            createEditorIfNeeded();
            setInputTypeFromEditor();
        } else {
            Editor editor = this.mEditor;
            if (editor != null) {
                editor.mInputType = 0;
            }
        }
        InputMethodManager imm = getInputMethodManager();
        if (imm != null) {
            imm.restartInput(this);
        }
    }

    private void setInputTypeFromEditor() {
        try {
            this.mEditor.mInputType = this.mEditor.mKeyListener.getInputType();
        } catch (IncompatibleClassChangeError e) {
            this.mEditor.mInputType = 1;
        }
        setInputTypeSingleLine(this.mSingleLine);
    }

    private void setKeyListenerOnly(KeyListener input) {
        if (this.mEditor != null || input != null) {
            createEditorIfNeeded();
            if (this.mEditor.mKeyListener != input) {
                this.mEditor.mKeyListener = input;
                if (input != null) {
                    CharSequence charSequence = this.mText;
                    if (!(charSequence instanceof Editable)) {
                        setText(charSequence);
                    }
                }
                setFilters((Editable) this.mText, this.mFilters);
            }
        }
    }

    public final MovementMethod getMovementMethod() {
        return this.mMovement;
    }

    public final void setMovementMethod(MovementMethod movement) {
        if (this.mMovement != movement) {
            this.mMovement = movement;
            if (movement != null && this.mSpannable == null) {
                setText(this.mText);
            }
            fixFocusableAndClickableSettings();
            Editor editor = this.mEditor;
            if (editor != null) {
                editor.prepareCursorControllers();
            }
        }
    }

    private void fixFocusableAndClickableSettings() {
        if (this.mMovement == null) {
            Editor editor = this.mEditor;
            if (editor == null || editor.mKeyListener == null) {
                setFocusable(16);
                setClickable(false);
                setLongClickable(false);
                return;
            }
        }
        setFocusable(1);
        setClickable(true);
        setLongClickable(true);
    }

    public final TransformationMethod getTransformationMethod() {
        return this.mTransformation;
    }

    public final void setTransformationMethod(TransformationMethod method) {
        TransformationMethod transformationMethod = this.mTransformation;
        if (method != transformationMethod) {
            if (transformationMethod != null) {
                Spannable spannable = this.mSpannable;
                if (spannable != null) {
                    spannable.removeSpan(transformationMethod);
                }
            }
            this.mTransformation = method;
            if (method instanceof TransformationMethod2) {
                TransformationMethod2 method2 = (TransformationMethod2) method;
                boolean z = (isTextSelectable() || (this.mText instanceof Editable)) ? false : true;
                this.mAllowTransformationLengthChange = z;
                method2.setLengthChangesAllowed(this.mAllowTransformationLengthChange);
            } else {
                this.mAllowTransformationLengthChange = false;
            }
            setText(this.mText);
            if (hasPasswordTransformationMethod()) {
                notifyViewAccessibilityStateChangedIfNeeded(0);
            }
            this.mTextDir = getTextDirectionHeuristic();
        }
    }

    public int getCompoundPaddingTop() {
        Drawables dr = this.mDrawables;
        if (dr == null || dr.mShowing[1] == null) {
            return this.mPaddingTop;
        }
        return (this.mPaddingTop + dr.mDrawablePadding) + dr.mDrawableSizeTop;
    }

    public int getCompoundPaddingBottom() {
        Drawables dr = this.mDrawables;
        if (dr == null || dr.mShowing[3] == null) {
            return this.mPaddingBottom;
        }
        return (this.mPaddingBottom + dr.mDrawablePadding) + dr.mDrawableSizeBottom;
    }

    public int getCompoundPaddingLeft() {
        Drawables dr = this.mDrawables;
        if (dr == null || dr.mShowing[0] == null) {
            return this.mPaddingLeft;
        }
        return (this.mPaddingLeft + dr.mDrawablePadding) + dr.mDrawableSizeLeft;
    }

    public int getCompoundPaddingRight() {
        Drawables dr = this.mDrawables;
        if (dr == null || dr.mShowing[2] == null) {
            return this.mPaddingRight;
        }
        return (this.mPaddingRight + dr.mDrawablePadding) + dr.mDrawableSizeRight;
    }

    public int getCompoundPaddingStart() {
        resolveDrawables();
        if (getLayoutDirection() != 1) {
            return getCompoundPaddingLeft();
        }
        return getCompoundPaddingRight();
    }

    public int getCompoundPaddingEnd() {
        resolveDrawables();
        if (getLayoutDirection() != 1) {
            return getCompoundPaddingRight();
        }
        return getCompoundPaddingLeft();
    }

    public int getExtendedPaddingTop() {
        if (this.mMaxMode != 1) {
            return getCompoundPaddingTop();
        }
        if (this.mLayout == null) {
            assumeLayout();
        }
        if (this.mLayout.getLineCount() <= this.mMaximum) {
            return getCompoundPaddingTop();
        }
        int top = getCompoundPaddingTop();
        int viewht = (getHeight() - top) - getCompoundPaddingBottom();
        int layoutht = this.mLayout.getLineTop(this.mMaximum);
        if (layoutht >= viewht) {
            return top;
        }
        int gravity = this.mGravity & 112;
        if (gravity == 48) {
            return top;
        }
        if (gravity == 80) {
            return (top + viewht) - layoutht;
        }
        return ((viewht - layoutht) / 2) + top;
    }

    public int getExtendedPaddingBottom() {
        if (this.mMaxMode != 1) {
            return getCompoundPaddingBottom();
        }
        if (this.mLayout == null) {
            assumeLayout();
        }
        if (this.mLayout.getLineCount() <= this.mMaximum) {
            return getCompoundPaddingBottom();
        }
        int top = getCompoundPaddingTop();
        int bottom = getCompoundPaddingBottom();
        int viewht = (getHeight() - top) - bottom;
        int layoutht = this.mLayout.getLineTop(this.mMaximum);
        if (layoutht >= viewht) {
            return bottom;
        }
        int gravity = this.mGravity & 112;
        if (gravity == 48) {
            return (bottom + viewht) - layoutht;
        }
        if (gravity == 80) {
            return bottom;
        }
        return ((viewht - layoutht) / 2) + bottom;
    }

    public int getTotalPaddingLeft() {
        return getCompoundPaddingLeft();
    }

    public int getTotalPaddingRight() {
        return getCompoundPaddingRight();
    }

    public int getTotalPaddingStart() {
        return getCompoundPaddingStart();
    }

    public int getTotalPaddingEnd() {
        return getCompoundPaddingEnd();
    }

    public int getTotalPaddingTop() {
        return getExtendedPaddingTop() + getVerticalOffset(true);
    }

    public int getTotalPaddingBottom() {
        return getExtendedPaddingBottom() + getBottomVerticalOffset(true);
    }

    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        Drawables dr = this.mDrawables;
        if (dr != null) {
            if (dr.mDrawableStart != null) {
                dr.mDrawableStart.setCallback(null);
            }
            dr.mDrawableStart = null;
            if (dr.mDrawableEnd != null) {
                dr.mDrawableEnd.setCallback(null);
            }
            dr.mDrawableEnd = null;
            dr.mDrawableHeightStart = 0;
            dr.mDrawableSizeStart = 0;
            dr.mDrawableHeightEnd = 0;
            dr.mDrawableSizeEnd = 0;
        }
        boolean drawables = (left == null && top == null && right == null && bottom == null) ? false : true;
        if (drawables) {
            if (dr == null) {
                Drawables drawables2 = new Drawables(getContext());
                dr = drawables2;
                this.mDrawables = drawables2;
            }
            this.mDrawables.mOverride = false;
            if (!(dr.mShowing[0] == left || dr.mShowing[0] == null)) {
                dr.mShowing[0].setCallback(null);
            }
            dr.mShowing[0] = left;
            if (!(dr.mShowing[1] == top || dr.mShowing[1] == null)) {
                dr.mShowing[1].setCallback(null);
            }
            dr.mShowing[1] = top;
            if (!(dr.mShowing[2] == right || dr.mShowing[2] == null)) {
                dr.mShowing[2].setCallback(null);
            }
            dr.mShowing[2] = right;
            if (!(dr.mShowing[3] == bottom || dr.mShowing[3] == null)) {
                dr.mShowing[3].setCallback(null);
            }
            dr.mShowing[3] = bottom;
            Rect compoundRect = dr.mCompoundRect;
            int[] state = getDrawableState();
            if (left != null) {
                left.setState(state);
                left.copyBounds(compoundRect);
                left.setCallback(this);
                dr.mDrawableSizeLeft = compoundRect.width();
                dr.mDrawableHeightLeft = compoundRect.height();
            } else {
                dr.mDrawableHeightLeft = 0;
                dr.mDrawableSizeLeft = 0;
            }
            if (right != null) {
                right.setState(state);
                right.copyBounds(compoundRect);
                right.setCallback(this);
                dr.mDrawableSizeRight = compoundRect.width();
                dr.mDrawableHeightRight = compoundRect.height();
            } else {
                dr.mDrawableHeightRight = 0;
                dr.mDrawableSizeRight = 0;
            }
            if (top != null) {
                top.setState(state);
                top.copyBounds(compoundRect);
                top.setCallback(this);
                dr.mDrawableSizeTop = compoundRect.height();
                dr.mDrawableWidthTop = compoundRect.width();
            } else {
                dr.mDrawableWidthTop = 0;
                dr.mDrawableSizeTop = 0;
            }
            if (bottom != null) {
                bottom.setState(state);
                bottom.copyBounds(compoundRect);
                bottom.setCallback(this);
                dr.mDrawableSizeBottom = compoundRect.height();
                dr.mDrawableWidthBottom = compoundRect.width();
            } else {
                dr.mDrawableWidthBottom = 0;
                dr.mDrawableSizeBottom = 0;
            }
        } else if (dr != null) {
            if (dr.hasMetadata()) {
                for (int i = dr.mShowing.length - 1; i >= 0; i--) {
                    if (dr.mShowing[i] != null) {
                        dr.mShowing[i].setCallback(null);
                    }
                    dr.mShowing[i] = null;
                }
                dr.mDrawableHeightLeft = 0;
                dr.mDrawableSizeLeft = 0;
                dr.mDrawableHeightRight = 0;
                dr.mDrawableSizeRight = 0;
                dr.mDrawableWidthTop = 0;
                dr.mDrawableSizeTop = 0;
                dr.mDrawableWidthBottom = 0;
                dr.mDrawableSizeBottom = 0;
            } else {
                this.mDrawables = null;
            }
        }
        if (dr != null) {
            dr.mDrawableLeftInitial = left;
            dr.mDrawableRightInitial = right;
        }
        resetResolvedDrawables();
        resolveDrawables();
        applyCompoundDrawableTint();
        invalidate();
        requestLayout();
    }

    @RemotableViewMethod
    public void setCompoundDrawablesWithIntrinsicBounds(int left, int top, int right, int bottom) {
        Context context = getContext();
        Drawable drawable = null;
        Drawable drawable2 = left != 0 ? context.getDrawable(left) : null;
        Drawable drawable3 = top != 0 ? context.getDrawable(top) : null;
        Drawable drawable4 = right != 0 ? context.getDrawable(right) : null;
        if (bottom != 0) {
            drawable = context.getDrawable(bottom);
        }
        setCompoundDrawablesWithIntrinsicBounds(drawable2, drawable3, drawable4, drawable);
    }

    @RemotableViewMethod
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        if (left != null) {
            left.setBounds(0, 0, left.getIntrinsicWidth(), left.getIntrinsicHeight());
        }
        if (right != null) {
            right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
        }
        if (top != null) {
            top.setBounds(0, 0, top.getIntrinsicWidth(), top.getIntrinsicHeight());
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, bottom.getIntrinsicWidth(), bottom.getIntrinsicHeight());
        }
        setCompoundDrawables(left, top, right, bottom);
    }

    @RemotableViewMethod
    public void setCompoundDrawablesRelative(Drawable start, Drawable top, Drawable end, Drawable bottom) {
        Drawables dr = this.mDrawables;
        if (dr != null) {
            if (dr.mShowing[0] != null) {
                dr.mShowing[0].setCallback(null);
            }
            Drawable[] drawableArr = dr.mShowing;
            dr.mDrawableLeftInitial = null;
            drawableArr[0] = null;
            if (dr.mShowing[2] != null) {
                dr.mShowing[2].setCallback(null);
            }
            drawableArr = dr.mShowing;
            dr.mDrawableRightInitial = null;
            drawableArr[2] = null;
            dr.mDrawableHeightLeft = 0;
            dr.mDrawableSizeLeft = 0;
            dr.mDrawableHeightRight = 0;
            dr.mDrawableSizeRight = 0;
        }
        boolean drawables = (start == null && top == null && end == null && bottom == null) ? false : true;
        if (drawables) {
            if (dr == null) {
                Drawables drawables2 = new Drawables(getContext());
                dr = drawables2;
                this.mDrawables = drawables2;
            }
            this.mDrawables.mOverride = true;
            if (!(dr.mDrawableStart == start || dr.mDrawableStart == null)) {
                dr.mDrawableStart.setCallback(null);
            }
            dr.mDrawableStart = start;
            if (!(dr.mShowing[1] == top || dr.mShowing[1] == null)) {
                dr.mShowing[1].setCallback(null);
            }
            dr.mShowing[1] = top;
            if (!(dr.mDrawableEnd == end || dr.mDrawableEnd == null)) {
                dr.mDrawableEnd.setCallback(null);
            }
            dr.mDrawableEnd = end;
            if (!(dr.mShowing[3] == bottom || dr.mShowing[3] == null)) {
                dr.mShowing[3].setCallback(null);
            }
            dr.mShowing[3] = bottom;
            Rect compoundRect = dr.mCompoundRect;
            int[] state = getDrawableState();
            if (start != null) {
                start.setState(state);
                start.copyBounds(compoundRect);
                start.setCallback(this);
                dr.mDrawableSizeStart = compoundRect.width();
                dr.mDrawableHeightStart = compoundRect.height();
            } else {
                dr.mDrawableHeightStart = 0;
                dr.mDrawableSizeStart = 0;
            }
            if (end != null) {
                end.setState(state);
                end.copyBounds(compoundRect);
                end.setCallback(this);
                dr.mDrawableSizeEnd = compoundRect.width();
                dr.mDrawableHeightEnd = compoundRect.height();
            } else {
                dr.mDrawableHeightEnd = 0;
                dr.mDrawableSizeEnd = 0;
            }
            if (top != null) {
                top.setState(state);
                top.copyBounds(compoundRect);
                top.setCallback(this);
                dr.mDrawableSizeTop = compoundRect.height();
                dr.mDrawableWidthTop = compoundRect.width();
            } else {
                dr.mDrawableWidthTop = 0;
                dr.mDrawableSizeTop = 0;
            }
            if (bottom != null) {
                bottom.setState(state);
                bottom.copyBounds(compoundRect);
                bottom.setCallback(this);
                dr.mDrawableSizeBottom = compoundRect.height();
                dr.mDrawableWidthBottom = compoundRect.width();
            } else {
                dr.mDrawableWidthBottom = 0;
                dr.mDrawableSizeBottom = 0;
            }
        } else if (dr != null) {
            if (dr.hasMetadata()) {
                if (dr.mDrawableStart != null) {
                    dr.mDrawableStart.setCallback(null);
                }
                dr.mDrawableStart = null;
                if (dr.mShowing[1] != null) {
                    dr.mShowing[1].setCallback(null);
                }
                dr.mShowing[1] = null;
                if (dr.mDrawableEnd != null) {
                    dr.mDrawableEnd.setCallback(null);
                }
                dr.mDrawableEnd = null;
                if (dr.mShowing[3] != null) {
                    dr.mShowing[3].setCallback(null);
                }
                dr.mShowing[3] = null;
                dr.mDrawableHeightStart = 0;
                dr.mDrawableSizeStart = 0;
                dr.mDrawableHeightEnd = 0;
                dr.mDrawableSizeEnd = 0;
                dr.mDrawableWidthTop = 0;
                dr.mDrawableSizeTop = 0;
                dr.mDrawableWidthBottom = 0;
                dr.mDrawableSizeBottom = 0;
            } else {
                this.mDrawables = null;
            }
        }
        resetResolvedDrawables();
        resolveDrawables();
        invalidate();
        requestLayout();
    }

    @RemotableViewMethod
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(int start, int top, int end, int bottom) {
        Context context = getContext();
        Drawable drawable = null;
        Drawable drawable2 = start != 0 ? context.getDrawable(start) : null;
        Drawable drawable3 = top != 0 ? context.getDrawable(top) : null;
        Drawable drawable4 = end != 0 ? context.getDrawable(end) : null;
        if (bottom != 0) {
            drawable = context.getDrawable(bottom);
        }
        setCompoundDrawablesRelativeWithIntrinsicBounds(drawable2, drawable3, drawable4, drawable);
    }

    @RemotableViewMethod
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(Drawable start, Drawable top, Drawable end, Drawable bottom) {
        if (start != null) {
            start.setBounds(0, 0, start.getIntrinsicWidth(), start.getIntrinsicHeight());
        }
        if (end != null) {
            end.setBounds(0, 0, end.getIntrinsicWidth(), end.getIntrinsicHeight());
        }
        if (top != null) {
            top.setBounds(0, 0, top.getIntrinsicWidth(), top.getIntrinsicHeight());
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, bottom.getIntrinsicWidth(), bottom.getIntrinsicHeight());
        }
        setCompoundDrawablesRelative(start, top, end, bottom);
    }

    public Drawable[] getCompoundDrawables() {
        Drawables dr = this.mDrawables;
        if (dr != null) {
            return (Drawable[]) dr.mShowing.clone();
        }
        return new Drawable[]{null, null, null, null};
    }

    public Drawable[] getCompoundDrawablesRelative() {
        if (this.mDrawables != null) {
            return new Drawable[]{this.mDrawables.mDrawableStart, this.mDrawables.mShowing[1], this.mDrawables.mDrawableEnd, this.mDrawables.mShowing[3]};
        }
        return new Drawable[]{null, null, null, null};
    }

    @RemotableViewMethod
    public void setCompoundDrawablePadding(int pad) {
        Drawables dr = this.mDrawables;
        if (pad != 0) {
            if (dr == null) {
                Drawables drawables = new Drawables(getContext());
                dr = drawables;
                this.mDrawables = drawables;
            }
            dr.mDrawablePadding = pad;
        } else if (dr != null) {
            dr.mDrawablePadding = pad;
        }
        invalidate();
        requestLayout();
    }

    public int getCompoundDrawablePadding() {
        Drawables dr = this.mDrawables;
        return dr != null ? dr.mDrawablePadding : 0;
    }

    public void setCompoundDrawableTintList(ColorStateList tint) {
        if (this.mDrawables == null) {
            this.mDrawables = new Drawables(getContext());
        }
        Drawables drawables = this.mDrawables;
        drawables.mTintList = tint;
        drawables.mHasTint = true;
        applyCompoundDrawableTint();
    }

    public ColorStateList getCompoundDrawableTintList() {
        Drawables drawables = this.mDrawables;
        return drawables != null ? drawables.mTintList : null;
    }

    public void setCompoundDrawableTintMode(Mode tintMode) {
        setCompoundDrawableTintBlendMode(tintMode != null ? BlendMode.fromValue(tintMode.nativeInt) : null);
    }

    public void setCompoundDrawableTintBlendMode(BlendMode blendMode) {
        if (this.mDrawables == null) {
            this.mDrawables = new Drawables(getContext());
        }
        Drawables drawables = this.mDrawables;
        drawables.mBlendMode = blendMode;
        drawables.mHasTintMode = true;
        applyCompoundDrawableTint();
    }

    public Mode getCompoundDrawableTintMode() {
        BlendMode mode = getCompoundDrawableTintBlendMode();
        return mode != null ? BlendMode.blendModeToPorterDuffMode(mode) : null;
    }

    public BlendMode getCompoundDrawableTintBlendMode() {
        Drawables drawables = this.mDrawables;
        return drawables != null ? drawables.mBlendMode : null;
    }

    private void applyCompoundDrawableTint() {
        Drawables drawables = this.mDrawables;
        if (drawables != null) {
            if (drawables.mHasTint || this.mDrawables.mHasTintMode) {
                ColorStateList tintList = this.mDrawables.mTintList;
                BlendMode blendMode = this.mDrawables.mBlendMode;
                boolean hasTint = this.mDrawables.mHasTint;
                boolean hasTintMode = this.mDrawables.mHasTintMode;
                int[] state = getDrawableState();
                for (Drawable dr : this.mDrawables.mShowing) {
                    if (!(dr == null || dr == this.mDrawables.mDrawableError)) {
                        dr.mutate();
                        if (hasTint) {
                            dr.setTintList(tintList);
                        }
                        if (hasTintMode) {
                            dr.setTintBlendMode(blendMode);
                        }
                        if (dr.isStateful()) {
                            dr.setState(state);
                        }
                    }
                }
            }
        }
    }

    public void setPadding(int left, int top, int right, int bottom) {
        if (!(left == this.mPaddingLeft && right == this.mPaddingRight && top == this.mPaddingTop && bottom == this.mPaddingBottom)) {
            nullLayouts();
        }
        super.setPadding(left, top, right, bottom);
        invalidate();
    }

    public void setPaddingRelative(int start, int top, int end, int bottom) {
        if (!(start == getPaddingStart() && end == getPaddingEnd() && top == this.mPaddingTop && bottom == this.mPaddingBottom)) {
            nullLayouts();
        }
        super.setPaddingRelative(start, top, end, bottom);
        invalidate();
    }

    public void setFirstBaselineToTopHeight(int firstBaselineToTopHeight) {
        int fontMetricsTop;
        Preconditions.checkArgumentNonnegative(firstBaselineToTopHeight);
        FontMetricsInt fontMetrics = getPaint().getFontMetricsInt();
        if (getIncludeFontPadding()) {
            fontMetricsTop = fontMetrics.top;
        } else {
            fontMetricsTop = fontMetrics.ascent;
        }
        if (firstBaselineToTopHeight > Math.abs(fontMetricsTop)) {
            setPadding(getPaddingLeft(), firstBaselineToTopHeight - (-fontMetricsTop), getPaddingRight(), getPaddingBottom());
        }
    }

    public void setLastBaselineToBottomHeight(int lastBaselineToBottomHeight) {
        int fontMetricsBottom;
        Preconditions.checkArgumentNonnegative(lastBaselineToBottomHeight);
        FontMetricsInt fontMetrics = getPaint().getFontMetricsInt();
        if (getIncludeFontPadding()) {
            fontMetricsBottom = fontMetrics.bottom;
        } else {
            fontMetricsBottom = fontMetrics.descent;
        }
        if (lastBaselineToBottomHeight > Math.abs(fontMetricsBottom)) {
            setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), lastBaselineToBottomHeight - fontMetricsBottom);
        }
    }

    public int getFirstBaselineToTopHeight() {
        return getPaddingTop() - getPaint().getFontMetricsInt().top;
    }

    public int getLastBaselineToBottomHeight() {
        return getPaddingBottom() + getPaint().getFontMetricsInt().bottom;
    }

    public final int getAutoLinkMask() {
        return this.mAutoLinkMask;
    }

    @RemotableViewMethod
    public void setTextSelectHandle(Drawable textSelectHandle) {
        Preconditions.checkNotNull(textSelectHandle, "The text select handle should not be null.");
        this.mTextSelectHandle = textSelectHandle;
        this.mTextSelectHandleRes = 0;
        Editor editor = this.mEditor;
        if (editor != null) {
            editor.loadHandleDrawables(true);
        }
    }

    @RemotableViewMethod
    public void setTextSelectHandle(int textSelectHandle) {
        Preconditions.checkArgument(textSelectHandle != 0, "The text select handle should be a valid drawable resource id.");
        setTextSelectHandle(this.mContext.getDrawable(textSelectHandle));
    }

    public Drawable getTextSelectHandle() {
        if (this.mTextSelectHandle == null && this.mTextSelectHandleRes != 0) {
            this.mTextSelectHandle = this.mContext.getDrawable(this.mTextSelectHandleRes);
        }
        return this.mTextSelectHandle;
    }

    @RemotableViewMethod
    public void setTextSelectHandleLeft(Drawable textSelectHandleLeft) {
        Preconditions.checkNotNull(textSelectHandleLeft, "The left text select handle should not be null.");
        this.mTextSelectHandleLeft = textSelectHandleLeft;
        this.mTextSelectHandleLeftRes = 0;
        Editor editor = this.mEditor;
        if (editor != null) {
            editor.loadHandleDrawables(true);
        }
    }

    @RemotableViewMethod
    public void setTextSelectHandleLeft(int textSelectHandleLeft) {
        Preconditions.checkArgument(textSelectHandleLeft != 0, "The text select left handle should be a valid drawable resource id.");
        setTextSelectHandleLeft(this.mContext.getDrawable(textSelectHandleLeft));
    }

    public Drawable getTextSelectHandleLeft() {
        if (this.mTextSelectHandleLeft == null && this.mTextSelectHandleLeftRes != 0) {
            this.mTextSelectHandleLeft = this.mContext.getDrawable(this.mTextSelectHandleLeftRes);
        }
        return this.mTextSelectHandleLeft;
    }

    @RemotableViewMethod
    public void setTextSelectHandleRight(Drawable textSelectHandleRight) {
        Preconditions.checkNotNull(textSelectHandleRight, "The right text select handle should not be null.");
        this.mTextSelectHandleRight = textSelectHandleRight;
        this.mTextSelectHandleRightRes = 0;
        Editor editor = this.mEditor;
        if (editor != null) {
            editor.loadHandleDrawables(true);
        }
    }

    @RemotableViewMethod
    public void setTextSelectHandleRight(int textSelectHandleRight) {
        Preconditions.checkArgument(textSelectHandleRight != 0, "The text select right handle should be a valid drawable resource id.");
        setTextSelectHandleRight(this.mContext.getDrawable(textSelectHandleRight));
    }

    public Drawable getTextSelectHandleRight() {
        if (this.mTextSelectHandleRight == null && this.mTextSelectHandleRightRes != 0) {
            this.mTextSelectHandleRight = this.mContext.getDrawable(this.mTextSelectHandleRightRes);
        }
        return this.mTextSelectHandleRight;
    }

    public void setTextCursorDrawable(Drawable textCursorDrawable) {
        this.mCursorDrawable = textCursorDrawable;
        this.mCursorDrawableRes = 0;
        Editor editor = this.mEditor;
        if (editor != null) {
            editor.loadCursorDrawable();
        }
    }

    public void setTextCursorDrawable(int textCursorDrawable) {
        setTextCursorDrawable(textCursorDrawable != 0 ? this.mContext.getDrawable(textCursorDrawable) : null);
    }

    public Drawable getTextCursorDrawable() {
        if (this.mCursorDrawable == null && this.mCursorDrawableRes != 0) {
            this.mCursorDrawable = this.mContext.getDrawable(this.mCursorDrawableRes);
        }
        return this.mCursorDrawable;
    }

    public void setTextAppearance(int resId) {
        setTextAppearance(this.mContext, resId);
    }

    @Deprecated
    public void setTextAppearance(Context context, int resId) {
        TypedArray ta = context.obtainStyledAttributes(resId, R.styleable.TextAppearance);
        TextAppearanceAttributes attributes = new TextAppearanceAttributes();
        readTextAppearance(context, ta, attributes, false);
        ta.recycle();
        applyTextAppearance(attributes);
    }

    private void readTextAppearance(Context context, TypedArray appearance, TextAppearanceAttributes attributes, boolean styleArray) {
        int n = appearance.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = appearance.getIndex(i);
            int index = attr;
            if (styleArray) {
                index = sAppearanceValues.get(attr, -1);
                if (index == -1) {
                }
            }
            switch (index) {
                case 0:
                    attributes.mTextSize = appearance.getDimensionPixelSize(attr, attributes.mTextSize);
                    break;
                case 1:
                    attributes.mTypefaceIndex = appearance.getInt(attr, attributes.mTypefaceIndex);
                    if (!(attributes.mTypefaceIndex == -1 || attributes.mFontFamilyExplicit)) {
                        attributes.mFontFamily = null;
                        break;
                    }
                case 2:
                    attributes.mTextStyle = appearance.getInt(attr, attributes.mTextStyle);
                    break;
                case 3:
                    attributes.mTextColor = appearance.getColorStateList(attr);
                    break;
                case 4:
                    attributes.mTextColorHighlight = appearance.getColor(attr, attributes.mTextColorHighlight);
                    break;
                case 5:
                    attributes.mTextColorHint = appearance.getColorStateList(attr);
                    break;
                case 6:
                    attributes.mTextColorLink = appearance.getColorStateList(attr);
                    break;
                case 7:
                    attributes.mShadowColor = appearance.getInt(attr, attributes.mShadowColor);
                    break;
                case 8:
                    attributes.mShadowDx = appearance.getFloat(attr, attributes.mShadowDx);
                    break;
                case 9:
                    attributes.mShadowDy = appearance.getFloat(attr, attributes.mShadowDy);
                    break;
                case 10:
                    attributes.mShadowRadius = appearance.getFloat(attr, attributes.mShadowRadius);
                    break;
                case 11:
                    attributes.mAllCaps = appearance.getBoolean(attr, attributes.mAllCaps);
                    break;
                case 12:
                    if (!context.isRestricted() && context.canLoadUnsafeResources()) {
                        try {
                            attributes.mFontTypeface = appearance.getFont(attr);
                        } catch (NotFoundException | UnsupportedOperationException e) {
                        }
                    }
                    if (attributes.mFontTypeface == null) {
                        attributes.mFontFamily = appearance.getString(attr);
                    }
                    attributes.mFontFamilyExplicit = true;
                    break;
                case 13:
                    attributes.mHasElegant = true;
                    attributes.mElegant = appearance.getBoolean(attr, attributes.mElegant);
                    break;
                case 14:
                    attributes.mHasLetterSpacing = true;
                    attributes.mLetterSpacing = appearance.getFloat(attr, attributes.mLetterSpacing);
                    break;
                case 15:
                    attributes.mFontFeatureSettings = appearance.getString(attr);
                    break;
                case 16:
                    attributes.mFontVariationSettings = appearance.getString(attr);
                    break;
                case 17:
                    attributes.mHasFallbackLineSpacing = true;
                    attributes.mFallbackLineSpacing = appearance.getBoolean(attr, attributes.mFallbackLineSpacing);
                    break;
                case 18:
                    attributes.mFontWeight = appearance.getInt(attr, attributes.mFontWeight);
                    break;
                case 19:
                    String localeString = appearance.getString(attr);
                    if (localeString == null) {
                        break;
                    }
                    LocaleList localeList = LocaleList.forLanguageTags(localeString);
                    if (!localeList.isEmpty()) {
                        attributes.mTextLocales = localeList;
                        break;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void applyTextAppearance(TextAppearanceAttributes attributes) {
        if (attributes.mTextColor != null) {
            setTextColor(attributes.mTextColor);
        }
        if (attributes.mTextColorHint != null) {
            setHintTextColor(attributes.mTextColorHint);
        }
        if (attributes.mTextColorLink != null) {
            setLinkTextColor(attributes.mTextColorLink);
        }
        if (attributes.mTextColorHighlight != 0) {
            setHighlightColor(attributes.mTextColorHighlight);
        }
        if (attributes.mTextSize != -1) {
            setRawTextSize((float) attributes.mTextSize, true);
        }
        if (attributes.mTextLocales != null) {
            setTextLocales(attributes.mTextLocales);
        }
        if (!(attributes.mTypefaceIndex == -1 || attributes.mFontFamilyExplicit)) {
            attributes.mFontFamily = null;
        }
        setTypefaceFromAttrs(attributes.mFontTypeface, attributes.mFontFamily, attributes.mTypefaceIndex, attributes.mTextStyle, attributes.mFontWeight);
        if (attributes.mShadowColor != 0) {
            setShadowLayer(attributes.mShadowRadius, attributes.mShadowDx, attributes.mShadowDy, attributes.mShadowColor);
        }
        if (attributes.mAllCaps) {
            setTransformationMethod(new AllCapsTransformationMethod(getContext()));
        }
        if (attributes.mHasElegant) {
            setElegantTextHeight(attributes.mElegant);
        }
        if (attributes.mHasFallbackLineSpacing) {
            setFallbackLineSpacing(attributes.mFallbackLineSpacing);
        }
        if (attributes.mHasLetterSpacing) {
            setLetterSpacing(attributes.mLetterSpacing);
        }
        if (attributes.mFontFeatureSettings != null) {
            setFontFeatureSettings(attributes.mFontFeatureSettings);
        }
        if (attributes.mFontVariationSettings != null) {
            setFontVariationSettings(attributes.mFontVariationSettings);
        }
    }

    public Locale getTextLocale() {
        return this.mTextPaint.getTextLocale();
    }

    public LocaleList getTextLocales() {
        return this.mTextPaint.getTextLocales();
    }

    private void changeListenerLocaleTo(Locale locale) {
        if (!this.mListenerChanged) {
            KeyListener listener = this.mEditor;
            if (listener != null) {
                listener = listener.mKeyListener;
                if (listener instanceof DigitsKeyListener) {
                    listener = DigitsKeyListener.getInstance(locale, (DigitsKeyListener) listener);
                } else if (listener instanceof DateKeyListener) {
                    listener = DateKeyListener.getInstance(locale);
                } else if (listener instanceof TimeKeyListener) {
                    listener = TimeKeyListener.getInstance(locale);
                } else if (listener instanceof DateTimeKeyListener) {
                    listener = DateTimeKeyListener.getInstance(locale);
                } else {
                    return;
                }
                boolean wasPasswordType = isPasswordInputType(this.mEditor.mInputType);
                setKeyListenerOnly(listener);
                setInputTypeFromEditor();
                if (wasPasswordType) {
                    int newInputClass = this.mEditor.mInputType & 15;
                    Editor editor;
                    if (newInputClass == 1) {
                        editor = this.mEditor;
                        editor.mInputType |= 128;
                    } else if (newInputClass == 2) {
                        editor = this.mEditor;
                        editor.mInputType |= 16;
                    }
                }
            }
        }
    }

    public void setTextLocale(Locale locale) {
        this.mLocalesChanged = true;
        this.mTextPaint.setTextLocale(locale);
        if (this.mLayout != null) {
            nullLayouts();
            requestLayout();
            invalidate();
        }
    }

    public void setTextLocales(LocaleList locales) {
        this.mLocalesChanged = true;
        this.mTextPaint.setTextLocales(locales);
        if (this.mLayout != null) {
            nullLayouts();
            requestLayout();
            invalidate();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (TypefaceUtils.isFontChanged(newConfig)) {
            replaceTypeface(getTypeface());
        }
        if (!this.mLocalesChanged) {
            this.mTextPaint.setTextLocales(LocaleList.getDefault());
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    @ExportedProperty(category = "text")
    public float getTextSize() {
        return this.mTextPaint.getTextSize();
    }

    @ExportedProperty(category = "text")
    public float getScaledTextSize() {
        return this.mTextPaint.getTextSize() / this.mTextPaint.density;
    }

    @ExportedProperty(category = "text", mapping = {@IntToString(from = 0, to = "NORMAL"), @IntToString(from = 1, to = "BOLD"), @IntToString(from = 2, to = "ITALIC"), @IntToString(from = 3, to = "BOLD_ITALIC")})
    public int getTypefaceStyle() {
        Typeface typeface = this.mTextPaint.getTypeface();
        return typeface != null ? typeface.getStyle() : 0;
    }

    @RemotableViewMethod
    public void setTextSize(float size) {
        setTextSize(2, size);
    }

    public void setTextSize(int unit, float size) {
        if (!isAutoSizeEnabled()) {
            setTextSizeInternal(unit, size, true);
        }
    }

    private void setTextSizeInternal(int unit, float size, boolean shouldRequestLayout) {
        Resources r;
        Context c = getContext();
        if (c == null) {
            r = Resources.getSystem();
        } else {
            r = c.getResources();
        }
        setRawTextSize(TypedValue.applyDimension(unit, size, r.getDisplayMetrics()), shouldRequestLayout);
    }

    @UnsupportedAppUsage
    private void setRawTextSize(float size, boolean shouldRequestLayout) {
        if (size != this.mTextPaint.getTextSize()) {
            this.mTextPaint.setTextSize(size);
            if (shouldRequestLayout && this.mLayout != null) {
                this.mNeedsAutoSizeText = false;
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public float getTextScaleX() {
        return this.mTextPaint.getTextScaleX();
    }

    @RemotableViewMethod
    public void setTextScaleX(float size) {
        if (size != this.mTextPaint.getTextScaleX()) {
            this.mUserSetTextScaleX = true;
            this.mTextPaint.setTextScaleX(size);
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public void setTypeface(Typeface tf) {
        if (replaceTypeface(tf) && this.mLayout != null) {
            nullLayouts();
            requestLayout();
            invalidate();
        }
    }

    private boolean replaceTypeface(Typeface tf) {
        tf = TypefaceUtils.replaceTypeface(getContext(), tf, getTextSize());
        if (this.mTextPaint.getTypeface() == tf) {
            return false;
        }
        this.mTextPaint.setTypeface(tf);
        return true;
    }

    public Typeface getTypeface() {
        return this.mTextPaint.getTypeface();
    }

    public void setElegantTextHeight(boolean elegant) {
        if (elegant != this.mTextPaint.isElegantTextHeight()) {
            this.mTextPaint.setElegantTextHeight(elegant);
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public void setFallbackLineSpacing(boolean enabled) {
        if (this.mUseFallbackLineSpacing != enabled) {
            this.mUseFallbackLineSpacing = enabled;
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public boolean isFallbackLineSpacing() {
        return this.mUseFallbackLineSpacing;
    }

    public boolean isElegantTextHeight() {
        return this.mTextPaint.isElegantTextHeight();
    }

    public float getLetterSpacing() {
        return this.mTextPaint.getLetterSpacing();
    }

    @RemotableViewMethod
    public void setLetterSpacing(float letterSpacing) {
        if (letterSpacing != this.mTextPaint.getLetterSpacing()) {
            this.mTextPaint.setLetterSpacing(letterSpacing);
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public String getFontFeatureSettings() {
        return this.mTextPaint.getFontFeatureSettings();
    }

    public String getFontVariationSettings() {
        return this.mTextPaint.getFontVariationSettings();
    }

    public void setBreakStrategy(int breakStrategy) {
        this.mBreakStrategy = breakStrategy;
        if (this.mLayout != null) {
            nullLayouts();
            requestLayout();
            invalidate();
        }
    }

    public int getBreakStrategy() {
        return this.mBreakStrategy;
    }

    public void setHyphenationFrequency(int hyphenationFrequency) {
        this.mHyphenationFrequency = hyphenationFrequency;
        if (this.mLayout != null) {
            nullLayouts();
            requestLayout();
            invalidate();
        }
    }

    public int getHyphenationFrequency() {
        return this.mHyphenationFrequency;
    }

    public Params getTextMetricsParams() {
        return new Params(new TextPaint(this.mTextPaint), getTextDirectionHeuristic(), this.mBreakStrategy, this.mHyphenationFrequency);
    }

    public void setTextMetricsParams(Params params) {
        this.mTextPaint.set(params.getTextPaint());
        this.mUserSetTextScaleX = true;
        this.mTextDir = params.getTextDirection();
        this.mBreakStrategy = params.getBreakStrategy();
        this.mHyphenationFrequency = params.getHyphenationFrequency();
        if (this.mLayout != null) {
            nullLayouts();
            requestLayout();
            invalidate();
        }
    }

    public void setJustificationMode(int justificationMode) {
        this.mJustificationMode = justificationMode;
        if (this.mLayout != null) {
            nullLayouts();
            requestLayout();
            invalidate();
        }
    }

    public int getJustificationMode() {
        return this.mJustificationMode;
    }

    @RemotableViewMethod
    public void setFontFeatureSettings(String fontFeatureSettings) {
        if (fontFeatureSettings != this.mTextPaint.getFontFeatureSettings()) {
            this.mTextPaint.setFontFeatureSettings(fontFeatureSettings);
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public boolean setFontVariationSettings(String fontVariationSettings) {
        String existingSettings = this.mTextPaint.getFontVariationSettings();
        if (fontVariationSettings == existingSettings || (fontVariationSettings != null && fontVariationSettings.equals(existingSettings))) {
            return true;
        }
        boolean effective = this.mTextPaint.setFontVariationSettings(fontVariationSettings);
        if (effective && this.mLayout != null) {
            nullLayouts();
            requestLayout();
            invalidate();
        }
        return effective;
    }

    @RemotableViewMethod
    public void setTextColor(int color) {
        this.mTextColor = ColorStateList.valueOf(color);
        updateTextColors();
    }

    @RemotableViewMethod
    public void setTextColor(ColorStateList colors) {
        if (colors != null) {
            this.mTextColor = colors;
            updateTextColors();
            return;
        }
        throw new NullPointerException();
    }

    public final ColorStateList getTextColors() {
        return this.mTextColor;
    }

    public final int getCurrentTextColor() {
        return this.mCurTextColor;
    }

    @RemotableViewMethod
    public void setHighlightColor(int color) {
        if (this.mHighlightColor != color) {
            this.mHighlightColor = color;
            invalidate();
        }
    }

    public int getHighlightColor() {
        return this.mHighlightColor;
    }

    @RemotableViewMethod
    public final void setShowSoftInputOnFocus(boolean show) {
        createEditorIfNeeded();
        this.mEditor.mShowSoftInputOnFocus = show;
    }

    public final boolean getShowSoftInputOnFocus() {
        Editor editor = this.mEditor;
        return editor == null || editor.mShowSoftInputOnFocus;
    }

    public void setShadowLayer(float radius, float dx, float dy, int color) {
        this.mTextPaint.setShadowLayer(radius, dx, dy, color);
        this.mShadowRadius = radius;
        this.mShadowDx = dx;
        this.mShadowDy = dy;
        this.mShadowColor = color;
        Editor editor = this.mEditor;
        if (editor != null) {
            editor.invalidateTextDisplayList();
            this.mEditor.invalidateHandlesAndActionMode();
        }
        invalidate();
    }

    public float getShadowRadius() {
        return this.mShadowRadius;
    }

    public float getShadowDx() {
        return this.mShadowDx;
    }

    public float getShadowDy() {
        return this.mShadowDy;
    }

    public int getShadowColor() {
        return this.mShadowColor;
    }

    public TextPaint getPaint() {
        return this.mTextPaint;
    }

    @RemotableViewMethod
    public final void setAutoLinkMask(int mask) {
        this.mAutoLinkMask = mask;
    }

    @RemotableViewMethod
    public final void setLinksClickable(boolean whether) {
        this.mLinksClickable = whether;
    }

    public final boolean getLinksClickable() {
        return this.mLinksClickable;
    }

    public URLSpan[] getUrls() {
        CharSequence charSequence = this.mText;
        if (charSequence instanceof Spanned) {
            return (URLSpan[]) ((Spanned) charSequence).getSpans(0, charSequence.length(), URLSpan.class);
        }
        return new URLSpan[0];
    }

    @RemotableViewMethod
    public final void setHintTextColor(int color) {
        this.mHintTextColor = ColorStateList.valueOf(color);
        updateTextColors();
    }

    public final void setHintTextColor(ColorStateList colors) {
        this.mHintTextColor = colors;
        updateTextColors();
    }

    public final ColorStateList getHintTextColors() {
        return this.mHintTextColor;
    }

    public final int getCurrentHintTextColor() {
        return this.mHintTextColor != null ? this.mCurHintTextColor : this.mCurTextColor;
    }

    @RemotableViewMethod
    public final void setLinkTextColor(int color) {
        this.mLinkTextColor = ColorStateList.valueOf(color);
        updateTextColors();
    }

    public final void setLinkTextColor(ColorStateList colors) {
        this.mLinkTextColor = colors;
        updateTextColors();
    }

    public final ColorStateList getLinkTextColors() {
        return this.mLinkTextColor;
    }

    public void setGravity(int gravity) {
        if ((gravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) == 0) {
            gravity |= Gravity.START;
        }
        if ((gravity & 112) == 0) {
            gravity |= 48;
        }
        boolean newLayout = false;
        if ((gravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) != (Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK & this.mGravity)) {
            newLayout = true;
        }
        if (gravity != this.mGravity) {
            invalidate();
        }
        this.mGravity = gravity;
        int want = this.mLayout;
        if (want != 0 && newLayout) {
            want = want.getWidth();
            Layout layout = this.mHintLayout;
            int hintWant = layout == null ? 0 : layout.getWidth();
            Metrics metrics = UNKNOWN_BORING;
            makeNewLayout(want, hintWant, metrics, metrics, ((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight(), true);
        }
    }

    public int getGravity() {
        return this.mGravity;
    }

    public int getPaintFlags() {
        return this.mTextPaint.getFlags();
    }

    @RemotableViewMethod
    public void setPaintFlags(int flags) {
        if (this.mTextPaint.getFlags() != flags) {
            this.mTextPaint.setFlags(flags);
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public void setHorizontallyScrolling(boolean whether) {
        if (this.mHorizontallyScrolling != whether) {
            this.mHorizontallyScrolling = whether;
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public final boolean isHorizontallyScrollable() {
        return this.mHorizontallyScrolling;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public boolean getHorizontallyScrolling() {
        return this.mHorizontallyScrolling;
    }

    @RemotableViewMethod
    public void setMinLines(int minLines) {
        this.mMinimum = minLines;
        this.mMinMode = 1;
        requestLayout();
        invalidate();
    }

    public int getMinLines() {
        return this.mMinMode == 1 ? this.mMinimum : -1;
    }

    @RemotableViewMethod
    public void setMinHeight(int minPixels) {
        this.mMinimum = minPixels;
        this.mMinMode = 2;
        requestLayout();
        invalidate();
    }

    public int getMinHeight() {
        return this.mMinMode == 2 ? this.mMinimum : -1;
    }

    @RemotableViewMethod
    public void setMaxLines(int maxLines) {
        this.mMaximum = maxLines;
        this.mMaxMode = 1;
        requestLayout();
        invalidate();
    }

    public int getMaxLines() {
        return this.mMaxMode == 1 ? this.mMaximum : -1;
    }

    @RemotableViewMethod
    public void setMaxHeight(int maxPixels) {
        this.mMaximum = maxPixels;
        this.mMaxMode = 2;
        requestLayout();
        invalidate();
    }

    public int getMaxHeight() {
        return this.mMaxMode == 2 ? this.mMaximum : -1;
    }

    @RemotableViewMethod
    public void setLines(int lines) {
        this.mMinimum = lines;
        this.mMaximum = lines;
        this.mMinMode = 1;
        this.mMaxMode = 1;
        requestLayout();
        invalidate();
    }

    @RemotableViewMethod
    public void setHeight(int pixels) {
        this.mMinimum = pixels;
        this.mMaximum = pixels;
        this.mMinMode = 2;
        this.mMaxMode = 2;
        requestLayout();
        invalidate();
    }

    @RemotableViewMethod
    public void setMinEms(int minEms) {
        this.mMinWidth = minEms;
        this.mMinWidthMode = 1;
        requestLayout();
        invalidate();
    }

    public int getMinEms() {
        return this.mMinWidthMode == 1 ? this.mMinWidth : -1;
    }

    @RemotableViewMethod
    public void setMinWidth(int minPixels) {
        this.mMinWidth = minPixels;
        this.mMinWidthMode = 2;
        requestLayout();
        invalidate();
    }

    public int getMinWidth() {
        return this.mMinWidthMode == 2 ? this.mMinWidth : -1;
    }

    @RemotableViewMethod
    public void setMaxEms(int maxEms) {
        this.mMaxWidth = maxEms;
        this.mMaxWidthMode = 1;
        requestLayout();
        invalidate();
    }

    public int getMaxEms() {
        return this.mMaxWidthMode == 1 ? this.mMaxWidth : -1;
    }

    @RemotableViewMethod
    public void setMaxWidth(int maxPixels) {
        this.mMaxWidth = maxPixels;
        this.mMaxWidthMode = 2;
        requestLayout();
        invalidate();
    }

    public int getMaxWidth() {
        return this.mMaxWidthMode == 2 ? this.mMaxWidth : -1;
    }

    @RemotableViewMethod
    public void setEms(int ems) {
        this.mMinWidth = ems;
        this.mMaxWidth = ems;
        this.mMinWidthMode = 1;
        this.mMaxWidthMode = 1;
        requestLayout();
        invalidate();
    }

    @RemotableViewMethod
    public void setWidth(int pixels) {
        this.mMinWidth = pixels;
        this.mMaxWidth = pixels;
        this.mMinWidthMode = 2;
        this.mMaxWidthMode = 2;
        requestLayout();
        invalidate();
    }

    public void setLineSpacing(float add, float mult) {
        if (this.mSpacingAdd != add || this.mSpacingMult != mult) {
            this.mSpacingAdd = add;
            this.mSpacingMult = mult;
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public float getLineSpacingMultiplier() {
        return this.mSpacingMult;
    }

    public float getLineSpacingExtra() {
        return this.mSpacingAdd;
    }

    public void setLineHeight(int lineHeight) {
        Preconditions.checkArgumentNonnegative(lineHeight);
        int fontHeight = getPaint().getFontMetricsInt(null);
        if (lineHeight != fontHeight) {
            setLineSpacing((float) (lineHeight - fontHeight), 1.0f);
        }
    }

    public final void append(CharSequence text) {
        append(text, 0, text.length());
    }

    public void append(CharSequence text, int start, int end) {
        CharSequence charSequence = this.mText;
        if (!(charSequence instanceof Editable)) {
            setText(charSequence, BufferType.EDITABLE);
        }
        ((Editable) this.mText).append(text, start, end);
        int linksWereAdded = this.mAutoLinkMask;
        if (linksWereAdded == true && Linkify.addLinks(this.mSpannable, linksWereAdded) && this.mLinksClickable && !textCanBeSelected()) {
            setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private void updateTextColors() {
        boolean inval = false;
        int[] drawableState = getDrawableState();
        int color = this.mTextColor.getColorForState(drawableState, 0);
        if (color != this.mCurTextColor) {
            this.mCurTextColor = color;
            inval = true;
        }
        ColorStateList colorStateList = this.mLinkTextColor;
        if (colorStateList != null) {
            color = colorStateList.getColorForState(drawableState, 0);
            if (color != this.mTextPaint.linkColor) {
                this.mTextPaint.linkColor = color;
                inval = true;
            }
        }
        colorStateList = this.mHintTextColor;
        if (colorStateList != null) {
            color = colorStateList.getColorForState(drawableState, 0);
            if (color != this.mCurHintTextColor) {
                this.mCurHintTextColor = color;
                if (this.mText.length() == 0) {
                    inval = true;
                }
            }
        }
        if (inval) {
            Editor editor = this.mEditor;
            if (editor != null) {
                editor.invalidateTextDisplayList();
            }
            invalidate();
        }
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:30:? A:{SYNTHETIC, RETURN, ORIG_RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0028  */
    /* JADX WARNING: Missing block: B:11:0x001f, code skipped:
            if (r0.isStateful() != false) goto L_0x0021;
     */
    public void drawableStateChanged() {
        /*
        r6 = this;
        super.drawableStateChanged();
        r0 = r6.mTextColor;
        if (r0 == 0) goto L_0x000d;
    L_0x0007:
        r0 = r0.isStateful();
        if (r0 != 0) goto L_0x0021;
    L_0x000d:
        r0 = r6.mHintTextColor;
        if (r0 == 0) goto L_0x0017;
    L_0x0011:
        r0 = r0.isStateful();
        if (r0 != 0) goto L_0x0021;
    L_0x0017:
        r0 = r6.mLinkTextColor;
        if (r0 == 0) goto L_0x0024;
    L_0x001b:
        r0 = r0.isStateful();
        if (r0 == 0) goto L_0x0024;
    L_0x0021:
        r6.updateTextColors();
    L_0x0024:
        r0 = r6.mDrawables;
        if (r0 == 0) goto L_0x004a;
    L_0x0028:
        r0 = r6.getDrawableState();
        r1 = r6.mDrawables;
        r1 = r1.mShowing;
        r2 = r1.length;
        r3 = 0;
    L_0x0032:
        if (r3 >= r2) goto L_0x004a;
    L_0x0034:
        r4 = r1[r3];
        if (r4 == 0) goto L_0x0047;
    L_0x0038:
        r5 = r4.isStateful();
        if (r5 == 0) goto L_0x0047;
    L_0x003e:
        r5 = r4.setState(r0);
        if (r5 == 0) goto L_0x0047;
    L_0x0044:
        r6.invalidateDrawable(r4);
    L_0x0047:
        r3 = r3 + 1;
        goto L_0x0032;
    L_0x004a:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.TextView.drawableStateChanged():void");
    }

    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        Drawables drawables = this.mDrawables;
        if (drawables != null) {
            for (Drawable dr : drawables.mShowing) {
                if (dr != null) {
                    dr.setHotspot(x, y);
                }
            }
        }
    }

    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        boolean freezesText = getFreezesText();
        boolean hasSelection = false;
        int start = -1;
        int end = -1;
        if (this.mText != null) {
            start = getSelectionStart();
            end = getSelectionEnd();
            if (start >= 0 || end >= 0) {
                hasSelection = true;
            }
        }
        if (!freezesText && !hasSelection) {
            return superState;
        }
        SavedState ss = new SavedState(superState);
        if (freezesText) {
            CharSequence charSequence = this.mText;
            if (charSequence instanceof Spanned) {
                SpannableStringBuilder sp = new SpannableStringBuilder(charSequence);
                if (this.mEditor != null) {
                    removeMisspelledSpans(sp);
                    sp.removeSpan(this.mEditor.mSuggestionRangeSpan);
                }
                ss.text = sp;
            } else {
                ss.text = charSequence.toString();
            }
        }
        if (hasSelection) {
            ss.selStart = start;
            ss.selEnd = end;
        }
        if (isFocused() && start >= 0 && end >= 0) {
            ss.frozenWithFocus = true;
        }
        ss.error = getError();
        Editor editor = this.mEditor;
        if (editor != null) {
            ss.editorState = editor.saveInstanceState();
        }
        return ss;
    }

    /* Access modifiers changed, original: 0000 */
    public void removeMisspelledSpans(Spannable spannable) {
        SuggestionSpan[] suggestionSpans = (SuggestionSpan[]) spannable.getSpans(0, spannable.length(), SuggestionSpan.class);
        for (int i = 0; i < suggestionSpans.length; i++) {
            int flags = suggestionSpans[i].getFlags();
            if (!((flags & 1) == 0 || (flags & 2) == 0)) {
                spannable.removeSpan(suggestionSpans[i]);
            }
        }
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState ss = (SavedState) state;
            super.onRestoreInstanceState(ss.getSuperState());
            if (ss.text != null) {
                setText(ss.text);
            }
            if (ss.selStart >= 0 && ss.selEnd >= 0 && this.mSpannable != null) {
                int len = this.mText.length();
                if (ss.selStart > len || ss.selEnd > len) {
                    String restored = "";
                    if (ss.text != null) {
                        restored = "(restored) ";
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Saved cursor position ");
                    stringBuilder.append(ss.selStart);
                    stringBuilder.append("/");
                    stringBuilder.append(ss.selEnd);
                    stringBuilder.append(" out of range for ");
                    stringBuilder.append(restored);
                    stringBuilder.append("text ");
                    stringBuilder.append(this.mText);
                    Log.e(LOG_TAG, stringBuilder.toString());
                } else {
                    Selection.setSelection(this.mSpannable, ss.selStart, ss.selEnd);
                    if (ss.frozenWithFocus) {
                        createEditorIfNeeded();
                        this.mEditor.mFrozenWithFocus = true;
                    }
                }
            }
            if (ss.error != null) {
                final CharSequence error = ss.error;
                post(new Runnable() {
                    public void run() {
                        if (TextView.this.mEditor == null || !TextView.this.mEditor.mErrorWasChanged) {
                            TextView.this.setError(error);
                        }
                    }
                });
            }
            if (ss.editorState != null) {
                createEditorIfNeeded();
                this.mEditor.restoreInstanceState(ss.editorState);
            }
            return;
        }
        super.onRestoreInstanceState(state);
    }

    @RemotableViewMethod
    public void setFreezesText(boolean freezesText) {
        this.mFreezesText = freezesText;
    }

    public boolean getFreezesText() {
        return this.mFreezesText;
    }

    public final void setEditableFactory(Factory factory) {
        this.mEditableFactory = factory;
        setText(this.mText);
    }

    public final void setSpannableFactory(Spannable.Factory factory) {
        this.mSpannableFactory = factory;
        setText(this.mText);
    }

    @RemotableViewMethod
    public final void setText(CharSequence text) {
        setText(text, this.mBufferType);
    }

    @RemotableViewMethod
    public final void setTextKeepState(CharSequence text) {
        setTextKeepState(text, this.mBufferType);
    }

    public void setText(CharSequence text, BufferType type) {
        setText(text, type, true, 0);
        CharWrapper charWrapper = this.mCharWrapper;
        if (charWrapper != null) {
            charWrapper.mChars = null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x00a1  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x009d  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0138  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0140  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x0186  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x0183  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x0190  */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x01b1 A:{LOOP_END, LOOP:1: B:98:0x01af->B:99:0x01b1} */
    /* JADX WARNING: Removed duplicated region for block: B:102:0x01bd  */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x01d0  */
    /* JADX WARNING: Removed duplicated region for block: B:108:0x01d7  */
    /* JADX WARNING: Removed duplicated region for block: B:111:0x01e0  */
    /* JADX WARNING: Removed duplicated region for block: B:116:0x01f0  */
    /* JADX WARNING: Removed duplicated region for block: B:120:0x0206  */
    /* JADX WARNING: Removed duplicated region for block: B:119:0x01ff  */
    /* JADX WARNING: Removed duplicated region for block: B:129:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:123:0x020d  */
    @android.annotation.UnsupportedAppUsage
    private void setText(java.lang.CharSequence r17, android.widget.TextView.BufferType r18, boolean r19, int r20) {
        /*
        r16 = this;
        r0 = r16;
        r1 = r18;
        r2 = 0;
        r0.mTextSetFromXmlOrResourceId = r2;
        if (r17 != 0) goto L_0x000c;
    L_0x0009:
        r3 = "";
        goto L_0x000e;
    L_0x000c:
        r3 = r17;
    L_0x000e:
        r4 = r16.isSuggestionsEnabled();
        if (r4 != 0) goto L_0x0018;
    L_0x0014:
        r3 = r0.removeSuggestionSpans(r3);
    L_0x0018:
        r4 = r0.mUserSetTextScaleX;
        if (r4 != 0) goto L_0x0023;
    L_0x001c:
        r4 = r0.mTextPaint;
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r4.setTextScaleX(r5);
    L_0x0023:
        r4 = r3 instanceof android.text.Spanned;
        r5 = 1;
        if (r4 == 0) goto L_0x004f;
    L_0x0028:
        r4 = r3;
        r4 = (android.text.Spanned) r4;
        r6 = android.text.TextUtils.TruncateAt.MARQUEE;
        r4 = r4.getSpanStart(r6);
        if (r4 < 0) goto L_0x004f;
    L_0x0033:
        r4 = r0.mContext;
        r4 = android.view.ViewConfiguration.get(r4);
        r4 = r4.isFadingMarqueeEnabled();
        if (r4 == 0) goto L_0x0045;
    L_0x003f:
        r0.setHorizontalFadingEdgeEnabled(r5);
        r0.mMarqueeFadeMode = r2;
        goto L_0x004a;
    L_0x0045:
        r0.setHorizontalFadingEdgeEnabled(r2);
        r0.mMarqueeFadeMode = r5;
    L_0x004a:
        r4 = android.text.TextUtils.TruncateAt.MARQUEE;
        r0.setEllipsize(r4);
    L_0x004f:
        r4 = r0.mFilters;
        r4 = r4.length;
        r6 = 0;
    L_0x0053:
        if (r6 >= r4) goto L_0x006d;
    L_0x0055:
        r7 = r0.mFilters;
        r7 = r7[r6];
        r9 = 0;
        r10 = r3.length();
        r11 = EMPTY_SPANNED;
        r12 = 0;
        r13 = 0;
        r8 = r3;
        r7 = r7.filter(r8, r9, r10, r11, r12, r13);
        if (r7 == 0) goto L_0x006a;
    L_0x0069:
        r3 = r7;
    L_0x006a:
        r6 = r6 + 1;
        goto L_0x0053;
    L_0x006d:
        r6 = "";
        if (r19 == 0) goto L_0x008a;
    L_0x0071:
        r7 = r0.mText;
        if (r7 == 0) goto L_0x0083;
    L_0x0075:
        r7 = r7.length();
        r8 = r0.mText;
        r9 = r3.length();
        r0.sendBeforeTextChanged(r8, r2, r7, r9);
        goto L_0x008c;
    L_0x0083:
        r7 = r3.length();
        r0.sendBeforeTextChanged(r6, r2, r2, r7);
    L_0x008a:
        r7 = r20;
    L_0x008c:
        r8 = 0;
        r9 = r0.mListeners;
        if (r9 == 0) goto L_0x0098;
    L_0x0091:
        r9 = r9.size();
        if (r9 == 0) goto L_0x0098;
    L_0x0097:
        r8 = 1;
    L_0x0098:
        r9 = r3 instanceof android.text.PrecomputedText;
        r10 = 0;
        if (r9 == 0) goto L_0x00a1;
    L_0x009d:
        r9 = r3;
        r9 = (android.text.PrecomputedText) r9;
        goto L_0x00a2;
    L_0x00a1:
        r9 = r10;
    L_0x00a2:
        r11 = android.widget.TextView.BufferType.EDITABLE;
        if (r1 == r11) goto L_0x011e;
    L_0x00a6:
        r11 = r16.getKeyListener();
        if (r11 != 0) goto L_0x011e;
    L_0x00ac:
        if (r8 == 0) goto L_0x00b0;
    L_0x00ae:
        goto L_0x011e;
    L_0x00b0:
        if (r9 == 0) goto L_0x0105;
    L_0x00b2:
        r11 = r0.mTextDir;
        if (r11 != 0) goto L_0x00bc;
    L_0x00b6:
        r11 = r16.getTextDirectionHeuristic();
        r0.mTextDir = r11;
        r11 = r9.getParams();
        r12 = r16.getPaint();
        r13 = r0.mTextDir;
        r14 = r0.mBreakStrategy;
        r15 = r0.mHyphenationFrequency;
        r11 = r11.checkResultUsable(r12, r13, r14, r15);
        if (r11 == 0) goto L_0x00de;
    L_0x00d1:
        if (r11 == r5) goto L_0x00d4;
    L_0x00d3:
        goto L_0x00dd;
    L_0x00d4:
        r5 = r16.getTextMetricsParams();
        r5 = android.text.PrecomputedText.create(r9, r5);
        r9 = r5;
    L_0x00dd:
        goto L_0x013c;
    L_0x00de:
        r2 = new java.lang.IllegalArgumentException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "PrecomputedText's Parameters don't match the parameters of this TextView.Consider using setTextMetricsParams(precomputedText.getParams()) to override the settings of this TextView: PrecomputedText: ";
        r5.append(r6);
        r6 = r9.getParams();
        r5.append(r6);
        r6 = "TextView: ";
        r5.append(r6);
        r6 = r16.getTextMetricsParams();
        r5.append(r6);
        r5 = r5.toString();
        r2.<init>(r5);
        throw r2;
    L_0x0105:
        r5 = android.widget.TextView.BufferType.SPANNABLE;
        if (r1 == r5) goto L_0x0117;
    L_0x0109:
        r5 = r0.mMovement;
        if (r5 == 0) goto L_0x010e;
    L_0x010d:
        goto L_0x0117;
    L_0x010e:
        r5 = r3 instanceof android.widget.TextView.CharWrapper;
        if (r5 != 0) goto L_0x013c;
    L_0x0112:
        r3 = android.text.TextUtils.stringOrSpannedString(r3);
        goto L_0x013c;
    L_0x0117:
        r5 = r0.mSpannableFactory;
        r3 = r5.newSpannable(r3);
        goto L_0x013c;
    L_0x011e:
        r16.createEditorIfNeeded();
        r5 = r0.mEditor;
        r5.forgetUndoRedo();
        r5 = r0.mEditableFactory;
        r5 = r5.newEditable(r3);
        r3 = r5;
        r11 = r0.mFilters;
        r0.setFilters(r5, r11);
        r11 = r16.getInputMethodManager();
        if (r11 == 0) goto L_0x013b;
    L_0x0138:
        r11.restartInput(r0);
    L_0x013c:
        r5 = r0.mAutoLinkMask;
        if (r5 == 0) goto L_0x017a;
    L_0x0140:
        r5 = android.widget.TextView.BufferType.EDITABLE;
        if (r1 == r5) goto L_0x0150;
    L_0x0144:
        r5 = r3 instanceof android.text.Spannable;
        if (r5 == 0) goto L_0x0149;
    L_0x0148:
        goto L_0x0150;
    L_0x0149:
        r5 = r0.mSpannableFactory;
        r5 = r5.newSpannable(r3);
        goto L_0x0153;
    L_0x0150:
        r5 = r3;
        r5 = (android.text.Spannable) r5;
    L_0x0153:
        r11 = r0.mAutoLinkMask;
        r11 = android.text.util.Linkify.addLinks(r5, r11);
        if (r11 == 0) goto L_0x017a;
    L_0x015b:
        r3 = r5;
        r11 = android.widget.TextView.BufferType.EDITABLE;
        if (r1 != r11) goto L_0x0163;
    L_0x0160:
        r11 = android.widget.TextView.BufferType.EDITABLE;
        goto L_0x0165;
    L_0x0163:
        r11 = android.widget.TextView.BufferType.SPANNABLE;
    L_0x0165:
        r1 = r11;
        r0.setTextInternal(r3);
        r11 = r0.mLinksClickable;
        if (r11 == 0) goto L_0x017a;
    L_0x016d:
        r11 = r16.textCanBeSelected();
        if (r11 != 0) goto L_0x017a;
    L_0x0173:
        r11 = android.text.method.LinkMovementMethod.getInstance();
        r0.setMovementMethod(r11);
    L_0x017a:
        r0.mBufferType = r1;
        r0.setTextInternal(r3);
        r5 = r0.mTransformation;
        if (r5 != 0) goto L_0x0186;
    L_0x0183:
        r0.mTransformed = r3;
        goto L_0x018c;
    L_0x0186:
        r5 = r5.getTransformation(r3, r0);
        r0.mTransformed = r5;
    L_0x018c:
        r5 = r0.mTransformed;
        if (r5 != 0) goto L_0x0192;
    L_0x0190:
        r0.mTransformed = r6;
    L_0x0192:
        r5 = r3.length();
        r6 = r3 instanceof android.text.Spannable;
        if (r6 == 0) goto L_0x01ec;
    L_0x019a:
        r6 = r0.mAllowTransformationLengthChange;
        if (r6 != 0) goto L_0x01ec;
    L_0x019e:
        r6 = r3;
        r6 = (android.text.Spannable) r6;
        r11 = r6.length();
        r12 = android.widget.TextView.ChangeWatcher.class;
        r11 = r6.getSpans(r2, r11, r12);
        r11 = (android.widget.TextView.ChangeWatcher[]) r11;
        r12 = r11.length;
        r13 = 0;
    L_0x01af:
        if (r13 >= r12) goto L_0x01b9;
    L_0x01b1:
        r14 = r11[r13];
        r6.removeSpan(r14);
        r13 = r13 + 1;
        goto L_0x01af;
    L_0x01b9:
        r13 = r0.mChangeWatcher;
        if (r13 != 0) goto L_0x01c4;
    L_0x01bd:
        r13 = new android.widget.TextView$ChangeWatcher;
        r13.<init>(r0, r10);
        r0.mChangeWatcher = r13;
    L_0x01c4:
        r10 = r0.mChangeWatcher;
        r13 = 6553618; // 0x640012 float:9.183575E-39 double:3.2379175E-317;
        r6.setSpan(r10, r2, r5, r13);
        r10 = r0.mEditor;
        if (r10 == 0) goto L_0x01d3;
    L_0x01d0:
        r10.addSpanWatchers(r6);
    L_0x01d3:
        r10 = r0.mTransformation;
        if (r10 == 0) goto L_0x01dc;
    L_0x01d7:
        r13 = 18;
        r6.setSpan(r10, r2, r5, r13);
    L_0x01dc:
        r10 = r0.mMovement;
        if (r10 == 0) goto L_0x01ec;
    L_0x01e0:
        r13 = r3;
        r13 = (android.text.Spannable) r13;
        r10.initialize(r0, r13);
        r10 = r0.mEditor;
        if (r10 == 0) goto L_0x01ec;
    L_0x01ea:
        r10.mSelectionMoved = r2;
    L_0x01ec:
        r6 = r0.mLayout;
        if (r6 == 0) goto L_0x01f3;
    L_0x01f0:
        r16.checkForRelayout();
    L_0x01f3:
        r0.sendOnTextChanged(r3, r2, r7, r5);
        r0.onTextChanged(r3, r2, r7, r5);
        r2 = 2;
        r0.notifyViewAccessibilityStateChangedIfNeeded(r2);
        if (r8 == 0) goto L_0x0206;
    L_0x01ff:
        r2 = r3;
        r2 = (android.text.Editable) r2;
        r0.sendAfterTextChanged(r2);
        goto L_0x0209;
    L_0x0206:
        r16.notifyListeningManagersAfterTextChanged();
    L_0x0209:
        r2 = r0.mEditor;
        if (r2 == 0) goto L_0x0210;
    L_0x020d:
        r2.prepareCursorControllers();
    L_0x0210:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.TextView.setText(java.lang.CharSequence, android.widget.TextView$BufferType, boolean, int):void");
    }

    public final void setText(char[] text, int start, int len) {
        int oldlen = 0;
        if (start < 0 || len < 0 || start + len > text.length) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(start);
            stringBuilder.append(", ");
            stringBuilder.append(len);
            throw new IndexOutOfBoundsException(stringBuilder.toString());
        }
        CharSequence charSequence = this.mText;
        if (charSequence != null) {
            oldlen = charSequence.length();
            sendBeforeTextChanged(this.mText, 0, oldlen, len);
        } else {
            sendBeforeTextChanged("", 0, 0, len);
        }
        CharWrapper charWrapper = this.mCharWrapper;
        if (charWrapper == null) {
            this.mCharWrapper = new CharWrapper(text, start, len);
        } else {
            charWrapper.set(text, start, len);
        }
        setText(this.mCharWrapper, this.mBufferType, false, oldlen);
    }

    public final void setTextKeepState(CharSequence text, BufferType type) {
        int start = getSelectionStart();
        int end = getSelectionEnd();
        int len = text.length();
        setText(text, type);
        if (start >= 0 || end >= 0) {
            Spannable spannable = this.mSpannable;
            if (spannable != null) {
                Selection.setSelection(spannable, Math.max(0, Math.min(start, len)), Math.max(0, Math.min(end, len)));
            }
        }
    }

    @RemotableViewMethod
    public final void setText(int resid) {
        setText(getContext().getResources().getText(resid));
        this.mTextSetFromXmlOrResourceId = true;
        this.mTextId = resid;
    }

    public final void setText(int resid, BufferType type) {
        setText(getContext().getResources().getText(resid), type);
        this.mTextSetFromXmlOrResourceId = true;
        this.mTextId = resid;
    }

    @RemotableViewMethod
    public final void setHint(CharSequence hint) {
        setHintInternal(hint);
        if (this.mEditor != null && isInputMethodTarget()) {
            this.mEditor.reportExtractedText();
        }
    }

    private void setHintInternal(CharSequence hint) {
        this.mHint = TextUtils.stringOrSpannedString(hint);
        if (this.mLayout != null) {
            checkForRelayout();
        }
        if (this.mText.length() == 0) {
            invalidate();
        }
        if (this.mEditor != null && this.mText.length() == 0 && this.mHint != null) {
            this.mEditor.invalidateTextDisplayList();
        }
    }

    @RemotableViewMethod
    public final void setHint(int resid) {
        setHint(getContext().getResources().getText(resid));
    }

    @CapturedViewProperty
    public CharSequence getHint() {
        return this.mHint;
    }

    public boolean isSingleLine() {
        return this.mSingleLine;
    }

    private static boolean isMultilineInputType(int type) {
        return (131087 & type) == 131073;
    }

    /* Access modifiers changed, original: 0000 */
    public CharSequence removeSuggestionSpans(CharSequence text) {
        if (text instanceof Spanned) {
            Spannable spannable;
            if (text instanceof Spannable) {
                spannable = (Spannable) text;
            } else {
                spannable = this.mSpannableFactory.newSpannable(text);
            }
            SuggestionSpan[] spans = (SuggestionSpan[]) spannable.getSpans(0, text.length(), SuggestionSpan.class);
            if (spans.length == 0) {
                return text;
            }
            text = spannable;
            for (Object removeSpan : spans) {
                spannable.removeSpan(removeSpan);
            }
        }
        return text;
    }

    public void setInputType(int type) {
        boolean wasPassword = isPasswordInputType(getInputType());
        boolean wasVisiblePassword = isVisiblePasswordInputType(getInputType());
        setInputType(type, false);
        boolean isPassword = isPasswordInputType(type);
        boolean isVisiblePassword = isVisiblePasswordInputType(type);
        boolean forceUpdate = false;
        if (isPassword) {
            setTransformationMethod(PasswordTransformationMethod.getInstance());
            setTypefaceFromAttrs(null, null, 3, 0, -1);
        } else if (isVisiblePassword) {
            if (this.mTransformation == PasswordTransformationMethod.getInstance()) {
                forceUpdate = true;
            }
            setTypefaceFromAttrs(null, null, 3, 0, -1);
        } else if (wasPassword || wasVisiblePassword) {
            setTypefaceFromAttrs(null, null, -1, 0, -1);
            if (this.mTransformation == PasswordTransformationMethod.getInstance()) {
                forceUpdate = true;
            }
        }
        boolean singleLine = isMultilineInputType(type) ^ true;
        if (this.mSingleLine != singleLine || forceUpdate) {
            applySingleLine(singleLine, isPassword ^ 1, true);
        }
        if (!isSuggestionsEnabled()) {
            setTextInternal(removeSuggestionSpans(this.mText));
        }
        InputMethodManager imm = getInputMethodManager();
        if (imm != null) {
            imm.restartInput(this);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean hasPasswordTransformationMethod() {
        return this.mTransformation instanceof PasswordTransformationMethod;
    }

    static boolean isPasswordInputType(int inputType) {
        int variation = inputType & 4095;
        return variation == 129 || variation == 225 || variation == 18;
    }

    private static boolean isVisiblePasswordInputType(int inputType) {
        return (inputType & 4095) == 145;
    }

    public void setRawInputType(int type) {
        if (type != 0 || this.mEditor != null) {
            createEditorIfNeeded();
            this.mEditor.mInputType = type;
        }
    }

    private Locale getCustomLocaleForKeyListenerOrNull() {
        if (!this.mUseInternationalizedInput) {
            return null;
        }
        LocaleList locales = getImeHintLocales();
        if (locales == null) {
            return null;
        }
        return locales.get(0);
    }

    @UnsupportedAppUsage
    private void setInputType(int type, boolean direct) {
        KeyListener input;
        int cls = type & 15;
        boolean input2 = true;
        if (cls == 1) {
            Capitalize cap;
            if ((32768 & type) == 0) {
                input = null;
            }
            if ((type & 4096) != 0) {
                cap = Capitalize.CHARACTERS;
            } else if ((type & 8192) != 0) {
                cap = Capitalize.WORDS;
            } else if ((type & 16384) != 0) {
                cap = Capitalize.SENTENCES;
            } else {
                cap = Capitalize.NONE;
            }
            input = TextKeyListener.getInstance(input, cap);
        } else if (cls == 2) {
            Locale locale = getCustomLocaleForKeyListenerOrNull();
            boolean z = (type & 4096) != 0;
            if ((type & 8192) == 0) {
                input2 = false;
            }
            input = DigitsKeyListener.getInstance(locale, z, input2);
            if (locale != null) {
                int newType = input.getInputType();
                if ((newType & 15) != 2) {
                    if ((type & 16) != 0) {
                        newType |= 128;
                    }
                    type = newType;
                }
            }
        } else if (cls == 4) {
            KeyListener input3;
            Locale locale2 = getCustomLocaleForKeyListenerOrNull();
            int i = type & InputType.TYPE_MASK_VARIATION;
            if (i == 16) {
                input3 = DateKeyListener.getInstance(locale2);
            } else if (i != 32) {
                input3 = DateTimeKeyListener.getInstance(locale2);
            } else {
                input3 = TimeKeyListener.getInstance(locale2);
            }
            if (this.mUseInternationalizedInput) {
                type = input3.getInputType();
            }
            input = input3;
        } else {
            input = cls == 3 ? DialerKeyListener.getInstance() : TextKeyListener.getInstance();
        }
        setRawInputType(type);
        this.mListenerChanged = false;
        if (direct) {
            createEditorIfNeeded();
            this.mEditor.mKeyListener = input;
            return;
        }
        setKeyListenerOnly(input);
    }

    public int getInputType() {
        Editor editor = this.mEditor;
        return editor == null ? 0 : editor.mInputType;
    }

    public void setImeOptions(int imeOptions) {
        createEditorIfNeeded();
        this.mEditor.createInputContentTypeIfNeeded();
        this.mEditor.mInputContentType.imeOptions = imeOptions;
    }

    public int getImeOptions() {
        Editor editor = this.mEditor;
        return (editor == null || editor.mInputContentType == null) ? 0 : this.mEditor.mInputContentType.imeOptions;
    }

    public void setImeActionLabel(CharSequence label, int actionId) {
        createEditorIfNeeded();
        this.mEditor.createInputContentTypeIfNeeded();
        this.mEditor.mInputContentType.imeActionLabel = label;
        this.mEditor.mInputContentType.imeActionId = actionId;
    }

    public CharSequence getImeActionLabel() {
        Editor editor = this.mEditor;
        return (editor == null || editor.mInputContentType == null) ? null : this.mEditor.mInputContentType.imeActionLabel;
    }

    public int getImeActionId() {
        Editor editor = this.mEditor;
        return (editor == null || editor.mInputContentType == null) ? 0 : this.mEditor.mInputContentType.imeActionId;
    }

    public void setOnEditorActionListener(OnEditorActionListener l) {
        createEditorIfNeeded();
        this.mEditor.createInputContentTypeIfNeeded();
        this.mEditor.mInputContentType.onEditorActionListener = l;
    }

    public void onEditorAction(int actionCode) {
        int i = actionCode;
        Editor editor = this.mEditor;
        InputContentType ict = editor == null ? null : editor.mInputContentType;
        if (ict != null) {
            if (ict.onEditorActionListener == null || !ict.onEditorActionListener.onEditorAction(this, i, null)) {
                String str = "focus search returned a view that wasn't able to take focus!";
                View v;
                if (i == 5) {
                    v = focusSearch(2);
                    if (v != null && !v.requestFocus(2)) {
                        throw new IllegalStateException(str);
                    }
                    return;
                } else if (i == 7) {
                    v = focusSearch(1);
                    if (v != null && !v.requestFocus(1)) {
                        throw new IllegalStateException(str);
                    }
                    return;
                } else if (i == 6) {
                    InputMethodManager imm = getInputMethodManager();
                    if (imm != null && imm.isActive(this)) {
                        imm.hideSoftInputFromWindow(getWindowToken(), 0);
                    }
                    return;
                }
            }
            return;
        }
        ViewRootImpl viewRootImpl = getViewRootImpl();
        if (viewRootImpl != null) {
            long eventTime = SystemClock.uptimeMillis();
            long j = eventTime;
            KeyEvent keyEvent = r4;
            KeyEvent keyEvent2 = new KeyEvent(eventTime, j, 0, 66, 0, 0, -1, 0, 22);
            viewRootImpl.dispatchKeyFromIme(keyEvent);
            viewRootImpl.dispatchKeyFromIme(new KeyEvent(SystemClock.uptimeMillis(), j, 1, 66, 0, 0, -1, 0, 22));
        }
    }

    public void setPrivateImeOptions(String type) {
        createEditorIfNeeded();
        this.mEditor.createInputContentTypeIfNeeded();
        this.mEditor.mInputContentType.privateImeOptions = type;
    }

    public String getPrivateImeOptions() {
        Editor editor = this.mEditor;
        return (editor == null || editor.mInputContentType == null) ? null : this.mEditor.mInputContentType.privateImeOptions;
    }

    public void setInputExtras(int xmlResId) throws XmlPullParserException, IOException {
        createEditorIfNeeded();
        XmlResourceParser parser = getResources().getXml(xmlResId);
        this.mEditor.createInputContentTypeIfNeeded();
        this.mEditor.mInputContentType.extras = new Bundle();
        getResources().parseBundleExtras(parser, this.mEditor.mInputContentType.extras);
    }

    public Bundle getInputExtras(boolean create) {
        if (this.mEditor == null && !create) {
            return null;
        }
        createEditorIfNeeded();
        if (this.mEditor.mInputContentType == null) {
            if (!create) {
                return null;
            }
            this.mEditor.createInputContentTypeIfNeeded();
        }
        if (this.mEditor.mInputContentType.extras == null) {
            if (!create) {
                return null;
            }
            this.mEditor.mInputContentType.extras = new Bundle();
        }
        return this.mEditor.mInputContentType.extras;
    }

    public void setImeHintLocales(LocaleList hintLocales) {
        createEditorIfNeeded();
        this.mEditor.createInputContentTypeIfNeeded();
        this.mEditor.mInputContentType.imeHintLocales = hintLocales;
        if (this.mUseInternationalizedInput) {
            changeListenerLocaleTo(hintLocales == null ? null : hintLocales.get(0));
        }
    }

    public LocaleList getImeHintLocales() {
        Editor editor = this.mEditor;
        if (editor == null || editor.mInputContentType == null) {
            return null;
        }
        return this.mEditor.mInputContentType.imeHintLocales;
    }

    public CharSequence getError() {
        Editor editor = this.mEditor;
        return editor == null ? null : editor.mError;
    }

    @RemotableViewMethod
    public void setError(CharSequence error) {
        if (error == null) {
            setError(null, null);
            return;
        }
        Drawable dr = getContext().getDrawable(com.android.internal.R.drawable.indicator_input_error);
        dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
        setError(error, dr);
    }

    public void setError(CharSequence error, Drawable icon) {
        createEditorIfNeeded();
        this.mEditor.setError(error, icon);
        notifyViewAccessibilityStateChangedIfNeeded(0);
    }

    /* Access modifiers changed, original: protected */
    public boolean setFrame(int l, int t, int r, int b) {
        boolean result = super.setFrame(l, t, r, b);
        Editor editor = this.mEditor;
        if (editor != null) {
            editor.setFrame();
        }
        restartMarqueeIfNeeded();
        return result;
    }

    private void restartMarqueeIfNeeded() {
        if (this.mRestartMarquee && this.mEllipsize == TruncateAt.MARQUEE) {
            this.mRestartMarquee = false;
            startMarquee();
        }
    }

    public void setFilters(InputFilter[] filters) {
        if (filters != null) {
            this.mFilters = filters;
            CharSequence charSequence = this.mText;
            if (charSequence instanceof Editable) {
                setFilters((Editable) charSequence, filters);
                return;
            }
            return;
        }
        throw new IllegalArgumentException();
    }

    private void setFilters(Editable e, InputFilter[] filters) {
        Editor editor = this.mEditor;
        if (editor != null) {
            boolean undoFilter = editor.mUndoInputFilter != null;
            boolean keyFilter = this.mEditor.mKeyListener instanceof InputFilter;
            int num = 0;
            if (undoFilter) {
                num = 0 + 1;
            }
            if (keyFilter) {
                num++;
            }
            if (num > 0) {
                InputFilter[] nf = new InputFilter[(filters.length + num)];
                System.arraycopy(filters, 0, nf, 0, filters.length);
                int num2 = 0;
                if (undoFilter) {
                    nf[filters.length] = this.mEditor.mUndoInputFilter;
                    num2 = 0 + 1;
                }
                if (keyFilter) {
                    nf[filters.length + num2] = (InputFilter) this.mEditor.mKeyListener;
                }
                e.setFilters(nf);
                return;
            }
        }
        e.setFilters(filters);
    }

    public InputFilter[] getFilters() {
        return this.mFilters;
    }

    private int getBoxHeight(Layout l) {
        int padding;
        Insets opticalInsets = View.isLayoutModeOptical(this.mParent) ? getOpticalInsets() : Insets.NONE;
        if (l == this.mHintLayout) {
            padding = getCompoundPaddingTop() + getCompoundPaddingBottom();
        } else {
            padding = getExtendedPaddingTop() + getExtendedPaddingBottom();
        }
        return ((getMeasuredHeight() - padding) + opticalInsets.top) + opticalInsets.bottom;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public int getVerticalOffset(boolean forceNormal) {
        int gravity = this.mGravity & 112;
        Layout l = this.mLayout;
        if (!(forceNormal || this.mText.length() != 0 || this.mHintLayout == null)) {
            l = this.mHintLayout;
        }
        if (gravity == 48) {
            return 0;
        }
        int boxht = getBoxHeight(l);
        int textht = l.getHeight();
        if (textht >= boxht) {
            return 0;
        }
        if (gravity == 80) {
            return boxht - textht;
        }
        return (boxht - textht) >> 1;
    }

    private int getBottomVerticalOffset(boolean forceNormal) {
        int gravity = this.mGravity & 112;
        Layout l = this.mLayout;
        if (!(forceNormal || this.mText.length() != 0 || this.mHintLayout == null)) {
            l = this.mHintLayout;
        }
        if (gravity == 80) {
            return 0;
        }
        int boxht = getBoxHeight(l);
        int textht = l.getHeight();
        if (textht >= boxht) {
            return 0;
        }
        if (gravity == 48) {
            return boxht - textht;
        }
        return (boxht - textht) >> 1;
    }

    /* Access modifiers changed, original: 0000 */
    public void invalidateCursorPath() {
        if (this.mHighlightPathBogus) {
            invalidateCursor();
            return;
        }
        int horizontalPadding = getCompoundPaddingLeft();
        int verticalPadding = getExtendedPaddingTop() + getVerticalOffset(true);
        if (this.mEditor.mDrawableForCursor == null) {
            synchronized (TEMP_RECTF) {
                float thick = (float) Math.ceil((double) this.mTextPaint.getStrokeWidth());
                if (thick < 1.0f) {
                    thick = 1.0f;
                }
                thick /= 2.0f;
                this.mHighlightPath.computeBounds(TEMP_RECTF, false);
                invalidate((int) Math.floor((double) ((((float) horizontalPadding) + TEMP_RECTF.left) - thick)), (int) Math.floor((double) ((((float) verticalPadding) + TEMP_RECTF.top) - thick)), (int) Math.ceil((double) ((((float) horizontalPadding) + TEMP_RECTF.right) + thick)), (int) Math.ceil((double) ((((float) verticalPadding) + TEMP_RECTF.bottom) + thick)));
            }
            return;
        }
        Rect bounds = this.mEditor.mDrawableForCursor.getBounds();
        invalidate(bounds.left + horizontalPadding, bounds.top + verticalPadding, bounds.right + horizontalPadding, bounds.bottom + verticalPadding);
    }

    /* Access modifiers changed, original: 0000 */
    public void invalidateCursor() {
        int where = getSelectionEnd();
        invalidateCursor(where, where, where);
    }

    private void invalidateCursor(int a, int b, int c) {
        if (a >= 0 || b >= 0 || c >= 0) {
            invalidateRegion(Math.min(Math.min(a, b), c), Math.max(Math.max(a, b), c), true);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void invalidateRegion(int start, int end, boolean invalidateCursor) {
        int lineStart = this.mLayout;
        if (lineStart == 0) {
            invalidate();
            return;
        }
        int lineEnd;
        int left;
        int right;
        lineStart = lineStart.getLineForOffset(start);
        int top = this.mLayout.getLineTop(lineStart);
        if (lineStart > 0) {
            top -= this.mLayout.getLineDescent(lineStart - 1);
        }
        if (start == end) {
            lineEnd = lineStart;
        } else {
            lineEnd = this.mLayout.getLineForOffset(end);
        }
        int bottom = this.mLayout.getLineBottom(lineEnd);
        if (invalidateCursor) {
            Editor editor = this.mEditor;
            if (!(editor == null || editor.mDrawableForCursor == null)) {
                Rect bounds = this.mEditor.mDrawableForCursor.getBounds();
                top = Math.min(top, bounds.top);
                bottom = Math.max(bottom, bounds.bottom);
            }
        }
        int compoundPaddingLeft = getCompoundPaddingLeft();
        int verticalPadding = getExtendedPaddingTop() + getVerticalOffset(true);
        if (lineStart != lineEnd || invalidateCursor) {
            left = compoundPaddingLeft;
            right = getWidth() - getCompoundPaddingRight();
        } else {
            left = ((int) this.mLayout.getPrimaryHorizontal(start)) + compoundPaddingLeft;
            right = ((int) (((double) this.mLayout.getPrimaryHorizontal(end)) + 1.0d)) + compoundPaddingLeft;
        }
        invalidate(this.mScrollX + left, verticalPadding + top, this.mScrollX + right, verticalPadding + bottom);
    }

    private void registerForPreDraw() {
        if (!this.mPreDrawRegistered) {
            getViewTreeObserver().addOnPreDrawListener(this);
            this.mPreDrawRegistered = true;
        }
    }

    private void unregisterForPreDraw() {
        getViewTreeObserver().removeOnPreDrawListener(this);
        this.mPreDrawRegistered = false;
        this.mPreDrawListenerDetached = false;
    }

    public boolean onPreDraw() {
        if (this.mLayout == null) {
            assumeLayout();
        }
        if (this.mMovement != null) {
            int curs = getSelectionEnd();
            Editor editor = this.mEditor;
            if (!(editor == null || editor.mSelectionModifierCursorController == null || !this.mEditor.mSelectionModifierCursorController.isSelectionStartDragged())) {
                curs = getSelectionStart();
            }
            if (curs < 0 && (this.mGravity & 112) == 80) {
                curs = this.mText.length();
            }
            if (curs >= 0) {
                bringPointIntoView(curs);
            }
        } else {
            bringTextIntoView();
        }
        Editor editor2 = this.mEditor;
        if (editor2 != null && editor2.mCreatedWithASelection) {
            this.mEditor.refreshTextActionMode();
            this.mEditor.mCreatedWithASelection = false;
        }
        unregisterForPreDraw();
        return true;
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Editor editor = this.mEditor;
        if (editor != null) {
            editor.onAttachedToWindow();
        }
        if (this.mPreDrawListenerDetached) {
            getViewTreeObserver().addOnPreDrawListener(this);
            this.mPreDrawListenerDetached = false;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindowInternal() {
        if (this.mPreDrawRegistered) {
            getViewTreeObserver().removeOnPreDrawListener(this);
            this.mPreDrawListenerDetached = true;
        }
        resetResolvedDrawables();
        Editor editor = this.mEditor;
        if (editor != null) {
            editor.onDetachedFromWindow();
        }
        super.onDetachedFromWindowInternal();
    }

    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
        Editor editor = this.mEditor;
        if (editor != null) {
            editor.onScreenStateChanged(screenState);
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean isPaddingOffsetRequired() {
        return (this.mShadowRadius == 0.0f && this.mDrawables == null) ? false : true;
    }

    /* Access modifiers changed, original: protected */
    public int getLeftPaddingOffset() {
        return (getCompoundPaddingLeft() - this.mPaddingLeft) + ((int) Math.min(0.0f, this.mShadowDx - this.mShadowRadius));
    }

    /* Access modifiers changed, original: protected */
    public int getTopPaddingOffset() {
        return (int) Math.min(0.0f, this.mShadowDy - this.mShadowRadius);
    }

    /* Access modifiers changed, original: protected */
    public int getBottomPaddingOffset() {
        return (int) Math.max(0.0f, this.mShadowDy + this.mShadowRadius);
    }

    /* Access modifiers changed, original: protected */
    public int getRightPaddingOffset() {
        return (-(getCompoundPaddingRight() - this.mPaddingRight)) + ((int) Math.max(0.0f, this.mShadowDx + this.mShadowRadius));
    }

    /* Access modifiers changed, original: protected */
    public boolean verifyDrawable(Drawable who) {
        boolean verified = super.verifyDrawable(who);
        if (!verified) {
            Drawables drawables = this.mDrawables;
            if (drawables != null) {
                for (Drawable dr : drawables.mShowing) {
                    if (who == dr) {
                        return true;
                    }
                }
            }
        }
        return verified;
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawables drawables = this.mDrawables;
        if (drawables != null) {
            for (Drawable dr : drawables.mShowing) {
                if (dr != null) {
                    dr.jumpToCurrentState();
                }
            }
        }
    }

    public void invalidateDrawable(Drawable drawable) {
        boolean handled = false;
        if (verifyDrawable(drawable)) {
            Rect dirty = drawable.getBounds();
            int scrollX = this.mScrollX;
            int scrollY = this.mScrollY;
            Drawables drawables = this.mDrawables;
            if (drawables != null) {
                int compoundPaddingTop;
                if (drawable == drawables.mShowing[0]) {
                    compoundPaddingTop = getCompoundPaddingTop();
                    scrollX += this.mPaddingLeft;
                    scrollY += (((((this.mBottom - this.mTop) - getCompoundPaddingBottom()) - compoundPaddingTop) - drawables.mDrawableHeightLeft) / 2) + compoundPaddingTop;
                    handled = true;
                } else if (drawable == drawables.mShowing[2]) {
                    compoundPaddingTop = getCompoundPaddingTop();
                    scrollX += ((this.mRight - this.mLeft) - this.mPaddingRight) - drawables.mDrawableSizeRight;
                    scrollY += (((((this.mBottom - this.mTop) - getCompoundPaddingBottom()) - compoundPaddingTop) - drawables.mDrawableHeightRight) / 2) + compoundPaddingTop;
                    handled = true;
                } else if (drawable == drawables.mShowing[1]) {
                    compoundPaddingTop = getCompoundPaddingLeft();
                    scrollX += (((((this.mRight - this.mLeft) - getCompoundPaddingRight()) - compoundPaddingTop) - drawables.mDrawableWidthTop) / 2) + compoundPaddingTop;
                    scrollY += this.mPaddingTop;
                    handled = true;
                } else if (drawable == drawables.mShowing[3]) {
                    compoundPaddingTop = getCompoundPaddingLeft();
                    scrollX += (((((this.mRight - this.mLeft) - getCompoundPaddingRight()) - compoundPaddingTop) - drawables.mDrawableWidthBottom) / 2) + compoundPaddingTop;
                    scrollY += ((this.mBottom - this.mTop) - this.mPaddingBottom) - drawables.mDrawableSizeBottom;
                    handled = true;
                }
            }
            if (handled) {
                invalidate(dirty.left + scrollX, dirty.top + scrollY, dirty.right + scrollX, dirty.bottom + scrollY);
            }
        }
        if (!handled) {
            super.invalidateDrawable(drawable);
        }
    }

    public boolean hasOverlappingRendering() {
        return ((getBackground() == null || getBackground().getCurrent() == null) && this.mSpannable == null && !hasSelection() && !isHorizontalFadingEdgeEnabled() && this.mShadowColor == 0) ? false : true;
    }

    public boolean isTextSelectable() {
        Editor editor = this.mEditor;
        return editor == null ? false : editor.mTextIsSelectable;
    }

    public void setTextIsSelectable(boolean selectable) {
        if (selectable || this.mEditor != null) {
            createEditorIfNeeded();
            if (this.mEditor.mTextIsSelectable != selectable) {
                this.mEditor.mTextIsSelectable = selectable;
                setFocusableInTouchMode(selectable);
                setFocusable(16);
                setClickable(selectable);
                setLongClickable(selectable);
                setMovementMethod(selectable ? ArrowKeyMovementMethod.getInstance() : null);
                setText(this.mText, selectable ? BufferType.SPANNABLE : BufferType.NORMAL);
                this.mEditor.prepareCursorControllers();
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState;
        if (this.mSingleLine) {
            drawableState = super.onCreateDrawableState(extraSpace);
        } else {
            drawableState = super.onCreateDrawableState(extraSpace + 1);
            View.mergeDrawableStates(drawableState, MULTILINE_STATE_SET);
        }
        if (isTextSelectable()) {
            int length = drawableState.length;
            for (int i = 0; i < length; i++) {
                if (drawableState[i] == 16842919) {
                    int[] nonPressedState = new int[(length - 1)];
                    System.arraycopy(drawableState, 0, nonPressedState, 0, i);
                    System.arraycopy(drawableState, i + 1, nonPressedState, i, (length - i) - 1);
                    return nonPressedState;
                }
            }
        }
        return drawableState;
    }

    @UnsupportedAppUsage
    private Path getUpdatedHighlightPath() {
        Paint highlightPaint = this.mHighlightPaint;
        int selStart = getSelectionStart();
        int selEnd = getSelectionEnd();
        if (this.mMovement == null) {
            return null;
        }
        if ((!isFocused() && !isPressed()) || selStart < 0) {
            return null;
        }
        if (selStart == selEnd) {
            Editor editor = this.mEditor;
            if (editor == null || !editor.shouldRenderCursor()) {
                return null;
            }
            if (this.mHighlightPathBogus) {
                if (this.mHighlightPath == null) {
                    this.mHighlightPath = new Path();
                }
                this.mHighlightPath.reset();
                this.mLayout.getCursorPath(selStart, this.mHighlightPath, this.mText);
                this.mEditor.updateCursorPosition();
                this.mHighlightPathBogus = false;
            }
            highlightPaint.setColor(this.mCurTextColor);
            highlightPaint.setStyle(Style.STROKE);
            return this.mHighlightPath;
        }
        if (this.mHighlightPathBogus) {
            if (this.mHighlightPath == null) {
                this.mHighlightPath = new Path();
            }
            this.mHighlightPath.reset();
            this.mLayout.getSelectionPath(selStart, selEnd, this.mHighlightPath);
            this.mHighlightPathBogus = false;
        }
        highlightPaint.setColor(this.mHighlightColor);
        highlightPaint.setStyle(Style.FILL);
        return this.mHighlightPath;
    }

    public int getHorizontalOffsetForDrawables() {
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        int hspace;
        int color;
        float clipRight;
        float clipTop;
        int voffsetCursor;
        int layoutDirection;
        Marquee marquee;
        int cursorOffsetVertical;
        Layout layout;
        Canvas canvas2 = canvas;
        restartMarqueeIfNeeded();
        super.onDraw(canvas);
        int compoundPaddingLeft = getCompoundPaddingLeft();
        int compoundPaddingTop = getCompoundPaddingTop();
        int compoundPaddingRight = getCompoundPaddingRight();
        int compoundPaddingBottom = getCompoundPaddingBottom();
        int scrollX = this.mScrollX;
        int scrollY = this.mScrollY;
        int right = this.mRight;
        int left = this.mLeft;
        int bottom = this.mBottom;
        int top = this.mTop;
        boolean isLayoutRtl = isLayoutRtl();
        int offset = getHorizontalOffsetForDrawables();
        int leftOffset = isLayoutRtl ? 0 : offset;
        int rightOffset = isLayoutRtl ? offset : 0;
        Drawables dr = this.mDrawables;
        if (dr != null) {
            int vspace = ((bottom - top) - compoundPaddingBottom) - compoundPaddingTop;
            hspace = ((right - left) - compoundPaddingRight) - compoundPaddingLeft;
            if (dr.mShowing[0] != null) {
                canvas.save();
                canvas2.translate((float) ((this.mPaddingLeft + scrollX) + leftOffset), (float) ((scrollY + compoundPaddingTop) + ((vspace - dr.mDrawableHeightLeft) / 2)));
                dr.mShowing[0].draw(canvas2);
                canvas.restore();
            }
            if (dr.mShowing[2] != null) {
                canvas.save();
                canvas2.translate((float) (((((scrollX + right) - left) - this.mPaddingRight) - dr.mDrawableSizeRight) - rightOffset), (float) ((scrollY + compoundPaddingTop) + ((vspace - dr.mDrawableHeightRight) / 2)));
                dr.mShowing[2].draw(canvas2);
                canvas.restore();
            }
            if (dr.mShowing[1] != null) {
                canvas.save();
                canvas2.translate((float) ((scrollX + compoundPaddingLeft) + ((hspace - dr.mDrawableWidthTop) / 2)), (float) (this.mPaddingTop + scrollY));
                dr.mShowing[1].draw(canvas2);
                canvas.restore();
            }
            if (dr.mShowing[3] != null) {
                canvas.save();
                canvas2.translate((float) ((scrollX + compoundPaddingLeft) + ((hspace - dr.mDrawableWidthBottom) / 2)), (float) ((((scrollY + bottom) - top) - this.mPaddingBottom) - dr.mDrawableSizeBottom));
                dr.mShowing[3].draw(canvas2);
                canvas.restore();
            }
        }
        int color2 = this.mCurTextColor;
        if (this.mLayout == null) {
            assumeLayout();
        }
        Layout layout2 = this.mLayout;
        if (this.mHint == null || this.mText.length() != 0) {
            color = color2;
        } else {
            if (this.mHintTextColor != null) {
                color2 = this.mCurHintTextColor;
            }
            layout2 = this.mHintLayout;
            color = color2;
        }
        this.mTextPaint.setColor(color);
        this.mTextPaint.drawableState = getDrawableState();
        canvas.save();
        hspace = getExtendedPaddingTop();
        int extendedPaddingBottom = getExtendedPaddingBottom();
        int maxScrollY = this.mLayout.getHeight() - (((this.mBottom - this.mTop) - compoundPaddingBottom) - compoundPaddingTop);
        float clipLeft = (float) (compoundPaddingLeft + scrollX);
        float clipTop2 = scrollY == 0 ? 0.0f : (float) (hspace + scrollY);
        int color3 = color;
        float clipRight2 = (float) (((right - left) - getCompoundPaddingRight()) + scrollX);
        float clipBottom = (float) (((bottom - top) + scrollY) - (scrollY == maxScrollY ? 0 : extendedPaddingBottom));
        int top2 = top;
        float f = this.mShadowRadius;
        if (f != 0.0f) {
            clipLeft += Math.min(0.0f, this.mShadowDx - f);
            f = clipBottom + Math.max(0.0f, this.mShadowDy + this.mShadowRadius);
            clipRight = clipRight2 + Math.max(0.0f, this.mShadowDx + this.mShadowRadius);
            clipTop = clipTop2 + Math.min(0.0f, this.mShadowDy - this.mShadowRadius);
            clipTop2 = clipLeft;
        } else {
            f = clipBottom;
            clipRight = clipRight2;
            clipTop = clipTop2;
            clipTop2 = clipLeft;
        }
        canvas2.clipRect(clipTop2, clipTop, clipRight, f);
        int voffsetText = 0;
        if ((this.mGravity & 112) != 48) {
            color = getVerticalOffset(false);
            voffsetCursor = getVerticalOffset(true);
            voffsetText = color;
        } else {
            voffsetCursor = 0;
        }
        canvas2.translate((float) compoundPaddingLeft, (float) (hspace + voffsetText));
        color = getLayoutDirection();
        int absoluteGravity = Gravity.getAbsoluteGravity(this.mGravity, color);
        float f2;
        if (isMarqueeFadeEnabled()) {
            if (this.mSingleLine || getLineCount() != 1 || !canMarquee() || (absoluteGravity & 7) == 3) {
                layoutDirection = color;
                f2 = f;
            } else {
                color2 = this.mRight - this.mLeft;
                layoutDirection = color;
                canvas2.translate(((float) layout2.getParagraphDirection(0)) * (this.mLayout.getLineRight(0.0f) - ((float) (color2 - (getCompoundPaddingLeft() + getCompoundPaddingRight())))), 0.0f);
            }
            marquee = this.mMarquee;
            if (marquee == null || !marquee.isRunning()) {
                color = 0;
            } else {
                color = 0;
                canvas2.translate(((float) layout2.getParagraphDirection(0)) * (-this.mMarquee.getScroll()), 0.0f);
            }
        } else {
            layoutDirection = color;
            f2 = f;
            color = 0;
        }
        top = voffsetCursor - voffsetText;
        Path highlight = getUpdatedHighlightPath();
        Editor editor = this.mEditor;
        Layout layout3;
        Path highlight2;
        int i;
        int top3;
        if (editor != null) {
            layout3 = layout2;
            highlight2 = highlight;
            compoundPaddingLeft = color;
            i = color3;
            color3 = layoutDirection;
            layoutDirection = i;
            i = top2;
            top2 = top;
            top3 = i;
            editor.onDraw(canvas, layout3, highlight2, this.mHighlightPaint, top2);
            cursorOffsetVertical = top2;
            layout = layout3;
            highlight = highlight2;
        } else {
            layout3 = layout2;
            highlight2 = highlight;
            float f3 = clipRight;
            int i2 = compoundPaddingLeft;
            top3 = top2;
            compoundPaddingLeft = color;
            i = color3;
            layoutDirection = i;
            cursorOffsetVertical = top;
            layout = layout3;
            layout.draw(canvas2, highlight, this.mHighlightPaint, cursorOffsetVertical);
        }
        marquee = this.mMarquee;
        if (marquee != null && marquee.shouldDrawGhost()) {
            canvas2.translate(((float) layout.getParagraphDirection(compoundPaddingLeft)) * this.mMarquee.getGhostOffset(), 0.0f);
            layout.draw(canvas2, highlight, this.mHighlightPaint, cursorOffsetVertical);
        }
        canvas.restore();
    }

    public void getFocusedRect(Rect r) {
        if (this.mLayout == null) {
            super.getFocusedRect(r);
            return;
        }
        int selEnd = getSelectionEnd();
        if (selEnd < 0) {
            super.getFocusedRect(r);
            return;
        }
        int line;
        int lineEnd;
        int selStart = getSelectionStart();
        if (selStart < 0 || selStart >= selEnd) {
            line = this.mLayout.getLineForOffset(selEnd);
            r.top = this.mLayout.getLineTop(line);
            r.bottom = this.mLayout.getLineBottom(line);
            r.left = ((int) this.mLayout.getPrimaryHorizontal(selEnd)) - 2;
            r.right = r.left + 4;
        } else {
            line = this.mLayout.getLineForOffset(selStart);
            lineEnd = this.mLayout.getLineForOffset(selEnd);
            r.top = this.mLayout.getLineTop(line);
            r.bottom = this.mLayout.getLineBottom(lineEnd);
            if (line == lineEnd) {
                r.left = (int) this.mLayout.getPrimaryHorizontal(selStart);
                r.right = (int) this.mLayout.getPrimaryHorizontal(selEnd);
            } else {
                if (this.mHighlightPathBogus) {
                    if (this.mHighlightPath == null) {
                        this.mHighlightPath = new Path();
                    }
                    this.mHighlightPath.reset();
                    this.mLayout.getSelectionPath(selStart, selEnd, this.mHighlightPath);
                    this.mHighlightPathBogus = false;
                }
                synchronized (TEMP_RECTF) {
                    this.mHighlightPath.computeBounds(TEMP_RECTF, true);
                    r.left = ((int) TEMP_RECTF.left) - 1;
                    r.right = ((int) TEMP_RECTF.right) + 1;
                }
            }
        }
        line = getCompoundPaddingLeft();
        lineEnd = getExtendedPaddingTop();
        if ((this.mGravity & 112) != 48) {
            lineEnd += getVerticalOffset(false);
        }
        r.offset(line, lineEnd);
        r.bottom += getExtendedPaddingBottom();
    }

    public int getLineCount() {
        Layout layout = this.mLayout;
        return layout != null ? layout.getLineCount() : 0;
    }

    public int getLineBounds(int line, Rect bounds) {
        int baseline = this.mLayout;
        if (baseline == 0) {
            if (bounds != null) {
                bounds.set(0, 0, 0, 0);
            }
            return 0;
        }
        baseline = baseline.getLineBounds(line, bounds);
        int voffset = getExtendedPaddingTop();
        if ((this.mGravity & 112) != 48) {
            voffset += getVerticalOffset(true);
        }
        if (bounds != null) {
            bounds.offset(getCompoundPaddingLeft(), voffset);
        }
        return baseline + voffset;
    }

    public int getBaseline() {
        if (this.mLayout == null) {
            return super.getBaseline();
        }
        return getBaselineOffset() + this.mLayout.getLineBaseline(0);
    }

    /* Access modifiers changed, original: 0000 */
    public int getBaselineOffset() {
        int voffset = 0;
        if ((this.mGravity & 112) != 48) {
            voffset = getVerticalOffset(true);
        }
        if (View.isLayoutModeOptical(this.mParent)) {
            voffset -= getOpticalInsets().top;
        }
        return getExtendedPaddingTop() + voffset;
    }

    /* Access modifiers changed, original: protected */
    public int getFadeTop(boolean offsetRequired) {
        if (this.mLayout == null) {
            return 0;
        }
        int voffset = 0;
        if ((this.mGravity & 112) != 48) {
            voffset = getVerticalOffset(true);
        }
        if (offsetRequired) {
            voffset += getTopPaddingOffset();
        }
        return getExtendedPaddingTop() + voffset;
    }

    /* Access modifiers changed, original: protected */
    public int getFadeHeight(boolean offsetRequired) {
        Layout layout = this.mLayout;
        return layout != null ? layout.getHeight() : 0;
    }

    public PointerIcon onResolvePointerIcon(MotionEvent event, int pointerIndex) {
        if (this.mSpannable != null && this.mLinksClickable) {
            int offset = getOffsetForPosition(event.getX(pointerIndex), event.getY(pointerIndex));
            if (((ClickableSpan[]) this.mSpannable.getSpans(offset, offset, ClickableSpan.class)).length > 0) {
                return PointerIcon.getSystemIcon(this.mContext, 1002);
            }
        }
        if (isTextSelectable() || isTextEditable()) {
            return PointerIcon.getSystemIcon(this.mContext, 1008);
        }
        return super.onResolvePointerIcon(event, pointerIndex);
    }

    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == 4 && handleBackInTextActionModeIfNeeded(event)) {
            return true;
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public boolean handleBackInTextActionModeIfNeeded(KeyEvent event) {
        Editor editor = this.mEditor;
        if (editor == null || editor.getTextActionMode() == null) {
            return false;
        }
        DispatcherState state;
        if (event.getAction() == 0 && event.getRepeatCount() == 0) {
            state = getKeyDispatcherState();
            if (state != null) {
                state.startTracking(event, this);
            }
            return true;
        }
        if (event.getAction() == 1) {
            state = getKeyDispatcherState();
            if (state != null) {
                state.handleUpEvent(event);
            }
            if (event.isTracking() && !event.isCanceled()) {
                stopTextActionMode();
                return true;
            }
        }
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (doKeyDown(keyCode, event, 0) == 0) {
            return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        KeyEvent down = KeyEvent.changeAction(event, null);
        int which = doKeyDown(keyCode, down, event);
        if (which == 0) {
            return super.onKeyMultiple(keyCode, repeatCount, event);
        }
        if (which == -1) {
            return true;
        }
        repeatCount--;
        KeyEvent up = KeyEvent.changeAction(event, 1);
        if (which == 1) {
            this.mEditor.mKeyListener.onKeyUp(this, (Editable) this.mText, keyCode, up);
            while (true) {
                repeatCount--;
                if (repeatCount <= 0) {
                    break;
                }
                this.mEditor.mKeyListener.onKeyDown(this, (Editable) this.mText, keyCode, down);
                this.mEditor.mKeyListener.onKeyUp(this, (Editable) this.mText, keyCode, up);
            }
            hideErrorIfUnchanged();
        } else if (which == 2) {
            this.mMovement.onKeyUp(this, this.mSpannable, keyCode, up);
            while (true) {
                repeatCount--;
                if (repeatCount <= 0) {
                    break;
                }
                this.mMovement.onKeyDown(this, this.mSpannable, keyCode, down);
                this.mMovement.onKeyUp(this, this.mSpannable, keyCode, up);
            }
        }
        return true;
    }

    private boolean shouldAdvanceFocusOnEnter() {
        if (getKeyListener() == null) {
            return false;
        }
        if (this.mSingleLine) {
            return true;
        }
        Editor editor = this.mEditor;
        if (editor != null && (editor.mInputType & 15) == 1) {
            int variation = this.mEditor.mInputType & InputType.TYPE_MASK_VARIATION;
            if (variation == 32 || variation == 48) {
                return true;
            }
        }
        return false;
    }

    private boolean shouldAdvanceFocusOnTab() {
        if (!(getKeyListener() == null || this.mSingleLine)) {
            Editor editor = this.mEditor;
            if (editor != null && (editor.mInputType & 15) == 1) {
                int variation = this.mEditor.mInputType & InputType.TYPE_MASK_VARIATION;
                if (variation == 262144 || variation == 131072) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isDirectionalNavigationKey(int keyCode) {
        switch (keyCode) {
            case 19:
            case 20:
            case 21:
            case 22:
                return true;
            default:
                return false;
        }
    }

    private int doKeyDown(int keyCode, KeyEvent event, KeyEvent otherEvent) {
        int i = 0;
        if (!isEnabled()) {
            return 0;
        }
        Editor editor;
        boolean doDown;
        boolean handled;
        if (event.getRepeatCount() == 0 && !KeyEvent.isModifierKey(keyCode)) {
            this.mPreventDefaultMovement = false;
        }
        if (keyCode == 4) {
            editor = this.mEditor;
            if (!(editor == null || editor.getTextActionMode() == null)) {
                stopTextActionMode();
                return -1;
            }
        } else if (keyCode != 23) {
            if (keyCode != 61) {
                if (keyCode != 66) {
                    if (keyCode != 112) {
                        if (keyCode != 124) {
                            switch (keyCode) {
                                case 277:
                                    if (event.hasNoModifiers() && canCut() && onTextContextMenuItem(16908320)) {
                                        return -1;
                                    }
                                case 278:
                                    if (event.hasNoModifiers() && canCopy() && onTextContextMenuItem(16908321)) {
                                        return -1;
                                    }
                                case 279:
                                    if (event.hasNoModifiers() && canPaste() && onTextContextMenuItem(16908322)) {
                                        return -1;
                                    }
                            }
                        } else if (event.hasModifiers(4096) && canCopy()) {
                            if (onTextContextMenuItem(16908321)) {
                                return -1;
                            }
                        } else if (event.hasModifiers(1) && canPaste() && onTextContextMenuItem(16908322)) {
                            return -1;
                        }
                    } else if (event.hasModifiers(1) && canCut() && onTextContextMenuItem(16908320)) {
                        return -1;
                    }
                } else if (event.hasNoModifiers()) {
                    editor = this.mEditor;
                    if (editor != null && editor.mInputContentType != null && this.mEditor.mInputContentType.onEditorActionListener != null && this.mEditor.mInputContentType.onEditorActionListener.onEditorAction(this, 0, event)) {
                        this.mEditor.mInputContentType.enterDown = true;
                        return -1;
                    } else if ((event.getFlags() & 16) != 0 || shouldAdvanceFocusOnEnter()) {
                        if (hasOnClickListeners()) {
                            return 0;
                        }
                        return -1;
                    }
                }
            } else if ((event.hasNoModifiers() || event.hasModifiers(1)) && shouldAdvanceFocusOnTab()) {
                return 0;
            }
        } else if (event.hasNoModifiers() && shouldAdvanceFocusOnEnter()) {
            return 0;
        }
        editor = this.mEditor;
        if (!(editor == null || editor.mKeyListener == null)) {
            doDown = true;
            if (otherEvent != null) {
                try {
                    beginBatchEdit();
                    handled = this.mEditor.mKeyListener.onKeyOther(this, (Editable) this.mText, otherEvent);
                    hideErrorIfUnchanged();
                    doDown = false;
                    if (handled) {
                        endBatchEdit();
                        return -1;
                    }
                } catch (AbstractMethodError e) {
                } catch (Throwable th) {
                    endBatchEdit();
                }
                endBatchEdit();
            }
            if (doDown) {
                beginBatchEdit();
                handled = this.mEditor.mKeyListener.onKeyDown(this, (Editable) this.mText, keyCode, event);
                endBatchEdit();
                hideErrorIfUnchanged();
                if (handled) {
                    return 1;
                }
            }
        }
        doDown = this.mMovement;
        if (doDown && this.mLayout != null) {
            handled = true;
            if (otherEvent != null) {
                try {
                    handled = false;
                    if (doDown.onKeyOther(this, this.mSpannable, otherEvent)) {
                        return -1;
                    }
                } catch (AbstractMethodError e2) {
                }
            }
            if (handled && this.mMovement.onKeyDown(this, this.mSpannable, keyCode, event)) {
                if (event.getRepeatCount() == 0 && !KeyEvent.isModifierKey(keyCode)) {
                    this.mPreventDefaultMovement = true;
                }
                return 2;
            } else if (event.getSource() == 257 && isDirectionalNavigationKey(keyCode)) {
                return -1;
            }
        }
        if (this.mPreventDefaultMovement && !KeyEvent.isModifierKey(keyCode)) {
            i = -1;
        }
        return i;
    }

    public void resetErrorChangedFlag() {
        Editor editor = this.mEditor;
        if (editor != null) {
            editor.mErrorWasChanged = false;
        }
    }

    public void hideErrorIfUnchanged() {
        Editor editor = this.mEditor;
        if (editor != null && editor.mError != null && !this.mEditor.mErrorWasChanged) {
            setError(null, null);
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (!isEnabled()) {
            return super.onKeyUp(keyCode, event);
        }
        if (!KeyEvent.isModifierKey(keyCode)) {
            this.mPreventDefaultMovement = false;
        }
        InputMethodManager imm;
        Editor editor;
        if (keyCode == 23) {
            if (event.hasNoModifiers() && !hasOnClickListeners() && this.mMovement != null && (this.mText instanceof Editable) && this.mLayout != null && onCheckIsTextEditor()) {
                imm = getInputMethodManager();
                viewClicked(imm);
                if (imm != null && getShowSoftInputOnFocus()) {
                    imm.showSoftInput(this, 0);
                }
            }
            return super.onKeyUp(keyCode, event);
        } else if (keyCode == 66 && event.hasNoModifiers()) {
            editor = this.mEditor;
            if (!(editor == null || editor.mInputContentType == null || this.mEditor.mInputContentType.onEditorActionListener == null || !this.mEditor.mInputContentType.enterDown)) {
                this.mEditor.mInputContentType.enterDown = false;
                if (this.mEditor.mInputContentType.onEditorActionListener.onEditorAction(this, 0, event)) {
                    return true;
                }
            }
            if (((event.getFlags() & 16) != 0 || shouldAdvanceFocusOnEnter()) && !hasOnClickListeners()) {
                View v = focusSearch(130);
                if (v != null) {
                    if (v.requestFocus(130)) {
                        super.onKeyUp(keyCode, event);
                        return true;
                    }
                    throw new IllegalStateException("focus search returned a view that wasn't able to take focus!");
                } else if ((event.getFlags() & 16) != 0) {
                    imm = getInputMethodManager();
                    if (imm != null && imm.isActive(this)) {
                        imm.hideSoftInputFromWindow(getWindowToken(), 0);
                    }
                }
            }
            return super.onKeyUp(keyCode, event);
        } else {
            editor = this.mEditor;
            if (editor != null && editor.mKeyListener != null && this.mEditor.mKeyListener.onKeyUp(this, (Editable) this.mText, keyCode, event)) {
                return true;
            }
            MovementMethod movementMethod = this.mMovement;
            if (movementMethod == null || this.mLayout == null || !movementMethod.onKeyUp(this, this.mSpannable, keyCode, event)) {
                return super.onKeyUp(keyCode, event);
            }
            return true;
        }
    }

    public boolean onCheckIsTextEditor() {
        Editor editor = this.mEditor;
        return (editor == null || editor.mInputType == 0) ? false : true;
    }

    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        if (onCheckIsTextEditor() && isEnabled()) {
            this.mEditor.createInputMethodStateIfNeeded();
            outAttrs.inputType = getInputType();
            if (this.mEditor.mInputContentType != null) {
                outAttrs.imeOptions = this.mEditor.mInputContentType.imeOptions;
                outAttrs.privateImeOptions = this.mEditor.mInputContentType.privateImeOptions;
                outAttrs.actionLabel = this.mEditor.mInputContentType.imeActionLabel;
                outAttrs.actionId = this.mEditor.mInputContentType.imeActionId;
                outAttrs.extras = this.mEditor.mInputContentType.extras;
                outAttrs.hintLocales = this.mEditor.mInputContentType.imeHintLocales;
            } else {
                outAttrs.imeOptions = 0;
                outAttrs.hintLocales = null;
            }
            if (focusSearch(130) != null) {
                outAttrs.imeOptions |= 134217728;
            }
            if (focusSearch(33) != null) {
                outAttrs.imeOptions |= 67108864;
            }
            if ((outAttrs.imeOptions & 255) == 0) {
                if ((outAttrs.imeOptions & 134217728) != 0) {
                    outAttrs.imeOptions |= 5;
                } else {
                    outAttrs.imeOptions |= 6;
                }
                if (!shouldAdvanceFocusOnEnter()) {
                    outAttrs.imeOptions |= 1073741824;
                }
            }
            if (isMultilineInputType(outAttrs.inputType)) {
                outAttrs.imeOptions |= 1073741824;
            }
            outAttrs.hintText = this.mHint;
            outAttrs.targetInputMethodUser = this.mTextOperationUser;
            if (this.mText instanceof Editable) {
                InputConnection ic = new EditableInputConnection(this);
                outAttrs.initialSelStart = getSelectionStart();
                outAttrs.initialSelEnd = getSelectionEnd();
                outAttrs.initialCapsMode = ic.getCursorCapsMode(getInputType());
                return ic;
            }
        }
        return null;
    }

    public boolean extractText(ExtractedTextRequest request, ExtractedText outText) {
        createEditorIfNeeded();
        return this.mEditor.extractText(request, outText);
    }

    static void removeParcelableSpans(Spannable spannable, int start, int end) {
        Object[] spans = spannable.getSpans(start, end, ParcelableSpan.class);
        int i = spans.length;
        while (i > 0) {
            i--;
            spannable.removeSpan(spans[i]);
        }
    }

    public void setExtractedText(ExtractedText text) {
        int end;
        int N;
        Editable content = getEditableText();
        if (text.text != null) {
            if (content == null) {
                setText(text.text, BufferType.EDITABLE);
            } else {
                int start;
                int end2;
                end = content.length();
                if (text.partialStartOffset >= 0) {
                    N = content.length();
                    int start2 = text.partialStartOffset;
                    if (start2 > N) {
                        start2 = N;
                    }
                    end = text.partialEndOffset;
                    if (end > N) {
                        end = N;
                    }
                    start = start2;
                    end2 = end;
                } else {
                    start = 0;
                    end2 = end;
                }
                removeParcelableSpans(content, start, end2);
                if (!TextUtils.equals(content.subSequence(start, end2), text.text)) {
                    content.replace(start, end2, text.text);
                } else if (text.text instanceof Spanned) {
                    TextUtils.copySpansFrom((Spanned) text.text, 0, end2 - start, Object.class, content, start);
                }
            }
        }
        Spannable sp = (Spannable) getText();
        end = sp.length();
        N = text.selectionStart;
        if (N < 0) {
            N = 0;
        } else if (N > end) {
            N = end;
        }
        int end3 = text.selectionEnd;
        if (end3 < 0) {
            end3 = 0;
        } else if (end3 > end) {
            end3 = end;
        }
        Selection.setSelection(sp, N, end3);
        if ((text.flags & 2) != 0) {
            MetaKeyKeyListener.startSelecting(this, sp);
        } else {
            MetaKeyKeyListener.stopSelecting(this, sp);
        }
        setHintInternal(text.hint);
    }

    public void setExtracting(ExtractedTextRequest req) {
        if (this.mEditor.mInputMethodState != null) {
            this.mEditor.mInputMethodState.mExtractedTextRequest = req;
        }
        this.mEditor.hideCursorAndSpanControllers();
        stopTextActionMode();
        if (this.mEditor.mSelectionModifierCursorController != null) {
            this.mEditor.mSelectionModifierCursorController.resetTouchOffsets();
        }
    }

    public void onCommitCompletion(CompletionInfo text) {
    }

    public void onCommitCorrection(CorrectionInfo info) {
        Editor editor = this.mEditor;
        if (editor != null) {
            editor.onCommitCorrection(info);
        }
    }

    public void beginBatchEdit() {
        Editor editor = this.mEditor;
        if (editor != null) {
            editor.beginBatchEdit();
        }
    }

    public void endBatchEdit() {
        Editor editor = this.mEditor;
        if (editor != null) {
            editor.endBatchEdit();
        }
    }

    public void onBeginBatchEdit() {
    }

    public void onEndBatchEdit() {
    }

    public boolean onPrivateIMECommand(String action, Bundle data) {
        return false;
    }

    @VisibleForTesting
    @UnsupportedAppUsage
    public void nullLayouts() {
        Layout layout = this.mLayout;
        if ((layout instanceof BoringLayout) && this.mSavedLayout == null) {
            this.mSavedLayout = (BoringLayout) layout;
        }
        layout = this.mHintLayout;
        if ((layout instanceof BoringLayout) && this.mSavedHintLayout == null) {
            this.mSavedHintLayout = (BoringLayout) layout;
        }
        this.mHintLayout = null;
        this.mLayout = null;
        this.mSavedMarqueeModeLayout = null;
        this.mHintBoring = null;
        this.mBoring = null;
        Editor editor = this.mEditor;
        if (editor != null) {
            editor.prepareCursorControllers();
        }
    }

    @UnsupportedAppUsage
    private void assumeLayout() {
        int width = ((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight();
        if (width < 1) {
            width = 0;
        }
        int physicalWidth = width;
        if (this.mHorizontallyScrolling) {
            width = 1048576;
        }
        Metrics metrics = UNKNOWN_BORING;
        makeNewLayout(width, physicalWidth, metrics, metrics, physicalWidth, false);
    }

    @UnsupportedAppUsage
    private Alignment getLayoutAlignment() {
        switch (getTextAlignment()) {
            case 1:
                int i = this.mGravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;
                if (i == 1) {
                    return Alignment.ALIGN_CENTER;
                }
                if (i == 3) {
                    return Alignment.ALIGN_LEFT;
                }
                if (i == 5) {
                    return Alignment.ALIGN_RIGHT;
                }
                if (i == Gravity.START) {
                    return Alignment.ALIGN_NORMAL;
                }
                if (i != Gravity.END) {
                    return Alignment.ALIGN_NORMAL;
                }
                return Alignment.ALIGN_OPPOSITE;
            case 2:
                return Alignment.ALIGN_NORMAL;
            case 3:
                return Alignment.ALIGN_OPPOSITE;
            case 4:
                return Alignment.ALIGN_CENTER;
            case 5:
                return getLayoutDirection() == 1 ? Alignment.ALIGN_RIGHT : Alignment.ALIGN_LEFT;
            case 6:
                return getLayoutDirection() == 1 ? Alignment.ALIGN_LEFT : Alignment.ALIGN_RIGHT;
            default:
                return Alignment.ALIGN_NORMAL;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:128:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:126:0x024d  */
    /* JADX WARNING: Missing block: B:110:0x0221, code skipped:
            if (r21 != r14.mLayout.getParagraphDirection(r6)) goto L_0x0229;
     */
    @com.android.internal.annotations.VisibleForTesting
    @android.annotation.UnsupportedAppUsage
    public void makeNewLayout(int r23, int r24, android.text.BoringLayout.Metrics r25, android.text.BoringLayout.Metrics r26, int r27, boolean r28) {
        /*
        r22 = this;
        r14 = r22;
        r15 = r27;
        r22.stopMarquee();
        r0 = r14.mMaximum;
        r14.mOldMaximum = r0;
        r0 = r14.mMaxMode;
        r14.mOldMaxMode = r0;
        r13 = 1;
        r14.mHighlightPathBogus = r13;
        if (r23 >= 0) goto L_0x0018;
    L_0x0014:
        r0 = 0;
        r16 = r0;
        goto L_0x001a;
    L_0x0018:
        r16 = r23;
    L_0x001a:
        if (r24 >= 0) goto L_0x0020;
    L_0x001c:
        r0 = 0;
        r17 = r0;
        goto L_0x0022;
    L_0x0020:
        r17 = r24;
    L_0x0022:
        r11 = r22.getLayoutAlignment();
        r0 = r14.mSingleLine;
        r10 = 0;
        if (r0 == 0) goto L_0x0039;
    L_0x002b:
        r0 = r14.mLayout;
        if (r0 == 0) goto L_0x0039;
    L_0x002f:
        r0 = android.text.Layout.Alignment.ALIGN_NORMAL;
        if (r11 == r0) goto L_0x0037;
    L_0x0033:
        r0 = android.text.Layout.Alignment.ALIGN_OPPOSITE;
        if (r11 != r0) goto L_0x0039;
    L_0x0037:
        r0 = r13;
        goto L_0x003a;
    L_0x0039:
        r0 = r10;
    L_0x003a:
        r18 = r0;
        r0 = 0;
        if (r18 == 0) goto L_0x0045;
    L_0x003f:
        r1 = r14.mLayout;
        r0 = r1.getParagraphDirection(r10);
    L_0x0045:
        r9 = r0;
        r0 = r14.mEllipsize;
        if (r0 == 0) goto L_0x0052;
    L_0x004a:
        r0 = r22.getKeyListener();
        if (r0 != 0) goto L_0x0052;
    L_0x0050:
        r5 = r13;
        goto L_0x0053;
    L_0x0052:
        r5 = r10;
    L_0x0053:
        r0 = r14.mEllipsize;
        r1 = android.text.TextUtils.TruncateAt.MARQUEE;
        if (r0 != r1) goto L_0x005f;
    L_0x0059:
        r0 = r14.mMarqueeFadeMode;
        if (r0 == 0) goto L_0x005f;
    L_0x005d:
        r0 = r13;
        goto L_0x0060;
    L_0x005f:
        r0 = r10;
    L_0x0060:
        r19 = r0;
        r0 = r14.mEllipsize;
        r1 = r14.mEllipsize;
        r2 = android.text.TextUtils.TruncateAt.MARQUEE;
        if (r1 != r2) goto L_0x0072;
    L_0x006a:
        r1 = r14.mMarqueeFadeMode;
        if (r1 != r13) goto L_0x0072;
    L_0x006e:
        r0 = android.text.TextUtils.TruncateAt.END_SMALL;
        r8 = r0;
        goto L_0x0073;
    L_0x0072:
        r8 = r0;
    L_0x0073:
        r0 = r14.mTextDir;
        if (r0 != 0) goto L_0x007d;
    L_0x0077:
        r0 = r22.getTextDirectionHeuristic();
        r14.mTextDir = r0;
    L_0x007d:
        r0 = r14.mEllipsize;
        if (r8 != r0) goto L_0x0083;
    L_0x0081:
        r7 = r13;
        goto L_0x0084;
    L_0x0083:
        r7 = r10;
    L_0x0084:
        r0 = r22;
        r1 = r16;
        r2 = r25;
        r3 = r27;
        r4 = r11;
        r6 = r8;
        r0 = r0.makeSingleLayout(r1, r2, r3, r4, r5, r6, r7);
        r14.mLayout = r0;
        if (r19 == 0) goto L_0x00bf;
    L_0x0096:
        r0 = android.text.TextUtils.TruncateAt.MARQUEE;
        if (r8 != r0) goto L_0x009d;
    L_0x009a:
        r0 = android.text.TextUtils.TruncateAt.END;
        goto L_0x009f;
    L_0x009d:
        r0 = android.text.TextUtils.TruncateAt.MARQUEE;
    L_0x009f:
        r12 = r0;
        r0 = r14.mEllipsize;
        if (r8 == r0) goto L_0x00a6;
    L_0x00a4:
        r0 = r13;
        goto L_0x00a7;
    L_0x00a6:
        r0 = r10;
    L_0x00a7:
        r6 = r22;
        r7 = r16;
        r20 = r8;
        r8 = r25;
        r4 = r9;
        r9 = r27;
        r3 = r10;
        r10 = r11;
        r2 = r11;
        r11 = r5;
        r1 = r13;
        r13 = r0;
        r0 = r6.makeSingleLayout(r7, r8, r9, r10, r11, r12, r13);
        r14.mSavedMarqueeModeLayout = r0;
        goto L_0x00c5;
    L_0x00bf:
        r20 = r8;
        r4 = r9;
        r3 = r10;
        r2 = r11;
        r1 = r13;
    L_0x00c5:
        r0 = r14.mEllipsize;
        if (r0 == 0) goto L_0x00cb;
    L_0x00c9:
        r0 = r1;
        goto L_0x00cc;
    L_0x00cb:
        r0 = r3;
    L_0x00cc:
        r11 = r0;
        r0 = 0;
        r14.mHintLayout = r0;
        r0 = r14.mHint;
        if (r0 == 0) goto L_0x020a;
    L_0x00d4:
        if (r11 == 0) goto L_0x00da;
    L_0x00d6:
        r0 = r16;
        r12 = r0;
        goto L_0x00dc;
    L_0x00da:
        r12 = r17;
    L_0x00dc:
        r0 = UNKNOWN_BORING;
        r5 = r26;
        if (r5 != r0) goto L_0x00f4;
    L_0x00e2:
        r0 = r14.mHint;
        r6 = r14.mTextPaint;
        r7 = r14.mTextDir;
        r8 = r14.mHintBoring;
        r0 = android.text.BoringLayout.isBoring(r0, r6, r7, r8);
        if (r0 == 0) goto L_0x00f2;
    L_0x00f0:
        r14.mHintBoring = r0;
    L_0x00f2:
        r13 = r0;
        goto L_0x00f5;
    L_0x00f4:
        r13 = r5;
    L_0x00f5:
        if (r13 == 0) goto L_0x0194;
    L_0x00f7:
        r0 = r13.width;
        if (r0 > r12) goto L_0x0152;
    L_0x00fb:
        if (r11 == 0) goto L_0x010a;
    L_0x00fd:
        r0 = r13.width;
        if (r0 > r15) goto L_0x0102;
    L_0x0101:
        goto L_0x010a;
    L_0x0102:
        r10 = r1;
        r23 = r2;
        r15 = r3;
        r21 = r4;
        goto L_0x0158;
    L_0x010a:
        r0 = r14.mSavedHintLayout;
        if (r0 == 0) goto L_0x0131;
    L_0x010e:
        r5 = r14.mHint;
        r6 = r14.mTextPaint;
        r7 = r14.mSpacingMult;
        r8 = r14.mSpacingAdd;
        r9 = r14.mIncludePad;
        r10 = r1;
        r1 = r5;
        r5 = r2;
        r2 = r6;
        r6 = r3;
        r3 = r12;
        r21 = r4;
        r4 = r5;
        r23 = r5;
        r5 = r7;
        r7 = r6;
        r6 = r8;
        r8 = r7;
        r7 = r13;
        r15 = r8;
        r8 = r9;
        r0 = r0.replaceOrMake(r1, r2, r3, r4, r5, r6, r7, r8);
        r14.mHintLayout = r0;
        goto L_0x014b;
    L_0x0131:
        r10 = r1;
        r23 = r2;
        r15 = r3;
        r21 = r4;
        r0 = r14.mHint;
        r1 = r14.mTextPaint;
        r4 = r14.mSpacingMult;
        r5 = r14.mSpacingAdd;
        r7 = r14.mIncludePad;
        r2 = r12;
        r3 = r23;
        r6 = r13;
        r0 = android.text.BoringLayout.make(r0, r1, r2, r3, r4, r5, r6, r7);
        r14.mHintLayout = r0;
    L_0x014b:
        r0 = r14.mHintLayout;
        r0 = (android.text.BoringLayout) r0;
        r14.mSavedHintLayout = r0;
        goto L_0x0199;
    L_0x0152:
        r10 = r1;
        r23 = r2;
        r15 = r3;
        r21 = r4;
    L_0x0158:
        if (r11 == 0) goto L_0x0199;
    L_0x015a:
        r0 = r13.width;
        if (r0 > r12) goto L_0x0199;
    L_0x015e:
        r0 = r14.mSavedHintLayout;
        if (r0 == 0) goto L_0x017b;
    L_0x0162:
        r1 = r14.mHint;
        r2 = r14.mTextPaint;
        r5 = r14.mSpacingMult;
        r6 = r14.mSpacingAdd;
        r8 = r14.mIncludePad;
        r9 = r14.mEllipsize;
        r3 = r12;
        r4 = r23;
        r7 = r13;
        r10 = r27;
        r0 = r0.replaceOrMake(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
        r14.mHintLayout = r0;
        goto L_0x0199;
    L_0x017b:
        r0 = r14.mHint;
        r1 = r14.mTextPaint;
        r4 = r14.mSpacingMult;
        r5 = r14.mSpacingAdd;
        r7 = r14.mIncludePad;
        r8 = r14.mEllipsize;
        r2 = r12;
        r3 = r23;
        r6 = r13;
        r9 = r27;
        r0 = android.text.BoringLayout.make(r0, r1, r2, r3, r4, r5, r6, r7, r8, r9);
        r14.mHintLayout = r0;
        goto L_0x0199;
    L_0x0194:
        r23 = r2;
        r15 = r3;
        r21 = r4;
    L_0x0199:
        r0 = r14.mHintLayout;
        if (r0 != 0) goto L_0x0203;
    L_0x019d:
        r0 = r14.mHint;
        r1 = r0.length();
        r2 = r14.mTextPaint;
        r0 = android.text.StaticLayout.Builder.obtain(r0, r15, r1, r2, r12);
        r1 = r23;
        r0 = r0.setAlignment(r1);
        r2 = r14.mTextDir;
        r0 = r0.setTextDirection(r2);
        r2 = r14.mSpacingAdd;
        r3 = r14.mSpacingMult;
        r0 = r0.setLineSpacing(r2, r3);
        r2 = r14.mIncludePad;
        r0 = r0.setIncludePad(r2);
        r2 = r14.mUseFallbackLineSpacing;
        r0 = r0.setUseLineSpacingFromFallbacks(r2);
        r2 = r14.mBreakStrategy;
        r0 = r0.setBreakStrategy(r2);
        r2 = r14.mHyphenationFrequency;
        r0 = r0.setHyphenationFrequency(r2);
        r2 = r14.mJustificationMode;
        r0 = r0.setJustificationMode(r2);
        r2 = r14.mMaxMode;
        r3 = 1;
        if (r2 != r3) goto L_0x01e3;
    L_0x01e0:
        r2 = r14.mMaximum;
        goto L_0x01e6;
    L_0x01e3:
        r2 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
    L_0x01e6:
        r0 = r0.setMaxLines(r2);
        if (r11 == 0) goto L_0x01f9;
    L_0x01ec:
        r2 = r14.mEllipsize;
        r2 = r0.setEllipsize(r2);
        r4 = r27;
        r6 = r15;
        r2.setEllipsizedWidth(r4);
        goto L_0x01fc;
    L_0x01f9:
        r4 = r27;
        r6 = r15;
    L_0x01fc:
        r2 = r0.build();
        r14.mHintLayout = r2;
        goto L_0x0215;
    L_0x0203:
        r1 = r23;
        r4 = r27;
        r6 = r15;
        r3 = 1;
        goto L_0x0215;
    L_0x020a:
        r5 = r26;
        r6 = r3;
        r21 = r4;
        r4 = r15;
        r3 = r1;
        r1 = r2;
        r13 = r5;
        r12 = r17;
    L_0x0215:
        if (r28 != 0) goto L_0x0227;
    L_0x0217:
        if (r18 == 0) goto L_0x0224;
    L_0x0219:
        r0 = r14.mLayout;
        r0 = r0.getParagraphDirection(r6);
        r2 = r21;
        if (r2 == r0) goto L_0x022c;
    L_0x0223:
        goto L_0x0229;
    L_0x0224:
        r2 = r21;
        goto L_0x022c;
    L_0x0227:
        r2 = r21;
    L_0x0229:
        r22.registerForPreDraw();
    L_0x022c:
        r0 = r14.mEllipsize;
        r5 = android.text.TextUtils.TruncateAt.MARQUEE;
        if (r0 != r5) goto L_0x0249;
    L_0x0232:
        r0 = (float) r4;
        r0 = r14.compressText(r0);
        if (r0 != 0) goto L_0x0249;
    L_0x0239:
        r0 = r14.mLayoutParams;
        r0 = r0.height;
        r5 = -2;
        if (r0 == r5) goto L_0x0247;
    L_0x0240:
        r5 = -1;
        if (r0 == r5) goto L_0x0247;
    L_0x0243:
        r22.startMarquee();
        goto L_0x0249;
    L_0x0247:
        r14.mRestartMarquee = r3;
    L_0x0249:
        r0 = r14.mEditor;
        if (r0 == 0) goto L_0x0250;
    L_0x024d:
        r0.prepareCursorControllers();
    L_0x0250:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.TextView.makeNewLayout(int, int, android.text.BoringLayout$Metrics, android.text.BoringLayout$Metrics, int, boolean):void");
    }

    @VisibleForTesting
    public boolean useDynamicLayout() {
        return isTextSelectable() || (this.mSpannable != null && this.mPrecomputed == null);
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x00c1  */
    public android.text.Layout makeSingleLayout(int r19, android.text.BoringLayout.Metrics r20, int r21, android.text.Layout.Alignment r22, boolean r23, android.text.TextUtils.TruncateAt r24, boolean r25) {
        /*
        r18 = this;
        r0 = r18;
        r12 = r19;
        r13 = r21;
        r14 = r22;
        r15 = r24;
        r16 = 0;
        r1 = r18.useDynamicLayout();
        if (r1 == 0) goto L_0x0069;
    L_0x0012:
        r1 = r0.mText;
        r2 = r0.mTextPaint;
        r1 = android.text.DynamicLayout.Builder.obtain(r1, r2, r12);
        r2 = r0.mTransformed;
        r1 = r1.setDisplayText(r2);
        r1 = r1.setAlignment(r14);
        r2 = r0.mTextDir;
        r1 = r1.setTextDirection(r2);
        r2 = r0.mSpacingAdd;
        r3 = r0.mSpacingMult;
        r1 = r1.setLineSpacing(r2, r3);
        r2 = r0.mIncludePad;
        r1 = r1.setIncludePad(r2);
        r2 = r0.mUseFallbackLineSpacing;
        r1 = r1.setUseLineSpacingFromFallbacks(r2);
        r2 = r0.mBreakStrategy;
        r1 = r1.setBreakStrategy(r2);
        r2 = r0.mHyphenationFrequency;
        r1 = r1.setHyphenationFrequency(r2);
        r2 = r0.mJustificationMode;
        r1 = r1.setJustificationMode(r2);
        r2 = r18.getKeyListener();
        if (r2 != 0) goto L_0x0058;
    L_0x0056:
        r2 = r15;
        goto L_0x0059;
    L_0x0058:
        r2 = 0;
    L_0x0059:
        r1 = r1.setEllipsize(r2);
        r1 = r1.setEllipsizedWidth(r13);
        r16 = r1.build();
        r17 = r20;
        goto L_0x0110;
    L_0x0069:
        r1 = UNKNOWN_BORING;
        r2 = r20;
        if (r2 != r1) goto L_0x0081;
    L_0x006f:
        r1 = r0.mTransformed;
        r3 = r0.mTextPaint;
        r4 = r0.mTextDir;
        r5 = r0.mBoring;
        r1 = android.text.BoringLayout.isBoring(r1, r3, r4, r5);
        if (r1 == 0) goto L_0x007f;
    L_0x007d:
        r0.mBoring = r1;
    L_0x007f:
        r11 = r1;
        goto L_0x0082;
    L_0x0081:
        r11 = r2;
    L_0x0082:
        if (r11 == 0) goto L_0x010e;
    L_0x0084:
        r1 = r11.width;
        if (r1 > r12) goto L_0x00ca;
    L_0x0088:
        if (r15 == 0) goto L_0x008e;
    L_0x008a:
        r1 = r11.width;
        if (r1 > r13) goto L_0x00ca;
    L_0x008e:
        if (r25 == 0) goto L_0x00aa;
    L_0x0090:
        r1 = r0.mSavedLayout;
        if (r1 == 0) goto L_0x00aa;
    L_0x0094:
        r2 = r0.mTransformed;
        r3 = r0.mTextPaint;
        r6 = r0.mSpacingMult;
        r7 = r0.mSpacingAdd;
        r9 = r0.mIncludePad;
        r4 = r19;
        r5 = r22;
        r8 = r11;
        r1 = r1.replaceOrMake(r2, r3, r4, r5, r6, r7, r8, r9);
        r16 = r1;
        goto L_0x00bf;
    L_0x00aa:
        r1 = r0.mTransformed;
        r2 = r0.mTextPaint;
        r5 = r0.mSpacingMult;
        r6 = r0.mSpacingAdd;
        r8 = r0.mIncludePad;
        r3 = r19;
        r4 = r22;
        r7 = r11;
        r1 = android.text.BoringLayout.make(r1, r2, r3, r4, r5, r6, r7, r8);
        r16 = r1;
    L_0x00bf:
        if (r25 == 0) goto L_0x00c7;
    L_0x00c1:
        r1 = r16;
        r1 = (android.text.BoringLayout) r1;
        r0.mSavedLayout = r1;
    L_0x00c7:
        r17 = r11;
        goto L_0x0110;
    L_0x00ca:
        if (r23 == 0) goto L_0x010b;
    L_0x00cc:
        r1 = r11.width;
        if (r1 > r12) goto L_0x010b;
    L_0x00d0:
        if (r25 == 0) goto L_0x00f0;
    L_0x00d2:
        r1 = r0.mSavedLayout;
        if (r1 == 0) goto L_0x00f0;
    L_0x00d6:
        r2 = r0.mTransformed;
        r3 = r0.mTextPaint;
        r6 = r0.mSpacingMult;
        r7 = r0.mSpacingAdd;
        r9 = r0.mIncludePad;
        r4 = r19;
        r5 = r22;
        r8 = r11;
        r10 = r24;
        r17 = r11;
        r11 = r21;
        r16 = r1.replaceOrMake(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11);
        goto L_0x0110;
    L_0x00f0:
        r17 = r11;
        r1 = r0.mTransformed;
        r2 = r0.mTextPaint;
        r5 = r0.mSpacingMult;
        r6 = r0.mSpacingAdd;
        r8 = r0.mIncludePad;
        r3 = r19;
        r4 = r22;
        r7 = r17;
        r9 = r24;
        r10 = r21;
        r16 = android.text.BoringLayout.make(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
        goto L_0x0110;
    L_0x010b:
        r17 = r11;
        goto L_0x0110;
    L_0x010e:
        r17 = r11;
    L_0x0110:
        if (r16 != 0) goto L_0x016b;
    L_0x0112:
        r1 = r0.mTransformed;
        r2 = 0;
        r3 = r1.length();
        r4 = r0.mTextPaint;
        r1 = android.text.StaticLayout.Builder.obtain(r1, r2, r3, r4, r12);
        r1 = r1.setAlignment(r14);
        r2 = r0.mTextDir;
        r1 = r1.setTextDirection(r2);
        r2 = r0.mSpacingAdd;
        r3 = r0.mSpacingMult;
        r1 = r1.setLineSpacing(r2, r3);
        r2 = r0.mIncludePad;
        r1 = r1.setIncludePad(r2);
        r2 = r0.mUseFallbackLineSpacing;
        r1 = r1.setUseLineSpacingFromFallbacks(r2);
        r2 = r0.mBreakStrategy;
        r1 = r1.setBreakStrategy(r2);
        r2 = r0.mHyphenationFrequency;
        r1 = r1.setHyphenationFrequency(r2);
        r2 = r0.mJustificationMode;
        r1 = r1.setJustificationMode(r2);
        r2 = r0.mMaxMode;
        r3 = 1;
        if (r2 != r3) goto L_0x0157;
    L_0x0154:
        r2 = r0.mMaximum;
        goto L_0x015a;
    L_0x0157:
        r2 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
    L_0x015a:
        r1 = r1.setMaxLines(r2);
        if (r23 == 0) goto L_0x0167;
    L_0x0160:
        r2 = r1.setEllipsize(r15);
        r2.setEllipsizedWidth(r13);
    L_0x0167:
        r16 = r1.build();
    L_0x016b:
        return r16;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.TextView.makeSingleLayout(int, android.text.BoringLayout$Metrics, int, android.text.Layout$Alignment, boolean, android.text.TextUtils$TruncateAt, boolean):android.text.Layout");
    }

    @UnsupportedAppUsage
    private boolean compressText(float width) {
        if (!isHardwareAccelerated() && width > 0.0f && this.mLayout != null && getLineCount() == 1 && !this.mUserSetTextScaleX && this.mTextPaint.getTextScaleX() == 1.0f) {
            float overflow = ((this.mLayout.getLineWidth(0) + 1.0f) - width) / width;
            if (overflow > 0.0f && overflow <= 0.07f) {
                this.mTextPaint.setTextScaleX((1.0f - overflow) - 0.005f);
                post(new Runnable() {
                    public void run() {
                        TextView.this.requestLayout();
                    }
                });
                return true;
            }
        }
        return false;
    }

    private static int desired(Layout layout) {
        int i;
        int n = layout.getLineCount();
        CharSequence text = layout.getText();
        float max = 0.0f;
        for (i = 0; i < n - 1; i++) {
            if (text.charAt(layout.getLineEnd(i) - 1) != 10) {
                return -1;
            }
        }
        for (i = 0; i < n; i++) {
            max = Math.max(max, layout.getLineWidth(i));
        }
        return (int) Math.ceil((double) max);
    }

    public void setIncludeFontPadding(boolean includepad) {
        if (this.mIncludePad != includepad) {
            this.mIncludePad = includepad;
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public boolean getIncludeFontPadding() {
        return this.mIncludePad;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Metrics boring;
        Metrics hintBoring;
        int des;
        boolean fromexisting;
        int width;
        int width2;
        int hintWidth;
        int unpaddedWidth;
        int height;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        Metrics boring2 = UNKNOWN_BORING;
        Metrics hintBoring2 = UNKNOWN_BORING;
        if (this.mTextDir == null) {
            this.mTextDir = getTextDirectionHeuristic();
        }
        int des2 = -1;
        boolean fromexisting2 = false;
        float widthLimit = widthMode == Integer.MIN_VALUE ? (float) widthSize : Float.MAX_VALUE;
        if (widthMode == 1073741824) {
            boring = boring2;
            hintBoring = hintBoring2;
            des = -1;
            fromexisting = false;
            width = widthSize;
        } else {
            Layout layout = this.mLayout;
            if (layout != null && this.mEllipsize == null) {
                des2 = desired(layout);
            }
            if (des2 < 0) {
                boring2 = BoringLayout.isBoring(this.mTransformed, this.mTextPaint, this.mTextDir, this.mBoring);
                if (boring2 != null) {
                    this.mBoring = boring2;
                }
            } else {
                fromexisting2 = true;
            }
            if (boring2 == null || boring2 == UNKNOWN_BORING) {
                if (des2 < 0) {
                    CharSequence charSequence = this.mTransformed;
                    des2 = (int) Math.ceil((double) Layout.getDesiredWidthWithLimit(charSequence, 0, charSequence.length(), this.mTextPaint, this.mTextDir, widthLimit));
                }
                width2 = des2;
            } else {
                width2 = boring2.width;
            }
            Drawables dr = this.mDrawables;
            if (dr != null) {
                width2 = Math.max(Math.max(width2, dr.mDrawableWidthTop), dr.mDrawableWidthBottom);
            }
            if (this.mHint != null) {
                width = -1;
                Layout layout2 = this.mHintLayout;
                if (layout2 != null && this.mEllipsize == null) {
                    width = desired(layout2);
                }
                if (width < 0) {
                    hintBoring2 = BoringLayout.isBoring(this.mHint, this.mTextPaint, this.mTextDir, this.mHintBoring);
                    if (hintBoring2 != null) {
                        this.mHintBoring = hintBoring2;
                    }
                }
                if (hintBoring2 == null || hintBoring2 == UNKNOWN_BORING) {
                    if (width < 0) {
                        CharSequence charSequence2 = this.mHint;
                        width = (int) Math.ceil((double) Layout.getDesiredWidthWithLimit(charSequence2, 0, charSequence2.length(), this.mTextPaint, this.mTextDir, widthLimit));
                    }
                    hintWidth = width;
                } else {
                    hintWidth = hintBoring2.width;
                }
                if (hintWidth > width2) {
                    width2 = hintWidth;
                }
            }
            width2 += getCompoundPaddingLeft() + getCompoundPaddingRight();
            if (this.mMaxWidthMode == 1) {
                width2 = Math.min(width2, this.mMaxWidth * getLineHeight());
            } else {
                width2 = Math.min(width2, this.mMaxWidth);
            }
            if (this.mMinWidthMode == 1) {
                width2 = Math.max(width2, this.mMinWidth * getLineHeight());
            } else {
                width2 = Math.max(width2, this.mMinWidth);
            }
            width2 = Math.max(width2, getSuggestedMinimumWidth());
            if (widthMode == Integer.MIN_VALUE) {
                boring = boring2;
                hintBoring = hintBoring2;
                des = des2;
                fromexisting = fromexisting2;
                width = Math.min(widthSize, width2);
            } else {
                boring = boring2;
                hintBoring = hintBoring2;
                des = des2;
                fromexisting = fromexisting2;
                width = width2;
            }
        }
        int want = (width - getCompoundPaddingLeft()) - getCompoundPaddingRight();
        hintWidth = want;
        if (this.mHorizontallyScrolling) {
            want = 1048576;
        }
        width2 = want;
        int hintWant = width2;
        Layout layout3 = this.mHintLayout;
        des2 = layout3 == null ? hintWant : layout3.getWidth();
        layout3 = this.mLayout;
        int hintWant2;
        int i;
        if (layout3 == null) {
            Object obj = null;
            hintWant2 = hintWant;
            widthSize = des;
            unpaddedWidth = hintWidth;
            widthMode = 1073741824;
            makeNewLayout(width2, hintWant, boring, hintBoring, (width - getCompoundPaddingLeft()) - getCompoundPaddingRight(), false);
            i = hintWant2;
        } else {
            boolean z;
            boolean layoutChanged;
            boolean widthChanged;
            boolean maximumChanged;
            int i2;
            int hintWidth2 = des2;
            hintWant2 = hintWant;
            unpaddedWidth = hintWidth;
            int i3 = widthMode;
            int i4 = widthSize;
            widthMode = 1073741824;
            widthSize = des;
            des = width2;
            if (layout3.getWidth() == des) {
                hintWidth = hintWant2;
                if (hintWidth2 == hintWidth && this.mLayout.getEllipsizedWidth() == (width - getCompoundPaddingLeft()) - getCompoundPaddingRight()) {
                    z = false;
                    layoutChanged = z;
                    z = this.mHint != null && this.mEllipsize == null && des > this.mLayout.getWidth() && ((this.mLayout instanceof BoringLayout) || (fromexisting && widthSize >= 0 && widthSize <= des));
                    widthChanged = z;
                    z = this.mMaxMode == this.mOldMaxMode || this.mMaximum != this.mOldMaximum;
                    maximumChanged = z;
                    if (layoutChanged && !maximumChanged) {
                        i = hintWidth;
                        i2 = des;
                    } else if (maximumChanged && widthChanged) {
                        this.mLayout.increaseWidthTo(des);
                        i = hintWidth;
                        i2 = des;
                    } else {
                        i2 = des;
                        makeNewLayout(des, hintWidth, boring, hintBoring, (width - getCompoundPaddingLeft()) - getCompoundPaddingRight(), false);
                    }
                }
            } else {
                hintWidth = hintWant2;
            }
            z = true;
            layoutChanged = z;
            if (this.mHint != null) {
            }
            widthChanged = z;
            if (this.mMaxMode == this.mOldMaxMode) {
            }
            maximumChanged = z;
            if (layoutChanged) {
            }
            if (maximumChanged) {
            }
            i2 = des;
            makeNewLayout(des, hintWidth, boring, hintBoring, (width - getCompoundPaddingLeft()) - getCompoundPaddingRight(), false);
        }
        if (heightMode == widthMode) {
            want = heightSize;
            this.mDesiredHeightAtMeasure = -1;
            height = want;
        } else {
            want = getDesiredHeight();
            height = want;
            this.mDesiredHeightAtMeasure = want;
            if (heightMode == Integer.MIN_VALUE) {
                height = Math.min(want, heightSize);
            }
        }
        want = (height - getCompoundPaddingTop()) - getCompoundPaddingBottom();
        if (this.mMaxMode == 1) {
            des2 = this.mLayout.getLineCount();
            hintWant = this.mMaximum;
            if (des2 > hintWant) {
                want = Math.min(want, this.mLayout.getLineTop(hintWant));
            }
        }
        if (this.mMovement != null) {
        } else if (this.mLayout.getWidth() <= unpaddedWidth && this.mLayout.getHeight() <= unpaddedHeight) {
            scrollTo(0, 0);
            setMeasuredDimension(width, height);
        }
        registerForPreDraw();
        setMeasuredDimension(width, height);
    }

    private void autoSizeText() {
        if (isAutoSizeEnabled()) {
            if (this.mNeedsAutoSizeText) {
                if (getMeasuredWidth() > 0 && getMeasuredHeight() > 0) {
                    int availableWidth;
                    if (this.mHorizontallyScrolling) {
                        availableWidth = 1048576;
                    } else {
                        availableWidth = (getMeasuredWidth() - getTotalPaddingLeft()) - getTotalPaddingRight();
                    }
                    int availableHeight = (getMeasuredHeight() - getExtendedPaddingBottom()) - getExtendedPaddingTop();
                    if (availableWidth > 0 && availableHeight > 0) {
                        synchronized (TEMP_RECTF) {
                            TEMP_RECTF.setEmpty();
                            TEMP_RECTF.right = (float) availableWidth;
                            TEMP_RECTF.bottom = (float) availableHeight;
                            float optimalTextSize = (float) findLargestTextSizeWhichFits(TEMP_RECTF);
                            if (optimalTextSize != getTextSize()) {
                                setTextSizeInternal(0, optimalTextSize, false);
                                makeNewLayout(availableWidth, 0, UNKNOWN_BORING, UNKNOWN_BORING, ((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight(), false);
                            }
                        }
                    } else {
                        return;
                    }
                }
                return;
            }
            this.mNeedsAutoSizeText = true;
        }
    }

    private int findLargestTextSizeWhichFits(RectF availableSpace) {
        int sizesCount = this.mAutoSizeTextSizesInPx.length;
        if (sizesCount != 0) {
            int bestSizeIndex = 0;
            int lowIndex = 0 + 1;
            int highIndex = sizesCount - 1;
            while (lowIndex <= highIndex) {
                int sizeToTryIndex = (lowIndex + highIndex) / 2;
                if (suggestedSizeFitsInSpace(this.mAutoSizeTextSizesInPx[sizeToTryIndex], availableSpace)) {
                    bestSizeIndex = lowIndex;
                    lowIndex = sizeToTryIndex + 1;
                } else {
                    highIndex = sizeToTryIndex - 1;
                    bestSizeIndex = highIndex;
                }
            }
            return this.mAutoSizeTextSizesInPx[bestSizeIndex];
        }
        throw new IllegalStateException("No available text sizes to choose from.");
    }

    private boolean suggestedSizeFitsInSpace(int suggestedSizeInPx, RectF availableSpace) {
        CharSequence text = this.mTransformed;
        if (text == null) {
            text = getText();
        }
        int maxLines = getMaxLines();
        TextPaint textPaint = this.mTempTextPaint;
        if (textPaint == null) {
            this.mTempTextPaint = new TextPaint();
        } else {
            textPaint.reset();
        }
        this.mTempTextPaint.set(getPaint());
        this.mTempTextPaint.setTextSize((float) suggestedSizeInPx);
        Builder layoutBuilder = Builder.obtain(text, 0, text.length(), this.mTempTextPaint, Math.round(availableSpace.right));
        layoutBuilder.setAlignment(getLayoutAlignment()).setLineSpacing(getLineSpacingExtra(), getLineSpacingMultiplier()).setIncludePad(getIncludeFontPadding()).setUseLineSpacingFromFallbacks(this.mUseFallbackLineSpacing).setBreakStrategy(getBreakStrategy()).setHyphenationFrequency(getHyphenationFrequency()).setJustificationMode(getJustificationMode()).setMaxLines(this.mMaxMode == 1 ? this.mMaximum : Integer.MAX_VALUE).setTextDirection(getTextDirectionHeuristic());
        StaticLayout layout = layoutBuilder.build();
        if ((maxLines == -1 || layout.getLineCount() <= maxLines) && ((float) layout.getHeight()) <= availableSpace.bottom) {
            return true;
        }
        return false;
    }

    private int getDesiredHeight() {
        boolean z = true;
        int desiredHeight = getDesiredHeight(this.mLayout, true);
        Layout layout = this.mHintLayout;
        if (this.mEllipsize == null) {
            z = false;
        }
        return Math.max(desiredHeight, getDesiredHeight(layout, z));
    }

    private int getDesiredHeight(Layout layout, boolean cap) {
        if (layout == null) {
            return 0;
        }
        int desired = layout.getHeight(cap);
        Drawables dr = this.mDrawables;
        if (dr != null) {
            desired = Math.max(Math.max(desired, dr.mDrawableHeightLeft), dr.mDrawableHeightRight);
        }
        int linecount = layout.getLineCount();
        int padding = getCompoundPaddingTop() + getCompoundPaddingBottom();
        desired += padding;
        if (this.mMaxMode != 1) {
            desired = Math.min(desired, this.mMaximum);
        } else if (cap && linecount > this.mMaximum && ((layout instanceof DynamicLayout) || (layout instanceof BoringLayout))) {
            desired = layout.getLineTop(this.mMaximum);
            if (dr != null) {
                desired = Math.max(Math.max(desired, dr.mDrawableHeightLeft), dr.mDrawableHeightRight);
            }
            desired += padding;
            linecount = this.mMaximum;
        }
        if (this.mMinMode != 1) {
            desired = Math.max(desired, this.mMinimum);
        } else if (linecount < this.mMinimum) {
            desired += getLineHeight() * (this.mMinimum - linecount);
        }
        return Math.max(desired, getSuggestedMinimumHeight());
    }

    private void checkForResize() {
        boolean sizeChanged = false;
        if (this.mLayout != null) {
            if (this.mLayoutParams.width == -2) {
                sizeChanged = true;
                invalidate();
            }
            if (this.mLayoutParams.height == -2) {
                if (getDesiredHeight() != getHeight()) {
                    sizeChanged = true;
                }
            } else if (this.mLayoutParams.height == -1 && this.mDesiredHeightAtMeasure >= 0 && getDesiredHeight() != this.mDesiredHeightAtMeasure) {
                sizeChanged = true;
            }
        }
        if (sizeChanged) {
            requestLayout();
        }
    }

    @UnsupportedAppUsage
    private void checkForRelayout() {
        if ((this.mLayoutParams.width != -2 || (this.mMaxWidthMode == this.mMinWidthMode && this.mMaxWidth == this.mMinWidth)) && ((this.mHint == null || this.mHintLayout != null) && ((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight() > 0)) {
            int oldht = this.mLayout.getHeight();
            int want = this.mLayout.getWidth();
            Layout layout = this.mHintLayout;
            int hintWant = layout == null ? 0 : layout.getWidth();
            Metrics metrics = UNKNOWN_BORING;
            makeNewLayout(want, hintWant, metrics, metrics, ((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight(), false);
            if (this.mEllipsize != TruncateAt.MARQUEE) {
                if (this.mLayoutParams.height != -2 && this.mLayoutParams.height != -1) {
                    autoSizeText();
                    invalidate();
                    return;
                } else if (this.mLayout.getHeight() == oldht) {
                    Layout layout2 = this.mHintLayout;
                    if (layout2 == null || layout2.getHeight() == oldht) {
                        autoSizeText();
                        invalidate();
                        return;
                    }
                }
            }
            requestLayout();
            invalidate();
        } else {
            nullLayouts();
            requestLayout();
            invalidate();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mDeferScroll >= 0) {
            int curs = this.mDeferScroll;
            this.mDeferScroll = -1;
            bringPointIntoView(Math.min(curs, this.mText.length()));
        }
        autoSizeText();
    }

    private boolean isShowingHint() {
        return TextUtils.isEmpty(this.mText) && !TextUtils.isEmpty(this.mHint);
    }

    @UnsupportedAppUsage
    private boolean bringTextIntoView() {
        int scrollx;
        int scrolly;
        Layout layout = isShowingHint() ? this.mHintLayout : this.mLayout;
        int line = 0;
        if ((this.mGravity & 112) == 80) {
            line = layout.getLineCount() - 1;
        }
        Alignment a = layout.getParagraphAlignment(line);
        int dir = layout.getParagraphDirection(line);
        int hspace = ((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight();
        int vspace = ((this.mBottom - this.mTop) - getExtendedPaddingTop()) - getExtendedPaddingBottom();
        int ht = layout.getHeight();
        if (a == Alignment.ALIGN_NORMAL) {
            a = dir == 1 ? Alignment.ALIGN_LEFT : Alignment.ALIGN_RIGHT;
        } else if (a == Alignment.ALIGN_OPPOSITE) {
            a = dir == 1 ? Alignment.ALIGN_RIGHT : Alignment.ALIGN_LEFT;
        }
        if (a == Alignment.ALIGN_CENTER) {
            int left = (int) Math.floor((double) layout.getLineLeft(line));
            int right = (int) Math.ceil((double) layout.getLineRight(line));
            if (right - left < hspace) {
                scrollx = ((right + left) / 2) - (hspace / 2);
            } else if (dir < 0) {
                scrollx = right - hspace;
            } else {
                scrollx = left;
            }
        } else if (a == Alignment.ALIGN_RIGHT) {
            scrollx = ((int) Math.ceil((double) layout.getLineRight(line))) - hspace;
        } else {
            scrollx = (int) Math.floor((double) layout.getLineLeft(line));
        }
        if (ht < vspace) {
            scrolly = 0;
        } else if ((this.mGravity & 112) == 80) {
            scrolly = ht - vspace;
        } else {
            scrolly = 0;
        }
        if (scrollx == this.mScrollX && scrolly == this.mScrollY) {
            return false;
        }
        scrollTo(scrollx, scrolly);
        return true;
    }

    public boolean bringPointIntoView(int offset) {
        int i = offset;
        if (isLayoutRequested()) {
            this.mDeferScroll = i;
            return false;
        }
        Layout layout = isShowingHint() ? this.mHintLayout : this.mLayout;
        if (layout == null) {
            return false;
        }
        int hs;
        boolean changed;
        int line = layout.getLineForOffset(i);
        int i2 = AnonymousClass4.$SwitchMap$android$text$Layout$Alignment[layout.getParagraphAlignment(line).ordinal()];
        boolean clamped = true;
        if (i2 == 1) {
            i2 = 1;
        } else if (i2 == 2) {
            i2 = -1;
        } else if (i2 == 3) {
            i2 = layout.getParagraphDirection(line);
        } else if (i2 != 4) {
            i2 = 0;
        } else {
            i2 = -layout.getParagraphDirection(line);
        }
        if (i2 <= 0) {
            clamped = false;
        }
        int x = (int) layout.getPrimaryHorizontal(i, clamped);
        int top = layout.getLineTop(line);
        int bottom = layout.getLineTop(line + 1);
        int left = (int) Math.floor((double) layout.getLineLeft(line));
        int right = (int) Math.ceil((double) layout.getLineRight(line));
        int ht = layout.getHeight();
        int hspace = ((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight();
        int vspace = ((this.mBottom - this.mTop) - getExtendedPaddingTop()) - getExtendedPaddingBottom();
        if (!this.mHorizontallyScrolling && right - left > hspace && right > x) {
            right = Math.max(x, left + hspace);
        }
        int hslack = (bottom - top) / 2;
        boolean changed2 = false;
        int vslack = hslack;
        if (vslack > vspace / 4) {
            vslack = vspace / 4;
        }
        if (hslack > hspace / 4) {
            hslack = hspace / 4;
        }
        i = this.mScrollX;
        int vs = this.mScrollY;
        if (top - vs < vslack) {
            vs = top - vslack;
        }
        int vs2 = vs;
        if (bottom - vs > vspace - vslack) {
            vs = bottom - (vspace - vslack);
        } else {
            vs = vs2;
        }
        if (ht - vs < vspace) {
            vs = ht - vspace;
        }
        if (0 - vs > 0) {
            vs = 0;
        }
        if (i2 != 0) {
            if (x - i < hslack) {
                i = x - hslack;
            }
            hs = i;
            if (x - i > hspace - hslack) {
                i = x - (hspace - hslack);
            } else {
                i = hs;
            }
        }
        if (i2 < 0) {
            if (left - i > 0) {
                i = left;
            }
            if (right - i < hspace) {
                i = right - hspace;
            }
        } else if (i2 > 0) {
            if (right - i < hspace) {
                i = right - hspace;
            }
            if (left - i > 0) {
                i = left;
            }
        } else if (right - left <= hspace) {
            i = left - ((hspace - (right - left)) / 2);
        } else if (x > right - hslack) {
            i = right - hspace;
        } else if (x < left + hslack) {
            i = left;
        } else if (left > i) {
            i = left;
        } else if (right < i + hspace) {
            i = right - hspace;
        } else {
            if (x - i < hslack) {
                i = x - hslack;
            }
            hs = i;
            if (x - i > hspace - hslack) {
                i = x - (hspace - hslack);
            } else {
                i = hs;
            }
        }
        int i3;
        int i4;
        int i5;
        int i6;
        if (i == this.mScrollX && vs == this.mScrollY) {
            i3 = i;
            vs2 = vslack;
            i4 = vspace;
            i5 = vs;
            i6 = i2;
            changed = changed2;
        } else {
            if (this.mScroller == null) {
                scrollTo(i, vs);
                i3 = i;
                vs2 = vslack;
                i4 = vspace;
                i5 = vs;
                i6 = i2;
            } else {
                vslack = i - this.mScrollX;
                vspace = vs - this.mScrollY;
                if (AnimationUtils.currentAnimationTimeMillis() - this.mLastScroll > 250) {
                    this.mScroller.startScroll(this.mScrollX, this.mScrollY, vslack, vspace);
                    awakenScrollBars(this.mScroller.getDuration());
                    invalidate();
                } else {
                    i5 = vs;
                    i6 = i2;
                    if (!this.mScroller.isFinished()) {
                        this.mScroller.abortAnimation();
                    }
                    scrollBy(vslack, vspace);
                }
                this.mLastScroll = AnimationUtils.currentAnimationTimeMillis();
            }
            changed = true;
        }
        if (isFocused()) {
            if (this.mTempRect == null) {
                this.mTempRect = new Rect();
            }
            this.mTempRect.set(x - 2, top, x + 2, bottom);
            getInterestingRect(this.mTempRect, line);
            this.mTempRect.offset(this.mScrollX, this.mScrollY);
            if (requestRectangleOnScreen(this.mTempRect)) {
                changed = true;
            }
        }
        return changed;
    }

    public boolean moveCursorToVisibleOffset() {
        if (!(this.mText instanceof Spannable)) {
            return false;
        }
        int start = getSelectionStart();
        if (start != getSelectionEnd()) {
            return false;
        }
        int line = this.mLayout.getLineForOffset(start);
        int top = this.mLayout.getLineTop(line);
        int bottom = this.mLayout.getLineTop(line + 1);
        int vspace = ((this.mBottom - this.mTop) - getExtendedPaddingTop()) - getExtendedPaddingBottom();
        int vslack = (bottom - top) / 2;
        if (vslack > vspace / 4) {
            vslack = vspace / 4;
        }
        int vs = this.mScrollY;
        if (top < vs + vslack) {
            line = this.mLayout.getLineForVertical((vs + vslack) + (bottom - top));
        } else if (bottom > (vspace + vs) - vslack) {
            line = this.mLayout.getLineForVertical(((vspace + vs) - vslack) - (bottom - top));
        }
        int hspace = ((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight();
        int hs = this.mScrollX;
        int leftChar = this.mLayout.getOffsetForHorizontal(line, (float) hs);
        int rightChar = this.mLayout.getOffsetForHorizontal(line, (float) (hspace + hs));
        int lowChar = leftChar < rightChar ? leftChar : rightChar;
        int highChar = leftChar > rightChar ? leftChar : rightChar;
        int newStart = start;
        if (newStart < lowChar) {
            newStart = lowChar;
        } else if (newStart > highChar) {
            newStart = highChar;
        }
        if (newStart != start) {
            Selection.setSelection(this.mSpannable, newStart);
            return true;
        }
        return false;
    }

    public void computeScroll() {
        Scroller scroller = this.mScroller;
        if (scroller != null && scroller.computeScrollOffset()) {
            this.mScrollX = this.mScroller.getCurrX();
            this.mScrollY = this.mScroller.getCurrY();
            invalidateParentCaches();
            postInvalidate();
        }
    }

    private void getInterestingRect(Rect r, int line) {
        convertFromViewportToContentCoordinates(r);
        if (line == 0) {
            r.top -= getExtendedPaddingTop();
        }
        if (line == this.mLayout.getLineCount() - 1) {
            r.bottom += getExtendedPaddingBottom();
        }
    }

    private void convertFromViewportToContentCoordinates(Rect r) {
        int horizontalOffset = viewportToContentHorizontalOffset();
        r.left += horizontalOffset;
        r.right += horizontalOffset;
        int verticalOffset = viewportToContentVerticalOffset();
        r.top += verticalOffset;
        r.bottom += verticalOffset;
    }

    /* Access modifiers changed, original: 0000 */
    public int viewportToContentHorizontalOffset() {
        return getCompoundPaddingLeft() - this.mScrollX;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public int viewportToContentVerticalOffset() {
        int offset = getExtendedPaddingTop() - this.mScrollY;
        if ((this.mGravity & 112) != 48) {
            return offset + getVerticalOffset(false);
        }
        return offset;
    }

    public void debug(int depth) {
        super.debug(depth);
        String output = View.debugIndent(depth);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(output);
        stringBuilder.append("frame={");
        stringBuilder.append(this.mLeft);
        String str = ", ";
        stringBuilder.append(str);
        stringBuilder.append(this.mTop);
        stringBuilder.append(str);
        stringBuilder.append(this.mRight);
        stringBuilder.append(str);
        stringBuilder.append(this.mBottom);
        stringBuilder.append("} scroll={");
        stringBuilder.append(this.mScrollX);
        stringBuilder.append(str);
        stringBuilder.append(this.mScrollY);
        stringBuilder.append("} ");
        output = stringBuilder.toString();
        if (this.mText != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(output);
            stringBuilder.append("mText=\"");
            stringBuilder.append(this.mText);
            stringBuilder.append("\" ");
            output = stringBuilder.toString();
            if (this.mLayout != null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(output);
                stringBuilder.append("mLayout width=");
                stringBuilder.append(this.mLayout.getWidth());
                stringBuilder.append(" height=");
                stringBuilder.append(this.mLayout.getHeight());
                output = stringBuilder.toString();
            }
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append(output);
            stringBuilder.append("mText=NULL");
            output = stringBuilder.toString();
        }
        Log.d("View", output);
    }

    @ExportedProperty(category = "text")
    public int getSelectionStart() {
        return Selection.getSelectionStart(getText());
    }

    @ExportedProperty(category = "text")
    public int getSelectionEnd() {
        return Selection.getSelectionEnd(getText());
    }

    public boolean hasSelection() {
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        return selectionStart >= 0 && selectionEnd > 0 && selectionStart != selectionEnd;
    }

    /* Access modifiers changed, original: 0000 */
    public String getSelectedText() {
        if (!hasSelection()) {
            return null;
        }
        int start = getSelectionStart();
        int end = getSelectionEnd();
        CharSequence charSequence = this.mText;
        return String.valueOf(start > end ? charSequence.subSequence(end, start) : charSequence.subSequence(start, end));
    }

    public void setSingleLine() {
        setSingleLine(true);
    }

    public void setAllCaps(boolean allCaps) {
        if (allCaps) {
            setTransformationMethod(new AllCapsTransformationMethod(getContext()));
        } else {
            setTransformationMethod(null);
        }
    }

    public boolean isAllCaps() {
        TransformationMethod method = getTransformationMethod();
        return method != null && (method instanceof AllCapsTransformationMethod);
    }

    @RemotableViewMethod
    public void setSingleLine(boolean singleLine) {
        setInputTypeSingleLine(singleLine);
        applySingleLine(singleLine, true, true);
    }

    private void setInputTypeSingleLine(boolean singleLine) {
        Editor editor = this.mEditor;
        if (editor != null && (editor.mInputType & 15) == 1) {
            if (singleLine) {
                editor = this.mEditor;
                editor.mInputType &= -131073;
                return;
            }
            editor = this.mEditor;
            editor.mInputType |= 131072;
        }
    }

    private void applySingleLine(boolean singleLine, boolean applyTransformation, boolean changeMaxLines) {
        this.mSingleLine = singleLine;
        if (singleLine) {
            setLines(1);
            setHorizontallyScrolling(true);
            if (applyTransformation) {
                setTransformationMethod(SingleLineTransformationMethod.getInstance());
                return;
            }
            return;
        }
        if (changeMaxLines) {
            setMaxLines(Integer.MAX_VALUE);
        }
        setHorizontallyScrolling(false);
        if (applyTransformation) {
            setTransformationMethod(null);
        }
    }

    public void setEllipsize(TruncateAt where) {
        if (this.mEllipsize != where) {
            this.mEllipsize = where;
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public void setMarqueeRepeatLimit(int marqueeLimit) {
        this.mMarqueeRepeatLimit = marqueeLimit;
    }

    public int getMarqueeRepeatLimit() {
        return this.mMarqueeRepeatLimit;
    }

    @ExportedProperty
    public TruncateAt getEllipsize() {
        return this.mEllipsize;
    }

    @RemotableViewMethod
    public void setSelectAllOnFocus(boolean selectAllOnFocus) {
        createEditorIfNeeded();
        this.mEditor.mSelectAllOnFocus = selectAllOnFocus;
        if (selectAllOnFocus) {
            CharSequence charSequence = this.mText;
            if (!(charSequence instanceof Spannable)) {
                setText(charSequence, BufferType.SPANNABLE);
            }
        }
    }

    @RemotableViewMethod
    public void setCursorVisible(boolean visible) {
        if (!visible || this.mEditor != null) {
            createEditorIfNeeded();
            if (this.mEditor.mCursorVisible != visible) {
                this.mEditor.mCursorVisible = visible;
                invalidate();
                this.mEditor.makeBlink();
                this.mEditor.prepareCursorControllers();
            }
        }
    }

    public boolean isCursorVisible() {
        Editor editor = this.mEditor;
        return editor == null ? true : editor.mCursorVisible;
    }

    private boolean canMarquee() {
        int width = ((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight();
        if (width <= 0) {
            return false;
        }
        if (this.mLayout.getLineWidth(0) <= ((float) width)) {
            if (this.mMarqueeFadeMode == 0) {
                return false;
            }
            Layout layout = this.mSavedMarqueeModeLayout;
            if (layout == null || layout.getLineWidth(0) <= ((float) width)) {
                return false;
            }
        }
        return true;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private void startMarquee() {
        if (getKeyListener() == null && !compressText((float) ((getWidth() - getCompoundPaddingLeft()) - getCompoundPaddingRight()))) {
            Marquee marquee = this.mMarquee;
            if ((marquee == null || marquee.isStopped()) && ((isFocused() || isSelected()) && getLineCount() == 1 && canMarquee())) {
                if (this.mMarqueeFadeMode == 1) {
                    this.mMarqueeFadeMode = 2;
                    Layout tmp = this.mLayout;
                    this.mLayout = this.mSavedMarqueeModeLayout;
                    this.mSavedMarqueeModeLayout = tmp;
                    setHorizontalFadingEdgeEnabled(true);
                    requestLayout();
                    invalidate();
                }
                if (this.mMarquee == null) {
                    this.mMarquee = new Marquee(this);
                }
                this.mMarquee.start(this.mMarqueeRepeatLimit);
            }
        }
    }

    private void stopMarquee() {
        Marquee marquee = this.mMarquee;
        if (!(marquee == null || marquee.isStopped())) {
            this.mMarquee.stop();
        }
        if (this.mMarqueeFadeMode == 2) {
            this.mMarqueeFadeMode = 1;
            Layout tmp = this.mSavedMarqueeModeLayout;
            this.mSavedMarqueeModeLayout = this.mLayout;
            this.mLayout = tmp;
            setHorizontalFadingEdgeEnabled(false);
            requestLayout();
            invalidate();
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private void startStopMarquee(boolean start) {
        if (this.mEllipsize != TruncateAt.MARQUEE) {
            return;
        }
        if (start) {
            startMarquee();
        } else {
            stopMarquee();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
    }

    /* Access modifiers changed, original: protected */
    public void onSelectionChanged(int selStart, int selEnd) {
        sendAccessibilityEvent(8192);
    }

    public void addTextChangedListener(TextWatcher watcher) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList();
        }
        this.mListeners.add(watcher);
    }

    public void removeTextChangedListener(TextWatcher watcher) {
        int i = this.mListeners;
        if (i != 0) {
            i = i.indexOf(watcher);
            if (i >= 0) {
                this.mListeners.remove(i);
            }
        }
    }

    private void sendBeforeTextChanged(CharSequence text, int start, int before, int after) {
        if (this.mListeners != null) {
            ArrayList<TextWatcher> list = this.mListeners;
            int count = list.size();
            for (int i = 0; i < count; i++) {
                ((TextWatcher) list.get(i)).beforeTextChanged(text, start, before, after);
            }
        }
        removeIntersectingNonAdjacentSpans(start, start + before, SpellCheckSpan.class);
        removeIntersectingNonAdjacentSpans(start, start + before, SuggestionSpan.class);
    }

    private <T> void removeIntersectingNonAdjacentSpans(int start, int end, Class<T> type) {
        Editable text = this.mText;
        if (text instanceof Editable) {
            text = text;
            T[] spans = text.getSpans(start, end, type);
            int length = spans.length;
            for (int i = 0; i < length; i++) {
                int spanStart = text.getSpanStart(spans[i]);
                if (text.getSpanEnd(spans[i]) == start || spanStart == end) {
                    break;
                }
                text.removeSpan(spans[i]);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void removeAdjacentSuggestionSpans(int pos) {
        Editable text = this.mText;
        if (text instanceof Editable) {
            text = text;
            SuggestionSpan[] spans = (SuggestionSpan[]) text.getSpans(pos, pos, SuggestionSpan.class);
            int length = spans.length;
            for (int i = 0; i < length; i++) {
                int spanStart = text.getSpanStart(spans[i]);
                int spanEnd = text.getSpanEnd(spans[i]);
                if ((spanEnd == pos || spanStart == pos) && SpellChecker.haveWordBoundariesChanged(text, pos, pos, spanStart, spanEnd)) {
                    text.removeSpan(spans[i]);
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void sendOnTextChanged(CharSequence text, int start, int before, int after) {
        if (this.mListeners != null) {
            ArrayList<TextWatcher> list = this.mListeners;
            int count = list.size();
            for (int i = 0; i < count; i++) {
                ((TextWatcher) list.get(i)).onTextChanged(text, start, before, after);
            }
        }
        Editor editor = this.mEditor;
        if (editor != null) {
            editor.sendOnTextChanged(start, before, after);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void sendAfterTextChanged(Editable text) {
        if (this.mListeners != null) {
            ArrayList<TextWatcher> list = this.mListeners;
            int count = list.size();
            for (int i = 0; i < count; i++) {
                ((TextWatcher) list.get(i)).afterTextChanged(text);
            }
        }
        notifyListeningManagersAfterTextChanged();
        hideErrorIfUnchanged();
    }

    private void notifyListeningManagersAfterTextChanged() {
        if (isAutofillable()) {
            AutofillManager afm = (AutofillManager) this.mContext.getSystemService(AutofillManager.class);
            if (afm != null) {
                if (Helper.sVerbose) {
                    Log.v(LOG_TAG, "notifyAutoFillManagerAfterTextChanged");
                }
                afm.notifyValueChanged(this);
            }
        }
    }

    private boolean isAutofillable() {
        return getAutofillType() != 0;
    }

    /* Access modifiers changed, original: 0000 */
    public void updateAfterEdit() {
        invalidate();
        int curs = getSelectionStart();
        if (curs >= 0 || (this.mGravity & 112) == 80) {
            registerForPreDraw();
        }
        checkForResize();
        if (curs >= 0) {
            this.mHighlightPathBogus = true;
            Editor editor = this.mEditor;
            if (editor != null) {
                editor.makeBlink();
            }
            bringPointIntoView(curs);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void handleTextChanged(CharSequence buffer, int start, int before, int after) {
        sLastCutCopyOrTextChangedTime = 0;
        Editor editor = this.mEditor;
        InputMethodState ims = editor == null ? null : editor.mInputMethodState;
        if (ims == null || ims.mBatchEditNesting == 0) {
            updateAfterEdit();
        }
        if (ims != null) {
            ims.mContentChanged = true;
            if (ims.mChangedStart < 0) {
                ims.mChangedStart = start;
                ims.mChangedEnd = start + before;
            } else {
                ims.mChangedStart = Math.min(ims.mChangedStart, start);
                ims.mChangedEnd = Math.max(ims.mChangedEnd, (start + before) - ims.mChangedDelta);
            }
            ims.mChangedDelta += after - before;
        }
        resetErrorChangedFlag();
        sendOnTextChanged(buffer, start, before, after);
        onTextChanged(buffer, start, before, after);
    }

    /* Access modifiers changed, original: 0000 */
    public void spanChange(Spanned buf, Object what, int oldStart, int newStart, int oldEnd, int newEnd) {
        Editor editor;
        Editor editor2;
        boolean selChanged = false;
        int newSelStart = -1;
        int newSelEnd = -1;
        InputMethodState ims = this.mEditor;
        ims = ims == null ? null : ims.mInputMethodState;
        if (what == Selection.SELECTION_END) {
            selChanged = true;
            newSelEnd = newStart;
            if (oldStart >= 0 || newStart >= 0) {
                invalidateCursor(Selection.getSelectionStart(buf), oldStart, newStart);
                checkForResize();
                registerForPreDraw();
                editor = this.mEditor;
                if (editor != null) {
                    editor.makeBlink();
                }
            }
        }
        if (what == Selection.SELECTION_START) {
            selChanged = true;
            newSelStart = newStart;
            if (oldStart >= 0 || newStart >= 0) {
                invalidateCursor(Selection.getSelectionEnd(buf), oldStart, newStart);
            }
        }
        if (selChanged) {
            this.mHighlightPathBogus = true;
            if (!(this.mEditor == null || isFocused())) {
                this.mEditor.mSelectionMoved = true;
            }
            if ((buf.getSpanFlags(what) & 512) == 0) {
                if (newSelStart < 0) {
                    newSelStart = Selection.getSelectionStart(buf);
                }
                if (newSelEnd < 0) {
                    newSelEnd = Selection.getSelectionEnd(buf);
                }
                editor2 = this.mEditor;
                if (editor2 != null) {
                    editor2.refreshTextActionMode();
                    if (!hasSelection() && this.mEditor.getTextActionMode() == null && hasTransientState()) {
                        setHasTransientState(false);
                    }
                }
                onSelectionChanged(newSelStart, newSelEnd);
            }
        }
        if ((what instanceof UpdateAppearance) || (what instanceof ParagraphStyle) || (what instanceof CharacterStyle)) {
            if (ims == null || ims.mBatchEditNesting == 0) {
                invalidate();
                this.mHighlightPathBogus = true;
                checkForResize();
            } else {
                ims.mContentChanged = true;
            }
            editor2 = this.mEditor;
            if (editor2 != null) {
                if (oldStart >= 0) {
                    editor2.invalidateTextDisplayList(this.mLayout, oldStart, oldEnd);
                }
                if (newStart >= 0) {
                    this.mEditor.invalidateTextDisplayList(this.mLayout, newStart, newEnd);
                }
                this.mEditor.invalidateHandlesAndActionMode();
            }
        }
        if (MetaKeyKeyListener.isMetaTracker(buf, what)) {
            this.mHighlightPathBogus = true;
            if (ims != null && MetaKeyKeyListener.isSelectingMetaTracker(buf, what)) {
                ims.mSelectionModeChanged = true;
            }
            if (Selection.getSelectionStart(buf) >= 0) {
                if (ims == null || ims.mBatchEditNesting == 0) {
                    invalidateCursor();
                } else {
                    ims.mCursorChanged = true;
                }
            }
        }
        if (!(!(what instanceof ParcelableSpan) || ims == null || ims.mExtractedTextRequest == null)) {
            if (ims.mBatchEditNesting != 0) {
                if (oldStart >= 0) {
                    if (ims.mChangedStart > oldStart) {
                        ims.mChangedStart = oldStart;
                    }
                    if (ims.mChangedStart > oldEnd) {
                        ims.mChangedStart = oldEnd;
                    }
                }
                if (newStart >= 0) {
                    if (ims.mChangedStart > newStart) {
                        ims.mChangedStart = newStart;
                    }
                    if (ims.mChangedStart > newEnd) {
                        ims.mChangedStart = newEnd;
                    }
                }
            } else {
                ims.mContentChanged = true;
            }
        }
        editor = this.mEditor;
        if (editor != null && editor.mSpellChecker != null && newStart < 0 && (what instanceof SpellCheckSpan)) {
            this.mEditor.mSpellChecker.onSpellCheckSpanRemoved((SpellCheckSpan) what);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (isTemporarilyDetached()) {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
            return;
        }
        Editor editor = this.mEditor;
        if (editor != null) {
            editor.onFocusChanged(focused, direction);
        }
        if (focused) {
            Spannable spannable = this.mSpannable;
            if (spannable != null) {
                MetaKeyKeyListener.resetMetaState(spannable);
            }
        }
        startStopMarquee(focused);
        TransformationMethod transformationMethod = this.mTransformation;
        if (transformationMethod != null) {
            transformationMethod.onFocusChanged(this, this.mText, focused, direction, previouslyFocusedRect);
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        Editor editor = this.mEditor;
        if (editor != null) {
            editor.onWindowFocusChanged(hasWindowFocus);
        }
        startStopMarquee(hasWindowFocus);
    }

    /* Access modifiers changed, original: protected */
    public void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        Editor editor = this.mEditor;
        if (editor != null && visibility != 0) {
            editor.hideCursorAndSpanControllers();
            stopTextActionMode();
        }
    }

    public void clearComposingText() {
        if (this.mText instanceof Spannable) {
            BaseInputConnection.removeComposingSpans(this.mSpannable);
        }
    }

    public void setSelected(boolean selected) {
        boolean wasSelected = isSelected();
        super.setSelected(selected);
        if (selected != wasSelected && this.mEllipsize == TruncateAt.MARQUEE) {
            if (selected) {
                startMarquee();
            } else {
                stopMarquee();
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:37:0x0070  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x009c  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00cc A:{RETURN} */
    public boolean onTouchEvent(android.view.MotionEvent r12) {
        /*
        r11 = this;
        r0 = r12.getActionMasked();
        r1 = r11.mEditor;
        r2 = 1;
        if (r1 == 0) goto L_0x001d;
    L_0x0009:
        r1.onTouchEvent(r12);
        r1 = r11.mEditor;
        r1 = r1.mSelectionModifierCursorController;
        if (r1 == 0) goto L_0x001d;
    L_0x0012:
        r1 = r11.mEditor;
        r1 = r1.mSelectionModifierCursorController;
        r1 = r1.isDragAcceleratorActive();
        if (r1 == 0) goto L_0x001d;
    L_0x001c:
        return r2;
    L_0x001d:
        r1 = super.onTouchEvent(r12);
        r3 = r11.mEditor;
        r4 = 0;
        if (r3 == 0) goto L_0x003e;
    L_0x0026:
        r3 = r3.mDiscardNextActionUp;
        if (r3 == 0) goto L_0x003e;
    L_0x002a:
        if (r0 != r2) goto L_0x003e;
    L_0x002c:
        r2 = r11.mEditor;
        r2.mDiscardNextActionUp = r4;
        r2 = r2.mIsInsertionActionModeStartPending;
        if (r2 == 0) goto L_0x003d;
    L_0x0034:
        r2 = r11.mEditor;
        r2.startInsertionActionMode();
        r2 = r11.mEditor;
        r2.mIsInsertionActionModeStartPending = r4;
    L_0x003d:
        return r1;
    L_0x003e:
        if (r0 != r2) goto L_0x0050;
    L_0x0040:
        r3 = r11.mEditor;
        if (r3 == 0) goto L_0x0048;
    L_0x0044:
        r3 = r3.mIgnoreActionUpEvent;
        if (r3 != 0) goto L_0x0050;
    L_0x0048:
        r3 = r11.isFocused();
        if (r3 == 0) goto L_0x0050;
    L_0x004e:
        r3 = r2;
        goto L_0x0051;
    L_0x0050:
        r3 = r4;
    L_0x0051:
        r5 = r11.mMovement;
        if (r5 != 0) goto L_0x005b;
    L_0x0055:
        r5 = r11.onCheckIsTextEditor();
        if (r5 == 0) goto L_0x00cd;
    L_0x005b:
        r5 = r11.isEnabled();
        if (r5 == 0) goto L_0x00cd;
    L_0x0061:
        r5 = r11.mText;
        r5 = r5 instanceof android.text.Spannable;
        if (r5 == 0) goto L_0x00cd;
    L_0x0067:
        r5 = r11.mLayout;
        if (r5 == 0) goto L_0x00cd;
    L_0x006b:
        r5 = 0;
        r6 = r11.mMovement;
        if (r6 == 0) goto L_0x0077;
    L_0x0070:
        r7 = r11.mSpannable;
        r6 = r6.onTouchEvent(r11, r7, r12);
        r5 = r5 | r6;
    L_0x0077:
        r6 = r11.isTextSelectable();
        if (r3 == 0) goto L_0x00a2;
    L_0x007d:
        r7 = r11.mLinksClickable;
        if (r7 == 0) goto L_0x00a2;
    L_0x0081:
        r7 = r11.mAutoLinkMask;
        if (r7 == 0) goto L_0x00a2;
    L_0x0085:
        if (r6 == 0) goto L_0x00a2;
    L_0x0087:
        r7 = r11.mSpannable;
        r8 = r11.getSelectionStart();
        r9 = r11.getSelectionEnd();
        r10 = android.text.style.ClickableSpan.class;
        r7 = r7.getSpans(r8, r9, r10);
        r7 = (android.text.style.ClickableSpan[]) r7;
        r8 = r7.length;
        if (r8 <= 0) goto L_0x00a2;
    L_0x009c:
        r8 = r7[r4];
        r8.onClick(r11);
        r5 = 1;
    L_0x00a2:
        if (r3 == 0) goto L_0x00ca;
    L_0x00a4:
        r7 = r11.isTextEditable();
        if (r7 != 0) goto L_0x00ac;
    L_0x00aa:
        if (r6 == 0) goto L_0x00ca;
    L_0x00ac:
        r7 = r11.getInputMethodManager();
        r11.viewClicked(r7);
        r8 = r11.isTextEditable();
        if (r8 == 0) goto L_0x00c4;
    L_0x00b9:
        r8 = r11.mEditor;
        r8 = r8.mShowSoftInputOnFocus;
        if (r8 == 0) goto L_0x00c4;
    L_0x00bf:
        if (r7 == 0) goto L_0x00c4;
    L_0x00c1:
        r7.showSoftInput(r11, r4);
    L_0x00c4:
        r4 = r11.mEditor;
        r4.onTouchUpEvent(r12);
        r5 = 1;
    L_0x00ca:
        if (r5 == 0) goto L_0x00cd;
    L_0x00cc:
        return r2;
    L_0x00cd:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.TextView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        MovementMethod movementMethod = this.mMovement;
        if (!(movementMethod == null || !(this.mText instanceof Spannable) || this.mLayout == null)) {
            try {
                if (movementMethod.onGenericMotionEvent(this, this.mSpannable, event)) {
                    return true;
                }
            } catch (AbstractMethodError e) {
            }
        }
        return super.onGenericMotionEvent(event);
    }

    /* Access modifiers changed, original: protected */
    public void onCreateContextMenu(ContextMenu menu) {
        Editor editor = this.mEditor;
        if (editor != null) {
            editor.onCreateContextMenu(menu);
        }
    }

    public boolean showContextMenu() {
        Editor editor = this.mEditor;
        if (editor != null) {
            editor.setContextMenuAnchor(Float.NaN, Float.NaN);
        }
        return super.showContextMenu();
    }

    public boolean showContextMenu(float x, float y) {
        Editor editor = this.mEditor;
        if (editor != null) {
            editor.setContextMenuAnchor(x, y);
        }
        return super.showContextMenu(x, y);
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public boolean isTextEditable() {
        return (this.mText instanceof Editable) && onCheckIsTextEditor() && isEnabled();
    }

    public boolean didTouchFocusSelect() {
        Editor editor = this.mEditor;
        return editor != null && editor.mTouchFocusSelected;
    }

    public void cancelLongPress() {
        super.cancelLongPress();
        Editor editor = this.mEditor;
        if (editor != null) {
            editor.mIgnoreActionUpEvent = true;
        }
    }

    public boolean onTrackballEvent(MotionEvent event) {
        MovementMethod movementMethod = this.mMovement;
        if (movementMethod != null) {
            Spannable spannable = this.mSpannable;
            if (!(spannable == null || this.mLayout == null || !movementMethod.onTrackballEvent(this, spannable, event))) {
                return true;
            }
        }
        return super.onTrackballEvent(event);
    }

    public void setScroller(Scroller s) {
        this.mScroller = s;
    }

    /* Access modifiers changed, original: protected */
    public float getLeftFadingEdgeStrength() {
        if (isMarqueeFadeEnabled()) {
            Marquee marquee = this.mMarquee;
            if (!(marquee == null || marquee.isStopped())) {
                marquee = this.mMarquee;
                if (marquee.shouldDrawLeftFade()) {
                    return getHorizontalFadingEdgeStrength(marquee.getScroll(), 0.0f);
                }
                return 0.0f;
            }
        }
        if (getLineCount() != 1) {
            return super.getLeftFadingEdgeStrength();
        }
        float lineLeft = getLayout().getLineLeft(0);
        if (lineLeft > ((float) this.mScrollX)) {
            return 0.0f;
        }
        return getHorizontalFadingEdgeStrength((float) this.mScrollX, lineLeft);
    }

    /* Access modifiers changed, original: protected */
    public float getRightFadingEdgeStrength() {
        if (isMarqueeFadeEnabled()) {
            Marquee marquee = this.mMarquee;
            if (!(marquee == null || marquee.isStopped())) {
                marquee = this.mMarquee;
                return getHorizontalFadingEdgeStrength(marquee.getMaxFadeScroll(), marquee.getScroll());
            }
        }
        if (getLineCount() != 1) {
            return super.getRightFadingEdgeStrength();
        }
        float rightEdge = (float) (this.mScrollX + ((getWidth() - getCompoundPaddingLeft()) - getCompoundPaddingRight()));
        float lineRight = getLayout().getLineRight(0);
        if (lineRight < rightEdge) {
            return 0.0f;
        }
        return getHorizontalFadingEdgeStrength(rightEdge, lineRight);
    }

    private float getHorizontalFadingEdgeStrength(float position1, float position2) {
        int horizontalFadingEdgeLength = getHorizontalFadingEdgeLength();
        if (horizontalFadingEdgeLength == 0) {
            return 0.0f;
        }
        float diff = Math.abs(position1 - position2);
        if (diff > ((float) horizontalFadingEdgeLength)) {
            return 1.0f;
        }
        return diff / ((float) horizontalFadingEdgeLength);
    }

    private boolean isMarqueeFadeEnabled() {
        return this.mEllipsize == TruncateAt.MARQUEE && this.mMarqueeFadeMode != 1;
    }

    /* Access modifiers changed, original: protected */
    public int computeHorizontalScrollRange() {
        Layout layout = this.mLayout;
        if (layout == null) {
            return super.computeHorizontalScrollRange();
        }
        int lineWidth = (this.mSingleLine && (this.mGravity & 7) == 3) ? (int) layout.getLineWidth(0) : this.mLayout.getWidth();
        return lineWidth;
    }

    /* Access modifiers changed, original: protected */
    public int computeVerticalScrollRange() {
        Layout layout = this.mLayout;
        if (layout != null) {
            return layout.getHeight();
        }
        return super.computeVerticalScrollRange();
    }

    /* Access modifiers changed, original: protected */
    public int computeVerticalScrollExtent() {
        return (getHeight() - getCompoundPaddingTop()) - getCompoundPaddingBottom();
    }

    public void findViewsWithText(ArrayList<View> outViews, CharSequence searched, int flags) {
        super.findViewsWithText(outViews, searched, flags);
        if (!outViews.contains(this) && (flags & 1) != 0 && !TextUtils.isEmpty(searched) && !TextUtils.isEmpty(this.mText)) {
            if (this.mText.toString().toLowerCase().contains(searched.toString().toLowerCase())) {
                outViews.add(this);
            }
        }
    }

    public static ColorStateList getTextColors(Context context, TypedArray attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(R.styleable.TextView);
            ColorStateList colors = a.getColorStateList(5);
            if (colors == null) {
                int ap = a.getResourceId(1, 0);
                if (ap != 0) {
                    TypedArray appearance = context.obtainStyledAttributes(ap, R.styleable.TextAppearance);
                    colors = appearance.getColorStateList(3);
                    appearance.recycle();
                }
            }
            a.recycle();
            return colors;
        }
        throw new NullPointerException();
    }

    public static int getTextColor(Context context, TypedArray attrs, int def) {
        ColorStateList colors = getTextColors(context, attrs);
        if (colors == null) {
            return def;
        }
        return colors.getDefaultColor();
    }

    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        if (event.hasModifiers(4096)) {
            if (keyCode != 29) {
                if (keyCode != 31) {
                    if (keyCode != 50) {
                        if (keyCode != 52) {
                            if (keyCode == 54 && canUndo()) {
                                return onTextContextMenuItem(16908338);
                            }
                        } else if (canCut()) {
                            return onTextContextMenuItem(16908320);
                        }
                    } else if (canPaste()) {
                        return onTextContextMenuItem(16908322);
                    }
                } else if (canCopy()) {
                    return onTextContextMenuItem(16908321);
                }
            } else if (canSelectText()) {
                return onTextContextMenuItem(16908319);
            }
        } else if (event.hasModifiers(4097)) {
            if (keyCode != 50) {
                if (keyCode == 54 && canRedo()) {
                    return onTextContextMenuItem(16908339);
                }
            } else if (canPaste()) {
                return onTextContextMenuItem(16908337);
            }
        }
        return super.onKeyShortcut(keyCode, event);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean canSelectText() {
        if (this.mText.length() != 0) {
            Editor editor = this.mEditor;
            if (editor != null && editor.hasSelectionController()) {
                return true;
            }
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean textCanBeSelected() {
        MovementMethod movementMethod = this.mMovement;
        boolean z = false;
        if (movementMethod == null || !movementMethod.canSelectArbitrarily()) {
            return false;
        }
        if (isTextEditable() || (isTextSelectable() && (this.mText instanceof Spannable) && isEnabled())) {
            z = true;
        }
        return z;
    }

    @UnsupportedAppUsage
    private Locale getTextServicesLocale(boolean allowNullLocale) {
        updateTextServicesLocaleAsync();
        if (this.mCurrentSpellCheckerLocaleCache != null || allowNullLocale) {
            return this.mCurrentSpellCheckerLocaleCache;
        }
        return Locale.getDefault();
    }

    public final void setTextOperationUser(UserHandle user) {
        if (!Objects.equals(this.mTextOperationUser, user)) {
            if (user == null || Process.myUserHandle().equals(user) || getContext().checkSelfPermission(permission.INTERACT_ACROSS_USERS_FULL) == 0) {
                this.mTextOperationUser = user;
                this.mCurrentSpellCheckerLocaleCache = null;
                Editor editor = this.mEditor;
                if (editor != null) {
                    editor.onTextOperationUserChanged();
                }
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("INTERACT_ACROSS_USERS_FULL is required. userId=");
            stringBuilder.append(user.getIdentifier());
            stringBuilder.append(" callingUserId");
            stringBuilder.append(UserHandle.myUserId());
            throw new SecurityException(stringBuilder.toString());
        }
    }

    /* Access modifiers changed, original: final */
    public final TextServicesManager getTextServicesManagerForUser() {
        return (TextServicesManager) getServiceManagerForUser("android", TextServicesManager.class);
    }

    /* Access modifiers changed, original: final */
    public final ClipboardManager getClipboardManagerForUser() {
        return (ClipboardManager) getServiceManagerForUser(getContext().getPackageName(), ClipboardManager.class);
    }

    /* Access modifiers changed, original: final */
    public final <T> T getServiceManagerForUser(String packageName, Class<T> managerClazz) {
        if (this.mTextOperationUser == null) {
            return getContext().getSystemService((Class) managerClazz);
        }
        try {
            return getContext().createPackageContextAsUser(packageName, 0, this.mTextOperationUser).getSystemService((Class) managerClazz);
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void startActivityAsTextOperationUserIfNecessary(Intent intent) {
        if (this.mTextOperationUser != null) {
            getContext().startActivityAsUser(intent, this.mTextOperationUser);
        } else {
            getContext().startActivity(intent);
        }
    }

    public Locale getTextServicesLocale() {
        return getTextServicesLocale(false);
    }

    public boolean isInExtractedMode() {
        return false;
    }

    private boolean isAutoSizeEnabled() {
        return supportsAutoSizeText() && this.mAutoSizeTextType != 0;
    }

    /* Access modifiers changed, original: protected */
    public boolean supportsAutoSizeText() {
        return true;
    }

    public Locale getSpellCheckerLocale() {
        return getTextServicesLocale(true);
    }

    private void updateTextServicesLocaleAsync() {
        AsyncTask.execute(new Runnable() {
            public void run() {
                TextView.this.updateTextServicesLocaleLocked();
            }
        });
    }

    @UnsupportedAppUsage
    private void updateTextServicesLocaleLocked() {
        TextServicesManager textServicesManager = getTextServicesManagerForUser();
        if (textServicesManager != null) {
            Locale locale;
            SpellCheckerSubtype subtype = textServicesManager.getCurrentSpellCheckerSubtype(true);
            if (subtype != null) {
                locale = subtype.getLocaleObject();
            } else {
                locale = null;
            }
            this.mCurrentSpellCheckerLocaleCache = locale;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void onLocaleChanged() {
        this.mEditor.onLocaleChanged();
    }

    public WordIterator getWordIterator() {
        Editor editor = this.mEditor;
        if (editor != null) {
            return editor.getWordIterator();
        }
        return null;
    }

    public void onPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        super.onPopulateAccessibilityEventInternal(event);
        CharSequence text = getTextForAccessibility();
        if (!TextUtils.isEmpty(text)) {
            event.getText().add(text);
        }
    }

    public CharSequence getAccessibilityClassName() {
        return TextView.class.getName();
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Missing block: B:55:0x0118, code skipped:
            if (r4 < r8.length()) goto L_0x011d;
     */
    public void onProvideStructure(android.view.ViewStructure r28, int r29, int r30) {
        /*
        r27 = this;
        r1 = r27;
        r2 = r28;
        r3 = r29;
        super.onProvideStructure(r28, r29, r30);
        r0 = r27.hasPasswordTransformationMethod();
        r5 = 1;
        if (r0 != 0) goto L_0x001d;
    L_0x0010:
        r0 = r27.getInputType();
        r0 = isPasswordInputType(r0);
        if (r0 == 0) goto L_0x001b;
    L_0x001a:
        goto L_0x001d;
    L_0x001b:
        r0 = 0;
        goto L_0x001e;
    L_0x001d:
        r0 = r5;
    L_0x001e:
        r6 = r0;
        if (r3 != r5) goto L_0x0065;
    L_0x0021:
        if (r3 != r5) goto L_0x0029;
    L_0x0023:
        r0 = r1.mTextSetFromXmlOrResourceId;
        r0 = r0 ^ r5;
        r2.setDataIsSensitive(r0);
    L_0x0029:
        r0 = r1.mTextId;
        if (r0 == 0) goto L_0x0065;
    L_0x002d:
        r0 = r27.getResources();	 Catch:{ NotFoundException -> 0x003b }
        r7 = r1.mTextId;	 Catch:{ NotFoundException -> 0x003b }
        r0 = r0.getResourceEntryName(r7);	 Catch:{ NotFoundException -> 0x003b }
        r2.setTextIdEntry(r0);	 Catch:{ NotFoundException -> 0x003b }
        goto L_0x0065;
    L_0x003b:
        r0 = move-exception;
        r7 = android.view.autofill.Helper.sVerbose;
        if (r7 == 0) goto L_0x0065;
    L_0x0040:
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = "onProvideAutofillStructure(): cannot set name for text id ";
        r7.append(r8);
        r8 = r1.mTextId;
        r7.append(r8);
        r8 = ": ";
        r7.append(r8);
        r8 = r0.getMessage();
        r7.append(r8);
        r7 = r7.toString();
        r8 = "TextView";
        android.util.Log.v(r8, r7);
    L_0x0065:
        if (r6 == 0) goto L_0x006e;
    L_0x0067:
        if (r3 != r5) goto L_0x006a;
    L_0x0069:
        goto L_0x006e;
    L_0x006a:
        r18 = r6;
        goto L_0x01c9;
    L_0x006e:
        r0 = r1.mLayout;
        if (r0 != 0) goto L_0x0075;
    L_0x0072:
        r27.assumeLayout();
    L_0x0075:
        r0 = r1.mLayout;
        r7 = r0.getLineCount();
        if (r7 > r5) goto L_0x0098;
    L_0x007d:
        r8 = r27.getText();
        if (r3 != r5) goto L_0x0087;
    L_0x0083:
        r2.setText(r8);
        goto L_0x0092;
    L_0x0087:
        r9 = r27.getSelectionStart();
        r10 = r27.getSelectionEnd();
        r2.setText(r8, r9, r10);
    L_0x0092:
        r18 = r6;
        r19 = r7;
        goto L_0x0163;
    L_0x0098:
        r8 = 2;
        r9 = new int[r8];
        r1.getLocationInWindow(r9);
        r10 = r9[r5];
        r11 = r27;
        r12 = r27.getParent();
    L_0x00a6:
        r13 = r12 instanceof android.view.View;
        if (r13 == 0) goto L_0x00b2;
    L_0x00aa:
        r11 = r12;
        r11 = (android.view.View) r11;
        r12 = r11.getParent();
        goto L_0x00a6;
    L_0x00b2:
        r13 = r11.getHeight();
        if (r10 < 0) goto L_0x00c5;
    L_0x00b8:
        r14 = 0;
        r14 = r1.getLineAtCoordinateUnclamped(r14);
        r15 = r13 + -1;
        r15 = (float) r15;
        r15 = r1.getLineAtCoordinateUnclamped(r15);
        goto L_0x00d3;
    L_0x00c5:
        r14 = -r10;
        r14 = (float) r14;
        r14 = r1.getLineAtCoordinateUnclamped(r14);
        r15 = r13 + -1;
        r15 = r15 - r10;
        r15 = (float) r15;
        r15 = r1.getLineAtCoordinateUnclamped(r15);
    L_0x00d3:
        r16 = r15 - r14;
        r16 = r16 / 2;
        r16 = r14 - r16;
        if (r16 >= 0) goto L_0x00e0;
    L_0x00db:
        r16 = 0;
        r4 = r16;
        goto L_0x00e2;
    L_0x00e0:
        r4 = r16;
    L_0x00e2:
        r16 = r15 - r14;
        r16 = r16 / 2;
        r8 = r15 + r16;
        if (r8 < r7) goto L_0x00ec;
    L_0x00ea:
        r8 = r7 + -1;
    L_0x00ec:
        r5 = r0.getLineStart(r4);
        r17 = r4;
        r4 = r0.getLineEnd(r8);
        r18 = r6;
        r6 = r27.getSelectionStart();
        r19 = r7;
        r7 = r27.getSelectionEnd();
        if (r6 >= r7) goto L_0x010a;
    L_0x0104:
        if (r6 >= r5) goto L_0x0107;
    L_0x0106:
        r5 = r6;
    L_0x0107:
        if (r7 <= r4) goto L_0x010a;
    L_0x0109:
        r4 = r7;
    L_0x010a:
        r20 = r8;
        r8 = r27.getText();
        if (r5 > 0) goto L_0x011b;
    L_0x0112:
        r21 = r9;
        r9 = r8.length();
        if (r4 >= r9) goto L_0x0121;
    L_0x011a:
        goto L_0x011d;
    L_0x011b:
        r21 = r9;
    L_0x011d:
        r8 = r8.subSequence(r5, r4);
    L_0x0121:
        r9 = 1;
        if (r3 != r9) goto L_0x0128;
    L_0x0124:
        r2.setText(r8);
        goto L_0x0163;
    L_0x0128:
        r9 = r6 - r5;
        r22 = r4;
        r4 = r7 - r5;
        r2.setText(r8, r9, r4);
        r4 = r15 - r14;
        r9 = 1;
        r4 = r4 + r9;
        r4 = new int[r4];
        r16 = r15 - r14;
        r23 = r5;
        r5 = r16 + 1;
        r5 = new int[r5];
        r9 = r27.getBaselineOffset();
        r24 = r14;
        r25 = r6;
        r6 = r24;
    L_0x0149:
        if (r6 > r15) goto L_0x0160;
    L_0x014b:
        r24 = r6 - r14;
        r26 = r0.getLineStart(r6);
        r4[r24] = r26;
        r24 = r6 - r14;
        r26 = r0.getLineBaseline(r6);
        r26 = r26 + r9;
        r5[r24] = r26;
        r6 = r6 + 1;
        goto L_0x0149;
    L_0x0160:
        r2.setTextLines(r4, r5);
    L_0x0163:
        if (r3 != 0) goto L_0x019b;
    L_0x0165:
        r4 = 0;
        r5 = r27.getTypefaceStyle();
        r6 = r5 & 1;
        if (r6 == 0) goto L_0x0170;
    L_0x016e:
        r4 = r4 | 1;
    L_0x0170:
        r6 = r5 & 2;
        if (r6 == 0) goto L_0x0176;
    L_0x0174:
        r4 = r4 | 2;
    L_0x0176:
        r6 = r1.mTextPaint;
        r6 = r6.getFlags();
        r7 = r6 & 32;
        if (r7 == 0) goto L_0x0182;
    L_0x0180:
        r4 = r4 | 1;
    L_0x0182:
        r7 = r6 & 8;
        if (r7 == 0) goto L_0x0188;
    L_0x0186:
        r4 = r4 | 4;
    L_0x0188:
        r7 = r6 & 16;
        if (r7 == 0) goto L_0x018e;
    L_0x018c:
        r4 = r4 | 8;
    L_0x018e:
        r7 = r27.getTextSize();
        r8 = r27.getCurrentTextColor();
        r9 = 1;
        r2.setTextStyle(r7, r8, r9, r4);
        goto L_0x019c;
    L_0x019b:
        r9 = 1;
    L_0x019c:
        if (r3 != r9) goto L_0x01c9;
    L_0x019e:
        r4 = r27.getMinEms();
        r2.setMinTextEms(r4);
        r4 = r27.getMaxEms();
        r2.setMaxTextEms(r4);
        r4 = -1;
        r5 = r27.getFilters();
        r6 = r5.length;
        r7 = 0;
    L_0x01b3:
        if (r7 >= r6) goto L_0x01c6;
    L_0x01b5:
        r8 = r5[r7];
        r9 = r8 instanceof android.text.InputFilter.LengthFilter;
        if (r9 == 0) goto L_0x01c3;
    L_0x01bb:
        r5 = r8;
        r5 = (android.text.InputFilter.LengthFilter) r5;
        r4 = r5.getMax();
        goto L_0x01c6;
    L_0x01c3:
        r7 = r7 + 1;
        goto L_0x01b3;
    L_0x01c6:
        r2.setMaxTextLength(r4);
    L_0x01c9:
        r0 = r27.getHint();
        r2.setHint(r0);
        r0 = r27.getInputType();
        r2.setInputType(r0);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.TextView.onProvideStructure(android.view.ViewStructure, int, int):void");
    }

    /* Access modifiers changed, original: 0000 */
    public boolean canRequestAutofill() {
        if (!isAutofillable()) {
            return false;
        }
        AutofillManager afm = (AutofillManager) this.mContext.getSystemService(AutofillManager.class);
        if (afm != null) {
            return afm.isEnabled();
        }
        return false;
    }

    private void requestAutofill() {
        AutofillManager afm = (AutofillManager) this.mContext.getSystemService(AutofillManager.class);
        if (afm != null) {
            afm.requestAutofill(this);
        }
    }

    public void autofill(AutofillValue value) {
        if (value.isText() && isTextEditable()) {
            setText(value.getTextValue(), this.mBufferType, true, 0);
            CharSequence text = getText();
            if (text instanceof Spannable) {
                Selection.setSelection((Spannable) text, text.length());
            }
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(value);
        stringBuilder.append(" could not be autofilled into ");
        stringBuilder.append(this);
        Log.w(LOG_TAG, stringBuilder.toString());
    }

    public int getAutofillType() {
        return isTextEditable();
    }

    public AutofillValue getAutofillValue() {
        if (isTextEditable()) {
            return AutofillValue.forText(TextUtils.trimToParcelableSize(getText()));
        }
        return null;
    }

    public void onInitializeAccessibilityEventInternal(AccessibilityEvent event) {
        super.onInitializeAccessibilityEventInternal(event);
        event.setPassword(hasPasswordTransformationMethod());
        if (event.getEventType() == 8192) {
            event.setFromIndex(Selection.getSelectionStart(this.mText));
            event.setToIndex(Selection.getSelectionEnd(this.mText));
            event.setItemCount(this.mText.length());
        }
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        info.setPassword(hasPasswordTransformationMethod());
        info.setText(getTextForAccessibility());
        info.setHintText(this.mHint);
        info.setShowingHintText(isShowingHint());
        if (this.mBufferType == BufferType.EDITABLE) {
            info.setEditable(true);
            if (isEnabled()) {
                info.addAction(AccessibilityAction.ACTION_SET_TEXT);
            }
        }
        Editor editor = this.mEditor;
        if (editor != null) {
            info.setInputType(editor.mInputType);
            if (this.mEditor.mError != null) {
                info.setContentInvalid(true);
                info.setError(this.mEditor.mError);
            }
        }
        if (!TextUtils.isEmpty(this.mText)) {
            info.addAction(256);
            info.addAction(512);
            info.setMovementGranularities(31);
            info.addAction(131072);
            info.setAvailableExtraData(Arrays.asList(new String[]{AccessibilityNodeInfo.EXTRA_DATA_TEXT_CHARACTER_LOCATION_KEY}));
        }
        if (isFocused()) {
            if (canCopy()) {
                info.addAction(16384);
            }
            if (canPaste()) {
                info.addAction(32768);
            }
            if (canCut()) {
                info.addAction(65536);
            }
            if (canShare()) {
                info.addAction(new AccessibilityAction(268435456, getResources().getString(com.android.internal.R.string.share)));
            }
            if (canProcessText()) {
                this.mEditor.mProcessTextIntentActionsHandler.onInitializeAccessibilityNodeInfo(info);
            }
        }
        for (InputFilter filter : this.mFilters) {
            if (filter instanceof LengthFilter) {
                info.setMaxTextLength(((LengthFilter) filter).getMax());
            }
        }
        if (!isSingleLine()) {
            info.setMultiLine(true);
        }
    }

    public void addExtraDataToAccessibilityNodeInfo(AccessibilityNodeInfo info, String extraDataKey, Bundle arguments) {
        if (arguments != null && extraDataKey.equals(AccessibilityNodeInfo.EXTRA_DATA_TEXT_CHARACTER_LOCATION_KEY)) {
            int positionInfoStartIndex = arguments.getInt(AccessibilityNodeInfo.EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_START_INDEX, -1);
            int positionInfoLength = arguments.getInt(AccessibilityNodeInfo.EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_LENGTH, -1);
            if (positionInfoLength <= 0 || positionInfoStartIndex < 0 || positionInfoStartIndex >= this.mText.length()) {
                Log.e(LOG_TAG, "Invalid arguments for accessibility character locations");
                return;
            }
            RectF[] boundingRects = new RectF[positionInfoLength];
            CursorAnchorInfo.Builder builder = new CursorAnchorInfo.Builder();
            populateCharacterBounds(builder, positionInfoStartIndex, positionInfoStartIndex + positionInfoLength, (float) viewportToContentHorizontalOffset(), (float) viewportToContentVerticalOffset());
            CursorAnchorInfo cursorAnchorInfo = builder.setMatrix(null).build();
            for (int i = 0; i < positionInfoLength; i++) {
                if ((cursorAnchorInfo.getCharacterBoundsFlags(positionInfoStartIndex + i) & 1) == 1) {
                    RectF bounds = cursorAnchorInfo.getCharacterBounds(positionInfoStartIndex + i);
                    if (bounds != null) {
                        mapRectFromViewToScreenCoords(bounds, true);
                        boundingRects[i] = bounds;
                    }
                }
            }
            info.getExtras().putParcelableArray(extraDataKey, boundingRects);
        }
    }

    public void populateCharacterBounds(CursorAnchorInfo.Builder builder, int startIndex, int endIndex, float viewportToContentHorizontalOffset, float viewportToContentVerticalOffset) {
        int i = startIndex;
        int i2 = endIndex;
        int minLine = this.mLayout.getLineForOffset(i);
        int maxLine = this.mLayout.getLineForOffset(i2 - 1);
        int line = minLine;
        while (line <= maxLine) {
            int lineStart = this.mLayout.getLineStart(line);
            int lineEnd = this.mLayout.getLineEnd(line);
            int offsetStart = Math.max(lineStart, i);
            int offsetEnd = Math.min(lineEnd, i2);
            boolean z = true;
            if (this.mLayout.getParagraphDirection(line) != 1) {
                z = false;
            }
            boolean ltrLine = z;
            float[] widths = new float[(offsetEnd - offsetStart)];
            this.mLayout.getPaint().getTextWidths(this.mTransformed, offsetStart, offsetEnd, widths);
            float top = (float) this.mLayout.getLineTop(line);
            float bottom = (float) this.mLayout.getLineBottom(line);
            int offset = offsetStart;
            while (offset < offsetEnd) {
                float left;
                float right;
                float charWidth = widths[offset - offsetStart];
                boolean isRtl = this.mLayout.isRtlCharAt(offset);
                float primary = this.mLayout.getPrimaryHorizontal(offset);
                float secondary = this.mLayout.getSecondaryHorizontal(offset);
                if (ltrLine) {
                    if (isRtl) {
                        left = secondary - charWidth;
                        right = secondary;
                    } else {
                        left = primary;
                        right = primary + charWidth;
                    }
                } else if (isRtl) {
                    left = primary - charWidth;
                    right = primary;
                } else {
                    left = secondary;
                    right = secondary + charWidth;
                }
                float localLeft = left + viewportToContentHorizontalOffset;
                float localRight = right + viewportToContentHorizontalOffset;
                float localTop = top + viewportToContentVerticalOffset;
                int minLine2 = minLine;
                minLine = bottom + viewportToContentVerticalOffset;
                boolean isTopLeftVisible = isPositionVisible(localLeft, localTop);
                boolean isBottomRightVisible = isPositionVisible(localRight, minLine);
                int characterBoundsFlags = 0;
                if (isTopLeftVisible || isBottomRightVisible) {
                    characterBoundsFlags = 0 | 1;
                }
                if (!(isTopLeftVisible && isBottomRightVisible)) {
                    characterBoundsFlags |= 2;
                }
                if (isRtl) {
                    characterBoundsFlags |= 4;
                }
                float localLeft2 = localLeft;
                builder.addCharacterBounds(offset, localLeft2, localTop, localRight, minLine, characterBoundsFlags);
                offset++;
                i = startIndex;
                i2 = endIndex;
                minLine = minLine2;
            }
            line++;
            i = startIndex;
            i2 = endIndex;
        }
    }

    /* JADX WARNING: Missing block: B:25:0x007d, code skipped:
            return false;
     */
    public boolean isPositionVisible(float r8, float r9) {
        /*
        r7 = this;
        r0 = TEMP_POSITION;
        monitor-enter(r0);
        r1 = TEMP_POSITION;	 Catch:{ all -> 0x0080 }
        r2 = 0;
        r1[r2] = r8;	 Catch:{ all -> 0x0080 }
        r3 = 1;
        r1[r3] = r9;	 Catch:{ all -> 0x0080 }
        r4 = r7;
    L_0x000c:
        if (r4 == 0) goto L_0x007e;
    L_0x000e:
        if (r4 == r7) goto L_0x0024;
    L_0x0010:
        r5 = r1[r2];	 Catch:{ all -> 0x0080 }
        r6 = r4.getScrollX();	 Catch:{ all -> 0x0080 }
        r6 = (float) r6;	 Catch:{ all -> 0x0080 }
        r5 = r5 - r6;
        r1[r2] = r5;	 Catch:{ all -> 0x0080 }
        r5 = r1[r3];	 Catch:{ all -> 0x0080 }
        r6 = r4.getScrollY();	 Catch:{ all -> 0x0080 }
        r6 = (float) r6;	 Catch:{ all -> 0x0080 }
        r5 = r5 - r6;
        r1[r3] = r5;	 Catch:{ all -> 0x0080 }
    L_0x0024:
        r5 = r1[r2];	 Catch:{ all -> 0x0080 }
        r6 = 0;
        r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1));
        if (r5 < 0) goto L_0x007c;
    L_0x002b:
        r5 = r1[r3];	 Catch:{ all -> 0x0080 }
        r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1));
        if (r5 < 0) goto L_0x007c;
    L_0x0031:
        r5 = r1[r2];	 Catch:{ all -> 0x0080 }
        r6 = r4.getWidth();	 Catch:{ all -> 0x0080 }
        r6 = (float) r6;	 Catch:{ all -> 0x0080 }
        r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1));
        if (r5 > 0) goto L_0x007c;
    L_0x003c:
        r5 = r1[r3];	 Catch:{ all -> 0x0080 }
        r6 = r4.getHeight();	 Catch:{ all -> 0x0080 }
        r6 = (float) r6;	 Catch:{ all -> 0x0080 }
        r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1));
        if (r5 <= 0) goto L_0x0048;
    L_0x0047:
        goto L_0x007c;
    L_0x0048:
        r5 = r4.getMatrix();	 Catch:{ all -> 0x0080 }
        r5 = r5.isIdentity();	 Catch:{ all -> 0x0080 }
        if (r5 != 0) goto L_0x0059;
    L_0x0052:
        r5 = r4.getMatrix();	 Catch:{ all -> 0x0080 }
        r5.mapPoints(r1);	 Catch:{ all -> 0x0080 }
    L_0x0059:
        r5 = r1[r2];	 Catch:{ all -> 0x0080 }
        r6 = r4.getLeft();	 Catch:{ all -> 0x0080 }
        r6 = (float) r6;	 Catch:{ all -> 0x0080 }
        r5 = r5 + r6;
        r1[r2] = r5;	 Catch:{ all -> 0x0080 }
        r5 = r1[r3];	 Catch:{ all -> 0x0080 }
        r6 = r4.getTop();	 Catch:{ all -> 0x0080 }
        r6 = (float) r6;	 Catch:{ all -> 0x0080 }
        r5 = r5 + r6;
        r1[r3] = r5;	 Catch:{ all -> 0x0080 }
        r5 = r4.getParent();	 Catch:{ all -> 0x0080 }
        r6 = r5 instanceof android.view.View;	 Catch:{ all -> 0x0080 }
        if (r6 == 0) goto L_0x007a;
    L_0x0075:
        r6 = r5;
        r6 = (android.view.View) r6;	 Catch:{ all -> 0x0080 }
        r4 = r6;
        goto L_0x007b;
    L_0x007a:
        r4 = 0;
    L_0x007b:
        goto L_0x000c;
    L_0x007c:
        monitor-exit(r0);	 Catch:{ all -> 0x0080 }
        return r2;
    L_0x007e:
        monitor-exit(r0);	 Catch:{ all -> 0x0080 }
        return r3;
    L_0x0080:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0080 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.TextView.isPositionVisible(float, float):boolean");
    }

    public boolean performAccessibilityActionInternal(int action, Bundle arguments) {
        Editor editor = this.mEditor;
        if (editor != null && editor.mProcessTextIntentActionsHandler.performAccessibilityAction(action)) {
            return true;
        }
        if (action == 16) {
            return performAccessibilityActionClick(arguments);
        }
        if (action == 256 || action == 512) {
            ensureIterableTextForAccessibilitySelectable();
            return super.performAccessibilityActionInternal(action, arguments);
        } else if (action != 16384) {
            if (action != 32768) {
                if (action != 65536) {
                    CharSequence text;
                    if (action == 131072) {
                        ensureIterableTextForAccessibilitySelectable();
                        text = getIterableTextForAccessibility();
                        if (text == null) {
                            return false;
                        }
                        int start;
                        int end;
                        if (arguments != null) {
                            start = arguments.getInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_START_INT, -1);
                        } else {
                            start = -1;
                        }
                        if (arguments != null) {
                            end = arguments.getInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_END_INT, -1);
                        } else {
                            end = -1;
                        }
                        if (!(getSelectionStart() == start && getSelectionEnd() == end)) {
                            if (start == end && end == -1) {
                                Selection.removeSelection((Spannable) text);
                                return true;
                            } else if (start >= 0 && start <= end && end <= text.length()) {
                                Selection.setSelection((Spannable) text, start, end);
                                Editor editor2 = this.mEditor;
                                if (editor2 != null) {
                                    editor2.startSelectionActionModeAsync(false);
                                }
                                return true;
                            }
                        }
                        return false;
                    } else if (action != 2097152) {
                        if (action != 268435456) {
                            return super.performAccessibilityActionInternal(action, arguments);
                        }
                        if (isFocused() && canShare() && onTextContextMenuItem(16908341)) {
                            return true;
                        }
                        return false;
                    } else if (!isEnabled() || this.mBufferType != BufferType.EDITABLE) {
                        return false;
                    } else {
                        if (arguments != null) {
                            text = arguments.getCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE);
                        } else {
                            text = null;
                        }
                        setText(text);
                        int updatedTextLength = this.mText;
                        if (updatedTextLength != 0) {
                            updatedTextLength = updatedTextLength.length();
                            if (updatedTextLength > 0) {
                                Selection.setSelection(this.mSpannable, updatedTextLength);
                            }
                        }
                        return true;
                    }
                } else if (isFocused() && canCut() && onTextContextMenuItem(16908320)) {
                    return true;
                } else {
                    return false;
                }
            } else if (isFocused() && canPaste() && onTextContextMenuItem(16908322)) {
                return true;
            } else {
                return false;
            }
        } else if (isFocused() && canCopy() && onTextContextMenuItem(16908321)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean performAccessibilityActionClick(Bundle arguments) {
        boolean handled = false;
        if (!isEnabled()) {
            return false;
        }
        if (isClickable() || isLongClickable()) {
            if (isFocusable() && !isFocused()) {
                requestFocus();
            }
            performClick();
            handled = true;
        }
        if ((this.mMovement != null || onCheckIsTextEditor()) && hasSpannableText() && this.mLayout != null && ((isTextEditable() || isTextSelectable()) && isFocused())) {
            InputMethodManager imm = getInputMethodManager();
            viewClicked(imm);
            if (!(isTextSelectable() || !this.mEditor.mShowSoftInputOnFocus || imm == null)) {
                handled |= imm.showSoftInput(this, 0);
            }
        }
        return handled;
    }

    private boolean hasSpannableText() {
        CharSequence charSequence = this.mText;
        return charSequence != null && (charSequence instanceof Spannable);
    }

    public void sendAccessibilityEventInternal(int eventType) {
        if (eventType == 32768) {
            Editor editor = this.mEditor;
            if (editor != null) {
                editor.mProcessTextIntentActionsHandler.initializeAccessibilityActions();
            }
        }
        super.sendAccessibilityEventInternal(eventType);
    }

    public void sendAccessibilityEventUnchecked(AccessibilityEvent event) {
        if (event.getEventType() != 4096) {
            super.sendAccessibilityEventUnchecked(event);
        }
    }

    @UnsupportedAppUsage
    private CharSequence getTextForAccessibility() {
        if (TextUtils.isEmpty(this.mText)) {
            return this.mHint;
        }
        return TextUtils.trimToParcelableSize(this.mTransformed);
    }

    /* Access modifiers changed, original: 0000 */
    public void sendAccessibilityEventTypeViewTextChanged(CharSequence beforeText, int fromIndex, int removedCount, int addedCount) {
        AccessibilityEvent event = AccessibilityEvent.obtain(16);
        event.setFromIndex(fromIndex);
        event.setRemovedCount(removedCount);
        event.setAddedCount(addedCount);
        event.setBeforeText(beforeText);
        sendAccessibilityEventUnchecked(event);
    }

    private InputMethodManager getInputMethodManager() {
        return (InputMethodManager) getContext().getSystemService(InputMethodManager.class);
    }

    public boolean isInputMethodTarget() {
        InputMethodManager imm = getInputMethodManager();
        return imm != null && imm.isActive(this);
    }

    public boolean onTextContextMenuItem(int id) {
        int min = 0;
        int max = this.mText.length();
        if (isFocused()) {
            int selStart = getSelectionStart();
            int selEnd = getSelectionEnd();
            min = Math.max(0, Math.min(selStart, selEnd));
            max = Math.max(0, Math.max(selStart, selEnd));
        }
        if (id != 16908355) {
            switch (id) {
                case 16908319:
                    boolean hadSelection = hasSelection();
                    selectAllText();
                    Editor editor = this.mEditor;
                    if (editor != null && hadSelection) {
                        editor.invalidateActionModeAsync();
                    }
                    return true;
                case 16908320:
                    if (setPrimaryClip(ClipData.newPlainText(null, getTransformedText(min, max)))) {
                        deleteText_internal(min, max);
                    } else {
                        Toast.makeText(getContext(), (int) com.android.internal.R.string.failed_to_copy_to_clipboard, 0).show();
                    }
                    return true;
                case 16908321:
                    int selStart2 = getSelectionStart();
                    int selEnd2 = getSelectionEnd();
                    if (setPrimaryClip(ClipData.newPlainText(null, getTransformedText(Math.max(0, Math.min(selStart2, selEnd2)), Math.max(0, Math.max(selStart2, selEnd2)))))) {
                        stopTextActionMode();
                    } else {
                        Toast.makeText(getContext(), (int) com.android.internal.R.string.failed_to_copy_to_clipboard, 0).show();
                    }
                    return true;
                case 16908322:
                    paste(min, max, true);
                    return true;
                default:
                    Editor editor2;
                    switch (id) {
                        case 16908337:
                            paste(min, max, false);
                            return true;
                        case 16908338:
                            editor2 = this.mEditor;
                            if (editor2 != null) {
                                editor2.undo();
                            }
                            return true;
                        case 16908339:
                            editor2 = this.mEditor;
                            if (editor2 != null) {
                                editor2.redo();
                            }
                            return true;
                        case 16908340:
                            editor2 = this.mEditor;
                            if (editor2 != null) {
                                editor2.replace();
                            }
                            return true;
                        case 16908341:
                            shareSelectedText();
                            return true;
                        default:
                            return false;
                    }
            }
        }
        requestAutofill();
        stopTextActionMode();
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public CharSequence getTransformedText(int start, int end) {
        return removeSuggestionSpans(this.mTransformed.subSequence(start, end));
    }

    public boolean performLongClick() {
        boolean handled = false;
        boolean performedHapticFeedback = false;
        Editor editor = this.mEditor;
        if (editor != null) {
            editor.mIsBeingLongClicked = true;
        }
        if (super.performLongClick()) {
            handled = true;
            performedHapticFeedback = true;
        }
        editor = this.mEditor;
        if (editor != null) {
            handled |= editor.performLongClick(handled);
            this.mEditor.mIsBeingLongClicked = false;
        }
        if (handled) {
            if (!performedHapticFeedback) {
                performHapticFeedback(0);
            }
            editor = this.mEditor;
            if (editor != null) {
                editor.mDiscardNextActionUp = true;
            }
        } else {
            MetricsLogger.action(this.mContext, (int) MetricsEvent.TEXT_LONGPRESS, 0);
        }
        return handled;
    }

    /* Access modifiers changed, original: protected */
    public void onScrollChanged(int horiz, int vert, int oldHoriz, int oldVert) {
        super.onScrollChanged(horiz, vert, oldHoriz, oldVert);
        Editor editor = this.mEditor;
        if (editor != null) {
            editor.onScrollChanged();
        }
    }

    public boolean isSuggestionsEnabled() {
        Editor editor = this.mEditor;
        boolean z = false;
        if (editor == null || (editor.mInputType & 15) != 1 || (this.mEditor.mInputType & 524288) > 0) {
            return false;
        }
        int variation = this.mEditor.mInputType & InputType.TYPE_MASK_VARIATION;
        if (variation == 0 || variation == 48 || variation == 80 || variation == 64 || variation == 160) {
            z = true;
        }
        return z;
    }

    public void setCustomSelectionActionModeCallback(Callback actionModeCallback) {
        createEditorIfNeeded();
        this.mEditor.mCustomSelectionActionModeCallback = actionModeCallback;
    }

    public Callback getCustomSelectionActionModeCallback() {
        Editor editor = this.mEditor;
        return editor == null ? null : editor.mCustomSelectionActionModeCallback;
    }

    public void setCustomInsertionActionModeCallback(Callback actionModeCallback) {
        createEditorIfNeeded();
        this.mEditor.mCustomInsertionActionModeCallback = actionModeCallback;
    }

    public Callback getCustomInsertionActionModeCallback() {
        Editor editor = this.mEditor;
        return editor == null ? null : editor.mCustomInsertionActionModeCallback;
    }

    public void setTextClassifier(TextClassifier textClassifier) {
        this.mTextClassifier = textClassifier;
    }

    public TextClassifier getTextClassifier() {
        TextClassificationManager tcm = this.mTextClassifier;
        if (tcm != null) {
            return tcm;
        }
        tcm = (TextClassificationManager) this.mContext.getSystemService(TextClassificationManager.class);
        if (tcm != null) {
            return tcm.getTextClassifier();
        }
        return TextClassifier.NO_OP;
    }

    /* Access modifiers changed, original: 0000 */
    public TextClassifier getTextClassificationSession() {
        TextClassifier textClassifier = this.mTextClassificationSession;
        if (textClassifier == null || textClassifier.isDestroyed()) {
            TextClassificationManager tcm = (TextClassificationManager) this.mContext.getSystemService(TextClassificationManager.class);
            if (tcm != null) {
                String widgetType;
                if (isTextEditable()) {
                    widgetType = TextClassifier.WIDGET_TYPE_EDITTEXT;
                } else if (isTextSelectable()) {
                    widgetType = TextClassifier.WIDGET_TYPE_TEXTVIEW;
                } else {
                    widgetType = TextClassifier.WIDGET_TYPE_UNSELECTABLE_TEXTVIEW;
                }
                this.mTextClassificationContext = new TextClassificationContext.Builder(this.mContext.getPackageName(), widgetType).build();
                TextClassifier textClassifier2 = this.mTextClassifier;
                if (textClassifier2 != null) {
                    this.mTextClassificationSession = tcm.createTextClassificationSession(this.mTextClassificationContext, textClassifier2);
                } else {
                    this.mTextClassificationSession = tcm.createTextClassificationSession(this.mTextClassificationContext);
                }
            } else {
                this.mTextClassificationSession = TextClassifier.NO_OP;
            }
        }
        return this.mTextClassificationSession;
    }

    /* Access modifiers changed, original: 0000 */
    public TextClassificationContext getTextClassificationContext() {
        return this.mTextClassificationContext;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean usesNoOpTextClassifier() {
        return getTextClassifier() == TextClassifier.NO_OP;
    }

    public boolean requestActionMode(TextLinkSpan clickedSpan) {
        Preconditions.checkNotNull(clickedSpan);
        CharSequence charSequence = this.mText;
        if (!(charSequence instanceof Spanned)) {
            return false;
        }
        int start = ((Spanned) charSequence).getSpanStart(clickedSpan);
        int end = ((Spanned) this.mText).getSpanEnd(clickedSpan);
        if (start < 0 || end > this.mText.length() || start >= end) {
            return false;
        }
        createEditorIfNeeded();
        this.mEditor.startLinkActionModeAsync(start, end);
        return true;
    }

    public boolean handleClick(TextLinkSpan clickedSpan) {
        Preconditions.checkNotNull(clickedSpan);
        Spanned spanned = this.mText;
        if (spanned instanceof Spanned) {
            spanned = spanned;
            int start = spanned.getSpanStart(clickedSpan);
            int end = spanned.getSpanEnd(clickedSpan);
            if (start >= 0 && end <= this.mText.length() && start < end) {
                Supplier<TextClassification> supplier = new -$$Lambda$TextView$DJlzb7VS7J_1890Kto7GAApQDN0(this, new Request.Builder(this.mText, start, end).setDefaultLocales(getTextLocales()).build());
                CompletableFuture.supplyAsync(supplier).completeOnTimeout(null, 1, TimeUnit.SECONDS).thenAccept(-$$Lambda$TextView$jQz3_DIfGrNeNdu_95_wi6UkW4E.INSTANCE);
                return true;
            }
        }
        return false;
    }

    public /* synthetic */ TextClassification lambda$handleClick$0$TextView(Request request) {
        return getTextClassifier().classifyText(request);
    }

    static /* synthetic */ void lambda$handleClick$1(TextClassification classification) {
        String str = LOG_TAG;
        if (classification == null) {
            Log.d(str, "Timeout while classifying text");
        } else if (classification.getActions().isEmpty()) {
            Log.d(str, "No link action to perform");
        } else {
            try {
                ((RemoteAction) classification.getActions().get(0)).getActionIntent().send();
            } catch (CanceledException e) {
                Log.e(str, "Error sending PendingIntent", e);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public void stopTextActionMode() {
        Editor editor = this.mEditor;
        if (editor != null) {
            editor.stopTextActionMode();
        }
    }

    public void hideFloatingToolbar(int durationMs) {
        Editor editor = this.mEditor;
        if (editor != null) {
            editor.hideFloatingToolbar(durationMs);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean canUndo() {
        Editor editor = this.mEditor;
        return editor != null && editor.canUndo();
    }

    /* Access modifiers changed, original: 0000 */
    public boolean canRedo() {
        Editor editor = this.mEditor;
        return editor != null && editor.canRedo();
    }

    /* Access modifiers changed, original: 0000 */
    public boolean canCut() {
        if (!hasPasswordTransformationMethod() && this.mText.length() > 0 && hasSelection() && (this.mText instanceof Editable)) {
            Editor editor = this.mEditor;
            if (!(editor == null || editor.mKeyListener == null)) {
                return true;
            }
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean canCopy() {
        if (!hasPasswordTransformationMethod() && this.mText.length() > 0 && hasSelection() && this.mEditor != null) {
            return true;
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean canShare() {
        if (getContext().canStartActivityForResult() && isDeviceProvisioned()) {
            return canCopy();
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isDeviceProvisioned() {
        if (this.mDeviceProvisionedState == 0) {
            int i;
            if (Global.getInt(this.mContext.getContentResolver(), "device_provisioned", 0) != 0) {
                i = 2;
            } else {
                i = 1;
            }
            this.mDeviceProvisionedState = i;
        }
        if (this.mDeviceProvisionedState == 2) {
            return true;
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public boolean canPaste() {
        if (this.mText instanceof Editable) {
            Editor editor = this.mEditor;
            if (editor != null && editor.mKeyListener != null && getSelectionStart() >= 0 && getSelectionEnd() >= 0 && getClipboardManagerForUser().hasPrimaryClip()) {
                return true;
            }
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean canPasteAsPlainText() {
        if (!canPaste()) {
            return false;
        }
        ClipData clipData = getClipboardManagerForUser().getPrimaryClip();
        ClipDescription description = clipData.getDescription();
        boolean isPlainType = description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);
        CharSequence text = clipData.getItemAt(0).getText();
        if (isPlainType && (text instanceof Spanned) && TextUtils.hasStyleSpan((Spanned) text)) {
            return true;
        }
        return description.hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean canProcessText() {
        if (getId() == -1) {
            return false;
        }
        return canShare();
    }

    /* Access modifiers changed, original: 0000 */
    public boolean canSelectAllText() {
        return (!canSelectText() || hasPasswordTransformationMethod() || (getSelectionStart() == 0 && getSelectionEnd() == this.mText.length())) ? false : true;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean selectAllText() {
        if (this.mEditor != null) {
            hideFloatingToolbar(500);
        }
        int length = this.mText.length();
        Selection.setSelection(this.mSpannable, 0, length);
        if (length > 0) {
            return true;
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public void replaceSelectionWithText(CharSequence text) {
        ((Editable) this.mText).replace(getSelectionStart(), getSelectionEnd(), text);
    }

    private void paste(int min, int max, boolean withFormatting) {
        ClipData clip = getClipboardManagerForUser().getPrimaryClip();
        if (clip != null) {
            boolean didFirst = false;
            for (int i = 0; i < clip.getItemCount(); i++) {
                CharSequence paste;
                if (withFormatting) {
                    paste = clip.getItemAt(i).coerceToStyledText(getContext());
                } else {
                    paste = clip.getItemAt(i).coerceToText(getContext());
                    paste = paste instanceof Spanned ? paste.toString() : paste;
                }
                if (paste != null) {
                    if (didFirst) {
                        ((Editable) this.mText).insert(getSelectionEnd(), "\n");
                        ((Editable) this.mText).insert(getSelectionEnd(), paste);
                    } else {
                        Selection.setSelection(this.mSpannable, max);
                        ((Editable) this.mText).replace(min, max, paste);
                        didFirst = true;
                    }
                }
            }
            sLastCutCopyOrTextChangedTime = 0;
        }
    }

    private void shareSelectedText() {
        String selectedText = getSelectedText();
        if (selectedText != null && !selectedText.isEmpty()) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType(ClipDescription.MIMETYPE_TEXT_PLAIN);
            String str = Intent.EXTRA_TEXT;
            sharingIntent.removeExtra(str);
            sharingIntent.putExtra(str, (String) TextUtils.trimToParcelableSize(selectedText));
            getContext().startActivity(Intent.createChooser(sharingIntent, null));
            Selection.setSelection(this.mSpannable, getSelectionEnd());
        }
    }

    private boolean setPrimaryClip(ClipData clip) {
        try {
            getClipboardManagerForUser().setPrimaryClip(clip);
            sLastCutCopyOrTextChangedTime = SystemClock.uptimeMillis();
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    public int getOffsetForPosition(float x, float y) {
        if (getLayout() == null) {
            return -1;
        }
        return getOffsetAtCoordinate(getLineAtCoordinate(y), x);
    }

    /* Access modifiers changed, original: 0000 */
    public float convertToLocalHorizontalCoordinate(float x) {
        return Math.min((float) ((getWidth() - getTotalPaddingRight()) - 1), Math.max(0.0f, x - ((float) getTotalPaddingLeft()))) + ((float) getScrollX());
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public int getLineAtCoordinate(float y) {
        return getLayout().getLineForVertical((int) (Math.min((float) ((getHeight() - getTotalPaddingBottom()) - 1), Math.max(0.0f, y - ((float) getTotalPaddingTop()))) + ((float) getScrollY())));
    }

    /* Access modifiers changed, original: 0000 */
    public int getLineAtCoordinateUnclamped(float y) {
        return getLayout().getLineForVertical((int) ((y - ((float) getTotalPaddingTop())) + ((float) getScrollY())));
    }

    /* Access modifiers changed, original: 0000 */
    public int getOffsetAtCoordinate(int line, float x) {
        return getLayout().getOffsetForHorizontal(line, convertToLocalHorizontalCoordinate(x));
    }

    public boolean onDragEvent(DragEvent event) {
        int action = event.getAction();
        boolean z = true;
        Editor editor;
        if (action == 1) {
            editor = this.mEditor;
            if (editor == null || !editor.hasInsertionController()) {
                z = false;
            }
            return z;
        } else if (action == 2) {
            if (this.mText instanceof Spannable) {
                Selection.setSelection(this.mSpannable, getOffsetForPosition(event.getX(), event.getY()));
            }
            return true;
        } else if (action == 3) {
            editor = this.mEditor;
            if (editor != null) {
                editor.onDrop(event);
            }
            return true;
        } else if (action != 5) {
            return true;
        } else {
            requestFocus();
            return true;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isInBatchEditMode() {
        InputMethodState ims = this.mEditor;
        boolean z = false;
        if (ims == null) {
            return false;
        }
        ims = ims.mInputMethodState;
        if (ims == null) {
            return this.mEditor.mInBatchEditControllers;
        }
        if (ims.mBatchEditNesting > 0) {
            z = true;
        }
        return z;
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        TextDirectionHeuristic newTextDir = getTextDirectionHeuristic();
        if (this.mTextDir != newTextDir) {
            this.mTextDir = newTextDir;
            if (this.mLayout != null) {
                checkForRelayout();
            }
        }
    }

    public TextDirectionHeuristic getTextDirectionHeuristic() {
        if (hasPasswordTransformationMethod()) {
            return TextDirectionHeuristics.LTR;
        }
        Editor editor = this.mEditor;
        boolean z = false;
        if (editor == null || (editor.mInputType & 15) != 3) {
            if (getLayoutDirection() == 1) {
                z = true;
            }
            boolean defaultIsRtl = z;
            switch (getTextDirection()) {
                case 2:
                    return TextDirectionHeuristics.ANYRTL_LTR;
                case 3:
                    return TextDirectionHeuristics.LTR;
                case 4:
                    return TextDirectionHeuristics.RTL;
                case 5:
                    return TextDirectionHeuristics.LOCALE;
                case 6:
                    return TextDirectionHeuristics.FIRSTSTRONG_LTR;
                case 7:
                    return TextDirectionHeuristics.FIRSTSTRONG_RTL;
                default:
                    TextDirectionHeuristic textDirectionHeuristic;
                    if (defaultIsRtl) {
                        textDirectionHeuristic = TextDirectionHeuristics.FIRSTSTRONG_RTL;
                    } else {
                        textDirectionHeuristic = TextDirectionHeuristics.FIRSTSTRONG_LTR;
                    }
                    return textDirectionHeuristic;
            }
        }
        byte digitDirection = Character.getDirectionality(DecimalFormatSymbols.getInstance(getTextLocale()).getDigitStrings()[0].codePointAt(0));
        if (digitDirection == (byte) 1 || digitDirection == (byte) 2) {
            return TextDirectionHeuristics.RTL;
        }
        return TextDirectionHeuristics.LTR;
    }

    public void onResolveDrawables(int layoutDirection) {
        if (this.mLastLayoutDirection != layoutDirection) {
            this.mLastLayoutDirection = layoutDirection;
            Drawables drawables = this.mDrawables;
            if (drawables != null && drawables.resolveWithLayoutDirection(layoutDirection)) {
                prepareDrawableForDisplay(this.mDrawables.mShowing[0]);
                prepareDrawableForDisplay(this.mDrawables.mShowing[2]);
                applyCompoundDrawableTint();
            }
        }
    }

    private void prepareDrawableForDisplay(Drawable dr) {
        if (dr != null) {
            dr.setLayoutDirection(getLayoutDirection());
            if (dr.isStateful()) {
                dr.setState(getDrawableState());
                dr.jumpToCurrentState();
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void resetResolvedDrawables() {
        super.resetResolvedDrawables();
        this.mLastLayoutDirection = -1;
    }

    /* Access modifiers changed, original: protected */
    public void viewClicked(InputMethodManager imm) {
        if (imm != null) {
            imm.viewClicked(this);
        }
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public void deleteText_internal(int start, int end) {
        ((Editable) this.mText).delete(start, end);
    }

    /* Access modifiers changed, original: protected */
    public void replaceText_internal(int start, int end, CharSequence text) {
        ((Editable) this.mText).replace(start, end, text);
    }

    /* Access modifiers changed, original: protected */
    public void setSpan_internal(Object span, int start, int end, int flags) {
        ((Editable) this.mText).setSpan(span, start, end, flags);
    }

    /* Access modifiers changed, original: protected */
    public void setCursorPosition_internal(int start, int end) {
        Selection.setSelection((Editable) this.mText, start, end);
    }

    @UnsupportedAppUsage
    private void createEditorIfNeeded() {
        if (this.mEditor == null) {
            this.mEditor = new Editor(this);
        }
    }

    @UnsupportedAppUsage
    public CharSequence getIterableTextForAccessibility() {
        return this.mText;
    }

    private void ensureIterableTextForAccessibilitySelectable() {
        CharSequence charSequence = this.mText;
        if (!(charSequence instanceof Spannable)) {
            setText(charSequence, BufferType.SPANNABLE);
        }
    }

    public TextSegmentIterator getIteratorForGranularity(int granularity) {
        if (granularity == 4) {
            Spannable text = (Spannable) getIterableTextForAccessibility();
            if (!(TextUtils.isEmpty(text) || getLayout() == null)) {
                LineTextSegmentIterator iterator = LineTextSegmentIterator.getInstance();
                iterator.initialize(text, getLayout());
                return iterator;
            }
        } else if (!(granularity != 16 || TextUtils.isEmpty((Spannable) getIterableTextForAccessibility()) || getLayout() == null)) {
            PageTextSegmentIterator iterator2 = PageTextSegmentIterator.getInstance();
            iterator2.initialize(this);
            return iterator2;
        }
        return super.getIteratorForGranularity(granularity);
    }

    public int getAccessibilitySelectionStart() {
        return getSelectionStart();
    }

    public boolean isAccessibilitySelectionExtendable() {
        return true;
    }

    public int getAccessibilitySelectionEnd() {
        return getSelectionEnd();
    }

    public void setAccessibilitySelection(int start, int end) {
        if (getAccessibilitySelectionStart() != start || getAccessibilitySelectionEnd() != end) {
            CharSequence text = getIterableTextForAccessibility();
            if (Math.min(start, end) < 0 || Math.max(start, end) > text.length()) {
                Selection.removeSelection((Spannable) text);
            } else {
                Selection.setSelection((Spannable) text, start, end);
            }
            Editor editor = this.mEditor;
            if (editor != null) {
                editor.hideCursorAndSpanControllers();
                this.mEditor.stopTextActionMode();
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void encodeProperties(ViewHierarchyEncoder stream) {
        super.encodeProperties(stream);
        TruncateAt ellipsize = getEllipsize();
        String str = null;
        stream.addProperty("text:ellipsize", ellipsize == null ? null : ellipsize.name());
        stream.addProperty("text:textSize", getTextSize());
        stream.addProperty("text:scaledTextSize", getScaledTextSize());
        stream.addProperty("text:typefaceStyle", getTypefaceStyle());
        stream.addProperty("text:selectionStart", getSelectionStart());
        stream.addProperty("text:selectionEnd", getSelectionEnd());
        stream.addProperty("text:curTextColor", this.mCurTextColor);
        CharSequence charSequence = this.mText;
        if (charSequence != null) {
            str = charSequence.toString();
        }
        stream.addProperty("text:text", str);
        stream.addProperty("text:gravity", this.mGravity);
    }
}
