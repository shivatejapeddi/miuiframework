package android.os.statistics;

class StringDictTree extends TreeNode {
    public StringDictTree(String[] strings) {
        for (String item : strings) {
            super.insert(item, 0);
        }
    }

    public boolean find(String str) {
        return super.find(str, 0);
    }
}
