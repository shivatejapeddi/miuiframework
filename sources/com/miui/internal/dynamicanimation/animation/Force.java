package com.miui.internal.dynamicanimation.animation;

interface Force {
    float getAcceleration(float f, float f2);

    boolean isAtEquilibrium(float f, float f2);
}
