package lotto.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lotto.exception.ErrorMessage;

public final class LottoNumber {
    private static final int LOWER_RANGE_INCLUSIVE = 1;
    private static final int UPPER_RANGE_INCLUSIVE = 45;
    private static final Map<Integer, LottoNumber> CACHE = new HashMap<>(
            IntStream.rangeClosed(LOWER_RANGE_INCLUSIVE, UPPER_RANGE_INCLUSIVE)
                    .boxed()
                    .collect(Collectors.toMap(number -> number, LottoNumber::new))
    );

    private final int number;

    private LottoNumber(int number) {
        this.number = number;
    }

    public static LottoNumber valueOf(int number) {
        if (UPPER_RANGE_INCLUSIVE < number || number < LOWER_RANGE_INCLUSIVE) {
            throw new IllegalArgumentException(ErrorMessage.LOTTO_NUMBER_OUT_OF_RANGE.build(number));
        }

        return CACHE.get(number);
    }

    public int getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }

        return (anObject instanceof LottoNumber aLottoNumber)
                && (number == aLottoNumber.number);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(number);
    }

    @Override
    public String toString() {
        return String.valueOf(number);
    }
}
