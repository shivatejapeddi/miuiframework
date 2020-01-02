package com.android.internal.telephony.gsm;

import android.app.backup.FullBackup;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.telephony.SmsCbLocation;
import android.telephony.SmsCbMessage;
import android.util.Pair;
import com.android.internal.R;
import com.android.internal.telephony.GsmAlphabet;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class GsmSmsCbMessage {
    private static final char CARRIAGE_RETURN = '\r';
    private static final String[] LANGUAGE_CODES_GROUP_0 = new String[]{Locale.GERMAN.getLanguage(), Locale.ENGLISH.getLanguage(), Locale.ITALIAN.getLanguage(), Locale.FRENCH.getLanguage(), new Locale("es").getLanguage(), new Locale("nl").getLanguage(), new Locale("sv").getLanguage(), new Locale("da").getLanguage(), new Locale("pt").getLanguage(), new Locale("fi").getLanguage(), new Locale(FullBackup.NO_BACKUP_TREE_TOKEN).getLanguage(), new Locale("el").getLanguage(), new Locale("tr").getLanguage(), new Locale("hu").getLanguage(), new Locale("pl").getLanguage(), null};
    private static final String[] LANGUAGE_CODES_GROUP_2 = new String[]{new Locale("cs").getLanguage(), new Locale("he").getLanguage(), new Locale("ar").getLanguage(), new Locale("ru").getLanguage(), new Locale("is").getLanguage(), null, null, null, null, null, null, null, null, null, null, null};
    private static final int PDU_BODY_PAGE_LENGTH = 82;

    private GsmSmsCbMessage() {
    }

    private static String getEtwsPrimaryMessage(Context context, int category) {
        Resources r = context.getResources();
        if (category == 0) {
            return r.getString(R.string.etws_primary_default_message_earthquake);
        }
        if (category == 1) {
            return r.getString(R.string.etws_primary_default_message_tsunami);
        }
        if (category == 2) {
            return r.getString(R.string.etws_primary_default_message_earthquake_and_tsunami);
        }
        if (category == 3) {
            return r.getString(R.string.etws_primary_default_message_test);
        }
        if (category != 4) {
            return "";
        }
        return r.getString(R.string.etws_primary_default_message_others);
    }

    public static SmsCbMessage createSmsCbMessage(Context context, SmsCbHeader header, SmsCbLocation location, byte[][] pdus) throws IllegalArgumentException {
        byte[][] bArr = pdus;
        if (header.isEtwsPrimaryNotification()) {
            return new SmsCbMessage(1, header.getGeographicalScope(), header.getSerialNumber(), location, header.getServiceCategory(), null, getEtwsPrimaryMessage(context, header.getEtwsInfo().getWarningType()), 3, header.getEtwsInfo(), header.getCmasInfo());
        }
        int priority;
        Context context2 = context;
        StringBuilder sb = new StringBuilder();
        String language = null;
        for (byte[] pdu : bArr) {
            Pair<String, String> p = parseBody(header, pdu);
            language = p.first;
            sb.append((String) p.second);
        }
        SmsCbHeader smsCbHeader = header;
        if (header.isEmergencyMessage()) {
            priority = 3;
        } else {
            priority = 0;
        }
        return new SmsCbMessage(1, header.getGeographicalScope(), header.getSerialNumber(), location, header.getServiceCategory(), language, sb.toString(), priority, header.getEtwsInfo(), header.getCmasInfo());
    }

    private static Pair<String, String> parseBody(SmsCbHeader header, byte[] pdu) {
        Object obj = pdu;
        int dataCodingScheme = header.getDataCodingScheme();
        int i = (dataCodingScheme & 240) >> 4;
        if (!(i == 9 || i == 14)) {
            String language;
            boolean hasLanguageIndicator;
            int encoding;
            if (i != 15) {
                switch (i) {
                    case 0:
                        language = LANGUAGE_CODES_GROUP_0[dataCodingScheme & 15];
                        hasLanguageIndicator = false;
                        encoding = 1;
                        break;
                    case 1:
                        if ((dataCodingScheme & 15) != 1) {
                            language = null;
                            hasLanguageIndicator = true;
                            encoding = 1;
                            break;
                        }
                        language = null;
                        hasLanguageIndicator = true;
                        encoding = 3;
                        break;
                    case 2:
                        language = LANGUAGE_CODES_GROUP_2[dataCodingScheme & 15];
                        hasLanguageIndicator = false;
                        encoding = 1;
                        break;
                    case 3:
                        language = null;
                        hasLanguageIndicator = false;
                        encoding = 1;
                        break;
                    case 4:
                    case 5:
                        i = (dataCodingScheme & 12) >> 2;
                        if (i != 1) {
                            if (i == 2) {
                                language = null;
                                hasLanguageIndicator = false;
                                encoding = 3;
                                break;
                            }
                            language = null;
                            hasLanguageIndicator = false;
                            encoding = 1;
                            break;
                        }
                        language = null;
                        hasLanguageIndicator = false;
                        encoding = 2;
                        break;
                    case 6:
                    case 7:
                        break;
                    default:
                        language = null;
                        hasLanguageIndicator = false;
                        encoding = 1;
                        break;
                }
            } else if (((dataCodingScheme & 4) >> 2) == 1) {
                language = null;
                hasLanguageIndicator = false;
                encoding = 2;
            } else {
                language = null;
                hasLanguageIndicator = false;
                encoding = 1;
            }
            if (header.isUmtsFormat()) {
                int nrPages = obj[6];
                if (obj.length >= (nrPages * 83) + 7) {
                    StringBuilder sb = new StringBuilder();
                    String language2 = language;
                    language = null;
                    while (language < nrPages) {
                        int offset = (language * 83) + 7;
                        int length = obj[offset + 82];
                        if (length <= 82) {
                            Pair<String, String> p = unpackBody(pdu, encoding, offset, length, hasLanguageIndicator, language2);
                            language2 = p.first;
                            sb.append((String) p.second);
                            language++;
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Page length ");
                            stringBuilder.append(length);
                            stringBuilder.append(" exceeds maximum value ");
                            stringBuilder.append(82);
                            throw new IllegalArgumentException(stringBuilder.toString());
                        }
                    }
                    return new Pair(language2, sb.toString());
                }
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Pdu length ");
                stringBuilder2.append(obj.length);
                stringBuilder2.append(" does not match ");
                stringBuilder2.append(nrPages);
                stringBuilder2.append(" pages");
                throw new IllegalArgumentException(stringBuilder2.toString());
            }
            return unpackBody(pdu, encoding, 6, obj.length - 6, hasLanguageIndicator, language);
        }
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("Unsupported GSM dataCodingScheme ");
        stringBuilder3.append(dataCodingScheme);
        throw new IllegalArgumentException(stringBuilder3.toString());
    }

    private static Pair<String, String> unpackBody(byte[] pdu, int encoding, int offset, int length, boolean hasLanguageIndicator, String language) {
        String body = null;
        if (encoding == 1) {
            body = GsmAlphabet.gsm7BitPackedToString(pdu, offset, (length * 8) / 7);
            if (hasLanguageIndicator && body != null && body.length() > 2) {
                language = body.substring(0, 2);
                body = body.substring(3);
            }
        } else if (encoding == 3) {
            if (hasLanguageIndicator && pdu.length >= offset + 2) {
                language = GsmAlphabet.gsm7BitPackedToString(pdu, offset, 2);
                offset += 2;
                length -= 2;
            }
            try {
                body = new String(pdu, offset, Configuration.DENSITY_DPI_ANY & length, "utf-16");
            } catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException("Error decoding UTF-16 message", e);
            }
        }
        if (body != null) {
            for (int i = body.length() - 1; i >= 0; i--) {
                if (body.charAt(i) != 13) {
                    body = body.substring(0, i + 1);
                    break;
                }
            }
        } else {
            body = "";
        }
        return new Pair(language, body);
    }
}
