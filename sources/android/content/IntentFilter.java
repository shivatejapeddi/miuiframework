package android.content;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PatternMatcher;
import android.text.TextUtils;
import android.util.AndroidException;
import android.util.Log;
import android.util.Printer;
import android.util.proto.ProtoOutputStream;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class IntentFilter implements Parcelable {
    private static final String ACTION_STR = "action";
    private static final String AGLOB_STR = "aglob";
    private static final String AUTH_STR = "auth";
    private static final String AUTO_VERIFY_STR = "autoVerify";
    private static final String CAT_STR = "cat";
    public static final Creator<IntentFilter> CREATOR = new Creator<IntentFilter>() {
        public IntentFilter createFromParcel(Parcel source) {
            return new IntentFilter(source);
        }

        public IntentFilter[] newArray(int size) {
            return new IntentFilter[size];
        }
    };
    private static final String HOST_STR = "host";
    private static final String LITERAL_STR = "literal";
    public static final int MATCH_ADJUSTMENT_MASK = 65535;
    public static final int MATCH_ADJUSTMENT_NORMAL = 32768;
    public static final int MATCH_CATEGORY_EMPTY = 1048576;
    public static final int MATCH_CATEGORY_HOST = 3145728;
    public static final int MATCH_CATEGORY_MASK = 268369920;
    public static final int MATCH_CATEGORY_PATH = 5242880;
    public static final int MATCH_CATEGORY_PORT = 4194304;
    public static final int MATCH_CATEGORY_SCHEME = 2097152;
    public static final int MATCH_CATEGORY_SCHEME_SPECIFIC_PART = 5767168;
    public static final int MATCH_CATEGORY_TYPE = 6291456;
    private static final String NAME_STR = "name";
    public static final int NO_MATCH_ACTION = -3;
    public static final int NO_MATCH_CATEGORY = -4;
    public static final int NO_MATCH_DATA = -2;
    public static final int NO_MATCH_TYPE = -1;
    private static final String PATH_STR = "path";
    private static final String PORT_STR = "port";
    private static final String PREFIX_STR = "prefix";
    public static final String SCHEME_HTTP = "http";
    public static final String SCHEME_HTTPS = "https";
    private static final String SCHEME_STR = "scheme";
    private static final String SGLOB_STR = "sglob";
    private static final String SSP_STR = "ssp";
    private static final int STATE_NEED_VERIFY = 16;
    private static final int STATE_NEED_VERIFY_CHECKED = 256;
    private static final int STATE_VERIFIED = 4096;
    private static final int STATE_VERIFY_AUTO = 1;
    public static final int SYSTEM_HIGH_PRIORITY = 1000;
    public static final int SYSTEM_LOW_PRIORITY = -1000;
    private static final String TYPE_STR = "type";
    public static final int VISIBILITY_EXPLICIT = 1;
    public static final int VISIBILITY_IMPLICIT = 2;
    public static final int VISIBILITY_NONE = 0;
    @UnsupportedAppUsage
    private final ArrayList<String> mActions;
    private ArrayList<String> mCategories;
    private ArrayList<AuthorityEntry> mDataAuthorities;
    private ArrayList<PatternMatcher> mDataPaths;
    private ArrayList<PatternMatcher> mDataSchemeSpecificParts;
    private ArrayList<String> mDataSchemes;
    private ArrayList<String> mDataTypes;
    private boolean mHasPartialTypes;
    private int mInstantAppVisibility;
    @UnsupportedAppUsage
    private int mOrder;
    private int mPriority;
    private int mVerifyState;

    public static final class AuthorityEntry {
        private final String mHost;
        private final String mOrigHost;
        private final int mPort;
        private final boolean mWild;

        public AuthorityEntry(String host, String port) {
            this.mOrigHost = host;
            boolean z = false;
            if (host.length() > 0 && host.charAt(0) == '*') {
                z = true;
            }
            this.mWild = z;
            this.mHost = this.mWild ? host.substring(1).intern() : host;
            this.mPort = port != null ? Integer.parseInt(port) : -1;
        }

        AuthorityEntry(Parcel src) {
            this.mOrigHost = src.readString();
            this.mHost = src.readString();
            this.mWild = src.readInt() != 0;
            this.mPort = src.readInt();
        }

        /* Access modifiers changed, original: 0000 */
        public void writeToParcel(Parcel dest) {
            dest.writeString(this.mOrigHost);
            dest.writeString(this.mHost);
            dest.writeInt(this.mWild);
            dest.writeInt(this.mPort);
        }

        /* Access modifiers changed, original: 0000 */
        public void writeToProto(ProtoOutputStream proto, long fieldId) {
            long token = proto.start(fieldId);
            proto.write(1138166333441L, this.mHost);
            proto.write(1133871366146L, this.mWild);
            proto.write(1120986464259L, this.mPort);
            proto.end(token);
        }

        public String getHost() {
            return this.mOrigHost;
        }

        public int getPort() {
            return this.mPort;
        }

        public boolean match(AuthorityEntry other) {
            if (this.mWild == other.mWild && this.mHost.equals(other.mHost) && this.mPort == other.mPort) {
                return true;
            }
            return false;
        }

        public boolean equals(Object obj) {
            if (obj instanceof AuthorityEntry) {
                return match((AuthorityEntry) obj);
            }
            return false;
        }

        public int match(Uri data) {
            String host = data.getHost();
            if (host == null) {
                return -2;
            }
            if (this.mWild) {
                if (host.length() < this.mHost.length()) {
                    return -2;
                }
                host = host.substring(host.length() - this.mHost.length());
            }
            if (host.compareToIgnoreCase(this.mHost) != 0) {
                return -2;
            }
            int i = this.mPort;
            if (i < 0) {
                return IntentFilter.MATCH_CATEGORY_HOST;
            }
            if (i != data.getPort()) {
                return -2;
            }
            return 4194304;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface InstantAppVisibility {
    }

    public static class MalformedMimeTypeException extends AndroidException {
        public MalformedMimeTypeException(String name) {
            super(name);
        }
    }

    private static int findStringInSet(String[] set, String string, int[] lengths, int lenPos) {
        if (set == null) {
            return -1;
        }
        int N = lengths[lenPos];
        for (int i = 0; i < N; i++) {
            if (set[i].equals(string)) {
                return i;
            }
        }
        return -1;
    }

    private static String[] addStringToSet(String[] set, String string, int[] lengths, int lenPos) {
        if (findStringInSet(set, string, lengths, lenPos) >= 0) {
            return set;
        }
        if (set == null) {
            return new String[]{string, 1};
        }
        int N = lengths[lenPos];
        if (N < set.length) {
            set[N] = string;
            lengths[lenPos] = N + 1;
            return set;
        }
        String[] newSet = new String[(((N * 3) / 2) + 2)];
        System.arraycopy(set, 0, newSet, 0, N);
        set = newSet;
        set[N] = string;
        lengths[lenPos] = N + 1;
        return set;
    }

    private static String[] removeStringFromSet(String[] set, String string, int[] lengths, int lenPos) {
        int pos = findStringInSet(set, string, lengths, lenPos);
        if (pos < 0) {
            return set;
        }
        int N = lengths[lenPos];
        if (N > set.length / 4) {
            int copyLen = N - (pos + 1);
            if (copyLen > 0) {
                System.arraycopy(set, pos + 1, set, pos, copyLen);
            }
            set[N - 1] = null;
            lengths[lenPos] = N - 1;
            return set;
        }
        String[] newSet = new String[(set.length / 3)];
        if (pos > 0) {
            System.arraycopy(set, 0, newSet, 0, pos);
        }
        if (pos + 1 < N) {
            System.arraycopy(set, pos + 1, newSet, pos, N - (pos + 1));
        }
        return newSet;
    }

    public static IntentFilter create(String action, String dataType) {
        try {
            return new IntentFilter(action, dataType);
        } catch (MalformedMimeTypeException e) {
            throw new RuntimeException("Bad MIME type", e);
        }
    }

    public IntentFilter() {
        this.mCategories = null;
        this.mDataSchemes = null;
        this.mDataSchemeSpecificParts = null;
        this.mDataAuthorities = null;
        this.mDataPaths = null;
        this.mDataTypes = null;
        this.mHasPartialTypes = false;
        this.mPriority = 0;
        this.mActions = new ArrayList();
    }

    public IntentFilter(String action) {
        this.mCategories = null;
        this.mDataSchemes = null;
        this.mDataSchemeSpecificParts = null;
        this.mDataAuthorities = null;
        this.mDataPaths = null;
        this.mDataTypes = null;
        this.mHasPartialTypes = false;
        this.mPriority = 0;
        this.mActions = new ArrayList();
        addAction(action);
    }

    public IntentFilter(String action, String dataType) throws MalformedMimeTypeException {
        this.mCategories = null;
        this.mDataSchemes = null;
        this.mDataSchemeSpecificParts = null;
        this.mDataAuthorities = null;
        this.mDataPaths = null;
        this.mDataTypes = null;
        this.mHasPartialTypes = false;
        this.mPriority = 0;
        this.mActions = new ArrayList();
        addAction(action);
        addDataType(dataType);
    }

    public IntentFilter(IntentFilter o) {
        this.mCategories = null;
        this.mDataSchemes = null;
        this.mDataSchemeSpecificParts = null;
        this.mDataAuthorities = null;
        this.mDataPaths = null;
        this.mDataTypes = null;
        this.mHasPartialTypes = false;
        this.mPriority = o.mPriority;
        this.mOrder = o.mOrder;
        this.mActions = new ArrayList(o.mActions);
        ArrayList arrayList = o.mCategories;
        if (arrayList != null) {
            this.mCategories = new ArrayList(arrayList);
        }
        arrayList = o.mDataTypes;
        if (arrayList != null) {
            this.mDataTypes = new ArrayList(arrayList);
        }
        arrayList = o.mDataSchemes;
        if (arrayList != null) {
            this.mDataSchemes = new ArrayList(arrayList);
        }
        arrayList = o.mDataSchemeSpecificParts;
        if (arrayList != null) {
            this.mDataSchemeSpecificParts = new ArrayList(arrayList);
        }
        arrayList = o.mDataAuthorities;
        if (arrayList != null) {
            this.mDataAuthorities = new ArrayList(arrayList);
        }
        arrayList = o.mDataPaths;
        if (arrayList != null) {
            this.mDataPaths = new ArrayList(arrayList);
        }
        this.mHasPartialTypes = o.mHasPartialTypes;
        this.mVerifyState = o.mVerifyState;
        this.mInstantAppVisibility = o.mInstantAppVisibility;
    }

    public final void setPriority(int priority) {
        this.mPriority = priority;
    }

    public final int getPriority() {
        return this.mPriority;
    }

    @SystemApi
    public final void setOrder(int order) {
        this.mOrder = order;
    }

    @SystemApi
    public final int getOrder() {
        return this.mOrder;
    }

    @UnsupportedAppUsage
    public final void setAutoVerify(boolean autoVerify) {
        this.mVerifyState &= -2;
        if (autoVerify) {
            this.mVerifyState |= 1;
        }
    }

    public final boolean getAutoVerify() {
        return (this.mVerifyState & 1) == 1;
    }

    public final boolean handleAllWebDataURI() {
        if (hasCategory(Intent.CATEGORY_APP_BROWSER) || (handlesWebUris(false) && countDataAuthorities() == 0)) {
            return true;
        }
        return false;
    }

    public final boolean handlesWebUris(boolean onlyWebSchemes) {
        if (hasAction("android.intent.action.VIEW") && hasCategory(Intent.CATEGORY_BROWSABLE)) {
            ArrayList arrayList = this.mDataSchemes;
            if (!(arrayList == null || arrayList.size() == 0)) {
                int N = this.mDataSchemes.size();
                for (int i = 0; i < N; i++) {
                    String scheme = (String) this.mDataSchemes.get(i);
                    boolean isWebScheme = SCHEME_HTTP.equals(scheme) || SCHEME_HTTPS.equals(scheme);
                    if (onlyWebSchemes) {
                        if (!isWebScheme) {
                            return false;
                        }
                    } else if (isWebScheme) {
                        return true;
                    }
                }
                return onlyWebSchemes;
            }
        }
        return false;
    }

    public final boolean needsVerification() {
        return getAutoVerify() && handlesWebUris(true);
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public final boolean isVerified() {
        int i = this.mVerifyState;
        boolean z = false;
        if ((i & 256) != 256) {
            return false;
        }
        if ((i & 16) == 16) {
            z = true;
        }
        return z;
    }

    public void setVerified(boolean verified) {
        this.mVerifyState |= 256;
        this.mVerifyState &= -4097;
        if (verified) {
            this.mVerifyState |= 4096;
        }
    }

    public void setVisibilityToInstantApp(int visibility) {
        this.mInstantAppVisibility = visibility;
    }

    public int getVisibilityToInstantApp() {
        return this.mInstantAppVisibility;
    }

    public boolean isVisibleToInstantApp() {
        return this.mInstantAppVisibility != 0;
    }

    public boolean isExplicitlyVisibleToInstantApp() {
        return this.mInstantAppVisibility == 1;
    }

    public boolean isImplicitlyVisibleToInstantApp() {
        return this.mInstantAppVisibility == 2;
    }

    public final void addAction(String action) {
        if (!this.mActions.contains(action)) {
            this.mActions.add(action.intern());
        }
    }

    public final int countActions() {
        return this.mActions.size();
    }

    public final String getAction(int index) {
        return (String) this.mActions.get(index);
    }

    public final boolean hasAction(String action) {
        return action != null && this.mActions.contains(action);
    }

    public final boolean matchAction(String action) {
        return hasAction(action);
    }

    public final Iterator<String> actionsIterator() {
        ArrayList arrayList = this.mActions;
        return arrayList != null ? arrayList.iterator() : null;
    }

    public final void addDataType(String type) throws MalformedMimeTypeException {
        int slashpos = type.indexOf(47);
        int typelen = type.length();
        if (slashpos <= 0 || typelen < slashpos + 2) {
            throw new MalformedMimeTypeException(type);
        }
        if (this.mDataTypes == null) {
            this.mDataTypes = new ArrayList();
        }
        if (typelen == slashpos + 2 && type.charAt(slashpos + 1) == '*') {
            String str = type.substring(null, slashpos);
            if (!this.mDataTypes.contains(str)) {
                this.mDataTypes.add(str.intern());
            }
            this.mHasPartialTypes = true;
        } else if (!this.mDataTypes.contains(type)) {
            this.mDataTypes.add(type.intern());
        }
    }

    public final boolean hasDataType(String type) {
        return this.mDataTypes != null && findMimeType(type);
    }

    @UnsupportedAppUsage
    public final boolean hasExactDataType(String type) {
        ArrayList arrayList = this.mDataTypes;
        return arrayList != null && arrayList.contains(type);
    }

    public final int countDataTypes() {
        ArrayList arrayList = this.mDataTypes;
        return arrayList != null ? arrayList.size() : 0;
    }

    public final String getDataType(int index) {
        return (String) this.mDataTypes.get(index);
    }

    public final Iterator<String> typesIterator() {
        ArrayList arrayList = this.mDataTypes;
        return arrayList != null ? arrayList.iterator() : null;
    }

    public final void addDataScheme(String scheme) {
        if (this.mDataSchemes == null) {
            this.mDataSchemes = new ArrayList();
        }
        if (!this.mDataSchemes.contains(scheme)) {
            this.mDataSchemes.add(scheme.intern());
        }
    }

    public final int countDataSchemes() {
        ArrayList arrayList = this.mDataSchemes;
        return arrayList != null ? arrayList.size() : 0;
    }

    public final String getDataScheme(int index) {
        return (String) this.mDataSchemes.get(index);
    }

    public final boolean hasDataScheme(String scheme) {
        ArrayList arrayList = this.mDataSchemes;
        return arrayList != null && arrayList.contains(scheme);
    }

    public final Iterator<String> schemesIterator() {
        ArrayList arrayList = this.mDataSchemes;
        return arrayList != null ? arrayList.iterator() : null;
    }

    public final void addDataSchemeSpecificPart(String ssp, int type) {
        addDataSchemeSpecificPart(new PatternMatcher(ssp, type));
    }

    public final void addDataSchemeSpecificPart(PatternMatcher ssp) {
        if (this.mDataSchemeSpecificParts == null) {
            this.mDataSchemeSpecificParts = new ArrayList();
        }
        this.mDataSchemeSpecificParts.add(ssp);
    }

    public final int countDataSchemeSpecificParts() {
        ArrayList arrayList = this.mDataSchemeSpecificParts;
        return arrayList != null ? arrayList.size() : 0;
    }

    public final PatternMatcher getDataSchemeSpecificPart(int index) {
        return (PatternMatcher) this.mDataSchemeSpecificParts.get(index);
    }

    public final boolean hasDataSchemeSpecificPart(String data) {
        int numDataSchemeSpecificParts = this.mDataSchemeSpecificParts;
        if (numDataSchemeSpecificParts == 0) {
            return false;
        }
        numDataSchemeSpecificParts = numDataSchemeSpecificParts.size();
        for (int i = 0; i < numDataSchemeSpecificParts; i++) {
            if (((PatternMatcher) this.mDataSchemeSpecificParts.get(i)).match(data)) {
                return true;
            }
        }
        return false;
    }

    @UnsupportedAppUsage
    public final boolean hasDataSchemeSpecificPart(PatternMatcher ssp) {
        int numDataSchemeSpecificParts = this.mDataSchemeSpecificParts;
        if (numDataSchemeSpecificParts == 0) {
            return false;
        }
        numDataSchemeSpecificParts = numDataSchemeSpecificParts.size();
        for (int i = 0; i < numDataSchemeSpecificParts; i++) {
            PatternMatcher pe = (PatternMatcher) this.mDataSchemeSpecificParts.get(i);
            if (pe.getType() == ssp.getType() && pe.getPath().equals(ssp.getPath())) {
                return true;
            }
        }
        return false;
    }

    public final Iterator<PatternMatcher> schemeSpecificPartsIterator() {
        ArrayList arrayList = this.mDataSchemeSpecificParts;
        return arrayList != null ? arrayList.iterator() : null;
    }

    public final void addDataAuthority(String host, String port) {
        if (port != null) {
            port = port.intern();
        }
        addDataAuthority(new AuthorityEntry(host.intern(), port));
    }

    public final void addDataAuthority(AuthorityEntry ent) {
        if (this.mDataAuthorities == null) {
            this.mDataAuthorities = new ArrayList();
        }
        this.mDataAuthorities.add(ent);
    }

    public final int countDataAuthorities() {
        ArrayList arrayList = this.mDataAuthorities;
        return arrayList != null ? arrayList.size() : 0;
    }

    public final AuthorityEntry getDataAuthority(int index) {
        return (AuthorityEntry) this.mDataAuthorities.get(index);
    }

    public final boolean hasDataAuthority(Uri data) {
        return matchDataAuthority(data) >= 0;
    }

    @UnsupportedAppUsage
    public final boolean hasDataAuthority(AuthorityEntry auth) {
        int numDataAuthorities = this.mDataAuthorities;
        if (numDataAuthorities == 0) {
            return false;
        }
        numDataAuthorities = numDataAuthorities.size();
        for (int i = 0; i < numDataAuthorities; i++) {
            if (((AuthorityEntry) this.mDataAuthorities.get(i)).match(auth)) {
                return true;
            }
        }
        return false;
    }

    public final Iterator<AuthorityEntry> authoritiesIterator() {
        ArrayList arrayList = this.mDataAuthorities;
        return arrayList != null ? arrayList.iterator() : null;
    }

    public final void addDataPath(String path, int type) {
        addDataPath(new PatternMatcher(path.intern(), type));
    }

    public final void addDataPath(PatternMatcher path) {
        if (this.mDataPaths == null) {
            this.mDataPaths = new ArrayList();
        }
        this.mDataPaths.add(path);
    }

    public final int countDataPaths() {
        ArrayList arrayList = this.mDataPaths;
        return arrayList != null ? arrayList.size() : 0;
    }

    public final PatternMatcher getDataPath(int index) {
        return (PatternMatcher) this.mDataPaths.get(index);
    }

    public final boolean hasDataPath(String data) {
        int numDataPaths = this.mDataPaths;
        if (numDataPaths == 0) {
            return false;
        }
        numDataPaths = numDataPaths.size();
        for (int i = 0; i < numDataPaths; i++) {
            if (((PatternMatcher) this.mDataPaths.get(i)).match(data)) {
                return true;
            }
        }
        return false;
    }

    @UnsupportedAppUsage
    public final boolean hasDataPath(PatternMatcher path) {
        int numDataPaths = this.mDataPaths;
        if (numDataPaths == 0) {
            return false;
        }
        numDataPaths = numDataPaths.size();
        for (int i = 0; i < numDataPaths; i++) {
            PatternMatcher pe = (PatternMatcher) this.mDataPaths.get(i);
            if (pe.getType() == path.getType() && pe.getPath().equals(path.getPath())) {
                return true;
            }
        }
        return false;
    }

    public final Iterator<PatternMatcher> pathsIterator() {
        ArrayList arrayList = this.mDataPaths;
        return arrayList != null ? arrayList.iterator() : null;
    }

    public final int matchDataAuthority(Uri data) {
        int numDataAuthorities = this.mDataAuthorities;
        if (numDataAuthorities == 0 || data == null) {
            return -2;
        }
        numDataAuthorities = numDataAuthorities.size();
        for (int i = 0; i < numDataAuthorities; i++) {
            int match = ((AuthorityEntry) this.mDataAuthorities.get(i)).match(data);
            if (match >= 0) {
                return match;
            }
        }
        return -2;
    }

    public final int matchData(String type, String scheme, Uri data) {
        ArrayList<String> types = this.mDataTypes;
        ArrayList<String> schemes = this.mDataSchemes;
        int match = 1048576;
        int i = -2;
        if (types == null && schemes == null) {
            if (type == null && data == null) {
                i = 1081344;
            }
            return i;
        }
        Object obj = "";
        if (schemes != null) {
            if (scheme != null) {
                obj = scheme;
            }
            if (!schemes.contains(obj)) {
                return -2;
            }
            match = 2097152;
            if (!(this.mDataSchemeSpecificParts == null || data == null)) {
                match = hasDataSchemeSpecificPart(data.getSchemeSpecificPart()) ? 5767168 : -2;
            }
            if (!(match == MATCH_CATEGORY_SCHEME_SPECIFIC_PART || this.mDataAuthorities == null)) {
                int authMatch = matchDataAuthority(data);
                if (authMatch < 0) {
                    return -2;
                }
                if (this.mDataPaths == null) {
                    match = authMatch;
                } else if (!hasDataPath(data.getPath())) {
                    return -2;
                } else {
                    match = MATCH_CATEGORY_PATH;
                }
            }
            if (match == -2) {
                return -2;
            }
        } else if (!(scheme == null || obj.equals(scheme) || "content".equals(scheme) || ContentResolver.SCHEME_FILE.equals(scheme))) {
            return -2;
        }
        if (types != null) {
            if (!findMimeType(type)) {
                return -1;
            }
            match = MATCH_CATEGORY_TYPE;
        } else if (type != null) {
            return -1;
        }
        return 32768 + match;
    }

    public final void addCategory(String category) {
        if (this.mCategories == null) {
            this.mCategories = new ArrayList();
        }
        if (!this.mCategories.contains(category)) {
            this.mCategories.add(category.intern());
        }
    }

    public final int countCategories() {
        ArrayList arrayList = this.mCategories;
        return arrayList != null ? arrayList.size() : 0;
    }

    public final String getCategory(int index) {
        return (String) this.mCategories.get(index);
    }

    public final boolean hasCategory(String category) {
        ArrayList arrayList = this.mCategories;
        return arrayList != null && arrayList.contains(category);
    }

    public final Iterator<String> categoriesIterator() {
        ArrayList arrayList = this.mCategories;
        return arrayList != null ? arrayList.iterator() : null;
    }

    public final String matchCategories(Set<String> categories) {
        String str = null;
        if (categories == null) {
            return null;
        }
        Iterator<String> it = categories.iterator();
        if (this.mCategories == null) {
            if (it.hasNext()) {
                str = (String) it.next();
            }
            return str;
        }
        while (it.hasNext()) {
            String category = (String) it.next();
            if (!this.mCategories.contains(category)) {
                return category;
            }
        }
        return null;
    }

    public final int match(ContentResolver resolver, Intent intent, boolean resolve, String logTag) {
        return match(intent.getAction(), resolve ? intent.resolveType(resolver) : intent.getType(), intent.getScheme(), intent.getData(), intent.getCategories(), logTag);
    }

    public final int match(String action, String type, String scheme, Uri data, Set<String> categories, String logTag) {
        if (action != null && !matchAction(action)) {
            return -3;
        }
        int dataMatch = matchData(type, scheme, data);
        if (dataMatch >= 0 && matchCategories(categories) != null) {
            return -4;
        }
        return dataMatch;
    }

    public void writeToXml(XmlSerializer serializer) throws IOException {
        String str;
        String str2;
        String type;
        String str3;
        String str4;
        if (getAutoVerify()) {
            serializer.attribute(null, AUTO_VERIFY_STR, Boolean.toString(true));
        }
        int N = countActions();
        int i = 0;
        while (true) {
            str = "name";
            if (i >= N) {
                break;
            }
            str2 = "action";
            serializer.startTag(null, str2);
            serializer.attribute(null, str, (String) this.mActions.get(i));
            serializer.endTag(null, str2);
            i++;
        }
        N = countCategories();
        for (i = 0; i < N; i++) {
            str2 = CAT_STR;
            serializer.startTag(null, str2);
            serializer.attribute(null, str, (String) this.mCategories.get(i));
            serializer.endTag(null, str2);
        }
        N = countDataTypes();
        for (i = 0; i < N; i++) {
            str2 = "type";
            serializer.startTag(null, str2);
            type = (String) this.mDataTypes.get(i);
            if (type.indexOf(47) < 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(type);
                stringBuilder.append("/*");
                type = stringBuilder.toString();
            }
            serializer.attribute(null, str, type);
            serializer.endTag(null, str2);
        }
        N = countDataSchemes();
        for (i = 0; i < N; i++) {
            str2 = SCHEME_STR;
            serializer.startTag(null, str2);
            serializer.attribute(null, str, (String) this.mDataSchemes.get(i));
            serializer.endTag(null, str2);
        }
        N = countDataSchemeSpecificParts();
        i = 0;
        while (true) {
            str = AGLOB_STR;
            str2 = SGLOB_STR;
            type = PREFIX_STR;
            str3 = LITERAL_STR;
            if (i >= N) {
                break;
            }
            str4 = SSP_STR;
            serializer.startTag(null, str4);
            PatternMatcher pe = (PatternMatcher) this.mDataSchemeSpecificParts.get(i);
            int type2 = pe.getType();
            if (type2 == 0) {
                serializer.attribute(null, str3, pe.getPath());
            } else if (type2 == 1) {
                serializer.attribute(null, type, pe.getPath());
            } else if (type2 == 2) {
                serializer.attribute(null, str2, pe.getPath());
            } else if (type2 == 3) {
                serializer.attribute(null, str, pe.getPath());
            }
            serializer.endTag(null, str4);
            i++;
        }
        N = countDataAuthorities();
        for (i = 0; i < N; i++) {
            str4 = AUTH_STR;
            serializer.startTag(null, str4);
            AuthorityEntry ae = (AuthorityEntry) this.mDataAuthorities.get(i);
            serializer.attribute(null, HOST_STR, ae.getHost());
            if (ae.getPort() >= 0) {
                serializer.attribute(null, "port", Integer.toString(ae.getPort()));
            }
            serializer.endTag(null, str4);
        }
        N = countDataPaths();
        for (i = 0; i < N; i++) {
            serializer.startTag(null, "path");
            PatternMatcher pe2 = (PatternMatcher) this.mDataPaths.get(i);
            int type3 = pe2.getType();
            if (type3 == 0) {
                serializer.attribute(null, str3, pe2.getPath());
            } else if (type3 == 1) {
                serializer.attribute(null, type, pe2.getPath());
            } else if (type3 == 2) {
                serializer.attribute(null, str2, pe2.getPath());
            } else if (type3 == 3) {
                serializer.attribute(null, str, pe2.getPath());
            }
            serializer.endTag(null, "path");
        }
    }

    public void readFromXml(XmlPullParser parser) throws XmlPullParserException, IOException {
        String autoVerify = parser.getAttributeValue(null, AUTO_VERIFY_STR);
        setAutoVerify(TextUtils.isEmpty(autoVerify) ? false : Boolean.getBoolean(autoVerify));
        int outerDepth = parser.getDepth();
        while (true) {
            int next = parser.next();
            int type = next;
            if (next == 1) {
                return;
            }
            if (type == 3 && parser.getDepth() <= outerDepth) {
                return;
            }
            if (type != 3) {
                if (type != 4) {
                    String tagName = parser.getName();
                    String str = "name";
                    String name;
                    if (tagName.equals("action")) {
                        name = parser.getAttributeValue(null, str);
                        if (name != null) {
                            addAction(name);
                        }
                    } else if (tagName.equals(CAT_STR)) {
                        name = parser.getAttributeValue(null, str);
                        if (name != null) {
                            addCategory(name);
                        }
                    } else if (tagName.equals("type")) {
                        name = parser.getAttributeValue(null, str);
                        if (name != null) {
                            try {
                                addDataType(name);
                            } catch (MalformedMimeTypeException e) {
                            }
                        }
                    } else if (tagName.equals(SCHEME_STR)) {
                        name = parser.getAttributeValue(null, str);
                        if (name != null) {
                            addDataScheme(name);
                        }
                    } else {
                        boolean equals = tagName.equals(SSP_STR);
                        String str2 = AGLOB_STR;
                        String str3 = SGLOB_STR;
                        String str4 = PREFIX_STR;
                        String str5 = LITERAL_STR;
                        String ssp;
                        String attributeValue;
                        if (equals) {
                            ssp = parser.getAttributeValue(null, str5);
                            if (ssp != null) {
                                addDataSchemeSpecificPart(ssp, 0);
                            } else {
                                str4 = parser.getAttributeValue(null, str4);
                                ssp = str4;
                                if (str4 != null) {
                                    addDataSchemeSpecificPart(ssp, 1);
                                } else {
                                    attributeValue = parser.getAttributeValue(null, str3);
                                    ssp = attributeValue;
                                    if (attributeValue != null) {
                                        addDataSchemeSpecificPart(ssp, 2);
                                    } else {
                                        attributeValue = parser.getAttributeValue(null, str2);
                                        ssp = attributeValue;
                                        if (attributeValue != null) {
                                            addDataSchemeSpecificPart(ssp, 3);
                                        }
                                    }
                                }
                            }
                        } else if (tagName.equals(AUTH_STR)) {
                            name = parser.getAttributeValue(null, HOST_STR);
                            attributeValue = parser.getAttributeValue(null, "port");
                            if (name != null) {
                                addDataAuthority(name, attributeValue);
                            }
                        } else if (tagName.equals("path")) {
                            ssp = parser.getAttributeValue(null, str5);
                            if (ssp != null) {
                                addDataPath(ssp, 0);
                            } else {
                                str4 = parser.getAttributeValue(null, str4);
                                ssp = str4;
                                if (str4 != null) {
                                    addDataPath(ssp, 1);
                                } else {
                                    attributeValue = parser.getAttributeValue(null, str3);
                                    ssp = attributeValue;
                                    if (attributeValue != null) {
                                        addDataPath(ssp, 2);
                                    } else {
                                        attributeValue = parser.getAttributeValue(null, str2);
                                        ssp = attributeValue;
                                        if (attributeValue != null) {
                                            addDataPath(ssp, 3);
                                        }
                                    }
                                }
                            }
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Unknown tag parsing IntentFilter: ");
                            stringBuilder.append(tagName);
                            Log.w("IntentFilter", stringBuilder.toString());
                        }
                    }
                    XmlUtils.skipCurrentTag(parser);
                }
            }
        }
    }

    public void writeToProto(ProtoOutputStream proto, long fieldId) {
        Iterator<String> it;
        long token = proto.start(fieldId);
        if (this.mActions.size() > 0) {
            it = this.mActions.iterator();
            while (it.hasNext()) {
                proto.write(2237677961217L, (String) it.next());
            }
        }
        ArrayList arrayList = this.mCategories;
        if (arrayList != null) {
            it = arrayList.iterator();
            while (it.hasNext()) {
                proto.write(2237677961218L, (String) it.next());
            }
        }
        arrayList = this.mDataSchemes;
        if (arrayList != null) {
            it = arrayList.iterator();
            while (it.hasNext()) {
                proto.write(2237677961219L, (String) it.next());
            }
        }
        Iterator<PatternMatcher> it2 = this.mDataSchemeSpecificParts;
        if (it2 != null) {
            it2 = it2.iterator();
            while (it2.hasNext()) {
                ((PatternMatcher) it2.next()).writeToProto(proto, 2246267895812L);
            }
        }
        Iterator<AuthorityEntry> it3 = this.mDataAuthorities;
        if (it3 != null) {
            it3 = it3.iterator();
            while (it3.hasNext()) {
                ((AuthorityEntry) it3.next()).writeToProto(proto, 2246267895813L);
            }
        }
        it2 = this.mDataPaths;
        if (it2 != null) {
            it2 = it2.iterator();
            while (it2.hasNext()) {
                ((PatternMatcher) it2.next()).writeToProto(proto, 2246267895814L);
            }
        }
        it = this.mDataTypes;
        if (it != null) {
            it = it.iterator();
            while (it.hasNext()) {
                proto.write(2237677961223L, (String) it.next());
            }
        }
        if (this.mPriority != 0 || this.mHasPartialTypes) {
            proto.write(1120986464264L, this.mPriority);
            proto.write(1133871366153L, this.mHasPartialTypes);
        }
        proto.write(1133871366154L, getAutoVerify());
        proto.end(token);
    }

    public void dump(Printer du, String prefix) {
        Iterator<String> it;
        PatternMatcher pe;
        StringBuilder sb = new StringBuilder(256);
        String str = "\"";
        if (this.mActions.size() > 0) {
            it = this.mActions.iterator();
            while (it.hasNext()) {
                sb.setLength(0);
                sb.append(prefix);
                sb.append("Action: \"");
                sb.append((String) it.next());
                sb.append(str);
                du.println(sb.toString());
            }
        }
        ArrayList arrayList = this.mCategories;
        if (arrayList != null) {
            it = arrayList.iterator();
            while (it.hasNext()) {
                sb.setLength(0);
                sb.append(prefix);
                sb.append("Category: \"");
                sb.append((String) it.next());
                sb.append(str);
                du.println(sb.toString());
            }
        }
        arrayList = this.mDataSchemes;
        if (arrayList != null) {
            it = arrayList.iterator();
            while (it.hasNext()) {
                sb.setLength(0);
                sb.append(prefix);
                sb.append("Scheme: \"");
                sb.append((String) it.next());
                sb.append(str);
                du.println(sb.toString());
            }
        }
        Iterator<PatternMatcher> it2 = this.mDataSchemeSpecificParts;
        if (it2 != null) {
            it2 = it2.iterator();
            while (it2.hasNext()) {
                pe = (PatternMatcher) it2.next();
                sb.setLength(0);
                sb.append(prefix);
                sb.append("Ssp: \"");
                sb.append(pe);
                sb.append(str);
                du.println(sb.toString());
            }
        }
        Iterator<AuthorityEntry> it3 = this.mDataAuthorities;
        if (it3 != null) {
            it3 = it3.iterator();
            while (it3.hasNext()) {
                AuthorityEntry ae = (AuthorityEntry) it3.next();
                sb.setLength(0);
                sb.append(prefix);
                sb.append("Authority: \"");
                sb.append(ae.mHost);
                sb.append("\": ");
                sb.append(ae.mPort);
                if (ae.mWild) {
                    sb.append(" WILD");
                }
                du.println(sb.toString());
            }
        }
        it2 = this.mDataPaths;
        if (it2 != null) {
            it2 = it2.iterator();
            while (it2.hasNext()) {
                pe = (PatternMatcher) it2.next();
                sb.setLength(0);
                sb.append(prefix);
                sb.append("Path: \"");
                sb.append(pe);
                sb.append(str);
                du.println(sb.toString());
            }
        }
        it = this.mDataTypes;
        if (it != null) {
            it = it.iterator();
            while (it.hasNext()) {
                sb.setLength(0);
                sb.append(prefix);
                sb.append("Type: \"");
                sb.append((String) it.next());
                sb.append(str);
                du.println(sb.toString());
            }
        }
        if (!(this.mPriority == 0 && this.mOrder == 0 && !this.mHasPartialTypes)) {
            sb.setLength(0);
            sb.append(prefix);
            sb.append("mPriority=");
            sb.append(this.mPriority);
            sb.append(", mOrder=");
            sb.append(this.mOrder);
            sb.append(", mHasPartialTypes=");
            sb.append(this.mHasPartialTypes);
            du.println(sb.toString());
        }
        if (getAutoVerify()) {
            sb.setLength(0);
            sb.append(prefix);
            sb.append("AutoVerify=");
            sb.append(getAutoVerify());
            du.println(sb.toString());
        }
    }

    public final int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel dest, int flags) {
        int i;
        dest.writeStringList(this.mActions);
        if (this.mCategories != null) {
            dest.writeInt(1);
            dest.writeStringList(this.mCategories);
        } else {
            dest.writeInt(0);
        }
        if (this.mDataSchemes != null) {
            dest.writeInt(1);
            dest.writeStringList(this.mDataSchemes);
        } else {
            dest.writeInt(0);
        }
        if (this.mDataTypes != null) {
            dest.writeInt(1);
            dest.writeStringList(this.mDataTypes);
        } else {
            dest.writeInt(0);
        }
        int N = this.mDataSchemeSpecificParts;
        if (N != 0) {
            N = N.size();
            dest.writeInt(N);
            for (i = 0; i < N; i++) {
                ((PatternMatcher) this.mDataSchemeSpecificParts.get(i)).writeToParcel(dest, flags);
            }
        } else {
            dest.writeInt(0);
        }
        ArrayList arrayList = this.mDataAuthorities;
        if (arrayList != null) {
            N = arrayList.size();
            dest.writeInt(N);
            for (i = 0; i < N; i++) {
                ((AuthorityEntry) this.mDataAuthorities.get(i)).writeToParcel(dest);
            }
        } else {
            dest.writeInt(0);
        }
        arrayList = this.mDataPaths;
        if (arrayList != null) {
            N = arrayList.size();
            dest.writeInt(N);
            for (i = 0; i < N; i++) {
                ((PatternMatcher) this.mDataPaths.get(i)).writeToParcel(dest, flags);
            }
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(this.mPriority);
        dest.writeInt(this.mHasPartialTypes);
        dest.writeInt(getAutoVerify());
        dest.writeInt(this.mInstantAppVisibility);
        dest.writeInt(this.mOrder);
    }

    public boolean debugCheck() {
        return true;
    }

    public IntentFilter(Parcel source) {
        int i;
        this.mCategories = null;
        this.mDataSchemes = null;
        this.mDataSchemeSpecificParts = null;
        this.mDataAuthorities = null;
        this.mDataPaths = null;
        this.mDataTypes = null;
        boolean z = false;
        this.mHasPartialTypes = false;
        this.mActions = new ArrayList();
        source.readStringList(this.mActions);
        if (source.readInt() != 0) {
            this.mCategories = new ArrayList();
            source.readStringList(this.mCategories);
        }
        if (source.readInt() != 0) {
            this.mDataSchemes = new ArrayList();
            source.readStringList(this.mDataSchemes);
        }
        if (source.readInt() != 0) {
            this.mDataTypes = new ArrayList();
            source.readStringList(this.mDataTypes);
        }
        int N = source.readInt();
        if (N > 0) {
            this.mDataSchemeSpecificParts = new ArrayList(N);
            for (i = 0; i < N; i++) {
                this.mDataSchemeSpecificParts.add(new PatternMatcher(source));
            }
        }
        N = source.readInt();
        if (N > 0) {
            this.mDataAuthorities = new ArrayList(N);
            for (i = 0; i < N; i++) {
                this.mDataAuthorities.add(new AuthorityEntry(source));
            }
        }
        N = source.readInt();
        if (N > 0) {
            this.mDataPaths = new ArrayList(N);
            for (i = 0; i < N; i++) {
                this.mDataPaths.add(new PatternMatcher(source));
            }
        }
        this.mPriority = source.readInt();
        this.mHasPartialTypes = source.readInt() > 0;
        if (source.readInt() > 0) {
            z = true;
        }
        setAutoVerify(z);
        setVisibilityToInstantApp(source.readInt());
        this.mOrder = source.readInt();
    }

    private final boolean findMimeType(String type) {
        ArrayList<String> t = this.mDataTypes;
        if (type == null) {
            return false;
        }
        if (t.contains(type)) {
            return true;
        }
        int typeLength = type.length();
        if (typeLength == 3 && type.equals("*/*")) {
            return t.isEmpty() ^ 1;
        }
        if (this.mHasPartialTypes && t.contains("*")) {
            return true;
        }
        int slashpos = type.indexOf(47);
        if (slashpos > 0) {
            if (this.mHasPartialTypes && t.contains(type.substring(0, slashpos))) {
                return true;
            }
            if (typeLength == slashpos + 2 && type.charAt(slashpos + 1) == '*') {
                int numTypes = t.size();
                for (int i = 0; i < numTypes; i++) {
                    if (type.regionMatches(0, (String) t.get(i), 0, slashpos + 1)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ArrayList<String> getHostsList() {
        ArrayList<String> result = new ArrayList();
        Iterator<AuthorityEntry> it = authoritiesIterator();
        if (it != null) {
            while (it.hasNext()) {
                result.add(((AuthorityEntry) it.next()).getHost());
            }
        }
        return result;
    }

    public String[] getHosts() {
        ArrayList<String> list = getHostsList();
        return (String[]) list.toArray(new String[list.size()]);
    }
}
