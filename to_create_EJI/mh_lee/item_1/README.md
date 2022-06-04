# 아이템1. 생성자 대신 정적 팩터리 메서드를 고려하라

## 정적 팩터리 메서드란?
Java 에서의 클래스의 인스턴스화, 즉 객체를 생성할 때 일반적인 **public 생성자**를 이용하는 것이 아니라, 정적(static) 메서드로 하는 것을 **정적 팩터리 메서드**라고 한다.

<br>

## 정적 팩터리 메서드가 생성자보다 좋은 점 (장점)
### 1) 이름을 가질 수 있다.

생성자는 항상 클래스와 이름이 같아야 한다.  
하지만 생성자에 넘기는 매개변수만으로는 반환되는 객체의 특성이 뭔지 파악하기 **힘들다**.  
하지만 **정적 팩터리 메서드**는 반환되는 객체의 특성을 **명시적**으로 나타낼 수 있다.  
<br>

<span style="font-size:110%">생성자 방식</span>

~~~JAVA
public class Seoul {
    private String name;

    // public 생성자를 활용해 객체를 생성하는 방법.
    public Seoul(String name){
        this.name = name;
    }
}
~~~
~~~JAVA
public class MainMethod {
    public static void main(String[] args) {

        // 일반 생성자를 활용하면 박태환과 서울에서 뭘 하는지 알 수 없다.
        Seoul seoul = new Seoul("박태환");
    }
}
~~~
> 이렇게 생성자에 넘기는 매개변수만으로는 서울에서 박태환과 뭘 하는지 파악하기 힘들다.

<br>

<span style="font-size:110%">정적 팩터리 메서드 방식</span>

~~~JAVA
public class StaticSeoul {
    private String name;

    // private 생성자
    private StaticSeoul(String name){
        this.name = name;
    }

    // 정적 팩터리 메서드를 사용하면 어떤 역활을 하는지 명시적인 표현이 가능하다
    public static StaticSeoul hangangDiveWithNameFrom(String name) {
        return new StaticSeoul(name);
    }
}
~~~
~~~JAVA
public class MainStaticMethod {
    public static void main(String[] args) {

        // 정적 팩터리 메서드를 활용하면
        // 박태환과 함께 한강간다는 것을 명시적으로 표현 가능하다.
        StaticSeoul staticSeoul = StaticSeoul.hangangDiveWithNameFrom("박태환");
    }
}
~~~
> 하지만 정적 팩터리 메서드를 이용하면 박태환과 함께 한강 다이브를 한다는 것을  
> 명시적으로 표현이 가능하다.

<br>

또, 같은 시그니처를 가지는 생성자 여러 개가 필요할 때 **정적 팩터리 메서드**를 이용할 수 있다.

<span style="font-size:110%">생성자 방식</span>

~~~JAVA
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
~~~
> 일반적인 생성자 방식으로는 매개변수의 이름만 다른 두 개의 생성자를 만들 수 없다.

<br>

<span style="font-size:110%">정적 팩터리 메서드 방식</span>

~~~JAVA
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
~~~
> 하지만 정적 팩터리 메서드로 바꿔준다면 같은 시그니처를 가지는 생성자 여러개를 만들 수 있다.

<br>

### 2) 호출될 때마다 인스턴스를 새로 생성하지 않을 수 있다.

보통 **싱글턴** 방식에서 많이 사용하는 방식이다.  
인스턴스를 미리 만들어 놓거나 새로 생성한 인스턴스를 캐싱하여서 재활용 하는 식으로,  
불필요한 객체 생성을 피해 속도를 높힐 수 있다.  
따라서 생성 비용이 큰 객체가 자주 요청되는 상황이면 성능을 상당히 끌어올려 준다.  

<br>

~~~JAVA
public class Singleton {
    // 외부에 제공할 자기 자신의 인스턴스.
    private static Singleton instance = null;
    private Singleton() { }

    public static Singleton getInstance(){
        // 만약 instance 가 null 이면 instance 생성
        if(instance == null) instance = new Singleton();

        return Singleton.instance;
    }
}
~~~
~~~JAVA
public class Main {
    public static void main(String[] args) {

        Singleton singleton1 = Singleton.getInstance();
        Singleton singleton2 = Singleton.getInstance();
        // 둘이 주소값이 같나 확인. 둘 다 같은 인스턴스 주소값을 반환받으니 true
        System.out.println(singleton1 == singleton2);
    }
}
~~~
> singleton1 과 singleton2 모두 이미 생성된 객체를 반환 받으므로, 불필요한 객체의 생성을 피한것을 볼 수 있다.

<br>

### 3) 반환 타입의 하위 타입의 객체를 반환할 수 있는 능력이 있다.

반환 타입으로는 **상위 타입**임을 드러내고, 내부적으로 구체적인 **하위 타입**을 결정 할 수 있다.  
이 방법을 사용하면 구현 클래스를 **공개하지 않고**, 원하는 객체를 **반환**하게 할 수 있어 API를 **작게 유지** 할 수 있다.  
JAVA 8 부터는 **인터페이스**에서도 **정적 메서드** 선언이 가능해 졌으므로, 인터페이스에서도 간단하게 이 방법을 구현할 수 있다.

<br>

