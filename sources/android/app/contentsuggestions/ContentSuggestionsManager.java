package android.app.contentsuggestions;

import android.annotation.SystemApi;
import android.app.contentsuggestions.IClassificationsCallback.Stub;
import android.os.Binder;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.util.SyncResultReceiver;
import java.util.List;
import java.util.concurrent.Executor;

@SystemApi
public final class ContentSuggestionsManager {
    private static final int SYNC_CALLS_TIMEOUT_MS = 5000;
    private static final String TAG = ContentSuggestionsManager.class.getSimpleName();
    private final IContentSuggestionsManager mService;
    private final int mUser;

    public interface ClassificationsCallback {
        void onContentClassificationsAvailable(int i, List<ContentClassification> list);
    }

    private static final class ClassificationsCallbackWrapper extends Stub {
        private final ClassificationsCallback mCallback;
        private final Executor mExecutor;

        ClassificationsCallbackWrapper(ClassificationsCallback callback, Executor executor) {
            this.mCallback = callback;
            this.mExecutor = executor;
        }

        public void onContentClassificationsAvailable(int statusCode, List<ContentClassification> classifications) {
            long identity = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new -$$Lambda$ContentSuggestionsManager$ClassificationsCallbackWrapper$bS71fhWJJl2gObzWDnBMzvYmM5w(this, statusCode, classifications));
            } finally {
                Binder.restoreCallingIdentity(identity);
            }
        }

        public /* synthetic */ void lambda$onContentClassificationsAvailable$0$ContentSuggestionsManager$ClassificationsCallbackWrapper(int statusCode, List classifications) {
            this.mCallback.onContentClassificationsAvailable(statusCode, classifications);
        }
    }

    public interface SelectionsCallback {
        void onContentSelectionsAvailable(int i, List<ContentSelection> list);
    }

    private static class SelectionsCallbackWrapper extends ISelectionsCallback.Stub {
        private final SelectionsCallback mCallback;
        private final Executor mExecutor;

        SelectionsCallbackWrapper(SelectionsCallback callback, Executor executor) {
            this.mCallback = callback;
            this.mExecutor = executor;
        }

        public void onContentSelectionsAvailable(int statusCode, List<ContentSelection> selections) {
            long identity = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new -$$Lambda$ContentSuggestionsManager$SelectionsCallbackWrapper$1Py0lukljawDYbfwobeRIUDvpNM(this, statusCode, selections));
            } finally {
                Binder.restoreCallingIdentity(identity);
            }
        }

        public /* synthetic */ void lambda$onContentSelectionsAvailable$0$ContentSuggestionsManager$SelectionsCallbackWrapper(int statusCode, List selections) {
            this.mCallback.onContentSelectionsAvailable(statusCode, selections);
        }
    }

    public ContentSuggestionsManager(int userId, IContentSuggestionsManager service) {
        this.mService = service;
        this.mUser = userId;
    }

    public void provideContextImage(int taskId, Bundle imageContextRequestExtras) {
        IContentSuggestionsManager iContentSuggestionsManager = this.mService;
        if (iContentSuggestionsManager == null) {
            Log.e(TAG, "provideContextImage called, but no ContentSuggestionsManager configured");
            return;
        }
        try {
            iContentSuggestionsManager.provideContextImage(this.mUser, taskId, imageContextRequestExtras);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void suggestContentSelections(SelectionsRequest request, Executor callbackExecutor, SelectionsCallback callback) {
        IContentSuggestionsManager iContentSuggestionsManager = this.mService;
        if (iContentSuggestionsManager == null) {
            Log.e(TAG, "suggestContentSelections called, but no ContentSuggestionsManager configured");
            return;
        }
        try {
            iContentSuggestionsManager.suggestContentSelections(this.mUser, request, new SelectionsCallbackWrapper(callback, callbackExecutor));
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void classifyContentSelections(ClassificationsRequest request, Executor callbackExecutor, ClassificationsCallback callback) {
        IContentSuggestionsManager iContentSuggestionsManager = this.mService;
        if (iContentSuggestionsManager == null) {
            Log.e(TAG, "classifyContentSelections called, but no ContentSuggestionsManager configured");
            return;
        }
        try {
            iContentSuggestionsManager.classifyContentSelections(this.mUser, request, new ClassificationsCallbackWrapper(callback, callbackExecutor));
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void notifyInteraction(String requestId, Bundle interaction) {
        IContentSuggestionsManager iContentSuggestionsManager = this.mService;
        if (iContentSuggestionsManager == null) {
            Log.e(TAG, "notifyInteraction called, but no ContentSuggestionsManager configured");
            return;
        }
        try {
            iContentSuggestionsManager.notifyInteraction(this.mUser, requestId, interaction);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public boolean isEnabled() {
        boolean z = false;
        if (this.mService == null) {
            return false;
        }
        SyncResultReceiver receiver = new SyncResultReceiver(5000);
        try {
            this.mService.isEnabled(this.mUser, receiver);
            if (receiver.getIntResult() != 0) {
                z = true;
            }
            return z;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }
}
