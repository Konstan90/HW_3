package ru.stepup.homework;

public class LifeTime implements LifeTimeable {
    private long currentTimeStamp;
    private long tl;
    private boolean isConsiderLT=true;

    public LifeTime(long currentTimeStamp, long tl) {
        this.currentTimeStamp = currentTimeStamp;
        this.tl = tl;
    }

    public LifeTime(long currentTimeStamp) {
        this.currentTimeStamp = currentTimeStamp;
    }

    public LifeTime() {
        this(System.currentTimeMillis());
    }


    @Override
    public long getCurTimeStamp() {
        return System.currentTimeMillis();
    }

    @Override
    public long getLT() {
        if(isConsiderLT) return tl;
        return 0L;
    }

    @Override
    public void setLT(long tl) {
        this.tl = tl;
    }

    @Override
    public void setIsConsiderLT(boolean isConsiderLT) {
        this.isConsiderLT = isConsiderLT;
    }
}