상위 타입
~~~JAVA
public interface School {
    // 학교 소개
    void print();
    // School 을 상속받는 하위타입 객체 리턴
    static School introduceSchool(String name){
        return new HangangSchool(name);
    }
}
~~~
하위 타입
~~~JAVA
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
~~~
실행문
~~~JAVA
public class MainReturnLow {
    public static void main(String[] args) {
        // 매개변수 한강을 넣어주면 하위타입 객체를 반환시켜 준다.
        School hangang = School.introduceSchool("한강");
        // 학교 소개 출력.
        hangang.print();
    }
}
~~~
결과문
~~~
한강초등학교입니다.
~~~

<br>

### 4) 매개변수에 따라 다른 클래스의 객체를 반환할 수 있다.
  
매개변수에 따라 원하는 클래스 객체를 반환 할 수 있으므로, 상황에 따라 효율적인 객체를 반환 할 수 있다.
  
~~~JAVA
public class Human {

    public static Human doSomething(String behavior){
        if(behavior.equals("walk")) return new Walk();
        else if(behavior.equals("run")) return new Run();
        else if(behavior.equals("sleep")) return new Sleep();
        else return new Stand();
    }
}

class Walk extends Human {
}

class Run extends Human {
}

class Sleep extends Human {
}

class Stand extends Human {
}
~~~
실행문
~~~JAVA
public class MainHuman {
    public static void main(String[] args) {
        // walk run sleep 이외의 매개변수를 넣어주면 stand 객체를 반환.
        Human human = Human.doSomething("walk");
        // human 에 반환된 객체의 위치 및 이름이 뜬다.
        System.out.println(human.getClass().getName());
    }
}
~~~
결과문
~~~
effectiveJava.item1.returnParameter.Walk
~~~

<br>

### 5) 정적 팩터리 메서드 작성 시점에 반환할 객체의 클래스가 없어도 된다.

한마디로 클라이언트를 구현체로부터 분리해준다고 말할 수 있다.  
이게 무슨소리냐 하면 JDBC의 서비스 접근 API인 DriverManager.getConnection()을 예로 들을 수 있다.  
우리가 일반적으로 JDBC에서 Connection 을 가져오는 코드는 다음과 같다.  
  
~~~JAVA
// JDBC 1단계 : 드라이버 객체 로딩			
DriverManager.registerDriver(new org.h2.Driver());
// JDBC 2단계 : 커넥션 연결
conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa", "");
~~~

여기 2단계인 getConnection() 에서 반환되는 구현체는 DBMS 의 종류에 따라 달라진다.  
클라이언트는 세부적인 구현 내용을 몰라도 서비스를 이용 할 수 있다.  
Connection 타입은 인터페이스이며, 실제로 구현하는 클래스가 존재하지 않아도 되고, 구현하는 클래스가 계속 추가될 수도 있다.

---

<br>

## 정적 팩터리 메서드의 단점

### 상속을 하려면 public 이나 protected 생성자가 필요하기 때문에 정적 팩터리 메서드만 제공하면 하위 클래스를 만들 수 없다.


<br>
  
### 정적 팩터리 메서드는 프로그래머가 찾기 어렵다.
정적 팩터리 메서드는 생성자처럼 API 설명에 명확히 드러나지 않아서 찾기 어렵다.  
따라서 메서드의 이름을 널리 알려진 규약에 따라 지음으로써 개발자들이 쉽게 찾을 수 있도록 만들어야 한다.

---

<br>

## 메서드 명명 방식
- `from` : 매개변수 하나를 받아서 해당 타입의 인스턴스를 반환하는 형 변환 메서드
~~~JAVA
Date date = Date.from(instance);
~~~
- `of` : 여러 매개변수를 받아서 적합한 타입의 인스턴스를 반환하는 집계 메서드
~~~JAVA
List list = List.of(1, 2, 3);
~~~
- `valueOf` : from과 of의 비슷한 의미를 갖는다.
~~~JAVA
Integer i = Integer.valueOf(10);
~~~
- `instance` 또는 `getInstance` :  (매개변수를 받는다면) 매개변수로 명시한 인스턴스를 반환하지만, 같은 인스턴스임을 보장하지 않는다.
~~~JAVA
Human human = Human.getInstance("work");
~~~
- `create` 또는 `newInstance` : (매개변수를 받는다면) 매개변수로 명시한 인스턴스를 반환한다. 이 때, 매번 새로운 인스턴스를 생성해 반환함을 보장한다.
~~~JAVA
Object newArray = Array.newInstance(classObject, arrayLen);
~~~
- `getType` : 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 쓴다. (네이밍에서의 `Type`은 팩터리 메서드가 반환할 객체의 타입 이름을 뜻한다.)
~~~JAVA
FileStore fs = Files.getFileStore(path);
~~~
- `newType` : 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 쓴다. 이 때, 매번 새로운 인스턴스를 생성해 반환함을 보장한다. (네이밍에서의 `Type`은 팩터리 메서드가 반환할 객체의 타입 이름을 뜻한다.)
~~~JAVA
BufferedReader br = Files.newBufferedReader(path);
~~~
- `type` : `getType`, `newType`의 간결한 버전이다. 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 쓴다. (네이밍에서의 type은 팩터리 메서드가 반환할 객체의 타입 이름을 뜻한다.)
~~~JAVA
List<Complaint> litany = Collection.list(legacyLitancy);
~~~
