package android.speech.tts;

import android.inputmethodservice.VoiceInputHelper;
import android.speech.tts.TextToSpeech.EngineInfo;
import java.util.Locale;

class TtsEnginesInjector {
    TtsEnginesInjector() {
    }

    static String getRecommendEngineForLocale(TtsEngines ttsEngines, Locale locale) {
        String openSdkSpeechEngine = "com.baidu.duersdk.opensdk";
        String mibrainEngine = VoiceInputHelper.MI_BRAIN_PKG;
        if (locale.getLanguage().equals(Locale.CHINESE.getLanguage())) {
            String str = VoiceInputHelper.MI_BRAIN_PKG;
            if (ttsEngines.isEngineInstalled(str)) {
                return str;
            }
            str = "com.baidu.duersdk.opensdk";
            if (ttsEngines.isEngineInstalled(str)) {
                return str;
            }
        }
        return null;
    }

    @Deprecated
    static String findFirstEngineSupportLocale(TtsEngines ttsEngines, Locale locale) {
        for (EngineInfo engine : ttsEngines.getEngines()) {
            if (ttsEngines.getLocalePrefForEngine(engine.name).equals(locale)) {
                return engine.name;
            }
        }
        return null;
    }
}
