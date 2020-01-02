package android.nfc.cardemulation;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public abstract class HostNfcFService extends Service {
    public static final int DEACTIVATION_LINK_LOSS = 0;
    public static final String KEY_DATA = "data";
    public static final String KEY_MESSENGER = "messenger";
    public static final int MSG_COMMAND_PACKET = 0;
    public static final int MSG_DEACTIVATED = 2;
    public static final int MSG_RESPONSE_PACKET = 1;
    public static final String SERVICE_INTERFACE = "android.nfc.cardemulation.action.HOST_NFCF_SERVICE";
    public static final String SERVICE_META_DATA = "android.nfc.cardemulation.host_nfcf_service";
    static final String TAG = "NfcFService";
    final Messenger mMessenger = new Messenger(new MsgHandler());
    Messenger mNfcService = null;

    final class MsgHandler extends Handler {
        MsgHandler() {
        }

        public void handleMessage(Message msg) {
            int i = msg.what;
            String str = "Response not sent; service was deactivated.";
            String str2 = HostNfcFService.TAG;
            if (i == 0) {
                Bundle dataBundle = msg.getData();
                if (dataBundle != null) {
                    if (HostNfcFService.this.mNfcService == null) {
                        HostNfcFService.this.mNfcService = msg.replyTo;
                    }
                    String str3 = "data";
                    byte[] packet = dataBundle.getByteArray(str3);
                    if (packet != null) {
                        byte[] responsePacket = HostNfcFService.this.processNfcFPacket(packet, null);
                        if (responsePacket != null) {
                            if (HostNfcFService.this.mNfcService == null) {
                                Log.e(str2, str);
                                return;
                            }
                            Message responseMsg = Message.obtain(null, 1);
                            Bundle responseBundle = new Bundle();
                            responseBundle.putByteArray(str3, responsePacket);
                            responseMsg.setData(responseBundle);
                            responseMsg.replyTo = HostNfcFService.this.mMessenger;
                            try {
                                HostNfcFService.this.mNfcService.send(responseMsg);
                            } catch (RemoteException e) {
                                Log.e("TAG", "Response not sent; RemoteException calling into NfcService.");
                            }
                        }
                    } else {
                        Log.e(str2, "Received MSG_COMMAND_PACKET without data.");
                    }
                }
            } else if (i != 1) {
                if (i != 2) {
                    super.handleMessage(msg);
                } else {
                    HostNfcFService hostNfcFService = HostNfcFService.this;
                    hostNfcFService.mNfcService = null;
                    hostNfcFService.onDeactivated(msg.arg1);
                }
            } else if (HostNfcFService.this.mNfcService == null) {
                Log.e(str2, str);
            } else {
                try {
                    msg.replyTo = HostNfcFService.this.mMessenger;
                    HostNfcFService.this.mNfcService.send(msg);
                } catch (RemoteException e2) {
                    Log.e(str2, "RemoteException calling into NfcService.");
                }
            }
        }
    }

    public abstract void onDeactivated(int i);

    public abstract byte[] processNfcFPacket(byte[] bArr, Bundle bundle);

    public final IBinder onBind(Intent intent) {
        return this.mMessenger.getBinder();
    }

    public final void sendResponsePacket(byte[] responsePacket) {
        Message responseMsg = Message.obtain((Handler) null, 1);
        Bundle dataBundle = new Bundle();
        dataBundle.putByteArray("data", responsePacket);
        responseMsg.setData(dataBundle);
        try {
            this.mMessenger.send(responseMsg);
        } catch (RemoteException e) {
            Log.e("TAG", "Local messenger has died.");
        }
    }
}
