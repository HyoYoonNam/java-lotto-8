# STUDY_NOTE
미션을 진행하면서 설계나 구현 등을 위해 학습했으나, README에 적기에는 과하다고 생각되는 내용들을 작성하는 문서입니다.

학습에 대한 레퍼런스는 각 주제별로 제목 바로 밑에 작성합니다.

## 1. IntStream의 range와 rangeClosed
1부터 45까지의 로또 번호를 미리 만들어 두고 캐싱하는 전략을 선택했는데, 이 과정에서 캐싱을 위한 `new HashMap<Integer, LottoNumber>` 인스턴스를 생성하는 시점에
1~45의 번호를 미리 넣어 두기 위해 stream 연산을 해야 된다(당연하게도 생성자에 시점에 `for ...` 연산을 전달할 수 없기 때문이다 대신 stream은 넣을 수 있다).

이때 `IntStream`의 존재 자체는 알고 있는 상태여서 이를 활용하고자 API를 확인해 봤는데 내가 쓸 만한 메서드는 `range`와 `rangeClosed`로 좁혀졌다.

그런데 메서드명만 보고는 둘의 차이가 뭐고, 어떤 메서드를 선택해야 할 지 모르겠어서 문서와 함께 내부 구현을 살펴봤다.

문서가 너무 잘 적혀 있어서 내 설명을 듣는 것보다는 직접 확인해보는 것을 추천한다.
그래도 결론을 말하자면, `range`는 끝 값을 포함하지 않고(exclusive), `rangeClosed`는 끝 값을 포함(inclusive)한다.

즉, 내 경우에는 `rangeClosed(1, 45)`를 호출하면 되겠다.

```java
// 수학 표현에서 '('와 ')'를 inclusive, '['와 ']'를 exclusive로 사용하기도 한다.
// 정수만 고려한다고 했을 때 (1, 5)는 2, 3, 4를, (1, 5]는 2, 3, 4, 5를 의미한다.

// `range`는 endExclusive를 가진다. 다음 for 문과 순서가 동등하다: for (int i = startInclusive; i < endExclusive ; i++) { ... }
public static IntStream range(int startInclusive, int endExclusive) {
    if (startInclusive >= endExclusive) {
        return empty();
    } else {
        return StreamSupport.intStream(
                new Streams.RangeIntSpliterator(startInclusive, endExclusive, false), false);
    }
}

// `rangeClosed`는 endInclusive를 가진다. 다음 for 문과 순서가 동등하다: for (int i = startInclusive; i <= endInclusive ; i++) { ... }
public static IntStream rangeClosed(int startInclusive, int endInclusive) {
    if (startInclusive > endInclusive) {
        return empty();
    } else {
        return StreamSupport.intStream(
                new Streams.RangeIntSpliterator(startInclusive, endInclusive, true), false);
    }
}
```

