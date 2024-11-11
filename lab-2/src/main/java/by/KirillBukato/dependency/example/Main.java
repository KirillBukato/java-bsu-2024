package by.KirillBukato.dependency.example;

import by.KirillBukato.dependency.context.ApplicationContext;
import by.KirillBukato.dependency.context.HardCodedSingletonApplicationContext;

public class Main {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new HardCodedSingletonApplicationContext(
                FirstBean.class, OtherBean.class
        );
        applicationContext.start();

        FirstBean firstBean = (FirstBean) applicationContext.getBean("firstBean");
        OtherBean otherBean = (OtherBean) applicationContext.getBean("otherBean");

        firstBean.doSomething();
        otherBean.doSomething();
    }
}
