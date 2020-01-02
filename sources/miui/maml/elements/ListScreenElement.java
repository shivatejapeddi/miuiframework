package miui.maml.elements;

import android.app.slice.SliceItem;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import com.miui.enterprise.sdk.ApplicationManager;
import java.util.ArrayList;
import java.util.Iterator;
import miui.maml.ScreenElementRoot;
import miui.maml.data.ContextVariables;
import miui.maml.data.Expression;
import miui.maml.data.IndexedVariable;
import miui.maml.elements.VariableArrayElement.VarObserver;
import miui.maml.util.Utils;
import miui.maml.util.Utils.XmlTraverseListener;
import org.w3c.dom.Element;

public class ListScreenElement extends ElementGroup {
    private static double ACC = -800.0d;
    private static String DATA_TYPE_BITMAP = "bitmap";
    private static String DATA_TYPE_DOUBLE = "double";
    private static String DATA_TYPE_FLOAT = ApplicationManager.FLOAT;
    private static String DATA_TYPE_INTEGER = SliceItem.FORMAT_INT;
    private static String DATA_TYPE_INTEGER1 = "integer";
    private static String DATA_TYPE_LONG = "long";
    private static String DATA_TYPE_STRING = "string";
    private static final String LOG_TAG = "ListScreenElement";
    public static final String TAG_NAME = "List";
    protected AttrDataBinders mAttrDataBinders;
    private int mBottomIndex;
    private int mCachedItemCount;
    private boolean mClearOnFinish;
    private ArrayList<ColumnInfo> mColumnsInfo;
    private int mCurrentIndex = -1;
    private long mCurrentTime;
    private ArrayList<DataIndexMap> mDataList = new ArrayList();
    private ArrayList<Integer> mIndexOrder = new ArrayList();
    private IndexedVariable[] mIndexedVariables;
    private ElementGroup mInnerGroup;
    private boolean mIsChildScroll;
    private boolean mIsScroll;
    private boolean mIsUpDirection;
    private ListItemElement mItem;
    private int mItemCount;
    private long mLastTime;
    protected ListData mListData;
    private Expression mMaxHeight;
    private boolean mMoving;
    private double mOffsetX;
    private double mOffsetY;
    private boolean mPressed;
    private ArrayList<Integer> mReuseIndex = new ArrayList();
    private AnimatedScreenElement mScrollBar;
    private int mSelectedId;
    private IndexedVariable mSelectedIdVar;
    private double mSpeed;
    private long mStartAnimTime;
    private float mStartAnimY;
    private int mTopIndex;
    private double mTouchStartX;
    private double mTouchStartY;
    private int mVisibleItemCount;

    public static class Column {
        public ListScreenElement mList;
        public String mName;
        public VarObserver mObserver;
        public ScreenElementRoot mRoot;
        public String mTarget;
        public VariableArrayElement mTargetElement;

        public Column(Element node, ScreenElementRoot root, ListScreenElement list) {
            this.mRoot = root;
            this.mList = list;
            if (node != null) {
                load(node);
            }
        }

        private void load(Element node) {
            this.mName = node.getAttribute("name");
            this.mTarget = node.getAttribute("target");
            this.mObserver = new VarObserver() {
                public void onDataChange(Object[] objects) {
                    Column.this.mList.addColumn(Column.this.mName, objects);
                }
            };
        }

        public void init() {
            if (this.mTargetElement == null) {
                ScreenElement ele = this.mRoot.findElement(this.mTarget);
                if (ele instanceof VariableArrayElement) {
                    this.mTargetElement = (VariableArrayElement) ele;
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("can't find VarArray:");
                    stringBuilder.append(this.mTarget);
                    Log.e(ListScreenElement.LOG_TAG, stringBuilder.toString());
                    return;
                }
            }
            this.mTargetElement.registerVarObserver(this.mObserver, true);
        }

        public void finish() {
            VariableArrayElement variableArrayElement = this.mTargetElement;
            if (variableArrayElement != null) {
                variableArrayElement.registerVarObserver(this.mObserver, false);
            }
        }
    }

