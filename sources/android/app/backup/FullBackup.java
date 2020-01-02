package android.app.backup;

import android.annotation.UnsupportedAppUsage;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.XmlResourceParser;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.MediaStore;
import android.system.ErrnoException;
import android.system.Os;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class FullBackup {
    public static final String APK_TREE_TOKEN = "a";
    public static final String APPS_PREFIX = "apps/";
    public static final String CACHE_TREE_TOKEN = "c";
    public static final String CONF_TOKEN_INTENT_EXTRA = "conftoken";
    public static final String DATABASE_TREE_TOKEN = "db";
    public static final String DEVICE_CACHE_TREE_TOKEN = "d_c";
    public static final String DEVICE_DATABASE_TREE_TOKEN = "d_db";
    public static final String DEVICE_FILES_TREE_TOKEN = "d_f";
    public static final String DEVICE_NO_BACKUP_TREE_TOKEN = "d_nb";
    public static final String DEVICE_ROOT_TREE_TOKEN = "d_r";
    public static final String DEVICE_SHAREDPREFS_TREE_TOKEN = "d_sp";
    public static final String FILES_TREE_TOKEN = "f";
    public static final String FLAG_REQUIRED_CLIENT_SIDE_ENCRYPTION = "clientSideEncryption";
    public static final String FLAG_REQUIRED_DEVICE_TO_DEVICE_TRANSFER = "deviceToDeviceTransfer";
    public static final String FLAG_REQUIRED_FAKE_CLIENT_SIDE_ENCRYPTION = "fakeClientSideEncryption";
    public static final String FULL_BACKUP_INTENT_ACTION = "fullback";
    public static final String FULL_RESTORE_INTENT_ACTION = "fullrest";
    public static final String KEY_VALUE_DATA_TOKEN = "k";
    public static final String MANAGED_EXTERNAL_TREE_TOKEN = "ef";
    public static final String NO_BACKUP_TREE_TOKEN = "nb";
    public static final String OBB_TREE_TOKEN = "obb";
    public static final String ROOT_TREE_TOKEN = "r";
    public static final String SHAREDPREFS_TREE_TOKEN = "sp";
    public static final String SHARED_PREFIX = "shared/";
    public static final String SHARED_STORAGE_TOKEN = "shared";
    static final String TAG = "FullBackup";
    static final String TAG_XML_PARSER = "BackupXmlParserLogging";
    private static final Map<String, BackupScheme> kPackageBackupSchemeMap = new ArrayMap();

    @VisibleForTesting
    public static class BackupScheme {
        private static final String TAG_EXCLUDE = "exclude";
        private static final String TAG_INCLUDE = "include";
        private final File CACHE_DIR;
        private final File DATABASE_DIR;
        private final File DEVICE_CACHE_DIR;
        private final File DEVICE_DATABASE_DIR;
        private final File DEVICE_FILES_DIR;
        private final File DEVICE_NOBACKUP_DIR;
        private final File DEVICE_ROOT_DIR;
        private final File DEVICE_SHAREDPREF_DIR;
        private final File EXTERNAL_DIR;
        private final File FILES_DIR;
        private final File NOBACKUP_DIR;
        private final File ROOT_DIR;
        private final File SHAREDPREF_DIR;
        ArraySet<PathWithRequiredFlags> mExcludes;
        final int mFullBackupContent;
        Map<String, Set<PathWithRequiredFlags>> mIncludes;
        final PackageManager mPackageManager;
        final String mPackageName;
        final StorageManager mStorageManager;
        private StorageVolume[] mVolumes = null;

        public static class PathWithRequiredFlags {
            private final String mPath;
            private final int mRequiredFlags;

            public PathWithRequiredFlags(String path, int requiredFlags) {
                this.mPath = path;
                this.mRequiredFlags = requiredFlags;
            }

            public String getPath() {
                return this.mPath;
            }

            public int getRequiredFlags() {
                return this.mRequiredFlags;
            }
        }

        /* Access modifiers changed, original: 0000 */
        public String tokenToDirectoryPath(String domainToken) {
            String str = FullBackup.TAG;
            try {
                if (domainToken.equals(FullBackup.FILES_TREE_TOKEN)) {
                    return this.FILES_DIR.getCanonicalPath();
                }
                if (domainToken.equals(FullBackup.DATABASE_TREE_TOKEN)) {
                    return this.DATABASE_DIR.getCanonicalPath();
                }
                if (domainToken.equals("r")) {
                    return this.ROOT_DIR.getCanonicalPath();
                }
                if (domainToken.equals(FullBackup.SHAREDPREFS_TREE_TOKEN)) {
                    return this.SHAREDPREF_DIR.getCanonicalPath();
                }
                if (domainToken.equals(FullBackup.CACHE_TREE_TOKEN)) {
                    return this.CACHE_DIR.getCanonicalPath();
                }
                if (domainToken.equals(FullBackup.NO_BACKUP_TREE_TOKEN)) {
                    return this.NOBACKUP_DIR.getCanonicalPath();
                }
                if (domainToken.equals(FullBackup.DEVICE_FILES_TREE_TOKEN)) {
                    return this.DEVICE_FILES_DIR.getCanonicalPath();
                }
                if (domainToken.equals(FullBackup.DEVICE_DATABASE_TREE_TOKEN)) {
                    return this.DEVICE_DATABASE_DIR.getCanonicalPath();
                }
                if (domainToken.equals(FullBackup.DEVICE_ROOT_TREE_TOKEN)) {
                    return this.DEVICE_ROOT_DIR.getCanonicalPath();
                }
                if (domainToken.equals(FullBackup.DEVICE_SHAREDPREFS_TREE_TOKEN)) {
                    return this.DEVICE_SHAREDPREF_DIR.getCanonicalPath();
                }
                if (domainToken.equals(FullBackup.DEVICE_CACHE_TREE_TOKEN)) {
                    return this.DEVICE_CACHE_DIR.getCanonicalPath();
                }
                if (domainToken.equals(FullBackup.DEVICE_NO_BACKUP_TREE_TOKEN)) {
                    return this.DEVICE_NOBACKUP_DIR.getCanonicalPath();
                }
                if (domainToken.equals(FullBackup.MANAGED_EXTERNAL_TREE_TOKEN)) {
                    if (this.EXTERNAL_DIR != null) {
                        return this.EXTERNAL_DIR.getCanonicalPath();
                    }
                    return null;
                } else if (domainToken.startsWith(FullBackup.SHARED_PREFIX)) {
                    return sharedDomainToPath(domainToken);
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unrecognized domain ");
                    stringBuilder.append(domainToken);
                    Log.i(str, stringBuilder.toString());
                    return null;
                }
            } catch (Exception e) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Error reading directory for domain: ");
                stringBuilder2.append(domainToken);
                Log.i(str, stringBuilder2.toString());
                return null;
            }
        }

        private String sharedDomainToPath(String domain) throws IOException {
            String volume = domain.substring(FullBackup.SHARED_PREFIX.length());
            StorageVolume[] volumes = getVolumeList();
            int volNum = Integer.parseInt(volume);
            if (volNum < this.mVolumes.length) {
                return volumes[volNum].getPathFile().getCanonicalPath();
            }
            return null;
        }

        private StorageVolume[] getVolumeList() {
            StorageManager storageManager = this.mStorageManager;
            if (storageManager == null) {
                Log.e(FullBackup.TAG, "Unable to access Storage Manager");
            } else if (this.mVolumes == null) {
                this.mVolumes = storageManager.getVolumeList();
            }
            return this.mVolumes;
        }

        BackupScheme(Context context) {
            this.mFullBackupContent = context.getApplicationInfo().fullBackupContent;
            this.mStorageManager = (StorageManager) context.getSystemService("storage");
            this.mPackageManager = context.getPackageManager();
            this.mPackageName = context.getPackageName();
            Context ceContext = context.createCredentialProtectedStorageContext();
            this.FILES_DIR = ceContext.getFilesDir();
            String str = "foo";
            this.DATABASE_DIR = ceContext.getDatabasePath(str).getParentFile();
            this.ROOT_DIR = ceContext.getDataDir();
            this.SHAREDPREF_DIR = ceContext.getSharedPreferencesPath(str).getParentFile();
            this.CACHE_DIR = ceContext.getCacheDir();
            this.NOBACKUP_DIR = ceContext.getNoBackupFilesDir();
            Context deContext = context.createDeviceProtectedStorageContext();
            this.DEVICE_FILES_DIR = deContext.getFilesDir();
            this.DEVICE_DATABASE_DIR = deContext.getDatabasePath(str).getParentFile();
            this.DEVICE_ROOT_DIR = deContext.getDataDir();
            this.DEVICE_SHAREDPREF_DIR = deContext.getSharedPreferencesPath(str).getParentFile();
            this.DEVICE_CACHE_DIR = deContext.getCacheDir();
            this.DEVICE_NOBACKUP_DIR = deContext.getNoBackupFilesDir();
            if (Process.myUid() != 1000) {
                this.EXTERNAL_DIR = context.getExternalFilesDir(null);
            } else {
                this.EXTERNAL_DIR = null;
            }
        }

        /* Access modifiers changed, original: 0000 */
        public boolean isFullBackupContentEnabled() {
            if (this.mFullBackupContent >= 0) {
                return true;
            }
            String str = FullBackup.TAG_XML_PARSER;
            if (Log.isLoggable(str, 2)) {
                Log.v(str, "android:fullBackupContent - \"false\"");
            }
            return false;
        }

        public synchronized Map<String, Set<PathWithRequiredFlags>> maybeParseAndGetCanonicalIncludePaths() throws IOException, XmlPullParserException {
            if (this.mIncludes == null) {
                maybeParseBackupSchemeLocked();
            }
            return this.mIncludes;
        }

        public synchronized ArraySet<PathWithRequiredFlags> maybeParseAndGetCanonicalExcludePaths() throws IOException, XmlPullParserException {
            if (this.mExcludes == null) {
                maybeParseBackupSchemeLocked();
            }
            return this.mExcludes;
        }

        private void maybeParseBackupSchemeLocked() throws IOException, XmlPullParserException {
            this.mIncludes = new ArrayMap();
            this.mExcludes = new ArraySet();
            int i = this.mFullBackupContent;
            String str = FullBackup.TAG_XML_PARSER;
            if (i != 0) {
                if (Log.isLoggable(str, 2)) {
                    Log.v(str, "android:fullBackupContent - found xml resource");
                }
                XmlResourceParser parser = null;
                try {
                    parser = this.mPackageManager.getResourcesForApplication(this.mPackageName).getXml(this.mFullBackupContent);
                    parseBackupSchemeFromXmlLocked(parser, this.mExcludes, this.mIncludes);
                    if (parser != null) {
                        parser.close();
                    }
                } catch (NameNotFoundException e) {
                    throw new IOException(e);
                } catch (Throwable th) {
                    if (parser != null) {
                        parser.close();
                    }
                }
            } else if (Log.isLoggable(str, 2)) {
                Log.v(str, "android:fullBackupContent - \"true\"");
            }
        }

        @VisibleForTesting
        public void parseBackupSchemeFromXmlLocked(XmlPullParser parser, Set<PathWithRequiredFlags> excludes, Map<String, Set<PathWithRequiredFlags>> includes) throws IOException, XmlPullParserException {
            int i;
            BackupScheme backupScheme = this;
            XmlPullParser xmlPullParser = parser;
            int event = parser.getEventType();
            while (true) {
                i = 2;
                if (event == 2) {
                    break;
                }
                event = parser.next();
            }
            String str = "\"";
            Set<PathWithRequiredFlags> set;
            Map<String, Set<PathWithRequiredFlags>> map;
            if ("full-backup-content".equals(parser.getName())) {
                int event2;
                String canonicalJournalPath;
                String str2 = FullBackup.TAG_XML_PARSER;
                String str3 = "====================================================";
                String str4 = "\n";
                if (Log.isLoggable(str2, 2)) {
                    Log.v(str2, str4);
                    Log.v(str2, str3);
                    Log.v(str2, "Found valid fullBackupContent; parsing xml resource.");
                    Log.v(str2, str3);
                    Log.v(str2, "");
                }
                while (true) {
                    int next = parser.next();
                    event = next;
                    if (next == 1) {
                        break;
                    }
                    if (event != i) {
                        set = excludes;
                        map = includes;
                        event2 = event;
                    } else {
                        validateInnerTagContents(parser);
                        String domainFromXml = xmlPullParser.getAttributeValue(null, "domain");
                        File domainDirectory = backupScheme.getDirectoryForCriteriaDomain(domainFromXml);
                        if (domainDirectory != null) {
                            File canonicalFile = backupScheme.extractCanonicalFile(domainDirectory, xmlPullParser.getAttributeValue(null, "path"));
                            if (canonicalFile == null) {
                                set = excludes;
                                map = includes;
                                event2 = event;
                            } else {
                                StringBuilder stringBuilder;
                                int requiredFlags = 0;
                                if (TAG_INCLUDE.equals(parser.getName())) {
                                    requiredFlags = backupScheme.getRequiredFlagsFromString(xmlPullParser.getAttributeValue(null, "requireFlags"));
                                }
                                Set<PathWithRequiredFlags> activeSet = backupScheme.parseCurrentTagForDomain(xmlPullParser, excludes, includes, domainFromXml);
                                activeSet.add(new PathWithRequiredFlags(canonicalFile.getCanonicalPath(), requiredFlags));
                                if (Log.isLoggable(str2, 2)) {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("...parsed ");
                                    stringBuilder.append(canonicalFile.getCanonicalPath());
                                    stringBuilder.append(" for domain \"");
                                    stringBuilder.append(domainFromXml);
                                    stringBuilder.append("\", requiredFlags + \"");
                                    stringBuilder.append(requiredFlags);
                                    stringBuilder.append(str);
                                    Log.v(str2, stringBuilder.toString());
                                }
                                String str5 = ". Ignore if nonexistent.";
                                String str6 = "...automatically generated ";
                                if ("database".equals(domainFromXml) && !canonicalFile.isDirectory()) {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(canonicalFile.getCanonicalPath());
                                    stringBuilder.append("-journal");
                                    canonicalJournalPath = stringBuilder.toString();
                                    activeSet.add(new PathWithRequiredFlags(canonicalJournalPath, requiredFlags));
                                    if (Log.isLoggable(str2, 2)) {
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append(str6);
                                        stringBuilder.append(canonicalJournalPath);
                                        stringBuilder.append(str5);
                                        Log.v(str2, stringBuilder.toString());
                                    }
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(canonicalFile.getCanonicalPath());
                                    stringBuilder.append("-wal");
                                    canonicalJournalPath = stringBuilder.toString();
                                    activeSet.add(new PathWithRequiredFlags(canonicalJournalPath, requiredFlags));
                                    if (Log.isLoggable(str2, 2)) {
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append(str6);
                                        stringBuilder.append(canonicalJournalPath);
                                        stringBuilder.append(str5);
                                        Log.v(str2, stringBuilder.toString());
                                    }
                                }
                                if (!"sharedpref".equals(domainFromXml) || canonicalFile.isDirectory()) {
                                    event2 = event;
                                } else {
                                    String str7 = ".xml";
                                    if (canonicalFile.getCanonicalPath().endsWith(str7)) {
                                        event2 = event;
                                    } else {
                                        canonicalJournalPath = new StringBuilder();
                                        event2 = event;
                                        canonicalJournalPath.append(canonicalFile.getCanonicalPath());
                                        canonicalJournalPath.append(str7);
                                        canonicalJournalPath = canonicalJournalPath.toString();
                                        activeSet.add(new PathWithRequiredFlags(canonicalJournalPath, requiredFlags));
                                        if (Log.isLoggable(str2, 2)) {
                                            StringBuilder stringBuilder2 = new StringBuilder();
                                            stringBuilder2.append(str6);
                                            stringBuilder2.append(canonicalJournalPath);
                                            stringBuilder2.append(str5);
                                            Log.v(str2, stringBuilder2.toString());
                                        }
                                    }
                                }
                            }
                        } else if (Log.isLoggable(str2, i)) {
                            StringBuilder stringBuilder3 = new StringBuilder();
                            stringBuilder3.append("...parsing \"");
                            stringBuilder3.append(parser.getName());
                            stringBuilder3.append("\": domain=\"");
                            stringBuilder3.append(domainFromXml);
                            stringBuilder3.append("\" invalid; skipping");
                            Log.v(str2, stringBuilder3.toString());
                            set = excludes;
                            map = includes;
                            event2 = event;
                        } else {
                            set = excludes;
                            map = includes;
                            event2 = event;
                        }
                    }
                    i = 2;
                    backupScheme = this;
                    xmlPullParser = parser;
                    event = event2;
                }
                set = excludes;
                map = includes;
                event2 = event;
                if (Log.isLoggable(str2, 2)) {
                    StringBuilder stringBuilder4;
                    Log.v(str2, str4);
                    Log.v(str2, "Xml resource parsing complete.");
                    Log.v(str2, "Final tally.");
                    Log.v(str2, "Includes:");
                    canonicalJournalPath = " requiredFlags: ";
                    String str8 = " path: ";
                    if (includes.isEmpty()) {
                        Log.v(str2, "  ...nothing specified (This means the entirety of app data minus excludes)");
                    } else {
                        for (Entry<String, Set<PathWithRequiredFlags>> entry : includes.entrySet()) {
                            stringBuilder4 = new StringBuilder();
                            stringBuilder4.append("  domain=");
                            stringBuilder4.append((String) entry.getKey());
                            Log.v(str2, stringBuilder4.toString());
                            for (PathWithRequiredFlags includeData : (Set) entry.getValue()) {
                                StringBuilder stringBuilder5 = new StringBuilder();
                                stringBuilder5.append(str8);
                                stringBuilder5.append(includeData.getPath());
                                stringBuilder5.append(canonicalJournalPath);
                                stringBuilder5.append(includeData.getRequiredFlags());
                                Log.v(str2, stringBuilder5.toString());
                            }
                        }
                    }
                    Log.v(str2, "Excludes:");
                    if (excludes.isEmpty()) {
                        Log.v(str2, "  ...nothing to exclude.");
                    } else {
                        for (PathWithRequiredFlags excludeData : excludes) {
                            stringBuilder4 = new StringBuilder();
                            stringBuilder4.append(str8);
                            stringBuilder4.append(excludeData.getPath());
                            stringBuilder4.append(canonicalJournalPath);
                            stringBuilder4.append(excludeData.getRequiredFlags());
                            Log.v(str2, stringBuilder4.toString());
                        }
                    }
                    Log.v(str2, "  ");
                    Log.v(str2, str3);
                    Log.v(str2, str4);
                    return;
                }
                return;
            }
            set = excludes;
            map = includes;
            StringBuilder stringBuilder6 = new StringBuilder();
            stringBuilder6.append("Xml file didn't start with correct tag (<full-backup-content>). Found \"");
            stringBuilder6.append(parser.getName());
            stringBuilder6.append(str);
            throw new XmlPullParserException(stringBuilder6.toString());
        }

        private int getRequiredFlagsFromString(String requiredFlags) {
            if (requiredFlags == null || requiredFlags.length() == 0) {
                return 0;
            }
            int flags = 0;
            for (String f : requiredFlags.split("\\|")) {
                Object obj = -1;
                int hashCode = f.hashCode();
                if (hashCode != 482744282) {
                    if (hashCode != 1499007205) {
                        if (hashCode == 1935925810 && f.equals(FullBackup.FLAG_REQUIRED_DEVICE_TO_DEVICE_TRANSFER)) {
                            obj = 1;
                        }
                    } else if (f.equals(FullBackup.FLAG_REQUIRED_CLIENT_SIDE_ENCRYPTION)) {
                        obj = null;
                    }
                } else if (f.equals(FullBackup.FLAG_REQUIRED_FAKE_CLIENT_SIDE_ENCRYPTION)) {
                    obj = 2;
                }
                if (obj == null) {
                    flags |= 1;
                } else if (obj != 1) {
                    if (obj == 2) {
                        flags |= Integer.MIN_VALUE;
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unrecognized requiredFlag provided, value: \"");
                    stringBuilder.append(f);
                    stringBuilder.append("\"");
                    Log.w(FullBackup.TAG, stringBuilder.toString());
                } else {
                    flags |= 2;
                }
            }
            return flags;
        }

        private Set<PathWithRequiredFlags> parseCurrentTagForDomain(XmlPullParser parser, Set<PathWithRequiredFlags> excludes, Map<String, Set<PathWithRequiredFlags>> includes, String domain) throws XmlPullParserException {
            if (TAG_INCLUDE.equals(parser.getName())) {
                String domainToken = getTokenForXmlDomain(domain);
                Set<PathWithRequiredFlags> includeSet = (Set) includes.get(domainToken);
                if (includeSet == null) {
                    includeSet = new ArraySet();
                    includes.put(domainToken, includeSet);
                }
                return includeSet;
            }
            if (TAG_EXCLUDE.equals(parser.getName())) {
                return excludes;
            }
            String str = FullBackup.TAG_XML_PARSER;
            if (Log.isLoggable(str, 2)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid tag found in xml \"");
                stringBuilder.append(parser.getName());
                stringBuilder.append("\"; aborting operation.");
                Log.v(str, stringBuilder.toString());
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Unrecognised tag in backup criteria xml (");
            stringBuilder2.append(parser.getName());
            stringBuilder2.append(")");
            throw new XmlPullParserException(stringBuilder2.toString());
        }

        private String getTokenForXmlDomain(String xmlDomain) {
            if ("root".equals(xmlDomain)) {
                return "r";
            }
            if (ContentResolver.SCHEME_FILE.equals(xmlDomain)) {
                return FullBackup.FILES_TREE_TOKEN;
            }
            if ("database".equals(xmlDomain)) {
                return FullBackup.DATABASE_TREE_TOKEN;
            }
            if ("sharedpref".equals(xmlDomain)) {
                return FullBackup.SHAREDPREFS_TREE_TOKEN;
            }
            if ("device_root".equals(xmlDomain)) {
                return FullBackup.DEVICE_ROOT_TREE_TOKEN;
            }
            if ("device_file".equals(xmlDomain)) {
                return FullBackup.DEVICE_FILES_TREE_TOKEN;
            }
            if ("device_database".equals(xmlDomain)) {
                return FullBackup.DEVICE_DATABASE_TREE_TOKEN;
            }
            if ("device_sharedpref".equals(xmlDomain)) {
                return FullBackup.DEVICE_SHAREDPREFS_TREE_TOKEN;
            }
            if (MediaStore.VOLUME_EXTERNAL.equals(xmlDomain)) {
                return FullBackup.MANAGED_EXTERNAL_TREE_TOKEN;
            }
            return null;
        }

        private File extractCanonicalFile(File domain, String filePathFromXml) {
            if (filePathFromXml == null) {
                filePathFromXml = "";
            }
            boolean contains = filePathFromXml.contains("..");
            String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
            String str2 = "...resolved \"";
            String str3 = FullBackup.TAG_XML_PARSER;
            StringBuilder stringBuilder;
            if (contains) {
                if (Log.isLoggable(str3, 2)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str2);
                    stringBuilder.append(domain.getPath());
                    stringBuilder.append(str);
                    stringBuilder.append(filePathFromXml);
                    stringBuilder.append("\", but the \"..\" path is not permitted; skipping.");
                    Log.v(str3, stringBuilder.toString());
                }
                return null;
            } else if (!filePathFromXml.contains("//")) {
                return new File(domain, filePathFromXml);
            } else {
                if (Log.isLoggable(str3, 2)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str2);
                    stringBuilder.append(domain.getPath());
                    stringBuilder.append(str);
                    stringBuilder.append(filePathFromXml);
                    stringBuilder.append("\", which contains the invalid \"//\" sequence; skipping.");
                    Log.v(str3, stringBuilder.toString());
                }
                return null;
            }
        }

        private File getDirectoryForCriteriaDomain(String domain) {
            if (TextUtils.isEmpty(domain)) {
                return null;
            }
            if (ContentResolver.SCHEME_FILE.equals(domain)) {
                return this.FILES_DIR;
            }
            if ("database".equals(domain)) {
                return this.DATABASE_DIR;
            }
            if ("root".equals(domain)) {
                return this.ROOT_DIR;
            }
            if ("sharedpref".equals(domain)) {
                return this.SHAREDPREF_DIR;
            }
            if ("device_file".equals(domain)) {
                return this.DEVICE_FILES_DIR;
            }
            if ("device_database".equals(domain)) {
                return this.DEVICE_DATABASE_DIR;
            }
            if ("device_root".equals(domain)) {
                return this.DEVICE_ROOT_DIR;
            }
            if ("device_sharedpref".equals(domain)) {
                return this.DEVICE_SHAREDPREF_DIR;
            }
            if (MediaStore.VOLUME_EXTERNAL.equals(domain)) {
                return this.EXTERNAL_DIR;
            }
            return null;
        }

        private void validateInnerTagContents(XmlPullParser parser) throws XmlPullParserException {
            if (parser != null) {
                String name = parser.getName();
                Object obj = -1;
                int hashCode = name.hashCode();
                if (hashCode != -1321148966) {
                    if (hashCode == 1942574248 && name.equals(TAG_INCLUDE)) {
                        obj = null;
                    }
                } else if (name.equals(TAG_EXCLUDE)) {
                    obj = 1;
                }
                if (obj != null) {
                    if (obj != 1) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("A valid tag is one of \"<include/>\" or \"<exclude/>. You provided \"");
                        stringBuilder.append(parser.getName());
                        stringBuilder.append("\"");
                        throw new XmlPullParserException(stringBuilder.toString());
                    } else if (parser.getAttributeCount() > 2) {
                        throw new XmlPullParserException("At most 2 tag attributes allowed for \"exclude\" tag (\"domain\" & \"path\".");
                    }
                } else if (parser.getAttributeCount() > 3) {
                    throw new XmlPullParserException("At most 3 tag attributes allowed for \"include\" tag (\"domain\" & \"path\" & optional \"requiredFlags\").");
                }
            }
        }
    }

    @UnsupportedAppUsage
    public static native int backupToTar(String str, String str2, String str3, String str4, String str5, FullBackupDataOutput fullBackupDataOutput);

    static synchronized BackupScheme getBackupScheme(Context context) {
        BackupScheme backupSchemeForPackage;
        synchronized (FullBackup.class) {
            backupSchemeForPackage = (BackupScheme) kPackageBackupSchemeMap.get(context.getPackageName());
            if (backupSchemeForPackage == null) {
                backupSchemeForPackage = new BackupScheme(context);
                kPackageBackupSchemeMap.put(context.getPackageName(), backupSchemeForPackage);
            }
        }
        return backupSchemeForPackage;
    }

    public static BackupScheme getBackupSchemeForTest(Context context) {
        BackupScheme testing = new BackupScheme(context);
        testing.mExcludes = new ArraySet();
        testing.mIncludes = new ArrayMap();
        return testing;
    }

    public static void restoreFile(ParcelFileDescriptor data, long size, int type, long mode, long mtime, File outFile) throws IOException {
        IOException e;
        long j;
        File file = outFile;
        long j2;
        if (type == 2) {
            if (file != null) {
                outFile.mkdirs();
            }
            j2 = size;
        } else {
            FileOutputStream out = null;
            String str = TAG;
            if (file != null) {
                try {
                    File parent = outFile.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    out = new FileOutputStream(file);
                } catch (IOException e2) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unable to create/open file ");
                    stringBuilder.append(outFile.getPath());
                    Log.e(str, stringBuilder.toString(), e2);
                }
            }
            byte[] buffer = new byte[32768];
            long origSize = size;
            FileInputStream in = new FileInputStream(data.getFileDescriptor());
            j2 = size;
            for (j = 0; j2 > j; j = 0) {
                int got = in.read(buffer, 0, j2 > ((long) buffer.length) ? buffer.length : (int) j2);
                if (got <= 0) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Incomplete read: expected ");
                    stringBuilder2.append(j2);
                    stringBuilder2.append(" but got ");
                    stringBuilder2.append(origSize - j2);
                    Log.w(str, stringBuilder2.toString());
                    break;
                }
                if (out != null) {
                    try {
                        out.write(buffer, 0, got);
                    } catch (IOException e22) {
                        e22 = e22;
                        StringBuilder stringBuilder3 = new StringBuilder();
                        stringBuilder3.append("Unable to write to file ");
                        stringBuilder3.append(outFile.getPath());
                        Log.e(str, stringBuilder3.toString(), e22);
                        out.close();
                        outFile.delete();
                        out = null;
                    }
                }
                j2 -= (long) got;
            }
            if (out != null) {
                out.close();
            }
        }
        if (mode < 0 || file == null) {
            long j3 = mtime;
            j = mode;
            return;
        }
        try {
            Os.chmod(outFile.getPath(), (int) (mode & 448));
        } catch (ErrnoException e3) {
            e3.rethrowAsIOException();
        }
        file.setLastModified(mtime);
    }
}
