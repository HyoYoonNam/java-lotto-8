package lotto.constant;

/**
 * 이 클래스는 Lotto와 관련된 도메인 규칙을 관리하는 상수 전용 클래스이므로 인스턴스를 생성할 수 없다.
 *
 * <p>이 클래스와, 클래스에 속하는 모든 상수는 불변이므로 값의 종류를 추가하거나 값 자체를 변경할 수 없다.
 */

public final class LottoConstant {
    /** 로또 번호가 가질 수 있는 정수값 중 범위 중 최소값 */
    public static final int LOTTO_NUMBER_LOWER_RANGE_INCLUSIVE = 1;
    /** 로또 번호가 가질 수 있는 정수값 중 범위 중 최대값 */
    public static final int LOTTO_NUMBER_UPPER_RANGE_INCLUSIVE = 45;
    /** 로또 한 장에 있어야 하는 로또 번호의 개수 */
    public static final int LOTTO_NUMBER_SIZE = 6;
    /** 로또 한 장의 판매 가격 */
    public static final int LOTTO_PRICE = 1000;

    private LottoConstant() {
    }
}