package com.android.internal.os;

import android.os.Process;
import com.android.internal.util.ArrayUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProcTimeInStateReader {
    private static final String TAG = "ProcTimeInStateReader";
    private static final List<Integer> TIME_IN_STATE_HEADER_LINE_FORMAT;
    private static final List<Integer> TIME_IN_STATE_LINE_FREQUENCY_FORMAT;
    private static final List<Integer> TIME_IN_STATE_LINE_TIME_FORMAT = Arrays.asList(new Integer[]{Integer.valueOf(32), Integer.valueOf(8202)});
    private long[] mFrequenciesKhz;
    private int[] mTimeInStateTimeFormat;

    static {
        Integer[] numArr = new Integer[2];
        numArr[0] = Integer.valueOf(8224);
        Integer valueOf = Integer.valueOf(10);
        numArr[1] = valueOf;
        TIME_IN_STATE_LINE_FREQUENCY_FORMAT = Arrays.asList(numArr);
        TIME_IN_STATE_HEADER_LINE_FORMAT = Collections.singletonList(valueOf);
    }

    public ProcTimeInStateReader(Path initialTimeInStateFile) throws IOException {
        initializeTimeInStateFormat(initialTimeInStateFile);
    }

    public long[] getUsageTimesMillis(Path timeInStatePath) {
        long[] readLongs = new long[this.mFrequenciesKhz.length];
        if (!Process.readProcFile(timeInStatePath.toString(), this.mTimeInStateTimeFormat, null, readLongs, null)) {
            return null;
        }
        for (int i = 0; i < readLongs.length; i++) {
            readLongs[i] = readLongs[i] * 10;
        }
        return readLongs;
    }

    public long[] getFrequenciesKhz() {
        return this.mFrequenciesKhz;
    }

    private void initializeTimeInStateFormat(Path timeInStatePath) throws IOException {
        byte[] timeInStateBytes = Files.readAllBytes(timeInStatePath);
        ArrayList<Integer> timeInStateFrequencyFormat = new ArrayList();
        ArrayList<Integer> timeInStateTimeFormat = new ArrayList();
        int i = 0;
        int numFrequencies = 0;
        while (i < timeInStateBytes.length) {
            if (Character.isDigit(timeInStateBytes[i])) {
                timeInStateFrequencyFormat.addAll(TIME_IN_STATE_LINE_FREQUENCY_FORMAT);
                timeInStateTimeFormat.addAll(TIME_IN_STATE_LINE_TIME_FORMAT);
                numFrequencies++;
            } else {
                timeInStateFrequencyFormat.addAll(TIME_IN_STATE_HEADER_LINE_FORMAT);
                timeInStateTimeFormat.addAll(TIME_IN_STATE_HEADER_LINE_FORMAT);
            }
            while (i < timeInStateBytes.length && timeInStateBytes[i] != (byte) 10) {
                i++;
            }
            i++;
        }
        if (numFrequencies != 0) {
            long[] readLongs = new long[numFrequencies];
            if (Process.parseProcLine(timeInStateBytes, 0, timeInStateBytes.length, ArrayUtils.convertToIntArray(timeInStateFrequencyFormat), null, readLongs, null)) {
                this.mTimeInStateTimeFormat = ArrayUtils.convertToIntArray(timeInStateTimeFormat);
                this.mFrequenciesKhz = readLongs;
                return;
            }
            throw new IOException("Failed to parse time_in_state file");
        }
        throw new IOException("Empty time_in_state file");
    }
}
