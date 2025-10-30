package lotto.constant.exception;

/**
 * 프로젝트에서 사용하는 에러 메시지.
 *
 * <p>{@code build} 메서드로 메시지를 생성할 수 있다.
 *
 * <p>각 메시지에 대한 설명은 그 자체로 명확하므로 주석을 생략한다.
 */

public enum ErrorMessage {
    LOTTO_NUMBER_COUNT_INVALID("로또 번호는 6개여야 합니다: "),
    LOTTO_NUMBER_DUPLICATED("로또 번호에 중복이 존재합니다: "),
    LOTTO_NUMBER_OUT_OF_RANGE("로또 번호의 숫자 범위는 1~45까지입니다: "),
    PURCHASE_AMOUNT_IS_NOT_DIVISIBLE_BY_LOTTO_PRICE("구입 금액은 1,000원 단위여야 합니다: "),
    ;

    private static final String MESSAGE_PREFIX = "[ERROR] ";
    private final String message;

    ErrorMessage(String message) {
        this.message = MESSAGE_PREFIX + message;
    }

    /** 기본적인 예외 메시지를 생성한다. */
    public String build() {
        return this.message;
    }

    /**
     * 기본적인 예외 메시지에 추가로 그 원인이 되는 값인 {@code target}을 포함해서 생성한다.
     */
    public String build(Object target) {
        return this.message + target.toString();
    }
}
