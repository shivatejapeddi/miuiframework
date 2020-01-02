package android.graphics.drawable.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import java.util.Objects;

public class PathShape extends Shape {
    private Path mPath;
    private float mScaleX;
    private float mScaleY;
    private final float mStdHeight;
    private final float mStdWidth;

    public PathShape(Path path, float stdWidth, float stdHeight) {
        this.mPath = path;
        this.mStdWidth = stdWidth;
        this.mStdHeight = stdHeight;
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.save();
        canvas.scale(this.mScaleX, this.mScaleY);
        canvas.drawPath(this.mPath, paint);
        canvas.restore();
    }

    /* Access modifiers changed, original: protected */
    public void onResize(float width, float height) {
        this.mScaleX = width / this.mStdWidth;
        this.mScaleY = height / this.mStdHeight;
    }

    public PathShape clone() throws CloneNotSupportedException {
        PathShape shape = (PathShape) super.clone();
        shape.mPath = new Path(this.mPath);
        return shape;
    }

    /* JADX WARNING: Missing block: B:22:0x0051, code skipped:
            return false;
     */
    public boolean equals(java.lang.Object r6) {
        /*
        r5 = this;
        r0 = 1;
        if (r5 != r6) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = 0;
        if (r6 == 0) goto L_0x0051;
    L_0x0007:
        r2 = r5.getClass();
        r3 = r6.getClass();
        if (r2 == r3) goto L_0x0012;
    L_0x0011:
        goto L_0x0051;
    L_0x0012:
        r2 = super.equals(r6);
        if (r2 != 0) goto L_0x0019;
    L_0x0018:
        return r1;
    L_0x0019:
        r2 = r6;
        r2 = (android.graphics.drawable.shapes.PathShape) r2;
        r3 = r2.mStdWidth;
        r4 = r5.mStdWidth;
        r3 = java.lang.Float.compare(r3, r4);
        if (r3 != 0) goto L_0x004f;
    L_0x0026:
        r3 = r2.mStdHeight;
        r4 = r5.mStdHeight;
        r3 = java.lang.Float.compare(r3, r4);
        if (r3 != 0) goto L_0x004f;
    L_0x0030:
        r3 = r2.mScaleX;
        r4 = r5.mScaleX;
        r3 = java.lang.Float.compare(r3, r4);
        if (r3 != 0) goto L_0x004f;
    L_0x003a:
        r3 = r2.mScaleY;
        r4 = r5.mScaleY;
        r3 = java.lang.Float.compare(r3, r4);
        if (r3 != 0) goto L_0x004f;
    L_0x0044:
        r3 = r5.mPath;
        r4 = r2.mPath;
        r3 = java.util.Objects.equals(r3, r4);
        if (r3 == 0) goto L_0x004f;
    L_0x004e:
        goto L_0x0050;
    L_0x004f:
        r0 = r1;
    L_0x0050:
        return r0;
    L_0x0051:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.drawable.shapes.PathShape.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(super.hashCode()), Float.valueOf(this.mStdWidth), Float.valueOf(this.mStdHeight), this.mPath, Float.valueOf(this.mScaleX), Float.valueOf(this.mScaleY)});
    }
}
