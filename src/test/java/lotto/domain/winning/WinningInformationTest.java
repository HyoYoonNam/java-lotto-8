package lotto.domain.winning;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class WinningInformationTest {
    @DisplayName("로또와 당첨 로또에 적합한 당첨 정보를 찾는다.")
    @ParameterizedTest(name = "[{index}] {0}, {1} -> {2}")
    @MethodSource("provideAllWinningCases")
    void findByMatchCountAndBonusMatched_returnCorrectWinningInformation(int matchCount, boolean bonusMatched,
                                                                         WinningInformation expectedOutput) {
        WinningInformation foundWinningInfo =
                WinningInformation.findByMatchCountAndBonusMatched(matchCount, bonusMatched);

        assertThat(foundWinningInfo).isSameAs(expectedOutput);
    }

    @DisplayName("당첨 번호 적중 개수가 범위를 벗어나면 에러를 발생한다.")
    @ParameterizedTest(name = "[{index}] {0} -> 예외 발생")
    @ValueSource(ints = {-1, 7})
    void findByMatchCountAndBonusMatched_throwsException_outOfRangedMatchCount(int matchCount) {
        assertThatThrownBy(() -> WinningInformation.findByMatchCountAndBonusMatched(matchCount, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> provideAllWinningCases() {
        return Stream.of(
                Arguments.of(3, false, WinningInformation.THREE_MATCHED),
                Arguments.of(3, true, WinningInformation.THREE_MATCHED),
                Arguments.of(4, false, WinningInformation.FOUR_MATCHED),
                Arguments.of(4, true, WinningInformation.FOUR_MATCHED),
                Arguments.of(5, false, WinningInformation.FIVE_MATCHED),
                Arguments.of(5, true, WinningInformation.FIVE_MATCHED_WITH_BONUS),
                Arguments.of(6, false, WinningInformation.SIX_MATCHED),
                Arguments.of(6, true, WinningInformation.SIX_MATCHED),
                Arguments.of(1, true, WinningInformation.EMPTY),
                Arguments.of(2, false, WinningInformation.EMPTY),
                Arguments.of(1, true, WinningInformation.EMPTY),
                Arguments.of(2, false, WinningInformation.EMPTY),
                Arguments.of(2, true, WinningInformation.EMPTY)
        );
    }
}
