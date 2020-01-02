package android.telephony.ims;

import android.os.RemoteException;
import android.telephony.ims.aidl.IRcs;

public final class RcsParticipantAliasChangedEvent extends RcsEvent {
    private final String mNewAlias;
    private final RcsParticipant mParticipant;

    public RcsParticipantAliasChangedEvent(long timestamp, RcsParticipant participant, String newAlias) {
        super(timestamp);
        this.mParticipant = participant;
        this.mNewAlias = newAlias;
    }

    public RcsParticipant getParticipant() {
        return this.mParticipant;
    }

    public String getNewAlias() {
        return this.mNewAlias;
    }

    public /* synthetic */ Integer lambda$persist$0$RcsParticipantAliasChangedEvent(IRcs iRcs, String callingPackage) throws RemoteException {
        return Integer.valueOf(iRcs.createParticipantAliasChangedEvent(getTimestamp(), getParticipant().getId(), getNewAlias(), callingPackage));
    }

    /* Access modifiers changed, original: 0000 */
    public void persist(RcsControllerCall rcsControllerCall) throws RcsMessageStoreException {
        rcsControllerCall.call(new -$$Lambda$RcsParticipantAliasChangedEvent$iaidodGQwVEX4DZ8FekRuR-x3gQ(this));
    }
}
