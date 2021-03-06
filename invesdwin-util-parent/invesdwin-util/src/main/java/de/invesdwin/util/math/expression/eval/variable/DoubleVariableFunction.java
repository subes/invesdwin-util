package de.invesdwin.util.math.expression.eval.variable;

import javax.annotation.concurrent.Immutable;

import de.invesdwin.util.math.expression.ExpressionReturnType;
import de.invesdwin.util.math.expression.IExpression;
import de.invesdwin.util.math.expression.IFunctionParameterInfo;
import de.invesdwin.util.math.expression.function.ADoubleFunction;
import de.invesdwin.util.math.expression.lambda.IEvaluateDouble;
import de.invesdwin.util.math.expression.lambda.IEvaluateDoubleFDate;
import de.invesdwin.util.math.expression.lambda.IEvaluateDoubleKey;
import de.invesdwin.util.math.expression.tokenizer.ExpressionContextUtil;

@Immutable
public class DoubleVariableFunction extends ADoubleFunction {

    private final DoubleVariableReference variable;

    public DoubleVariableFunction(final DoubleVariableReference variable) {
        this.variable = variable;
    }

    @Override
    public String getExpressionName() {
        return variable.getVariable().getExpressionName();
    }

    @Override
    public String getName() {
        return variable.getVariable().getName();
    }

    @Override
    public String getDescription() {
        return variable.getVariable().getDescription();
    }

    @Override
    public IFunctionParameterInfo getParameterInfo(final int index) {
        throw new ArrayIndexOutOfBoundsException(index);
    }

    @Override
    public int getNumberOfArguments() {
        return 0;
    }

    @Override
    public IEvaluateDoubleFDate newEvaluateDoubleFDate(final String context, final IExpression[] args) {
        return variable.newEvaluateDoubleFDate();
    }

    @Override
    public IEvaluateDoubleKey newEvaluateDoubleKey(final String context, final IExpression[] args) {
        return variable.newEvaluateDoubleKey();
    }

    @Override
    public IEvaluateDouble newEvaluateDouble(final String context, final IExpression[] args) {
        return variable.newEvaluateDouble();
    }

    @Override
    public boolean isNaturalFunction(final IExpression[] args) {
        return variable.isConstant();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final String context = variable.getContext();
        ExpressionContextUtil.putContext(context, sb);
        sb.append(getExpressionName());
        return sb.toString();
    }

    @Override
    public ExpressionReturnType getReturnType() {
        return variable.getVariable().getReturnType();
    }

    @Override
    public boolean shouldCompress() {
        return variable.shouldCompress();
    }

    @Override
    public boolean shouldPersist() {
        return variable.shouldPersist();
    }

    @Override
    public boolean shouldDraw() {
        return variable.shouldDraw();
    }

}
