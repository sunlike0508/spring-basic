# 제어의 역전

Appconfig가 나오기 전에는 orderserviceimpl이 직접 memberservieimpl을 선택하고 discountpolicy도 정율인자 정액인지 선택해서 구체화한다.

그러나 Appconfig에서 이거를 다 결정한다.

즉, orderserviceimpl은 구현객체를 생성하고 연결할 필요가 없다. 제어의 주도권을 외부에 넘겨줬다.

```
* 여기서 프레임워크와 라이브러리 차이

제어권의 담당주체가 누구냐?

내가 작성한 코드를 제어하고 대신 실행하면 프레임워크(ex : Junit)

내가 작성한 코드가 직접 제어의 흐름을 담당하면 라이브러리
```

자, 그래서 정리하면 Appconfig처럼 객체를 생성하고 관리하면서 의존관계를 연결해 주는 것을
DI 컨테이너 or IoC 컨테이너라고 한다. (팩토리 메서드 패턴)

(요즘은 DI 컨테이너라고 한다. 혹은 어셈블러, 오브젝트 팩토리라고 한다.)

---

# 스프링 컨테이너 생성

```
ApplicationContext applicationContext = new AnnotationConfigApplicationContext.(SpringAppConfig.class);
```

ApplicationContext를 스프링 컨테이너라고 한다.

ApplicationContext는 인터페이스이다.

정확히는 BeanFactory인데 이거를 직접 사용하는 경우는 없다.

ApplicationContext가 BeanFactory를 implement하기 때문이다.

스프링 컨테이너는 bean 저장소를 가지고 있다.

어플리케이션이 실행되면 @Bean이 붙은 메서드를 스프링 저장소에 Bean으로 등록한다.

Bean의 이름은 중복되면 안된다.

(bean 이름은 보통 메서드 이름을 등록한다. 그러나 따로 이름을 지정할 수 있다.@Bean 옵션 기능)

모든 Bean이 등록되면 스프링 컨테이너는 설정정보는 참고해서 의존 관계를 주입(DI)한다.

단순히 자바 코드를 호출하는 것 같지만 아니다. 싱글톤 컨테이너의 개념이 여기서 나온다.

그런데 SpringAppConfig처럼 자바코드로 작성하면 빈 생성(등록)과 주입이 한꺼번에 이루어진다.

실제 스프링의 빈 라이프사이클은 생성과 주입이 나누어져 있다.

#### BeanFactory

스프링 컨테이너의 최상위 인터페이스이다.

스프링 빈을 관리하고 조회하는 역할을 담당

getBean() 제공

지금까지 우리가 사용했던 대부분의 기능은 이놈임

#### ApplicationContext

BeanFactory를 상속 받은 것

기본적인 기능말고 그 외 다른 기능들을 한꺼번에 상속

이벤트 발행(이벤트 발행, 구독), 메시지 소스(언어별 출력),
환경설정(로컬, 개발, 운영), 리소스 (파일, 클래스패스, 외부 리소스)


---

# 스프링 Bean 조회

* test/beanfind 경로 참조

부모 타입을 조회하면 자식 타입도 함께 조회된다.

따라서 자바 객체의 최고 부모인 Object 타입으로 조회하면 모든 스프링 빈을 조회 할 수 있다.

---

# 스프링 설정 방법

* test/beanDefinition 참조

자바 코드, XML, Groovy 등등이 있다. 방법은 여러가지나 실행단계는 같다.

스프링 컨테이너는 설정한 내용을 읽어서 BeanDefinition을 생성한다.

ex) `AnnotationConfigApplicationContext` 는 `AnnotatedBeanDefinitionReader` 를 사용해서 `AppConfig.class` 를
읽고 `BeanDefinition` 을 생성한다.

BeanDefinition을 직접 생성해서 스프링 컨테이너에 등록할 수 있다. (실무에서는 뭐...그닥)

스프링이 다양한 형태의 설정 정보를 BeanDefinition으로 추상화해서 사용한다.

