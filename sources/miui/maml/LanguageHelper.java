package miui.maml;

import android.os.MemoryFile;
import android.util.Log;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilderFactory;
import miui.maml.data.Variables;
import miui.maml.util.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class LanguageHelper {
    private static final String COMPATIBLE_STRING_ROOT_TAG = "strings";
    private static final String DEFAULT_STRING_FILE_PATH = "strings/strings.xml";
    private static final String LOG_TAG = "LanguageHelper";
    private static final String STRING_FILE_PATH = "strings/strings.xml";
    private static final String STRING_ROOT_TAG = "resources";
    private static final String STRING_TAG = "string";

    public static boolean load(Locale locale, ResourceManager resourceManager, Variables variables) {
        MemoryFile stringFile = null;
        String str = "strings/strings.xml";
        if (locale != null) {
            stringFile = resourceManager.getFile(Utils.addFileNameSuffix(str, locale.toString()));
            if (stringFile == null) {
                stringFile = resourceManager.getFile(Utils.addFileNameSuffix(str, locale.getLanguage()));
            }
        }
        boolean z = false;
        String str2 = LOG_TAG;
        if (stringFile == null) {
            stringFile = resourceManager.getFile(str);
            if (stringFile == null) {
                Log.w(str2, "no available string resources to load.");
                return false;
            }
        }
        try {
            z = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stringFile.getInputStream());
            Document doc = z;
            return z;
        } catch (Exception e) {
            Log.e(str2, e.getMessage());
            return z;
        } finally {
            stringFile.close();
        }
    }

    private static boolean setVariables(Document doc, Variables variables) {
        boolean standardFormat = true;
        NodeList rootsList = doc.getElementsByTagName(STRING_ROOT_TAG);
        if (rootsList.getLength() <= 0) {
            rootsList = doc.getElementsByTagName(COMPATIBLE_STRING_ROOT_TAG);
            if (rootsList.getLength() <= 0) {
                return false;
            }
            standardFormat = false;
        }
        NodeList stringList = ((Element) rootsList.item(0)).getElementsByTagName(STRING_TAG);
        for (int i = 0; i < stringList.getLength(); i++) {
            Element string = (Element) stringList.item(i);
            variables.put(string.getAttribute("name"), (standardFormat ? string.getTextContent() : string.getAttribute("value")).replaceAll("\\\\", ""));
        }
        return true;
    }
}
