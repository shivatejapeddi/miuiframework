package com.android.internal.app;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import com.android.internal.R;

public class ResolverTargetActionsDialogFragment extends DialogFragment implements OnClickListener {
    private static final int APP_INFO_INDEX = 0;
    private static final String NAME_KEY = "componentName";
    private static final String TITLE_KEY = "title";

    public ResolverTargetActionsDialogFragment(CharSequence title, ComponentName name) {
        Bundle args = new Bundle();
        args.putCharSequence("title", title);
        args.putParcelable(NAME_KEY, name);
        setArguments(args);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Builder(getContext()).setCancelable(true).setItems((int) R.array.resolver_target_actions, (OnClickListener) this).setTitle(getArguments().getCharSequence("title")).create();
    }

    public void onClick(DialogInterface dialog, int which) {
        ComponentName name = (ComponentName) getArguments().getParcelable(NAME_KEY);
        if (which == 0) {
            startActivity(new Intent().setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.fromParts("package", name.getPackageName(), null)).addFlags(524288));
        }
        dismiss();
    }
}
