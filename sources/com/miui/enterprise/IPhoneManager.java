package com.miui.enterprise;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IPhoneManager extends IInterface {

    public static class Default implements IPhoneManager {
        public void controlSMS(int flags, int userId) throws RemoteException {
        }

        public void controlPhoneCall(int flags, int userId) throws RemoteException {
        }

        public void controlCellular(int flags, int userId) throws RemoteException {
        }

        public int getSMSStatus(int userId) throws RemoteException {
            return 0;
        }

        public int getPhoneCallStatus(int userId) throws RemoteException {
            return 0;
        }

        public int getCellularStatus(int userId) throws RemoteException {
            return 0;
        }

        public String getIMEI(int slotId) throws RemoteException {
            return null;
        }

        public void setPhoneCallAutoRecord(boolean isAutoRecord, int userId) throws RemoteException {
        }

        public void setPhoneCallAutoRecordDir(String dir) throws RemoteException {
        }

        public boolean isAutoRecordPhoneCall(int userId) throws RemoteException {
            return false;
        }

        public void setSMSBlackList(List<String> list, int userId) throws RemoteException {
        }

        public void setSMSWhiteList(List<String> list, int userId) throws RemoteException {
        }

        public List<String> getSMSBlackList(int userId) throws RemoteException {
            return null;
        }

        public List<String> getSMSWhiteList(int userId) throws RemoteException {
            return null;
        }

        public void setSMSContactRestriction(int mode, int userId) throws RemoteException {
        }

        public int getSMSContactRestriction(int userId) throws RemoteException {
            return 0;
        }

        public void setCallBlackList(List<String> list, int userId) throws RemoteException {
        }

        public void setCallWhiteList(List<String> list, int userId) throws RemoteException {
        }

        public List<String> getCallBlackList(int userId) throws RemoteException {
            return null;
        }

        public List<String> getCallWhiteList(int userId) throws RemoteException {
            return null;
        }

        public void setCallContactRestriction(int mode, int userId) throws RemoteException {
        }

        public int getCallContactRestriction(int userId) throws RemoteException {
            return 0;
        }

        public void endCall() throws RemoteException {
        }

        public void disableCallForward(boolean disabled) throws RemoteException {
        }

        public void disableCallLog(boolean disable) throws RemoteException {
        }

        public String getAreaCode(String phoneNumber) throws RemoteException {
            return null;
        }

        public String getMeid(int slotId) throws RemoteException {
            return null;
        }

        public void setIccCardActivate(int slotId, boolean isActive) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IPhoneManager {
        private static final String DESCRIPTOR = "com.miui.enterprise.IPhoneManager";
        static final int TRANSACTION_controlCellular = 3;
        static final int TRANSACTION_controlPhoneCall = 2;
        static final int TRANSACTION_controlSMS = 1;
        static final int TRANSACTION_disableCallForward = 24;
        static final int TRANSACTION_disableCallLog = 25;
        static final int TRANSACTION_endCall = 23;
        static final int TRANSACTION_getAreaCode = 26;
        static final int TRANSACTION_getCallBlackList = 19;
        static final int TRANSACTION_getCallContactRestriction = 22;
        static final int TRANSACTION_getCallWhiteList = 20;
        static final int TRANSACTION_getCellularStatus = 6;
        static final int TRANSACTION_getIMEI = 7;
        static final int TRANSACTION_getMeid = 27;
        static final int TRANSACTION_getPhoneCallStatus = 5;
        static final int TRANSACTION_getSMSBlackList = 13;
        static final int TRANSACTION_getSMSContactRestriction = 16;
        static final int TRANSACTION_getSMSStatus = 4;
        static final int TRANSACTION_getSMSWhiteList = 14;
        static final int TRANSACTION_isAutoRecordPhoneCall = 10;
        static final int TRANSACTION_setCallBlackList = 17;
        static final int TRANSACTION_setCallContactRestriction = 21;
        static final int TRANSACTION_setCallWhiteList = 18;
        static final int TRANSACTION_setIccCardActivate = 28;
        static final int TRANSACTION_setPhoneCallAutoRecord = 8;
        static final int TRANSACTION_setPhoneCallAutoRecordDir = 9;
        static final int TRANSACTION_setSMSBlackList = 11;
        static final int TRANSACTION_setSMSContactRestriction = 15;
        static final int TRANSACTION_setSMSWhiteList = 12;

        private static class Proxy implements IPhoneManager {
            public static IPhoneManager sDefaultImpl;
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

            public void controlSMS(int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().controlSMS(flags, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void controlPhoneCall(int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().controlPhoneCall(flags, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void controlCellular(int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().controlCellular(flags, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSMSStatus(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    int i = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getSMSStatus(userId);
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

            public int getPhoneCallStatus(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    int i = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getPhoneCallStatus(userId);
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

            public int getCellularStatus(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    int i = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getCellularStatus(userId);
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

            public String getIMEI(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    String str = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getIMEI(slotId);
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

            public void setPhoneCallAutoRecord(boolean isAutoRecord, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isAutoRecord ? 1 : 0);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPhoneCallAutoRecord(isAutoRecord, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPhoneCallAutoRecordDir(String dir) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(dir);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPhoneCallAutoRecordDir(dir);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isAutoRecordPhoneCall(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isAutoRecordPhoneCall(userId);
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

            public void setSMSBlackList(List<String> list, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(list);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSMSBlackList(list, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSMSWhiteList(List<String> list, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(list);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSMSWhiteList(list, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getSMSBlackList(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    List<String> list = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getSMSBlackList(userId);
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

            public List<String> getSMSWhiteList(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    List<String> list = 14;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getSMSWhiteList(userId);
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

            public void setSMSContactRestriction(int mode, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSMSContactRestriction(mode, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSMSContactRestriction(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    int i = 16;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getSMSContactRestriction(userId);
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

            public void setCallBlackList(List<String> list, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(list);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setCallBlackList(list, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCallWhiteList(List<String> list, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(list);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setCallWhiteList(list, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getCallBlackList(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    List<String> list = 19;
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getCallBlackList(userId);
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

            public List<String> getCallWhiteList(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    List<String> list = 20;
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getCallWhiteList(userId);
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

            public void setCallContactRestriction(int mode, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setCallContactRestriction(mode, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCallContactRestriction(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    int i = 22;
                    if (!this.mRemote.transact(22, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getCallContactRestriction(userId);
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

            public void endCall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().endCall();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableCallForward(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(disabled ? 1 : 0);
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().disableCallForward(disabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableCallLog(boolean disable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(disable ? 1 : 0);
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().disableCallLog(disable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getAreaCode(String phoneNumber) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(phoneNumber);
                    String str = 26;
                    if (!this.mRemote.transact(26, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getAreaCode(phoneNumber);
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

            public String getMeid(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    String str = 27;
                    if (!this.mRemote.transact(27, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getMeid(slotId);
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

            public void setIccCardActivate(int slotId, boolean isActive) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(isActive ? 1 : 0);
                    if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setIccCardActivate(slotId, isActive);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPhoneManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPhoneManager)) {
                return new Proxy(obj);
            }
            return (IPhoneManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "controlSMS";
                case 2:
                    return "controlPhoneCall";
                case 3:
                    return "controlCellular";
                case 4:
                    return "getSMSStatus";
                case 5:
                    return "getPhoneCallStatus";
                case 6:
                    return "getCellularStatus";
                case 7:
                    return "getIMEI";
                case 8:
                    return "setPhoneCallAutoRecord";
                case 9:
                    return "setPhoneCallAutoRecordDir";
                case 10:
                    return "isAutoRecordPhoneCall";
                case 11:
                    return "setSMSBlackList";
                case 12:
                    return "setSMSWhiteList";
                case 13:
                    return "getSMSBlackList";
                case 14:
                    return "getSMSWhiteList";
                case 15:
                    return "setSMSContactRestriction";
                case 16:
                    return "getSMSContactRestriction";
                case 17:
                    return "setCallBlackList";
                case 18:
                    return "setCallWhiteList";
                case 19:
                    return "getCallBlackList";
                case 20:
                    return "getCallWhiteList";
                case 21:
                    return "setCallContactRestriction";
                case 22:
                    return "getCallContactRestriction";
                case 23:
                    return "endCall";
                case 24:
                    return "disableCallForward";
                case 25:
                    return "disableCallLog";
                case 26:
                    return "getAreaCode";
                case 27:
                    return "getMeid";
                case 28:
                    return "setIccCardActivate";
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
                int _result;
                String _result2;
                List<String> _result3;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        controlSMS(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        controlPhoneCall(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        controlCellular(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        _result = getSMSStatus(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _result = getPhoneCallStatus(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        _result = getCellularStatus(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _result2 = getIMEI(data.readInt());
                        reply.writeNoException();
                        reply.writeString(_result2);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setPhoneCallAutoRecord(_arg0, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        setPhoneCallAutoRecordDir(data.readString());
                        reply.writeNoException();
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        boolean _result4 = isAutoRecordPhoneCall(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        setSMSBlackList(data.createStringArrayList(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        setSMSWhiteList(data.createStringArrayList(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        _result3 = getSMSBlackList(data.readInt());
                        reply.writeNoException();
                        reply.writeStringList(_result3);
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        _result3 = getSMSWhiteList(data.readInt());
                        reply.writeNoException();
                        reply.writeStringList(_result3);
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        setSMSContactRestriction(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        _result = getSMSContactRestriction(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        setCallBlackList(data.createStringArrayList(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        setCallWhiteList(data.createStringArrayList(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        _result3 = getCallBlackList(data.readInt());
                        reply.writeNoException();
                        reply.writeStringList(_result3);
                        return true;
                    case 20:
                        data.enforceInterface(descriptor);
                        _result3 = getCallWhiteList(data.readInt());
                        reply.writeNoException();
                        reply.writeStringList(_result3);
                        return true;
                    case 21:
                        data.enforceInterface(descriptor);
                        setCallContactRestriction(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 22:
                        data.enforceInterface(descriptor);
                        _result = getCallContactRestriction(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 23:
                        data.enforceInterface(descriptor);
                        endCall();
                        reply.writeNoException();
                        return true;
                    case 24:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        disableCallForward(_arg0);
                        reply.writeNoException();
                        return true;
                    case 25:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        disableCallLog(_arg0);
                        reply.writeNoException();
                        return true;
                    case 26:
                        data.enforceInterface(descriptor);
                        _result2 = getAreaCode(data.readString());
                        reply.writeNoException();
                        reply.writeString(_result2);
                        return true;
                    case 27:
                        data.enforceInterface(descriptor);
                        _result2 = getMeid(data.readInt());
                        reply.writeNoException();
                        reply.writeString(_result2);
                        return true;
                    case 28:
                        data.enforceInterface(descriptor);
                        _result = data.readInt();
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setIccCardActivate(_result, _arg0);
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IPhoneManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IPhoneManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void controlCellular(int i, int i2) throws RemoteException;

    void controlPhoneCall(int i, int i2) throws RemoteException;

    void controlSMS(int i, int i2) throws RemoteException;

    void disableCallForward(boolean z) throws RemoteException;

    void disableCallLog(boolean z) throws RemoteException;

    void endCall() throws RemoteException;

    String getAreaCode(String str) throws RemoteException;

    List<String> getCallBlackList(int i) throws RemoteException;

    int getCallContactRestriction(int i) throws RemoteException;

    List<String> getCallWhiteList(int i) throws RemoteException;

    int getCellularStatus(int i) throws RemoteException;

    String getIMEI(int i) throws RemoteException;

    String getMeid(int i) throws RemoteException;

    int getPhoneCallStatus(int i) throws RemoteException;

    List<String> getSMSBlackList(int i) throws RemoteException;

    int getSMSContactRestriction(int i) throws RemoteException;

    int getSMSStatus(int i) throws RemoteException;

    List<String> getSMSWhiteList(int i) throws RemoteException;

    boolean isAutoRecordPhoneCall(int i) throws RemoteException;

    void setCallBlackList(List<String> list, int i) throws RemoteException;

    void setCallContactRestriction(int i, int i2) throws RemoteException;

    void setCallWhiteList(List<String> list, int i) throws RemoteException;

    void setIccCardActivate(int i, boolean z) throws RemoteException;

    void setPhoneCallAutoRecord(boolean z, int i) throws RemoteException;

    void setPhoneCallAutoRecordDir(String str) throws RemoteException;

    void setSMSBlackList(List<String> list, int i) throws RemoteException;

    void setSMSContactRestriction(int i, int i2) throws RemoteException;

    void setSMSWhiteList(List<String> list, int i) throws RemoteException;
}
