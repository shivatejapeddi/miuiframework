package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.inspector.InspectionCompanion.UninitializedPropertyMapException;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import com.android.internal.R;

public class ToggleButton extends CompoundButton {
    private static final int NO_ALPHA = 255;
    private float mDisabledAlpha;
    private Drawable mIndicatorDrawable;
    private CharSequence mTextOff;
    private CharSequence mTextOn;

    public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<ToggleButton> {
        private int mDisabledAlphaId;
        private boolean mPropertiesMapped = false;
        private int mTextOffId;
        private int mTextOnId;

        public void mapProperties(PropertyMapper propertyMapper) {
            this.mDisabledAlphaId = propertyMapper.mapFloat("disabledAlpha", 16842803);
            this.mTextOffId = propertyMapper.mapObject("textOff", 16843045);
            this.mTextOnId = propertyMapper.mapObject("textOn", 16843044);
            this.mPropertiesMapped = true;
        }

        public void readProperties(ToggleButton node, PropertyReader propertyReader) {
            if (this.mPropertiesMapped) {
                propertyReader.readFloat(this.mDisabledAlphaId, node.getDisabledAlpha());
                propertyReader.readObject(this.mTextOffId, node.getTextOff());
                propertyReader.readObject(this.mTextOnId, node.getTextOn());
                return;
            }
            throw new UninitializedPropertyMapException();
        }
    }

    public ToggleButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ToggleButton, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.ToggleButton, attrs, a, defStyleAttr, defStyleRes);
        this.mTextOn = a.getText(1);
        this.mTextOff = a.getText(2);
        this.mDisabledAlpha = a.getFloat(0, 0.5f);
        syncTextState();
        a.recycle();
    }

    public ToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ToggleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 16842827);
    }

    public ToggleButton(Context context) {
        this(context, null);
    }

    public void setChecked(boolean checked) {
        super.setChecked(checked);
        syncTextState();
    }

    private void syncTextState() {
        CharSequence charSequence;
        boolean checked = isChecked();
        if (checked) {
            charSequence = this.mTextOn;
            if (charSequence != null) {
                setText(charSequence);
                return;
            }
        }
        if (!checked) {
            charSequence = this.mTextOff;
            if (charSequence != null) {
                setText(charSequence);
            }
        }
    }

    public CharSequence getTextOn() {
        return this.mTextOn;
    }

    public void setTextOn(CharSequence textOn) {
        this.mTextOn = textOn;
    }

    public CharSequence getTextOff() {
        return this.mTextOff;
    }

    public void setTextOff(CharSequence textOff) {
        this.mTextOff = textOff;
    }

    public float getDisabledAlpha() {
        return this.mDisabledAlpha;
    }

    /* Access modifiers changed, original: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        updateReferenceToIndicatorDrawable(getBackground());
    }

    public void setBackgroundDrawable(Drawable d) {
        super.setBackgroundDrawable(d);
        updateReferenceToIndicatorDrawable(d);
    }

    private void updateReferenceToIndicatorDrawable(Drawable backgroundDrawable) {
        if (backgroundDrawable instanceof LayerDrawable) {
            this.mIndicatorDrawable = ((LayerDrawable) backgroundDrawable).findDrawableByLayerId(16908311);
        } else {
            this.mIndicatorDrawable = null;
        }
    }

    /* Access modifiers changed, original: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable = this.mIndicatorDrawable;
        if (drawable != null) {
            drawable.setAlpha(isEnabled() ? 255 : (int) (this.mDisabledAlpha * 255.0f));
        }
    }

    public CharSequence getAccessibilityClassName() {
        return ToggleButton.class.getName();
    }
}
