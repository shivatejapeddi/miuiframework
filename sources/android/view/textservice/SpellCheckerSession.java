package android.view.textservice;

import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import com.android.internal.textservice.ISpellCheckerSession;
import com.android.internal.textservice.ISpellCheckerSessionListener;
import com.android.internal.textservice.ITextServicesSessionListener;
import com.android.internal.textservice.ITextServicesSessionListener.Stub;
import dalvik.system.CloseGuard;
import java.util.LinkedList;
import java.util.Queue;

public class SpellCheckerSession {
    private static final boolean DBG = false;
    private static final int MSG_ON_GET_SUGGESTION_MULTIPLE = 1;
    private static final int MSG_ON_GET_SUGGESTION_MULTIPLE_FOR_SENTENCE = 2;
    public static final String SERVICE_META_DATA = "android.view.textservice.scs";
    private static final String TAG = SpellCheckerSession.class.getSimpleName();
    private final CloseGuard mGuard = CloseGuard.get();
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i == 1) {
                SpellCheckerSession.this.handleOnGetSuggestionsMultiple((SuggestionsInfo[]) msg.obj);
            } else if (i == 2) {
                SpellCheckerSession.this.handleOnGetSentenceSuggestionsMultiple((SentenceSuggestionsInfo[]) msg.obj);
            }
        }
    };
    private final InternalListener mInternalListener;
    private final SpellCheckerInfo mSpellCheckerInfo;
    @UnsupportedAppUsage
    private final SpellCheckerSessionListener mSpellCheckerSessionListener;
    private final SpellCheckerSessionListenerImpl mSpellCheckerSessionListenerImpl;
    private final TextServicesManager mTextServicesManager;

    private static final class InternalListener extends Stub {
        private final SpellCheckerSessionListenerImpl mParentSpellCheckerSessionListenerImpl;

        public InternalListener(SpellCheckerSessionListenerImpl spellCheckerSessionListenerImpl) {
            this.mParentSpellCheckerSessionListenerImpl = spellCheckerSessionListenerImpl;
        }

        public void onServiceConnected(ISpellCheckerSession session) {
            this.mParentSpellCheckerSessionListenerImpl.onServiceConnected(session);
        }
    }

    public interface SpellCheckerSessionListener {
        void onGetSentenceSuggestions(SentenceSuggestionsInfo[] sentenceSuggestionsInfoArr);

        void onGetSuggestions(SuggestionsInfo[] suggestionsInfoArr);
    }

    private static final class SpellCheckerSessionListenerImpl extends ISpellCheckerSessionListener.Stub {
        private static final int STATE_CLOSED_AFTER_CONNECTION = 2;
        private static final int STATE_CLOSED_BEFORE_CONNECTION = 3;
        private static final int STATE_CONNECTED = 1;
        private static final int STATE_WAIT_CONNECTION = 0;
        private static final int TASK_CANCEL = 1;
        private static final int TASK_CLOSE = 3;
        private static final int TASK_GET_SUGGESTIONS_MULTIPLE = 2;
        private static final int TASK_GET_SUGGESTIONS_MULTIPLE_FOR_SENTENCE = 4;
        private Handler mAsyncHandler;
        private Handler mHandler;
        private ISpellCheckerSession mISpellCheckerSession;
        private final Queue<SpellCheckerParams> mPendingTasks = new LinkedList();
        private int mState = 0;
        private HandlerThread mThread;

        private static class SpellCheckerParams {
            public final boolean mSequentialWords;
            public ISpellCheckerSession mSession;
            public final int mSuggestionsLimit;
            public final TextInfo[] mTextInfos;
            public final int mWhat;

            public SpellCheckerParams(int what, TextInfo[] textInfos, int suggestionsLimit, boolean sequentialWords) {
                this.mWhat = what;
                this.mTextInfos = textInfos;
                this.mSuggestionsLimit = suggestionsLimit;
                this.mSequentialWords = sequentialWords;
            }
        }

        private static String taskToString(int task) {
            if (task == 1) {
                return "TASK_CANCEL";
            }
            if (task == 2) {
                return "TASK_GET_SUGGESTIONS_MULTIPLE";
            }
            if (task == 3) {
                return "TASK_CLOSE";
            }
            if (task == 4) {
                return "TASK_GET_SUGGESTIONS_MULTIPLE_FOR_SENTENCE";
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unexpected task=");
            stringBuilder.append(task);
            return stringBuilder.toString();
        }

        private static String stateToString(int state) {
            if (state == 0) {
                return "STATE_WAIT_CONNECTION";
            }
            if (state == 1) {
                return "STATE_CONNECTED";
            }
            if (state == 2) {
                return "STATE_CLOSED_AFTER_CONNECTION";
            }
            if (state == 3) {
                return "STATE_CLOSED_BEFORE_CONNECTION";
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unexpected state=");
            stringBuilder.append(state);
            return stringBuilder.toString();
        }

        public SpellCheckerSessionListenerImpl(Handler handler) {
            this.mHandler = handler;
        }

        /* JADX WARNING: Removed duplicated region for block: B:38:? A:{SYNTHETIC, RETURN, ORIG_RETURN} */
        /* JADX WARNING: Removed duplicated region for block: B:30:0x00aa  */
        private void processTask(com.android.internal.textservice.ISpellCheckerSession r6, android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.SpellCheckerParams r7, boolean r8) {
            /*
            r5 = this;
            r0 = 3;
            r1 = 1;
            if (r8 != 0) goto L_0x0014;
        L_0x0004:
            r2 = r5.mAsyncHandler;
            if (r2 != 0) goto L_0x0009;
        L_0x0008:
            goto L_0x0014;
        L_0x0009:
            r7.mSession = r6;
            r1 = android.os.Message.obtain(r2, r1, r7);
            r2.sendMessage(r1);
            goto L_0x00a6;
        L_0x0014:
            r2 = r7.mWhat;
            if (r2 == r1) goto L_0x0087;
        L_0x0018:
            r1 = 2;
            if (r2 == r1) goto L_0x0063;
        L_0x001b:
            if (r2 == r0) goto L_0x0045;
        L_0x001d:
            r1 = 4;
            if (r2 == r1) goto L_0x0022;
        L_0x0020:
            goto L_0x00a5;
        L_0x0022:
            r1 = r7.mTextInfos;	 Catch:{ RemoteException -> 0x002b }
            r2 = r7.mSuggestionsLimit;	 Catch:{ RemoteException -> 0x002b }
            r6.onGetSentenceSuggestionsMultiple(r1, r2);	 Catch:{ RemoteException -> 0x002b }
            goto L_0x00a5;
        L_0x002b:
            r1 = move-exception;
            r2 = android.view.textservice.SpellCheckerSession.TAG;
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r4 = "Failed to get suggestions ";
            r3.append(r4);
            r3.append(r1);
            r3 = r3.toString();
            android.util.Log.e(r2, r3);
            goto L_0x00a5;
        L_0x0045:
            r6.onClose();	 Catch:{ RemoteException -> 0x0049 }
            goto L_0x00a5;
        L_0x0049:
            r1 = move-exception;
            r2 = android.view.textservice.SpellCheckerSession.TAG;
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r4 = "Failed to close ";
            r3.append(r4);
            r3.append(r1);
            r3 = r3.toString();
            android.util.Log.e(r2, r3);
            goto L_0x00a5;
        L_0x0063:
            r1 = r7.mTextInfos;	 Catch:{ RemoteException -> 0x006d }
            r2 = r7.mSuggestionsLimit;	 Catch:{ RemoteException -> 0x006d }
            r3 = r7.mSequentialWords;	 Catch:{ RemoteException -> 0x006d }
            r6.onGetSuggestionsMultiple(r1, r2, r3);	 Catch:{ RemoteException -> 0x006d }
            goto L_0x00a5;
        L_0x006d:
            r1 = move-exception;
            r2 = android.view.textservice.SpellCheckerSession.TAG;
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r4 = "Failed to get suggestions ";
            r3.append(r4);
            r3.append(r1);
            r3 = r3.toString();
            android.util.Log.e(r2, r3);
            goto L_0x00a5;
        L_0x0087:
            r6.onCancel();	 Catch:{ RemoteException -> 0x008b }
            goto L_0x00a5;
        L_0x008b:
            r1 = move-exception;
            r2 = android.view.textservice.SpellCheckerSession.TAG;
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r4 = "Failed to cancel ";
            r3.append(r4);
            r3.append(r1);
            r3 = r3.toString();
            android.util.Log.e(r2, r3);
        L_0x00a6:
            r1 = r7.mWhat;
            if (r1 != r0) goto L_0x00b3;
        L_0x00aa:
            monitor-enter(r5);
            r5.processCloseLocked();	 Catch:{ all -> 0x00b0 }
            monitor-exit(r5);	 Catch:{ all -> 0x00b0 }
            goto L_0x00b3;
        L_0x00b0:
            r0 = move-exception;
            monitor-exit(r5);	 Catch:{ all -> 0x00b0 }
            throw r0;
        L_0x00b3:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.view.textservice.SpellCheckerSession$SpellCheckerSessionListenerImpl.processTask(com.android.internal.textservice.ISpellCheckerSession, android.view.textservice.SpellCheckerSession$SpellCheckerSessionListenerImpl$SpellCheckerParams, boolean):void");
        }

        private void processCloseLocked() {
            this.mISpellCheckerSession = null;
            HandlerThread handlerThread = this.mThread;
            if (handlerThread != null) {
                handlerThread.quit();
            }
            this.mHandler = null;
            this.mPendingTasks.clear();
            this.mThread = null;
            this.mAsyncHandler = null;
            int i = this.mState;
            if (i == 0) {
                this.mState = 3;
            } else if (i != 1) {
                String access$200 = SpellCheckerSession.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("processCloseLocked is called unexpectedly. mState=");
                stringBuilder.append(stateToString(this.mState));
                Log.e(access$200, stringBuilder.toString());
            } else {
                this.mState = 2;
            }
        }

        public void onServiceConnected(ISpellCheckerSession session) {
            synchronized (this) {
                int i = this.mState;
                if (i != 0) {
                    if (i != 3) {
                        String access$200 = SpellCheckerSession.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("ignoring onServiceConnected due to unexpected mState=");
                        stringBuilder.append(stateToString(this.mState));
                        Log.e(access$200, stringBuilder.toString());
                        return;
                    }
                } else if (session == null) {
                    Log.e(SpellCheckerSession.TAG, "ignoring onServiceConnected due to session=null");
                } else {
                    this.mISpellCheckerSession = session;
                    if ((session.asBinder() instanceof Binder) && this.mThread == null) {
                        this.mThread = new HandlerThread("SpellCheckerSession", 10);
                        this.mThread.start();
                        this.mAsyncHandler = new Handler(this.mThread.getLooper()) {
                            public void handleMessage(Message msg) {
                                SpellCheckerParams scp = msg.obj;
                                SpellCheckerSessionListenerImpl.this.processTask(scp.mSession, scp, true);
                            }
                        };
                    }
                    this.mState = 1;
                    while (!this.mPendingTasks.isEmpty()) {
                        processTask(session, (SpellCheckerParams) this.mPendingTasks.poll(), false);
                    }
                }
            }
        }

        public void cancel() {
            processOrEnqueueTask(new SpellCheckerParams(1, null, 0, false));
        }

        public void getSuggestionsMultiple(TextInfo[] textInfos, int suggestionsLimit, boolean sequentialWords) {
            processOrEnqueueTask(new SpellCheckerParams(2, textInfos, suggestionsLimit, sequentialWords));
        }

        public void getSentenceSuggestionsMultiple(TextInfo[] textInfos, int suggestionsLimit) {
            processOrEnqueueTask(new SpellCheckerParams(4, textInfos, suggestionsLimit, false));
        }

        public void close() {
            processOrEnqueueTask(new SpellCheckerParams(3, null, 0, false));
        }

        public boolean isDisconnected() {
            boolean z;
            synchronized (this) {
                z = true;
                if (this.mState == 1) {
                    z = false;
                }
            }
            return z;
        }

        /* JADX WARNING: Missing block: B:9:0x0010, code skipped:
            return;
     */
        /* JADX WARNING: Missing block: B:36:0x007d, code skipped:
            return;
     */
        private void processOrEnqueueTask(android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.SpellCheckerParams r5) {
            /*
            r4 = this;
            monitor-enter(r4);
            r0 = r5.mWhat;	 Catch:{ all -> 0x0086 }
            r1 = 3;
            if (r0 != r1) goto L_0x0011;
        L_0x0006:
            r0 = r4.mState;	 Catch:{ all -> 0x0086 }
            r2 = 2;
            if (r0 == r2) goto L_0x000f;
        L_0x000b:
            r0 = r4.mState;	 Catch:{ all -> 0x0086 }
            if (r0 != r1) goto L_0x0011;
        L_0x000f:
            monitor-exit(r4);	 Catch:{ all -> 0x0086 }
            return;
        L_0x0011:
            r0 = r4.mState;	 Catch:{ all -> 0x0086 }
            r2 = 1;
            if (r0 == 0) goto L_0x0048;
        L_0x0016:
            r0 = r4.mState;	 Catch:{ all -> 0x0086 }
            if (r0 == r2) goto L_0x0048;
        L_0x001a:
            r0 = android.view.textservice.SpellCheckerSession.TAG;	 Catch:{ all -> 0x0086 }
            r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0086 }
            r1.<init>();	 Catch:{ all -> 0x0086 }
            r2 = "ignoring processOrEnqueueTask due to unexpected mState=";
            r1.append(r2);	 Catch:{ all -> 0x0086 }
            r2 = r4.mState;	 Catch:{ all -> 0x0086 }
            r2 = stateToString(r2);	 Catch:{ all -> 0x0086 }
            r1.append(r2);	 Catch:{ all -> 0x0086 }
            r2 = " scp.mWhat=";
            r1.append(r2);	 Catch:{ all -> 0x0086 }
            r2 = r5.mWhat;	 Catch:{ all -> 0x0086 }
            r2 = taskToString(r2);	 Catch:{ all -> 0x0086 }
            r1.append(r2);	 Catch:{ all -> 0x0086 }
            r1 = r1.toString();	 Catch:{ all -> 0x0086 }
            android.util.Log.e(r0, r1);	 Catch:{ all -> 0x0086 }
            monitor-exit(r4);	 Catch:{ all -> 0x0086 }
            return;
        L_0x0048:
            r0 = r4.mState;	 Catch:{ all -> 0x0086 }
            if (r0 != 0) goto L_0x007e;
        L_0x004c:
            r0 = r5.mWhat;	 Catch:{ all -> 0x0086 }
            if (r0 != r1) goto L_0x0055;
        L_0x0050:
            r4.processCloseLocked();	 Catch:{ all -> 0x0086 }
            monitor-exit(r4);	 Catch:{ all -> 0x0086 }
            return;
        L_0x0055:
            r0 = 0;
            r3 = r5.mWhat;	 Catch:{ all -> 0x0086 }
            if (r3 != r2) goto L_0x0070;
        L_0x005a:
            r2 = r4.mPendingTasks;	 Catch:{ all -> 0x0086 }
            r2 = r2.isEmpty();	 Catch:{ all -> 0x0086 }
            if (r2 != 0) goto L_0x0070;
        L_0x0062:
            r2 = r4.mPendingTasks;	 Catch:{ all -> 0x0086 }
            r2 = r2.poll();	 Catch:{ all -> 0x0086 }
            r2 = (android.view.textservice.SpellCheckerSession.SpellCheckerSessionListenerImpl.SpellCheckerParams) r2;	 Catch:{ all -> 0x0086 }
            r3 = r2.mWhat;	 Catch:{ all -> 0x0086 }
            if (r3 != r1) goto L_0x006f;
        L_0x006e:
            r0 = r2;
        L_0x006f:
            goto L_0x005a;
        L_0x0070:
            r1 = r4.mPendingTasks;	 Catch:{ all -> 0x0086 }
            r1.offer(r5);	 Catch:{ all -> 0x0086 }
            if (r0 == 0) goto L_0x007c;
        L_0x0077:
            r1 = r4.mPendingTasks;	 Catch:{ all -> 0x0086 }
            r1.offer(r0);	 Catch:{ all -> 0x0086 }
        L_0x007c:
            monitor-exit(r4);	 Catch:{ all -> 0x0086 }
            return;
        L_0x007e:
            r0 = r4.mISpellCheckerSession;	 Catch:{ all -> 0x0086 }
            monitor-exit(r4);	 Catch:{ all -> 0x0086 }
            r1 = 0;
            r4.processTask(r0, r5, r1);
            return;
        L_0x0086:
            r0 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x0086 }
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.view.textservice.SpellCheckerSession$SpellCheckerSessionListenerImpl.processOrEnqueueTask(android.view.textservice.SpellCheckerSession$SpellCheckerSessionListenerImpl$SpellCheckerParams):void");
        }

        public void onGetSuggestions(SuggestionsInfo[] results) {
            synchronized (this) {
                if (this.mHandler != null) {
                    this.mHandler.sendMessage(Message.obtain(this.mHandler, 1, results));
                }
            }
        }

        public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] results) {
            synchronized (this) {
                if (this.mHandler != null) {
                    this.mHandler.sendMessage(Message.obtain(this.mHandler, 2, results));
                }
            }
        }
    }

    public SpellCheckerSession(SpellCheckerInfo info, TextServicesManager tsm, SpellCheckerSessionListener listener) {
        if (info == null || listener == null || tsm == null) {
            throw new NullPointerException();
        }
        this.mSpellCheckerInfo = info;
        this.mSpellCheckerSessionListenerImpl = new SpellCheckerSessionListenerImpl(this.mHandler);
        this.mInternalListener = new InternalListener(this.mSpellCheckerSessionListenerImpl);
        this.mTextServicesManager = tsm;
        this.mSpellCheckerSessionListener = listener;
        this.mGuard.open("finishSession");
    }

    public boolean isSessionDisconnected() {
        return this.mSpellCheckerSessionListenerImpl.isDisconnected();
    }

    public SpellCheckerInfo getSpellChecker() {
        return this.mSpellCheckerInfo;
    }

    public void cancel() {
        this.mSpellCheckerSessionListenerImpl.cancel();
    }

    public void close() {
        this.mGuard.close();
        this.mSpellCheckerSessionListenerImpl.close();
        this.mTextServicesManager.finishSpellCheckerService(this.mSpellCheckerSessionListenerImpl);
    }

    public void getSentenceSuggestions(TextInfo[] textInfos, int suggestionsLimit) {
        this.mSpellCheckerSessionListenerImpl.getSentenceSuggestionsMultiple(textInfos, suggestionsLimit);
    }

    @Deprecated
    public void getSuggestions(TextInfo textInfo, int suggestionsLimit) {
        getSuggestions(new TextInfo[]{textInfo}, suggestionsLimit, false);
    }

    @Deprecated
    public void getSuggestions(TextInfo[] textInfos, int suggestionsLimit, boolean sequentialWords) {
        this.mSpellCheckerSessionListenerImpl.getSuggestionsMultiple(textInfos, suggestionsLimit, sequentialWords);
    }

    private void handleOnGetSuggestionsMultiple(SuggestionsInfo[] suggestionInfos) {
        this.mSpellCheckerSessionListener.onGetSuggestions(suggestionInfos);
    }

    private void handleOnGetSentenceSuggestionsMultiple(SentenceSuggestionsInfo[] suggestionInfos) {
        this.mSpellCheckerSessionListener.onGetSentenceSuggestions(suggestionInfos);
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            if (this.mGuard != null) {
                this.mGuard.warnIfOpen();
                close();
            }
            super.finalize();
        } catch (Throwable th) {
            super.finalize();
        }
    }

    public ITextServicesSessionListener getTextServicesSessionListener() {
        return this.mInternalListener;
    }

    public ISpellCheckerSessionListener getSpellCheckerSessionListener() {
        return this.mSpellCheckerSessionListenerImpl;
    }
}
