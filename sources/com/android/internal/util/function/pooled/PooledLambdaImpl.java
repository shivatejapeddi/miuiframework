package com.android.internal.util.function.pooled;

import android.os.Message;
import android.text.TextUtils;
import android.util.Pools.SynchronizedPool;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.BitUtils;
import com.android.internal.util.Preconditions;
import com.android.internal.util.function.HeptConsumer;
import com.android.internal.util.function.HeptFunction;
import com.android.internal.util.function.HeptPredicate;
import com.android.internal.util.function.HexConsumer;
import com.android.internal.util.function.HexFunction;
import com.android.internal.util.function.HexPredicate;
import com.android.internal.util.function.NonaConsumer;
import com.android.internal.util.function.NonaFunction;
import com.android.internal.util.function.NonaPredicate;
import com.android.internal.util.function.OctConsumer;
import com.android.internal.util.function.OctFunction;
import com.android.internal.util.function.OctPredicate;
import com.android.internal.util.function.QuadConsumer;
import com.android.internal.util.function.QuadFunction;
import com.android.internal.util.function.QuadPredicate;
import com.android.internal.util.function.QuintConsumer;
import com.android.internal.util.function.QuintFunction;
import com.android.internal.util.function.QuintPredicate;
import com.android.internal.util.function.TriConsumer;
import com.android.internal.util.function.TriFunction;
import com.android.internal.util.function.TriPredicate;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

final class PooledLambdaImpl<R> extends OmniFunction<Object, Object, Object, Object, Object, Object, Object, Object, Object, R> {
    private static final boolean DEBUG = false;
    private static final int FLAG_ACQUIRED_FROM_MESSAGE_CALLBACKS_POOL = 2048;
    private static final int FLAG_RECYCLED = 512;
    private static final int FLAG_RECYCLE_ON_USE = 1024;
    private static final String LOG_TAG = "PooledLambdaImpl";
    static final int MASK_EXPOSED_AS = 520192;
    static final int MASK_FUNC_TYPE = 66584576;
    private static final int MAX_ARGS = 9;
    private static final int MAX_POOL_SIZE = 50;
    static final Pool sMessageCallbacksPool = new Pool(Message.sPoolSync);
    static final Pool sPool = new Pool(new Object());
    Object[] mArgs = null;
    long mConstValue;
    int mFlags = 0;
    Object mFunc;

    static class LambdaType {
        public static final int MASK = 127;
        public static final int MASK_ARG_COUNT = 15;
        public static final int MASK_BIT_COUNT = 7;
        public static final int MASK_RETURN_TYPE = 112;

        static class ReturnType {
            public static final int BOOLEAN = 2;
            public static final int DOUBLE = 6;
            public static final int INT = 4;
            public static final int LONG = 5;
            public static final int OBJECT = 3;
            public static final int VOID = 1;

            ReturnType() {
            }

            static String toString(int returnType) {
                switch (returnType) {
                    case 1:
                        return "VOID";
                    case 2:
                        return "BOOLEAN";
                    case 3:
                        return "OBJECT";
                    case 4:
                        return "INT";
                    case 5:
                        return "LONG";
                    case 6:
                        return "DOUBLE";
                    default:
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("");
                        stringBuilder.append(returnType);
                        return stringBuilder.toString();
                }
            }

            static String lambdaSuffix(int type) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(prefix(type));
                stringBuilder.append(suffix(type));
                return stringBuilder.toString();
            }

            private static String prefix(int type) {
                if (type == 4) {
                    return "Int";
                }
                if (type == 5) {
                    return "Long";
                }
                if (type != 6) {
                    return "";
                }
                return "Double";
            }

            private static String suffix(int type) {
                if (type == 1) {
                    return "Consumer";
                }
                if (type == 2) {
                    return "Predicate";
                }
                if (type != 3) {
                    return "Supplier";
                }
                return "Function";
            }
        }

        LambdaType() {
        }

        static int encode(int argCount, int returnType) {
            return PooledLambdaImpl.mask(15, argCount) | PooledLambdaImpl.mask(112, returnType);
        }

        static int decodeArgCount(int type) {
            return type & 15;
        }

        static int decodeReturnType(int type) {
            return PooledLambdaImpl.unmask(112, type);
        }

