package parser;

import ast.*;
import lexer.TokenStream;
import myutils.Assertable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Stack;

public abstract class Parser implements Assertable {
    public static class ScopeManager implements Assertable {
        private final Stack<FunctionDeclaration> functionStack = new Stack<>();
        private final Stack<LoopStatement> loopStack = new Stack<>();

        public void pushFunctionScope(FunctionDeclaration function) {
            VERIFY(function != null);
            functionStack.push(function);
        }

        public FunctionDeclaration getActiveFunctionScope() {
            if (functionStack.isEmpty())
                FIXME_REPORT_SEMANTIC_ERROR();
            return functionStack.peek();
        }

        public FunctionDeclaration popFunctionScope() {
            if (functionStack.isEmpty())
                FIXME_REPORT_SEMANTIC_ERROR();
            return functionStack.pop();
        }

        public LoopStatement getActiveLoopScope() {
            if (loopStack.isEmpty())
                FIXME_REPORT_SEMANTIC_ERROR();
            return loopStack.peek();
        }

        public LoopStatement getLoopScope(Identifier label) {
            if (label == null)
                return getActiveLoopScope();
            if (loopStack.isEmpty())
                FIXME_REPORT_SEMANTIC_ERROR();
            for (var cursor = loopStack.size() - 1; cursor >= 0; cursor--) {
                var current = loopStack.elementAt(cursor);
                if (label.equals(current.getLabel()))
                    return current;
            }
            FIXME_REPORT_SEMANTIC_ERROR();
            return null;
        }

        public void pushLoopScope(LoopStatement loop) {
            VERIFY(loop != null);
            loopStack.push(loop);
        }

        public LoopStatement popLoopScope() {
            if (loopStack.empty())
                FIXME_REPORT_SYNTAX_ERROR();
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
}
