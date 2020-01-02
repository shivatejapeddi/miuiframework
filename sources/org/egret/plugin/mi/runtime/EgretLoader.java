package org.egret.plugin.mi.runtime;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera.Parameters;
import android.telecom.Logging.Session;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;
import org.egret.plugin.mi.java.egretruntimelauncher.EgretRuntimeLauncher;
import org.egret.plugin.mi.java.egretruntimelauncher.EgretRuntimeLauncher.EgretRuntimeDownloadListener;

public class EgretLoader {
    private static final String LOGTAG = "EgretLoader";
    private static final String MI_APPID = "2000";
    private static final String MI_APPKEY = "3321031F35156D389B0B272910695E3F";
    private Activity activity;
    private Object gameEngine;
    private EgretRuntimeLauncher launcher;
    private HashMap<String, String> options;

    private class GameEngineMethod {
        public static final String CALL_INTERFACE = "callEgretInterface";
        public static final String ENABLE_INTERFACE = "enableEgretRuntimeInterface";
        public static final String GET_VIEW = "game_engine_get_view";
        public static final String INIT = "game_engine_init";
        public static final String ON_PAUSE = "game_engine_onPause";
        public static final String ON_RESUME = "game_engine_onResume";
        public static final String SET_INTERFACE = "setRuntimeInterfaceSet";
        public static final String SET_OPTIONS = "game_engine_set_options";
        public static final String STOP = "game_engine_onStop";

        private GameEngineMethod() {
        }
    }

    private class GameOptionName {
        public static final String EGRET_ROOT = "egret.runtime.egretRoot";
        public static final String GAME_ID = "egret.runtime.gameId";
        public static final String LOADER_TYPE = "egret.runtime.libraryLoaderType";
        public static final String LOADER_URL = "egret.runtime.loaderUrl";
        public static final String ORIENTATION = "egret.runtime.screenOrientation";
        public static final String UPDATE_URL = "egret.runtime.updateUrl";

        private GameOptionName() {
        }
    }

    public EgretLoader(Context context) {
        Log.d(LOGTAG, "EgretLoader(Context context)");
        if (checkContext(context)) {
            this.options = new HashMap();
            this.activity = (Activity) context;
        }
    }

    private boolean checkContext(Context context) {
        if (!Activity.class.isInstance(context)) {
            return false;
        }
        try {
            context.getClass().getMethod("setEgretRuntimeListener", new Class[]{Object.class}).invoke(context, new Object[]{this});
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @JavascriptInterface
    public void setOption(String key, String value) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("setOption: ");
        stringBuilder.append(key);
        stringBuilder.append(Session.SUBSESSION_SEPARATION_CHAR);
        stringBuilder.append(value);
        Log.d(LOGTAG, stringBuilder.toString());
        if (!checkEgretContext()) {
            Object key2;
            if (key2.equals("gameId")) {
                key2 = GameOptionName.GAME_ID;
            } else if (key2.equals("gameUrl")) {
                key2 = GameOptionName.LOADER_URL;
                HashMap hashMap = this.options;
                String str = GameOptionName.UPDATE_URL;
                if (!hashMap.containsKey(str)) {
                    this.options.put(str, value);
                }
            }
            this.options.put(key2, value);
        }
    }

    @JavascriptInterface
    public void setScreenOrientation(String orientation) {
        String str = Parameters.SCENE_MODE_LANDSCAPE;
        boolean equals = orientation.equals(str);
        String str2 = GameOptionName.ORIENTATION;
        if (equals) {
            this.options.put(str2, str);
        } else {
            this.options.put(str2, Parameters.SCENE_MODE_PORTRAIT);
        }
    }

    @JavascriptInterface
    public void init(String name) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("init: ");
        stringBuilder.append(name);
        Log.d(LOGTAG, stringBuilder.toString());
    }

