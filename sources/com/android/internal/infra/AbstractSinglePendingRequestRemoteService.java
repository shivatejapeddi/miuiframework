package com.android.internal.infra;

import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.IInterface;
import android.util.Slog;
import com.android.internal.infra.AbstractRemoteService.BasePendingRequest;
import com.android.internal.infra.AbstractRemoteService.VultureCallback;
import java.io.PrintWriter;

public abstract class AbstractSinglePendingRequestRemoteService<S extends AbstractSinglePendingRequestRemoteService<S, I>, I extends IInterface> extends AbstractRemoteService<S, I> {
    protected BasePendingRequest<S, I> mPendingRequest;

    public AbstractSinglePendingRequestRemoteService(Context context, String serviceInterface, ComponentName componentName, int userId, VultureCallback<S> callback, Handler handler, int bindingFlags, boolean verbose) {
        super(context, serviceInterface, componentName, userId, callback, handler, bindingFlags, verbose);
    }

    /* Access modifiers changed, original: 0000 */
    public void handlePendingRequests() {
        if (this.mPendingRequest != null) {
            BasePendingRequest<S, I> pendingRequest = this.mPendingRequest;
            this.mPendingRequest = null;
            handlePendingRequest(pendingRequest);
        }
    }

    /* Access modifiers changed, original: protected */
    public void handleOnDestroy() {
        handleCancelPendingRequest();
    }

    /* Access modifiers changed, original: protected */
    public BasePendingRequest<S, I> handleCancelPendingRequest() {
        BasePendingRequest<S, I> pendingRequest = this.mPendingRequest;
        if (pendingRequest != null) {
            pendingRequest.cancel();
            this.mPendingRequest = null;
        }
        return pendingRequest;
    }

    /* Access modifiers changed, original: 0000 */
    public void handleBindFailure() {
        if (this.mPendingRequest != null) {
            if (this.mVerbose) {
                String str = this.mTag;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Sending failure to ");
                stringBuilder.append(this.mPendingRequest);
                Slog.v(str, stringBuilder.toString());
            }
            this.mPendingRequest.onFailed();
            this.mPendingRequest = null;
        }
    }

    public void dump(String prefix, PrintWriter pw) {
        super.dump(prefix, pw);
        pw.append(prefix).append("hasPendingRequest=").append(String.valueOf(this.mPendingRequest != null)).println();
    }

    /* Access modifiers changed, original: 0000 */
    public void handlePendingRequestWhileUnBound(BasePendingRequest<S, I> pendingRequest) {
        if (this.mPendingRequest != null) {
            if (this.mVerbose) {
                String str = this.mTag;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("handlePendingRequestWhileUnBound(): cancelling ");
                stringBuilder.append(this.mPendingRequest);
                stringBuilder.append(" to handle ");
                stringBuilder.append(pendingRequest);
                Slog.v(str, stringBuilder.toString());
            }
            this.mPendingRequest.cancel();
        }
        this.mPendingRequest = pendingRequest;
    }
}
