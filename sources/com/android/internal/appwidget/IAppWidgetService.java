package com.android.internal.appwidget;

import android.annotation.UnsupportedAppUsage;
import android.app.IApplicationThread;
import android.app.IServiceConnection;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ParceledListSlice;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.widget.RemoteViews;

public interface IAppWidgetService extends IInterface {

    public static class Default implements IAppWidgetService {
        public ParceledListSlice startListening(IAppWidgetHost host, String callingPackage, int hostId, int[] appWidgetIds) throws RemoteException {
            return null;
        }

        public void stopListening(String callingPackage, int hostId) throws RemoteException {
        }

        public int allocateAppWidgetId(String callingPackage, int hostId) throws RemoteException {
            return 0;
        }

        public void deleteAppWidgetId(String callingPackage, int appWidgetId) throws RemoteException {
        }

        public void deleteHost(String packageName, int hostId) throws RemoteException {
        }

        public void deleteAllHosts() throws RemoteException {
        }

        public RemoteViews getAppWidgetViews(String callingPackage, int appWidgetId) throws RemoteException {
            return null;
        }

        public int[] getAppWidgetIdsForHost(String callingPackage, int hostId) throws RemoteException {
            return null;
        }

        public IntentSender createAppWidgetConfigIntentSender(String callingPackage, int appWidgetId, int intentFlags) throws RemoteException {
            return null;
        }

        public void updateAppWidgetIds(String callingPackage, int[] appWidgetIds, RemoteViews views) throws RemoteException {
        }

        public void updateAppWidgetOptions(String callingPackage, int appWidgetId, Bundle extras) throws RemoteException {
        }

        public Bundle getAppWidgetOptions(String callingPackage, int appWidgetId) throws RemoteException {
            return null;
        }

        public void partiallyUpdateAppWidgetIds(String callingPackage, int[] appWidgetIds, RemoteViews views) throws RemoteException {
        }

        public void updateAppWidgetProvider(ComponentName provider, RemoteViews views) throws RemoteException {
        }

        public void updateAppWidgetProviderInfo(ComponentName provider, String metadataKey) throws RemoteException {
        }

        public void notifyAppWidgetViewDataChanged(String packageName, int[] appWidgetIds, int viewId) throws RemoteException {
        }

        public ParceledListSlice getInstalledProvidersForProfile(int categoryFilter, int profileId, String packageName) throws RemoteException {
            return null;
        }

        public AppWidgetProviderInfo getAppWidgetInfo(String callingPackage, int appWidgetId) throws RemoteException {
            return null;
        }

        public boolean hasBindAppWidgetPermission(String packageName, int userId) throws RemoteException {
            return false;
        }

        public void setBindAppWidgetPermission(String packageName, int userId, boolean permission) throws RemoteException {
        }

        public boolean bindAppWidgetId(String callingPackage, int appWidgetId, int providerProfileId, ComponentName providerComponent, Bundle options) throws RemoteException {
            return false;
        }

        public boolean bindRemoteViewsService(String callingPackage, int appWidgetId, Intent intent, IApplicationThread caller, IBinder token, IServiceConnection connection, int flags) throws RemoteException {
            return false;
        }

        public int[] getAppWidgetIds(ComponentName providerComponent) throws RemoteException {
            return null;
        }

        public boolean isBoundWidgetPackage(String packageName, int userId) throws RemoteException {
            return false;
        }

        public boolean requestPinAppWidget(String packageName, ComponentName providerComponent, Bundle extras, IntentSender resultIntent) throws RemoteException {
            return false;
        }

