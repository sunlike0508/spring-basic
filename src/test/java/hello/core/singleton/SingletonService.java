package hello.core.singleton;

public class SingletonService {

    private static final SingletonService singletonService = new SingletonService();


    /**
     * 이렇게 생성자를 private으로 막아서 외부에서 새로 생성되지 못하게 막는다.
     */
    private SingletonService() {
        System.out.println("싱글콘 객체 생성 호출");
    }


    // 싱글톤 객체 가져다 쓰는 함수
    public static SingletonService getInstance() {
        return singletonService;
    }


    public void logic() {
        System.out.println("싱글톤 객체 사용 호출");
    }
}
