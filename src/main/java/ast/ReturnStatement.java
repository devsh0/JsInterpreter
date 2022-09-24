package ast;

import java.util.Objects;

public class ReturnStatement implements Statement {

    public ReturnStatement(FunctionDeclaration ref) {
        functionRef = ref;
    }

    @Override
    public Object execute() {
        throw new ReturnException(functionRef, expression.execute());
    }

    public ReturnStatement setExpression(final Expression expression) {
        Objects.requireNonNull(expression);
        this.expression = expression;
        return this;
    }

    @Override
    public String getPrettyString(int indent) {
        var builder = new StringBuilder("\n");
        builder.append(" ".repeat(indent));
        builder.append("return");
        if (expression != null) {
            builder.append(" ");
            builder.append(expression.getPrettyString(indent));
        }
        builder.append(";");
        return builder.toString();
    }

    public FunctionDeclaration getFunctionRef() {
        return functionRef;
    }

    private Expression expression;
    private FunctionDeclaration functionRef;
}
