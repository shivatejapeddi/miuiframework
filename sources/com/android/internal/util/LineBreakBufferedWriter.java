package com.android.internal.util;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;

public class LineBreakBufferedWriter extends PrintWriter {
    private char[] buffer;
    private int bufferIndex;
    private final int bufferSize;
    private int lastNewline;
    private final String lineSeparator;

    public LineBreakBufferedWriter(Writer out, int bufferSize) {
        this(out, bufferSize, 16);
    }

    public LineBreakBufferedWriter(Writer out, int bufferSize, int initialCapacity) {
        super(out);
        this.lastNewline = -1;
        this.buffer = new char[Math.min(initialCapacity, bufferSize)];
        this.bufferIndex = 0;
        this.bufferSize = bufferSize;
        this.lineSeparator = System.getProperty("line.separator");
    }

    public void flush() {
        writeBuffer(this.bufferIndex);
        this.bufferIndex = 0;
        super.flush();
    }

    public void write(int c) {
        int i = this.bufferIndex;
        char[] cArr = this.buffer;
        if (i < cArr.length) {
            cArr[i] = (char) c;
            this.bufferIndex = i + 1;
            if (((char) c) == 10) {
                this.lastNewline = this.bufferIndex;
                return;
            }
            return;
        }
        write(new char[]{(char) c}, 0, 1);
    }

    public void println() {
        write(this.lineSeparator);
    }

    public void write(char[] buf, int off, int len) {
        int i;
        while (true) {
            i = this.bufferIndex;
            int i2 = i + len;
            int maxLength = this.bufferSize;
            if (i2 <= maxLength) {
                break;
            }
            i2 = -1;
            maxLength -= i;
            for (i = 0; i < maxLength; i++) {
                if (buf[off + i] == 10) {
                    if (this.bufferIndex + i >= this.bufferSize) {
                        break;
                    }
                    i2 = i;
                }
            }
            if (i2 != -1) {
                appendToBuffer(buf, off, i2);
                writeBuffer(this.bufferIndex);
                this.bufferIndex = 0;
                this.lastNewline = -1;
                off += i2 + 1;
                len -= i2 + 1;
            } else {
                int i3 = this.lastNewline;
                if (i3 != -1) {
                    writeBuffer(i3);
                    removeFromBuffer(this.lastNewline + 1);
                    this.lastNewline = -1;
                } else {
                    int rest = this.bufferSize - this.bufferIndex;
                    appendToBuffer(buf, off, rest);
                    writeBuffer(this.bufferIndex);
                    this.bufferIndex = 0;
                    off += rest;
                    len -= rest;
                }
            }
        }
        if (len > 0) {
            appendToBuffer(buf, off, len);
            for (i = len - 1; i >= 0; i--) {
                if (buf[off + i] == 10) {
                    this.lastNewline = (this.bufferIndex - len) + i;
                    return;
                }
            }
        }
    }

    public void write(String s, int off, int len) {
        int i;
        while (true) {
            i = this.bufferIndex;
            int i2 = i + len;
            int maxLength = this.bufferSize;
            if (i2 <= maxLength) {
                break;
            }
            i2 = -1;
            maxLength -= i;
            for (i = 0; i < maxLength; i++) {
                if (s.charAt(off + i) == 10) {
                    if (this.bufferIndex + i >= this.bufferSize) {
                        break;
                    }
                    i2 = i;
                }
            }
            if (i2 != -1) {
                appendToBuffer(s, off, i2);
                writeBuffer(this.bufferIndex);
                this.bufferIndex = 0;
                this.lastNewline = -1;
                off += i2 + 1;
                len -= i2 + 1;
            } else {
                int i3 = this.lastNewline;
                if (i3 != -1) {
                    writeBuffer(i3);
                    removeFromBuffer(this.lastNewline + 1);
                    this.lastNewline = -1;
                } else {
                    int rest = this.bufferSize - this.bufferIndex;
                    appendToBuffer(s, off, rest);
                    writeBuffer(this.bufferIndex);
                    this.bufferIndex = 0;
                    off += rest;
                    len -= rest;
                }
            }
        }
        if (len > 0) {
            appendToBuffer(s, off, len);
            for (i = len - 1; i >= 0; i--) {
                if (s.charAt(off + i) == 10) {
                    this.lastNewline = (this.bufferIndex - len) + i;
                    return;
                }
            }
        }
    }

    private void appendToBuffer(char[] buf, int off, int len) {
        int i = this.bufferIndex;
        if (i + len > this.buffer.length) {
            ensureCapacity(i + len);
        }
        System.arraycopy(buf, off, this.buffer, this.bufferIndex, len);
        this.bufferIndex += len;
    }

    private void appendToBuffer(String s, int off, int len) {
        int i = this.bufferIndex;
        if (i + len > this.buffer.length) {
            ensureCapacity(i + len);
        }
        s.getChars(off, off + len, this.buffer, this.bufferIndex);
        this.bufferIndex += len;
    }

    private void ensureCapacity(int capacity) {
        int newSize = (this.buffer.length * 2) + 2;
        if (newSize < capacity) {
            newSize = capacity;
        }
        this.buffer = Arrays.copyOf(this.buffer, newSize);
    }

    private void removeFromBuffer(int i) {
        int i2 = this.bufferIndex;
        int rest = i2 - i;
        if (rest > 0) {
            char[] cArr = this.buffer;
            System.arraycopy(cArr, i2 - rest, cArr, 0, rest);
            this.bufferIndex = rest;
            return;
        }
        this.bufferIndex = 0;
    }

    private void writeBuffer(int length) {
        if (length > 0) {
            super.write(this.buffer, 0, length);
        }
    }
}
