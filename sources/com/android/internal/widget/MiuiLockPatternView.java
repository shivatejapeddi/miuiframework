package com.android.internal.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.miui.R;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.widget.LockPatternView.Cell;
import com.android.internal.widget.LockPatternView.DisplayMode;
import com.android.internal.widget.LockPatternView.OnPatternListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MiuiLockPatternView extends View {
    private static final int ASPECT_LOCK_HEIGHT = 2;
    private static final int ASPECT_LOCK_WIDTH = 1;
    private static final int ASPECT_SQUARE = 0;
    private static final int MILLIS_PER_CIRCLE_ANIMATING = 700;
    private static final boolean PROFILE_DRAWING = false;
    static final int STATUS_BAR_HEIGHT = 25;
    private static final String TAG = "LockPatternView";
    private long mAnimatingPeriodStart;
    private final Matrix mArrowMatrix;
    private int mAspect;
    private Bitmap mBitmapArrowGreenUp;
    private Bitmap mBitmapArrowRedUp;
    private Bitmap mBitmapBtnError;
    private Bitmap mBitmapBtnTouched;
    private Bitmap mBitmapCircleDefault;
    private Bitmap mBitmapCircleGreen;
    private Bitmap mBitmapCircleRed;
    private int mBitmapHeight;
    private int mBitmapWidth;
    private final Matrix mCircleMatrix;
    private final Path mCurrentPath;
    private float mDiameterFactor;
    private boolean mDrawingProfilingStarted;
    private boolean mEnableHapticFeedback;
    private float mHitFactor;
    private float mInProgressX;
    private float mInProgressY;
    private boolean mInStealthMode;
    private boolean mInputEnabled;
    private final Rect mInvalidate;
    private OnPatternListener mOnPatternListener;
    private Paint mPaint;
    private Paint mPathPaint;
    private int mPathPaintColor;
    private int mPathPaintErrorColor;
    private ArrayList<Cell> mPattern;
    private DisplayMode mPatternDisplayMode;
    private boolean[][] mPatternDrawLookup;
    private boolean mPatternInProgress;
    private float mSquareHeight;
    private float mSquareWidth;
    private final int mStrokeAlpha;

    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        private final int mDisplayMode;
        private final boolean mInStealthMode;
        private final boolean mInputEnabled;
        private final String mSerializedPattern;
        private final boolean mTactileFeedbackEnabled;

        private SavedState(Parcelable superState, String serializedPattern, int displayMode, boolean inputEnabled, boolean inStealthMode, boolean tactileFeedbackEnabled) {
            super(superState);
            this.mSerializedPattern = serializedPattern;
            this.mDisplayMode = displayMode;
            this.mInputEnabled = inputEnabled;
            this.mInStealthMode = inStealthMode;
            this.mTactileFeedbackEnabled = tactileFeedbackEnabled;
        }

        private SavedState(Parcel in) {
            super(in);
            this.mSerializedPattern = in.readString();
            this.mDisplayMode = in.readInt();
            this.mInputEnabled = ((Boolean) in.readValue(null)).booleanValue();
            this.mInStealthMode = ((Boolean) in.readValue(null)).booleanValue();
            this.mTactileFeedbackEnabled = ((Boolean) in.readValue(null)).booleanValue();
        }

        public String getSerializedPattern() {
            return this.mSerializedPattern;
        }

        public int getDisplayMode() {
            return this.mDisplayMode;
        }

        public boolean isInputEnabled() {
            return this.mInputEnabled;
        }

        public boolean isInStealthMode() {
            return this.mInStealthMode;
        }

        public boolean isTactileFeedbackEnabled() {
            return this.mTactileFeedbackEnabled;
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.mSerializedPattern);
            dest.writeInt(this.mDisplayMode);
            dest.writeValue(Boolean.valueOf(this.mInputEnabled));
            dest.writeValue(Boolean.valueOf(this.mInStealthMode));
            dest.writeValue(Boolean.valueOf(this.mTactileFeedbackEnabled));
        }
    }

    public MiuiLockPatternView(Context context) {
        this(context, null);
    }

    public MiuiLockPatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int i = 0;
        this.mDrawingProfilingStarted = false;
        this.mPaint = new Paint();
        this.mPathPaint = new Paint();
        this.mPattern = new ArrayList(9);
        this.mPatternDrawLookup = (boolean[][]) Array.newInstance(boolean.class, new int[]{3, 3});
        this.mInProgressX = -1.0f;
        this.mInProgressY = -1.0f;
        this.mPatternDisplayMode = DisplayMode.Correct;
        this.mInputEnabled = true;
        this.mInStealthMode = false;
        this.mEnableHapticFeedback = true;
        this.mPatternInProgress = false;
        this.mDiameterFactor = 0.1f;
        this.mStrokeAlpha = 128;
        this.mHitFactor = 0.6f;
        this.mCurrentPath = new Path();
        this.mInvalidate = new Rect();
        this.mArrowMatrix = new Matrix();
        this.mCircleMatrix = new Matrix();
        String aspect = context.obtainStyledAttributes(attrs, R.styleable.LockPatternView).getString(2);
        if ("square".equals(aspect)) {
            this.mAspect = 0;
        } else if ("lock_width".equals(aspect)) {
            this.mAspect = 1;
        } else if ("lock_height".equals(aspect)) {
            this.mAspect = 2;
        } else {
            this.mAspect = 0;
        }
        setClickable(true);
        this.mPathPaintColor = getResources().getColor(R.color.pattern_lockscreen_paint_color);
        this.mPathPaintErrorColor = getResources().getColor(R.color.pattern_lockscreen_paint_error_color);
        this.mPathPaint.setAntiAlias(true);
        this.mPathPaint.setDither(true);
        this.mPathPaint.setAlpha(128);
        this.mPathPaint.setStyle(Style.STROKE);
        this.mPathPaint.setStrokeJoin(Join.ROUND);
        this.mPathPaint.setStrokeCap(Cap.ROUND);
        this.mBitmapBtnError = getBitmapFor(R.drawable.btn_code_lock_error_holo);
        this.mBitmapBtnTouched = getBitmapFor(R.drawable.btn_code_lock_touched_holo);
        this.mBitmapCircleDefault = getBitmapFor(R.drawable.indicator_code_lock_point_area_default_holo);
        this.mBitmapCircleGreen = getBitmapFor(R.drawable.indicator_code_lock_point_area_green_holo);
        this.mBitmapCircleRed = getBitmapFor(R.drawable.indicator_code_lock_point_area_red_holo);
        this.mBitmapArrowGreenUp = getBitmapFor(R.drawable.indicator_code_lock_drag_direction_green_up);
        this.mBitmapArrowRedUp = getBitmapFor(R.drawable.indicator_code_lock_drag_direction_red_up);
        Bitmap[] bitmaps = new Bitmap[]{this.mBitmapBtnError, this.mBitmapBtnTouched, this.mBitmapCircleDefault, this.mBitmapCircleGreen, this.mBitmapCircleRed};
        int length = bitmaps.length;
        while (i < length) {
            Bitmap bitmap = bitmaps[i];
            this.mBitmapWidth = Math.max(this.mBitmapWidth, bitmap.getWidth());
            this.mBitmapHeight = Math.max(this.mBitmapHeight, bitmap.getHeight());
            i++;
        }
    }

    private Bitmap getBitmapFor(int resId) {
        return BitmapFactory.decodeResource(getContext().getResources(), resId);
    }

    public boolean isInStealthMode() {
        return this.mInStealthMode;
    }

    public boolean isTactileFeedbackEnabled() {
        return this.mEnableHapticFeedback;
    }

    public void setInStealthMode(boolean inStealthMode) {
        this.mInStealthMode = inStealthMode;
    }

    public void setTactileFeedbackEnabled(boolean tactileFeedbackEnabled) {
        this.mEnableHapticFeedback = tactileFeedbackEnabled;
    }

    public void setOnPatternListener(OnPatternListener onPatternListener) {
        this.mOnPatternListener = onPatternListener;
    }

    public void setPattern(DisplayMode displayMode, List<Cell> pattern) {
        this.mPattern.clear();
        this.mPattern.addAll(pattern);
        clearPatternDrawLookup();
        for (Cell cell : pattern) {
            this.mPatternDrawLookup[cell.getRow()][cell.getColumn()] = true;
        }
        setDisplayMode(displayMode);
    }

    public void setDisplayMode(DisplayMode displayMode) {
        this.mPatternDisplayMode = displayMode;
        if (displayMode == DisplayMode.Animate) {
            if (this.mPattern.size() != 0) {
                this.mAnimatingPeriodStart = SystemClock.elapsedRealtime();
                Cell first = (Cell) this.mPattern.get(0);
                this.mInProgressX = getCenterXForColumn(first.getColumn());
                this.mInProgressY = getCenterYForRow(first.getRow());
                clearPatternDrawLookup();
            } else {
                throw new IllegalStateException("you must have a pattern to animate if you want to set the display mode to animate");
            }
        }
        invalidate();
    }

    private void notifyCellAdded() {
        OnPatternListener onPatternListener = this.mOnPatternListener;
        if (onPatternListener != null) {
            onPatternListener.onPatternCellAdded(this.mPattern);
        }
        sendAccessEvent(R.string.lockscreen_access_pattern_cell_added);
    }

    private void notifyPatternStarted() {
        OnPatternListener onPatternListener = this.mOnPatternListener;
        if (onPatternListener != null) {
            onPatternListener.onPatternStart();
        }
        sendAccessEvent(R.string.lockscreen_access_pattern_start);
    }

    private void notifyPatternDetected() {
        OnPatternListener onPatternListener = this.mOnPatternListener;
        if (onPatternListener != null) {
            onPatternListener.onPatternDetected(this.mPattern);
        }
        sendAccessEvent(R.string.lockscreen_access_pattern_detected);
    }

    private void notifyPatternCleared() {
        OnPatternListener onPatternListener = this.mOnPatternListener;
        if (onPatternListener != null) {
            onPatternListener.onPatternCleared();
        }
        sendAccessEvent(R.string.lockscreen_access_pattern_cleared);
    }

    public void clearPattern() {
        resetPattern();
    }

    private void resetPattern() {
        this.mPattern.clear();
        clearPatternDrawLookup();
        this.mPatternDisplayMode = DisplayMode.Correct;
        invalidate();
    }

    private void clearPatternDrawLookup() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.mPatternDrawLookup[i][j] = false;
            }
        }
    }

    public void disableInput() {
        this.mInputEnabled = false;
    }

    public void enableInput() {
        this.mInputEnabled = true;
    }

    /* Access modifiers changed, original: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.mSquareWidth = ((float) ((w - this.mPaddingLeft) - this.mPaddingRight)) / 3.0f;
        this.mSquareHeight = ((float) ((h - this.mPaddingTop) - this.mPaddingBottom)) / 3.0f;
    }

    private int resolveMeasured(int measureSpec, int desired) {
        int specSize = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);
        if (mode == Integer.MIN_VALUE) {
            return Math.max(specSize, desired);
        }
        if (mode != 0) {
            return specSize;
        }
        return desired;
    }

    /* Access modifiers changed, original: protected */
    public int getSuggestedMinimumWidth() {
        return this.mBitmapWidth * 3;
    }

    /* Access modifiers changed, original: protected */
    public int getSuggestedMinimumHeight() {
        return this.mBitmapWidth * 3;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minimumWidth = getSuggestedMinimumWidth();
        int minimumHeight = getSuggestedMinimumHeight();
        int viewWidth = resolveMeasured(widthMeasureSpec, minimumWidth);
        int viewHeight = resolveMeasured(heightMeasureSpec, minimumHeight);
        int i = this.mAspect;
        if (i == 0) {
            i = Math.min(viewWidth, viewHeight);
            viewHeight = i;
            viewWidth = i;
        } else if (i == 1) {
            viewHeight = Math.min(viewWidth, viewHeight);
        } else if (i == 2) {
            viewWidth = Math.min(viewWidth, viewHeight);
        }
        setMeasuredDimension(viewWidth, viewHeight);
    }

    private Cell detectAndAddHit(float x, float y) {
        Cell cell = checkForNewHit(x, y);
        if (cell == null) {
            return null;
        }
        Cell fillInGapCell = null;
        ArrayList<Cell> pattern = this.mPattern;
        if (!pattern.isEmpty()) {
            Cell lastCell = (Cell) pattern.get(pattern.size() - 1);
            int dRow = cell.row - lastCell.row;
            int dColumn = cell.column - lastCell.column;
            int fillInRow = lastCell.row;
            int fillInColumn = lastCell.column;
            int i = -1;
            if (Math.abs(dRow) == 2 && Math.abs(dColumn) != 1) {
                fillInRow = lastCell.row + (dRow > 0 ? 1 : -1);
            }
            if (Math.abs(dColumn) == 2 && Math.abs(dRow) != 1) {
                int i2 = lastCell.column;
                if (dColumn > 0) {
                    i = 1;
                }
                fillInColumn = i2 + i;
            }
            fillInGapCell = Cell.of(fillInRow, fillInColumn);
        }
        if (!(fillInGapCell == null || this.mPatternDrawLookup[fillInGapCell.row][fillInGapCell.column])) {
            addCellToPattern(fillInGapCell);
        }
        addCellToPattern(cell);
        if (this.mEnableHapticFeedback) {
            performHapticFeedback(1, 3);
        }
        return cell;
    }

    private void addCellToPattern(Cell newCell) {
        this.mPatternDrawLookup[newCell.getRow()][newCell.getColumn()] = true;
        this.mPattern.add(newCell);
        notifyCellAdded();
    }

    private Cell checkForNewHit(float x, float y) {
        int rowHit = getRowHit(y);
        if (rowHit < 0) {
            return null;
        }
        int columnHit = getColumnHit(x);
        if (columnHit >= 0 && !this.mPatternDrawLookup[rowHit][columnHit]) {
            return Cell.of(rowHit, columnHit);
        }
        return null;
    }

    private int getRowHit(float y) {
        float squareHeight = this.mSquareHeight;
        float hitSize = this.mHitFactor * squareHeight;
        float offset = ((float) this.mPaddingTop) + ((squareHeight - hitSize) / 2.0f);
        for (int i = 0; i < 3; i++) {
            float hitTop = (((float) i) * squareHeight) + offset;
            if (y >= hitTop && y <= hitTop + hitSize) {
                return i;
            }
        }
        return -1;
    }

    private int getColumnHit(float x) {
        float squareWidth = this.mSquareWidth;
        float hitSize = this.mHitFactor * squareWidth;
        float offset = ((float) this.mPaddingLeft) + ((squareWidth - hitSize) / 2.0f);
        for (int i = 0; i < 3; i++) {
            float hitLeft = (((float) i) * squareWidth) + offset;
            if (x >= hitLeft && x <= hitLeft + hitSize) {
                return i;
            }
        }
        return -1;
    }

    public boolean onHoverEvent(MotionEvent event) {
        if (AccessibilityManager.getInstance(this.mContext).isTouchExplorationEnabled()) {
            int action = event.getAction();
            if (action == 7) {
                event.setAction(2);
            } else if (action == 9) {
                event.setAction(0);
            } else if (action == 10) {
                event.setAction(1);
            }
            onTouchEvent(event);
            event.setAction(action);
        }
        return super.onHoverEvent(event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.mInputEnabled || !isEnabled()) {
            return false;
        }
        int action = event.getAction();
        if (action == 0) {
            handleActionDown(event);
            return true;
        } else if (action == 1) {
            handleActionUp(event);
            return true;
        } else if (action == 2) {
            handleActionMove(event);
            return true;
        } else if (action != 3) {
            return false;
        } else {
            resetPattern();
            this.mPatternInProgress = false;
            notifyPatternCleared();
            return true;
        }
    }

    private void handleActionMove(MotionEvent event) {
        MotionEvent motionEvent = event;
        int historySize = event.getHistorySize();
        int i = 0;
        while (i < historySize + 1) {
            int historySize2;
            float x = i < historySize ? motionEvent.getHistoricalX(i) : event.getX();
            float y = i < historySize ? motionEvent.getHistoricalY(i) : event.getY();
            int patternSizePreHitDetect = this.mPattern.size();
            Cell hitCell = detectAndAddHit(x, y);
            int patternSize = this.mPattern.size();
            if (hitCell != null && patternSize == 1) {
                this.mPatternInProgress = true;
                notifyPatternStarted();
            }
            float dx = Math.abs(x - this.mInProgressX);
            float dy = Math.abs(y - this.mInProgressY);
            float f = dx + dy;
            float f2 = this.mSquareWidth;
            float f3;
            float f4;
            int i2;
            float f5;
            float f6;
            if (f > 0.01f * f2) {
                f = this.mInProgressX;
                float oldY = this.mInProgressY;
                this.mInProgressX = x;
                this.mInProgressY = y;
                if (!this.mPatternInProgress || patternSize <= 0) {
                    historySize2 = historySize;
                    f3 = x;
                    f4 = y;
                    i2 = patternSizePreHitDetect;
                    f5 = dx;
                    f6 = dy;
                    invalidate();
                } else {
                    float left;
                    float right;
                    float top;
                    float bottom;
                    float bottom2;
                    ArrayList<Cell> pattern = this.mPattern;
                    f2 = (f2 * this.mDiameterFactor) * 0.5f;
                    Cell lastCell = (Cell) pattern.get(patternSize - 1);
                    float startX = getCenterXForColumn(lastCell.column);
                    historySize2 = historySize;
                    historySize = getCenterYForRow(lastCell.row);
                    dx = this.mInvalidate;
                    if (startX < x) {
                        left = startX;
                        right = x;
                    } else {
                        left = x;
                        right = startX;
                    }
                    if (historySize < y) {
                        top = historySize;
                        bottom = y;
                    } else {
                        top = y;
                        bottom = historySize;
                    }
                    dx.set((int) (left - f2), (int) (top - f2), (int) (right + f2), (int) (bottom + f2));
                    if (startX < f) {
                        x = startX;
                        y = f;
                    } else {
                        x = f;
                        y = startX;
                    }
                    if (historySize < oldY) {
                        dy = historySize;
                        bottom2 = oldY;
                    } else {
                        dy = oldY;
                        bottom2 = historySize;
                    }
                    int startY = historySize;
                    dx.union((int) (x - f2), (int) (dy - f2), (int) (y + f2), (int) (bottom2 + f2));
                    if (hitCell != null) {
                        startX = getCenterXForColumn(hitCell.column);
                        historySize = getCenterYForRow(hitCell.row);
                        if (patternSize >= 2) {
                            Cell hitCell2 = (Cell) pattern.get((patternSize - 1) - (patternSize - patternSizePreHitDetect));
                            y = getCenterXForColumn(hitCell2.column);
                            float oldY2 = getCenterYForRow(hitCell2.row);
                            if (startX < y) {
                                f = startX;
                                oldY = y;
                            } else {
                                f = y;
                                oldY = startX;
                            }
                            float f7;
                            if (historySize < oldY2) {
                                dy = historySize;
                                bottom2 = oldY2;
                                f7 = oldY2;
                                hitCell = hitCell2;
                                x = f;
                                f = y;
                                y = oldY;
                                oldY = f7;
                            } else {
                                dy = oldY2;
                                bottom2 = historySize;
                                f7 = oldY2;
                                hitCell = hitCell2;
                                x = f;
                                y = oldY;
                                oldY = f7;
                            }
                        } else {
                            bottom2 = historySize;
                            dy = historySize;
                            y = startX;
                            x = startX;
                        }
                        left = startX;
                        startX = this.mSquareWidth / 2.0f;
                        int startY2 = historySize;
                        historySize = this.mSquareHeight / 1073741824;
                        right = x;
                        Cell hitCell3 = hitCell;
                        dx.set((int) (x - startX), (int) (dy - historySize), (int) (y + startX), (int) (bottom2 + historySize));
                        bottom = y;
                        hitCell = hitCell3;
                    }
                    invalidate((Rect) dx);
                }
            } else {
                historySize2 = historySize;
                f3 = x;
                f4 = y;
                i2 = patternSizePreHitDetect;
                f5 = dx;
                f6 = dy;
            }
            i++;
            motionEvent = event;
            historySize = historySize2;
        }
    }

    private void sendAccessEvent(int resId) {
        setContentDescription(this.mContext.getString(resId));
        sendAccessibilityEvent(4);
        setContentDescription(null);
    }

    private void handleActionUp(MotionEvent event) {
        if (!this.mPattern.isEmpty()) {
            this.mPatternInProgress = false;
            notifyPatternDetected();
            invalidate();
        }
    }

    private void handleActionDown(MotionEvent event) {
        resetPattern();
        float x = event.getX();
        float y = event.getY();
        Cell hitCell = detectAndAddHit(x, y);
        if (hitCell != null) {
            this.mPatternInProgress = true;
            this.mPatternDisplayMode = DisplayMode.Correct;
            notifyPatternStarted();
        } else {
            this.mPatternInProgress = false;
            notifyPatternCleared();
        }
        if (hitCell != null) {
            float startX = getCenterXForColumn(hitCell.column);
            float startY = getCenterYForRow(hitCell.row);
            float widthOffset = this.mSquareWidth / 2.0f;
            float heightOffset = this.mSquareHeight / 2.0f;
            invalidate((int) (startX - widthOffset), (int) (startY - heightOffset), (int) (startX + widthOffset), (int) (startY + heightOffset));
        }
        this.mInProgressX = x;
        this.mInProgressY = y;
    }

    private float getCenterXForColumn(int column) {
        float f = (float) this.mPaddingLeft;
        float f2 = (float) column;
        float f3 = this.mSquareWidth;
        return (f + (f2 * f3)) + (f3 / 2.0f);
    }

    private float getCenterYForRow(int row) {
        float f = (float) this.mPaddingTop;
        float f2 = (float) row;
        float f3 = this.mSquareHeight;
        return (f + (f2 * f3)) + (f3 / 2.0f);
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        int oneCycle;
        int spotInCycle;
        int i;
        float percentageOfNextCircle;
        float dy;
        int j;
        float leftX;
        Cell cell;
        boolean oldFlag;
        Canvas canvas2 = canvas;
        ArrayList<Cell> pattern = this.mPattern;
        int count = pattern.size();
        boolean[][] drawLookup = this.mPatternDrawLookup;
        if (this.mPatternDisplayMode == DisplayMode.Animate) {
            oneCycle = (count + 1) * 700;
            spotInCycle = ((int) (SystemClock.elapsedRealtime() - this.mAnimatingPeriodStart)) % oneCycle;
            int numCircles = spotInCycle / 700;
            clearPatternDrawLookup();
            for (i = 0; i < numCircles; i++) {
                Cell cell2 = (Cell) pattern.get(i);
                drawLookup[cell2.getRow()][cell2.getColumn()] = true;
            }
            boolean needToUpdateInProgressPoint = numCircles > 0 && numCircles < count;
            if (needToUpdateInProgressPoint) {
                percentageOfNextCircle = ((float) (spotInCycle % 700)) / 700.0f;
                Cell currentCell = (Cell) pattern.get(numCircles - 1);
                float centerX = getCenterXForColumn(currentCell.column);
                float centerY = getCenterYForRow(currentCell.row);
                Cell nextCell = (Cell) pattern.get(numCircles);
                dy = (getCenterYForRow(nextCell.row) - centerY) * percentageOfNextCircle;
                this.mInProgressX = centerX + ((getCenterXForColumn(nextCell.column) - centerX) * percentageOfNextCircle);
                this.mInProgressY = centerY + dy;
            }
            invalidate();
        }
        percentageOfNextCircle = this.mSquareWidth;
        float squareHeight = this.mSquareHeight;
        this.mPathPaint.setStrokeWidth(percentageOfNextCircle * this.mDiameterFactor);
        Path currentPath = this.mCurrentPath;
        currentPath.rewind();
        int paddingTop = this.mPaddingTop;
        i = this.mPaddingLeft;
        oneCycle = 0;
        while (true) {
            spotInCycle = 3;
            if (oneCycle >= 3) {
                break;
            }
            float topY = ((float) paddingTop) + (((float) oneCycle) * squareHeight);
            j = 0;
            while (j < spotInCycle) {
                leftX = ((float) i) + (((float) j) * percentageOfNextCircle);
                float topY2 = topY;
                drawCircle(canvas2, (int) leftX, (int) topY, drawLookup[oneCycle][j]);
                j++;
                topY = topY2;
                spotInCycle = 3;
            }
            oneCycle++;
        }
        boolean z = !this.mInStealthMode || this.mPatternDisplayMode == DisplayMode.Wrong;
        boolean drawPath = z;
        boolean oldFlag2 = (this.mPaint.getFlags() & 2) != 0;
        this.mPaint.setFilterBitmap(true);
        this.mPathPaint.setColor(this.mPatternDisplayMode == DisplayMode.Wrong ? this.mPathPaintErrorColor : this.mPathPaintColor);
        float f;
        if (drawPath) {
            int i2;
            int i3;
            j = 0;
            while (j < count - 1) {
                cell = (Cell) pattern.get(j);
                Cell next = (Cell) pattern.get(j + 1);
                i2 = j;
                if (!drawLookup[next.row][next.column]) {
                    i3 = i;
                    f = percentageOfNextCircle;
                    oldFlag = oldFlag2;
                    break;
                }
                f = percentageOfNextCircle;
                oldFlag = oldFlag2;
                i3 = i;
                drawArrow(canvas, ((float) i) + (((float) cell.column) * percentageOfNextCircle), ((float) paddingTop) + (((float) cell.row) * squareHeight), cell, next);
                j = i2 + 1;
                oldFlag2 = oldFlag;
                percentageOfNextCircle = f;
                i = i3;
            }
            i2 = j;
            i3 = i;
            f = percentageOfNextCircle;
            oldFlag = oldFlag2;
        } else {
            f = percentageOfNextCircle;
            oldFlag = oldFlag2;
        }
        if (drawPath) {
            z = false;
            for (spotInCycle = 0; spotInCycle < count; spotInCycle++) {
                cell = (Cell) pattern.get(spotInCycle);
                if (!drawLookup[cell.row][cell.column]) {
                    break;
                }
                z = true;
                dy = getCenterXForColumn(cell.column);
                leftX = getCenterYForRow(cell.row);
                if (spotInCycle == 0) {
                    currentPath.moveTo(dy, leftX);
                } else {
                    currentPath.lineTo(dy, leftX);
                }
            }
            if ((this.mPatternInProgress || this.mPatternDisplayMode == DisplayMode.Animate) && anyCircles) {
                currentPath.lineTo(this.mInProgressX, this.mInProgressY);
            }
            canvas2.drawPath(currentPath, this.mPathPaint);
        }
        this.mPaint.setFilterBitmap(oldFlag);
    }

    private void drawArrow(Canvas canvas, float leftX, float topY, Cell start, Cell end) {
        Cell cell = start;
        Cell cell2 = end;
        boolean green = this.mPatternDisplayMode != DisplayMode.Wrong;
        int endRow = cell2.row;
        int startRow = cell.row;
        int endColumn = cell2.column;
        int startColumn = cell.column;
        int offsetX = (((int) this.mSquareWidth) - this.mBitmapWidth) / 2;
        int offsetY = (((int) this.mSquareHeight) - this.mBitmapHeight) / 2;
        Bitmap arrow = green ? this.mBitmapArrowGreenUp : this.mBitmapArrowRedUp;
        int cellWidth = this.mBitmapWidth;
        int cellHeight = this.mBitmapHeight;
        float theta = (float) Math.atan2((double) (endRow - startRow), (double) (endColumn - startColumn));
        float angle = ((float) Math.toDegrees((double) theta)) + 90.0f;
        float sx = Math.min(this.mSquareWidth / ((float) this.mBitmapWidth), 1.0f);
        float sy = Math.min(this.mSquareHeight / ((float) this.mBitmapHeight), 1.0f);
        this.mArrowMatrix.setTranslate(leftX + ((float) offsetX), topY + ((float) offsetY));
        this.mArrowMatrix.preTranslate((float) (this.mBitmapWidth / 2), (float) (this.mBitmapHeight / 2));
        this.mArrowMatrix.preScale(sx, sy);
        this.mArrowMatrix.preTranslate((float) ((-this.mBitmapWidth) / 2), (float) ((-this.mBitmapHeight) / 2));
        this.mArrowMatrix.preRotate(angle, ((float) cellWidth) / 2.0f, ((float) cellHeight) / 2.0f);
        this.mArrowMatrix.preTranslate(((float) (cellWidth - arrow.getWidth())) / 2.0f, 0.0f);
        canvas.drawBitmap(arrow, this.mArrowMatrix, this.mPaint);
    }

    private void drawCircle(Canvas canvas, int leftX, int topY, boolean partOfPattern) {
        Bitmap outerCircle;
        Bitmap innerCircle;
        Canvas canvas2 = canvas;
        if (!partOfPattern || (this.mInStealthMode && this.mPatternDisplayMode != DisplayMode.Wrong)) {
            outerCircle = this.mBitmapCircleDefault;
            innerCircle = null;
        } else if (this.mPatternInProgress) {
            outerCircle = this.mBitmapCircleGreen;
            innerCircle = this.mBitmapBtnTouched;
        } else if (this.mPatternDisplayMode == DisplayMode.Wrong) {
            outerCircle = this.mBitmapCircleRed;
            innerCircle = this.mBitmapBtnError;
        } else if (this.mPatternDisplayMode == DisplayMode.Correct || this.mPatternDisplayMode == DisplayMode.Animate) {
            outerCircle = this.mBitmapCircleGreen;
            innerCircle = null;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("unknown display mode ");
            stringBuilder.append(this.mPatternDisplayMode);
            throw new IllegalStateException(stringBuilder.toString());
        }
        int width = this.mBitmapWidth;
        int height = this.mBitmapHeight;
        int offsetX = (int) ((this.mSquareWidth - ((float) width)) / 1073741824);
        int offsetY = (int) ((this.mSquareHeight - ((float) height)) / 2.0f);
        float sx = Math.min(this.mSquareWidth / ((float) this.mBitmapWidth), 1.0f);
        float sy = Math.min(this.mSquareHeight / ((float) this.mBitmapHeight), 1.0f);
        this.mCircleMatrix.setTranslate((float) (leftX + offsetX), (float) (topY + offsetY));
        this.mCircleMatrix.preTranslate((float) (this.mBitmapWidth / 2), (float) (this.mBitmapHeight / 2));
        this.mCircleMatrix.preScale(sx, sy);
        this.mCircleMatrix.preTranslate((float) ((-this.mBitmapWidth) / 2), (float) ((-this.mBitmapHeight) / 2));
        canvas2.drawBitmap(outerCircle, this.mCircleMatrix, this.mPaint);
        if (innerCircle != null) {
            canvas2.drawBitmap(innerCircle, this.mCircleMatrix, this.mPaint);
        }
    }

    /* Access modifiers changed, original: protected */
    public Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), LockPatternUtils.patternToString(this.mPattern), this.mPatternDisplayMode.ordinal(), this.mInputEnabled, this.mInStealthMode, this.mEnableHapticFeedback);
    }

    /* Access modifiers changed, original: protected */
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setPattern(DisplayMode.Correct, LockPatternUtils.stringToPattern(ss.getSerializedPattern()));
        this.mPatternDisplayMode = DisplayMode.values()[ss.getDisplayMode()];
        this.mInputEnabled = ss.isInputEnabled();
        this.mInStealthMode = ss.isInStealthMode();
        this.mEnableHapticFeedback = ss.isTactileFeedbackEnabled();
    }
}
