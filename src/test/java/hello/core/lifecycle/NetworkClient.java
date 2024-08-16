package hello.core.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class NetworkClient {

    private String url;


    public NetworkClient() {
        System.out.println("생성자 url = " + url);
        connect();
        call("초기화 연결 메시지");
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public void connect() {
        System.out.println("connect : " + url);
    }


    public void call(String message) {
        System.out.println("call = " + url + " message = " + message);
    }


    public void disconnect() {
        System.out.println("close = " + url);
    }


    @PostConstruct
    public void init() {
        connect();
        call("초기화 연결 메시지");
    }


    @PreDestroy
    public void close() {
        disconnect();
    }
}
