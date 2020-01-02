package miui.view;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.miui.R;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Iterator;

public class MiuiKeyBoardView extends FrameLayout implements OnClickListener, OnTouchListener {
    private static final float FUNC_KEY_RATIO = 1.6f;
    private static final float HORIZONTAL_MARGIN_RATIO = 0.2f;
    private static final float OK_KEY_RATIO = 2.8f;
    private static final int PREVIEW_ANIMATION_DURATION = 100;
    private static final long SHOW_PREVIEW_DURATION = 300;
    private static final float[][] SIZE_GROUP = new float[][]{new float[]{1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f}, new float[]{1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f}, new float[]{1.6f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.6f}, new float[]{OK_KEY_RATIO, SPACE_KEY_RATIO, OK_KEY_RATIO}};
    private static final float SPACE_KEY_RATIO = 5.8f;
    private static final String SPACE_STR = " ";
    private static final float VERTICAL_MARGIN_RATIO = 0.17f;
    private ArrayList<KeyButton> mAllKeys;
    private View mBtnCapsLock;
    private View mBtnLetterDelete;
    private View mBtnLetterOK;
    private View mBtnLetterSpace;
    private View mBtnSymbolDelete;
    private View mBtnSymbolOK;
    private View mBtnSymbolSpace;
    private View mBtnToLetterBoard;
    private View mBtnToSymbolBoard;
    private Runnable mConfirmHide;
    private Context mContext;
    private boolean mIsShowingPreview;
    private boolean mIsUpperMode;
    private ArrayList<OnKeyboardActionListener> mKeyboardListeners;
    private FrameLayout mLetterBoard;
    private int mPopupViewHeight;
    private int mPopupViewWidth;
    private int mPopupViewX;
    private int mPopupViewY;
    private TextView mPreviewText;
    private final Runnable mSendDeleteActionRunnable;
    private ValueAnimator mShowPreviewAnimator;
    private long mShowPreviewLastTime;
    private Animation mShrinkToBottonAnimation;
    private Animation mStretchFromBottonAnimation;
    private FrameLayout mSymbolBoard;

    public static class KeyButton extends TextView {
        public KeyButton(Context context) {
            super(context);
        }

        public KeyButton(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public KeyButton(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        /* Access modifiers changed, original: protected */
        public void onFinishInflate() {
            if (getTag() instanceof String) {
                setText((CharSequence) (String) getTag());
            }
            super.onFinishInflate();
        }

        public void layout(int l, int t, int r, int b) {
            measure(MeasureSpec.makeMeasureSpec(r - l, 1073741824), MeasureSpec.makeMeasureSpec(b - t, 1073741824));
            super.layout(l, t, r, b);
        }
    }

    public interface OnKeyboardActionListener {
        void onKeyBoardDelete();

        void onKeyBoardOK();

        void onText(CharSequence charSequence);
    }

    public MiuiKeyBoardView(Context context) {
        this(context, null);
    }

    public MiuiKeyBoardView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MiuiKeyBoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mAllKeys = new ArrayList();
        this.mIsUpperMode = false;
        this.mIsShowingPreview = false;
        this.mShowPreviewLastTime = 0;
        this.mShowPreviewAnimator = new ValueAnimator();
        this.mStretchFromBottonAnimation = null;
        this.mShrinkToBottonAnimation = null;
        this.mSendDeleteActionRunnable = new Runnable() {
            public void run() {
                MiuiKeyBoardView.this.onKeyBoardDelete();
                MiuiKeyBoardView.this.postDelayed(this, 50);
            }
        };
        this.mConfirmHide = new Runnable() {
            public void run() {
                MiuiKeyBoardView.this.showPreviewAnim(false);
            }
        };
        this.mContext = context;
        View.inflate(this.mContext, R.layout.letter_board, this);
        View.inflate(this.mContext, R.layout.symbol_board, this);
        View.inflate(this.mContext, R.layout.key_preview_text, this);
        setFocusableInTouchMode(true);
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        if (getParent() != null) {
            ((ViewGroup) getParent()).setClipChildren(false);
        }
        super.onAttachedToWindow();
    }

