package lotto.domain;

import camp.nextstep.edu.missionutils.Randoms;
import java.util.ArrayList;
import java.util.List;
import lotto.exception.ErrorMessage;

public final class LottoVendingMachine {
    private static final int LOTTO_PRICE = 1000;

    private LottoVendingMachine() {
    }

    /**
     * 구매할 금액만큼의 로또를 발행하여 리턴한다.
     *
     * @param purchaseAmount 구매할 금액; 1,000원으로 나누어 떨어져야 한다.
     * @return 발행된 로또들; 이 리스트는 불변이다.
     * @throws IllegalArgumentException {@code purchaseAmount}의 전제를 위반하면 발생한다.
     */
    public static List<Lotto> purchase(final int purchaseAmount) {
        boolean isDivisibleByLottoPrice = purchaseAmount % LOTTO_PRICE == 0;
        if (!isDivisibleByLottoPrice) {
            throw new IllegalArgumentException(ErrorMessage.PURCHASE_AMOUNT_IS_NOT_DIVISIBLE_BY_LOTTO_PRICE
                    .build(purchaseAmount));
        }

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
