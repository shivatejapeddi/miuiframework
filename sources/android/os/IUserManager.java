package android.os;

import android.annotation.UnsupportedAppUsage;
import android.content.IntentSender;
import android.content.pm.UserInfo;
import android.graphics.Bitmap;
import android.os.UserManager.EnforcingUser;
import java.util.List;

public interface IUserManager extends IInterface {

    public static class Default implements IUserManager {
        public int getCredentialOwnerProfile(int userHandle) throws RemoteException {
            return 0;
        }

        public int getProfileParentId(int userHandle) throws RemoteException {
            return 0;
        }

        public UserInfo createUser(String name, int flags) throws RemoteException {
            return null;
        }

        public UserInfo createProfileForUser(String name, int flags, int userHandle, String[] disallowedPackages) throws RemoteException {
            return null;
        }

        public UserInfo createRestrictedProfile(String name, int parentUserHandle) throws RemoteException {
            return null;
        }

        public void setUserEnabled(int userHandle) throws RemoteException {
        }

        public void setUserAdmin(int userId) throws RemoteException {
        }

        public void evictCredentialEncryptionKey(int userHandle) throws RemoteException {
        }

        public boolean removeUser(int userHandle) throws RemoteException {
            return false;
        }

        public boolean removeUserEvenWhenDisallowed(int userHandle) throws RemoteException {
            return false;
        }

        public void setUserName(int userHandle, String name) throws RemoteException {
        }

        public void setUserIcon(int userHandle, Bitmap icon) throws RemoteException {
        }

        public ParcelFileDescriptor getUserIcon(int userHandle) throws RemoteException {
            return null;
        }

        public UserInfo getPrimaryUser() throws RemoteException {
            return null;
        }

        public List<UserInfo> getUsers(boolean excludeDying) throws RemoteException {
            return null;
        }

        public List<UserInfo> getProfiles(int userHandle, boolean enabledOnly) throws RemoteException {
            return null;
        }

        public int[] getProfileIds(int userId, boolean enabledOnly) throws RemoteException {
            return null;
        }

        public boolean canAddMoreManagedProfiles(int userHandle, boolean allowedToRemoveOne) throws RemoteException {
            return false;
        }

        public UserInfo getProfileParent(int userHandle) throws RemoteException {
            return null;
        }

        public boolean isSameProfileGroup(int userHandle, int otherUserHandle) throws RemoteException {
            return false;
        }

        public UserInfo getUserInfo(int userHandle) throws RemoteException {
            return null;
        }

        public String getUserAccount(int userHandle) throws RemoteException {
            return null;
        }

        public void setUserAccount(int userHandle, String accountName) throws RemoteException {
        }

        public long getUserCreationTime(int userHandle) throws RemoteException {
            return 0;
        }

        public boolean isRestricted() throws RemoteException {
            return false;
        }

        public boolean canHaveRestrictedProfile(int userHandle) throws RemoteException {
            return false;
        }

        public int getUserSerialNumber(int userHandle) throws RemoteException {
            return 0;
        }

        public int getUserHandle(int userSerialNumber) throws RemoteException {
            return 0;
        }

        public int getUserRestrictionSource(String restrictionKey, int userHandle) throws RemoteException {
            return 0;
        }

        public List<EnforcingUser> getUserRestrictionSources(String restrictionKey, int userHandle) throws RemoteException {
            return null;
        }

        public Bundle getUserRestrictions(int userHandle) throws RemoteException {
            return null;
        }

        public boolean hasBaseUserRestriction(String restrictionKey, int userHandle) throws RemoteException {
            return false;
        }

        public boolean hasUserRestriction(String restrictionKey, int userHandle) throws RemoteException {
            return false;
        }

        public boolean hasUserRestrictionOnAnyUser(String restrictionKey) throws RemoteException {
            return false;
        }

        public void setUserRestriction(String key, boolean value, int userHandle) throws RemoteException {
        }

        public void setApplicationRestrictions(String packageName, Bundle restrictions, int userHandle) throws RemoteException {
        }

        public Bundle getApplicationRestrictions(String packageName) throws RemoteException {
            return null;
        }

        public Bundle getApplicationRestrictionsForUser(String packageName, int userHandle) throws RemoteException {
            return null;
        }

        public void setDefaultGuestRestrictions(Bundle restrictions) throws RemoteException {
        }

        public Bundle getDefaultGuestRestrictions() throws RemoteException {
            return null;
        }

        public boolean markGuestForDeletion(int userHandle) throws RemoteException {
            return false;
        }

        public boolean isQuietModeEnabled(int userHandle) throws RemoteException {
            return false;
        }

        public void setSeedAccountData(int userHandle, String accountName, String accountType, PersistableBundle accountOptions, boolean persist) throws RemoteException {
        }

        public String getSeedAccountName() throws RemoteException {
            return null;
        }

