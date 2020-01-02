package android.view.contentcapture;

import android.database.sqlite.SQLiteGlobal;
import android.util.DebugUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewStructure;
import android.view.autofill.AutofillId;
import android.view.contentcapture.ViewNode.ViewStructureImpl;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.Preconditions;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Random;

public abstract class ContentCaptureSession implements AutoCloseable {
    public static final int FLUSH_REASON_FULL = 1;
    public static final int FLUSH_REASON_IDLE_TIMEOUT = 5;
    public static final int FLUSH_REASON_SESSION_FINISHED = 4;
    public static final int FLUSH_REASON_SESSION_STARTED = 3;
    public static final int FLUSH_REASON_TEXT_CHANGE_TIMEOUT = 6;
    public static final int FLUSH_REASON_VIEW_ROOT_ENTERED = 2;
    private static final int INITIAL_CHILDREN_CAPACITY = 5;
    public static final int NO_SESSION_ID = 0;
    public static final int STATE_ACTIVE = 2;
    public static final int STATE_BY_APP = 64;
    public static final int STATE_DISABLED = 4;
    public static final int STATE_DUPLICATED_ID = 8;
    public static final int STATE_FLAG_SECURE = 32;
    public static final int STATE_INTERNAL_ERROR = 256;
    public static final int STATE_NOT_WHITELISTED = 512;
    public static final int STATE_NO_RESPONSE = 128;
    public static final int STATE_NO_SERVICE = 16;
    public static final int STATE_SERVICE_DIED = 1024;
    public static final int STATE_SERVICE_RESURRECTED = 4096;
    public static final int STATE_SERVICE_UPDATING = 2048;
    public static final int STATE_WAITING_FOR_SERVER = 1;
    private static final String TAG = ContentCaptureSession.class.getSimpleName();
    public static final int UNKNOWN_STATE = 0;
    private static final Random sIdGenerator = new Random();
    @GuardedBy({"mLock"})
    private ArrayList<ContentCaptureSession> mChildren;
    private ContentCaptureContext mClientContext;
    private ContentCaptureSessionId mContentCaptureSessionId;
    @GuardedBy({"mLock"})
    private boolean mDestroyed;
    protected final int mId;
    private final Object mLock;
    private int mState;

    @Retention(RetentionPolicy.SOURCE)
    public @interface FlushReason {
    }

    public abstract void flush(int i);

    public abstract MainContentCaptureSession getMainCaptureSession();

    public abstract void internalNotifyViewAppeared(ViewStructureImpl viewStructureImpl);

    public abstract void internalNotifyViewDisappeared(AutofillId autofillId);

    public abstract void internalNotifyViewTextChanged(AutofillId autofillId, CharSequence charSequence);

    public abstract void internalNotifyViewTreeEvent(boolean z);

    public abstract ContentCaptureSession newChild(ContentCaptureContext contentCaptureContext);

    public abstract void onDestroy();

    public abstract void updateContentCaptureContext(ContentCaptureContext contentCaptureContext);

    protected ContentCaptureSession() {
        this(getRandomSessionId());
    }

    @VisibleForTesting
    public ContentCaptureSession(int id) {
        this.mLock = new Object();
        boolean z = false;
        this.mState = 0;
        if (id != 0) {
            z = true;
        }
        Preconditions.checkArgument(z);
        this.mId = id;
    }

    ContentCaptureSession(ContentCaptureContext initialContext) {
        this();
        this.mClientContext = (ContentCaptureContext) Preconditions.checkNotNull(initialContext);
    }

    public final ContentCaptureSessionId getContentCaptureSessionId() {
        if (this.mContentCaptureSessionId == null) {
            this.mContentCaptureSessionId = new ContentCaptureSessionId(this.mId);
        }
        return this.mContentCaptureSessionId;
    }

    public int getId() {
        return this.mId;
    }

