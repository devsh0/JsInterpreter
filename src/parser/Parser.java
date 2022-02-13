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
        // FIXME: This can also be a for loop.
        private final Stack<WhileStatement> loopStack = new Stack<>();

        public void pushFunctionScope(CompoundStatement function) {
            VERIFY(function instanceof FunctionDeclaration);
            functionStack.push((FunctionDeclaration)function);
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

        public WhileStatement getActiveLoopScope() {
            if (loopStack.isEmpty())
                FIXME_REPORT_SEMANTIC_ERROR();
            var loop = loopStack.peek();
            VERIFY(loop instanceof WhileStatement);
            return loop;
        }

        public WhileStatement getLoopScope(Identifier label) {
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

        public void pushLoopScope(WhileStatement loop) {
            // FIXME: This can also possible be a for loop.
            VERIFY(loop instanceof WhileStatement);
            loopStack.push(loop);
        }

        public CompoundStatement popLoopScope(Identifier label) {
            VERIFY(label != null);
            while (true) {
                var last = popLoopScope();
                if (!(last instanceof WhileStatement))
                    FIXME_UNIMPLEMENTED();
                var lastLoop = (WhileStatement)last;
                if (label.equals(lastLoop.getLabel()))
                    return lastLoop;
            }
        }

        public CompoundStatement popLoopScope() {
            if (loopStack.empty())
                FIXME_REPORT_SYNTAX_ERROR();
            return loopStack.pop();
        }
    }

    private static TokenStream stream;
    private static ScopeManager scopeManager;

    public abstract ASTNode parse();

    public static TokenStream stream() {
        return stream;
    }

    public static ScopeManager scopeManager() {
        return scopeManager;
    }

    public static Parser get(String code) {
        stream = new TokenStream(code);
        scopeManager = new ScopeManager();
        return new ProgramParser();
    }

    public static Parser get(Path path) {
        try {
            String code = Files.readString(path);
            return get(code);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