    @JavascriptInterface
    public void start(String name) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("start: ");
        stringBuilder.append(name);
        Log.d(LOGTAG, stringBuilder.toString());
        if (!checkEgretContext()) {
            String egretRoot = new File(this.activity.getFilesDir(), EgretRuntimeLauncher.EGRET_ROOT).getAbsolutePath();
            this.options.put(GameOptionName.EGRET_ROOT, egretRoot);
            this.options.put(GameOptionName.LOADER_TYPE, "2");
            this.launcher = new EgretRuntimeLauncher(this.activity, egretRoot, MI_APPID, MI_APPKEY, 0);
            this.launcher.run(new EgretRuntimeDownloadListener() {
                public void onSuccess(Class<?> gameEngineClass) {
                    EgretLoader.this.startGame(gameEngineClass);
                }

                public void onError(String message) {
                }

                public void onProgressTotal(int progress, int length) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("progress: ");
                    stringBuilder.append(String.valueOf(progress));
                    stringBuilder.append("/");
                    stringBuilder.append(String.valueOf(length));
                    Log.d(EgretLoader.LOGTAG, stringBuilder.toString());
                }

                public void onProgress(String fileName, int progress, int length) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(fileName);
                    stringBuilder.append(": ");
                    stringBuilder.append(String.valueOf(progress));
                    stringBuilder.append("/");
                    stringBuilder.append(String.valueOf(length));
                    Log.d(EgretLoader.LOGTAG, stringBuilder.toString());
                }
            });
        }
    }

    private void startGame(Class<?> gameEngineClass) {
        try {
            this.gameEngine = gameEngineClass.newInstance();
            this.activity.runOnUiThread(new Runnable() {
                public void run() {
                    EgretLoader.this.startGameEngine();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startGameEngine() {
        callSetGameOptions();
        callInitRuntime();
        callSetRuntimeView();
    }

    private void callSetGameOptions() {
        HashMap<String, Object> optionSet = new HashMap();
        for (Entry<String, String> entry : this.options.entrySet()) {
            optionSet.put((String) entry.getKey(), entry.getValue());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((String) entry.getKey());
            stringBuilder.append(": ");
            stringBuilder.append((String) entry.getValue());
            Log.d(LOGTAG, stringBuilder.toString());
        }
        callGameEngineMethod("game_engine_set_options", new Class[]{HashMap.class}, new Object[]{optionSet});
    }

    private void callInitRuntime() {
        callGameEngineMethod("game_engine_init", new Class[]{Context.class}, new Object[]{this.activity});
    }

    private void callSetRuntimeView() {
        View view = (View) callGameEngineMethod("game_engine_get_view");
        if (view != null) {
            setScreenOrientation();
            this.activity.setContentView(view);
        }
    }

    private void setScreenOrientation() {
        HashMap hashMap = this.options;
        String str = GameOptionName.ORIENTATION;
        if (hashMap.containsKey(str) && ((String) this.options.get(str)).equals(Parameters.SCENE_MODE_LANDSCAPE)) {
            this.activity.setRequestedOrientation(0);
        } else {
            this.activity.setRequestedOrientation(1);
        }
    }

    public void onPause() {
        Log.d(LOGTAG, "onPause()");
        if (!checkEgretContext() && !checkEgretGameEngine()) {
            callGameEngineMethod("game_engine_onPause");
        }
    }

    public void onResume() {
        Log.d(LOGTAG, "onResume()");
        if (!checkEgretContext() && !checkEgretGameEngine()) {
            callGameEngineMethod("game_engine_onResume");
        }
    }

    public void onStop() {
        Log.d(LOGTAG, "stop()");
        if (!checkEgretContext() && !checkEgretGameEngine()) {
            callGameEngineMethod("game_engine_onStop");
            this.gameEngine = null;
        }
    }

    public void stopEgretRuntime() {
        onStop();
    }

    public boolean checkEgretContext() {
        if (this.activity != null) {
            return false;
        }
        Log.d(LOGTAG, "The context is not activity");
        return true;
    }

    private boolean checkEgretGameEngine() {
        if (this.gameEngine != null) {
            return false;
        }
        Log.d(LOGTAG, "Egret game engine is null");
        return true;
    }

    private Object callGameEngineMethod(String name) {
        return callGameEngineMethod(name, null, null);
    }

    private Object callGameEngineMethod(String name, Class<?>[] params, Object[] args) {
        Object obj = null;
        if (name != null) {
            Object obj2 = this.gameEngine;
            if (obj2 != null) {
                try {
                    obj = obj2.getClass().getDeclaredMethod(name, params).invoke(this.gameEngine, args);
                    return obj;
                } catch (Exception e) {
                    e.printStackTrace();
                    return obj;
                }
            }
        }
        return null;
    }
}
