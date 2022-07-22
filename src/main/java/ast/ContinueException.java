package ast;

public class ContinueException extends EarlyExitException {
    public ContinueException(CompoundStatement target) {
        super(target);
    }
}
