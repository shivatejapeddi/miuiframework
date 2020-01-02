package android.media;

import android.app.ActivityThread;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.MiuiSettings.SilenceMode;
import android.provider.MiuiSettings.System;
import android.provider.Settings;
import android.speech.tts.TextToSpeech.Engine;
import android.telecom.Logging.Session;
import android.telecom.TelecomManager;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import miui.os.Build;
import miui.process.ProcessManager;
import miui.util.AudioManagerHelper;

public class AudioServiceInjector {
    public static final String ACTION_VOLUME_BOOST = "miui.intent.action.VOLUME_BOOST";
    private static final String ALL_RANDOM = "AllFilesRandom";
    public static final int DEFAULT_VOL_STREAM_NO_PLAYBACK = 3;
    public static final String EXTRA_BOOST_STATE = "volume_boost_state";
    private static final String FOUR_TIME_ZONES = "DayZones";
    private static final String IS_LITHIUM = "lithium";
    private static final String IS_SCORPIO = "scorpio";
    private static final int MIUI_NATURE_SOUND_COLUMN_MAX = 4;
    private static final int[][] MIUI_NATURE_SOUND_NUMBER = new int[][]{new int[]{3, 3, 3, 3}, new int[]{8, 2, 5, 2}, new int[]{12, 0, 0, 0}};
    private static final String[] MIUI_NATURE_SOUND_PLAY_STRATEGY;
    private static final String[] MIUI_NATURE_SOUND_PREFIX = new String[]{"file:///system/media/audio/ui/WaterDropNotification", "file:///system/media/audio/ui/BirdCicadaNotification", "file:///system/media/audio/ui/WindRainNotification"};
    private static final int MIUI_NATURE_SOUND_ROW_MAX = 3;
    private static final String[] MIUI_NATURE_SOUND_SEQ_TYPE;
    private static final String[] MIUI_NATURE_SOUND_TIME_ZONE = new String[]{"Day", "Midday", "Evening", "Night"};
    private static final String[] MIUI_NATURE_SOUND_URI = new String[]{"file:///system/media/audio/ui/WaterDrop_preview.ogg", "file:///system/media/audio/ui/BirdCicada_preview.ogg", "file:///system/media/audio/ui/WindRain_preview.ogg"};
    private static final int MUTE_TIME_INTERVAL_INDEX = 3;
    private static final String ONE_TIME_ZONE = "AllDay";
    private static final int RANDOM_PLAYBACK_BOUND = 1;
    private static final String RANDOM_SOUND_DEFALTE_URI = "file:///system/media/audio/ui/notice_audition.ogg";
    private static final int RANDOM_SOUND_NUM_INDEX = 0;
    private static String RANDOM_SOUND_PREFIX = "file:///system/media/audio/ui/notification_beep_sound_";
    private static String SEQUENCE_SOUND_PREFIX = "file:///system/media/audio/ui/notification_beep_soundseq_";
    private static final int SEQ_SOUND_NUM_INDEX = 1;
    private static final int SEQ_TIME_INTERVAL_INDEX = 2;
    private static final String SOUND_SUFFIX = ".ogg";
    private static final String TAG = "AudioService";
    private static final String TIME_ZONES_RANDOM = "TimeZonesRandom";
    private static final int TIME_ZONE_BOUND_CLOCK_EIGHT = 8;
    private static final int TIME_ZONE_BOUND_CLOCK_FOURTEEN = 14;
    private static final int TIME_ZONE_BOUND_CLOCK_NINETEEN = 19;
    private static final int TIME_ZONE_BOUND_CLOCK_TWELVE = 12;
    private static final int TIME_ZONE_BOUND_CLOCK_ZERO = 0;
    private static final String TWO_TIME_ZONES = "DayNight";
    private static final int availableDevice = 140;
    private static final String[] mContentResolverParameters = new String[]{System.RANDOM_NOTE_MODE_RANDOM_SOUND_NUMBER, System.RANDOM_NOTE_MODE_SEQUENCE_SOUND_NUMBER, System.RANDOM_NOTE_MODE_SEQUENCE_TIME_INTERVAL_MS, System.RANDOM_NOTE_MODE_MUTE_TIME_INTERVAL_MS};
    private static final String[] mContentResolverTypes = new String[]{Settings.System.NOTIFICATION_SOUND, System.SMS_RECEIVED_SOUND, System.SMS_RECEIVED_SOUND_SLOT_1, System.SMS_RECEIVED_SOUND_SLOT_2, System.CALENDAR_ALERT};
    private static boolean mFirstUpdateMode = true;
    private static boolean mHasPlayedNormalNotification = false;
    private static boolean mInternationalLocation = true;
    private static long mLastNotificationTimeMs = 0;
    private static final Object mLockRandomStatusUpdate = new Object();
    private static String[] mNatureSoundString;
    private static boolean mNotificationRandomSound = true;
    public static int mOriginalIndexWhenSetStreamVolume;
    private static int mPreviousNatureSoundIndex = 0;
    private static boolean[] mRandomSound = new boolean[]{true, true, true, true, true};
    private static boolean mRandomSoundAtLeastOne = false;
    private static int mSeqIndex = 0;
    private static boolean mSunriseAndSunsetUpdate = true;
    private static int mSunriseTimeHours = 0;
    private static int mSunriseTimeMins = 0;
    private static int mSunsetTimeHours = 0;
    private static int mSunsetTimeMins = 0;
    private static int[] mTimeAndSoundNumParameters = new int[]{10, 1, 1000, 10000};
    private static String mTimeZone;
    private static String mTimeZoneforUpdate;
    private static HashMap mTypes = new HashMap() {
        {
            put("content://settings/system/notification_sound", Integer.valueOf(0));
            put("content://settings/system/sms_received_sound", Integer.valueOf(1));
            put("content://settings/system/sms_received_sound_slot_1", Integer.valueOf(2));
            put("content://settings/system/sms_received_sound_slot_2", Integer.valueOf(3));
            put("content://settings/system/calendar_alert", Integer.valueOf(4));
        }
    };
    private static HashMap<String, Integer> sAppList = new HashMap();

