package JUC.queue.delayQueue;

import java.util.concurrent.Delayed;

public interface Examination extends Delayed {

    String EXAM = "EXAM";

    String SUBMIT = "SUBMIT";


    void executor(String behavior);
}
