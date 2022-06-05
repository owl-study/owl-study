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

대표적으로 EnumSet 클래스를 예로 들 수 있다.

```java
public static <E extends Enum<E>> EnumSet <E> noneOf(Class<E> elementType) {
  Enum<?>[] universe = getUniverse(elementType);
  if (universe == null) {
    throw new ClassCastException(elementType + " not an enum");
  } else {
    return (EnumSet)(universe.length < 64 ? new RegularEnumSet(elementType, universe) : new JumboEnumSet(elementType, universe));
  }
}
```

원소가 64개 이하면 RegularEnumbset, 65개 이상이면 JumboEnumSet의 인스턴스를 반환한다. 클라이언트는 이 두 클래스의 존재를 모른다.

의존도가 낮기 때문에 nonOf 메서드의 요구사항이 변경되어 코드가 수정되어도 문제가 없다.(유지보수가 좋다.)



#### 다섯 번째, 정적 팩터리 메서드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다. 



### 단점

#### 첫 번쨰, 상속을 하려면 public 이나 protected 생성자가 필요하니 정적 팩터리 메서드만 제공하면 하위 클래스를 만들 수 없다. 

```java
public class Fruit {

  public class Furniture() { }    // 기본 생성자

  public static Table createTable() {
    return new Table();
  }
}
```

```java
public class Table extends Furniture {
  public Table(){
    super();      // 컴파일 에러 발생
  }
}
``` 

자식 클래스를 생성 할 때는 부모 클래스를 먼저 super() 메서드로 생성하는데, 부모 클래스의 생성자가 private으로 지정되어 있어 컴파일 에러가 발생한다.

따라서 이것을 해결하려면 부모 생성자를 public 또는 protected 로 지정해주어야 한다. 

사실 상속은 의존도를 높이므로 좋지 않다. 컴포지션(조합) 사용을 유도하여 오히려 장점으로 받아들일 수 있다.

```java
puublic class Table {
  private Furniture;
}
```

컴포지션이란 상속이 아닌 인스턴스 변수로 가져오는 방법이다. 


#### 두 번째, 정적 팩터리 메서드는 프로그래머가 찾기 어렵다.

javadox이 생성자는 자동으로 정리해주지만 정적 팩터리 메서드는 정리해주지 않는다. 따라서 API 문서를 잘 작성하고 널리 알려진 메서드 네이밍 컨벤션을 사용해서 클라이언트가 잘 사용할 수 있도록 작성해야 한다. 아래는 정적 팩터리 메서드에서 흔히 사용하는 명명 방식이다.

|제목|내용|예시|
|:-----------:|--------------------------------------------------------------|--------------------------------------------------------------------------------------|
|**from**|매개변수를 하나 받아서 해당 타입의 인스턴스를 반환하는 형변환 메서드|Date d = Date.from(instant);
|**of**|여러 매개변수를 받아 적합한 타입의 인스턴스를 반환하는 집계 메서드|Set<Rank> faceCards = EnumSet.of(JACK, QUEEN, KING);|
|**valueOf**| from 과 of의 더 자세한 버전 | BigInteger prime = BigInteger.valueOf(Integer.MAX_VALUE);|
|**instance / getInstance**|(매개변수를 받는다면) 매개변수로 명시한 인스턴스를 반환하지만, 같은 인스턴스임을 보장하지 않는다.|StackWalker luke = StackWalker.getInstance(options);|
|**create / newInstance**|instance 혹인 getInstance와 같지만, 매번 새로운 인스턴스를 생성해 반환함을 보장한다.|Object newArray = Array.newInstance(classObject, arrayLen);|
|**getType**|getInstance와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 쓴다. "Type"은 팩터리 메서드가 반환할 객체의 타입이다.|FileStor fs = Files.getFileStore(path)|
|**newType**|newInstance와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 쓴다. "Type"은 팩터리 메서드가 반환할 객체의 타입이다.|BufferedReader br = Files.newBufferedReader(path)|
|**type**|getType과 newType의 간결한 버전| List<Complaint> litany = Collections.list(legacyLitany);|

<br>

  
## 핵심정리
```
  정적 팩터리 메서드와 public 생성자는 각자의 쓰임새가 있으니 상대적인 장/단점을 이해하고 사용하는 것이 좋다.
  
  그렇다고 하더라도 정적 팩터리를 사용하는 것이 유리한 경우가 많으므로 무작정 public 생성자를 제공하던 습관이 있다면 고치는 것이 좋다.
```
  

  
### :book: Reference :

[정적 팩토리 메서드(Static Factory Method)는 왜 사용할까?](https://tecoble.techcourse.co.kr/post/2020-05-26-static-factory-method/)
  
[[이펙티브 자바] 아이템1. 생성자 대신 정적팩터리 메서드를 고려하라_by 성건희](https://web2eye.tistory.com/220#%EC%A0%95%EC%A0%81-%ED%8C%A9%ED%86%A0%EB%A6%AC-%EB%A9%94%EC%84%9C%EB%93%9C-%EB%8B%A8%EC%A0%90)


---

[[1]](#2️⃣-두번째) 플라이 웨이트 패턴
