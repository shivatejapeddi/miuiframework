package android.service.carrier;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.service.carrier.ICarrierMessagingClientService.Stub;

public class CarrierMessagingClientService extends Service {
    private final ICarrierMessagingClientServiceImpl mImpl = new ICarrierMessagingClientServiceImpl();

    private class ICarrierMessagingClientServiceImpl extends Stub {
        private ICarrierMessagingClientServiceImpl() {
        }
    }

    public final IBinder onBind(Intent intent) {
        return this.mImpl.asBinder();
    }
}
