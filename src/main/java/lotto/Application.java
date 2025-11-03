package lotto;

import java.util.List;
import lotto.domain.winning.WinningLotto;
import lotto.view.InputView;

public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현
        int purchaseAmount = InputView.readValidPurchaseAmount();

        WinningLotto winningLotto = InputView.readValidWinningLotto();

    }
}
