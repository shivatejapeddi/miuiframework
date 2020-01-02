package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewHierarchyEncoder;
import android.view.ViewStructure;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.autofill.AutofillManager;
import android.view.autofill.AutofillValue;
import android.view.inspector.InspectionCompanion.UninitializedPropertyMapException;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import com.android.internal.R;

public abstract class CompoundButton extends Button implements Checkable {
    private static final int[] CHECKED_STATE_SET = new int[]{16842912};
    private static final String LOG_TAG = CompoundButton.class.getSimpleName();
    @UnsupportedAppUsage
    private boolean mBroadcasting;
    private BlendMode mButtonBlendMode;
    @UnsupportedAppUsage
    private Drawable mButtonDrawable;
    private ColorStateList mButtonTintList;
    private boolean mChecked;
    private boolean mCheckedFromResource;
    private boolean mHasButtonBlendMode;
    private boolean mHasButtonTint;
    @UnsupportedAppUsage
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;

    public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<CompoundButton> {
        private int mButtonBlendModeId;
        private int mButtonId;
        private int mButtonTintId;
        private int mButtonTintModeId;
        private int mCheckedId;
        private boolean mPropertiesMapped = false;

        public void mapProperties(PropertyMapper propertyMapper) {
            this.mButtonId = propertyMapper.mapObject("button", 16843015);
            this.mButtonBlendModeId = propertyMapper.mapObject("buttonBlendMode", 3);
            this.mButtonTintId = propertyMapper.mapObject("buttonTint", 16843887);
            this.mButtonTintModeId = propertyMapper.mapObject("buttonTintMode", 16843888);
            this.mCheckedId = propertyMapper.mapBoolean("checked", 16843014);
            this.mPropertiesMapped = true;
        }

        public void readProperties(CompoundButton node, PropertyReader propertyReader) {
            if (this.mPropertiesMapped) {
                propertyReader.readObject(this.mButtonId, node.getButtonDrawable());
                propertyReader.readObject(this.mButtonBlendModeId, node.getButtonTintBlendMode());
                propertyReader.readObject(this.mButtonTintId, node.getButtonTintList());
                propertyReader.readObject(this.mButtonTintModeId, node.getButtonTintMode());
                propertyReader.readBoolean(this.mCheckedId, node.isChecked());
                return;
            }
            throw new UninitializedPropertyMapException();
        }
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(CompoundButton compoundButton, boolean z);
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
        boolean checked;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.checked = ((Boolean) in.readValue(null)).booleanValue();
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(Boolean.valueOf(this.checked));
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("CompoundButton.SavedState{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
            stringBuilder.append(" checked=");
            stringBuilder.append(this.checked);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public CompoundButton(Context context) {
        this(context, null);
    }

    public CompoundButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompoundButton(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CompoundButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mButtonTintList = null;
        this.mButtonBlendMode = null;
        this.mHasButtonTint = false;
        this.mHasButtonBlendMode = false;
        this.mCheckedFromResource = false;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CompoundButton, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.CompoundButton, attrs, a, defStyleAttr, defStyleRes);
        Drawable d = a.getDrawable(1);
        if (d != null) {
            setButtonDrawable(d);
        }
        if (a.hasValue(3)) {
            this.mButtonBlendMode = Drawable.parseBlendMode(a.getInt(3, -1), this.mButtonBlendMode);
            this.mHasButtonBlendMode = true;
        }
        if (a.hasValue(2)) {
            this.mButtonTintList = a.getColorStateList(2);
            this.mHasButtonTint = true;
        }
        setChecked(a.getBoolean(0, false));
        this.mCheckedFromResource = true;
        a.recycle();
        applyButtonTint();
    }

    public void toggle() {
        setChecked(this.mChecked ^ 1);
    }

    public boolean performClick() {
        toggle();
        boolean handled = super.performClick();
        if (!handled) {
            playSoundEffect(0);
        }
        return handled;
    }

    @ExportedProperty
    public boolean isChecked() {
        return this.mChecked;
    }

    public void setChecked(boolean checked) {
        if (this.mChecked != checked) {
            this.mCheckedFromResource = false;
            this.mChecked = checked;
            refreshDrawableState();
            notifyViewAccessibilityStateChangedIfNeeded(0);
            if (!this.mBroadcasting) {
                this.mBroadcasting = true;
                OnCheckedChangeListener onCheckedChangeListener = this.mOnCheckedChangeListener;
                if (onCheckedChangeListener != null) {
                    onCheckedChangeListener.onCheckedChanged(this, this.mChecked);
                }
                onCheckedChangeListener = this.mOnCheckedChangeWidgetListener;
                if (onCheckedChangeListener != null) {
                    onCheckedChangeListener.onCheckedChanged(this, this.mChecked);
                }
                AutofillManager afm = (AutofillManager) this.mContext.getSystemService(AutofillManager.class);
                if (afm != null) {
                    afm.notifyValueChanged(this);
                }
                this.mBroadcasting = false;
            }
        }
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.mOnCheckedChangeListener = listener;
    }

    /* Access modifiers changed, original: 0000 */
    public void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener) {
        this.mOnCheckedChangeWidgetListener = listener;
    }

