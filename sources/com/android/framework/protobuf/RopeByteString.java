package com.android.framework.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

final class RopeByteString extends ByteString {
    private static final int[] minLengthByDepth;
    private static final long serialVersionUID = 1;
    private final ByteString left;
    private final int leftLength;
    private final ByteString right;
    private final int totalLength;
    private final int treeDepth;

    private static class Balancer {
        private final Stack<ByteString> prefixesStack;

        private Balancer() {
            this.prefixesStack = new Stack();
        }

        private ByteString balance(ByteString left, ByteString right) {
            doBalance(left);
            doBalance(right);
            ByteString partialString = (ByteString) this.prefixesStack.pop();
            while (!this.prefixesStack.isEmpty()) {
                partialString = new RopeByteString((ByteString) this.prefixesStack.pop(), partialString);
            }
            return partialString;
        }

        private void doBalance(ByteString root) {
            if (root.isBalanced()) {
                insert(root);
            } else if (root instanceof RopeByteString) {
                RopeByteString rbs = (RopeByteString) root;
                doBalance(rbs.left);
                doBalance(rbs.right);
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Has a new type of ByteString been created? Found ");
                stringBuilder.append(root.getClass());
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }

        private void insert(ByteString byteString) {
            int depthBin = getDepthBinForLength(byteString.size());
            int binEnd = RopeByteString.minLengthByDepth[depthBin + 1];
            if (this.prefixesStack.isEmpty() || ((ByteString) this.prefixesStack.peek()).size() >= binEnd) {
                this.prefixesStack.push(byteString);
                return;
            }
            int binStart = RopeByteString.minLengthByDepth[depthBin];
            ByteString newTree = (ByteString) this.prefixesStack.pop();
            while (!this.prefixesStack.isEmpty() && ((ByteString) this.prefixesStack.peek()).size() < binStart) {
                newTree = new RopeByteString((ByteString) this.prefixesStack.pop(), newTree);
            }
            newTree = new RopeByteString(newTree, byteString);
            while (!this.prefixesStack.isEmpty()) {
                if (((ByteString) this.prefixesStack.peek()).size() >= RopeByteString.minLengthByDepth[getDepthBinForLength(newTree.size()) + 1]) {
                    break;
                }
                newTree = new RopeByteString((ByteString) this.prefixesStack.pop(), newTree);
            }
            this.prefixesStack.push(newTree);
        }

        private int getDepthBinForLength(int length) {
            int depth = Arrays.binarySearch(RopeByteString.minLengthByDepth, length);
            if (depth < 0) {
                return (-(depth + 1)) - 1;
            }
            return depth;
        }
    }

    private static class PieceIterator implements Iterator<LeafByteString> {
        private final Stack<RopeByteString> breadCrumbs;
        private LeafByteString next;

        private PieceIterator(ByteString root) {
            this.breadCrumbs = new Stack();
            this.next = getLeafByLeft(root);
        }

        private LeafByteString getLeafByLeft(ByteString root) {
            ByteString pos = root;
            while (pos instanceof RopeByteString) {
                RopeByteString rbs = (RopeByteString) pos;
                this.breadCrumbs.push(rbs);
                pos = rbs.left;
            }
            return (LeafByteString) pos;
        }

        private LeafByteString getNextNonEmptyLeaf() {
            while (!this.breadCrumbs.isEmpty()) {
                LeafByteString result = getLeafByLeft(((RopeByteString) this.breadCrumbs.pop()).right);
                if (!result.isEmpty()) {
                    return result;
                }
            }
            return null;
        }

        public boolean hasNext() {
            return this.next != null;
        }

