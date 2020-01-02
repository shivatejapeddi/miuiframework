package miui.maml.data;

public class IndexedVariable {
    protected int mIndex;
    private boolean mIsNumber;
    protected Variables mVars;

    public IndexedVariable(String name, Variables vars, boolean isNumber) {
        this.mIsNumber = isNumber;
        this.mIndex = this.mIsNumber ? vars.registerDoubleVariable(name) : vars.registerVariable(name);
        this.mVars = vars;
    }

    public final void set(double value) {
        this.mVars.put(this.mIndex, value);
    }

    public final boolean set(Object value) {
        if (this.mIsNumber) {
            return this.mVars.putDouble(this.mIndex, value);
        }
        this.mVars.put(this.mIndex, value);
        return true;
    }

    public final boolean setArr(int index, double value) {
        return this.mVars.putArr(this.mIndex, index, value);
    }

    public final boolean setArr(int index, Object value) {
        return this.mIsNumber ? this.mVars.putArrDouble(this.mIndex, index, value) : this.mVars.putArr(this.mIndex, index, value);
    }

    public final Object get() {
        return this.mVars.get(this.mIndex);
    }

    public final String getString() {
        return this.mVars.getString(this.mIndex);
    }

    public final Object getArr(int index) {
        return this.mVars.getArr(this.mIndex, index);
    }

    public final String getArrString(int index) {
        return this.mVars.getArrString(this.mIndex, index);
    }

    public final double getDouble() {
        return this.mVars.getDouble(this.mIndex);
    }

    public final double getArrDouble(int index) {
        return this.mVars.getArrDouble(this.mIndex, index);
    }

    public final boolean isNull() {
        if (this.mIsNumber) {
            if (!this.mVars.existsDouble(this.mIndex)) {
                return true;
            }
        } else if (this.mVars.get(this.mIndex) == null) {
            return true;
        }
        return false;
    }

    public final boolean isNull(int index) {
        if (this.mIsNumber) {
            if (!this.mVars.existsArrItem(this.mIndex, index)) {
                return true;
            }
        } else if (this.mVars.getArr(this.mIndex, index) == null) {
            return true;
        }
        return false;
    }

    public final int getVersion() {
        return this.mVars.getVer(this.mIndex, this.mIsNumber);
    }

    public final int getIndex() {
        return this.mIndex;
    }

    public final Variables getVariables() {
        return this.mVars;
    }
}
