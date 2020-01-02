package miui.util;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.TypefaceInjector;
import android.provider.BrowserContract;
import android.provider.MiuiSettings.XSpace;
import android.provider.Settings.System;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import miui.os.Build;
import miui.os.Environment;
import miui.push.PushConstants;

public class TypefaceUtils {
    private static final String DEFAULT_FAMILY = "sans-serif";
    private static final int DEFAULT_IDX = 4;
    private static final int DEFAULT_SCALE = 50;
    private static final String KEY_FONT_WEIGHT = "key_miui_font_weight_scale";
    private static final String KEY_USE_MIUI_FONT = "use_miui_font";
    private static final String MIPRO_FAMILY = "mipro";
    private static final String MITYPE_FAMILY = "mitype";
    private static final String MITYPE_MONO_FAMILY = "mitype-mono";
    private static final String MIUI_FAMILY = "miui";
    private static final String TAG = "TypefaceUtils";
    private static final boolean USING_VAR_FONT;
    private static final int WEIGHT_BOLD = 700;
    private static final int WEIGHT_DEMIBOLD = 550;
    private static final int WEIGHT_EXTRALIGHT = 200;
    private static final int WEIGHT_HEAVY = 900;
    private static final int WEIGHT_LIGHT = 300;
    private static final int WEIGHT_MEDIUM = 500;
    private static final int WEIGHT_NORMAL = 350;
    private static final int WEIGHT_REGULAR = 400;
    private static final int WEIGHT_SEMIBOLD = 600;
    private static final int WEIGHT_THIN = 100;
    private static final Field sFamilyNameField = getFamilyNameField();
    private static final Map<String, Boolean> sUsingMiuiFontMap = new ArrayMap();

    private static class FontsWhiteListHolder {
        private static final HashSet<String> mFontsWhiteList = new HashSet();

        private FontsWhiteListHolder() {
        }

