package com.android.internal.telephony;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMms extends IInterface {

    public static class Default implements IMms {
        public void sendMessage(int subId, String callingPkg, Uri contentUri, String locationUrl, Bundle configOverrides, PendingIntent sentIntent) throws RemoteException {
        }

        public void downloadMessage(int subId, String callingPkg, String locationUrl, Uri contentUri, Bundle configOverrides, PendingIntent downloadedIntent) throws RemoteException {
        }

        public Bundle getCarrierConfigValues(int subId) throws RemoteException {
            return null;
        }

        public Uri importTextMessage(String callingPkg, String address, int type, String text, long timestampMillis, boolean seen, boolean read) throws RemoteException {
            return null;
        }

        public Uri importMultimediaMessage(String callingPkg, Uri contentUri, String messageId, long timestampSecs, boolean seen, boolean read) throws RemoteException {
            return null;
        }

        public boolean deleteStoredMessage(String callingPkg, Uri messageUri) throws RemoteException {
            return false;
        }

        public boolean deleteStoredConversation(String callingPkg, long conversationId) throws RemoteException {
            return false;
        }

        public boolean updateStoredMessageStatus(String callingPkg, Uri messageUri, ContentValues statusValues) throws RemoteException {
            return false;
        }

        public boolean archiveStoredConversation(String callingPkg, long conversationId, boolean archived) throws RemoteException {
            return false;
        }

        public Uri addTextMessageDraft(String callingPkg, String address, String text) throws RemoteException {
            return null;
        }

        public Uri addMultimediaMessageDraft(String callingPkg, Uri contentUri) throws RemoteException {
            return null;
        }

        public void sendStoredMessage(int subId, String callingPkg, Uri messageUri, Bundle configOverrides, PendingIntent sentIntent) throws RemoteException {
        }

        public void setAutoPersisting(String callingPkg, boolean enabled) throws RemoteException {
        }

        public boolean getAutoPersisting() throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMms {
        private static final String DESCRIPTOR = "com.android.internal.telephony.IMms";
        static final int TRANSACTION_addMultimediaMessageDraft = 11;
        static final int TRANSACTION_addTextMessageDraft = 10;
        static final int TRANSACTION_archiveStoredConversation = 9;
        static final int TRANSACTION_deleteStoredConversation = 7;
        static final int TRANSACTION_deleteStoredMessage = 6;
        static final int TRANSACTION_downloadMessage = 2;
        static final int TRANSACTION_getAutoPersisting = 14;
        static final int TRANSACTION_getCarrierConfigValues = 3;
        static final int TRANSACTION_importMultimediaMessage = 5;
        static final int TRANSACTION_importTextMessage = 4;
        static final int TRANSACTION_sendMessage = 1;
        static final int TRANSACTION_sendStoredMessage = 12;
        static final int TRANSACTION_setAutoPersisting = 13;
        static final int TRANSACTION_updateStoredMessageStatus = 8;

        private static class Proxy implements IMms {
            public static IMms sDefaultImpl;
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

            public void sendMessage(int subId, String callingPkg, Uri contentUri, String locationUrl, Bundle configOverrides, PendingIntent sentIntent) throws RemoteException {
                Throwable th;
                String str;
                String str2;
                Uri uri = contentUri;
                Bundle bundle = configOverrides;
                PendingIntent pendingIntent = sentIntent;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(subId);
                    } catch (Throwable th2) {
                        th = th2;
                        str = callingPkg;
                        str2 = locationUrl;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(callingPkg);
                        if (uri != null) {
                            _data.writeInt(1);
                            uri.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeString(locationUrl);
                            if (bundle != null) {
                                _data.writeInt(1);
                                bundle.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (pendingIntent != null) {
                                _data.writeInt(1);
                                pendingIntent.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().sendMessage(subId, callingPkg, contentUri, locationUrl, configOverrides, sentIntent);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th3) {
                            th = th3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        str2 = locationUrl;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    int i = subId;
                    str = callingPkg;
                    str2 = locationUrl;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void downloadMessage(int subId, String callingPkg, String locationUrl, Uri contentUri, Bundle configOverrides, PendingIntent downloadedIntent) throws RemoteException {
                Throwable th;
                String str;
                String str2;
                Uri uri = contentUri;
                Bundle bundle = configOverrides;
                PendingIntent pendingIntent = downloadedIntent;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(subId);
                    } catch (Throwable th2) {
                        th = th2;
                        str = callingPkg;
                        str2 = locationUrl;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(callingPkg);
                        try {
                            _data.writeString(locationUrl);
                            if (uri != null) {
                                _data.writeInt(1);
                                uri.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (bundle != null) {
                                _data.writeInt(1);
                                bundle.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (pendingIntent != null) {
                                _data.writeInt(1);
                                pendingIntent.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().downloadMessage(subId, callingPkg, locationUrl, contentUri, configOverrides, downloadedIntent);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th3) {
                            th = th3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        str2 = locationUrl;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    int i = subId;
                    str = callingPkg;
                    str2 = locationUrl;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public Bundle getCarrierConfigValues(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    Bundle bundle = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        bundle = Stub.getDefaultImpl();
                        if (bundle != 0) {
                            bundle = Stub.getDefaultImpl().getCarrierConfigValues(subId);
                            return bundle;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        bundle = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return bundle;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Uri importTextMessage(String callingPkg, String address, int type, String text, long timestampMillis, boolean seen, boolean read) throws RemoteException {
                Throwable th;
                String str;
                int i;
                String str2;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(callingPkg);
                    } catch (Throwable th2) {
                        th = th2;
                        str = address;
                        i = type;
                        str2 = text;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(address);
                    } catch (Throwable th3) {
                        th = th3;
                        i = type;
                        str2 = text;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(type);
                        try {
                            _data.writeString(text);
                            _data.writeLong(timestampMillis);
                            int i2 = 1;
                            _data.writeInt(seen ? 1 : 0);
                            if (!read) {
                                i2 = 0;
                            }
                            _data.writeInt(i2);
                            Uri _result;
                            if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                if (_reply.readInt() != 0) {
                                    _result = (Uri) Uri.CREATOR.createFromParcel(_reply);
                                } else {
                                    _result = null;
                                }
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            _result = Stub.getDefaultImpl().importTextMessage(callingPkg, address, type, text, timestampMillis, seen, read);
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        str2 = text;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str3 = callingPkg;
                    str = address;
                    i = type;
                    str2 = text;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public Uri importMultimediaMessage(String callingPkg, Uri contentUri, String messageId, long timestampSecs, boolean seen, boolean read) throws RemoteException {
                Throwable th;
                long j;
                String str;
                Uri uri = contentUri;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(callingPkg);
                        i = 1;
                        if (uri != null) {
                            _data.writeInt(1);
                            uri.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeString(messageId);
                        } catch (Throwable th2) {
                            th = th2;
                            j = timestampSecs;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        str = messageId;
                        j = timestampSecs;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeLong(timestampSecs);
                        _data.writeInt(seen ? 1 : 0);
                        if (!read) {
                            i = 0;
                        }
                        _data.writeInt(i);
                        Uri _result;
                        if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            if (_reply.readInt() != 0) {
                                _result = (Uri) Uri.CREATOR.createFromParcel(_reply);
                            } else {
                                _result = null;
                            }
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        _result = Stub.getDefaultImpl().importMultimediaMessage(callingPkg, contentUri, messageId, timestampSecs, seen, read);
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    String str2 = callingPkg;
                    str = messageId;
                    j = timestampSecs;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public boolean deleteStoredMessage(String callingPkg, Uri messageUri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    boolean _result = true;
                    if (messageUri != null) {
                        _data.writeInt(1);
                        messageUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().deleteStoredMessage(callingPkg, messageUri);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean deleteStoredConversation(String callingPkg, long conversationId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeLong(conversationId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().deleteStoredConversation(callingPkg, conversationId);
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

            public boolean updateStoredMessageStatus(String callingPkg, Uri messageUri, ContentValues statusValues) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    boolean _result = true;
                    if (messageUri != null) {
                        _data.writeInt(1);
                        messageUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (statusValues != null) {
                        _data.writeInt(1);
                        statusValues.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().updateStoredMessageStatus(callingPkg, messageUri, statusValues);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean archiveStoredConversation(String callingPkg, long conversationId, boolean archived) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeLong(conversationId);
                    boolean _result = true;
                    _data.writeInt(archived ? 1 : 0);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().archiveStoredConversation(callingPkg, conversationId, archived);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Uri addTextMessageDraft(String callingPkg, String address, String text) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeString(address);
                    _data.writeString(text);
                    Uri uri = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        uri = Stub.getDefaultImpl();
                        if (uri != 0) {
                            uri = Stub.getDefaultImpl().addTextMessageDraft(callingPkg, address, text);
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

            public Uri addMultimediaMessageDraft(String callingPkg, Uri contentUri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    if (contentUri != null) {
                        _data.writeInt(1);
                        contentUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    Uri uri = this.mRemote;
                    if (!uri.transact(11, _data, _reply, 0)) {
                        uri = Stub.getDefaultImpl();
                        if (uri != null) {
                            uri = Stub.getDefaultImpl().addMultimediaMessageDraft(callingPkg, contentUri);
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

            public void sendStoredMessage(int subId, String callingPkg, Uri messageUri, Bundle configOverrides, PendingIntent sentIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    if (messageUri != null) {
                        _data.writeInt(1);
                        messageUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (configOverrides != null) {
                        _data.writeInt(1);
                        configOverrides.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sendStoredMessage(subId, callingPkg, messageUri, configOverrides, sentIntent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAutoPersisting(String callingPkg, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAutoPersisting(callingPkg, enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getAutoPersisting() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getAutoPersisting();
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMms asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMms)) {
                return new Proxy(obj);
            }
            return (IMms) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "sendMessage";
                case 2:
                    return "downloadMessage";
                case 3:
                    return "getCarrierConfigValues";
                case 4:
                    return "importTextMessage";
                case 5:
                    return "importMultimediaMessage";
                case 6:
                    return "deleteStoredMessage";
                case 7:
                    return "deleteStoredConversation";
                case 8:
                    return "updateStoredMessageStatus";
                case 9:
                    return "archiveStoredConversation";
                case 10:
                    return "addTextMessageDraft";
                case 11:
                    return "addMultimediaMessageDraft";
                case 12:
                    return "sendStoredMessage";
                case 13:
                    return "setAutoPersisting";
                case 14:
                    return "getAutoPersisting";
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
                boolean z = false;
                int _arg0;
                String _arg1;
                Bundle _arg4;
                PendingIntent _arg5;
                Uri _arg3;
                Uri _result;
                String _arg02;
                Uri _arg12;
                boolean _result2;
                switch (i) {
                    case 1:
                        Uri _arg2;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        String _arg32 = data.readString();
                        if (data.readInt() != 0) {
                            _arg4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg4 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg5 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        sendMessage(_arg0, _arg1, _arg2, _arg32, _arg4, _arg5);
                        reply.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _arg1 = data.readString();
                        String _arg22 = data.readString();
                        if (data.readInt() != 0) {
                            _arg3 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg4 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg5 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        downloadMessage(_arg0, _arg1, _arg22, _arg3, _arg4, _arg5);
                        reply.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        Bundle _result3 = getCarrierConfigValues(data.readInt());
                        reply.writeNoException();
                        if (_result3 != null) {
                            parcel2.writeInt(1);
                            _result3.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        _result = importTextMessage(data.readString(), data.readString(), data.readInt(), data.readString(), data.readLong(), data.readInt() != 0, data.readInt() != 0);
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(1);
                            _result.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg3 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        _result = importMultimediaMessage(_arg1, _arg3, data.readString(), data.readLong(), data.readInt() != 0, data.readInt() != 0);
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(1);
                            _result.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        boolean _result4 = deleteStoredMessage(_arg02, _arg12);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        _result2 = deleteStoredConversation(data.readString(), data.readLong());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 8:
                        ContentValues _arg23;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg23 = (ContentValues) ContentValues.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        _result2 = updateStoredMessageStatus(_arg02, _arg12, _arg23);
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        long _arg13 = data.readLong();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        boolean _result5 = archiveStoredConversation(_arg02, _arg13, z);
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        Uri _result6 = addTextMessageDraft(data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        Uri _result7 = addMultimediaMessageDraft(_arg02, _arg12);
                        reply.writeNoException();
                        if (_result7 != null) {
                            parcel2.writeInt(1);
                            _result7.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 12:
                        Uri _arg24;
                        Bundle _arg33;
                        PendingIntent _arg42;
                        parcel.enforceInterface(descriptor);
                        int _arg03 = data.readInt();
                        String _arg14 = data.readString();
                        if (data.readInt() != 0) {
                            _arg24 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg24 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg33 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg33 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg42 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg42 = null;
                        }
                        sendStoredMessage(_arg03, _arg14, _arg24, _arg33, _arg42);
                        reply.writeNoException();
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setAutoPersisting(_arg02, z);
                        reply.writeNoException();
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        boolean _result8 = getAutoPersisting();
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IMms impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMms getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    Uri addMultimediaMessageDraft(String str, Uri uri) throws RemoteException;

    Uri addTextMessageDraft(String str, String str2, String str3) throws RemoteException;

    boolean archiveStoredConversation(String str, long j, boolean z) throws RemoteException;

    boolean deleteStoredConversation(String str, long j) throws RemoteException;

    boolean deleteStoredMessage(String str, Uri uri) throws RemoteException;

    void downloadMessage(int i, String str, String str2, Uri uri, Bundle bundle, PendingIntent pendingIntent) throws RemoteException;

    boolean getAutoPersisting() throws RemoteException;

    Bundle getCarrierConfigValues(int i) throws RemoteException;

    Uri importMultimediaMessage(String str, Uri uri, String str2, long j, boolean z, boolean z2) throws RemoteException;

    Uri importTextMessage(String str, String str2, int i, String str3, long j, boolean z, boolean z2) throws RemoteException;

    void sendMessage(int i, String str, Uri uri, String str2, Bundle bundle, PendingIntent pendingIntent) throws RemoteException;

    void sendStoredMessage(int i, String str, Uri uri, Bundle bundle, PendingIntent pendingIntent) throws RemoteException;

    void setAutoPersisting(String str, boolean z) throws RemoteException;

    boolean updateStoredMessageStatus(String str, Uri uri, ContentValues contentValues) throws RemoteException;
}
