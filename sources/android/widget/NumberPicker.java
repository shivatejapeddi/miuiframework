package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import libcore.icu.LocaleData;

public class NumberPicker extends LinearLayout {
    private static final int DEFAULT_LAYOUT_RESOURCE_ID = 17367238;
    private static final long DEFAULT_LONG_PRESS_UPDATE_INTERVAL = 300;
    private static final char[] DIGIT_CHARACTERS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 1632, 1633, 1634, 1635, 1636, 1637, 1638, 1639, 1640, 1641, 1776, 1777, 1778, 1779, 1780, 1781, 1782, 1783, 1784, 1785, 2406, 2407, 2408, 2409, 2410, 2411, 2412, 2413, 2414, 2415, 2534, 2535, 2536, 2537, 2538, 2539, 2540, 2541, 2542, 2543, 3302, 3303, 3304, 3305, 3306, 3307, 3308, 3309, 3310, 3311};
    private static final int SELECTOR_ADJUSTMENT_DURATION_MILLIS = 800;
    private static final int SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT = 8;
    @UnsupportedAppUsage
    private static final int SELECTOR_MIDDLE_ITEM_INDEX = 1;
    @UnsupportedAppUsage
    private static final int SELECTOR_WHEEL_ITEM_COUNT = 3;
    private static final int SIZE_UNSPECIFIED = -1;
    private static final int SNAP_SCROLL_DURATION = 300;
    private static final float TOP_AND_BOTTOM_FADING_EDGE_STRENGTH = 0.9f;
    private static final int UNSCALED_DEFAULT_SELECTION_DIVIDERS_DISTANCE = 48;
    private static final int UNSCALED_DEFAULT_SELECTION_DIVIDER_HEIGHT = 2;
    private static final TwoDigitFormatter sTwoDigitFormatter = new TwoDigitFormatter();
    private AccessibilityNodeProviderImpl mAccessibilityNodeProvider;
    private final Scroller mAdjustScroller;
    private BeginSoftInputOnLongPressCommand mBeginSoftInputOnLongPressCommand;
    private int mBottomSelectionDividerBottom;
    private ChangeCurrentByOneFromLongPressCommand mChangeCurrentByOneFromLongPressCommand;
    private final boolean mComputeMaxWidth;
    private int mCurrentScrollOffset;
    private final ImageButton mDecrementButton;
    private boolean mDecrementVirtualButtonPressed;
    private String[] mDisplayedValues;
    @UnsupportedAppUsage
    private final Scroller mFlingScroller;
    private Formatter mFormatter;
    private final boolean mHasSelectorWheel;
    private boolean mHideWheelUntilFocused;
    private boolean mIgnoreMoveEvents;
    private final ImageButton mIncrementButton;
    private boolean mIncrementVirtualButtonPressed;
    private int mInitialScrollOffset;
    @UnsupportedAppUsage
    private final EditText mInputText;
    private long mLastDownEventTime;
    private float mLastDownEventY;
    private float mLastDownOrMoveEventY;
    private int mLastHandledDownDpadKeyCode;
    private int mLastHoveredChildVirtualViewId;
    private long mLongPressUpdateInterval;
    private final int mMaxHeight;
    @UnsupportedAppUsage
    private int mMaxValue;
    private int mMaxWidth;
    @UnsupportedAppUsage
    private int mMaximumFlingVelocity;
    @UnsupportedAppUsage
    private final int mMinHeight;
    private int mMinValue;
    @UnsupportedAppUsage
    private final int mMinWidth;
    private int mMinimumFlingVelocity;
    private OnScrollListener mOnScrollListener;
    @UnsupportedAppUsage
    private OnValueChangeListener mOnValueChangeListener;
    private boolean mPerformClickOnTap;
    private final PressedStateHelper mPressedStateHelper;
    private int mPreviousScrollerY;
    private int mScrollState;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private final Drawable mSelectionDivider;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private int mSelectionDividerHeight;
    private final int mSelectionDividersDistance;
    private int mSelectorElementHeight;
    private final SparseArray<String> mSelectorIndexToStringCache;
    @UnsupportedAppUsage
    private final int[] mSelectorIndices;
    private int mSelectorTextGapHeight;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private final Paint mSelectorWheelPaint;
    private SetSelectionCommand mSetSelectionCommand;
    private final int mSolidColor;
    @UnsupportedAppUsage
    private final int mTextSize;
    private int mTopSelectionDividerTop;
    private int mTouchSlop;
    private int mValue;
    private VelocityTracker mVelocityTracker;
    private final Drawable mVirtualButtonPressedDrawable;
    private boolean mWrapSelectorWheel;
    private boolean mWrapSelectorWheelPreferred;

    public interface OnValueChangeListener {
        void onValueChange(NumberPicker numberPicker, int i, int i2);
    }

    class AccessibilityNodeProviderImpl extends AccessibilityNodeProvider {
        private static final int UNDEFINED = Integer.MIN_VALUE;
        private static final int VIRTUAL_VIEW_ID_DECREMENT = 3;
        private static final int VIRTUAL_VIEW_ID_INCREMENT = 1;
        private static final int VIRTUAL_VIEW_ID_INPUT = 2;
        private int mAccessibilityFocusedView = Integer.MIN_VALUE;
        private final int[] mTempArray = new int[2];
        private final Rect mTempRect = new Rect();

        AccessibilityNodeProviderImpl() {
        }

        public AccessibilityNodeInfo createAccessibilityNodeInfo(int virtualViewId) {
            if (virtualViewId == -1) {
                return createAccessibilityNodeInfoForNumberPicker(NumberPicker.this.mScrollX, NumberPicker.this.mScrollY, NumberPicker.this.mScrollX + (NumberPicker.this.mRight - NumberPicker.this.mLeft), NumberPicker.this.mScrollY + (NumberPicker.this.mBottom - NumberPicker.this.mTop));
            }
            if (virtualViewId == 1) {
                return createAccessibilityNodeInfoForVirtualButton(1, getVirtualIncrementButtonText(), NumberPicker.this.mScrollX, NumberPicker.this.mBottomSelectionDividerBottom - NumberPicker.this.mSelectionDividerHeight, NumberPicker.this.mScrollX + (NumberPicker.this.mRight - NumberPicker.this.mLeft), NumberPicker.this.mScrollY + (NumberPicker.this.mBottom - NumberPicker.this.mTop));
            } else if (virtualViewId == 2) {
                return createAccessibiltyNodeInfoForInputText(NumberPicker.this.mScrollX, NumberPicker.this.mTopSelectionDividerTop + NumberPicker.this.mSelectionDividerHeight, NumberPicker.this.mScrollX + (NumberPicker.this.mRight - NumberPicker.this.mLeft), NumberPicker.this.mBottomSelectionDividerBottom - NumberPicker.this.mSelectionDividerHeight);
            } else {
                if (virtualViewId != 3) {
                    return super.createAccessibilityNodeInfo(virtualViewId);
                }
                return createAccessibilityNodeInfoForVirtualButton(3, getVirtualDecrementButtonText(), NumberPicker.this.mScrollX, NumberPicker.this.mScrollY, NumberPicker.this.mScrollX + (NumberPicker.this.mRight - NumberPicker.this.mLeft), NumberPicker.this.mTopSelectionDividerTop + NumberPicker.this.mSelectionDividerHeight);
            }
        }

        public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String searched, int virtualViewId) {
            if (TextUtils.isEmpty(searched)) {
                return Collections.emptyList();
            }
            String searchedLowerCase = searched.toLowerCase();
            List<AccessibilityNodeInfo> result = new ArrayList();
            if (virtualViewId == -1) {
                findAccessibilityNodeInfosByTextInChild(searchedLowerCase, 3, result);
                findAccessibilityNodeInfosByTextInChild(searchedLowerCase, 2, result);
                findAccessibilityNodeInfosByTextInChild(searchedLowerCase, 1, result);
                return result;
            } else if (virtualViewId != 1 && virtualViewId != 2 && virtualViewId != 3) {
                return super.findAccessibilityNodeInfosByText(searched, virtualViewId);
            } else {
                findAccessibilityNodeInfosByTextInChild(searchedLowerCase, virtualViewId, result);
                return result;
            }
        }

        public boolean performAction(int virtualViewId, int action, Bundle arguments) {
            boolean z = false;
            if (virtualViewId != -1) {
                NumberPicker numberPicker;
                if (virtualViewId != 1) {
                    if (virtualViewId != 2) {
                        if (virtualViewId == 3) {
                            if (action != 16) {
                                if (action != 64) {
                                    if (action != 128 || this.mAccessibilityFocusedView != virtualViewId) {
                                        return false;
                                    }
                                    this.mAccessibilityFocusedView = Integer.MIN_VALUE;
                                    sendAccessibilityEventForVirtualView(virtualViewId, 65536);
                                    numberPicker = NumberPicker.this;
                                    numberPicker.invalidate(0, 0, numberPicker.mRight, NumberPicker.this.mTopSelectionDividerTop);
                                    return true;
                                } else if (this.mAccessibilityFocusedView == virtualViewId) {
                                    return false;
                                } else {
                                    this.mAccessibilityFocusedView = virtualViewId;
                                    sendAccessibilityEventForVirtualView(virtualViewId, 32768);
                                    numberPicker = NumberPicker.this;
                                    numberPicker.invalidate(0, 0, numberPicker.mRight, NumberPicker.this.mTopSelectionDividerTop);
                                    return true;
                                }
                            } else if (!NumberPicker.this.isEnabled()) {
                                return false;
                            } else {
                                if (virtualViewId == 1) {
                                    z = true;
                                }
                                NumberPicker.this.changeValueByOne(z);
                                sendAccessibilityEventForVirtualView(virtualViewId, 1);
                                return true;
                            }
                        }
                    } else if (action != 1) {
                        if (action != 2) {
                            if (action != 16) {
                                if (action != 32) {
                                    if (action != 64) {
                                        if (action != 128) {
                                            return NumberPicker.this.mInputText.performAccessibilityAction(action, arguments);
                                        }
                                        if (this.mAccessibilityFocusedView != virtualViewId) {
                                            return false;
                                        }
                                        this.mAccessibilityFocusedView = Integer.MIN_VALUE;
                                        sendAccessibilityEventForVirtualView(virtualViewId, 65536);
                                        NumberPicker.this.mInputText.invalidate();
                                        return true;
                                    } else if (this.mAccessibilityFocusedView == virtualViewId) {
                                        return false;
                                    } else {
                                        this.mAccessibilityFocusedView = virtualViewId;
                                        sendAccessibilityEventForVirtualView(virtualViewId, 32768);
                                        NumberPicker.this.mInputText.invalidate();
                                        return true;
                                    }
                                } else if (!NumberPicker.this.isEnabled()) {
                                    return false;
                                } else {
                                    NumberPicker.this.performLongClick();
                                    return true;
                                }
                            } else if (!NumberPicker.this.isEnabled()) {
                                return false;
                            } else {
                                NumberPicker.this.performClick();
                                return true;
                            }
                        } else if (!NumberPicker.this.isEnabled() || !NumberPicker.this.mInputText.isFocused()) {
                            return false;
                        } else {
                            NumberPicker.this.mInputText.clearFocus();
                            return true;
                        }
                    } else if (!NumberPicker.this.isEnabled() || NumberPicker.this.mInputText.isFocused()) {
                        return false;
                    } else {
                        return NumberPicker.this.mInputText.requestFocus();
                    }
                } else if (action != 16) {
                    if (action != 64) {
                        if (action != 128 || this.mAccessibilityFocusedView != virtualViewId) {
                            return false;
                        }
                        this.mAccessibilityFocusedView = Integer.MIN_VALUE;
                        sendAccessibilityEventForVirtualView(virtualViewId, 65536);
                        numberPicker = NumberPicker.this;
                        numberPicker.invalidate(0, numberPicker.mBottomSelectionDividerBottom, NumberPicker.this.mRight, NumberPicker.this.mBottom);
                        return true;
                    } else if (this.mAccessibilityFocusedView == virtualViewId) {
                        return false;
                    } else {
                        this.mAccessibilityFocusedView = virtualViewId;
                        sendAccessibilityEventForVirtualView(virtualViewId, 32768);
                        numberPicker = NumberPicker.this;
                        numberPicker.invalidate(0, numberPicker.mBottomSelectionDividerBottom, NumberPicker.this.mRight, NumberPicker.this.mBottom);
                        return true;
                    }
                } else if (!NumberPicker.this.isEnabled()) {
                    return false;
                } else {
                    NumberPicker.this.changeValueByOne(true);
                    sendAccessibilityEventForVirtualView(virtualViewId, 1);
                    return true;
                }
            } else if (action != 64) {
                if (action != 128) {
                    if (action != 4096) {
                        if (action == 8192) {
                            if (!NumberPicker.this.isEnabled() || (!NumberPicker.this.getWrapSelectorWheel() && NumberPicker.this.getValue() <= NumberPicker.this.getMinValue())) {
                                return false;
                            }
                            NumberPicker.this.changeValueByOne(false);
                            return true;
                        }
                    } else if (!NumberPicker.this.isEnabled() || (!NumberPicker.this.getWrapSelectorWheel() && NumberPicker.this.getValue() >= NumberPicker.this.getMaxValue())) {
                        return false;
                    } else {
                        NumberPicker.this.changeValueByOne(true);
                        return true;
                    }
                } else if (this.mAccessibilityFocusedView != virtualViewId) {
                    return false;
                } else {
                    this.mAccessibilityFocusedView = Integer.MIN_VALUE;
                    NumberPicker.this.clearAccessibilityFocus();
                    return true;
                }
            } else if (this.mAccessibilityFocusedView == virtualViewId) {
                return false;
            } else {
                this.mAccessibilityFocusedView = virtualViewId;
                NumberPicker.this.requestAccessibilityFocus();
                return true;
            }
            return super.performAction(virtualViewId, action, arguments);
        }

        public void sendAccessibilityEventForVirtualView(int virtualViewId, int eventType) {
            if (virtualViewId != 1) {
                if (virtualViewId == 2) {
                    sendAccessibilityEventForVirtualText(eventType);
                } else if (virtualViewId == 3 && hasVirtualDecrementButton()) {
                    sendAccessibilityEventForVirtualButton(virtualViewId, eventType, getVirtualDecrementButtonText());
                }
            } else if (hasVirtualIncrementButton()) {
                sendAccessibilityEventForVirtualButton(virtualViewId, eventType, getVirtualIncrementButtonText());
            }
        }

        private void sendAccessibilityEventForVirtualText(int eventType) {
            if (AccessibilityManager.getInstance(NumberPicker.this.mContext).isEnabled()) {
                AccessibilityEvent event = AccessibilityEvent.obtain(eventType);
                NumberPicker.this.mInputText.onInitializeAccessibilityEvent(event);
                NumberPicker.this.mInputText.onPopulateAccessibilityEvent(event);
                event.setSource(NumberPicker.this, 2);
                NumberPicker numberPicker = NumberPicker.this;
                numberPicker.requestSendAccessibilityEvent(numberPicker, event);
            }
        }

        private void sendAccessibilityEventForVirtualButton(int virtualViewId, int eventType, String text) {
            if (AccessibilityManager.getInstance(NumberPicker.this.mContext).isEnabled()) {
                AccessibilityEvent event = AccessibilityEvent.obtain(eventType);
                event.setClassName(Button.class.getName());
                event.setPackageName(NumberPicker.this.mContext.getPackageName());
                event.getText().add(text);
                event.setEnabled(NumberPicker.this.isEnabled());
                event.setSource(NumberPicker.this, virtualViewId);
                NumberPicker numberPicker = NumberPicker.this;
                numberPicker.requestSendAccessibilityEvent(numberPicker, event);
            }
        }

        private void findAccessibilityNodeInfosByTextInChild(String searchedLowerCase, int virtualViewId, List<AccessibilityNodeInfo> outResult) {
            String text;
            if (virtualViewId != 1) {
                if (virtualViewId == 2) {
                    CharSequence text2 = NumberPicker.this.mInputText.getText();
                    if (TextUtils.isEmpty(text2) || !text2.toString().toLowerCase().contains(searchedLowerCase)) {
                        CharSequence contentDesc = NumberPicker.this.mInputText.getText();
                        if (!TextUtils.isEmpty(contentDesc) && contentDesc.toString().toLowerCase().contains(searchedLowerCase)) {
                            outResult.add(createAccessibilityNodeInfo(2));
                            return;
                        }
                    }
                    outResult.add(createAccessibilityNodeInfo(2));
                    return;
                } else if (virtualViewId == 3) {
                    text = getVirtualDecrementButtonText();
                    if (!TextUtils.isEmpty(text) && text.toString().toLowerCase().contains(searchedLowerCase)) {
                        outResult.add(createAccessibilityNodeInfo(3));
                    }
                    return;
                }
                return;
            }
            text = getVirtualIncrementButtonText();
            if (!TextUtils.isEmpty(text) && text.toString().toLowerCase().contains(searchedLowerCase)) {
                outResult.add(createAccessibilityNodeInfo(1));
            }
        }

        private AccessibilityNodeInfo createAccessibiltyNodeInfoForInputText(int left, int top, int right, int bottom) {
            AccessibilityNodeInfo info = NumberPicker.this.mInputText.createAccessibilityNodeInfo();
            info.setSource(NumberPicker.this, 2);
            if (this.mAccessibilityFocusedView != 2) {
                info.addAction(64);
            }
            if (this.mAccessibilityFocusedView == 2) {
                info.addAction(128);
            }
            Rect boundsInParent = this.mTempRect;
            boundsInParent.set(left, top, right, bottom);
            info.setVisibleToUser(NumberPicker.this.isVisibleToUser(boundsInParent));
            info.setBoundsInParent(boundsInParent);
            Rect boundsInScreen = boundsInParent;
            int[] locationOnScreen = this.mTempArray;
            NumberPicker.this.getLocationOnScreen(locationOnScreen);
            boundsInScreen.offset(locationOnScreen[0], locationOnScreen[1]);
            info.setBoundsInScreen(boundsInScreen);
            return info;
        }

        private AccessibilityNodeInfo createAccessibilityNodeInfoForVirtualButton(int virtualViewId, String text, int left, int top, int right, int bottom) {
            AccessibilityNodeInfo info = AccessibilityNodeInfo.obtain();
            info.setClassName(Button.class.getName());
            info.setPackageName(NumberPicker.this.mContext.getPackageName());
            info.setSource(NumberPicker.this, virtualViewId);
            info.setParent(NumberPicker.this);
            info.setText(text);
            info.setClickable(true);
            info.setLongClickable(true);
            info.setEnabled(NumberPicker.this.isEnabled());
            Rect boundsInParent = this.mTempRect;
            boundsInParent.set(left, top, right, bottom);
            info.setVisibleToUser(NumberPicker.this.isVisibleToUser(boundsInParent));
            info.setBoundsInParent(boundsInParent);
            Rect boundsInScreen = boundsInParent;
            int[] locationOnScreen = this.mTempArray;
            NumberPicker.this.getLocationOnScreen(locationOnScreen);
            boundsInScreen.offset(locationOnScreen[0], locationOnScreen[1]);
            info.setBoundsInScreen(boundsInScreen);
            if (this.mAccessibilityFocusedView != virtualViewId) {
                info.addAction(64);
            }
            if (this.mAccessibilityFocusedView == virtualViewId) {
                info.addAction(128);
            }
            if (NumberPicker.this.isEnabled()) {
                info.addAction(16);
            }
            return info;
        }

        private AccessibilityNodeInfo createAccessibilityNodeInfoForNumberPicker(int left, int top, int right, int bottom) {
            AccessibilityNodeInfo info = AccessibilityNodeInfo.obtain();
            info.setClassName(NumberPicker.class.getName());
            info.setPackageName(NumberPicker.this.mContext.getPackageName());
            info.setSource(NumberPicker.this);
            if (hasVirtualDecrementButton()) {
                info.addChild(NumberPicker.this, 3);
            }
            info.addChild(NumberPicker.this, 2);
            if (hasVirtualIncrementButton()) {
                info.addChild(NumberPicker.this, 1);
            }
            info.setParent((View) NumberPicker.this.getParentForAccessibility());
            info.setEnabled(NumberPicker.this.isEnabled());
            info.setScrollable(true);
            float applicationScale = NumberPicker.this.getContext().getResources().getCompatibilityInfo().applicationScale;
            Rect boundsInParent = this.mTempRect;
            boundsInParent.set(left, top, right, bottom);
            boundsInParent.scale(applicationScale);
            info.setBoundsInParent(boundsInParent);
            info.setVisibleToUser(NumberPicker.this.isVisibleToUser());
            Rect boundsInScreen = boundsInParent;
            int[] locationOnScreen = this.mTempArray;
            NumberPicker.this.getLocationOnScreen(locationOnScreen);
            boundsInScreen.offset(locationOnScreen[0], locationOnScreen[1]);
            boundsInScreen.scale(applicationScale);
            info.setBoundsInScreen(boundsInScreen);
            if (this.mAccessibilityFocusedView != -1) {
                info.addAction(64);
            }
            if (this.mAccessibilityFocusedView == -1) {
                info.addAction(128);
            }
            if (NumberPicker.this.isEnabled()) {
                if (NumberPicker.this.getWrapSelectorWheel() || NumberPicker.this.getValue() < NumberPicker.this.getMaxValue()) {
                    info.addAction(4096);
                }
                if (NumberPicker.this.getWrapSelectorWheel() || NumberPicker.this.getValue() > NumberPicker.this.getMinValue()) {
                    info.addAction(8192);
                }
            }
            return info;
        }

        private boolean hasVirtualDecrementButton() {
            return NumberPicker.this.getWrapSelectorWheel() || NumberPicker.this.getValue() > NumberPicker.this.getMinValue();
        }

        private boolean hasVirtualIncrementButton() {
            return NumberPicker.this.getWrapSelectorWheel() || NumberPicker.this.getValue() < NumberPicker.this.getMaxValue();
        }

        private String getVirtualDecrementButtonText() {
            int value = NumberPicker.this.mValue - 1;
            if (NumberPicker.this.mWrapSelectorWheel) {
                value = NumberPicker.this.getWrappedSelectorIndex(value);
            }
            if (value < NumberPicker.this.mMinValue) {
                return null;
            }
            String access$6800;
            if (NumberPicker.this.mDisplayedValues == null) {
                access$6800 = NumberPicker.this.formatNumber(value);
            } else {
                access$6800 = NumberPicker.this.mDisplayedValues[value - NumberPicker.this.mMinValue];
            }
            return access$6800;
        }

        private String getVirtualIncrementButtonText() {
            int value = NumberPicker.this.mValue + 1;
            if (NumberPicker.this.mWrapSelectorWheel) {
                value = NumberPicker.this.getWrappedSelectorIndex(value);
            }
            if (value > NumberPicker.this.mMaxValue) {
                return null;
            }
            String access$6800;
            if (NumberPicker.this.mDisplayedValues == null) {
                access$6800 = NumberPicker.this.formatNumber(value);
            } else {
                access$6800 = NumberPicker.this.mDisplayedValues[value - NumberPicker.this.mMinValue];
            }
            return access$6800;
        }
    }

    class BeginSoftInputOnLongPressCommand implements Runnable {
        BeginSoftInputOnLongPressCommand() {
        }

        public void run() {
            NumberPicker.this.performLongClick();
        }
    }

    class ChangeCurrentByOneFromLongPressCommand implements Runnable {
        private boolean mIncrement;

        ChangeCurrentByOneFromLongPressCommand() {
        }

        private void setStep(boolean increment) {
            this.mIncrement = increment;
        }

        public void run() {
            NumberPicker.this.changeValueByOne(this.mIncrement);
            NumberPicker numberPicker = NumberPicker.this;
            numberPicker.postDelayed(this, numberPicker.mLongPressUpdateInterval);
        }
    }

    public static class CustomEditText extends EditText {
        public CustomEditText(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public void onEditorAction(int actionCode) {
            super.onEditorAction(actionCode);
            if (actionCode == 6) {
                clearFocus();
            }
        }
    }

    public interface Formatter {
        String format(int i);
    }

    class InputTextFilter extends NumberKeyListener {
        InputTextFilter() {
        }

        public int getInputType() {
            return 1;
        }

        /* Access modifiers changed, original: protected */
        public char[] getAcceptedChars() {
            return NumberPicker.DIGIT_CHARACTERS;
        }

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Spanned spanned = dest;
            int i = dstart;
            int i2 = dend;
            if (NumberPicker.this.mSetSelectionCommand != null) {
                NumberPicker.this.mSetSelectionCommand.cancel();
            }
            int i3 = 0;
            String str = "";
            CharSequence filtered;
            if (NumberPicker.this.mDisplayedValues == null) {
                filtered = super.filter(source, start, end, dest, dstart, dend);
                if (filtered == null) {
                    filtered = source.subSequence(start, end);
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(String.valueOf(spanned.subSequence(0, i)));
                stringBuilder.append(filtered);
                stringBuilder.append(spanned.subSequence(i2, dest.length()));
                String result = stringBuilder.toString();
                if (str.equals(result)) {
                    return result;
                }
                if (NumberPicker.this.getSelectedPos(result) > NumberPicker.this.mMaxValue || result.length() > String.valueOf(NumberPicker.this.mMaxValue).length()) {
                    return str;
                }
                return filtered;
            }
            filtered = String.valueOf(source.subSequence(start, end));
            if (TextUtils.isEmpty(filtered)) {
                return str;
            }
            String result2 = new StringBuilder();
            result2.append(String.valueOf(spanned.subSequence(0, i)));
            result2.append(filtered);
            result2.append(spanned.subSequence(i2, dest.length()));
            result2 = result2.toString();
            String str2 = String.valueOf(result2).toLowerCase();
            String[] access$800 = NumberPicker.this.mDisplayedValues;
            int length = access$800.length;
            while (i3 < length) {
                String val = access$800[i3];
                if (val.toLowerCase().startsWith(str2)) {
                    NumberPicker.this.postSetSelectionCommand(result2.length(), val.length());
                    return val.subSequence(i, val.length());
                }
                i3++;
            }
            return str;
        }
    }

    public interface OnScrollListener {
        public static final int SCROLL_STATE_FLING = 2;
        public static final int SCROLL_STATE_IDLE = 0;
        public static final int SCROLL_STATE_TOUCH_SCROLL = 1;

        @Retention(RetentionPolicy.SOURCE)
        public @interface ScrollState {
        }

        void onScrollStateChange(NumberPicker numberPicker, int i);
    }

    class PressedStateHelper implements Runnable {
        public static final int BUTTON_DECREMENT = 2;
        public static final int BUTTON_INCREMENT = 1;
        private final int MODE_PRESS = 1;
        private final int MODE_TAPPED = 2;
        private int mManagedButton;
        private int mMode;

        PressedStateHelper() {
        }

        public void cancel() {
            NumberPicker numberPicker;
            this.mMode = 0;
            this.mManagedButton = 0;
            NumberPicker.this.removeCallbacks(this);
            if (NumberPicker.this.mIncrementVirtualButtonPressed) {
                NumberPicker.this.mIncrementVirtualButtonPressed = false;
                numberPicker = NumberPicker.this;
                numberPicker.invalidate(0, numberPicker.mBottomSelectionDividerBottom, NumberPicker.this.mRight, NumberPicker.this.mBottom);
            }
            NumberPicker.this.mDecrementVirtualButtonPressed = false;
            if (NumberPicker.this.mDecrementVirtualButtonPressed) {
                numberPicker = NumberPicker.this;
                numberPicker.invalidate(0, 0, numberPicker.mRight, NumberPicker.this.mTopSelectionDividerTop);
            }
        }

        public void buttonPressDelayed(int button) {
            cancel();
            this.mMode = 1;
            this.mManagedButton = button;
            NumberPicker.this.postDelayed(this, (long) ViewConfiguration.getTapTimeout());
        }

        public void buttonTapped(int button) {
            cancel();
            this.mMode = 2;
            this.mManagedButton = button;
            NumberPicker.this.post(this);
        }

        public void run() {
            int i = this.mMode;
            NumberPicker numberPicker;
            if (i == 1) {
                i = this.mManagedButton;
                if (i == 1) {
                    NumberPicker.this.mIncrementVirtualButtonPressed = true;
                    numberPicker = NumberPicker.this;
                    numberPicker.invalidate(0, numberPicker.mBottomSelectionDividerBottom, NumberPicker.this.mRight, NumberPicker.this.mBottom);
                } else if (i == 2) {
                    NumberPicker.this.mDecrementVirtualButtonPressed = true;
                    numberPicker = NumberPicker.this;
                    numberPicker.invalidate(0, 0, numberPicker.mRight, NumberPicker.this.mTopSelectionDividerTop);
                }
            } else if (i == 2) {
                i = this.mManagedButton;
                if (i == 1) {
                    if (!NumberPicker.this.mIncrementVirtualButtonPressed) {
                        NumberPicker.this.postDelayed(this, (long) ViewConfiguration.getPressedStateDuration());
                    }
                    NumberPicker.access$1280(NumberPicker.this, 1);
                    numberPicker = NumberPicker.this;
                    numberPicker.invalidate(0, numberPicker.mBottomSelectionDividerBottom, NumberPicker.this.mRight, NumberPicker.this.mBottom);
                } else if (i == 2) {
                    if (!NumberPicker.this.mDecrementVirtualButtonPressed) {
                        NumberPicker.this.postDelayed(this, (long) ViewConfiguration.getPressedStateDuration());
                    }
                    NumberPicker.access$1680(NumberPicker.this, 1);
                    numberPicker = NumberPicker.this;
                    numberPicker.invalidate(0, 0, numberPicker.mRight, NumberPicker.this.mTopSelectionDividerTop);
                }
            }
        }
    }

    private static class SetSelectionCommand implements Runnable {
        private final EditText mInputText;
        private boolean mPosted;
        private int mSelectionEnd;
        private int mSelectionStart;

        public SetSelectionCommand(EditText inputText) {
            this.mInputText = inputText;
        }

        public void post(int selectionStart, int selectionEnd) {
            this.mSelectionStart = selectionStart;
            this.mSelectionEnd = selectionEnd;
            if (!this.mPosted) {
                this.mInputText.post(this);
                this.mPosted = true;
            }
        }

        public void cancel() {
            if (this.mPosted) {
                this.mInputText.removeCallbacks(this);
                this.mPosted = false;
            }
        }

        public void run() {
            this.mPosted = false;
            this.mInputText.setSelection(this.mSelectionStart, this.mSelectionEnd);
        }
    }

    private static class TwoDigitFormatter implements Formatter {
        final Object[] mArgs = new Object[1];
        final StringBuilder mBuilder = new StringBuilder();
        java.util.Formatter mFmt;
        char mZeroDigit;

        TwoDigitFormatter() {
            init(Locale.getDefault());
        }

        private void init(Locale locale) {
            this.mFmt = createFormatter(locale);
            this.mZeroDigit = getZeroDigit(locale);
        }

        public String format(int value) {
            Locale currentLocale = Locale.getDefault();
            if (this.mZeroDigit != getZeroDigit(currentLocale)) {
                init(currentLocale);
            }
            this.mArgs[0] = Integer.valueOf(value);
            StringBuilder stringBuilder = this.mBuilder;
            stringBuilder.delete(0, stringBuilder.length());
            this.mFmt.format("%02d", this.mArgs);
            return this.mFmt.toString();
        }

        private static char getZeroDigit(Locale locale) {
            return LocaleData.get(locale).zeroDigit;
        }

        private java.util.Formatter createFormatter(Locale locale) {
            return new java.util.Formatter(this.mBuilder, locale);
        }
    }

    static /* synthetic */ boolean access$1280(NumberPicker x0, int x1) {
        byte b = (byte) (x0.mIncrementVirtualButtonPressed ^ x1);
        x0.mIncrementVirtualButtonPressed = b;
        return b;
    }

    static /* synthetic */ boolean access$1680(NumberPicker x0, int x1) {
        byte b = (byte) (x0.mDecrementVirtualButtonPressed ^ x1);
        x0.mDecrementVirtualButtonPressed = b;
        return b;
    }

    @UnsupportedAppUsage
    public static final Formatter getTwoDigitFormatter() {
        return sTwoDigitFormatter;
    }

    public NumberPicker(Context context) {
        this(context, null);
    }

    public NumberPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 16844068);
    }

    public NumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public NumberPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mWrapSelectorWheelPreferred = true;
        this.mLongPressUpdateInterval = DEFAULT_LONG_PRESS_UPDATE_INTERVAL;
        this.mSelectorIndexToStringCache = new SparseArray();
        this.mSelectorIndices = new int[3];
        this.mInitialScrollOffset = Integer.MIN_VALUE;
        this.mScrollState = 0;
        this.mLastHandledDownDpadKeyCode = -1;
        TypedArray attributesArray = context.obtainStyledAttributes(attrs, R.styleable.NumberPicker, defStyleAttr, defStyleRes);
        TypedArray attributesArray2 = attributesArray;
        saveAttributeDataForStyleable(context, R.styleable.NumberPicker, attrs, attributesArray, defStyleAttr, defStyleRes);
        int layoutResId = attributesArray2.getResourceId(3, 17367238);
        this.mHasSelectorWheel = layoutResId != 17367238;
        this.mHideWheelUntilFocused = attributesArray2.getBoolean(2, false);
        this.mSolidColor = attributesArray2.getColor(0, 0);
        Drawable selectionDivider = attributesArray2.getDrawable(8);
        if (selectionDivider != null) {
            selectionDivider.setCallback(this);
            selectionDivider.setLayoutDirection(getLayoutDirection());
            if (selectionDivider.isStateful()) {
                selectionDivider.setState(getDrawableState());
            }
        }
        this.mSelectionDivider = selectionDivider;
        this.mSelectionDividerHeight = attributesArray2.getDimensionPixelSize(1, (int) TypedValue.applyDimension(1, 2.0f, getResources().getDisplayMetrics()));
        this.mSelectionDividersDistance = attributesArray2.getDimensionPixelSize(9, (int) TypedValue.applyDimension(1, 48.0f, getResources().getDisplayMetrics()));
        this.mMinHeight = attributesArray2.getDimensionPixelSize(6, -1);
        this.mMaxHeight = attributesArray2.getDimensionPixelSize(4, -1);
        int i = this.mMinHeight;
        if (i != -1) {
            int i2 = this.mMaxHeight;
            if (i2 != -1 && i > i2) {
                throw new IllegalArgumentException("minHeight > maxHeight");
            }
        }
        this.mMinWidth = attributesArray2.getDimensionPixelSize(7, -1);
        this.mMaxWidth = attributesArray2.getDimensionPixelSize(5, -1);
        i = this.mMinWidth;
        if (i != -1) {
            int i3 = this.mMaxWidth;
            if (i3 != -1 && i > i3) {
                throw new IllegalArgumentException("minWidth > maxWidth");
            }
        }
        this.mComputeMaxWidth = this.mMaxWidth == -1;
        this.mVirtualButtonPressedDrawable = attributesArray2.getDrawable(10);
        attributesArray2.recycle();
        this.mPressedStateHelper = new PressedStateHelper();
        setWillNotDraw(this.mHasSelectorWheel ^ 1);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(layoutResId, this, true);
        OnClickListener onClickListener = new OnClickListener() {
            public void onClick(View v) {
                NumberPicker.this.hideSoftInput();
                NumberPicker.this.mInputText.clearFocus();
                if (v.getId() == R.id.increment) {
                    NumberPicker.this.changeValueByOne(true);
                } else {
                    NumberPicker.this.changeValueByOne(false);
                }
            }
        };
        OnLongClickListener onLongClickListener = new OnLongClickListener() {
            public boolean onLongClick(View v) {
                NumberPicker.this.hideSoftInput();
                NumberPicker.this.mInputText.clearFocus();
                if (v.getId() == R.id.increment) {
                    NumberPicker.this.postChangeCurrentByOneFromLongPress(true, 0);
                } else {
                    NumberPicker.this.postChangeCurrentByOneFromLongPress(false, 0);
                }
                return true;
            }
        };
        if (this.mHasSelectorWheel) {
            this.mIncrementButton = null;
        } else {
            this.mIncrementButton = (ImageButton) findViewById(R.id.increment);
            this.mIncrementButton.setOnClickListener(onClickListener);
            this.mIncrementButton.setOnLongClickListener(onLongClickListener);
        }
        if (this.mHasSelectorWheel) {
            this.mDecrementButton = null;
        } else {
            this.mDecrementButton = (ImageButton) findViewById(R.id.decrement);
            this.mDecrementButton.setOnClickListener(onClickListener);
            this.mDecrementButton.setOnLongClickListener(onLongClickListener);
        }
        this.mInputText = (EditText) findViewById(R.id.numberpicker_input);
        this.mInputText.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    NumberPicker.this.mInputText.selectAll();
                    return;
                }
                NumberPicker.this.mInputText.setSelection(0, 0);
                NumberPicker.this.validateInputTextView(v);
            }
        });
        this.mInputText.setFilters(new InputFilter[]{new InputTextFilter()});
        this.mInputText.setAccessibilityLiveRegion(1);
        this.mInputText.setRawInputType(2);
        this.mInputText.setImeOptions(6);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        this.mTouchSlop = configuration.getScaledTouchSlop();
        this.mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
        this.mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity() / 8;
        this.mTextSize = (int) this.mInputText.getTextSize();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Align.CENTER);
        paint.setTextSize((float) this.mTextSize);
        paint.setTypeface(this.mInputText.getTypeface());
        int color = this.mInputText.getTextColors().getColorForState(ENABLED_STATE_SET, -1);
        paint.setColor(color);
        this.mSelectorWheelPaint = paint;
        this.mFlingScroller = new Scroller(getContext(), null, true);
        this.mAdjustScroller = new Scroller(getContext(), new DecelerateInterpolator(2.5f));
        updateInputTextView();
        if (getImportantForAccessibility() == 0) {
            color = 1;
            setImportantForAccessibility(1);
        } else {
            color = 1;
        }
        if (getFocusable() == 16) {
            setFocusable(color);
            setFocusableInTouchMode(color);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (this.mHasSelectorWheel) {
            int msrdWdth = getMeasuredWidth();
            int msrdHght = getMeasuredHeight();
            int inptTxtMsrdWdth = this.mInputText.getMeasuredWidth();
            int inptTxtMsrdHght = this.mInputText.getMeasuredHeight();
            int inptTxtLeft = (msrdWdth - inptTxtMsrdWdth) / 2;
            int inptTxtTop = (msrdHght - inptTxtMsrdHght) / 2;
            this.mInputText.layout(inptTxtLeft, inptTxtTop, inptTxtLeft + inptTxtMsrdWdth, inptTxtTop + inptTxtMsrdHght);
            if (changed) {
                initializeSelectorWheel();
                initializeFadingEdges();
                int height = getHeight();
                int i = this.mSelectionDividersDistance;
                height = (height - i) / 2;
                int i2 = this.mSelectionDividerHeight;
                this.mTopSelectionDividerTop = height - i2;
                this.mBottomSelectionDividerBottom = (this.mTopSelectionDividerTop + (i2 * 2)) + i;
            }
            return;
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mHasSelectorWheel) {
            super.onMeasure(makeMeasureSpec(widthMeasureSpec, this.mMaxWidth), makeMeasureSpec(heightMeasureSpec, this.mMaxHeight));
            setMeasuredDimension(resolveSizeAndStateRespectingMinSize(this.mMinWidth, getMeasuredWidth(), widthMeasureSpec), resolveSizeAndStateRespectingMinSize(this.mMinHeight, getMeasuredHeight(), heightMeasureSpec));
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private boolean moveToFinalScrollerPosition(Scroller scroller) {
        scroller.forceFinished(true);
        int amountToScroll = scroller.getFinalY() - scroller.getCurrY();
        int overshootAdjustment = this.mInitialScrollOffset - ((this.mCurrentScrollOffset + amountToScroll) % this.mSelectorElementHeight);
        if (overshootAdjustment == 0) {
            return false;
        }
        int abs = Math.abs(overshootAdjustment);
        int i = this.mSelectorElementHeight;
        if (abs > i / 2) {
            if (overshootAdjustment > 0) {
                overshootAdjustment -= i;
            } else {
                overshootAdjustment += i;
            }
        }
        scrollBy(0, amountToScroll + overshootAdjustment);
        return true;
    }

    /* JADX WARNING: Missing block: B:31:0x00ab, code skipped:
            return false;
     */
    public boolean onInterceptTouchEvent(android.view.MotionEvent r6) {
        /*
        r5 = this;
        r0 = r5.mHasSelectorWheel;
        r1 = 0;
        if (r0 == 0) goto L_0x00ab;
    L_0x0005:
        r0 = r5.isEnabled();
        if (r0 != 0) goto L_0x000d;
    L_0x000b:
        goto L_0x00ab;
    L_0x000d:
        r0 = r6.getActionMasked();
        if (r0 == 0) goto L_0x0014;
    L_0x0013:
        return r1;
    L_0x0014:
        r5.removeAllCallbacks();
        r5.hideSoftInput();
        r2 = r6.getY();
        r5.mLastDownEventY = r2;
        r5.mLastDownOrMoveEventY = r2;
        r2 = r6.getEventTime();
        r5.mLastDownEventTime = r2;
        r5.mIgnoreMoveEvents = r1;
        r5.mPerformClickOnTap = r1;
        r2 = r5.mLastDownEventY;
        r3 = r5.mTopSelectionDividerTop;
        r3 = (float) r3;
        r3 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        r4 = 1;
        if (r3 >= 0) goto L_0x0041;
    L_0x0036:
        r2 = r5.mScrollState;
        if (r2 != 0) goto L_0x0051;
    L_0x003a:
        r2 = r5.mPressedStateHelper;
        r3 = 2;
        r2.buttonPressDelayed(r3);
        goto L_0x0051;
    L_0x0041:
        r3 = r5.mBottomSelectionDividerBottom;
        r3 = (float) r3;
        r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r2 <= 0) goto L_0x0051;
    L_0x0048:
        r2 = r5.mScrollState;
        if (r2 != 0) goto L_0x0051;
    L_0x004c:
        r2 = r5.mPressedStateHelper;
        r2.buttonPressDelayed(r4);
    L_0x0051:
        r2 = r5.getParent();
        r2.requestDisallowInterceptTouchEvent(r4);
        r2 = r5.mFlingScroller;
        r2 = r2.isFinished();
        if (r2 != 0) goto L_0x006e;
    L_0x0060:
        r2 = r5.mFlingScroller;
        r2.forceFinished(r4);
        r2 = r5.mAdjustScroller;
        r2.forceFinished(r4);
        r5.onScrollStateChange(r1);
        goto L_0x00aa;
    L_0x006e:
        r2 = r5.mAdjustScroller;
        r2 = r2.isFinished();
        if (r2 != 0) goto L_0x0081;
    L_0x0076:
        r1 = r5.mFlingScroller;
        r1.forceFinished(r4);
        r1 = r5.mAdjustScroller;
        r1.forceFinished(r4);
        goto L_0x00aa;
    L_0x0081:
        r2 = r5.mLastDownEventY;
        r3 = r5.mTopSelectionDividerTop;
        r3 = (float) r3;
        r3 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r3 >= 0) goto L_0x0094;
        r2 = android.view.ViewConfiguration.getLongPressTimeout();
        r2 = (long) r2;
        r5.postChangeCurrentByOneFromLongPress(r1, r2);
        goto L_0x00aa;
    L_0x0094:
        r1 = r5.mBottomSelectionDividerBottom;
        r1 = (float) r1;
        r1 = (r2 > r1 ? 1 : (r2 == r1 ? 0 : -1));
        if (r1 <= 0) goto L_0x00a5;
        r1 = android.view.ViewConfiguration.getLongPressTimeout();
        r1 = (long) r1;
        r5.postChangeCurrentByOneFromLongPress(r4, r1);
        goto L_0x00aa;
    L_0x00a5:
        r5.mPerformClickOnTap = r4;
        r5.postBeginSoftInputOnLongPressCommand();
    L_0x00aa:
        return r4;
    L_0x00ab:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.NumberPicker.onInterceptTouchEvent(android.view.MotionEvent):boolean");
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled() || !this.mHasSelectorWheel) {
            return false;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(event);
        int action = event.getActionMasked();
        if (action == 1) {
            removeBeginSoftInputCommand();
            removeChangeCurrentByOneFromLongPress();
            this.mPressedStateHelper.cancel();
            VelocityTracker velocityTracker = this.mVelocityTracker;
            velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumFlingVelocity);
            int initialVelocity = (int) velocityTracker.getYVelocity();
            if (Math.abs(initialVelocity) > this.mMinimumFlingVelocity) {
                fling(initialVelocity);
                onScrollStateChange(2);
            } else {
                int eventY = (int) event.getY();
                long deltaTime = event.getEventTime() - this.mLastDownEventTime;
                if (((int) Math.abs(((float) eventY) - this.mLastDownEventY)) > this.mTouchSlop || deltaTime >= ((long) ViewConfiguration.getTapTimeout())) {
                    ensureScrollWheelAdjusted();
                } else if (this.mPerformClickOnTap) {
                    this.mPerformClickOnTap = false;
                    performClick();
                } else {
                    int selectorIndexOffset = (eventY / this.mSelectorElementHeight) - 1;
                    if (selectorIndexOffset > 0) {
                        changeValueByOne(true);
                        this.mPressedStateHelper.buttonTapped(1);
                    } else if (selectorIndexOffset < 0) {
                        changeValueByOne(false);
                        this.mPressedStateHelper.buttonTapped(2);
                    }
                }
                onScrollStateChange(0);
            }
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        } else if (action == 2 && !this.mIgnoreMoveEvents) {
            float currentMoveY = event.getY();
            if (this.mScrollState == 1) {
                scrollBy(0, (int) (currentMoveY - this.mLastDownOrMoveEventY));
                invalidate();
            } else if (((int) Math.abs(currentMoveY - this.mLastDownEventY)) > this.mTouchSlop) {
                removeAllCallbacks();
                onScrollStateChange(1);
            }
            this.mLastDownOrMoveEventY = currentMoveY;
        }
        return true;
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        if (action == 1 || action == 3) {
            removeAllCallbacks();
        }
        return super.dispatchTouchEvent(event);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == 19 || keyCode == 20) {
            if (this.mHasSelectorWheel) {
                int action = event.getAction();
                if (action == 0) {
                    if (!this.mWrapSelectorWheel) {
                        if (keyCode == 20) {
                        }
                    }
                    requestFocus();
                    this.mLastHandledDownDpadKeyCode = keyCode;
                    removeAllCallbacks();
                    if (this.mFlingScroller.isFinished()) {
                        changeValueByOne(keyCode == 20);
                    }
                    return true;
                } else if (action == 1 && this.mLastHandledDownDpadKeyCode == keyCode) {
                    this.mLastHandledDownDpadKeyCode = -1;
                    return true;
                }
            }
        } else if (keyCode == 23 || keyCode == 66) {
            removeAllCallbacks();
        }
        return super.dispatchKeyEvent(event);
    }

    public boolean dispatchTrackballEvent(MotionEvent event) {
        int action = event.getActionMasked();
        if (action == 1 || action == 3) {
            removeAllCallbacks();
        }
        return super.dispatchTrackballEvent(event);
    }

    /* Access modifiers changed, original: protected */
    public boolean dispatchHoverEvent(MotionEvent event) {
        if (!this.mHasSelectorWheel) {
            return super.dispatchHoverEvent(event);
        }
        if (AccessibilityManager.getInstance(this.mContext).isEnabled()) {
            int hoveredVirtualViewId;
            int eventY = (int) event.getY();
            if (eventY < this.mTopSelectionDividerTop) {
                hoveredVirtualViewId = 3;
            } else if (eventY > this.mBottomSelectionDividerBottom) {
                hoveredVirtualViewId = 1;
            } else {
                hoveredVirtualViewId = 2;
            }
            int action = event.getActionMasked();
            AccessibilityNodeProviderImpl provider = (AccessibilityNodeProviderImpl) getAccessibilityNodeProvider();
            if (action == 7) {
                int i = this.mLastHoveredChildVirtualViewId;
                if (!(i == hoveredVirtualViewId || i == -1)) {
                    provider.sendAccessibilityEventForVirtualView(i, 256);
                    provider.sendAccessibilityEventForVirtualView(hoveredVirtualViewId, 128);
                    this.mLastHoveredChildVirtualViewId = hoveredVirtualViewId;
                    provider.performAction(hoveredVirtualViewId, 64, null);
                }
            } else if (action == 9) {
                provider.sendAccessibilityEventForVirtualView(hoveredVirtualViewId, 128);
                this.mLastHoveredChildVirtualViewId = hoveredVirtualViewId;
                provider.performAction(hoveredVirtualViewId, 64, null);
            } else if (action == 10) {
                provider.sendAccessibilityEventForVirtualView(hoveredVirtualViewId, 256);
                this.mLastHoveredChildVirtualViewId = -1;
            }
        }
        return false;
    }

    public void computeScroll() {
        Scroller scroller = this.mFlingScroller;
        if (scroller.isFinished()) {
            scroller = this.mAdjustScroller;
            if (scroller.isFinished()) {
                return;
            }
        }
        scroller.computeScrollOffset();
        int currentScrollerY = scroller.getCurrY();
        if (this.mPreviousScrollerY == 0) {
            this.mPreviousScrollerY = scroller.getStartY();
        }
        scrollBy(0, currentScrollerY - this.mPreviousScrollerY);
        this.mPreviousScrollerY = currentScrollerY;
        if (scroller.isFinished()) {
            onScrollerFinished(scroller);
        } else {
            invalidate();
        }
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!this.mHasSelectorWheel) {
            this.mIncrementButton.setEnabled(enabled);
        }
        if (!this.mHasSelectorWheel) {
            this.mDecrementButton.setEnabled(enabled);
        }
        this.mInputText.setEnabled(enabled);
    }

    public void scrollBy(int x, int y) {
        int[] selectorIndices = this.mSelectorIndices;
        int startScrollOffset = this.mCurrentScrollOffset;
        if (!this.mWrapSelectorWheel && y > 0 && selectorIndices[1] <= this.mMinValue) {
            this.mCurrentScrollOffset = this.mInitialScrollOffset;
        } else if (this.mWrapSelectorWheel || y >= 0 || selectorIndices[1] < this.mMaxValue) {
            int i;
            this.mCurrentScrollOffset += y;
            while (true) {
                i = this.mCurrentScrollOffset;
                if (i - this.mInitialScrollOffset <= this.mSelectorTextGapHeight) {
                    break;
                }
                this.mCurrentScrollOffset = i - this.mSelectorElementHeight;
                decrementSelectorIndices(selectorIndices);
                setValueInternal(selectorIndices[1], true);
                if (!this.mWrapSelectorWheel && selectorIndices[1] <= this.mMinValue) {
                    this.mCurrentScrollOffset = this.mInitialScrollOffset;
                }
            }
            while (true) {
                i = this.mCurrentScrollOffset;
                if (i - this.mInitialScrollOffset >= (-this.mSelectorTextGapHeight)) {
                    break;
                }
                this.mCurrentScrollOffset = i + this.mSelectorElementHeight;
                incrementSelectorIndices(selectorIndices);
                setValueInternal(selectorIndices[1], true);
                if (!this.mWrapSelectorWheel && selectorIndices[1] >= this.mMaxValue) {
                    this.mCurrentScrollOffset = this.mInitialScrollOffset;
                }
            }
            if (startScrollOffset != i) {
                onScrollChanged(0, i, 0, startScrollOffset);
            }
        } else {
            this.mCurrentScrollOffset = this.mInitialScrollOffset;
        }
    }

    /* Access modifiers changed, original: protected */
    public int computeVerticalScrollOffset() {
        return this.mCurrentScrollOffset;
    }

    /* Access modifiers changed, original: protected */
    public int computeVerticalScrollRange() {
        return ((this.mMaxValue - this.mMinValue) + 1) * this.mSelectorElementHeight;
    }

    /* Access modifiers changed, original: protected */
    public int computeVerticalScrollExtent() {
        return getHeight();
    }

    public int getSolidColor() {
        return this.mSolidColor;
    }

    public void setOnValueChangedListener(OnValueChangeListener onValueChangedListener) {
        this.mOnValueChangeListener = onValueChangedListener;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }

    public void setFormatter(Formatter formatter) {
        if (formatter != this.mFormatter) {
            this.mFormatter = formatter;
            initializeSelectorWheelIndices();
            updateInputTextView();
        }
    }

    public void setValue(int value) {
        setValueInternal(value, false);
    }

    public boolean performClick() {
        if (!this.mHasSelectorWheel) {
            return super.performClick();
        }
        if (!super.performClick()) {
            showSoftInput();
        }
        return true;
    }

    public boolean performLongClick() {
        if (!this.mHasSelectorWheel) {
            return super.performLongClick();
        }
        if (!super.performLongClick()) {
            showSoftInput();
            this.mIgnoreMoveEvents = true;
        }
        return true;
    }

    private void showSoftInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(InputMethodManager.class);
        if (inputMethodManager != null) {
            if (this.mHasSelectorWheel) {
                this.mInputText.setVisibility(0);
            }
            this.mInputText.requestFocus();
            inputMethodManager.showSoftInput(this.mInputText, 0);
        }
    }

    private void hideSoftInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(InputMethodManager.class);
        if (inputMethodManager != null && inputMethodManager.isActive(this.mInputText)) {
            inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
        }
        if (this.mHasSelectorWheel) {
            this.mInputText.setVisibility(4);
        }
    }

    private void tryComputeMaxWidth() {
        if (this.mComputeMaxWidth) {
            int maxTextWidth = 0;
            int valueCount = this.mDisplayedValues;
            int i;
            float digitWidth;
            if (valueCount == 0) {
                float maxDigitWidth = 0.0f;
                for (i = 0; i <= 9; i++) {
                    digitWidth = this.mSelectorWheelPaint.measureText(formatNumberWithLocale(i));
                    if (digitWidth > maxDigitWidth) {
                        maxDigitWidth = digitWidth;
                    }
                }
                i = 0;
                for (int current = this.mMaxValue; current > 0; current /= 10) {
                    i++;
                }
                maxTextWidth = (int) (((float) i) * maxDigitWidth);
            } else {
                valueCount = valueCount.length;
                for (i = 0; i < valueCount; i++) {
                    digitWidth = this.mSelectorWheelPaint.measureText(this.mDisplayedValues[i]);
                    if (digitWidth > ((float) maxTextWidth)) {
                        maxTextWidth = (int) digitWidth;
                    }
                }
            }
            maxTextWidth += this.mInputText.getPaddingLeft() + this.mInputText.getPaddingRight();
            if (this.mMaxWidth != maxTextWidth) {
                valueCount = this.mMinWidth;
                if (maxTextWidth > valueCount) {
                    this.mMaxWidth = maxTextWidth;
                } else {
                    this.mMaxWidth = valueCount;
                }
                invalidate();
            }
        }
    }

    public boolean getWrapSelectorWheel() {
        return this.mWrapSelectorWheel;
    }

    public void setWrapSelectorWheel(boolean wrapSelectorWheel) {
        this.mWrapSelectorWheelPreferred = wrapSelectorWheel;
        updateWrapSelectorWheel();
    }

    private void updateWrapSelectorWheel() {
        boolean z = true;
        if (!((this.mMaxValue - this.mMinValue >= this.mSelectorIndices.length) && this.mWrapSelectorWheelPreferred)) {
            z = false;
        }
        this.mWrapSelectorWheel = z;
    }

    public void setOnLongPressUpdateInterval(long intervalMillis) {
        this.mLongPressUpdateInterval = intervalMillis;
    }

    public int getValue() {
        return this.mValue;
    }

    public int getMinValue() {
        return this.mMinValue;
    }

    public void setMinValue(int minValue) {
        if (this.mMinValue != minValue) {
            if (minValue >= 0) {
                this.mMinValue = minValue;
                int i = this.mMinValue;
                if (i > this.mValue) {
                    this.mValue = i;
                }
                updateWrapSelectorWheel();
                initializeSelectorWheelIndices();
                updateInputTextView();
                tryComputeMaxWidth();
                invalidate();
                return;
            }
            throw new IllegalArgumentException("minValue must be >= 0");
        }
    }

    public int getMaxValue() {
        return this.mMaxValue;
    }

    public void setMaxValue(int maxValue) {
        if (this.mMaxValue != maxValue) {
            if (maxValue >= 0) {
                this.mMaxValue = maxValue;
                int i = this.mMaxValue;
                if (i < this.mValue) {
                    this.mValue = i;
                }
                updateWrapSelectorWheel();
                initializeSelectorWheelIndices();
                updateInputTextView();
                tryComputeMaxWidth();
                invalidate();
                return;
            }
            throw new IllegalArgumentException("maxValue must be >= 0");
        }
    }

    public String[] getDisplayedValues() {
        return this.mDisplayedValues;
    }

    public void setDisplayedValues(String[] displayedValues) {
        if (this.mDisplayedValues != displayedValues) {
            this.mDisplayedValues = displayedValues;
            if (this.mDisplayedValues != null) {
                this.mInputText.setRawInputType(ConnectivityManager.CALLBACK_PRECHECK);
            } else {
                this.mInputText.setRawInputType(2);
            }
            updateInputTextView();
            initializeSelectorWheelIndices();
            tryComputeMaxWidth();
        }
    }

    public CharSequence getDisplayedValueForCurrentSelection() {
        return (CharSequence) this.mSelectorIndexToStringCache.get(getValue());
    }

    public void setSelectionDividerHeight(int height) {
        this.mSelectionDividerHeight = height;
        invalidate();
    }

    public int getSelectionDividerHeight() {
        return this.mSelectionDividerHeight;
    }

    /* Access modifiers changed, original: protected */
    public float getTopFadingEdgeStrength() {
        return TOP_AND_BOTTOM_FADING_EDGE_STRENGTH;
    }

    /* Access modifiers changed, original: protected */
    public float getBottomFadingEdgeStrength() {
        return TOP_AND_BOTTOM_FADING_EDGE_STRENGTH;
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeAllCallbacks();
    }

    /* Access modifiers changed, original: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable selectionDivider = this.mSelectionDivider;
        if (selectionDivider != null && selectionDivider.isStateful() && selectionDivider.setState(getDrawableState())) {
            invalidateDrawable(selectionDivider);
        }
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.mSelectionDivider;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
    }

    public void onResolveDrawables(int layoutDirection) {
        super.onResolveDrawables(layoutDirection);
        Drawable drawable = this.mSelectionDivider;
        if (drawable != null) {
            drawable.setLayoutDirection(layoutDirection);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        if (this.mHasSelectorWheel) {
            boolean showSelectorWheel = this.mHideWheelUntilFocused ? hasFocus() : true;
            float x = (float) ((this.mRight - this.mLeft) / 2);
            float y = (float) this.mCurrentScrollOffset;
            if (showSelectorWheel) {
                Drawable drawable = this.mVirtualButtonPressedDrawable;
                if (drawable != null && this.mScrollState == 0) {
                    if (this.mDecrementVirtualButtonPressed) {
                        drawable.setState(PRESSED_STATE_SET);
                        this.mVirtualButtonPressedDrawable.setBounds(0, 0, this.mRight, this.mTopSelectionDividerTop);
                        this.mVirtualButtonPressedDrawable.draw(canvas);
                    }
                    if (this.mIncrementVirtualButtonPressed) {
                        this.mVirtualButtonPressedDrawable.setState(PRESSED_STATE_SET);
                        this.mVirtualButtonPressedDrawable.setBounds(0, this.mBottomSelectionDividerBottom, this.mRight, this.mBottom);
                        this.mVirtualButtonPressedDrawable.draw(canvas);
                    }
                }
            }
            int[] selectorIndices = this.mSelectorIndices;
            int i = 0;
            while (i < selectorIndices.length) {
                String scrollSelectorValue = (String) this.mSelectorIndexToStringCache.get(selectorIndices[i]);
                if ((showSelectorWheel && i != 1) || (i == 1 && this.mInputText.getVisibility() != 0)) {
                    canvas.drawText(scrollSelectorValue, x, y, this.mSelectorWheelPaint);
                }
                y += (float) this.mSelectorElementHeight;
                i++;
            }
            if (showSelectorWheel) {
                Drawable drawable2 = this.mSelectionDivider;
                if (drawable2 != null) {
                    i = this.mTopSelectionDividerTop;
                    drawable2.setBounds(0, i, this.mRight, this.mSelectionDividerHeight + i);
                    this.mSelectionDivider.draw(canvas);
                    int bottomOfBottomDivider = this.mBottomSelectionDividerBottom;
                    this.mSelectionDivider.setBounds(0, bottomOfBottomDivider - this.mSelectionDividerHeight, this.mRight, bottomOfBottomDivider);
                    this.mSelectionDivider.draw(canvas);
                }
            }
            return;
        }
        super.onDraw(canvas);
    }

    public void onInitializeAccessibilityEventInternal(AccessibilityEvent event) {
        super.onInitializeAccessibilityEventInternal(event);
        event.setClassName(NumberPicker.class.getName());
        event.setScrollable(true);
        event.setScrollY((this.mMinValue + this.mValue) * this.mSelectorElementHeight);
        event.setMaxScrollY((this.mMaxValue - this.mMinValue) * this.mSelectorElementHeight);
    }

    public AccessibilityNodeProvider getAccessibilityNodeProvider() {
        if (!this.mHasSelectorWheel) {
            return super.getAccessibilityNodeProvider();
        }
        if (this.mAccessibilityNodeProvider == null) {
            this.mAccessibilityNodeProvider = new AccessibilityNodeProviderImpl();
        }
        return this.mAccessibilityNodeProvider;
    }

    public void setTextColor(int color) {
        this.mSelectorWheelPaint.setColor(color);
        this.mInputText.setTextColor(color);
        invalidate();
    }

    public int getTextColor() {
        return this.mSelectorWheelPaint.getColor();
    }

    public void setTextSize(float size) {
        this.mSelectorWheelPaint.setTextSize(size);
        this.mInputText.setTextSize(0, size);
        invalidate();
    }

    public float getTextSize() {
        return this.mSelectorWheelPaint.getTextSize();
    }

    private int makeMeasureSpec(int measureSpec, int maxSize) {
        if (maxSize == -1) {
            return measureSpec;
        }
        int size = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);
        if (mode == Integer.MIN_VALUE) {
            return MeasureSpec.makeMeasureSpec(Math.min(size, maxSize), 1073741824);
        }
        if (mode == 0) {
            return MeasureSpec.makeMeasureSpec(maxSize, 1073741824);
        }
        if (mode == 1073741824) {
            return measureSpec;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown measure mode: ");
        stringBuilder.append(mode);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private int resolveSizeAndStateRespectingMinSize(int minSize, int measuredSize, int measureSpec) {
        if (minSize != -1) {
            return View.resolveSizeAndState(Math.max(minSize, measuredSize), measureSpec, 0);
        }
        return measuredSize;
    }

    @UnsupportedAppUsage
    private void initializeSelectorWheelIndices() {
        this.mSelectorIndexToStringCache.clear();
        int[] selectorIndices = this.mSelectorIndices;
        int current = getValue();
        for (int i = 0; i < this.mSelectorIndices.length; i++) {
            int selectorIndex = (i - 1) + current;
            if (this.mWrapSelectorWheel) {
                selectorIndex = getWrappedSelectorIndex(selectorIndex);
            }
            selectorIndices[i] = selectorIndex;
            ensureCachedScrollSelectorValue(selectorIndices[i]);
        }
    }

    private void setValueInternal(int current, boolean notifyChange) {
        if (this.mValue != current) {
            if (this.mWrapSelectorWheel) {
                current = getWrappedSelectorIndex(current);
            } else {
                current = Math.min(Math.max(current, this.mMinValue), this.mMaxValue);
            }
            int previous = this.mValue;
            this.mValue = current;
            if (this.mScrollState != 2) {
                updateInputTextView();
            }
            if (notifyChange) {
                notifyChange(previous, current);
            }
            initializeSelectorWheelIndices();
            invalidate();
        }
    }

    @UnsupportedAppUsage
    private void changeValueByOne(boolean increment) {
        if (this.mHasSelectorWheel) {
            hideSoftInput();
            if (!moveToFinalScrollerPosition(this.mFlingScroller)) {
                moveToFinalScrollerPosition(this.mAdjustScroller);
            }
            this.mPreviousScrollerY = 0;
            if (increment) {
                this.mFlingScroller.startScroll(0, 0, 0, -this.mSelectorElementHeight, 300);
            } else {
                this.mFlingScroller.startScroll(0, 0, 0, this.mSelectorElementHeight, 300);
            }
            invalidate();
        } else if (increment) {
            setValueInternal(this.mValue + 1, true);
        } else {
            setValueInternal(this.mValue - 1, true);
        }
    }

    private void initializeSelectorWheel() {
        initializeSelectorWheelIndices();
        int[] selectorIndices = this.mSelectorIndices;
        this.mSelectorTextGapHeight = (int) ((((float) ((this.mBottom - this.mTop) - (selectorIndices.length * this.mTextSize))) / ((float) selectorIndices.length)) + 0.5f);
        this.mSelectorElementHeight = this.mTextSize + this.mSelectorTextGapHeight;
        this.mInitialScrollOffset = (this.mInputText.getBaseline() + this.mInputText.getTop()) - (this.mSelectorElementHeight * 1);
        this.mCurrentScrollOffset = this.mInitialScrollOffset;
        updateInputTextView();
    }

    private void initializeFadingEdges() {
        setVerticalFadingEdgeEnabled(true);
        setFadingEdgeLength(((this.mBottom - this.mTop) - this.mTextSize) / 2);
    }

    private void onScrollerFinished(Scroller scroller) {
        if (scroller == this.mFlingScroller) {
            ensureScrollWheelAdjusted();
            updateInputTextView();
            onScrollStateChange(0);
        } else if (this.mScrollState != 1) {
            updateInputTextView();
        }
    }

    private void onScrollStateChange(int scrollState) {
        if (this.mScrollState != scrollState) {
            this.mScrollState = scrollState;
            OnScrollListener onScrollListener = this.mOnScrollListener;
            if (onScrollListener != null) {
                onScrollListener.onScrollStateChange(this, scrollState);
            }
        }
    }

    private void fling(int velocityY) {
        this.mPreviousScrollerY = 0;
        if (velocityY > 0) {
            this.mFlingScroller.fling(0, 0, 0, velocityY, 0, 0, 0, Integer.MAX_VALUE);
        } else {
            this.mFlingScroller.fling(0, Integer.MAX_VALUE, 0, velocityY, 0, 0, 0, Integer.MAX_VALUE);
        }
        invalidate();
    }

    private int getWrappedSelectorIndex(int selectorIndex) {
        int i = this.mMaxValue;
        int i2;
        if (selectorIndex > i) {
            i2 = this.mMinValue;
            return (i2 + ((selectorIndex - i) % (i - i2))) - 1;
        }
        i2 = this.mMinValue;
        if (selectorIndex < i2) {
            return (i - ((i2 - selectorIndex) % (i - i2))) + 1;
        }
        return selectorIndex;
    }

    private void incrementSelectorIndices(int[] selectorIndices) {
        int i;
        for (i = 0; i < selectorIndices.length - 1; i++) {
            selectorIndices[i] = selectorIndices[i + 1];
        }
        i = selectorIndices[selectorIndices.length - 2] + 1;
        if (this.mWrapSelectorWheel && i > this.mMaxValue) {
            i = this.mMinValue;
        }
        selectorIndices[selectorIndices.length - 1] = i;
        ensureCachedScrollSelectorValue(i);
    }

    private void decrementSelectorIndices(int[] selectorIndices) {
        int i;
        for (i = selectorIndices.length - 1; i > 0; i--) {
            selectorIndices[i] = selectorIndices[i - 1];
        }
        i = selectorIndices[1] - 1;
        if (this.mWrapSelectorWheel && i < this.mMinValue) {
            i = this.mMaxValue;
        }
        selectorIndices[0] = i;
        ensureCachedScrollSelectorValue(i);
    }

    private void ensureCachedScrollSelectorValue(int selectorIndex) {
        SparseArray<String> cache = this.mSelectorIndexToStringCache;
        if (((String) cache.get(selectorIndex)) == null) {
            String scrollSelectorValue;
            int displayedValueIndex = this.mMinValue;
            if (selectorIndex < displayedValueIndex || selectorIndex > this.mMaxValue) {
                scrollSelectorValue = "";
            } else {
                String[] strArr = this.mDisplayedValues;
                if (strArr != null) {
                    scrollSelectorValue = strArr[selectorIndex - displayedValueIndex];
                } else {
                    scrollSelectorValue = formatNumber(selectorIndex);
                }
            }
            cache.put(selectorIndex, scrollSelectorValue);
        }
    }

    private String formatNumber(int value) {
        Formatter formatter = this.mFormatter;
        return formatter != null ? formatter.format(value) : formatNumberWithLocale(value);
    }

    private void validateInputTextView(View v) {
        String str = String.valueOf(((TextView) v).getText());
        if (TextUtils.isEmpty(str)) {
            updateInputTextView();
        } else {
            setValueInternal(getSelectedPos(str.toString()), true);
        }
    }

    private boolean updateInputTextView() {
        String text;
        String[] strArr = this.mDisplayedValues;
        if (strArr == null) {
            text = formatNumber(this.mValue);
        } else {
            text = strArr[this.mValue - this.mMinValue];
        }
        if (!TextUtils.isEmpty(text)) {
            CharSequence beforeText = this.mInputText.getText();
            if (!text.equals(beforeText.toString())) {
                this.mInputText.setText((CharSequence) text);
                if (AccessibilityManager.getInstance(this.mContext).isEnabled()) {
                    AccessibilityEvent event = AccessibilityEvent.obtain(16);
                    this.mInputText.onInitializeAccessibilityEvent(event);
                    this.mInputText.onPopulateAccessibilityEvent(event);
                    event.setFromIndex(0);
                    event.setRemovedCount(beforeText.length());
                    event.setAddedCount(text.length());
                    event.setBeforeText(beforeText);
                    event.setSource(this, 2);
                    requestSendAccessibilityEvent(this, event);
                }
                return true;
            }
        }
        return false;
    }

    private void notifyChange(int previous, int current) {
        OnValueChangeListener onValueChangeListener = this.mOnValueChangeListener;
        if (onValueChangeListener != null) {
            onValueChangeListener.onValueChange(this, previous, this.mValue);
        }
    }

    private void postChangeCurrentByOneFromLongPress(boolean increment, long delayMillis) {
        ChangeCurrentByOneFromLongPressCommand changeCurrentByOneFromLongPressCommand = this.mChangeCurrentByOneFromLongPressCommand;
        if (changeCurrentByOneFromLongPressCommand == null) {
            this.mChangeCurrentByOneFromLongPressCommand = new ChangeCurrentByOneFromLongPressCommand();
        } else {
            removeCallbacks(changeCurrentByOneFromLongPressCommand);
        }
        this.mChangeCurrentByOneFromLongPressCommand.setStep(increment);
        postDelayed(this.mChangeCurrentByOneFromLongPressCommand, delayMillis);
    }

    private void removeChangeCurrentByOneFromLongPress() {
        ChangeCurrentByOneFromLongPressCommand changeCurrentByOneFromLongPressCommand = this.mChangeCurrentByOneFromLongPressCommand;
        if (changeCurrentByOneFromLongPressCommand != null) {
            removeCallbacks(changeCurrentByOneFromLongPressCommand);
        }
    }

    private void postBeginSoftInputOnLongPressCommand() {
        BeginSoftInputOnLongPressCommand beginSoftInputOnLongPressCommand = this.mBeginSoftInputOnLongPressCommand;
        if (beginSoftInputOnLongPressCommand == null) {
            this.mBeginSoftInputOnLongPressCommand = new BeginSoftInputOnLongPressCommand();
        } else {
            removeCallbacks(beginSoftInputOnLongPressCommand);
        }
        postDelayed(this.mBeginSoftInputOnLongPressCommand, (long) ViewConfiguration.getLongPressTimeout());
    }

    private void removeBeginSoftInputCommand() {
        BeginSoftInputOnLongPressCommand beginSoftInputOnLongPressCommand = this.mBeginSoftInputOnLongPressCommand;
        if (beginSoftInputOnLongPressCommand != null) {
            removeCallbacks(beginSoftInputOnLongPressCommand);
        }
    }

    private void removeAllCallbacks() {
        ChangeCurrentByOneFromLongPressCommand changeCurrentByOneFromLongPressCommand = this.mChangeCurrentByOneFromLongPressCommand;
        if (changeCurrentByOneFromLongPressCommand != null) {
            removeCallbacks(changeCurrentByOneFromLongPressCommand);
        }
        SetSelectionCommand setSelectionCommand = this.mSetSelectionCommand;
        if (setSelectionCommand != null) {
            setSelectionCommand.cancel();
        }
        BeginSoftInputOnLongPressCommand beginSoftInputOnLongPressCommand = this.mBeginSoftInputOnLongPressCommand;
        if (beginSoftInputOnLongPressCommand != null) {
            removeCallbacks(beginSoftInputOnLongPressCommand);
        }
        this.mPressedStateHelper.cancel();
    }

    private int getSelectedPos(String value) {
        if (this.mDisplayedValues == null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return this.mMinValue;
            }
        }
        for (int i = 0; i < this.mDisplayedValues.length; i++) {
            value = value.toLowerCase();
            if (this.mDisplayedValues[i].toLowerCase().startsWith(value)) {
                return this.mMinValue + i;
            }
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e2) {
            return this.mMinValue;
        }
    }

    private void postSetSelectionCommand(int selectionStart, int selectionEnd) {
        if (this.mSetSelectionCommand == null) {
            this.mSetSelectionCommand = new SetSelectionCommand(this.mInputText);
        }
        this.mSetSelectionCommand.post(selectionStart, selectionEnd);
    }

    private boolean ensureScrollWheelAdjusted() {
        int deltaY = this.mInitialScrollOffset - this.mCurrentScrollOffset;
        if (deltaY == 0) {
            return false;
        }
        this.mPreviousScrollerY = 0;
        int abs = Math.abs(deltaY);
        int i = this.mSelectorElementHeight;
        if (abs > i / 2) {
            if (deltaY > 0) {
                i = -i;
            }
            deltaY += i;
        }
        this.mAdjustScroller.startScroll(0, 0, 0, deltaY, 800);
        invalidate();
        return true;
    }

    private static String formatNumberWithLocale(int value) {
        return String.format(Locale.getDefault(), "%d", new Object[]{Integer.valueOf(value)});
    }
}
