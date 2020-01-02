package android.os;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcelable.Creator;
import android.provider.Settings.Global;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class WorkSource implements Parcelable {
    public static final Creator<WorkSource> CREATOR = new Creator<WorkSource>() {
        public WorkSource createFromParcel(Parcel in) {
            return new WorkSource(in);
        }

        public WorkSource[] newArray(int size) {
            return new WorkSource[size];
        }
    };
    static final boolean DEBUG = false;
    static final String TAG = "WorkSource";
    static WorkSource sGoneWork;
    static WorkSource sNewbWork;
    static final WorkSource sTmpWorkSource = new WorkSource(0);
    private ArrayList<WorkChain> mChains;
    @UnsupportedAppUsage
    String[] mNames;
    @UnsupportedAppUsage
    int mNum;
    @UnsupportedAppUsage
    int[] mUids;

    @SystemApi
    public static final class WorkChain implements Parcelable {
        public static final Creator<WorkChain> CREATOR = new Creator<WorkChain>() {
            public WorkChain createFromParcel(Parcel in) {
                return new WorkChain(in, null);
            }

            public WorkChain[] newArray(int size) {
                return new WorkChain[size];
            }
        };
        private int mSize;
        private String[] mTags;
        private int[] mUids;

        /* synthetic */ WorkChain(Parcel x0, AnonymousClass1 x1) {
            this(x0);
        }

        public WorkChain() {
            this.mSize = 0;
            this.mUids = new int[4];
            this.mTags = new String[4];
        }

        @VisibleForTesting
        public WorkChain(WorkChain other) {
            this.mSize = other.mSize;
            this.mUids = (int[]) other.mUids.clone();
            this.mTags = (String[]) other.mTags.clone();
        }

        private WorkChain(Parcel in) {
            this.mSize = in.readInt();
            this.mUids = in.createIntArray();
            this.mTags = in.createStringArray();
        }

        public WorkChain addNode(int uid, String tag) {
            if (this.mSize == this.mUids.length) {
                resizeArrays();
            }
            int[] iArr = this.mUids;
            int i = this.mSize;
            iArr[i] = uid;
            this.mTags[i] = tag;
            this.mSize = i + 1;
            return this;
        }

        public int getAttributionUid() {
            return this.mSize > 0 ? this.mUids[0] : -1;
        }

        public String getAttributionTag() {
            String[] strArr = this.mTags;
            return strArr.length > 0 ? strArr[0] : null;
        }

        @VisibleForTesting
        public int[] getUids() {
            int i = this.mSize;
            int[] uids = new int[i];
            System.arraycopy(this.mUids, 0, uids, 0, i);
            return uids;
        }

        @VisibleForTesting
        public String[] getTags() {
            int i = this.mSize;
            String[] tags = new String[i];
            System.arraycopy(this.mTags, 0, tags, 0, i);
            return tags;
        }

        @VisibleForTesting
        public int getSize() {
            return this.mSize;
        }

        private void resizeArrays() {
            int i = this.mSize;
            int newSize = i * 2;
            int[] uids = new int[newSize];
            String[] tags = new String[newSize];
            System.arraycopy(this.mUids, 0, uids, 0, i);
            System.arraycopy(this.mTags, 0, tags, 0, this.mSize);
            this.mUids = uids;
            this.mTags = tags;
        }

        public String toString() {
            StringBuilder result = new StringBuilder("WorkChain{");
            for (int i = 0; i < this.mSize; i++) {
                String str = ", ";
                if (i != 0) {
                    result.append(str);
                }
                result.append("(");
                result.append(this.mUids[i]);
                if (this.mTags[i] != null) {
                    result.append(str);
                    result.append(this.mTags[i]);
                }
                result.append(")");
            }
            result.append("}");
            return result.toString();
        }

        public int hashCode() {
            return ((this.mSize + (Arrays.hashCode(this.mUids) * 31)) * 31) + Arrays.hashCode(this.mTags);
        }

        public boolean equals(Object o) {
            boolean z = false;
            if (!(o instanceof WorkChain)) {
                return false;
            }
            WorkChain other = (WorkChain) o;
            if (this.mSize == other.mSize && Arrays.equals(this.mUids, other.mUids) && Arrays.equals(this.mTags, other.mTags)) {
                z = true;
            }
            return z;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mSize);
            dest.writeIntArray(this.mUids);
            dest.writeStringArray(this.mTags);
        }
    }

    public WorkSource() {
        this.mNum = 0;
        this.mChains = null;
    }

    public WorkSource(WorkSource orig) {
        if (orig == null) {
            this.mNum = 0;
            this.mChains = null;
            return;
        }
        this.mNum = orig.mNum;
        int[] iArr = orig.mUids;
        if (iArr != null) {
            this.mUids = (int[]) iArr.clone();
            String[] strArr = orig.mNames;
            this.mNames = strArr != null ? (String[]) strArr.clone() : null;
        } else {
            this.mUids = null;
            this.mNames = null;
        }
        ArrayList arrayList = orig.mChains;
        if (arrayList != null) {
            this.mChains = new ArrayList(arrayList.size());
            Iterator it = orig.mChains.iterator();
            while (it.hasNext()) {
                this.mChains.add(new WorkChain((WorkChain) it.next()));
            }
        } else {
            this.mChains = null;
        }
    }

    public WorkSource(int uid) {
        this.mNum = 1;
        this.mUids = new int[]{uid, 0};
        this.mNames = null;
        this.mChains = null;
    }

    public WorkSource(int uid, String name) {
        if (name != null) {
            this.mNum = 1;
            this.mUids = new int[]{uid, 0};
            this.mNames = new String[]{name, null};
            this.mChains = null;
            return;
        }
        throw new NullPointerException("Name can't be null");
    }

    @UnsupportedAppUsage
    WorkSource(Parcel in) {
        this.mNum = in.readInt();
        this.mUids = in.createIntArray();
        this.mNames = in.createStringArray();
        int numChains = in.readInt();
        if (numChains > 0) {
            this.mChains = new ArrayList(numChains);
            in.readParcelableList(this.mChains, WorkChain.class.getClassLoader());
            return;
        }
        this.mChains = null;
    }

    public static boolean isChainedBatteryAttributionEnabled(Context context) {
        return Global.getInt(context.getContentResolver(), Global.CHAINED_BATTERY_ATTRIBUTION_ENABLED, 0) == 1;
    }

    public int size() {
        return this.mNum;
    }

    public int get(int index) {
        return this.mUids[index];
    }

    public int getAttributionUid() {
        if (isEmpty()) {
            return -1;
        }
        return this.mNum > 0 ? this.mUids[0] : ((WorkChain) this.mChains.get(0)).getAttributionUid();
    }

    public String getName(int index) {
        String[] strArr = this.mNames;
        return strArr != null ? strArr[index] : null;
    }

    public void clearNames() {
        if (this.mNames != null) {
            this.mNames = null;
            int destIndex = 1;
            int newNum = this.mNum;
            for (int sourceIndex = 1; sourceIndex < this.mNum; sourceIndex++) {
                int[] iArr = this.mUids;
                if (iArr[sourceIndex] == iArr[sourceIndex - 1]) {
                    newNum--;
                } else {
                    iArr[destIndex] = iArr[sourceIndex];
                    destIndex++;
                }
            }
            this.mNum = newNum;
        }
    }

    public void clear() {
        this.mNum = 0;
        ArrayList arrayList = this.mChains;
        if (arrayList != null) {
            arrayList.clear();
        }
    }

    public boolean equals(Object o) {
        boolean z = false;
        if (!(o instanceof WorkSource)) {
            return false;
        }
        WorkSource other = (WorkSource) o;
        if (diff(other)) {
            return false;
        }
        ArrayList arrayList = this.mChains;
        if (arrayList != null && !arrayList.isEmpty()) {
            return this.mChains.equals(other.mChains);
        }
        arrayList = other.mChains;
        if (arrayList == null || arrayList.isEmpty()) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        int i;
        int result = 0;
        for (i = 0; i < this.mNum; i++) {
            result = ((result << 4) | (result >>> 28)) ^ this.mUids[i];
        }
        if (this.mNames != null) {
            for (i = 0; i < this.mNum; i++) {
                result = ((result << 4) | (result >>> 28)) ^ this.mNames[i].hashCode();
            }
        }
        ArrayList arrayList = this.mChains;
        if (arrayList != null) {
            return ((result << 4) | (result >>> 28)) ^ arrayList.hashCode();
        }
        return result;
    }

    public boolean diff(WorkSource other) {
        int N = this.mNum;
        if (N != other.mNum) {
            return true;
        }
        int[] uids1 = this.mUids;
        int[] uids2 = other.mUids;
        String[] names1 = this.mNames;
        String[] names2 = other.mNames;
        int i = 0;
        while (i < N) {
            if (uids1[i] != uids2[i]) {
                return true;
            }
            if (names1 != null && names2 != null && !names1[i].equals(names2[i])) {
                return true;
            }
            i++;
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0049  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0031  */
    public void set(android.os.WorkSource r7) {
        /*
        r6 = this;
        r0 = 0;
        if (r7 != 0) goto L_0x000d;
    L_0x0003:
        r6.mNum = r0;
        r0 = r6.mChains;
        if (r0 == 0) goto L_0x000c;
    L_0x0009:
        r0.clear();
    L_0x000c:
        return;
    L_0x000d:
        r1 = r7.mNum;
        r6.mNum = r1;
        r1 = r7.mUids;
        r2 = 0;
        if (r1 == 0) goto L_0x004c;
    L_0x0016:
        r3 = r6.mUids;
        if (r3 == 0) goto L_0x0023;
    L_0x001a:
        r4 = r3.length;
        r5 = r6.mNum;
        if (r4 < r5) goto L_0x0023;
    L_0x001f:
        java.lang.System.arraycopy(r1, r0, r3, r0, r5);
        goto L_0x002d;
    L_0x0023:
        r1 = r7.mUids;
        r1 = r1.clone();
        r1 = (int[]) r1;
        r6.mUids = r1;
    L_0x002d:
        r1 = r7.mNames;
        if (r1 == 0) goto L_0x0049;
    L_0x0031:
        r2 = r6.mNames;
        if (r2 == 0) goto L_0x003e;
    L_0x0035:
        r3 = r2.length;
        r4 = r6.mNum;
        if (r3 < r4) goto L_0x003e;
    L_0x003a:
        java.lang.System.arraycopy(r1, r0, r2, r0, r4);
        goto L_0x0050;
    L_0x003e:
        r0 = r7.mNames;
        r0 = r0.clone();
        r0 = (java.lang.String[]) r0;
        r6.mNames = r0;
        goto L_0x0050;
    L_0x0049:
        r6.mNames = r2;
        goto L_0x0050;
    L_0x004c:
        r6.mUids = r2;
        r6.mNames = r2;
    L_0x0050:
        r0 = r7.mChains;
        if (r0 == 0) goto L_0x0084;
    L_0x0054:
        r1 = r6.mChains;
        if (r1 == 0) goto L_0x005c;
    L_0x0058:
        r1.clear();
        goto L_0x0067;
    L_0x005c:
        r1 = new java.util.ArrayList;
        r0 = r0.size();
        r1.<init>(r0);
        r6.mChains = r1;
    L_0x0067:
        r0 = r7.mChains;
        r0 = r0.iterator();
    L_0x006d:
        r1 = r0.hasNext();
        if (r1 == 0) goto L_0x0084;
    L_0x0073:
        r1 = r0.next();
        r1 = (android.os.WorkSource.WorkChain) r1;
        r2 = r6.mChains;
        r3 = new android.os.WorkSource$WorkChain;
        r3.<init>(r1);
        r2.add(r3);
        goto L_0x006d;
    L_0x0084:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.WorkSource.set(android.os.WorkSource):void");
    }

    public void set(int uid) {
        this.mNum = 1;
        if (this.mUids == null) {
            this.mUids = new int[2];
        }
        this.mUids[0] = uid;
        this.mNames = null;
        ArrayList arrayList = this.mChains;
        if (arrayList != null) {
            arrayList.clear();
        }
    }

    public void set(int uid, String name) {
        if (name != null) {
            this.mNum = 1;
            if (this.mUids == null) {
                this.mUids = new int[2];
                this.mNames = new String[2];
            }
            this.mUids[0] = uid;
            this.mNames[0] = name;
            ArrayList arrayList = this.mChains;
            if (arrayList != null) {
                arrayList.clear();
                return;
            }
            return;
        }
        throw new NullPointerException("Name can't be null");
    }

    @Deprecated
    public WorkSource[] setReturningDiffs(WorkSource other) {
        synchronized (sTmpWorkSource) {
            sNewbWork = null;
            sGoneWork = null;
            updateLocked(other, true, true);
            if (sNewbWork == null) {
                if (sGoneWork == null) {
                    return null;
                }
            }
            WorkSource[] diffs = new WorkSource[]{sNewbWork, sGoneWork};
            return diffs;
        }
    }

    public boolean add(WorkSource other) {
        boolean z;
        synchronized (sTmpWorkSource) {
            z = false;
            boolean uidAdded = updateLocked(other, false, false);
            if (other.mChains != null) {
                if (this.mChains == null) {
                    this.mChains = new ArrayList(other.mChains.size());
                }
                Iterator it = other.mChains.iterator();
                while (it.hasNext()) {
                    WorkChain wc = (WorkChain) it.next();
                    if (!this.mChains.contains(wc)) {
                        this.mChains.add(new WorkChain(wc));
                    }
                }
            }
            if (uidAdded || false) {
                z = true;
            }
        }
        return z;
    }

    @Deprecated
    public WorkSource addReturningNewbs(WorkSource other) {
        WorkSource workSource;
        synchronized (sTmpWorkSource) {
            sNewbWork = null;
            updateLocked(other, false, true);
            workSource = sNewbWork;
        }
        return workSource;
    }

    public boolean add(int uid) {
        int i = this.mNum;
        if (i <= 0) {
            this.mNames = null;
            insert(0, uid);
            return true;
        } else if (this.mNames == null) {
            i = Arrays.binarySearch(this.mUids, 0, i, uid);
            if (i >= 0) {
                return false;
            }
            insert((-i) - 1, uid);
            return true;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Adding without name to named ");
            stringBuilder.append(this);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public boolean add(int uid, String name) {
        if (this.mNum <= 0) {
            insert(0, uid, name);
            return true;
        } else if (this.mNames != null) {
            int i = 0;
            while (i < this.mNum) {
                int[] iArr = this.mUids;
                if (iArr[i] > uid) {
                    break;
                }
                if (iArr[i] == uid) {
                    int diff = this.mNames[i].compareTo(name);
                    if (diff > 0) {
                        break;
                    } else if (diff == 0) {
                        return false;
                    }
                }
                i++;
            }
            insert(i, uid, name);
            return true;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Adding name to unnamed ");
            stringBuilder.append(this);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public boolean remove(WorkSource other) {
        boolean z = false;
        if (isEmpty() || other.isEmpty()) {
            return false;
        }
        boolean uidRemoved;
        if (this.mNames == null && other.mNames == null) {
            uidRemoved = removeUids(other);
        } else {
            String str = " does not";
            StringBuilder stringBuilder;
            if (this.mNames == null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Other ");
                stringBuilder.append(other);
                stringBuilder.append(" has names, but target ");
                stringBuilder.append(this);
                stringBuilder.append(str);
                throw new IllegalArgumentException(stringBuilder.toString());
            } else if (other.mNames != null) {
                uidRemoved = removeUidsAndNames(other);
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Target ");
                stringBuilder.append(this);
                stringBuilder.append(" has names, but other ");
                stringBuilder.append(other);
                stringBuilder.append(str);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }
        boolean chainRemoved = false;
        ArrayList arrayList = other.mChains;
        if (arrayList != null) {
            ArrayList arrayList2 = this.mChains;
            if (arrayList2 != null) {
                chainRemoved = arrayList2.removeAll(arrayList);
            }
        }
        if (uidRemoved || chainRemoved) {
            z = true;
        }
        return z;
    }

    @SystemApi
    public WorkChain createWorkChain() {
        if (this.mChains == null) {
            this.mChains = new ArrayList(4);
        }
        WorkChain wc = new WorkChain();
        this.mChains.add(wc);
        return wc;
    }

    public boolean isEmpty() {
        if (this.mNum == 0) {
            ArrayList arrayList = this.mChains;
            if (arrayList == null || arrayList.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<WorkChain> getWorkChains() {
        return this.mChains;
    }

    public void transferWorkChains(WorkSource other) {
        ArrayList arrayList = this.mChains;
        if (arrayList != null) {
            arrayList.clear();
        }
        arrayList = other.mChains;
        if (arrayList != null && !arrayList.isEmpty()) {
            if (this.mChains == null) {
                this.mChains = new ArrayList(4);
            }
            this.mChains.addAll(other.mChains);
            other.mChains.clear();
        }
    }

    private boolean removeUids(WorkSource other) {
        int N1 = this.mNum;
        int[] uids1 = this.mUids;
        int N2 = other.mNum;
        int[] uids2 = other.mUids;
        boolean changed = false;
        int i1 = 0;
        int i2 = 0;
        while (i1 < N1 && i2 < N2) {
            if (uids2[i2] == uids1[i1]) {
                N1--;
                changed = true;
                if (i1 < N1) {
                    System.arraycopy(uids1, i1 + 1, uids1, i1, N1 - i1);
                }
                i2++;
            } else if (uids2[i2] > uids1[i1]) {
                i1++;
            } else {
                i2++;
            }
        }
        this.mNum = N1;
        return changed;
    }

    private boolean removeUidsAndNames(WorkSource other) {
        int N1 = this.mNum;
        int[] uids1 = this.mUids;
        String[] names1 = this.mNames;
        int N2 = other.mNum;
        int[] uids2 = other.mUids;
        String[] names2 = other.mNames;
        boolean changed = false;
        int i1 = 0;
        int i2 = 0;
        while (i1 < N1 && i2 < N2) {
            if (uids2[i2] == uids1[i1] && names2[i2].equals(names1[i1])) {
                N1--;
                changed = true;
                if (i1 < N1) {
                    System.arraycopy(uids1, i1 + 1, uids1, i1, N1 - i1);
                    System.arraycopy(names1, i1 + 1, names1, i1, N1 - i1);
                }
                i2++;
            } else if (uids2[i2] > uids1[i1] || (uids2[i2] == uids1[i1] && names2[i2].compareTo(names1[i1]) > 0)) {
                i1++;
            } else {
                i2++;
            }
        }
        this.mNum = N1;
        return changed;
    }

    private boolean updateLocked(WorkSource other, boolean set, boolean returnNewbs) {
        if (this.mNames == null && other.mNames == null) {
            return updateUidsLocked(other, set, returnNewbs);
        }
        String str = " does not";
        StringBuilder stringBuilder;
        if (this.mNum > 0 && this.mNames == null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Other ");
            stringBuilder.append(other);
            stringBuilder.append(" has names, but target ");
            stringBuilder.append(this);
            stringBuilder.append(str);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (other.mNum <= 0 || other.mNames != null) {
            return updateUidsAndNamesLocked(other, set, returnNewbs);
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Target ");
            stringBuilder.append(this);
            stringBuilder.append(" has names, but other ");
            stringBuilder.append(other);
            stringBuilder.append(str);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    private static WorkSource addWork(WorkSource cur, int newUid) {
        if (cur == null) {
            return new WorkSource(newUid);
        }
        cur.insert(cur.mNum, newUid);
        return cur;
    }

    private boolean updateUidsLocked(WorkSource other, boolean set, boolean returnNewbs) {
        int N1 = this.mNum;
        int[] uids1 = this.mUids;
        int N2 = other.mNum;
        int[] uids2 = other.mUids;
        boolean changed = false;
        int i1 = 0;
        int i2 = 0;
        while (true) {
            if (i1 >= N1 && i2 >= N2) {
                this.mNum = N1;
                this.mUids = uids1;
                return changed;
            } else if (i1 >= N1 || (i2 < N2 && uids2[i2] < uids1[i1])) {
                changed = true;
                if (uids1 == null) {
                    uids1 = new int[4];
                    uids1[0] = uids2[i2];
                } else if (N1 >= uids1.length) {
                    int[] newuids = new int[((uids1.length * 3) / 2)];
                    if (i1 > 0) {
                        System.arraycopy(uids1, 0, newuids, 0, i1);
                    }
                    if (i1 < N1) {
                        System.arraycopy(uids1, i1, newuids, i1 + 1, N1 - i1);
                    }
                    uids1 = newuids;
                    uids1[i1] = uids2[i2];
                } else {
                    if (i1 < N1) {
                        System.arraycopy(uids1, i1, uids1, i1 + 1, N1 - i1);
                    }
                    uids1[i1] = uids2[i2];
                }
                if (returnNewbs) {
                    sNewbWork = addWork(sNewbWork, uids2[i2]);
                }
                N1++;
                i1++;
                i2++;
            } else if (set) {
                int start = i1;
                while (i1 < N1 && (i2 >= N2 || uids2[i2] > uids1[i1])) {
                    sGoneWork = addWork(sGoneWork, uids1[i1]);
                    i1++;
                }
                if (start < i1) {
                    System.arraycopy(uids1, i1, uids1, start, N1 - i1);
                    N1 -= i1 - start;
                    i1 = start;
                }
                if (i1 < N1 && i2 < N2 && uids2[i2] == uids1[i1]) {
                    i1++;
                    i2++;
                }
            } else {
                if (i2 < N2 && uids2[i2] == uids1[i1]) {
                    i2++;
                }
                i1++;
            }
        }
    }

    private int compare(WorkSource other, int i1, int i2) {
        int diff = this.mUids[i1] - other.mUids[i2];
        if (diff != 0) {
            return diff;
        }
        return this.mNames[i1].compareTo(other.mNames[i2]);
    }

    private static WorkSource addWork(WorkSource cur, int newUid, String newName) {
        if (cur == null) {
            return new WorkSource(newUid, newName);
        }
        cur.insert(cur.mNum, newUid, newName);
        return cur;
    }

    /* JADX WARNING: Missing block: B:9:0x001d, code skipped:
            if (r7 > 0) goto L_0x0074;
     */
    private boolean updateUidsAndNamesLocked(android.os.WorkSource r12, boolean r13, boolean r14) {
        /*
        r11 = this;
        r0 = r12.mNum;
        r1 = r12.mUids;
        r2 = r12.mNames;
        r3 = 0;
        r4 = 0;
        r5 = 0;
    L_0x0009:
        r6 = r11.mNum;
        if (r4 < r6) goto L_0x0011;
    L_0x000d:
        if (r5 >= r0) goto L_0x0010;
    L_0x000f:
        goto L_0x0011;
    L_0x0010:
        return r3;
    L_0x0011:
        r6 = -1;
        r7 = r11.mNum;
        if (r4 >= r7) goto L_0x0074;
    L_0x0016:
        if (r5 >= r0) goto L_0x0020;
    L_0x0018:
        r7 = r11.compare(r12, r4, r5);
        r6 = r7;
        if (r7 <= 0) goto L_0x0020;
    L_0x001f:
        goto L_0x0074;
    L_0x0020:
        if (r13 != 0) goto L_0x002b;
    L_0x0022:
        if (r5 >= r0) goto L_0x0028;
    L_0x0024:
        if (r6 != 0) goto L_0x0028;
    L_0x0026:
        r5 = r5 + 1;
    L_0x0028:
        r4 = r4 + 1;
        goto L_0x008e;
    L_0x002b:
        r7 = r4;
    L_0x002c:
        if (r6 >= 0) goto L_0x004f;
    L_0x002e:
        r8 = sGoneWork;
        r9 = r11.mUids;
        r9 = r9[r4];
        r10 = r11.mNames;
        r10 = r10[r4];
        r8 = addWork(r8, r9, r10);
        sGoneWork = r8;
        r4 = r4 + 1;
        r8 = r11.mNum;
        if (r4 < r8) goto L_0x0045;
    L_0x0044:
        goto L_0x004f;
    L_0x0045:
        if (r5 >= r0) goto L_0x004c;
    L_0x0047:
        r8 = r11.compare(r12, r4, r5);
        goto L_0x004d;
    L_0x004c:
        r8 = -1;
    L_0x004d:
        r6 = r8;
        goto L_0x002c;
    L_0x004f:
        if (r7 >= r4) goto L_0x0069;
    L_0x0051:
        r8 = r11.mUids;
        r9 = r11.mNum;
        r9 = r9 - r4;
        java.lang.System.arraycopy(r8, r4, r8, r7, r9);
        r8 = r11.mNames;
        r9 = r11.mNum;
        r9 = r9 - r4;
        java.lang.System.arraycopy(r8, r4, r8, r7, r9);
        r8 = r11.mNum;
        r9 = r4 - r7;
        r8 = r8 - r9;
        r11.mNum = r8;
        r4 = r7;
    L_0x0069:
        r8 = r11.mNum;
        if (r4 >= r8) goto L_0x008e;
    L_0x006d:
        if (r6 != 0) goto L_0x008e;
    L_0x006f:
        r4 = r4 + 1;
        r5 = r5 + 1;
        goto L_0x008e;
    L_0x0074:
        r3 = 1;
        r7 = r1[r5];
        r8 = r2[r5];
        r11.insert(r4, r7, r8);
        if (r14 == 0) goto L_0x008a;
    L_0x007e:
        r7 = sNewbWork;
        r8 = r1[r5];
        r9 = r2[r5];
        r7 = addWork(r7, r8, r9);
        sNewbWork = r7;
    L_0x008a:
        r4 = r4 + 1;
        r5 = r5 + 1;
    L_0x008e:
        goto L_0x0009;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.WorkSource.updateUidsAndNamesLocked(android.os.WorkSource, boolean, boolean):boolean");
    }

    private void insert(int index, int uid) {
        int[] iArr = this.mUids;
        if (iArr == null) {
            this.mUids = new int[4];
            this.mUids[0] = uid;
            this.mNum = 1;
            return;
        }
        int i = this.mNum;
        if (i >= iArr.length) {
            int[] newuids = new int[((i * 3) / 2)];
            if (index > 0) {
                System.arraycopy(iArr, 0, newuids, 0, index);
            }
            int i2 = this.mNum;
            if (index < i2) {
                System.arraycopy(this.mUids, index, newuids, index + 1, i2 - index);
            }
            this.mUids = newuids;
            this.mUids[index] = uid;
            this.mNum++;
            return;
        }
        if (index < i) {
            System.arraycopy(iArr, index, iArr, index + 1, i - index);
        }
        this.mUids[index] = uid;
        this.mNum++;
    }

    private void insert(int index, int uid, String name) {
        int[] iArr = this.mUids;
        if (iArr == null) {
            this.mUids = new int[4];
            this.mUids[0] = uid;
            this.mNames = new String[4];
            this.mNames[0] = name;
            this.mNum = 1;
            return;
        }
        int i = this.mNum;
        if (i >= iArr.length) {
            int[] newuids = new int[((i * 3) / 2)];
            String[] newnames = new String[((i * 3) / 2)];
            if (index > 0) {
                System.arraycopy(iArr, 0, newuids, 0, index);
                System.arraycopy(this.mNames, 0, newnames, 0, index);
            }
            int i2 = this.mNum;
            if (index < i2) {
                System.arraycopy(this.mUids, index, newuids, index + 1, i2 - index);
                System.arraycopy(this.mNames, index, newnames, index + 1, this.mNum - index);
            }
            this.mUids = newuids;
            this.mNames = newnames;
            this.mUids[index] = uid;
            this.mNames[index] = name;
            this.mNum++;
            return;
        }
        if (index < i) {
            System.arraycopy(iArr, index, iArr, index + 1, i - index);
            String[] strArr = this.mNames;
            System.arraycopy(strArr, index, strArr, index + 1, this.mNum - index);
        }
        this.mUids[index] = uid;
        this.mNames[index] = name;
        this.mNum++;
    }

    public static ArrayList<WorkChain>[] diffChains(WorkSource oldWs, WorkSource newWs) {
        int i;
        WorkChain wc;
        ArrayList arrayList;
        ArrayList<WorkChain> newChains = null;
        ArrayList<WorkChain> goneChains = null;
        if (oldWs.mChains != null) {
            for (i = 0; i < oldWs.mChains.size(); i++) {
                wc = (WorkChain) oldWs.mChains.get(i);
                arrayList = newWs.mChains;
                if (arrayList == null || !arrayList.contains(wc)) {
                    if (goneChains == null) {
                        goneChains = new ArrayList(oldWs.mChains.size());
                    }
                    goneChains.add(wc);
                }
            }
        }
        if (newWs.mChains != null) {
            for (i = 0; i < newWs.mChains.size(); i++) {
                wc = (WorkChain) newWs.mChains.get(i);
                arrayList = oldWs.mChains;
                if (arrayList == null || !arrayList.contains(wc)) {
                    if (newChains == null) {
                        newChains = new ArrayList(newWs.mChains.size());
                    }
                    newChains.add(wc);
                }
            }
        }
        if (newChains == null && goneChains == null) {
            return null;
        }
        return new ArrayList[]{newChains, goneChains};
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mNum);
        dest.writeIntArray(this.mUids);
        dest.writeStringArray(this.mNames);
        ArrayList arrayList = this.mChains;
        if (arrayList == null) {
            dest.writeInt(-1);
            return;
        }
        dest.writeInt(arrayList.size());
        dest.writeParcelableList(this.mChains, flags);
    }

    public String toString() {
        String str;
        StringBuilder result = new StringBuilder();
        result.append("WorkSource{");
        int i = 0;
        while (true) {
            str = ", ";
            if (i >= this.mNum) {
                break;
            }
            if (i != 0) {
                result.append(str);
            }
            result.append(this.mUids[i]);
            if (this.mNames != null) {
                result.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                result.append(this.mNames[i]);
            }
            i++;
        }
        if (this.mChains != null) {
            result.append(" chains=");
            for (i = 0; i < this.mChains.size(); i++) {
                if (i != 0) {
                    result.append(str);
                }
                result.append(this.mChains.get(i));
            }
        }
        result.append("}");
        return result.toString();
    }

    public void writeToProto(ProtoOutputStream proto, long fieldId) {
        long j;
        ProtoOutputStream protoOutputStream = proto;
        long workSourceToken = proto.start(fieldId);
        int i = 0;
        while (true) {
            j = 2246267895809L;
            if (i >= this.mNum) {
                break;
            }
            j = protoOutputStream.start(2246267895809L);
            protoOutputStream.write(1120986464257L, this.mUids[i]);
            String[] strArr = this.mNames;
            if (strArr != null) {
                protoOutputStream.write(1138166333442L, strArr[i]);
            }
            protoOutputStream.end(j);
            i++;
        }
        if (this.mChains != null) {
            i = 0;
            while (i < this.mChains.size()) {
                WorkChain wc = (WorkChain) this.mChains.get(i);
                long workChain = protoOutputStream.start(2246267895810L);
                String[] tags = wc.getTags();
                int[] uids = wc.getUids();
                int j2 = 0;
                while (j2 < tags.length) {
                    long contentProto = protoOutputStream.start(j);
                    protoOutputStream.write(1120986464257L, uids[j2]);
                    protoOutputStream.write(1138166333442L, tags[j2]);
                    protoOutputStream.end(contentProto);
                    j2++;
                    j = 2246267895809L;
                }
                protoOutputStream.end(workChain);
                i++;
                long j3 = 1138166333442L;
                j = 2246267895809L;
            }
        }
        protoOutputStream.end(workSourceToken);
    }
}
