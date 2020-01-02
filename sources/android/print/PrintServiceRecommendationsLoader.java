package android.print;

import android.content.Context;
import android.content.Loader;
import android.os.Handler;
import android.os.Message;
import android.print.PrintManager.PrintServiceRecommendationsChangeListener;
import android.printservice.recommendation.RecommendationInfo;
import com.android.internal.util.Preconditions;
import java.util.List;

public class PrintServiceRecommendationsLoader extends Loader<List<RecommendationInfo>> {
    private final Handler mHandler = new MyHandler();
    private PrintServiceRecommendationsChangeListener mListener;
    private final PrintManager mPrintManager;

    private class MyHandler extends Handler {
        public MyHandler() {
            super(PrintServiceRecommendationsLoader.this.getContext().getMainLooper());
        }

        public void handleMessage(Message msg) {
            if (PrintServiceRecommendationsLoader.this.isStarted()) {
                PrintServiceRecommendationsLoader.this.deliverResult((List) msg.obj);
            }
        }
    }

    public PrintServiceRecommendationsLoader(PrintManager printManager, Context context) {
        super((Context) Preconditions.checkNotNull(context));
        this.mPrintManager = (PrintManager) Preconditions.checkNotNull(printManager);
    }

    /* Access modifiers changed, original: protected */
    public void onForceLoad() {
        queueNewResult();
    }

    private void queueNewResult() {
        Message m = this.mHandler.obtainMessage(0);
        m.obj = this.mPrintManager.getPrintServiceRecommendations();
        this.mHandler.sendMessage(m);
    }

    /* Access modifiers changed, original: protected */
    public void onStartLoading() {
        this.mListener = new PrintServiceRecommendationsChangeListener() {
            public void onPrintServiceRecommendationsChanged() {
                PrintServiceRecommendationsLoader.this.queueNewResult();
            }
        };
        this.mPrintManager.addPrintServiceRecommendationsChangeListener(this.mListener, null);
        deliverResult(this.mPrintManager.getPrintServiceRecommendations());
    }

    /* Access modifiers changed, original: protected */
    public void onStopLoading() {
        PrintServiceRecommendationsChangeListener printServiceRecommendationsChangeListener = this.mListener;
        if (printServiceRecommendationsChangeListener != null) {
            this.mPrintManager.removePrintServiceRecommendationsChangeListener(printServiceRecommendationsChangeListener);
            this.mListener = null;
        }
        this.mHandler.removeMessages(0);
    }

    /* Access modifiers changed, original: protected */
    public void onReset() {
        onStopLoading();
    }
}
