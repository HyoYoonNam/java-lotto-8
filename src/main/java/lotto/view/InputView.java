package lotto.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import lotto.constant.exception.ErrorMessage;
import lotto.domain.winning.WinningLotto;

/**
 * 이 클래스는 사용자로부터 입력을 받는 것과, 입력을 받기 위한 프롬프트 출력을 담당한다.
 *
 * <p>이 클래스는 인스턴스 생성과 상속이 불가능한 정적 유틸 클래스이다.
 */

public final class InputView {
    private static final String READ_PURCHASE_AMOUNT_PROMPT = "구입 금액을 입력해주세요.";
    private static final String READ_WINNING_NUMBERS_PROMPT = "당첨 번호를 입력해 주세요.";
    private static final String INPUT_DELIMITER = ",";
    private static final String INPUT_IS_NOT_INTEGER = ErrorMessage.MESSAGE_PREFIX + "입력된 값이 숫자가 아닙니다: %s";
    private static final String READ_BONUS_NUMBER_PROMPT = "보너스 번호를 입력해 주세요.";

    /**
     * 로또를 구입할 금액을 입력받아 리턴한다.
     *
     * <p>이 메서드는 유효한 금액(양의 정수, 1000원 단위)을 리턴할 수 있을 때까지 반복적으로 프롬프트를 출력하며 입력을 요구한다.
     * 즉, {@code @return}이 유효한 금액임을 보장한다.
     */
    public static int readValidPurchaseAmount() {
        return getValidUserInput(InputView::readLineAsInt, READ_PURCHASE_AMOUNT_PROMPT);
    }

    public static WinningLotto readValidWinningLotto() {
        return getValidUserInput(() -> {
            List<Integer> winningNumbers = getValidWinningNumbers();
            int bonusNumber = getValidBonusNumber();
            return WinningLotto.of(winningNumbers, bonusNumber);
        }, "");
    }

    /**
     * 콤마(,)로 구분된 당첨 번호 목록을 입력받고, 콤마(,) 기준으로 분리하여 리턴한다.
     *
     * @return 당첨 번호들
     * @throws IllegalArgumentException 입력에 콤마(,)를 제외하고, 숫자가 아닌 요소가 있다면 발생한다.
     */
    public static List<Integer> getValidWinningNumbers() {
        return getValidUserInput(() -> {
            String[] split = readLine().split(INPUT_DELIMITER);
            return Stream.of(split)
                    .map(String::strip)
                    .map(InputView::parseToInteger)
                    .toList();
        }, READ_WINNING_NUMBERS_PROMPT);
    }

    /**
     * 입력받은 보너스 번호를 리턴한다.
     *
     * @throws IllegalArgumentException 입력이 숫자가 아니라면 발생한다.
     */
    public static int getValidBonusNumber() {
        return getValidUserInput(InputView::readLineAsInt, READ_BONUS_NUMBER_PROMPT);
    }

    private static <T> T getValidUserInput(Supplier<T> readUserInputSupplier, String prompt) {
        T userInput = null;
        boolean validUserInput = false;

        while (!validUserInput) {
            System.out.println(prompt);
            try {
                userInput = readUserInputSupplier.get();
                validUserInput = true;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        return userInput;
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
