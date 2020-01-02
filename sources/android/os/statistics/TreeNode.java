package android.os.statistics;

import java.util.HashMap;

/* compiled from: StringDictTree */
class TreeNode {
    private HashMap<Character, TreeNode> mChildNodes;
    private String mString = null;

    TreeNode() {
    }

    /* Access modifiers changed, original: 0000 */
    public void insert(String str, int idx) {
        if (idx == str.length() || (this.mChildNodes == null && this.mString == null)) {
            this.mString = str;
            return;
        }
        insertToChild(str, idx);
        String str2 = this.mString;
        if (str2 != null && idx < str2.length()) {
            insertToChild(this.mString, idx);
            this.mString = null;
        }
    }

    private void insertToChild(String str, int idx) {
        if (this.mChildNodes == null) {
            this.mChildNodes = new HashMap(0);
        }
        if (idx < str.length()) {
            char header = str.charAt(idx);
            TreeNode insertNode = (TreeNode) this.mChildNodes.get(Character.valueOf(header));
            if (insertNode == null) {
                insertNode = new TreeNode();
                this.mChildNodes.put(Character.valueOf(header), insertNode);
            }
            insertNode.insert(str, idx + 1);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean find(String str, int idx) {
        if (isTerminal()) {
            String str2 = this.mString;
            if (str2 != null) {
                return str.startsWith(str2);
            }
        }
        boolean z = false;
        if (this.mChildNodes == null || str == null || idx >= str.length()) {
            return false;
        }
        TreeNode child = (TreeNode) this.mChildNodes.get(Character.valueOf(str.charAt(idx)));
        if (child != null && child.find(str, idx + 1)) {
            z = true;
        }
        return z;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isTerminal() {
        return this.mChildNodes == null;
    }
}
