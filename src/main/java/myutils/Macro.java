package myutils;

public interface Macro {

    private static void die(String message) {
        throw new AssertionError(message);
    }

    public static void unreachable() {
        die("Execution wasn't supposed to reach here!");
    }

    public static void todo_report_syntax_error() {
        die("Expecting a syntax error here!");
    }

    public static void todo_report_semantic_error() {
        die("Expecting a semantic error here!");
    }

    public static void unimplemented() {
        die("This feature is yet to be implemented!");
    }

    public static void verify(boolean condition) {
        if (!condition)
            die("Verification failed!");
    }
}
