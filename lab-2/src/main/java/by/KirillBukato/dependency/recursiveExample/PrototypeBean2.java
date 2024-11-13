package by.KirillBukato.dependency.recursiveExample;

import by.KirillBukato.dependency.annotation.Bean;
import by.KirillBukato.dependency.annotation.BeanScope;
import by.KirillBukato.dependency.annotation.Inject;
import by.KirillBukato.dependency.annotation.PostConstruct;

@Bean(name = "prototypeBean2", scope = BeanScope.PROTOTYPE)
public class PrototypeBean2 {

    @Inject
    private PrototypeBean1 prototypeBean1;

    public void printSomething() {
        System.out.println("Hello, I'm prototype bean 2");
    }

    public void doSomething() {

    }

    @PostConstruct
    void init() {
        System.out.println("Prototype bean 2 is initialized");
    }
}
