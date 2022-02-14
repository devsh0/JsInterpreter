package ast;

public class WhileStatement extends LoopStatement {
    @Override
    public Object execute() {
        return super.execute();
    }

    @Override
    public String getDump(int indent) {
        var builder = getIndentedBuilder(indent);
        builder.append("while (");
        if (conditionExpression != null)
            builder.append(conditionExpression.getDump(0));
        builder.append(")");
        builder.append(body.getDump(indent));
        return builder.toString();
    }
}
