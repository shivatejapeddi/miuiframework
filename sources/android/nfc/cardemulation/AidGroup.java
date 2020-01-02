package android.nfc.cardemulation;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class AidGroup implements Parcelable {
    @UnsupportedAppUsage
    public static final Creator<AidGroup> CREATOR = new Creator<AidGroup>() {
        public AidGroup createFromParcel(Parcel source) {
            String category = source.readString();
            int listSize = source.readInt();
            List aidList = new ArrayList();
            if (listSize > 0) {
                source.readStringList(aidList);
            }
            return new AidGroup(aidList, category);
        }

        public AidGroup[] newArray(int size) {
            return new AidGroup[size];
        }
    };
    public static final int MAX_NUM_AIDS = 256;
    static final String TAG = "AidGroup";
    @UnsupportedAppUsage
    protected List<String> aids;
    @UnsupportedAppUsage
    protected String category;
    @UnsupportedAppUsage
    protected String description;

    public AidGroup(List<String> aids, String category) {
        if (aids == null || aids.size() == 0) {
            throw new IllegalArgumentException("No AIDS in AID group.");
        } else if (aids.size() <= 256) {
            for (String aid : aids) {
                if (!CardEmulation.isValidAid(aid)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("AID ");
                    stringBuilder.append(aid);
                    stringBuilder.append(" is not a valid AID.");
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
            if (isValidCategory(category)) {
                this.category = category;
            } else {
                this.category = "other";
            }
            this.aids = new ArrayList(aids.size());
            for (String aid2 : aids) {
                this.aids.add(aid2.toUpperCase());
            }
            this.description = null;
        } else {
            throw new IllegalArgumentException("Too many AIDs in AID group.");
        }
    }

    @UnsupportedAppUsage
    AidGroup(String category, String description) {
        this.aids = new ArrayList();
        this.category = category;
        this.description = description;
    }

    @UnsupportedAppUsage
    public String getCategory() {
        return this.category;
    }

    @UnsupportedAppUsage
    public List<String> getAids() {
        return this.aids;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Category: ");
        stringBuilder.append(this.category);
        stringBuilder.append(", AIDs:");
        StringBuilder out = new StringBuilder(stringBuilder.toString());
        for (String aid : this.aids) {
            out.append(aid);
            out.append(", ");
        }
        return out.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.category);
        dest.writeInt(this.aids.size());
        if (this.aids.size() > 0) {
            dest.writeStringList(this.aids);
        }
    }

    @UnsupportedAppUsage
    public static AidGroup createFromXml(XmlPullParser parser) throws XmlPullParserException, IOException {
        String category = null;
        List aids = new ArrayList();
        AidGroup group = null;
        boolean inGroup = false;
        int eventType = parser.getEventType();
        int minDepth = parser.getDepth();
        while (eventType != 1 && parser.getDepth() >= minDepth) {
            String tagName = parser.getName();
            String str = "aid-group";
            if (eventType != 2) {
                if (eventType == 3 && tagName.equals(str) && inGroup && aids.size() > 0) {
                    group = new AidGroup(aids, category);
                    break;
                }
            }
            boolean equals = tagName.equals("aid");
            String str2 = TAG;
            if (equals) {
                if (inGroup) {
                    String aid = parser.getAttributeValue(null, "value");
                    if (aid != null) {
                        aids.add(aid.toUpperCase());
                    }
                } else {
                    Log.d(str2, "Ignoring <aid> tag while not in group");
                }
            } else if (tagName.equals(str)) {
                category = parser.getAttributeValue(null, "category");
                if (category == null) {
                    Log.e(str2, "<aid-group> tag without valid category");
                    return null;
                }
                inGroup = true;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Ignoring unexpected tag: ");
                stringBuilder.append(tagName);
                Log.d(str2, stringBuilder.toString());
            }
            eventType = parser.next();
        }
        return group;
    }

    @UnsupportedAppUsage
    public void writeAsXml(XmlSerializer out) throws IOException {
        String str = "aid-group";
        out.startTag(null, str);
        out.attribute(null, "category", this.category);
        for (String aid : this.aids) {
            String str2 = "aid";
            out.startTag(null, str2);
            out.attribute(null, "value", aid);
            out.endTag(null, str2);
        }
        out.endTag(null, str);
    }

    static boolean isValidCategory(String category) {
        return CardEmulation.CATEGORY_PAYMENT.equals(category) || "other".equals(category);
    }
}
