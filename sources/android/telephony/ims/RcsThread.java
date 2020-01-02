package android.telephony.ims;

import android.os.RemoteException;
import android.telephony.ims.RcsMessageQueryParams.Builder;
import android.telephony.ims.aidl.IRcs;
import com.android.internal.annotations.VisibleForTesting;

public abstract class RcsThread {
    protected final RcsControllerCall mRcsControllerCall;
    protected int mThreadId;

    public abstract boolean isGroup();

    protected RcsThread(RcsControllerCall rcsControllerCall, int threadId) {
        this.mThreadId = threadId;
        this.mRcsControllerCall = rcsControllerCall;
    }

    public RcsMessageSnippet getSnippet() throws RcsMessageStoreException {
        return (RcsMessageSnippet) this.mRcsControllerCall.call(new -$$Lambda$RcsThread$TwqOqnkLjl05BhB2arTpJkBo73Y(this));
    }

    public /* synthetic */ RcsMessageSnippet lambda$getSnippet$0$RcsThread(IRcs iRcs, String callingPackage) throws RemoteException {
        return iRcs.getMessageSnippet(this.mThreadId, callingPackage);
    }

    public RcsIncomingMessage addIncomingMessage(RcsIncomingMessageCreationParams rcsIncomingMessageCreationParams) throws RcsMessageStoreException {
        return new RcsIncomingMessage(this.mRcsControllerCall, ((Integer) this.mRcsControllerCall.call(new -$$Lambda$RcsThread$9gFw0KtL-BczxOxCksL2zOV2xHM(this, rcsIncomingMessageCreationParams))).intValue());
    }

    public /* synthetic */ Integer lambda$addIncomingMessage$1$RcsThread(RcsIncomingMessageCreationParams rcsIncomingMessageCreationParams, IRcs iRcs, String callingPackage) throws RemoteException {
        return Integer.valueOf(iRcs.addIncomingMessage(this.mThreadId, rcsIncomingMessageCreationParams, callingPackage));
    }

    public RcsOutgoingMessage addOutgoingMessage(RcsOutgoingMessageCreationParams rcsOutgoingMessageCreationParams) throws RcsMessageStoreException {
        return new RcsOutgoingMessage(this.mRcsControllerCall, ((Integer) this.mRcsControllerCall.call(new -$$Lambda$RcsThread$_9zf-uqUJl6VjAbIMvQwKcAyzUs(this, rcsOutgoingMessageCreationParams))).intValue());
    }

    public /* synthetic */ Integer lambda$addOutgoingMessage$2$RcsThread(RcsOutgoingMessageCreationParams rcsOutgoingMessageCreationParams, IRcs iRcs, String callingPackage) throws RemoteException {
        return Integer.valueOf(iRcs.addOutgoingMessage(this.mThreadId, rcsOutgoingMessageCreationParams, callingPackage));
    }

    public void deleteMessage(RcsMessage rcsMessage) throws RcsMessageStoreException {
        this.mRcsControllerCall.callWithNoReturn(new -$$Lambda$RcsThread$uAkHFwrvypgP5w5y0Uy4uwQ6blY(this, rcsMessage));
    }

    public /* synthetic */ void lambda$deleteMessage$3$RcsThread(RcsMessage rcsMessage, IRcs iRcs, String callingPackage) throws RemoteException {
        iRcs.deleteMessage(rcsMessage.getId(), rcsMessage.isIncoming(), this.mThreadId, isGroup(), callingPackage);
    }

    public RcsMessageQueryResult getMessages() throws RcsMessageStoreException {
        RcsMessageQueryParams queryParams = new Builder().setThread(this).build();
        RcsControllerCall rcsControllerCall = this.mRcsControllerCall;
        return new RcsMessageQueryResult(rcsControllerCall, (RcsMessageQueryResultParcelable) rcsControllerCall.call(new -$$Lambda$RcsThread$A9iPL3bU3iiRv1xCYNUNP76n6Vw(queryParams)));
    }

    @VisibleForTesting
    public int getThreadId() {
        return this.mThreadId;
    }

    public int getThreadType() {
        return isGroup();
    }
}
