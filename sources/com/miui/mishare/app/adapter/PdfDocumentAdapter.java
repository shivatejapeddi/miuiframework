package com.miui.mishare.app.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentAdapter.LayoutResultCallback;
import android.print.PrintDocumentAdapter.WriteResultCallback;
import android.print.PrintDocumentInfo.Builder;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PdfDocumentAdapter extends PrintDocumentAdapter {
    private Context mContext = null;
    private String mFileName;
    private Uri mUri;

    public PdfDocumentAdapter(Context context, Uri uri, String fileName) {
        this.mContext = context;
        this.mUri = uri;
        this.mFileName = fileName;
    }

    public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes1, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {
        if (cancellationSignal.isCanceled()) {
            layoutResultCallback.onLayoutCancelled();
            return;
        }
        Builder builder = new Builder(this.mFileName);
        builder.setContentType(0).setPageCount(-1).build();
        layoutResultCallback.onLayoutFinished(builder.build(), printAttributes1.equals(printAttributes) ^ 1);
    }

    public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
        if (this.mUri != null) {
            InputStream in = null;
            try {
                in = this.mContext.getContentResolver().openInputStream(this.mUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (in != null) {
                OutputStream out = null;
                try {
                    out = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());
                    byte[] buf = new byte[16384];
                    while (true) {
                        int read = in.read(buf);
                        int size = read;
                        if (read >= 0 && !cancellationSignal.isCanceled()) {
                            out.write(buf, 0, size);
                        }
                    }
                    if (cancellationSignal.isCanceled()) {
                        writeResultCallback.onWriteCancelled();
                    } else {
                        writeResultCallback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                    }
                    try {
                        in.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    try {
                        out.close();
                    } catch (IOException e22) {
                        e22.printStackTrace();
                    }
                } catch (Exception e3) {
                    writeResultCallback.onWriteFailed(e3.getMessage());
                    try {
                        in.close();
                    } catch (IOException e222) {
                        e222.printStackTrace();
                    }
                    if (out != null) {
                        out.close();
                    }
                } catch (Throwable th) {
                    try {
                        in.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e42) {
                            e42.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
