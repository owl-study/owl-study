# 아이템9. try-finally보다는 try-with-resources를 사용하라

## 🙆🏻‍♀ 핵심 정리
> 꼭 회수해야 하는 자원을 사용하는 경우에 try-finally대신 try-with-resources를 사용하면 좋은 점
> 1. 보다 더 짧고 간결한 코드.
> 2. 보다 더 확실한 자원회수.
> 3. 보다 더 자세한 예외정보 파악.
> 4. 예외 없음

<br>


## try-finally는 최선이 아니다
자바 라이브러리에는 close 메서드를 호출해 직접 닫아줘야 하는 자원이 많다.
전통적으로 try-finally를 사용하여 자원의 회수를 보장해 왔지만 몇 가지 문제점들이 있고, 이 보다 더 좋은 방법이 있다.
<br>

### 👎 자원이 둘 이상이라면 try-finally 방식은 너무 지저분하다! 

~~~ java
static void copy(String src, String dst) throws IOException {
    InputStream in = new FileInputStream(src);
    try {
        OutputStream out = new FileOutputStream(dst);
        try {
            byte[] buf = new byte[BUFFER_SIZE];
            int n;
            while ((n = in.read(buf)) >= 0)
                out.write(buf, 0, n);
        } finally {
            out.close();
        }
    } finally {
        in.close();
    }
}
~~~  
[try-with-resources로 개선된 코드 보기]()
<br>

### ⚠ 예상치 못한 예외(에러?)를 놓칠 수 있다!

~~~ java
public static String firstLineOfFile(String path) throw IOException {
    BufferedReader br = new BufferedReader(new FileReader(path));
    try {
        return br.readLine();
    } finally {
        br.close();
    }
}
~~~
[try-with-resources로 개선된 코드 보기]()

이 메소드는 반환해야 하는 자원이 한 개라 지저분하지 않고 괜찮은 코드처럼 보이지만
결점을 가지고 있다. 기기의 물리적인 문제로 readLine 메소드가 제대로 실행되지 않는 경우, 처음 발생한 예외(기기의 물리적인 문제로 발생한 예외)를 두 번째 예외(readLine 메서드가 발생한 예외)가 덮어서 확인 할 수 없게 된다.<br>
그렇다고 첫 번째 예외를 확인 할 수 있도록 개선하면 코드가 너무 지저분해 진다.

<br>

## try-with-resources가 최선의 자원회수 방법이다.
자바 7에서 처음 등장한 방법으로 사용하기 위해선 [AutoCloseable](https://github.com/leejk0924/owl-study/blob/main/contents/Item_9.md#1-autocloseable) 인터페이스를 구현해야한다.
회수해야 하는 자원을 사용하는 클래스나 인터페이스를 만들어야 한다면 **무조건 AutoCloseable을 구현하고, try-with-resources를 이용하자.**

### 앞의 코드들을 try-with-resources를 이용한 코드로 수정해 보자.

####  👍 반환할 자원이 두개라 복잡했던 코드 --------- [try-finally 코드 보기](https://github.com/leejk0924/owl-study/blob/main/contents/Item_9.md#-%EC%9E%90%EC%9B%90%EC%9D%B4-%EB%91%98-%EC%9D%B4%EC%83%81%EC%9D%B4%EB%9D%BC%EB%A9%B4-try-finally-%EB%B0%A9%EC%8B%9D%EC%9D%80-%EB%84%88%EB%AC%B4-%EC%A7%80%EC%A0%80%EB%B6%84%ED%95%98%EB%8B%A4)
~~~ java
static void copy(String src, String dst) throws IOException {
    try (  InputStream in = new FileInputStream(src);
         OutputStream out = new FileOutputStream(dst)) {
        byte[] buf = new byte[BUFFER_SIZE];
        int n;
        while ( (n = in.read(buf)) >= 0 )
            out.write(buf, 0, n);
}
~~~

####  👍 예상치 못한 예외를 놓칠 수 있던 코드 ------- [try-finally 코드 보기]()

~~~ java
public static String firstLineOfFile(String path) throw IOException {
    try (BufferedReader br = new BufferedReader(
            new FileReader(path))) {
        return br.readLine();
}
~~~

try-with-resources를 이용한 코드가 더 간결하고 문제를 파악하기 쉽다.

firstLineOfFile 메소드에서 readLine과 코드에는 보이지 않는 close 모두에서 예외가 발생한다면
스택 추적 내역에 readLine에서 발생한 예외가 먼저 표시된다. 그리고 close에서 발생한 예외는 숨겨졌다는 꼬리표인 suppressed를 달고 그 이후에 같이 출력된다.
<br>try-finally와 달리 첫 번째 예외부터 확인할 수 있다.

또, 아래처럼 java 7에서 Throwble에 추가된 getSuppressed 메서드를 이용하면 프로그램 코드에서 가져올 수도 있다.

~~~ java
try {
    testException1();
} catch (Throwable e) {
    Throwable[] suppExe = e.getSuppressed();

    for (int i = 0; i < suppExe.length; i++) {
        System.out.println("Suppressed Exceptions:");
        System.out.println(suppExe[i]);
    }
}
~~~





### try-with-resources에서 catch 사용하기
try-finally처럼 try-with-resources에서도 catch 절을 쓸 수 있다.
<br>catch 절 덕분에 try 문을 더 중첩하지 않고도 다수의 예외를 처리할 수 있다.

~~~ java
static String firstLineOfFile (String path, String defaultVal){
    try (BufferedReader br = new BufferedReader(
            new FileReader(path))) {
        return br.readLine();
    } catch (IOException e) {
        return defaultVal;
    }
}
~~~
<br>

# 👼🏻 천사(참고자료) <br>
- [찐 코딩노예 - [이펙티브 자바 item 9] try-finally보다는 try-with-resources를 사용하라](https://jithub.tistory.com/317)
- [lychee.log - [이펙티브 자바 item 9] try-finally보다는 try-with-resources를 사용하라](https://velog.io/@lychee/%EC%9D%B4%ED%8E%99%ED%8B%B0%EB%B8%8C-%EC%9E%90%EB%B0%94-%EC%95%84%EC%9D%B4%ED%85%9C-9.-try-finally-%EB%8C%80%EC%8B%A0-try-with-resources%EB%A5%BC-%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC)
---

###### [[1]]() AutoCloseable
단순히 void를 반환하는 close 메서드 하나만 덩그러니 정의한 인터페이스.
이미 수 많은 자바와 서드파티 라이브러리의 클래스와 인터페이스들이 **AutoCloseable**을 구현하거나 확장해 두었다.
