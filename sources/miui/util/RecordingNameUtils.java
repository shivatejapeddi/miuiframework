package miui.util;

import android.content.Context;
import android.database.Cursor;
import android.miui.R;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;
import android.telecom.Logging.Session;
import android.text.TextUtils;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

public class RecordingNameUtils {
    private static final int MAX_FILE_NAME_LENGTH = 50;
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    static {
        sDateFormat.setTimeZone(Calendar.getInstance().getTimeZone());
    }

    public static String generateCallRecordName(List<String> callerNames, List<String> callerNumbers, String extension) {
        String pendingInfo = new StringBuilder();
        String str = Session.SESSION_SEPARATION_CHAR_CHILD;
        pendingInfo.append(str);
        pendingInfo.append(sDateFormat.format(Calendar.getInstance().getTime()));
        pendingInfo.append(extension);
        pendingInfo = pendingInfo.toString();
        int remainLength = 50 - pendingInfo.length();
        StringBuilder callerBuilder = new StringBuilder();
        int i = 0;
        while (i < callerNumbers.size()) {
            callerBuilder.append(i > 0 ? str : "");
            callerBuilder.append((String) callerNumbers.get(i));
            i++;
        }
        i = remainLength - callerBuilder.length();
        int lastPos = 0;
        for (int i2 = 0; i2 < callerNames.size() && i > 2; i2++) {
            String name = (String) callerNames.get(i2);
            if (name.length() > i - 2) {
                name = name.substring(0, i - 2);
            }
            if (TextUtils.isEmpty(name)) {
                lastPos = callerBuilder.indexOf(str, lastPos + 1) + 1;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(name);
                stringBuilder.append("(");
                callerBuilder.insert(lastPos, stringBuilder.toString());
                lastPos = callerBuilder.indexOf(str, lastPos + 1);
                if (lastPos < 0) {
                    lastPos = callerBuilder.length();
                }
                callerBuilder.insert(lastPos, ')');
                if (callerBuilder.charAt(lastPos - 1) == '(') {
                    callerBuilder.deleteCharAt(lastPos - 1);
                    callerBuilder.deleteCharAt(lastPos - 1);
                } else {
                    lastPos += 2;
                    i -= 2;
                }
                i -= name.length();
            }
        }
        int callerLen = remainLength < callerBuilder.length() ? remainLength : callerBuilder.length();
        StringBuilder fileNameBuilder = new StringBuilder();
        fileNameBuilder.append(callerBuilder.substring(0, callerLen));
        fileNameBuilder.append(pendingInfo);
        return fileNameBuilder.toString().replace(',', 'p').replace("+", "00").replace('*', 's');
    }

    public static String generatFMRecordName(Context context, String fmTitle, String extension) {
        StringBuilder fileNameBuilder = new StringBuilder();
        fileNameBuilder.append(context.getString(R.string.fm_record_smaple_prefix_miui));
        fileNameBuilder.append('@');
        fileNameBuilder.append(fmTitle);
        fileNameBuilder.append('_');
        fileNameBuilder.append(sDateFormat.format(Calendar.getInstance().getTime()));
        fileNameBuilder.append(extension);
        return fileNameBuilder.toString();
    }

    private static String getFileNameWithoutExtension(String path) {
        String name;
        int index = path.lastIndexOf(File.separatorChar);
        if (index != -1) {
            name = path.substring(index + 1);
        } else {
            name = path;
        }
        index = name.lastIndexOf(46);
        if (index != -1) {
            return name.substring(0, index);
        }
        return name;
    }

    public static String getRecordingFileTitle(String name) {
        int index = name.indexOf(64);
        if (index == -1 || index == name.length() - 1 || name.substring(0, index).contains(Session.SESSION_SEPARATION_CHAR_CHILD)) {
            return name;
        }
        int endpos = name.lastIndexOf(95);
        if (endpos > index) {
            name = name.substring(0, endpos);
        }
        return name.substring(index + 1);
    }

    public static String getFMRecordingTitle(String name) {
        return getRecordingFileTitle(name);
    }

    public static String getCallRecordingTitle(Context context, String name) {
        String title = getRecordingFileTitle(name);
        int index = name.indexOf(64);
        if (index == -1 || index == name.length() - 1 || title == null) {
            return title;
        }
        String callers = getCallers(context, title);
        return callers == null ? title : callers;
    }

    public static String[] getCallRecordingCallerNumbers(Context context, String name) {
        String title = getRecordingFileTitle(name);
        if (title != null) {
            return getPhoneNumbers(context, title);
        }
        return null;
    }

    private static String getCallers(Context context, String title) {
        String[] numbers = getPhoneNumbers(context, title);
        if (numbers.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numbers.length; i++) {
            sb.append(getCallerString(context, numbers[i]));
            if (i != numbers.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    private static String[] getPhoneNumbers(Context context, String title) {
        String[] numbers = title.replace('p', ',').replace('s', '*').split(Session.SESSION_SEPARATION_CHAR_CHILD);
        for (int i = 0; i < numbers.length; i++) {
            int indexOfBeginParenthesis = numbers[i].indexOf(40);
            int indexOfEndParenthesis = numbers[i].indexOf(41);
            if (indexOfBeginParenthesis > 0 && indexOfEndParenthesis > indexOfBeginParenthesis) {
                numbers[i] = numbers[i].substring(indexOfBeginParenthesis + 1, indexOfEndParenthesis);
            }
        }
        return numbers;
    }

    private static String getCallerString(Context context, String number) {
        StringBuilder caller = new StringBuilder();
        if (number == null || TextUtils.equals(number, "")) {
            caller.append(context.getString(R.string.unknown_caller));
        } else {
            Cursor c = context.getContentResolver().query(Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, number), null, null, null, null);
            if (c == null) {
                return number;
            }
            HashSet<String> nameSet = new HashSet();
            int nameIndex = c.getColumnIndex("display_name");
            boolean first = true;
            while (c.moveToNext()) {
                String name = c.getString(nameIndex);
                if (!nameSet.contains(name)) {
                    if (!first) {
                        caller.append(' ');
                        caller.append(context.getString(R.string.call_record_or));
                        caller.append(' ');
                    }
                    caller.append(name);
                    first = false;
                    nameSet.add(name);
                }
            }
            c.close();
        }
        return caller.length() == 0 ? number : caller.toString();
    }

    public static long getRecordingCreationTime(Context context, String name) {
        name = getFileNameWithoutExtension(name);
        int startIndex = name.lastIndexOf(95);
        if (startIndex != -1) {
            name = name.substring(startIndex + 1);
        }
        try {
            return sDateFormat.parse(name).getTime();
        } catch (Exception e) {
            return 0;
        }
    }
}
