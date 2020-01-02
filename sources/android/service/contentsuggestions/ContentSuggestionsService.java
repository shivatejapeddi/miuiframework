package android.service.contentsuggestions;

import android.annotation.SystemApi;
import android.app.Service;
import android.app.contentsuggestions.ClassificationsRequest;
import android.app.contentsuggestions.ContentSuggestionsManager.ClassificationsCallback;
import android.app.contentsuggestions.ContentSuggestionsManager.SelectionsCallback;
import android.app.contentsuggestions.IClassificationsCallback;
import android.app.contentsuggestions.ISelectionsCallback;
import android.app.contentsuggestions.SelectionsRequest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorSpace;
import android.graphics.ColorSpace.Named;
import android.graphics.GraphicBuffer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.service.contentsuggestions.IContentSuggestionsService.Stub;
import android.util.Log;
import android.util.Slog;
import com.android.internal.util.function.pooled.PooledLambda;
import java.util.List;

@SystemApi
public abstract class ContentSuggestionsService extends Service {
    public static final String SERVICE_INTERFACE = "android.service.contentsuggestions.ContentSuggestionsService";
    private static final String TAG = ContentSuggestionsService.class.getSimpleName();
    private Handler mHandler;
    private final IContentSuggestionsService mInterface = new Stub() {
        public void provideContextImage(int taskId, GraphicBuffer contextImage, int colorSpaceId, Bundle imageContextRequestExtras) {
            Bitmap wrappedBuffer = null;
            if (contextImage != null) {
                ColorSpace colorSpace = null;
                if (colorSpaceId >= 0 && colorSpaceId < Named.values().length) {
                    colorSpace = ColorSpace.get(Named.values()[colorSpaceId]);
                }
                wrappedBuffer = Bitmap.wrapHardwareBuffer(contextImage, colorSpace);
            }
            ContentSuggestionsService.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$Mv-op4AGm9iWERwfXEFnqOVKWt0.INSTANCE, ContentSuggestionsService.this, Integer.valueOf(taskId), wrappedBuffer, imageContextRequestExtras));
        }

        public void suggestContentSelections(SelectionsRequest request, ISelectionsCallback callback) {
            Handler access$000 = ContentSuggestionsService.this.mHandler;
            -$$Lambda$yZSFRdNS_6TrQJ8NQKXAv0kSKzk -__lambda_yzsfrdns_6trqj8nqkxav0kskzk = -$$Lambda$yZSFRdNS_6TrQJ8NQKXAv0kSKzk.INSTANCE;
            ContentSuggestionsService contentSuggestionsService = ContentSuggestionsService.this;
            access$000.sendMessage(PooledLambda.obtainMessage(-__lambda_yzsfrdns_6trqj8nqkxav0kskzk, contentSuggestionsService, request, contentSuggestionsService.wrapSelectionsCallback(callback)));
        }

        public void classifyContentSelections(ClassificationsRequest request, IClassificationsCallback callback) {
            Handler access$000 = ContentSuggestionsService.this.mHandler;
            -$$Lambda$5oRtA6f92le979Nv8-bd2We4x10 -__lambda_5orta6f92le979nv8-bd2we4x10 = -$$Lambda$5oRtA6f92le979Nv8-bd2We4x10.INSTANCE;
            ContentSuggestionsService contentSuggestionsService = ContentSuggestionsService.this;
            access$000.sendMessage(PooledLambda.obtainMessage(-__lambda_5orta6f92le979nv8-bd2we4x10, contentSuggestionsService, request, contentSuggestionsService.wrapClassificationCallback(callback)));
        }

        public void notifyInteraction(String requestId, Bundle interaction) {
            ContentSuggestionsService.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$XFxerYS8emT_xgiGwwUrQtqnPnc.INSTANCE, ContentSuggestionsService.this, requestId, interaction));
        }
    };

    public abstract void onClassifyContentSelections(ClassificationsRequest classificationsRequest, ClassificationsCallback classificationsCallback);

    public abstract void onNotifyInteraction(String str, Bundle bundle);

    public abstract void onProcessContextImage(int i, Bitmap bitmap, Bundle bundle);

    public abstract void onSuggestContentSelections(SelectionsRequest selectionsRequest, SelectionsCallback selectionsCallback);

    public void onCreate() {
        super.onCreate();
        this.mHandler = new Handler(Looper.getMainLooper(), null, true);
    }

    public final IBinder onBind(Intent intent) {
        if (SERVICE_INTERFACE.equals(intent.getAction())) {
            return this.mInterface.asBinder();
        }
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Tried to bind to wrong intent (should be android.service.contentsuggestions.ContentSuggestionsService: ");
        stringBuilder.append(intent);
        Log.w(str, stringBuilder.toString());
        return null;
    }

    private SelectionsCallback wrapSelectionsCallback(ISelectionsCallback callback) {
        return new -$$Lambda$ContentSuggestionsService$Cq6WuwbJQLqgS0UnqLBYUMft1GM(callback);
    }

    static /* synthetic */ void lambda$wrapSelectionsCallback$0(ISelectionsCallback callback, int statusCode, List selections) {
        try {
            callback.onContentSelectionsAvailable(statusCode, selections);
        } catch (RemoteException e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error sending result: ");
            stringBuilder.append(e);
            Slog.e(str, stringBuilder.toString());
        }
    }

    private ClassificationsCallback wrapClassificationCallback(IClassificationsCallback callback) {
        return new -$$Lambda$ContentSuggestionsService$EMLezZyRGdfK3m-N1TAvrHKUEII(callback);
    }

    static /* synthetic */ void lambda$wrapClassificationCallback$1(IClassificationsCallback callback, int statusCode, List classifications) {
        try {
            callback.onContentClassificationsAvailable(statusCode, classifications);
        } catch (RemoteException e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error sending result: ");
            stringBuilder.append(e);
            Slog.e(str, stringBuilder.toString());
        }
    }
}
