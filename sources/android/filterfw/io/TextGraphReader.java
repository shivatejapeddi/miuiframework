package android.filterfw.io;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterFactory;
import android.filterfw.core.FilterGraph;
import android.filterfw.core.KeyValueMap;
import android.filterfw.core.ProtocolException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

public class TextGraphReader extends GraphReader {
    private KeyValueMap mBoundReferences;
    private ArrayList<Command> mCommands = new ArrayList();
    private Filter mCurrentFilter;
    private FilterGraph mCurrentGraph;
    private FilterFactory mFactory;
    private KeyValueMap mSettings;

    private interface Command {
        void execute(TextGraphReader textGraphReader) throws GraphIOException;
    }

    private class AddLibraryCommand implements Command {
        private String mLibraryName;

        public AddLibraryCommand(String libraryName) {
            this.mLibraryName = libraryName;
        }

        public void execute(TextGraphReader reader) {
            reader.mFactory;
            FilterFactory.addFilterLibrary(this.mLibraryName);
        }
    }

    private class AllocateFilterCommand implements Command {
        private String mClassName;
        private String mFilterName;

        public AllocateFilterCommand(String className, String filterName) {
            this.mClassName = className;
            this.mFilterName = filterName;
        }

        public void execute(TextGraphReader reader) throws GraphIOException {
            try {
                reader.mCurrentFilter = reader.mFactory.createFilterByClassName(this.mClassName, this.mFilterName);
            } catch (IllegalArgumentException e) {
                throw new GraphIOException(e.getMessage());
            }
        }
    }

    private class ConnectCommand implements Command {
        private String mSourceFilter;
        private String mSourcePort;
        private String mTargetFilter;
        private String mTargetName;

        public ConnectCommand(String sourceFilter, String sourcePort, String targetFilter, String targetName) {
            this.mSourceFilter = sourceFilter;
            this.mSourcePort = sourcePort;
            this.mTargetFilter = targetFilter;
            this.mTargetName = targetName;
        }

        public void execute(TextGraphReader reader) {
            reader.mCurrentGraph.connect(this.mSourceFilter, this.mSourcePort, this.mTargetFilter, this.mTargetName);
        }
    }

    private class ImportPackageCommand implements Command {
        private String mPackageName;

        public ImportPackageCommand(String packageName) {
            this.mPackageName = packageName;
        }

        public void execute(TextGraphReader reader) throws GraphIOException {
            try {
                reader.mFactory.addPackage(this.mPackageName);
            } catch (IllegalArgumentException e) {
                throw new GraphIOException(e.getMessage());
            }
        }
    }

    private class InitFilterCommand implements Command {
        private KeyValueMap mParams;

        public InitFilterCommand(KeyValueMap params) {
            this.mParams = params;
        }

        public void execute(TextGraphReader reader) throws GraphIOException {
            try {
                reader.mCurrentFilter.initWithValueMap(this.mParams);
                reader.mCurrentGraph.addFilter(TextGraphReader.this.mCurrentFilter);
            } catch (ProtocolException e) {
                throw new GraphIOException(e.getMessage());
            }
        }
    }

    public FilterGraph readGraphString(String graphString) throws GraphIOException {
        FilterGraph result = new FilterGraph();
        reset();
        this.mCurrentGraph = result;
        parseString(graphString);
        applySettings();
        executeCommands();
        reset();
        return result;
    }

    private void reset() {
        this.mCurrentGraph = null;
        this.mCurrentFilter = null;
        this.mCommands.clear();
        this.mBoundReferences = new KeyValueMap();
        this.mSettings = new KeyValueMap();
        this.mFactory = new FilterFactory();
    }

