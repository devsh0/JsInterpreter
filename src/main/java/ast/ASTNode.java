package ast;

public interface ASTNode {
    public Object execute();

    public String getString(int indent);
}
