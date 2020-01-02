package miui.maml.util;

import android.content.Context;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import miui.content.res.ThemeNativeUtils;
import miui.maml.util.Utils.XmlTraverseListener;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfigFile {
    private static final String LOG_TAG = "ConfigFile";
    public static final String TAG_APP_PICKER = "AppPicker";
    public static final String TAG_CHECK_BOX = "CheckBox";
    private static final String TAG_GADGET = "Gadget";
    private static final String TAG_GADGETS = "Gadgets";
    public static final String TAG_GROUP = "Group";
    public static final String TAG_IMAGE_PICKER = "ImagePicker";
    public static final String TAG_NUMBER_CHOICE = "NumberChoice";
    public static final String TAG_NUMBER_INPUT = "NumberInput";
    private static final String TAG_ROOT = "Config";
    public static final String TAG_STRING_CHOICE = "StringChoice";
    public static final String TAG_STRING_INPUT = "StringInput";
    private static final String TAG_TASK = "Intent";
    private static final String TAG_TASKS = "Tasks";
    private static final String TAG_VARIABLE = "Variable";
    private static final String TAG_VARIABLES = "Variables";
    private boolean mDirty;
    private String mFilePath;
    private ArrayList<Gadget> mGadgets = new ArrayList();
    private HashMap<String, Task> mTasks = new HashMap();
    private HashMap<String, Variable> mVariables = new HashMap();

    private interface OnLoadElementListener {
        void OnLoadElement(Element element);
    }

    public static class Gadget {
        public String path;
        public int x;
        public int y;

        public Gadget(String pa, int gx, int gy) {
            this.path = pa;
            this.x = gx;
            this.y = gy;
        }
    }

    public static class Variable {
        public String name;
        public String type;
        public String value;
    }

    public Collection<Variable> getVariables() {
        return this.mVariables.values();
    }

    public Collection<Task> getTasks() {
        return this.mTasks.values();
    }

    public Collection<Gadget> getGadgets() {
        return this.mGadgets;
    }

    public boolean save(Context context) {
        boolean dirty = this.mDirty;
        this.mDirty = false;
        if (!dirty || save(this.mFilePath, context)) {
            return true;
        }
        return false;
    }

    public boolean save(String filePath, Context context) {
        StringBuilder stringBuilder;
        String str = LOG_TAG;
        ThemeNativeUtils.remove(filePath);
        String tempConfigPath = null;
        try {
            createNewFile(filePath);
        } catch (IOException e) {
            try {
                File file = context.getExternalFilesDir(null);
                String str2 = "temp";
                if (VERSION.SDK_INT < 23) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(context.getDir(str2, 0).getPath());
                    stringBuilder.append(File.separator);
                    stringBuilder.append(str2);
                    tempConfigPath = stringBuilder.toString();
                } else if (file != null) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(file.getPath());
                    stringBuilder.append(File.separator);
                    stringBuilder.append(str2);
                    tempConfigPath = stringBuilder.toString();
                }
                createNewFile(tempConfigPath);
            } catch (Exception e2) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("create target file failed");
                stringBuilder.append(e);
                Log.e(str, stringBuilder.toString());
                return false;
            }
        } catch (Exception e3) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("create target file or temp file failed");
            stringBuilder2.append(e3);
            Log.e(str, stringBuilder2.toString());
            return false;
        }
        IOException e4 = new StringBuilder();
        String str3 = TAG_ROOT;
        writeTag(e4, str3, false);
        writeVariables(e4);
        writeTasks(e4);
        writeGadgets(e4);
        writeTag(e4, str3, true);
        try {
            if (new File(filePath).exists()) {
                ThemeNativeUtils.write(filePath, e4.toString());
            } else if (TextUtils.isEmpty(tempConfigPath) || !new File(tempConfigPath).exists()) {
                Log.w(str, "target file and temp file are not exists");
                return false;
            } else {
                ThemeNativeUtils.write(tempConfigPath, e4.toString());
                ThemeNativeUtils.copy(tempConfigPath, filePath);
                ThemeNativeUtils.remove(tempConfigPath);
            }
            ThemeNativeUtils.updateFilePermissionWithThemeContext("/data/system/theme/config.config");
            return true;
        } catch (Exception e5) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("write file error");
            stringBuilder.append(e5);
            Log.e(str, stringBuilder.toString());
            return false;
        }
    }

    private void createNewFile(String filePath) throws IOException {
        if (!TextUtils.isEmpty(filePath)) {
            File f = new File(filePath);
            f.getParentFile().mkdirs();
            f.delete();
            f.createNewFile();
        }
    }

    public boolean load(String filePath) {
        this.mFilePath = filePath;
        this.mVariables.clear();
        this.mTasks.clear();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            Element root = dbf.newDocumentBuilder().parse(is).getDocumentElement();
            if (root == null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
                return false;
            } else if (root.getNodeName().equals(TAG_ROOT)) {
                loadVariables(root);
                loadTasks(root);
                loadGadgets(root);
                try {
                    is.close();
                } catch (IOException e2) {
                }
                return true;
            } else {
                try {
                    is.close();
                } catch (IOException e3) {
                }
                return false;
            }
        } catch (FileNotFoundException e4) {
            if (is != null) {
                is.close();
            }
            return false;
        } catch (ParserConfigurationException e5) {
            e5.printStackTrace();
            if (is != null) {
                is.close();
            }
            return false;
        } catch (SAXException e6) {
            e6.printStackTrace();
            if (is != null) {
                is.close();
            }
            return false;
        } catch (IOException e7) {
            e7.printStackTrace();
            if (is != null) {
                is.close();
            }
            return false;
        } catch (Exception e8) {
            e8.printStackTrace();
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e9) {
                }
            }
            return false;
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e10) {
                }
            }
        }
    }

    private void writeVariables(StringBuilder sb) {
        if (this.mVariables.size() != 0) {
            String str = TAG_VARIABLES;
            writeTag(sb, str, false);
            String[] names = new String[]{"name", "type", "value"};
            for (Variable item : this.mVariables.values()) {
                writeTag(sb, "Variable", names, new String[]{item.name, item.type, item.value});
            }
            writeTag(sb, str, true);
        }
    }

    private void writeTasks(StringBuilder sb) {
        if (this.mTasks.size() != 0) {
            String str = TAG_TASKS;
            writeTag(sb, str, false);
            String[] names = new String[]{Task.TAG_ID, Task.TAG_ACTION, Task.TAG_TYPE, Task.TAG_CATEGORY, Task.TAG_PACKAGE, Task.TAG_CLASS, Task.TAG_NAME};
            for (Task item : this.mTasks.values()) {
                writeTag(sb, TAG_TASK, names, new String[]{item.id, item.action, item.type, item.category, item.packageName, item.className, item.name}, true);
            }
            writeTag(sb, str, true);
        }
    }

    private void writeGadgets(StringBuilder sb) {
        if (this.mGadgets.size() != 0) {
            String str = TAG_GADGETS;
            writeTag(sb, str, false);
            String[] names = new String[]{"path", "x", "y"};
            Iterator it = this.mGadgets.iterator();
            while (it.hasNext()) {
                Gadget item = (Gadget) it.next();
                writeTag(sb, TAG_GADGET, names, new String[]{item.path, String.valueOf(item.x), String.valueOf(item.y)}, true);
            }
            writeTag(sb, str, true);
        }
    }

    private static void writeTag(StringBuilder sb, String tag, boolean end) {
        sb.append("<");
        if (end) {
            sb.append("/");
        }
        sb.append(tag);
        sb.append(">\n");
    }

    private static void writeTag(StringBuilder sb, String tag, String[] names, String[] values) {
        writeTag(sb, tag, names, values, true);
    }

    private static void writeTag(StringBuilder sb, String tag, String[] names, String[] values, boolean ignoreEmptyValues) {
        sb.append("<");
        sb.append(tag);
        int i = 0;
        while (i < names.length) {
            if (!ignoreEmptyValues || !TextUtils.isEmpty(values[i])) {
                sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                sb.append(names[i]);
                sb.append("=\"");
                sb.append(values[i]);
                sb.append("\"");
            }
            i++;
        }
        sb.append("/>\n");
    }

    private void loadVariables(Element root) {
        loadList(root, TAG_VARIABLES, "Variable", new OnLoadElementListener() {
            public void OnLoadElement(Element ele) {
                ConfigFile.this.put(ele.getAttribute("name"), ele.getAttribute("value"), ele.getAttribute("type"));
            }
        });
    }

    private void loadTasks(Element root) {
        loadList(root, TAG_TASKS, TAG_TASK, new OnLoadElementListener() {
            public void OnLoadElement(Element ele) {
                ConfigFile.this.putTask(Task.load(ele));
            }
        });
    }

    private void loadGadgets(Element root) {
        loadList(root, TAG_GADGETS, TAG_GADGET, new OnLoadElementListener() {
            public void OnLoadElement(Element ele) {
                if (ele != null) {
                    ConfigFile.this.putGadget(new Gadget(ele.getAttribute("path"), Utils.getAttrAsInt(ele, "x", 0), Utils.getAttrAsInt(ele, "y", 0)));
                }
            }
        });
    }

    private void loadList(Element root, String listTag, String itemTag, OnLoadElementListener listener) {
        root = Utils.getChild(root, listTag);
        if (root != null) {
            NodeList children = root.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node node = children.item(i);
                if (node.getNodeType() == (short) 1 && node.getNodeName().equals(itemTag)) {
                    listener.OnLoadElement((Element) node);
                }
            }
        }
    }

    public String getVariable(String name) {
        Variable item = (Variable) this.mVariables.get(name);
        return item == null ? null : item.value;
    }

    public void putString(String name, String value) {
        put(name, value, "string");
        this.mDirty = true;
    }

    public void putNumber(String name, String value) {
        put(name, value, "number");
        this.mDirty = true;
    }

    public void putNumber(String name, double value) {
        putNumber(name, Utils.doubleToString(value));
    }

    public void putTask(Task task) {
        if (task != null && !TextUtils.isEmpty(task.id)) {
            this.mTasks.put(task.id, task);
            this.mDirty = true;
        }
    }

    public void putGadget(Gadget g) {
        if (g != null) {
            this.mGadgets.add(g);
            this.mDirty = true;
        }
    }

    public void removeGadget(Gadget g) {
        this.mGadgets.remove(g);
        this.mDirty = true;
    }

    public void moveGadget(Gadget g, int position) {
        if (this.mGadgets.remove(g)) {
            this.mGadgets.add(position, g);
        }
    }

    public Task getTask(String id) {
        return (Task) this.mTasks.get(id);
    }

    private void put(String name, String value, String type) {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(type)) {
            if ("string".equals(type) || "number".equals(type)) {
                Variable item = (Variable) this.mVariables.get(name);
                if (item == null) {
                    item = new Variable();
                    item.name = name;
                    this.mVariables.put(name, item);
                }
                item.type = type;
                item.value = value;
            }
        }
    }

    public void loadDefaultSettings(Element root) {
        if (root != null && root.getNodeName().equals(TAG_ROOT)) {
            Utils.traverseXmlElementChildren(root, "Group", new XmlTraverseListener() {
                public void onChild(Element child) {
                    Utils.traverseXmlElementChildren(child, null, new XmlTraverseListener() {
                        public void onChild(Element child) {
                            String tag = child.getNodeName();
                            String id = child.getAttribute("id");
                            String str = "default";
                            if (ConfigFile.TAG_STRING_INPUT.equals(tag)) {
                                ConfigFile.this.putString(id, child.getAttribute(str));
                            } else if (ConfigFile.TAG_CHECK_BOX.equals(tag)) {
                                ConfigFile configFile = ConfigFile.this;
                                String str2 = "1";
                                if (!child.getAttribute(str).equals(str2)) {
                                    str2 = "0";
                                }
                                configFile.putNumber(id, str2);
                            } else if (ConfigFile.TAG_NUMBER_INPUT.equals(tag)) {
                                ConfigFile.this.putNumber(id, Utils.doubleToString((double) Utils.getAttrAsFloat(child, str, 0.0f)));
                            } else if (ConfigFile.TAG_STRING_CHOICE.equals(tag)) {
                                ConfigFile.this.putString(id, child.getAttribute(str));
                            } else if (ConfigFile.TAG_NUMBER_CHOICE.equals(tag)) {
                                ConfigFile.this.putNumber(id, Utils.doubleToString((double) Utils.getAttrAsFloat(child, str, 0.0f)));
                            } else if (ConfigFile.TAG_APP_PICKER.equals(tag)) {
                                ConfigFile.this.putTask(Task.load(child));
                            }
                        }
                    });
                }
            });
        }
    }
}