가끔 스프링 코드나 오픈소스에서 BeanDefinition를 볼 수 있다.

---

# 웹 애플리케이션과 싱글톤

스프링이 아닌 순수 자바로 만든 DI 컨테이너는 애플리케이션이 구동될때 등록한 빈을 가지고 있다가 클라이언트가 요청할 때 마다 등록된 빈을 새로운 객체를 생성하여 전달한다.

그러면 운영에서 초당 10만개의 요청이 있다면 10만개의 객체가 생성. 운영에서는 수많은 bean이 있을 텐데 엄청난 객체를 생성해야 한다.

그렇다면 객체를 하나만 생성하고 이거를 공유하면 어떨까?

### 싱글톤 패턴

* test/singleton

클래스의 인스턴스가 딱 1개만 생성되는 것을 보장하는 디자인 패턴

객체 인스턴스가 2개 이상 생성되지 않게 막으면 된다.

**그래서 스프링 컨테이너는 객체를 미리 생성해서 저장해두고 고객(클라이언트)의 요청이 들어올때 마다 객체를 공유해서 효율적으로 사용.**

##### 문제점

구현 코드가 많다.

구체 클래스에 의존 (DIP 위반, interface를 통해 이용하는 것이 아니기 때문)

위에 이유로 자동스럽게 OCP도 위반

테스트가 힘들다. (객체를 미리 생성하는 것이기 때문에 내가 조절하기 힘듬

private 생성자로 자식 객체를 만들기 어려움.

결론적으로 유연성이 떨어짐. -> 안티패턴

**그러나 !!! 스프링은 위의 문제점을 모두 해결해준다.**

--- 

# 싱글톤 컨테이너

* test/singleton 참조

스프링 컨테이너는 빈 객체를 싱글톤 패턴을 적용하지 않지만 싱글콘으로 관리한다.

1) 싱글톤 패턴의 지저분한 코드 들어가지 않음
2) DIP , OCP 테스트, private 생성자로부터 자유롭게 싱글톤을 사용할 수 있다.

스프링의 기본 빈 등록 방식은 싱글콘이지만, 요청할 때마다 새로운 객체를 생성해서 반환하는 기능도 있다. (빈스코프)

### 주의점

* test/singleton 참조

싱글톤은 객체 인스턴스를 하나만 생성하서 공유하기 때문에 상태를 유지(stateful)를 하면 안된다.

즉, 무상태(stateless)로 설계해야 한다.

1) 특정 클라이언트의 의존적인 필드 X
2) 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 X
3) 가급적 읽기 기능만
4) 필드 대신 자바에서 공유되지 않는 지역변수, 파라미터, treadLocal만 사용
5) 스프링 빈의 필드에 공유값을 절대 설정하면 안된다.

### @Configuration과 싱글톤

```
@Bean 
public MemberService memberService() {
    return new MemberServiceImpl(memberRepository());
}


@Bean 
public OrderService orderService() {
    return new OrderServiceImpl(memberRepository(),discountPolicy());
}


@Bean 
public MemoryMemberRepository memberRepository() {
    return new MemoryMemberRepository();
}
```

위에 코드에서 memberService를 bean 등록할 때 memberRepository() 메소드를 호출

orderService를 bean 등록할 때도 memberRepository() 메소드를 호출

memberRepository()가 호출되면 new 때문에 객체를 신규로 생성해야 한다.

(자바 코드이기 때문에 일단 메서드를 호출 할 수밖에 없음)

이러면 싱글톤이 깨지는거 아닌가? 왜냐하면 싱글콘은 분명 객체를 오직 1개만 생성해서 한다고 했으니까.

근데 위에 코드만 보면 객체를 2개를 만드는 것처럼 보인다.

그러나 스프링 컨테이너는 싱글톤 패턴으로 관리한다고 했는데... 과연? 테스트 해보자

* ConfigurationSingletonTest 클래스 참조

SpringAppConfig 빈을 조회해서 클래스 정보를 출력해보면 순수 클래스명이 아닌 xxxCGLIB가 붙은 클래스명이 나온다.

