package android.location;

import android.content.Context;
import android.location.GnssMeasurementsEvent.Callback;
import android.location.IGnssMeasurementsListener.Stub;
import android.os.RemoteException;
import com.android.internal.util.Preconditions;

class GnssMeasurementCallbackTransport extends LocalListenerHelper<Callback> {
    private static final String TAG = "GnssMeasCbTransport";
    private final IGnssMeasurementsListener mListenerTransport = new ListenerTransport();
    private final ILocationManager mLocationManager;

    private class ListenerTransport extends Stub {
        private ListenerTransport() {
        }

        public void onGnssMeasurementsReceived(final GnssMeasurementsEvent event) {
            GnssMeasurementCallbackTransport.this.foreach(new ListenerOperation<Callback>() {
                public void execute(Callback callback) throws RemoteException {
                    callback.onGnssMeasurementsReceived(event);
                }
            });
        }

        public void onStatusChanged(final int status) {
            GnssMeasurementCallbackTransport.this.foreach(new ListenerOperation<Callback>() {
                public void execute(Callback callback) throws RemoteException {
                    callback.onStatusChanged(status);
                }
            });
        }
    }

    public GnssMeasurementCallbackTransport(Context context, ILocationManager locationManager) {
        super(context, TAG);
        this.mLocationManager = locationManager;
    }

    /* Access modifiers changed, original: protected */
    public boolean registerWithServer() throws RemoteException {
        return this.mLocationManager.addGnssMeasurementsListener(this.mListenerTransport, getContext().getPackageName());
    }

    /* Access modifiers changed, original: protected */
    public void unregisterFromServer() throws RemoteException {
        this.mLocationManager.removeGnssMeasurementsListener(this.mListenerTransport);
    }

    /* Access modifiers changed, original: protected */
    public void injectGnssMeasurementCorrections(GnssMeasurementCorrections measurementCorrections) throws RemoteException {
        Preconditions.checkNotNull(measurementCorrections);
        this.mLocationManager.injectGnssMeasurementCorrections(measurementCorrections, getContext().getPackageName());
    }

    /* Access modifiers changed, original: protected */
    public long getGnssCapabilities() throws RemoteException {
        return this.mLocationManager.getGnssCapabilities(getContext().getPackageName());
    }
}
