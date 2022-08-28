package ast;

import java.util.Objects;

public abstract class CompoundStatement implements Statement {
    protected Block body;

    public abstract Object execute();

    public CompoundStatement setBody(Block body) {
        Objects.requireNonNull(body);
        this.body = body;
        return this;
    }

    public Block getBody() {
        return body;
    }

    @Override
    abstract public String getString(int indent);
}
