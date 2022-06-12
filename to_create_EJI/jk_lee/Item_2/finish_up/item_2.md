# 아이템2.생성자에 매개변수가 많다면 빌더를 고려하라
## ❓ 빌더란?

### 대표적인 객체 생성 디자인패턴
> - [점층적 생성자(Telescoping Constructor) 패턴]()
> - [자바 빈(JavaBean) 패턴]()
> - [빌더(Builder) 패턴]()

생성자(Constructor)로 인스턴스를 생성할 때 겪는 불편함에 대한 방안으로 고안 된 디자인 패턴이다. Java에서 클래스를 객체화하는 패턴으로는 [점층적 생성자 패턴]()과 [자바 빈즈 패턴](), [빌더 패턴]()이 존재하는데, 점층적 생성자 패턴이 가진 안전성과 자바 빈즈 패턴의 가독성이 합쳐진 것이 빌더 패턴이다. 빌더 패턴에 대해서 더 자세히 알아보기 위해서는 결국 나머지 두 패턴을 이해하고 비교해보아야 한다.


## 🤔 왜 하필 빌더를 고려해야할까?

### 빌더 패턴의 장점
> - 매개변수가 많아도 쉽고 안전하게 객체를 생성할 수 있다.
> - 매개변수의 순서와 상관없이 유연하게 객체를 생성할 수 있다.
> - 적절한 이름을 부여하여 가독성을 높일 수 있다.

결론부터 말하자면 빌더패턴은 생성자(Constructor)가 많을 경우 또는 오브젝트 생성 후 변경 불가능한 불변 오브젝트가 필요한 경우, 불변 오브젝트를 생성하여 오브젝트의 일관성(Consistency),변경 불가능(immutable)을 실현하여 코드의 가독성과 불변성,일관성을 유지하도록 하는 장점이 있다. 앞에서 설명했듯 이런 빌더 패턴의 장점은 다른 패턴과의 비교를 통해서 더 깊게 이해할 수 있을 것이다.

## 점층적 생성자 패턴
매개변수를 하나씩 늘려가며 생성자를 오버로딩 하는 기법으로, 필요한 매개변수가 포함된 생성자 중에서 가장 짧은 생성자를 골라 호출하면 된다. 그러나 사용자가 설정하기 원하지 않는 매개변수에도 값을 지정해야 하고, 매개변수의 수가 많아질수록 가독성이 떨어진다. 같은 타입의 매개변수 여러개가 연속되어있다면 각 값의 의미를 이해하기 어렵다. 또 순서를 뒤바꿔 넘겨줬을 때 컴파일러에서 오류를 걸러낼 수 없기때문에 찾기 힘든 버그로 이어질 수 있다.

~~~java
public class NutritionFacts {

    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;
    
    public NutritionFacts(int servingSize, int servings) {
        this(servingSize, servings, 0);
    }

    public NutritionFacts(int servingSize, int servings, int calories) {
        this(servingSize, servings, calories, 0);
    }

    public NutritionFacts(int servingSize, int servings, int calories, int fat) {
        this(servingSize, servings, calories, fat, 0);
    }

    public NutritionFacts(int servingSize, int servings, int calories, int fat, int sodium) {
        this(servingSize, servings, calories, fat, sodium, 0);
    }

    public NutritionFacts(int servingSize, int servings, int calories, int fat, int sodium, int carbohydrate) {
        this.servingSize = servingSize;
        this.servings = servings;
        this.calories = calories;
        this.fat = fat;
        this.sodium = sodium;
        this.carbohydrate = carbohydrate;
    }
}

// 사용예제
ex) NutritionFacts cocaCola = new NutritionFacts(240, 8, 100, 0, 35, 27);
~~~

## 자바 빈즈 패턴
가독성이 떨어지는 점층적 생성자 패턴을 보완하여 각 매개변수를 setter 메서드를 호출하여 값을 설정하는 방식이다. 하지만 자바빈즈 패턴도 문제점이 있다. 우선 불변객체로 만들 수 없고, 한 객체를 완성시키기 위해서는 여러 번 메서드를 호출해야한다. 이는 완전히 생성되기 전까지는 일관성을 보증해줄 수 없다. 만약 완전히 생성되지 않았는데 객체를 호출한다면 의도하지 않았던 유효성 없는 값이 나올 수도 있다.

이를 보완하기 위해서 freeze()를 만들어서 객체가 완전히 생성되기 전까지는 호출이 불가능하게 막고 완전히 생성된다면 freeze()를 통해서 불변객체로 만들어주고 호출 가능하게 해주는 방법이 있다. 하지만 이 방법은 다루기 어렵고 프로그래머가 freeze()를 확실히 호출해줬는지를 컴파일러가 보증해줄 수 없기 때문에 오류에 취약하다.

~~~java
public class NutritionFacts {

    // 기본값 초기화, 필수 = -1
    private int servingSize = -1;
    private int servings = -1;
    private int calories = 0;
    private int fat = 0;
    private int sodium = 0;
    private int carbohydrate = 0;

    public NutritionFacts() {};
    
