package parser;

import ast.Block;
import ast.CompoundStatement;
import ast.Program;
import ast.Statement;
import lexer.Token;
import lexer.TokenStream;

import java.util.Objects;
import java.util.Optional;

import static myutils.Macro.*;

public class BlockParser extends Parser {
    private CompoundStatement owner;
    private boolean isCurlyEnclosed;

    private enum StatementType {
        Unrecognized,
        Exit,
        BreakStatement,
        ContinueStatement,
        ReturnStatement,
        FunctionDeclarationStatement,
        VariableDeclarationStatement,
        ExpressionStatement,
        LabelStatement,
        IfStatement,
        WhileStatement,
        ForStatement
    }

    BlockParser(TokenStream stream, ScopeManager scopeManager, CompoundStatement owner, boolean isCurlyEnclosed) {
        super(stream, scopeManager);
        Objects.requireNonNull(owner);
        this.owner = owner;
        this.isCurlyEnclosed = isCurlyEnclosed;
    }

    BlockParser(TokenStream stream, ScopeManager scopeManager, CompoundStatement owner) {
        this(stream, scopeManager, owner, true);
    }

    private boolean eof() {
        if (stream().eof()) {
            if (!(owner instanceof Program))
                todo_report_semantic_error();
            return true;
        }
        return false;
    }

    private StatementType nextStatementType() {
        if (eof())
            return StatementType.Exit;

        var nextToken = stream().peekNextToken();
        var tokenType = nextToken.getType();
        var tokenValue = nextToken.getValue();

        if (tokenType == Token.Type.LineCommentT) {
            stream().consumeNextToken();
            return nextStatementType();
        }

        if (tokenType == Token.Type.EOF)
            return StatementType.Exit;

        if (tokenType == Token.Type.SemiColonT)
            return StatementType.ExpressionStatement;

        if (tokenType == Token.Type.RightCurlyT)
            return StatementType.Exit;

        if (tokenValue.equals("return"))
            return StatementType.ReturnStatement;
        if (tokenValue.equals("function"))
            return StatementType.FunctionDeclarationStatement;
        if (tokenValue.equals("let"))
            return StatementType.VariableDeclarationStatement;
        if (tokenValue.equals("if"))
            return StatementType.IfStatement;
        if (tokenValue.equals("while"))
            return StatementType.WhileStatement;
        if (tokenValue.equals("for"))
            return StatementType.ForStatement;
        if (tokenValue.equals("break"))
            return StatementType.BreakStatement;
        if (tokenValue.equals("continue"))
            return StatementType.ContinueStatement;

        if (tokenType == Token.Type.IdentifierT ||
                tokenType == Token.Type.NumberLiteralT ||
                tokenType == Token.Type.StringLiteralT ||
                tokenValue.equals("true") || tokenValue.equals("false")
        ) {
            var tokens = stream().peekTokens(2);
            var type = tokens.get(1).getType();
            var value = tokens.get(1).getValue();

            switch (value) {
                case "+":
                case "-":
                case "*":
                case "/":
                case "%":
                case "=":
                case "==":
                case "+=":
                case "-=":
                case "*=":
                case "/=":
                case "%=":
                case ">":
                case "<":
                case ">=":
                case "<=":
                case "!=":
                case "(":
                    return StatementType.ExpressionStatement;
            }

            if (type == Token.Type.SemiColonT)
                return StatementType.ExpressionStatement;

            if (type == Token.Type.ColonT)
                return StatementType.LabelStatement;

            todo_report_syntax_error();
        }

        return StatementType.Unrecognized;
    }

    private Optional<Statement> parseNextStatement() {
        var statementType = nextStatementType();
        switch (statementType) {
            case ReturnStatement:
                return Optional.of(new ReturnStatementParser(stream(), scopeManager()).parse());
            case IfStatement:
                return Optional.of(new IfStatementParser(stream(), scopeManager()).parse());
            case FunctionDeclarationStatement:
                return Optional.of(new FunctionDeclarationParser(stream(), scopeManager()).parse());
            case VariableDeclarationStatement:
                return Optional.of(new VariableDeclarationParser(stream(), scopeManager()).parse());
            case ExpressionStatement:
                return Optional.of(new ExpressionStatementParser(stream(), scopeManager()).parse());
            case WhileStatement:
                return Optional.of(new WhileStatementParser(stream(), scopeManager(), null).parse());
            case ForStatement:
                return Optional.of(new ForStatementParser(stream(), scopeManager(), null).parse());
            case BreakStatement:
                return Optional.of(new BreakStatementParser(stream(), scopeManager()).parse());
            case ContinueStatement:
                return Optional.of(new ContinueStatementParser(stream(), scopeManager()).parse());
            case LabelStatement:
                return Optional.of(new LabeledStatementParser(stream(), scopeManager()).parse());
            case Exit:
                return Optional.empty();
            default:
                unimplemented();
                return Optional.empty();
        }
    }

    @Override
    public Block parse() {
        var block = new Block(owner);
        var nextStatement = parseNextStatement();
        nextStatement.ifPresent(block::append);

        while (isCurlyEnclosed && (nextStatement = parseNextStatement()).isPresent())
            block.append(nextStatement.get());

        return block;
    }
}
