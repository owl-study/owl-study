# 아이템2. 생성자에 매개변수가 많다면 빌더를 고려하라
## ❓점층적 생성자 패턴(Telescoping Constructor Pattern) 이란?
필수 매개변수만 받는 생성자, 선택 매개변수를 점차 늘려가면서 생성자를 만드는 패턴이다.

### ⌨️ 예시코드
```java
  public class NutritionFacts {
    private final int servingSize;    // (ml, 1회 제공량)       필수
    private final int servings;       // (회, 총 n회 제공량)    필수
    private final int calories;       // (1회 제공량당)         선택
    private final int fat;            // (g/1회 제공량)         선택
    private final int sodium;         // (mg/1회 제공량)        선택
    private final int carbohydrate;   // (g/1회 제공량)         선택
  }
  
  public NutritionFacts(int servingSize, int servings) {
    this(servingSize, servings, 0);
  }
  public NutritionFacts(int servingSize, int servings, int calories) {
    this(servingSize, servings, calories, 0);
  }
  public NutritionFacts(int servingSize, int servings, int calories, fat) {
    this(servingSize, servings, calories, fat, 0);
  }  
  public NutritionFacts(int servingSize, int servings, int calories, fat, sodium) {
    this(servingSize, servings, calories, fat, sodium, 0);
  }  
  public NutritionFacts(int servingSize, int servings, int calories, fat, sodium, carbohydrate) {
    this.servingSize  = servingSize;
    this.servings     = servings;
    this.calories     = calories;
    this.fat          = fat;
    this.sodium       = sodium;
    this.carbohydrate = carbohydrate;
  }
```
##### 단점
1. 초기화하고 싶은 필드만 포함한 생성자가 없다면, 설정하길 원치 않은 필드까지 매개변수에 값을 지정해줘야 한다.
2. 복잡하고 읽기 어렵다. 클라이언트가 실수로 매개변수의 순서를 바꿔서 건네줘도 컴파일러는 알아차리기 어렵다.
3. 매개변수의 수가 많아질 경우 걷잡을 수 없게된다.

✂️ 요약 : `점층적 생성자 패턴도 쓸 수는 있지만, 매개변수 개수가 많아지면 클라이언트가 코드를 작성 또는 읽기 어렵다.`

## ❓ 자바빈즈 패턴(JavaBeans Pattern) 이란?
매개변수가 없는 생성자로 객체를 만든 뒤, setter 메서드를 호출하여 원하는 매개변수 값을 설정하는 패턴이다.

### ⌨️ 예시코드
```java
  public class NutritionFacts {
  // 매개변수들은 (기본값이 있다면) 기본값으로 초기화된다.
    private int servingSize = -1;    // 필수
    private int servings = -1;       // 필수
    private int calories = 0;
    private int fat = 0;    
    private int sodium = 0;
    private int carbohydrate = 0;
  }
  // 기본 생성자
  public NutritionFacts() { }
  
  // Setter
  public void setServingSize(int val) {
    servingSize = val;
  }
  
  public void setServingSize(int val) {
    servings = val;
  }
  
  public void setServingSize(int val) {
    calories = val;
  }
  
  public void setServingSize(int val) {
    fat = val;
  }
  
  public void setServingSize(int val) {
    sodium = val;
  }
  
  public void setServingSize(int val) {
    carbohydrate = val;
  }
  
```
##### 장점
1. 각 인자 의미 파악이 쉬워졌고, 복잡한 여러 생성자를 생성할 필요가 없다.

##### 단점
1. 객체 하나를 만들려면 여러 메서드를 호출해야하고, 객체가 완전히 생성되기 전까지는 **일관성(consistency)**이 무너진 상태에 놓이게 된다.

✂️ 요약 : `setter로 인해 **immutable Class(변경 불가 클래스)** 생성이 불가능하다.`

## ❓ 빌더 패턴(Builder Pattern) 이란?
`점층적 생성자 패턴`의 안전성과 `자바빈즈 패턴`의 가독성을 모두 가지는 패턴이 빌더 패턴이다.
클라이언트는 필수 매개변수만으로 생성자(또는 정적 팩터리)를 호출하여 빌더 객체를 얻는다.

### ⌨️ 예시코드 (1)
```java
public Class NutritionFacts{
  private final int servingSize;
  private final int servings;
  private final int calories;
  private final int fat;
  private final int sodium;
  private final int carbohydrate;
  
  public static class Builder {
    // 필수 매개변수
    private final int servingSize;
    private final int servings;
    
    // 선택 매개변수 (기본 값으로 초기화)
    private int calories      = 0;
    private int fat           = 0;
    private int sodium        = 9;
    private int carbohydrate  = 0;
    
    // Builder의 생성자
    public Buider(int servingSize, int servings) {
      this.servingSize  = servingSize;
      this.servings     = servings;
    }
   
    public Builder calories(int val)
      calories = val;
      return this;
    }
    public Builder fat(int val)
      fat = val;
      return this;
    }
    public Builder sodium(int val)
      sodium = val;
      return this;
    }
    public Builder carbohydrate(int val)
      carbohydrate = val;
      return this;
    }
    public NutritionFacts build() {
      return new NutritionFacts(this);
    }
  private NutritionFacts(Builder builder) {
    servingSize   = builder.servingSize;
    servings      = builder.servings;
    calories      = builder.calories;
    fat           = builder.fat;
    sodium        = builder.sodium;
    carbohydrate  = builder.carbohydrate;
  } 
}
```
NutritionFacts 클래스는 불변이고, 모든 매개변수의 기본값들을 한곳에 모아 빌더의 세터 메서드를 통해 return this하여 연쇄적으로 호출 할 수 있다.
연쇄적 호출하는 방식을 **메서드 체이닝(method chaining)** 또는 **플루언트 API(fluent API)**라고 한다.

