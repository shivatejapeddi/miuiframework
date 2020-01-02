package android.content.pm;

import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

abstract class BaseParceledListSlice<T> implements Parcelable {
    private static boolean DEBUG = false;
    private static final int MAX_IPC_SIZE = 65536;
    private static String TAG = "ParceledListSlice";
    private int mInlineCountLimit = Integer.MAX_VALUE;
    private final List<T> mList;

    public abstract Creator<?> readParcelableCreator(Parcel parcel, ClassLoader classLoader);

    public abstract void writeElement(T t, Parcel parcel, int i);

    @UnsupportedAppUsage
    public abstract void writeParcelableCreator(T t, Parcel parcel);

    public BaseParceledListSlice(List<T> list) {
        this.mList = list;
    }

    BaseParceledListSlice(Parcel p, ClassLoader loader) {
        String str;
        ClassLoader classLoader = loader;
        int N = p.readInt();
        this.mList = new ArrayList(N);
        if (DEBUG) {
            str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Retrieving ");
            stringBuilder.append(N);
            stringBuilder.append(" items");
            Log.d(str, stringBuilder.toString());
        }
        if (N > 0) {
            String str2;
            Creator<?> creator = readParcelableCreator(p, loader);
            int i = 0;
            Class<?> listElementClass = null;
            while (true) {
                str = ": ";
                Parcel parcel;
                if (i >= N) {
                    parcel = p;
                    break;
                } else if (p.readInt() == 0) {
                    parcel = p;
                    break;
                } else {
                    T parcelable = readCreator(creator, p, classLoader);
                    if (listElementClass == null) {
                        listElementClass = parcelable.getClass();
                    } else {
                        verifySameType(listElementClass, parcelable.getClass());
                    }
                    this.mList.add(parcelable);
                    if (DEBUG) {
                        str2 = TAG;
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Read inline #");
                        stringBuilder2.append(i);
                        stringBuilder2.append(str);
                        List list = this.mList;
                        stringBuilder2.append(list.get(list.size() - 1));
                        Log.d(str2, stringBuilder2.toString());
                    }
                    i++;
                }
            }
            if (i < N) {
                IBinder retriever = p.readStrongBinder();
                while (i < N) {
                    String str3 = " of ";
                    if (DEBUG) {
                        str2 = TAG;
                        StringBuilder stringBuilder3 = new StringBuilder();
                        stringBuilder3.append("Reading more @");
                        stringBuilder3.append(i);
                        stringBuilder3.append(str3);
                        stringBuilder3.append(N);
                        stringBuilder3.append(": retriever=");
                        stringBuilder3.append(retriever);
                        Log.d(str2, stringBuilder3.toString());
                    }
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    data.writeInt(i);
                    try {
                        String str4;
                        retriever.transact(1, data, reply, 0);
                        while (i < N && reply.readInt() != 0) {
                            T parcelable2 = readCreator(creator, reply, classLoader);
                            verifySameType(listElementClass, parcelable2.getClass());
                            this.mList.add(parcelable2);
                            if (DEBUG) {
                                String str5 = TAG;
                                StringBuilder stringBuilder4 = new StringBuilder();
                                stringBuilder4.append("Read extra #");
                                stringBuilder4.append(i);
                                stringBuilder4.append(str);
                                List list2 = this.mList;
                                str4 = str;
                                stringBuilder4.append(list2.get(list2.size() - 1));
                                Log.d(str5, stringBuilder4.toString());
                            } else {
                                str4 = str;
                            }
                            i++;
                            str = str4;
                        }
                        str4 = str;
                        reply.recycle();
                        data.recycle();
                        str = str4;
                    } catch (RemoteException e) {
                        RemoteException e2 = e2;
                        String str6 = TAG;
                        StringBuilder stringBuilder5 = new StringBuilder();
                        stringBuilder5.append("Failure retrieving array; only received ");
                        stringBuilder5.append(i);
                        stringBuilder5.append(str3);
                        stringBuilder5.append(N);
                        Log.w(str6, stringBuilder5.toString(), e2);
                        return;
                    }
                }
            }
        }
    }

