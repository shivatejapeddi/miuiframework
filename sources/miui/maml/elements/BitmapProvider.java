package miui.maml.elements;

import android.content.ComponentName;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import miui.maml.ObjectFactory.BitmapProviderFactory;
import miui.maml.ResourceManager.AsyncLoadListener;
import miui.maml.ResourceManager.BitmapInfo;
import miui.maml.ScreenElementRoot;
import miui.maml.data.Expression;
import miui.maml.data.IndexedVariable;
import miui.maml.util.Utils;
import miui.maml.util.net.IOUtils;

public abstract class BitmapProvider {
    private static final String LOG_TAG = "BitmapProvider";
    protected ScreenElementRoot mRoot;
    protected VersionedBitmap mVersionedBitmap = new VersionedBitmap(null);

    private static class AppIconProvider extends BitmapProvider {
        public static final String TAG_NAME = "ApplicationIcon";
        private String mCls;
        private boolean mNoIcon;
        private String mPkg;

        public AppIconProvider(ScreenElementRoot root) {
            super(root);
        }

        public void init(String src) {
            super.init(src);
            this.mNoIcon = false;
            String str = "invalid src of ApplicationIcon type: ";
            String str2 = "BitmapProvider";
            StringBuilder stringBuilder;
            if (TextUtils.isEmpty(src)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(src);
                Log.e(str2, stringBuilder.toString());
                this.mNoIcon = true;
                return;
            }
            String[] name = src.split(",");
            if (name.length == 2) {
                this.mPkg = name[0];
                this.mCls = name[1];
                return;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(src);
            Log.e(str2, stringBuilder.toString());
            this.mNoIcon = true;
        }

        public VersionedBitmap getBitmap(String src, boolean sync, int w, int h) {
            if (this.mVersionedBitmap.getBitmap() == null && !this.mNoIcon) {
                try {
                    Drawable d = this.mRoot.getContext().mContext.getPackageManager().getActivityIcon(new ComponentName(this.mPkg, this.mCls));
                    if (d instanceof BitmapDrawable) {
                        this.mVersionedBitmap.setBitmap(((BitmapDrawable) d).getBitmap());
                    } else {
                        this.mNoIcon = true;
                    }
                } catch (NameNotFoundException e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("fail to get icon for src of ApplicationIcon type: ");
                    stringBuilder.append(src);
                    Log.e("BitmapProvider", stringBuilder.toString());
                    this.mNoIcon = true;
                }
            }
            return this.mVersionedBitmap;
        }
    }

    public static class BitmapHolderProvider extends BitmapProvider {
        public static final String TAG_NAME = "BitmapHolder";
        private IBitmapHolder mBitmapHolder;
        private String mId;

        public BitmapHolderProvider(ScreenElementRoot root) {
            super(root);
        }

        public void init(String src) {
            super.init(src);
            if (!TextUtils.isEmpty(src)) {
                String name;
                int dot = src.indexOf(46);
                if (dot == -1) {
                    name = src;
                } else {
                    name = src.substring(0, dot);
                    this.mId = src.substring(dot + 1);
                }
                ScreenElement se = this.mRoot.findElement(name);
                if (se instanceof IBitmapHolder) {
                    this.mBitmapHolder = (IBitmapHolder) se;
                }
            }
        }

        public VersionedBitmap getBitmap(String src, boolean sync, int w, int h) {
            IBitmapHolder iBitmapHolder = this.mBitmapHolder;
            return iBitmapHolder != null ? iBitmapHolder.getBitmap(this.mId) : null;
        }
    }

    public static class BitmapVariableProvider extends BitmapProvider {
        public static final String TAG_NAME = "BitmapVar";
        private String mCurSrc;
        private Expression mIndexExpression;
        private IndexedVariable mVar;

        public BitmapVariableProvider(ScreenElementRoot root) {
            super(root);
        }

        public void init(String src) {
            super.init(src);
            if (!TextUtils.isEmpty(src)) {
                this.mVar = new IndexedVariable(src, this.mRoot.getVariables(), false);
                this.mCurSrc = src;
            }
        }

