package android.graphics.drawable.shapes;

import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import java.util.Objects;

public class ArcShape extends RectShape {
    private final float mStartAngle;
    private final float mSweepAngle;

    public ArcShape(float startAngle, float sweepAngle) {
        this.mStartAngle = startAngle;
        this.mSweepAngle = sweepAngle;
    }

    public final float getStartAngle() {
        return this.mStartAngle;
    }

    public final float getSweepAngle() {
        return this.mSweepAngle;
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawArc(rect(), this.mStartAngle, this.mSweepAngle, true, paint);
    }

    public void getOutline(Outline outline) {
    }

    public ArcShape clone() throws CloneNotSupportedException {
        return (ArcShape) super.clone();
    }

    /* JADX WARNING: Missing block: B:16:0x0033, code skipped:
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
        if (r6 == 0) goto L_0x0033;
    L_0x0007:
        r2 = r5.getClass();
        r3 = r6.getClass();
        if (r2 == r3) goto L_0x0012;
    L_0x0011:
        goto L_0x0033;
    L_0x0012:
        r2 = super.equals(r6);
        if (r2 != 0) goto L_0x0019;
    L_0x0018:
        return r1;
    L_0x0019:
        r2 = r6;
        r2 = (android.graphics.drawable.shapes.ArcShape) r2;
        r3 = r2.mStartAngle;
        r4 = r5.mStartAngle;
        r3 = java.lang.Float.compare(r3, r4);
        if (r3 != 0) goto L_0x0031;
    L_0x0026:
        r3 = r2.mSweepAngle;
        r4 = r5.mSweepAngle;
        r3 = java.lang.Float.compare(r3, r4);
        if (r3 != 0) goto L_0x0031;
    L_0x0030:
        goto L_0x0032;
    L_0x0031:
        r0 = r1;
    L_0x0032:
        return r0;
    L_0x0033:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.drawable.shapes.ArcShape.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(super.hashCode()), Float.valueOf(this.mStartAngle), Float.valueOf(this.mSweepAngle)});
    }
}
