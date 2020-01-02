package android.telephony.ims;

import android.os.RemoteException;
import android.telephony.ims.aidl.IRcs;
import java.util.ArrayList;
import java.util.List;

public class RcsOutgoingMessage extends RcsMessage {
    RcsOutgoingMessage(RcsControllerCall rcsControllerCall, int id) {
        super(rcsControllerCall, id);
    }

    public List<RcsOutgoingMessageDelivery> getOutgoingDeliveries() throws RcsMessageStoreException {
        List<RcsOutgoingMessageDelivery> messageDeliveries = new ArrayList();
        int[] deliveryParticipants = (int[]) this.mRcsControllerCall.call(new -$$Lambda$RcsOutgoingMessage$uP-7yJmMalJRjXgq_qS_YvAUKuo(this));
        if (deliveryParticipants != null) {
            for (Integer deliveryParticipant : deliveryParticipants) {
                messageDeliveries.add(new RcsOutgoingMessageDelivery(this.mRcsControllerCall, Integer.valueOf(deliveryParticipant).intValue(), this.mId));
            }
        }
        return messageDeliveries;
    }

    public /* synthetic */ int[] lambda$getOutgoingDeliveries$0$RcsOutgoingMessage(IRcs iRcs, String callingPackage) throws RemoteException {
        return iRcs.getMessageRecipients(this.mId, callingPackage);
    }

    public boolean isIncoming() {
        return false;
    }
}
