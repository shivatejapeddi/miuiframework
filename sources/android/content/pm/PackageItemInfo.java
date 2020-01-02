package android.content.pm;

import android.annotation.SystemApi;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.text.TextUtils;
import android.util.Printer;
import android.util.proto.ProtoOutputStream;
import com.android.internal.util.Preconditions;
import java.text.Collator;
import java.util.Comparator;

public class PackageItemInfo {
    public static final float DEFAULT_MAX_LABEL_SIZE_PX = 500.0f;
    public static final int DUMP_FLAG_ALL = 3;
    public static final int DUMP_FLAG_APPLICATION = 2;
    public static final int DUMP_FLAG_DETAILS = 1;
    private static final int MAX_SAFE_LABEL_LENGTH = 50000;
    @SystemApi
    @Deprecated
    public static final int SAFE_LABEL_FLAG_FIRST_LINE = 4;
    @SystemApi
    @Deprecated
    public static final int SAFE_LABEL_FLAG_SINGLE_LINE = 2;
    @SystemApi
    @Deprecated
    public static final int SAFE_LABEL_FLAG_TRIM = 1;
    private static volatile boolean sForceSafeLabels = false;
    public int banner;
    public int icon;
    public int labelRes;
    public int logo;
    public Bundle metaData;
    public String name;
    public CharSequence nonLocalizedLabel;
    public String packageName;
    public int showUserIcon;

    public static class DisplayNameComparator implements Comparator<PackageItemInfo> {
        private PackageManager mPM;
        private final Collator sCollator = Collator.getInstance();

        public DisplayNameComparator(PackageManager pm) {
            this.mPM = pm;
        }

        public final int compare(PackageItemInfo aa, PackageItemInfo ab) {
            CharSequence sa = aa.loadLabel(this.mPM);
            if (sa == null) {
                sa = aa.name;
            }
            CharSequence sb = ab.loadLabel(this.mPM);
            if (sb == null) {
                sb = ab.name;
            }
            return this.sCollator.compare(sa.toString(), sb.toString());
        }
    }

    @SystemApi
    public static void forceSafeLabels() {
        sForceSafeLabels = true;
    }

    public PackageItemInfo() {
        this.showUserIcon = -10000;
    }

    public PackageItemInfo(PackageItemInfo orig) {
        this.name = orig.name;
        String str = this.name;
        if (str != null) {
            this.name = str.trim();
        }
        this.packageName = orig.packageName;
        this.labelRes = orig.labelRes;
        this.nonLocalizedLabel = orig.nonLocalizedLabel;
        CharSequence charSequence = this.nonLocalizedLabel;
        if (charSequence != null) {
            this.nonLocalizedLabel = charSequence.toString().trim();
        }
        this.icon = orig.icon;
        this.banner = orig.banner;
        this.logo = orig.logo;
        this.metaData = orig.metaData;
        this.showUserIcon = orig.showUserIcon;
    }

    public CharSequence loadLabel(PackageManager pm) {
        if (sForceSafeLabels) {
            return loadSafeLabel(pm, 500.0f, 5);
        }
        return loadUnsafeLabel(pm);
    }

    public CharSequence loadUnsafeLabel(PackageManager pm) {
        CharSequence charSequence = this.nonLocalizedLabel;
        if (charSequence != null) {
            return charSequence;
        }
        charSequence = this.labelRes;
        if (charSequence != null) {
            charSequence = pm.getText(this.packageName, charSequence, getApplicationInfo());
            if (charSequence != null) {
                return charSequence.toString().trim();
            }
        }
        String str = this.name;
        if (str != null) {
            return str;
        }
        return this.packageName;
    }

    @SystemApi
    @Deprecated
    public CharSequence loadSafeLabel(PackageManager pm) {
        return loadSafeLabel(pm, 500.0f, 5);
    }

    @SystemApi
    public CharSequence loadSafeLabel(PackageManager pm, float ellipsizeDip, int flags) {
        Preconditions.checkNotNull(pm);
        return TextUtils.makeSafeForPresentation(loadUnsafeLabel(pm).toString(), 50000, ellipsizeDip, flags);
    }

