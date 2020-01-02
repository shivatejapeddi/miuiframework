package android.util;

import android.telephony.PhoneNumberUtils;
import android.text.format.DateFormat;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import libcore.internal.StringPool;
import org.apache.miui.commons.lang3.CharUtils;

public final class JsonReader implements Closeable {
    private static final String FALSE = "false";
    private static final String TRUE = "true";
    private final char[] buffer = new char[1024];
    private int bufferStartColumn = 1;
    private int bufferStartLine = 1;
    private final Reader in;
    private boolean lenient = false;
    private int limit = 0;
    private String name;
    private int pos = 0;
    private boolean skipping;
    private final List<JsonScope> stack = new ArrayList();
    private final StringPool stringPool = new StringPool();
    private JsonToken token;
    private String value;
    private int valueLength;
    private int valuePos;

    public JsonReader(Reader in) {
        push(JsonScope.EMPTY_DOCUMENT);
        this.skipping = false;
        if (in != null) {
            this.in = in;
            return;
        }
        throw new NullPointerException("in == null");
    }

    public void setLenient(boolean lenient) {
        this.lenient = lenient;
    }

    public boolean isLenient() {
        return this.lenient;
    }

    public void beginArray() throws IOException {
        expect(JsonToken.BEGIN_ARRAY);
    }

    public void endArray() throws IOException {
        expect(JsonToken.END_ARRAY);
    }

    public void beginObject() throws IOException {
        expect(JsonToken.BEGIN_OBJECT);
    }

    public void endObject() throws IOException {
        expect(JsonToken.END_OBJECT);
    }

