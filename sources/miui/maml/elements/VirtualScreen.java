package miui.maml.elements;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import miui.maml.ScreenElementRoot;
import miui.maml.data.VariableNames;
import miui.maml.elements.BitmapProvider.IBitmapHolder;
import miui.maml.elements.BitmapProvider.VersionedBitmap;
import miui.maml.util.Utils;
import org.w3c.dom.Element;

public class VirtualScreen extends ElementGroup implements IBitmapHolder {
    public static final String TAG_NAME = "VirtualScreen";
    private Bitmap mScreenBitmap;
    private Canvas mScreenCanvas;
    private boolean mTicked;
    private VersionedBitmap mVersionedBitmap;

    public VirtualScreen(Element node, ScreenElementRoot root) {
        super(node, root);
    }

    public void init() {
        super.init();
        float width = getWidth();
        if (width < 0.0f) {
            width = scale(Utils.getVariableNumber(VariableNames.SCREEN_WIDTH, getVariables()));
        }
        float height = getHeight();
        if (height < 0.0f) {
            height = scale(Utils.getVariableNumber(VariableNames.SCREEN_HEIGHT, getVariables()));
        }
        this.mScreenBitmap = Bitmap.createBitmap(Math.round(width), Math.round(height), Config.ARGB_8888);
        this.mScreenBitmap.setDensity(this.mRoot.getTargetDensity());
        this.mScreenCanvas = new Canvas(this.mScreenBitmap);
        this.mVersionedBitmap = new VersionedBitmap(this.mScreenBitmap);
    }

    public void finish() {
        super.finish();
        this.mScreenBitmap.recycle();
        this.mScreenBitmap = null;
        this.mScreenCanvas = null;
    }

    /* Access modifiers changed, original: protected */
    public void doRender(Canvas c) {
        if (this.mTicked) {
            this.mTicked = false;
            this.mScreenCanvas.save();
            this.mScreenCanvas.concat(getMatrix());
            this.mScreenCanvas.drawColor(0, Mode.CLEAR);
            super.doRender(this.mScreenCanvas);
            this.mScreenCanvas.restore();
            this.mVersionedBitmap.updateVersion();
        }
    }

    /* Access modifiers changed, original: protected */
    public void doTick(long currentTime) {
        super.doTick(currentTime);
        this.mTicked = true;
    }

    public Bitmap getBitmap() {
        return this.mScreenBitmap;
    }

    public VersionedBitmap getBitmap(String id) {
        return this.mVersionedBitmap;
    }
}
