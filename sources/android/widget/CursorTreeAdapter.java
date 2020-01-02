package android.widget;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

public abstract class CursorTreeAdapter extends BaseExpandableListAdapter implements Filterable, CursorFilterClient {
    private boolean mAutoRequery;
    SparseArray<MyCursorHelper> mChildrenCursorHelpers;
    private Context mContext;
    CursorFilter mCursorFilter;
    FilterQueryProvider mFilterQueryProvider;
    MyCursorHelper mGroupCursorHelper;
    private Handler mHandler;

    class MyCursorHelper {
        private MyContentObserver mContentObserver;
        private Cursor mCursor;
        private MyDataSetObserver mDataSetObserver;
        private boolean mDataValid;
        private int mRowIDColumn;

        private class MyContentObserver extends ContentObserver {
            public MyContentObserver() {
                super(CursorTreeAdapter.this.mHandler);
            }

            public boolean deliverSelfNotifications() {
                return true;
            }

            public void onChange(boolean selfChange) {
                if (CursorTreeAdapter.this.mAutoRequery && MyCursorHelper.this.mCursor != null && !MyCursorHelper.this.mCursor.isClosed()) {
                    MyCursorHelper myCursorHelper = MyCursorHelper.this;
                    myCursorHelper.mDataValid = myCursorHelper.mCursor.requery();
                }
            }
        }

        private class MyDataSetObserver extends DataSetObserver {
            private MyDataSetObserver() {
            }

            public void onChanged() {
                MyCursorHelper.this.mDataValid = true;
                CursorTreeAdapter.this.notifyDataSetChanged();
            }

            public void onInvalidated() {
                MyCursorHelper.this.mDataValid = false;
                CursorTreeAdapter.this.notifyDataSetInvalidated();
            }
        }

