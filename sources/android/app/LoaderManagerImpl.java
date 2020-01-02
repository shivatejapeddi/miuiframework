package android.app;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.content.Loader.OnLoadCanceledListener;
import android.content.Loader.OnLoadCompleteListener;
import android.os.Bundle;
import android.util.DebugUtils;
import android.util.Log;
import android.util.SparseArray;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;

/* compiled from: LoaderManager */
class LoaderManagerImpl extends LoaderManager {
    static boolean DEBUG = false;
    static final String TAG = "LoaderManager";
    boolean mCreatingLoader;
    private FragmentHostCallback mHost;
    final SparseArray<LoaderInfo> mInactiveLoaders = new SparseArray(0);
    final SparseArray<LoaderInfo> mLoaders = new SparseArray(0);
    boolean mRetaining;
    boolean mRetainingStarted;
    boolean mStarted;
    final String mWho;

    /* compiled from: LoaderManager */
    final class LoaderInfo implements OnLoadCompleteListener<Object>, OnLoadCanceledListener<Object> {
        final Bundle mArgs;
        LoaderCallbacks<Object> mCallbacks;
        Object mData;
        boolean mDeliveredData;
        boolean mDestroyed;
        boolean mHaveData;
        final int mId;
        boolean mListenerRegistered;
        Loader<Object> mLoader;
        LoaderInfo mPendingLoader;
        boolean mReportNextStart;
        boolean mRetaining;
        boolean mRetainingStarted;
        boolean mStarted;

        public LoaderInfo(int id, Bundle args, LoaderCallbacks<Object> callbacks) {
            this.mId = id;
            this.mArgs = args;
            this.mCallbacks = callbacks;
        }

