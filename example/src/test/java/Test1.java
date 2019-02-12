import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Test1 {

    private final static List<String> handleEventList = new ArrayList<>();

    static {
        handleEventList.add("6009");
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println(handleEventList);



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


        Thread.sleep(100000);
        for (int i = 1;i<100;i++){

        }

    }
}