        MyCursorHelper(Cursor cursor) {
            boolean cursorPresent = cursor != null;
            this.mCursor = cursor;
            this.mDataValid = cursorPresent;
            this.mRowIDColumn = cursorPresent ? cursor.getColumnIndex("_id") : -1;
            this.mContentObserver = new MyContentObserver();
            this.mDataSetObserver = new MyDataSetObserver();
            if (cursorPresent) {
                cursor.registerContentObserver(this.mContentObserver);
                cursor.registerDataSetObserver(this.mDataSetObserver);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public Cursor getCursor() {
            return this.mCursor;
        }

        /* Access modifiers changed, original: 0000 */
        public int getCount() {
            if (this.mDataValid) {
                Cursor cursor = this.mCursor;
                if (cursor != null) {
                    return cursor.getCount();
                }
            }
            return 0;
        }

        /* Access modifiers changed, original: 0000 */
        public long getId(int position) {
            if (this.mDataValid) {
                Cursor cursor = this.mCursor;
                if (cursor == null || !cursor.moveToPosition(position)) {
                    return 0;
                }
                return this.mCursor.getLong(this.mRowIDColumn);
            }
            return 0;
        }

        /* Access modifiers changed, original: 0000 */
        public Cursor moveTo(int position) {
            if (this.mDataValid) {
                Cursor cursor = this.mCursor;
                if (cursor != null && cursor.moveToPosition(position)) {
                    return this.mCursor;
                }
            }
            return null;
        }

        /* Access modifiers changed, original: 0000 */
        public void changeCursor(Cursor cursor, boolean releaseCursors) {
            if (cursor != this.mCursor) {
                deactivate();
                this.mCursor = cursor;
                if (cursor != null) {
                    cursor.registerContentObserver(this.mContentObserver);
                    cursor.registerDataSetObserver(this.mDataSetObserver);
                    this.mRowIDColumn = cursor.getColumnIndex("_id");
                    this.mDataValid = true;
                    CursorTreeAdapter.this.notifyDataSetChanged(releaseCursors);
                } else {
                    this.mRowIDColumn = -1;
                    this.mDataValid = false;
                    CursorTreeAdapter.this.notifyDataSetInvalidated();
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void deactivate() {
            Cursor cursor = this.mCursor;
            if (cursor != null) {
                cursor.unregisterContentObserver(this.mContentObserver);
                this.mCursor.unregisterDataSetObserver(this.mDataSetObserver);
                this.mCursor.close();
                this.mCursor = null;
            }
        }

        /* Access modifiers changed, original: 0000 */
        public boolean isValid() {
            return this.mDataValid && this.mCursor != null;
        }
    }

    public abstract void bindChildView(View view, Context context, Cursor cursor, boolean z);

    public abstract void bindGroupView(View view, Context context, Cursor cursor, boolean z);

    public abstract Cursor getChildrenCursor(Cursor cursor);

    public abstract View newChildView(Context context, Cursor cursor, boolean z, ViewGroup viewGroup);

    public abstract View newGroupView(Context context, Cursor cursor, boolean z, ViewGroup viewGroup);

    public CursorTreeAdapter(Cursor cursor, Context context) {
        init(cursor, context, true);
    }

    public CursorTreeAdapter(Cursor cursor, Context context, boolean autoRequery) {
        init(cursor, context, autoRequery);
    }

    private void init(Cursor cursor, Context context, boolean autoRequery) {
        this.mContext = context;
        this.mHandler = new Handler();
        this.mAutoRequery = autoRequery;
        this.mGroupCursorHelper = new MyCursorHelper(cursor);
        this.mChildrenCursorHelpers = new SparseArray();
    }

    /* Access modifiers changed, original: declared_synchronized */
    /* JADX WARNING: Missing block: B:12:0x002c, code skipped:
            return r0;
     */
    public synchronized android.widget.CursorTreeAdapter.MyCursorHelper getChildrenCursorHelper(int r4, boolean r5) {
        /*
        r3 = this;
        monitor-enter(r3);
        r0 = r3.mChildrenCursorHelpers;	 Catch:{ all -> 0x002d }
        r0 = r0.get(r4);	 Catch:{ all -> 0x002d }
        r0 = (android.widget.CursorTreeAdapter.MyCursorHelper) r0;	 Catch:{ all -> 0x002d }
        if (r0 != 0) goto L_0x002b;
    L_0x000b:
        r1 = r3.mGroupCursorHelper;	 Catch:{ all -> 0x002d }
        r1 = r1.moveTo(r4);	 Catch:{ all -> 0x002d }
        if (r1 != 0) goto L_0x0016;
    L_0x0013:
        r1 = 0;
        monitor-exit(r3);
        return r1;
    L_0x0016:
        r1 = r3.mGroupCursorHelper;	 Catch:{ all -> 0x002d }
        r1 = r1.getCursor();	 Catch:{ all -> 0x002d }
        r1 = r3.getChildrenCursor(r1);	 Catch:{ all -> 0x002d }
        r2 = new android.widget.CursorTreeAdapter$MyCursorHelper;	 Catch:{ all -> 0x002d }
        r2.<init>(r1);	 Catch:{ all -> 0x002d }
        r0 = r2;
        r2 = r3.mChildrenCursorHelpers;	 Catch:{ all -> 0x002d }
        r2.put(r4, r0);	 Catch:{ all -> 0x002d }
    L_0x002b:
        monitor-exit(r3);
        return r0;
    L_0x002d:
        r4 = move-exception;
        monitor-exit(r3);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.CursorTreeAdapter.getChildrenCursorHelper(int, boolean):android.widget.CursorTreeAdapter$MyCursorHelper");
    }

    public void setGroupCursor(Cursor cursor) {
        this.mGroupCursorHelper.changeCursor(cursor, false);
    }

    public void setChildrenCursor(int groupPosition, Cursor childrenCursor) {
        getChildrenCursorHelper(groupPosition, false).changeCursor(childrenCursor, false);
    }

    public Cursor getChild(int groupPosition, int childPosition) {
        return getChildrenCursorHelper(groupPosition, true).moveTo(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return getChildrenCursorHelper(groupPosition, true).getId(childPosition);
    }

    public int getChildrenCount(int groupPosition) {
        MyCursorHelper helper = getChildrenCursorHelper(groupPosition, true);
        return (!this.mGroupCursorHelper.isValid() || helper == null) ? 0 : helper.getCount();
    }

    public Cursor getGroup(int groupPosition) {
        return this.mGroupCursorHelper.moveTo(groupPosition);
    }

    public int getGroupCount() {
        return this.mGroupCursorHelper.getCount();
    }

    public long getGroupId(int groupPosition) {
        return this.mGroupCursorHelper.getId(groupPosition);
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Cursor cursor = this.mGroupCursorHelper.moveTo(groupPosition);
        if (cursor != null) {
            View v;
            if (convertView == null) {
                v = newGroupView(this.mContext, cursor, isExpanded, parent);
            } else {
                v = convertView;
            }
            bindGroupView(v, this.mContext, cursor, isExpanded);
            return v;
        }
        throw new IllegalStateException("this should only be called when the cursor is valid");
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Cursor cursor = getChildrenCursorHelper(groupPosition, true).moveTo(childPosition);
        if (cursor != null) {
            View v;
            if (convertView == null) {
                v = newChildView(this.mContext, cursor, isLastChild, parent);
            } else {
                v = convertView;
            }
            bindChildView(v, this.mContext, cursor, isLastChild);
            return v;
        }
        throw new IllegalStateException("this should only be called when the cursor is valid");
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }

    private synchronized void releaseCursorHelpers() {
        for (int pos = this.mChildrenCursorHelpers.size() - 1; pos >= 0; pos--) {
            ((MyCursorHelper) this.mChildrenCursorHelpers.valueAt(pos)).deactivate();
        }
        this.mChildrenCursorHelpers.clear();
    }

    public void notifyDataSetChanged() {
        notifyDataSetChanged(true);
    }

    public void notifyDataSetChanged(boolean releaseCursors) {
        if (releaseCursors) {
            releaseCursorHelpers();
        }
        super.notifyDataSetChanged();
    }

    public void notifyDataSetInvalidated() {
        releaseCursorHelpers();
        super.notifyDataSetInvalidated();
    }

    public void onGroupCollapsed(int groupPosition) {
        deactivateChildrenCursorHelper(groupPosition);
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized void deactivateChildrenCursorHelper(int groupPosition) {
        MyCursorHelper cursorHelper = getChildrenCursorHelper(groupPosition, true);
        this.mChildrenCursorHelpers.remove(groupPosition);
        cursorHelper.deactivate();
    }

    public String convertToString(Cursor cursor) {
        return cursor == null ? "" : cursor.toString();
    }

    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        FilterQueryProvider filterQueryProvider = this.mFilterQueryProvider;
        if (filterQueryProvider != null) {
            return filterQueryProvider.runQuery(constraint);
        }
        return this.mGroupCursorHelper.getCursor();
    }

    public Filter getFilter() {
        if (this.mCursorFilter == null) {
            this.mCursorFilter = new CursorFilter(this);
        }
        return this.mCursorFilter;
    }

    public FilterQueryProvider getFilterQueryProvider() {
        return this.mFilterQueryProvider;
    }

    public void setFilterQueryProvider(FilterQueryProvider filterQueryProvider) {
        this.mFilterQueryProvider = filterQueryProvider;
    }

    public void changeCursor(Cursor cursor) {
        this.mGroupCursorHelper.changeCursor(cursor, true);
    }

    public Cursor getCursor() {
        return this.mGroupCursorHelper.getCursor();
    }
}
