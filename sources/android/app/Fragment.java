package android.app;

import android.animation.Animator;
import android.annotation.UnsupportedAppUsage;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.UserHandle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.util.AndroidRuntimeException;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.DebugUtils;
import android.util.SparseArray;
import android.util.SuperNotCalledException;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import com.android.internal.R;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

@Deprecated
public class Fragment implements ComponentCallbacks2, OnCreateContextMenuListener {
    static final int ACTIVITY_CREATED = 2;
    static final int CREATED = 1;
    static final int INITIALIZING = 0;
    static final int INVALID_STATE = -1;
    static final int RESUMED = 5;
    static final int STARTED = 4;
    static final int STOPPED = 3;
    private static final Transition USE_DEFAULT_TRANSITION = new TransitionSet();
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private static final ArrayMap<String, Class<?>> sClassMap = new ArrayMap();
    @UnsupportedAppUsage
    boolean mAdded;
    AnimationInfo mAnimationInfo;
    Bundle mArguments;
    int mBackStackNesting;
    boolean mCalled;
    boolean mCheckedForLoaderManager;
    @UnsupportedAppUsage
    FragmentManagerImpl mChildFragmentManager;
    FragmentManagerNonConfig mChildNonConfig;
    ViewGroup mContainer;
    int mContainerId;
    boolean mDeferStart;
    boolean mDetached;
    @UnsupportedAppUsage
    int mFragmentId;
    @UnsupportedAppUsage
    FragmentManagerImpl mFragmentManager;
    boolean mFromLayout;
    boolean mHasMenu;
    boolean mHidden;
    boolean mHiddenChanged;
    @UnsupportedAppUsage
    FragmentHostCallback mHost;
    boolean mInLayout;
    @UnsupportedAppUsage
    int mIndex = -1;
    boolean mIsCreated;
    boolean mIsNewlyAdded;
    LayoutInflater mLayoutInflater;
    LoaderManagerImpl mLoaderManager;
    @UnsupportedAppUsage
    boolean mLoadersStarted;
    boolean mMenuVisible = true;
    Fragment mParentFragment;
    boolean mPerformedCreateView;
    boolean mRemoving;
    boolean mRestored;
    boolean mRetainInstance;
    boolean mRetaining;
    @UnsupportedAppUsage
    Bundle mSavedFragmentState;
    SparseArray<Parcelable> mSavedViewState;
    int mState = 0;
    String mTag;
    Fragment mTarget;
    int mTargetIndex = -1;
    int mTargetRequestCode;
    boolean mUserVisibleHint = true;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    View mView;
    @UnsupportedAppUsage
    String mWho;

    static class AnimationInfo {
        private Boolean mAllowEnterTransitionOverlap;
        private Boolean mAllowReturnTransitionOverlap;
        Animator mAnimatingAway;
        private Transition mEnterTransition = null;
        SharedElementCallback mEnterTransitionCallback = SharedElementCallback.NULL_CALLBACK;
        boolean mEnterTransitionPostponed;
        private Transition mExitTransition = null;
        SharedElementCallback mExitTransitionCallback = SharedElementCallback.NULL_CALLBACK;
        boolean mIsHideReplaced;
        int mNextAnim;
        int mNextTransition;
        int mNextTransitionStyle;
        private Transition mReenterTransition = Fragment.USE_DEFAULT_TRANSITION;
        private Transition mReturnTransition = Fragment.USE_DEFAULT_TRANSITION;
        private Transition mSharedElementEnterTransition = null;
        private Transition mSharedElementReturnTransition = Fragment.USE_DEFAULT_TRANSITION;
        OnStartEnterTransitionListener mStartEnterTransitionListener;
        int mStateAfterAnimating;

        AnimationInfo() {
        }
    }

    @Deprecated
    public static class InstantiationException extends AndroidRuntimeException {
        public InstantiationException(String msg, Exception cause) {
            super(msg, cause);
        }
    }

    interface OnStartEnterTransitionListener {
        void onStartEnterTransition();

        void startListening();
    }

