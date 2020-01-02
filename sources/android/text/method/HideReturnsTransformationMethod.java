package android.text.method;

import android.annotation.UnsupportedAppUsage;
import org.apache.miui.commons.lang3.CharUtils;

public class HideReturnsTransformationMethod extends ReplacementTransformationMethod {
    private static char[] ORIGINAL = new char[]{CharUtils.CR};
    private static char[] REPLACEMENT = new char[]{65279};
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private static HideReturnsTransformationMethod sInstance;

    /* Access modifiers changed, original: protected */
    public char[] getOriginal() {
        return ORIGINAL;
    }

    /* Access modifiers changed, original: protected */
    public char[] getReplacement() {
        return REPLACEMENT;
    }

    public static HideReturnsTransformationMethod getInstance() {
        HideReturnsTransformationMethod hideReturnsTransformationMethod = sInstance;
        if (hideReturnsTransformationMethod != null) {
            return hideReturnsTransformationMethod;
        }
        sInstance = new HideReturnsTransformationMethod();
        return sInstance;
    }
}
