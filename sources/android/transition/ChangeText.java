package android.transition;

import android.widget.EditText;
import android.widget.TextView;

public class ChangeText extends Transition {
    public static final int CHANGE_BEHAVIOR_IN = 2;
    public static final int CHANGE_BEHAVIOR_KEEP = 0;
    public static final int CHANGE_BEHAVIOR_OUT = 1;
    public static final int CHANGE_BEHAVIOR_OUT_IN = 3;
    private static final String LOG_TAG = "TextChange";
    private static final String PROPNAME_TEXT = "android:textchange:text";
    private static final String PROPNAME_TEXT_COLOR = "android:textchange:textColor";
    private static final String PROPNAME_TEXT_SELECTION_END = "android:textchange:textSelectionEnd";
    private static final String PROPNAME_TEXT_SELECTION_START = "android:textchange:textSelectionStart";
    private static final String[] sTransitionProperties = new String[]{PROPNAME_TEXT, PROPNAME_TEXT_SELECTION_START, PROPNAME_TEXT_SELECTION_END};
    private int mChangeBehavior = 0;

    public ChangeText setChangeBehavior(int changeBehavior) {
        if (changeBehavior >= 0 && changeBehavior <= 3) {
            this.mChangeBehavior = changeBehavior;
        }
        return this;
    }

    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    public int getChangeBehavior() {
        return this.mChangeBehavior;
    }

