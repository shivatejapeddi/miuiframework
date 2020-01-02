package miui.maml.shader;

import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.provider.BrowserContract.Bookmarks;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import miui.maml.ScreenElementRoot;
import miui.maml.data.Expression;
import miui.maml.data.Variables;
import miui.maml.util.ColorParser;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public abstract class ShaderElement {
    private static final String LOG_TAG = "ShaderElement";
    protected GradientStops mGradientStops = new GradientStops();
    protected ScreenElementRoot mRoot;
    protected Shader mShader;
    protected Matrix mShaderMatrix = new Matrix();
    protected TileMode mTileMode;
    protected float mX;
    protected Expression mXExp;
    protected float mY;
    protected Expression mYExp;

    protected final class GradientStop {
        public static final String TAG_NAME = "GradientStop";
        private ColorParser mColorParser;
        private Expression mPositionExp;

        public GradientStop(Element node, ScreenElementRoot root) {
            this.mColorParser = ColorParser.fromElement(ShaderElement.this.mRoot.getVariables(), node);
            this.mPositionExp = Expression.build(ShaderElement.this.mRoot.getVariables(), node.getAttribute(Bookmarks.POSITION));
            if (this.mPositionExp == null) {
                Log.e(TAG_NAME, "lost position attribute.");
            }
        }

        public int getColor() {
            return this.mColorParser.getColor();
        }

        public float getPosition() {
            return (float) this.mPositionExp.evaluate();
        }
    }

    protected final class GradientStops {
        private int[] mColors;
        protected ArrayList<GradientStop> mGradientStopArr = new ArrayList();
        private float[] mPositions;

        protected GradientStops() {
        }

        public void init() {
            this.mColors = new int[size()];
            this.mPositions = new float[size()];
        }

        public int[] getColors() {
            return this.mColors;
        }

        public float[] getPositions() {
            return this.mPositions;
        }

        public void update() {
            boolean changed = false;
            for (int i = 0; i < size(); i++) {
                int c = ((GradientStop) this.mGradientStopArr.get(i)).getColor();
                if (c != this.mColors[i]) {
                    changed = true;
                }
                this.mColors[i] = c;
                float p = ((GradientStop) this.mGradientStopArr.get(i)).getPosition();
                if (p != this.mPositions[i]) {
                    changed = true;
                }
                this.mPositions[i] = p;
            }
            if (changed) {
                ShaderElement.this.onGradientStopsChanged();
            }
        }

        public void add(GradientStop stop) {
            this.mGradientStopArr.add(stop);
        }

        public int size() {
            return this.mGradientStopArr.size();
        }
    }

    public abstract void onGradientStopsChanged();

    public abstract boolean updateShaderMatrix();

    public ShaderElement(Element node, ScreenElementRoot root) {
        this.mRoot = root;
        Variables vars = getVariables();
        this.mXExp = Expression.build(vars, node.getAttribute("x"));
        this.mYExp = Expression.build(vars, node.getAttribute("y"));
        this.mTileMode = getTileMode(node.getAttribute("tile"));
        if (!node.getTagName().equalsIgnoreCase(BitmapShaderElement.TAG_NAME)) {
            loadGradientStops(node, root);
        }
    }

    /* Access modifiers changed, original: protected */
    public Variables getVariables() {
        return this.mRoot.getVariables();
    }

    public static TileMode getTileMode(String strTile) {
        if (TextUtils.isEmpty(strTile)) {
            return TileMode.CLAMP;
        }
        if (strTile.equalsIgnoreCase("mirror")) {
            return TileMode.MIRROR;
        }
        if (strTile.equalsIgnoreCase("repeat")) {
            return TileMode.REPEAT;
        }
        return TileMode.CLAMP;
    }

    private void loadGradientStops(Element node, ScreenElementRoot root) {
        NodeList nodeList = node.getElementsByTagName(GradientStop.TAG_NAME);
        for (int i = 0; i < nodeList.getLength(); i++) {
            this.mGradientStops.add(new GradientStop((Element) nodeList.item(i), root));
        }
        if (this.mGradientStops.size() <= 0) {
            Log.e(LOG_TAG, "lost gradient stop.");
        } else {
            this.mGradientStops.init();
        }
    }

    public float getX() {
        Expression expression = this.mXExp;
        return (float) (((double) this.mRoot.getScale()) * (expression != null ? expression.evaluate() : 0.0d));
    }

    public float getY() {
        Expression expression = this.mYExp;
        return (float) (((double) this.mRoot.getScale()) * (expression != null ? expression.evaluate() : 0.0d));
    }

    public void updateShader() {
        this.mGradientStops.update();
        if (updateShaderMatrix()) {
            this.mShader.setLocalMatrix(this.mShaderMatrix);
        }
    }

    public Shader getShader() {
        return this.mShader;
    }
}