    public void setButtonDrawable(int resId) {
        Drawable d;
        if (resId != 0) {
            d = getContext().getDrawable(resId);
        } else {
            d = null;
        }
        setButtonDrawable(d);
    }

    public void setButtonDrawable(Drawable drawable) {
        Drawable drawable2 = this.mButtonDrawable;
        if (drawable2 != drawable) {
            if (drawable2 != null) {
                drawable2.setCallback(null);
                unscheduleDrawable(this.mButtonDrawable);
            }
            this.mButtonDrawable = drawable;
            if (drawable != null) {
                drawable.setCallback(this);
                drawable.setLayoutDirection(getLayoutDirection());
                if (drawable.isStateful()) {
                    drawable.setState(getDrawableState());
                }
                drawable.setVisible(getVisibility() == 0, false);
                setMinHeight(drawable.getIntrinsicHeight());
                applyButtonTint();
            }
        }
    }

    public void onResolveDrawables(int layoutDirection) {
        super.onResolveDrawables(layoutDirection);
        Drawable drawable = this.mButtonDrawable;
        if (drawable != null) {
            drawable.setLayoutDirection(layoutDirection);
        }
    }

    public Drawable getButtonDrawable() {
        return this.mButtonDrawable;
    }

    public void setButtonTintList(ColorStateList tint) {
        this.mButtonTintList = tint;
        this.mHasButtonTint = true;
        applyButtonTint();
    }

    public ColorStateList getButtonTintList() {
        return this.mButtonTintList;
    }

    public void setButtonTintMode(Mode tintMode) {
        setButtonTintBlendMode(tintMode != null ? BlendMode.fromValue(tintMode.nativeInt) : null);
    }

    public void setButtonTintBlendMode(BlendMode tintMode) {
        this.mButtonBlendMode = tintMode;
        this.mHasButtonBlendMode = true;
        applyButtonTint();
    }

    public Mode getButtonTintMode() {
        BlendMode blendMode = this.mButtonBlendMode;
        if (blendMode != null) {
            return BlendMode.blendModeToPorterDuffMode(blendMode);
        }
        return null;
    }

    public BlendMode getButtonTintBlendMode() {
        return this.mButtonBlendMode;
    }

    private void applyButtonTint() {
        if (this.mButtonDrawable == null) {
            return;
        }
        if (this.mHasButtonTint || this.mHasButtonBlendMode) {
            this.mButtonDrawable = this.mButtonDrawable.mutate();
            if (this.mHasButtonTint) {
                this.mButtonDrawable.setTintList(this.mButtonTintList);
            }
            if (this.mHasButtonBlendMode) {
                this.mButtonDrawable.setTintBlendMode(this.mButtonBlendMode);
            }
            if (this.mButtonDrawable.isStateful()) {
                this.mButtonDrawable.setState(getDrawableState());
            }
        }
    }

    public CharSequence getAccessibilityClassName() {
        return CompoundButton.class.getName();
    }

