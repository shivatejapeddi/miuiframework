package com.android.internal.app.procstats;

import android.util.DebugUtils;
import com.android.internal.app.procstats.SparseMappingTable.Table;
import java.io.PrintWriter;

public class SysMemUsageTable extends Table {
    public SysMemUsageTable(SparseMappingTable tableData) {
        super(tableData);
    }

    public void mergeStats(SysMemUsageTable that) {
        int N = that.getKeyCount();
        for (int i = 0; i < N; i++) {
            int key = that.getKeyAt(i);
            mergeStats(SparseMappingTable.getIdFromKey(key), that.getArrayForKey(key), SparseMappingTable.getIndexFromKey(key));
        }
    }

    public void mergeStats(int state, long[] addData, int addOff) {
        int key = getOrAddKey((byte) state, 16);
        mergeSysMemUsage(getArrayForKey(key), SparseMappingTable.getIndexFromKey(key), addData, addOff);
    }

    public long[] getTotalMemUsage() {
        long[] total = new long[16];
        int N = getKeyCount();
        for (int i = 0; i < N; i++) {
            int key = getKeyAt(i);
            mergeSysMemUsage(total, 0, getArrayForKey(key), SparseMappingTable.getIndexFromKey(key));
        }
        return total;
    }

    public static void mergeSysMemUsage(long[] dstData, int dstOff, long[] addData, int addOff) {
        long dstCount = dstData[dstOff + 0];
        long addCount = addData[addOff + 0];
        int i;
        if (dstCount == 0) {
            dstData[dstOff + 0] = addCount;
            for (i = 1; i < 16; i++) {
                dstData[dstOff + i] = addData[addOff + i];
            }
        } else if (addCount > 0) {
            dstData[dstOff + 0] = dstCount + addCount;
            for (i = 1; i < 16; i += 3) {
                if (dstData[dstOff + i] > addData[addOff + i]) {
                    dstData[dstOff + i] = addData[addOff + i];
                }
                dstData[(dstOff + i) + 1] = (long) (((((double) dstData[(dstOff + i) + 1]) * ((double) dstCount)) + (((double) addData[(addOff + i) + 1]) * ((double) addCount))) / ((double) (dstCount + addCount)));
                if (dstData[(dstOff + i) + 2] < addData[(addOff + i) + 2]) {
                    dstData[(dstOff + i) + 2] = addData[(addOff + i) + 2];
                }
            }
        }
    }

    public void dump(PrintWriter pw, String prefix, int[] screenStates, int[] memStates) {
        PrintWriter printWriter = pw;
        int[] iArr = screenStates;
        int[] iArr2 = memStates;
        int printedScreen = -1;
        for (int iscreen : iArr) {
            int printedMem = -1;
            for (int imem : iArr2) {
                int bucket = (iscreen + imem) * 14;
                long count = getValueForId((byte) bucket, 0);
                if (count > 0) {
                    int printedScreen2;
                    int printedMem2;
                    pw.print(prefix);
                    if (iArr.length > 1) {
                        DumpUtils.printScreenLabel(printWriter, printedScreen != iscreen ? iscreen : -1);
                        printedScreen2 = iscreen;
                    } else {
                        printedScreen2 = printedScreen;
                    }
                    if (iArr2.length > 1) {
                        DumpUtils.printMemLabel(printWriter, printedMem != imem ? imem : -1, 0);
                        printedMem2 = imem;
                    } else {
                        printedMem2 = printedMem;
                    }
                    printWriter.print(": ");
                    printWriter.print(count);
                    printWriter.println(" samples:");
                    PrintWriter printWriter2 = pw;
                    String str = prefix;
                    int i = bucket;
                    dumpCategory(printWriter2, str, "  Cached", i, 1);
                    dumpCategory(printWriter2, str, "  Free", i, 4);
                    dumpCategory(printWriter2, str, "  ZRam", i, 7);
                    dumpCategory(printWriter2, str, "  Kernel", i, 10);
                    dumpCategory(printWriter2, str, "  Native", i, 13);
                    printedMem = printedMem2;
                    printedScreen = printedScreen2;
                }
            }
        }
    }

    private void dumpCategory(PrintWriter pw, String prefix, String label, int bucket, int index) {
        pw.print(prefix);
        pw.print(label);
        pw.print(": ");
        DebugUtils.printSizeValue(pw, getValueForId((byte) bucket, index) * 1024);
        pw.print(" min, ");
        DebugUtils.printSizeValue(pw, getValueForId((byte) bucket, index + 1) * 1024);
        pw.print(" avg, ");
        DebugUtils.printSizeValue(pw, getValueForId((byte) bucket, index + 2) * 1024);
        pw.println(" max");
    }
}
