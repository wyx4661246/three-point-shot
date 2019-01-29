import java.util.concurrent.atomic.AtomicInteger;

public class Test1 {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("11111111111111111111");
        System.exit(-1);
        Thread.sleep(10000);


        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            private volatile boolean hasShutdown = false;
            private AtomicInteger shutdownTimes = new AtomicInteger(0);

            @Override
            public void run() {
                synchronized (this) {
                    System.out.println("====================");
                }
            }
        }, "ShutdownHook"));
    }
}
