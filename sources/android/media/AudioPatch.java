package android.media;

import android.annotation.UnsupportedAppUsage;

public class AudioPatch {
    @UnsupportedAppUsage
    private final AudioHandle mHandle;
    private final AudioPortConfig[] mSinks;
    private final AudioPortConfig[] mSources;

    @UnsupportedAppUsage
    AudioPatch(AudioHandle patchHandle, AudioPortConfig[] sources, AudioPortConfig[] sinks) {
        this.mHandle = patchHandle;
        this.mSources = sources;
        this.mSinks = sinks;
    }

    @UnsupportedAppUsage
    public AudioPortConfig[] sources() {
        return this.mSources;
    }

    @UnsupportedAppUsage
    public AudioPortConfig[] sinks() {
        return this.mSinks;
    }

    public int id() {
        return this.mHandle.id();
    }

    public String toString() {
        String str;
        StringBuilder s = new StringBuilder();
        s.append("mHandle: ");
        s.append(this.mHandle.toString());
        s.append(" mSources: {");
        AudioPortConfig[] audioPortConfigArr = this.mSources;
        int length = audioPortConfigArr.length;
        int i = 0;
        int i2 = 0;
        while (true) {
            str = ", ";
            if (i2 >= length) {
                break;
            }
            s.append(audioPortConfigArr[i2].toString());
            s.append(str);
            i2++;
        }
        s.append("} mSinks: {");
        audioPortConfigArr = this.mSinks;
        length = audioPortConfigArr.length;
        while (i < length) {
            s.append(audioPortConfigArr[i].toString());
            s.append(str);
            i++;
        }
        s.append("}");
        return s.toString();
    }
}
