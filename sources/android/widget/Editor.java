package android.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.UnsupportedAppUsage;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.UndoManager;
import android.content.UndoOperation;
import android.content.UndoOwner;
import android.content.pm.PackageManager;
import android.content.res.MiuiConfiguration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.RenderNode;
import android.graphics.drawable.Drawable;
import android.miui.R;
import android.net.Uri;
import android.os.AnrMonitor;
import android.os.Bundle;
import android.os.Handler;
import android.os.LocaleList;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.ParcelableParcel;
import android.os.SystemClock;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.provider.Settings.Global;
import android.provider.UserDictionary.Words;
import android.text.DynamicLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.ParcelableSpan;
import android.text.Selection;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.text.method.MetaKeyKeyListener;
import android.text.method.MovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.WordIterator;
import android.text.style.EasyEditSpan;
import android.text.style.SuggestionRangeSpan;
import android.text.style.SuggestionSpan;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.ContextMenu;
import android.view.DragAndDropPermissions;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.InternalInsetsInfo;
import android.view.ViewTreeObserver.OnComputeInternalInsetsListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.ViewTreeObserver.OnTouchModeChangeListener;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.CorrectionInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView.OnEditorActionListener;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.internal.phrase.Phrases.Utils;
import com.android.internal.phrase.QueryPhraseTask;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import com.android.internal.view.menu.ActionMenu;
import com.miui.internal.helper.MiuiVersionHelper;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import miui.os.Build;
import miui.view.animation.BackEaseOutInterpolator;
import miui.view.animation.CubicEaseOutInterpolator;

public class Editor {
    static final int BLINK = 500;
    private static final boolean DEBUG_UNDO = false;
    private static int DRAG_SHADOW_MAX_TEXT_LENGTH = 20;
    static final int EXTRACT_NOTHING = -2;
    static final int EXTRACT_UNKNOWN = -1;
    private static final boolean FLAG_USE_MAGNIFIER = true;
    public static final int HANDLE_TYPE_SELECTION_END = 1;
    public static final int HANDLE_TYPE_SELECTION_START = 0;
    private static final float LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS = 0.5f;
    private static final int MENU_ITEM_ORDER_COPY = 4;
    private static final int MENU_ITEM_ORDER_CUT = 3;
    private static final int MENU_ITEM_ORDER_PASTE = 5;
    private static final int MENU_ITEM_ORDER_PASTE_AS_PLAIN_TEXT = 6;
    private static final int MENU_ITEM_ORDER_PROCESS_TEXT_INTENT_ACTIONS_START = 10;
    private static final int MENU_ITEM_ORDER_REDO = 2;
    private static final int MENU_ITEM_ORDER_REPLACE = 9;
    private static final int MENU_ITEM_ORDER_SELECT_ALL = 8;
    private static final int MENU_ITEM_ORDER_SHARE = 7;
    private static final int MENU_ITEM_ORDER_UNDO = 1;
    private static final String MOCK_CALLBACK_NAME = "Mock for Callback";
    private static final String TAG = "Editor";
    private static final int TAP_STATE_DOUBLE_TAP = 2;
    private static final int TAP_STATE_FIRST_TAP = 1;
    private static final int TAP_STATE_INITIAL = 0;
    private static final int TAP_STATE_TRIPLE_CLICK = 3;
    private static final float[] TEMP_POSITION = new float[2];
    private static final String UNDO_OWNER_TAG = "Editor";
    private static final int UNSET_LINE = -1;
    private static final int UNSET_X_VALUE = -1;
    private static final ActionMode mMockActionMode = new ActionMode() {
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
    private static DisplayMetrics sTmpDisplayMetrics;
    private boolean mAllowToStartActionMode = true;
    boolean mAllowUndo = true;
    private Blink mBlink;
    private float mContextMenuAnchorX;
    private float mContextMenuAnchorY;
    private CorrectionHighlighter mCorrectionHighlighter;
    @UnsupportedAppUsage
    boolean mCreatedWithASelection;
    boolean mCursorVisible = true;
    Callback mCustomInsertionActionModeCallback;
    Callback mCustomSelectionActionModeCallback;
    private boolean mCustomSelectionActionModeCallbackDestroyed = false;
    boolean mDiscardNextActionUp;
    Drawable mDrawableForCursor = null;
    private Runnable mEmailPopupShower;
    private EmailAddPopupWindow mEmailPopupWindow;
    CharSequence mError;
    private ErrorPopup mErrorPopup;
    boolean mErrorWasChanged;
    boolean mFirstTouchUp = true;
    boolean mFrozenWithFocus;
    boolean mIgnoreActionUpEvent;
    boolean mInBatchEditControllers;
    InputContentType mInputContentType;
    InputMethodState mInputMethodState;
    int mInputType = 0;
    private Runnable mInsertionActionModeRunnable;
    @UnsupportedAppUsage
    private boolean mInsertionControllerEnabled;
    private InsertionPointCursorController mInsertionPointCursorController;
    boolean mIsBeingLongClicked;
    boolean mIsInsertionActionModeStartPending = false;
    KeyListener mKeyListener;
    private int mLastButtonState;
    private float mLastDownPositionX;
    private float mLastDownPositionY;
    private long mLastTouchUpTime = 0;
    private float mLastUpPositionX;
    private float mLastUpPositionY;
    private final MagnifierMotionAnimator mMagnifierAnimator;
    private PositionListener mPositionListener;
    private boolean mPreserveSelection;
    final ProcessTextIntentActionsHandler mProcessTextIntentActionsHandler;
    private boolean mRenderCursorRegardlessTiming;
    private boolean mRestartActionModeOnNextRefresh;
    boolean mSelectAllOnFocus;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private Drawable mSelectHandleCenter;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private Drawable mSelectHandleLeft;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private Drawable mSelectHandleRight;
    private SelectionActionModeHelper mSelectionActionModeHelper;
    @UnsupportedAppUsage
    private boolean mSelectionControllerEnabled;
    SelectionModifierCursorController mSelectionModifierCursorController;
    boolean mSelectionMoved;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123769485)
    private long mShowCursor;
    private boolean mShowErrorAfterAttach;
    @UnsupportedAppUsage
    boolean mShowSoftInputOnFocus = true;
    private Runnable mShowSuggestionRunnable;
    protected WeakReference<PinnedPopupWindow> mShownWindow;
    private SpanController mSpanController;
    SpellChecker mSpellChecker;
    SuggestionRangeSpan mSuggestionRangeSpan;
    private SuggestionsPopupWindow mSuggestionsPopupWindow;
    private int mTapState = 0;
    private Rect mTempRect;
    private ActionMode mTextActionMode;
    boolean mTextIsSelectable;
    private TextRenderNode[] mTextRenderNodes;
    private TextView mTextView;
    boolean mTouchFocusSelected;
    final UndoInputFilter mUndoInputFilter = new UndoInputFilter(this);
    private final UndoManager mUndoManager = new UndoManager();
    private UndoOwner mUndoOwner = this.mUndoManager.getOwner("Editor", this);
    private boolean mUpdateWordIteratorText;
    private WordIterator mWordIterator;
    private WordIterator mWordIteratorWithText;

    private interface TextViewPositionListener {
        void updatePosition(int i, int i2, boolean z, boolean z2);
    }

    private abstract class PinnedPopupWindow implements TextViewPositionListener {
        protected ViewGroup mContentView;
        protected int mPopupElevation;
        protected PopupWindow mPopupWindow;
        int mPositionX;
        int mPositionY;

        public abstract int clipVertically(int i);

        public abstract void createPopupWindow();

        public abstract int getTextOffset();

        public abstract int getVerticalLocalPosition(int i);

        public abstract void initContentView();

        public PinnedPopupWindow() {
            this.mPopupElevation = MiuiVersionHelper.getPopupElevation(Editor.this.mTextView.getResources());
            createPopupWindow();
            this.mPopupWindow.setWindowLayoutType(1002);
            this.mPopupWindow.setWidth(-2);
            this.mPopupWindow.setHeight(-2);
            initContentView();
            this.mContentView.setLayoutParams(new LayoutParams(-2, -2));
            this.mPopupWindow.setContentView(this.mContentView);
        }

        public void show() {
            Editor.this.onPopupWindowShown(this);
            Editor.this.getPositionListener().addSubscriber(this, false);
            computeLocalPosition();
            int i = this.mPopupElevation;
            if (i > 0) {
                this.mContentView.setElevation((float) i);
                this.mPopupWindow.setElevation(this.mContentView.getElevation());
                this.mPopupWindow.setContentView(this.mContentView);
            }
            PositionListener positionListener = Editor.this.getPositionListener();
            updatePosition(positionListener.getPositionX(), positionListener.getPositionY());
        }

        /* Access modifiers changed, original: protected */
        public void measureContent() {
            DisplayMetrics displayMetrics = Editor.this.mTextView.getResources().getDisplayMetrics();
            this.mContentView.measure(MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, Integer.MIN_VALUE));
        }

        /* Access modifiers changed, original: protected */
        public void computeLocalPosition() {
            measureContent();
            int width = this.mContentView.getMeasuredWidth();
            int offset = getTextOffset();
            Layout layout = Editor.this.mTextView.getLayout();
            if (layout != null) {
                this.mPositionX = (int) (layout.getPrimaryHorizontal(offset) - (((float) width) / 2.0f));
                this.mPositionX += Editor.this.mTextView.viewportToContentHorizontalOffset();
                this.mPositionY = getVerticalLocalPosition(layout.getLineForOffset(offset));
                this.mPositionY += Editor.this.mTextView.viewportToContentVerticalOffset();
                return;
            }
            this.mPositionX = 0;
            this.mPositionY = Editor.this.mTextView.getCompoundPaddingTop();
        }

        /* Access modifiers changed, original: protected */
        public void updatePosition(int parentPositionX, int parentPositionY) {
            int positionX = this.mPositionX + parentPositionX;
            int positionY = clipVertically(this.mPositionY + parentPositionY);
            DisplayMetrics displayMetrics = Editor.this.mTextView.getResources().getDisplayMetrics();
            positionX = Math.max(0, Math.min(displayMetrics.widthPixels - this.mContentView.getMeasuredWidth(), positionX));
            if (isShowing()) {
                this.mPopupWindow.update(positionX, positionY, -1, -1);
            } else {
                this.mPopupWindow.showAtLocation(Editor.this.mTextView, 0, positionX, positionY);
            }
        }

        public void hide() {
            dismiss();
            Editor.this.getPositionListener().removeSubscriber(this);
        }

        public void dismiss() {
            this.mPopupWindow.dismiss();
            Editor.this.onPopupWindowDismiss(this);
        }

        public void updatePosition(int parentPositionX, int parentPositionY, boolean parentPositionChanged, boolean parentScrolled) {
            if (isShowing() && Editor.this.isOffsetVisible(getTextOffset())) {
                if (parentScrolled) {
                    computeLocalPosition();
                }
                updatePosition(parentPositionX, parentPositionY);
                return;
            }
            hide();
        }

