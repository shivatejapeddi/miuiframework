package miui.hareware.display;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.TextUtils;

class DisplayFeatureServiceProxy {
    private static final String INTERFACE_DESCRIPTOR = "miui.hardware.display.IDisplayFeatureService";
    private static final int TRANSACTION_setAd = 3;
    private static final int TRANSACTION_setCABC = 5;
    private static final int TRANSACTION_setCE = 4;
    private static final int TRANSACTION_setColorPrefer = 1;
    private static final int TRANSACTION_setEyeCare = 2;
    private static final int TRANSACTION_setFeature = 100;
    private static final int TRANSACTION_setGamutMode = 6;
    private String mDescriptor;
    private IBinder mService;

    DisplayFeatureServiceProxy(IBinder service) {
        this.mService = service;
        try {
            this.mDescriptor = this.mService.getInterfaceDescriptor();
            if (TextUtils.isEmpty(this.mDescriptor)) {
                this.mDescriptor = INTERFACE_DESCRIPTOR;
            }
        } catch (RemoteException e) {
        }
    }

    /* Access modifiers changed, original: 0000 */
    public int setColorPrefer(int displayId, int mode) throws RemoteException {
        return callTransact(1, displayId, mode);
    }

    /* Access modifiers changed, original: 0000 */
    public int setEyeCare(int displayId, int mode) throws RemoteException {
        return callTransact(2, displayId, mode);
    }

    /* Access modifiers changed, original: 0000 */
    public int setAd(int displayId, int enable, int value) throws RemoteException {
        return callTransact(3, displayId, enable, value);
    }

    /* Access modifiers changed, original: 0000 */
    public int setCE(int displayId, int mode) throws RemoteException {
        return callTransact(4, displayId, mode);
    }

    /* Access modifiers changed, original: 0000 */
    public int setCABC(int displayId, int mode) throws RemoteException {
        return callTransact(5, displayId, mode);
    }

    /* Access modifiers changed, original: 0000 */
    public int setGamutMode(int displayId, int mode) throws RemoteException {
        return callTransact(6, displayId, mode);
    }

    /* Access modifiers changed, original: 0000 */
    public int setFeature(int displayId, int mode, int value, int cookie) throws RemoteException {
        return callTransact(100, displayId, mode, value, cookie);
    }

    private int callTransact(int transactId, int... params) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        int result = -1;
        try {
            data.writeInterfaceToken(this.mDescriptor);
            for (int param : params) {
                data.writeInt(param);
            }
            if (this.mService.transact(transactId, data, reply, 0)) {
                reply.readException();
                result = reply.readInt();
            }
            reply.recycle();
            data.recycle();
            return result;
        } catch (Throwable th) {
            reply.recycle();
            data.recycle();
        }
    }
}
