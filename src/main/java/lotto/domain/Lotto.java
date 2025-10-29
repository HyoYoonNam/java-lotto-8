package lotto.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lotto.exception.ErrorMessage;

/**
 * 이 클래스는 {@code equals}와 {@code hashCode}를 재정의 하지 않는다.
 *
 * <p>생성시 인자로 받은 {@code numbers}가 모두 동일하더라도 두 로또는 서로 다른 로또로 비교되어야 하기 때문이다.
 * 예를 들어 3등 당첨인 1, 2, 3, 4, 5, 6 로또가 두 장 존재할 때 당첨된 로또의 수는 반드시 2개여야 한다.
 */
public class Lotto {
    private static final int NUMBER_SIZE = 6;

    private final List<LottoNumber> numbers;

    private Lotto(List<Integer> numbers) {
        validate(numbers);
        this.numbers = numbers.stream()
                .map(LottoNumber::valueOf)
                .toList();
    }

    /**
     * 로또 번호 6개를 받아 로또 1개를 리턴한다.
     *
     * @param numbers 로또가 가지는 로또 번호들; 그 수는 6개여야 하고, 서로 중복되면 안 된다. 또한 각 로또 번호는 1 이상 45 이하의 정수여야 한다.
     * @return 6개의 번호를 가지는 로또
     * @throws IllegalArgumentException 아래 상황 중 하나라도 해당되면 예외가 발생한다.
     *         - numbers가 가지는 로또 번호의 개수가 6개가 아닌 경우
     *         - numbers가 가지는 로또 번호 사이에 중복이 존재하는 경우
     *         - numbers가 가지는 로또 번호 중 1 이상 45 이하의 정수가 아닌 것이 존재하는 경우
     */
    public static Lotto from(List<Integer> numbers) {
        return new Lotto(numbers);
    }

    private static void validate(List<Integer> numbers) {
        validateNumbersSize(numbers);
        validateNoDuplicateNumbers(numbers);
    }

    private static void validateNumbersSize(List<Integer> numbers) {
        if (numbers.size() != NUMBER_SIZE) {
            throw new IllegalArgumentException(ErrorMessage.LOTTO_NUMBER_COUNT_INVALID.build(numbers.size()));
        }
    }

    private static void validateNoDuplicateNumbers(List<Integer> numbers) {
        Set<Integer> duplicates = findDuplicateNumbers(numbers);
        if (!duplicates.isEmpty()) { // TODO: 이거까지 duplicateExists로 추출해낼지 고민인데, 지금만으로도 의미가 명확하고, 추출은 투머치 같아서 아마 안 할 듯
            throw new IllegalArgumentException(ErrorMessage.LOTTO_NUMBER_DUPLICATED.build(numbers));
        }
    }

    private static Set<Integer> findDuplicateNumbers(List<Integer> numbers) {
        Set<Integer> duplicates = new HashSet<>();
        Set<Integer> uniqueNumbers = new HashSet<>();

        numbers.forEach(number -> {
            if (!uniqueNumbers.add(number)) {
                duplicates.add(number);
            }
        });

        return duplicates;
    }
}
