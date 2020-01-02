package android.telephony.ims;

import android.net.Uri;
import android.os.RemoteException;
import android.telephony.ims.RcsParticipantQueryParams.Builder;
import android.telephony.ims.aidl.IRcs;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class RcsGroupThread extends RcsThread {
    public RcsGroupThread(RcsControllerCall rcsControllerCall, int threadId) {
        super(rcsControllerCall, threadId);
    }

    public boolean isGroup() {
        return true;
    }

    public String getGroupName() throws RcsMessageStoreException {
        return (String) this.mRcsControllerCall.call(new -$$Lambda$RcsGroupThread$cwnjgWxIgjmTCKAe7pcICt4Voo0(this));
    }

    public /* synthetic */ String lambda$getGroupName$0$RcsGroupThread(IRcs iRcs, String callingPackage) throws RemoteException {
        return iRcs.getGroupThreadName(this.mThreadId, callingPackage);
    }

    public void setGroupName(String groupName) throws RcsMessageStoreException {
        this.mRcsControllerCall.callWithNoReturn(new -$$Lambda$RcsGroupThread$ZorE2WcUPTtLCwMm_x5CnWwa7YI(this, groupName));
    }

    public /* synthetic */ void lambda$setGroupName$1$RcsGroupThread(String groupName, IRcs iRcs, String callingPackage) throws RemoteException {
        iRcs.setGroupThreadName(this.mThreadId, groupName, callingPackage);
    }

    public Uri getGroupIcon() throws RcsMessageStoreException {
        return (Uri) this.mRcsControllerCall.call(new -$$Lambda$RcsGroupThread$4K1iTAEPwdeTAbDd4wTsX1Jl4S4(this));
    }

    public /* synthetic */ Uri lambda$getGroupIcon$2$RcsGroupThread(IRcs iRcs, String callingPackage) throws RemoteException {
        return iRcs.getGroupThreadIcon(this.mThreadId, callingPackage);
    }

    public void setGroupIcon(Uri groupIcon) throws RcsMessageStoreException {
        this.mRcsControllerCall.callWithNoReturn(new -$$Lambda$RcsGroupThread$2-3X4NWEVE7qw298P70JdcMW6oM(this, groupIcon));
    }

    public /* synthetic */ void lambda$setGroupIcon$3$RcsGroupThread(Uri groupIcon, IRcs iRcs, String callingPackage) throws RemoteException {
        iRcs.setGroupThreadIcon(this.mThreadId, groupIcon, callingPackage);
    }

    public RcsParticipant getOwner() throws RcsMessageStoreException {
        return new RcsParticipant(this.mRcsControllerCall, ((Integer) this.mRcsControllerCall.call(new -$$Lambda$RcsGroupThread$OMEGtapvlm86Yn7pLPBR5He4UoQ(this))).intValue());
    }

    public /* synthetic */ Integer lambda$getOwner$4$RcsGroupThread(IRcs iRcs, String callingPackage) throws RemoteException {
        return Integer.valueOf(iRcs.getGroupThreadOwner(this.mThreadId, callingPackage));
    }

    public void setOwner(RcsParticipant participant) throws RcsMessageStoreException {
        this.mRcsControllerCall.callWithNoReturn(new -$$Lambda$RcsGroupThread$9QKuv_xqJEallZ-aE2sSumu3POo(this, participant));
    }

    public /* synthetic */ void lambda$setOwner$5$RcsGroupThread(RcsParticipant participant, IRcs iRcs, String callingPackage) throws RemoteException {
        iRcs.setGroupThreadOwner(this.mThreadId, participant.getId(), callingPackage);
    }

    public void addParticipant(RcsParticipant participant) throws RcsMessageStoreException {
        if (participant != null) {
            this.mRcsControllerCall.callWithNoReturn(new -$$Lambda$RcsGroupThread$HaJSnZuef49b66N8v9ayzVaOQxQ(this, participant));
        }
    }

    public /* synthetic */ void lambda$addParticipant$6$RcsGroupThread(RcsParticipant participant, IRcs iRcs, String callingPackage) throws RemoteException {
        iRcs.addParticipantToGroupThread(this.mThreadId, participant.getId(), callingPackage);
    }

    public void removeParticipant(RcsParticipant participant) throws RcsMessageStoreException {
        if (participant != null) {
            this.mRcsControllerCall.callWithNoReturn(new -$$Lambda$RcsGroupThread$xvETBJ_gzJJ5zvelRSNsYZBdXKw(this, participant));
        }
    }

    public /* synthetic */ void lambda$removeParticipant$7$RcsGroupThread(RcsParticipant participant, IRcs iRcs, String callingPackage) throws RemoteException {
        iRcs.removeParticipantFromGroupThread(this.mThreadId, participant.getId(), callingPackage);
    }

    public Set<RcsParticipant> getParticipants() throws RcsMessageStoreException {
        return Collections.unmodifiableSet(new LinkedHashSet(new RcsParticipantQueryResult(this.mRcsControllerCall, (RcsParticipantQueryResultParcelable) this.mRcsControllerCall.call(new -$$Lambda$RcsGroupThread$X2eY_CkF7PfEGF8QwmaD6Cv0PhI(new Builder().setThread(this).build()))).getParticipants()));
    }

    public Uri getConferenceUri() throws RcsMessageStoreException {
        return (Uri) this.mRcsControllerCall.call(new -$$Lambda$RcsGroupThread$hYpkX2Z60Pf5FiSb6pvoBpmHfXA(this));
    }

    public /* synthetic */ Uri lambda$getConferenceUri$9$RcsGroupThread(IRcs iRcs, String callingPackage) throws RemoteException {
        return iRcs.getGroupThreadConferenceUri(this.mThreadId, callingPackage);
    }

    public void setConferenceUri(Uri conferenceUri) throws RcsMessageStoreException {
        this.mRcsControllerCall.callWithNoReturn(new -$$Lambda$RcsGroupThread$LhWdWS6noezEn0xijClZdbKHOas(this, conferenceUri));
    }

    public /* synthetic */ void lambda$setConferenceUri$10$RcsGroupThread(Uri conferenceUri, IRcs iRcs, String callingPackage) throws RemoteException {
        iRcs.setGroupThreadConferenceUri(this.mThreadId, conferenceUri, callingPackage);
    }
}
