package com.android.internal.telephony;

import android.annotation.UnsupportedAppUsage;

public class EncodeException extends Exception {
    public static final int ERROR_EXCEED_SIZE = 1;
    public static final int ERROR_UNENCODABLE = 0;
    private int mError = 0;

    @UnsupportedAppUsage
    public EncodeException(String s) {
        super(s);
    }

    public EncodeException(String s, int error) {
        super(s);
        this.mError = error;
    }

    @UnsupportedAppUsage
    public EncodeException(char c) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unencodable char: '");
        stringBuilder.append(c);
        stringBuilder.append("'");
        super(stringBuilder.toString());
    }

    public int getError() {
        return this.mError;
    }
}
