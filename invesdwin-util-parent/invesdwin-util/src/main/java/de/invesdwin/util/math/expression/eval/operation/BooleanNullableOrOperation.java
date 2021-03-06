package de.invesdwin.util.math.expression.eval.operation;

import javax.annotation.concurrent.Immutable;

import de.invesdwin.util.error.UnknownArgumentException;
import de.invesdwin.util.math.Booleans;
import de.invesdwin.util.math.Doubles;
import de.invesdwin.util.math.Integers;
import de.invesdwin.util.math.expression.ExpressionType;
import de.invesdwin.util.math.expression.eval.BooleanConstantExpression;
import de.invesdwin.util.math.expression.eval.ConstantExpression;
import de.invesdwin.util.math.expression.eval.IParsedExpression;
import de.invesdwin.util.math.expression.eval.operation.simple.BooleanOrOperation;
import de.invesdwin.util.math.expression.lambda.IEvaluateBoolean;
import de.invesdwin.util.math.expression.lambda.IEvaluateBooleanFDate;
import de.invesdwin.util.math.expression.lambda.IEvaluateBooleanKey;
import de.invesdwin.util.math.expression.lambda.IEvaluateBooleanNullable;
import de.invesdwin.util.math.expression.lambda.IEvaluateBooleanNullableFDate;
import de.invesdwin.util.math.expression.lambda.IEvaluateBooleanNullableKey;
import de.invesdwin.util.math.expression.lambda.IEvaluateDouble;
import de.invesdwin.util.math.expression.lambda.IEvaluateDoubleFDate;
import de.invesdwin.util.math.expression.lambda.IEvaluateDoubleKey;
import de.invesdwin.util.math.expression.lambda.IEvaluateGeneric;
import de.invesdwin.util.math.expression.lambda.IEvaluateGenericFDate;
import de.invesdwin.util.math.expression.lambda.IEvaluateGenericKey;
import de.invesdwin.util.math.expression.lambda.IEvaluateInteger;
import de.invesdwin.util.math.expression.lambda.IEvaluateIntegerFDate;
import de.invesdwin.util.math.expression.lambda.IEvaluateIntegerKey;

@Immutable
public class BooleanNullableOrOperation extends DoubleBinaryOperation {

    public BooleanNullableOrOperation(final IParsedExpression left, final IParsedExpression right) {
        super(Op.OR, left, right);
    }

    @Override
    public IEvaluateDoubleFDate newEvaluateDoubleFDate() {
        final IEvaluateBooleanNullableFDate f = newEvaluateBooleanNullableFDate();
        return key -> {
            final Boolean check = f.evaluateBooleanNullable(key);
            return Doubles.fromBoolean(check);
        };
    }

    @Override
    public IEvaluateDoubleKey newEvaluateDoubleKey() {
        final IEvaluateBooleanNullableKey f = newEvaluateBooleanNullableKey();
        return key -> {
            final Boolean check = f.evaluateBooleanNullable(key);
            return Doubles.fromBoolean(check);
        };
    }

    @Override
    public IEvaluateDouble newEvaluateDouble() {
        final IEvaluateBooleanNullable f = newEvaluateBooleanNullable();
        return () -> {
            final Boolean check = f.evaluateBooleanNullable();
            return Doubles.fromBoolean(check);
        };
    }

    @Override
    public IEvaluateIntegerFDate newEvaluateIntegerFDate() {
        final IEvaluateBooleanFDate f = newEvaluateBooleanFDate();
        return key -> {
            final boolean check = f.evaluateBoolean(key);
            return Integers.fromBoolean(check);
        };
    }

    @Override
    public IEvaluateIntegerKey newEvaluateIntegerKey() {
        final IEvaluateBooleanKey f = newEvaluateBooleanKey();
        return key -> {
            final boolean check = f.evaluateBoolean(key);
            return Integers.fromBoolean(check);
        };
    }

    @Override
    public IEvaluateInteger newEvaluateInteger() {
        final IEvaluateBoolean f = newEvaluateBoolean();
        return () -> {
            final boolean check = f.evaluateBoolean();
            return Integers.fromBoolean(check);
        };
    }

    @Override
    public IEvaluateBooleanNullableFDate newEvaluateBooleanNullableFDate() {
        final IEvaluateBooleanFDate leftF = left.newEvaluateBooleanFDate();
        final IEvaluateBooleanNullableFDate rightF = right.newEvaluateBooleanNullableFDate();
        return key -> {
            final boolean leftResult = leftF.evaluateBoolean(key);
            if (leftResult) {
                return Boolean.TRUE;
            } else {
                return rightF.evaluateBooleanNullable(key);
            }
        };
    }

    @Override
    public IEvaluateBooleanNullableKey newEvaluateBooleanNullableKey() {
        final IEvaluateBooleanKey leftF = left.newEvaluateBooleanKey();
        final IEvaluateBooleanNullableKey rightF = right.newEvaluateBooleanNullableKey();
        return key -> {
            final boolean leftResult = leftF.evaluateBoolean(key);
            if (leftResult) {
                return Boolean.TRUE;
            } else {
                return rightF.evaluateBooleanNullable(key);
            }
        };
    }

