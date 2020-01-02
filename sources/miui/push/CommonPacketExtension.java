package miui.push;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CommonPacketExtension implements PacketExtension {
    public static final String ATTRIBUTE_NAME = "attributes";
    public static final String CHILDREN_NAME = "children";
    private String[] mAttributeNames = null;
    private String[] mAttributeValues = null;
    private List<CommonPacketExtension> mChildrenEles = null;
    private String mExtensionElementName;
    private String mNamespace;
    private String mText;

    public CommonPacketExtension(String extensionElementName, String namespace, String[] attributeNames, String[] attributeValues) {
        this.mExtensionElementName = extensionElementName;
        this.mNamespace = namespace;
        this.mAttributeNames = attributeNames;
        this.mAttributeValues = attributeValues;
    }

    public CommonPacketExtension(String extensionElementName, String namespace, String attributeName, String attributeValue) {
        this.mExtensionElementName = extensionElementName;
        this.mNamespace = namespace;
        this.mAttributeNames = new String[]{attributeName};
        this.mAttributeValues = new String[]{attributeValue};
    }

    public CommonPacketExtension(String extensionElementName, String namespace, List<String> attributeNames, List<String> attributeValues) {
        this.mExtensionElementName = extensionElementName;
        this.mNamespace = namespace;
        this.mAttributeNames = (String[]) attributeNames.toArray(new String[attributeNames.size()]);
        this.mAttributeValues = (String[]) attributeValues.toArray(new String[attributeValues.size()]);
    }

    public CommonPacketExtension(String extensionElementName, String namespace, String[] attributeNames, String[] attributeValues, String text, List<CommonPacketExtension> children) {
        this.mExtensionElementName = extensionElementName;
        this.mNamespace = namespace;
        this.mAttributeNames = attributeNames;
        this.mAttributeValues = attributeValues;
        this.mText = text;
        this.mChildrenEles = children;
    }

    public CommonPacketExtension(String extensionElementName, String namespace, List<String> attributeNames, List<String> attributeValues, String text, List<CommonPacketExtension> children) {
        this.mExtensionElementName = extensionElementName;
        this.mNamespace = namespace;
        this.mAttributeNames = (String[]) attributeNames.toArray(new String[attributeNames.size()]);
        this.mAttributeValues = (String[]) attributeValues.toArray(new String[attributeValues.size()]);
        this.mText = text;
        this.mChildrenEles = children;
    }

    public String getElementName() {
        return this.mExtensionElementName;
    }

    public String getNamespace() {
        return this.mNamespace;
    }

    public String getText() {
        if (TextUtils.isEmpty(this.mText)) {
            return this.mText;
        }
        return StringUtils.unescapeFromXML(this.mText);
    }

    public String toString() {
        return toXML();
    }

    public String toXML() {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append(this.mExtensionElementName);
        boolean isEmpty = TextUtils.isEmpty(this.mNamespace);
        String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        String str2 = "\"";
        if (!isEmpty) {
            sb.append(str);
            sb.append("xmlns=");
            sb.append(str2);
            sb.append(this.mNamespace);
            sb.append(str2);
        }
        String[] strArr = this.mAttributeNames;
        if (strArr != null && strArr.length > 0) {
            for (int n = 0; n < this.mAttributeNames.length; n++) {
                if (!TextUtils.isEmpty(this.mAttributeValues[n])) {
                    sb.append(str);
                    sb.append(this.mAttributeNames[n]);
                    sb.append("=\"");
                    sb.append(StringUtils.escapeForXML(this.mAttributeValues[n]));
                    sb.append(str2);
                }
            }
        }
        str = "</";
        str2 = ">";
        if (TextUtils.isEmpty(this.mText)) {
            List list = this.mChildrenEles;
            if (list == null || list.size() <= 0) {
                sb.append("/>");
            } else {
                sb.append(str2);
                for (CommonPacketExtension cmmnPktExt : this.mChildrenEles) {
                    sb.append(cmmnPktExt.toXML());
                }
                sb.append(str);
                sb.append(this.mExtensionElementName);
                sb.append(str2);
            }
        } else {
            sb.append(str2);
            sb.append(this.mText);
            sb.append(str);
            sb.append(this.mExtensionElementName);
            sb.append(str2);
        }
        return sb.toString();
    }

    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putString(PushConstants.EXTRA_EXTENSION_ELEMENT_NAME, this.mExtensionElementName);
        b.putString(PushConstants.EXTRA_EXTENSION_NAMESPACE, this.mNamespace);
        b.putString(PushConstants.EXTRA_EXTENSION_TEXT, this.mText);
        Bundle attributesBundle = new Bundle();
        String[] strArr = this.mAttributeNames;
        if (strArr != null && strArr.length > 0) {
            int n = 0;
            while (true) {
                String[] strArr2 = this.mAttributeNames;
                if (n >= strArr2.length) {
                    break;
                }
                attributesBundle.putString(strArr2[n], this.mAttributeValues[n]);
                n++;
            }
        }
        b.putBundle(ATTRIBUTE_NAME, attributesBundle);
        List list = this.mChildrenEles;
        if (list != null && list.size() > 0) {
            b.putParcelableArray(CHILDREN_NAME, toParcelableArray(this.mChildrenEles));
        }
        return b;
    }

    public Parcelable toParcelable() {
        return toBundle();
    }

    public static Parcelable[] toParcelableArray(CommonPacketExtension[] extensions) {
        if (extensions == null) {
            return null;
        }
        Parcelable[] parcelables = new Parcelable[extensions.length];
        for (int i = 0; i < extensions.length; i++) {
            parcelables[i] = extensions[i].toParcelable();
        }
        return parcelables;
    }

    public static Parcelable[] toParcelableArray(List<CommonPacketExtension> extensions) {
        return toParcelableArray((CommonPacketExtension[]) extensions.toArray(new CommonPacketExtension[extensions.size()]));
    }

    public static CommonPacketExtension[] getArray(Parcelable[] parcelables) {
        CommonPacketExtension[] extensions = new CommonPacketExtension[(parcelables == null ? null : parcelables.length)];
        if (parcelables != null) {
            for (int i = 0; i < parcelables.length; i++) {
                extensions[i] = parseFromBundle((Bundle) parcelables[i]);
            }
        }
        return extensions;
    }

    public static CommonPacketExtension parseFromBundle(Bundle b) {
        List<CommonPacketExtension> childrenEles;
        Bundle bundle = b;
        String extensionElement = bundle.getString(PushConstants.EXTRA_EXTENSION_ELEMENT_NAME);
        String extensionNamespace = bundle.getString(PushConstants.EXTRA_EXTENSION_NAMESPACE);
        String text = bundle.getString(PushConstants.EXTRA_EXTENSION_TEXT);
        Bundle attributesBundle = bundle.getBundle(ATTRIBUTE_NAME);
        Set<String> attributes = attributesBundle.keySet();
        String[] attributeNames = new String[attributes.size()];
        String[] attributeValues = new String[attributes.size()];
        int index = 0;
        for (String attr : attributes) {
            attributeNames[index] = attr;
            attributeValues[index] = attributesBundle.getString(attr);
            index++;
        }
        Parcelable[] children = CHILDREN_NAME;
        if (bundle.containsKey(children)) {
            children = bundle.getParcelableArray(children);
            List<CommonPacketExtension> childrenEles2 = new ArrayList(children.length);
            for (Parcelable child : children) {
                childrenEles2.add(parseFromBundle((Bundle) child));
            }
            childrenEles = childrenEles2;
        } else {
            childrenEles = null;
        }
        return new CommonPacketExtension(extensionElement, extensionNamespace, attributeNames, attributeValues, text, (List) childrenEles);
    }

    public String getAttributeValue(String attributeName) {
        if (attributeName != null) {
            if (this.mAttributeNames != null) {
                int i = 0;
                while (true) {
                    String[] strArr = this.mAttributeNames;
                    if (i >= strArr.length) {
                        break;
                    } else if (attributeName.equals(strArr[i])) {
                        return this.mAttributeValues[i];
                    } else {
                        i++;
                    }
                }
            }
            return null;
        }
        throw new IllegalArgumentException();
    }

    public CommonPacketExtension getChildByName(String name) {
        if (!TextUtils.isEmpty(name)) {
            List<CommonPacketExtension> list = this.mChildrenEles;
            if (list != null) {
                for (CommonPacketExtension temp : list) {
                    if (temp.mExtensionElementName.equals(name)) {
                        return temp;
                    }
                }
                return null;
            }
        }
        return null;
    }

    public List<CommonPacketExtension> getChildrenByName(String name) {
        if (TextUtils.isEmpty(name) || this.mChildrenEles == null) {
            return null;
        }
        List<CommonPacketExtension> children = new ArrayList();
        for (CommonPacketExtension temp : this.mChildrenEles) {
            if (temp.mExtensionElementName.equals(name)) {
                children.add(temp);
            }
        }
        return children;
    }

    public List<CommonPacketExtension> getChildrenExt() {
        return this.mChildrenEles;
    }

    public void appendChild(CommonPacketExtension child) {
        if (this.mChildrenEles == null) {
            this.mChildrenEles = new ArrayList();
        }
        if (!this.mChildrenEles.contains(child)) {
            this.mChildrenEles.add(child);
        }
    }

    public void setText(String text) {
        if (TextUtils.isEmpty(text)) {
            this.mText = text;
        } else {
            this.mText = StringUtils.escapeForXML(text);
        }
    }
}