        static {
            mFontsWhiteList.add("com.tencent.mm");
            mFontsWhiteList.add(XSpace.QQ_PACKAGE_NAME);
            mFontsWhiteList.add("com.UCMobile");
            mFontsWhiteList.add("com.qzone");
            mFontsWhiteList.add(XSpace.WEIBO_PACKAGE_NAME);
            mFontsWhiteList.add("com.qvod.player");
            mFontsWhiteList.add("com.qihoo360.mobilesafe");
            mFontsWhiteList.add("com.kugou.android");
            mFontsWhiteList.add("com.taobao.taobao");
            mFontsWhiteList.add("com.baidu.BaiduMap");
            mFontsWhiteList.add("com.youku.phone");
            mFontsWhiteList.add("com.sds.android.ttpod");
            mFontsWhiteList.add("com.qihoo.appstore");
            mFontsWhiteList.add("com.pplive.androidphone");
            mFontsWhiteList.add("com.tencent.minihd.qq");
            mFontsWhiteList.add("tv.pps.mobile");
            mFontsWhiteList.add("com.xiaomi.channel");
            mFontsWhiteList.add("com.shuqi.controller");
            mFontsWhiteList.add("com.storm.smart");
            mFontsWhiteList.add("com.tencent.qbx");
            mFontsWhiteList.add("com.moji.mjweather");
            mFontsWhiteList.add("com.wandoujia.phoenix2");
            mFontsWhiteList.add("com.renren.mobile.android");
            mFontsWhiteList.add("com.duokan.reader");
            mFontsWhiteList.add("com.immomo.momo");
            mFontsWhiteList.add("com.tencent.news");
            mFontsWhiteList.add("com.tencent.qqmusic");
            mFontsWhiteList.add("com.qiyi.video");
            mFontsWhiteList.add("com.baidu.video");
            mFontsWhiteList.add("com.tencent.WBlog");
            mFontsWhiteList.add("qsbk.app");
            mFontsWhiteList.add("com.netease.newsreader.activity");
            mFontsWhiteList.add("com.sohu.newsclient");
            mFontsWhiteList.add("com.tencent.mtt");
            mFontsWhiteList.add("com.baidu.tieba");
            mFontsWhiteList.add("com.wochacha");
            mFontsWhiteList.add("com.tencent.qqpimsecure");
            mFontsWhiteList.add("com.xiaomi.shop");
            mFontsWhiteList.add("com.mt.mtxx.mtxx");
            mFontsWhiteList.add("com.qihoo360.mobilesafe.opti.powerctl");
            mFontsWhiteList.add("com.dragon.android.pandaspace");
            mFontsWhiteList.add("cn.etouch.ecalendar");
            mFontsWhiteList.add("com.changba");
            mFontsWhiteList.add(PushConstants.PUSH_SERVICE_PACKAGE_NAME);
            mFontsWhiteList.add("com.tencent.qqlive");
            mFontsWhiteList.add("com.chaozh.iReaderFree");
            mFontsWhiteList.add("com.snda.wifilocating");
            mFontsWhiteList.add("com.ijinshan.kbatterydoctor");
            mFontsWhiteList.add("com.duowan.mobile");
            mFontsWhiteList.add("com.hiapk.marketpho");
            mFontsWhiteList.add("com.qihoo360.launcher");
            mFontsWhiteList.add("com.qihoo360.mobilesafe.opti");
            mFontsWhiteList.add("cn.com.fetion");
            mFontsWhiteList.add("com.nd.android.pandahome2");
            mFontsWhiteList.add("com.youdao.dict");
            mFontsWhiteList.add("com.eg.android.AlipayGphone");
            mFontsWhiteList.add("cn.kuwo.player");
            mFontsWhiteList.add("cn.wps.moffice");
            mFontsWhiteList.add("com.alibaba.mobileim");
            mFontsWhiteList.add("com.letv.android.client");
            mFontsWhiteList.add("com.baidu.searchbox");
            mFontsWhiteList.add("com.funshion.video.mobile");
            mFontsWhiteList.add("com.gau.go.launcherex");
            mFontsWhiteList.add("cn.opda.a.phonoalbumshoushou");
            mFontsWhiteList.add("com.qq.reader");
            mFontsWhiteList.add("com.duomi.android");
            mFontsWhiteList.add("com.qihoo.browser");
            mFontsWhiteList.add("com.meitu.meiyancamera");
            mFontsWhiteList.add("com.nd.android.pandareader");
            mFontsWhiteList.add("com.kingsoft");
            mFontsWhiteList.add("com.cleanmaster.mguard");
            mFontsWhiteList.add("com.sohu.sohuvideo");
            mFontsWhiteList.add("com.jingdong.app.mall");
            mFontsWhiteList.add("bubei.tingshu");
            mFontsWhiteList.add("com.alipay.android.app");
            mFontsWhiteList.add("vStudio.Android.Camera360");
            mFontsWhiteList.add("com.androidesk");
            mFontsWhiteList.add("com.ss.android.article.news");
            mFontsWhiteList.add("org.funship.findsomething.withRK");
            mFontsWhiteList.add("com.mybook66");
            mFontsWhiteList.add("com.tencent.token");
            mFontsWhiteList.add("com.tmall.wireless");
            mFontsWhiteList.add("com.tencent.qqgame.qqlordwvga");
            mFontsWhiteList.add("com.budejie.www");
            mFontsWhiteList.add("com.sankuai.meituan");
            mFontsWhiteList.add("com.google.android.apps.maps");
            mFontsWhiteList.add("com.kascend.video");
            mFontsWhiteList.add("com.tencent.android.pad");
            mFontsWhiteList.add("com.muzhiwan.market");
            mFontsWhiteList.add("com.mymoney");
            mFontsWhiteList.add("com.baidu.browser.apps");
            mFontsWhiteList.add("com.geili.koudai");
            mFontsWhiteList.add("com.baidu.news");
            mFontsWhiteList.add("com.tencent.androidqqmail");
            mFontsWhiteList.add("com.myzaker.ZAKER_Phone");
            mFontsWhiteList.add("com.ifeng.news2");
            mFontsWhiteList.add("com.handsgo.jiakao.android");
            mFontsWhiteList.add("com.hexin.plat.android");
            mFontsWhiteList.add("com.tencent.qqphonebook");
            mFontsWhiteList.add("my.beautyCamera");
            mFontsWhiteList.add("com.autonavi.minimap");
            mFontsWhiteList.add("com.cubic.autohome");
            mFontsWhiteList.add("com.clov4r.android.nil");
            mFontsWhiteList.add("com.yangzhibin.chengrenxiaohua");
            mFontsWhiteList.add("com.dianxinos.powermanager");
            mFontsWhiteList.add("com.ijinshan.duba");
            mFontsWhiteList.add("com.wuba");
            mFontsWhiteList.add("sina.mobile.tianqitong");
            mFontsWhiteList.add("com.mandi.lol");
            mFontsWhiteList.add("com.duowan.lolbox");
            mFontsWhiteList.add("com.android.chrome");
            mFontsWhiteList.add("com.chinamworld.main");
            mFontsWhiteList.add("com.ss.android.essay.joke");
            mFontsWhiteList.add("air.com.tencent.qqpasture");
            mFontsWhiteList.add("com.kingreader.framework");
            mFontsWhiteList.add("cn.ibuka.manga.ui");
            mFontsWhiteList.add("com.ting.mp3.qianqian.android");
            mFontsWhiteList.add("com.jiubang.goscreenlock");
            mFontsWhiteList.add("com.shoujiduoduo.ringtone");
            mFontsWhiteList.add("com.lbe.security");
            mFontsWhiteList.add("com.snda.youni");
            mFontsWhiteList.add("com.jiasoft.swreader");
            mFontsWhiteList.add("com.anyview");
            mFontsWhiteList.add("com.baidu.appsearch");
            mFontsWhiteList.add("com.sohu.inputmethod.sogou");
            mFontsWhiteList.add("com.mxtech.videoplayer.ad");
            mFontsWhiteList.add("com.zdworks.android.zdclock");
            mFontsWhiteList.add("com.antutu.ABenchMark");
            mFontsWhiteList.add("dopool.player");
            mFontsWhiteList.add("com.uc.browser");
            mFontsWhiteList.add("com.ijinshan.mguard");
            mFontsWhiteList.add("bdmobile.android.app");
            mFontsWhiteList.add("com.alensw.PicFolder");
            mFontsWhiteList.add("com.xiaomi.topic");
            mFontsWhiteList.add("com.oupeng.mini.android");
            mFontsWhiteList.add("com.qihoo360.launcher.screenlock");
            mFontsWhiteList.add("com.android.vending");
            mFontsWhiteList.add("com.meilishuo");
            mFontsWhiteList.add("com.qidian.QDReader");
            mFontsWhiteList.add("com.tencent.research.drop");
            mFontsWhiteList.add("com.android.bluetooth");
            mFontsWhiteList.add("com.sinovatech.unicom.ui");
            mFontsWhiteList.add("com.dianping.v1");
            mFontsWhiteList.add("com.yx");
            mFontsWhiteList.add("com.dianxinos.dxhome");
            mFontsWhiteList.add("com.yiche.price");
            mFontsWhiteList.add("com.iBookStar.activity");
            mFontsWhiteList.add("com.android.dazhihui");
            mFontsWhiteList.add("cn.wps.moffice_eng");
            mFontsWhiteList.add("com.taobao.wwseller");
            mFontsWhiteList.add("com.icbc");
            mFontsWhiteList.add("cn.chinabus.main");
            mFontsWhiteList.add("com.ganji.android");
            mFontsWhiteList.add("com.ting.mp3.android");
            mFontsWhiteList.add("com.hy.minifetion");
            mFontsWhiteList.add("com.mogujie");
            mFontsWhiteList.add("com.baozoumanhua.android");
            mFontsWhiteList.add("com.calendar.UI");
            mFontsWhiteList.add("com.wacai365");
            mFontsWhiteList.add("com.cnvcs.junqi");
            mFontsWhiteList.add("cn.cntv");
            mFontsWhiteList.add("com.xunlei.kankan");
            mFontsWhiteList.add("com.xikang.android.slimcoach");
            mFontsWhiteList.add("com.thunder.ktvdaren");
            mFontsWhiteList.add("cn.goapk.market");
            mFontsWhiteList.add("cn.htjyb.reader");
            mFontsWhiteList.add("com.sec.android.app.camera");
            mFontsWhiteList.add("com.blovestorm");
            mFontsWhiteList.add("me.papa");
            mFontsWhiteList.add("com.when.android.calendar365");
            mFontsWhiteList.add("com.android.wallpaper.livepicker");
            mFontsWhiteList.add("com.vancl.activity");
            mFontsWhiteList.add("jp.naver.line.android");
            mFontsWhiteList.add("com.netease.mkey");
            mFontsWhiteList.add("com.youba.barcode");
            mFontsWhiteList.add("com.hupu.games");
            mFontsWhiteList.add("com.kandian.vodapp");
            mFontsWhiteList.add("com.dewmobile.kuaiya");
            mFontsWhiteList.add("com.anguanjia.safe");
            mFontsWhiteList.add("com.tudou.android");
            mFontsWhiteList.add("cmb.pb");
            mFontsWhiteList.add("com.weico.sinaweibo");
            mFontsWhiteList.add("com.ireadercity.b2");
            mFontsWhiteList.add("cn.wps.livespace");
            mFontsWhiteList.add("com.estrongs.android.pop");
            mFontsWhiteList.add(XSpace.FACEBOOK_PACKAGE_NAME);
            mFontsWhiteList.add("com.disney.WMW");
            mFontsWhiteList.add("com.tuan800.tao800");
            mFontsWhiteList.add("com.byread.reader");
            mFontsWhiteList.add("me.imid.fuubo");
            mFontsWhiteList.add("com.lingdong.client.android");
            mFontsWhiteList.add("com.mop.activity");
            mFontsWhiteList.add("com.sina.mfweibo");
            mFontsWhiteList.add("cld.navi.mainframe");
            mFontsWhiteList.add("com.mappn.gfan");
            mFontsWhiteList.add("com.tencent.pengyou");
            mFontsWhiteList.add("com.xunlei.downloadprovider");
            mFontsWhiteList.add("com.tencent.android.qqdownloader");
            mFontsWhiteList.add(XSpace.WHATSAPP_PACKAGE_NAME);
            mFontsWhiteList.add("com.mx.browser");
            mFontsWhiteList.add("com.xiaomi.jr");
            mFontsWhiteList.add("com.xiaomi.smarthome");
            mFontsWhiteList.add("com.miui.backup.transfer");
            mFontsWhiteList.add("com.sohu.inputmethod.sogou.xiaomi");
            mFontsWhiteList.add("com.baidu.input_miv6");
            mFontsWhiteList.add("com.baidu.input_mi");
            mFontsWhiteList.add("com.souhu.inputmethod.sogou.xiaomi");
            mFontsWhiteList.add("com.iflytek.inputmethod.miui");
            mFontsWhiteList.add("com.wali.live");
            mFontsWhiteList.add("com.miui.hybrid");
            mFontsWhiteList.add("com.miui.hybrid.loader");
            mFontsWhiteList.add("com.miui.player");
            mFontsWhiteList.add(BrowserContract.AUTHORITY);
            mFontsWhiteList.add("com.miui.systemAdSolution");
            mFontsWhiteList.add("com.mi.health");
            mFontsWhiteList.add("com.xiaomi.vipaccount");
        }
    }

