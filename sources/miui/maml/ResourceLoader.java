package miui.maml;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.os.MemoryFile;
import android.telecom.Logging.Session;
import android.text.TextUtils;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import miui.maml.ResourceManager.BitmapInfo;
import miui.maml.util.Utils;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public abstract class ResourceLoader {
    private static final String CONFIG_FILE_NAME = "config.xml";
    private static final String IMAGES_FOLDER_NAME = "images";
    private static final String LOG_TAG = "ResourceLoader";
    private static final String MANIFEST_FILE_NAME = "manifest.xml";
    protected String mConfigName = CONFIG_FILE_NAME;
    protected String mLanguageCountrySuffix;
    protected String mLanguageSuffix;
    protected Locale mLocale;
    protected String mManifestName = MANIFEST_FILE_NAME;
    private String mThemeName;

    public abstract InputStream getInputStream(String str, long[] jArr);

    public abstract boolean resourceExists(String str);

    public ResourceLoader setLocal(Locale locale) {
        if (locale != null) {
            this.mLanguageSuffix = locale.getLanguage();
            this.mLanguageCountrySuffix = locale.toString();
            if (TextUtils.equals(this.mLanguageSuffix, this.mLanguageCountrySuffix)) {
                this.mLanguageSuffix = null;
            }
        }
        this.mLocale = locale;
        return this;
    }

    public Locale getLocale() {
        return this.mLocale;
    }

    public MemoryFile getFile(String src) {
        String str = LOG_TAG;
        long[] olen = new long[1];
        InputStream is = getInputStream(src, olen);
        if (is == null) {
            return null;
        }
        try {
            byte[] buff = new byte[65536];
            MemoryFile mf = new MemoryFile(null, (int) olen[0]);
            int start = 0;
            while (true) {
                int read = is.read(buff, 0, 65536);
                int read2 = read;
                if (read <= 0) {
                    break;
                }
                mf.writeBytes(buff, 0, start, read2);
                start += read2;
            }
            if (mf.length() > 0) {
                try {
                    is.close();
                } catch (IOException e) {
                }
                return mf;
            }
            try {
                is.close();
            } catch (IOException e2) {
            }
            return null;
        } catch (IOException e3) {
            Log.e(str, e3.toString());
            is.close();
        } catch (OutOfMemoryError e4) {
            Log.e(str, e4.toString());
            is.close();
        } catch (Throwable th) {
            try {
                is.close();
            } catch (IOException e5) {
            }
            throw th;
        }
    }

    public BitmapInfo getBitmapInfo(String src, Options opts) {
        StringBuilder stringBuilder;
        String str = IMAGES_FOLDER_NAME;
        String path = getPathForLanguage(src, str);
        String str2 = LOG_TAG;
        if (path == null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("TRY AGAIN to get getPathForLanguage: ");
            stringBuilder.append(src);
            Log.d(str2, stringBuilder.toString());
            path = getPathForLanguage(src, str);
            if (path == null) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("fail to get getPathForLanguage: ");
                stringBuilder2.append(src);
                Log.e(str2, stringBuilder2.toString());
                return null;
            }
        }
        InputStream is = getInputStream(path);
        if (is == null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("TRY AGAIN to get InputStream: ");
            stringBuilder.append(src);
            Log.d(str2, stringBuilder.toString());
            is = getInputStream(path);
            if (is == null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("fail to get InputStream: ");
                stringBuilder.append(src);
                Log.e(str2, stringBuilder.toString());
                return null;
            }
        }
        try {
            Rect padding = new Rect();
            Bitmap bm = BitmapFactory.decodeStream(is, padding, opts);
            if (bm == null) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("TRY AGAIN to decode bitmap: ");
                stringBuilder3.append(src);
                Log.d(str2, stringBuilder3.toString());
                if (BitmapFactory.decodeStream(is, padding, opts) == null) {
                    stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("fail to decode bitmap: ");
                    stringBuilder3.append(src);
                    Log.e(str2, stringBuilder3.toString());
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                    return null;
                }
                try {
                    is.close();
                } catch (IOException e2) {
                }
                return null;
            }
            BitmapInfo bitmapInfo = new BitmapInfo(bm, padding);
            try {
                is.close();
            } catch (IOException e3) {
            }
            return bitmapInfo;
        } catch (OutOfMemoryError e4) {
            Log.e(str2, e4.toString());
            is.close();
        } catch (Throwable th) {
            try {
                is.close();
            } catch (IOException e5) {
            }
            throw th;
        }
    }

    public Element getManifestRoot() {
        return getXmlRoot(this.mManifestName);
    }

    public Element getConfigRoot() {
        return getXmlRoot(this.mConfigName);
    }

    private Element getXmlRoot(String path) {
        InputStream is = getInputStream(getPathForLanguage(path));
        Element element = null;
        String str = LOG_TAG;
        if (is == null) {
            Log.e(str, "getXmlRoot local inputStream is null");
            return null;
        }
        try {
            element = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is).getDocumentElement();
            try {
                is.close();
            } catch (IOException e) {
            }
            return element;
        } catch (IOException e2) {
            Log.e(str, e2.toString());
            is.close();
            return element;
        } catch (OutOfMemoryError e3) {
            Log.e(str, e3.toString());
            is.close();
            return element;
        } catch (ParserConfigurationException e4) {
            Log.e(str, e4.toString());
            is.close();
            return element;
        } catch (SAXException e5) {
            Log.e(str, e5.toString());
            is.close();
            return element;
        } catch (Exception e6) {
            Log.e(str, e6.toString());
            try {
                is.close();
            } catch (IOException e7) {
            }
            return element;
        } catch (Throwable th) {
            try {
                is.close();
            } catch (IOException e8) {
            }
            throw th;
        }
    }

    public String getPathForLanguage(String file) {
        String ret = null;
        if (!TextUtils.isEmpty(this.mLanguageCountrySuffix)) {
            ret = Utils.addFileNameSuffix(file, this.mLanguageCountrySuffix);
            if (!resourceExists(ret)) {
                ret = null;
            }
        }
        if (ret == null && !TextUtils.isEmpty(this.mLanguageSuffix)) {
            ret = Utils.addFileNameSuffix(file, this.mLanguageSuffix);
            if (!resourceExists(ret)) {
                ret = null;
            }
        }
        return ret != null ? ret : file;
    }

    private String getPathForLanguage(String src, String folder) {
        StringBuilder stringBuilder;
        String path;
        boolean isEmpty = TextUtils.isEmpty(this.mLanguageCountrySuffix);
        String str = Session.SESSION_SEPARATION_CHAR_CHILD;
        String str2 = "/";
        if (!isEmpty) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(folder);
            stringBuilder.append(str);
            stringBuilder.append(this.mLanguageCountrySuffix);
            stringBuilder.append(str2);
            stringBuilder.append(src);
            path = stringBuilder.toString();
            if (resourceExists(path)) {
                return path;
            }
        }
        if (!TextUtils.isEmpty(this.mLanguageSuffix)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(folder);
            stringBuilder.append(str);
            stringBuilder.append(this.mLanguageSuffix);
            stringBuilder.append(str2);
            stringBuilder.append(src);
            path = stringBuilder.toString();
            if (resourceExists(path)) {
                return path;
            }
        }
        if (!TextUtils.isEmpty(folder)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(folder);
            stringBuilder.append(str2);
            stringBuilder.append(src);
            path = stringBuilder.toString();
            if (resourceExists(path)) {
                return path;
            }
        }
        return resourceExists(src) ? src : null;
    }

    public final InputStream getInputStream(String path) {
        return getInputStream(path, null);
    }

    public void init() {
    }

    public void finish() {
    }
}
