package org.mipay.android.manager;

import android.content.Context;

public interface IMipayManager {
    boolean contains(String str);

    int generateKeyPair(String str, String str2);

    String getFpIds();

    int getSupportBIOTypes(Context context);

    int getVersion();

    int removeAllKey();

    byte[] sign();

    int signInit(String str, String str2);

    int signUpdate(byte[] bArr, int i, int i2);
}
