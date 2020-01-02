package android.app;

import android.animation.LayoutTransition;
import android.app.FragmentManager.BackStackEntry;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.R;

@Deprecated
public class FragmentBreadCrumbs extends ViewGroup implements OnBackStackChangedListener {
    private static final int DEFAULT_GRAVITY = 8388627;
    Activity mActivity;
    LinearLayout mContainer;
    private int mGravity;
    LayoutInflater mInflater;
    private int mLayoutResId;
    int mMaxVisible;
    private OnBreadCrumbClickListener mOnBreadCrumbClickListener;
    private OnClickListener mOnClickListener;
    private OnClickListener mParentClickListener;
    BackStackRecord mParentEntry;
    private int mTextColor;
    BackStackRecord mTopEntry;

    @Deprecated
    public interface OnBreadCrumbClickListener {
        boolean onBreadCrumbClick(BackStackEntry backStackEntry, int i);
    }

    public FragmentBreadCrumbs(Context context) {
        this(context, null);
    }

    public FragmentBreadCrumbs(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.fragmentBreadCrumbsStyle);
    }

    public FragmentBreadCrumbs(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public FragmentBreadCrumbs(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mMaxVisible = -1;
        this.mOnClickListener = new OnClickListener() {
            public void onClick(View v) {
                if (v.getTag() instanceof BackStackEntry) {
                    BackStackEntry bse = (BackStackEntry) v.getTag();
                    if (bse != FragmentBreadCrumbs.this.mParentEntry) {
                        if (FragmentBreadCrumbs.this.mOnBreadCrumbClickListener != null) {
                            if (FragmentBreadCrumbs.this.mOnBreadCrumbClickListener.onBreadCrumbClick(bse == FragmentBreadCrumbs.this.mTopEntry ? null : bse, 0)) {
                                return;
                            }
                        }
                        if (bse == FragmentBreadCrumbs.this.mTopEntry) {
                            FragmentBreadCrumbs.this.mActivity.getFragmentManager().popBackStack();
                        } else {
                            FragmentBreadCrumbs.this.mActivity.getFragmentManager().popBackStack(bse.getId(), 0);
                        }
                    } else if (FragmentBreadCrumbs.this.mParentClickListener != null) {
                        FragmentBreadCrumbs.this.mParentClickListener.onClick(v);
                    }
                }
            }
        };
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FragmentBreadCrumbs, defStyleAttr, defStyleRes);
        this.mGravity = a.getInt(0, DEFAULT_GRAVITY);
        this.mLayoutResId = a.getResourceId(2, R.layout.fragment_bread_crumb_item);
        this.mTextColor = a.getColor(1, 0);
        a.recycle();
    }

    public void setActivity(Activity a) {
        this.mActivity = a;
        this.mInflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContainer = (LinearLayout) this.mInflater.inflate((int) R.layout.fragment_bread_crumbs, (ViewGroup) this, false);
        addView(this.mContainer);
        a.getFragmentManager().addOnBackStackChangedListener(this);
        updateCrumbs();
        setLayoutTransition(new LayoutTransition());
    }

    public void setMaxVisible(int visibleCrumbs) {
        if (visibleCrumbs >= 1) {
            this.mMaxVisible = visibleCrumbs;
            return;
        }
        throw new IllegalArgumentException("visibleCrumbs must be greater than zero");
    }

    public void setParentTitle(CharSequence title, CharSequence shortTitle, OnClickListener listener) {
        this.mParentEntry = createBackStackEntry(title, shortTitle);
        this.mParentClickListener = listener;
        updateCrumbs();
    }

    public void setOnBreadCrumbClickListener(OnBreadCrumbClickListener listener) {
        this.mOnBreadCrumbClickListener = listener;
    }

    private BackStackRecord createBackStackEntry(CharSequence title, CharSequence shortTitle) {
        if (title == null) {
            return null;
        }
        BackStackRecord entry = new BackStackRecord((FragmentManagerImpl) this.mActivity.getFragmentManager());
        entry.setBreadCrumbTitle(title);
        entry.setBreadCrumbShortTitle(shortTitle);
        return entry;
    }

    public void setTitle(CharSequence title, CharSequence shortTitle) {
        this.mTopEntry = createBackStackEntry(title, shortTitle);
        updateCrumbs();
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() != 0) {
            int childRight;
            View child = getChildAt(null);
            int childTop = this.mPaddingTop;
            int childBottom = (this.mPaddingTop + child.getMeasuredHeight()) - this.mPaddingBottom;
            int absoluteGravity = Gravity.getAbsoluteGravity(this.mGravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK, getLayoutDirection());
            if (absoluteGravity == 1) {
                absoluteGravity = this.mPaddingLeft + (((this.mRight - this.mLeft) - child.getMeasuredWidth()) / 2);
                childRight = child.getMeasuredWidth() + absoluteGravity;
            } else if (absoluteGravity != 5) {
                absoluteGravity = this.mPaddingLeft;
                childRight = child.getMeasuredWidth() + absoluteGravity;
            } else {
                childRight = (this.mRight - this.mLeft) - this.mPaddingRight;
                absoluteGravity = childRight - child.getMeasuredWidth();
            }
            if (absoluteGravity < this.mPaddingLeft) {
                absoluteGravity = this.mPaddingLeft;
            }
            if (childRight > (this.mRight - this.mLeft) - this.mPaddingRight) {
                childRight = (this.mRight - this.mLeft) - this.mPaddingRight;
            }
            child.layout(absoluteGravity, childTop, childRight, childBottom);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        int maxHeight = 0;
        int maxWidth = 0;
        int measuredChildState = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth());
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
                measuredChildState = View.combineMeasuredStates(measuredChildState, child.getMeasuredState());
            }
        }
        setMeasuredDimension(View.resolveSizeAndState(Math.max(maxWidth + (this.mPaddingLeft + this.mPaddingRight), getSuggestedMinimumWidth()), widthMeasureSpec, measuredChildState), View.resolveSizeAndState(Math.max(maxHeight + (this.mPaddingTop + this.mPaddingBottom), getSuggestedMinimumHeight()), heightMeasureSpec, measuredChildState << 16));
    }

    public void onBackStackChanged() {
        updateCrumbs();
    }

    private int getPreEntryCount() {
        int i = 1;
        int i2 = this.mTopEntry != null ? 1 : 0;
        if (this.mParentEntry == null) {
            i = 0;
        }
        return i2 + i;
    }

    private BackStackEntry getPreEntry(int index) {
        BackStackEntry backStackEntry = this.mParentEntry;
        if (backStackEntry == null) {
            return this.mTopEntry;
        }
        if (index != 0) {
            backStackEntry = this.mTopEntry;
        }
        return backStackEntry;
    }

    /* Access modifiers changed, original: 0000 */
    public void updateCrumbs() {
        int j;
        FragmentManager fm = this.mActivity.getFragmentManager();
        int numEntries = fm.getBackStackEntryCount();
        int numPreEntries = getPreEntryCount();
        int numViews = this.mContainer.getChildCount();
        int i = 0;
        while (i < numEntries + numPreEntries) {
            BackStackEntry bse;
            if (i < numPreEntries) {
                bse = getPreEntry(i);
            } else {
                bse = fm.getBackStackEntryAt(i - numPreEntries);
            }
            if (i < numViews && this.mContainer.getChildAt(i).getTag() != bse) {
                for (j = i; j < numViews; j++) {
                    this.mContainer.removeViewAt(i);
                }
                numViews = i;
            }
            if (i >= numViews) {
                View item = this.mInflater.inflate(this.mLayoutResId, (ViewGroup) this, false);
                TextView text = (TextView) item.findViewById(16908310);
                text.setText(bse.getBreadCrumbTitle());
                text.setTag(bse);
                text.setTextColor(this.mTextColor);
                if (i == 0) {
                    item.findViewById(R.id.left_icon).setVisibility(8);
                }
                this.mContainer.addView(item);
                text.setOnClickListener(this.mOnClickListener);
            }
            i++;
        }
        i = numEntries + numPreEntries;
        numViews = this.mContainer.getChildCount();
        while (numViews > i) {
            this.mContainer.removeViewAt(numViews - 1);
            numViews--;
        }
        int i2 = 0;
        while (i2 < numViews) {
            View child = this.mContainer.getChildAt(i2);
            child.findViewById(16908310).setEnabled(i2 < numViews + -1);
            int i3 = this.mMaxVisible;
            if (i3 > 0) {
                child.setVisibility(i2 < numViews - i3 ? 8 : 0);
                View leftIcon = child.findViewById(R.id.left_icon);
                if (i2 <= numViews - this.mMaxVisible || i2 == 0) {
                    j = 8;
                } else {
                    j = 0;
                }
                leftIcon.setVisibility(j);
            }
            i2++;
        }
    }
}
