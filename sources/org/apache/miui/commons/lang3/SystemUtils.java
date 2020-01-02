package org.apache.miui.commons.lang3;

import java.io.File;
import java.io.PrintStream;

public class SystemUtils {
    public static final String AWT_TOOLKIT = getSystemProperty("awt.toolkit");
    public static final String FILE_ENCODING = getSystemProperty("file.encoding");
    public static final String FILE_SEPARATOR = getSystemProperty("file.separator");
    public static final boolean IS_JAVA_1_1 = getJavaVersionMatches("1.1");
    public static final boolean IS_JAVA_1_2 = getJavaVersionMatches("1.2");
    public static final boolean IS_JAVA_1_3 = getJavaVersionMatches("1.3");
    public static final boolean IS_JAVA_1_4 = getJavaVersionMatches("1.4");
    public static final boolean IS_JAVA_1_5 = getJavaVersionMatches("1.5");
    public static final boolean IS_JAVA_1_6 = getJavaVersionMatches("1.6");
    public static final boolean IS_JAVA_1_7 = getJavaVersionMatches("1.7");
    public static final boolean IS_OS_AIX = getOSMatchesName("AIX");
    public static final boolean IS_OS_FREE_BSD = getOSMatchesName("FreeBSD");
    public static final boolean IS_OS_HP_UX = getOSMatchesName("HP-UX");
    public static final boolean IS_OS_IRIX = getOSMatchesName("Irix");
    public static final boolean IS_OS_LINUX;
    public static final boolean IS_OS_MAC = getOSMatchesName("Mac");
    public static final boolean IS_OS_MAC_OSX = getOSMatchesName("Mac OS X");
    public static final boolean IS_OS_NET_BSD = getOSMatchesName("NetBSD");
    public static final boolean IS_OS_OPEN_BSD = getOSMatchesName("OpenBSD");
    public static final boolean IS_OS_OS2 = getOSMatchesName("OS/2");
    public static final boolean IS_OS_SOLARIS = getOSMatchesName("Solaris");
    public static final boolean IS_OS_SUN_OS = getOSMatchesName("SunOS");
    public static final boolean IS_OS_UNIX;
    public static final boolean IS_OS_WINDOWS;
    public static final boolean IS_OS_WINDOWS_2000;
    public static final boolean IS_OS_WINDOWS_2003;
    public static final boolean IS_OS_WINDOWS_2008;
    public static final boolean IS_OS_WINDOWS_7;
    public static final boolean IS_OS_WINDOWS_95;
    public static final boolean IS_OS_WINDOWS_98;
    public static final boolean IS_OS_WINDOWS_ME;
    public static final boolean IS_OS_WINDOWS_NT = getOSMatchesName("Windows NT");
    public static final boolean IS_OS_WINDOWS_VISTA;
    public static final boolean IS_OS_WINDOWS_XP;
    public static final String JAVA_AWT_FONTS = getSystemProperty("java.awt.fonts");
    public static final String JAVA_AWT_GRAPHICSENV = getSystemProperty("java.awt.graphicsenv");
    public static final String JAVA_AWT_HEADLESS = getSystemProperty("java.awt.headless");
    public static final String JAVA_AWT_PRINTERJOB = getSystemProperty("java.awt.printerjob");
    public static final String JAVA_CLASS_PATH = getSystemProperty("java.class.path");
    public static final String JAVA_CLASS_VERSION = getSystemProperty("java.class.version");
    public static final String JAVA_COMPILER = getSystemProperty("java.compiler");
    public static final String JAVA_ENDORSED_DIRS = getSystemProperty("java.endorsed.dirs");
    public static final String JAVA_EXT_DIRS = getSystemProperty("java.ext.dirs");
    public static final String JAVA_HOME = getSystemProperty(JAVA_HOME_KEY);
    private static final String JAVA_HOME_KEY = "java.home";
    public static final String JAVA_IO_TMPDIR = getSystemProperty(JAVA_IO_TMPDIR_KEY);
    private static final String JAVA_IO_TMPDIR_KEY = "java.io.tmpdir";
    public static final String JAVA_LIBRARY_PATH = getSystemProperty("java.library.path");
    public static final String JAVA_RUNTIME_NAME = getSystemProperty("java.runtime.name");
    public static final String JAVA_RUNTIME_VERSION = getSystemProperty("java.runtime.version");
    public static final String JAVA_SPECIFICATION_NAME = getSystemProperty("java.specification.name");
    public static final String JAVA_SPECIFICATION_VENDOR = getSystemProperty("java.specification.vendor");
    public static final String JAVA_SPECIFICATION_VERSION = getSystemProperty("java.specification.version");
    private static final JavaVersion JAVA_SPECIFICATION_VERSION_AS_ENUM = JavaVersion.get(JAVA_SPECIFICATION_VERSION);
    public static final String JAVA_UTIL_PREFS_PREFERENCES_FACTORY = getSystemProperty("java.util.prefs.PreferencesFactory");
    public static final String JAVA_VENDOR = getSystemProperty("java.vendor");
    public static final String JAVA_VENDOR_URL = getSystemProperty("java.vendor.url");
    public static final String JAVA_VERSION = getSystemProperty("java.version");
    public static final String JAVA_VM_INFO = getSystemProperty("java.vm.info");
    public static final String JAVA_VM_NAME = getSystemProperty("java.vm.name");
    public static final String JAVA_VM_SPECIFICATION_NAME = getSystemProperty("java.vm.specification.name");
    public static final String JAVA_VM_SPECIFICATION_VENDOR = getSystemProperty("java.vm.specification.vendor");
    public static final String JAVA_VM_SPECIFICATION_VERSION = getSystemProperty("java.vm.specification.version");
    public static final String JAVA_VM_VENDOR = getSystemProperty("java.vm.vendor");
    public static final String JAVA_VM_VERSION = getSystemProperty("java.vm.version");
    public static final String LINE_SEPARATOR = getSystemProperty("line.separator");
    public static final String OS_ARCH = getSystemProperty("os.arch");
    public static final String OS_NAME = getSystemProperty("os.name");
    private static final String OS_NAME_WINDOWS_PREFIX = "Windows";
    public static final String OS_VERSION = getSystemProperty("os.version");
    public static final String PATH_SEPARATOR = getSystemProperty("path.separator");
    public static final String USER_COUNTRY;
    public static final String USER_DIR = getSystemProperty(USER_DIR_KEY);
    private static final String USER_DIR_KEY = "user.dir";
    public static final String USER_HOME = getSystemProperty(USER_HOME_KEY);
    private static final String USER_HOME_KEY = "user.home";
    public static final String USER_LANGUAGE = getSystemProperty("user.language");
    public static final String USER_NAME = getSystemProperty("user.name");
    public static final String USER_TIMEZONE = getSystemProperty("user.timezone");

