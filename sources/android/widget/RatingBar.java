package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.inspector.InspectionCompanion.UninitializedPropertyMapException;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import com.android.internal.R;

public class RatingBar extends AbsSeekBar {
    private int mNumStars;
    @UnsupportedAppUsage
    private OnRatingBarChangeListener mOnRatingBarChangeListener;
    private int mProgressOnStartTracking;

    public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<RatingBar> {
        private int mIsIndicatorId;
        private int mNumStarsId;
        private boolean mPropertiesMapped = false;
        private int mRatingId;
        private int mStepSizeId;

        public void mapProperties(PropertyMapper propertyMapper) {
            this.mIsIndicatorId = propertyMapper.mapBoolean("isIndicator", 16843079);
            this.mNumStarsId = propertyMapper.mapInt("numStars", 16843076);
            this.mRatingId = propertyMapper.mapFloat("rating", 16843077);
            this.mStepSizeId = propertyMapper.mapFloat("stepSize", 16843078);
            this.mPropertiesMapped = true;
        }

        public void readProperties(RatingBar node, PropertyReader propertyReader) {
            if (this.mPropertiesMapped) {
                propertyReader.readBoolean(this.mIsIndicatorId, node.isIndicator());
                propertyReader.readInt(this.mNumStarsId, node.getNumStars());
                propertyReader.readFloat(this.mRatingId, node.getRating());
                propertyReader.readFloat(this.mStepSizeId, node.getStepSize());
                return;
            }
            throw new UninitializedPropertyMapException();
        }
    }

    public interface OnRatingBarChangeListener {
        void onRatingChanged(RatingBar ratingBar, float f, boolean z);
    }

    public RatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RatingBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mNumStars = 5;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatingBar, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.RatingBar, attrs, a, defStyleAttr, defStyleRes);
        int numStars = a.getInt(0, this.mNumStars);
        setIsIndicator(a.getBoolean(3, this.mIsUserSeekable ^ 1));
        float rating = a.getFloat(1, -1.0f);
        float stepSize = a.getFloat(2, -1.0f);
        a.recycle();
        if (numStars > 0 && numStars != this.mNumStars) {
            setNumStars(numStars);
        }
        if (stepSize >= 0.0f) {
            setStepSize(stepSize);
        } else {
            setStepSize(0.5f);
        }
        if (rating >= 0.0f) {
            setRating(rating);
        }
        this.mTouchProgressOffset = 0.6f;
    }

    public RatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, 16842876);
    }

    public RatingBar(Context context) {
        this(context, null);
    }

    public void setOnRatingBarChangeListener(OnRatingBarChangeListener listener) {
        this.mOnRatingBarChangeListener = listener;
    }

    public OnRatingBarChangeListener getOnRatingBarChangeListener() {
        return this.mOnRatingBarChangeListener;
    }

    public void setIsIndicator(boolean isIndicator) {
        this.mIsUserSeekable = isIndicator ^ 1;
        if (isIndicator) {
            setFocusable(16);
        } else {
            setFocusable(1);
        }
    }

    public boolean isIndicator() {
        return this.mIsUserSeekable ^ 1;
    }

    public void setNumStars(int numStars) {
        if (numStars > 0) {
            this.mNumStars = numStars;
            requestLayout();
        }
    }

    public int getNumStars() {
        return this.mNumStars;
    }

    public void setRating(float rating) {
        setProgress(Math.round(getProgressPerStar() * rating));
    }

    public float getRating() {
        return ((float) getProgress()) / getProgressPerStar();
    }

    public void setStepSize(float stepSize) {
        if (stepSize > 0.0f) {
            float newMax = ((float) this.mNumStars) / stepSize;
            int newProgress = (int) ((newMax / ((float) getMax())) * ((float) getProgress()));
            setMax((int) newMax);
            setProgress(newProgress);
        }
    }

    public float getStepSize() {
        return ((float) getNumStars()) / ((float) getMax());
    }

    private float getProgressPerStar() {
        if (this.mNumStars > 0) {
            return (((float) getMax()) * 1.0f) / ((float) this.mNumStars);
        }
        return 1.0f;
    }

    /* Access modifiers changed, original: 0000 */
    public Shape getDrawableShape() {
        return new RectShape();
    }

    /* Access modifiers changed, original: 0000 */
    public void onProgressRefresh(float scale, boolean fromUser, int progress) {
        super.onProgressRefresh(scale, fromUser, progress);
        updateSecondaryProgress(progress);
        if (!fromUser) {
            dispatchRatingChange(false);
        }
    }

    private void updateSecondaryProgress(int progress) {
        float ratio = getProgressPerStar();
        if (ratio > 0.0f) {
            setSecondaryProgress((int) (Math.ceil((double) (((float) progress) / ratio)) * ((double) ratio)));
        }
    }

    /* Access modifiers changed, original: protected|declared_synchronized */
    public synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mSampleWidth > 0) {
            setMeasuredDimension(View.resolveSizeAndState(this.mSampleWidth * this.mNumStars, widthMeasureSpec, 0), getMeasuredHeight());
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void onStartTrackingTouch() {
        this.mProgressOnStartTracking = getProgress();
        super.onStartTrackingTouch();
    }

    /* Access modifiers changed, original: 0000 */
    public void onStopTrackingTouch() {
        super.onStopTrackingTouch();
        if (getProgress() != this.mProgressOnStartTracking) {
            dispatchRatingChange(true);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void onKeyChange() {
        super.onKeyChange();
        dispatchRatingChange(true);
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchRatingChange(boolean fromUser) {
        OnRatingBarChangeListener onRatingBarChangeListener = this.mOnRatingBarChangeListener;
        if (onRatingBarChangeListener != null) {
            onRatingBarChangeListener.onRatingChanged(this, getRating(), fromUser);
        }
    }

    public synchronized void setMax(int max) {
        if (max > 0) {
            super.setMax(max);
        }
    }

    public CharSequence getAccessibilityClassName() {
        return RatingBar.class.getName();
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        if (canUserSetProgress()) {
            info.addAction(AccessibilityAction.ACTION_SET_PROGRESS);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean canUserSetProgress() {
        return super.canUserSetProgress() && !isIndicator();
    }
}