    private void parseString(String graphString) throws GraphIOException {
        Pattern semicolonPattern;
        Pattern packageNamePattern;
        String str;
        String curClassName;
        Pattern commandPattern = Pattern.compile("@[a-zA-Z]+");
        Pattern curlyClosePattern = Pattern.compile("\\}");
        Pattern curlyOpenPattern = Pattern.compile("\\{");
        Pattern ignorePattern = Pattern.compile("(\\s+|//[^\\n]*\\n)+");
        Pattern packageNamePattern2 = Pattern.compile("[a-zA-Z\\.]+");
        Pattern libraryNamePattern = Pattern.compile("[a-zA-Z\\./:]+");
        Pattern portPattern = Pattern.compile("\\[[a-zA-Z0-9\\-_]+\\]");
        String str2 = "=>";
        Pattern rightArrowPattern = Pattern.compile(str2);
        String str3 = ";";
        Pattern semicolonPattern2 = Pattern.compile(str3);
        Pattern wordPattern = Pattern.compile("[a-zA-Z0-9\\-_]+");
        int state = 0;
        PatternScanner scanner = new PatternScanner(graphString, ignorePattern);
        String curSourceFilterName = null;
        String curSourcePortName = null;
        String curTargetFilterName = null;
        String curClassName2 = null;
        while (!scanner.atEnd()) {
            String str4;
            PatternScanner scanner2;
            Pattern libraryNamePattern2;
            int i;
            Pattern commandPattern2;
            Pattern packageNamePattern3;
            Pattern libraryNamePattern3;
            String curClassName3;
            int state2;
            switch (state) {
                case 0:
                    i = state;
                    semicolonPattern = semicolonPattern2;
                    str4 = str3;
                    commandPattern2 = commandPattern;
                    packageNamePattern = packageNamePattern2;
                    scanner2 = scanner;
                    libraryNamePattern2 = libraryNamePattern;
                    str = curClassName2;
                    ignorePattern = wordPattern;
                    curClassName = str;
                    semicolonPattern2 = commandPattern2;
                    state = scanner2.eat(semicolonPattern2, "<command>");
                    if (state.equals("@import")) {
                        state = 1;
                        break;
                    } else if (state.equals("@library")) {
                        state = 2;
                        break;
                    } else if (state.equals("@filter")) {
                        state = 3;
                        break;
                    } else if (state.equals("@connect")) {
                        state = 8;
                        break;
                    } else if (state.equals("@set")) {
                        state = 13;
                        break;
                    } else if (state.equals("@external")) {
                        state = 14;
                        break;
                    } else if (state.equals("@setting")) {
                        state = 15;
                        break;
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unknown command '");
                        stringBuilder.append(state);
                        stringBuilder.append("'!");
                        throw new GraphIOException(stringBuilder.toString());
                    }
                case 1:
                    i = state;
                    semicolonPattern = semicolonPattern2;
                    str4 = str3;
                    commandPattern2 = commandPattern;
                    scanner2 = scanner;
                    libraryNamePattern2 = libraryNamePattern;
                    str = curClassName2;
                    ignorePattern = wordPattern;
                    curClassName = str;
                    packageNamePattern = packageNamePattern2;
                    this.mCommands.add(new ImportPackageCommand(scanner2.eat(packageNamePattern, "<package-name>")));
                    state = 16;
                    semicolonPattern2 = commandPattern2;
                    break;
                case 2:
                    i = state;
                    semicolonPattern = semicolonPattern2;
                    str4 = str3;
                    commandPattern2 = commandPattern;
                    packageNamePattern3 = packageNamePattern2;
                    scanner2 = scanner;
                    str = curClassName2;
                    ignorePattern = wordPattern;
                    curClassName = str;
                    libraryNamePattern2 = libraryNamePattern;
                    this.mCommands.add(new AddLibraryCommand(scanner2.eat(libraryNamePattern2, "<library-name>")));
                    state = 16;
                    semicolonPattern2 = commandPattern2;
                    packageNamePattern = packageNamePattern3;
                    break;
                case 3:
                    i = state;
                    semicolonPattern = semicolonPattern2;
                    str4 = str3;
                    commandPattern2 = commandPattern;
                    packageNamePattern3 = packageNamePattern2;
                    libraryNamePattern3 = libraryNamePattern;
                    scanner2 = scanner;
                    str = curClassName2;
                    ignorePattern = wordPattern;
                    curClassName = str;
                    curClassName = scanner2.eat(ignorePattern, "<class-name>");
                    state = 4;
                    semicolonPattern2 = commandPattern2;
                    packageNamePattern = packageNamePattern3;
                    libraryNamePattern2 = libraryNamePattern3;
                    break;
                case 4:
                    i = state;
                    semicolonPattern = semicolonPattern2;
                    str4 = str3;
                    commandPattern2 = commandPattern;
                    curClassName3 = curClassName2;
                    packageNamePattern3 = packageNamePattern2;
                    libraryNamePattern3 = libraryNamePattern;
                    scanner2 = scanner;
                    ignorePattern = wordPattern;
                    curClassName = curClassName3;
                    this.mCommands.add(new AllocateFilterCommand(curClassName, scanner2.eat(ignorePattern, "<filter-name>")));
                    state = 5;
                    semicolonPattern2 = commandPattern2;
                    packageNamePattern = packageNamePattern3;
                    libraryNamePattern2 = libraryNamePattern3;
                    break;
                case 5:
                    i = state;
                    semicolonPattern = semicolonPattern2;
                    str4 = str3;
                    commandPattern2 = commandPattern;
                    curClassName3 = curClassName2;
                    packageNamePattern3 = packageNamePattern2;
                    libraryNamePattern3 = libraryNamePattern;
                    scanner2 = scanner;
                    ignorePattern = wordPattern;
                    scanner2.eat(curlyOpenPattern, "{");
                    state = 6;
                    semicolonPattern2 = commandPattern2;
                    packageNamePattern = packageNamePattern3;
                    libraryNamePattern2 = libraryNamePattern3;
                    curClassName = curClassName3;
                    break;
                case 6:
                    i = state;
                    semicolonPattern = semicolonPattern2;
                    str4 = str3;
                    commandPattern2 = commandPattern;
                    curClassName3 = curClassName2;
                    packageNamePattern3 = packageNamePattern2;
                    libraryNamePattern3 = libraryNamePattern;
                    scanner2 = scanner;
                    ignorePattern = wordPattern;
                    this.mCommands.add(new InitFilterCommand(readKeyValueAssignments(scanner2, curlyClosePattern)));
                    state = 7;
                    semicolonPattern2 = commandPattern2;
                    packageNamePattern = packageNamePattern3;
                    libraryNamePattern2 = libraryNamePattern3;
                    curClassName = curClassName3;
                    break;
                case 7:
                    i = state;
                    semicolonPattern = semicolonPattern2;
                    str4 = str3;
                    commandPattern2 = commandPattern;
                    curClassName3 = curClassName2;
                    packageNamePattern3 = packageNamePattern2;
                    libraryNamePattern3 = libraryNamePattern;
                    scanner2 = scanner;
                    ignorePattern = wordPattern;
                    scanner2.eat(curlyClosePattern, "}");
                    state = 0;
                    semicolonPattern2 = commandPattern2;
                    packageNamePattern = packageNamePattern3;
                    libraryNamePattern2 = libraryNamePattern3;
                    curClassName = curClassName3;
                    break;
                case 8:
                    i = state;
                    semicolonPattern = semicolonPattern2;
                    str4 = str3;
                    commandPattern2 = commandPattern;
                    curClassName3 = curClassName2;
                    packageNamePattern3 = packageNamePattern2;
                    libraryNamePattern3 = libraryNamePattern;
                    scanner2 = scanner;
                    ignorePattern = wordPattern;
                    curSourceFilterName = scanner2.eat(ignorePattern, "<source-filter-name>");
                    state = 9;
                    semicolonPattern2 = commandPattern2;
                    packageNamePattern = packageNamePattern3;
                    libraryNamePattern2 = libraryNamePattern3;
                    curClassName = curClassName3;
                    break;
                case 9:
                    i = state;
                    semicolonPattern = semicolonPattern2;
                    str4 = str3;
                    commandPattern2 = commandPattern;
                    curClassName3 = curClassName2;
                    packageNamePattern3 = packageNamePattern2;
                    libraryNamePattern3 = libraryNamePattern;
                    scanner2 = scanner;
                    ignorePattern = wordPattern;
                    state = scanner2.eat(portPattern, "[<source-port-name>]");
                    curSourcePortName = state.substring(1, state.length() - 1);
                    state = 10;
                    semicolonPattern2 = commandPattern2;
                    packageNamePattern = packageNamePattern3;
                    libraryNamePattern2 = libraryNamePattern3;
                    curClassName = curClassName3;
                    break;
                case 10:
                    i = state;
                    semicolonPattern = semicolonPattern2;
                    str4 = str3;
                    commandPattern2 = commandPattern;
                    curClassName3 = curClassName2;
                    packageNamePattern3 = packageNamePattern2;
                    libraryNamePattern3 = libraryNamePattern;
                    scanner2 = scanner;
                    ignorePattern = wordPattern;
                    scanner2.eat(rightArrowPattern, str2);
                    state = 11;
                    semicolonPattern2 = commandPattern2;
                    packageNamePattern = packageNamePattern3;
                    libraryNamePattern2 = libraryNamePattern3;
                    curClassName = curClassName3;
                    break;
                case 11:
                    i = state;
                    semicolonPattern = semicolonPattern2;
                    str4 = str3;
                    commandPattern2 = commandPattern;
                    curClassName3 = curClassName2;
                    packageNamePattern3 = packageNamePattern2;
                    libraryNamePattern3 = libraryNamePattern;
                    scanner2 = scanner;
                    ignorePattern = wordPattern;
                    curTargetFilterName = scanner2.eat(ignorePattern, "<target-filter-name>");
                    state = 12;
                    semicolonPattern2 = commandPattern2;
                    packageNamePattern = packageNamePattern3;
                    libraryNamePattern2 = libraryNamePattern3;
                    curClassName = curClassName3;
                    break;
                case 12:
                    state2 = state;
                    state = scanner.eat(portPattern, "[<target-port-name>]");
                    PatternScanner scanner3 = scanner;
                    String curTargetPortName = state.substring(1, state.length() - 1);
                    i = state2;
                    String portString = state;
                    commandPattern2 = commandPattern;
                    scanner2 = scanner3;
                    packageNamePattern3 = packageNamePattern2;
                    ConnectCommand connectCommand = state;
                    libraryNamePattern3 = libraryNamePattern;
                    ArrayList arrayList = this.mCommands;
                    curClassName3 = curClassName2;
                    ignorePattern = wordPattern;
                    semicolonPattern = semicolonPattern2;
                    str4 = str3;
                    state = new ConnectCommand(curSourceFilterName, curSourcePortName, curTargetFilterName, curTargetPortName);
                    arrayList.add(connectCommand);
                    state = 16;
                    semicolonPattern2 = commandPattern2;
                    packageNamePattern = packageNamePattern3;
                    libraryNamePattern2 = libraryNamePattern3;
                    curClassName = curClassName3;
                    break;
                case 13:
                    state2 = state;
                    this.mBoundReferences.putAll(readKeyValueAssignments(scanner, semicolonPattern2));
                    state = 16;
                    semicolonPattern = semicolonPattern2;
                    str4 = str3;
                    semicolonPattern2 = commandPattern;
                    packageNamePattern = packageNamePattern2;
                    scanner2 = scanner;
                    libraryNamePattern2 = libraryNamePattern;
                    str = curClassName2;
                    ignorePattern = wordPattern;
                    curClassName = str;
                    break;
                case 14:
                    state2 = state;
                    bindExternal(scanner.eat(wordPattern, "<external-identifier>"));
                    state = 16;
                    semicolonPattern = semicolonPattern2;
                    str4 = str3;
                    semicolonPattern2 = commandPattern;
                    packageNamePattern = packageNamePattern2;
                    scanner2 = scanner;
                    libraryNamePattern2 = libraryNamePattern;
                    str = curClassName2;
                    ignorePattern = wordPattern;
                    curClassName = str;
                    break;
                case 15:
                    this.mSettings.putAll(readKeyValueAssignments(scanner, semicolonPattern2));
                    state = 16;
                    semicolonPattern = semicolonPattern2;
                    str4 = str3;
                    semicolonPattern2 = commandPattern;
                    packageNamePattern = packageNamePattern2;
                    scanner2 = scanner;
                    libraryNamePattern2 = libraryNamePattern;
                    str = curClassName2;
                    ignorePattern = wordPattern;
                    curClassName = str;
                    break;
                case 16:
                    scanner.eat(semicolonPattern2, str3);
                    state = 0;
                    semicolonPattern = semicolonPattern2;
                    str4 = str3;
                    semicolonPattern2 = commandPattern;
                    packageNamePattern = packageNamePattern2;
                    scanner2 = scanner;
                    libraryNamePattern2 = libraryNamePattern;
                    str = curClassName2;
                    ignorePattern = wordPattern;
                    curClassName = str;
                    break;
                default:
                    semicolonPattern = semicolonPattern2;
                    str4 = str3;
                    semicolonPattern2 = commandPattern;
                    packageNamePattern = packageNamePattern2;
                    scanner2 = scanner;
                    libraryNamePattern2 = libraryNamePattern;
                    str = curClassName2;
                    ignorePattern = wordPattern;
                    curClassName = str;
                    break;
            }
            libraryNamePattern = libraryNamePattern2;
            packageNamePattern2 = packageNamePattern;
            scanner = scanner2;
            str3 = str4;
            String packageNamePattern4 = graphString;
            commandPattern = semicolonPattern2;
            semicolonPattern2 = semicolonPattern;
            Pattern pattern = ignorePattern;
            curClassName2 = curClassName;
            wordPattern = pattern;
        }
        semicolonPattern = semicolonPattern2;
        packageNamePattern = packageNamePattern2;
        str = curClassName2;
        curClassName = str;
        int state3 = state;
        if (state3 != 16 && state3 != 0) {
            throw new GraphIOException("Unexpected end of input!");
        }
    }

