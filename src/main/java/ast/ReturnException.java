package ast;

public class ReturnException extends EarlyExitException {
    private Object returnValue;

    public ReturnException(CompoundStatement target, Object returnValue) {
        super(target);
        this.returnValue = returnValue;
    }

    public Object getReturnValue() {
        return returnValue;
    }
}