    private static class Holder {
        private static final String[] DEFAULT_FONT_NAMES = new String[]{TypefaceUtils.DEFAULT_FAMILY, "sans-serif-thin", "sans-serif-light", "sans-serif-medium", "sans-serif-black", "sans-serif-regular", "arial", "helvetica", "tahoma", "verdana"};
        private static final String[] DEFAULT_NAME_MAP;
        private static final SparseArray<FontCacheItem> FONT_CACHE = new SparseArray();
        private static final int[][] LARGE_RULES;
        private static final String[] MITYPE_MONO_NAMES;
        private static final Typeface MITYPE_MONO_VAR_FONT;
        private static final String[] MITYPE_NAMES;
        private static final Typeface MITYPE_VAR_FONT;
        private static final int[] MITYPE_WGHT;
        private static final String[] MIUI_NAMES;
        private static final String[] MIUI_NAME_ARRAY;
        private static final Typeface[] MIUI_TYPEFACES;
        private static final Typeface MIUI_VAR_FONT;
        private static final int[] MIUI_WGHT;
        private static final int[][] NORAML_RULES;
        private static final int SIZE_GRADE_COUNT = 3;
        private static final int[][] SMALL_RULES;
        private static final String[] VF_NAME_ARRAY;
        private static final int[] WEIGHT_KEYS;

