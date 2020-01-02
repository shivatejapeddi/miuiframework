package android.filterfw.geometry;

import android.annotation.UnsupportedAppUsage;

public class Point {
    @UnsupportedAppUsage
    public float x;
    @UnsupportedAppUsage
    public float y;

    @UnsupportedAppUsage
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean IsInUnitRange() {
        float f = this.x;
        if (f >= 0.0f && f <= 1.0f) {
            f = this.y;
            if (f >= 0.0f && f <= 1.0f) {
                return true;
            }
        }
        return false;
    }

    public Point plus(float x, float y) {
        return new Point(this.x + x, this.y + y);
    }

    public Point plus(Point point) {
        return plus(point.x, point.y);
    }

    public Point minus(float x, float y) {
        return new Point(this.x - x, this.y - y);
    }

    public Point minus(Point point) {
        return minus(point.x, point.y);
    }

    public Point times(float s) {
        return new Point(this.x * s, this.y * s);
    }

    public Point mult(float x, float y) {
        return new Point(this.x * x, this.y * y);
    }

    public float length() {
        return (float) Math.hypot((double) this.x, (double) this.y);
    }

    public float distanceTo(Point p) {
        return p.minus(this).length();
    }

    public Point scaledTo(float length) {
        return times(length / length());
    }

    public Point normalize() {
        return scaledTo(1.0f);
    }

    public Point rotated90(int count) {
        float nx = this.x;
        float ny = this.y;
        for (int i = 0; i < count; i++) {
            float ox = nx;
            nx = ny;
            ny = -ox;
        }
        return new Point(nx, ny);
    }

    public Point rotated(float radians) {
        return new Point((float) ((Math.cos((double) radians) * ((double) this.x)) - (Math.sin((double) radians) * ((double) this.y))), (float) ((Math.sin((double) radians) * ((double) this.x)) + (Math.cos((double) radians) * ((double) this.y))));
    }

    public Point rotatedAround(Point center, float radians) {
        return minus(center).rotated(radians).plus(center);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        stringBuilder.append(this.x);
        stringBuilder.append(", ");
        stringBuilder.append(this.y);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
