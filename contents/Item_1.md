

# 아이템1. 생성자 대신 정적 팩터리 메서드를 고려하라
## ❓ 정적 팩터리 메서드란?
정적 팩터리 메서드라는 말이 낯설게 느껴질 수도 있겠지만 사실은 꽤 친숙한 메서드일 것이다. **객체를 생성할 때, 생성자가 아니라 정적 Static 메서드를 사용하는 것**을 정적 팩토리 메서드라고 한다. 

> 여기서 팩터리는 객체를 생성하는 역할을 분리하겠다는 의미로 [GoF 디자인 패턴]() 중 [팩토리 패턴]()에서 이름만 따왔을 뿐 크게 연관성은 없다.

### 🤓 예시 코드
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

## 🤔 왜 생성자 대신 정적 팩터리 메서드를 고려해야할까?
### 🖐️ 다섯가지 이유
#### 첫번째 : "이름을 가질 수 있다."

프로그래밍에서 다른 사람이 이해하기 쉽도록 코드를 짜는 것은 중요한 요소 중에 하나다. 
`new` 키워드를 사용해 객체를 만드는 생성자는 생성자의 결국 내부 구조를 잘 알고 있어야만 목적에 알맞게 객체를 생성할 수 있다. 
반면 정적 팩터리 메서드는 이름을 가질 수 있으므로 네이밍을 통해 어떤 값을 이용해 객체를 만드려고 하는지 등 읽는 이에게 코드의 설계 의도를 좀 더 쉽게 전달할 수 있다. 

#### 비교 1. 생성자

```java
Public class Application {
    public static void main(String[] args) {
        Car car = new Car("현대");
    }
}

class Car {
    private String brand;

    public Car(String brand) {
        this.brand = brand;
    }
}
```

> 위와 같이 생성자로 Car 객체 생성할 때, 생성자의 내부 구조를 모르고 있다면 new 키워드만으로는 "현대"가 어떤 정보인지 이해하는 것이 어렵다.

#### 비교 2. 정적 팩토리 메서드

```java
public class Application {
    public static void main(String[] args) {
        Car car = Car.brandOf("현대");
    }
}

class Car {
    private String brand;

    private Car(String brand) {
        this.brand = brand;
    }
    
    public static Car brandOf(String brand) {
        return new Car(brand);
    }
}
```

> 반면 정적 팩토리 메서드로 객체를 생성하는 경우 메서드의 이름을 통해 직관적으로 객체에 대한 정보를 단번에 이해할 수 있다. 이처럼 정적 팩토리 메서드는 객체 생성 시 목적에 알맞은 이름을 표현함으로써 코드의 가독성이 좋아지는 효과가 있다.

#### 두번째 : "호출할 때마다 인스턴스를 새로 생성할 필요가 없다."
#### 세번째 : "하위 자료형 객체를 반환할 수 있다."
#### 네번째 : "매개변수에 따라 다른 클래스의 객체를 반환할 수 있다."
#### 다섯번째 : "작성 시점에 반환할 객체의 클래스가 없어도 된다."

## 😓 장점이 있으면 단점도 있는 법.
### ✌️ 두가지 단점
#### 첫번째 : "하위 클래스를 만들 수 없다."
#### 두번째 : "프로그래머를 찾기가 어렵다."

## 🙆‍♀️ 정리!
> 정적 팩터리 메서드와 public 생성자는 각자의 쓰임새가 있으니 상대적인 장단점을 이해하고 사용하는 것이 좋다. 하지만 대부분의 경우 정적 팩터리를 사용하는 편이 유리하므로 무작정 public 생성자를 사용했다면 습관을 고치는 것이 좋다.

 # 👼 Reference
 - https://tecoble.techcourse.co.kr/post/2020-05-26-static-factory-method/
 - https://7942yongdae.tistory.com/147
