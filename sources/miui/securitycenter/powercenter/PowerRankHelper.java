package miui.securitycenter.powercenter;

import android.content.Context;
import android.os.Bundle;
import android.os.UserManager;
import com.android.internal.os.BatterySipper;
import com.android.internal.os.BatterySipper.DrainType;
import com.android.internal.os.BatteryStatsHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PowerRankHelper {
    private static final boolean DEBUG = false;
    private static final String TAG = "PowerRankHelper";
    private final List<BatterySipper> mAppUsageList = new ArrayList();
    private Context mContext;
    private BatteryStatsHelper mHelper;
    private double mMaxPower = 1.0d;
    private double mMiscPower;
    private final List<BatterySipper> mMiscUsageList = new ArrayList();
    private final List<BatterySipper> mSystemAppUsageList = new ArrayList();
    private double mTotalPower;
    private UserManager mUm;

    public PowerRankHelper(Context context) {
        this.mHelper = new BatteryStatsHelper(context);
        this.mHelper.create((Bundle) null);
        this.mUm = (UserManager) context.getSystemService("user");
        this.mContext = context;
    }

    public void refreshStats() {
        this.mHelper.clearStats();
        this.mMaxPower = 0.0d;
        this.mTotalPower = 0.0d;
        this.mMiscPower = 0.0d;
        this.mAppUsageList.clear();
        this.mMiscUsageList.clear();
        this.mSystemAppUsageList.clear();
        this.mHelper.refreshStats(0, -1);
        try {
            for (BatterySipper osSipper : (List) this.mHelper.getClass().getDeclaredMethod("getSystemAppUsageList", new Class[0]).invoke(this.mHelper, new Object[0])) {
                if (osSipper.drainType == DrainType.APP) {
                    this.mSystemAppUsageList.add(BatterySipperHelper.makeBatterySipperForSystemApp(this.mContext, osSipper));
                }
            }
        } catch (Exception e) {
        }
        List<BatterySipper> usageList = this.mHelper.getUsageList();
        BatterySipper otherSipper = BatterySipperHelper.makeBatterySipper(this.mContext, 10, null);
        for (BatterySipper osSipper2 : usageList) {
            if (osSipper2.drainType == DrainType.APP) {
                BatterySipper sipper = BatterySipperHelper.makeBatterySipper(this.mContext, 6, osSipper2);
                this.mTotalPower += sipper.value;
                this.mAppUsageList.add(sipper);
            } else if (osSipper2.drainType == DrainType.PHONE) {
                addEntry(BatterySipperHelper.makeBatterySipper(this.mContext, 2, osSipper2));
            } else if (osSipper2.drainType == DrainType.SCREEN) {
                addEntry(BatterySipperHelper.makeBatterySipper(this.mContext, 5, osSipper2));
            } else if (osSipper2.drainType == DrainType.WIFI) {
                addEntry(BatterySipperHelper.makeBatterySipper(this.mContext, 3, osSipper2));
            } else if (osSipper2.drainType == DrainType.BLUETOOTH) {
                addEntry(BatterySipperHelper.makeBatterySipper(this.mContext, 4, osSipper2));
            } else if (osSipper2.drainType == DrainType.IDLE) {
                addEntry(BatterySipperHelper.makeBatterySipper(this.mContext, 0, osSipper2));
            } else if (osSipper2.drainType == DrainType.CELL) {
                addEntry(BatterySipperHelper.makeBatterySipper(this.mContext, 1, osSipper2));
            } else if (osSipper2.drainType == DrainType.AMBIENT_DISPLAY) {
                addEntry(BatterySipperHelper.makeBatterySipper(this.mContext, 11, osSipper2));
            } else {
                BatterySipperHelper.addBatterySipper(otherSipper, osSipper2);
            }
        }
        if (otherSipper.usageTime > 0 && otherSipper.value > 0.0d) {
            addEntry(otherSipper);
        }
        if (this.mAppUsageList.size() >= 2) {
            Collections.sort(this.mAppUsageList);
        }
        if (this.mMiscUsageList.size() >= 2) {
            Collections.sort(this.mMiscUsageList);
        }
    }

    private void addEntry(BatterySipper sipper) {
        if (sipper.value > this.mMaxPower) {
            this.mMaxPower = sipper.value;
        }
        this.mTotalPower += sipper.value;
        this.mMiscPower += sipper.value;
        this.mMiscUsageList.add(sipper);
    }

    public List<BatterySipper> getAppUsageList() {
        return this.mAppUsageList;
    }

    public List<BatterySipper> getMiscUsageList() {
        return this.mMiscUsageList;
    }

    public List<BatterySipper> getSystemAppUsageList() {
        return this.mSystemAppUsageList;
    }

    public double getMiscUsageTotal() {
        return this.mMiscPower;
    }

    public double getUsageTotal() {
        return this.mTotalPower;
    }
}
