package JUC.queue.delayQueue;

import java.util.concurrent.DelayQueue;

public class Main {

    public static void main(String[] args) {

        int studentNum = 10;
        int examDuration = 12;

        Platform platform = new Platform();
        ExaminationPaper examinationPaper = new ExaminationPaper();

        DelayQueue<Examination> examinations = new DelayQueue<>();
        for (int i = 0; i < studentNum; i++) {
            examinations.put(new Student("小明-"+i,15,"20111100001",examDuration,examinationPaper,platform));
        }

        Teacher teacher = new Teacher(platform,examinations);

        //添加考试到时屏障
        examinations.put(new ExaminationBarrier(examDuration,teacher));


        Thread teacherThread = new Thread(teacher);
        Thread clock = new Thread(new Clock(examinations));

        teacherThread.start();
        clock.start();

//        ArriveTime arriveTime = new ArriveTime(12,teacher);
//        arriveTime.arrive();
    }
}
