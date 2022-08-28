package ast;

public class ExpressionStatement implements Statement, Expression {
    @Override
    public Object execute() {
        if (expression == null)
            return null;
        return expression.execute();
    }

    public ExpressionStatement setSource(Expression expression) {
        this.expression = expression;
        return this;
    }

    public Expression getSource() {
        return this.expression;
    }

    protected Expression expression;

    @Override
    public String getString(int indent) {
        if (expression == null)
            return "";

        var builder = new StringBuilder("\n");
        builder.append(" ".repeat(indent));
        builder.append(expression.getString(indent));
        builder.append(";");
        return builder.toString();
    }
}
