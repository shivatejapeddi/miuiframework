package android.telephony.ims.aidl;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.ims.RcsEventQueryParams;
import android.telephony.ims.RcsEventQueryResultDescriptor;
import android.telephony.ims.RcsFileTransferCreationParams;
import android.telephony.ims.RcsIncomingMessageCreationParams;
import android.telephony.ims.RcsMessageQueryParams;
import android.telephony.ims.RcsMessageQueryResultParcelable;
import android.telephony.ims.RcsMessageSnippet;
import android.telephony.ims.RcsOutgoingMessageCreationParams;
import android.telephony.ims.RcsParticipantQueryParams;
import android.telephony.ims.RcsParticipantQueryResultParcelable;
import android.telephony.ims.RcsQueryContinuationToken;
import android.telephony.ims.RcsThreadQueryParams;
import android.telephony.ims.RcsThreadQueryResultParcelable;

public interface IRcs extends IInterface {

    public static class Default implements IRcs {
        public RcsThreadQueryResultParcelable getRcsThreads(RcsThreadQueryParams queryParams, String callingPackage) throws RemoteException {
            return null;
        }

        public RcsThreadQueryResultParcelable getRcsThreadsWithToken(RcsQueryContinuationToken continuationToken, String callingPackage) throws RemoteException {
            return null;
        }

        public RcsParticipantQueryResultParcelable getParticipants(RcsParticipantQueryParams queryParams, String callingPackage) throws RemoteException {
            return null;
        }

        public RcsParticipantQueryResultParcelable getParticipantsWithToken(RcsQueryContinuationToken continuationToken, String callingPackage) throws RemoteException {
            return null;
        }

        public RcsMessageQueryResultParcelable getMessages(RcsMessageQueryParams queryParams, String callingPackage) throws RemoteException {
            return null;
        }

        public RcsMessageQueryResultParcelable getMessagesWithToken(RcsQueryContinuationToken continuationToken, String callingPackage) throws RemoteException {
            return null;
        }

        public RcsEventQueryResultDescriptor getEvents(RcsEventQueryParams queryParams, String callingPackage) throws RemoteException {
            return null;
        }

        public RcsEventQueryResultDescriptor getEventsWithToken(RcsQueryContinuationToken continuationToken, String callingPackage) throws RemoteException {
            return null;
        }

        public boolean deleteThread(int threadId, int threadType, String callingPackage) throws RemoteException {
            return false;
        }

        public int createRcs1To1Thread(int participantId, String callingPackage) throws RemoteException {
            return 0;
        }

        public int createGroupThread(int[] participantIds, String groupName, Uri groupIcon, String callingPackage) throws RemoteException {
            return 0;
        }

        public int addIncomingMessage(int rcsThreadId, RcsIncomingMessageCreationParams rcsIncomingMessageCreationParams, String callingPackage) throws RemoteException {
            return 0;
        }

        public int addOutgoingMessage(int rcsThreadId, RcsOutgoingMessageCreationParams rcsOutgoingMessageCreationParams, String callingPackage) throws RemoteException {
            return 0;
        }

        public void deleteMessage(int rcsMessageId, boolean isIncoming, int rcsThreadId, boolean isGroup, String callingPackage) throws RemoteException {
        }

        public RcsMessageSnippet getMessageSnippet(int rcsThreadId, String callingPackage) throws RemoteException {
            return null;
        }

        public void set1To1ThreadFallbackThreadId(int rcsThreadId, long fallbackId, String callingPackage) throws RemoteException {
        }

        public long get1To1ThreadFallbackThreadId(int rcsThreadId, String callingPackage) throws RemoteException {
            return 0;
        }

        public int get1To1ThreadOtherParticipantId(int rcsThreadId, String callingPackage) throws RemoteException {
            return 0;
        }

        public void setGroupThreadName(int rcsThreadId, String groupName, String callingPackage) throws RemoteException {
        }

        public String getGroupThreadName(int rcsThreadId, String callingPackage) throws RemoteException {
            return null;
        }

        public void setGroupThreadIcon(int rcsThreadId, Uri groupIcon, String callingPackage) throws RemoteException {
        }

        public Uri getGroupThreadIcon(int rcsThreadId, String callingPackage) throws RemoteException {
            return null;
        }

        public void setGroupThreadOwner(int rcsThreadId, int participantId, String callingPackage) throws RemoteException {
        }

        public int getGroupThreadOwner(int rcsThreadId, String callingPackage) throws RemoteException {
            return 0;
        }

        public void setGroupThreadConferenceUri(int rcsThreadId, Uri conferenceUri, String callingPackage) throws RemoteException {
        }

        public Uri getGroupThreadConferenceUri(int rcsThreadId, String callingPackage) throws RemoteException {
            return null;
        }

        public void addParticipantToGroupThread(int rcsThreadId, int participantId, String callingPackage) throws RemoteException {
        }

        public void removeParticipantFromGroupThread(int rcsThreadId, int participantId, String callingPackage) throws RemoteException {
        }

        public int createRcsParticipant(String canonicalAddress, String alias, String callingPackage) throws RemoteException {
            return 0;
        }

        public String getRcsParticipantCanonicalAddress(int participantId, String callingPackage) throws RemoteException {
            return null;
        }

        public String getRcsParticipantAlias(int participantId, String callingPackage) throws RemoteException {
            return null;
        }

        public void setRcsParticipantAlias(int id, String alias, String callingPackage) throws RemoteException {
        }

        public String getRcsParticipantContactId(int participantId, String callingPackage) throws RemoteException {
            return null;
        }

        public void setRcsParticipantContactId(int participantId, String contactId, String callingPackage) throws RemoteException {
        }

        public void setMessageSubId(int messageId, boolean isIncoming, int subId, String callingPackage) throws RemoteException {
        }

        public int getMessageSubId(int messageId, boolean isIncoming, String callingPackage) throws RemoteException {
            return 0;
        }

        public void setMessageStatus(int messageId, boolean isIncoming, int status, String callingPackage) throws RemoteException {
        }

        public int getMessageStatus(int messageId, boolean isIncoming, String callingPackage) throws RemoteException {
            return 0;
        }

        public void setMessageOriginationTimestamp(int messageId, boolean isIncoming, long originationTimestamp, String callingPackage) throws RemoteException {
        }

        public long getMessageOriginationTimestamp(int messageId, boolean isIncoming, String callingPackage) throws RemoteException {
            return 0;
        }

        public void setGlobalMessageIdForMessage(int messageId, boolean isIncoming, String globalId, String callingPackage) throws RemoteException {
        }

        public String getGlobalMessageIdForMessage(int messageId, boolean isIncoming, String callingPackage) throws RemoteException {
            return null;
        }

        public void setMessageArrivalTimestamp(int messageId, boolean isIncoming, long arrivalTimestamp, String callingPackage) throws RemoteException {
        }

        public long getMessageArrivalTimestamp(int messageId, boolean isIncoming, String callingPackage) throws RemoteException {
            return 0;
        }

        public void setMessageSeenTimestamp(int messageId, boolean isIncoming, long seenTimestamp, String callingPackage) throws RemoteException {
        }

        public long getMessageSeenTimestamp(int messageId, boolean isIncoming, String callingPackage) throws RemoteException {
            return 0;
        }

        public void setTextForMessage(int messageId, boolean isIncoming, String text, String callingPackage) throws RemoteException {
        }

        public String getTextForMessage(int messageId, boolean isIncoming, String callingPackage) throws RemoteException {
            return null;
        }

        public void setLatitudeForMessage(int messageId, boolean isIncoming, double latitude, String callingPackage) throws RemoteException {
        }

        public double getLatitudeForMessage(int messageId, boolean isIncoming, String callingPackage) throws RemoteException {
            return 0.0d;
        }

        public void setLongitudeForMessage(int messageId, boolean isIncoming, double longitude, String callingPackage) throws RemoteException {
        }

        public double getLongitudeForMessage(int messageId, boolean isIncoming, String callingPackage) throws RemoteException {
            return 0.0d;
        }

        public int[] getFileTransfersAttachedToMessage(int messageId, boolean isIncoming, String callingPackage) throws RemoteException {
            return null;
        }

        public int getSenderParticipant(int messageId, String callingPackage) throws RemoteException {
            return 0;
        }

        public int[] getMessageRecipients(int messageId, String callingPackage) throws RemoteException {
            return null;
        }

        public long getOutgoingDeliveryDeliveredTimestamp(int messageId, int participantId, String callingPackage) throws RemoteException {
            return 0;
        }

        public void setOutgoingDeliveryDeliveredTimestamp(int messageId, int participantId, long deliveredTimestamp, String callingPackage) throws RemoteException {
        }

        public long getOutgoingDeliverySeenTimestamp(int messageId, int participantId, String callingPackage) throws RemoteException {
            return 0;
        }

        public void setOutgoingDeliverySeenTimestamp(int messageId, int participantId, long seenTimestamp, String callingPackage) throws RemoteException {
        }

        public int getOutgoingDeliveryStatus(int messageId, int participantId, String callingPackage) throws RemoteException {
            return 0;
        }

        public void setOutgoingDeliveryStatus(int messageId, int participantId, int status, String callingPackage) throws RemoteException {
        }

        public int storeFileTransfer(int messageId, boolean isIncoming, RcsFileTransferCreationParams fileTransferCreationParams, String callingPackage) throws RemoteException {
            return 0;
        }

        public void deleteFileTransfer(int partId, String callingPackage) throws RemoteException {
        }

        public void setFileTransferSessionId(int partId, String sessionId, String callingPackage) throws RemoteException {
        }

        public String getFileTransferSessionId(int partId, String callingPackage) throws RemoteException {
            return null;
        }

        public void setFileTransferContentUri(int partId, Uri contentUri, String callingPackage) throws RemoteException {
        }

        public Uri getFileTransferContentUri(int partId, String callingPackage) throws RemoteException {
            return null;
        }

