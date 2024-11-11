package by.KirillBukato.dependency.example;

import by.KirillBukato.dependency.annotation.Bean;
import by.KirillBukato.dependency.annotation.Inject;
import by.KirillBukato.dependency.annotation.PostConstruct;

@Bean(name = "otherBean")
public class OtherBean {

    @Inject
    private FirstBean firstBean;

    void doSomething() {
        System.out.println("Hi, I'm other bean");
    }

    void doSomethingWithFirst() {
        System.out.println("Trying to shake first bean...");
        firstBean.doSomething();
    }

    @PostConstruct
    void init() {
        System.out.println("Other bean is initialized");
    }

}
