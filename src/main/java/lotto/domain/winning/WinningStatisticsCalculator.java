package lotto.domain.winning;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lotto.constant.LottoConstant;
import lotto.domain.lotto.Lotto;

/**
 * 이 클래스는 당첨 로또와, 구매한 로또 목록을 이용하여 여러 통계랑을 계산한다.
 *
 * <p>이 클래스는 불변이므로 상속할 수 없다. 또한 인스턴스를 생성할 수 없는 정적 유틸 클래스이다.
 */

public final class WinningStatisticsCalculator {
    private WinningStatisticsCalculator() {
    }

    /** 당첨 로또와 구매한 로또 목록을 받아 여러 통계량 계산하여 리턴한다. */
    public static WinningStatisticDto calculate(final WinningLotto winningLotto,
                                                final List<Lotto> lottos) {
        Map<WinningInformation, Integer> winningMap = calculateWinningInformation(winningLotto, lottos);
        double rateOfReturn = calculateRateOfReturn(winningMap, lottos.size() * LottoConstant.LOTTO_PRICE);

        return new WinningStatisticDto(winningMap, rateOfReturn);
    }

    private static Map<WinningInformation, Integer> calculateWinningInformation(WinningLotto winningLotto,
                                                                                List<Lotto> lottos) {
        Map<WinningInformation, Integer> winningMap = new EnumMap<>(WinningInformation.class);
        for (WinningInformation winningInfo : WinningInformation.values()) {
            winningMap.put(winningInfo, 0);
        }

        for (Lotto lotto : lottos) {
            int matchCount = winningLotto.calculateMatchCount(lotto);
            boolean bonusMatched = winningLotto.isMatchedBonusNumber(lotto);
            WinningInformation winningInfo =
                    WinningInformation.findByMatchCountAndBonusMatched(matchCount, bonusMatched);
            winningMap.put(winningInfo, winningMap.get(winningInfo) + 1);
        }

        return winningMap;
    }

    private static double calculateRateOfReturn(Map<WinningInformation, Integer> statistics, int totalPurchaseMoney) {
        int totalPrizeMoney = 0;
        for (Entry<WinningInformation, Integer> winningInformationIntegerEntry : statistics.entrySet()) {
            int lottoAmount = winningInformationIntegerEntry.getValue();
            if (lottoAmount == 0) {
                continue;
            }

            totalPrizeMoney += winningInformationIntegerEntry.getKey().getPrizeMoney() * lottoAmount;
        }

        return ((double) totalPrizeMoney / totalPurchaseMoney) * 100;
    }
}
