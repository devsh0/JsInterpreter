package ast;

public interface Statement extends ASTNode {

    @Override
    public String getString(int indent);
}
