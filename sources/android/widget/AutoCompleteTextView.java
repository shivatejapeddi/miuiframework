package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.KeyEvent.DispatcherState;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inspector.InspectionCompanion.UninitializedPropertyMapException;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Filter.FilterListener;
import com.android.internal.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

public class AutoCompleteTextView extends EditText implements FilterListener {
    static final boolean DEBUG = false;
    static final int EXPAND_MAX = 3;
    static final String TAG = "AutoCompleteTextView";
    private ListAdapter mAdapter;
    private MyWatcher mAutoCompleteTextWatcher;
    private boolean mBlockCompletion;
    private int mDropDownAnchorId;
    private boolean mDropDownDismissedOnCompletion;
    private Filter mFilter;
    private int mHintResource;
    private CharSequence mHintText;
    @UnsupportedAppUsage
    private TextView mHintView;
    private OnItemClickListener mItemClickListener;
    private OnItemSelectedListener mItemSelectedListener;
    private int mLastKeyCode;
    @UnsupportedAppUsage
    private PopupDataSetObserver mObserver;
    @UnsupportedAppUsage
    private final PassThroughClickListener mPassThroughClickListener;
    @UnsupportedAppUsage
    private final ListPopupWindow mPopup;
    private boolean mPopupCanBeUpdated;
    private final Context mPopupContext;
    private int mThreshold;
    private Validator mValidator;

    private class DropDownItemClickListener implements OnItemClickListener {
        private DropDownItemClickListener() {
        }

        /* synthetic */ DropDownItemClickListener(AutoCompleteTextView x0, AnonymousClass1 x1) {
            this();
        }

