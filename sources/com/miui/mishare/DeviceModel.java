package com.miui.mishare;

import android.content.Context;
import android.miui.R;

public class DeviceModel {
    public static final byte MANUFACTURE_UNDEFINED = (byte) 0;

    public static class Oppo {
        public static final byte MANUFACTURE_END = (byte) 19;
        public static final byte MANUFACTURE_OPPO = (byte) 10;
        public static final byte MANUFACTURE_REALME = (byte) 11;
        public static final byte MANUFACTURE_START = (byte) 10;
    }

    public static class Vivo {
        public static final byte MANUFACTURE_END = (byte) 29;
        public static final byte MANUFACTURE_START = (byte) 20;
    }

    public static class Xiaomi {
        public static final byte MANUFACTURE_END = (byte) 39;
        public static final byte MANUFACTURE_REDMI = (byte) 31;
        public static final byte MANUFACTURE_START = (byte) 30;
        public static final byte MANUFACTURE_XIAOMI = (byte) 30;
        public static final int MI_9T = 8193;
        public static final int MI_9T_PRO = 8194;
        public static final int MI_A3 = 8195;
        public static final int REDMI_6 = 4099;
        public static final int REDMI_6A = 4100;
        public static final int REDMI_6_PRO = 4101;
        public static final int REDMI_7A = 4102;
        public static final int REDMI_K20 = 4104;
        public static final int REDMI_K20_PRO = 4105;
        public static final int REDMI_NOTE_5 = 4103;
        public static final int REDMI_NOTE_7 = 4097;
        public static final int REDMI_NOTE_7_PRO = 4098;
        public static final int UNDEFINED = 65535;
        public static final int XIAOMI_6 = 8;
        public static final int XIAOMI_8 = 3;
        public static final int XIAOMI_8_EXP = 7;
        public static final int XIAOMI_8_SE = 4;
        public static final int XIAOMI_8_UD = 5;
        public static final int XIAOMI_8_YOUNG = 6;
        public static final int XIAOMI_9 = 1;
        public static final int XIAOMI_9_SE = 2;
        public static final int XIAOMI_CC_9 = 513;
        public static final int XIAOMI_CC_9E = 514;
        public static final int XIAOMI_CC_9_CUSTOM = 515;
        public static final int XIAOMI_MAX_3 = 3842;
        public static final int XIAOMI_MIX_2 = 257;
        public static final int XIAOMI_MIX_2S = 258;
        public static final int XIAOMI_MIX_3 = 259;
        public static final int XIAOMI_MIX_3_5G = 260;
        public static final int XIAOMI_NOTE_3 = 3843;
        public static final int XIAOMI_POCO_F1 = 3841;
    }

