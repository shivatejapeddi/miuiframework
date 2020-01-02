package android.media.soundtrigger;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent;
import android.hardware.soundtrigger.SoundTrigger.RecognitionEvent;
import android.media.soundtrigger.ISoundTriggerDetectionService.Stub;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.util.ArrayMap;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.function.pooled.PooledLambda;
import java.util.UUID;

@SystemApi
public abstract class SoundTriggerDetectionService extends Service {
    private static final boolean DEBUG = false;
    private static final String LOG_TAG = SoundTriggerDetectionService.class.getSimpleName();
    @GuardedBy({"mLock"})
    private final ArrayMap<UUID, ISoundTriggerDetectionServiceClient> mClients = new ArrayMap();
    private Handler mHandler;
    private final Object mLock = new Object();

    public abstract void onStopOperation(UUID uuid, Bundle bundle, int i);

    /* Access modifiers changed, original: protected|final */
    public final void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        this.mHandler = new Handler(base.getMainLooper());
    }

    private void setClient(UUID uuid, Bundle params, ISoundTriggerDetectionServiceClient client) {
        synchronized (this.mLock) {
            this.mClients.put(uuid, client);
        }
        onConnected(uuid, params);
    }

    private void removeClient(UUID uuid, Bundle params) {
        synchronized (this.mLock) {
            this.mClients.remove(uuid);
        }
        onDisconnected(uuid, params);
    }

    public void onConnected(UUID uuid, Bundle params) {
    }

    public void onDisconnected(UUID uuid, Bundle params) {
    }

    public void onGenericRecognitionEvent(UUID uuid, Bundle params, int opId, RecognitionEvent event) {
        operationFinished(uuid, opId);
    }

    public void onError(UUID uuid, Bundle params, int opId, int status) {
        operationFinished(uuid, opId);
    }

    /* JADX WARNING: Missing block: B:11:?, code skipped:
            r1.onOpFinished(r7);
     */
    public final void operationFinished(java.util.UUID r6, int r7) {
        /*
        r5 = this;
        r0 = r5.mLock;	 Catch:{ RemoteException -> 0x0033 }
        monitor-enter(r0);	 Catch:{ RemoteException -> 0x0033 }
        r1 = r5.mClients;	 Catch:{ all -> 0x0030 }
        r1 = r1.get(r6);	 Catch:{ all -> 0x0030 }
        r1 = (android.media.soundtrigger.ISoundTriggerDetectionServiceClient) r1;	 Catch:{ all -> 0x0030 }
        if (r1 != 0) goto L_0x002b;
    L_0x000d:
        r2 = LOG_TAG;	 Catch:{ all -> 0x0030 }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0030 }
        r3.<init>();	 Catch:{ all -> 0x0030 }
        r4 = "operationFinished called, but no client for ";
        r3.append(r4);	 Catch:{ all -> 0x0030 }
        r3.append(r6);	 Catch:{ all -> 0x0030 }
        r4 = ". Was this called after onDisconnected?";
        r3.append(r4);	 Catch:{ all -> 0x0030 }
        r3 = r3.toString();	 Catch:{ all -> 0x0030 }
        android.util.Log.w(r2, r3);	 Catch:{ all -> 0x0030 }
        monitor-exit(r0);	 Catch:{ all -> 0x0030 }
        return;
    L_0x002b:
        monitor-exit(r0);	 Catch:{ all -> 0x0030 }
        r1.onOpFinished(r7);	 Catch:{ RemoteException -> 0x0033 }
        goto L_0x004b;
    L_0x0030:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0030 }
        throw r1;	 Catch:{ RemoteException -> 0x0033 }
    L_0x0033:
        r0 = move-exception;
        r1 = LOG_TAG;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "operationFinished, remote exception for client ";
        r2.append(r3);
        r2.append(r6);
        r2 = r2.toString();
        android.util.Log.e(r1, r2, r0);
    L_0x004b:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.soundtrigger.SoundTriggerDetectionService.operationFinished(java.util.UUID, int):void");
    }

    public final IBinder onBind(Intent intent) {
        return new Stub() {
            private final Object mBinderLock = new Object();
            @GuardedBy({"mBinderLock"})
            public final ArrayMap<UUID, Bundle> mParams = new ArrayMap();

            public void setClient(ParcelUuid puuid, Bundle params, ISoundTriggerDetectionServiceClient client) {
                UUID uuid = puuid.getUuid();
                synchronized (this.mBinderLock) {
                    this.mParams.put(uuid, params);
                }
                SoundTriggerDetectionService.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$SoundTriggerDetectionService$1$LlOo7TiZplZCgGhS07DqYHocFcw.INSTANCE, SoundTriggerDetectionService.this, uuid, params, client));
            }

            public void removeClient(ParcelUuid puuid) {
                Bundle params;
                UUID uuid = puuid.getUuid();
                synchronized (this.mBinderLock) {
                    params = (Bundle) this.mParams.remove(uuid);
                }
                SoundTriggerDetectionService.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$SoundTriggerDetectionService$1$pKR4r0FzOzoVczcnvLQIZNjkZZw.INSTANCE, SoundTriggerDetectionService.this, uuid, params));
            }

            public void onGenericRecognitionEvent(ParcelUuid puuid, int opId, GenericRecognitionEvent event) {
                Bundle params;
                UUID uuid = puuid.getUuid();
                synchronized (this.mBinderLock) {
                    params = (Bundle) this.mParams.get(uuid);
                }
                SoundTriggerDetectionService.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$ISQYIYPBRBIOLBUJy7rrJW-SiJg.INSTANCE, SoundTriggerDetectionService.this, uuid, params, Integer.valueOf(opId), event));
            }

            public void onError(ParcelUuid puuid, int opId, int status) {
                Bundle params;
                UUID uuid = puuid.getUuid();
                synchronized (this.mBinderLock) {
                    params = (Bundle) this.mParams.get(uuid);
                }
                SoundTriggerDetectionService.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$oNgT3sYhSGVWlnU92bECo_ULGeY.INSTANCE, SoundTriggerDetectionService.this, uuid, params, Integer.valueOf(opId), Integer.valueOf(status)));
            }

            public void onStopOperation(ParcelUuid puuid, int opId) {
                Bundle params;
                UUID uuid = puuid.getUuid();
                synchronized (this.mBinderLock) {
                    params = (Bundle) this.mParams.get(uuid);
                }
                SoundTriggerDetectionService.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$bPGNpvkCtpPW14oaI3pxn1e6JtQ.INSTANCE, SoundTriggerDetectionService.this, uuid, params, Integer.valueOf(opId)));
            }
        };
    }

    public boolean onUnbind(Intent intent) {
        this.mClients.clear();
        return false;
    }
}