    public final ContentCaptureSession createContentCaptureSession(ContentCaptureContext context) {
        ContentCaptureSession child = newChild(context);
        if (ContentCaptureHelper.sDebug) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("createContentCaptureSession(");
            stringBuilder.append(context);
            stringBuilder.append(": parent=");
            stringBuilder.append(this.mId);
            stringBuilder.append(", child=");
            stringBuilder.append(child.mId);
            Log.d(str, stringBuilder.toString());
        }
        synchronized (this.mLock) {
            if (this.mChildren == null) {
                this.mChildren = new ArrayList(5);
            }
            this.mChildren.add(child);
        }
        return child;
    }

    public final void setContentCaptureContext(ContentCaptureContext context) {
        this.mClientContext = context;
        updateContentCaptureContext(context);
    }

    public final ContentCaptureContext getContentCaptureContext() {
        return this.mClientContext;
    }

    /* JADX WARNING: Missing block: B:9:0x0029, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:30:?, code skipped:
            flush(4);
     */
    /* JADX WARNING: Missing block: B:33:0x00bc, code skipped:
            onDestroy();
     */
    public final void destroy() {
        /*
        r8 = this;
        r0 = r8.mLock;
        monitor-enter(r0);
        r1 = r8.mDestroyed;	 Catch:{ all -> 0x00c0 }
        if (r1 == 0) goto L_0x002a;
    L_0x0007:
        r1 = android.view.contentcapture.ContentCaptureHelper.sDebug;	 Catch:{ all -> 0x00c0 }
        if (r1 == 0) goto L_0x0028;
    L_0x000b:
        r1 = TAG;	 Catch:{ all -> 0x00c0 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00c0 }
        r2.<init>();	 Catch:{ all -> 0x00c0 }
        r3 = "destroy(";
        r2.append(r3);	 Catch:{ all -> 0x00c0 }
        r3 = r8.mId;	 Catch:{ all -> 0x00c0 }
        r2.append(r3);	 Catch:{ all -> 0x00c0 }
        r3 = "): already destroyed";
        r2.append(r3);	 Catch:{ all -> 0x00c0 }
        r2 = r2.toString();	 Catch:{ all -> 0x00c0 }
        android.util.Log.d(r1, r2);	 Catch:{ all -> 0x00c0 }
    L_0x0028:
        monitor-exit(r0);	 Catch:{ all -> 0x00c0 }
        return;
    L_0x002a:
        r1 = 1;
        r8.mDestroyed = r1;	 Catch:{ all -> 0x00c0 }
        r1 = android.view.contentcapture.ContentCaptureHelper.sVerbose;	 Catch:{ all -> 0x00c0 }
        if (r1 == 0) goto L_0x0057;
    L_0x0031:
        r1 = TAG;	 Catch:{ all -> 0x00c0 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00c0 }
        r2.<init>();	 Catch:{ all -> 0x00c0 }
        r3 = "destroy(): state=";
        r2.append(r3);	 Catch:{ all -> 0x00c0 }
        r3 = r8.mState;	 Catch:{ all -> 0x00c0 }
        r3 = getStateAsString(r3);	 Catch:{ all -> 0x00c0 }
        r2.append(r3);	 Catch:{ all -> 0x00c0 }
        r3 = ", mId=";
        r2.append(r3);	 Catch:{ all -> 0x00c0 }
        r3 = r8.mId;	 Catch:{ all -> 0x00c0 }
        r2.append(r3);	 Catch:{ all -> 0x00c0 }
        r2 = r2.toString();	 Catch:{ all -> 0x00c0 }
        android.util.Log.v(r1, r2);	 Catch:{ all -> 0x00c0 }
    L_0x0057:
        r1 = r8.mChildren;	 Catch:{ all -> 0x00c0 }
        if (r1 == 0) goto L_0x00b1;
    L_0x005b:
        r1 = r8.mChildren;	 Catch:{ all -> 0x00c0 }
        r1 = r1.size();	 Catch:{ all -> 0x00c0 }
        r2 = android.view.contentcapture.ContentCaptureHelper.sVerbose;	 Catch:{ all -> 0x00c0 }
        if (r2 == 0) goto L_0x0080;
    L_0x0065:
        r2 = TAG;	 Catch:{ all -> 0x00c0 }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00c0 }
        r3.<init>();	 Catch:{ all -> 0x00c0 }
        r4 = "Destroying ";
        r3.append(r4);	 Catch:{ all -> 0x00c0 }
        r3.append(r1);	 Catch:{ all -> 0x00c0 }
        r4 = " children first";
        r3.append(r4);	 Catch:{ all -> 0x00c0 }
        r3 = r3.toString();	 Catch:{ all -> 0x00c0 }
        android.util.Log.v(r2, r3);	 Catch:{ all -> 0x00c0 }
    L_0x0080:
        r2 = 0;
    L_0x0081:
        if (r2 >= r1) goto L_0x00b1;
    L_0x0083:
        r3 = r8.mChildren;	 Catch:{ all -> 0x00c0 }
        r3 = r3.get(r2);	 Catch:{ all -> 0x00c0 }
        r3 = (android.view.contentcapture.ContentCaptureSession) r3;	 Catch:{ all -> 0x00c0 }
        r3.destroy();	 Catch:{ Exception -> 0x008f }
        goto L_0x00ae;
    L_0x008f:
        r4 = move-exception;
        r5 = TAG;	 Catch:{ all -> 0x00c0 }
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00c0 }
        r6.<init>();	 Catch:{ all -> 0x00c0 }
        r7 = "exception destroying child session #";
        r6.append(r7);	 Catch:{ all -> 0x00c0 }
        r6.append(r2);	 Catch:{ all -> 0x00c0 }
        r7 = ": ";
        r6.append(r7);	 Catch:{ all -> 0x00c0 }
        r6.append(r4);	 Catch:{ all -> 0x00c0 }
        r6 = r6.toString();	 Catch:{ all -> 0x00c0 }
        android.util.Log.w(r5, r6);	 Catch:{ all -> 0x00c0 }
    L_0x00ae:
        r2 = r2 + 1;
        goto L_0x0081;
    L_0x00b1:
        monitor-exit(r0);	 Catch:{ all -> 0x00c0 }
        r0 = 4;
        r8.flush(r0);	 Catch:{ all -> 0x00bb }
        r8.onDestroy();
        return;
    L_0x00bb:
        r0 = move-exception;
        r8.onDestroy();
        throw r0;
    L_0x00c0:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x00c0 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.contentcapture.ContentCaptureSession.destroy():void");
    }

    public void close() {
        destroy();
    }

    public final void notifyViewAppeared(ViewStructure node) {
        Preconditions.checkNotNull(node);
        if (!isContentCaptureEnabled()) {
            return;
        }
        if (node instanceof ViewStructureImpl) {
            internalNotifyViewAppeared((ViewStructureImpl) node);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid node class: ");
        stringBuilder.append(node.getClass());
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public final void notifyViewDisappeared(AutofillId id) {
        Preconditions.checkNotNull(id);
        if (isContentCaptureEnabled()) {
            internalNotifyViewDisappeared(id);
        }
    }

    public final void notifyViewsDisappeared(AutofillId hostId, long[] virtualIds) {
        boolean isNonVirtual = hostId.isNonVirtual();
        Object[] objArr = new Object[1];
        int i = 0;
        objArr[0] = hostId;
        Preconditions.checkArgument(isNonVirtual, "hostId cannot be virtual: %s", objArr);
        Preconditions.checkArgument(ArrayUtils.isEmpty(virtualIds) ^ 1, "virtual ids cannot be empty");
        if (isContentCaptureEnabled()) {
            int length = virtualIds.length;
            while (i < length) {
                internalNotifyViewDisappeared(new AutofillId(hostId, virtualIds[i], this.mId));
                i++;
            }
        }
    }

    public final void notifyViewTextChanged(AutofillId id, CharSequence text) {
        Preconditions.checkNotNull(id);
        if (isContentCaptureEnabled()) {
            internalNotifyViewTextChanged(id, text);
        }
    }

    public final ViewStructure newViewStructure(View view) {
        return new ViewStructureImpl(view);
    }

    public AutofillId newAutofillId(AutofillId hostId, long virtualChildId) {
        Preconditions.checkNotNull(hostId);
        Preconditions.checkArgument(hostId.isNonVirtual(), "hostId cannot be virtual: %s", hostId);
        return new AutofillId(hostId, virtualChildId, this.mId);
    }

    public final ViewStructure newVirtualViewStructure(AutofillId parentId, long virtualId) {
        return new ViewStructureImpl(parentId, virtualId, this.mId);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isContentCaptureEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = !this.mDestroyed;
        }
        return z;
    }

    /* Access modifiers changed, original: 0000 */
    public void dump(String prefix, PrintWriter pw) {
        pw.print(prefix);
        pw.print("id: ");
        pw.println(this.mId);
        if (this.mClientContext != null) {
            pw.print(prefix);
            this.mClientContext.dump(pw);
            pw.println();
        }
        synchronized (this.mLock) {
            pw.print(prefix);
            pw.print("destroyed: ");
            pw.println(this.mDestroyed);
            if (!(this.mChildren == null || this.mChildren.isEmpty())) {
                String prefix2 = new StringBuilder();
                prefix2.append(prefix);
                prefix2.append("  ");
                prefix2 = prefix2.toString();
                int numberChildren = this.mChildren.size();
                pw.print(prefix);
                pw.print("number children: ");
                pw.println(numberChildren);
                for (int i = 0; i < numberChildren; i++) {
                    ContentCaptureSession child = (ContentCaptureSession) this.mChildren.get(i);
                    pw.print(prefix);
                    pw.print(i);
                    pw.println(": ");
                    child.dump(prefix2, pw);
                }
            }
        }
    }

    public String toString() {
        return Integer.toString(this.mId);
    }

    protected static String getStateAsString(int state) {
        String str;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(state);
        stringBuilder.append(" (");
        if (state == 0) {
            str = IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        } else {
            str = DebugUtils.flagsToString(ContentCaptureSession.class, "STATE_", state);
        }
        stringBuilder.append(str);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public static String getFlushReasonAsString(int reason) {
        switch (reason) {
            case 1:
                return SQLiteGlobal.SYNC_MODE_FULL;
            case 2:
                return "VIEW_ROOT";
            case 3:
                return "STARTED";
            case 4:
                return "FINISHED";
            case 5:
                return "IDLE";
            case 6:
                return "TEXT_CHANGE";
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("UNKOWN-");
                stringBuilder.append(reason);
                return stringBuilder.toString();
        }
    }

    private static int getRandomSessionId() {
        while (true) {
            int id = sIdGenerator.nextInt();
            if (id != 0) {
                return id;
            }
        }
    }
}
