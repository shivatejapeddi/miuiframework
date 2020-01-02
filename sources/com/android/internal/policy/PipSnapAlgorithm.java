package com.android.internal.policy;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Size;
import android.view.Gravity;
import com.android.internal.R;
import java.io.PrintWriter;
import java.util.ArrayList;

public class PipSnapAlgorithm {
    private static final float CORNER_MAGNET_THRESHOLD = 0.3f;
    private static final int SNAP_MODE_CORNERS_AND_SIDES = 1;
    private static final int SNAP_MODE_CORNERS_ONLY = 0;
    private static final int SNAP_MODE_EDGE = 2;
    private static final int SNAP_MODE_EDGE_MAGNET_CORNERS = 3;
    private static final int SNAP_MODE_LONG_EDGE_MAGNET_CORNERS = 4;
    private final Context mContext;
    private final float mDefaultSizePercent;
    private final int mDefaultSnapMode = 3;
    private final int mFlingDeceleration;
    private boolean mIsMinimized;
    private final float mMaxAspectRatioForMinSize;
    private final float mMinAspectRatioForMinSize;
    private final int mMinimizedVisibleSize;
    private int mOrientation = 0;
    private final ArrayList<Integer> mSnapGravities = new ArrayList();
    private int mSnapMode = 3;

    public PipSnapAlgorithm(Context context) {
        Resources res = context.getResources();
        this.mContext = context;
        this.mMinimizedVisibleSize = res.getDimensionPixelSize(R.dimen.pip_minimized_visible_size);
        this.mDefaultSizePercent = res.getFloat(R.dimen.config_pictureInPictureDefaultSizePercent);
        this.mMaxAspectRatioForMinSize = res.getFloat(R.dimen.config_pictureInPictureAspectRatioLimitForMinSize);
        this.mMinAspectRatioForMinSize = 1.0f / this.mMaxAspectRatioForMinSize;
        this.mFlingDeceleration = this.mContext.getResources().getDimensionPixelSize(R.dimen.pip_fling_deceleration);
        onConfigurationChanged();
    }

    public void onConfigurationChanged() {
        Resources res = this.mContext.getResources();
        this.mOrientation = res.getConfiguration().orientation;
        this.mSnapMode = res.getInteger(R.integer.config_pictureInPictureSnapMode);
        calculateSnapTargets();
    }

    public void setMinimized(boolean isMinimized) {
        this.mIsMinimized = isMinimized;
    }

    public Rect findClosestSnapBounds(Rect movementBounds, Rect stackBounds, float velocityX, float velocityY, Point dragStartPosition) {
        Rect intersectStackBounds = new Rect(stackBounds);
        Point intersect = getEdgeIntersect(stackBounds, movementBounds, velocityX, velocityY, dragStartPosition);
        intersectStackBounds.offsetTo(intersect.x, intersect.y);
        return findClosestSnapBounds(movementBounds, intersectStackBounds);
    }

