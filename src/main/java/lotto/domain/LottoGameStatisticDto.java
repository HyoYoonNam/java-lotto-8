package lotto.domain;

import java.util.Map;

/**
 * 이 {@code record}는 로또 게임과 관련된 여러 통계량을 가지는 DTO(Data Transfer Object)이다.
 *
 * <p> 이 DTO는 {@code record}로 선언되었으므로 불변임을 보장한다.
 */

public record LottoGameStatisticDto(Map<WinningInformation, Integer> winningMap, double rateOfReturn) {
}
