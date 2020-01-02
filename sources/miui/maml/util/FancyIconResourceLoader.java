package miui.maml.util;

import java.io.InputStream;
import miui.content.res.ThemeResources;
import miui.content.res.ThemeResourcesSystem;
import miui.maml.ResourceLoader;

public class FancyIconResourceLoader extends ResourceLoader {
    private String mRelatviePathBaseIcons;

    public FancyIconResourceLoader(String relativePathBaseIcons) {
        this.mRelatviePathBaseIcons = relativePathBaseIcons;
    }

    public boolean resourceExists(String path) {
        ThemeResourcesSystem system = ThemeResources.getSystem();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mRelatviePathBaseIcons);
        stringBuilder.append(path);
        return system.hasIcon(stringBuilder.toString());
    }

    public InputStream getInputStream(String path, long[] size) {
        ThemeResourcesSystem system = ThemeResources.getSystem();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mRelatviePathBaseIcons);
        stringBuilder.append(path);
        return system.getIconStream(stringBuilder.toString(), size);
    }
}
