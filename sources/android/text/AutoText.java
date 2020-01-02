package android.text;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.provider.UserDictionary.Words;
import android.view.View;
import com.android.internal.R;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.util.Locale;
import org.xmlpull.v1.XmlPullParserException;

public class AutoText {
    private static final int DEFAULT = 14337;
    private static final int INCREMENT = 1024;
    private static final int RIGHT = 9300;
    private static final int TRIE_C = 0;
    private static final int TRIE_CHILD = 2;
    private static final int TRIE_NEXT = 3;
    private static final char TRIE_NULL = '￿';
    private static final int TRIE_OFF = 1;
    private static final int TRIE_ROOT = 0;
    private static final int TRIE_SIZEOF = 4;
    private static AutoText sInstance = new AutoText(Resources.getSystem());
    private static Object sLock = new Object();
    private Locale mLocale;
    private int mSize;
    private String mText;
    private char[] mTrie;
    private char mTrieUsed;

    private AutoText(Resources resources) {
        this.mLocale = resources.getConfiguration().locale;
        init(resources);
    }

    private static AutoText getInstance(View view) {
        AutoText instance;
        Resources res = view.getContext().getResources();
        Locale locale = res.getConfiguration().locale;
        synchronized (sLock) {
            instance = sInstance;
            if (!locale.equals(instance.mLocale)) {
                instance = new AutoText(res);
                sInstance = instance;
            }
        }
        return instance;
    }

    public static String get(CharSequence src, int start, int end, View view) {
        return getInstance(view).lookup(src, start, end);
    }

    public static int getSize(View view) {
        return getInstance(view).getSize();
    }

    private int getSize() {
        return this.mSize;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0049 A:{LOOP_END, LOOP:0: B:1:0x0006->B:18:0x0049} */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0048 A:{SYNTHETIC} */
    private java.lang.String lookup(java.lang.CharSequence r9, int r10, int r11) {
        /*
        r8 = this;
        r0 = r8.mTrie;
        r1 = 0;
        r0 = r0[r1];
        r1 = r10;
    L_0x0006:
        r2 = 0;
        if (r1 >= r11) goto L_0x004c;
    L_0x0009:
        r3 = r9.charAt(r1);
    L_0x000d:
        r4 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        if (r0 == r4) goto L_0x0046;
    L_0x0012:
        r5 = r8.mTrie;
        r6 = r0 + 0;
        r6 = r5[r6];
        if (r3 != r6) goto L_0x0041;
    L_0x001a:
        r6 = r11 + -1;
        if (r1 != r6) goto L_0x003a;
    L_0x001e:
        r6 = r0 + 1;
        r6 = r5[r6];
        if (r6 == r4) goto L_0x003a;
    L_0x0024:
        r2 = r0 + 1;
        r2 = r5[r2];
        r4 = r8.mText;
        r4 = r4.charAt(r2);
        r5 = r8.mText;
        r6 = r2 + 1;
        r7 = r2 + 1;
        r7 = r7 + r4;
        r5 = r5.substring(r6, r7);
        return r5;
    L_0x003a:
        r5 = r8.mTrie;
        r6 = r0 + 2;
        r0 = r5[r6];
        goto L_0x0046;
    L_0x0041:
        r4 = r0 + 3;
        r0 = r5[r4];
        goto L_0x000d;
    L_0x0046:
        if (r0 != r4) goto L_0x0049;
    L_0x0048:
        return r2;
    L_0x0049:
        r1 = r1 + 1;
        goto L_0x0006;
    L_0x004c:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.AutoText.lookup(java.lang.CharSequence, int, int):java.lang.String");
    }

    private void init(Resources r) {
        XmlResourceParser parser = r.getXml(R.xml.autotext);
        StringBuilder right = new StringBuilder(RIGHT);
        this.mTrie = new char[14337];
        this.mTrie[0] = TRIE_NULL;
        this.mTrieUsed = 1;
        try {
            XmlUtils.beginDocument(parser, "words");
            String odest = "";
            while (true) {
                XmlUtils.nextElement(parser);
                String element = parser.getName();
                if (element == null) {
                    break;
                } else if (!element.equals(Words.WORD)) {
                    break;
                } else {
                    String src = parser.getAttributeValue(null, "src");
                    if (parser.next() == 4) {
                        char off;
                        String dest = parser.getText();
                        if (dest.equals(odest)) {
                            off = 0;
                        } else {
                            off = (char) right.length();
                            right.append((char) dest.length());
                            right.append(dest);
                        }
                        add(src, off);
                    }
                }
            }
            r.flushLayoutCache();
            parser.close();
            this.mText = right.toString();
        } catch (XmlPullParserException e) {
            throw new RuntimeException(e);
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        } catch (Throwable th) {
            parser.close();
        }
    }

    private void add(String src, char off) {
        int slen = src.length();
        int herep = 0;
        this.mSize++;
        for (int i = 0; i < slen; i++) {
            char c = src.charAt(i);
            boolean found = false;
            while (true) {
                char[] cArr = this.mTrie;
                if (cArr[herep] == TRIE_NULL) {
                    break;
                } else if (c != cArr[cArr[herep] + 0]) {
                    herep = cArr[herep] + 3;
                } else if (i == slen - 1) {
                    cArr[cArr[herep] + 1] = off;
                    return;
                } else {
                    herep = cArr[herep] + 2;
                    found = true;
                }
            }
            if (!found) {
                char node = newTrieNode();
                char[] cArr2 = this.mTrie;
                cArr2[herep] = node;
                cArr2[cArr2[herep] + 0] = c;
                cArr2[cArr2[herep] + 1] = TRIE_NULL;
                cArr2[cArr2[herep] + 3] = TRIE_NULL;
                cArr2[cArr2[herep] + 2] = TRIE_NULL;
                if (i == slen - 1) {
                    cArr2[cArr2[herep] + 1] = off;
                    return;
                }
                herep = cArr2[herep] + 2;
            }
        }
    }

    private char newTrieNode() {
        int i = this.mTrieUsed + 4;
        char[] cArr = this.mTrie;
        if (i > cArr.length) {
            char[] copy = new char[(cArr.length + 1024)];
            System.arraycopy(cArr, 0, copy, 0, cArr.length);
            this.mTrie = copy;
        }
        char ret = this.mTrieUsed;
        this.mTrieUsed = (char) (this.mTrieUsed + 4);
        return ret;
    }
}
