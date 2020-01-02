package android.widget;

import android.content.Context;
import android.miui.R;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

public class FloatPanelView extends FrameLayout {
    public static final int DOWN_ARROW = -1;
    public static final int NO_ARROW = 0;
    public static final int UP_ARROW = 1;
    private ViewGroup mContent;
    private int mDirection;
    private ImageView mDownArrow;
    private int mLeftRoundCorner;
    private int mOffset;
    private int mRightRoundCorner;
    private ImageView mUpArrow;

    public FloatPanelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FloatPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FloatPanelView(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.mUpArrow = getImageView(R.drawable.text_select_arrow_up);
        this.mDownArrow = getImageView(R.drawable.text_select_arrow_down);
    }

    private ImageView getImageView(int resourceId) {
        ImageView i = new ImageView(getContext());
        i.setImageResource(resourceId);
        return i;
    }

    public void setContent(ViewGroup v) {
        if (v != this.mContent) {
            removeAllViews();
            if (v != null) {
                addView(v);
                this.mContent = v;
                addView(this.mUpArrow);
                addView(this.mDownArrow);
            }
        }
    }

    public ViewGroup getContent() {
        return this.mContent;
    }

    public void setArrow(int direction) {
        if (direction != this.mDirection) {
            this.mDirection = direction;
            requestLayout();
        }
    }

    public void setOffset(int offset) {
        if (this.mOffset != offset) {
            this.mOffset = offset;
            requestLayout();
        }
    }

    public void setLeftCorner(int corner) {
        if (corner != this.mLeftRoundCorner) {
            this.mLeftRoundCorner = corner;
            requestLayout();
        }
    }

    public void setRightCorner(int corner) {
        if (corner != this.mRightRoundCorner) {
            this.mRightRoundCorner = corner;
            requestLayout();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ImageView arrow = null;
        int i = this.mDirection;
        if (i == 1) {
            arrow = this.mUpArrow;
        } else if (i == -1) {
            arrow = this.mDownArrow;
        }
        i = arrow == null ? 0 : arrow.getDrawable().getIntrinsicHeight();
        this.mContent.measure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(this.mContent.getMeasuredWidth(), this.mContent.getMeasuredHeight() + i);
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        ImageView imageView = this.mUpArrow;
        if (imageView == null || this.mDownArrow == null || this.mContent == null) {
            Log.e("FloatPanelView", "couldn't find view");
            return;
        }
        imageView.setVisibility(8);
        this.mDownArrow.setVisibility(8);
        imageView = null;
        int i = this.mDirection;
        if (i == 1) {
            imageView = this.mUpArrow;
        } else if (i == -1) {
            imageView = this.mDownArrow;
        }
        if (imageView != null) {
            i = imageView.getDrawable().getIntrinsicWidth();
            int arrowHeight = imageView.getDrawable().getIntrinsicHeight();
            int t = this.mDirection == 1 ? top : this.mContent.getMeasuredHeight() + top;
            imageView.setVisibility(0);
            int l = ((((right - left) - i) / 2) + left) + this.mOffset;
            if (l < this.mContent.getLeft() + this.mLeftRoundCorner) {
                l = this.mContent.getLeft() + this.mLeftRoundCorner;
            } else {
                int i2 = right - i;
                int i3 = this.mRightRoundCorner;
                if (l > i2 - i3) {
                    l = (right - i) - i3;
                }
            }
            imageView.layout(l, t, l + i, t + arrowHeight);
            t = t == top ? arrowHeight : top;
            ViewGroup viewGroup = this.mContent;
            viewGroup.layout(viewGroup.getLeft(), t, this.mContent.getLeft() + this.mContent.getMeasuredWidth(), this.mContent.getMeasuredHeight() + t);
        } else {
            ViewGroup viewGroup2 = this.mContent;
            viewGroup2.layout(viewGroup2.getLeft(), this.mContent.getTop(), this.mContent.getLeft() + this.mContent.getMeasuredWidth(), this.mContent.getTop() + this.mContent.getMeasuredHeight());
        }
    }
}
