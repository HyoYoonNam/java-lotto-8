package lotto.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import lotto.constant.exception.ErrorMessage;
import lotto.domain.lotto.Lotto;
import lotto.domain.lotto.LottoVendingMachine;
import lotto.domain.winning.WinningLotto;

/**
 * 이 클래스는 사용자로부터 입력을 받는 것과, 입력을 받기 위한 프롬프트 출력을 담당한다.
 *
 * <p>이 클래스는 인스턴스 생성과 상속이 불가능한 정적 유틸 클래스이다.
 *
 * <p>이 클래스에 속하는 메서드들에서 언급하는 '유효'하다는 것은 도메인 규칙을 위반하지 않음을 의미한다.
 */

public final class InputView {
    private static final String READ_PURCHASE_AMOUNT_PROMPT = "구입 금액을 입력해주세요.";
    private static final String READ_WINNING_NUMBERS_PROMPT = "당첨 번호를 입력해 주세요.";
    private static final String INPUT_DELIMITER = ",";
    private static final String INPUT_IS_NOT_INTEGER = ErrorMessage.MESSAGE_PREFIX + "입력된 값이 정수가 아닙니다: %s";
    private static final String READ_BONUS_NUMBER_PROMPT = "보너스 번호를 입력해 주세요.";

    /**
     * 로또를 구입할 금액을 입력받아 리턴한다.
     *
     * <p>이 메서드는 유효한 구매 금액이 입력될 때까지 반복적으로 프롬프트를 출력하며 입력을 요구한다.
     * 즉, {@code @return}이 유효한 금액임을 보장한다.
     */
    public static int readValidPurchaseAmount() {
        return getValidUserInput(() -> {
            int purchaseAmount = InputView.readLineAsInt();
            LottoVendingMachine.validatePurchaseAmount(purchaseAmount);
            return purchaseAmount;
        }, READ_PURCHASE_AMOUNT_PROMPT);
    }

    /**
     * 콤마(,)로 구분된 당첨 번호를 입력받아 그 당첨 번호를 가지는 로또를 리턴한다.
     *
     * <p>이 메서드는 유효한 당첨 번호가 입력될 때까지 반복적으로 프롬프트를 출력하며 입력을 요구한다.
     * 즉, {@code @return}이 유효한 당첨 번호를 가지는 로또임을 보장한다.
     *
     * @see Lotto
     * @see lotto.domain.lotto.LottoNumber
     */
    public static Lotto readValidWinningNumbers() {
        return getValidUserInput(InputView::readWinningNumbers, READ_WINNING_NUMBERS_PROMPT);
    }

    private static Lotto readWinningNumbers() {
        String[] split = readLine().split(INPUT_DELIMITER);
        List<Integer> winningNumbers = Stream.of(split)
                .map(String::strip)
                .map(InputView::parseToInteger)
                .toList();
        return Lotto.from(winningNumbers);
    }

    /**
     * 당첨 번호에 대해서 유효한 보너스 번호를 입력 받아 당첨 로또를 리턴한다.
     *
     * <p>이 메서드는 유효한 보너스 번호가 입력될 때까지 반복적으로 프롬프트를 출력하며 입력을 요구한다.
     * 즉, {@code @return}이 유효한 당첨 번호와 보너스 번호를 가지는 당첨 로또임을 보장한다.
     *
     * @param winningNumbers 당첨 번호를 가지는 로또
     */
    public static WinningLotto readValidBonusNumberForWinningLotto(Lotto winningNumbers) {
        return getValidUserInput(() -> {
            int bonusNumber = readLineAsInt();
            return WinningLotto.of(winningNumbers, bonusNumber);
        }, READ_BONUS_NUMBER_PROMPT);
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
