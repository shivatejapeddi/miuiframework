package org.ifaa.android.manager.face;

import android.content.Context;
import org.ifaa.android.manager.face.IFAAFaceManager.AuthenticatorCallback;

public abstract class IFAAFaceManagerV2 extends IFAAFaceManager {
    public abstract void authenticate(String str, int i, AuthenticatorCallback authenticatorCallback);

    public abstract int cancel(String str);

    public abstract void enroll(String str, int i, AuthenticatorCallback authenticatorCallback);

    public abstract byte[] invokeCommand(Context context, byte[] bArr);

    public abstract void upgrade(String str);

    public int getVersion() {
        return 2;
    }
}
