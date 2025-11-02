package lotto.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class WinningLottoTest {
    @DisplayName("당첨 번호와 보너스 번호 사이에 중복이 존재하면 예외를 발생한다.")
    @Test
    void of_throwsException_duplicatesBetweenWinningNumbersAndBonusNumber() {
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
        int bonusNumber = 2;
        assertThatThrownBy(() -> WinningLotto.of(winningNumbers, bonusNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContainingAll("[ERROR]", "중복");
    }

    @DisplayName("당첨 번호 사이에 중복이 존재하면 예외를 발생한다.")
    @Test
    void of_throwsException_duplicatesBetweenWinningNumbers() {
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 5);
        int bonusNumber = 10;
        assertThatThrownBy(() -> WinningLotto.of(winningNumbers, bonusNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContainingAll("[ERROR]", "중복");
    }

    @DisplayName("로또가 맞춘 당첨 번호의 수를 계산한다.")
    @Test
    void calculateMatchCount_returnMatchConut_lotto() {
        WinningLotto winningLotto = WinningLotto.of(List.of(1, 2, 3, 4, 5, 6), 10);
        Lotto lotto = Lotto.from(List.of(1, 2, 3, 20, 30, 40));

        int matchCount = winningLotto.calculateMatchCount(lotto);

        assertThat(matchCount).isEqualTo(3);
    }

    @DisplayName("로또가 보너스 번호를 맞췄음을 판단한다.")
    @Test
    void isMatchedBonusNumber_returnTrue_lottoContainsBonusNumber() {
        int bonusNumber = 10;
        WinningLotto winningLotto = WinningLotto.of(List.of(1, 2, 3, 4, 5, 6), bonusNumber);
        Lotto lotto = Lotto.from(List.of(1, 2, 3, 4, 5, bonusNumber));

        boolean matchedBonusNumber = winningLotto.isMatchedBonusNumber(lotto);

        assertThat(matchedBonusNumber).isTrue();
    }

    @DisplayName("로또가 보너스 번호를 맞추지 못했음을 판단한다.")
    @Test
    void isMatchedBonusNumber_returnFalse_lottoNotContainsBonusNumber() {
        WinningLotto winningLotto = WinningLotto.of(List.of(1, 2, 3, 4, 5, 6), 10);
        Lotto lotto = Lotto.from(List.of(1, 2, 3, 4, 5, 6));

        boolean matchedBonusNumber = winningLotto.isMatchedBonusNumber(lotto);

        assertThat(matchedBonusNumber).isFalse();
    }
}
