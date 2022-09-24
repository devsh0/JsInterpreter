package ast;

public interface Statement extends ASTNode {

    @Override
    public String getPrettyString(int indent);
}