        private static class FontCacheItem {
            Typeface[] cache = new Typeface[3];
            int scale;

            FontCacheItem() {
                for (int i = 0; i < 3; i++) {
                    this.cache[i] = null;
                }
            }

            /* Access modifiers changed, original: 0000 */
            public void setFont(Typeface typeface, int sizeGrade) {
                this.cache[sizeGrade] = typeface;
            }

            /* Access modifiers changed, original: 0000 */
            public Typeface getFont(int sizeGrade) {
                return this.cache[sizeGrade];
            }

            /* Access modifiers changed, original: 0000 */
            public void clear() {
                int i = 0;
                while (true) {
                    Typeface[] typefaceArr = this.cache;
                    if (i < typefaceArr.length) {
                        typefaceArr[i] = null;
                        i++;
                    } else {
                        return;
                    }
                }
            }
        }

        private Holder() {
        }

        static {
            String str = "miui";
            if (TypefaceUtils.USING_VAR_FONT) {
                int[] iArr;
                DEFAULT_NAME_MAP = new String[]{"thin", "thin", "light", "light", "medium", "medium", "black", "heavy"};
                VF_NAME_ARRAY = new String[]{"thin", "extralight", "light", "normal", "regular", "medium", "demibold", "semibold", "bold", "heavy"};
                MIUI_NAME_ARRAY = new String[]{"thin", "null", "light", "null", "regular", "bold"};
                WEIGHT_KEYS = new int[]{100, 200, 300, 350, 400, 500, 550, 600, 700, 900};
                MIUI_WGHT = new int[]{150, 200, 250, 305, 340, 400, 480, 540, MetricsEvent.ACTION_PERMISSION_REQUEST_UNKNOWN, 700};
                MITYPE_WGHT = new int[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
                int i = 0;
                while (true) {
                    iArr = MIUI_WGHT;
                    if (i >= iArr.length) {
                        break;
                    }
                    FONT_CACHE.put(iArr[i], new FontCacheItem());
                    i++;
                }
                i = 0;
                while (true) {
                    iArr = MITYPE_WGHT;
                    if (i < iArr.length) {
                        FONT_CACHE.put(iArr[i], new FontCacheItem());
                        i++;
                    } else {
                        String str2 = TypefaceUtils.MIPRO_FAMILY;
                        MIUI_NAMES = new String[]{str, str2};
                        str = TypefaceUtils.MITYPE_FAMILY;
                        MITYPE_NAMES = new String[]{str};
                        String str3 = TypefaceUtils.MITYPE_MONO_FAMILY;
                        MITYPE_MONO_NAMES = new String[]{str3};
                        SMALL_RULES = new int[][]{new int[]{100, 500}, new int[]{200, 500}, new int[]{300, 500}, new int[]{350, 550}, new int[]{350, 550}, new int[]{400, 600}, new int[]{500, 700}, new int[]{550, 700}, new int[]{600, 700}, new int[]{700, 900}};
                        NORAML_RULES = new int[][]{new int[]{100, 500}, new int[]{100, 500}, new int[]{200, 550}, new int[]{300, 550}, new int[]{300, 600}, new int[]{350, 700}, new int[]{400, 700}, new int[]{500, 900}, new int[]{550, 900}, new int[]{600, 900}};
                        LARGE_RULES = new int[][]{new int[]{100, 300}, new int[]{100, 350}, new int[]{200, 400}, new int[]{200, 500}, new int[]{300, 550}, new int[]{300, 600}, new int[]{350, 700}, new int[]{400, 900}, new int[]{500, 900}, new int[]{550, 900}};
                        MIUI_TYPEFACES = new Typeface[0];
                        MIUI_VAR_FONT = Typeface.create(str2, 0);
                        MITYPE_VAR_FONT = Typeface.create(str, 0);
                        MITYPE_MONO_VAR_FONT = Typeface.create(str3, 0);
                        return;
                    }
                }
            }
            int[] iArr2 = new int[0];
            MITYPE_WGHT = iArr2;
            MIUI_WGHT = iArr2;
            WEIGHT_KEYS = iArr2;
            String[] strArr = new String[0];
            MITYPE_MONO_NAMES = strArr;
            MITYPE_NAMES = strArr;
            MIUI_NAMES = strArr;
            MIUI_NAME_ARRAY = strArr;
            VF_NAME_ARRAY = strArr;
            DEFAULT_NAME_MAP = strArr;
            int[][] iArr3 = (int[][]) Array.newInstance(int.class, new int[]{0, 0});
            LARGE_RULES = iArr3;
            NORAML_RULES = iArr3;
            SMALL_RULES = iArr3;
            MITYPE_MONO_VAR_FONT = null;
            MITYPE_VAR_FONT = null;
            MIUI_VAR_FONT = null;
            if (new File("system/fonts/Miui-Regular.ttf").exists()) {
                MIUI_TYPEFACES = new Typeface[4];
                MIUI_TYPEFACES[0] = Typeface.create(str, 0);
                MIUI_TYPEFACES[1] = Typeface.create(str, 1);
                MIUI_TYPEFACES[2] = Typeface.create(str, 2);
                MIUI_TYPEFACES[3] = Typeface.create(str, 3);
                return;
            }
            MIUI_TYPEFACES = new Typeface[0];
        }

        static int getTextSizeGrade(float textSize) {
            if (textSize > 20.0f) {
                return 1;
            }
            if (textSize <= 0.0f || textSize >= 12.0f) {
                return 0;
            }
            return 2;
        }

        static int[][] getRules(int sizeGrade) {
            if (sizeGrade == 0) {
                return NORAML_RULES;
            }
            if (sizeGrade != 1) {
                return SMALL_RULES;
            }
            return LARGE_RULES;
        }

        static int getWeightIndex(int weight) {
            int i = 0;
            while (true) {
                int[] iArr = WEIGHT_KEYS;
                if (i >= iArr.length) {
                    return -1;
                }
                if (iArr[i] == weight) {
                    return i;
                }
                i++;
            }
        }

        static int getWght(int weight, boolean isMitype) {
            int[] wghts = isMitype ? MITYPE_WGHT : MIUI_WGHT;
            int index = getWeightIndex(weight);
            if (index >= 0) {
                return wghts[index];
            }
            return getWght(400, isMitype);
        }

        static Typeface getCachedFont(int wght, int scale, int sizeGrade) {
            FontCacheItem item = (FontCacheItem) FONT_CACHE.get(wght);
            if (item == null || item.scale != scale) {
                return null;
            }
            return item.getFont(sizeGrade);
        }

        static void cacheFont(Typeface tf, int wght, int scale, int sizeGrade) {
            FontCacheItem item = (FontCacheItem) FONT_CACHE.get(wght);
            if (item.scale != scale) {
                item.scale = scale;
                item.clear();
            }
            item.setFont(tf, sizeGrade);
        }
    }

