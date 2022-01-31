package ast;

public class ElseStatement extends CompoundStatement {
    @Override
    public Object execute() {
        Assert(body != null);
        return body.execute();
    }
}
