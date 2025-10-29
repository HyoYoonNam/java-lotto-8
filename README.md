# java-lotto-precourse

## 구현할 기능 목록
### 로또 번호 (LottoNumber)
- [x] 1~45(정수, 경계값 포함)중 하나에 속하는 로또 번호를 생성한다.
- [x] 범위(1~45)를 벗어나는 로또 번호는 생성할 수 없다.
- [x] 로또 번호가 같으면 동일한 객체로 판단한다.

## 설계
### 로또 번호에 중복된 숫자가 있는지 검증
#### 방법1. Set을 이용한 가장 간단한 구현
주어진 `LottoTest` 중 실패하는 테스트를 통과시키기 위해 그 구현을 고민했을 때,
직관적으로 떠오른 방식은 중복을 허용하지 않는 `Set` 타입으로 변환했을 때의 사이즈와 비교하는 것이다(아래 코드 참고).
```java
// 방법1
Set<Integer> set = Set.copyOf(numbers);
if (set.size() != numbers.size()) {
    throw new IllegalArgumentException("[ERROR] 로또 번호에 중복이 존재합니다.");
}
```

#### 방법2. Set을 사용하지만 더 빠르게 예외 발생시키기
물론 위 구현('방법1')은 중복 검증 테스트를 성공적으로 통과시킨다.

하지만 "스트림 연산을 통해서도 해결할 수 있지 않을까?"하는 의문이 들었고,
`how to find duplicated value using stream java`로 서칭했다.

