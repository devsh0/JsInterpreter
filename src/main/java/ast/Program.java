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

    @Override
    public String getPrettyString(int indent) {
        var bodyStr = this.body.getPrettyString(indent);
        int index = bodyStr.indexOf('{');
        bodyStr = bodyStr.substring(index + 1);
        index = bodyStr.lastIndexOf('}');
        bodyStr = bodyStr.substring(0, index);
        return bodyStr;
    }

    public void prettyPrint() {
        System.out.println(this.getPrettyString(0));
    }

    private Block body;
}
