package msg;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class Consumer<T> implements Runnable {

    interface CallBack<T> {
        void onConsume(T element);
    }

    private static final String TAG = "Consumer";

    private final BlockingQueue<T> blockingQueue;
    private CallBack<T> callBack;
    private boolean interrupted = false;
    private final Object lock = new Object();

    public Consumer(BlockingQueue<T> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    public void setCallBack(CallBack<T> callBack) {
        this.callBack = callBack;
    }

    public void notifyTake() {
        Logger.i(TAG, "notifyTake called");
        synchronized (lock) {
            lock.notify();
        }
    }

    public void interrupt() {
        this.interrupted = true;
    }

    @Override
    public void run() {
        while (!interrupted) {
            try {
                T element = blockingQueue.take();
                callBack.onConsume(element);
                synchronized (lock) {
                    lock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Logger.i(TAG, "interrupted!");
    }
}
