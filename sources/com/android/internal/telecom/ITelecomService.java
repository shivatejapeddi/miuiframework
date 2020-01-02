package com.android.internal.telecom;

import android.annotation.UnsupportedAppUsage;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomAnalytics;
import java.util.List;

public interface ITelecomService extends IInterface {

    public static class Default implements ITelecomService {
        public void showInCallScreen(boolean showDialpad, String callingPackage) throws RemoteException {
        }

        public PhoneAccountHandle getDefaultOutgoingPhoneAccount(String uriScheme, String callingPackage) throws RemoteException {
            return null;
        }

        public PhoneAccountHandle getUserSelectedOutgoingPhoneAccount(String callingPackage) throws RemoteException {
            return null;
        }

        public void setUserSelectedOutgoingPhoneAccount(PhoneAccountHandle account) throws RemoteException {
        }

        public List<PhoneAccountHandle> getCallCapablePhoneAccounts(boolean includeDisabledAccounts, String callingPackage) throws RemoteException {
            return null;
        }

        public List<PhoneAccountHandle> getSelfManagedPhoneAccounts(String callingPackage) throws RemoteException {
            return null;
        }

        public List<PhoneAccountHandle> getPhoneAccountsSupportingScheme(String uriScheme, String callingPackage) throws RemoteException {
            return null;
        }

        public List<PhoneAccountHandle> getPhoneAccountsForPackage(String packageName) throws RemoteException {
            return null;
        }

        public PhoneAccount getPhoneAccount(PhoneAccountHandle account) throws RemoteException {
            return null;
        }

        public int getAllPhoneAccountsCount() throws RemoteException {
            return 0;
        }

        public List<PhoneAccount> getAllPhoneAccounts() throws RemoteException {
            return null;
        }

        public List<PhoneAccountHandle> getAllPhoneAccountHandles() throws RemoteException {
            return null;
        }

        public PhoneAccountHandle getSimCallManager(int subId) throws RemoteException {
            return null;
        }

        public PhoneAccountHandle getSimCallManagerForUser(int userId) throws RemoteException {
            return null;
        }

        public void registerPhoneAccount(PhoneAccount metadata) throws RemoteException {
        }

        public void unregisterPhoneAccount(PhoneAccountHandle account) throws RemoteException {
        }

        public void clearAccounts(String packageName) throws RemoteException {
        }

        public boolean isVoiceMailNumber(PhoneAccountHandle accountHandle, String number, String callingPackage) throws RemoteException {
            return false;
        }

        public String getVoiceMailNumber(PhoneAccountHandle accountHandle, String callingPackage) throws RemoteException {
            return null;
        }

        public String getLine1Number(PhoneAccountHandle accountHandle, String callingPackage) throws RemoteException {
            return null;
        }

        public ComponentName getDefaultPhoneApp() throws RemoteException {
            return null;
        }

        public String getDefaultDialerPackage() throws RemoteException {
            return null;
        }

        public String getSystemDialerPackage() throws RemoteException {
            return null;
        }

        public TelecomAnalytics dumpCallAnalytics() throws RemoteException {
            return null;
        }

        public void silenceRinger(String callingPackage) throws RemoteException {
        }

        public boolean isInCall(String callingPackage) throws RemoteException {
            return false;
        }

        public boolean isInManagedCall(String callingPackage) throws RemoteException {
            return false;
        }

        public boolean isRinging(String callingPackage) throws RemoteException {
            return false;
        }

        public int getCallState() throws RemoteException {
            return 0;
        }

        public boolean endCall(String callingPackage) throws RemoteException {
            return false;
        }

        public void acceptRingingCall(String callingPackage) throws RemoteException {
        }

        public void acceptRingingCallWithVideoState(String callingPackage, int videoState) throws RemoteException {
        }

        public void cancelMissedCallsNotification(String callingPackage) throws RemoteException {
        }

        public boolean handlePinMmi(String dialString, String callingPackage) throws RemoteException {
            return false;
        }

        public boolean handlePinMmiForPhoneAccount(PhoneAccountHandle accountHandle, String dialString, String callingPackage) throws RemoteException {
            return false;
        }

        public Uri getAdnUriForPhoneAccount(PhoneAccountHandle accountHandle, String callingPackage) throws RemoteException {
            return null;
        }

        public boolean isTtySupported(String callingPackage) throws RemoteException {
            return false;
        }

        public int getCurrentTtyMode(String callingPackage) throws RemoteException {
            return 0;
        }

        public void addNewIncomingCall(PhoneAccountHandle phoneAccount, Bundle extras) throws RemoteException {
        }

        public void addNewUnknownCall(PhoneAccountHandle phoneAccount, Bundle extras) throws RemoteException {
        }

        public void placeCall(Uri handle, Bundle extras, String callingPackage) throws RemoteException {
        }

        public boolean enablePhoneAccount(PhoneAccountHandle accountHandle, boolean isEnabled) throws RemoteException {
            return false;
        }

        public boolean setDefaultDialer(String packageName) throws RemoteException {
            return false;
        }

