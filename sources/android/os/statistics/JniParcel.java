package android.os.statistics;

final class JniParcel {
    private final long[] numbers;
    private int numbersCount = 0;
    private final Object[] objects;
    private int objectsCount;
    private int readPosOfNumbers = 0;
    private int readPosOfObjects;

    public JniParcel(int numbersCapacity, int objectsCapacity) {
        this.numbers = new long[numbersCapacity];
        this.objects = new Object[objectsCapacity];
        this.objectsCount = 0;
        this.readPosOfObjects = 0;
    }

    public final void reset() {
        this.numbersCount = 0;
        this.readPosOfNumbers = 0;
        int localObjectsCount = this.objectsCount;
        this.objectsCount = 0;
        this.readPosOfObjects = 0;
        for (int i = 0; i < localObjectsCount; i++) {
            this.objects[i] = null;
        }
    }

    public final int readInt() {
        int localReadPosOfNumbers = this.readPosOfNumbers;
        this.readPosOfNumbers++;
        return (int) this.numbers[localReadPosOfNumbers];
    }

    public final long readLong() {
        int localReadPosOfNumbers = this.readPosOfNumbers;
        this.readPosOfNumbers++;
        return this.numbers[localReadPosOfNumbers];
    }

    public final Object readObject() {
        int localReadPosOfObjects = this.readPosOfObjects;
        this.readPosOfObjects++;
        return this.objects[localReadPosOfObjects];
    }

    public final String readString() {
        int localReadPosOfObjects = this.readPosOfObjects;
        this.readPosOfObjects++;
        String str = this.objects[localReadPosOfObjects];
        return str != null ? str : "";
    }

    private void writeLong1(long value1) {
        this.numbers[this.numbersCount] = value1;
        this.numbersCount++;
    }

    private void writeLong2(long value1, long value2) {
        int localNumbersCount = this.numbersCount;
        long[] jArr = this.numbers;
        jArr[localNumbersCount] = value1;
        jArr[localNumbersCount + 1] = value2;
        this.numbersCount += 2;
    }

    private void writeLong3(long value1, long value2, long value3) {
        int localNumbersCount = this.numbersCount;
        long[] jArr = this.numbers;
        jArr[localNumbersCount] = value1;
        jArr[localNumbersCount + 1] = value2;
        jArr[localNumbersCount + 2] = value3;
        this.numbersCount += 3;
    }

    private void writeLong4(long value1, long value2, long value3, long value4) {
        int localNumbersCount = this.numbersCount;
        long[] jArr = this.numbers;
        jArr[localNumbersCount] = value1;
        jArr[localNumbersCount + 1] = value2;
        jArr[localNumbersCount + 2] = value3;
        jArr[localNumbersCount + 3] = value4;
        this.numbersCount += 4;
    }

    private void writeLong5(long value1, long value2, long value3, long value4, long value5) {
        int localNumbersCount = this.numbersCount;
        long[] jArr = this.numbers;
        jArr[localNumbersCount] = value1;
        jArr[localNumbersCount + 1] = value2;
        jArr[localNumbersCount + 2] = value3;
        jArr[localNumbersCount + 3] = value4;
        jArr[localNumbersCount + 4] = value5;
        this.numbersCount += 5;
    }

    private void writeLong6(long value1, long value2, long value3, long value4, long value5, long value6) {
        int localNumbersCount = this.numbersCount;
        long[] jArr = this.numbers;
        jArr[localNumbersCount] = value1;
        jArr[localNumbersCount + 1] = value2;
        jArr[localNumbersCount + 2] = value3;
        jArr[localNumbersCount + 3] = value4;
        jArr[localNumbersCount + 4] = value5;
        jArr[localNumbersCount + 5] = value6;
        this.numbersCount += 6;
    }

    private void writeLong7(long value1, long value2, long value3, long value4, long value5, long value6, long value7) {
        int localNumbersCount = this.numbersCount;
        long[] jArr = this.numbers;
        jArr[localNumbersCount] = value1;
        jArr[localNumbersCount + 1] = value2;
        jArr[localNumbersCount + 2] = value3;
        jArr[localNumbersCount + 3] = value4;
        jArr[localNumbersCount + 4] = value5;
        jArr[localNumbersCount + 5] = value6;
        jArr[localNumbersCount + 6] = value7;
        this.numbersCount += 7;
    }

