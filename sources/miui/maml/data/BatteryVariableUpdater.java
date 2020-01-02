package miui.maml.data;

import android.content.Context;
import android.content.Intent;

public class BatteryVariableUpdater extends NotifierVariableUpdater {
    public static final String USE_TAG = "Battery";
    private IndexedVariable mBatteryLevel = new IndexedVariable(VariableNames.BATTERY_LEVEL, getRoot().getContext().mVariables, true);
    private int mLevel;

    public BatteryVariableUpdater(VariableUpdaterManager m) {
        super(m, Intent.ACTION_BATTERY_CHANGED);
    }

    public void onNotify(Context context, Intent intent, Object o) {
        if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
            int level = intent.getIntExtra("level", -1);
            if (level != -1 && this.mLevel != level) {
                this.mBatteryLevel.set(level >= 100 ? 100.0d : (double) level);
                this.mLevel = level;
                getRoot().requestUpdate();
            }
        }
    }
}
