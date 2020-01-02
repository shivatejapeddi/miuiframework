package android.security.keystore;

import android.app.slice.Slice;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.KeymasterBlob;
import java.util.ArrayList;
import java.util.List;

public interface IKeystoreService extends IInterface {

    public static class Default implements IKeystoreService {
        public int getState(int userId) throws RemoteException {
            return 0;
        }

        public byte[] get(String name, int uid) throws RemoteException {
            return null;
        }

        public int insert(String name, byte[] item, int uid, int flags) throws RemoteException {
            return 0;
        }

        public int del(String name, int uid) throws RemoteException {
            return 0;
        }

        public int exist(String name, int uid) throws RemoteException {
            return 0;
        }

        public String[] list(String namePrefix, int uid) throws RemoteException {
            return null;
        }

        public int reset() throws RemoteException {
            return 0;
        }

        public int onUserPasswordChanged(int userId, String newPassword) throws RemoteException {
            return 0;
        }

        public int lock(int userId) throws RemoteException {
            return 0;
        }

        public int unlock(int userId, String userPassword) throws RemoteException {
            return 0;
        }

        public int isEmpty(int userId) throws RemoteException {
            return 0;
        }

        public String grant(String name, int granteeUid) throws RemoteException {
            return null;
        }

        public int ungrant(String name, int granteeUid) throws RemoteException {
            return 0;
        }

        public long getmtime(String name, int uid) throws RemoteException {
            return 0;
        }

        public int is_hardware_backed(String string) throws RemoteException {
            return 0;
        }

        public int clear_uid(long uid) throws RemoteException {
            return 0;
        }

        public int addRngEntropy(IKeystoreResponseCallback cb, byte[] data, int flags) throws RemoteException {
            return 0;
        }

        public int generateKey(IKeystoreKeyCharacteristicsCallback cb, String alias, KeymasterArguments arguments, byte[] entropy, int uid, int flags) throws RemoteException {
            return 0;
        }

        public int getKeyCharacteristics(IKeystoreKeyCharacteristicsCallback cb, String alias, KeymasterBlob clientId, KeymasterBlob appData, int uid) throws RemoteException {
            return 0;
        }

        public int importKey(IKeystoreKeyCharacteristicsCallback cb, String alias, KeymasterArguments arguments, int format, byte[] keyData, int uid, int flags) throws RemoteException {
            return 0;
        }

        public int exportKey(IKeystoreExportKeyCallback cb, String alias, int format, KeymasterBlob clientId, KeymasterBlob appData, int uid) throws RemoteException {
            return 0;
        }

        public int begin(IKeystoreOperationResultCallback cb, IBinder appToken, String alias, int purpose, boolean pruneable, KeymasterArguments params, byte[] entropy, int uid) throws RemoteException {
            return 0;
        }

        public int update(IKeystoreOperationResultCallback cb, IBinder token, KeymasterArguments params, byte[] input) throws RemoteException {
            return 0;
        }

        public int finish(IKeystoreOperationResultCallback cb, IBinder token, KeymasterArguments params, byte[] signature, byte[] entropy) throws RemoteException {
            return 0;
        }

        public int abort(IKeystoreResponseCallback cb, IBinder token) throws RemoteException {
            return 0;
        }

        public int addAuthToken(byte[] authToken) throws RemoteException {
            return 0;
        }

        public int onUserAdded(int userId, int parentId) throws RemoteException {
            return 0;
        }

        public int onUserRemoved(int userId) throws RemoteException {
            return 0;
        }

        public int attestKey(IKeystoreCertificateChainCallback cb, String alias, KeymasterArguments params) throws RemoteException {
            return 0;
        }

        public int attestDeviceIds(IKeystoreCertificateChainCallback cb, KeymasterArguments params) throws RemoteException {
            return 0;
        }

        public int onDeviceOffBody() throws RemoteException {
            return 0;
        }

