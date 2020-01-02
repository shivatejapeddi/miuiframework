package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.ResourceId;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Pools.SynchronizedPool;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewDebug.IntToString;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewHierarchyEncoder;
import android.view.accessibility.AccessibilityEvent;
import android.view.inspector.InspectionCompanion.UninitializedPropertyMapException;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import android.widget.RemoteViews.RemoteView;
import com.android.internal.R;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

@RemoteView
public class RelativeLayout extends ViewGroup {
    public static final int ABOVE = 2;
    public static final int ALIGN_BASELINE = 4;
    public static final int ALIGN_BOTTOM = 8;
    public static final int ALIGN_END = 19;
    public static final int ALIGN_LEFT = 5;
    public static final int ALIGN_PARENT_BOTTOM = 12;
    public static final int ALIGN_PARENT_END = 21;
    public static final int ALIGN_PARENT_LEFT = 9;
    public static final int ALIGN_PARENT_RIGHT = 11;
    public static final int ALIGN_PARENT_START = 20;
    public static final int ALIGN_PARENT_TOP = 10;
    public static final int ALIGN_RIGHT = 7;
    public static final int ALIGN_START = 18;
    public static final int ALIGN_TOP = 6;
    public static final int BELOW = 3;
    public static final int CENTER_HORIZONTAL = 14;
    public static final int CENTER_IN_PARENT = 13;
    public static final int CENTER_VERTICAL = 15;
    private static final int DEFAULT_WIDTH = 65536;
    public static final int END_OF = 17;
    public static final int LEFT_OF = 0;
    public static final int RIGHT_OF = 1;
    private static final int[] RULES_HORIZONTAL = new int[]{0, 1, 5, 7, 16, 17, 18, 19};
    private static final int[] RULES_VERTICAL = new int[]{2, 3, 4, 6, 8};
    public static final int START_OF = 16;
    public static final int TRUE = -1;
    private static final int VALUE_NOT_SET = Integer.MIN_VALUE;
    private static final int VERB_COUNT = 22;
    private boolean mAllowBrokenMeasureSpecs;
    private View mBaselineView;
    private final Rect mContentBounds;
    private boolean mDirtyHierarchy;
    private final DependencyGraph mGraph;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private int mGravity;
    private int mIgnoreGravity;
    private boolean mMeasureVerticalWithPaddingMargin;
    private final Rect mSelfBounds;
    private View[] mSortedHorizontalChildren;
    private View[] mSortedVerticalChildren;
    private SortedSet<View> mTopToBottomLeftToRightSet;

    private static class DependencyGraph {
        private SparseArray<Node> mKeyNodes;
        private ArrayList<Node> mNodes;
        private ArrayDeque<Node> mRoots;

        static class Node {
            private static final int POOL_LIMIT = 100;
            private static final SynchronizedPool<Node> sPool = new SynchronizedPool(100);
            final SparseArray<Node> dependencies = new SparseArray();
            final ArrayMap<Node, DependencyGraph> dependents = new ArrayMap();
            View view;

            Node() {
            }

            static Node acquire(View view) {
                Node node = (Node) sPool.acquire();
                if (node == null) {
                    node = new Node();
                }
                node.view = view;
                return node;
            }

            /* Access modifiers changed, original: 0000 */
            public void release() {
                this.view = null;
                this.dependents.clear();
                this.dependencies.clear();
                sPool.release(this);
            }
        }

        private DependencyGraph() {
            this.mNodes = new ArrayList();
            this.mKeyNodes = new SparseArray();
            this.mRoots = new ArrayDeque();
        }

        /* Access modifiers changed, original: 0000 */
        public void clear() {
            ArrayList<Node> nodes = this.mNodes;
            int count = nodes.size();
            for (int i = 0; i < count; i++) {
                ((Node) nodes.get(i)).release();
            }
            nodes.clear();
            this.mKeyNodes.clear();
            this.mRoots.clear();
        }

        /* Access modifiers changed, original: 0000 */
        public void add(View view) {
            int id = view.getId();
            Node node = Node.acquire(view);
            if (id != -1) {
                this.mKeyNodes.put(id, node);
            }
            this.mNodes.add(node);
        }

        /* Access modifiers changed, original: varargs */
        public void getSortedViews(View[] sorted, int... rules) {
            ArrayDeque<Node> roots = findRoots(rules);
            int index = 0;
            while (true) {
                Node node = (Node) roots.pollLast();
                Node node2 = node;
                if (node == null) {
                    break;
                }
                View view = node2.view;
                int key = view.getId();
                int index2 = index + 1;
                sorted[index] = view;
                ArrayMap<Node, DependencyGraph> dependents = node2.dependents;
                int count = dependents.size();
                for (int i = 0; i < count; i++) {
                    Node dependent = (Node) dependents.keyAt(i);
                    SparseArray<Node> dependencies = dependent.dependencies;
                    dependencies.remove(key);
                    if (dependencies.size() == 0) {
                        roots.add(dependent);
                    }
                }
                index = index2;
            }
            if (index < sorted.length) {
                throw new IllegalStateException("Circular dependencies cannot exist in RelativeLayout");
            }
        }

        private ArrayDeque<Node> findRoots(int[] rulesFilter) {
            int i;
            Node node;
            SparseArray<Node> keyNodes = this.mKeyNodes;
            ArrayList<Node> nodes = this.mNodes;
            int count = nodes.size();
            for (i = 0; i < count; i++) {
                node = (Node) nodes.get(i);
                node.dependents.clear();
                node.dependencies.clear();
            }
            for (i = 0; i < count; i++) {
                node = (Node) nodes.get(i);
                int[] rules = ((LayoutParams) node.view.getLayoutParams()).mRules;
                for (int rule : rulesFilter) {
                    int rule2 = rules[rule2];
                    if (rule2 > 0 || ResourceId.isValid(rule2)) {
                        Node dependency = (Node) keyNodes.get(rule2);
                        if (!(dependency == null || dependency == node)) {
                            dependency.dependents.put(node, this);
                            node.dependencies.put(rule2, dependency);
                        }
                    }
                }
            }
            ArrayDeque<Node> roots = this.mRoots;
            roots.clear();
            for (int i2 = 0; i2 < count; i2++) {
                Node node2 = (Node) nodes.get(i2);
                if (node2.dependencies.size() == 0) {
                    roots.addLast(node2);
                }
            }
            return roots;
        }
    }

    public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<RelativeLayout> {
        private int mGravityId;
        private int mIgnoreGravityId;
        private boolean mPropertiesMapped = false;

        public void mapProperties(PropertyMapper propertyMapper) {
            this.mGravityId = propertyMapper.mapGravity("gravity", 16842927);
            this.mIgnoreGravityId = propertyMapper.mapInt("ignoreGravity", 16843263);
            this.mPropertiesMapped = true;
        }

        public void readProperties(RelativeLayout node, PropertyReader propertyReader) {
            if (this.mPropertiesMapped) {
                propertyReader.readGravity(this.mGravityId, node.getGravity());
                propertyReader.readInt(this.mIgnoreGravityId, node.getIgnoreGravity());
                return;
            }
            throw new UninitializedPropertyMapException();
        }
    }

