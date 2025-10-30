package lotto.domain;

import camp.nextstep.edu.missionutils.Randoms;
import java.util.ArrayList;
import java.util.List;

public final class LottoVendingMachine {
    private static final int LOTTO_PRICE = 1000;

    private LottoVendingMachine() {
    }

    /**
     * 구매할 금액만큼의 로또를 발행하여 리턴한다.
     *
     * @param purchaseAmount 구매할 금액
     * @return 발행된 로또들
     *         이 리스트는 불변입니다.
     */
    public static List<Lotto> purchase(final int purchaseAmount) {
        List<Lotto> lottos = new ArrayList<>();
        int lottoAmount = purchaseAmount / LOTTO_PRICE;
        for (int i = 0; i < lottoAmount; i++) {
            lottos.add(issue());
        }
        return List.copyOf(lottos);
    }

    private static Lotto issue() {
        List<Integer> numbers = Randoms.pickUniqueNumbersInRange(1, 45, 6);
        return Lotto.from(numbers);
    }
}
