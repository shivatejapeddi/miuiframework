package miui.telephony;

public interface DefaultSlotSelector {
    int getDefaultDataSlot(int[] iArr, int i);

    void onSimRemoved(int i, String[] strArr);
}
