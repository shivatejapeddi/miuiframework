package miui.maml.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabaseCorruptException;
import android.database.sqlite.SQLiteDiskIOException;
import android.database.sqlite.SQLiteFullException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import miui.maml.ScreenElementRoot;
import miui.maml.elements.ImageScreenElement;
import miui.maml.elements.ListScreenElement;
import miui.maml.elements.ListScreenElement.ColumnInfo;
import miui.maml.elements.ListScreenElement.ColumnInfo.Type;
import miui.maml.util.TextFormatter;
import miui.maml.util.Utils;
import miui.os.SystemProperties;
import org.w3c.dom.Element;

public class ContentProviderBinder extends VariableBinder {
    private static final boolean DBG = false;
    private static final String LOG_TAG = "ContentProviderBinder";
    private static final int QUERY_TOKEN = 100;
    public static final String TAG_NAME = "ContentProviderBinder";
    protected String[] mArgs;
    private boolean mAwareChangeWhilePause;
    public ChangeObserver mChangeObserver;
    protected String[] mColumns;
    protected String mCountName;
    private IndexedVariable mCountVar;
    private Handler mHandler;
    private long mLastQueryTime;
    private Uri mLastUri;
    private List mList;
    private boolean mNeedsRequery;
    protected String mOrder;
    private QueryHandler mQueryHandler;
    private boolean mSystemBootCompleted;
    private int mUpdateInterval;
    private Runnable mUpdater;
    protected TextFormatter mUriFormatter;
    protected TextFormatter mWhereFormatter;

    /* renamed from: miui.maml.data.ContentProviderBinder$2 */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$miui$maml$elements$ListScreenElement$ColumnInfo$Type = new int[Type.values().length];

