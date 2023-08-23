package value.generator.api;

import value.generator.fixed.FixedValueGenerator;
import value.generator.random.impl.bool.RandomBooleanValueGenerator;
import value.generator.random.impl.numeric.RandomFloatValueGenerator;
import value.generator.random.impl.numeric.RandomIntegerValueGenerator;
import value.generator.random.impl.string.RandomStringValueGenerator;

public interface ValueGeneratorFactory {

    static <T> ValueGenerator<T> createFixed(T value) {
        return new FixedValueGenerator<>(value);
    }

    static ValueGenerator<Boolean> createRandomBoolean() {
        return new RandomBooleanValueGenerator();
    }

    static ValueGenerator<Integer> createRandomInteger(Integer from, Integer to) {
        return new RandomIntegerValueGenerator(from, to);
    }

    static ValueGenerator<Float> createRandomFloat(Float from, Float to) {
        return new RandomFloatValueGenerator(from, to);
    }

    static ValueGenerator<String> createRandomString() {
        return new RandomStringValueGenerator();
    }

}
