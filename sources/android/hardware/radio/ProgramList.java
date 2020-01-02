package android.hardware.radio;

import android.annotation.SystemApi;
import android.hardware.radio.ProgramSelector.Identifier;
import android.hardware.radio.RadioManager.ProgramInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@SystemApi
public final class ProgramList implements AutoCloseable {
    private boolean mIsClosed = false;
    private boolean mIsComplete = false;
    private final List<ListCallback> mListCallbacks = new ArrayList();
    private final Object mLock = new Object();
    private OnCloseListener mOnCloseListener;
    private final List<OnCompleteListener> mOnCompleteListeners = new ArrayList();
    private final Map<Identifier, ProgramInfo> mPrograms = new HashMap();

    public interface OnCompleteListener {
        void onComplete();
    }

    interface OnCloseListener {
        void onClose();
    }

    public static abstract class ListCallback {
        public void onItemChanged(Identifier id) {
        }

        public void onItemRemoved(Identifier id) {
        }
    }

    public static final class Chunk implements Parcelable {
        public static final Creator<Chunk> CREATOR = new Creator<Chunk>() {
            public Chunk createFromParcel(Parcel in) {
                return new Chunk(in, null);
            }

            public Chunk[] newArray(int size) {
                return new Chunk[size];
            }
        };
        private final boolean mComplete;
        private final Set<ProgramInfo> mModified;
        private final boolean mPurge;
        private final Set<Identifier> mRemoved;

        /* synthetic */ Chunk(Parcel x0, AnonymousClass1 x1) {
            this(x0);
        }

        public Chunk(boolean purge, boolean complete, Set<ProgramInfo> modified, Set<Identifier> removed) {
            this.mPurge = purge;
            this.mComplete = complete;
            this.mModified = modified != null ? modified : Collections.emptySet();
            this.mRemoved = removed != null ? removed : Collections.emptySet();
        }

        private Chunk(Parcel in) {
            boolean z = true;
            this.mPurge = in.readByte() != (byte) 0;
            if (in.readByte() == (byte) 0) {
                z = false;
            }
            this.mComplete = z;
            this.mModified = Utils.createSet(in, ProgramInfo.CREATOR);
            this.mRemoved = Utils.createSet(in, Identifier.CREATOR);
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte((byte) this.mPurge);
            dest.writeByte((byte) this.mComplete);
            Utils.writeSet(dest, this.mModified);
            Utils.writeSet(dest, this.mRemoved);
        }

        public int describeContents() {
            return 0;
        }

        public boolean isPurge() {
            return this.mPurge;
        }

        public boolean isComplete() {
            return this.mComplete;
        }

        public Set<ProgramInfo> getModified() {
            return this.mModified;
        }

        public Set<Identifier> getRemoved() {
            return this.mRemoved;
        }
    }

    public static final class Filter implements Parcelable {
        public static final Creator<Filter> CREATOR = new Creator<Filter>() {
            public Filter createFromParcel(Parcel in) {
                return new Filter(in, null);
            }

            public Filter[] newArray(int size) {
                return new Filter[size];
            }
        };
        private final boolean mExcludeModifications;
        private final Set<Integer> mIdentifierTypes;
        private final Set<Identifier> mIdentifiers;
        private final boolean mIncludeCategories;
        private final Map<String, String> mVendorFilter;

        /* synthetic */ Filter(Parcel x0, AnonymousClass1 x1) {
            this(x0);
        }

        public Filter(Set<Integer> identifierTypes, Set<Identifier> identifiers, boolean includeCategories, boolean excludeModifications) {
            this.mIdentifierTypes = (Set) Objects.requireNonNull(identifierTypes);
            this.mIdentifiers = (Set) Objects.requireNonNull(identifiers);
            this.mIncludeCategories = includeCategories;
            this.mExcludeModifications = excludeModifications;
            this.mVendorFilter = null;
        }