    private void expect(JsonToken expected) throws IOException {
        peek();
        if (this.token == expected) {
            advance();
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected ");
        stringBuilder.append(expected);
        stringBuilder.append(" but was ");
        stringBuilder.append(peek());
        throw new IllegalStateException(stringBuilder.toString());
    }

    public boolean hasNext() throws IOException {
        peek();
        return (this.token == JsonToken.END_OBJECT || this.token == JsonToken.END_ARRAY) ? false : true;
    }

    public JsonToken peek() throws IOException {
        JsonToken jsonToken = this.token;
        if (jsonToken != null) {
            return jsonToken;
        }
        switch (peekStack()) {
            case EMPTY_DOCUMENT:
                replaceTop(JsonScope.NONEMPTY_DOCUMENT);
                jsonToken = nextValue();
                if (this.lenient || this.token == JsonToken.BEGIN_ARRAY || this.token == JsonToken.BEGIN_OBJECT) {
                    return jsonToken;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Expected JSON document to start with '[' or '{' but was ");
                stringBuilder.append(this.token);
                throw new IOException(stringBuilder.toString());
            case EMPTY_ARRAY:
                return nextInArray(true);
            case NONEMPTY_ARRAY:
                return nextInArray(false);
            case EMPTY_OBJECT:
                return nextInObject(true);
            case DANGLING_NAME:
                return objectValue();
            case NONEMPTY_OBJECT:
                return nextInObject(false);
            case NONEMPTY_DOCUMENT:
                try {
                    jsonToken = nextValue();
                    if (this.lenient) {
                        return jsonToken;
                    }
                    throw syntaxError("Expected EOF");
                } catch (EOFException e) {
                    JsonToken jsonToken2 = JsonToken.END_DOCUMENT;
                    this.token = jsonToken2;
                    return jsonToken2;
                }
            case CLOSED:
                throw new IllegalStateException("JsonReader is closed");
            default:
                throw new AssertionError();
        }
    }

    private JsonToken advance() throws IOException {
        peek();
        JsonToken result = this.token;
        this.token = null;
        this.value = null;
        this.name = null;
        return result;
    }

    public String nextName() throws IOException {
        peek();
        if (this.token == JsonToken.NAME) {
            String result = this.name;
            advance();
            return result;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected a name but was ");
        stringBuilder.append(peek());
        throw new IllegalStateException(stringBuilder.toString());
    }

    public String nextString() throws IOException {
        peek();
        if (this.token == JsonToken.STRING || this.token == JsonToken.NUMBER) {
            String result = this.value;
            advance();
            return result;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected a string but was ");
        stringBuilder.append(peek());
        throw new IllegalStateException(stringBuilder.toString());
    }

    public boolean nextBoolean() throws IOException {
        peek();
        if (this.token == JsonToken.BOOLEAN) {
            boolean result = this.value == TRUE;
            advance();
            return result;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected a boolean but was ");
        stringBuilder.append(this.token);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public void nextNull() throws IOException {
        peek();
        if (this.token == JsonToken.NULL) {
            advance();
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected null but was ");
        stringBuilder.append(this.token);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public double nextDouble() throws IOException {
        peek();
        if (this.token == JsonToken.STRING || this.token == JsonToken.NUMBER) {
            double result = Double.parseDouble(this.value);
            advance();
            return result;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected a double but was ");
        stringBuilder.append(this.token);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public long nextLong() throws IOException {
        peek();
        if (this.token == JsonToken.STRING || this.token == JsonToken.NUMBER) {
            NumberFormatException ignored;
            try {
                ignored = Long.parseLong(this.value);
            } catch (NumberFormatException e) {
                double asDouble = Double.parseDouble(this.value);
                long result = (long) asDouble;
                if (((double) result) == asDouble) {
                    ignored = result;
                } else {
                    throw new NumberFormatException(this.value);
                }
            }
            advance();
            return ignored;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected a long but was ");
        stringBuilder.append(this.token);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public int nextInt() throws IOException {
        peek();
        if (this.token == JsonToken.STRING || this.token == JsonToken.NUMBER) {
            NumberFormatException ignored;
            try {
                ignored = Integer.parseInt(this.value);
            } catch (NumberFormatException e) {
                double asDouble = Double.parseDouble(this.value);
                int result = (int) asDouble;
                if (((double) result) == asDouble) {
                    ignored = result;
                } else {
                    throw new NumberFormatException(this.value);
                }
            }
            advance();
            return ignored;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected an int but was ");
        stringBuilder.append(this.token);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public void close() throws IOException {
        this.value = null;
        this.token = null;
        this.stack.clear();
        this.stack.add(JsonScope.CLOSED);
        this.in.close();
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x002f A:{SYNTHETIC, EDGE_INSN: B:25:0x002f->B:29:0x002f ?: BREAK  } */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x002f A:{SYNTHETIC, EDGE_INSN: B:25:0x002f->B:29:0x002f ?: BREAK  } */
    public void skipValue() throws java.io.IOException {
        /*
        r4 = this;
        r0 = 1;
        r4.skipping = r0;
        r0 = 0;
        r1 = r4.hasNext();	 Catch:{ all -> 0x003b }
        if (r1 == 0) goto L_0x0033;
    L_0x000a:
        r1 = r4.peek();	 Catch:{ all -> 0x003b }
        r2 = android.util.JsonToken.END_DOCUMENT;	 Catch:{ all -> 0x003b }
        if (r1 == r2) goto L_0x0033;
    L_0x0012:
        r1 = r0;
    L_0x0013:
        r2 = r4.advance();	 Catch:{ all -> 0x003b }
        r3 = android.util.JsonToken.BEGIN_ARRAY;	 Catch:{ all -> 0x003b }
        if (r2 == r3) goto L_0x002b;
    L_0x001b:
        r3 = android.util.JsonToken.BEGIN_OBJECT;	 Catch:{ all -> 0x003b }
        if (r2 != r3) goto L_0x0020;
    L_0x001f:
        goto L_0x002b;
    L_0x0020:
        r3 = android.util.JsonToken.END_ARRAY;	 Catch:{ all -> 0x003b }
        if (r2 == r3) goto L_0x0028;
    L_0x0024:
        r3 = android.util.JsonToken.END_OBJECT;	 Catch:{ all -> 0x003b }
        if (r2 != r3) goto L_0x002d;
    L_0x0028:
        r1 = r1 + -1;
        goto L_0x002d;
    L_0x002b:
        r1 = r1 + 1;
    L_0x002d:
        if (r1 != 0) goto L_0x0013;
    L_0x002f:
        r4.skipping = r0;
        return;
    L_0x0033:
        r1 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x003b }
        r2 = "No element left to skip";
        r1.<init>(r2);	 Catch:{ all -> 0x003b }
        throw r1;	 Catch:{ all -> 0x003b }
    L_0x003b:
        r1 = move-exception;
        r4.skipping = r0;
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.JsonReader.skipValue():void");
    }

    private JsonScope peekStack() {
        List list = this.stack;
        return (JsonScope) list.get(list.size() - 1);
    }

    private JsonScope pop() {
        List list = this.stack;
        return (JsonScope) list.remove(list.size() - 1);
    }

    private void push(JsonScope newTop) {
        this.stack.add(newTop);
    }

    private void replaceTop(JsonScope newTop) {
        List list = this.stack;
        list.set(list.size() - 1, newTop);
    }

    private JsonToken nextInArray(boolean firstElement) throws IOException {
        int nextNonWhitespace;
        JsonToken jsonToken;
        if (firstElement) {
            replaceTop(JsonScope.NONEMPTY_ARRAY);
        } else {
            nextNonWhitespace = nextNonWhitespace();
            if (nextNonWhitespace != 44) {
                if (nextNonWhitespace == 59) {
                    checkLenient();
                } else if (nextNonWhitespace == 93) {
                    pop();
                    jsonToken = JsonToken.END_ARRAY;
                    this.token = jsonToken;
                    return jsonToken;
                } else {
                    throw syntaxError("Unterminated array");
                }
            }
        }
        nextNonWhitespace = nextNonWhitespace();
        if (!(nextNonWhitespace == 44 || nextNonWhitespace == 59)) {
            if (nextNonWhitespace != 93) {
                this.pos--;
                return nextValue();
            } else if (firstElement) {
                pop();
                jsonToken = JsonToken.END_ARRAY;
                this.token = jsonToken;
                return jsonToken;
            }
        }
        checkLenient();
        this.pos--;
        this.value = "null";
        jsonToken = JsonToken.NULL;
        this.token = jsonToken;
        return jsonToken;
    }

    private JsonToken nextInObject(boolean firstElement) throws IOException {
        JsonToken jsonToken;
        JsonToken jsonToken2;
        if (!firstElement) {
            int nextNonWhitespace = nextNonWhitespace();
            if (!(nextNonWhitespace == 44 || nextNonWhitespace == 59)) {
                if (nextNonWhitespace == 125) {
                    pop();
                    jsonToken2 = JsonToken.END_OBJECT;
                    this.token = jsonToken2;
                    return jsonToken2;
                }
                throw syntaxError("Unterminated object");
            }
        } else if (nextNonWhitespace() != 125) {
            this.pos--;
        } else {
            pop();
            jsonToken2 = JsonToken.END_OBJECT;
            this.token = jsonToken2;
            return jsonToken2;
        }
        int quote = nextNonWhitespace();
        if (quote != 34) {
            if (quote != 39) {
                checkLenient();
                this.pos--;
                this.name = nextLiteral(false);
                if (this.name.isEmpty()) {
                    throw syntaxError("Expected name");
                }
                replaceTop(JsonScope.DANGLING_NAME);
                jsonToken = JsonToken.NAME;
                this.token = jsonToken;
                return jsonToken;
            }
            checkLenient();
        }
        this.name = nextString((char) quote);
        replaceTop(JsonScope.DANGLING_NAME);
        jsonToken = JsonToken.NAME;
        this.token = jsonToken;
        return jsonToken;
    }

    private JsonToken objectValue() throws IOException {
        int nextNonWhitespace = nextNonWhitespace();
        if (nextNonWhitespace != 58) {
            if (nextNonWhitespace == 61) {
                checkLenient();
                if (this.pos < this.limit || fillBuffer(1)) {
                    char[] cArr = this.buffer;
                    int i = this.pos;
                    if (cArr[i] == '>') {
                        this.pos = i + 1;
                    }
                }
            } else {
                throw syntaxError("Expected ':'");
            }
        }
        replaceTop(JsonScope.NONEMPTY_OBJECT);
        return nextValue();
    }

    private JsonToken nextValue() throws IOException {
        JsonToken jsonToken;
        int c = nextNonWhitespace();
        if (c != 34) {
            if (c == 39) {
                checkLenient();
            } else if (c == 91) {
                push(JsonScope.EMPTY_ARRAY);
                jsonToken = JsonToken.BEGIN_ARRAY;
                this.token = jsonToken;
                return jsonToken;
            } else if (c != 123) {
                this.pos--;
                return readLiteral();
            } else {
                push(JsonScope.EMPTY_OBJECT);
                jsonToken = JsonToken.BEGIN_OBJECT;
                this.token = jsonToken;
                return jsonToken;
            }
        }
        this.value = nextString((char) c);
        jsonToken = JsonToken.STRING;
        this.token = jsonToken;
        return jsonToken;
    }

    private boolean fillBuffer(int minimum) throws IOException {
        int i;
        int i2 = 0;
        while (true) {
            i = this.pos;
            if (i2 >= i) {
                break;
            }
            if (this.buffer[i2] == 10) {
                this.bufferStartLine++;
                this.bufferStartColumn = 1;
            } else {
                this.bufferStartColumn++;
            }
            i2++;
        }
        i2 = this.limit;
        if (i2 != i) {
            this.limit = i2 - i;
            char[] cArr = this.buffer;
            System.arraycopy(cArr, i, cArr, 0, this.limit);
        } else {
            this.limit = 0;
        }
        this.pos = 0;
        do {
            Reader reader = this.in;
            char[] cArr2 = this.buffer;
            int i3 = this.limit;
            i2 = reader.read(cArr2, i3, cArr2.length - i3);
            i = i2;
            if (i2 == -1) {
                return false;
            }
            this.limit += i;
            if (this.bufferStartLine == 1) {
                i2 = this.bufferStartColumn;
                if (i2 == 1 && this.limit > 0 && this.buffer[0] == 65279) {
                    this.pos++;
                    this.bufferStartColumn = i2 - 1;
                }
            }
        } while (this.limit < minimum);
        return true;
    }

    private int getLineNumber() {
        int result = this.bufferStartLine;
        for (int i = 0; i < this.pos; i++) {
            if (this.buffer[i] == 10) {
                result++;
            }
        }
        return result;
    }

    private int getColumnNumber() {
        int result = this.bufferStartColumn;
        for (int i = 0; i < this.pos; i++) {
            if (this.buffer[i] == 10) {
                result = 1;
            } else {
                result++;
            }
        }
        return result;
    }

    private int nextNonWhitespace() throws IOException {
        while (true) {
            if (this.pos < this.limit || fillBuffer(1)) {
                int c = this.buffer;
                int i = this.pos;
                this.pos = i + 1;
                c = c[i];
                if (c != 9 && c != 10 && c != 13 && c != 32) {
                    if (c == 35) {
                        checkLenient();
                        skipToEndOfLine();
                    } else if (c != 47) {
                        return c;
                    } else {
                        if (this.pos == this.limit && !fillBuffer(1)) {
                            return c;
                        }
                        checkLenient();
                        char peek = this.buffer;
                        int i2 = this.pos;
                        peek = peek[i2];
                        if (peek == '*') {
                            this.pos = i2 + 1;
                            if (skipTo("*/")) {
                                this.pos += 2;
                            } else {
                                throw syntaxError("Unterminated comment");
                            }
                        } else if (peek != '/') {
                            return c;
                        } else {
                            this.pos = i2 + 1;
                            skipToEndOfLine();
                        }
                    }
                }
            } else {
                throw new EOFException("End of input");
            }
        }
    }

    private void checkLenient() throws IOException {
        if (!this.lenient) {
            throw syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
        }
    }

    private void skipToEndOfLine() throws IOException {
        while (true) {
            if (this.pos < this.limit || fillBuffer(1)) {
                char c = this.buffer;
                int i = this.pos;
                this.pos = i + 1;
                c = c[i];
                if (c == CharUtils.CR || c == 10) {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private boolean skipTo(String toFind) throws IOException {
        while (true) {
            if (this.pos + toFind.length() > this.limit && !fillBuffer(toFind.length())) {
                return false;
            }
            int c = 0;
            while (c < toFind.length()) {
                if (this.buffer[this.pos + c] != toFind.charAt(c)) {
                    this.pos++;
                } else {
                    c++;
                }
            }
            return true;
        }
    }

    /* JADX WARNING: Missing block: B:21:0x0054, code skipped:
            if (r0 != null) goto L_0x005c;
     */
    /* JADX WARNING: Missing block: B:22:0x0056, code skipped:
            r0 = new java.lang.StringBuilder();
     */
    /* JADX WARNING: Missing block: B:23:0x005c, code skipped:
            r0.append(r7.buffer, r1, r7.pos - r1);
     */
    /* JADX WARNING: Missing block: B:24:0x0068, code skipped:
            if (fillBuffer(1) == false) goto L_0x006b;
     */
    /* JADX WARNING: Missing block: B:27:0x0071, code skipped:
            throw syntaxError("Unterminated string");
     */
    private java.lang.String nextString(char r8) throws java.io.IOException {
        /*
        r7 = this;
        r0 = 0;
    L_0x0001:
        r1 = r7.pos;
    L_0x0003:
        r2 = r7.pos;
        r3 = r7.limit;
        r4 = 1;
        if (r2 >= r3) goto L_0x0054;
    L_0x000a:
        r3 = r7.buffer;
        r5 = r2 + 1;
        r7.pos = r5;
        r2 = r3[r2];
        if (r2 != r8) goto L_0x0035;
    L_0x0014:
        r5 = r7.skipping;
        if (r5 == 0) goto L_0x001c;
    L_0x0018:
        r3 = "skipped!";
        return r3;
    L_0x001c:
        if (r0 != 0) goto L_0x0029;
    L_0x001e:
        r5 = r7.stringPool;
        r6 = r7.pos;
        r6 = r6 - r1;
        r6 = r6 - r4;
        r3 = r5.get(r3, r1, r6);
        return r3;
    L_0x0029:
        r5 = r7.pos;
        r5 = r5 - r1;
        r5 = r5 - r4;
        r0.append(r3, r1, r5);
        r3 = r0.toString();
        return r3;
    L_0x0035:
        r3 = 92;
        if (r2 != r3) goto L_0x0053;
    L_0x0039:
        if (r0 != 0) goto L_0x0041;
    L_0x003b:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r3;
    L_0x0041:
        r3 = r7.buffer;
        r5 = r7.pos;
        r5 = r5 - r1;
        r5 = r5 - r4;
        r0.append(r3, r1, r5);
        r3 = r7.readEscapeCharacter();
        r0.append(r3);
        r1 = r7.pos;
    L_0x0053:
        goto L_0x0003;
    L_0x0054:
        if (r0 != 0) goto L_0x005c;
    L_0x0056:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r0 = r2;
    L_0x005c:
        r2 = r7.buffer;
        r3 = r7.pos;
        r3 = r3 - r1;
        r0.append(r2, r1, r3);
        r1 = r7.fillBuffer(r4);
        if (r1 == 0) goto L_0x006b;
    L_0x006a:
        goto L_0x0001;
    L_0x006b:
        r1 = "Unterminated string";
        r1 = r7.syntaxError(r1);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.JsonReader.nextString(char):java.lang.String");
    }

    /* JADX WARNING: Missing block: B:31:0x004f, code skipped:
            checkLenient();
     */
    private java.lang.String nextLiteral(boolean r7) throws java.io.IOException {
        /*
        r6 = this;
        r0 = 0;
        r1 = -1;
        r6.valuePos = r1;
        r1 = 0;
        r6.valueLength = r1;
        r2 = 0;
    L_0x0008:
        r3 = r6.pos;
        r4 = r3 + r2;
        r5 = r6.limit;
        if (r4 >= r5) goto L_0x0053;
    L_0x0010:
        r4 = r6.buffer;
        r3 = r3 + r2;
        r3 = r4[r3];
        r4 = 9;
        if (r3 == r4) goto L_0x0052;
    L_0x0019:
        r4 = 10;
        if (r3 == r4) goto L_0x0052;
    L_0x001d:
        r4 = 12;
        if (r3 == r4) goto L_0x0052;
    L_0x0021:
        r4 = 13;
        if (r3 == r4) goto L_0x0052;
    L_0x0025:
        r4 = 32;
        if (r3 == r4) goto L_0x0052;
    L_0x0029:
        r4 = 35;
        if (r3 == r4) goto L_0x004f;
    L_0x002d:
        r4 = 44;
        if (r3 == r4) goto L_0x0052;
    L_0x0031:
        r4 = 47;
        if (r3 == r4) goto L_0x004f;
    L_0x0035:
        r4 = 61;
        if (r3 == r4) goto L_0x004f;
    L_0x0039:
        r4 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        if (r3 == r4) goto L_0x0052;
    L_0x003d:
        r4 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        if (r3 == r4) goto L_0x0052;
    L_0x0041:
        r4 = 58;
        if (r3 == r4) goto L_0x0052;
    L_0x0045:
        r4 = 59;
        if (r3 == r4) goto L_0x004f;
    L_0x0049:
        switch(r3) {
            case 91: goto L_0x0052;
            case 92: goto L_0x004f;
            case 93: goto L_0x0052;
            default: goto L_0x004c;
        };
    L_0x004c:
        r2 = r2 + 1;
        goto L_0x0008;
    L_0x004f:
        r6.checkLenient();
    L_0x0052:
        goto L_0x008a;
    L_0x0053:
        r3 = r6.buffer;
        r3 = r3.length;
        if (r2 >= r3) goto L_0x0068;
    L_0x0058:
        r3 = r2 + 1;
        r3 = r6.fillBuffer(r3);
        if (r3 == 0) goto L_0x0061;
    L_0x0060:
        goto L_0x0008;
    L_0x0061:
        r3 = r6.buffer;
        r4 = r6.limit;
        r3[r4] = r1;
        goto L_0x008a;
    L_0x0068:
        if (r0 != 0) goto L_0x0070;
    L_0x006a:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r3;
    L_0x0070:
        r3 = r6.buffer;
        r4 = r6.pos;
        r0.append(r3, r4, r2);
        r3 = r6.valueLength;
        r3 = r3 + r2;
        r6.valueLength = r3;
        r3 = r6.pos;
        r3 = r3 + r2;
        r6.pos = r3;
        r2 = 0;
        r3 = 1;
        r3 = r6.fillBuffer(r3);
        if (r3 != 0) goto L_0x00bf;
    L_0x008a:
        if (r7 == 0) goto L_0x0094;
    L_0x008c:
        if (r0 != 0) goto L_0x0094;
    L_0x008e:
        r1 = r6.pos;
        r6.valuePos = r1;
        r1 = 0;
        goto L_0x00b4;
    L_0x0094:
        r1 = r6.skipping;
        if (r1 == 0) goto L_0x009c;
    L_0x0098:
        r1 = "skipped!";
        goto L_0x00b4;
    L_0x009c:
        if (r0 != 0) goto L_0x00a9;
    L_0x009e:
        r1 = r6.stringPool;
        r3 = r6.buffer;
        r4 = r6.pos;
        r1 = r1.get(r3, r4, r2);
        goto L_0x00b4;
    L_0x00a9:
        r1 = r6.buffer;
        r3 = r6.pos;
        r0.append(r1, r3, r2);
        r1 = r0.toString();
    L_0x00b4:
        r3 = r6.valueLength;
        r3 = r3 + r2;
        r6.valueLength = r3;
        r3 = r6.pos;
        r3 = r3 + r2;
        r6.pos = r3;
        return r1;
    L_0x00bf:
        goto L_0x0008;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.JsonReader.nextLiteral(boolean):java.lang.String");
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getClass().getSimpleName());
        stringBuilder.append(" near ");
        stringBuilder.append(getSnippet());
        return stringBuilder.toString();
    }

    private char readEscapeCharacter() throws IOException {
        String str = "Unterminated escape sequence";
        if (this.pos != this.limit || fillBuffer(1)) {
            char escaped = this.buffer;
            int i = this.pos;
            this.pos = i + 1;
            escaped = escaped[i];
            if (escaped == 'b') {
                return 8;
            }
            if (escaped == 'f') {
                return 12;
            }
            if (escaped == 'n') {
                return 10;
            }
            if (escaped == 'r') {
                return CharUtils.CR;
            }
            if (escaped == 't') {
                return 9;
            }
            if (escaped != 'u') {
                return escaped;
            }
            if (this.pos + 4 <= this.limit || fillBuffer(4)) {
                String hex = this.stringPool.get(this.buffer, this.pos, 4);
                this.pos += 4;
                return (char) Integer.parseInt(hex, 16);
            }
            throw syntaxError(str);
        }
        throw syntaxError(str);
    }

    private JsonToken readLiteral() throws IOException {
        this.value = nextLiteral(true);
        if (this.valueLength != 0) {
            this.token = decodeLiteral();
            if (this.token == JsonToken.STRING) {
                checkLenient();
            }
            return this.token;
        }
        throw syntaxError("Expected literal value");
    }

    private JsonToken decodeLiteral() throws IOException {
        int i = this.valuePos;
        if (i == -1) {
            return JsonToken.STRING;
        }
        char[] cArr;
        int i2;
        if (this.valueLength == 4) {
            char[] cArr2 = this.buffer;
            if ('n' == cArr2[i] || PhoneNumberUtils.WILD == cArr2[i]) {
                cArr = this.buffer;
                int i3 = this.valuePos;
                if ('u' == cArr[i3 + 1] || 'U' == cArr[i3 + 1]) {
                    cArr = this.buffer;
                    i3 = this.valuePos;
                    if ('l' == cArr[i3 + 2] || DateFormat.STANDALONE_MONTH == cArr[i3 + 2]) {
                        cArr = this.buffer;
                        i3 = this.valuePos;
                        if ('l' == cArr[i3 + 3] || DateFormat.STANDALONE_MONTH == cArr[i3 + 3]) {
                            this.value = "null";
                            return JsonToken.NULL;
                        }
                    }
                }
            }
        }
        if (this.valueLength == 4) {
            char[] cArr3 = this.buffer;
            int i4 = this.valuePos;
            if ('t' == cArr3[i4] || 'T' == cArr3[i4]) {
                cArr3 = this.buffer;
                i4 = this.valuePos;
                if ('r' == cArr3[i4 + 1] || 'R' == cArr3[i4 + 1]) {
                    cArr = this.buffer;
                    int i5 = this.valuePos;
                    if ('u' == cArr[i5 + 2] || 'U' == cArr[i5 + 2]) {
                        cArr = this.buffer;
                        i2 = this.valuePos;
                        if ('e' == cArr[i2 + 3] || DateFormat.DAY == cArr[i2 + 3]) {
                            this.value = TRUE;
                            return JsonToken.BOOLEAN;
                        }
                    }
                }
            }
        }
        if (this.valueLength == 5) {
            char[] cArr4 = this.buffer;
            int i6 = this.valuePos;
            if ('f' == cArr4[i6] || 'F' == cArr4[i6]) {
                cArr4 = this.buffer;
                i6 = this.valuePos;
                if (DateFormat.AM_PM == cArr4[i6 + 1] || DateFormat.CAPITAL_AM_PM == cArr4[i6 + 1]) {
                    cArr = this.buffer;
                    i2 = this.valuePos;
                    if ('l' == cArr[i2 + 2] || DateFormat.STANDALONE_MONTH == cArr[i2 + 2]) {
                        cArr4 = this.buffer;
                        i6 = this.valuePos;
                        if ('s' == cArr4[i6 + 3] || 'S' == cArr4[i6 + 3]) {
                            cArr = this.buffer;
                            i2 = this.valuePos;
                            if ('e' == cArr[i2 + 4] || DateFormat.DAY == cArr[i2 + 4]) {
                                this.value = FALSE;
                                return JsonToken.BOOLEAN;
                            }
                        }
                    }
                }
            }
        }
        this.value = this.stringPool.get(this.buffer, this.valuePos, this.valueLength);
        return decodeNumber(this.buffer, this.valuePos, this.valueLength);
    }

    private JsonToken decodeNumber(char[] chars, int offset, int length) {
        int i = offset;
        int c = chars[i];
        if (c == 45) {
            i++;
            c = chars[i];
        }
        if (c == 48) {
            i++;
            c = chars[i];
        } else if (c < 49 || c > 57) {
            return JsonToken.STRING;
        } else {
            i++;
            c = chars[i];
            while (c >= 48 && c <= 57) {
                i++;
                c = chars[i];
            }
        }
        if (c == 46) {
            i++;
            c = chars[i];
            while (c >= 48 && c <= 57) {
                i++;
                c = chars[i];
            }
        }
        if (c == 101 || c == 69) {
            i++;
            c = chars[i];
            if (c == 43 || c == 45) {
                i++;
                c = chars[i];
            }
            if (c < 48 || c > 57) {
                return JsonToken.STRING;
            }
            i++;
            c = chars[i];
            while (c >= 48 && c <= 57) {
                i++;
                c = chars[i];
            }
        }
        if (i == offset + length) {
            return JsonToken.NUMBER;
        }
        return JsonToken.STRING;
    }

    private IOException syntaxError(String message) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(message);
        stringBuilder.append(" at line ");
        stringBuilder.append(getLineNumber());
        stringBuilder.append(" column ");
        stringBuilder.append(getColumnNumber());
        throw new MalformedJsonException(stringBuilder.toString());
    }

    private CharSequence getSnippet() {
        StringBuilder snippet = new StringBuilder();
        int beforePos = Math.min(this.pos, 20);
        snippet.append(this.buffer, this.pos - beforePos, beforePos);
        snippet.append(this.buffer, this.pos, Math.min(this.limit - this.pos, 20));
        return snippet;
    }
}
