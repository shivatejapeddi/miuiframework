package android.widget;

import android.telephony.PhoneNumberUtils;
import android.text.util.Linkify.MatchFilter;
import android.text.util.Linkify.TransformFilter;
import android.webkit.WebView;
import com.android.i18n.phonenumbers.PhoneNumberMatch;
import com.android.i18n.phonenumbers.PhoneNumberUtil;
import com.android.i18n.phonenumbers.PhoneNumberUtil.Leniency;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import miui.util.Patterns;

public class Linkify {
    public static final int PHONE_NUMBERS = 4;
    private static final int PHONE_NUMBER_MINIMUM_DIGITS = 5;
    private static final Pattern WEB_CHAR_PATTERN = Pattern.compile("[a-zA-Z0-9\\.]");
    public static final int WEB_URLS = 1;
    public static final MatchFilter sUrlMatchFilter = android.text.util.Linkify.sUrlMatchFilter;

    static final ArrayList<LinkSpec> getLinks(CharSequence text, int start, int end, int mask) {
        if (start == -1 || end == -1 || start > end) {
            return null;
        }
        ArrayList<LinkSpec> links = new ArrayList();
        boolean hasSelection = start != end;
        if ((mask & 1) != 0) {
            gatherWebLinks(links, text, start, end, hasSelection);
        }
        if ((mask & 4) != 0) {
            gatherTelLinks(links, text, start, end, hasSelection);
        }
        return links;
    }

    private static final void gatherLinks(ArrayList<LinkSpec> links, CharSequence text, int start, int end, Pattern pattern, String[] schemes, MatchFilter matchFilter, TransformFilter transformFilter) {
        Matcher m = pattern.matcher(text.subSequence(start, end));
        while (m.find()) {
            int s = m.start();
            int e = m.end();
            if (matchFilter == null || matchFilter.acceptMatch(text, s, e)) {
                LinkSpec spec = new LinkSpec();
                spec.url = makeUrl(m.group(0), schemes, m, transformFilter);
                spec.start = s;
                spec.end = e;
                links.add(spec);
            }
        }
    }

    private static final void gatherWebLinks(ArrayList<LinkSpec> links, CharSequence text, int start, int end, boolean hasSelection) {
        int i = hasSelection ? start : start - 1;
        while (i >= 0 && isValidChar(text.charAt(i), WEB_CHAR_PATTERN)) {
            start = i;
            i--;
        }
        i = hasSelection ? end - 1 : end;
        while (i >= 0 && i < text.length() && isValidChar(text.charAt(i), WEB_CHAR_PATTERN)) {
            end = i + 1;
            i++;
        }
        gatherLinks(links, text, start, end, Patterns.WEB_URL, new String[]{"http://", "https://", "rtsp://"}, sUrlMatchFilter, null);
    }

    private static final void gatherTelLinks(ArrayList<LinkSpec> links, CharSequence text, int start, int end, boolean hasSelection) {
        int i = hasSelection ? start : start - 1;
        while (i >= 0 && Character.isDigit(text.charAt(i))) {
            start = i;
            i--;
        }
        i = hasSelection ? end - 1 : end;
        while (i >= 0 && i < text.length() && Character.isDigit(text.charAt(i))) {
            end = i + 1;
            i++;
        }
        if (end - start >= 5) {
            for (PhoneNumberMatch match : PhoneNumberUtil.getInstance().findNumbers(text.subSequence(start, end), Locale.getDefault().getCountry(), Leniency.POSSIBLE, Long.MAX_VALUE)) {
                LinkSpec spec = new LinkSpec();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(WebView.SCHEME_TEL);
                stringBuilder.append(PhoneNumberUtils.normalizeNumber(match.rawString()));
                spec.url = stringBuilder.toString();
                spec.start = match.start();
                spec.end = match.end();
                links.add(spec);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0052  */
    private static final java.lang.String makeUrl(java.lang.String r8, java.lang.String[] r9, java.util.regex.Matcher r10, android.text.util.Linkify.TransformFilter r11) {
        /*
        if (r11 == 0) goto L_0x0006;
    L_0x0002:
        r8 = r11.transformUrl(r10, r8);
    L_0x0006:
        r6 = 0;
        r0 = 0;
        r7 = r0;
    L_0x0009:
        r0 = r9.length;
        if (r7 >= r0) goto L_0x0050;
    L_0x000c:
        r1 = 1;
        r2 = 0;
        r3 = r9[r7];
        r4 = 0;
        r0 = r9[r7];
        r5 = r0.length();
        r0 = r8;
        r0 = r0.regionMatches(r1, r2, r3, r4, r5);
        if (r0 == 0) goto L_0x004d;
    L_0x001e:
        r6 = 1;
        r1 = 0;
        r2 = 0;
        r3 = r9[r7];
        r4 = 0;
        r0 = r9[r7];
        r5 = r0.length();
        r0 = r8;
        r0 = r0.regionMatches(r1, r2, r3, r4, r5);
        if (r0 != 0) goto L_0x0050;
    L_0x0031:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = r9[r7];
        r0.append(r1);
        r1 = r9[r7];
        r1 = r1.length();
        r1 = r8.substring(r1);
        r0.append(r1);
        r8 = r0.toString();
        goto L_0x0050;
    L_0x004d:
        r7 = r7 + 1;
        goto L_0x0009;
    L_0x0050:
        if (r6 != 0) goto L_0x0064;
    L_0x0052:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = 0;
        r1 = r9[r1];
        r0.append(r1);
        r0.append(r8);
        r8 = r0.toString();
    L_0x0064:
        return r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.Linkify.makeUrl(java.lang.String, java.lang.String[], java.util.regex.Matcher, android.text.util.Linkify$TransformFilter):java.lang.String");
    }

    private static boolean isValidChar(char c, Pattern pattern) {
        return pattern.matcher(String.valueOf(c)).matches();
    }
}
