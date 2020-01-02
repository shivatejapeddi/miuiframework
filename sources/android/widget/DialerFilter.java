package android.widget;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.AllCaps;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.DialerKeyListener;
import android.text.method.KeyListener;
import android.text.method.TextKeyListener;
import android.util.AttributeSet;
import android.view.KeyEvent;

@Deprecated
public class DialerFilter extends RelativeLayout {
    public static final int DIGITS_AND_LETTERS = 1;
    public static final int DIGITS_AND_LETTERS_NO_DIGITS = 2;
    public static final int DIGITS_AND_LETTERS_NO_LETTERS = 3;
    public static final int DIGITS_ONLY = 4;
    public static final int LETTERS_ONLY = 5;
    EditText mDigits;
    EditText mHint;
    ImageView mIcon;
    InputFilter[] mInputFilters;
    private boolean mIsQwerty;
    EditText mLetters;
    int mMode;
    EditText mPrimary;

    public DialerFilter(Context context) {
        super(context);
    }

    public DialerFilter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /* Access modifiers changed, original: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mInputFilters = new InputFilter[]{new AllCaps()};
        this.mHint = (EditText) findViewById(16908293);
        EditText editText = this.mHint;
        if (editText != null) {
            editText.setFilters(this.mInputFilters);
            this.mLetters = this.mHint;
            this.mLetters.setKeyListener(TextKeyListener.getInstance());
            this.mLetters.setMovementMethod(null);
            this.mLetters.setFocusable(false);
            this.mPrimary = (EditText) findViewById(16908300);
            editText = this.mPrimary;
            if (editText != null) {
                editText.setFilters(this.mInputFilters);
                this.mDigits = this.mPrimary;
                this.mDigits.setKeyListener(DialerKeyListener.getInstance());
                this.mDigits.setMovementMethod(null);
                this.mDigits.setFocusable(false);
                this.mIcon = (ImageView) findViewById(16908294);
                setFocusable(true);
                this.mIsQwerty = true;
                setMode(1);
                return;
            }
            throw new IllegalStateException("DialerFilter must have a child EditText named primary");
        }
        throw new IllegalStateException("DialerFilter must have a child EditText named hint");
    }

    /* Access modifiers changed, original: protected */
    public void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        ImageView imageView = this.mIcon;
        if (imageView != null) {
            imageView.setVisibility(focused ? 0 : 8);
        }
    }

    public boolean isQwertyKeyboard() {
        return this.mIsQwerty;
    }

    /* JADX WARNING: Missing block: B:10:0x001b, code skipped:
            if (r1 != 5) goto L_0x00d6;
     */
    public boolean onKeyDown(int r8, android.view.KeyEvent r9) {
        /*
        r7 = this;
        r0 = 0;
        r1 = 66;
        r2 = 1;
        if (r8 == r1) goto L_0x00d5;
    L_0x0006:
        r1 = 67;
        r3 = 5;
        r4 = 4;
        r5 = 3;
        r6 = 2;
        if (r8 == r1) goto L_0x0066;
    L_0x000e:
        switch(r8) {
            case 19: goto L_0x00d5;
            case 20: goto L_0x00d5;
            case 21: goto L_0x00d5;
            case 22: goto L_0x00d5;
            case 23: goto L_0x00d5;
            default: goto L_0x0011;
        };
    L_0x0011:
        r1 = r7.mMode;
        if (r1 == r2) goto L_0x002f;
    L_0x0015:
        if (r1 == r6) goto L_0x0027;
    L_0x0017:
        if (r1 == r5) goto L_0x001f;
    L_0x0019:
        if (r1 == r4) goto L_0x001f;
    L_0x001b:
        if (r1 == r3) goto L_0x0027;
    L_0x001d:
        goto L_0x00d6;
    L_0x001f:
        r1 = r7.mDigits;
        r0 = r1.onKeyDown(r8, r9);
        goto L_0x00d6;
    L_0x0027:
        r1 = r7.mLetters;
        r0 = r1.onKeyDown(r8, r9);
        goto L_0x00d6;
    L_0x002f:
        r1 = r7.mLetters;
        r0 = r1.onKeyDown(r8, r9);
        r1 = android.view.KeyEvent.isModifierKey(r8);
        if (r1 == 0) goto L_0x0043;
    L_0x003b:
        r1 = r7.mDigits;
        r1.onKeyDown(r8, r9);
        r0 = 1;
        goto L_0x00d6;
    L_0x0043:
        r1 = r9.isPrintingKey();
        if (r1 != 0) goto L_0x0051;
    L_0x0049:
        r3 = 62;
        if (r8 == r3) goto L_0x0051;
    L_0x004d:
        r3 = 61;
        if (r8 != r3) goto L_0x00d6;
    L_0x0051:
        r3 = android.text.method.DialerKeyListener.CHARACTERS;
        r3 = r9.getMatch(r3);
        if (r3 == 0) goto L_0x0061;
    L_0x0059:
        r4 = r7.mDigits;
        r4 = r4.onKeyDown(r8, r9);
        r0 = r0 & r4;
        goto L_0x0064;
    L_0x0061:
        r7.setMode(r6);
    L_0x0064:
        goto L_0x00d6;
    L_0x0066:
        r1 = r7.mMode;
        if (r1 == r2) goto L_0x00c6;
    L_0x006a:
        if (r1 == r6) goto L_0x00a6;
    L_0x006c:
        if (r1 == r5) goto L_0x0081;
    L_0x006e:
        if (r1 == r4) goto L_0x007a;
    L_0x0070:
        if (r1 == r3) goto L_0x0073;
    L_0x0072:
        goto L_0x00d4;
    L_0x0073:
        r1 = r7.mLetters;
        r0 = r1.onKeyDown(r8, r9);
        goto L_0x00d4;
    L_0x007a:
        r1 = r7.mDigits;
        r0 = r1.onKeyDown(r8, r9);
        goto L_0x00d4;
    L_0x0081:
        r1 = r7.mDigits;
        r1 = r1.getText();
        r1 = r1.length();
        r3 = r7.mLetters;
        r3 = r3.getText();
        r3 = r3.length();
        if (r1 != r3) goto L_0x009f;
    L_0x0097:
        r1 = r7.mLetters;
        r1.onKeyDown(r8, r9);
        r7.setMode(r2);
    L_0x009f:
        r1 = r7.mDigits;
        r0 = r1.onKeyDown(r8, r9);
        goto L_0x00d4;
    L_0x00a6:
        r1 = r7.mLetters;
        r0 = r1.onKeyDown(r8, r9);
        r1 = r7.mLetters;
        r1 = r1.getText();
        r1 = r1.length();
        r3 = r7.mDigits;
        r3 = r3.getText();
        r3 = r3.length();
        if (r1 != r3) goto L_0x00d4;
    L_0x00c2:
        r7.setMode(r2);
        goto L_0x00d4;
    L_0x00c6:
        r1 = r7.mDigits;
        r0 = r1.onKeyDown(r8, r9);
        r1 = r7.mLetters;
        r1 = r1.onKeyDown(r8, r9);
        r0 = r0 & r1;
    L_0x00d4:
        goto L_0x00d6;
    L_0x00d6:
        if (r0 != 0) goto L_0x00dd;
    L_0x00d8:
        r1 = super.onKeyDown(r8, r9);
        return r1;
    L_0x00dd:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.DialerFilter.onKeyDown(int, android.view.KeyEvent):boolean");
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return this.mLetters.onKeyUp(keyCode, event) || this.mDigits.onKeyUp(keyCode, event);
    }

    public int getMode() {
        return this.mMode;
    }

    public void setMode(int newMode) {
        if (newMode == 1) {
            makeDigitsPrimary();
            this.mLetters.setVisibility(0);
            this.mDigits.setVisibility(0);
        } else if (newMode == 2) {
            makeLettersPrimary();
            this.mLetters.setVisibility(0);
            this.mDigits.setVisibility(4);
        } else if (newMode == 3) {
            makeDigitsPrimary();
            this.mLetters.setVisibility(4);
            this.mDigits.setVisibility(0);
        } else if (newMode == 4) {
            makeDigitsPrimary();
            this.mLetters.setVisibility(8);
            this.mDigits.setVisibility(0);
        } else if (newMode == 5) {
            makeLettersPrimary();
            this.mLetters.setVisibility(0);
            this.mDigits.setVisibility(8);
        }
        int oldMode = this.mMode;
        this.mMode = newMode;
        onModeChange(oldMode, newMode);
    }

    private void makeLettersPrimary() {
        if (this.mPrimary == this.mDigits) {
            swapPrimaryAndHint(true);
        }
    }

    private void makeDigitsPrimary() {
        if (this.mPrimary == this.mLetters) {
            swapPrimaryAndHint(false);
        }
    }

    private void swapPrimaryAndHint(boolean makeLettersPrimary) {
        Editable lettersText = this.mLetters.getText();
        Editable digitsText = this.mDigits.getText();
        KeyListener lettersInput = this.mLetters.getKeyListener();
        KeyListener digitsInput = this.mDigits.getKeyListener();
        if (makeLettersPrimary) {
            this.mLetters = this.mPrimary;
            this.mDigits = this.mHint;
        } else {
            this.mLetters = this.mHint;
            this.mDigits = this.mPrimary;
        }
        this.mLetters.setKeyListener(lettersInput);
        this.mLetters.setText((CharSequence) lettersText);
        lettersText = this.mLetters.getText();
        Selection.setSelection(lettersText, lettersText.length());
        this.mDigits.setKeyListener(digitsInput);
        this.mDigits.setText((CharSequence) digitsText);
        digitsText = this.mDigits.getText();
        Selection.setSelection(digitsText, digitsText.length());
        this.mPrimary.setFilters(this.mInputFilters);
        this.mHint.setFilters(this.mInputFilters);
    }

    public CharSequence getLetters() {
        if (this.mLetters.getVisibility() == 0) {
            return this.mLetters.getText();
        }
        return "";
    }

    public CharSequence getDigits() {
        if (this.mDigits.getVisibility() == 0) {
            return this.mDigits.getText();
        }
        return "";
    }

    public CharSequence getFilterText() {
        if (this.mMode != 4) {
            return getLetters();
        }
        return getDigits();
    }

    public void append(String text) {
        int i = this.mMode;
        if (i != 1) {
            if (i != 2) {
                if (i == 3 || i == 4) {
                    this.mDigits.getText().append((CharSequence) text);
                    return;
                } else if (i != 5) {
                    return;
                }
            }
            this.mLetters.getText().append((CharSequence) text);
            return;
        }
        this.mDigits.getText().append((CharSequence) text);
        this.mLetters.getText().append((CharSequence) text);
    }

    public void clearText() {
        this.mLetters.getText().clear();
        this.mDigits.getText().clear();
        if (this.mIsQwerty) {
            setMode(1);
        } else {
            setMode(4);
        }
    }

    public void setLettersWatcher(TextWatcher watcher) {
        CharSequence text = this.mLetters.getText();
        ((Spannable) text).setSpan(watcher, 0, text.length(), 18);
    }

    public void setDigitsWatcher(TextWatcher watcher) {
        CharSequence text = this.mDigits.getText();
        ((Spannable) text).setSpan(watcher, 0, text.length(), 18);
    }

    public void setFilterWatcher(TextWatcher watcher) {
        if (this.mMode != 4) {
            setLettersWatcher(watcher);
        } else {
            setDigitsWatcher(watcher);
        }
    }

    public void removeFilterWatcher(TextWatcher watcher) {
        Spannable text;
        if (this.mMode != 4) {
            text = this.mLetters.getText();
        } else {
            text = this.mDigits.getText();
        }
        text.removeSpan(watcher);
    }

    /* Access modifiers changed, original: protected */
    public void onModeChange(int oldMode, int newMode) {
    }
}
