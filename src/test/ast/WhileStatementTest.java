package test.ast;

import ast.*;
import ast.operator.BinaryOperator;
import ast.operator.RelationalOperator;
import ast.value.JSNumber;
import ast.value.JSValue;
import org.js.Interpreter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WhileStatementTest {
    private static final Interpreter interpreter = Interpreter.get();

    /**
     * let i = 0;
     * let sum = 0;
     * while (i < n) {
     * sum = sum + i;
     * i = i + 1;
     * }
     * sum;
     */
    @Test
    public void simpleWhile() {
        var program = new Program();
        var programBody = new Block(program);

        var sumVar = Identifier.from("sum");
        programBody.append(new VariableDeclaration().setIdentifier(sumVar).setInitializer(JSValue.from(0)));
        var iVar = Identifier.from("i");
        programBody.append(new VariableDeclaration().setIdentifier(iVar).setInitializer(JSValue.from(0)));

        var whileStatement = (new WhileStatement().setConditionExpression(new BinaryExpression()
                .setOperator(RelationalOperator.LessThan)
                .setLhs(iVar)
                .setRhs(JSValue.from(10))
        ));
        var whileBody = new Block(whileStatement);
        whileBody.append(new AssignmentExpression().setDestination(sumVar).setSource(
                new BinaryExpression().setOperator(BinaryOperator.Plus).setLhs(sumVar).setRhs(iVar)
        ));

        whileBody.append(new AssignmentExpression().setDestination(iVar).setSource(
                new BinaryExpression().setOperator(BinaryOperator.Plus).setLhs(iVar).setRhs(JSValue.from(1))
        ));

        whileStatement.setBody(whileBody);
        programBody.append(whileStatement);
        programBody.append(new ExpressionStatement().setSource(sumVar));

        program.setBody(programBody);
        var returnVal = interpreter.run(program);
        assertTrue(returnVal instanceof JSNumber);

        var number = (Number) ((JSNumber) returnVal).getValue();
        assertEquals(45, number.doubleValue());
    }

    /**
     * let sum = 0;
     * let i = 0;
     * while (true) {
     * sum = sum + i;
     * if (100 < sum)
     * break;
     * i = i + 1;
     * }
     * i;
     */
    @Test
    public void whileWithNestedIf() {
        var program = new Program();
        var programBody = new Block(program);

        var varSum = Identifier.from("sum");
        programBody.append(new VariableDeclaration().setIdentifier(varSum).setInitializer(JSValue.from(0)));
        var varI = Identifier.from("i");
        programBody.append(new VariableDeclaration().setIdentifier(varI).setInitializer(JSValue.from(0)));

        var whileStatement = new WhileStatement().setConditionExpression(JSValue.from(true));
        var whileBody = new Block(whileStatement);
        whileBody.append(new AssignmentExpression().setDestination(varSum).setSource(new BinaryExpression()
                .setOperator(BinaryOperator.Plus)
                .setLhs(varSum)
                .setRhs(varI)
        ));
        var ifStatement = new IfStatement().setConditionExpression(new BinaryExpression()
                .setOperator(RelationalOperator.LessThan)
                .setLhs(JSValue.from(100))
                .setRhs(varSum)
        );
        var ifBody = new Block(ifStatement);
        ifBody.append(new BreakStatement(whileStatement));
        ifStatement.setBody(ifBody);

        whileBody.append(ifStatement);
        whileBody.append(new AssignmentExpression().setDestination(varI).setSource(new BinaryExpression()
                .setOperator(BinaryOperator.Plus)
                .setLhs(varI)
                .setRhs(JSValue.from(1))
        ));

        whileStatement.setBody(whileBody);
        programBody.append(whileStatement);
        programBody.append(new ExpressionStatement().setSource(varI));
        program.setBody(programBody);

        var retVal = interpreter.run(program);
        assertTrue(retVal instanceof JSNumber);

        var number = (Number) ((JSNumber) retVal).getValue();
        assertEquals(14, number.doubleValue());
    }
}