    static {
        r2 = new String[5];
        String[] strArr = MIUI_NATURE_SOUND_URI;
        r2[0] = strArr[0];
        r2[1] = strArr[0];
        r2[2] = strArr[0];
        r2[3] = strArr[0];
        r2[4] = strArr[0];
        mNatureSoundString = r2;
        String str = ONE_TIME_ZONE;
        MIUI_NATURE_SOUND_SEQ_TYPE = new String[]{str, TWO_TIME_ZONES, str, FOUR_TIME_ZONES};
        str = TIME_ZONES_RANDOM;
        MIUI_NATURE_SOUND_PLAY_STRATEGY = new String[]{str, str, ALL_RANDOM, str};
        r2 = MIUI_NATURE_SOUND_TIME_ZONE;
        mTimeZone = r2[0];
        mTimeZoneforUpdate = r2[0];
        sAppList.put("com.changba", Integer.valueOf(1));
        sAppList.put("com.tencent.karaoke", Integer.valueOf(2));
    }

    private static boolean isScorpio() {
        return Build.DEVICE.equals(IS_SCORPIO);
    }

    private static boolean isLithium() {
        return Build.DEVICE.equals(IS_LITHIUM);
    }

    public static boolean shouldAdjustHiFiVolume(int streamType, int direction, int streamIndex, int maxIndex, Context context) {
        boolean z = false;
        if ((!isScorpio() && !isLithium()) || streamType != 3 || !AudioManagerHelper.isHiFiMode(context)) {
            return false;
        }
        int maxStreamIndex = maxIndex;
        boolean adjustDownHiFiVolume = direction == -1 && AudioManagerHelper.getHiFiVolume(context) > 0;
        boolean adjustUpHiFiVolume = direction == 1 && streamIndex == maxStreamIndex;
        if (adjustDownHiFiVolume || adjustUpHiFiVolume) {
            z = true;
        }
        return z;
    }

    public static void adjustHiFiVolume(int direction, Context context) {
        int currentHiFiVolume = AudioManagerHelper.getHiFiVolume(context);
        if (direction == -1) {
            AudioManagerHelper.setHiFiVolume(context, currentHiFiVolume - 10);
        } else if (direction == 1 && currentHiFiVolume < 100) {
            AudioManagerHelper.setHiFiVolume(context, currentHiFiVolume + 10);
        }
    }

    public static void setStreamVolumeIntAlt(Object object, int streamType, int index, int device, int maxIndex, int[] streamVolumeAlias, Context context) {
        handleZenModeVolumeChanged(context, streamVolumeAlias[streamType], device, index);
        onSetStreamVolumeIntAlt(object, streamType, index, device, maxIndex, streamVolumeAlias, context);
    }

