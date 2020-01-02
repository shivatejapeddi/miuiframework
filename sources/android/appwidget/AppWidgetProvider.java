package android.appwidget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AppWidgetProvider extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        boolean equals = AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action);
        int[] newIds = AppWidgetManager.EXTRA_APPWIDGET_IDS;
        Bundle extras;
        if (equals) {
            extras = intent.getExtras();
            if (extras != null) {
                newIds = extras.getIntArray(newIds);
                if (newIds != null && newIds.length > 0) {
                    onUpdate(context, AppWidgetManager.getInstance(context), newIds);
                    return;
                }
                return;
            }
            return;
        }
        equals = AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action);
        int appWidgetId = AppWidgetManager.EXTRA_APPWIDGET_ID;
        if (equals) {
            extras = intent.getExtras();
            if (extras != null && extras.containsKey(appWidgetId)) {
                int appWidgetId2 = extras.getInt(appWidgetId);
                onDeleted(context, new int[]{appWidgetId2});
            }
        } else if (AppWidgetManager.ACTION_APPWIDGET_OPTIONS_CHANGED.equals(action)) {
            extras = intent.getExtras();
            if (extras != null && extras.containsKey(appWidgetId)) {
                Bundle widgetExtras = AppWidgetManager.EXTRA_APPWIDGET_OPTIONS;
                if (extras.containsKey(widgetExtras)) {
                    onAppWidgetOptionsChanged(context, AppWidgetManager.getInstance(context), extras.getInt(appWidgetId), extras.getBundle(widgetExtras));
                }
            }
        } else if (AppWidgetManager.ACTION_APPWIDGET_ENABLED.equals(action)) {
            onEnabled(context);
        } else if (AppWidgetManager.ACTION_APPWIDGET_DISABLED.equals(action)) {
            onDisabled(context);
        } else if (AppWidgetManager.ACTION_APPWIDGET_RESTORED.equals(action)) {
            extras = intent.getExtras();
            if (extras != null) {
                int[] oldIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_OLD_IDS);
                newIds = extras.getIntArray(newIds);
                if (oldIds != null && oldIds.length > 0) {
                    onRestored(context, oldIds, newIds);
                    onUpdate(context, AppWidgetManager.getInstance(context), newIds);
                }
            }
        }
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    }

    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
    }

    public void onDeleted(Context context, int[] appWidgetIds) {
    }

    public void onEnabled(Context context) {
    }

    public void onDisabled(Context context) {
    }

    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
    }
}