    public static class LayoutParams extends MarginLayoutParams {
        @ExportedProperty(category = "layout")
        public boolean alignWithParent;
        @UnsupportedAppUsage
        private int mBottom;
        private int[] mInitialRules;
        private boolean mIsRtlCompatibilityMode;
        @UnsupportedAppUsage
        private int mLeft;
        private boolean mNeedsLayoutResolution;
        @UnsupportedAppUsage
        private int mRight;
        @ExportedProperty(category = "layout", indexMapping = {@IntToString(from = 2, to = "above"), @IntToString(from = 4, to = "alignBaseline"), @IntToString(from = 8, to = "alignBottom"), @IntToString(from = 5, to = "alignLeft"), @IntToString(from = 12, to = "alignParentBottom"), @IntToString(from = 9, to = "alignParentLeft"), @IntToString(from = 11, to = "alignParentRight"), @IntToString(from = 10, to = "alignParentTop"), @IntToString(from = 7, to = "alignRight"), @IntToString(from = 6, to = "alignTop"), @IntToString(from = 3, to = "below"), @IntToString(from = 14, to = "centerHorizontal"), @IntToString(from = 13, to = "center"), @IntToString(from = 15, to = "centerVertical"), @IntToString(from = 0, to = "leftOf"), @IntToString(from = 1, to = "rightOf"), @IntToString(from = 18, to = "alignStart"), @IntToString(from = 19, to = "alignEnd"), @IntToString(from = 20, to = "alignParentStart"), @IntToString(from = 21, to = "alignParentEnd"), @IntToString(from = 16, to = "startOf"), @IntToString(from = 17, to = "endOf")}, mapping = {@IntToString(from = -1, to = "true"), @IntToString(from = 0, to = "false/NO_ID")}, resolveId = true)
        private int[] mRules;
        private boolean mRulesChanged;
        @UnsupportedAppUsage
        private int mTop;

        public static final class InspectionCompanion implements android.view.inspector.InspectionCompanion<LayoutParams> {
            private int mAboveId;
            private int mAlignBaselineId;
            private int mAlignBottomId;
            private int mAlignEndId;
            private int mAlignLeftId;
            private int mAlignParentBottomId;
            private int mAlignParentEndId;
            private int mAlignParentLeftId;
            private int mAlignParentRightId;
            private int mAlignParentStartId;
            private int mAlignParentTopId;
            private int mAlignRightId;
            private int mAlignStartId;
            private int mAlignTopId;
            private int mAlignWithParentIfMissingId;
            private int mBelowId;
            private int mCenterHorizontalId;
            private int mCenterInParentId;
            private int mCenterVerticalId;
            private boolean mPropertiesMapped;
            private int mToEndOfId;
            private int mToLeftOfId;
            private int mToRightOfId;
            private int mToStartOfId;

            public void mapProperties(PropertyMapper propertyMapper) {
                this.mPropertiesMapped = true;
                this.mAboveId = propertyMapper.mapResourceId("layout_above", 16843140);
                this.mAlignBaselineId = propertyMapper.mapResourceId("layout_alignBaseline", 16843142);
                this.mAlignBottomId = propertyMapper.mapResourceId("layout_alignBottom", 16843146);
                this.mAlignEndId = propertyMapper.mapResourceId("layout_alignEnd", 16843706);
                this.mAlignLeftId = propertyMapper.mapResourceId("layout_alignLeft", 16843143);
                this.mAlignParentBottomId = propertyMapper.mapBoolean("layout_alignParentBottom", 16843150);
                this.mAlignParentEndId = propertyMapper.mapBoolean("layout_alignParentEnd", 16843708);
                this.mAlignParentLeftId = propertyMapper.mapBoolean("layout_alignParentLeft", 16843147);
                this.mAlignParentRightId = propertyMapper.mapBoolean("layout_alignParentRight", 16843149);
                this.mAlignParentStartId = propertyMapper.mapBoolean("layout_alignParentStart", 16843707);
                this.mAlignParentTopId = propertyMapper.mapBoolean("layout_alignParentTop", 16843148);
                this.mAlignRightId = propertyMapper.mapResourceId("layout_alignRight", 16843145);
                this.mAlignStartId = propertyMapper.mapResourceId("layout_alignStart", 16843705);
                this.mAlignTopId = propertyMapper.mapResourceId("layout_alignTop", 16843144);
                this.mAlignWithParentIfMissingId = propertyMapper.mapBoolean("layout_alignWithParentIfMissing", 16843154);
                this.mBelowId = propertyMapper.mapResourceId("layout_below", 16843141);
                this.mCenterHorizontalId = propertyMapper.mapBoolean("layout_centerHorizontal", 16843152);
                this.mCenterInParentId = propertyMapper.mapBoolean("layout_centerInParent", 16843151);
                this.mCenterVerticalId = propertyMapper.mapBoolean("layout_centerVertical", 16843153);
                this.mToEndOfId = propertyMapper.mapResourceId("layout_toEndOf", 16843704);
                this.mToLeftOfId = propertyMapper.mapResourceId("layout_toLeftOf", 16843138);
                this.mToRightOfId = propertyMapper.mapResourceId("layout_toRightOf", 16843139);
                this.mToStartOfId = propertyMapper.mapResourceId("layout_toStartOf", 16843703);
            }

            public void readProperties(LayoutParams node, PropertyReader propertyReader) {
                if (this.mPropertiesMapped) {
                    int[] rules = node.getRules();
                    propertyReader.readResourceId(this.mAboveId, rules[2]);
                    propertyReader.readResourceId(this.mAlignBaselineId, rules[4]);
                    propertyReader.readResourceId(this.mAlignBottomId, rules[8]);
                    propertyReader.readResourceId(this.mAlignEndId, rules[19]);
                    propertyReader.readResourceId(this.mAlignLeftId, rules[5]);
                    propertyReader.readBoolean(this.mAlignParentBottomId, rules[12] == -1);
                    propertyReader.readBoolean(this.mAlignParentEndId, rules[21] == -1);
                    propertyReader.readBoolean(this.mAlignParentLeftId, rules[9] == -1);
                    propertyReader.readBoolean(this.mAlignParentRightId, rules[11] == -1);
                    propertyReader.readBoolean(this.mAlignParentStartId, rules[20] == -1);
                    propertyReader.readBoolean(this.mAlignParentTopId, rules[10] == -1);
                    propertyReader.readResourceId(this.mAlignRightId, rules[7]);
                    propertyReader.readResourceId(this.mAlignStartId, rules[18]);
                    propertyReader.readResourceId(this.mAlignTopId, rules[6]);
                    propertyReader.readBoolean(this.mAlignWithParentIfMissingId, node.alignWithParent);
                    propertyReader.readResourceId(this.mBelowId, rules[3]);
                    propertyReader.readBoolean(this.mCenterHorizontalId, rules[14] == -1);
                    propertyReader.readBoolean(this.mCenterInParentId, rules[13] == -1);
                    propertyReader.readBoolean(this.mCenterVerticalId, rules[15] == -1);
                    propertyReader.readResourceId(this.mToEndOfId, rules[17]);
                    propertyReader.readResourceId(this.mToLeftOfId, rules[0]);
                    propertyReader.readResourceId(this.mToRightOfId, rules[1]);
                    propertyReader.readResourceId(this.mToStartOfId, rules[16]);
                    return;
                }
                throw new UninitializedPropertyMapException();
            }
        }

