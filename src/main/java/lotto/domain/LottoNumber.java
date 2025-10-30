package lotto.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lotto.exception.ErrorMessage;

/**
 * 이 클래스는 1이상 45 이하의 로또 번호 한 개를 나타내는 Value Object이다.
 *
 * <p>이 클래스는 불변이며, {@code valueOf()} 정적 팩토리 메서드를 통해서만 인스턴스를 생성할 수 있다.
 */

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

    /**
     * 인자로 받은 number에 대응되는 LottoNumber 객체를 리턴한다.
     *
     * @param number 리턴되는 LottoNumber 객체가 가지게 될 로또 번호; 1 이상이고 45 이하여야 한다.
     * @throws IllegalArgumentException number가 범위를 벗어나면,
     *         즉, ({@code number < 1 || number > 45})이면 발생한다.
     */
    public static LottoNumber valueOf(int number) {
        if (UPPER_RANGE_INCLUSIVE < number || number < LOWER_RANGE_INCLUSIVE) {
            // TODO: 상위 계층에서 예외 처리 필요 (입력 다시 받게)
            throw new IllegalArgumentException(ErrorMessage.LOTTO_NUMBER_OUT_OF_RANGE.build(number));
        }

        return CACHE.get(number);
    }

    public int getNumber() {
        return number;
    }

    /** 이 객체와 다른 객체를 로또 번호 기준으로 동등성 검사를 한다. */
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
