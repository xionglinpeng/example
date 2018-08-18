package JUC.queue.delayQueue;

import lombok.ToString;

import java.util.Random;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@ToString
public class Student extends AbstractExamination implements Delayed {

    /*姓名*/
    private String name;
    /*年龄*/
    private Integer age;
    /*学号*/
    private String code;
    /*学生用于开始的试卷*/
    private ExaminationPaper examinationPaper;


    /*需要一个讲台，学生用于交试卷*/
    private Platform platform;


    public Student(String name, Integer age, String code, int examDuration, ExaminationPaper examinationPaper, Platform platform) {
        super(new Random().nextInt(examDuration) + 3);
        this.name = name;
        this.age = age;
        this.code = code;
        this.examinationPaper = examinationPaper;
        this.platform = platform;
    }


    //考试
    public void exam() {
        System.out.printf("%s考试中...\n",name);
    }


    /**
     * 交卷（无论是自己交卷还是强制交卷，都是交卷）;
     * 交卷交到哪儿：学生将试卷交到讲台，老师讲台收取试卷。
     */
    public void submit() {
        try {
            this.platform.getPlatform().put(examinationPaper);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("%s交卷，要求时间，实际耗时：%d秒\n",name,super.expendTime);
    }

    @Override
    public void executor(String behavior) {
        switch (behavior) {
            case EXAM:
                this.exam();
                break;
            case SUBMIT:
                this.submit();
                break;
        }
    }
}
