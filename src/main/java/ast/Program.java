package ast;

import java.util.Objects;

public class Program extends CompoundStatement {
    @Override
    public Object execute() {
        Objects.requireNonNull(body);
        return body.execute();
    }

    @Override
    public String getDump(int indent) {
        var builder = getIndentedBuilder(indent);
        builder.append(body.getDump(-Indent));
        builder = new StringBuilder(builder.substring(builder.indexOf("{") + 1, builder.lastIndexOf("}")));
        return builder.toString();
    }

    public Program setBody(final Block block) {
        this.body = block;
        return this;
    }

    private Block body;
}