        static String toString(int type) {
            int argCount = decodeArgCount(type);
            int returnType = decodeReturnType(type);
            if (argCount == 0) {
                if (returnType == 1) {
                    return "Runnable";
                }
                if (returnType == 3 || returnType == 2) {
                    return "Supplier";
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(argCountPrefix(argCount));
            stringBuilder.append(ReturnType.lambdaSuffix(returnType));
            return stringBuilder.toString();
        }

        private static String argCountPrefix(int argCount) {
            String str = "";
            if (argCount == 15) {
                return str;
            }
            switch (argCount) {
                case 0:
                    return str;
                case 1:
                    return str;
                case 2:
                    return "Bi";
                case 3:
                    return "Tri";
                case 4:
                    return "Quad";
                case 5:
                    return "Quint";
                case 6:
                    return "Hex";
                case 7:
                    return "Hept";
                case 8:
                    return "Oct";
                case 9:
                    return "Nona";
                default:
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append(argCount);
                    stringBuilder.append("arg");
                    return stringBuilder.toString();
            }
        }
    }

    static class Pool extends SynchronizedPool<PooledLambdaImpl> {
        public Pool(Object lock) {
            super(50, lock);
        }
    }

    private PooledLambdaImpl() {
    }

    public void recycle() {
        if (!isRecycled()) {
            doRecycle();
        }
    }

    private void doRecycle() {
        Pool pool;
        if ((this.mFlags & 2048) != 0) {
            pool = sMessageCallbacksPool;
        } else {
            pool = sPool;
        }
        this.mFunc = null;
        Object[] objArr = this.mArgs;
        if (objArr != null) {
            Arrays.fill(objArr, null);
        }
        this.mFlags = 512;
        this.mConstValue = 0;
        pool.release(this);
    }

    /* Access modifiers changed, original: 0000 */
    public R invoke(Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9) {
        checkNotRecycled();
        if (fillInArg(a1) && fillInArg(a2) && fillInArg(a3) && fillInArg(a4) && fillInArg(a5) && fillInArg(a6) && fillInArg(a7) && fillInArg(a8) && fillInArg(a9)) {
        }
        int argCount = LambdaType.decodeArgCount(getFlags(MASK_FUNC_TYPE));
        if (argCount != 15) {
            int i = 0;
            while (i < argCount) {
                if (this.mArgs[i] != ArgumentPlaceholder.INSTANCE) {
                    i++;
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Missing argument #");
                    stringBuilder.append(i);
                    stringBuilder.append(" among ");
                    stringBuilder.append(Arrays.toString(this.mArgs));
                    throw new IllegalStateException(stringBuilder.toString());
                }
            }
        }
        int argsSize;
        int i2;
        try {
            Object doInvoke = doInvoke();
            if (isRecycleOnUse()) {
                doRecycle();
            }
            if (!isRecycled()) {
                argsSize = ArrayUtils.size(this.mArgs);
                for (i2 = 0; i2 < argsSize; i2++) {
                    popArg(i2);
                }
            }
            return doInvoke;
        } catch (Throwable th) {
            if (isRecycleOnUse()) {
                doRecycle();
            }
            if (!isRecycled()) {
                argsSize = ArrayUtils.size(this.mArgs);
                for (i2 = 0; i2 < argsSize; i2++) {
                    popArg(i2);
                }
            }
        }
    }

    private boolean fillInArg(Object invocationArg) {
        int argsSize = ArrayUtils.size(this.mArgs);
        for (int i = 0; i < argsSize; i++) {
            if (this.mArgs[i] == ArgumentPlaceholder.INSTANCE) {
                this.mArgs[i] = invocationArg;
                this.mFlags = (int) (((long) this.mFlags) | BitUtils.bitAt(i));
                return true;
            }
        }
        if (invocationArg == null || invocationArg == ArgumentPlaceholder.INSTANCE) {
            return false;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("No more arguments expected for provided arg ");
        stringBuilder.append(invocationArg);
        stringBuilder.append(" among ");
        stringBuilder.append(Arrays.toString(this.mArgs));
        throw new IllegalStateException(stringBuilder.toString());
    }

    private void checkNotRecycled() {
        if (isRecycled()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Instance is recycled: ");
            stringBuilder.append(this);
            throw new IllegalStateException(stringBuilder.toString());
        }
    }

    private R doInvoke() {
        int funcType = getFlags(MASK_FUNC_TYPE);
        int argCount = LambdaType.decodeArgCount(funcType);
        int returnType = LambdaType.decodeReturnType(funcType);
        if (argCount != 15) {
            switch (argCount) {
                case 0:
                    if (returnType == 1) {
                        ((Runnable) this.mFunc).run();
                        return null;
                    } else if (returnType == 2 || returnType == 3) {
                        return ((Supplier) this.mFunc).get();
                    }
                case 1:
                    if (returnType == 1) {
                        ((Consumer) this.mFunc).accept(popArg(0));
                        return null;
                    } else if (returnType == 2) {
                        return Boolean.valueOf(((Predicate) this.mFunc).test(popArg(0)));
                    } else {
                        if (returnType == 3) {
                            return ((Function) this.mFunc).apply(popArg(0));
                        }
                    }
                    break;
                case 2:
                    if (returnType == 1) {
                        ((BiConsumer) this.mFunc).accept(popArg(0), popArg(1));
                        return null;
                    } else if (returnType == 2) {
                        return Boolean.valueOf(((BiPredicate) this.mFunc).test(popArg(0), popArg(1)));
                    } else {
                        if (returnType == 3) {
                            return ((BiFunction) this.mFunc).apply(popArg(0), popArg(1));
                        }
                    }
                    break;
                case 3:
                    if (returnType == 1) {
                        ((TriConsumer) this.mFunc).accept(popArg(0), popArg(1), popArg(2));
                        return null;
                    } else if (returnType == 2) {
                        return Boolean.valueOf(((TriPredicate) this.mFunc).test(popArg(0), popArg(1), popArg(2)));
                    } else {
                        if (returnType == 3) {
                            return ((TriFunction) this.mFunc).apply(popArg(0), popArg(1), popArg(2));
                        }
                    }
                    break;
                case 4:
                    if (returnType == 1) {
                        ((QuadConsumer) this.mFunc).accept(popArg(0), popArg(1), popArg(2), popArg(3));
                        return null;
                    } else if (returnType == 2) {
                        return Boolean.valueOf(((QuadPredicate) this.mFunc).test(popArg(0), popArg(1), popArg(2), popArg(3)));
                    } else {
                        if (returnType == 3) {
                            return ((QuadFunction) this.mFunc).apply(popArg(0), popArg(1), popArg(2), popArg(3));
                        }
                    }
                    break;
                case 5:
                    if (returnType == 1) {
                        ((QuintConsumer) this.mFunc).accept(popArg(0), popArg(1), popArg(2), popArg(3), popArg(4));
                        return null;
                    } else if (returnType == 2) {
                        return Boolean.valueOf(((QuintPredicate) this.mFunc).test(popArg(0), popArg(1), popArg(2), popArg(3), popArg(4)));
                    } else {
                        if (returnType == 3) {
                            return ((QuintFunction) this.mFunc).apply(popArg(0), popArg(1), popArg(2), popArg(3), popArg(4));
                        }
                    }
                    break;
                case 6:
                    if (returnType == 1) {
                        ((HexConsumer) this.mFunc).accept(popArg(0), popArg(1), popArg(2), popArg(3), popArg(4), popArg(5));
                        return null;
                    } else if (returnType == 2) {
                        return Boolean.valueOf(((HexPredicate) this.mFunc).test(popArg(0), popArg(1), popArg(2), popArg(3), popArg(4), popArg(5)));
                    } else {
                        if (returnType == 3) {
                            return ((HexFunction) this.mFunc).apply(popArg(0), popArg(1), popArg(2), popArg(3), popArg(4), popArg(5));
                        }
                    }
                    break;
                case 7:
                    if (returnType == 1) {
                        ((HeptConsumer) this.mFunc).accept(popArg(0), popArg(1), popArg(2), popArg(3), popArg(4), popArg(5), popArg(6));
                        return null;
                    } else if (returnType == 2) {
                        return Boolean.valueOf(((HeptPredicate) this.mFunc).test(popArg(0), popArg(1), popArg(2), popArg(3), popArg(4), popArg(5), popArg(6)));
                    } else {
                        if (returnType == 3) {
                            return ((HeptFunction) this.mFunc).apply(popArg(0), popArg(1), popArg(2), popArg(3), popArg(4), popArg(5), popArg(6));
                        }
                    }
                    break;
                case 8:
                    if (returnType == 1) {
                        ((OctConsumer) this.mFunc).accept(popArg(0), popArg(1), popArg(2), popArg(3), popArg(4), popArg(5), popArg(6), popArg(7));
                        return null;
                    } else if (returnType == 2) {
                        return Boolean.valueOf(((OctPredicate) this.mFunc).test(popArg(0), popArg(1), popArg(2), popArg(3), popArg(4), popArg(5), popArg(6), popArg(7)));
                    } else {
                        if (returnType == 3) {
                            return ((OctFunction) this.mFunc).apply(popArg(0), popArg(1), popArg(2), popArg(3), popArg(4), popArg(5), popArg(6), popArg(7));
                        }
                    }
                    break;
                case 9:
                    if (returnType == 1) {
                        ((NonaConsumer) this.mFunc).accept(popArg(0), popArg(1), popArg(2), popArg(3), popArg(4), popArg(5), popArg(6), popArg(7), popArg(8));
                        return null;
                    } else if (returnType == 2) {
                        return Boolean.valueOf(((NonaPredicate) this.mFunc).test(popArg(0), popArg(1), popArg(2), popArg(3), popArg(4), popArg(5), popArg(6), popArg(7), popArg(8)));
                    } else {
                        if (returnType == 3) {
                            return ((NonaFunction) this.mFunc).apply(popArg(0), popArg(1), popArg(2), popArg(3), popArg(4), popArg(5), popArg(6), popArg(7), popArg(8));
                        }
                    }
                    break;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown function type: ");
            stringBuilder.append(LambdaType.toString(funcType));
            throw new IllegalStateException(stringBuilder.toString());
        } else if (returnType == 4) {
            return Integer.valueOf(getAsInt());
        } else {
            if (returnType == 5) {
                return Long.valueOf(getAsLong());
            }
            if (returnType != 6) {
                return this.mFunc;
            }
            return Double.valueOf(getAsDouble());
        }
    }

    private boolean isConstSupplier() {
        return LambdaType.decodeArgCount(getFlags(MASK_FUNC_TYPE)) == 15;
    }

    private Object popArg(int index) {
        Object result = this.mArgs[index];
        if (isInvocationArgAtIndex(index)) {
            this.mArgs[index] = ArgumentPlaceholder.INSTANCE;
            this.mFlags = (int) (((long) this.mFlags) & (~BitUtils.bitAt(index)));
        }
        return result;
    }

    public String toString() {
        StringBuilder stringBuilder;
        if (isRecycled()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("<recycled PooledLambda@");
            stringBuilder.append(hashCodeHex(this));
            stringBuilder.append(">");
            return stringBuilder.toString();
        }
        stringBuilder = new StringBuilder();
        String str = ")";
        String str2 = "(";
        if (isConstSupplier()) {
            stringBuilder.append(getFuncTypeAsString());
            stringBuilder.append(str2);
            stringBuilder.append(doInvoke());
            stringBuilder.append(str);
        } else {
            Object func = this.mFunc;
            if (func instanceof PooledLambdaImpl) {
                stringBuilder.append(func);
            } else {
                stringBuilder.append(getFuncTypeAsString());
                stringBuilder.append("@");
                stringBuilder.append(hashCodeHex(func));
            }
            stringBuilder.append(str2);
            stringBuilder.append(commaSeparateFirstN(this.mArgs, LambdaType.decodeArgCount(getFlags(MASK_FUNC_TYPE))));
            stringBuilder.append(str);
        }
        return stringBuilder.toString();
    }

    private String commaSeparateFirstN(Object[] arr, int n) {
        if (arr == null) {
            return "";
        }
        return TextUtils.join((CharSequence) ",", Arrays.copyOf(arr, n));
    }

    private static String hashCodeHex(Object o) {
        return Integer.toHexString(Objects.hashCode(o));
    }

    private String getFuncTypeAsString() {
        if (isRecycled()) {
            return "<recycled>";
        }
        String str = "supplier";
        if (isConstSupplier()) {
            return str;
        }
        String name = LambdaType.toString(getFlags(MASK_EXPOSED_AS));
        if (name.endsWith("Consumer")) {
            return "consumer";
        }
        if (name.endsWith("Function")) {
            return "function";
        }
        if (name.endsWith("Predicate")) {
            return "predicate";
        }
        if (name.endsWith("Supplier")) {
            return str;
        }
        if (name.endsWith("Runnable")) {
            return "runnable";
        }
        return name;
    }

    static <E extends PooledLambda> E acquire(Pool pool, Object func, int fNumArgs, int numPlaceholders, int fReturnType, Object a, Object b, Object c, Object d, Object e, Object f, Object g, Object h, Object i) {
        int i2 = fNumArgs;
        PooledLambdaImpl r = acquire(pool);
        r.mFunc = Preconditions.checkNotNull(func);
        r.setFlags(MASK_FUNC_TYPE, LambdaType.encode(i2, fReturnType));
        r.setFlags(MASK_EXPOSED_AS, LambdaType.encode(numPlaceholders, fReturnType));
        if (ArrayUtils.size(r.mArgs) < i2) {
            r.mArgs = new Object[i2];
        }
        setIfInBounds(r.mArgs, 0, a);
        setIfInBounds(r.mArgs, 1, b);
        setIfInBounds(r.mArgs, 2, c);
        setIfInBounds(r.mArgs, 3, d);
        setIfInBounds(r.mArgs, 4, e);
        setIfInBounds(r.mArgs, 5, f);
        setIfInBounds(r.mArgs, 6, g);
        setIfInBounds(r.mArgs, 7, h);
        setIfInBounds(r.mArgs, 8, i);
        return r;
    }

    static PooledLambdaImpl acquireConstSupplier(int type) {
        PooledLambdaImpl r = acquire(sPool);
        int lambdaType = LambdaType.encode(15, type);
        r.setFlags(MASK_FUNC_TYPE, lambdaType);
        r.setFlags(MASK_EXPOSED_AS, lambdaType);
        return r;
    }

    static PooledLambdaImpl acquire(Pool pool) {
        PooledLambdaImpl r = (PooledLambdaImpl) pool.acquire();
        if (r == null) {
            r = new PooledLambdaImpl();
        }
        r.mFlags &= -513;
        r.setFlags(2048, pool == sMessageCallbacksPool ? 1 : 0);
        return r;
    }

    private static void setIfInBounds(Object[] array, int i, Object a) {
        if (i < ArrayUtils.size(array)) {
            array[i] = a;
        }
    }

    public OmniFunction<Object, Object, Object, Object, Object, Object, Object, Object, Object, R> negate() {
        throw new UnsupportedOperationException();
    }

    public <V> OmniFunction<Object, Object, Object, Object, Object, Object, Object, Object, Object, V> andThen(Function<? super R, ? extends V> function) {
        throw new UnsupportedOperationException();
    }

    public double getAsDouble() {
        return Double.longBitsToDouble(this.mConstValue);
    }

    public int getAsInt() {
        return (int) this.mConstValue;
    }

    public long getAsLong() {
        return this.mConstValue;
    }

    public OmniFunction<Object, Object, Object, Object, Object, Object, Object, Object, Object, R> recycleOnUse() {
        this.mFlags |= 1024;
        return this;
    }

    private boolean isRecycled() {
        return (this.mFlags & 512) != 0;
    }

    private boolean isRecycleOnUse() {
        return (this.mFlags & 1024) != 0;
    }

    private boolean isInvocationArgAtIndex(int argIndex) {
        return (this.mFlags & (1 << argIndex)) != 0;
    }

    /* Access modifiers changed, original: 0000 */
    public int getFlags(int mask) {
        return unmask(mask, this.mFlags);
    }

    /* Access modifiers changed, original: 0000 */
    public void setFlags(int mask, int value) {
        this.mFlags &= ~mask;
        this.mFlags |= mask(mask, value);
    }

    private static int mask(int mask, int value) {
        return (value << Integer.numberOfTrailingZeros(mask)) & mask;
    }

    private static int unmask(int mask, int bits) {
        return (bits & mask) / (1 << Integer.numberOfTrailingZeros(mask));
    }
}
