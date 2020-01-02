package com.android.internal.accessibility;

import android.speech.tts.TextToSpeech;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$qdzoyIBhDB17ZFWPp1Rf8ICv-R8 implements Consumer {
    public static final /* synthetic */ -$$Lambda$qdzoyIBhDB17ZFWPp1Rf8ICv-R8 INSTANCE = new -$$Lambda$qdzoyIBhDB17ZFWPp1Rf8ICv-R8();

    private /* synthetic */ -$$Lambda$qdzoyIBhDB17ZFWPp1Rf8ICv-R8() {
    }

    public final void accept(Object obj) {
        ((TextToSpeech) obj).shutdown();
    }
}
