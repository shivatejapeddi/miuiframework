package android.telephony.ims;

import android.os.RemoteException;
import android.telephony.ims.aidl.IRcs;

public class RcsParticipant {
    private final int mId;
    private final RcsControllerCall mRcsControllerCall;

    public RcsParticipant(RcsControllerCall rcsControllerCall, int id) {
        this.mRcsControllerCall = rcsControllerCall;
        this.mId = id;
    }

    public String getCanonicalAddress() throws RcsMessageStoreException {
        return (String) this.mRcsControllerCall.call(new -$$Lambda$RcsParticipant$T35onLZnU-uRTl7zQ7ZWRFtFvx4(this));
    }

    public /* synthetic */ String lambda$getCanonicalAddress$0$RcsParticipant(IRcs iRcs, String callingPackage) throws RemoteException {
        return iRcs.getRcsParticipantCanonicalAddress(this.mId, callingPackage);
    }

    public String getAlias() throws RcsMessageStoreException {
        return (String) this.mRcsControllerCall.call(new -$$Lambda$RcsParticipant$MNtRFbM6h-ycH3bPEUZgB5f56zs(this));
    }

    public /* synthetic */ String lambda$getAlias$1$RcsParticipant(IRcs iRcs, String callingPackage) throws RemoteException {
        return iRcs.getRcsParticipantAlias(this.mId, callingPackage);
    }

    public void setAlias(String alias) throws RcsMessageStoreException {
        this.mRcsControllerCall.callWithNoReturn(new -$$Lambda$RcsParticipant$xir-e-NE3auWDac4dOx89mKtRKU(this, alias));
    }

    public /* synthetic */ void lambda$setAlias$2$RcsParticipant(String alias, IRcs iRcs, String callingPackage) throws RemoteException {
        iRcs.setRcsParticipantAlias(this.mId, alias, callingPackage);
    }

    public String getContactId() throws RcsMessageStoreException {
        return (String) this.mRcsControllerCall.call(new -$$Lambda$RcsParticipant$up5zUlvCkFUru1_1NfgXrzNmBic(this));
    }

    public /* synthetic */ String lambda$getContactId$3$RcsParticipant(IRcs iRcs, String callingPackage) throws RemoteException {
        return iRcs.getRcsParticipantContactId(this.mId, callingPackage);
    }

    public void setContactId(String contactId) throws RcsMessageStoreException {
        this.mRcsControllerCall.callWithNoReturn(new -$$Lambda$RcsParticipant$HgHlMU15W2RReyvhk-UQ-432pfA(this, contactId));
    }

    public /* synthetic */ void lambda$setContactId$4$RcsParticipant(String contactId, IRcs iRcs, String callingPackage) throws RemoteException {
        iRcs.setRcsParticipantContactId(this.mId, contactId, callingPackage);
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RcsParticipant)) {
            return false;
        }
        if (this.mId != ((RcsParticipant) obj).mId) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return this.mId;
    }

    public int getId() {
        return this.mId;
    }
}
