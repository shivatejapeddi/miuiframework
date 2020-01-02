package android.inputmethodservice;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class ExtractEditText extends EditText {
    private InputMethodService mIME;
    private int mSettingExtractedText;

    public ExtractEditText(Context context) {
        super(context, null);
    }

    public ExtractEditText(Context context, AttributeSet attrs) {
        super(context, attrs, 16842862);
    }

    public ExtractEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ExtractEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /* Access modifiers changed, original: 0000 */
    public void setIME(InputMethodService ime) {
        this.mIME = ime;
    }

    public void startInternalChanges() {
        this.mSettingExtractedText++;
    }

    public void finishInternalChanges() {
        this.mSettingExtractedText--;
    }

    public void setExtractedText(ExtractedText text) {
        try {
            this.mSettingExtractedText++;
            super.setExtractedText(text);
        } finally {
            this.mSettingExtractedText--;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onSelectionChanged(int selStart, int selEnd) {
        if (this.mSettingExtractedText == 0) {
            InputMethodService inputMethodService = this.mIME;
            if (inputMethodService != null && selStart >= 0 && selEnd >= 0) {
                inputMethodService.onExtractedSelectionChanged(selStart, selEnd);
            }
        }
    }

    public boolean performClick() {
        if (!super.performClick()) {
            InputMethodService inputMethodService = this.mIME;
            if (inputMethodService != null) {
                inputMethodService.onExtractedTextClicked();
                return true;
            }
        }
        return false;
    }

    public boolean onTextContextMenuItem(int id) {
        if (id == 16908319 || id == 16908340) {
            return super.onTextContextMenuItem(id);
        }
        InputMethodService inputMethodService = this.mIME;
        if (inputMethodService == null || !inputMethodService.onExtractTextContextMenuItem(id)) {
            return super.onTextContextMenuItem(id);
        }
        if (id == 16908321 || id == 16908322) {
            stopTextActionMode();
        }
        return true;
    }

    public boolean isInputMethodTarget() {
        return true;
    }

    public boolean hasVerticalScrollBar() {
        return computeVerticalScrollRange() > computeVerticalScrollExtent();
    }

    public boolean hasWindowFocus() {
        return isEnabled();
    }

    public boolean isFocused() {
        return isEnabled();
    }

    public boolean hasFocus() {
        return isEnabled();
    }

    /* Access modifiers changed, original: protected */
    public void viewClicked(InputMethodManager imm) {
        InputMethodService inputMethodService = this.mIME;
        if (inputMethodService != null) {
            inputMethodService.onViewClicked(false);
        }
    }

    public boolean isInExtractedMode() {
        return true;
    }

    /* Access modifiers changed, original: protected */
    public void deleteText_internal(int start, int end) {
        this.mIME.onExtractedDeleteText(start, end);
    }

    /* Access modifiers changed, original: protected */
    public void replaceText_internal(int start, int end, CharSequence text) {
        this.mIME.onExtractedReplaceText(start, end, text);
    }

    /* Access modifiers changed, original: protected */
    public void setSpan_internal(Object span, int start, int end, int flags) {
        this.mIME.onExtractedSetSpan(span, start, end, flags);
    }

    /* Access modifiers changed, original: protected */
    public void setCursorPosition_internal(int start, int end) {
        this.mIME.onExtractedSelectionChanged(start, end);
    }
}
