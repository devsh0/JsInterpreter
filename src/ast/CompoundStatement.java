package ast;

public interface CompoundStatement extends Statement {
    public Block getBody();
    public Statement getLastStatement();
}
