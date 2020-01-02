package org.mipay.android.manager;

import android.content.Context;

public class MipayManagerFactory {
    public static IMipayManager getMipayManager(Context context, int authType) {
        return MipayManagerImpl.getInstance(context);
    }
}
