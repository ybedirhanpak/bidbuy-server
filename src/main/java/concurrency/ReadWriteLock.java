package concurrency;

public class ReadWriteLock {
    private int numReaders = 0;
    private Thread writerThread;

    private void message(String msg) {
        System.out.println(Thread.currentThread() + " : " + msg );
    }

    public synchronized void lockRead() throws InterruptedException {
        message("in lockRead");
        while (numReaders == -1 && writerThread != Thread.currentThread()) {
            message("in lockRead waiting...");
            wait();
        }
        numReaders++;
        message("in lockRead increased numReaders.");
    }

    public synchronized void releaseRead() {
        message("in releaseRead");
        if (numReaders <= 0) {
            throw new IllegalMonitorStateException();
        }
        numReaders--;
        message("decreased numReaders.");
        if (numReaders == 0) {
            notifyAll();
        }
    }

    public synchronized void lockWrite() throws InterruptedException {
        message("in lockWrite");
        while (numReaders > 0) {
            message("in lockWrite waiting...");
            wait();
        }
        message("in lockWrite increased numReaders.");
        numReaders = -1;
        writerThread = Thread.currentThread();
    }

    public synchronized void releaseWrite() {
        if(numReaders != -1 || writerThread != Thread.currentThread()) {
            throw new IllegalMonitorStateException();
        }
        message("release write");
        numReaders = 0;
        writerThread = null;
        notifyAll();
    }
}