    public Point getEdgeIntersect(Rect stackBounds, Rect movementBounds, float velX, float velY, Point dragStartPosition) {
        int i;
        int maxDistance;
        Rect rect = stackBounds;
        Rect rect2 = movementBounds;
        float f = velX;
        float f2 = velY;
        Point point = dragStartPosition;
        boolean isLandscape = this.mOrientation == 2;
        int x = rect.left;
        int y = rect.top;
        float slope = f2 / f;
        float yIntercept = ((float) y) - (((float) x) * slope);
        Point vertPoint = new Point();
        Point horizPoint = new Point();
        vertPoint.x = f > 0.0f ? rect2.right : rect2.left;
        vertPoint.y = findY(slope, yIntercept, (float) vertPoint.x);
        horizPoint.y = f2 > 0.0f ? rect2.bottom : rect2.top;
        horizPoint.x = findX(slope, yIntercept, (float) horizPoint.y);
        if (isLandscape) {
            if (f > 0.0f) {
                i = rect2.right - rect.left;
            } else {
                i = rect.left - rect2.left;
            }
            maxDistance = i;
        } else if (f2 > 0.0f) {
            maxDistance = rect2.bottom - rect.top;
        } else {
            maxDistance = rect.top - rect2.top;
        }
        float f3;
        if (maxDistance > 0) {
            i = isLandscape ? point.y : point.x;
            int endPoint = isLandscape ? horizPoint.y : horizPoint.x;
            int center = movementBounds.centerX();
            if ((i >= center || endPoint >= center) && (i <= center || endPoint <= center)) {
                f3 = yIntercept;
                int i2 = endPoint;
                int i3 = i;
            } else {
                if (isLandscape) {
                    slope = (double) f;
                } else {
                    f3 = yIntercept;
                    slope = (double) f2;
                }
                int slope2 = Math.min(((int) (0.0f - Math.pow(slope, 2.0d))) / (this.mFlingDeceleration * 2), maxDistance);
                if (isLandscape) {
                    horizPoint.x = rect.left + (f > 0.0f ? slope2 : -slope2);
                } else {
                    horizPoint.y = rect.top + (f2 > 0.0f ? slope2 : -slope2);
                }
                return horizPoint;
            }
        }
        f3 = yIntercept;
        double distanceVert = Math.hypot((double) (vertPoint.x - x), (double) (vertPoint.y - y));
        double distanceHoriz = Math.hypot((double) (horizPoint.x - x), (double) (horizPoint.y - y));
        if (distanceVert == 0.0d) {
            return horizPoint;
        }
        if (distanceHoriz == 0.0d) {
            return vertPoint;
        }
        return Math.abs(distanceVert) > Math.abs(distanceHoriz) ? horizPoint : vertPoint;
    }

    private int findY(float slope, float yIntercept, float x) {
        return (int) ((slope * x) + yIntercept);
    }

    private int findX(float slope, float yIntercept, float y) {
        return (int) ((y - yIntercept) / slope);
    }

    public Rect findClosestSnapBounds(Rect movementBounds, Rect stackBounds) {
        Rect rect = movementBounds;
        Rect rect2 = stackBounds;
        Rect pipBounds = new Rect(rect.left, rect.top, rect.right + stackBounds.width(), rect.bottom + stackBounds.height());
        Rect newBounds = new Rect(rect2);
        int i = this.mSnapMode;
        if (i == 4 || i == 3) {
            Rect tmpBounds = new Rect();
            Point[] snapTargets = new Point[this.mSnapGravities.size()];
            for (int i2 = 0; i2 < this.mSnapGravities.size(); i2++) {
                Gravity.apply(((Integer) this.mSnapGravities.get(i2)).intValue(), stackBounds.width(), stackBounds.height(), pipBounds, 0, 0, tmpBounds);
                snapTargets[i2] = new Point(tmpBounds.left, tmpBounds.top);
            }
            Point snapTarget = findClosestPoint(rect2.left, rect2.top, snapTargets);
            if (distanceToPoint(snapTarget, rect2.left, rect2.top) < ((float) Math.max(stackBounds.width(), stackBounds.height())) * CORNER_MAGNET_THRESHOLD) {
                newBounds.offsetTo(snapTarget.x, snapTarget.y);
            } else {
                snapRectToClosestEdge(rect2, rect, newBounds);
            }
        } else if (i == 2) {
            snapRectToClosestEdge(rect2, rect, newBounds);
        } else {
            Rect tmpBounds2 = new Rect();
            Point[] snapTargets2 = new Point[this.mSnapGravities.size()];
            for (int i3 = 0; i3 < this.mSnapGravities.size(); i3++) {
                Gravity.apply(((Integer) this.mSnapGravities.get(i3)).intValue(), stackBounds.width(), stackBounds.height(), pipBounds, 0, 0, tmpBounds2);
                snapTargets2[i3] = new Point(tmpBounds2.left, tmpBounds2.top);
            }
            Point snapTarget2 = findClosestPoint(rect2.left, rect2.top, snapTargets2);
            newBounds.offsetTo(snapTarget2.x, snapTarget2.y);
        }
        return newBounds;
    }

