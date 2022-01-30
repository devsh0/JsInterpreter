package ast;

import ast.value.JSValue;
import org.js.Interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Block implements Statement {
    @Override
    public Object execute() {
        var interpreter = Interpreter.get();
        interpreter.enterScope(scope);
        Object returnValue = JSValue.undefined();
        for (var statement : statementList) {
            returnValue = statement.execute();
        }
        interpreter.exitCurrentScope();
        return returnValue;
    }

    public Block append(final ASTNode node) {
        Objects.requireNonNull(node);
        statementList.add(node);
        return this;
    }

    public Scope getScope() {
        return scope;
    }
    private List<ASTNode> statementList = new ArrayList<>();
    private Scope scope = new Scope();
}
