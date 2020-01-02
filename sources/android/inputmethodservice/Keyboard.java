package android.inputmethodservice;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import com.android.internal.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.xmlpull.v1.XmlPullParserException;

@Deprecated
public class Keyboard {
    public static final int EDGE_BOTTOM = 8;
    public static final int EDGE_LEFT = 1;
    public static final int EDGE_RIGHT = 2;
    public static final int EDGE_TOP = 4;
    private static final int GRID_HEIGHT = 5;
    private static final int GRID_SIZE = 50;
    private static final int GRID_WIDTH = 10;
    public static final int KEYCODE_ALT = -6;
    public static final int KEYCODE_CANCEL = -3;
    public static final int KEYCODE_DELETE = -5;
    public static final int KEYCODE_DONE = -4;
    public static final int KEYCODE_MODE_CHANGE = -2;
    public static final int KEYCODE_SHIFT = -1;
    private static float SEARCH_DISTANCE = 1.8f;
    static final String TAG = "Keyboard";
    private static final String TAG_KEY = "Key";
    private static final String TAG_KEYBOARD = "Keyboard";
    private static final String TAG_ROW = "Row";
    private int mCellHeight;
    private int mCellWidth;
    private int mDefaultHeight;
    private int mDefaultHorizontalGap;
    private int mDefaultVerticalGap;
    private int mDefaultWidth;
    private int mDisplayHeight;
    private int mDisplayWidth;
    private int[][] mGridNeighbors;
    private int mKeyHeight;
    private int mKeyWidth;
    private int mKeyboardMode;
    private List<Key> mKeys;
    private CharSequence mLabel;
    @UnsupportedAppUsage
    private List<Key> mModifierKeys;
    private int mProximityThreshold;
    private int[] mShiftKeyIndices;
    private Key[] mShiftKeys;
    private boolean mShifted;
    @UnsupportedAppUsage
    private int mTotalHeight;
    @UnsupportedAppUsage
    private int mTotalWidth;
    private ArrayList<Row> rows;

    public static class Key {
        private static final int[] KEY_STATE_NORMAL = new int[0];
        private static final int[] KEY_STATE_NORMAL_OFF = new int[]{16842911};
        private static final int[] KEY_STATE_NORMAL_ON = new int[]{16842911, 16842912};
        private static final int[] KEY_STATE_PRESSED = new int[]{16842919};
        private static final int[] KEY_STATE_PRESSED_OFF = new int[]{16842919, 16842911};
        private static final int[] KEY_STATE_PRESSED_ON = new int[]{16842919, 16842911, 16842912};
        public int[] codes;
        public int edgeFlags;
        public int gap;
        public int height;
        public Drawable icon;
        public Drawable iconPreview;
        private Keyboard keyboard;
        public CharSequence label;
        public boolean modifier;
        public boolean on;
        public CharSequence popupCharacters;
        public int popupResId;
        public boolean pressed;
        public boolean repeatable;
        public boolean sticky;
        public CharSequence text;
        public int width;
        public int x;
        public int y;

        public Key(Row parent) {
            this.keyboard = parent.parent;
            this.height = parent.defaultHeight;
            this.width = parent.defaultWidth;
            this.gap = parent.defaultHorizontalGap;
            this.edgeFlags = parent.rowEdgeFlags;
        }

