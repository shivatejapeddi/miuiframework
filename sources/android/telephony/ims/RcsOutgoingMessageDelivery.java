package android.telephony.ims;

import android.os.RemoteException;
import android.telephony.ims.aidl.IRcs;

public class RcsOutgoingMessageDelivery {
    private final RcsControllerCall mRcsControllerCall;
    private final int mRcsOutgoingMessageId;
    private final int mRecipientId;

    RcsOutgoingMessageDelivery(RcsControllerCall rcsControllerCall, int recipientId, int messageId) {
        this.mRcsControllerCall = rcsControllerCall;
        this.mRecipientId = recipientId;
        this.mRcsOutgoingMessageId = messageId;
    }

    public void setDeliveredTimestamp(long deliveredTimestamp) throws RcsMessageStoreException {
        this.mRcsControllerCall.callWithNoReturn(new -$$Lambda$RcsOutgoingMessageDelivery$RRb0ymf6fqzeTy7WOV3ylkaBJDA(this, deliveredTimestamp));
    }

    public /* synthetic */ void lambda$setDeliveredTimestamp$0$RcsOutgoingMessageDelivery(long deliveredTimestamp, IRcs iRcs, String callingPackage) throws RemoteException {
        iRcs.setOutgoingDeliveryDeliveredTimestamp(this.mRcsOutgoingMessageId, this.mRecipientId, deliveredTimestamp, callingPackage);
    }

    public long getDeliveredTimestamp() throws RcsMessageStoreException {
        return ((Long) this.mRcsControllerCall.call(new -$$Lambda$RcsOutgoingMessageDelivery$XobnngqskscGHACfd0qrHXy-W6A(this))).longValue();
    }

    public /* synthetic */ Long lambda$getDeliveredTimestamp$1$RcsOutgoingMessageDelivery(IRcs iRcs, String callingPackage) throws RemoteException {
        return Long.valueOf(iRcs.getOutgoingDeliveryDeliveredTimestamp(this.mRcsOutgoingMessageId, this.mRecipientId, callingPackage));
    }

    public void setSeenTimestamp(long seenTimestamp) throws RcsMessageStoreException {
        this.mRcsControllerCall.callWithNoReturn(new -$$Lambda$RcsOutgoingMessageDelivery$P2OcWKWejNP6qsda0ef9G0jKYKs(this, seenTimestamp));
    }

    public /* synthetic */ void lambda$setSeenTimestamp$2$RcsOutgoingMessageDelivery(long seenTimestamp, IRcs iRcs, String callingPackage) throws RemoteException {
        iRcs.setOutgoingDeliverySeenTimestamp(this.mRcsOutgoingMessageId, this.mRecipientId, seenTimestamp, callingPackage);
    }

    public long getSeenTimestamp() throws RcsMessageStoreException {
        return ((Long) this.mRcsControllerCall.call(new -$$Lambda$RcsOutgoingMessageDelivery$fxSVb-4v4N7q2YgopxM2Hg_pCH0(this))).longValue();
    }

    public /* synthetic */ Long lambda$getSeenTimestamp$3$RcsOutgoingMessageDelivery(IRcs iRcs, String callingPackage) throws RemoteException {
        return Long.valueOf(iRcs.getOutgoingDeliverySeenTimestamp(this.mRcsOutgoingMessageId, this.mRecipientId, callingPackage));
    }

    public void setStatus(int status) throws RcsMessageStoreException {
        this.mRcsControllerCall.callWithNoReturn(new -$$Lambda$RcsOutgoingMessageDelivery$l9Yzsl9k4Z30dUsRJ0yJpKeg9jk(this, status));
    }

    public /* synthetic */ void lambda$setStatus$4$RcsOutgoingMessageDelivery(int status, IRcs iRcs, String callingPackage) throws RemoteException {
        iRcs.setOutgoingDeliveryStatus(this.mRcsOutgoingMessageId, this.mRecipientId, status, callingPackage);
    }

    public int getStatus() throws RcsMessageStoreException {
        return ((Integer) this.mRcsControllerCall.call(new -$$Lambda$RcsOutgoingMessageDelivery$Hwf3ep_etCKWfwwAtq0Sdu0dtwY(this))).intValue();
    }

    public /* synthetic */ Integer lambda$getStatus$5$RcsOutgoingMessageDelivery(IRcs iRcs, String callingPackage) throws RemoteException {
        return Integer.valueOf(iRcs.getOutgoingDeliveryStatus(this.mRcsOutgoingMessageId, this.mRecipientId, callingPackage));
    }

    public RcsParticipant getRecipient() {
        return new RcsParticipant(this.mRcsControllerCall, this.mRecipientId);
    }

    public RcsOutgoingMessage getMessage() {
        return new RcsOutgoingMessage(this.mRcsControllerCall, this.mRcsOutgoingMessageId);
    }
}
