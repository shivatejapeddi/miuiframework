package com.google.android.gles_jni;

import javax.microedition.khronos.egl.EGLSurface;

public class EGLSurfaceImpl extends EGLSurface {
    long mEGLSurface;

    public EGLSurfaceImpl() {
        this.mEGLSurface = 0;
    }

    public EGLSurfaceImpl(long surface) {
        this.mEGLSurface = surface;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (this.mEGLSurface != ((EGLSurfaceImpl) o).mEGLSurface) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result = 17 * 31;
        long j = this.mEGLSurface;
        return result + ((int) (j ^ (j >>> 32)));
    }
}
