package lotto;

import java.util.List;
import lotto.domain.lotto.Lotto;
import lotto.domain.lotto.LottoVendingMachine;
import lotto.domain.winning.WinningLotto;
import lotto.domain.winning.WinningStatisticDto;
import lotto.domain.winning.WinningStatisticsCalculator;
import lotto.view.InputView;
import lotto.view.OutputView;

/**
 * 이 클래스는 로또 게임을 진행하기 위해 각 도메인과 View 객체들을 호출하며 조율한다.
 *
 * <p>이 클래스는 {@code final}로 선언되어 상속할 수 없다.
 *
 * <p>이 클래스는 로또 게임을 시작하기 위한 진입점이며, 인스턴스를 생성할 수 없다.
 * {@code run()} 정적 메서드를 통해서만 게임을 시작할 수 있다.
 */

public final class LottoGame {
    /** 로또 게임을 진행한다. */
    public static void run() {
        // 1. 구입 금액을 입력받고, 그만큼 로또를 발행
        int purchaseAmount = InputView.readValidPurchaseAmount();
        List<Lotto> lottos = LottoVendingMachine.purchase(purchaseAmount);
        OutputView.printPurchasedLottos(lottos);

        // 2. 당첨 번호와 보너스 번호를 입력받고, 당첨 로또를 결정
        Lotto winningNumbers = InputView.readValidWinningNumbers();
        WinningLotto winningLotto = InputView.readValidBonusNumberForWinningLotto(winningNumbers);

        // 3. 당첨 통계 및 수익률 출력 후 게임을 종료
        WinningStatisticDto winningStatisticDto = WinningStatisticsCalculator.calculate(winningLotto, lottos);
        OutputView.printWinningStatistic(winningStatisticDto);
    }

    private LottoGame() {
    }
}