        public void onItemClick(AdapterView parent, View v, int position, long id) {
            AutoCompleteTextView.this.performCompletion(v, position, id);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface InputMethodMode {
    }

    public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<AutoCompleteTextView> {
        private int mCompletionHintId;
        private int mCompletionThresholdId;
        private int mDropDownHeightId;
        private int mDropDownHorizontalOffsetId;
        private int mDropDownVerticalOffsetId;
        private int mDropDownWidthId;
        private int mPopupBackgroundId;
        private boolean mPropertiesMapped = false;

        public void mapProperties(PropertyMapper propertyMapper) {
            this.mCompletionHintId = propertyMapper.mapObject("completionHint", 16843122);
            this.mCompletionThresholdId = propertyMapper.mapInt("completionThreshold", 16843124);
            this.mDropDownHeightId = propertyMapper.mapInt("dropDownHeight", 16843395);
            this.mDropDownHorizontalOffsetId = propertyMapper.mapInt("dropDownHorizontalOffset", 16843436);
            this.mDropDownVerticalOffsetId = propertyMapper.mapInt("dropDownVerticalOffset", 16843437);
            this.mDropDownWidthId = propertyMapper.mapInt("dropDownWidth", 16843362);
            this.mPopupBackgroundId = propertyMapper.mapObject("popupBackground", 16843126);
            this.mPropertiesMapped = true;
        }

        public void readProperties(AutoCompleteTextView node, PropertyReader propertyReader) {
            if (this.mPropertiesMapped) {
                propertyReader.readObject(this.mCompletionHintId, node.getCompletionHint());
                propertyReader.readInt(this.mCompletionThresholdId, node.getThreshold());
                propertyReader.readInt(this.mDropDownHeightId, node.getDropDownHeight());
                propertyReader.readInt(this.mDropDownHorizontalOffsetId, node.getDropDownHorizontalOffset());
                propertyReader.readInt(this.mDropDownVerticalOffsetId, node.getDropDownVerticalOffset());
                propertyReader.readInt(this.mDropDownWidthId, node.getDropDownWidth());
                propertyReader.readObject(this.mPopupBackgroundId, node.getDropDownBackground());
                return;
            }
            throw new UninitializedPropertyMapException();
        }
    }

    private class MyWatcher implements TextWatcher {
        private boolean mOpenBefore;

        private MyWatcher() {
        }

        /* synthetic */ MyWatcher(AutoCompleteTextView x0, AnonymousClass1 x1) {
            this();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (!AutoCompleteTextView.this.mBlockCompletion) {
                this.mOpenBefore = AutoCompleteTextView.this.isPopupShowing();
            }
        }

        public void afterTextChanged(Editable s) {
            if (!AutoCompleteTextView.this.mBlockCompletion) {
                if (!this.mOpenBefore || AutoCompleteTextView.this.isPopupShowing()) {
                    AutoCompleteTextView.this.refreshAutoCompleteResults();
                }
            }
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    private class PassThroughClickListener implements OnClickListener {
        private OnClickListener mWrapped;

        private PassThroughClickListener() {
        }

        /* synthetic */ PassThroughClickListener(AutoCompleteTextView x0, AnonymousClass1 x1) {
            this();
        }

        public void onClick(View v) {
            AutoCompleteTextView.this.onClickImpl();
            OnClickListener onClickListener = this.mWrapped;
            if (onClickListener != null) {
                onClickListener.onClick(v);
            }
        }
    }

    private static class PopupDataSetObserver extends DataSetObserver {
        private final WeakReference<AutoCompleteTextView> mViewReference;
        private final Runnable updateRunnable;

        /* synthetic */ PopupDataSetObserver(AutoCompleteTextView x0, AnonymousClass1 x1) {
            this(x0);
        }

        private PopupDataSetObserver(AutoCompleteTextView view) {
            this.updateRunnable = new Runnable() {
                public void run() {
                    AutoCompleteTextView textView = (AutoCompleteTextView) PopupDataSetObserver.this.mViewReference.get();
                    if (textView != null) {
                        ListAdapter adapter = textView.mAdapter;
                        if (adapter != null) {
                            textView.updateDropDownForFilter(adapter.getCount());
                        }
                    }
                }
            };
            this.mViewReference = new WeakReference(view);
        }

        public void onChanged() {
            AutoCompleteTextView textView = (AutoCompleteTextView) this.mViewReference.get();
            if (textView != null && textView.mAdapter != null) {
                textView.post(this.updateRunnable);
            }
        }
    }

    public interface Validator {
        CharSequence fixText(CharSequence charSequence);

        boolean isValid(CharSequence charSequence);
    }

    public AutoCompleteTextView(Context context) {
        this(context, null);
    }

    public AutoCompleteTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 16842859);
    }

    public AutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr, defStyleRes, null);
    }

