package core;

public class ThreadManager {
    public static void sleep(int ms) {
        try {
            System.out.println("Thread sleeping: " + Thread.currentThread());
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void message(String msg) {
        System.out.println("Thread" + Thread.currentThread().getId() + " : " + msg);
    }
}
