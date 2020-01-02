package android.text.method;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.AnrMonitor;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.Selection;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.method.TextKeyListener.Capitalize;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;

public class MultiTapKeyListener extends BaseKeyListener implements SpanWatcher {
    private static MultiTapKeyListener[] sInstance = new MultiTapKeyListener[(Capitalize.values().length * 2)];
    private static final SparseArray<String> sRecs = new SparseArray();
    private boolean mAutoText;
    private Capitalize mCapitalize;

    private class Timeout extends Handler implements Runnable {
        private Editable mBuffer;

        public Timeout(Editable buffer) {
            this.mBuffer = buffer;
            Editable editable = this.mBuffer;
            editable.setSpan(this, 0, editable.length(), 18);
            postAtTime(this, SystemClock.uptimeMillis() + AnrMonitor.MESSAGE_EXECUTION_TIMEOUT);
        }

        public void run() {
            Spannable buf = this.mBuffer;
            if (buf != null) {
                int st = Selection.getSelectionStart(buf);
                int en = Selection.getSelectionEnd(buf);
                int start = buf.getSpanStart(TextKeyListener.ACTIVE);
                int end = buf.getSpanEnd(TextKeyListener.ACTIVE);
                if (st == start && en == end) {
                    Selection.setSelection(buf, Selection.getSelectionEnd(buf));
                }
                buf.removeSpan(this);
            }
        }
    }

    static {
        sRecs.put(8, ".,1!@#$%^&*:/?'=()");
        sRecs.put(9, "abc2ABC");
        sRecs.put(10, "def3DEF");
        sRecs.put(11, "ghi4GHI");
        sRecs.put(12, "jkl5JKL");
        sRecs.put(13, "mno6MNO");
        sRecs.put(14, "pqrs7PQRS");
        sRecs.put(15, "tuv8TUV");
        sRecs.put(16, "wxyz9WXYZ");
        sRecs.put(7, "0+");
        sRecs.put(18, WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
    }

    public MultiTapKeyListener(Capitalize cap, boolean autotext) {
        this.mCapitalize = cap;
        this.mAutoText = autotext;
    }

    public static MultiTapKeyListener getInstance(boolean autotext, Capitalize cap) {
        int off = (cap.ordinal() * 2) + autotext;
        MultiTapKeyListener[] multiTapKeyListenerArr = sInstance;
        if (multiTapKeyListenerArr[off] == null) {
            multiTapKeyListenerArr[off] = new MultiTapKeyListener(cap, autotext);
        }
        return sInstance[off];
    }

    public int getInputType() {
        return BaseKeyListener.makeTextContentType(this.mCapitalize, this.mAutoText);
    }

    public boolean onKeyDown(View view, Editable content, int keyCode, KeyEvent event) {
        int pref;
        String val;
        int ix;
        Editable editable = content;
        int i = keyCode;
        if (view != null) {
            pref = TextKeyListener.getInstance().getPrefs(view.getContext());
        } else {
            pref = 0;
        }
        int a = Selection.getSelectionStart(content);
        int b = Selection.getSelectionEnd(content);
        int selStart = Math.min(a, b);
        int selEnd = Math.max(a, b);
        int activeStart = editable.getSpanStart(TextKeyListener.ACTIVE);
        int activeEnd = editable.getSpanEnd(TextKeyListener.ACTIVE);
        int rec = (editable.getSpanFlags(TextKeyListener.ACTIVE) & -16777216) >>> 24;
        if (activeStart == selStart && activeEnd == selEnd && selEnd - selStart == 1 && rec >= 0 && rec < sRecs.size()) {
            Timeout timeout;
            if (i == 17) {
                char current = editable.charAt(selStart);
                if (Character.isLowerCase(current)) {
                    editable.replace(selStart, selEnd, String.valueOf(current).toUpperCase());
                    removeTimeouts(content);
                    timeout = new Timeout(editable);
                    return true;
                } else if (Character.isUpperCase(current)) {
                    editable.replace(selStart, selEnd, String.valueOf(current).toLowerCase());
                    removeTimeouts(content);
                    timeout = new Timeout(editable);
                    return true;
                }
            }
            if (sRecs.indexOfKey(i) == rec) {
                val = (String) sRecs.valueAt(rec);
                char ch = editable.charAt(selStart);
                b = val.indexOf(ch);
                if (b >= 0) {
                    ix = (b + 1) % val.length();
                    content.replace(selStart, selEnd, val, ix, ix + 1);
                    removeTimeouts(content);
                    timeout = new Timeout(editable);
                    return true;
                }
                String str = val;
            }
            b = sRecs.indexOfKey(i);
            if (b >= 0) {
                Selection.setSelection(editable, selEnd, selEnd);
                selStart = b;
                rec = selEnd;
            } else {
                rec = selStart;
                selStart = b;
            }
        } else {
            rec = selStart;
            selStart = sRecs.indexOfKey(i);
        }
        if (selStart < 0) {
            return super.onKeyDown(view, content, keyCode, event);
        }
        val = (String) sRecs.valueAt(selStart);
        if ((pref & 1) != 0 && TextKeyListener.shouldCap(this.mCapitalize, editable, rec)) {
            for (int i2 = 0; i2 < val.length(); i2++) {
                if (Character.isUpperCase(val.charAt(i2))) {
                    ix = i2;
                    break;
                }
            }
        }
        ix = 0;
        if (rec != selEnd) {
            Selection.setSelection(editable, selEnd);
        }
        editable.setSpan(OLD_SEL_START, rec, rec, 17);
        content.replace(rec, selEnd, val, ix, ix + 1);
        a = editable.getSpanStart(OLD_SEL_START);
        b = Selection.getSelectionEnd(content);
        if (b != a) {
            Selection.setSelection(editable, a, b);
            editable.setSpan(TextKeyListener.LAST_TYPED, a, b, 33);
            editable.setSpan(TextKeyListener.ACTIVE, a, b, 33 | (selStart << 24));
        }
        removeTimeouts(content);
        Timeout timeout2 = new Timeout(editable);
        if (editable.getSpanStart(this) < 0) {
            for (int selEnd2 : (KeyListener[]) editable.getSpans(0, content.length(), KeyListener.class)) {
                editable.removeSpan(selEnd2);
            }
            editable.setSpan(this, 0, content.length(), 18);
        }
        return true;
    }

    public void onSpanChanged(Spannable buf, Object what, int s, int e, int start, int stop) {
        if (what == Selection.SELECTION_END) {
            buf.removeSpan(TextKeyListener.ACTIVE);
            removeTimeouts(buf);
        }
    }

    private static void removeTimeouts(Spannable buf) {
        Timeout[] timeout = (Timeout[]) buf.getSpans(0, buf.length(), Timeout.class);
        for (Timeout t : timeout) {
            t.removeCallbacks(t);
            t.mBuffer = null;
            buf.removeSpan(t);
        }
    }

    public void onSpanAdded(Spannable s, Object what, int start, int end) {
    }

    public void onSpanRemoved(Spannable s, Object what, int start, int end) {
    }
}
