package lotto.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Lotto {
    private static final int NUMBER_SIZE = 6;

    private final List<Integer> numbers;

    public Lotto(List<Integer> numbers) {
        validate(numbers);
        this.numbers = numbers;
    }

    private void validate(List<Integer> numbers) {
        validateNumbersSize(numbers);
        validateNoDuplicateNumbers(numbers);
    }

    private static void validateNumbersSize(List<Integer> numbers) {
        if (numbers.size() != NUMBER_SIZE) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 " + NUMBER_SIZE + "개여야 합니다.");
        }
    }

    private static void validateNoDuplicateNumbers(List<Integer> numbers) {
        Set<Integer> duplicates = findDuplicateNumbers(numbers);
        if (!duplicates.isEmpty()) { // TODO: 이거까지 duplicateExists로 추출해낼지 고민인데, 지금만으로도 의미가 명확하고, 추출은 투머치 같아서 아마 안 할 듯
            throw new IllegalArgumentException("[ERROR] 로또 번호에 중복이 존재합니다.");
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