        static /* synthetic */ int access$112(LayoutParams x0, int x1) {
            int i = x0.mLeft + x1;
            x0.mLeft = i;
            return i;
        }

        static /* synthetic */ int access$120(LayoutParams x0, int x1) {
            int i = x0.mLeft - x1;
            x0.mLeft = i;
            return i;
        }

        static /* synthetic */ int access$212(LayoutParams x0, int x1) {
            int i = x0.mRight + x1;
            x0.mRight = i;
            return i;
        }

        static /* synthetic */ int access$220(LayoutParams x0, int x1) {
            int i = x0.mRight - x1;
            x0.mRight = i;
            return i;
        }

        static /* synthetic */ int access$312(LayoutParams x0, int x1) {
            int i = x0.mBottom + x1;
            x0.mBottom = i;
            return i;
        }

        static /* synthetic */ int access$412(LayoutParams x0, int x1) {
            int i = x0.mTop + x1;
            x0.mTop = i;
            return i;
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.mRules = new int[22];
            this.mInitialRules = new int[22];
            this.mRulesChanged = false;
            this.mIsRtlCompatibilityMode = false;
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.RelativeLayout_Layout);
            boolean z = c.getApplicationInfo().targetSdkVersion < 17 || !c.getApplicationInfo().hasRtlSupport();
            this.mIsRtlCompatibilityMode = z;
            int[] rules = this.mRules;
            int[] initialRules = this.mInitialRules;
            int N = a.getIndexCount();
            for (int i = 0; i < N; i++) {
                int attr = a.getIndex(i);
                int i2 = -1;
                switch (attr) {
                    case 0:
                        rules[0] = a.getResourceId(attr, 0);
                        break;
                    case 1:
                        rules[1] = a.getResourceId(attr, 0);
                        break;
                    case 2:
                        rules[2] = a.getResourceId(attr, 0);
                        break;
                    case 3:
                        rules[3] = a.getResourceId(attr, 0);
                        break;
                    case 4:
                        rules[4] = a.getResourceId(attr, 0);
                        break;
                    case 5:
                        rules[5] = a.getResourceId(attr, 0);
                        break;
                    case 6:
                        rules[6] = a.getResourceId(attr, 0);
                        break;
                    case 7:
                        rules[7] = a.getResourceId(attr, 0);
                        break;
                    case 8:
                        rules[8] = a.getResourceId(attr, 0);
                        break;
                    case 9:
                        if (!a.getBoolean(attr, false)) {
                            i2 = false;
                        }
                        rules[9] = i2;
                        break;
                    case 10:
                        if (!a.getBoolean(attr, false)) {
                            i2 = false;
                        }
                        rules[10] = i2;
                        break;
                    case 11:
                        if (!a.getBoolean(attr, false)) {
                            i2 = false;
                        }
                        rules[11] = i2;
                        break;
                    case 12:
                        if (!a.getBoolean(attr, false)) {
                            i2 = false;
                        }
                        rules[12] = i2;
                        break;
                    case 13:
                        if (!a.getBoolean(attr, false)) {
                            i2 = false;
                        }
                        rules[13] = i2;
                        break;
                    case 14:
                        if (!a.getBoolean(attr, false)) {
                            i2 = false;
                        }
                        rules[14] = i2;
                        break;
                    case 15:
                        if (!a.getBoolean(attr, false)) {
                            i2 = false;
                        }
                        rules[15] = i2;
                        break;
                    case 16:
                        this.alignWithParent = a.getBoolean(attr, false);
                        break;
                    case 17:
                        rules[16] = a.getResourceId(attr, 0);
                        break;
                    case 18:
                        rules[17] = a.getResourceId(attr, 0);
                        break;
                    case 19:
                        rules[18] = a.getResourceId(attr, 0);
                        break;
                    case 20:
                        rules[19] = a.getResourceId(attr, 0);
                        break;
                    case 21:
                        if (!a.getBoolean(attr, false)) {
                            i2 = false;
                        }
                        rules[20] = i2;
                        break;
                    case 22:
                        if (!a.getBoolean(attr, false)) {
                            i2 = false;
                        }
                        rules[21] = i2;
                        break;
                    default:
                        break;
                }
            }
            this.mRulesChanged = true;
            System.arraycopy(rules, 0, initialRules, 0, 22);
            a.recycle();
        }

        public LayoutParams(int w, int h) {
            super(w, h);
            this.mRules = new int[22];
            this.mInitialRules = new int[22];
            this.mRulesChanged = false;
            this.mIsRtlCompatibilityMode = false;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
            this.mRules = new int[22];
            this.mInitialRules = new int[22];
            this.mRulesChanged = false;
            this.mIsRtlCompatibilityMode = false;
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
            this.mRules = new int[22];
            this.mInitialRules = new int[22];
            this.mRulesChanged = false;
            this.mIsRtlCompatibilityMode = false;
        }

        public LayoutParams(LayoutParams source) {
            super((MarginLayoutParams) source);
            this.mRules = new int[22];
            this.mInitialRules = new int[22];
            this.mRulesChanged = false;
            this.mIsRtlCompatibilityMode = false;
            this.mIsRtlCompatibilityMode = source.mIsRtlCompatibilityMode;
            this.mRulesChanged = source.mRulesChanged;
            this.alignWithParent = source.alignWithParent;
            System.arraycopy(source.mRules, 0, this.mRules, 0, 22);
            System.arraycopy(source.mInitialRules, 0, this.mInitialRules, 0, 22);
        }

