package ast;

public interface ASTNode {
    public Object execute();

    public String getPrettyString(int indent);
}
