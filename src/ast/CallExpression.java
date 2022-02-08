package ast;

import org.js.Interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CallExpression extends ExpressionStatement {
    @Override
    public Object execute() {
        Objects.requireNonNull(callee);
        var interpreter = Interpreter.get();
        var functionOrEmpty = interpreter.queryScope(callee);
        ASSERT(functionOrEmpty.isPresent());
        var entity = functionOrEmpty.get();
        ASSERT(entity instanceof FunctionDeclaration);
        var function = (FunctionDeclaration) entity;
        var parameters = function.getParameters();
        var functionBody = function.getBody();
        var scope = functionBody.getScope();
        var iterCount = Math.min(arguments.size(), parameters.size());
        for (int i = 0; i < iterCount; i++)
            scope.addEntry(parameters.get(i), (ASTNode)arguments.get(i).execute());

        Object returnVal;
        try {
            returnVal = functionBody.execute();
        } catch (ReturnException exception) {
            returnVal = exception.getReturnValue();
        }
        return returnVal;
    }

    public CallExpression setCallee(final Identifier id) {
        Objects.requireNonNull(id);
        callee = id;
        return this;
    }

    public CallExpression addArgument(Expression arg) {
        Objects.requireNonNull(arg);
        arguments.add(arg);
        return this;
    }

    public CallExpression setArguments(Expression... args) {
        Objects.requireNonNull(args);
        arguments = Arrays.asList(args);
        return this;
    }

    @Override
    public String getDump(int indent) {
        var builder = getIndentedBuilder(indent);
        builder.append(callee.getDump(indent)).append("(");
        for (var arg :arguments)
            builder.append(arg.getDump(indent)).append(", ");
        if (arguments.size() > 0)
            builder = new StringBuilder(builder.substring(0, builder.lastIndexOf(",")));
        return builder.append(")").toString();
    }

    private Identifier callee;
    private List<Expression> arguments = new ArrayList<>();
}
