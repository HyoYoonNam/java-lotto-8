package lotto.exception;

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

    public String build() {
        return this.message;
    }

    public String build(Object target) {
        return this.message + target.toString();
    }
}
