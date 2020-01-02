package android.app.servertransaction;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityThread.ActivityClientRecord;
import android.app.ClientTransactionHandler;
import android.app.ProfilerInfo;
import android.app.ResultInfo;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.PersistableBundle;
import android.os.Trace;
import com.android.internal.app.IVoiceInteractor;
import com.android.internal.app.IVoiceInteractor.Stub;
import com.android.internal.content.ReferrerIntent;
import java.util.List;
import java.util.Objects;

public class LaunchActivityItem extends ClientTransactionItem {
    public static final Creator<LaunchActivityItem> CREATOR = new Creator<LaunchActivityItem>() {
        public LaunchActivityItem createFromParcel(Parcel in) {
            return new LaunchActivityItem(in, null);
        }

        public LaunchActivityItem[] newArray(int size) {
            return new LaunchActivityItem[size];
        }
    };
    private IBinder mAssistToken;
    private CompatibilityInfo mCompatInfo;
    private Configuration mCurConfig;
    private int mIdent;
    @UnsupportedAppUsage
    private ActivityInfo mInfo;
    @UnsupportedAppUsage
    private Intent mIntent;
    private boolean mIsForward;
    private Configuration mOverrideConfig;
    private List<ReferrerIntent> mPendingNewIntents;
    private List<ResultInfo> mPendingResults;
    private PersistableBundle mPersistentState;
    private int mProcState;
    private ProfilerInfo mProfilerInfo;
    private String mReferrer;
    private Bundle mState;
    private IVoiceInteractor mVoiceInteractor;

