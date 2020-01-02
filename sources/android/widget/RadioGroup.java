package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.view.ViewStructure;
import android.view.autofill.AutofillManager;
import android.view.autofill.AutofillValue;
import com.android.internal.R;

public class RadioGroup extends LinearLayout {
    private static final String LOG_TAG = RadioGroup.class.getSimpleName();
    private int mCheckedId = -1;
    @UnsupportedAppUsage
    private android.widget.CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
    private int mInitialCheckedId = -1;
    @UnsupportedAppUsage
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private PassThroughHierarchyChangeListener mPassThroughListener;
    private boolean mProtectFromCheckedChange = false;

    private class CheckedStateTracker implements android.widget.CompoundButton.OnCheckedChangeListener {
        private CheckedStateTracker() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!RadioGroup.this.mProtectFromCheckedChange) {
                RadioGroup.this.mProtectFromCheckedChange = true;
                if (RadioGroup.this.mCheckedId != -1) {
                    RadioGroup radioGroup = RadioGroup.this;
                    radioGroup.setCheckedStateForView(radioGroup.mCheckedId, false);
                }
                RadioGroup.this.mProtectFromCheckedChange = false;
                RadioGroup.this.setCheckedId(buttonView.getId());
            }
        }
    }

    public static class LayoutParams extends android.widget.LinearLayout.LayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(int w, int h, float initWeight) {
            super(w, h, initWeight);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams p) {
            super(p);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        /* Access modifiers changed, original: protected */
        public void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
            if (a.hasValue(widthAttr)) {
                this.width = a.getLayoutDimension(widthAttr, "layout_width");
            } else {
                this.width = -2;
            }
            if (a.hasValue(heightAttr)) {
                this.height = a.getLayoutDimension(heightAttr, "layout_height");
            } else {
                this.height = -2;
            }
        }
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(RadioGroup radioGroup, int i);
    }

    private class PassThroughHierarchyChangeListener implements OnHierarchyChangeListener {
        private OnHierarchyChangeListener mOnHierarchyChangeListener;

        private PassThroughHierarchyChangeListener() {
        }

        public void onChildViewAdded(View parent, View child) {
            if (parent == RadioGroup.this && (child instanceof RadioButton)) {
                if (child.getId() == -1) {
                    child.setId(View.generateViewId());
                }
                ((RadioButton) child).setOnCheckedChangeWidgetListener(RadioGroup.this.mChildOnCheckedChangeListener);
            }
            OnHierarchyChangeListener onHierarchyChangeListener = this.mOnHierarchyChangeListener;
            if (onHierarchyChangeListener != null) {
                onHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        public void onChildViewRemoved(View parent, View child) {
            if (parent == RadioGroup.this && (child instanceof RadioButton)) {
                ((RadioButton) child).setOnCheckedChangeWidgetListener(null);
            }
            OnHierarchyChangeListener onHierarchyChangeListener = this.mOnHierarchyChangeListener;
            if (onHierarchyChangeListener != null) {
                onHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }

    public RadioGroup(Context context) {
        super(context);
        setOrientation(1);
        init();
    }

    public RadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (getImportantForAutofill() == 0) {
            setImportantForAutofill(1);
        }
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.RadioGroup, 16842878, 0);
        saveAttributeDataForStyleable(context, R.styleable.RadioGroup, attrs, attributes, 16842878, 0);
        int value = attributes.getResourceId(1, -1);
        if (value != -1) {
            this.mCheckedId = value;
            this.mInitialCheckedId = value;
        }
        setOrientation(attributes.getInt(0, 1));
        attributes.recycle();
        init();
    }

    private void init() {
        this.mChildOnCheckedChangeListener = new CheckedStateTracker();
        this.mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(this.mPassThroughListener);
    }

    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        this.mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    /* Access modifiers changed, original: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        int i = this.mCheckedId;
        if (i != -1) {
            this.mProtectFromCheckedChange = true;
            setCheckedStateForView(i, true);
            this.mProtectFromCheckedChange = false;
            setCheckedId(this.mCheckedId);
        }
    }

    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        if (child instanceof RadioButton) {
            RadioButton button = (RadioButton) child;
            if (button.isChecked()) {
                this.mProtectFromCheckedChange = true;
                int i = this.mCheckedId;
                if (i != -1) {
                    setCheckedStateForView(i, false);
                }
                this.mProtectFromCheckedChange = false;
                setCheckedId(button.getId());
            }
        }
        super.addView(child, index, params);
    }

    public void check(int id) {
        if (id == -1 || id != this.mCheckedId) {
            int i = this.mCheckedId;
            if (i != -1) {
                setCheckedStateForView(i, false);
            }
            if (id != -1) {
                setCheckedStateForView(id, true);
            }
            setCheckedId(id);
        }
    }

    private void setCheckedId(int id) {
        boolean changed = id != this.mCheckedId;
        this.mCheckedId = id;
        OnCheckedChangeListener onCheckedChangeListener = this.mOnCheckedChangeListener;
        if (onCheckedChangeListener != null) {
            onCheckedChangeListener.onCheckedChanged(this, this.mCheckedId);
        }
        if (changed) {
            AutofillManager afm = (AutofillManager) this.mContext.getSystemService(AutofillManager.class);
            if (afm != null) {
                afm.notifyValueChanged(this);
            }
        }
    }

    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView != null && (checkedView instanceof RadioButton)) {
            ((RadioButton) checkedView).setChecked(checked);
        }
    }

    public int getCheckedRadioButtonId() {
        return this.mCheckedId;
    }

    public void clearCheck() {
        check(-1);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.mOnCheckedChangeListener = listener;
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* Access modifiers changed, original: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /* Access modifiers changed, original: protected */
    public android.widget.LinearLayout.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    public CharSequence getAccessibilityClassName() {
        return RadioGroup.class.getName();
    }

    /* Access modifiers changed, original: protected */
    public void onProvideStructure(ViewStructure structure, int viewFor, int flags) {
        super.onProvideStructure(structure, viewFor, flags);
        boolean z = true;
        if (viewFor == 1) {
            if (this.mCheckedId == this.mInitialCheckedId) {
                z = false;
            }
            structure.setDataIsSensitive(z);
        }
    }

    public void autofill(AutofillValue value) {
        if (!isEnabled()) {
            return;
        }
        if (value.isList()) {
            int index = value.getListValue();
            View child = getChildAt(index);
            if (child == null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("RadioGroup.autoFill(): no child with index ");
                stringBuilder.append(index);
                Log.w("View", stringBuilder.toString());
                return;
            }
            check(child.getId());
            return;
        }
        String str = LOG_TAG;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(value);
        stringBuilder2.append(" could not be autofilled into ");
        stringBuilder2.append(this);
        Log.w(str, stringBuilder2.toString());
    }

    public int getAutofillType() {
        return isEnabled() ? 3 : 0;
    }

    public AutofillValue getAutofillValue() {
        if (!isEnabled()) {
            return null;
        }
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            if (getChildAt(i).getId() == this.mCheckedId) {
                return AutofillValue.forList(i);
            }
        }
        return null;
    }
}
