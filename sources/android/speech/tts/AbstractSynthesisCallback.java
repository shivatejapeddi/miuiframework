package android.speech.tts;

abstract class AbstractSynthesisCallback implements SynthesisCallback {
    protected final boolean mClientIsUsingV2;

    public abstract void stop();

    AbstractSynthesisCallback(boolean clientIsUsingV2) {
        this.mClientIsUsingV2 = clientIsUsingV2;
    }

    /* Access modifiers changed, original: 0000 */
    public int errorCodeOnStop() {
        return this.mClientIsUsingV2 ? -2 : -1;
    }
}