    private void captureValues(TransitionValues transitionValues) {
        if (transitionValues.view instanceof TextView) {
            TextView textview = transitionValues.view;
            transitionValues.values.put(PROPNAME_TEXT, textview.getText());
            if (textview instanceof EditText) {
                transitionValues.values.put(PROPNAME_TEXT_SELECTION_START, Integer.valueOf(textview.getSelectionStart()));
                transitionValues.values.put(PROPNAME_TEXT_SELECTION_END, Integer.valueOf(textview.getSelectionEnd()));
            }
            if (this.mChangeBehavior > 0) {
                transitionValues.values.put(PROPNAME_TEXT_COLOR, Integer.valueOf(textview.getCurrentTextColor()));
            }
        }
    }

    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    /* JADX WARNING: Removed duplicated region for block: B:60:0x01a3  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x019e  */
    public android.animation.Animator createAnimator(android.view.ViewGroup r27, android.transition.TransitionValues r28, android.transition.TransitionValues r29) {
        /*
        r26 = this;
        r10 = r26;
        r11 = r28;
        r12 = r29;
        r0 = 0;
        if (r11 == 0) goto L_0x01c4;
    L_0x0009:
        if (r12 == 0) goto L_0x01c4;
    L_0x000b:
        r1 = r11.view;
        r1 = r1 instanceof android.widget.TextView;
        if (r1 == 0) goto L_0x01c4;
    L_0x0011:
        r1 = r12.view;
        r1 = r1 instanceof android.widget.TextView;
        if (r1 != 0) goto L_0x0019;
    L_0x0017:
        goto L_0x01c4;
    L_0x0019:
        r1 = r12.view;
        r13 = r1;
        r13 = (android.widget.TextView) r13;
        r14 = r11.values;
        r15 = r12.values;
        r1 = "android:textchange:text";
        r2 = r14.get(r1);
        r3 = "";
        if (r2 == 0) goto L_0x0033;
    L_0x002c:
        r2 = r14.get(r1);
        r2 = (java.lang.CharSequence) r2;
        goto L_0x0034;
    L_0x0033:
        r2 = r3;
    L_0x0034:
        r9 = r2;
        r2 = r15.get(r1);
        if (r2 == 0) goto L_0x0043;
    L_0x003b:
        r1 = r15.get(r1);
        r3 = r1;
        r3 = (java.lang.CharSequence) r3;
        goto L_0x0044;
    L_0x0044:
        r8 = r3;
        r1 = r13 instanceof android.widget.EditText;
        r2 = -1;
        if (r1 == 0) goto L_0x009e;
    L_0x004a:
        r1 = "android:textchange:textSelectionStart";
        r3 = r14.get(r1);
        if (r3 == 0) goto L_0x005d;
    L_0x0052:
        r3 = r14.get(r1);
        r3 = (java.lang.Integer) r3;
        r3 = r3.intValue();
        goto L_0x005e;
    L_0x005d:
        r3 = r2;
    L_0x005e:
        r4 = "android:textchange:textSelectionEnd";
        r5 = r14.get(r4);
        if (r5 == 0) goto L_0x0071;
    L_0x0066:
        r5 = r14.get(r4);
        r5 = (java.lang.Integer) r5;
        r5 = r5.intValue();
        goto L_0x0072;
    L_0x0071:
        r5 = r3;
    L_0x0072:
        r6 = r15.get(r1);
        if (r6 == 0) goto L_0x0083;
    L_0x0078:
        r1 = r15.get(r1);
        r1 = (java.lang.Integer) r1;
        r2 = r1.intValue();
        goto L_0x0084;
    L_0x0084:
        r1 = r2;
        r2 = r15.get(r4);
        if (r2 == 0) goto L_0x0096;
    L_0x008b:
        r2 = r15.get(r4);
        r2 = (java.lang.Integer) r2;
        r2 = r2.intValue();
        goto L_0x0097;
    L_0x0096:
        r2 = r1;
    L_0x0097:
        r16 = r1;
        r17 = r2;
        r7 = r3;
        r6 = r5;
        goto L_0x00a7;
    L_0x009e:
        r1 = r2;
        r3 = r2;
        r4 = r2;
        r17 = r1;
        r7 = r2;
        r16 = r3;
        r6 = r4;
    L_0x00a7:
        r1 = r9.equals(r8);
        if (r1 != 0) goto L_0x01c3;
    L_0x00ad:
        r0 = r10.mChangeBehavior;
        r5 = 2;
        if (r0 == r5) goto L_0x00bf;
    L_0x00b2:
        r13.setText(r9);
        r0 = r13 instanceof android.widget.EditText;
        if (r0 == 0) goto L_0x00bf;
    L_0x00b9:
        r0 = r13;
        r0 = (android.widget.EditText) r0;
        r10.setSelection(r0, r7, r6);
    L_0x00bf:
        r0 = r10.mChangeBehavior;
        r18 = 0;
        if (r0 != 0) goto L_0x00ee;
    L_0x00c5:
        r19 = r18;
        r0 = new float[r5];
        r0 = {0, 1065353216};
        r5 = android.animation.ValueAnimator.ofFloat(r0);
        r4 = new android.transition.ChangeText$1;
        r0 = r4;
        r1 = r26;
        r2 = r9;
        r3 = r13;
        r20 = r7;
        r7 = r4;
        r4 = r8;
        r11 = r5;
        r5 = r16;
        r21 = r6;
        r6 = r17;
        r0.<init>(r2, r3, r4, r5, r6);
        r11.addListener(r7);
        r24 = r14;
        r22 = r18;
        goto L_0x01a7;
    L_0x00ee:
        r21 = r6;
        r20 = r7;
        r0 = "android:textchange:textColor";
        r1 = r14.get(r0);
        r1 = (java.lang.Integer) r1;
        r11 = r1.intValue();
        r0 = r15.get(r0);
        r0 = (java.lang.Integer) r0;
        r7 = r0.intValue();
        r0 = 0;
        r19 = 0;
        r1 = r10.mChangeBehavior;
        r6 = 3;
        r4 = 1;
        if (r1 == r6) goto L_0x011f;
    L_0x0111:
        if (r1 != r4) goto L_0x0114;
    L_0x0113:
        goto L_0x011f;
    L_0x0114:
        r12 = r0;
        r23 = r4;
        r25 = r7;
        r22 = r11;
        r24 = r14;
        r14 = r6;
        goto L_0x0153;
    L_0x011f:
        r1 = new int[r5];
        r2 = android.graphics.Color.alpha(r11);
        r1[r18] = r2;
        r1[r4] = r18;
        r3 = android.animation.ValueAnimator.ofInt(r1);
        r0 = new android.transition.ChangeText$2;
        r0.<init>(r13, r11);
        r3.addUpdateListener(r0);
        r2 = new android.transition.ChangeText$3;
        r0 = r2;
        r1 = r26;
        r22 = r11;
        r11 = r2;
        r2 = r9;
        r12 = r3;
        r3 = r13;
        r23 = r4;
        r4 = r8;
        r24 = r14;
        r14 = r5;
        r5 = r16;
        r14 = r6;
        r6 = r17;
        r25 = r7;
        r0.<init>(r2, r3, r4, r5, r6, r7);
        r12.addListener(r11);
    L_0x0153:
        r0 = r10.mChangeBehavior;
        if (r0 == r14) goto L_0x0160;
    L_0x0157:
        r1 = 2;
        if (r0 != r1) goto L_0x015b;
    L_0x015a:
        goto L_0x0161;
    L_0x015b:
        r0 = r19;
        r2 = r25;
        goto L_0x0181;
    L_0x0160:
        r1 = 2;
    L_0x0161:
        r0 = new int[r1];
        r0[r18] = r18;
        r1 = android.graphics.Color.alpha(r25);
        r0[r23] = r1;
        r0 = android.animation.ValueAnimator.ofInt(r0);
        r1 = new android.transition.ChangeText$4;
        r2 = r25;
        r1.<init>(r13, r2);
        r0.addUpdateListener(r1);
        r1 = new android.transition.ChangeText$5;
        r1.<init>(r13, r2);
        r0.addListener(r1);
    L_0x0181:
        if (r12 == 0) goto L_0x019c;
    L_0x0183:
        if (r0 == 0) goto L_0x019c;
    L_0x0185:
        r1 = new android.animation.AnimatorSet;
        r1.<init>();
        r5 = r1;
        r1 = r5;
        r1 = (android.animation.AnimatorSet) r1;
        r3 = 2;
        r3 = new android.animation.Animator[r3];
        r3[r18] = r12;
        r3[r23] = r0;
        r1.playSequentially(r3);
        r19 = r2;
        r11 = r5;
        goto L_0x01a7;
    L_0x019c:
        if (r12 == 0) goto L_0x01a3;
    L_0x019e:
        r5 = r12;
        r19 = r2;
        r11 = r5;
        goto L_0x01a7;
    L_0x01a3:
        r5 = r0;
        r19 = r2;
        r11 = r5;
    L_0x01a7:
        r12 = new android.transition.ChangeText$6;
        r0 = r12;
        r1 = r26;
        r2 = r13;
        r3 = r8;
        r4 = r16;
        r5 = r17;
        r6 = r19;
        r7 = r9;
        r14 = r8;
        r8 = r20;
        r18 = r9;
        r9 = r21;
        r0.<init>(r2, r3, r4, r5, r6, r7, r8, r9);
        r10.addListener(r0);
        return r11;
    L_0x01c3:
        return r0;
    L_0x01c4:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.transition.ChangeText.createAnimator(android.view.ViewGroup, android.transition.TransitionValues, android.transition.TransitionValues):android.animation.Animator");
    }

    private void setSelection(EditText editText, int start, int end) {
        if (start >= 0 && end >= 0) {
            editText.setSelection(start, end);
        }
    }
}