    public void applyMinimizedOffset(Rect stackBounds, Rect movementBounds, Point displaySize, Rect stableInsets) {
        if (stackBounds.left <= movementBounds.centerX()) {
            stackBounds.offsetTo((stableInsets.left + this.mMinimizedVisibleSize) - stackBounds.width(), stackBounds.top);
        } else {
            stackBounds.offsetTo((displaySize.x - stableInsets.right) - this.mMinimizedVisibleSize, stackBounds.top);
        }
    }

    public float getSnapFraction(Rect stackBounds, Rect movementBounds) {
        Rect tmpBounds = new Rect();
        snapRectToClosestEdge(stackBounds, movementBounds, tmpBounds);
        float widthFraction = ((float) (tmpBounds.left - movementBounds.left)) / ((float) movementBounds.width());
        float heightFraction = ((float) (tmpBounds.top - movementBounds.top)) / ((float) movementBounds.height());
        if (tmpBounds.top == movementBounds.top) {
            return widthFraction;
        }
        if (tmpBounds.left == movementBounds.right) {
            return 1.0f + heightFraction;
        }
        if (tmpBounds.top == movementBounds.bottom) {
            return (1.0f - widthFraction) + 2.0f;
        }
        return (1.0f - heightFraction) + 3.0f;
    }

    public void applySnapFraction(Rect stackBounds, Rect movementBounds, float snapFraction) {
        if (snapFraction < 1.0f) {
            stackBounds.offsetTo(movementBounds.left + ((int) (((float) movementBounds.width()) * snapFraction)), movementBounds.top);
        } else if (snapFraction < 2.0f) {
            stackBounds.offsetTo(movementBounds.right, movementBounds.top + ((int) (((float) movementBounds.height()) * (snapFraction - 1.0f))));
        } else if (snapFraction < 3.0f) {
            stackBounds.offsetTo(movementBounds.left + ((int) ((1.0f - (snapFraction - 2.0f)) * ((float) movementBounds.width()))), movementBounds.bottom);
        } else {
            stackBounds.offsetTo(movementBounds.left, movementBounds.top + ((int) ((1.0f - (snapFraction - 3.0f)) * ((float) movementBounds.height()))));
        }
    }

    public void getMovementBounds(Rect stackBounds, Rect insetBounds, Rect movementBoundsOut, int bottomOffset) {
        movementBoundsOut.set(insetBounds);
        movementBoundsOut.right = Math.max(insetBounds.left, insetBounds.right - stackBounds.width());
        movementBoundsOut.bottom = Math.max(insetBounds.top, insetBounds.bottom - stackBounds.height());
        movementBoundsOut.bottom -= bottomOffset;
    }

    public Size getSizeForAspectRatio(float aspectRatio, float minEdgeSize, int displayWidth, int displayHeight) {
        int height;
        int width;
        int minSize = (int) Math.max(minEdgeSize, ((float) Math.min(displayWidth, displayHeight)) * this.mDefaultSizePercent);
        if (aspectRatio > this.mMinAspectRatioForMinSize) {
            float widthAtMaxAspectRatioForMinSize = this.mMaxAspectRatioForMinSize;
            if (aspectRatio <= widthAtMaxAspectRatioForMinSize) {
                float radius = PointF.length(widthAtMaxAspectRatioForMinSize * ((float) minSize), (float) minSize);
                height = (int) Math.round(Math.sqrt((double) ((radius * radius) / ((aspectRatio * aspectRatio) + 1.0f))));
                width = Math.round(((float) height) * aspectRatio);
                return new Size(width, height);
            }
        }
        if (aspectRatio <= 1.0f) {
            width = minSize;
            height = Math.round(((float) width) / aspectRatio);
        } else {
            height = minSize;
            width = Math.round(((float) height) * aspectRatio);
        }
        return new Size(width, height);
    }

