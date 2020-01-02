package android.print;

import android.content.ComponentName;
import android.content.Context;
import android.graphics.drawable.Icon;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.printservice.PrintServiceInfo;
import android.printservice.recommendation.IRecommendationsChangeListener;
import android.printservice.recommendation.RecommendationInfo;
import java.util.List;

public interface IPrintManager extends IInterface {

    public static class Default implements IPrintManager {
        public List<PrintJobInfo> getPrintJobInfos(int appId, int userId) throws RemoteException {
            return null;
        }

        public PrintJobInfo getPrintJobInfo(PrintJobId printJobId, int appId, int userId) throws RemoteException {
            return null;
        }

        public Bundle print(String printJobName, IPrintDocumentAdapter printAdapter, PrintAttributes attributes, String packageName, int appId, int userId) throws RemoteException {
            return null;
        }

        public void cancelPrintJob(PrintJobId printJobId, int appId, int userId) throws RemoteException {
        }

        public void restartPrintJob(PrintJobId printJobId, int appId, int userId) throws RemoteException {
        }

        public void addPrintJobStateChangeListener(IPrintJobStateChangeListener listener, int appId, int userId) throws RemoteException {
        }

        public void removePrintJobStateChangeListener(IPrintJobStateChangeListener listener, int userId) throws RemoteException {
        }

        public void addPrintServicesChangeListener(IPrintServicesChangeListener listener, int userId) throws RemoteException {
        }

        public void removePrintServicesChangeListener(IPrintServicesChangeListener listener, int userId) throws RemoteException {
        }

        public List<PrintServiceInfo> getPrintServices(int selectionFlags, int userId) throws RemoteException {
            return null;
        }

        public void setPrintServiceEnabled(ComponentName service, boolean isEnabled, int userId) throws RemoteException {
        }

        public void addPrintServiceRecommendationsChangeListener(IRecommendationsChangeListener listener, int userId) throws RemoteException {
        }

        public void removePrintServiceRecommendationsChangeListener(IRecommendationsChangeListener listener, int userId) throws RemoteException {
        }

        public List<RecommendationInfo> getPrintServiceRecommendations(int userId) throws RemoteException {
            return null;
        }

        public void createPrinterDiscoverySession(IPrinterDiscoveryObserver observer, int userId) throws RemoteException {
        }

        public void startPrinterDiscovery(IPrinterDiscoveryObserver observer, List<PrinterId> list, int userId) throws RemoteException {
        }

        public void stopPrinterDiscovery(IPrinterDiscoveryObserver observer, int userId) throws RemoteException {
        }

        public void validatePrinters(List<PrinterId> list, int userId) throws RemoteException {
        }

        public void startPrinterStateTracking(PrinterId printerId, int userId) throws RemoteException {
        }

        public Icon getCustomPrinterIcon(PrinterId printerId, int userId) throws RemoteException {
            return null;
        }

        public void stopPrinterStateTracking(PrinterId printerId, int userId) throws RemoteException {
        }

        public void destroyPrinterDiscoverySession(IPrinterDiscoveryObserver observer, int userId) throws RemoteException {
        }

        public boolean getBindInstantServiceAllowed(int userId) throws RemoteException {
            return false;
        }

