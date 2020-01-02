package android.media;

import android.media.SubtitleTrack.Cue;
import android.os.Handler;
import android.os.Parcel;
import android.util.Log;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/* compiled from: SRTRenderer */
class SRTTrack extends WebVttTrack {
    private static final int KEY_LOCAL_SETTING = 102;
    private static final int KEY_START_TIME = 7;
    private static final int KEY_STRUCT_TEXT = 16;
    private static final int MEDIA_TIMED_TEXT = 99;
    private static final String TAG = "SRTTrack";
    private final Handler mEventHandler;

    SRTTrack(WebVttRenderingWidget renderingWidget, MediaFormat format) {
        super(renderingWidget, format);
        this.mEventHandler = null;
    }

    SRTTrack(Handler eventHandler, MediaFormat format) {
        super(null, format);
        this.mEventHandler = eventHandler;
    }

    /* Access modifiers changed, original: protected */
    public void onData(SubtitleData data) {
        try {
            TextTrackCue cue = new TextTrackCue();
            cue.mStartTimeMs = data.getStartTimeUs() / 1000;
            cue.mEndTimeMs = (data.getStartTimeUs() + data.getDurationUs()) / 1000;
            String[] lines = new String(data.getData(), "UTF-8").split("\\r?\\n");
            cue.mLines = new TextTrackCueSpan[lines.length][];
            int length = lines.length;
            int i = 0;
            int i2 = 0;
            while (i2 < length) {
                int i3 = i + 1;
                cue.mLines[i] = new TextTrackCueSpan[]{new TextTrackCueSpan(lines[i2], -1)};
                i2++;
                i = i3;
            }
            addCue(cue);
        } catch (UnsupportedEncodingException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("subtitle data is not UTF-8 encoded: ");
            stringBuilder.append(e);
            Log.w(TAG, stringBuilder.toString());
        }
    }

    public void onData(byte[] data, boolean eos, long runID) {
        UnsupportedEncodingException e;
        StringBuilder stringBuilder;
        IOException ioe;
        String str = TAG;
        long j;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data), "UTF-8"));
            while (true) {
                String readLine = br.readLine();
                String header = readLine;
                BufferedReader br2;
                if (readLine != null) {
                    readLine = br.readLine();
                    if (readLine == null) {
                        j = runID;
                        return;
                    }
                    header = new TextTrackCue();
                    String[] startEnd = readLine.split("-->");
                    header.mStartTimeMs = parseMs(startEnd[0]);
                    int i = 1;
                    header.mEndTimeMs = parseMs(startEnd[1]);
                    try {
                        int i2;
                        header.mRunID = runID;
                        List<String> paragraph = new ArrayList();
                        while (true) {
                            String readLine2 = br.readLine();
                            String s = readLine2;
                            if (readLine2 == null || s.trim().equals("")) {
                                i2 = 0;
                                header.mLines = new TextTrackCueSpan[paragraph.size()][];
                                header.mStrings = (String[]) paragraph.toArray(new String[0]);
                            } else {
                                paragraph.add(s);
                            }
                        }
                        i2 = 0;
                        header.mLines = new TextTrackCueSpan[paragraph.size()][];
                        header.mStrings = (String[]) paragraph.toArray(new String[0]);
                        for (String line : paragraph) {
                            TextTrackCueSpan[] textTrackCueSpanArr = new TextTrackCueSpan[i];
                            br2 = br;
                            textTrackCueSpanArr[0] = new TextTrackCueSpan(line, -1);
                            TextTrackCueSpan[] span = textTrackCueSpanArr;
                            header.mStrings[i2] = line;
                            i = i2 + 1;
                            header.mLines[i2] = span;
                            span = data;
                            int i3 = 0;
                            i2 = i;
                            br = br2;
                            i = 1;
                        }
                        br2 = br;
                    } catch (UnsupportedEncodingException e2) {
                        e = e2;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("subtitle data is not UTF-8 encoded: ");
                        stringBuilder.append(e);
                        Log.w(str, stringBuilder.toString());
                    } catch (IOException e3) {
                        ioe = e3;
                        Log.e(str, ioe.getMessage(), ioe);
                    }
                    try {
                        addCue(header);
                        byte[] bArr = data;
                        br = br2;
                    } catch (UnsupportedEncodingException e4) {
                        e = e4;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("subtitle data is not UTF-8 encoded: ");
                        stringBuilder.append(e);
                        Log.w(str, stringBuilder.toString());
                    } catch (IOException e5) {
                        ioe = e5;
                        Log.e(str, ioe.getMessage(), ioe);
                    }
                }
                j = runID;
                br2 = br;
                return;
            }
        } catch (UnsupportedEncodingException e6) {
            e = e6;
            j = runID;
            stringBuilder = new StringBuilder();
            stringBuilder.append("subtitle data is not UTF-8 encoded: ");
            stringBuilder.append(e);
            Log.w(str, stringBuilder.toString());
        } catch (IOException e7) {
            ioe = e7;
            j = runID;
            Log.e(str, ioe.getMessage(), ioe);
        }
    }

    public void updateView(Vector<Cue> activeCues) {
        if (getRenderingWidget() != null) {
            super.updateView(activeCues);
        } else if (this.mEventHandler != null) {
            Iterator it = activeCues.iterator();
            while (it.hasNext()) {
                Cue cue = (Cue) it.next();
                TextTrackCue ttc = (TextTrackCue) cue;
                Parcel parcel = Parcel.obtain();
                parcel.writeInt(102);
                parcel.writeInt(7);
                parcel.writeInt((int) cue.mStartTimeMs);
                parcel.writeInt(16);
                StringBuilder sb = new StringBuilder();
                for (String line : ttc.mStrings) {
                    sb.append(line);
                    sb.append(10);
                }
                byte[] buf = sb.toString().getBytes();
                parcel.writeInt(buf.length);
                parcel.writeByteArray(buf);
                this.mEventHandler.sendMessage(this.mEventHandler.obtainMessage(99, 0, 0, parcel));
            }
            activeCues.clear();
        }
    }

    private static long parseMs(String in) {
        String str = in;
        String str2 = ":";
        String str3 = ",";
        return (((((Long.parseLong(str.split(str2)[0].trim()) * 60) * 60) * 1000) + ((60 * Long.parseLong(str.split(str2)[1].trim())) * 1000)) + (1000 * Long.parseLong(str.split(str2)[2].split(str3)[0].trim()))) + Long.parseLong(str.split(str2)[2].split(str3)[1].trim());
    }
}
