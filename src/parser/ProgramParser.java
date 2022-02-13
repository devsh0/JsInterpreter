package parser;

import ast.Program;

public class ProgramParser extends Parser {
    @Override
    public Program parse() {
        var program = new Program();
        program.setBody(new BlockParser(program).parse());
        return program;
    }
}
