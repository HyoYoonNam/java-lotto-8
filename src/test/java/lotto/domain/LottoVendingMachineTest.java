package lotto.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}
