package miui.securitycenter.powercenter;

import android.os.BatteryStats;
import android.os.MemoryFile;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.ParcelFileDescriptor.AutoCloseInputStream;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.android.internal.app.IBatteryStats.Stub;
import com.android.internal.os.BatteryStatsImpl;
import java.io.FileInputStream;
import java.io.IOException;

class BatteryStatsUtils {
    private static final String TAG = "BatteryStatsHelper";

    BatteryStatsUtils() {
    }

    static BatteryStatsImpl getBatteryStats() {
        IOException e;
        String str = TAG;
        String str2 = "fis close";
        String str3 = "";
        BatteryStatsImpl statsImpl = null;
        FileInputStream fis;
        Parcel parcel;
        try {
            ParcelFileDescriptor pfd = Stub.asInterface(ServiceManager.getService(BatteryStats.SERVICE_NAME)).getStatisticsStream();
            if (pfd != null) {
                fis = new AutoCloseInputStream(pfd);
                parcel = Parcel.obtain();
                try {
                    byte[] data = readFully(fis, MemoryFile.getSize(pfd.getFileDescriptor()));
                    parcel.unmarshall(data, 0, data.length);
                    parcel.setDataPosition(0);
                    statsImpl = (BatteryStatsImpl) BatteryStatsImpl.CREATOR.createFromParcel(parcel);
                    parcel.recycle();
                    try {
                        fis.close();
                    } catch (IOException e2) {
                        e = e2;
                    }
                } catch (IOException e3) {
                    Log.w(str, "Unable to read statistics stream", e3);
                    parcel.recycle();
                    try {
                        fis.close();
                    } catch (IOException e4) {
                        e3 = e4;
                    }
                }
            }
        } catch (RemoteException e5) {
            Log.e(str, "remote exception:", e5);
        } catch (Throwable th) {
            parcel.recycle();
            try {
                fis.close();
            } catch (IOException e6) {
                Log.e(str3, str2, e6);
            }
        }
        return statsImpl;
        Log.e(str3, str2, e3);
        return statsImpl;
    }

    private static byte[] readFully(FileInputStream stream, int avail) throws IOException {
        int pos = 0;
        byte[] data = new byte[avail];
        while (true) {
            int amt = stream.read(data, pos, data.length - pos);
            if (amt <= 0) {
                return data;
            }
            pos += amt;
            avail = stream.available();
            if (avail > data.length - pos) {
                byte[] newData = new byte[(pos + avail)];
                System.arraycopy(data, 0, newData, 0, pos);
                data = newData;
            }
        }
    }
}
