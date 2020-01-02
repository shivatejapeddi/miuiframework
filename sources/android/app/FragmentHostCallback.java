package android.app;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import java.io.FileDescriptor;
import java.io.PrintWriter;

@Deprecated
public abstract class FragmentHostCallback<E> extends FragmentContainer {
    private final Activity mActivity;
    private ArrayMap<String, LoaderManager> mAllLoaderManagers;
    private boolean mCheckedForLoaderManager;
    final Context mContext;
    final FragmentManagerImpl mFragmentManager;
    private final Handler mHandler;
    private LoaderManagerImpl mLoaderManager;
    @UnsupportedAppUsage
    private boolean mLoadersStarted;
    private boolean mRetainLoaders;
    final int mWindowAnimations;

    public abstract E onGetHost();

    public FragmentHostCallback(Context context, Handler handler, int windowAnimations) {
        this(context instanceof Activity ? (Activity) context : null, context, chooseHandler(context, handler), windowAnimations);
    }

    FragmentHostCallback(Activity activity) {
        this(activity, activity, activity.mHandler, 0);
    }

    FragmentHostCallback(Activity activity, Context context, Handler handler, int windowAnimations) {
        this.mFragmentManager = new FragmentManagerImpl();
        this.mActivity = activity;
        this.mContext = context;
        this.mHandler = handler;
        this.mWindowAnimations = windowAnimations;
    }

    private static Handler chooseHandler(Context context, Handler handler) {
        if (handler == null && (context instanceof Activity)) {
            return ((Activity) context).mHandler;
        }
        return handler;
    }