    public KeyValueMap readKeyValueAssignments(String assignments) throws GraphIOException {
        return readKeyValueAssignments(new PatternScanner(assignments, Pattern.compile("\\s+")), null);
    }

    private KeyValueMap readKeyValueAssignments(PatternScanner scanner, Pattern endPattern) throws GraphIOException {
        KeyValueMap newVals;
        PatternScanner patternScanner = scanner;
        int STATE_EQUALS = 1;
        int STATE_VALUE = 2;
        int STATE_POST_VALUE = 3;
        String str = "=";
        Pattern equalsPattern = Pattern.compile(str);
        String str2 = ";";
        Pattern semicolonPattern = Pattern.compile(str2);
        Pattern wordPattern = Pattern.compile("[a-zA-Z]+[a-zA-Z0-9]*");
        Pattern stringPattern = Pattern.compile("'[^']*'|\\\"[^\\\"]*\\\"");
        Pattern intPattern = Pattern.compile("[0-9]+");
        Pattern floatPattern = Pattern.compile("[0-9]*\\.[0-9]+f?");
        Pattern referencePattern = Pattern.compile("\\$[a-zA-Z]+[a-zA-Z0-9]");
        Pattern booleanPattern = Pattern.compile("true|false");
        int state = 0;
        KeyValueMap newVals2 = new KeyValueMap();
        int STATE_IDENTIFIER = 0;
        String curKey = null;
        while (true) {
            int STATE_EQUALS2 = STATE_EQUALS;
            int i;
            if (!scanner.atEnd()) {
                int i2;
                if (endPattern != null && scanner.peek(endPattern)) {
                    i2 = STATE_VALUE;
                    i = STATE_POST_VALUE;
                    newVals = newVals2;
                    break;
                }
                String str3;
                if (state == 0) {
                    i2 = STATE_VALUE;
                    i = STATE_POST_VALUE;
                    newVals = newVals2;
                    str3 = str2;
                    curKey = patternScanner.eat(wordPattern, "<identifier>");
                    state = 1;
                } else if (state == 1) {
                    i2 = STATE_VALUE;
                    i = STATE_POST_VALUE;
                    newVals = newVals2;
                    str3 = str2;
                    patternScanner.eat(equalsPattern, str);
                    state = 2;
                } else if (state == 2) {
                    String tryEat = patternScanner.tryEat(stringPattern);
                    String curValue = tryEat;
                    if (tryEat != null) {
                        i2 = STATE_VALUE;
                        i = STATE_POST_VALUE;
                        STATE_VALUE = newVals2;
                        STATE_VALUE.put(curKey, curValue.substring(1, curValue.length() - 1));
                        newVals = STATE_VALUE;
                        str3 = str2;
                    } else {
                        i2 = STATE_VALUE;
                        i = STATE_POST_VALUE;
                        newVals = newVals2;
                        str3 = patternScanner.tryEat(referencePattern);
                        String curValue2 = str3;
                        if (str3 != null) {
                            str3 = str2;
                            STATE_VALUE = curValue2.substring(1, curValue2.length());
                            curValue = curValue2;
                            curValue2 = this.mBoundReferences;
                            if (curValue2 != null) {
                                curValue2 = curValue2.get(STATE_VALUE);
                            } else {
                                curValue2 = null;
                            }
                            if (curValue2 != null) {
                                newVals.put(curKey, curValue2);
                            } else {
                                String referencedObject = curValue2;
                                curValue2 = new StringBuilder();
                                curValue2.append("Unknown object reference to '");
                                curValue2.append(STATE_VALUE);
                                curValue2.append("'!");
                                throw new GraphIOException(curValue2.toString());
                            }
                        }
                        str3 = str2;
                        STATE_VALUE = patternScanner.tryEat(booleanPattern);
                        curValue = STATE_VALUE;
                        if (STATE_VALUE != 0) {
                            newVals.put(curKey, Boolean.valueOf(Boolean.parseBoolean(curValue)));
                        } else {
                            STATE_VALUE = patternScanner.tryEat(floatPattern);
                            curValue = STATE_VALUE;
                            if (STATE_VALUE != 0) {
                                newVals.put(curKey, Float.valueOf(Float.parseFloat(curValue)));
                            } else {
                                STATE_VALUE = patternScanner.tryEat(intPattern);
                                curValue = STATE_VALUE;
                                if (STATE_VALUE != 0) {
                                    newVals.put(curKey, Integer.valueOf(Integer.parseInt(curValue)));
                                } else {
                                    throw new GraphIOException(patternScanner.unexpectedTokenMessage("<value>"));
                                }
                            }
                        }
                    }
                    state = 3;
                } else if (state != 3) {
                    i2 = STATE_VALUE;
                    i = STATE_POST_VALUE;
                    newVals = newVals2;
                    str3 = str2;
                } else {
                    patternScanner.eat(semicolonPattern, str2);
                    state = 0;
                    i2 = STATE_VALUE;
                    i = STATE_POST_VALUE;
                    newVals = newVals2;
                    str3 = str2;
                }
                str2 = str3;
                STATE_POST_VALUE = i;
                STATE_VALUE = i2;
                newVals2 = newVals;
                STATE_EQUALS = STATE_EQUALS2;
            } else {
                i = STATE_POST_VALUE;
                newVals = newVals2;
                break;
            }
        }
        if (state == 0 || state == 3) {
            return newVals;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unexpected end of assignments on line ");
        stringBuilder.append(scanner.lineNo());
        stringBuilder.append("!");
        throw new GraphIOException(stringBuilder.toString());
    }

    private void bindExternal(String name) throws GraphIOException {
        if (this.mReferences.containsKey(name)) {
            this.mBoundReferences.put(name, this.mReferences.get(name));
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown external variable '");
        stringBuilder.append(name);
        stringBuilder.append("'! You must add a reference to this external in the host program using addReference(...)!");
        throw new GraphIOException(stringBuilder.toString());
    }

    private void checkReferences() throws GraphIOException {
        for (String reference : this.mReferences.keySet()) {
            if (!this.mBoundReferences.containsKey(reference)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Host program specifies reference to '");
                stringBuilder.append(reference);
                stringBuilder.append("', which is not declared @external in graph file!");
                throw new GraphIOException(stringBuilder.toString());
            }
        }
    }

    private void applySettings() throws GraphIOException {
        for (String setting : this.mSettings.keySet()) {
            Object value = this.mSettings.get(setting);
            StringBuilder stringBuilder;
            if (setting.equals("autoBranch")) {
                expectSettingClass(setting, value, String.class);
                if (value.equals("synced")) {
                    this.mCurrentGraph.setAutoBranchMode(1);
                } else if (value.equals("unsynced")) {
                    this.mCurrentGraph.setAutoBranchMode(2);
                } else if (value.equals("off")) {
                    this.mCurrentGraph.setAutoBranchMode(0);
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Unknown autobranch setting: ");
                    stringBuilder.append(value);
                    stringBuilder.append("!");
                    throw new GraphIOException(stringBuilder.toString());
                }
            } else if (setting.equals("discardUnconnectedOutputs")) {
                expectSettingClass(setting, value, Boolean.class);
                this.mCurrentGraph.setDiscardUnconnectedOutputs(((Boolean) value).booleanValue());
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown @setting '");
                stringBuilder.append(setting);
                stringBuilder.append("'!");
                throw new GraphIOException(stringBuilder.toString());
            }
        }
    }

    private void expectSettingClass(String setting, Object value, Class expectedClass) throws GraphIOException {
        if (value.getClass() != expectedClass) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Setting '");
            stringBuilder.append(setting);
            stringBuilder.append("' must have a value of type ");
            stringBuilder.append(expectedClass.getSimpleName());
            stringBuilder.append(", but found a value of type ");
            stringBuilder.append(value.getClass().getSimpleName());
            stringBuilder.append("!");
            throw new GraphIOException(stringBuilder.toString());
        }
    }

    private void executeCommands() throws GraphIOException {
        Iterator it = this.mCommands.iterator();
        while (it.hasNext()) {
            ((Command) it.next()).execute(this);
        }
    }
}
