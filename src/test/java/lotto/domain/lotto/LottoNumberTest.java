package lotto.domain.lotto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class LottoNumberTest {
    @Nested
    @DisplayName("로또 번호 생성(valueOf) 테스트")
    class ValueOfTest {
        @DisplayName("로또 번호를 생성한다.")
        @ParameterizedTest(name = "[{index}] {0} -> 로또 번호 생성 성공")
        @ValueSource(ints = {
                1, 45,          // 경계값
                12, 22, 32, 42  // 일반값
        })
        void valueOf_createLottoNumber_numberInrange(int inRangednumber) {
            LottoNumber lottoNumber = LottoNumber.valueOf(inRangednumber);

            assertThat(lottoNumber.getNumber()).isEqualTo(inRangednumber);
        }

        @DisplayName("숫자 범위를 벗어나는 로또 번호의 생성 시도에 대해서 예외를 발생시킨다.")
        @ParameterizedTest(name = "[{index}] {0} -> 예외 발생")
        @ValueSource(ints = {
                0, 46,  // 경계값
                -1      // 음수
        })
        void valueOf_throwsException_numberOutOfRange(int outOfRangedNumber) {
            assertThatThrownBy(() -> LottoNumber.valueOf(outOfRangedNumber))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContainingAll("[ERROR]", "범위", "1~45");
        }
    }

    @Nested
    @DisplayName("로또 번호 동등성(equals) 테스트")
    class EqualsTest {
        @DisplayName("로또 번호가 같으면 동등한 객체로 판단한다.")
        @Test
        void equals_returnTrue_numbersAreSame() {
            LottoNumber lottoNumberA = LottoNumber.valueOf(1);
            LottoNumber lottoNumberB = LottoNumber.valueOf(1);

            assertThat(lottoNumberA.equals(lottoNumberB)).isTrue();
        }

        @DisplayName("로또 번호가 같지 않으면 동등하지 않은 객체로 판단한다.")
        @Test
        void equals_returnFalse_numbersAreNotSame() {
            LottoNumber lottoNumberA = LottoNumber.valueOf(1);
            LottoNumber lottoNumberB = LottoNumber.valueOf(2);

            assertThat(lottoNumberA.equals(lottoNumberB)).isFalse();
        }
    }

    @Nested
    @DisplayName("로또 번호 간의 비교(compareTo) 테스트")
    class CompareToTest {
        @DisplayName("더 큰 번호와 비교하면 음수를 리턴한다.")
        @Test
        void compareTo_returnNegative_compareWithBiggerNumber() {
            LottoNumber lottoNumber = LottoNumber.valueOf(11);
            LottoNumber biggerNumber = LottoNumber.valueOf(21);

            assertThat(lottoNumber.compareTo(biggerNumber)).isNegative();
        }

        @DisplayName("더 작은 번호와 비교하면 양수를 리턴한다.")
        @Test
        void compareTo_returnPositive_compareWithSmallerNumber() {
            LottoNumber lottoNumber = LottoNumber.valueOf(11);
            LottoNumber smallerNumber = LottoNumber.valueOf(1);

            assertThat(lottoNumber.compareTo(smallerNumber)).isPositive();
        }

        @DisplayName("같은 번호와 비교하면 0을 리턴한다.")
        @Test
        void compareTo_returnZero_compareWithSameNumber() {
            LottoNumber lottoNumber = LottoNumber.valueOf(11);
            LottoNumber smallerNumber = LottoNumber.valueOf(11);

            assertThat(lottoNumber.compareTo(smallerNumber)).isZero();
        }
    }
}