    private void writeLong8(long value1, long value2, long value3, long value4, long value5, long value6, long value7, long value8) {
        int localNumbersCount = this.numbersCount;
        long[] jArr = this.numbers;
        jArr[localNumbersCount] = value1;
        jArr[localNumbersCount + 1] = value2;
        jArr[localNumbersCount + 2] = value3;
        jArr[localNumbersCount + 3] = value4;
        jArr[localNumbersCount + 4] = value5;
        jArr[localNumbersCount + 5] = value6;
        jArr[localNumbersCount + 6] = value7;
        jArr[localNumbersCount + 7] = value8;
        this.numbersCount += 8;
    }

    private void writeLong9(long value1, long value2, long value3, long value4, long value5, long value6, long value7, long value8, long value9) {
        int localNumbersCount = this.numbersCount;
        long[] jArr = this.numbers;
        jArr[localNumbersCount] = value1;
        jArr[localNumbersCount + 1] = value2;
        jArr[localNumbersCount + 2] = value3;
        jArr[localNumbersCount + 3] = value4;
        jArr[localNumbersCount + 4] = value5;
        jArr[localNumbersCount + 5] = value6;
        jArr[localNumbersCount + 6] = value7;
        jArr[localNumbersCount + 7] = value8;
        jArr[localNumbersCount + 8] = value9;
        this.numbersCount += 9;
    }

    private void writeLong10(long value1, long value2, long value3, long value4, long value5, long value6, long value7, long value8, long value9, long value10) {
        int localNumbersCount = this.numbersCount;
        long[] jArr = this.numbers;
        jArr[localNumbersCount] = value1;
        jArr[localNumbersCount + 1] = value2;
        jArr[localNumbersCount + 2] = value3;
        jArr[localNumbersCount + 3] = value4;
        jArr[localNumbersCount + 4] = value5;
        jArr[localNumbersCount + 5] = value6;
        jArr[localNumbersCount + 6] = value7;
        jArr[localNumbersCount + 7] = value8;
        jArr[localNumbersCount + 8] = value9;
        jArr[localNumbersCount + 9] = value10;
        this.numbersCount += 10;
    }

    private void writeLong11(long value1, long value2, long value3, long value4, long value5, long value6, long value7, long value8, long value9, long value10, long value11) {
        int localNumbersCount = this.numbersCount;
        long[] jArr = this.numbers;
        jArr[localNumbersCount] = value1;
        jArr[localNumbersCount + 1] = value2;
        jArr[localNumbersCount + 2] = value3;
        jArr[localNumbersCount + 3] = value4;
        jArr[localNumbersCount + 4] = value5;
        jArr[localNumbersCount + 5] = value6;
        jArr[localNumbersCount + 6] = value7;
        jArr[localNumbersCount + 7] = value8;
        jArr[localNumbersCount + 8] = value9;
        jArr[localNumbersCount + 9] = value10;
        jArr[localNumbersCount + 10] = value11;
        this.numbersCount += 11;
    }

    private void writeLong12(long value1, long value2, long value3, long value4, long value5, long value6, long value7, long value8, long value9, long value10, long value11, long value12) {
        int localNumbersCount = this.numbersCount;
        long[] jArr = this.numbers;
        jArr[localNumbersCount] = value1;
        jArr[localNumbersCount + 1] = value2;
        jArr[localNumbersCount + 2] = value3;
        jArr[localNumbersCount + 3] = value4;
        jArr[localNumbersCount + 4] = value5;
        jArr[localNumbersCount + 5] = value6;
        jArr[localNumbersCount + 6] = value7;
        jArr[localNumbersCount + 7] = value8;
        jArr[localNumbersCount + 8] = value9;
        jArr[localNumbersCount + 9] = value10;
        jArr[localNumbersCount + 10] = value11;
        jArr[localNumbersCount + 11] = value12;
        this.numbersCount += 12;
    }

