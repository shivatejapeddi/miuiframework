package com.miui.joyose;

import android.content.Context;

public abstract class JoyoseManager {
    public static final String EXTRA_CUR_PLACE = "cur_place";
    public static final String EXTRA_CUR_STATE = "cur_state";
    public static final String EXTRA_PRE_STATE = "pre_state";
    public static final long SCENE_IN_CYCLE_MODE = 8;
    public static final long SCENE_IN_HOME_MODE = 2;
    public static final long SCENE_IN_UNIT_MODE = 4;
    public static final long SCENE_SLEEP_MODE = 1;
    public static final String SLEEP_MODE_CHANGE_ACTION = "action_sleep_state_changed";
    public static final int SLEEP_MODE_STATE_DEEP_SLEEP = 3;
    public static final int SLEEP_MODE_STATE_LIGHT_EXIT_SLEEP = 4;
    public static final int SLEEP_MODE_STATE_LIGHT_SLEEP = 2;
    public static final int SLEEP_MODE_STATE_NO_SLEEP = 1;
    public static final int SLEEP_MODE_STATE_UNKNOW = 0;
    public static final int USER_IN_HOME = 1;
    public static final int USER_IN_UNIT = 2;
    public static final String USER_PLACE_CHANGE_ACTION = "action_user_place_changed";
    public static final int USER_UNCERTAIN_IN_HOME = 4;
    public static final int USER_UNCERTAIN_IN_UNIT = 8;
    public static final int USER_UNKNOW_PLACE = 0;

    public long getJoyoseSupportSceneList(Context context) {
        return JoyoseProxy.getJoyoseSupportSceneList(context);
    }

    public boolean checkSceneWorkState(long scene) {
        return JoyoseProxy.checkSceneWorkState(scene);
    }
}