    public void onDump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
    }

    public boolean onShouldSaveFragmentState(Fragment fragment) {
        return true;
    }

    public LayoutInflater onGetLayoutInflater() {
        return (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public boolean onUseFragmentManagerInflaterFactory() {
        return false;
    }

    public void onInvalidateOptionsMenu() {
    }

    public void onStartActivityFromFragment(Fragment fragment, Intent intent, int requestCode, Bundle options) {
        if (requestCode == -1) {
            this.mContext.startActivity(intent);
            return;
        }
        throw new IllegalStateException("Starting activity with a requestCode requires a FragmentActivity host");
    }

    public void onStartActivityAsUserFromFragment(Fragment fragment, Intent intent, int requestCode, Bundle options, UserHandle userHandle) {
        if (requestCode == -1) {
            this.mContext.startActivityAsUser(intent, userHandle);
            return;
        }
        throw new IllegalStateException("Starting activity with a requestCode requires a FragmentActivity host");
    }

    public void onStartIntentSenderFromFragment(Fragment fragment, IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws SendIntentException {
        if (requestCode == -1) {
            this.mContext.startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags, options);
        } else {
            throw new IllegalStateException("Starting intent sender with a requestCode requires a FragmentActivity host");
        }
    }

    public void onRequestPermissionsFromFragment(Fragment fragment, String[] permissions, int requestCode) {
    }

    public boolean onHasWindowAnimations() {
        return true;
    }

    public int onGetWindowAnimations() {
        return this.mWindowAnimations;
    }

    public void onAttachFragment(Fragment fragment) {
    }

    public <T extends View> T onFindViewById(int id) {
        return null;
    }

    public boolean onHasView() {
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean getRetainLoaders() {
        return this.mRetainLoaders;
    }

    /* Access modifiers changed, original: 0000 */
    public Activity getActivity() {
        return this.mActivity;
    }

    /* Access modifiers changed, original: 0000 */
    public Context getContext() {
        return this.mContext;
    }

    /* Access modifiers changed, original: 0000 */
    public Handler getHandler() {
        return this.mHandler;
    }

    /* Access modifiers changed, original: 0000 */
    public FragmentManagerImpl getFragmentManagerImpl() {
        return this.mFragmentManager;
    }

    /* Access modifiers changed, original: 0000 */
    public LoaderManagerImpl getLoaderManagerImpl() {
        LoaderManagerImpl loaderManagerImpl = this.mLoaderManager;
        if (loaderManagerImpl != null) {
            return loaderManagerImpl;
        }
        this.mCheckedForLoaderManager = true;
        this.mLoaderManager = getLoaderManager("(root)", this.mLoadersStarted, true);
        return this.mLoaderManager;
    }

    /* Access modifiers changed, original: 0000 */
    public void inactivateFragment(String who) {
        ArrayMap arrayMap = this.mAllLoaderManagers;
        if (arrayMap != null) {
            LoaderManagerImpl lm = (LoaderManagerImpl) arrayMap.get(who);
            if (lm != null && !lm.mRetaining) {
                lm.doDestroy();
                this.mAllLoaderManagers.remove(who);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void doLoaderStart() {
        if (!this.mLoadersStarted) {
            this.mLoadersStarted = true;
            LoaderManagerImpl loaderManagerImpl = this.mLoaderManager;
            if (loaderManagerImpl != null) {
                loaderManagerImpl.doStart();
            } else if (!this.mCheckedForLoaderManager) {
                this.mLoaderManager = getLoaderManager("(root)", this.mLoadersStarted, false);
            }
            this.mCheckedForLoaderManager = true;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void doLoaderStop(boolean retain) {
        this.mRetainLoaders = retain;
        LoaderManagerImpl loaderManagerImpl = this.mLoaderManager;
        if (loaderManagerImpl != null && this.mLoadersStarted) {
            this.mLoadersStarted = false;
            if (retain) {
                loaderManagerImpl.doRetain();
            } else {
                loaderManagerImpl.doStop();
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void doLoaderRetain() {
        LoaderManagerImpl loaderManagerImpl = this.mLoaderManager;
        if (loaderManagerImpl != null) {
            loaderManagerImpl.doRetain();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void doLoaderDestroy() {
        LoaderManagerImpl loaderManagerImpl = this.mLoaderManager;
        if (loaderManagerImpl != null) {
            loaderManagerImpl.doDestroy();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void reportLoaderStart() {
        int N = this.mAllLoaderManagers;
        if (N != 0) {
            int i;
            N = N.size();
            LoaderManagerImpl[] loaders = new LoaderManagerImpl[N];
            for (i = N - 1; i >= 0; i--) {
                loaders[i] = (LoaderManagerImpl) this.mAllLoaderManagers.valueAt(i);
            }
            for (i = 0; i < N; i++) {
                LoaderManagerImpl lm = loaders[i];
                lm.finishRetain();
                lm.doReportStart();
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public LoaderManagerImpl getLoaderManager(String who, boolean started, boolean create) {
        if (this.mAllLoaderManagers == null) {
            this.mAllLoaderManagers = new ArrayMap();
        }
        LoaderManagerImpl lm = (LoaderManagerImpl) this.mAllLoaderManagers.get(who);
        if (lm == null && create) {
            lm = new LoaderManagerImpl(who, this, started);
            this.mAllLoaderManagers.put(who, lm);
            return lm;
        } else if (!started || lm == null || lm.mStarted) {
            return lm;
        } else {
            lm.doStart();
            return lm;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public ArrayMap<String, LoaderManager> retainLoaderNonConfig() {
        boolean retainLoaders = false;
        int N = this.mAllLoaderManagers;
        if (N != 0) {
            N = N.size();
            LoaderManagerImpl[] loaders = new LoaderManagerImpl[N];
            for (int i = N - 1; i >= 0; i--) {
                loaders[i] = (LoaderManagerImpl) this.mAllLoaderManagers.valueAt(i);
            }
            boolean doRetainLoaders = getRetainLoaders();
            for (int i2 = 0; i2 < N; i2++) {
                LoaderManagerImpl lm = loaders[i2];
                if (!lm.mRetaining && doRetainLoaders) {
                    if (!lm.mStarted) {
                        lm.doStart();
                    }
                    lm.doRetain();
                }
                if (lm.mRetaining) {
                    retainLoaders = true;
                } else {
                    lm.doDestroy();
                    this.mAllLoaderManagers.remove(lm.mWho);
                }
            }
        }
        if (retainLoaders) {
            return this.mAllLoaderManagers;
        }
        return null;
    }

    /* Access modifiers changed, original: 0000 */
    public void restoreLoaderNonConfig(ArrayMap<String, LoaderManager> loaderManagers) {
        if (loaderManagers != null) {
            int N = loaderManagers.size();
            for (int i = 0; i < N; i++) {
                ((LoaderManagerImpl) loaderManagers.valueAt(i)).updateHostController(this);
            }
        }
        this.mAllLoaderManagers = loaderManagers;
    }

    /* Access modifiers changed, original: 0000 */
    public void dumpLoaders(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        writer.print(prefix);
        writer.print("mLoadersStarted=");
        writer.println(this.mLoadersStarted);
        if (this.mLoaderManager != null) {
            writer.print(prefix);
            writer.print("Loader Manager ");
            writer.print(Integer.toHexString(System.identityHashCode(this.mLoaderManager)));
            writer.println(":");
            LoaderManagerImpl loaderManagerImpl = this.mLoaderManager;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("  ");
            loaderManagerImpl.dump(stringBuilder.toString(), fd, writer, args);
        }
    }
}