### ⌨️ 예시코드 (2)
### Pizza 클래스
```java
public abstract class Pizza {
  public enum Topping {HAM, MUSHROOM, ONION, PEPPER, SAUSAGE,}

  final Set<Topping> toppings;

  // 추상 빌더
  abstract static class Builder<T extends Builder<T>>{ // 재귀적 타입 제한 (자신(자신의 하위)을 타입으로 지정)
    EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);
    
    // 핵심
    public T addTopping(Topping topping) {              // "T"를 Builder<T> 하면 return 타입으로 this 할 수 있다.
        toppings.add(Objects.requireNonNull(topping));
        return self();                                  // 상속 구조일 경우 만약 this 를 리턴하면 자기 자신이 된다.
    }
    
     abstract Pizza build();
    // 하위 클래스는 이 메서드를 재정의하여 "this"를 반환하도록 해야한다.
    protected abstract T self();
    
  }
  Pizza(Builder<?> builder) {
    toppings = builder.toppings.clone();
  }
}
```
### NyPizza 클래스
```java
public class NyPizza extends Pizza {
    public enum Size {SMALL, MEDIUM, LARGE}
    private final Size size;

    public static class Builder extends Pizza.Builder<NyPizza.Builder> {    // <Builder>는 Pizza 의 빌더가 아닌 NyPizza 의 빌더이다.
        private final Size size;

        public Builder(Size size) {
            this.size = Objects.requireNonNull(size);
        }

        @Override
        public NyPizza build() {
            return new NyPizza(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    private NyPizza(Builder builder) {
        super(builder);
        size = builder.size;
    }
}
```
`빌더 패턴은 계층적으로 설계된 클래스와 함께 쓰기 좋다.`
추상 클래스는 추상 빌더를 가지게 하고, 구체 클래스(concrete class)는 구체 빌더를 갖게 한다. 
위 예제코드로 설명하자면, **NyPizza.Builder**는 **NyPizza**를 반환해야 한다.
하위 클래스의 메서드가 상위 클래스의 메서드가 정의한 반환 타입이 아닌, 자신을 반환하는 기능을 **공변 반환 타이핑(covariant return typing)** 이라고 한다.

### ❗ Concrete class란?
 객체지향 관련 서적에 concrete class으로 표현되어 있는 것이 대부분 번역서에서 `구상 클래스`, 
 `구현 클래스` 또는 `구체 클래스`로 번역되어 있어 의미가 혼돈이 될 수 있다.
 모든 연산에 대하여 구현되어 있는 클래스가 `concrete class`이다. 요약하자면, `추상 클래스가 아닌 클래스`를 concrete class 이다.

 [reference : What is the Concrete class in java](https://stackoverflow.com/questions/43224901/what-is-the-concrete-class-in-java)
 
### ❗ Objects.requireNonNull() 를 왜 쓸까?
`Ojbects.requireNonNull()` 메서드는 이펙티브 자바에서 자주 볼 수 있다. 
해당 메서드는 이름을 통해 어떤 역할을 하는지 쉽게 파악할 수 있는데, 이 메서드는 `메서드나 생성자의 파라미터 null 검증`을 위해 설계된 메서드 이다.

```java
public static <T> T requireNonNull(T obj) {
  if (obj == null)
    throw new NullPointerException();
  return obj;
}
```
해당 메서드를 왜 쓰는지 궁금할 것이다. 왜냐면 참조 객체가 null일 경우 NPE(NullPointerException)이 나는 기능뿐이기 때문이다.
이를 사용하는 이유는 다음과 같다.
##### * explicity (명시성)
##### * fail fast (빠른 실패)  - 가능한 빠르게 실패하고, 문제를 파악할 수 있다.

###### explict
### ⌨️ 예시코드

```java
public class A {
  String name;
}
```
```java
public class B {
  A a;
  public B (A a) {
    this.a = Objects.requireNonNull(a);
  }
}
```
위 코드와 같이 A를 참조하는 B가 있을 때, A가 null이 아니어야 함을 명시적으로 표현할 수 있다.

###### fail-fast
### ⌨️ 예시코드
```java
A a = null'
B b = new B(a);     // 생성 시점에 바로 NPE 발생
```

```java
public class C {
  A a;
  public C (A a) {
    this.a = a;
  }
}
```
requireNonNull을 사용하지 않은 경우 바로 익셉션을 발생하지 않고 이후에 해당 객체가 사용될 때 NPE가 발생하게 된다.
이는 시스템이 복잡해질 수록 장애를 발경하기 어렵게 만든다.

[reference : Objects.requireNonNull](https://velog.io/@rockpago/Objects.requireNonNull)



