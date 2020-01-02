package com.miui.mishare.app.activity;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrintAttributes.Builder;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import com.miui.mishare.app.adapter.PdfDocumentAdapter;
import com.miui.mishare.app.util.MiShareFileHelper;
import com.miui.mishare.app.util.PrintHelper;
import java.io.FileNotFoundException;

public class MiPrintControlActivity extends Activity {
    private static final String PRINT_PREFIX = "MIUI:";
    private int mResumeTimes = 0;

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        print(getIntent().getData());
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        super.onResume();
        this.mResumeTimes++;
        if (this.mResumeTimes > 1) {
            finish();
        }
    }

    private void print(Uri uri) {
        if (uri == null) {
            finish();
            return;
        }
        PrintHelper printHelper = new PrintHelper(this);
        printHelper.setScaleMode(1);
        String fileName = MiShareFileHelper.getFileNameFromUri(this, uri);
        if (MiShareFileHelper.isFilePdf(this, uri)) {
            doPdfPrint(uri, fileName);
        } else if (MiShareFileHelper.isImageCanPrint(this, uri)) {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(PRINT_PREFIX);
                stringBuilder.append(fileName);
                printHelper.printBitmap(stringBuilder.toString(), uri, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void doPdfPrint(Uri uri, String fileName) {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter printAdapter = new PdfDocumentAdapter(this, uri, fileName);
        if (printManager != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(PRINT_PREFIX);
            stringBuilder.append(fileName);
            printManager.print(stringBuilder.toString(), printAdapter, new Builder().build());
        }
    }
}