    static {
        boolean z = sFamilyNameField != null && new File("system/fonts/MiLanProVF.ttf").exists();
        USING_VAR_FONT = z;
    }

    private static Field getFamilyNameField() {
        try {
            return Typeface.class.getField("familyName");
        } catch (Exception e) {
            Log.i(TAG, "Typeface has no familyName field");
            return null;
        }
    }

    public static boolean usingMiuiFonts(Context context) {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        if (Environment.isUsingMiui(context) || FontsWhiteListHolder.mFontsWhiteList.contains(context.getPackageName())) {
            return true;
        }
        return isUsingMiuiFont(context);
    }

    private static boolean isUsingMiuiFont(Context context) {
        String packageName = context.getPackageName();
        Boolean usingMiuiFont = (Boolean) sUsingMiuiFontMap.get(packageName);
        if (usingMiuiFont == null) {
            try {
                usingMiuiFont = Boolean.valueOf(context.getPackageManager().getApplicationInfo(packageName, 128).metaData.getBoolean(KEY_USE_MIUI_FONT, false));
                sUsingMiuiFontMap.put(packageName, usingMiuiFont);
            } catch (Exception e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("get metaData of ");
                stringBuilder.append(context.getPackageName());
                stringBuilder.append(" failed");
                Log.w(TAG, stringBuilder.toString(), e);
                return false;
            }
        }
        return usingMiuiFont.booleanValue();
    }

