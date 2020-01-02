package miui.provider;

public class ExtraGuardVirusInfoEntity {
    private int mAdvice = 1;
    private String mDescription = null;
    private String mEngineName = null;
    private String mPackageName = null;
    private int mType = 1;
    private String mVirusName = null;

    public int getType() {
        return this.mType;
    }

    public void setType(int _type) {
        this.mType = _type;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public void setDescription(String _description) {
        this.mDescription = _description;
    }

    public int getAdvice() {
        return this.mAdvice;
    }

    public void setAdvice(int _advice) {
        this.mAdvice = _advice;
    }

    public String getVirusName() {
        return this.mVirusName;
    }

    public void setVirusName(String _virusName) {
        this.mVirusName = _virusName;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public void setPackageName(String _packageName) {
        this.mPackageName = _packageName;
    }

    public String getEngineName() {
        return this.mEngineName;
    }

    public void setEngineName(String _engineName) {
        this.mEngineName = _engineName;
    }
}
