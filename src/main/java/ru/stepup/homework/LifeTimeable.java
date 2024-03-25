package ru.stepup.homework;

public interface LifeTimeable {
    long getCurTimeStamp();
    long getLT();
    void setLT(long tl);

    void setIsConsiderLT(boolean isConsiderLT);

}