    public static boolean isFontChanged(Configuration newConfig) {
        return (newConfig.extraConfig.themeChangedFlags & 536870912) != 0;
    }

    public static boolean isUsingMiFont(Typeface typeface) {
        return isNamesOf(typeface != null ? getFontNames(typeface) : new String[null], "miui", MIPRO_FAMILY, MITYPE_FAMILY, MITYPE_MONO_FAMILY);
    }

    public static Typeface replaceTypeface(Context context, Typeface oldTypeface) {
        return replaceTypeface(context, oldTypeface, -1.0f);
    }

    public static Typeface replaceTypeface(Context context, Typeface oldTypeface, float textSize) {
        String[] fontNames = oldTypeface != null ? getFontNames(oldTypeface) : new String[null];
        boolean isMiuiFont = isNamesOf(fontNames, new String[]{"miui", MIPRO_FAMILY, MITYPE_FAMILY, MITYPE_MONO_FAMILY});
        if (!Build.IS_INTERNATIONAL_BUILD && !TypefaceInjector.isUsingThemeFont()) {
            Typeface newTypeface = null;
            if (usingMiuiFonts(context) || isMiuiFont) {
                if (USING_VAR_FONT) {
                    newTypeface = replaceWithVarFont(context, oldTypeface, textSize);
                } else {
                    newTypeface = replaceWithMiuiFont(oldTypeface);
                }
            }
            return newTypeface == null ? oldTypeface : newTypeface;
        } else if (isMiuiFont) {
            return convertMiuiFontToSysFont(oldTypeface, fontNames);
        } else {
            return oldTypeface;
        }
    }

