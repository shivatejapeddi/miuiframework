package miui.maml.data;

import java.util.HashSet;
import miui.maml.data.Expression.FunctionExpression;

public class RootExpression extends Expression {
    public static final String LOG_TAG = "RootExression";
    private boolean mAlwaysEvaluate;
    private double mDoubleValue;
    private Expression mExp;
    private boolean mIsNumInit = false;
    private boolean mIsStrInit = false;
    private String mStringValue;
    private VarVersionVisitor mVarVersionVisitor = null;
    private Variables mVars;
    private HashSet<VarVersion> mVersionSet = new HashSet();
    private VarVersion[] mVersions;

    public static class VarVersion {
        int mIndex;
        private boolean mIsNumber;
        int mVersion;

        public VarVersion(int index, int version, boolean isNumber) {
            this.mIndex = index;
            this.mVersion = version;
            this.mIsNumber = isNumber;
        }

        public int hashCode() {
            return this.mIsNumber ? this.mIndex : (-this.mIndex) - 1;
        }

        public boolean equals(Object version) {
            boolean z = false;
            if (!(version instanceof VarVersion)) {
                return false;
            }
            VarVersion tempVersion = (VarVersion) version;
            if (tempVersion.mIsNumber == this.mIsNumber && tempVersion.mIndex == this.mIndex) {
                z = true;
            }
            return z;
        }

        public int getVer(Variables vars) {
            return vars.getVer(this.mIndex, this.mIsNumber);
        }
    }

    private static class VarVersionVisitor extends ExpressionVisitor {
        private RootExpression mRoot;

        public VarVersionVisitor(RootExpression root) {
            this.mRoot = root;
        }

        public void visit(Expression exp) {
            if (exp instanceof VariableExpression) {
                VariableExpression ep = (VariableExpression) exp;
                ep.evaluate();
                this.mRoot.addVarVersion(new VarVersion(ep.getIndex(), ep.getVersion(), exp instanceof NumberVariableExpression));
            } else if (exp instanceof FunctionExpression) {
                String func = ((FunctionExpression) exp).getFunName();
                if ("rand".equals(func) || "eval".equals(func) || "preciseeval".equals(func)) {
                    this.mRoot.mAlwaysEvaluate = true;
                }
            }
        }
    }

    public RootExpression(Variables vars, Expression exp) {
        this.mVars = vars;
        this.mExp = exp;
    }

    public void addVarVersion(VarVersion version) {
        this.mVersionSet.add(version);
    }

    public double evaluate() {
        if (this.mIsNumInit) {
            boolean isChange = false;
            if (this.mAlwaysEvaluate) {
                isChange = true;
            } else if (this.mVersions != null) {
                int i = 0;
                while (true) {
                    VarVersion version = this.mVersions;
                    if (i >= version.length) {
                        break;
                    }
                    version = version[i];
                    if (version != null) {
                        int newVersion = version.getVer(this.mVars);
                        if (version.mVersion != newVersion) {
                            isChange = true;
                            version.mVersion = newVersion;
                        }
                    }
                    i++;
                }
            }
            if (isChange) {
                this.mDoubleValue = this.mExp.evaluate();
            }
        } else {
            this.mDoubleValue = this.mExp.evaluate();
            if (this.mVarVersionVisitor == null) {
                this.mVarVersionVisitor = new VarVersionVisitor(this);
                this.mExp.accept(this.mVarVersionVisitor);
                if (this.mVersionSet.size() <= 0) {
                    this.mVersions = null;
                } else {
                    this.mVersions = new VarVersion[this.mVersionSet.size()];
                    this.mVersionSet.toArray(this.mVersions);
                }
            }
            this.mIsNumInit = true;
        }
        return this.mDoubleValue;
    }

    public String evaluateStr() {
        if (this.mIsStrInit) {
            boolean isChange = false;
            if (this.mAlwaysEvaluate) {
                isChange = true;
            } else if (this.mVersions != null) {
                int i = 0;
                while (true) {
                    VarVersion version = this.mVersions;
                    if (i >= version.length) {
                        break;
                    }
                    version = version[i];
                    if (version != null) {
                        int newVersion = version.getVer(this.mVars);
                        if (version.mVersion != newVersion) {
                            isChange = true;
                            version.mVersion = newVersion;
                        }
                    }
                    i++;
                }
            }
            if (isChange) {
                this.mStringValue = this.mExp.evaluateStr();
            }
        } else {
            this.mStringValue = this.mExp.evaluateStr();
            if (this.mVarVersionVisitor == null) {
                this.mVarVersionVisitor = new VarVersionVisitor(this);
                this.mExp.accept(this.mVarVersionVisitor);
                this.mVersions = new VarVersion[this.mVersionSet.size()];
                this.mVersionSet.toArray(this.mVersions);
            }
            this.mIsStrInit = true;
        }
        return this.mStringValue;
    }

    public boolean isNull() {
        return this.mExp.isNull();
    }

    public void accept(ExpressionVisitor v) {
    }
}