## 2. 인스턴스를 캐싱해서 사용하는 객체의 equals 재정의
reference: [이펙티브 자바 - 조슈아 블로크](https://product.kyobobook.co.kr/detail/S000001033066): 아이템10~11

이펙티브 자바의 목차를 읽으면서 미션에 적용할 만한 주제에 대해 학습한 뒤 사용하고 있다.

그 중 `equals`와 `hashCode`를 재정의 하라는 주제가 있는데, 내가 구현한 `LottoNumber` 클래스는 내부적으로 인스턴스를 캐싱 후 사용하기 때문에,
기본적으로 `==`을 사용한 identity 비교를 하더라도 로또 번호가 같기만 하면 같은 객체로 판단할 수 있다.

그래서 "`equals`를 재정의 하지 않아도 의도대로 동작하는데, 굳이 재정의 할 필요가 있을까?"하는 의문이 들었다.

결론은 "**그래도 재정의 해야 한다!**"로 내렸다. 근거는 다음과 같다.

내가 요구 사항으로 정의한 "로또 번호가 같으면 동일한 객체로 판단한다."에 대한 구현은 `equals` 재정의 또는 (기존처럼) 인스턴스 캐싱 등으로 할 수 있는데,
결국 '구현에 의존'하여 특정 구현(인스턴스 캐싱)에서만 해당 요구 사항이 만족되는 상황이다.

따라서 구현에 의존하지 않음과 이펙티브 자바에서 소개하는, 자바를 잘 쓰는 방법(정확히 뭐라고 표현해야 할 지 모르겠다)을 고려하여 `equals`를 재정의 해 두는 것이 적절하겠다.

기존에 알고 있던 `equals` 표준 정의 방식은 다음과 같다.
```java
if (anObject == null || getClass() != anObject.getClass()) {
    return false;
}

LottoNumber aLottoNumber = (LottoNumber) anObject;

return number == aLottoNumber.number;
```

그런데 이펙티브 자바와 자바의 `String.equals` 구현을 보니 개선할 여지가 보여서 다음과 같이 개선한다.
```java
// 주의: instanceof를 사용한 pattern matching은 Java16+가 요구된다.
@Override
public boolean equals(Object anObject) {
    if (this == anObject) {
        return true;
    }

    return (anObject instanceof LottoNumber aLottoNumber)
            && (number == aLottoNumber.number);
}
```

## 3. Javadoc
출처: 
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html#s7-javadoc)
- https://www.baeldung.com/javadoc
- 이펙티브 자바: 아이템 56, 아이템 74

내용이 짧고, 간단하고, 명확해서 자세히 정리할 필요는 없어 보인다. 미션을 진행하면서 필요한 내용을 학습하고 즉시 적용하기로 한다.

### Google Java Style Guide
- 첫 paragraph를 제외하고는 `<p>`태그로 시작한다. 태그와 첫 글자 사이에는 공백을 두지 않는다.
- Block tags는 `@param`, `@return`, `@throws`, `@deprecated` 순서로 쓴다.
  - 반드시 설명과 함께 해야 한다. 블록 태그만 단독으로 사용하지 않는다.
  - 설명은 블록 태그에서 공백(스페이스)을 한 칸 주고 같은 줄에 적는 것으로 시작한다. 만약 줄이 부족하면 다음 줄로 이동하고, 4spaces 이상 띄워서 이어 작성한다.
  - 블록 태그 즉, `@`이 등장하는 라인 기준으로 그 위 라인은 한 줄 띄운다(비운다).
- `@Override` 메서드를 구현하는 경우에는 그 상위 메서드에서 이미 javadoc이 구현되었다면, 굳이 재정의 메서드에서는 작성하지 않아도 된다.
  - `toString`, `equals` 등

### 이펙티브 자바
- 상속용으로 설계된 클래스의 메서드가 아니라면 (그 메서드가 어떻게 동작하는지가 아니라) 무엇을 하는지를 기술해야 한다. 즉, how가 아닌 what을 기술해야 한다.
- (코딩 표준에 따라서 다름) @return 태그의 설명이 메서드 설명과 같을 때 @return 태그를 생략해도 좋다.
  - 즉, 메서드 설명에서 리턴을 포함한 전반적인 what 설명을 하는 것이 우선적
- 다음 상황에서 상한/하한 값은 `private`이니 javadoc에 추가하지 않아야 한다. 대신 리터럴로 작성한다.  
  리터럴 값이 바뀐다는 것은 API 스펙 자체의 변화를 의미하기에 당연히 Javadoc도 바뀌어야 함을 의미하기 때문에 리터럴로 적어도 좋다.
  ```java
  public final class LottoNumber {
    private static final int LOWER_RANGE_INCLUSIVE = 1;
    private static final int UPPER_RANGE_INCLUSIVE = 45;
  
      /**
     * 인자로 받은 number에 대응되는 LottoNumber 객체를 리턴한다.
     *
     * @param number 로또 번호; 1 이상이고 45 이하여야 한다.
     * @throws IllegalArgumentException number가 범위를 벗어나면,
     *         즉, ({@code number < 1 || number > 45})이면 발생한다.
     */
    public static LottoNumber valueOf(int number) {
        if (UPPER_RANGE_INCLUSIVE < number || number < LOWER_RANGE_INCLUSIVE) {
            throw new IllegalArgumentException(ErrorMessage.LOTTO_NUMBER_OUT_OF_RANGE.build(number));
        }

        return CACHE.get(number);
    }
  ```
- 문서화 주석의 첫 문장의 첫 번째 마침표(`.`)가 나오는 부분에서 끊긴다. 이를 방지하려면 `{@literal}`로 감싸준다.
  - 예를 들어 "머스터드 대령이나 Mrs. 피콕 같은 용의자." 대신 "머스터드 대령이나 {@literal Mrs.} 피콕 같은 용의자."로 작성함이 적절하다.
  - (Java 10+) {@summary 머스터드 대령이나 Mrs. 피콕 같은 용의자.}처럼 요약 설명 전용 태그를 사용할 수 있다.

### 자바 표준 라이브러리 참고
- 클래스, 인터페이스 레벨의 javadoc은 javadoc의 마지막 부분(`*/`)과 클래스, 인터페이스 헤더 사이에 한 줄을 비운다.
  - 메서드의 경우는 javadoc과 메서드 헤더 부분 사이를 붙인다. 즉, 빈 줄을 두지 않는다.

### 상수값 링크로 가져오기
원래 전체 패키지 경로 안 적고도 되는 것 같은데, 일단 나는 실패했다.

```java
{@value lotto.constant.LottoConstant#LOTTO_PRICE}
```

## 4. Unchecked Exception은 javadoc에 기술하면 절대 안 될까?
출처:
  - 이펙티브 자바: 아이템49, 아이템56, 아이템74
  - [How do you document unchecked exceptions?](https://stackoverflow.com/questions/3746884/how-do-you-document-unchecked-exceptions)
  - [Avoid @throws in javadoc](http://www.javapractices.com/topic/TopicAction.do?Id=171)

~~이펙티브 자바에서는 Unchecked Exception인 경우, 즉 메서드나 클래스 선언부에서 `throws`로 던지는 체크 예외가 아닌 경우는 javadoc에 `@throws`로 설명하지 말라고 한다.~~

~~만약 발생할 수 있는 Unchecked Exception에 대한 설명을 적고 싶다면 `@throws`가 아니라, `@param` 등에 적으라고 한다.~~

~~발생할 수 있는 런타임 예외는 코드를 잘 짜서 다 잡아서 처리할 수 있도록 하라는 의도같다.~~

~~그런데 경우에 따라 "그렇게 빡빡하게 굴지 말고, 중요하다고 생각된다면 Unchecked Exception라도 `@throws`에 적어라!"라고 주장하는 이들도 꽤 있다. 판단 기준을 정해두고 팀마다 유연하게 결정해도 되겠다.~~

~~또한 해당 이번 미션에서도 "사용자가 잘못된 값을 입력한 경우 `IllegalArgumentException`을 발생시키고, ... 에러 메시지 출력 후 그 부분부터 입력을 다시 받는다."라는 요구 사항이 있는데 '잡아서 처리하라'는 요구로 보인다.~~

~~결론: Unchecked Exception(예를 들어 `LottoNumber.valueOf`에서의 `IllegalArgumentException`)은 잡아서 잘 처리하고, javadoc의 `@throws`에는 적지 말자!~~

잠깐 정신이 나갔었나 보다...! 책 내용을 완전히 잘못 이해하고 있었다. 이제라도 깨달아서 다행이다.

(이펙티브 자바에서 제시하는 내용들이 꽤나 고급 내용이고, 내가 시도해보지 않았던 것들 투성이라 머리에 과부하가 왔나 보다.. ㅠㅠ. 그런데 이렇게 이전보다 훨씬 개선된 작업을 하고 있다는게 너무 재밌다.)

책에서 말한 진짜 내용은 "발생할 수 있는 (언체크 포함) 모든 예외는 `@throws`로 기술하되, 메서드 선언부에서의 `throws ...`에는 넣지 말라"라는 것이었다!!!

'언체크 예외를 메서드 선언부 `throws`에 사용하는 방식'에 대해서는 [내가 작성했던 글: 언체크 예외에 대한 명시적 throws 선언시 예외 타입에 의한 컴파일 에러 발생](https://hyoyoonnam.github.io/posts/throw-runtime-exception-with-throws-Exception/)을 참고해도 좋다.

## 5. 개행을 위한 "\n"과 System.lineSeparator()
출처:
- [System.out.println()을 테스트 하는 방법](https://www.geeksforgeeks.org/advance-java/unit-testing-of-system-out-println-with-junit/)
- [JAVA 줄바꿈 대하여: 출력 개행에 \n을 쓰면 안된다고??](https://engineerinsight.tistory.com/14)

2주 차 때 `System.out.println()`에 대한 테스트를 하는 방법을 찾던 중, `System.lineSeparator()`라는 녀석을 보았다.

당시에는 그냥 단순히 "`\n` 리터럴 대신 명확한 이름으로 쓰기 위함인가?" 정도로만 생각하고 넘어 갔는데, 이번 3주 차에서 추가로 서칭을 해보니 그 원리를 더 알고 사용하는 것이 적절하다고 생각해서 학습을 진행한다.

우리가 무심코 사용하던 `System.out.println()`가 내부적으로 어떻게 개행을 처리하는지 결국 내부 구현을 따라가다 보면 알 수 있는데, 그 흐름을 아래와 같이 하나의 사진으로 정리해봤다.

![System-out-println()의 개행 처리 흐름](https://github.com/user-attachments/assets/b96631e4-7227-4828-857a-16dd9d983b4e)

결론적으로, 자바 표준 출력도 `\n`이 아니라, `System.lineSeparator()`로 개행을 처리한다. 이에 대한 설명은 javadoc에 명확히 되어 있어서, 별 다른 번역없이 이를 첨부만 하겠다.

> Returns the _system-dependent_ line separator string.
> 
> It always returns the same value - the initial value of the system property line.separator.
> 
> On UNIX systems, it returns "\n"; on Microsoft Windows systems it returns "\r\n".
> 
> Returns:
> 
> the _system-dependent_ line separator string

따라서, `StringBuilder`에서 개행을 추가하기 위해서는 다음과 같이 해야 된다.
```java
StringBuilder result = new StringBuilder();
result.append(lottoAmount)
        .append("개를 구매했습니다.")
        .append(System.lineSeparator());
//        .append("\n"); // 이건 적절하지 못하다. OS에 따라 개행으로 처리되지 않을 위험이 있다.
```

## 6. 스트림 안에서 예외 처리
출처:
- [2주 차 PR 리뷰](https://github.com/woowacourse-precourse/java-calculator-8/pull/808/files/fc8a3f20f28310f9e0e91412bed5d1681e2061c2#r2460441509)
- [기본적인 스트림 예외 처리](https://stackoverflow.com/questions/19757300/java-8-lambda-streams-filter-by-method-with-exception)
- [스트림의 장점 중 하나인 'declarative'를 일지 않으면서 예외 처리](https://www.baeldung.com/java-streams-aggregate-exceptions#aggregating-exceptions-and-output-in-the-stream-pipeline-using-reflection)

## 7. Enum에 여러 정보 담기
출처: [Java Enum 활용기 - 우아한기술블로그](https://techblog.woowahan.com/2527/)

```text
3개 일치 (5,000원) - 1개
4개 일치 (50,000원) - 0개
5개 일치 (1,500,000원) - 0개
5개 일치, 보너스 볼 일치 (30,000,000원) - 0개
6개 일치 (2,000,000,000원) - 0개
```

위와 같이 당첨 내역을 출력할 때, `(3개 일치, 5000원)`, `(4개 일치, 50000원)`과 같이 서로 연관 있는 정보를 짝지어서 관리할 필요성을 느꼈다.

## 8. DTO와 Record (Java 16+)
출처: [자바 DTO vs Record, 무엇을 사용해야 할까?](https://yozm.wishket.com/magazine/detail/2814/)

`LottoGameStatisticDto`를 구현했는데, 인텔리제이가 `Convert to record class`라며 추천해 주길래 관련 내용을 찾고, 적용했다.

결론적으로, DTO가 별 다른 값 수정을 필요로 하지 않는다면 `record`로 선언하는 편이 자바 문법상으로 불변임을 보장하고, 불변을 위한 구현 코드들을 생략할 수 있어 좋다.
