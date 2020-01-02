package miui.util;

import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManagerNative;
import android.app.AppGlobals;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ParceledListSlice;
import android.content.pm.ResolveInfo;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AudioOutputHelper {
    private static TrackCollector COLLECTOR = null;
    private static final TrackCollector COLLECTOR_COMPAT = new CompatCollector();
    private static final TrackCollector COLLECTOR_LP = new LPCollector();
    private static final String DEFAULT_TEMP_FILE = "audio_flinger_%d_%d_%d.dump";
    private static final String TAG = AudioOutputHelper.class.getName();
    static final Result UNHANDLED = new Result(false, null);

    public static class AudioOutputClient {
        public boolean mActive;
        public final int mProcessId;
        public final int mSessionId;
        public final int mStreamType;

        public AudioOutputClient(int sessionId, int processId, int streamType, boolean active) {
            this.mSessionId = sessionId;
            this.mProcessId = processId;
            this.mStreamType = streamType;
            this.mActive = active;
        }

        public AudioOutputClient(int sessionId, int processId, int streamType) {
            this(sessionId, processId, streamType, false);
        }
    }

    interface TrackCollector {
        Result collectTracks(BufferedReader bufferedReader, String str, List<AudioOutputClient> list, Map<Integer, Integer> map) throws IOException;
    }

    static final class CompatCollector implements TrackCollector {
        public static final Pattern ACTIVE_TRACKS_FINDER_COMPAT = Pattern.compile("^Output thread 0x[\\w]+ active tracks");
        public static final Pattern TRACKS_FINDER_COMPAT = Pattern.compile("^Output thread 0x[\\w]+ tracks");
        public static final Pattern TRACK_CONTENT_FINDER_COMPAT = Pattern.compile("^(\\s|F)+\\d+\\s+\\d+\\s+(\\d+)\\s+\\d+\\s+\\w+\\s+(\\d+)\\s.+");
        public static final int TRACK_SESSION_GRP_IDX = 3;
        public static final int TRACK_STREAM_TYPE_GRP_IDX = 2;

        CompatCollector() {
        }

        public Result collectTracks(BufferedReader reader, String currentLine, List<AudioOutputClient> clients, Map<Integer, Integer> sessions) throws IOException {
            if (TRACKS_FINDER_COMPAT.matcher(currentLine).find()) {
                reader.readLine();
                return new Result(true, collectTracks(reader, (List) clients, (Map) sessions, false));
            } else if (!ACTIVE_TRACKS_FINDER_COMPAT.matcher(currentLine).find()) {
                return AudioOutputHelper.UNHANDLED;
            } else {
                reader.readLine();
                return new Result(true, collectTracks(reader, (List) clients, (Map) sessions, true));
            }
        }

        private String collectTracks(BufferedReader reader, List<AudioOutputClient> clients, Map<Integer, Integer> sessions, boolean active) throws NumberFormatException, IOException {
            String content;
            while (true) {
                String readLine = reader.readLine();
                content = readLine;
                if (readLine == null) {
                    break;
                }
                Matcher matcher = TRACK_CONTENT_FINDER_COMPAT.matcher(content);
                if (!matcher.find()) {
                    break;
                }
                int sessionId = Integer.valueOf(matcher.group(3)).intValue();
                Integer proc = (Integer) sessions.get(Integer.valueOf(sessionId));
                if (proc != null) {
                    int pid = proc.intValue();
                    int streamType = Integer.valueOf(matcher.group(2)).intValue();
                    boolean found = false;
                    if (active) {
                        for (AudioOutputClient c : clients) {
                            if (c.mSessionId == sessionId) {
                                c.mActive = active;
                                found = true;
                            }
                        }
                    }
                    if (!found) {
                        clients.add(new AudioOutputClient(sessionId, pid, streamType, active));
                    }
                }
            }
            return content;
        }
    }

    private static class DUMP_TAG {
        public static final int PID_GRP_IDX = 2;
        public static final Pattern SESSIONS_CONTENT_FINDER = Pattern.compile("^\\s+(\\d+)\\s+(\\d+)\\s+\\d+$");
        public static final Pattern SESSIONS_HEAD_FINDER = Pattern.compile("^[\\s]+session[\\s]+pid[\\s]+(cnt|count)");
        public static final int SESSION_GRP_IDX = 1;
        public static final Pattern STANDBY_FINDER = Pattern.compile("^[\\s]*[s|S]tandby: (\\w+)");
        public static final int STANDBY_GRP_IDX = 1;

        private DUMP_TAG() {
        }
    }

    static final class LPCollector implements TrackCollector {
        public static final Pattern ACTIVE_TRACKS_FINDER = Pattern.compile("^[\\s]+[\\d]+[\\s]+Tracks of which [\\d]+ are active");
        public static final int TRACK_ACTIVE_IDX = 2;
        public static final Pattern TRACK_CONTENT_FINDER = Pattern.compile("^(\\s|F)+\\d+\\s+(\\w+)\\s+\\d+\\s+(\\d+)\\s+\\d+\\s+\\d+\\s+(\\d+)\\s.+");
        public static final int TRACK_SESSION_GRP_IDX = 4;
        public static final int TRACK_STREAM_TYPE_GRP_IDX = 3;

        LPCollector() {
        }

        public Result collectTracks(BufferedReader reader, String currentLine, List<AudioOutputClient> clients, Map<Integer, Integer> sessions) throws IOException {
            if (!ACTIVE_TRACKS_FINDER.matcher(currentLine).find()) {
                return AudioOutputHelper.UNHANDLED;
            }
            reader.readLine();
            return new Result(true, collectTracks(reader, clients, sessions));
        }

        private String collectTracks(BufferedReader reader, List<AudioOutputClient> clients, Map<Integer, Integer> sessions) throws NumberFormatException, IOException {
            String content;
            while (true) {
                String readLine = reader.readLine();
                content = readLine;
                if (readLine == null) {
                    break;
                }
                Matcher matcher = TRACK_CONTENT_FINDER.matcher(content);
                if (!matcher.find()) {
                    break;
                }
                int sessionId = Integer.valueOf(matcher.group(4)).intValue();
                Integer proc = (Integer) sessions.get(Integer.valueOf(sessionId));
                if (proc != null) {
                    clients.add(new AudioOutputClient(sessionId, proc.intValue(), Integer.valueOf(matcher.group(3)).intValue(), "yes".equals(matcher.group(2))));
                }
            }
            return content;
        }
    }

    static final class Result {
        public final boolean mHandled;
        public final String mSkipped;

        public Result(boolean handled, String skipped) {
            this.mHandled = handled;
            this.mSkipped = skipped;
        }
    }

    private static String collectSessions(BufferedReader reader, Map<Integer, Integer> sessions) throws NumberFormatException, IOException {
        String content;
        while (true) {
            String readLine = reader.readLine();
            content = readLine;
            if (readLine == null) {
                break;
            }
            Matcher matcher = DUMP_TAG.SESSIONS_CONTENT_FINDER.matcher(content);
            if (!matcher.find()) {
                break;
            }
            sessions.put(Integer.valueOf(Integer.valueOf(matcher.group(1)).intValue()), Integer.valueOf(Integer.valueOf(matcher.group(2)).intValue()));
        }
        return content;
    }

    public static List<AudioOutputClient> parseAudioFlingerDump(Context context) {
        File dir = null;
        try {
            dir = context.getFilesDir();
        } catch (Exception e) {
        }
        if (dir == null) {
            dir = new File("/cache");
        }
        int pid = Process.myPid();
        long tid = Thread.currentThread().getId();
        Object[] objArr = new Object[3];
        objArr[0] = Integer.valueOf(pid);
        objArr[1] = Long.valueOf(tid);
        int i = 0 + 1;
        objArr[2] = Integer.valueOf(0);
        String str = DEFAULT_TEMP_FILE;
        File dumpFile = new File(dir, String.format(str, objArr));
        while (dumpFile.exists()) {
            Object[] objArr2 = new Object[3];
            objArr2[0] = Integer.valueOf(pid);
            objArr2[1] = Long.valueOf(tid);
            int i2 = i + 1;
            objArr2[2] = Integer.valueOf(i);
            dumpFile = new File(dir, String.format(str, objArr2));
            i = i2;
        }
        List<AudioOutputClient> result = parseAudioFlingerDumpInternal(dumpFile);
        dumpFile.delete();
        return result;
    }

    private static List<AudioOutputClient> parseAudioFlingerDumpInternal(File tempFile) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(tempFile);
            ServiceManager.getService("media.audio_flinger").dump(os.getFD(), null);
            try {
                os.close();
            } catch (IOException e) {
            }
        } catch (FileNotFoundException e2) {
            Log.e(TAG, e2.toString());
            if (os != null) {
                os.close();
            }
        } catch (RemoteException e3) {
            Log.e(TAG, e3.toString());
            if (os != null) {
                os.close();
            }
        } catch (IOException e4) {
            Log.e(TAG, e4.toString());
            if (os != null) {
                os.close();
            }
        } catch (Exception e5) {
            Log.e(TAG, e5.toString());
            if (os != null) {
                os.close();
            }
        } catch (Throwable th) {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e6) {
                }
            }
            throw th;
        }
        InputStream in = null;
        try {
            in = new FileInputStream(tempFile);
        } catch (FileNotFoundException e7) {
        }
        if (in == null) {
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            List<AudioOutputClient> clients = new ArrayList();
            Map<Integer, Integer> sessions = null;
            boolean standby = false;
            String skipped = null;
            while (true) {
                String content;
                if (skipped != null) {
                    content = skipped;
                    skipped = null;
                } else {
                    content = reader.readLine();
                    if (content == null) {
                        try {
                            break;
                        } catch (IOException e8) {
                        }
                    }
                }
                if (DUMP_TAG.SESSIONS_HEAD_FINDER.matcher(content).matches()) {
                    if (sessions == null) {
                        sessions = new HashMap();
                    }
                    skipped = collectSessions(reader, sessions);
                } else {
                    Matcher standbyMatcher = DUMP_TAG.STANDBY_FINDER.matcher(content);
                    if (standbyMatcher.find()) {
                        standby = isStandBy(standbyMatcher.group(1).trim());
                    } else if (!standby) {
                        TrackCollector collector = COLLECTOR;
                        if (collector != null) {
                            skipped = collector.collectTracks(reader, content, clients, sessions).mSkipped;
                        } else {
                            Result r = COLLECTOR_LP.collectTracks(reader, content, clients, sessions);
                            if (r.mHandled) {
                                skipped = r.mSkipped;
                                COLLECTOR = COLLECTOR_LP;
                                Log.i(TAG, "collector=lp");
                            } else {
                                r = COLLECTOR_COMPAT.collectTracks(reader, content, clients, sessions);
                                if (r.mHandled) {
                                    skipped = r.mSkipped;
                                    COLLECTOR = COLLECTOR_COMPAT;
                                    Log.i(TAG, "collector=compat");
                                }
                            }
                        }
                    }
                }
            }
            reader.close();
            try {
                in.close();
            } catch (IOException e9) {
            }
            return clients;
        } catch (Exception e10) {
            Log.e(TAG, e10.toString());
            try {
                reader.close();
            } catch (IOException e11) {
            }
            try {
                in.close();
            } catch (IOException e12) {
            }
            return null;
        } catch (Throwable th2) {
            try {
                reader.close();
            } catch (IOException e13) {
            }
            try {
                in.close();
            } catch (IOException e14) {
            }
            throw th2;
        }
    }

    private static boolean isStandBy(String str) {
        boolean z = false;
        try {
            if (Integer.valueOf(str).intValue() != 0) {
                z = true;
            }
            return z;
        } catch (NumberFormatException e) {
            if (Boolean.valueOf(str).booleanValue() || "yes".equals(str)) {
                z = true;
            }
            return z;
        }
    }

    public static List<RunningAppProcessInfo> getActiveClientProcessList(List<RunningAppProcessInfo> procs, Context context, boolean addMainProc) {
        if (procs == null) {
            return null;
        }
        List<AudioOutputClient> clients = parseAudioFlingerDump(context);
        if (clients == null) {
            return null;
        }
        ArrayList<RunningAppProcessInfo> actives = new ArrayList();
        for (AudioOutputClient c : clients) {
            if (c.mActive) {
                int pid = c.mProcessId;
                for (RunningAppProcessInfo proc : procs) {
                    if (proc.pid == pid) {
                        actives.add(proc);
                    }
                }
            }
        }
        if (addMainProc) {
            actives.addAll(getMainProcessNames(actives, procs));
        }
        return actives;
    }

    public static List<String> getActiveReceiverNameList(Context context) {
        try {
            ParceledListSlice<ResolveInfo> receivers = AppGlobals.getPackageManager().queryIntentReceivers(new Intent(Intent.ACTION_MEDIA_BUTTON), null, 0, 0);
            if (receivers != null) {
                if (receivers.getList().size() != 0) {
                    List<RunningAppProcessInfo> processes = getActiveClientProcessList(ActivityManagerNative.getDefault().getRunningAppProcesses(), context, true);
                    if (processes != null) {
                        if (!processes.isEmpty()) {
                            List<String> result = new ArrayList();
                            for (RunningAppProcessInfo p : processes) {
                                for (ResolveInfo info : receivers.getList()) {
                                    if (info.activityInfo != null && p.processName.equals(info.activityInfo.processName)) {
                                        result.add(p.processName);
                                        break;
                                    }
                                }
                            }
                            return result;
                        }
                    }
                    return null;
                }
            }
            return null;
        } catch (RemoteException e) {
            return null;
        }
    }

    public static boolean hasActiveReceivers(Context context) {
        List<String> processes = getActiveReceiverNameList(context);
        return (processes == null || processes.isEmpty()) ? false : true;
    }

    private static List<RunningAppProcessInfo> getMainProcessNames(List<RunningAppProcessInfo> actives, List<RunningAppProcessInfo> procs) {
        ArrayList<RunningAppProcessInfo> mainProcs = new ArrayList();
        for (RunningAppProcessInfo active : actives) {
            int colonIndex = active.processName.indexOf(":");
            if (colonIndex > 0) {
                String mainName = active.processName.substring(0, colonIndex);
                for (RunningAppProcessInfo info : procs) {
                    if (mainName.equals(info.processName)) {
                        mainProcs.add(info);
                    }
                }
            }
        }
        return mainProcs;
    }
}
