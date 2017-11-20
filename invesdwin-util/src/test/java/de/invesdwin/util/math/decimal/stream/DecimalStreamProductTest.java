package de.invesdwin.util.math.decimal.stream;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.concurrent.NotThreadSafe;

import org.junit.Test;

import de.invesdwin.util.assertions.Assertions;
import de.invesdwin.util.math.decimal.Decimal;

@NotThreadSafe
public class DecimalStreamProductTest {

    @Test
    public void testIsSameAsMultiplication() {
        final DecimalStreamProduct<Decimal> productByLogSumStream = new DecimalStreamProduct<Decimal>(Decimal.ZERO) {
            @Override
            protected Decimal getValueAdjustmentAddition() {
                return Decimal.ONE;
            }
        };
        final List<Decimal> values = new ArrayList<>();
        values.add(new Decimal("0.1"));
        values.add(new Decimal("0.2"));
        values.add(new Decimal("0.3"));
        Decimal productByMultiplication = null;
        for (final Decimal value : values) {
            if (productByMultiplication == null) {
                productByMultiplication = value.add(Decimal.ONE);
            } else {
                productByMultiplication = productByMultiplication.multiply(value.add(Decimal.ONE));
            }
            productByLogSumStream.process(value);
        }
        productByMultiplication = productByMultiplication.subtract(Decimal.ONE);
        final Decimal productByLogSum = productByLogSumStream.getProduct();
        Assertions.assertThat(productByLogSum.round()).isEqualTo(new Decimal("0.716"));
        Assertions.assertThat(productByMultiplication).isEqualTo(new Decimal("0.716"));

    }

}