```
class hello.core.SpringAppConfig$$SpringCGLIB$$0
```

이것은 스프링이 CGLIB라는 바이트코드를 조작 라이브러리를 사용해서 SpringAppConfig 클래스를 상속받은 임의의 다른 클래스를 만들고빈으로 등록한 것이다. (프록시)

```

@Bean 
public MemberRepository memberRepository() {
   if(memoryMemberRepository가 이미 스프링 컨테이너에 등록되어 있으면?) {
     return 스프링 컨테이너에서 찾아서 반환;
   } else { //스프링 컨테이너에 없으면
        기존 로직을 호출해서 MemoryMemberRepository를 생성하고 스프링 컨테이너에 등록 return 반환
   }
}

```

그래서 이미 등록된 bean을 다시 등록할 때 새로 등록하는 것이 아닌 기존 것을 반환한다.

@Configuration 을 빼면? 당연히 같은 bean이 계속해서 등록된다.


---

# 컴포넌트 스캔과 의존관계 자동 주입 시작하기

자바 코드 @Bean이나 XML 등을 통해 스프링 빈에 등록할 설정정보를 나열했다.

그러나 등록해야할 빈이 수십 수백개라면? 언제 일일이 작성할꺼임?

그래서 스프링은 설정정보가 없어도 자동으로 빈을 등록하는 컴포넌트 스캔이라는 기능을 제공.

이전에는 SpringAppConfig에서 설정정보(객체 생성 및 주입, 연결관계)를 정의했으나

이제는 @ComponentScan을 통해 @Component가 붙은 모든 클래스를 스프링 빈으로 등록한다.

이때 스프링 빈의 기본 이름은 클래스명, 클래스명의 맨 앞글자만 소문자로 변경. (물론 이름은 내가 설정가능)

그리고 @Autowired가 붙은 곳을 찾아 스프링 컨테이너가 자동으로 등록된 빈을 찾아서 주입한다.

기본 조회 전략은 같은 빈을 찾아서 주입 (ex: getBean(MemberRepository.class))

### 탐색 위치와 기본 스캔 대상

모든 자바 클래스를 다 스캔하면 오래 걸림. 따라서 필요한 위치부터 탐색할 수 있게 지정 가능

```
@ComponentScan(basePackages = "hello.core.member", excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class))
```

basePackages : 탐색할 패키지의 시작 위치 지정. 해당 패키지를 포함하여 하위 패키지 모두 탐색

```
@ComponentScan(basePackageClasses = AutoAppConfig.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class))
```

basePackageClasses : 지정한 클래스의 패키지를 탐색 시작으로 정한다.

만약 지정하지 않으면 @ComponentScan이 붙은 설정 정보 클래스의 패키지가 시작 위치가 된다.

* 권장방법

패키지 위치를 정하지 않고, 최상단에 두는 것이 좋음. 스프링부트도 이걸 제공, 권장.

참고로 스프링 부트를 사용하면 스프링 부트의 대표 시작 정보인 `@SpringBootApplication` 를 이 프로젝트 시작 루트 위치에 두는 것이 관례이다.
(그리고 이 설정안에 바로 `@ComponentScan` 이 들어있다!)

### 필터

includeFilters : 포함할 컴포넌트
excludeFilters : 제외할 컴포넌트

ANNOTATION : 기본값, 어노테이션을 인식해서 동작

ASSIGNABLE_TYPE : 지정한 타입과 자식 타입을 인식해서 동작

ASPECTJ : AspectJ 패턴 사용

REGEX : 정규 표현식

CUSTOM : TypeFilter 인터페이스 구현해서 처리

### 중복 등록

수동 등록 빈과 자동 등록 빈의 이름이 중복 되면? -->> 수동이 우선권!!!

그러나 최근 스프링에서는 이러한 것들 오류가 나게 만들었다. 애매하잖아. 이런걸로 오류가 날 가능성이 충분하다.