        public LeafByteString next() {
            if (this.next != null) {
                LeafByteString result = this.next;
                this.next = getNextNonEmptyLeaf();
                return result;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class RopeInputStream extends InputStream {
        private LeafByteString currentPiece;
        private int currentPieceIndex;
        private int currentPieceOffsetInRope;
        private int currentPieceSize;
        private int mark;
        private PieceIterator pieceIterator;

        public RopeInputStream() {
            initialize();
        }

        public int read(byte[] b, int offset, int length) {
            if (b == null) {
                throw new NullPointerException();
            } else if (offset >= 0 && length >= 0 && length <= b.length - offset) {
                return readSkipInternal(b, offset, length);
            } else {
                throw new IndexOutOfBoundsException();
            }
        }

        public long skip(long length) {
            if (length >= 0) {
                if (length > 2147483647L) {
                    length = 2147483647L;
                }
                return (long) readSkipInternal(null, 0, (int) length);
            }
            throw new IndexOutOfBoundsException();
        }

        private int readSkipInternal(byte[] b, int offset, int length) {
            int bytesRemaining = length;
            while (bytesRemaining > 0) {
                advanceIfCurrentPieceFullyRead();
                if (this.currentPiece == null) {
                    if (bytesRemaining == length) {
                        return -1;
                    }
                    return length - bytesRemaining;
                }
                int count = Math.min(this.currentPieceSize - this.currentPieceIndex, bytesRemaining);
                if (b != null) {
                    this.currentPiece.copyTo(b, this.currentPieceIndex, offset, count);
                    offset += count;
                }
                this.currentPieceIndex += count;
                bytesRemaining -= count;
            }
            return length - bytesRemaining;
        }

        public int read() throws IOException {
            advanceIfCurrentPieceFullyRead();
            LeafByteString leafByteString = this.currentPiece;
            if (leafByteString == null) {
                return -1;
            }
            int i = this.currentPieceIndex;
            this.currentPieceIndex = i + 1;
            return leafByteString.byteAt(i) & 255;
        }

        public int available() throws IOException {
            return RopeByteString.this.size() - (this.currentPieceOffsetInRope + this.currentPieceIndex);
        }

        public boolean markSupported() {
            return true;
        }

        public void mark(int readAheadLimit) {
            this.mark = this.currentPieceOffsetInRope + this.currentPieceIndex;
        }

        public synchronized void reset() {
            initialize();
            readSkipInternal(null, 0, this.mark);
        }

        private void initialize() {
            this.pieceIterator = new PieceIterator(RopeByteString.this);
            this.currentPiece = this.pieceIterator.next();
            this.currentPieceSize = this.currentPiece.size();
            this.currentPieceIndex = 0;
            this.currentPieceOffsetInRope = 0;
        }

        private void advanceIfCurrentPieceFullyRead() {
            if (this.currentPiece != null) {
                int i = this.currentPieceIndex;
                int i2 = this.currentPieceSize;
                if (i == i2) {
                    this.currentPieceOffsetInRope += i2;
                    this.currentPieceIndex = 0;
                    if (this.pieceIterator.hasNext()) {
                        this.currentPiece = this.pieceIterator.next();
                        this.currentPieceSize = this.currentPiece.size();
                        return;
                    }
                    this.currentPiece = null;
                    this.currentPieceSize = 0;
                }
            }
        }
    }

    static {
        int temp;
        List<Integer> numbers = new ArrayList();
        int f1 = 1;
        int f2 = 1;
        while (f2 > 0) {
            numbers.add(Integer.valueOf(f2));
            temp = f1 + f2;
            f1 = f2;
            f2 = temp;
        }
        numbers.add(Integer.valueOf(Integer.MAX_VALUE));
        minLengthByDepth = new int[numbers.size()];
        temp = 0;
        while (true) {
            int[] iArr = minLengthByDepth;
            if (temp < iArr.length) {
                iArr[temp] = ((Integer) numbers.get(temp)).intValue();
                temp++;
            } else {
                return;
            }
        }
    }

    private RopeByteString(ByteString left, ByteString right) {
        this.left = left;
        this.right = right;
        this.leftLength = left.size();
        this.totalLength = this.leftLength + right.size();
        this.treeDepth = Math.max(left.getTreeDepth(), right.getTreeDepth()) + 1;
    }

    static ByteString concatenate(ByteString left, ByteString right) {
        if (right.size() == 0) {
            return left;
        }
        if (left.size() == 0) {
            return right;
        }
        int newLength = left.size() + right.size();
        if (newLength < 128) {
            return concatenateBytes(left, right);
        }
        if (left instanceof RopeByteString) {
            RopeByteString leftRope = (RopeByteString) left;
            if (leftRope.right.size() + right.size() < 128) {
                return new RopeByteString(leftRope.left, concatenateBytes(leftRope.right, right));
            } else if (leftRope.left.getTreeDepth() > leftRope.right.getTreeDepth() && leftRope.getTreeDepth() > right.getTreeDepth()) {
                return new RopeByteString(leftRope.left, new RopeByteString(leftRope.right, right));
            }
        }
        if (newLength >= minLengthByDepth[Math.max(left.getTreeDepth(), right.getTreeDepth()) + 1]) {
            return new RopeByteString(left, right);
        }
        return new Balancer().balance(left, right);
    }

    private static ByteString concatenateBytes(ByteString left, ByteString right) {
        int leftSize = left.size();
        int rightSize = right.size();
        byte[] bytes = new byte[(leftSize + rightSize)];
        left.copyTo(bytes, 0, 0, leftSize);
        right.copyTo(bytes, 0, leftSize, rightSize);
        return ByteString.wrap(bytes);
    }

    static RopeByteString newInstanceForTest(ByteString left, ByteString right) {
        return new RopeByteString(left, right);
    }

    public byte byteAt(int index) {
        ByteString.checkIndex(index, this.totalLength);
        int i = this.leftLength;
        if (index < i) {
            return this.left.byteAt(index);
        }
        return this.right.byteAt(index - i);
    }

    public int size() {
        return this.totalLength;
    }

    /* Access modifiers changed, original: protected */
    public int getTreeDepth() {
        return this.treeDepth;
    }

    /* Access modifiers changed, original: protected */
    public boolean isBalanced() {
        return this.totalLength >= minLengthByDepth[this.treeDepth];
    }

    public ByteString substring(int beginIndex, int endIndex) {
        int length = ByteString.checkRange(beginIndex, endIndex, this.totalLength);
        if (length == 0) {
            return ByteString.EMPTY;
        }
        if (length == this.totalLength) {
            return this;
        }
        int i = this.leftLength;
        if (endIndex <= i) {
            return this.left.substring(beginIndex, endIndex);
        }
        if (beginIndex >= i) {
            return this.right.substring(beginIndex - i, endIndex - i);
        }
        return new RopeByteString(this.left.substring(beginIndex), this.right.substring(0, endIndex - this.leftLength));
    }

    /* Access modifiers changed, original: protected */
    public void copyToInternal(byte[] target, int sourceOffset, int targetOffset, int numberToCopy) {
        int i = sourceOffset + numberToCopy;
        int leftLength = this.leftLength;
        if (i <= leftLength) {
            this.left.copyToInternal(target, sourceOffset, targetOffset, numberToCopy);
        } else if (sourceOffset >= leftLength) {
            this.right.copyToInternal(target, sourceOffset - leftLength, targetOffset, numberToCopy);
        } else {
            leftLength -= sourceOffset;
            this.left.copyToInternal(target, sourceOffset, targetOffset, leftLength);
            this.right.copyToInternal(target, 0, targetOffset + leftLength, numberToCopy - leftLength);
        }
    }

    public void copyTo(ByteBuffer target) {
        this.left.copyTo(target);
        this.right.copyTo(target);
    }

    public ByteBuffer asReadOnlyByteBuffer() {
        return ByteBuffer.wrap(toByteArray()).asReadOnlyBuffer();
    }

    public List<ByteBuffer> asReadOnlyByteBufferList() {
        List<ByteBuffer> result = new ArrayList();
        PieceIterator pieces = new PieceIterator(this);
        while (pieces.hasNext()) {
            result.add(pieces.next().asReadOnlyByteBuffer());
        }
        return result;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        this.left.writeTo(outputStream);
        this.right.writeTo(outputStream);
    }

    /* Access modifiers changed, original: 0000 */
    public void writeToInternal(OutputStream out, int sourceOffset, int numberToWrite) throws IOException {
        int i = sourceOffset + numberToWrite;
        int numberToWriteInLeft = this.leftLength;
        if (i <= numberToWriteInLeft) {
            this.left.writeToInternal(out, sourceOffset, numberToWrite);
        } else if (sourceOffset >= numberToWriteInLeft) {
            this.right.writeToInternal(out, sourceOffset - numberToWriteInLeft, numberToWrite);
        } else {
            numberToWriteInLeft -= sourceOffset;
            this.left.writeToInternal(out, sourceOffset, numberToWriteInLeft);
            this.right.writeToInternal(out, 0, numberToWrite - numberToWriteInLeft);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void writeTo(ByteOutput output) throws IOException {
        this.left.writeTo(output);
        this.right.writeTo(output);
    }

    /* Access modifiers changed, original: protected */
    public String toStringInternal(Charset charset) {
        return new String(toByteArray(), charset);
    }

    public boolean isValidUtf8() {
        int leftPartial = this.left.partialIsValidUtf8(0, 0, this.leftLength);
        int state = this.right;
        if (state.partialIsValidUtf8(leftPartial, 0, state.size()) == 0) {
            return true;
        }
        return false;
    }

    /* Access modifiers changed, original: protected */
    public int partialIsValidUtf8(int state, int offset, int length) {
        int toIndex = offset + length;
        int leftLength = this.leftLength;
        if (toIndex <= leftLength) {
            return this.left.partialIsValidUtf8(state, offset, length);
        }
        if (offset >= leftLength) {
            return this.right.partialIsValidUtf8(state, offset - leftLength, length);
        }
        leftLength -= offset;
        return this.right.partialIsValidUtf8(this.left.partialIsValidUtf8(state, offset, leftLength), 0, length - leftLength);
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ByteString)) {
            return false;
        }
        ByteString otherByteString = (ByteString) other;
        if (this.totalLength != otherByteString.size()) {
            return false;
        }
        if (this.totalLength == 0) {
            return true;
        }
        int thisHash = peekCachedHashCode();
        int thatHash = otherByteString.peekCachedHashCode();
        if (thisHash == 0 || thatHash == 0 || thisHash == thatHash) {
            return equalsFragments(otherByteString);
        }
        return false;
    }

    private boolean equalsFragments(ByteString other) {
        int thisOffset = 0;
        Iterator<LeafByteString> thisIter = new PieceIterator(this);
        LeafByteString thisString = (LeafByteString) thisIter.next();
        int thatOffset = 0;
        PieceIterator thatIter = new PieceIterator(other);
        LeafByteString thatString = (LeafByteString) thatIter.next();
        int pos = 0;
        while (true) {
            boolean stillEqual;
            int thisRemaining = thisString.size() - thisOffset;
            int thatRemaining = thatString.size() - thatOffset;
            int bytesToCompare = Math.min(thisRemaining, thatRemaining);
            if (thisOffset == 0) {
                stillEqual = thisString.equalsRange(thatString, thatOffset, bytesToCompare);
            } else {
                stillEqual = thatString.equalsRange(thisString, thisOffset, bytesToCompare);
            }
            if (!stillEqual) {
                return false;
            }
            pos += bytesToCompare;
            int i = this.totalLength;
            if (pos < i) {
                if (bytesToCompare == thisRemaining) {
                    thisOffset = 0;
                    thisString = (LeafByteString) thisIter.next();
                } else {
                    thisOffset += bytesToCompare;
                }
                if (bytesToCompare == thatRemaining) {
                    thatOffset = 0;
                    thatString = (LeafByteString) thatIter.next();
                } else {
                    thatOffset += bytesToCompare;
                }
            } else if (pos == i) {
                return true;
            } else {
                throw new IllegalStateException();
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public int partialHash(int h, int offset, int length) {
        int toIndex = offset + length;
        int leftLength = this.leftLength;
        if (toIndex <= leftLength) {
            return this.left.partialHash(h, offset, length);
        }
        if (offset >= leftLength) {
            return this.right.partialHash(h, offset - leftLength, length);
        }
        leftLength -= offset;
        return this.right.partialHash(this.left.partialHash(h, offset, leftLength), 0, length - leftLength);
    }

    public CodedInputStream newCodedInput() {
        return CodedInputStream.newInstance(new RopeInputStream());
    }

    public InputStream newInput() {
        return new RopeInputStream();
    }

    /* Access modifiers changed, original: 0000 */
    public Object writeReplace() {
        return ByteString.wrap(toByteArray());
    }

    private void readObject(ObjectInputStream in) throws IOException {
        throw new InvalidObjectException("RopeByteStream instances are not to be serialized directly");
    }
}