        public VersionedBitmap getBitmap(String src, boolean sync, int w, int h) {
            if (!Utils.equals(this.mCurSrc, src)) {
                this.mVar = null;
                this.mIndexExpression = null;
                if (!TextUtils.isEmpty(src)) {
                    int leftBracket = src.indexOf(91);
                    int len = src.length();
                    if (leftBracket != -1 && leftBracket < len - 1 && src.charAt(len - 1) == ']') {
                        this.mIndexExpression = Expression.build(this.mRoot.getVariables(), src.substring(leftBracket + 1, len - 1));
                    }
                    this.mVar = new IndexedVariable(this.mIndexExpression == null ? src : src.substring(0, leftBracket), this.mRoot.getVariables(), false);
                }
                this.mCurSrc = src;
            }
            Bitmap bmp = null;
            try {
                if (this.mVar != null) {
                    bmp = this.mIndexExpression != null ? (Bitmap) this.mVar.getArr((int) this.mIndexExpression.evaluate()) : (Bitmap) this.mVar.get();
                }
            } catch (ClassCastException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("fail to cast as Bitmap from object: ");
                stringBuilder.append(src);
                Log.w("BitmapProvider", stringBuilder.toString());
            }
            this.mVersionedBitmap.setBitmap(bmp);
            return this.mVersionedBitmap;
        }
    }

    private static class UriProvider extends BitmapProvider {
        public static final String TAG_NAME = "Uri";
        private String mCachedBitmapUri;
        private String mCurLoadingBitmapUri;
        private Object mLock = new Object();

        private class LoaderAsyncTask extends AsyncTask<Object, Object, Bitmap> {
            private int mHeight = -1;
            private String mUri = null;
            private int mWidth = -1;

            public LoaderAsyncTask(String uri, int width, int height) {
                this.mUri = uri;
                this.mWidth = width;
                this.mHeight = height;
            }

            /* Access modifiers changed, original: protected|varargs */
            public Bitmap doInBackground(Object... params) {
                Bitmap bmp = UriProvider.this.getBitmapFromUri(Uri.parse(this.mUri), this.mWidth, this.mHeight);
                if (bmp == null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("fail to decode bitmap: ");
                    stringBuilder.append(this.mUri);
                    Log.w("BitmapProvider", stringBuilder.toString());
                }
                synchronized (UriProvider.this.mLock) {
                    if (TextUtils.equals(this.mUri, UriProvider.this.mCurLoadingBitmapUri)) {
                        UriProvider.this.mVersionedBitmap.setBitmap(bmp);
                        UriProvider.this.mCachedBitmapUri = UriProvider.this.mCurLoadingBitmapUri;
                        UriProvider.this.mRoot.requestUpdate();
                        UriProvider.this.mCurLoadingBitmapUri = null;
                    }
                }
                return bmp;
            }
        }

        public UriProvider(ScreenElementRoot root) {
            super(root);
        }

        public VersionedBitmap getBitmap(String src, boolean sync, int w, int h) {
            if (TextUtils.isEmpty(src)) {
                this.mVersionedBitmap.setBitmap(null);
                return this.mVersionedBitmap;
            }
            Bitmap bmp = this.mVersionedBitmap.getBitmap();
            if ((bmp != null && bmp.isRecycled()) || !TextUtils.equals(this.mCachedBitmapUri, src)) {
                synchronized (this.mLock) {
                    if (!(TextUtils.equals(this.mCurLoadingBitmapUri, src) || TextUtils.equals(this.mCachedBitmapUri, src))) {
                        this.mCurLoadingBitmapUri = src;
                        new LoaderAsyncTask(src, w, h).execute(new Object[0]);
                    }
                }
            }
            this.mVersionedBitmap.setBitmap(bmp);
            return this.mVersionedBitmap;
        }

        public void finish() {
            super.finish();
            synchronized (this.mLock) {
                this.mCachedBitmapUri = null;
                this.mCurLoadingBitmapUri = null;
                this.mVersionedBitmap.reset();
            }
        }
    }

    private static class FileSystemProvider extends UriProvider {
        public static final String TAG_NAME = "FileSystem";

        public FileSystemProvider(ScreenElementRoot root) {
            super(root);
        }