따라서 개발자도 빈 이름을 명확하게(클래스명을 중복되지 않게) 개발하는 것이 좋다.

지금 이 프로젝트는 MemberRepository 빈 중복 등록을 한다.

1. 컴포넌트스캔을 통한 MemoryMemberRepository클래스에서 빈 자동 등록
2. SpringAppConfig에서 빈 수동 등록

그래서 아래와 같이 임시로 프로퍼티에서 중복 허용함.

```properties
spring.main.allow-bean-definition-overriding=true
```

그러나 다시 말하지만 중복으로 등록하는 것은 좋지 않다.

---

# 다양한 의존관계 주입 방법

1) 생성자 주입

```java

@Component
class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;


    @Autowired // 생성자가 1개만 있다면 이건 생략 가능하다.
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
}
```

생성자 호출 시점에 딱 1번만 호출되는 것이 보장

불변, 필수 의존 관계에 사용 (한계와 제한을 최대한 두는 것이 모든 코드에 좋다. 확장을 말하는 것이 아님)

final을 사용할 수 있기 때문에 생성자에서 무조건 초기화 해주는 것을 보장.

주입할 대상이 없으면 오류가 난다. 만약 주입할 대상 없이 실행시키고 싶으면 `@Autowired(required = false)`을 사용한다.

2) 세터 주입

```java

@Component
class MemberServiceImpl implements MemberService {

    private MemberRepository memberRepository;


    @Autowired
    public void setMemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
}
```

선택, 변경이 가능하다.

자바빈 프로퍼티 규약의 수정자 메서드 방식을 사용하는 방법이다.

자바빈 프로퍼티는 과거 객체의 접근을 getter, setter를 사용해서 이용하던 규약이다.

3) 필드 주입

```java

@Component
class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;
}
```

외부에서 변경이 불가능하다.

DI 컨테이너가 있어야 작동한다.

절대 사용하지 말자.

테스트에서만 사용하자.

4) 일반 메소드 주입

```java

@Component
class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;


    @Autowired
    public void innit(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
}
```

일반 메서드에 @Autowired를 이용해서 사용.

당연히 스프링 빈이어야 작동한다. 그래야 스프링 컨테이너가 @Autowired를 읽으니까.

### 옵션

* test/autowired 참조

자동 주입 대상 옵션 처리

1) @Autowired(required = false)

자동 주입할 대상이 없으면 수정자 메서드 자체가 호출 안됨

2) @Nullable : 자동 주입할 대상이 없으면 null로 대체

3) Optional<> : 자동 주입할 대상이 없으면 Optional.empty

### 생성자 주입을 선택하라.

* 불변

의존 관계는 어플리케이션이 종료전까지 변하면 안된다.

수정자 주입을 사용하면 public으로 열어둬야 한다.

누군가 수정할 수 있게 만들면 안된다. 딱 1번만 생성(호출)할 수 있게 만들자.

final을 사용할 수 있다. -> 생성자에서 무조건 초기화하라고 말해준다.(컴파일 오류가 뜸)

만약 수정자로 했다면 컴파일에서 알려주지 않음.

프레임워크에 의존하지 않고 순수한 자바 언어를 사용할 수 있다.

### 롬복

@RequiredArgsConstructor

final이 붙은 필드를 모아서 생성자 자동 생결

### 조회 빈이 2개 이상 있을 때

1) @Autowired 필드 명 매칭

DiscountPolicy가 정액, 정율 빈이 두개가 등록되었다 가정

```java

@Component
public class OrderServiceImpl implements OrderService {

    // 기존코드 -> 실패
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = fixDiscountPolicy;
    }


    // 필드명으로 넣은 코드 정액으로 들어감
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy fixDiscountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = fixDiscountPolicy;
    }
}
```

2) @Qualifier -> @Qualifier 끼리 매칭 -> 빈 이름 매칭