    public void setServingSize(int servingSize) {
        this.servingSize = servingSize;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public void setSodium(int sodium) {
        this.sodium = sodium;
    }

    public void setCarbohydrate(int carbohydrate) {
        this.carbohydrate = carbohydrate;
    }
}

// 사용예제
NutritionFacts cocaCola = new NutritionFacts();
cocaCola.setServingSize(240);
cocaCola.setServings(8);
cocaCola.setCalories(100);
cocaCola.setCSodium(35);
cocaCola.setCarbohydrate(27);
~~~

## 빌더 패턴
빌더 패턴은 점층적 생성자 패턴의 안정성과 자바빈즈 패턴의 가독성을 겸비한 패턴이다. 클라이언트는 생성자(혹은 정적 팩터리 메서드)를 호출하여 인스턴스를 얻는 것이 아닌, class 내에 정의되어 있는 정적 맴버 클래스 Builder를 이용하여 인스턴스를 얻는다.

~~~java
public class NutritionFacts {

    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    private NutritionFacts(Builder builder) {
        this.servingSize = builder.servingSize;
        this.servings = builder.servings;
        this.calories = builder.calories;
        this.fat = builder.fat;
        this.sodium = builder.sodium;
        this.carbohydrate = builder.carbohydrate;
    };

    static class Builder {
        
        private final int servingSize;
        private final int servings;
        private int calories = 0;
        private int fat = 0;
        private int sodium = 0;
        private int carbohydrate = 0;
        
        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings = servings;
        }
        
        public Builder calories(int val) {
            calories = val;
            return this;
        }

        public Builder fat(int val) {
            fat = val;
            return this;
        }

        public Builder sodium(int val) {
            sodium = val;
            return this;
        }

        public Builder carbohydrate(int val) {
            carbohydrate = val;
            return this;
        }
        
        public NutritionFacts build() {
            return new NutritionFacts(this);
        }
    }
}


// 사용예제
new NutritionFacts.Builder(240, 8).calories(240).sodium(35).carbohydrate(27).build();

~~~

위의 코드와 같이 final로 불변성을 지킬 수 있으며, 빌더의 setter 메서드들은 빌더 자신을 반환하기 때문에 연쇄적으로 호출할 수 있다. (fluent API, method chaning)

또한 빌더 패턴은 계층적으로 설계된 클래스와 함께 쓰기에 좋다. 추상 클래스는 추상 빌더를, 구체 클래스는 구체 빌더를 갖게 한다. 빌더 패턴은 유연하게 필드를 다룰 수 있게 만들어준다. 하지만 빌더 패턴을 사용하면 객체를 만들려면 빌더부터 만들어야 한다. 빌더 생성 비용이 크지는 않지만 성능에 민감한 상황에서는 문제가 될 수 있다.

~~~java
public abstract class Pizza {
    public enum Topping { HAM, ONION, MUSHROOM }
    private final Set<Topping> toppings;

    Pizza(Builder<?> builder) {
        toppings = builder.toppings;
    }

    static abstract class Builder<T extends Builder<T>> {
        private EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);

        public T addTopping(Topping topping) {
            toppings.add(Objects.requireNonNull(topping));
            return self();
        }

        abstract protected T self();
        abstract Pizza build();
    }
}
public class NyPizza extends Pizza {
    public enum Size { small, middle, large }
    private final Size size;

    private NyPizza(PizzaBuilder builder) {
        super(builder);
        this.size = builder.size;
    }

    static class PizzaBuilder extends Builder<PizzaBuilder> {
        private final Size size;

        public PizzaBuilder(Size size) {
            this.size = Objects.requireNonNull(size);
        }

        @Override
        protected PizzaBuilder self() {
            return this;
        }

        @Override
        public NyPizza build() {
            return new NyPizza(this);
        }
    }
}
~~~


## 🙆🏻‍♀️  정리!
> 생성자나 정적 팩터리가 처리해야할 매개변수가 많다면 빌더 패턴을 선택하는 편이 더 좋다. 매개변수 중 다수가 필수가 아니거나, 같은 타입이라면 특히 더 그렇다. 빌더는 점층적 생성자보다 클라이언트 코드를 읽고 쓰기가 훨씬 간결하고 자바빈즈보다 훨씬 안전하다.

# 👼 Reference
- [Java 빌더 패턴 (Builder Pattern)이란? by Jan92](https://wildeveloperetrain.tistory.com/30)
- [[JAVA] 빌더 패턴(Builder Pattern)에 대해 알아보자 by demonic](https://lemontia.tistory.com/483)
- [[Java] 빌더 패턴(Builder Pattern)을 사용해야 하는 이유 by 망나니개발자](https://mangkyu.tistory.com/163)
- [Builder 기반으로 객체를 안전하게 생성하는 방법 by yun](https://cheese10yun.github.io/spring-builder-pattern/)
- [[Java] 빌더 패턴(Builder Pattern) by Gyun's 개발일지](https://devlog-wjdrbs96.tistory.com/207)
- [Builder Pattern(빌더 패턴 by Effective Java) by 웅이삼촌 개발블로그](https://velog.io/@hero6027/Builder-Pattern%EB%B9%8C%EB%8D%94-%ED%8C%A8%ED%84%B4-by-Effective-Java)
- [Java - Builder 패턴 적용하기. by 개발자로 홀로 서기](https://mommoo.tistory.com/54)
- [생성자에 매개변수가 많다면 빌더를 고려하라 by wooky9633](https://velog.io/@wooky9633/%EC%83%9D%EC%84%B1%EC%9E%90%EC%97%90-%EB%A7%A4%EA%B0%9C%EB%B3%80%EC%88%98%EA%B0%80-%EB%A7%8E%EB%8B%A4%EB%A9%B4-%EB%B9%8C%EB%8D%94%EB%A5%BC-%EA%B3%A0%EB%A0%A4%ED%95%98%EB%9D%BC)
<hr>