        public void setFileTransferContentType(int partId, String contentType, String callingPackage) throws RemoteException {
        }

        public String getFileTransferContentType(int partId, String callingPackage) throws RemoteException {
            return null;
        }

        public void setFileTransferFileSize(int partId, long fileSize, String callingPackage) throws RemoteException {
        }

        public long getFileTransferFileSize(int partId, String callingPackage) throws RemoteException {
            return 0;
        }

        public void setFileTransferTransferOffset(int partId, long transferOffset, String callingPackage) throws RemoteException {
        }

        public long getFileTransferTransferOffset(int partId, String callingPackage) throws RemoteException {
            return 0;
        }

        public void setFileTransferStatus(int partId, int transferStatus, String callingPackage) throws RemoteException {
        }

        public int getFileTransferStatus(int partId, String callingPackage) throws RemoteException {
            return 0;
        }

        public void setFileTransferWidth(int partId, int width, String callingPackage) throws RemoteException {
        }

        public int getFileTransferWidth(int partId, String callingPackage) throws RemoteException {
            return 0;
        }

        public void setFileTransferHeight(int partId, int height, String callingPackage) throws RemoteException {
        }

        public int getFileTransferHeight(int partId, String callingPackage) throws RemoteException {
            return 0;
        }

        public void setFileTransferLength(int partId, long length, String callingPackage) throws RemoteException {
        }

        public long getFileTransferLength(int partId, String callingPackage) throws RemoteException {
            return 0;
        }

        public void setFileTransferPreviewUri(int partId, Uri uri, String callingPackage) throws RemoteException {
        }

        public Uri getFileTransferPreviewUri(int partId, String callingPackage) throws RemoteException {
            return null;
        }

        public void setFileTransferPreviewType(int partId, String type, String callingPackage) throws RemoteException {
        }

        public String getFileTransferPreviewType(int partId, String callingPackage) throws RemoteException {
            return null;
        }

        public int createGroupThreadNameChangedEvent(long timestamp, int threadId, int originationParticipantId, String newName, String callingPackage) throws RemoteException {
            return 0;
        }

        public int createGroupThreadIconChangedEvent(long timestamp, int threadId, int originationParticipantId, Uri newIcon, String callingPackage) throws RemoteException {
            return 0;
        }

        public int createGroupThreadParticipantJoinedEvent(long timestamp, int threadId, int originationParticipantId, int participantId, String callingPackage) throws RemoteException {
            return 0;
        }

        public int createGroupThreadParticipantLeftEvent(long timestamp, int threadId, int originationParticipantId, int participantId, String callingPackage) throws RemoteException {
            return 0;
        }

