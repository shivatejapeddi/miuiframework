package android.accounts;

import android.content.IntentSender;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.UserHandle;
import java.util.HashMap;
import java.util.Map;

public interface IAccountManager extends IInterface {

    public static class Default implements IAccountManager {
        public String getPassword(Account account) throws RemoteException {
            return null;
        }

        public String getUserData(Account account, String key) throws RemoteException {
            return null;
        }

        public AuthenticatorDescription[] getAuthenticatorTypes(int userId) throws RemoteException {
            return null;
        }

        public Account[] getAccounts(String accountType, String opPackageName) throws RemoteException {
            return null;
        }

        public Account[] getAccountsForPackage(String packageName, int uid, String opPackageName) throws RemoteException {
            return null;
        }

        public Account[] getAccountsByTypeForPackage(String type, String packageName, String opPackageName) throws RemoteException {
            return null;
        }

        public Account[] getAccountsAsUser(String accountType, int userId, String opPackageName) throws RemoteException {
            return null;
        }

        public void hasFeatures(IAccountManagerResponse response, Account account, String[] features, String opPackageName) throws RemoteException {
        }

        public void getAccountByTypeAndFeatures(IAccountManagerResponse response, String accountType, String[] features, String opPackageName) throws RemoteException {
        }

        public void getAccountsByFeatures(IAccountManagerResponse response, String accountType, String[] features, String opPackageName) throws RemoteException {
        }

        public boolean addAccountExplicitly(Account account, String password, Bundle extras) throws RemoteException {
            return false;
        }

        public void removeAccount(IAccountManagerResponse response, Account account, boolean expectActivityLaunch) throws RemoteException {
        }

        public void removeAccountAsUser(IAccountManagerResponse response, Account account, boolean expectActivityLaunch, int userId) throws RemoteException {
        }

        public boolean removeAccountExplicitly(Account account) throws RemoteException {
            return false;
        }

        public void copyAccountToUser(IAccountManagerResponse response, Account account, int userFrom, int userTo) throws RemoteException {
        }

        public void invalidateAuthToken(String accountType, String authToken) throws RemoteException {
        }

        public String peekAuthToken(Account account, String authTokenType) throws RemoteException {
            return null;
        }

        public void setAuthToken(Account account, String authTokenType, String authToken) throws RemoteException {
        }

        public void setPassword(Account account, String password) throws RemoteException {
        }

        public void clearPassword(Account account) throws RemoteException {
        }

        public void setUserData(Account account, String key, String value) throws RemoteException {
        }

        public void updateAppPermission(Account account, String authTokenType, int uid, boolean value) throws RemoteException {
        }

        public void getAuthToken(IAccountManagerResponse response, Account account, String authTokenType, boolean notifyOnAuthFailure, boolean expectActivityLaunch, Bundle options) throws RemoteException {
        }

        public void addAccount(IAccountManagerResponse response, String accountType, String authTokenType, String[] requiredFeatures, boolean expectActivityLaunch, Bundle options) throws RemoteException {
        }

        public void addAccountAsUser(IAccountManagerResponse response, String accountType, String authTokenType, String[] requiredFeatures, boolean expectActivityLaunch, Bundle options, int userId) throws RemoteException {
        }

        public void updateCredentials(IAccountManagerResponse response, Account account, String authTokenType, boolean expectActivityLaunch, Bundle options) throws RemoteException {
        }

        public void editProperties(IAccountManagerResponse response, String accountType, boolean expectActivityLaunch) throws RemoteException {
        }

        public void confirmCredentialsAsUser(IAccountManagerResponse response, Account account, Bundle options, boolean expectActivityLaunch, int userId) throws RemoteException {
        }

        public boolean accountAuthenticated(Account account) throws RemoteException {
            return false;
        }

        public void getAuthTokenLabel(IAccountManagerResponse response, String accountType, String authTokenType) throws RemoteException {
        }

        public Account[] getSharedAccountsAsUser(int userId) throws RemoteException {
            return null;
        }

        public boolean removeSharedAccountAsUser(Account account, int userId) throws RemoteException {
            return false;
        }

        public void addSharedAccountsFromParentUser(int parentUserId, int userId, String opPackageName) throws RemoteException {
        }

        public void renameAccount(IAccountManagerResponse response, Account accountToRename, String newName) throws RemoteException {
        }

        public String getPreviousName(Account account) throws RemoteException {
            return null;
        }

        public boolean renameSharedAccountAsUser(Account accountToRename, String newName, int userId) throws RemoteException {
            return false;
        }

        public void startAddAccountSession(IAccountManagerResponse response, String accountType, String authTokenType, String[] requiredFeatures, boolean expectActivityLaunch, Bundle options) throws RemoteException {
        }

        public void startUpdateCredentialsSession(IAccountManagerResponse response, Account account, String authTokenType, boolean expectActivityLaunch, Bundle options) throws RemoteException {
        }

        public void finishSessionAsUser(IAccountManagerResponse response, Bundle sessionBundle, boolean expectActivityLaunch, Bundle appInfo, int userId) throws RemoteException {
        }

        public boolean someUserHasAccount(Account account) throws RemoteException {
            return false;
        }

