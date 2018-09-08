package JUC.volatiles;

public class VolatileInc implements Runnable {

    private static volatile int count = 0;

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            count++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        VolatileInc volatileInc = new VolatileInc();
        Thread a = new Thread(volatileInc);
        Thread b = new Thread(volatileInc);

        a.start();
        b.start();

        for (int i = 0; i < 10000; i++) {
            count++;
        }
        a.join();
        b.join();
        System.out.println("最终count="+count);
    }
}
