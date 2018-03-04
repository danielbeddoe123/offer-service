package com.beddoed.offers.utils;

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
}
