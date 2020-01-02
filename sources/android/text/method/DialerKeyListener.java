package android.text.method;

import android.telephony.PhoneNumberUtils;
import android.text.Spannable;
import android.view.KeyCharacterMap.KeyData;
import android.view.KeyEvent;
import org.apache.miui.commons.lang3.ClassUtils;

public class DialerKeyListener extends NumberKeyListener {
    public static final char[] CHARACTERS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '#', '*', '+', '-', '(', ')', ',', '/', PhoneNumberUtils.WILD, ClassUtils.PACKAGE_SEPARATOR_CHAR, ' ', ';'};
    private static DialerKeyListener sInstance;

    /* Access modifiers changed, original: protected */
    public char[] getAcceptedChars() {
        return CHARACTERS;
    }

    public static DialerKeyListener getInstance() {
        DialerKeyListener dialerKeyListener = sInstance;
        if (dialerKeyListener != null) {
            return dialerKeyListener;
        }
        sInstance = new DialerKeyListener();
        return sInstance;
    }

    public int getInputType() {
        return 3;
    }

    /* Access modifiers changed, original: protected */
    public int lookup(KeyEvent event, Spannable content) {
        int meta = MetaKeyKeyListener.getMetaState((CharSequence) content, event);
        int number = event.getNumber();
        if ((meta & 3) == 0 && number != 0) {
            return number;
        }
        int match = super.lookup(event, content);
        if (match != 0) {
            return match;
        }
        if (meta != 0) {
            KeyData kd = new KeyData();
            char[] accepted = getAcceptedChars();
            if (event.getKeyData(kd)) {
                for (int i = 1; i < kd.meta.length; i++) {
                    if (NumberKeyListener.ok(accepted, kd.meta[i])) {
                        return kd.meta[i];
                    }
                }
            }
        }
        return number;
    }
}