        public Intent createManageBlockedNumbersIntent() throws RemoteException {
            return null;
        }

        public boolean isIncomingCallPermitted(PhoneAccountHandle phoneAccountHandle) throws RemoteException {
            return false;
        }

        public boolean isOutgoingCallPermitted(PhoneAccountHandle phoneAccountHandle) throws RemoteException {
            return false;
        }

        public void waitOnHandlers() throws RemoteException {
        }

        public void acceptHandover(Uri srcAddr, int videoState, PhoneAccountHandle destAcct) throws RemoteException {
        }

        public boolean isInEmergencyCall() throws RemoteException {
            return false;
        }

        public void handleCallIntent(Intent intent) throws RemoteException {
        }

        public void setTestDefaultCallRedirectionApp(String packageName) throws RemoteException {
        }

        public void setTestPhoneAcctSuggestionComponent(String flattenedComponentName) throws RemoteException {
        }

        public void setTestDefaultCallScreeningApp(String packageName) throws RemoteException {
        }

        public void addOrRemoveTestCallCompanionApp(String packageName, boolean isAdded) throws RemoteException {
        }

        public void setTestAutoModeApp(String packageName) throws RemoteException {
        }

        public void setTestDefaultDialer(String packageName) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ITelecomService {
        private static final String DESCRIPTOR = "com.android.internal.telecom.ITelecomService";
        static final int TRANSACTION_acceptHandover = 48;
        static final int TRANSACTION_acceptRingingCall = 31;
        static final int TRANSACTION_acceptRingingCallWithVideoState = 32;
        static final int TRANSACTION_addNewIncomingCall = 39;
        static final int TRANSACTION_addNewUnknownCall = 40;
        static final int TRANSACTION_addOrRemoveTestCallCompanionApp = 54;
        static final int TRANSACTION_cancelMissedCallsNotification = 33;
        static final int TRANSACTION_clearAccounts = 17;
        static final int TRANSACTION_createManageBlockedNumbersIntent = 44;
        static final int TRANSACTION_dumpCallAnalytics = 24;
        static final int TRANSACTION_enablePhoneAccount = 42;
        static final int TRANSACTION_endCall = 30;
        static final int TRANSACTION_getAdnUriForPhoneAccount = 36;
        static final int TRANSACTION_getAllPhoneAccountHandles = 12;
        static final int TRANSACTION_getAllPhoneAccounts = 11;
        static final int TRANSACTION_getAllPhoneAccountsCount = 10;
        static final int TRANSACTION_getCallCapablePhoneAccounts = 5;
        static final int TRANSACTION_getCallState = 29;
        static final int TRANSACTION_getCurrentTtyMode = 38;
        static final int TRANSACTION_getDefaultDialerPackage = 22;
        static final int TRANSACTION_getDefaultOutgoingPhoneAccount = 2;
        static final int TRANSACTION_getDefaultPhoneApp = 21;
        static final int TRANSACTION_getLine1Number = 20;
        static final int TRANSACTION_getPhoneAccount = 9;
        static final int TRANSACTION_getPhoneAccountsForPackage = 8;
        static final int TRANSACTION_getPhoneAccountsSupportingScheme = 7;
        static final int TRANSACTION_getSelfManagedPhoneAccounts = 6;
        static final int TRANSACTION_getSimCallManager = 13;
        static final int TRANSACTION_getSimCallManagerForUser = 14;
        static final int TRANSACTION_getSystemDialerPackage = 23;
        static final int TRANSACTION_getUserSelectedOutgoingPhoneAccount = 3;
        static final int TRANSACTION_getVoiceMailNumber = 19;
        static final int TRANSACTION_handleCallIntent = 50;
        static final int TRANSACTION_handlePinMmi = 34;
        static final int TRANSACTION_handlePinMmiForPhoneAccount = 35;
        static final int TRANSACTION_isInCall = 26;
        static final int TRANSACTION_isInEmergencyCall = 49;
        static final int TRANSACTION_isInManagedCall = 27;
        static final int TRANSACTION_isIncomingCallPermitted = 45;
        static final int TRANSACTION_isOutgoingCallPermitted = 46;
        static final int TRANSACTION_isRinging = 28;
        static final int TRANSACTION_isTtySupported = 37;
        static final int TRANSACTION_isVoiceMailNumber = 18;
        static final int TRANSACTION_placeCall = 41;
        static final int TRANSACTION_registerPhoneAccount = 15;
        static final int TRANSACTION_setDefaultDialer = 43;
        static final int TRANSACTION_setTestAutoModeApp = 55;
        static final int TRANSACTION_setTestDefaultCallRedirectionApp = 51;
        static final int TRANSACTION_setTestDefaultCallScreeningApp = 53;
        static final int TRANSACTION_setTestDefaultDialer = 56;
        static final int TRANSACTION_setTestPhoneAcctSuggestionComponent = 52;
        static final int TRANSACTION_setUserSelectedOutgoingPhoneAccount = 4;
        static final int TRANSACTION_showInCallScreen = 1;
        static final int TRANSACTION_silenceRinger = 25;
        static final int TRANSACTION_unregisterPhoneAccount = 16;
        static final int TRANSACTION_waitOnHandlers = 47;

        private static class Proxy implements ITelecomService {
            public static ITelecomService sDefaultImpl;
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

            public void showInCallScreen(boolean showDialpad, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(showDialpad ? 1 : 0);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().showInCallScreen(showDialpad, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public PhoneAccountHandle getDefaultOutgoingPhoneAccount(String uriScheme, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uriScheme);
                    _data.writeString(callingPackage);
                    PhoneAccountHandle phoneAccountHandle = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        phoneAccountHandle = Stub.getDefaultImpl();
                        if (phoneAccountHandle != 0) {
                            phoneAccountHandle = Stub.getDefaultImpl().getDefaultOutgoingPhoneAccount(uriScheme, callingPackage);
                            return phoneAccountHandle;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        phoneAccountHandle = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(_reply);
                    } else {
                        phoneAccountHandle = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return phoneAccountHandle;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public PhoneAccountHandle getUserSelectedOutgoingPhoneAccount(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    PhoneAccountHandle phoneAccountHandle = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        phoneAccountHandle = Stub.getDefaultImpl();
                        if (phoneAccountHandle != 0) {
                            phoneAccountHandle = Stub.getDefaultImpl().getUserSelectedOutgoingPhoneAccount(callingPackage);
                            return phoneAccountHandle;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        phoneAccountHandle = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(_reply);
                    } else {
                        phoneAccountHandle = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return phoneAccountHandle;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setUserSelectedOutgoingPhoneAccount(PhoneAccountHandle account) throws RemoteException {
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
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUserSelectedOutgoingPhoneAccount(account);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<PhoneAccountHandle> getCallCapablePhoneAccounts(boolean includeDisabledAccounts, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(includeDisabledAccounts ? 1 : 0);
                    _data.writeString(callingPackage);
                    List<PhoneAccountHandle> list = this.mRemote;
                    if (!list.transact(5, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != null) {
                            list = Stub.getDefaultImpl().getCallCapablePhoneAccounts(includeDisabledAccounts, callingPackage);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(PhoneAccountHandle.CREATOR);
                    List<PhoneAccountHandle> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<PhoneAccountHandle> getSelfManagedPhoneAccounts(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    List<PhoneAccountHandle> list = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getSelfManagedPhoneAccounts(callingPackage);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(PhoneAccountHandle.CREATOR);
                    List<PhoneAccountHandle> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<PhoneAccountHandle> getPhoneAccountsSupportingScheme(String uriScheme, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uriScheme);
                    _data.writeString(callingPackage);
                    List<PhoneAccountHandle> list = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getPhoneAccountsSupportingScheme(uriScheme, callingPackage);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(PhoneAccountHandle.CREATOR);
                    List<PhoneAccountHandle> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<PhoneAccountHandle> getPhoneAccountsForPackage(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    List<PhoneAccountHandle> list = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getPhoneAccountsForPackage(packageName);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(PhoneAccountHandle.CREATOR);
                    List<PhoneAccountHandle> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public PhoneAccount getPhoneAccount(PhoneAccountHandle account) throws RemoteException {
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
                    PhoneAccount phoneAccount = this.mRemote;
                    if (!phoneAccount.transact(9, _data, _reply, 0)) {
                        phoneAccount = Stub.getDefaultImpl();
                        if (phoneAccount != null) {
                            phoneAccount = Stub.getDefaultImpl().getPhoneAccount(account);
                            return phoneAccount;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        phoneAccount = (PhoneAccount) PhoneAccount.CREATOR.createFromParcel(_reply);
                    } else {
                        phoneAccount = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return phoneAccount;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getAllPhoneAccountsCount() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getAllPhoneAccountsCount();
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

            public List<PhoneAccount> getAllPhoneAccounts() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<PhoneAccount> list = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getAllPhoneAccounts();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(PhoneAccount.CREATOR);
                    List<PhoneAccount> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<PhoneAccountHandle> getAllPhoneAccountHandles() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<PhoneAccountHandle> list = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getAllPhoneAccountHandles();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(PhoneAccountHandle.CREATOR);
                    List<PhoneAccountHandle> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public PhoneAccountHandle getSimCallManager(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    PhoneAccountHandle phoneAccountHandle = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        phoneAccountHandle = Stub.getDefaultImpl();
                        if (phoneAccountHandle != 0) {
                            phoneAccountHandle = Stub.getDefaultImpl().getSimCallManager(subId);
                            return phoneAccountHandle;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        phoneAccountHandle = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(_reply);
                    } else {
                        phoneAccountHandle = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return phoneAccountHandle;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public PhoneAccountHandle getSimCallManagerForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    PhoneAccountHandle phoneAccountHandle = 14;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        phoneAccountHandle = Stub.getDefaultImpl();
                        if (phoneAccountHandle != 0) {
                            phoneAccountHandle = Stub.getDefaultImpl().getSimCallManagerForUser(userId);
                            return phoneAccountHandle;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        phoneAccountHandle = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(_reply);
                    } else {
                        phoneAccountHandle = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return phoneAccountHandle;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerPhoneAccount(PhoneAccount metadata) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (metadata != null) {
                        _data.writeInt(1);
                        metadata.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerPhoneAccount(metadata);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterPhoneAccount(PhoneAccountHandle account) throws RemoteException {
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
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterPhoneAccount(account);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearAccounts(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearAccounts(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isVoiceMailNumber(PhoneAccountHandle accountHandle, String number, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (accountHandle != null) {
                        _data.writeInt(1);
                        accountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(number);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isVoiceMailNumber(accountHandle, number, callingPackage);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getVoiceMailNumber(PhoneAccountHandle accountHandle, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (accountHandle != null) {
                        _data.writeInt(1);
                        accountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    String str = this.mRemote;
                    if (!str.transact(19, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != null) {
                            str = Stub.getDefaultImpl().getVoiceMailNumber(accountHandle, callingPackage);
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

            public String getLine1Number(PhoneAccountHandle accountHandle, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (accountHandle != null) {
                        _data.writeInt(1);
                        accountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    String str = this.mRemote;
                    if (!str.transact(20, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != null) {
                            str = Stub.getDefaultImpl().getLine1Number(accountHandle, callingPackage);
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

            public ComponentName getDefaultPhoneApp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ComponentName componentName = 21;
                    if (!this.mRemote.transact(21, _data, _reply, 0)) {
                        componentName = Stub.getDefaultImpl();
                        if (componentName != 0) {
                            componentName = Stub.getDefaultImpl().getDefaultPhoneApp();
                            return componentName;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        componentName = (ComponentName) ComponentName.CREATOR.createFromParcel(_reply);
                    } else {
                        componentName = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return componentName;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getDefaultDialerPackage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 22;
                    if (!this.mRemote.transact(22, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getDefaultDialerPackage();
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

            public String getSystemDialerPackage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 23;
                    if (!this.mRemote.transact(23, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getSystemDialerPackage();
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

            public TelecomAnalytics dumpCallAnalytics() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    TelecomAnalytics telecomAnalytics = 24;
                    if (!this.mRemote.transact(24, _data, _reply, 0)) {
                        telecomAnalytics = Stub.getDefaultImpl();
                        if (telecomAnalytics != 0) {
                            telecomAnalytics = Stub.getDefaultImpl().dumpCallAnalytics();
                            return telecomAnalytics;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        telecomAnalytics = (TelecomAnalytics) TelecomAnalytics.CREATOR.createFromParcel(_reply);
                    } else {
                        telecomAnalytics = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return telecomAnalytics;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void silenceRinger(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().silenceRinger(callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isInCall(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(26, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isInCall(callingPackage);
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

            public boolean isInManagedCall(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(27, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isInManagedCall(callingPackage);
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

            public boolean isRinging(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(28, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isRinging(callingPackage);
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

            public int getCallState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 29;
                    if (!this.mRemote.transact(29, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getCallState();
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

            public boolean endCall(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(30, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().endCall(callingPackage);
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

            public void acceptRingingCall(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(31, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().acceptRingingCall(callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void acceptRingingCallWithVideoState(String callingPackage, int videoState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(videoState);
                    if (this.mRemote.transact(32, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().acceptRingingCallWithVideoState(callingPackage, videoState);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancelMissedCallsNotification(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(33, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancelMissedCallsNotification(callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean handlePinMmi(String dialString, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(dialString);
                    _data.writeString(callingPackage);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(34, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().handlePinMmi(dialString, callingPackage);
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

            public boolean handlePinMmiForPhoneAccount(PhoneAccountHandle accountHandle, String dialString, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (accountHandle != null) {
                        _data.writeInt(1);
                        accountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(dialString);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(35, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().handlePinMmiForPhoneAccount(accountHandle, dialString, callingPackage);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Uri getAdnUriForPhoneAccount(PhoneAccountHandle accountHandle, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (accountHandle != null) {
                        _data.writeInt(1);
                        accountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    Uri uri = this.mRemote;
                    if (!uri.transact(36, _data, _reply, 0)) {
                        uri = Stub.getDefaultImpl();
                        if (uri != null) {
                            uri = Stub.getDefaultImpl().getAdnUriForPhoneAccount(accountHandle, callingPackage);
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

            public boolean isTtySupported(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(37, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isTtySupported(callingPackage);
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

            public int getCurrentTtyMode(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    int i = 38;
                    if (!this.mRemote.transact(38, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getCurrentTtyMode(callingPackage);
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

            public void addNewIncomingCall(PhoneAccountHandle phoneAccount, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (phoneAccount != null) {
                        _data.writeInt(1);
                        phoneAccount.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(39, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addNewIncomingCall(phoneAccount, extras);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addNewUnknownCall(PhoneAccountHandle phoneAccount, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (phoneAccount != null) {
                        _data.writeInt(1);
                        phoneAccount.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(40, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addNewUnknownCall(phoneAccount, extras);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void placeCall(Uri handle, Bundle extras, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (handle != null) {
                        _data.writeInt(1);
                        handle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(41, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().placeCall(handle, extras, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean enablePhoneAccount(PhoneAccountHandle accountHandle, boolean isEnabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (accountHandle != null) {
                        _data.writeInt(1);
                        accountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(isEnabled ? 1 : 0);
                    if (this.mRemote.transact(42, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().enablePhoneAccount(accountHandle, isEnabled);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setDefaultDialer(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(43, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setDefaultDialer(packageName);
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

            public Intent createManageBlockedNumbersIntent() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Intent intent = 44;
                    if (!this.mRemote.transact(44, _data, _reply, 0)) {
                        intent = Stub.getDefaultImpl();
                        if (intent != 0) {
                            intent = Stub.getDefaultImpl().createManageBlockedNumbersIntent();
                            return intent;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        intent = (Intent) Intent.CREATOR.createFromParcel(_reply);
                    } else {
                        intent = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return intent;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isIncomingCallPermitted(PhoneAccountHandle phoneAccountHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (phoneAccountHandle != null) {
                        _data.writeInt(1);
                        phoneAccountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(45, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isIncomingCallPermitted(phoneAccountHandle);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isOutgoingCallPermitted(PhoneAccountHandle phoneAccountHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (phoneAccountHandle != null) {
                        _data.writeInt(1);
                        phoneAccountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(46, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isOutgoingCallPermitted(phoneAccountHandle);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void waitOnHandlers() throws RemoteException {
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
                    Stub.getDefaultImpl().waitOnHandlers();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void acceptHandover(Uri srcAddr, int videoState, PhoneAccountHandle destAcct) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (srcAddr != null) {
                        _data.writeInt(1);
                        srcAddr.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(videoState);
                    if (destAcct != null) {
                        _data.writeInt(1);
                        destAcct.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(48, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().acceptHandover(srcAddr, videoState, destAcct);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isInEmergencyCall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(49, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isInEmergencyCall();
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

            public void handleCallIntent(Intent intent) throws RemoteException {
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
                    if (this.mRemote.transact(50, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().handleCallIntent(intent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setTestDefaultCallRedirectionApp(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(51, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setTestDefaultCallRedirectionApp(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setTestPhoneAcctSuggestionComponent(String flattenedComponentName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(flattenedComponentName);
                    if (this.mRemote.transact(52, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setTestPhoneAcctSuggestionComponent(flattenedComponentName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setTestDefaultCallScreeningApp(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(53, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setTestDefaultCallScreeningApp(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addOrRemoveTestCallCompanionApp(String packageName, boolean isAdded) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(isAdded ? 1 : 0);
                    if (this.mRemote.transact(54, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addOrRemoveTestCallCompanionApp(packageName, isAdded);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setTestAutoModeApp(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(55, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setTestAutoModeApp(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setTestDefaultDialer(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(56, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setTestDefaultDialer(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITelecomService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ITelecomService)) {
                return new Proxy(obj);
            }
            return (ITelecomService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "showInCallScreen";
                case 2:
                    return "getDefaultOutgoingPhoneAccount";
                case 3:
                    return "getUserSelectedOutgoingPhoneAccount";
                case 4:
                    return "setUserSelectedOutgoingPhoneAccount";
                case 5:
                    return "getCallCapablePhoneAccounts";
                case 6:
                    return "getSelfManagedPhoneAccounts";
                case 7:
                    return "getPhoneAccountsSupportingScheme";
                case 8:
                    return "getPhoneAccountsForPackage";
                case 9:
                    return "getPhoneAccount";
                case 10:
                    return "getAllPhoneAccountsCount";
                case 11:
                    return "getAllPhoneAccounts";
                case 12:
                    return "getAllPhoneAccountHandles";
                case 13:
                    return "getSimCallManager";
                case 14:
                    return "getSimCallManagerForUser";
                case 15:
                    return "registerPhoneAccount";
                case 16:
                    return "unregisterPhoneAccount";
                case 17:
                    return "clearAccounts";
                case 18:
                    return "isVoiceMailNumber";
                case 19:
                    return "getVoiceMailNumber";
                case 20:
                    return "getLine1Number";
                case 21:
                    return "getDefaultPhoneApp";
                case 22:
                    return "getDefaultDialerPackage";
                case 23:
                    return "getSystemDialerPackage";
                case 24:
                    return "dumpCallAnalytics";
                case 25:
                    return "silenceRinger";
                case 26:
                    return "isInCall";
                case 27:
                    return "isInManagedCall";
                case 28:
                    return "isRinging";
                case 29:
                    return "getCallState";
                case 30:
                    return "endCall";
                case 31:
                    return "acceptRingingCall";
                case 32:
                    return "acceptRingingCallWithVideoState";
                case 33:
                    return "cancelMissedCallsNotification";
                case 34:
                    return "handlePinMmi";
                case 35:
                    return "handlePinMmiForPhoneAccount";
                case 36:
                    return "getAdnUriForPhoneAccount";
                case 37:
                    return "isTtySupported";
                case 38:
                    return "getCurrentTtyMode";
                case 39:
                    return "addNewIncomingCall";
                case 40:
                    return "addNewUnknownCall";
                case 41:
                    return "placeCall";
                case 42:
                    return "enablePhoneAccount";
                case 43:
                    return "setDefaultDialer";
                case 44:
                    return "createManageBlockedNumbersIntent";
                case 45:
                    return "isIncomingCallPermitted";
                case 46:
                    return "isOutgoingCallPermitted";
                case 47:
                    return "waitOnHandlers";
                case 48:
                    return "acceptHandover";
                case 49:
                    return "isInEmergencyCall";
                case 50:
                    return "handleCallIntent";
                case 51:
                    return "setTestDefaultCallRedirectionApp";
                case 52:
                    return "setTestPhoneAcctSuggestionComponent";
                case 53:
                    return "setTestDefaultCallScreeningApp";
                case 54:
                    return "addOrRemoveTestCallCompanionApp";
                case 55:
                    return "setTestAutoModeApp";
                case 56:
                    return "setTestDefaultDialer";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code != IBinder.INTERFACE_TRANSACTION) {
                boolean _arg0 = false;
                PhoneAccountHandle _result;
                PhoneAccountHandle _arg02;
                List<PhoneAccountHandle> _result2;
                List<PhoneAccountHandle> _result3;
                PhoneAccountHandle _arg03;
                int _result4;
                boolean _result5;
                String _result6;
                String _result7;
                boolean _result8;
                boolean _result9;
                int _result10;
                Bundle _arg1;
                Uri _arg04;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        showInCallScreen(_arg0, data.readString());
                        reply.writeNoException();
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        PhoneAccountHandle _result11 = getDefaultOutgoingPhoneAccount(data.readString(), data.readString());
                        reply.writeNoException();
                        if (_result11 != null) {
                            reply.writeInt(1);
                            _result11.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        _result = getUserSelectedOutgoingPhoneAccount(data.readString());
                        reply.writeNoException();
                        if (_result != null) {
                            reply.writeInt(1);
                            _result.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        setUserSelectedOutgoingPhoneAccount(_arg02);
                        reply.writeNoException();
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        _result2 = getCallCapablePhoneAccounts(_arg0, data.readString());
                        reply.writeNoException();
                        reply.writeTypedList(_result2);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        _result3 = getSelfManagedPhoneAccounts(data.readString());
                        reply.writeNoException();
                        reply.writeTypedList(_result3);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _result2 = getPhoneAccountsSupportingScheme(data.readString(), data.readString());
                        reply.writeNoException();
                        reply.writeTypedList(_result2);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        _result3 = getPhoneAccountsForPackage(data.readString());
                        reply.writeNoException();
                        reply.writeTypedList(_result3);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        PhoneAccount _result12 = getPhoneAccount(_arg03);
                        reply.writeNoException();
                        if (_result12 != null) {
                            reply.writeInt(1);
                            _result12.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        _result4 = getAllPhoneAccountsCount();
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        List<PhoneAccount> _result13 = getAllPhoneAccounts();
                        reply.writeNoException();
                        reply.writeTypedList(_result13);
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        List<PhoneAccountHandle> _result14 = getAllPhoneAccountHandles();
                        reply.writeNoException();
                        reply.writeTypedList(_result14);
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        _result = getSimCallManager(data.readInt());
                        reply.writeNoException();
                        if (_result != null) {
                            reply.writeInt(1);
                            _result.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        _result = getSimCallManagerForUser(data.readInt());
                        reply.writeNoException();
                        if (_result != null) {
                            reply.writeInt(1);
                            _result.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 15:
                        PhoneAccount _arg05;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (PhoneAccount) PhoneAccount.CREATOR.createFromParcel(data);
                        } else {
                            _arg05 = null;
                        }
                        registerPhoneAccount(_arg05);
                        reply.writeNoException();
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        unregisterPhoneAccount(_arg02);
                        reply.writeNoException();
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        clearAccounts(data.readString());
                        reply.writeNoException();
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        _result5 = isVoiceMailNumber(_arg02, data.readString(), data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result5);
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        _result6 = getVoiceMailNumber(_arg02, data.readString());
                        reply.writeNoException();
                        reply.writeString(_result6);
                        return true;
                    case 20:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        _result6 = getLine1Number(_arg02, data.readString());
                        reply.writeNoException();
                        reply.writeString(_result6);
                        return true;
                    case 21:
                        data.enforceInterface(descriptor);
                        ComponentName _result15 = getDefaultPhoneApp();
                        reply.writeNoException();
                        if (_result15 != null) {
                            reply.writeInt(1);
                            _result15.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 22:
                        data.enforceInterface(descriptor);
                        _result7 = getDefaultDialerPackage();
                        reply.writeNoException();
                        reply.writeString(_result7);
                        return true;
                    case 23:
                        data.enforceInterface(descriptor);
                        _result7 = getSystemDialerPackage();
                        reply.writeNoException();
                        reply.writeString(_result7);
                        return true;
                    case 24:
                        data.enforceInterface(descriptor);
                        TelecomAnalytics _result16 = dumpCallAnalytics();
                        reply.writeNoException();
                        if (_result16 != null) {
                            reply.writeInt(1);
                            _result16.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 25:
                        data.enforceInterface(descriptor);
                        silenceRinger(data.readString());
                        reply.writeNoException();
                        return true;
                    case 26:
                        data.enforceInterface(descriptor);
                        _result8 = isInCall(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result8);
                        return true;
                    case 27:
                        data.enforceInterface(descriptor);
                        _result8 = isInManagedCall(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result8);
                        return true;
                    case 28:
                        data.enforceInterface(descriptor);
                        _result8 = isRinging(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result8);
                        return true;
                    case 29:
                        data.enforceInterface(descriptor);
                        _result4 = getCallState();
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 30:
                        data.enforceInterface(descriptor);
                        _result8 = endCall(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result8);
                        return true;
                    case 31:
                        data.enforceInterface(descriptor);
                        acceptRingingCall(data.readString());
                        reply.writeNoException();
                        return true;
                    case 32:
                        data.enforceInterface(descriptor);
                        acceptRingingCallWithVideoState(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 33:
                        data.enforceInterface(descriptor);
                        cancelMissedCallsNotification(data.readString());
                        reply.writeNoException();
                        return true;
                    case 34:
                        data.enforceInterface(descriptor);
                        _result9 = handlePinMmi(data.readString(), data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result9);
                        return true;
                    case 35:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        _result5 = handlePinMmiForPhoneAccount(_arg02, data.readString(), data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result5);
                        return true;
                    case 36:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        Uri _result17 = getAdnUriForPhoneAccount(_arg03, data.readString());
                        reply.writeNoException();
                        if (_result17 != null) {
                            reply.writeInt(1);
                            _result17.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 37:
                        data.enforceInterface(descriptor);
                        _result8 = isTtySupported(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result8);
                        return true;
                    case 38:
                        data.enforceInterface(descriptor);
                        _result10 = getCurrentTtyMode(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result10);
                        return true;
                    case 39:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        addNewIncomingCall(_arg02, _arg1);
                        reply.writeNoException();
                        return true;
                    case 40:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        addNewUnknownCall(_arg02, _arg1);
                        reply.writeNoException();
                        return true;
                    case 41:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (Uri) Uri.CREATOR.createFromParcel(data);
                        } else {
                            _arg04 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        placeCall(_arg04, _arg1, data.readString());
                        reply.writeNoException();
                        return true;
                    case 42:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        _result9 = enablePhoneAccount(_arg03, _arg0);
                        reply.writeNoException();
                        reply.writeInt(_result9);
                        return true;
                    case 43:
                        data.enforceInterface(descriptor);
                        _result8 = setDefaultDialer(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result8);
                        return true;
                    case 44:
                        data.enforceInterface(descriptor);
                        Intent _result18 = createManageBlockedNumbersIntent();
                        reply.writeNoException();
                        if (_result18 != null) {
                            reply.writeInt(1);
                            _result18.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 45:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        _result8 = isIncomingCallPermitted(_arg02);
                        reply.writeNoException();
                        reply.writeInt(_result8);
                        return true;
                    case 46:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        _result8 = isOutgoingCallPermitted(_arg02);
                        reply.writeNoException();
                        reply.writeInt(_result8);
                        return true;
                    case 47:
                        data.enforceInterface(descriptor);
                        waitOnHandlers();
                        reply.writeNoException();
                        return true;
                    case 48:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (Uri) Uri.CREATOR.createFromParcel(data);
                        } else {
                            _arg04 = null;
                        }
                        _result10 = data.readInt();
                        if (data.readInt() != 0) {
                            _result = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                        } else {
                            _result = null;
                        }
                        acceptHandover(_arg04, _result10, _result);
                        reply.writeNoException();
                        return true;
                    case 49:
                        data.enforceInterface(descriptor);
                        _arg0 = isInEmergencyCall();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 50:
                        Intent _arg06;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg06 = (Intent) Intent.CREATOR.createFromParcel(data);
                        } else {
                            _arg06 = null;
                        }
                        handleCallIntent(_arg06);
                        reply.writeNoException();
                        return true;
                    case 51:
                        data.enforceInterface(descriptor);
                        setTestDefaultCallRedirectionApp(data.readString());
                        reply.writeNoException();
                        return true;
                    case 52:
                        data.enforceInterface(descriptor);
                        setTestPhoneAcctSuggestionComponent(data.readString());
                        reply.writeNoException();
                        return true;
                    case 53:
                        data.enforceInterface(descriptor);
                        setTestDefaultCallScreeningApp(data.readString());
                        reply.writeNoException();
                        return true;
                    case 54:
                        data.enforceInterface(descriptor);
                        String _arg07 = data.readString();
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        addOrRemoveTestCallCompanionApp(_arg07, _arg0);
                        reply.writeNoException();
                        return true;
                    case 55:
                        data.enforceInterface(descriptor);
                        setTestAutoModeApp(data.readString());
                        reply.writeNoException();
                        return true;
                    case 56:
                        data.enforceInterface(descriptor);
                        setTestDefaultDialer(data.readString());
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(ITelecomService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ITelecomService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void acceptHandover(Uri uri, int i, PhoneAccountHandle phoneAccountHandle) throws RemoteException;

    void acceptRingingCall(String str) throws RemoteException;

    void acceptRingingCallWithVideoState(String str, int i) throws RemoteException;

    void addNewIncomingCall(PhoneAccountHandle phoneAccountHandle, Bundle bundle) throws RemoteException;

    void addNewUnknownCall(PhoneAccountHandle phoneAccountHandle, Bundle bundle) throws RemoteException;

    void addOrRemoveTestCallCompanionApp(String str, boolean z) throws RemoteException;

    void cancelMissedCallsNotification(String str) throws RemoteException;

    void clearAccounts(String str) throws RemoteException;

    Intent createManageBlockedNumbersIntent() throws RemoteException;

    TelecomAnalytics dumpCallAnalytics() throws RemoteException;

    boolean enablePhoneAccount(PhoneAccountHandle phoneAccountHandle, boolean z) throws RemoteException;

    boolean endCall(String str) throws RemoteException;

    Uri getAdnUriForPhoneAccount(PhoneAccountHandle phoneAccountHandle, String str) throws RemoteException;

    List<PhoneAccountHandle> getAllPhoneAccountHandles() throws RemoteException;

    List<PhoneAccount> getAllPhoneAccounts() throws RemoteException;

    int getAllPhoneAccountsCount() throws RemoteException;

    List<PhoneAccountHandle> getCallCapablePhoneAccounts(boolean z, String str) throws RemoteException;

    @UnsupportedAppUsage
    int getCallState() throws RemoteException;

    int getCurrentTtyMode(String str) throws RemoteException;

    String getDefaultDialerPackage() throws RemoteException;

    PhoneAccountHandle getDefaultOutgoingPhoneAccount(String str, String str2) throws RemoteException;

    ComponentName getDefaultPhoneApp() throws RemoteException;

    String getLine1Number(PhoneAccountHandle phoneAccountHandle, String str) throws RemoteException;

    PhoneAccount getPhoneAccount(PhoneAccountHandle phoneAccountHandle) throws RemoteException;

    List<PhoneAccountHandle> getPhoneAccountsForPackage(String str) throws RemoteException;

    List<PhoneAccountHandle> getPhoneAccountsSupportingScheme(String str, String str2) throws RemoteException;

    List<PhoneAccountHandle> getSelfManagedPhoneAccounts(String str) throws RemoteException;

    PhoneAccountHandle getSimCallManager(int i) throws RemoteException;

    PhoneAccountHandle getSimCallManagerForUser(int i) throws RemoteException;

    String getSystemDialerPackage() throws RemoteException;

    PhoneAccountHandle getUserSelectedOutgoingPhoneAccount(String str) throws RemoteException;

    String getVoiceMailNumber(PhoneAccountHandle phoneAccountHandle, String str) throws RemoteException;

    void handleCallIntent(Intent intent) throws RemoteException;

    boolean handlePinMmi(String str, String str2) throws RemoteException;

    boolean handlePinMmiForPhoneAccount(PhoneAccountHandle phoneAccountHandle, String str, String str2) throws RemoteException;

    boolean isInCall(String str) throws RemoteException;

    boolean isInEmergencyCall() throws RemoteException;

    boolean isInManagedCall(String str) throws RemoteException;

    boolean isIncomingCallPermitted(PhoneAccountHandle phoneAccountHandle) throws RemoteException;

    boolean isOutgoingCallPermitted(PhoneAccountHandle phoneAccountHandle) throws RemoteException;

    boolean isRinging(String str) throws RemoteException;

    boolean isTtySupported(String str) throws RemoteException;

    boolean isVoiceMailNumber(PhoneAccountHandle phoneAccountHandle, String str, String str2) throws RemoteException;

    void placeCall(Uri uri, Bundle bundle, String str) throws RemoteException;

    void registerPhoneAccount(PhoneAccount phoneAccount) throws RemoteException;

    boolean setDefaultDialer(String str) throws RemoteException;

    void setTestAutoModeApp(String str) throws RemoteException;

    void setTestDefaultCallRedirectionApp(String str) throws RemoteException;

    void setTestDefaultCallScreeningApp(String str) throws RemoteException;

    void setTestDefaultDialer(String str) throws RemoteException;

    void setTestPhoneAcctSuggestionComponent(String str) throws RemoteException;

    void setUserSelectedOutgoingPhoneAccount(PhoneAccountHandle phoneAccountHandle) throws RemoteException;

    void showInCallScreen(boolean z, String str) throws RemoteException;

    void silenceRinger(String str) throws RemoteException;

    void unregisterPhoneAccount(PhoneAccountHandle phoneAccountHandle) throws RemoteException;

    void waitOnHandlers() throws RemoteException;
}
