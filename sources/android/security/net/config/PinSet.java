package android.security.net.config;

import android.util.ArraySet;
import java.util.Collections;
import java.util.Set;

public final class PinSet {
    public static final PinSet EMPTY_PINSET = new PinSet(Collections.emptySet(), Long.MAX_VALUE);
    public final long expirationTime;
    public final Set<Pin> pins;

    public PinSet(Set<Pin> pins, long expirationTime) {
        if (pins != null) {
            this.pins = pins;
            this.expirationTime = expirationTime;
            return;
        }
        throw new NullPointerException("pins must not be null");
    }

    /* Access modifiers changed, original: 0000 */
    public Set<String> getPinAlgorithms() {
        Set<String> algorithms = new ArraySet();
        for (Pin pin : this.pins) {
            algorithms.add(pin.digestAlgorithm);
        }
        return algorithms;
    }
}