그렇게 [처음 찾은 글](https://stackoverflow.com/questions/68656381/how-to-find-duplicate-elements-in-a-stream-in-java)에서는
스트림이 아니라 (내가 생각한 방법1과 유사하게) `Set`을 사용하는 방식을 제시했다.

하지만 그 효율성 측면에서 차이가 있는데, 일단 해당 코드를 보자.
```java
// 방법2
Set<Integer> set = new HashSet<>();
for (Integer number : numbers) {
    if (!set.add(number)) {
        throw new IllegalArgumentException("[ERROR] 로또 번호에 중복이 존재합니다.");
    }
}
```

차이를 알겠는가? 방법1의 경우에는 일단 주어진 `numbers` ***전체를 순회***한 뒤에야 `size`를 통해 중복을 판단한다.

하지만 방법2의 경우 각 요소를 `Set`에 `add`하면서 **_`false`가 리턴되는 순간 중복으로 판단_**한다.
즉, 얼리 리턴처럼 조건을 만족하자마자 빠르게 종료되기 때문에 중복이 존재하지 않거나 중복이 끝에만 존재하는 경우를 제외하고는 더 우수한 성능이 기대된다.
('기대된다'고 표현한 것은, `Set.copyOf` 메서드가 극한으로 최적화 되어 있어서 전체를 순회하지만, 단순한 순회 구현보다 훨씬 빠르게 동작하지 않을까? 하는 생각도 들기 때문이다)

#### 방법3. 스트림을 이용해서 중복 여부 뿐만 아니라, 구체적인 중복 값까지 파악하기
추가적으로 생긴 욕심은 '에러가 발생한 지점을 명확히 해주고 싶다!'는 것이었다.

예를 들어 2주 차에서는 예외가 발생했을 때의 입력값 전체(`numbers`)를 메시지에 포함해줬지만,
이번에는 그 중에서 실제로 예외의 원인이 되는 입력(중복된 숫자)만을 메시지에 포함해주고 싶다는 것이다.

이에 대한 해결은 [이 글](https://www.baeldung.com/java-list-find-duplicates#1-using-filter-and-setadd-method)을 참고했다.

예외의 원인이 되는 값 전체를 출력해주려면 결국 방법2처럼 빠른 예외를 발생시키지 못하고, 방법1처럼 전체를 순회해야 된다는 trade-off가 생기기는 한다.

하지만 해당 미션에서의 도메인 룰이 '로또 번호는 6개'여야 함을 포함하고 있고, 구현에서 검증하기 때문에 결국 전체 순회로 인한 비효율이 유의미한 수준이 되지는 않음이 보장된다.

전체 순회로 인해서 비효율이 생기려면 그 크기가 매우 커져야 하는데, 크기가 너무 큰 경우는 이미 로또 번호 개수에 대한 검증에서 예외 처리되기 때문이다.

따라서 다음과 같은 구현을 채택한다.
```java
// 방법3
Set<Integer> elements = new HashSet<>();
List<Integer> list = numbers.stream()
        .filter(n -> !elements.add(n))
        .toList();
```

#### 방법4. 함수형 프로그래밍은 side effect 지양한다.
다만, 방법3처럼 스트림이 외부 컨텍스트에 변화를 주거나(side effect) 의존하는 것은 함수형 프로그래밍의 지향점이 아님을 알고 있다.

내가 의도하는 구현에서는 `중복된 값들`만 있으면 되지, `중복 없애고 남은 값들`까지는 필요하지 않기 때문에 다음과 같이 스트림을 사용하지 않고 구현하는 편이 좋겠다.

(만약 `중복 없애고 남은 값들`도 필요한 경우라면 스트림의 `groupingBy`를 활용하면 side effect도 해소하고, 요구 사항 만족도 가능한 것으로 파악된다.)

```java
// 방법4
Set<Integer> set = new HashSet<>();
boolean duplicated = false;
for (Integer number : numbers) {
    if (!set.add(number)) {
        duplicated = true;
    }
}
if (duplicated) {
    throw new IllegalArgumentException("[ERROR] 로또 번호에 중복이 존재합니다.");
}
```

### 로또 번호를 LottoNumber Value Object로 설계
기존에는 생각하지 못했던 방법인데, [해당 PR](https://github.com/woowacourse-precourse/java-racingcar-8/pull/169/files#r2470659778)을
리뷰 중에 Value Object라는 개념에 대해 알게 되었고, 이번 미션에서 '로또 번호'에 대한 구현으로 적절하겠다고 판단했다.

주어진 기능 요구 사항 중 "로또 번호의 숫자 범위는 1~45까지이다."라는 요구 사항이 있다.

V.O.를 알지 못하던 때였다면 당연스레 `Lotto` 내부에서 범위 검증을 했겠으나, 이제는 다르다!

`Lotto`는 어떤 규칙(개수 제한, 중복 금지 등)을 기반으로 `로또 번호`들을 가지는 객체이다. 그 자체로 `로또 번호`가 아니다. 따라서 로또 번호 도메인에 대해서 V.O.로 만들어서 관리함이 적절하다.

#### 고민1: 로또 번호 45개를 미리 만들어 둘 필요가 있을까?
결론: **YES**

만약 사람 A와 B 모두 `2`라는 로또 번호를 가지고 있다고 하자. 이 둘을 다르다고 할 수 있을까?

내 답은 "아니다"이다. 로또 번호를 누가(A, B) 소유했는지는 로또 번호가 알 바가 아니다. 로또 번호는 단순히 로또 번호일 뿐이다.

당첨 번호 중 하나가 `2`일 때 A의 `2`는 당첨이고, B의 `2`는 당첨이 아니라면 논리적 결함이 있지 않은가?

로또 번호 `2`는 그 어떤 상황에서도 동일한 `2`로 취급받아야 한다. 즉, 클래스 레벨에서 공유되도록 구현함이 적절하다.

## 3주 차 목표
- [ ] 코드 컨벤션 중 class 선언부 다음에 빈 줄을 두지 말라는 컨벤션을 지킨다. (아래 `intellij-java-wooteco-style.xml` 중 일부 참고)
    ```xml
    <option name="BLANK_LINES_AFTER_CLASS_HEADER" value="0"/>
    ```
- [ ] 하나의 테스트 클래스에서 테스트 케이스가 다양하다면, `@Nested` 애노테이션으로 구분하기
- [ ] 스트림 내부에서의 예외 처리가 가능함을 인지하고, 필요한 경우 적용하기
- [ ] 커밋에 깃모지 사용하기
- [ ] 메서드를 접근 제어자가 아니라, 기능 별(논리적 단위)로 묶기
- [ ] 커밋 메시지 컨벤션을 지킨다.
  - 2주 차까지는 제목 50자, 본문 72자 제한이 '글자 수' 기준인 줄 알고, 인크루트 글자 수 검사기로 체크했다...!
  - 3주 차 진행 중에 갑자기 의문이 들어 찾아 보니 화면에 표시되는 칸 수 기준이었고, 기존의 내 커밋 제목들은 이를 한참 초과해버렸다.
  - vi 에디터를 사용중인데, [Configuring Git and Vim](https://csswizardry.com/2017/03/configuring-git-and-vim/)을 참고해서 50번 째와 72번 째 컬럼에 색을 넣어 표시해주는 설정을 해서 사용하기로 한다.

## References
[VO(Value Object)는 무엇일까? 왜 사용할까?](https://ksh-coding.tistory.com/83)