        public Filter() {
            this.mIdentifierTypes = Collections.emptySet();
            this.mIdentifiers = Collections.emptySet();
            this.mIncludeCategories = false;
            this.mExcludeModifications = false;
            this.mVendorFilter = null;
        }

        public Filter(Map<String, String> vendorFilter) {
            this.mIdentifierTypes = Collections.emptySet();
            this.mIdentifiers = Collections.emptySet();
            this.mIncludeCategories = false;
            this.mExcludeModifications = false;
            this.mVendorFilter = vendorFilter;
        }

        private Filter(Parcel in) {
            this.mIdentifierTypes = Utils.createIntSet(in);
            this.mIdentifiers = Utils.createSet(in, Identifier.CREATOR);
            boolean z = true;
            this.mIncludeCategories = in.readByte() != (byte) 0;
            if (in.readByte() == (byte) 0) {
                z = false;
            }
            this.mExcludeModifications = z;
            this.mVendorFilter = Utils.readStringMap(in);
        }

        public void writeToParcel(Parcel dest, int flags) {
            Utils.writeIntSet(dest, this.mIdentifierTypes);
            Utils.writeSet(dest, this.mIdentifiers);
            dest.writeByte((byte) this.mIncludeCategories);
            dest.writeByte((byte) this.mExcludeModifications);
            Utils.writeStringMap(dest, this.mVendorFilter);
        }

        public int describeContents() {
            return 0;
        }

        public Map<String, String> getVendorFilter() {
            return this.mVendorFilter;
        }

        public Set<Integer> getIdentifierTypes() {
            return this.mIdentifierTypes;
        }

        public Set<Identifier> getIdentifiers() {
            return this.mIdentifiers;
        }

        public boolean areCategoriesIncluded() {
            return this.mIncludeCategories;
        }

        public boolean areModificationsExcluded() {
            return this.mExcludeModifications;
        }
    }

    ProgramList() {
    }

    public void registerListCallback(final Executor executor, final ListCallback callback) {
        registerListCallback(new ListCallback() {
            public void onItemChanged(Identifier id) {
                executor.execute(new -$$Lambda$ProgramList$1$DVvry5MfhR6n8H2EZn67rvuhllI(callback, id));
            }

            public void onItemRemoved(Identifier id) {
                executor.execute(new -$$Lambda$ProgramList$1$a_xWqo5pESOZhcJIWvpiCd2AXmY(callback, id));
            }
        });
    }

    public void registerListCallback(ListCallback callback) {
        synchronized (this.mLock) {
            if (this.mIsClosed) {
                return;
            }
            this.mListCallbacks.add((ListCallback) Objects.requireNonNull(callback));
        }
    }

    public void unregisterListCallback(ListCallback callback) {
        synchronized (this.mLock) {
            if (this.mIsClosed) {
                return;
            }
            this.mListCallbacks.remove(Objects.requireNonNull(callback));
        }
    }

    static /* synthetic */ void lambda$addOnCompleteListener$0(Executor executor, OnCompleteListener listener) {
        Objects.requireNonNull(listener);
        executor.execute(new -$$Lambda$1DA3e7WM2G0cVcFyFUhdDG0CYnw(listener));
    }

    public void addOnCompleteListener(Executor executor, OnCompleteListener listener) {
        addOnCompleteListener(new -$$Lambda$ProgramList$aDYMynqVdAUqeKXIxfNtN1u67zs(executor, listener));
    }

