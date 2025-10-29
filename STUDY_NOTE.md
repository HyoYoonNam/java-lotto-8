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