        static {
            try {
                $SwitchMap$miui$maml$elements$ListScreenElement$ColumnInfo$Type[Type.DOUBLE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$miui$maml$elements$ListScreenElement$ColumnInfo$Type[Type.FLOAT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$miui$maml$elements$ListScreenElement$ColumnInfo$Type[Type.INTEGER.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$miui$maml$elements$ListScreenElement$ColumnInfo$Type[Type.LONG.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$miui$maml$elements$ListScreenElement$ColumnInfo$Type[Type.STRING.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$miui$maml$elements$ListScreenElement$ColumnInfo$Type[Type.BITMAP.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    public static class Builder {
        private ContentProviderBinder mBinder;

        protected Builder(ContentProviderBinder binder) {
            this.mBinder = binder;
        }

        public Builder setName(String name) {
            this.mBinder.mName = name;
            return this;
        }

        public Builder setWhere(String where) {
            ContentProviderBinder contentProviderBinder = this.mBinder;
            contentProviderBinder.mWhereFormatter = new TextFormatter(contentProviderBinder.getVariables(), where);
            return this;
        }

        public Builder setWhere(String whereFormat, String whereParas) {
            ContentProviderBinder contentProviderBinder = this.mBinder;
            contentProviderBinder.mWhereFormatter = new TextFormatter(contentProviderBinder.getVariables(), whereFormat, whereParas);
            return this;
        }

        public Builder setColumns(String[] columns) {
            this.mBinder.mColumns = columns;
            return this;
        }

        public Builder setArgs(String[] args) {
            this.mBinder.mArgs = args;
            return this;
        }

        public Builder setOrder(String order) {
            this.mBinder.mOrder = order;
            return this;
        }

        public Builder setCountName(String countName) {
            ContentProviderBinder contentProviderBinder = this.mBinder;
            contentProviderBinder.mCountName = countName;
            contentProviderBinder.createCountVar();
            return this;
        }

        public void addVariable(String name, String type, String column, int row, Variables var) {
            Variable v = new Variable(name, type, var);
            v.mColumn = column;
            v.mRow = row;
            this.mBinder.addVariable(v);
        }
    }

    protected class ChangeObserver extends ContentObserver {
        public ChangeObserver() {
            super(ContentProviderBinder.this.mHandler);
        }

        public boolean deliverSelfNotifications() {
            return true;
        }

        public void onChange(boolean selfChange) {
            ContentProviderBinder.this.onContentChanged();
        }
    }

    private static class List {
        private ListScreenElement mList;
        private int mMaxCount;
        private String mName;
        private ScreenElementRoot mRoot;

        public List(Element node, ScreenElementRoot root) {
            this.mName = node.getAttribute("name");
            this.mMaxCount = Utils.getAttrAsInt(node, "maxCount", Integer.MAX_VALUE);
            this.mRoot = root;
        }

        public void fill(Cursor c) {
            if (c != null) {
                String str = "ContentProviderBinder";
                if (this.mList == null) {
                    this.mList = (ListScreenElement) this.mRoot.findElement(this.mName);
                    if (this.mList == null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("fail to find list: ");
                        stringBuilder.append(this.mName);
                        Log.e(str, stringBuilder.toString());
                        return;
                    }
                }
                this.mList.removeAllItems();
                ArrayList<ColumnInfo> columnsInfo = this.mList.getColumnsInfo();
                int size = columnsInfo.size();
                int[] column = new int[size];
                Object[] objects = new Object[size];
                int i = 0;
                while (i < column.length) {
                    try {
                        column[i] = c.getColumnIndexOrThrow(((ColumnInfo) columnsInfo.get(i)).mVarName);
                        i++;
                    } catch (IllegalArgumentException e) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("illegal column:");
                        stringBuilder2.append(((ColumnInfo) columnsInfo.get(i)).mVarName);
                        stringBuilder2.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                        stringBuilder2.append(e.toString());
                        Log.e(str, stringBuilder2.toString());
                        return;
                    }
                }
                c.moveToFirst();
                int count = c.getCount();
                if (count > this.mMaxCount) {
                    count = this.mMaxCount;
                }
                for (i = 0; i < count; i++) {
                    for (int j = 0; j < size; j++) {
                        objects[j] = null;
                        ColumnInfo columnInfo = (ColumnInfo) columnsInfo.get(j);
                        int col = column[j];
                        if (!c.isNull(col)) {
                            int i2 = AnonymousClass2.$SwitchMap$miui$maml$elements$ListScreenElement$ColumnInfo$Type[columnInfo.mType.ordinal()];
                            if (i2 == 5) {
                                objects[j] = c.getString(col);
                            } else if (i2 != 6) {
                                i2 = AnonymousClass2.$SwitchMap$miui$maml$elements$ListScreenElement$ColumnInfo$Type[columnInfo.mType.ordinal()];
                                if (i2 == 1) {
                                    objects[j] = Double.valueOf(c.getDouble(col));
                                } else if (i2 == 2) {
                                    objects[j] = Float.valueOf(c.getFloat(col));
                                } else if (i2 == 3) {
                                    objects[j] = Integer.valueOf(c.getInt(col));
                                } else if (i2 == 4) {
                                    objects[j] = Long.valueOf(c.getLong(col));
                                }
                            } else {
                                byte[] valueBytes = c.getBlob(col);
                                if (valueBytes != null) {
                                    objects[j] = BitmapFactory.decodeByteArray(valueBytes, 0, valueBytes.length);
                                }
                            }
                        }
                    }
                    this.mList.addItem(objects);
                    c.moveToNext();
                }
            }
        }
    }

    public interface QueryCompleteListener {
        void onQueryCompleted(String str);
    }

    private final class QueryHandler extends AsyncQueryHandler {

        protected class CatchingWorkerHandler extends WorkerHandler {
            public CatchingWorkerHandler(Looper looper) {
                super(looper);
            }

            public void handleMessage(Message msg) {
                String str = "Exception on background worker thread";
                String str2 = "ContentProviderBinder";
                try {
                    super.handleMessage(msg);
                } catch (SQLiteDiskIOException e) {
                    Log.w(str2, str, e);
                } catch (SQLiteFullException e2) {
                    Log.w(str2, str, e2);
                } catch (SQLiteDatabaseCorruptException e3) {
                    Log.w(str2, str, e3);
                }
            }
        }

        /* Access modifiers changed, original: protected */
        public Handler createHandler(Looper looper) {
            return new CatchingWorkerHandler(looper);
        }

        public QueryHandler(Context context) {
            super(Looper.getMainLooper(), context.getContentResolver());
        }

        /* Access modifiers changed, original: protected */
        public void onQueryComplete(int token, Object cookie, Cursor cursor) {
            ContentProviderBinder.this.onQueryComplete(cursor);
        }
    }

    private static class Variable extends miui.maml.data.VariableBinder.Variable {
        public static final int BLOB_BITMAP = 1001;
        public boolean mBlocked;
        public String mColumn;
        private ImageScreenElement mImageVar;
        private boolean mNoImageElement;
        public int mRow;

        public Variable(String name, String type, Variables var) {
            super(name, type, var);
        }

        public Variable(Element node, Variables var) {
            super(node, var);
            this.mColumn = node.getAttribute("column");
            this.mRow = Utils.getAttrAsInt(node, "row", 0);
        }

        /* Access modifiers changed, original: protected */
        public int parseType(String type) {
            int ret = super.parseType(type);
            if ("blob.bitmap".equalsIgnoreCase(this.mTypeStr)) {
                return 1001;
            }
            this.mNoImageElement = true;
            return ret;
        }

        public void setNull(ScreenElementRoot root) {
            if (getImageElement(root) != null) {
                getImageElement(root).setBitmap(null);
            } else {
                set(null);
            }
        }

        public ImageScreenElement getImageElement(ScreenElementRoot root) {
            if (this.mImageVar == null && !this.mNoImageElement) {
                this.mImageVar = (ImageScreenElement) root.findElement(this.mName);
                this.mNoImageElement = this.mImageVar == null;
            }
            return this.mImageVar;
        }
    }

    public void createCountVar() {
        String str = this.mCountName;
        if (str == null) {
            this.mCountVar = null;
        } else {
            this.mCountVar = new IndexedVariable(str, getContext().mVariables, true);
        }
    }

    public ContentProviderBinder(Element node, ScreenElementRoot root) {
        super(node, root);
        this.mChangeObserver = new ChangeObserver();
        this.mUpdateInterval = -1;
        this.mNeedsRequery = true;
        this.mHandler = root.getContext().getHandler();
        this.mQueryHandler = new QueryHandler(getContext().mContext);
        if (node != null) {
            load(node);
        }
    }

    public ContentProviderBinder(ScreenElementRoot root) {
        this(null, root);
    }

    public void finish() {
        this.mLastUri = null;
        registerObserver(null, false);
        this.mHandler.removeCallbacks(this.mUpdater);
        setBlockedColumns(null);
        super.finish();
    }

    public void pause() {
        super.pause();
        this.mHandler.removeCallbacks(this.mUpdater);
    }

    public void resume() {
        super.resume();
        if (this.mNeedsRequery) {
            startQuery();
        } else {
            checkUpdate();
        }
    }

    public void refresh() {
        super.refresh();
        startQuery();
    }

    private void load(Element node) {
        Element element = node;
        Variables vars = getVariables();
        this.mUriFormatter = new TextFormatter(vars, element.getAttribute("uri"), element.getAttribute("uriFormat"), element.getAttribute("uriParas"), Expression.build(vars, element.getAttribute("uriExp")), Expression.build(vars, element.getAttribute("uriFormatExp")));
        String tmp = element.getAttribute("columns");
        String str = ",";
        this.mColumns = TextUtils.isEmpty(tmp) ? null : tmp.split(str);
        TextFormatter textFormatter = r3;
        TextFormatter textFormatter2 = new TextFormatter(vars, element.getAttribute("where"), element.getAttribute("whereFormat"), element.getAttribute("whereParas"), Expression.build(vars, element.getAttribute("whereExp")), Expression.build(vars, element.getAttribute("whereFormatExp")));
        this.mWhereFormatter = textFormatter;
        tmp = element.getAttribute("args");
        this.mArgs = TextUtils.isEmpty(tmp) ? null : tmp.split(str);
        String tmp2 = element.getAttribute("order");
        this.mOrder = TextUtils.isEmpty(tmp2) ? null : tmp2;
        tmp2 = element.getAttribute("countName");
        this.mCountName = TextUtils.isEmpty(tmp2) ? null : tmp2;
        tmp = this.mCountName;
        if (tmp != null) {
            this.mCountVar = new IndexedVariable(tmp, vars, true);
        }
        this.mUpdateInterval = Utils.getAttrAsInt(element, "updateInterval", -1);
        if (this.mUpdateInterval > 0) {
            this.mUpdater = new Runnable() {
                public void run() {
                    ContentProviderBinder.this.checkUpdate();
                }
            };
        }
        loadVariables(node);
        Element list = Utils.getChild(element, ListScreenElement.TAG_NAME);
        if (list != null) {
            try {
                this.mList = new List(list, this.mRoot);
            } catch (IllegalArgumentException e) {
                Log.e("ContentProviderBinder", "invalid List");
            }
        }
        this.mAwareChangeWhilePause = Boolean.parseBoolean(element.getAttribute("vigilant"));
    }

    /* Access modifiers changed, original: protected */
    public Variable onLoadVariable(Element child) {
        return new Variable(child, getContext().mVariables);
    }

    public void startQuery() {
        String uriText = getUriText();
        if (uriText == null) {
            Log.e("ContentProviderBinder", "start query: uri null");
            return;
        }
        if (!this.mSystemBootCompleted && uriText.startsWith("content://sms/")) {
            this.mSystemBootCompleted = "1".equals(SystemProperties.get("sys.boot_completed"));
            if (!this.mSystemBootCompleted) {
                return;
            }
        }
        this.mNeedsRequery = false;
        this.mQueryHandler.cancelOperation(100);
        Uri uri = Uri.parse(uriText);
        if (uri != null) {
            if (this.mUpdateInterval == -1 && !uri.equals(this.mLastUri)) {
                registerObserver(uri, true);
                this.mLastUri = uri;
            }
            this.mQueryHandler.startQuery(100, null, uri, this.mColumns, this.mWhereFormatter.getText(), this.mArgs, this.mOrder);
            this.mLastQueryTime = System.currentTimeMillis();
            checkUpdate();
        }
    }

    public final String getUriText() {
        return this.mUriFormatter.getText();
    }

    public final void setBlockedColumns(String[] cols) {
        HashSet<String> hs = null;
        if (cols != null) {
            hs = new HashSet();
            for (String s : cols) {
                hs.add(s);
            }
        }
        Iterator it = this.mVariables.iterator();
        while (it.hasNext()) {
            Variable var = (Variable) ((miui.maml.data.VariableBinder.Variable) it.next());
            var.mBlocked = hs != null ? hs.contains(var.mColumn) : false;
        }
    }

    private void onQueryComplete(Cursor cursor) {
        if (cursor != null) {
            if (!this.mFinished) {
                updateVariables(cursor);
            }
            cursor.close();
        }
        onUpdateComplete();
    }

    private void registerObserver(Uri uri, boolean reg) {
        StringBuilder stringBuilder;
        String str = "  uri:";
        String str2 = "ContentProviderBinder";
        ContentResolver cr = getContext().mContext.getContentResolver();
        cr.unregisterContentObserver(this.mChangeObserver);
        if (reg) {
            try {
                cr.registerContentObserver(uri, true, this.mChangeObserver);
            } catch (IllegalArgumentException e) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(e.toString());
                stringBuilder.append(str);
                stringBuilder.append(uri);
                Log.e(str2, stringBuilder.toString());
            } catch (SecurityException se) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(se.toString());
                stringBuilder.append(str);
                stringBuilder.append(uri);
                Log.e(str2, stringBuilder.toString());
            }
        }
    }

    private void updateVariables(Cursor cursor) {
        Cursor cursor2 = cursor;
        String str = "ContentProviderBinder";
        int count = cursor2 == null ? 0 : cursor.getCount();
        IndexedVariable indexedVariable = this.mCountVar;
        if (indexedVariable != null) {
            indexedVariable.set((double) count);
        }
        List list = this.mList;
        if (list != null) {
            list.fill(cursor2);
        }
        if (cursor2 == null || count == 0) {
            Iterator it = this.mVariables.iterator();
            while (it.hasNext()) {
                ((Variable) ((miui.maml.data.VariableBinder.Variable) it.next())).setNull(this.mRoot);
            }
            return;
        }
        Iterator it2 = this.mVariables.iterator();
        while (it2.hasNext()) {
            miui.maml.data.VariableBinder.Variable v = (miui.maml.data.VariableBinder.Variable) it2.next();
            Variable var = (Variable) v;
            if (!var.mBlocked) {
                double value = 0.0d;
                if (cursor2.moveToPosition(var.mRow)) {
                    StringBuilder stringBuilder;
                    try {
                        int col = cursor2.getColumnIndexOrThrow(var.mColumn);
                        if (cursor2.isNull(col)) {
                            var.setNull(this.mRoot);
                        } else {
                            int i = v.mType;
                            if (i == 2) {
                                v.set((Object) cursor2.getString(col));
                            } else if (i == 1001 || i == 7) {
                                Object bitmap = null;
                                byte[] valueBytes = cursor2.getBlob(col);
                                if (valueBytes != null) {
                                    bitmap = BitmapFactory.decodeByteArray(valueBytes, 0, valueBytes.length);
                                }
                                if (v.mType == 7) {
                                    v.set(bitmap);
                                } else {
                                    ImageScreenElement image = var.getImageElement(this.mRoot);
                                    if (image != null) {
                                        image.setBitmap(bitmap);
                                    }
                                }
                            } else if (i == 8) {
                                java.util.List<Double> nums = new ArrayList();
                                do {
                                    nums.add(Double.valueOf(cursor2.getDouble(col)));
                                } while (cursor.moveToNext());
                                v.set(nums.toArray());
                            } else if (i != 9) {
                                int i2 = v.mType;
                                if (i2 == 3) {
                                    value = (double) cursor2.getInt(col);
                                } else if (i2 == 4) {
                                    value = (double) cursor2.getLong(col);
                                } else if (i2 == 5) {
                                    value = (double) cursor2.getFloat(col);
                                } else if (i2 != 6) {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("invalide type");
                                    stringBuilder.append(v.mTypeStr);
                                    Log.w(str, stringBuilder.toString());
                                } else {
                                    value = cursor2.getDouble(col);
                                }
                                v.set(value);
                            } else {
                                java.util.List<String> strings = new ArrayList();
                                do {
                                    strings.add(cursor2.getString(col));
                                } while (cursor.moveToNext());
                                v.set(strings.toArray());
                            }
                        }
                    } catch (NumberFormatException e) {
                        Log.w(str, String.format("failed to get value from cursor", new Object[0]));
                    } catch (IllegalArgumentException e2) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("column does not exist: ");
                        stringBuilder.append(var.mColumn);
                        Log.e(str, stringBuilder.toString());
                    } catch (Exception e3) {
                        Log.e(str, e3.toString());
                    }
                }
            }
        }
    }

    public void onContentChanged() {
        Log.i("ContentProviderBinder", "ChangeObserver: content changed.");
        if (!this.mFinished) {
            if (!this.mPaused || this.mAwareChangeWhilePause) {
                startQuery();
            } else {
                this.mNeedsRequery = true;
            }
        }
    }

    private void checkUpdate() {
        if (this.mUpdateInterval > 0) {
            this.mHandler.removeCallbacks(this.mUpdater);
            long elapsedTime = System.currentTimeMillis() - this.mLastQueryTime;
            if (elapsedTime >= ((long) (this.mUpdateInterval * 1000))) {
                startQuery();
                elapsedTime = 0;
            }
            this.mHandler.postDelayed(this.mUpdater, ((long) (this.mUpdateInterval * 1000)) - elapsedTime);
        }
    }
}
