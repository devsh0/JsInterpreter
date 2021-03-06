package ast;

import myutils.Assertable;
import org.js.Interpreter;

import java.util.Objects;

public class Identifier implements ASTNode, Expression {
    private String name;

    public void printName() {
        System.out.println("Identifier name: " + name);
    }

    @Override
    public Object execute() {
        var entity = Interpreter.get().queryScope(this);
        ASSERT(entity.isPresent());
        return entity.get().execute();
    }

    @Override
    public String getDump(int indent) {
        return name;
    }

    public Identifier setName(final String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Identifier that = (Identifier) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }

    public static Identifier from(String name) {
        Objects.requireNonNull(name);
        Assertable._ASSERT(!name.isEmpty());
        return new Identifier().setName(name);
    }
}
