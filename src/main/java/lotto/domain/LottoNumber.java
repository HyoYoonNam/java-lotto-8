package lotto.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        return CACHE.get(number);
    }

    public int getNumber() {
        return number;
    }
}