    /* JADX WARNING: Missing block: B:11:0x001c, code skipped:
            return;
     */
    public void addOnCompleteListener(android.hardware.radio.ProgramList.OnCompleteListener r4) {
        /*
        r3 = this;
        r0 = r3.mLock;
        monitor-enter(r0);
        r1 = r3.mIsClosed;	 Catch:{ all -> 0x001d }
        if (r1 == 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r0);	 Catch:{ all -> 0x001d }
        return;
    L_0x0009:
        r1 = r3.mOnCompleteListeners;	 Catch:{ all -> 0x001d }
        r2 = java.util.Objects.requireNonNull(r4);	 Catch:{ all -> 0x001d }
        r2 = (android.hardware.radio.ProgramList.OnCompleteListener) r2;	 Catch:{ all -> 0x001d }
        r1.add(r2);	 Catch:{ all -> 0x001d }
        r1 = r3.mIsComplete;	 Catch:{ all -> 0x001d }
        if (r1 == 0) goto L_0x001b;
    L_0x0018:
        r4.onComplete();	 Catch:{ all -> 0x001d }
    L_0x001b:
        monitor-exit(r0);	 Catch:{ all -> 0x001d }
        return;
    L_0x001d:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x001d }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.radio.ProgramList.addOnCompleteListener(android.hardware.radio.ProgramList$OnCompleteListener):void");
    }

    public void removeOnCompleteListener(OnCompleteListener listener) {
        synchronized (this.mLock) {
            if (this.mIsClosed) {
                return;
            }
            this.mOnCompleteListeners.remove(Objects.requireNonNull(listener));
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setOnCloseListener(OnCloseListener listener) {
        synchronized (this.mLock) {
            if (this.mOnCloseListener == null) {
                this.mOnCloseListener = listener;
            } else {
                throw new IllegalStateException("Close callback is already set");
            }
        }
    }

    /* JADX WARNING: Missing block: B:11:0x0028, code skipped:
            return;
     */
    public void close() {
        /*
        r2 = this;
        r0 = r2.mLock;
        monitor-enter(r0);
        r1 = r2.mIsClosed;	 Catch:{ all -> 0x0029 }
        if (r1 == 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r0);	 Catch:{ all -> 0x0029 }
        return;
    L_0x0009:
        r1 = 1;
        r2.mIsClosed = r1;	 Catch:{ all -> 0x0029 }
        r1 = r2.mPrograms;	 Catch:{ all -> 0x0029 }
        r1.clear();	 Catch:{ all -> 0x0029 }
        r1 = r2.mListCallbacks;	 Catch:{ all -> 0x0029 }
        r1.clear();	 Catch:{ all -> 0x0029 }
        r1 = r2.mOnCompleteListeners;	 Catch:{ all -> 0x0029 }
        r1.clear();	 Catch:{ all -> 0x0029 }
        r1 = r2.mOnCloseListener;	 Catch:{ all -> 0x0029 }
        if (r1 == 0) goto L_0x0027;
    L_0x001f:
        r1 = r2.mOnCloseListener;	 Catch:{ all -> 0x0029 }
        r1.onClose();	 Catch:{ all -> 0x0029 }
        r1 = 0;
        r2.mOnCloseListener = r1;	 Catch:{ all -> 0x0029 }
    L_0x0027:
        monitor-exit(r0);	 Catch:{ all -> 0x0029 }
        return;
    L_0x0029:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0029 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.radio.ProgramList.close():void");
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Missing block: B:14:0x005a, code skipped:
            return;
     */
    public void apply(android.hardware.radio.ProgramList.Chunk r4) {
        /*
        r3 = this;
        r0 = r3.mLock;
        monitor-enter(r0);
        r1 = r3.mIsClosed;	 Catch:{ all -> 0x005b }
        if (r1 == 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r0);	 Catch:{ all -> 0x005b }
        return;
    L_0x0009:
        r1 = 0;
        r3.mIsComplete = r1;	 Catch:{ all -> 0x005b }
        r1 = r4.isPurge();	 Catch:{ all -> 0x005b }
        if (r1 == 0) goto L_0x0029;
    L_0x0012:
        r1 = new java.util.HashSet;	 Catch:{ all -> 0x005b }
        r2 = r3.mPrograms;	 Catch:{ all -> 0x005b }
        r2 = r2.keySet();	 Catch:{ all -> 0x005b }
        r1.<init>(r2);	 Catch:{ all -> 0x005b }
        r1 = r1.stream();	 Catch:{ all -> 0x005b }
        r2 = new android.hardware.radio.-$$Lambda$ProgramList$F-JpTj3vYguKIUQbnLbTePTuqUE;	 Catch:{ all -> 0x005b }
        r2.<init>(r3);	 Catch:{ all -> 0x005b }
        r1.forEach(r2);	 Catch:{ all -> 0x005b }
    L_0x0029:
        r1 = r4.getRemoved();	 Catch:{ all -> 0x005b }
        r1 = r1.stream();	 Catch:{ all -> 0x005b }
        r2 = new android.hardware.radio.-$$Lambda$ProgramList$pKu0Zp5jwjix619hfB_Imj8Ke_g;	 Catch:{ all -> 0x005b }
        r2.<init>(r3);	 Catch:{ all -> 0x005b }
        r1.forEach(r2);	 Catch:{ all -> 0x005b }
        r1 = r4.getModified();	 Catch:{ all -> 0x005b }
        r1 = r1.stream();	 Catch:{ all -> 0x005b }
        r2 = new android.hardware.radio.-$$Lambda$ProgramList$eY050tMTgAcGV9hiWR-UDxhkfhw;	 Catch:{ all -> 0x005b }
        r2.<init>(r3);	 Catch:{ all -> 0x005b }
        r1.forEach(r2);	 Catch:{ all -> 0x005b }
        r1 = r4.isComplete();	 Catch:{ all -> 0x005b }
        if (r1 == 0) goto L_0x0059;
    L_0x004f:
        r1 = 1;
        r3.mIsComplete = r1;	 Catch:{ all -> 0x005b }
        r1 = r3.mOnCompleteListeners;	 Catch:{ all -> 0x005b }
        r2 = android.hardware.radio.-$$Lambda$ProgramList$GfCj9jJ5znxw2TV4c2uykq35dgI.INSTANCE;	 Catch:{ all -> 0x005b }
        r1.forEach(r2);	 Catch:{ all -> 0x005b }
    L_0x0059:
        monitor-exit(r0);	 Catch:{ all -> 0x005b }
        return;
    L_0x005b:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x005b }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.radio.ProgramList.apply(android.hardware.radio.ProgramList$Chunk):void");
    }

    public /* synthetic */ void lambda$apply$1$ProgramList(Identifier id) {
        removeLocked(id);
    }

    public /* synthetic */ void lambda$apply$2$ProgramList(Identifier id) {
        removeLocked(id);
    }

    public /* synthetic */ void lambda$apply$3$ProgramList(ProgramInfo info) {
        putLocked(info);
    }

    private void putLocked(ProgramInfo value) {
        this.mPrograms.put((Identifier) Objects.requireNonNull(value.getSelector().getPrimaryId()), value);
        this.mListCallbacks.forEach(new -$$Lambda$ProgramList$fDnoTVk5UB7qTfD9S7SYPcadYn0(value.getSelector().getPrimaryId()));
    }

    private void removeLocked(Identifier key) {
        ProgramInfo removed = (ProgramInfo) this.mPrograms.remove(Objects.requireNonNull(key));
        if (removed != null) {
            this.mListCallbacks.forEach(new -$$Lambda$ProgramList$fHYelmhnUsVTYl6dFj75fMqCjGs(removed.getSelector().getPrimaryId()));
        }
    }

    public List<ProgramInfo> toList() {
        List list;
        synchronized (this.mLock) {
            list = (List) this.mPrograms.values().stream().collect(Collectors.toList());
        }
        return list;
    }

    public ProgramInfo get(Identifier id) {
        ProgramInfo programInfo;
        synchronized (this.mLock) {
            programInfo = (ProgramInfo) this.mPrograms.get(Objects.requireNonNull(id));
        }
        return programInfo;
    }
}
