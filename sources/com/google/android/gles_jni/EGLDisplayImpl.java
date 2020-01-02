package com.google.android.gles_jni;

import javax.microedition.khronos.egl.EGLDisplay;

public class EGLDisplayImpl extends EGLDisplay {
    long mEGLDisplay;

    public EGLDisplayImpl(long dpy) {
        this.mEGLDisplay = dpy;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (this.mEGLDisplay != ((EGLDisplayImpl) o).mEGLDisplay) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result = 17 * 31;
        long j = this.mEGLDisplay;
        return result + ((int) (j ^ (j >>> 32)));
    }
}
