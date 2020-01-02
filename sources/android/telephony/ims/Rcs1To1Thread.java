package android.telephony.ims;

import android.os.RemoteException;
import android.telephony.ims.aidl.IRcs;

public class Rcs1To1Thread extends RcsThread {
    private int mThreadId;

    public Rcs1To1Thread(RcsControllerCall rcsControllerCall, int threadId) {
        super(rcsControllerCall, threadId);
        this.mThreadId = threadId;
    }

    public boolean isGroup() {
        return false;
    }

    public long getFallbackThreadId() throws RcsMessageStoreException {
        return ((Long) this.mRcsControllerCall.call(new -$$Lambda$Rcs1To1Thread$_6gUCvjDS6WXqf0AClQwrZ7ZpSc(this))).longValue();
    }

    public /* synthetic */ Long lambda$getFallbackThreadId$0$Rcs1To1Thread(IRcs iRcs, String callingPackage) throws RemoteException {
        return Long.valueOf(iRcs.get1To1ThreadFallbackThreadId(this.mThreadId, callingPackage));
    }

    public void setFallbackThreadId(long fallbackThreadId) throws RcsMessageStoreException {
        this.mRcsControllerCall.callWithNoReturn(new -$$Lambda$Rcs1To1Thread$vx_evSYitgJIMB6l-hANvSJpdBE(this, fallbackThreadId));
    }

    public /* synthetic */ void lambda$setFallbackThreadId$1$Rcs1To1Thread(long fallbackThreadId, IRcs iRcs, String callingPackage) throws RemoteException {
        iRcs.set1To1ThreadFallbackThreadId(this.mThreadId, fallbackThreadId, callingPackage);
    }

    public RcsParticipant getRecipient() throws RcsMessageStoreException {
        return new RcsParticipant(this.mRcsControllerCall, ((Integer) this.mRcsControllerCall.call(new -$$Lambda$Rcs1To1Thread$DlCgifrXUJFouqWWh-0GG6hzH-s(this))).intValue());
    }

    public /* synthetic */ Integer lambda$getRecipient$2$Rcs1To1Thread(IRcs iRcs, String callingPackage) throws RemoteException {
        return Integer.valueOf(iRcs.get1To1ThreadOtherParticipantId(this.mThreadId, callingPackage));
    }
}