    private Point findClosestPoint(int x, int y, Point[] points) {
        Point closestPoint = null;
        float minDistance = Float.MAX_VALUE;
        for (Point p : points) {
            float distance = distanceToPoint(p, x, y);
            if (distance < minDistance) {
                closestPoint = p;
                minDistance = distance;
            }
        }
        return closestPoint;
    }

    private void snapRectToClosestEdge(Rect stackBounds, Rect movementBounds, Rect boundsOut) {
        int boundedLeft = Math.max(movementBounds.left, Math.min(movementBounds.right, stackBounds.left));
        int boundedTop = Math.max(movementBounds.top, Math.min(movementBounds.bottom, stackBounds.top));
        boundsOut.set(stackBounds);
        if (this.mIsMinimized) {
            boundsOut.offsetTo(boundedLeft, boundedTop);
            return;
        }
        int shortest;
        int fromLeft = Math.abs(stackBounds.left - movementBounds.left);
        int fromTop = Math.abs(stackBounds.top - movementBounds.top);
        int fromRight = Math.abs(movementBounds.right - stackBounds.left);
        int fromBottom = Math.abs(movementBounds.bottom - stackBounds.top);
        if (this.mSnapMode != 4) {
            shortest = Math.min(Math.min(fromLeft, fromRight), Math.min(fromTop, fromBottom));
        } else if (this.mOrientation == 2) {
            shortest = Math.min(fromTop, fromBottom);
        } else {
            shortest = Math.min(fromLeft, fromRight);
        }
        if (shortest == fromLeft) {
            boundsOut.offsetTo(movementBounds.left, boundedTop);
        } else if (shortest == fromTop) {
            boundsOut.offsetTo(boundedLeft, movementBounds.top);
        } else if (shortest == fromRight) {
            boundsOut.offsetTo(movementBounds.right, boundedTop);
        } else {
            boundsOut.offsetTo(boundedLeft, movementBounds.bottom);
        }
    }

    private float distanceToPoint(Point p, int x, int y) {
        return PointF.length((float) (p.x - x), (float) (p.y - y));
    }

    private void calculateSnapTargets() {
        this.mSnapGravities.clear();
        int i = this.mSnapMode;
        if (i != 0) {
            if (i != 1) {
                if (!(i == 3 || i == 4)) {
                    return;
                }
            } else if (this.mOrientation == 2) {
                this.mSnapGravities.add(Integer.valueOf(49));
                this.mSnapGravities.add(Integer.valueOf(81));
            } else {
                this.mSnapGravities.add(Integer.valueOf(19));
                this.mSnapGravities.add(Integer.valueOf(21));
            }
        }
        this.mSnapGravities.add(Integer.valueOf(51));
        this.mSnapGravities.add(Integer.valueOf(53));
        this.mSnapGravities.add(Integer.valueOf(83));
        this.mSnapGravities.add(Integer.valueOf(85));
    }

    public void dump(PrintWriter pw, String prefix) {
        String innerPrefix = new StringBuilder();
        innerPrefix.append(prefix);
        innerPrefix.append("  ");
        innerPrefix = innerPrefix.toString();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append(PipSnapAlgorithm.class.getSimpleName());
        pw.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(innerPrefix);
        stringBuilder.append("mSnapMode=");
        stringBuilder.append(this.mSnapMode);
        pw.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(innerPrefix);
        stringBuilder.append("mOrientation=");
        stringBuilder.append(this.mOrientation);
        pw.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(innerPrefix);
        stringBuilder.append("mMinimizedVisibleSize=");
        stringBuilder.append(this.mMinimizedVisibleSize);
        pw.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(innerPrefix);
        stringBuilder.append("mIsMinimized=");
        stringBuilder.append(this.mIsMinimized);
        pw.println(stringBuilder.toString());
    }
}
