package android.telephony.ims;

import com.android.ims.RcsTypeIdPair;
import java.util.List;
import java.util.stream.Collectors;

public final class RcsMessageQueryResult {
    private final RcsControllerCall mRcsControllerCall;
    private final RcsMessageQueryResultParcelable mRcsMessageQueryResultParcelable;

    RcsMessageQueryResult(RcsControllerCall rcsControllerCall, RcsMessageQueryResultParcelable rcsMessageQueryResultParcelable) {
        this.mRcsControllerCall = rcsControllerCall;
        this.mRcsMessageQueryResultParcelable = rcsMessageQueryResultParcelable;
    }

    public RcsQueryContinuationToken getContinuationToken() {
        return this.mRcsMessageQueryResultParcelable.mContinuationToken;
    }

    public List<RcsMessage> getMessages() {
        return (List) this.mRcsMessageQueryResultParcelable.mMessageTypeIdPairs.stream().map(new -$$Lambda$RcsMessageQueryResult$20XnTdVu75hlh0utIOyf1L-ZpTE(this)).collect(Collectors.toList());
    }

    public /* synthetic */ RcsMessage lambda$getMessages$0$RcsMessageQueryResult(RcsTypeIdPair typeIdPair) {
        if (typeIdPair.getType() == 1) {
            return new RcsIncomingMessage(this.mRcsControllerCall, typeIdPair.getId());
        }
        return new RcsOutgoingMessage(this.mRcsControllerCall, typeIdPair.getId());
    }
}
