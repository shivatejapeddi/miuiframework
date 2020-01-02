package com.android.internal.util;

import android.annotation.UnsupportedAppUsage;
import android.os.Message;

public class State implements IState {
    @UnsupportedAppUsage
    protected State() {
    }

    @UnsupportedAppUsage
    public void enter() {
    }

    @UnsupportedAppUsage
    public void exit() {
    }

    @UnsupportedAppUsage
    public boolean processMessage(Message msg) {
        return false;
    }

    @UnsupportedAppUsage
    public String getName() {
        String name = getClass().getName();
        return name.substring(name.lastIndexOf(36) + 1);
    }
}
