package android.graphics.drawable.shapes;

import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.RectF;
import java.util.Objects;

public class RectShape extends Shape {
    private RectF mRect = new RectF();

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(this.mRect, paint);
    }

    public void getOutline(Outline outline) {
        RectF rect = rect();
        outline.setRect((int) Math.ceil((double) rect.left), (int) Math.ceil((double) rect.top), (int) Math.floor((double) rect.right), (int) Math.floor((double) rect.bottom));
    }

    /* Access modifiers changed, original: protected */
    public void onResize(float width, float height) {
        this.mRect.set(0.0f, 0.0f, width, height);
    }

    /* Access modifiers changed, original: protected|final */
    public final RectF rect() {
        return this.mRect;
    }

    public RectShape clone() throws CloneNotSupportedException {
        RectShape shape = (RectShape) super.clone();
        shape.mRect = new RectF(this.mRect);
        return shape;
    }

    /* JADX WARNING: Missing block: B:12:0x0025, code skipped:
            return false;
     */
    public boolean equals(java.lang.Object r4) {
        /*
        r3 = this;
        if (r3 != r4) goto L_0x0004;
    L_0x0002:
        r0 = 1;
        return r0;
    L_0x0004:
        r0 = 0;
        if (r4 == 0) goto L_0x0025;
    L_0x0007:
        r1 = r3.getClass();
        r2 = r4.getClass();
        if (r1 == r2) goto L_0x0012;
    L_0x0011:
        goto L_0x0025;
    L_0x0012:
        r1 = super.equals(r4);
        if (r1 != 0) goto L_0x0019;
    L_0x0018:
        return r0;
    L_0x0019:
        r0 = r4;
        r0 = (android.graphics.drawable.shapes.RectShape) r0;
        r1 = r3.mRect;
        r2 = r0.mRect;
        r1 = java.util.Objects.equals(r1, r2);
        return r1;
    L_0x0025:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.drawable.shapes.RectShape.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(super.hashCode()), this.mRect});
    }
}
