package ru.stepup.homework;

import java.lang.reflect.Proxy;

import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Fraction fr = new Fraction(2,3);
        Fractionable num = Utils.cache(fr);
        ExpiredCleanable ec = (ExpiredCleanable) Proxy.getInvocationHandler(num);
        ec.getCacheCleaner().interrupt();
        for(int i=1; i<=100; i++){
            num.doubleValue();
            sleep(1000);
        }
    }
}