package miui.contentcatcher;

import com.miui.internal.contentcatcher.IInterceptor;

public interface IInterceptorContainer {
    IInterceptor getInterceptor();

    void setInterceptor(IInterceptor iInterceptor);
}
