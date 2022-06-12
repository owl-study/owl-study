# 아이템2. 생성자에 매개변수가 많다면 빌더를 고려하라


정적 팩터리와 생성자는 선택적 매개변수가 많을 때 적절히 대응하기 어렵다는 제약이 있다.  
그래서 선택적 매개변수가 많을 때 활용할 수 있는 방법이 있다.  
- 점층적 생성자 패턴(telescoping constructor pattern)
- 자바빈즈 패턴(JavaBeans pattern)
- 빌더 패턴(Builder pattern)

## 점층적 생성자 패턴(telescoping constructor pattern)

**필수 매개변수**만 받는 생성자,  
**필수 매개변수**와 **선택 매개변수 1개**를 받는 생성자,  
**필수 매개변수**와 **선택 매개변수 2개**를 받는 생성자......  
이런 식으로 선택 매개변수가 없는 생성자부터 선택 매개변수를 전부 다 받는 생성자까지 늘려가는 방식이다.

~~~JAVA
public class Member {

  // 필수 매개변수
  private String id;
  private String name;
  private String password;
  private int age;
  // 선택 매개변수
  private String gender;
  private String address;
  private String job;

  // 필요 매개변수만 받는 생성자
  public Member(String id, String name, String password, int age) {
    this(id, name, password, age, null, null, null);
  }

  public Member(String id, String name, String password, int age, String gender) {
    this(id, name, password, age, gender, null, null);
  }

  public Member(String id, String name, String password, int age, String gender,
      String address) {
    this(id, name, password, age, gender, address, null);
  }

  public Member(String id, String name, String password, int age, String gender,
      String address, String job) {
    this.id = id;
    this.name = name;
    this.password = password;
    this.age = age;
    this.gender = gender;
    this.address = address;
    this.job = job;
  }
}
~~~

### 점층적 생성자 패턴의 장점  
- 컴파일 안정성이 있다.  
  필수 매개변수가 여러개가 있을 시 하나라도 누락되면 컴파일 단계에서 에러검출을 해준다.
  
### 점층적 생성자 패턴의 단점
- 각 값의 의미파악이 어렵다.
- 매개변수가 많아지면 코드를 작성하거나, 읽기 어렵다
- 같은 타입의 매개변수가 연달아 들어가면 컴파일 에러가 발생하지 않기 때문에 런타임 에러가 발생할 수 있다.

---

<br>

## 자바빈즈 패턴(JavaBeans pattern)

매개변수가 **없는** 생성자로 객체를 만든 후, **세터(setter)** 메서드를 호출해 원하는 매개변수의 값을 설정하는 방식이다.

~~~JAVA
public class Member {


  private String id;
  private String name;
  private String password;

  private int age;
  private String gender;
  private String address;
  private String job;

  public void setId(String id) {this.id = id;}

  public void setName(String name) {this.name = name;}

  public void setPassword(String password) {this.password = password;}

  public void setAge(int age) {this.age = age;}

  public void setGender(String gender) {this.gender = gender;}

  public void setAddress(String address) {this.address = address;}

  public void setJob(String job) {this.job = job;}
}
~~~

### 자바빈즈 패턴의 장점
- 인스턴스를 만들기 쉽다.
- 코드가 읽기 쉬워진다.
### 자바빈즈 패턴의 단점
- 객체 하나를 생성하기 위해 메서드 여러개를 호출해야 한다.
- 클래스를 불변으로 만들 수 없다. (객체의 일관성이 무너진다)

---

<br>

## 빌더 패턴(Builder pattern)

점층적 생성자 패턴의 **안전성**과 자바빈즈 패턴의 **가독성**을 겸비한 패턴이다.  
클라이언트는 필요한 객체를 직접 만드는 대신, 필수 매개변수만으로 생성자를 호출해 빌더 객체를 얻는다.  
빌더 객체가 제공하는 일종의 세터 메서들로 원하는 선택 매개변수들을 설정한다.  
매개변수가 없는 build 메서드를 호출해 필요한 객체들을 얻는다.
> 빌더는 생성할 클래스 안에 정적 맴버 클래스로 만들어두는게 일반적이다.

~~~JAVA

public class Member {