    /* Access modifiers changed, original: protected */
    public void onFinishInflate() {
        Resources res = this.mContext.getResources();
        this.mPaddingTop = res.getDimensionPixelSize(R.dimen.keyboard_padding_top);
        this.mPaddingLeft = res.getDimensionPixelSize(R.dimen.keyboard_padding_left);
        this.mStretchFromBottonAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.stretch_from_bottom);
        this.mShrinkToBottonAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shrink_to_bottom);
        this.mKeyboardListeners = new ArrayList();
        setClipChildren(false);
        setClipToPadding(false);
        this.mPreviewText = (TextView) findViewById(R.id.preview_text);
        this.mLetterBoard = (FrameLayout) findViewById(R.id.keyboard_letter);
        this.mLetterBoard.setVisibility(0);
        this.mBtnCapsLock = findViewById(R.id.btn_caps_lock);
        this.mBtnLetterDelete = findViewById(R.id.btn_letter_delete);
        this.mBtnToSymbolBoard = findViewById(R.id.btn_shift2symbol);
        this.mBtnLetterSpace = findViewById(R.id.btn_letter_space);
        this.mBtnLetterOK = findViewById(R.id.btn_letter_ok);
        this.mSymbolBoard = (FrameLayout) findViewById(R.id.keyboard_symbol);
        this.mSymbolBoard.setVisibility(4);
        this.mBtnSymbolDelete = findViewById(R.id.btn_symbol_delete);
        this.mBtnToLetterBoard = findViewById(R.id.btn_shift2letter);
        this.mBtnSymbolSpace = findViewById(R.id.btn_symbol_space);
        this.mBtnSymbolOK = findViewById(R.id.btn_symbol_ok);
        setOnTouchAndClickListenerForKey(this.mLetterBoard);
        setOnTouchAndClickListenerForKey(this.mSymbolBoard);
    }

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }

    private void setOnTouchAndClickListenerForKey(ViewGroup group) {
        int childCount = group.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = group.getChildAt(i);
            if (child instanceof KeyButton) {
                child.setOnClickListener(this);
                child.setOnTouchListener(this);
                this.mAllKeys.add((KeyButton) child);
            } else if (child instanceof ViewGroup) {
                setOnTouchAndClickListenerForKey((ViewGroup) child);
            }
        }
    }

    private float getChildCoordRelativeToKeyboard(View descendant, float[] coord, boolean useTransformation, boolean ignoreScale) {
        coord[1] = 0.0f;
        coord[0] = 0.0f;
        if (useTransformation) {
            descendant.getMatrix().mapPoints(coord);
        }
        float scale = 1.0f * descendant.getScaleX();
        coord[0] = coord[0] + ((float) descendant.getLeft());
        coord[1] = coord[1] + ((float) descendant.getTop());
        View viewParent = descendant.getParent();
        while ((viewParent instanceof View) && viewParent != this) {
            View view = viewParent;
            if (useTransformation) {
                view.getMatrix().mapPoints(coord);
                scale *= view.getScaleX();
            }
            coord[0] = coord[0] + ((float) (view.getLeft() - view.getScrollX()));
            coord[1] = coord[1] + ((float) (view.getTop() - view.getScrollY()));
            viewParent = view.getParent();
        }
        if (ignoreScale) {
            coord[0] = coord[0] - ((((float) descendant.getWidth()) * (1.0f - scale)) / 2.0f);
            coord[1] = coord[1] - ((((float) descendant.getHeight()) * (1.0f - scale)) / 2.0f);
        }
        return scale;
    }

    public void addKeyboardListener(OnKeyboardActionListener listener) {
        Iterator it = this.mKeyboardListeners.iterator();
        while (it.hasNext()) {
            if (listener.equals((OnKeyboardActionListener) it.next())) {
                return;
            }
        }
        this.mKeyboardListeners.add(listener);
    }

    public void removeKeyboardListener(OnKeyboardActionListener listener) {
        this.mKeyboardListeners.remove(listener);
    }

    private void onText(CharSequence text) {
        Iterator it = this.mKeyboardListeners.iterator();
        while (it.hasNext()) {
            ((OnKeyboardActionListener) it.next()).onText(text);
        }
    }

    private void onKeyBoardDelete() {
        Iterator it = this.mKeyboardListeners.iterator();
        while (it.hasNext()) {
            ((OnKeyboardActionListener) it.next()).onKeyBoardDelete();
        }
    }

    private void onKeyBoardOK() {
        Iterator it = this.mKeyboardListeners.iterator();
        while (it.hasNext()) {
            ((OnKeyboardActionListener) it.next()).onKeyBoardOK();
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        if (isEnabled()) {
            int action = event.getAction();
            if (action == 0) {
                if ((v.getTag() instanceof String) && ((String) v.getTag()).length() == 1) {
                    calcAndStartShowPreviewAnim(v);
                }
                if (v == this.mBtnLetterDelete || v == this.mBtnSymbolDelete) {
                    postDelayed(this.mSendDeleteActionRunnable, 500);
                }
            } else if (action == 1 || action == 3) {
                long hideDelayed = SHOW_PREVIEW_DURATION - (System.currentTimeMillis() - this.mShowPreviewLastTime);
                if (this.mIsShowingPreview) {
                    Runnable runnable = this.mConfirmHide;
                    long j = 0;
                    if (hideDelayed > 0) {
                        j = hideDelayed;
                    }
                    postDelayed(runnable, j);
                }
                if (v == this.mBtnLetterDelete || v == this.mBtnSymbolDelete) {
                    removeCallbacks(this.mSendDeleteActionRunnable);
                }
            }
        }
        return false;
    }

    public void onClick(View v) {
        if (isEnabled()) {
            if (v == this.mBtnCapsLock) {
                shiftLetterBoard();
            } else if (v == this.mBtnToSymbolBoard) {
                showLetterBoard(false);
            } else if (v == this.mBtnToLetterBoard) {
                showLetterBoard(true);
            } else if (v == this.mBtnLetterDelete || v == this.mBtnSymbolDelete) {
                onKeyBoardDelete();
            } else if (v == this.mBtnSymbolOK || v == this.mBtnLetterOK) {
                onKeyBoardOK();
            } else if (v == this.mBtnSymbolSpace || v == this.mBtnLetterSpace) {
                onText(" ");
            } else {
                onText(((KeyButton) v).getText());
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int keyboardWidth = right - left;
        int normalKeyWidth = (int) (((float) (((keyboardWidth - (this.mPaddingLeft * 2)) / SIZE_GROUP[0].length) * 1)) / 1.2f);
        int horizontalMargin = (int) (((float) normalKeyWidth) * 0.2f);
        int normalKeyHeight = (int) (((float) ((((bottom - top) - (this.mPaddingTop * 2)) / SIZE_GROUP.length) * 1)) / 1.17f);
        int verticalMargin = (int) (((float) normalKeyHeight) * 0.2f);
        this.mLetterBoard.layout(0, 0, keyboardWidth, bottom - top);
        this.mSymbolBoard.layout(0, 0, keyboardWidth, bottom - top);
        int i = keyboardWidth;
        int i2 = normalKeyWidth;
        int i3 = horizontalMargin;
        int i4 = normalKeyHeight;
        int i5 = verticalMargin;
        keyboardOnLayout(this.mLetterBoard, i, i2, i3, i4, i5);
        keyboardOnLayout(this.mSymbolBoard, i, i2, i3, i4, i5);
        TextView textView = this.mPreviewText;
        int i6 = this.mPopupViewX;
        i = this.mPopupViewY;
        textView.layout(i6, i, this.mPopupViewWidth + i6, this.mPopupViewHeight + i);
    }

    /* Access modifiers changed, original: 0000 */
    public void keyboardOnLayout(ViewGroup board, int width, int normalKeyWidth, int horizontalMargin, int normalKeyHeight, int verticalMargin) {
        int i = normalKeyWidth;
        int i2 = horizontalMargin;
        int rowCount = SIZE_GROUP.length;
        int index = 0;
        int paddingTop = this.mPaddingTop;
        int i3 = 0;
        while (i3 < rowCount) {
            int rowCount2;
            float[] row = SIZE_GROUP[i3];
            float totalSize = 0.0f;
            for (float f : row) {
                totalSize += f * ((float) i);
            }
            int padding = (int) ((((float) width) - (totalSize + ((float) ((row.length - 1) * i2)))) / 1073741824);
            int j = 0;
            while (j < row.length) {
                KeyButton btn = (KeyButton) board.getChildAt(index);
                int paddingLeft = padding;
                rowCount2 = rowCount;
                if ("!".equals(btn.getText())) {
                    paddingLeft = (int) (((float) paddingLeft) + (((float) i) * (row[j] - 1.0f)));
                }
                btn.layout(paddingLeft, paddingTop, (int) (((float) padding) + (((float) i) * row[j])), paddingTop + normalKeyHeight);
                padding = (int) (((float) padding) + ((((float) i) * row[j]) + ((float) i2)));
                index++;
                j++;
                i = normalKeyWidth;
                rowCount = rowCount2;
            }
            ViewGroup viewGroup = board;
            rowCount2 = rowCount;
            paddingTop += verticalMargin + normalKeyHeight;
            i3++;
            i = normalKeyWidth;
        }
    }

    public void show() {
        this.mLetterBoard.setVisibility(4);
        this.mSymbolBoard.setVisibility(0);
        if (this.mIsUpperMode) {
            shiftLetterBoard();
        }
        startAnimation(this.mStretchFromBottonAnimation);
    }

    public void hide() {
        startAnimation(this.mShrinkToBottonAnimation);
    }

    private void calcAndStartShowPreviewAnim(View v) {
        if (v instanceof KeyButton) {
            this.mPreviewText.setText(((KeyButton) v).getText());
            this.mPreviewText.setTypeface(Typeface.DEFAULT_BOLD);
            this.mPopupViewWidth = (int) (((double) v.getWidth()) * 1.7d);
            this.mPopupViewHeight = (int) (((double) v.getHeight()) * 1.4d);
            this.mPreviewText.setWidth(this.mPopupViewWidth);
            this.mPreviewText.setHeight(this.mPopupViewHeight);
            float[] viewPos = new float[2];
            getChildCoordRelativeToKeyboard(v, viewPos, false, true);
            this.mPopupViewX = (int) (viewPos[0] + ((float) ((v.getWidth() - this.mPopupViewWidth) / 2)));
            this.mPopupViewY = (int) ((viewPos[1] - ((float) this.mPopupViewHeight)) - (((float) v.getHeight()) * VERTICAL_MARGIN_RATIO));
            showPreviewAnim(true);
            this.mPreviewText.setVisibility(0);
        }
    }

    private void showPreviewAnim(boolean show) {
        this.mShowPreviewAnimator.cancel();
        removeCallbacks(this.mConfirmHide);
        this.mShowPreviewAnimator.removeAllListeners();
        this.mShowPreviewAnimator.removeAllUpdateListeners();
        if (show) {
            this.mShowPreviewAnimator.setFloatValues(0.0f, 1.0f);
        } else {
            this.mShowPreviewAnimator.setFloatValues(1.0f, 0.0f);
        }
        this.mShowPreviewAnimator.setDuration(100);
        this.mPreviewText.setVisibility(0);
        this.mPreviewText.setPivotX(((float) this.mPopupViewWidth) * 0.5f);
        this.mPreviewText.setPivotY((float) this.mPopupViewHeight);
        this.mShowPreviewAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                MiuiKeyBoardView.this.mPreviewText.setAlpha(((Float) animation.getAnimatedValue()).floatValue());
            }
        });
        this.mShowPreviewAnimator.start();
        this.mIsShowingPreview = show;
        if (show) {
            this.mShowPreviewLastTime = System.currentTimeMillis();
        }
    }

    private void showLetterBoard(boolean show) {
        int i = 0;
        this.mLetterBoard.setVisibility(show ? 0 : 4);
        FrameLayout frameLayout = this.mSymbolBoard;
        if (show) {
            i = 4;
        }
        frameLayout.setVisibility(i);
    }

    private void shiftLetterBoard() {
        Iterator it = this.mAllKeys.iterator();
        while (it.hasNext()) {
            KeyButton key = (KeyButton) it.next();
            if (key.getTag() instanceof String) {
                String tag = (String) key.getTag();
                if (tag.length() == 1 && Character.isLowerCase(tag.toCharArray()[0])) {
                    key.setText(this.mIsUpperMode ? tag.toLowerCase() : tag.toUpperCase());
                }
            }
        }
        this.mIsUpperMode ^= 1;
        if (this.mIsUpperMode) {
            this.mBtnCapsLock.setBackgroundResource(R.drawable.keyboard_caps_lock_highlight);
        } else {
            this.mBtnCapsLock.setBackgroundResource(R.drawable.keyboard_caps_lock);
        }
    }
}
