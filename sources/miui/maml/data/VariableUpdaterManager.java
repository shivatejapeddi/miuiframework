package miui.maml.data;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Iterator;
import miui.maml.ScreenElementRoot;

public class VariableUpdaterManager {
    public static final String USE_TAG_NONE = "none";
    private ScreenElementRoot mRoot;
    private ArrayList<VariableUpdater> mUpdaters = new ArrayList();

    public VariableUpdaterManager(ScreenElementRoot c) {
        this.mRoot = c;
    }

    public ScreenElementRoot getRoot() {
        return this.mRoot;
    }

    public void add(VariableUpdater updater) {
        this.mUpdaters.add(updater);
    }

    public void remove(VariableUpdater updater) {
        this.mUpdaters.remove(updater);
    }

    public void init() {
        Iterator it = this.mUpdaters.iterator();
        while (it.hasNext()) {
            ((VariableUpdater) it.next()).init();
        }
    }

    public void tick(long currentTime) {
        Iterator it = this.mUpdaters.iterator();
        while (it.hasNext()) {
            ((VariableUpdater) it.next()).tick(currentTime);
        }
    }

    public void resume() {
        Iterator it = this.mUpdaters.iterator();
        while (it.hasNext()) {
            ((VariableUpdater) it.next()).resume();
        }
    }

    public void pause() {
        Iterator it = this.mUpdaters.iterator();
        while (it.hasNext()) {
            ((VariableUpdater) it.next()).pause();
        }
    }

    public void finish() {
        Iterator it = this.mUpdaters.iterator();
        while (it.hasNext()) {
            ((VariableUpdater) it.next()).finish();
        }
    }

    public void addFromTag(String tag) {
        if (!TextUtils.isEmpty(tag) && !"none".equalsIgnoreCase(tag)) {
            for (String s : tag.split(",")) {
                String s2 = s2.trim();
                String name = s2;
                String property = null;
                int dotPos = s2.indexOf(46);
                if (dotPos != -1) {
                    name = s2.substring(0, dotPos);
                    property = s2.substring(dotPos + 1);
                }
                if (name.equals("DateTime")) {
                    add(new DateTimeVariableUpdater(this, property));
                } else if (name.equals(BatteryVariableUpdater.USE_TAG)) {
                    add(new BatteryVariableUpdater(this));
                }
            }
        }
    }
}
