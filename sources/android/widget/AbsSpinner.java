package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.autofill.AutofillValue;
import com.android.internal.R;
import com.miui.internal.variable.api.v29.Android_Widget_AbsSpinner.Extension;
import com.miui.internal.variable.api.v29.Android_Widget_AbsSpinner.Interface;

public abstract class AbsSpinner extends AdapterView<SpinnerAdapter> {
    private static final String LOG_TAG = AbsSpinner.class.getSimpleName();
    SpinnerAdapter mAdapter;
    private DataSetObserver mDataSetObserver;
    int mHeightMeasureSpec;
    final RecycleBin mRecycler;
    int mSelectionBottomPadding;
    int mSelectionLeftPadding;
    int mSelectionRightPadding;
    int mSelectionTopPadding;
    final Rect mSpinnerPadding;
    private Rect mTouchFrame;
    int mWidthMeasureSpec;

    class RecycleBin {
        private final SparseArray<View> mScrapHeap = new SparseArray();

        RecycleBin() {
        }

        public void put(int position, View v) {
            this.mScrapHeap.put(position, v);
        }

        /* Access modifiers changed, original: 0000 */
        public View get(int position) {
            View result = (View) this.mScrapHeap.get(position);
            if (result != null) {
                this.mScrapHeap.delete(position);
            }
            return result;
        }

        /* Access modifiers changed, original: 0000 */
        public void clear() {
            SparseArray<View> scrapHeap = this.mScrapHeap;
            int count = scrapHeap.size();
            for (int i = 0; i < count; i++) {
                View view = (View) scrapHeap.valueAt(i);
                if (view != null) {
                    AbsSpinner.this.removeDetachedView(view, true);
                }
            }
            scrapHeap.clear();
        }
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int position;
        long selectedId;

        SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel in) {
            super(in);
            this.selectedId = in.readLong();
            this.position = in.readInt();
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeLong(this.selectedId);
            out.writeInt(this.position);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("AbsSpinner.SavedState{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
            stringBuilder.append(" selectedId=");
            stringBuilder.append(this.selectedId);
            stringBuilder.append(" position=");
            stringBuilder.append(this.position);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public abstract void layout(int i, boolean z);

    static {
        Extension.get().bindOriginal(new Interface() {
            public void setAdapter(AbsSpinner absSpinner, SpinnerAdapter spinnerAdapter) {
                absSpinner.originalSetAdapter(spinnerAdapter);
            }
        });
    }

    public AbsSpinner(Context context) {
        super(context);
        this.mSelectionLeftPadding = 0;
        this.mSelectionTopPadding = 0;
        this.mSelectionRightPadding = 0;
        this.mSelectionBottomPadding = 0;
        this.mSpinnerPadding = new Rect();
        this.mRecycler = new RecycleBin();
        initAbsSpinner();
    }

    public AbsSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AbsSpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mSelectionLeftPadding = 0;
        this.mSelectionTopPadding = 0;
        this.mSelectionRightPadding = 0;
        this.mSelectionBottomPadding = 0;
        this.mSpinnerPadding = new Rect();
        this.mRecycler = new RecycleBin();
        if (getImportantForAutofill() == 0) {
            setImportantForAutofill(1);
        }
        initAbsSpinner();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AbsSpinner, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.AbsSpinner, attrs, a, defStyleAttr, defStyleRes);
        Object[] entries = a.getTextArray(0);
        if (entries != null) {
            SpinnerAdapter adapter = new ArrayAdapter(context, 17367048, entries);
            adapter.setDropDownViewResource(17367049);
            setAdapter(adapter);
        }
        a.recycle();
    }

    private void initAbsSpinner() {
        setFocusable(true);
        setWillNotDraw(false);
    }

