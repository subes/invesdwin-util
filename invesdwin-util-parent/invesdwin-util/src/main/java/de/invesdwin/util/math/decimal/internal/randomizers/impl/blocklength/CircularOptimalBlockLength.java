package de.invesdwin.util.math.decimal.internal.randomizers.impl.blocklength;

import java.util.List;

import javax.annotation.concurrent.NotThreadSafe;

import de.invesdwin.util.math.Doubles;
import de.invesdwin.util.math.decimal.ADecimal;
import de.invesdwin.util.math.decimal.IDecimalAggregate;

/**
 * Politis, N. Dimitris, White Halbert, "Automatic Block-Length Selection for the Dependent Bootstrap", Econometric
 * Reviews , 2004
 * 
 * Politis, D., White, H., Patton Andrew,"CORRECTION TO 'Automatic Block-Length Selection for the Dependent Bootstrap'",
 * Econometric Reviews, 28(4):372–375, 2009
 * 
 * http://www.math.ucsd.edu/~politis/SOFT/PPW/ppw.R
 * 
 */
@NotThreadSafe
public class CircularOptimalBlockLength<E extends ADecimal<E>> {
    private static final double DISTRIBUTION_CONSTANT = 1.959964D;
    private static final int MIN_CHECK_LAG_INTERVAL = 5;
    private static final double ONE_THIRD = 1D / 3D;
    private static final double MULTIPLICATOR_ONE_AND_A_THIRD = 1D + ONE_THIRD;

    private final List<E> sample;
    private final double sampleAvg;
    private final double sampleAutoCovariance0;

    public CircularOptimalBlockLength(final IDecimalAggregate<E> parent) {
        this.sample = parent.values();
        this.sampleAvg = parent.avg().doubleValue();
        this.sampleAutoCovariance0 = sampleAutoCovariance(0);
    }

    private long determineOptimalLag() {
        final int length = sample.size();
        final int checkLag = determineOptimalLag_checkLagInterval(length);
        final long maxLag = determineOptimalLag_maxlag(length);
        final double correlationThreshold = determineOptimalLag_correlationThreshold(length);

        int prevHalfLag = 0;
        int halfLag = 0;
        int lagIncrements = 0;
        int curLagIdx = 1;
        while (curLagIdx <= maxLag) {
            final double absCorrelation = Doubles.abs(sampleAutoCorrelation(curLagIdx));
            if (absCorrelation > correlationThreshold) {
                prevHalfLag = curLagIdx;
                lagIncrements = 0;
            } else {
                lagIncrements++;
                if (lagIncrements == checkLag) {
                    halfLag = curLagIdx - checkLag + 1;
                    break;
                }
            }
            curLagIdx++;
        }
        if (halfLag == 0) {
            halfLag = prevHalfLag;
        }
        final long limitedLag = Math.min(2 * halfLag, maxLag);
        return limitedLag;
    }

    private int determineOptimalLag_checkLagInterval(final int length) {
        final double logLength = Math.log10(length);
        final double sqrtLogLength = Math.sqrt(logLength);
        final int roundedSqrtLogLength = (int) Math.ceil(sqrtLogLength);
        return Math.max(MIN_CHECK_LAG_INTERVAL, roundedSqrtLogLength);
    }

    private long determineOptimalLag_maxlag(final int length) {
        final double sqrtLength = Math.sqrt(length);
        final long roundedSqrtLength = (long) Math.ceil(sqrtLength);
        final int checkLagInterval = determineOptimalLag_checkLagInterval(length);
        final long maxLag = roundedSqrtLength + checkLagInterval;
        final int absMaxLag = length - 1;
        return Math.min(absMaxLag, maxLag);
    }

    private double determineOptimalLag_correlationThreshold(final int length) {
        final double logLengthPerLength = Math.log10(length) / length;
        final double sqrtLogLengthPerLength = Math.sqrt(logLengthPerLength);
        return DISTRIBUTION_CONSTANT * sqrtLogLengthPerLength;
    }

    private double determineOptimalBlockLength_lagMultiplicator(final double value) {
        final double absValue = Doubles.abs(value);
        if (absValue <= 0.5D) {
            return 1D;
        } else if (absValue <= 1D) {
            return 2D * (1D - absValue);
        } else {
            return 0D;
        }
    }

    private long determineOptimalBlockLength_maxBlockLength(final int length) {
        final double sqrtLength = Math.sqrt(length);
        final double threeTimesSqrtLength = 3D * sqrtLength;
        final double oneThirdLength = length / 3D;
        final double min = Math.min(threeTimesSqrtLength, oneThirdLength);
        final long rounded = (long) Math.ceil(min);
        return rounded;
    }

    public int getBlockLength() {
        final int optimalBlockLength = (int) determineOptimalBlockLength();
        return Math.max(1, optimalBlockLength);
    }

    private long determineOptimalBlockLength() {
        final int length = sample.size();
        final long optimalLag = determineOptimalLag();
        double sumTwoLagMultiCovar = sampleAutoCovariance0;
        double sumTwoLagMultiLagCovar = 0D;
        for (int curLag = 1; curLag <= optimalLag; curLag++) {
            final double lagMultiplicator = determineOptimalBlockLength_lagMultiplicator(1D * curLag / optimalLag);
            final double covariance = sampleAutoCovariance(curLag);
            sumTwoLagMultiCovar += 2D * lagMultiplicator * covariance;
            sumTwoLagMultiLagCovar += 2D * lagMultiplicator * curLag * covariance;
        }
        final double blockLengthDivisor = sumTwoLagMultiCovar * sumTwoLagMultiCovar
                * determineOptimalBlockLength_blockLengthMultiplicator();
        double blockLength = Math
                .pow(2D * sumTwoLagMultiLagCovar * sumTwoLagMultiLagCovar * length / blockLengthDivisor, ONE_THIRD);
        final double maxBlockLength = determineOptimalBlockLength_maxBlockLength(length);

        blockLength = Doubles.between(blockLength, 1D, maxBlockLength);
        return Math.round(blockLength);
    }

    protected double determineOptimalBlockLength_blockLengthMultiplicator() {
        return MULTIPLICATOR_ONE_AND_A_THIRD;
    }

    private double sampleAutoCorrelation(final int lag) {
        return sampleAutoCovariance(lag) / sampleAutoCovariance0;
    }

    private double sampleAutoCovariance(final int lag) {
        if (lag >= sample.size()) {
            throw new IllegalArgumentException(
                    "Index needs to be smaller than sample size [" + sample.size() + "]: " + lag);
        }
        final int length = sample.size();
        double sum = 0D;
        final int maxIdx = length - lag - 1;
        for (int i = 0; i <= maxIdx; ++i) {
            final double curAdj = sample.get(i).doubleValue() - sampleAvg;
            final double nextAdj = sample.get(i + lag).doubleValue() - sampleAvg;
            sum += curAdj * nextAdj;
        }
        return sum / length;
    }

}