    public static byte myManufactureCode() {
        /*
        r0 = android.os.Build.DEVICE;
        r1 = r0.hashCode();
        switch(r1) {
            case -1386609209: goto L_0x0167;
            case -1368035283: goto L_0x015c;
            case -1363908749: goto L_0x0151;
            case -1361394003: goto L_0x0146;
            case -1331635022: goto L_0x013c;
            case -1291320481: goto L_0x0130;
            case -1109838608: goto L_0x0125;
            case -909729689: goto L_0x0119;
            case -902144405: goto L_0x010e;
            case -816343937: goto L_0x0101;
            case -788334647: goto L_0x00f4;
            case -678450419: goto L_0x00e7;
            case -493712819: goto L_0x00db;
            case -398224152: goto L_0x00ce;
            case 3181769: goto L_0x00c3;
            case 3441008: goto L_0x00b6;
            case 3598763: goto L_0x00aa;
            case 3615844: goto L_0x009d;
            case 100897019: goto L_0x0091;
            case 107157753: goto L_0x0084;
            case 109196996: goto L_0x0078;
            case 556700599: goto L_0x006c;
            case 563886458: goto L_0x005f;
            case 599866403: goto L_0x0054;
            case 666728873: goto L_0x0049;
            case 979861249: goto L_0x003c;
            case 1048825830: goto L_0x002f;
            case 1131821161: goto L_0x0023;
            case 1445294948: goto L_0x0017;
            case 1654011785: goto L_0x000b;
            default: goto L_0x0009;
        };
    L_0x0009:
        goto L_0x0172;
    L_0x000b:
        r1 = "davinciin";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x0013:
        r0 = 27;
        goto L_0x0173;
    L_0x0017:
        r1 = "davinci";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x001f:
        r0 = 26;
        goto L_0x0173;
    L_0x0023:
        r1 = "andromeda";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x002b:
        r0 = 11;
        goto L_0x0173;
    L_0x002f:
        r1 = "raphaelin";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x0038:
        r0 = 29;
        goto L_0x0173;
    L_0x003c:
        r1 = "raphael";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x0045:
        r0 = 28;
        goto L_0x0173;
    L_0x0049:
        r1 = "cepheus";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x0051:
        r0 = 0;
        goto L_0x0173;
    L_0x0054:
        r1 = "equuleus";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x005c:
        r0 = 4;
        goto L_0x0173;
    L_0x005f:
        r1 = "nitrogen";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x0068:
        r0 = 16;
        goto L_0x0173;
    L_0x006c:
        r1 = "beryllium";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x0074:
        r0 = 15;
        goto L_0x0173;
    L_0x0078:
        r1 = "sagit";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x0081:
        r0 = 7;
        goto L_0x0173;
    L_0x0084:
        r1 = "pyxis";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x008d:
        r0 = 12;
        goto L_0x0173;
    L_0x0091:
        r1 = "jason";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x0099:
        r0 = 17;
        goto L_0x0173;
    L_0x009d:
        r1 = "vela";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x00a6:
        r0 = 13;
        goto L_0x0173;
    L_0x00aa:
        r1 = "ursa";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x00b3:
        r0 = 6;
        goto L_0x0173;
    L_0x00b6:
        r1 = "pine";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x00bf:
        r0 = 24;
        goto L_0x0173;
    L_0x00c3:
        r1 = "grus";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x00cb:
        r0 = 1;
        goto L_0x0173;
    L_0x00ce:
        r1 = "polaris";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x00d7:
        r0 = 9;
        goto L_0x0173;
    L_0x00db:
        r1 = "platina";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x00e4:
        r0 = 5;
        goto L_0x0173;
    L_0x00e7:
        r1 = "perseus";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x00f0:
        r0 = 10;
        goto L_0x0173;
    L_0x00f4:
        r1 = "whyred";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x00fd:
        r0 = 25;
        goto L_0x0173;
    L_0x0101:
        r1 = "violet";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x010a:
        r0 = 19;
        goto L_0x0173;
    L_0x010e:
        r1 = "sirius";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x0117:
        r0 = 3;
        goto L_0x0173;
    L_0x0119:
        r1 = "sakura";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x0122:
        r0 = 22;
        goto L_0x0173;
    L_0x0125:
        r1 = "laurus";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x012d:
        r0 = 14;
        goto L_0x0173;
    L_0x0130:
        r1 = "sakura_india";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x0139:
        r0 = 23;
        goto L_0x0173;
    L_0x013c:
        r1 = "dipper";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x0144:
        r0 = 2;
        goto L_0x0173;
    L_0x0146:
        r1 = "chiron";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x014e:
        r0 = 8;
        goto L_0x0173;
    L_0x0151:
        r1 = "cereus";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x0159:
        r0 = 20;
        goto L_0x0173;
    L_0x015c:
        r1 = "cactus";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x0164:
        r0 = 21;
        goto L_0x0173;
    L_0x0167:
        r1 = "lavender";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x016f:
        r0 = 18;
        goto L_0x0173;
    L_0x0172:
        r0 = -1;
    L_0x0173:
        r1 = 30;
        switch(r0) {
            case 0: goto L_0x017c;
            case 1: goto L_0x017c;
            case 2: goto L_0x017c;
            case 3: goto L_0x017c;
            case 4: goto L_0x017c;
            case 5: goto L_0x017c;
            case 6: goto L_0x017c;
            case 7: goto L_0x017c;
            case 8: goto L_0x017c;
            case 9: goto L_0x017c;
            case 10: goto L_0x017c;
            case 11: goto L_0x017c;
            case 12: goto L_0x017c;
            case 13: goto L_0x017c;
            case 14: goto L_0x017c;
            case 15: goto L_0x017c;
            case 16: goto L_0x017c;
            case 17: goto L_0x017c;
            case 18: goto L_0x0179;
            case 19: goto L_0x0179;
            case 20: goto L_0x0179;
            case 21: goto L_0x0179;
            case 22: goto L_0x0179;
            case 23: goto L_0x0179;
            case 24: goto L_0x0179;
            case 25: goto L_0x0179;
            case 26: goto L_0x0179;
            case 27: goto L_0x0179;
            case 28: goto L_0x0179;
            case 29: goto L_0x0179;
            default: goto L_0x0178;
        };
    L_0x0178:
        return r1;
    L_0x0179:
        r0 = 31;
        return r0;
    L_0x017c:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.mishare.DeviceModel.myManufactureCode():byte");
    }

    public static int myDeviceCode() {
        /*
        r0 = android.os.Build.DEVICE;
        r1 = r0.hashCode();
        r2 = 8;
        r3 = 7;
        r4 = 6;
        r5 = 5;
        r6 = 4;
        r7 = 3;
        r8 = 2;
        r9 = 1;
        switch(r1) {
            case -1386609209: goto L_0x016f;
            case -1368035283: goto L_0x0164;
            case -1363908749: goto L_0x0159;
            case -1361394003: goto L_0x014f;
            case -1331635022: goto L_0x0145;
            case -1291320481: goto L_0x0139;
            case -1109838608: goto L_0x012e;
            case -909729689: goto L_0x0122;
            case -902144405: goto L_0x0117;
            case -816343937: goto L_0x010a;
            case -788334647: goto L_0x00fd;
            case -678450419: goto L_0x00f0;
            case -493712819: goto L_0x00e4;
            case -398224152: goto L_0x00d7;
            case 3181769: goto L_0x00cc;
            case 3441008: goto L_0x00bf;
            case 3598763: goto L_0x00b3;
            case 3615844: goto L_0x00a6;
            case 100897019: goto L_0x009a;
            case 107157753: goto L_0x008d;
            case 109196996: goto L_0x0081;
            case 556700599: goto L_0x0075;
            case 563886458: goto L_0x0068;
            case 599866403: goto L_0x005d;
            case 666728873: goto L_0x0052;
            case 979861249: goto L_0x0045;
            case 1048825830: goto L_0x0038;
            case 1131821161: goto L_0x002c;
            case 1445294948: goto L_0x0020;
            case 1654011785: goto L_0x0014;
            default: goto L_0x0012;
        };
    L_0x0012:
        goto L_0x017a;
    L_0x0014:
        r1 = "davinciin";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x001c:
        r0 = 27;
        goto L_0x017b;
    L_0x0020:
        r1 = "davinci";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x0028:
        r0 = 26;
        goto L_0x017b;
    L_0x002c:
        r1 = "andromeda";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x0034:
        r0 = 11;
        goto L_0x017b;
    L_0x0038:
        r1 = "raphaelin";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x0041:
        r0 = 29;
        goto L_0x017b;
    L_0x0045:
        r1 = "raphael";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x004e:
        r0 = 28;
        goto L_0x017b;
    L_0x0052:
        r1 = "cepheus";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x005a:
        r0 = 0;
        goto L_0x017b;
    L_0x005d:
        r1 = "equuleus";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x0065:
        r0 = r6;
        goto L_0x017b;
    L_0x0068:
        r1 = "nitrogen";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x0071:
        r0 = 16;
        goto L_0x017b;
    L_0x0075:
        r1 = "beryllium";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x007d:
        r0 = 15;
        goto L_0x017b;
    L_0x0081:
        r1 = "sagit";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x008a:
        r0 = r3;
        goto L_0x017b;
    L_0x008d:
        r1 = "pyxis";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x0096:
        r0 = 12;
        goto L_0x017b;
    L_0x009a:
        r1 = "jason";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x00a2:
        r0 = 17;
        goto L_0x017b;
    L_0x00a6:
        r1 = "vela";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x00af:
        r0 = 13;
        goto L_0x017b;
    L_0x00b3:
        r1 = "ursa";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x00bc:
        r0 = r4;
        goto L_0x017b;
    L_0x00bf:
        r1 = "pine";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x00c8:
        r0 = 24;
        goto L_0x017b;
    L_0x00cc:
        r1 = "grus";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x00d4:
        r0 = r9;
        goto L_0x017b;
    L_0x00d7:
        r1 = "polaris";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x00e0:
        r0 = 9;
        goto L_0x017b;
    L_0x00e4:
        r1 = "platina";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x00ed:
        r0 = r5;
        goto L_0x017b;
    L_0x00f0:
        r1 = "perseus";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x00f9:
        r0 = 10;
        goto L_0x017b;
    L_0x00fd:
        r1 = "whyred";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x0106:
        r0 = 25;
        goto L_0x017b;
    L_0x010a:
        r1 = "violet";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x0113:
        r0 = 19;
        goto L_0x017b;
    L_0x0117:
        r1 = "sirius";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x0120:
        r0 = r7;
        goto L_0x017b;
    L_0x0122:
        r1 = "sakura";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x012b:
        r0 = 22;
        goto L_0x017b;
    L_0x012e:
        r1 = "laurus";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x0136:
        r0 = 14;
        goto L_0x017b;
    L_0x0139:
        r1 = "sakura_india";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x0142:
        r0 = 23;
        goto L_0x017b;
    L_0x0145:
        r1 = "dipper";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x014d:
        r0 = r8;
        goto L_0x017b;
    L_0x014f:
        r1 = "chiron";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x0157:
        r0 = r2;
        goto L_0x017b;
    L_0x0159:
        r1 = "cereus";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x0161:
        r0 = 20;
        goto L_0x017b;
    L_0x0164:
        r1 = "cactus";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x016c:
        r0 = 21;
        goto L_0x017b;
    L_0x016f:
        r1 = "lavender";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0012;
    L_0x0177:
        r0 = 18;
        goto L_0x017b;
    L_0x017a:
        r0 = -1;
    L_0x017b:
        r1 = 4105; // 0x1009 float:5.752E-42 double:2.028E-320;
        r10 = 4104; // 0x1008 float:5.751E-42 double:2.0276E-320;
        switch(r0) {
            case 0: goto L_0x01d9;
            case 1: goto L_0x01d8;
            case 2: goto L_0x01d7;
            case 3: goto L_0x01d6;
            case 4: goto L_0x01d5;
            case 5: goto L_0x01d4;
            case 6: goto L_0x01d3;
            case 7: goto L_0x01d2;
            case 8: goto L_0x01cf;
            case 9: goto L_0x01cc;
            case 10: goto L_0x01c9;
            case 11: goto L_0x01c6;
            case 12: goto L_0x01c3;
            case 13: goto L_0x01c0;
            case 14: goto L_0x01b6;
            case 15: goto L_0x01b3;
            case 16: goto L_0x01b0;
            case 17: goto L_0x01ad;
            case 18: goto L_0x01aa;
            case 19: goto L_0x01a7;
            case 20: goto L_0x01a4;
            case 21: goto L_0x01a1;
            case 22: goto L_0x019e;
            case 23: goto L_0x019e;
            case 24: goto L_0x019b;
            case 25: goto L_0x0198;
            case 26: goto L_0x0190;
            case 27: goto L_0x018f;
            case 28: goto L_0x0187;
            case 29: goto L_0x0186;
            default: goto L_0x0182;
        };
    L_0x0182:
        r0 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        return r0;
    L_0x0186:
        return r1;
    L_0x0187:
        r0 = miui.os.Build.IS_INTERNATIONAL_BUILD;
        if (r0 != 0) goto L_0x018c;
    L_0x018b:
        goto L_0x018e;
    L_0x018c:
        r1 = 8194; // 0x2002 float:1.1482E-41 double:4.0484E-320;
    L_0x018e:
        return r1;
    L_0x018f:
        return r10;
    L_0x0190:
        r0 = miui.os.Build.IS_INTERNATIONAL_BUILD;
        if (r0 != 0) goto L_0x0195;
    L_0x0194:
        goto L_0x0197;
    L_0x0195:
        r10 = 8193; // 0x2001 float:1.1481E-41 double:4.048E-320;
    L_0x0197:
        return r10;
    L_0x0198:
        r0 = 4103; // 0x1007 float:5.75E-42 double:2.027E-320;
        return r0;
    L_0x019b:
        r0 = 4102; // 0x1006 float:5.748E-42 double:2.0267E-320;
        return r0;
    L_0x019e:
        r0 = 4101; // 0x1005 float:5.747E-42 double:2.026E-320;
        return r0;
    L_0x01a1:
        r0 = 4100; // 0x1004 float:5.745E-42 double:2.0257E-320;
        return r0;
    L_0x01a4:
        r0 = 4099; // 0x1003 float:5.744E-42 double:2.025E-320;
        return r0;
    L_0x01a7:
        r0 = 4098; // 0x1002 float:5.743E-42 double:2.0247E-320;
        return r0;
    L_0x01aa:
        r0 = 4097; // 0x1001 float:5.741E-42 double:2.024E-320;
        return r0;
    L_0x01ad:
        r0 = 3843; // 0xf03 float:5.385E-42 double:1.8987E-320;
        return r0;
    L_0x01b0:
        r0 = 3842; // 0xf02 float:5.384E-42 double:1.898E-320;
        return r0;
    L_0x01b3:
        r0 = 3841; // 0xf01 float:5.382E-42 double:1.8977E-320;
        return r0;
    L_0x01b6:
        r0 = miui.os.Build.IS_INTERNATIONAL_BUILD;
        if (r0 != 0) goto L_0x01bd;
    L_0x01ba:
        r0 = 514; // 0x202 float:7.2E-43 double:2.54E-321;
        goto L_0x01bf;
    L_0x01bd:
        r0 = 8195; // 0x2003 float:1.1484E-41 double:4.049E-320;
    L_0x01bf:
        return r0;
    L_0x01c0:
        r0 = 515; // 0x203 float:7.22E-43 double:2.544E-321;
        return r0;
    L_0x01c3:
        r0 = 513; // 0x201 float:7.19E-43 double:2.535E-321;
        return r0;
    L_0x01c6:
        r0 = 260; // 0x104 float:3.64E-43 double:1.285E-321;
        return r0;
    L_0x01c9:
        r0 = 259; // 0x103 float:3.63E-43 double:1.28E-321;
        return r0;
    L_0x01cc:
        r0 = 258; // 0x102 float:3.62E-43 double:1.275E-321;
        return r0;
    L_0x01cf:
        r0 = 257; // 0x101 float:3.6E-43 double:1.27E-321;
        return r0;
    L_0x01d2:
        return r2;
    L_0x01d3:
        return r3;
    L_0x01d4:
        return r4;
    L_0x01d5:
        return r5;
    L_0x01d6:
        return r6;
    L_0x01d7:
        return r7;
    L_0x01d8:
        return r8;
    L_0x01d9:
        return r9;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.mishare.DeviceModel.myDeviceCode():int");
    }

    public static String getManufactureName(Context context, byte manufactureCode) {
        return context.getString(getManufactureRes(manufactureCode));
    }

    private static int getManufactureRes(byte manufactureCode) {
        if (manufactureCode >= Vivo.MANUFACTURE_START && manufactureCode <= Vivo.MANUFACTURE_END) {
            return R.string.vivo;
        }
        if (manufactureCode >= (byte) 10 && manufactureCode <= Oppo.MANUFACTURE_END) {
            return R.string.oppo;
        }
        if (manufactureCode < (byte) 30 || manufactureCode > Xiaomi.MANUFACTURE_END) {
            return R.string.unrecognized_manufacture;
        }
        return R.string.xiaomi;
    }

    public static String getDeviceName(Context context, byte manufactureCode, int deviceCode) {
        return context.getString(getDeviceRes(manufactureCode, deviceCode));
    }

    private static int getDeviceRes(byte manufactureCode, int deviceCode) {
        if (manufactureCode >= Vivo.MANUFACTURE_START && manufactureCode <= Vivo.MANUFACTURE_END) {
            return R.string.vivo_phone;
        }
        if (manufactureCode >= (byte) 10 && manufactureCode <= Oppo.MANUFACTURE_END) {
            return R.string.oppo_phone;
        }
        if (manufactureCode < (byte) 30 || manufactureCode > Xiaomi.MANUFACTURE_END) {
            return R.string.unrecognized_model;
        }
        switch (deviceCode) {
            case 1:
                return R.string.xiaomi_9;
            case 2:
                return R.string.xiaomi_9_se;
            case 3:
                return R.string.xiaomi_8;
            case 4:
                return R.string.xiaomi_8_se;
            case 5:
                return R.string.xiaomi_8_ud;
            case 6:
                return R.string.xiaomi_8_young;
            case 7:
                return R.string.xiaomi_8_exp;
            case 8:
                return R.string.xiaomi_6;
            default:
                switch (deviceCode) {
                    case 257:
                        return R.string.xiaomi_mix_2;
                    case 258:
                        return R.string.xiaomi_mix_2s;
                    case 259:
                        return R.string.xiaomi_mix_3;
                    case 260:
                        return R.string.xiaomi_mix_3_5g;
                    default:
                        switch (deviceCode) {
                            case 513:
                                return R.string.xiaomi_cc_9;
                            case 514:
                                return R.string.xiaomi_cc_9e;
                            case 515:
                                return R.string.xiaomi_cc_9_custom;
                            default:
                                switch (deviceCode) {
                                    case Xiaomi.XIAOMI_POCO_F1 /*3841*/:
                                        return R.string.xiaomi_poco_f1;
                                    case Xiaomi.XIAOMI_MAX_3 /*3842*/:
                                        return R.string.xiaomi_max_3;
                                    case Xiaomi.XIAOMI_NOTE_3 /*3843*/:
                                        return R.string.xiaomi_note_3;
                                    default:
                                        switch (deviceCode) {
                                            case 4097:
                                                return R.string.redmi_note_7;
                                            case 4098:
                                                return R.string.redmi_note_7_pro;
                                            case 4099:
                                                return R.string.redmi_6;
                                            case 4100:
                                                return R.string.redmi_6a;
                                            case 4101:
                                                return R.string.redmi_6_pro;
                                            case 4102:
                                                return R.string.redmi_7a;
                                            case 4103:
                                                return R.string.redmi_note_5;
                                            case 4104:
                                                return R.string.redmi_k20;
                                            case 4105:
                                                return R.string.redmi_k20_pro;
                                            default:
                                                switch (deviceCode) {
                                                    case 8193:
                                                        return R.string.mi_9t;
                                                    case 8194:
                                                        return R.string.mi_9t_pro;
                                                    case 8195:
                                                        return R.string.mi_a3;
                                                    default:
                                                        return R.string.xiaomi_phone;
                                                }
                                        }
                                }
                        }
                }
        }
    }
}