    private static void onSetStreamVolumeIntAlt(Object object, int streamType, int index, int device, int maxIndex, int[] streamVolumeAlias, Context context) {
        UnsupportedOperationException e;
        Context context2;
        NoSuchMethodException e2;
        IllegalAccessException e3;
        InvocationTargetException e4;
        Object obj = object;
        int i = streamType;
        boolean isSupportHifiVolume = false;
        int i2;
        int i3;
        try {
            if (isScorpio() || isLithium()) {
                isSupportHifiVolume = true;
            }
            Class<?> clazz = object.getClass();
            Method method = null;
            String str = "setStreamVolumeInt";
            if (VERSION.SDK_INT >= 23) {
                if (clazz != null) {
                    method = clazz.getDeclaredMethod(str, new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE, Boolean.TYPE, String.class});
                }
            } else if (clazz != null) {
                method = clazz.getDeclaredMethod(str, new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE, Boolean.TYPE});
            }
            if (3 == i && isSupportHifiVolume) {
                if (!AudioManagerHelper.isHiFiMode(context)) {
                    i2 = index;
                    i3 = maxIndex;
                } else if (index >= maxIndex) {
                    int rawStreamMaxIndex = maxIndex;
                    if (method != null) {
                        try {
                            method.setAccessible(true);
                            if (VERSION.SDK_INT >= 23) {
                                method.invoke(obj, new Object[]{Integer.valueOf(streamVolumeAlias[i]), Integer.valueOf(rawStreamMaxIndex), Integer.valueOf(device), Boolean.valueOf(false), TAG});
                            } else {
                                method.invoke(obj, new Object[]{Integer.valueOf(streamVolumeAlias[i]), Integer.valueOf(rawStreamMaxIndex), Integer.valueOf(device), Boolean.valueOf(false)});
                            }
                        } catch (UnsupportedOperationException e5) {
                            e = e5;
                            context2 = context;
                            e.printStackTrace();
                        } catch (NoSuchMethodException e6) {
                            e2 = e6;
                            context2 = context;
                            e2.printStackTrace();
                        } catch (IllegalAccessException e7) {
                            e3 = e7;
                            context2 = context;
                            e3.printStackTrace();
                        } catch (InvocationTargetException e8) {
                            e4 = e8;
                            context2 = context;
                            e4.printStackTrace();
                        }
                    }
                    try {
                        AudioManagerHelper.setHiFiVolume(context, (mOriginalIndexWhenSetStreamVolume - ((rawStreamMaxIndex + 5) / 10)) * 10);
                        return;
                    } catch (UnsupportedOperationException e9) {
                        e = e9;
                        e.printStackTrace();
                    } catch (NoSuchMethodException e10) {
                        e2 = e10;
                        e2.printStackTrace();
                    } catch (IllegalAccessException e11) {
                        e3 = e11;
                        e3.printStackTrace();
                    } catch (InvocationTargetException e12) {
                        e4 = e12;
                        e4.printStackTrace();
                    }
                }
                context2 = context;
            } else {
                i2 = index;
                i3 = maxIndex;
                context2 = context;
            }
            if (method != null) {
                method.setAccessible(true);
                if (VERSION.SDK_INT >= 23) {
                    String tag = new StringBuilder();
                    tag.append("Pid:");
                    tag.append(Binder.getCallingPid());
                    tag.append(" Uid:");
                    tag.append(Binder.getCallingUid());
                    tag = tag.toString();
                    method.invoke(obj, new Object[]{Integer.valueOf(streamVolumeAlias[i]), Integer.valueOf(index), Integer.valueOf(device), Boolean.valueOf(false), tag});
                    return;
                }
                method.invoke(obj, new Object[]{Integer.valueOf(streamVolumeAlias[i]), Integer.valueOf(index), Integer.valueOf(device), Boolean.valueOf(false)});
            }
        } catch (UnsupportedOperationException e13) {
            e = e13;
            i2 = index;
            i3 = maxIndex;
            context2 = context;
            e.printStackTrace();
        } catch (NoSuchMethodException e14) {
            e2 = e14;
            i2 = index;
            i3 = maxIndex;
            context2 = context;
            e2.printStackTrace();
        } catch (IllegalAccessException e15) {
            e3 = e15;
            i2 = index;
            i3 = maxIndex;
            context2 = context;
            e3.printStackTrace();
        } catch (InvocationTargetException e16) {
            e4 = e16;
            i2 = index;
            i3 = maxIndex;
            context2 = context;
            e4.printStackTrace();
        }
    }

    public static int calculateStreamVolume(int streamType, int index, Context context) {
        int retValue = (index + 5) / 10;
        if ((isScorpio() || isLithium()) && streamType == 3 && AudioManagerHelper.isHiFiMode(context)) {
            return retValue + (AudioManagerHelper.getHiFiVolume(context) / 10);
        }
        return retValue;
    }

    public static int calculateStreamMaxVolume(int streamType, int maxIndex, Context context) {
        int retValue = (maxIndex + 5) / 10;
        if ((isScorpio() || isLithium()) && 3 == streamType && AudioManagerHelper.isHiFiMode(context)) {
            return retValue + 10;
        }
        return retValue;
    }

    public static boolean isOnlyAdjustVolume(int flags) {
        return (1048576 & flags) != 0;
    }

    public static boolean isXOptMode() {
        return SystemProperties.getBoolean("persist.sys.miui_optimization", "1".equals(SystemProperties.get("ro.miui.cts")) ^ 1) ^ 1;
    }

    public static boolean isOnlyAdjustVolume(int flags, int stream, int mode) {
        return (1048576 & flags) != 0 || (stream == 2 && !isXOptMode());
    }

    public static void adjustMaxStreamVolume(int[] maxStreamVolume) {
        int i = 0;
        while (i < maxStreamVolume.length) {
            if (!(i == 0 || i == 6 || i == 7)) {
                maxStreamVolume[i] = 15;
            }
            i++;
        }
    }

    public static void adjustMinStreamVolume(int[] minStreamVolume) {
        for (int i = 0; i < minStreamVolume.length; i++) {
            if (i == 6) {
                minStreamVolume[i] = 1;
            }
        }
    }

    public static void adjustDefaultStreamVolume(int[] defaultStreamVolume) {
        int i = 0;
        while (i < defaultStreamVolume.length) {
            if (!(i == 0 || i == 6)) {
                defaultStreamVolume[i] = 10;
            }
            i++;
        }
    }

    public static int checkForRingerModeChange(Context context, int oldRingerMode, int newRingerMode, int direction) {
        return AudioManagerHelper.getValidatedRingerMode(context, newRingerMode);
    }

    public static boolean isPackageProtectedWhenUserBackground(int userId, PackageInfo pkg) {
        return ProcessManager.isLockedApplication(pkg.packageName, userId);
    }

    public static void updateRestriction(Context context) {
    }

    public static int getRingerModeAffectedStreams(int streams, Context context) {
        if (!SilenceMode.isSupported) {
            return streams;
        }
        int voiceAssistStream = getVoiceAssistNum();
        int i = 0;
        if (SilenceMode.getZenMode(context) == 4) {
            streams = ((streams | 38) & -9) | (Settings.System.getIntForUser(context.getContentResolver(), SilenceMode.MUTE_MUSIC_AT_SILENT, 0, -3) == 1 ? 8 : 0);
            if (voiceAssistStream > 0) {
                streams = (streams & (~(1 << voiceAssistStream))) | (Settings.System.getIntForUser(context.getContentResolver(), SilenceMode.MUTE_VOICEASSIST_AT_SILENT, 1, -3) == 1 ? 1 << voiceAssistStream : 0);
            }
        }
        if (SilenceMode.getZenMode(context) == 1) {
            if (VERSION.SDK_INT < 28) {
                i = 2;
            }
            streams = 256 | i;
            if (voiceAssistStream > 0) {
                streams |= 1 << voiceAssistStream;
            }
        }
        return streams;
    }

    public static void checkMusicStream(Object[] object, Context context, int ringerMode, int dev) {
    }

    public static void handleZenModeVolumeChanged(Context context, int streamType, int device, int index) {
        if (SilenceMode.isSupported && streamType == 3 && SilenceMode.getZenMode(context) == 1 && !isXOptMode()) {
            String nameForMute = SilenceMode.VOLUME_MUSIC_BEFORE_MUTE;
            String suffix = AudioSystem.getOutputDeviceName(device);
            if (!suffix.isEmpty()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(nameForMute);
                stringBuilder.append(Session.SESSION_SEPARATION_CHAR_CHILD);
                stringBuilder.append(suffix);
                nameForMute = stringBuilder.toString();
                long identity = Binder.clearCallingIdentity();
                Settings.System.putIntForUser(context.getContentResolver(), nameForMute, (index + 5) / 10, -2);
                Binder.restoreCallingIdentity(identity);
            }
        }
    }

    public static void handleZenModeChangedForMusic(Object object, Context context, int preZenMode, int zenmode, int maxIndexSrc, int maxIndexDsts, int[] streamVolumeAlias) {
        if (SilenceMode.isSupported && zenmode != preZenMode && !isXOptMode()) {
            if (zenmode == 1) {
                saveAllDevicesMusicVolume(object, context, maxIndexSrc, maxIndexDsts, streamVolumeAlias);
            } else if (preZenMode == 1) {
                restoreAllDevicesMusicVolume(object, context, maxIndexSrc, maxIndexDsts, streamVolumeAlias);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("on change zenmode from ");
            stringBuilder.append(preZenMode);
            stringBuilder.append(" to ");
            stringBuilder.append(zenmode);
            Log.i(TAG, stringBuilder.toString());
        }
    }

    private static void saveAllDevicesMusicVolume(Object object, Context context, int maxIndexSrc, int maxIndexDst, int[] streamVolumeAlias) {
        ContentResolver contentResolver = context.getContentResolver();
        int remainingDevices = 268435455;
        int i = 0;
        while (remainingDevices != 0) {
            String name = Settings.System.VOLUME_MUSIC;
            String nameForMute = SilenceMode.VOLUME_MUSIC_BEFORE_MUTE;
            int device = 1 << i;
            if ((device & remainingDevices) != 0) {
                remainingDevices &= ~device;
                String suffix = AudioSystem.getOutputDeviceName(device);
                if (!suffix.isEmpty()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(name);
                    String str = Session.SESSION_SEPARATION_CHAR_CHILD;
                    stringBuilder.append(str);
                    stringBuilder.append(suffix);
                    name = stringBuilder.toString();
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(nameForMute);
                    stringBuilder.append(str);
                    stringBuilder.append(suffix);
                    nameForMute = stringBuilder.toString();
                }
                int volume = Settings.System.getIntForUser(contentResolver, name, -1, -2);
                onSetStreamVolumeIntAlt(object, 3, 0, device, maxIndexDst, streamVolumeAlias, context);
                if (volume != -1) {
                    Settings.System.putIntForUser(contentResolver, nameForMute, volume, -2);
                }
            }
            i++;
        }
    }

    private static void restoreAllDevicesMusicVolume(Object object, Context context, int maxIndexSrc, int maxIndexDst, int[] streamVolumeAlias) {
        ContentResolver contentResolver = context.getContentResolver();
        int remainingDevices = 268435455;
        int i = 0;
        while (remainingDevices != 0) {
            String name = Settings.System.VOLUME_MUSIC;
            String nameForMute = SilenceMode.VOLUME_MUSIC_BEFORE_MUTE;
            int device = 1 << i;
            if ((device & remainingDevices) != 0) {
                remainingDevices &= ~device;
                String suffix = AudioSystem.getOutputDeviceName(device);
                if (!suffix.isEmpty()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(name);
                    String str = Session.SESSION_SEPARATION_CHAR_CHILD;
                    stringBuilder.append(str);
                    stringBuilder.append(suffix);
                    name = stringBuilder.toString();
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(nameForMute);
                    stringBuilder.append(str);
                    stringBuilder.append(suffix);
                    nameForMute = stringBuilder.toString();
                }
                int volume = Settings.System.getIntForUser(contentResolver, nameForMute, 10, -2);
                if (volume != -1) {
                    Settings.System.putIntForUser(contentResolver, name, volume, -2);
                }
            }
            i++;
        }
        updateMusicStreamVolume(object);
    }

    private static void updateMusicStreamVolume(Object object) {
        try {
            Method method = object.getClass().getDeclaredMethod("reloadMusicVolume", new Class[0]);
            if (method != null) {
                method.setAccessible(true);
                method.invoke(object, new Object[0]);
            }
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        } catch (InvocationTargetException e4) {
            e4.printStackTrace();
        }
    }

    public static void setDefaultTimeZoneStatus(boolean status) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("setDefaultTimeZoneStatus: mInternationalLocation=");
        stringBuilder.append(status);
        Log.i(TAG, stringBuilder.toString());
        mInternationalLocation = status;
    }

    public static void setSunriseAndSunsetTime(int SunriseHours, int SunriseMins, int SunsetHours, int SunsetMins) {
        mSunriseTimeHours = SunriseHours;
        mSunsetTimeHours = SunsetHours;
        mSunriseTimeMins = SunriseMins;
        mSunsetTimeMins = SunsetMins;
    }

    public static void checkSunriseAndSunsetTimeUpdate(Context context) {
        if (mRandomSoundAtLeastOne && !mInternationalLocation) {
            mTimeZoneforUpdate = queryCurrentTimeZone();
            if (!mTimeZoneforUpdate.equals(MIUI_NATURE_SOUND_TIME_ZONE[2])) {
                mSunriseAndSunsetUpdate = true;
            } else if (mSunriseAndSunsetUpdate) {
                updateSunriseAndSunsetTime(context);
                mSunriseAndSunsetUpdate = false;
            }
        }
    }

    public static void updateSunriseAndSunsetTime(Context context) {
        Log.i(TAG, "updateSunriseAndSunsetTime sendBroadcast!");
        context.sendBroadcast(new Intent("com.android.media.update.sunrise.sunset.time"));
    }

    private static String queryCurrentTimeZoneForDomestic(int CurrentTimeHours, int CurrentTimeMins) {
        int i;
        if (CurrentTimeHours >= 0) {
            i = mSunriseTimeHours;
            if (CurrentTimeHours < i || (CurrentTimeHours == i && CurrentTimeMins < mSunriseTimeMins)) {
                return MIUI_NATURE_SOUND_TIME_ZONE[3];
            }
        }
        i = mSunriseTimeHours;
        if ((CurrentTimeHours > i || (CurrentTimeHours == i && CurrentTimeMins >= mSunriseTimeMins)) && CurrentTimeHours < 12) {
            return MIUI_NATURE_SOUND_TIME_ZONE[0];
        }
        if (CurrentTimeHours >= 12 && CurrentTimeHours < 14) {
            return MIUI_NATURE_SOUND_TIME_ZONE[1];
        }
        if (CurrentTimeHours >= 14) {
            i = mSunsetTimeHours;
            if (CurrentTimeHours < i || (CurrentTimeHours == i && CurrentTimeMins < mSunsetTimeMins)) {
                return MIUI_NATURE_SOUND_TIME_ZONE[0];
            }
        }
        return MIUI_NATURE_SOUND_TIME_ZONE[2];
    }

    private static String queryCurrentTimeZone() {
        Calendar CurrentTimeCalendar = Calendar.getInstance();
        int CurrentTimeHours = CurrentTimeCalendar.get(11);
        int CurrentTimeMins = CurrentTimeCalendar.get(12);
        if (!mInternationalLocation) {
            return queryCurrentTimeZoneForDomestic(CurrentTimeHours, CurrentTimeMins);
        }
        if (CurrentTimeHours >= 0 && CurrentTimeHours < 8) {
            return MIUI_NATURE_SOUND_TIME_ZONE[3];
        }
        if (CurrentTimeHours >= 8 && CurrentTimeHours < 12) {
            return MIUI_NATURE_SOUND_TIME_ZONE[0];
        }
        if (CurrentTimeHours >= 12 && CurrentTimeHours < 14) {
            return MIUI_NATURE_SOUND_TIME_ZONE[1];
        }
        if (CurrentTimeHours < 14 || CurrentTimeHours >= 19) {
            return MIUI_NATURE_SOUND_TIME_ZONE[2];
        }
        return MIUI_NATURE_SOUND_TIME_ZONE[0];
    }

    private static int querySerialsIndex(int typeIndex) {
        int SerialsIndex = 0;
        synchronized (mLockRandomStatusUpdate) {
            for (int index = 0; index < MIUI_NATURE_SOUND_URI.length; index++) {
                if (MIUI_NATURE_SOUND_URI[index].equals(mNatureSoundString[typeIndex])) {
                    SerialsIndex = index;
                    break;
                }
            }
        }
        return SerialsIndex;
    }

    private static int querySoundSourcesNumber(String type) {
        int result;
        int RowIndex = 0;
        int ColumnIndex = 0;
        int TypeIndex = getIndexForType(type);
        synchronized (mLockRandomStatusUpdate) {
            for (int index = 0; index < MIUI_NATURE_SOUND_URI.length; index++) {
                if (MIUI_NATURE_SOUND_URI[index].equals(mNatureSoundString[TypeIndex])) {
                    RowIndex = index;
                    break;
                }
            }
        }
        mTimeZone = queryCurrentTimeZone();
        int index2 = 0;
        while (true) {
            String[] strArr = MIUI_NATURE_SOUND_TIME_ZONE;
            if (index2 >= strArr.length) {
                break;
            } else if (strArr[index2].equals(mTimeZone)) {
                ColumnIndex = index2;
                break;
            } else {
                index2++;
            }
        }
        if (RowIndex >= 3 || ColumnIndex >= 4) {
            result = 1;
        } else if (MIUI_NATURE_SOUND_PLAY_STRATEGY[RowIndex].equals(TIME_ZONES_RANDOM)) {
            result = MIUI_NATURE_SOUND_NUMBER[RowIndex][ColumnIndex];
        } else {
            result = MIUI_NATURE_SOUND_NUMBER[RowIndex][0];
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("querySoundSourcesNumber:result=");
        stringBuilder.append(result);
        stringBuilder.append(" RowIndex=");
        stringBuilder.append(RowIndex);
        stringBuilder.append(" ColumnIndex=");
        stringBuilder.append(ColumnIndex);
        stringBuilder.append(" mTimeZone=");
        stringBuilder.append(mTimeZone);
        Log.i(TAG, stringBuilder.toString());
        return result;
    }

    private static String createActualUriForNatureSound(String type) {
        String TimeZone = MIUI_NATURE_SOUND_TIME_ZONE[0];
        StringBuilder stringBuilder = new StringBuilder();
        int SerialsIndex = querySerialsIndex(getIndexForType(type));
        stringBuilder.append(MIUI_NATURE_SOUND_PREFIX[SerialsIndex]);
        int SoundSourcesNumber = querySoundSourcesNumber(type);
        if (SoundSourcesNumber > 1) {
            int index;
            do {
                index = new Random().nextInt(SoundSourcesNumber) + 1;
            } while (mPreviousNatureSoundIndex == index);
            mPreviousNatureSoundIndex = index;
            if (MIUI_NATURE_SOUND_PLAY_STRATEGY[SerialsIndex].equals(TIME_ZONES_RANDOM)) {
                stringBuilder.append(mTimeZone);
            } else {
                stringBuilder.append(TimeZone);
            }
            stringBuilder.append(index);
        } else if (SoundSourcesNumber == 1) {
            stringBuilder.append(mTimeZone);
            stringBuilder.append(1);
        }
        stringBuilder.append(SOUND_SUFFIX);
        String FilePath = stringBuilder.toString();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("createActualUriForNatureSound: FilePath=");
        stringBuilder2.append(FilePath);
        Log.i(TAG, stringBuilder2.toString());
        return FilePath;
    }

    private static String createSeqUriForNatureSound(String type) {
        StringBuilder stringBuilder = new StringBuilder();
        int SerialsIndex = querySerialsIndex(getIndexForType(type));
        stringBuilder.append(MIUI_NATURE_SOUND_PREFIX[SerialsIndex]);
        stringBuilder.append("Seq");
        String TimeZone;
        int index;
        if (MIUI_NATURE_SOUND_SEQ_TYPE[SerialsIndex].equals(ONE_TIME_ZONE)) {
            stringBuilder.append(MIUI_NATURE_SOUND_SEQ_TYPE[SerialsIndex]);
            stringBuilder.append(1);
        } else if (MIUI_NATURE_SOUND_SEQ_TYPE[SerialsIndex].equals(TWO_TIME_ZONES)) {
            TimeZone = queryCurrentTimeZone();
            if (TimeZone.equals(MIUI_NATURE_SOUND_TIME_ZONE[0]) || TimeZone.equals(MIUI_NATURE_SOUND_TIME_ZONE[1])) {
                index = 1;
            } else {
                index = 2;
            }
            stringBuilder.append(MIUI_NATURE_SOUND_SEQ_TYPE[SerialsIndex]);
            stringBuilder.append(index);
        } else if (MIUI_NATURE_SOUND_SEQ_TYPE[SerialsIndex].equals(FOUR_TIME_ZONES)) {
            TimeZone = queryCurrentTimeZone();
            if (TimeZone.equals(MIUI_NATURE_SOUND_TIME_ZONE[0])) {
                index = 1;
            } else if (TimeZone.equals(MIUI_NATURE_SOUND_TIME_ZONE[1])) {
                index = 2;
            } else if (TimeZone.equals(MIUI_NATURE_SOUND_TIME_ZONE[2])) {
                index = 3;
            } else {
                index = 4;
            }
            stringBuilder.append(MIUI_NATURE_SOUND_SEQ_TYPE[SerialsIndex]);
            stringBuilder.append(index);
        }
        stringBuilder.append(SOUND_SUFFIX);
        String FilePath = stringBuilder.toString();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("createActualUriForNatureSound: FilePath=");
        stringBuilder2.append(FilePath);
        Log.i(TAG, stringBuilder2.toString());
        return FilePath;
    }

    private static boolean getMiuiNatureSoundStatus(String uri) {
        boolean miuiNatureSound = false;
        int index = 0;
        while (true) {
            String[] strArr = MIUI_NATURE_SOUND_URI;
            if (index >= strArr.length) {
                break;
            }
            miuiNatureSound = strArr[index].equals(uri);
            if (miuiNatureSound) {
                break;
            }
            index++;
        }
        return miuiNatureSound;
    }

    public static void updateNotificationMode(Context context) {
        int index;
        ContentResolver contentResolver = context.getContentResolver();
        synchronized (mLockRandomStatusUpdate) {
            for (index = 0; index < mContentResolverTypes.length; index++) {
                String uri = Settings.System.getString(contentResolver, mContentResolverTypes[index]);
                boolean randomSound = getMiuiNatureSoundStatus(uri);
                if (randomSound != mRandomSound[index]) {
                    mRandomSound[index] = randomSound;
                    mSeqIndex = 0;
                    if (!(!mRandomSound[index] || mInternationalLocation || mFirstUpdateMode)) {
                        updateSunriseAndSunsetTime(context);
                    }
                }
                if (mRandomSound[index]) {
                    mNatureSoundString[index] = uri;
                }
            }
            mRandomSoundAtLeastOne = false;
            for (boolean z : mRandomSound) {
                if (z) {
                    mRandomSoundAtLeastOne = true;
                    break;
                }
            }
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("updateNotificationMode: mRandomSoundAtLeastOne=");
            stringBuilder.append(mRandomSoundAtLeastOne);
            Log.i(str, stringBuilder.toString());
        }
        if (mFirstUpdateMode) {
            mFirstUpdateMode = false;
        }
        index = 0;
        while (index < mContentResolverParameters.length) {
            try {
                int parameter = Settings.System.getIntForUser(contentResolver, mContentResolverParameters[index], 0, UserHandle.myUserId());
                if (!(parameter == 0 || parameter == mTimeAndSoundNumParameters[index])) {
                    mTimeAndSoundNumParameters[index] = parameter;
                }
                index++;
            } catch (Exception e) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Cannot get random notification settings provider value: ");
                stringBuilder2.append(e);
                Log.e(TAG, stringBuilder2.toString());
            }
        }
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("updateNotificationMode():\n mRandomSound[]=");
        stringBuilder3.append(mRandomSound);
        stringBuilder3.append("\n mTimeAndSoundNumParameters[]=");
        stringBuilder3.append(mTimeAndSoundNumParameters);
        Log.i(TAG, stringBuilder3.toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:37:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x009f  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x007a  */
    public static java.lang.String getNotificationUri() {
        /*
        r0 = java.lang.System.currentTimeMillis();
        r2 = 0;
        r3 = mNotificationRandomSound;
        r4 = 2;
        r5 = 3;
        r6 = 0;
        r8 = 0;
        r9 = 1;
        if (r3 != 0) goto L_0x0045;
    L_0x000f:
        r3 = 1;
        r10 = mLastNotificationTimeMs;
        r6 = (r10 > r6 ? 1 : (r10 == r6 ? 0 : -1));
        if (r6 == 0) goto L_0x0039;
    L_0x0016:
        r6 = r0 - r10;
        r12 = mTimeAndSoundNumParameters;
        r5 = r12[r5];
        r13 = (long) r5;
        r5 = (r6 > r13 ? 1 : (r6 == r13 ? 0 : -1));
        if (r5 <= 0) goto L_0x0022;
    L_0x0021:
        goto L_0x0039;
    L_0x0022:
        r5 = r0 - r10;
        r4 = r12[r4];
        r7 = (long) r4;
        r4 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        if (r4 > 0) goto L_0x0033;
    L_0x002b:
        r4 = mHasPlayedNormalNotification;
        if (r4 != 0) goto L_0x003c;
    L_0x002f:
        mHasPlayedNormalNotification = r9;
        r3 = 0;
        goto L_0x003c;
    L_0x0033:
        r4 = mHasPlayedNormalNotification;
        if (r4 != 0) goto L_0x003c;
    L_0x0037:
        r3 = 0;
        goto L_0x003c;
    L_0x0039:
        r3 = 0;
        mHasPlayedNormalNotification = r8;
    L_0x003c:
        if (r3 != 0) goto L_0x0043;
    L_0x003e:
        r2 = "normal_notification";
        mLastNotificationTimeMs = r0;
    L_0x0043:
        goto L_0x00bb;
    L_0x0045:
        r3 = 0;
        r10 = 0;
        r11 = mLastNotificationTimeMs;
        r6 = (r11 > r6 ? 1 : (r11 == r6 ? 0 : -1));
        if (r6 == 0) goto L_0x0073;
    L_0x004d:
        r6 = r0 - r11;
        r13 = mTimeAndSoundNumParameters;
        r5 = r13[r5];
        r14 = (long) r5;
        r5 = (r6 > r14 ? 1 : (r6 == r14 ? 0 : -1));
        if (r5 <= 0) goto L_0x0059;
    L_0x0058:
        goto L_0x0073;
    L_0x0059:
        r5 = r0 - r11;
        r4 = r13[r4];
        r11 = (long) r4;
        r4 = (r5 > r11 ? 1 : (r5 == r11 ? 0 : -1));
        if (r4 > 0) goto L_0x006d;
    L_0x0062:
        r4 = mSeqIndex;
        r5 = r13[r9];
        if (r4 >= r5) goto L_0x0076;
    L_0x0068:
        r4 = r4 + r9;
        mSeqIndex = r4;
        r10 = 1;
        goto L_0x0076;
    L_0x006d:
        r4 = mSeqIndex;
        if (r4 != 0) goto L_0x0076;
    L_0x0071:
        r3 = 1;
        goto L_0x0076;
    L_0x0073:
        r3 = 1;
        mSeqIndex = r8;
    L_0x0076:
        r4 = ".ogg";
        if (r3 == 0) goto L_0x009f;
    L_0x007a:
        r5 = new java.util.Random;
        r5.<init>();
        r6 = mTimeAndSoundNumParameters;
        r6 = r6[r8];
        r5 = r5.nextInt(r6);
        r5 = r5 + r9;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = RANDOM_SOUND_PREFIX;
        r6.append(r7);
        r6.append(r5);
        r6.append(r4);
        r2 = r6.toString();
        mLastNotificationTimeMs = r0;
        goto L_0x00ba;
    L_0x009f:
        if (r10 == 0) goto L_0x00ba;
    L_0x00a1:
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = SEQUENCE_SOUND_PREFIX;
        r5.append(r6);
        r6 = mSeqIndex;
        r5.append(r6);
        r5.append(r4);
        r2 = r5.toString();
        mLastNotificationTimeMs = r0;
        goto L_0x00bb;
    L_0x00bb:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.AudioServiceInjector.getNotificationUri():java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0047  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0087  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0080  */
    public static java.lang.String getNotificationUri(java.lang.String r16) {
        /*
        r0 = java.lang.System.currentTimeMillis();
        r2 = 0;
        r3 = getIndexForType(r16);
        r3 = playRandomSound(r3);
        r4 = 2;
        r5 = 3;
        r6 = 0;
        r7 = 0;
        r9 = 1;
        if (r3 != 0) goto L_0x004d;
    L_0x0015:
        r3 = 1;
        r10 = mLastNotificationTimeMs;
        r7 = (r10 > r7 ? 1 : (r10 == r7 ? 0 : -1));
        if (r7 == 0) goto L_0x0042;
    L_0x001c:
        r7 = r0 - r10;
        r12 = mTimeAndSoundNumParameters;
        r5 = r12[r5];
        r13 = (long) r5;
        r5 = (r7 > r13 ? 1 : (r7 == r13 ? 0 : -1));
        if (r5 <= 0) goto L_0x0028;
    L_0x0027:
        goto L_0x0042;
    L_0x0028:
        r5 = r0 - r10;
        r4 = r12[r4];
        r7 = (long) r4;
        r4 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        if (r4 > 0) goto L_0x003c;
    L_0x0031:
        r4 = mSeqIndex;
        r5 = r12[r9];
        if (r4 >= r5) goto L_0x0045;
    L_0x0037:
        r4 = r4 + r9;
        mSeqIndex = r4;
        r3 = 0;
        goto L_0x0045;
    L_0x003c:
        r4 = mSeqIndex;
        if (r4 != 0) goto L_0x0045;
    L_0x0040:
        r3 = 0;
        goto L_0x0045;
    L_0x0042:
        r3 = 0;
        mSeqIndex = r6;
    L_0x0045:
        if (r3 != 0) goto L_0x004c;
    L_0x0047:
        r2 = "normal_notification";
        mLastNotificationTimeMs = r0;
    L_0x004c:
        goto L_0x008f;
    L_0x004d:
        r3 = 0;
        r10 = 0;
        r11 = mLastNotificationTimeMs;
        r7 = (r11 > r7 ? 1 : (r11 == r7 ? 0 : -1));
        if (r7 == 0) goto L_0x007b;
    L_0x0055:
        r7 = r0 - r11;
        r13 = mTimeAndSoundNumParameters;
        r5 = r13[r5];
        r14 = (long) r5;
        r5 = (r7 > r14 ? 1 : (r7 == r14 ? 0 : -1));
        if (r5 <= 0) goto L_0x0061;
    L_0x0060:
        goto L_0x007b;
    L_0x0061:
        r5 = r0 - r11;
        r4 = r13[r4];
        r7 = (long) r4;
        r4 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        if (r4 > 0) goto L_0x0075;
    L_0x006a:
        r4 = mSeqIndex;
        r5 = r13[r9];
        if (r4 >= r5) goto L_0x007e;
    L_0x0070:
        r4 = r4 + r9;
        mSeqIndex = r4;
        r10 = 1;
        goto L_0x007e;
    L_0x0075:
        r4 = mSeqIndex;
        if (r4 != 0) goto L_0x007e;
    L_0x0079:
        r3 = 1;
        goto L_0x007e;
    L_0x007b:
        r3 = 1;
        mSeqIndex = r6;
    L_0x007e:
        if (r3 == 0) goto L_0x0087;
    L_0x0080:
        r2 = createActualUriForNatureSound(r16);
        mLastNotificationTimeMs = r0;
        goto L_0x008f;
    L_0x0087:
        if (r10 == 0) goto L_0x008f;
    L_0x0089:
        r2 = createSeqUriForNatureSound(r16);
        mLastNotificationTimeMs = r0;
    L_0x008f:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.AudioServiceInjector.getNotificationUri(java.lang.String):java.lang.String");
    }

    private static int getIndexForType(String type) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getIndexForType() type=");
        stringBuilder.append(type);
        Log.d(TAG, stringBuilder.toString());
        return ((Integer) mTypes.get(type)).intValue();
    }

    private static boolean playRandomSound(int index) {
        if (index == -1) {
            return false;
        }
        boolean z;
        synchronized (mLockRandomStatusUpdate) {
            z = mRandomSound[index];
        }
        return z;
    }

    private static boolean wasStreamActiveRecently(int stream, int delay_ms) {
        return AudioSystem.isStreamActive(stream, delay_ms) || AudioSystem.isStreamActiveRemotely(stream, delay_ms);
    }

    public static int getActiveStreamType(boolean isInCommunication, int platformType, int suggestedStreamType, int streamOverrideDelayMs, boolean DEBUG_VOL, boolean lockVoiceAssistStream) {
        String str;
        int i = suggestedStreamType;
        int i2 = streamOverrideDelayMs;
        int voiceAssistStream = getVoiceAssistNum();
        String str2 = "getActiveStreamType: Forcing DEFAULT_VOL_STREAM_NO_PLAYBACK(3) b/c default";
        String str3 = "getActiveStreamType: Forcing STREAM_VOICEASSIST";
        String str4 = TAG;
        if (platformType == 1) {
            if (isInCommunication) {
                return AudioSystem.getForceUse(0) == 3 ? 6 : 0;
            } else {
                if (lockVoiceAssistStream) {
                    if (voiceAssistStream == -1 || wasStreamActiveRecently(voiceAssistStream, i2) || !wasStreamActiveRecently(3, i2)) {
                        return voiceAssistStream;
                    }
                    return 3;
                } else if (voiceAssistStream == -1 || !AudioSystem.isStreamActive(voiceAssistStream, i2)) {
                    str = "getActiveStreamType: Forcing STREAM_RING stream active";
                    String str5 = "getActiveStreamType: Forcing STREAM_NOTIFICATION stream active";
                    if (i == Integer.MIN_VALUE) {
                        if (wasStreamActiveRecently(2, i2)) {
                            if (DEBUG_VOL) {
                                Log.v(str4, str);
                            }
                            return 2;
                        } else if (wasStreamActiveRecently(5, i2)) {
                            if (DEBUG_VOL) {
                                Log.v(str4, str5);
                            }
                            return 5;
                        } else {
                            if (DEBUG_VOL) {
                                Log.v(str4, str2);
                            }
                            return 3;
                        }
                    } else if (wasStreamActiveRecently(5, i2)) {
                        if (DEBUG_VOL) {
                            Log.v(str4, str5);
                        }
                        return 5;
                    } else if (wasStreamActiveRecently(2, i2)) {
                        if (DEBUG_VOL) {
                            Log.v(str4, str);
                        }
                        return 2;
                    }
                } else {
                    if (DEBUG_VOL) {
                        Log.v(str4, str3);
                    }
                    return voiceAssistStream;
                }
            }
        }
        if (isInCommunication) {
            if (AudioSystem.getForceUse(0) == 3) {
                if (DEBUG_VOL) {
                    Log.v(str4, "getActiveStreamType: Forcing STREAM_BLUETOOTH_SCO");
                }
                return 6;
            }
            if (DEBUG_VOL) {
                Log.v(str4, "getActiveStreamType: Forcing STREAM_VOICE_CALL");
            }
            return 0;
        } else if (!lockVoiceAssistStream) {
            String str6 = "getActiveStreamType: Forcing STREAM_NOTIFICATION";
            if (AudioSystem.isStreamActive(5, i2)) {
                if (DEBUG_VOL) {
                    Log.v(str4, str6);
                }
                return 5;
            }
            str = "getActiveStreamType: Forcing STREAM_RING";
            if (AudioSystem.isStreamActive(2, i2)) {
                if (DEBUG_VOL) {
                    Log.v(str4, str);
                }
                return 2;
            } else if (voiceAssistStream != -1 && AudioSystem.isStreamActive(voiceAssistStream, i2)) {
                if (DEBUG_VOL) {
                    Log.v(str4, str3);
                }
                return voiceAssistStream;
            } else if (i != Integer.MIN_VALUE) {
                if (DEBUG_VOL) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("getActiveStreamType: Returning suggested type ");
                    stringBuilder.append(i);
                    Log.v(str4, stringBuilder.toString());
                }
                return i;
            } else if (AudioSystem.isStreamActive(5, i2)) {
                if (DEBUG_VOL) {
                    Log.v(str4, str6);
                }
                return 5;
            } else if (AudioSystem.isStreamActive(2, i2)) {
                if (DEBUG_VOL) {
                    Log.v(str4, str);
                }
                return 2;
            } else {
                if (DEBUG_VOL) {
                    Log.v(str4, str2);
                }
                return 3;
            }
        } else if (voiceAssistStream == -1 || wasStreamActiveRecently(voiceAssistStream, i2) || !wasStreamActiveRecently(3, i2)) {
            return voiceAssistStream;
        } else {
            return 3;
        }
    }

    public static int getActiveStreamType(boolean isInCommunication, int platformType, int suggestedStreamType, int streamOverrideDelayMs, boolean DEBUG_VOL) {
        return getActiveStreamType(isInCommunication, platformType, suggestedStreamType, streamOverrideDelayMs, DEBUG_VOL, false);
    }

    public static boolean isActiveForReal(boolean orgActiveForReal, int maybeActiveStreamType) {
        if (isXOptMode()) {
            return orgActiveForReal;
        }
        if (maybeActiveStreamType == 2 || maybeActiveStreamType == 5) {
            return wasStreamActiveRecently(maybeActiveStreamType, 0);
        }
        return AudioSystem.isStreamActive(maybeActiveStreamType, 0);
    }

    public static int getDefaultVolStreamNoPlayback(int defaultType) {
        if (isXOptMode()) {
            return defaultType;
        }
        return 3;
    }

    public static boolean needEnableVoiceVolumeBoost(int direction, boolean isMaxVol, int device, int streamTypeAlias, boolean boostEnabled) {
        if (!isXOptMode() && streamTypeAlias == 0 && device == 1) {
            if ("manual".equals(SystemProperties.get("ro.vendor.audio.voice.volume.boost"))) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("direction:");
                stringBuilder.append(direction);
                stringBuilder.append(" isMaxVol:");
                stringBuilder.append(isMaxVol);
                stringBuilder.append(" device:");
                stringBuilder.append(device);
                stringBuilder.append(" streamTypeAlias:");
                stringBuilder.append(streamTypeAlias);
                stringBuilder.append(" boostEnabled:");
                stringBuilder.append(boostEnabled);
                Log.d(TAG, stringBuilder.toString());
                if (direction == 1 && isMaxVol && !boostEnabled) {
                    return true;
                }
                return direction == -1 && boostEnabled;
            }
        }
        return false;
    }

    public static boolean setVolumeBoost(boolean boostEnabled, Context context) {
        AudioManager am = (AudioManager) context.getSystemService("audio");
        String params = new StringBuilder();
        params.append("voice_volume_boost=");
        params.append(boostEnabled ? "false" : "true");
        params = params.toString();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("params:");
        stringBuilder.append(params);
        Log.d(TAG, stringBuilder.toString());
        am.setParameters(params);
        sendVolumeBoostBroadcast(boostEnabled ^ 1, context);
        return boostEnabled ^ 1;
    }

    public static void sendVolumeBoostBroadcast(boolean boostEnabled, Context context) {
        long ident = Binder.clearCallingIdentity();
        try {
            Intent intent = new Intent(ACTION_VOLUME_BOOST);
            intent.putExtra(EXTRA_BOOST_STATE, boostEnabled);
            context.sendStickyBroadcastAsUser(intent, UserHandle.ALL);
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    public static void handleModeChanged(Context context, int pid, int mode) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(AudioStatusHandler.KEY_USAGE, Integer.valueOf(0));
        bundle.putInt("pid", pid);
        bundle.putInt("state", mode);
        handleAudioStatusChanged(context, bundle);
    }

    public static void handleSpeakerChanged(Context context, int pid, boolean speakeron) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("None thiing for handleSpeakerChanged:");
        stringBuilder.append(pid);
        stringBuilder.append("/");
        stringBuilder.append(speakeron);
        Log.w(TAG, stringBuilder.toString());
    }

    public static void handleSetForceUse(Context context, int usage, int config, String eventSource) {
        AudioStatusHandler.getInstance(context).handleSetForceUse(usage, config, eventSource);
    }

    public static void handleAudioStatusChanged(Context context, Bundle bundle) {
        AudioStatusHandler.getInstance(context).handleAudioStatusChanged(bundle);
    }

    public static int getVoiceAssistNum() {
        for (int i = 0; i < AudioSystem.STREAM_NAMES.length; i++) {
            if ("STREAM_VOICEASSIST".equals(AudioSystem.STREAM_NAMES[i])) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isAppCalledInCall(Context context) {
        boolean isInCall = false;
        if (Binder.getCallingUid() >= 10000) {
            TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
            long ident = Binder.clearCallingIdentity();
            isInCall = telecomManager.isInCall();
            Binder.restoreCallingIdentity(ident);
            if (isInCall) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("called from app when in call: pid = ");
                stringBuilder.append(Binder.getCallingPid());
                stringBuilder.append(", uid = ");
                stringBuilder.append(Binder.getCallingUid());
                Log.w(TAG, stringBuilder.toString());
            }
        }
        return isInCall;
    }

    public static boolean supportKaraokeMode(String pkgName) {
        return sAppList.get(pkgName) != null;
    }

    public static void broadcastRecorderState(int state, int sessionId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("broadcastRecorderState:");
        stringBuilder.append(ActivityThread.currentPackageName());
        Log.d(TAG, stringBuilder.toString());
        if (supportKaraokeMode(ActivityThread.currentPackageName())) {
            Intent intent = new Intent();
            intent.setAction("miui.media.AUDIO_RECORD_STATE_CHANGED_ACTION");
            intent.setFlags(268435456);
            intent.putExtra("state", state);
            intent.putExtra(Engine.KEY_PARAM_SESSION_ID, sessionId);
            long ident = Binder.clearCallingIdentity();
            try {
                ActivityThread.currentApplication().getApplicationContext().sendBroadcast(intent, "com.miui.permission.AUDIO_RECORD_STATE_CHANGED_ACTION");
            } finally {
                Binder.restoreCallingIdentity(ident);
            }
        }
    }
}
