package miui.mqsas;

import android.content.pm.ParceledListSlice;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
import miui.mqsas.sdk.event.AnrEvent;
import miui.mqsas.sdk.event.BootEvent;
import miui.mqsas.sdk.event.FeatureEvent;
import miui.mqsas.sdk.event.JavaExceptionEvent;
import miui.mqsas.sdk.event.PackageEvent;
import miui.mqsas.sdk.event.ScreenOnEvent;
import miui.mqsas.sdk.event.WatchdogEvent;

public interface IMQSService extends IInterface {

    public static class Default implements IMQSService {
        public void reportSimpleEvent(int type, String info) throws RemoteException {
        }

        public void reportNativeEventV2(String module, String info, String appId, int isGlobalNeed) throws RemoteException {
        }

        public int checkIfNeedDumpFd(JavaExceptionEvent event) throws RemoteException {
            return 0;
        }

        public int checkIfNeedDumpheap(JavaExceptionEvent event) throws RemoteException {
            return 0;
        }

        public void reportAnrEvent(AnrEvent event) throws RemoteException {
        }

        public void reportJavaExceptionEvent(JavaExceptionEvent event) throws RemoteException {
        }

        public void reportWatchdogEvent(WatchdogEvent event) throws RemoteException {
        }

        public void reportScreenOnEvent(ScreenOnEvent event) throws RemoteException {
        }

        public void reportPackageEvent(PackageEvent event) throws RemoteException {
        }

        public void onBootCompleted() throws RemoteException {
        }

        public void reportBroadcastEvent(ParceledListSlice events) throws RemoteException {
        }

        public void reportBootEvent(BootEvent event) throws RemoteException {
        }

        public void reportTelephonyEvent(int type, String info) throws RemoteException {
        }

        public void reportConnectExceptionEvent(int type, int reason, String bssid) throws RemoteException {
        }

        public void reportKillProcessEvents(ParceledListSlice events) throws RemoteException {
        }

        public void reportBluetoothEvent(int type, String info) throws RemoteException {
        }

        public void reportEvent(String module, String info, boolean isGlobalNeed) throws RemoteException {
        }

        public void reportEvents(String module, List<String> list, boolean isGlobalNeed) throws RemoteException {
        }

        public String getOnlineRuleMatched(String module, String info) throws RemoteException {
            return null;
        }

        public void reportPackageForegroundEvents(ParceledListSlice events) throws RemoteException {
        }

        public void dumpBugReport() throws RemoteException {
        }

        public void dialogButtonChecked(int operation, int eventType, String dgt, boolean isChecked) throws RemoteException {
        }

        public void reportBrightnessEvent(int startOrEnd, int progress, int autoBrightnessEnabled, String extra) throws RemoteException {
        }

        public void reportBrightnessEventV2(Bundle data) throws RemoteException {
        }

        public void reportHangExceptionEvents() throws RemoteException {
        }

        public void reportEventsV2(String module, List<String> list, String appId, boolean isGlobalNeed) throws RemoteException {
        }

        public void reportEventV2(String module, String info, String appId, boolean isGlobalNeed) throws RemoteException {
        }

        public void reportXmsEvent(String module, String info) throws RemoteException {
        }

