package ast;

public class BreakException extends EarlyExitException {
    public BreakException(CompoundStatement target) {
        super(target);
    }
}
