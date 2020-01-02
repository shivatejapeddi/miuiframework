package miui.log;

import android.util.Log;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public final class LogSwitchesConfigWriter {
    protected static final String TAG = "LogSwitchesConfigWriter";
    private final String logSwitchesFileName;
    private final String logSwitchesFilePath;
    private final String logSwitchesFolder;

    public LogSwitchesConfigWriter(String logSwitchesFolder, String logSwitchesFileName) {
        this.logSwitchesFolder = logSwitchesFolder;
        this.logSwitchesFileName = logSwitchesFileName;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(logSwitchesFolder);
        stringBuilder.append("/");
        stringBuilder.append(logSwitchesFileName);
        this.logSwitchesFilePath = stringBuilder.toString();
    }

    public synchronized void write(HashMap<String, AppLogSwitches> newLogSwitchesConfig) {
        IOException ex;
        Throwable th;
        BufferedWriter writer = null;
        try {
            Utils.createLogSwitchesFileIfNotExisted(this.logSwitchesFolder, this.logSwitchesFilePath);
            writer = new BufferedWriter(new FileWriter(this.logSwitchesFilePath));
            for (AppLogSwitches appLogSwitches : newLogSwitchesConfig.values()) {
                try {
                    writer.write(appLogSwitches.toString());
                    writer.newLine();
                } catch (IOException e) {
                    ex = e;
                    try {
                        String str = TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("failed to write ");
                        stringBuilder.append(this.logSwitchesFilePath);
                        Log.e(str, stringBuilder.toString(), ex);
                        if (writer != null) {
                            writer.close();
                        }
                        return;
                    } catch (Throwable th2) {
                        th = th2;
                    }
                }
            }
            writer.flush();
            try {
                writer.close();
            } catch (IOException e2) {
            }
        } catch (IOException e3) {
            ex = e3;
        } catch (Throwable th3) {
            th = th3;
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e4) {
                }
            }
            throw th;
        }
        return;
    }
}
