package lotto.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.List;
import java.util.stream.Stream;

/**
 * 이 클래스는 사용자로부터 입력을 받는 것과, 입력을 받기 위한 프롬프트 출력을 담당한다.
 *
 * <p>이 클래스는 인스턴스 생성과 상속이 불가능한 정적 유틸 클래스이다.
 */

public final class InputView {
    private static final String READ_WINNING_NUMBERS_PROMPT = "당첨 번호를 입력해 주세요.";
    private static final String INPUT_DELIMITER = ",";
    private static final String INPUT_IS_NOT_INTEGER = "입력된 값이 숫자가 아닙니다: %s";
    private static final String READ_BONUS_NUMBER_PROMPT = "보너스 번호를 입력해 주세요.";

    /**
     * 콤마(,)로 구분된 당첨 번호 목록을 입력받고, 콤마(,) 기준으로 분리하여 리턴한다.
     *
     * @return 당첨 번호들
     * @throws IllegalArgumentException 입력에 콤마(,)를 제외하고, 숫자가 아닌 요소가 있다면 발생한다.
     */
    public static List<Integer> readWinningNumbers() {
        System.out.println(READ_WINNING_NUMBERS_PROMPT);
        String[] split = readLine().split(INPUT_DELIMITER);

        return Stream.of(split)
                .map(InputView::parseToInteger)
                .toList();
    }

    /**
     * 입력받은 보너스 번호를 리턴한다.
     *
     * @throws IllegalArgumentException 입력이 숫자가 아니라면 발생한다.
     */
    public static int readBonusNumber() {
        System.out.println(READ_BONUS_NUMBER_PROMPT);
        return readLineAsInt();
    }

    private static String readLine() {
        return Console.readLine();
    }

    private static Integer parseToInteger(String s) {
        try {
            return Integer.valueOf(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format(INPUT_IS_NOT_INTEGER, s), e);
        }
    }

    private static int readLineAsInt() {
        String line = Console.readLine();
        try {
            return parseToInteger(line);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format(INPUT_IS_NOT_INTEGER, line), e);
        }
    }
}
