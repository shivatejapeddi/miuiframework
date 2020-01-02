package com.miui.internal.viewcontentfetcher;

import android.os.Bundle;
import android.view.View;
import java.util.Map;
import miui.contentcatcher.sdk.Token;

public interface IViewContentFetcher {
    void createFetcher(Token token, Map<String, String> map);

    Bundle fetchViewContent(View view, int i);

    Bundle fetchWebViewBitmap(View view, int i);

    Map<String, Object> reservedFunction(Map<String, Object> map, int i);
}
