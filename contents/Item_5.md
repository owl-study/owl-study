# 아이템5. 자원을 직접 명시하지말고 의존 객체 주입을 사용하라.

## 🙆🏻‍♀️ 핵심 정리
> 1. 클래스가 내부적으로 다수의 자원에 의존하고, 그 자원이 클래스 동작에 영향을 준다면 자원을 직접 명시하지 말고 의존성 주입을 사용하라
> 2. 클래스가 자원을 직접 만들면 유용성, 재사용성, 테스트 용이성이 떨어진다. 
> 3. 필요한 자원을 생성자에 넘기는 의존 객체 주입을 사용하면 클래스의 유연성, 재사용성, 테스트 용이성이 개선된다.

## 🤔 의존 객체 주입이란?
의존성 주입이란 클래스간 의존성을 클래스 외부에서 주입하는 것을 뜻한다. 그리고 의존성이 있다는 것은 클래스간에 의존 관계가 있다는 것을 뜻한다. 즉, 한 클래스가 바뀔 때 다른 클래스가 영향을 받는다는 것이다. 이런 의존성을 클래스 외부에서 주입한다는 것은 객체간의 상호 결합을 낮춰서 더욱 객체지향적이고 유연한 확장성을 가진 코드를 만들 수 있도록 한다.
의존 객체 주입의 한 형태로는 인스턴스를 생성할 때 생성자에 필요한 자원을 넘겨주는 방식이 있다.
아래 예시를 통해 자세히 알아보자.

## 👎 자원을 직접 명시한 예시
### (1) 정적 유틸리티를 잘못 사용한 예
~~~java
public class SpellCheckerStaticUtility {
    private static final Lexicon dictionary = new KorLexicon();

    private SpellCheckerStaticUtility() {}

    public static boolean isValid(String word) {
        return dictionary.getWords().stream()
                .anyMatch(w -> w.equals(word));
    }
}
~~~

### (2) 싱글턴을 잘못 사용한 예
~~~java
public class SpellCheckerSingleton {
    private final Lexicon dictionary = new KorLexicon();

    private SpellCheckerSingleton() {}

    private static SpellCheckerSingleton INSTANCE = new SpellCheckerSingleton();

    public boolean isValid(String word) {
        return dictionary.getWords().stream()
                .anyMatch(w -> w.equals(word));
    }
}
~~~

위 에시는 사전에 의존하는 맞춤법 검사기에 대한 코드이다. 2가지 방식 모두 사전을 단 하나만 사용한다고 가정한 점에서 아쉬운 코드이다. 일반적으로는 사전이 언어별로 혹은 특수 어휘나 테스트 등의 용도에 따라 다양한 사전을 참고할 수 있기 때문이다. 때문에 사용하는 자원에 따라 동작이 달라지는 클래스에는 정적 유틸리티 클래스나 싱글턴 방식은 적합하지 않다.
클래스가 자원을 직접 만들면 코드가 유연하지 않고 확장성이 떨어지며 테스트가 용이하지 않기 때문에 인스턴스를 생성할 때 생성자에 필요한 자원을 넘겨주는 의존 객체 주입을 사용하는 것이 좋다.


## 👍 의존 객체 주입을 사용한 예시

~~~java
public class SpellChecker {
    private final Lexicon dictionary;

	// 인스턴스를 생성할 때 생성자에 필요한 자원을 넘겨받는다.
    public SpellChecker(Lexicon dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary);
    }

    public boolean isValid(String word) {
        return dictionary.getWords().stream()
                .anyMatch(w -> w.equals(word));
    }
}
~~~

예시는 `dictionary`라는 단 하나의 자원만 사용하지만, 자원이 몇개든 의존 관계가 어떻든 상관 없이 잘 작동한다. 또한 같은 자원을 사용하려는 여러 클라이언트가 의존 객체들을 안심하고 공유할 수 있기도 한다. 이런 구조라면 SpellChecker 클래스를 수정하지 않고 여러 Dictionary 객체를 변경할 수 있게 된다. 즉, 한 클래스가 변경되더라도 다른 클래스가 영향을 받지 않게 되므로 의존 객체 주입은 코드의 유연성과 확장성, 테스트의 용이성을 높여준 것이다. 


## 👼🏻 Reference

- [의존성 주입이란 무엇이며 왜 필요한가? By Dev.cho](https://kotlinworld.com/64#%EC%A-%BC%EC%-E%--%EC%-D%B-%EB%-E%--%--%EB%AC%B-%EC%--%--%EC%-D%B-%EB%A-%B-%--%EC%--%-C%--%ED%--%--%EC%-A%--%ED%--%A-%EA%B-%-C%-F)
- [자원을 직접 명시하지 말고 의존 객체 주입을 사용하라 By mini](https://blog.riyenas.dev/effective_java_reusing_object/)
