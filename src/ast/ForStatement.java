package ast;

public class ForStatement extends LoopStatement {
    @Override
    public Object execute() {
        initializer.execute();
        if (this.updateExpression != null)
            this.body.append(new ExpressionStatement().setSource(this.updateExpression));
        return super.execute();
    }

    @Override
    public String getDump(int indent) {
        var builder = getIndentedBuilder(indent);
        builder.append("for (");
        if (initializer != null)
            builder.append(initializer.getDump(0)).append("; ");
        if (conditionExpression != null)
            builder.append(conditionExpression.getDump(0)).append(";");
        if (updateExpression != null)
            builder.append(updateExpression.getDump(0)).append(")");
        builder.append(body.getDump(indent));
        return builder.toString();
    }

    public ForStatement setInitializer(Statement initializer) {
        VERIFY(initializer != null);
        this.initializer = initializer;
        return this;
    }

    public ForStatement setUpdateExpression(Expression expression) {
        this.updateExpression = expression;
        return this;
    }

    public CompoundStatement setLabel(Identifier label) {
        this.label = label;
        return this;
    }

    public Identifier getLabel() {
        return label;
    }

    private Statement initializer;
    private Expression updateExpression;
    private Identifier label;
}
