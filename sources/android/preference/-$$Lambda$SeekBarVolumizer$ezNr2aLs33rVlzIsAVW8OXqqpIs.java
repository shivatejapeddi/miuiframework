package android.preference;

import android.media.audiopolicy.AudioProductStrategy;
import java.util.function.Function;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$SeekBarVolumizer$ezNr2aLs33rVlzIsAVW8OXqqpIs implements Function {
    public static final /* synthetic */ -$$Lambda$SeekBarVolumizer$ezNr2aLs33rVlzIsAVW8OXqqpIs INSTANCE = new -$$Lambda$SeekBarVolumizer$ezNr2aLs33rVlzIsAVW8OXqqpIs();

    private /* synthetic */ -$$Lambda$SeekBarVolumizer$ezNr2aLs33rVlzIsAVW8OXqqpIs() {
    }

    public final Object apply(Object obj) {
        return Integer.valueOf(((AudioProductStrategy) obj).getVolumeGroupIdForAudioAttributes(AudioProductStrategy.sDefaultAttributes));
    }
}
