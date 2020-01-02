package com.android.internal.accessibility;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AccessibilityShortcutController$T96D356-n5VObNOonEIYV8s83Fc implements OnCancelListener {
    private final /* synthetic */ AccessibilityShortcutController f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ -$$Lambda$AccessibilityShortcutController$T96D356-n5VObNOonEIYV8s83Fc(AccessibilityShortcutController accessibilityShortcutController, int i) {
        this.f$0 = accessibilityShortcutController;
        this.f$1 = i;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        this.f$0.lambda$createShortcutWarningDialog$1$AccessibilityShortcutController(this.f$1, dialogInterface);
    }
}