    public void setAdapter(SpinnerAdapter adapter) {
        if (Extension.get().getExtension() != null) {
            ((Interface) Extension.get().getExtension().asInterface()).setAdapter(this, adapter);
        } else {
            originalSetAdapter(adapter);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void originalSetAdapter(SpinnerAdapter adapter) {
        SpinnerAdapter spinnerAdapter = this.mAdapter;
        if (spinnerAdapter != null) {
            spinnerAdapter.unregisterDataSetObserver(this.mDataSetObserver);
            resetList();
        }
        this.mAdapter = adapter;
        int position = -1;
        this.mOldSelectedPosition = -1;
        this.mOldSelectedRowId = Long.MIN_VALUE;
        if (this.mAdapter != null) {
            this.mOldItemCount = this.mItemCount;
            this.mItemCount = this.mAdapter.getCount();
            checkFocus();
            this.mDataSetObserver = new AdapterDataSetObserver();
            this.mAdapter.registerDataSetObserver(this.mDataSetObserver);
            if (this.mItemCount > 0) {
                position = 0;
            }
            setSelectedPositionInt(position);
            setNextSelectedPositionInt(position);
            if (this.mItemCount == 0) {
                checkSelectionChanged();
            }
        } else {
            checkFocus();
            resetList();
            checkSelectionChanged();
        }
        requestLayout();
    }

    /* Access modifiers changed, original: 0000 */
    public void resetList() {
        this.mDataChanged = false;
        this.mNeedSync = false;
        removeAllViewsInLayout();
        this.mOldSelectedPosition = -1;
        this.mOldSelectedRowId = Long.MIN_VALUE;
        setSelectedPositionInt(-1);
        setNextSelectedPositionInt(-1);
        invalidate();
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        Rect rect = this.mSpinnerPadding;
        int i = this.mPaddingLeft;
        int i2 = this.mSelectionLeftPadding;
        if (i > i2) {
            i2 = this.mPaddingLeft;
        }
        rect.left = i2;
        rect = this.mSpinnerPadding;
        i = this.mPaddingTop;
        i2 = this.mSelectionTopPadding;
        if (i > i2) {
            i2 = this.mPaddingTop;
        }
        rect.top = i2;
        rect = this.mSpinnerPadding;
        i = this.mPaddingRight;
        i2 = this.mSelectionRightPadding;
        if (i > i2) {
            i2 = this.mPaddingRight;
        }
        rect.right = i2;
        rect = this.mSpinnerPadding;
        i = this.mPaddingBottom;
        i2 = this.mSelectionBottomPadding;
        if (i > i2) {
            i2 = this.mPaddingBottom;
        }
        rect.bottom = i2;
        if (this.mDataChanged) {
            handleDataChanged();
        }
        int preferredHeight = 0;
        i = 0;
        boolean needsMeasuring = true;
        int selectedPosition = getSelectedItemPosition();
        if (selectedPosition >= 0) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter != null && selectedPosition < spinnerAdapter.getCount()) {
                View view = this.mRecycler.get(selectedPosition);
                if (view == null) {
                    view = this.mAdapter.getView(selectedPosition, null, this);
                    if (view.getImportantForAccessibility() == 0) {
                        view.setImportantForAccessibility(1);
                    }
                }
                this.mRecycler.put(selectedPosition, view);
                if (view.getLayoutParams() == null) {
                    this.mBlockLayoutRequests = true;
                    view.setLayoutParams(generateDefaultLayoutParams());
                    this.mBlockLayoutRequests = false;
                }
                measureChild(view, widthMeasureSpec, heightMeasureSpec);
                preferredHeight = (getChildHeight(view) + this.mSpinnerPadding.top) + this.mSpinnerPadding.bottom;
                i = (getChildWidth(view) + this.mSpinnerPadding.left) + this.mSpinnerPadding.right;
                needsMeasuring = false;
            }
        }
        if (needsMeasuring) {
            preferredHeight = this.mSpinnerPadding.top + this.mSpinnerPadding.bottom;
            if (widthMode == 0) {
                i = this.mSpinnerPadding.left + this.mSpinnerPadding.right;
            }
        }
        preferredHeight = Math.max(preferredHeight, getSuggestedMinimumHeight());
        i = Math.max(i, getSuggestedMinimumWidth());
        setMeasuredDimension(View.resolveSizeAndState(i, widthMeasureSpec, 0), View.resolveSizeAndState(preferredHeight, heightMeasureSpec, 0));
        this.mHeightMeasureSpec = heightMeasureSpec;
        this.mWidthMeasureSpec = widthMeasureSpec;
    }

