package android.location;

import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface IGeocodeProvider extends IInterface {

    public static class Default implements IGeocodeProvider {
        public String getFromLocation(double latitude, double longitude, int maxResults, GeocoderParams params, List<Address> list) throws RemoteException {
            return null;
        }

        public String getFromLocationName(String locationName, double lowerLeftLatitude, double lowerLeftLongitude, double upperRightLatitude, double upperRightLongitude, int maxResults, GeocoderParams params, List<Address> list) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IGeocodeProvider {
        private static final String DESCRIPTOR = "android.location.IGeocodeProvider";
        static final int TRANSACTION_getFromLocation = 1;
        static final int TRANSACTION_getFromLocationName = 2;

        private static class Proxy implements IGeocodeProvider {
            public static IGeocodeProvider sDefaultImpl;
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
                            if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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
                    if (!this.mRemote.transact(2, _data, _reply2, 0)) {
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IGeocodeProvider asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IGeocodeProvider)) {
                return new Proxy(obj);
            }
            return (IGeocodeProvider) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "getFromLocation";
            }
            if (transactionCode != 2) {
                return null;
            }
            return "getFromLocationName";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            if (i == 1) {
                GeocoderParams _arg3;
                parcel.enforceInterface(descriptor);
                double _arg0 = data.readDouble();
                double _arg1 = data.readDouble();
                int _arg2 = data.readInt();
                if (data.readInt() != 0) {
                    _arg3 = (GeocoderParams) GeocoderParams.CREATOR.createFromParcel(parcel);
                } else {
                    _arg3 = null;
                }
                List<Address> _arg4 = new ArrayList();
                List<Address> _arg42 = _arg4;
                String _result = getFromLocation(_arg0, _arg1, _arg2, _arg3, _arg4);
                reply.writeNoException();
                parcel2.writeString(_result);
                parcel2.writeTypedList(_arg42);
                return true;
            } else if (i == 2) {
                GeocoderParams _arg6;
                parcel.enforceInterface(descriptor);
                String _arg02 = data.readString();
                double _arg12 = data.readDouble();
                double _arg22 = data.readDouble();
                double _arg32 = data.readDouble();
                double _arg43 = data.readDouble();
                int _arg5 = data.readInt();
                if (data.readInt() != 0) {
                    _arg6 = (GeocoderParams) GeocoderParams.CREATOR.createFromParcel(parcel);
                } else {
                    _arg6 = null;
                }
                List<Address> _arg7 = new ArrayList();
                String _result2 = getFromLocationName(_arg02, _arg12, _arg22, _arg32, _arg43, _arg5, _arg6, _arg7);
                reply.writeNoException();
                parcel2.writeString(_result2);
                parcel2.writeTypedList(_arg7);
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                parcel2.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IGeocodeProvider impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IGeocodeProvider getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    @UnsupportedAppUsage
    String getFromLocation(double d, double d2, int i, GeocoderParams geocoderParams, List<Address> list) throws RemoteException;

    @UnsupportedAppUsage
    String getFromLocationName(String str, double d, double d2, double d3, double d4, int i, GeocoderParams geocoderParams, List<Address> list) throws RemoteException;
}