    static {
        String str = "user.country";
        if (getSystemProperty(str) == null) {
            str = "user.region";
        }
        USER_COUNTRY = getSystemProperty(str);
        boolean z = false;
        boolean z2 = getOSMatchesName("Linux") || getOSMatchesName("LINUX");
        IS_OS_LINUX = z2;
        if (IS_OS_AIX || IS_OS_HP_UX || IS_OS_IRIX || IS_OS_LINUX || IS_OS_MAC_OSX || IS_OS_SOLARIS || IS_OS_SUN_OS || IS_OS_FREE_BSD || IS_OS_OPEN_BSD || IS_OS_NET_BSD) {
            z = true;
        }
        IS_OS_UNIX = z;
        str = OS_NAME_WINDOWS_PREFIX;
        IS_OS_WINDOWS = getOSMatchesName(str);
        IS_OS_WINDOWS_2000 = getOSMatches(str, "5.0");
        IS_OS_WINDOWS_2003 = getOSMatches(str, "5.2");
        String str2 = "6.1";
        IS_OS_WINDOWS_2008 = getOSMatches("Windows Server 2008", str2);
        String str3 = "Windows 9";
        IS_OS_WINDOWS_95 = getOSMatches(str3, "4.0");
        IS_OS_WINDOWS_98 = getOSMatches(str3, "4.1");
        IS_OS_WINDOWS_ME = getOSMatches(str, "4.9");
        IS_OS_WINDOWS_XP = getOSMatches(str, "5.1");
        IS_OS_WINDOWS_VISTA = getOSMatches(str, "6.0");
        IS_OS_WINDOWS_7 = getOSMatches(str, str2);
    }

    public static File getJavaHome() {
        return new File(System.getProperty(JAVA_HOME_KEY));
    }

    public static File getJavaIoTmpDir() {
        return new File(System.getProperty(JAVA_IO_TMPDIR_KEY));
    }

    private static boolean getJavaVersionMatches(String versionPrefix) {
        return isJavaVersionMatch(JAVA_SPECIFICATION_VERSION, versionPrefix);
    }

    private static boolean getOSMatches(String osNamePrefix, String osVersionPrefix) {
        return isOSMatch(OS_NAME, OS_VERSION, osNamePrefix, osVersionPrefix);
    }

    private static boolean getOSMatchesName(String osNamePrefix) {
        return isOSNameMatch(OS_NAME, osNamePrefix);
    }

    private static String getSystemProperty(String property) {
        try {
            return System.getProperty(property);
        } catch (SecurityException e) {
            PrintStream printStream = System.err;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Caught a SecurityException reading the system property '");
            stringBuilder.append(property);
            stringBuilder.append("'; the SystemUtils property value will default to null.");
            printStream.println(stringBuilder.toString());
            return null;
        }
    }

    public static File getUserDir() {
        return new File(System.getProperty(USER_DIR_KEY));
    }

    public static File getUserHome() {
        return new File(System.getProperty(USER_HOME_KEY));
    }

    public static boolean isJavaAwtHeadless() {
        String str = JAVA_AWT_HEADLESS;
        return str != null ? str.equals(Boolean.TRUE.toString()) : false;
    }

    public static boolean isJavaVersionAtLeast(JavaVersion requiredVersion) {
        return JAVA_SPECIFICATION_VERSION_AS_ENUM.atLeast(requiredVersion);
    }

    static boolean isJavaVersionMatch(String version, String versionPrefix) {
        if (version == null) {
            return false;
        }
        return version.startsWith(versionPrefix);
    }

    static boolean isOSMatch(String osName, String osVersion, String osNamePrefix, String osVersionPrefix) {
        boolean z = false;
        if (osName == null || osVersion == null) {
            return false;
        }
        if (osName.startsWith(osNamePrefix) && osVersion.startsWith(osVersionPrefix)) {
            z = true;
        }
        return z;
    }

    static boolean isOSNameMatch(String osName, String osNamePrefix) {
        if (osName == null) {
            return false;
        }
        return osName.startsWith(osNamePrefix);
    }
}