        public int createParticipantAliasChangedEvent(long timestamp, int participantId, String newAlias, String callingPackage) throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IRcs {
        private static final String DESCRIPTOR = "android.telephony.ims.aidl.IRcs";
        static final int TRANSACTION_addIncomingMessage = 12;
        static final int TRANSACTION_addOutgoingMessage = 13;
        static final int TRANSACTION_addParticipantToGroupThread = 27;
        static final int TRANSACTION_createGroupThread = 11;
        static final int TRANSACTION_createGroupThreadIconChangedEvent = 87;
        static final int TRANSACTION_createGroupThreadNameChangedEvent = 86;
        static final int TRANSACTION_createGroupThreadParticipantJoinedEvent = 88;
        static final int TRANSACTION_createGroupThreadParticipantLeftEvent = 89;
        static final int TRANSACTION_createParticipantAliasChangedEvent = 90;
        static final int TRANSACTION_createRcs1To1Thread = 10;
        static final int TRANSACTION_createRcsParticipant = 29;
        static final int TRANSACTION_deleteFileTransfer = 63;
        static final int TRANSACTION_deleteMessage = 14;
        static final int TRANSACTION_deleteThread = 9;
        static final int TRANSACTION_get1To1ThreadFallbackThreadId = 17;
        static final int TRANSACTION_get1To1ThreadOtherParticipantId = 18;
        static final int TRANSACTION_getEvents = 7;
        static final int TRANSACTION_getEventsWithToken = 8;
        static final int TRANSACTION_getFileTransferContentType = 69;
        static final int TRANSACTION_getFileTransferContentUri = 67;
        static final int TRANSACTION_getFileTransferFileSize = 71;
        static final int TRANSACTION_getFileTransferHeight = 79;
        static final int TRANSACTION_getFileTransferLength = 81;
        static final int TRANSACTION_getFileTransferPreviewType = 85;
        static final int TRANSACTION_getFileTransferPreviewUri = 83;
        static final int TRANSACTION_getFileTransferSessionId = 65;
        static final int TRANSACTION_getFileTransferStatus = 75;
        static final int TRANSACTION_getFileTransferTransferOffset = 73;
        static final int TRANSACTION_getFileTransferWidth = 77;
        static final int TRANSACTION_getFileTransfersAttachedToMessage = 53;
        static final int TRANSACTION_getGlobalMessageIdForMessage = 42;
        static final int TRANSACTION_getGroupThreadConferenceUri = 26;
        static final int TRANSACTION_getGroupThreadIcon = 22;
        static final int TRANSACTION_getGroupThreadName = 20;
        static final int TRANSACTION_getGroupThreadOwner = 24;
        static final int TRANSACTION_getLatitudeForMessage = 50;
        static final int TRANSACTION_getLongitudeForMessage = 52;
        static final int TRANSACTION_getMessageArrivalTimestamp = 44;
        static final int TRANSACTION_getMessageOriginationTimestamp = 40;
        static final int TRANSACTION_getMessageRecipients = 55;
        static final int TRANSACTION_getMessageSeenTimestamp = 46;
        static final int TRANSACTION_getMessageSnippet = 15;
        static final int TRANSACTION_getMessageStatus = 38;
        static final int TRANSACTION_getMessageSubId = 36;
        static final int TRANSACTION_getMessages = 5;
        static final int TRANSACTION_getMessagesWithToken = 6;
        static final int TRANSACTION_getOutgoingDeliveryDeliveredTimestamp = 56;
        static final int TRANSACTION_getOutgoingDeliverySeenTimestamp = 58;
        static final int TRANSACTION_getOutgoingDeliveryStatus = 60;
        static final int TRANSACTION_getParticipants = 3;
        static final int TRANSACTION_getParticipantsWithToken = 4;
        static final int TRANSACTION_getRcsParticipantAlias = 31;
        static final int TRANSACTION_getRcsParticipantCanonicalAddress = 30;
        static final int TRANSACTION_getRcsParticipantContactId = 33;
        static final int TRANSACTION_getRcsThreads = 1;
        static final int TRANSACTION_getRcsThreadsWithToken = 2;
        static final int TRANSACTION_getSenderParticipant = 54;
        static final int TRANSACTION_getTextForMessage = 48;
        static final int TRANSACTION_removeParticipantFromGroupThread = 28;
        static final int TRANSACTION_set1To1ThreadFallbackThreadId = 16;
        static final int TRANSACTION_setFileTransferContentType = 68;
        static final int TRANSACTION_setFileTransferContentUri = 66;
        static final int TRANSACTION_setFileTransferFileSize = 70;
        static final int TRANSACTION_setFileTransferHeight = 78;
        static final int TRANSACTION_setFileTransferLength = 80;
        static final int TRANSACTION_setFileTransferPreviewType = 84;
        static final int TRANSACTION_setFileTransferPreviewUri = 82;
        static final int TRANSACTION_setFileTransferSessionId = 64;
        static final int TRANSACTION_setFileTransferStatus = 74;
        static final int TRANSACTION_setFileTransferTransferOffset = 72;
        static final int TRANSACTION_setFileTransferWidth = 76;
        static final int TRANSACTION_setGlobalMessageIdForMessage = 41;
        static final int TRANSACTION_setGroupThreadConferenceUri = 25;
        static final int TRANSACTION_setGroupThreadIcon = 21;
        static final int TRANSACTION_setGroupThreadName = 19;
        static final int TRANSACTION_setGroupThreadOwner = 23;
        static final int TRANSACTION_setLatitudeForMessage = 49;
        static final int TRANSACTION_setLongitudeForMessage = 51;
        static final int TRANSACTION_setMessageArrivalTimestamp = 43;
        static final int TRANSACTION_setMessageOriginationTimestamp = 39;
        static final int TRANSACTION_setMessageSeenTimestamp = 45;
        static final int TRANSACTION_setMessageStatus = 37;
        static final int TRANSACTION_setMessageSubId = 35;
        static final int TRANSACTION_setOutgoingDeliveryDeliveredTimestamp = 57;
        static final int TRANSACTION_setOutgoingDeliverySeenTimestamp = 59;
        static final int TRANSACTION_setOutgoingDeliveryStatus = 61;
        static final int TRANSACTION_setRcsParticipantAlias = 32;
        static final int TRANSACTION_setRcsParticipantContactId = 34;
        static final int TRANSACTION_setTextForMessage = 47;
        static final int TRANSACTION_storeFileTransfer = 62;

        private static class Proxy implements IRcs {
            public static IRcs sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public RcsThreadQueryResultParcelable getRcsThreads(RcsThreadQueryParams queryParams, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    RcsThreadQueryResultParcelable rcsThreadQueryResultParcelable = 0;
                    if (queryParams != null) {
                        _data.writeInt(1);
                        queryParams.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        rcsThreadQueryResultParcelable = Stub.getDefaultImpl();
                        if (rcsThreadQueryResultParcelable != 0) {
                            rcsThreadQueryResultParcelable = Stub.getDefaultImpl().getRcsThreads(queryParams, callingPackage);
                            return rcsThreadQueryResultParcelable;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        rcsThreadQueryResultParcelable = (RcsThreadQueryResultParcelable) RcsThreadQueryResultParcelable.CREATOR.createFromParcel(_reply);
                    } else {
                        rcsThreadQueryResultParcelable = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return rcsThreadQueryResultParcelable;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public RcsThreadQueryResultParcelable getRcsThreadsWithToken(RcsQueryContinuationToken continuationToken, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (continuationToken != null) {
                        _data.writeInt(1);
                        continuationToken.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    RcsThreadQueryResultParcelable rcsThreadQueryResultParcelable = this.mRemote;
                    if (!rcsThreadQueryResultParcelable.transact(2, _data, _reply, 0)) {
                        rcsThreadQueryResultParcelable = Stub.getDefaultImpl();
                        if (rcsThreadQueryResultParcelable != null) {
                            rcsThreadQueryResultParcelable = Stub.getDefaultImpl().getRcsThreadsWithToken(continuationToken, callingPackage);
                            return rcsThreadQueryResultParcelable;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        rcsThreadQueryResultParcelable = (RcsThreadQueryResultParcelable) RcsThreadQueryResultParcelable.CREATOR.createFromParcel(_reply);
                    } else {
                        rcsThreadQueryResultParcelable = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return rcsThreadQueryResultParcelable;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public RcsParticipantQueryResultParcelable getParticipants(RcsParticipantQueryParams queryParams, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (queryParams != null) {
                        _data.writeInt(1);
                        queryParams.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    RcsParticipantQueryResultParcelable rcsParticipantQueryResultParcelable = this.mRemote;
                    if (!rcsParticipantQueryResultParcelable.transact(3, _data, _reply, 0)) {
                        rcsParticipantQueryResultParcelable = Stub.getDefaultImpl();
                        if (rcsParticipantQueryResultParcelable != null) {
                            rcsParticipantQueryResultParcelable = Stub.getDefaultImpl().getParticipants(queryParams, callingPackage);
                            return rcsParticipantQueryResultParcelable;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        rcsParticipantQueryResultParcelable = (RcsParticipantQueryResultParcelable) RcsParticipantQueryResultParcelable.CREATOR.createFromParcel(_reply);
                    } else {
                        rcsParticipantQueryResultParcelable = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return rcsParticipantQueryResultParcelable;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public RcsParticipantQueryResultParcelable getParticipantsWithToken(RcsQueryContinuationToken continuationToken, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (continuationToken != null) {
                        _data.writeInt(1);
                        continuationToken.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    RcsParticipantQueryResultParcelable rcsParticipantQueryResultParcelable = this.mRemote;
                    if (!rcsParticipantQueryResultParcelable.transact(4, _data, _reply, 0)) {
                        rcsParticipantQueryResultParcelable = Stub.getDefaultImpl();
                        if (rcsParticipantQueryResultParcelable != null) {
                            rcsParticipantQueryResultParcelable = Stub.getDefaultImpl().getParticipantsWithToken(continuationToken, callingPackage);
                            return rcsParticipantQueryResultParcelable;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        rcsParticipantQueryResultParcelable = (RcsParticipantQueryResultParcelable) RcsParticipantQueryResultParcelable.CREATOR.createFromParcel(_reply);
                    } else {
                        rcsParticipantQueryResultParcelable = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return rcsParticipantQueryResultParcelable;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public RcsMessageQueryResultParcelable getMessages(RcsMessageQueryParams queryParams, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (queryParams != null) {
                        _data.writeInt(1);
                        queryParams.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    RcsMessageQueryResultParcelable rcsMessageQueryResultParcelable = this.mRemote;
                    if (!rcsMessageQueryResultParcelable.transact(5, _data, _reply, 0)) {
                        rcsMessageQueryResultParcelable = Stub.getDefaultImpl();
                        if (rcsMessageQueryResultParcelable != null) {
                            rcsMessageQueryResultParcelable = Stub.getDefaultImpl().getMessages(queryParams, callingPackage);
                            return rcsMessageQueryResultParcelable;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        rcsMessageQueryResultParcelable = (RcsMessageQueryResultParcelable) RcsMessageQueryResultParcelable.CREATOR.createFromParcel(_reply);
                    } else {
                        rcsMessageQueryResultParcelable = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return rcsMessageQueryResultParcelable;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public RcsMessageQueryResultParcelable getMessagesWithToken(RcsQueryContinuationToken continuationToken, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (continuationToken != null) {
                        _data.writeInt(1);
                        continuationToken.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    RcsMessageQueryResultParcelable rcsMessageQueryResultParcelable = this.mRemote;
                    if (!rcsMessageQueryResultParcelable.transact(6, _data, _reply, 0)) {
                        rcsMessageQueryResultParcelable = Stub.getDefaultImpl();
                        if (rcsMessageQueryResultParcelable != null) {
                            rcsMessageQueryResultParcelable = Stub.getDefaultImpl().getMessagesWithToken(continuationToken, callingPackage);
                            return rcsMessageQueryResultParcelable;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        rcsMessageQueryResultParcelable = (RcsMessageQueryResultParcelable) RcsMessageQueryResultParcelable.CREATOR.createFromParcel(_reply);
                    } else {
                        rcsMessageQueryResultParcelable = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return rcsMessageQueryResultParcelable;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public RcsEventQueryResultDescriptor getEvents(RcsEventQueryParams queryParams, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (queryParams != null) {
                        _data.writeInt(1);
                        queryParams.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    RcsEventQueryResultDescriptor rcsEventQueryResultDescriptor = this.mRemote;
                    if (!rcsEventQueryResultDescriptor.transact(7, _data, _reply, 0)) {
                        rcsEventQueryResultDescriptor = Stub.getDefaultImpl();
                        if (rcsEventQueryResultDescriptor != null) {
                            rcsEventQueryResultDescriptor = Stub.getDefaultImpl().getEvents(queryParams, callingPackage);
                            return rcsEventQueryResultDescriptor;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        rcsEventQueryResultDescriptor = (RcsEventQueryResultDescriptor) RcsEventQueryResultDescriptor.CREATOR.createFromParcel(_reply);
                    } else {
                        rcsEventQueryResultDescriptor = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return rcsEventQueryResultDescriptor;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public RcsEventQueryResultDescriptor getEventsWithToken(RcsQueryContinuationToken continuationToken, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (continuationToken != null) {
                        _data.writeInt(1);
                        continuationToken.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    RcsEventQueryResultDescriptor rcsEventQueryResultDescriptor = this.mRemote;
                    if (!rcsEventQueryResultDescriptor.transact(8, _data, _reply, 0)) {
                        rcsEventQueryResultDescriptor = Stub.getDefaultImpl();
                        if (rcsEventQueryResultDescriptor != null) {
                            rcsEventQueryResultDescriptor = Stub.getDefaultImpl().getEventsWithToken(continuationToken, callingPackage);
                            return rcsEventQueryResultDescriptor;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        rcsEventQueryResultDescriptor = (RcsEventQueryResultDescriptor) RcsEventQueryResultDescriptor.CREATOR.createFromParcel(_reply);
                    } else {
                        rcsEventQueryResultDescriptor = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return rcsEventQueryResultDescriptor;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean deleteThread(int threadId, int threadType, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(threadId);
                    _data.writeInt(threadType);
                    _data.writeString(callingPackage);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().deleteThread(threadId, threadType, callingPackage);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int createRcs1To1Thread(int participantId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(participantId);
                    _data.writeString(callingPackage);
                    int i = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().createRcs1To1Thread(participantId, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int createGroupThread(int[] participantIds, String groupName, Uri groupIcon, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(participantIds);
                    _data.writeString(groupName);
                    if (groupIcon != null) {
                        _data.writeInt(1);
                        groupIcon.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    int i = this.mRemote;
                    if (!i.transact(11, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().createGroupThread(participantIds, groupName, groupIcon, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int addIncomingMessage(int rcsThreadId, RcsIncomingMessageCreationParams rcsIncomingMessageCreationParams, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rcsThreadId);
                    if (rcsIncomingMessageCreationParams != null) {
                        _data.writeInt(1);
                        rcsIncomingMessageCreationParams.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    int i = this.mRemote;
                    if (!i.transact(12, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().addIncomingMessage(rcsThreadId, rcsIncomingMessageCreationParams, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int addOutgoingMessage(int rcsThreadId, RcsOutgoingMessageCreationParams rcsOutgoingMessageCreationParams, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rcsThreadId);
                    if (rcsOutgoingMessageCreationParams != null) {
                        _data.writeInt(1);
                        rcsOutgoingMessageCreationParams.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    int i = this.mRemote;
                    if (!i.transact(13, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().addOutgoingMessage(rcsThreadId, rcsOutgoingMessageCreationParams, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deleteMessage(int rcsMessageId, boolean isIncoming, int rcsThreadId, boolean isGroup, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rcsMessageId);
                    int i = 1;
                    _data.writeInt(isIncoming ? 1 : 0);
                    _data.writeInt(rcsThreadId);
                    if (!isGroup) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deleteMessage(rcsMessageId, isIncoming, rcsThreadId, isGroup, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public RcsMessageSnippet getMessageSnippet(int rcsThreadId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rcsThreadId);
                    _data.writeString(callingPackage);
                    RcsMessageSnippet rcsMessageSnippet = 15;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        rcsMessageSnippet = Stub.getDefaultImpl();
                        if (rcsMessageSnippet != 0) {
                            rcsMessageSnippet = Stub.getDefaultImpl().getMessageSnippet(rcsThreadId, callingPackage);
                            return rcsMessageSnippet;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        rcsMessageSnippet = (RcsMessageSnippet) RcsMessageSnippet.CREATOR.createFromParcel(_reply);
                    } else {
                        rcsMessageSnippet = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return rcsMessageSnippet;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void set1To1ThreadFallbackThreadId(int rcsThreadId, long fallbackId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rcsThreadId);
                    _data.writeLong(fallbackId);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().set1To1ThreadFallbackThreadId(rcsThreadId, fallbackId, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long get1To1ThreadFallbackThreadId(int rcsThreadId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rcsThreadId);
                    _data.writeString(callingPackage);
                    long j = 17;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().get1To1ThreadFallbackThreadId(rcsThreadId, callingPackage);
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int get1To1ThreadOtherParticipantId(int rcsThreadId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rcsThreadId);
                    _data.writeString(callingPackage);
                    int i = 18;
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().get1To1ThreadOtherParticipantId(rcsThreadId, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setGroupThreadName(int rcsThreadId, String groupName, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rcsThreadId);
                    _data.writeString(groupName);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setGroupThreadName(rcsThreadId, groupName, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getGroupThreadName(int rcsThreadId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rcsThreadId);
                    _data.writeString(callingPackage);
                    String str = 20;
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getGroupThreadName(rcsThreadId, callingPackage);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setGroupThreadIcon(int rcsThreadId, Uri groupIcon, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rcsThreadId);
                    if (groupIcon != null) {
                        _data.writeInt(1);
                        groupIcon.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setGroupThreadIcon(rcsThreadId, groupIcon, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Uri getGroupThreadIcon(int rcsThreadId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rcsThreadId);
                    _data.writeString(callingPackage);
                    Uri uri = 22;
                    if (!this.mRemote.transact(22, _data, _reply, 0)) {
                        uri = Stub.getDefaultImpl();
                        if (uri != 0) {
                            uri = Stub.getDefaultImpl().getGroupThreadIcon(rcsThreadId, callingPackage);
                            return uri;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        uri = (Uri) Uri.CREATOR.createFromParcel(_reply);
                    } else {
                        uri = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return uri;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setGroupThreadOwner(int rcsThreadId, int participantId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rcsThreadId);
                    _data.writeInt(participantId);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setGroupThreadOwner(rcsThreadId, participantId, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getGroupThreadOwner(int rcsThreadId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rcsThreadId);
                    _data.writeString(callingPackage);
                    int i = 24;
                    if (!this.mRemote.transact(24, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getGroupThreadOwner(rcsThreadId, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setGroupThreadConferenceUri(int rcsThreadId, Uri conferenceUri, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rcsThreadId);
                    if (conferenceUri != null) {
                        _data.writeInt(1);
                        conferenceUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setGroupThreadConferenceUri(rcsThreadId, conferenceUri, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Uri getGroupThreadConferenceUri(int rcsThreadId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rcsThreadId);
                    _data.writeString(callingPackage);
                    Uri uri = 26;
                    if (!this.mRemote.transact(26, _data, _reply, 0)) {
                        uri = Stub.getDefaultImpl();
                        if (uri != 0) {
                            uri = Stub.getDefaultImpl().getGroupThreadConferenceUri(rcsThreadId, callingPackage);
                            return uri;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        uri = (Uri) Uri.CREATOR.createFromParcel(_reply);
                    } else {
                        uri = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return uri;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addParticipantToGroupThread(int rcsThreadId, int participantId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rcsThreadId);
                    _data.writeInt(participantId);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(27, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addParticipantToGroupThread(rcsThreadId, participantId, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeParticipantFromGroupThread(int rcsThreadId, int participantId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rcsThreadId);
                    _data.writeInt(participantId);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeParticipantFromGroupThread(rcsThreadId, participantId, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int createRcsParticipant(String canonicalAddress, String alias, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(canonicalAddress);
                    _data.writeString(alias);
                    _data.writeString(callingPackage);
                    int i = 29;
                    if (!this.mRemote.transact(29, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().createRcsParticipant(canonicalAddress, alias, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getRcsParticipantCanonicalAddress(int participantId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(participantId);
                    _data.writeString(callingPackage);
                    String str = 30;
                    if (!this.mRemote.transact(30, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getRcsParticipantCanonicalAddress(participantId, callingPackage);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getRcsParticipantAlias(int participantId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(participantId);
                    _data.writeString(callingPackage);
                    String str = 31;
                    if (!this.mRemote.transact(31, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getRcsParticipantAlias(participantId, callingPackage);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setRcsParticipantAlias(int id, String alias, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(id);
                    _data.writeString(alias);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(32, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setRcsParticipantAlias(id, alias, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getRcsParticipantContactId(int participantId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(participantId);
                    _data.writeString(callingPackage);
                    String str = 33;
                    if (!this.mRemote.transact(33, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getRcsParticipantContactId(participantId, callingPackage);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setRcsParticipantContactId(int participantId, String contactId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(participantId);
                    _data.writeString(contactId);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(34, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setRcsParticipantContactId(participantId, contactId, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMessageSubId(int messageId, boolean isIncoming, int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(isIncoming ? 1 : 0);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(35, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setMessageSubId(messageId, isIncoming, subId, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getMessageSubId(int messageId, boolean isIncoming, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(isIncoming ? 1 : 0);
                    _data.writeString(callingPackage);
                    int i = this.mRemote;
                    if (!i.transact(36, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getMessageSubId(messageId, isIncoming, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMessageStatus(int messageId, boolean isIncoming, int status, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(isIncoming ? 1 : 0);
                    _data.writeInt(status);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(37, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setMessageStatus(messageId, isIncoming, status, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getMessageStatus(int messageId, boolean isIncoming, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(isIncoming ? 1 : 0);
                    _data.writeString(callingPackage);
                    int i = this.mRemote;
                    if (!i.transact(38, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getMessageStatus(messageId, isIncoming, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMessageOriginationTimestamp(int messageId, boolean isIncoming, long originationTimestamp, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(isIncoming ? 1 : 0);
                    _data.writeLong(originationTimestamp);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(39, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setMessageOriginationTimestamp(messageId, isIncoming, originationTimestamp, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getMessageOriginationTimestamp(int messageId, boolean isIncoming, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(isIncoming ? 1 : 0);
                    _data.writeString(callingPackage);
                    long j = this.mRemote;
                    if (!j.transact(40, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != null) {
                            j = Stub.getDefaultImpl().getMessageOriginationTimestamp(messageId, isIncoming, callingPackage);
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setGlobalMessageIdForMessage(int messageId, boolean isIncoming, String globalId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(isIncoming ? 1 : 0);
                    _data.writeString(globalId);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(41, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setGlobalMessageIdForMessage(messageId, isIncoming, globalId, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getGlobalMessageIdForMessage(int messageId, boolean isIncoming, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(isIncoming ? 1 : 0);
                    _data.writeString(callingPackage);
                    String str = this.mRemote;
                    if (!str.transact(42, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != null) {
                            str = Stub.getDefaultImpl().getGlobalMessageIdForMessage(messageId, isIncoming, callingPackage);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMessageArrivalTimestamp(int messageId, boolean isIncoming, long arrivalTimestamp, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(isIncoming ? 1 : 0);
                    _data.writeLong(arrivalTimestamp);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(43, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setMessageArrivalTimestamp(messageId, isIncoming, arrivalTimestamp, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getMessageArrivalTimestamp(int messageId, boolean isIncoming, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(isIncoming ? 1 : 0);
                    _data.writeString(callingPackage);
                    long j = this.mRemote;
                    if (!j.transact(44, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != null) {
                            j = Stub.getDefaultImpl().getMessageArrivalTimestamp(messageId, isIncoming, callingPackage);
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMessageSeenTimestamp(int messageId, boolean isIncoming, long seenTimestamp, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(isIncoming ? 1 : 0);
                    _data.writeLong(seenTimestamp);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(45, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setMessageSeenTimestamp(messageId, isIncoming, seenTimestamp, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getMessageSeenTimestamp(int messageId, boolean isIncoming, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(isIncoming ? 1 : 0);
                    _data.writeString(callingPackage);
                    long j = this.mRemote;
                    if (!j.transact(46, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != null) {
                            j = Stub.getDefaultImpl().getMessageSeenTimestamp(messageId, isIncoming, callingPackage);
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setTextForMessage(int messageId, boolean isIncoming, String text, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(isIncoming ? 1 : 0);
                    _data.writeString(text);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(47, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setTextForMessage(messageId, isIncoming, text, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getTextForMessage(int messageId, boolean isIncoming, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(isIncoming ? 1 : 0);
                    _data.writeString(callingPackage);
                    String str = this.mRemote;
                    if (!str.transact(48, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != null) {
                            str = Stub.getDefaultImpl().getTextForMessage(messageId, isIncoming, callingPackage);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setLatitudeForMessage(int messageId, boolean isIncoming, double latitude, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(isIncoming ? 1 : 0);
                    _data.writeDouble(latitude);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(49, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setLatitudeForMessage(messageId, isIncoming, latitude, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public double getLatitudeForMessage(int messageId, boolean isIncoming, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(isIncoming ? 1 : 0);
                    _data.writeString(callingPackage);
                    double d = this.mRemote;
                    if (!d.transact(50, _data, _reply, 0)) {
                        d = Stub.getDefaultImpl();
                        if (d != null) {
                            d = Stub.getDefaultImpl().getLatitudeForMessage(messageId, isIncoming, callingPackage);
                            return d;
                        }
                    }
                    _reply.readException();
                    d = _reply.readDouble();
                    double _result = d;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setLongitudeForMessage(int messageId, boolean isIncoming, double longitude, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(isIncoming ? 1 : 0);
                    _data.writeDouble(longitude);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(51, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setLongitudeForMessage(messageId, isIncoming, longitude, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public double getLongitudeForMessage(int messageId, boolean isIncoming, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(isIncoming ? 1 : 0);
                    _data.writeString(callingPackage);
                    double d = this.mRemote;
                    if (!d.transact(52, _data, _reply, 0)) {
                        d = Stub.getDefaultImpl();
                        if (d != null) {
                            d = Stub.getDefaultImpl().getLongitudeForMessage(messageId, isIncoming, callingPackage);
                            return d;
                        }
                    }
                    _reply.readException();
                    d = _reply.readDouble();
                    double _result = d;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getFileTransfersAttachedToMessage(int messageId, boolean isIncoming, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(isIncoming ? 1 : 0);
                    _data.writeString(callingPackage);
                    int[] iArr = this.mRemote;
                    if (!iArr.transact(53, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != null) {
                            iArr = Stub.getDefaultImpl().getFileTransfersAttachedToMessage(messageId, isIncoming, callingPackage);
                            return iArr;
                        }
                    }
                    _reply.readException();
                    iArr = _reply.createIntArray();
                    int[] _result = iArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSenderParticipant(int messageId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeString(callingPackage);
                    int i = 54;
                    if (!this.mRemote.transact(54, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getSenderParticipant(messageId, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getMessageRecipients(int messageId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeString(callingPackage);
                    int[] iArr = 55;
                    if (!this.mRemote.transact(55, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != 0) {
                            iArr = Stub.getDefaultImpl().getMessageRecipients(messageId, callingPackage);
                            return iArr;
                        }
                    }
                    _reply.readException();
                    iArr = _reply.createIntArray();
                    int[] _result = iArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getOutgoingDeliveryDeliveredTimestamp(int messageId, int participantId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(participantId);
                    _data.writeString(callingPackage);
                    long j = 56;
                    if (!this.mRemote.transact(56, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getOutgoingDeliveryDeliveredTimestamp(messageId, participantId, callingPackage);
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setOutgoingDeliveryDeliveredTimestamp(int messageId, int participantId, long deliveredTimestamp, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(participantId);
                    _data.writeLong(deliveredTimestamp);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(57, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setOutgoingDeliveryDeliveredTimestamp(messageId, participantId, deliveredTimestamp, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getOutgoingDeliverySeenTimestamp(int messageId, int participantId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(participantId);
                    _data.writeString(callingPackage);
                    long j = 58;
                    if (!this.mRemote.transact(58, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getOutgoingDeliverySeenTimestamp(messageId, participantId, callingPackage);
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setOutgoingDeliverySeenTimestamp(int messageId, int participantId, long seenTimestamp, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(participantId);
                    _data.writeLong(seenTimestamp);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(59, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setOutgoingDeliverySeenTimestamp(messageId, participantId, seenTimestamp, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getOutgoingDeliveryStatus(int messageId, int participantId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(participantId);
                    _data.writeString(callingPackage);
                    int i = 60;
                    if (!this.mRemote.transact(60, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getOutgoingDeliveryStatus(messageId, participantId, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setOutgoingDeliveryStatus(int messageId, int participantId, int status, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(participantId);
                    _data.writeInt(status);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(61, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setOutgoingDeliveryStatus(messageId, participantId, status, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int storeFileTransfer(int messageId, boolean isIncoming, RcsFileTransferCreationParams fileTransferCreationParams, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    int i = 0;
                    _data.writeInt(isIncoming ? 1 : 0);
                    if (fileTransferCreationParams != null) {
                        _data.writeInt(1);
                        fileTransferCreationParams.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    if (!this.mRemote.transact(62, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().storeFileTransfer(messageId, isIncoming, fileTransferCreationParams, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deleteFileTransfer(int partId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(partId);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(63, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deleteFileTransfer(partId, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFileTransferSessionId(int partId, String sessionId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(partId);
                    _data.writeString(sessionId);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(64, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setFileTransferSessionId(partId, sessionId, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getFileTransferSessionId(int partId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(partId);
                    _data.writeString(callingPackage);
                    String str = 65;
                    if (!this.mRemote.transact(65, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getFileTransferSessionId(partId, callingPackage);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFileTransferContentUri(int partId, Uri contentUri, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(partId);
                    if (contentUri != null) {
                        _data.writeInt(1);
                        contentUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(66, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setFileTransferContentUri(partId, contentUri, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Uri getFileTransferContentUri(int partId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(partId);
                    _data.writeString(callingPackage);
                    Uri uri = 67;
                    if (!this.mRemote.transact(67, _data, _reply, 0)) {
                        uri = Stub.getDefaultImpl();
                        if (uri != 0) {
                            uri = Stub.getDefaultImpl().getFileTransferContentUri(partId, callingPackage);
                            return uri;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        uri = (Uri) Uri.CREATOR.createFromParcel(_reply);
                    } else {
                        uri = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return uri;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFileTransferContentType(int partId, String contentType, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(partId);
                    _data.writeString(contentType);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(68, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setFileTransferContentType(partId, contentType, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getFileTransferContentType(int partId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(partId);
                    _data.writeString(callingPackage);
                    String str = 69;
                    if (!this.mRemote.transact(69, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getFileTransferContentType(partId, callingPackage);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFileTransferFileSize(int partId, long fileSize, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(partId);
                    _data.writeLong(fileSize);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(70, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setFileTransferFileSize(partId, fileSize, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getFileTransferFileSize(int partId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(partId);
                    _data.writeString(callingPackage);
                    long j = 71;
                    if (!this.mRemote.transact(71, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getFileTransferFileSize(partId, callingPackage);
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFileTransferTransferOffset(int partId, long transferOffset, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(partId);
                    _data.writeLong(transferOffset);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(72, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setFileTransferTransferOffset(partId, transferOffset, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getFileTransferTransferOffset(int partId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(partId);
                    _data.writeString(callingPackage);
                    long j = 73;
                    if (!this.mRemote.transact(73, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getFileTransferTransferOffset(partId, callingPackage);
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFileTransferStatus(int partId, int transferStatus, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(partId);
                    _data.writeInt(transferStatus);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(74, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setFileTransferStatus(partId, transferStatus, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getFileTransferStatus(int partId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(partId);
                    _data.writeString(callingPackage);
                    int i = 75;
                    if (!this.mRemote.transact(75, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getFileTransferStatus(partId, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFileTransferWidth(int partId, int width, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(partId);
                    _data.writeInt(width);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(76, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setFileTransferWidth(partId, width, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getFileTransferWidth(int partId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(partId);
                    _data.writeString(callingPackage);
                    int i = 77;
                    if (!this.mRemote.transact(77, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getFileTransferWidth(partId, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFileTransferHeight(int partId, int height, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(partId);
                    _data.writeInt(height);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(78, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setFileTransferHeight(partId, height, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getFileTransferHeight(int partId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(partId);
                    _data.writeString(callingPackage);
                    int i = 79;
                    if (!this.mRemote.transact(79, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getFileTransferHeight(partId, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFileTransferLength(int partId, long length, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(partId);
                    _data.writeLong(length);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(80, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setFileTransferLength(partId, length, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getFileTransferLength(int partId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(partId);
                    _data.writeString(callingPackage);
                    long j = 81;
                    if (!this.mRemote.transact(81, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getFileTransferLength(partId, callingPackage);
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFileTransferPreviewUri(int partId, Uri uri, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(partId);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(82, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setFileTransferPreviewUri(partId, uri, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Uri getFileTransferPreviewUri(int partId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(partId);
                    _data.writeString(callingPackage);
                    Uri uri = 83;
                    if (!this.mRemote.transact(83, _data, _reply, 0)) {
                        uri = Stub.getDefaultImpl();
                        if (uri != 0) {
                            uri = Stub.getDefaultImpl().getFileTransferPreviewUri(partId, callingPackage);
                            return uri;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        uri = (Uri) Uri.CREATOR.createFromParcel(_reply);
                    } else {
                        uri = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return uri;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFileTransferPreviewType(int partId, String type, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(partId);
                    _data.writeString(type);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(84, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setFileTransferPreviewType(partId, type, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getFileTransferPreviewType(int partId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(partId);
                    _data.writeString(callingPackage);
                    String str = 85;
                    if (!this.mRemote.transact(85, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getFileTransferPreviewType(partId, callingPackage);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int createGroupThreadNameChangedEvent(long timestamp, int threadId, int originationParticipantId, String newName, String callingPackage) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                String str;
                String str2;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeLong(timestamp);
                    } catch (Throwable th2) {
                        th = th2;
                        i = threadId;
                        i2 = originationParticipantId;
                        str = newName;
                        str2 = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(threadId);
                    } catch (Throwable th3) {
                        th = th3;
                        i2 = originationParticipantId;
                        str = newName;
                        str2 = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(originationParticipantId);
                        try {
                            _data.writeString(newName);
                        } catch (Throwable th4) {
                            th = th4;
                            str2 = callingPackage;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeString(callingPackage);
                            if (this.mRemote.transact(86, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                int _result = _reply.readInt();
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            int createGroupThreadNameChangedEvent = Stub.getDefaultImpl().createGroupThreadNameChangedEvent(timestamp, threadId, originationParticipantId, newName, callingPackage);
                            _reply.recycle();
                            _data.recycle();
                            return createGroupThreadNameChangedEvent;
                        } catch (Throwable th5) {
                            th = th5;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        str = newName;
                        str2 = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    long j = timestamp;
                    i = threadId;
                    i2 = originationParticipantId;
                    str = newName;
                    str2 = callingPackage;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public int createGroupThreadIconChangedEvent(long timestamp, int threadId, int originationParticipantId, Uri newIcon, String callingPackage) throws RemoteException {
                Throwable th;
                int i;
                String str;
                int i2;
                Uri uri = newIcon;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeLong(timestamp);
                        try {
                            _data.writeInt(threadId);
                        } catch (Throwable th2) {
                            th = th2;
                            i = originationParticipantId;
                            str = callingPackage;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(originationParticipantId);
                            if (uri != null) {
                                _data.writeInt(1);
                                uri.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            try {
                                _data.writeString(callingPackage);
                                if (this.mRemote.transact(87, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                    _reply.readException();
                                    int _result = _reply.readInt();
                                    _reply.recycle();
                                    _data.recycle();
                                    return _result;
                                }
                                int createGroupThreadIconChangedEvent = Stub.getDefaultImpl().createGroupThreadIconChangedEvent(timestamp, threadId, originationParticipantId, newIcon, callingPackage);
                                _reply.recycle();
                                _data.recycle();
                                return createGroupThreadIconChangedEvent;
                            } catch (Throwable th3) {
                                th = th3;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            str = callingPackage;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        i2 = threadId;
                        i = originationParticipantId;
                        str = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    long j = timestamp;
                    i2 = threadId;
                    i = originationParticipantId;
                    str = callingPackage;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public int createGroupThreadParticipantJoinedEvent(long timestamp, int threadId, int originationParticipantId, int participantId, String callingPackage) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                int i3;
                String str;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeLong(timestamp);
                    } catch (Throwable th2) {
                        th = th2;
                        i = threadId;
                        i2 = originationParticipantId;
                        i3 = participantId;
                        str = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(threadId);
                    } catch (Throwable th3) {
                        th = th3;
                        i2 = originationParticipantId;
                        i3 = participantId;
                        str = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(originationParticipantId);
                        try {
                            _data.writeInt(participantId);
                        } catch (Throwable th4) {
                            th = th4;
                            str = callingPackage;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeString(callingPackage);
                            if (this.mRemote.transact(88, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                int _result = _reply.readInt();
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            int createGroupThreadParticipantJoinedEvent = Stub.getDefaultImpl().createGroupThreadParticipantJoinedEvent(timestamp, threadId, originationParticipantId, participantId, callingPackage);
                            _reply.recycle();
                            _data.recycle();
                            return createGroupThreadParticipantJoinedEvent;
                        } catch (Throwable th5) {
                            th = th5;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        i3 = participantId;
                        str = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    long j = timestamp;
                    i = threadId;
                    i2 = originationParticipantId;
                    i3 = participantId;
                    str = callingPackage;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public int createGroupThreadParticipantLeftEvent(long timestamp, int threadId, int originationParticipantId, int participantId, String callingPackage) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                int i3;
                String str;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeLong(timestamp);
                    } catch (Throwable th2) {
                        th = th2;
                        i = threadId;
                        i2 = originationParticipantId;
                        i3 = participantId;
                        str = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(threadId);
                    } catch (Throwable th3) {
                        th = th3;
                        i2 = originationParticipantId;
                        i3 = participantId;
                        str = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(originationParticipantId);
                        try {
                            _data.writeInt(participantId);
                        } catch (Throwable th4) {
                            th = th4;
                            str = callingPackage;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeString(callingPackage);
                            if (this.mRemote.transact(89, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                int _result = _reply.readInt();
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            int createGroupThreadParticipantLeftEvent = Stub.getDefaultImpl().createGroupThreadParticipantLeftEvent(timestamp, threadId, originationParticipantId, participantId, callingPackage);
                            _reply.recycle();
                            _data.recycle();
                            return createGroupThreadParticipantLeftEvent;
                        } catch (Throwable th5) {
                            th = th5;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        i3 = participantId;
                        str = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    long j = timestamp;
                    i = threadId;
                    i2 = originationParticipantId;
                    i3 = participantId;
                    str = callingPackage;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public int createParticipantAliasChangedEvent(long timestamp, int participantId, String newAlias, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(timestamp);
                    _data.writeInt(participantId);
                    _data.writeString(newAlias);
                    _data.writeString(callingPackage);
                    int i = 90;
                    if (!this.mRemote.transact(90, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().createParticipantAliasChangedEvent(timestamp, participantId, newAlias, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRcs asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IRcs)) {
                return new Proxy(obj);
            }
            return (IRcs) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getRcsThreads";
                case 2:
                    return "getRcsThreadsWithToken";
                case 3:
                    return "getParticipants";
                case 4:
                    return "getParticipantsWithToken";
                case 5:
                    return "getMessages";
                case 6:
                    return "getMessagesWithToken";
                case 7:
                    return "getEvents";
                case 8:
                    return "getEventsWithToken";
                case 9:
                    return "deleteThread";
                case 10:
                    return "createRcs1To1Thread";
                case 11:
                    return "createGroupThread";
                case 12:
                    return "addIncomingMessage";
                case 13:
                    return "addOutgoingMessage";
                case 14:
                    return "deleteMessage";
                case 15:
                    return "getMessageSnippet";
                case 16:
                    return "set1To1ThreadFallbackThreadId";
                case 17:
                    return "get1To1ThreadFallbackThreadId";
                case 18:
                    return "get1To1ThreadOtherParticipantId";
                case 19:
                    return "setGroupThreadName";
                case 20:
                    return "getGroupThreadName";
                case 21:
                    return "setGroupThreadIcon";
                case 22:
                    return "getGroupThreadIcon";
                case 23:
                    return "setGroupThreadOwner";
                case 24:
                    return "getGroupThreadOwner";
                case 25:
                    return "setGroupThreadConferenceUri";
                case 26:
                    return "getGroupThreadConferenceUri";
                case 27:
                    return "addParticipantToGroupThread";
                case 28:
                    return "removeParticipantFromGroupThread";
                case 29:
                    return "createRcsParticipant";
                case 30:
                    return "getRcsParticipantCanonicalAddress";
                case 31:
                    return "getRcsParticipantAlias";
                case 32:
                    return "setRcsParticipantAlias";
                case 33:
                    return "getRcsParticipantContactId";
                case 34:
                    return "setRcsParticipantContactId";
                case 35:
                    return "setMessageSubId";
                case 36:
                    return "getMessageSubId";
                case 37:
                    return "setMessageStatus";
                case 38:
                    return "getMessageStatus";
                case 39:
                    return "setMessageOriginationTimestamp";
                case 40:
                    return "getMessageOriginationTimestamp";
                case 41:
                    return "setGlobalMessageIdForMessage";
                case 42:
                    return "getGlobalMessageIdForMessage";
                case 43:
                    return "setMessageArrivalTimestamp";
                case 44:
                    return "getMessageArrivalTimestamp";
                case 45:
                    return "setMessageSeenTimestamp";
                case 46:
                    return "getMessageSeenTimestamp";
                case 47:
                    return "setTextForMessage";
                case 48:
                    return "getTextForMessage";
                case 49:
                    return "setLatitudeForMessage";
                case 50:
                    return "getLatitudeForMessage";
                case 51:
                    return "setLongitudeForMessage";
                case 52:
                    return "getLongitudeForMessage";
                case 53:
                    return "getFileTransfersAttachedToMessage";
                case 54:
                    return "getSenderParticipant";
                case 55:
                    return "getMessageRecipients";
                case 56:
                    return "getOutgoingDeliveryDeliveredTimestamp";
                case 57:
                    return "setOutgoingDeliveryDeliveredTimestamp";
                case 58:
                    return "getOutgoingDeliverySeenTimestamp";
                case 59:
                    return "setOutgoingDeliverySeenTimestamp";
                case 60:
                    return "getOutgoingDeliveryStatus";
                case 61:
                    return "setOutgoingDeliveryStatus";
                case 62:
                    return "storeFileTransfer";
                case 63:
                    return "deleteFileTransfer";
                case 64:
                    return "setFileTransferSessionId";
                case 65:
                    return "getFileTransferSessionId";
                case 66:
                    return "setFileTransferContentUri";
                case 67:
                    return "getFileTransferContentUri";
                case 68:
                    return "setFileTransferContentType";
                case 69:
                    return "getFileTransferContentType";
                case 70:
                    return "setFileTransferFileSize";
                case 71:
                    return "getFileTransferFileSize";
                case 72:
                    return "setFileTransferTransferOffset";
                case 73:
                    return "getFileTransferTransferOffset";
                case 74:
                    return "setFileTransferStatus";
                case 75:
                    return "getFileTransferStatus";
                case 76:
                    return "setFileTransferWidth";
                case 77:
                    return "getFileTransferWidth";
                case 78:
                    return "setFileTransferHeight";
                case 79:
                    return "getFileTransferHeight";
                case 80:
                    return "setFileTransferLength";
                case 81:
                    return "getFileTransferLength";
                case 82:
                    return "setFileTransferPreviewUri";
                case 83:
                    return "getFileTransferPreviewUri";
                case 84:
                    return "setFileTransferPreviewType";
                case 85:
                    return "getFileTransferPreviewType";
                case 86:
                    return "createGroupThreadNameChangedEvent";
                case 87:
                    return "createGroupThreadIconChangedEvent";
                case 88:
                    return "createGroupThreadParticipantJoinedEvent";
                case 89:
                    return "createGroupThreadParticipantLeftEvent";
                case 90:
                    return "createParticipantAliasChangedEvent";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            if (i != 1598968902) {
                boolean _arg1 = false;
                RcsThreadQueryResultParcelable _result;
                RcsQueryContinuationToken _arg0;
                RcsParticipantQueryResultParcelable _result2;
                RcsMessageQueryResultParcelable _result3;
                RcsEventQueryResultDescriptor _result4;
                int _result5;
                int _result6;
                int _arg02;
                int _result7;
                long _result8;
                String _result9;
                Uri _arg12;
                Uri _result10;
                int _arg03;
                long _result11;
                String _result12;
                double _result13;
                switch (i) {
                    case 1:
                        RcsThreadQueryParams _arg04;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (RcsThreadQueryParams) RcsThreadQueryParams.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        _result = getRcsThreads(_arg04, data.readString());
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(1);
                            _result.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (RcsQueryContinuationToken) RcsQueryContinuationToken.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result = getRcsThreadsWithToken(_arg0, data.readString());
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(1);
                            _result.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 3:
                        RcsParticipantQueryParams _arg05;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (RcsParticipantQueryParams) RcsParticipantQueryParams.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg05 = null;
                        }
                        _result2 = getParticipants(_arg05, data.readString());
                        reply.writeNoException();
                        if (_result2 != null) {
                            parcel2.writeInt(1);
                            _result2.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (RcsQueryContinuationToken) RcsQueryContinuationToken.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result2 = getParticipantsWithToken(_arg0, data.readString());
                        reply.writeNoException();
                        if (_result2 != null) {
                            parcel2.writeInt(1);
                            _result2.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 5:
                        RcsMessageQueryParams _arg06;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg06 = (RcsMessageQueryParams) RcsMessageQueryParams.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg06 = null;
                        }
                        _result3 = getMessages(_arg06, data.readString());
                        reply.writeNoException();
                        if (_result3 != null) {
                            parcel2.writeInt(1);
                            _result3.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (RcsQueryContinuationToken) RcsQueryContinuationToken.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result3 = getMessagesWithToken(_arg0, data.readString());
                        reply.writeNoException();
                        if (_result3 != null) {
                            parcel2.writeInt(1);
                            _result3.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 7:
                        RcsEventQueryParams _arg07;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg07 = (RcsEventQueryParams) RcsEventQueryParams.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg07 = null;
                        }
                        _result4 = getEvents(_arg07, data.readString());
                        reply.writeNoException();
                        if (_result4 != null) {
                            parcel2.writeInt(1);
                            _result4.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (RcsQueryContinuationToken) RcsQueryContinuationToken.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result4 = getEventsWithToken(_arg0, data.readString());
                        reply.writeNoException();
                        if (_result4 != null) {
                            parcel2.writeInt(1);
                            _result4.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        boolean _result14 = deleteThread(data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result14);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        _result5 = createRcs1To1Thread(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 11:
                        Uri _arg2;
                        parcel.enforceInterface(descriptor);
                        int[] _arg08 = data.createIntArray();
                        String _arg13 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        _result6 = createGroupThread(_arg08, _arg13, _arg2, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 12:
                        RcsIncomingMessageCreationParams _arg14;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg14 = (RcsIncomingMessageCreationParams) RcsIncomingMessageCreationParams.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        _result7 = addIncomingMessage(_arg02, _arg14, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 13:
                        RcsOutgoingMessageCreationParams _arg15;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg15 = (RcsOutgoingMessageCreationParams) RcsOutgoingMessageCreationParams.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg15 = null;
                        }
                        _result7 = addOutgoingMessage(_arg02, _arg15, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        deleteMessage(data.readInt(), data.readInt() != 0, data.readInt(), data.readInt() != 0, data.readString());
                        reply.writeNoException();
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        RcsMessageSnippet _result15 = getMessageSnippet(data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result15 != null) {
                            parcel2.writeInt(1);
                            _result15.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        set1To1ThreadFallbackThreadId(data.readInt(), data.readLong(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        _result8 = get1To1ThreadFallbackThreadId(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeLong(_result8);
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        _result5 = get1To1ThreadOtherParticipantId(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        setGroupThreadName(data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        _result9 = getGroupThreadName(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result9);
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg12 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        setGroupThreadIcon(_arg02, _arg12, data.readString());
                        reply.writeNoException();
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        _result10 = getGroupThreadIcon(data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result10 != null) {
                            parcel2.writeInt(1);
                            _result10.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        setGroupThreadOwner(data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        _result5 = getGroupThreadOwner(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg12 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        setGroupThreadConferenceUri(_arg02, _arg12, data.readString());
                        reply.writeNoException();
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        _result10 = getGroupThreadConferenceUri(data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result10 != null) {
                            parcel2.writeInt(1);
                            _result10.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        addParticipantToGroupThread(data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        removeParticipantFromGroupThread(data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        _result7 = createRcsParticipant(data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        _result9 = getRcsParticipantCanonicalAddress(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result9);
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        _result9 = getRcsParticipantAlias(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result9);
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        setRcsParticipantAlias(data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        _result9 = getRcsParticipantContactId(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result9);
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        setRcsParticipantContactId(data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 35:
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setMessageSubId(_arg03, _arg1, data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result7 = getMessageSubId(_arg03, _arg1, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setMessageStatus(_arg03, _arg1, data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 38:
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result7 = getMessageStatus(_arg03, _arg1, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 39:
                        parcel.enforceInterface(descriptor);
                        setMessageOriginationTimestamp(data.readInt(), data.readInt() != 0, data.readLong(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 40:
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result11 = getMessageOriginationTimestamp(_arg03, _arg1, data.readString());
                        reply.writeNoException();
                        parcel2.writeLong(_result11);
                        return true;
                    case 41:
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setGlobalMessageIdForMessage(_arg03, _arg1, data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 42:
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result12 = getGlobalMessageIdForMessage(_arg03, _arg1, data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result12);
                        return true;
                    case 43:
                        parcel.enforceInterface(descriptor);
                        setMessageArrivalTimestamp(data.readInt(), data.readInt() != 0, data.readLong(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 44:
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result11 = getMessageArrivalTimestamp(_arg03, _arg1, data.readString());
                        reply.writeNoException();
                        parcel2.writeLong(_result11);
                        return true;
                    case 45:
                        parcel.enforceInterface(descriptor);
                        setMessageSeenTimestamp(data.readInt(), data.readInt() != 0, data.readLong(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 46:
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result11 = getMessageSeenTimestamp(_arg03, _arg1, data.readString());
                        reply.writeNoException();
                        parcel2.writeLong(_result11);
                        return true;
                    case 47:
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setTextForMessage(_arg03, _arg1, data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 48:
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result12 = getTextForMessage(_arg03, _arg1, data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result12);
                        return true;
                    case 49:
                        parcel.enforceInterface(descriptor);
                        setLatitudeForMessage(data.readInt(), data.readInt() != 0, data.readDouble(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 50:
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result13 = getLatitudeForMessage(_arg03, _arg1, data.readString());
                        reply.writeNoException();
                        parcel2.writeDouble(_result13);
                        return true;
                    case 51:
                        parcel.enforceInterface(descriptor);
                        setLongitudeForMessage(data.readInt(), data.readInt() != 0, data.readDouble(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 52:
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result13 = getLongitudeForMessage(_arg03, _arg1, data.readString());
                        reply.writeNoException();
                        parcel2.writeDouble(_result13);
                        return true;
                    case 53:
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        int[] _result16 = getFileTransfersAttachedToMessage(_arg03, _arg1, data.readString());
                        reply.writeNoException();
                        parcel2.writeIntArray(_result16);
                        return true;
                    case 54:
                        parcel.enforceInterface(descriptor);
                        _result5 = getSenderParticipant(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 55:
                        parcel.enforceInterface(descriptor);
                        int[] _result17 = getMessageRecipients(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeIntArray(_result17);
                        return true;
                    case 56:
                        parcel.enforceInterface(descriptor);
                        _result11 = getOutgoingDeliveryDeliveredTimestamp(data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeLong(_result11);
                        return true;
                    case 57:
                        parcel.enforceInterface(descriptor);
                        setOutgoingDeliveryDeliveredTimestamp(data.readInt(), data.readInt(), data.readLong(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 58:
                        parcel.enforceInterface(descriptor);
                        _result11 = getOutgoingDeliverySeenTimestamp(data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeLong(_result11);
                        return true;
                    case 59:
                        parcel.enforceInterface(descriptor);
                        setOutgoingDeliverySeenTimestamp(data.readInt(), data.readInt(), data.readLong(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 60:
                        parcel.enforceInterface(descriptor);
                        _result7 = getOutgoingDeliveryStatus(data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 61:
                        parcel.enforceInterface(descriptor);
                        setOutgoingDeliveryStatus(data.readInt(), data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 62:
                        RcsFileTransferCreationParams _arg22;
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        if (data.readInt() != 0) {
                            _arg22 = (RcsFileTransferCreationParams) RcsFileTransferCreationParams.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        _result6 = storeFileTransfer(_arg03, _arg1, _arg22, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 63:
                        parcel.enforceInterface(descriptor);
                        deleteFileTransfer(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 64:
                        parcel.enforceInterface(descriptor);
                        setFileTransferSessionId(data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 65:
                        parcel.enforceInterface(descriptor);
                        _result9 = getFileTransferSessionId(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result9);
                        return true;
                    case 66:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg12 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        setFileTransferContentUri(_arg02, _arg12, data.readString());
                        reply.writeNoException();
                        return true;
                    case 67:
                        parcel.enforceInterface(descriptor);
                        _result10 = getFileTransferContentUri(data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result10 != null) {
                            parcel2.writeInt(1);
                            _result10.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 68:
                        parcel.enforceInterface(descriptor);
                        setFileTransferContentType(data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 69:
                        parcel.enforceInterface(descriptor);
                        _result9 = getFileTransferContentType(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result9);
                        return true;
                    case 70:
                        parcel.enforceInterface(descriptor);
                        setFileTransferFileSize(data.readInt(), data.readLong(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 71:
                        parcel.enforceInterface(descriptor);
                        _result8 = getFileTransferFileSize(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeLong(_result8);
                        return true;
                    case 72:
                        parcel.enforceInterface(descriptor);
                        setFileTransferTransferOffset(data.readInt(), data.readLong(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 73:
                        parcel.enforceInterface(descriptor);
                        _result8 = getFileTransferTransferOffset(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeLong(_result8);
                        return true;
                    case 74:
                        parcel.enforceInterface(descriptor);
                        setFileTransferStatus(data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 75:
                        parcel.enforceInterface(descriptor);
                        _result5 = getFileTransferStatus(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 76:
                        parcel.enforceInterface(descriptor);
                        setFileTransferWidth(data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 77:
                        parcel.enforceInterface(descriptor);
                        _result5 = getFileTransferWidth(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 78:
                        parcel.enforceInterface(descriptor);
                        setFileTransferHeight(data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 79:
                        parcel.enforceInterface(descriptor);
                        _result5 = getFileTransferHeight(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 80:
                        parcel.enforceInterface(descriptor);
                        setFileTransferLength(data.readInt(), data.readLong(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 81:
                        parcel.enforceInterface(descriptor);
                        _result8 = getFileTransferLength(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeLong(_result8);
                        return true;
                    case 82:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg12 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        setFileTransferPreviewUri(_arg02, _arg12, data.readString());
                        reply.writeNoException();
                        return true;
                    case 83:
                        parcel.enforceInterface(descriptor);
                        _result10 = getFileTransferPreviewUri(data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result10 != null) {
                            parcel2.writeInt(1);
                            _result10.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 84:
                        parcel.enforceInterface(descriptor);
                        setFileTransferPreviewType(data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 85:
                        parcel.enforceInterface(descriptor);
                        _result9 = getFileTransferPreviewType(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result9);
                        return true;
                    case 86:
                        parcel.enforceInterface(descriptor);
                        _arg02 = createGroupThreadNameChangedEvent(data.readLong(), data.readInt(), data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_arg02);
                        return true;
                    case 87:
                        Uri _arg3;
                        parcel.enforceInterface(descriptor);
                        long _arg09 = data.readLong();
                        int _arg16 = data.readInt();
                        int _arg23 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg3 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        _arg02 = createGroupThreadIconChangedEvent(_arg09, _arg16, _arg23, _arg3, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_arg02);
                        return true;
                    case 88:
                        parcel.enforceInterface(descriptor);
                        _arg02 = createGroupThreadParticipantJoinedEvent(data.readLong(), data.readInt(), data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_arg02);
                        return true;
                    case 89:
                        parcel.enforceInterface(descriptor);
                        _arg02 = createGroupThreadParticipantLeftEvent(data.readLong(), data.readInt(), data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_arg02);
                        return true;
                    case 90:
                        parcel.enforceInterface(descriptor);
                        _arg02 = createParticipantAliasChangedEvent(data.readLong(), data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_arg02);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IRcs impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IRcs getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    int addIncomingMessage(int i, RcsIncomingMessageCreationParams rcsIncomingMessageCreationParams, String str) throws RemoteException;

    int addOutgoingMessage(int i, RcsOutgoingMessageCreationParams rcsOutgoingMessageCreationParams, String str) throws RemoteException;

    void addParticipantToGroupThread(int i, int i2, String str) throws RemoteException;

    int createGroupThread(int[] iArr, String str, Uri uri, String str2) throws RemoteException;

    int createGroupThreadIconChangedEvent(long j, int i, int i2, Uri uri, String str) throws RemoteException;

    int createGroupThreadNameChangedEvent(long j, int i, int i2, String str, String str2) throws RemoteException;

    int createGroupThreadParticipantJoinedEvent(long j, int i, int i2, int i3, String str) throws RemoteException;

    int createGroupThreadParticipantLeftEvent(long j, int i, int i2, int i3, String str) throws RemoteException;

    int createParticipantAliasChangedEvent(long j, int i, String str, String str2) throws RemoteException;

    int createRcs1To1Thread(int i, String str) throws RemoteException;

    int createRcsParticipant(String str, String str2, String str3) throws RemoteException;

    void deleteFileTransfer(int i, String str) throws RemoteException;

    void deleteMessage(int i, boolean z, int i2, boolean z2, String str) throws RemoteException;

    boolean deleteThread(int i, int i2, String str) throws RemoteException;

    long get1To1ThreadFallbackThreadId(int i, String str) throws RemoteException;

    int get1To1ThreadOtherParticipantId(int i, String str) throws RemoteException;

    RcsEventQueryResultDescriptor getEvents(RcsEventQueryParams rcsEventQueryParams, String str) throws RemoteException;

    RcsEventQueryResultDescriptor getEventsWithToken(RcsQueryContinuationToken rcsQueryContinuationToken, String str) throws RemoteException;

    String getFileTransferContentType(int i, String str) throws RemoteException;

    Uri getFileTransferContentUri(int i, String str) throws RemoteException;

    long getFileTransferFileSize(int i, String str) throws RemoteException;

    int getFileTransferHeight(int i, String str) throws RemoteException;

    long getFileTransferLength(int i, String str) throws RemoteException;

    String getFileTransferPreviewType(int i, String str) throws RemoteException;

    Uri getFileTransferPreviewUri(int i, String str) throws RemoteException;

    String getFileTransferSessionId(int i, String str) throws RemoteException;

    int getFileTransferStatus(int i, String str) throws RemoteException;

    long getFileTransferTransferOffset(int i, String str) throws RemoteException;

    int getFileTransferWidth(int i, String str) throws RemoteException;

    int[] getFileTransfersAttachedToMessage(int i, boolean z, String str) throws RemoteException;

    String getGlobalMessageIdForMessage(int i, boolean z, String str) throws RemoteException;

    Uri getGroupThreadConferenceUri(int i, String str) throws RemoteException;

    Uri getGroupThreadIcon(int i, String str) throws RemoteException;

    String getGroupThreadName(int i, String str) throws RemoteException;

    int getGroupThreadOwner(int i, String str) throws RemoteException;

    double getLatitudeForMessage(int i, boolean z, String str) throws RemoteException;

    double getLongitudeForMessage(int i, boolean z, String str) throws RemoteException;

    long getMessageArrivalTimestamp(int i, boolean z, String str) throws RemoteException;

    long getMessageOriginationTimestamp(int i, boolean z, String str) throws RemoteException;

    int[] getMessageRecipients(int i, String str) throws RemoteException;

    long getMessageSeenTimestamp(int i, boolean z, String str) throws RemoteException;

    RcsMessageSnippet getMessageSnippet(int i, String str) throws RemoteException;

    int getMessageStatus(int i, boolean z, String str) throws RemoteException;

    int getMessageSubId(int i, boolean z, String str) throws RemoteException;

    RcsMessageQueryResultParcelable getMessages(RcsMessageQueryParams rcsMessageQueryParams, String str) throws RemoteException;

    RcsMessageQueryResultParcelable getMessagesWithToken(RcsQueryContinuationToken rcsQueryContinuationToken, String str) throws RemoteException;

    long getOutgoingDeliveryDeliveredTimestamp(int i, int i2, String str) throws RemoteException;

    long getOutgoingDeliverySeenTimestamp(int i, int i2, String str) throws RemoteException;

    int getOutgoingDeliveryStatus(int i, int i2, String str) throws RemoteException;

    RcsParticipantQueryResultParcelable getParticipants(RcsParticipantQueryParams rcsParticipantQueryParams, String str) throws RemoteException;

    RcsParticipantQueryResultParcelable getParticipantsWithToken(RcsQueryContinuationToken rcsQueryContinuationToken, String str) throws RemoteException;

    String getRcsParticipantAlias(int i, String str) throws RemoteException;

    String getRcsParticipantCanonicalAddress(int i, String str) throws RemoteException;

    String getRcsParticipantContactId(int i, String str) throws RemoteException;

    RcsThreadQueryResultParcelable getRcsThreads(RcsThreadQueryParams rcsThreadQueryParams, String str) throws RemoteException;

    RcsThreadQueryResultParcelable getRcsThreadsWithToken(RcsQueryContinuationToken rcsQueryContinuationToken, String str) throws RemoteException;

    int getSenderParticipant(int i, String str) throws RemoteException;

    String getTextForMessage(int i, boolean z, String str) throws RemoteException;

    void removeParticipantFromGroupThread(int i, int i2, String str) throws RemoteException;

    void set1To1ThreadFallbackThreadId(int i, long j, String str) throws RemoteException;

    void setFileTransferContentType(int i, String str, String str2) throws RemoteException;

    void setFileTransferContentUri(int i, Uri uri, String str) throws RemoteException;

    void setFileTransferFileSize(int i, long j, String str) throws RemoteException;

    void setFileTransferHeight(int i, int i2, String str) throws RemoteException;

    void setFileTransferLength(int i, long j, String str) throws RemoteException;

    void setFileTransferPreviewType(int i, String str, String str2) throws RemoteException;

    void setFileTransferPreviewUri(int i, Uri uri, String str) throws RemoteException;

    void setFileTransferSessionId(int i, String str, String str2) throws RemoteException;

    void setFileTransferStatus(int i, int i2, String str) throws RemoteException;

    void setFileTransferTransferOffset(int i, long j, String str) throws RemoteException;

    void setFileTransferWidth(int i, int i2, String str) throws RemoteException;

    void setGlobalMessageIdForMessage(int i, boolean z, String str, String str2) throws RemoteException;

    void setGroupThreadConferenceUri(int i, Uri uri, String str) throws RemoteException;

    void setGroupThreadIcon(int i, Uri uri, String str) throws RemoteException;

    void setGroupThreadName(int i, String str, String str2) throws RemoteException;

    void setGroupThreadOwner(int i, int i2, String str) throws RemoteException;

    void setLatitudeForMessage(int i, boolean z, double d, String str) throws RemoteException;

    void setLongitudeForMessage(int i, boolean z, double d, String str) throws RemoteException;

    void setMessageArrivalTimestamp(int i, boolean z, long j, String str) throws RemoteException;

    void setMessageOriginationTimestamp(int i, boolean z, long j, String str) throws RemoteException;

    void setMessageSeenTimestamp(int i, boolean z, long j, String str) throws RemoteException;

    void setMessageStatus(int i, boolean z, int i2, String str) throws RemoteException;

    void setMessageSubId(int i, boolean z, int i2, String str) throws RemoteException;

    void setOutgoingDeliveryDeliveredTimestamp(int i, int i2, long j, String str) throws RemoteException;

    void setOutgoingDeliverySeenTimestamp(int i, int i2, long j, String str) throws RemoteException;

    void setOutgoingDeliveryStatus(int i, int i2, int i3, String str) throws RemoteException;

    void setRcsParticipantAlias(int i, String str, String str2) throws RemoteException;

    void setRcsParticipantContactId(int i, String str, String str2) throws RemoteException;

    void setTextForMessage(int i, boolean z, String str, String str2) throws RemoteException;

    int storeFileTransfer(int i, boolean z, RcsFileTransferCreationParams rcsFileTransferCreationParams, String str) throws RemoteException;
}
