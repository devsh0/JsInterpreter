package ast;

import ast.value.JSValue;
import org.js.Interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Block implements Statement {
    public Block (ASTNode owner) {
        scope = new Scope(owner);
    }

    private boolean handleFunctionExit(Interpreter interpreter) {
        var funRef = interpreter.getExitingFunctionRef();
        if (funRef == null)
            return false;
        var ownerOrEmpty = scope.getOwner();
        if (ownerOrEmpty.isPresent()) {
            var owner = ownerOrEmpty.get();
            if (owner == funRef)
                interpreter.notifyFunctionExit();
        }
        return true;
    }

    @Override
    public Object execute() {
        var interpreter = Interpreter.get();
        interpreter.enterScope(scope);
        Object returnValue = JSValue.undefined();
        for (var statement : statementList) {
            returnValue = statement.execute();
            if (handleFunctionExit(interpreter))
                break;
        }
        interpreter.exitCurrentScope();
        return returnValue;
    }

    public Block append(final Statement node) {
        Objects.requireNonNull(node);
        statementList.add(node);
        return this;
    }

    public Scope getScope() {
        return scope;
    }

    private List<Statement> statementList = new ArrayList<>();
    private Scope scope;
}
