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




























