package android.telephony.ims;

import android.os.RemoteException;
import android.telephony.ims.aidl.IRcs;

public final class RcsGroupThreadParticipantLeftEvent extends RcsGroupThreadEvent {
    private RcsParticipant mLeavingParticipant;

    public RcsGroupThreadParticipantLeftEvent(long timestamp, RcsGroupThread rcsGroupThread, RcsParticipant originatingParticipant, RcsParticipant leavingParticipant) {
        super(timestamp, rcsGroupThread, originatingParticipant);
        this.mLeavingParticipant = leavingParticipant;
    }

    public RcsParticipant getLeavingParticipant() {
        return this.mLeavingParticipant;
    }

    /* Access modifiers changed, original: 0000 */
    public void persist(RcsControllerCall rcsControllerCall) throws RcsMessageStoreException {
        rcsControllerCall.call(new -$$Lambda$RcsGroupThreadParticipantLeftEvent$vX6x1bZueUi684uTuoFiWxhgs80(this));
    }

    public /* synthetic */ Integer lambda$persist$0$RcsGroupThreadParticipantLeftEvent(IRcs iRcs, String callingPackage) throws RemoteException {
        return Integer.valueOf(iRcs.createGroupThreadParticipantLeftEvent(getTimestamp(), getRcsGroupThread().getThreadId(), getOriginatingParticipant().getId(), getLeavingParticipant().getId(), callingPackage));
    }
}