        public VersionedBitmap getBitmap(String src, boolean sync, int w, int h) {
            if (TextUtils.isEmpty(src)) {
                this.mVersionedBitmap.setBitmap(null);
                return this.mVersionedBitmap;
            }
            URI uri = new File(src).toURI();
            if (uri != null) {
                return super.getBitmap(uri.toString(), sync, w, h);
            }
            this.mVersionedBitmap.setBitmap(null);
            return this.mVersionedBitmap;
        }
    }

    public interface IBitmapHolder {
        VersionedBitmap getBitmap(String str);
    }

    private static class ResourceImageProvider extends BitmapProvider {
        public static final String TAG_NAME = "ResourceImage";
        private AsyncLoadListener mAsyncLoadListener = new AsyncLoadListener() {
            public void onLoadComplete(String src, BitmapInfo info) {
                synchronized (ResourceImageProvider.this.mSrcNameLock) {
                    StringBuilder stringBuilder;
                    if (TextUtils.equals(src, ResourceImageProvider.this.mLoadingBitmapName)) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("load image async complete: ");
                        stringBuilder.append(src);
                        stringBuilder.append(" last cached ");
                        stringBuilder.append(ResourceImageProvider.this.mCachedBitmapName);
                        Log.i("BitmapProvider", stringBuilder.toString());
                        ResourceImageProvider.this.mVersionedBitmap.setBitmap(info == null ? null : info.mBitmap);
                        ResourceImageProvider.this.mCachedBitmapName = src;
                        ResourceImageProvider.this.mLoadingBitmapName = null;
                    } else {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("load image async complete: ");
                        stringBuilder.append(src);
                        stringBuilder.append(" not equals ");
                        stringBuilder.append(ResourceImageProvider.this.mLoadingBitmapName);
                        Log.i("BitmapProvider", stringBuilder.toString());
                    }
                }
                ResourceImageProvider.this.mRoot.requestUpdate();
            }
        };
        private String mCachedBitmapName;
        String mLoadingBitmapName;
        Object mSrcNameLock = new Object();

        public ResourceImageProvider(ScreenElementRoot root) {
            super(root);
        }

        public VersionedBitmap getBitmap(String src, boolean sync, int w, int h) {
            Bitmap bmp = this.mVersionedBitmap.getBitmap();
            if ((bmp != null && bmp.isRecycled()) || !TextUtils.equals(this.mCachedBitmapName, src)) {
                Bitmap bitmap = null;
                BitmapInfo info;
                if (sync) {
                    info = this.mRoot.getContext().mResourceManager.getBitmapInfo(src);
                    VersionedBitmap versionedBitmap = this.mVersionedBitmap;
                    if (info != null) {
                        bitmap = info.mBitmap;
                    }
                    versionedBitmap.setBitmap(bitmap);
                    this.mCachedBitmapName = src;
                } else {
                    info = this.mRoot.getContext().mResourceManager.getBitmapInfoAsync(src, this.mAsyncLoadListener);
                    synchronized (this.mSrcNameLock) {
                        if (info != null) {
                            if (info.mLoading) {
                                this.mLoadingBitmapName = src;
                            }
                        }
                        this.mVersionedBitmap.setBitmap(info == null ? null : info.mBitmap);
                        this.mCachedBitmapName = src;
                        this.mLoadingBitmapName = null;
                    }
                }
            }
            return this.mVersionedBitmap;
        }

        public void finish() {
            super.finish();
            synchronized (this.mSrcNameLock) {
                this.mLoadingBitmapName = null;
                this.mCachedBitmapName = null;
                this.mVersionedBitmap.reset();
            }
        }
    }

    public static class VersionedBitmap {
        private Bitmap mBitmap;
        private int mVersion;

        public VersionedBitmap(Bitmap bmp) {
            this.mBitmap = bmp;
        }

        public void set(VersionedBitmap versionedBmp) {
            if (versionedBmp != null) {
                this.mBitmap = versionedBmp.mBitmap;
                this.mVersion = versionedBmp.mVersion;
                return;
            }
            reset();
        }

        public boolean setBitmap(Bitmap bmp) {
            if (bmp != this.mBitmap) {
                this.mBitmap = bmp;
                this.mVersion++;
            }
            if (bmp != this.mBitmap) {
                return true;
            }
            return false;
        }

        public int updateVersion() {
            int i = this.mVersion;
            this.mVersion = i + 1;
            return i;
        }

