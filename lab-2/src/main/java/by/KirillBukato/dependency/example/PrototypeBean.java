package by.KirillBukato.dependency.example;

import by.KirillBukato.dependency.annotation.Bean;
import by.KirillBukato.dependency.annotation.BeanScope;
import by.KirillBukato.dependency.annotation.Inject;
import by.KirillBukato.dependency.annotation.PostConstruct;

@Bean(name = "prototypeBean", scope = BeanScope.PROTOTYPE)
public class PrototypeBean {

    @Inject
    private FirstBean randomNameForFirstBean;

    @Inject
    private NotBean notBean;

    public void printSomething() {
        System.out.println("Hello, I'm prototype bean");
    }

    public void doSomething() {
        randomNameForFirstBean.printSomething();
        notBean.printSomething();
    }

    @PostConstruct
    void init() {
        System.out.println("Prototype bean is initialized");
    }
}
