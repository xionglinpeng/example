package JUC.queue.delayQueue;

import java.util.concurrent.DelayQueue;

public class Clock implements Runnable{

    public static volatile boolean stop = false;

    private DelayQueue<Examination> studentDelayQueue;

    public Clock(DelayQueue<Examination> studentDelayQueue) {
        this.studentDelayQueue = studentDelayQueue;
    }

    //监听时间，学生考试完成了，学生交卷
    public void lintenerTime() {
        try {
            for (;;){
                Examination examination = studentDelayQueue.take();
                if (stop) break;
                examination.executor(Examination.SUBMIT);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.lintenerTime();
    }
}
