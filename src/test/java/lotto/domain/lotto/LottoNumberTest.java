package lotto.domain.lotto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class LottoNumberTest {
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

    @DisplayName("로또 번호가 같으면 동일한 객체로 판단한다")
    @Test
    void equals_returnTrue_numbersAreSame() {
        LottoNumber lottoNumberA = LottoNumber.valueOf(1);
        LottoNumber lottoNumberB = LottoNumber.valueOf(1);

        assertThat(lottoNumberA.equals(lottoNumberB)).isTrue();
    }
}