    private T readCreator(Creator<?> creator, Parcel p, ClassLoader loader) {
        if (creator instanceof ClassLoaderCreator) {
            return ((ClassLoaderCreator) creator).createFromParcel(p, loader);
        }
        return creator.createFromParcel(p);
    }

    private static void verifySameType(Class<?> expected, Class<?> actual) {
        if (!actual.equals(expected)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Can't unparcel type ");
            stringBuilder.append(actual.getName());
            stringBuilder.append(" in list of type ");
            stringBuilder.append(expected == null ? null : expected.getName());
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    @UnsupportedAppUsage
    public List<T> getList() {
        return this.mList;
    }

    public void setInlineCountLimit(int maxCount) {
        this.mInlineCountLimit = maxCount;
    }

    public void writeToParcel(Parcel dest, int flags) {
        final int N = this.mList.size();
        final int callFlags = flags;
        dest.writeInt(N);
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Writing ");
            stringBuilder.append(N);
            stringBuilder.append(" items");
            Log.d(str, stringBuilder.toString());
        }
        if (N > 0) {
            final Class<?> listElementClass = this.mList.get(0).getClass();
            writeParcelableCreator(this.mList.get(0), dest);
            int i = 0;
            while (i < N && i < this.mInlineCountLimit && dest.dataSize() < 65536) {
                dest.writeInt(1);
                T parcelable = this.mList.get(i);
                verifySameType(listElementClass, parcelable.getClass());
                writeElement(parcelable, dest, callFlags);
                if (DEBUG) {
                    String str2 = TAG;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Wrote inline #");
                    stringBuilder2.append(i);
                    stringBuilder2.append(": ");
                    stringBuilder2.append(this.mList.get(i));
                    Log.d(str2, stringBuilder2.toString());
                }
                i++;
            }
            if (i < N) {
                dest.writeInt(0);
                Binder retriever = new Binder() {
                    /* Access modifiers changed, original: protected */
                    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
                        if (code != 1) {
                            return super.onTransact(code, data, reply, flags);
                        }
                        String access$100;
                        StringBuilder stringBuilder;
                        int i = data.readInt();
                        String str = " of ";
                        if (BaseParceledListSlice.DEBUG) {
                            access$100 = BaseParceledListSlice.TAG;
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Writing more @");
                            stringBuilder.append(i);
                            stringBuilder.append(str);
                            stringBuilder.append(N);
                            Log.d(access$100, stringBuilder.toString());
                        }
                        while (i < N && reply.dataSize() < 65536) {
                            reply.writeInt(1);
                            T parcelable = BaseParceledListSlice.this.mList.get(i);
                            BaseParceledListSlice.verifySameType(listElementClass, parcelable.getClass());
                            BaseParceledListSlice.this.writeElement(parcelable, reply, callFlags);
                            if (BaseParceledListSlice.DEBUG) {
                                String access$1002 = BaseParceledListSlice.TAG;
                                StringBuilder stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("Wrote extra #");
                                stringBuilder2.append(i);
                                stringBuilder2.append(": ");
                                stringBuilder2.append(BaseParceledListSlice.this.mList.get(i));
                                Log.d(access$1002, stringBuilder2.toString());
                            }
                            i++;
                        }
                        if (i < N) {
                            if (BaseParceledListSlice.DEBUG) {
                                access$100 = BaseParceledListSlice.TAG;
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("Breaking @");
                                stringBuilder.append(i);
                                stringBuilder.append(str);
                                stringBuilder.append(N);
                                Log.d(access$100, stringBuilder.toString());
                            }
                            reply.writeInt(0);
                        }
                        return true;
                    }
                };
                if (DEBUG) {
                    String str3 = TAG;
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("Breaking @");
                    stringBuilder3.append(i);
                    stringBuilder3.append(" of ");
                    stringBuilder3.append(N);
                    stringBuilder3.append(": retriever=");
                    stringBuilder3.append(retriever);
                    Log.d(str3, stringBuilder3.toString());
                }
                dest.writeStrongBinder(retriever);
            }
        }
    }
}