        public Bitmap getBitmap() {
            return this.mBitmap;
        }

        public void reset() {
            this.mBitmap = null;
            this.mVersion = 0;
        }

        public static boolean equals(VersionedBitmap a, VersionedBitmap b) {
            return a != null && b != null && a.mBitmap == b.mBitmap && a.mVersion == b.mVersion;
        }
    }

    private static class VirtualScreenProvider extends BitmapProvider {
        public static final String TAG_NAME = "VirtualScreen";
        private VirtualScreen mVirtualScreen;

        public VirtualScreenProvider(ScreenElementRoot root) {
            super(root);
        }

        public void init(String src) {
            super.init(src);
            ScreenElement se = this.mRoot.findElement(src);
            if (se instanceof VirtualScreen) {
                this.mVirtualScreen = (VirtualScreen) se;
            }
        }

        public VersionedBitmap getBitmap(String src, boolean sync, int w, int h) {
            VersionedBitmap versionedBitmap = this.mVersionedBitmap;
            VirtualScreen virtualScreen = this.mVirtualScreen;
            versionedBitmap.setBitmap(virtualScreen != null ? virtualScreen.getBitmap() : null);
            return this.mVersionedBitmap;
        }
    }

    public static BitmapProvider create(ScreenElementRoot root, String type) {
        if (TextUtils.equals(type, ResourceImageProvider.TAG_NAME)) {
            return new ResourceImageProvider(root);
        }
        if (TextUtils.equals(type, "VirtualScreen")) {
            return new VirtualScreenProvider(root);
        }
        if (TextUtils.equals(type, AppIconProvider.TAG_NAME)) {
            return new AppIconProvider(root);
        }
        if (TextUtils.equals(type, FileSystemProvider.TAG_NAME)) {
            return new FileSystemProvider(root);
        }
        if (TextUtils.equals(type, UriProvider.TAG_NAME)) {
            return new UriProvider(root);
        }
        if (TextUtils.equals(type, BitmapHolderProvider.TAG_NAME)) {
            return new BitmapHolderProvider(root);
        }
        if (TextUtils.equals(type, BitmapVariableProvider.TAG_NAME)) {
            return new BitmapVariableProvider(root);
        }
        BitmapProviderFactory f = (BitmapProviderFactory) root.getContext().getObjectFactory("BitmapProvider");
        if (f != null) {
            BitmapProvider provider = f.create(root, type);
            if (provider != null) {
                return provider;
            }
        }
        return new ResourceImageProvider(root);
    }

    public BitmapProvider(ScreenElementRoot root) {
        this.mRoot = root;
    }

    public void init(String src) {
        reset();
    }

    public void reset() {
    }

    public void finish() {
        this.mVersionedBitmap.reset();
    }

    public VersionedBitmap getBitmap(String src, boolean sync, int w, int h) {
        return this.mVersionedBitmap;
    }

    /* Access modifiers changed, original: protected */
    public Bitmap getBitmapFromUri(Uri uri, int width, int height) {
        InputStream in1 = null;
        InputStream in2 = null;
        Bitmap bitmap = null;
        try {
            in1 = this.mRoot.getContext().mContext.getContentResolver().openInputStream(uri);
            if (width <= 0 || height <= 0) {
                bitmap = BitmapFactory.decodeStream(in1, null, null);
                IOUtils.closeQuietly(in1);
                IOUtils.closeQuietly(null);
                return bitmap;
            }
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in1, null, options);
            options.inSampleSize = computeSampleSize(options, width * height);
            options.inJustDecodeBounds = false;
            options.outHeight = height;
            options.outWidth = width;
            in2 = this.mRoot.getContext().mContext.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(in2, null, options);
            return bitmap;
        } catch (Exception e) {
            Log.d("BitmapProvider", "getBitmapFromUri Exception", e);
            return bitmap;
        } finally {
            IOUtils.closeQuietly(in1);
            IOUtils.closeQuietly(in2);
        }
    }

    private static int computeSampleSize(Options options, int maxNumOfPixels) {
        int finalSize = 1;
        while (((double) (finalSize * 2)) <= Math.sqrt((((double) options.outHeight) * ((double) options.outWidth)) / ((double) maxNumOfPixels))) {
            finalSize *= 2;
        }
        return finalSize;
    }
}
