package org.egret.plugin.mi.java.egretruntimelauncher;

import android.util.Log;
import dalvik.system.DexClassLoader;
import java.io.File;

public class EgretRuntimeLoader {
    private static final String GAME_ENGINE_CLASS = "org.egret.egretframeworknative.engine.EgretGameEngine";
    private static final String TAG = "EgretRuntimeLoader";
    private static EgretRuntimeLoader instance = null;
    private Class<?> egretGameEngineClass = null;
    private boolean loaded = false;

    private EgretRuntimeLoader() {
    }

    public static EgretRuntimeLoader get() {
        if (instance == null) {
            instance = new EgretRuntimeLoader();
        }
        return instance;
    }

    public void load(String library) {
        this.loaded = true;
        if (library.endsWith(".jar")) {
            loadJar(library);
        }
    }

    public void loadJar(String pathName) {
        File f = new File(pathName);
        f.setExecutable(true);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("loadJar: ");
        stringBuilder.append(pathName);
        stringBuilder.append(": ");
        stringBuilder.append(String.valueOf(f.exists()));
        Log.d(TAG, stringBuilder.toString());
        try {
            DexClassLoader classLoader = new DexClassLoader(pathName, new File(pathName).getParent(), null, getClass().getClassLoader());
            if (this.egretGameEngineClass == null) {
                this.egretGameEngineClass = classLoader.loadClass(GAME_ENGINE_CLASS);
            }
        } catch (Exception e) {
            Log.e("Loader", "need dex format jar");
            e.printStackTrace();
        }
    }

    public Class<?> getEgretGameEngineClass() {
        return this.egretGameEngineClass;
    }

    public boolean isLoaded() {
        return this.loaded;
    }
}
