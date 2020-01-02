package miui.telephony;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ResultReceiver;
import java.util.List;

public interface IMiuiTelephony extends IInterface {

    public static class Default implements IMiuiTelephony {
        public void setDefaultVoiceSlotId(int slotId, String opPackageName) throws RemoteException {
        }

        public boolean setDefaultDataSlotId(int slotId, String opPackageName) throws RemoteException {
            return false;
        }

        public boolean isImsRegistered(int phoneId) throws RemoteException {
            return false;
        }

        public boolean isVideoTelephonyAvailable(int phoneId) throws RemoteException {
            return false;
        }

        public boolean isWifiCallingAvailable(int phoneId) throws RemoteException {
            return false;
        }

        public boolean isVolteEnabledByUser() throws RemoteException {
            return false;
        }

        public boolean isVolteEnabledByUserForSlot(int slotId) throws RemoteException {
            return false;
        }

        public boolean isVtEnabledByPlatform() throws RemoteException {
            return false;
        }

        public boolean isVtEnabledByPlatformForSlot(int slotId) throws RemoteException {
            return false;
        }

        public boolean isVolteEnabledByPlatform() throws RemoteException {
            return false;
        }

        public boolean isVolteEnabledByPlatformForSlot(int slotId) throws RemoteException {
            return false;
        }

        public int getSystemDefaultSlotId() throws RemoteException {
            return 0;
        }

        public boolean isGwsdSupport() throws RemoteException {
            return false;
        }

        public boolean isIccCardActivate(int slotId) throws RemoteException {
            return false;
        }

        public void setIccCardActivate(int slotId, boolean isActivate) throws RemoteException {
        }

        public List<String> getDeviceIdList(String callingPackage) throws RemoteException {
            return null;
        }

        public List<String> getImeiList(String callingPackage) throws RemoteException {
            return null;
        }

        public List<String> getMeidList(String callingPackage) throws RemoteException {
            return null;
        }

        public String getDeviceId(String callingPackage) throws RemoteException {
            return null;
        }

        public String getImei(int slotId, String callingPackage) throws RemoteException {
            return null;
        }

        public String getMeid(int slotId, String callingPackage) throws RemoteException {
            return null;
        }

        public String getSmallDeviceId(String callingPackage) throws RemoteException {
            return null;
        }

        public boolean isSameOperator(String numeric, String anotherNumeric) throws RemoteException {
            return false;
        }

        public String getSpn(String numeric, int slotId, String spn, boolean longName) throws RemoteException {
            return null;
        }

        public String onOperatorNumericOrNameSet(int slotId, String property, String value) throws RemoteException {
            return null;
        }

        public Bundle getCellLocationForSlot(int slotId, String callingPkg) throws RemoteException {
            return null;
        }

