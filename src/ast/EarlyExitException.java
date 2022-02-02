package ast;

public class EarlyExitException extends RuntimeException {
    private CompoundStatement target;

    public EarlyExitException(CompoundStatement target) {
        this.target = target;
    }

    public CompoundStatement getTarget() {
        return target;
    }
}