```java

@Component
@Qualifier("mainDiscountPolicy")
public class RateDiscountPolicy implements DiscountPolicy {}


@Component
@Qualifier("fixDiscountPolicy")
public class FixDiscountPolicy implements DiscountPolicy {}

// 위처럼 설정하고 


@Component
public class OrderServiceImpl implements OrderService {

    // 주입하는 곳에서 아래와 같이 사용
    public OrderServiceImpl(MemberRepository memberRepository,
            @Qualifier("mainDiscountPolicy") DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = fixDiscountPolicy;
    }


    // 이렇게 수동 빈 등록할때도 사용 가능
    @Bean
    @Qualifier("mainDiscountPolicy")
    public DiscountPolicy discountPolicy() {

        return new RateDiscountPolicy();
    }
}

```

만약 퀄라파이어가 mainDiscountPolicy로 등록된 퀄리파이를 못찾으면 mainDiscountPolicy라고 등록된 빈을 추가로 찾는다.

그러나 왠만하면 퀄리파이어는 퀄리파이어만 찾을 사용할때만 사용하는 것이 좋다.

2차 기능(스프링 빈 찾기)은... 쩝

3) @Primary 사용

같은 빈이 여러개라면 @Primary 붙은거 먼저 주입

```java

@Component
@Primary
public class RateDiscountPolicy implements DiscountPolicy {

}
```

그럼 @Qualifier 와 @Primary 둘다 있다면 우선 순위는? @Qualifier

@Qualifier가 더 상세한 빈을 나타내기 떄문이다.

### 조회한 빈이 모두 필요할때 List, Map

의도적으로 해당 타입의 스프링 빈이 모두 필요한 경우가 있다.

AllBeanTest.class 참조

### 자동, 수동의 올바른 실무 운영 기준

편리한 자동 기능을 기본으로 사용하자.

스프링 부트는 컴포넌트 스캔을 기본으로 사용하고, 스프링 부트의 다양한 스프링 빈들도 조건이 맞으면 자동으로 등록한다.

점점 자동화 된다는 의미.

결정적으로 알아서 자동으로 스프링 컨테이너가 OCP, DIP를 지켜준다.

그러면 수동으로는 언제?

업무 로직 객체(빈) : 사용 X
기술 지원 객체(빈) : 이때 사용한다. ex) AOP, 공통로그 등

그러나 비즈니스(업무) 로직에서 다형성을 적극 활용할 때에는 수동 빈 등록 가능

```java

@Configuration
public class DiscountPolicyConfig {

    @Bean
    public DiscountPolicy rateDiscountPolicy() {
        return new RateDiscountPolicy();
    }


    @Bean
    public DiscountPolicy fixDiscountPolicy() {
        return new FixDiscountPolicy();
    }
}
```

아니면 자동 빈 등록사용하고 같은 패키지에 묶어 두자.

참고로 스프링과 스프링부트가 자동으로 등록하는 수 많은 빈들은 굳이 내가 수동으로 할 필요 없다.

예를 들어 DataSource 같은건 기술 지원 빈인데 스프링부트가 자동으로 빈으로 등록해주기 때문에 내가 수동으로 할 필요가 없다.

스프링 부트가 아니라 내가 직접 기술 지원 객체를 스프링 빈으로 등록해서 개발자의 의도를 명확하게 들어 낸다.

---

# 빈 생명주기 콜백

* test/lifecycle 참조

스프링은 객체 생성 -> 의존관계 주입을 하는 라이프사이클을 가진다.

스프링 빈은 객체를 생성하고, 의존관계 주입이 다 끝난 다음에 필요한 데이터를 사용할 수 있다.

따라서 초기화 작업은 의존 관계 주입이 모두 완료되고 난 다음에 호출해야 한다.

그러면 개발자는 의존 관계 주입이 끝난 시점을 어떻게 알까?

스프링은 의존관계 주입이 완료되면 스프링 빈에게 콜백 메서드를 통해서 초기화를 할수 있는 시점을 알려준다.

또한 스프링은 스프링 컨테이너가 종료되기 직전에 소멸 콜백을 알려준다.(싱글톤 패턴)