        public void isCredentialsUpdateSuggested(IAccountManagerResponse response, Account account, String statusToken) throws RemoteException {
        }

        public Map getPackagesAndVisibilityForAccount(Account account) throws RemoteException {
            return null;
        }

        public boolean addAccountExplicitlyWithVisibility(Account account, String password, Bundle extras, Map visibility) throws RemoteException {
            return false;
        }

        public boolean setAccountVisibility(Account a, String packageName, int newVisibility) throws RemoteException {
            return false;
        }

        public int getAccountVisibility(Account a, String packageName) throws RemoteException {
            return 0;
        }

        public Map getAccountsAndVisibilityForPackage(String packageName, String accountType) throws RemoteException {
            return null;
        }

        public void registerAccountListener(String[] accountTypes, String opPackageName) throws RemoteException {
        }

        public void unregisterAccountListener(String[] accountTypes, String opPackageName) throws RemoteException {
        }

        public boolean hasAccountAccess(Account account, String packageName, UserHandle userHandle) throws RemoteException {
            return false;
        }

        public IntentSender createRequestAccountAccessIntentSenderAsUser(Account account, String packageName, UserHandle userHandle) throws RemoteException {
            return null;
        }

        public void onAccountAccessed(String token) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IAccountManager {
        private static final String DESCRIPTOR = "android.accounts.IAccountManager";
        static final int TRANSACTION_accountAuthenticated = 29;
        static final int TRANSACTION_addAccount = 24;
        static final int TRANSACTION_addAccountAsUser = 25;
        static final int TRANSACTION_addAccountExplicitly = 11;
        static final int TRANSACTION_addAccountExplicitlyWithVisibility = 43;
        static final int TRANSACTION_addSharedAccountsFromParentUser = 33;
        static final int TRANSACTION_clearPassword = 20;
        static final int TRANSACTION_confirmCredentialsAsUser = 28;
        static final int TRANSACTION_copyAccountToUser = 15;
        static final int TRANSACTION_createRequestAccountAccessIntentSenderAsUser = 50;
        static final int TRANSACTION_editProperties = 27;
        static final int TRANSACTION_finishSessionAsUser = 39;
        static final int TRANSACTION_getAccountByTypeAndFeatures = 9;
        static final int TRANSACTION_getAccountVisibility = 45;
        static final int TRANSACTION_getAccounts = 4;
        static final int TRANSACTION_getAccountsAndVisibilityForPackage = 46;
        static final int TRANSACTION_getAccountsAsUser = 7;
        static final int TRANSACTION_getAccountsByFeatures = 10;
        static final int TRANSACTION_getAccountsByTypeForPackage = 6;
        static final int TRANSACTION_getAccountsForPackage = 5;
        static final int TRANSACTION_getAuthToken = 23;
        static final int TRANSACTION_getAuthTokenLabel = 30;
        static final int TRANSACTION_getAuthenticatorTypes = 3;
        static final int TRANSACTION_getPackagesAndVisibilityForAccount = 42;
        static final int TRANSACTION_getPassword = 1;
        static final int TRANSACTION_getPreviousName = 35;
        static final int TRANSACTION_getSharedAccountsAsUser = 31;
        static final int TRANSACTION_getUserData = 2;
        static final int TRANSACTION_hasAccountAccess = 49;
        static final int TRANSACTION_hasFeatures = 8;
        static final int TRANSACTION_invalidateAuthToken = 16;
        static final int TRANSACTION_isCredentialsUpdateSuggested = 41;
        static final int TRANSACTION_onAccountAccessed = 51;
        static final int TRANSACTION_peekAuthToken = 17;
        static final int TRANSACTION_registerAccountListener = 47;
        static final int TRANSACTION_removeAccount = 12;
        static final int TRANSACTION_removeAccountAsUser = 13;
        static final int TRANSACTION_removeAccountExplicitly = 14;
        static final int TRANSACTION_removeSharedAccountAsUser = 32;
        static final int TRANSACTION_renameAccount = 34;
        static final int TRANSACTION_renameSharedAccountAsUser = 36;
        static final int TRANSACTION_setAccountVisibility = 44;
        static final int TRANSACTION_setAuthToken = 18;
        static final int TRANSACTION_setPassword = 19;
        static final int TRANSACTION_setUserData = 21;
        static final int TRANSACTION_someUserHasAccount = 40;
        static final int TRANSACTION_startAddAccountSession = 37;
        static final int TRANSACTION_startUpdateCredentialsSession = 38;
        static final int TRANSACTION_unregisterAccountListener = 48;
        static final int TRANSACTION_updateAppPermission = 22;
        static final int TRANSACTION_updateCredentials = 26;

        private static class Proxy implements IAccountManager {
            public static IAccountManager sDefaultImpl;
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

            public String getPassword(Account account) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 0;
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getPassword(account);
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

            public String getUserData(Account account, String key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(key);
                    String str = this.mRemote;
                    if (!str.transact(2, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != null) {
                            str = Stub.getDefaultImpl().getUserData(account, key);
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

            public AuthenticatorDescription[] getAuthenticatorTypes(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    AuthenticatorDescription[] authenticatorDescriptionArr = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        authenticatorDescriptionArr = Stub.getDefaultImpl();
                        if (authenticatorDescriptionArr != 0) {
                            authenticatorDescriptionArr = Stub.getDefaultImpl().getAuthenticatorTypes(userId);
                            return authenticatorDescriptionArr;
                        }
                    }
                    _reply.readException();
                    AuthenticatorDescription[] _result = (AuthenticatorDescription[]) _reply.createTypedArray(AuthenticatorDescription.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Account[] getAccounts(String accountType, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(accountType);
                    _data.writeString(opPackageName);
                    Account[] accountArr = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        accountArr = Stub.getDefaultImpl();
                        if (accountArr != 0) {
                            accountArr = Stub.getDefaultImpl().getAccounts(accountType, opPackageName);
                            return accountArr;
                        }
                    }
                    _reply.readException();
                    Account[] _result = (Account[]) _reply.createTypedArray(Account.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Account[] getAccountsForPackage(String packageName, int uid, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(uid);
                    _data.writeString(opPackageName);
                    Account[] accountArr = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        accountArr = Stub.getDefaultImpl();
                        if (accountArr != 0) {
                            accountArr = Stub.getDefaultImpl().getAccountsForPackage(packageName, uid, opPackageName);
                            return accountArr;
                        }
                    }
                    _reply.readException();
                    Account[] _result = (Account[]) _reply.createTypedArray(Account.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Account[] getAccountsByTypeForPackage(String type, String packageName, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(type);
                    _data.writeString(packageName);
                    _data.writeString(opPackageName);
                    Account[] accountArr = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        accountArr = Stub.getDefaultImpl();
                        if (accountArr != 0) {
                            accountArr = Stub.getDefaultImpl().getAccountsByTypeForPackage(type, packageName, opPackageName);
                            return accountArr;
                        }
                    }
                    _reply.readException();
                    Account[] _result = (Account[]) _reply.createTypedArray(Account.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Account[] getAccountsAsUser(String accountType, int userId, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(accountType);
                    _data.writeInt(userId);
                    _data.writeString(opPackageName);
                    Account[] accountArr = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        accountArr = Stub.getDefaultImpl();
                        if (accountArr != 0) {
                            accountArr = Stub.getDefaultImpl().getAccountsAsUser(accountType, userId, opPackageName);
                            return accountArr;
                        }
                    }
                    _reply.readException();
                    Account[] _result = (Account[]) _reply.createTypedArray(Account.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void hasFeatures(IAccountManagerResponse response, Account account, String[] features, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringArray(features);
                    _data.writeString(opPackageName);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().hasFeatures(response, account, features, opPackageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void getAccountByTypeAndFeatures(IAccountManagerResponse response, String accountType, String[] features, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    _data.writeString(accountType);
                    _data.writeStringArray(features);
                    _data.writeString(opPackageName);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().getAccountByTypeAndFeatures(response, accountType, features, opPackageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void getAccountsByFeatures(IAccountManagerResponse response, String accountType, String[] features, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    _data.writeString(accountType);
                    _data.writeStringArray(features);
                    _data.writeString(opPackageName);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().getAccountsByFeatures(response, accountType, features, opPackageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean addAccountExplicitly(Account account, String password, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(password);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().addAccountExplicitly(account, password, extras);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeAccount(IAccountManagerResponse response, Account account, boolean expectActivityLaunch) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    int i = 1;
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!expectActivityLaunch) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeAccount(response, account, expectActivityLaunch);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeAccountAsUser(IAccountManagerResponse response, Account account, boolean expectActivityLaunch, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    int i = 1;
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!expectActivityLaunch) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeAccountAsUser(response, account, expectActivityLaunch, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean removeAccountExplicitly(Account account) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().removeAccountExplicitly(account);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void copyAccountToUser(IAccountManagerResponse response, Account account, int userFrom, int userTo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userFrom);
                    _data.writeInt(userTo);
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().copyAccountToUser(response, account, userFrom, userTo);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void invalidateAuthToken(String accountType, String authToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(accountType);
                    _data.writeString(authToken);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().invalidateAuthToken(accountType, authToken);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String peekAuthToken(Account account, String authTokenType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authTokenType);
                    String str = this.mRemote;
                    if (!str.transact(17, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != null) {
                            str = Stub.getDefaultImpl().peekAuthToken(account, authTokenType);
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

            public void setAuthToken(Account account, String authTokenType, String authToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authTokenType);
                    _data.writeString(authToken);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAuthToken(account, authTokenType, authToken);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPassword(Account account, String password) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(password);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPassword(account, password);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearPassword(Account account) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearPassword(account);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setUserData(Account account, String key, String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(key);
                    _data.writeString(value);
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUserData(account, key, value);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateAppPermission(Account account, String authTokenType, int uid, boolean value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authTokenType);
                    _data.writeInt(uid);
                    if (!value) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateAppPermission(account, authTokenType, uid, value);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void getAuthToken(IAccountManagerResponse response, Account account, String authTokenType, boolean notifyOnAuthFailure, boolean expectActivityLaunch, Bundle options) throws RemoteException {
                Throwable th;
                Account account2 = account;
                Bundle bundle = options;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    if (account2 != null) {
                        _data.writeInt(1);
                        account2.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeString(authTokenType);
                        _data.writeInt(notifyOnAuthFailure ? 1 : 0);
                        _data.writeInt(expectActivityLaunch ? 1 : 0);
                        if (bundle != null) {
                            _data.writeInt(1);
                            bundle.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().getAuthToken(response, account, authTokenType, notifyOnAuthFailure, expectActivityLaunch, options);
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
                    String str = authTokenType;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void addAccount(IAccountManagerResponse response, String accountType, String authTokenType, String[] requiredFeatures, boolean expectActivityLaunch, Bundle options) throws RemoteException {
                Throwable th;
                String str;
                String[] strArr;
                Bundle bundle = options;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    try {
                        _data.writeString(accountType);
                    } catch (Throwable th2) {
                        th = th2;
                        str = authTokenType;
                        strArr = requiredFeatures;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(authTokenType);
                        try {
                            _data.writeStringArray(requiredFeatures);
                            _data.writeInt(expectActivityLaunch ? 1 : 0);
                            if (bundle != null) {
                                _data.writeInt(1);
                                bundle.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        strArr = requiredFeatures;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().addAccount(response, accountType, authTokenType, requiredFeatures, expectActivityLaunch, options);
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str2 = accountType;
                    str = authTokenType;
                    strArr = requiredFeatures;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void addAccountAsUser(IAccountManagerResponse response, String accountType, String authTokenType, String[] requiredFeatures, boolean expectActivityLaunch, Bundle options, int userId) throws RemoteException {
                Throwable th;
                String str;
                String[] strArr;
                int i;
                Bundle bundle = options;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    try {
                        _data.writeString(accountType);
                    } catch (Throwable th2) {
                        th = th2;
                        str = authTokenType;
                        strArr = requiredFeatures;
                        i = userId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(authTokenType);
                        try {
                            _data.writeStringArray(requiredFeatures);
                            _data.writeInt(expectActivityLaunch ? 1 : 0);
                            if (bundle != null) {
                                _data.writeInt(1);
                                bundle.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            i = userId;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(userId);
                            if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().addAccountAsUser(response, accountType, authTokenType, requiredFeatures, expectActivityLaunch, options, userId);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        strArr = requiredFeatures;
                        i = userId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str2 = accountType;
                    str = authTokenType;
                    strArr = requiredFeatures;
                    i = userId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void updateCredentials(IAccountManagerResponse response, Account account, String authTokenType, boolean expectActivityLaunch, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authTokenType);
                    _data.writeInt(expectActivityLaunch ? 1 : 0);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateCredentials(response, account, authTokenType, expectActivityLaunch, options);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void editProperties(IAccountManagerResponse response, String accountType, boolean expectActivityLaunch) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    _data.writeString(accountType);
                    _data.writeInt(expectActivityLaunch ? 1 : 0);
                    if (this.mRemote.transact(27, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().editProperties(response, accountType, expectActivityLaunch);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void confirmCredentialsAsUser(IAccountManagerResponse response, Account account, Bundle options, boolean expectActivityLaunch, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    int i = 1;
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!expectActivityLaunch) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().confirmCredentialsAsUser(response, account, options, expectActivityLaunch, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean accountAuthenticated(Account account) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(29, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().accountAuthenticated(account);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void getAuthTokenLabel(IAccountManagerResponse response, String accountType, String authTokenType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    _data.writeString(accountType);
                    _data.writeString(authTokenType);
                    if (this.mRemote.transact(30, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().getAuthTokenLabel(response, accountType, authTokenType);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Account[] getSharedAccountsAsUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    Account[] accountArr = 31;
                    if (!this.mRemote.transact(31, _data, _reply, 0)) {
                        accountArr = Stub.getDefaultImpl();
                        if (accountArr != 0) {
                            accountArr = Stub.getDefaultImpl().getSharedAccountsAsUser(userId);
                            return accountArr;
                        }
                    }
                    _reply.readException();
                    Account[] _result = (Account[]) _reply.createTypedArray(Account.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean removeSharedAccountAsUser(Account account, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(32, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().removeSharedAccountAsUser(account, userId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addSharedAccountsFromParentUser(int parentUserId, int userId, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(parentUserId);
                    _data.writeInt(userId);
                    _data.writeString(opPackageName);
                    if (this.mRemote.transact(33, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addSharedAccountsFromParentUser(parentUserId, userId, opPackageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void renameAccount(IAccountManagerResponse response, Account accountToRename, String newName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    if (accountToRename != null) {
                        _data.writeInt(1);
                        accountToRename.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(newName);
                    if (this.mRemote.transact(34, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().renameAccount(response, accountToRename, newName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getPreviousName(Account account) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    String str = this.mRemote;
                    if (!str.transact(35, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != null) {
                            str = Stub.getDefaultImpl().getPreviousName(account);
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

            public boolean renameSharedAccountAsUser(Account accountToRename, String newName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (accountToRename != null) {
                        _data.writeInt(1);
                        accountToRename.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(newName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(36, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().renameSharedAccountAsUser(accountToRename, newName, userId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startAddAccountSession(IAccountManagerResponse response, String accountType, String authTokenType, String[] requiredFeatures, boolean expectActivityLaunch, Bundle options) throws RemoteException {
                Throwable th;
                String str;
                String[] strArr;
                Bundle bundle = options;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    try {
                        _data.writeString(accountType);
                    } catch (Throwable th2) {
                        th = th2;
                        str = authTokenType;
                        strArr = requiredFeatures;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(authTokenType);
                        try {
                            _data.writeStringArray(requiredFeatures);
                            _data.writeInt(expectActivityLaunch ? 1 : 0);
                            if (bundle != null) {
                                _data.writeInt(1);
                                bundle.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        strArr = requiredFeatures;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        if (this.mRemote.transact(37, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().startAddAccountSession(response, accountType, authTokenType, requiredFeatures, expectActivityLaunch, options);
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str2 = accountType;
                    str = authTokenType;
                    strArr = requiredFeatures;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void startUpdateCredentialsSession(IAccountManagerResponse response, Account account, String authTokenType, boolean expectActivityLaunch, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authTokenType);
                    _data.writeInt(expectActivityLaunch ? 1 : 0);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(38, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startUpdateCredentialsSession(response, account, authTokenType, expectActivityLaunch, options);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void finishSessionAsUser(IAccountManagerResponse response, Bundle sessionBundle, boolean expectActivityLaunch, Bundle appInfo, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    if (sessionBundle != null) {
                        _data.writeInt(1);
                        sessionBundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(expectActivityLaunch ? 1 : 0);
                    if (appInfo != null) {
                        _data.writeInt(1);
                        appInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(39, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().finishSessionAsUser(response, sessionBundle, expectActivityLaunch, appInfo, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean someUserHasAccount(Account account) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(40, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().someUserHasAccount(account);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void isCredentialsUpdateSuggested(IAccountManagerResponse response, Account account, String statusToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(statusToken);
                    if (this.mRemote.transact(41, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().isCredentialsUpdateSuggested(response, account, statusToken);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Map getPackagesAndVisibilityForAccount(Account account) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    Map map = this.mRemote;
                    if (!map.transact(42, _data, _reply, 0)) {
                        map = Stub.getDefaultImpl();
                        if (map != null) {
                            map = Stub.getDefaultImpl().getPackagesAndVisibilityForAccount(account);
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

            public boolean addAccountExplicitlyWithVisibility(Account account, String password, Bundle extras, Map visibility) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(password);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeMap(visibility);
                    if (this.mRemote.transact(43, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().addAccountExplicitlyWithVisibility(account, password, extras, visibility);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setAccountVisibility(Account a, String packageName, int newVisibility) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (a != null) {
                        _data.writeInt(1);
                        a.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    _data.writeInt(newVisibility);
                    if (this.mRemote.transact(44, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setAccountVisibility(a, packageName, newVisibility);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getAccountVisibility(Account a, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (a != null) {
                        _data.writeInt(1);
                        a.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    int i = this.mRemote;
                    if (!i.transact(45, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getAccountVisibility(a, packageName);
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

            public Map getAccountsAndVisibilityForPackage(String packageName, String accountType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(accountType);
                    Map map = 46;
                    if (!this.mRemote.transact(46, _data, _reply, 0)) {
                        map = Stub.getDefaultImpl();
                        if (map != 0) {
                            map = Stub.getDefaultImpl().getAccountsAndVisibilityForPackage(packageName, accountType);
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

            public void registerAccountListener(String[] accountTypes, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(accountTypes);
                    _data.writeString(opPackageName);
                    if (this.mRemote.transact(47, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerAccountListener(accountTypes, opPackageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterAccountListener(String[] accountTypes, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(accountTypes);
                    _data.writeString(opPackageName);
                    if (this.mRemote.transact(48, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterAccountListener(accountTypes, opPackageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasAccountAccess(Account account, String packageName, UserHandle userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (userHandle != null) {
                        _data.writeInt(1);
                        userHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(49, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().hasAccountAccess(account, packageName, userHandle);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IntentSender createRequestAccountAccessIntentSenderAsUser(Account account, String packageName, UserHandle userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IntentSender intentSender = 0;
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (userHandle != null) {
                        _data.writeInt(1);
                        userHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(50, _data, _reply, 0)) {
                        intentSender = Stub.getDefaultImpl();
                        if (intentSender != 0) {
                            intentSender = Stub.getDefaultImpl().createRequestAccountAccessIntentSenderAsUser(account, packageName, userHandle);
                            return intentSender;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        intentSender = (IntentSender) IntentSender.CREATOR.createFromParcel(_reply);
                    } else {
                        intentSender = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return intentSender;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onAccountAccessed(String token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(token);
                    if (this.mRemote.transact(51, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onAccountAccessed(token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAccountManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAccountManager)) {
                return new Proxy(obj);
            }
            return (IAccountManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getPassword";
                case 2:
                    return "getUserData";
                case 3:
                    return "getAuthenticatorTypes";
                case 4:
                    return "getAccounts";
                case 5:
                    return "getAccountsForPackage";
                case 6:
                    return "getAccountsByTypeForPackage";
                case 7:
                    return "getAccountsAsUser";
                case 8:
                    return "hasFeatures";
                case 9:
                    return "getAccountByTypeAndFeatures";
                case 10:
                    return "getAccountsByFeatures";
                case 11:
                    return "addAccountExplicitly";
                case 12:
                    return "removeAccount";
                case 13:
                    return "removeAccountAsUser";
                case 14:
                    return "removeAccountExplicitly";
                case 15:
                    return "copyAccountToUser";
                case 16:
                    return "invalidateAuthToken";
                case 17:
                    return "peekAuthToken";
                case 18:
                    return "setAuthToken";
                case 19:
                    return "setPassword";
                case 20:
                    return "clearPassword";
                case 21:
                    return "setUserData";
                case 22:
                    return "updateAppPermission";
                case 23:
                    return "getAuthToken";
                case 24:
                    return "addAccount";
                case 25:
                    return "addAccountAsUser";
                case 26:
                    return "updateCredentials";
                case 27:
                    return "editProperties";
                case 28:
                    return "confirmCredentialsAsUser";
                case 29:
                    return "accountAuthenticated";
                case 30:
                    return "getAuthTokenLabel";
                case 31:
                    return "getSharedAccountsAsUser";
                case 32:
                    return "removeSharedAccountAsUser";
                case 33:
                    return "addSharedAccountsFromParentUser";
                case 34:
                    return "renameAccount";
                case 35:
                    return "getPreviousName";
                case 36:
                    return "renameSharedAccountAsUser";
                case 37:
                    return "startAddAccountSession";
                case 38:
                    return "startUpdateCredentialsSession";
                case 39:
                    return "finishSessionAsUser";
                case 40:
                    return "someUserHasAccount";
                case 41:
                    return "isCredentialsUpdateSuggested";
                case 42:
                    return "getPackagesAndVisibilityForAccount";
                case 43:
                    return "addAccountExplicitlyWithVisibility";
                case 44:
                    return "setAccountVisibility";
                case 45:
                    return "getAccountVisibility";
                case 46:
                    return "getAccountsAndVisibilityForPackage";
                case 47:
                    return "registerAccountListener";
                case 48:
                    return "unregisterAccountListener";
                case 49:
                    return "hasAccountAccess";
                case 50:
                    return "createRequestAccountAccessIntentSenderAsUser";
                case 51:
                    return "onAccountAccessed";
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
                boolean _arg2 = false;
                Account _arg0;
                String _result;
                String _result2;
                Account[] _result3;
                IAccountManagerResponse _arg02;
                Account _arg1;
                Bundle _arg22;
                boolean _result4;
                IAccountManagerResponse _arg03;
                Account _arg12;
                boolean _result5;
                IAccountManagerResponse _arg04;
                String _arg23;
                boolean _arg3;
                boolean _arg4;
                String _arg13;
                String[] _arg32;
                Bundle _arg5;
                IAccountManagerResponse _arg05;
                Account _arg14;
                Bundle _arg42;
                Bundle _arg24;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result = getPassword(_arg0);
                        reply.writeNoException();
                        parcel2.writeString(_result);
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result2 = getUserData(_arg0, data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result2);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        AuthenticatorDescription[] _result6 = getAuthenticatorTypes(data.readInt());
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result6, 1);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        Account[] _result7 = getAccounts(data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result7, 1);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _result3 = getAccountsForPackage(data.readString(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result3, 1);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        _result3 = getAccountsByTypeForPackage(data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result3, 1);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        _result3 = getAccountsAsUser(data.readString(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result3, 1);
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        _arg02 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg1 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        hasFeatures(_arg02, _arg1, data.createStringArray(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        getAccountByTypeAndFeatures(android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder()), data.readString(), data.createStringArray(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        getAccountsByFeatures(android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder()), data.readString(), data.createStringArray(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result = data.readString();
                        if (data.readInt() != 0) {
                            _arg22 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        _result4 = addAccountExplicitly(_arg0, _result, _arg22);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        _arg03 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg12 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        removeAccount(_arg03, _arg12, _arg2);
                        reply.writeNoException();
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        _arg03 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg12 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        removeAccountAsUser(_arg03, _arg12, _arg2, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result5 = removeAccountExplicitly(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        _arg02 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg1 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        copyAccountToUser(_arg02, _arg1, data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        invalidateAuthToken(data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result2 = peekAuthToken(_arg0, data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result2);
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        setAuthToken(_arg0, data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        setPassword(_arg0, data.readString());
                        reply.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        clearPassword(_arg0);
                        reply.writeNoException();
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        setUserData(_arg0, data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        _result2 = data.readString();
                        int _arg25 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        updateAppPermission(_arg1, _result2, _arg25, _arg2);
                        reply.writeNoException();
                        return true;
                    case 23:
                        Account _arg15;
                        Bundle _arg52;
                        parcel.enforceInterface(descriptor);
                        _arg04 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg15 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg15 = null;
                        }
                        _arg23 = data.readString();
                        _arg3 = data.readInt() != 0;
                        _arg4 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg52 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg52 = null;
                        }
                        getAuthToken(_arg04, _arg15, _arg23, _arg3, _arg4, _arg52);
                        reply.writeNoException();
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        _arg04 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                        _arg13 = data.readString();
                        _arg23 = data.readString();
                        _arg32 = data.createStringArray();
                        _arg4 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg5 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        addAccount(_arg04, _arg13, _arg23, _arg32, _arg4, _arg5);
                        reply.writeNoException();
                        return true;
                    case 25:
                        Bundle _arg53;
                        parcel.enforceInterface(descriptor);
                        IAccountManagerResponse _arg06 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                        _arg23 = data.readString();
                        String _arg26 = data.readString();
                        String[] _arg33 = data.createStringArray();
                        _arg4 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg53 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg53 = null;
                        }
                        addAccountAsUser(_arg06, _arg23, _arg26, _arg33, _arg4, _arg53, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        _arg05 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg14 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        _arg13 = data.readString();
                        _arg3 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg42 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg42 = null;
                        }
                        updateCredentials(_arg05, _arg14, _arg13, _arg3, _arg42);
                        reply.writeNoException();
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        _arg03 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                        _result2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        editProperties(_arg03, _result2, _arg2);
                        reply.writeNoException();
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        _arg05 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg14 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg24 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg24 = null;
                        }
                        confirmCredentialsAsUser(_arg05, _arg14, _arg24, data.readInt() != 0, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result5 = accountAuthenticated(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        getAuthTokenLabel(android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder()), data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        Account[] _result8 = getSharedAccountsAsUser(data.readInt());
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result8, 1);
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        boolean _result9 = removeSharedAccountAsUser(_arg0, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result9);
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        addSharedAccountsFromParentUser(data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        _arg02 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg1 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        renameAccount(_arg02, _arg1, data.readString());
                        reply.writeNoException();
                        return true;
                    case 35:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result = getPreviousName(_arg0);
                        reply.writeNoException();
                        parcel2.writeString(_result);
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result4 = renameSharedAccountAsUser(_arg0, data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        _arg04 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                        _arg13 = data.readString();
                        _arg23 = data.readString();
                        _arg32 = data.createStringArray();
                        _arg4 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg5 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        startAddAccountSession(_arg04, _arg13, _arg23, _arg32, _arg4, _arg5);
                        reply.writeNoException();
                        return true;
                    case 38:
                        parcel.enforceInterface(descriptor);
                        _arg05 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg14 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        _arg13 = data.readString();
                        _arg3 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg42 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg42 = null;
                        }
                        startUpdateCredentialsSession(_arg05, _arg14, _arg13, _arg3, _arg42);
                        reply.writeNoException();
                        return true;
                    case 39:
                        Bundle _arg16;
                        parcel.enforceInterface(descriptor);
                        _arg05 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg16 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg16 = null;
                        }
                        _result4 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg24 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg24 = null;
                        }
                        finishSessionAsUser(_arg05, _arg16, _result4, _arg24, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 40:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result5 = someUserHasAccount(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 41:
                        parcel.enforceInterface(descriptor);
                        _arg02 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg1 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        isCredentialsUpdateSuggested(_arg02, _arg1, data.readString());
                        reply.writeNoException();
                        return true;
                    case 42:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        Map _result10 = getPackagesAndVisibilityForAccount(_arg0);
                        reply.writeNoException();
                        parcel2.writeMap(_result10);
                        return true;
                    case 43:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result = data.readString();
                        if (data.readInt() != 0) {
                            _arg22 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        _arg4 = addAccountExplicitlyWithVisibility(_arg0, _result, _arg22, parcel.readHashMap(getClass().getClassLoader()));
                        reply.writeNoException();
                        parcel2.writeInt(_arg4);
                        return true;
                    case 44:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result4 = setAccountVisibility(_arg0, data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 45:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        int _result11 = getAccountVisibility(_arg0, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result11);
                        return true;
                    case 46:
                        parcel.enforceInterface(descriptor);
                        Map _result12 = getAccountsAndVisibilityForPackage(data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeMap(_result12);
                        return true;
                    case 47:
                        parcel.enforceInterface(descriptor);
                        registerAccountListener(data.createStringArray(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 48:
                        parcel.enforceInterface(descriptor);
                        unregisterAccountListener(data.createStringArray(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 49:
                        UserHandle _arg27;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result = data.readString();
                        if (data.readInt() != 0) {
                            _arg27 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg27 = null;
                        }
                        _result4 = hasAccountAccess(_arg0, _result, _arg27);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 50:
                        UserHandle _arg28;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        _result2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg28 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg28 = null;
                        }
                        IntentSender _result13 = createRequestAccountAccessIntentSenderAsUser(_arg1, _result2, _arg28);
                        reply.writeNoException();
                        if (_result13 != null) {
                            parcel2.writeInt(1);
                            _result13.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 51:
                        parcel.enforceInterface(descriptor);
                        onAccountAccessed(data.readString());
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IAccountManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IAccountManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean accountAuthenticated(Account account) throws RemoteException;

    void addAccount(IAccountManagerResponse iAccountManagerResponse, String str, String str2, String[] strArr, boolean z, Bundle bundle) throws RemoteException;

    void addAccountAsUser(IAccountManagerResponse iAccountManagerResponse, String str, String str2, String[] strArr, boolean z, Bundle bundle, int i) throws RemoteException;

    boolean addAccountExplicitly(Account account, String str, Bundle bundle) throws RemoteException;

    boolean addAccountExplicitlyWithVisibility(Account account, String str, Bundle bundle, Map map) throws RemoteException;

    void addSharedAccountsFromParentUser(int i, int i2, String str) throws RemoteException;

    void clearPassword(Account account) throws RemoteException;

    void confirmCredentialsAsUser(IAccountManagerResponse iAccountManagerResponse, Account account, Bundle bundle, boolean z, int i) throws RemoteException;

    void copyAccountToUser(IAccountManagerResponse iAccountManagerResponse, Account account, int i, int i2) throws RemoteException;

    IntentSender createRequestAccountAccessIntentSenderAsUser(Account account, String str, UserHandle userHandle) throws RemoteException;

    void editProperties(IAccountManagerResponse iAccountManagerResponse, String str, boolean z) throws RemoteException;

    void finishSessionAsUser(IAccountManagerResponse iAccountManagerResponse, Bundle bundle, boolean z, Bundle bundle2, int i) throws RemoteException;

    void getAccountByTypeAndFeatures(IAccountManagerResponse iAccountManagerResponse, String str, String[] strArr, String str2) throws RemoteException;

    int getAccountVisibility(Account account, String str) throws RemoteException;

    Account[] getAccounts(String str, String str2) throws RemoteException;

    Map getAccountsAndVisibilityForPackage(String str, String str2) throws RemoteException;

    Account[] getAccountsAsUser(String str, int i, String str2) throws RemoteException;

    void getAccountsByFeatures(IAccountManagerResponse iAccountManagerResponse, String str, String[] strArr, String str2) throws RemoteException;

    Account[] getAccountsByTypeForPackage(String str, String str2, String str3) throws RemoteException;

    Account[] getAccountsForPackage(String str, int i, String str2) throws RemoteException;

    void getAuthToken(IAccountManagerResponse iAccountManagerResponse, Account account, String str, boolean z, boolean z2, Bundle bundle) throws RemoteException;

    void getAuthTokenLabel(IAccountManagerResponse iAccountManagerResponse, String str, String str2) throws RemoteException;

    AuthenticatorDescription[] getAuthenticatorTypes(int i) throws RemoteException;

    Map getPackagesAndVisibilityForAccount(Account account) throws RemoteException;

    String getPassword(Account account) throws RemoteException;

    String getPreviousName(Account account) throws RemoteException;

    Account[] getSharedAccountsAsUser(int i) throws RemoteException;

    String getUserData(Account account, String str) throws RemoteException;

    boolean hasAccountAccess(Account account, String str, UserHandle userHandle) throws RemoteException;

    void hasFeatures(IAccountManagerResponse iAccountManagerResponse, Account account, String[] strArr, String str) throws RemoteException;

    void invalidateAuthToken(String str, String str2) throws RemoteException;

    void isCredentialsUpdateSuggested(IAccountManagerResponse iAccountManagerResponse, Account account, String str) throws RemoteException;

    void onAccountAccessed(String str) throws RemoteException;

    String peekAuthToken(Account account, String str) throws RemoteException;

    void registerAccountListener(String[] strArr, String str) throws RemoteException;

    void removeAccount(IAccountManagerResponse iAccountManagerResponse, Account account, boolean z) throws RemoteException;

    void removeAccountAsUser(IAccountManagerResponse iAccountManagerResponse, Account account, boolean z, int i) throws RemoteException;

    boolean removeAccountExplicitly(Account account) throws RemoteException;

    boolean removeSharedAccountAsUser(Account account, int i) throws RemoteException;

    void renameAccount(IAccountManagerResponse iAccountManagerResponse, Account account, String str) throws RemoteException;

    boolean renameSharedAccountAsUser(Account account, String str, int i) throws RemoteException;

    boolean setAccountVisibility(Account account, String str, int i) throws RemoteException;

    void setAuthToken(Account account, String str, String str2) throws RemoteException;

    void setPassword(Account account, String str) throws RemoteException;

    void setUserData(Account account, String str, String str2) throws RemoteException;

    boolean someUserHasAccount(Account account) throws RemoteException;

    void startAddAccountSession(IAccountManagerResponse iAccountManagerResponse, String str, String str2, String[] strArr, boolean z, Bundle bundle) throws RemoteException;

    void startUpdateCredentialsSession(IAccountManagerResponse iAccountManagerResponse, Account account, String str, boolean z, Bundle bundle) throws RemoteException;

    void unregisterAccountListener(String[] strArr, String str) throws RemoteException;

    void updateAppPermission(Account account, String str, int i, boolean z) throws RemoteException;

    void updateCredentials(IAccountManagerResponse iAccountManagerResponse, Account account, String str, boolean z, Bundle bundle) throws RemoteException;
}