    private static Typeface convertMiuiFontToSysFont(Typeface oldTypeface, String[] fontNames) {
        String fontName = DEFAULT_FAMILY;
        for (int i = 1; i < Holder.DEFAULT_NAME_MAP.length; i += 2) {
            for (String contains : fontNames) {
                if (contains.contains(Holder.DEFAULT_NAME_MAP[i])) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("sans-serif-");
                    stringBuilder.append(Holder.DEFAULT_NAME_MAP[i - 1]);
                    fontName = stringBuilder.toString();
                    break;
                }
            }
        }
        return Typeface.create(fontName, oldTypeface.getStyle());
    }

    private static Typeface replaceWithMiuiFont(Typeface oldTypeface) {
        if (oldTypeface != null && !Typeface.DEFAULT.equals(oldTypeface) && !Typeface.DEFAULT_BOLD.equals(oldTypeface) && !Typeface.SANS_SERIF.equals(oldTypeface)) {
            return null;
        }
        return Holder.MIUI_TYPEFACES[oldTypeface == null ? 0 : oldTypeface.getStyle()];
    }

    private static Typeface replaceWithVarFont(Context context, Typeface oldTypeface, float textSize) {
        if (oldTypeface != null && oldTypeface.isItalic()) {
            return null;
        }
        float scaledTextSize;
        String[] fontNames;
        if (context == null) {
            scaledTextSize = -1.0f;
        } else {
            scaledTextSize = textSize / context.getResources().getDisplayMetrics().scaledDensity;
        }
        if (oldTypeface == null || oldTypeface == Typeface.DEFAULT || oldTypeface == Typeface.DEFAULT_BOLD) {
            fontNames = new String[]{MIPRO_FAMILY};
        } else {
            fontNames = getFontNames(oldTypeface);
        }
        boolean isDefault = isDefaultFont(fontNames);
        if (isDefault || isNamesOf(fontNames, Holder.MIUI_NAMES)) {
            if (isDefault) {
                fontNames = convertFontNames(fontNames);
            }
            return getVarFont(context, oldTypeface, scaledTextSize, false, Holder.MIUI_VAR_FONT, fontNames);
        } else if (isNamesOf(fontNames, MITYPE_MONO_FAMILY)) {
            return getVarFont(context, oldTypeface, scaledTextSize, true, Holder.MITYPE_MONO_VAR_FONT, fontNames);
        } else if (!isNamesOf(fontNames, MITYPE_FAMILY)) {
            return null;
        } else {
            return getVarFont(context, oldTypeface, scaledTextSize, true, Holder.MITYPE_VAR_FONT, fontNames);
        }
    }

    private static String[] convertFontNames(String[] fontNames) {
        for (int i = 0; i < Holder.DEFAULT_NAME_MAP.length; i += 2) {
            for (String contains : fontNames) {
                if (contains.contains(Holder.DEFAULT_NAME_MAP[i])) {
                    String[] strArr = new String[1];
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("mipro-");
                    stringBuilder.append(Holder.DEFAULT_NAME_MAP[i + 1]);
                    strArr[0] = stringBuilder.toString();
                    return strArr;
                }
            }
        }
        return new String[]{MIPRO_FAMILY};
    }

    private static String[] getFontNames(Typeface oldTypeface) {
        String[] fontNames = new String[null];
        Field field = sFamilyNameField;
        if (field == null) {
            return fontNames;
        }
        try {
            return (String[]) field.get(oldTypeface);
        } catch (Exception e) {
            Log.w(TAG, "get familyName failed", e);
            return fontNames;
        }
    }

