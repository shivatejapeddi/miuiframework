package miui.log;

import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public final class LogSwitchesConfigParser {
    private static final String TAG = "LogSwitchesConfigParser";

    public static HashMap<String, AppLogSwitches> parseLogSwitchesConfig(String configFilePath) {
        StringBuilder stringBuilder;
        HashMap hashMap;
        String str = "";
        String str2 = TAG;
        if (!new File(configFilePath).exists()) {
            return new HashMap();
        }
        BufferedReader reader = null;
        try {
            ArrayList<String> lines = new ArrayList();
            reader = new BufferedReader(new FileReader(configFilePath));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                lines.add(line.replace("\r", str).replace("\n", str));
            }
            HashMap<String, AppLogSwitches> config = new HashMap();
            Iterator it = lines.iterator();
            while (it.hasNext()) {
                AppLogSwitches appConfig = AppLogSwitches.parseAppLogSwitches((String) it.next());
                if (appConfig != null) {
                    AppLogSwitches existing = (AppLogSwitches) config.get(appConfig.uniqueName);
                    if (existing != null) {
                        existing.merge(appConfig);
                    } else {
                        config.put(appConfig.uniqueName, appConfig);
                    }
                }
            }
            try {
                reader.close();
            } catch (IOException e) {
            }
            return config;
        } catch (FileNotFoundException ex) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("cannot found ");
            stringBuilder.append(configFilePath);
            Log.e(str2, stringBuilder.toString(), ex);
            hashMap = new HashMap();
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e2) {
                }
            }
            return hashMap;
        } catch (IOException ex2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("failed to read ");
            stringBuilder.append(configFilePath);
            Log.e(str2, stringBuilder.toString(), ex2);
            hashMap = new HashMap();
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e3) {
                }
            }
            return hashMap;
        } catch (Throwable th) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e4) {
                }
            }
        }
    }
}
