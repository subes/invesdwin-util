package de.invesdwin.util.math.expression.eval.operation;

import javax.annotation.concurrent.Immutable;

import de.invesdwin.util.math.Doubles;
import de.invesdwin.util.math.Integers;
import de.invesdwin.util.math.expression.IExpression;
import de.invesdwin.util.math.expression.eval.ConstantExpression;
import de.invesdwin.util.math.expression.eval.IParsedExpression;
import de.invesdwin.util.time.fdate.IFDateProvider;

@Immutable
public class BinaryOperation implements IParsedExpression {

    protected final IParsedExpression left;
    protected final IParsedExpression right;
    private final Op op;
    private boolean sealed = false;

    public BinaryOperation(final Op op, final IParsedExpression left, final IParsedExpression right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    public Op getOp() {
        return op;
    }

    public IParsedExpression getLeft() {
        return left;
    }

    public BinaryOperation setLeft(final IParsedExpression left) {
        return newBinaryOperation(op, left, right);
    }

    public IParsedExpression getRight() {
        return right;
    }

    public void seal() {
        sealed = true;
    }

    public boolean isSealed() {
        return sealed;
    }

    @Override
    public double evaluateDouble(final IFDateProvider key) {
        final double a = left.evaluateDouble(key);
        final double b = right.evaluateDouble(key);

        return op.applyDouble(a, b);
    }

    @Override
    public double evaluateDouble(final int key) {
        final double a = left.evaluateDouble(key);
        final double b = right.evaluateDouble(key);

        return op.applyDouble(a, b);
    }

    @Override
    public double evaluateDouble() {
        final double a = left.evaluateDouble();
        final double b = right.evaluateDouble();

        return op.applyDouble(a, b);
    }

    @Override
    public int evaluateInteger(final IFDateProvider key) {
        final double a = left.evaluateDouble(key);
        final double b = right.evaluateDouble(key);

        return op.applyInteger(a, b);
    }

    @Override
    public int evaluateInteger(final int key) {
        final double a = left.evaluateDouble(key);
        final double b = right.evaluateDouble(key);

        return op.applyInteger(a, b);
    }

    @Override
    public int evaluateInteger() {
        final double a = left.evaluateDouble();
        final double b = right.evaluateDouble();

        return op.applyInteger(a, b);
    }

    @Override
    public Boolean evaluateBooleanNullable(final IFDateProvider key) {
        final double a = left.evaluateDouble(key);
        final double b = right.evaluateDouble(key);

        return op.applyBooleanNullable(a, b);
    }

    @Override
    public Boolean evaluateBooleanNullable(final int key) {
        final double a = left.evaluateDouble(key);
        final double b = right.evaluateDouble(key);

        return op.applyBooleanNullable(a, b);
    }

    @Override
    public Boolean evaluateBooleanNullable() {
        final double a = left.evaluateDouble();
        final double b = right.evaluateDouble();

        return op.applyBooleanNullable(a, b);
    }

    @Override
    public boolean evaluateBoolean(final IFDateProvider key) {
        final double a = left.evaluateDouble(key);
        final double b = right.evaluateDouble(key);

        return op.applyBoolean(a, b);
    }

    @Override
    public boolean evaluateBoolean(final int key) {
        final double a = left.evaluateDouble(key);
        final double b = right.evaluateDouble(key);

        return op.applyBoolean(a, b);
    }

    @Override
    public boolean evaluateBoolean() {
        final double a = left.evaluateDouble();
        final double b = right.evaluateDouble();

        return op.applyBoolean(a, b);
    }

    @Override
    public IParsedExpression simplify() {
        final IParsedExpression newLeft = left.simplify();
        final IParsedExpression newRight = right.simplify();
        return simplify(newLeft, newRight);
    }

    protected IParsedExpression simplify(final IParsedExpression simplifiedLeft,
            final IParsedExpression simplifiedRight) {
        IParsedExpression newLeft = simplifiedLeft;
        IParsedExpression newRight = simplifiedRight;
        // First of all we check of both sides are constant. If true, we can directly evaluate the result...
        if (newLeft.isConstant() && newRight.isConstant()) {
            return newConstantExpression();
        }
        // + and * are commutative and associative, therefore we can reorder operands as we desire
        if (op == Op.ADD || op == Op.MULTIPLY) {
            // We prefer the have the constant part at the left side, re-order if it is the other way round.
            // This simplifies further optimizations as we can concentrate on the left side
            if (newRight.isConstant()) {
                final IParsedExpression tmp = newRight;
                newRight = newLeft;
                newLeft = tmp;
            }

            if (newRight instanceof BinaryOperation) {
                final IParsedExpression childOp = trySimplifyRightSide(newLeft, newRight);
                if (childOp != null) {
                    //we can directly use the child instead of this one
                    return childOp;
                }
            }
        }

        return newBinaryOperation(op, newLeft, newRight);
    }

    private IParsedExpression newConstantExpression() {
        return new ConstantExpression(evaluateDouble());
    }

    protected BinaryOperation newBinaryOperation(final Op op, final IParsedExpression left,
            final IParsedExpression right) {
        return new BinaryOperation(op, left, right);
    }

    private IParsedExpression trySimplifyRightSide(final IParsedExpression newLeft, final IParsedExpression newRight) {
        final BinaryOperation childOp = (BinaryOperation) newRight;
        if (op != childOp.op) {
            return null;
        }

        // We have a sub-operation with the same operator, let's see if we can pre-compute some constants
        if (newLeft.isConstant()) {
            // Left side is constant, we therefore can combine constants. We can rely on the constant
            // being on the left side, since we reorder commutative operations (see above)
            if (childOp.left.isConstant()) {
                if (op == Op.ADD) {
                    return newBinaryOperation(op,
                            new ConstantExpression(newLeft.evaluateDouble() + childOp.left.evaluateDouble()),
                            childOp.right);
                }
                if (op == Op.MULTIPLY) {
                    return newBinaryOperation(op,
                            new ConstantExpression(newLeft.evaluateDouble() * childOp.left.evaluateDouble()),
                            childOp.right);
                }
            }
        }

        if (childOp.left.isConstant()) {
            // Since our left side is non constant, but the left side of the child expression is,
            // we push the constant up, to support further optimizations
            return newBinaryOperation(op, childOp.left, newBinaryOperation(op, newLeft, childOp.right));
        }

        return null;
    }

    @Override
    public String toString() {
        return "(" + left + " " + op + " " + right + ")";
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    public enum Op {
        ADD(3, "+") {
            @Override
            public double applyDouble(final double a, final double b) {
                return a + b;
            }

            @Override
            public int applyInteger(final double a, final double b) {
                return Integers.checkedCastNoOverflow(applyDouble(a, b));
            }

            @Override
            public Boolean applyBooleanNullable(final double a, final double b) {
                return Doubles.doubleToBooleanNullable(applyDouble(a, b));
            }

            @Override
            public boolean applyBoolean(final double a, final double b) {
                return Doubles.doubleToBoolean(applyDouble(a, b));
            }

            @Override
            public double applyDouble(final int a, final int b) {
                return applyDouble((double) a, (double) b);
            }

            @Override
            public int applyInteger(final int a, final int b) {
                return Integers.checkedCastNoOverflow(applyDouble(a, b));
            }

            @Override
            public Boolean applyBooleanNullable(final int a, final int b) {
                return Doubles.doubleToBooleanNullable(applyDouble(a, b));
            }

            @Override
            public boolean applyBoolean(final int a, final int b) {
                return Doubles.doubleToBoolean(applyDouble(a, b));
            }
        },
        SUBTRACT(3, "-") {
            @Override
            public double applyDouble(final double a, final double b) {
                return a - b;
            }

            @Override
            public int applyInteger(final double a, final double b) {
                return Integers.checkedCastNoOverflow(applyDouble(a, b));
            }

            @Override
            public Boolean applyBooleanNullable(final double a, final double b) {
                return Doubles.doubleToBooleanNullable(applyDouble(a, b));
            }

            @Override
            public boolean applyBoolean(final double a, final double b) {
                return Doubles.doubleToBoolean(applyDouble(a, b));
            }

            @Override
            public double applyDouble(final int a, final int b) {
                return a - b;
            }

            @Override
            public int applyInteger(final int a, final int b) {
                return a - b;
            }

            @Override
            public Boolean applyBooleanNullable(final int a, final int b) {
                return Integers.integerToBooleanNullable(applyInteger(a, b));
            }

            @Override
            public boolean applyBoolean(final int a, final int b) {
                return Integers.integerToBoolean(applyInteger(a, b));
            }
        },
        MULTIPLY(4, "*") {
            @Override
            public double applyDouble(final double a, final double b) {
                return a * b;
            }

            @Override
            public int applyInteger(final double a, final double b) {
                return Integers.checkedCastNoOverflow(applyDouble(a, b));
            }

            @Override
            public Boolean applyBooleanNullable(final double a, final double b) {
                return Doubles.doubleToBooleanNullable(applyDouble(a, b));
            }

            @Override
            public boolean applyBoolean(final double a, final double b) {
                return Doubles.doubleToBoolean(applyDouble(a, b));
            }

            @Override
            public double applyDouble(final int a, final int b) {
                return applyDouble((double) a, (double) b);
            }

            @Override
            public int applyInteger(final int a, final int b) {
                return Integers.checkedCastNoOverflow(applyDouble(a, b));
            }

            @Override
            public Boolean applyBooleanNullable(final int a, final int b) {
                return Doubles.doubleToBooleanNullable(applyInteger(a, b));
            }

            @Override
            public boolean applyBoolean(final int a, final int b) {
                return Doubles.doubleToBoolean(applyInteger(a, b));
            }
        },
        DIVIDE(4, "/") {
            @Override
            public double applyDouble(final double a, final double b) {
                return Doubles.divide(a, b);
            }

            @Override
            public int applyInteger(final double a, final double b) {
                return Integers.checkedCastNoOverflow(applyDouble(a, b));
            }

            @Override
            public Boolean applyBooleanNullable(final double a, final double b) {
                return Doubles.doubleToBooleanNullable(applyDouble(a, b));
            }

            @Override
            public boolean applyBoolean(final double a, final double b) {
                return Doubles.doubleToBoolean(applyDouble(a, b));
            }

            @Override
            public double applyDouble(final int a, final int b) {
                return Doubles.divide(a, b);
            }

            @Override
            public int applyInteger(final int a, final int b) {
                return Integers.divide(a, b);
            }

            @Override
            public Boolean applyBooleanNullable(final int a, final int b) {
                return Doubles.doubleToBooleanNullable(applyInteger(a, b));
            }

            @Override
            public boolean applyBoolean(final int a, final int b) {
                return Doubles.doubleToBoolean(applyInteger(a, b));
            }
        },
        MODULO(4, "%") {
            @Override
            public double applyDouble(final double a, final double b) {
                return a % b;
            }

            @Override
            public int applyInteger(final double a, final double b) {
                return Integers.checkedCastNoOverflow(applyDouble(a, b));
            }

            @Override
            public Boolean applyBooleanNullable(final double a, final double b) {
                return Doubles.doubleToBooleanNullable(applyDouble(a, b));
            }

            @Override
            public boolean applyBoolean(final double a, final double b) {
                return Doubles.doubleToBoolean(applyDouble(a, b));
            }

            @Override
            public double applyDouble(final int a, final int b) {
                return applyInteger(a, b);
            }

            @Override
            public int applyInteger(final int a, final int b) {
                return a % b;
            }

            @Override
            public Boolean applyBooleanNullable(final int a, final int b) {
                return Doubles.doubleToBooleanNullable(applyInteger(a, b));
            }

            @Override
            public boolean applyBoolean(final int a, final int b) {
                return Doubles.doubleToBoolean(applyInteger(a, b));
            }
        },
        POWER(5, "^") {
            @Override
            public double applyDouble(final double a, final double b) {
                return Math.pow(a, b);
            }

            @Override
            public int applyInteger(final double a, final double b) {
                return Integers.checkedCastNoOverflow(applyDouble(a, b));
            }

            @Override
            public Boolean applyBooleanNullable(final double a, final double b) {
                return Doubles.doubleToBooleanNullable(applyDouble(a, b));
            }

            @Override
            public boolean applyBoolean(final double a, final double b) {
                return Doubles.doubleToBoolean(applyDouble(a, b));
            }

            @Override
            public double applyDouble(final int a, final int b) {
                return Math.pow(a, b);
            }

            @Override
            public int applyInteger(final int a, final int b) {
                return Integers.checkedCastNoOverflow(applyDouble(a, b));
            }

            @Override
            public Boolean applyBooleanNullable(final int a, final int b) {
                return Doubles.doubleToBooleanNullable(applyDouble(a, b));
            }

            @Override
            public boolean applyBoolean(final int a, final int b) {
                return Doubles.doubleToBoolean(applyDouble(a, b));
            }
        },
        LT(2, "<") {
            @Override
            public double applyDouble(final double a, final double b) {
                return Doubles.booleanToDouble(applyBoolean(a, b));
            }

            @Override
            public int applyInteger(final double a, final double b) {
                return Integers.booleanToInteger(applyBoolean(a, b));
            }

            @Override
            public Boolean applyBooleanNullable(final double a, final double b) {
                return applyBoolean(a, b);
            }

            @Override
            public boolean applyBoolean(final double a, final double b) {
                return Doubles.isLessThan(a, b);
            }

            @Override
            public double applyDouble(final int a, final int b) {
                return Doubles.booleanToDouble(applyBoolean(a, b));
            }

            @Override
            public int applyInteger(final int a, final int b) {
                return Integers.booleanToInteger(applyBoolean(a, b));
            }

            @Override
            public Boolean applyBooleanNullable(final int a, final int b) {
                return applyBoolean(a, b);
            }

            @Override
            public boolean applyBoolean(final int a, final int b) {
                return Integers.isLessThan(a, b);
            }
        },
        LT_EQ(2, "<=") {
            @Override
            public double applyDouble(final double a, final double b) {
                return Doubles.booleanToDouble(applyBoolean(a, b));
            }

            @Override
            public int applyInteger(final double a, final double b) {
                return Integers.booleanToInteger(applyBoolean(a, b));
            }

            @Override
            public Boolean applyBooleanNullable(final double a, final double b) {
                return applyBoolean(a, b);
            }

            @Override
            public boolean applyBoolean(final double a, final double b) {
                return Doubles.isLessThanOrEqualTo(a, b);
            }

            @Override
            public double applyDouble(final int a, final int b) {
                return Doubles.booleanToDouble(applyBoolean(a, b));
            }

            @Override
            public int applyInteger(final int a, final int b) {
                return Integers.booleanToInteger(applyBoolean(a, b));
            }

            @Override
            public Boolean applyBooleanNullable(final int a, final int b) {
                return applyBoolean(a, b);
            }

            @Override
            public boolean applyBoolean(final int a, final int b) {
                return Integers.isLessThanOrEqualTo(a, b);
            }
        },
        EQ(2, "==") {
            @Override
            public double applyDouble(final double a, final double b) {
                return Doubles.booleanToDouble(applyBoolean(a, b));
            }

            @Override
            public int applyInteger(final double a, final double b) {
                return Integers.booleanToInteger(applyBoolean(a, b));
            }

            @Override
            public Boolean applyBooleanNullable(final double a, final double b) {
                return applyBoolean(a, b);
            }

            @Override
            public boolean applyBoolean(final double a, final double b) {
                return Doubles.equals(a, b);
            }

            @Override
            public double applyDouble(final int a, final int b) {
                return Doubles.booleanToDouble(applyBoolean(a, b));
            }

            @Override
            public int applyInteger(final int a, final int b) {
                return Integers.booleanToInteger(applyBoolean(a, b));
            }

            @Override
            public Boolean applyBooleanNullable(final int a, final int b) {
                return applyBoolean(a, b);
            }

            @Override
            public boolean applyBoolean(final int a, final int b) {
                return Integers.equals(a, b);
            }
        },
        GT_EQ(2, ">=") {
            @Override
            public double applyDouble(final double a, final double b) {
                return Doubles.booleanToDouble(applyBoolean(a, b));
            }

            @Override
            public int applyInteger(final double a, final double b) {
                return Integers.booleanToInteger(applyBoolean(a, b));
            }

            @Override
            public Boolean applyBooleanNullable(final double a, final double b) {
                return applyBoolean(a, b);
            }

            @Override
            public boolean applyBoolean(final double a, final double b) {
                return Doubles.isGreaterThanOrEqualTo(a, b);
            }

            @Override
            public double applyDouble(final int a, final int b) {
                return Doubles.booleanToDouble(applyBoolean(a, b));
            }

            @Override
            public int applyInteger(final int a, final int b) {
                return Integers.booleanToInteger(applyBoolean(a, b));
            }

            @Override
            public Boolean applyBooleanNullable(final int a, final int b) {
                return applyBoolean(a, b);
            }

            @Override
            public boolean applyBoolean(final int a, final int b) {
                return Integers.isGreaterThanOrEqualTo(a, b);
            }
        },
        GT(2, ">") {
            @Override
            public double applyDouble(final double a, final double b) {
                return Doubles.booleanToDouble(applyBoolean(a, b));
            }

            @Override
            public int applyInteger(final double a, final double b) {
                return Integers.booleanToInteger(applyBoolean(a, b));
            }

            @Override
            public Boolean applyBooleanNullable(final double a, final double b) {
                return applyBoolean(a, b);
            }

            @Override
            public boolean applyBoolean(final double a, final double b) {
                return Doubles.isGreaterThan(a, b);
            }

            @Override
            public double applyDouble(final int a, final int b) {
                return Doubles.booleanToDouble(applyBoolean(a, b));
            }

            @Override
            public int applyInteger(final int a, final int b) {
                return Integers.booleanToInteger(applyBoolean(a, b));
            }

            @Override
            public Boolean applyBooleanNullable(final int a, final int b) {
                return applyBoolean(a, b);
            }

            @Override
            public boolean applyBoolean(final int a, final int b) {
                return Integers.isGreaterThan(a, b);
            }
        },
        NEQ(2, "!=") {
            @Override
            public double applyDouble(final double a, final double b) {
                return Doubles.booleanToDouble(applyBoolean(a, b));
            }

            @Override
            public int applyInteger(final double a, final double b) {
                return Integers.booleanToInteger(applyBoolean(a, b));
            }

            @Override
            public Boolean applyBooleanNullable(final double a, final double b) {
                return applyBoolean(a, b);
            }

            @Override
            public boolean applyBoolean(final double a, final double b) {
                return Doubles.notEquals(a, b);
            }

            @Override
            public double applyDouble(final int a, final int b) {
                return Doubles.booleanToDouble(applyBoolean(a, b));
            }

            @Override
            public int applyInteger(final int a, final int b) {
                return Integers.booleanToInteger(applyBoolean(a, b));
            }

            @Override
            public Boolean applyBooleanNullable(final int a, final int b) {
                return applyBoolean(a, b);
            }

            @Override
            public boolean applyBoolean(final int a, final int b) {
                return Integers.notEquals(a, b);
            }
        },
        AND(1, "&&") {
            @Override
            public double applyDouble(final double a, final double b) {
                throw new UnsupportedOperationException("use class " + AndOperation.class.getSimpleName());
            }

            @Override
            public int applyInteger(final double a, final double b) {
                throw new UnsupportedOperationException("use class " + AndOperation.class.getSimpleName());
            }

            @Override
            public Boolean applyBooleanNullable(final double a, final double b) {
                throw new UnsupportedOperationException("use class " + AndOperation.class.getSimpleName());
            }

            @Override
            public boolean applyBoolean(final double a, final double b) {
                throw new UnsupportedOperationException("use class " + AndOperation.class.getSimpleName());
            }

            @Override
            public double applyDouble(final int a, final int b) {
                throw new UnsupportedOperationException("use class " + AndOperation.class.getSimpleName());
            }

            @Override
            public int applyInteger(final int a, final int b) {
                throw new UnsupportedOperationException("use class " + AndOperation.class.getSimpleName());
            }

            @Override
            public Boolean applyBooleanNullable(final int a, final int b) {
                throw new UnsupportedOperationException("use class " + AndOperation.class.getSimpleName());
            }

            @Override
            public boolean applyBoolean(final int a, final int b) {
                throw new UnsupportedOperationException("use class " + AndOperation.class.getSimpleName());
            }
        },
        OR(1, "||") {
            @Override
            public double applyDouble(final double a, final double b) {
                throw new UnsupportedOperationException("use class " + OrOperation.class.getSimpleName());
            }

            @Override
            public int applyInteger(final double a, final double b) {
                throw new UnsupportedOperationException("use class " + OrOperation.class.getSimpleName());
            }

            @Override
            public Boolean applyBooleanNullable(final double a, final double b) {
                throw new UnsupportedOperationException("use class " + OrOperation.class.getSimpleName());
            }

            @Override
            public boolean applyBoolean(final double a, final double b) {
                throw new UnsupportedOperationException("use class " + OrOperation.class.getSimpleName());
            }

            @Override
            public double applyDouble(final int a, final int b) {
                throw new UnsupportedOperationException("use class " + OrOperation.class.getSimpleName());
            }

            @Override
            public int applyInteger(final int a, final int b) {
                throw new UnsupportedOperationException("use class " + OrOperation.class.getSimpleName());
            }

            @Override
            public Boolean applyBooleanNullable(final int a, final int b) {
                throw new UnsupportedOperationException("use class " + OrOperation.class.getSimpleName());
            }

            @Override
            public boolean applyBoolean(final int a, final int b) {
                throw new UnsupportedOperationException("use class " + OrOperation.class.getSimpleName());
            }
        },
        NOT(1, "!") {
            @Override
            public double applyDouble(final double a, final double b) {
                throw new UnsupportedOperationException("use class " + NotOperation.class.getSimpleName());
            }

            @Override
            public int applyInteger(final double a, final double b) {
                throw new UnsupportedOperationException("use class " + NotOperation.class.getSimpleName());
            }

            @Override
            public Boolean applyBooleanNullable(final double a, final double b) {
                throw new UnsupportedOperationException("use class " + NotOperation.class.getSimpleName());
            }

            @Override
            public boolean applyBoolean(final double a, final double b) {
                throw new UnsupportedOperationException("use class " + NotOperation.class.getSimpleName());
            }

            @Override
            public double applyDouble(final int a, final int b) {
                throw new UnsupportedOperationException("use class " + NotOperation.class.getSimpleName());
            }

            @Override
            public int applyInteger(final int a, final int b) {
                throw new UnsupportedOperationException("use class " + NotOperation.class.getSimpleName());
            }

            @Override
            public Boolean applyBooleanNullable(final int a, final int b) {
                throw new UnsupportedOperationException("use class " + NotOperation.class.getSimpleName());
            }

            @Override
            public boolean applyBoolean(final int a, final int b) {
                throw new UnsupportedOperationException("use class " + NotOperation.class.getSimpleName());
            }
        },
        CROSSES_ABOVE(1, "crosses above") {
            @Override
            public double applyDouble(final double a, final double b) {
                throw new UnsupportedOperationException("use class " + CrossesAboveOperation.class.getSimpleName());
            }

            @Override
            public int applyInteger(final double a, final double b) {
                throw new UnsupportedOperationException("use class " + CrossesAboveOperation.class.getSimpleName());
            }

            @Override
            public Boolean applyBooleanNullable(final double a, final double b) {
                throw new UnsupportedOperationException("use class " + CrossesAboveOperation.class.getSimpleName());
            }

            @Override
            public boolean applyBoolean(final double a, final double b) {
                throw new UnsupportedOperationException("use class " + CrossesAboveOperation.class.getSimpleName());
            }

            @Override
            public double applyDouble(final int a, final int b) {
                throw new UnsupportedOperationException("use class " + CrossesAboveOperation.class.getSimpleName());
            }

            @Override
            public int applyInteger(final int a, final int b) {
                throw new UnsupportedOperationException("use class " + CrossesAboveOperation.class.getSimpleName());
            }

            @Override
            public Boolean applyBooleanNullable(final int a, final int b) {
                throw new UnsupportedOperationException("use class " + CrossesAboveOperation.class.getSimpleName());
            }

            @Override
            public boolean applyBoolean(final int a, final int b) {
                throw new UnsupportedOperationException("use class " + CrossesAboveOperation.class.getSimpleName());
            }
        },
        CROSSES_BELOW(1, "crosses below") {
            @Override
            public double applyDouble(final double a, final double b) {
                throw new UnsupportedOperationException("use class " + CrossesBelowOperation.class.getSimpleName());
            }

            @Override
            public int applyInteger(final double a, final double b) {
                throw new UnsupportedOperationException("use class " + CrossesBelowOperation.class.getSimpleName());
            }

            @Override
            public Boolean applyBooleanNullable(final double a, final double b) {
                throw new UnsupportedOperationException("use class " + CrossesBelowOperation.class.getSimpleName());
            }

            @Override
            public boolean applyBoolean(final double a, final double b) {
                throw new UnsupportedOperationException("use class " + CrossesBelowOperation.class.getSimpleName());
            }

            @Override
            public double applyDouble(final int a, final int b) {
                throw new UnsupportedOperationException("use class " + CrossesBelowOperation.class.getSimpleName());
            }

            @Override
            public int applyInteger(final int a, final int b) {
                throw new UnsupportedOperationException("use class " + CrossesBelowOperation.class.getSimpleName());
            }

            @Override
            public Boolean applyBooleanNullable(final int a, final int b) {
                throw new UnsupportedOperationException("use class " + CrossesBelowOperation.class.getSimpleName());
            }

            @Override
            public boolean applyBoolean(final int a, final int b) {
                throw new UnsupportedOperationException("use class " + CrossesBelowOperation.class.getSimpleName());
            }
        };

        private final int priority;
        private String text;

        Op(final int priority, final String text) {
            this.priority = priority;
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }

        public int getPriority() {
            return priority;
        }

        public abstract double applyDouble(double a, double b);

        protected abstract int applyInteger(double a, double b);

        public abstract Boolean applyBooleanNullable(double a, double b);

        public abstract boolean applyBoolean(double a, double b);

        public abstract double applyDouble(int a, int b);

        protected abstract int applyInteger(int a, int b);

        public abstract Boolean applyBooleanNullable(int a, int b);

        public abstract boolean applyBoolean(int a, int b);

    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public boolean shouldPersist() {
        return left.shouldPersist() || right.shouldPersist();
    }

    @Override
    public boolean shouldDraw() {
        return left.shouldDraw() || right.shouldDraw();
    }

    @Override
    public IExpression[] getChildren() {
        return new IExpression[] { left, right };
    }

    public static BinaryOperation validateComparativeOperation(final IExpression condition) {
        if (!(condition instanceof BinaryOperation)) {
            throw new IllegalArgumentException(
                    "condition needs to be a " + BinaryOperation.class.getSimpleName() + ": " + condition);
        }
        final BinaryOperation binaryOperation = (BinaryOperation) condition;
        switch (binaryOperation.getOp()) {
        case EQ:
        case NEQ:
        case GT:
        case GT_EQ:
        case LT:
        case LT_EQ:
            break;
        default:
            throw new IllegalArgumentException("Comparative " + BinaryOperation.class.getSimpleName()
                    + " needs to be one of [" + BinaryOperation.Op.EQ + "," + BinaryOperation.Op.NEQ + ","
                    + BinaryOperation.Op.GT + "," + BinaryOperation.Op.GT_EQ + "," + BinaryOperation.Op.LT + ","
                    + BinaryOperation.Op.LT_EQ + "]: " + binaryOperation.getOp());
        }
        return binaryOperation;
    }

}
