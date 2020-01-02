package android.service.contentcapture;

import android.Manifest.permission;
import android.app.AppGlobals;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.RemoteException;
import android.util.Log;
import android.util.Slog;
import android.util.Xml;
import com.android.internal.R;
import java.io.IOException;
import java.io.PrintWriter;
import org.xmlpull.v1.XmlPullParserException;

public final class ContentCaptureServiceInfo {
    private static final String TAG = ContentCaptureServiceInfo.class.getSimpleName();
    private static final String XML_TAG_SERVICE = "content-capture-service";
    private final ServiceInfo mServiceInfo;
    private final String mSettingsActivity;

    private static ServiceInfo getServiceInfoOrThrow(ComponentName comp, boolean isTemp, int userId) throws NameNotFoundException {
        int flags = 128;
        if (!isTemp) {
            flags = 128 | 1048576;
        }
        ServiceInfo si = null;
        try {
            si = AppGlobals.getPackageManager().getServiceInfo(comp, flags, userId);
        } catch (RemoteException e) {
        }
        if (si != null) {
            return si;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Could not get serviceInfo for ");
        stringBuilder.append(isTemp ? " (temp)" : "(default system)");
        stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        stringBuilder.append(comp.flattenToShortString());
        throw new NameNotFoundException(stringBuilder.toString());
    }

    public ContentCaptureServiceInfo(Context context, ComponentName comp, boolean isTemporaryService, int userId) throws NameNotFoundException {
        this(context, getServiceInfoOrThrow(comp, isTemporaryService, userId));
    }

    private ContentCaptureServiceInfo(Context context, ServiceInfo si) {
        String str = si.permission;
        String str2 = permission.BIND_CONTENT_CAPTURE_SERVICE;
        if (str2.equals(str)) {
            this.mServiceInfo = si;
            XmlResourceParser parser = si.loadXmlMetaData(context.getPackageManager(), ContentCaptureService.SERVICE_META_DATA);
            if (parser == null) {
                this.mSettingsActivity = null;
                return;
            }
            str2 = null;
            TypedArray afsAttributes;
            try {
                Resources resources = context.getPackageManager().getResourcesForApplication(si.applicationInfo);
                int type = 0;
                while (type != 1 && type != 2) {
                    type = parser.next();
                }
                if (XML_TAG_SERVICE.equals(parser.getName())) {
                    afsAttributes = null;
                    afsAttributes = resources.obtainAttributes(Xml.asAttributeSet(parser), R.styleable.ContentCaptureService);
                    str2 = afsAttributes.getString(0);
                    afsAttributes.recycle();
                } else {
                    Log.e(TAG, "Meta-data does not start with content-capture-service tag");
                }
            } catch (NameNotFoundException | IOException | XmlPullParserException e) {
                Log.e(TAG, "Error parsing auto fill service meta-data", e);
            } catch (Throwable th) {
                if (afsAttributes != null) {
                    afsAttributes.recycle();
                }
            }
            this.mSettingsActivity = str2;
            return;
        }
        str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ContentCaptureService from '");
        stringBuilder.append(si.packageName);
        stringBuilder.append("' does not require permission ");
        stringBuilder.append(str2);
        Slog.w(str, stringBuilder.toString());
        throw new SecurityException("Service does not require permission android.permission.BIND_CONTENT_CAPTURE_SERVICE");
    }

    public ServiceInfo getServiceInfo() {
        return this.mServiceInfo;
    }

    public String getSettingsActivity() {
        return this.mSettingsActivity;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getSimpleName());
        builder.append("[");
        builder.append(this.mServiceInfo);
        builder.append(", settings:");
        builder.append(this.mSettingsActivity);
        return builder.toString();
    }

    public void dump(String prefix, PrintWriter pw) {
        pw.print(prefix);
        pw.print("Component: ");
        pw.println(getServiceInfo().getComponentName());
        pw.print(prefix);
        pw.print("Settings: ");
        pw.println(this.mSettingsActivity);
    }
}
