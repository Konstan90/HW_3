package ru.stepup.homework;

public class DaemonJob extends Thread{
    Runnable task;
    int interval;
    public DaemonJob(Runnable task, int interval) {
        this.task = task;
        this.interval = interval;
        this.setDaemon(true);
        this.start();
    }

    @Override
    public void run() {
        do {
            if(!interrupted()){
                try {
                    sleep(interval);
                    executeTask();
                } catch (InterruptedException e) {
                }
            }
        } while (true);
    }

    public void executeTask() {
        task.run();
    }

}
