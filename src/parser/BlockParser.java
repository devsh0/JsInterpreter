package parser;

import ast.*;
import lexer.Token;

import java.util.Objects;
import java.util.Optional;

public class BlockParser extends Parser {
    private CompoundStatement owner;
    private boolean hasMultipleStatement;

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

    BlockParser(CompoundStatement owner, boolean hasMultipleStatement) {
        Objects.requireNonNull(owner);
        this.owner = owner;
        this.hasMultipleStatement = hasMultipleStatement;
    }

    BlockParser(CompoundStatement owner) {
        this(owner, true);
    }

    private boolean eof() {
        if (stream().eof()) {
            if (!(owner instanceof Program))
                FIXME_REPORT_SYNTAX_ERROR();
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
            while (true) {
                if (eof())
                    return StatementType.Exit;
                nextToken = stream().peekNextToken();
                tokenType = nextToken.getType();
                tokenValue = nextToken.getValue();
                if (tokenType != Token.Type.LineCommentT)
                    break;
                stream().consumeNextToken();
            }
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

        if (tokenType == Token.Type.IdentifierT) {
            var tokens = stream().peekTokens(2);
            var type = tokens.get(1).getType();
            var value = tokens.get(1).getValue();

            switch (value) {
                case "=":
                case "+=":
                case "-=":
                case "*=":
                case "/=":
                case "%=":
                case "(":
                    return StatementType.ExpressionStatement;
            }

            if (type == Token.Type.SemiColonT)
                return StatementType.ExpressionStatement;

            if (type == Token.Type.ColonT)
                return StatementType.LabelStatement;

            FIXME_REPORT_SYNTAX_ERROR();
        }

        return StatementType.Unrecognized;
    }

    private Optional<Statement> parseNextStatement() {
        var statementType = nextStatementType();
        switch (statementType) {
            case ReturnStatement:
                return Optional.of(new ReturnStatementParser().parse());
            case IfStatement:
                return Optional.of(new IfStatementParser().parse());
            case FunctionDeclarationStatement:
                return Optional.of(new FunctionDeclarationParser().parse());
            case VariableDeclarationStatement:
                return Optional.of(new VariableDeclarationParser().parse());
            case ExpressionStatement:
                return Optional.of(new ExpressionStatementParser().parse());
            case WhileStatement:
                return Optional.of(new WhileStatementParser(null).parse());
            case ForStatement:
                return Optional.of(new ForStatementParser(null).parse());
            case BreakStatement:
                return Optional.of(new BreakStatementParser().parse());
            case ContinueStatement:
                return Optional.of(new ContinueStatementParser().parse());
            case LabelStatement:
                return Optional.of(new LabeledStatementParser().parse());
            case Exit:
                return Optional.empty();
            default:
                FIXME_UNIMPLEMENTED();
                return Optional.empty();
        }
    }

    @Override
    public Block parse() {
        var block = new Block(owner);
        var nextStatement = parseNextStatement();

        while (nextStatement.isPresent()) {
            block.append(nextStatement.get());
            if (!hasMultipleStatement)
                break;
            nextStatement = parseNextStatement();
        }

        return block;
    }
}
