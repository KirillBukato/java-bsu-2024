package by.KirillBukato.dependency.recursiveExample;

import by.KirillBukato.dependency.annotation.Bean;
import by.KirillBukato.dependency.annotation.BeanScope;
import by.KirillBukato.dependency.annotation.Inject;
import by.KirillBukato.dependency.annotation.PostConstruct;

@Bean(name = "prototypeBean1", scope = BeanScope.PROTOTYPE)
public class PrototypeBean1 {

    @Inject
    private PrototypeBean2 prototypeBean2;

    public void printSomething() {
        System.out.println("Hello, I'm prototype bean 1");
    }

    public void doSomething() {

    }

    @PostConstruct
    void init() {
        System.out.println("Prototype bean 1 is initialized");
    }
}
