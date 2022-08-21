package parser;

import ast.Program;
import lexer.TokenStream;

public class ProgramParser extends Parser {
    public ProgramParser(TokenStream stream, ScopeManager scopeManager) {
        super(stream, scopeManager);
    }

    @Override
    public Program parse() {
        var program = new Program();
        program.setBody(new BlockParser(stream(), scopeManager(), program).parse());
        return program;
    }
}
