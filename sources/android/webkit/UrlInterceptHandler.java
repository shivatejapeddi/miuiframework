package android.webkit;

import android.annotation.UnsupportedAppUsage;
import android.webkit.CacheManager.CacheResult;
import java.util.Map;

@Deprecated
public interface UrlInterceptHandler {
    @Deprecated
    @UnsupportedAppUsage
    PluginData getPluginData(String str, Map<String, String> map);

    @Deprecated
    @UnsupportedAppUsage
    CacheResult service(String str, Map<String, String> map);
}