    @Deprecated
    public static class SavedState implements Parcelable {
        public static final ClassLoaderCreator<SavedState> CREATOR = new ClassLoaderCreator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, null);
            }

            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        final Bundle mState;

        SavedState(Bundle state) {
            this.mState = state;
        }

        SavedState(Parcel in, ClassLoader loader) {
            this.mState = in.readBundle();
            if (loader != null) {
                Bundle bundle = this.mState;
                if (bundle != null) {
                    bundle.setClassLoader(loader);
                }
            }
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeBundle(this.mState);
        }
    }

    public static Fragment instantiate(Context context, String fname) {
        return instantiate(context, fname, null);
    }

    public static Fragment instantiate(Context context, String fname, Bundle args) {
        StringBuilder stringBuilder;
        String str = ": make sure class name exists, is public, and has an empty constructor that is public";
        String str2 = "Unable to instantiate fragment ";
        StringBuilder stringBuilder2;
        try {
            Class<?> clazz = (Class) sClassMap.get(fname);
            if (clazz == null) {
                clazz = context.getClassLoader().loadClass(fname);
                if (Fragment.class.isAssignableFrom(clazz)) {
                    sClassMap.put(fname, clazz);
                } else {
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Trying to instantiate a class ");
                    stringBuilder2.append(fname);
                    stringBuilder2.append(" that is not a Fragment");
                    throw new InstantiationException(stringBuilder2.toString(), new ClassCastException());
                }
            }
            Fragment f = (Fragment) clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
            if (args != null) {
                args.setClassLoader(f.getClass().getClassLoader());
                f.setArguments(args);
            }
            return f;
        } catch (ClassNotFoundException e) {
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str2);
            stringBuilder2.append(fname);
            stringBuilder2.append(str);
            throw new InstantiationException(stringBuilder2.toString(), e);
        } catch (InstantiationException e2) {
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str2);
            stringBuilder2.append(fname);
            stringBuilder2.append(str);
            throw new InstantiationException(stringBuilder2.toString(), e2);
        } catch (IllegalAccessException e3) {
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str2);
            stringBuilder2.append(fname);
            stringBuilder2.append(str);
            throw new InstantiationException(stringBuilder2.toString(), e3);
        } catch (NoSuchMethodException e4) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(fname);
            stringBuilder.append(": could not find Fragment constructor");
            throw new InstantiationException(stringBuilder.toString(), e4);
        } catch (InvocationTargetException e5) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(fname);
            stringBuilder.append(": calling Fragment constructor caused an exception");
            throw new InstantiationException(stringBuilder.toString(), e5);
        }
    }

    /* Access modifiers changed, original: final */
    public final void restoreViewState(Bundle savedInstanceState) {
        SparseArray sparseArray = this.mSavedViewState;
        if (sparseArray != null) {
            this.mView.restoreHierarchyState(sparseArray);
            this.mSavedViewState = null;
        }
        this.mCalled = false;
        onViewStateRestored(savedInstanceState);
        if (!this.mCalled) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fragment ");
            stringBuilder.append(this);
            stringBuilder.append(" did not call through to super.onViewStateRestored()");
            throw new SuperNotCalledException(stringBuilder.toString());
        }
    }

    /* Access modifiers changed, original: final */
    public final void setIndex(int index, Fragment parent) {
        this.mIndex = index;
        StringBuilder stringBuilder;
        if (parent != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(parent.mWho);
            stringBuilder.append(":");
            stringBuilder.append(this.mIndex);
            this.mWho = stringBuilder.toString();
            return;
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("android:fragment:");
        stringBuilder.append(this.mIndex);
        this.mWho = stringBuilder.toString();
    }

    /* Access modifiers changed, original: final */
    public final boolean isInBackStack() {
        return this.mBackStackNesting > 0;
    }

    public final boolean equals(Object o) {
        return super.equals(o);
    }

    public final int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        DebugUtils.buildShortClassTag(this, sb);
        if (this.mIndex >= 0) {
            sb.append(" #");
            sb.append(this.mIndex);
        }
        if (this.mFragmentId != 0) {
            sb.append(" id=0x");
            sb.append(Integer.toHexString(this.mFragmentId));
        }
        if (this.mTag != null) {
            sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            sb.append(this.mTag);
        }
        sb.append('}');
        return sb.toString();
    }

    public final int getId() {
        return this.mFragmentId;
    }

    public final String getTag() {
        return this.mTag;
    }

    public void setArguments(Bundle args) {
        if (this.mIndex < 0 || !isStateSaved()) {
            this.mArguments = args;
            return;
        }
        throw new IllegalStateException("Fragment already active");
    }

    public final Bundle getArguments() {
        return this.mArguments;
    }

    public final boolean isStateSaved() {
        FragmentManagerImpl fragmentManagerImpl = this.mFragmentManager;
        if (fragmentManagerImpl == null) {
            return false;
        }
        return fragmentManagerImpl.isStateSaved();
    }

    public void setInitialSavedState(SavedState state) {
        if (this.mIndex < 0) {
            Bundle bundle = (state == null || state.mState == null) ? null : state.mState;
            this.mSavedFragmentState = bundle;
            return;
        }
        throw new IllegalStateException("Fragment already active");
    }

    public void setTargetFragment(Fragment fragment, int requestCode) {
        FragmentManager mine = getFragmentManager();
        FragmentManager theirs = fragment != null ? fragment.getFragmentManager() : null;
        if (mine == null || theirs == null || mine == theirs) {
            Fragment check = fragment;
            while (check != null) {
                if (check != this) {
                    check = check.getTargetFragment();
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Setting ");
                    stringBuilder.append(fragment);
                    stringBuilder.append(" as the target of ");
                    stringBuilder.append(this);
                    stringBuilder.append(" would create a target cycle");
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
            this.mTarget = fragment;
            this.mTargetRequestCode = requestCode;
            return;
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Fragment ");
        stringBuilder2.append(fragment);
        stringBuilder2.append(" must share the same FragmentManager to be set as a target fragment");
        throw new IllegalArgumentException(stringBuilder2.toString());
    }

    public final Fragment getTargetFragment() {
        return this.mTarget;
    }

    public final int getTargetRequestCode() {
        return this.mTargetRequestCode;
    }

    public Context getContext() {
        FragmentHostCallback fragmentHostCallback = this.mHost;
        return fragmentHostCallback == null ? null : fragmentHostCallback.getContext();
    }

    public final Activity getActivity() {
        FragmentHostCallback fragmentHostCallback = this.mHost;
        return fragmentHostCallback == null ? null : fragmentHostCallback.getActivity();
    }

    public final Object getHost() {
        FragmentHostCallback fragmentHostCallback = this.mHost;
        return fragmentHostCallback == null ? null : fragmentHostCallback.onGetHost();
    }

    public final Resources getResources() {
        FragmentHostCallback fragmentHostCallback = this.mHost;
        if (fragmentHostCallback != null) {
            return fragmentHostCallback.getContext().getResources();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Fragment ");
        stringBuilder.append(this);
        stringBuilder.append(" not attached to Activity");
        throw new IllegalStateException(stringBuilder.toString());
    }

    public final CharSequence getText(int resId) {
        return getResources().getText(resId);
    }

    public final String getString(int resId) {
        return getResources().getString(resId);
    }

    public final String getString(int resId, Object... formatArgs) {
        return getResources().getString(resId, formatArgs);
    }

    public final FragmentManager getFragmentManager() {
        return this.mFragmentManager;
    }

    public final FragmentManager getChildFragmentManager() {
        if (this.mChildFragmentManager == null) {
            instantiateChildFragmentManager();
            int i = this.mState;
            if (i >= 5) {
                this.mChildFragmentManager.dispatchResume();
            } else if (i >= 4) {
                this.mChildFragmentManager.dispatchStart();
            } else if (i >= 2) {
                this.mChildFragmentManager.dispatchActivityCreated();
            } else if (i >= 1) {
                this.mChildFragmentManager.dispatchCreate();
            }
        }
        return this.mChildFragmentManager;
    }

    public final Fragment getParentFragment() {
        return this.mParentFragment;
    }

    public final boolean isAdded() {
        return this.mHost != null && this.mAdded;
    }

    public final boolean isDetached() {
        return this.mDetached;
    }

    public final boolean isRemoving() {
        return this.mRemoving;
    }

    public final boolean isInLayout() {
        return this.mInLayout;
    }

    public final boolean isResumed() {
        return this.mState >= 5;
    }

    public final boolean isVisible() {
        if (isAdded() && !isHidden()) {
            View view = this.mView;
            if (!(view == null || view.getWindowToken() == null || this.mView.getVisibility() != 0)) {
                return true;
            }
        }
        return false;
    }

    public final boolean isHidden() {
        return this.mHidden;
    }

    public void onHiddenChanged(boolean hidden) {
    }

    public void setRetainInstance(boolean retain) {
        this.mRetainInstance = retain;
    }

    public final boolean getRetainInstance() {
        return this.mRetainInstance;
    }

    public void setHasOptionsMenu(boolean hasMenu) {
        if (this.mHasMenu != hasMenu) {
            this.mHasMenu = hasMenu;
            if (isAdded() && !isHidden()) {
                this.mFragmentManager.invalidateOptionsMenu();
            }
        }
    }

    public void setMenuVisibility(boolean menuVisible) {
        if (this.mMenuVisible != menuVisible) {
            this.mMenuVisible = menuVisible;
            if (this.mHasMenu && isAdded() && !isHidden()) {
                this.mFragmentManager.invalidateOptionsMenu();
            }
        }
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        boolean useBrokenAddedCheck = false;
        Context context = getContext();
        FragmentManagerImpl fragmentManagerImpl = this.mFragmentManager;
        if (!(fragmentManagerImpl == null || fragmentManagerImpl.mHost == null)) {
            context = this.mFragmentManager.mHost.getContext();
        }
        boolean z = true;
        if (context != null) {
            useBrokenAddedCheck = context.getApplicationInfo().targetSdkVersion <= 23;
        }
        boolean performDeferredStart = useBrokenAddedCheck ? !this.mUserVisibleHint && isVisibleToUser && this.mState < 4 && this.mFragmentManager != null : !this.mUserVisibleHint && isVisibleToUser && this.mState < 4 && this.mFragmentManager != null && isAdded();
        if (performDeferredStart) {
            this.mFragmentManager.performPendingDeferredStart(this);
        }
        this.mUserVisibleHint = isVisibleToUser;
        if (this.mState >= 4 || isVisibleToUser) {
            z = false;
        }
        this.mDeferStart = z;
    }

    public boolean getUserVisibleHint() {
        return this.mUserVisibleHint;
    }

    @Deprecated
    public LoaderManager getLoaderManager() {
        LoaderManagerImpl loaderManagerImpl = this.mLoaderManager;
        if (loaderManagerImpl != null) {
            return loaderManagerImpl;
        }
        FragmentHostCallback fragmentHostCallback = this.mHost;
        if (fragmentHostCallback != null) {
            this.mCheckedForLoaderManager = true;
            this.mLoaderManager = fragmentHostCallback.getLoaderManager(this.mWho, this.mLoadersStarted, true);
            return this.mLoaderManager;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Fragment ");
        stringBuilder.append(this);
        stringBuilder.append(" not attached to Activity");
        throw new IllegalStateException(stringBuilder.toString());
    }

    public void startActivity(Intent intent) {
        startActivity(intent, null);
    }

    public void startActivity(Intent intent, Bundle options) {
        FragmentHostCallback fragmentHostCallback = this.mHost;
        if (fragmentHostCallback == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fragment ");
            stringBuilder.append(this);
            stringBuilder.append(" not attached to Activity");
            throw new IllegalStateException(stringBuilder.toString());
        } else if (options != null) {
            fragmentHostCallback.onStartActivityFromFragment(this, intent, -1, options);
        } else {
            fragmentHostCallback.onStartActivityFromFragment(this, intent, -1, null);
        }
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode, null);
    }

    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        FragmentHostCallback fragmentHostCallback = this.mHost;
        if (fragmentHostCallback != null) {
            fragmentHostCallback.onStartActivityFromFragment(this, intent, requestCode, options);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Fragment ");
        stringBuilder.append(this);
        stringBuilder.append(" not attached to Activity");
        throw new IllegalStateException(stringBuilder.toString());
    }

    public void startActivityForResultAsUser(Intent intent, int requestCode, Bundle options, UserHandle user) {
        FragmentHostCallback fragmentHostCallback = this.mHost;
        if (fragmentHostCallback != null) {
            fragmentHostCallback.onStartActivityAsUserFromFragment(this, intent, requestCode, options, user);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Fragment ");
        stringBuilder.append(this);
        stringBuilder.append(" not attached to Activity");
        throw new IllegalStateException(stringBuilder.toString());
    }

    public void startIntentSenderForResult(IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws SendIntentException {
        FragmentHostCallback fragmentHostCallback = this.mHost;
        if (fragmentHostCallback != null) {
            fragmentHostCallback.onStartIntentSenderFromFragment(this, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Fragment ");
        stringBuilder.append(this);
        stringBuilder.append(" not attached to Activity");
        throw new IllegalStateException(stringBuilder.toString());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public final void requestPermissions(String[] permissions, int requestCode) {
        FragmentHostCallback fragmentHostCallback = this.mHost;
        if (fragmentHostCallback != null) {
            fragmentHostCallback.onRequestPermissionsFromFragment(this, permissions, requestCode);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Fragment ");
        stringBuilder.append(this);
        stringBuilder.append(" not attached to Activity");
        throw new IllegalStateException(stringBuilder.toString());
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    }

    public boolean shouldShowRequestPermissionRationale(String permission) {
        FragmentHostCallback fragmentHostCallback = this.mHost;
        if (fragmentHostCallback != null) {
            return fragmentHostCallback.getContext().getPackageManager().shouldShowRequestPermissionRationale(permission);
        }
        return false;
    }

    public LayoutInflater onGetLayoutInflater(Bundle savedInstanceState) {
        LayoutInflater result = this.mHost;
        if (result != null) {
            result = result.onGetLayoutInflater();
            if (this.mHost.onUseFragmentManagerInflaterFactory()) {
                getChildFragmentManager();
                result.setPrivateFactory(this.mChildFragmentManager.getLayoutInflaterFactory());
            }
            return result;
        }
        throw new IllegalStateException("onGetLayoutInflater() cannot be executed until the Fragment is attached to the FragmentManager.");
    }

    public final LayoutInflater getLayoutInflater() {
        LayoutInflater layoutInflater = this.mLayoutInflater;
        if (layoutInflater == null) {
            return performGetLayoutInflater(null);
        }
        return layoutInflater;
    }

    /* Access modifiers changed, original: 0000 */
    public LayoutInflater performGetLayoutInflater(Bundle savedInstanceState) {
        this.mLayoutInflater = onGetLayoutInflater(savedInstanceState);
        return this.mLayoutInflater;
    }

    @Deprecated
    public void onInflate(AttributeSet attrs, Bundle savedInstanceState) {
        this.mCalled = true;
    }

    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        boolean isReturnSet;
        onInflate(attrs, savedInstanceState);
        this.mCalled = true;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Fragment);
        Transition transition = null;
        setEnterTransition(loadTransition(context, a, getEnterTransition(), null, 4));
        setReturnTransition(loadTransition(context, a, getReturnTransition(), USE_DEFAULT_TRANSITION, 6));
        setExitTransition(loadTransition(context, a, getExitTransition(), null, 3));
        setReenterTransition(loadTransition(context, a, getReenterTransition(), USE_DEFAULT_TRANSITION, 8));
        setSharedElementEnterTransition(loadTransition(context, a, getSharedElementEnterTransition(), null, 5));
        setSharedElementReturnTransition(loadTransition(context, a, getSharedElementReturnTransition(), USE_DEFAULT_TRANSITION, 7));
        boolean isEnterSet = this.mAnimationInfo;
        if (isEnterSet) {
            isEnterSet = isEnterSet.mAllowEnterTransitionOverlap != null;
            isReturnSet = this.mAnimationInfo.mAllowReturnTransitionOverlap != null;
        } else {
            isEnterSet = false;
            isReturnSet = false;
        }
        if (!isEnterSet) {
            setAllowEnterTransitionOverlap(a.getBoolean(9, true));
        }
        if (!isReturnSet) {
            setAllowReturnTransitionOverlap(a.getBoolean(10, true));
        }
        a.recycle();
        FragmentHostCallback fragmentHostCallback = this.mHost;
        if (fragmentHostCallback != null) {
            transition = fragmentHostCallback.getActivity();
        }
        Activity hostActivity = transition;
        if (hostActivity != null) {
            this.mCalled = false;
            onInflate(hostActivity, attrs, savedInstanceState);
        }
    }

    @Deprecated
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        this.mCalled = true;
    }

    public void onAttachFragment(Fragment childFragment) {
    }

    public void onAttach(Context context) {
        this.mCalled = true;
        FragmentHostCallback fragmentHostCallback = this.mHost;
        Activity hostActivity = fragmentHostCallback == null ? null : fragmentHostCallback.getActivity();
        if (hostActivity != null) {
            this.mCalled = false;
            onAttach(hostActivity);
        }
    }

    @Deprecated
    public void onAttach(Activity activity) {
        this.mCalled = true;
    }

    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        return null;
    }

    public void onCreate(Bundle savedInstanceState) {
        this.mCalled = true;
        Context context = getContext();
        if ((context != null ? context.getApplicationInfo().targetSdkVersion : 0) >= 24) {
            restoreChildFragmentState(savedInstanceState, true);
            FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
            if (fragmentManagerImpl != null && !fragmentManagerImpl.isStateAtLeast(1)) {
                this.mChildFragmentManager.dispatchCreate();
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void restoreChildFragmentState(Bundle savedInstanceState, boolean provideNonConfig) {
        if (savedInstanceState != null) {
            Parcelable p = savedInstanceState.getParcelable("android:fragments");
            if (p != null) {
                if (this.mChildFragmentManager == null) {
                    instantiateChildFragmentManager();
                }
                this.mChildFragmentManager.restoreAllState(p, provideNonConfig ? this.mChildNonConfig : null);
                this.mChildNonConfig = null;
                this.mChildFragmentManager.dispatchCreate();
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
    }

    public View getView() {
        return this.mView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        this.mCalled = true;
    }

    public void onViewStateRestored(Bundle savedInstanceState) {
        this.mCalled = true;
    }

    public void onStart() {
        this.mCalled = true;
        if (!this.mLoadersStarted) {
            this.mLoadersStarted = true;
            if (this.mCheckedForLoaderManager) {
                LoaderManagerImpl loaderManagerImpl = this.mLoaderManager;
                if (loaderManagerImpl != null) {
                    loaderManagerImpl.doStart();
                    return;
                }
                return;
            }
            this.mCheckedForLoaderManager = true;
            this.mLoaderManager = this.mHost.getLoaderManager(this.mWho, this.mLoadersStarted, false);
        }
    }

    public void onResume() {
        this.mCalled = true;
    }

    public void onSaveInstanceState(Bundle outState) {
    }

    public void onMultiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig) {
        onMultiWindowModeChanged(isInMultiWindowMode);
    }

    @Deprecated
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
    }

    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        onPictureInPictureModeChanged(isInPictureInPictureMode);
    }

    @Deprecated
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
    }

    public void onConfigurationChanged(Configuration newConfig) {
        this.mCalled = true;
    }

    public void onPause() {
        this.mCalled = true;
    }

    public void onStop() {
        this.mCalled = true;
    }

    public void onLowMemory() {
        this.mCalled = true;
    }

    public void onTrimMemory(int level) {
        this.mCalled = true;
    }

    public void onDestroyView() {
        this.mCalled = true;
    }

    public void onDestroy() {
        this.mCalled = true;
        if (!this.mCheckedForLoaderManager) {
            this.mCheckedForLoaderManager = true;
            this.mLoaderManager = this.mHost.getLoaderManager(this.mWho, this.mLoadersStarted, false);
        }
        LoaderManagerImpl loaderManagerImpl = this.mLoaderManager;
        if (loaderManagerImpl != null) {
            loaderManagerImpl.doDestroy();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void initState() {
        this.mIndex = -1;
        this.mWho = null;
        this.mAdded = false;
        this.mRemoving = false;
        this.mFromLayout = false;
        this.mInLayout = false;
        this.mRestored = false;
        this.mBackStackNesting = 0;
        this.mFragmentManager = null;
        this.mChildFragmentManager = null;
        this.mHost = null;
        this.mFragmentId = 0;
        this.mContainerId = 0;
        this.mTag = null;
        this.mHidden = false;
        this.mDetached = false;
        this.mRetaining = false;
        this.mLoaderManager = null;
        this.mLoadersStarted = false;
        this.mCheckedForLoaderManager = false;
    }

    public void onDetach() {
        this.mCalled = true;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    public void onPrepareOptionsMenu(Menu menu) {
    }

    public void onDestroyOptionsMenu() {
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    public void onOptionsMenuClosed(Menu menu) {
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        getActivity().onCreateContextMenu(menu, v, menuInfo);
    }

    public void registerForContextMenu(View view) {
        view.setOnCreateContextMenuListener(this);
    }

    public void unregisterForContextMenu(View view) {
        view.setOnCreateContextMenuListener(null);
    }

    public boolean onContextItemSelected(MenuItem item) {
        return false;
    }

    public void setEnterSharedElementCallback(SharedElementCallback callback) {
        if (callback == null) {
            if (this.mAnimationInfo != null) {
                callback = SharedElementCallback.NULL_CALLBACK;
            } else {
                return;
            }
        }
        ensureAnimationInfo().mEnterTransitionCallback = callback;
    }

    public void setExitSharedElementCallback(SharedElementCallback callback) {
        if (callback == null) {
            if (this.mAnimationInfo != null) {
                callback = SharedElementCallback.NULL_CALLBACK;
            } else {
                return;
            }
        }
        ensureAnimationInfo().mExitTransitionCallback = callback;
    }

    public void setEnterTransition(Transition transition) {
        if (shouldChangeTransition(transition, null)) {
            ensureAnimationInfo().mEnterTransition = transition;
        }
    }

    public Transition getEnterTransition() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return null;
        }
        return animationInfo.mEnterTransition;
    }

    public void setReturnTransition(Transition transition) {
        if (shouldChangeTransition(transition, USE_DEFAULT_TRANSITION)) {
            ensureAnimationInfo().mReturnTransition = transition;
        }
    }

    public Transition getReturnTransition() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return null;
        }
        Transition enterTransition;
        if (animationInfo.mReturnTransition == USE_DEFAULT_TRANSITION) {
            enterTransition = getEnterTransition();
        } else {
            enterTransition = this.mAnimationInfo.mReturnTransition;
        }
        return enterTransition;
    }

    public void setExitTransition(Transition transition) {
        if (shouldChangeTransition(transition, null)) {
            ensureAnimationInfo().mExitTransition = transition;
        }
    }

    public Transition getExitTransition() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return null;
        }
        return animationInfo.mExitTransition;
    }

    public void setReenterTransition(Transition transition) {
        if (shouldChangeTransition(transition, USE_DEFAULT_TRANSITION)) {
            ensureAnimationInfo().mReenterTransition = transition;
        }
    }

    public Transition getReenterTransition() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return null;
        }
        Transition exitTransition;
        if (animationInfo.mReenterTransition == USE_DEFAULT_TRANSITION) {
            exitTransition = getExitTransition();
        } else {
            exitTransition = this.mAnimationInfo.mReenterTransition;
        }
        return exitTransition;
    }

    public void setSharedElementEnterTransition(Transition transition) {
        if (shouldChangeTransition(transition, null)) {
            ensureAnimationInfo().mSharedElementEnterTransition = transition;
        }
    }

    public Transition getSharedElementEnterTransition() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return null;
        }
        return animationInfo.mSharedElementEnterTransition;
    }

    public void setSharedElementReturnTransition(Transition transition) {
        if (shouldChangeTransition(transition, USE_DEFAULT_TRANSITION)) {
            ensureAnimationInfo().mSharedElementReturnTransition = transition;
        }
    }

    public Transition getSharedElementReturnTransition() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return null;
        }
        Transition sharedElementEnterTransition;
        if (animationInfo.mSharedElementReturnTransition == USE_DEFAULT_TRANSITION) {
            sharedElementEnterTransition = getSharedElementEnterTransition();
        } else {
            sharedElementEnterTransition = this.mAnimationInfo.mSharedElementReturnTransition;
        }
        return sharedElementEnterTransition;
    }

    public void setAllowEnterTransitionOverlap(boolean allow) {
        ensureAnimationInfo().mAllowEnterTransitionOverlap = Boolean.valueOf(allow);
    }

    public boolean getAllowEnterTransitionOverlap() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        return (animationInfo == null || animationInfo.mAllowEnterTransitionOverlap == null) ? true : this.mAnimationInfo.mAllowEnterTransitionOverlap.booleanValue();
    }

    public void setAllowReturnTransitionOverlap(boolean allow) {
        ensureAnimationInfo().mAllowReturnTransitionOverlap = Boolean.valueOf(allow);
    }

    public boolean getAllowReturnTransitionOverlap() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        return (animationInfo == null || animationInfo.mAllowReturnTransitionOverlap == null) ? true : this.mAnimationInfo.mAllowReturnTransitionOverlap.booleanValue();
    }

    public void postponeEnterTransition() {
        ensureAnimationInfo().mEnterTransitionPostponed = true;
    }

    public void startPostponedEnterTransition() {
        FragmentManagerImpl fragmentManagerImpl = this.mFragmentManager;
        if (fragmentManagerImpl == null || fragmentManagerImpl.mHost == null) {
            ensureAnimationInfo().mEnterTransitionPostponed = false;
        } else if (Looper.myLooper() != this.mFragmentManager.mHost.getHandler().getLooper()) {
            this.mFragmentManager.mHost.getHandler().postAtFrontOfQueue(new -$$Lambda$Fragment$m7ODa2MK0_rf4XCEL5JOn14G0h8(this));
        } else {
            callStartTransitionListener();
        }
    }

    private void callStartTransitionListener() {
        OnStartEnterTransitionListener listener;
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            listener = null;
        } else {
            animationInfo.mEnterTransitionPostponed = false;
            listener = animationInfo.mStartEnterTransitionListener;
            this.mAnimationInfo.mStartEnterTransitionListener = null;
        }
        if (listener != null) {
            listener.onStartEnterTransition();
        }
    }

    private boolean shouldChangeTransition(Transition transition, Transition defaultValue) {
        boolean z = true;
        if (transition != defaultValue) {
            return true;
        }
        if (this.mAnimationInfo == null) {
            z = false;
        }
        return z;
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        StringBuilder stringBuilder;
        writer.print(prefix);
        writer.print("mFragmentId=#");
        writer.print(Integer.toHexString(this.mFragmentId));
        writer.print(" mContainerId=#");
        writer.print(Integer.toHexString(this.mContainerId));
        writer.print(" mTag=");
        writer.println(this.mTag);
        writer.print(prefix);
        writer.print("mState=");
        writer.print(this.mState);
        writer.print(" mIndex=");
        writer.print(this.mIndex);
        writer.print(" mWho=");
        writer.print(this.mWho);
        writer.print(" mBackStackNesting=");
        writer.println(this.mBackStackNesting);
        writer.print(prefix);
        writer.print("mAdded=");
        writer.print(this.mAdded);
        writer.print(" mRemoving=");
        writer.print(this.mRemoving);
        writer.print(" mFromLayout=");
        writer.print(this.mFromLayout);
        writer.print(" mInLayout=");
        writer.println(this.mInLayout);
        writer.print(prefix);
        writer.print("mHidden=");
        writer.print(this.mHidden);
        writer.print(" mDetached=");
        writer.print(this.mDetached);
        writer.print(" mMenuVisible=");
        writer.print(this.mMenuVisible);
        writer.print(" mHasMenu=");
        writer.println(this.mHasMenu);
        writer.print(prefix);
        writer.print("mRetainInstance=");
        writer.print(this.mRetainInstance);
        writer.print(" mRetaining=");
        writer.print(this.mRetaining);
        writer.print(" mUserVisibleHint=");
        writer.println(this.mUserVisibleHint);
        if (this.mFragmentManager != null) {
            writer.print(prefix);
            writer.print("mFragmentManager=");
            writer.println(this.mFragmentManager);
        }
        if (this.mHost != null) {
            writer.print(prefix);
            writer.print("mHost=");
            writer.println(this.mHost);
        }
        if (this.mParentFragment != null) {
            writer.print(prefix);
            writer.print("mParentFragment=");
            writer.println(this.mParentFragment);
        }
        if (this.mArguments != null) {
            writer.print(prefix);
            writer.print("mArguments=");
            writer.println(this.mArguments);
        }
        if (this.mSavedFragmentState != null) {
            writer.print(prefix);
            writer.print("mSavedFragmentState=");
            writer.println(this.mSavedFragmentState);
        }
        if (this.mSavedViewState != null) {
            writer.print(prefix);
            writer.print("mSavedViewState=");
            writer.println(this.mSavedViewState);
        }
        if (this.mTarget != null) {
            writer.print(prefix);
            writer.print("mTarget=");
            writer.print(this.mTarget);
            writer.print(" mTargetRequestCode=");
            writer.println(this.mTargetRequestCode);
        }
        if (getNextAnim() != 0) {
            writer.print(prefix);
            writer.print("mNextAnim=");
            writer.println(getNextAnim());
        }
        if (this.mContainer != null) {
            writer.print(prefix);
            writer.print("mContainer=");
            writer.println(this.mContainer);
        }
        if (this.mView != null) {
            writer.print(prefix);
            writer.print("mView=");
            writer.println(this.mView);
        }
        if (getAnimatingAway() != null) {
            writer.print(prefix);
            writer.print("mAnimatingAway=");
            writer.println(getAnimatingAway());
            writer.print(prefix);
            writer.print("mStateAfterAnimating=");
            writer.println(getStateAfterAnimating());
        }
        String str = "  ";
        if (this.mLoaderManager != null) {
            writer.print(prefix);
            writer.println("Loader Manager:");
            LoaderManagerImpl loaderManagerImpl = this.mLoaderManager;
            stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append(str);
            loaderManagerImpl.dump(stringBuilder.toString(), fd, writer, args);
        }
        if (this.mChildFragmentManager != null) {
            writer.print(prefix);
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Child ");
            stringBuilder2.append(this.mChildFragmentManager);
            stringBuilder2.append(":");
            writer.println(stringBuilder2.toString());
            FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
            stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append(str);
            fragmentManagerImpl.dump(stringBuilder.toString(), fd, writer, args);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public Fragment findFragmentByWho(String who) {
        if (who.equals(this.mWho)) {
            return this;
        }
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            return fragmentManagerImpl.findFragmentByWho(who);
        }
        return null;
    }

    /* Access modifiers changed, original: 0000 */
    public void instantiateChildFragmentManager() {
        this.mChildFragmentManager = new FragmentManagerImpl();
        this.mChildFragmentManager.attachController(this.mHost, new FragmentContainer() {
            public <T extends View> T onFindViewById(int id) {
                if (Fragment.this.mView != null) {
                    return Fragment.this.mView.findViewById(id);
                }
                throw new IllegalStateException("Fragment does not have a view");
            }

            public boolean onHasView() {
                return Fragment.this.mView != null;
            }
        }, this);
    }

    /* Access modifiers changed, original: 0000 */
    public void performCreate(Bundle savedInstanceState) {
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.noteStateNotSaved();
        }
        this.mState = 1;
        this.mCalled = false;
        onCreate(savedInstanceState);
        this.mIsCreated = true;
        if (this.mCalled) {
            Context context = getContext();
            if ((context != null ? context.getApplicationInfo().targetSdkVersion : 0) < 24) {
                restoreChildFragmentState(savedInstanceState, false);
                return;
            }
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Fragment ");
        stringBuilder.append(this);
        stringBuilder.append(" did not call through to super.onCreate()");
        throw new SuperNotCalledException(stringBuilder.toString());
    }

    /* Access modifiers changed, original: 0000 */
    public View performCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.noteStateNotSaved();
        }
        this.mPerformedCreateView = true;
        return onCreateView(inflater, container, savedInstanceState);
    }

    /* Access modifiers changed, original: 0000 */
    public void performActivityCreated(Bundle savedInstanceState) {
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.noteStateNotSaved();
        }
        this.mState = 2;
        this.mCalled = false;
        onActivityCreated(savedInstanceState);
        if (this.mCalled) {
            fragmentManagerImpl = this.mChildFragmentManager;
            if (fragmentManagerImpl != null) {
                fragmentManagerImpl.dispatchActivityCreated();
                return;
            }
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Fragment ");
        stringBuilder.append(this);
        stringBuilder.append(" did not call through to super.onActivityCreated()");
        throw new SuperNotCalledException(stringBuilder.toString());
    }

    /* Access modifiers changed, original: 0000 */
    public void performStart() {
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.noteStateNotSaved();
            this.mChildFragmentManager.execPendingActions();
        }
        this.mState = 4;
        this.mCalled = false;
        onStart();
        if (this.mCalled) {
            fragmentManagerImpl = this.mChildFragmentManager;
            if (fragmentManagerImpl != null) {
                fragmentManagerImpl.dispatchStart();
            }
            LoaderManagerImpl loaderManagerImpl = this.mLoaderManager;
            if (loaderManagerImpl != null) {
                loaderManagerImpl.doReportStart();
                return;
            }
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Fragment ");
        stringBuilder.append(this);
        stringBuilder.append(" did not call through to super.onStart()");
        throw new SuperNotCalledException(stringBuilder.toString());
    }

    /* Access modifiers changed, original: 0000 */
    public void performResume() {
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.noteStateNotSaved();
            this.mChildFragmentManager.execPendingActions();
        }
        this.mState = 5;
        this.mCalled = false;
        onResume();
        if (this.mCalled) {
            fragmentManagerImpl = this.mChildFragmentManager;
            if (fragmentManagerImpl != null) {
                fragmentManagerImpl.dispatchResume();
                this.mChildFragmentManager.execPendingActions();
                return;
            }
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Fragment ");
        stringBuilder.append(this);
        stringBuilder.append(" did not call through to super.onResume()");
        throw new SuperNotCalledException(stringBuilder.toString());
    }

    /* Access modifiers changed, original: 0000 */
    public void noteStateNotSaved() {
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.noteStateNotSaved();
        }
    }

    /* Access modifiers changed, original: 0000 */
    @Deprecated
    public void performMultiWindowModeChanged(boolean isInMultiWindowMode) {
        onMultiWindowModeChanged(isInMultiWindowMode);
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.dispatchMultiWindowModeChanged(isInMultiWindowMode);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void performMultiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig) {
        onMultiWindowModeChanged(isInMultiWindowMode, newConfig);
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.dispatchMultiWindowModeChanged(isInMultiWindowMode, newConfig);
        }
    }

    /* Access modifiers changed, original: 0000 */
    @Deprecated
    public void performPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        onPictureInPictureModeChanged(isInPictureInPictureMode);
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.dispatchPictureInPictureModeChanged(isInPictureInPictureMode);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void performPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.dispatchPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void performConfigurationChanged(Configuration newConfig) {
        onConfigurationChanged(newConfig);
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.dispatchConfigurationChanged(newConfig);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void performLowMemory() {
        onLowMemory();
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.dispatchLowMemory();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void performTrimMemory(int level) {
        onTrimMemory(level);
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.dispatchTrimMemory(level);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean performCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        boolean show = false;
        if (this.mHidden) {
            return false;
        }
        if (this.mHasMenu && this.mMenuVisible) {
            show = true;
            onCreateOptionsMenu(menu, inflater);
        }
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            return show | fragmentManagerImpl.dispatchCreateOptionsMenu(menu, inflater);
        }
        return show;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean performPrepareOptionsMenu(Menu menu) {
        boolean show = false;
        if (this.mHidden) {
            return false;
        }
        if (this.mHasMenu && this.mMenuVisible) {
            show = true;
            onPrepareOptionsMenu(menu);
        }
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            return show | fragmentManagerImpl.dispatchPrepareOptionsMenu(menu);
        }
        return show;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean performOptionsItemSelected(MenuItem item) {
        if (!this.mHidden) {
            if (this.mHasMenu && this.mMenuVisible && onOptionsItemSelected(item)) {
                return true;
            }
            FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
            if (fragmentManagerImpl != null && fragmentManagerImpl.dispatchOptionsItemSelected(item)) {
                return true;
            }
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean performContextItemSelected(MenuItem item) {
        if (!this.mHidden) {
            if (onContextItemSelected(item)) {
                return true;
            }
            FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
            if (fragmentManagerImpl != null && fragmentManagerImpl.dispatchContextItemSelected(item)) {
                return true;
            }
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public void performOptionsMenuClosed(Menu menu) {
        if (!this.mHidden) {
            if (this.mHasMenu && this.mMenuVisible) {
                onOptionsMenuClosed(menu);
            }
            FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
            if (fragmentManagerImpl != null) {
                fragmentManagerImpl.dispatchOptionsMenuClosed(menu);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void performSaveInstanceState(Bundle outState) {
        onSaveInstanceState(outState);
        Parcelable p = this.mChildFragmentManager;
        if (p != null) {
            p = p.saveAllState();
            if (p != null) {
                outState.putParcelable("android:fragments", p);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void performPause() {
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.dispatchPause();
        }
        this.mState = 4;
        this.mCalled = false;
        onPause();
        if (!this.mCalled) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fragment ");
            stringBuilder.append(this);
            stringBuilder.append(" did not call through to super.onPause()");
            throw new SuperNotCalledException(stringBuilder.toString());
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void performStop() {
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.dispatchStop();
        }
        this.mState = 3;
        this.mCalled = false;
        onStop();
        if (!this.mCalled) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fragment ");
            stringBuilder.append(this);
            stringBuilder.append(" did not call through to super.onStop()");
            throw new SuperNotCalledException(stringBuilder.toString());
        } else if (this.mLoadersStarted) {
            this.mLoadersStarted = false;
            if (!this.mCheckedForLoaderManager) {
                this.mCheckedForLoaderManager = true;
                this.mLoaderManager = this.mHost.getLoaderManager(this.mWho, this.mLoadersStarted, false);
            }
            if (this.mLoaderManager == null) {
                return;
            }
            if (this.mHost.getRetainLoaders()) {
                this.mLoaderManager.doRetain();
            } else {
                this.mLoaderManager.doStop();
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void performDestroyView() {
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.dispatchDestroyView();
        }
        this.mState = 1;
        this.mCalled = false;
        onDestroyView();
        if (this.mCalled) {
            LoaderManagerImpl loaderManagerImpl = this.mLoaderManager;
            if (loaderManagerImpl != null) {
                loaderManagerImpl.doReportNextStart();
            }
            this.mPerformedCreateView = false;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Fragment ");
        stringBuilder.append(this);
        stringBuilder.append(" did not call through to super.onDestroyView()");
        throw new SuperNotCalledException(stringBuilder.toString());
    }

    /* Access modifiers changed, original: 0000 */
    public void performDestroy() {
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.dispatchDestroy();
        }
        this.mState = 0;
        this.mCalled = false;
        this.mIsCreated = false;
        onDestroy();
        if (this.mCalled) {
            this.mChildFragmentManager = null;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Fragment ");
        stringBuilder.append(this);
        stringBuilder.append(" did not call through to super.onDestroy()");
        throw new SuperNotCalledException(stringBuilder.toString());
    }

    /* Access modifiers changed, original: 0000 */
    public void performDetach() {
        this.mCalled = false;
        onDetach();
        this.mLayoutInflater = null;
        StringBuilder stringBuilder;
        if (this.mCalled) {
            FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
            if (fragmentManagerImpl == null) {
                return;
            }
            if (this.mRetaining) {
                fragmentManagerImpl.dispatchDestroy();
                this.mChildFragmentManager = null;
                return;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("Child FragmentManager of ");
            stringBuilder.append(this);
            stringBuilder.append(" was not  destroyed and this fragment is not retaining instance");
            throw new IllegalStateException(stringBuilder.toString());
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("Fragment ");
        stringBuilder.append(this);
        stringBuilder.append(" did not call through to super.onDetach()");
        throw new SuperNotCalledException(stringBuilder.toString());
    }

    /* Access modifiers changed, original: 0000 */
    public void setOnStartEnterTransitionListener(OnStartEnterTransitionListener listener) {
        ensureAnimationInfo();
        if (listener != this.mAnimationInfo.mStartEnterTransitionListener) {
            if (listener == null || this.mAnimationInfo.mStartEnterTransitionListener == null) {
                if (this.mAnimationInfo.mEnterTransitionPostponed) {
                    this.mAnimationInfo.mStartEnterTransitionListener = listener;
                }
                if (listener != null) {
                    listener.startListening();
                }
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Trying to set a replacement startPostponedEnterTransition on ");
            stringBuilder.append(this);
            throw new IllegalStateException(stringBuilder.toString());
        }
    }

    private static Transition loadTransition(Context context, TypedArray typedArray, Transition currentValue, Transition defaultValue, int id) {
        if (currentValue != defaultValue) {
            return currentValue;
        }
        int transitionId = typedArray.getResourceId(id, 0);
        Transition transition = defaultValue;
        if (!(transitionId == 0 || transitionId == 17760256)) {
            transition = TransitionInflater.from(context).inflateTransition(transitionId);
            if ((transition instanceof TransitionSet) && ((TransitionSet) transition).getTransitionCount() == 0) {
                transition = null;
            }
        }
        return transition;
    }

    private AnimationInfo ensureAnimationInfo() {
        if (this.mAnimationInfo == null) {
            this.mAnimationInfo = new AnimationInfo();
        }
        return this.mAnimationInfo;
    }

    /* Access modifiers changed, original: 0000 */
    public int getNextAnim() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return 0;
        }
        return animationInfo.mNextAnim;
    }

    /* Access modifiers changed, original: 0000 */
    public void setNextAnim(int animResourceId) {
        if (this.mAnimationInfo != null || animResourceId != 0) {
            ensureAnimationInfo().mNextAnim = animResourceId;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public int getNextTransition() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return 0;
        }
        return animationInfo.mNextTransition;
    }

    /* Access modifiers changed, original: 0000 */
    public void setNextTransition(int nextTransition, int nextTransitionStyle) {
        if (this.mAnimationInfo != null || nextTransition != 0 || nextTransitionStyle != 0) {
            ensureAnimationInfo();
            AnimationInfo animationInfo = this.mAnimationInfo;
            animationInfo.mNextTransition = nextTransition;
            animationInfo.mNextTransitionStyle = nextTransitionStyle;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public int getNextTransitionStyle() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return 0;
        }
        return animationInfo.mNextTransitionStyle;
    }

    /* Access modifiers changed, original: 0000 */
    public SharedElementCallback getEnterTransitionCallback() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return SharedElementCallback.NULL_CALLBACK;
        }
        return animationInfo.mEnterTransitionCallback;
    }

    /* Access modifiers changed, original: 0000 */
    public SharedElementCallback getExitTransitionCallback() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return SharedElementCallback.NULL_CALLBACK;
        }
        return animationInfo.mExitTransitionCallback;
    }

    /* Access modifiers changed, original: 0000 */
    public Animator getAnimatingAway() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return null;
        }
        return animationInfo.mAnimatingAway;
    }

    /* Access modifiers changed, original: 0000 */
    public void setAnimatingAway(Animator animator) {
        ensureAnimationInfo().mAnimatingAway = animator;
    }

    /* Access modifiers changed, original: 0000 */
    public int getStateAfterAnimating() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return 0;
        }
        return animationInfo.mStateAfterAnimating;
    }

    /* Access modifiers changed, original: 0000 */
    public void setStateAfterAnimating(int state) {
        ensureAnimationInfo().mStateAfterAnimating = state;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isPostponed() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return false;
        }
        return animationInfo.mEnterTransitionPostponed;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isHideReplaced() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return false;
        }
        return animationInfo.mIsHideReplaced;
    }

    /* Access modifiers changed, original: 0000 */
    public void setHideReplaced(boolean replaced) {
        ensureAnimationInfo().mIsHideReplaced = replaced;
    }
}
