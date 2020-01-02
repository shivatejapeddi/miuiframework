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

public abstract class HostApduService extends Service {
    public static final int DEACTIVATION_DESELECTED = 1;
    public static final int DEACTIVATION_LINK_LOSS = 0;
    public static final String KEY_DATA = "data";
    public static final int MSG_COMMAND_APDU = 0;
    public static final int MSG_DEACTIVATED = 2;
    public static final int MSG_RESPONSE_APDU = 1;
    public static final int MSG_UNHANDLED = 3;
    public static final String SERVICE_INTERFACE = "android.nfc.cardemulation.action.HOST_APDU_SERVICE";
    public static final String SERVICE_META_DATA = "android.nfc.cardemulation.host_apdu_service";
    static final String TAG = "ApduService";
    final Messenger mMessenger = new Messenger(new MsgHandler());
    Messenger mNfcService = null;

    final class MsgHandler extends Handler {
        MsgHandler() {
        }

        public void handleMessage(Message msg) {
            String str;
            int i = msg.what;
            String str2 = "Response not sent; service was deactivated.";
            String str3 = HostApduService.TAG;
            if (i != 0) {
                str = "RemoteException calling into NfcService.";
                if (i != 1) {
                    if (i == 2) {
                        HostApduService hostApduService = HostApduService.this;
                        hostApduService.mNfcService = null;
                        hostApduService.onDeactivated(msg.arg1);
                    } else if (i != 3) {
                        super.handleMessage(msg);
                    } else if (HostApduService.this.mNfcService == null) {
                        Log.e(str3, "notifyUnhandled not sent; service was deactivated.");
                    } else {
                        try {
                            msg.replyTo = HostApduService.this.mMessenger;
                            HostApduService.this.mNfcService.send(msg);
                        } catch (RemoteException e) {
                            Log.e(str3, str);
                        }
                    }
                } else if (HostApduService.this.mNfcService == null) {
                    Log.e(str3, str2);
                } else {
                    try {
                        msg.replyTo = HostApduService.this.mMessenger;
                        HostApduService.this.mNfcService.send(msg);
                    } catch (RemoteException e2) {
                        Log.e(str3, str);
                    }
                }
            } else {
                Bundle dataBundle = msg.getData();
                if (dataBundle != null) {
                    if (HostApduService.this.mNfcService == null) {
                        HostApduService.this.mNfcService = msg.replyTo;
                    }
                    str = "data";
                    byte[] apdu = dataBundle.getByteArray(str);
                    if (apdu != null) {
                        byte[] responseApdu = HostApduService.this.processCommandApdu(apdu, null);
                        if (responseApdu != null) {
                            if (HostApduService.this.mNfcService == null) {
                                Log.e(str3, str2);
                                return;
                            }
                            Message responseMsg = Message.obtain(null, 1);
                            Bundle responseBundle = new Bundle();
                            responseBundle.putByteArray(str, responseApdu);
                            responseMsg.setData(responseBundle);
                            responseMsg.replyTo = HostApduService.this.mMessenger;
                            try {
                                HostApduService.this.mNfcService.send(responseMsg);
                            } catch (RemoteException e3) {
                                Log.e("TAG", "Response not sent; RemoteException calling into NfcService.");
                            }
                        }
                    } else {
                        Log.e(str3, "Received MSG_COMMAND_APDU without data.");
                    }
                }
            }
        }
    }

    public abstract void onDeactivated(int i);

    public abstract byte[] processCommandApdu(byte[] bArr, Bundle bundle);

    public final IBinder onBind(Intent intent) {
        return this.mMessenger.getBinder();
    }

    public final void sendResponseApdu(byte[] responseApdu) {
        Message responseMsg = Message.obtain((Handler) null, 1);
        Bundle dataBundle = new Bundle();
        dataBundle.putByteArray("data", responseApdu);
        responseMsg.setData(dataBundle);
        try {
            this.mMessenger.send(responseMsg);
        } catch (RemoteException e) {
            Log.e("TAG", "Local messenger has died.");
        }
    }

    public final void notifyUnhandled() {
        try {
            this.mMessenger.send(Message.obtain((Handler) null, 3));
        } catch (RemoteException e) {
            Log.e("TAG", "Local messenger has died.");
        }
    }
}
