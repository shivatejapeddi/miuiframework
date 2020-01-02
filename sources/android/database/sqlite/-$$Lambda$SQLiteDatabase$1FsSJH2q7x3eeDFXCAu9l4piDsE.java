package android.database.sqlite;

import java.io.File;
import java.util.Comparator;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$SQLiteDatabase$1FsSJH2q7x3eeDFXCAu9l4piDsE implements Comparator {
    public static final /* synthetic */ -$$Lambda$SQLiteDatabase$1FsSJH2q7x3eeDFXCAu9l4piDsE INSTANCE = new -$$Lambda$SQLiteDatabase$1FsSJH2q7x3eeDFXCAu9l4piDsE();

    private /* synthetic */ -$$Lambda$SQLiteDatabase$1FsSJH2q7x3eeDFXCAu9l4piDsE() {
    }

    public final int compare(Object obj, Object obj2) {
        return ((File) obj).getName().compareTo(((File) obj2).getName());
    }
}