        public boolean isShowing() {
            return this.mPopupWindow.isShowing();
        }
    }

    private interface Fader {
        void cancelAnimations();

        void fadeIn(int i, int i2);

        void fadeOut();

        void setY(int i);
    }

    private abstract class ActionPinnedPopupWindow extends PinnedPopupWindow implements Fader {
        protected static final int LONG_ACTION_MENU_COUNT = 6;
        protected boolean mAboveHandle;
        protected AnimatorSet mAnimationFadeIn;
        protected AnimatorSet mAnimationFadeOut;
        protected AnimatorListenerAdapter mAnimationFadeOutListener;
        private HandleView mHandleView;
        protected LayoutInflater mInflater;
        private final OnComputeInternalInsetsListener mInsetsComputer = new OnComputeInternalInsetsListener() {
            public void onComputeInternalInsets(InternalInsetsInfo info) {
                info.contentInsets.setEmpty();
                info.visibleInsets.setEmpty();
                info.touchableRegion.set(ActionPinnedPopupWindow.this.mTouchableRegion);
                info.setTouchableInsets(3);
            }
        };
        protected TouchPanelLayout mMainPanel;
        private Runnable mShower = new Runnable() {
            public void run() {
                if (Editor.this.isOffsetVisible(Editor.this.mTextView.getSelectionStart()) || Editor.this.isOffsetVisible(Editor.this.mTextView.getSelectionEnd()) || ActionPinnedPopupWindow.this.isMiddleOffsetInSelection()) {
                    ActionPinnedPopupWindow.this.show();
                    if (ActionPinnedPopupWindow.this.mHandleView instanceof InsertionHandleView) {
                        ((InsertionHandleView) ActionPinnedPopupWindow.this.mHandleView).hideAfterDelay();
                    }
                }
            }
        };
        private int mSpaceOffScreen;
        private final Region mTouchableRegion = new Region();
        protected List<View> mVisibleChildren = new ArrayList();

        public abstract void createAnimations();

        public ActionPinnedPopupWindow(HandleView handleView) {
            super();
            this.mHandleView = handleView;
            this.mSpaceOffScreen = Editor.this.mTextView.getResources().getDimensionPixelSize(R.dimen.text_action_popup_off_screen);
            createAnimations();
            ((AnimatePopupWindow) this.mPopupWindow).setFader(this);
            this.mInflater = (LayoutInflater) Editor.this.mTextView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        /* Access modifiers changed, original: protected */
        public void measureContent() {
            int widthMode;
            DisplayMetrics displayMetrics = Editor.this.mTextView.getResources().getDisplayMetrics();
            int contentWidth = displayMetrics.widthPixels + (this.mSpaceOffScreen * 2);
            if (!Build.IS_TABLET) {
                if (this.mVisibleChildren.size() >= (Build.IS_INTERNATIONAL_BUILD ? 5 : 6)) {
                    widthMode = 1073741824;
                    this.mContentView.measure(MeasureSpec.makeMeasureSpec(contentWidth, widthMode), MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, Integer.MIN_VALUE));
                }
            }
            widthMode = Integer.MIN_VALUE;
            this.mContentView.measure(MeasureSpec.makeMeasureSpec(contentWidth, widthMode), MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, Integer.MIN_VALUE));
        }

        /* Access modifiers changed, original: protected */
        public int getSelectionStart() {
            return Editor.this.mTextView.getSelectionStart();
        }

        /* Access modifiers changed, original: protected */
        public int getSelectionEnd() {
            return Editor.this.mTextView.getSelectionEnd();
        }

        /* Access modifiers changed, original: protected */
        public void computeLocalPosition() {
            int selectionStart = getSelectionStart();
            int selectionEnd = getSelectionEnd();
            Layout layout = Editor.this.mTextView.getLayout();
            this.mAboveHandle = true;
            measureContent();
            if (Editor.this.isOffsetVisible(selectionStart)) {
                this.mPositionY = layout.getLineTop(layout.getLineForOffset(selectionStart)) - this.mContentView.getMeasuredHeight();
                this.mPositionY += Editor.this.mTextView.viewportToContentVerticalOffset();
            } else if (Editor.this.isOffsetVisible(selectionEnd)) {
                this.mPositionY = layout.getLineBottom(layout.getLineForOffset(selectionEnd));
                this.mPositionY += Editor.this.mTextView.viewportToContentVerticalOffset();
                if (this.mHandleView.getVisibility() == 0) {
                    this.mPositionY += Editor.this.mTextView.getResources().getDrawable(R.drawable.text_select_handle_left).getIntrinsicHeight() / 2;
                }
                this.mAboveHandle = false;
            } else if (Editor.this.mTextView.isSingleLine()) {
                this.mPositionY = layout.getLineTop(layout.getLineForOffset(getTextOffset())) - this.mContentView.getMeasuredHeight();
                this.mPositionY += Editor.this.mTextView.viewportToContentVerticalOffset();
            } else {
                if (Editor.this.mTempRect == null) {
                    Editor.this.mTempRect = new Rect();
                }
                Editor.this.mTextView.getLocalVisibleRect(Editor.this.mTempRect);
                this.mPositionY = (((Editor.this.mTempRect.bottom - Editor.this.mTempRect.top) / 2) + Editor.this.mTempRect.top) - (this.mContentView.getMeasuredHeight() / 2);
            }
            int width = this.mContentView.getMeasuredWidth();
            this.mPositionX = (int) (((((layout.getPrimaryHorizontal(selectionStart) + layout.getPrimaryHorizontal(selectionEnd)) - ((float) width)) / 2.0f) + ((float) Editor.this.mTextView.viewportToContentHorizontalOffset())) * this.mHandleView.mHorizontalScale);
        }

        /* Access modifiers changed, original: protected */
        public void updatePosition(int parentPositionX, int parentPositionY) {
            int positionX = this.mPositionX + parentPositionX;
            int positionY = clipVertically(this.mPositionY + parentPositionY);
            int width = this.mContentView.getMeasuredWidth();
            this.mPopupWindow.setWidth(width);
            positionX = Math.max(-this.mSpaceOffScreen, Math.min((Editor.this.mTextView.getResources().getDisplayMetrics().widthPixels + this.mSpaceOffScreen) - width, positionX));
            if (isShowing()) {
                this.mPopupWindow.update(positionX, positionY, width, -1);
                return;
            }
            this.mPopupWindow.showAtLocation(Editor.this.mTextView, 0, positionX, positionY);
            setTouchableSurfaceInsetsComputer();
        }

        /* Access modifiers changed, original: protected */
        public int clipVertically(int positionY) {
            Layout layout = Editor.this.mTextView.getLayout();
            int selectionStart = Editor.this.mTextView.getSelectionStart();
            int selectionEnd = Editor.this.mTextView.getSelectionEnd();
            int lineStart = layout.getLineForOffset(selectionStart);
            int lineEnd = layout.getLineForOffset(selectionEnd);
            int spaceBetweenSelection = layout.getLineTop(lineEnd) - layout.getLineBottom(lineStart);
            int handleHeight = Editor.this.mTextView.getResources().getDrawable(R.drawable.text_select_handle_left).getIntrinsicHeight();
            if (positionY < 0 && Editor.this.isOffsetVisible(selectionStart)) {
                if (spaceBetweenSelection > this.mContentView.getMeasuredHeight()) {
                    positionY += layout.getLineBottom(lineStart) - layout.getLineTop(lineStart);
                } else {
                    positionY += layout.getLineBottom(lineEnd) - layout.getLineTop(lineStart);
                }
                positionY = (positionY + this.mContentView.getMeasuredHeight()) + (handleHeight / 2);
                this.mAboveHandle = false;
                return positionY;
            } else if (positionY < 0 && Editor.this.mTextView.isSingleLine()) {
                positionY = (layout.getLineBottom(lineStart) + Editor.this.mTextView.viewportToContentVerticalOffset()) + (handleHeight / 2);
                this.mAboveHandle = false;
                return positionY;
            } else if (this.mContentView.getMeasuredHeight() + positionY <= Editor.this.getDisplayHeightPixels() || !Editor.this.isOffsetVisible(selectionEnd)) {
                return positionY;
            } else {
                if (spaceBetweenSelection > this.mContentView.getMeasuredHeight()) {
                    positionY -= layout.getLineBottom(lineEnd) - layout.getLineTop(lineEnd);
                } else {
                    positionY -= layout.getLineBottom(lineEnd) - layout.getLineTop(lineStart);
                }
                return (positionY - this.mContentView.getMeasuredHeight()) - (handleHeight / 2);
            }
        }

        public void hide() {
            Editor.this.mTextView.removeCallbacks(this.mShower);
            super.hide();
        }

        public void dismiss() {
            super.dismiss();
            setZeroTouchableSurface();
        }

        public void updatePosition(int parentPositionX, int parentPositionY, boolean parentPositionChanged, boolean parentScrolled) {
            if (parentScrolled || parentPositionChanged) {
                boolean insertionMode = false;
                if (this.mHandleView.isShowing()) {
                    InsertionHandleView insertionHandle = this.mHandleView;
                    if (insertionHandle instanceof InsertionHandleView) {
                        insertionHandle = insertionHandle;
                        insertionHandle.removeHiderCallback();
                        insertionMode = true;
                        insertionHandle.mReShowPopup = true;
                    }
                }
                if (isMainPanelContent()) {
                    hide();
                    show(500);
                } else if (insertionMode) {
                    hide();
                } else {
                    Editor.this.stopSelectionActionMode();
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void show(int delay) {
            Editor.this.mTextView.removeCallbacks(this.mShower);
            Editor.this.mTextView.postDelayed(this.mShower, (long) delay);
        }

        private boolean isMiddleOffsetInSelection() {
            int middleOffset = Editor.this.mTextView.getOffsetForPosition((float) (Editor.this.mTextView.getWidth() / 2), (float) (Editor.this.mTextView.getHeight() / 2));
            return middleOffset <= Editor.this.mTextView.getSelectionEnd() && middleOffset >= Editor.this.mTextView.getSelectionStart();
        }

        /* Access modifiers changed, original: protected */
        public int getVerticalLocalPosition(int line) {
            return 0;
        }

        /* Access modifiers changed, original: protected */
        public int getTextOffset() {
            return (Editor.this.mTextView.getSelectionStart() + Editor.this.mTextView.getSelectionEnd()) / 2;
        }

        public void fadeOut() {
            this.mPopupWindow.dismiss();
        }

        public void cancelAnimations() {
            this.mAnimationFadeIn.cancel();
            this.mContentView.setScaleX(1.0f);
            this.mContentView.setScaleY(1.0f);
        }

        public void setY(int y) {
        }

        public void show() {
            setMainPanelAsContent();
            super.show();
        }

        /* Access modifiers changed, original: protected */
        public void setMainPanelAsContent() {
            if (this.mMainPanel.getParent() != null) {
                setContentAreaAsTouchableSurface(false);
                return;
            }
            this.mContentView.removeAllViews();
            this.mContentView.addView(this.mMainPanel, new LayoutParams(-1, -1));
            setContentAreaAsTouchableSurface(false);
        }

        /* Access modifiers changed, original: protected */
        public void setSubPanelAsContent(View subView, Drawable panelBackground) {
            dismiss();
            HandleView handleView = this.mHandleView;
            if (handleView instanceof InsertionHandleView) {
                ((InsertionHandleView) handleView).removeHiderCallback();
            }
            View subPanel = new LinearLayout(Editor.this.mTextView.getContext());
            if (panelBackground != null) {
                subPanel.setBackground(panelBackground);
                subPanel.setId(R.id.text_action_background_view);
            }
            subPanel.addView(subView);
            this.mContentView.removeAllViews();
            this.mContentView.addView(subPanel, new LayoutParams(-1, -1));
            this.mVisibleChildren.clear();
            computeLocalPosition();
            PositionListener positionListener = Editor.this.getPositionListener();
            updatePosition(positionListener.getPositionX(), positionListener.getPositionY());
            setContentAreaAsTouchableSurface(false);
        }

        private boolean isMainPanelContent() {
            return this.mMainPanel != null && this.mContentView.getChildAt(0) == this.mMainPanel;
        }

        /* Access modifiers changed, original: protected */
        public void setZeroTouchableSurface() {
            this.mTouchableRegion.setEmpty();
        }

        /* Access modifiers changed, original: protected */
        public void setContentAreaAsTouchableSurface(boolean needMeasure) {
            View panel = this.mContentView.findViewById(R.id.text_action_background_view);
            int paddingLeft = panel.getPaddingLeft();
            int paddingTop = panel.getPaddingTop();
            int paddingRight = panel.getPaddingRight();
            int paddingBottom = panel.getPaddingBottom();
            if (needMeasure || !this.mPopupWindow.isShowing()) {
                int widthMode;
                DisplayMetrics displayMetrics = Editor.this.mTextView.getResources().getDisplayMetrics();
                int maxWidth = displayMetrics.widthPixels + (this.mSpaceOffScreen * 2);
                if (this.mVisibleChildren.size() < (Build.IS_INTERNATIONAL_BUILD ? 5 : 6)) {
                    widthMode = Integer.MIN_VALUE;
                } else {
                    widthMode = 1073741824;
                }
                panel.measure(MeasureSpec.makeMeasureSpec(maxWidth, widthMode), MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, Integer.MIN_VALUE));
            }
            this.mTouchableRegion.set(panel.getLeft() + paddingLeft, panel.getTop() + paddingTop, (panel.getLeft() + panel.getMeasuredWidth()) - paddingRight, (panel.getTop() + panel.getMeasuredHeight()) - paddingBottom);
        }

        private void setTouchableSurfaceInsetsComputer() {
            ViewTreeObserver viewTreeObserver = this.mPopupWindow.getContentView().getRootView().getViewTreeObserver();
            viewTreeObserver.removeOnComputeInternalInsetsListener(this.mInsetsComputer);
            viewTreeObserver.addOnComputeInternalInsetsListener(this.mInsetsComputer);
        }
    }

    private class ActionPopupWindow extends ActionPinnedPopupWindow implements OnClickListener {
        private final int POPUP_TEXT_LAYOUT = R.layout.text_edit_action_popup_text;
        private TextView mAutoFillTextView;
        private TextView mCopyTextView;
        private TextView mCutTextView;
        private boolean mFeatureTel;
        private TextView mPasteTextView;
        private TextView mPhraseTextView;
        private ArrayList<String> mPhrases;
        private QueryPhraseTask mQueryPhraseTask;
        private TextView mReplaceTextView;
        private ImageView mSearchImageView;
        private TextView mSelectAllTextView;
        private TextView mSelectTextView;
        private ImageView mShareImageView;
        private ImageView mTelImageView;
        private int mTextActionPadding;
        private Handler mTranslationHandler = new Handler() {
            public void handleMessage(Message msg) {
                int i = msg.what;
                if (i == 0) {
                    ActionPopupWindow.this.mTranslationPresenter.updatePanel(msg.obj);
                } else if (i != 1) {
                    Log.e("Editor", "Unrecognised message received.");
                } else {
                    ActionPopupWindow.this.mTranslationPresenter.updatePanel(null);
                }
                ActionPopupWindow.this.setContentAreaAsTouchableSurface(true);
            }
        };
        private ImageView mTranslationImageView;
        private TranslationManager mTranslationManager;
        private View mTranslationPanel;
        private TranslationPresenter mTranslationPresenter;
        private ImageView mWebImageView;

        public ActionPopupWindow(HandleView handleView) {
            int i;
            super(handleView);
            Resources resources = Editor.this.mTextView.getResources();
            if (Editor.isBigFontMode()) {
                i = R.dimen.text_action_popup_padding_small;
            } else {
                i = R.dimen.text_action_popup_padding;
            }
            this.mTextActionPadding = resources.getDimensionPixelSize(i);
            Intent intent = new Intent(Intent.ACTION_DIAL);
            if (!Build.IS_TABLET && intent.resolveActivity(Editor.this.mTextView.getContext().getPackageManager()) != null) {
                this.mFeatureTel = true;
            }
        }

        /* Access modifiers changed, original: protected */
        public void createPopupWindow() {
            Editor editor = Editor.this;
            this.mPopupWindow = new AnimatePopupWindow(editor, editor.mTextView.getContext(), null);
            this.mPopupWindow.setClippingEnabled(false);
        }

        /* Access modifiers changed, original: protected */
        public void initContentView() {
            this.mMainPanel = new TouchPanelLayout(Editor.this.mTextView.getContext());
            if (this.mPopupElevation > 0) {
                this.mMainPanel.setElevation((float) this.mPopupElevation);
            }
            this.mMainPanel.setOrientation(0);
            this.mMainPanel.setBackgroundResource(R.drawable.text_select_bg);
            this.mMainPanel.setId(R.id.text_action_background_view);
            this.mContentView = new FrameLayout(Editor.this.mTextView.getContext());
            this.mContentView.addView(this.mMainPanel, new LayoutParams(-1, -1));
            this.mSearchImageView = newImageView();
            this.mMainPanel.addView(this.mSearchImageView);
            this.mSearchImageView.setImageResource(R.drawable.text_action_search);
            this.mSearchImageView.setContentDescription(Editor.this.mTextView.getResources().getString(R.string.content_description_search));
            this.mWebImageView = newImageView();
            this.mMainPanel.addView(this.mWebImageView);
            this.mWebImageView.setImageResource(R.drawable.text_action_url);
            this.mWebImageView.setContentDescription(Editor.this.mTextView.getResources().getString(R.string.content_description_web_url));
            this.mTelImageView = newImageView();
            this.mMainPanel.addView(this.mTelImageView);
            this.mTelImageView.setImageResource(R.drawable.text_action_tel);
            this.mTelImageView.setContentDescription(Editor.this.mTextView.getResources().getString(R.string.content_description_dial));
            this.mSelectTextView = newTextView();
            this.mMainPanel.addView(this.mSelectTextView);
            this.mSelectTextView.setText((int) R.string.select);
            this.mSelectAllTextView = newTextView();
            this.mMainPanel.addView(this.mSelectAllTextView);
            this.mSelectAllTextView.setText((int) R.string.select_all);
            this.mCutTextView = newTextView();
            this.mMainPanel.addView(this.mCutTextView);
            this.mCutTextView.setText((int) R.string.cut);
            this.mCopyTextView = newTextView();
            this.mMainPanel.addView(this.mCopyTextView);
            this.mCopyTextView.setText((int) R.string.copy);
            this.mPasteTextView = newTextView();
            this.mMainPanel.addView(this.mPasteTextView);
            this.mPasteTextView.setText((int) R.string.paste);
            this.mReplaceTextView = newTextView();
            this.mMainPanel.addView(this.mReplaceTextView);
            this.mReplaceTextView.setText((int) R.string.replace);
            this.mAutoFillTextView = newTextView();
            this.mMainPanel.addView(this.mAutoFillTextView);
            this.mAutoFillTextView.setText((int) R.string.autofill);
            this.mAutoFillTextView.setId(com.android.internal.R.id.floating_toolbar_menu_item_text);
            this.mTranslationImageView = newImageView();
            this.mMainPanel.addView(this.mTranslationImageView);
            this.mTranslationImageView.setImageResource(R.drawable.text_action_dic);
            this.mTranslationImageView.setContentDescription(Editor.this.mTextView.getResources().getString(R.string.content_description_translate));
            this.mShareImageView = newImageView();
            this.mMainPanel.addView(this.mShareImageView);
            this.mShareImageView.setImageResource(R.drawable.text_action_share);
            this.mShareImageView.setContentDescription(Editor.this.mTextView.getResources().getString(R.string.content_description_share));
            this.mPhraseTextView = newTextView();
            this.mMainPanel.addView(this.mPhraseTextView);
        }

        /* Access modifiers changed, original: protected */
        public void createAnimations() {
            this.mAnimationFadeIn = new AnimatorSet();
            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(this.mContentView, View.ALPHA, 0.0f, 1.0f);
            ObjectAnimator scaleAnimatorX = ObjectAnimator.ofFloat(this.mContentView, View.SCALE_X, 0.6f, 1.0f);
            ObjectAnimator scaleAnimatorY = ObjectAnimator.ofFloat(this.mContentView, View.SCALE_Y, 0.6f, 1.0f);
            this.mAnimationFadeIn.setInterpolator(new CubicEaseOutInterpolator());
            this.mAnimationFadeIn.setDuration(150);
            this.mAnimationFadeIn.playTogether(alphaAnimator, scaleAnimatorX, scaleAnimatorY);
            this.mAnimationFadeOut = new AnimatorSet();
            alphaAnimator = ObjectAnimator.ofFloat(this.mContentView, View.ALPHA, 1.0f, 0.0f);
            scaleAnimatorX = ObjectAnimator.ofFloat(this.mContentView, View.SCALE_X, 1.0f, 0.6f);
            scaleAnimatorY = ObjectAnimator.ofFloat(this.mContentView, View.SCALE_Y, 1.0f, 0.6f);
            this.mAnimationFadeOut.setInterpolator(new CubicEaseOutInterpolator());
            this.mAnimationFadeOut.setDuration(100);
            this.mAnimationFadeOut.playTogether(alphaAnimator, scaleAnimatorX, scaleAnimatorY);
            this.mAnimationFadeOutListener = new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    ((AnimatePopupWindow) ActionPopupWindow.this.mPopupWindow).dismiss(false);
                }
            };
        }

        private TextView newTextView() {
            TextView textView = (TextView) ((LayoutInflater) Editor.this.mTextView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate((int) R.layout.text_edit_action_popup_text, null);
            textView.setLayoutParams(new LinearLayout.LayoutParams(-2, -2, 1.0f));
            textView.setSingleLine();
            textView.setGravity(17);
            textView.setOnClickListener(this);
            return textView;
        }

        private ImageView newImageView() {
            LinearLayout.LayoutParams wrapContent = new LinearLayout.LayoutParams(-2, -2, 1.0f);
            ImageView imageView = new ImageView(Editor.this.mTextView.getContext());
            imageView.setLayoutParams(wrapContent);
            imageView.setBackgroundResource(R.drawable.text_select_button_bg);
            imageView.setScaleType(ScaleType.CENTER);
            imageView.setOnClickListener(this);
            return imageView;
        }

        /* JADX WARNING: Missing block: B:154:0x0271, code skipped:
            if (r2.getStatus() == android.os.AsyncTask.Status.FINISHED) goto L_0x0276;
     */
        public void show() {
            /*
            r24 = this;
            r0 = r24;
            r1 = android.widget.Editor.this;
            r1 = r1.mTextView;
            r1 = r1.getText();
            r2 = android.widget.Editor.this;
            r2 = r2.mTextView;
            r2 = r2.getSelectionStart();
            r3 = android.widget.Editor.this;
            r3 = r3.mTextView;
            r3 = r3.getSelectionEnd();
            r4 = android.widget.Editor.this;
            r4 = r4.isPasswordInputType();
            r5 = android.widget.Editor.this;
            r5 = r5.mTextView;
            r5 = android.widget.WindowLayoutParamsUtil.isInSystemWindow(r5);
            r6 = r1.length();
            r7 = 1;
            r8 = 0;
            if (r6 <= 0) goto L_0x0042;
        L_0x0038:
            r6 = r3 - r2;
            if (r6 <= 0) goto L_0x0042;
        L_0x003c:
            if (r4 != 0) goto L_0x0042;
        L_0x003e:
            if (r5 != 0) goto L_0x0042;
        L_0x0040:
            r6 = r7;
            goto L_0x0043;
        L_0x0042:
            r6 = r8;
        L_0x0043:
            r9 = 0;
            r10 = r0.mTelImageView;
            r11 = 0;
            r10.setTag(r11);
            r10 = r0.mFeatureTel;
            if (r10 == 0) goto L_0x006b;
        L_0x004e:
            r10 = r1.length();
            if (r10 <= 0) goto L_0x006b;
        L_0x0054:
            r10 = 4;
            r10 = android.widget.Linkify.getLinks(r1, r2, r3, r10);
            if (r10 == 0) goto L_0x006b;
        L_0x005b:
            r12 = r10.size();
            if (r12 != r7) goto L_0x006b;
        L_0x0061:
            r12 = r0.mTelImageView;
            r13 = r10.get(r8);
            r12.setTag(r13);
            r9 = 1;
        L_0x006b:
            r10 = 0;
            r12 = r0.mWebImageView;
            r12.setTag(r11);
            if (r9 != 0) goto L_0x008f;
        L_0x0073:
            r11 = r1.length();
            if (r11 <= 0) goto L_0x008f;
        L_0x0079:
            r11 = android.widget.Linkify.getLinks(r1, r2, r3, r7);
            if (r11 == 0) goto L_0x008f;
        L_0x007f:
            r12 = r11.size();
            if (r12 <= 0) goto L_0x008f;
        L_0x0085:
            r12 = r0.mWebImageView;
            r13 = r11.get(r8);
            r12.setTag(r13);
            r10 = 1;
        L_0x008f:
            r11 = android.widget.Editor.this;
            r11 = r11.mTextView;
            r11 = r11.canCopy();
            r12 = android.widget.Editor.this;
            r12 = r12.mTextView;
            r12 = r12.canCut();
            r13 = android.widget.Editor.this;
            r13 = r13.mTextView;
            r13 = r13.canPaste();
            r14 = android.widget.Editor.this;
            r14 = r14.mTextView;
            r14 = r14.isSuggestionsEnabled();
            if (r14 == 0) goto L_0x00db;
        L_0x00b9:
            r14 = android.widget.Editor.this;
            r14 = r14.shouldOfferToShowSuggestions();
            if (r14 == 0) goto L_0x00db;
        L_0x00c1:
            r14 = android.widget.Editor.this;
            r14 = r14.mTextView;
            r14 = r14.isInExtractedMode();
            if (r14 == 0) goto L_0x00d9;
        L_0x00cd:
            r14 = android.widget.Editor.this;
            r14 = r14.mTextView;
            r14 = r14.hasSelection();
            if (r14 != 0) goto L_0x00db;
        L_0x00d9:
            r14 = r7;
            goto L_0x00dc;
        L_0x00db:
            r14 = r8;
        L_0x00dc:
            r15 = r1.length();
            if (r15 <= 0) goto L_0x00f0;
        L_0x00e2:
            r15 = android.widget.Editor.this;
            r15 = r15.mTextView;
            r15 = r15.hasSelection();
            if (r15 != 0) goto L_0x00f0;
        L_0x00ee:
            r15 = r7;
            goto L_0x00f1;
        L_0x00f0:
            r15 = r8;
        L_0x00f1:
            r16 = r1.length();
            if (r16 <= 0) goto L_0x0101;
        L_0x00f7:
            if (r2 != 0) goto L_0x00ff;
        L_0x00f9:
            r7 = r1.length();
            if (r3 == r7) goto L_0x0101;
        L_0x00ff:
            r7 = 1;
            goto L_0x0102;
        L_0x0101:
            r7 = r8;
        L_0x0102:
            r17 = r1.length();
            if (r17 <= 0) goto L_0x011a;
        L_0x0108:
            r8 = android.widget.Editor.this;
            r8 = r8.mTextView;
            r8 = r8.hasSelection();
            if (r8 == 0) goto L_0x011a;
        L_0x0114:
            if (r4 != 0) goto L_0x011a;
        L_0x0116:
            if (r5 != 0) goto L_0x011a;
        L_0x0118:
            r8 = 1;
            goto L_0x011b;
        L_0x011a:
            r8 = 0;
        L_0x011b:
            r18 = r2;
            r2 = android.widget.Editor.this;
            r2 = r2.mTextView;
            r2 = r2.hasSelection();
            if (r2 != 0) goto L_0x0149;
        L_0x0129:
            r2 = android.widget.Editor.this;
            r2 = r2.mTextView;
            r2 = r2.getContext();
            r2 = com.android.internal.phrase.Phrases.Utils.isAddPhraseActivity(r2);
            if (r2 != 0) goto L_0x0149;
        L_0x0139:
            r2 = android.widget.Editor.this;
            r2 = r2.mTextView;
            r2 = r2.isInExtractedMode();
            if (r2 != 0) goto L_0x0149;
        L_0x0145:
            if (r5 != 0) goto L_0x0149;
        L_0x0147:
            r2 = 1;
            goto L_0x014a;
        L_0x0149:
            r2 = 0;
        L_0x014a:
            r19 = r3;
            r3 = r0.mTranslationManager;
            if (r3 != 0) goto L_0x0168;
        L_0x0150:
            r3 = new android.widget.TranslationManager;
            r20 = r2;
            r2 = android.widget.Editor.this;
            r2 = r2.mTextView;
            r2 = r2.getContext();
            r21 = r8;
            r8 = r0.mTranslationHandler;
            r3.<init>(r2, r8);
            r0.mTranslationManager = r3;
            goto L_0x016c;
        L_0x0168:
            r20 = r2;
            r21 = r8;
        L_0x016c:
            r2 = r1.length();
            if (r2 <= 0) goto L_0x018c;
        L_0x0172:
            r2 = android.widget.Editor.this;
            r2 = r2.mTextView;
            r2 = r2.hasSelection();
            if (r2 == 0) goto L_0x018c;
        L_0x017e:
            r2 = r0.mTranslationManager;
            r2 = r2.isAvailable();
            if (r2 == 0) goto L_0x018c;
        L_0x0186:
            if (r4 != 0) goto L_0x018c;
        L_0x0188:
            if (r5 != 0) goto L_0x018c;
        L_0x018a:
            r2 = 1;
            goto L_0x018d;
        L_0x018c:
            r2 = 0;
        L_0x018d:
            r3 = android.widget.Editor.this;
            r3 = r3.mTextView;
            r3 = r3.canRequestAutofill();
            if (r3 == 0) goto L_0x01b7;
        L_0x0199:
            r3 = android.widget.Editor.this;
            r3 = r3.mTextView;
            r3 = r3.getSelectedText();
            if (r3 == 0) goto L_0x01b5;
        L_0x01a5:
            r3 = android.widget.Editor.this;
            r3 = r3.mTextView;
            r3 = r3.getSelectedText();
            r3 = r3.isEmpty();
            if (r3 == 0) goto L_0x01b7;
        L_0x01b5:
            r3 = 1;
            goto L_0x01b8;
        L_0x01b7:
            r3 = 0;
        L_0x01b8:
            r8 = r0.mSearchImageView;
            r22 = r1;
            if (r6 == 0) goto L_0x01c0;
        L_0x01be:
            r1 = 0;
            goto L_0x01c2;
        L_0x01c0:
            r1 = 8;
        L_0x01c2:
            r8.setVisibility(r1);
            r1 = r0.mTelImageView;
            if (r9 == 0) goto L_0x01cb;
        L_0x01c9:
            r8 = 0;
            goto L_0x01cd;
        L_0x01cb:
            r8 = 8;
        L_0x01cd:
            r1.setVisibility(r8);
            r1 = r0.mWebImageView;
            if (r10 == 0) goto L_0x01d6;
        L_0x01d4:
            r8 = 0;
            goto L_0x01d8;
        L_0x01d6:
            r8 = 8;
        L_0x01d8:
            r1.setVisibility(r8);
            r1 = r0.mCopyTextView;
            if (r11 == 0) goto L_0x01e1;
        L_0x01df:
            r8 = 0;
            goto L_0x01e3;
        L_0x01e1:
            r8 = 8;
        L_0x01e3:
            r1.setVisibility(r8);
            r1 = r0.mCutTextView;
            if (r12 == 0) goto L_0x01ec;
        L_0x01ea:
            r8 = 0;
            goto L_0x01ee;
        L_0x01ec:
            r8 = 8;
        L_0x01ee:
            r1.setVisibility(r8);
            r1 = r0.mPasteTextView;
            if (r13 == 0) goto L_0x01f7;
        L_0x01f5:
            r8 = 0;
            goto L_0x01f9;
        L_0x01f7:
            r8 = 8;
        L_0x01f9:
            r1.setVisibility(r8);
            r1 = r0.mReplaceTextView;
            if (r14 == 0) goto L_0x0202;
        L_0x0200:
            r8 = 0;
            goto L_0x0204;
        L_0x0202:
            r8 = 8;
        L_0x0204:
            r1.setVisibility(r8);
            r1 = r0.mSelectTextView;
            if (r15 == 0) goto L_0x020d;
        L_0x020b:
            r8 = 0;
            goto L_0x020f;
        L_0x020d:
            r8 = 8;
        L_0x020f:
            r1.setVisibility(r8);
            r1 = r0.mSelectAllTextView;
            if (r7 == 0) goto L_0x0218;
        L_0x0216:
            r8 = 0;
            goto L_0x021a;
        L_0x0218:
            r8 = 8;
        L_0x021a:
            r1.setVisibility(r8);
            r1 = r0.mTranslationImageView;
            if (r2 == 0) goto L_0x0223;
        L_0x0221:
            r8 = 0;
            goto L_0x0225;
        L_0x0223:
            r8 = 8;
        L_0x0225:
            r1.setVisibility(r8);
            r1 = r0.mShareImageView;
            if (r21 == 0) goto L_0x022e;
        L_0x022c:
            r8 = 0;
            goto L_0x0230;
        L_0x022e:
            r8 = 8;
        L_0x0230:
            r1.setVisibility(r8);
            r1 = r0.mPhraseTextView;
            if (r20 == 0) goto L_0x0239;
        L_0x0237:
            r8 = 0;
            goto L_0x023b;
        L_0x0239:
            r8 = 8;
        L_0x023b:
            r1.setVisibility(r8);
            r1 = r0.mAutoFillTextView;
            if (r3 == 0) goto L_0x0244;
        L_0x0242:
            r8 = 0;
            goto L_0x0246;
        L_0x0244:
            r8 = 8;
        L_0x0246:
            r1.setVisibility(r8);
            if (r11 != 0) goto L_0x0258;
        L_0x024b:
            if (r12 != 0) goto L_0x0258;
        L_0x024d:
            if (r13 != 0) goto L_0x0258;
        L_0x024f:
            if (r14 != 0) goto L_0x0258;
        L_0x0251:
            if (r15 != 0) goto L_0x0258;
        L_0x0253:
            if (r7 != 0) goto L_0x0258;
        L_0x0255:
            r16 = 1;
            goto L_0x025a;
        L_0x0258:
            r16 = 0;
        L_0x025a:
            r1 = r16;
            if (r20 == 0) goto L_0x0290;
        L_0x025e:
            r8 = new android.widget.Editor$ActionPopupWindow$3;
            r8.<init>(r4, r1);
            r16 = r2;
            r2 = r0.mQueryPhraseTask;
            if (r2 == 0) goto L_0x0274;
        L_0x0269:
            r2 = r2.getStatus();
            r23 = r3;
            r3 = android.os.AsyncTask.Status.FINISHED;
            if (r2 != r3) goto L_0x028f;
        L_0x0273:
            goto L_0x0276;
        L_0x0274:
            r23 = r3;
        L_0x0276:
            r2 = new com.android.internal.phrase.QueryPhraseTask;
            r3 = android.widget.Editor.this;
            r3 = r3.mTextView;
            r3 = r3.getContext();
            r2.<init>(r3, r8);
            r0.mQueryPhraseTask = r2;
            r2 = r0.mQueryPhraseTask;
            r3 = 0;
            r3 = new java.lang.Void[r3];
            r2.execute(r3);
        L_0x028f:
            goto L_0x02a5;
        L_0x0290:
            r16 = r2;
            r23 = r3;
            if (r1 == 0) goto L_0x0297;
        L_0x0296:
            return;
        L_0x0297:
            r2 = r0.mPhraseTextView;
            r3 = 8;
            r2.setVisibility(r3);
            r2 = 0;
            r0.setMainPanelChildPadding(r2);
            super.show();
        L_0x02a5:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.widget.Editor$ActionPopupWindow.show():void");
        }

        /* JADX WARNING: Removed duplicated region for block: B:33:0x00a9  */
        /* JADX WARNING: Removed duplicated region for block: B:32:0x008d  */
        /* JADX WARNING: Removed duplicated region for block: B:32:0x008d  */
        /* JADX WARNING: Removed duplicated region for block: B:33:0x00a9  */
        private void setMainPanelChildPadding(boolean r11) {
            /*
            r10 = this;
            r0 = r10.mMainPanel;
            r0 = r0.getChildCount();
            r1 = r10.mVisibleChildren;
            r1.clear();
            r1 = 0;
        L_0x000c:
            if (r1 >= r0) goto L_0x0022;
        L_0x000e:
            r2 = r10.mMainPanel;
            r2 = r2.getChildAt(r1);
            r3 = r2.getVisibility();
            if (r3 != 0) goto L_0x001f;
        L_0x001a:
            r3 = r10.mVisibleChildren;
            r3.add(r2);
        L_0x001f:
            r1 = r1 + 1;
            goto L_0x000c;
        L_0x0022:
            r1 = r10.mVisibleChildren;
            r1 = r1.size();
            r2 = miui.os.Build.IS_TABLET;
            r3 = 5;
            if (r2 != 0) goto L_0x0038;
        L_0x002d:
            r2 = miui.os.Build.IS_INTERNATIONAL_BUILD;
            if (r2 == 0) goto L_0x0033;
        L_0x0031:
            r2 = r3;
            goto L_0x0034;
        L_0x0033:
            r2 = 6;
        L_0x0034:
            if (r1 < r2) goto L_0x0038;
        L_0x0036:
            r2 = 0;
            goto L_0x0089;
        L_0x0038:
            r2 = miui.os.Build.IS_TABLET;
            if (r2 != 0) goto L_0x005f;
        L_0x003c:
            r2 = java.util.Locale.getDefault();
            r2 = r2.getLanguage();
            r4 = java.util.Locale.US;
            r4 = r4.getLanguage();
            r2 = r2.equals(r4);
            if (r2 == 0) goto L_0x005f;
        L_0x0050:
            if (r11 == 0) goto L_0x005f;
        L_0x0052:
            if (r1 < r3) goto L_0x005f;
        L_0x0054:
            r2 = r10.mTextActionPadding;
            r2 = (double) r2;
            r4 = 4600877379321698714; // 0x3fd999999999999a float:-1.5881868E-23 double:0.4;
            r2 = r2 * r4;
            r2 = (int) r2;
            goto L_0x0089;
        L_0x005f:
            r2 = miui.os.Build.IS_TABLET;
            if (r2 != 0) goto L_0x0087;
        L_0x0063:
            r2 = java.util.Locale.getDefault();
            r2 = r2.getLanguage();
            r3 = java.util.Locale.US;
            r3 = r3.getLanguage();
            r2 = r2.equals(r3);
            if (r2 == 0) goto L_0x0087;
        L_0x0077:
            if (r11 == 0) goto L_0x0087;
        L_0x0079:
            r2 = 4;
            if (r1 < r2) goto L_0x0087;
        L_0x007c:
            r2 = r10.mTextActionPadding;
            r2 = (double) r2;
            r4 = 4603579539098121011; // 0x3fe3333333333333 float:4.172325E-8 double:0.6;
            r2 = r2 * r4;
            r2 = (int) r2;
            goto L_0x0089;
        L_0x0087:
            r2 = r10.mTextActionPadding;
        L_0x0089:
            r3 = 0;
            r4 = 1;
            if (r1 != r4) goto L_0x00a9;
        L_0x008d:
            r4 = r10.mVisibleChildren;
            r3 = r4.get(r3);
            r3 = (android.view.View) r3;
            r4 = r3.getBackground();
            r5 = 3;
            r4.setLevel(r5);
            r5 = r3.getPaddingTop();
            r6 = r3.getPaddingBottom();
            r3.setPadding(r2, r5, r2, r6);
            goto L_0x00d8;
        L_0x00a9:
            r5 = 0;
        L_0x00aa:
            if (r5 >= r1) goto L_0x00d8;
        L_0x00ac:
            r6 = r10.mVisibleChildren;
            r6 = r6.get(r5);
            r6 = (android.view.View) r6;
            r7 = r6.getBackground();
            if (r5 != 0) goto L_0x00be;
        L_0x00ba:
            r7.setLevel(r3);
            goto L_0x00ca;
        L_0x00be:
            r8 = r1 + -1;
            if (r5 != r8) goto L_0x00c7;
        L_0x00c2:
            r8 = 2;
            r7.setLevel(r8);
            goto L_0x00ca;
        L_0x00c7:
            r7.setLevel(r4);
        L_0x00ca:
            r8 = r6.getPaddingTop();
            r9 = r6.getPaddingBottom();
            r6.setPadding(r2, r8, r2, r9);
            r5 = r5 + 1;
            goto L_0x00aa;
        L_0x00d8:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.widget.Editor$ActionPopupWindow.setMainPanelChildPadding(boolean):void");
        }

        private void startAddPhraseActivity() {
            Intent intent = Utils.getAddPhraseIntent();
            intent.addFlags(268435456);
            Editor.this.mTextView.getContext().startActivity(intent);
        }

        public void onClick(View view) {
            int clickItem;
            int clickItem2;
            View view2 = view;
            int min = 0;
            int max = Editor.this.mTextView.getText().length();
            if (Editor.this.mTextView.isFocused()) {
                int selStart = Editor.this.mTextView.getSelectionStart();
                int selEnd = Editor.this.mTextView.getSelectionEnd();
                min = Math.max(0, Math.min(selStart, selEnd));
                max = Math.max(0, Math.max(selStart, selEnd));
            }
            if (view2 == this.mSelectTextView) {
                if (Editor.this.hasSelectionController()) {
                    Editor.this.getSelectionController().setMinTouchOffset(min);
                    Editor.this.getSelectionController().setMaxTouchOffset(max);
                }
                Editor.this.startSelectionActionMode();
                clickItem = -1;
            } else {
                Intent intent;
                Editor editor;
                if (view2 == this.mSelectAllTextView) {
                    Editor.this.mTextView.onTextContextMenuItem(16908319);
                    Editor.this.startSelectionActionMode();
                    clickItem2 = 16908319;
                } else if (view2 == this.mPasteTextView && Editor.this.mTextView.canPaste()) {
                    Editor.this.mTextView.onTextContextMenuItem(16908322);
                    hide();
                    clickItem2 = 16908322;
                } else if (view2 == this.mReplaceTextView) {
                    Editor.this.replace();
                    clickItem2 = 16908340;
                } else if (view2 == this.mCopyTextView) {
                    Editor.this.mTextView.onTextContextMenuItem(16908321);
                    Selection.setSelection((Spannable) Editor.this.mTextView.getText(), Editor.this.mTextView.getSelectionEnd(), Editor.this.mTextView.getSelectionEnd());
                    clickItem2 = 16908321;
                } else if (view2 == this.mCutTextView) {
                    Editor.this.mTextView.onTextContextMenuItem(16908320);
                    clickItem2 = 16908320;
                } else if (view2 == this.mShareImageView) {
                    intent = new Intent(Intent.ACTION_SEND);
                    intent.setType(ClipDescription.MIMETYPE_TEXT_PLAIN);
                    intent.putExtra(Intent.EXTRA_TEXT, Editor.this.mTextView.getText().subSequence(min, max).toString());
                    Context context = Editor.this.mTextView.getContext();
                    Editor.this.startActivityFromContext(context, Intent.createChooser(intent, context.getString(com.android.internal.R.string.share)));
                    Selection.setSelection((Spannable) Editor.this.mTextView.getText(), (min + max) / 2);
                    clickItem2 = 16908341;
                } else if (view2 == this.mSearchImageView) {
                    if (Build.IS_INTERNATIONAL_BUILD) {
                        intent = new Intent(Intent.ACTION_WEB_SEARCH);
                        intent.putExtra("query", Editor.this.mTextView.getText().subSequence(min, max).toString());
                        Editor editor2 = Editor.this;
                        editor2.startActivityFromContext(editor2.mTextView.getContext(), intent);
                    } else {
                        intent = new Intent(Intent.ACTION_SEARCH);
                        String path = new StringBuilder();
                        path.append("qsb://query?words=");
                        path.append(Editor.this.mTextView.getText().subSequence(min, max).toString());
                        path.append("&ref=miuiEditor&web_search=true");
                        intent.setData(Uri.parse(path.toString()));
                        editor = Editor.this;
                        editor.startActivityFromContext(editor.mTextView.getContext(), intent);
                    }
                    Selection.setSelection((Spannable) Editor.this.mTextView.getText(), (min + max) / 2);
                    clickItem2 = 16908341;
                } else {
                    View view3 = this.mTelImageView;
                    LinkSpec link;
                    Intent intent2;
                    if (view2 == view3) {
                        link = (LinkSpec) view3.getTag();
                        if (link != null) {
                            intent2 = new Intent(Intent.ACTION_DIAL, Uri.parse(link.url));
                            editor = Editor.this;
                            editor.startActivityFromContext(editor.mTextView.getContext(), intent2);
                            if (min != max) {
                                Selection.setSelection((Spannable) Editor.this.mTextView.getText(), (min + max) / 2);
                            }
                        }
                        clickItem = -1;
                    } else {
                        view3 = this.mWebImageView;
                        if (view2 == view3) {
                            link = (LinkSpec) view3.getTag();
                            if (link != null) {
                                intent2 = new Intent("android.intent.action.VIEW", Uri.parse(link.url));
                                editor = Editor.this;
                                editor.startActivityFromContext(editor.mTextView.getContext(), intent2);
                                if (min != max) {
                                    Selection.setSelection((Spannable) Editor.this.mTextView.getText(), (min + max) / 2);
                                }
                            }
                            clickItem = -1;
                        } else {
                            view3 = this.mPhraseTextView;
                            String str = Context.LAYOUT_INFLATER_SERVICE;
                            if (view2 == view3) {
                                ArrayList arrayList = this.mPhrases;
                                if (arrayList == null) {
                                    clickItem = -1;
                                } else if (arrayList.size() == 0) {
                                    clickItem = -1;
                                } else {
                                    LayoutInflater inflater = (LayoutInflater) Editor.this.mTextView.getContext().getSystemService(str);
                                    View phraseListLayout = inflater.inflate((int) R.layout.phrase_list, null);
                                    ImageButton editBtn = (ImageButton) phraseListLayout.findViewById(R.id.edit_phrase);
                                    ImageButton addBtn = (ImageButton) phraseListLayout.findViewById(R.id.add_phrase);
                                    if (Editor.this.isPasswordInputType()) {
                                        editBtn.setVisibility(8);
                                        addBtn.setVisibility(8);
                                    } else {
                                        editBtn.setVisibility(0);
                                        editBtn.setOnClickListener(new OnClickListener() {
                                            public void onClick(View v) {
                                                Intent intent = Utils.getPhraseEditIntent();
                                                intent.addFlags(268435456);
                                                Editor.this.mTextView.getContext().startActivity(intent);
                                            }
                                        });
                                        addBtn.setVisibility(0);
                                        addBtn.setOnClickListener(new OnClickListener() {
                                            public void onClick(View v) {
                                                ActionPopupWindow.this.startAddPhraseActivity();
                                            }
                                        });
                                    }
                                    ListView phraseListView = (ListView) phraseListLayout.findViewById(16908298);
                                    phraseListView.setOverScrollMode(2);
                                    phraseListView.setAdapter(new PhraseAdapter(this.mPhrases));
                                    final int minimum = min;
                                    final int maximum = max;
                                    phraseListView.setOnItemClickListener(new OnItemClickListener() {
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            String paste = (String) parent.getAdapter().getItem(position);
                                            CharSequence text = Editor.this.mTextView.getText();
                                            Selection.setSelection((Spannable) text, maximum);
                                            ((Editable) text).replace(minimum, maximum, paste);
                                        }
                                    });
                                    int width = Editor.this.mTextView.getResources().getDimensionPixelSize(R.dimen.phrase_list_width);
                                    int height = -2;
                                    clickItem = -1;
                                    if (this.mPhrases.size() > 2) {
                                        height = getMaxPhraseListHeight();
                                    }
                                    phraseListLayout.setLayoutParams(new LayoutParams(width, height));
                                    setSubPanelAsContent(phraseListLayout, Editor.this.mTextView.getResources().getDrawable(R.drawable.text_select_bg, Editor.this.mTextView.getContext().getTheme()));
                                }
                                startAddPhraseActivity();
                            } else {
                                clickItem = -1;
                                if (view2 == this.mTranslationImageView) {
                                    View view4 = this.mTranslationPanel;
                                    if (view4 == null) {
                                        this.mTranslationPanel = ((LayoutInflater) Editor.this.mTextView.getContext().getSystemService(str)).inflate((int) R.layout.translation_panel, null);
                                    } else if (view4.getParent() != null) {
                                        ((ViewGroup) this.mTranslationPanel.getParent()).removeView(this.mTranslationPanel);
                                    }
                                    if (this.mTranslationPresenter == null) {
                                        this.mTranslationPresenter = new TranslationPresenter(Editor.this.mTextView.getContext(), this.mTranslationPanel);
                                    }
                                    DisplayMetrics displayMetrics = Editor.this.mTextView.getResources().getDisplayMetrics();
                                    Drawable background = Editor.this.mTextView.getResources().getDrawable(R.drawable.text_select_bg, Editor.this.mTextView.getContext().getTheme());
                                    Rect paddings = new Rect();
                                    background.getPadding(paddings);
                                    this.mTranslationPanel.setLayoutParams(new LayoutParams((displayMetrics.widthPixels - paddings.left) - paddings.right, (Editor.this.mTextView.getResources().getDimensionPixelSize(R.dimen.text_action_translation_max_height) + paddings.top) + paddings.bottom));
                                    setSubPanelAsContent(this.mTranslationPanel, null);
                                    this.mTranslationPresenter.setAboveHandle(this.mAboveHandle);
                                    this.mTranslationPresenter.setInProgress();
                                    this.mTranslationManager.translate(null, null, Editor.this.mTextView.getTransformedText(min, max).toString());
                                } else if (view2 == this.mAutoFillTextView) {
                                    Editor.this.mTextView.onTextContextMenuItem(16908355);
                                }
                            }
                        }
                    }
                }
                Editor.this.getSelectionActionModeHelper().onSelectionAction(clickItem2);
            }
            clickItem2 = clickItem;
            Editor.this.getSelectionActionModeHelper().onSelectionAction(clickItem2);
        }

        public void fadeIn(int x, int y) {
            this.mContentView.setPivotX((float) (this.mContentView.getMeasuredWidth() / 2));
            this.mContentView.setPivotY(this.mAboveHandle ? (float) this.mContentView.getMeasuredHeight() : 0.0f);
            this.mAnimationFadeIn.start();
        }

        /* Access modifiers changed, original: protected */
        public void updatePosition(int parentPositionX, int parentPositionY) {
            AnimatePopupWindow popupWindow = this.mPopupWindow;
            if (popupWindow.isDismissing()) {
                popupWindow.dismiss(false);
            }
            super.updatePosition(parentPositionX, parentPositionY);
        }

        public void dismiss() {
            ((AnimatePopupWindow) this.mPopupWindow).dismiss(true);
            setZeroTouchableSurface();
        }

        private int getMaxPhraseListHeight() {
            int extraHeight;
            Resources res = Editor.this.mTextView.getResources();
            Theme theme = Editor.this.mTextView.getContext().getTheme();
            int header = res.getDrawable(R.drawable.phrase_list_header_bg, theme).getIntrinsicHeight();
            Drawable bg = res.getDrawable(R.drawable.phrase_list_item_bg, theme);
            bg.setLevel(0);
            int first = bg.getIntrinsicHeight();
            bg.setLevel(1);
            int middle = bg.getIntrinsicHeight();
            bg.setLevel(2);
            int last = bg.getIntrinsicHeight();
            int height = (header + first) + middle;
            if (this.mPhrases.size() == 3) {
                extraHeight = last;
            } else if (this.mPhrases.size() == 4) {
                extraHeight = middle + (last / 2);
            } else {
                extraHeight = middle + (middle / 2);
            }
            return height + extraHeight;
        }
    }

    private class AnimatePopupWindow extends PopupWindow {
        private boolean mDismissing;
        private Fader mFader;

        /* synthetic */ AnimatePopupWindow(Editor x0, Context x1, AnonymousClass1 x2) {
            this(x1);
        }

        private AnimatePopupWindow(Context context) {
            super(context, null, 0);
            this.mDismissing = false;
            if (this.mFader == null) {
                setAnimationStyle(R.style.Animation_TextSelect);
            }
        }

        public AnimatePopupWindow(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            this.mDismissing = false;
        }

        public void showAtLocation(View parent, int gravity, int x, int y) {
            this.mDismissing = false;
            Fader fader = this.mFader;
            if (fader != null) {
                fader.cancelAnimations();
            }
            super.showAtLocation(parent, gravity, x, y);
            fader = this.mFader;
            if (fader != null) {
                fader.fadeIn(x, y);
            }
        }

        public void update(int x, int y, int width, int height) {
            this.mDismissing = false;
            super.update(x, y, width, height);
            Fader fader = this.mFader;
            if (fader != null) {
                fader.setY(y);
            }
        }

        public void dismiss(boolean animation) {
            if (!animation || !this.mDismissing) {
                Fader fader = this.mFader;
                if (fader != null) {
                    fader.cancelAnimations();
                }
                if (animation) {
                    fader = this.mFader;
                    if (fader != null) {
                        this.mDismissing = true;
                        fader.fadeOut();
                    }
                }
                this.mDismissing = false;
                dismiss();
            }
        }

        public boolean isDismissing() {
            return this.mDismissing;
        }

        public void setFader(Fader fader) {
            this.mFader = fader;
        }
    }

    private class Blink implements Runnable {
        private boolean mCancelled;

        private Blink() {
        }

        /* synthetic */ Blink(Editor x0, AnonymousClass1 x1) {
            this();
        }

        public void run() {
            if (!this.mCancelled) {
                Editor.this.mTextView.removeCallbacks(this);
                if (Editor.this.shouldBlink()) {
                    if (Editor.this.mTextView.getLayout() != null) {
                        Editor.this.mTextView.invalidateCursorPath();
                    }
                    Editor.this.mTextView.postDelayed(this, 500);
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void cancel() {
            if (!this.mCancelled) {
                Editor.this.mTextView.removeCallbacks(this);
                this.mCancelled = true;
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void uncancel() {
            this.mCancelled = false;
        }
    }

    private class CorrectionHighlighter {
        private static final int FADE_OUT_DURATION = 400;
        private int mEnd;
        private long mFadingStartTime;
        private final Paint mPaint = new Paint(1);
        private final Path mPath = new Path();
        private int mStart;
        private RectF mTempRectF;

        public CorrectionHighlighter() {
            this.mPaint.setCompatibilityScaling(Editor.this.mTextView.getResources().getCompatibilityInfo().applicationScale);
            this.mPaint.setStyle(Style.FILL);
        }

        public void highlight(CorrectionInfo info) {
            this.mStart = info.getOffset();
            this.mEnd = this.mStart + info.getNewText().length();
            this.mFadingStartTime = SystemClock.uptimeMillis();
            if (this.mStart < 0 || this.mEnd < 0) {
                stopAnimation();
            }
        }

        public void draw(Canvas canvas, int cursorOffsetVertical) {
            if (updatePath() && updatePaint()) {
                if (cursorOffsetVertical != 0) {
                    canvas.translate(0.0f, (float) cursorOffsetVertical);
                }
                canvas.drawPath(this.mPath, this.mPaint);
                if (cursorOffsetVertical != 0) {
                    canvas.translate(0.0f, (float) (-cursorOffsetVertical));
                }
                invalidate(true);
                return;
            }
            stopAnimation();
            invalidate(false);
        }

        private boolean updatePaint() {
            long duration = SystemClock.uptimeMillis() - this.mFadingStartTime;
            if (duration > 400) {
                return false;
            }
            this.mPaint.setColor((Editor.this.mTextView.mHighlightColor & 16777215) + (((int) (((float) Color.alpha(Editor.this.mTextView.mHighlightColor)) * (1.0f - (((float) duration) / 400.0f)))) << 24));
            return true;
        }

        private boolean updatePath() {
            Layout layout = Editor.this.mTextView.getLayout();
            if (layout == null) {
                return false;
            }
            int length = Editor.this.mTextView.getText().length();
            int start = Math.min(length, this.mStart);
            int end = Math.min(length, this.mEnd);
            this.mPath.reset();
            layout.getSelectionPath(start, end, this.mPath);
            return true;
        }

        private void invalidate(boolean delayed) {
            if (Editor.this.mTextView.getLayout() != null) {
                if (this.mTempRectF == null) {
                    this.mTempRectF = new RectF();
                }
                this.mPath.computeBounds(this.mTempRectF, false);
                int left = Editor.this.mTextView.getCompoundPaddingLeft();
                int top = Editor.this.mTextView.getExtendedPaddingTop() + Editor.this.mTextView.getVerticalOffset(true);
                if (delayed) {
                    Editor.this.mTextView.postInvalidateOnAnimation(((int) this.mTempRectF.left) + left, ((int) this.mTempRectF.top) + top, ((int) this.mTempRectF.right) + left, ((int) this.mTempRectF.bottom) + top);
                } else {
                    Editor.this.mTextView.postInvalidate((int) this.mTempRectF.left, (int) this.mTempRectF.top, (int) this.mTempRectF.right, (int) this.mTempRectF.bottom);
                }
            }
        }

        private void stopAnimation() {
            Editor.this.mCorrectionHighlighter = null;
        }
    }

    private interface CursorController extends OnTouchModeChangeListener {
        void hide();

        boolean isActive();

        boolean isCursorBeingModified();

        void onDetached();

        void show();
    }

    private static class DragLocalState {
        public int end;
        public TextView sourceTextView;
        public int start;

        public DragLocalState(TextView sourceTextView, int start, int end) {
            this.sourceTextView = sourceTextView;
            this.start = start;
            this.end = end;
        }
    }

    private interface EasyEditDeleteListener {
        void onDeleteClick(EasyEditSpan easyEditSpan);
    }

    private class EasyEditPopupWindow extends PinnedPopupWindow implements OnClickListener {
        private static final int POPUP_TEXT_LAYOUT = 17367333;
        private TextView mDeleteTextView;
        private EasyEditSpan mEasyEditSpan;
        private EasyEditDeleteListener mOnDeleteListener;

        private EasyEditPopupWindow() {
            super();
        }

        /* synthetic */ EasyEditPopupWindow(Editor x0, AnonymousClass1 x1) {
            this();
        }

        /* Access modifiers changed, original: protected */
        public void createPopupWindow() {
            this.mPopupWindow = new PopupWindow(Editor.this.mTextView.getContext(), null, 16843464);
            this.mPopupWindow.setInputMethodMode(2);
            this.mPopupWindow.setClippingEnabled(true);
        }

        /* Access modifiers changed, original: protected */
        public void initContentView() {
            LinearLayout linearLayout = new LinearLayout(Editor.this.mTextView.getContext());
            linearLayout.setOrientation(0);
            this.mContentView = linearLayout;
            this.mContentView.setBackgroundResource(com.android.internal.R.drawable.text_edit_side_paste_window);
            LayoutInflater inflater = (LayoutInflater) Editor.this.mTextView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LayoutParams wrapContent = new LayoutParams(-2, -2);
            this.mDeleteTextView = (TextView) inflater.inflate(17367333, null);
            this.mDeleteTextView.setLayoutParams(wrapContent);
            this.mDeleteTextView.setText((int) com.android.internal.R.string.delete);
            this.mDeleteTextView.setOnClickListener(this);
            this.mContentView.addView(this.mDeleteTextView);
        }

        public void setEasyEditSpan(EasyEditSpan easyEditSpan) {
            this.mEasyEditSpan = easyEditSpan;
        }

        private void setOnDeleteListener(EasyEditDeleteListener listener) {
            this.mOnDeleteListener = listener;
        }

        public void onClick(View view) {
            if (view == this.mDeleteTextView) {
                EasyEditSpan easyEditSpan = this.mEasyEditSpan;
                if (easyEditSpan != null && easyEditSpan.isDeleteEnabled()) {
                    EasyEditDeleteListener easyEditDeleteListener = this.mOnDeleteListener;
                    if (easyEditDeleteListener != null) {
                        easyEditDeleteListener.onDeleteClick(this.mEasyEditSpan);
                    }
                }
            }
        }

        public void hide() {
            EasyEditSpan easyEditSpan = this.mEasyEditSpan;
            if (easyEditSpan != null) {
                easyEditSpan.setDeleteEnabled(false);
            }
            this.mOnDeleteListener = null;
            super.hide();
        }

        /* Access modifiers changed, original: protected */
        public int getTextOffset() {
            return ((Editable) Editor.this.mTextView.getText()).getSpanEnd(this.mEasyEditSpan);
        }

        /* Access modifiers changed, original: protected */
        public int getVerticalLocalPosition(int line) {
            return Editor.this.mTextView.getLayout().getLineBottom(line);
        }

        /* Access modifiers changed, original: protected */
        public int clipVertically(int positionY) {
            return positionY;
        }
    }

    public static class EditOperation extends UndoOperation<Editor> {
        public static final ClassLoaderCreator<EditOperation> CREATOR = new ClassLoaderCreator<EditOperation>() {
            public EditOperation createFromParcel(Parcel in) {
                return new EditOperation(in, null);
            }

            public EditOperation createFromParcel(Parcel in, ClassLoader loader) {
                return new EditOperation(in, loader);
            }

            public EditOperation[] newArray(int size) {
                return new EditOperation[size];
            }
        };
        private static final int TYPE_DELETE = 1;
        private static final int TYPE_INSERT = 0;
        private static final int TYPE_REPLACE = 2;
        private boolean mFrozen;
        private boolean mIsComposition;
        private int mNewCursorPos;
        private String mNewText;
        private int mOldCursorPos;
        private String mOldText;
        private int mStart;
        private int mType;

        public EditOperation(Editor editor, String oldText, int dstart, String newText, boolean isComposition) {
            super(editor.mUndoOwner);
            this.mOldText = oldText;
            this.mNewText = newText;
            if (this.mNewText.length() > 0 && this.mOldText.length() == 0) {
                this.mType = 0;
            } else if (this.mNewText.length() != 0 || this.mOldText.length() <= 0) {
                this.mType = 2;
            } else {
                this.mType = 1;
            }
            this.mStart = dstart;
            this.mOldCursorPos = editor.mTextView.getSelectionStart();
            this.mNewCursorPos = this.mNewText.length() + dstart;
            this.mIsComposition = isComposition;
        }

        public EditOperation(Parcel src, ClassLoader loader) {
            super(src, loader);
            this.mType = src.readInt();
            this.mOldText = src.readString();
            this.mNewText = src.readString();
            this.mStart = src.readInt();
            this.mOldCursorPos = src.readInt();
            this.mNewCursorPos = src.readInt();
            boolean z = false;
            this.mFrozen = src.readInt() == 1;
            if (src.readInt() == 1) {
                z = true;
            }
            this.mIsComposition = z;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mType);
            dest.writeString(this.mOldText);
            dest.writeString(this.mNewText);
            dest.writeInt(this.mStart);
            dest.writeInt(this.mOldCursorPos);
            dest.writeInt(this.mNewCursorPos);
            dest.writeInt(this.mFrozen);
            dest.writeInt(this.mIsComposition);
        }

        private int getNewTextEnd() {
            return this.mStart + this.mNewText.length();
        }

        private int getOldTextEnd() {
            return this.mStart + this.mOldText.length();
        }

        public void commit() {
        }

        public void undo() {
            modifyText((Editable) ((Editor) getOwnerData()).mTextView.getText(), this.mStart, getNewTextEnd(), this.mOldText, this.mStart, this.mOldCursorPos);
        }

        public void redo() {
            modifyText((Editable) ((Editor) getOwnerData()).mTextView.getText(), this.mStart, getOldTextEnd(), this.mNewText, this.mStart, this.mNewCursorPos);
        }

        private boolean mergeWith(EditOperation edit) {
            if (this.mFrozen) {
                return false;
            }
            int i = this.mType;
            if (i == 0) {
                return mergeInsertWith(edit);
            }
            if (i == 1) {
                return mergeDeleteWith(edit);
            }
            if (i != 2) {
                return false;
            }
            return mergeReplaceWith(edit);
        }

        private boolean mergeInsertWith(EditOperation edit) {
            int i = edit.mType;
            StringBuilder stringBuilder;
            if (i == 0) {
                if (getNewTextEnd() != edit.mStart) {
                    return false;
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.mNewText);
                stringBuilder.append(edit.mNewText);
                this.mNewText = stringBuilder.toString();
                this.mNewCursorPos = edit.mNewCursorPos;
                this.mFrozen = edit.mFrozen;
                this.mIsComposition = edit.mIsComposition;
                return true;
            } else if (!this.mIsComposition || i != 2 || this.mStart > edit.mStart || getNewTextEnd() < edit.getOldTextEnd()) {
                return false;
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.mNewText.substring(0, edit.mStart - this.mStart));
                stringBuilder.append(edit.mNewText);
                stringBuilder.append(this.mNewText.substring(edit.getOldTextEnd() - this.mStart, this.mNewText.length()));
                this.mNewText = stringBuilder.toString();
                this.mNewCursorPos = edit.mNewCursorPos;
                this.mIsComposition = edit.mIsComposition;
                return true;
            }
        }

        private boolean mergeDeleteWith(EditOperation edit) {
            if (edit.mType != 1 || this.mStart != edit.getOldTextEnd()) {
                return false;
            }
            this.mStart = edit.mStart;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(edit.mOldText);
            stringBuilder.append(this.mOldText);
            this.mOldText = stringBuilder.toString();
            this.mNewCursorPos = edit.mNewCursorPos;
            this.mIsComposition = edit.mIsComposition;
            return true;
        }

        private boolean mergeReplaceWith(EditOperation edit) {
            StringBuilder stringBuilder;
            if (edit.mType == 0 && getNewTextEnd() == edit.mStart) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.mNewText);
                stringBuilder.append(edit.mNewText);
                this.mNewText = stringBuilder.toString();
                this.mNewCursorPos = edit.mNewCursorPos;
                return true;
            } else if (!this.mIsComposition) {
                return false;
            } else {
                if (edit.mType == 1 && this.mStart <= edit.mStart && getNewTextEnd() >= edit.getOldTextEnd()) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(this.mNewText.substring(0, edit.mStart - this.mStart));
                    stringBuilder.append(this.mNewText.substring(edit.getOldTextEnd() - this.mStart, this.mNewText.length()));
                    this.mNewText = stringBuilder.toString();
                    if (this.mNewText.isEmpty()) {
                        this.mType = 1;
                    }
                    this.mNewCursorPos = edit.mNewCursorPos;
                    this.mIsComposition = edit.mIsComposition;
                    return true;
                } else if (edit.mType != 2 || this.mStart != edit.mStart || !TextUtils.equals(this.mNewText, edit.mOldText)) {
                    return false;
                } else {
                    this.mNewText = edit.mNewText;
                    this.mNewCursorPos = edit.mNewCursorPos;
                    this.mIsComposition = edit.mIsComposition;
                    return true;
                }
            }
        }

        public void forceMergeWith(EditOperation edit) {
            if (!mergeWith(edit)) {
                Editable editable = (Editable) ((Editor) getOwnerData()).mTextView.getText();
                Editable originalText = new SpannableStringBuilder(editable.toString());
                modifyText(originalText, this.mStart, getNewTextEnd(), this.mOldText, this.mStart, this.mOldCursorPos);
                Editable finalText = new SpannableStringBuilder(editable.toString());
                modifyText(finalText, edit.mStart, edit.getOldTextEnd(), edit.mNewText, edit.mStart, edit.mNewCursorPos);
                this.mType = 2;
                this.mNewText = finalText.toString();
                this.mOldText = originalText.toString();
                this.mStart = 0;
                this.mNewCursorPos = edit.mNewCursorPos;
                this.mIsComposition = edit.mIsComposition;
            }
        }

        private static void modifyText(Editable text, int deleteFrom, int deleteTo, CharSequence newText, int newTextInsertAt, int newCursorPos) {
            if (Editor.isValidRange(text, deleteFrom, deleteTo) && newTextInsertAt <= text.length() - (deleteTo - deleteFrom)) {
                if (deleteFrom != deleteTo) {
                    text.delete(deleteFrom, deleteTo);
                }
                if (newText.length() != 0) {
                    text.insert(newTextInsertAt, newText);
                }
            }
            if (newCursorPos >= 0 && newCursorPos <= text.length()) {
                Selection.setSelection(text, newCursorPos);
            }
        }

        private String getTypeString() {
            int i = this.mType;
            if (i == 0) {
                return "insert";
            }
            if (i == 1) {
                return "delete";
            }
            if (i != 2) {
                return "";
            }
            return "replace";
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[mType=");
            stringBuilder.append(getTypeString());
            stringBuilder.append(", mOldText=");
            stringBuilder.append(this.mOldText);
            stringBuilder.append(", mNewText=");
            stringBuilder.append(this.mNewText);
            stringBuilder.append(", mStart=");
            stringBuilder.append(this.mStart);
            stringBuilder.append(", mOldCursorPos=");
            stringBuilder.append(this.mOldCursorPos);
            stringBuilder.append(", mNewCursorPos=");
            stringBuilder.append(this.mNewCursorPos);
            stringBuilder.append(", mFrozen=");
            stringBuilder.append(this.mFrozen);
            stringBuilder.append(", mIsComposition=");
            stringBuilder.append(this.mIsComposition);
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    private class EmailAddPopupWindow extends ActionPinnedPopupWindow implements OnClickListener {
        private final int POPUP_TEXT_LAYOUT = R.layout.text_edit_action_popup_text;
        private EmailInfo mEmail;
        private TextView mEmailTextView;
        private boolean mPosChanged;
        private int mTextActionPadding;

        public EmailAddPopupWindow(HandleView handleView) {
            int i;
            super(handleView);
            Resources resources = Editor.this.mTextView.getResources();
            if (Editor.isBigFontMode()) {
                i = R.dimen.text_action_popup_padding_small;
            } else {
                i = R.dimen.text_action_popup_padding;
            }
            this.mTextActionPadding = resources.getDimensionPixelSize(i);
        }

        public void setEmail(EmailInfo email) {
            EmailInfo emailInfo = this.mEmail;
            boolean z = (emailInfo != null && emailInfo.start == email.start && this.mEmail.email.length() == email.email.length() && this.mEmail.cursorPos == email.cursorPos) ? false : true;
            this.mPosChanged = z;
            this.mEmail = email;
        }

        /* Access modifiers changed, original: protected */
        public void createPopupWindow() {
            Editor editor = Editor.this;
            this.mPopupWindow = new AnimatePopupWindow(editor, editor.mTextView.getContext(), null);
            this.mPopupWindow.setClippingEnabled(false);
        }

        /* Access modifiers changed, original: protected */
        public void initContentView() {
            this.mMainPanel = new TouchPanelLayout(Editor.this.mTextView.getContext());
            this.mMainPanel.setOrientation(0);
            this.mMainPanel.setBackgroundResource(R.drawable.text_select_bg);
            this.mMainPanel.setId(R.id.text_action_background_view);
            this.mContentView = new FrameLayout(Editor.this.mTextView.getContext());
            this.mContentView.addView(this.mMainPanel, new LayoutParams(-1, -1));
            this.mEmailTextView = newTextView();
            this.mEmailTextView.setText((int) R.string.frequent_email);
            this.mMainPanel.addView(this.mEmailTextView);
        }

        /* Access modifiers changed, original: protected */
        public void createAnimations() {
            this.mAnimationFadeIn = new AnimatorSet();
            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(this.mContentView, View.ALPHA, 0.0f, 1.0f);
            ObjectAnimator scaleAnimatorX = ObjectAnimator.ofFloat(this.mContentView, View.SCALE_X, 0.6f, 1.0f);
            ObjectAnimator scaleAnimatorY = ObjectAnimator.ofFloat(this.mContentView, View.SCALE_Y, 0.6f, 1.0f);
            this.mAnimationFadeIn.setInterpolator(new CubicEaseOutInterpolator());
            this.mAnimationFadeIn.setDuration(150);
            this.mAnimationFadeIn.playTogether(alphaAnimator, scaleAnimatorX, scaleAnimatorY);
            this.mAnimationFadeOut = new AnimatorSet();
            alphaAnimator = ObjectAnimator.ofFloat(this.mContentView, View.ALPHA, 1.0f, 0.0f);
            scaleAnimatorX = ObjectAnimator.ofFloat(this.mContentView, View.SCALE_X, 1.0f, 0.6f);
            scaleAnimatorY = ObjectAnimator.ofFloat(this.mContentView, View.SCALE_Y, 1.0f, 0.6f);
            this.mAnimationFadeOut.setInterpolator(new CubicEaseOutInterpolator());
            this.mAnimationFadeOut.setDuration(100);
            this.mAnimationFadeOut.playTogether(alphaAnimator, scaleAnimatorX, scaleAnimatorY);
            this.mAnimationFadeOutListener = new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    ((AnimatePopupWindow) EmailAddPopupWindow.this.mPopupWindow).dismiss(false);
                }
            };
        }

        private TextView newTextView() {
            TextView textView = (TextView) ((LayoutInflater) Editor.this.mTextView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate((int) R.layout.text_edit_action_popup_text, null);
            textView.setLayoutParams(new LinearLayout.LayoutParams(-2, -2, 1.0f));
            textView.setSingleLine();
            textView.setGravity(17);
            textView.setOnClickListener(this);
            return textView;
        }

        public void show() {
            if (!WindowLayoutParamsUtil.isInSystemWindow(Editor.this.mTextView)) {
                boolean canShow = (Utils.isAddPhraseActivity(Editor.this.mTextView.getContext()) || Editor.this.isPasswordInputType() || Editor.this.mTextView.isInExtractedMode()) ? false : true;
                if (canShow && this.mPosChanged && this.mEmail != null) {
                    this.mEmailTextView.getBackground().setLevel(3);
                    TextView textView = this.mEmailTextView;
                    textView.setPadding(this.mTextActionPadding, textView.getPaddingTop(), this.mTextActionPadding, this.mEmailTextView.getPaddingBottom());
                    super.show();
                }
            }
        }

        public void onClick(View view) {
            if (this.mEmail != null) {
                Utils.startAddPhraseActivity(Editor.this.mTextView.getContext(), this.mEmail.email);
                hide();
            }
        }

        public void fadeIn(int x, int y) {
            this.mContentView.setPivotX((float) (this.mContentView.getMeasuredWidth() / 2));
            this.mContentView.setPivotY(this.mAboveHandle ? (float) this.mContentView.getMeasuredHeight() : 0.0f);
            this.mAnimationFadeIn.start();
        }

        /* Access modifiers changed, original: protected */
        public void updatePosition(int parentPositionX, int parentPositionY) {
            AnimatePopupWindow popupWindow = this.mPopupWindow;
            if (popupWindow.isDismissing()) {
                popupWindow.dismiss(false);
            }
            super.updatePosition(parentPositionX, parentPositionY);
        }

        public void dismiss() {
            ((AnimatePopupWindow) this.mPopupWindow).dismiss(true);
            setZeroTouchableSurface();
            this.mEmail = null;
        }

        /* Access modifiers changed, original: protected */
        public int getSelectionStart() {
            if (this.mEmail == null) {
                return 0;
            }
            Layout layout = Editor.this.mTextView.getLayout();
            if (layout != null) {
                int cursorLine = layout.getLineForOffset(this.mEmail.cursorPos);
                if (layout.getLineForOffset(this.mEmail.start) < cursorLine) {
                    return layout.getLineStart(cursorLine);
                }
            }
            return this.mEmail.start;
        }

        /* Access modifiers changed, original: protected */
        public int getSelectionEnd() {
            if (this.mEmail == null) {
                return 0;
            }
            Layout layout = Editor.this.mTextView.getLayout();
            if (layout != null) {
                int cursorLine = layout.getLineForOffset(this.mEmail.cursorPos);
                if (layout.getLineForOffset(this.mEmail.start + this.mEmail.email.length()) > cursorLine) {
                    return layout.getLineEnd(cursorLine) - 1;
                }
            }
            return this.mEmail.start + this.mEmail.email.length();
        }
    }

    private static class ErrorPopup extends PopupWindow {
        private boolean mAbove = false;
        private int mPopupInlineErrorAboveBackgroundId = 0;
        private int mPopupInlineErrorBackgroundId = 0;
        private final TextView mView;

        ErrorPopup(TextView v, int width, int height) {
            super((View) v, width, height);
            this.mView = v;
            this.mPopupInlineErrorBackgroundId = getResourceId(this.mPopupInlineErrorBackgroundId, 297);
            this.mView.setBackgroundResource(this.mPopupInlineErrorBackgroundId);
        }

        /* Access modifiers changed, original: 0000 */
        public void fixDirection(boolean above) {
            int i;
            this.mAbove = above;
            if (above) {
                this.mPopupInlineErrorAboveBackgroundId = getResourceId(this.mPopupInlineErrorAboveBackgroundId, 296);
            } else {
                this.mPopupInlineErrorBackgroundId = getResourceId(this.mPopupInlineErrorBackgroundId, 297);
            }
            TextView textView = this.mView;
            if (above) {
                i = this.mPopupInlineErrorAboveBackgroundId;
            } else {
                i = this.mPopupInlineErrorBackgroundId;
            }
            textView.setBackgroundResource(i);
        }

        private int getResourceId(int currentId, int index) {
            if (currentId != 0) {
                return currentId;
            }
            TypedArray styledAttributes = this.mView.getContext().obtainStyledAttributes(android.R.styleable.Theme);
            currentId = styledAttributes.getResourceId(index, 0);
            styledAttributes.recycle();
            return currentId;
        }

        public void update(int x, int y, int w, int h, boolean force) {
            super.update(x, y, w, h, force);
            boolean above = isAboveAnchor();
            if (above != this.mAbove) {
                fixDirection(above);
            }
        }
    }

    public @interface HandleType {
    }

    @VisibleForTesting
    public abstract class HandleView extends View implements TextViewPositionListener {
        private static final int HISTORY_SIZE = 5;
        private static final int TOUCH_UP_FILTER_DELAY_AFTER = 150;
        private static final int TOUCH_UP_FILTER_DELAY_BEFORE = 350;
        private Runnable mActionPopupShower;
        protected ActionPopupWindow mActionPopupWindow;
        protected AnimatorSet mAnimationFadeIn;
        protected AnimatorSet mAnimationFadeOut;
        protected AnimatorListenerAdapter mAnimationFadeOutListener;
        protected final PopupWindow mContainer;
        private float mCurrentDragInitialTouchRawX;
        protected Drawable mDrawable;
        protected Drawable mDrawableLtr;
        protected Drawable mDrawableRtl;
        private int mHandleExtension;
        protected int mHorizontalGravity;
        protected float mHorizontalScale;
        protected int mHotspotX;
        private float mIdealVerticalOffset;
        private boolean mIsDragging;
        private int mLastParentX;
        private int mLastParentY;
        private int mLastWindowY;
        private int mMinSize;
        private int mNumberPreviousOffsets;
        private boolean mPositionHasChanged;
        private int mPositionX;
        private int mPositionY;
        protected int mPrevLine;
        protected int mPreviousLineTouched;
        protected int mPreviousOffset;
        private int mPreviousOffsetIndex;
        private final int[] mPreviousOffsets;
        private final long[] mPreviousOffsetsTimes;
        private float mTextViewScaleX;
        private float mTextViewScaleY;
        private float mTouchOffsetY;
        private float mTouchToWindowOffsetX;
        private float mTouchToWindowOffsetY;

        public abstract int getCurrentCursorOffset();

        public abstract int getHorizontalGravity(boolean z);

        public abstract int getHotspotX(Drawable drawable, boolean z);

        public abstract int getMagnifierHandleTrigger();

        public abstract void updatePosition(float f, float f2);

        public abstract void updateSelection(int i);

        /* synthetic */ HandleView(Editor x0, Drawable x1, Drawable x2, AnonymousClass1 x3) {
            this(x1, x2);
        }

        private HandleView(Drawable drawableLtr, Drawable drawableRtl) {
            super(Editor.this.mTextView.getContext());
            this.mHorizontalScale = 1.0f;
            this.mPreviousOffset = -1;
            this.mPositionHasChanged = true;
            this.mPrevLine = -1;
            this.mPreviousLineTouched = -1;
            this.mCurrentDragInitialTouchRawX = -1.0f;
            this.mPreviousOffsetsTimes = new long[5];
            this.mPreviousOffsets = new int[5];
            this.mPreviousOffsetIndex = 0;
            this.mNumberPreviousOffsets = 0;
            this.mContainer = new AnimatePopupWindow(Editor.this, Editor.this.mTextView.getContext(), null);
            this.mContainer.setSplitTouchEnabled(true);
            this.mContainer.setClippingEnabled(false);
            this.mContainer.setWindowLayoutType(1002);
            this.mContainer.setContentView(this);
            this.mContainer.setOnDismissListener(new OnDismissListener(Editor.this) {
                public void onDismiss() {
                    HandleView.this.onDetached();
                }
            });
            setDrawables(drawableLtr, drawableRtl);
            this.mMinSize = Editor.this.mTextView.getContext().getResources().getDimensionPixelSize(R.dimen.text_handle_min_size);
            updateDrawable(false);
        }

        public float getIdealVerticalOffset() {
            return this.mIdealVerticalOffset;
        }

        /* Access modifiers changed, original: 0000 */
        public void setDrawables(Drawable drawableLtr, Drawable drawableRtl) {
            this.mDrawableLtr = drawableLtr;
            this.mDrawableRtl = drawableRtl;
            updateDrawable(true);
        }

        /* Access modifiers changed, original: protected */
        public void updateDrawable(boolean updateDrawableWhenDragging) {
            if (updateDrawableWhenDragging) {
                Layout layout = Editor.this.mTextView.getLayout();
                if (layout != null) {
                    int offset = getCurrentCursorOffset();
                    boolean isRtlCharAtOffset = Editor.this.mTextView.getLayout().isRtlCharAt(offset);
                    Drawable oldDrawable = this.mDrawable;
                    this.mDrawable = isRtlCharAtOffset ? this.mDrawableRtl : this.mDrawableLtr;
                    this.mHotspotX = getHotspotX(this.mDrawable, isRtlCharAtOffset);
                    this.mHorizontalGravity = getHorizontalGravity(isRtlCharAtOffset);
                    if (oldDrawable != this.mDrawable && isShowing()) {
                        this.mPositionX = ((getCursorHorizontalPosition(layout, offset) - this.mHotspotX) - getHorizontalOffset()) + getCursorOffset();
                        this.mPositionX += Editor.this.mTextView.viewportToContentHorizontalOffset();
                        this.mPositionHasChanged = true;
                        updatePosition(this.mLastParentX, this.mLastParentY, false, false);
                        postInvalidate();
                    }
                }
            }
        }

        private void startTouchUpFilter(int offset) {
            this.mNumberPreviousOffsets = 0;
            addPositionToTouchUpFilter(offset);
        }

        private void addPositionToTouchUpFilter(int offset) {
            this.mPreviousOffsetIndex = (this.mPreviousOffsetIndex + 1) % 5;
            int[] iArr = this.mPreviousOffsets;
            int i = this.mPreviousOffsetIndex;
            iArr[i] = offset;
            this.mPreviousOffsetsTimes[i] = SystemClock.uptimeMillis();
            this.mNumberPreviousOffsets++;
        }

        private void filterOnTouchUp() {
            long now = SystemClock.uptimeMillis();
            int i = 0;
            int index = this.mPreviousOffsetIndex;
            int iMax = Math.min(this.mNumberPreviousOffsets, 5);
            while (i < iMax && now - this.mPreviousOffsetsTimes[index] < 150) {
                i++;
                index = ((this.mPreviousOffsetIndex - i) + 5) % 5;
            }
            if (i > 0 && i < iMax && now - this.mPreviousOffsetsTimes[index] > 350) {
                positionAtCursorOffset(this.mPreviousOffsets[index], false);
            }
        }

        public boolean offsetHasBeenChanged() {
            return this.mNumberPreviousOffsets > 1;
        }

        /* Access modifiers changed, original: protected */
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int line;
            Layout layout = Editor.this.mTextView.getLayout();
            int lineHeight = 0;
            if (layout != null) {
                line = layout.getLineForOffset(getCurrentCursorOffset());
                lineHeight = layout.getLineBottom(line) - layout.getLineTop(line);
            }
            this.mHandleExtension = getHandleExtension(lineHeight);
            line = getPreferredHeight();
            setMeasuredDimension(getPreferredWidth(), this.mHandleExtension + line);
            int i = this.mHandleExtension;
            this.mTouchOffsetY = ((float) i) * 0.5f;
            this.mIdealVerticalOffset = (((float) line) * 0.5f) + ((float) i);
        }

        /* Access modifiers changed, original: 0000 */
        public int getPreferredWidth() {
            return Math.max(this.mDrawable.getIntrinsicWidth(), this.mMinSize);
        }

        /* Access modifiers changed, original: 0000 */
        public int getPreferredHeight() {
            return Math.max(this.mDrawable.getIntrinsicHeight(), this.mMinSize);
        }

        public void show() {
            AnimatePopupWindow container = this.mContainer;
            if (container.isDismissing()) {
                container.dismiss(false);
            }
            if (!isShowing()) {
                Editor.this.getPositionListener().addSubscriber(this, true);
                this.mPreviousOffset = -1;
                this.mHorizontalScale = Editor.getDescendantViewScale(Editor.this.mTextView);
                positionAtCursorOffset(getCurrentCursorOffset(), false);
            }
        }

        /* Access modifiers changed, original: protected */
        public void dismiss() {
            this.mIsDragging = false;
            ((AnimatePopupWindow) this.mContainer).dismiss();
            onDetached();
        }

        public void hide() {
            dismiss();
            Editor.this.getPositionListener().removeSubscriber(this);
        }

        /* Access modifiers changed, original: protected */
        public ActionPopupWindow getActionPopupWindow() {
            if (this.mActionPopupWindow == null) {
                this.mActionPopupWindow = new ActionPopupWindow(this);
            }
            return this.mActionPopupWindow;
        }

        /* Access modifiers changed, original: 0000 */
        public void showActionPopupWindow(int delay) {
            this.mActionPopupWindow = getActionPopupWindow();
            if (this.mActionPopupShower == null) {
                this.mActionPopupShower = new Runnable() {
                    public void run() {
                        HandleView.this.mActionPopupWindow.show();
                    }
                };
            }
            Editor.this.mTextView.removeCallbacks(this.mActionPopupShower);
            Editor.this.mTextView.postDelayed(this.mActionPopupShower, (long) delay);
        }

        /* Access modifiers changed, original: protected */
        public void hideActionPopupWindow() {
            if (this.mActionPopupShower != null) {
                Editor.this.mTextView.removeCallbacks(this.mActionPopupShower);
            }
            ActionPopupWindow actionPopupWindow = this.mActionPopupWindow;
            if (actionPopupWindow != null) {
                actionPopupWindow.hide();
            }
        }

        public boolean isPopshowing() {
            ActionPopupWindow actionPopupWindow = this.mActionPopupWindow;
            return actionPopupWindow != null && actionPopupWindow.isShowing();
        }

        public boolean isShowing() {
            return this.mContainer.isShowing();
        }

        private boolean isVisible() {
            if (this.mIsDragging) {
                return true;
            }
            if (Editor.this.mTextView.isInBatchEditMode()) {
                return false;
            }
            return Editor.this.isPositionVisible((this.mPositionX + this.mHotspotX) + getHorizontalOffset(), this.mPositionY);
        }

        @VisibleForTesting
        public float getHorizontal(Layout layout, int offset) {
            return layout.getPrimaryHorizontal(offset);
        }

        private void setVisible(boolean visible) {
            this.mContainer.getContentView().setVisibility(visible ? 0 : 4);
        }

        /* Access modifiers changed, original: protected */
        public void positionAtCursorOffset(int offset, boolean parentScrolled) {
            Layout layout = Editor.this.mTextView.getLayout();
            if (layout == null) {
                Editor.this.prepareCursorControllers();
                return;
            }
            boolean offsetChanged = offset != this.mPreviousOffset;
            if (offsetChanged || parentScrolled) {
                if (offsetChanged) {
                    updateSelection(offset);
                    addPositionToTouchUpFilter(offset);
                }
                int line = layout.getLineForOffset(offset);
                this.mPositionX = (int) ((((layout.getPrimaryHorizontal(offset) * this.mHorizontalScale) - ((float) this.mHotspotX)) - ((float) getHorizontalOffset())) + ((float) getCursorOffset()));
                int handleExtension = getHandleExtension(layout.getLineBottom(line) - layout.getLineTop(line));
                this.mPositionY = layout.getLineBottom(line) - handleExtension;
                if (handleExtension != this.mHandleExtension) {
                    onMeasure(0, 0);
                }
                this.mPositionX = (int) (((float) this.mPositionX) + (((float) Editor.this.mTextView.viewportToContentHorizontalOffset()) * this.mHorizontalScale));
                this.mPositionY += Editor.this.mTextView.viewportToContentVerticalOffset();
                this.mPreviousOffset = offset;
                this.mPositionHasChanged = true;
            }
        }

        /* Access modifiers changed, original: 0000 */
        public int getCursorHorizontalPosition(Layout layout, int offset) {
            return (int) (getHorizontal(layout, offset) - 0.5f);
        }

        public void updatePosition(int parentPositionX, int parentPositionY, boolean parentPositionChanged, boolean parentScrolled) {
            positionAtCursorOffset(getCurrentCursorOffset(), parentScrolled);
            if (parentPositionChanged || this.mPositionHasChanged) {
                if (this.mIsDragging) {
                    if (!(parentPositionX == this.mLastParentX && parentPositionY == this.mLastParentY)) {
                        this.mTouchToWindowOffsetX += (float) (parentPositionX - this.mLastParentX);
                        this.mTouchToWindowOffsetY += (float) (parentPositionY - this.mLastParentY);
                        this.mLastParentX = parentPositionX;
                        this.mLastParentY = parentPositionY;
                    }
                    onHandleMoved();
                }
                if (isVisible()) {
                    int positionX = this.mPositionX + parentPositionX;
                    int positionY = clipVertically(this.mPositionY + parentPositionY);
                    if (isShowing()) {
                        this.mContainer.update(positionX, positionY, -1, -1);
                    } else {
                        onAttached();
                        this.mContainer.showAtLocation(Editor.this.mTextView, 0, positionX, positionY);
                    }
                } else if (isShowing()) {
                    dismiss();
                }
                this.mPositionHasChanged = false;
            }
        }

        private int clipVertically(int positionY) {
            if (positionY < Editor.this.getDisplayHeightPixels()) {
                return positionY;
            }
            int offset = getCurrentCursorOffset();
            Layout layout = Editor.this.mTextView.getLayout();
            int line = layout.getLineForOffset(offset);
            return (positionY - (layout.getLineBottom(line) - layout.getLineTop(line))) - getMeasuredHeight();
        }

        /* Access modifiers changed, original: protected */
        public void onDraw(Canvas c) {
            int drawWidth = this.mDrawable.getIntrinsicWidth();
            int drawHeight = (this.mDrawable.getIntrinsicHeight() + this.mHandleExtension) - 1;
            int left = getHorizontalOffset();
            this.mDrawable.setBounds(left, 0, left + drawWidth, drawHeight);
            this.mDrawable.draw(c);
        }

        /* Access modifiers changed, original: protected */
        public int getHorizontalOffset() {
            int width = getPreferredWidth();
            int drawWidth = this.mDrawable.getIntrinsicWidth();
            int i = this.mHorizontalGravity;
            if (i == 3) {
                return 0;
            }
            if (i != 5) {
                return (width - drawWidth) / 2;
            }
            return width - drawWidth;
        }

        /* Access modifiers changed, original: protected */
        public int getCursorOffset() {
            return 0;
        }

        private boolean tooLargeTextForMagnifier() {
            float magnifierContentHeight = (float) Math.round(((float) Editor.this.mMagnifierAnimator.mMagnifier.getHeight()) / Editor.this.mMagnifierAnimator.mMagnifier.getZoom());
            FontMetrics fontMetrics = Editor.this.mTextView.getPaint().getFontMetrics();
            return this.mTextViewScaleY * (fontMetrics.descent - fontMetrics.ascent) > magnifierContentHeight;
        }

        private boolean checkForTransforms() {
            if (Editor.this.mMagnifierAnimator.mMagnifierIsShowing) {
                return true;
            }
            if (Editor.this.mTextView.getRotation() != 0.0f || Editor.this.mTextView.getRotationX() != 0.0f || Editor.this.mTextView.getRotationY() != 0.0f) {
                return false;
            }
            this.mTextViewScaleX = Editor.this.mTextView.getScaleX();
            this.mTextViewScaleY = Editor.this.mTextView.getScaleY();
            for (ViewParent viewParent = Editor.this.mTextView.getParent(); viewParent != null; viewParent = viewParent.getParent()) {
                if (viewParent instanceof View) {
                    View view = (View) viewParent;
                    if (view.getRotation() != 0.0f || view.getRotationX() != 0.0f || view.getRotationY() != 0.0f) {
                        return false;
                    }
                    this.mTextViewScaleX *= view.getScaleX();
                    this.mTextViewScaleY *= view.getScaleY();
                }
            }
            return true;
        }

        /* JADX WARNING: Missing block: B:6:0x000d, code skipped:
            if (r0 != 3) goto L_0x00b8;
     */
        public boolean onTouchEvent(android.view.MotionEvent r10) {
            /*
            r9 = this;
            r0 = r10.getActionMasked();
            r1 = 1;
            if (r0 == 0) goto L_0x006d;
        L_0x0007:
            if (r0 == r1) goto L_0x0066;
        L_0x0009:
            r2 = 2;
            if (r0 == r2) goto L_0x0011;
        L_0x000c:
            r2 = 3;
            if (r0 == r2) goto L_0x0069;
        L_0x000f:
            goto L_0x00b8;
        L_0x0011:
            r0 = r10.getRawX();
            r2 = r10.getRawY();
            r3 = r9.mTouchToWindowOffsetY;
            r4 = r9.mLastParentY;
            r5 = (float) r4;
            r3 = r3 - r5;
            r5 = r9.mLastWindowY;
            r6 = (float) r5;
            r3 = r3 - r6;
            r6 = r9.mPositionY;
            r6 = (float) r6;
            r6 = r2 - r6;
            r4 = (float) r4;
            r6 = r6 - r4;
            r4 = (float) r5;
            r6 = r6 - r4;
            r4 = r9.mIdealVerticalOffset;
            r5 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1));
            if (r5 >= 0) goto L_0x003b;
        L_0x0032:
            r4 = java.lang.Math.min(r6, r4);
            r4 = java.lang.Math.max(r4, r3);
            goto L_0x0043;
        L_0x003b:
            r4 = java.lang.Math.max(r6, r4);
            r4 = java.lang.Math.min(r4, r3);
        L_0x0043:
            r5 = r9.mLastParentY;
            r5 = (float) r5;
            r5 = r5 + r4;
            r7 = r9.mLastWindowY;
            r7 = (float) r7;
            r5 = r5 + r7;
            r9.mTouchToWindowOffsetY = r5;
            r5 = r9.mTouchToWindowOffsetX;
            r5 = r0 - r5;
            r7 = r9.mHotspotX;
            r7 = (float) r7;
            r5 = r5 + r7;
            r7 = r9.getHorizontalOffset();
            r7 = (float) r7;
            r5 = r5 + r7;
            r7 = r9.mTouchToWindowOffsetY;
            r7 = r2 - r7;
            r8 = r9.mTouchOffsetY;
            r7 = r7 + r8;
            r9.updatePosition(r5, r7);
            goto L_0x00b8;
        L_0x0066:
            r9.filterOnTouchUp();
        L_0x0069:
            r0 = 0;
            r9.mIsDragging = r0;
            goto L_0x00b8;
        L_0x006d:
            r0 = r9.getCurrentCursorOffset();
            r9.startTouchUpFilter(r0);
            r0 = r10.getRawX();
            r2 = r9.mPositionX;
            r2 = (float) r2;
            r0 = r0 - r2;
            r9.mTouchToWindowOffsetX = r0;
            r0 = r10.getRawY();
            r2 = r9.mPositionY;
            r2 = (float) r2;
            r0 = r0 - r2;
            r9.mTouchToWindowOffsetY = r0;
            r0 = android.widget.Editor.this;
            r0 = r0.getPositionListener();
            r0 = r0.getPositionX();
            r9.mLastParentX = r0;
            r0 = android.widget.Editor.this;
            r0 = r0.getPositionListener();
            r0 = r0.getPositionY();
            r9.mLastParentY = r0;
            r0 = android.widget.Editor.this;
            r0 = r0.mTextView;
            r0 = r0.getRootView();
            r0 = r0.getLocationOnScreen();
            r0 = r0[r1];
            r9.mLastWindowY = r0;
            r9.mIsDragging = r1;
            r0 = -1;
            r9.mPreviousLineTouched = r0;
        L_0x00b8:
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.widget.Editor$HandleView.onTouchEvent(android.view.MotionEvent):boolean");
        }

        /* JADX WARNING: Removed duplicated region for block: B:62:0x01e2  */
        /* JADX WARNING: Removed duplicated region for block: B:53:0x016f  */
        /* JADX WARNING: Removed duplicated region for block: B:44:0x0117  */
        /* JADX WARNING: Removed duplicated region for block: B:53:0x016f  */
        /* JADX WARNING: Removed duplicated region for block: B:62:0x01e2  */
        /* JADX WARNING: Removed duplicated region for block: B:36:0x00ee  */
        /* JADX WARNING: Removed duplicated region for block: B:44:0x0117  */
        /* JADX WARNING: Removed duplicated region for block: B:62:0x01e2  */
        /* JADX WARNING: Removed duplicated region for block: B:53:0x016f  */
        private boolean obtainMagnifierShowCoordinates(android.view.MotionEvent r20, android.graphics.PointF r21) {
            /*
            r19 = this;
            r0 = r19;
            r1 = r21;
            r2 = r19.getMagnifierHandleTrigger();
            r3 = 2;
            r4 = 1;
            if (r2 == 0) goto L_0x003d;
        L_0x000c:
            if (r2 == r4) goto L_0x0028;
        L_0x000e:
            if (r2 == r3) goto L_0x0013;
        L_0x0010:
            r5 = -1;
            r6 = -1;
            goto L_0x0049;
        L_0x0013:
            r5 = android.widget.Editor.this;
            r5 = r5.mTextView;
            r5 = r5.getSelectionEnd();
            r6 = android.widget.Editor.this;
            r6 = r6.mTextView;
            r6 = r6.getSelectionStart();
            goto L_0x0049;
        L_0x0028:
            r5 = android.widget.Editor.this;
            r5 = r5.mTextView;
            r5 = r5.getSelectionStart();
            r6 = android.widget.Editor.this;
            r6 = r6.mTextView;
            r6 = r6.getSelectionEnd();
            goto L_0x0049;
        L_0x003d:
            r5 = android.widget.Editor.this;
            r5 = r5.mTextView;
            r5 = r5.getSelectionStart();
            r6 = -1;
        L_0x0049:
            r7 = -1;
            r8 = 0;
            if (r5 != r7) goto L_0x004e;
        L_0x004d:
            return r8;
        L_0x004e:
            r9 = r20.getActionMasked();
            if (r9 != 0) goto L_0x005b;
        L_0x0054:
            r9 = r20.getRawX();
            r0.mCurrentDragInitialTouchRawX = r9;
            goto L_0x0065;
        L_0x005b:
            r9 = r20.getActionMasked();
            if (r9 != r4) goto L_0x0065;
        L_0x0061:
            r9 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
            r0.mCurrentDragInitialTouchRawX = r9;
        L_0x0065:
            r9 = android.widget.Editor.this;
            r9 = r9.mTextView;
            r9 = r9.getLayout();
            r10 = r9.getLineForOffset(r5);
            if (r6 == r7) goto L_0x007d;
        L_0x0075:
            r7 = r9.getLineForOffset(r6);
            if (r10 != r7) goto L_0x007d;
        L_0x007b:
            r7 = r4;
            goto L_0x007e;
        L_0x007d:
            r7 = r8;
        L_0x007e:
            if (r7 == 0) goto L_0x00ac;
        L_0x0080:
            if (r5 >= r6) goto L_0x0084;
        L_0x0082:
            r11 = r4;
            goto L_0x0085;
        L_0x0084:
            r11 = r8;
        L_0x0085:
            r12 = android.widget.Editor.this;
            r12 = r12.mTextView;
            r12 = r12.getLayout();
            r12 = r0.getHorizontal(r12, r5);
            r13 = android.widget.Editor.this;
            r13 = r13.mTextView;
            r13 = r13.getLayout();
            r13 = r0.getHorizontal(r13, r6);
            r12 = (r12 > r13 ? 1 : (r12 == r13 ? 0 : -1));
            if (r12 >= 0) goto L_0x00a7;
        L_0x00a5:
            r12 = r4;
            goto L_0x00a8;
        L_0x00a7:
            r12 = r8;
        L_0x00a8:
            if (r11 == r12) goto L_0x00ac;
        L_0x00aa:
            r11 = r4;
            goto L_0x00ad;
        L_0x00ac:
            r11 = r8;
        L_0x00ad:
            r12 = new int[r3];
            r13 = android.widget.Editor.this;
            r13 = r13.mTextView;
            r13.getLocationOnScreen(r12);
            r13 = r20.getRawX();
            r14 = r12[r8];
            r14 = (float) r14;
            r13 = r13 - r14;
            r14 = android.widget.Editor.this;
            r14 = r14.mTextView;
            r14 = r14.getTotalPaddingLeft();
            r15 = android.widget.Editor.this;
            r15 = r15.mTextView;
            r15 = r15.getScrollX();
            r14 = r14 - r15;
            r14 = (float) r14;
            r15 = android.widget.Editor.this;
            r15 = r15.mTextView;
            r15 = r15.getTotalPaddingLeft();
            r8 = android.widget.Editor.this;
            r8 = r8.mTextView;
            r8 = r8.getScrollX();
            r15 = r15 - r8;
            r8 = (float) r15;
            if (r7 == 0) goto L_0x0106;
        L_0x00ee:
            if (r2 != r3) goto L_0x00f2;
        L_0x00f0:
            r3 = r4;
            goto L_0x00f3;
        L_0x00f2:
            r3 = 0;
        L_0x00f3:
            r3 = r3 ^ r11;
            if (r3 == 0) goto L_0x0106;
        L_0x00f6:
            r3 = android.widget.Editor.this;
            r3 = r3.mTextView;
            r3 = r3.getLayout();
            r3 = r0.getHorizontal(r3, r6);
            r14 = r14 + r3;
            goto L_0x0115;
        L_0x0106:
            r3 = android.widget.Editor.this;
            r3 = r3.mTextView;
            r3 = r3.getLayout();
            r3 = r3.getLineLeft(r10);
            r14 = r14 + r3;
        L_0x0115:
            if (r7 == 0) goto L_0x012f;
        L_0x0117:
            if (r2 != r4) goto L_0x011b;
        L_0x0119:
            r3 = r4;
            goto L_0x011c;
        L_0x011b:
            r3 = 0;
        L_0x011c:
            r3 = r3 ^ r11;
            if (r3 == 0) goto L_0x012f;
        L_0x011f:
            r3 = android.widget.Editor.this;
            r3 = r3.mTextView;
            r3 = r3.getLayout();
            r3 = r0.getHorizontal(r3, r6);
            r8 = r8 + r3;
            goto L_0x013e;
        L_0x012f:
            r3 = android.widget.Editor.this;
            r3 = r3.mTextView;
            r3 = r3.getLayout();
            r3 = r3.getLineRight(r10);
            r8 = r8 + r3;
        L_0x013e:
            r3 = r0.mTextViewScaleX;
            r14 = r14 * r3;
            r8 = r8 * r3;
            r3 = android.widget.Editor.this;
            r3 = r3.mMagnifierAnimator;
            r3 = r3.mMagnifier;
            r3 = r3.getWidth();
            r3 = (float) r3;
            r15 = android.widget.Editor.this;
            r15 = r15.mMagnifierAnimator;
            r15 = r15.mMagnifier;
            r15 = r15.getZoom();
            r3 = r3 / r15;
            r3 = java.lang.Math.round(r3);
            r3 = (float) r3;
            r15 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
            r16 = r3 / r15;
            r16 = r14 - r16;
            r16 = (r13 > r16 ? 1 : (r13 == r16 ? 0 : -1));
            if (r16 < 0) goto L_0x01e2;
        L_0x016f:
            r16 = r3 / r15;
            r16 = r8 + r16;
            r16 = (r13 > r16 ? 1 : (r13 == r16 ? 0 : -1));
            if (r16 <= 0) goto L_0x017a;
        L_0x0177:
            r18 = r2;
            goto L_0x01e4;
        L_0x017a:
            r4 = r0.mTextViewScaleX;
            r17 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r4 = (r4 > r17 ? 1 : (r4 == r17 ? 0 : -1));
            if (r4 != 0) goto L_0x0186;
        L_0x0182:
            r4 = r13;
            r18 = r2;
            goto L_0x0198;
        L_0x0186:
            r4 = r20.getRawX();
            r15 = r0.mCurrentDragInitialTouchRawX;
            r4 = r4 - r15;
            r18 = r2;
            r2 = r0.mTextViewScaleX;
            r4 = r4 * r2;
            r4 = r4 + r15;
            r2 = 0;
            r2 = r12[r2];
            r2 = (float) r2;
            r4 = r4 - r2;
        L_0x0198:
            r2 = java.lang.Math.min(r8, r4);
            r2 = java.lang.Math.max(r14, r2);
            r1.x = r2;
            r2 = android.widget.Editor.this;
            r2 = r2.mTextView;
            r2 = r2.getLayout();
            r2 = r2.getLineTop(r10);
            r15 = android.widget.Editor.this;
            r15 = r15.mTextView;
            r15 = r15.getLayout();
            r15 = r15.getLineBottom(r10);
            r2 = r2 + r15;
            r2 = (float) r2;
            r15 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
            r2 = r2 / r15;
            r15 = android.widget.Editor.this;
            r15 = r15.mTextView;
            r15 = r15.getTotalPaddingTop();
            r15 = (float) r15;
            r2 = r2 + r15;
            r15 = android.widget.Editor.this;
            r15 = r15.mTextView;
            r15 = r15.getScrollY();
            r15 = (float) r15;
            r2 = r2 - r15;
            r15 = r0.mTextViewScaleY;
            r2 = r2 * r15;
            r1.y = r2;
            r2 = 1;
            return r2;
        L_0x01e2:
            r18 = r2;
        L_0x01e4:
            r2 = 0;
            return r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.widget.Editor$HandleView.obtainMagnifierShowCoordinates(android.view.MotionEvent, android.graphics.PointF):boolean");
        }

        private boolean handleOverlapsMagnifier(HandleView handle, Rect magnifierRect) {
            PopupWindow window = handle.mContainer;
            if (window.hasDecorView()) {
                return Rect.intersects(new Rect(window.getDecorViewLayoutParams().x, window.getDecorViewLayoutParams().y, window.getDecorViewLayoutParams().x + window.getContentView().getWidth(), window.getDecorViewLayoutParams().y + window.getContentView().getHeight()), magnifierRect);
            }
            return false;
        }

        private HandleView getOtherSelectionHandle() {
            SelectionModifierCursorController controller = Editor.this.getSelectionController();
            if (controller == null || !controller.isActive()) {
                return null;
            }
            HandleView access$500;
            if (controller.mStartHandle != this) {
                access$500 = controller.mStartHandle;
            } else {
                access$500 = controller.mEndHandle;
            }
            return access$500;
        }

        private void updateHandlesVisibility() {
            Point magnifierTopLeft = Editor.this.mMagnifierAnimator.mMagnifier.getPosition();
            if (magnifierTopLeft != null) {
                Rect surfaceInsets = Editor.this.mTextView.getViewRootImpl().mWindowAttributes.surfaceInsets;
                magnifierTopLeft.offset(-surfaceInsets.left, -surfaceInsets.top);
                Rect magnifierRect = new Rect(magnifierTopLeft.x, magnifierTopLeft.y, magnifierTopLeft.x + Editor.this.mMagnifierAnimator.mMagnifier.getWidth(), magnifierTopLeft.y + Editor.this.mMagnifierAnimator.mMagnifier.getHeight());
                setVisible(handleOverlapsMagnifier(this, magnifierRect) ^ 1);
                HandleView otherHandle = getOtherSelectionHandle();
                if (otherHandle != null) {
                    otherHandle.setVisible(handleOverlapsMagnifier(otherHandle, magnifierRect) ^ 1);
                }
            }
        }

        /* Access modifiers changed, original: protected|final */
        public final void updateMagnifier(MotionEvent event) {
            if (Editor.this.mMagnifierAnimator != null) {
                PointF showPosInView = new PointF();
                boolean shouldShow = checkForTransforms() && !tooLargeTextForMagnifier() && obtainMagnifierShowCoordinates(event, showPosInView);
                if (shouldShow) {
                    Editor.this.mRenderCursorRegardlessTiming = true;
                    Editor.this.mTextView.invalidateCursorPath();
                    Editor.this.suspendBlink();
                    Editor.this.mMagnifierAnimator.show(showPosInView.x, showPosInView.y);
                    updateHandlesVisibility();
                } else {
                    dismissMagnifier();
                }
            }
        }

        /* Access modifiers changed, original: protected|final */
        public final void dismissMagnifier() {
            if (Editor.this.mMagnifierAnimator != null) {
                Editor.this.mMagnifierAnimator.dismiss();
                Editor.this.mRenderCursorRegardlessTiming = false;
                Editor.this.resumeBlink();
                setVisible(true);
                HandleView otherHandle = getOtherSelectionHandle();
                if (otherHandle != null) {
                    otherHandle.setVisible(true);
                }
            }
        }

        public boolean isDragging() {
            return this.mIsDragging;
        }

        /* Access modifiers changed, original: 0000 */
        public void onHandleMoved() {
            hideActionPopupWindow();
        }

        public void onDetached() {
            hideActionPopupWindow();
        }

        public void onAttached() {
        }

        private int getHandleExtension(int lineHeight) {
            return Math.min(lineHeight, Editor.this.mTextView.getLineHeight() * 2);
        }
    }

    static class InputContentType {
        boolean enterDown;
        Bundle extras;
        int imeActionId;
        CharSequence imeActionLabel;
        LocaleList imeHintLocales;
        int imeOptions = 0;
        OnEditorActionListener onEditorActionListener;
        @UnsupportedAppUsage
        String privateImeOptions;

        InputContentType() {
        }
    }

    static class InputMethodState {
        int mBatchEditNesting;
        int mChangedDelta;
        int mChangedEnd;
        int mChangedStart;
        boolean mContentChanged;
        boolean mCursorChanged;
        Rect mCursorRectInWindow = new Rect();
        final ExtractedText mExtractedText = new ExtractedText();
        ExtractedTextRequest mExtractedTextRequest;
        boolean mSelectionModeChanged;
        float[] mTmpOffset = new float[2];
        RectF mTmpRectF = new RectF();

        InputMethodState() {
        }
    }

    private class InsertionHandleView extends HandleView implements Fader {
        private static final int DELAY_BEFORE_HANDLE_FADES_OUT = 3000;
        private static final int RECENT_CUT_COPY_DURATION = 15000;
        private float mDownPositionX;
        private float mDownPositionY;
        private Runnable mHider;
        private boolean mReShowPopup;

        public InsertionHandleView(Drawable drawable) {
            super(Editor.this, drawable, drawable, null);
            createAnimations();
            ((AnimatePopupWindow) this.mContainer).setFader(this);
        }

        /* Access modifiers changed, original: protected */
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setPivotX((float) (getMeasuredWidth() / 2));
            setPivotY((float) (getMeasuredHeight() - getPreferredHeight()));
        }

        public void show() {
            super.show();
            long durationSinceCutOrCopy = SystemClock.uptimeMillis() - TextView.sLastCutCopyOrTextChangedTime;
            if (Editor.this.mInsertionActionModeRunnable != null && (Editor.this.mTapState == 2 || Editor.this.mTapState == 3 || Editor.this.isCursorInsideEasyCorrectionSpan())) {
                Editor.this.mTextView.removeCallbacks(Editor.this.mInsertionActionModeRunnable);
            }
            if (!(Editor.this.mTapState == 2 || Editor.this.mTapState == 3 || Editor.this.isCursorInsideEasyCorrectionSpan() || durationSinceCutOrCopy >= 15000 || Editor.this.mTextActionMode != null)) {
                if (Editor.this.mInsertionActionModeRunnable == null) {
                    Editor.this.mInsertionActionModeRunnable = new Runnable() {
                        public void run() {
                            Editor.this.startInsertionActionMode();
                        }
                    };
                }
                Editor.this.mTextView.postDelayed(Editor.this.mInsertionActionModeRunnable, (long) (ViewConfiguration.getDoubleTapTimeout() + 1));
            }
            hideAfterDelay();
        }

        public void hide() {
            this.mReShowPopup = false;
            super.hide();
        }

        public void showWithActionPopup() {
            show();
            showActionPopupWindow(0);
        }

        private void hideAfterDelay() {
            if (this.mHider == null) {
                this.mHider = new Runnable() {
                    public void run() {
                        InsertionHandleView.this.hide();
                    }
                };
            } else {
                removeHiderCallback();
            }
            Editor.this.mTextView.postDelayed(this.mHider, AnrMonitor.PERF_EVENT_LOGGING_TIMEOUT);
        }

        private void removeHiderCallback() {
            if (this.mHider != null) {
                Editor.this.mTextView.removeCallbacks(this.mHider);
            }
        }

        /* Access modifiers changed, original: protected */
        public int getHotspotX(Drawable drawable, boolean isRtlRun) {
            return drawable.getIntrinsicWidth() / 2;
        }

        /* Access modifiers changed, original: protected */
        public int getHorizontalGravity(boolean isRtlRun) {
            return 1;
        }

        /* Access modifiers changed, original: protected */
        public int getCursorOffset() {
            int offset = super.getCursorOffset();
            if (Editor.this.mDrawableForCursor == null) {
                return offset;
            }
            if (Editor.this.mTempRect == null) {
                Editor.this.mTempRect = new Rect();
            }
            Editor.this.mDrawableForCursor.getPadding(Editor.this.mTempRect);
            return offset + (((Editor.this.mDrawableForCursor.getIntrinsicWidth() - Editor.this.mTempRect.left) - Editor.this.mTempRect.right) / 2);
        }

        public boolean onTouchEvent(MotionEvent ev) {
            boolean result = super.onTouchEvent(ev);
            int actionMasked = ev.getActionMasked();
            if (actionMasked == 0) {
                this.mDownPositionX = ev.getRawX();
                this.mDownPositionY = ev.getRawY();
            } else if (actionMasked == 1) {
                if (offsetHasBeenChanged()) {
                    if (this.mReShowPopup) {
                        showActionPopupWindow(0);
                        this.mReShowPopup = false;
                    }
                    if (Editor.this.hasSelectionController()) {
                        Editor.this.getSelectionController().setMinTouchOffset(getCurrentCursorOffset());
                        Editor.this.getSelectionController().setMaxTouchOffset(getCurrentCursorOffset());
                    }
                } else {
                    float deltaX = this.mDownPositionX - ev.getRawX();
                    float deltaY = this.mDownPositionY - ev.getRawY();
                    float distanceSquared = (deltaX * deltaX) + (deltaY * deltaY);
                    int touchSlop = ViewConfiguration.get(Editor.this.mTextView.getContext()).getScaledTouchSlop();
                    if (distanceSquared < ((float) (touchSlop * touchSlop))) {
                        if (this.mActionPopupWindow == null || !this.mActionPopupWindow.isShowing()) {
                            showWithActionPopup();
                        } else {
                            this.mReShowPopup = false;
                            hideActionPopupWindow();
                        }
                    }
                }
                hideAfterDelay();
            } else if (actionMasked == 3) {
                hideAfterDelay();
            }
            return result;
        }

        private void setVisible(boolean visible) {
            this.mContainer.getContentView().setVisibility(visible ? 0 : 4);
        }

        public int getCurrentCursorOffset() {
            return Editor.this.mTextView.getSelectionStart();
        }

        public void updateSelection(int offset) {
            if (offset < 0) {
                offset = 0;
            }
            Selection.setSelection((Spannable) Editor.this.mTextView.getText(), offset);
        }

        public void updatePosition(float x, float y) {
            int offset;
            if (this.mHorizontalScale != 1.0f) {
                x /= this.mHorizontalScale;
            }
            Layout layout = Editor.this.mTextView.getLayout();
            if (layout != null) {
                if (this.mPreviousLineTouched == -1) {
                    this.mPreviousLineTouched = Editor.this.mTextView.getLineAtCoordinate(y);
                }
                int currLine = Editor.this.getCurrentLineAdjustedForSlop(layout, this.mPreviousLineTouched, y);
                offset = Editor.this.mTextView.getOffsetAtCoordinate(currLine, x);
                this.mPreviousLineTouched = currLine;
            } else {
                offset = Editor.this.mTextView.getOffsetForPosition(x, y);
            }
            positionAtCursorOffset(offset, false);
        }

        /* Access modifiers changed, original: 0000 */
        public void onHandleMoved() {
            if (isPopshowing()) {
                this.mReShowPopup = true;
            }
            super.onHandleMoved();
            removeHiderCallback();
        }

        public void onAttached() {
            if (this.mReShowPopup) {
                showActionPopupWindow(0);
                this.mReShowPopup = false;
            }
            super.onAttached();
            hideAfterDelay();
        }

        public void onDetached() {
            super.onDetached();
            removeHiderCallback();
        }

        /* Access modifiers changed, original: protected */
        public int getMagnifierHandleTrigger() {
            return 0;
        }

        public void fadeIn(int x, int y) {
            Layout layout = Editor.this.mTextView.getLayout();
            if (layout != null) {
                int line = layout.getLineForOffset(getCurrentCursorOffset());
                setPivotY((float) (layout.getLineBottom(line) - layout.getLineTop(line)));
            }
            this.mAnimationFadeIn.start();
        }

        public void fadeOut() {
            this.mAnimationFadeOut.removeAllListeners();
            this.mAnimationFadeOut.addListener(this.mAnimationFadeOutListener);
            this.mAnimationFadeOut.start();
        }

        public void cancelAnimations() {
            this.mAnimationFadeIn.cancel();
            this.mAnimationFadeOut.removeAllListeners();
            this.mAnimationFadeOut.cancel();
            setScaleX(1.0f);
            setScaleY(1.0f);
        }

        public void setY(int y) {
        }

        private void createAnimations() {
            this.mAnimationFadeIn = new AnimatorSet();
            ObjectAnimator scaleAnimatorX = ObjectAnimator.ofFloat((Object) this, View.SCALE_X, 0.0f, 1.0f);
            ObjectAnimator scaleAnimatorY = ObjectAnimator.ofFloat((Object) this, View.SCALE_Y, 0.0f, 1.0f);
            this.mAnimationFadeIn.setInterpolator(new BackEaseOutInterpolator());
            this.mAnimationFadeIn.setDuration(300);
            this.mAnimationFadeIn.playTogether(scaleAnimatorX, scaleAnimatorY);
            this.mAnimationFadeOut = new AnimatorSet();
            scaleAnimatorX = ObjectAnimator.ofFloat((Object) this, View.SCALE_X, 1.0f, 0.0f);
            scaleAnimatorY = ObjectAnimator.ofFloat((Object) this, View.SCALE_Y, 1.0f, 0.0f);
            this.mAnimationFadeOut.setInterpolator(new CubicEaseOutInterpolator());
            this.mAnimationFadeOut.setDuration(150);
            this.mAnimationFadeOut.playTogether(scaleAnimatorX, scaleAnimatorY);
            this.mAnimationFadeOutListener = new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    ((AnimatePopupWindow) InsertionHandleView.this.mContainer).dismiss(false);
                }
            };
        }
    }

    private class InsertionPointCursorController implements CursorController {
        private InsertionHandleView mHandle;

        private InsertionPointCursorController() {
        }

        /* synthetic */ InsertionPointCursorController(Editor x0, AnonymousClass1 x1) {
            this();
        }

        public void show() {
            getHandle().show();
            getHandle().setVisibility(0);
        }

        public void showWithActionPopup() {
            getHandle().showWithActionPopup();
            if (Editor.this.mTextView.getText().length() == 0) {
                getHandle().setVisibility(8);
            } else {
                getHandle().setVisibility(0);
            }
        }

        public void hide() {
            InsertionHandleView insertionHandleView = this.mHandle;
            if (insertionHandleView != null) {
                insertionHandleView.hide();
            }
        }

        public void onTouchModeChanged(boolean isInTouchMode) {
            if (!isInTouchMode) {
                hide();
            }
        }

        private InsertionHandleView getHandle() {
            if (Editor.this.mSelectHandleCenter == null) {
                Editor editor = Editor.this;
                editor.mSelectHandleCenter = editor.mTextView.getResources().getDrawable(R.drawable.text_select_handle_middle);
            }
            if (this.mHandle == null) {
                Editor editor2 = Editor.this;
                this.mHandle = new InsertionHandleView(editor2.mSelectHandleCenter);
            }
            return this.mHandle;
        }

        private void reloadHandleDrawable() {
            InsertionHandleView insertionHandleView = this.mHandle;
            if (insertionHandleView != null) {
                insertionHandleView.setDrawables(Editor.this.mSelectHandleCenter, Editor.this.mSelectHandleCenter);
            }
        }

        public void onDetached() {
            Editor.this.mTextView.getViewTreeObserver().removeOnTouchModeChangeListener(this);
            InsertionHandleView insertionHandleView = this.mHandle;
            if (insertionHandleView != null) {
                insertionHandleView.onDetached();
            }
        }

        public boolean isCursorBeingModified() {
            InsertionHandleView insertionHandleView = this.mHandle;
            return insertionHandleView != null && insertionHandleView.isDragging();
        }

        public boolean isActive() {
            InsertionHandleView insertionHandleView = this.mHandle;
            return insertionHandleView != null && insertionHandleView.isShowing();
        }

        public void invalidateHandle() {
            InsertionHandleView insertionHandleView = this.mHandle;
            if (insertionHandleView != null) {
                insertionHandleView.invalidate();
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    private @interface MagnifierHandleTrigger {
        public static final int INSERTION = 0;
        public static final int SELECTION_END = 2;
        public static final int SELECTION_START = 1;
    }

    private static class MagnifierMotionAnimator {
        private static final long DURATION = 100;
        private float mAnimationCurrentX;
        private float mAnimationCurrentY;
        private float mAnimationStartX;
        private float mAnimationStartY;
        private final ValueAnimator mAnimator;
        private float mLastX;
        private float mLastY;
        private final Magnifier mMagnifier;
        private boolean mMagnifierIsShowing;

        /* synthetic */ MagnifierMotionAnimator(Magnifier x0, AnonymousClass1 x1) {
            this(x0);
        }

        private MagnifierMotionAnimator(Magnifier magnifier) {
            this.mMagnifier = magnifier;
            this.mAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.mAnimator.setDuration((long) DURATION);
            this.mAnimator.setInterpolator(new LinearInterpolator());
            this.mAnimator.addUpdateListener(new -$$Lambda$Editor$MagnifierMotionAnimator$E-RaelOMgCHAzvKgSSZE-hDYeIg(this));
        }

        public /* synthetic */ void lambda$new$0$Editor$MagnifierMotionAnimator(ValueAnimator animation) {
            float f = this.mAnimationStartX;
            this.mAnimationCurrentX = f + ((this.mLastX - f) * animation.getAnimatedFraction());
            f = this.mAnimationStartY;
            this.mAnimationCurrentY = f + ((this.mLastY - f) * animation.getAnimatedFraction());
            this.mMagnifier.show(this.mAnimationCurrentX, this.mAnimationCurrentY);
        }

        private void show(float x, float y) {
            boolean startNewAnimation = this.mMagnifierIsShowing && y != this.mLastY;
            if (startNewAnimation) {
                if (this.mAnimator.isRunning()) {
                    this.mAnimator.cancel();
                    this.mAnimationStartX = this.mAnimationCurrentX;
                    this.mAnimationStartY = this.mAnimationCurrentY;
                } else {
                    this.mAnimationStartX = this.mLastX;
                    this.mAnimationStartY = this.mLastY;
                }
                this.mAnimator.start();
            } else if (!this.mAnimator.isRunning()) {
                this.mMagnifier.show(x, y);
            }
            this.mLastX = x;
            this.mLastY = y;
            this.mMagnifierIsShowing = true;
        }

        private void update() {
            this.mMagnifier.update();
        }

        private void dismiss() {
            this.mMagnifier.dismiss();
            this.mAnimator.cancel();
            this.mMagnifierIsShowing = false;
        }
    }

    private class PhraseAdapter extends BaseAdapter {
        private LayoutInflater mInflater = null;
        private ArrayList<String> mPhraseList = null;

        public PhraseAdapter(ArrayList<String> list) {
            this.mInflater = (LayoutInflater) Editor.this.mTextView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.mPhraseList = list;
        }

        public int getCount() {
            ArrayList arrayList = this.mPhraseList;
            return arrayList == null ? 0 : arrayList.size();
        }

        public Object getItem(int position) {
            ArrayList arrayList = this.mPhraseList;
            if (arrayList == null || position >= arrayList.size()) {
                return null;
            }
            return this.mPhraseList.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView;
            if (convertView == null) {
                textView = (TextView) this.mInflater.inflate((int) R.layout.phrase_list_item, parent, false);
            } else {
                textView = (TextView) convertView;
            }
            textView.setText((String) getItem(position));
            if (getCount() == 1) {
                textView.getBackground().setLevel(3);
            } else if (position == 0) {
                textView.getBackground().setLevel(0);
            } else if (position == getCount() - 1) {
                textView.getBackground().setLevel(2);
            } else {
                textView.getBackground().setLevel(1);
            }
            return textView;
        }
    }

    private class PositionListener implements OnPreDrawListener {
        private final int MAXIMUM_NUMBER_OF_LISTENERS;
        private boolean[] mCanMove;
        private int mNumberOfListeners;
        private boolean mPositionHasChanged;
        private TextViewPositionListener[] mPositionListeners;
        private int mPositionX;
        private int mPositionY;
        private boolean mScrollHasChanged;
        final int[] mTempCoords;

        private PositionListener() {
            this.MAXIMUM_NUMBER_OF_LISTENERS = 6;
            this.mPositionListeners = new TextViewPositionListener[6];
            this.mCanMove = new boolean[6];
            this.mPositionHasChanged = true;
            this.mTempCoords = new int[2];
        }

        /* synthetic */ PositionListener(Editor x0, AnonymousClass1 x1) {
            this();
        }

        public void addSubscriber(TextViewPositionListener positionListener, boolean canMove) {
            if (this.mNumberOfListeners == 0) {
                updatePosition();
                Editor.this.mTextView.getViewTreeObserver().addOnPreDrawListener(this);
            }
            int emptySlotIndex = -1;
            int i = 0;
            while (i < 6) {
                TextViewPositionListener listener = this.mPositionListeners[i];
                if (listener != positionListener) {
                    if (emptySlotIndex < 0 && listener == null) {
                        emptySlotIndex = i;
                    }
                    i++;
                } else {
                    return;
                }
            }
            this.mPositionListeners[emptySlotIndex] = positionListener;
            this.mCanMove[emptySlotIndex] = canMove;
            this.mNumberOfListeners++;
        }

        public void removeSubscriber(TextViewPositionListener positionListener) {
            for (int i = 0; i < 6; i++) {
                TextViewPositionListener[] textViewPositionListenerArr = this.mPositionListeners;
                if (textViewPositionListenerArr[i] == positionListener) {
                    textViewPositionListenerArr[i] = null;
                    this.mNumberOfListeners--;
                    break;
                }
            }
            if (this.mNumberOfListeners == 0) {
                Editor.this.mTextView.getViewTreeObserver().removeOnPreDrawListener(this);
            }
        }

        public int getPositionX() {
            return this.mPositionX;
        }

        public int getPositionY() {
            return this.mPositionY;
        }

        public boolean onPreDraw() {
            updatePosition();
            int i = 0;
            while (i < 6) {
                if (this.mPositionHasChanged || this.mScrollHasChanged || this.mCanMove[i]) {
                    TextViewPositionListener positionListener = this.mPositionListeners[i];
                    if (positionListener != null) {
                        positionListener.updatePosition(this.mPositionX, this.mPositionY, this.mPositionHasChanged, this.mScrollHasChanged);
                    }
                }
                i++;
            }
            this.mScrollHasChanged = false;
            return true;
        }

        private void updatePosition() {
            Editor.this.mTextView.getLocationInWindow(this.mTempCoords);
            int[] iArr = this.mTempCoords;
            boolean z = (iArr[0] == this.mPositionX && iArr[1] == this.mPositionY) ? false : true;
            this.mPositionHasChanged = z;
            iArr = this.mTempCoords;
            this.mPositionX = iArr[0];
            this.mPositionY = iArr[1];
        }

        public void onScrollChanged() {
            this.mScrollHasChanged = true;
        }
    }

    static final class ProcessTextIntentActionsHandler {
        /* synthetic */ ProcessTextIntentActionsHandler(Editor x0, AnonymousClass1 x1) {
            this(x0);
        }

        private ProcessTextIntentActionsHandler(Editor editor) {
        }

        public void initializeAccessibilityActions() {
        }

        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo nodeInfo) {
        }

        public boolean performAccessibilityAction(int actionId) {
            return false;
        }
    }

    private abstract class SelectionHandleView extends HandleView implements Fader {
        @HandleType
        private final int mHandleType = 0;
        private float mTranslation;
        private int mY = -1;

        public SelectionHandleView(Drawable drawableLtr, Drawable drawableRtl) {
            super(Editor.this, drawableLtr, drawableRtl, null);
            setPivotX((float) (this.mHotspotX + getHorizontalOffset()));
            ((AnimatePopupWindow) this.mContainer).setFader(this);
        }

        public boolean onTouchEvent(MotionEvent ev) {
            boolean result = super.onTouchEvent(ev);
            if (ev.getActionMasked() == 1) {
                showActionPopupWindow(300);
            }
            return result;
        }

        public void onDetached() {
            if (Editor.this.hasSelectionController() && !Editor.this.getSelectionController().mTextSelectionModeEnable) {
                hideActionPopupWindow();
            }
            Editor.this.hideEmailPopupWindow();
        }

        public void onAttached() {
            showActionPopupWindow(0);
        }

        public ActionPopupWindow getActionPopupWindow() {
            if (this.mActionPopupWindow == null) {
                this.mActionPopupWindow = new SelectionPopupWindow(this);
            }
            return this.mActionPopupWindow;
        }

        public void setActionPopupWindow(ActionPopupWindow actionPopupWindow) {
            this.mActionPopupWindow = actionPopupWindow;
        }

        public void fadeIn(int x, int y) {
            float startX = ((float) x) + (this.mTranslation * this.mHorizontalScale);
            final int locationY = y;
            this.mY = -1;
            this.mAnimationFadeIn = new AnimatorSet();
            this.mAnimationFadeIn.setInterpolator(new CubicEaseOutInterpolator());
            this.mAnimationFadeIn.setDuration(300);
            ObjectAnimator scaleAnimatorX = ObjectAnimator.ofFloat((Object) this, View.SCALE_X, 0.0f, 1.0f);
            if (this.mTranslation != 0.0f) {
                ValueAnimator.ofFloat(new float[]{startX, (float) x}).addUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float animatedValue = ((Float) animation.getAnimatedValue()).floatValue();
                        if (SelectionHandleView.this.mY == -1) {
                            SelectionHandleView.this.mY = locationY;
                        }
                        SelectionHandleView.this.mContainer.update((int) animatedValue, SelectionHandleView.this.mY, -1, -1);
                    }
                });
                this.mAnimationFadeIn.playTogether(scaleAnimatorX, translationAnimator);
                this.mTranslation = 0.0f;
            } else {
                this.mAnimationFadeIn.playTogether(scaleAnimatorX);
            }
            this.mAnimationFadeIn.start();
        }

        public void fadeOut() {
            ((AnimatePopupWindow) this.mContainer).dismiss();
        }

        public void cancelAnimations() {
            if (this.mAnimationFadeIn != null) {
                this.mAnimationFadeIn.cancel();
            }
            setScaleX(1.0f);
            setTranslationX(0.0f);
        }

        public void setY(int y) {
            this.mY = y;
        }

        public void setTranslation(float translation) {
            this.mTranslation = translation;
        }

        /* Access modifiers changed, original: protected */
        public int getMagnifierHandleTrigger() {
            if (isStartHandle()) {
                return 1;
            }
            return 2;
        }

        public float getHorizontal(Layout layout, int offset) {
            return getHorizontal(layout, offset, isStartHandle());
        }

        private boolean isStartHandle() {
            return true;
        }

        private float getHorizontal(Layout layout, int offset, boolean startHandle) {
            int line = layout.getLineForOffset(offset);
            boolean isRtlParagraph = false;
            boolean isRtlChar = layout.isRtlCharAt(startHandle ? offset : Math.max(offset - 1, 0));
            if (layout.getParagraphDirection(line) == -1) {
                isRtlParagraph = true;
            }
            return isRtlChar == isRtlParagraph ? layout.getPrimaryHorizontal(offset) : layout.getSecondaryHorizontal(offset);
        }
    }

    private class SelectionEndHandleView extends SelectionHandleView {
        public SelectionEndHandleView(Drawable drawableLtr, Drawable drawableRtl) {
            super(drawableLtr, drawableRtl);
        }

        /* Access modifiers changed, original: protected */
        public int getHotspotX(Drawable drawable, boolean isRtlRun) {
            if (isRtlRun) {
                return (drawable.getIntrinsicWidth() * 3) / 4;
            }
            return drawable.getIntrinsicWidth() / 4;
        }

        /* Access modifiers changed, original: protected */
        public int getHorizontalGravity(boolean isRtlRun) {
            return isRtlRun ? 5 : 3;
        }

        public int getCurrentCursorOffset() {
            return Editor.this.mTextView.getSelectionEnd();
        }

        public void updateSelection(int offset) {
            Selection.setSelection((Spannable) Editor.this.mTextView.getText(), Editor.this.mTextView.getSelectionStart(), offset);
            updateDrawable(false);
        }

        public void updatePosition(float x, float y) {
            Layout layout = Editor.this.mTextView.getLayout();
            if (layout == null) {
                positionAndAdjustForCrossingHandles(Editor.this.mTextView.getOffsetForPosition(x, y));
                return;
            }
            if (this.mPreviousLineTouched == -1) {
                this.mPreviousLineTouched = Editor.this.mTextView.getLineAtCoordinate(y);
            }
            int selectionStart = Editor.this.mTextView.getSelectionStart();
            int currLine = Editor.this.getCurrentLineAdjustedForSlop(layout, this.mPreviousLineTouched, y);
            int offset = Editor.this.mTextView.getOffsetAtCoordinate(currLine, x);
            if (offset <= selectionStart) {
                currLine = layout.getLineForOffset(selectionStart);
                offset = Editor.this.mTextView.getOffsetAtCoordinate(currLine, x);
            }
            this.mPreviousLineTouched = currLine;
            positionAndAdjustForCrossingHandles(offset);
        }

        private void positionAndAdjustForCrossingHandles(int offset) {
            int selectionStart = Editor.this.mTextView.getSelectionStart();
            if (offset <= selectionStart) {
                offset = Math.min(selectionStart + 1, Editor.this.mTextView.getText().length());
            }
            positionAtCursorOffset(offset, false);
        }
    }

    class SelectionModifierCursorController implements CursorController {
        private static final int DELAY_BEFORE_REPLACE_ACTION = 200;
        private static final int DRAG_ACCELERATOR_MODE_CHARACTER = 1;
        private static final int DRAG_ACCELERATOR_MODE_INACTIVE = 0;
        private static final int DRAG_ACCELERATOR_MODE_PARAGRAPH = 3;
        private static final int DRAG_ACCELERATOR_MODE_WORD = 2;
        private boolean mDoubleTabed;
        private float mDownPositionX;
        private float mDownPositionY;
        private int mDragAcceleratorMode = 0;
        private SelectionHandleView mEndHandle;
        private boolean mGestureStayedInTapRegion;
        private boolean mHaventMovedEnoughToStartDrag;
        private boolean mInSwipeSelectionMode;
        private int mLineSelectionIsOn = -1;
        private int mMaxTouchOffset;
        private int mMinTouchOffset;
        private long mPreviousTapUpTime = 0;
        private SelectionHandleView mStartHandle;
        private int mStartOffset = -1;
        private int mSwipeSelectionStart;
        private boolean mSwitchedLines = false;
        private boolean mTextSelectionModeEnable;
        private float[] mTranslationCache = new float[]{0.0f, 0.0f};

        SelectionModifierCursorController() {
            resetTouchOffsets();
        }

        public void show() {
            if (!Editor.this.mTextView.isInBatchEditMode()) {
                initDrawables();
                initHandles();
                this.mTextSelectionModeEnable = true;
            }
        }

        private void initDrawables() {
            Editor editor;
            if (Editor.this.mSelectHandleLeft == null) {
                editor = Editor.this;
                editor.mSelectHandleLeft = editor.mTextView.getContext().getResources().getDrawable(R.drawable.text_select_handle_left);
            }
            if (Editor.this.mSelectHandleRight == null) {
                editor = Editor.this;
                editor.mSelectHandleRight = editor.mTextView.getContext().getResources().getDrawable(R.drawable.text_select_handle_right);
            }
        }

        private void initHandleView() {
            Editor editor;
            if (this.mStartHandle == null) {
                editor = Editor.this;
                this.mStartHandle = new SelectionStartHandleView(editor.mSelectHandleLeft, Editor.this.mSelectHandleRight);
            }
            if (this.mEndHandle == null) {
                editor = Editor.this;
                this.mEndHandle = new SelectionEndHandleView(editor.mSelectHandleRight, Editor.this.mSelectHandleLeft);
            }
        }

        private void initHandles() {
            initHandleView();
            this.mStartHandle.setTranslation(this.mTranslationCache[0]);
            this.mEndHandle.setTranslation(this.mTranslationCache[1]);
            this.mStartHandle.show();
            this.mEndHandle.show();
            this.mStartHandle.showActionPopupWindow(200);
            this.mEndHandle.setActionPopupWindow(this.mStartHandle.getActionPopupWindow());
            Editor.this.hideInsertionPointCursorController();
        }

        private void reloadHandleDrawables() {
            SelectionHandleView selectionHandleView = this.mStartHandle;
            if (selectionHandleView != null) {
                selectionHandleView.setDrawables(Editor.this.mSelectHandleLeft, Editor.this.mSelectHandleRight);
                this.mEndHandle.setDrawables(Editor.this.mSelectHandleRight, Editor.this.mSelectHandleLeft);
            }
        }

        public void hide() {
            this.mTextSelectionModeEnable = false;
            SelectionHandleView selectionHandleView = this.mStartHandle;
            if (selectionHandleView != null) {
                selectionHandleView.hide();
            }
            selectionHandleView = this.mEndHandle;
            if (selectionHandleView != null) {
                selectionHandleView.hide();
            }
        }

        public void enterDrag(int dragAcceleratorMode) {
            show();
            this.mDragAcceleratorMode = dragAcceleratorMode;
            this.mStartOffset = Editor.this.mTextView.getOffsetForPosition(Editor.this.mLastDownPositionX, Editor.this.mLastDownPositionY);
            this.mLineSelectionIsOn = Editor.this.mTextView.getLineAtCoordinate(Editor.this.mLastDownPositionY);
            hide();
            Editor.this.mTextView.getParent().requestDisallowInterceptTouchEvent(true);
            Editor.this.mTextView.cancelLongPress();
        }

        public void onTouchEvent(MotionEvent event) {
            boolean isMouse = event.isFromSource(true);
            float x = event.getX();
            float y = event.getY();
            int actionMasked = event.getActionMasked();
            boolean stayedInArea = false;
            float deltaX;
            if (actionMasked != 0) {
                SelectionHandleView selectionHandleView;
                if (actionMasked == 1) {
                    this.mPreviousTapUpTime = SystemClock.uptimeMillis();
                    if (isDragAcceleratorActive()) {
                        updateSelection(event);
                        selectionHandleView = this.mEndHandle;
                        if (selectionHandleView != null) {
                            selectionHandleView.dismissMagnifier();
                        }
                        requestDisallowInterceptTouchEvent(false);
                        resetDragAcceleratorState();
                        if (Editor.this.mTextView.hasSelection() || Editor.this.mTapState == 2) {
                            Editor.this.startSelectionActionModeAsync(this.mHaventMovedEnoughToStartDrag);
                        }
                    } else {
                        return;
                    }
                } else if (actionMasked == 2) {
                    if (this.mGestureStayedInTapRegion || this.mHaventMovedEnoughToStartDrag) {
                        deltaX = x - this.mDownPositionX;
                        float deltaY = y - this.mDownPositionY;
                        float distanceSquared = (deltaX * deltaX) + (deltaY * deltaY);
                        ViewConfiguration viewConfiguration = ViewConfiguration.get(Editor.this.mTextView.getContext());
                        int doubleTapTouchSlop = viewConfiguration.getScaledDoubleTapTouchSlop();
                        int touchSlop = viewConfiguration.getScaledTouchSlop();
                        if (this.mGestureStayedInTapRegion) {
                            this.mGestureStayedInTapRegion = distanceSquared <= ((float) (doubleTapTouchSlop * doubleTapTouchSlop));
                        }
                        if (this.mHaventMovedEnoughToStartDrag) {
                            this.mHaventMovedEnoughToStartDrag = distanceSquared <= ((float) (touchSlop * touchSlop));
                        }
                    }
                    if (isMouse && !isDragAcceleratorActive()) {
                        actionMasked = Editor.this.mTextView.getOffsetForPosition(x, y);
                        if (Editor.this.mTextView.hasSelection() && ((!this.mHaventMovedEnoughToStartDrag || this.mStartOffset != actionMasked) && actionMasked >= Editor.this.mTextView.getSelectionStart() && actionMasked <= Editor.this.mTextView.getSelectionEnd())) {
                            Editor.this.startDragAndDrop();
                            return;
                        } else if (this.mStartOffset != actionMasked) {
                            Editor.this.stopTextActionMode();
                            enterDrag(1);
                            Editor.this.mDiscardNextActionUp = true;
                            this.mHaventMovedEnoughToStartDrag = false;
                        }
                    }
                    selectionHandleView = this.mStartHandle;
                    if (selectionHandleView == null || !selectionHandleView.isShowing()) {
                        updateSelection(event);
                        if (Editor.this.mTextView.hasSelection()) {
                            selectionHandleView = this.mEndHandle;
                            if (selectionHandleView != null) {
                                selectionHandleView.updateMagnifier(event);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    return;
                } else if (actionMasked == 5 || actionMasked == 6) {
                    if (Editor.this.mTextView.getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH_DISTINCT)) {
                        updateMinAndMaxOffsets(event);
                        return;
                    }
                    return;
                }
                requestDisallowInterceptTouchEvent(false);
                return;
            }
            this.mInSwipeSelectionMode = false;
            if (Editor.this.extractedTextModeWillBeStarted()) {
                hide();
                return;
            }
            actionMasked = Editor.this.mTextView.getOffsetForPosition(x, y);
            this.mMaxTouchOffset = actionMasked;
            this.mMinTouchOffset = actionMasked;
            if (this.mGestureStayedInTapRegion && (Editor.this.mTapState == 2 || Editor.this.mTapState == 3)) {
                deltaX = x - this.mDownPositionX;
                float deltaY2 = y - this.mDownPositionY;
                float distanceSquared2 = (deltaX * deltaX) + (deltaY2 * deltaY2);
                int doubleTapSlop = ViewConfiguration.get(Editor.this.mTextView.getContext()).getScaledDoubleTapSlop();
                if (distanceSquared2 < ((float) (doubleTapSlop * doubleTapSlop))) {
                    stayedInArea = true;
                }
                if (stayedInArea && (isMouse || Editor.this.isPositionOnText(x, y))) {
                    if (Editor.this.mTapState == 2) {
                        Editor.this.selectCurrentWordAndStartDrag();
                    } else if (Editor.this.mTapState == 3) {
                        selectCurrentParagraphAndStartDrag();
                    }
                    this.mSwipeSelectionStart = Editor.this.mTextView.getOffsetForPosition(x, y);
                    this.mInSwipeSelectionMode = true;
                    this.mDoubleTabed = true;
                    Editor.this.mDiscardNextActionUp = true;
                }
            }
            this.mDownPositionX = x;
            this.mDownPositionY = y;
            this.mGestureStayedInTapRegion = true;
            this.mHaventMovedEnoughToStartDrag = true;
        }

        private void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            ViewParent parent = Editor.this.mTextView.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(disallowIntercept);
            }
        }

        private void updateSelection(MotionEvent event) {
            if (Editor.this.mTextView.getLayout() != null) {
                int i = this.mDragAcceleratorMode;
                if (i == 1) {
                    updateCharacterBasedSelection(event);
                } else if (i == 2) {
                    updateWordBasedSelection(event);
                } else if (i == 3) {
                    updateParagraphBasedSelection(event);
                }
            }
        }

        private boolean selectCurrentParagraphAndStartDrag() {
            if (Editor.this.mInsertionActionModeRunnable != null) {
                Editor.this.mTextView.removeCallbacks(Editor.this.mInsertionActionModeRunnable);
            }
            Editor.this.stopTextActionMode();
            if (!Editor.this.selectCurrentParagraph()) {
                return false;
            }
            enterDrag(3);
            return true;
        }

        private void updateCharacterBasedSelection(MotionEvent event) {
            Selection.setSelection((Spannable) Editor.this.mTextView.getText(), this.mStartOffset, Editor.this.mTextView.getOffsetForPosition(event.getX(), event.getY()));
        }

        private void updateWordBasedSelection(MotionEvent event) {
            if (!this.mHaventMovedEnoughToStartDrag) {
                int currLine;
                int touchSlop;
                int startOffset;
                boolean isMouse = event.isFromSource(true);
                ViewConfiguration viewConfig = ViewConfiguration.get(Editor.this.mTextView.getContext());
                float eventX = event.getX();
                float eventY = event.getY();
                if (isMouse) {
                    currLine = Editor.this.mTextView.getLineAtCoordinate(eventY);
                } else {
                    float y = eventY;
                    if (this.mSwitchedLines) {
                        float fingerOffset;
                        touchSlop = viewConfig.getScaledTouchSlop();
                        SelectionHandleView selectionHandleView = this.mStartHandle;
                        if (selectionHandleView != null) {
                            fingerOffset = selectionHandleView.getIdealVerticalOffset();
                        } else {
                            fingerOffset = (float) touchSlop;
                        }
                        y = eventY - fingerOffset;
                    }
                    touchSlop = Editor.this;
                    touchSlop = touchSlop.getCurrentLineAdjustedForSlop(touchSlop.mTextView.getLayout(), this.mLineSelectionIsOn, y);
                    if (this.mSwitchedLines || touchSlop == this.mLineSelectionIsOn) {
                        currLine = touchSlop;
                    } else {
                        this.mSwitchedLines = true;
                        return;
                    }
                }
                touchSlop = Editor.this.mTextView.getOffsetAtCoordinate(currLine, eventX);
                if (this.mStartOffset < touchSlop) {
                    touchSlop = Editor.this.getWordEnd(touchSlop);
                    startOffset = Editor.this.getWordStart(this.mStartOffset);
                } else {
                    touchSlop = Editor.this.getWordStart(touchSlop);
                    startOffset = Editor.this.getWordEnd(this.mStartOffset);
                    if (startOffset == touchSlop) {
                        touchSlop = Editor.this.getNextCursorOffset(touchSlop, false);
                    }
                }
                this.mLineSelectionIsOn = currLine;
                Selection.setSelection((Spannable) Editor.this.mTextView.getText(), startOffset, touchSlop);
            }
        }

        private void updateParagraphBasedSelection(MotionEvent event) {
            int offset = Editor.this.mTextView.getOffsetForPosition(event.getX(), event.getY());
            long paragraphsRange = Editor.this.getParagraphsRange(Math.min(offset, this.mStartOffset), Math.max(offset, this.mStartOffset));
            Selection.setSelection((Spannable) Editor.this.mTextView.getText(), TextUtils.unpackRangeStartFromLong(paragraphsRange), TextUtils.unpackRangeEndFromLong(paragraphsRange));
        }

        private void updateMinAndMaxOffsets(MotionEvent event) {
            int pointerCount = event.getPointerCount();
            for (int index = 0; index < pointerCount; index++) {
                int offset = Editor.this.mTextView.getOffsetForPosition(event.getX(index), event.getY(index));
                if (offset < this.mMinTouchOffset) {
                    this.mMinTouchOffset = offset;
                }
                if (offset > this.mMaxTouchOffset) {
                    this.mMaxTouchOffset = offset;
                }
            }
        }

        public int getMinTouchOffset() {
            return this.mMinTouchOffset;
        }

        public void setMinTouchOffset(int minTouchOffset) {
            this.mMinTouchOffset = minTouchOffset;
        }

        public int getMaxTouchOffset() {
            return this.mMaxTouchOffset;
        }

        public void setMaxTouchOffset(int maxTouchOffset) {
            this.mMaxTouchOffset = maxTouchOffset;
        }

        public void resetTouchOffsets() {
            this.mMaxTouchOffset = -1;
            this.mMinTouchOffset = -1;
            resetDragAcceleratorState();
        }

        private void resetDragAcceleratorState() {
            this.mStartOffset = -1;
            this.mDragAcceleratorMode = 0;
            this.mSwitchedLines = false;
            int selectionStart = Editor.this.mTextView.getSelectionStart();
            int selectionEnd = Editor.this.mTextView.getSelectionEnd();
            if (selectionStart > selectionEnd) {
                Selection.setSelection((Spannable) Editor.this.mTextView.getText(), selectionEnd, selectionStart);
            }
        }

        public boolean isSelectionStartDragged() {
            SelectionHandleView selectionHandleView = this.mStartHandle;
            return selectionHandleView != null && selectionHandleView.isDragging();
        }

        public boolean isCursorBeingModified() {
            if (!(isDragAcceleratorActive() || isSelectionStartDragged())) {
                SelectionHandleView selectionHandleView = this.mEndHandle;
                if (selectionHandleView == null || !selectionHandleView.isDragging()) {
                    return false;
                }
            }
            return true;
        }

        public boolean isDragAcceleratorActive() {
            return this.mDragAcceleratorMode != 0;
        }

        public void onTouchModeChanged(boolean isInTouchMode) {
            if (!isInTouchMode) {
                hide();
            }
        }

        public void onDetached() {
            Editor.this.mTextView.getViewTreeObserver().removeOnTouchModeChangeListener(this);
            SelectionHandleView selectionHandleView = this.mStartHandle;
            if (selectionHandleView != null) {
                selectionHandleView.hideActionPopupWindow();
            }
            selectionHandleView = this.mEndHandle;
            if (selectionHandleView != null) {
                selectionHandleView.hideActionPopupWindow();
            }
        }

        public boolean isActive() {
            SelectionHandleView selectionHandleView = this.mStartHandle;
            return selectionHandleView != null && selectionHandleView.isShowing();
        }

        public void invalidateHandles() {
            SelectionHandleView selectionHandleView = this.mStartHandle;
            if (selectionHandleView != null) {
                selectionHandleView.invalidate();
            }
            selectionHandleView = this.mEndHandle;
            if (selectionHandleView != null) {
                selectionHandleView.invalidate();
            }
        }

        public void setTranslationCache(float[] translationCache) {
            this.mTranslationCache = translationCache;
        }
    }

    private class SelectionPopupWindow extends ActionPopupWindow {
        private LayoutAnimationController mLayoutAnimationController;

        public SelectionPopupWindow(HandleView handleView) {
            super(handleView);
        }

        /* Access modifiers changed, original: protected */
        public void createAnimations() {
            this.mAnimationFadeIn = new AnimatorSet();
            ObjectAnimator scaleAnimatorX = ObjectAnimator.ofFloat(this.mContentView, View.SCALE_X, 0.5f, 1.0f);
            this.mAnimationFadeIn.setInterpolator(new CubicEaseOutInterpolator());
            this.mAnimationFadeIn.setDuration(200);
            this.mAnimationFadeIn.playTogether(scaleAnimatorX);
            this.mLayoutAnimationController = new LayoutAnimationController(AnimationUtils.loadAnimation(Editor.this.mTextView.getContext(), R.anim.text_select_layout_fade_in));
            this.mLayoutAnimationController.setInterpolator(new CubicEaseOutInterpolator());
            this.mLayoutAnimationController.setDelay(0.05f);
        }

        public void fadeIn(int x, int y) {
            this.mContentView.setPivotX((float) (this.mContentView.getMeasuredWidth() / 2));
            this.mContentView.setPivotY((float) (this.mContentView.getMeasuredHeight() / 2));
            this.mAnimationFadeIn.start();
            this.mMainPanel.setLayoutAnimation(this.mLayoutAnimationController);
        }
    }

    private class SelectionStartHandleView extends SelectionHandleView {
        public SelectionStartHandleView(Drawable drawableLtr, Drawable drawableRtl) {
            super(drawableLtr, drawableRtl);
        }

        /* Access modifiers changed, original: protected */
        public int getHotspotX(Drawable drawable, boolean isRtlRun) {
            if (isRtlRun) {
                return drawable.getIntrinsicWidth() / 4;
            }
            return (drawable.getIntrinsicWidth() * 3) / 4;
        }

        /* Access modifiers changed, original: protected */
        public int getHorizontalGravity(boolean isRtlRun) {
            return isRtlRun ? 3 : 5;
        }

        public int getCurrentCursorOffset() {
            return Editor.this.mTextView.getSelectionStart();
        }

        public void updateSelection(int offset) {
            if (offset < 0) {
                offset = 0;
            }
            Selection.setSelection((Spannable) Editor.this.mTextView.getText(), offset, Editor.this.mTextView.getSelectionEnd());
            updateDrawable(false);
        }

        public void updatePosition(float x, float y) {
            Layout layout = Editor.this.mTextView.getLayout();
            if (layout == null) {
                positionAndAdjustForCrossingHandles(Editor.this.mTextView.getOffsetForPosition(x, y));
                return;
            }
            if (this.mPreviousLineTouched == -1) {
                this.mPreviousLineTouched = Editor.this.mTextView.getLineAtCoordinate(y);
            }
            int selectionEnd = Editor.this.mTextView.getSelectionEnd();
            int currLine = Editor.this.getCurrentLineAdjustedForSlop(layout, this.mPreviousLineTouched, y);
            int offset = Editor.this.mTextView.getOffsetAtCoordinate(currLine, x);
            if (offset >= selectionEnd) {
                currLine = layout.getLineForOffset(selectionEnd);
                offset = Editor.this.mTextView.getOffsetAtCoordinate(currLine, x);
            }
            this.mPreviousLineTouched = currLine;
            positionAndAdjustForCrossingHandles(offset);
        }

        private void positionAndAdjustForCrossingHandles(int offset) {
            int selectionEnd = Editor.this.mTextView.getSelectionEnd();
            if (offset >= selectionEnd) {
                offset = Math.max(0, selectionEnd - 1);
            }
            positionAtCursorOffset(offset, false);
        }
    }

    class SpanController implements SpanWatcher {
        private static final int DISPLAY_TIMEOUT_MS = 3000;
        private Runnable mHidePopup;
        private EasyEditPopupWindow mPopupWindow;

        SpanController() {
        }

        private boolean isNonIntermediateSelectionSpan(Spannable text, Object span) {
            return (Selection.SELECTION_START == span || Selection.SELECTION_END == span) && (text.getSpanFlags(span) & 512) == 0;
        }

        public void onSpanAdded(Spannable text, Object span, int start, int end) {
            if (isNonIntermediateSelectionSpan(text, span)) {
                Editor.this.sendUpdateSelection();
            } else if (span instanceof EasyEditSpan) {
                if (this.mPopupWindow == null) {
                    this.mPopupWindow = new EasyEditPopupWindow(Editor.this, null);
                    this.mHidePopup = new Runnable() {
                        public void run() {
                            SpanController.this.hide();
                        }
                    };
                }
                if (this.mPopupWindow.mEasyEditSpan != null) {
                    this.mPopupWindow.mEasyEditSpan.setDeleteEnabled(false);
                }
                this.mPopupWindow.setEasyEditSpan((EasyEditSpan) span);
                this.mPopupWindow.setOnDeleteListener(new EasyEditDeleteListener() {
                    public void onDeleteClick(EasyEditSpan span) {
                        Editable editable = (Editable) Editor.this.mTextView.getText();
                        int start = editable.getSpanStart(span);
                        int end = editable.getSpanEnd(span);
                        if (start >= 0 && end >= 0) {
                            SpanController.this.sendEasySpanNotification(1, span);
                            Editor.this.mTextView.deleteText_internal(start, end);
                        }
                        editable.removeSpan(span);
                    }
                });
                if (Editor.this.mTextView.getWindowVisibility() == 0 && Editor.this.mTextView.getLayout() != null && !Editor.this.extractedTextModeWillBeStarted()) {
                    this.mPopupWindow.show();
                    Editor.this.mTextView.removeCallbacks(this.mHidePopup);
                    Editor.this.mTextView.postDelayed(this.mHidePopup, AnrMonitor.PERF_EVENT_LOGGING_TIMEOUT);
                }
            }
        }

        public void onSpanRemoved(Spannable text, Object span, int start, int end) {
            if (isNonIntermediateSelectionSpan(text, span)) {
                Editor.this.sendUpdateSelection();
                return;
            }
            EasyEditPopupWindow easyEditPopupWindow = this.mPopupWindow;
            if (easyEditPopupWindow != null && span == easyEditPopupWindow.mEasyEditSpan) {
                hide();
            }
        }

        public void onSpanChanged(Spannable text, Object span, int previousStart, int previousEnd, int newStart, int newEnd) {
            if (isNonIntermediateSelectionSpan(text, span)) {
                Editor.this.sendUpdateSelection();
            } else if (this.mPopupWindow != null && (span instanceof EasyEditSpan)) {
                EasyEditSpan easyEditSpan = (EasyEditSpan) span;
                sendEasySpanNotification(2, easyEditSpan);
                text.removeSpan(easyEditSpan);
            }
        }

        public void hide() {
            EasyEditPopupWindow easyEditPopupWindow = this.mPopupWindow;
            if (easyEditPopupWindow != null) {
                easyEditPopupWindow.hide();
                Editor.this.mTextView.removeCallbacks(this.mHidePopup);
            }
        }

        private void sendEasySpanNotification(int textChangedType, EasyEditSpan span) {
            try {
                PendingIntent pendingIntent = span.getPendingIntent();
                if (pendingIntent != null) {
                    Intent intent = new Intent();
                    intent.putExtra(EasyEditSpan.EXTRA_TEXT_CHANGED_TYPE, textChangedType);
                    pendingIntent.send(Editor.this.mTextView.getContext(), 0, intent);
                }
            } catch (CanceledException e) {
                Log.w("Editor", "PendingIntent for notification cannot be sent", e);
            }
        }
    }

    private class SuggestionInfo {
        TextAppearanceSpan highlightSpan;
        int suggestionEnd;
        int suggestionIndex;
        SuggestionSpan suggestionSpan;
        int suggestionStart;
        SpannableStringBuilder text;

        private SuggestionInfo() {
            this.text = new SpannableStringBuilder();
            this.highlightSpan = new TextAppearanceSpan(Editor.this.mTextView.getContext(), 16974104);
        }

        /* synthetic */ SuggestionInfo(Editor x0, AnonymousClass1 x1) {
            this();
        }
    }

    private final class SuggestionsPopupWindow extends PinnedPopupWindow implements OnItemClickListener {
        private static final int ADD_TO_DICTIONARY = -1;
        private static final int DELETE_TEXT = -2;
        private static final int MAX_NUMBER_SUGGESTIONS = 5;
        private boolean mCursorWasVisibleBeforeSuggestions;
        private boolean mIsShowingUp = false;
        private int mNumberOfSuggestions;
        private final HashMap<SuggestionSpan, Integer> mSpansLengths;
        private SuggestionInfo[] mSuggestionInfos;
        private final Comparator<SuggestionSpan> mSuggestionSpanComparator;
        private SuggestionAdapter mSuggestionsAdapter;

        private class CustomPopupWindow extends AnimatePopupWindow {
            public CustomPopupWindow(Context context, int defStyle) {
                super(context, null, defStyle);
            }

            public void dismiss() {
                super.dismiss();
                Editor.this.getPositionListener().removeSubscriber(SuggestionsPopupWindow.this);
                ((Spannable) Editor.this.mTextView.getText()).removeSpan(Editor.this.mSuggestionRangeSpan);
                Editor.this.mTextView.setCursorVisible(SuggestionsPopupWindow.this.mCursorWasVisibleBeforeSuggestions);
                if (Editor.this.hasInsertionController()) {
                    Editor.this.getInsertionController().show();
                }
            }
        }

        private class SuggestionAdapter extends BaseAdapter {
            private LayoutInflater mInflater;

            private SuggestionAdapter() {
                this.mInflater = (LayoutInflater) Editor.this.mTextView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            /* synthetic */ SuggestionAdapter(SuggestionsPopupWindow x0, AnonymousClass1 x1) {
                this();
            }

            public int getCount() {
                return SuggestionsPopupWindow.this.mNumberOfSuggestions;
            }

            public Object getItem(int position) {
                return SuggestionsPopupWindow.this.mSuggestionInfos[position];
            }

            public long getItemId(int position) {
                return (long) position;
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) convertView;
                if (textView == null) {
                    textView = (TextView) this.mInflater.inflate(Editor.this.mTextView.mTextEditSuggestionItemLayout, parent, false);
                }
                SuggestionInfo suggestionInfo = SuggestionsPopupWindow.this.mSuggestionInfos[position];
                textView.setText(suggestionInfo.text);
                if (suggestionInfo.suggestionIndex == -1 || suggestionInfo.suggestionIndex == -2) {
                    textView.setBackgroundColor(0);
                } else {
                    textView.setBackgroundColor(-1);
                }
                return textView;
            }
        }

        private class SuggestionSpanComparator implements Comparator<SuggestionSpan> {
            private SuggestionSpanComparator() {
            }

            /* synthetic */ SuggestionSpanComparator(SuggestionsPopupWindow x0, AnonymousClass1 x1) {
                this();
            }

            public int compare(SuggestionSpan span1, SuggestionSpan span2) {
                int flag1 = span1.getFlags();
                int flag2 = span2.getFlags();
                if (flag1 != flag2) {
                    boolean misspelled2 = false;
                    boolean easy1 = (flag1 & 1) != 0;
                    boolean easy2 = (flag2 & 1) != 0;
                    boolean misspelled1 = (flag1 & 2) != 0;
                    if ((flag2 & 2) != 0) {
                        misspelled2 = true;
                    }
                    if (easy1 && !misspelled1) {
                        return -1;
                    }
                    if (easy2 && !misspelled2) {
                        return 1;
                    }
                    if (misspelled1) {
                        return -1;
                    }
                    if (misspelled2) {
                        return 1;
                    }
                }
                return ((Integer) SuggestionsPopupWindow.this.mSpansLengths.get(span1)).intValue() - ((Integer) SuggestionsPopupWindow.this.mSpansLengths.get(span2)).intValue();
            }
        }

        public SuggestionsPopupWindow() {
            super();
            this.mCursorWasVisibleBeforeSuggestions = Editor.this.mCursorVisible;
            this.mSuggestionSpanComparator = new SuggestionSpanComparator(this, null);
            this.mSpansLengths = new HashMap();
        }

        /* Access modifiers changed, original: protected */
        public void createPopupWindow() {
            this.mPopupWindow = new CustomPopupWindow(Editor.this.mTextView.getContext(), 16843635);
            this.mPopupWindow.setInputMethodMode(2);
            this.mPopupWindow.setFocusable(true);
            this.mPopupWindow.setClippingEnabled(false);
        }

        /* Access modifiers changed, original: protected */
        public void initContentView() {
            ListView listView = new ListView(Editor.this.mTextView.getContext());
            this.mSuggestionsAdapter = new SuggestionAdapter(this, null);
            listView.setAdapter(this.mSuggestionsAdapter);
            listView.setOnItemClickListener(this);
            this.mContentView = listView;
            this.mSuggestionInfos = new SuggestionInfo[7];
            int i = 0;
            while (true) {
                SuggestionInfo[] suggestionInfoArr = this.mSuggestionInfos;
                if (i < suggestionInfoArr.length) {
                    suggestionInfoArr[i] = new SuggestionInfo(Editor.this, null);
                    i++;
                } else {
                    return;
                }
            }
        }

        public boolean isShowingUp() {
            return this.mIsShowingUp;
        }

        public void onParentLostFocus() {
            this.mIsShowingUp = false;
        }

        private SuggestionSpan[] getSuggestionSpans() {
            int pos = Editor.this.mTextView.getSelectionStart();
            Spannable spannable = (Spannable) Editor.this.mTextView.getText();
            SuggestionSpan[] suggestionSpans = (SuggestionSpan[]) spannable.getSpans(pos, pos, SuggestionSpan.class);
            this.mSpansLengths.clear();
            for (SuggestionSpan suggestionSpan : suggestionSpans) {
                this.mSpansLengths.put(suggestionSpan, Integer.valueOf(spannable.getSpanEnd(suggestionSpan) - spannable.getSpanStart(suggestionSpan)));
            }
            Arrays.sort(suggestionSpans, this.mSuggestionSpanComparator);
            return suggestionSpans;
        }

        public void show() {
            if ((Editor.this.mTextView.getText() instanceof Editable) && updateSuggestions()) {
                this.mCursorWasVisibleBeforeSuggestions = Editor.this.mCursorVisible;
                Editor.this.mTextView.setCursorVisible(false);
                this.mIsShowingUp = true;
                super.show();
            }
        }

        /* Access modifiers changed, original: protected */
        public void measureContent() {
            DisplayMetrics displayMetrics = Editor.this.mTextView.getResources().getDisplayMetrics();
            int horizontalMeasure = MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, Integer.MIN_VALUE);
            int verticalMeasure = MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, Integer.MIN_VALUE);
            int width = 0;
            View view = null;
            for (int i = 0; i < this.mNumberOfSuggestions; i++) {
                view = this.mSuggestionsAdapter.getView(i, view, this.mContentView);
                view.getLayoutParams().width = -2;
                view.measure(horizontalMeasure, verticalMeasure);
                width = Math.max(width, view.getMeasuredWidth());
            }
            this.mContentView.measure(MeasureSpec.makeMeasureSpec(width, 1073741824), verticalMeasure);
            Drawable popupBackground = this.mPopupWindow.getBackground();
            if (popupBackground != null) {
                if (Editor.this.mTempRect == null) {
                    Editor.this.mTempRect = new Rect();
                }
                popupBackground.getPadding(Editor.this.mTempRect);
                width += Editor.this.mTempRect.left + Editor.this.mTempRect.right;
            }
            this.mPopupWindow.setWidth(width);
        }

        /* Access modifiers changed, original: protected */
        public int getTextOffset() {
            return Editor.this.mTextView.getSelectionStart();
        }

        /* Access modifiers changed, original: protected */
        public int getVerticalLocalPosition(int line) {
            return Editor.this.mTextView.getLayout().getLineBottom(line);
        }

        /* Access modifiers changed, original: protected */
        public int clipVertically(int positionY) {
            return Math.min(positionY, Editor.this.mTextView.getResources().getDisplayMetrics().heightPixels - this.mContentView.getMeasuredHeight());
        }

        public void hide() {
            super.hide();
        }

        private boolean updateSuggestions() {
            Spannable spannable = (Spannable) Editor.this.mTextView.getText();
            SuggestionSpan[] suggestionSpans = getSuggestionSpans();
            int nbSpans = suggestionSpans.length;
            if (nbSpans == 0) {
                return false;
            }
            this.mNumberOfSuggestions = 0;
            int spanUnionStart = Editor.this.mTextView.getText().length();
            int spanUnionEnd = 0;
            SuggestionSpan misspelledSpan = null;
            int underlineColor = 0;
            int spanIndex = 0;
            while (spanIndex < nbSpans) {
                SuggestionSpan[] suggestionSpans2;
                int spanUnionStart2;
                int spanUnionEnd2;
                SuggestionSpan misspelledSpan2;
                SuggestionSpan suggestionSpan = suggestionSpans[spanIndex];
                int spanStart = spannable.getSpanStart(suggestionSpan);
                int spanEnd = spannable.getSpanEnd(suggestionSpan);
                spanUnionStart = Math.min(spanStart, spanUnionStart);
                spanUnionEnd = Math.max(spanEnd, spanUnionEnd);
                if ((suggestionSpan.getFlags() & 2) != 0) {
                    misspelledSpan = suggestionSpan;
                }
                if (spanIndex == 0) {
                    underlineColor = suggestionSpan.getUnderlineColor();
                }
                String[] suggestions = suggestionSpan.getSuggestions();
                int nbSuggestions = suggestions.length;
                int suggestionIndex = 0;
                while (suggestionIndex < nbSuggestions) {
                    CharSequence suggestion = suggestions[suggestionIndex];
                    boolean suggestionIsDuplicate = false;
                    suggestionSpans2 = suggestionSpans;
                    suggestionSpans = null;
                    while (true) {
                        spanUnionStart2 = spanUnionStart;
                        if (suggestionSpans >= this.mNumberOfSuggestions) {
                            spanUnionEnd2 = spanUnionEnd;
                            misspelledSpan2 = misspelledSpan;
                            break;
                        }
                        if (this.mSuggestionInfos[suggestionSpans].text.toString().equals(suggestion)) {
                            SuggestionSpan otherSuggestionSpan = this.mSuggestionInfos[suggestionSpans].suggestionSpan;
                            spanUnionEnd2 = spanUnionEnd;
                            spanUnionEnd = spannable.getSpanStart(otherSuggestionSpan);
                            misspelledSpan2 = misspelledSpan;
                            misspelledSpan = spannable.getSpanEnd(otherSuggestionSpan);
                            if (spanStart == spanUnionEnd && spanEnd == misspelledSpan) {
                                suggestionIsDuplicate = true;
                                break;
                            }
                        }
                        spanUnionEnd2 = spanUnionEnd;
                        misspelledSpan2 = misspelledSpan;
                        suggestionSpans++;
                        spanUnionStart = spanUnionStart2;
                        spanUnionEnd = spanUnionEnd2;
                        misspelledSpan = misspelledSpan2;
                    }
                    if (!suggestionIsDuplicate) {
                        suggestionSpans = this.mSuggestionInfos[this.mNumberOfSuggestions];
                        suggestionSpans.suggestionSpan = suggestionSpan;
                        suggestionSpans.suggestionIndex = suggestionIndex;
                        suggestionSpans.text.replace(0, suggestionSpans.text.length(), suggestion);
                        this.mNumberOfSuggestions++;
                        if (this.mNumberOfSuggestions == 5) {
                            spanIndex = nbSpans;
                            break;
                        }
                    }
                    suggestionIndex++;
                    spanUnionStart = spanUnionStart2;
                    suggestionSpans = suggestionSpans2;
                    spanUnionEnd = spanUnionEnd2;
                    misspelledSpan = misspelledSpan2;
                }
                suggestionSpans2 = suggestionSpans;
                spanUnionStart2 = spanUnionStart;
                spanUnionEnd2 = spanUnionEnd;
                misspelledSpan2 = misspelledSpan;
                spanIndex++;
                spanUnionStart = spanUnionStart2;
                suggestionSpans = suggestionSpans2;
                spanUnionEnd = spanUnionEnd2;
                misspelledSpan = misspelledSpan2;
            }
            for (int i = 0; i < this.mNumberOfSuggestions; i++) {
                highlightTextDifferences(this.mSuggestionInfos[i], spanUnionStart, spanUnionEnd);
            }
            if (misspelledSpan != null) {
                int misspelledStart = spannable.getSpanStart(misspelledSpan);
                spanIndex = spannable.getSpanEnd(misspelledSpan);
                if (misspelledStart >= 0 && spanIndex > misspelledStart) {
                    SuggestionInfo suggestionInfo = this.mSuggestionInfos[this.mNumberOfSuggestions];
                    suggestionInfo.suggestionSpan = misspelledSpan;
                    suggestionInfo.suggestionIndex = -1;
                    suggestionInfo.text.replace(0, suggestionInfo.text.length(), Editor.this.mTextView.getContext().getString(com.android.internal.R.string.addToDictionary));
                    suggestionInfo.text.setSpan(suggestionInfo.highlightSpan, 0, 0, 33);
                    this.mNumberOfSuggestions++;
                }
            }
            SuggestionInfo suggestionInfo2 = this.mSuggestionInfos[this.mNumberOfSuggestions];
            suggestionInfo2.suggestionSpan = null;
            suggestionInfo2.suggestionIndex = -2;
            suggestionInfo2.text.replace(0, suggestionInfo2.text.length(), Editor.this.mTextView.getContext().getString(com.android.internal.R.string.deleteText));
            suggestionInfo2.text.setSpan(suggestionInfo2.highlightSpan, 0, 0, 33);
            this.mNumberOfSuggestions++;
            if (Editor.this.mSuggestionRangeSpan == null) {
                Editor.this.mSuggestionRangeSpan = new SuggestionRangeSpan();
            }
            if (underlineColor == 0) {
                Editor.this.mSuggestionRangeSpan.setBackgroundColor(Editor.this.mTextView.mHighlightColor);
            } else {
                Editor.this.mSuggestionRangeSpan.setBackgroundColor((16777215 & underlineColor) + (((int) (((float) Color.alpha(underlineColor)) * 1053609165)) << 24));
            }
            spannable.setSpan(Editor.this.mSuggestionRangeSpan, spanUnionStart, spanUnionEnd, 33);
            this.mSuggestionsAdapter.notifyDataSetChanged();
            return true;
        }

        private void highlightTextDifferences(SuggestionInfo suggestionInfo, int unionStart, int unionEnd) {
            Spannable text = (Spannable) Editor.this.mTextView.getText();
            int spanStart = text.getSpanStart(suggestionInfo.suggestionSpan);
            int spanEnd = text.getSpanEnd(suggestionInfo.suggestionSpan);
            suggestionInfo.suggestionStart = spanStart - unionStart;
            suggestionInfo.suggestionEnd = suggestionInfo.suggestionStart + suggestionInfo.text.length();
            suggestionInfo.text.setSpan(suggestionInfo.highlightSpan, 0, suggestionInfo.text.length(), 33);
            String textAsString = text.toString();
            suggestionInfo.text.insert(0, textAsString.substring(unionStart, spanStart));
            suggestionInfo.text.append(textAsString.substring(spanEnd, unionEnd));
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Editable editable = (Editable) Editor.this.mTextView.getText();
            SuggestionInfo suggestionInfo = this.mSuggestionInfos[position];
            int spanUnionStart;
            int spanUnionEnd;
            if (suggestionInfo.suggestionIndex == -2) {
                spanUnionStart = editable.getSpanStart(Editor.this.mSuggestionRangeSpan);
                spanUnionEnd = editable.getSpanEnd(Editor.this.mSuggestionRangeSpan);
                if (spanUnionStart >= 0 && spanUnionEnd > spanUnionStart) {
                    if (spanUnionEnd < editable.length() && Character.isSpaceChar(editable.charAt(spanUnionEnd)) && (spanUnionStart == 0 || Character.isSpaceChar(editable.charAt(spanUnionStart - 1)))) {
                        spanUnionEnd++;
                    }
                    Editor.this.mTextView.deleteText_internal(spanUnionStart, spanUnionEnd);
                }
                hide();
                return;
            }
            spanUnionStart = editable.getSpanStart(suggestionInfo.suggestionSpan);
            int spanEnd = editable.getSpanEnd(suggestionInfo.suggestionSpan);
            SuggestionInfo suggestionInfo2;
            int i;
            Editable editable2;
            if (spanUnionStart < 0) {
                suggestionInfo2 = suggestionInfo;
                i = spanUnionStart;
            } else if (spanEnd <= spanUnionStart) {
                editable2 = editable;
                suggestionInfo2 = suggestionInfo;
                i = spanUnionStart;
            } else {
                String originalText = editable.toString().substring(spanUnionStart, spanEnd);
                String str;
                if (suggestionInfo.suggestionIndex == -1) {
                    Intent intent = new Intent(Settings.ACTION_USER_DICTIONARY_INSERT);
                    intent.putExtra(Words.WORD, originalText);
                    intent.putExtra(Words.LOCALE, Editor.this.mTextView.getTextServicesLocale().toString());
                    intent.setFlags(intent.getFlags() | 268435456);
                    Editor.this.mTextView.getContext().startActivity(intent);
                    editable.removeSpan(suggestionInfo.suggestionSpan);
                    Selection.setSelection(editable, spanEnd);
                    Editor.this.updateSpellCheckSpans(spanUnionStart, spanEnd, false);
                    editable2 = editable;
                    suggestionInfo2 = suggestionInfo;
                    i = spanUnionStart;
                    str = originalText;
                } else {
                    int suggestionSpanFlags;
                    String[] suggestions;
                    int suggestionStart;
                    SuggestionSpan[] suggestionSpans = (SuggestionSpan[]) editable.getSpans(spanUnionStart, spanEnd, SuggestionSpan.class);
                    int length = suggestionSpans.length;
                    int[] suggestionSpansStarts = new int[length];
                    int[] suggestionSpansEnds = new int[length];
                    int[] suggestionSpansFlags = new int[length];
                    for (int i2 = 0; i2 < length; i2++) {
                        SuggestionSpan suggestionSpan = suggestionSpans[i2];
                        suggestionSpansStarts[i2] = editable.getSpanStart(suggestionSpan);
                        suggestionSpansEnds[i2] = editable.getSpanEnd(suggestionSpan);
                        suggestionSpansFlags[i2] = editable.getSpanFlags(suggestionSpan);
                        suggestionSpanFlags = suggestionSpan.getFlags();
                        if ((suggestionSpanFlags & 2) > 0) {
                            suggestionSpan.setFlags((suggestionSpanFlags & -3) & -2);
                        }
                    }
                    spanUnionEnd = suggestionInfo.suggestionStart;
                    String suggestion = suggestionInfo.text.subSequence(spanUnionEnd, suggestionInfo.suggestionEnd).toString();
                    Editor.this.mTextView.replaceText_internal(spanUnionStart, spanEnd, suggestion);
                    suggestionInfo.suggestionSpan.notifySelection(Editor.this.mTextView.getContext(), originalText, suggestionInfo.suggestionIndex);
                    editable = suggestionInfo.suggestionSpan.getSuggestions();
                    editable[suggestionInfo.suggestionIndex] = originalText;
                    suggestionSpanFlags = suggestion.length() - (spanEnd - spanUnionStart);
                    int i3 = 0;
                    while (i3 < length) {
                        suggestions = editable;
                        if (suggestionSpansStarts[i3] > spanUnionStart || suggestionSpansEnds[i3] < spanEnd) {
                            suggestionInfo2 = suggestionInfo;
                            i = spanUnionStart;
                            suggestionStart = spanUnionEnd;
                            str = originalText;
                        } else {
                            suggestionInfo2 = suggestionInfo;
                            i = spanUnionStart;
                            suggestionStart = spanUnionEnd;
                            str = originalText;
                            Editor.this.mTextView.setSpan_internal(suggestionSpans[i3], suggestionSpansStarts[i3], suggestionSpansEnds[i3] + suggestionSpanFlags, suggestionSpansFlags[i3]);
                        }
                        i3++;
                        editable = suggestions;
                        suggestionInfo = suggestionInfo2;
                        spanUnionStart = i;
                        originalText = str;
                        spanUnionEnd = suggestionStart;
                    }
                    suggestions = editable;
                    suggestionInfo2 = suggestionInfo;
                    i = spanUnionStart;
                    suggestionStart = spanUnionEnd;
                    str = originalText;
                    editable = spanEnd + suggestionSpanFlags;
                    Editor.this.mTextView.setCursorPosition_internal(editable, editable);
                }
                hide();
                return;
            }
            hide();
        }
    }

    @interface TextActionMode {
        public static final int INSERTION = 1;
        public static final int SELECTION = 0;
        public static final int TEXT_LINK = 2;
    }

    private static class TextRenderNode {
        boolean isDirty = true;
        RenderNode renderNode;

        public TextRenderNode(String name) {
            this.renderNode = RenderNode.create(name, null);
        }

        /* Access modifiers changed, original: 0000 */
        public boolean needsRecord() {
            return this.isDirty || !this.renderNode.hasDisplayList();
        }
    }

    public static class UndoInputFilter implements InputFilter {
        private static final int MERGE_EDIT_MODE_FORCE_MERGE = 0;
        private static final int MERGE_EDIT_MODE_NEVER_MERGE = 1;
        private static final int MERGE_EDIT_MODE_NORMAL = 2;
        private final Editor mEditor;
        private boolean mExpanding;
        private boolean mHasComposition;
        private boolean mIsUserEdit;
        private boolean mPreviousOperationWasInSameBatchEdit;

        @Retention(RetentionPolicy.SOURCE)
        private @interface MergeMode {
        }

        public UndoInputFilter(Editor editor) {
            this.mEditor = editor;
        }

        public void saveInstanceState(Parcel parcel) {
            parcel.writeInt(this.mIsUserEdit);
            parcel.writeInt(this.mHasComposition);
            parcel.writeInt(this.mExpanding);
            parcel.writeInt(this.mPreviousOperationWasInSameBatchEdit);
        }

        public void restoreInstanceState(Parcel parcel) {
            boolean z = true;
            this.mIsUserEdit = parcel.readInt() != 0;
            this.mHasComposition = parcel.readInt() != 0;
            this.mExpanding = parcel.readInt() != 0;
            if (parcel.readInt() == 0) {
                z = false;
            }
            this.mPreviousOperationWasInSameBatchEdit = z;
        }

        public void beginBatchEdit() {
            this.mIsUserEdit = true;
        }

        public void endBatchEdit() {
            this.mIsUserEdit = false;
            this.mPreviousOperationWasInSameBatchEdit = false;
        }

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (!canUndoEdit(source, start, end, dest, dstart, dend)) {
                return null;
            }
            boolean shouldCreateSeparateState;
            boolean hadComposition = this.mHasComposition;
            this.mHasComposition = isComposition(source);
            boolean wasExpanding = this.mExpanding;
            if (end - start != dend - dstart) {
                this.mExpanding = end - start > dend - dstart;
                if (hadComposition && this.mExpanding != wasExpanding) {
                    shouldCreateSeparateState = true;
                    handleEdit(source, start, end, dest, dstart, dend, shouldCreateSeparateState);
                    return null;
                }
            }
            shouldCreateSeparateState = false;
            handleEdit(source, start, end, dest, dstart, dend, shouldCreateSeparateState);
            return null;
        }

        /* Access modifiers changed, original: 0000 */
        public void freezeLastEdit() {
            this.mEditor.mUndoManager.beginUpdate("Edit text");
            EditOperation lastEdit = getLastEdit();
            if (lastEdit != null) {
                lastEdit.mFrozen = true;
            }
            this.mEditor.mUndoManager.endUpdate();
        }

        private void handleEdit(CharSequence source, int start, int end, Spanned dest, int dstart, int dend, boolean shouldCreateSeparateState) {
            int mergeMode;
            if (isInTextWatcher() || this.mPreviousOperationWasInSameBatchEdit) {
                mergeMode = 0;
            } else if (shouldCreateSeparateState) {
                mergeMode = 1;
            } else {
                mergeMode = 2;
            }
            String newText = TextUtils.substring(source, start, end);
            EditOperation edit = new EditOperation(this.mEditor, TextUtils.substring(dest, dstart, dend), dstart, newText, this.mHasComposition);
            if (!this.mHasComposition || !TextUtils.equals(edit.mNewText, edit.mOldText)) {
                recordEdit(edit, mergeMode);
            }
        }

        private EditOperation getLastEdit() {
            return (EditOperation) this.mEditor.mUndoManager.getLastOperation(EditOperation.class, this.mEditor.mUndoOwner, 1);
        }

        private void recordEdit(EditOperation edit, int mergeMode) {
            UndoManager um = this.mEditor.mUndoManager;
            um.beginUpdate("Edit text");
            EditOperation lastEdit = getLastEdit();
            if (lastEdit == null) {
                um.addOperation(edit, 0);
            } else if (mergeMode == 0) {
                lastEdit.forceMergeWith(edit);
            } else if (!this.mIsUserEdit) {
                um.commitState(this.mEditor.mUndoOwner);
                um.addOperation(edit, 0);
            } else if (!(mergeMode == 2 && lastEdit.mergeWith(edit))) {
                um.commitState(this.mEditor.mUndoOwner);
                um.addOperation(edit, 0);
            }
            this.mPreviousOperationWasInSameBatchEdit = this.mIsUserEdit;
            um.endUpdate();
        }

        private boolean canUndoEdit(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (!this.mEditor.mAllowUndo || this.mEditor.mUndoManager.isInUndo() || !Editor.isValidRange(source, start, end) || !Editor.isValidRange(dest, dstart, dend)) {
                return false;
            }
            if (start == end && dstart == dend) {
                return false;
            }
            return true;
        }

        private static boolean isComposition(CharSequence source) {
            boolean z = false;
            if (!(source instanceof Spannable)) {
                return false;
            }
            Spannable text = (Spannable) source;
            if (BaseInputConnection.getComposingSpanStart(text) < BaseInputConnection.getComposingSpanEnd(text)) {
                z = true;
            }
            return z;
        }

        private boolean isInTextWatcher() {
            CharSequence text = this.mEditor.mTextView.getText();
            return (text instanceof SpannableStringBuilder) && ((SpannableStringBuilder) text).getTextWatcherDepth() > 0;
        }
    }

    Editor(TextView textView) {
        this.mTextView = textView;
        TextView textView2 = this.mTextView;
        textView2.setFilters(textView2.getFilters());
        this.mProcessTextIntentActionsHandler = new ProcessTextIntentActionsHandler(this, null);
        this.mMagnifierAnimator = new MagnifierMotionAnimator(Magnifier.createBuilderWithOldMagnifierDefaults(this.mTextView).build(), null);
    }

    /* Access modifiers changed, original: 0000 */
    public ParcelableParcel saveInstanceState() {
        ParcelableParcel state = new ParcelableParcel(getClass().getClassLoader());
        Parcel parcel = state.getParcel();
        this.mUndoManager.saveInstanceState(parcel);
        this.mUndoInputFilter.saveInstanceState(parcel);
        return state;
    }

    /* Access modifiers changed, original: 0000 */
    public void restoreInstanceState(ParcelableParcel state) {
        Parcel parcel = state.getParcel();
        this.mUndoManager.restoreInstanceState(parcel, state.getClassLoader());
        this.mUndoInputFilter.restoreInstanceState(parcel);
        this.mUndoOwner = this.mUndoManager.getOwner("Editor", this);
    }

    /* Access modifiers changed, original: 0000 */
    public void forgetUndoRedo() {
        UndoOwner[] owners = new UndoOwner[]{this.mUndoOwner};
        this.mUndoManager.forgetUndos(owners, -1);
        this.mUndoManager.forgetRedos(owners, -1);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean canUndo() {
        UndoOwner[] owners = new UndoOwner[]{this.mUndoOwner};
        if (!this.mAllowUndo || this.mUndoManager.countUndos(owners) <= 0) {
            return false;
        }
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean canRedo() {
        UndoOwner[] owners = new UndoOwner[]{this.mUndoOwner};
        if (!this.mAllowUndo || this.mUndoManager.countRedos(owners) <= 0) {
            return false;
        }
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    public void undo() {
        if (this.mAllowUndo) {
            this.mUndoManager.undo(new UndoOwner[]{this.mUndoOwner}, 1);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void redo() {
        if (this.mAllowUndo) {
            this.mUndoManager.redo(new UndoOwner[]{this.mUndoOwner}, 1);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void replace() {
        int middle = (this.mTextView.getSelectionStart() + this.mTextView.getSelectionEnd()) / 2;
        stopSelectionActionMode();
        Selection.setSelection((Spannable) this.mTextView.getText(), middle);
        showSuggestions();
    }

    /* Access modifiers changed, original: 0000 */
    public void onAttachedToWindow() {
        if (this.mShowErrorAfterAttach) {
            showError();
            this.mShowErrorAfterAttach = false;
        }
        ViewTreeObserver observer = this.mTextView.getViewTreeObserver();
        InsertionPointCursorController insertionPointCursorController = this.mInsertionPointCursorController;
        if (insertionPointCursorController != null) {
            observer.addOnTouchModeChangeListener(insertionPointCursorController);
        }
        SelectionModifierCursorController selectionModifierCursorController = this.mSelectionModifierCursorController;
        if (selectionModifierCursorController != null) {
            selectionModifierCursorController.resetTouchOffsets();
            observer.addOnTouchModeChangeListener(this.mSelectionModifierCursorController);
        }
        updateSpellCheckSpans(0, this.mTextView.getText().length(), true);
        if (this.mTextView.hasTransientState() && this.mTextView.getSelectionStart() != this.mTextView.getSelectionEnd()) {
            this.mTextView.setHasTransientState(false);
            startSelectionActionMode();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void onDetachedFromWindow() {
        if (this.mError != null) {
            hideError();
        }
        Blink blink = this.mBlink;
        if (blink != null) {
            this.mTextView.removeCallbacks(blink);
        }
        InsertionPointCursorController insertionPointCursorController = this.mInsertionPointCursorController;
        if (insertionPointCursorController != null) {
            insertionPointCursorController.onDetached();
        }
        SelectionModifierCursorController selectionModifierCursorController = this.mSelectionModifierCursorController;
        if (selectionModifierCursorController != null) {
            selectionModifierCursorController.onDetached();
        }
        Runnable runnable = this.mShowSuggestionRunnable;
        if (runnable != null) {
            this.mTextView.removeCallbacks(runnable);
        }
        discardTextDisplayLists();
        SpellChecker spellChecker = this.mSpellChecker;
        if (spellChecker != null) {
            spellChecker.closeSession();
            this.mSpellChecker = null;
        }
        hideCursorAndSpanControllers();
        stopTextActionModeWithPreservingSelection();
    }

    private void discardTextDisplayLists() {
        if (this.mTextRenderNodes != null) {
            int i = 0;
            while (true) {
                TextRenderNode[] textRenderNodeArr = this.mTextRenderNodes;
                if (i < textRenderNodeArr.length) {
                    RenderNode displayList = textRenderNodeArr[i] != null ? textRenderNodeArr[i].renderNode : null;
                    if (displayList != null && displayList.hasDisplayList()) {
                        displayList.discardDisplayList();
                    }
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    private void showError() {
        if (this.mTextView.getWindowToken() == null) {
            this.mShowErrorAfterAttach = true;
            return;
        }
        if (this.mErrorPopup == null) {
            TextView err = (TextView) LayoutInflater.from(this.mTextView.getContext()).inflate((int) com.android.internal.R.layout.textview_hint, null);
            float scale = this.mTextView.getResources().getDisplayMetrics().density;
            this.mErrorPopup = new ErrorPopup(err, (int) ((200.0f * scale) + 0.5f), (int) ((50.0f * scale) + 0.5f));
            this.mErrorPopup.setFocusable(false);
            this.mErrorPopup.setInputMethodMode(1);
        }
        TextView tv = (TextView) this.mErrorPopup.getContentView();
        chooseSize(this.mErrorPopup, this.mError, tv);
        tv.setText(this.mError);
        this.mErrorPopup.showAsDropDown(this.mTextView, getErrorX(), getErrorY());
        ErrorPopup errorPopup = this.mErrorPopup;
        errorPopup.fixDirection(errorPopup.isAboveAnchor());
    }

    public void setError(CharSequence error, Drawable icon) {
        this.mError = TextUtils.stringOrSpannedString(error);
        this.mErrorWasChanged = true;
        if (this.mError == null) {
            setErrorIcon(null);
            ErrorPopup errorPopup = this.mErrorPopup;
            if (errorPopup != null) {
                if (errorPopup.isShowing()) {
                    this.mErrorPopup.dismiss();
                }
                this.mErrorPopup = null;
                return;
            }
            return;
        }
        setErrorIcon(icon);
        if (this.mTextView.isFocused()) {
            showError();
        }
    }

    private void setErrorIcon(Drawable icon) {
        Drawables dr = this.mTextView.mDrawables;
        if (dr == null) {
            TextView textView = this.mTextView;
            Drawables drawables = new Drawables(textView.getContext());
            dr = drawables;
            textView.mDrawables = drawables;
        }
        dr.setErrorDrawable(icon, this.mTextView);
        this.mTextView.resetResolvedDrawables();
        this.mTextView.invalidate();
        this.mTextView.requestLayout();
    }

    private void hideError() {
        ErrorPopup errorPopup = this.mErrorPopup;
        if (errorPopup != null && errorPopup.isShowing()) {
            this.mErrorPopup.dismiss();
        }
        this.mShowErrorAfterAttach = false;
    }

    private int getErrorX() {
        float scale = this.mTextView.getResources().getDisplayMetrics().density;
        Drawables dr = this.mTextView.mDrawables;
        int i = 0;
        if (this.mTextView.getLayoutDirection() != 1) {
            if (dr != null) {
                i = dr.mDrawableSizeRight;
            }
            return ((this.mTextView.getWidth() - this.mErrorPopup.getWidth()) - this.mTextView.getPaddingRight()) + (((-i) / 2) + ((int) ((25.0f * scale) + 0.5f)));
        }
        if (dr != null) {
            i = dr.mDrawableSizeLeft;
        }
        return this.mTextView.getPaddingLeft() + ((i / 2) - ((int) ((25.0f * scale) + 0.5f)));
    }

    private int getErrorY() {
        int compoundPaddingTop = this.mTextView.getCompoundPaddingTop();
        int vspace = ((this.mTextView.getBottom() - this.mTextView.getTop()) - this.mTextView.getCompoundPaddingBottom()) - compoundPaddingTop;
        Drawables dr = this.mTextView.mDrawables;
        int height = 0;
        if (this.mTextView.getLayoutDirection() != 1) {
            if (dr != null) {
                height = dr.mDrawableHeightRight;
            }
        } else if (dr != null) {
            height = dr.mDrawableHeightLeft;
        }
        return (((((vspace - height) / 2) + compoundPaddingTop) + height) - this.mTextView.getHeight()) - ((int) ((2.0f * this.mTextView.getResources().getDisplayMetrics().density) + 0.5f));
    }

    /* Access modifiers changed, original: 0000 */
    public void createInputContentTypeIfNeeded() {
        if (this.mInputContentType == null) {
            this.mInputContentType = new InputContentType();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void createInputMethodStateIfNeeded() {
        if (this.mInputMethodState == null) {
            this.mInputMethodState = new InputMethodState();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean shouldRenderCursor() {
        boolean z = false;
        if (!isCursorVisible()) {
            return false;
        }
        if (this.mRenderCursorRegardlessTiming) {
            return true;
        }
        if ((SystemClock.uptimeMillis() - this.mShowCursor) % 1000 < 500) {
            z = true;
        }
        return z;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isCursorVisible() {
        return this.mCursorVisible && this.mTextView.isTextEditable();
    }

    /* Access modifiers changed, original: 0000 */
    public void prepareCursorControllers() {
        boolean z;
        boolean windowSupportsHandles = false;
        LayoutParams params = this.mTextView.getRootView().getLayoutParams();
        boolean z2 = true;
        if (params instanceof WindowManager.LayoutParams) {
            WindowManager.LayoutParams windowParams = (WindowManager.LayoutParams) params;
            z = windowParams.type < 1000 || windowParams.type > 1999;
            windowSupportsHandles = z;
        }
        boolean enabled = windowSupportsHandles && this.mTextView.getLayout() != null;
        z = enabled && isCursorVisible();
        this.mInsertionControllerEnabled = z;
        if (!(enabled && this.mTextView.textCanBeSelected())) {
            z2 = false;
        }
        this.mSelectionControllerEnabled = z2;
        if (!this.mInsertionControllerEnabled) {
            hideInsertionPointCursorController();
            InsertionPointCursorController insertionPointCursorController = this.mInsertionPointCursorController;
            if (insertionPointCursorController != null) {
                insertionPointCursorController.onDetached();
                this.mInsertionPointCursorController = null;
            }
        }
        if (!this.mSelectionControllerEnabled) {
            stopSelectionActionMode();
            SelectionModifierCursorController selectionModifierCursorController = this.mSelectionModifierCursorController;
            if (selectionModifierCursorController != null) {
                selectionModifierCursorController.onDetached();
                this.mSelectionModifierCursorController = null;
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void hideInsertionPointCursorController() {
        InsertionPointCursorController insertionPointCursorController = this.mInsertionPointCursorController;
        if (insertionPointCursorController != null) {
            insertionPointCursorController.hide();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void hideCursorAndSpanControllers() {
        hideCursorControllers();
        hideSpanControllers();
    }

    private void hideSpanControllers() {
        SpanController spanController = this.mSpanController;
        if (spanController != null) {
            spanController.hide();
        }
    }

    private void hideCursorControllers() {
        SuggestionsPopupWindow suggestionsPopupWindow = this.mSuggestionsPopupWindow;
        if (!(suggestionsPopupWindow == null || suggestionsPopupWindow.isShowingUp())) {
            this.mSuggestionsPopupWindow.hide();
        }
        hideInsertionPointCursorController();
        stopSelectionActionMode();
    }

    private void updateSpellCheckSpans(int start, int end, boolean createSpellChecker) {
        this.mTextView.removeAdjacentSuggestionSpans(start);
        this.mTextView.removeAdjacentSuggestionSpans(end);
        if (this.mTextView.isTextEditable() && this.mTextView.isSuggestionsEnabled() && !this.mTextView.isInExtractedMode()) {
            if (this.mSpellChecker == null && createSpellChecker) {
                this.mSpellChecker = new SpellChecker(this.mTextView);
            }
            SpellChecker spellChecker = this.mSpellChecker;
            if (spellChecker != null) {
                spellChecker.spellCheck(start, end);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void onScreenStateChanged(int screenState) {
        if (screenState == 0) {
            suspendBlink();
        } else if (screenState == 1) {
            resumeBlink();
        }
    }

    private void suspendBlink() {
        Blink blink = this.mBlink;
        if (blink != null) {
            blink.cancel();
        }
    }

    private void resumeBlink() {
        Blink blink = this.mBlink;
        if (blink != null) {
            blink.uncancel();
            makeBlink();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void adjustInputType(boolean password, boolean passwordInputType, boolean webPasswordInputType, boolean numberPasswordInputType) {
        int i = this.mInputType;
        if ((i & 15) == 1) {
            if (password || passwordInputType) {
                this.mInputType = (this.mInputType & -4081) | 128;
            }
            if (webPasswordInputType) {
                this.mInputType = (this.mInputType & -4081) | 224;
            }
        } else if ((i & 15) == 2 && numberPasswordInputType) {
            this.mInputType = (i & -4081) | 16;
        }
    }

    private void chooseSize(PopupWindow pop, CharSequence text, TextView tv) {
        int wid = tv.getPaddingLeft() + tv.getPaddingRight();
        int ht = tv.getPaddingTop() + tv.getPaddingBottom();
        Layout staticLayout = new StaticLayout(text, tv.getPaint(), this.mTextView.getResources().getDimensionPixelSize(com.android.internal.R.dimen.textview_error_popup_default_width), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
        float max = 0.0f;
        for (int i = 0; i < staticLayout.getLineCount(); i++) {
            max = Math.max(max, staticLayout.getLineWidth(i));
        }
        pop.setWidth(((int) Math.ceil((double) max)) + wid);
        pop.setHeight(staticLayout.getHeight() + ht);
    }

    /* Access modifiers changed, original: 0000 */
    public void setFrame() {
        ErrorPopup errorPopup = this.mErrorPopup;
        if (errorPopup != null) {
            chooseSize(this.mErrorPopup, this.mError, (TextView) errorPopup.getContentView());
            this.mErrorPopup.update((View) this.mTextView, getErrorX(), getErrorY(), this.mErrorPopup.getWidth(), this.mErrorPopup.getHeight());
        }
    }

    private boolean canSelectText() {
        return hasSelectionController() && this.mTextView.getText().length() != 0;
    }

    private boolean hasPasswordTransformationMethod() {
        return this.mTextView.getTransformationMethod() instanceof PasswordTransformationMethod;
    }

    private int getWordStart(int offset) {
        int retOffset;
        if (getWordIteratorWithText().isOnPunctuation(getWordIteratorWithText().prevBoundary(offset))) {
            retOffset = getWordIteratorWithText().getPunctuationBeginning(offset);
        } else {
            retOffset = getWordIteratorWithText().getPrevWordBeginningOnTwoWordsBoundary(offset);
        }
        if (retOffset == -1) {
            return offset;
        }
        return retOffset;
    }

    private int getWordEnd(int offset) {
        int retOffset;
        if (getWordIteratorWithText().isAfterPunctuation(getWordIteratorWithText().nextBoundary(offset))) {
            retOffset = getWordIteratorWithText().getPunctuationEnd(offset);
        } else {
            retOffset = getWordIteratorWithText().getNextWordEndOnTwoWordBoundary(offset);
        }
        if (retOffset == -1) {
            return offset;
        }
        return retOffset;
    }

    private boolean needsToSelectAllToSelectWordOrParagraph() {
        if (this.mTextView.hasPasswordTransformationMethod()) {
            return true;
        }
        int inputType = this.mTextView.getInputType();
        int klass = inputType & 15;
        int variation = inputType & InputType.TYPE_MASK_VARIATION;
        if (klass == 2 || klass == 3 || klass == 4 || variation == 16 || variation == 32 || variation == 208 || variation == 176) {
            return true;
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Missing block: B:27:0x00ab, code skipped:
            return false;
     */
    public boolean selectCurrentWord() {
        /*
        r12 = this;
        r0 = r12.canSelectText();
        r1 = 0;
        if (r0 != 0) goto L_0x0008;
    L_0x0007:
        return r1;
    L_0x0008:
        r0 = r12.needsToSelectAllToSelectWordOrParagraph();
        if (r0 == 0) goto L_0x0015;
    L_0x000e:
        r0 = r12.mTextView;
        r0 = r0.selectAllText();
        return r0;
    L_0x0015:
        r2 = r12.getLastTouchOffsets();
        r0 = android.text.TextUtils.unpackRangeStartFromLong(r2);
        r4 = android.text.TextUtils.unpackRangeEndFromLong(r2);
        if (r0 < 0) goto L_0x00ab;
    L_0x0023:
        r5 = r12.mTextView;
        r5 = r5.getText();
        r5 = r5.length();
        if (r0 <= r5) goto L_0x0031;
    L_0x002f:
        goto L_0x00ab;
    L_0x0031:
        if (r4 < 0) goto L_0x00aa;
    L_0x0033:
        r5 = r12.mTextView;
        r5 = r5.getText();
        r5 = r5.length();
        if (r4 <= r5) goto L_0x0040;
    L_0x003f:
        goto L_0x00aa;
    L_0x0040:
        r5 = r12.mTextView;
        r5 = r5.getText();
        r5 = (android.text.Spanned) r5;
        r6 = android.text.style.URLSpan.class;
        r5 = r5.getSpans(r0, r4, r6);
        r5 = (android.text.style.URLSpan[]) r5;
        r6 = r5.length;
        r7 = 1;
        if (r6 < r7) goto L_0x0070;
    L_0x0054:
        r6 = r5[r1];
        r8 = r12.mTextView;
        r8 = r8.getText();
        r8 = (android.text.Spanned) r8;
        r8 = r8.getSpanStart(r6);
        r9 = r12.mTextView;
        r9 = r9.getText();
        r9 = (android.text.Spanned) r9;
        r6 = r9.getSpanEnd(r6);
        r9 = r6;
        goto L_0x0098;
    L_0x0070:
        r6 = r12.getWordIterator();
        r8 = r12.mTextView;
        r8 = r8.getText();
        r6.setCharSequence(r8, r0, r4);
        r8 = r6.getBeginning(r0);
        r9 = r6.getEnd(r4);
        r10 = -1;
        if (r8 == r10) goto L_0x008c;
    L_0x0088:
        if (r9 == r10) goto L_0x008c;
    L_0x008a:
        if (r8 != r9) goto L_0x0098;
    L_0x008c:
        r10 = r12.getCharClusterRange(r0);
        r8 = android.text.TextUtils.unpackRangeStartFromLong(r10);
        r9 = android.text.TextUtils.unpackRangeEndFromLong(r10);
    L_0x0098:
        r12.setSelectionTranslation(r8, r9);
        r6 = r12.mTextView;
        r6 = r6.getText();
        r6 = (android.text.Spannable) r6;
        android.text.Selection.setSelection(r6, r8, r9);
        if (r9 <= r8) goto L_0x00a9;
    L_0x00a8:
        r1 = r7;
    L_0x00a9:
        return r1;
    L_0x00aa:
        return r1;
    L_0x00ab:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.Editor.selectCurrentWord():boolean");
    }

    private void setSelectionTranslation(int selectionStart, int selectionEnd) {
        if (hasSelectionController()) {
            float[] translationArray = new float[]{0.0f, 0.0f};
            Layout layout = this.mTextView.getLayout();
            if (layout != null && selectionEnd > selectionStart) {
                float positionStart = layout.getPrimaryHorizontal(selectionStart);
                float positionEnd = layout.getPrimaryHorizontal(selectionEnd);
                int selectionOld = Selection.getSelectionStart(this.mTextView.getText());
                if (selectionOld < selectionStart || selectionOld > selectionEnd) {
                    translationArray[0] = (positionEnd - positionStart) / 2.0f;
                    translationArray[1] = (positionStart - positionEnd) / 2.0f;
                } else {
                    float positionOld = layout.getPrimaryHorizontal(selectionOld);
                    if (selectionOld == selectionStart) {
                        translationArray[1] = positionOld - positionEnd;
                    } else if (selectionOld == selectionEnd) {
                        translationArray[0] = positionOld - positionStart;
                    } else {
                        translationArray[0] = positionOld - positionStart;
                        translationArray[1] = positionOld - positionEnd;
                    }
                }
            }
            getSelectionController().setTranslationCache(translationArray);
        }
    }

    private boolean selectCurrentParagraph() {
        if (!this.mTextView.canSelectText()) {
            return false;
        }
        if (needsToSelectAllToSelectWordOrParagraph()) {
            return this.mTextView.selectAllText();
        }
        long lastTouchOffsets = getLastTouchOffsets();
        long paragraphsRange = getParagraphsRange(TextUtils.unpackRangeStartFromLong(lastTouchOffsets), TextUtils.unpackRangeEndFromLong(lastTouchOffsets));
        int start = TextUtils.unpackRangeStartFromLong(paragraphsRange);
        int end = TextUtils.unpackRangeEndFromLong(paragraphsRange);
        if (start >= end) {
            return false;
        }
        Selection.setSelection((Spannable) this.mTextView.getText(), start, end);
        return true;
    }

    private long getParagraphsRange(int startOffset, int endOffset) {
        Layout layout = this.mTextView.getLayout();
        if (layout == null) {
            return TextUtils.packRangeInLong(-1, -1);
        }
        CharSequence text = this.mTextView.getText();
        int minLine = layout.getLineForOffset(startOffset);
        while (minLine > 0 && text.charAt(layout.getLineEnd(minLine - 1) - 1) != 10) {
            minLine--;
        }
        int maxLine = layout.getLineForOffset(endOffset);
        while (maxLine < layout.getLineCount() - 1 && text.charAt(layout.getLineEnd(maxLine) - 1) != 10) {
            maxLine++;
        }
        return TextUtils.packRangeInLong(layout.getLineStart(minLine), layout.getLineEnd(maxLine));
    }

    /* Access modifiers changed, original: 0000 */
    public void onLocaleChanged() {
        this.mWordIterator = null;
    }

    public WordIterator getWordIterator() {
        if (this.mWordIterator == null) {
            this.mWordIterator = new WordIterator(this.mTextView.getTextServicesLocale());
        }
        return this.mWordIterator;
    }

    private WordIterator getWordIteratorWithText() {
        if (this.mWordIteratorWithText == null) {
            this.mWordIteratorWithText = new WordIterator(this.mTextView.getTextServicesLocale());
            this.mUpdateWordIteratorText = true;
        }
        if (this.mUpdateWordIteratorText) {
            CharSequence text = this.mTextView.getText();
            this.mWordIteratorWithText.setCharSequence(text, 0, text.length());
            this.mUpdateWordIteratorText = false;
        }
        return this.mWordIteratorWithText;
    }

    private long getCharRange(int offset) {
        int textLength = this.mTextView.getText().length();
        if (offset + 1 < textLength && Character.isSurrogatePair(this.mTextView.getText().charAt(offset), this.mTextView.getText().charAt(offset + 1))) {
            return TextUtils.packRangeInLong(offset, offset + 2);
        }
        if (offset < textLength) {
            return TextUtils.packRangeInLong(offset, offset + 1);
        }
        if (offset - 2 >= 0) {
            if (Character.isSurrogatePair(this.mTextView.getText().charAt(offset - 2), this.mTextView.getText().charAt(offset - 1))) {
                return TextUtils.packRangeInLong(offset - 2, offset);
            }
        }
        if (offset - 1 >= 0) {
            return TextUtils.packRangeInLong(offset - 1, offset);
        }
        return TextUtils.packRangeInLong(offset, offset);
    }

    private int getNextCursorOffset(int offset, boolean findAfterGivenOffset) {
        Layout layout = this.mTextView.getLayout();
        if (layout == null) {
            return offset;
        }
        return findAfterGivenOffset == layout.isRtlCharAt(offset) ? layout.getOffsetToLeftOf(offset) : layout.getOffsetToRightOf(offset);
    }

    private long getCharClusterRange(int offset) {
        if (offset < this.mTextView.getText().length()) {
            int clusterEndOffset = getNextCursorOffset(offset, true);
            return TextUtils.packRangeInLong(getNextCursorOffset(clusterEndOffset, false), clusterEndOffset);
        } else if (offset - 1 < 0) {
            return TextUtils.packRangeInLong(offset, offset);
        } else {
            int clusterStartOffset = getNextCursorOffset(offset, false);
            return TextUtils.packRangeInLong(clusterStartOffset, getNextCursorOffset(clusterStartOffset, true));
        }
    }

    private boolean touchPositionIsInSelection() {
        int selectionStart = this.mTextView.getSelectionStart();
        int selectionEnd = this.mTextView.getSelectionEnd();
        boolean z = false;
        if (selectionStart == selectionEnd) {
            return false;
        }
        if (selectionStart > selectionEnd) {
            int tmp = selectionStart;
            selectionStart = selectionEnd;
            selectionEnd = tmp;
            Selection.setSelection((Spannable) this.mTextView.getText(), selectionStart, selectionEnd);
        }
        SelectionModifierCursorController selectionController = getSelectionController();
        int minOffset = selectionController.getMinTouchOffset();
        int maxOffset = selectionController.getMaxTouchOffset();
        if (minOffset >= selectionStart && maxOffset < selectionEnd) {
            z = true;
        }
        return z;
    }

    private PositionListener getPositionListener() {
        if (this.mPositionListener == null) {
            this.mPositionListener = new PositionListener(this, null);
        }
        return this.mPositionListener;
    }

    /* JADX WARNING: Missing block: B:26:0x0082, code skipped:
            return false;
     */
    private boolean isPositionVisible(int r8, int r9) {
        /*
        r7 = this;
        r0 = TEMP_POSITION;
        monitor-enter(r0);
        r1 = TEMP_POSITION;	 Catch:{ all -> 0x0085 }
        r2 = (float) r8;	 Catch:{ all -> 0x0085 }
        r3 = 0;
        r1[r3] = r2;	 Catch:{ all -> 0x0085 }
        r2 = (float) r9;	 Catch:{ all -> 0x0085 }
        r4 = 1;
        r1[r4] = r2;	 Catch:{ all -> 0x0085 }
        r2 = r7.mTextView;	 Catch:{ all -> 0x0085 }
    L_0x000f:
        if (r2 == 0) goto L_0x0083;
    L_0x0011:
        r5 = r7.mTextView;	 Catch:{ all -> 0x0085 }
        if (r2 == r5) goto L_0x0029;
    L_0x0015:
        r5 = r1[r3];	 Catch:{ all -> 0x0085 }
        r6 = r2.getScrollX();	 Catch:{ all -> 0x0085 }
        r6 = (float) r6;	 Catch:{ all -> 0x0085 }
        r5 = r5 - r6;
        r1[r3] = r5;	 Catch:{ all -> 0x0085 }
        r5 = r1[r4];	 Catch:{ all -> 0x0085 }
        r6 = r2.getScrollY();	 Catch:{ all -> 0x0085 }
        r6 = (float) r6;	 Catch:{ all -> 0x0085 }
        r5 = r5 - r6;
        r1[r4] = r5;	 Catch:{ all -> 0x0085 }
    L_0x0029:
        r5 = r1[r3];	 Catch:{ all -> 0x0085 }
        r6 = 0;
        r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1));
        if (r5 < 0) goto L_0x0081;
    L_0x0030:
        r5 = r1[r4];	 Catch:{ all -> 0x0085 }
        r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1));
        if (r5 < 0) goto L_0x0081;
    L_0x0036:
        r5 = r1[r3];	 Catch:{ all -> 0x0085 }
        r6 = r2.getWidth();	 Catch:{ all -> 0x0085 }
        r6 = (float) r6;	 Catch:{ all -> 0x0085 }
        r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1));
        if (r5 > 0) goto L_0x0081;
    L_0x0041:
        r5 = r1[r4];	 Catch:{ all -> 0x0085 }
        r6 = r2.getHeight();	 Catch:{ all -> 0x0085 }
        r6 = (float) r6;	 Catch:{ all -> 0x0085 }
        r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1));
        if (r5 <= 0) goto L_0x004d;
    L_0x004c:
        goto L_0x0081;
    L_0x004d:
        r5 = r2.getMatrix();	 Catch:{ all -> 0x0085 }
        r5 = r5.isIdentity();	 Catch:{ all -> 0x0085 }
        if (r5 != 0) goto L_0x005e;
    L_0x0057:
        r5 = r2.getMatrix();	 Catch:{ all -> 0x0085 }
        r5.mapPoints(r1);	 Catch:{ all -> 0x0085 }
    L_0x005e:
        r5 = r1[r3];	 Catch:{ all -> 0x0085 }
        r6 = r2.getLeft();	 Catch:{ all -> 0x0085 }
        r6 = (float) r6;	 Catch:{ all -> 0x0085 }
        r5 = r5 + r6;
        r1[r3] = r5;	 Catch:{ all -> 0x0085 }
        r5 = r1[r4];	 Catch:{ all -> 0x0085 }
        r6 = r2.getTop();	 Catch:{ all -> 0x0085 }
        r6 = (float) r6;	 Catch:{ all -> 0x0085 }
        r5 = r5 + r6;
        r1[r4] = r5;	 Catch:{ all -> 0x0085 }
        r5 = r2.getParent();	 Catch:{ all -> 0x0085 }
        r6 = r5 instanceof android.view.View;	 Catch:{ all -> 0x0085 }
        if (r6 == 0) goto L_0x007f;
    L_0x007a:
        r6 = r5;
        r6 = (android.view.View) r6;	 Catch:{ all -> 0x0085 }
        r2 = r6;
        goto L_0x0080;
    L_0x007f:
        r2 = 0;
    L_0x0080:
        goto L_0x000f;
    L_0x0081:
        monitor-exit(r0);	 Catch:{ all -> 0x0085 }
        return r3;
    L_0x0083:
        monitor-exit(r0);	 Catch:{ all -> 0x0085 }
        return r4;
    L_0x0085:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0085 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.Editor.isPositionVisible(int, int):boolean");
    }

    private boolean isOffsetVisible(int offset) {
        Layout layout = this.mTextView.getLayout();
        if (layout == null) {
            return false;
        }
        int line = layout.getLineForOffset(offset);
        int lineBottom = layout.getLineBottom(line);
        int lineTop = layout.getLineTop(line);
        int primaryHorizontal = (int) layout.getPrimaryHorizontal(offset);
        TextView textView = this.mTextView;
        return textView.isPositionVisible((float) (textView.viewportToContentHorizontalOffset() + primaryHorizontal), (float) (this.mTextView.viewportToContentVerticalOffset() + lineBottom));
    }

    private boolean isPositionOnText(float x, float y) {
        Layout layout = this.mTextView.getLayout();
        if (layout == null) {
            return false;
        }
        int line = this.mTextView.getLineAtCoordinate(y);
        x = this.mTextView.convertToLocalHorizontalCoordinate(x);
        if (x >= layout.getLineLeft(line) && x <= layout.getLineRight(line)) {
            return true;
        }
        return false;
    }

    private void startDragAndDrop() {
        if (!this.mTextView.isInExtractedMode()) {
            int start = this.mTextView.getSelectionStart();
            int end = this.mTextView.getSelectionEnd();
            this.mTextView.startDragAndDrop(ClipData.newPlainText(null, this.mTextView.getTransformedText(start, end)), getTextThumbnailBuilder(start, end), new DragLocalState(this.mTextView, start, end), 256);
            stopTextActionMode();
            if (hasSelectionController()) {
                getSelectionController().resetTouchOffsets();
            }
        }
    }

    public boolean performLongClick(boolean handled) {
        if (!(handled || isPositionOnText(this.mLastDownPositionX, this.mLastDownPositionY) || !this.mInsertionControllerEnabled)) {
            Selection.setSelection((Spannable) this.mTextView.getText(), this.mTextView.getOffsetForPosition(this.mLastDownPositionX, this.mLastDownPositionY));
            getInsertionController().showWithActionPopup();
            this.mIsInsertionActionModeStartPending = true;
            handled = true;
            MetricsLogger.action(this.mTextView.getContext(), (int) MetricsEvent.TEXT_LONGPRESS, 0);
        }
        if (!handled && this.mTextView.hasSelection()) {
            if (touchPositionIsInSelection()) {
                startDragAndDrop();
                MetricsLogger.action(this.mTextView.getContext(), (int) MetricsEvent.TEXT_LONGPRESS, 2);
            } else {
                getSelectionController().hide();
                selectCurrentWord();
                getSelectionController().show();
                MetricsLogger.action(this.mTextView.getContext(), (int) MetricsEvent.TEXT_LONGPRESS, 1);
            }
            handled = true;
        }
        if (!handled) {
            handled = selectCurrentWordAndStartDrag();
            if (handled) {
                MetricsLogger.action(this.mTextView.getContext(), (int) MetricsEvent.TEXT_LONGPRESS, 1);
            }
        }
        return handled;
    }

    /* Access modifiers changed, original: 0000 */
    public float getLastUpPositionX() {
        return this.mLastUpPositionX;
    }

    /* Access modifiers changed, original: 0000 */
    public float getLastUpPositionY() {
        return this.mLastUpPositionY;
    }

    private long getLastTouchOffsets() {
        SelectionModifierCursorController selectionController = getSelectionController();
        return TextUtils.packRangeInLong(selectionController.getMinTouchOffset(), selectionController.getMaxTouchOffset());
    }

    /* Access modifiers changed, original: 0000 */
    public void onFocusChanged(boolean focused, int direction) {
        this.mShowCursor = SystemClock.uptimeMillis();
        ensureEndedBatchEdit();
        if (focused) {
            int selStart = this.mTextView.getSelectionStart();
            int selEnd = this.mTextView.getSelectionEnd();
            boolean isFocusHighlighted = this.mSelectAllOnFocus && selStart == 0 && selEnd == this.mTextView.getText().length();
            boolean z = this.mFrozenWithFocus && this.mTextView.hasSelection() && !isFocusHighlighted;
            this.mCreatedWithASelection = z;
            if (!this.mFrozenWithFocus || selStart < 0 || selEnd < 0) {
                int lastTapPosition = getLastTapPosition();
                if (lastTapPosition >= 0) {
                    Selection.setSelection((Spannable) this.mTextView.getText(), lastTapPosition);
                }
                MovementMethod mMovement = this.mTextView.getMovementMethod();
                if (mMovement != null) {
                    TextView textView = this.mTextView;
                    mMovement.onTakeFocus(textView, (Spannable) textView.getText(), direction);
                }
                if ((this.mTextView.isInExtractedMode() || this.mSelectionMoved) && selStart >= 0 && selEnd >= 0) {
                    Selection.setSelection((Spannable) this.mTextView.getText(), selStart, selEnd);
                }
                if (this.mSelectAllOnFocus) {
                    this.mTextView.selectAllText();
                }
                this.mTouchFocusSelected = true;
            }
            this.mFrozenWithFocus = false;
            this.mSelectionMoved = false;
            if (this.mError != null) {
                showError();
            }
            makeBlink();
            return;
        }
        if (this.mError != null) {
            hideError();
        }
        this.mTextView.onEndBatchEdit();
        if (this.mTextView.isInExtractedMode()) {
            hideCursorAndSpanControllers();
            stopTextActionModeWithPreservingSelection();
        } else {
            hideCursorAndSpanControllers();
            if (this.mTextView.isTemporarilyDetached()) {
                stopTextActionModeWithPreservingSelection();
            } else {
                stopTextActionMode();
            }
            downgradeEasyCorrectionSpans();
        }
        SelectionModifierCursorController selectionModifierCursorController = this.mSelectionModifierCursorController;
        if (selectionModifierCursorController != null) {
            selectionModifierCursorController.resetTouchOffsets();
        }
        this.mFirstTouchUp = true;
    }

    private void downgradeEasyCorrectionSpans() {
        CharSequence text = this.mTextView.getText();
        if (text instanceof Spannable) {
            Spannable spannable = (Spannable) text;
            SuggestionSpan[] suggestionSpans = (SuggestionSpan[]) spannable.getSpans(0, spannable.length(), SuggestionSpan.class);
            for (int i = 0; i < suggestionSpans.length; i++) {
                int flags = suggestionSpans[i].getFlags();
                if ((flags & 1) != 0 && (flags & 2) == 0) {
                    suggestionSpans[i].setFlags(flags & -2);
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void sendOnTextChanged(int start, int before, int after) {
        getSelectionActionModeHelper().onTextChanged(start, start + before);
        updateSpellCheckSpans(start, start + after, false);
        this.mUpdateWordIteratorText = true;
        hideCursorControllers();
        SelectionModifierCursorController selectionModifierCursorController = this.mSelectionModifierCursorController;
        if (selectionModifierCursorController != null) {
            selectionModifierCursorController.resetTouchOffsets();
        }
        stopTextActionMode();
    }

    private void handleEmailPopup(int endPos) {
        if (this.mTextView.isTextEditable()) {
            CharSequence text = this.mTextView.getText();
            if (!TextUtils.isEmpty(text)) {
                EmailInfo email = TextPatternUtil.findEmailAtPos(text.toString(), endPos);
                if (email != null) {
                    showEmailPopupWindow(email);
                    return;
                }
            }
            hideEmailPopupWindow();
            return;
        }
        hideEmailPopupWindow();
    }

    /* Access modifiers changed, original: 0000 */
    public EmailAddPopupWindow getEmailPopupWindow() {
        if (this.mEmailPopupWindow == null) {
            SelectionModifierCursorController cursorController = getSelectionController();
            cursorController.initDrawables();
            cursorController.initHandleView();
            this.mEmailPopupWindow = new EmailAddPopupWindow(cursorController.mStartHandle);
        }
        return this.mEmailPopupWindow;
    }

    /* Access modifiers changed, original: 0000 */
    public void showEmailPopupWindow(EmailInfo email) {
        this.mEmailPopupWindow = getEmailPopupWindow();
        this.mEmailPopupWindow.setEmail(email);
        if (this.mEmailPopupShower == null) {
            this.mEmailPopupShower = new Runnable() {
                public void run() {
                    Editor.this.mEmailPopupWindow.show();
                }
            };
        }
        this.mTextView.removeCallbacks(this.mEmailPopupShower);
        this.mTextView.post(this.mEmailPopupShower);
    }

    /* Access modifiers changed, original: protected */
    public void hideEmailPopupWindow() {
        Runnable runnable = this.mEmailPopupShower;
        if (runnable != null) {
            this.mTextView.removeCallbacks(runnable);
        }
        EmailAddPopupWindow emailAddPopupWindow = this.mEmailPopupWindow;
        if (emailAddPopupWindow != null) {
            emailAddPopupWindow.hide();
        }
    }

    private int getLastTapPosition() {
        int lastTapPosition = this.mSelectionModifierCursorController;
        if (lastTapPosition != 0) {
            lastTapPosition = lastTapPosition.getMinTouchOffset();
            if (lastTapPosition >= 0) {
                if (lastTapPosition > this.mTextView.getText().length()) {
                    lastTapPosition = this.mTextView.getText().length();
                }
                return lastTapPosition;
            }
        }
        return -1;
    }

    /* Access modifiers changed, original: 0000 */
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        Blink blink;
        if (hasWindowFocus) {
            blink = this.mBlink;
            if (blink != null) {
                blink.uncancel();
                makeBlink();
                return;
            }
            return;
        }
        blink = this.mBlink;
        if (blink != null) {
            blink.cancel();
        }
        InputContentType inputContentType = this.mInputContentType;
        if (inputContentType != null) {
            inputContentType.enterDown = false;
        }
        hideCursorAndSpanControllers();
        stopTextActionModeWithPreservingSelection();
        SuggestionsPopupWindow suggestionsPopupWindow = this.mSuggestionsPopupWindow;
        if (suggestionsPopupWindow != null) {
            suggestionsPopupWindow.onParentLostFocus();
        }
        ensureEndedBatchEdit();
    }

    private void updateTapState(MotionEvent event) {
        int action = event.getActionMasked();
        if (action == 0) {
            boolean isMouse = event.isFromSource(true);
            int i = this.mTapState;
            if ((i != 1 && (i != 2 || !isMouse)) || SystemClock.uptimeMillis() - this.mLastTouchUpTime > ((long) ViewConfiguration.getDoubleTapTimeout())) {
                this.mTapState = 1;
            } else if (this.mTapState == 1) {
                this.mTapState = 2;
            } else {
                this.mTapState = 3;
            }
        }
        if (action == 1) {
            this.mLastTouchUpTime = SystemClock.uptimeMillis();
        }
    }

    private boolean shouldFilterOutTouchEvent(MotionEvent event) {
        if (!event.isFromSource(8194)) {
            return false;
        }
        boolean primaryButtonStateChanged = ((this.mLastButtonState ^ event.getButtonState()) & 1) != 0;
        int action = event.getActionMasked();
        if ((action == 0 || action == 1) && !primaryButtonStateChanged) {
            return true;
        }
        if (action != 2 || event.isButtonPressed(1)) {
            return false;
        }
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    public void onTouchEvent(MotionEvent event) {
        boolean filterOutEvent = shouldFilterOutTouchEvent(event);
        this.mLastButtonState = event.getButtonState();
        if (filterOutEvent) {
            if (event.getActionMasked() == 1) {
                this.mDiscardNextActionUp = true;
            }
            return;
        }
        updateTapState(event);
        updateFloatingToolbarVisibility(event);
        if (hasSelectionController()) {
            getSelectionController().onTouchEvent(event);
        }
        Runnable runnable = this.mShowSuggestionRunnable;
        if (runnable != null) {
            this.mTextView.removeCallbacks(runnable);
            this.mShowSuggestionRunnable = null;
        }
        if (event.getActionMasked() == 1) {
            this.mLastUpPositionX = event.getX();
            this.mLastUpPositionY = event.getY();
        }
        if (event.getActionMasked() == 0) {
            this.mLastDownPositionX = event.getX();
            this.mLastDownPositionY = event.getY();
            this.mTouchFocusSelected = false;
            this.mIgnoreActionUpEvent = false;
        }
    }

    private void updateFloatingToolbarVisibility(MotionEvent event) {
    }

    /* Access modifiers changed, original: 0000 */
    public void hideFloatingToolbar(int duration) {
    }

    public boolean isInSwipeSelectionMode() {
        if (getSelectionController() != null) {
            return getSelectionController().mInSwipeSelectionMode;
        }
        return false;
    }

    private InputMethodManager getInputMethodManager() {
        return (InputMethodManager) this.mTextView.getContext().getSystemService(InputMethodManager.class);
    }

    public void beginBatchEdit() {
        this.mInBatchEditControllers = true;
        InputMethodState ims = this.mInputMethodState;
        if (ims != null) {
            int nesting = ims.mBatchEditNesting + 1;
            ims.mBatchEditNesting = nesting;
            if (nesting == 1) {
                ims.mCursorChanged = false;
                ims.mChangedDelta = 0;
                if (ims.mContentChanged) {
                    ims.mChangedStart = 0;
                    ims.mChangedEnd = this.mTextView.getText().length();
                } else {
                    ims.mChangedStart = -1;
                    ims.mChangedEnd = -1;
                    ims.mContentChanged = false;
                }
                this.mUndoInputFilter.beginBatchEdit();
                this.mTextView.onBeginBatchEdit();
            }
        }
    }

    public void endBatchEdit() {
        this.mInBatchEditControllers = false;
        InputMethodState ims = this.mInputMethodState;
        if (ims != null) {
            int nesting = ims.mBatchEditNesting - 1;
            ims.mBatchEditNesting = nesting;
            if (nesting == 0) {
                finishBatchEdit(ims);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void ensureEndedBatchEdit() {
        InputMethodState ims = this.mInputMethodState;
        if (ims != null && ims.mBatchEditNesting != 0) {
            ims.mBatchEditNesting = 0;
            finishBatchEdit(ims);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void finishBatchEdit(InputMethodState ims) {
        this.mTextView.onEndBatchEdit();
        this.mUndoInputFilter.endBatchEdit();
        if (ims.mContentChanged || ims.mSelectionModeChanged) {
            this.mTextView.updateAfterEdit();
            reportExtractedText();
        } else if (ims.mCursorChanged) {
            this.mTextView.invalidateCursor();
        }
        sendUpdateSelection();
    }

    /* Access modifiers changed, original: 0000 */
    public boolean extractText(ExtractedTextRequest request, ExtractedText outText) {
        return extractTextInternal(request, -1, -1, -1, outText);
    }

    private boolean extractTextInternal(ExtractedTextRequest request, int partialStartOffset, int partialEndOffset, int delta, ExtractedText outText) {
        if (request == null || outText == null) {
            return false;
        }
        CharSequence content = this.mTextView.getText();
        if (content == null) {
            return false;
        }
        if (partialStartOffset != -2) {
            int N = content.length();
            if (partialStartOffset < 0) {
                outText.partialEndOffset = -1;
                outText.partialStartOffset = -1;
                partialStartOffset = 0;
                partialEndOffset = N;
            } else {
                partialEndOffset += delta;
                if (content instanceof Spanned) {
                    Spanned spanned = (Spanned) content;
                    Object[] spans = spanned.getSpans(partialStartOffset, partialEndOffset, ParcelableSpan.class);
                    int i = spans.length;
                    while (i > 0) {
                        i--;
                        int j = spanned.getSpanStart(spans[i]);
                        if (j < partialStartOffset) {
                            partialStartOffset = j;
                        }
                        j = spanned.getSpanEnd(spans[i]);
                        if (j > partialEndOffset) {
                            partialEndOffset = j;
                        }
                    }
                }
                outText.partialStartOffset = partialStartOffset;
                outText.partialEndOffset = partialEndOffset - delta;
                if (partialStartOffset > N) {
                    partialStartOffset = N;
                } else if (partialStartOffset < 0) {
                    partialStartOffset = 0;
                }
                if (partialEndOffset > N) {
                    partialEndOffset = N;
                } else if (partialEndOffset < 0) {
                    partialEndOffset = 0;
                }
            }
            if ((request.flags & 1) != 0) {
                outText.text = content.subSequence(partialStartOffset, partialEndOffset);
            } else {
                outText.text = TextUtils.substring(content, partialStartOffset, partialEndOffset);
            }
        } else {
            outText.partialStartOffset = 0;
            outText.partialEndOffset = 0;
            outText.text = "";
        }
        outText.flags = 0;
        if (MetaKeyKeyListener.getMetaState(content, 2048) != 0) {
            outText.flags |= 2;
        }
        if (this.mTextView.isSingleLine()) {
            outText.flags |= 1;
        }
        outText.startOffset = 0;
        outText.selectionStart = this.mTextView.getSelectionStart();
        outText.selectionEnd = this.mTextView.getSelectionEnd();
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean reportExtractedText() {
        InputMethodState ims = this.mInputMethodState;
        if (ims != null) {
            boolean contentChanged = ims.mContentChanged;
            if (contentChanged || ims.mSelectionModeChanged) {
                ims.mContentChanged = false;
                ims.mSelectionModeChanged = false;
                ExtractedTextRequest req = ims.mExtractedTextRequest;
                if (req != null) {
                    InputMethodManager imm = getInputMethodManager();
                    if (imm != null) {
                        if (ims.mChangedStart < 0 && !contentChanged) {
                            ims.mChangedStart = -2;
                        }
                        if (extractTextInternal(req, ims.mChangedStart, ims.mChangedEnd, ims.mChangedDelta, ims.mExtractedText)) {
                            imm.updateExtractedText(this.mTextView, req.token, ims.mExtractedText);
                            ims.mChangedStart = -1;
                            ims.mChangedEnd = -1;
                            ims.mChangedDelta = 0;
                            ims.mContentChanged = false;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void sendUpdateSelection() {
        InputMethodState inputMethodState = this.mInputMethodState;
        if (inputMethodState != null && inputMethodState.mBatchEditNesting <= 0) {
            InputMethodManager imm = getInputMethodManager();
            if (imm != null) {
                int candStart;
                int candEnd;
                int selectionStart = this.mTextView.getSelectionStart();
                int selectionEnd = this.mTextView.getSelectionEnd();
                if (this.mTextView.getText() instanceof Spannable) {
                    Spannable sp = (Spannable) this.mTextView.getText();
                    candStart = BaseInputConnection.getComposingSpanStart(sp);
                    candEnd = BaseInputConnection.getComposingSpanEnd(sp);
                } else {
                    candStart = -1;
                    candEnd = -1;
                }
                imm.updateSelection(this.mTextView, selectionStart, selectionEnd, candStart, candEnd);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void onDraw(Canvas canvas, Layout layout, Path highlight, Paint highlightPaint, int cursorOffsetVertical) {
        Path highlight2;
        Canvas canvas2 = canvas;
        Path highlight3 = highlight;
        int i = cursorOffsetVertical;
        int selectionStart = this.mTextView.getSelectionStart();
        int selectionEnd = this.mTextView.getSelectionEnd();
        InputMethodState ims = this.mInputMethodState;
        if (ims != null && ims.mBatchEditNesting == 0) {
            InputMethodManager imm = getInputMethodManager();
            if (imm != null) {
                if (imm.isActive(this.mTextView) && (ims.mContentChanged || ims.mSelectionModeChanged)) {
                    reportExtractedText();
                }
                if (imm.isWatchingCursor(this.mTextView) && highlight3 != null) {
                    highlight3.computeBounds(ims.mTmpRectF, true);
                    float[] fArr = ims.mTmpOffset;
                    ims.mTmpOffset[1] = 0.0f;
                    fArr[0] = 0.0f;
                    canvas.getMatrix().mapPoints(ims.mTmpOffset);
                    ims.mTmpRectF.offset(ims.mTmpOffset[0], ims.mTmpOffset[1]);
                    ims.mTmpRectF.offset(0.0f, (float) i);
                    ims.mCursorRectInWindow.set((int) (((double) ims.mTmpRectF.left) + 0.5d), (int) (((double) ims.mTmpRectF.top) + 0.5d), (int) (((double) ims.mTmpRectF.right) + 0.5d), (int) (((double) ims.mTmpRectF.bottom) + 0.5d));
                    imm.updateCursor(this.mTextView, ims.mCursorRectInWindow.left, ims.mCursorRectInWindow.top, ims.mCursorRectInWindow.right, ims.mCursorRectInWindow.bottom);
                }
            }
        }
        CorrectionHighlighter correctionHighlighter = this.mCorrectionHighlighter;
        if (correctionHighlighter != null) {
            correctionHighlighter.draw(canvas2, i);
        }
        if (highlight3 == null || selectionStart != selectionEnd || this.mDrawableForCursor == null) {
            highlight2 = highlight3;
        } else {
            drawCursor(canvas2, i);
            highlight2 = null;
        }
        if (this.mTextView.canHaveDisplayList() && canvas.isHardwareAccelerated()) {
            drawHardwareAccelerated(canvas, layout, highlight2, highlightPaint, cursorOffsetVertical);
            Layout layout2 = layout;
            Paint paint = highlightPaint;
            return;
        }
        layout.draw(canvas2, highlight2, highlightPaint, i);
    }

    private void drawHardwareAccelerated(Canvas canvas, Layout layout, Path highlight, Paint highlightPaint, int cursorOffsetVertical) {
        Canvas canvas2 = canvas;
        Layout layout2 = layout;
        long lineRange = layout2.getLineRangeForDraw(canvas2);
        int firstLine = TextUtils.unpackRangeStartFromLong(lineRange);
        int lastLine = TextUtils.unpackRangeEndFromLong(lineRange);
        if (lastLine >= 0) {
            layout.drawBackground(canvas, highlight, highlightPaint, cursorOffsetVertical, firstLine, lastLine);
            if (layout2 instanceof DynamicLayout) {
                int[] blockEndLines;
                int blockIndex;
                long lineRange2;
                int[] blockIndices;
                int indexFirstChangedBlock;
                int lastLine2;
                int firstLine2;
                DynamicLayout dynamicLayout;
                int i;
                if (this.mTextRenderNodes == null) {
                    this.mTextRenderNodes = (TextRenderNode[]) ArrayUtils.emptyArray(TextRenderNode.class);
                }
                DynamicLayout dynamicLayout2 = (DynamicLayout) layout2;
                int[] blockEndLines2 = dynamicLayout2.getBlockEndLines();
                int[] blockIndices2 = dynamicLayout2.getBlockIndices();
                int numberOfBlocks = dynamicLayout2.getNumberOfBlocks();
                int indexFirstChangedBlock2 = dynamicLayout2.getIndexFirstChangedBlock();
                int i2 = 0;
                int endOfPreviousBlock = -1;
                int searchStartIndex = 0;
                while (i2 < numberOfBlocks) {
                    int searchStartIndex2;
                    int blockEndLine = blockEndLines2[i2];
                    blockEndLines = blockEndLines2;
                    blockIndex = blockIndices2[i2];
                    lineRange2 = lineRange;
                    boolean blockIsInvalid = blockIndex == -1;
                    if (blockIsInvalid) {
                        blockIndex = getAvailableDisplayListIndex(blockIndices2, numberOfBlocks, searchStartIndex);
                        blockIndices2[i2] = blockIndex;
                        TextRenderNode[] textRenderNodeArr = this.mTextRenderNodes;
                        if (textRenderNodeArr[blockIndex] != null) {
                            textRenderNodeArr[blockIndex].isDirty = true;
                        }
                        searchStartIndex2 = blockIndex + 1;
                    } else {
                        searchStartIndex2 = searchStartIndex;
                    }
                    searchStartIndex = this.mTextRenderNodes;
                    if (searchStartIndex[blockIndex] == null) {
                        blockIndices = blockIndices2;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Text ");
                        stringBuilder.append(blockIndex);
                        searchStartIndex[blockIndex] = new TextRenderNode(stringBuilder.toString());
                    } else {
                        blockIndices = blockIndices2;
                        boolean z = blockIsInvalid;
                    }
                    boolean blockDisplayListIsInvalid = this.mTextRenderNodes[blockIndex].needsRecord();
                    RenderNode blockDisplayList = this.mTextRenderNodes[blockIndex].renderNode;
                    if (i2 >= indexFirstChangedBlock2 || blockDisplayListIsInvalid) {
                        int right;
                        int blockBeginLine = endOfPreviousBlock + 1;
                        indexFirstChangedBlock = indexFirstChangedBlock2;
                        indexFirstChangedBlock2 = layout2.getLineTop(blockBeginLine);
                        endOfPreviousBlock = layout2.getLineBottom(blockEndLine);
                        int left = 0;
                        int right2 = this.mTextView.getWidth();
                        if (this.mTextView.getHorizontallyScrolling() != 0) {
                            int i3 = firstLine;
                            float min = 2139095039;
                            searchStartIndex = blockBeginLine;
                            lastLine2 = lastLine;
                            lastLine = 1;
                            firstLine2 = i3;
                            while (searchStartIndex <= blockEndLine) {
                                dynamicLayout = dynamicLayout2;
                                min = Math.min(min, layout2.getLineLeft(searchStartIndex));
                                lastLine = Math.max(lastLine, layout2.getLineRight(searchStartIndex));
                                searchStartIndex++;
                                dynamicLayout2 = dynamicLayout;
                            }
                            dynamicLayout = dynamicLayout2;
                            right = (int) (1056964608 + lastLine);
                            firstLine = (int) min;
                        } else {
                            dynamicLayout = dynamicLayout2;
                            firstLine2 = firstLine;
                            lastLine2 = lastLine;
                            firstLine = left;
                            right = right2;
                        }
                        if (blockDisplayListIsInvalid) {
                            RecordingCanvas recordingCanvas = blockDisplayList.start(right - firstLine, endOfPreviousBlock - indexFirstChangedBlock2);
                            try {
                                recordingCanvas.translate((float) (-firstLine), (float) (-indexFirstChangedBlock2));
                                layout2.drawText(recordingCanvas, blockBeginLine, blockEndLine);
                                this.mTextRenderNodes[blockIndex].isDirty = false;
                                blockDisplayList.end(recordingCanvas);
                                blockDisplayList.setClipToBounds(false);
                            } catch (Throwable th) {
                                blockDisplayList.end(recordingCanvas);
                                blockDisplayList.setClipToBounds(false);
                            }
                        }
                        blockDisplayList.setLeftTopRightBottom(firstLine, indexFirstChangedBlock2, right, endOfPreviousBlock);
                    } else {
                        dynamicLayout = dynamicLayout2;
                        boolean z2 = blockDisplayListIsInvalid;
                        indexFirstChangedBlock = indexFirstChangedBlock2;
                        i = endOfPreviousBlock;
                        firstLine2 = firstLine;
                        lastLine2 = lastLine;
                    }
                    ((RecordingCanvas) canvas2).drawRenderNode(blockDisplayList);
                    endOfPreviousBlock = blockEndLine;
                    i2++;
                    blockEndLines2 = blockEndLines;
                    lineRange = lineRange2;
                    blockIndices2 = blockIndices;
                    searchStartIndex = searchStartIndex2;
                    indexFirstChangedBlock2 = indexFirstChangedBlock;
                    firstLine = firstLine2;
                    lastLine = lastLine2;
                    dynamicLayout2 = dynamicLayout;
                }
                dynamicLayout = dynamicLayout2;
                blockEndLines = blockEndLines2;
                blockIndices = blockIndices2;
                indexFirstChangedBlock = indexFirstChangedBlock2;
                i = endOfPreviousBlock;
                lineRange2 = lineRange;
                firstLine2 = firstLine;
                lastLine2 = lastLine;
                dynamicLayout2.setIndexFirstChangedBlock(numberOfBlocks);
                dynamicLayout2 = firstLine2;
                blockIndex = lastLine2;
            } else {
                layout2.drawText(canvas2, firstLine, lastLine);
            }
        }
    }

    private int getAvailableDisplayListIndex(int[] blockIndices, int numberOfBlocks, int searchStartIndex) {
        int length = this.mTextRenderNodes.length;
        for (int i = searchStartIndex; i < length; i++) {
            boolean blockIndexFound = false;
            for (int j = 0; j < numberOfBlocks; j++) {
                if (blockIndices[j] == i) {
                    blockIndexFound = true;
                    break;
                }
            }
            if (!blockIndexFound) {
                return i;
            }
        }
        this.mTextRenderNodes = (TextRenderNode[]) GrowingArrayUtils.append(this.mTextRenderNodes, length, null);
        return length;
    }

    private void drawCursor(Canvas canvas, int cursorOffsetVertical) {
        boolean translate = cursorOffsetVertical != 0;
        if (translate) {
            canvas.translate(0.0f, (float) cursorOffsetVertical);
        }
        Drawable drawable = this.mDrawableForCursor;
        if (drawable != null) {
            drawable.draw(canvas);
        }
        if (translate) {
            canvas.translate(0.0f, (float) (-cursorOffsetVertical));
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void invalidateHandlesAndActionMode() {
        SelectionModifierCursorController selectionModifierCursorController = this.mSelectionModifierCursorController;
        if (selectionModifierCursorController != null) {
            selectionModifierCursorController.invalidateHandles();
        }
        InsertionPointCursorController insertionPointCursorController = this.mInsertionPointCursorController;
        if (insertionPointCursorController != null) {
            insertionPointCursorController.invalidateHandle();
        }
        ActionMode actionMode = this.mTextActionMode;
        if (actionMode != null) {
            actionMode.invalidate();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void invalidateTextDisplayList(Layout layout, int start, int end) {
        if (this.mTextRenderNodes != null && (layout instanceof DynamicLayout)) {
            int firstLine = layout.getLineForOffset(start);
            int lastLine = layout.getLineForOffset(end);
            DynamicLayout dynamicLayout = (DynamicLayout) layout;
            int[] blockEndLines = dynamicLayout.getBlockEndLines();
            int[] blockIndices = dynamicLayout.getBlockIndices();
            int numberOfBlocks = dynamicLayout.getNumberOfBlocks();
            int i = 0;
            while (i < numberOfBlocks && blockEndLines[i] < firstLine) {
                i++;
            }
            while (i < numberOfBlocks) {
                int blockIndex = blockIndices[i];
                if (blockIndex != -1) {
                    this.mTextRenderNodes[blockIndex].isDirty = true;
                }
                if (blockEndLines[i] < lastLine) {
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void invalidateTextDisplayList() {
        if (this.mTextRenderNodes != null) {
            int i = 0;
            while (true) {
                TextRenderNode[] textRenderNodeArr = this.mTextRenderNodes;
                if (i < textRenderNodeArr.length) {
                    if (textRenderNodeArr[i] != null) {
                        textRenderNodeArr[i].isDirty = true;
                    }
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void updateCursorPosition() {
        loadCursorDrawable();
        if (this.mDrawableForCursor != null) {
            Layout layout = getActiveLayout();
            int offset = this.mTextView.getSelectionStart();
            int line = layout.getLineForOffset(offset);
            updateCursorPosition(layout.getLineTop(line), layout.getLineBottomWithoutSpacing(line), layout.getPrimaryHorizontal(offset, layout.shouldClampCursor(line)));
            handleEmailPopup(offset);
        }
    }

    private float getPrimaryHorizontal(Layout layout, Layout hintLayout, int offset, boolean clamped) {
        if (!TextUtils.isEmpty(layout.getText()) || hintLayout == null || TextUtils.isEmpty(hintLayout.getText())) {
            return layout.getPrimaryHorizontal(offset, clamped);
        }
        return hintLayout.getPrimaryHorizontal(offset, clamped);
    }

    /* Access modifiers changed, original: 0000 */
    public void refreshTextActionMode() {
        if (extractedTextModeWillBeStarted()) {
            this.mRestartActionModeOnNextRefresh = false;
            return;
        }
        boolean hasSelection = this.mTextView.hasSelection();
        SelectionModifierCursorController selectionController = getSelectionController();
        InsertionPointCursorController insertionController = getInsertionController();
        if ((selectionController == null || !selectionController.isCursorBeingModified()) && (insertionController == null || !insertionController.isCursorBeingModified())) {
            if (hasSelection) {
                hideInsertionPointCursorController();
                if (this.mTextActionMode == null) {
                    if (this.mRestartActionModeOnNextRefresh) {
                        startSelectionActionModeAsync(false);
                    }
                } else if (selectionController == null || !selectionController.isActive()) {
                    stopTextActionModeWithPreservingSelection();
                    startSelectionActionModeAsync(false);
                } else {
                    this.mTextActionMode.invalidateContentRect();
                }
            } else if (insertionController == null || !insertionController.isActive()) {
                stopTextActionMode();
            } else {
                ActionMode actionMode = this.mTextActionMode;
                if (actionMode != null) {
                    actionMode.invalidateContentRect();
                }
            }
            this.mRestartActionModeOnNextRefresh = false;
            return;
        }
        this.mRestartActionModeOnNextRefresh = false;
    }

    /* Access modifiers changed, original: 0000 */
    public void startInsertionActionMode() {
    }

    /* Access modifiers changed, original: 0000 */
    public TextView getTextView() {
        return this.mTextView;
    }

    /* Access modifiers changed, original: 0000 */
    public ActionMode getTextActionMode() {
        return this.mTextActionMode;
    }

    /* Access modifiers changed, original: 0000 */
    public void setRestartActionModeOnNextRefresh(boolean value) {
        this.mRestartActionModeOnNextRefresh = value;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean startSelectionActionMode() {
        boolean selectionStarted = startSelectionActionModeInternal();
        if (selectionStarted) {
            getSelectionController().show();
        } else if (getInsertionController() != null) {
            getInsertionController().show();
        }
        return selectionStarted;
    }

    /* Access modifiers changed, original: 0000 */
    public void startSelectionActionModeAsync(boolean adjustSelection) {
        getSelectionActionModeHelper().startSelectionActionModeAsync(adjustSelection);
    }

    /* Access modifiers changed, original: 0000 */
    public void startLinkActionModeAsync(int start, int end) {
    }

    /* Access modifiers changed, original: 0000 */
    public void invalidateActionModeAsync() {
        getSelectionActionModeHelper().invalidateActionModeAsync();
    }

    private SelectionActionModeHelper getSelectionActionModeHelper() {
        if (this.mSelectionActionModeHelper == null) {
            this.mSelectionActionModeHelper = new SelectionActionModeHelper(this);
        }
        return this.mSelectionActionModeHelper;
    }

    private boolean selectCurrentWordAndStartDrag() {
        Runnable runnable = this.mInsertionActionModeRunnable;
        if (runnable != null) {
            this.mTextView.removeCallbacks(runnable);
        }
        if (extractedTextModeWillBeStarted() || !checkField()) {
            return false;
        }
        if (!this.mTextView.hasSelection() && !selectCurrentWord()) {
            return false;
        }
        stopTextActionModeWithPreservingSelection();
        getSelectionController().enterDrag(2);
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean checkField() {
        if (this.mTextView.canSelectText() && this.mTextView.requestFocus()) {
            return true;
        }
        Log.w("TextView", "TextView does not support text selection. Selection cancelled.");
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean checkFieldAndSelectCurrentWord() {
        if (!this.mTextView.canSelectText() || !this.mTextView.requestFocus()) {
            Log.w("TextView", "TextView does not support text selection. Selection cancelled.");
            return false;
        } else if (this.mTextView.hasSelection()) {
            return true;
        } else {
            return selectCurrentWord();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean startSelectionActionModeInternal() {
        ActionMode actionMode = this.mTextActionMode;
        if (actionMode != null) {
            actionMode.invalidate();
            return false;
        } else if (!checkFieldAndSelectCurrentWord()) {
            return false;
        } else {
            if (!extractedTextModeWillBeStarted()) {
                Callback callback = this.mCustomSelectionActionModeCallback;
                if (callback != null && callback.toString().contains(MOCK_CALLBACK_NAME)) {
                    this.mAllowToStartActionMode = this.mCustomSelectionActionModeCallback.onCreateActionMode(mMockActionMode, new ActionMenu(this.mTextView.getContext()));
                    if (!this.mAllowToStartActionMode) {
                        Selection.setSelection((Spannable) this.mTextView.getText(), this.mTextView.getSelectionEnd());
                        return false;
                    }
                }
            }
            if (!this.mTextView.isTextSelectable() && this.mShowSoftInputOnFocus) {
                InputMethodManager imm = getInputMethodManager();
                if (imm != null) {
                    imm.showSoftInput(this.mTextView, 0, null);
                }
            }
            return true;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean startActionModeInternal(@TextActionMode int actionMode) {
        return startSelectionActionModeInternal();
    }

    /* Access modifiers changed, original: 0000 */
    public boolean extractedTextModeWillBeStarted() {
        boolean z = false;
        if (this.mTextView.isInExtractedMode()) {
            return false;
        }
        InputMethodManager imm = getInputMethodManager();
        if (imm != null && imm.isFullscreenMode()) {
            z = true;
        }
        return z;
    }

    private boolean shouldOfferToShowSuggestions() {
        CharSequence text = this.mTextView.getText();
        if (!(text instanceof Spannable)) {
            return false;
        }
        Spannable spannable = (Spannable) text;
        int selectionStart = this.mTextView.getSelectionStart();
        int selectionEnd = this.mTextView.getSelectionEnd();
        SuggestionSpan[] suggestionSpans = (SuggestionSpan[]) spannable.getSpans(selectionStart, selectionEnd, SuggestionSpan.class);
        if (suggestionSpans.length == 0) {
            return false;
        }
        int i;
        if (selectionStart == selectionEnd) {
            for (SuggestionSpan suggestions : suggestionSpans) {
                if (suggestions.getSuggestions().length > 0) {
                    return true;
                }
            }
            return false;
        }
        i = this.mTextView.getText().length();
        int maxSpanEnd = 0;
        int unionOfSpansCoveringSelectionStartStart = this.mTextView.getText().length();
        int unionOfSpansCoveringSelectionStartEnd = 0;
        boolean hasValidSuggestions = false;
        for (int i2 = 0; i2 < suggestionSpans.length; i2++) {
            int spanStart = spannable.getSpanStart(suggestionSpans[i2]);
            int spanEnd = spannable.getSpanEnd(suggestionSpans[i2]);
            i = Math.min(i, spanStart);
            maxSpanEnd = Math.max(maxSpanEnd, spanEnd);
            if (selectionStart >= spanStart && selectionStart <= spanEnd) {
                boolean hasValidSuggestions2 = hasValidSuggestions || suggestionSpans[i2].getSuggestions().length > 0;
                unionOfSpansCoveringSelectionStartStart = Math.min(unionOfSpansCoveringSelectionStartStart, spanStart);
                unionOfSpansCoveringSelectionStartEnd = Math.max(unionOfSpansCoveringSelectionStartEnd, spanEnd);
                hasValidSuggestions = hasValidSuggestions2;
            }
        }
        if (hasValidSuggestions && unionOfSpansCoveringSelectionStartStart < unionOfSpansCoveringSelectionStartEnd && i >= unionOfSpansCoveringSelectionStartStart && maxSpanEnd <= unionOfSpansCoveringSelectionStartEnd) {
            return true;
        }
        return false;
    }

    private boolean isCursorInsideEasyCorrectionSpan() {
        SuggestionSpan[] suggestionSpans = (SuggestionSpan[]) ((Spannable) this.mTextView.getText()).getSpans(this.mTextView.getSelectionStart(), this.mTextView.getSelectionEnd(), SuggestionSpan.class);
        for (SuggestionSpan flags : suggestionSpans) {
            if ((flags.getFlags() & 1) != 0) {
                return true;
            }
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public void onTouchUpEvent(MotionEvent event) {
        if (!getSelectionActionModeHelper().resetSelection(getTextView().getOffsetForPosition(event.getX(), event.getY()))) {
            boolean selectAllGotFocus = true;
            boolean z;
            if (hasInsertionController() && getInsertionController().getHandle().isPopshowing()) {
                z = true;
            } else {
                z = false;
            }
            if (!(this.mSelectAllOnFocus && this.mTextView.didTouchFocusSelect())) {
                selectAllGotFocus = false;
            }
            hideCursorAndSpanControllers();
            stopTextActionMode();
            CharSequence text = this.mTextView.getText();
            if (!selectAllGotFocus && text.length() > 0) {
                Selection.setSelection((Spannable) text, this.mTextView.getOffsetForPosition(event.getX(), event.getY()));
                SpellChecker spellChecker = this.mSpellChecker;
                if (spellChecker != null) {
                    spellChecker.onSelectionChanged();
                }
                if (!extractedTextModeWillBeStarted()) {
                    if (isCursorInsideEasyCorrectionSpan()) {
                        this.mShowSuggestionRunnable = new Runnable() {
                            public void run() {
                                Editor.this.showSuggestions();
                            }
                        };
                        this.mTextView.postDelayed(this.mShowSuggestionRunnable, (long) ViewConfiguration.getDoubleTapTimeout());
                    } else if (hasInsertionController()) {
                        getInsertionController().show();
                    }
                }
            }
        }
    }

    /* Access modifiers changed, original: final */
    public final void onTextOperationUserChanged() {
        SpellChecker spellChecker = this.mSpellChecker;
        if (spellChecker != null) {
            spellChecker.resetSession();
        }
    }

    /* Access modifiers changed, original: protected */
    public void stopTextActionMode() {
        ActionMode actionMode = this.mTextActionMode;
        if (actionMode != null) {
            actionMode.finish();
        }
        stopSelectionActionMode();
    }

    private void stopTextActionModeWithPreservingSelection() {
        if (this.mTextActionMode != null) {
            this.mRestartActionModeOnNextRefresh = true;
        }
        this.mPreserveSelection = true;
        stopTextActionMode();
        this.mPreserveSelection = false;
    }

    /* Access modifiers changed, original: protected */
    public void stopSelectionActionMode() {
        SelectionModifierCursorController selectionModifierCursorController = this.mSelectionModifierCursorController;
        if (selectionModifierCursorController != null) {
            selectionModifierCursorController.hide();
        }
        if (!this.mCustomSelectionActionModeCallbackDestroyed) {
            Callback callback = this.mCustomSelectionActionModeCallback;
            if (callback != null && this.mAllowToStartActionMode) {
                callback.onDestroyActionMode(mMockActionMode);
                this.mCustomSelectionActionModeCallbackDestroyed = true;
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean hasInsertionController() {
        return this.mInsertionControllerEnabled;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean hasSelectionController() {
        return this.mSelectionControllerEnabled;
    }

    /* Access modifiers changed, original: 0000 */
    public InsertionPointCursorController getInsertionController() {
        if (!this.mInsertionControllerEnabled) {
            return null;
        }
        if (this.mInsertionPointCursorController == null) {
            this.mInsertionPointCursorController = new InsertionPointCursorController(this, null);
            this.mTextView.getViewTreeObserver().addOnTouchModeChangeListener(this.mInsertionPointCursorController);
        }
        return this.mInsertionPointCursorController;
    }

    /* Access modifiers changed, original: 0000 */
    public SelectionModifierCursorController getSelectionController() {
        if (!this.mSelectionControllerEnabled) {
            return null;
        }
        if (this.mSelectionModifierCursorController == null) {
            this.mSelectionModifierCursorController = new SelectionModifierCursorController();
            this.mTextView.getViewTreeObserver().addOnTouchModeChangeListener(this.mSelectionModifierCursorController);
        }
        return this.mSelectionModifierCursorController;
    }

    @VisibleForTesting
    public Drawable getCursorDrawable() {
        return this.mDrawableForCursor;
    }

    private void updateCursorPosition(int top, int bottom, float horizontal) {
        loadCursorDrawable();
        int left = clampHorizontalPosition(this.mDrawableForCursor, horizontal);
        this.mDrawableForCursor.setBounds(left, top - this.mTempRect.top, left + this.mDrawableForCursor.getIntrinsicWidth(), this.mTempRect.bottom + bottom);
    }

    private int clampHorizontalPosition(Drawable drawable, float horizontal) {
        horizontal = Math.max(0.5f, horizontal - 0.5f);
        if (this.mTempRect == null) {
            this.mTempRect = new Rect();
        }
        int drawableWidth = 0;
        if (drawable != null) {
            drawable.getPadding(this.mTempRect);
            drawableWidth = drawable.getIntrinsicWidth();
        } else {
            this.mTempRect.setEmpty();
        }
        int scrollX = this.mTextView.getScrollX();
        float horizontalDiff = horizontal - ((float) scrollX);
        int viewClippedWidth = (this.mTextView.getWidth() - this.mTextView.getCompoundPaddingLeft()) - this.mTextView.getCompoundPaddingRight();
        if (horizontalDiff >= ((float) viewClippedWidth) - 1.0f) {
            return (viewClippedWidth + scrollX) - (drawableWidth - this.mTempRect.right);
        }
        if (Math.abs(horizontalDiff) <= 1.0f || (TextUtils.isEmpty(this.mTextView.getText()) && ((float) (1048576 - scrollX)) <= ((float) viewClippedWidth) + 1.0f && horizontal <= 1.0f)) {
            return scrollX - this.mTempRect.left;
        }
        return ((int) horizontal) - this.mTempRect.left;
    }

    public void onCommitCorrection(CorrectionInfo info) {
        CorrectionHighlighter correctionHighlighter = this.mCorrectionHighlighter;
        if (correctionHighlighter == null) {
            this.mCorrectionHighlighter = new CorrectionHighlighter();
        } else {
            correctionHighlighter.invalidate(false);
        }
        this.mCorrectionHighlighter.highlight(info);
        this.mUndoInputFilter.freezeLastEdit();
    }

    /* Access modifiers changed, original: 0000 */
    public void showSuggestions() {
        if (this.mSuggestionsPopupWindow == null) {
            this.mSuggestionsPopupWindow = new SuggestionsPopupWindow();
        }
        hideCursorAndSpanControllers();
        this.mSuggestionsPopupWindow.show();
    }

    /* Access modifiers changed, original: 0000 */
    public boolean areSuggestionsShown() {
        SuggestionsPopupWindow suggestionsPopupWindow = this.mSuggestionsPopupWindow;
        return suggestionsPopupWindow != null && suggestionsPopupWindow.isShowing();
    }

    /* Access modifiers changed, original: 0000 */
    public void onScrollChanged() {
        PositionListener positionListener = this.mPositionListener;
        if (positionListener != null) {
            positionListener.onScrollChanged();
        }
    }

    private boolean shouldBlink() {
        boolean z = false;
        if (!isCursorVisible() || !this.mTextView.isFocused()) {
            return false;
        }
        int start = this.mTextView.getSelectionStart();
        if (start < 0) {
            return false;
        }
        int end = this.mTextView.getSelectionEnd();
        if (end < 0) {
            return false;
        }
        if (start == end) {
            z = true;
        }
        return z;
    }

    /* Access modifiers changed, original: 0000 */
    public void makeBlink() {
        if (shouldBlink()) {
            this.mShowCursor = SystemClock.uptimeMillis();
            if (this.mBlink == null) {
                this.mBlink = new Blink(this, null);
            }
            this.mTextView.removeCallbacks(this.mBlink);
            this.mTextView.postDelayed(this.mBlink, 500);
            return;
        }
        Blink blink = this.mBlink;
        if (blink != null) {
            this.mTextView.removeCallbacks(blink);
        }
    }

    private DragShadowBuilder getTextThumbnailBuilder(int start, int end) {
        TextView shadowView = (TextView) View.inflate(this.mTextView.getContext(), com.android.internal.R.layout.text_drag_thumbnail, null);
        if (shadowView != null) {
            int i = end - start;
            int i2 = DRAG_SHADOW_MAX_TEXT_LENGTH;
            if (i > i2) {
                end = TextUtils.unpackRangeEndFromLong(getCharClusterRange(i2 + start));
            }
            shadowView.setText(this.mTextView.getTransformedText(start, end));
            shadowView.setTextColor(this.mTextView.getTextColors());
            shadowView.setTextAppearance(16);
            shadowView.setGravity(17);
            shadowView.setLayoutParams(new LayoutParams(-2, -2));
            int size = MeasureSpec.makeMeasureSpec(0, 0);
            shadowView.measure(size, size);
            shadowView.layout(0, 0, shadowView.getMeasuredWidth(), shadowView.getMeasuredHeight());
            shadowView.invalidate();
            return new DragShadowBuilder(shadowView);
        }
        throw new IllegalArgumentException("Unable to inflate text drag thumbnail");
    }

    /* Access modifiers changed, original: 0000 */
    public void onDrop(DragEvent event) {
        SpannableStringBuilder content = new SpannableStringBuilder();
        DragAndDropPermissions permissions = DragAndDropPermissions.obtain(event);
        if (permissions != null) {
            permissions.takeTransient();
        }
        try {
            ClipData clipData = event.getClipData();
            int itemCount = clipData.getItemCount();
            for (int i = 0; i < itemCount; i++) {
                content.append(clipData.getItemAt(i).coerceToStyledText(this.mTextView.getContext()));
            }
            this.mTextView.beginBatchEdit();
            this.mUndoInputFilter.freezeLastEdit();
            try {
                int offset = this.mTextView.getOffsetForPosition(event.getX(), event.getY());
                Object localState = event.getLocalState();
                DragLocalState dragLocalState = null;
                if (localState instanceof DragLocalState) {
                    dragLocalState = (DragLocalState) localState;
                }
                boolean dragDropIntoItself = dragLocalState != null && dragLocalState.sourceTextView == this.mTextView;
                if (!dragDropIntoItself || offset < dragLocalState.start || offset >= dragLocalState.end) {
                    int originalLength = this.mTextView.getText().length();
                    int min = offset;
                    int max = offset;
                    Selection.setSelection((Spannable) this.mTextView.getText(), max);
                    this.mTextView.replaceText_internal(min, max, content);
                    if (dragDropIntoItself) {
                        int shift;
                        int dragSourceStart = dragLocalState.start;
                        int dragSourceEnd = dragLocalState.end;
                        if (max <= dragSourceStart) {
                            shift = this.mTextView.getText().length() - originalLength;
                            dragSourceStart += shift;
                            dragSourceEnd += shift;
                        }
                        this.mTextView.deleteText_internal(dragSourceStart, dragSourceEnd);
                        shift = Math.max(0, dragSourceStart - 1);
                        int nextCharIdx = Math.min(this.mTextView.getText().length(), dragSourceStart + 1);
                        if (nextCharIdx > shift + 1) {
                            CharSequence t = this.mTextView.getTransformedText(shift, nextCharIdx);
                            if (Character.isSpaceChar(t.charAt(0)) && Character.isSpaceChar(t.charAt(1))) {
                                this.mTextView.deleteText_internal(shift, shift + 1);
                            }
                        }
                    }
                    this.mTextView.endBatchEdit();
                    this.mUndoInputFilter.freezeLastEdit();
                }
            } finally {
                this.mTextView.endBatchEdit();
                this.mUndoInputFilter.freezeLastEdit();
            }
        } finally {
            if (permissions != null) {
                permissions.release();
            }
        }
    }

    public void addSpanWatchers(Spannable text) {
        int textLength = text.length();
        KeyListener keyListener = this.mKeyListener;
        if (keyListener != null) {
            text.setSpan(keyListener, 0, textLength, 18);
        }
        if (this.mSpanController == null) {
            this.mSpanController = new SpanController();
        }
        text.setSpan(this.mSpanController, 0, textLength, 18);
    }

    /* Access modifiers changed, original: 0000 */
    public void setContextMenuAnchor(float x, float y) {
        this.mContextMenuAnchorX = x;
        this.mContextMenuAnchorY = y;
    }

    /* Access modifiers changed, original: 0000 */
    public void onCreateContextMenu(ContextMenu menu) {
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isPasswordInputType() {
        int variation = this.mInputType & 4095;
        return variation == 129 || variation == 225 || variation == 18;
    }

    /* Access modifiers changed, original: protected */
    public void onPopupWindowShown(PinnedPopupWindow window) {
        WeakReference weakReference = this.mShownWindow;
        if (weakReference != null) {
            PinnedPopupWindow shownWindow = (PinnedPopupWindow) weakReference.get();
            if (shownWindow != window) {
                if (shownWindow != null) {
                    shownWindow.hide();
                }
            } else {
                return;
            }
        }
        this.mShownWindow = new WeakReference(window);
    }

    /* Access modifiers changed, original: protected */
    public void onPopupWindowDismiss(PinnedPopupWindow window) {
        WeakReference weakReference = this.mShownWindow;
        if (weakReference != null && ((PinnedPopupWindow) weakReference.get()) == window) {
            this.mShownWindow = null;
        }
    }

    private static float getDescendantViewScale(View descendant) {
        float scale = 1.0f * descendant.getScaleX();
        ViewParent viewParent = descendant.getParent();
        while (viewParent instanceof View) {
            View view = (View) viewParent;
            if (view.getId() == 16908290) {
                break;
            }
            scale *= view.getScaleX();
            viewParent = view.getParent();
        }
        return scale;
    }

    private Layout getActiveLayout() {
        Layout layout = this.mTextView.getLayout();
        Layout hintLayout = this.mTextView.getHintLayout();
        if (!TextUtils.isEmpty(layout.getText()) || hintLayout == null || TextUtils.isEmpty(hintLayout.getText())) {
            return layout;
        }
        return hintLayout;
    }

    /* JADX WARNING: Missing block: B:18:0x007b, code skipped:
            return r0;
     */
    private int getCurrentLineAdjustedForSlop(android.text.Layout r13, int r14, float r15) {
        /*
        r12 = this;
        r0 = r12.mTextView;
        r0 = r0.getLineAtCoordinate(r15);
        if (r13 == 0) goto L_0x007b;
    L_0x0008:
        r1 = r13.getLineCount();
        if (r14 > r1) goto L_0x007b;
    L_0x000e:
        r1 = r13.getLineCount();
        if (r1 <= 0) goto L_0x007b;
    L_0x0014:
        if (r14 >= 0) goto L_0x0017;
    L_0x0016:
        goto L_0x007b;
    L_0x0017:
        r1 = r0 - r14;
        r1 = java.lang.Math.abs(r1);
        r2 = 2;
        if (r1 < r2) goto L_0x0021;
    L_0x0020:
        return r0;
    L_0x0021:
        r1 = r12.mTextView;
        r1 = r1.viewportToContentVerticalOffset();
        r1 = (float) r1;
        r2 = r13.getLineCount();
        r3 = r12.mTextView;
        r3 = r3.getLineHeight();
        r3 = (float) r3;
        r4 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r3 = r3 * r4;
        r4 = 0;
        r5 = r13.getLineTop(r4);
        r5 = (float) r5;
        r5 = r5 + r1;
        r6 = r13.getLineTop(r14);
        r6 = (float) r6;
        r6 = r6 + r1;
        r7 = r6 - r3;
        r8 = r5 + r3;
        r7 = java.lang.Math.max(r7, r8);
        r8 = r2 + -1;
        r8 = r13.getLineBottom(r8);
        r8 = (float) r8;
        r8 = r8 + r1;
        r9 = r13.getLineBottom(r14);
        r9 = (float) r9;
        r9 = r9 + r1;
        r10 = r9 + r3;
        r11 = r8 - r3;
        r10 = java.lang.Math.min(r10, r11);
        r11 = (r15 > r7 ? 1 : (r15 == r7 ? 0 : -1));
        if (r11 > 0) goto L_0x006c;
    L_0x0065:
        r11 = r14 + -1;
        r4 = java.lang.Math.max(r11, r4);
        goto L_0x007a;
    L_0x006c:
        r4 = (r15 > r10 ? 1 : (r15 == r10 ? 0 : -1));
        if (r4 < 0) goto L_0x0079;
    L_0x0070:
        r4 = r14 + 1;
        r11 = r2 + -1;
        r4 = java.lang.Math.min(r4, r11);
        goto L_0x007a;
    L_0x0079:
        r4 = r14;
    L_0x007a:
        return r4;
    L_0x007b:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.Editor.getCurrentLineAdjustedForSlop(android.text.Layout, int, float):int");
    }

    /* Access modifiers changed, original: 0000 */
    public void loadCursorDrawable() {
        if (this.mDrawableForCursor == null) {
            this.mDrawableForCursor = this.mTextView.getTextCursorDrawable();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void loadHandleDrawables(boolean overwrite) {
        if (this.mSelectHandleCenter == null || overwrite) {
            this.mSelectHandleCenter = this.mTextView.getTextSelectHandle();
            if (hasInsertionController()) {
                getInsertionController().reloadHandleDrawable();
            }
        }
        if (this.mSelectHandleLeft == null || this.mSelectHandleRight == null || overwrite) {
            this.mSelectHandleLeft = this.mTextView.getTextSelectHandleLeft();
            this.mSelectHandleRight = this.mTextView.getTextSelectHandleRight();
            if (hasSelectionController()) {
                getSelectionController().reloadHandleDrawables();
            }
        }
    }

    private static boolean isValidRange(CharSequence text, int start, int end) {
        return start >= 0 && start <= end && end <= text.length();
    }

    private void startActivityFromContext(Context context, Intent intent) {
        if (context instanceof Activity) {
            context.startActivity(intent);
            return;
        }
        intent.addFlags(268435456);
        context.startActivity(intent);
    }

    private static boolean isBigFontMode() {
        int scaleMode = MiuiConfiguration.getScaleMode();
        if (scaleMode == 11 || scaleMode == 15) {
            return true;
        }
        return false;
    }

    private int getDisplayHeightPixels() {
        if (Global.getInt(this.mTextView.getContext().getContentResolver(), MiuiSettings.Global.FORCE_FSG_NAV_BAR, 0) != 1) {
            return this.mTextView.getResources().getDisplayMetrics().heightPixels;
        }
        if (sTmpDisplayMetrics == null) {
            sTmpDisplayMetrics = new DisplayMetrics();
        }
        this.mTextView.getContext().getDisplay().getRealMetrics(sTmpDisplayMetrics);
        return sTmpDisplayMetrics.heightPixels;
    }
}
