package android.content;

import android.annotation.UnsupportedAppUsage;
import android.net.Uri;
import java.util.ArrayList;
import java.util.List;

public class UriMatcher {
    private static final int EXACT = 0;
    public static final int NO_MATCH = -1;
    private static final int NUMBER = 1;
    private static final int TEXT = 2;
    @UnsupportedAppUsage
    private ArrayList<UriMatcher> mChildren;
    private int mCode;
    @UnsupportedAppUsage
    private final String mText;
    private final int mWhich;

    public UriMatcher(int code) {
        this.mCode = code;
        this.mWhich = -1;
        this.mChildren = new ArrayList();
        this.mText = null;
    }

    private UriMatcher(int which, String text) {
        this.mCode = -1;
        this.mWhich = which;
        this.mChildren = new ArrayList();
        this.mText = text;
    }

    public void addURI(String authority, String path, int code) {
        if (code >= 0) {
            String[] tokens = null;
            int numTokens = 0;
            if (path != null) {
                String newPath = path;
                if (path.length() > 1 && path.charAt(0) == '/') {
                    newPath = path.substring(1);
                }
                tokens = newPath.split("/");
            }
            if (tokens != null) {
                numTokens = tokens.length;
            }
            UriMatcher node = this;
            int i = -1;
            while (i < numTokens) {
                UriMatcher child;
                String token = i < 0 ? authority : tokens[i];
                ArrayList<UriMatcher> children = node.mChildren;
                int numChildren = children.size();
                int j = 0;
                while (j < numChildren) {
                    child = (UriMatcher) children.get(j);
                    if (token.equals(child.mText)) {
                        node = child;
                        break;
                    }
                    j++;
                }
                if (j == numChildren) {
                    child = createChild(token);
                    node.mChildren.add(child);
                    node = child;
                }
                i++;
            }
            node.mCode = code;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("code ");
        stringBuilder.append(code);
        stringBuilder.append(" is invalid: it must be positive");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0035  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0026  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0026  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0035  */
    private static android.content.UriMatcher createChild(java.lang.String r6) {
        /*
        r0 = r6.hashCode();
        r1 = 35;
        r2 = 0;
        r3 = "*";
        r4 = "#";
        r5 = 1;
        if (r0 == r1) goto L_0x001b;
    L_0x000e:
        r1 = 42;
        if (r0 == r1) goto L_0x0013;
    L_0x0012:
        goto L_0x0023;
    L_0x0013:
        r0 = r6.equals(r3);
        if (r0 == 0) goto L_0x0012;
    L_0x0019:
        r0 = r5;
        goto L_0x0024;
    L_0x001b:
        r0 = r6.equals(r4);
        if (r0 == 0) goto L_0x0012;
    L_0x0021:
        r0 = r2;
        goto L_0x0024;
    L_0x0023:
        r0 = -1;
    L_0x0024:
        if (r0 == 0) goto L_0x0035;
    L_0x0026:
        if (r0 == r5) goto L_0x002e;
    L_0x0028:
        r0 = new android.content.UriMatcher;
        r0.<init>(r2, r6);
        return r0;
    L_0x002e:
        r0 = new android.content.UriMatcher;
        r1 = 2;
        r0.<init>(r1, r3);
        return r0;
    L_0x0035:
        r0 = new android.content.UriMatcher;
        r0.<init>(r5, r4);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.UriMatcher.createChild(java.lang.String):android.content.UriMatcher");
    }

    public int match(Uri uri) {
        List<String> pathSegments = uri.getPathSegments();
        int li = pathSegments.size();
        UriMatcher node = this;
        if (li == 0 && uri.getAuthority() == null) {
            return this.mCode;
        }
        int i = -1;
        while (i < li) {
            String u = i < 0 ? uri.getAuthority() : (String) pathSegments.get(i);
            ArrayList<UriMatcher> list = node.mChildren;
            if (list == null) {
                break;
            }
            node = null;
            int lj = list.size();
            for (int j = 0; j < lj; j++) {
                UriMatcher n = (UriMatcher) list.get(j);
                int i2 = n.mWhich;
                if (i2 != 0) {
                    if (i2 == 1) {
                        i2 = u.length();
                        for (int k = 0; k < i2; k++) {
                            char c = u.charAt(k);
                            if (c < '0' || c > '9') {
                                break;
                            }
                        }
                        node = n;
                    } else if (i2 == 2) {
                        node = n;
                    }
                } else if (n.mText.equals(u)) {
                    node = n;
                }
                if (node != null) {
                    break;
                }
            }
            if (node == null) {
                return -1;
            }
            i++;
        }
        return node.mCode;
    }
}
