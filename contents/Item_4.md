# 아이템4. 인스턴스화를 막으려거든 private 생성자를 사용하라

------------------------
이 아이템에서 주요 내용
1. 기본생성자
2. 상속받으면 상위클래스가 호출됨
3. AssertionError() 의 쓰임
------------------------



막는 방법 1 - 추상클래스 사용

막는 방법 2 - private 생성자 사용

막는 방법 3 - 그냥 문서화



## ❓ 정적 메서드 & 필드로만 구성된 클래스
정적 메서드 & 정적 필드는 코드를 실행하기 전에 클래스의 인스턴스를 생성하지 않고 사용하는데 유용하다.

![JVM 메모리 구조](https://goldenrabbit.co.kr/wp-content/uploads/2021/11/%E1%84%8C%E1%85%A1%E1%84%87%E1%85%A1-%E1%84%86%E1%85%A6%E1%84%86%E1%85%A9%E1%84%85%E1%85%B5-%E1%84%86%E1%85%A9%E1%84%83%E1%85%A6%E1%86%AF_02.png)

이는 클래스가 메모리에 올라갈 때, 정적 메서드가 Method area내 static 영역에 생성된다. 때문에 인스턴스를 생성하지 않고, 클래스명으로 정적 메서드를 호출 할 수 있다.

## ❓ 정적 유틸리티 클래스의 설계 의도
정적 메서드(또는 필드)만 담은 클래스는 인스턴스를 생성하여 사용하려고 설계한 것이 아니다. 하지만 매개변수를 받지않는 기본 생성자가 만들어지며, 클래스의 사용자는 해당 생성자가 어떤 의도로 생성된지 모르는 상태로 인스턴스화 할 수 있다. 그렇기 때문에 작성자의 의도대로 사용자가 사용할 수 있도록 객체의 인스턴스화를 막을 방법이 필요하다.

#### (1) 추상클래스로 인스턴스화 막기
추상 클래스에 정적 메서드 & 정적 필드를 담으면 불필요한 인스턴스화를 막을 수 있다. 스프링 프레임 워크에서 이 방법을 주로 사용하고 있다.
(예시: [AnnotationConfigUtils 클래스](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/annotation/AnnotationConfigUtils.html))



### 🤓 예시 코드
```java
public abstract class Test {
    static void hello() {
        System.out.println("hello");
    }
}
```
하지만, 완전히 인스턴스화를 막을 수는 없다. 추상 클래스를 상속받는 하위클래스를 만들어 인스턴스화 할 수 있다. 사용자 입장에서는 해당 클래스를 볼 경우 상속해서 사용하는 뜻으로 오해할 수 있다. 

#### (2) private 생성자
컴파일러가 기본생성자를 만드는 경우는 명시된 생성자가 없을 경우이기 때문에 private 생성자를 만들면 인스턴스화를 막을 수 있다. 하지만, 접근 제어자가 private이니 클래스 안에서 실수로 생성자를 호출할 수도 있기 때문에 AssertionError()를 작성한다.
또한 접근제어가자 private으로 선언 했기 때문에 하위 클래스가 상위 클래스의 생성자에 접근을 막기도 한다.
```java
public abstract class Test {
    static void hello() {
        System.out.println("hello");
    }
    private Test() {
        throw new AssertionError();
    }
}
```
#### (3) 문서화
가장 좋은 방법이며 가장 쉬운 방법은 문서화 통하여 사용자가 올바르게 사용하도록 인도하는 것이다. 

## 🙆‍♀️ 아이템4의 주요 내용!
1. 기본생성자
2. 상속받을 경우 상위클래스를 호출
3. AssertionError()

 # 👼 Reference
 - [아이템 4. 인스턴스화를 막으려거든 private 생성자를 사용하라 by 림딩동](https://limdingdong.tistory.com/19)
 - [정적 메소드는 언제 써야하는가? by 마이구미](https://mygumi.tistory.com/253)
 
 - [자바 코드와 메서드, 스태틱 변수 등은 메모리의 어디에 위치할까?](https://goldenrabbit.co.kr/2021/11/03/%EC%9E%90%EB%B0%94-%EC%BD%94%EB%93%9C%EC%99%80-%EB%A9%94%EC%84%9C%EB%93%9C-%EC%8A%A4%ED%83%9C%ED%8B%B1-%EB%B3%80%EC%88%98-%EB%93%B1%EC%9D%80-%EB%A9%94%EB%AA%A8%EB%A6%AC%EC%9D%98-%EC%96%B4%EB%94%94/)
 
 - [[Java] 메모리 구조 메소드(Method), 스택(Stack), 힙(Heap) 영역에 대하여 by 코딩팩토리](https://coding-factory.tistory.com/830)

 - [클래스 AssertionError](http://cris.joongbu.ac.kr/course/java/api/java/lang/AssertionError.html)
 

---
