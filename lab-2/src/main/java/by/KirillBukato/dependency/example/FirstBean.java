package by.KirillBukato.dependency.example;

import by.KirillBukato.dependency.annotation.Bean;
import by.KirillBukato.dependency.annotation.PostConstruct;

@Bean(name = "firstBean")
public class FirstBean {

    void printSomething() {
        System.out.println("Hello, I'm first bean");
    }

    void doSomething() {
        System.out.println("First bean is working on a project...");
    }

    @PostConstruct
    void init() {
        System.out.println("First bean is initialized");
    }
}
