package lotto.view;

import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import lotto.domain.lotto.Lotto;
import lotto.domain.lotto.LottoNumber;
import lotto.domain.winning.WinningInformation;
import lotto.domain.winning.WinningStatisticDto;

/**
 * 이 클래스는 프로그램의 출력을 담당한다.
 *
 * <p>이 클래스는 인스턴스 생성과 상속이 불가능한 정적 유틸 클래스이다.
 */

public final class OutputView {
    private static final String NEW_LINE = System.lineSeparator();
    private static final String PURCHASED_MESSAGE_FORMAT = "%d개를 구매했습니다.";
    private static final String PRINT_WINNING_STATISTIC_HEADER = "당첨 통계" + NEW_LINE + "---";
    private static final String PERCENT_LITERAL = "%%";
    private static final String RATE_OF_RETURN_FORMAT = "총 수익률은 %.1f" + PERCENT_LITERAL + "입니다.";

    private OutputView() {
    }

    /**
     * 구입한 로또들의 목록인 {@code lottos}를 받아 구입 개수와 각 로또가 가지는 로또 번호를 출력한다.
     *
     * @param lottos {@link LottoNumber} 6개를 가지는 {@link Lotto}들의 목록
     */
    public static void printPurchasedLottos(List<Lotto> lottos) {
        List<Lotto> lottosCopy = List.copyOf(lottos);
        StringBuilder textOutputBuilder = new StringBuilder();

        textOutputBuilder.append(String.format(PURCHASED_MESSAGE_FORMAT, lottosCopy.size()))
                .append(NEW_LINE);

        for (Lotto lotto : lottosCopy) {
            textOutputBuilder.append(lotto.toString())
                    .append(NEW_LINE);
        }

        System.out.print(textOutputBuilder.toString());
        System.out.print(NEW_LINE);
    }

    /**
     * 당첨 통계량들이 들어 있는 {@code winningStatisticDto}를 받아 당첨 통계 정보들을 출력한다.
     *
     * @param winningStatisticDto 구입한 로또 목록과 당첨 로또를 이용하여 계산된 통계량들이 들어 있는 당첨 통계 DTO
     */
    public static void printWinningStatistic(WinningStatisticDto winningStatisticDto) {
        System.out.println(PRINT_WINNING_STATISTIC_HEADER);

        System.out.print(getWinningInformationLines(winningStatisticDto.winningMap()));

        System.out.println(String.format(RATE_OF_RETURN_FORMAT, winningStatisticDto.rateOfReturn()));
    }

    private static String getWinningInformationLines(Map<WinningInformation, Integer> winningMap) {
        StringBuilder result = new StringBuilder();

        for (WinningInformation winningInfo : winningMap.keySet()) {
            if (winningInfo == WinningInformation.EMPTY) {
                continue;
            }

            result.append(winningInfo.getMatchCount())
                    .append("개 일치")
                    .append(getBonusMatchedInfo(winningInfo.isBonusMatched()))
                    .append("(").append(getFormattedNumberEachThousandUnit(winningInfo.getPrizeMoney())).append("원)")
                    .append(" - ").append(winningMap.get(winningInfo)).append("개")
                    .append(NEW_LINE);
        }

        return result.toString();
    }

    private static String getBonusMatchedInfo(boolean bonusMatched) {
        if (bonusMatched) {
            return ", 보너스 볼 일치 ";
        }
        return " ";
    }

    private static String getFormattedNumberEachThousandUnit(int number) {
        return NumberFormat.getNumberInstance().format(number);
    }
}
