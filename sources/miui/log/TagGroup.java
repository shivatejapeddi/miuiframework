package miui.log;

public final class TagGroup implements ILogTag {
    private final String[] androidTags;
    private boolean isGroupOn;
    private final String[] miuiTags;
    public final String name;
    private int onNumber;

    TagGroup(String name, String[] miuiTags) {
        this(name, miuiTags, null);
    }

    TagGroup(String name, String[] miuiTags, String[] androidTags) {
        this.name = name;
        this.miuiTags = miuiTags == null ? new String[0] : miuiTags;
        this.androidTags = androidTags == null ? new String[0] : androidTags;
        this.isGroupOn = false;
        this.onNumber = 0;
    }

    public boolean isOn() {
        return this.isGroupOn;
    }

    public synchronized void switchOn() {
        this.onNumber++;
        boolean needSwitchOnTags = false;
        int i = 0;
        if (this.onNumber == 0) {
            this.isGroupOn = false;
            needSwitchOnTags = true;
        } else if (this.onNumber == 1) {
            this.isGroupOn = true;
            needSwitchOnTags = true;
        }
        if (needSwitchOnTags) {
            for (String androidTag : this.androidTags) {
                AndroidTags.switchOn(androidTag);
            }
            String[] strArr = this.miuiTags;
            int length = strArr.length;
            while (i < length) {
                MiuiTags.switchOn(strArr[i]);
                i++;
            }
        }
    }

    public synchronized void switchOff() {
        this.onNumber--;
        boolean needSwitchOffTags = false;
        int i = 0;
        if (this.onNumber == 0) {
            this.isGroupOn = false;
            needSwitchOffTags = true;
        } else if (this.onNumber == -1) {
            this.isGroupOn = false;
            needSwitchOffTags = true;
        }
        if (needSwitchOffTags) {
            for (String miuiTag : this.miuiTags) {
                MiuiTags.switchOff(miuiTag);
            }
            String[] strArr = this.androidTags;
            int length = strArr.length;
            while (i < length) {
                AndroidTags.switchOff(strArr[i]);
                i++;
            }
        }
    }
}
