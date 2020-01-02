package android.telephony.ims;

import android.net.Uri;
import android.os.RemoteException;
import android.telephony.ims.aidl.IRcs;

public final class RcsGroupThreadIconChangedEvent extends RcsGroupThreadEvent {
    private final Uri mNewIcon;

    public RcsGroupThreadIconChangedEvent(long timestamp, RcsGroupThread rcsGroupThread, RcsParticipant originatingParticipant, Uri newIcon) {
        super(timestamp, rcsGroupThread, originatingParticipant);
        this.mNewIcon = newIcon;
    }

    public Uri getNewIcon() {
        return this.mNewIcon;
    }

    public /* synthetic */ Integer lambda$persist$0$RcsGroupThreadIconChangedEvent(IRcs iRcs, String callingPackage) throws RemoteException {
        return Integer.valueOf(iRcs.createGroupThreadIconChangedEvent(getTimestamp(), getRcsGroupThread().getThreadId(), getOriginatingParticipant().getId(), this.mNewIcon, callingPackage));
    }

    /* Access modifiers changed, original: 0000 */
    public void persist(RcsControllerCall rcsControllerCall) throws RcsMessageStoreException {
        rcsControllerCall.call(new -$$Lambda$RcsGroupThreadIconChangedEvent$XfKd9jzuhr_hAT3mvSOBgWj08Js(this));
    }
}
