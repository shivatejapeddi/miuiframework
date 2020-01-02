package com.android.internal.util;

import android.content.ContentResolver.MimeTypeInfo;
import android.content.res.Resources;
import android.graphics.drawable.Icon;
import android.text.TextUtils;
import android.util.ArrayMap;
import com.android.internal.R;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.widget.MessagingMessage;
import java.util.Locale;
import java.util.Objects;
import libcore.net.MimeUtils;

public class MimeIconUtils {
    @GuardedBy({"sCache"})
    private static final ArrayMap<String, MimeTypeInfo> sCache = new ArrayMap();

    private static MimeTypeInfo buildTypeInfo(String mimeType, int iconId, int labelId, int extLabelId) {
        CharSequence label;
        Resources res = Resources.getSystem();
        if (TextUtils.isEmpty(MimeUtils.guessExtensionFromMimeType(mimeType)) || extLabelId == -1) {
            label = res.getString(labelId);
        } else {
            label = res.getString(extLabelId, new Object[]{ext.toUpperCase(Locale.US)});
        }
        return new MimeTypeInfo(Icon.createWithResource(res, iconId), label, label);
    }

    private static android.content.ContentResolver.MimeTypeInfo buildTypeInfo(java.lang.String r10) {
        /*
        r0 = r10.hashCode();
        r1 = -1;
        switch(r0) {
            case -2135180893: goto L_0x0543;
            case -2135135086: goto L_0x0538;
            case -2035614749: goto L_0x052d;
            case -1988437312: goto L_0x0522;
            case -1917350260: goto L_0x0516;
            case -1883861089: goto L_0x050b;
            case -1808693885: goto L_0x04ff;
            case -1777056778: goto L_0x04f4;
            case -1747277413: goto L_0x04e9;
            case -1719571662: goto L_0x04dd;
            case -1628346451: goto L_0x04d1;
            case -1590813831: goto L_0x04c5;
            case -1506009513: goto L_0x04b9;
            case -1386165903: goto L_0x04ad;
            case -1348236371: goto L_0x04a1;
            case -1348228591: goto L_0x0495;
            case -1348228026: goto L_0x0489;
            case -1348228010: goto L_0x047d;
            case -1348221103: goto L_0x0471;
            case -1326989846: goto L_0x0465;
            case -1316922187: goto L_0x0459;
            case -1296467268: goto L_0x044d;
            case -1294595255: goto L_0x0442;
            case -1248334925: goto L_0x0436;
            case -1248333084: goto L_0x042a;
            case -1248326952: goto L_0x041e;
            case -1248325150: goto L_0x0412;
            case -1190438973: goto L_0x0406;
            case -1143717099: goto L_0x03fb;
            case -1082243251: goto L_0x03ee;
            case -1073633483: goto L_0x03e2;
            case -1071817359: goto L_0x03d6;
            case -1050893613: goto L_0x03ca;
            case -1033484950: goto L_0x03be;
            case -1004747231: goto L_0x03b1;
            case -1004727243: goto L_0x03a4;
            case -958424608: goto L_0x0397;
            case -951557661: goto L_0x038b;
            case -779959281: goto L_0x037f;
            case -779913474: goto L_0x0373;
            case -723118015: goto L_0x0367;
            case -676675015: goto L_0x035b;
            case -479218428: goto L_0x034f;
            case -427343476: goto L_0x0343;
            case -396757341: goto L_0x0337;
            case -366307023: goto L_0x032b;
            case -261480694: goto L_0x031e;
            case -261469704: goto L_0x0311;
            case -261439913: goto L_0x0304;
            case -261278343: goto L_0x02f7;
            case -228136375: goto L_0x02eb;
            case -221944004: goto L_0x02df;
            case -109382304: goto L_0x02d3;
            case -43923783: goto L_0x02c7;
            case -43840953: goto L_0x02bb;
            case 26919318: goto L_0x02af;
            case 81142075: goto L_0x02a4;
            case 163679941: goto L_0x0299;
            case 180207563: goto L_0x028d;
            case 245790645: goto L_0x0281;
            case 262346941: goto L_0x0274;
            case 302189274: goto L_0x0268;
            case 302663708: goto L_0x025c;
            case 363965503: goto L_0x0250;
            case 394650567: goto L_0x0245;
            case 428819984: goto L_0x0239;
            case 501428239: goto L_0x022c;
            case 571050671: goto L_0x0220;
            case 603849904: goto L_0x0214;
            case 641141505: goto L_0x0208;
            case 669516689: goto L_0x01fc;
            case 694663701: goto L_0x01f0;
            case 717553764: goto L_0x01e4;
            case 822609188: goto L_0x01d7;
            case 822849473: goto L_0x01ca;
            case 822865318: goto L_0x01bd;
            case 822865392: goto L_0x01b0;
            case 859118878: goto L_0x01a4;
            case 904647503: goto L_0x0198;
            case 1043583697: goto L_0x018c;
            case 1060806194: goto L_0x0180;
            case 1154415139: goto L_0x0174;
            case 1154449330: goto L_0x0168;
            case 1239557416: goto L_0x015d;
            case 1255211837: goto L_0x0150;
            case 1283455191: goto L_0x0144;
            case 1305955842: goto L_0x0138;
            case 1377360791: goto L_0x012c;
            case 1383977381: goto L_0x0120;
            case 1431987873: goto L_0x0114;
            case 1432260414: goto L_0x0108;
            case 1436962847: goto L_0x00fc;
            case 1440428940: goto L_0x00f0;
            case 1454024983: goto L_0x00e4;
            case 1461751133: goto L_0x00d8;
            case 1502452311: goto L_0x00cc;
            case 1536912403: goto L_0x00c0;
            case 1573656544: goto L_0x00b5;
            case 1577426419: goto L_0x00a9;
            case 1637222218: goto L_0x009d;
            case 1643664935: goto L_0x0091;
            case 1673742401: goto L_0x0085;
            case 1709755138: goto L_0x0079;
            case 1851895234: goto L_0x006d;
            case 1868769095: goto L_0x0061;
            case 1948418893: goto L_0x0055;
            case 1969663169: goto L_0x0049;
            case 1993842850: goto L_0x003d;
            case 2041423923: goto L_0x0031;
            case 2062084266: goto L_0x0024;
            case 2062095256: goto L_0x0017;
            case 2132236175: goto L_0x000a;
            default: goto L_0x0008;
        };
    L_0x0008:
        goto L_0x054e;
    L_0x000a:
        r0 = "text/javascript";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0013:
        r0 = 39;
        goto L_0x054f;
    L_0x0017:
        r0 = "text/x-c++src";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0020:
        r0 = 21;
        goto L_0x054f;
    L_0x0024:
        r0 = "text/x-c++hdr";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x002d:
        r0 = 20;
        goto L_0x054f;
    L_0x0031:
        r0 = "application/x-x509-user-cert";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0039:
        r0 = 9;
        goto L_0x054f;
    L_0x003d:
        r0 = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0045:
        r0 = 106; // 0x6a float:1.49E-43 double:5.24E-322;
        goto L_0x054f;
    L_0x0049:
        r0 = "application/rdf+xml";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0051:
        r0 = 13;
        goto L_0x054f;
    L_0x0055:
        r0 = "application/mac-binhex40";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x005d:
        r0 = 41;
        goto L_0x054f;
    L_0x0061:
        r0 = "application/x-quicktimeplayer";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0069:
        r0 = 100;
        goto L_0x054f;
    L_0x006d:
        r0 = "application/x-webarchive";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0075:
        r0 = 53;
        goto L_0x054f;
    L_0x0079:
        r0 = "application/x-font-woff";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0081:
        r0 = 65;
        goto L_0x054f;
    L_0x0085:
        r0 = "application/vnd.stardivision.writer";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x008d:
        r0 = 92;
        goto L_0x054f;
    L_0x0091:
        r0 = "application/vnd.oasis.opendocument.spreadsheet";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0099:
        r0 = 81;
        goto L_0x054f;
    L_0x009d:
        r0 = "application/x-kspread";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x00a5:
        r0 = 86;
        goto L_0x054f;
    L_0x00a9:
        r0 = "application/vnd.openxmlformats-officedocument.presentationml.slideshow";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x00b1:
        r0 = 111; // 0x6f float:1.56E-43 double:5.5E-322;
        goto L_0x054f;
    L_0x00b5:
        r0 = "application/x-pkcs12";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x00bd:
        r0 = 5;
        goto L_0x054f;
    L_0x00c0:
        r0 = "application/x-object";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x00c8:
        r0 = 15;
        goto L_0x054f;
    L_0x00cc:
        r0 = "application/font-woff";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x00d4:
        r0 = 64;
        goto L_0x054f;
    L_0x00d8:
        r0 = "application/vnd.oasis.opendocument.text-master";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x00e0:
        r0 = 89;
        goto L_0x054f;
    L_0x00e4:
        r0 = "application/x-7z-compressed";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x00ec:
        r0 = 56;
        goto L_0x054f;
    L_0x00f0:
        r0 = "application/javascript";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x00f8:
        r0 = 37;
        goto L_0x054f;
    L_0x00fc:
        r0 = "application/vnd.oasis.opendocument.presentation";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0104:
        r0 = 79;
        goto L_0x054f;
    L_0x0108:
        r0 = "application/x-latex";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0110:
        r0 = 32;
        goto L_0x054f;
    L_0x0114:
        r0 = "application/x-kword";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x011c:
        r0 = 98;
        goto L_0x054f;
    L_0x0120:
        r0 = "application/vnd.sun.xml.impress";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0128:
        r0 = 76;
        goto L_0x054f;
    L_0x012c:
        r0 = "application/vnd.oasis.opendocument.graphics-template";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0134:
        r0 = 68;
        goto L_0x054f;
    L_0x0138:
        r0 = "application/x-debian-package";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0140:
        r0 = 45;
        goto L_0x054f;
    L_0x0144:
        r0 = "application/x-apple-diskimage";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x014c:
        r0 = 44;
        goto L_0x054f;
    L_0x0150:
        r0 = "text/x-haskell";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0159:
        r0 = 26;
        goto L_0x054f;
    L_0x015d:
        r0 = "application/x-pkcs7-crl";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0165:
        r0 = 7;
        goto L_0x054f;
    L_0x0168:
        r0 = "application/x-gtar";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0170:
        r0 = 46;
        goto L_0x054f;
    L_0x0174:
        r0 = "application/x-font";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x017c:
        r0 = 63;
        goto L_0x054f;
    L_0x0180:
        r0 = "application/vnd.openxmlformats-officedocument.wordprocessingml.template";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0188:
        r0 = 104; // 0x68 float:1.46E-43 double:5.14E-322;
        goto L_0x054f;
    L_0x018c:
        r0 = "application/x-pkcs7-certificates";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0194:
        r0 = 10;
        goto L_0x054f;
    L_0x0198:
        r0 = "application/msword";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x01a0:
        r0 = 102; // 0x66 float:1.43E-43 double:5.04E-322;
        goto L_0x054f;
    L_0x01a4:
        r0 = "application/x-abiword";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x01ac:
        r0 = 97;
        goto L_0x054f;
    L_0x01b0:
        r0 = "text/x-tex";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x01b9:
        r0 = 31;
        goto L_0x054f;
    L_0x01bd:
        r0 = "text/x-tcl";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x01c6:
        r0 = 30;
        goto L_0x054f;
    L_0x01ca:
        r0 = "text/x-csh";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x01d3:
        r0 = 25;
        goto L_0x054f;
    L_0x01d7:
        r0 = "text/vcard";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x01e0:
        r0 = 60;
        goto L_0x054f;
    L_0x01e4:
        r0 = "application/vnd.google-apps.document";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x01ec:
        r0 = 99;
        goto L_0x054f;
    L_0x01f0:
        r0 = "application/vnd.openxmlformats-officedocument.presentationml.template";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x01f8:
        r0 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        goto L_0x054f;
    L_0x01fc:
        r0 = "application/vnd.stardivision.impress";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0204:
        r0 = 75;
        goto L_0x054f;
    L_0x0208:
        r0 = "application/x-texinfo";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0210:
        r0 = 33;
        goto L_0x054f;
    L_0x0214:
        r0 = "application/xhtml+xml";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x021c:
        r0 = 16;
        goto L_0x054f;
    L_0x0220:
        r0 = "application/vnd.stardivision.writer-global";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0228:
        r0 = 93;
        goto L_0x054f;
    L_0x022c:
        r0 = "text/x-vcard";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0235:
        r0 = 59;
        goto L_0x054f;
    L_0x0239:
        r0 = "application/vnd.oasis.opendocument.graphics";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0241:
        r0 = 67;
        goto L_0x054f;
    L_0x0245:
        r0 = "application/pgp-keys";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x024d:
        r0 = 3;
        goto L_0x054f;
    L_0x0250:
        r0 = "application/x-rar-compressed";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0258:
        r0 = 58;
        goto L_0x054f;
    L_0x025c:
        r0 = "application/ecmascript";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0264:
        r0 = 35;
        goto L_0x054f;
    L_0x0268:
        r0 = "vnd.android.document/directory";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0271:
        r0 = 1;
        goto L_0x054f;
    L_0x0274:
        r0 = "text/x-vcalendar";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x027d:
        r0 = 62;
        goto L_0x054f;
    L_0x0281:
        r0 = "application/vnd.google-apps.drawing";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0289:
        r0 = 73;
        goto L_0x054f;
    L_0x028d:
        r0 = "application/x-stuffit";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0295:
        r0 = 51;
        goto L_0x054f;
    L_0x0299:
        r0 = "application/pgp-signature";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x02a1:
        r0 = 4;
        goto L_0x054f;
    L_0x02a4:
        r0 = "application/vnd.android.package-archive";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x02ac:
        r0 = 2;
        goto L_0x054f;
    L_0x02af:
        r0 = "application/x-iso9660-image";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x02b7:
        r0 = 47;
        goto L_0x054f;
    L_0x02bb:
        r0 = "application/json";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x02c3:
        r0 = 36;
        goto L_0x054f;
    L_0x02c7:
        r0 = "application/gzip";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x02cf:
        r0 = 55;
        goto L_0x054f;
    L_0x02d3:
        r0 = "application/vnd.oasis.opendocument.spreadsheet-template";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x02db:
        r0 = 82;
        goto L_0x054f;
    L_0x02df:
        r0 = "application/x-font-ttf";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x02e7:
        r0 = 66;
        goto L_0x054f;
    L_0x02eb:
        r0 = "application/x-pkcs7-mime";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x02f3:
        r0 = 11;
        goto L_0x054f;
    L_0x02f7:
        r0 = "text/x-java";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0300:
        r0 = 27;
        goto L_0x054f;
    L_0x0304:
        r0 = "text/x-dsrc";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x030d:
        r0 = 24;
        goto L_0x054f;
    L_0x0311:
        r0 = "text/x-csrc";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x031a:
        r0 = 23;
        goto L_0x054f;
    L_0x031e:
        r0 = "text/x-chdr";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0327:
        r0 = 22;
        goto L_0x054f;
    L_0x032b:
        r0 = "application/vnd.ms-excel";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0333:
        r0 = 105; // 0x69 float:1.47E-43 double:5.2E-322;
        goto L_0x054f;
    L_0x0337:
        r0 = "application/vnd.sun.xml.impress.template";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x033f:
        r0 = 77;
        goto L_0x054f;
    L_0x0343:
        r0 = "application/x-webarchive-xml";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x034b:
        r0 = 54;
        goto L_0x054f;
    L_0x034f:
        r0 = "application/vnd.sun.xml.writer.global";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0357:
        r0 = 95;
        goto L_0x054f;
    L_0x035b:
        r0 = "application/vnd.oasis.opendocument.text-web";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0363:
        r0 = 91;
        goto L_0x054f;
    L_0x0367:
        r0 = "application/x-javascript";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x036f:
        r0 = 40;
        goto L_0x054f;
    L_0x0373:
        r0 = "application/vnd.sun.xml.draw";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x037b:
        r0 = 71;
        goto L_0x054f;
    L_0x037f:
        r0 = "application/vnd.sun.xml.calc";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0387:
        r0 = 84;
        goto L_0x054f;
    L_0x038b:
        r0 = "application/vnd.google-apps.presentation";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0393:
        r0 = 80;
        goto L_0x054f;
    L_0x0397:
        r0 = "text/calendar";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x03a0:
        r0 = 61;
        goto L_0x054f;
    L_0x03a4:
        r0 = "text/xml";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x03ad:
        r0 = 19;
        goto L_0x054f;
    L_0x03b1:
        r0 = "text/css";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x03ba:
        r0 = 17;
        goto L_0x054f;
    L_0x03be:
        r0 = "application/vnd.sun.xml.draw.template";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x03c6:
        r0 = 72;
        goto L_0x054f;
    L_0x03ca:
        r0 = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x03d2:
        r0 = 103; // 0x67 float:1.44E-43 double:5.1E-322;
        goto L_0x054f;
    L_0x03d6:
        r0 = "application/vnd.ms-powerpoint";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x03de:
        r0 = 108; // 0x6c float:1.51E-43 double:5.34E-322;
        goto L_0x054f;
    L_0x03e2:
        r0 = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x03ea:
        r0 = 109; // 0x6d float:1.53E-43 double:5.4E-322;
        goto L_0x054f;
    L_0x03ee:
        r0 = "text/html";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x03f7:
        r0 = 18;
        goto L_0x054f;
    L_0x03fb:
        r0 = "application/x-pkcs7-certreqresp";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0403:
        r0 = 6;
        goto L_0x054f;
    L_0x0406:
        r0 = "application/x-pkcs7-signature";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x040e:
        r0 = 12;
        goto L_0x054f;
    L_0x0412:
        r0 = "application/zip";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x041a:
        r0 = 43;
        goto L_0x054f;
    L_0x041e:
        r0 = "application/xml";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0426:
        r0 = 38;
        goto L_0x054f;
    L_0x042a:
        r0 = "application/rar";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0432:
        r0 = 42;
        goto L_0x054f;
    L_0x0436:
        r0 = "application/pdf";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x043e:
        r0 = 74;
        goto L_0x054f;
    L_0x0442:
        r0 = "inode/directory";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x044a:
        r0 = 0;
        goto L_0x054f;
    L_0x044d:
        r0 = "application/atom+xml";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0455:
        r0 = 34;
        goto L_0x054f;
    L_0x0459:
        r0 = "application/vnd.oasis.opendocument.text-template";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0461:
        r0 = 90;
        goto L_0x054f;
    L_0x0465:
        r0 = "application/x-shockwave-flash";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x046d:
        r0 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        goto L_0x054f;
    L_0x0471:
        r0 = "application/x-tar";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0479:
        r0 = 52;
        goto L_0x054f;
    L_0x047d:
        r0 = "application/x-lzx";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0485:
        r0 = 50;
        goto L_0x054f;
    L_0x0489:
        r0 = "application/x-lzh";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0491:
        r0 = 49;
        goto L_0x054f;
    L_0x0495:
        r0 = "application/x-lha";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x049d:
        r0 = 48;
        goto L_0x054f;
    L_0x04a1:
        r0 = "application/x-deb";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x04a9:
        r0 = 57;
        goto L_0x054f;
    L_0x04ad:
        r0 = "application/x-kpresenter";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x04b5:
        r0 = 78;
        goto L_0x054f;
    L_0x04b9:
        r0 = "application/vnd.openxmlformats-officedocument.spreadsheetml.template";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x04c1:
        r0 = 107; // 0x6b float:1.5E-43 double:5.3E-322;
        goto L_0x054f;
    L_0x04c5:
        r0 = "application/vnd.sun.xml.calc.template";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x04cd:
        r0 = 85;
        goto L_0x054f;
    L_0x04d1:
        r0 = "application/vnd.sun.xml.writer";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x04d9:
        r0 = 94;
        goto L_0x054f;
    L_0x04dd:
        r0 = "application/vnd.oasis.opendocument.text";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x04e5:
        r0 = 88;
        goto L_0x054f;
    L_0x04e9:
        r0 = "application/vnd.sun.xml.writer.template";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x04f1:
        r0 = 96;
        goto L_0x054f;
    L_0x04f4:
        r0 = "application/vnd.oasis.opendocument.image";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x04fc:
        r0 = 69;
        goto L_0x054f;
    L_0x04ff:
        r0 = "text/x-pascal";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0508:
        r0 = 29;
        goto L_0x054f;
    L_0x050b:
        r0 = "application/rss+xml";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0513:
        r0 = 14;
        goto L_0x054f;
    L_0x0516:
        r0 = "text/x-literate-haskell";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x051f:
        r0 = 28;
        goto L_0x054f;
    L_0x0522:
        r0 = "application/x-x509-ca-cert";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x052a:
        r0 = 8;
        goto L_0x054f;
    L_0x052d:
        r0 = "application/vnd.google-apps.spreadsheet";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0535:
        r0 = 87;
        goto L_0x054f;
    L_0x0538:
        r0 = "application/vnd.stardivision.draw";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x0540:
        r0 = 70;
        goto L_0x054f;
    L_0x0543:
        r0 = "application/vnd.stardivision.calc";
        r0 = r10.equals(r0);
        if (r0 == 0) goto L_0x0008;
    L_0x054b:
        r0 = 83;
        goto L_0x054f;
    L_0x054e:
        r0 = r1;
    L_0x054f:
        r2 = 17040491; // 0x104046b float:2.424774E-38 double:8.419121E-317;
        r3 = 17040490; // 0x104046a float:2.4247738E-38 double:8.4191207E-317;
        r4 = 17040489; // 0x1040469 float:2.4247735E-38 double:8.41912E-317;
        r5 = 17040488; // 0x1040468 float:2.4247732E-38 double:8.4191197E-317;
        r6 = 17040482; // 0x1040462 float:2.4247715E-38 double:8.4191167E-317;
        r7 = 17040481; // 0x1040461 float:2.4247713E-38 double:8.4191163E-317;
        r8 = 17040485; // 0x1040465 float:2.4247724E-38 double:8.419118E-317;
        r9 = 17040484; // 0x1040464 float:2.424772E-38 double:8.4191177E-317;
        switch(r0) {
            case 0: goto L_0x0604;
            case 1: goto L_0x0604;
            case 2: goto L_0x05f9;
            case 3: goto L_0x05f1;
            case 4: goto L_0x05f1;
            case 5: goto L_0x05f1;
            case 6: goto L_0x05f1;
            case 7: goto L_0x05f1;
            case 8: goto L_0x05f1;
            case 9: goto L_0x05f1;
            case 10: goto L_0x05f1;
            case 11: goto L_0x05f1;
            case 12: goto L_0x05f1;
            case 13: goto L_0x05e9;
            case 14: goto L_0x05e9;
            case 15: goto L_0x05e9;
            case 16: goto L_0x05e9;
            case 17: goto L_0x05e9;
            case 18: goto L_0x05e9;
            case 19: goto L_0x05e9;
            case 20: goto L_0x05e9;
            case 21: goto L_0x05e9;
            case 22: goto L_0x05e9;
            case 23: goto L_0x05e9;
            case 24: goto L_0x05e9;
            case 25: goto L_0x05e9;
            case 26: goto L_0x05e9;
            case 27: goto L_0x05e9;
            case 28: goto L_0x05e9;
            case 29: goto L_0x05e9;
            case 30: goto L_0x05e9;
            case 31: goto L_0x05e9;
            case 32: goto L_0x05e9;
            case 33: goto L_0x05e9;
            case 34: goto L_0x05e9;
            case 35: goto L_0x05e9;
            case 36: goto L_0x05e9;
            case 37: goto L_0x05e9;
            case 38: goto L_0x05e9;
            case 39: goto L_0x05e9;
            case 40: goto L_0x05e9;
            case 41: goto L_0x05db;
            case 42: goto L_0x05db;
            case 43: goto L_0x05db;
            case 44: goto L_0x05db;
            case 45: goto L_0x05db;
            case 46: goto L_0x05db;
            case 47: goto L_0x05db;
            case 48: goto L_0x05db;
            case 49: goto L_0x05db;
            case 50: goto L_0x05db;
            case 51: goto L_0x05db;
            case 52: goto L_0x05db;
            case 53: goto L_0x05db;
            case 54: goto L_0x05db;
            case 55: goto L_0x05db;
            case 56: goto L_0x05db;
            case 57: goto L_0x05db;
            case 58: goto L_0x05db;
            case 59: goto L_0x05d3;
            case 60: goto L_0x05d3;
            case 61: goto L_0x05cb;
            case 62: goto L_0x05cb;
            case 63: goto L_0x05c3;
            case 64: goto L_0x05c3;
            case 65: goto L_0x05c3;
            case 66: goto L_0x05c3;
            case 67: goto L_0x05b5;
            case 68: goto L_0x05b5;
            case 69: goto L_0x05b5;
            case 70: goto L_0x05b5;
            case 71: goto L_0x05b5;
            case 72: goto L_0x05b5;
            case 73: goto L_0x05b5;
            case 74: goto L_0x05ad;
            case 75: goto L_0x05a5;
            case 76: goto L_0x05a5;
            case 77: goto L_0x05a5;
            case 78: goto L_0x05a5;
            case 79: goto L_0x05a5;
            case 80: goto L_0x05a5;
            case 81: goto L_0x059d;
            case 82: goto L_0x059d;
            case 83: goto L_0x059d;
            case 84: goto L_0x059d;
            case 85: goto L_0x059d;
            case 86: goto L_0x059d;
            case 87: goto L_0x059d;
            case 88: goto L_0x0595;
            case 89: goto L_0x0595;
            case 90: goto L_0x0595;
            case 91: goto L_0x0595;
            case 92: goto L_0x0595;
            case 93: goto L_0x0595;
            case 94: goto L_0x0595;
            case 95: goto L_0x0595;
            case 96: goto L_0x0595;
            case 97: goto L_0x0595;
            case 98: goto L_0x0595;
            case 99: goto L_0x0595;
            case 100: goto L_0x0587;
            case 101: goto L_0x0587;
            case 102: goto L_0x057f;
            case 103: goto L_0x057f;
            case 104: goto L_0x057f;
            case 105: goto L_0x0577;
            case 106: goto L_0x0577;
            case 107: goto L_0x0577;
            case 108: goto L_0x056f;
            case 109: goto L_0x056f;
            case 110: goto L_0x056f;
            case 111: goto L_0x056f;
            default: goto L_0x056a;
        };
    L_0x056a:
        r0 = buildGenericTypeInfo(r10);
        return r0;
    L_0x056f:
        r0 = 17302404; // 0x1080384 float:2.4981777E-38 double:8.5485234E-317;
        r0 = buildTypeInfo(r10, r0, r5, r4);
        return r0;
    L_0x0577:
        r0 = 17302398; // 0x108037e float:2.498176E-38 double:8.5485204E-317;
        r0 = buildTypeInfo(r10, r0, r3, r2);
        return r0;
    L_0x057f:
        r0 = 17302409; // 0x1080389 float:2.498179E-38 double:8.548526E-317;
        r0 = buildTypeInfo(r10, r0, r7, r6);
        return r0;
    L_0x0587:
        r0 = 17302408; // 0x1080388 float:2.4981789E-38 double:8.5485254E-317;
        r1 = 17040492; // 0x104046c float:2.4247744E-38 double:8.4191217E-317;
        r2 = 17040493; // 0x104046d float:2.4247746E-38 double:8.419122E-317;
        r0 = buildTypeInfo(r10, r0, r1, r2);
        return r0;
    L_0x0595:
        r0 = 17302396; // 0x108037c float:2.4981755E-38 double:8.5485195E-317;
        r0 = buildTypeInfo(r10, r0, r7, r6);
        return r0;
    L_0x059d:
        r0 = 17302406; // 0x1080386 float:2.4981783E-38 double:8.5485244E-317;
        r0 = buildTypeInfo(r10, r0, r3, r2);
        return r0;
    L_0x05a5:
        r0 = 17302405; // 0x1080385 float:2.498178E-38 double:8.548524E-317;
        r0 = buildTypeInfo(r10, r0, r5, r4);
        return r0;
    L_0x05ad:
        r0 = 17302403; // 0x1080383 float:2.4981774E-38 double:8.548523E-317;
        r0 = buildTypeInfo(r10, r0, r7, r6);
        return r0;
    L_0x05b5:
        r0 = 17302402; // 0x1080382 float:2.4981772E-38 double:8.5485224E-317;
        r1 = 17040486; // 0x1040466 float:2.4247727E-38 double:8.4191187E-317;
        r2 = 17040487; // 0x1040467 float:2.424773E-38 double:8.419119E-317;
        r0 = buildTypeInfo(r10, r0, r1, r2);
        return r0;
    L_0x05c3:
        r0 = 17302400; // 0x1080380 float:2.4981766E-38 double:8.5485214E-317;
        r0 = buildTypeInfo(r10, r0, r9, r8);
        return r0;
    L_0x05cb:
        r0 = 17302397; // 0x108037d float:2.4981758E-38 double:8.54852E-317;
        r0 = buildTypeInfo(r10, r0, r9, r8);
        return r0;
    L_0x05d3:
        r0 = 17302395; // 0x108037b float:2.4981752E-38 double:8.548519E-317;
        r0 = buildTypeInfo(r10, r0, r9, r8);
        return r0;
    L_0x05db:
        r0 = 17302394; // 0x108037a float:2.498175E-38 double:8.5485185E-317;
        r1 = 17040479; // 0x104045f float:2.4247707E-38 double:8.4191153E-317;
        r2 = 17040480; // 0x1040460 float:2.424771E-38 double:8.419116E-317;
        r0 = buildTypeInfo(r10, r0, r1, r2);
        return r0;
    L_0x05e9:
        r0 = 17302393; // 0x1080379 float:2.4981746E-38 double:8.548518E-317;
        r0 = buildTypeInfo(r10, r0, r7, r6);
        return r0;
    L_0x05f1:
        r0 = 17302392; // 0x1080378 float:2.4981744E-38 double:8.5485175E-317;
        r0 = buildTypeInfo(r10, r0, r9, r8);
        return r0;
    L_0x05f9:
        r0 = 17302390; // 0x1080376 float:2.4981738E-38 double:8.5485165E-317;
        r2 = 17040476; // 0x104045c float:2.42477E-38 double:8.419114E-317;
        r0 = buildTypeInfo(r10, r0, r2, r1);
        return r0;
    L_0x0604:
        r0 = 17302399; // 0x108037f float:2.4981763E-38 double:8.548521E-317;
        r2 = 17040483; // 0x1040463 float:2.4247718E-38 double:8.419117E-317;
        r0 = buildTypeInfo(r10, r0, r2, r1);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.util.MimeIconUtils.buildTypeInfo(java.lang.String):android.content.ContentResolver$MimeTypeInfo");
    }

    private static MimeTypeInfo buildGenericTypeInfo(String mimeType) {
        if (mimeType.startsWith("audio/")) {
            return buildTypeInfo(mimeType, R.drawable.ic_doc_audio, R.string.mime_type_audio, R.string.mime_type_audio_ext);
        }
        if (mimeType.startsWith("video/")) {
            return buildTypeInfo(mimeType, R.drawable.ic_doc_video, R.string.mime_type_video, R.string.mime_type_video_ext);
        }
        if (mimeType.startsWith(MessagingMessage.IMAGE_MIME_TYPE_PREFIX)) {
            return buildTypeInfo(mimeType, R.drawable.ic_doc_image, R.string.mime_type_image, R.string.mime_type_image_ext);
        }
        if (mimeType.startsWith("text/")) {
            return buildTypeInfo(mimeType, R.drawable.ic_doc_text, R.string.mime_type_document, R.string.mime_type_document_ext);
        }
        String bouncedMimeType = MimeUtils.guessMimeTypeFromExtension(MimeUtils.guessExtensionFromMimeType(mimeType));
        if (bouncedMimeType == null || Objects.equals(mimeType, bouncedMimeType)) {
            return buildTypeInfo(mimeType, R.drawable.ic_doc_generic, R.string.mime_type_generic, R.string.mime_type_generic_ext);
        }
        return buildTypeInfo(bouncedMimeType);
    }

    public static MimeTypeInfo getTypeInfo(String mimeType) {
        MimeTypeInfo res;
        mimeType = mimeType.toLowerCase(Locale.US);
        synchronized (sCache) {
            res = (MimeTypeInfo) sCache.get(mimeType);
            if (res == null) {
                res = buildTypeInfo(mimeType);
                sCache.put(mimeType, res);
            }
        }
        return res;
    }
}
