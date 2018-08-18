package JUC.queue.delayQueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Platform {

    /*讲台*/
    private BlockingQueue<ExaminationPaper> platform;

    public Platform() {
        this.platform = new LinkedBlockingQueue<>();
    }

    /**
     * 获取讲台
     */
    public BlockingQueue<ExaminationPaper> getPlatform() {
        return platform;
    }
}