    private void writeObject1(Object object1) {
        this.objects[this.objectsCount] = object1;
        this.objectsCount++;
    }

    private void writeObject2(Object object1, Object object2) {
        int localObjectsCount = this.objectsCount;
        Object[] objArr = this.objects;
        objArr[localObjectsCount] = object1;
        objArr[localObjectsCount + 1] = object2;
        this.objectsCount += 2;
    }

    private void writeObject3(Object object1, Object object2, Object object3) {
        int localObjectsCount = this.objectsCount;
        Object[] objArr = this.objects;
        objArr[localObjectsCount] = object1;
        objArr[localObjectsCount + 1] = object2;
        objArr[localObjectsCount + 2] = object3;
        this.objectsCount += 3;
    }

    private void writeObject4(Object object1, Object object2, Object object3, Object object4) {
        int localObjectsCount = this.objectsCount;
        Object[] objArr = this.objects;
        objArr[localObjectsCount] = object1;
        objArr[localObjectsCount + 1] = object2;
        objArr[localObjectsCount + 2] = object3;
        objArr[localObjectsCount + 3] = object4;
        this.objectsCount += 4;
    }

    private void writeObject5(Object object1, Object object2, Object object3, Object object4, Object object5) {
        int localObjectsCount = this.objectsCount;
        Object[] objArr = this.objects;
        objArr[localObjectsCount] = object1;
        objArr[localObjectsCount + 1] = object2;
        objArr[localObjectsCount + 2] = object3;
        objArr[localObjectsCount + 3] = object4;
        objArr[localObjectsCount + 4] = object5;
        this.objectsCount += 5;
    }

    private void writeObject6(Object object1, Object object2, Object object3, Object object4, Object object5, Object object6) {
        int localObjectsCount = this.objectsCount;
        Object[] objArr = this.objects;
        objArr[localObjectsCount] = object1;
        objArr[localObjectsCount + 1] = object2;
        objArr[localObjectsCount + 2] = object3;
        objArr[localObjectsCount + 3] = object4;
        objArr[localObjectsCount + 4] = object5;
        objArr[localObjectsCount + 5] = object6;
        this.objectsCount += 6;
    }

    private void writeObject7(Object object1, Object object2, Object object3, Object object4, Object object5, Object object6, Object object7) {
        int localObjectsCount = this.objectsCount;
        Object[] objArr = this.objects;
        objArr[localObjectsCount] = object1;
        objArr[localObjectsCount + 1] = object2;
        objArr[localObjectsCount + 2] = object3;
        objArr[localObjectsCount + 3] = object4;
        objArr[localObjectsCount + 4] = object5;
        objArr[localObjectsCount + 5] = object6;
        objArr[localObjectsCount + 6] = object7;
        this.objectsCount += 7;
    }

    private void writeObject8(Object object1, Object object2, Object object3, Object object4, Object object5, Object object6, Object object7, Object object8) {
        int localObjectsCount = this.objectsCount;
        Object[] objArr = this.objects;
        objArr[localObjectsCount] = object1;
        objArr[localObjectsCount + 1] = object2;
        objArr[localObjectsCount + 2] = object3;
        objArr[localObjectsCount + 3] = object4;
        objArr[localObjectsCount + 4] = object5;
        objArr[localObjectsCount + 5] = object6;
        objArr[localObjectsCount + 6] = object7;
        objArr[localObjectsCount + 7] = object8;
        this.objectsCount += 8;
    }

    private void writeObject9(Object object1, Object object2, Object object3, Object object4, Object object5, Object object6, Object object7, Object object8, Object object9) {
        int localObjectsCount = this.objectsCount;
        Object[] objArr = this.objects;
        objArr[localObjectsCount] = object1;
        objArr[localObjectsCount + 1] = object2;
        objArr[localObjectsCount + 2] = object3;
        objArr[localObjectsCount + 3] = object4;
        objArr[localObjectsCount + 4] = object5;
        objArr[localObjectsCount + 5] = object6;
        objArr[localObjectsCount + 6] = object7;
        objArr[localObjectsCount + 7] = object8;
        objArr[localObjectsCount + 8] = object9;
        this.objectsCount += 9;
    }
}