    public AutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, Theme popupTheme) {
        TypedArray pa;
        Context context2 = context;
        AttributeSet attributeSet = attrs;
        int i = defStyleAttr;
        int i2 = defStyleRes;
        Theme theme = popupTheme;
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mDropDownDismissedOnCompletion = true;
        this.mLastKeyCode = 0;
        this.mValidator = null;
        this.mPopupCanBeUpdated = true;
        TypedArray a = context2.obtainStyledAttributes(attributeSet, R.styleable.AutoCompleteTextView, i, i2);
        TypedArray a2 = a;
        saveAttributeDataForStyleable(context, R.styleable.AutoCompleteTextView, attrs, a, defStyleAttr, defStyleRes);
        if (theme != null) {
            this.mPopupContext = new ContextThemeWrapper(context2, theme);
        } else {
            int popupThemeResId = a2.getResourceId(8, 0);
            if (popupThemeResId != 0) {
                this.mPopupContext = new ContextThemeWrapper(context2, popupThemeResId);
            } else {
                this.mPopupContext = context2;
            }
        }
        Context context3 = this.mPopupContext;
        if (context3 != context2) {
            TypedArray pa2 = context3.obtainStyledAttributes(attributeSet, R.styleable.AutoCompleteTextView, i, i2);
            saveAttributeDataForStyleable(context, R.styleable.AutoCompleteTextView, attrs, a2, defStyleAttr, defStyleRes);
            pa = pa2;
        } else {
            pa = a2;
        }
        Drawable popupListSelector = pa.getDrawable(3);
        int popupWidth = pa.getLayoutDimension(5, -2);
        int popupHeight = pa.getLayoutDimension(7, -2);
        int popupHintLayoutResId = pa.getResourceId(1, R.layout.simple_dropdown_hint);
        CharSequence popupHintText = pa.getText(0);
        if (pa != a2) {
            pa.recycle();
        }
        this.mPopup = new ListPopupWindow(this.mPopupContext, attributeSet, i, i2);
        this.mPopup.setSoftInputMode(16);
        this.mPopup.setPromptPosition(1);
        this.mPopup.setListSelector(popupListSelector);
        this.mPopup.setOnItemClickListener(new DropDownItemClickListener(this, null));
        this.mPopup.setWidth(popupWidth);
        this.mPopup.setHeight(popupHeight);
        this.mHintResource = popupHintLayoutResId;
        setCompletionHint(popupHintText);
        this.mDropDownAnchorId = a2.getResourceId(6, -1);
        this.mThreshold = a2.getInt(2, 2);
        a2.recycle();
        int inputType = getInputType();
        if ((inputType & 15) == 1) {
            setRawInputType(inputType | 65536);
        }
        setFocusable(true);
        this.mAutoCompleteTextWatcher = new MyWatcher(this, null);
        addTextChangedListener(this.mAutoCompleteTextWatcher);
        this.mPassThroughClickListener = new PassThroughClickListener(this, null);
        super.setOnClickListener(this.mPassThroughClickListener);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.mPassThroughClickListener.mWrapped = listener;
    }

    private void onClickImpl() {
        if (isPopupShowing()) {
            ensureImeVisible(true);
        }
    }

    public void setCompletionHint(CharSequence hint) {
        this.mHintText = hint;
        if (hint != null) {
            TextView textView = this.mHintView;
            if (textView == null) {
                TextView hintView = (TextView) LayoutInflater.from(this.mPopupContext).inflate(this.mHintResource, null).findViewById(16908308);
                hintView.setText(this.mHintText);
                this.mHintView = hintView;
                this.mPopup.setPromptView(hintView);
                return;
            }
            textView.setText(hint);
            return;
        }
        this.mPopup.setPromptView(null);
        this.mHintView = null;
    }

    public CharSequence getCompletionHint() {
        return this.mHintText;
    }

    public int getDropDownWidth() {
        return this.mPopup.getWidth();
    }

    public void setDropDownWidth(int width) {
        this.mPopup.setWidth(width);
    }

    public int getDropDownHeight() {
        return this.mPopup.getHeight();
    }

    public void setDropDownHeight(int height) {
        this.mPopup.setHeight(height);
    }

    public int getDropDownAnchor() {
        return this.mDropDownAnchorId;
    }

    public void setDropDownAnchor(int id) {
        this.mDropDownAnchorId = id;
        this.mPopup.setAnchorView(null);
    }

    public Drawable getDropDownBackground() {
        return this.mPopup.getBackground();
    }

    public void setDropDownBackgroundDrawable(Drawable d) {
        this.mPopup.setBackgroundDrawable(d);
    }

    public void setDropDownBackgroundResource(int id) {
        this.mPopup.setBackgroundDrawable(getContext().getDrawable(id));
    }

    public void setDropDownVerticalOffset(int offset) {
        this.mPopup.setVerticalOffset(offset);
    }

    public int getDropDownVerticalOffset() {
        return this.mPopup.getVerticalOffset();
    }

    public void setDropDownHorizontalOffset(int offset) {
        this.mPopup.setHorizontalOffset(offset);
    }

    public int getDropDownHorizontalOffset() {
        return this.mPopup.getHorizontalOffset();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public void setDropDownAnimationStyle(int animationStyle) {
        this.mPopup.setAnimationStyle(animationStyle);
    }

    public int getDropDownAnimationStyle() {
        return this.mPopup.getAnimationStyle();
    }

    public boolean isDropDownAlwaysVisible() {
        return this.mPopup.isDropDownAlwaysVisible();
    }

    @UnsupportedAppUsage
    public void setDropDownAlwaysVisible(boolean dropDownAlwaysVisible) {
        this.mPopup.setDropDownAlwaysVisible(dropDownAlwaysVisible);
    }

    public boolean isDropDownDismissedOnCompletion() {
        return this.mDropDownDismissedOnCompletion;
    }

    @UnsupportedAppUsage
    public void setDropDownDismissedOnCompletion(boolean dropDownDismissedOnCompletion) {
        this.mDropDownDismissedOnCompletion = dropDownDismissedOnCompletion;
    }

    public int getThreshold() {
        return this.mThreshold;
    }

    public void setThreshold(int threshold) {
        if (threshold <= 0) {
            threshold = 1;
        }
        this.mThreshold = threshold;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.mItemClickListener = l;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener l) {
        this.mItemSelectedListener = l;
    }

    @Deprecated
    public OnItemClickListener getItemClickListener() {
        return this.mItemClickListener;
    }

    @Deprecated
    public OnItemSelectedListener getItemSelectedListener() {
        return this.mItemSelectedListener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return this.mItemClickListener;
    }

    public OnItemSelectedListener getOnItemSelectedListener() {
        return this.mItemSelectedListener;
    }

    public void setOnDismissListener(final OnDismissListener dismissListener) {
        android.widget.PopupWindow.OnDismissListener wrappedListener = null;
        if (dismissListener != null) {
            wrappedListener = new android.widget.PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    dismissListener.onDismiss();
                }
            };
        }
        this.mPopup.setOnDismissListener(wrappedListener);
    }

    public ListAdapter getAdapter() {
        return this.mAdapter;
    }

    public <T extends ListAdapter & Filterable> void setAdapter(T adapter) {
        PopupDataSetObserver popupDataSetObserver = this.mObserver;
        if (popupDataSetObserver == null) {
            this.mObserver = new PopupDataSetObserver(this, null);
        } else {
            ListAdapter listAdapter = this.mAdapter;
            if (listAdapter != null) {
                listAdapter.unregisterDataSetObserver(popupDataSetObserver);
            }
        }
        this.mAdapter = adapter;
        ListAdapter listAdapter2 = this.mAdapter;
        if (listAdapter2 != null) {
            this.mFilter = ((Filterable) listAdapter2).getFilter();
            adapter.registerDataSetObserver(this.mObserver);
        } else {
            this.mFilter = null;
        }
        this.mPopup.setAdapter(this.mAdapter);
    }

    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == 4 && isPopupShowing() && !this.mPopup.isDropDownAlwaysVisible()) {
            DispatcherState state;
            if (event.getAction() == 0 && event.getRepeatCount() == 0) {
                state = getKeyDispatcherState();
                if (state != null) {
                    state.startTracking(event, this);
                }
                return true;
            } else if (event.getAction() == 1) {
                state = getKeyDispatcherState();
                if (state != null) {
                    state.handleUpEvent(event);
                }
                if (event.isTracking() && !event.isCanceled()) {
                    dismissDropDown();
                    return true;
                }
            }
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (this.mPopup.onKeyUp(keyCode, event) && (keyCode == 23 || keyCode == 61 || keyCode == 66)) {
            if (event.hasNoModifiers()) {
                performCompletion();
            }
            return true;
        } else if (!isPopupShowing() || keyCode != 61 || !event.hasNoModifiers()) {
            return super.onKeyUp(keyCode, event);
        } else {
            performCompletion();
            return true;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (this.mPopup.onKeyDown(keyCode, event)) {
            return true;
        }
        if (!isPopupShowing() && keyCode == 20 && event.hasNoModifiers()) {
            performValidation();
        }
        if (isPopupShowing() && keyCode == 61 && event.hasNoModifiers()) {
            return true;
        }
        this.mLastKeyCode = keyCode;
        boolean handled = super.onKeyDown(keyCode, event);
        this.mLastKeyCode = 0;
        if (handled && isPopupShowing()) {
            clearListSelection();
        }
        return handled;
    }

    public boolean enoughToFilter() {
        return getText().length() >= this.mThreshold;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public void doBeforeTextChanged() {
        this.mAutoCompleteTextWatcher.beforeTextChanged(null, 0, 0, 0);
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public void doAfterTextChanged() {
        this.mAutoCompleteTextWatcher.afterTextChanged(null);
    }

    public final void refreshAutoCompleteResults() {
        if (!enoughToFilter()) {
            if (!this.mPopup.isDropDownAlwaysVisible()) {
                dismissDropDown();
            }
            Filter filter = this.mFilter;
            if (filter != null) {
                filter.filter(null);
            }
        } else if (this.mFilter != null) {
            this.mPopupCanBeUpdated = true;
            performFiltering(getText(), this.mLastKeyCode);
        }
    }

    public boolean isPopupShowing() {
        return this.mPopup.isShowing();
    }

    /* Access modifiers changed, original: protected */
    public CharSequence convertSelectionToString(Object selectedItem) {
        return this.mFilter.convertResultToString(selectedItem);
    }

    public void clearListSelection() {
        this.mPopup.clearListSelection();
    }

    public void setListSelection(int position) {
        this.mPopup.setSelection(position);
    }

    public int getListSelection() {
        return this.mPopup.getSelectedItemPosition();
    }

    /* Access modifiers changed, original: protected */
    public void performFiltering(CharSequence text, int keyCode) {
        this.mFilter.filter(text, this);
    }

    public void performCompletion() {
        performCompletion(null, -1, -1);
    }

    public void onCommitCompletion(CompletionInfo completion) {
        if (isPopupShowing()) {
            this.mPopup.performItemClick(completion.getPosition());
        }
    }

    private void performCompletion(View selectedView, int position, long id) {
        if (isPopupShowing()) {
            Object selectedItem;
            if (position < 0) {
                selectedItem = this.mPopup.getSelectedItem();
            } else {
                selectedItem = this.mAdapter.getItem(position);
            }
            if (selectedItem == null) {
                Log.w(TAG, "performCompletion: no selected item");
                return;
            }
            this.mBlockCompletion = true;
            replaceText(convertSelectionToString(selectedItem));
            this.mBlockCompletion = false;
            if (this.mItemClickListener != null) {
                ListPopupWindow list = this.mPopup;
                if (selectedView == null || position < 0) {
                    selectedView = list.getSelectedView();
                    position = list.getSelectedItemPosition();
                    id = list.getSelectedItemId();
                }
                this.mItemClickListener.onItemClick(list.getListView(), selectedView, position, id);
            }
        }
        if (this.mDropDownDismissedOnCompletion && !this.mPopup.isDropDownAlwaysVisible()) {
            dismissDropDown();
        }
    }

    public boolean isPerformingCompletion() {
        return this.mBlockCompletion;
    }

    public void setText(CharSequence text, boolean filter) {
        if (filter) {
            setText(text);
            return;
        }
        this.mBlockCompletion = true;
        setText(text);
        this.mBlockCompletion = false;
    }

    /* Access modifiers changed, original: protected */
    public void replaceText(CharSequence text) {
        clearComposingText();
        setText(text);
        Editable spannable = getText();
        Selection.setSelection(spannable, spannable.length());
    }

    public void onFilterComplete(int count) {
        updateDropDownForFilter(count);
    }

    private void updateDropDownForFilter(int count) {
        if (getWindowVisibility() != 8) {
            boolean dropDownAlwaysVisible = this.mPopup.isDropDownAlwaysVisible();
            boolean enoughToFilter = enoughToFilter();
            if ((count > 0 || dropDownAlwaysVisible) && enoughToFilter) {
                if (hasFocus() && hasWindowFocus() && this.mPopupCanBeUpdated) {
                    showDropDown();
                }
            } else if (!dropDownAlwaysVisible && isPopupShowing()) {
                dismissDropDown();
                this.mPopupCanBeUpdated = true;
            }
        }
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus && !this.mPopup.isDropDownAlwaysVisible()) {
            dismissDropDown();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDisplayHint(int hint) {
        super.onDisplayHint(hint);
        if (hint == 4 && !this.mPopup.isDropDownAlwaysVisible()) {
            dismissDropDown();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (!isTemporarilyDetached()) {
            if (!focused) {
                performValidation();
            }
            if (!(focused || this.mPopup.isDropDownAlwaysVisible())) {
                dismissDropDown();
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        dismissDropDown();
        super.onDetachedFromWindow();
    }

    public void dismissDropDown() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(InputMethodManager.class);
        if (imm != null) {
            imm.displayCompletions(this, null);
        }
        this.mPopup.dismiss();
        this.mPopupCanBeUpdated = false;
    }

    /* Access modifiers changed, original: protected */
    public boolean setFrame(int l, int t, int r, int b) {
        boolean result = super.setFrame(l, t, r, b);
        if (isPopupShowing()) {
            showDropDown();
        }
        return result;
    }

    @UnsupportedAppUsage
    public void showDropDownAfterLayout() {
        this.mPopup.postShow();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123768913)
    public void ensureImeVisible(boolean visible) {
        this.mPopup.setInputMethodMode(visible ? 1 : 2);
        if (this.mPopup.isDropDownAlwaysVisible() || (this.mFilter != null && enoughToFilter())) {
            showDropDown();
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public boolean isInputMethodNotNeeded() {
        return this.mPopup.getInputMethodMode() == 2;
    }

    public int getInputMethodMode() {
        return this.mPopup.getInputMethodMode();
    }

    public void setInputMethodMode(int mode) {
        this.mPopup.setInputMethodMode(mode);
    }

    public void showDropDown() {
        buildImeCompletions();
        if (this.mPopup.getAnchorView() == null) {
            if (this.mDropDownAnchorId != -1) {
                this.mPopup.setAnchorView(getRootView().findViewById(this.mDropDownAnchorId));
            } else {
                this.mPopup.setAnchorView(this);
            }
        }
        if (!isPopupShowing()) {
            this.mPopup.setInputMethodMode(1);
            this.mPopup.setListItemExpandMax(3);
        }
        this.mPopup.show();
        this.mPopup.getListView().setOverScrollMode(0);
    }

    @UnsupportedAppUsage
    public void setForceIgnoreOutsideTouch(boolean forceIgnoreOutsideTouch) {
        this.mPopup.setForceIgnoreOutsideTouch(forceIgnoreOutsideTouch);
    }

    private void buildImeCompletions() {
        ListAdapter adapter = this.mAdapter;
        if (adapter != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(InputMethodManager.class);
            if (imm != null) {
                int count = Math.min(adapter.getCount(), 20);
                CompletionInfo[] completions = new CompletionInfo[count];
                int realCount = 0;
                for (int i = 0; i < count; i++) {
                    if (adapter.isEnabled(i)) {
                        completions[realCount] = new CompletionInfo(adapter.getItemId(i), realCount, convertSelectionToString(adapter.getItem(i)));
                        realCount++;
                    }
                }
                if (realCount != count) {
                    CompletionInfo[] tmp = new CompletionInfo[realCount];
                    System.arraycopy(completions, 0, tmp, 0, realCount);
                    completions = tmp;
                }
                imm.displayCompletions(this, completions);
            }
        }
    }

    public void setValidator(Validator validator) {
        this.mValidator = validator;
    }

    public Validator getValidator() {
        return this.mValidator;
    }

    public void performValidation() {
        if (this.mValidator != null) {
            CharSequence text = getText();
            if (!(TextUtils.isEmpty(text) || this.mValidator.isValid(text))) {
                setText(this.mValidator.fixText(text));
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public Filter getFilter() {
        return this.mFilter;
    }
}
