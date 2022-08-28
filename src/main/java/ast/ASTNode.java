package ast;

public interface ASTNode {
    public static final int Indent = 4;

    public Object execute();

    public String getDump(int indent);

    public default void dump() {
        System.out.println(getDump(0));
    }

    public default StringBuilder getIndentedBuilder(int indents) {
        return new StringBuilder(getIndent(indents));
    }

    public default String getIndent(int indents) {
        var builder = new StringBuilder();
        while (indents-- > 0)
            builder.append(" ");
        return builder.toString();
    }
}
