package android.print;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.CancellationSignal.OnCancelListener;
import android.os.ParcelFileDescriptor;
import android.print.PrintDocumentAdapter.LayoutResultCallback;
import android.print.PrintDocumentAdapter.WriteResultCallback;
import com.android.internal.R;
import java.io.File;

public class PrintFileDocumentAdapter extends PrintDocumentAdapter {
    private static final String LOG_TAG = "PrintedFileDocAdapter";
    private final Context mContext;
    private final PrintDocumentInfo mDocumentInfo;
    private final File mFile;
    private WriteFileAsyncTask mWriteFileAsyncTask;

    private final class WriteFileAsyncTask extends AsyncTask<Void, Void, Void> {
        private final CancellationSignal mCancellationSignal;
        private final ParcelFileDescriptor mDestination;
        private final WriteResultCallback mResultCallback;

        public WriteFileAsyncTask(ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
            this.mDestination = destination;
            this.mResultCallback = callback;
            this.mCancellationSignal = cancellationSignal;
            this.mCancellationSignal.setOnCancelListener(new OnCancelListener(PrintFileDocumentAdapter.this) {
                public void onCancel() {
                    WriteFileAsyncTask.this.cancel(true);
                }
            });
        }

        /* Access modifiers changed, original: protected|varargs */
        /* JADX WARNING: Missing block: B:16:?, code skipped:
            $closeResource(r3, r2);
     */
        /* JADX WARNING: Missing block: B:23:?, code skipped:
            $closeResource(r2, r1);
     */
        public java.lang.Void doInBackground(java.lang.Void... r6) {
            /*
            r5 = this;
            r0 = 0;
            r1 = new java.io.FileInputStream;	 Catch:{ OperationCanceledException -> 0x004d, IOException -> 0x0032 }
            r2 = android.print.PrintFileDocumentAdapter.this;	 Catch:{ OperationCanceledException -> 0x004d, IOException -> 0x0032 }
            r2 = r2.mFile;	 Catch:{ OperationCanceledException -> 0x004d, IOException -> 0x0032 }
            r1.<init>(r2);	 Catch:{ OperationCanceledException -> 0x004d, IOException -> 0x0032 }
            r2 = new java.io.FileOutputStream;	 Catch:{ all -> 0x002b }
            r3 = r5.mDestination;	 Catch:{ all -> 0x002b }
            r3 = r3.getFileDescriptor();	 Catch:{ all -> 0x002b }
            r2.<init>(r3);	 Catch:{ all -> 0x002b }
            r3 = r5.mCancellationSignal;	 Catch:{ all -> 0x0024 }
            android.os.FileUtils.copy(r1, r2, r3, r0, r0);	 Catch:{ all -> 0x0024 }
            $closeResource(r0, r2);	 Catch:{ all -> 0x002b }
            $closeResource(r0, r1);	 Catch:{ OperationCanceledException -> 0x004d, IOException -> 0x0032 }
            goto L_0x004e;
        L_0x0024:
            r3 = move-exception;
            throw r3;	 Catch:{ all -> 0x0026 }
        L_0x0026:
            r4 = move-exception;
            $closeResource(r3, r2);	 Catch:{ all -> 0x002b }
            throw r4;	 Catch:{ all -> 0x002b }
        L_0x002b:
            r2 = move-exception;
            throw r2;	 Catch:{ all -> 0x002d }
        L_0x002d:
            r3 = move-exception;
            $closeResource(r2, r1);	 Catch:{ OperationCanceledException -> 0x004d, IOException -> 0x0032 }
            throw r3;	 Catch:{ OperationCanceledException -> 0x004d, IOException -> 0x0032 }
        L_0x0032:
            r1 = move-exception;
            r2 = "PrintedFileDocAdapter";
            r3 = "Error writing data!";
            android.util.Log.e(r2, r3, r1);
            r2 = r5.mResultCallback;
            r3 = android.print.PrintFileDocumentAdapter.this;
            r3 = r3.mContext;
            r4 = 17041441; // 0x1040821 float:2.4250403E-38 double:8.4195906E-317;
            r3 = r3.getString(r4);
            r2.onWriteFailed(r3);
            goto L_0x004f;
        L_0x004d:
            r1 = move-exception;
        L_0x004f:
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.print.PrintFileDocumentAdapter$WriteFileAsyncTask.doInBackground(java.lang.Void[]):java.lang.Void");
        }

        private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
            if (x0 != null) {
                try {
                    x1.close();
                    return;
                } catch (Throwable th) {
                    x0.addSuppressed(th);
                    return;
                }
            }
            x1.close();
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(Void result) {
            this.mResultCallback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
        }

        /* Access modifiers changed, original: protected */
        public void onCancelled(Void result) {
            this.mResultCallback.onWriteFailed(PrintFileDocumentAdapter.this.mContext.getString(R.string.write_fail_reason_cancelled));
        }
    }

    public PrintFileDocumentAdapter(Context context, File file, PrintDocumentInfo documentInfo) {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null!");
        } else if (documentInfo != null) {
            this.mContext = context;
            this.mFile = file;
            this.mDocumentInfo = documentInfo;
        } else {
            throw new IllegalArgumentException("documentInfo cannot be null!");
        }
    }

    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle metadata) {
        callback.onLayoutFinished(this.mDocumentInfo, false);
    }

    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
        this.mWriteFileAsyncTask = new WriteFileAsyncTask(destination, cancellationSignal, callback);
        this.mWriteFileAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
    }
}