    /* synthetic */ LaunchActivityItem(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public void preExecute(ClientTransactionHandler client, IBinder token) {
        client.countLaunchingActivities(1);
        client.updateProcessState(this.mProcState, false);
        client.updatePendingConfiguration(this.mCurConfig);
    }

    public void execute(ClientTransactionHandler client, IBinder token, PendingTransactionActions pendingActions) {
        ClientTransactionHandler clientTransactionHandler = client;
        IBinder iBinder = token;
        Trace.traceBegin(64, "activityStart");
        client.handleLaunchActivity(new ActivityClientRecord(iBinder, this.mIntent, this.mIdent, this.mInfo, this.mOverrideConfig, this.mCompatInfo, this.mReferrer, this.mVoiceInteractor, this.mState, this.mPersistentState, this.mPendingResults, this.mPendingNewIntents, this.mIsForward, this.mProfilerInfo, clientTransactionHandler, this.mAssistToken), pendingActions, null);
        Trace.traceEnd(64);
    }

    public void postExecute(ClientTransactionHandler client, IBinder token, PendingTransactionActions pendingActions) {
        client.countLaunchingActivities(-1);
    }

    private LaunchActivityItem() {
    }

    public static LaunchActivityItem obtain(Intent intent, int ident, ActivityInfo info, Configuration curConfig, Configuration overrideConfig, CompatibilityInfo compatInfo, String referrer, IVoiceInteractor voiceInteractor, int procState, Bundle state, PersistableBundle persistentState, List<ResultInfo> pendingResults, List<ReferrerIntent> pendingNewIntents, boolean isForward, ProfilerInfo profilerInfo, IBinder assistToken) {
        LaunchActivityItem instance = (LaunchActivityItem) ObjectPool.obtain(LaunchActivityItem.class);
        if (instance == null) {
            instance = new LaunchActivityItem();
        }
        setValues(instance, intent, ident, info, curConfig, overrideConfig, compatInfo, referrer, voiceInteractor, procState, state, persistentState, pendingResults, pendingNewIntents, isForward, profilerInfo, assistToken);
        return instance;
    }

    public void recycle() {
        setValues(this, null, 0, null, null, null, null, null, null, 0, null, null, null, null, false, null, null);
        ObjectPool.recycle(this);
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedObject(this.mIntent, flags);
        dest.writeInt(this.mIdent);
        dest.writeTypedObject(this.mInfo, flags);
        dest.writeTypedObject(this.mCurConfig, flags);
        dest.writeTypedObject(this.mOverrideConfig, flags);
        dest.writeTypedObject(this.mCompatInfo, flags);
        dest.writeString(this.mReferrer);
        dest.writeStrongInterface(this.mVoiceInteractor);
        dest.writeInt(this.mProcState);
        dest.writeBundle(this.mState);
        dest.writePersistableBundle(this.mPersistentState);
        dest.writeTypedList(this.mPendingResults, flags);
        dest.writeTypedList(this.mPendingNewIntents, flags);
        dest.writeBoolean(this.mIsForward);
        dest.writeTypedObject(this.mProfilerInfo, flags);
        dest.writeStrongBinder(this.mAssistToken);
    }

    private LaunchActivityItem(Parcel in) {
        Parcel parcel = in;
        setValues(this, (Intent) parcel.readTypedObject(Intent.CREATOR), in.readInt(), (ActivityInfo) parcel.readTypedObject(ActivityInfo.CREATOR), (Configuration) parcel.readTypedObject(Configuration.CREATOR), (Configuration) parcel.readTypedObject(Configuration.CREATOR), (CompatibilityInfo) parcel.readTypedObject(CompatibilityInfo.CREATOR), in.readString(), Stub.asInterface(in.readStrongBinder()), in.readInt(), parcel.readBundle(getClass().getClassLoader()), parcel.readPersistableBundle(getClass().getClassLoader()), parcel.createTypedArrayList(ResultInfo.CREATOR), parcel.createTypedArrayList(ReferrerIntent.CREATOR), in.readBoolean(), (ProfilerInfo) parcel.readTypedObject(ProfilerInfo.CREATOR), in.readStrongBinder());
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        boolean intentsEqual;
        LaunchActivityItem other = (LaunchActivityItem) o;
        if (!(this.mIntent == null && other.mIntent == null)) {
            Intent intent = this.mIntent;
            if (intent == null || !intent.filterEquals(other.mIntent)) {
                intentsEqual = false;
                if (!(intentsEqual && this.mIdent == other.mIdent && activityInfoEqual(other.mInfo) && Objects.equals(this.mCurConfig, other.mCurConfig) && Objects.equals(this.mOverrideConfig, other.mOverrideConfig) && Objects.equals(this.mCompatInfo, other.mCompatInfo) && Objects.equals(this.mReferrer, other.mReferrer) && this.mProcState == other.mProcState && areBundlesEqual(this.mState, other.mState) && areBundlesEqual(this.mPersistentState, other.mPersistentState) && Objects.equals(this.mPendingResults, other.mPendingResults) && Objects.equals(this.mPendingNewIntents, other.mPendingNewIntents) && this.mIsForward == other.mIsForward && Objects.equals(this.mProfilerInfo, other.mProfilerInfo) && Objects.equals(this.mAssistToken, other.mAssistToken))) {
                    z = false;
                }
                return z;
            }
        }
        intentsEqual = true;
        z = false;
        return z;
    }

    public int hashCode() {
        int filterHashCode = ((((((((((((((17 * 31) + this.mIntent.filterHashCode()) * 31) + this.mIdent) * 31) + Objects.hashCode(this.mCurConfig)) * 31) + Objects.hashCode(this.mOverrideConfig)) * 31) + Objects.hashCode(this.mCompatInfo)) * 31) + Objects.hashCode(this.mReferrer)) * 31) + Objects.hashCode(Integer.valueOf(this.mProcState))) * 31;
        Bundle bundle = this.mState;
        int i = 0;
        int size = (filterHashCode + (bundle != null ? bundle.size() : 0)) * 31;
        PersistableBundle persistableBundle = this.mPersistentState;
        if (persistableBundle != null) {
            i = persistableBundle.size();
        }
        return ((((((((((size + i) * 31) + Objects.hashCode(this.mPendingResults)) * 31) + Objects.hashCode(this.mPendingNewIntents)) * 31) + this.mIsForward) * 31) + Objects.hashCode(this.mProfilerInfo)) * 31) + Objects.hashCode(this.mAssistToken);
    }

    private boolean activityInfoEqual(ActivityInfo other) {
        ActivityInfo activityInfo = this.mInfo;
        boolean z = true;
        if (activityInfo == null) {
            if (other != null) {
                z = false;
            }
            return z;
        }
        if (!(other != null && activityInfo.flags == other.flags && this.mInfo.maxAspectRatio == other.maxAspectRatio && Objects.equals(this.mInfo.launchToken, other.launchToken) && Objects.equals(this.mInfo.getComponentName(), other.getComponentName()))) {
            z = false;
        }
        return z;
    }

    private static boolean areBundlesEqual(BaseBundle extras, BaseBundle newExtras) {
        boolean z = true;
        if (extras == null || newExtras == null) {
            if (extras != newExtras) {
                z = false;
            }
            return z;
        } else if (extras.size() != newExtras.size()) {
            return false;
        } else {
            for (String key : extras.keySet()) {
                if (key != null && !Objects.equals(extras.get(key), newExtras.get(key))) {
                    return false;
                }
            }
            return true;
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("LaunchActivityItem{intent=");
        stringBuilder.append(this.mIntent);
        stringBuilder.append(",ident=");
        stringBuilder.append(this.mIdent);
        stringBuilder.append(",info=");
        stringBuilder.append(this.mInfo);
        stringBuilder.append(",curConfig=");
        stringBuilder.append(this.mCurConfig);
        stringBuilder.append(",overrideConfig=");
        stringBuilder.append(this.mOverrideConfig);
        stringBuilder.append(",referrer=");
        stringBuilder.append(this.mReferrer);
        stringBuilder.append(",procState=");
        stringBuilder.append(this.mProcState);
        stringBuilder.append(",state=");
        stringBuilder.append(this.mState);
        stringBuilder.append(",persistentState=");
        stringBuilder.append(this.mPersistentState);
        stringBuilder.append(",pendingResults=");
        stringBuilder.append(this.mPendingResults);
        stringBuilder.append(",pendingNewIntents=");
        stringBuilder.append(this.mPendingNewIntents);
        stringBuilder.append(",profilerInfo=");
        stringBuilder.append(this.mProfilerInfo);
        stringBuilder.append(" assistToken=");
        stringBuilder.append(this.mAssistToken);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    private static void setValues(LaunchActivityItem instance, Intent intent, int ident, ActivityInfo info, Configuration curConfig, Configuration overrideConfig, CompatibilityInfo compatInfo, String referrer, IVoiceInteractor voiceInteractor, int procState, Bundle state, PersistableBundle persistentState, List<ResultInfo> pendingResults, List<ReferrerIntent> pendingNewIntents, boolean isForward, ProfilerInfo profilerInfo, IBinder assistToken) {
        LaunchActivityItem launchActivityItem = instance;
        launchActivityItem.mIntent = intent;
        launchActivityItem.mIdent = ident;
        launchActivityItem.mInfo = info;
        launchActivityItem.mCurConfig = curConfig;
        launchActivityItem.mOverrideConfig = overrideConfig;
        launchActivityItem.mCompatInfo = compatInfo;
        launchActivityItem.mReferrer = referrer;
        launchActivityItem.mVoiceInteractor = voiceInteractor;
        launchActivityItem.mProcState = procState;
        launchActivityItem.mState = state;
        launchActivityItem.mPersistentState = persistentState;
        launchActivityItem.mPendingResults = pendingResults;
        launchActivityItem.mPendingNewIntents = pendingNewIntents;
        launchActivityItem.mIsForward = isForward;
        launchActivityItem.mProfilerInfo = profilerInfo;
        launchActivityItem.mAssistToken = assistToken;
    }
}
