package miui.os;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.net.LocalSocketAddress.Namespace;
import android.os.SystemProperties;
import android.util.Slog;
import java.io.IOException;

public class MiuiBoosterClient {
    private static final String MIUI_BOOSTER_SOCKET_NAME = SystemProperties.get("persist.sys.miuibooster.name");
    public static final int SYSTEM_EVENT_BASE = 0;
    public static final int SYSTEM_EVENT_SLIDE_CLOSE = 2;
    public static final int SYSTEM_EVENT_SLIDE_OPEN = 1;
    private static final String TAG = "MiuiHardCoderClient";
    private static volatile MiuiBoosterClient sMiuiBoosterClient = null;
    private LocalSocketAddress mLocalSocketAddress;
    private LocalSocket mMiuiBoosterLocalSocket;

    public static MiuiBoosterClient getInstance() {
        if (sMiuiBoosterClient == null) {
            synchronized (MiuiBoosterClient.class) {
                if (sMiuiBoosterClient == null) {
                    sMiuiBoosterClient = new MiuiBoosterClient();
                }
            }
        }
        return sMiuiBoosterClient;
    }

    private MiuiBoosterClient() {
        try {
            this.mMiuiBoosterLocalSocket = new LocalSocket();
            this.mLocalSocketAddress = new LocalSocketAddress(MIUI_BOOSTER_SOCKET_NAME, Namespace.ABSTRACT);
            this.mMiuiBoosterLocalSocket.connect(this.mLocalSocketAddress);
        } catch (IOException ex) {
            Slog.e(TAG, "IOException ", ex);
        }
    }

    public boolean writeEvent(int event) {
        try {
            this.mMiuiBoosterLocalSocket.getOutputStream().write(event);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
