package lotto.domain.lotto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LottoTest {
    @Nested
    @DisplayName("로또 생성(from) 테스트")
    class OfTest {
        @Test
        void 로또_번호의_개수가_6개가_넘어가면_예외가_발생한다() {
            assertThatThrownBy(() -> Lotto.from(List.of(1, 2, 3, 4, 5, 6, 7)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("로또 번호에 중복된 숫자가 있으면 예외가 발생한다.")
        @Test
        void 로또_번호에_중복된_숫자가_있으면_예외가_발생한다() {
            assertThatThrownBy(() -> Lotto.from(List.of(1, 2, 3, 4, 5, 5)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("로또가 가지는 로또 번호는 오름차순으로 정렬된다")
        @Test
        void from_ReturnSortedLottoNumbers_numbersAreUnsorted() {
            List<Integer> unsortedNumbers = List.of(1, 5, 2, 4, 6, 3);

            Lotto lotto = Lotto.from(unsortedNumbers);

            assertThat(lotto.getNumbers()).isSorted();
        }
    }

    @Nested
    @DisplayName("로또 번호의 포함 여부(contains) 테스트")
    class ContainsTest {
        @DisplayName("로또가 번호를 포함하면 true를 리턴한다.")
        @Test
        void contains_returnTrue_lottoContainsNumber() {
            int containedNumber = 1;
            Lotto lotto = Lotto.from(List.of(containedNumber, 2, 3, 4, 5, 6));
            LottoNumber lottoNumber = LottoNumber.valueOf(containedNumber);

            assertThat(lotto.contains(lottoNumber)).isTrue();
        }

        @DisplayName("로또가 번호를 포함하지 않으면 false를 리턴한다.")
        @Test
        void contains_returnFalse_lottoNotContainsNumber() {
            Lotto lotto = Lotto.from(List.of(2, 3, 4, 5, 6, 7));
            LottoNumber lottoNumber = LottoNumber.valueOf(1);

            assertThat(lotto.contains(lottoNumber)).isFalse();
        }
    }

    @Nested
    @DisplayName("로또가 가지는 번호들(getNumbers) 테스트")
    class GetNumbers {
        @DisplayName("로또가 가진 번호가 정렬되어 리턴된다.")
        @Test
        void getNumbers_returnSortedNumbers_lottoFromUnsortedNumbers() {
            List<Integer> unsortedNumbers = List.of(1, 5, 2, 4, 6, 3);
            Lotto lotto = Lotto.from(unsortedNumbers);

            List<Integer> numbers = lotto.getNumbers();

            assertThat(numbers).containsExactly(1, 2, 3, 4, 5, 6);
        }
    }
}
