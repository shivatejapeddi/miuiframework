package android.content;

import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.IBinder;

public abstract class ContentProviderNative extends Binder implements IContentProvider {
    public abstract String getProviderName();

    public ContentProviderNative() {
        attachInterface(this, IContentProvider.descriptor);
    }

    @UnsupportedAppUsage
    public static IContentProvider asInterface(IBinder obj) {
        if (obj == null) {
            return null;
        }
        IContentProvider in = (IContentProvider) obj.queryLocalInterface(IContentProvider.descriptor);
        if (in != null) {
            return in;
        }
        return new ContentProviderProxy(obj);
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:14:0x0027, B:71:0x0299] */
    /* JADX WARNING: Missing block: B:80:0x02be, code skipped:
            if (r4 != null) goto L_0x02c0;
     */
    /* JADX WARNING: Missing block: B:81:0x02c0, code skipped:
            r4.close();
     */
    /* JADX WARNING: Missing block: B:82:0x02c3, code skipped:
            if (r1 != null) goto L_0x02c5;
     */
    /* JADX WARNING: Missing block: B:83:0x02c5, code skipped:
            r1.close();
     */
    public boolean onTransact(int r17, android.os.Parcel r18, android.os.Parcel r19, int r20) throws android.os.RemoteException {
        /*
        r16 = this;
        r7 = r16;
        r8 = r17;
        r9 = r18;
        r10 = r19;
        r0 = 0;
        r1 = "android.content.IContentProvider";
        r11 = 1;
        if (r8 == r11) goto L_0x024e;
    L_0x000e:
        r2 = 2;
        if (r8 == r2) goto L_0x0238;
    L_0x0011:
        r2 = 3;
        if (r8 == r2) goto L_0x0216;
    L_0x0014:
        r2 = 4;
        if (r8 == r2) goto L_0x01f4;
    L_0x0017:
        r2 = 10;
        if (r8 == r2) goto L_0x01c6;
    L_0x001b:
        switch(r8) {
            case 13: goto L_0x01a4;
            case 14: goto L_0x016d;
            case 15: goto L_0x013d;
            default: goto L_0x001e;
        };
    L_0x001e:
        switch(r8) {
            case 20: goto L_0x010d;
            case 21: goto L_0x00e9;
            case 22: goto L_0x00cf;
            case 23: goto L_0x0098;
            case 24: goto L_0x0086;
            case 25: goto L_0x006c;
            case 26: goto L_0x0052;
            case 27: goto L_0x0027;
            default: goto L_0x0021;
        };
        r0 = super.onTransact(r17, r18, r19, r20);
        return r0;
    L_0x0027:
        r9.enforceInterface(r1);	 Catch:{ Exception -> 0x02d1 }
        r1 = r18.readString();	 Catch:{ Exception -> 0x02d1 }
        r2 = android.net.Uri.CREATOR;	 Catch:{ Exception -> 0x02d1 }
        r2 = r2.createFromParcel(r9);	 Catch:{ Exception -> 0x02d1 }
        r2 = (android.net.Uri) r2;	 Catch:{ Exception -> 0x02d1 }
        r3 = r18.readBundle();	 Catch:{ Exception -> 0x02d1 }
        r4 = r18.readStrongBinder();	 Catch:{ Exception -> 0x02d1 }
        r4 = android.os.ICancellationSignal.Stub.asInterface(r4);	 Catch:{ Exception -> 0x02d1 }
        r5 = r7.refresh(r1, r2, r3, r4);	 Catch:{ Exception -> 0x02d1 }
        r19.writeNoException();	 Catch:{ Exception -> 0x02d1 }
        if (r5 == 0) goto L_0x004d;
    L_0x004c:
        goto L_0x004e;
    L_0x004d:
        r0 = -1;
    L_0x004e:
        r10.writeInt(r0);	 Catch:{ Exception -> 0x02d1 }
        return r11;
    L_0x0052:
        r9.enforceInterface(r1);	 Catch:{ Exception -> 0x02d1 }
        r0 = r18.readString();	 Catch:{ Exception -> 0x02d1 }
        r1 = android.net.Uri.CREATOR;	 Catch:{ Exception -> 0x02d1 }
        r1 = r1.createFromParcel(r9);	 Catch:{ Exception -> 0x02d1 }
        r1 = (android.net.Uri) r1;	 Catch:{ Exception -> 0x02d1 }
        r2 = r7.uncanonicalize(r0, r1);	 Catch:{ Exception -> 0x02d1 }
        r19.writeNoException();	 Catch:{ Exception -> 0x02d1 }
        android.net.Uri.writeToParcel(r10, r2);	 Catch:{ Exception -> 0x02d1 }
        return r11;
    L_0x006c:
        r9.enforceInterface(r1);	 Catch:{ Exception -> 0x02d1 }
        r0 = r18.readString();	 Catch:{ Exception -> 0x02d1 }
        r1 = android.net.Uri.CREATOR;	 Catch:{ Exception -> 0x02d1 }
        r1 = r1.createFromParcel(r9);	 Catch:{ Exception -> 0x02d1 }
        r1 = (android.net.Uri) r1;	 Catch:{ Exception -> 0x02d1 }
        r2 = r7.canonicalize(r0, r1);	 Catch:{ Exception -> 0x02d1 }
        r19.writeNoException();	 Catch:{ Exception -> 0x02d1 }
        android.net.Uri.writeToParcel(r10, r2);	 Catch:{ Exception -> 0x02d1 }
        return r11;
    L_0x0086:
        r9.enforceInterface(r1);	 Catch:{ Exception -> 0x02d1 }
        r0 = r16.createCancellationSignal();	 Catch:{ Exception -> 0x02d1 }
        r19.writeNoException();	 Catch:{ Exception -> 0x02d1 }
        r1 = r0.asBinder();	 Catch:{ Exception -> 0x02d1 }
        r10.writeStrongBinder(r1);	 Catch:{ Exception -> 0x02d1 }
        return r11;
    L_0x0098:
        r9.enforceInterface(r1);	 Catch:{ Exception -> 0x02d1 }
        r2 = r18.readString();	 Catch:{ Exception -> 0x02d1 }
        r1 = android.net.Uri.CREATOR;	 Catch:{ Exception -> 0x02d1 }
        r1 = r1.createFromParcel(r9);	 Catch:{ Exception -> 0x02d1 }
        r3 = r1;
        r3 = (android.net.Uri) r3;	 Catch:{ Exception -> 0x02d1 }
        r4 = r18.readString();	 Catch:{ Exception -> 0x02d1 }
        r5 = r18.readBundle();	 Catch:{ Exception -> 0x02d1 }
        r1 = r18.readStrongBinder();	 Catch:{ Exception -> 0x02d1 }
        r6 = android.os.ICancellationSignal.Stub.asInterface(r1);	 Catch:{ Exception -> 0x02d1 }
        r1 = r16;
        r1 = r1.openTypedAssetFile(r2, r3, r4, r5, r6);	 Catch:{ Exception -> 0x02d1 }
        r19.writeNoException();	 Catch:{ Exception -> 0x02d1 }
        if (r1 == 0) goto L_0x00cb;
    L_0x00c4:
        r10.writeInt(r11);	 Catch:{ Exception -> 0x02d1 }
        r1.writeToParcel(r10, r11);	 Catch:{ Exception -> 0x02d1 }
        goto L_0x00ce;
    L_0x00cb:
        r10.writeInt(r0);	 Catch:{ Exception -> 0x02d1 }
    L_0x00ce:
        return r11;
    L_0x00cf:
        r9.enforceInterface(r1);	 Catch:{ Exception -> 0x02d1 }
        r0 = android.net.Uri.CREATOR;	 Catch:{ Exception -> 0x02d1 }
        r0 = r0.createFromParcel(r9);	 Catch:{ Exception -> 0x02d1 }
        r0 = (android.net.Uri) r0;	 Catch:{ Exception -> 0x02d1 }
        r1 = r18.readString();	 Catch:{ Exception -> 0x02d1 }
        r2 = r7.getStreamTypes(r0, r1);	 Catch:{ Exception -> 0x02d1 }
        r19.writeNoException();	 Catch:{ Exception -> 0x02d1 }
        r10.writeStringArray(r2);	 Catch:{ Exception -> 0x02d1 }
        return r11;
    L_0x00e9:
        r9.enforceInterface(r1);	 Catch:{ Exception -> 0x02d1 }
        r2 = r18.readString();	 Catch:{ Exception -> 0x02d1 }
        r3 = r18.readString();	 Catch:{ Exception -> 0x02d1 }
        r4 = r18.readString();	 Catch:{ Exception -> 0x02d1 }
        r5 = r18.readString();	 Catch:{ Exception -> 0x02d1 }
        r6 = r18.readBundle();	 Catch:{ Exception -> 0x02d1 }
        r1 = r16;
        r0 = r1.call(r2, r3, r4, r5, r6);	 Catch:{ Exception -> 0x02d1 }
        r19.writeNoException();	 Catch:{ Exception -> 0x02d1 }
        r10.writeBundle(r0);	 Catch:{ Exception -> 0x02d1 }
        return r11;
    L_0x010d:
        r9.enforceInterface(r1);	 Catch:{ Exception -> 0x02d1 }
        r1 = r18.readString();	 Catch:{ Exception -> 0x02d1 }
        r2 = r18.readString();	 Catch:{ Exception -> 0x02d1 }
        r3 = r18.readInt();	 Catch:{ Exception -> 0x02d1 }
        r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x02d1 }
        r4.<init>(r3);	 Catch:{ Exception -> 0x02d1 }
        r5 = 0;
    L_0x0122:
        if (r5 >= r3) goto L_0x0132;
    L_0x0124:
        r6 = android.content.ContentProviderOperation.CREATOR;	 Catch:{ Exception -> 0x02d1 }
        r6 = r6.createFromParcel(r9);	 Catch:{ Exception -> 0x02d1 }
        r6 = (android.content.ContentProviderOperation) r6;	 Catch:{ Exception -> 0x02d1 }
        r4.add(r5, r6);	 Catch:{ Exception -> 0x02d1 }
        r5 = r5 + 1;
        goto L_0x0122;
    L_0x0132:
        r5 = r7.applyBatch(r1, r2, r4);	 Catch:{ Exception -> 0x02d1 }
        r19.writeNoException();	 Catch:{ Exception -> 0x02d1 }
        r10.writeTypedArray(r5, r0);	 Catch:{ Exception -> 0x02d1 }
        return r11;
    L_0x013d:
        r9.enforceInterface(r1);	 Catch:{ Exception -> 0x02d1 }
        r1 = r18.readString();	 Catch:{ Exception -> 0x02d1 }
        r2 = android.net.Uri.CREATOR;	 Catch:{ Exception -> 0x02d1 }
        r2 = r2.createFromParcel(r9);	 Catch:{ Exception -> 0x02d1 }
        r2 = (android.net.Uri) r2;	 Catch:{ Exception -> 0x02d1 }
        r3 = r18.readString();	 Catch:{ Exception -> 0x02d1 }
        r4 = r18.readStrongBinder();	 Catch:{ Exception -> 0x02d1 }
        r4 = android.os.ICancellationSignal.Stub.asInterface(r4);	 Catch:{ Exception -> 0x02d1 }
        r5 = r7.openAssetFile(r1, r2, r3, r4);	 Catch:{ Exception -> 0x02d1 }
        r19.writeNoException();	 Catch:{ Exception -> 0x02d1 }
        if (r5 == 0) goto L_0x0169;
    L_0x0162:
        r10.writeInt(r11);	 Catch:{ Exception -> 0x02d1 }
        r5.writeToParcel(r10, r11);	 Catch:{ Exception -> 0x02d1 }
        goto L_0x016c;
    L_0x0169:
        r10.writeInt(r0);	 Catch:{ Exception -> 0x02d1 }
    L_0x016c:
        return r11;
    L_0x016d:
        r9.enforceInterface(r1);	 Catch:{ Exception -> 0x02d1 }
        r2 = r18.readString();	 Catch:{ Exception -> 0x02d1 }
        r1 = android.net.Uri.CREATOR;	 Catch:{ Exception -> 0x02d1 }
        r1 = r1.createFromParcel(r9);	 Catch:{ Exception -> 0x02d1 }
        r3 = r1;
        r3 = (android.net.Uri) r3;	 Catch:{ Exception -> 0x02d1 }
        r4 = r18.readString();	 Catch:{ Exception -> 0x02d1 }
        r1 = r18.readStrongBinder();	 Catch:{ Exception -> 0x02d1 }
        r5 = android.os.ICancellationSignal.Stub.asInterface(r1);	 Catch:{ Exception -> 0x02d1 }
        r6 = r18.readStrongBinder();	 Catch:{ Exception -> 0x02d1 }
        r1 = r16;
        r1 = r1.openFile(r2, r3, r4, r5, r6);	 Catch:{ Exception -> 0x02d1 }
        r19.writeNoException();	 Catch:{ Exception -> 0x02d1 }
        if (r1 == 0) goto L_0x01a0;
    L_0x0199:
        r10.writeInt(r11);	 Catch:{ Exception -> 0x02d1 }
        r1.writeToParcel(r10, r11);	 Catch:{ Exception -> 0x02d1 }
        goto L_0x01a3;
    L_0x01a0:
        r10.writeInt(r0);	 Catch:{ Exception -> 0x02d1 }
    L_0x01a3:
        return r11;
    L_0x01a4:
        r9.enforceInterface(r1);	 Catch:{ Exception -> 0x02d1 }
        r0 = r18.readString();	 Catch:{ Exception -> 0x02d1 }
        r1 = android.net.Uri.CREATOR;	 Catch:{ Exception -> 0x02d1 }
        r1 = r1.createFromParcel(r9);	 Catch:{ Exception -> 0x02d1 }
        r1 = (android.net.Uri) r1;	 Catch:{ Exception -> 0x02d1 }
        r2 = android.content.ContentValues.CREATOR;	 Catch:{ Exception -> 0x02d1 }
        r2 = r9.createTypedArray(r2);	 Catch:{ Exception -> 0x02d1 }
        r2 = (android.content.ContentValues[]) r2;	 Catch:{ Exception -> 0x02d1 }
        r3 = r7.bulkInsert(r0, r1, r2);	 Catch:{ Exception -> 0x02d1 }
        r19.writeNoException();	 Catch:{ Exception -> 0x02d1 }
        r10.writeInt(r3);	 Catch:{ Exception -> 0x02d1 }
        return r11;
    L_0x01c6:
        r9.enforceInterface(r1);	 Catch:{ Exception -> 0x02d1 }
        r2 = r18.readString();	 Catch:{ Exception -> 0x02d1 }
        r0 = android.net.Uri.CREATOR;	 Catch:{ Exception -> 0x02d1 }
        r0 = r0.createFromParcel(r9);	 Catch:{ Exception -> 0x02d1 }
        r3 = r0;
        r3 = (android.net.Uri) r3;	 Catch:{ Exception -> 0x02d1 }
        r0 = android.content.ContentValues.CREATOR;	 Catch:{ Exception -> 0x02d1 }
        r0 = r0.createFromParcel(r9);	 Catch:{ Exception -> 0x02d1 }
        r4 = r0;
        r4 = (android.content.ContentValues) r4;	 Catch:{ Exception -> 0x02d1 }
        r5 = r18.readString();	 Catch:{ Exception -> 0x02d1 }
        r6 = r18.readStringArray();	 Catch:{ Exception -> 0x02d1 }
        r1 = r16;
        r0 = r1.update(r2, r3, r4, r5, r6);	 Catch:{ Exception -> 0x02d1 }
        r19.writeNoException();	 Catch:{ Exception -> 0x02d1 }
        r10.writeInt(r0);	 Catch:{ Exception -> 0x02d1 }
        return r11;
    L_0x01f4:
        r9.enforceInterface(r1);	 Catch:{ Exception -> 0x02d1 }
        r0 = r18.readString();	 Catch:{ Exception -> 0x02d1 }
        r1 = android.net.Uri.CREATOR;	 Catch:{ Exception -> 0x02d1 }
        r1 = r1.createFromParcel(r9);	 Catch:{ Exception -> 0x02d1 }
        r1 = (android.net.Uri) r1;	 Catch:{ Exception -> 0x02d1 }
        r2 = r18.readString();	 Catch:{ Exception -> 0x02d1 }
        r3 = r18.readStringArray();	 Catch:{ Exception -> 0x02d1 }
        r4 = r7.delete(r0, r1, r2, r3);	 Catch:{ Exception -> 0x02d1 }
        r19.writeNoException();	 Catch:{ Exception -> 0x02d1 }
        r10.writeInt(r4);	 Catch:{ Exception -> 0x02d1 }
        return r11;
    L_0x0216:
        r9.enforceInterface(r1);	 Catch:{ Exception -> 0x02d1 }
        r0 = r18.readString();	 Catch:{ Exception -> 0x02d1 }
        r1 = android.net.Uri.CREATOR;	 Catch:{ Exception -> 0x02d1 }
        r1 = r1.createFromParcel(r9);	 Catch:{ Exception -> 0x02d1 }
        r1 = (android.net.Uri) r1;	 Catch:{ Exception -> 0x02d1 }
        r2 = android.content.ContentValues.CREATOR;	 Catch:{ Exception -> 0x02d1 }
        r2 = r2.createFromParcel(r9);	 Catch:{ Exception -> 0x02d1 }
        r2 = (android.content.ContentValues) r2;	 Catch:{ Exception -> 0x02d1 }
        r3 = r7.insert(r0, r1, r2);	 Catch:{ Exception -> 0x02d1 }
        r19.writeNoException();	 Catch:{ Exception -> 0x02d1 }
        android.net.Uri.writeToParcel(r10, r3);	 Catch:{ Exception -> 0x02d1 }
        return r11;
    L_0x0238:
        r9.enforceInterface(r1);	 Catch:{ Exception -> 0x02d1 }
        r0 = android.net.Uri.CREATOR;	 Catch:{ Exception -> 0x02d1 }
        r0 = r0.createFromParcel(r9);	 Catch:{ Exception -> 0x02d1 }
        r0 = (android.net.Uri) r0;	 Catch:{ Exception -> 0x02d1 }
        r1 = r7.getType(r0);	 Catch:{ Exception -> 0x02d1 }
        r19.writeNoException();	 Catch:{ Exception -> 0x02d1 }
        r10.writeString(r1);	 Catch:{ Exception -> 0x02d1 }
        return r11;
    L_0x024e:
        r9.enforceInterface(r1);	 Catch:{ Exception -> 0x02d1 }
        r2 = r18.readString();	 Catch:{ Exception -> 0x02d1 }
        r1 = android.net.Uri.CREATOR;	 Catch:{ Exception -> 0x02d1 }
        r1 = r1.createFromParcel(r9);	 Catch:{ Exception -> 0x02d1 }
        r3 = r1;
        r3 = (android.net.Uri) r3;	 Catch:{ Exception -> 0x02d1 }
        r1 = r18.readInt();	 Catch:{ Exception -> 0x02d1 }
        r12 = r1;
        r1 = 0;
        if (r12 <= 0) goto L_0x0277;
    L_0x0266:
        r4 = new java.lang.String[r12];	 Catch:{ Exception -> 0x02d1 }
        r1 = r4;
        r4 = 0;
    L_0x026a:
        if (r4 >= r12) goto L_0x0275;
    L_0x026c:
        r5 = r18.readString();	 Catch:{ Exception -> 0x02d1 }
        r1[r4] = r5;	 Catch:{ Exception -> 0x02d1 }
        r4 = r4 + 1;
        goto L_0x026a;
    L_0x0275:
        r13 = r1;
        goto L_0x0278;
    L_0x0277:
        r13 = r1;
    L_0x0278:
        r5 = r18.readBundle();	 Catch:{ Exception -> 0x02d1 }
        r1 = r18.readStrongBinder();	 Catch:{ Exception -> 0x02d1 }
        r1 = android.database.IContentObserver.Stub.asInterface(r1);	 Catch:{ Exception -> 0x02d1 }
        r14 = r1;
        r1 = r18.readStrongBinder();	 Catch:{ Exception -> 0x02d1 }
        r6 = android.os.ICancellationSignal.Stub.asInterface(r1);	 Catch:{ Exception -> 0x02d1 }
        r1 = r16;
        r4 = r13;
        r1 = r1.query(r2, r3, r4, r5, r6);	 Catch:{ Exception -> 0x02d1 }
        if (r1 == 0) goto L_0x02ca;
    L_0x0298:
        r4 = 0;
        r0 = new android.database.CursorToBulkCursorAdaptor;	 Catch:{ all -> 0x02bd }
        r15 = r16.getProviderName();	 Catch:{ all -> 0x02bd }
        r0.<init>(r1, r14, r15);	 Catch:{ all -> 0x02bd }
        r4 = r0;
        r1 = 0;
        r0 = r4.getBulkCursorDescriptor();	 Catch:{ all -> 0x02bd }
        r4 = 0;
        r19.writeNoException();	 Catch:{ all -> 0x02bd }
        r10.writeInt(r11);	 Catch:{ all -> 0x02bd }
        r0.writeToParcel(r10, r11);	 Catch:{ all -> 0x02bd }
        if (r4 == 0) goto L_0x02b7;
    L_0x02b4:
        r4.close();	 Catch:{ Exception -> 0x02d1 }
    L_0x02b7:
        if (r1 == 0) goto L_0x02bc;
    L_0x02b9:
        r1.close();	 Catch:{ Exception -> 0x02d1 }
    L_0x02bc:
        goto L_0x02d0;
    L_0x02bd:
        r0 = move-exception;
        if (r4 == 0) goto L_0x02c3;
    L_0x02c0:
        r4.close();	 Catch:{ Exception -> 0x02d1 }
    L_0x02c3:
        if (r1 == 0) goto L_0x02c8;
    L_0x02c5:
        r1.close();	 Catch:{ Exception -> 0x02d1 }
        throw r0;	 Catch:{ Exception -> 0x02d1 }
    L_0x02ca:
        r19.writeNoException();	 Catch:{ Exception -> 0x02d1 }
        r10.writeInt(r0);	 Catch:{ Exception -> 0x02d1 }
    L_0x02d0:
        return r11;
    L_0x02d1:
        r0 = move-exception;
        android.database.DatabaseUtils.writeExceptionToParcel(r10, r0);
        return r11;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.ContentProviderNative.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
    }

    public IBinder asBinder() {
        return this;
    }
}
