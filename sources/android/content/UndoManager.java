package android.content;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.text.TextUtils;
import android.util.ArrayMap;
import java.util.ArrayList;

public class UndoManager {
    public static final int MERGE_MODE_ANY = 2;
    public static final int MERGE_MODE_NONE = 0;
    public static final int MERGE_MODE_UNIQUE = 1;
    private int mCommitId = 1;
    private int mHistorySize = 20;
    private boolean mInUndo;
    private boolean mMerged;
    private int mNextSavedIdx;
    private final ArrayMap<String, UndoOwner> mOwners = new ArrayMap(1);
    private final ArrayList<UndoState> mRedos = new ArrayList();
    private UndoOwner[] mStateOwners;
    private int mStateSeq;
    private final ArrayList<UndoState> mUndos = new ArrayList();
    private int mUpdateCount;
    private UndoState mWorking;

    static final class UndoState {
        private boolean mCanMerge;
        private final int mCommitId;
        private boolean mExecuted;
        private CharSequence mLabel;
        private final UndoManager mManager;
        private final ArrayList<UndoOperation<?>> mOperations;
        private ArrayList<UndoOperation<?>> mRecent;

        UndoState(UndoManager manager, int commitId) {
            this.mOperations = new ArrayList();
            this.mCanMerge = true;
            this.mManager = manager;
            this.mCommitId = commitId;
        }

