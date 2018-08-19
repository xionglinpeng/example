package JUC.queue.delayQueue;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public abstract class AbstractExamination implements Examination{

    /*学生考试耗时，学生考试使用多长时间，由学生自己决定（只要没到考试规定的时间）*/
    protected long expendTime;

    /*提交时间*/
    protected long submitTime;


    public AbstractExamination(long expendTime) {
        this.expendTime = expendTime;
        this.submitTime = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(this.expendTime,TimeUnit.SECONDS);

    }

    @Override
    public long getDelay(TimeUnit unit) {
        return submitTime-System.currentTimeMillis();
    }

    @Override
    public int compareTo(Delayed o) {
        AbstractExamination examination = (AbstractExamination)o;
        return (int)(this.expendTime - examination.expendTime);
    }

}
