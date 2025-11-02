package lotto.view;

import java.util.List;
import lotto.domain.lotto.Lotto;
import lotto.domain.lotto.LottoNumber;

/**
 * 이 클래스는 프로그램의 출력을 담당한다.
 *
 * <p>이 클래스는 인스턴스 생성과 상속이 불가능한 정적 유틸 클래스이다.
 */

public final class OutputView {
    private static final String NEW_LINE = System.lineSeparator();
    private static final String PURCHASED_MESSAGE_FORMAT = "%d개를 구매했습니다.";

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
    }
}
