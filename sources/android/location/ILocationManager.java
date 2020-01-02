package android.location;

import android.annotation.UnsupportedAppUsage;
import android.app.PendingIntent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.internal.location.ProviderProperties;
import java.util.ArrayList;
import java.util.List;

public interface ILocationManager extends IInterface {

    public static class Default implements ILocationManager {
        public void requestLocationUpdates(LocationRequest request, ILocationListener listener, PendingIntent intent, String packageName) throws RemoteException {
        }

        public void removeUpdates(ILocationListener listener, PendingIntent intent, String packageName) throws RemoteException {
        }

        public void requestGeofence(LocationRequest request, Geofence geofence, PendingIntent intent, String packageName) throws RemoteException {
        }

        public void removeGeofence(Geofence fence, PendingIntent intent, String packageName) throws RemoteException {
        }

        public Location getLastLocation(LocationRequest request, String packageName) throws RemoteException {
            return null;
        }

        public boolean registerGnssStatusCallback(IGnssStatusListener callback, String packageName) throws RemoteException {
            return false;
        }

        public void unregisterGnssStatusCallback(IGnssStatusListener callback) throws RemoteException {
        }

        public boolean geocoderIsPresent() throws RemoteException {
            return false;
        }

        public String getFromLocation(double latitude, double longitude, int maxResults, GeocoderParams params, List<Address> list) throws RemoteException {
            return null;
        }

        public String getFromLocationName(String locationName, double lowerLeftLatitude, double lowerLeftLongitude, double upperRightLatitude, double upperRightLongitude, int maxResults, GeocoderParams params, List<Address> list) throws RemoteException {
            return null;
        }

        public boolean sendNiResponse(int notifId, int userResponse) throws RemoteException {
            return false;
        }

        public boolean addGnssMeasurementsListener(IGnssMeasurementsListener listener, String packageName) throws RemoteException {
            return false;
        }

        public void injectGnssMeasurementCorrections(GnssMeasurementCorrections corrections, String packageName) throws RemoteException {
        }

        public long getGnssCapabilities(String packageName) throws RemoteException {
            return 0;
        }

        public void removeGnssMeasurementsListener(IGnssMeasurementsListener listener) throws RemoteException {
        }

        public boolean addGnssNavigationMessageListener(IGnssNavigationMessageListener listener, String packageName) throws RemoteException {
            return false;
        }

        public void removeGnssNavigationMessageListener(IGnssNavigationMessageListener listener) throws RemoteException {
        }

        public int getGnssYearOfHardware() throws RemoteException {
            return 0;
        }

        public String getGnssHardwareModelName() throws RemoteException {
            return null;
        }

        public int getGnssBatchSize(String packageName) throws RemoteException {
            return 0;
        }

        public boolean addGnssBatchingCallback(IBatchedLocationCallback callback, String packageName) throws RemoteException {
            return false;
        }

        public void removeGnssBatchingCallback() throws RemoteException {
        }

        public boolean startGnssBatch(long periodNanos, boolean wakeOnFifoFull, String packageName) throws RemoteException {
            return false;
        }

        public void flushGnssBatch(String packageName) throws RemoteException {
        }

        public boolean stopGnssBatch() throws RemoteException {
            return false;
        }

        public boolean injectLocation(Location location) throws RemoteException {
            return false;
        }

        public List<String> getAllProviders() throws RemoteException {
            return null;
        }

        public List<String> getProviders(Criteria criteria, boolean enabledOnly) throws RemoteException {
            return null;
        }

        public String getBestProvider(Criteria criteria, boolean enabledOnly) throws RemoteException {
            return null;
        }

        public ProviderProperties getProviderProperties(String provider) throws RemoteException {
            return null;
        }

        public boolean isProviderPackage(String packageName) throws RemoteException {
            return false;
        }

        public void setExtraLocationControllerPackage(String packageName) throws RemoteException {
        }

        public String getExtraLocationControllerPackage() throws RemoteException {
            return null;
        }

        public void setExtraLocationControllerPackageEnabled(boolean enabled) throws RemoteException {
        }

        public boolean isExtraLocationControllerPackageEnabled() throws RemoteException {
            return false;
        }

        public boolean isProviderEnabledForUser(String provider, int userId) throws RemoteException {
            return false;
        }

        public boolean isLocationEnabledForUser(int userId) throws RemoteException {
            return false;
        }

        public void addTestProvider(String name, ProviderProperties properties, String opPackageName) throws RemoteException {
        }

        public void removeTestProvider(String provider, String opPackageName) throws RemoteException {
        }

        public void setTestProviderLocation(String provider, Location loc, String opPackageName) throws RemoteException {
        }

