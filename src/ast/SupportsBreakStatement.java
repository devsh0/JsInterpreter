package ast;

public abstract class SupportsBreakStatement extends CompoundStatement {
    public abstract SupportsBreakStatement setLabel(Identifier label);
    public abstract Identifier getLabel();
}
