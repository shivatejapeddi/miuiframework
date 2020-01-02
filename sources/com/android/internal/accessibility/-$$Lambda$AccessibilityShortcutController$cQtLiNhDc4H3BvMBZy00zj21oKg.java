package com.android.internal.accessibility;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AccessibilityShortcutController$cQtLiNhDc4H3BvMBZy00zj21oKg implements OnDismissListener {
    private final /* synthetic */ TtsPrompt f$0;

    public /* synthetic */ -$$Lambda$AccessibilityShortcutController$cQtLiNhDc4H3BvMBZy00zj21oKg(TtsPrompt ttsPrompt) {
        this.f$0 = ttsPrompt;
    }

    public final void onDismiss(DialogInterface dialogInterface) {
        this.f$0.dismiss();
    }
}
