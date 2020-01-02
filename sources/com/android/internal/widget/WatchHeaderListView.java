package com.android.internal.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ListView.FixedViewInfo;
import java.util.ArrayList;
import java.util.function.Predicate;

public class WatchHeaderListView extends ListView {
    private View mTopPanel;

    private static class WatchHeaderListAdapter extends HeaderViewListAdapter {
        private View mTopPanel;

        public WatchHeaderListAdapter(ArrayList<FixedViewInfo> headerViewInfos, ArrayList<FixedViewInfo> footerViewInfos, ListAdapter adapter) {
            super(headerViewInfos, footerViewInfos, adapter);
        }

        public void setTopPanel(View v) {
            this.mTopPanel = v;
        }

        private int getTopPanelCount() {
            View view = this.mTopPanel;
            return (view == null || view.getVisibility() == 8) ? 0 : 1;
        }

        public int getCount() {
            return super.getCount() + getTopPanelCount();
        }

        public boolean areAllItemsEnabled() {
            return getTopPanelCount() == 0 && super.areAllItemsEnabled();
        }

        public boolean isEnabled(int position) {
            int topPanelCount = getTopPanelCount();
            return position < topPanelCount ? false : super.isEnabled(position - topPanelCount);
        }

        public Object getItem(int position) {
            int topPanelCount = getTopPanelCount();
            return position < topPanelCount ? null : super.getItem(position - topPanelCount);
        }

        public long getItemId(int position) {
            int numHeaders = getHeadersCount() + getTopPanelCount();
            if (getWrappedAdapter() != null && position >= numHeaders) {
                int adjPosition = position - numHeaders;
                if (adjPosition < getWrappedAdapter().getCount()) {
                    return getWrappedAdapter().getItemId(adjPosition);
                }
            }
            return -1;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            int topPanelCount = getTopPanelCount();
            return position < topPanelCount ? this.mTopPanel : super.getView(position - topPanelCount, convertView, parent);
        }

        public int getItemViewType(int position) {
            int numHeaders = getHeadersCount() + getTopPanelCount();
            if (getWrappedAdapter() != null && position >= numHeaders) {
                int adjPosition = position - numHeaders;
                if (adjPosition < getWrappedAdapter().getCount()) {
                    return getWrappedAdapter().getItemViewType(adjPosition);
                }
            }
            return -2;
        }
    }

    public WatchHeaderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WatchHeaderListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WatchHeaderListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /* Access modifiers changed, original: protected */
    public HeaderViewListAdapter wrapHeaderListAdapterInternal(ArrayList<FixedViewInfo> headerViewInfos, ArrayList<FixedViewInfo> footerViewInfos, ListAdapter adapter) {
        return new WatchHeaderListAdapter(headerViewInfos, footerViewInfos, adapter);
    }

    public void addView(View child, LayoutParams params) {
        if (this.mTopPanel == null) {
            setTopPanel(child);
            return;
        }
        throw new IllegalStateException("WatchHeaderListView can host only one header");
    }

    public void setTopPanel(View v) {
        this.mTopPanel = v;
        wrapAdapterIfNecessary();
    }

    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        wrapAdapterIfNecessary();
    }

    /* Access modifiers changed, original: protected */
    public View findViewTraversal(int id) {
        View v = super.findViewTraversal(id);
        if (v == null) {
            View view = this.mTopPanel;
            if (!(view == null || view.isRootNamespace())) {
                return this.mTopPanel.findViewById(id);
            }
        }
        return v;
    }

    /* Access modifiers changed, original: protected */
    public View findViewWithTagTraversal(Object tag) {
        View v = super.findViewWithTagTraversal(tag);
        if (v == null) {
            View view = this.mTopPanel;
            if (!(view == null || view.isRootNamespace())) {
                return this.mTopPanel.findViewWithTag(tag);
            }
        }
        return v;
    }

    /* Access modifiers changed, original: protected */
    public <T extends View> T findViewByPredicateTraversal(Predicate<View> predicate, View childToSkip) {
        View v = super.findViewByPredicateTraversal(predicate, childToSkip);
        if (v == null) {
            View view = this.mTopPanel;
            if (!(view == null || view == childToSkip || view.isRootNamespace())) {
                return this.mTopPanel.findViewByPredicate(predicate);
            }
        }
        return v;
    }

    public int getHeaderViewsCount() {
        if (this.mTopPanel == null) {
            return super.getHeaderViewsCount();
        }
        return super.getHeaderViewsCount() + (this.mTopPanel.getVisibility() == 8 ? 0 : 1);
    }

    private void wrapAdapterIfNecessary() {
        ListAdapter adapter = getAdapter();
        if (adapter != null && this.mTopPanel != null) {
            if (!(adapter instanceof WatchHeaderListAdapter)) {
                wrapHeaderListAdapterInternal();
            }
            ((WatchHeaderListAdapter) getAdapter()).setTopPanel(this.mTopPanel);
            dispatchDataSetObserverOnChangedInternal();
        }
    }
}
