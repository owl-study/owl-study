

# 아이템1. 생성자 대신 정적 팩터리 메서드를 고려하라
## ❓ 정적 팩터리 메서드란?
정적 팩터리 메서드라는 말이 낯설게 느껴질 수도 있겠지만 사실은 꽤 친숙한 메서드일 것이다. **객체를 생성할 때, 생성자가 아니라 정적 Static 메서드를 사용하는 것**을 정적 팩토리 메서드라고 한다. 

> 여기서 팩터리는 객체를 생성하는 역할을 분리하겠다는 의미로 [GoF 디자인 패턴](#1-gof-디자인-패턴) 중 [팩토리 패턴](#2-팩토리-패턴)에서 이름만 따왔을 뿐 크게 연관성은 없다.

### 🤓 예시 코드
#### (1) LocalTime 클래스의 of 메소드 (Static 메서드)

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

`LocalTime`의 `of` 메소드처럼 생성자를 통해 직접 객체를 생성하는 것이 아니라 **_메서드로 객체를 생성하는 것_** 을 정적 팩터리 메서드 라고 한다.

## 🤔 왜 생성자 대신 정적 팩터리 메서드를 고려해야할까?
### 🖐️ 다섯가지 이유
#### 1️⃣ 첫번째 : "이름을 가질 수 있다."

프로그래밍에서 다른 사람이 이해하기 쉽도록 코드를 짜는 것은 중요한 요소 중에 하나다. 
`new` 키워드를 사용해 객체를 만드는 생성자는 생성자의 결국 내부 구조를 잘 알고 있어야만 목적에 알맞게 객체를 생성할 수 있다. 
반면 정적 팩터리 메서드는 이름을 가질 수 있으므로 네이밍을 통해 어떤 값을 이용해 객체를 만드려고 하는지 등 읽는 이에게 코드의 설계 의도를 좀 더 쉽게 전달할 수 있다. 

#### 🤓 비교 1. 생성자

```java
class Phone {
    private String brand;

    public Phone(String brand) {
        this.brand = brand;
    }
}
```

```java
Public class Application {
    public static void main(String[] args) {
        Phone phone = new Phone("Samsung");
    }
}

```
> 위와 같이 생성자로 Phone 객체 생성할 때, 생성자의 내부 구조를 모르고 있다면 new 키워드만으로는 "Samsung" 어떤 정보인지 이해하는 것이 어렵다.

#### 🤓 비교 2. 정적 팩토리 메서드


```java
class Phone {
    private String brand;

    private Phone(String brand) {
        this.brand = brand;
    }
    
    public static Phone brandOf(String brand) {
        return new Phone(brand);
    }
}
```
```java
public class Application {
    public static void main(String[] args) {
        Phone phone = Phone.brandOf("Samsung");
    }
}
```
> 반면 정적 팩토리 메서드로 객체를 생성하는 경우 메서드의 이름을 통해 직관적으로 객체에 대한 정보를 단번에 이해할 수 있다. 이처럼 정적 팩토리 메서드는 객체 생성 시 목적에 알맞은 이름을 표현함으로써 코드의 가독성이 좋아지는 효과가 있다.

또, 같은 시그니처를 가지는 생성자 여러 개가 필요할 때에도 **정적 팩터리 메서드**를 이용할 수 있다.

#### 🤓 비교 1. 생성자

```JAVA
public class Student {
    private String name;
    private String id;

    // 컴파일 오류 발생
    public Student(String name){
        this.name = name;
    }

    public Student(String id){
        this.id = id;
    }
}
```

> 일반적인 생성자 방식으로는 매개변수의 이름만 다른 두 개의 생성자를 만들 수 없다.

#### 🤓 비교 2. 정적 팩토리 메서드

```JAVA
public class StaticStudent {
    private String name;
    private String id;

    public static StaticStudent name(String name){
        StaticStudent staticStudent = new StaticStudent();
        staticStudent.name = name;
        return staticStudent;
    }

    public static StaticStudent id(String id){
        StaticStudent staticStudent = new StaticStudent();
        staticStudent.id = id;
        return staticStudent;
    }
}
```
> 하지만 정적 팩터리 메서드로 바꿔준다면 같은 시그니처를 가지는 생성자 여러개를 만들 수 있다.

#### 2️⃣ 두번째 : "호출할 때마다 인스턴스를 새로 생성할 필요가 없다."
인스턴스의 내부 값을 수정할 수 없는 클래스를 [불변 클래스](#3-불변-클래스)라고 한다. 대표적인 불변 클래스 Boolean 처럼 자주 사용하는 인스턴스(`true`, `false`)의 개수가 정해져있다면 해당하는 숫자만큼 미리 인스턴스를 생성해놓고 조회(캐싱)하여 재활용하는 구조로 만들수 있다. 이렇게 정적 팩터리 메서드와 캐싱 구조를 함께 사용하면 매번 새로운 객체를 생성할 필요가 없어져 불필요한 객체 생성을 피할 수 있다. 특히나 **규모가 커서 생성 비용도 큰 객체가 자주 요청되는 상황이라면** 성능이 저하될 가능성이 있는데, 정적 팩토리 메서드를 사용하면 **성능을 상당히 끌어올릴 수 있는 것이다.**

#### 🤓 예시 코드

```java
public final class Boolean implements java.io.Serializable,Comparable<Boolean> {
    public static final Boolean TRUE = new Boolean(true);
    public static final Boolean FALSE = new Boolean(false);
    public static Boolean valueOf(boolean b) {
        return (b ? TRUE : FALSE);
    }
}
```

> 위의 Boolean 클래스는 `TRUE`, `FALSE`를 상수로 정의해놓고 `valueOf()` 메소드에서 기본 타입인 boolean 값을 받아 Boolean 객체 참조로 변환해주고 있다. `valueOf()` 메소드는 객체를 아예 생성하지 않고 상수를 반환하는 것이다. 이렇게 인스턴스를 미리 만들어 놓거나 새로 생성한 인스턴스를 캐싱하여 재활용하는 식으로 불필요한 객체 생성을 피하고, 객체 생성 비용이 큰 객체가 자주 요청되는 상황에서 성능을 끌어올릴 수 있다.

 
#### 🚶‍♂️ 더 나아가기
이렇게 호출마다 인스턴스를 생성하는 것이 아니라, 같은 요청에 같은 인스턴스를 반환하는 방식으로 인스턴스의 생성과 파괴를 철저하게 통제하는 클래스를 인스턴스 통제 클래스 라고 한다. 또 인스턴스가 단 하나뿐임을 보장하며 인스턴스를 통제하는 것은 [플라이웨이트 패턴](#4-플라이웨이트-패턴)의 근간이 된다.

#### 3️⃣ 세번째 : "하위 타입의 객체를 반환할 수 있다."
생성자를 사용하여 객체를 생성하는 경우 객체의 클래스가 하나로 제한되지만, 정적 팩터리 메서드를 사용하면 원하는 객체를 리턴할 수 있다. 자바 다형성의 특징을 이용하여 높은 자유도와 유연성을 제공하는 것이다. 인터페이스를 구현하고 있는 클래스를 노출시키지 않을 수 있으니 사용자 입장에서도 반환 된 클래스가 어떤 클래스인지 굳이 찾아보지 않아도 되는 장점까지 있다. JAVA 8 부터는 인터페이스에서도 정적 메서드 선언이 가능해졌으므로, 인터페이스에서도 간단하게 이 방법을 구현할 수 있다.

#### 🤓 예시 코드
##### 🤓 부모 클래스
```JAVA
public interface School {
    // 학교 소개
    void print();
    // School 을 상속받는 하위타입 객체 리턴
    static School introduceSchool(String name){
        return new HangangSchool(name);
    }
}
```
##### 🤓 자식 클래스
```JAVA
public class HangangSchool implements School{

    private String name;

    public HangangSchool(String name){
        this.name = name;
    }

    @Override
    public void print() {
        System.out.println( name + "초등학교입니다.");
    }
}
```
##### 🤓 실행문
```JAVA
public class MainReturnLow {
    public static void main(String[] args) {
        // 매개변수 한강을 넣어주면 하위타입 객체를 반환시켜 준다.
        School hangang = School.introduceSchool("한강");
        // 학교 소개 출력.
        hangang.print();
    }
}
```
##### 🤓 결과문
```
한강초등학교입니다.
```
#### 4️⃣ 네번째 : "매개변수에 따라 다른 클래스의 객체를 반환할 수 있다."
위에서 설명한 세번째 장점과 유사한 맥락으로, 반환 타입의 하위타입이기만 하면 어떤 클래스의 객체를 반환하든 상관 없다. 
대표적으로 EnumSet 클래스를 예로 들 수 있다.

#### 🤓 예시 코드

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

#### 5️⃣ 다섯번째 : "작성 시점에 반환할 객체의 클래스가 없어도 된다."

한마디로 클라이언트를 구현체로부터 분리해주는 것이다. JDBC의 서비스 접근 API인 `DriverManager.getConnection()`을 예로 들을 수 있다. 
  ~~~JAVA
import java.util.ArrayList; 
import java.util.List; 

public class TicketStore { 
	/** TicketSeller는 인터페이스이고 구현체가 없음에도 아래와 같은 메서드 작성이 가능하다.**/ 
	public static List<TicketSeller> getSellers(){ 
		return new ArrayList<>(); 
	} 
}
~~~

반환값이 인터페이스여도 상관이 없다!  
그리고 정적팩터리 메서드의 변경 없이 구현체를 바꿔끼울 수 있어서 코드가 유연해진다.  
이렇게 인터페이스나 클래스가 만들어 지는 시점에서 하위 타입의 클래스가 **존재 하지 않아도**, 나중에 만들 클래스가 기존의 인터페이스나 클래스를 상속 받는 상황이면 언제든지 의존성을 주입 받아서 **사용이 가능**하다.  
  
한마디로 클라이언트를 구현체로부터 분리해준다고 말할 수 있다.  
이게 무슨소리냐 하면 JDBC의 서비스 접근 API인 DriverManager.getConnection()을 예로 들을 수 있다.  
우리가 일반적으로 JDBC에서 Connection 을 가져오는 코드는 다음과 같다.  
  
  
~~~JAVA
// JDBC 1단계 : 드라이버 객체 로딩			
DriverManager.registerDriver(new org.h2.Driver());

// JDBC 2단계 : 커넥션 연결
conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa", "");
~~~

여기 2단계인 `getConnection()`에서 반환되는 구현체는 DBMS 의 종류에 따라 달라진다. 클라이언트는 세부적인 구현 내용을 몰라도 서비스를 이용 할 수 있다. `Connection` 타입은 인터페이스이며, 실제로 구현하는 클래스가 존재하지 않아도 되고, 구현하는 클래스가 계속 추가될 수도 있다.

## 😓 장점이 있으면 단점도 있는 법.
### ✌️ 두가지 단점
#### 1️⃣ 첫번째 : "하위 클래스를 만들 수 없다."
상속을 하려면 public 혹은 protected 생성자가 필요하기때문에, 정적 팩터리 메서드만 제공하면 하위 클래스를 만들 수 없다. 인스턴스 통제 클래스를 구현하기 위해서는 사용자가 new 키워드를 사용하여 임의로 객체를 생성함을 막아야한다. 이를 위해 생성자의 접근 제어자를 private 로 설정해야하는데, 생성자가 private 인 클래스는 상속을 할 수 없다. 즉, 부모 클래스가 될 수 없다. 그러나 이 제약은 상속보다 [컴포지션](#5-컴포지션)을 사용하도록 유도하고, [불변 타입](#6-불변-객체-타입)으로 만들기 위해서는 이 제약을 지켜야 한다고 한다. 따라서 이 단점은 장점으로도 작용할 수도 있다.  


#### 🤓 예시 코드

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

자식 클래스의 생성자에서 super()을 통해 부모 클래스의 생성자를 호출하는데, 부모 클래스의 생성자가 private으로 지정되어 있어 컴파일 에러가 발생한다.

따라서 이것을 해결하려면 부모 생성자를 public 또는 protected로 지정해주어야 한다.

사실 상속은 의존도를 높이므로 좋지 않다. 

컴포지션(조합) 사용을 유도하기 때문에 오히려 장점으로 받아들일 수 있다.

```java
public class Table {
  private Furniture;
}
```
컴포지션이란 상속이 아닌 인스턴스 변수로 가져오는 방법이다.


#### 2️⃣ 두번째 : "프로그래머가 찾는 것이 어렵다."
java doc에서 클래스의 생성자는 잘 표시되어있어 쉽게 찾을 수 있으나, 정적 팩터리 메서드는 일반 메서드이기 때문에 개발자가 직접 문서를 찾아야하는 어려움이 있다. 따라서 API 문서를 잘 작성하고 널리 알려진 메서드 네이밍 컨벤션을 사용해서 클라이언트가 잘 사용할 수 있도록 작성해야 한다. 아래는 정적 팩터리 메서드에서 흔히 사용하는 명명 방식이다.

#### 📛 정적 팩터리 메서드 명명방식
- **`from`** : 매개변수를 하나 받아서 해당 타입의 인스턴스를 반환하는 형변환 메서드
- > 
  ```java
  Date d = Date.from(instant);
  ```
- **`of`** : 여러 매개변수를 받아 적합한 타입의 인스턴스를 반환하는 집계 메서드
  > 
  ```java 
  Set<Rank> faceCards = EnumSet.of(JACK, QUEEN, KING);
  ```
  
- **`valueOf`** : `from`과 `of`의 더 자세한 버전
  > 
  ```java 
  BigInteger prime = BigInteger.valueOf(Integer.MAX_VALUE);
  ```
  
- **`instace` 혹은 `getInstance`** : (매개변수를 받는다면) 매개변수로 명시한 인스턴스를 반환하나, 같은 인스턴스임을 보장하지 않음.
  > 
  ```java 
  StackWalker luke = StackWalker.getInstance(options);
  ```
  
- **`create` 혹은 `newInstance`** : `instance` 혹은 `getInstance`와 같지만 매번 새로운 인스턴스를 생성해 반환함을 보장.
  > 
  ```java 
  Object newArray = Array.newInstance(classObject, arrayLen);
  ```
  
- **`getType`** : `getInstance`와 같으나 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 사용. `Type`은 팩터리 메서드가 반환할 객체의 타입.
  > 
  ```java 
  FileStore fs = files.getFileStore(path);
  ```
  
- **`newType`** : `newInstance`와 같으나 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 사용. `Type`은 팩터리 메서드가 반환할 객체의 타입.
  > 
  ```java 
  BufferedReader br = Files.newBufferedReader(path);
  ```
  
- **`type`** : `getType`과 `newType`의 간결한 버전
  > 
  ```java 
  List<Complaint> litany = Collections.list(legacyLitany);
  ```
  

## 🙆‍♀️ 정리!
> 정적 팩터리 메서드와 public 생성자는 각자의 쓰임새가 있으니 상대적인 장단점을 이해하고 사용하는 것이 좋다. 하지만 대부분의 경우 정적 팩터리를 사용하는 편이 유리하므로 무작정 public 생성자를 사용했다면 습관을 고치는 것이 좋다.

 # 👼 Reference
 - [정적 팩토리 메서드(Static Factory Method)는 왜 사용할까? by 2기_보스독](https://tecoble.techcourse.co.kr/post/2020-05-26-static-factory-method/)
 - [Java - 정적 팩토리 메서드의 정의와 네이밍 컨벤션 by 프로그래머 YD](https://7942yongdae.tistory.com/147)
 - [[Effective Java] 아이템1: 생성자 대신 정적 팩토리 메소드를 고려하라 by Gyun's 개발일지](https://devlog-wjdrbs96.tistory.com/256)
 - [1. 객체 생성과 파괴 by Catsbi;s DLog](https://catsbi.oopy.io/d7f3a636-b613-453b-91c7-655d71fda2b1)
 - [[Java] 28. 정적 팩토리 메서드?! by kim.svadoz](https://coder-in-war.tistory.com/entry/Java-28-%EC%A0%95%EC%A0%81-%ED%8C%A9%ED%86%A0%EB%A6%AC-%EB%A9%94%EC%84%9C%EB%93%9C)
 - [생성자 대신 정적 팩터리 메서드를 고려하라 by Hudi.blog](https://hudi.blog/effective-java-static-factory-method/)
 - [[이펙티브 자바] 아이템1. 생성자 대신 정적 팩터리 메서드를 고려하라 by 성건희](https://web2eye.tistory.com/220#%EC%A0%95%EC%A0%81-%ED%8C%A9%ED%86%A0%EB%A6%AC-%EB%A9%94%EC%84%9C%EB%93%9C-%EB%8B%A8%EC%A0%90)
 - [아이템 [1] - 생성자 대신 정적 팩터리 메서드를 고려하라 by 오늘의 개발](https://a1010100z.tistory.com/entry/%EC%95%84%EC%9D%B4%ED%85%9C-1-%EC%83%9D%EC%84%B1%EC%9E%90-%EB%8C%80%EC%8B%A0-%EC%A0%95%EC%A0%81-%ED%8C%A9%ED%84%B0%EB%A6%AC-%EB%A9%94%EC%84%9C%EB%93%9C%EB%A5%BC-%EA%B3%A0%EB%A0%A4%ED%95%98%EB%9D%BC)



---

###### [[1]](https://github.com/leejk0924/owl-study/blob/main/to_create_EJI/jk_lee/Item_1/finish_up/item1.md#-%EC%A0%95%EC%A0%81-%ED%8C%A9%ED%84%B0%EB%A6%AC-%EB%A9%94%EC%84%9C%EB%93%9C%EB%9E%80) GoF 디자인 패턴
<디자인 패턴>(Design Patterns, ISBN 0-201-63361-2)은 소프트웨어 설계에 있어 공통된 문제들에 대한 표준적인 해법과 작명법을 제안한 책이다. 책의 첫 번째 반절은 다양한 디자인 패턴의 정의에 할애하고 있고, 나머지 반절은 실제적으로 유용한 디자인 패턴들을 나열하고 있다. 책의 예제들은 객체지향적인 언어인 C++과 스몰토크로 제시되고 있다.

##### [[2]](https://github.com/leejk0924/owl-study/blob/main/to_create_EJI/jk_lee/Item_1/finish_up/item1.md#-%EC%A0%95%EC%A0%81-%ED%8C%A9%ED%84%B0%EB%A6%AC-%EB%A9%94%EC%84%9C%EB%93%9C%EB%9E%80) 팩토리 패턴
팩토리 메서드 패턴(Factory method pattern)은 부모 클래스에서 알려지지 않은 구체 클래스를 생성하는 패턴이며, 자식 클래스가 어떤 객체를 생성할지를 결정하도록 하는 패턴이기도 하다. 

##### [[3]](https://github.com/leejk0924/owl-study/blob/main/to_create_EJI/jk_lee/Item_1/finish_up/item1.md#2%EF%B8%8F%E2%83%A3-%EB%91%90%EB%B2%88%EC%A7%B8--%ED%98%B8%EC%B6%9C%ED%95%A0-%EB%95%8C%EB%A7%88%EB%8B%A4-%EC%9D%B8%EC%8A%A4%ED%84%B4%EC%8A%A4%EB%A5%BC-%EC%83%88%EB%A1%9C-%EC%83%9D%EC%84%B1%ED%95%A0-%ED%95%84%EC%9A%94%EA%B0%80-%EC%97%86%EB%8B%A4) 불변 클래스
불변 객체란 객체 생성 이후 내부의 상태가 변하지 않는 객체이다. 불변 객체는 read-only 메서드만을 제공하며, 객체의 내부 상태를 제공하는 메서드를 제공하지 않거나 방어적 복사(defensive-copy)를 통해 제공한다. Java의 대표적인 불변 객체로는 String이 있다.

###### [[4]](https://github.com/leejk0924/owl-study/blob/main/to_create_EJI/jk_lee/Item_1/finish_up/item1.md#%EF%B8%8F-%EB%8D%94-%EB%82%98%EC%95%84%EA%B0%80%EA%B8%B0) 플라이웨이트 패턴 
어떤 클래스의 인스턴스 한 개만 가지고 여러 개의 "가상 인스턴스"를 제공하고 싶을 때 사용하는 패턴이다. 즉, 인스턴스를 가능한 대로 공유시켜 쓸데없이 `new` 연산자를 통한 메모리 낭비를 줄이는 방식이다. 

###### [[5]](https://github.com/leejk0924/owl-study/blob/main/to_create_EJI/jk_lee/Item_1/finish_up/item1.md#1%EF%B8%8F%E2%83%A3-%EC%B2%AB%EB%B2%88%EC%A7%B8--%ED%95%98%EC%9C%84-%ED%81%B4%EB%9E%98%EC%8A%A4%EB%A5%BC-%EB%A7%8C%EB%93%A4-%EC%88%98-%EC%97%86%EB%8B%A4) 컴포지션

다른 객체의 인스턴스를 자신의 인스턴스 변수로 포함해서 메서드를 호출하는 기법이다. 해당 인스턴스의 내부 구현이 바뀌더라도 영향을 받지 않는 장점이 있다. 또한, 다른 객체의 인스턴스이므로 인터페이스를 이용하면 Type을 바꿀 수 있다.

###### [[6]](https://github.com/leejk0924/owl-study/blob/main/to_create_EJI/jk_lee/Item_1/finish_up/item1.md#1%EF%B8%8F%E2%83%A3-%EC%B2%AB%EB%B2%88%EC%A7%B8--%ED%95%98%EC%9C%84-%ED%81%B4%EB%9E%98%EC%8A%A4%EB%A5%BC-%EB%A7%8C%EB%93%A4-%EC%88%98-%EC%97%86%EB%8B%A4) 불변 객체 타입
불변 객체(Immutable)란 생성 후 그 상태를 변결 할 수 없는 객체를 말한다. 반대 개념으로는 가변(mutable) 객체로 새성 후에도 상태를 변경 할 수 있다. 불변객체의 초기 상태는 대개 생성 시에 결정되지만 객체가 실제로 사용되는 순간까지 늦추기도 한다. 불변 객체를 사용하면 복제나 비교를 위한 조작을 단순화 할 수 있고, 성능 개선에도 도움을 준다. 하지만 객체가 변경 가능한 데이터를 많이 가지고 있는 경우엔 불변이 오히려 부적절한 경우가 있다. 이 때문에 많은 프로그래밍 언어에서는 불변이나 가변 중 하나를 선택할 수 있도록 하고 있다. 
