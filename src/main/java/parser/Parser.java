package parser;

import ast.ASTNode;
import ast.Program;
import lexer.TokenStream;

public abstract class Parser {

    private final TokenStream stream;
    private final ScopeManager scopeManager;

    public Parser(TokenStream stream, ScopeManager scopeManager) {
        this.stream = stream;
        this.scopeManager = scopeManager;
    }

    public abstract ASTNode parse();

    public TokenStream stream() {
        return stream;
    }

    public ScopeManager scopeManager() {
        return scopeManager;
    }

    public static Program parse(String code) {
        TokenStream stream = new TokenStream(code);
        ScopeManager manager = new ScopeManager();
        return new ProgramParser(stream, manager).parse();
    }
}
