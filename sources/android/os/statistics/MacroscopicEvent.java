package android.os.statistics;

import android.os.statistics.PerfEvent.DetailFields;

public abstract class MacroscopicEvent<T extends DetailFields> extends PerfEvent<T> {
    protected MacroscopicEvent(int _eventType, T _fields) {
        super(_eventType, _fields);
    }
}
