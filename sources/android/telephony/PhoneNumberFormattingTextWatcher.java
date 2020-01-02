package android.telephony;

import android.annotation.UnsupportedAppUsage;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import com.android.i18n.phonenumbers.AsYouTypeFormatter;
import com.android.i18n.phonenumbers.PhoneNumberUtil;
import java.util.Locale;

public class PhoneNumberFormattingTextWatcher implements TextWatcher {
    @UnsupportedAppUsage
    private AsYouTypeFormatter mFormatter;
    private boolean mSelfChange;
    private boolean mStopFormatting;

    public PhoneNumberFormattingTextWatcher() {
        this(Locale.getDefault().getCountry());
    }

    public PhoneNumberFormattingTextWatcher(String countryCode) {
        this.mSelfChange = false;
        if (countryCode != null) {
            this.mFormatter = PhoneNumberUtil.getInstance().getAsYouTypeFormatter(countryCode);
            return;
        }
        throw new IllegalArgumentException();
    }

    /* JADX WARNING: Missing block: B:9:0x0015, code skipped:
            return;
     */
    public void beforeTextChanged(java.lang.CharSequence r2, int r3, int r4, int r5) {
        /*
        r1 = this;
        r0 = r1.mSelfChange;
        if (r0 != 0) goto L_0x0015;
    L_0x0004:
        r0 = r1.mStopFormatting;
        if (r0 == 0) goto L_0x0009;
    L_0x0008:
        goto L_0x0015;
    L_0x0009:
        if (r4 <= 0) goto L_0x0014;
    L_0x000b:
        r0 = r1.hasSeparator(r2, r3, r4);
        if (r0 == 0) goto L_0x0014;
    L_0x0011:
        r1.stopFormatting();
    L_0x0014:
        return;
    L_0x0015:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.telephony.PhoneNumberFormattingTextWatcher.beforeTextChanged(java.lang.CharSequence, int, int, int):void");
    }

    /* JADX WARNING: Missing block: B:9:0x0015, code skipped:
            return;
     */
    public void onTextChanged(java.lang.CharSequence r2, int r3, int r4, int r5) {
        /*
        r1 = this;
        r0 = r1.mSelfChange;
        if (r0 != 0) goto L_0x0015;
    L_0x0004:
        r0 = r1.mStopFormatting;
        if (r0 == 0) goto L_0x0009;
    L_0x0008:
        goto L_0x0015;
    L_0x0009:
        if (r5 <= 0) goto L_0x0014;
    L_0x000b:
        r0 = r1.hasSeparator(r2, r3, r5);
        if (r0 == 0) goto L_0x0014;
    L_0x0011:
        r1.stopFormatting();
    L_0x0014:
        return;
    L_0x0015:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.telephony.PhoneNumberFormattingTextWatcher.onTextChanged(java.lang.CharSequence, int, int, int):void");
    }

    public synchronized void afterTextChanged(Editable s) {
        boolean z = true;
        if (this.mStopFormatting) {
            if (s.length() == 0) {
                z = false;
            }
            this.mStopFormatting = z;
        } else if (!this.mSelfChange) {
            String formatted = reformat(s, Selection.getSelectionEnd(s));
            if (formatted != null) {
                int rememberedPos = this.mFormatter.getRememberedPosition();
                this.mSelfChange = true;
                s.replace(0, s.length(), formatted, 0, formatted.length());
                if (formatted.equals(s.toString())) {
                    Selection.setSelection(s, rememberedPos);
                }
                this.mSelfChange = false;
            }
            PhoneNumberUtils.ttsSpanAsPhoneNumber(s, 0, s.length());
        }
    }

    private String reformat(CharSequence s, int cursor) {
        int curIndex = cursor - 1;
        String formatted = null;
        this.mFormatter.clear();
        char lastNonSeparator = 0;
        boolean hasCursor = false;
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (PhoneNumberUtils.isNonSeparator(c)) {
                if (lastNonSeparator != 0) {
                    formatted = getFormattedNumber(lastNonSeparator, hasCursor);
                    hasCursor = false;
                }
                lastNonSeparator = c;
            }
            if (i == curIndex) {
                hasCursor = true;
            }
        }
        if (lastNonSeparator != 0) {
            return getFormattedNumber(lastNonSeparator, hasCursor);
        }
        return formatted;
    }

    private String getFormattedNumber(char lastNonSeparator, boolean hasCursor) {
        if (hasCursor) {
            return this.mFormatter.inputDigitAndRememberPosition(lastNonSeparator);
        }
        return this.mFormatter.inputDigit(lastNonSeparator);
    }

    private void stopFormatting() {
        this.mStopFormatting = true;
        this.mFormatter.clear();
    }

    private boolean hasSeparator(CharSequence s, int start, int count) {
        for (int i = start; i < start + count; i++) {
            if (!PhoneNumberUtils.isNonSeparator(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
