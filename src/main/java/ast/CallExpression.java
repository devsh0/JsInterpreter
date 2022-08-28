package ast;

import org.js.Interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static myutils.Macro.verify;

public class CallExpression extends ExpressionStatement {
    @Override
    public Object execute() {
        Objects.requireNonNull(callee);
        var interpreter = Interpreter.get();
        var functionOrEmpty = interpreter.queryScope(callee);
        verify(functionOrEmpty.isPresent());
        var entity = functionOrEmpty.get();
        verify(entity instanceof FunctionDeclaration);
        var function = (FunctionDeclaration) entity;
        var parameters = function.getParameters();
        var functionBody = function.getBody();
        var functionScope = functionBody.getScope();
        var iterCount = Math.min(arguments.size(), parameters.size());
        for (int i = 0; i < iterCount; i++)
            functionScope.addOrUpdateEntry(parameters.get(i), (ASTNode) arguments.get(i).execute());

        Object returnVal;
        try {
            returnVal = functionBody.execute();
        } catch (ReturnException exception) {
            returnVal = exception.getReturnValue();
            while (!interpreter.exitCurrentScope().equals(functionScope)) ;
        }
        return returnVal;
    }

    public CallExpression setIdentifier(final Identifier id) {
        Objects.requireNonNull(id);
        callee = id;
        return this;
    }

    public CallExpression appendArgument(Expression arg) {
        Objects.requireNonNull(arg);
        arguments.add(arg);
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