        public String debug(String output) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(output);
            stringBuilder.append("ViewGroup.LayoutParams={ width=");
            stringBuilder.append(android.view.ViewGroup.LayoutParams.sizeToString(this.width));
            stringBuilder.append(", height=");
            stringBuilder.append(android.view.ViewGroup.LayoutParams.sizeToString(this.height));
            stringBuilder.append(" }");
            return stringBuilder.toString();
        }

        public void addRule(int verb) {
            addRule(verb, -1);
        }

        public void addRule(int verb, int subject) {
            if (!this.mNeedsLayoutResolution && isRelativeRule(verb) && this.mInitialRules[verb] != 0 && subject == 0) {
                this.mNeedsLayoutResolution = true;
            }
            this.mRules[verb] = subject;
            this.mInitialRules[verb] = subject;
            this.mRulesChanged = true;
        }

        public void removeRule(int verb) {
            addRule(verb, 0);
        }

        public int getRule(int verb) {
            return this.mRules[verb];
        }

        private boolean hasRelativeRules() {
            int[] iArr = this.mInitialRules;
            return (iArr[16] == 0 && iArr[17] == 0 && iArr[18] == 0 && iArr[19] == 0 && iArr[20] == 0 && iArr[21] == 0) ? false : true;
        }

        private boolean isRelativeRule(int rule) {
            return rule == 16 || rule == 17 || rule == 18 || rule == 19 || rule == 20 || rule == 21;
        }

        private void resolveRules(int layoutDirection) {
            int i = 1;
            boolean isLayoutRtl = layoutDirection == 1;
            System.arraycopy(this.mInitialRules, 0, this.mRules, 0, 22);
            int i2 = 11;
            int[] iArr;
            int[] iArr2;
            if (this.mIsRtlCompatibilityMode) {
                iArr = this.mRules;
                if (iArr[18] != 0) {
                    if (iArr[5] == 0) {
                        iArr[5] = iArr[18];
                    }
                    this.mRules[18] = 0;
                }
                iArr = this.mRules;
                if (iArr[19] != 0) {
                    if (iArr[7] == 0) {
                        iArr[7] = iArr[19];
                    }
                    this.mRules[19] = 0;
                }
                iArr = this.mRules;
                if (iArr[16] != 0) {
                    if (iArr[0] == 0) {
                        iArr[0] = iArr[16];
                    }
                    this.mRules[16] = 0;
                }
                iArr = this.mRules;
                if (iArr[17] != 0) {
                    if (iArr[1] == 0) {
                        iArr[1] = iArr[17];
                    }
                    this.mRules[17] = 0;
                }
                iArr2 = this.mRules;
                if (iArr2[20] != 0) {
                    if (iArr2[9] == 0) {
                        iArr2[9] = iArr2[20];
                    }
                    this.mRules[20] = 0;
                }
                iArr2 = this.mRules;
                if (iArr2[21] != 0) {
                    if (iArr2[11] == 0) {
                        iArr2[11] = iArr2[21];
                    }
                    this.mRules[21] = 0;
                }
            } else {
                int[] iArr3;
                iArr = this.mRules;
                if (!(iArr[18] == 0 && iArr[19] == 0)) {
                    iArr = this.mRules;
                    if (!(iArr[5] == 0 && iArr[7] == 0)) {
                        iArr = this.mRules;
                        iArr[5] = 0;
                        iArr[7] = 0;
                    }
                }
                iArr = this.mRules;
                if (iArr[18] != 0) {
                    int i3 = isLayoutRtl ? 7 : 5;
                    iArr3 = this.mRules;
                    iArr[i3] = iArr3[18];
                    iArr3[18] = 0;
                }
                iArr = this.mRules;
                if (iArr[19] != 0) {
                    int i4 = isLayoutRtl ? 5 : 7;
                    iArr3 = this.mRules;
                    iArr[i4] = iArr3[19];
                    iArr3[19] = 0;
                }
                iArr = this.mRules;
                if (!(iArr[16] == 0 && iArr[17] == 0)) {
                    iArr = this.mRules;
                    if (!(iArr[0] == 0 && iArr[1] == 0)) {
                        iArr = this.mRules;
                        iArr[0] = 0;
                        iArr[1] = 0;
                    }
                }
                iArr = this.mRules;
                if (iArr[16] != 0) {
                    int i5 = isLayoutRtl ? 1 : 0;
                    int[] iArr4 = this.mRules;
                    iArr[i5] = iArr4[16];
                    iArr4[16] = 0;
                }
                iArr = this.mRules;
                if (iArr[17] != 0) {
                    if (isLayoutRtl) {
                        i = 0;
                    }
                    iArr3 = this.mRules;
                    iArr[i] = iArr3[17];
                    iArr3[17] = 0;
                }
                iArr2 = this.mRules;
                if (!(iArr2[20] == 0 && iArr2[21] == 0)) {
                    iArr2 = this.mRules;
                    if (!(iArr2[9] == 0 && iArr2[11] == 0)) {
                        iArr2 = this.mRules;
                        iArr2[9] = 0;
                        iArr2[11] = 0;
                    }
                }
                iArr2 = this.mRules;
                if (iArr2[20] != 0) {
                    int i6 = isLayoutRtl ? 11 : 9;
                    iArr3 = this.mRules;
                    iArr2[i6] = iArr3[20];
                    iArr3[20] = 0;
                }
                iArr2 = this.mRules;
                if (iArr2[21] != 0) {
                    if (isLayoutRtl) {
                        i2 = 9;
                    }
                    iArr = this.mRules;
                    iArr2[i2] = iArr[21];
                    iArr[21] = 0;
                }
            }
            this.mRulesChanged = false;
            this.mNeedsLayoutResolution = false;
        }

        public int[] getRules(int layoutDirection) {
            resolveLayoutDirection(layoutDirection);
            return this.mRules;
        }

        public int[] getRules() {
            return this.mRules;
        }

        public void resolveLayoutDirection(int layoutDirection) {
            if (shouldResolveLayoutDirection(layoutDirection)) {
                resolveRules(layoutDirection);
            }
            super.resolveLayoutDirection(layoutDirection);
        }

        private boolean shouldResolveLayoutDirection(int layoutDirection) {
            return (this.mNeedsLayoutResolution || hasRelativeRules()) && (this.mRulesChanged || layoutDirection != getLayoutDirection());
        }

        /* Access modifiers changed, original: protected */
        public void encodeProperties(ViewHierarchyEncoder encoder) {
            super.encodeProperties(encoder);
            encoder.addProperty("layout:alignWithParent", this.alignWithParent);
        }
    }

    private class TopToBottomLeftToRightComparator implements Comparator<View> {
        private TopToBottomLeftToRightComparator() {
        }

        public int compare(View first, View second) {
            int topDifference = first.getTop() - second.getTop();
            if (topDifference != 0) {
                return topDifference;
            }
            int leftDifference = first.getLeft() - second.getLeft();
            if (leftDifference != 0) {
                return leftDifference;
            }
            int heightDiference = first.getHeight() - second.getHeight();
            if (heightDiference != 0) {
                return heightDiference;
            }
            int widthDiference = first.getWidth() - second.getWidth();
            if (widthDiference != 0) {
                return widthDiference;
            }
            return 0;
        }
    }

    public RelativeLayout(Context context) {
        this(context, null);
    }

    public RelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mBaselineView = null;
        this.mGravity = 8388659;
        this.mContentBounds = new Rect();
        this.mSelfBounds = new Rect();
        this.mTopToBottomLeftToRightSet = null;
        this.mGraph = new DependencyGraph();
        this.mAllowBrokenMeasureSpecs = false;
        this.mMeasureVerticalWithPaddingMargin = false;
        initFromAttributes(context, attrs, defStyleAttr, defStyleRes);
        queryCompatibilityModes(context);
    }

    private void initFromAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RelativeLayout, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.RelativeLayout, attrs, a, defStyleAttr, defStyleRes);
        this.mIgnoreGravity = a.getResourceId(1, -1);
        this.mGravity = a.getInt(0, this.mGravity);
        a.recycle();
    }

    private void queryCompatibilityModes(Context context) {
        int version = context.getApplicationInfo().targetSdkVersion;
        boolean z = true;
        this.mAllowBrokenMeasureSpecs = version <= 17;
        if (version < 18) {
            z = false;
        }
        this.mMeasureVerticalWithPaddingMargin = z;
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @RemotableViewMethod
    public void setIgnoreGravity(int viewId) {
        this.mIgnoreGravity = viewId;
    }

    public int getIgnoreGravity() {
        return this.mIgnoreGravity;
    }

    public int getGravity() {
        return this.mGravity;
    }

    @RemotableViewMethod
    public void setGravity(int gravity) {
        if (this.mGravity != gravity) {
            if ((Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK & gravity) == 0) {
                gravity |= Gravity.START;
            }
            if ((gravity & 112) == 0) {
                gravity |= 48;
            }
            this.mGravity = gravity;
            requestLayout();
        }
    }

    @RemotableViewMethod
    public void setHorizontalGravity(int horizontalGravity) {
        int gravity = horizontalGravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        int i = this.mGravity;
        if ((Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK & i) != gravity) {
            this.mGravity = (-8388616 & i) | gravity;
            requestLayout();
        }
    }

    @RemotableViewMethod
    public void setVerticalGravity(int verticalGravity) {
        int gravity = verticalGravity & 112;
        int i = this.mGravity;
        if ((i & 112) != gravity) {
            this.mGravity = (i & -113) | gravity;
            requestLayout();
        }
    }

    public int getBaseline() {
        View view = this.mBaselineView;
        return view != null ? view.getBaseline() : super.getBaseline();
    }

    public void requestLayout() {
        super.requestLayout();
        this.mDirtyHierarchy = true;
    }

    private void sortChildren() {
        int count = getChildCount();
        View[] viewArr = this.mSortedVerticalChildren;
        if (viewArr == null || viewArr.length != count) {
            this.mSortedVerticalChildren = new View[count];
        }
        viewArr = this.mSortedHorizontalChildren;
        if (viewArr == null || viewArr.length != count) {
            this.mSortedHorizontalChildren = new View[count];
        }
        DependencyGraph graph = this.mGraph;
        graph.clear();
        for (int i = 0; i < count; i++) {
            graph.add(getChildAt(i));
        }
        graph.getSortedViews(this.mSortedVerticalChildren, RULES_VERTICAL);
        graph.getSortedViews(this.mSortedHorizontalChildren, RULES_HORIZONTAL);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int count;
        LayoutParams views;
        int count2;
        View child;
        View[] views2;
        View ignore;
        int top;
        int i;
        if (this.mDirtyHierarchy) {
            this.mDirtyHierarchy = false;
            sortChildren();
        }
        int myWidth = -1;
        int myHeight = -1;
        int width2 = 0;
        int height = 0;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode != 0) {
            myWidth = widthSize;
        }
        if (heightMode != 0) {
            myHeight = heightSize;
        }
        if (widthMode == 1073741824) {
            width2 = myWidth;
        }
        if (heightMode == 1073741824) {
            height = myHeight;
        }
        View ignore2 = null;
        int gravity = this.mGravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        boolean horizontalGravity = (gravity == Gravity.START || gravity == 0) ? false : true;
        gravity = this.mGravity & 112;
        boolean verticalGravity = (gravity == 48 || gravity == 0) ? false : true;
        boolean offsetHorizontalAxis = false;
        boolean offsetVerticalAxis = false;
        if (horizontalGravity || verticalGravity) {
            int i2 = this.mIgnoreGravity;
            if (i2 != -1) {
                ignore2 = findViewById(i2);
            }
        }
        boolean isWrapContentWidth = widthMode != 1073741824;
        boolean isWrapContentHeight = heightMode != 1073741824;
        int layoutDirection = getLayoutDirection();
        if (isLayoutRtl()) {
            width = width2;
            if (myWidth == -1) {
                myWidth = 65536;
            }
        } else {
            width = width2;
        }
        View[] views3 = this.mSortedHorizontalChildren;
        int height2 = height;
        height = views3.length;
        widthMode = 0;
        while (true) {
            int heightMode2 = heightMode;
            if (widthMode >= height) {
                break;
            }
            View child2 = views3[widthMode];
            View[] views4 = views3;
            count = height;
            if (child2.getVisibility() != 8) {
                views = (LayoutParams) child2.getLayoutParams();
                applyHorizontalSizeRules(views, myWidth, views.getRules(layoutDirection));
                measureChildHorizontal(child2, views, myWidth, myHeight);
                if (positionChildHorizontal(child2, views, myWidth, isWrapContentWidth)) {
                    offsetHorizontalAxis = true;
                }
            }
            widthMode++;
            views3 = views4;
            height = count;
            heightMode = heightMode2;
        }
        count = height;
        views3 = this.mSortedVerticalChildren;
        height = views3.length;
        widthMode = getContext().getApplicationInfo().targetSdkVersion;
        heightMode = 0;
        int right = Integer.MIN_VALUE;
        int bottom = Integer.MIN_VALUE;
        int layoutDirection2 = layoutDirection;
        gravity = Integer.MAX_VALUE;
        layoutDirection = Integer.MAX_VALUE;
        heightSize = height2;
        widthSize = width;
        while (heightMode < height) {
            int myHeight2;
            count2 = height;
            child = views3[heightMode];
            views2 = views3;
            height2 = heightMode;
            if (child.getVisibility() != 8) {
                views = (LayoutParams) child.getLayoutParams();
                applyVerticalSizeRules(views, myHeight, child.getBaseline());
                measureChild(child, views, myWidth, myHeight);
                if (positionChildVertical(child, views, myHeight, isWrapContentHeight)) {
                    offsetVerticalAxis = true;
                }
                if (!isWrapContentWidth) {
                    myHeight2 = myHeight;
                } else if (!isLayoutRtl()) {
                    myHeight2 = myHeight;
                    if (widthMode < 19) {
                        widthSize = Math.max(widthSize, views.mRight);
                    } else {
                        widthSize = Math.max(widthSize, views.mRight + views.rightMargin);
                    }
                } else if (widthMode < 19) {
                    widthSize = Math.max(widthSize, myWidth - views.mLeft);
                    myHeight2 = myHeight;
                } else {
                    myHeight2 = myHeight;
                    widthSize = Math.max(widthSize, (myWidth - views.mLeft) + views.leftMargin);
                }
                if (isWrapContentHeight) {
                    if (widthMode < 19) {
                        heightSize = Math.max(heightSize, views.mBottom);
                    } else {
                        heightSize = Math.max(heightSize, views.mBottom + views.bottomMargin);
                    }
                }
                if (child != ignore2 || verticalGravity) {
                    gravity = Math.min(gravity, views.mLeft - views.leftMargin);
                    layoutDirection = Math.min(layoutDirection, views.mTop - views.topMargin);
                }
                if (child != ignore2 || horizontalGravity) {
                    myHeight = Math.max(right, views.mRight + views.rightMargin);
                    width = layoutDirection;
                    bottom = Math.max(bottom, views.mBottom + views.bottomMargin);
                    right = myHeight;
                    layoutDirection = width;
                }
            } else {
                myHeight2 = myHeight;
                heightMode = right;
                myHeight = bottom;
            }
            heightMode = height2 + 1;
            height = count2;
            views3 = views2;
            myHeight = myHeight2;
        }
        views2 = views3;
        count2 = height;
        height2 = heightMode;
        heightMode = right;
        myHeight = bottom;
        LayoutParams baselineParams = null;
        child = null;
        width2 = 0;
        while (true) {
            bottom = myWidth;
            myWidth = count2;
            if (width2 >= myWidth) {
                break;
            }
            View child3 = views2[width2];
            ignore = ignore2;
            top = layoutDirection;
            if (child3.getVisibility() != 8) {
                LayoutParams top2 = (LayoutParams) child3.getLayoutParams();
                if (child == null || baselineParams == null || compareLayoutPosition(top2, baselineParams) < 0) {
                    child = child3;
                    baselineParams = top2;
                }
            }
            width2++;
            ignore2 = ignore;
            layoutDirection = top;
            count2 = myWidth;
            myWidth = bottom;
        }
        top = layoutDirection;
        ignore = ignore2;
        this.mBaselineView = child;
        LayoutParams baselineParams2;
        if (isWrapContentWidth) {
            widthSize += this.mPaddingRight;
            if (this.mLayoutParams != null && this.mLayoutParams.width >= 0) {
                widthSize = Math.max(widthSize, this.mLayoutParams.width);
            }
            widthSize = View.resolveSize(Math.max(widthSize, getSuggestedMinimumWidth()), widthMeasureSpec);
            if (offsetHorizontalAxis) {
                width2 = 0;
                while (width2 < myWidth) {
                    View child4 = views2[width2];
                    View baselineView = child;
                    baselineParams2 = baselineParams;
                    if (child4.getVisibility() != 8) {
                        LayoutParams baselineView2 = (LayoutParams) child4.getLayoutParams();
                        widthMode = layoutDirection2;
                        layoutDirection2 = baselineView2.getRules(widthMode);
                        if (layoutDirection2[13] != null || layoutDirection2[14] != null) {
                            centerHorizontal(child4, baselineView2, widthSize);
                        } else if (layoutDirection2[11] != null) {
                            width = child4.getMeasuredWidth();
                            baselineView2.mLeft = (widthSize - this.mPaddingRight) - width;
                            baselineView2.mRight = baselineView2.mLeft + width;
                        }
                    } else {
                        widthMode = layoutDirection2;
                    }
                    width2++;
                    i = widthMeasureSpec;
                    layoutDirection2 = widthMode;
                    child = baselineView;
                    baselineParams = baselineParams2;
                }
                baselineParams2 = baselineParams;
                widthMode = layoutDirection2;
            } else {
                baselineParams2 = baselineParams;
                widthMode = layoutDirection2;
            }
        } else {
            baselineParams2 = baselineParams;
            widthMode = layoutDirection2;
        }
        if (isWrapContentHeight) {
            heightSize += this.mPaddingBottom;
            if (this.mLayoutParams != null && this.mLayoutParams.height >= 0) {
                heightSize = Math.max(heightSize, this.mLayoutParams.height);
            }
            heightSize = View.resolveSize(Math.max(heightSize, getSuggestedMinimumHeight()), heightMeasureSpec);
            if (offsetVerticalAxis) {
                layoutDirection = 0;
                while (layoutDirection < myWidth) {
                    boolean isWrapContentHeight2;
                    child = views2[layoutDirection];
                    if (child.getVisibility() != 8) {
                        views = (LayoutParams) child.getLayoutParams();
                        int[] rules = views.getRules(widthMode);
                        if (rules[13] != 0) {
                            isWrapContentHeight2 = isWrapContentHeight;
                        } else if (rules[15] != 0) {
                            isWrapContentHeight2 = isWrapContentHeight;
                        } else if (rules[12] != 0) {
                            height2 = child.getMeasuredHeight();
                            isWrapContentHeight2 = isWrapContentHeight;
                            views.mTop = (heightSize - this.mPaddingBottom) - height2;
                            views.mBottom = views.mTop + height2;
                        } else {
                            isWrapContentHeight2 = isWrapContentHeight;
                        }
                        centerVertical(child, views, heightSize);
                    } else {
                        isWrapContentHeight2 = isWrapContentHeight;
                    }
                    layoutDirection++;
                    width2 = heightMeasureSpec;
                    isWrapContentHeight = isWrapContentHeight2;
                }
            }
        }
        if (horizontalGravity || verticalGravity) {
            Rect selfBounds = this.mSelfBounds;
            selfBounds.set(this.mPaddingLeft, this.mPaddingTop, widthSize - this.mPaddingRight, heightSize - this.mPaddingBottom);
            Rect contentBounds = this.mContentBounds;
            Gravity.apply(this.mGravity, heightMode - gravity, myHeight - top, selfBounds, contentBounds, widthMode);
            height = contentBounds.left - gravity;
            int verticalOffset = contentBounds.top - top;
            if (height == 0 && verticalOffset == 0) {
                height2 = myHeight;
                myHeight = ignore;
            } else {
                Rect contentBounds2;
                i = 0;
                while (i < myWidth) {
                    View ignore3;
                    Rect selfBounds2 = selfBounds;
                    selfBounds = views2[i];
                    height2 = myHeight;
                    contentBounds2 = contentBounds;
                    if (selfBounds.getVisibility() != 8) {
                        ignore3 = ignore;
                        if (selfBounds != ignore3) {
                            views = (LayoutParams) selfBounds.getLayoutParams();
                            if (horizontalGravity) {
                                LayoutParams.access$112(views, height);
                                LayoutParams.access$212(views, height);
                            }
                            if (verticalGravity) {
                                LayoutParams.access$412(views, verticalOffset);
                                LayoutParams.access$312(views, verticalOffset);
                            }
                        }
                    } else {
                        ignore3 = ignore;
                    }
                    i++;
                    ignore = ignore3;
                    selfBounds = selfBounds2;
                    myHeight = height2;
                    contentBounds = contentBounds2;
                }
                height2 = myHeight;
                contentBounds2 = contentBounds;
            }
        } else {
            height2 = myHeight;
            myHeight = ignore;
        }
        if (isLayoutRtl()) {
            layoutDirection = bottom - widthSize;
            for (width2 = 0; width2 < myWidth; width2++) {
                child = views2[width2];
                if (child.getVisibility() != 8) {
                    LayoutParams params = (LayoutParams) child.getLayoutParams();
                    LayoutParams.access$120(params, layoutDirection);
                    LayoutParams.access$220(params, layoutDirection);
                }
            }
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    private int compareLayoutPosition(LayoutParams p1, LayoutParams p2) {
        int topDiff = p1.mTop - p2.mTop;
        if (topDiff != 0) {
            return topDiff;
        }
        return p1.mLeft - p2.mLeft;
    }

    private void measureChild(View child, LayoutParams params, int myWidth, int myHeight) {
        child.measure(getChildMeasureSpec(params.mLeft, params.mRight, params.width, params.leftMargin, params.rightMargin, this.mPaddingLeft, this.mPaddingRight, myWidth), getChildMeasureSpec(params.mTop, params.mBottom, params.height, params.topMargin, params.bottomMargin, this.mPaddingTop, this.mPaddingBottom, myHeight));
    }

    private void measureChildHorizontal(View child, LayoutParams params, int myWidth, int myHeight) {
        int maxHeight;
        int childWidthMeasureSpec = getChildMeasureSpec(params.mLeft, params.mRight, params.width, params.leftMargin, params.rightMargin, this.mPaddingLeft, this.mPaddingRight, myWidth);
        if (myHeight >= 0 || this.mAllowBrokenMeasureSpecs) {
            int heightMode;
            if (this.mMeasureVerticalWithPaddingMargin) {
                maxHeight = Math.max(0, (((myHeight - this.mPaddingTop) - this.mPaddingBottom) - params.topMargin) - params.bottomMargin);
            } else {
                maxHeight = Math.max(0, myHeight);
            }
            if (params.height == -1) {
                heightMode = 1073741824;
            } else {
                heightMode = Integer.MIN_VALUE;
            }
            maxHeight = MeasureSpec.makeMeasureSpec(maxHeight, heightMode);
        } else if (params.height >= 0) {
            maxHeight = MeasureSpec.makeMeasureSpec(params.height, 1073741824);
        } else {
            maxHeight = MeasureSpec.makeMeasureSpec(0, 0);
        }
        child.measure(childWidthMeasureSpec, maxHeight);
    }

    private int getChildMeasureSpec(int childStart, int childEnd, int childSize, int startMargin, int endMargin, int startPadding, int endPadding, int mySize) {
        int i = childStart;
        int i2 = childEnd;
        int i3 = childSize;
        int childSpecMode = 0;
        int childSpecSize = 0;
        boolean isUnspecified = mySize < 0;
        if (!isUnspecified) {
        } else if (!this.mAllowBrokenMeasureSpecs) {
            if (i != Integer.MIN_VALUE && i2 != Integer.MIN_VALUE) {
                childSpecSize = Math.max(0, i2 - i);
                childSpecMode = 1073741824;
            } else if (i3 >= 0) {
                childSpecSize = childSize;
                childSpecMode = 1073741824;
            } else {
                childSpecSize = 0;
                childSpecMode = 0;
            }
            return MeasureSpec.makeMeasureSpec(childSpecSize, childSpecMode);
        }
        int tempStart = childStart;
        int tempEnd = childEnd;
        if (tempStart == Integer.MIN_VALUE) {
            tempStart = startPadding + startMargin;
        }
        if (tempEnd == Integer.MIN_VALUE) {
            tempEnd = (mySize - endPadding) - endMargin;
        }
        int maxAvailable = tempEnd - tempStart;
        int i4 = 1073741824;
        if (i != Integer.MIN_VALUE && i2 != Integer.MIN_VALUE) {
            if (isUnspecified) {
                i4 = 0;
            }
            childSpecMode = i4;
            childSpecSize = Math.max(0, maxAvailable);
        } else if (i3 >= 0) {
            childSpecMode = 1073741824;
            if (maxAvailable >= 0) {
                childSpecSize = Math.min(maxAvailable, i3);
            } else {
                childSpecSize = childSize;
            }
        } else if (i3 == -1) {
            if (isUnspecified) {
                i4 = 0;
            }
            childSpecMode = i4;
            childSpecSize = Math.max(0, maxAvailable);
        } else if (i3 == -2) {
            if (maxAvailable >= 0) {
                childSpecMode = Integer.MIN_VALUE;
                childSpecSize = maxAvailable;
            } else {
                childSpecMode = 0;
                childSpecSize = 0;
            }
        }
        return MeasureSpec.makeMeasureSpec(childSpecSize, childSpecMode);
    }

    private boolean positionChildHorizontal(View child, LayoutParams params, int myWidth, boolean wrapContent) {
        int[] rules = params.getRules(getLayoutDirection());
        boolean z = true;
        if (params.mLeft == Integer.MIN_VALUE && params.mRight != Integer.MIN_VALUE) {
            params.mLeft = params.mRight - child.getMeasuredWidth();
        } else if (params.mLeft != Integer.MIN_VALUE && params.mRight == Integer.MIN_VALUE) {
            params.mRight = params.mLeft + child.getMeasuredWidth();
        } else if (params.mLeft == Integer.MIN_VALUE && params.mRight == Integer.MIN_VALUE) {
            if (rules[13] == 0 && rules[14] == 0) {
                positionAtEdge(child, params, myWidth);
            } else {
                if (wrapContent) {
                    positionAtEdge(child, params, myWidth);
                } else {
                    centerHorizontal(child, params, myWidth);
                }
                return true;
            }
        }
        if (rules[21] == 0) {
            z = false;
        }
        return z;
    }

    private void positionAtEdge(View child, LayoutParams params, int myWidth) {
        if (isLayoutRtl()) {
            params.mRight = (myWidth - this.mPaddingRight) - params.rightMargin;
            params.mLeft = params.mRight - child.getMeasuredWidth();
            return;
        }
        params.mLeft = this.mPaddingLeft + params.leftMargin;
        params.mRight = params.mLeft + child.getMeasuredWidth();
    }

    private boolean positionChildVertical(View child, LayoutParams params, int myHeight, boolean wrapContent) {
        int[] rules = params.getRules();
        boolean z = true;
        if (params.mTop == Integer.MIN_VALUE && params.mBottom != Integer.MIN_VALUE) {
            params.mTop = params.mBottom - child.getMeasuredHeight();
        } else if (params.mTop != Integer.MIN_VALUE && params.mBottom == Integer.MIN_VALUE) {
            params.mBottom = params.mTop + child.getMeasuredHeight();
        } else if (params.mTop == Integer.MIN_VALUE && params.mBottom == Integer.MIN_VALUE) {
            if (rules[13] == 0 && rules[15] == 0) {
                params.mTop = this.mPaddingTop + params.topMargin;
                params.mBottom = params.mTop + child.getMeasuredHeight();
            } else {
                if (wrapContent) {
                    params.mTop = this.mPaddingTop + params.topMargin;
                    params.mBottom = params.mTop + child.getMeasuredHeight();
                } else {
                    centerVertical(child, params, myHeight);
                }
                return true;
            }
        }
        if (rules[12] == 0) {
            z = false;
        }
        return z;
    }

    private void applyHorizontalSizeRules(LayoutParams childParams, int myWidth, int[] rules) {
        childParams.mLeft = Integer.MIN_VALUE;
        childParams.mRight = Integer.MIN_VALUE;
        LayoutParams anchorParams = getRelatedViewParams(rules, 0);
        if (anchorParams != null) {
            childParams.mRight = anchorParams.mLeft - (anchorParams.leftMargin + childParams.rightMargin);
        } else if (childParams.alignWithParent && rules[0] != 0 && myWidth >= 0) {
            childParams.mRight = (myWidth - this.mPaddingRight) - childParams.rightMargin;
        }
        anchorParams = getRelatedViewParams(rules, 1);
        if (anchorParams != null) {
            childParams.mLeft = anchorParams.mRight + (anchorParams.rightMargin + childParams.leftMargin);
        } else if (childParams.alignWithParent && rules[1] != 0) {
            childParams.mLeft = this.mPaddingLeft + childParams.leftMargin;
        }
        anchorParams = getRelatedViewParams(rules, 5);
        if (anchorParams != null) {
            childParams.mLeft = anchorParams.mLeft + childParams.leftMargin;
        } else if (childParams.alignWithParent && rules[5] != 0) {
            childParams.mLeft = this.mPaddingLeft + childParams.leftMargin;
        }
        anchorParams = getRelatedViewParams(rules, 7);
        if (anchorParams != null) {
            childParams.mRight = anchorParams.mRight - childParams.rightMargin;
        } else if (childParams.alignWithParent && rules[7] != 0 && myWidth >= 0) {
            childParams.mRight = (myWidth - this.mPaddingRight) - childParams.rightMargin;
        }
        if (rules[9] != 0) {
            childParams.mLeft = this.mPaddingLeft + childParams.leftMargin;
        }
        if (rules[11] != 0 && myWidth >= 0) {
            childParams.mRight = (myWidth - this.mPaddingRight) - childParams.rightMargin;
        }
    }

    private void applyVerticalSizeRules(LayoutParams childParams, int myHeight, int myBaseline) {
        int[] rules = childParams.getRules();
        int baselineOffset = getRelatedViewBaselineOffset(rules);
        if (baselineOffset != -1) {
            if (myBaseline != -1) {
                baselineOffset -= myBaseline;
            }
            childParams.mTop = baselineOffset;
            childParams.mBottom = Integer.MIN_VALUE;
            return;
        }
        childParams.mTop = Integer.MIN_VALUE;
        childParams.mBottom = Integer.MIN_VALUE;
        LayoutParams anchorParams = getRelatedViewParams(rules, 2);
        if (anchorParams != null) {
            childParams.mBottom = anchorParams.mTop - (anchorParams.topMargin + childParams.bottomMargin);
        } else if (childParams.alignWithParent && rules[2] != 0 && myHeight >= 0) {
            childParams.mBottom = (myHeight - this.mPaddingBottom) - childParams.bottomMargin;
        }
        anchorParams = getRelatedViewParams(rules, 3);
        if (anchorParams != null) {
            childParams.mTop = anchorParams.mBottom + (anchorParams.bottomMargin + childParams.topMargin);
        } else if (childParams.alignWithParent && rules[3] != 0) {
            childParams.mTop = this.mPaddingTop + childParams.topMargin;
        }
        anchorParams = getRelatedViewParams(rules, 6);
        if (anchorParams != null) {
            childParams.mTop = anchorParams.mTop + childParams.topMargin;
        } else if (childParams.alignWithParent && rules[6] != 0) {
            childParams.mTop = this.mPaddingTop + childParams.topMargin;
        }
        anchorParams = getRelatedViewParams(rules, 8);
        if (anchorParams != null) {
            childParams.mBottom = anchorParams.mBottom - childParams.bottomMargin;
        } else if (childParams.alignWithParent && rules[8] != 0 && myHeight >= 0) {
            childParams.mBottom = (myHeight - this.mPaddingBottom) - childParams.bottomMargin;
        }
        if (rules[10] != 0) {
            childParams.mTop = this.mPaddingTop + childParams.topMargin;
        }
        if (rules[12] != 0 && myHeight >= 0) {
            childParams.mBottom = (myHeight - this.mPaddingBottom) - childParams.bottomMargin;
        }
    }

    private View getRelatedView(int[] rules, int relation) {
        int id = rules[relation];
        if (id == 0) {
            return null;
        }
        Node node = (Node) this.mGraph.mKeyNodes.get(id);
        if (node == null) {
            return null;
        }
        View v = node.view;
        while (v.getVisibility() == 8) {
            node = (Node) this.mGraph.mKeyNodes.get(((LayoutParams) v.getLayoutParams()).getRules(v.getLayoutDirection())[relation]);
            if (node == null || v == node.view) {
                return null;
            }
            v = node.view;
        }
        return v;
    }

    private LayoutParams getRelatedViewParams(int[] rules, int relation) {
        View v = getRelatedView(rules, relation);
        if (v == null || !(v.getLayoutParams() instanceof LayoutParams)) {
            return null;
        }
        return (LayoutParams) v.getLayoutParams();
    }

    private int getRelatedViewBaselineOffset(int[] rules) {
        View v = getRelatedView(rules, 4);
        if (v != null) {
            int baseline = v.getBaseline();
            if (baseline != -1 && (v.getLayoutParams() instanceof LayoutParams)) {
                return ((LayoutParams) v.getLayoutParams()).mTop + baseline;
            }
        }
        return -1;
    }

    private static void centerHorizontal(View child, LayoutParams params, int myWidth) {
        int childWidth = child.getMeasuredWidth();
        int left = (myWidth - childWidth) / 2;
        params.mLeft = left;
        params.mRight = left + childWidth;
    }

    private static void centerVertical(View child, LayoutParams params, int myHeight) {
        int childHeight = child.getMeasuredHeight();
        int top = (myHeight - childHeight) / 2;
        params.mTop = top;
        params.mBottom = top + childHeight;
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams st = (LayoutParams) child.getLayoutParams();
                child.layout(st.mLeft, st.mTop, st.mRight, st.mBottom);
            }
        }
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* Access modifiers changed, original: protected */
    public android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    /* Access modifiers changed, original: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /* Access modifiers changed, original: protected */
    public android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams lp) {
        if (sPreserveMarginParamsInLayoutParamConversion) {
            if (lp instanceof LayoutParams) {
                return new LayoutParams((LayoutParams) lp);
            }
            if (lp instanceof MarginLayoutParams) {
                return new LayoutParams((MarginLayoutParams) lp);
            }
        }
        return new LayoutParams(lp);
    }

    public boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        if (this.mTopToBottomLeftToRightSet == null) {
            this.mTopToBottomLeftToRightSet = new TreeSet(new TopToBottomLeftToRightComparator());
        }
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            this.mTopToBottomLeftToRightSet.add(getChildAt(i));
        }
        for (View view : this.mTopToBottomLeftToRightSet) {
            if (view.getVisibility() == 0 && view.dispatchPopulateAccessibilityEvent(event)) {
                this.mTopToBottomLeftToRightSet.clear();
                return true;
            }
        }
        this.mTopToBottomLeftToRightSet.clear();
        return false;
    }

    public CharSequence getAccessibilityClassName() {
        return RelativeLayout.class.getName();
    }
}
