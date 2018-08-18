package JUC.queue.delayQueue;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.SynchronousQueue;

public class Teacher implements Runnable {

    /*需要一个讲台，老师用于收取试卷*/
    private Platform platform;

//    private List<>
    private DelayQueue<Examination> studentDelayQueue;

    private int alreadySubmitPaperNum = 0;

    public static SynchronousQueue synchronousQueue = new SynchronousQueue();

    public Teacher(Platform platform, DelayQueue<Examination> studentDelayQueue) {
        this.platform = platform;
        this.studentDelayQueue = studentDelayQueue;
    }

    //通知学生开始考试
    public void notifyExam() {
        studentDelayQueue.forEach(student->student.executor(Examination.EXAM));
    }

    //接收学生提交的试卷
    public void receiveExaminationPaper(){
        try {
            for (;;) {
                if (alreadySubmitPaperNum == studentDelayQueue.size()) {
                    break;
                }
                ExaminationPaper examinationPaper = platform.getPlatform().take();
//                System.out.printf("老师接收到学生的试卷：%s\n",examinationPaper);
                alreadySubmitPaperNum++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //时间到期了，强制未完成的学生交卷
    public void forceSunmit() {
        studentDelayQueue.forEach(student -> {
            System.out.print("强制交卷：");
            student.executor(Examination.SUBMIT);
        });
    }

    @Override
    public void run() {
        //通知学生开始考试
        this.notifyExam();
        //监听接收完成考试学生的试卷
        this.receiveExaminationPaper();
    }
}
