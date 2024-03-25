package ru.stepup.homework;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FractionableInvHandler implements InvocationHandler,ExpiredCleanable {
    private Object obj;
    private boolean cached = false;
    private double cachedValue;
    private LifeTimeable lifeTime;
    private Map<String, TimestampedObj> cacheObjWithLT = new HashMap<>();
    private final DaemonJob cacheCleaner = new DaemonJob(this::cacheClear,100);

    public FractionableInvHandler(Object obj, LifeTimeable lt) {
        this.obj = obj;
        this.lifeTime = lt;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method classMethod = obj.getClass().getMethod(method.getName(), method.getParameterTypes());
        if (classMethod.isAnnotationPresent(Cache.class)) {
            //System.out.println("is cached");
            lifeTime.setLT(classMethod.getAnnotation(Cache.class).value());
            synchronized (cacheObjWithLT) {
                String cacheKey = String.valueOf(method.hashCode()) + method.getName() + String.valueOf(obj.hashCode());
                //System.out.println(cacheObjWithLT);
                if(cacheObjWithLT.containsKey(cacheKey)) {
                    //System.out.println("containsKey");
                    cacheObjWithLT.get(cacheKey).refreshTL(lifeTime);
                    return cacheObjWithLT.get(cacheKey).o;
                }
                else {
                    //System.out.println("nocontainsKey");
                    Object outObj = method.invoke(obj,args);
                    cacheObjWithLT.put(cacheKey, new TimestampedObj(outObj,lifeTime));
                    return outObj;
                }
            }
        }

        return method.invoke(obj,args);
    }

    private void cacheClear() {
        ArrayList<String> expiredKeys= new ArrayList<>();
        cacheObjWithLT.forEach((key,value)-> {
          if(value.lt <= lifeTime.getCurTimeStamp()) expiredKeys.add(key);
        });
        expiredKeys.forEach((key)->{
            synchronized (cacheObjWithLT) {
                cacheObjWithLT.remove(key);
            }
        });
    }
    @Override
    public DaemonJob getCacheCleaner(){
        return cacheCleaner;
    }

    private static class TimestampedObj {
        Object o;
        long lt;

        public TimestampedObj(Object o, LifeTimeable lt) {
            this.o = o;
            refreshTL(lt);
        }
        void refreshTL(LifeTimeable lt){
            this.lt = lt.getCurTimeStamp() + lt.getLT();
        }

    }
}
