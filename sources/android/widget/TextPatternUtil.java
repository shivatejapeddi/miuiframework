package android.widget;

import android.text.TextUtils;
import android.util.Patterns;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TextPatternUtil {
    private static final Pattern EMAIL_PATTERN = Patterns.EMAIL_ADDRESS;
    private static final int MAX_EMAIL_ADDR_LENGTH = 256;

    static class EmailInfo {
        int cursorPos;
        String email;
        int start;

        EmailInfo(int s, int c, String e) {
            this.start = s;
            this.cursorPos = c;
            this.email = e;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean isContainedIn(String address) {
            String lowerEmail = this.email;
            if (lowerEmail == null || address == null) {
                return false;
            }
            lowerEmail = lowerEmail.toLowerCase();
            String lowerAddress = address.toLowerCase();
            if (lowerEmail.equals(lowerAddress)) {
                return true;
            }
            return lowerAddress.contains(lowerEmail);
        }

        /* Access modifiers changed, original: 0000 */
        public boolean isInList(List<String> list) {
            if (list != null && list.size() > 0) {
                for (String item : list) {
                    if (isContainedIn(item)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("EmailInfo{start = ");
            stringBuilder.append(this.start);
            stringBuilder.append(", email = ");
            stringBuilder.append(this.email);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    private TextPatternUtil() {
    }

    public static List<EmailInfo> getEmailList(int startPos, int curPos, String input) {
        if (TextUtils.isEmpty(input)) {
            return new ArrayList();
        }
        List<EmailInfo> emailList = null;
        Matcher m = EMAIL_PATTERN.matcher(input);
        while (m.find()) {
            if (emailList == null) {
                emailList = new ArrayList();
            }
            boolean existed = false;
            String email = m.group();
            for (EmailInfo item : emailList) {
                if (item.isContainedIn(email)) {
                    existed = true;
                    break;
                }
            }
            if (!existed) {
                emailList.add(new EmailInfo(m.start() + startPos, curPos, email));
            }
        }
        return emailList;
    }

    public static EmailInfo findEmailAtPos(String text, int curPos) {
        int startPos = curPos - 256;
        startPos = startPos < 0 ? 0 : startPos;
        int endPos = curPos + 256;
        List<EmailInfo> emailList = getEmailList(startPos, curPos, text.substring(startPos, endPos > text.length() ? text.length() : endPos));
        if (emailList != null && emailList.size() > 0) {
            for (EmailInfo email : emailList) {
                if (inRange(curPos, email.start, email.start + email.email.length())) {
                    return email;
                }
            }
        }
        return null;
    }

    private static boolean inRange(int pos, int rangeStart, int rangeEnd) {
        return pos >= rangeStart && pos <= rangeEnd;
    }
}
