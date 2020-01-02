package android.gamepad;

import android.content.Context;
import android.os.RemoteException;
import android.util.ArrayMap;

public class BsGamePadManager {
    final Context mContext;
    private final ArrayMap<Integer, BsGameKeyMap> mGamePadMapper = new ArrayMap();
    final IBsGamePadService mService;

    public BsGamePadManager(Context context, IBsGamePadService service) {
        this.mContext = context;
        this.mService = service;
    }

    public void addKeyMap(int touchMask, float x, float y, float r) {
        synchronized (this.mGamePadMapper) {
            int index = this.mGamePadMapper.indexOfKey(Integer.valueOf(touchMask));
            if (index >= 0) {
                this.mGamePadMapper.setValueAt(index, new BsGameKeyMap(x, y, r));
            } else {
                this.mGamePadMapper.put(Integer.valueOf(touchMask), new BsGameKeyMap(x, y, r));
            }
        }
    }

    public void setAppSwitch(boolean enable) {
        try {
            this.mService.setAppSwitch(enable);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void loadKeyMap(boolean isChooseMove, int rotation) {
        try {
            this.mService.loadKeyMap(this.mGamePadMapper, isChooseMove, rotation);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void unloadKeyMap() {
        this.mGamePadMapper.clear();
        try {
            this.mService.unloadKeyMap();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
