package android.media;

import android.media.AudioManager.AudioRecordingCallback;
import java.util.concurrent.Executor;

public interface AudioRecordingMonitor {
    AudioRecordingConfiguration getActiveRecordingConfiguration();

    void registerAudioRecordingCallback(Executor executor, AudioRecordingCallback audioRecordingCallback);

    void unregisterAudioRecordingCallback(AudioRecordingCallback audioRecordingCallback);
}
