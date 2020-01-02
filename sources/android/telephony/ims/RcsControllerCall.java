package android.telephony.ims;

import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.ims.aidl.IRcs;
import android.telephony.ims.aidl.IRcs.Stub;

class RcsControllerCall {
    private final Context mContext;

    interface RcsServiceCall<R> {
        R methodOnIRcs(IRcs iRcs, String str) throws RemoteException;
    }

    interface RcsServiceCallWithNoReturn {
        void methodOnIRcs(IRcs iRcs, String str) throws RemoteException;
    }

    RcsControllerCall(Context context) {
        this.mContext = context;
    }

    /* Access modifiers changed, original: 0000 */
    public <R> R call(RcsServiceCall<R> serviceCall) throws RcsMessageStoreException {
        IRcs iRcs = Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_RCS_SERVICE));
        if (iRcs != null) {
            try {
                return serviceCall.methodOnIRcs(iRcs, this.mContext.getOpPackageName());
            } catch (RemoteException exception) {
                throw new RcsMessageStoreException(exception.getMessage());
            }
        }
        throw new RcsMessageStoreException("Could not connect to RCS storage service");
    }

    /* Access modifiers changed, original: 0000 */
    public void callWithNoReturn(RcsServiceCallWithNoReturn serviceCall) throws RcsMessageStoreException {
        call(new -$$Lambda$RcsControllerCall$lqKvRobLziMoZre7XkbJkfc5LEM(serviceCall));
    }
}
