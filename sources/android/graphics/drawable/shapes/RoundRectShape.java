package android.graphics.drawable.shapes;

import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import java.util.Arrays;
import java.util.Objects;

public class RoundRectShape extends RectShape {
    private float[] mInnerRadii;
    private RectF mInnerRect;
    private RectF mInset;
    private float[] mOuterRadii;
    private Path mPath;

    public RoundRectShape(float[] outerRadii, RectF inset, float[] innerRadii) {
        if (outerRadii != null && outerRadii.length < 8) {
            throw new ArrayIndexOutOfBoundsException("outer radii must have >= 8 values");
        } else if (innerRadii == null || innerRadii.length >= 8) {
            this.mOuterRadii = outerRadii;
            this.mInset = inset;
            this.mInnerRadii = innerRadii;
            if (inset != null) {
                this.mInnerRect = new RectF();
            }
            this.mPath = new Path();
        } else {
            throw new ArrayIndexOutOfBoundsException("inner radii must have >= 8 values");
        }
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawPath(this.mPath, paint);
    }

    public void getOutline(Outline outline) {
        if (this.mInnerRect == null) {
            float radius = 0.0f;
            float[] fArr = this.mOuterRadii;
            if (fArr != null) {
                radius = fArr[0];
                for (int i = 1; i < 8; i++) {
                    if (this.mOuterRadii[i] != radius) {
                        outline.setConvexPath(this.mPath);
                        return;
                    }
                }
            }
            RectF rect = rect();
            outline.setRoundRect((int) Math.ceil((double) rect.left), (int) Math.ceil((double) rect.top), (int) Math.floor((double) rect.right), (int) Math.floor((double) rect.bottom), radius);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onResize(float w, float h) {
        super.onResize(w, h);
        RectF r = rect();
        this.mPath.reset();
        float[] fArr = this.mOuterRadii;
        if (fArr != null) {
            this.mPath.addRoundRect(r, fArr, Direction.CW);
        } else {
            this.mPath.addRect(r, Direction.CW);
        }
        RectF rectF = this.mInnerRect;
        if (rectF != null) {
            rectF.set(r.left + this.mInset.left, r.top + this.mInset.top, r.right - this.mInset.right, r.bottom - this.mInset.bottom);
            if (this.mInnerRect.width() < w && this.mInnerRect.height() < h) {
                fArr = this.mInnerRadii;
                if (fArr != null) {
                    this.mPath.addRoundRect(this.mInnerRect, fArr, Direction.CCW);
                } else {
                    this.mPath.addRect(this.mInnerRect, Direction.CCW);
                }
            }
        }
    }

    public RoundRectShape clone() throws CloneNotSupportedException {
        RoundRectShape shape = (RoundRectShape) super.clone();
        float[] fArr = this.mOuterRadii;
        float[] fArr2 = null;
        shape.mOuterRadii = fArr != null ? (float[]) fArr.clone() : null;
        fArr = this.mInnerRadii;
        if (fArr != null) {
            fArr2 = (float[]) fArr.clone();
        }
        shape.mInnerRadii = fArr2;
        shape.mInset = new RectF(this.mInset);
        shape.mInnerRect = new RectF(this.mInnerRect);
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
        r2 = (android.graphics.drawable.shapes.RoundRectShape) r2;
        r3 = r5.mOuterRadii;
        r4 = r2.mOuterRadii;
        r3 = java.util.Arrays.equals(r3, r4);
        if (r3 == 0) goto L_0x004f;
    L_0x0026:
        r3 = r5.mInset;
        r4 = r2.mInset;
        r3 = java.util.Objects.equals(r3, r4);
        if (r3 == 0) goto L_0x004f;
    L_0x0030:
        r3 = r5.mInnerRadii;
        r4 = r2.mInnerRadii;
        r3 = java.util.Arrays.equals(r3, r4);
        if (r3 == 0) goto L_0x004f;
    L_0x003a:
        r3 = r5.mInnerRect;
        r4 = r2.mInnerRect;
        r3 = java.util.Objects.equals(r3, r4);
        if (r3 == 0) goto L_0x004f;
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
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.drawable.shapes.RoundRectShape.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        return (((Objects.hash(new Object[]{Integer.valueOf(super.hashCode()), this.mInset, this.mInnerRect, this.mPath}) * 31) + Arrays.hashCode(this.mOuterRadii)) * 31) + Arrays.hashCode(this.mInnerRadii);
    }
}
