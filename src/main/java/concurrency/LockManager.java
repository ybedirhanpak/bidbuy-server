package concurrency;

import core.ThreadManager;

import java.util.HashMap;

public class LockManager {
    private static HashMap<String, Lock> lockMap = new HashMap<>();

    public static <T> Lock getLock(Class<T> className, int id) {
        String key = className.getSimpleName() + id;
        Lock lock = lockMap.get(key);
        if(lock == null) {
            lock = new Lock();
            lockMap.put(key, lock);
        }
        return lock;
    }
}