        public void setBindInstantServiceAllowed(int userId, boolean allowed) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IPrintManager {
        private static final String DESCRIPTOR = "android.print.IPrintManager";
        static final int TRANSACTION_addPrintJobStateChangeListener = 6;
        static final int TRANSACTION_addPrintServiceRecommendationsChangeListener = 12;
        static final int TRANSACTION_addPrintServicesChangeListener = 8;
        static final int TRANSACTION_cancelPrintJob = 4;
        static final int TRANSACTION_createPrinterDiscoverySession = 15;
        static final int TRANSACTION_destroyPrinterDiscoverySession = 22;
        static final int TRANSACTION_getBindInstantServiceAllowed = 23;
        static final int TRANSACTION_getCustomPrinterIcon = 20;
        static final int TRANSACTION_getPrintJobInfo = 2;
        static final int TRANSACTION_getPrintJobInfos = 1;
        static final int TRANSACTION_getPrintServiceRecommendations = 14;
        static final int TRANSACTION_getPrintServices = 10;
        static final int TRANSACTION_print = 3;
        static final int TRANSACTION_removePrintJobStateChangeListener = 7;
        static final int TRANSACTION_removePrintServiceRecommendationsChangeListener = 13;
        static final int TRANSACTION_removePrintServicesChangeListener = 9;
        static final int TRANSACTION_restartPrintJob = 5;
        static final int TRANSACTION_setBindInstantServiceAllowed = 24;
        static final int TRANSACTION_setPrintServiceEnabled = 11;
        static final int TRANSACTION_startPrinterDiscovery = 16;
        static final int TRANSACTION_startPrinterStateTracking = 19;
        static final int TRANSACTION_stopPrinterDiscovery = 17;
        static final int TRANSACTION_stopPrinterStateTracking = 21;
        static final int TRANSACTION_validatePrinters = 18;

        private static class Proxy implements IPrintManager {
            public static IPrintManager sDefaultImpl;
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

            public List<PrintJobInfo> getPrintJobInfos(int appId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(appId);
                    _data.writeInt(userId);
                    List<PrintJobInfo> list = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getPrintJobInfos(appId, userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(PrintJobInfo.CREATOR);
                    List<PrintJobInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public PrintJobInfo getPrintJobInfo(PrintJobId printJobId, int appId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (printJobId != null) {
                        _data.writeInt(1);
                        printJobId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(appId);
                    _data.writeInt(userId);
                    PrintJobInfo printJobInfo = this.mRemote;
                    if (!printJobInfo.transact(2, _data, _reply, 0)) {
                        printJobInfo = Stub.getDefaultImpl();
                        if (printJobInfo != null) {
                            printJobInfo = Stub.getDefaultImpl().getPrintJobInfo(printJobId, appId, userId);
                            return printJobInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        printJobInfo = (PrintJobInfo) PrintJobInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        printJobInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return printJobInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle print(String printJobName, IPrintDocumentAdapter printAdapter, PrintAttributes attributes, String packageName, int appId, int userId) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                String str;
                PrintAttributes printAttributes = attributes;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(printJobName);
                        _data.writeStrongBinder(printAdapter != null ? printAdapter.asBinder() : null);
                        if (printAttributes != null) {
                            _data.writeInt(1);
                            printAttributes.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeString(packageName);
                        } catch (Throwable th2) {
                            th = th2;
                            i = appId;
                            i2 = userId;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(appId);
                        } catch (Throwable th3) {
                            th = th3;
                            i2 = userId;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(userId);
                            try {
                                Bundle _result;
                                if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                    _reply.readException();
                                    if (_reply.readInt() != 0) {
                                        _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                                    } else {
                                        _result = null;
                                    }
                                    _reply.recycle();
                                    _data.recycle();
                                    return _result;
                                }
                                _result = Stub.getDefaultImpl().print(printJobName, printAdapter, attributes, packageName, appId, userId);
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
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        str = packageName;
                        i = appId;
                        i2 = userId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str2 = printJobName;
                    str = packageName;
                    i = appId;
                    i2 = userId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void cancelPrintJob(PrintJobId printJobId, int appId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (printJobId != null) {
                        _data.writeInt(1);
                        printJobId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(appId);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancelPrintJob(printJobId, appId, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void restartPrintJob(PrintJobId printJobId, int appId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (printJobId != null) {
                        _data.writeInt(1);
                        printJobId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(appId);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().restartPrintJob(printJobId, appId, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addPrintJobStateChangeListener(IPrintJobStateChangeListener listener, int appId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeInt(appId);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addPrintJobStateChangeListener(listener, appId, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removePrintJobStateChangeListener(IPrintJobStateChangeListener listener, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removePrintJobStateChangeListener(listener, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addPrintServicesChangeListener(IPrintServicesChangeListener listener, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addPrintServicesChangeListener(listener, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removePrintServicesChangeListener(IPrintServicesChangeListener listener, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removePrintServicesChangeListener(listener, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<PrintServiceInfo> getPrintServices(int selectionFlags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(selectionFlags);
                    _data.writeInt(userId);
                    List<PrintServiceInfo> list = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getPrintServices(selectionFlags, userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(PrintServiceInfo.CREATOR);
                    List<PrintServiceInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPrintServiceEnabled(ComponentName service, boolean isEnabled, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!isEnabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPrintServiceEnabled(service, isEnabled, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addPrintServiceRecommendationsChangeListener(IRecommendationsChangeListener listener, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addPrintServiceRecommendationsChangeListener(listener, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removePrintServiceRecommendationsChangeListener(IRecommendationsChangeListener listener, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removePrintServiceRecommendationsChangeListener(listener, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<RecommendationInfo> getPrintServiceRecommendations(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    List<RecommendationInfo> list = 14;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getPrintServiceRecommendations(userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(RecommendationInfo.CREATOR);
                    List<RecommendationInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void createPrinterDiscoverySession(IPrinterDiscoveryObserver observer, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().createPrinterDiscoverySession(observer, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startPrinterDiscovery(IPrinterDiscoveryObserver observer, List<PrinterId> priorityList, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeTypedList(priorityList);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startPrinterDiscovery(observer, priorityList, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopPrinterDiscovery(IPrinterDiscoveryObserver observer, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopPrinterDiscovery(observer, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void validatePrinters(List<PrinterId> printerIds, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(printerIds);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().validatePrinters(printerIds, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startPrinterStateTracking(PrinterId printerId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (printerId != null) {
                        _data.writeInt(1);
                        printerId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startPrinterStateTracking(printerId, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Icon getCustomPrinterIcon(PrinterId printerId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (printerId != null) {
                        _data.writeInt(1);
                        printerId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    Icon icon = this.mRemote;
                    if (!icon.transact(20, _data, _reply, 0)) {
                        icon = Stub.getDefaultImpl();
                        if (icon != null) {
                            icon = Stub.getDefaultImpl().getCustomPrinterIcon(printerId, userId);
                            return icon;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        icon = (Icon) Icon.CREATOR.createFromParcel(_reply);
                    } else {
                        icon = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return icon;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopPrinterStateTracking(PrinterId printerId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (printerId != null) {
                        _data.writeInt(1);
                        printerId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopPrinterStateTracking(printerId, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void destroyPrinterDiscoverySession(IPrinterDiscoveryObserver observer, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().destroyPrinterDiscoverySession(observer, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getBindInstantServiceAllowed(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(23, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getBindInstantServiceAllowed(userId);
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

            public void setBindInstantServiceAllowed(int userId, boolean allowed) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(allowed ? 1 : 0);
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setBindInstantServiceAllowed(userId, allowed);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPrintManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPrintManager)) {
                return new Proxy(obj);
            }
            return (IPrintManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getPrintJobInfos";
                case 2:
                    return "getPrintJobInfo";
                case 3:
                    return Context.PRINT_SERVICE;
                case 4:
                    return "cancelPrintJob";
                case 5:
                    return "restartPrintJob";
                case 6:
                    return "addPrintJobStateChangeListener";
                case 7:
                    return "removePrintJobStateChangeListener";
                case 8:
                    return "addPrintServicesChangeListener";
                case 9:
                    return "removePrintServicesChangeListener";
                case 10:
                    return "getPrintServices";
                case 11:
                    return "setPrintServiceEnabled";
                case 12:
                    return "addPrintServiceRecommendationsChangeListener";
                case 13:
                    return "removePrintServiceRecommendationsChangeListener";
                case 14:
                    return "getPrintServiceRecommendations";
                case 15:
                    return "createPrinterDiscoverySession";
                case 16:
                    return "startPrinterDiscovery";
                case 17:
                    return "stopPrinterDiscovery";
                case 18:
                    return "validatePrinters";
                case 19:
                    return "startPrinterStateTracking";
                case 20:
                    return "getCustomPrinterIcon";
                case 21:
                    return "stopPrinterStateTracking";
                case 22:
                    return "destroyPrinterDiscoverySession";
                case 23:
                    return "getBindInstantServiceAllowed";
                case 24:
                    return "setBindInstantServiceAllowed";
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
                PrintJobId _arg0;
                PrinterId _arg02;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        List<PrintJobInfo> _result = getPrintJobInfos(data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result);
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (PrintJobId) PrintJobId.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        PrintJobInfo _result2 = getPrintJobInfo(_arg0, data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result2 != null) {
                            parcel2.writeInt(1);
                            _result2.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 3:
                        PrintAttributes _arg2;
                        parcel.enforceInterface(descriptor);
                        String _arg03 = data.readString();
                        IPrintDocumentAdapter _arg1 = android.print.IPrintDocumentAdapter.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg2 = (PrintAttributes) PrintAttributes.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        Bundle _result3 = print(_arg03, _arg1, _arg2, data.readString(), data.readInt(), data.readInt());
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
                        if (data.readInt() != 0) {
                            _arg0 = (PrintJobId) PrintJobId.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        cancelPrintJob(_arg0, data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (PrintJobId) PrintJobId.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        restartPrintJob(_arg0, data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        addPrintJobStateChangeListener(android.print.IPrintJobStateChangeListener.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        removePrintJobStateChangeListener(android.print.IPrintJobStateChangeListener.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        addPrintServicesChangeListener(android.print.IPrintServicesChangeListener.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        removePrintServicesChangeListener(android.print.IPrintServicesChangeListener.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        List<PrintServiceInfo> _result4 = getPrintServices(data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result4);
                        return true;
                    case 11:
                        ComponentName _arg04;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setPrintServiceEnabled(_arg04, z, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        addPrintServiceRecommendationsChangeListener(android.printservice.recommendation.IRecommendationsChangeListener.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        removePrintServiceRecommendationsChangeListener(android.printservice.recommendation.IRecommendationsChangeListener.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        List<RecommendationInfo> _result5 = getPrintServiceRecommendations(data.readInt());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result5);
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        createPrinterDiscoverySession(android.print.IPrinterDiscoveryObserver.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        startPrinterDiscovery(android.print.IPrinterDiscoveryObserver.Stub.asInterface(data.readStrongBinder()), parcel.createTypedArrayList(PrinterId.CREATOR), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        stopPrinterDiscovery(android.print.IPrinterDiscoveryObserver.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        validatePrinters(parcel.createTypedArrayList(PrinterId.CREATOR), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (PrinterId) PrinterId.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        startPrinterStateTracking(_arg02, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (PrinterId) PrinterId.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        Icon _result6 = getCustomPrinterIcon(_arg02, data.readInt());
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (PrinterId) PrinterId.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        stopPrinterStateTracking(_arg02, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        destroyPrinterDiscoverySession(android.print.IPrinterDiscoveryObserver.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        boolean _result7 = getBindInstantServiceAllowed(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        int _arg05 = data.readInt();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setBindInstantServiceAllowed(_arg05, z);
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IPrintManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IPrintManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addPrintJobStateChangeListener(IPrintJobStateChangeListener iPrintJobStateChangeListener, int i, int i2) throws RemoteException;

    void addPrintServiceRecommendationsChangeListener(IRecommendationsChangeListener iRecommendationsChangeListener, int i) throws RemoteException;

    void addPrintServicesChangeListener(IPrintServicesChangeListener iPrintServicesChangeListener, int i) throws RemoteException;

    void cancelPrintJob(PrintJobId printJobId, int i, int i2) throws RemoteException;

    void createPrinterDiscoverySession(IPrinterDiscoveryObserver iPrinterDiscoveryObserver, int i) throws RemoteException;

    void destroyPrinterDiscoverySession(IPrinterDiscoveryObserver iPrinterDiscoveryObserver, int i) throws RemoteException;

    boolean getBindInstantServiceAllowed(int i) throws RemoteException;

    Icon getCustomPrinterIcon(PrinterId printerId, int i) throws RemoteException;

    PrintJobInfo getPrintJobInfo(PrintJobId printJobId, int i, int i2) throws RemoteException;

    List<PrintJobInfo> getPrintJobInfos(int i, int i2) throws RemoteException;

    List<RecommendationInfo> getPrintServiceRecommendations(int i) throws RemoteException;

    List<PrintServiceInfo> getPrintServices(int i, int i2) throws RemoteException;

    Bundle print(String str, IPrintDocumentAdapter iPrintDocumentAdapter, PrintAttributes printAttributes, String str2, int i, int i2) throws RemoteException;

    void removePrintJobStateChangeListener(IPrintJobStateChangeListener iPrintJobStateChangeListener, int i) throws RemoteException;

    void removePrintServiceRecommendationsChangeListener(IRecommendationsChangeListener iRecommendationsChangeListener, int i) throws RemoteException;

    void removePrintServicesChangeListener(IPrintServicesChangeListener iPrintServicesChangeListener, int i) throws RemoteException;

    void restartPrintJob(PrintJobId printJobId, int i, int i2) throws RemoteException;

    void setBindInstantServiceAllowed(int i, boolean z) throws RemoteException;

    void setPrintServiceEnabled(ComponentName componentName, boolean z, int i) throws RemoteException;

    void startPrinterDiscovery(IPrinterDiscoveryObserver iPrinterDiscoveryObserver, List<PrinterId> list, int i) throws RemoteException;

    void startPrinterStateTracking(PrinterId printerId, int i) throws RemoteException;

    void stopPrinterDiscovery(IPrinterDiscoveryObserver iPrinterDiscoveryObserver, int i) throws RemoteException;

    void stopPrinterStateTracking(PrinterId printerId, int i) throws RemoteException;

    void validatePrinters(List<PrinterId> list, int i) throws RemoteException;
}