    public void onInitializeAccessibilityEventInternal(AccessibilityEvent event) {
        super.onInitializeAccessibilityEventInternal(event);
        event.setChecked(this.mChecked);
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        info.setCheckable(true);
        info.setChecked(this.mChecked);
    }

    public int getCompoundPaddingLeft() {
        int padding = super.getCompoundPaddingLeft();
        if (isLayoutRtl()) {
            return padding;
        }
        Drawable buttonDrawable = this.mButtonDrawable;
        if (buttonDrawable != null) {
            return padding + buttonDrawable.getIntrinsicWidth();
        }
        return padding;
    }

    public int getCompoundPaddingRight() {
        int padding = super.getCompoundPaddingRight();
        if (!isLayoutRtl()) {
            return padding;
        }
        Drawable buttonDrawable = this.mButtonDrawable;
        if (buttonDrawable != null) {
            return padding + buttonDrawable.getIntrinsicWidth();
        }
        return padding;
    }

    public int getHorizontalOffsetForDrawables() {
        Drawable buttonDrawable = this.mButtonDrawable;
        return buttonDrawable != null ? buttonDrawable.getIntrinsicWidth() : 0;
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        int verticalGravity;
        int drawableHeight;
        Drawable buttonDrawable = this.mButtonDrawable;
        if (buttonDrawable != null) {
            int top;
            verticalGravity = getGravity() & 112;
            drawableHeight = buttonDrawable.getIntrinsicHeight();
            int drawableWidth = buttonDrawable.getIntrinsicWidth();
            if (verticalGravity == 16) {
                top = (getHeight() - drawableHeight) / 2;
            } else if (verticalGravity != 80) {
                top = 0;
            } else {
                top = getHeight() - drawableHeight;
            }
            int bottom = top + drawableHeight;
            int left = isLayoutRtl() ? getWidth() - drawableWidth : 0;
            int right = isLayoutRtl() ? getWidth() : drawableWidth;
            buttonDrawable.setBounds(left, top, right, bottom);
            Drawable background = getBackground();
            if (background != null) {
                background.setHotspotBounds(left, top, right, bottom);
            }
        }
        super.onDraw(canvas);
        if (buttonDrawable != null) {
            verticalGravity = this.mScrollX;
            drawableHeight = this.mScrollY;
            if (verticalGravity == 0 && drawableHeight == 0) {
                buttonDrawable.draw(canvas);
                return;
            }
            canvas.translate((float) verticalGravity, (float) drawableHeight);
            buttonDrawable.draw(canvas);
            canvas.translate((float) (-verticalGravity), (float) (-drawableHeight));
        }
    }

    /* Access modifiers changed, original: protected */
    public int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            View.mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    /* Access modifiers changed, original: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable buttonDrawable = this.mButtonDrawable;
        if (buttonDrawable != null && buttonDrawable.isStateful() && buttonDrawable.setState(getDrawableState())) {
            invalidateDrawable(buttonDrawable);
        }
    }

    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        Drawable drawable = this.mButtonDrawable;
        if (drawable != null) {
            drawable.setHotspot(x, y);
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == this.mButtonDrawable;
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.mButtonDrawable;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.checked = isChecked();
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setChecked(ss.checked);
        requestLayout();
    }

    /* Access modifiers changed, original: protected */
    public void encodeProperties(ViewHierarchyEncoder stream) {
        super.encodeProperties(stream);
        stream.addProperty("checked", isChecked());
    }

    /* Access modifiers changed, original: protected */
    public void onProvideStructure(ViewStructure structure, int viewFor, int flags) {
        super.onProvideStructure(structure, viewFor, flags);
        if (viewFor == 1) {
            structure.setDataIsSensitive(1 ^ this.mCheckedFromResource);
        }
    }

    public void autofill(AutofillValue value) {
        if (!isEnabled()) {
            return;
        }
        if (value.isToggle()) {
            setChecked(value.getToggleValue());
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
        return isEnabled() ? 2 : 0;
    }

    public AutofillValue getAutofillValue() {
        return isEnabled() ? AutofillValue.forToggle(isChecked()) : null;
    }
}
