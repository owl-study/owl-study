# 아이템1. 생성자 대신 정적 팩터리 메서드를 고려하라
## 정적 팩터리 메서드란?
정적 팩터리 메서드라는 말이 낯설게 느껴질 수도 있겠지만 사실은 꽤 친숙한 메서드일 것이다. **객체를 생성할 때, 생성자가 아니라 정적 Static 메서드를 사용하는 것**을 정적 팩토리 메서드라고 한다. 

> 여기서 팩터리는 객체를 생성하는 역할을 분리하겠다는 의미로 [GoF 디자인 패턴]() 중 [팩토리 패턴]()에서 이름만 따왔을 뿐 크게 연관성은 없다.

### 예시 코드
#### (1) LocalTime 클래스의 of 메소드(Static 메서드)
```java
public static LocalTime of(int hour, int minute) {
  ChronoField.HOUR_OF_DAY.checkValidValue((long)hour);
  if (minute == 0) {
    return HOURS[hour];
  } else {
    ChronoField.MINUTE_OF_HOUR.checkValidValue((long)minute);
    return new LocalTime(hour, minute, 0, 0);
  }
}

```
#### (2) `hour`, `minutes`을 인자로 받은 뒤 `LocalTime` 객체(9시 30분)를 반환
```java
LocalTime openTime = LocalTime.of(9, 30);
```

`LocalTime`의 `of` 메소드처럼 생성자를 통해 직접 객체를 생성하는 것이 아니라 메서드로 객체를 생성하는 것을 **_정적 팩터리 메서드_** 라고 한다.

 # 👼 Reference
 - https://tecoble.techcourse.co.kr/post/2020-05-26-static-factory-method/
 - https://7942yongdae.tistory.com/147
