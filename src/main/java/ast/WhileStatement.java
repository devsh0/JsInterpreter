package ast;

public class WhileStatement extends LoopStatement {
    @Override
    public Object execute() {
        return super.execute();
    }

    @Override
    public String getPrettyString(int indent) {
        var builder = new StringBuilder("\n");

        if (label != null) {
            builder.append(" ".repeat(indent));
            builder.append(label);
            builder.append(":\n");
        }

        builder.append(" ".repeat(indent));
        builder.append("while (");
        var conditionString = conditionStatement.getPrettyString(indent).trim();
        conditionString = conditionString.replace(";", "");
        builder.append(conditionString);
        builder.append(")");
        builder.append(this.body.getPrettyString(indent));
        return builder.toString();
    }
}