        public int importWrappedKey(IKeystoreKeyCharacteristicsCallback cb, String wrappedKeyAlias, byte[] wrappedKey, String wrappingKeyAlias, byte[] maskingKey, KeymasterArguments arguments, long rootSid, long fingerprintSid) throws RemoteException {
            return 0;
        }

        public int presentConfirmationPrompt(IBinder listener, String promptText, byte[] extraData, String locale, int uiOptionsAsFlags) throws RemoteException {
            return 0;
        }

        public int cancelConfirmationPrompt(IBinder listener) throws RemoteException {
            return 0;
        }

        public boolean isConfirmationPromptSupported() throws RemoteException {
            return false;
        }

        public int onKeyguardVisibilityChanged(boolean isShowing, int userId) throws RemoteException {
            return 0;
        }

        public int listUidsOfAuthBoundKeys(List<String> list) throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IKeystoreService {
        private static final String DESCRIPTOR = "android.security.keystore.IKeystoreService";
        static final int TRANSACTION_abort = 25;
        static final int TRANSACTION_addAuthToken = 26;
        static final int TRANSACTION_addRngEntropy = 17;
        static final int TRANSACTION_attestDeviceIds = 30;
        static final int TRANSACTION_attestKey = 29;
        static final int TRANSACTION_begin = 22;
        static final int TRANSACTION_cancelConfirmationPrompt = 34;
        static final int TRANSACTION_clear_uid = 16;
        static final int TRANSACTION_del = 4;
        static final int TRANSACTION_exist = 5;
        static final int TRANSACTION_exportKey = 21;
        static final int TRANSACTION_finish = 24;
        static final int TRANSACTION_generateKey = 18;
        static final int TRANSACTION_get = 2;
        static final int TRANSACTION_getKeyCharacteristics = 19;
        static final int TRANSACTION_getState = 1;
        static final int TRANSACTION_getmtime = 14;
        static final int TRANSACTION_grant = 12;
        static final int TRANSACTION_importKey = 20;
        static final int TRANSACTION_importWrappedKey = 32;
        static final int TRANSACTION_insert = 3;
        static final int TRANSACTION_isConfirmationPromptSupported = 35;
        static final int TRANSACTION_isEmpty = 11;
        static final int TRANSACTION_is_hardware_backed = 15;
        static final int TRANSACTION_list = 6;
        static final int TRANSACTION_listUidsOfAuthBoundKeys = 37;
        static final int TRANSACTION_lock = 9;
        static final int TRANSACTION_onDeviceOffBody = 31;
        static final int TRANSACTION_onKeyguardVisibilityChanged = 36;
        static final int TRANSACTION_onUserAdded = 27;
        static final int TRANSACTION_onUserPasswordChanged = 8;
        static final int TRANSACTION_onUserRemoved = 28;
        static final int TRANSACTION_presentConfirmationPrompt = 33;
        static final int TRANSACTION_reset = 7;
        static final int TRANSACTION_ungrant = 13;
        static final int TRANSACTION_unlock = 10;
        static final int TRANSACTION_update = 23;

        private static class Proxy implements IKeystoreService {
            public static IKeystoreService sDefaultImpl;
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

            public int getState(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    int i = 1;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getState(userId);
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

            public byte[] get(String name, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(uid);
                    byte[] bArr = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        bArr = Stub.getDefaultImpl();
                        if (bArr != 0) {
                            bArr = Stub.getDefaultImpl().get(name, uid);
                            return bArr;
                        }
                    }
                    _reply.readException();
                    bArr = _reply.createByteArray();
                    byte[] _result = bArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int insert(String name, byte[] item, int uid, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeByteArray(item);
                    _data.writeInt(uid);
                    _data.writeInt(flags);
                    int i = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().insert(name, item, uid, flags);
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

            public int del(String name, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(uid);
                    int i = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().del(name, uid);
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

            public int exist(String name, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(uid);
                    int i = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().exist(name, uid);
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

            public String[] list(String namePrefix, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(namePrefix);
                    _data.writeInt(uid);
                    String[] strArr = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().list(namePrefix, uid);
                            return strArr;
                        }
                    }
                    _reply.readException();
                    strArr = _reply.createStringArray();
                    String[] _result = strArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int reset() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().reset();
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

            public int onUserPasswordChanged(int userId, String newPassword) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(newPassword);
                    int i = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().onUserPasswordChanged(userId, newPassword);
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

            public int lock(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    int i = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().lock(userId);
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

            public int unlock(int userId, String userPassword) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(userPassword);
                    int i = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().unlock(userId, userPassword);
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

            public int isEmpty(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    int i = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().isEmpty(userId);
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

            public String grant(String name, int granteeUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(granteeUid);
                    String str = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().grant(name, granteeUid);
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

            public int ungrant(String name, int granteeUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(granteeUid);
                    int i = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().ungrant(name, granteeUid);
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

            public long getmtime(String name, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(uid);
                    long j = 14;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getmtime(name, uid);
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

            public int is_hardware_backed(String string) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(string);
                    int i = 15;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().is_hardware_backed(string);
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

            public int clear_uid(long uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(uid);
                    int i = 16;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().clear_uid(uid);
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

            public int addRngEntropy(IKeystoreResponseCallback cb, byte[] data, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    _data.writeByteArray(data);
                    _data.writeInt(flags);
                    int i = 17;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().addRngEntropy(cb, data, flags);
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

            public int generateKey(IKeystoreKeyCharacteristicsCallback cb, String alias, KeymasterArguments arguments, byte[] entropy, int uid, int flags) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                byte[] bArr;
                KeymasterArguments keymasterArguments = arguments;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    try {
                        _data.writeString(alias);
                        if (keymasterArguments != null) {
                            _data.writeInt(1);
                            keymasterArguments.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeByteArray(entropy);
                        } catch (Throwable th2) {
                            th = th2;
                            i = uid;
                            i2 = flags;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(uid);
                        } catch (Throwable th3) {
                            th = th3;
                            i2 = flags;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        bArr = entropy;
                        i = uid;
                        i2 = flags;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(flags);
                        try {
                            if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                int _result = _reply.readInt();
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            int generateKey = Stub.getDefaultImpl().generateKey(cb, alias, arguments, entropy, uid, flags);
                            _reply.recycle();
                            _data.recycle();
                            return generateKey;
                        } catch (Throwable th5) {
                            th = th5;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str = alias;
                    bArr = entropy;
                    i = uid;
                    i2 = flags;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public int getKeyCharacteristics(IKeystoreKeyCharacteristicsCallback cb, String alias, KeymasterBlob clientId, KeymasterBlob appData, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    _data.writeString(alias);
                    int i = 0;
                    if (clientId != null) {
                        _data.writeInt(1);
                        clientId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (appData != null) {
                        _data.writeInt(1);
                        appData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(uid);
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getKeyCharacteristics(cb, alias, clientId, appData, uid);
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

            public int importKey(IKeystoreKeyCharacteristicsCallback cb, String alias, KeymasterArguments arguments, int format, byte[] keyData, int uid, int flags) throws RemoteException {
                Throwable th;
                byte[] bArr;
                int i;
                int i2;
                KeymasterArguments keymasterArguments = arguments;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    try {
                        _data.writeString(alias);
                        if (keymasterArguments != null) {
                            _data.writeInt(1);
                            keymasterArguments.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeInt(format);
                        } catch (Throwable th2) {
                            th = th2;
                            bArr = keyData;
                            i = uid;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        i2 = format;
                        bArr = keyData;
                        i = uid;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeByteArray(keyData);
                    } catch (Throwable th4) {
                        th = th4;
                        i = uid;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(uid);
                        _data.writeInt(flags);
                        if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            int _result = _reply.readInt();
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        int importKey = Stub.getDefaultImpl().importKey(cb, alias, arguments, format, keyData, uid, flags);
                        _reply.recycle();
                        _data.recycle();
                        return importKey;
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str = alias;
                    i2 = format;
                    bArr = keyData;
                    i = uid;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public int exportKey(IKeystoreExportKeyCallback cb, String alias, int format, KeymasterBlob clientId, KeymasterBlob appData, int uid) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                KeymasterBlob keymasterBlob = clientId;
                KeymasterBlob keymasterBlob2 = appData;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    try {
                        _data.writeString(alias);
                    } catch (Throwable th2) {
                        th = th2;
                        i = format;
                        i2 = uid;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(format);
                        if (keymasterBlob != null) {
                            _data.writeInt(1);
                            keymasterBlob.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (keymasterBlob2 != null) {
                            _data.writeInt(1);
                            keymasterBlob2.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        i2 = uid;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(uid);
                        try {
                            if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                int _result = _reply.readInt();
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            int exportKey = Stub.getDefaultImpl().exportKey(cb, alias, format, clientId, appData, uid);
                            _reply.recycle();
                            _data.recycle();
                            return exportKey;
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str = alias;
                    i = format;
                    i2 = uid;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public int begin(IKeystoreOperationResultCallback cb, IBinder appToken, String alias, int purpose, boolean pruneable, KeymasterArguments params, byte[] entropy, int uid) throws RemoteException {
                Throwable th;
                String str;
                int i;
                KeymasterArguments keymasterArguments = params;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    try {
                        _data.writeStrongBinder(appToken);
                    } catch (Throwable th2) {
                        th = th2;
                        str = alias;
                        i = purpose;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(alias);
                        try {
                            _data.writeInt(purpose);
                            _data.writeInt(pruneable ? 1 : 0);
                            if (keymasterArguments != null) {
                                _data.writeInt(1);
                                keymasterArguments.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            _data.writeByteArray(entropy);
                            _data.writeInt(uid);
                            if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                int _result = _reply.readInt();
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            int begin = Stub.getDefaultImpl().begin(cb, appToken, alias, purpose, pruneable, params, entropy, uid);
                            _reply.recycle();
                            _data.recycle();
                            return begin;
                        } catch (Throwable th3) {
                            th = th3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        i = purpose;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    IBinder iBinder = appToken;
                    str = alias;
                    i = purpose;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public int update(IKeystoreOperationResultCallback cb, IBinder token, KeymasterArguments params, byte[] input) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    _data.writeStrongBinder(token);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeByteArray(input);
                    int i = this.mRemote;
                    if (!i.transact(23, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().update(cb, token, params, input);
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

            public int finish(IKeystoreOperationResultCallback cb, IBinder token, KeymasterArguments params, byte[] signature, byte[] entropy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    _data.writeStrongBinder(token);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeByteArray(signature);
                    _data.writeByteArray(entropy);
                    int i = this.mRemote;
                    if (!i.transact(24, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().finish(cb, token, params, signature, entropy);
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

            public int abort(IKeystoreResponseCallback cb, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    _data.writeStrongBinder(token);
                    int i = 25;
                    if (!this.mRemote.transact(25, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().abort(cb, token);
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

            public int addAuthToken(byte[] authToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(authToken);
                    int i = 26;
                    if (!this.mRemote.transact(26, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().addAuthToken(authToken);
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

            public int onUserAdded(int userId, int parentId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(parentId);
                    int i = 27;
                    if (!this.mRemote.transact(27, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().onUserAdded(userId, parentId);
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

            public int onUserRemoved(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    int i = 28;
                    if (!this.mRemote.transact(28, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().onUserRemoved(userId);
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

            public int attestKey(IKeystoreCertificateChainCallback cb, String alias, KeymasterArguments params) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    _data.writeString(alias);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(29, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().attestKey(cb, alias, params);
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

            public int attestDeviceIds(IKeystoreCertificateChainCallback cb, KeymasterArguments params) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(30, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().attestDeviceIds(cb, params);
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

            public int onDeviceOffBody() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 31;
                    if (!this.mRemote.transact(31, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().onDeviceOffBody();
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

            public int importWrappedKey(IKeystoreKeyCharacteristicsCallback cb, String wrappedKeyAlias, byte[] wrappedKey, String wrappingKeyAlias, byte[] maskingKey, KeymasterArguments arguments, long rootSid, long fingerprintSid) throws RemoteException {
                Throwable th;
                KeymasterArguments keymasterArguments = arguments;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    try {
                        _data.writeString(wrappedKeyAlias);
                        _data.writeByteArray(wrappedKey);
                        _data.writeString(wrappingKeyAlias);
                        _data.writeByteArray(maskingKey);
                        if (keymasterArguments != null) {
                            _data.writeInt(1);
                            keymasterArguments.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        _data.writeLong(rootSid);
                        _data.writeLong(fingerprintSid);
                        if (this.mRemote.transact(32, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            int _result = _reply.readInt();
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        int importWrappedKey = Stub.getDefaultImpl().importWrappedKey(cb, wrappedKeyAlias, wrappedKey, wrappingKeyAlias, maskingKey, arguments, rootSid, fingerprintSid);
                        _reply.recycle();
                        _data.recycle();
                        return importWrappedKey;
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    String str = wrappedKeyAlias;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public int presentConfirmationPrompt(IBinder listener, String promptText, byte[] extraData, String locale, int uiOptionsAsFlags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener);
                    _data.writeString(promptText);
                    _data.writeByteArray(extraData);
                    _data.writeString(locale);
                    _data.writeInt(uiOptionsAsFlags);
                    int i = 33;
                    if (!this.mRemote.transact(33, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().presentConfirmationPrompt(listener, promptText, extraData, locale, uiOptionsAsFlags);
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

            public int cancelConfirmationPrompt(IBinder listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener);
                    int i = 34;
                    if (!this.mRemote.transact(34, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().cancelConfirmationPrompt(listener);
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

            public boolean isConfirmationPromptSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(35, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isConfirmationPromptSupported();
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

            public int onKeyguardVisibilityChanged(boolean isShowing, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isShowing ? 1 : 0);
                    _data.writeInt(userId);
                    int i = this.mRemote;
                    if (!i.transact(36, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().onKeyguardVisibilityChanged(isShowing, userId);
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

            public int listUidsOfAuthBoundKeys(List<String> uids) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int _result = 37;
                    if (!this.mRemote.transact(37, _data, _reply, 0)) {
                        _result = Stub.getDefaultImpl();
                        if (_result != 0) {
                            _result = Stub.getDefaultImpl().listUidsOfAuthBoundKeys(uids);
                            return _result;
                        }
                    }
                    _reply.readException();
                    _result = _reply.readInt();
                    _reply.readStringList(uids);
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

        public static IKeystoreService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IKeystoreService)) {
                return new Proxy(obj);
            }
            return (IKeystoreService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getState";
                case 2:
                    return "get";
                case 3:
                    return "insert";
                case 4:
                    return "del";
                case 5:
                    return "exist";
                case 6:
                    return Slice.HINT_LIST;
                case 7:
                    return "reset";
                case 8:
                    return "onUserPasswordChanged";
                case 9:
                    return "lock";
                case 10:
                    return "unlock";
                case 11:
                    return "isEmpty";
                case 12:
                    return "grant";
                case 13:
                    return "ungrant";
                case 14:
                    return "getmtime";
                case 15:
                    return "is_hardware_backed";
                case 16:
                    return "clear_uid";
                case 17:
                    return "addRngEntropy";
                case 18:
                    return "generateKey";
                case 19:
                    return "getKeyCharacteristics";
                case 20:
                    return "importKey";
                case 21:
                    return "exportKey";
                case 22:
                    return "begin";
                case 23:
                    return "update";
                case 24:
                    return "finish";
                case 25:
                    return "abort";
                case 26:
                    return "addAuthToken";
                case 27:
                    return "onUserAdded";
                case 28:
                    return "onUserRemoved";
                case 29:
                    return "attestKey";
                case 30:
                    return "attestDeviceIds";
                case 31:
                    return "onDeviceOffBody";
                case 32:
                    return "importWrappedKey";
                case 33:
                    return "presentConfirmationPrompt";
                case 34:
                    return "cancelConfirmationPrompt";
                case 35:
                    return "isConfirmationPromptSupported";
                case 36:
                    return "onKeyguardVisibilityChanged";
                case 37:
                    return "listUidsOfAuthBoundKeys";
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
                boolean _arg0 = false;
                int _result;
                int _result2;
                int _result3;
                int _result4;
                int _result5;
                String _arg1;
                KeymasterArguments _arg2;
                IKeystoreCertificateChainCallback _arg02;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        _result = getState(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        byte[] _result6 = get(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeByteArray(_result6);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        _result2 = insert(data.readString(), data.createByteArray(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        _result3 = del(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _result3 = exist(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        String[] _result7 = list(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeStringArray(_result7);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        _result4 = reset();
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        _result3 = onUserPasswordChanged(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        _result = lock(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        _result3 = unlock(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        _result = isEmpty(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        String _result8 = grant(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_result8);
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        _result3 = ungrant(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        long _result9 = getmtime(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeLong(_result9);
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        _result = is_hardware_backed(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        _result3 = clear_uid(data.readLong());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        _result5 = addRngEntropy(android.security.keystore.IKeystoreResponseCallback.Stub.asInterface(data.readStrongBinder()), data.createByteArray(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 18:
                        KeymasterArguments _arg22;
                        parcel.enforceInterface(descriptor);
                        IKeystoreKeyCharacteristicsCallback _arg03 = android.security.keystore.IKeystoreKeyCharacteristicsCallback.Stub.asInterface(data.readStrongBinder());
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg22 = (KeymasterArguments) KeymasterArguments.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        _result4 = generateKey(_arg03, _arg1, _arg22, data.createByteArray(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 19:
                        KeymasterBlob _arg23;
                        KeymasterBlob _arg3;
                        parcel.enforceInterface(descriptor);
                        IKeystoreKeyCharacteristicsCallback _arg04 = android.security.keystore.IKeystoreKeyCharacteristicsCallback.Stub.asInterface(data.readStrongBinder());
                        String _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            _arg23 = (KeymasterBlob) KeymasterBlob.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg3 = (KeymasterBlob) KeymasterBlob.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        _result4 = getKeyCharacteristics(_arg04, _arg12, _arg23, _arg3, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 20:
                        KeymasterArguments _arg24;
                        parcel.enforceInterface(descriptor);
                        IKeystoreKeyCharacteristicsCallback _arg05 = android.security.keystore.IKeystoreKeyCharacteristicsCallback.Stub.asInterface(data.readStrongBinder());
                        String _arg13 = data.readString();
                        if (data.readInt() != 0) {
                            _arg24 = (KeymasterArguments) KeymasterArguments.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg24 = null;
                        }
                        _result4 = importKey(_arg05, _arg13, _arg24, data.readInt(), data.createByteArray(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 21:
                        KeymasterBlob _arg32;
                        KeymasterBlob _arg4;
                        parcel.enforceInterface(descriptor);
                        IKeystoreExportKeyCallback _arg06 = android.security.keystore.IKeystoreExportKeyCallback.Stub.asInterface(data.readStrongBinder());
                        _arg1 = data.readString();
                        int _arg25 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg32 = (KeymasterBlob) KeymasterBlob.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg32 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg4 = (KeymasterBlob) KeymasterBlob.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg4 = null;
                        }
                        _result4 = exportKey(_arg06, _arg1, _arg25, _arg32, _arg4, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 22:
                        KeymasterArguments _arg5;
                        parcel.enforceInterface(descriptor);
                        IKeystoreOperationResultCallback _arg07 = android.security.keystore.IKeystoreOperationResultCallback.Stub.asInterface(data.readStrongBinder());
                        IBinder _arg14 = data.readStrongBinder();
                        String _arg26 = data.readString();
                        int _arg33 = data.readInt();
                        boolean _arg42 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg5 = (KeymasterArguments) KeymasterArguments.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        _result4 = begin(_arg07, _arg14, _arg26, _arg33, _arg42, _arg5, data.createByteArray(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        IKeystoreOperationResultCallback _arg08 = android.security.keystore.IKeystoreOperationResultCallback.Stub.asInterface(data.readStrongBinder());
                        IBinder _arg15 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg2 = (KeymasterArguments) KeymasterArguments.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        _result2 = update(_arg08, _arg15, _arg2, data.createByteArray());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 24:
                        KeymasterArguments _arg27;
                        parcel.enforceInterface(descriptor);
                        IKeystoreOperationResultCallback _arg09 = android.security.keystore.IKeystoreOperationResultCallback.Stub.asInterface(data.readStrongBinder());
                        IBinder _arg16 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg27 = (KeymasterArguments) KeymasterArguments.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg27 = null;
                        }
                        _result4 = finish(_arg09, _arg16, _arg27, data.createByteArray(), data.createByteArray());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        _result3 = abort(android.security.keystore.IKeystoreResponseCallback.Stub.asInterface(data.readStrongBinder()), data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        _result = addAuthToken(data.createByteArray());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        _result3 = onUserAdded(data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        _result = onUserRemoved(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        _arg02 = android.security.keystore.IKeystoreCertificateChainCallback.Stub.asInterface(data.readStrongBinder());
                        String _arg17 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (KeymasterArguments) KeymasterArguments.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        _result5 = attestKey(_arg02, _arg17, _arg2);
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 30:
                        KeymasterArguments _arg18;
                        parcel.enforceInterface(descriptor);
                        _arg02 = android.security.keystore.IKeystoreCertificateChainCallback.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg18 = (KeymasterArguments) KeymasterArguments.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg18 = null;
                        }
                        _result3 = attestDeviceIds(_arg02, _arg18);
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        _result4 = onDeviceOffBody();
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 32:
                        KeymasterArguments _arg52;
                        parcel.enforceInterface(descriptor);
                        IKeystoreKeyCharacteristicsCallback _arg010 = android.security.keystore.IKeystoreKeyCharacteristicsCallback.Stub.asInterface(data.readStrongBinder());
                        String _arg19 = data.readString();
                        byte[] _arg28 = data.createByteArray();
                        String _arg34 = data.readString();
                        byte[] _arg43 = data.createByteArray();
                        if (data.readInt() != 0) {
                            _arg52 = (KeymasterArguments) KeymasterArguments.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg52 = null;
                        }
                        _result4 = importWrappedKey(_arg010, _arg19, _arg28, _arg34, _arg43, _arg52, data.readLong(), data.readLong());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        _result4 = presentConfirmationPrompt(data.readStrongBinder(), data.readString(), data.createByteArray(), data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        _result = cancelConfirmationPrompt(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 35:
                        parcel.enforceInterface(descriptor);
                        _arg0 = isConfirmationPromptSupported();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        _result3 = onKeyguardVisibilityChanged(_arg0, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        List<String> _arg011 = new ArrayList();
                        _result = listUidsOfAuthBoundKeys(_arg011);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        parcel2.writeStringList(_arg011);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IKeystoreService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IKeystoreService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    int abort(IKeystoreResponseCallback iKeystoreResponseCallback, IBinder iBinder) throws RemoteException;

    int addAuthToken(byte[] bArr) throws RemoteException;

    int addRngEntropy(IKeystoreResponseCallback iKeystoreResponseCallback, byte[] bArr, int i) throws RemoteException;

    int attestDeviceIds(IKeystoreCertificateChainCallback iKeystoreCertificateChainCallback, KeymasterArguments keymasterArguments) throws RemoteException;

    int attestKey(IKeystoreCertificateChainCallback iKeystoreCertificateChainCallback, String str, KeymasterArguments keymasterArguments) throws RemoteException;

    int begin(IKeystoreOperationResultCallback iKeystoreOperationResultCallback, IBinder iBinder, String str, int i, boolean z, KeymasterArguments keymasterArguments, byte[] bArr, int i2) throws RemoteException;

    int cancelConfirmationPrompt(IBinder iBinder) throws RemoteException;

    int clear_uid(long j) throws RemoteException;

    int del(String str, int i) throws RemoteException;

    int exist(String str, int i) throws RemoteException;

    int exportKey(IKeystoreExportKeyCallback iKeystoreExportKeyCallback, String str, int i, KeymasterBlob keymasterBlob, KeymasterBlob keymasterBlob2, int i2) throws RemoteException;

    int finish(IKeystoreOperationResultCallback iKeystoreOperationResultCallback, IBinder iBinder, KeymasterArguments keymasterArguments, byte[] bArr, byte[] bArr2) throws RemoteException;

    int generateKey(IKeystoreKeyCharacteristicsCallback iKeystoreKeyCharacteristicsCallback, String str, KeymasterArguments keymasterArguments, byte[] bArr, int i, int i2) throws RemoteException;

    byte[] get(String str, int i) throws RemoteException;

    int getKeyCharacteristics(IKeystoreKeyCharacteristicsCallback iKeystoreKeyCharacteristicsCallback, String str, KeymasterBlob keymasterBlob, KeymasterBlob keymasterBlob2, int i) throws RemoteException;

    int getState(int i) throws RemoteException;

    long getmtime(String str, int i) throws RemoteException;

    String grant(String str, int i) throws RemoteException;

    int importKey(IKeystoreKeyCharacteristicsCallback iKeystoreKeyCharacteristicsCallback, String str, KeymasterArguments keymasterArguments, int i, byte[] bArr, int i2, int i3) throws RemoteException;

    int importWrappedKey(IKeystoreKeyCharacteristicsCallback iKeystoreKeyCharacteristicsCallback, String str, byte[] bArr, String str2, byte[] bArr2, KeymasterArguments keymasterArguments, long j, long j2) throws RemoteException;

    int insert(String str, byte[] bArr, int i, int i2) throws RemoteException;

    boolean isConfirmationPromptSupported() throws RemoteException;

    int isEmpty(int i) throws RemoteException;

    int is_hardware_backed(String str) throws RemoteException;

    String[] list(String str, int i) throws RemoteException;

    int listUidsOfAuthBoundKeys(List<String> list) throws RemoteException;

    int lock(int i) throws RemoteException;

    int onDeviceOffBody() throws RemoteException;

    int onKeyguardVisibilityChanged(boolean z, int i) throws RemoteException;

    int onUserAdded(int i, int i2) throws RemoteException;

    int onUserPasswordChanged(int i, String str) throws RemoteException;

    int onUserRemoved(int i) throws RemoteException;

    int presentConfirmationPrompt(IBinder iBinder, String str, byte[] bArr, String str2, int i) throws RemoteException;

    int reset() throws RemoteException;

    int ungrant(String str, int i) throws RemoteException;

    int unlock(int i, String str) throws RemoteException;

    int update(IKeystoreOperationResultCallback iKeystoreOperationResultCallback, IBinder iBinder, KeymasterArguments keymasterArguments, byte[] bArr) throws RemoteException;
}
