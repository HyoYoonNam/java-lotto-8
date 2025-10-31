package lotto.domain;


import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lotto.constant.LottoConstant;
import lotto.constant.exception.ErrorMessage;

/**
 * 이 클래스는 {@value lotto.constant.LottoConstant#LOTTO_NUMBER_LOWER_RANGE_INCLUSIVE}이상
 * {@value lotto.constant.LottoConstant#LOTTO_NUMBER_UPPER_RANGE_INCLUSIVE} 이하의 로또 번호 한 개를 나타내는 Value Object이다.
 *
 * <p>이 클래스는 자신이 나타내는 로또 번호를 기준으로 오름차순 정렬된다.
 *
 * <p>이 클래스는 불변이며, {@code valueOf()} 정적 팩토리 메서드를 통해서만 인스턴스를 생성할 수 있다.
 */

public final class LottoNumber implements Comparable<LottoNumber> {
    private static final Map<Integer, LottoNumber> CACHE = new HashMap<>(
            IntStream.rangeClosed(LottoConstant.LOTTO_NUMBER_LOWER_RANGE_INCLUSIVE,
                            LottoConstant.LOTTO_NUMBER_UPPER_RANGE_INCLUSIVE)
                    .boxed()
                    .collect(Collectors.toMap(number -> number, LottoNumber::new))
    );

    private final int number;

    private LottoNumber(int number) {
        this.number = number;
    }

    /**
     * 인자로 받은 {@code number}에 대응되는 {@code LottoNumber} 객체를 리턴한다.
     *
     * @param number 리턴되는 {@code LottoNumber} 객체가 나타나게 될 로또 번호
     *        {@value lotto.constant.LottoConstant#LOTTO_NUMBER_LOWER_RANGE_INCLUSIVE} 이상이고,
     *        {@value lotto.constant.LottoConstant#LOTTO_NUMBER_UPPER_RANGE_INCLUSIVE} 이하여야 한다.
     * @throws IllegalArgumentException {@code @param}의 전제를 위반하면 발생한다.
     */
    public static LottoNumber valueOf(int number) {
        if (LottoConstant.LOTTO_NUMBER_UPPER_RANGE_INCLUSIVE < number ||
                number < LottoConstant.LOTTO_NUMBER_LOWER_RANGE_INCLUSIVE) {
            // TODO: 상위 계층에서 예외 처리 필요 (입력 다시 받게)
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
    public int compareTo(LottoNumber lottoNumber) {
        return Integer.compare(number, lottoNumber.number);
    }

    @Override
    public String toString() {
        return String.valueOf(number);
    }
}
