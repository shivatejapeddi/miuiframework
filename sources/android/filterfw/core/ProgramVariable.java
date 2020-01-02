package android.filterfw.core;

public class ProgramVariable {
    private Program mProgram;
    private String mVarName;

    public ProgramVariable(Program program, String varName) {
        this.mProgram = program;
        this.mVarName = varName;
    }

    public Program getProgram() {
        return this.mProgram;
    }

    public String getVariableName() {
        return this.mVarName;
    }

    public void setValue(Object value) {
        Program program = this.mProgram;
        if (program != null) {
            program.setHostValue(this.mVarName, value);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Attempting to set program variable '");
        stringBuilder.append(this.mVarName);
        stringBuilder.append("' but the program is null!");
        throw new RuntimeException(stringBuilder.toString());
    }

    public Object getValue() {
        Program program = this.mProgram;
        if (program != null) {
            return program.getHostValue(this.mVarName);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Attempting to get program variable '");
        stringBuilder.append(this.mVarName);
        stringBuilder.append("' but the program is null!");
        throw new RuntimeException(stringBuilder.toString());
    }
}
