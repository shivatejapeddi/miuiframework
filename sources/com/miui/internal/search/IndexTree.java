package com.miui.internal.search;

import android.content.ContentValues;
import java.util.Iterator;
import java.util.LinkedList;

public abstract class IndexTree {
    private static final String TAG = "IndexTree";
    private volatile boolean mChanged;
    final Object mLock = new Object();
    private IndexTree mParent;
    private LinkedList<IndexTree> mSon;

    public abstract void commit(StringBuilder stringBuilder);

    public abstract boolean delete(String str, String[] strArr);

    public abstract String getUri();

    public abstract IndexTree insert(ContentValues contentValues);

    public abstract boolean query(RankedCursor rankedCursor, String str, String str2, String[] strArr, String str3, boolean z);

    public abstract boolean update(ContentValues contentValues, String str, String[] strArr);

    protected IndexTree(IndexTree parent) {
        this.mParent = parent;
    }

    public final boolean needCommit() {
        if (this.mChanged) {
            return true;
        }
        LinkedList linkedList = this.mSon;
        if (linkedList == null) {
            return false;
        }
        Iterator it = linkedList.iterator();
        while (it.hasNext()) {
            if (((IndexTree) it.next()).needCommit()) {
                return true;
            }
        }
        return false;
    }

    public IndexTree getParent() {
        return this.mParent;
    }

    public LinkedList<? extends IndexTree> getSons() {
        return this.mSon;
    }

    public final void dispatchQuery(RankedCursor cursor, String query, String selection, String[] selectionArgs, String sortOrder, boolean fullQuery) {
        if (query(cursor, query, selection, selectionArgs, sortOrder, fullQuery)) {
            LinkedList<? extends IndexTree> sons = getSons();
            if (sons != null) {
                synchronized (this.mLock) {
                    Iterator it = sons.iterator();
                    while (it.hasNext()) {
                        ((IndexTree) it.next()).dispatchQuery(cursor, query, selection, selectionArgs, sortOrder, fullQuery);
                    }
                }
            }
        }
    }

    public final int dispatchUpdate(ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        if (update(values, selection, selectionArgs)) {
            count = 0 + 1;
            this.mChanged = true;
        }
        LinkedList<? extends IndexTree> sons = getSons();
        if (sons == null) {
            return count;
        }
        synchronized (this.mLock) {
            Iterator it = sons.iterator();
            while (it.hasNext()) {
                count += ((IndexTree) it.next()).dispatchUpdate(values, selection, selectionArgs);
            }
        }
        return count;
    }

    public final String dispatchInsert(ContentValues values) {
        IndexTree newSon = insert(values);
        if (newSon != null) {
            this.mChanged = true;
            newSon.mChanged = true;
            return newSon.getUri();
        }
        LinkedList<? extends IndexTree> sons = getSons();
        if (sons == null) {
            return null;
        }
        synchronized (this.mLock) {
            Iterator it = sons.iterator();
            while (it.hasNext()) {
                String ret = ((IndexTree) it.next()).dispatchInsert(values);
                if (ret != null) {
                    return ret;
                }
            }
            return null;
        }
    }

    public final int dispatchDelete(String selection, String[] selectionArgs, boolean deleted) {
        int count = 0;
        if (!deleted) {
            deleted = delete(selection, selectionArgs);
            if (deleted && this.mParent != null) {
                removeSelf();
            }
        }
        if (deleted) {
            count = 0 + 1;
        }
        LinkedList<? extends IndexTree> sons = getSons();
        if (sons == null) {
            return count;
        }
        synchronized (this.mLock) {
            Iterator it = ((LinkedList) sons.clone()).iterator();
            while (it.hasNext()) {
                count += ((IndexTree) it.next()).dispatchDelete(selection, selectionArgs, deleted);
            }
        }
        return count;
    }

    public final void dispatchCommit(StringBuilder builder) {
        commit(builder);
        this.mChanged = false;
        if (this.mSon != null) {
            synchronized (this.mLock) {
                Iterator it = this.mSon.iterator();
                while (it.hasNext()) {
                    ((IndexTree) it.next()).dispatchCommit(builder);
                }
            }
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void addSon(IndexTree newSon) {
        LinkedList linkedList = this.mSon;
        addSon(linkedList == null ? 0 : linkedList.size(), newSon);
    }

    /* Access modifiers changed, original: protected|final */
    public final void addSon(int index, IndexTree newSon) {
        if (newSon != null) {
            this.mChanged = true;
            if (this.mSon == null) {
                this.mSon = new LinkedList();
            }
            this.mSon.add(index, newSon);
        }
    }

    public void removeSelf() {
        IndexTree indexTree = this.mParent;
        if (indexTree != null) {
            synchronized (indexTree.mLock) {
                this.mParent.mChanged = true;
                this.mParent.mSon.remove(this);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void replaceBy(IndexTree tree) {
        LinkedList<IndexTree> brothers = getParent().getSons();
        brothers.set(brothers.indexOf(this), tree);
    }
}
