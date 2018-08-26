package JUC.queue.delayQueue;

public class ExaminationBarrier extends AbstractExamination {

    private Teacher teacher;

    public ExaminationBarrier(int examDuration,Teacher teacher) {
        super(examDuration);
        this.teacher = teacher;
    }

    @Override
    public void executor(String behavior) {
//        Thread.currentThread().interrupt();

        switch (behavior) {
            case SUBMIT:
//                Thread.currentThread().interrupt();
                Clock.stop = true;
                teacher.forceSunmit();
        }

    }
}
