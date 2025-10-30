package lotto.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class LottoVendingMachineTest {
    private static final int LOTTO_PRICE = 1000;

    @Test
    @DisplayName("구입 금액에 해당하는 만큼 로또를 발행한다.")
    void purchase_ReturnLottosAsPurchaseAmount_validPurchaseAmount() {
        int purchaseAmount = 1000;
        int expectedLottoCount = purchaseAmount / LOTTO_PRICE;

        List<Lotto> lottos = LottoVendingMachine.purchase(purchaseAmount);

        assertThat(lottos).hasSize(expectedLottoCount);
    }

    @ParameterizedTest(name = "[{index}] {0} -> 예외 발생")
    @ValueSource(ints = {
            999, 1001,  // 경계값
            1200        // 일반값
    })
    @DisplayName("구입 금액이 1,000원으로 나누어 떨어지지 않는다면 예외를 발생한다")
    void purchase_throwsException_invalidPurchaseAmount(int invalidPurchaseAmount) {
        assertThatThrownBy(() -> LottoVendingMachine.purchase(invalidPurchaseAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContainingAll("[ERROR]", "1,000");
    }
}
