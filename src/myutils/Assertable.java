package myutils;

public interface Assertable {
    static void _ASSERT(final boolean condition) {
        if (!condition)
            throw new AssertionError("Assertion failed!");
    }

    static void _ASSERT_NOT_REACHED() {
        System.err.println("Execution wasn't supposed to reach here!");
        _ASSERT(false);
    }

    static void _FIXME_REPORT_SYNTAX_ERROR() {
        System.err.println("Expecting a syntax error here!");
        _ASSERT(false);
    }

    static void _FIXME_UNIMPLEMENTED() {
        System.err.println("This feature is yet to be implemented!");
        _ASSERT(false);
    }

    static void _VERIFY(boolean condition) {
        if (!condition) {
            System.err.println("Verification failed bro!");
            _ASSERT(false);
        }
    }

    default void VERIFY(boolean condition) {
        _VERIFY(condition);
    }

    default void ASSERT(final boolean condition) {
        _ASSERT(condition);
    }

    default void FIXME_UNIMPLEMENTED() {
        _FIXME_UNIMPLEMENTED();
    }

    default void FIXME_REPORT_SYNTAX_ERROR() {
        _FIXME_REPORT_SYNTAX_ERROR();
    }

    default void ASSERT_NOT_REACHED() {
        _ASSERT_NOT_REACHED();
    }
}
