package com.beddoed.offers.utils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TestUtils {

    public static <T> T randomOneOf(List<T> objectList) {
        final Random random = new Random();
        return objectList.get(random.nextInt(objectList.size()));
    }

    public static <T> T randomOneOf(T... items) {
        return randomOneOf(Arrays.asList(items));
    }

    public static boolean randomBoolean() {
        return new Random().nextBoolean();
    }

    public static BigDecimal randomBigDecimal() {
        return BigDecimal.valueOf(new Random().nextDouble());
    }
}
