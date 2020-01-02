package com.miui.internal.search;

import android.database.AbstractCursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.DatabaseUtils;
import android.os.Bundle;
import java.util.LinkedList;

public class RankedCursor extends AbstractCursor {
    private final int columnCount;
    private final String[] columnNames;
    private LinkedList<ScoredData> data = new LinkedList();

    private static class ScoredData implements Comparable<ScoredData> {
        private String[] data;
        private double score;

        ScoredData(double score, String[] data) {
            this.score = score;
            this.data = data;
        }

        private Object get(int column) {
            return this.data[column];
        }

        public double getScore() {
            return this.score;
        }

        public int compareTo(ScoredData o) {
            double d = this.score;
            double d2 = o.score;
            if (d == d2) {
                return 0;
            }
            if (d < d2) {
                return -1;
            }
            return 1;
        }
    }

    public RankedCursor(String[] columnNames) {
        this.columnNames = columnNames;
        this.columnCount = columnNames.length;
        setExtras(new Bundle());
    }

    public void addRow(double score, String[] columnValues) {
        if (columnValues.length == this.columnCount) {
            ScoredData newData = new ScoredData(score, columnValues);
            for (int i = 0; i < this.data.size(); i++) {
                if (newData.compareTo((ScoredData) this.data.get(i)) > 0) {
                    this.data.add(i, newData);
                    return;
                }
            }
            this.data.add(newData);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("columnNames.length = ");
        stringBuilder.append(this.columnCount);
        stringBuilder.append(", columnValues.length = ");
        stringBuilder.append(columnValues.length);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public double getScore() {
        int position = getPosition();
        if (position < 0) {
            throw new CursorIndexOutOfBoundsException("Before first row.");
        } else if (position < getCount()) {
            LinkedList linkedList = this.data;
            if (linkedList == null || linkedList.isEmpty()) {
                return 0.0d;
            }
            return ((ScoredData) this.data.get(position)).getScore();
        } else {
            throw new CursorIndexOutOfBoundsException("After last row.");
        }
    }

    private Object get(int column) {
        if (column < 0 || column >= this.columnCount) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Requested column: ");
            stringBuilder.append(column);
            stringBuilder.append(", # of columns: ");
            stringBuilder.append(this.columnCount);
            throw new CursorIndexOutOfBoundsException(stringBuilder.toString());
        }
        int position = getPosition();
        if (position < 0) {
            throw new CursorIndexOutOfBoundsException("Before first row.");
        } else if (position < getCount()) {
            return ((ScoredData) this.data.get(position)).get(column);
        } else {
            throw new CursorIndexOutOfBoundsException("After last row.");
        }
    }

    public int getCount() {
        return this.data.size();
    }

    public String[] getColumnNames() {
        return this.columnNames;
    }

    public String getString(int column) {
        Object value = get(column);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    public short getShort(int column) {
        Object value = get(column);
        if (value == null) {
            return (short) 0;
        }
        if (value instanceof Number) {
            return ((Number) value).shortValue();
        }
        return Short.parseShort(value.toString());
    }

    public int getInt(int column) {
        Object value = get(column);
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return Integer.parseInt(value.toString());
    }

    public long getLong(int column) {
        Object value = get(column);
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return Long.parseLong(value.toString());
    }

    public float getFloat(int column) {
        Object value = get(column);
        if (value == null) {
            return 0.0f;
        }
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        return Float.parseFloat(value.toString());
    }

    public double getDouble(int column) {
        Object value = get(column);
        if (value == null) {
            return 0.0d;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return Double.parseDouble(value.toString());
    }

    public byte[] getBlob(int column) {
        return (byte[]) get(column);
    }

    public int getType(int column) {
        return DatabaseUtils.getTypeOfObject(get(column));
    }

    public boolean isNull(int column) {
        return get(column) == null;
    }
}
