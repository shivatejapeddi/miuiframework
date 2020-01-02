package com.android.internal.widget;

import android.annotation.UnsupportedAppUsage;
import android.app.PendingIntent;
import android.app.trust.IStrongAuthTracker;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.security.keystore.recovery.KeyChainProtectionParams;
import android.security.keystore.recovery.KeyChainSnapshot;
import android.security.keystore.recovery.RecoveryCertPath;
import android.security.keystore.recovery.WrappedApplicationKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ILockSettings extends IInterface {

    public static class Default implements ILockSettings {
        public void setRawLockPassword(byte[] hash, int userId) throws RemoteException {
        }

        public void setBoolean(String key, boolean value, int userId) throws RemoteException {
        }

        public void setLong(String key, long value, int userId) throws RemoteException {
        }

        public void setString(String key, String value, int userId) throws RemoteException {
        }

        public boolean getBoolean(String key, boolean defaultValue, int userId) throws RemoteException {
            return false;
        }

        public long getLong(String key, long defaultValue, int userId) throws RemoteException {
            return 0;
        }

        public String getString(String key, String defaultValue, int userId) throws RemoteException {
            return null;
        }

        public void setLockCredential(byte[] credential, int type, byte[] savedCredential, int requestedQuality, int userId, boolean allowUntrustedChange) throws RemoteException {
        }

        public void resetKeyStore(int userId) throws RemoteException {
        }

        public VerifyCredentialResponse checkCredential(byte[] credential, int type, int userId, ICheckCredentialProgressCallback progressCallback) throws RemoteException {
            return null;
        }

        public VerifyCredentialResponse verifyCredential(byte[] credential, int type, long challenge, int userId) throws RemoteException {
            return null;
        }

        public VerifyCredentialResponse verifyTiedProfileChallenge(byte[] credential, int type, long challenge, int userId) throws RemoteException {
            return null;
        }

        public boolean checkVoldPassword(int userId) throws RemoteException {
            return false;
        }

        public boolean havePattern(int userId) throws RemoteException {
            return false;
        }

        public boolean havePassword(int userId) throws RemoteException {
            return false;
        }

        public byte[] getHashFactor(byte[] currentCredential, int userId) throws RemoteException {
            return null;
        }

        public void setSeparateProfileChallengeEnabled(int userId, boolean enabled, byte[] managedUserPassword) throws RemoteException {
        }

        public boolean getSeparateProfileChallengeEnabled(int userId) throws RemoteException {
            return false;
        }

        public void registerStrongAuthTracker(IStrongAuthTracker tracker) throws RemoteException {
        }

        public void unregisterStrongAuthTracker(IStrongAuthTracker tracker) throws RemoteException {
        }

        public void requireStrongAuth(int strongAuthReason, int userId) throws RemoteException {
        }

        public void systemReady() throws RemoteException {
        }

        public void userPresent(int userId) throws RemoteException {
        }

        public int getStrongAuthForUser(int userId) throws RemoteException {
            return 0;
        }

        public boolean hasPendingEscrowToken(int userId) throws RemoteException {
            return false;
        }

        public void initRecoveryServiceWithSigFile(String rootCertificateAlias, byte[] recoveryServiceCertFile, byte[] recoveryServiceSigFile) throws RemoteException {
        }

        public KeyChainSnapshot getKeyChainSnapshot() throws RemoteException {
            return null;
        }

        public String generateKey(String alias) throws RemoteException {
            return null;
        }

        public String generateKeyWithMetadata(String alias, byte[] metadata) throws RemoteException {
            return null;
        }

        public String importKey(String alias, byte[] keyBytes) throws RemoteException {
            return null;
        }

        public String importKeyWithMetadata(String alias, byte[] keyBytes, byte[] metadata) throws RemoteException {
            return null;
        }

        public String getKey(String alias) throws RemoteException {
            return null;
        }

        public void removeKey(String alias) throws RemoteException {
        }

        public void setSnapshotCreatedPendingIntent(PendingIntent intent) throws RemoteException {
        }

        public void setServerParams(byte[] serverParams) throws RemoteException {
        }

        public void setRecoveryStatus(String alias, int status) throws RemoteException {
        }

        public Map getRecoveryStatus() throws RemoteException {
            return null;
        }

        public void setRecoverySecretTypes(int[] secretTypes) throws RemoteException {
        }

        public int[] getRecoverySecretTypes() throws RemoteException {
            return null;
        }

        public byte[] startRecoverySessionWithCertPath(String sessionId, String rootCertificateAlias, RecoveryCertPath verifierCertPath, byte[] vaultParams, byte[] vaultChallenge, List<KeyChainProtectionParams> list) throws RemoteException {
            return null;
        }

        public Map recoverKeyChainSnapshot(String sessionId, byte[] recoveryKeyBlob, List<WrappedApplicationKey> list) throws RemoteException {
            return null;
        }

        public void closeSession(String sessionId) throws RemoteException {
        }

        public void sanitizePassword() throws RemoteException {
        }

        public String getPassword() throws RemoteException {
            return null;
        }

        public void savePrivacyPasswordPattern(String pattern, String filename, int userId) throws RemoteException {
        }

        public boolean checkPrivacyPasswordPattern(String pattern, String filename, int userId) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ILockSettings {
        private static final String DESCRIPTOR = "com.android.internal.widget.ILockSettings";
        static final int TRANSACTION_checkCredential = 10;
        static final int TRANSACTION_checkPrivacyPasswordPattern = 46;
        static final int TRANSACTION_checkVoldPassword = 13;
        static final int TRANSACTION_closeSession = 42;
        static final int TRANSACTION_generateKey = 28;
        static final int TRANSACTION_generateKeyWithMetadata = 29;
        static final int TRANSACTION_getBoolean = 5;
        static final int TRANSACTION_getHashFactor = 16;
        static final int TRANSACTION_getKey = 32;
        static final int TRANSACTION_getKeyChainSnapshot = 27;
        static final int TRANSACTION_getLong = 6;
        static final int TRANSACTION_getPassword = 44;
        static final int TRANSACTION_getRecoverySecretTypes = 39;
        static final int TRANSACTION_getRecoveryStatus = 37;
        static final int TRANSACTION_getSeparateProfileChallengeEnabled = 18;
        static final int TRANSACTION_getString = 7;
        static final int TRANSACTION_getStrongAuthForUser = 24;
        static final int TRANSACTION_hasPendingEscrowToken = 25;
        static final int TRANSACTION_havePassword = 15;
        static final int TRANSACTION_havePattern = 14;
        static final int TRANSACTION_importKey = 30;
        static final int TRANSACTION_importKeyWithMetadata = 31;
        static final int TRANSACTION_initRecoveryServiceWithSigFile = 26;
        static final int TRANSACTION_recoverKeyChainSnapshot = 41;
        static final int TRANSACTION_registerStrongAuthTracker = 19;
        static final int TRANSACTION_removeKey = 33;
        static final int TRANSACTION_requireStrongAuth = 21;
        static final int TRANSACTION_resetKeyStore = 9;
        static final int TRANSACTION_sanitizePassword = 43;
        static final int TRANSACTION_savePrivacyPasswordPattern = 45;
        static final int TRANSACTION_setBoolean = 2;
        static final int TRANSACTION_setLockCredential = 8;
        static final int TRANSACTION_setLong = 3;
        static final int TRANSACTION_setRawLockPassword = 1;
        static final int TRANSACTION_setRecoverySecretTypes = 38;
        static final int TRANSACTION_setRecoveryStatus = 36;
        static final int TRANSACTION_setSeparateProfileChallengeEnabled = 17;
        static final int TRANSACTION_setServerParams = 35;
        static final int TRANSACTION_setSnapshotCreatedPendingIntent = 34;
        static final int TRANSACTION_setString = 4;
        static final int TRANSACTION_startRecoverySessionWithCertPath = 40;
        static final int TRANSACTION_systemReady = 22;
        static final int TRANSACTION_unregisterStrongAuthTracker = 20;
        static final int TRANSACTION_userPresent = 23;
        static final int TRANSACTION_verifyCredential = 11;
        static final int TRANSACTION_verifyTiedProfileChallenge = 12;

        private static class Proxy implements ILockSettings {
            public static ILockSettings sDefaultImpl;
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

            public void setRawLockPassword(byte[] hash, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(hash);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setRawLockPassword(hash, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setBoolean(String key, boolean value, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeInt(value ? 1 : 0);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setBoolean(key, value, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setLong(String key, long value, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeLong(value);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setLong(key, value, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setString(String key, String value, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeString(value);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setString(key, value, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getBoolean(String key, boolean defaultValue, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    boolean _result = true;
                    _data.writeInt(defaultValue ? 1 : 0);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().getBoolean(key, defaultValue, userId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getLong(String key, long defaultValue, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeLong(defaultValue);
                    _data.writeInt(userId);
                    long j = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getLong(key, defaultValue, userId);
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

            public String getString(String key, String defaultValue, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeString(defaultValue);
                    _data.writeInt(userId);
                    String str = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getString(key, defaultValue, userId);
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

            public void setLockCredential(byte[] credential, int type, byte[] savedCredential, int requestedQuality, int userId, boolean allowUntrustedChange) throws RemoteException {
                Throwable th;
                int i;
                byte[] bArr;
                int i2;
                int i3;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeByteArray(credential);
                    } catch (Throwable th2) {
                        th = th2;
                        i = type;
                        bArr = savedCredential;
                        i2 = requestedQuality;
                        i3 = userId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(type);
                        try {
                            _data.writeByteArray(savedCredential);
                            try {
                                _data.writeInt(requestedQuality);
                            } catch (Throwable th3) {
                                th = th3;
                                i3 = userId;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            i2 = requestedQuality;
                            i3 = userId;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        bArr = savedCredential;
                        i2 = requestedQuality;
                        i3 = userId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(userId);
                        _data.writeInt(allowUntrustedChange ? 1 : 0);
                    } catch (Throwable th6) {
                        th = th6;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().setLockCredential(credential, type, savedCredential, requestedQuality, userId, allowUntrustedChange);
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th7) {
                        th = th7;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    byte[] bArr2 = credential;
                    i = type;
                    bArr = savedCredential;
                    i2 = requestedQuality;
                    i3 = userId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void resetKeyStore(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().resetKeyStore(userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public VerifyCredentialResponse checkCredential(byte[] credential, int type, int userId, ICheckCredentialProgressCallback progressCallback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(credential);
                    _data.writeInt(type);
                    _data.writeInt(userId);
                    _data.writeStrongBinder(progressCallback != null ? progressCallback.asBinder() : null);
                    VerifyCredentialResponse verifyCredentialResponse = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        verifyCredentialResponse = Stub.getDefaultImpl();
                        if (verifyCredentialResponse != 0) {
                            verifyCredentialResponse = Stub.getDefaultImpl().checkCredential(credential, type, userId, progressCallback);
                            return verifyCredentialResponse;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        verifyCredentialResponse = (VerifyCredentialResponse) VerifyCredentialResponse.CREATOR.createFromParcel(_reply);
                    } else {
                        verifyCredentialResponse = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return verifyCredentialResponse;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public VerifyCredentialResponse verifyCredential(byte[] credential, int type, long challenge, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(credential);
                    _data.writeInt(type);
                    _data.writeLong(challenge);
                    _data.writeInt(userId);
                    VerifyCredentialResponse verifyCredentialResponse = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        verifyCredentialResponse = Stub.getDefaultImpl();
                        if (verifyCredentialResponse != 0) {
                            verifyCredentialResponse = Stub.getDefaultImpl().verifyCredential(credential, type, challenge, userId);
                            return verifyCredentialResponse;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        verifyCredentialResponse = (VerifyCredentialResponse) VerifyCredentialResponse.CREATOR.createFromParcel(_reply);
                    } else {
                        verifyCredentialResponse = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return verifyCredentialResponse;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public VerifyCredentialResponse verifyTiedProfileChallenge(byte[] credential, int type, long challenge, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(credential);
                    _data.writeInt(type);
                    _data.writeLong(challenge);
                    _data.writeInt(userId);
                    VerifyCredentialResponse verifyCredentialResponse = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        verifyCredentialResponse = Stub.getDefaultImpl();
                        if (verifyCredentialResponse != 0) {
                            verifyCredentialResponse = Stub.getDefaultImpl().verifyTiedProfileChallenge(credential, type, challenge, userId);
                            return verifyCredentialResponse;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        verifyCredentialResponse = (VerifyCredentialResponse) VerifyCredentialResponse.CREATOR.createFromParcel(_reply);
                    } else {
                        verifyCredentialResponse = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return verifyCredentialResponse;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean checkVoldPassword(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().checkVoldPassword(userId);
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

            public boolean havePattern(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().havePattern(userId);
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

            public boolean havePassword(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().havePassword(userId);
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

            public byte[] getHashFactor(byte[] currentCredential, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(currentCredential);
                    _data.writeInt(userId);
                    byte[] bArr = 16;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        bArr = Stub.getDefaultImpl();
                        if (bArr != 0) {
                            bArr = Stub.getDefaultImpl().getHashFactor(currentCredential, userId);
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

            public void setSeparateProfileChallengeEnabled(int userId, boolean enabled, byte[] managedUserPassword) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(enabled ? 1 : 0);
                    _data.writeByteArray(managedUserPassword);
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSeparateProfileChallengeEnabled(userId, enabled, managedUserPassword);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getSeparateProfileChallengeEnabled(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getSeparateProfileChallengeEnabled(userId);
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

            public void registerStrongAuthTracker(IStrongAuthTracker tracker) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(tracker != null ? tracker.asBinder() : null);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerStrongAuthTracker(tracker);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterStrongAuthTracker(IStrongAuthTracker tracker) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(tracker != null ? tracker.asBinder() : null);
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterStrongAuthTracker(tracker);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requireStrongAuth(int strongAuthReason, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(strongAuthReason);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requireStrongAuth(strongAuthReason, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void systemReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().systemReady();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void userPresent(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().userPresent(userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getStrongAuthForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    int i = 24;
                    if (!this.mRemote.transact(24, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getStrongAuthForUser(userId);
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

            public boolean hasPendingEscrowToken(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(25, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasPendingEscrowToken(userId);
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

            public void initRecoveryServiceWithSigFile(String rootCertificateAlias, byte[] recoveryServiceCertFile, byte[] recoveryServiceSigFile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(rootCertificateAlias);
                    _data.writeByteArray(recoveryServiceCertFile);
                    _data.writeByteArray(recoveryServiceSigFile);
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().initRecoveryServiceWithSigFile(rootCertificateAlias, recoveryServiceCertFile, recoveryServiceSigFile);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public KeyChainSnapshot getKeyChainSnapshot() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    KeyChainSnapshot keyChainSnapshot = 27;
                    if (!this.mRemote.transact(27, _data, _reply, 0)) {
                        keyChainSnapshot = Stub.getDefaultImpl();
                        if (keyChainSnapshot != 0) {
                            keyChainSnapshot = Stub.getDefaultImpl().getKeyChainSnapshot();
                            return keyChainSnapshot;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        keyChainSnapshot = (KeyChainSnapshot) KeyChainSnapshot.CREATOR.createFromParcel(_reply);
                    } else {
                        keyChainSnapshot = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return keyChainSnapshot;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String generateKey(String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    String str = 28;
                    if (!this.mRemote.transact(28, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().generateKey(alias);
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

            public String generateKeyWithMetadata(String alias, byte[] metadata) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeByteArray(metadata);
                    String str = 29;
                    if (!this.mRemote.transact(29, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().generateKeyWithMetadata(alias, metadata);
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

            public String importKey(String alias, byte[] keyBytes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeByteArray(keyBytes);
                    String str = 30;
                    if (!this.mRemote.transact(30, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().importKey(alias, keyBytes);
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

            public String importKeyWithMetadata(String alias, byte[] keyBytes, byte[] metadata) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeByteArray(keyBytes);
                    _data.writeByteArray(metadata);
                    String str = 31;
                    if (!this.mRemote.transact(31, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().importKeyWithMetadata(alias, keyBytes, metadata);
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

            public String getKey(String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    String str = 32;
                    if (!this.mRemote.transact(32, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getKey(alias);
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

            public void removeKey(String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    if (this.mRemote.transact(33, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeKey(alias);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSnapshotCreatedPendingIntent(PendingIntent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(34, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSnapshotCreatedPendingIntent(intent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setServerParams(byte[] serverParams) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(serverParams);
                    if (this.mRemote.transact(35, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setServerParams(serverParams);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setRecoveryStatus(String alias, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeInt(status);
                    if (this.mRemote.transact(36, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setRecoveryStatus(alias, status);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Map getRecoveryStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Map map = 37;
                    if (!this.mRemote.transact(37, _data, _reply, 0)) {
                        map = Stub.getDefaultImpl();
                        if (map != 0) {
                            map = Stub.getDefaultImpl().getRecoveryStatus();
                            return map;
                        }
                    }
                    _reply.readException();
                    map = getClass().getClassLoader();
                    HashMap _result = _reply.readHashMap(map);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setRecoverySecretTypes(int[] secretTypes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(secretTypes);
                    if (this.mRemote.transact(38, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setRecoverySecretTypes(secretTypes);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getRecoverySecretTypes() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int[] iArr = 39;
                    if (!this.mRemote.transact(39, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != 0) {
                            iArr = Stub.getDefaultImpl().getRecoverySecretTypes();
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

            public byte[] startRecoverySessionWithCertPath(String sessionId, String rootCertificateAlias, RecoveryCertPath verifierCertPath, byte[] vaultParams, byte[] vaultChallenge, List<KeyChainProtectionParams> secrets) throws RemoteException {
                Throwable th;
                byte[] bArr;
                byte[] bArr2;
                List<KeyChainProtectionParams> list;
                String str;
                RecoveryCertPath recoveryCertPath = verifierCertPath;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(sessionId);
                        try {
                            _data.writeString(rootCertificateAlias);
                            if (recoveryCertPath != null) {
                                _data.writeInt(1);
                                recoveryCertPath.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            bArr = vaultParams;
                            bArr2 = vaultChallenge;
                            list = secrets;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        str = rootCertificateAlias;
                        bArr = vaultParams;
                        bArr2 = vaultChallenge;
                        list = secrets;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeByteArray(vaultParams);
                        try {
                            _data.writeByteArray(vaultChallenge);
                        } catch (Throwable th4) {
                            th = th4;
                            list = secrets;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeTypedList(secrets);
                            if (this.mRemote.transact(40, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                byte[] _result = _reply.createByteArray();
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            byte[] startRecoverySessionWithCertPath = Stub.getDefaultImpl().startRecoverySessionWithCertPath(sessionId, rootCertificateAlias, verifierCertPath, vaultParams, vaultChallenge, secrets);
                            _reply.recycle();
                            _data.recycle();
                            return startRecoverySessionWithCertPath;
                        } catch (Throwable th5) {
                            th = th5;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        bArr2 = vaultChallenge;
                        list = secrets;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str2 = sessionId;
                    str = rootCertificateAlias;
                    bArr = vaultParams;
                    bArr2 = vaultChallenge;
                    list = secrets;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public Map recoverKeyChainSnapshot(String sessionId, byte[] recoveryKeyBlob, List<WrappedApplicationKey> applicationKeys) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(sessionId);
                    _data.writeByteArray(recoveryKeyBlob);
                    _data.writeTypedList(applicationKeys);
                    Map map = 41;
                    if (!this.mRemote.transact(41, _data, _reply, 0)) {
                        map = Stub.getDefaultImpl();
                        if (map != 0) {
                            map = Stub.getDefaultImpl().recoverKeyChainSnapshot(sessionId, recoveryKeyBlob, applicationKeys);
                            return map;
                        }
                    }
                    _reply.readException();
                    map = getClass().getClassLoader();
                    HashMap _result = _reply.readHashMap(map);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void closeSession(String sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(sessionId);
                    if (this.mRemote.transact(42, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().closeSession(sessionId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sanitizePassword() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(43, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sanitizePassword();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getPassword() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 44;
                    if (!this.mRemote.transact(44, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getPassword();
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

            public void savePrivacyPasswordPattern(String pattern, String filename, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pattern);
                    _data.writeString(filename);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(45, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().savePrivacyPasswordPattern(pattern, filename, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean checkPrivacyPasswordPattern(String pattern, String filename, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pattern);
                    _data.writeString(filename);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(46, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().checkPrivacyPasswordPattern(pattern, filename, userId);
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

        public static ILockSettings asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ILockSettings)) {
                return new Proxy(obj);
            }
            return (ILockSettings) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setRawLockPassword";
                case 2:
                    return "setBoolean";
                case 3:
                    return "setLong";
                case 4:
                    return "setString";
                case 5:
                    return "getBoolean";
                case 6:
                    return "getLong";
                case 7:
                    return "getString";
                case 8:
                    return "setLockCredential";
                case 9:
                    return "resetKeyStore";
                case 10:
                    return "checkCredential";
                case 11:
                    return "verifyCredential";
                case 12:
                    return "verifyTiedProfileChallenge";
                case 13:
                    return "checkVoldPassword";
                case 14:
                    return "havePattern";
                case 15:
                    return "havePassword";
                case 16:
                    return "getHashFactor";
                case 17:
                    return "setSeparateProfileChallengeEnabled";
                case 18:
                    return "getSeparateProfileChallengeEnabled";
                case 19:
                    return "registerStrongAuthTracker";
                case 20:
                    return "unregisterStrongAuthTracker";
                case 21:
                    return "requireStrongAuth";
                case 22:
                    return "systemReady";
                case 23:
                    return "userPresent";
                case 24:
                    return "getStrongAuthForUser";
                case 25:
                    return "hasPendingEscrowToken";
                case 26:
                    return "initRecoveryServiceWithSigFile";
                case 27:
                    return "getKeyChainSnapshot";
                case 28:
                    return "generateKey";
                case 29:
                    return "generateKeyWithMetadata";
                case 30:
                    return "importKey";
                case 31:
                    return "importKeyWithMetadata";
                case 32:
                    return "getKey";
                case 33:
                    return "removeKey";
                case 34:
                    return "setSnapshotCreatedPendingIntent";
                case 35:
                    return "setServerParams";
                case 36:
                    return "setRecoveryStatus";
                case 37:
                    return "getRecoveryStatus";
                case 38:
                    return "setRecoverySecretTypes";
                case 39:
                    return "getRecoverySecretTypes";
                case 40:
                    return "startRecoverySessionWithCertPath";
                case 41:
                    return "recoverKeyChainSnapshot";
                case 42:
                    return "closeSession";
                case 43:
                    return "sanitizePassword";
                case 44:
                    return "getPassword";
                case 45:
                    return "savePrivacyPasswordPattern";
                case 46:
                    return "checkPrivacyPasswordPattern";
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
                boolean _arg5 = false;
                String _arg0;
                boolean _result;
                String _result2;
                VerifyCredentialResponse _result3;
                boolean _result4;
                String _result5;
                String _result6;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        setRawLockPassword(data.createByteArray(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg5 = true;
                        }
                        setBoolean(_arg0, _arg5, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        setLong(data.readString(), data.readLong(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        setString(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg5 = true;
                        }
                        _result = getBoolean(_arg0, _arg5, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        long _result7 = getLong(data.readString(), data.readLong(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeLong(_result7);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        _result2 = getString(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_result2);
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        byte[] _arg02 = data.createByteArray();
                        int _arg1 = data.readInt();
                        byte[] _arg2 = data.createByteArray();
                        int _arg3 = data.readInt();
                        int _arg4 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg5 = true;
                        }
                        setLockCredential(_arg02, _arg1, _arg2, _arg3, _arg4, _arg5);
                        reply.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        resetKeyStore(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        VerifyCredentialResponse _result8 = checkCredential(data.createByteArray(), data.readInt(), data.readInt(), com.android.internal.widget.ICheckCredentialProgressCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        if (_result8 != null) {
                            parcel2.writeInt(1);
                            _result8.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        _result3 = verifyCredential(data.createByteArray(), data.readInt(), data.readLong(), data.readInt());
                        reply.writeNoException();
                        if (_result3 != null) {
                            parcel2.writeInt(1);
                            _result3.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        _result3 = verifyTiedProfileChallenge(data.createByteArray(), data.readInt(), data.readLong(), data.readInt());
                        reply.writeNoException();
                        if (_result3 != null) {
                            parcel2.writeInt(1);
                            _result3.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        _result4 = checkVoldPassword(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        _result4 = havePattern(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        _result4 = havePassword(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        byte[] _result9 = getHashFactor(data.createByteArray(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeByteArray(_result9);
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        int _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg5 = true;
                        }
                        setSeparateProfileChallengeEnabled(_arg03, _arg5, data.createByteArray());
                        reply.writeNoException();
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        _result4 = getSeparateProfileChallengeEnabled(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        registerStrongAuthTracker(android.app.trust.IStrongAuthTracker.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        unregisterStrongAuthTracker(android.app.trust.IStrongAuthTracker.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        requireStrongAuth(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        systemReady();
                        reply.writeNoException();
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        userPresent(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        int _result10 = getStrongAuthForUser(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result10);
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        _result4 = hasPendingEscrowToken(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        initRecoveryServiceWithSigFile(data.readString(), data.createByteArray(), data.createByteArray());
                        reply.writeNoException();
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        KeyChainSnapshot _result11 = getKeyChainSnapshot();
                        reply.writeNoException();
                        if (_result11 != null) {
                            parcel2.writeInt(1);
                            _result11.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        _result5 = generateKey(data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result5);
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        _result6 = generateKeyWithMetadata(data.readString(), data.createByteArray());
                        reply.writeNoException();
                        parcel2.writeString(_result6);
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        _result6 = importKey(data.readString(), data.createByteArray());
                        reply.writeNoException();
                        parcel2.writeString(_result6);
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        _result2 = importKeyWithMetadata(data.readString(), data.createByteArray(), data.createByteArray());
                        reply.writeNoException();
                        parcel2.writeString(_result2);
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        _result5 = getKey(data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result5);
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        removeKey(data.readString());
                        reply.writeNoException();
                        return true;
                    case 34:
                        PendingIntent _arg04;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        setSnapshotCreatedPendingIntent(_arg04);
                        reply.writeNoException();
                        return true;
                    case 35:
                        parcel.enforceInterface(descriptor);
                        setServerParams(data.createByteArray());
                        reply.writeNoException();
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        setRecoveryStatus(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        Map _result12 = getRecoveryStatus();
                        reply.writeNoException();
                        parcel2.writeMap(_result12);
                        return true;
                    case 38:
                        parcel.enforceInterface(descriptor);
                        setRecoverySecretTypes(data.createIntArray());
                        reply.writeNoException();
                        return true;
                    case 39:
                        parcel.enforceInterface(descriptor);
                        int[] _result13 = getRecoverySecretTypes();
                        reply.writeNoException();
                        parcel2.writeIntArray(_result13);
                        return true;
                    case 40:
                        RecoveryCertPath _arg22;
                        parcel.enforceInterface(descriptor);
                        String _arg05 = data.readString();
                        String _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            _arg22 = (RecoveryCertPath) RecoveryCertPath.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        byte[] _result14 = startRecoverySessionWithCertPath(_arg05, _arg12, _arg22, data.createByteArray(), data.createByteArray(), parcel.createTypedArrayList(KeyChainProtectionParams.CREATOR));
                        reply.writeNoException();
                        parcel2.writeByteArray(_result14);
                        return true;
                    case 41:
                        parcel.enforceInterface(descriptor);
                        Map _result15 = recoverKeyChainSnapshot(data.readString(), data.createByteArray(), parcel.createTypedArrayList(WrappedApplicationKey.CREATOR));
                        reply.writeNoException();
                        parcel2.writeMap(_result15);
                        return true;
                    case 42:
                        parcel.enforceInterface(descriptor);
                        closeSession(data.readString());
                        reply.writeNoException();
                        return true;
                    case 43:
                        parcel.enforceInterface(descriptor);
                        sanitizePassword();
                        reply.writeNoException();
                        return true;
                    case 44:
                        parcel.enforceInterface(descriptor);
                        _arg0 = getPassword();
                        reply.writeNoException();
                        parcel2.writeString(_arg0);
                        return true;
                    case 45:
                        parcel.enforceInterface(descriptor);
                        savePrivacyPasswordPattern(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 46:
                        parcel.enforceInterface(descriptor);
                        _result = checkPrivacyPasswordPattern(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(ILockSettings impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ILockSettings getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    VerifyCredentialResponse checkCredential(byte[] bArr, int i, int i2, ICheckCredentialProgressCallback iCheckCredentialProgressCallback) throws RemoteException;

    boolean checkPrivacyPasswordPattern(String str, String str2, int i) throws RemoteException;

    boolean checkVoldPassword(int i) throws RemoteException;

    void closeSession(String str) throws RemoteException;

    String generateKey(String str) throws RemoteException;

    String generateKeyWithMetadata(String str, byte[] bArr) throws RemoteException;

    @UnsupportedAppUsage
    boolean getBoolean(String str, boolean z, int i) throws RemoteException;

    byte[] getHashFactor(byte[] bArr, int i) throws RemoteException;

    String getKey(String str) throws RemoteException;

    KeyChainSnapshot getKeyChainSnapshot() throws RemoteException;

    @UnsupportedAppUsage
    long getLong(String str, long j, int i) throws RemoteException;

    String getPassword() throws RemoteException;

    int[] getRecoverySecretTypes() throws RemoteException;

    Map getRecoveryStatus() throws RemoteException;

    boolean getSeparateProfileChallengeEnabled(int i) throws RemoteException;

    @UnsupportedAppUsage
    String getString(String str, String str2, int i) throws RemoteException;

    int getStrongAuthForUser(int i) throws RemoteException;

    boolean hasPendingEscrowToken(int i) throws RemoteException;

    @UnsupportedAppUsage
    boolean havePassword(int i) throws RemoteException;

    @UnsupportedAppUsage
    boolean havePattern(int i) throws RemoteException;

    String importKey(String str, byte[] bArr) throws RemoteException;

    String importKeyWithMetadata(String str, byte[] bArr, byte[] bArr2) throws RemoteException;

    void initRecoveryServiceWithSigFile(String str, byte[] bArr, byte[] bArr2) throws RemoteException;

    Map recoverKeyChainSnapshot(String str, byte[] bArr, List<WrappedApplicationKey> list) throws RemoteException;

    void registerStrongAuthTracker(IStrongAuthTracker iStrongAuthTracker) throws RemoteException;

    void removeKey(String str) throws RemoteException;

    void requireStrongAuth(int i, int i2) throws RemoteException;

    void resetKeyStore(int i) throws RemoteException;

    void sanitizePassword() throws RemoteException;

    void savePrivacyPasswordPattern(String str, String str2, int i) throws RemoteException;

    @UnsupportedAppUsage
    void setBoolean(String str, boolean z, int i) throws RemoteException;

    void setLockCredential(byte[] bArr, int i, byte[] bArr2, int i2, int i3, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void setLong(String str, long j, int i) throws RemoteException;

    void setRawLockPassword(byte[] bArr, int i) throws RemoteException;

    void setRecoverySecretTypes(int[] iArr) throws RemoteException;

    void setRecoveryStatus(String str, int i) throws RemoteException;

    void setSeparateProfileChallengeEnabled(int i, boolean z, byte[] bArr) throws RemoteException;

    void setServerParams(byte[] bArr) throws RemoteException;

    void setSnapshotCreatedPendingIntent(PendingIntent pendingIntent) throws RemoteException;

    @UnsupportedAppUsage
    void setString(String str, String str2, int i) throws RemoteException;

    byte[] startRecoverySessionWithCertPath(String str, String str2, RecoveryCertPath recoveryCertPath, byte[] bArr, byte[] bArr2, List<KeyChainProtectionParams> list) throws RemoteException;

    void systemReady() throws RemoteException;

    void unregisterStrongAuthTracker(IStrongAuthTracker iStrongAuthTracker) throws RemoteException;

    void userPresent(int i) throws RemoteException;

    VerifyCredentialResponse verifyCredential(byte[] bArr, int i, long j, int i2) throws RemoteException;

    VerifyCredentialResponse verifyTiedProfileChallenge(byte[] bArr, int i, long j, int i2) throws RemoteException;
}
