package android.telephony.ims;

import android.os.RemoteException;
import android.telephony.ims.aidl.IRcs;

public class RcsIncomingMessage extends RcsMessage {
    RcsIncomingMessage(RcsControllerCall rcsControllerCall, int id) {
        super(rcsControllerCall, id);
    }

    public void setArrivalTimestamp(long arrivalTimestamp) throws RcsMessageStoreException {
        this.mRcsControllerCall.callWithNoReturn(new -$$Lambda$RcsIncomingMessage$OdAmvZkbLfGMknLzGuOOXKVYczw(this, arrivalTimestamp));
    }

    public /* synthetic */ void lambda$setArrivalTimestamp$0$RcsIncomingMessage(long arrivalTimestamp, IRcs iRcs, String callingPackage) throws RemoteException {
        iRcs.setMessageArrivalTimestamp(this.mId, true, arrivalTimestamp, callingPackage);
    }

    public long getArrivalTimestamp() throws RcsMessageStoreException {
        return ((Long) this.mRcsControllerCall.call(new -$$Lambda$RcsIncomingMessage$FSzDY0-cZbSPckAubiU3QaXu_Yg(this))).longValue();
    }

    public /* synthetic */ Long lambda$getArrivalTimestamp$1$RcsIncomingMessage(IRcs iRcs, String callingPackage) throws RemoteException {
        return Long.valueOf(iRcs.getMessageArrivalTimestamp(this.mId, true, callingPackage));
    }

    public void setSeenTimestamp(long notifiedTimestamp) throws RcsMessageStoreException {
        this.mRcsControllerCall.callWithNoReturn(new -$$Lambda$RcsIncomingMessage$OvvfqgFG2FNYN7ohCBbWdETfeuQ(this, notifiedTimestamp));
    }

    public /* synthetic */ void lambda$setSeenTimestamp$2$RcsIncomingMessage(long notifiedTimestamp, IRcs iRcs, String callingPackage) throws RemoteException {
        iRcs.setMessageSeenTimestamp(this.mId, true, notifiedTimestamp, callingPackage);
    }

    public long getSeenTimestamp() throws RcsMessageStoreException {
        return ((Long) this.mRcsControllerCall.call(new -$$Lambda$RcsIncomingMessage$21fHX_-vVRTL95x404C5b4eGWok(this))).longValue();
    }

    public /* synthetic */ Long lambda$getSeenTimestamp$3$RcsIncomingMessage(IRcs iRcs, String callingPackage) throws RemoteException {
        return Long.valueOf(iRcs.getMessageSeenTimestamp(this.mId, true, callingPackage));
    }

    public RcsParticipant getSenderParticipant() throws RcsMessageStoreException {
        return new RcsParticipant(this.mRcsControllerCall, ((Integer) this.mRcsControllerCall.call(new -$$Lambda$RcsIncomingMessage$ye8KwJqH7fqnRAZlQY1PRVyh2b0(this))).intValue());
    }

    public /* synthetic */ Integer lambda$getSenderParticipant$4$RcsIncomingMessage(IRcs iRcs, String callingPackage) throws RemoteException {
        return Integer.valueOf(iRcs.getSenderParticipant(this.mId, callingPackage));
    }

    public boolean isIncoming() {
        return true;
    }
}