    public static class ColumnInfo {
        public Type mType;
        public String mVarName;

        public enum Type {
            STRING,
            BITMAP,
            INTEGER,
            DOUBLE,
            LONG,
            FLOAT;

            public boolean isNumber() {
                return this == INTEGER || this == DOUBLE || this == LONG || this == FLOAT;
            }
        }

        public ColumnInfo(String item) {
            int index = item.indexOf(":");
            if (index != -1) {
                this.mVarName = item.substring(0, index);
                String type = item.substring(index + 1);
                if (ListScreenElement.DATA_TYPE_STRING.equals(type)) {
                    this.mType = Type.STRING;
                    return;
                } else if (ListScreenElement.DATA_TYPE_BITMAP.equals(type)) {
                    this.mType = Type.BITMAP;
                    return;
                } else if (ListScreenElement.DATA_TYPE_INTEGER.equals(type) || ListScreenElement.DATA_TYPE_INTEGER1.equals(type)) {
                    this.mType = Type.INTEGER;
                    return;
                } else if (ListScreenElement.DATA_TYPE_DOUBLE.equals(type)) {
                    this.mType = Type.DOUBLE;
                    return;
                } else if (ListScreenElement.DATA_TYPE_LONG.equals(type)) {
                    this.mType = Type.LONG;
                    return;
                } else if (ListScreenElement.DATA_TYPE_FLOAT.equals(type)) {
                    this.mType = Type.FLOAT;
                    return;
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("List: invalid item data type:");
                    stringBuilder.append(type);
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("List: invalid item data ");
            stringBuilder2.append(item);
            throw new IllegalArgumentException(stringBuilder2.toString());
        }

        public boolean validate(Object data) {
            if (data == null) {
                return true;
            }
            switch (this.mType) {
                case STRING:
                    return data instanceof String;
                case BITMAP:
                    return data instanceof Bitmap;
                case INTEGER:
                    return data instanceof Integer;
                case DOUBLE:
                    return data instanceof Double;
                case LONG:
                    return data instanceof Long;
                case FLOAT:
                    return data instanceof Float;
                default:
                    return false;
            }
        }

        public static ArrayList<ColumnInfo> createColumnsInfo(String data) {
            if (TextUtils.isEmpty(data)) {
                return null;
            }
            ArrayList<ColumnInfo> ret = new ArrayList();
            for (String item : data.split(",")) {
                ret.add(new ColumnInfo(item));
            }
            return ret;
        }
    }

    private static class DataIndexMap {
        public Object[] mData;
        public int mElementIndex;
        public boolean mNeedRebind;

        public DataIndexMap(Object[] data, int elementIndex) {
            this.mElementIndex = -1;
            this.mData = data;
            this.mElementIndex = elementIndex;
        }

        public DataIndexMap(Object[] data) {
            this(data, -1);
        }

        public void setData(int index, Object data) {
            Object[] objArr = this.mData;
            if (objArr != null && objArr.length > index) {
                objArr[index] = data;
                this.mNeedRebind = true;
            }
        }
    }

    public static class ListData {
        public ArrayList<Column> mColumns = new ArrayList();
        public ListScreenElement mList;
        public ScreenElementRoot mRoot;

        public ListData(Element node, ScreenElementRoot root, ListScreenElement list) {
            this.mRoot = root;
            this.mList = list;
            if (node != null) {
                load(node);
            }
        }

        private void load(Element node) {
            Utils.traverseXmlElementChildren(node, "Column", new XmlTraverseListener() {
                public void onChild(Element child) {
                    ListData.this.mColumns.add(new Column(child, ListData.this.mRoot, ListData.this.mList));
                }
            });
        }

        public void init() {
            Iterator it = this.mColumns.iterator();
            while (it.hasNext()) {
                Column column = (Column) it.next();
                if (column != null) {
                    column.init();
                }
            }
        }

        public void finish() {
            Iterator it = this.mColumns.iterator();
            while (it.hasNext()) {
                Column column = (Column) it.next();
                if (column != null) {
                    column.finish();
                }
            }
        }
    }

    private static class ListItemElement extends ElementGroup {
        public static final String TAG_NAME = "Item";
        private int mDataIndex = -1;
        private AnimatedScreenElement mDivider;
        protected Element mNode;

        public int getDataIndex() {
            return this.mDataIndex;
        }

        public void setDataIndex(int dataIndex) {
            this.mDataIndex = dataIndex;
            AnimatedScreenElement animatedScreenElement = this.mDivider;
            if (animatedScreenElement == null) {
                return;
            }
            if (dataIndex <= 0) {
                animatedScreenElement.show(false);
            } else {
                animatedScreenElement.show(true);
            }
        }

        public ListItemElement(Element node, ScreenElementRoot root) {
            super(node, root);
            this.mNode = node;
            ScreenElement se = findElement("divider");
            if (se instanceof AnimatedScreenElement) {
                this.mDivider = (AnimatedScreenElement) se;
                removeElement(se);
                addElement(this.mDivider);
            }
            this.mAlignV = AlignV.TOP;
        }
    }

    public ListScreenElement(Element node, ScreenElementRoot root) {
        super(node, root);
        ListItemElement listItemElement = this.mItem;
        String str = LOG_TAG;
        if (listItemElement != null) {
            setClip(true);
            this.mMaxHeight = Expression.build(getVariables(), node.getAttribute("maxHeight"));
            this.mClearOnFinish = Boolean.parseBoolean(node.getAttribute("clearOnFinish"));
            String data = node.getAttribute("data");
            if (TextUtils.isEmpty(data)) {
                Log.e(str, "no data");
                throw new IllegalArgumentException("List: no data");
            }
            this.mColumnsInfo = ColumnInfo.createColumnsInfo(data);
            ArrayList arrayList = this.mColumnsInfo;
            if (arrayList != null) {
                this.mIndexedVariables = new IndexedVariable[arrayList.size()];
                Element child = Utils.getChild(node, AttrDataBinders.TAG);
                if (child != null) {
                    this.mAttrDataBinders = new AttrDataBinders(child, this.mRoot.getContext().mContextVariables);
                    Element child2 = Utils.getChild(node, "Data");
                    if (child2 != null) {
                        this.mListData = new ListData(child2, this.mRoot, this);
                    }
                    child = findElement("scrollbar");
                    if (child instanceof AnimatedScreenElement) {
                        this.mScrollBar = (AnimatedScreenElement) child;
                        this.mScrollBar.mAlignV = AlignV.TOP;
                        removeElement(child);
                        addElement(this.mScrollBar);
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(this.mName);
                    stringBuilder.append(".selectedId");
                    this.mSelectedIdVar = new IndexedVariable(stringBuilder.toString(), this.mRoot.getContext().mVariables, true);
                    return;
                }
                Log.e(str, "no attr data binder");
                throw new IllegalArgumentException("List: no attr data binder");
            }
            Log.e(str, "invalid item data");
            throw new IllegalArgumentException("List: invalid item data");
        }
        Log.e(str, "no item");
        throw new IllegalArgumentException("List: no item");
    }

    /* Access modifiers changed, original: protected */
    public ScreenElement onCreateChild(Element ele) {
        if (!ele.getTagName().equalsIgnoreCase(ListItemElement.TAG_NAME) || this.mInnerGroup != null) {
            return super.onCreateChild(ele);
        }
        this.mItem = new ListItemElement(ele, this.mRoot);
        this.mInnerGroup = new ElementGroup(null, this.mRoot);
        return this.mInnerGroup;
    }

    public ArrayList<ColumnInfo> getColumnsInfo() {
        return this.mColumnsInfo;
    }

    public void removeAllItems() {
        this.mInnerGroup.removeAllElements();
        this.mInnerGroup.setY(0.0d);
        this.mDataList.clear();
        this.mIndexOrder.clear();
        this.mReuseIndex.clear();
        this.mCurrentIndex = -1;
        this.mItemCount = 0;
        setActualHeight(descale((double) getHeight()));
    }

    public void addColumn(String columnName, Object[] objects) {
        if (columnName != null && objects != null) {
            int index;
            int columnIndex = -1;
            int columnSize = this.mColumnsInfo.size();
            for (index = 0; index < columnSize; index++) {
                if (columnName.equals(((ColumnInfo) this.mColumnsInfo.get(index)).mVarName)) {
                    columnIndex = index;
                    break;
                }
            }
            if (columnIndex >= 0) {
                int i;
                index = objects.length;
                int dataSize = this.mDataList.size();
                for (i = 0; i < dataSize; i++) {
                    Object data = null;
                    if (i < index) {
                        data = objects[i];
                    }
                    ((DataIndexMap) this.mDataList.get(i)).setData(columnIndex, data);
                    if (((DataIndexMap) this.mDataList.get(i)).mElementIndex >= 0) {
                        getItem(i);
                    }
                }
                for (i = dataSize; i < index; i++) {
                    Object[] data2 = new Object[this.mColumnsInfo.size()];
                    data2[columnIndex] = objects[i];
                    addItem(data2);
                }
                clearEmptyRow();
                requestUpdate();
            }
        }
    }

    private void clearEmptyRow() {
        int i = this.mDataList.size() - 1;
        while (i >= 0) {
            boolean isEmpty = true;
            for (Object data : ((DataIndexMap) this.mDataList.get(i)).mData) {
                if (data != null) {
                    isEmpty = false;
                    break;
                }
            }
            if (isEmpty) {
                removeItem(i);
                i--;
            } else {
                return;
            }
        }
    }

    public void addItem(Object... objects) {
        if (objects != null) {
            int length = objects.length;
            int size = this.mColumnsInfo.size();
            String str = LOG_TAG;
            if (length != size) {
                Log.e(str, "invalid item data count");
                return;
            }
            length = objects.length;
            size = 0;
            while (size < length) {
                if (((ColumnInfo) this.mColumnsInfo.get(size)).validate(objects[size])) {
                    size++;
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("invalid item data type: ");
                    stringBuilder.append(objects[size]);
                    Log.e(str, stringBuilder.toString());
                    return;
                }
            }
            this.mDataList.add(new DataIndexMap((Object[]) objects.clone()));
            this.mCurrentIndex++;
            this.mItemCount++;
            setActualHeight(descale((double) getHeight()));
            this.mVisibleItemCount = (int) (Math.max(super.getHeight(), scale(evaluate(this.mMaxHeight))) / this.mItem.getHeight());
            this.mCachedItemCount = this.mVisibleItemCount * 2;
            size = this.mInnerGroup.getElements().size();
            if (size < this.mCachedItemCount) {
                ListItemElement item = new ListItemElement(this.mItem.mNode, this.mItem.mRoot);
                this.mInnerGroup.addElement(item);
                ((DataIndexMap) this.mDataList.get(this.mCurrentIndex)).mElementIndex = size;
                this.mSelectedId = this.mCurrentIndex;
                item.init();
                this.mSelectedId = -1;
                bindData(item, size, this.mCurrentIndex);
                this.mIndexOrder.add(Integer.valueOf(this.mCurrentIndex));
            }
            requestUpdate();
        }
    }

    private void bindData(ListItemElement item, int elementIndex, int dataIndex) {
        if (dataIndex < 0 || dataIndex >= this.mItemCount) {
            Log.e(LOG_TAG, "invalid item data");
            return;
        }
        Object[] objects = ((DataIndexMap) this.mDataList.get(dataIndex)).mData;
        item.setDataIndex(dataIndex);
        ((DataIndexMap) this.mDataList.get(dataIndex)).mElementIndex = elementIndex;
        ((DataIndexMap) this.mDataList.get(dataIndex)).mNeedRebind = false;
        item.setY((double) ((float) ((double) (((float) dataIndex) * this.mItem.getHeight()))));
        int N = this.mColumnsInfo.size();
        ContextVariables vars = getContext().mContextVariables;
        for (int i = 0; i < N; i++) {
            vars.setVar(((ColumnInfo) this.mColumnsInfo.get(i)).mVarName, objects[i]);
        }
        AttrDataBinders attrDataBinders = this.mAttrDataBinders;
        if (attrDataBinders != null) {
            attrDataBinders.bind(item);
        }
    }

    private void checkVisibility() {
        ArrayList<ScreenElement> items = this.mInnerGroup.getElements();
        for (int index = 0; index < items.size(); index++) {
            ListItemElement lie = (ListItemElement) items.get(index);
            int dataIndex = lie.getDataIndex();
            if (dataIndex < 0 || dataIndex < this.mTopIndex || dataIndex > this.mBottomIndex) {
                if (lie.isVisible()) {
                    lie.show(false);
                }
            } else if (!lie.isVisible()) {
                lie.show(true);
            }
        }
    }

    private void setVariables() {
        int N = this.mColumnsInfo.size();
        int i = 0;
        while (i < N) {
            ColumnInfo columnInfo = (ColumnInfo) this.mColumnsInfo.get(i);
            if (columnInfo.mType != Type.BITMAP) {
                IndexedVariable[] indexedVariableArr = this.mIndexedVariables;
                if (indexedVariableArr[i] == null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(this.mName);
                    stringBuilder.append(".");
                    stringBuilder.append(columnInfo.mVarName);
                    indexedVariableArr[i] = new IndexedVariable(stringBuilder.toString(), this.mRoot.getContext().mVariables, columnInfo.mType.isNumber());
                }
                IndexedVariable indexedVariable = this.mIndexedVariables[i];
                int i2 = this.mSelectedId;
                indexedVariable.set(i2 < 0 ? null : ((DataIndexMap) this.mDataList.get(i2)).mData[i]);
            }
            i++;
        }
    }

    public void removeItem(int index) {
        if (index >= 0 && index < this.mItemCount) {
            int i;
            this.mDataList.remove(index);
            this.mItemCount--;
            setActualHeight(descale((double) getHeight()));
            int length = this.mIndexOrder.size();
            int removeIndex = 0;
            for (i = 0; i < length; i++) {
                ListItemElement item = (ListItemElement) this.mInnerGroup.getElements().get(((Integer) this.mIndexOrder.get(i)).intValue());
                int dataIndex = item.getDataIndex();
                if (dataIndex == index) {
                    removeIndex = i;
                    item.setDataIndex(-1);
                    item.setY(-1.7976931348623157E308d);
                    item.show(false);
                } else if (dataIndex > index) {
                    item.setDataIndex(dataIndex - 1);
                    item.setY((double) (((float) (dataIndex - 1)) * this.mItem.getHeight()));
                }
            }
            if (length > 0) {
                i = ((Integer) this.mIndexOrder.remove(removeIndex)).intValue();
                moveTo((double) this.mInnerGroup.getY());
                this.mReuseIndex.add(Integer.valueOf(i));
            }
            requestUpdate();
        }
    }

    private int getUseableElementIndex() {
        int elementIndex;
        if (this.mReuseIndex.size() > 0) {
            elementIndex = ((Integer) this.mReuseIndex.remove(0)).intValue();
        } else if (this.mIsUpDirection) {
            elementIndex = ((Integer) this.mIndexOrder.remove(0)).intValue();
        } else {
            ArrayList arrayList = this.mIndexOrder;
            elementIndex = ((Integer) arrayList.remove(arrayList.size() - 1)).intValue();
        }
        if (this.mIsUpDirection) {
            this.mIndexOrder.add(Integer.valueOf(elementIndex));
        } else {
            this.mIndexOrder.add(0, Integer.valueOf(elementIndex));
        }
        return elementIndex;
    }

    private ListItemElement getItem(int index) {
        if (index < 0 || index >= this.mItemCount) {
            return null;
        }
        int elementIndex = ((DataIndexMap) this.mDataList.get(index)).mElementIndex;
        ListItemElement item = null;
        if (elementIndex >= 0) {
            item = (ListItemElement) this.mInnerGroup.getElements().get(elementIndex);
        }
        if (elementIndex < 0 || item.getDataIndex() != index) {
            elementIndex = getUseableElementIndex();
            item = (ListItemElement) this.mInnerGroup.getElements().get(elementIndex);
            if (item.getDataIndex() < 0) {
                item.reset();
            }
        }
        if (item.getDataIndex() != index || ((DataIndexMap) this.mDataList.get(index)).mNeedRebind) {
            bindData(item, elementIndex, index);
        }
        return item;
    }

    public boolean onTouch(MotionEvent event) {
        boolean z = false;
        if (!isVisible()) {
            return false;
        }
        float x = event.getX();
        float y = event.getY();
        boolean ret = false;
        int actionMasked = event.getActionMasked();
        MotionEvent motionEvent;
        if (actionMasked == 0) {
            motionEvent = event;
            if (touched(x, y)) {
                this.mMoving = true;
                this.mPressed = true;
                performAction("down");
                onActionDown(x, y);
                this.mStartAnimTime = -1;
                this.mSpeed = 0.0d;
                this.mLastTime = SystemClock.elapsedRealtime();
                this.mSelectedId = (int) Math.floor((double) ((y - this.mInnerGroup.getAbsoluteTop()) / this.mItem.getHeight()));
                this.mSelectedIdVar.set((double) this.mSelectedId);
                setVariables();
                this.mTouchStartX = (double) x;
                this.mTouchStartY = (double) y;
                updateScorllBar();
                ret = true;
            }
        } else if (actionMasked == 1) {
            motionEvent = event;
            this.mPressed = false;
            if (this.mMoving) {
                Log.i(LOG_TAG, "unlock touch up");
                performAction("up");
                onActionUp();
                if (this.mSpeed < 400.0d) {
                    resetInner();
                } else {
                    startAnimation();
                }
                ret = true;
            }
        } else if (actionMasked != 2) {
            if (actionMasked != 3) {
                motionEvent = event;
            } else {
                this.mPressed = false;
                if (this.mMoving) {
                    performAction("cancel");
                    resetInner();
                    this.mStartAnimTime = -1;
                    ret = true;
                    motionEvent = event;
                } else {
                    motionEvent = event;
                }
            }
        } else if (this.mMoving) {
            this.mCurrentTime = SystemClock.elapsedRealtime();
            this.mOffsetY = ((double) y) - this.mTouchStartY;
            this.mOffsetX = ((double) x) - this.mTouchStartX;
            if (!(this.mIsScroll || this.mIsChildScroll)) {
                double absOffsetY = Math.abs(this.mOffsetY);
                double absOffsetX = Math.abs(this.mOffsetX);
                if (absOffsetY > 5.0d && !this.mIsChildScroll && absOffsetY >= absOffsetX) {
                    this.mIsScroll = true;
                } else if (absOffsetX > 5.0d && !this.mIsScroll && absOffsetY < absOffsetX) {
                    this.mIsChildScroll = true;
                }
            }
            boolean z2 = this.mOffsetY < 0.0d || this.mIsChildScroll;
            this.mIsUpDirection = z2;
            if (this.mIsScroll) {
                event.setAction(3);
                performAction("move");
                onActionMove(x, y);
                this.mSpeed = (Math.abs(this.mOffsetY) / ((double) (this.mCurrentTime - this.mLastTime))) * 1000.0d;
                moveTo(((double) this.mInnerGroup.getY()) + this.mOffsetY);
                this.mTouchStartY = (double) y;
                this.mLastTime = this.mCurrentTime;
            } else {
                motionEvent = event;
            }
            ret = true;
        } else {
            motionEvent = event;
        }
        if (super.onTouch(event) || (ret && this.mInterceptTouch)) {
            z = true;
        }
        return z;
    }

    private void updateScorllBar() {
        if (this.mScrollBar != null && this.mIsScroll) {
            double itemsHeight = (double) (((float) this.mItemCount) * this.mItem.getHeight());
            double zoneHeight = (double) getHeight();
            double rate = zoneHeight / itemsHeight;
            boolean isShow = true;
            if (rate >= 1.0d) {
                rate = 0.0d;
                isShow = false;
            }
            double yRate = ((double) this.mInnerGroup.getY()) / (zoneHeight - itemsHeight);
            if (yRate > 1.0d) {
                yRate = 1.0d;
            }
            this.mScrollBar.setY((double) ((float) (((1.0d - rate) * zoneHeight) * yRate)));
            this.mScrollBar.setH((double) ((float) (zoneHeight * rate)));
            if (this.mScrollBar.isVisible() != isShow) {
                this.mScrollBar.show(isShow);
            }
        }
    }

    private void startAnimation() {
        this.mStartAnimTime = SystemClock.elapsedRealtime();
        this.mStartAnimY = this.mInnerGroup.getY();
    }

    private void moveTo(double y) {
        if (y < ((double) (getHeight() - (((float) this.mItemCount) * this.mItem.getHeight())))) {
            y = (double) (getHeight() - (((float) this.mItemCount) * this.mItem.getHeight()));
            this.mStartAnimTime = 0;
        }
        if (y > 0.0d) {
            y = 0.0d;
            this.mStartAnimTime = 0;
        }
        this.mInnerGroup.setY((double) ((float) y));
        this.mTopIndex = Math.min((int) Math.floor((-y) / ((double) this.mItem.getHeight())), (this.mItemCount - ((int) (getHeight() / this.mItem.getHeight()))) - 1);
        this.mBottomIndex = Math.min(((int) (getHeight() / this.mItem.getHeight())) + this.mTopIndex, this.mItemCount - 1);
        for (int index = this.mTopIndex; index <= this.mBottomIndex; index++) {
            getItem(index);
        }
        checkVisibility();
        updateScorllBar();
    }

    /* Access modifiers changed, original: protected */
    public void doTick(long currentTime) {
        super.doTick(currentTime);
        long j = this.mStartAnimTime;
        if (j >= 0 && !this.mPressed) {
            long time = currentTime - j;
            if (j != 0) {
                double d = this.mSpeed;
                double d2 = ACC;
                if (((((double) time) * d2) / 1000.0d) + d >= 0.0d) {
                    this.mOffsetY = ((d * ((double) time)) / 1000.0d) + ((((d2 * 0.5d) * ((double) time)) * ((double) time)) / 1000000.0d);
                    moveTo(((double) this.mStartAnimY) + (this.mIsUpDirection ? -this.mOffsetY : this.mOffsetY));
                    requestUpdate();
                }
            }
            resetInner();
            requestUpdate();
        }
    }

    public void finish() {
        super.finish();
        if (this.mClearOnFinish) {
            removeAllItems();
        }
        ListData listData = this.mListData;
        if (listData != null) {
            listData.finish();
        }
    }

    public void init() {
        super.init();
        resetInner();
        this.mInnerGroup.setY(0.0d);
        setActualHeight(descale((double) getHeight()));
        this.mSelectedId = -1;
        this.mSelectedIdVar.set((double) this.mSelectedId);
        setVariables();
        ListData listData = this.mListData;
        if (listData != null) {
            listData.init();
        }
    }

    private void resetInner() {
        AnimatedScreenElement animatedScreenElement = this.mScrollBar;
        if (animatedScreenElement != null) {
            animatedScreenElement.show(false);
        }
        this.mMoving = false;
        this.mIsScroll = false;
        this.mIsChildScroll = false;
        this.mStartAnimTime = -1;
        this.mSpeed = 0.0d;
    }

    public float getHeight() {
        return this.mMaxHeight == null ? super.getHeight() : Math.min(((float) this.mItemCount) * this.mItem.getHeight(), scale(evaluate(this.mMaxHeight)));
    }

    public ScreenElement findElement(String name) {
        int i = this.mSelectedId;
        if (i >= 0 && i < this.mItemCount) {
            i = ((DataIndexMap) this.mDataList.get(i)).mElementIndex;
            if (i >= 0) {
                ScreenElement ele = ((ListItemElement) this.mInnerGroup.getElements().get(i)).findElement(name);
                if (ele != null) {
                    return ele;
                }
            }
        }
        return super.findElement(name);
    }
}