스프링 빈의 이벤트 라이프사이클

스프링 컨테이너 생성 -> 스프링 빈 생성 -> 의존관계 주입 -> 초기화 콜백 -> 사용 -> 소멸전 콜백 -> 스프링 종료

초기화 콜백 : 빈이 생성되고, 빈의 의존관계가 주입된 후 호출 (너 이제 초기화 할 수 있어)
소멸전 콜백 : 빈이 소멸되기 직전에 호출

* 객체의 생성과 초기화를 분리하자.

물론 생성자에서 객체 생성 및 주입을 하고 파리미터를 받아 초기화까지 할 수 있다.

그러나!!!

객체 생성(및 주입)과 객체 초기화 하는 책임을 분리!!

생성자에서는 필수 정보를 받아 생성(및 주입)하는 역할만하는 것이 좋다. SRP 책임 분리원칙

### 빈 생명 주기 콜백 3가지 방법

1) 초기화, 소멸 인터페이스

```java
public class NetworkClient implements InitializingBean, DisposableBean {

    private String url;


    // 빈 생성이 다 되고 초기화가 끝나면 호출되는 메서드, InitializingBean 종속
    @Override
    public void afterPropertiesSet() throws Exception {
        connect();
        call("초기화 연결 메시지");
    }


    // 스프링이 끝나기 직전 콜백 함수, DisposableBean 종속
    @Override
    public void destroy() throws Exception {
        disconnect();
    }
}
```

전용 인터페이스에 의존하기 때문에 지금은 거의 사용되지 않음...

2) 설정 정보에 초기화, 종료 메서드 지정

```java

@Configuration
static class LifeCycleConfig {

    @Bean(initMethod = "init", destroyMethod = "close")
    public NetworkClient networkClient() {
        NetworkClient networkClient = new NetworkClient();
        networkClient.setUrl("http://test");

        return networkClient;
    }
}


public class NetworkClient {

    public void init() {
        connect();
        call("초기화 연결 메시지");
    }


    public void close() {
        disconnect();
    }
}


```

메서드 이름을 마음대로 설정 가능

스프링 코드(1번 인터페이스 같은)에 의존하지 않는다.

코드가 아닌 설정정보를 사용하기 때문에 내가 고칠 수 없는 외부 라이브러리에도 초기화, 종료 메서드
적용할 수 있다.

* 종료 메서드 추론

@Bean의 destroyMethod 속성에는 아주 특별한 기능이 있다.

라이브러리는 대부분 close, shutdown 이라는 이름의 종료 메서드를 사용한다.

@Bean의 destoryMethod 는 기본값이 추론으로 등록되어 있다.

이 추론 기능은 close, shutdown 이름 그대로 메서드를 호출한다.

그래서 직접 스프링 빈 등록하면 종료 메서드는 적어주지 않아도 알아서 호출한다.

추론 기능을 사용하지 싫으면 destroyMethod = "" 이렇게 공백 주면 된다.

3) @PostConstruct, @PreDestroy

컴포넌트 스캔과 잘 어울림

단점은 외부 라이브러리에 적용이 불가능.

코드를 고칠 수 없는 외부 라이브러리는 2)번을 사용

---

# 빈 스코프

* test/scope 참조

스코프란 빈이 존재할 수 있는 범위

1) 싱글톤 : 기본 스코프. 가장 긴 스코프

2) 프로토타입 : 프로토타입 빈의 생성과 의존관계 주입까지만 관여. 매우 짧음

스프링 컨테이너는 프로토타입 빈을 미리 생성해서 관리하지 않고 클라이언트 요청이 올때마다 빈을 생성하고 의존관계 주입 후 클라이언트에 전달한다.

그리고 빈에 등록하지 않고 관리하지 않는다.

따라서 @PreDestroy 메소드는 호출되지 않는다.

```` text
* 싱글콘과 함께 사용시 문제점

싱글톤 빈에서 프로토타입을 사용하다보면 여러 요청이 들어왔을 때 프로토타입의 로직의 내용이 변경