    public Drawable loadIcon(PackageManager pm) {
        return pm.loadItemIcon(this, getApplicationInfo());
    }

    public Drawable loadUnbadgedIcon(PackageManager pm) {
        return pm.loadUnbadgedItemIcon(this, getApplicationInfo());
    }

    public Drawable loadBanner(PackageManager pm) {
        Drawable dr = this.banner;
        if (dr != null) {
            dr = pm.getDrawable(this.packageName, dr, getApplicationInfo());
            if (dr != null) {
                return dr;
            }
        }
        return loadDefaultBanner(pm);
    }

    public Drawable loadDefaultIcon(PackageManager pm) {
        return pm.getDefaultActivityIcon();
    }

    /* Access modifiers changed, original: protected */
    public Drawable loadDefaultBanner(PackageManager pm) {
        return null;
    }

    public Drawable loadLogo(PackageManager pm) {
        Drawable d = this.logo;
        if (d != null) {
            d = pm.getDrawable(this.packageName, d, getApplicationInfo());
            if (d != null) {
                return d;
            }
        }
        return loadDefaultLogo(pm);
    }

    /* Access modifiers changed, original: protected */
    public Drawable loadDefaultLogo(PackageManager pm) {
        return null;
    }

    public XmlResourceParser loadXmlMetaData(PackageManager pm, String name) {
        int resid = this.metaData;
        if (resid != 0) {
            resid = resid.getInt(name);
            if (resid != 0) {
                return pm.getXml(this.packageName, resid, getApplicationInfo());
            }
        }
        return null;
    }

    /* Access modifiers changed, original: protected */
    public void dumpFront(Printer pw, String prefix) {
        StringBuilder stringBuilder;
        if (this.name != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("name=");
            stringBuilder.append(this.name);
            pw.println(stringBuilder.toString());
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append("packageName=");
        stringBuilder.append(this.packageName);
        pw.println(stringBuilder.toString());
        if (this.labelRes != 0 || this.nonLocalizedLabel != null || this.icon != 0 || this.banner != 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("labelRes=0x");
            stringBuilder.append(Integer.toHexString(this.labelRes));
            stringBuilder.append(" nonLocalizedLabel=");
            stringBuilder.append(this.nonLocalizedLabel);
            stringBuilder.append(" icon=0x");
            stringBuilder.append(Integer.toHexString(this.icon));
            stringBuilder.append(" banner=0x");
            stringBuilder.append(Integer.toHexString(this.banner));
            pw.println(stringBuilder.toString());
        }
    }

    /* Access modifiers changed, original: protected */
    public void dumpBack(Printer pw, String prefix) {
    }

    public void writeToParcel(Parcel dest, int parcelableFlags) {
        dest.writeString(this.name);
        dest.writeString(this.packageName);
        dest.writeInt(this.labelRes);
        TextUtils.writeToParcel(this.nonLocalizedLabel, dest, parcelableFlags);
        dest.writeInt(this.icon);
        dest.writeInt(this.logo);
        dest.writeBundle(this.metaData);
        dest.writeInt(this.banner);
        dest.writeInt(this.showUserIcon);
    }

    public void writeToProto(ProtoOutputStream proto, long fieldId, int dumpFlags) {
        long token = proto.start(fieldId);
        String str = this.name;
        if (str != null) {
            proto.write(1138166333441L, str);
        }
        proto.write(1138166333442L, this.packageName);
        proto.write(1120986464259L, this.labelRes);
        CharSequence charSequence = this.nonLocalizedLabel;
        if (charSequence != null) {
            proto.write(1138166333444L, charSequence.toString());
        }
        proto.write(1120986464261L, this.icon);
        proto.write(1120986464262L, this.banner);
        proto.end(token);
    }

    protected PackageItemInfo(Parcel source) {
        this.name = source.readString();
        this.packageName = source.readString();
        this.labelRes = source.readInt();
        this.nonLocalizedLabel = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(source);
        this.icon = source.readInt();
        this.logo = source.readInt();
        this.metaData = source.readBundle();
        this.banner = source.readInt();
        this.showUserIcon = source.readInt();
    }

    /* Access modifiers changed, original: protected */
    public ApplicationInfo getApplicationInfo() {
        return null;
    }
}
