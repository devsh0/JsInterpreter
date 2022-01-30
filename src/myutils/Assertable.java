package myutils;

public interface Assertable {
    static void _assert(final boolean condition) {
        if (!condition)
            throw new AssertionError("Assertion failed!");
    }

    default void Assert(final boolean condition) {
        _assert(condition);
    }
}
