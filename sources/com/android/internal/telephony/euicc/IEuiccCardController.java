package com.android.internal.telephony.euicc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IEuiccCardController extends IInterface {

    public static class Default implements IEuiccCardController {
        public void getAllProfiles(String callingPackage, String cardId, IGetAllProfilesCallback callback) throws RemoteException {
        }

        public void getProfile(String callingPackage, String cardId, String iccid, IGetProfileCallback callback) throws RemoteException {
        }

        public void disableProfile(String callingPackage, String cardId, String iccid, boolean refresh, IDisableProfileCallback callback) throws RemoteException {
        }

        public void switchToProfile(String callingPackage, String cardId, String iccid, boolean refresh, ISwitchToProfileCallback callback) throws RemoteException {
        }

        public void setNickname(String callingPackage, String cardId, String iccid, String nickname, ISetNicknameCallback callback) throws RemoteException {
        }

        public void deleteProfile(String callingPackage, String cardId, String iccid, IDeleteProfileCallback callback) throws RemoteException {
        }

        public void resetMemory(String callingPackage, String cardId, int options, IResetMemoryCallback callback) throws RemoteException {
        }

        public void getDefaultSmdpAddress(String callingPackage, String cardId, IGetDefaultSmdpAddressCallback callback) throws RemoteException {
        }

        public void getSmdsAddress(String callingPackage, String cardId, IGetSmdsAddressCallback callback) throws RemoteException {
        }

        public void setDefaultSmdpAddress(String callingPackage, String cardId, String address, ISetDefaultSmdpAddressCallback callback) throws RemoteException {
        }

        public void getRulesAuthTable(String callingPackage, String cardId, IGetRulesAuthTableCallback callback) throws RemoteException {
        }

        public void getEuiccChallenge(String callingPackage, String cardId, IGetEuiccChallengeCallback callback) throws RemoteException {
        }

        public void getEuiccInfo1(String callingPackage, String cardId, IGetEuiccInfo1Callback callback) throws RemoteException {
        }

        public void getEuiccInfo2(String callingPackage, String cardId, IGetEuiccInfo2Callback callback) throws RemoteException {
        }

        public void authenticateServer(String callingPackage, String cardId, String matchingId, byte[] serverSigned1, byte[] serverSignature1, byte[] euiccCiPkIdToBeUsed, byte[] serverCertificatein, IAuthenticateServerCallback callback) throws RemoteException {
        }

        public void prepareDownload(String callingPackage, String cardId, byte[] hashCc, byte[] smdpSigned2, byte[] smdpSignature2, byte[] smdpCertificate, IPrepareDownloadCallback callback) throws RemoteException {
        }

        public void loadBoundProfilePackage(String callingPackage, String cardId, byte[] boundProfilePackage, ILoadBoundProfilePackageCallback callback) throws RemoteException {
        }

        public void cancelSession(String callingPackage, String cardId, byte[] transactionId, int reason, ICancelSessionCallback callback) throws RemoteException {
        }

        public void listNotifications(String callingPackage, String cardId, int events, IListNotificationsCallback callback) throws RemoteException {
        }

        public void retrieveNotificationList(String callingPackage, String cardId, int events, IRetrieveNotificationListCallback callback) throws RemoteException {
        }

        public void retrieveNotification(String callingPackage, String cardId, int seqNumber, IRetrieveNotificationCallback callback) throws RemoteException {
        }

        public void removeNotificationFromList(String callingPackage, String cardId, int seqNumber, IRemoveNotificationFromListCallback callback) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IEuiccCardController {
        private static final String DESCRIPTOR = "com.android.internal.telephony.euicc.IEuiccCardController";
        static final int TRANSACTION_authenticateServer = 15;
        static final int TRANSACTION_cancelSession = 18;
        static final int TRANSACTION_deleteProfile = 6;
        static final int TRANSACTION_disableProfile = 3;
        static final int TRANSACTION_getAllProfiles = 1;
        static final int TRANSACTION_getDefaultSmdpAddress = 8;
        static final int TRANSACTION_getEuiccChallenge = 12;
        static final int TRANSACTION_getEuiccInfo1 = 13;
        static final int TRANSACTION_getEuiccInfo2 = 14;
        static final int TRANSACTION_getProfile = 2;
        static final int TRANSACTION_getRulesAuthTable = 11;
        static final int TRANSACTION_getSmdsAddress = 9;
        static final int TRANSACTION_listNotifications = 19;
        static final int TRANSACTION_loadBoundProfilePackage = 17;
        static final int TRANSACTION_prepareDownload = 16;
        static final int TRANSACTION_removeNotificationFromList = 22;
        static final int TRANSACTION_resetMemory = 7;
        static final int TRANSACTION_retrieveNotification = 21;
        static final int TRANSACTION_retrieveNotificationList = 20;
        static final int TRANSACTION_setDefaultSmdpAddress = 10;
        static final int TRANSACTION_setNickname = 5;
        static final int TRANSACTION_switchToProfile = 4;

        private static class Proxy implements IEuiccCardController {
            public static IEuiccCardController sDefaultImpl;
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

            public void getAllProfiles(String callingPackage, String cardId, IGetAllProfilesCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getAllProfiles(callingPackage, cardId, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getProfile(String callingPackage, String cardId, String iccid, IGetProfileCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeString(iccid);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getProfile(callingPackage, cardId, iccid, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void disableProfile(String callingPackage, String cardId, String iccid, boolean refresh, IDisableProfileCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeString(iccid);
                    _data.writeInt(refresh ? 1 : 0);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().disableProfile(callingPackage, cardId, iccid, refresh, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void switchToProfile(String callingPackage, String cardId, String iccid, boolean refresh, ISwitchToProfileCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeString(iccid);
                    _data.writeInt(refresh ? 1 : 0);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().switchToProfile(callingPackage, cardId, iccid, refresh, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setNickname(String callingPackage, String cardId, String iccid, String nickname, ISetNicknameCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeString(iccid);
                    _data.writeString(nickname);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setNickname(callingPackage, cardId, iccid, nickname, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void deleteProfile(String callingPackage, String cardId, String iccid, IDeleteProfileCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeString(iccid);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().deleteProfile(callingPackage, cardId, iccid, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void resetMemory(String callingPackage, String cardId, int options, IResetMemoryCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeInt(options);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().resetMemory(callingPackage, cardId, options, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getDefaultSmdpAddress(String callingPackage, String cardId, IGetDefaultSmdpAddressCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getDefaultSmdpAddress(callingPackage, cardId, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getSmdsAddress(String callingPackage, String cardId, IGetSmdsAddressCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getSmdsAddress(callingPackage, cardId, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setDefaultSmdpAddress(String callingPackage, String cardId, String address, ISetDefaultSmdpAddressCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeString(address);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(10, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setDefaultSmdpAddress(callingPackage, cardId, address, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getRulesAuthTable(String callingPackage, String cardId, IGetRulesAuthTableCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getRulesAuthTable(callingPackage, cardId, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getEuiccChallenge(String callingPackage, String cardId, IGetEuiccChallengeCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(12, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getEuiccChallenge(callingPackage, cardId, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getEuiccInfo1(String callingPackage, String cardId, IGetEuiccInfo1Callback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(13, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getEuiccInfo1(callingPackage, cardId, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getEuiccInfo2(String callingPackage, String cardId, IGetEuiccInfo2Callback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(14, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getEuiccInfo2(callingPackage, cardId, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void authenticateServer(String callingPackage, String cardId, String matchingId, byte[] serverSigned1, byte[] serverSignature1, byte[] euiccCiPkIdToBeUsed, byte[] serverCertificatein, IAuthenticateServerCallback callback) throws RemoteException {
                Throwable th;
                String str;
                String str2;
                byte[] bArr;
                byte[] bArr2;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(callingPackage);
                    } catch (Throwable th2) {
                        th = th2;
                        str = cardId;
                        str2 = matchingId;
                        bArr = serverSigned1;
                        bArr2 = serverSignature1;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(cardId);
                        try {
                            _data.writeString(matchingId);
                        } catch (Throwable th3) {
                            th = th3;
                            bArr = serverSigned1;
                            bArr2 = serverSignature1;
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeByteArray(serverSigned1);
                            try {
                                _data.writeByteArray(serverSignature1);
                                _data.writeByteArray(euiccCiPkIdToBeUsed);
                                _data.writeByteArray(serverCertificatein);
                                _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                                if (this.mRemote.transact(15, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                    _data.recycle();
                                    return;
                                }
                                Stub.getDefaultImpl().authenticateServer(callingPackage, cardId, matchingId, serverSigned1, serverSignature1, euiccCiPkIdToBeUsed, serverCertificatein, callback);
                                _data.recycle();
                            } catch (Throwable th4) {
                                th = th4;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                            bArr2 = serverSignature1;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        str2 = matchingId;
                        bArr = serverSigned1;
                        bArr2 = serverSignature1;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str3 = callingPackage;
                    str = cardId;
                    str2 = matchingId;
                    bArr = serverSigned1;
                    bArr2 = serverSignature1;
                    _data.recycle();
                    throw th;
                }
            }

            public void prepareDownload(String callingPackage, String cardId, byte[] hashCc, byte[] smdpSigned2, byte[] smdpSignature2, byte[] smdpCertificate, IPrepareDownloadCallback callback) throws RemoteException {
                Throwable th;
                String str;
                byte[] bArr;
                byte[] bArr2;
                byte[] bArr3;
                byte[] bArr4;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(callingPackage);
                    } catch (Throwable th2) {
                        th = th2;
                        str = cardId;
                        bArr = hashCc;
                        bArr2 = smdpSigned2;
                        bArr3 = smdpSignature2;
                        bArr4 = smdpCertificate;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(cardId);
                        try {
                            _data.writeByteArray(hashCc);
                            try {
                                _data.writeByteArray(smdpSigned2);
                                try {
                                    _data.writeByteArray(smdpSignature2);
                                } catch (Throwable th3) {
                                    th = th3;
                                    bArr4 = smdpCertificate;
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th4) {
                                th = th4;
                                bArr3 = smdpSignature2;
                                bArr4 = smdpCertificate;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                            bArr2 = smdpSigned2;
                            bArr3 = smdpSignature2;
                            bArr4 = smdpCertificate;
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeByteArray(smdpCertificate);
                            _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                            if (this.mRemote.transact(16, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().prepareDownload(callingPackage, cardId, hashCc, smdpSigned2, smdpSignature2, smdpCertificate, callback);
                            _data.recycle();
                        } catch (Throwable th6) {
                            th = th6;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        bArr = hashCc;
                        bArr2 = smdpSigned2;
                        bArr3 = smdpSignature2;
                        bArr4 = smdpCertificate;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    String str2 = callingPackage;
                    str = cardId;
                    bArr = hashCc;
                    bArr2 = smdpSigned2;
                    bArr3 = smdpSignature2;
                    bArr4 = smdpCertificate;
                    _data.recycle();
                    throw th;
                }
            }

            public void loadBoundProfilePackage(String callingPackage, String cardId, byte[] boundProfilePackage, ILoadBoundProfilePackageCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeByteArray(boundProfilePackage);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(17, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().loadBoundProfilePackage(callingPackage, cardId, boundProfilePackage, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void cancelSession(String callingPackage, String cardId, byte[] transactionId, int reason, ICancelSessionCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeByteArray(transactionId);
                    _data.writeInt(reason);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(18, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().cancelSession(callingPackage, cardId, transactionId, reason, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void listNotifications(String callingPackage, String cardId, int events, IListNotificationsCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeInt(events);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(19, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().listNotifications(callingPackage, cardId, events, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void retrieveNotificationList(String callingPackage, String cardId, int events, IRetrieveNotificationListCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeInt(events);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(20, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().retrieveNotificationList(callingPackage, cardId, events, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void retrieveNotification(String callingPackage, String cardId, int seqNumber, IRetrieveNotificationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeInt(seqNumber);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(21, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().retrieveNotification(callingPackage, cardId, seqNumber, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void removeNotificationFromList(String callingPackage, String cardId, int seqNumber, IRemoveNotificationFromListCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeInt(seqNumber);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(22, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().removeNotificationFromList(callingPackage, cardId, seqNumber, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IEuiccCardController asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IEuiccCardController)) {
                return new Proxy(obj);
            }
            return (IEuiccCardController) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getAllProfiles";
                case 2:
                    return "getProfile";
                case 3:
                    return "disableProfile";
                case 4:
                    return "switchToProfile";
                case 5:
                    return "setNickname";
                case 6:
                    return "deleteProfile";
                case 7:
                    return "resetMemory";
                case 8:
                    return "getDefaultSmdpAddress";
                case 9:
                    return "getSmdsAddress";
                case 10:
                    return "setDefaultSmdpAddress";
                case 11:
                    return "getRulesAuthTable";
                case 12:
                    return "getEuiccChallenge";
                case 13:
                    return "getEuiccInfo1";
                case 14:
                    return "getEuiccInfo2";
                case 15:
                    return "authenticateServer";
                case 16:
                    return "prepareDownload";
                case 17:
                    return "loadBoundProfilePackage";
                case 18:
                    return "cancelSession";
                case 19:
                    return "listNotifications";
                case 20:
                    return "retrieveNotificationList";
                case 21:
                    return "retrieveNotification";
                case 22:
                    return "removeNotificationFromList";
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
            String descriptor = DESCRIPTOR;
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        getAllProfiles(data.readString(), data.readString(), com.android.internal.telephony.euicc.IGetAllProfilesCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        getProfile(data.readString(), data.readString(), data.readString(), com.android.internal.telephony.euicc.IGetProfileCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        disableProfile(data.readString(), data.readString(), data.readString(), data.readInt() != 0, com.android.internal.telephony.euicc.IDisableProfileCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        switchToProfile(data.readString(), data.readString(), data.readString(), data.readInt() != 0, com.android.internal.telephony.euicc.ISwitchToProfileCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        setNickname(data.readString(), data.readString(), data.readString(), data.readString(), com.android.internal.telephony.euicc.ISetNicknameCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        deleteProfile(data.readString(), data.readString(), data.readString(), com.android.internal.telephony.euicc.IDeleteProfileCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        resetMemory(data.readString(), data.readString(), data.readInt(), com.android.internal.telephony.euicc.IResetMemoryCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        getDefaultSmdpAddress(data.readString(), data.readString(), com.android.internal.telephony.euicc.IGetDefaultSmdpAddressCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        getSmdsAddress(data.readString(), data.readString(), com.android.internal.telephony.euicc.IGetSmdsAddressCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        setDefaultSmdpAddress(data.readString(), data.readString(), data.readString(), com.android.internal.telephony.euicc.ISetDefaultSmdpAddressCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        getRulesAuthTable(data.readString(), data.readString(), com.android.internal.telephony.euicc.IGetRulesAuthTableCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        getEuiccChallenge(data.readString(), data.readString(), com.android.internal.telephony.euicc.IGetEuiccChallengeCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        getEuiccInfo1(data.readString(), data.readString(), com.android.internal.telephony.euicc.IGetEuiccInfo1Callback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        getEuiccInfo2(data.readString(), data.readString(), com.android.internal.telephony.euicc.IGetEuiccInfo2Callback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        authenticateServer(data.readString(), data.readString(), data.readString(), data.createByteArray(), data.createByteArray(), data.createByteArray(), data.createByteArray(), com.android.internal.telephony.euicc.IAuthenticateServerCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        prepareDownload(data.readString(), data.readString(), data.createByteArray(), data.createByteArray(), data.createByteArray(), data.createByteArray(), com.android.internal.telephony.euicc.IPrepareDownloadCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        loadBoundProfilePackage(data.readString(), data.readString(), data.createByteArray(), com.android.internal.telephony.euicc.ILoadBoundProfilePackageCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        cancelSession(data.readString(), data.readString(), data.createByteArray(), data.readInt(), com.android.internal.telephony.euicc.ICancelSessionCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        listNotifications(data.readString(), data.readString(), data.readInt(), com.android.internal.telephony.euicc.IListNotificationsCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        retrieveNotificationList(data.readString(), data.readString(), data.readInt(), com.android.internal.telephony.euicc.IRetrieveNotificationListCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        retrieveNotification(data.readString(), data.readString(), data.readInt(), com.android.internal.telephony.euicc.IRetrieveNotificationCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        removeNotificationFromList(data.readString(), data.readString(), data.readInt(), com.android.internal.telephony.euicc.IRemoveNotificationFromListCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IEuiccCardController impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IEuiccCardController getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void authenticateServer(String str, String str2, String str3, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, IAuthenticateServerCallback iAuthenticateServerCallback) throws RemoteException;

    void cancelSession(String str, String str2, byte[] bArr, int i, ICancelSessionCallback iCancelSessionCallback) throws RemoteException;

    void deleteProfile(String str, String str2, String str3, IDeleteProfileCallback iDeleteProfileCallback) throws RemoteException;

    void disableProfile(String str, String str2, String str3, boolean z, IDisableProfileCallback iDisableProfileCallback) throws RemoteException;

    void getAllProfiles(String str, String str2, IGetAllProfilesCallback iGetAllProfilesCallback) throws RemoteException;

    void getDefaultSmdpAddress(String str, String str2, IGetDefaultSmdpAddressCallback iGetDefaultSmdpAddressCallback) throws RemoteException;

    void getEuiccChallenge(String str, String str2, IGetEuiccChallengeCallback iGetEuiccChallengeCallback) throws RemoteException;

    void getEuiccInfo1(String str, String str2, IGetEuiccInfo1Callback iGetEuiccInfo1Callback) throws RemoteException;

    void getEuiccInfo2(String str, String str2, IGetEuiccInfo2Callback iGetEuiccInfo2Callback) throws RemoteException;

    void getProfile(String str, String str2, String str3, IGetProfileCallback iGetProfileCallback) throws RemoteException;

    void getRulesAuthTable(String str, String str2, IGetRulesAuthTableCallback iGetRulesAuthTableCallback) throws RemoteException;

    void getSmdsAddress(String str, String str2, IGetSmdsAddressCallback iGetSmdsAddressCallback) throws RemoteException;

    void listNotifications(String str, String str2, int i, IListNotificationsCallback iListNotificationsCallback) throws RemoteException;

    void loadBoundProfilePackage(String str, String str2, byte[] bArr, ILoadBoundProfilePackageCallback iLoadBoundProfilePackageCallback) throws RemoteException;

    void prepareDownload(String str, String str2, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, IPrepareDownloadCallback iPrepareDownloadCallback) throws RemoteException;

    void removeNotificationFromList(String str, String str2, int i, IRemoveNotificationFromListCallback iRemoveNotificationFromListCallback) throws RemoteException;

    void resetMemory(String str, String str2, int i, IResetMemoryCallback iResetMemoryCallback) throws RemoteException;

    void retrieveNotification(String str, String str2, int i, IRetrieveNotificationCallback iRetrieveNotificationCallback) throws RemoteException;

    void retrieveNotificationList(String str, String str2, int i, IRetrieveNotificationListCallback iRetrieveNotificationListCallback) throws RemoteException;

    void setDefaultSmdpAddress(String str, String str2, String str3, ISetDefaultSmdpAddressCallback iSetDefaultSmdpAddressCallback) throws RemoteException;

    void setNickname(String str, String str2, String str3, String str4, ISetNicknameCallback iSetNicknameCallback) throws RemoteException;

    void switchToProfile(String str, String str2, String str3, boolean z, ISwitchToProfileCallback iSwitchToProfileCallback) throws RemoteException;
}
