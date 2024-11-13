package by.KirillBukato.dependency.recursiveExample;

import by.KirillBukato.dependency.context.ApplicationContext;
import by.KirillBukato.dependency.context.SimpleApplicationContext;

public class Main {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new SimpleApplicationContext(
                PrototypeBean1.class,
                PrototypeBean2.class
        );
        applicationContext.start();

        try {
            PrototypeBean1 prototypeBean1 = (PrototypeBean1) applicationContext.getBean("prototypeBean1");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}

