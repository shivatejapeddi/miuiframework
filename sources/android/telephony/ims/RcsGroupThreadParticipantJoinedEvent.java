package android.telephony.ims;

import android.os.RemoteException;
import android.telephony.ims.aidl.IRcs;

public final class RcsGroupThreadParticipantJoinedEvent extends RcsGroupThreadEvent {
    private final RcsParticipant mJoinedParticipantId;

    public RcsGroupThreadParticipantJoinedEvent(long timestamp, RcsGroupThread rcsGroupThread, RcsParticipant originatingParticipant, RcsParticipant joinedParticipant) {
        super(timestamp, rcsGroupThread, originatingParticipant);
        this.mJoinedParticipantId = joinedParticipant;
    }

    public RcsParticipant getJoinedParticipant() {
        return this.mJoinedParticipantId;
    }

    /* Access modifiers changed, original: 0000 */
    public void persist(RcsControllerCall rcsControllerCall) throws RcsMessageStoreException {
        rcsControllerCall.call(new -$$Lambda$RcsGroupThreadParticipantJoinedEvent$KF8KQ4WJfLnGm4G9rOgwA9MjEj8(this));
    }

    public /* synthetic */ Integer lambda$persist$0$RcsGroupThreadParticipantJoinedEvent(IRcs iRcs, String callingPackage) throws RemoteException {
        return Integer.valueOf(iRcs.createGroupThreadParticipantJoinedEvent(getTimestamp(), getRcsGroupThread().getThreadId(), getOriginatingParticipant().getId(), getJoinedParticipant().getId(), callingPackage));
    }
}
