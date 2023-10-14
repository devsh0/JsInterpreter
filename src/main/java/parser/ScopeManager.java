package parser;

import ast.FunctionDeclaration;
import ast.Identifier;
import ast.LoopStatement;

import java.util.Stack;

import static myutils.Macro.todo_report_syntax_error;
import static myutils.Macro.verify;

public class ScopeManager {
    private final Stack<FunctionDeclaration> functionStack = new Stack<>();
    private final Stack<LoopStatement> loopStack = new Stack<>();

    public void pushFunctionScope(FunctionDeclaration function) {
        verify(function != null);
        functionStack.push(function);
    }

    public FunctionDeclaration getActiveFunction() {
        if (functionStack.isEmpty())
            todo_report_syntax_error();
        return functionStack.peek();
    }

    public FunctionDeclaration popFunction() {
        if (functionStack.isEmpty())
            todo_report_syntax_error();
        return functionStack.pop();
    }

    public LoopStatement getActiveLoop() {
        if (loopStack.isEmpty())
            todo_report_syntax_error();
        return loopStack.peek();
    }

    public LoopStatement getLoopWithLabel(Identifier label) {
        if (label == null)
            return getActiveLoop();
        if (loopStack.isEmpty())
            todo_report_syntax_error();
        for (var cursor = loopStack.size() - 1; cursor >= 0; cursor--) {
            var current = loopStack.elementAt(cursor);
            if (label.equals(current.getLabel()))
                return current;
        }
        todo_report_syntax_error();
        return null;
    }

    public void pushLoop(LoopStatement loop) {
        verify(loop != null);
        loopStack.push(loop);
    }

    public LoopStatement popLoop() {
        if (loopStack.empty())
            todo_report_syntax_error();
        return loopStack.pop();
    }
}