        /* Access modifiers changed, original: 0000 */
        public void start() {
            if (this.mRetaining && this.mRetainingStarted) {
                this.mStarted = true;
            } else if (!this.mStarted) {
                this.mStarted = true;
                if (LoaderManagerImpl.DEBUG) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("  Starting: ");
                    stringBuilder.append(this);
                    Log.v(LoaderManagerImpl.TAG, stringBuilder.toString());
                }
                if (this.mLoader == null) {
                    LoaderCallbacks loaderCallbacks = this.mCallbacks;
                    if (loaderCallbacks != null) {
                        this.mLoader = loaderCallbacks.onCreateLoader(this.mId, this.mArgs);
                    }
                }
                Loader loader = this.mLoader;
                if (loader != null) {
                    if (!loader.getClass().isMemberClass() || Modifier.isStatic(this.mLoader.getClass().getModifiers())) {
                        if (!this.mListenerRegistered) {
                            this.mLoader.registerListener(this.mId, this);
                            this.mLoader.registerOnLoadCanceledListener(this);
                            this.mListenerRegistered = true;
                        }
                        this.mLoader.startLoading();
                    } else {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Object returned from onCreateLoader must not be a non-static inner member class: ");
                        stringBuilder2.append(this.mLoader);
                        throw new IllegalArgumentException(stringBuilder2.toString());
                    }
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void retain() {
            if (LoaderManagerImpl.DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("  Retaining: ");
                stringBuilder.append(this);
                Log.v(LoaderManagerImpl.TAG, stringBuilder.toString());
            }
            this.mRetaining = true;
            this.mRetainingStarted = this.mStarted;
            this.mStarted = false;
            this.mCallbacks = null;
        }

        /* Access modifiers changed, original: 0000 */
        public void finishRetain() {
            if (this.mRetaining) {
                if (LoaderManagerImpl.DEBUG) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("  Finished Retaining: ");
                    stringBuilder.append(this);
                    Log.v(LoaderManagerImpl.TAG, stringBuilder.toString());
                }
                this.mRetaining = false;
                boolean z = this.mStarted;
                if (!(z == this.mRetainingStarted || z)) {
                    stop();
                }
            }
            if (this.mStarted && this.mHaveData && !this.mReportNextStart) {
                callOnLoadFinished(this.mLoader, this.mData);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void reportStart() {
            if (this.mStarted && this.mReportNextStart) {
                this.mReportNextStart = false;
                if (this.mHaveData && !this.mRetaining) {
                    callOnLoadFinished(this.mLoader, this.mData);
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void stop() {
            if (LoaderManagerImpl.DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("  Stopping: ");
                stringBuilder.append(this);
                Log.v(LoaderManagerImpl.TAG, stringBuilder.toString());
            }
            this.mStarted = false;
            if (!this.mRetaining) {
                Loader loader = this.mLoader;
                if (loader != null && this.mListenerRegistered) {
                    this.mListenerRegistered = false;
                    loader.unregisterListener(this);
                    this.mLoader.unregisterOnLoadCanceledListener(this);
                    this.mLoader.stopLoading();
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public boolean cancel() {
            if (LoaderManagerImpl.DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("  Canceling: ");
                stringBuilder.append(this);
                Log.v(LoaderManagerImpl.TAG, stringBuilder.toString());
            }
            if (this.mStarted) {
                boolean cancelLoadResult = this.mLoader;
                if (cancelLoadResult && this.mListenerRegistered) {
                    cancelLoadResult = cancelLoadResult.cancelLoad();
                    if (!cancelLoadResult) {
                        onLoadCanceled(this.mLoader);
                    }
                    return cancelLoadResult;
                }
            }
            return false;
        }

        /* Access modifiers changed, original: 0000 */
        public void destroy() {
            boolean z = LoaderManagerImpl.DEBUG;
            String str = LoaderManagerImpl.TAG;
            if (z) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("  Destroying: ");
                stringBuilder.append(this);
                Log.v(str, stringBuilder.toString());
            }
            this.mDestroyed = true;
            z = this.mDeliveredData;
            this.mDeliveredData = false;
            if (this.mCallbacks != null && this.mLoader != null && this.mHaveData && z) {
                if (LoaderManagerImpl.DEBUG) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("  Reseting: ");
                    stringBuilder2.append(this);
                    Log.v(str, stringBuilder2.toString());
                }
                str = null;
                if (LoaderManagerImpl.this.mHost != null) {
                    str = LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause;
                    LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = "onLoaderReset";
                }
                try {
                    this.mCallbacks.onLoaderReset(this.mLoader);
                } finally {
                    if (LoaderManagerImpl.this.mHost != null) {
                        LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = str;
                    }
                }
            }
            this.mCallbacks = null;
            this.mData = null;
            this.mHaveData = false;
            Loader loader = this.mLoader;
            if (loader != null) {
                if (this.mListenerRegistered) {
                    this.mListenerRegistered = false;
                    loader.unregisterListener(this);
                    this.mLoader.unregisterOnLoadCanceledListener(this);
                }
                this.mLoader.reset();
            }
            LoaderInfo loaderInfo = this.mPendingLoader;
            if (loaderInfo != null) {
                loaderInfo.destroy();
            }
        }

        public void onLoadCanceled(Loader<Object> loader) {
            boolean z = LoaderManagerImpl.DEBUG;
            String str = LoaderManagerImpl.TAG;
            if (z) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onLoadCanceled: ");
                stringBuilder.append(this);
                Log.v(str, stringBuilder.toString());
            }
            if (this.mDestroyed) {
                if (LoaderManagerImpl.DEBUG) {
                    Log.v(str, "  Ignoring load canceled -- destroyed");
                }
            } else if (LoaderManagerImpl.this.mLoaders.get(this.mId) != this) {
                if (LoaderManagerImpl.DEBUG) {
                    Log.v(str, "  Ignoring load canceled -- not active");
                }
            } else {
                LoaderInfo pending = this.mPendingLoader;
                if (pending != null) {
                    if (LoaderManagerImpl.DEBUG) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("  Switching to pending loader: ");
                        stringBuilder2.append(pending);
                        Log.v(str, stringBuilder2.toString());
                    }
                    this.mPendingLoader = null;
                    LoaderManagerImpl.this.mLoaders.put(this.mId, null);
                    destroy();
                    LoaderManagerImpl.this.installLoader(pending);
                }
            }
        }

        public void onLoadComplete(Loader<Object> loader, Object data) {
            boolean z = LoaderManagerImpl.DEBUG;
            String str = LoaderManagerImpl.TAG;
            if (z) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onLoadComplete: ");
                stringBuilder.append(this);
                Log.v(str, stringBuilder.toString());
            }
            if (this.mDestroyed) {
                if (LoaderManagerImpl.DEBUG) {
                    Log.v(str, "  Ignoring load complete -- destroyed");
                }
            } else if (LoaderManagerImpl.this.mLoaders.get(this.mId) != this) {
                if (LoaderManagerImpl.DEBUG) {
                    Log.v(str, "  Ignoring load complete -- not active");
                }
            } else {
                LoaderInfo pending = this.mPendingLoader;
                if (pending != null) {
                    if (LoaderManagerImpl.DEBUG) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("  Switching to pending loader: ");
                        stringBuilder2.append(pending);
                        Log.v(str, stringBuilder2.toString());
                    }
                    this.mPendingLoader = null;
                    LoaderManagerImpl.this.mLoaders.put(this.mId, null);
                    destroy();
                    LoaderManagerImpl.this.installLoader(pending);
                    return;
                }
                if (!(this.mData == data && this.mHaveData)) {
                    this.mData = data;
                    this.mHaveData = true;
                    if (this.mStarted) {
                        callOnLoadFinished(loader, data);
                    }
                }
                LoaderInfo info = (LoaderInfo) LoaderManagerImpl.this.mInactiveLoaders.get(this.mId);
                if (!(info == null || info == this)) {
                    info.mDeliveredData = false;
                    info.destroy();
                    LoaderManagerImpl.this.mInactiveLoaders.remove(this.mId);
                }
                if (!(LoaderManagerImpl.this.mHost == null || LoaderManagerImpl.this.hasRunningLoaders())) {
                    LoaderManagerImpl.this.mHost.mFragmentManager.startPendingDeferredFragments();
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void callOnLoadFinished(Loader<Object> loader, Object data) {
            if (this.mCallbacks != null) {
                String lastBecause = null;
                if (LoaderManagerImpl.this.mHost != null) {
                    lastBecause = LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause;
                    LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = "onLoadFinished";
                }
                try {
                    if (LoaderManagerImpl.DEBUG) {
                        String str = LoaderManagerImpl.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("  onLoadFinished in ");
                        stringBuilder.append(loader);
                        stringBuilder.append(": ");
                        stringBuilder.append(loader.dataToString(data));
                        Log.v(str, stringBuilder.toString());
                    }
                    this.mCallbacks.onLoadFinished(loader, data);
                    this.mDeliveredData = true;
                } finally {
                    if (LoaderManagerImpl.this.mHost != null) {
                        LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = lastBecause;
                    }
                }
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(64);
            sb.append("LoaderInfo{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(" #");
            sb.append(this.mId);
            sb.append(" : ");
            DebugUtils.buildShortClassTag(this.mLoader, sb);
            sb.append("}}");
            return sb.toString();
        }

        public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
            StringBuilder stringBuilder;
            writer.print(prefix);
            writer.print("mId=");
            writer.print(this.mId);
            writer.print(" mArgs=");
            writer.println(this.mArgs);
            writer.print(prefix);
            writer.print("mCallbacks=");
            writer.println(this.mCallbacks);
            writer.print(prefix);
            writer.print("mLoader=");
            writer.println(this.mLoader);
            Loader loader = this.mLoader;
            String str = "  ";
            if (loader != null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(prefix);
                stringBuilder.append(str);
                loader.dump(stringBuilder.toString(), fd, writer, args);
            }
            if (this.mHaveData || this.mDeliveredData) {
                writer.print(prefix);
                writer.print("mHaveData=");
                writer.print(this.mHaveData);
                writer.print("  mDeliveredData=");
                writer.println(this.mDeliveredData);
                writer.print(prefix);
                writer.print("mData=");
                writer.println(this.mData);
            }
            writer.print(prefix);
            writer.print("mStarted=");
            writer.print(this.mStarted);
            writer.print(" mReportNextStart=");
            writer.print(this.mReportNextStart);
            writer.print(" mDestroyed=");
            writer.println(this.mDestroyed);
            writer.print(prefix);
            writer.print("mRetaining=");
            writer.print(this.mRetaining);
            writer.print(" mRetainingStarted=");
            writer.print(this.mRetainingStarted);
            writer.print(" mListenerRegistered=");
            writer.println(this.mListenerRegistered);
            if (this.mPendingLoader != null) {
                writer.print(prefix);
                writer.println("Pending Loader ");
                writer.print(this.mPendingLoader);
                writer.println(":");
                LoaderInfo loaderInfo = this.mPendingLoader;
                stringBuilder = new StringBuilder();
                stringBuilder.append(prefix);
                stringBuilder.append(str);
                loaderInfo.dump(stringBuilder.toString(), fd, writer, args);
            }
        }
    }

    LoaderManagerImpl(String who, FragmentHostCallback host, boolean started) {
        this.mWho = who;
        this.mHost = host;
        this.mStarted = started;
    }

    /* Access modifiers changed, original: 0000 */
    public void updateHostController(FragmentHostCallback host) {
        this.mHost = host;
    }

    public FragmentHostCallback getFragmentHostCallback() {
        return this.mHost;
    }

    private LoaderInfo createLoader(int id, Bundle args, LoaderCallbacks<Object> callback) {
        LoaderInfo info = new LoaderInfo(id, args, callback);
        info.mLoader = callback.onCreateLoader(id, args);
        return info;
    }

    private LoaderInfo createAndInstallLoader(int id, Bundle args, LoaderCallbacks<Object> callback) {
        try {
            this.mCreatingLoader = true;
            LoaderInfo info = createLoader(id, args, callback);
            installLoader(info);
            return info;
        } finally {
            this.mCreatingLoader = false;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void installLoader(LoaderInfo info) {
        this.mLoaders.put(info.mId, info);
        if (this.mStarted) {
            info.start();
        }
    }

    public <D> Loader<D> initLoader(int id, Bundle args, LoaderCallbacks<D> callback) {
        if (this.mCreatingLoader) {
            throw new IllegalStateException("Called while creating a loader");
        }
        StringBuilder stringBuilder;
        LoaderInfo info = (LoaderInfo) this.mLoaders.get(id);
        boolean z = DEBUG;
        String str = TAG;
        if (z) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("initLoader in ");
            stringBuilder.append(this);
            stringBuilder.append(": args=");
            stringBuilder.append(args);
            Log.v(str, stringBuilder.toString());
        }
        if (info == null) {
            info = createAndInstallLoader(id, args, callback);
            if (DEBUG) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("  Created new loader ");
                stringBuilder.append(info);
                Log.v(str, stringBuilder.toString());
            }
        } else {
            if (DEBUG) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("  Re-using existing loader ");
                stringBuilder.append(info);
                Log.v(str, stringBuilder.toString());
            }
            info.mCallbacks = callback;
        }
        if (info.mHaveData && this.mStarted) {
            info.callOnLoadFinished(info.mLoader, info.mData);
        }
        return info.mLoader;
    }

    public <D> Loader<D> restartLoader(int id, Bundle args, LoaderCallbacks<D> callback) {
        if (this.mCreatingLoader) {
            throw new IllegalStateException("Called while creating a loader");
        }
        LoaderInfo info = (LoaderInfo) this.mLoaders.get(id);
        boolean z = DEBUG;
        String str = TAG;
        if (z) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("restartLoader in ");
            stringBuilder.append(this);
            stringBuilder.append(": args=");
            stringBuilder.append(args);
            Log.v(str, stringBuilder.toString());
        }
        if (info != null) {
            LoaderInfo inactive = (LoaderInfo) this.mInactiveLoaders.get(id);
            StringBuilder stringBuilder2;
            if (inactive == null) {
                if (DEBUG) {
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("  Making last loader inactive: ");
                    stringBuilder2.append(info);
                    Log.v(str, stringBuilder2.toString());
                }
                info.mLoader.abandon();
                this.mInactiveLoaders.put(id, info);
            } else if (info.mHaveData) {
                if (DEBUG) {
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("  Removing last inactive loader: ");
                    stringBuilder2.append(info);
                    Log.v(str, stringBuilder2.toString());
                }
                inactive.mDeliveredData = false;
                inactive.destroy();
                info.mLoader.abandon();
                this.mInactiveLoaders.put(id, info);
            } else if (info.cancel()) {
                if (DEBUG) {
                    Log.v(str, "  Current loader is running; configuring pending loader");
                }
                if (info.mPendingLoader != null) {
                    if (DEBUG) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("  Removing pending loader: ");
                        stringBuilder2.append(info.mPendingLoader);
                        Log.v(str, stringBuilder2.toString());
                    }
                    info.mPendingLoader.destroy();
                    info.mPendingLoader = null;
                }
                if (DEBUG) {
                    Log.v(str, "  Enqueuing as new pending loader");
                }
                info.mPendingLoader = createLoader(id, args, callback);
                return info.mPendingLoader.mLoader;
            } else {
                if (DEBUG) {
                    Log.v(str, "  Current loader is stopped; replacing");
                }
                this.mLoaders.put(id, null);
                info.destroy();
            }
        }
        return createAndInstallLoader(id, args, callback).mLoader;
    }

    public void destroyLoader(int id) {
        if (this.mCreatingLoader) {
            throw new IllegalStateException("Called while creating a loader");
        }
        LoaderInfo info;
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("destroyLoader in ");
            stringBuilder.append(this);
            stringBuilder.append(" of ");
            stringBuilder.append(id);
            Log.v(TAG, stringBuilder.toString());
        }
        int idx = this.mLoaders.indexOfKey(id);
        if (idx >= 0) {
            info = (LoaderInfo) this.mLoaders.valueAt(idx);
            this.mLoaders.removeAt(idx);
            info.destroy();
        }
        idx = this.mInactiveLoaders.indexOfKey(id);
        if (idx >= 0) {
            info = (LoaderInfo) this.mInactiveLoaders.valueAt(idx);
            this.mInactiveLoaders.removeAt(idx);
            info.destroy();
        }
        if (this.mHost != null && !hasRunningLoaders()) {
            this.mHost.mFragmentManager.startPendingDeferredFragments();
        }
    }

    public <D> Loader<D> getLoader(int id) {
        if (this.mCreatingLoader) {
            throw new IllegalStateException("Called while creating a loader");
        }
        LoaderInfo loaderInfo = (LoaderInfo) this.mLoaders.get(id);
        if (loaderInfo == null) {
            return null;
        }
        if (loaderInfo.mPendingLoader != null) {
            return loaderInfo.mPendingLoader.mLoader;
        }
        return loaderInfo.mLoader;
    }

    /* Access modifiers changed, original: 0000 */
    public void doStart() {
        boolean z = DEBUG;
        String str = TAG;
        if (z) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Starting in ");
            stringBuilder.append(this);
            Log.v(str, stringBuilder.toString());
        }
        if (this.mStarted) {
            RuntimeException e = new RuntimeException("here");
            e.fillInStackTrace();
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Called doStart when already started: ");
            stringBuilder2.append(this);
            Log.w(str, stringBuilder2.toString(), e);
            return;
        }
        this.mStarted = true;
        for (int i = this.mLoaders.size() - 1; i >= 0; i--) {
            ((LoaderInfo) this.mLoaders.valueAt(i)).start();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void doStop() {
        boolean z = DEBUG;
        String str = TAG;
        if (z) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Stopping in ");
            stringBuilder.append(this);
            Log.v(str, stringBuilder.toString());
        }
        if (this.mStarted) {
            for (int i = this.mLoaders.size() - 1; i >= 0; i--) {
                ((LoaderInfo) this.mLoaders.valueAt(i)).stop();
            }
            this.mStarted = false;
            return;
        }
        RuntimeException e = new RuntimeException("here");
        e.fillInStackTrace();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Called doStop when not started: ");
        stringBuilder2.append(this);
        Log.w(str, stringBuilder2.toString(), e);
    }

    /* Access modifiers changed, original: 0000 */
    public void doRetain() {
        boolean z = DEBUG;
        String str = TAG;
        if (z) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Retaining in ");
            stringBuilder.append(this);
            Log.v(str, stringBuilder.toString());
        }
        if (this.mStarted) {
            this.mRetaining = true;
            this.mStarted = false;
            for (int i = this.mLoaders.size() - 1; i >= 0; i--) {
                ((LoaderInfo) this.mLoaders.valueAt(i)).retain();
            }
            return;
        }
        RuntimeException e = new RuntimeException("here");
        e.fillInStackTrace();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Called doRetain when not started: ");
        stringBuilder2.append(this);
        Log.w(str, stringBuilder2.toString(), e);
    }

    /* Access modifiers changed, original: 0000 */
    public void finishRetain() {
        if (this.mRetaining) {
            if (DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Finished Retaining in ");
                stringBuilder.append(this);
                Log.v(TAG, stringBuilder.toString());
            }
            this.mRetaining = false;
            for (int i = this.mLoaders.size() - 1; i >= 0; i--) {
                ((LoaderInfo) this.mLoaders.valueAt(i)).finishRetain();
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void doReportNextStart() {
        for (int i = this.mLoaders.size() - 1; i >= 0; i--) {
            ((LoaderInfo) this.mLoaders.valueAt(i)).mReportNextStart = true;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void doReportStart() {
        for (int i = this.mLoaders.size() - 1; i >= 0; i--) {
            ((LoaderInfo) this.mLoaders.valueAt(i)).reportStart();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void doDestroy() {
        StringBuilder stringBuilder;
        int i;
        boolean z = this.mRetaining;
        String str = TAG;
        if (!z) {
            if (DEBUG) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Destroying Active in ");
                stringBuilder.append(this);
                Log.v(str, stringBuilder.toString());
            }
            for (i = this.mLoaders.size() - 1; i >= 0; i--) {
                ((LoaderInfo) this.mLoaders.valueAt(i)).destroy();
            }
            this.mLoaders.clear();
        }
        if (DEBUG) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Destroying Inactive in ");
            stringBuilder.append(this);
            Log.v(str, stringBuilder.toString());
        }
        for (i = this.mInactiveLoaders.size() - 1; i >= 0; i--) {
            ((LoaderInfo) this.mInactiveLoaders.valueAt(i)).destroy();
        }
        this.mInactiveLoaders.clear();
        this.mHost = null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("LoaderManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        DebugUtils.buildShortClassTag(this.mHost, sb);
        sb.append("}}");
        return sb.toString();
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        String innerPrefix;
        String str = ": ";
        String str2 = "  #";
        String str3 = "    ";
        if (this.mLoaders.size() > 0) {
            writer.print(prefix);
            writer.println("Active Loaders:");
            innerPrefix = new StringBuilder();
            innerPrefix.append(prefix);
            innerPrefix.append(str3);
            innerPrefix = innerPrefix.toString();
            for (int i = 0; i < this.mLoaders.size(); i++) {
                LoaderInfo li = (LoaderInfo) this.mLoaders.valueAt(i);
                writer.print(prefix);
                writer.print(str2);
                writer.print(this.mLoaders.keyAt(i));
                writer.print(str);
                writer.println(li.toString());
                li.dump(innerPrefix, fd, writer, args);
            }
        }
        if (this.mInactiveLoaders.size() > 0) {
            writer.print(prefix);
            writer.println("Inactive Loaders:");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append(str3);
            innerPrefix = stringBuilder.toString();
            for (int i2 = 0; i2 < this.mInactiveLoaders.size(); i2++) {
                LoaderInfo li2 = (LoaderInfo) this.mInactiveLoaders.valueAt(i2);
                writer.print(prefix);
                writer.print(str2);
                writer.print(this.mInactiveLoaders.keyAt(i2));
                writer.print(str);
                writer.println(li2.toString());
                li2.dump(innerPrefix, fd, writer, args);
            }
        }
    }

    public boolean hasRunningLoaders() {
        boolean loadersRunning = false;
        int count = this.mLoaders.size();
        for (int i = 0; i < count; i++) {
            LoaderInfo li = (LoaderInfo) this.mLoaders.valueAt(i);
            int i2 = (!li.mStarted || li.mDeliveredData) ? 0 : 1;
            loadersRunning |= i2;
        }
        return loadersRunning;
    }
}
