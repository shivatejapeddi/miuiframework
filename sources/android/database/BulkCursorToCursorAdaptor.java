package android.database;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

public final class BulkCursorToCursorAdaptor extends AbstractWindowedCursor {
    private static final String TAG = "BulkCursor";
    private IBulkCursor mBulkCursor;
    private String[] mColumns;
    private int mCount;
    private SelfContentObserver mObserverBridge = new SelfContentObserver(this);
    private boolean mWantsAllOnMoveCalls;

    public void initialize(BulkCursorDescriptor d) {
        this.mBulkCursor = d.cursor;
        this.mColumns = d.columnNames;
        this.mWantsAllOnMoveCalls = d.wantsAllOnMoveCalls;
        this.mCount = d.count;
        if (d.window != null) {
            setWindow(d.window);
        }
    }

    public IContentObserver getObserver() {
        return this.mObserverBridge.getContentObserver();
    }

    private void throwIfCursorIsClosed() {
        if (this.mBulkCursor == null) {
            throw new StaleDataException("Attempted to access a cursor after it has been closed.");
        }
    }

    public int getCount() {
        throwIfCursorIsClosed();
        return this.mCount;
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0039  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0038 A:{RETURN} */
    public boolean onMove(int r5, int r6) {
        /*
        r4 = this;
        r4.throwIfCursorIsClosed();
        r0 = 0;
        r1 = r4.mWindow;	 Catch:{ RemoteException -> 0x003b }
        if (r1 == 0) goto L_0x002a;
    L_0x0008:
        r1 = r4.mWindow;	 Catch:{ RemoteException -> 0x003b }
        r1 = r1.getStartPosition();	 Catch:{ RemoteException -> 0x003b }
        if (r6 < r1) goto L_0x002a;
    L_0x0010:
        r1 = r4.mWindow;	 Catch:{ RemoteException -> 0x003b }
        r1 = r1.getStartPosition();	 Catch:{ RemoteException -> 0x003b }
        r2 = r4.mWindow;	 Catch:{ RemoteException -> 0x003b }
        r2 = r2.getNumRows();	 Catch:{ RemoteException -> 0x003b }
        r1 = r1 + r2;
        if (r6 < r1) goto L_0x0020;
    L_0x001f:
        goto L_0x002a;
    L_0x0020:
        r1 = r4.mWantsAllOnMoveCalls;	 Catch:{ RemoteException -> 0x003b }
        if (r1 == 0) goto L_0x0033;
    L_0x0024:
        r1 = r4.mBulkCursor;	 Catch:{ RemoteException -> 0x003b }
        r1.onMove(r6);	 Catch:{ RemoteException -> 0x003b }
        goto L_0x0033;
    L_0x002a:
        r1 = r4.mBulkCursor;	 Catch:{ RemoteException -> 0x003b }
        r1 = r1.getWindow(r6);	 Catch:{ RemoteException -> 0x003b }
        r4.setWindow(r1);	 Catch:{ RemoteException -> 0x003b }
        r1 = r4.mWindow;
        if (r1 != 0) goto L_0x0039;
    L_0x0038:
        return r0;
    L_0x0039:
        r0 = 1;
        return r0;
    L_0x003b:
        r1 = move-exception;
        r2 = "BulkCursor";
        r3 = "Unable to get window because the remote process is dead";
        android.util.Log.e(r2, r3);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.database.BulkCursorToCursorAdaptor.onMove(int, int):boolean");
    }

    public void deactivate() {
        super.deactivate();
        IBulkCursor iBulkCursor = this.mBulkCursor;
        if (iBulkCursor != null) {
            try {
                iBulkCursor.deactivate();
            } catch (RemoteException e) {
                Log.w(TAG, "Remote process exception when deactivating");
            }
        }
    }

    public void close() {
        super.close();
        IBulkCursor iBulkCursor = this.mBulkCursor;
        if (iBulkCursor != null) {
            try {
                iBulkCursor.close();
            } catch (RemoteException e) {
                Log.w(TAG, "Remote process exception when closing");
            } catch (Throwable th) {
                this.mBulkCursor = null;
            }
            this.mBulkCursor = null;
        }
    }

    public boolean requery() {
        throwIfCursorIsClosed();
        try {
            this.mCount = this.mBulkCursor.requery(getObserver());
            if (this.mCount != -1) {
                this.mPos = -1;
                closeWindow();
                super.requery();
                return true;
            }
            deactivate();
            return false;
        } catch (Exception ex) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to requery because the remote process exception ");
            stringBuilder.append(ex.getMessage());
            Log.e(TAG, stringBuilder.toString());
            deactivate();
            return false;
        }
    }

    public String[] getColumnNames() {
        throwIfCursorIsClosed();
        return this.mColumns;
    }

    public Bundle getExtras() {
        throwIfCursorIsClosed();
        try {
            return this.mBulkCursor.getExtras();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public Bundle respond(Bundle extras) {
        throwIfCursorIsClosed();
        try {
            return this.mBulkCursor.respond(extras);
        } catch (RemoteException e) {
            Log.w(TAG, "respond() threw RemoteException, returning an empty bundle.", e);
            return Bundle.EMPTY;
        }
    }
}
