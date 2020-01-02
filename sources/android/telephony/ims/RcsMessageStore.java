package android.telephony.ims;

import android.content.Context;
import android.net.Uri;
import java.util.List;

public class RcsMessageStore {
    RcsControllerCall mRcsControllerCall;

    RcsMessageStore(Context context) {
        this.mRcsControllerCall = new RcsControllerCall(context);
    }

    public RcsThreadQueryResult getRcsThreads(RcsThreadQueryParams queryParameters) throws RcsMessageStoreException {
        RcsControllerCall rcsControllerCall = this.mRcsControllerCall;
        return new RcsThreadQueryResult(rcsControllerCall, (RcsThreadQueryResultParcelable) rcsControllerCall.call(new -$$Lambda$RcsMessageStore$z090Zf4wxRrBwUxXanwm4N3vb7w(queryParameters)));
    }

    public RcsThreadQueryResult getRcsThreads(RcsQueryContinuationToken continuationToken) throws RcsMessageStoreException {
        RcsControllerCall rcsControllerCall = this.mRcsControllerCall;
        return new RcsThreadQueryResult(rcsControllerCall, (RcsThreadQueryResultParcelable) rcsControllerCall.call(new -$$Lambda$RcsMessageStore$XArwINUevYo-Ol_OgZskFwRkGhs(continuationToken)));
    }

    public RcsParticipantQueryResult getRcsParticipants(RcsParticipantQueryParams queryParameters) throws RcsMessageStoreException {
        RcsControllerCall rcsControllerCall = this.mRcsControllerCall;
        return new RcsParticipantQueryResult(rcsControllerCall, (RcsParticipantQueryResultParcelable) rcsControllerCall.call(new -$$Lambda$RcsMessageStore$720PbSnOJzhKXiqHw1UEfx5w-6A(queryParameters)));
    }

    public RcsParticipantQueryResult getRcsParticipants(RcsQueryContinuationToken continuationToken) throws RcsMessageStoreException {
        RcsControllerCall rcsControllerCall = this.mRcsControllerCall;
        return new RcsParticipantQueryResult(rcsControllerCall, (RcsParticipantQueryResultParcelable) rcsControllerCall.call(new -$$Lambda$RcsMessageStore$tSyQsX68KutSWLEXxfgNSJ47ep0(continuationToken)));
    }

    public RcsMessageQueryResult getRcsMessages(RcsMessageQueryParams queryParams) throws RcsMessageStoreException {
        RcsControllerCall rcsControllerCall = this.mRcsControllerCall;
        return new RcsMessageQueryResult(rcsControllerCall, (RcsMessageQueryResultParcelable) rcsControllerCall.call(new -$$Lambda$RcsMessageStore$5QXAY7bGFdmsWgLF0pk1tyYYovg(queryParams)));
    }

    public RcsMessageQueryResult getRcsMessages(RcsQueryContinuationToken continuationToken) throws RcsMessageStoreException {
        RcsControllerCall rcsControllerCall = this.mRcsControllerCall;
        return new RcsMessageQueryResult(rcsControllerCall, (RcsMessageQueryResultParcelable) rcsControllerCall.call(new -$$Lambda$RcsMessageStore$fs2V7Gtqd2gkYR7NanLG2NjZNho(continuationToken)));
    }

    public RcsEventQueryResult getRcsEvents(RcsEventQueryParams queryParams) throws RcsMessageStoreException {
        return ((RcsEventQueryResultDescriptor) this.mRcsControllerCall.call(new -$$Lambda$RcsMessageStore$IvBKppwBc6MDwzIkAi2XJcVB-iI(queryParams))).getRcsEventQueryResult(this.mRcsControllerCall);
    }

    public RcsEventQueryResult getRcsEvents(RcsQueryContinuationToken continuationToken) throws RcsMessageStoreException {
        return ((RcsEventQueryResultDescriptor) this.mRcsControllerCall.call(new -$$Lambda$RcsMessageStore$RFZerRPNR1WyCuEIu6_yEveDhrk(continuationToken))).getRcsEventQueryResult(this.mRcsControllerCall);
    }

    public void persistRcsEvent(RcsEvent rcsEvent) throws RcsMessageStoreException {
        rcsEvent.persist(this.mRcsControllerCall);
    }

    public Rcs1To1Thread createRcs1To1Thread(RcsParticipant recipient) throws RcsMessageStoreException {
        RcsControllerCall rcsControllerCall = this.mRcsControllerCall;
        return new Rcs1To1Thread(rcsControllerCall, ((Integer) rcsControllerCall.call(new -$$Lambda$RcsMessageStore$eOFObBGn-N5PMKJvVTBw06iJWQ4(recipient))).intValue());
    }

    public RcsGroupThread createGroupThread(List<RcsParticipant> recipients, String groupName, Uri groupIcon) throws RcsMessageStoreException {
        int[] recipientIds = null;
        if (recipients != null) {
            recipientIds = new int[recipients.size()];
            for (int i = 0; i < recipients.size(); i++) {
                recipientIds[i] = ((RcsParticipant) recipients.get(i)).getId();
            }
        }
        return new RcsGroupThread(this.mRcsControllerCall, ((Integer) this.mRcsControllerCall.call(new -$$Lambda$RcsMessageStore$g309WUVpYx8N7s-uWdUAGJXtJOs(recipientIds, groupName, groupIcon))).intValue());
    }

    public void deleteThread(RcsThread thread) throws RcsMessageStoreException {
        if (thread != null && !((Boolean) this.mRcsControllerCall.call(new -$$Lambda$RcsMessageStore$nbXWLR_ux8VCEHNEyE7JO0J05YI(thread))).booleanValue()) {
            throw new RcsMessageStoreException("Could not delete RcsThread");
        }
    }

    public RcsParticipant createRcsParticipant(String canonicalAddress, String alias) throws RcsMessageStoreException {
        RcsControllerCall rcsControllerCall = this.mRcsControllerCall;
        return new RcsParticipant(rcsControllerCall, ((Integer) rcsControllerCall.call(new -$$Lambda$RcsMessageStore$d1Om4XlR70Dyh7qD9d6F4NZZkQI(canonicalAddress, alias))).intValue());
    }
}
