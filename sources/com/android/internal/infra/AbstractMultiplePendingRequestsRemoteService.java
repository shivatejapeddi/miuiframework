package com.android.internal.infra;

import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.IInterface;
import android.util.Slog;
import com.android.internal.infra.AbstractRemoteService.BasePendingRequest;
import com.android.internal.infra.AbstractRemoteService.VultureCallback;
import java.io.PrintWriter;
import java.util.ArrayList;

public abstract class AbstractMultiplePendingRequestsRemoteService<S extends AbstractMultiplePendingRequestsRemoteService<S, I>, I extends IInterface> extends AbstractRemoteService<S, I> {
    private final int mInitialCapacity;
    protected ArrayList<BasePendingRequest<S, I>> mPendingRequests;

    public AbstractMultiplePendingRequestsRemoteService(Context context, String serviceInterface, ComponentName componentName, int userId, VultureCallback<S> callback, Handler handler, int bindingFlags, boolean verbose, int initialCapacity) {
        super(context, serviceInterface, componentName, userId, callback, handler, bindingFlags, verbose);
        this.mInitialCapacity = initialCapacity;
    }

    /* Access modifiers changed, original: 0000 */
    public void handlePendingRequests() {
        int size = this.mPendingRequests;
        if (size != 0) {
            size = size.size();
            if (this.mVerbose) {
                String str = this.mTag;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Sending ");
                stringBuilder.append(size);
                stringBuilder.append(" pending requests");
                Slog.v(str, stringBuilder.toString());
            }
            for (int i = 0; i < size; i++) {
                ((BasePendingRequest) this.mPendingRequests.get(i)).run();
            }
            this.mPendingRequests = null;
        }
    }

    /* Access modifiers changed, original: protected */
    public void handleOnDestroy() {
        int size = this.mPendingRequests;
        if (size != 0) {
            size = size.size();
            if (this.mVerbose) {
                String str = this.mTag;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Canceling ");
                stringBuilder.append(size);
                stringBuilder.append(" pending requests");
                Slog.v(str, stringBuilder.toString());
            }
            for (int i = 0; i < size; i++) {
                ((BasePendingRequest) this.mPendingRequests.get(i)).cancel();
            }
            this.mPendingRequests = null;
        }
    }

    /* Access modifiers changed, original: final */
    public final void handleBindFailure() {
        int size = this.mPendingRequests;
        if (size != 0) {
            size = size.size();
            if (this.mVerbose) {
                String str = this.mTag;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Sending failure to ");
                stringBuilder.append(size);
                stringBuilder.append(" pending requests");
                Slog.v(str, stringBuilder.toString());
            }
            for (int i = 0; i < size; i++) {
                BasePendingRequest<S, I> request = (BasePendingRequest) this.mPendingRequests.get(i);
                request.onFailed();
                request.finish();
            }
            this.mPendingRequests = null;
        }
    }

    public void dump(String prefix, PrintWriter pw) {
        super.dump(prefix, pw);
        pw.append(prefix).append("initialCapacity=").append(String.valueOf(this.mInitialCapacity)).println();
        int size = this.mPendingRequests;
        pw.append(prefix).append("pendingRequests=").append(String.valueOf(size == 0 ? 0 : size.size())).println();
    }

    /* Access modifiers changed, original: 0000 */
    public void handlePendingRequestWhileUnBound(BasePendingRequest<S, I> pendingRequest) {
        if (this.mPendingRequests == null) {
            this.mPendingRequests = new ArrayList(this.mInitialCapacity);
        }
        this.mPendingRequests.add(pendingRequest);
        if (this.mVerbose) {
            String str = this.mTag;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("queued ");
            stringBuilder.append(this.mPendingRequests.size());
            stringBuilder.append(" requests; last=");
            stringBuilder.append(pendingRequest);
            Slog.v(str, stringBuilder.toString());
        }
    }
}
