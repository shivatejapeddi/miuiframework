package android.graphics.improve;

import android.app.ActivityThread;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.miui.R;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.widget.ImageView;
import android.widget.Toast;
import java.lang.ref.WeakReference;
import java.util.HashSet;

public class SuperResolution {
    public static final String INTENT_INIT = "miui.intent.action.super_resolution_turnon";
    private static final int MODEL_TYPE = 0;
    private static final int MSG_IMPROVE = 998;
    private static final int MSG_INIT = 999;
    private static final int MSG_UPDATE_DRAWABLE = 1;
    public static final String PKG_MM = "com.tencent.mm";
    public static final String PKG_NEWS = "com.ss.android.article.news";
    public static final String TAG = "SuperResolution";
    private static volatile SuperResolution instance = null;
    private static final int mLimitMaxHeight = 800;
    private static final int mLimitMaxWidth = 800;
    private static final int mLimitMinHeight = 100;
    private static final int mLimitMinWidth = 100;
    private static final int mLimitTime = 4000;
    private HashSet<Integer> mBitmapRecord = null;
    private Context mContext;
    private String mCurPkg;
    private BitmapImproveAble mDrawableImprove;
    private ImproveHookAble mImproveHook;
    private boolean mInitComplete;
    private SuperResolutionReceiver mReceiver;
    private SuperResolutionHandler mSRHandler;
    private MainHandler mainHandler = null;

    public static class ImproveInfo {
        String clsName;
        Drawable drawable;
        Bitmap newBitmap;
        Bitmap oldBitmap;
        long startTime;
        private WeakReference<ImageView> weakImg;

        public void setImageView(ImageView imageView) {
            this.weakImg = new WeakReference(imageView);
        }

        public ImageView getImageView() {
            WeakReference weakReference = this.weakImg;
            if (weakReference != null) {
                return (ImageView) weakReference.get();
            }
            return null;
        }
    }

