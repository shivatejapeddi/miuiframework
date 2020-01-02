package android.media;

import android.annotation.UnsupportedAppUsage;

class AudioHandle {
    @UnsupportedAppUsage
    private final int mId;

    @UnsupportedAppUsage
    AudioHandle(int id) {
        this.mId = id;
    }

    /* Access modifiers changed, original: 0000 */
    public int id() {
        return this.mId;
    }

    public boolean equals(Object o) {
        boolean z = false;
        if (o == null || !(o instanceof AudioHandle)) {
            return false;
        }
        if (this.mId == ((AudioHandle) o).id()) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return this.mId;
    }

    public String toString() {
        return Integer.toString(this.mId);
    }
}
