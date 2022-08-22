package parser;

import ast.*;
import lexer.TokenStream;
import java.util.Stack;

import static myutils.Macro.todo_report_semantic_error;
import static myutils.Macro.verify;

public abstract class Parser {
    public static class ScopeManager {
        private final Stack<FunctionDeclaration> functionStack = new Stack<>();
        private final Stack<LoopStatement> loopStack = new Stack<>();

        public void pushFunctionScope(FunctionDeclaration function) {
            verify(function != null);
            functionStack.push(function);
        }

        public FunctionDeclaration getActiveFunctionScope() {
            if (functionStack.isEmpty())
                todo_report_semantic_error();
            return functionStack.peek();
        }

        public FunctionDeclaration popFunctionScope() {
            if (functionStack.isEmpty())
                todo_report_semantic_error();
            return functionStack.pop();
        }

        public LoopStatement getActiveLoopScope() {
            if (loopStack.isEmpty())
                todo_report_semantic_error();
            return loopStack.peek();
        }

        public LoopStatement getLoopScope(Identifier label) {
            if (label == null)
                return getActiveLoopScope();
            if (loopStack.isEmpty())
                todo_report_semantic_error();
            for (var cursor = loopStack.size() - 1; cursor >= 0; cursor--) {
                var current = loopStack.elementAt(cursor);
                if (label.equals(current.getLabel()))
                    return current;
            }
            todo_report_semantic_error();
            return null;
        }

        public void pushLoopScope(LoopStatement loop) {
            verify(loop != null);
            loopStack.push(loop);
        }

        public LoopStatement popLoopScope() {
            if (loopStack.empty())
                todo_report_semantic_error();
            return loopStack.pop();
        }
    }

    private final TokenStream stream;
    private final ScopeManager scopeManager;

    public abstract ASTNode parse();

    public TokenStream stream() {
        return stream;
    }

    public ScopeManager scopeManager() {
        return scopeManager;
    }

    public Parser(TokenStream stream, ScopeManager scopeManager) {
        this.stream = stream;
        this.scopeManager = scopeManager;
    }

    public static Program parse(String code) {
        TokenStream stream = new TokenStream(code);
        ScopeManager manager = new ScopeManager();
        return new ProgramParser(stream, manager).parse();
    }
}
