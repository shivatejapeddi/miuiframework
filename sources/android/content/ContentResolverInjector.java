package android.content;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ICancellationSignal;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.provider.ContactsContract;
import android.util.Slog;
import java.util.ArrayList;

public class ContentResolverInjector {
    private static final boolean ENABLE = SystemProperties.getBoolean("persist.am.enable_crj", true);
    private static final String TAG = "ContentResolverInjector";
    private static ArrayList<String> mUnstablePackagesWhiteList = new ArrayList();

    static {
        mUnstablePackagesWhiteList.add(ContactsContract.AUTHORITY);
        mUnstablePackagesWhiteList.add("com.android.incallui");
        mUnstablePackagesWhiteList.add("com.android.mms");
    }

    public static boolean isForceAcquireUnstableProvider(String packageName, Uri uri) {
        if (!ENABLE || !mUnstablePackagesWhiteList.contains(packageName)) {
            return false;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("force acquire UnstableProvider for pkg=");
        stringBuilder.append(packageName);
        stringBuilder.append(", uri=");
        stringBuilder.append(uri);
        Slog.d(TAG, stringBuilder.toString());
        return true;
    }

    public static Cursor unstableQuery(ContentResolver cr, Uri uri, String[] projection, Bundle queryArgs, ICancellationSignal remoteCancellationSignal) {
        IContentProvider unstableProvider = cr.acquireUnstableProvider(uri);
        if (unstableProvider == null) {
            return null;
        }
        Cursor qCursor = null;
        try {
            qCursor = unstableProvider.query(cr.getPackageName(), uri, projection, queryArgs, remoteCancellationSignal);
        } catch (RemoteException e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("remote process has died again, unstableQuery pkg=");
            stringBuilder.append(cr.getPackageName());
            stringBuilder.append(", uri=");
            stringBuilder.append(uri);
            Slog.d(str, stringBuilder.toString());
            cr.unstableProviderDied(unstableProvider);
        } catch (Throwable th) {
            cr.releaseUnstableProvider(unstableProvider);
        }
        cr.releaseUnstableProvider(unstableProvider);
        return qCursor;
    }
}
