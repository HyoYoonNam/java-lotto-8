package lotto;

import lotto.domain.lotto.Lotto;
import lotto.domain.winning.WinningLotto;
import lotto.view.InputView;

public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현
        int purchaseAmount = InputView.readValidPurchaseAmount();

        Lotto winningNumbers = InputView.readValidWinningNumbers();
        WinningLotto winningLotto = InputView.readValidBonusNumber(winningNumbers);

    }
}