        public void setCallForwardingOption(int phoneId, int action, int reason, String number, ResultReceiver callback) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMiuiTelephony {
        private static final String DESCRIPTOR = "miui.telephony.IMiuiTelephony";
        static final int TRANSACTION_getCellLocationForSlot = 26;
        static final int TRANSACTION_getDeviceId = 19;
        static final int TRANSACTION_getDeviceIdList = 16;
        static final int TRANSACTION_getImei = 20;
        static final int TRANSACTION_getImeiList = 17;
        static final int TRANSACTION_getMeid = 21;
        static final int TRANSACTION_getMeidList = 18;
        static final int TRANSACTION_getSmallDeviceId = 22;
        static final int TRANSACTION_getSpn = 24;
        static final int TRANSACTION_getSystemDefaultSlotId = 12;
        static final int TRANSACTION_isGwsdSupport = 13;
        static final int TRANSACTION_isIccCardActivate = 14;
        static final int TRANSACTION_isImsRegistered = 3;
        static final int TRANSACTION_isSameOperator = 23;
        static final int TRANSACTION_isVideoTelephonyAvailable = 4;
        static final int TRANSACTION_isVolteEnabledByPlatform = 10;
        static final int TRANSACTION_isVolteEnabledByPlatformForSlot = 11;
        static final int TRANSACTION_isVolteEnabledByUser = 6;
        static final int TRANSACTION_isVolteEnabledByUserForSlot = 7;
        static final int TRANSACTION_isVtEnabledByPlatform = 8;
        static final int TRANSACTION_isVtEnabledByPlatformForSlot = 9;
        static final int TRANSACTION_isWifiCallingAvailable = 5;
        static final int TRANSACTION_onOperatorNumericOrNameSet = 25;
        static final int TRANSACTION_setCallForwardingOption = 27;
        static final int TRANSACTION_setDefaultDataSlotId = 2;
        static final int TRANSACTION_setDefaultVoiceSlotId = 1;
        static final int TRANSACTION_setIccCardActivate = 15;

        private static class Proxy implements IMiuiTelephony {
            public static IMiuiTelephony sDefaultImpl;
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

            public void setDefaultVoiceSlotId(int slotId, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeString(opPackageName);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDefaultVoiceSlotId(slotId, opPackageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setDefaultDataSlotId(int slotId, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeString(opPackageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setDefaultDataSlotId(slotId, opPackageName);
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

            public boolean isImsRegistered(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isImsRegistered(phoneId);
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

            public boolean isVideoTelephonyAvailable(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isVideoTelephonyAvailable(phoneId);
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

            public boolean isWifiCallingAvailable(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isWifiCallingAvailable(phoneId);
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

            public boolean isVolteEnabledByUser() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isVolteEnabledByUser();
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

            public boolean isVolteEnabledByUserForSlot(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isVolteEnabledByUserForSlot(slotId);
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

            public boolean isVtEnabledByPlatform() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isVtEnabledByPlatform();
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

            public boolean isVtEnabledByPlatformForSlot(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isVtEnabledByPlatformForSlot(slotId);
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

            public boolean isVolteEnabledByPlatform() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isVolteEnabledByPlatform();
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

            public boolean isVolteEnabledByPlatformForSlot(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isVolteEnabledByPlatformForSlot(slotId);
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

            public int getSystemDefaultSlotId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getSystemDefaultSlotId();
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

            public boolean isGwsdSupport() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isGwsdSupport();
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

            public boolean isIccCardActivate(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isIccCardActivate(slotId);
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

            public void setIccCardActivate(int slotId, boolean isActivate) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(isActivate ? 1 : 0);
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setIccCardActivate(slotId, isActivate);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getDeviceIdList(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    List<String> list = 16;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getDeviceIdList(callingPackage);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getImeiList(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    List<String> list = 17;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getImeiList(callingPackage);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getMeidList(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    List<String> list = 18;
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getMeidList(callingPackage);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getDeviceId(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    String str = 19;
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getDeviceId(callingPackage);
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

            public String getImei(int slotId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeString(callingPackage);
                    String str = 20;
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getImei(slotId, callingPackage);
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

            public String getMeid(int slotId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeString(callingPackage);
                    String str = 21;
                    if (!this.mRemote.transact(21, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getMeid(slotId, callingPackage);
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

            public String getSmallDeviceId(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    String str = 22;
                    if (!this.mRemote.transact(22, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getSmallDeviceId(callingPackage);
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

            public boolean isSameOperator(String numeric, String anotherNumeric) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(numeric);
                    _data.writeString(anotherNumeric);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(23, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isSameOperator(numeric, anotherNumeric);
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

            public String getSpn(String numeric, int slotId, String spn, boolean longName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(numeric);
                    _data.writeInt(slotId);
                    _data.writeString(spn);
                    _data.writeInt(longName ? 1 : 0);
                    String str = this.mRemote;
                    if (!str.transact(24, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != null) {
                            str = Stub.getDefaultImpl().getSpn(numeric, slotId, spn, longName);
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

            public String onOperatorNumericOrNameSet(int slotId, String property, String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeString(property);
                    _data.writeString(value);
                    String str = 25;
                    if (!this.mRemote.transact(25, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().onOperatorNumericOrNameSet(slotId, property, value);
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

            public Bundle getCellLocationForSlot(int slotId, String callingPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeString(callingPkg);
                    Bundle bundle = 26;
                    if (!this.mRemote.transact(26, _data, _reply, 0)) {
                        bundle = Stub.getDefaultImpl();
                        if (bundle != 0) {
                            bundle = Stub.getDefaultImpl().getCellLocationForSlot(slotId, callingPkg);
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

            public void setCallForwardingOption(int phoneId, int action, int reason, String number, ResultReceiver callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(action);
                    _data.writeInt(reason);
                    _data.writeString(number);
                    if (callback != null) {
                        _data.writeInt(1);
                        callback.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(27, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setCallForwardingOption(phoneId, action, reason, number, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMiuiTelephony asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMiuiTelephony)) {
                return new Proxy(obj);
            }
            return (IMiuiTelephony) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setDefaultVoiceSlotId";
                case 2:
                    return "setDefaultDataSlotId";
                case 3:
                    return "isImsRegistered";
                case 4:
                    return "isVideoTelephonyAvailable";
                case 5:
                    return "isWifiCallingAvailable";
                case 6:
                    return "isVolteEnabledByUser";
                case 7:
                    return "isVolteEnabledByUserForSlot";
                case 8:
                    return "isVtEnabledByPlatform";
                case 9:
                    return "isVtEnabledByPlatformForSlot";
                case 10:
                    return "isVolteEnabledByPlatform";
                case 11:
                    return "isVolteEnabledByPlatformForSlot";
                case 12:
                    return "getSystemDefaultSlotId";
                case 13:
                    return "isGwsdSupport";
                case 14:
                    return "isIccCardActivate";
                case 15:
                    return "setIccCardActivate";
                case 16:
                    return "getDeviceIdList";
                case 17:
                    return "getImeiList";
                case 18:
                    return "getMeidList";
                case 19:
                    return "getDeviceId";
                case 20:
                    return "getImei";
                case 21:
                    return "getMeid";
                case 22:
                    return "getSmallDeviceId";
                case 23:
                    return "isSameOperator";
                case 24:
                    return "getSpn";
                case 25:
                    return "onOperatorNumericOrNameSet";
                case 26:
                    return "getCellLocationForSlot";
                case 27:
                    return "setCallForwardingOption";
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
                boolean _arg3 = false;
                boolean _result;
                boolean _result2;
                List<String> _result3;
                String _result4;
                String _result5;
                String _arg2;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        setDefaultVoiceSlotId(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        _result = setDefaultDataSlotId(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        _result2 = isImsRegistered(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        _result2 = isVideoTelephonyAvailable(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _result2 = isWifiCallingAvailable(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        _arg3 = isVolteEnabledByUser();
                        reply.writeNoException();
                        parcel2.writeInt(_arg3);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        _result2 = isVolteEnabledByUserForSlot(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        _arg3 = isVtEnabledByPlatform();
                        reply.writeNoException();
                        parcel2.writeInt(_arg3);
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        _result2 = isVtEnabledByPlatformForSlot(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        _arg3 = isVolteEnabledByPlatform();
                        reply.writeNoException();
                        parcel2.writeInt(_arg3);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        _result2 = isVolteEnabledByPlatformForSlot(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        int _result6 = getSystemDefaultSlotId();
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        _arg3 = isGwsdSupport();
                        reply.writeNoException();
                        parcel2.writeInt(_arg3);
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        _result2 = isIccCardActivate(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        int _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg3 = true;
                        }
                        setIccCardActivate(_arg0, _arg3);
                        reply.writeNoException();
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        _result3 = getDeviceIdList(data.readString());
                        reply.writeNoException();
                        parcel2.writeStringList(_result3);
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        _result3 = getImeiList(data.readString());
                        reply.writeNoException();
                        parcel2.writeStringList(_result3);
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        _result3 = getMeidList(data.readString());
                        reply.writeNoException();
                        parcel2.writeStringList(_result3);
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        _result4 = getDeviceId(data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result4);
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        _result5 = getImei(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result5);
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        _result5 = getMeid(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result5);
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        _result4 = getSmallDeviceId(data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result4);
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        _result = isSameOperator(data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        _result4 = data.readString();
                        int _arg1 = data.readInt();
                        _arg2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg3 = true;
                        }
                        String _result7 = getSpn(_result4, _arg1, _arg2, _arg3);
                        reply.writeNoException();
                        parcel2.writeString(_result7);
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        _arg2 = onOperatorNumericOrNameSet(data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_arg2);
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        Bundle _result8 = getCellLocationForSlot(data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result8 != null) {
                            parcel2.writeInt(1);
                            _result8.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 27:
                        ResultReceiver _arg4;
                        parcel.enforceInterface(descriptor);
                        int _arg02 = data.readInt();
                        int _arg12 = data.readInt();
                        int _arg22 = data.readInt();
                        String _arg32 = data.readString();
                        if (data.readInt() != 0) {
                            _arg4 = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg4 = null;
                        }
                        setCallForwardingOption(_arg02, _arg12, _arg22, _arg32, _arg4);
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IMiuiTelephony impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMiuiTelephony getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    Bundle getCellLocationForSlot(int i, String str) throws RemoteException;

    String getDeviceId(String str) throws RemoteException;

    List<String> getDeviceIdList(String str) throws RemoteException;

    String getImei(int i, String str) throws RemoteException;

    List<String> getImeiList(String str) throws RemoteException;

    String getMeid(int i, String str) throws RemoteException;

    List<String> getMeidList(String str) throws RemoteException;

    String getSmallDeviceId(String str) throws RemoteException;

    String getSpn(String str, int i, String str2, boolean z) throws RemoteException;

    int getSystemDefaultSlotId() throws RemoteException;

    boolean isGwsdSupport() throws RemoteException;

    boolean isIccCardActivate(int i) throws RemoteException;

    boolean isImsRegistered(int i) throws RemoteException;

    boolean isSameOperator(String str, String str2) throws RemoteException;

    boolean isVideoTelephonyAvailable(int i) throws RemoteException;

    boolean isVolteEnabledByPlatform() throws RemoteException;

    boolean isVolteEnabledByPlatformForSlot(int i) throws RemoteException;

    boolean isVolteEnabledByUser() throws RemoteException;

    boolean isVolteEnabledByUserForSlot(int i) throws RemoteException;

    boolean isVtEnabledByPlatform() throws RemoteException;

    boolean isVtEnabledByPlatformForSlot(int i) throws RemoteException;

    boolean isWifiCallingAvailable(int i) throws RemoteException;

    String onOperatorNumericOrNameSet(int i, String str, String str2) throws RemoteException;

    void setCallForwardingOption(int i, int i2, int i3, String str, ResultReceiver resultReceiver) throws RemoteException;

    boolean setDefaultDataSlotId(int i, String str) throws RemoteException;

    void setDefaultVoiceSlotId(int i, String str) throws RemoteException;

    void setIccCardActivate(int i, boolean z) throws RemoteException;
}
