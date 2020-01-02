package com.miui.mishare.app.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument.Page;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.CancellationSignal.OnCancelListener;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintAttributes.MediaSize;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentAdapter.LayoutResultCallback;
import android.print.PrintDocumentAdapter.WriteResultCallback;
import android.print.PrintDocumentInfo.Builder;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PrintHelper {
    public static final int COLOR_MODE_COLOR = 2;
    public static final int COLOR_MODE_MONOCHROME = 1;
    private static final String LOG_TAG = "PrintHelperKitkat";
    private static final int MAX_PRINT_SIZE = 3500;
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_PORTRAIT = 2;
    public static final int SCALE_MODE_FILL = 2;
    public static final int SCALE_MODE_FIT = 1;
    int mColorMode = 2;
    final Context mContext;
    Options mDecodeOptions = null;
    private final Object mLock = new Object();
    int mOrientation = 1;
    int mScaleMode = 2;

    public interface OnPrintFinishCallback {
        void onFinish();
    }

    public PrintHelper(Context context) {
        this.mContext = context;
    }

    public void setScaleMode(int scaleMode) {
        this.mScaleMode = scaleMode;
    }

    public int getScaleMode() {
        return this.mScaleMode;
    }

    public void setColorMode(int colorMode) {
        this.mColorMode = colorMode;
    }

    public void setOrientation(int orientation) {
        this.mOrientation = orientation;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public int getColorMode() {
        return this.mColorMode;
    }

    public void printBitmap(String jobName, Bitmap bitmap, OnPrintFinishCallback callback) {
        if (bitmap != null) {
            MediaSize mediaSize;
            int fittingMode = this.mScaleMode;
            PrintManager printManager = (PrintManager) this.mContext.getSystemService(Context.PRINT_SERVICE);
            MediaSize mediaSize2 = MediaSize.UNKNOWN_PORTRAIT;
            if (bitmap.getWidth() > bitmap.getHeight()) {
                mediaSize = MediaSize.UNKNOWN_LANDSCAPE;
            } else {
                mediaSize = mediaSize2;
            }
            final String str = jobName;
            final Bitmap bitmap2 = bitmap;
            final int i = fittingMode;
            final OnPrintFinishCallback onPrintFinishCallback = callback;
            printManager.print(jobName, new PrintDocumentAdapter() {
                private PrintAttributes mAttributes;

                public void onLayout(PrintAttributes oldPrintAttributes, PrintAttributes newPrintAttributes, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {
                    this.mAttributes = newPrintAttributes;
                    layoutResultCallback.onLayoutFinished(new Builder(str).setContentType(1).setPageCount(1).build(), true ^ newPrintAttributes.equals(oldPrintAttributes));
                }

                public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor fileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
                    PrintedPdfDocument pdfDocument = new PrintedPdfDocument(PrintHelper.this.mContext, this.mAttributes);
                    Bitmap maybeGrayscale = PrintHelper.this.convertBitmapForColorMode(bitmap2, this.mAttributes.getColorMode());
                    try {
                        Page page = pdfDocument.startPage(1);
                        page.getCanvas().drawBitmap(maybeGrayscale, PrintHelper.this.getMatrix(maybeGrayscale.getWidth(), maybeGrayscale.getHeight(), new RectF(page.getInfo().getContentRect()), i), null);
                        pdfDocument.finishPage(page);
                        pdfDocument.writeTo(new FileOutputStream(fileDescriptor.getFileDescriptor()));
                        writeResultCallback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                    } catch (IOException ioe) {
                        Log.e(PrintHelper.LOG_TAG, "Error writing printed content", ioe);
                        writeResultCallback.onWriteFailed(null);
                    } catch (Throwable th) {
                        pdfDocument.close();
                        if (fileDescriptor != null) {
                            try {
                                fileDescriptor.close();
                            } catch (IOException e) {
                            }
                        }
                        if (maybeGrayscale != bitmap2) {
                            maybeGrayscale.recycle();
                        }
                    }
                    pdfDocument.close();
                    if (fileDescriptor != null) {
                        try {
                            fileDescriptor.close();
                        } catch (IOException e2) {
                        }
                    }
                    if (maybeGrayscale != bitmap2) {
                        maybeGrayscale.recycle();
                    }
                }

                public void onFinish() {
                    OnPrintFinishCallback onPrintFinishCallback = onPrintFinishCallback;
                    if (onPrintFinishCallback != null) {
                        onPrintFinishCallback.onFinish();
                    }
                }
            }, new PrintAttributes.Builder().setMediaSize(mediaSize).setColorMode(this.mColorMode).build());
        }
    }

    private Matrix getMatrix(int imageWidth, int imageHeight, RectF content, int fittingMode) {
        Matrix matrix = new Matrix();
        float scale = content.width() / ((float) imageWidth);
        if (fittingMode == 2) {
            scale = Math.max(scale, content.height() / ((float) imageHeight));
        } else {
            scale = Math.min(scale, content.height() / ((float) imageHeight));
        }
        matrix.postScale(scale, scale);
        matrix.postTranslate((content.width() - (((float) imageWidth) * scale)) / 2.0f, (content.height() - (((float) imageHeight) * scale)) / 2.0f);
        return matrix;
    }

    public void printBitmap(String jobName, Uri imageFile, OnPrintFinishCallback callback) throws FileNotFoundException {
        final String str = jobName;
        final Uri uri = imageFile;
        final OnPrintFinishCallback onPrintFinishCallback = callback;
        final int i = this.mScaleMode;
        PrintDocumentAdapter printDocumentAdapter = new PrintDocumentAdapter() {
            private PrintAttributes mAttributes;
            Bitmap mBitmap = null;
            AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap;

            public void onLayout(PrintAttributes oldPrintAttributes, PrintAttributes newPrintAttributes, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {
                this.mAttributes = newPrintAttributes;
                if (cancellationSignal.isCanceled()) {
                    layoutResultCallback.onLayoutCancelled();
                } else if (this.mBitmap != null) {
                    layoutResultCallback.onLayoutFinished(new Builder(str).setContentType(1).setPageCount(1).build(), true ^ newPrintAttributes.equals(oldPrintAttributes));
                } else {
                    final CancellationSignal cancellationSignal2 = cancellationSignal;
                    final PrintAttributes printAttributes = newPrintAttributes;
                    final PrintAttributes printAttributes2 = oldPrintAttributes;
                    final LayoutResultCallback layoutResultCallback2 = layoutResultCallback;
                    this.mLoadBitmap = new AsyncTask<Uri, Boolean, Bitmap>() {
                        /* Access modifiers changed, original: protected */
                        public void onPreExecute() {
                            cancellationSignal2.setOnCancelListener(new OnCancelListener() {
                                public void onCancel() {
                                    AnonymousClass2.this.cancelLoad();
                                    AnonymousClass1.this.cancel(false);
                                }
                            });
                        }

                        /* Access modifiers changed, original: protected|varargs */
                        public Bitmap doInBackground(Uri... uris) {
                            try {
                                return PrintHelper.this.loadConstrainedBitmap(uri, PrintHelper.MAX_PRINT_SIZE);
                            } catch (FileNotFoundException e) {
                                return null;
                            }
                        }

                        /* Access modifiers changed, original: protected */
                        public void onPostExecute(Bitmap bitmap) {
                            super.onPostExecute(bitmap);
                            AnonymousClass2 anonymousClass2 = AnonymousClass2.this;
                            anonymousClass2.mBitmap = bitmap;
                            if (bitmap != null) {
                                layoutResultCallback2.onLayoutFinished(new Builder(str).setContentType(1).setPageCount(1).build(), true ^ printAttributes.equals(printAttributes2));
                            } else {
                                layoutResultCallback2.onLayoutFailed(null);
                            }
                            AnonymousClass2.this.mLoadBitmap = null;
                        }

                        /* Access modifiers changed, original: protected */
                        public void onCancelled(Bitmap result) {
                            layoutResultCallback2.onLayoutCancelled();
                            AnonymousClass2.this.mLoadBitmap = null;
                        }
                    }.execute((Object[]) new Uri[0]);
                }
            }

            private void cancelLoad() {
                synchronized (PrintHelper.this.mLock) {
                    if (PrintHelper.this.mDecodeOptions != null) {
                        PrintHelper.this.mDecodeOptions.requestCancelDecode();
                        PrintHelper.this.mDecodeOptions = null;
                    }
                }
            }

            public void onFinish() {
                super.onFinish();
                cancelLoad();
                AsyncTask asyncTask = this.mLoadBitmap;
                if (asyncTask != null) {
                    asyncTask.cancel(true);
                }
                OnPrintFinishCallback onPrintFinishCallback = onPrintFinishCallback;
                if (onPrintFinishCallback != null) {
                    onPrintFinishCallback.onFinish();
                }
                Bitmap bitmap = this.mBitmap;
                if (bitmap != null) {
                    bitmap.recycle();
                    this.mBitmap = null;
                }
            }

            public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor fileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
                PrintedPdfDocument pdfDocument = new PrintedPdfDocument(PrintHelper.this.mContext, this.mAttributes);
                Bitmap maybeGrayscale = PrintHelper.this.convertBitmapForColorMode(this.mBitmap, this.mAttributes.getColorMode());
                try {
                    Page page = pdfDocument.startPage(1);
                    page.getCanvas().drawBitmap(maybeGrayscale, PrintHelper.this.getMatrix(this.mBitmap.getWidth(), this.mBitmap.getHeight(), new RectF(page.getInfo().getContentRect()), i), null);
                    pdfDocument.finishPage(page);
                    pdfDocument.writeTo(new FileOutputStream(fileDescriptor.getFileDescriptor()));
                    writeResultCallback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                } catch (IOException ioe) {
                    Log.e(PrintHelper.LOG_TAG, "Error writing printed content", ioe);
                    writeResultCallback.onWriteFailed(null);
                } catch (Throwable th) {
                    pdfDocument.close();
                    if (fileDescriptor != null) {
                        try {
                            fileDescriptor.close();
                        } catch (IOException e) {
                        }
                    }
                    if (maybeGrayscale != this.mBitmap) {
                        maybeGrayscale.recycle();
                    }
                }
                pdfDocument.close();
                if (fileDescriptor != null) {
                    try {
                        fileDescriptor.close();
                    } catch (IOException e2) {
                    }
                }
                if (maybeGrayscale != this.mBitmap) {
                    maybeGrayscale.recycle();
                }
            }
        };
        PrintManager printManager = (PrintManager) this.mContext.getSystemService(Context.PRINT_SERVICE);
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setColorMode(this.mColorMode);
        int i2 = this.mOrientation;
        if (i2 == 1) {
            builder.setMediaSize(MediaSize.UNKNOWN_LANDSCAPE);
        } else if (i2 == 2) {
            builder.setMediaSize(MediaSize.UNKNOWN_PORTRAIT);
        }
        printManager.print(jobName, printDocumentAdapter, builder.build());
    }

    private Bitmap loadConstrainedBitmap(Uri uri, int maxSideLength) throws FileNotFoundException {
        if (maxSideLength <= 0 || uri == null || this.mContext == null) {
            throw new IllegalArgumentException("bad argument to getScaledBitmap");
        }
        Options opt = new Options();
        opt.inJustDecodeBounds = true;
        loadBitmap(uri, opt);
        int w = opt.outWidth;
        int h = opt.outHeight;
        if (w <= 0 || h <= 0) {
            return null;
        }
        int imageSide = Math.max(w, h);
        int sampleSize = 1;
        while (imageSide > maxSideLength) {
            imageSide >>>= 1;
            sampleSize <<= 1;
        }
        if (sampleSize <= 0 || Math.min(w, h) / sampleSize <= 0) {
            return null;
        }
        Options decodeOptions;
        synchronized (this.mLock) {
            this.mDecodeOptions = new Options();
            this.mDecodeOptions.inMutable = true;
            this.mDecodeOptions.inSampleSize = sampleSize;
            decodeOptions = this.mDecodeOptions;
        }
        try {
            Bitmap loadBitmap = loadBitmap(uri, decodeOptions);
            synchronized (this.mLock) {
                this.mDecodeOptions = null;
            }
            return loadBitmap;
        } catch (Throwable th) {
            synchronized (this.mLock) {
                this.mDecodeOptions = null;
            }
        }
    }

    private Bitmap loadBitmap(Uri uri, Options o) throws FileNotFoundException {
        String str = "close fail ";
        String str2 = LOG_TAG;
        if (uri != null) {
            Context context = this.mContext;
            if (context != null) {
                InputStream is = null;
                try {
                    is = context.getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is, null, o);
                    int orientation = ImageOrientationUtil.getExifRotation(ImageOrientationUtil.getFromMediaUri(this.mContext, this.mContext.getContentResolver(), uri));
                    if (orientation > 0 && bitmap != null) {
                        Matrix matrix = new Matrix();
                        matrix.postRotate((float) orientation);
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    }
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException t) {
                            Log.w(str2, str, t);
                        }
                    }
                    return bitmap;
                } catch (Throwable th) {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException t2) {
                            Log.w(str2, str, t2);
                        }
                    }
                }
            }
        }
        throw new IllegalArgumentException("bad argument to loadBitmap");
    }

    private Bitmap convertBitmapForColorMode(Bitmap original, int colorMode) {
        if (colorMode != 1) {
            return original;
        }
        Bitmap grayscale = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Config.ARGB_8888);
        Canvas c = new Canvas(grayscale);
        Paint p = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0.0f);
        p.setColorFilter(new ColorMatrixColorFilter(cm));
        c.drawBitmap(original, 0.0f, 0.0f, p);
        c.setBitmap(null);
        return grayscale;
    }
}