    @Override
    public IEvaluateBooleanNullable newEvaluateBooleanNullable() {
        final IEvaluateBoolean leftF = left.newEvaluateBoolean();
        final IEvaluateBooleanNullable rightF = right.newEvaluateBooleanNullable();
        return () -> {
            final boolean leftResult = leftF.evaluateBoolean();
            if (leftResult) {
                return Boolean.TRUE;
            } else {
                return rightF.evaluateBooleanNullable();
            }
        };
    }

    @Override
    public IEvaluateBooleanFDate newEvaluateBooleanFDate() {
        final IEvaluateBooleanFDate leftF = left.newEvaluateBooleanFDate();
        final IEvaluateBooleanFDate rightF = right.newEvaluateBooleanFDate();
        return key -> leftF.evaluateBoolean(key) || rightF.evaluateBoolean(key);
    }

    @Override
    public IEvaluateBooleanKey newEvaluateBooleanKey() {
        final IEvaluateBooleanKey leftF = left.newEvaluateBooleanKey();
        final IEvaluateBooleanKey rightF = right.newEvaluateBooleanKey();
        return key -> leftF.evaluateBoolean(key) || rightF.evaluateBoolean(key);
    }

    @Override
    public IEvaluateBoolean newEvaluateBoolean() {
        final IEvaluateBoolean leftF = left.newEvaluateBoolean();
        final IEvaluateBoolean rightF = right.newEvaluateBoolean();
        return () -> leftF.evaluateBoolean() || rightF.evaluateBoolean();
    }

    @Override
    public IEvaluateGenericKey<String> newEvaluateTrueReasonKey() {
        final IEvaluateGenericKey<String> leftF = left.newEvaluateTrueReasonKey();
        final IEvaluateGenericKey<String> rightF = right.newEvaluateTrueReasonKey();
        return key -> {
            final String leftStr = leftF.evaluateGeneric(key);
            if (leftStr != null) {
                return leftStr;
            }
            final String rightStr = rightF.evaluateGeneric(key);
            return rightStr;
        };
    }

    @Override
    public IEvaluateGeneric<String> newEvaluateTrueReason() {
        final IEvaluateGeneric<String> leftF = left.newEvaluateTrueReason();
        final IEvaluateGeneric<String> rightF = right.newEvaluateTrueReason();
        return () -> {
            final String leftStr = leftF.evaluateGeneric();
            if (leftStr != null) {
                return leftStr;
            }
            final String rightStr = rightF.evaluateGeneric();
            return rightStr;
        };
    }

    @Override
    public IEvaluateGenericFDate<String> newEvaluateTrueReasonFDate() {
        final IEvaluateGenericFDate<String> leftF = left.newEvaluateTrueReasonFDate();
        final IEvaluateGenericFDate<String> rightF = right.newEvaluateTrueReasonFDate();
        return key -> {
            final String leftStr = leftF.evaluateGeneric(key);
            if (leftStr != null) {
                return leftStr;
            }
            final String rightStr = rightF.evaluateGeneric(key);
            return rightStr;
        };
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public IParsedExpression simplify() {
        final IParsedExpression newLeft = left.simplify();
        final IParsedExpression newRight = right.simplify();
        if (newLeft.isConstant()) {
            final Boolean leftResult = newLeft.newEvaluateBooleanNullable().evaluateBooleanNullable();
            if (Booleans.isTrue(leftResult)) {
                return BooleanConstantExpression.TRUE;
            } else {
                if (newRight.isConstant()) {
                    final Boolean rightResult = newRight.newEvaluateBooleanNullable().evaluateBooleanNullable();
                    if (rightResult != null) {
                        return new ConstantExpression(Doubles.fromBoolean(rightResult),
                                ExpressionType.determineSmallestBooleanType(rightResult));
                    } else {
                        return new ConstantExpression(Doubles.fromBoolean(leftResult),
                                ExpressionType.determineSmallestBooleanType(leftResult));
                    }
                } else {
                    return newRight;
                }
            }
        }
        if (newRight.isConstant()) {
            final Boolean rightResult = newRight.newEvaluateBooleanNullable().evaluateBooleanNullable();
            if (Booleans.isTrue(rightResult)) {
                return BooleanConstantExpression.TRUE;
            } else {
                if (newLeft.isConstant()) {
                    final Boolean leftResult = newLeft.newEvaluateBooleanNullable().evaluateBooleanNullable();
                    if (leftResult != null) {
                        return new ConstantExpression(Doubles.fromBoolean(leftResult),
                                ExpressionType.determineSmallestBooleanType(leftResult));
                    } else {
                        return new ConstantExpression(Doubles.fromBoolean(rightResult),
                                ExpressionType.determineSmallestBooleanType(rightResult));
                    }
                } else {
                    return newLeft;
                }
            }
        }
        return simplify(newLeft, newRight);
    }

    @Override
    protected IBinaryOperation newBinaryOperation(final IParsedExpression left, final IParsedExpression right) {
        final ExpressionType simplifyType = op.simplifyType(left, right);
        if (simplifyType == null) {
            return new BooleanNullableOrOperation(left, right);
        } else if (simplifyType == ExpressionType.Boolean) {
            return new BooleanOrOperation(left, right);
        } else {
            throw UnknownArgumentException.newInstance(ExpressionType.class, simplifyType);
        }
    }

}
