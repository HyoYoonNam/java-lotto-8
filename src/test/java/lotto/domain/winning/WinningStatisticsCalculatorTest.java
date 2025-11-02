package lotto.domain.winning;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lotto.domain.lotto.Lotto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class WinningStatisticsCalculatorTest {
    @DisplayName("로또의 당첨 통계량들을 계산한다.")
    @ParameterizedTest(name = "[{index}] 성공")
    @MethodSource("provideCalculateArguments")
    void calculate_returnWinningStatisticsAndRateOfReturn(
            WinningLotto winningLotto,
            List<Lotto> lottos,
            Map<WinningInformation, Integer> expectedStats,
            double expectedRateOfReturn) {

        WinningStatisticDto statistic = WinningStatisticsCalculator.calculate(winningLotto, lottos);

        assertThat(statistic.winningMap()).isEqualTo(expectedStats);
        assertThat(statistic.rateOfReturn()).isEqualTo(expectedRateOfReturn);
    }

    private static Stream<Arguments> provideCalculateArguments() {
        // given arguments
        int bonusNumber = 10;
        WinningLotto winningLotto = WinningLotto.of(List.of(1, 2, 3, 4, 5, 6), bonusNumber);

        List<Lotto> lottos = List.of(
                // 1등(1개)
                Lotto.from(List.of(1, 2, 3, 4, 5, 6)),                  // 6개
                // 2등(1개)
                Lotto.from(List.of(1, 2, 3, 4, 5, bonusNumber)),        // 5개 + 보너스
                // 3등(1개)
                Lotto.from(List.of(1, 2, 3, 4, 5, 45)),                 // 5개, 3등
                // 4등(2개)
                Lotto.from(List.of(1, 2, 3, 4, bonusNumber, 45)),       // 4개 + 보너스
                Lotto.from(List.of(1, 2, 3, 4, 45, 44)),                // 4개
                // 5등(2개)
                Lotto.from(List.of(1, 2, 3, bonusNumber, 45, 44)),      // 3개 + 보너스
                Lotto.from(List.of(1, 2, 3, 45, 44, 43)),               // 3개
                // 꽝(6개)
                Lotto.from(List.of(1, 2, bonusNumber, 45, 44, 43)),     // 2개 + 보너스
                Lotto.from(List.of(1, 2, 45, 44, 43, 42)),              // 2개
                Lotto.from(List.of(1, bonusNumber, 45, 44, 43, 42)),    // 1개 + 보너스
                Lotto.from(List.of(1, 45, 44, 43, 42, 41)),             // 1개
                Lotto.from(List.of(bonusNumber, 44, 43, 42, 41, 40)),   // 0개 + 보너스
                Lotto.from(List.of(45, 44, 43, 42, 41, 40))             // 0개
        );

        // expected output arguments
        Map<WinningInformation, Integer> expectedMap = new EnumMap<>(WinningInformation.class);
        expectedMap.put(WinningInformation.SIX_MATCHED, 1);
        expectedMap.put(WinningInformation.FIVE_MATCHED_WITH_BONUS, 1);
        expectedMap.put(WinningInformation.FIVE_MATCHED, 1);
        expectedMap.put(WinningInformation.FOUR_MATCHED, 2);
        expectedMap.put(WinningInformation.THREE_MATCHED, 2);
        expectedMap.put(WinningInformation.EMPTY, 6);

        double expectedPrize = 2_031_610_000.0;
        double totalPurchase = 13_000.0;
        double expectedRate = (expectedPrize / totalPurchase) * 100.0;

        return Stream.of(
                Arguments.of(winningLotto, lottos, expectedMap, expectedRate)
        );
    }
}