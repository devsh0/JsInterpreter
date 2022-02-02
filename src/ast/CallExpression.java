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
        Assert(functionOrEmpty.isPresent());
        var entity = functionOrEmpty.get();
        Assert(entity instanceof FunctionDeclaration);
        var function = (FunctionDeclaration) entity;
        var parameters = function.getParameters();
        var functionBody = function.getBody();
        var scope = functionBody.getScope();
        var iterCount = Math.min(arguments.size(), parameters.size());
        for (int i = 0; i < iterCount; i++)
            scope.addEntry(parameters.get(i), arguments.get(i));

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

    public CallExpression setArguments(Expression... args) {
        Objects.requireNonNull(args);
        arguments = Arrays.asList(args);
        return this;
    }

    private Identifier callee;
    private List<Expression> arguments = new ArrayList<>();
}
