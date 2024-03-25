package ru.stepup.homework;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class Utils extends Fraction implements Fractionable {
    public Utils(int num, int denum) {
        super(num, denum);
    }

    public static <T> T cache(T fr) {
        Class cls = fr.getClass();
//        for(Field f : Arrays.asList(cls.getDeclaredFields()))
//        {
//            System.out.println(f.getName());
//        }
        return (T) Proxy.newProxyInstance(cls.getClassLoader(),
                cls.getInterfaces(),
                new FractionableInvHandler(fr,new LifeTime()));
    }
    @Override
    public double doubleValue() {
        return super.doubleValue();
    }

    @Override
    public void setNum(int num) {
        super.setNum(num);
    }

    @Override
    public void setDenum(int denum) {
        super.setDenum(denum);
    }
}
