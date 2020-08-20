package de.invesdwin.util.math.expression.eval;

import javax.annotation.concurrent.Immutable;

import de.invesdwin.util.math.Doubles;
import de.invesdwin.util.math.Integers;
import de.invesdwin.util.math.decimal.Decimal;
import de.invesdwin.util.math.expression.ExpressionType;
import de.invesdwin.util.math.expression.IExpression;
import de.invesdwin.util.time.fdate.IFDateProvider;

@Immutable
public class ConstantExpression implements IParsedExpression {

    private final ExpressionType type;
    private final double doubleValue;
    private final int intValue;
    private final Boolean booleanNullableValue;
    private final boolean booleanValue;

    public ConstantExpression(final double value, final ExpressionType type) {
        this.type = type;
        this.doubleValue = value;
        this.intValue = Integers.checkedCastNoOverflow(value);
        this.booleanNullableValue = Doubles.doubleToBooleanNullable(value);
        this.booleanValue = Doubles.doubleToBoolean(value);
    }

    @Override
    public double evaluateDouble(final IFDateProvider key) {
        return doubleValue;
    }

    @Override
    public double evaluateDouble(final int key) {
        return doubleValue;
    }

    @Override
    public double evaluateDouble() {
        return doubleValue;
    }

    @Override
    public int evaluateInteger(final IFDateProvider key) {
        return intValue;
    }

    @Override
    public int evaluateInteger(final int key) {
        return intValue;
    }

    @Override
    public int evaluateInteger() {
        return intValue;
    }

    @Override
    public Boolean evaluateBooleanNullable(final IFDateProvider key) {
        return booleanNullableValue;
    }

    @Override
    public Boolean evaluateBooleanNullable(final int key) {
        return booleanNullableValue;
    }

    @Override
    public Boolean evaluateBooleanNullable() {
        return booleanNullableValue;
    }

    @Override
    public boolean evaluateBoolean(final IFDateProvider key) {
        return booleanValue;
    }

    @Override
    public boolean evaluateBoolean(final int key) {
        return booleanValue;
    }

    @Override
    public boolean evaluateBoolean() {
        return booleanValue;
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public String toString() {
        return new Decimal(doubleValue).toString();
    }

    @Override
    public IParsedExpression simplify() {
        return this;
    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ConstantExpression) {
            final ConstantExpression cObj = (ConstantExpression) obj;
            return doubleValue == cObj.doubleValue;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return ConstantExpression.class.hashCode() + Double.hashCode(doubleValue);
    }

    @Override
    public boolean shouldPersist() {
        return false;
    }

    @Override
    public boolean shouldDraw() {
        return true;
    }

    @Override
    public IExpression[] getChildren() {
        return EMPTY_EXPRESSIONS;
    }

    @Override
    public ExpressionType getType() {
        return type;
    }

}
