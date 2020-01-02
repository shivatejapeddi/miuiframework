package android.content.pm.split;

import android.content.pm.PackageParser.PackageLite;
import android.util.IntArray;
import android.util.SparseArray;
import java.util.Arrays;
import java.util.BitSet;
import libcore.util.EmptyArray;

public abstract class SplitDependencyLoader<E extends Exception> {
    private final SparseArray<int[]> mDependencies;

    public static class IllegalDependencyException extends Exception {
        private IllegalDependencyException(String message) {
            super(message);
        }
    }

    public abstract void constructSplit(int i, int[] iArr, int i2) throws Exception;

    public abstract boolean isSplitCached(int i);

    protected SplitDependencyLoader(SparseArray<int[]> dependencies) {
        this.mDependencies = dependencies;
    }

    /* Access modifiers changed, original: protected */
    public void loadDependenciesForSplit(int splitIdx) throws Exception {
        if (!isSplitCached(splitIdx)) {
            if (splitIdx == 0) {
                constructSplit(0, collectConfigSplitIndices(0), -1);
                return;
            }
            int parentIdx;
            IntArray linearDependencies = new IntArray();
            linearDependencies.add(splitIdx);
            while (true) {
                int[] deps = (int[]) this.mDependencies.get(splitIdx);
                if (deps == null || deps.length <= 0) {
                    splitIdx = -1;
                } else {
                    splitIdx = deps[0];
                }
                if (splitIdx < 0 || isSplitCached(splitIdx)) {
                    parentIdx = splitIdx;
                } else {
                    linearDependencies.add(splitIdx);
                }
            }
            parentIdx = splitIdx;
            for (int i = linearDependencies.size() - 1; i >= 0; i--) {
                int idx = linearDependencies.get(i);
                constructSplit(idx, collectConfigSplitIndices(idx), parentIdx);
                parentIdx = idx;
            }
        }
    }

    private int[] collectConfigSplitIndices(int splitIdx) {
        int[] deps = (int[]) this.mDependencies.get(splitIdx);
        if (deps == null || deps.length <= 1) {
            return EmptyArray.INT;
        }
        return Arrays.copyOfRange(deps, 1, deps.length);
    }

    private static int[] append(int[] src, int elem) {
        if (src == null) {
            return new int[]{elem};
        }
        int[] dst = Arrays.copyOf(src, src.length + 1);
        dst[src.length] = elem;
        return dst;
    }

    public static SparseArray<int[]> createDependenciesFromPackage(PackageLite pkg) throws IllegalDependencyException {
        SparseArray<int[]> splitDependencies = new SparseArray();
        splitDependencies.put(0, new int[]{-1});
        int splitIdx = 0;
        while (true) {
            String str = "', which is missing.";
            String str2 = "Split '";
            if (splitIdx < pkg.splitNames.length) {
                if (pkg.isFeatureSplits[splitIdx]) {
                    int depIdx;
                    String splitDependency = pkg.usesSplitNames[splitIdx];
                    if (splitDependency != null) {
                        depIdx = Arrays.binarySearch(pkg.splitNames, splitDependency);
                        if (depIdx >= 0) {
                            depIdx++;
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(str2);
                            stringBuilder.append(pkg.splitNames[splitIdx]);
                            stringBuilder.append("' requires split '");
                            stringBuilder.append(splitDependency);
                            stringBuilder.append(str);
                            throw new IllegalDependencyException(stringBuilder.toString());
                        }
                    }
                    depIdx = 0;
                    splitDependencies.put(splitIdx + 1, new int[]{depIdx});
                }
                splitIdx++;
            } else {
                int depIdx2;
                for (int splitIdx2 = 0; splitIdx2 < pkg.splitNames.length; splitIdx2++) {
                    if (!pkg.isFeatureSplits[splitIdx2]) {
                        String configForSplit = pkg.configForSplit[splitIdx2];
                        if (configForSplit != null) {
                            depIdx2 = Arrays.binarySearch(pkg.splitNames, configForSplit);
                            StringBuilder stringBuilder2;
                            if (depIdx2 < 0) {
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append(str2);
                                stringBuilder2.append(pkg.splitNames[splitIdx2]);
                                stringBuilder2.append("' targets split '");
                                stringBuilder2.append(configForSplit);
                                stringBuilder2.append(str);
                                throw new IllegalDependencyException(stringBuilder2.toString());
                            } else if (pkg.isFeatureSplits[depIdx2]) {
                                depIdx2++;
                            } else {
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append(str2);
                                stringBuilder2.append(pkg.splitNames[splitIdx2]);
                                stringBuilder2.append("' declares itself as configuration split for a non-feature split '");
                                stringBuilder2.append(pkg.splitNames[depIdx2]);
                                stringBuilder2.append("'");
                                throw new IllegalDependencyException(stringBuilder2.toString());
                            }
                        }
                        depIdx2 = 0;
                        splitDependencies.put(depIdx2, append((int[]) splitDependencies.get(depIdx2), splitIdx2 + 1));
                    }
                }
                BitSet bitset = new BitSet();
                depIdx2 = splitDependencies.size();
                for (splitIdx = 0; splitIdx < depIdx2; splitIdx++) {
                    int splitIdx3 = splitDependencies.keyAt(splitIdx);
                    bitset.clear();
                    while (splitIdx3 != -1) {
                        if (bitset.get(splitIdx3)) {
                            throw new IllegalDependencyException("Cycle detected in split dependencies.");
                        }
                        bitset.set(splitIdx3);
                        int[] deps = (int[]) splitDependencies.get(splitIdx3);
                        splitIdx3 = deps != null ? deps[0] : -1;
                    }
                }
                return splitDependencies;
            }
        }
    }
}
