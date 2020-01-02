package com.android.internal.telephony;

import android.annotation.UnsupportedAppUsage;
import android.content.res.Resources;
import android.telephony.Rlog;
import android.text.TextUtils;
import android.util.SparseIntArray;
import com.android.internal.R;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class GsmAlphabet {
    public static final byte GSM_EXTENDED_ESCAPE = (byte) 27;
    private static final String TAG = "GSM";
    public static final int UDH_SEPTET_COST_CONCATENATED_MESSAGE = 6;
    public static final int UDH_SEPTET_COST_LENGTH = 1;
    public static final int UDH_SEPTET_COST_ONE_SHIFT_TABLE = 4;
    public static final int UDH_SEPTET_COST_TWO_SHIFT_TABLES = 7;
    @UnsupportedAppUsage
    private static final SparseIntArray[] sCharsToGsmTables;
    @UnsupportedAppUsage
    private static final SparseIntArray[] sCharsToShiftTables;
    private static boolean sDisableCountryEncodingCheck = false;
    @UnsupportedAppUsage
    private static int[] sEnabledLockingShiftTables;
    @UnsupportedAppUsage
    private static int[] sEnabledSingleShiftTables;
    @UnsupportedAppUsage
    private static int sHighestEnabledSingleShiftCode;
    @UnsupportedAppUsage
    private static final String[] sLanguageShiftTables = new String[]{"          \f         ^                   {}     \\            [~] |                                    €                          ", "          \f         ^                   {}     \\            [~] |      Ğ İ         Ş               ç € ğ ı         ş            ", "         ç\f         ^                   {}     \\            [~] |Á       Í     Ó     Ú           á   €   í     ó     ú          ", "     ê   ç\fÔô Áá  ΦΓ^ΩΠΨΣΘ     Ê        {}     \\            [~] |À       Í     Ó     Ú     ÃÕ    Â   €   í     ó     ú     ãõ  â", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*০১ ২৩৪৫৬৭৮৯য়ৠৡৢ{}ৣ৲৳৴৵\\৶৷৸৹৺       [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ૦૧૨૩૪૫૬૭૮૯  {}     \\            [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ०१२३४५६७८९॒॑{}॓॔क़ख़ग़\\ज़ड़ढ़फ़य़ॠॡॢॣ॰ॱ [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ೦೧೨೩೪೫೬೭೮೯ೞೱ{}ೲ    \\            [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ൦൧൨൩൪൫൬൭൮൯൰൱{}൲൳൴൵ൺ\\ൻർൽൾൿ       [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ୦୧୨୩୪୫୬୭୮୯ଡ଼ଢ଼{}ୟ୰ୱ  \\            [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ੦੧੨੩੪੫੬੭੮੯ਖ਼ਗ਼{}ਜ਼ੜਫ਼ੵ \\            [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ௦௧௨௩௪௫௬௭௮௯௳௴{}௵௶௷௸௺\\            [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*   ౦౧౨౩౪౫౬౭౮౯ౘౙ{}౸౹౺౻౼\\౽౾౿         [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*؀؁ ۰۱۲۳۴۵۶۷۸۹،؍{}؎؏ؐؑؒ\\ؓؔ؛؟ـْ٘٫٬ٲٳۍ[~]۔|ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          "};
    @UnsupportedAppUsage
    private static final String[] sLanguageTables = new String[]{"@£$¥èéùìòÇ\nØø\rÅåΔ_ΦΓΛΩΠΨΣΘΞ￿ÆæßÉ !\"#¤%&'()*+,-./0123456789:;<=>?¡ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÑÜ§¿abcdefghijklmnopqrstuvwxyzäöñüà", "@£$¥€éùıòÇ\nĞğ\rÅåΔ_ΦΓΛΩΠΨΣΘΞ￿ŞşßÉ !\"#¤%&'()*+,-./0123456789:;<=>?İABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÑÜ§çabcdefghijklmnopqrstuvwxyzäöñüà", "", "@£$¥êéúíóç\nÔô\rÁáΔ_ªÇÀ∞^\\€Ó|￿ÂâÊÉ !\"#º%&'()*+,-./0123456789:;<=>?ÍABCDEFGHIJKLMNOPQRSTUVWXYZÃÕÚÜ§~abcdefghijklmnopqrstuvwxyzãõ`üà", "ঁংঃঅআইঈউঊঋ\nঌ \r এঐ  ওঔকখগঘঙচ￿ছজঝঞ !টঠডঢণত)(থদ,ধ.ন0123456789:; পফ?বভমযর ল   শষসহ়ঽািীুূৃৄ  েৈ  োৌ্ৎabcdefghijklmnopqrstuvwxyzৗড়ঢ়ৰৱ", "ઁંઃઅઆઇઈઉઊઋ\nઌઍ\r એઐઑ ઓઔકખગઘઙચ￿છજઝઞ !ટઠડઢણત)(થદ,ધ.ન0123456789:; પફ?બભમયર લળ વશષસહ઼ઽાિીુૂૃૄૅ ેૈૉ ોૌ્ૐabcdefghijklmnopqrstuvwxyzૠૡૢૣ૱", "ँंःअआइईउऊऋ\nऌऍ\rऎएऐऑऒओऔकखगघङच￿छजझञ !टठडढणत)(थद,ध.न0123456789:;ऩपफ?बभमयरऱलळऴवशषसह़ऽािीुूृॄॅॆेैॉॊोौ्ॐabcdefghijklmnopqrstuvwxyzॲॻॼॾॿ", " ಂಃಅಆಇಈಉಊಋ\nಌ \rಎಏಐ ಒಓಔಕಖಗಘಙಚ￿ಛಜಝಞ !ಟಠಡಢಣತ)(ಥದ,ಧ.ನ0123456789:; ಪಫ?ಬಭಮಯರಱಲಳ ವಶಷಸಹ಼ಽಾಿೀುೂೃೄ ೆೇೈ ೊೋೌ್ೕabcdefghijklmnopqrstuvwxyzೖೠೡೢೣ", " ംഃഅആഇഈഉഊഋ\nഌ \rഎഏഐ ഒഓഔകഖഗഘങച￿ഛജഝഞ !ടഠഡഢണത)(ഥദ,ധ.ന0123456789:; പഫ?ബഭമയരറലളഴവശഷസഹ ഽാിീുൂൃൄ െേൈ ൊോൌ്ൗabcdefghijklmnopqrstuvwxyzൠൡൢൣ൹", "ଁଂଃଅଆଇଈଉଊଋ\nଌ \r ଏଐ  ଓଔକଖଗଘଙଚ￿ଛଜଝଞ !ଟଠଡଢଣତ)(ଥଦ,ଧ.ନ0123456789:; ପଫ?ବଭମଯର ଲଳ ଵଶଷସହ଼ଽାିୀୁୂୃୄ  େୈ  ୋୌ୍ୖabcdefghijklmnopqrstuvwxyzୗୠୡୢୣ", "ਁਂਃਅਆਇਈਉਊ \n  \r ਏਐ  ਓਔਕਖਗਘਙਚ￿ਛਜਝਞ !ਟਠਡਢਣਤ)(ਥਦ,ਧ.ਨ0123456789:; ਪਫ?ਬਭਮਯਰ ਲਲ਼ ਵਸ਼ ਸਹ਼ ਾਿੀੁੂ    ੇੈ  ੋੌ੍ੑabcdefghijklmnopqrstuvwxyzੰੱੲੳੴ", " ஂஃஅஆஇஈஉஊ \n  \rஎஏஐ ஒஓஔக   ஙச￿ ஜ ஞ !ட   ணத)(  , .ந0123456789:;னப ?  மயரறலளழவஶஷஸஹ  ாிீுூ   ெேை ொோௌ்ௐabcdefghijklmnopqrstuvwxyzௗ௰௱௲௹", "ఁంఃఅఆఇఈఉఊఋ\nఌ \rఎఏఐ ఒఓఔకఖగఘఙచ￿ఛజఝఞ !టఠడఢణత)(థద,ధ.న0123456789:; పఫ?బభమయరఱలళ వశషసహ ఽాిీుూృౄ ెేై ొోౌ్ౕabcdefghijklmnopqrstuvwxyzౖౠౡౢౣ", "اآبٻڀپڦتۂٿ\nٹٽ\rٺټثجځڄڃڅچڇحخد￿ڌڈډڊ !ڏڍذرڑړ)(ڙز,ږ.ژ0123456789:;ښسش?صضطظعفقکڪګگڳڱلمنںڻڼوۄەہھءیېےٍُِٗٔabcdefghijklmnopqrstuvwxyzّٰٕٖٓ"};

    private static class LanguagePairCount {
        @UnsupportedAppUsage
        final int languageCode;
        @UnsupportedAppUsage
        final int[] septetCounts;
        @UnsupportedAppUsage
        final int[] unencodableCounts;

        @UnsupportedAppUsage
        LanguagePairCount(int code) {
            this.languageCode = code;
            int maxSingleShiftCode = GsmAlphabet.sHighestEnabledSingleShiftCode;
            this.septetCounts = new int[(maxSingleShiftCode + 1)];
            this.unencodableCounts = new int[(maxSingleShiftCode + 1)];
            int tableOffset = 0;
            for (int i = 1; i <= maxSingleShiftCode; i++) {
                if (GsmAlphabet.sEnabledSingleShiftTables[tableOffset] == i) {
                    tableOffset++;
                } else {
                    this.septetCounts[i] = -1;
                }
            }
            if (code == 1 && maxSingleShiftCode >= 1) {
                this.septetCounts[1] = -1;
            } else if (code == 3 && maxSingleShiftCode >= 2) {
                this.septetCounts[2] = -1;
            }
        }
    }

    public static class TextEncodingDetails {
        @UnsupportedAppUsage
        public int codeUnitCount;
        @UnsupportedAppUsage
        public int codeUnitSize;
        @UnsupportedAppUsage
        public int codeUnitsRemaining;
        @UnsupportedAppUsage
        public int languageShiftTable;
        @UnsupportedAppUsage
        public int languageTable;
        @UnsupportedAppUsage
        public int msgCount;

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("TextEncodingDetails { msgCount=");
            stringBuilder.append(this.msgCount);
            stringBuilder.append(", codeUnitCount=");
            stringBuilder.append(this.codeUnitCount);
            stringBuilder.append(", codeUnitsRemaining=");
            stringBuilder.append(this.codeUnitsRemaining);
            stringBuilder.append(", codeUnitSize=");
            stringBuilder.append(this.codeUnitSize);
            stringBuilder.append(", languageTable=");
            stringBuilder.append(this.languageTable);
            stringBuilder.append(", languageShiftTable=");
            stringBuilder.append(this.languageShiftTable);
            stringBuilder.append(" }");
            return stringBuilder.toString();
        }
    }

    private GsmAlphabet() {
    }

    @UnsupportedAppUsage
    public static int charToGsm(char c) {
        int i = 0;
        try {
            i = charToGsm(c, false);
            return i;
        } catch (EncodeException e) {
            return sCharsToGsmTables[i].get(32, 32);
        }
    }

    @UnsupportedAppUsage
    public static int charToGsm(char c, boolean throwException) throws EncodeException {
        int ret = sCharsToGsmTables[0].get(c, -1);
        if (ret != -1) {
            return ret;
        }
        if (sCharsToShiftTables[0].get(c, -1) != -1) {
            return 27;
        }
        if (!throwException) {
            return sCharsToGsmTables[0].get(32, 32);
        }
        throw new EncodeException(c);
    }

    public static int charToGsmExtended(char c) {
        int ret = sCharsToShiftTables[0].get(c, -1);
        if (ret == -1) {
            return sCharsToGsmTables[0].get(32, 32);
        }
        return ret;
    }

    @UnsupportedAppUsage
    public static char gsmToChar(int gsmChar) {
        if (gsmChar < 0 || gsmChar >= 128) {
            return ' ';
        }
        return sLanguageTables[0].charAt(gsmChar);
    }

    public static char gsmExtendedToChar(int gsmChar) {
        if (gsmChar == 27 || gsmChar < 0 || gsmChar >= 128) {
            return ' ';
        }
        char c = sLanguageShiftTables[0].charAt(gsmChar);
        if (c == ' ') {
            return sLanguageTables[0].charAt(gsmChar);
        }
        return c;
    }

    public static byte[] stringToGsm7BitPackedWithHeader(String data, byte[] header) throws EncodeException {
        return stringToGsm7BitPackedWithHeader(data, header, 0, 0);
    }

    @UnsupportedAppUsage
    public static byte[] stringToGsm7BitPackedWithHeader(String data, byte[] header, int languageTable, int languageShiftTable) throws EncodeException {
        if (header == null || header.length == 0) {
            return stringToGsm7BitPacked(data, languageTable, languageShiftTable);
        }
        byte[] ret = stringToGsm7BitPacked(data, (((header.length + 1) * 8) + 6) / 7, true, languageTable, languageShiftTable);
        ret[1] = (byte) header.length;
        System.arraycopy(header, 0, ret, 2, header.length);
        return ret;
    }

    @UnsupportedAppUsage
    public static byte[] stringToGsm7BitPacked(String data) throws EncodeException {
        return stringToGsm7BitPacked(data, 0, true, 0, 0);
    }

    public static byte[] stringToGsm7BitPacked(String data, int languageTable, int languageShiftTable) throws EncodeException {
        return stringToGsm7BitPacked(data, 0, true, languageTable, languageShiftTable);
    }

    @UnsupportedAppUsage
    public static byte[] stringToGsm7BitPacked(String data, int startingSeptetOffset, boolean throwException, int languageTable, int languageShiftTable) throws EncodeException {
        String str = data;
        int i = languageTable;
        int i2 = languageShiftTable;
        int dataLen = data.length();
        int septetCount = countGsmSeptetsUsingTables(str, throwException ^ 1, i, i2);
        int i3 = -1;
        if (septetCount != -1) {
            septetCount += startingSeptetOffset;
            if (septetCount <= 255) {
                byte[] ret = new byte[((((septetCount * 7) + 7) / 8) + 1)];
                SparseIntArray charToLanguageTable = sCharsToGsmTables[i];
                SparseIntArray charToShiftTable = sCharsToShiftTables[i2];
                int i4 = 0;
                int septets = startingSeptetOffset;
                int bitOffset = startingSeptetOffset * 7;
                while (i4 < dataLen && septets < septetCount) {
                    char c = str.charAt(i4);
                    int v = charToLanguageTable.get(c, i3);
                    if (v == i3) {
                        v = charToShiftTable.get(c, i3);
                        if (v != i3) {
                            packSmsChar(ret, bitOffset, 27);
                            bitOffset += 7;
                            septets++;
                        } else if (throwException) {
                            throw new EncodeException("stringToGsm7BitPacked(): unencodable char");
                        } else {
                            v = charToLanguageTable.get(32, 32);
                        }
                    }
                    packSmsChar(ret, bitOffset, v);
                    septets++;
                    i4++;
                    bitOffset += 7;
                    i3 = -1;
                }
                ret[0] = (byte) septetCount;
                return ret;
            }
            throw new EncodeException("Payload cannot exceed 255 septets", 1);
        }
        throw new EncodeException("countGsmSeptetsUsingTables(): unencodable char");
    }

    @UnsupportedAppUsage
    private static void packSmsChar(byte[] packedChars, int bitOffset, int value) {
        int shift = bitOffset % 8;
        int byteOffset = (bitOffset / 8) + 1;
        packedChars[byteOffset] = (byte) (packedChars[byteOffset] | (value << shift));
        if (shift > 1) {
            packedChars[byteOffset + 1] = (byte) (value >> (8 - shift));
        }
    }

    @UnsupportedAppUsage
    public static String gsm7BitPackedToString(byte[] pdu, int offset, int lengthSeptets) {
        return gsm7BitPackedToString(pdu, offset, lengthSeptets, 0, 0, 0);
    }

    @UnsupportedAppUsage
    public static String gsm7BitPackedToString(byte[] pdu, int offset, int lengthSeptets, int numPaddingBits, int languageTable, int shiftTable) {
        int languageTable2;
        int i = lengthSeptets;
        int i2 = languageTable;
        int shiftTable2 = shiftTable;
        StringBuilder ret = new StringBuilder(i);
        String str = ", using default";
        String str2 = TAG;
        if (i2 < 0 || i2 > sLanguageTables.length) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("unknown language table ");
            stringBuilder.append(i2);
            stringBuilder.append(str);
            Rlog.w(str2, stringBuilder.toString());
            languageTable2 = 0;
        } else {
            languageTable2 = i2;
        }
        if (shiftTable2 < 0 || shiftTable2 > sLanguageShiftTables.length) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("unknown single shift table ");
            stringBuilder2.append(shiftTable2);
            stringBuilder2.append(str);
            Rlog.w(str2, stringBuilder2.toString());
            shiftTable2 = 0;
        }
        boolean prevCharWasEscape = false;
        try {
            StringBuilder stringBuilder3;
            String languageTableToChar = sLanguageTables[languageTable2];
            String shiftTableToChar = sLanguageShiftTables[shiftTable2];
            if (languageTableToChar.isEmpty()) {
                stringBuilder3 = new StringBuilder();
                stringBuilder3.append("no language table for code ");
                stringBuilder3.append(languageTable2);
                stringBuilder3.append(str);
                Rlog.w(str2, stringBuilder3.toString());
                languageTableToChar = sLanguageTables[0];
            }
            if (shiftTableToChar.isEmpty()) {
                stringBuilder3 = new StringBuilder();
                stringBuilder3.append("no single shift table for code ");
                stringBuilder3.append(shiftTable2);
                stringBuilder3.append(str);
                Rlog.w(str2, stringBuilder3.toString());
                shiftTableToChar = sLanguageShiftTables[0];
            }
            for (int i3 = 0; i3 < i; i3++) {
                int bitOffset = (i3 * 7) + numPaddingBits;
                int byteOffset = bitOffset / 8;
                int shift = bitOffset % 8;
                int gsmVal = (pdu[offset + byteOffset] >> shift) & 127;
                if (shift > 1) {
                    gsmVal = (gsmVal & (127 >> (shift - 1))) | (127 & (pdu[(offset + byteOffset) + 1] << (8 - shift)));
                }
                if (prevCharWasEscape) {
                    if (gsmVal == 27) {
                        ret.append(' ');
                    } else {
                        char c = shiftTableToChar.charAt(gsmVal);
                        if (c == ' ') {
                            ret.append(languageTableToChar.charAt(gsmVal));
                        } else {
                            ret.append(c);
                        }
                    }
                    prevCharWasEscape = false;
                } else if (gsmVal == 27) {
                    prevCharWasEscape = true;
                } else {
                    ret.append(languageTableToChar.charAt(gsmVal));
                }
            }
            return ret.toString();
        } catch (RuntimeException ex) {
            Rlog.e(str2, "Error GSM 7 bit packed: ", ex);
            return null;
        }
    }

    @UnsupportedAppUsage
    public static String gsm8BitUnpackedToString(byte[] data, int offset, int length) {
        return gsm8BitUnpackedToString(data, offset, length, "");
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public static String gsm8BitUnpackedToString(byte[] data, int offset, int length, String characterset) {
        byte[] bArr = data;
        int i = length;
        boolean isMbcs = false;
        Charset charset = null;
        ByteBuffer mbcsBuffer = null;
        if (TextUtils.isEmpty(characterset)) {
            String str = characterset;
        } else {
            if (!characterset.equalsIgnoreCase("us-ascii") && Charset.isSupported(characterset)) {
                isMbcs = true;
                charset = Charset.forName(characterset);
                mbcsBuffer = ByteBuffer.allocate(2);
            }
        }
        String languageTableToChar = sLanguageTables[0];
        String shiftTableToChar = sLanguageShiftTables[0];
        StringBuilder ret = new StringBuilder(i);
        boolean prevWasEscape = false;
        int i2 = offset;
        while (i2 < offset + i) {
            int c = bArr[i2] & 255;
            if (c == 255) {
                break;
            }
            if (c != 27) {
                if (prevWasEscape) {
                    char shiftChar = c < shiftTableToChar.length() ? shiftTableToChar.charAt(c) : ' ';
                    if (shiftChar != ' ') {
                        ret.append(shiftChar);
                    } else if (c < languageTableToChar.length()) {
                        ret.append(languageTableToChar.charAt(c));
                    } else {
                        ret.append(' ');
                    }
                } else if (isMbcs && c >= 128 && i2 + 1 < offset + i) {
                    mbcsBuffer.clear();
                    int i3 = i2 + 1;
                    mbcsBuffer.put(bArr, i2, 2);
                    mbcsBuffer.flip();
                    ret.append(charset.decode(mbcsBuffer).toString());
                    i2 = i3;
                } else if (c < languageTableToChar.length()) {
                    ret.append(languageTableToChar.charAt(c));
                } else {
                    ret.append(' ');
                }
                prevWasEscape = false;
            } else if (prevWasEscape) {
                ret.append(' ');
                prevWasEscape = false;
            } else {
                prevWasEscape = true;
            }
            i2++;
        }
        return ret.toString();
    }

    @UnsupportedAppUsage
    public static byte[] stringToGsm8BitPacked(String s) {
        byte[] ret = new byte[countGsmSeptetsUsingTables(s, 1, 0, 0)];
        stringToGsm8BitUnpackedField(s, ret, 0, ret.length);
        return ret;
    }

    public static void stringToGsm8BitUnpackedField(String s, byte[] dest, int offset, int length) {
        int outByteIndex = offset;
        SparseIntArray charToLanguageTable = sCharsToGsmTables[0];
        SparseIntArray charToShiftTable = sCharsToShiftTables[0];
        int i = 0;
        int sz = s.length();
        while (i < sz && outByteIndex - offset < length) {
            int outByteIndex2;
            char c = s.charAt(i);
            int v = charToLanguageTable.get(c, -1);
            if (v == -1) {
                v = charToShiftTable.get(c, -1);
                if (v == -1) {
                    v = charToLanguageTable.get(32, 32);
                } else if ((outByteIndex + 1) - offset >= length) {
                    break;
                } else {
                    outByteIndex2 = outByteIndex + 1;
                    dest[outByteIndex] = GSM_EXTENDED_ESCAPE;
                    outByteIndex = outByteIndex2;
                }
            }
            outByteIndex2 = outByteIndex + 1;
            dest[outByteIndex] = (byte) v;
            i++;
            outByteIndex = outByteIndex2;
        }
        while (outByteIndex - offset < length) {
            i = outByteIndex + 1;
            dest[outByteIndex] = (byte) -1;
            outByteIndex = i;
        }
    }

    public static int countGsmSeptets(char c) {
        int i = 0;
        try {
            i = countGsmSeptets(c, false);
            return i;
        } catch (EncodeException e) {
            return i;
        }
    }

    @UnsupportedAppUsage
    public static int countGsmSeptets(char c, boolean throwsException) throws EncodeException {
        if (sCharsToGsmTables[0].get(c, -1) != -1) {
            return 1;
        }
        if (sCharsToShiftTables[0].get(c, -1) != -1) {
            return 2;
        }
        if (!throwsException) {
            return 1;
        }
        throw new EncodeException(c);
    }

    public static boolean isGsmSeptets(char c) {
        return (sCharsToGsmTables[0].get(c, -1) == -1 && sCharsToShiftTables[0].get(c, -1) == -1) ? false : true;
    }

    public static int countGsmSeptetsUsingTables(CharSequence s, boolean use7bitOnly, int languageTable, int languageShiftTable) {
        int count = 0;
        int sz = s.length();
        SparseIntArray charToLanguageTable = sCharsToGsmTables[languageTable];
        SparseIntArray charToShiftTable = sCharsToShiftTables[languageShiftTable];
        for (int i = 0; i < sz; i++) {
            char c = s.charAt(i);
            if (c == 27) {
                Rlog.w(TAG, "countGsmSeptets() string contains Escape character, skipping.");
            } else if (charToLanguageTable.get(c, -1) != -1) {
                count++;
            } else if (charToShiftTable.get(c, -1) != -1) {
                count += 2;
            } else if (!use7bitOnly) {
                return -1;
            } else {
                count++;
            }
        }
        return count;
    }

    public static TextEncodingDetails countGsmSeptets(CharSequence s, boolean use7bitOnly) {
        CharSequence charSequence = s;
        boolean z = use7bitOnly;
        if (!sDisableCountryEncodingCheck) {
            enableCountrySpecificEncodings();
        }
        int length = sEnabledSingleShiftTables.length;
        int[] iArr = sEnabledLockingShiftTables;
        int i = 0;
        int i2 = 160;
        int i3 = -1;
        if (length + iArr.length == 0) {
            TextEncodingDetails ted = new TextEncodingDetails();
            int septets = countGsmSeptetsUsingTables(charSequence, z, 0, 0);
            if (septets == -1) {
                return null;
            }
            ted.codeUnitSize = 1;
            ted.codeUnitCount = septets;
            if (septets > 160) {
                ted.msgCount = (septets + 152) / 153;
                ted.codeUnitsRemaining = (ted.msgCount * 153) - septets;
            } else {
                ted.msgCount = 1;
                ted.codeUnitsRemaining = 160 - septets;
            }
            return ted;
        }
        int table;
        length = sHighestEnabledSingleShiftCode;
        ArrayList<LanguagePairCount> lpcList = new ArrayList(iArr.length + 1);
        lpcList.add(new LanguagePairCount(0));
        int[] iArr2 = sEnabledLockingShiftTables;
        int length2 = iArr2.length;
        while (i < length2) {
            int i4 = iArr2[i];
            if (!(i4 == 0 || sLanguageTables[i4].isEmpty())) {
                lpcList.add(new LanguagePairCount(i4));
            }
            i++;
        }
        i = s.length();
        for (int i5 = 0; i5 < i && !lpcList.isEmpty(); i5++) {
            char c = charSequence.charAt(i5);
            if (c == 27) {
                Rlog.w(TAG, "countGsmSeptets() string contains Escape character, ignoring!");
            } else {
                for (LanguagePairCount lpc : lpcList) {
                    if (sCharsToGsmTables[lpc.languageCode].get(c, -1) == -1) {
                        for (table = 0; table <= length; table++) {
                            if (lpc.septetCounts[table] != -1) {
                                int[] iArr3;
                                if (sCharsToShiftTables[table].get(c, -1) != -1) {
                                    iArr3 = lpc.septetCounts;
                                    iArr3[table] = iArr3[table] + 2;
                                } else if (z) {
                                    iArr3 = lpc.septetCounts;
                                    iArr3[table] = iArr3[table] + 1;
                                    iArr3 = lpc.unencodableCounts;
                                    iArr3[table] = iArr3[table] + 1;
                                } else {
                                    lpc.septetCounts[table] = -1;
                                }
                            }
                        }
                    } else {
                        for (int table2 = 0; table2 <= length; table2++) {
                            if (lpc.septetCounts[table2] != -1) {
                                int[] iArr4 = lpc.septetCounts;
                                iArr4[table2] = iArr4[table2] + 1;
                            }
                        }
                    }
                }
            }
        }
        TextEncodingDetails ted2 = new TextEncodingDetails();
        ted2.msgCount = Integer.MAX_VALUE;
        ted2.codeUnitSize = 1;
        length2 = Integer.MAX_VALUE;
        for (LanguagePairCount lpc2 : lpcList) {
            int shiftTable = 0;
            while (shiftTable <= length) {
                table = lpc2.septetCounts[shiftTable];
                if (table != i3) {
                    int udhLength;
                    if (lpc2.languageCode != 0 && shiftTable != 0) {
                        udhLength = 8;
                    } else if (lpc2.languageCode == 0 && shiftTable == 0) {
                        udhLength = 0;
                    } else {
                        udhLength = 5;
                    }
                    if (table + udhLength > i2) {
                        if (udhLength == 0) {
                            udhLength = 1;
                        }
                        i3 = 160 - (udhLength + 6);
                        int msgCount = ((table + i3) - 1) / i3;
                        i3 = msgCount;
                        i2 = (msgCount * i3) - table;
                    } else {
                        i3 = 1;
                        i2 = (160 - udhLength) - table;
                    }
                    int unencodableCount = lpc2.unencodableCounts[shiftTable];
                    if ((!z || unencodableCount <= minUnencodableCount) && ((z && unencodableCount < minUnencodableCount) || i3 < ted2.msgCount || (i3 == ted2.msgCount && i2 > ted2.codeUnitsRemaining))) {
                        length2 = unencodableCount;
                        ted2.msgCount = i3;
                        ted2.codeUnitCount = table;
                        ted2.codeUnitsRemaining = i2;
                        ted2.languageTable = lpc2.languageCode;
                        ted2.languageShiftTable = shiftTable;
                    }
                }
                shiftTable++;
                i2 = 160;
                i3 = -1;
            }
            i2 = 160;
            i3 = -1;
        }
        if (ted2.msgCount == Integer.MAX_VALUE) {
            return null;
        }
        return ted2;
    }

    @UnsupportedAppUsage
    public static int findGsmSeptetLimitIndex(String s, int start, int limit, int langTable, int langShiftTable) {
        int accumulator = 0;
        int size = s.length();
        SparseIntArray charToLangTable = sCharsToGsmTables[langTable];
        SparseIntArray charToLangShiftTable = sCharsToShiftTables[langShiftTable];
        for (int i = start; i < size; i++) {
            if (charToLangTable.get(s.charAt(i), -1) != -1) {
                accumulator++;
            } else if (charToLangShiftTable.get(s.charAt(i), -1) == -1) {
                accumulator++;
            } else {
                accumulator += 2;
            }
            if (accumulator > limit) {
                return i;
            }
        }
        return size;
    }

    public static synchronized void setEnabledSingleShiftTables(int[] tables) {
        synchronized (GsmAlphabet.class) {
            sEnabledSingleShiftTables = tables;
            sDisableCountryEncodingCheck = true;
            if (tables.length > 0) {
                sHighestEnabledSingleShiftCode = tables[tables.length - 1];
            } else {
                sHighestEnabledSingleShiftCode = 0;
            }
        }
    }

    public static synchronized void setEnabledLockingShiftTables(int[] tables) {
        synchronized (GsmAlphabet.class) {
            sEnabledLockingShiftTables = tables;
            sDisableCountryEncodingCheck = true;
        }
    }

    public static synchronized int[] getEnabledSingleShiftTables() {
        int[] iArr;
        synchronized (GsmAlphabet.class) {
            iArr = sEnabledSingleShiftTables;
        }
        return iArr;
    }

    public static synchronized int[] getEnabledLockingShiftTables() {
        int[] iArr;
        synchronized (GsmAlphabet.class) {
            iArr = sEnabledLockingShiftTables;
        }
        return iArr;
    }

    private static void enableCountrySpecificEncodings() {
        Resources r = Resources.getSystem();
        sEnabledSingleShiftTables = r.getIntArray(R.array.config_sms_enabled_single_shift_tables);
        sEnabledLockingShiftTables = r.getIntArray(R.array.config_sms_enabled_locking_shift_tables);
        int[] iArr = sEnabledSingleShiftTables;
        if (iArr.length > 0) {
            sHighestEnabledSingleShiftCode = iArr[iArr.length - 1];
        } else {
            sHighestEnabledSingleShiftCode = 0;
        }
    }

    static {
        String str;
        String str2;
        String table;
        int tableLen;
        enableCountrySpecificEncodings();
        int numTables = sLanguageTables.length;
        int numShiftTables = sLanguageShiftTables.length;
        String str3 = TAG;
        if (numTables != numShiftTables) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error: language tables array length ");
            stringBuilder.append(numTables);
            stringBuilder.append(" != shift tables array length ");
            stringBuilder.append(numShiftTables);
            Rlog.e(str3, stringBuilder.toString());
        }
        sCharsToGsmTables = new SparseIntArray[numTables];
        int i = 0;
        while (true) {
            str = " (expected 128 or 0)";
            str2 = " length ";
            if (i >= numTables) {
                break;
            }
            table = sLanguageTables[i];
            tableLen = table.length();
            if (!(tableLen == 0 || tableLen == 128)) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Error: language tables index ");
                stringBuilder2.append(i);
                stringBuilder2.append(str2);
                stringBuilder2.append(tableLen);
                stringBuilder2.append(str);
                Rlog.e(str3, stringBuilder2.toString());
            }
            SparseIntArray charToGsmTable = new SparseIntArray(tableLen);
            sCharsToGsmTables[i] = charToGsmTable;
            for (int j = 0; j < tableLen; j++) {
                charToGsmTable.put(table.charAt(j), j);
            }
            i++;
        }
        sCharsToShiftTables = new SparseIntArray[numTables];
        for (i = 0; i < numShiftTables; i++) {
            table = sLanguageShiftTables[i];
            tableLen = table.length();
            if (!(tableLen == 0 || tableLen == 128)) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("Error: language shift tables index ");
                stringBuilder3.append(i);
                stringBuilder3.append(str2);
                stringBuilder3.append(tableLen);
                stringBuilder3.append(str);
                Rlog.e(str3, stringBuilder3.toString());
            }
            SparseIntArray charToShiftTable = new SparseIntArray(tableLen);
            sCharsToShiftTables[i] = charToShiftTable;
            for (int j2 = 0; j2 < tableLen; j2++) {
                char c = table.charAt(j2);
                if (c != ' ') {
                    charToShiftTable.put(c, j2);
                }
            }
        }
    }
}
