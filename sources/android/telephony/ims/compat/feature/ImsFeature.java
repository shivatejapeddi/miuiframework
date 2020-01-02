package android.telephony.ims.compat.feature;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Log;
import com.android.ims.internal.IImsFeatureStatusCallback;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

public abstract class ImsFeature {
    public static final int EMERGENCY_MMTEL = 0;
    public static final int INVALID = -1;
    private static final String LOG_TAG = "ImsFeature";
    public static final int MAX = 3;
    public static final int MMTEL = 1;
    public static final int RCS = 2;
    public static final int STATE_INITIALIZING = 1;
    public static final int STATE_NOT_AVAILABLE = 0;
    public static final int STATE_READY = 2;
    protected Context mContext;
    private int mSlotId = -1;
    private int mState = 0;
    private final Set<IImsFeatureStatusCallback> mStatusCallbacks = Collections.newSetFromMap(new WeakHashMap());

    @Retention(RetentionPolicy.SOURCE)
    public @interface ImsState {
    }

    public abstract IInterface getBinder();

    public abstract void onFeatureReady();

    public abstract void onFeatureRemoved();

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void setSlotId(int slotId) {
        this.mSlotId = slotId;
    }

    @UnsupportedAppUsage
    public int getFeatureState() {
        return this.mState;
    }

    /* Access modifiers changed, original: protected|final */
    @UnsupportedAppUsage
    public final void setFeatureState(int state) {
        if (this.mState != state) {
            this.mState = state;
            notifyFeatureState(state);
        }
    }

    public void addImsFeatureStatusCallback(IImsFeatureStatusCallback c) {
        if (c != null) {
            try {
                c.notifyImsFeatureStatus(this.mState);
                synchronized (this.mStatusCallbacks) {
                    this.mStatusCallbacks.add(c);
                }
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Couldn't notify feature state: ");
                stringBuilder.append(e.getMessage());
                Log.w(LOG_TAG, stringBuilder.toString());
            }
        }
    }

    public void removeImsFeatureStatusCallback(IImsFeatureStatusCallback c) {
        if (c != null) {
            synchronized (this.mStatusCallbacks) {
                this.mStatusCallbacks.remove(c);
            }
        }
    }

    private void notifyFeatureState(int state) {
        synchronized (this.mStatusCallbacks) {
            Iterator<IImsFeatureStatusCallback> iter = this.mStatusCallbacks.iterator();
            while (iter.hasNext()) {
                IImsFeatureStatusCallback callback = (IImsFeatureStatusCallback) iter.next();
                try {
                    String str = LOG_TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("notifying ImsFeatureState=");
                    stringBuilder.append(state);
                    Log.i(str, stringBuilder.toString());
                    callback.notifyImsFeatureStatus(state);
                } catch (RemoteException e) {
                    iter.remove();
                    String str2 = LOG_TAG;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Couldn't notify feature state: ");
                    stringBuilder2.append(e.getMessage());
                    Log.w(str2, stringBuilder2.toString());
                }
            }
        }
    }
}
