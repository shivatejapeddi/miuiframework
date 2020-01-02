package miui.contentcatcher.sdk;

import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import miui.contentcatcher.IContentCatcherService;
import miui.contentcatcher.IContentCatcherService.Stub;
import miui.contentcatcher.sdk.data.PageConfig;
import miui.contentcatcher.sdk.injector.IContentDecorateCallback;
import miui.contentcatcher.sdk.listener.IContentListenerCallback;

public class ContentCatcherManager {
    private static final String CONTENTCAP_SERVICE_NAME = "miui.contentcatcher.ContentCatcherService";
    private static boolean DEBUG = false;
    public static final int MI_LIFE_VERSION = 2;
    public static final int MI_MARKET_VERSION = 1;
    private static final String TAG = ContentCatcherManager.class.getSimpleName();
    @GuardedBy({"ContentCatcherManager.class"})
    private static volatile ContentCatcherManager sInstance = null;
    DeathRecipient mDeathHandler = new DeathRecipient() {
        public void binderDied() {
            Slog.w(ContentCatcherManager.TAG, "ContentCatcher binderDied!");
            if (ContentCatcherManager.this.mService != null) {
                ContentCatcherManager.this.mService.asBinder().unlinkToDeath(ContentCatcherManager.this.mDeathHandler, 0);
                ContentCatcherManager.this.mService = null;
            }
        }
    };
    private volatile IContentCatcherService mService;

    public static ContentCatcherManager getInstance() {
        if (sInstance == null) {
            synchronized (ContentCatcherManager.class) {
                if (sInstance == null) {
                    sInstance = new ContentCatcherManager();
                }
            }
        }
        return sInstance;
    }

    private ContentCatcherManager() {
    }

    public PageConfig getPageConfig(Token token) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getPageInjectorInfo ");
            stringBuilder.append(token);
            Slog.i(str, stringBuilder.toString());
        }
        try {
            IContentCatcherService service = getContentCatcherService();
            if (service != null) {
                return service.getPageConfig(token);
            }
        } catch (RemoteException e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("isPageInterested error: ");
            stringBuilder2.append(e.toString());
            Slog.e(str2, stringBuilder2.toString());
        }
        return null;
    }

    public void registerContentInjector(Token token, IContentDecorateCallback callback) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("registerContentInjector ");
            stringBuilder.append(token);
            Slog.i(str, stringBuilder.toString());
        }
        try {
            IContentCatcherService service = getContentCatcherService();
            if (service != null) {
                service.registerContentInjector(token, callback);
            }
        } catch (RemoteException e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("registerContentInjector error: ");
            stringBuilder2.append(e.toString());
            Slog.e(str2, stringBuilder2.toString());
        }
    }

    public void unregisterContentInjector(Token token) {
        if (DEBUG) {
            Slog.i(TAG, "unregisterContentInjector");
        }
        try {
            IContentCatcherService service = getContentCatcherService();
            if (service != null) {
                service.unregisterContentInjector(token);
            }
        } catch (RemoteException e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("unregisterContentInjector error: ");
            stringBuilder.append(e.toString());
            Slog.e(str, stringBuilder.toString());
        }
    }

    public void onContentCatched(Content content) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onContentCatched: ");
            stringBuilder.append(content);
            Slog.i(str, stringBuilder.toString());
        }
        try {
            IContentCatcherService service = getContentCatcherService();
            if (service != null) {
                service.onContentCatched(content);
            }
        } catch (RemoteException e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("onContentCatched error: ");
            stringBuilder2.append(e.toString());
            Slog.e(str2, stringBuilder2.toString());
        }
    }

    public void registerContentListener(ClientToken token, IContentListenerCallback callback) {
        if (DEBUG) {
            Slog.i(TAG, "registerContentListener");
        }
        try {
            IContentCatcherService service = getContentCatcherService();
            if (service != null) {
                service.registerContentListener(token, callback);
            }
        } catch (RemoteException e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("registerContentListener error: ");
            stringBuilder.append(e.toString());
            Slog.e(str, stringBuilder.toString());
        }
    }

    public void unregisterContentListener(ClientToken token) {
        if (DEBUG) {
            Slog.i(TAG, "unregisterContentListener");
        }
        try {
            IContentCatcherService service = getContentCatcherService();
            if (service != null) {
                service.unregisterContentListener(token);
            }
        } catch (RemoteException e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("unregisterContentListener error: ");
            stringBuilder.append(e.toString());
            Slog.e(str, stringBuilder.toString());
        }
    }

    public void decorateContent(ClientToken listenerToken, Token targetInjectorToken, DecorateContentParam params) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("decorateContent listenerToken ");
            stringBuilder.append(listenerToken);
            stringBuilder.append(", targetInjectorToken ");
            stringBuilder.append(targetInjectorToken);
            Slog.i(str, stringBuilder.toString());
        }
        try {
            IContentCatcherService service = getContentCatcherService();
            if (service != null) {
                service.decorateContent(listenerToken, targetInjectorToken, params);
            }
        } catch (RemoteException e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("decorateContent error: ");
            stringBuilder2.append(e.toString());
            Slog.e(str2, stringBuilder2.toString());
        }
    }

    public boolean updateConfig(String[] configs) {
        if (DEBUG) {
            Slog.i(TAG, "updateConfig");
        }
        try {
            IContentCatcherService service = getContentCatcherService();
            if (service != null) {
                service.updateConfig(configs);
            }
        } catch (RemoteException e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("updateConfig error: ");
            stringBuilder.append(e.toString());
            Slog.e(str, stringBuilder.toString());
        }
        return true;
    }

    public boolean updateClientConfig(String target, String jobTag, boolean enable) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("updateClientConfig target ");
            stringBuilder.append(target);
            stringBuilder.append(" jobTag ");
            stringBuilder.append(jobTag);
            stringBuilder.append(" enable ");
            stringBuilder.append(enable);
            Slog.i(str, stringBuilder.toString());
        }
        try {
            IContentCatcherService service = getContentCatcherService();
            if (service != null) {
                service.updateClientConfig(target, jobTag, enable);
            }
        } catch (RemoteException e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("updateClientConfig error: ");
            stringBuilder2.append(e.toString());
            Slog.e(str2, stringBuilder2.toString());
        }
        return true;
    }

    public void updateJobValidity(String jobTag, String packageName, boolean enable) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("updateJobValidity jobTag ");
            stringBuilder.append(jobTag);
            stringBuilder.append(" packageName ");
            stringBuilder.append(packageName);
            stringBuilder.append(" enable ");
            stringBuilder.append(enable);
            Slog.i(str, stringBuilder.toString());
        }
        try {
            IContentCatcherService service = getContentCatcherService();
            if (service != null) {
                service.updateJobValidity(jobTag, packageName, enable);
            }
        } catch (RemoteException e) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("updateJobValidity error: ");
            stringBuilder2.append(e.toString());
            Slog.e(str2, stringBuilder2.toString());
        }
    }

    /* Access modifiers changed, original: protected */
    public IContentCatcherService getContentCatcherService() {
        if (this.mService == null) {
            synchronized (this) {
                if (this.mService == null) {
                    this.mService = Stub.asInterface(ServiceManager.getService(CONTENTCAP_SERVICE_NAME));
                    if (this.mService != null) {
                        try {
                            this.mService.asBinder().linkToDeath(this.mDeathHandler, 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Slog.e(TAG, "failed to get ContentCatcherService.");
                    }
                }
            }
        }
        return this.mService;
    }
}
