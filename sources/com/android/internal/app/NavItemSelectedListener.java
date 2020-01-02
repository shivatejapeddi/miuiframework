package com.android.internal.app;

import android.app.ActionBar.OnNavigationListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

class NavItemSelectedListener implements OnItemSelectedListener {
    private final OnNavigationListener mListener;

    public NavItemSelectedListener(OnNavigationListener listener) {
        this.mListener = listener;
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        OnNavigationListener onNavigationListener = this.mListener;
        if (onNavigationListener != null) {
            onNavigationListener.onNavigationItemSelected(position, id);
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
