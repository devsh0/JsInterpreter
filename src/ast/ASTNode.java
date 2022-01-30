package ast;

import myutils.Assertable;

public interface ASTNode extends Assertable {
    public Object execute();

    public default void dump() {
        System.out.println(this);
    }
}
