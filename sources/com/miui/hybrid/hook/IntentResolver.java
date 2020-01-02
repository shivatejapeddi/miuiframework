package com.miui.hybrid.hook;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.FastImmutableArraySet;
import android.util.LogPrinter;
import android.util.MutableInt;
import android.util.PrintWriterPrinter;
import android.util.Printer;
import android.util.Slog;
import com.android.internal.util.FastPrintWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class IntentResolver<F extends IntentFilter, R> {
    private static final boolean DEBUG = false;
    private static final String TAG = "IntentResolver";
    private static final boolean localLOGV = false;
    private static final boolean localVerificationLOGV = false;
    private static final Comparator mResolvePrioritySorter = new Comparator() {
        public int compare(Object o1, Object o2) {
            int q1 = ((IntentFilter) o1).getPriority();
            int q2 = ((IntentFilter) o2).getPriority();
            if (q1 > q2) {
                return -1;
            }
            return q1 < q2 ? 1 : 0;
        }
    };
    private final ArrayMap<String, F[]> mActionToFilter = new ArrayMap();
    private final ArrayMap<String, F[]> mBaseTypeToFilter = new ArrayMap();
    private final ArraySet<F> mFilters = new ArraySet();
    private final ArrayMap<String, F[]> mSchemeToFilter = new ArrayMap();
    private final ArrayMap<String, F[]> mTypeToFilter = new ArrayMap();
    private final ArrayMap<String, F[]> mTypedActionToFilter = new ArrayMap();
    private final ArrayMap<String, F[]> mWildTypeToFilter = new ArrayMap();

    private class IteratorWrapper implements Iterator<F> {
        private F mCur;
        private final Iterator<F> mI;

        IteratorWrapper(Iterator<F> it) {
            this.mI = it;
        }

        public boolean hasNext() {
            return this.mI.hasNext();
        }

        public F next() {
            IntentFilter intentFilter = (IntentFilter) this.mI.next();
            this.mCur = intentFilter;
            return intentFilter;
        }

        public void remove() {
            IntentFilter intentFilter = this.mCur;
            if (intentFilter != null) {
                IntentResolver.this.removeFilterInternal(intentFilter);
            }
            this.mI.remove();
        }
    }

    public abstract boolean isPackageForFilter(String str, F f);

    public abstract F[] newArray(int i);

    public void addFilter(F f) {
        this.mFilters.add(f);
        int numS = register_intent_filter(f, f.schemesIterator(), this.mSchemeToFilter, "      Scheme: ");
        int numT = register_mime_types(f, "      Type: ");
        if (numS == 0 && numT == 0) {
            register_intent_filter(f, f.actionsIterator(), this.mActionToFilter, "      Action: ");
        }
        if (numT != 0) {
            register_intent_filter(f, f.actionsIterator(), this.mTypedActionToFilter, "      TypedAction: ");
        }
    }

    private boolean filterEquals(IntentFilter f1, IntentFilter f2) {
        int s1 = f1.countActions();
        if (s1 != f2.countActions()) {
            return false;
        }
        int i;
        for (i = 0; i < s1; i++) {
            if (!f2.hasAction(f1.getAction(i))) {
                return false;
            }
        }
        s1 = f1.countCategories();
        if (s1 != f2.countCategories()) {
            return false;
        }
        for (i = 0; i < s1; i++) {
            if (!f2.hasCategory(f1.getCategory(i))) {
                return false;
            }
        }
        s1 = f1.countDataTypes();
        if (s1 != f2.countDataTypes()) {
            return false;
        }
        for (i = 0; i < s1; i++) {
            if (!f2.hasExactDataType(f1.getDataType(i))) {
                return false;
            }
        }
        s1 = f1.countDataSchemes();
        if (s1 != f2.countDataSchemes()) {
            return false;
        }
        for (i = 0; i < s1; i++) {
            if (!f2.hasDataScheme(f1.getDataScheme(i))) {
                return false;
            }
        }
        s1 = f1.countDataAuthorities();
        if (s1 != f2.countDataAuthorities()) {
            return false;
        }
        for (i = 0; i < s1; i++) {
            if (!f2.hasDataAuthority(f1.getDataAuthority(i))) {
                return false;
            }
        }
        s1 = f1.countDataPaths();
        if (s1 != f2.countDataPaths()) {
            return false;
        }
        for (i = 0; i < s1; i++) {
            if (!f2.hasDataPath(f1.getDataPath(i))) {
                return false;
            }
        }
        s1 = f1.countDataSchemeSpecificParts();
        if (s1 != f2.countDataSchemeSpecificParts()) {
            return false;
        }
        for (i = 0; i < s1; i++) {
            if (!f2.hasDataSchemeSpecificPart(f1.getDataSchemeSpecificPart(i))) {
                return false;
            }
        }
        return true;
    }

    private ArrayList<F> collectFilters(F[] array, IntentFilter matching) {
        ArrayList<F> res = null;
        if (array != null) {
            for (F cur : array) {
                if (cur == null) {
                    break;
                }
                if (filterEquals(cur, matching)) {
                    if (res == null) {
                        res = new ArrayList();
                    }
                    res.add(cur);
                }
            }
        }
        return res;
    }

    public ArrayList<F> findFilters(IntentFilter matching) {
        if (matching.countDataSchemes() == 1) {
            return collectFilters((IntentFilter[]) this.mSchemeToFilter.get(matching.getDataScheme(0)), matching);
        }
        if (matching.countDataTypes() != 0 && matching.countActions() == 1) {
            return collectFilters((IntentFilter[]) this.mTypedActionToFilter.get(matching.getAction(0)), matching);
        }
        if (matching.countDataTypes() == 0 && matching.countDataSchemes() == 0 && matching.countActions() == 1) {
            return collectFilters((IntentFilter[]) this.mActionToFilter.get(matching.getAction(0)), matching);
        }
        ArrayList<F> res = null;
        Iterator it = this.mFilters.iterator();
        while (it.hasNext()) {
            IntentFilter cur = (IntentFilter) it.next();
            if (filterEquals(cur, matching)) {
                if (res == null) {
                    res = new ArrayList();
                }
                res.add(cur);
            }
        }
        return res;
    }

    public void removeFilter(F f) {
        removeFilterInternal(f);
        this.mFilters.remove(f);
    }

    /* Access modifiers changed, original: 0000 */
    public void removeFilterInternal(F f) {
        int numS = unregister_intent_filter(f, f.schemesIterator(), this.mSchemeToFilter, "      Scheme: ");
        int numT = unregister_mime_types(f, "      Type: ");
        if (numS == 0 && numT == 0) {
            unregister_intent_filter(f, f.actionsIterator(), this.mActionToFilter, "      Action: ");
        }
        if (numT != 0) {
            unregister_intent_filter(f, f.actionsIterator(), this.mTypedActionToFilter, "      TypedAction: ");
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean dumpMap(PrintWriter out, String titlePrefix, String title, String prefix, ArrayMap<String, F[]> map, String packageName, boolean printFilter, boolean collapseDuplicates) {
        IntentResolver intentResolver = this;
        PrintWriter printWriter = out;
        String str = prefix;
        ArrayMap<String, F[]> arrayMap = map;
        String str2 = packageName;
        String eprefix = new StringBuilder();
        eprefix.append(str);
        String str3 = "  ";
        eprefix.append(str3);
        eprefix = eprefix.toString();
        String fprefix = new StringBuilder();
        fprefix.append(str);
        fprefix.append("    ");
        fprefix = fprefix.toString();
        ArrayMap<Object, MutableInt> found = new ArrayMap();
        boolean printedSomething = false;
        int mapi = 0;
        Printer printer = null;
        String title2 = title;
        while (mapi < map.size()) {
            String str4;
            IntentFilter[] a = (IntentFilter[]) arrayMap.valueAt(mapi);
            int N = a.length;
            F printedHeader = null;
            str = ":";
            String str5;
            boolean printedSomething2;
            Printer printer2;
            boolean printedHeader2;
            int i;
            String title3;
            F filter;
            if (!collapseDuplicates || printFilter) {
                str5 = str3;
                printedSomething2 = printedSomething;
                printer2 = printer;
                printedHeader2 = false;
                i = 0;
                title3 = title2;
                while (i < N) {
                    F f = a[i];
                    filter = f;
                    if (f == null) {
                        str4 = str5;
                        break;
                    }
                    if (str2 == null || intentResolver.isPackageForFilter(str2, filter)) {
                        if (title3 != null) {
                            out.print(titlePrefix);
                            printWriter.println(title3);
                            title3 = null;
                        }
                        if (!printedHeader2) {
                            printWriter.print(eprefix);
                            printWriter.print((String) arrayMap.keyAt(mapi));
                            printWriter.println(str);
                            printedHeader2 = true;
                        }
                        intentResolver.dumpFilter(printWriter, fprefix, filter);
                        if (printFilter) {
                            Printer printer3;
                            if (printer2 == null) {
                                printer3 = new PrintWriterPrinter(printWriter);
                            } else {
                                printer3 = printer2;
                            }
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(fprefix);
                            str4 = str5;
                            stringBuilder.append(str4);
                            filter.dump(printer3, stringBuilder.toString());
                            printedSomething2 = true;
                            printer2 = printer3;
                        } else {
                            str4 = str5;
                            printedSomething2 = true;
                        }
                    } else {
                        str4 = str5;
                    }
                    i++;
                    intentResolver = this;
                    str5 = str4;
                    printWriter = out;
                }
                str4 = str5;
                title2 = title3;
                printer = printer2;
                printedSomething = printedSomething2;
            } else {
                found.clear();
                title = title2;
                int i2 = 0;
                while (i2 < N) {
                    F f2 = a[i2];
                    F filter2 = f2;
                    F f3;
                    if (f2 == null) {
                        str5 = str3;
                        printedSomething2 = printedSomething;
                        printer2 = printer;
                        f3 = filter2;
                        printedHeader2 = printedHeader;
                        break;
                    }
                    if (str2 != null) {
                        printer2 = printer;
                        filter = filter2;
                        if (!intentResolver.isPackageForFilter(str2, filter)) {
                            str5 = str3;
                            printedSomething2 = printedSomething;
                            f3 = filter;
                            filter2 = printedHeader;
                            i2++;
                            printer = printer2;
                            printedHeader = filter2;
                            printedSomething = printedSomething2;
                            str3 = str5;
                        }
                    } else {
                        printer2 = printer;
                        filter = filter2;
                    }
                    filter2 = printedHeader;
                    Object label = intentResolver.filterToLabel(filter);
                    int index = found.indexOfKey(label);
                    printedSomething2 = printedSomething;
                    if (index < 0) {
                        str5 = str3;
                        found.put(label, new MutableInt(1));
                    } else {
                        str5 = str3;
                        MutableInt mutableInt = (MutableInt) found.valueAt(index);
                        mutableInt.value++;
                    }
                    i2++;
                    printer = printer2;
                    printedHeader = filter2;
                    printedSomething = printedSomething2;
                    str3 = str5;
                }
                str5 = str3;
                printedSomething2 = printedSomething;
                printer2 = printer;
                printedHeader2 = printedHeader;
                title3 = title;
                for (i = 0; i < found.size(); i++) {
                    if (title3 != null) {
                        out.print(titlePrefix);
                        printWriter.println(title3);
                        title3 = null;
                    }
                    if (!printedHeader2) {
                        printWriter.print(eprefix);
                        printWriter.print((String) arrayMap.keyAt(mapi));
                        printWriter.println(str);
                        printedHeader2 = true;
                    }
                    printedSomething2 = true;
                    intentResolver.dumpFilterLabel(printWriter, fprefix, found.keyAt(i), ((MutableInt) found.valueAt(i)).value);
                }
                title2 = title3;
                printer = printer2;
                printedSomething = printedSomething2;
                str4 = str5;
            }
            mapi++;
            intentResolver = this;
            str = prefix;
            str3 = str4;
            printWriter = out;
        }
        return printedSomething;
    }

    public boolean dump(PrintWriter out, String title, String prefix, String packageName, boolean printFilter, boolean collapseDuplicates) {
        String str = prefix;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append("  ");
        String innerPrefix = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        String str2 = "\n";
        stringBuilder.append(str2);
        stringBuilder.append(str);
        String sepPrefix = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append(title);
        stringBuilder.append(str2);
        stringBuilder.append(str);
        String curPrefix = stringBuilder.toString();
        if (dumpMap(out, curPrefix, "Full MIME Types:", innerPrefix, this.mTypeToFilter, packageName, printFilter, collapseDuplicates)) {
            curPrefix = sepPrefix;
        }
        if (dumpMap(out, curPrefix, "Base MIME Types:", innerPrefix, this.mBaseTypeToFilter, packageName, printFilter, collapseDuplicates)) {
            curPrefix = sepPrefix;
        }
        if (dumpMap(out, curPrefix, "Wild MIME Types:", innerPrefix, this.mWildTypeToFilter, packageName, printFilter, collapseDuplicates)) {
            curPrefix = sepPrefix;
        }
        if (dumpMap(out, curPrefix, "Schemes:", innerPrefix, this.mSchemeToFilter, packageName, printFilter, collapseDuplicates)) {
            curPrefix = sepPrefix;
        }
        if (dumpMap(out, curPrefix, "Non-Data Actions:", innerPrefix, this.mActionToFilter, packageName, printFilter, collapseDuplicates)) {
            curPrefix = sepPrefix;
        }
        if (dumpMap(out, curPrefix, "MIME Typed Actions:", innerPrefix, this.mTypedActionToFilter, packageName, printFilter, collapseDuplicates)) {
            curPrefix = sepPrefix;
        }
        return curPrefix == sepPrefix;
    }

    public Iterator<F> filterIterator() {
        return new IteratorWrapper(this.mFilters.iterator());
    }

    public Set<F> filterSet() {
        return Collections.unmodifiableSet(this.mFilters);
    }

    public List<R> queryIntentFromList(Intent intent, String resolvedType, boolean defaultOnly, ArrayList<F[]> listCut, int userId) {
        ArrayList<R> resultList = new ArrayList();
        boolean debug = (intent.getFlags() & 8) != 0;
        FastImmutableArraySet<String> categories = getFastIntentCategories(intent);
        String scheme = intent.getScheme();
        int N = listCut.size();
        for (int i = 0; i < N; i++) {
            buildResolveList(intent, categories, debug, defaultOnly, resolvedType, scheme, (IntentFilter[]) listCut.get(i), resultList, userId);
        }
        filterResults(resultList);
        sortResults(resultList);
        return resultList;
    }

    /* JADX WARNING: Removed duplicated region for block: B:43:0x018c  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x01c9  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x01e9  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x01ff  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0215  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x022b  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0247  */
    public java.util.List<R> queryIntent(android.content.Intent r22, java.lang.String r23, boolean r24, int r25) {
        /*
        r21 = this;
        r10 = r21;
        r11 = r23;
        r12 = r22.getScheme();
        r0 = new java.util.ArrayList;
        r0.<init>();
        r13 = r0;
        r0 = r22.getFlags();
        r0 = r0 & 8;
        r1 = 0;
        if (r0 == 0) goto L_0x001a;
    L_0x0018:
        r0 = 1;
        goto L_0x001b;
    L_0x001a:
        r0 = r1;
    L_0x001b:
        r14 = r0;
        r15 = "IntentResolver";
        if (r14 == 0) goto L_0x005b;
    L_0x0020:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r2 = "Resolving type=";
        r0.append(r2);
        r0.append(r11);
        r2 = " scheme=";
        r0.append(r2);
        r0.append(r12);
        r2 = " defaultOnly=";
        r0.append(r2);
        r9 = r24;
        r0.append(r9);
        r2 = " userId=";
        r0.append(r2);
        r8 = r25;
        r0.append(r8);
        r2 = " of ";
        r0.append(r2);
        r7 = r22;
        r0.append(r7);
        r0 = r0.toString();
        android.util.Slog.v(r15, r0);
        goto L_0x0061;
    L_0x005b:
        r7 = r22;
        r9 = r24;
        r8 = r25;
    L_0x0061:
        r0 = 0;
        r2 = 0;
        r3 = 0;
        r4 = 0;
        if (r11 == 0) goto L_0x0180;
    L_0x0067:
        r5 = 47;
        r5 = r11.indexOf(r5);
        if (r5 <= 0) goto L_0x0177;
    L_0x006f:
        r1 = r11.substring(r1, r5);
        r6 = "*";
        r16 = r1.equals(r6);
        if (r16 != 0) goto L_0x0142;
    L_0x007b:
        r16 = r0;
        r0 = r23.length();
        r17 = r2;
        r2 = r5 + 2;
        r18 = r3;
        r3 = "Second type cut: ";
        r19 = r4;
        r4 = "First type cut: ";
        if (r0 != r2) goto L_0x00db;
    L_0x008f:
        r0 = r5 + 1;
        r0 = r11.charAt(r0);
        r2 = 42;
        if (r0 == r2) goto L_0x009a;
    L_0x0099:
        goto L_0x00db;
    L_0x009a:
        r0 = r10.mBaseTypeToFilter;
        r0 = r0.get(r1);
        r0 = (android.content.IntentFilter[]) r0;
        if (r14 == 0) goto L_0x00ba;
    L_0x00a4:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r2.append(r4);
        r4 = java.util.Arrays.toString(r0);
        r2.append(r4);
        r2 = r2.toString();
        android.util.Slog.v(r15, r2);
    L_0x00ba:
        r2 = r10.mWildTypeToFilter;
        r2 = r2.get(r1);
        r2 = (android.content.IntentFilter[]) r2;
        if (r14 == 0) goto L_0x011b;
    L_0x00c4:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4.append(r3);
        r3 = java.util.Arrays.toString(r2);
        r4.append(r3);
        r3 = r4.toString();
        android.util.Slog.v(r15, r3);
        goto L_0x011b;
    L_0x00db:
        r0 = r10.mTypeToFilter;
        r0 = r0.get(r11);
        r0 = (android.content.IntentFilter[]) r0;
        if (r14 == 0) goto L_0x00fb;
    L_0x00e5:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r2.append(r4);
        r4 = java.util.Arrays.toString(r0);
        r2.append(r4);
        r2 = r2.toString();
        android.util.Slog.v(r15, r2);
    L_0x00fb:
        r2 = r10.mWildTypeToFilter;
        r2 = r2.get(r1);
        r2 = (android.content.IntentFilter[]) r2;
        if (r14 == 0) goto L_0x011b;
    L_0x0105:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4.append(r3);
        r3 = java.util.Arrays.toString(r2);
        r4.append(r3);
        r3 = r4.toString();
        android.util.Slog.v(r15, r3);
    L_0x011b:
        r3 = r10.mWildTypeToFilter;
        r3 = r3.get(r6);
        r3 = (android.content.IntentFilter[]) r3;
        if (r14 == 0) goto L_0x013d;
    L_0x0125:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r6 = "Third type cut: ";
        r4.append(r6);
        r6 = java.util.Arrays.toString(r3);
        r4.append(r6);
        r4 = r4.toString();
        android.util.Slog.v(r15, r4);
    L_0x013d:
        r17 = r2;
        r18 = r3;
        goto L_0x018a;
    L_0x0142:
        r16 = r0;
        r17 = r2;
        r18 = r3;
        r19 = r4;
        r0 = r22.getAction();
        if (r0 == 0) goto L_0x0188;
    L_0x0150:
        r0 = r10.mTypedActionToFilter;
        r2 = r22.getAction();
        r0 = r0.get(r2);
        r0 = (android.content.IntentFilter[]) r0;
        if (r14 == 0) goto L_0x018a;
    L_0x015e:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Typed Action list: ";
        r2.append(r3);
        r3 = java.util.Arrays.toString(r0);
        r2.append(r3);
        r2 = r2.toString();
        android.util.Slog.v(r15, r2);
        goto L_0x018a;
    L_0x0177:
        r16 = r0;
        r17 = r2;
        r18 = r3;
        r19 = r4;
        goto L_0x0188;
    L_0x0180:
        r16 = r0;
        r17 = r2;
        r18 = r3;
        r19 = r4;
    L_0x0188:
        r0 = r16;
    L_0x018a:
        if (r12 == 0) goto L_0x01b0;
    L_0x018c:
        r1 = r10.mSchemeToFilter;
        r1 = r1.get(r12);
        r1 = (android.content.IntentFilter[]) r1;
        if (r14 == 0) goto L_0x01ae;
    L_0x0196:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Scheme list: ";
        r2.append(r3);
        r3 = java.util.Arrays.toString(r1);
        r2.append(r3);
        r2 = r2.toString();
        android.util.Slog.v(r15, r2);
    L_0x01ae:
        r19 = r1;
    L_0x01b0:
        if (r11 != 0) goto L_0x01e1;
    L_0x01b2:
        if (r12 != 0) goto L_0x01e1;
    L_0x01b4:
        r1 = r22.getAction();
        if (r1 == 0) goto L_0x01e1;
    L_0x01ba:
        r1 = r10.mActionToFilter;
        r2 = r22.getAction();
        r1 = r1.get(r2);
        r0 = r1;
        r0 = (android.content.IntentFilter[]) r0;
        if (r14 == 0) goto L_0x01e1;
    L_0x01c9:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Action list: ";
        r1.append(r2);
        r2 = java.util.Arrays.toString(r0);
        r1.append(r2);
        r1 = r1.toString();
        android.util.Slog.v(r15, r1);
    L_0x01e1:
        r16 = r0;
        r20 = getFastIntentCategories(r22);
        if (r16 == 0) goto L_0x01fd;
    L_0x01e9:
        r0 = r21;
        r1 = r22;
        r2 = r20;
        r3 = r14;
        r4 = r24;
        r5 = r23;
        r6 = r12;
        r7 = r16;
        r8 = r13;
        r9 = r25;
        r0.buildResolveList(r1, r2, r3, r4, r5, r6, r7, r8, r9);
    L_0x01fd:
        if (r17 == 0) goto L_0x0213;
    L_0x01ff:
        r0 = r21;
        r1 = r22;
        r2 = r20;
        r3 = r14;
        r4 = r24;
        r5 = r23;
        r6 = r12;
        r7 = r17;
        r8 = r13;
        r9 = r25;
        r0.buildResolveList(r1, r2, r3, r4, r5, r6, r7, r8, r9);
    L_0x0213:
        if (r18 == 0) goto L_0x0229;
    L_0x0215:
        r0 = r21;
        r1 = r22;
        r2 = r20;
        r3 = r14;
        r4 = r24;
        r5 = r23;
        r6 = r12;
        r7 = r18;
        r8 = r13;
        r9 = r25;
        r0.buildResolveList(r1, r2, r3, r4, r5, r6, r7, r8, r9);
    L_0x0229:
        if (r19 == 0) goto L_0x023f;
    L_0x022b:
        r0 = r21;
        r1 = r22;
        r2 = r20;
        r3 = r14;
        r4 = r24;
        r5 = r23;
        r6 = r12;
        r7 = r19;
        r8 = r13;
        r9 = r25;
        r0.buildResolveList(r1, r2, r3, r4, r5, r6, r7, r8, r9);
    L_0x023f:
        r10.filterResults(r13);
        r10.sortResults(r13);
        if (r14 == 0) goto L_0x026e;
    L_0x0247:
        r0 = "Final result list:";
        android.util.Slog.v(r15, r0);
        r0 = 0;
    L_0x024d:
        r1 = r13.size();
        if (r0 >= r1) goto L_0x026e;
    L_0x0253:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "  ";
        r1.append(r2);
        r2 = r13.get(r0);
        r1.append(r2);
        r1 = r1.toString();
        android.util.Slog.v(r15, r1);
        r0 = r0 + 1;
        goto L_0x024d;
    L_0x026e:
        return r13;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.hybrid.hook.IntentResolver.queryIntent(android.content.Intent, java.lang.String, boolean, int):java.util.List");
    }

    /* Access modifiers changed, original: protected */
    public boolean allowFilterResult(F f, List<R> list) {
        return true;
    }

    /* Access modifiers changed, original: protected */
    public boolean isFilterStopped(F f, int userId) {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean isFilterVerified(F filter) {
        return filter.isVerified();
    }

    /* Access modifiers changed, original: protected */
    public R newResult(F filter, int match, int userId) {
        return filter;
    }

    /* Access modifiers changed, original: protected */
    public void sortResults(List<R> results) {
        Collections.sort(results, mResolvePrioritySorter);
    }

    /* Access modifiers changed, original: protected */
    public void filterResults(List<R> list) {
    }

    /* Access modifiers changed, original: protected */
    public void dumpFilter(PrintWriter out, String prefix, F filter) {
        out.print(prefix);
        out.println(filter);
    }

    /* Access modifiers changed, original: protected */
    public Object filterToLabel(F f) {
        return "IntentFilter";
    }

    /* Access modifiers changed, original: protected */
    public void dumpFilterLabel(PrintWriter out, String prefix, Object label, int count) {
        out.print(prefix);
        out.print(label);
        out.print(": ");
        out.println(count);
    }

    private final void addFilter(ArrayMap<String, F[]> map, String name, F filter) {
        IntentFilter[] array = (IntentFilter[]) map.get(name);
        if (array == null) {
            F[] array2 = newArray(2);
            map.put(name, array2);
            array2[0] = filter;
            return;
        }
        int N = array.length;
        int i = N;
        while (i > 0 && array[i - 1] == null) {
            i--;
        }
        if (i < N) {
            array[i] = filter;
            return;
        }
        F[] newa = newArray((N * 3) / 2);
        System.arraycopy(array, 0, newa, 0, N);
        newa[N] = filter;
        map.put(name, newa);
    }

    private final int register_mime_types(F filter, String prefix) {
        Iterator<String> i = filter.typesIterator();
        if (i == null) {
            return 0;
        }
        int num = 0;
        while (i.hasNext()) {
            String name = (String) i.next();
            num++;
            String baseName = name;
            int slashpos = name.indexOf(47);
            if (slashpos > 0) {
                baseName = name.substring(0, slashpos).intern();
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(name);
                stringBuilder.append("/*");
                name = stringBuilder.toString();
            }
            addFilter(this.mTypeToFilter, name, filter);
            if (slashpos > 0) {
                addFilter(this.mBaseTypeToFilter, baseName, filter);
            } else {
                addFilter(this.mWildTypeToFilter, baseName, filter);
            }
        }
        return num;
    }

    private final int unregister_mime_types(F filter, String prefix) {
        Iterator<String> i = filter.typesIterator();
        if (i == null) {
            return 0;
        }
        int num = 0;
        while (i.hasNext()) {
            String name = (String) i.next();
            num++;
            String baseName = name;
            int slashpos = name.indexOf(47);
            if (slashpos > 0) {
                baseName = name.substring(0, slashpos).intern();
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(name);
                stringBuilder.append("/*");
                name = stringBuilder.toString();
            }
            remove_all_objects(this.mTypeToFilter, name, filter);
            if (slashpos > 0) {
                remove_all_objects(this.mBaseTypeToFilter, baseName, filter);
            } else {
                remove_all_objects(this.mWildTypeToFilter, baseName, filter);
            }
        }
        return num;
    }

    private final int register_intent_filter(F filter, Iterator<String> i, ArrayMap<String, F[]> dest, String prefix) {
        if (i == null) {
            return 0;
        }
        int num = 0;
        while (i.hasNext()) {
            num++;
            addFilter(dest, (String) i.next(), filter);
        }
        return num;
    }

    private final int unregister_intent_filter(F filter, Iterator<String> i, ArrayMap<String, F[]> dest, String prefix) {
        if (i == null) {
            return 0;
        }
        int num = 0;
        while (i.hasNext()) {
            num++;
            remove_all_objects(dest, (String) i.next(), filter);
        }
        return num;
    }

    private final void remove_all_objects(ArrayMap<String, F[]> map, String name, Object object) {
        IntentFilter[] array = (IntentFilter[]) map.get(name);
        if (array != null) {
            int LAST = array.length - 1;
            while (LAST >= 0 && array[LAST] == null) {
                LAST--;
            }
            for (int idx = LAST; idx >= 0; idx--) {
                if (array[idx] == object) {
                    int remain = LAST - idx;
                    if (remain > 0) {
                        System.arraycopy(array, idx + 1, array, idx, remain);
                    }
                    array[LAST] = null;
                    LAST--;
                }
            }
            if (LAST < 0) {
                map.remove(name);
            } else if (LAST < array.length / 2) {
                F[] newa = newArray(LAST + 2);
                System.arraycopy(array, 0, newa, 0, LAST + 1);
                map.put(name, newa);
            }
        }
    }

    private static FastImmutableArraySet<String> getFastIntentCategories(Intent intent) {
        Set<String> categories = intent.getCategories();
        if (categories == null) {
            return null;
        }
        return new FastImmutableArraySet((String[]) categories.toArray(new String[categories.size()]));
    }

    private void buildResolveList(Intent intent, FastImmutableArraySet<String> categories, boolean debug, boolean defaultOnly, String resolvedType, String scheme, F[] src, List<R> dest, int userId) {
        Printer logPrinter;
        PrintWriter logPrintWriter;
        int i;
        String str;
        Uri uri;
        String str2;
        F[] fArr = src;
        List<R> list = dest;
        int i2 = userId;
        String action = intent.getAction();
        Uri data = intent.getData();
        String packageName = intent.getPackage();
        boolean excludingStopped = intent.isExcludingStopped();
        String str3 = TAG;
        if (debug) {
            Printer logPrinter2 = new LogPrinter(2, str3, 3);
            logPrinter = logPrinter2;
            logPrintWriter = new FastPrintWriter(logPrinter2);
        } else {
            logPrinter = null;
            logPrintWriter = null;
        }
        int N = fArr != null ? fArr.length : 0;
        boolean hasNonDefaults = false;
        int i3 = 0;
        while (i3 < N) {
            F f = fArr[i3];
            F filter = f;
            int i4;
            F action2;
            PrintWriter data2;
            Printer packageName2;
            if (f == null) {
                i4 = i3;
                i = N;
                str = action;
                uri = data;
                str2 = packageName;
                action2 = filter;
                data2 = logPrintWriter;
                packageName2 = logPrinter;
                break;
            }
            StringBuilder stringBuilder;
            if (debug) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Matching against filter ");
                stringBuilder.append(filter);
                Slog.v(str3, stringBuilder.toString());
            }
            if (excludingStopped && isFilterStopped(filter, i2)) {
                if (debug) {
                    Slog.v(str3, "  Filter's target is stopped; skipping");
                    i4 = i3;
                    i = N;
                    str = action;
                    uri = data;
                    str2 = packageName;
                    action2 = filter;
                    data2 = logPrintWriter;
                    packageName2 = logPrinter;
                } else {
                    i4 = i3;
                    i = N;
                    str = action;
                    uri = data;
                    str2 = packageName;
                    action2 = filter;
                    data2 = logPrintWriter;
                    packageName2 = logPrinter;
                }
            } else if (packageName == null || isPackageForFilter(packageName, filter)) {
                if (filter.getAutoVerify() && debug) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("  Filter verified: ");
                    stringBuilder.append(isFilterVerified(filter));
                    Slog.v(str3, stringBuilder.toString());
                    int authorities = filter.countDataAuthorities();
                    int z = 0;
                    while (z < authorities) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        int authorities2 = authorities;
                        stringBuilder2.append("   ");
                        stringBuilder2.append(filter.getDataAuthority(z).getHost());
                        Slog.v(str3, stringBuilder2.toString());
                        z++;
                        fArr = src;
                        authorities = authorities2;
                    }
                }
                if (allowFilterResult(filter, list)) {
                    String str4 = action;
                    str = action;
                    action2 = filter;
                    i4 = i3;
                    i = N;
                    Uri uri2 = data;
                    uri = data;
                    data2 = logPrintWriter;
                    str2 = packageName;
                    packageName2 = logPrinter;
                    int match = filter.match(str4, resolvedType, scheme, uri2, categories, TAG);
                    String str5;
                    StringBuilder stringBuilder3;
                    if (match >= 0) {
                        str5 = Intent.CATEGORY_DEFAULT;
                        if (debug) {
                            stringBuilder3 = new StringBuilder();
                            stringBuilder3.append("  Filter matched!  match=0x");
                            stringBuilder3.append(Integer.toHexString(match));
                            stringBuilder3.append(" hasDefault=");
                            stringBuilder3.append(action2.hasCategory(str5));
                            Slog.v(str3, stringBuilder3.toString());
                        }
                        if (!defaultOnly || action2.hasCategory(str5)) {
                            R oneResult = newResult(action2, match, i2);
                            if (oneResult != null) {
                                list.add(oneResult);
                                if (debug) {
                                    str4 = "    ";
                                    dumpFilter(data2, str4, action2);
                                    data2.flush();
                                    action2.dump(packageName2, str4);
                                }
                            }
                        } else {
                            hasNonDefaults = true;
                        }
                    } else if (debug) {
                        if (match == -4) {
                            str5 = "category";
                        } else if (match == -3) {
                            str5 = "action";
                        } else if (match == -2) {
                            str5 = "data";
                        } else if (match != -1) {
                            str5 = "unknown reason";
                        } else {
                            str5 = "type";
                        }
                        stringBuilder3 = new StringBuilder();
                        stringBuilder3.append("  Filter did not match: ");
                        stringBuilder3.append(str5);
                        Slog.v(str3, stringBuilder3.toString());
                    }
                } else if (debug) {
                    Slog.v(str3, "  Filter's target already added");
                    i4 = i3;
                    i = N;
                    str = action;
                    uri = data;
                    str2 = packageName;
                    action2 = filter;
                    data2 = logPrintWriter;
                    packageName2 = logPrinter;
                } else {
                    i4 = i3;
                    i = N;
                    str = action;
                    uri = data;
                    str2 = packageName;
                    action2 = filter;
                    data2 = logPrintWriter;
                    packageName2 = logPrinter;
                }
            } else if (debug) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("  Filter is not from package ");
                stringBuilder.append(packageName);
                stringBuilder.append("; skipping");
                Slog.v(str3, stringBuilder.toString());
                i4 = i3;
                i = N;
                str = action;
                uri = data;
                str2 = packageName;
                action2 = filter;
                data2 = logPrintWriter;
                packageName2 = logPrinter;
            } else {
                i4 = i3;
                i = N;
                str = action;
                uri = data;
                str2 = packageName;
                action2 = filter;
                data2 = logPrintWriter;
                packageName2 = logPrinter;
            }
            i3 = i4 + 1;
            fArr = src;
            logPrintWriter = data2;
            logPrinter = packageName2;
            action = str;
            N = i;
            data = uri;
            packageName = str2;
        }
        i = N;
        str = action;
        uri = data;
        str2 = packageName;
        if (!debug || !hasNonDefaults) {
            return;
        }
        if (dest.size() == 0) {
            Slog.v(str3, "resolveIntent failed: found match, but none with CATEGORY_DEFAULT");
        } else if (dest.size() > 1) {
            Slog.v(str3, "resolveIntent: multiple matches, only some with CATEGORY_DEFAULT");
        }
    }
}