  // 필수 매개변수
  private String id;
  private String name;
  private String password;
  private int age;
  // 선택 매개변수
  private String gender;
  private String address;
  private String job;

  public static class Builder{

    // 필수 매개변수
    private String id;
    private String name;
    private String password;
    private int age = 0;
    // 선택 매개변수 -> 기본값으로 초기화
    private String gender = "";
    private String address = "";
    private String job = "";

    // 필수 매개변수만 받는 생성자
    public Builder(String id, String name, String password, int age) {
      this.id = id;
      this.name = name;
      this.password = password;
      this.age = age;
    }

    public Builder gender(String gender){
      this.gender = gender;
      return this;
    }

    public Builder address(String address){
      this.address = address;
      return this;
    }

    public Builder job(String job){
      this.job = job;
      return this;
    }

    public Member build(){
      return new Member(this);
    }
  }

  private Member(Builder builder){
    id = builder.id;
    name = builder.name;
    password = builder.password;
    age = builder.age;
    gender = builder.gender;
    address = builder.address;
    job = builder.job;
  }

  // 값 출력을 위한 toString 오버라이드
  @Override
  public String toString() {
    return "Member{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", password='" + password + '\'' +
        ", age=" + age +
        ", gender='" + gender + '\'' +
        ", address='" + address + '\'' +
        ", job='" + job + '\'' +
        '}';
  }
}

~~~

실행문

~~~JAVA

public class MainBuilder {

  public static void main(String[] args) {

    // 필수 매개변수만 받아 인스턴스 생성하기
    Member jung = new Member.Builder("test1234", "정재원", "test5678", 20)
        .build();

    System.out.println(jung.toString());

    // 선택 매개변수까지 받아 인스턴스 생성하기
    Member lee = new Member.Builder("addmore1234", "lee", "add1234", 34)
        .gender("male")
        .address("서울특별시 관악구 난우길 4")
        .job("개발자")
        .build();

    System.out.println(lee.toString());

  }
}
~~~

결과
~~~
Member{id='test1234', name='정재원', password='test5678', age=20, gender='', address='', job=''}
Member{id='addmore1234', name='lee', password='add1234', age=34, gender='male', address='서울특별시 관악구 난우길 4', job='개발자'}
~~~

<BR>

또한 Lombok 을 이용하면 쉽게 Builder패턴을 이용할 수 있다.

~~~JAVA
import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class LombokMember {

    // 필수 매개변수
    private String id;
    private String name;
    private String password;
    private int age;
    // 선택 매개변수
    private String gender;
    private String address;
    private String job;

}
~~~

실행문

~~~JAVA

public class MainBuilder {

  public static void main(String[] args) {

    // 롬복 빌더 어노테이션을 이용해 인스턴스 생성하기
    LombokMember kim = LombokMember.builder()
        .id("kimMaeMi")
        .name("매미킴")
        .password("kmm1234")
        .age(17)
        .gender("male")
        .address("서울역")
        .job("개그맨")
        .build();

    System.out.println(kim.toString());
  }
}
~~~

결과

~~~
LombokMember{id='kimMaeMi', name='매미킴', password='kmm1234', age=17, gender='male', address='서울역', job='개그맨'}
~~~ 

### 빌더패턴의 장점
- 각 파라미터가 어떤 값이 들어가는지 한눈에 보인다.
- 객체의 일관성을 부여할 수 있다.
- 자바빈즈 패턴보다 훨씬 안전하다.
- 계층적으로 설계된 클래스와 함께 쓰기 좋다.
- 하위 클래스의 메서드가 상위 클래스의 메서드가 정의한 반환 타입이 아닌, 그 하위 타입을 반환할 수 있어서 클라이언트가 형변환에 신경쓰지 않고 빌더를 사용 가능하다. (공변 반환 타이핑)
### 빌더패턴의 단점
- 객체를 만들려면 빌더부터 만들어 줘야 한다.
- 코드가 장황해서 매개변수가 4개 이상이어야 값어치를 한다.
- Lombok의 @Builder 어노테이션을 이용하면 필수 매개변수 여러개를 설정할 수 없다.
- 필수 매개변수를 다 설정해 주지 않아도, 컴파일 단계에서 에러검출이 안된다.
