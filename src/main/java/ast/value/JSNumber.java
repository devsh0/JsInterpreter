package ast.value;

import ast.Expression;
import ast.operator.*;

import java.util.Objects;

public class JSNumber implements
        OperatorLessThan.SupportsLessThanTest,
        OperatorGreaterThan.SupportsGreaterThanTest,
        OperatorGreaterThanOrEqual.SupportsGreaterThanOrEqualTest,
        OperatorLessThanOrEqual.SupportsLessThanOrEqualTest,
        OperatorEquals.SupportsEqualityTest,
        OperatorPlus.Addable,
        OperatorMinus.Subtractable,
        OperatorMultiply.Multiplicable,
        OperatorDivide.Divisible,
        OperatorMod.Modable {

    private Double value;

    @Override
    public boolean isTruthy() {
        return value != 0;
    }

    @Override
    public boolean isFalsy() {
        return value == 0;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public JSValue add(Expression other) {
        if (other instanceof JSNumber) {
            var otherNumber = ((JSNumber) other).getValue();
            return JSNumber.from(value + (Double) otherNumber);
        } else if (other instanceof JSString) {
            var otherString = ((JSString) other).getValue();
            return JSString.from(asString() + (String) otherString);
        } else {
            ASSERT(false);
            return null;
        }
    }

    @Override
    public JSValue subtract(Expression other) {
        var otherNumber = ((JSNumber) other).getValue();
        return JSNumber.from(value - (Double) otherNumber);
    }

    @Override
    public JSValue multiply(Expression other) {
        var otherNumber = ((JSNumber) other).getValue();
        return JSNumber.from(value * (Double) otherNumber);
    }

    @Override
    public JSValue divide(Expression other) {
        var otherNumber = ((JSNumber) other).getValue();
        return JSNumber.from(value / (Double) otherNumber);
    }

    @Override
    public Expression mod(Expression other) {
        var otherNumber = ((JSNumber) other).getValue();
        return JSNumber.from(value % (Double) otherNumber);
    }

    @Override
    public JSNumber setValue(Object value) {
        ASSERT(value instanceof Number);
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
    public String getDump(int indent) {
        return value + "";
    }

    @Override
    public String toString() {
        return value + "";
    }

    public static JSNumber from(Number number) {
        Objects.requireNonNull(number);
        return new JSNumber().setValue(number);
    }

    private JSNumber getJSNumber(Expression expr) {
        Objects.requireNonNull(expr);
        var valueOrError = expr.execute();
        // TODO: Handle type casts.
        ASSERT(valueOrError instanceof JSNumber);
        return (JSNumber) valueOrError;
    }

    @Override
    public JSBoolean isEqualTo(Expression other) {
        var number = getJSNumber(other);
        return JSBoolean.from(value.doubleValue() == number.value.doubleValue());
    }

    @Override
    public JSBoolean isLessThan(Expression other) {
        var number = getJSNumber(other);
        return JSBoolean.from(value < number.value);
    }

    @Override
    public JSBoolean isGreaterThan(Expression other) {
        var number = getJSNumber(other);
        return JSBoolean.from(value > number.value);
    }

    @Override
    public JSBoolean isGreaterThanOrEqual(Expression other) {
        var number = getJSNumber(other);
        return JSBoolean.from(value >= number.value);
    }

    @Override
    public JSBoolean isLessThanOrEqual(Expression other) {
        var number = getJSNumber(other);
        return JSBoolean.from(value <= number.value);
    }
}
