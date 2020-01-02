package miui.log;

public final class MiuiTag implements ILogTag {
    public final boolean defaultOn;
    public final int id;
    private boolean isTagOn;
    public final String name;
    private int onNumber = 0;

    MiuiTag(int id, String name, boolean defaultOn) {
        this.id = id;
        this.name = name;
        this.defaultOn = defaultOn;
        this.isTagOn = defaultOn;
    }

    public boolean isOn() {
        return this.isTagOn;
    }

    public synchronized void switchOn() {
        this.onNumber++;
        if (this.onNumber == 0) {
            this.isTagOn = this.defaultOn;
        } else if (this.onNumber == 1) {
            this.isTagOn = true;
        }
    }

    public synchronized void switchOff() {
        this.onNumber--;
        if (this.onNumber == 0) {
            this.isTagOn = this.defaultOn;
        } else if (this.onNumber == -1) {
            this.isTagOn = false;
        }
    }
}
