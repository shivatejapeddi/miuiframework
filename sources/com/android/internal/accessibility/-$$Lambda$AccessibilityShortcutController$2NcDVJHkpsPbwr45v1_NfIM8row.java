package com.android.internal.accessibility;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AccessibilityShortcutController$2NcDVJHkpsPbwr45v1_NfIM8row implements OnClickListener {
    private final /* synthetic */ AccessibilityShortcutController f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ -$$Lambda$AccessibilityShortcutController$2NcDVJHkpsPbwr45v1_NfIM8row(AccessibilityShortcutController accessibilityShortcutController, int i) {
        this.f$0 = accessibilityShortcutController;
        this.f$1 = i;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$createShortcutWarningDialog$0$AccessibilityShortcutController(this.f$1, dialogInterface, i);
    }
}