    private class MainHandler extends Handler {
        public MainHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                ImproveInfo info = msg.obj;
                ImageView imageView = info.getImageView();
                if (SuperResolution.this.checkViewIsAvailable(imageView) && !SuperResolution.this.isTimeOutOfLimit(info.startTime) && !SuperResolution.this.drawableHasChanged(imageView, info)) {
                    try {
                        Matrix matrix = imageView.getImageMatrix();
                        SuperResolution.this.mImproveHook.setBitmap(info, imageView);
                        imageView.setImageMatrix(matrix);
                        SRLog.d("handle message", "update drawable successful");
                    } catch (Throwable e) {
                        SRReporter.reportFailure(imageView.getContext(), "hook fail");
                        SRLog.e("update drawable fail", e.getMessage());
                    }
                }
            }
        }
    }

    private class SuperResolutionHandler extends Handler {
        public SuperResolutionHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i == 998) {
                ImproveInfo info = msg.obj;
                long startTime = SystemClock.uptimeMillis();
                SRReporter.reportImprove();
                SRLog.d("start improve bitmap", SuperResolution.this.getBitmapInfo(info.oldBitmap));
                Bitmap newBitmap = SuperResolution.this.mDrawableImprove.improveBitmap(info.oldBitmap);
                ImageView imageView = info.getImageView();
                if (imageView != null) {
                    String str = "handle message";
                    if (newBitmap == null || newBitmap.equals(info.oldBitmap)) {
                        SRLog.e(str, "improve fail !!!");
                        SRReporter.reportFailure(imageView.getContext(), "algorithm error");
                        return;
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("improve bitmap successful");
                    stringBuilder.append(SuperResolution.this.getBitmapInfo(newBitmap));
                    SRLog.d(str, stringBuilder.toString());
                    info.newBitmap = newBitmap;
                    SuperResolution.this.add2Record(imageView, info.newBitmap);
                    SRReporter.reportImproveTime(SystemClock.uptimeMillis() - startTime);
                    SuperResolution.this.checkTime(startTime, "check improve use time");
                    if (!SuperResolution.this.isTimeOutOfLimit(info.startTime)) {
                        SuperResolution.this.mainHandler.sendMessageDelayed(SuperResolution.this.mainHandler.obtainMessage(1, info), (info.startTime + 500) - SystemClock.uptimeMillis());
                    }
                }
            } else if (i == 999) {
                SuperResolution.this.initImproveInSRThread();
            }
        }
    }

    public class SuperResolutionReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String str = SuperResolution.INTENT_INIT;
            SRLog.d(str, "onReceive");
            if (intent != null && str.equals(intent.getAction())) {
                if (SuperResolution.this.isAllowInitAsync(context)) {
                    SuperResolution.this.initAsync(context);
                } else {
                    SuperResolution.this.mInitComplete = false;
                }
            }
        }
    }

    private boolean checkViewIsAvailable(ImageView imageView) {
        if (imageView != null && imageView.isAttachedToWindow()) {
            return true;
        }
        return false;
    }

    private SuperResolution() {
    }

    public static SuperResolution getInstance(Context context) {
        if (instance == null) {
            synchronized (SuperResolution.class) {
                if (instance == null) {
                    SuperResolution temp = new SuperResolution();
                    temp.init(context);
                    instance = temp;
                }
            }
        }
        return instance;
    }

    private void registerReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(INTENT_INIT);
        context.registerReceiver(this.mReceiver, filter);
    }

    private void init(Context context) {
        if (context != null && !ActivityThread.isSystem()) {
            this.mContext = context.getApplicationContext();
            this.mCurPkg = context.getPackageName();
            this.mReceiver = new SuperResolutionReceiver();
            if (isAllowInitSync()) {
                registerReceiver(context);
                initAsync(context);
            }
        }
    }

    private void initAsync(final Context context) {
        new AsyncTask<Void, Void, Void>() {
            /* Access modifiers changed, original: protected|varargs */
            public Void doInBackground(Void... params) {
                SuperResolution.this.initInChildThread(context);
                return null;
            }
        }.execute((Object[]) new Void[0]);
    }

    private void initInChildThread(Context context) {
        if (isAllowInitAsync(context)) {
            synchronized (SuperResolution.class) {
                if (this.mSRHandler != null) {
                    this.mInitComplete = true;
                    return;
                }
                this.mImproveHook = getImproveHook(context, context.getPackageName());
                if (this.mImproveHook == null) {
                    return;
                }
                HandlerThread superResolutionThread = new HandlerThread(TAG);
                superResolutionThread.start();
                this.mSRHandler = new SuperResolutionHandler(superResolutionThread.getLooper());
                this.mSRHandler.obtainMessage(999).sendToTarget();
            }
        }
    }

    private ImproveHookAble getImproveHook(Context context, String curPkgName) {
        try {
            if ("com.tencent.mm".equals(curPkgName)) {
                return new MMImproveHook(context);
            }
            if ("com.ss.android.article.news".equals(curPkgName)) {
                return new NewsImproveHook(context);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            SRReporter.reportFailure(context, "hook error");
        }
    }

    private boolean isAllowInitSync() {
        if (SRUtils.isInAppAccessList(this.mCurPkg) && this.mCurPkg.equals(ActivityThread.currentProcessName()) && SRUtils.isProductAccessSR()) {
            return true;
        }
        return false;
    }

    private boolean isAllowInitAsync(Context context) {
        boolean isAppAccessSR = SRUtils.isAppAccessSR(this.mCurPkg);
        String str = "show_already";
        String str2 = TAG;
        if (isAppAccessSR) {
            context.getSharedPreferences(str2, 0).edit().putBoolean(str, true).apply();
            return true;
        }
        if (context.getSharedPreferences(str2, 0).getBoolean(str, false) && !(SRUtils.isCloundAccessSR(this.mCurPkg) && SRUtils.isLocalAccessSR(this.mCurPkg))) {
            showToast(context);
            context.getSharedPreferences(str2, 0).edit().putBoolean(str, false).apply();
        }
        return false;
    }

    private void showToast(final Context context) {
        getMainHandler().post(new Runnable() {
            public void run() {
                Toast.makeText(context, (int) R.string.super_resolution_maintain_tips, 1).show();
            }
        });
    }

    private boolean drawableHasChanged(ImageView view, ImproveInfo info) {
        if (view.getDrawable() == info.drawable) {
            return false;
        }
        return true;
    }

    private boolean isTimeOutOfLimit(long time) {
        if (SystemClock.uptimeMillis() - time <= 4000) {
            return false;
        }
        SRLog.d("isTimeOutOfLimit", "process time out of limit,limit:4000");
        return true;
    }

    private void initImproveInSRThread() {
        if (SRUtils.isSupportSelfArithmetic()) {
            this.mDrawableImprove = new SelfBitmapImprove();
        } else {
            this.mDrawableImprove = new KingSoftBitmapImprove();
        }
        this.mInitComplete = this.mDrawableImprove.init(this.mContext, 0);
        String str = "init";
        if (this.mInitComplete) {
            this.mBitmapRecord = new HashSet();
            this.mainHandler = getMainHandler();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("-- init successful, Supplier: ");
            stringBuilder.append(this.mDrawableImprove.getClass().getSimpleName());
            SRLog.d(str, stringBuilder.toString());
            return;
        }
        SRLog.e(str, "-- init error!!!");
        SRReporter.reportFailure(this.mContext, "init error");
    }

    private MainHandler getMainHandler() {
        if (this.mainHandler == null) {
            this.mainHandler = new MainHandler(Looper.getMainLooper());
        }
        return this.mainHandler;
    }

    public void improveBitmapIfNeeded(Context context, Drawable drawable, ImageView imageView) {
        if (this.mInitComplete) {
            String clsName = context.getClass().getSimpleName();
            try {
                Bitmap oldBitmap = this.mImproveHook.getBitmap(clsName, drawable, imageView.getClass().getSimpleName());
                if (oldBitmap != null && !isAlreadyImprove(imageView, oldBitmap) && !isSizeOutOfLimit(oldBitmap)) {
                    ImproveInfo info = new ImproveInfo();
                    info.oldBitmap = oldBitmap;
                    info.clsName = clsName;
                    info.drawable = drawable;
                    info.setImageView(imageView);
                    info.startTime = SystemClock.uptimeMillis();
                    this.mSRHandler.removeMessages(998);
                    SuperResolutionHandler superResolutionHandler = this.mSRHandler;
                    superResolutionHandler.sendMessageAtFrontOfQueue(superResolutionHandler.obtainMessage(998, info));
                }
            } catch (Throwable e) {
                e.printStackTrace();
                SRReporter.reportFailure(context, "get bitmap error");
            }
        }
    }

    private boolean isSizeOutOfLimit(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        if (height <= 800 && height >= 100 && width <= 800 && width >= 100) {
            return false;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("bitmap out of limit,current:");
        stringBuilder.append(getBitmapInfo(bitmap));
        SRLog.d("SizeOutOfLimit", stringBuilder.toString());
        return true;
    }

    private boolean isAlreadyImprove(ImageView view, Bitmap bitmap) {
        return this.mBitmapRecord.contains(Integer.valueOf(bitmap.hashCode()));
    }

    private void add2Record(ImageView view, Bitmap bitmap) {
        this.mBitmapRecord.add(Integer.valueOf(bitmap.hashCode()));
    }

    private void checkTime(long startTime, String reason) {
        long endTime = SystemClock.uptimeMillis();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("consuming:");
        stringBuilder.append(endTime - startTime);
        stringBuilder.append(", reason:");
        stringBuilder.append(reason);
        SRLog.d("checkTime", stringBuilder.toString());
    }

    public void release() {
        this.mDrawableImprove.release();
    }

    private String getBitmapInfo(Bitmap bitmap) {
        StringBuffer sb = new StringBuffer();
        sb.append(", bitmap hashCode:");
        sb.append(bitmap.hashCode());
        sb.append(" H:");
        sb.append(bitmap.getHeight());
        sb.append(" W:");
        sb.append(bitmap.getWidth());
        return sb.toString();
    }
}
