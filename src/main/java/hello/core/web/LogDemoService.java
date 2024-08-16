package hello.core.web;


import hello.core.common.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogDemoService {

    //private final ObjectProvider<MyLogger> myLoggerObjectProvider;
    private final MyLogger myLogger;


    public void logic(String id) {
        // 여기서 또 호출한다 해도 새로 생성하지 않음. 스프링 컨테이너에서 관리하고 있기 때문에
        // 그래서 controller에서 최초 호출할때 생성된 빈은 여기서 같은 빈을 반환한다.
        //MyLogger myLogger = myLoggerObjectProvider.getObject();

        myLogger.log("service id = " + id);
    }

}