        public void reportFeatureEvent(FeatureEvent event) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMQSService {
        private static final String DESCRIPTOR = "miui.mqsas.IMQSService";
        static final int TRANSACTION_checkIfNeedDumpFd = 3;
        static final int TRANSACTION_checkIfNeedDumpheap = 4;
        static final int TRANSACTION_dialogButtonChecked = 22;
        static final int TRANSACTION_dumpBugReport = 21;
        static final int TRANSACTION_getOnlineRuleMatched = 19;
        static final int TRANSACTION_onBootCompleted = 10;
        static final int TRANSACTION_reportAnrEvent = 5;
        static final int TRANSACTION_reportBluetoothEvent = 16;
        static final int TRANSACTION_reportBootEvent = 12;
        static final int TRANSACTION_reportBrightnessEvent = 23;
        static final int TRANSACTION_reportBrightnessEventV2 = 24;
        static final int TRANSACTION_reportBroadcastEvent = 11;
        static final int TRANSACTION_reportConnectExceptionEvent = 14;
        static final int TRANSACTION_reportEvent = 17;
        static final int TRANSACTION_reportEventV2 = 27;
        static final int TRANSACTION_reportEvents = 18;
        static final int TRANSACTION_reportEventsV2 = 26;
        static final int TRANSACTION_reportFeatureEvent = 29;
        static final int TRANSACTION_reportHangExceptionEvents = 25;
        static final int TRANSACTION_reportJavaExceptionEvent = 6;
        static final int TRANSACTION_reportKillProcessEvents = 15;
        static final int TRANSACTION_reportNativeEventV2 = 2;
        static final int TRANSACTION_reportPackageEvent = 9;
        static final int TRANSACTION_reportPackageForegroundEvents = 20;
        static final int TRANSACTION_reportScreenOnEvent = 8;
        static final int TRANSACTION_reportSimpleEvent = 1;
        static final int TRANSACTION_reportTelephonyEvent = 13;
        static final int TRANSACTION_reportWatchdogEvent = 7;
        static final int TRANSACTION_reportXmsEvent = 28;

        private static class Proxy implements IMQSService {
            public static IMQSService sDefaultImpl;
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

            public void reportSimpleEvent(int type, String info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(info);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reportSimpleEvent(type, info);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void reportNativeEventV2(String module, String info, String appId, int isGlobalNeed) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(info);
                    _data.writeString(appId);
                    _data.writeInt(isGlobalNeed);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reportNativeEventV2(module, info, appId, isGlobalNeed);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public int checkIfNeedDumpFd(JavaExceptionEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(3, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().checkIfNeedDumpFd(event);
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

            public int checkIfNeedDumpheap(JavaExceptionEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(4, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().checkIfNeedDumpheap(event);
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

            public void reportAnrEvent(AnrEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportAnrEvent(event);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportJavaExceptionEvent(JavaExceptionEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportJavaExceptionEvent(event);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportWatchdogEvent(WatchdogEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportWatchdogEvent(event);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportScreenOnEvent(ScreenOnEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reportScreenOnEvent(event);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void reportPackageEvent(PackageEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reportPackageEvent(event);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onBootCompleted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onBootCompleted();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportBroadcastEvent(ParceledListSlice events) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (events != null) {
                        _data.writeInt(1);
                        events.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reportBroadcastEvent(events);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void reportBootEvent(BootEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(12, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reportBootEvent(event);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void reportTelephonyEvent(int type, String info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(info);
                    if (this.mRemote.transact(13, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reportTelephonyEvent(type, info);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void reportConnectExceptionEvent(int type, int reason, String bssid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(reason);
                    _data.writeString(bssid);
                    if (this.mRemote.transact(14, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reportConnectExceptionEvent(type, reason, bssid);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void reportKillProcessEvents(ParceledListSlice events) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (events != null) {
                        _data.writeInt(1);
                        events.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(15, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reportKillProcessEvents(events);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void reportBluetoothEvent(int type, String info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(info);
                    if (this.mRemote.transact(16, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reportBluetoothEvent(type, info);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void reportEvent(String module, String info, boolean isGlobalNeed) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(info);
                    _data.writeInt(isGlobalNeed ? 1 : 0);
                    if (this.mRemote.transact(17, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reportEvent(module, info, isGlobalNeed);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void reportEvents(String module, List<String> info, boolean isGlobalNeed) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeStringList(info);
                    _data.writeInt(isGlobalNeed ? 1 : 0);
                    if (this.mRemote.transact(18, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reportEvents(module, info, isGlobalNeed);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public String getOnlineRuleMatched(String module, String info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(info);
                    String str = 19;
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getOnlineRuleMatched(module, info);
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

            public void reportPackageForegroundEvents(ParceledListSlice events) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (events != null) {
                        _data.writeInt(1);
                        events.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(20, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reportPackageForegroundEvents(events);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void dumpBugReport() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(21, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().dumpBugReport();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void dialogButtonChecked(int operation, int eventType, String dgt, boolean isChecked) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(operation);
                    _data.writeInt(eventType);
                    _data.writeString(dgt);
                    _data.writeInt(isChecked ? 1 : 0);
                    if (this.mRemote.transact(22, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().dialogButtonChecked(operation, eventType, dgt, isChecked);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void reportBrightnessEvent(int startOrEnd, int progress, int autoBrightnessEnabled, String extra) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(startOrEnd);
                    _data.writeInt(progress);
                    _data.writeInt(autoBrightnessEnabled);
                    _data.writeString(extra);
                    if (this.mRemote.transact(23, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reportBrightnessEvent(startOrEnd, progress, autoBrightnessEnabled, extra);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void reportBrightnessEventV2(Bundle data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (data != null) {
                        _data.writeInt(1);
                        data.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(24, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reportBrightnessEventV2(data);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void reportHangExceptionEvents() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(25, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reportHangExceptionEvents();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void reportEventsV2(String module, List<String> infoList, String appId, boolean isGlobalNeed) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeStringList(infoList);
                    _data.writeString(appId);
                    _data.writeInt(isGlobalNeed ? 1 : 0);
                    if (this.mRemote.transact(26, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reportEventsV2(module, infoList, appId, isGlobalNeed);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void reportEventV2(String module, String info, String appId, boolean isGlobalNeed) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(info);
                    _data.writeString(appId);
                    _data.writeInt(isGlobalNeed ? 1 : 0);
                    if (this.mRemote.transact(27, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reportEventV2(module, info, appId, isGlobalNeed);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void reportXmsEvent(String module, String info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(info);
                    if (this.mRemote.transact(28, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reportXmsEvent(module, info);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void reportFeatureEvent(FeatureEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(29, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reportFeatureEvent(event);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMQSService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMQSService)) {
                return new Proxy(obj);
            }
            return (IMQSService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "reportSimpleEvent";
                case 2:
                    return "reportNativeEventV2";
                case 3:
                    return "checkIfNeedDumpFd";
                case 4:
                    return "checkIfNeedDumpheap";
                case 5:
                    return "reportAnrEvent";
                case 6:
                    return "reportJavaExceptionEvent";
                case 7:
                    return "reportWatchdogEvent";
                case 8:
                    return "reportScreenOnEvent";
                case 9:
                    return "reportPackageEvent";
                case 10:
                    return "onBootCompleted";
                case 11:
                    return "reportBroadcastEvent";
                case 12:
                    return "reportBootEvent";
                case 13:
                    return "reportTelephonyEvent";
                case 14:
                    return "reportConnectExceptionEvent";
                case 15:
                    return "reportKillProcessEvents";
                case 16:
                    return "reportBluetoothEvent";
                case 17:
                    return "reportEvent";
                case 18:
                    return "reportEvents";
                case 19:
                    return "getOnlineRuleMatched";
                case 20:
                    return "reportPackageForegroundEvents";
                case 21:
                    return "dumpBugReport";
                case 22:
                    return "dialogButtonChecked";
                case 23:
                    return "reportBrightnessEvent";
                case 24:
                    return "reportBrightnessEventV2";
                case 25:
                    return "reportHangExceptionEvents";
                case 26:
                    return "reportEventsV2";
                case 27:
                    return "reportEventV2";
                case 28:
                    return "reportXmsEvent";
                case 29:
                    return "reportFeatureEvent";
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
                boolean _arg2 = false;
                JavaExceptionEvent _arg0;
                int _result;
                ParceledListSlice _arg02;
                String _arg03;
                String _arg1;
                List<String> _arg12;
                String _arg22;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        reportSimpleEvent(data.readInt(), data.readString());
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        reportNativeEventV2(data.readString(), data.readString(), data.readString(), data.readInt());
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (JavaExceptionEvent) JavaExceptionEvent.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        _result = checkIfNeedDumpFd(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (JavaExceptionEvent) JavaExceptionEvent.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        _result = checkIfNeedDumpheap(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 5:
                        AnrEvent _arg04;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (AnrEvent) AnrEvent.CREATOR.createFromParcel(data);
                        } else {
                            _arg04 = null;
                        }
                        reportAnrEvent(_arg04);
                        reply.writeNoException();
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (JavaExceptionEvent) JavaExceptionEvent.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        reportJavaExceptionEvent(_arg0);
                        reply.writeNoException();
                        return true;
                    case 7:
                        WatchdogEvent _arg05;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (WatchdogEvent) WatchdogEvent.CREATOR.createFromParcel(data);
                        } else {
                            _arg05 = null;
                        }
                        reportWatchdogEvent(_arg05);
                        reply.writeNoException();
                        return true;
                    case 8:
                        ScreenOnEvent _arg06;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg06 = (ScreenOnEvent) ScreenOnEvent.CREATOR.createFromParcel(data);
                        } else {
                            _arg06 = null;
                        }
                        reportScreenOnEvent(_arg06);
                        return true;
                    case 9:
                        PackageEvent _arg07;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg07 = (PackageEvent) PackageEvent.CREATOR.createFromParcel(data);
                        } else {
                            _arg07 = null;
                        }
                        reportPackageEvent(_arg07);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        onBootCompleted();
                        reply.writeNoException();
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        reportBroadcastEvent(_arg02);
                        return true;
                    case 12:
                        BootEvent _arg08;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg08 = (BootEvent) BootEvent.CREATOR.createFromParcel(data);
                        } else {
                            _arg08 = null;
                        }
                        reportBootEvent(_arg08);
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        reportTelephonyEvent(data.readInt(), data.readString());
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        reportConnectExceptionEvent(data.readInt(), data.readInt(), data.readString());
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        reportKillProcessEvents(_arg02);
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        reportBluetoothEvent(data.readInt(), data.readString());
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        _arg03 = data.readString();
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        reportEvent(_arg03, _arg1, _arg2);
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        _arg03 = data.readString();
                        _arg12 = data.createStringArrayList();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        reportEvents(_arg03, _arg12, _arg2);
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        _arg1 = getOnlineRuleMatched(data.readString(), data.readString());
                        reply.writeNoException();
                        reply.writeString(_arg1);
                        return true;
                    case 20:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        reportPackageForegroundEvents(_arg02);
                        return true;
                    case 21:
                        data.enforceInterface(descriptor);
                        dumpBugReport();
                        return true;
                    case 22:
                        data.enforceInterface(descriptor);
                        _result = data.readInt();
                        int _arg13 = data.readInt();
                        _arg22 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        dialogButtonChecked(_result, _arg13, _arg22, _arg2);
                        return true;
                    case 23:
                        data.enforceInterface(descriptor);
                        reportBrightnessEvent(data.readInt(), data.readInt(), data.readInt(), data.readString());
                        return true;
                    case 24:
                        Bundle _arg09;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg09 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                        } else {
                            _arg09 = null;
                        }
                        reportBrightnessEventV2(_arg09);
                        return true;
                    case 25:
                        data.enforceInterface(descriptor);
                        reportHangExceptionEvents();
                        return true;
                    case 26:
                        data.enforceInterface(descriptor);
                        _arg03 = data.readString();
                        _arg12 = data.createStringArrayList();
                        _arg22 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        reportEventsV2(_arg03, _arg12, _arg22, _arg2);
                        return true;
                    case 27:
                        data.enforceInterface(descriptor);
                        _arg03 = data.readString();
                        _arg1 = data.readString();
                        _arg22 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        reportEventV2(_arg03, _arg1, _arg22, _arg2);
                        return true;
                    case 28:
                        data.enforceInterface(descriptor);
                        reportXmsEvent(data.readString(), data.readString());
                        return true;
                    case 29:
                        FeatureEvent _arg010;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg010 = (FeatureEvent) FeatureEvent.CREATOR.createFromParcel(data);
                        } else {
                            _arg010 = null;
                        }
                        reportFeatureEvent(_arg010);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IMQSService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMQSService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    int checkIfNeedDumpFd(JavaExceptionEvent javaExceptionEvent) throws RemoteException;

    int checkIfNeedDumpheap(JavaExceptionEvent javaExceptionEvent) throws RemoteException;

    void dialogButtonChecked(int i, int i2, String str, boolean z) throws RemoteException;

    void dumpBugReport() throws RemoteException;

    String getOnlineRuleMatched(String str, String str2) throws RemoteException;

    void onBootCompleted() throws RemoteException;

    void reportAnrEvent(AnrEvent anrEvent) throws RemoteException;

    void reportBluetoothEvent(int i, String str) throws RemoteException;

    void reportBootEvent(BootEvent bootEvent) throws RemoteException;

    void reportBrightnessEvent(int i, int i2, int i3, String str) throws RemoteException;

    void reportBrightnessEventV2(Bundle bundle) throws RemoteException;

    void reportBroadcastEvent(ParceledListSlice parceledListSlice) throws RemoteException;

    void reportConnectExceptionEvent(int i, int i2, String str) throws RemoteException;

    void reportEvent(String str, String str2, boolean z) throws RemoteException;

    void reportEventV2(String str, String str2, String str3, boolean z) throws RemoteException;

    void reportEvents(String str, List<String> list, boolean z) throws RemoteException;

    void reportEventsV2(String str, List<String> list, String str2, boolean z) throws RemoteException;

    void reportFeatureEvent(FeatureEvent featureEvent) throws RemoteException;

    void reportHangExceptionEvents() throws RemoteException;

    void reportJavaExceptionEvent(JavaExceptionEvent javaExceptionEvent) throws RemoteException;

    void reportKillProcessEvents(ParceledListSlice parceledListSlice) throws RemoteException;

    void reportNativeEventV2(String str, String str2, String str3, int i) throws RemoteException;

    void reportPackageEvent(PackageEvent packageEvent) throws RemoteException;

    void reportPackageForegroundEvents(ParceledListSlice parceledListSlice) throws RemoteException;

    void reportScreenOnEvent(ScreenOnEvent screenOnEvent) throws RemoteException;

    void reportSimpleEvent(int i, String str) throws RemoteException;

    void reportTelephonyEvent(int i, String str) throws RemoteException;

    void reportWatchdogEvent(WatchdogEvent watchdogEvent) throws RemoteException;

    void reportXmsEvent(String str, String str2) throws RemoteException;
}
