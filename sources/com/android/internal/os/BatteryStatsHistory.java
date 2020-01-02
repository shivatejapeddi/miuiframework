package com.android.internal.os;

import android.os.BatteryStats.HistoryItem;
import android.os.Parcel;
import android.os.StatFs;
import android.os.SystemClock;
import android.util.ArraySet;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.util.ParseUtils;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@VisibleForTesting(visibility = Visibility.PACKAGE)
public class BatteryStatsHistory {
    private static final boolean DEBUG = false;
    public static final String FILE_SUFFIX = ".bin";
    public static final String HISTORY_DIR = "battery-history";
    private static final int MIN_FREE_SPACE = 104857600;
    private static final String TAG = "BatteryStatsHistory";
    private AtomicFile mActiveFile;
    private int mCurrentFileIndex;
    private Parcel mCurrentParcel;
    private int mCurrentParcelEnd;
    private final List<Integer> mFileNumbers = new ArrayList();
    private final Parcel mHistoryBuffer;
    private final File mHistoryDir;
    private List<Parcel> mHistoryParcels = null;
    private int mParcelIndex = 0;
    private int mRecordCount = 0;
    private final BatteryStatsImpl mStats;

    public BatteryStatsHistory(BatteryStatsImpl stats, File systemDir, Parcel historyBuffer) {
        this.mStats = stats;
        this.mHistoryBuffer = historyBuffer;
        this.mHistoryDir = new File(systemDir, HISTORY_DIR);
        this.mHistoryDir.mkdirs();
        if (!this.mHistoryDir.exists()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("HistoryDir does not exist:");
            stringBuilder.append(this.mHistoryDir.getPath());
            Slog.wtf(TAG, stringBuilder.toString());
        }
        final Set<Integer> dedup = new ArraySet();
        this.mHistoryDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                int b = name.lastIndexOf(BatteryStatsHistory.FILE_SUFFIX);
                if (b <= 0) {
                    return false;
                }
                Integer c = Integer.valueOf(ParseUtils.parseInt(name.substring(0, b), -1));
                if (c.intValue() == -1) {
                    return false;
                }
                dedup.add(c);
                return true;
            }
        });
        if (dedup.isEmpty()) {
            this.mFileNumbers.add(Integer.valueOf(0));
            setActiveFile(0);
            return;
        }
        this.mFileNumbers.addAll(dedup);
        Collections.sort(this.mFileNumbers);
        List list = this.mFileNumbers;
        setActiveFile(((Integer) list.get(list.size() - 1)).intValue());
    }

    public BatteryStatsHistory(BatteryStatsImpl stats, Parcel historyBuffer) {
        this.mStats = stats;
        this.mHistoryDir = null;
        this.mHistoryBuffer = historyBuffer;
    }

    private void setActiveFile(int fileNumber) {
        this.mActiveFile = getFile(fileNumber);
    }

    private AtomicFile getFile(int num) {
        File file = this.mHistoryDir;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(num);
        stringBuilder.append(FILE_SUFFIX);
        return new AtomicFile(new File(file, stringBuilder.toString()));
    }

    public void startNextFile() {
        if (this.mFileNumbers.isEmpty()) {
            Slog.wtf(TAG, "mFileNumbers should never be empty");
            return;
        }
        List list = this.mFileNumbers;
        int next = ((Integer) list.get(list.size() - 1)).intValue() + 1;
        this.mFileNumbers.add(Integer.valueOf(next));
        setActiveFile(next);
        if (!hasFreeDiskSpace()) {
            getFile(((Integer) this.mFileNumbers.remove(0)).intValue()).delete();
        }
        while (this.mFileNumbers.size() > this.mStats.mConstants.MAX_HISTORY_FILES) {
            getFile(((Integer) this.mFileNumbers.get(0)).intValue()).delete();
            this.mFileNumbers.remove(0);
        }
    }

    public void resetAllFiles() {
        for (Integer i : this.mFileNumbers) {
            getFile(i.intValue()).delete();
        }
        this.mFileNumbers.clear();
        this.mFileNumbers.add(Integer.valueOf(0));
        setActiveFile(0);
    }

    public boolean startIteratingHistory() {
        this.mRecordCount = 0;
        this.mCurrentFileIndex = 0;
        this.mCurrentParcel = null;
        this.mCurrentParcelEnd = 0;
        this.mParcelIndex = 0;
        return true;
    }

    public void finishIteratingHistory() {
        Parcel parcel = this.mHistoryBuffer;
        parcel.setDataPosition(parcel.dataSize());
    }

    public Parcel getNextParcel(HistoryItem out) {
        List list;
        int curPos;
        if (this.mRecordCount == 0) {
            out.clear();
        }
        this.mRecordCount++;
        Parcel parcel = this.mCurrentParcel;
        if (parcel != null) {
            if (parcel.dataPosition() < this.mCurrentParcelEnd) {
                return this.mCurrentParcel;
            }
            parcel = this.mHistoryBuffer;
            Parcel parcel2 = this.mCurrentParcel;
            if (parcel == parcel2) {
                return null;
            }
            list = this.mHistoryParcels;
            if (list == null || !list.contains(parcel2)) {
                this.mCurrentParcel.recycle();
            }
        }
        while (this.mCurrentFileIndex < this.mFileNumbers.size() - 1) {
            this.mCurrentParcel = null;
            this.mCurrentParcelEnd = 0;
            parcel = Parcel.obtain();
            List list2 = this.mFileNumbers;
            int i = this.mCurrentFileIndex;
            this.mCurrentFileIndex = i + 1;
            if (readFileToParcel(parcel, getFile(((Integer) list2.get(i)).intValue()))) {
                i = parcel.readInt();
                curPos = parcel.dataPosition();
                this.mCurrentParcelEnd = curPos + i;
                this.mCurrentParcel = parcel;
                if (curPos < this.mCurrentParcelEnd) {
                    return this.mCurrentParcel;
                }
            } else {
                parcel.recycle();
            }
        }
        if (this.mHistoryParcels != null) {
            while (this.mParcelIndex < this.mHistoryParcels.size()) {
                list = this.mHistoryParcels;
                int i2 = this.mParcelIndex;
                this.mParcelIndex = i2 + 1;
                parcel = (Parcel) list.get(i2);
                if (skipHead(parcel)) {
                    i2 = parcel.readInt();
                    curPos = parcel.dataPosition();
                    this.mCurrentParcelEnd = curPos + i2;
                    this.mCurrentParcel = parcel;
                    if (curPos < this.mCurrentParcelEnd) {
                        return this.mCurrentParcel;
                    }
                }
            }
        }
        if (this.mHistoryBuffer.dataSize() <= 0) {
            return null;
        }
        this.mHistoryBuffer.setDataPosition(0);
        this.mCurrentParcel = this.mHistoryBuffer;
        this.mCurrentParcelEnd = this.mCurrentParcel.dataSize();
        return this.mCurrentParcel;
    }

    public boolean readFileToParcel(Parcel out, AtomicFile file) {
        try {
            long start = SystemClock.uptimeMillis();
            byte[] raw = file.readFully();
            out.unmarshall(raw, 0, raw.length);
            out.setDataPosition(0);
            return skipHead(out);
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error reading file ");
            stringBuilder.append(file.getBaseFile().getPath());
            Slog.e(TAG, stringBuilder.toString(), e);
            return false;
        }
    }

    private boolean skipHead(Parcel p) {
        p.setDataPosition(0);
        int version = p.readInt();
        BatteryStatsImpl batteryStatsImpl = this.mStats;
        if (version != 186) {
            return false;
        }
        p.readLong();
        return true;
    }

    public void writeToParcel(Parcel out) {
        long start = SystemClock.uptimeMillis();
        out.writeInt(this.mFileNumbers.size() - 1);
        for (int i = 0; i < this.mFileNumbers.size() - 1; i++) {
            AtomicFile file = getFile(((Integer) this.mFileNumbers.get(i)).intValue());
            byte[] raw = new byte[null];
            try {
                raw = file.readFully();
            } catch (Exception e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error reading file ");
                stringBuilder.append(file.getBaseFile().getPath());
                Slog.e(TAG, stringBuilder.toString(), e);
            }
            out.writeByteArray(raw);
        }
    }

    public void readFromParcel(Parcel in) {
        long start = SystemClock.uptimeMillis();
        this.mHistoryParcels = new ArrayList();
        int count = in.readInt();
        for (int i = 0; i < count; i++) {
            byte[] temp = in.createByteArray();
            if (temp.length != 0) {
                Parcel p = Parcel.obtain();
                p.unmarshall(temp, 0, temp.length);
                p.setDataPosition(0);
                this.mHistoryParcels.add(p);
            }
        }
    }

    private boolean hasFreeDiskSpace() {
        return new StatFs(this.mHistoryDir.getAbsolutePath()).getAvailableBytes() > 104857600;
    }

    public List<Integer> getFilesNumbers() {
        return this.mFileNumbers;
    }

    public AtomicFile getActiveFile() {
        return this.mActiveFile;
    }

    public int getHistoryUsedSize() {
        int i;
        int ret = 0;
        for (i = 0; i < this.mFileNumbers.size() - 1; i++) {
            ret = (int) (((long) ret) + getFile(((Integer) this.mFileNumbers.get(i)).intValue()).getBaseFile().length());
        }
        ret += this.mHistoryBuffer.dataSize();
        if (this.mHistoryParcels != null) {
            for (i = 0; i < this.mHistoryParcels.size(); i++) {
                ret += ((Parcel) this.mHistoryParcels.get(i)).dataSize();
            }
        }
        return ret;
    }
}
