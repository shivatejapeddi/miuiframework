package miui.maml.elements;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.text.TextUtils;
import android.util.Log;
import miui.maml.ScreenElementRoot;
import miui.maml.elements.BitmapProvider.VersionedBitmap;
import miui.maml.util.Utils;
import miui.widget.SpectrumVisualizer;
import org.w3c.dom.Element;

public class SpectrumVisualizerScreenElement extends ImageScreenElement {
    public static final String TAG_NAME = "SpectrumVisualizer";
    private int mAlphaWidthNum;
    private Canvas mCanvas;
    private String mDotbar;
    private Bitmap mPanel;
    private String mPanelSrc;
    private int mResDensity;
    private String mShadow;
    private SpectrumVisualizer mSpectrumVisualizer;

    public SpectrumVisualizerScreenElement(Element node, ScreenElementRoot root) {
        super(node, root);
        load(node);
    }

    private void load(Element node) {
        if (node != null) {
            this.mPanelSrc = node.getAttribute("panelSrc");
            this.mDotbar = node.getAttribute("dotbarSrc");
            this.mShadow = node.getAttribute("shadowSrc");
            this.mSpectrumVisualizer = new SpectrumVisualizer(getContext().mContext);
            this.mSpectrumVisualizer.setSoftDrawEnabled(false);
            this.mSpectrumVisualizer.enableUpdate(false);
            this.mAlphaWidthNum = Utils.getAttrAsInt(node, "alphaWidthNum", -1);
        }
    }

    public void init() {
        super.init();
        Bitmap shadow = null;
        this.mPanel = TextUtils.isEmpty(this.mPanelSrc) ? null : getContext().mResourceManager.getBitmap(this.mPanelSrc);
        Bitmap dotbar = TextUtils.isEmpty(this.mDotbar) ? null : getContext().mResourceManager.getBitmap(this.mDotbar);
        if (!TextUtils.isEmpty(this.mShadow)) {
            shadow = getContext().mResourceManager.getBitmap(this.mShadow);
        }
        int width = (int) getWidth();
        int height = (int) getHeight();
        String str = "SpectrumVisualizerScreenElement";
        if (width <= 0 || height <= 0) {
            Bitmap bitmap = this.mPanel;
            if (bitmap != null) {
                width = bitmap.getWidth();
                height = this.mPanel.getHeight();
            } else {
                Log.e(str, "no panel or size");
                return;
            }
        }
        if (dotbar == null) {
            Log.e(str, "no dotbar");
            return;
        }
        this.mSpectrumVisualizer.setBitmaps(width, height, dotbar, shadow);
        int i = this.mAlphaWidthNum;
        if (i >= 0) {
            this.mSpectrumVisualizer.setAlphaNum(i);
        }
        this.mResDensity = dotbar.getDensity();
        this.mSpectrumVisualizer.layout(0, 0, width, height);
        Bitmap bmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        bmp.setDensity(this.mResDensity);
        this.mCanvas = new Canvas(bmp);
        this.mBitmap.setBitmap(bmp);
    }

    /* Access modifiers changed, original: protected */
    public void doRender(Canvas c) {
        if (this.mPanel != null) {
            this.mPaint.setAlpha(getAlpha());
            c.drawBitmap(this.mPanel, getLeft(), getTop(), this.mPaint);
        }
        super.doRender(c);
    }

    /* Access modifiers changed, original: protected */
    public VersionedBitmap getBitmap(boolean sync) {
        Canvas canvas = this.mCanvas;
        if (canvas == null) {
            return null;
        }
        canvas.drawColor(0, Mode.CLEAR);
        this.mCanvas.setDensity(0);
        this.mSpectrumVisualizer.draw(this.mCanvas);
        this.mCanvas.setDensity(this.mResDensity);
        this.mBitmap.updateVersion();
        return this.mBitmap;
    }

    public void enableUpdate(boolean b) {
        this.mSpectrumVisualizer.enableUpdate(b);
    }
}
