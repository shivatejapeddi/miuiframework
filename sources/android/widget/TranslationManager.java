package android.widget;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.miui.translationservice.ITranslation;
import com.miui.translationservice.ITranslation.Stub;
import com.miui.translationservice.ITranslationRemoteCallback;
import com.miui.translationservice.provider.TranslationResult;
import miui.os.Build;

class TranslationManager {
    static final int MSG_QUERY_FAIL = 1;
    static final int MSG_QUERY_SUCCESS = 0;
    private static final int STATE_DESTROYING = 3;
    private static final int STATE_ERROR = -1;
    private static final int STATE_INIT = 0;
    private static final int STATE_INITIALIZING = 1;
    private static final int STATE_TRANSLATING = 2;
    private static final String TAG = "TranslationManager";
    private static final String TRANSLATION_SERVICE_CLASS = "com.miui.translationservice.TranslationService";
    private static final String TRANSLATION_SERVICE_PACKAGE = "com.miui.translationservice";
    private ServiceConnection mConnection;
    private Context mContext;
    private Handler mHandler;
    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    private TranslateTask mPendingTask;
    private ITranslation mService = null;
    private int mState = 0;

    private static class TranslateTask {
        String mSource;
        String mTarget;
        String mWord;

        TranslateTask(String source, String target, String word) {
            this.mSource = source;
            this.mTarget = target;
            this.mWord = word;
        }

        static boolean equals(TranslateTask task1, TranslateTask task2) {
            boolean z = true;
            if (task1 == null || task2 == null) {
                return task1 == null && task2 == null;
            } else {
                if (!(TextUtils.equals(task1.mSource, task2.mSource) && TextUtils.equals(task1.mTarget, task2.mTarget) && TextUtils.equals(task1.mWord, task2.mWord))) {
                    z = false;
                }
                return z;
            }
        }
    }

    private class TranslationConnection implements ServiceConnection {
        private TranslationConnection() {
        }

        /* synthetic */ TranslationConnection(TranslationManager x0, AnonymousClass1 x1) {
            this();
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TranslationManager.TAG, "service connected");
            TranslationManager.this.onInitialized(Stub.asInterface(service));
        }

        public void onServiceDisconnected(ComponentName name) {
            Log.d(TranslationManager.TAG, "service disconnected");
            TranslationManager.this.onDisconnected();
        }
    }

    private class TranslationRemoteCallback extends ITranslationRemoteCallback.Stub {
        TranslateTask mTask;

        public TranslationRemoteCallback(TranslateTask task) {
            this.mTask = task;
        }

        public void onTranslationFinished(final TranslationResult result) {
            Log.i(TranslationManager.TAG, "translate finish");
            TranslationManager.this.mMainHandler.post(new Runnable() {
                public void run() {
                    TranslationManager.this.onTranslateDone(TranslationRemoteCallback.this.mTask, result);
                }
            });
        }
    }

    TranslationManager(Context context, Handler handler) {
        this.mContext = context.getApplicationContext();
        this.mHandler = handler;
    }

    /* Access modifiers changed, original: 0000 */
    public void translate(String source, String target, String word) {
        this.mPendingTask = new TranslateTask(source, target, word);
        if (this.mState < 1) {
            doInitialize();
        }
    }

    private void doInitialize() {
        Log.d(TAG, "try to bind translation service");
        this.mState = 1;
        if (this.mConnection == null) {
            this.mConnection = new TranslationConnection(this, null);
        }
        new AsyncTask<Void, Void, Boolean>() {
            /* Access modifiers changed, original: protected|varargs */
            public Boolean doInBackground(Void... params) {
                return Boolean.valueOf(TranslationManager.this.mContext.bindService(new Intent().setClassName(TranslationManager.TRANSLATION_SERVICE_PACKAGE, TranslationManager.TRANSLATION_SERVICE_CLASS), TranslationManager.this.mConnection, 1));
            }

            /* Access modifiers changed, original: protected */
            public void onPostExecute(Boolean success) {
                if (success == null || !success.booleanValue()) {
                    TranslationManager.this.onInitialized(null);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    private void doDestroy() {
        this.mState = 3;
        new AsyncTask<Void, Void, Void>() {
            /* Access modifiers changed, original: protected|varargs */
            public Void doInBackground(Void... params) {
                TranslationManager.this.mContext.unbindService(TranslationManager.this.mConnection);
                return null;
            }

            /* Access modifiers changed, original: protected */
            public void onPostExecute(Void aVoid) {
                TranslationManager.this.onDestroyed();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    private void doTranslate() {
        this.mState = 2;
        try {
            this.mService.translate(this.mPendingTask.mSource, this.mPendingTask.mTarget, this.mPendingTask.mWord, new TranslationRemoteCallback(this.mPendingTask));
        } catch (RemoteException e) {
            Log.i(TAG, "bind translation service failed", e);
            onTranslateDone(this.mPendingTask, null);
        }
    }

    private void onInitialized(ITranslation service) {
        String str = TAG;
        if (service != null) {
            this.mService = service;
            if (this.mPendingTask != null) {
                Log.d(str, "translate pending task");
                doTranslate();
                return;
            }
            Log.d(str, "no pending task, unbind service directly");
            doDestroy();
            return;
        }
        Log.i(str, "bind service failed");
        this.mConnection = null;
        this.mState = -1;
        if (this.mPendingTask != null) {
            this.mPendingTask = null;
            deliverResult(null);
        }
    }

    private void onTranslateDone(TranslateTask task, TranslationResult result) {
        boolean equals = TranslateTask.equals(this.mPendingTask, task);
        String str = TAG;
        if (equals) {
            Log.d(str, "translate task done");
            deliverResult(result);
            this.mPendingTask = null;
        }
        if (this.mState != 2) {
            return;
        }
        if (this.mPendingTask == null) {
            Log.d(str, "no pending task found. release service");
            doDestroy();
            return;
        }
        Log.d(str, "task changed");
        doTranslate();
    }

    private void onDestroyed() {
        this.mService = null;
        this.mConnection = null;
        this.mState = 0;
        if (this.mPendingTask != null) {
            Log.d(TAG, "new task received when destroying");
            doInitialize();
        }
    }

    private void onDisconnected() {
        if (this.mState == 2) {
            Log.d(TAG, "disconnected during translating");
            this.mService = null;
            deliverResult(null);
            this.mPendingTask = null;
            doDestroy();
        }
    }

    private void deliverResult(TranslationResult result) {
        Message msg;
        this.mHandler.removeCallbacksAndMessages(null);
        if (result == null) {
            msg = this.mHandler.obtainMessage(1);
        } else {
            msg = this.mHandler.obtainMessage(0, result);
        }
        this.mHandler.sendMessageDelayed(msg, 200);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isAvailable() {
        return Build.IS_INTERNATIONAL_BUILD ^ 1;
    }
}
