package miui.maml.shader;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader.TileMode;
import miui.maml.ScreenElementRoot;
import org.w3c.dom.Element;

public class BitmapShaderElement extends ShaderElement {
    public static final String TAG_NAME = "BitmapShader";
    private Bitmap mBitmap;
    private TileMode mTileModeX;
    private TileMode mTileModeY;

    public BitmapShaderElement(Element node, ScreenElementRoot root) {
        super(node, root);
        this.mBitmap = this.mRoot.getContext().mResourceManager.getBitmap(node.getAttribute("src"));
        resolveTileMode(node);
        this.mShader = new BitmapShader(this.mBitmap, this.mTileModeX, this.mTileModeY);
    }

    private void resolveTileMode(Element node) {
        String[] tiles = node.getAttribute("tile").split(",");
        if (tiles.length <= 1) {
            TileMode tileMode = this.mTileMode;
            this.mTileModeY = tileMode;
            this.mTileModeX = tileMode;
            return;
        }
        this.mTileModeX = ShaderElement.getTileMode(tiles[0]);
        this.mTileModeY = ShaderElement.getTileMode(tiles[1]);
    }

    public void updateShader() {
    }

    public void onGradientStopsChanged() {
    }

    public boolean updateShaderMatrix() {
        return false;
    }
}
