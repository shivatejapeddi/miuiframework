package android.text.method;

import org.apache.miui.commons.lang3.CharUtils;

public class SingleLineTransformationMethod extends ReplacementTransformationMethod {
    private static char[] ORIGINAL = new char[]{10, CharUtils.CR};
    private static char[] REPLACEMENT = new char[]{' ', 65279};
    private static SingleLineTransformationMethod sInstance;

    /* Access modifiers changed, original: protected */
    public char[] getOriginal() {
        return ORIGINAL;
    }

    /* Access modifiers changed, original: protected */
    public char[] getReplacement() {
        return REPLACEMENT;
    }

    public static SingleLineTransformationMethod getInstance() {
        SingleLineTransformationMethod singleLineTransformationMethod = sInstance;
        if (singleLineTransformationMethod != null) {
            return singleLineTransformationMethod;
        }
        sInstance = new SingleLineTransformationMethod();
        return sInstance;
    }
}
