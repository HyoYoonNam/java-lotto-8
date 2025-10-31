package lotto.domain;


import camp.nextstep.edu.missionutils.Randoms;
import java.util.ArrayList;
import java.util.List;
import lotto.constant.LottoConstant;
import lotto.constant.exception.ErrorMessage;

/**
 * 이 클래스는 로또를 발행하는 책임을 가진다.
 *
 * <p>이 클래스는 불변이므로 상속할 수 없다. 또한 인스턴스를 생성할 수 없는 정적 유틸 클래스이다.
 */

public final class LottoVendingMachine {
    private LottoVendingMachine() {
    }

    /**
     * 구매할 금액만큼의 로또를 발행하여 리턴한다.
     *
     * @param purchaseAmount 구매할 금액; {@value lotto.constant.LottoConstant#LOTTO_PRICE}원으로 나누어 떨어져야 한다.
     * @return 발행된 로또들; 이 리스트는 불변이다.
     * @throws IllegalArgumentException {@code @param}의 전제를 위반하면 발생한다.
     * @see Lotto
     */
    public static List<Lotto> purchase(final int purchaseAmount) {
        boolean isDivisibleByLottoPrice = purchaseAmount % LottoConstant.LOTTO_PRICE == 0;
        if (!isDivisibleByLottoPrice) {
            throw new IllegalArgumentException(ErrorMessage.PURCHASE_AMOUNT_IS_NOT_DIVISIBLE_BY_LOTTO_PRICE
                    .build(purchaseAmount));
        }

        List<Lotto> lottos = new ArrayList<>();
        int lottoAmount = purchaseAmount / LottoConstant.LOTTO_PRICE;
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
