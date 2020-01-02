package com.android.internal.os;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ZygoteConnection$xjqM7qW7vAjTqh2tR5XRF5Vn5mk implements Runnable {
    private final /* synthetic */ String[] f$0;

    public /* synthetic */ -$$Lambda$ZygoteConnection$xjqM7qW7vAjTqh2tR5XRF5Vn5mk(String[] strArr) {
        this.f$0 = strArr;
    }

    public final void run() {
        ZygoteInit.setApiBlacklistExemptions(this.f$0);
    }
}
