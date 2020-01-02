package android.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.apache.miui.commons.lang3.CharUtils;

public final class JsonWriter implements Closeable {
    private String indent;
    private boolean lenient;
    private final Writer out;
    private String separator;
    private final List<JsonScope> stack = new ArrayList();

    /* renamed from: android.util.JsonWriter$1 */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$util$JsonScope = new int[JsonScope.values().length];

        static {
            try {
                $SwitchMap$android$util$JsonScope[JsonScope.EMPTY_DOCUMENT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$util$JsonScope[JsonScope.EMPTY_ARRAY.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$util$JsonScope[JsonScope.NONEMPTY_ARRAY.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$util$JsonScope[JsonScope.DANGLING_NAME.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$android$util$JsonScope[JsonScope.NONEMPTY_DOCUMENT.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public JsonWriter(Writer out) {
        this.stack.add(JsonScope.EMPTY_DOCUMENT);
        this.separator = ":";
        if (out != null) {
            this.out = out;
            return;
        }
        throw new NullPointerException("out == null");
    }

    public void setIndent(String indent) {
        if (indent.isEmpty()) {
            this.indent = null;
            this.separator = ":";
            return;
        }
        this.indent = indent;
        this.separator = ": ";
    }

    public void setLenient(boolean lenient) {
        this.lenient = lenient;
    }

    public boolean isLenient() {
        return this.lenient;
    }

    public JsonWriter beginArray() throws IOException {
        return open(JsonScope.EMPTY_ARRAY, "[");
    }

    public JsonWriter endArray() throws IOException {
        return close(JsonScope.EMPTY_ARRAY, JsonScope.NONEMPTY_ARRAY, "]");
    }

    public JsonWriter beginObject() throws IOException {
        return open(JsonScope.EMPTY_OBJECT, "{");
    }

    public JsonWriter endObject() throws IOException {
        return close(JsonScope.EMPTY_OBJECT, JsonScope.NONEMPTY_OBJECT, "}");
    }

    private JsonWriter open(JsonScope empty, String openBracket) throws IOException {
        beforeValue(true);
        this.stack.add(empty);
        this.out.write(openBracket);
        return this;
    }

    private JsonWriter close(JsonScope empty, JsonScope nonempty, String closeBracket) throws IOException {
        JsonScope context = peek();
        if (context == nonempty || context == empty) {
            List list = this.stack;
            list.remove(list.size() - 1);
            if (context == nonempty) {
                newline();
            }
            this.out.write(closeBracket);
            return this;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Nesting problem: ");
        stringBuilder.append(this.stack);
        throw new IllegalStateException(stringBuilder.toString());
    }

    private JsonScope peek() {
        List list = this.stack;
        return (JsonScope) list.get(list.size() - 1);
    }

    private void replaceTop(JsonScope topOfStack) {
        List list = this.stack;
        list.set(list.size() - 1, topOfStack);
    }

    public JsonWriter name(String name) throws IOException {
        if (name != null) {
            beforeName();
            string(name);
            return this;
        }
        throw new NullPointerException("name == null");
    }

    public JsonWriter value(String value) throws IOException {
        if (value == null) {
            return nullValue();
        }
        beforeValue(false);
        string(value);
        return this;
    }

    public JsonWriter nullValue() throws IOException {
        beforeValue(false);
        this.out.write("null");
        return this;
    }

    public JsonWriter value(boolean value) throws IOException {
        beforeValue(false);
        this.out.write(value ? "true" : "false");
        return this;
    }

    public JsonWriter value(double value) throws IOException {
        if (this.lenient || !(Double.isNaN(value) || Double.isInfinite(value))) {
            beforeValue(false);
            this.out.append(Double.toString(value));
            return this;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Numeric values must be finite, but was ");
        stringBuilder.append(value);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public JsonWriter value(long value) throws IOException {
        beforeValue(false);
        this.out.write(Long.toString(value));
        return this;
    }

    public JsonWriter value(Number value) throws IOException {
        if (value == null) {
            return nullValue();
        }
        String string = value.toString();
        if (this.lenient || !(string.equals("-Infinity") || string.equals("Infinity") || string.equals("NaN"))) {
            beforeValue(false);
            this.out.append(string);
            return this;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Numeric values must be finite, but was ");
        stringBuilder.append(value);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public void flush() throws IOException {
        this.out.flush();
    }

    public void close() throws IOException {
        this.out.close();
        if (peek() != JsonScope.NONEMPTY_DOCUMENT) {
            throw new IOException("Incomplete document");
        }
    }

    private void string(String value) throws IOException {
        String str = "\"";
        this.out.write(str);
        int length = value.length();
        for (int i = 0; i < length; i++) {
            char c = value.charAt(i);
            if (c == 12) {
                this.out.write("\\f");
            } else if (c != CharUtils.CR) {
                if (c != '\"' && c != '\\') {
                    String str2 = "\\u%04x";
                    if (c != 8232 && c != 8233) {
                        switch (c) {
                            case 8:
                                this.out.write("\\b");
                                break;
                            case 9:
                                this.out.write("\\t");
                                break;
                            case 10:
                                this.out.write("\\n");
                                break;
                            default:
                                if (c > 31) {
                                    this.out.write(c);
                                    break;
                                }
                                this.out.write(String.format(str2, new Object[]{Integer.valueOf(c)}));
                                break;
                        }
                    }
                    this.out.write(String.format(str2, new Object[]{Integer.valueOf(c)}));
                } else {
                    this.out.write(92);
                    this.out.write(c);
                }
            } else {
                this.out.write("\\r");
            }
        }
        this.out.write(str);
    }

    private void newline() throws IOException {
        if (this.indent != null) {
            this.out.write("\n");
            for (int i = 1; i < this.stack.size(); i++) {
                this.out.write(this.indent);
            }
        }
    }

    private void beforeName() throws IOException {
        JsonScope context = peek();
        if (context == JsonScope.NONEMPTY_OBJECT) {
            this.out.write(44);
        } else if (context != JsonScope.EMPTY_OBJECT) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Nesting problem: ");
            stringBuilder.append(this.stack);
            throw new IllegalStateException(stringBuilder.toString());
        }
        newline();
        replaceTop(JsonScope.DANGLING_NAME);
    }

    private void beforeValue(boolean root) throws IOException {
        int i = AnonymousClass1.$SwitchMap$android$util$JsonScope[peek().ordinal()];
        if (i != 1) {
            if (i == 2) {
                replaceTop(JsonScope.NONEMPTY_ARRAY);
                newline();
            } else if (i == 3) {
                this.out.append(',');
                newline();
            } else if (i == 4) {
                this.out.append(this.separator);
                replaceTop(JsonScope.NONEMPTY_OBJECT);
            } else if (i != 5) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Nesting problem: ");
                stringBuilder.append(this.stack);
                throw new IllegalStateException(stringBuilder.toString());
            } else {
                throw new IllegalStateException("JSON must have only one top-level value.");
            }
        } else if (this.lenient || root) {
            replaceTop(JsonScope.NONEMPTY_DOCUMENT);
        } else {
            throw new IllegalStateException("JSON must start with an array or an object.");
        }
    }
}
