package ast;

import org.js.Interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

// Fixme: Is there a difference between function declaration and definition in JS?
public class FunctionDeclaration implements CompoundStatement {
    @Override
    public Object execute() {
        Interpreter.get().getCurrentScope().addEntry(id, this);
        return this;
    }

    public FunctionDeclaration setBody(final Block body) {
        Objects.requireNonNull(body);
        this.body = body;
        return this;
    }

    public FunctionDeclaration setId(final Identifier id) {
        Objects.requireNonNull(id);
        this.id = id;
        return this;
    }

    public FunctionDeclaration setParameters(Identifier... identifiers) {
        Objects.requireNonNull(identifiers);
        parameters.addAll(Arrays.asList(identifiers));
        return this;
    }

    public static FunctionDeclaration from(Identifier id, Identifier[] parameters, Block body) {
        var fun = new FunctionDeclaration().setId(id).setParameters(parameters).setBody(body);
        for (var parameter : parameters)
            fun.body.append(VariableDeclaration.from(parameter));
        return fun;
    }

    public Identifier getId() {
        return id;
    }

    public List<Identifier> getParameters() {
        return parameters;
    }

    public Block getBody() {
        return body;
    }

    @Override
    public Statement getLastStatement() {
        return body.getLastStatement();
    }

    private Identifier id;
    private List<Identifier> parameters = new ArrayList<>();
    private Block body;
}