        public String getSeedAccountType() throws RemoteException {
            return null;
        }

        public PersistableBundle getSeedAccountOptions() throws RemoteException {
            return null;
        }

        public void clearSeedAccountData() throws RemoteException {
        }

        public boolean someUserHasSeedAccount(String accountName, String accountType) throws RemoteException {
            return false;
        }

        public boolean isManagedProfile(int userId) throws RemoteException {
            return false;
        }

        public boolean isDemoUser(int userId) throws RemoteException {
            return false;
        }

        public UserInfo createProfileForUserEvenWhenDisallowed(String name, int flags, int userHandle, String[] disallowedPackages) throws RemoteException {
            return null;
        }

        public boolean isUserUnlockingOrUnlocked(int userId) throws RemoteException {
            return false;
        }

        public int getManagedProfileBadge(int userId) throws RemoteException {
            return 0;
        }

        public boolean isUserUnlocked(int userId) throws RemoteException {
            return false;
        }

        public boolean isUserRunning(int userId) throws RemoteException {
            return false;
        }

        public boolean isUserNameSet(int userHandle) throws RemoteException {
            return false;
        }

        public boolean hasRestrictedProfiles() throws RemoteException {
            return false;
        }

        public boolean requestQuietModeEnabled(String callingPackage, boolean enableQuietMode, int userHandle, IntentSender target) throws RemoteException {
            return false;
        }

        public String getUserName() throws RemoteException {
            return null;
        }

        public long getUserStartRealtime() throws RemoteException {
            return 0;
        }

