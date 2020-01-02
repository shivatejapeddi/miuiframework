package miui.maml;

import android.content.Context;
import java.io.File;
import miui.maml.util.ZipResourceLoader;

public class ScreenElementRootFactory {

    public static class Parameter {
        private Context mContext;
        private String mPath;
        private ResourceLoader mResourceLoader;

        public Parameter(Context context, String path) {
            if (context != null) {
                this.mContext = context.getApplicationContext();
            }
            this.mPath = path;
        }

        public Parameter(Context context, ResourceLoader loader) {
            if (context != null) {
                this.mContext = context.getApplicationContext();
            }
            this.mResourceLoader = loader;
        }
    }

    public static ScreenElementRoot create(Parameter param) {
        Context context = param.mContext;
        if (context != null) {
            ResourceLoader loader = param.mResourceLoader;
            String path = param.mPath;
            if (loader == null && path != null && new File(path).exists()) {
                loader = new ZipResourceLoader(path).setLocal(context.getResources().getConfiguration().locale);
            }
            if (loader == null) {
                return null;
            }
            return new ScreenElementRoot(new ScreenContext(context, new LifecycleResourceManager(loader, 3600000, 360000)));
        }
        throw new NullPointerException();
    }
}
