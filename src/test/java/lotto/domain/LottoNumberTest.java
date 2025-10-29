package lotto.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class LottoNumberTest {
    @ParameterizedTest(name = "[{index}] {0} -> 로또 번호 생성 성공")
    @ValueSource(ints = {
            1, 45,          // 경계값
            12, 22, 32, 42  // 일반값
    })
    @DisplayName("로또 번호를 생성한다.")
    void valueOf_createLottoNumber_numberInrange(int inRangednumber) {
        LottoNumber lottoNumber = LottoNumber.valueOf(inRangednumber);

        assertThat(lottoNumber.getNumber()).isEqualTo(inRangednumber);
    }
}
