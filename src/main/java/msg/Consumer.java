package msg;

import java.util.concurrent.BlockingDeque;

public class Consumer<T> implements Runnable {

    interface CallBack<T> {
        void onConsume(T element);
    }

    private final BlockingDeque<T> blockingDeque;
    private CallBack<T> callBack;
    private boolean interrupted = false;
    private final Object lock = new Object();

    public Consumer(BlockingDeque<T> blockingDeque) {
        this.blockingDeque = blockingDeque;
    }

    public void setCallBack(CallBack<T> callBack) {
        this.callBack = callBack;
    }

    public void notifyTake() {
        System.out.println(Thread.currentThread().getName()+ "-notifyTake called");
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
                T element = blockingDeque.takeLast();
                callBack.onConsume(element);
                synchronized (lock) {
                    lock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName()+ "-interrupted!");
    }
}