    private static void setFontNames(Typeface font, String[] fontNames) {
        try {
            sFamilyNameField.set(font, fontNames);
        } catch (Exception e) {
            Log.w(TAG, "set familyName failed", e);
        }
    }

    private static int getNameIndex(String[] fontNames, String[] nameArray) {
        for (String contains : fontNames) {
            for (int i = 0; i < nameArray.length; i++) {
                if (contains.contains(nameArray[i])) {
                    return i;
                }
            }
        }
        return 4;
    }

    private static int getProperWeight(Typeface oldTypeface, String[] fontNames) {
        if (oldTypeface == null) {
            return 400;
        }
        int idx = getNameIndex(fontNames, isNamesOf(fontNames, new String[]{"miui"}) ? Holder.MIUI_NAME_ARRAY : Holder.VF_NAME_ARRAY);
        int weight = (!oldTypeface.isBold() || idx >= Holder.WEIGHT_KEYS.length - 1) ? Holder.WEIGHT_KEYS[idx] : Holder.WEIGHT_KEYS[idx + 1];
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getProperWeight, fontNames = ");
        stringBuilder.append(Arrays.toString(fontNames));
        stringBuilder.append(", style = ");
        stringBuilder.append(oldTypeface.getStyle());
        stringBuilder.append(", idx = ");
        stringBuilder.append(idx);
        stringBuilder.append(", weight = ");
        stringBuilder.append(weight);
        Log.d(TAG, stringBuilder.toString());
        return weight;
    }

    private static Typeface getVarFont(Context context, Typeface oldTypeface, float textSize, boolean isMitype, Typeface baseFont, String[] fontNames) {
        int weight = getProperWeight(oldTypeface, fontNames);
        int wght = Holder.getWght(weight, isMitype);
        int scale = 50;
        if (context != null) {
            scale = System.getInt(context.getContentResolver(), KEY_FONT_WEIGHT, 50);
        }
        int sizeGrade = Holder.getTextSizeGrade(textSize);
        Typeface font = Holder.getCachedFont(wght, scale, sizeGrade);
        if (font != null) {
            return font;
        }
        font = TypefaceHelper.createVarFont(baseFont, getScaleWght(weight, scale, sizeGrade, isMitype));
        setFontNames(font, fontNames);
        Holder.cacheFont(font, wght, scale, sizeGrade);
        return font;
    }

    private static int getScaleWght(int weight, int scale, int textGrade, boolean isMitype) {
        int[][] rules = Holder.getRules(textGrade);
        int wgtIndex = Holder.getWeightIndex(weight);
        if (wgtIndex >= 0) {
            return getWghtInRange(weight, rules[wgtIndex], scale, isMitype);
        }
        return getWghtInRange(400, Holder.NORAML_RULES[Holder.getWeightIndex(400)], scale, isMitype);
    }

    private static int getWghtInRange(int weight, int[] range, int scale, boolean isMitype) {
        int startWght = Holder.getWght(range[0], isMitype);
        int midWght = Holder.getWght(weight, isMitype);
        int endWght = Holder.getWght(range[1], isMitype);
        int wght = midWght;
        float t;
        if (scale < 50) {
            t = ((float) scale) / 50.0f;
            return (int) (((1.0f - t) * ((float) startWght)) + (((float) midWght) * t));
        } else if (scale <= 50) {
            return wght;
        } else {
            t = ((float) (scale - 50)) / 50.0f;
            return (int) (((1.0f - t) * ((float) midWght)) + (((float) endWght) * t));
        }
    }

    private static boolean isDefaultFont(String[] names) {
        if (names == null) {
            return false;
        }
        for (CharSequence contains : Holder.DEFAULT_FONT_NAMES) {
            for (String contains2 : names) {
                if (contains2.contains(contains)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isNamesOf(String[] names, String... familyNames) {
        if (names != null && names.length > 0) {
            for (int i = 0; i < familyNames.length; i++) {
                String extendName = new StringBuilder();
                extendName.append(familyNames[i]);
                extendName.append('-');
                extendName = extendName.toString();
                int j = 0;
                while (j < names.length) {
                    if (names[j].equals(familyNames[i]) || names[j].contains(extendName)) {
                        return true;
                    }
                    j++;
                }
            }
        }
        return false;
    }
}
