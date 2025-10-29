package lotto.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lotto.exception.ErrorMessage;

public class Lotto {
    private static final int NUMBER_SIZE = 6;

    private final List<LottoNumber> numbers;

    private Lotto(List<Integer> numbers) {
        validate(numbers);
        this.numbers = numbers.stream()
                .map(LottoNumber::valueOf)
                .toList();
    }

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