        UndoState(UndoManager manager, Parcel p, ClassLoader loader) {
            this.mOperations = new ArrayList();
            boolean z = true;
            this.mCanMerge = true;
            this.mManager = manager;
            this.mCommitId = p.readInt();
            this.mCanMerge = p.readInt() != 0;
            if (p.readInt() == 0) {
                z = false;
            }
            this.mExecuted = z;
            this.mLabel = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(p);
            int N = p.readInt();
            for (int i = 0; i < N; i++) {
                UndoOperation op = (UndoOperation) p.readParcelable(loader);
                op.mOwner = this.mManager.restoreOwner(p);
                this.mOperations.add(op);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void writeToParcel(Parcel p) {
            if (this.mRecent == null) {
                p.writeInt(this.mCommitId);
                p.writeInt(this.mCanMerge);
                p.writeInt(this.mExecuted);
                TextUtils.writeToParcel(this.mLabel, p, 0);
                int N = this.mOperations.size();
                p.writeInt(N);
                for (int i = 0; i < N; i++) {
                    UndoOperation op = (UndoOperation) this.mOperations.get(i);
                    this.mManager.saveOwner(op.mOwner, p);
                    p.writeParcelable(op, 0);
                }
                return;
            }
            throw new IllegalStateException("Can't save state before committing");
        }

        /* Access modifiers changed, original: 0000 */
        public int getCommitId() {
            return this.mCommitId;
        }

        /* Access modifiers changed, original: 0000 */
        public void setLabel(CharSequence label) {
            this.mLabel = label;
        }

        /* Access modifiers changed, original: 0000 */
        public void updateLabel(CharSequence label) {
            if (this.mLabel != null) {
                this.mLabel = label;
            }
        }

        /* Access modifiers changed, original: 0000 */
        public CharSequence getLabel() {
            return this.mLabel;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean setCanMerge(boolean state) {
            if (state && this.mExecuted) {
                return false;
            }
            this.mCanMerge = state;
            return true;
        }

        /* Access modifiers changed, original: 0000 */
        public void makeExecuted() {
            this.mExecuted = true;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean canMerge() {
            return this.mCanMerge && !this.mExecuted;
        }

        /* Access modifiers changed, original: 0000 */
        public int countOperations() {
            return this.mOperations.size();
        }

        /* Access modifiers changed, original: 0000 */
        public boolean hasOperation(UndoOwner owner) {
            int N = this.mOperations.size();
            boolean z = false;
            if (owner == null) {
                if (N != 0) {
                    z = true;
                }
                return z;
            }
            for (int i = 0; i < N; i++) {
                if (((UndoOperation) this.mOperations.get(i)).getOwner() == owner) {
                    return true;
                }
            }
            return false;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean hasMultipleOwners() {
            int N = this.mOperations.size();
            if (N <= 1) {
                return false;
            }
            UndoOwner owner = ((UndoOperation) this.mOperations.get(0)).getOwner();
            for (int i = 1; i < N; i++) {
                if (((UndoOperation) this.mOperations.get(i)).getOwner() != owner) {
                    return true;
                }
            }
            return false;
        }

        /* Access modifiers changed, original: 0000 */
        public void addOperation(UndoOperation<?> op) {
            if (this.mOperations.contains(op)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Already holds ");
                stringBuilder.append(op);
                throw new IllegalStateException(stringBuilder.toString());
            }
            this.mOperations.add(op);
            if (this.mRecent == null) {
                this.mRecent = new ArrayList();
                this.mRecent.add(op);
            }
            UndoOwner undoOwner = op.mOwner;
            undoOwner.mOpCount++;
        }

        /* Access modifiers changed, original: 0000 */
        public <T extends UndoOperation> T getLastOperation(Class<T> clazz, UndoOwner owner) {
            int N = this.mOperations.size();
            T t = null;
            if (clazz == null && owner == null) {
                if (N > 0) {
                    t = (UndoOperation) this.mOperations.get(N - 1);
                }
                return t;
            }
            int i = N - 1;
            while (i >= 0) {
                UndoOperation<?> op = (UndoOperation) this.mOperations.get(i);
                if (owner != null && op.getOwner() != owner) {
                    i--;
                } else if (clazz == null || op.getClass() == clazz) {
                    return op;
                } else {
                    return null;
                }
            }
            return null;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean matchOwner(UndoOwner owner) {
            for (int i = this.mOperations.size() - 1; i >= 0; i--) {
                if (((UndoOperation) this.mOperations.get(i)).matchOwner(owner)) {
                    return true;
                }
            }
            return false;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean hasData() {
            for (int i = this.mOperations.size() - 1; i >= 0; i--) {
                if (((UndoOperation) this.mOperations.get(i)).hasData()) {
                    return true;
                }
            }
            return false;
        }

        /* Access modifiers changed, original: 0000 */
        public void commit() {
            ArrayList arrayList = this.mRecent;
            int N = arrayList != null ? arrayList.size() : 0;
            for (int i = 0; i < N; i++) {
                ((UndoOperation) this.mRecent.get(i)).commit();
            }
            this.mRecent = null;
        }

        /* Access modifiers changed, original: 0000 */
        public void undo() {
            for (int i = this.mOperations.size() - 1; i >= 0; i--) {
                ((UndoOperation) this.mOperations.get(i)).undo();
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void redo() {
            int N = this.mOperations.size();
            for (int i = 0; i < N; i++) {
                ((UndoOperation) this.mOperations.get(i)).redo();
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void destroy() {
            for (int i = this.mOperations.size() - 1; i >= 0; i--) {
                UndoOwner owner = ((UndoOperation) this.mOperations.get(i)).mOwner;
                owner.mOpCount--;
                if (owner.mOpCount <= 0) {
                    if (owner.mOpCount >= 0) {
                        this.mManager.removeOwner(owner);
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Underflow of op count on owner ");
                        stringBuilder.append(owner);
                        stringBuilder.append(" in op ");
                        stringBuilder.append(this.mOperations.get(i));
                        throw new IllegalStateException(stringBuilder.toString());
                    }
                }
            }
        }
    }

    @UnsupportedAppUsage
    public UndoOwner getOwner(String tag, Object data) {
        if (tag == null) {
            throw new NullPointerException("tag can't be null");
        } else if (data != null) {
            UndoOwner owner = (UndoOwner) this.mOwners.get(tag);
            if (owner != null) {
                if (owner.mData != data) {
                    if (owner.mData == null) {
                        owner.mData = data;
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Owner ");
                        stringBuilder.append(owner);
                        stringBuilder.append(" already exists with data ");
                        stringBuilder.append(owner.mData);
                        stringBuilder.append(" but giving different data ");
                        stringBuilder.append(data);
                        throw new IllegalStateException(stringBuilder.toString());
                    }
                }
                return owner;
            }
            owner = new UndoOwner(tag, this);
            owner.mData = data;
            this.mOwners.put(tag, owner);
            return owner;
        } else {
            throw new NullPointerException("data can't be null");
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void removeOwner(UndoOwner owner) {
    }

    @UnsupportedAppUsage
    public void saveInstanceState(Parcel p) {
        if (this.mUpdateCount <= 0) {
            this.mStateSeq++;
            if (this.mStateSeq <= 0) {
                this.mStateSeq = 0;
            }
            this.mNextSavedIdx = 0;
            p.writeInt(this.mHistorySize);
            p.writeInt(this.mOwners.size());
            int i = this.mUndos.size();
            while (i > 0) {
                p.writeInt(1);
                i--;
                ((UndoState) this.mUndos.get(i)).writeToParcel(p);
            }
            i = this.mRedos.size();
            while (i > 0) {
                p.writeInt(2);
                i--;
                ((UndoState) this.mRedos.get(i)).writeToParcel(p);
            }
            p.writeInt(0);
            return;
        }
        throw new IllegalStateException("Can't save state while updating");
    }

    /* Access modifiers changed, original: 0000 */
    public void saveOwner(UndoOwner owner, Parcel out) {
        int i = owner.mStateSeq;
        int i2 = this.mStateSeq;
        if (i == i2) {
            out.writeInt(owner.mSavedIdx);
            return;
        }
        owner.mStateSeq = i2;
        owner.mSavedIdx = this.mNextSavedIdx;
        out.writeInt(owner.mSavedIdx);
        out.writeString(owner.mTag);
        out.writeInt(owner.mOpCount);
        this.mNextSavedIdx++;
    }

    @UnsupportedAppUsage
    public void restoreInstanceState(Parcel p, ClassLoader loader) {
        if (this.mUpdateCount <= 0) {
            forgetUndos(null, -1);
            forgetRedos(null, -1);
            this.mHistorySize = p.readInt();
            this.mStateOwners = new UndoOwner[p.readInt()];
            while (true) {
                int readInt = p.readInt();
                int stype = readInt;
                if (readInt != 0) {
                    UndoState ustate = new UndoState(this, p, loader);
                    if (stype == 1) {
                        this.mUndos.add(0, ustate);
                    } else {
                        this.mRedos.add(0, ustate);
                    }
                } else {
                    return;
                }
            }
        }
        throw new IllegalStateException("Can't save state while updating");
    }

    /* Access modifiers changed, original: 0000 */
    public UndoOwner restoreOwner(Parcel in) {
        int idx = in.readInt();
        UndoOwner owner = this.mStateOwners[idx];
        if (owner != null) {
            return owner;
        }
        String tag = in.readString();
        int opCount = in.readInt();
        owner = new UndoOwner(tag, this);
        owner.mOpCount = opCount;
        this.mStateOwners[idx] = owner;
        this.mOwners.put(tag, owner);
        return owner;
    }

    public void setHistorySize(int size) {
        this.mHistorySize = size;
        if (this.mHistorySize >= 0 && countUndos(null) > this.mHistorySize) {
            forgetUndos(null, countUndos(null) - this.mHistorySize);
        }
    }

    public int getHistorySize() {
        return this.mHistorySize;
    }

    @UnsupportedAppUsage
    public int undo(UndoOwner[] owners, int count) {
        if (this.mWorking == null) {
            int num = 0;
            int i = -1;
            this.mInUndo = true;
            UndoState us = getTopUndo(null);
            if (us != null) {
                us.makeExecuted();
            }
            while (count > 0) {
                int findPrevState = findPrevState(this.mUndos, owners, i);
                i = findPrevState;
                if (findPrevState < 0) {
                    break;
                }
                UndoState state = (UndoState) this.mUndos.remove(i);
                state.undo();
                this.mRedos.add(state);
                count--;
                num++;
            }
            this.mInUndo = false;
            return num;
        }
        throw new IllegalStateException("Can't be called during an update");
    }

    @UnsupportedAppUsage
    public int redo(UndoOwner[] owners, int count) {
        if (this.mWorking == null) {
            int num = 0;
            int i = -1;
            this.mInUndo = true;
            while (count > 0) {
                int findPrevState = findPrevState(this.mRedos, owners, i);
                i = findPrevState;
                if (findPrevState < 0) {
                    break;
                }
                UndoState state = (UndoState) this.mRedos.remove(i);
                state.redo();
                this.mUndos.add(state);
                count--;
                num++;
            }
            this.mInUndo = false;
            return num;
        }
        throw new IllegalStateException("Can't be called during an update");
    }

    @UnsupportedAppUsage
    public boolean isInUndo() {
        return this.mInUndo;
    }

    @UnsupportedAppUsage
    public int forgetUndos(UndoOwner[] owners, int count) {
        if (count < 0) {
            count = this.mUndos.size();
        }
        int removed = 0;
        int i = 0;
        while (i < this.mUndos.size() && removed < count) {
            UndoState state = (UndoState) this.mUndos.get(i);
            if (count <= 0 || !matchOwners(state, owners)) {
                i++;
            } else {
                state.destroy();
                this.mUndos.remove(i);
                removed++;
            }
        }
        return removed;
    }

    @UnsupportedAppUsage
    public int forgetRedos(UndoOwner[] owners, int count) {
        if (count < 0) {
            count = this.mRedos.size();
        }
        int removed = 0;
        int i = 0;
        while (i < this.mRedos.size() && removed < count) {
            UndoState state = (UndoState) this.mRedos.get(i);
            if (count <= 0 || !matchOwners(state, owners)) {
                i++;
            } else {
                state.destroy();
                this.mRedos.remove(i);
                removed++;
            }
        }
        return removed;
    }

    @UnsupportedAppUsage
    public int countUndos(UndoOwner[] owners) {
        if (owners == null) {
            return this.mUndos.size();
        }
        int count = 0;
        int i = 0;
        while (true) {
            int findNextState = findNextState(this.mUndos, owners, i);
            i = findNextState;
            if (findNextState < 0) {
                return count;
            }
            count++;
            i++;
        }
    }

    @UnsupportedAppUsage
    public int countRedos(UndoOwner[] owners) {
        if (owners == null) {
            return this.mRedos.size();
        }
        int count = 0;
        int i = 0;
        while (true) {
            int findNextState = findNextState(this.mRedos, owners, i);
            i = findNextState;
            if (findNextState < 0) {
                return count;
            }
            count++;
            i++;
        }
    }

    public CharSequence getUndoLabel(UndoOwner[] owners) {
        UndoState state = getTopUndo(owners);
        return state != null ? state.getLabel() : null;
    }

    public CharSequence getRedoLabel(UndoOwner[] owners) {
        UndoState state = getTopRedo(owners);
        return state != null ? state.getLabel() : null;
    }

    @UnsupportedAppUsage
    public void beginUpdate(CharSequence label) {
        if (this.mInUndo) {
            throw new IllegalStateException("Can't being update while performing undo/redo");
        }
        if (this.mUpdateCount <= 0) {
            createWorkingState();
            this.mMerged = false;
            this.mUpdateCount = 0;
        }
        this.mWorking.updateLabel(label);
        this.mUpdateCount++;
    }

    private void createWorkingState() {
        int i = this.mCommitId;
        this.mCommitId = i + 1;
        this.mWorking = new UndoState(this, i);
        if (this.mCommitId < 0) {
            this.mCommitId = 1;
        }
    }

    public boolean isInUpdate() {
        return this.mUpdateCount > 0;
    }

    @UnsupportedAppUsage
    public void setUndoLabel(CharSequence label) {
        UndoState undoState = this.mWorking;
        if (undoState != null) {
            undoState.setLabel(label);
            return;
        }
        throw new IllegalStateException("Must be called during an update");
    }

    public void suggestUndoLabel(CharSequence label) {
        UndoState undoState = this.mWorking;
        if (undoState != null) {
            undoState.updateLabel(label);
            return;
        }
        throw new IllegalStateException("Must be called during an update");
    }

    public int getUpdateNestingLevel() {
        return this.mUpdateCount;
    }

    public boolean hasOperation(UndoOwner owner) {
        UndoState undoState = this.mWorking;
        if (undoState != null) {
            return undoState.hasOperation(owner);
        }
        throw new IllegalStateException("Must be called during an update");
    }

    public UndoOperation<?> getLastOperation(int mergeMode) {
        return getLastOperation(null, null, mergeMode);
    }

    public UndoOperation<?> getLastOperation(UndoOwner owner, int mergeMode) {
        return getLastOperation(null, owner, mergeMode);
    }

    @UnsupportedAppUsage
    public <T extends UndoOperation> T getLastOperation(Class<T> clazz, UndoOwner owner, int mergeMode) {
        UndoState undoState = this.mWorking;
        if (undoState != null) {
            if (!(mergeMode == 0 || this.mMerged || undoState.hasData())) {
                undoState = getTopUndo(null);
                if (undoState != null && ((mergeMode == 2 || !undoState.hasMultipleOwners()) && undoState.canMerge())) {
                    UndoOperation<?> lastOperation = undoState.getLastOperation(clazz, owner);
                    UndoOperation<?> last = lastOperation;
                    if (lastOperation != null && last.allowMerge()) {
                        this.mWorking.destroy();
                        this.mWorking = undoState;
                        this.mUndos.remove(undoState);
                        this.mMerged = true;
                        return last;
                    }
                }
            }
            return this.mWorking.getLastOperation(clazz, owner);
        }
        throw new IllegalStateException("Must be called during an update");
    }

    @UnsupportedAppUsage
    public void addOperation(UndoOperation<?> op, int mergeMode) {
        if (this.mWorking == null) {
            throw new IllegalStateException("Must be called during an update");
        } else if (op.getOwner().mManager == this) {
            if (!(mergeMode == 0 || this.mMerged || this.mWorking.hasData())) {
                UndoState state = getTopUndo(null);
                if (state != null && ((mergeMode == 2 || !state.hasMultipleOwners()) && state.canMerge() && state.hasOperation(op.getOwner()))) {
                    this.mWorking.destroy();
                    this.mWorking = state;
                    this.mUndos.remove(state);
                    this.mMerged = true;
                }
            }
            this.mWorking.addOperation(op);
        } else {
            throw new IllegalArgumentException("Given operation's owner is not in this undo manager.");
        }
    }

    @UnsupportedAppUsage
    public void endUpdate() {
        if (this.mWorking != null) {
            this.mUpdateCount--;
            if (this.mUpdateCount == 0) {
                pushWorkingState();
                return;
            }
            return;
        }
        throw new IllegalStateException("Must be called during an update");
    }

    private void pushWorkingState() {
        int N = this.mUndos.size() + 1;
        if (this.mWorking.hasData()) {
            this.mUndos.add(this.mWorking);
            forgetRedos(null, -1);
            this.mWorking.commit();
            if (N >= 2) {
                ((UndoState) this.mUndos.get(N - 2)).makeExecuted();
            }
        } else {
            this.mWorking.destroy();
        }
        this.mWorking = null;
        int i = this.mHistorySize;
        if (i >= 0 && N > i) {
            forgetUndos(null, N - i);
        }
    }

    @UnsupportedAppUsage
    public int commitState(UndoOwner owner) {
        UndoState undoState = this.mWorking;
        if (undoState == null || !undoState.hasData()) {
            undoState = getTopUndo(null);
            if (undoState != null && (owner == null || undoState.hasOperation(owner))) {
                undoState.setCanMerge(false);
                return undoState.getCommitId();
            }
        } else if (owner == null || this.mWorking.hasOperation(owner)) {
            this.mWorking.setCanMerge(false);
            int commitId = this.mWorking.getCommitId();
            pushWorkingState();
            createWorkingState();
            this.mMerged = true;
            return commitId;
        }
        return -1;
    }

    public boolean uncommitState(int commitId, UndoOwner owner) {
        UndoState undoState = this.mWorking;
        if (undoState == null || undoState.getCommitId() != commitId) {
            undoState = getTopUndo(null);
            if (undoState != null && ((owner == null || undoState.hasOperation(owner)) && undoState.getCommitId() == commitId)) {
                return undoState.setCanMerge(true);
            }
        } else if (owner == null || this.mWorking.hasOperation(owner)) {
            return this.mWorking.setCanMerge(true);
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public UndoState getTopUndo(UndoOwner[] owners) {
        UndoState undoState = null;
        if (this.mUndos.size() <= 0) {
            return null;
        }
        int i = findPrevState(this.mUndos, owners, -1);
        if (i >= 0) {
            undoState = (UndoState) this.mUndos.get(i);
        }
        return undoState;
    }

    /* Access modifiers changed, original: 0000 */
    public UndoState getTopRedo(UndoOwner[] owners) {
        UndoState undoState = null;
        if (this.mRedos.size() <= 0) {
            return null;
        }
        int i = findPrevState(this.mRedos, owners, -1);
        if (i >= 0) {
            undoState = (UndoState) this.mRedos.get(i);
        }
        return undoState;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean matchOwners(UndoState state, UndoOwner[] owners) {
        if (owners == null) {
            return true;
        }
        for (UndoOwner matchOwner : owners) {
            if (state.matchOwner(matchOwner)) {
                return true;
            }
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public int findPrevState(ArrayList<UndoState> states, UndoOwner[] owners, int from) {
        int N = states.size();
        if (from == -1) {
            from = N - 1;
        }
        if (from >= N) {
            return -1;
        }
        if (owners == null) {
            return from;
        }
        while (from >= 0) {
            if (matchOwners((UndoState) states.get(from), owners)) {
                return from;
            }
            from--;
        }
        return -1;
    }

    /* Access modifiers changed, original: 0000 */
    public int findNextState(ArrayList<UndoState> states, UndoOwner[] owners, int from) {
        int N = states.size();
        if (from < 0) {
            from = 0;
        }
        if (from >= N) {
            return -1;
        }
        if (owners == null) {
            return from;
        }
        while (from < N) {
            if (matchOwners((UndoState) states.get(from), owners)) {
                return from;
            }
            from++;
        }
        return -1;
    }
}
