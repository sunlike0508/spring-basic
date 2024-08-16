package hello.core.web;


import hello.core.common.MyLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
    //private final ObjectProvider<MyLogger> myLoggerObjectProvider;
    private final MyLogger myLogger;


    @RequestMapping("log-demo")
    public @ResponseBody String logDemo(HttpServletRequest request) throws InterruptedException {
        String requestURL = request.getRequestURL().toString();

        System.out.println("mylogger = " + myLogger.getClass());

        // 이때 request 빈을 생성하고 주입하고 초기화(uuid)해서 던진다.
        // MyLogger myLogger = myLoggerObjectProvider.getObject();

        // 여기서 이제 내가 필요한 request의 값들 set, 맴버id, url 등등
        myLogger.setRequestURL(requestURL);
        myLogger.log("logDemo test");

        //Thread.sleep(10000);

        logDemoService.logic("testId");

        return "OK";
    }
}
