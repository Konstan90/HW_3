package ru.stepup.homework;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Proxy;

import static java.lang.Thread.sleep;

public class CacheTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
   private final PrintStream originalOut = System.out;


    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }
    @Test
    public void cacheTest() throws InterruptedException {
        //Время жизни кэша - 5 сек
        Fraction fr = new Fraction(2,3);
        Fractionable num = Utils.cache(fr);
        ExpiredCleanable ec = (ExpiredCleanable) Proxy.getInvocationHandler(num);
        ec.getCacheCleaner().interrupt();
        num.doubleValue(); //первый вызов, кэш пустой, печатается "invoke double value"
        Assertions.assertEquals("invoke double value\n", outContent.toString());
        num.doubleValue(); //вызова на экран не должно быть, тк значение возвращается из кэша
        Assertions.assertEquals("invoke double value\n", outContent.toString()); //проверяем что экран не изменился
        sleep(600);//засыпаем на 6 секунд (объект живет 5 секунд)
        num.doubleValue();
        //после очистки кэша на экране должно быть 2 напечатанных строки
        Assertions.assertEquals("invoke double value\ninvoke double value\n", outContent.toString());
    }
    @Test
    public void denumTest(){
        Fraction fr = new Fraction(1,2);
        Assertions.assertEquals((double) fr.doubleValue(),(double) 1/2);
    }

}