    /* Access modifiers changed, original: 0000 */
    public int getChildHeight(View child) {
        return child.getMeasuredHeight();
    }

    /* Access modifiers changed, original: 0000 */
    public int getChildWidth(View child) {
        return child.getMeasuredWidth();
    }

    /* Access modifiers changed, original: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -2);
    }

    /* Access modifiers changed, original: 0000 */
    public void recycleAllViews() {
        int childCount = getChildCount();
        RecycleBin recycleBin = this.mRecycler;
        int position = this.mFirstPosition;
        for (int i = 0; i < childCount; i++) {
            recycleBin.put(position + i, getChildAt(i));
        }
    }

    public void setSelection(int position, boolean animate) {
        boolean shouldAnimate = true;
        if (!animate || this.mFirstPosition > position || position > (this.mFirstPosition + getChildCount()) - 1) {
            shouldAnimate = false;
        }
        setSelectionInt(position, shouldAnimate);
    }

    public void setSelection(int position) {
        setNextSelectedPositionInt(position);
        requestLayout();
        invalidate();
    }

    /* Access modifiers changed, original: 0000 */
    public void setSelectionInt(int position, boolean animate) {
        if (position != this.mOldSelectedPosition) {
            this.mBlockLayoutRequests = true;
            int delta = position - this.mSelectedPosition;
            setNextSelectedPositionInt(position);
            layout(delta, animate);
            this.mBlockLayoutRequests = false;
        }
    }

    public View getSelectedView() {
        if (this.mItemCount <= 0 || this.mSelectedPosition < 0) {
            return null;
        }
        return getChildAt(this.mSelectedPosition - this.mFirstPosition);
    }

    public void requestLayout() {
        if (!this.mBlockLayoutRequests) {
            super.requestLayout();
        }
    }

    public SpinnerAdapter getAdapter() {
        return this.mAdapter;
    }

    public int getCount() {
        return this.mItemCount;
    }

    public int pointToPosition(int x, int y) {
        Rect frame = this.mTouchFrame;
        if (frame == null) {
            this.mTouchFrame = new Rect();
            frame = this.mTouchFrame;
        }
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                child.getHitRect(frame);
                if (frame.contains(x, y)) {
                    return this.mFirstPosition + i;
                }
            }
        }
        return -1;
    }

    /* Access modifiers changed, original: protected */
    public void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        super.dispatchRestoreInstanceState(container);
        handleDataChanged();
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.selectedId = getSelectedItemId();
        if (ss.selectedId >= 0) {
            ss.position = getSelectedItemPosition();
        } else {
            ss.position = -1;
        }
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        if (ss.selectedId >= 0) {
            this.mDataChanged = true;
            this.mNeedSync = true;
            this.mSyncRowId = ss.selectedId;
            this.mSyncPosition = ss.position;
            this.mSyncMode = 0;
            requestLayout();
        }
    }

    public CharSequence getAccessibilityClassName() {
        return AbsSpinner.class.getName();
    }

    public void autofill(AutofillValue value) {
        if (!isEnabled()) {
            return;
        }
        if (value.isList()) {
            setSelection(value.getListValue());
            return;
        }
        String str = LOG_TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(value);
        stringBuilder.append(" could not be autofilled into ");
        stringBuilder.append(this);
        Log.w(str, stringBuilder.toString());
    }

    public int getAutofillType() {
        return isEnabled() ? 3 : 0;
    }

    public AutofillValue getAutofillValue() {
        return isEnabled() ? AutofillValue.forList(getSelectedItemPosition()) : null;
    }
}
