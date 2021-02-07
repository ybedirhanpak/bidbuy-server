package concurrency;

import core.ThreadManager;

public class Lock {

    private boolean isLocked = false;
    private int lockCount = 0;
    private Thread ownerThread = null;

    public void printMsg(String msg) {
        ThreadManager.message(this + " : " + msg);
    }

    public synchronized void lock() throws InterruptedException {
        while (isLocked && ownerThread != Thread.currentThread()) {
            printMsg("Waiting for lock...");
            wait();
        }
        isLocked = true;
        lockCount++;
        ownerThread = Thread.currentThread();
        printMsg("Locked, lockCount: " + lockCount);
    }

    public synchronized void release() {
        printMsg("In release");
        if (lockCount <= 0 || ownerThread != Thread.currentThread()) {
            throw new IllegalMonitorStateException();
        }

        if (ownerThread == Thread.currentThread()) {
            lockCount--;
            printMsg("Decreased lockCount: " + lockCount);
        }

        if (lockCount == 0) {
            printMsg("Notify others");
            isLocked = false;
            notify();
        }
    }
}