        public boolean isRequestPinAppWidgetSupported() throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IAppWidgetService {
        private static final String DESCRIPTOR = "com.android.internal.appwidget.IAppWidgetService";
        static final int TRANSACTION_allocateAppWidgetId = 3;
        static final int TRANSACTION_bindAppWidgetId = 21;
        static final int TRANSACTION_bindRemoteViewsService = 22;
        static final int TRANSACTION_createAppWidgetConfigIntentSender = 9;
        static final int TRANSACTION_deleteAllHosts = 6;
        static final int TRANSACTION_deleteAppWidgetId = 4;
        static final int TRANSACTION_deleteHost = 5;
        static final int TRANSACTION_getAppWidgetIds = 23;
        static final int TRANSACTION_getAppWidgetIdsForHost = 8;
        static final int TRANSACTION_getAppWidgetInfo = 18;
        static final int TRANSACTION_getAppWidgetOptions = 12;
        static final int TRANSACTION_getAppWidgetViews = 7;
        static final int TRANSACTION_getInstalledProvidersForProfile = 17;
        static final int TRANSACTION_hasBindAppWidgetPermission = 19;
        static final int TRANSACTION_isBoundWidgetPackage = 24;
        static final int TRANSACTION_isRequestPinAppWidgetSupported = 26;
        static final int TRANSACTION_notifyAppWidgetViewDataChanged = 16;
        static final int TRANSACTION_partiallyUpdateAppWidgetIds = 13;
        static final int TRANSACTION_requestPinAppWidget = 25;
        static final int TRANSACTION_setBindAppWidgetPermission = 20;
        static final int TRANSACTION_startListening = 1;
        static final int TRANSACTION_stopListening = 2;
        static final int TRANSACTION_updateAppWidgetIds = 10;
        static final int TRANSACTION_updateAppWidgetOptions = 11;
        static final int TRANSACTION_updateAppWidgetProvider = 14;
        static final int TRANSACTION_updateAppWidgetProviderInfo = 15;

        private static class Proxy implements IAppWidgetService {
            public static IAppWidgetService sDefaultImpl;
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

            public ParceledListSlice startListening(IAppWidgetHost host, String callingPackage, int hostId, int[] appWidgetIds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(host != null ? host.asBinder() : null);
                    _data.writeString(callingPackage);
                    _data.writeInt(hostId);
                    _data.writeIntArray(appWidgetIds);
                    ParceledListSlice parceledListSlice = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().startListening(host, callingPackage, hostId, appWidgetIds);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopListening(String callingPackage, int hostId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(hostId);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopListening(callingPackage, hostId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int allocateAppWidgetId(String callingPackage, int hostId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(hostId);
                    int i = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().allocateAppWidgetId(callingPackage, hostId);
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

            public void deleteAppWidgetId(String callingPackage, int appWidgetId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(appWidgetId);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deleteAppWidgetId(callingPackage, appWidgetId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deleteHost(String packageName, int hostId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(hostId);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deleteHost(packageName, hostId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deleteAllHosts() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deleteAllHosts();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public RemoteViews getAppWidgetViews(String callingPackage, int appWidgetId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(appWidgetId);
                    RemoteViews remoteViews = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        remoteViews = Stub.getDefaultImpl();
                        if (remoteViews != 0) {
                            remoteViews = Stub.getDefaultImpl().getAppWidgetViews(callingPackage, appWidgetId);
                            return remoteViews;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        remoteViews = (RemoteViews) RemoteViews.CREATOR.createFromParcel(_reply);
                    } else {
                        remoteViews = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return remoteViews;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getAppWidgetIdsForHost(String callingPackage, int hostId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(hostId);
                    int[] iArr = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != 0) {
                            iArr = Stub.getDefaultImpl().getAppWidgetIdsForHost(callingPackage, hostId);
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

            public IntentSender createAppWidgetConfigIntentSender(String callingPackage, int appWidgetId, int intentFlags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(appWidgetId);
                    _data.writeInt(intentFlags);
                    IntentSender intentSender = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        intentSender = Stub.getDefaultImpl();
                        if (intentSender != 0) {
                            intentSender = Stub.getDefaultImpl().createAppWidgetConfigIntentSender(callingPackage, appWidgetId, intentFlags);
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

            public void updateAppWidgetIds(String callingPackage, int[] appWidgetIds, RemoteViews views) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeIntArray(appWidgetIds);
                    if (views != null) {
                        _data.writeInt(1);
                        views.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateAppWidgetIds(callingPackage, appWidgetIds, views);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateAppWidgetOptions(String callingPackage, int appWidgetId, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(appWidgetId);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateAppWidgetOptions(callingPackage, appWidgetId, extras);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle getAppWidgetOptions(String callingPackage, int appWidgetId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(appWidgetId);
                    Bundle bundle = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        bundle = Stub.getDefaultImpl();
                        if (bundle != 0) {
                            bundle = Stub.getDefaultImpl().getAppWidgetOptions(callingPackage, appWidgetId);
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

            public void partiallyUpdateAppWidgetIds(String callingPackage, int[] appWidgetIds, RemoteViews views) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeIntArray(appWidgetIds);
                    if (views != null) {
                        _data.writeInt(1);
                        views.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().partiallyUpdateAppWidgetIds(callingPackage, appWidgetIds, views);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateAppWidgetProvider(ComponentName provider, RemoteViews views) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (provider != null) {
                        _data.writeInt(1);
                        provider.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (views != null) {
                        _data.writeInt(1);
                        views.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateAppWidgetProvider(provider, views);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateAppWidgetProviderInfo(ComponentName provider, String metadataKey) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (provider != null) {
                        _data.writeInt(1);
                        provider.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(metadataKey);
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateAppWidgetProviderInfo(provider, metadataKey);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyAppWidgetViewDataChanged(String packageName, int[] appWidgetIds, int viewId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeIntArray(appWidgetIds);
                    _data.writeInt(viewId);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyAppWidgetViewDataChanged(packageName, appWidgetIds, viewId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getInstalledProvidersForProfile(int categoryFilter, int profileId, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(categoryFilter);
                    _data.writeInt(profileId);
                    _data.writeString(packageName);
                    ParceledListSlice parceledListSlice = 17;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getInstalledProvidersForProfile(categoryFilter, profileId, packageName);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public AppWidgetProviderInfo getAppWidgetInfo(String callingPackage, int appWidgetId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(appWidgetId);
                    AppWidgetProviderInfo appWidgetProviderInfo = 18;
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        appWidgetProviderInfo = Stub.getDefaultImpl();
                        if (appWidgetProviderInfo != 0) {
                            appWidgetProviderInfo = Stub.getDefaultImpl().getAppWidgetInfo(callingPackage, appWidgetId);
                            return appWidgetProviderInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        appWidgetProviderInfo = (AppWidgetProviderInfo) AppWidgetProviderInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        appWidgetProviderInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return appWidgetProviderInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasBindAppWidgetPermission(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasBindAppWidgetPermission(packageName, userId);
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

            public void setBindAppWidgetPermission(String packageName, int userId, boolean permission) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeInt(permission ? 1 : 0);
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setBindAppWidgetPermission(packageName, userId, permission);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean bindAppWidgetId(String callingPackage, int appWidgetId, int providerProfileId, ComponentName providerComponent, Bundle options) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                ComponentName componentName = providerComponent;
                Bundle bundle = options;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(callingPackage);
                    } catch (Throwable th2) {
                        th = th2;
                        i = appWidgetId;
                        i2 = providerProfileId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        boolean _result;
                        _data.writeInt(appWidgetId);
                        try {
                            _data.writeInt(providerProfileId);
                            _result = true;
                            if (componentName != null) {
                                _data.writeInt(1);
                                componentName.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
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
                        try {
                            if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                if (_reply.readInt() == 0) {
                                    _result = false;
                                }
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            _result = Stub.getDefaultImpl().bindAppWidgetId(callingPackage, appWidgetId, providerProfileId, providerComponent, options);
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
                        i2 = providerProfileId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str = callingPackage;
                    i = appWidgetId;
                    i2 = providerProfileId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public boolean bindRemoteViewsService(String callingPackage, int appWidgetId, Intent intent, IApplicationThread caller, IBinder token, IServiceConnection connection, int flags) throws RemoteException {
                Throwable th;
                IBinder iBinder;
                int i;
                int i2;
                Intent intent2 = intent;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        boolean _result;
                        IBinder iBinder2;
                        _data.writeString(callingPackage);
                        try {
                            _data.writeInt(appWidgetId);
                            _result = true;
                            if (intent2 != null) {
                                _data.writeInt(1);
                                intent2.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            iBinder2 = null;
                            _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                        } catch (Throwable th2) {
                            th = th2;
                            iBinder = token;
                            i = flags;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeStrongBinder(token);
                            if (connection != null) {
                                iBinder2 = connection.asBinder();
                            }
                            _data.writeStrongBinder(iBinder2);
                        } catch (Throwable th3) {
                            th = th3;
                            i = flags;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(flags);
                            if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                if (_reply.readInt() == 0) {
                                    _result = false;
                                }
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            _result = Stub.getDefaultImpl().bindRemoteViewsService(callingPackage, appWidgetId, intent, caller, token, connection, flags);
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
                        i2 = appWidgetId;
                        iBinder = token;
                        i = flags;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str = callingPackage;
                    i2 = appWidgetId;
                    iBinder = token;
                    i = flags;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public int[] getAppWidgetIds(ComponentName providerComponent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (providerComponent != null) {
                        _data.writeInt(1);
                        providerComponent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int[] iArr = this.mRemote;
                    if (!iArr.transact(23, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != null) {
                            iArr = Stub.getDefaultImpl().getAppWidgetIds(providerComponent);
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

            public boolean isBoundWidgetPackage(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(24, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isBoundWidgetPackage(packageName, userId);
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

            public boolean requestPinAppWidget(String packageName, ComponentName providerComponent, Bundle extras, IntentSender resultIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _result = true;
                    if (providerComponent != null) {
                        _data.writeInt(1);
                        providerComponent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (resultIntent != null) {
                        _data.writeInt(1);
                        resultIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().requestPinAppWidget(packageName, providerComponent, extras, resultIntent);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isRequestPinAppWidgetSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(26, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isRequestPinAppWidgetSupported();
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

        public static IAppWidgetService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAppWidgetService)) {
                return new Proxy(obj);
            }
            return (IAppWidgetService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "startListening";
                case 2:
                    return "stopListening";
                case 3:
                    return "allocateAppWidgetId";
                case 4:
                    return "deleteAppWidgetId";
                case 5:
                    return "deleteHost";
                case 6:
                    return "deleteAllHosts";
                case 7:
                    return "getAppWidgetViews";
                case 8:
                    return "getAppWidgetIdsForHost";
                case 9:
                    return "createAppWidgetConfigIntentSender";
                case 10:
                    return "updateAppWidgetIds";
                case 11:
                    return "updateAppWidgetOptions";
                case 12:
                    return "getAppWidgetOptions";
                case 13:
                    return "partiallyUpdateAppWidgetIds";
                case 14:
                    return "updateAppWidgetProvider";
                case 15:
                    return "updateAppWidgetProviderInfo";
                case 16:
                    return "notifyAppWidgetViewDataChanged";
                case 17:
                    return "getInstalledProvidersForProfile";
                case 18:
                    return "getAppWidgetInfo";
                case 19:
                    return "hasBindAppWidgetPermission";
                case 20:
                    return "setBindAppWidgetPermission";
                case 21:
                    return "bindAppWidgetId";
                case 22:
                    return "bindRemoteViewsService";
                case 23:
                    return "getAppWidgetIds";
                case 24:
                    return "isBoundWidgetPackage";
                case 25:
                    return "requestPinAppWidget";
                case 26:
                    return "isRequestPinAppWidgetSupported";
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
                int _result;
                String _arg0;
                int[] _arg1;
                RemoteViews _arg22;
                Bundle _arg23;
                ComponentName _arg02;
                boolean _result2;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        ParceledListSlice _result3 = startListening(com.android.internal.appwidget.IAppWidgetHost.Stub.asInterface(data.readStrongBinder()), data.readString(), data.readInt(), data.createIntArray());
                        reply.writeNoException();
                        if (_result3 != null) {
                            parcel2.writeInt(1);
                            _result3.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        stopListening(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        _result = allocateAppWidgetId(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        deleteAppWidgetId(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        deleteHost(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        deleteAllHosts();
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        RemoteViews _result4 = getAppWidgetViews(data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result4 != null) {
                            parcel2.writeInt(1);
                            _result4.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        int[] _result5 = getAppWidgetIdsForHost(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeIntArray(_result5);
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        IntentSender _result6 = createAppWidgetConfigIntentSender(data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = data.createIntArray();
                        if (data.readInt() != 0) {
                            _arg22 = (RemoteViews) RemoteViews.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        updateAppWidgetIds(_arg0, _arg1, _arg22);
                        reply.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        int _arg12 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg23 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        updateAppWidgetOptions(_arg0, _arg12, _arg23);
                        reply.writeNoException();
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        Bundle _result7 = getAppWidgetOptions(data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result7 != null) {
                            parcel2.writeInt(1);
                            _result7.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = data.createIntArray();
                        if (data.readInt() != 0) {
                            _arg22 = (RemoteViews) RemoteViews.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        partiallyUpdateAppWidgetIds(_arg0, _arg1, _arg22);
                        reply.writeNoException();
                        return true;
                    case 14:
                        RemoteViews _arg13;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg13 = (RemoteViews) RemoteViews.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        updateAppWidgetProvider(_arg02, _arg13);
                        reply.writeNoException();
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        updateAppWidgetProviderInfo(_arg02, data.readString());
                        reply.writeNoException();
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        notifyAppWidgetViewDataChanged(data.readString(), data.createIntArray(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        ParceledListSlice _result8 = getInstalledProvidersForProfile(data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result8 != null) {
                            parcel2.writeInt(1);
                            _result8.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        AppWidgetProviderInfo _result9 = getAppWidgetInfo(data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result9 != null) {
                            parcel2.writeInt(1);
                            _result9.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        _result2 = hasBindAppWidgetPermission(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        String _arg03 = data.readString();
                        _result = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setBindAppWidgetPermission(_arg03, _result, _arg2);
                        reply.writeNoException();
                        return true;
                    case 21:
                        ComponentName _arg3;
                        Bundle _arg4;
                        parcel.enforceInterface(descriptor);
                        String _arg04 = data.readString();
                        int _arg14 = data.readInt();
                        int _arg24 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg3 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg4 = null;
                        }
                        _arg2 = bindAppWidgetId(_arg04, _arg14, _arg24, _arg3, _arg4);
                        reply.writeNoException();
                        parcel2.writeInt(_arg2);
                        return true;
                    case 22:
                        Intent _arg25;
                        parcel.enforceInterface(descriptor);
                        String _arg05 = data.readString();
                        int _arg15 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg25 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg25 = null;
                        }
                        _arg2 = bindRemoteViewsService(_arg05, _arg15, _arg25, android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder()), data.readStrongBinder(), android.app.IServiceConnection.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg2);
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        _arg1 = getAppWidgetIds(_arg02);
                        reply.writeNoException();
                        parcel2.writeIntArray(_arg1);
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        _result2 = isBoundWidgetPackage(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 25:
                        ComponentName _arg16;
                        IntentSender _arg32;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg16 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg16 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg23 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg32 = (IntentSender) IntentSender.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg32 = null;
                        }
                        boolean _result10 = requestPinAppWidget(_arg0, _arg16, _arg23, _arg32);
                        reply.writeNoException();
                        parcel2.writeInt(_result10);
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        _arg2 = isRequestPinAppWidgetSupported();
                        reply.writeNoException();
                        parcel2.writeInt(_arg2);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IAppWidgetService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IAppWidgetService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    int allocateAppWidgetId(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    boolean bindAppWidgetId(String str, int i, int i2, ComponentName componentName, Bundle bundle) throws RemoteException;

    @UnsupportedAppUsage
    boolean bindRemoteViewsService(String str, int i, Intent intent, IApplicationThread iApplicationThread, IBinder iBinder, IServiceConnection iServiceConnection, int i2) throws RemoteException;

    IntentSender createAppWidgetConfigIntentSender(String str, int i, int i2) throws RemoteException;

    void deleteAllHosts() throws RemoteException;

    void deleteAppWidgetId(String str, int i) throws RemoteException;

    void deleteHost(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    int[] getAppWidgetIds(ComponentName componentName) throws RemoteException;

    int[] getAppWidgetIdsForHost(String str, int i) throws RemoteException;

    AppWidgetProviderInfo getAppWidgetInfo(String str, int i) throws RemoteException;

    Bundle getAppWidgetOptions(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    RemoteViews getAppWidgetViews(String str, int i) throws RemoteException;

    ParceledListSlice getInstalledProvidersForProfile(int i, int i2, String str) throws RemoteException;

    boolean hasBindAppWidgetPermission(String str, int i) throws RemoteException;

    boolean isBoundWidgetPackage(String str, int i) throws RemoteException;

    boolean isRequestPinAppWidgetSupported() throws RemoteException;

    void notifyAppWidgetViewDataChanged(String str, int[] iArr, int i) throws RemoteException;

    void partiallyUpdateAppWidgetIds(String str, int[] iArr, RemoteViews remoteViews) throws RemoteException;

    boolean requestPinAppWidget(String str, ComponentName componentName, Bundle bundle, IntentSender intentSender) throws RemoteException;

    void setBindAppWidgetPermission(String str, int i, boolean z) throws RemoteException;

    ParceledListSlice startListening(IAppWidgetHost iAppWidgetHost, String str, int i, int[] iArr) throws RemoteException;

    void stopListening(String str, int i) throws RemoteException;

    void updateAppWidgetIds(String str, int[] iArr, RemoteViews remoteViews) throws RemoteException;

    void updateAppWidgetOptions(String str, int i, Bundle bundle) throws RemoteException;

    void updateAppWidgetProvider(ComponentName componentName, RemoteViews remoteViews) throws RemoteException;

    void updateAppWidgetProviderInfo(ComponentName componentName, String str) throws RemoteException;
}
