package com.android.internal.os;

import java.io.FileDescriptor;
import java.util.function.ToIntFunction;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ZygoteServer$NJVbduCrCzDq0RHpPga7lyCk4eE implements ToIntFunction {
    public static final /* synthetic */ -$$Lambda$ZygoteServer$NJVbduCrCzDq0RHpPga7lyCk4eE INSTANCE = new -$$Lambda$ZygoteServer$NJVbduCrCzDq0RHpPga7lyCk4eE();

    private /* synthetic */ -$$Lambda$ZygoteServer$NJVbduCrCzDq0RHpPga7lyCk4eE() {
    }

    public final int applyAsInt(Object obj) {
        return ((FileDescriptor) obj).getInt$();
    }
}
