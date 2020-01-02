package com.android.internal.widget;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

public class BackgroundFallback {
    private Drawable mBackgroundFallback;

    public void setDrawable(Drawable d) {
        this.mBackgroundFallback = d;
    }

    public Drawable getDrawable() {
        return this.mBackgroundFallback;
    }

    public boolean hasFallback() {
        return this.mBackgroundFallback != null;
    }

    public void draw(ViewGroup boundsView, ViewGroup root, Canvas c, View content, View coveringView1, View coveringView2) {
        Canvas canvas = c;
        View view = coveringView1;
        View view2 = coveringView2;
        if (hasFallback()) {
            int width = boundsView.getWidth();
            int height = boundsView.getHeight();
            int rootOffsetX = root.getLeft();
            int rootOffsetY = root.getTop();
            int left = width;
            int top = height;
            int right = 0;
            int bottom = 0;
            int childCount = root.getChildCount();
            int i = 0;
            while (i < childCount) {
                View child = root.getChildAt(i);
                int childCount2 = childCount;
                childCount = child.getBackground();
                if (child != content) {
                    if (child.getVisibility() == 0) {
                        if (!isOpaque(childCount)) {
                        }
                    }
                    i++;
                    childCount = childCount2;
                } else if (childCount == 0 && (child instanceof ViewGroup) && ((ViewGroup) child).getChildCount() == 0) {
                    i++;
                    childCount = childCount2;
                }
                left = Math.min(left, child.getLeft() + rootOffsetX);
                top = Math.min(top, child.getTop() + rootOffsetY);
                right = Math.max(right, child.getRight() + rootOffsetX);
                bottom = Math.max(bottom, child.getBottom() + rootOffsetY);
                i++;
                childCount = childCount2;
            }
            boolean eachBarCoversTopInY = true;
            i = 0;
            while (i < 2) {
                View v = i == 0 ? view : view2;
                if (v != null && v.getVisibility() == 0 && v.getAlpha() == 1.0f && isOpaque(v.getBackground())) {
                    if (v.getTop() <= 0 && v.getBottom() >= height && v.getLeft() <= 0 && v.getRight() >= left) {
                        left = 0;
                    }
                    if (v.getTop() <= 0 && v.getBottom() >= height && v.getLeft() <= right && v.getRight() >= width) {
                        right = width;
                    }
                    if (v.getTop() <= 0 && v.getBottom() >= top && v.getLeft() <= 0 && v.getRight() >= width) {
                        top = 0;
                    }
                    if (v.getTop() <= bottom && v.getBottom() >= height && v.getLeft() <= 0 && v.getRight() >= width) {
                        bottom = height;
                    }
                    int i2 = (v.getTop() > 0 || v.getBottom() < top) ? 0 : 1;
                    eachBarCoversTopInY &= i2;
                } else {
                    eachBarCoversTopInY = false;
                }
                i++;
            }
            if (eachBarCoversTopInY && (viewsCoverEntireWidth(view, view2, width) || viewsCoverEntireWidth(view2, view, width))) {
                top = 0;
            }
            if (left < right && top < bottom) {
                int i3;
                if (top > 0) {
                    i3 = 0;
                    this.mBackgroundFallback.setBounds(0, 0, width, top);
                    this.mBackgroundFallback.draw(canvas);
                } else {
                    i3 = 0;
                }
                if (left > 0) {
                    this.mBackgroundFallback.setBounds(i3, top, left, height);
                    this.mBackgroundFallback.draw(canvas);
                }
                if (right < width) {
                    this.mBackgroundFallback.setBounds(right, top, width, height);
                    this.mBackgroundFallback.draw(canvas);
                }
                if (bottom < height) {
                    this.mBackgroundFallback.setBounds(left, bottom, right, height);
                    this.mBackgroundFallback.draw(canvas);
                }
            }
        }
    }

    private boolean isOpaque(Drawable childBg) {
        return childBg != null && childBg.getOpacity() == -1;
    }

    private boolean viewsCoverEntireWidth(View view1, View view2, int width) {
        return view1.getLeft() <= 0 && view1.getRight() >= view2.getLeft() && view2.getRight() >= width;
    }
}
