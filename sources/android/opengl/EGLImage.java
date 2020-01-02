package android.opengl;

public class EGLImage extends EGLObjectHandle {
    private EGLImage(long handle) {
        super(handle);
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof EGLImage)) {
            return false;
        }
        if (getNativeHandle() != ((EGLImage) o).getNativeHandle()) {
            z = false;
        }
        return z;
    }
}
