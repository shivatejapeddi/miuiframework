package com.android.internal.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.media.MediaRouter;
import android.media.MediaRouter.RouteInfo;
import android.util.Log;
import android.view.View.OnClickListener;

public abstract class MediaRouteDialogPresenter {
    private static final String CHOOSER_FRAGMENT_TAG = "android.app.MediaRouteButton:MediaRouteChooserDialogFragment";
    private static final String CONTROLLER_FRAGMENT_TAG = "android.app.MediaRouteButton:MediaRouteControllerDialogFragment";
    private static final String TAG = "MediaRouter";

    public static DialogFragment showDialogFragment(Activity activity, int routeTypes, OnClickListener extendedSettingsClickListener) {
        MediaRouter router = (MediaRouter) activity.getSystemService(Context.MEDIA_ROUTER_SERVICE);
        FragmentManager fm = activity.getFragmentManager();
        RouteInfo route = router.getSelectedRoute();
        boolean isDefault = route.isDefault();
        String str = TAG;
        String str2;
        if (isDefault || !route.matchesTypes(routeTypes)) {
            str2 = CHOOSER_FRAGMENT_TAG;
            if (fm.findFragmentByTag(str2) != null) {
                Log.w(str, "showDialog(): Route chooser dialog already showing!");
                return null;
            }
            MediaRouteChooserDialogFragment f = new MediaRouteChooserDialogFragment();
            f.setRouteTypes(routeTypes);
            f.setExtendedSettingsClickListener(extendedSettingsClickListener);
            f.show(fm, str2);
            return f;
        }
        str2 = CONTROLLER_FRAGMENT_TAG;
        if (fm.findFragmentByTag(str2) != null) {
            Log.w(str, "showDialog(): Route controller dialog already showing!");
            return null;
        }
        MediaRouteControllerDialogFragment f2 = new MediaRouteControllerDialogFragment();
        f2.show(fm, str2);
        return f2;
    }

    public static Dialog createDialog(Context context, int routeTypes, OnClickListener extendedSettingsClickListener) {
        int theme;
        MediaRouter router = (MediaRouter) context.getSystemService(Context.MEDIA_ROUTER_SERVICE);
        if (MediaRouteChooserDialog.isLightTheme(context)) {
            theme = 16974130;
        } else {
            theme = 16974126;
        }
        RouteInfo route = router.getSelectedRoute();
        if (!route.isDefault() && route.matchesTypes(routeTypes)) {
            return new MediaRouteControllerDialog(context, theme);
        }
        MediaRouteChooserDialog d = new MediaRouteChooserDialog(context, theme);
        d.setRouteTypes(routeTypes);
        d.setExtendedSettingsClickListener(extendedSettingsClickListener);
        return d;
    }
}