        public long getUserUnlockRealtime() throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IUserManager {
        private static final String DESCRIPTOR = "android.os.IUserManager";
        static final int TRANSACTION_canAddMoreManagedProfiles = 18;
        static final int TRANSACTION_canHaveRestrictedProfile = 26;
        static final int TRANSACTION_clearSeedAccountData = 47;
        static final int TRANSACTION_createProfileForUser = 4;
        static final int TRANSACTION_createProfileForUserEvenWhenDisallowed = 51;
        static final int TRANSACTION_createRestrictedProfile = 5;
        static final int TRANSACTION_createUser = 3;
        static final int TRANSACTION_evictCredentialEncryptionKey = 8;
        static final int TRANSACTION_getApplicationRestrictions = 37;
        static final int TRANSACTION_getApplicationRestrictionsForUser = 38;
        static final int TRANSACTION_getCredentialOwnerProfile = 1;
        static final int TRANSACTION_getDefaultGuestRestrictions = 40;
        static final int TRANSACTION_getManagedProfileBadge = 53;
        static final int TRANSACTION_getPrimaryUser = 14;
        static final int TRANSACTION_getProfileIds = 17;
        static final int TRANSACTION_getProfileParent = 19;
        static final int TRANSACTION_getProfileParentId = 2;
        static final int TRANSACTION_getProfiles = 16;
        static final int TRANSACTION_getSeedAccountName = 44;
        static final int TRANSACTION_getSeedAccountOptions = 46;
        static final int TRANSACTION_getSeedAccountType = 45;
        static final int TRANSACTION_getUserAccount = 22;
        static final int TRANSACTION_getUserCreationTime = 24;
        static final int TRANSACTION_getUserHandle = 28;
        static final int TRANSACTION_getUserIcon = 13;
        static final int TRANSACTION_getUserInfo = 21;
        static final int TRANSACTION_getUserName = 59;
        static final int TRANSACTION_getUserRestrictionSource = 29;
        static final int TRANSACTION_getUserRestrictionSources = 30;
        static final int TRANSACTION_getUserRestrictions = 31;
        static final int TRANSACTION_getUserSerialNumber = 27;
        static final int TRANSACTION_getUserStartRealtime = 60;
        static final int TRANSACTION_getUserUnlockRealtime = 61;
        static final int TRANSACTION_getUsers = 15;
        static final int TRANSACTION_hasBaseUserRestriction = 32;
        static final int TRANSACTION_hasRestrictedProfiles = 57;
        static final int TRANSACTION_hasUserRestriction = 33;
        static final int TRANSACTION_hasUserRestrictionOnAnyUser = 34;
        static final int TRANSACTION_isDemoUser = 50;
        static final int TRANSACTION_isManagedProfile = 49;
        static final int TRANSACTION_isQuietModeEnabled = 42;
        static final int TRANSACTION_isRestricted = 25;
        static final int TRANSACTION_isSameProfileGroup = 20;
        static final int TRANSACTION_isUserNameSet = 56;
        static final int TRANSACTION_isUserRunning = 55;
        static final int TRANSACTION_isUserUnlocked = 54;
        static final int TRANSACTION_isUserUnlockingOrUnlocked = 52;
        static final int TRANSACTION_markGuestForDeletion = 41;
        static final int TRANSACTION_removeUser = 9;
        static final int TRANSACTION_removeUserEvenWhenDisallowed = 10;
        static final int TRANSACTION_requestQuietModeEnabled = 58;
        static final int TRANSACTION_setApplicationRestrictions = 36;
        static final int TRANSACTION_setDefaultGuestRestrictions = 39;
        static final int TRANSACTION_setSeedAccountData = 43;
        static final int TRANSACTION_setUserAccount = 23;
        static final int TRANSACTION_setUserAdmin = 7;
        static final int TRANSACTION_setUserEnabled = 6;
        static final int TRANSACTION_setUserIcon = 12;
        static final int TRANSACTION_setUserName = 11;
        static final int TRANSACTION_setUserRestriction = 35;
        static final int TRANSACTION_someUserHasSeedAccount = 48;

        private static class Proxy implements IUserManager {
            public static IUserManager sDefaultImpl;
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

            public int getCredentialOwnerProfile(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    int i = 1;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getCredentialOwnerProfile(userHandle);
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

            public int getProfileParentId(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    int i = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getProfileParentId(userHandle);
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

            public UserInfo createUser(String name, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(flags);
                    UserInfo userInfo = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        userInfo = Stub.getDefaultImpl();
                        if (userInfo != 0) {
                            userInfo = Stub.getDefaultImpl().createUser(name, flags);
                            return userInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        userInfo = (UserInfo) UserInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        userInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return userInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public UserInfo createProfileForUser(String name, int flags, int userHandle, String[] disallowedPackages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(flags);
                    _data.writeInt(userHandle);
                    _data.writeStringArray(disallowedPackages);
                    UserInfo userInfo = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        userInfo = Stub.getDefaultImpl();
                        if (userInfo != 0) {
                            userInfo = Stub.getDefaultImpl().createProfileForUser(name, flags, userHandle, disallowedPackages);
                            return userInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        userInfo = (UserInfo) UserInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        userInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return userInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public UserInfo createRestrictedProfile(String name, int parentUserHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(parentUserHandle);
                    UserInfo userInfo = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        userInfo = Stub.getDefaultImpl();
                        if (userInfo != 0) {
                            userInfo = Stub.getDefaultImpl().createRestrictedProfile(name, parentUserHandle);
                            return userInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        userInfo = (UserInfo) UserInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        userInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return userInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setUserEnabled(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUserEnabled(userHandle);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setUserAdmin(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUserAdmin(userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void evictCredentialEncryptionKey(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().evictCredentialEncryptionKey(userHandle);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean removeUser(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().removeUser(userHandle);
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

            public boolean removeUserEvenWhenDisallowed(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().removeUserEvenWhenDisallowed(userHandle);
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

            public void setUserName(int userHandle, String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    _data.writeString(name);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUserName(userHandle, name);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setUserIcon(int userHandle, Bitmap icon) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    if (icon != null) {
                        _data.writeInt(1);
                        icon.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUserIcon(userHandle, icon);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParcelFileDescriptor getUserIcon(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    ParcelFileDescriptor parcelFileDescriptor = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        parcelFileDescriptor = Stub.getDefaultImpl();
                        if (parcelFileDescriptor != 0) {
                            parcelFileDescriptor = Stub.getDefaultImpl().getUserIcon(userHandle);
                            return parcelFileDescriptor;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parcelFileDescriptor = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
                    } else {
                        parcelFileDescriptor = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parcelFileDescriptor;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public UserInfo getPrimaryUser() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    UserInfo userInfo = 14;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        userInfo = Stub.getDefaultImpl();
                        if (userInfo != 0) {
                            userInfo = Stub.getDefaultImpl().getPrimaryUser();
                            return userInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        userInfo = (UserInfo) UserInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        userInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return userInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<UserInfo> getUsers(boolean excludeDying) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(excludeDying ? 1 : 0);
                    List<UserInfo> list = this.mRemote;
                    if (!list.transact(15, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != null) {
                            list = Stub.getDefaultImpl().getUsers(excludeDying);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(UserInfo.CREATOR);
                    List<UserInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<UserInfo> getProfiles(int userHandle, boolean enabledOnly) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    _data.writeInt(enabledOnly ? 1 : 0);
                    List<UserInfo> list = this.mRemote;
                    if (!list.transact(16, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != null) {
                            list = Stub.getDefaultImpl().getProfiles(userHandle, enabledOnly);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(UserInfo.CREATOR);
                    List<UserInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getProfileIds(int userId, boolean enabledOnly) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(enabledOnly ? 1 : 0);
                    int[] iArr = this.mRemote;
                    if (!iArr.transact(17, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != null) {
                            iArr = Stub.getDefaultImpl().getProfileIds(userId, enabledOnly);
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

            public boolean canAddMoreManagedProfiles(int userHandle, boolean allowedToRemoveOne) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _result = true;
                    _data.writeInt(allowedToRemoveOne ? 1 : 0);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().canAddMoreManagedProfiles(userHandle, allowedToRemoveOne);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public UserInfo getProfileParent(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    UserInfo userInfo = 19;
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        userInfo = Stub.getDefaultImpl();
                        if (userInfo != 0) {
                            userInfo = Stub.getDefaultImpl().getProfileParent(userHandle);
                            return userInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        userInfo = (UserInfo) UserInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        userInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return userInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isSameProfileGroup(int userHandle, int otherUserHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    _data.writeInt(otherUserHandle);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isSameProfileGroup(userHandle, otherUserHandle);
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

            public UserInfo getUserInfo(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    UserInfo userInfo = 21;
                    if (!this.mRemote.transact(21, _data, _reply, 0)) {
                        userInfo = Stub.getDefaultImpl();
                        if (userInfo != 0) {
                            userInfo = Stub.getDefaultImpl().getUserInfo(userHandle);
                            return userInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        userInfo = (UserInfo) UserInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        userInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return userInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getUserAccount(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    String str = 22;
                    if (!this.mRemote.transact(22, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getUserAccount(userHandle);
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

            public void setUserAccount(int userHandle, String accountName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    _data.writeString(accountName);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUserAccount(userHandle, accountName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getUserCreationTime(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    long j = 24;
                    if (!this.mRemote.transact(24, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getUserCreationTime(userHandle);
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

            public boolean isRestricted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(25, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isRestricted();
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

            public boolean canHaveRestrictedProfile(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(26, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().canHaveRestrictedProfile(userHandle);
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

            public int getUserSerialNumber(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    int i = 27;
                    if (!this.mRemote.transact(27, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getUserSerialNumber(userHandle);
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

            public int getUserHandle(int userSerialNumber) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userSerialNumber);
                    int i = 28;
                    if (!this.mRemote.transact(28, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getUserHandle(userSerialNumber);
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

            public int getUserRestrictionSource(String restrictionKey, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(restrictionKey);
                    _data.writeInt(userHandle);
                    int i = 29;
                    if (!this.mRemote.transact(29, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getUserRestrictionSource(restrictionKey, userHandle);
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

            public List<EnforcingUser> getUserRestrictionSources(String restrictionKey, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(restrictionKey);
                    _data.writeInt(userHandle);
                    List<EnforcingUser> list = 30;
                    if (!this.mRemote.transact(30, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getUserRestrictionSources(restrictionKey, userHandle);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(EnforcingUser.CREATOR);
                    List<EnforcingUser> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle getUserRestrictions(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    Bundle bundle = 31;
                    if (!this.mRemote.transact(31, _data, _reply, 0)) {
                        bundle = Stub.getDefaultImpl();
                        if (bundle != 0) {
                            bundle = Stub.getDefaultImpl().getUserRestrictions(userHandle);
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

            public boolean hasBaseUserRestriction(String restrictionKey, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(restrictionKey);
                    _data.writeInt(userHandle);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(32, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasBaseUserRestriction(restrictionKey, userHandle);
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

            public boolean hasUserRestriction(String restrictionKey, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(restrictionKey);
                    _data.writeInt(userHandle);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(33, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasUserRestriction(restrictionKey, userHandle);
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

            public boolean hasUserRestrictionOnAnyUser(String restrictionKey) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(restrictionKey);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(34, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasUserRestrictionOnAnyUser(restrictionKey);
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

            public void setUserRestriction(String key, boolean value, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeInt(value ? 1 : 0);
                    _data.writeInt(userHandle);
                    if (this.mRemote.transact(35, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUserRestriction(key, value, userHandle);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setApplicationRestrictions(String packageName, Bundle restrictions, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (restrictions != null) {
                        _data.writeInt(1);
                        restrictions.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (this.mRemote.transact(36, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setApplicationRestrictions(packageName, restrictions, userHandle);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle getApplicationRestrictions(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    Bundle bundle = 37;
                    if (!this.mRemote.transact(37, _data, _reply, 0)) {
                        bundle = Stub.getDefaultImpl();
                        if (bundle != 0) {
                            bundle = Stub.getDefaultImpl().getApplicationRestrictions(packageName);
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

            public Bundle getApplicationRestrictionsForUser(String packageName, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userHandle);
                    Bundle bundle = 38;
                    if (!this.mRemote.transact(38, _data, _reply, 0)) {
                        bundle = Stub.getDefaultImpl();
                        if (bundle != 0) {
                            bundle = Stub.getDefaultImpl().getApplicationRestrictionsForUser(packageName, userHandle);
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

            public void setDefaultGuestRestrictions(Bundle restrictions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (restrictions != null) {
                        _data.writeInt(1);
                        restrictions.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(39, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDefaultGuestRestrictions(restrictions);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle getDefaultGuestRestrictions() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Bundle bundle = 40;
                    if (!this.mRemote.transact(40, _data, _reply, 0)) {
                        bundle = Stub.getDefaultImpl();
                        if (bundle != 0) {
                            bundle = Stub.getDefaultImpl().getDefaultGuestRestrictions();
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

            public boolean markGuestForDeletion(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(41, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().markGuestForDeletion(userHandle);
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

            public boolean isQuietModeEnabled(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(42, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isQuietModeEnabled(userHandle);
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

            public void setSeedAccountData(int userHandle, String accountName, String accountType, PersistableBundle accountOptions, boolean persist) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    _data.writeString(accountName);
                    _data.writeString(accountType);
                    int i = 1;
                    if (accountOptions != null) {
                        _data.writeInt(1);
                        accountOptions.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!persist) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(43, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSeedAccountData(userHandle, accountName, accountType, accountOptions, persist);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getSeedAccountName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 44;
                    if (!this.mRemote.transact(44, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getSeedAccountName();
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

            public String getSeedAccountType() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 45;
                    if (!this.mRemote.transact(45, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getSeedAccountType();
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

            public PersistableBundle getSeedAccountOptions() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    PersistableBundle persistableBundle = 46;
                    if (!this.mRemote.transact(46, _data, _reply, 0)) {
                        persistableBundle = Stub.getDefaultImpl();
                        if (persistableBundle != 0) {
                            persistableBundle = Stub.getDefaultImpl().getSeedAccountOptions();
                            return persistableBundle;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        persistableBundle = (PersistableBundle) PersistableBundle.CREATOR.createFromParcel(_reply);
                    } else {
                        persistableBundle = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return persistableBundle;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearSeedAccountData() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(47, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearSeedAccountData();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean someUserHasSeedAccount(String accountName, String accountType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(accountName);
                    _data.writeString(accountType);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(48, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().someUserHasSeedAccount(accountName, accountType);
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

            public boolean isManagedProfile(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(49, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isManagedProfile(userId);
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

            public boolean isDemoUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(50, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isDemoUser(userId);
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

            public UserInfo createProfileForUserEvenWhenDisallowed(String name, int flags, int userHandle, String[] disallowedPackages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(flags);
                    _data.writeInt(userHandle);
                    _data.writeStringArray(disallowedPackages);
                    UserInfo userInfo = 51;
                    if (!this.mRemote.transact(51, _data, _reply, 0)) {
                        userInfo = Stub.getDefaultImpl();
                        if (userInfo != 0) {
                            userInfo = Stub.getDefaultImpl().createProfileForUserEvenWhenDisallowed(name, flags, userHandle, disallowedPackages);
                            return userInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        userInfo = (UserInfo) UserInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        userInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return userInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isUserUnlockingOrUnlocked(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(52, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isUserUnlockingOrUnlocked(userId);
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

            public int getManagedProfileBadge(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    int i = 53;
                    if (!this.mRemote.transact(53, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getManagedProfileBadge(userId);
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

            public boolean isUserUnlocked(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(54, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isUserUnlocked(userId);
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

            public boolean isUserRunning(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(55, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isUserRunning(userId);
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

            public boolean isUserNameSet(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(56, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isUserNameSet(userHandle);
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

            public boolean hasRestrictedProfiles() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(57, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasRestrictedProfiles();
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

            public boolean requestQuietModeEnabled(String callingPackage, boolean enableQuietMode, int userHandle, IntentSender target) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean _result = true;
                    _data.writeInt(enableQuietMode ? 1 : 0);
                    _data.writeInt(userHandle);
                    if (target != null) {
                        _data.writeInt(1);
                        target.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(58, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().requestQuietModeEnabled(callingPackage, enableQuietMode, userHandle, target);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getUserName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 59;
                    if (!this.mRemote.transact(59, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getUserName();
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

            public long getUserStartRealtime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 60;
                    if (!this.mRemote.transact(60, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getUserStartRealtime();
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

            public long getUserUnlockRealtime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 61;
                    if (!this.mRemote.transact(61, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getUserUnlockRealtime();
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IUserManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IUserManager)) {
                return new Proxy(obj);
            }
            return (IUserManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getCredentialOwnerProfile";
                case 2:
                    return "getProfileParentId";
                case 3:
                    return "createUser";
                case 4:
                    return "createProfileForUser";
                case 5:
                    return "createRestrictedProfile";
                case 6:
                    return "setUserEnabled";
                case 7:
                    return "setUserAdmin";
                case 8:
                    return "evictCredentialEncryptionKey";
                case 9:
                    return "removeUser";
                case 10:
                    return "removeUserEvenWhenDisallowed";
                case 11:
                    return "setUserName";
                case 12:
                    return "setUserIcon";
                case 13:
                    return "getUserIcon";
                case 14:
                    return "getPrimaryUser";
                case 15:
                    return "getUsers";
                case 16:
                    return "getProfiles";
                case 17:
                    return "getProfileIds";
                case 18:
                    return "canAddMoreManagedProfiles";
                case 19:
                    return "getProfileParent";
                case 20:
                    return "isSameProfileGroup";
                case 21:
                    return "getUserInfo";
                case 22:
                    return "getUserAccount";
                case 23:
                    return "setUserAccount";
                case 24:
                    return "getUserCreationTime";
                case 25:
                    return "isRestricted";
                case 26:
                    return "canHaveRestrictedProfile";
                case 27:
                    return "getUserSerialNumber";
                case 28:
                    return "getUserHandle";
                case 29:
                    return "getUserRestrictionSource";
                case 30:
                    return "getUserRestrictionSources";
                case 31:
                    return "getUserRestrictions";
                case 32:
                    return "hasBaseUserRestriction";
                case 33:
                    return "hasUserRestriction";
                case 34:
                    return "hasUserRestrictionOnAnyUser";
                case 35:
                    return "setUserRestriction";
                case 36:
                    return "setApplicationRestrictions";
                case 37:
                    return "getApplicationRestrictions";
                case 38:
                    return "getApplicationRestrictionsForUser";
                case 39:
                    return "setDefaultGuestRestrictions";
                case 40:
                    return "getDefaultGuestRestrictions";
                case 41:
                    return "markGuestForDeletion";
                case 42:
                    return "isQuietModeEnabled";
                case 43:
                    return "setSeedAccountData";
                case 44:
                    return "getSeedAccountName";
                case 45:
                    return "getSeedAccountType";
                case 46:
                    return "getSeedAccountOptions";
                case 47:
                    return "clearSeedAccountData";
                case 48:
                    return "someUserHasSeedAccount";
                case 49:
                    return "isManagedProfile";
                case 50:
                    return "isDemoUser";
                case 51:
                    return "createProfileForUserEvenWhenDisallowed";
                case 52:
                    return "isUserUnlockingOrUnlocked";
                case 53:
                    return "getManagedProfileBadge";
                case 54:
                    return "isUserUnlocked";
                case 55:
                    return "isUserRunning";
                case 56:
                    return "isUserNameSet";
                case 57:
                    return "hasRestrictedProfiles";
                case 58:
                    return "requestQuietModeEnabled";
                case 59:
                    return "getUserName";
                case 60:
                    return "getUserStartRealtime";
                case 61:
                    return "getUserUnlockRealtime";
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
                int _result;
                UserInfo _result2;
                UserInfo _result3;
                boolean _result4;
                boolean _result5;
                UserInfo _result6;
                String _result7;
                int _result8;
                Bundle _result9;
                String _arg0;
                Bundle _arg12;
                long _result10;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        _result = getCredentialOwnerProfile(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        _result = getProfileParentId(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        _result2 = createUser(data.readString(), data.readInt());
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
                        _result3 = createProfileForUser(data.readString(), data.readInt(), data.readInt(), data.createStringArray());
                        reply.writeNoException();
                        if (_result3 != null) {
                            parcel2.writeInt(1);
                            _result3.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _result2 = createRestrictedProfile(data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result2 != null) {
                            parcel2.writeInt(1);
                            _result2.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        setUserEnabled(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        setUserAdmin(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        evictCredentialEncryptionKey(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        _result4 = removeUser(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        _result4 = removeUserEvenWhenDisallowed(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        setUserName(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 12:
                        Bitmap _arg13;
                        parcel.enforceInterface(descriptor);
                        int _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg13 = (Bitmap) Bitmap.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        setUserIcon(_arg02, _arg13);
                        reply.writeNoException();
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        ParcelFileDescriptor _result11 = getUserIcon(data.readInt());
                        reply.writeNoException();
                        if (_result11 != null) {
                            parcel2.writeInt(1);
                            _result11.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        UserInfo _result12 = getPrimaryUser();
                        reply.writeNoException();
                        if (_result12 != null) {
                            parcel2.writeInt(1);
                            _result12.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        List<UserInfo> _result13 = getUsers(_arg1);
                        reply.writeNoException();
                        parcel2.writeTypedList(_result13);
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        _result = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        List<UserInfo> _result14 = getProfiles(_result, _arg1);
                        reply.writeNoException();
                        parcel2.writeTypedList(_result14);
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        _result = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        int[] _result15 = getProfileIds(_result, _arg1);
                        reply.writeNoException();
                        parcel2.writeIntArray(_result15);
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        _result = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result5 = canAddMoreManagedProfiles(_result, _arg1);
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        _result6 = getProfileParent(data.readInt());
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        _result5 = isSameProfileGroup(data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        _result6 = getUserInfo(data.readInt());
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        _result7 = getUserAccount(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_result7);
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        setUserAccount(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        long _result16 = getUserCreationTime(data.readInt());
                        reply.writeNoException();
                        parcel2.writeLong(_result16);
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isRestricted();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        _result4 = canHaveRestrictedProfile(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        _result = getUserSerialNumber(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        _result = getUserHandle(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        _result8 = getUserRestrictionSource(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        List<EnforcingUser> _result17 = getUserRestrictionSources(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result17);
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        _result9 = getUserRestrictions(data.readInt());
                        reply.writeNoException();
                        if (_result9 != null) {
                            parcel2.writeInt(1);
                            _result9.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        _result5 = hasBaseUserRestriction(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        _result5 = hasUserRestriction(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        _result4 = hasUserRestrictionOnAnyUser(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 35:
                        parcel.enforceInterface(descriptor);
                        _result7 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setUserRestriction(_result7, _arg1, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        setApplicationRestrictions(_arg0, _arg12, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        _result9 = getApplicationRestrictions(data.readString());
                        reply.writeNoException();
                        if (_result9 != null) {
                            parcel2.writeInt(1);
                            _result9.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 38:
                        parcel.enforceInterface(descriptor);
                        Bundle _result18 = getApplicationRestrictionsForUser(data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result18 != null) {
                            parcel2.writeInt(1);
                            _result18.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 39:
                        Bundle _arg03;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        setDefaultGuestRestrictions(_arg03);
                        reply.writeNoException();
                        return true;
                    case 40:
                        parcel.enforceInterface(descriptor);
                        _arg12 = getDefaultGuestRestrictions();
                        reply.writeNoException();
                        if (_arg12 != null) {
                            parcel2.writeInt(1);
                            _arg12.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 41:
                        parcel.enforceInterface(descriptor);
                        _result4 = markGuestForDeletion(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 42:
                        parcel.enforceInterface(descriptor);
                        _result4 = isQuietModeEnabled(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 43:
                        PersistableBundle _arg3;
                        parcel.enforceInterface(descriptor);
                        int _arg04 = data.readInt();
                        String _arg14 = data.readString();
                        String _arg2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg3 = (PersistableBundle) PersistableBundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        setSeedAccountData(_arg04, _arg14, _arg2, _arg3, data.readInt() != 0);
                        reply.writeNoException();
                        return true;
                    case 44:
                        parcel.enforceInterface(descriptor);
                        _arg0 = getSeedAccountName();
                        reply.writeNoException();
                        parcel2.writeString(_arg0);
                        return true;
                    case 45:
                        parcel.enforceInterface(descriptor);
                        _arg0 = getSeedAccountType();
                        reply.writeNoException();
                        parcel2.writeString(_arg0);
                        return true;
                    case 46:
                        parcel.enforceInterface(descriptor);
                        PersistableBundle _result19 = getSeedAccountOptions();
                        reply.writeNoException();
                        if (_result19 != null) {
                            parcel2.writeInt(1);
                            _result19.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 47:
                        parcel.enforceInterface(descriptor);
                        clearSeedAccountData();
                        reply.writeNoException();
                        return true;
                    case 48:
                        parcel.enforceInterface(descriptor);
                        _result5 = someUserHasSeedAccount(data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 49:
                        parcel.enforceInterface(descriptor);
                        _result4 = isManagedProfile(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 50:
                        parcel.enforceInterface(descriptor);
                        _result4 = isDemoUser(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 51:
                        parcel.enforceInterface(descriptor);
                        _result3 = createProfileForUserEvenWhenDisallowed(data.readString(), data.readInt(), data.readInt(), data.createStringArray());
                        reply.writeNoException();
                        if (_result3 != null) {
                            parcel2.writeInt(1);
                            _result3.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 52:
                        parcel.enforceInterface(descriptor);
                        _result4 = isUserUnlockingOrUnlocked(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 53:
                        parcel.enforceInterface(descriptor);
                        _result = getManagedProfileBadge(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 54:
                        parcel.enforceInterface(descriptor);
                        _result4 = isUserUnlocked(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 55:
                        parcel.enforceInterface(descriptor);
                        _result4 = isUserRunning(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 56:
                        parcel.enforceInterface(descriptor);
                        _result4 = isUserNameSet(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 57:
                        parcel.enforceInterface(descriptor);
                        _arg1 = hasRestrictedProfiles();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 58:
                        IntentSender _arg32;
                        parcel.enforceInterface(descriptor);
                        _result7 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result8 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg32 = (IntentSender) IntentSender.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg32 = null;
                        }
                        boolean _result20 = requestQuietModeEnabled(_result7, _arg1, _result8, _arg32);
                        reply.writeNoException();
                        parcel2.writeInt(_result20);
                        return true;
                    case 59:
                        parcel.enforceInterface(descriptor);
                        _arg0 = getUserName();
                        reply.writeNoException();
                        parcel2.writeString(_arg0);
                        return true;
                    case 60:
                        parcel.enforceInterface(descriptor);
                        _result10 = getUserStartRealtime();
                        reply.writeNoException();
                        parcel2.writeLong(_result10);
                        return true;
                    case 61:
                        parcel.enforceInterface(descriptor);
                        _result10 = getUserUnlockRealtime();
                        reply.writeNoException();
                        parcel2.writeLong(_result10);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IUserManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IUserManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean canAddMoreManagedProfiles(int i, boolean z) throws RemoteException;

    boolean canHaveRestrictedProfile(int i) throws RemoteException;

    void clearSeedAccountData() throws RemoteException;

    UserInfo createProfileForUser(String str, int i, int i2, String[] strArr) throws RemoteException;

    UserInfo createProfileForUserEvenWhenDisallowed(String str, int i, int i2, String[] strArr) throws RemoteException;

    UserInfo createRestrictedProfile(String str, int i) throws RemoteException;

    UserInfo createUser(String str, int i) throws RemoteException;

    void evictCredentialEncryptionKey(int i) throws RemoteException;

    Bundle getApplicationRestrictions(String str) throws RemoteException;

    Bundle getApplicationRestrictionsForUser(String str, int i) throws RemoteException;

    int getCredentialOwnerProfile(int i) throws RemoteException;

    Bundle getDefaultGuestRestrictions() throws RemoteException;

    int getManagedProfileBadge(int i) throws RemoteException;

    UserInfo getPrimaryUser() throws RemoteException;

    int[] getProfileIds(int i, boolean z) throws RemoteException;

    UserInfo getProfileParent(int i) throws RemoteException;

    int getProfileParentId(int i) throws RemoteException;

    List<UserInfo> getProfiles(int i, boolean z) throws RemoteException;

    String getSeedAccountName() throws RemoteException;

    PersistableBundle getSeedAccountOptions() throws RemoteException;

    String getSeedAccountType() throws RemoteException;

    String getUserAccount(int i) throws RemoteException;

    long getUserCreationTime(int i) throws RemoteException;

    int getUserHandle(int i) throws RemoteException;

    ParcelFileDescriptor getUserIcon(int i) throws RemoteException;

    @UnsupportedAppUsage
    UserInfo getUserInfo(int i) throws RemoteException;

    String getUserName() throws RemoteException;

    int getUserRestrictionSource(String str, int i) throws RemoteException;

    List<EnforcingUser> getUserRestrictionSources(String str, int i) throws RemoteException;

    Bundle getUserRestrictions(int i) throws RemoteException;

    int getUserSerialNumber(int i) throws RemoteException;

    long getUserStartRealtime() throws RemoteException;

    long getUserUnlockRealtime() throws RemoteException;

    List<UserInfo> getUsers(boolean z) throws RemoteException;

    boolean hasBaseUserRestriction(String str, int i) throws RemoteException;

    boolean hasRestrictedProfiles() throws RemoteException;

    boolean hasUserRestriction(String str, int i) throws RemoteException;

    boolean hasUserRestrictionOnAnyUser(String str) throws RemoteException;

    boolean isDemoUser(int i) throws RemoteException;

    boolean isManagedProfile(int i) throws RemoteException;

    boolean isQuietModeEnabled(int i) throws RemoteException;

    boolean isRestricted() throws RemoteException;

    boolean isSameProfileGroup(int i, int i2) throws RemoteException;

    boolean isUserNameSet(int i) throws RemoteException;

    boolean isUserRunning(int i) throws RemoteException;

    boolean isUserUnlocked(int i) throws RemoteException;

    boolean isUserUnlockingOrUnlocked(int i) throws RemoteException;

    boolean markGuestForDeletion(int i) throws RemoteException;

    boolean removeUser(int i) throws RemoteException;

    boolean removeUserEvenWhenDisallowed(int i) throws RemoteException;

    boolean requestQuietModeEnabled(String str, boolean z, int i, IntentSender intentSender) throws RemoteException;

    void setApplicationRestrictions(String str, Bundle bundle, int i) throws RemoteException;

    void setDefaultGuestRestrictions(Bundle bundle) throws RemoteException;

    void setSeedAccountData(int i, String str, String str2, PersistableBundle persistableBundle, boolean z) throws RemoteException;

    void setUserAccount(int i, String str) throws RemoteException;

    void setUserAdmin(int i) throws RemoteException;

    void setUserEnabled(int i) throws RemoteException;

    void setUserIcon(int i, Bitmap bitmap) throws RemoteException;

    void setUserName(int i, String str) throws RemoteException;

    void setUserRestriction(String str, boolean z, int i) throws RemoteException;

    boolean someUserHasSeedAccount(String str, String str2) throws RemoteException;
}
