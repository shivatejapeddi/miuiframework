package android.service.autofill;

import android.app.assist.AssistStructure;
import android.app.assist.AssistStructure.ViewNode;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArrayMap;
import android.view.autofill.AutofillId;
import android.view.autofill.Helper;

public final class FillContext implements Parcelable {
    public static final Creator<FillContext> CREATOR = new Creator<FillContext>() {
        public FillContext createFromParcel(Parcel parcel) {
            return new FillContext(parcel, null);
        }

        public FillContext[] newArray(int size) {
            return new FillContext[size];
        }
    };
    private final AutofillId mFocusedId;
    private final int mRequestId;
    private final AssistStructure mStructure;
    private ArrayMap<AutofillId, ViewNode> mViewNodeLookupTable;

    /* synthetic */ FillContext(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public FillContext(int requestId, AssistStructure structure, AutofillId autofillId) {
        this.mRequestId = requestId;
        this.mStructure = structure;
        this.mFocusedId = autofillId;
    }

    private FillContext(Parcel parcel) {
        this(parcel.readInt(), (AssistStructure) parcel.readParcelable(null), (AutofillId) parcel.readParcelable(null));
    }

    public int getRequestId() {
        return this.mRequestId;
    }

    public AssistStructure getStructure() {
        return this.mStructure;
    }

    public AutofillId getFocusedId() {
        return this.mFocusedId;
    }

    public String toString() {
        if (!Helper.sDebug) {
            return super.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FillContext [reqId=");
        stringBuilder.append(this.mRequestId);
        stringBuilder.append(", focusedId=");
        stringBuilder.append(this.mFocusedId);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.mRequestId);
        parcel.writeParcelable(this.mStructure, flags);
        parcel.writeParcelable(this.mFocusedId, flags);
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x0097 A:{LOOP_END, LOOP:4: B:29:0x0091->B:31:0x0097} */
    public android.app.assist.AssistStructure.ViewNode[] findViewNodesByAutofillIds(android.view.autofill.AutofillId[] r11) {
        /*
        r10 = this;
        r0 = new java.util.LinkedList;
        r0.<init>();
        r1 = r11.length;
        r1 = new android.app.assist.AssistStructure.ViewNode[r1];
        r2 = new android.util.SparseIntArray;
        r3 = r11.length;
        r2.<init>(r3);
        r3 = 0;
    L_0x000f:
        r4 = r11.length;
        if (r3 >= r4) goto L_0x0034;
    L_0x0012:
        r4 = r10.mViewNodeLookupTable;
        r5 = 0;
        if (r4 == 0) goto L_0x002e;
    L_0x0017:
        r6 = r11[r3];
        r4 = r4.indexOfKey(r6);
        if (r4 < 0) goto L_0x002a;
    L_0x001f:
        r5 = r10.mViewNodeLookupTable;
        r5 = r5.valueAt(r4);
        r5 = (android.app.assist.AssistStructure.ViewNode) r5;
        r1[r3] = r5;
        goto L_0x002d;
    L_0x002a:
        r2.put(r3, r5);
    L_0x002d:
        goto L_0x0031;
    L_0x002e:
        r2.put(r3, r5);
    L_0x0031:
        r3 = r3 + 1;
        goto L_0x000f;
    L_0x0034:
        r3 = r10.mStructure;
        r3 = r3.getWindowNodeCount();
        r4 = 0;
    L_0x003b:
        if (r4 >= r3) goto L_0x004d;
    L_0x003d:
        r5 = r10.mStructure;
        r5 = r5.getWindowNodeAt(r4);
        r5 = r5.getRootViewNode();
        r0.add(r5);
        r4 = r4 + 1;
        goto L_0x003b;
    L_0x004d:
        r4 = r2.size();
        if (r4 <= 0) goto L_0x00a2;
    L_0x0053:
        r4 = r0.isEmpty();
        if (r4 != 0) goto L_0x00a2;
    L_0x0059:
        r4 = r0.removeFirst();
        r4 = (android.app.assist.AssistStructure.ViewNode) r4;
        r5 = 0;
    L_0x0060:
        r6 = r2.size();
        if (r5 >= r6) goto L_0x0090;
    L_0x0066:
        r6 = r2.keyAt(r5);
        r7 = r11[r6];
        r8 = r4.getAutofillId();
        r8 = r7.equals(r8);
        if (r8 == 0) goto L_0x008d;
    L_0x0076:
        r1[r6] = r4;
        r8 = r10.mViewNodeLookupTable;
        if (r8 != 0) goto L_0x0084;
    L_0x007c:
        r8 = new android.util.ArrayMap;
        r9 = r11.length;
        r8.<init>(r9);
        r10.mViewNodeLookupTable = r8;
    L_0x0084:
        r8 = r10.mViewNodeLookupTable;
        r8.put(r7, r4);
        r2.removeAt(r5);
        goto L_0x0090;
    L_0x008d:
        r5 = r5 + 1;
        goto L_0x0060;
    L_0x0090:
        r5 = 0;
    L_0x0091:
        r6 = r4.getChildCount();
        if (r5 >= r6) goto L_0x00a1;
    L_0x0097:
        r6 = r4.getChildAt(r5);
        r0.addLast(r6);
        r5 = r5 + 1;
        goto L_0x0091;
    L_0x00a1:
        goto L_0x004d;
    L_0x00a2:
        r4 = 0;
    L_0x00a3:
        r5 = r2.size();
        if (r4 >= r5) goto L_0x00c7;
    L_0x00a9:
        r5 = r10.mViewNodeLookupTable;
        if (r5 != 0) goto L_0x00b8;
    L_0x00ad:
        r5 = new android.util.ArrayMap;
        r6 = r2.size();
        r5.<init>(r6);
        r10.mViewNodeLookupTable = r5;
    L_0x00b8:
        r5 = r10.mViewNodeLookupTable;
        r6 = r2.keyAt(r4);
        r6 = r11[r6];
        r7 = 0;
        r5.put(r6, r7);
        r4 = r4 + 1;
        goto L_0x00a3;
    L_0x00c7:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.service.autofill.FillContext.findViewNodesByAutofillIds(android.view.autofill.AutofillId[]):android.app.assist.AssistStructure$ViewNode[]");
    }
}
