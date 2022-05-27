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
1. 객체 하나를 만들려면 여러 메서드를 호출해야하고, 객체가 완전히 생성되기 전까지는 일관성(consistency)이 무너진 상태에 놓이게 된다.

✂️ 요약 : `setter로 인해 immutable Class(변경 불가 클래스) 생성이 불가능하다.`

## ❓ 빌더 패턴(Builder Pattern) 이란?
`점층적 생성자 패턴`의 안전성과 `자바빈즈 패턴`의 가독성을 모두 가지는 패턴이 빌더 패턴이다.
클라이언트는 필수 매개변수만으로 생성자(또는 정적 팩터리)를 호출하여 빌더 객체를 얻는다.
그리고 메서드 체이닝(method chaining)을 통해 선택 매개변수를 설정한다.
### ⌨️ 예시코드
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