        public Key(Resources res, Row parent, int x, int y, XmlResourceParser parser) {
            this(parent);
            this.x = x;
            this.y = y;
            TypedArray a = res.obtainAttributes(Xml.asAttributeSet(parser), R.styleable.Keyboard);
            this.width = Keyboard.getDimensionOrFraction(a, 0, this.keyboard.mDisplayWidth, parent.defaultWidth);
            this.height = Keyboard.getDimensionOrFraction(a, 1, this.keyboard.mDisplayHeight, parent.defaultHeight);
            this.gap = Keyboard.getDimensionOrFraction(a, 2, this.keyboard.mDisplayWidth, parent.defaultHorizontalGap);
            a.recycle();
            a = res.obtainAttributes(Xml.asAttributeSet(parser), R.styleable.Keyboard_Key);
            this.x += this.gap;
            TypedValue codesValue = new TypedValue();
            a.getValue(0, codesValue);
            if (codesValue.type == 16 || codesValue.type == 17) {
                this.codes = new int[]{codesValue.data};
            } else if (codesValue.type == 3) {
                this.codes = parseCSV(codesValue.string.toString());
            }
            this.iconPreview = a.getDrawable(7);
            Drawable drawable = this.iconPreview;
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), this.iconPreview.getIntrinsicHeight());
            }
            this.popupCharacters = a.getText(2);
            this.popupResId = a.getResourceId(1, 0);
            this.repeatable = a.getBoolean(6, false);
            this.modifier = a.getBoolean(4, false);
            this.sticky = a.getBoolean(5, false);
            this.edgeFlags = a.getInt(3, 0);
            this.edgeFlags |= parent.rowEdgeFlags;
            this.icon = a.getDrawable(10);
            drawable = this.icon;
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), this.icon.getIntrinsicHeight());
            }
            this.label = a.getText(9);
            this.text = a.getText(8);
            if (this.codes == null && !TextUtils.isEmpty(this.label)) {
                this.codes = new int[]{this.label.charAt(0)};
            }
            a.recycle();
        }

        public void onPressed() {
            this.pressed ^= 1;
        }

        public void onReleased(boolean inside) {
            this.pressed ^= 1;
            if (this.sticky && inside) {
                this.on ^= 1;
            }
        }

        /* Access modifiers changed, original: 0000 */
        public int[] parseCSV(String value) {
            int count = 0;
            int lastIndex = 0;
            String str = ",";
            if (value.length() > 0) {
                while (true) {
                    count++;
                    int indexOf = value.indexOf(str, lastIndex + 1);
                    lastIndex = indexOf;
                    if (indexOf <= 0) {
                        break;
                    }
                }
            }
            int[] values = new int[count];
            count = 0;
            StringTokenizer st = new StringTokenizer(value, str);
            while (st.hasMoreTokens()) {
                int count2 = count + 1;
                try {
                    values[count] = Integer.parseInt(st.nextToken());
                } catch (NumberFormatException e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Error parsing keycodes ");
                    stringBuilder.append(value);
                    Log.e("Keyboard", stringBuilder.toString());
                }
                count = count2;
            }
            return values;
        }

        public boolean isInside(int x, int y) {
            boolean leftEdge = (this.edgeFlags & 1) > 0;
            boolean rightEdge = (this.edgeFlags & 2) > 0;
            boolean topEdge = (this.edgeFlags & 4) > 0;
            boolean bottomEdge = (this.edgeFlags & 8) > 0;
            int i = this.x;
            if (x >= i || (leftEdge && x <= i + this.width)) {
                i = this.x;
                if (x < this.width + i || (rightEdge && x >= i)) {
                    i = this.y;
                    if (y >= i || (topEdge && y <= i + this.height)) {
                        i = this.y;
                        if (y < this.height + i || (bottomEdge && y >= i)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        public int squaredDistanceFrom(int x, int y) {
            int xDist = (this.x + (this.width / 2)) - x;
            int yDist = (this.y + (this.height / 2)) - y;
            return (xDist * xDist) + (yDist * yDist);
        }

        public int[] getCurrentDrawableState() {
            int[] states = KEY_STATE_NORMAL;
            if (this.on) {
                if (this.pressed) {
                    return KEY_STATE_PRESSED_ON;
                }
                return KEY_STATE_NORMAL_ON;
            } else if (this.sticky) {
                if (this.pressed) {
                    return KEY_STATE_PRESSED_OFF;
                }
                return KEY_STATE_NORMAL_OFF;
            } else if (this.pressed) {
                return KEY_STATE_PRESSED;
            } else {
                return states;
            }
        }
    }

    public static class Row {
        public int defaultHeight;
        public int defaultHorizontalGap;
        public int defaultWidth;
        ArrayList<Key> mKeys = new ArrayList();
        public int mode;
        private Keyboard parent;
        public int rowEdgeFlags;
        public int verticalGap;

        public Row(Keyboard parent) {
            this.parent = parent;
        }

        public Row(Resources res, Keyboard parent, XmlResourceParser parser) {
            this.parent = parent;
            TypedArray a = res.obtainAttributes(Xml.asAttributeSet(parser), R.styleable.Keyboard);
            this.defaultWidth = Keyboard.getDimensionOrFraction(a, 0, parent.mDisplayWidth, parent.mDefaultWidth);
            this.defaultHeight = Keyboard.getDimensionOrFraction(a, 1, parent.mDisplayHeight, parent.mDefaultHeight);
            this.defaultHorizontalGap = Keyboard.getDimensionOrFraction(a, 2, parent.mDisplayWidth, parent.mDefaultHorizontalGap);
            this.verticalGap = Keyboard.getDimensionOrFraction(a, 3, parent.mDisplayHeight, parent.mDefaultVerticalGap);
            a.recycle();
            a = res.obtainAttributes(Xml.asAttributeSet(parser), R.styleable.Keyboard_Row);
            this.rowEdgeFlags = a.getInt(0, 0);
            this.mode = a.getResourceId(1, 0);
        }
    }

    public Keyboard(Context context, int xmlLayoutResId) {
        this(context, xmlLayoutResId, 0);
    }

    public Keyboard(Context context, int xmlLayoutResId, int modeId, int width, int height) {
        this.mShiftKeys = new Key[]{null, null};
        this.mShiftKeyIndices = new int[]{-1, -1};
        this.rows = new ArrayList();
        this.mDisplayWidth = width;
        this.mDisplayHeight = height;
        this.mDefaultHorizontalGap = 0;
        this.mDefaultWidth = this.mDisplayWidth / 10;
        this.mDefaultVerticalGap = 0;
        this.mDefaultHeight = this.mDefaultWidth;
        this.mKeys = new ArrayList();
        this.mModifierKeys = new ArrayList();
        this.mKeyboardMode = modeId;
        loadKeyboard(context, context.getResources().getXml(xmlLayoutResId));
    }

    public Keyboard(Context context, int xmlLayoutResId, int modeId) {
        this.mShiftKeys = new Key[]{null, null};
        this.mShiftKeyIndices = new int[]{-1, -1};
        this.rows = new ArrayList();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        this.mDisplayWidth = dm.widthPixels;
        this.mDisplayHeight = dm.heightPixels;
        this.mDefaultHorizontalGap = 0;
        this.mDefaultWidth = this.mDisplayWidth / 10;
        this.mDefaultVerticalGap = 0;
        this.mDefaultHeight = this.mDefaultWidth;
        this.mKeys = new ArrayList();
        this.mModifierKeys = new ArrayList();
        this.mKeyboardMode = modeId;
        loadKeyboard(context, context.getResources().getXml(xmlLayoutResId));
    }

    public Keyboard(Context context, int layoutTemplateResId, CharSequence characters, int columns, int horizontalPadding) {
        this(context, layoutTemplateResId);
        int x = 0;
        int y = 0;
        int column = 0;
        this.mTotalWidth = 0;
        Row row = new Row(this);
        row.defaultHeight = this.mDefaultHeight;
        row.defaultWidth = this.mDefaultWidth;
        row.defaultHorizontalGap = this.mDefaultHorizontalGap;
        row.verticalGap = this.mDefaultVerticalGap;
        row.rowEdgeFlags = 12;
        int i = columns;
        int maxColumns = i == -1 ? Integer.MAX_VALUE : i;
        for (int i2 = 0; i2 < characters.length(); i2++) {
            char c = characters.charAt(i2);
            if (column >= maxColumns || (this.mDefaultWidth + x) + horizontalPadding > this.mDisplayWidth) {
                x = 0;
                y += this.mDefaultVerticalGap + this.mDefaultHeight;
                column = 0;
            }
            Key key = new Key(row);
            key.x = x;
            key.y = y;
            key.label = String.valueOf(c);
            key.codes = new int[]{c};
            column++;
            x += key.width + key.gap;
            this.mKeys.add(key);
            row.mKeys.add(key);
            if (x > this.mTotalWidth) {
                this.mTotalWidth = x;
            }
        }
        CharSequence charSequence = characters;
        this.mTotalHeight = this.mDefaultHeight + y;
        this.rows.add(row);
    }

    /* Access modifiers changed, original: final */
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public final void resize(int newWidth, int newHeight) {
        int numRows = this.rows.size();
        for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
            int keyIndex;
            Row row = (Row) this.rows.get(rowIndex);
            int numKeys = row.mKeys.size();
            int totalGap = 0;
            int totalWidth = 0;
            for (keyIndex = 0; keyIndex < numKeys; keyIndex++) {
                Key key = (Key) row.mKeys.get(keyIndex);
                if (keyIndex > 0) {
                    totalGap += key.gap;
                }
                totalWidth += key.width;
            }
            if (totalGap + totalWidth > newWidth) {
                keyIndex = 0;
                float scaleFactor = ((float) (newWidth - totalGap)) / ((float) totalWidth);
                for (int keyIndex2 = 0; keyIndex2 < numKeys; keyIndex2++) {
                    Key key2 = (Key) row.mKeys.get(keyIndex2);
                    key2.width = (int) (((float) key2.width) * scaleFactor);
                    key2.x = keyIndex;
                    keyIndex += key2.width + key2.gap;
                }
            }
        }
        this.mTotalWidth = newWidth;
    }

    public List<Key> getKeys() {
        return this.mKeys;
    }

    public List<Key> getModifierKeys() {
        return this.mModifierKeys;
    }

    /* Access modifiers changed, original: protected */
    public int getHorizontalGap() {
        return this.mDefaultHorizontalGap;
    }

    /* Access modifiers changed, original: protected */
    public void setHorizontalGap(int gap) {
        this.mDefaultHorizontalGap = gap;
    }

    /* Access modifiers changed, original: protected */
    public int getVerticalGap() {
        return this.mDefaultVerticalGap;
    }

    /* Access modifiers changed, original: protected */
    public void setVerticalGap(int gap) {
        this.mDefaultVerticalGap = gap;
    }

    /* Access modifiers changed, original: protected */
    public int getKeyHeight() {
        return this.mDefaultHeight;
    }

    /* Access modifiers changed, original: protected */
    public void setKeyHeight(int height) {
        this.mDefaultHeight = height;
    }

    /* Access modifiers changed, original: protected */
    public int getKeyWidth() {
        return this.mDefaultWidth;
    }

    /* Access modifiers changed, original: protected */
    public void setKeyWidth(int width) {
        this.mDefaultWidth = width;
    }

    public int getHeight() {
        return this.mTotalHeight;
    }

    public int getMinWidth() {
        return this.mTotalWidth;
    }

    public boolean setShifted(boolean shiftState) {
        for (Key shiftKey : this.mShiftKeys) {
            if (shiftKey != null) {
                shiftKey.on = shiftState;
            }
        }
        if (this.mShifted == shiftState) {
            return false;
        }
        this.mShifted = shiftState;
        return true;
    }

    public boolean isShifted() {
        return this.mShifted;
    }

    public int[] getShiftKeyIndices() {
        return this.mShiftKeyIndices;
    }

    public int getShiftKeyIndex() {
        return this.mShiftKeyIndices[0];
    }

    private void computeNearestNeighbors() {
        this.mCellWidth = ((getMinWidth() + 10) - 1) / 10;
        this.mCellHeight = ((getHeight() + 5) - 1) / 5;
        this.mGridNeighbors = new int[50][];
        int[] indices = new int[this.mKeys.size()];
        int gridWidth = this.mCellWidth * 10;
        int gridHeight = this.mCellHeight * 5;
        int x = 0;
        while (x < gridWidth) {
            int y = 0;
            while (y < gridHeight) {
                int count;
                int count2 = 0;
                for (int i = 0; i < this.mKeys.size(); i++) {
                    Key key = (Key) this.mKeys.get(i);
                    if (key.squaredDistanceFrom(x, y) < this.mProximityThreshold || key.squaredDistanceFrom((this.mCellWidth + x) - 1, y) < this.mProximityThreshold || key.squaredDistanceFrom((this.mCellWidth + x) - 1, (this.mCellHeight + y) - 1) < this.mProximityThreshold || key.squaredDistanceFrom(x, (this.mCellHeight + y) - 1) < this.mProximityThreshold) {
                        count = count2 + 1;
                        indices[count2] = i;
                        count2 = count;
                    }
                }
                int[] cell = new int[count2];
                System.arraycopy(indices, 0, cell, 0, count2);
                int[][] iArr = this.mGridNeighbors;
                count = this.mCellHeight;
                iArr[((y / count) * 10) + (x / this.mCellWidth)] = cell;
                y += count;
            }
            x += this.mCellWidth;
        }
    }

    public int[] getNearestKeys(int x, int y) {
        if (this.mGridNeighbors == null) {
            computeNearestNeighbors();
        }
        if (x >= 0 && x < getMinWidth() && y >= 0 && y < getHeight()) {
            int index = ((y / this.mCellHeight) * 10) + (x / this.mCellWidth);
            if (index < 50) {
                return this.mGridNeighbors[index];
            }
        }
        return new int[0];
    }

    /* Access modifiers changed, original: protected */
    public Row createRowFromXml(Resources res, XmlResourceParser parser) {
        return new Row(res, this, parser);
    }

    /* Access modifiers changed, original: protected */
    public Key createKeyFromXml(Resources res, Row parent, int x, int y, XmlResourceParser parser) {
        return new Key(res, parent, x, y, parser);
    }

    private void loadKeyboard(Context context, XmlResourceParser parser) {
        boolean event;
        Exception e;
        StringBuilder stringBuilder;
        XmlResourceParser xmlResourceParser = parser;
        String str = "Keyboard";
        boolean leftMostKey = false;
        Resources res = context.getResources();
        boolean inRow = false;
        int row = 0;
        int x = 0;
        Key key = null;
        Row currentRow = null;
        boolean skipRow = false;
        boolean inKey = false;
        int y = 0;
        while (true) {
            boolean leftMostKey2;
            try {
                boolean next = parser.next();
                event = next;
                int i;
                if (next) {
                    break;
                } else if (event) {
                    String tag = parser.getName();
                    boolean z = false;
                    if (TAG_ROW.equals(tag)) {
                        x = 0;
                        try {
                            currentRow = createRowFromXml(res, xmlResourceParser);
                            this.rows.add(currentRow);
                            if (!(currentRow.mode == 0 || currentRow.mode == this.mKeyboardMode)) {
                                z = true;
                            }
                            boolean skipRow2 = z;
                            if (skipRow2) {
                                try {
                                    skipToEndOfRow(xmlResourceParser);
                                    inRow = false;
                                    skipRow = skipRow2;
                                    leftMostKey2 = leftMostKey;
                                    leftMostKey = event;
                                } catch (Exception e2) {
                                    e = e2;
                                    inRow = true;
                                    skipRow = skipRow2;
                                    leftMostKey2 = leftMostKey;
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("Parse error:");
                                    stringBuilder.append(e);
                                    Log.e(str, stringBuilder.toString());
                                    e.printStackTrace();
                                    this.mTotalHeight = y - this.mDefaultVerticalGap;
                                }
                            }
                            inRow = true;
                            skipRow = skipRow2;
                            leftMostKey2 = leftMostKey;
                            leftMostKey = event;
                        } catch (Exception e3) {
                            e = e3;
                            inRow = true;
                            leftMostKey2 = leftMostKey;
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Parse error:");
                            stringBuilder.append(e);
                            Log.e(str, stringBuilder.toString());
                            e.printStackTrace();
                            this.mTotalHeight = y - this.mDefaultVerticalGap;
                        }
                    } else if (TAG_KEY.equals(tag)) {
                        Key key2;
                        int i2 = 1;
                        leftMostKey2 = leftMostKey;
                        leftMostKey = event;
                        try {
                            key2 = createKeyFromXml(res, currentRow, x, y, parser);
                        } catch (Exception e4) {
                            e = e4;
                            inKey = true;
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Parse error:");
                            stringBuilder.append(e);
                            Log.e(str, stringBuilder.toString());
                            e.printStackTrace();
                            this.mTotalHeight = y - this.mDefaultVerticalGap;
                        }
                        try {
                            this.mKeys.add(key2);
                            if (key2.codes[0] == -1) {
                                for (i = 0; i < this.mShiftKeys.length; i++) {
                                    if (this.mShiftKeys[i] == null) {
                                        this.mShiftKeys[i] = key2;
                                        this.mShiftKeyIndices[i] = this.mKeys.size() - 1;
                                        break;
                                    }
                                }
                                this.mModifierKeys.add(key2);
                            } else if (key2.codes[0] == -6) {
                                this.mModifierKeys.add(key2);
                            }
                            currentRow.mKeys.add(key2);
                            key = key2;
                            inKey = true;
                        } catch (Exception e5) {
                            e = e5;
                            key = key2;
                            key2 = 1;
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Parse error:");
                            stringBuilder.append(e);
                            Log.e(str, stringBuilder.toString());
                            e.printStackTrace();
                            this.mTotalHeight = y - this.mDefaultVerticalGap;
                        }
                    } else {
                        leftMostKey2 = leftMostKey;
                        leftMostKey = event;
                        try {
                            if (str.equals(tag)) {
                                parseKeyboardAttributes(res, xmlResourceParser);
                            }
                        } catch (Exception e6) {
                            e = e6;
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Parse error:");
                            stringBuilder.append(e);
                            Log.e(str, stringBuilder.toString());
                            e.printStackTrace();
                            this.mTotalHeight = y - this.mDefaultVerticalGap;
                        }
                    }
                    leftMostKey = leftMostKey2;
                } else {
                    leftMostKey2 = leftMostKey;
                    if (event) {
                        if (inKey) {
                            inKey = false;
                            i = x + (key.gap + key.width);
                            try {
                                if (i > this.mTotalWidth) {
                                    this.mTotalWidth = i;
                                }
                                x = i;
                                leftMostKey = leftMostKey2;
                            } catch (Exception e7) {
                                e = e7;
                                x = i;
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("Parse error:");
                                stringBuilder.append(e);
                                Log.e(str, stringBuilder.toString());
                                e.printStackTrace();
                                this.mTotalHeight = y - this.mDefaultVerticalGap;
                            }
                        } else if (inRow) {
                            inRow = false;
                            y += currentRow.verticalGap;
                            y += currentRow.defaultHeight;
                            row++;
                            leftMostKey = leftMostKey2;
                        }
                    }
                    leftMostKey = leftMostKey2;
                }
            } catch (Exception e8) {
                e = e8;
                leftMostKey2 = leftMostKey;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Parse error:");
                stringBuilder.append(e);
                Log.e(str, stringBuilder.toString());
                e.printStackTrace();
                this.mTotalHeight = y - this.mDefaultVerticalGap;
            }
        }
        leftMostKey = event;
        this.mTotalHeight = y - this.mDefaultVerticalGap;
    }

    private void skipToEndOfRow(XmlResourceParser parser) throws XmlPullParserException, IOException {
        while (true) {
            int next = parser.next();
            int event = next;
            if (next == 1) {
                return;
            }
            if (event == 3 && parser.getName().equals(TAG_ROW)) {
                return;
            }
        }
    }

    private void parseKeyboardAttributes(Resources res, XmlResourceParser parser) {
        TypedArray a = res.obtainAttributes(Xml.asAttributeSet(parser), R.styleable.Keyboard);
        int i = this.mDisplayWidth;
        this.mDefaultWidth = getDimensionOrFraction(a, 0, i, i / 10);
        this.mDefaultHeight = getDimensionOrFraction(a, 1, this.mDisplayHeight, 50);
        this.mDefaultHorizontalGap = getDimensionOrFraction(a, 2, this.mDisplayWidth, 0);
        this.mDefaultVerticalGap = getDimensionOrFraction(a, 3, this.mDisplayHeight, 0);
        this.mProximityThreshold = (int) (((float) this.mDefaultWidth) * SEARCH_DISTANCE);
        i = this.mProximityThreshold;
        this.mProximityThreshold = i * i;
        a.recycle();
    }

    static int getDimensionOrFraction(TypedArray a, int index, int base, int defValue) {
        TypedValue value = a.peekValue(index);
        if (value == null) {
            return defValue;
        }
        if (value.type == 5) {
            return a.getDimensionPixelOffset(index, defValue);
        }
        if (value.type == 6) {
            return Math.round(a.getFraction(index, base, base, (float) defValue));
        }
        return defValue;
    }
}
