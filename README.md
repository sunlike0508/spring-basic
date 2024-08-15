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
ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringAppConfig.class);
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
































