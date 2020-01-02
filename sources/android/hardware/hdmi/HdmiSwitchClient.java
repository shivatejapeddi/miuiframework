package android.hardware.hdmi;

import android.annotation.SystemApi;
import android.hardware.hdmi.HdmiControlManager.ControlCallbackResult;
import android.hardware.hdmi.IHdmiControlCallback.Stub;
import android.os.Binder;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

@SystemApi
public class HdmiSwitchClient extends HdmiClient {
    private static final String TAG = "HdmiSwitchClient";

    @SystemApi
    public interface OnSelectListener {
        void onSelect(@ControlCallbackResult int i);
    }

    HdmiSwitchClient(IHdmiControlService service) {
        super(service);
    }

    private static IHdmiControlCallback getCallbackWrapper(final OnSelectListener listener) {
        return new Stub() {
            public void onComplete(int result) {
                listener.onSelect(result);
            }
        };
    }

    public int getDeviceType() {
        return 6;
    }

    public void selectDevice(int logicalAddress, OnSelectListener listener) {
        Preconditions.checkNotNull(listener);
        try {
            this.mService.deviceSelect(logicalAddress, getCallbackWrapper(listener));
        } catch (RemoteException e) {
            Log.e(TAG, "failed to select device: ", e);
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void selectPort(int portId, OnSelectListener listener) {
        Preconditions.checkNotNull(listener);
        try {
            this.mService.portSelect(portId, getCallbackWrapper(listener));
        } catch (RemoteException e) {
            Log.e(TAG, "failed to select port: ", e);
            throw e.rethrowFromSystemServer();
        }
    }

    public void selectDevice(int logicalAddress, final Executor executor, final OnSelectListener listener) {
        Preconditions.checkNotNull(listener);
        try {
            this.mService.deviceSelect(logicalAddress, new Stub() {
                public void onComplete(int result) {
                    Binder.withCleanCallingIdentity(new -$$Lambda$HdmiSwitchClient$2$knvX6ZgANoRRFcb_fUHlUdWIjCQ(executor, listener, result));
                }
            });
        } catch (RemoteException e) {
            Log.e(TAG, "failed to select device: ", e);
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void selectPort(int portId, final Executor executor, final OnSelectListener listener) {
        Preconditions.checkNotNull(listener);
        try {
            this.mService.portSelect(portId, new Stub() {
                public void onComplete(int result) {
                    Binder.withCleanCallingIdentity(new -$$Lambda$HdmiSwitchClient$3$Cqxvec1NmkC6VlEdX5OEOabobXY(executor, listener, result));
                }
            });
        } catch (RemoteException e) {
            Log.e(TAG, "failed to select port: ", e);
            throw e.rethrowFromSystemServer();
        }
    }

    public List<HdmiDeviceInfo> getDeviceList() {
        try {
            return this.mService.getDeviceList();
        } catch (RemoteException e) {
            Log.e("TAG", "Failed to call getDeviceList():", e);
            return Collections.emptyList();
        }
    }
}
