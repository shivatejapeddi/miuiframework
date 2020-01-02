package com.android.id;

import android.content.Context;

public interface IdProvider {
    String getAAID(Context context);

    String getOAID(Context context);

    String getUDID(Context context);

    String getVAID(Context context);
}
