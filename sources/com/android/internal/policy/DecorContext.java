package com.android.internal.policy;

import android.content.AutofillOptions;
import android.content.ContentCaptureOptions;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.view.ContextThemeWrapper;
import android.view.WindowManager;
import android.view.WindowManagerImpl;
import android.view.contentcapture.ContentCaptureManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import java.lang.ref.WeakReference;

@VisibleForTesting(visibility = Visibility.PACKAGE)
public class DecorContext extends ContextThemeWrapper {
    private WeakReference<Context> mActivityContext;
    private Resources mActivityResources;
    private ContentCaptureManager mContentCaptureManager;
    private PhoneWindow mPhoneWindow;
    private WindowManager mWindowManager;

    @VisibleForTesting
    public DecorContext(Context context, Context activityContext) {
        super(context.createDisplayContext(activityContext.getDisplay()), null);
        this.mActivityContext = new WeakReference(activityContext);
        this.mActivityResources = activityContext.getResources();
    }

    /* Access modifiers changed, original: 0000 */
    public void setPhoneWindow(PhoneWindow phoneWindow) {
        this.mPhoneWindow = phoneWindow;
        this.mWindowManager = null;
    }

    public Object getSystemService(String name) {
        String str = Context.WINDOW_SERVICE;
        if (str.equals(name)) {
            if (this.mWindowManager == null) {
                this.mWindowManager = ((WindowManagerImpl) super.getSystemService(str)).createLocalWindowManager(this.mPhoneWindow);
            }
            return this.mWindowManager;
        } else if (!"content_capture".equals(name)) {
            return super.getSystemService(name);
        } else {
            if (this.mContentCaptureManager == null) {
                Context activityContext = (Context) this.mActivityContext.get();
                if (activityContext != null) {
                    this.mContentCaptureManager = (ContentCaptureManager) activityContext.getSystemService(name);
                }
            }
            return this.mContentCaptureManager;
        }
    }

    public Resources getResources() {
        Context activityContext = (Context) this.mActivityContext.get();
        if (activityContext != null) {
            this.mActivityResources = activityContext.getResources();
        }
        return this.mActivityResources;
    }

    public AssetManager getAssets() {
        return this.mActivityResources.getAssets();
    }

    public AutofillOptions getAutofillOptions() {
        Context activityContext = (Context) this.mActivityContext.get();
        if (activityContext != null) {
            return activityContext.getAutofillOptions();
        }
        return null;
    }

    public ContentCaptureOptions getContentCaptureOptions() {
        Context activityContext = (Context) this.mActivityContext.get();
        if (activityContext != null) {
            return activityContext.getContentCaptureOptions();
        }
        return null;
    }
}