SingletonWritePrototypeTest1.class 참조

그럼 싱글톤 빈을 사용하면서 프로토타입의 의도대로 요청(의존 관계를 주입 받을)때마다 신규 프로토타입 빈을 받고(DL : Dependency lookup) 싶다면?

1. 싱글톤 호춣할때마다 프로토타입 빈을 생성한다.

이러면 너무 코드가 지저분하고 스프링 컨테이너에 종속족이다. 단위 테스트를 짜기 어렵다.

-> 우리는 지정한 프로토타입 빈을 컨테이너에서 대신 찾아주는 기능만 원한다.

2. ObjectProvider, ObjectFactory(레거시)

1)번의 문제를 해결하는 녀석.

```java

@Scope("singleton")
@RequiredArgsConstructor
static class ClientBean {

    private final ObjectProvider<PrototypeBean> prototypeBeanProvider;


    public int logic() {
        
        PrototypeBean prototypeBean = prototypeBeanProvider.getObject();
        // 항상 새로운 프로토타입 빈을 생성

        prototypeBean.addCount();

        return prototypeBean.getCount();
    }
}

```

ObjectFactory은 기능이 단순

ObjectProvider은 ObjectFactory를 상속 받은 것. 옵션, 편의 기능도 많고 별로 라이브러리 필요 없음. 그래도 스프링에 의존

3. JSR-330 Provider

스프링 3.0 미만은 javax.inject:javax.inject:1 추가

스프링 3.0 이상은 jakarta.inject:jakarta.inject-api:2.0.1 추가

자바 표준이고 mock을 사용가능

ObjectProvider과 JSR은 프로토타입이 아니더라도 DL할때 언제든지 사용 가능하다.

자바와 스프링이 각각 제공하는 기능이 겹칠때가 있다면 왠만하면 스프링 기능을 쓰는게 좋다.
(예외 : JPA...)

````

그럼 우린 이걸 왜 쓰냐?

매번 사용할 때마다 의존관계 주입이 완료된 새로운 객체를 필요하면... 이건 나도 알겟는데?

근데 실무에서 거의 안씀. 왠만하면 싱글톤으로 해결 가능

3) 웹 관련 스코프 : 웹 환경에서만 동작. 스프링에서 종료 시점까지 관리하기 때문에 종료 메소드 호출함.

LogDemoController 클래스 참조

* request : 웹 요청. http 요청마다 별도의 빈 인스턴스가 생성되고 관리.

UUID를 사용해서 로그에 어떤 url을 통해 무슨 http 요청을 구분해보자.

* session : 웹 세션. http session과 동일한 생명주기
* application : 서블릿 컨텍스트와 같은 생명주기

#### 스코프와 프록시

```java

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyLogger {}
```

proxyMode = ScopedProxyMode.TARGET_CLASS

클래스면 : TARGET_CLASS

인터페이스면 INTERFACES

이렇게하면 MyLogger의 가짜 프록시 클래스를 만들어두고 HTTP request와 상관 없이 가짜 프록시 클래스를 다른 빈에 미리 주입해 둘 수 있다.

즉, MyLogger 가짜 프록시 클래스를 LogDemoController 빈 생성하고 의존관계 주입할때 넣어 두는 것이다.

그 후에 실제 요청이 오면 그때 프록시 객체 내부에서 진짜 빈을 요청하는 위임 로직을 알고 있다.

그래서 myLogger.logic()을 호출하면 최초 가짜 프록시 객체의 메서드를 호출이 되고 가짜 프록시 객체는 실제 객체의 메소드를 찾아서 반환한다.

자, 이거의 진짜 최고의 이점은 뭘까? 바로 클라이언트 코드를 수정하지 않는 다는 것. AOP와 같다.

이것이 다형성과 DI 컨테이너가 가진 큰 장점이다.

싱글톤인것 처럼 보이지만 아니다. 요청마다 새로 생성되기 때문에. 따라서 조심히 국한되어 사용하자.
 



















