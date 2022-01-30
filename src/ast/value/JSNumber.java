package ast.value;

import ast.Expression;
import ast.operator.OperatorDivide;
import ast.operator.OperatorMinus;
import ast.operator.OperatorMultiply;
import ast.operator.OperatorPlus;

import java.util.Objects;

public class JSNumber implements
        OperatorPlus.Addable,
        OperatorMinus.Subtractable,
        OperatorMultiply.Multiplicable,
        OperatorDivide.Divisible {

    private Double value;

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public JSValue add(Expression other) {
        if (other instanceof JSNumber) {
            var otherNumber = ((JSNumber)other).getValue();
            return JSNumber.from(value + (Double)otherNumber);
        } else if (other instanceof JSString) {
            var otherString = ((JSString)other).getValue();
            return JSString.from(asString() + (String)otherString);
        } else {
            Assert(false);
            return null;
        }
    }

    @Override
    public JSValue subtract(Expression other) {
        var otherNumber = ((JSNumber)other).getValue();
        return JSNumber.from(value - (Double)otherNumber);
    }

    @Override
    public JSValue multiply(Expression other) {
        var otherNumber = ((JSNumber)other).getValue();
        return JSNumber.from(value * (Double)otherNumber);
    }

    @Override
    public JSValue divide(Expression other) {
        var otherNumber = ((JSNumber)other).getValue();
        return JSNumber.from(value / (Double)otherNumber);
    }

    @Override
    public JSNumber setValue(Object value) {
        Assert(value instanceof Number);
        this.value = ((Number) value).doubleValue();
        return this;
    }

    @Override
    public JSString asString() {
        return JSString.from(String.valueOf(value));
    }

    @Override
    public JSNumber execute() {
        return this;
    }

    @Override
    public String toString() {
        return value + "";
    }

    public static JSNumber from(Number number) {
        Objects.requireNonNull(number);
        return new JSNumber().setValue(number);
    }
}
