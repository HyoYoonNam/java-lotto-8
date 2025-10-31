package lotto.domain;

import java.util.List;
import lotto.constant.exception.ErrorMessage;

/**
 * 이 클래스는 당첨 번호(6개)와 보너스 번호(1개)를 가지는 당첨 로또이다.
 *
 * <p>이 클래스는 정적 팩토리 메서드인 {@code of(List<Integer>, int)}를 통해서만 인스턴스를 생성할 수 있다.
 *
 * <p>이 클래스는 {@code final}로 선언되어 상속할 수 없다.
 */

public final class WinningLotto {
    private final List<LottoNumber> winningNumbers;
    private final LottoNumber bonusNumber;

    private WinningLotto(List<LottoNumber> winningNumbers, LottoNumber bonusNumber) {
        if (winningNumbers.contains(bonusNumber)) {
            throw new IllegalArgumentException(
                    ErrorMessage.DUPLICATES_BETWEEN_WINNING_NUMBERS_AND_BONUS_NUMBER.build(bonusNumber));
        }

        this.winningNumbers = winningNumbers;
        this.bonusNumber = bonusNumber;
    }

    /**
     * 당첨 번호 6개와 보너스 번호 1개를 입력받아 이를 포함하는 인스턴스를 리턴한다.
     *
     * @param winningNumbers 당첨 번호들은 {@link LottoNumber}와 {@link Lotto}의 전제를 만족해야 한다.
     * @param bonusNumber 보너스 번호는 {@link LottoNumber}의 전제를 따르며,
     *         {@code @param winningNumbers}와 중복되면 안 된다.
     * @throws IllegalArgumentException {@code @param}의 전제 중 하나라도 위반하면 발생한다.
     */
    public static WinningLotto of(List<Integer> winningNumbers, int bonusNumber) {
        List<Integer> winningNumbersCopy = List.copyOf(winningNumbers);

        List<LottoNumber> winningLottoNumbers = winningNumbersCopy.stream()
                .map(LottoNumber::valueOf)
                .toList();
        LottoNumber bonusLottoNumber = LottoNumber.valueOf(bonusNumber);

        return new WinningLotto(winningLottoNumbers, bonusLottoNumber);
    }

    public List<Integer> getWinningNumbers() {
        return winningNumbers.stream()
                .mapToInt(LottoNumber::getNumber)
                .boxed()
                .toList();
    }

    public int getBonusNumber() {
        return bonusNumber.getNumber();
    }
}
