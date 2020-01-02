package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView.FixedViewInfo;
import java.util.ArrayList;
import java.util.Iterator;

public class HeaderViewListAdapter implements WrapperListAdapter, Filterable {
    static final ArrayList<FixedViewInfo> EMPTY_INFO_LIST = new ArrayList();
    @UnsupportedAppUsage
    private final ListAdapter mAdapter;
    boolean mAreAllFixedViewsSelectable;
    @UnsupportedAppUsage
    ArrayList<FixedViewInfo> mFooterViewInfos;
    @UnsupportedAppUsage
    ArrayList<FixedViewInfo> mHeaderViewInfos;
    private final boolean mIsFilterable;

    public HeaderViewListAdapter(ArrayList<FixedViewInfo> headerViewInfos, ArrayList<FixedViewInfo> footerViewInfos, ListAdapter adapter) {
        this.mAdapter = adapter;
        this.mIsFilterable = adapter instanceof Filterable;
        if (headerViewInfos == null) {
            this.mHeaderViewInfos = EMPTY_INFO_LIST;
        } else {
            this.mHeaderViewInfos = headerViewInfos;
        }
        if (footerViewInfos == null) {
            this.mFooterViewInfos = EMPTY_INFO_LIST;
        } else {
            this.mFooterViewInfos = footerViewInfos;
        }
        boolean z = areAllListInfosSelectable(this.mHeaderViewInfos) && areAllListInfosSelectable(this.mFooterViewInfos);
        this.mAreAllFixedViewsSelectable = z;
    }

    public int getHeadersCount() {
        return this.mHeaderViewInfos.size();
    }

    public int getFootersCount() {
        return this.mFooterViewInfos.size();
    }

    public boolean isEmpty() {
        ListAdapter listAdapter = this.mAdapter;
        return listAdapter == null || listAdapter.isEmpty();
    }

    private boolean areAllListInfosSelectable(ArrayList<FixedViewInfo> infos) {
        if (infos != null) {
            Iterator it = infos.iterator();
            while (it.hasNext()) {
                if (!((FixedViewInfo) it.next()).isSelectable) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean removeHeader(View v) {
        int i = 0;
        while (true) {
            boolean z = false;
            if (i >= this.mHeaderViewInfos.size()) {
                return false;
            }
            if (((FixedViewInfo) this.mHeaderViewInfos.get(i)).view == v) {
                this.mHeaderViewInfos.remove(i);
                if (areAllListInfosSelectable(this.mHeaderViewInfos) && areAllListInfosSelectable(this.mFooterViewInfos)) {
                    z = true;
                }
                this.mAreAllFixedViewsSelectable = z;
                return true;
            }
            i++;
        }
    }

    public boolean removeFooter(View v) {
        int i = 0;
        while (true) {
            boolean z = false;
            if (i >= this.mFooterViewInfos.size()) {
                return false;
            }
            if (((FixedViewInfo) this.mFooterViewInfos.get(i)).view == v) {
                this.mFooterViewInfos.remove(i);
                if (areAllListInfosSelectable(this.mHeaderViewInfos) && areAllListInfosSelectable(this.mFooterViewInfos)) {
                    z = true;
                }
                this.mAreAllFixedViewsSelectable = z;
                return true;
            }
            i++;
        }
    }

    public int getCount() {
        if (this.mAdapter != null) {
            return (getFootersCount() + getHeadersCount()) + this.mAdapter.getCount();
        }
        return getFootersCount() + getHeadersCount();
    }

    public boolean areAllItemsEnabled() {
        ListAdapter listAdapter = this.mAdapter;
        boolean z = true;
        if (listAdapter == null) {
            return true;
        }
        if (!(this.mAreAllFixedViewsSelectable && listAdapter.areAllItemsEnabled())) {
            z = false;
        }
        return z;
    }

    public boolean isEnabled(int position) {
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            return ((FixedViewInfo) this.mHeaderViewInfos.get(position)).isSelectable;
        }
        int adjPosition = position - numHeaders;
        int adapterCount = 0;
        ListAdapter listAdapter = this.mAdapter;
        if (listAdapter != null) {
            adapterCount = listAdapter.getCount();
            if (adjPosition < adapterCount) {
                return this.mAdapter.isEnabled(adjPosition);
            }
        }
        return ((FixedViewInfo) this.mFooterViewInfos.get(adjPosition - adapterCount)).isSelectable;
    }

    public Object getItem(int position) {
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            return ((FixedViewInfo) this.mHeaderViewInfos.get(position)).data;
        }
        int adjPosition = position - numHeaders;
        int adapterCount = 0;
        ListAdapter listAdapter = this.mAdapter;
        if (listAdapter != null) {
            adapterCount = listAdapter.getCount();
            if (adjPosition < adapterCount) {
                return this.mAdapter.getItem(adjPosition);
            }
        }
        return ((FixedViewInfo) this.mFooterViewInfos.get(adjPosition - adapterCount)).data;
    }

    public long getItemId(int position) {
        int numHeaders = getHeadersCount();
        int adapterCount = this.mAdapter;
        if (adapterCount != 0 && position >= numHeaders) {
            int adjPosition = position - numHeaders;
            if (adjPosition < adapterCount.getCount()) {
                return this.mAdapter.getItemId(adjPosition);
            }
        }
        return -1;
    }

    public boolean hasStableIds() {
        ListAdapter listAdapter = this.mAdapter;
        if (listAdapter != null) {
            return listAdapter.hasStableIds();
        }
        return false;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            return ((FixedViewInfo) this.mHeaderViewInfos.get(position)).view;
        }
        int adjPosition = position - numHeaders;
        int adapterCount = 0;
        ListAdapter listAdapter = this.mAdapter;
        if (listAdapter != null) {
            adapterCount = listAdapter.getCount();
            if (adjPosition < adapterCount) {
                return this.mAdapter.getView(adjPosition, convertView, parent);
            }
        }
        return ((FixedViewInfo) this.mFooterViewInfos.get(adjPosition - adapterCount)).view;
    }

    public int getItemViewType(int position) {
        int numHeaders = getHeadersCount();
        int adapterCount = this.mAdapter;
        if (adapterCount != 0 && position >= numHeaders) {
            int adjPosition = position - numHeaders;
            if (adjPosition < adapterCount.getCount()) {
                return this.mAdapter.getItemViewType(adjPosition);
            }
        }
        return -2;
    }

    public int getViewTypeCount() {
        ListAdapter listAdapter = this.mAdapter;
        if (listAdapter != null) {
            return listAdapter.getViewTypeCount();
        }
        return 1;
    }

    public void registerDataSetObserver(DataSetObserver observer) {
        ListAdapter listAdapter = this.mAdapter;
        if (listAdapter != null) {
            listAdapter.registerDataSetObserver(observer);
        }
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        ListAdapter listAdapter = this.mAdapter;
        if (listAdapter != null) {
            listAdapter.unregisterDataSetObserver(observer);
        }
    }

    public Filter getFilter() {
        if (this.mIsFilterable) {
            return ((Filterable) this.mAdapter).getFilter();
        }
        return null;
    }

    public ListAdapter getWrappedAdapter() {
        return this.mAdapter;
    }
}
