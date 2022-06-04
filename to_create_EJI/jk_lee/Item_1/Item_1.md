# Item 1. 생성자 대신 정적 팩터리 메서드를 고려하라
클라이언트는 클래스로부터 인스턴스를 얻기 위해 public 생성자 뿐만 아니라, 정적 팩터리 메서드(static factory method)를 통해 제공 받을 수 있다.

## 정적팩터리 메서드(Static Factory Method)란?
해당 클래스의 인스턴스를 반환하는 단순한 static 메서드이다. 

Boolean 클래스의 valueOf(), Optional 클래스의 of(), LocalTime 클래스의 of()가 정적 팩터리 메서드를 통해 인스턴스를 생성한다.

```java
public static LocalTime of(int hour, int minute) {
  HOUR_OF_DAY.checkValidValue(hour);
    if (minute == 0) {
      return HOURS[hour];  // for performance
    }
  MINUTE_OF_HOUR.checkValidValue(minute);
  return new LocalTime(hour, minute, 0, 0);
}
    
// hour, minutes을 인자로 받아서 11시 20분을 나타내는 LocalTime 객체를 반환
LocalTime time = LocalTime.of(9, 30);
```


또 다른 예시로 enum의 요소를 조회할 때 사용하는 valueOf도 정적 팩토리 메서드의 일종이라 할 수 있다. 
```java
public enum Color {
  RED,
  BLUE;
}

Color redColor = Color.valueOf("RED");
Color blueColor = Color.valueOf("BLUE");
```

정적 팩터리 메서드가 무엇인지 알았다면, **`[생성자가 있는데 굳이 정적 팩터리 메서드를 따로 만들어서 객체를 생성해야하는것인가]?`** 라는 의문이 생길것이다.

생성자와 정적 팩터리 메서드는 동일하게 객체를 생성하는 역할을 하지만 활용도에서 차이가 난다. 
정적 팩토리 메서드의 장/단점을 통해 적절하게 활용하는 법을 알아보고자 한다.

### 장점 

#### 첫 번째, 이름을 가질 수 있다.

객체는 생성 목적과 과정에 따라 생성자를 구별해서 사용할 필요가 있다.
생성자는내부 구조를 잘 알고 있어야 목적에 맞게 객체를 생성할 수 있다. 
하지만 정적 팩토리 메서드를 사용하면 메서드 이름에 객체의 생성 목적을 담아낼 수 있다.

```java
public class Product {
  private String name;
  
  public Product (String name) {
    this.name = name;
  }
  
  static Product nameOf (String name) {
    return new Product(name);
  }
}
```

```java
public class Main {
  public static void main (String[] args) {
    Product p1 = new Product("책상");
    Product p2 = Product.nameOf("의자");
  }
}
```
p1의 경우 `new Product("")`와 같이 new 예약어와 클래스명으로만 객체를 생성하는 반면에 p2의 경우 `nameOf`를 통해 객체를 생성한다. 

이처럼 정적 팩토리 메서드를 사용하면 해당 생성의 목적을 이름에 표현할 수 있어 가독성이 좋아진다.




#### 두 번째, "호출될 때마다 인스턴스를 새로 생성하지 않아도 된다.



#### 세 번째, 반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있다.

#### 네 번째, 입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다.

```java
public interface HelloService {
  String hello();
}
```
```java
public class Korea implements HelloService{
  String hello() {
    return "안녕하세요";
  }
}
```
```java
public class America implements HelloService{
  String hello() {
    return "Hi";
  }
}
```
```java
public class Main {
  public static void main(String[] args) {
    HelloService koreaHello = new Korea();
    HelloService americaHello = new America();
  }
}
```



#### 다섯 번째, 정적 팩터리 메서드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다. 



### 단점

#### 첫 번쨰, 상속을 하려면 public 이나 protected 생성자가 필요하니 정적 팩터리 메서드만 제공하면 하위 클래스를 만들 수 없다. 

#### 두 번째, 정적 팩터리 메서드는 프로그래머가 찾기 어렵다.


### :book: Reference :

[정적 팩토리 메서드(Static Factory Method)는 왜 사용할까?](https://tecoble.techcourse.co.kr/post/2020-05-26-static-factory-method/)

#


[[1]](#2️⃣-두번째) 플라이 웨이트 패턴
