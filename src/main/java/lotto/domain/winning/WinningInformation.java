package lotto.domain.winning;

import java.util.Arrays;
import lotto.constant.exception.ErrorMessage;

/**
 * 이 클래스는 로또 번호가 당첨 번호, 보너스 번호를 맞춘 개수에 따라 가지는 당첨 금액 정보를 나타낸다.
 *
 * <p>각 Enum value는 {@code int} 당첨 번호(6개) 중 맞춘 개수, {@code boolean} 보너스 번호 적중 여부, {@code int} 당첨 금액을 가진다.
 */

public enum WinningInformation {
    THREE_MATCHED(3, false, 5_000),
    FOUR_MATCHED(4, false, 50_000),
    FIVE_MATCHED(5, false, 1_500_000),
    FIVE_MATCHED_WITH_BONUS(5, true, 30_000_000),
    SIX_MATCHED(6, false, 2_000_000_000),
    EMPTY(0, false, 0),
    ;

    private static final int MIN_MATCH_COUNT = 0;
    private static final int MAX_MATCH_COUNT = 6;

    private final int matchCount;
    private final boolean bonusMatched;
    private final int prizeMoney;

    WinningInformation(int matchCount, boolean bonusMatched, int prizeMoney) {
        this.matchCount = matchCount;
        this.bonusMatched = bonusMatched;
        this.prizeMoney = prizeMoney;
    }

    /**
     * 로또가 맞춘 당첨 번호의 수와 보너스 번호를 맞췄는지 여부를 기준으로 당첨 정보 {@code WinningInformation}를 리턴한다.
     *
     * @param matchCount 로또가 맞춘 당첨 번호의 수; 최소 0개, 최대 6개까지 가능하다.
     * @param bonusMatched 로또가 당첨 로또의 보너스 번호를 맞췄는지 여부
     * @return {@code @param}에 부합하는 정보를 가진 {@code WinningInformation} Enum value;
     *         부합하는 value가 없다면, 기본값으로 {@link #EMPTY}를 리턴한다.
     * @throws IllegalArgumentException {@code @param matchCount}의 전제를 위반하면 발생한다.
     */
    public static WinningInformation findByMatchCountAndBonusMatched(int matchCount,
                                                                     boolean bonusMatched) {
        validateMatchCount(matchCount);

        if (matchCount == 5 && bonusMatched) {
            return FIVE_MATCHED_WITH_BONUS;
        }

        return Arrays.stream(WinningInformation.values())
                .filter(winningInfo -> winningInfo.matchCount == matchCount)
                .findFirst()
                .orElse(EMPTY);
    }

    private static void validateMatchCount(int matchCount) {
        if (matchCount < MIN_MATCH_COUNT || matchCount > MAX_MATCH_COUNT) {
            throw new IllegalArgumentException(ErrorMessage.OUT_OF_RANGE_MATCH_COUNT.build(matchCount));
        }
    }

    public int getMatchCount() {
        return matchCount;
    }

    public boolean isBonusMatched() {
        return bonusMatched;
    }

    public int getPrizeMoney() {
        return prizeMoney;
    }
}
