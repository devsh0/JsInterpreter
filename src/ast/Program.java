package ast;

import java.util.Objects;

public class Program extends CompoundStatement {
    @Override
    public Object execute() {
        Objects.requireNonNull(body);
        return body.execute();
    }

    public Program setBody(final Block block) {
        this.body = block;
        return this;
    }

    private Block body;
}
