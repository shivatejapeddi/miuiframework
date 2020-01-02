package android.telephony.ims;

import com.android.ims.RcsTypeIdPair;
import java.util.List;
import java.util.stream.Collectors;

public final class RcsThreadQueryResult {
    private final RcsControllerCall mRcsControllerCall;
    private final RcsThreadQueryResultParcelable mRcsThreadQueryResultParcelable;

    RcsThreadQueryResult(RcsControllerCall rcsControllerCall, RcsThreadQueryResultParcelable rcsThreadQueryResultParcelable) {
        this.mRcsControllerCall = rcsControllerCall;
        this.mRcsThreadQueryResultParcelable = rcsThreadQueryResultParcelable;
    }

    public RcsQueryContinuationToken getContinuationToken() {
        return this.mRcsThreadQueryResultParcelable.mContinuationToken;
    }

    public List<RcsThread> getThreads() {
        return (List) this.mRcsThreadQueryResultParcelable.mRcsThreadIds.stream().map(new -$$Lambda$RcsThreadQueryResult$HsaNrgQR1ZYF-F0J6msBz3OMF6s(this)).collect(Collectors.toList());
    }

    public /* synthetic */ RcsThread lambda$getThreads$0$RcsThreadQueryResult(RcsTypeIdPair typeIdPair) {
        if (typeIdPair.getType() == 0) {
            return new Rcs1To1Thread(this.mRcsControllerCall, typeIdPair.getId());
        }
        return new RcsGroupThread(this.mRcsControllerCall, typeIdPair.getId());
    }
}
