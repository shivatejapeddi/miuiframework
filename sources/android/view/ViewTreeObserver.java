package android.view;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public final class ViewTreeObserver {
    private static boolean sIllegalOnDrawModificationIsFatal;
    private boolean mAlive = true;
    private CopyOnWriteArray<Consumer<List<Rect>>> mGestureExclusionListeners;
    private boolean mInDispatchOnDraw;
    @UnsupportedAppUsage
    private CopyOnWriteArray<OnComputeInternalInsetsListener> mOnComputeInternalInsetsListeners;
    private ArrayList<OnDrawListener> mOnDrawListeners;
    private CopyOnWriteArrayList<OnEnterAnimationCompleteListener> mOnEnterAnimationCompleteListeners;
    private ArrayList<Runnable> mOnFrameCommitListeners;
    private CopyOnWriteArrayList<OnGlobalFocusChangeListener> mOnGlobalFocusListeners;
    @UnsupportedAppUsage
    private CopyOnWriteArray<OnGlobalLayoutListener> mOnGlobalLayoutListeners;
    private CopyOnWriteArray<OnPreDrawListener> mOnPreDrawListeners;
    @UnsupportedAppUsage
    private CopyOnWriteArray<OnScrollChangedListener> mOnScrollChangedListeners;
    @UnsupportedAppUsage
    private CopyOnWriteArrayList<OnTouchModeChangeListener> mOnTouchModeChangeListeners;
    private CopyOnWriteArrayList<OnWindowAttachListener> mOnWindowAttachListeners;
    private CopyOnWriteArrayList<OnWindowFocusChangeListener> mOnWindowFocusListeners;
    private CopyOnWriteArray<OnWindowShownListener> mOnWindowShownListeners;
    private boolean mWindowShown;

    public interface OnScrollChangedListener {
        void onScrollChanged();
    }

    public interface OnPreDrawListener {
        boolean onPreDraw();
    }

    static class CopyOnWriteArray<T> {
        private final Access<T> mAccess = new Access();
        private ArrayList<T> mData = new ArrayList();
        private ArrayList<T> mDataCopy;
        private boolean mStart;

        static class Access<T> {
            private ArrayList<T> mData;
            private int mSize;

            Access() {
            }

            /* Access modifiers changed, original: 0000 */
            public T get(int index) {
                return this.mData.get(index);
            }

            /* Access modifiers changed, original: 0000 */
            public int size() {
                return this.mSize;
            }
        }

        CopyOnWriteArray() {
        }

        private ArrayList<T> getArray() {
            if (!this.mStart) {
                return this.mData;
            }
            if (this.mDataCopy == null) {
                this.mDataCopy = new ArrayList(this.mData);
            }
            return this.mDataCopy;
        }

        /* Access modifiers changed, original: 0000 */
        public Access<T> start() {
            if (this.mStart) {
                throw new IllegalStateException("Iteration already started");
            }
            this.mStart = true;
            this.mDataCopy = null;
            this.mAccess.mData = this.mData;
            this.mAccess.mSize = this.mData.size();
            return this.mAccess;
        }

        /* Access modifiers changed, original: 0000 */
        public void end() {
            if (this.mStart) {
                this.mStart = false;
                ArrayList arrayList = this.mDataCopy;
                if (arrayList != null) {
                    this.mData = arrayList;
                    this.mAccess.mData.clear();
                    this.mAccess.mSize = 0;
                }
                this.mDataCopy = null;
                return;
            }
            throw new IllegalStateException("Iteration not started");
        }

        /* Access modifiers changed, original: 0000 */
        public int size() {
            return getArray().size();
        }

        /* Access modifiers changed, original: 0000 */
        public void add(T item) {
            getArray().add(item);
        }

        /* Access modifiers changed, original: 0000 */
        public void addAll(CopyOnWriteArray<T> array) {
            getArray().addAll(array.mData);
        }

        /* Access modifiers changed, original: 0000 */
        public void remove(T item) {
            getArray().remove(item);
        }

        /* Access modifiers changed, original: 0000 */
        public void clear() {
            getArray().clear();
        }
    }

    public static final class InternalInsetsInfo {
        public static final int TOUCHABLE_INSETS_CONTENT = 1;
        public static final int TOUCHABLE_INSETS_FRAME = 0;
        @UnsupportedAppUsage
        public static final int TOUCHABLE_INSETS_REGION = 3;
        public static final int TOUCHABLE_INSETS_VISIBLE = 2;
        @UnsupportedAppUsage
        public final Rect contentInsets = new Rect();
        @UnsupportedAppUsage
        int mTouchableInsets;
        @UnsupportedAppUsage
        public final Region touchableRegion = new Region();
        @UnsupportedAppUsage
        public final Rect visibleInsets = new Rect();

        @UnsupportedAppUsage
        public void setTouchableInsets(int val) {
            this.mTouchableInsets = val;
        }

        /* Access modifiers changed, original: 0000 */
        public void reset() {
            this.contentInsets.setEmpty();
            this.visibleInsets.setEmpty();
            this.touchableRegion.setEmpty();
            this.mTouchableInsets = 0;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean isEmpty() {
            return this.contentInsets.isEmpty() && this.visibleInsets.isEmpty() && this.touchableRegion.isEmpty() && this.mTouchableInsets == 0;
        }

        public int hashCode() {
            return (((((this.contentInsets.hashCode() * 31) + this.visibleInsets.hashCode()) * 31) + this.touchableRegion.hashCode()) * 31) + this.mTouchableInsets;
        }

        public boolean equals(Object o) {
            boolean z = true;
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            InternalInsetsInfo other = (InternalInsetsInfo) o;
            if (!(this.mTouchableInsets == other.mTouchableInsets && this.contentInsets.equals(other.contentInsets) && this.visibleInsets.equals(other.visibleInsets) && this.touchableRegion.equals(other.touchableRegion))) {
                z = false;
            }
            return z;
        }

        /* Access modifiers changed, original: 0000 */
        @UnsupportedAppUsage
        public void set(InternalInsetsInfo other) {
            this.contentInsets.set(other.contentInsets);
            this.visibleInsets.set(other.visibleInsets);
            this.touchableRegion.set(other.touchableRegion);
            this.mTouchableInsets = other.mTouchableInsets;
        }
    }

    public interface OnComputeInternalInsetsListener {
        void onComputeInternalInsets(InternalInsetsInfo internalInsetsInfo);
    }

    public interface OnDrawListener {
        void onDraw();
    }

    public interface OnEnterAnimationCompleteListener {
        void onEnterAnimationComplete();
    }

    public interface OnGlobalFocusChangeListener {
        void onGlobalFocusChanged(View view, View view2);
    }

    public interface OnGlobalLayoutListener {
        void onGlobalLayout();
    }

    public interface OnTouchModeChangeListener {
        void onTouchModeChanged(boolean z);
    }

    public interface OnWindowAttachListener {
        void onWindowAttached();

        void onWindowDetached();
    }

    public interface OnWindowFocusChangeListener {
        void onWindowFocusChanged(boolean z);
    }

    public interface OnWindowShownListener {
        void onWindowShown();
    }

    ViewTreeObserver(Context context) {
        boolean z = true;
        if (context.getApplicationInfo().targetSdkVersion < 26) {
            z = false;
        }
        sIllegalOnDrawModificationIsFatal = z;
    }

    /* Access modifiers changed, original: 0000 */
    public void merge(ViewTreeObserver observer) {
        CopyOnWriteArrayList copyOnWriteArrayList;
        CopyOnWriteArray copyOnWriteArray;
        CopyOnWriteArrayList copyOnWriteArrayList2 = observer.mOnWindowAttachListeners;
        if (copyOnWriteArrayList2 != null) {
            copyOnWriteArrayList = this.mOnWindowAttachListeners;
            if (copyOnWriteArrayList != null) {
                copyOnWriteArrayList.addAll(copyOnWriteArrayList2);
            } else {
                this.mOnWindowAttachListeners = copyOnWriteArrayList2;
            }
        }
        copyOnWriteArrayList2 = observer.mOnWindowFocusListeners;
        if (copyOnWriteArrayList2 != null) {
            copyOnWriteArrayList = this.mOnWindowFocusListeners;
            if (copyOnWriteArrayList != null) {
                copyOnWriteArrayList.addAll(copyOnWriteArrayList2);
            } else {
                this.mOnWindowFocusListeners = copyOnWriteArrayList2;
            }
        }
        copyOnWriteArrayList2 = observer.mOnGlobalFocusListeners;
        if (copyOnWriteArrayList2 != null) {
            copyOnWriteArrayList = this.mOnGlobalFocusListeners;
            if (copyOnWriteArrayList != null) {
                copyOnWriteArrayList.addAll(copyOnWriteArrayList2);
            } else {
                this.mOnGlobalFocusListeners = copyOnWriteArrayList2;
            }
        }
        CopyOnWriteArray copyOnWriteArray2 = observer.mOnGlobalLayoutListeners;
        if (copyOnWriteArray2 != null) {
            copyOnWriteArray = this.mOnGlobalLayoutListeners;
            if (copyOnWriteArray != null) {
                copyOnWriteArray.addAll(copyOnWriteArray2);
            } else {
                this.mOnGlobalLayoutListeners = copyOnWriteArray2;
            }
        }
        copyOnWriteArray2 = observer.mOnPreDrawListeners;
        if (copyOnWriteArray2 != null) {
            copyOnWriteArray = this.mOnPreDrawListeners;
            if (copyOnWriteArray != null) {
                copyOnWriteArray.addAll(copyOnWriteArray2);
            } else {
                this.mOnPreDrawListeners = copyOnWriteArray2;
            }
        }
        ArrayList arrayList = observer.mOnDrawListeners;
        if (arrayList != null) {
            ArrayList arrayList2 = this.mOnDrawListeners;
            if (arrayList2 != null) {
                arrayList2.addAll(arrayList);
            } else {
                this.mOnDrawListeners = arrayList;
            }
        }
        if (observer.mOnFrameCommitListeners != null) {
            arrayList = this.mOnFrameCommitListeners;
            if (arrayList != null) {
                arrayList.addAll(observer.captureFrameCommitCallbacks());
            } else {
                this.mOnFrameCommitListeners = observer.captureFrameCommitCallbacks();
            }
        }
        copyOnWriteArrayList2 = observer.mOnTouchModeChangeListeners;
        if (copyOnWriteArrayList2 != null) {
            copyOnWriteArrayList = this.mOnTouchModeChangeListeners;
            if (copyOnWriteArrayList != null) {
                copyOnWriteArrayList.addAll(copyOnWriteArrayList2);
            } else {
                this.mOnTouchModeChangeListeners = copyOnWriteArrayList2;
            }
        }
        copyOnWriteArray2 = observer.mOnComputeInternalInsetsListeners;
        if (copyOnWriteArray2 != null) {
            copyOnWriteArray = this.mOnComputeInternalInsetsListeners;
            if (copyOnWriteArray != null) {
                copyOnWriteArray.addAll(copyOnWriteArray2);
            } else {
                this.mOnComputeInternalInsetsListeners = copyOnWriteArray2;
            }
        }
        copyOnWriteArray2 = observer.mOnScrollChangedListeners;
        if (copyOnWriteArray2 != null) {
            copyOnWriteArray = this.mOnScrollChangedListeners;
            if (copyOnWriteArray != null) {
                copyOnWriteArray.addAll(copyOnWriteArray2);
            } else {
                this.mOnScrollChangedListeners = copyOnWriteArray2;
            }
        }
        copyOnWriteArray2 = observer.mOnWindowShownListeners;
        if (copyOnWriteArray2 != null) {
            copyOnWriteArray = this.mOnWindowShownListeners;
            if (copyOnWriteArray != null) {
                copyOnWriteArray.addAll(copyOnWriteArray2);
            } else {
                this.mOnWindowShownListeners = copyOnWriteArray2;
            }
        }
        copyOnWriteArray2 = observer.mGestureExclusionListeners;
        if (copyOnWriteArray2 != null) {
            copyOnWriteArray = this.mGestureExclusionListeners;
            if (copyOnWriteArray != null) {
                copyOnWriteArray.addAll(copyOnWriteArray2);
            } else {
                this.mGestureExclusionListeners = copyOnWriteArray2;
            }
        }
        observer.kill();
    }

    public void addOnWindowAttachListener(OnWindowAttachListener listener) {
        checkIsAlive();
        if (this.mOnWindowAttachListeners == null) {
            this.mOnWindowAttachListeners = new CopyOnWriteArrayList();
        }
        this.mOnWindowAttachListeners.add(listener);
    }

    public void removeOnWindowAttachListener(OnWindowAttachListener victim) {
        checkIsAlive();
        CopyOnWriteArrayList copyOnWriteArrayList = this.mOnWindowAttachListeners;
        if (copyOnWriteArrayList != null) {
            copyOnWriteArrayList.remove(victim);
        }
    }

    public void addOnWindowFocusChangeListener(OnWindowFocusChangeListener listener) {
        checkIsAlive();
        if (this.mOnWindowFocusListeners == null) {
            this.mOnWindowFocusListeners = new CopyOnWriteArrayList();
        }
        this.mOnWindowFocusListeners.add(listener);
    }

    public void removeOnWindowFocusChangeListener(OnWindowFocusChangeListener victim) {
        checkIsAlive();
        CopyOnWriteArrayList copyOnWriteArrayList = this.mOnWindowFocusListeners;
        if (copyOnWriteArrayList != null) {
            copyOnWriteArrayList.remove(victim);
        }
    }

    public void addOnGlobalFocusChangeListener(OnGlobalFocusChangeListener listener) {
        checkIsAlive();
        if (this.mOnGlobalFocusListeners == null) {
            this.mOnGlobalFocusListeners = new CopyOnWriteArrayList();
        }
        this.mOnGlobalFocusListeners.add(listener);
    }

    public void removeOnGlobalFocusChangeListener(OnGlobalFocusChangeListener victim) {
        checkIsAlive();
        CopyOnWriteArrayList copyOnWriteArrayList = this.mOnGlobalFocusListeners;
        if (copyOnWriteArrayList != null) {
            copyOnWriteArrayList.remove(victim);
        }
    }

    public void addOnGlobalLayoutListener(OnGlobalLayoutListener listener) {
        checkIsAlive();
        if (this.mOnGlobalLayoutListeners == null) {
            this.mOnGlobalLayoutListeners = new CopyOnWriteArray();
        }
        this.mOnGlobalLayoutListeners.add(listener);
    }

    @Deprecated
    public void removeGlobalOnLayoutListener(OnGlobalLayoutListener victim) {
        removeOnGlobalLayoutListener(victim);
    }

    public void removeOnGlobalLayoutListener(OnGlobalLayoutListener victim) {
        checkIsAlive();
        CopyOnWriteArray copyOnWriteArray = this.mOnGlobalLayoutListeners;
        if (copyOnWriteArray != null) {
            copyOnWriteArray.remove(victim);
        }
    }

    public void addOnPreDrawListener(OnPreDrawListener listener) {
        checkIsAlive();
        if (this.mOnPreDrawListeners == null) {
            this.mOnPreDrawListeners = new CopyOnWriteArray();
        }
        this.mOnPreDrawListeners.add(listener);
    }

    public void removeOnPreDrawListener(OnPreDrawListener victim) {
        checkIsAlive();
        CopyOnWriteArray copyOnWriteArray = this.mOnPreDrawListeners;
        if (copyOnWriteArray != null) {
            copyOnWriteArray.remove(victim);
        }
    }

    public void addOnWindowShownListener(OnWindowShownListener listener) {
        checkIsAlive();
        if (this.mOnWindowShownListeners == null) {
            this.mOnWindowShownListeners = new CopyOnWriteArray();
        }
        this.mOnWindowShownListeners.add(listener);
        if (this.mWindowShown) {
            listener.onWindowShown();
        }
    }

    public void removeOnWindowShownListener(OnWindowShownListener victim) {
        checkIsAlive();
        CopyOnWriteArray copyOnWriteArray = this.mOnWindowShownListeners;
        if (copyOnWriteArray != null) {
            copyOnWriteArray.remove(victim);
        }
    }

    public void addOnDrawListener(OnDrawListener listener) {
        checkIsAlive();
        if (this.mOnDrawListeners == null) {
            this.mOnDrawListeners = new ArrayList();
        }
        if (this.mInDispatchOnDraw) {
            IllegalStateException ex = new IllegalStateException("Cannot call addOnDrawListener inside of onDraw");
            if (sIllegalOnDrawModificationIsFatal) {
                throw ex;
            }
            Log.e("ViewTreeObserver", ex.getMessage(), ex);
        }
        this.mOnDrawListeners.add(listener);
    }

    public void removeOnDrawListener(OnDrawListener victim) {
        checkIsAlive();
        if (this.mOnDrawListeners != null) {
            if (this.mInDispatchOnDraw) {
                IllegalStateException ex = new IllegalStateException("Cannot call removeOnDrawListener inside of onDraw");
                if (sIllegalOnDrawModificationIsFatal) {
                    throw ex;
                }
                Log.e("ViewTreeObserver", ex.getMessage(), ex);
            }
            this.mOnDrawListeners.remove(victim);
        }
    }

    public void registerFrameCommitCallback(Runnable callback) {
        checkIsAlive();
        if (this.mOnFrameCommitListeners == null) {
            this.mOnFrameCommitListeners = new ArrayList();
        }
        this.mOnFrameCommitListeners.add(callback);
    }

    /* Access modifiers changed, original: 0000 */
    public ArrayList<Runnable> captureFrameCommitCallbacks() {
        ArrayList<Runnable> ret = this.mOnFrameCommitListeners;
        this.mOnFrameCommitListeners = null;
        return ret;
    }

    public boolean unregisterFrameCommitCallback(Runnable callback) {
        checkIsAlive();
        ArrayList arrayList = this.mOnFrameCommitListeners;
        if (arrayList == null) {
            return false;
        }
        return arrayList.remove(callback);
    }

    public void addOnScrollChangedListener(OnScrollChangedListener listener) {
        checkIsAlive();
        if (this.mOnScrollChangedListeners == null) {
            this.mOnScrollChangedListeners = new CopyOnWriteArray();
        }
        this.mOnScrollChangedListeners.add(listener);
    }

    public void removeOnScrollChangedListener(OnScrollChangedListener victim) {
        checkIsAlive();
        CopyOnWriteArray copyOnWriteArray = this.mOnScrollChangedListeners;
        if (copyOnWriteArray != null) {
            copyOnWriteArray.remove(victim);
        }
    }

    public void addOnTouchModeChangeListener(OnTouchModeChangeListener listener) {
        checkIsAlive();
        if (this.mOnTouchModeChangeListeners == null) {
            this.mOnTouchModeChangeListeners = new CopyOnWriteArrayList();
        }
        this.mOnTouchModeChangeListeners.add(listener);
    }

    public void removeOnTouchModeChangeListener(OnTouchModeChangeListener victim) {
        checkIsAlive();
        CopyOnWriteArrayList copyOnWriteArrayList = this.mOnTouchModeChangeListeners;
        if (copyOnWriteArrayList != null) {
            copyOnWriteArrayList.remove(victim);
        }
    }

    @UnsupportedAppUsage
    public void addOnComputeInternalInsetsListener(OnComputeInternalInsetsListener listener) {
        checkIsAlive();
        if (this.mOnComputeInternalInsetsListeners == null) {
            this.mOnComputeInternalInsetsListeners = new CopyOnWriteArray();
        }
        this.mOnComputeInternalInsetsListeners.add(listener);
    }

    @UnsupportedAppUsage
    public void removeOnComputeInternalInsetsListener(OnComputeInternalInsetsListener victim) {
        checkIsAlive();
        CopyOnWriteArray copyOnWriteArray = this.mOnComputeInternalInsetsListeners;
        if (copyOnWriteArray != null) {
            copyOnWriteArray.remove(victim);
        }
    }

    public void addOnEnterAnimationCompleteListener(OnEnterAnimationCompleteListener listener) {
        checkIsAlive();
        if (this.mOnEnterAnimationCompleteListeners == null) {
            this.mOnEnterAnimationCompleteListeners = new CopyOnWriteArrayList();
        }
        this.mOnEnterAnimationCompleteListeners.add(listener);
    }

    public void removeOnEnterAnimationCompleteListener(OnEnterAnimationCompleteListener listener) {
        checkIsAlive();
        CopyOnWriteArrayList copyOnWriteArrayList = this.mOnEnterAnimationCompleteListeners;
        if (copyOnWriteArrayList != null) {
            copyOnWriteArrayList.remove(listener);
        }
    }

    public void addOnSystemGestureExclusionRectsChangedListener(Consumer<List<Rect>> listener) {
        checkIsAlive();
        if (this.mGestureExclusionListeners == null) {
            this.mGestureExclusionListeners = new CopyOnWriteArray();
        }
        this.mGestureExclusionListeners.add(listener);
    }

    public void removeOnSystemGestureExclusionRectsChangedListener(Consumer<List<Rect>> listener) {
        checkIsAlive();
        CopyOnWriteArray copyOnWriteArray = this.mGestureExclusionListeners;
        if (copyOnWriteArray != null) {
            copyOnWriteArray.remove(listener);
        }
    }

    private void checkIsAlive() {
        if (!this.mAlive) {
            throw new IllegalStateException("This ViewTreeObserver is not alive, call getViewTreeObserver() again");
        }
    }

    public boolean isAlive() {
        return this.mAlive;
    }

    private void kill() {
        this.mAlive = false;
    }

    /* Access modifiers changed, original: final */
    public final void dispatchOnWindowAttachedChange(boolean attached) {
        CopyOnWriteArrayList<OnWindowAttachListener> listeners = this.mOnWindowAttachListeners;
        if (listeners != null && listeners.size() > 0) {
            Iterator it = listeners.iterator();
            while (it.hasNext()) {
                OnWindowAttachListener listener = (OnWindowAttachListener) it.next();
                if (attached) {
                    listener.onWindowAttached();
                } else {
                    listener.onWindowDetached();
                }
            }
        }
    }

    /* Access modifiers changed, original: final */
    public final void dispatchOnWindowFocusChange(boolean hasFocus) {
        CopyOnWriteArrayList<OnWindowFocusChangeListener> listeners = this.mOnWindowFocusListeners;
        if (listeners != null && listeners.size() > 0) {
            Iterator it = listeners.iterator();
            while (it.hasNext()) {
                ((OnWindowFocusChangeListener) it.next()).onWindowFocusChanged(hasFocus);
            }
        }
    }

    /* Access modifiers changed, original: final */
    @UnsupportedAppUsage
    public final void dispatchOnGlobalFocusChange(View oldFocus, View newFocus) {
        CopyOnWriteArrayList<OnGlobalFocusChangeListener> listeners = this.mOnGlobalFocusListeners;
        if (listeners != null && listeners.size() > 0) {
            Iterator it = listeners.iterator();
            while (it.hasNext()) {
                ((OnGlobalFocusChangeListener) it.next()).onGlobalFocusChanged(oldFocus, newFocus);
            }
        }
    }

    public final void dispatchOnGlobalLayout() {
        CopyOnWriteArray<OnGlobalLayoutListener> listeners = this.mOnGlobalLayoutListeners;
        if (listeners != null && listeners.size() > 0) {
            Access<OnGlobalLayoutListener> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    ((OnGlobalLayoutListener) access.get(i)).onGlobalLayout();
                }
            } finally {
                listeners.end();
            }
        }
    }

    /* Access modifiers changed, original: final */
    public final boolean hasOnPreDrawListeners() {
        CopyOnWriteArray copyOnWriteArray = this.mOnPreDrawListeners;
        return copyOnWriteArray != null && copyOnWriteArray.size() > 0;
    }

    public final boolean dispatchOnPreDraw() {
        boolean cancelDraw = false;
        CopyOnWriteArray<OnPreDrawListener> listeners = this.mOnPreDrawListeners;
        if (listeners != null && listeners.size() > 0) {
            Access<OnPreDrawListener> access = listeners.start();
            try {
                for (int i = 0; i < access.size(); i++) {
                    cancelDraw |= ((OnPreDrawListener) access.get(i)).onPreDraw() ^ 1;
                }
            } finally {
                listeners.end();
            }
        }
        return cancelDraw;
    }

    public final void dispatchOnWindowShown() {
        this.mWindowShown = true;
        CopyOnWriteArray<OnWindowShownListener> listeners = this.mOnWindowShownListeners;
        if (listeners != null && listeners.size() > 0) {
            Access<OnWindowShownListener> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    ((OnWindowShownListener) access.get(i)).onWindowShown();
                }
            } finally {
                listeners.end();
            }
        }
    }

    public final void dispatchOnDraw() {
        if (this.mOnDrawListeners != null) {
            this.mInDispatchOnDraw = true;
            ArrayList<OnDrawListener> listeners = this.mOnDrawListeners;
            int numListeners = listeners.size();
            for (int i = 0; i < numListeners; i++) {
                ((OnDrawListener) listeners.get(i)).onDraw();
            }
            this.mInDispatchOnDraw = false;
        }
    }

    /* Access modifiers changed, original: final */
    @UnsupportedAppUsage
    public final void dispatchOnTouchModeChanged(boolean inTouchMode) {
        CopyOnWriteArrayList<OnTouchModeChangeListener> listeners = this.mOnTouchModeChangeListeners;
        if (listeners != null && listeners.size() > 0) {
            Iterator it = listeners.iterator();
            while (it.hasNext()) {
                ((OnTouchModeChangeListener) it.next()).onTouchModeChanged(inTouchMode);
            }
        }
    }

    /* Access modifiers changed, original: final */
    @UnsupportedAppUsage
    public final void dispatchOnScrollChanged() {
        CopyOnWriteArray<OnScrollChangedListener> listeners = this.mOnScrollChangedListeners;
        if (listeners != null && listeners.size() > 0) {
            Access<OnScrollChangedListener> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    ((OnScrollChangedListener) access.get(i)).onScrollChanged();
                }
            } finally {
                listeners.end();
            }
        }
    }

    /* Access modifiers changed, original: final */
    @UnsupportedAppUsage
    public final boolean hasComputeInternalInsetsListeners() {
        CopyOnWriteArray<OnComputeInternalInsetsListener> listeners = this.mOnComputeInternalInsetsListeners;
        return listeners != null && listeners.size() > 0;
    }

    /* Access modifiers changed, original: final */
    @UnsupportedAppUsage
    public final void dispatchOnComputeInternalInsets(InternalInsetsInfo inoutInfo) {
        CopyOnWriteArray<OnComputeInternalInsetsListener> listeners = this.mOnComputeInternalInsetsListeners;
        if (listeners != null && listeners.size() > 0) {
            Access<OnComputeInternalInsetsListener> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    ((OnComputeInternalInsetsListener) access.get(i)).onComputeInternalInsets(inoutInfo);
                }
            } finally {
                listeners.end();
            }
        }
    }

    public final void dispatchOnEnterAnimationComplete() {
        CopyOnWriteArrayList<OnEnterAnimationCompleteListener> listeners = this.mOnEnterAnimationCompleteListeners;
        if (listeners != null && !listeners.isEmpty()) {
            Iterator it = listeners.iterator();
            while (it.hasNext()) {
                ((OnEnterAnimationCompleteListener) it.next()).onEnterAnimationComplete();
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchOnSystemGestureExclusionRectsChanged(List<Rect> rects) {
        CopyOnWriteArray<Consumer<List<Rect>>> listeners = this.mGestureExclusionListeners;
        if (listeners != null && listeners.size() > 0) {
            Access<Consumer<List<Rect>>> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    ((Consumer) access.get(i)).accept(rects);
                }
            } finally {
                listeners.end();
            }
        }
    }
}