        public void setTestProviderEnabled(String provider, boolean enabled, String opPackageName) throws RemoteException {
        }

        public List<LocationRequest> getTestProviderCurrentRequests(String provider, String opPackageName) throws RemoteException {
            return null;
        }

        public LocationTime getGnssTimeMillis() throws RemoteException {
            return null;
        }

        public void setTestProviderStatus(String provider, int status, Bundle extras, long updateTime, String opPackageName) throws RemoteException {
        }

        public boolean sendExtraCommand(String provider, String command, Bundle extras) throws RemoteException {
            return false;
        }

        public void locationCallbackFinished(ILocationListener listener) throws RemoteException {
        }

        public String[] getBackgroundThrottlingWhitelist() throws RemoteException {
            return null;
        }

        public String[] getIgnoreSettingsWhitelist() throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ILocationManager {
        private static final String DESCRIPTOR = "android.location.ILocationManager";
        static final int TRANSACTION_addGnssBatchingCallback = 21;
        static final int TRANSACTION_addGnssMeasurementsListener = 12;
        static final int TRANSACTION_addGnssNavigationMessageListener = 16;
        static final int TRANSACTION_addTestProvider = 38;
        static final int TRANSACTION_flushGnssBatch = 24;
        static final int TRANSACTION_geocoderIsPresent = 8;
        static final int TRANSACTION_getAllProviders = 27;
        static final int TRANSACTION_getBackgroundThrottlingWhitelist = 47;
        static final int TRANSACTION_getBestProvider = 29;
        static final int TRANSACTION_getExtraLocationControllerPackage = 33;
        static final int TRANSACTION_getFromLocation = 9;
        static final int TRANSACTION_getFromLocationName = 10;
        static final int TRANSACTION_getGnssBatchSize = 20;
        static final int TRANSACTION_getGnssCapabilities = 14;
        static final int TRANSACTION_getGnssHardwareModelName = 19;
        static final int TRANSACTION_getGnssTimeMillis = 43;
        static final int TRANSACTION_getGnssYearOfHardware = 18;
        static final int TRANSACTION_getIgnoreSettingsWhitelist = 48;
        static final int TRANSACTION_getLastLocation = 5;
        static final int TRANSACTION_getProviderProperties = 30;
        static final int TRANSACTION_getProviders = 28;
        static final int TRANSACTION_getTestProviderCurrentRequests = 42;
        static final int TRANSACTION_injectGnssMeasurementCorrections = 13;
        static final int TRANSACTION_injectLocation = 26;
        static final int TRANSACTION_isExtraLocationControllerPackageEnabled = 35;
        static final int TRANSACTION_isLocationEnabledForUser = 37;
        static final int TRANSACTION_isProviderEnabledForUser = 36;
        static final int TRANSACTION_isProviderPackage = 31;
        static final int TRANSACTION_locationCallbackFinished = 46;
        static final int TRANSACTION_registerGnssStatusCallback = 6;
        static final int TRANSACTION_removeGeofence = 4;
        static final int TRANSACTION_removeGnssBatchingCallback = 22;
        static final int TRANSACTION_removeGnssMeasurementsListener = 15;
        static final int TRANSACTION_removeGnssNavigationMessageListener = 17;
        static final int TRANSACTION_removeTestProvider = 39;
        static final int TRANSACTION_removeUpdates = 2;
        static final int TRANSACTION_requestGeofence = 3;
        static final int TRANSACTION_requestLocationUpdates = 1;
        static final int TRANSACTION_sendExtraCommand = 45;
        static final int TRANSACTION_sendNiResponse = 11;
        static final int TRANSACTION_setExtraLocationControllerPackage = 32;
        static final int TRANSACTION_setExtraLocationControllerPackageEnabled = 34;
        static final int TRANSACTION_setTestProviderEnabled = 41;
        static final int TRANSACTION_setTestProviderLocation = 40;
        static final int TRANSACTION_setTestProviderStatus = 44;
        static final int TRANSACTION_startGnssBatch = 23;
        static final int TRANSACTION_stopGnssBatch = 25;
        static final int TRANSACTION_unregisterGnssStatusCallback = 7;

        private static class Proxy implements ILocationManager {
            public static ILocationManager sDefaultImpl;
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

            public void requestLocationUpdates(LocationRequest request, ILocationListener listener, PendingIntent intent, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestLocationUpdates(request, listener, intent, packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeUpdates(ILocationListener listener, PendingIntent intent, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeUpdates(listener, intent, packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestGeofence(LocationRequest request, Geofence geofence, PendingIntent intent, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (geofence != null) {
                        _data.writeInt(1);
                        geofence.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestGeofence(request, geofence, intent, packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeGeofence(Geofence fence, PendingIntent intent, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fence != null) {
                        _data.writeInt(1);
                        fence.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeGeofence(fence, intent, packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Location getLastLocation(LocationRequest request, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    Location location = this.mRemote;
                    if (!location.transact(5, _data, _reply, 0)) {
                        location = Stub.getDefaultImpl();
                        if (location != null) {
                            location = Stub.getDefaultImpl().getLastLocation(request, packageName);
                            return location;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        location = (Location) Location.CREATOR.createFromParcel(_reply);
                    } else {
                        location = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return location;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean registerGnssStatusCallback(IGnssStatusListener callback, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().registerGnssStatusCallback(callback, packageName);
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

            public void unregisterGnssStatusCallback(IGnssStatusListener callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterGnssStatusCallback(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean geocoderIsPresent() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().geocoderIsPresent();
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

            public String getFromLocation(double latitude, double longitude, int maxResults, GeocoderParams params, List<Address> addrs) throws RemoteException {
                Throwable th;
                List<Address> list;
                double d;
                GeocoderParams geocoderParams = params;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeDouble(latitude);
                        try {
                            _data.writeDouble(longitude);
                            _data.writeInt(maxResults);
                            if (geocoderParams != null) {
                                _data.writeInt(1);
                                geocoderParams.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            String _result;
                            if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _result = _reply.readString();
                                try {
                                    _reply.readTypedList(addrs, Address.CREATOR);
                                    _reply.recycle();
                                    _data.recycle();
                                    return _result;
                                } catch (Throwable th2) {
                                    th = th2;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            }
                            _result = Stub.getDefaultImpl().getFromLocation(latitude, longitude, maxResults, params, addrs);
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        } catch (Throwable th3) {
                            th = th3;
                            list = addrs;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        d = longitude;
                        list = addrs;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    double d2 = latitude;
                    d = longitude;
                    list = addrs;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public String getFromLocationName(String locationName, double lowerLeftLatitude, double lowerLeftLongitude, double upperRightLatitude, double upperRightLongitude, int maxResults, GeocoderParams params, List<Address> addrs) throws RemoteException {
                Throwable th;
                List<Address> list;
                Parcel _reply;
                GeocoderParams geocoderParams = params;
                Parcel _data = Parcel.obtain();
                Parcel _reply2 = Parcel.obtain();
                try {
                    Parcel _reply3;
                    String fromLocationName;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(locationName);
                    _data.writeDouble(lowerLeftLatitude);
                    _data.writeDouble(lowerLeftLongitude);
                    _data.writeDouble(upperRightLatitude);
                    _data.writeDouble(upperRightLongitude);
                    _data.writeInt(maxResults);
                    if (geocoderParams != null) {
                        try {
                            _data.writeInt(1);
                            geocoderParams.writeToParcel(_data, 0);
                        } catch (Throwable th2) {
                            th = th2;
                            list = addrs;
                            _reply = _reply2;
                        }
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(10, _data, _reply2, 0)) {
                        try {
                            if (Stub.getDefaultImpl() != null) {
                                _reply3 = _reply2;
                                try {
                                    fromLocationName = Stub.getDefaultImpl().getFromLocationName(locationName, lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude, maxResults, params, addrs);
                                    _reply3.recycle();
                                    _data.recycle();
                                    return fromLocationName;
                                } catch (Throwable th3) {
                                    th = th3;
                                    list = addrs;
                                    _reply = _reply3;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            list = addrs;
                            _reply = _reply2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    }
                    _reply3 = _reply2;
                    try {
                        _reply3.readException();
                        fromLocationName = _reply3.readString();
                        _reply = _reply3;
                        try {
                            _reply.readTypedList(addrs, Address.CREATOR);
                            _reply.recycle();
                            _data.recycle();
                            return fromLocationName;
                        } catch (Throwable th5) {
                            th = th5;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        list = addrs;
                        _reply = _reply3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    list = addrs;
                    _reply = _reply2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public boolean sendNiResponse(int notifId, int userResponse) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(notifId);
                    _data.writeInt(userResponse);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().sendNiResponse(notifId, userResponse);
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

            public boolean addGnssMeasurementsListener(IGnssMeasurementsListener listener, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().addGnssMeasurementsListener(listener, packageName);
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

            public void injectGnssMeasurementCorrections(GnssMeasurementCorrections corrections, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (corrections != null) {
                        _data.writeInt(1);
                        corrections.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().injectGnssMeasurementCorrections(corrections, packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getGnssCapabilities(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    long j = 14;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getGnssCapabilities(packageName);
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

            public void removeGnssMeasurementsListener(IGnssMeasurementsListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeGnssMeasurementsListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean addGnssNavigationMessageListener(IGnssNavigationMessageListener listener, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().addGnssNavigationMessageListener(listener, packageName);
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

            public void removeGnssNavigationMessageListener(IGnssNavigationMessageListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeGnssNavigationMessageListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getGnssYearOfHardware() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 18;
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getGnssYearOfHardware();
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

            public String getGnssHardwareModelName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 19;
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getGnssHardwareModelName();
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

            public int getGnssBatchSize(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    int i = 20;
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getGnssBatchSize(packageName);
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

            public boolean addGnssBatchingCallback(IBatchedLocationCallback callback, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(21, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().addGnssBatchingCallback(callback, packageName);
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

            public void removeGnssBatchingCallback() throws RemoteException {
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
                    Stub.getDefaultImpl().removeGnssBatchingCallback();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean startGnssBatch(long periodNanos, boolean wakeOnFifoFull, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(periodNanos);
                    boolean _result = true;
                    _data.writeInt(wakeOnFifoFull ? 1 : 0);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().startGnssBatch(periodNanos, wakeOnFifoFull, packageName);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void flushGnssBatch(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().flushGnssBatch(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean stopGnssBatch() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(25, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().stopGnssBatch();
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

            public boolean injectLocation(Location location) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (location != null) {
                        _data.writeInt(1);
                        location.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().injectLocation(location);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getAllProviders() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<String> list = 27;
                    if (!this.mRemote.transact(27, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getAllProviders();
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

            public List<String> getProviders(Criteria criteria, boolean enabledOnly) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    List<String> list = 0;
                    if (criteria != null) {
                        _data.writeInt(1);
                        criteria.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!enabledOnly) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (!this.mRemote.transact(28, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getProviders(criteria, enabledOnly);
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

            public String getBestProvider(Criteria criteria, boolean enabledOnly) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    String str = 0;
                    if (criteria != null) {
                        _data.writeInt(1);
                        criteria.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!enabledOnly) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (!this.mRemote.transact(29, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getBestProvider(criteria, enabledOnly);
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

            public ProviderProperties getProviderProperties(String provider) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(provider);
                    ProviderProperties providerProperties = 30;
                    if (!this.mRemote.transact(30, _data, _reply, 0)) {
                        providerProperties = Stub.getDefaultImpl();
                        if (providerProperties != 0) {
                            providerProperties = Stub.getDefaultImpl().getProviderProperties(provider);
                            return providerProperties;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        providerProperties = (ProviderProperties) ProviderProperties.CREATOR.createFromParcel(_reply);
                    } else {
                        providerProperties = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return providerProperties;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isProviderPackage(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(31, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isProviderPackage(packageName);
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

            public void setExtraLocationControllerPackage(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(32, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setExtraLocationControllerPackage(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getExtraLocationControllerPackage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 33;
                    if (!this.mRemote.transact(33, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getExtraLocationControllerPackage();
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

            public void setExtraLocationControllerPackageEnabled(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(34, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setExtraLocationControllerPackageEnabled(enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isExtraLocationControllerPackageEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(35, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isExtraLocationControllerPackageEnabled();
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

            public boolean isProviderEnabledForUser(String provider, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(provider);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(36, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isProviderEnabledForUser(provider, userId);
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

            public boolean isLocationEnabledForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(37, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isLocationEnabledForUser(userId);
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

            public void addTestProvider(String name, ProviderProperties properties, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    if (properties != null) {
                        _data.writeInt(1);
                        properties.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(opPackageName);
                    if (this.mRemote.transact(38, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addTestProvider(name, properties, opPackageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeTestProvider(String provider, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(provider);
                    _data.writeString(opPackageName);
                    if (this.mRemote.transact(39, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeTestProvider(provider, opPackageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setTestProviderLocation(String provider, Location loc, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(provider);
                    if (loc != null) {
                        _data.writeInt(1);
                        loc.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(opPackageName);
                    if (this.mRemote.transact(40, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setTestProviderLocation(provider, loc, opPackageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setTestProviderEnabled(String provider, boolean enabled, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(provider);
                    _data.writeInt(enabled ? 1 : 0);
                    _data.writeString(opPackageName);
                    if (this.mRemote.transact(41, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setTestProviderEnabled(provider, enabled, opPackageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<LocationRequest> getTestProviderCurrentRequests(String provider, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(provider);
                    _data.writeString(opPackageName);
                    List<LocationRequest> list = 42;
                    if (!this.mRemote.transact(42, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getTestProviderCurrentRequests(provider, opPackageName);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(LocationRequest.CREATOR);
                    List<LocationRequest> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public LocationTime getGnssTimeMillis() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    LocationTime locationTime = 43;
                    if (!this.mRemote.transact(43, _data, _reply, 0)) {
                        locationTime = Stub.getDefaultImpl();
                        if (locationTime != 0) {
                            locationTime = Stub.getDefaultImpl().getGnssTimeMillis();
                            return locationTime;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        locationTime = (LocationTime) LocationTime.CREATOR.createFromParcel(_reply);
                    } else {
                        locationTime = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return locationTime;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setTestProviderStatus(String provider, int status, Bundle extras, long updateTime, String opPackageName) throws RemoteException {
                Throwable th;
                long j;
                String str;
                int i;
                Bundle bundle = extras;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(provider);
                        try {
                            _data.writeInt(status);
                            if (bundle != null) {
                                _data.writeInt(1);
                                bundle.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            j = updateTime;
                            str = opPackageName;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        i = status;
                        j = updateTime;
                        str = opPackageName;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeLong(updateTime);
                        try {
                            _data.writeString(opPackageName);
                            if (this.mRemote.transact(44, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().setTestProviderStatus(provider, status, extras, updateTime, opPackageName);
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
                        str = opPackageName;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str2 = provider;
                    i = status;
                    j = updateTime;
                    str = opPackageName;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public boolean sendExtraCommand(String provider, String command, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(provider);
                    _data.writeString(command);
                    boolean _result = true;
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(45, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        if (_reply.readInt() != 0) {
                            extras.readFromParcel(_reply);
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().sendExtraCommand(provider, command, extras);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void locationCallbackFinished(ILocationListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(46, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().locationCallbackFinished(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getBackgroundThrottlingWhitelist() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 47;
                    if (!this.mRemote.transact(47, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getBackgroundThrottlingWhitelist();
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

            public String[] getIgnoreSettingsWhitelist() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 48;
                    if (!this.mRemote.transact(48, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getIgnoreSettingsWhitelist();
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ILocationManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ILocationManager)) {
                return new Proxy(obj);
            }
            return (ILocationManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "requestLocationUpdates";
                case 2:
                    return "removeUpdates";
                case 3:
                    return "requestGeofence";
                case 4:
                    return "removeGeofence";
                case 5:
                    return "getLastLocation";
                case 6:
                    return "registerGnssStatusCallback";
                case 7:
                    return "unregisterGnssStatusCallback";
                case 8:
                    return "geocoderIsPresent";
                case 9:
                    return "getFromLocation";
                case 10:
                    return "getFromLocationName";
                case 11:
                    return "sendNiResponse";
                case 12:
                    return "addGnssMeasurementsListener";
                case 13:
                    return "injectGnssMeasurementCorrections";
                case 14:
                    return "getGnssCapabilities";
                case 15:
                    return "removeGnssMeasurementsListener";
                case 16:
                    return "addGnssNavigationMessageListener";
                case 17:
                    return "removeGnssNavigationMessageListener";
                case 18:
                    return "getGnssYearOfHardware";
                case 19:
                    return "getGnssHardwareModelName";
                case 20:
                    return "getGnssBatchSize";
                case 21:
                    return "addGnssBatchingCallback";
                case 22:
                    return "removeGnssBatchingCallback";
                case 23:
                    return "startGnssBatch";
                case 24:
                    return "flushGnssBatch";
                case 25:
                    return "stopGnssBatch";
                case 26:
                    return "injectLocation";
                case 27:
                    return "getAllProviders";
                case 28:
                    return "getProviders";
                case 29:
                    return "getBestProvider";
                case 30:
                    return "getProviderProperties";
                case 31:
                    return "isProviderPackage";
                case 32:
                    return "setExtraLocationControllerPackage";
                case 33:
                    return "getExtraLocationControllerPackage";
                case 34:
                    return "setExtraLocationControllerPackageEnabled";
                case 35:
                    return "isExtraLocationControllerPackageEnabled";
                case 36:
                    return "isProviderEnabledForUser";
                case 37:
                    return "isLocationEnabledForUser";
                case 38:
                    return "addTestProvider";
                case 39:
                    return "removeTestProvider";
                case 40:
                    return "setTestProviderLocation";
                case 41:
                    return "setTestProviderEnabled";
                case 42:
                    return "getTestProviderCurrentRequests";
                case 43:
                    return "getGnssTimeMillis";
                case 44:
                    return "setTestProviderStatus";
                case 45:
                    return "sendExtraCommand";
                case 46:
                    return "locationCallbackFinished";
                case 47:
                    return "getBackgroundThrottlingWhitelist";
                case 48:
                    return "getIgnoreSettingsWhitelist";
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
            boolean z;
            if (i != 1598968902) {
                boolean _arg1 = false;
                Parcel parcel3;
                LocationRequest _arg0;
                PendingIntent _arg2;
                PendingIntent _arg12;
                boolean _result;
                boolean z2;
                String _result2;
                long _result3;
                Criteria _arg02;
                String _result4;
                String _arg03;
                String[] _result5;
                switch (i) {
                    case 1:
                        parcel3 = parcel2;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (LocationRequest) LocationRequest.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        ILocationListener _arg13 = android.location.ILocationListener.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg2 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        requestLocationUpdates(_arg0, _arg13, _arg2, data.readString());
                        reply.writeNoException();
                        return true;
                    case 2:
                        parcel3 = parcel2;
                        parcel.enforceInterface(descriptor);
                        ILocationListener _arg04 = android.location.ILocationListener.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg12 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        removeUpdates(_arg04, _arg12, data.readString());
                        reply.writeNoException();
                        return true;
                    case 3:
                        Geofence _arg14;
                        parcel3 = parcel2;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (LocationRequest) LocationRequest.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg14 = (Geofence) Geofence.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg2 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        requestGeofence(_arg0, _arg14, _arg2, data.readString());
                        reply.writeNoException();
                        return true;
                    case 4:
                        Geofence _arg05;
                        parcel3 = parcel2;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (Geofence) Geofence.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg05 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg12 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        removeGeofence(_arg05, _arg12, data.readString());
                        reply.writeNoException();
                        return true;
                    case 5:
                        LocationRequest _arg06;
                        parcel3 = parcel2;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg06 = (LocationRequest) LocationRequest.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg06 = null;
                        }
                        Location _result6 = getLastLocation(_arg06, data.readString());
                        reply.writeNoException();
                        if (_result6 != null) {
                            z = true;
                            parcel3.writeInt(1);
                            _result6.writeToParcel(parcel3, 1);
                        } else {
                            z = true;
                            parcel3.writeInt(0);
                        }
                        return z;
                    case 6:
                        parcel3 = parcel2;
                        parcel.enforceInterface(descriptor);
                        _result = registerGnssStatusCallback(android.location.IGnssStatusListener.Stub.asInterface(data.readStrongBinder()), data.readString());
                        reply.writeNoException();
                        parcel3.writeInt(_result);
                        return true;
                    case 7:
                        z2 = true;
                        parcel3 = parcel2;
                        parcel.enforceInterface(descriptor);
                        unregisterGnssStatusCallback(android.location.IGnssStatusListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return z2;
                    case 8:
                        parcel3 = parcel2;
                        parcel.enforceInterface(descriptor);
                        _arg1 = geocoderIsPresent();
                        reply.writeNoException();
                        parcel3.writeInt(_arg1);
                        return true;
                    case 9:
                        GeocoderParams _arg3;
                        parcel3 = parcel2;
                        parcel.enforceInterface(descriptor);
                        double _arg07 = data.readDouble();
                        descriptor = data.readDouble();
                        int _arg22 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg3 = (GeocoderParams) GeocoderParams.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        List<Address> _arg4 = new ArrayList();
                        List<Address> _arg42 = _arg4;
                        _result2 = getFromLocation(_arg07, descriptor, _arg22, _arg3, _arg4);
                        reply.writeNoException();
                        parcel3.writeString(_result2);
                        parcel3.writeTypedList(_arg42);
                        return true;
                    case 10:
                        GeocoderParams _arg6;
                        parcel.enforceInterface(descriptor);
                        String _arg08 = data.readString();
                        double _arg15 = data.readDouble();
                        double _arg23 = data.readDouble();
                        double _arg32 = data.readDouble();
                        double _arg43 = data.readDouble();
                        int _arg5 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg6 = (GeocoderParams) GeocoderParams.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg6 = null;
                        }
                        List<Address> _arg7 = new ArrayList();
                        boolean z3 = true;
                        parcel3 = parcel2;
                        _result2 = getFromLocationName(_arg08, _arg15, _arg23, _arg32, _arg43, _arg5, _arg6, _arg7);
                        reply.writeNoException();
                        parcel3.writeString(_result2);
                        parcel3.writeTypedList(_arg7);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        _result = sendNiResponse(data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        _result = addGnssMeasurementsListener(android.location.IGnssMeasurementsListener.Stub.asInterface(data.readStrongBinder()), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 13:
                        GnssMeasurementCorrections _arg09;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg09 = (GnssMeasurementCorrections) GnssMeasurementCorrections.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg09 = null;
                        }
                        injectGnssMeasurementCorrections(_arg09, data.readString());
                        reply.writeNoException();
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        _result3 = getGnssCapabilities(data.readString());
                        reply.writeNoException();
                        parcel2.writeLong(_result3);
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        removeGnssMeasurementsListener(android.location.IGnssMeasurementsListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        _result = addGnssNavigationMessageListener(android.location.IGnssNavigationMessageListener.Stub.asInterface(data.readStrongBinder()), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        removeGnssNavigationMessageListener(android.location.IGnssNavigationMessageListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        int _result7 = getGnssYearOfHardware();
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        _result2 = getGnssHardwareModelName();
                        reply.writeNoException();
                        parcel2.writeString(_result2);
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        int _result8 = getGnssBatchSize(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        _result = addGnssBatchingCallback(android.location.IBatchedLocationCallback.Stub.asInterface(data.readStrongBinder()), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        removeGnssBatchingCallback();
                        reply.writeNoException();
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        _result3 = data.readLong();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        z = startGnssBatch(_result3, _arg1, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(z);
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        flushGnssBatch(data.readString());
                        reply.writeNoException();
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        _arg1 = stopGnssBatch();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 26:
                        Location _arg010;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg010 = (Location) Location.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg010 = null;
                        }
                        z2 = injectLocation(_arg010);
                        reply.writeNoException();
                        parcel2.writeInt(z2);
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        List<String> _result9 = getAllProviders();
                        reply.writeNoException();
                        parcel2.writeStringList(_result9);
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (Criteria) Criteria.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        List<String> _result10 = getProviders(_arg02, _arg1);
                        reply.writeNoException();
                        parcel2.writeStringList(_result10);
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (Criteria) Criteria.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result4 = getBestProvider(_arg02, _arg1);
                        reply.writeNoException();
                        parcel2.writeString(_result4);
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        ProviderProperties _result11 = getProviderProperties(data.readString());
                        reply.writeNoException();
                        if (_result11 != null) {
                            parcel2.writeInt(1);
                            _result11.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        z2 = isProviderPackage(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(z2);
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        setExtraLocationControllerPackage(data.readString());
                        reply.writeNoException();
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        _result2 = getExtraLocationControllerPackage();
                        reply.writeNoException();
                        parcel2.writeString(_result2);
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setExtraLocationControllerPackageEnabled(_arg1);
                        reply.writeNoException();
                        return true;
                    case 35:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isExtraLocationControllerPackageEnabled();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        _result = isProviderEnabledForUser(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        z2 = isLocationEnabledForUser(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(z2);
                        return true;
                    case 38:
                        ProviderProperties _arg16;
                        parcel.enforceInterface(descriptor);
                        _result2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg16 = (ProviderProperties) ProviderProperties.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg16 = null;
                        }
                        addTestProvider(_result2, _arg16, data.readString());
                        reply.writeNoException();
                        return true;
                    case 39:
                        parcel.enforceInterface(descriptor);
                        removeTestProvider(data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 40:
                        Location _arg17;
                        parcel.enforceInterface(descriptor);
                        _result2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg17 = (Location) Location.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg17 = null;
                        }
                        setTestProviderLocation(_result2, _arg17, data.readString());
                        reply.writeNoException();
                        return true;
                    case 41:
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setTestProviderEnabled(_arg03, _arg1, data.readString());
                        reply.writeNoException();
                        return true;
                    case 42:
                        parcel.enforceInterface(descriptor);
                        List<LocationRequest> _result12 = getTestProviderCurrentRequests(data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result12);
                        return true;
                    case 43:
                        parcel.enforceInterface(descriptor);
                        LocationTime _result13 = getGnssTimeMillis();
                        reply.writeNoException();
                        if (_result13 != null) {
                            parcel2.writeInt(1);
                            _result13.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 44:
                        Bundle _arg24;
                        parcel.enforceInterface(descriptor);
                        String _arg011 = data.readString();
                        int _arg18 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg24 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg24 = null;
                        }
                        setTestProviderStatus(_arg011, _arg18, _arg24, data.readLong(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 45:
                        Bundle _arg25;
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readString();
                        _result4 = data.readString();
                        if (data.readInt() != 0) {
                            _arg25 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg25 = null;
                        }
                        z = sendExtraCommand(_arg03, _result4, _arg25);
                        reply.writeNoException();
                        parcel2.writeInt(z);
                        if (_arg25 != null) {
                            parcel2.writeInt(1);
                            _arg25.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 46:
                        parcel.enforceInterface(descriptor);
                        locationCallbackFinished(android.location.ILocationListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 47:
                        parcel.enforceInterface(descriptor);
                        _result5 = getBackgroundThrottlingWhitelist();
                        reply.writeNoException();
                        parcel2.writeStringArray(_result5);
                        return true;
                    case 48:
                        parcel.enforceInterface(descriptor);
                        _result5 = getIgnoreSettingsWhitelist();
                        reply.writeNoException();
                        parcel2.writeStringArray(_result5);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            z = true;
            parcel2.writeString(descriptor);
            return z;
        }

        public static boolean setDefaultImpl(ILocationManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ILocationManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean addGnssBatchingCallback(IBatchedLocationCallback iBatchedLocationCallback, String str) throws RemoteException;

    boolean addGnssMeasurementsListener(IGnssMeasurementsListener iGnssMeasurementsListener, String str) throws RemoteException;

    boolean addGnssNavigationMessageListener(IGnssNavigationMessageListener iGnssNavigationMessageListener, String str) throws RemoteException;

    void addTestProvider(String str, ProviderProperties providerProperties, String str2) throws RemoteException;

    void flushGnssBatch(String str) throws RemoteException;

    boolean geocoderIsPresent() throws RemoteException;

    @UnsupportedAppUsage
    List<String> getAllProviders() throws RemoteException;

    String[] getBackgroundThrottlingWhitelist() throws RemoteException;

    String getBestProvider(Criteria criteria, boolean z) throws RemoteException;

    String getExtraLocationControllerPackage() throws RemoteException;

    String getFromLocation(double d, double d2, int i, GeocoderParams geocoderParams, List<Address> list) throws RemoteException;

    String getFromLocationName(String str, double d, double d2, double d3, double d4, int i, GeocoderParams geocoderParams, List<Address> list) throws RemoteException;

    int getGnssBatchSize(String str) throws RemoteException;

    long getGnssCapabilities(String str) throws RemoteException;

    String getGnssHardwareModelName() throws RemoteException;

    LocationTime getGnssTimeMillis() throws RemoteException;

    int getGnssYearOfHardware() throws RemoteException;

    String[] getIgnoreSettingsWhitelist() throws RemoteException;

    Location getLastLocation(LocationRequest locationRequest, String str) throws RemoteException;

    ProviderProperties getProviderProperties(String str) throws RemoteException;

    List<String> getProviders(Criteria criteria, boolean z) throws RemoteException;

    List<LocationRequest> getTestProviderCurrentRequests(String str, String str2) throws RemoteException;

    void injectGnssMeasurementCorrections(GnssMeasurementCorrections gnssMeasurementCorrections, String str) throws RemoteException;

    boolean injectLocation(Location location) throws RemoteException;

    boolean isExtraLocationControllerPackageEnabled() throws RemoteException;

    boolean isLocationEnabledForUser(int i) throws RemoteException;

    boolean isProviderEnabledForUser(String str, int i) throws RemoteException;

    boolean isProviderPackage(String str) throws RemoteException;

    void locationCallbackFinished(ILocationListener iLocationListener) throws RemoteException;

    boolean registerGnssStatusCallback(IGnssStatusListener iGnssStatusListener, String str) throws RemoteException;

    void removeGeofence(Geofence geofence, PendingIntent pendingIntent, String str) throws RemoteException;

    void removeGnssBatchingCallback() throws RemoteException;

    void removeGnssMeasurementsListener(IGnssMeasurementsListener iGnssMeasurementsListener) throws RemoteException;

    void removeGnssNavigationMessageListener(IGnssNavigationMessageListener iGnssNavigationMessageListener) throws RemoteException;

    void removeTestProvider(String str, String str2) throws RemoteException;

    void removeUpdates(ILocationListener iLocationListener, PendingIntent pendingIntent, String str) throws RemoteException;

    void requestGeofence(LocationRequest locationRequest, Geofence geofence, PendingIntent pendingIntent, String str) throws RemoteException;

    void requestLocationUpdates(LocationRequest locationRequest, ILocationListener iLocationListener, PendingIntent pendingIntent, String str) throws RemoteException;

    boolean sendExtraCommand(String str, String str2, Bundle bundle) throws RemoteException;

    boolean sendNiResponse(int i, int i2) throws RemoteException;

    void setExtraLocationControllerPackage(String str) throws RemoteException;

    void setExtraLocationControllerPackageEnabled(boolean z) throws RemoteException;

    void setTestProviderEnabled(String str, boolean z, String str2) throws RemoteException;

    void setTestProviderLocation(String str, Location location, String str2) throws RemoteException;

    void setTestProviderStatus(String str, int i, Bundle bundle, long j, String str2) throws RemoteException;

    boolean startGnssBatch(long j, boolean z, String str) throws RemoteException;

    boolean stopGnssBatch() throws RemoteException;

    void unregisterGnssStatusCallback(IGnssStatusListener iGnssStatusListener) throws RemoteException;
}
