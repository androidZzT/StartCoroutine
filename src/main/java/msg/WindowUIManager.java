package msg;

import java.util.Timer;
import java.util.TimerTask;

public class WindowUIManager {

    interface OnWindowStateChangeListener {
        void onWindowStateChanged(boolean showing);
    }

    private final OnWindowStateChangeListener listener;
    private Timer timer = new Timer("UI");
    private String currentView;
    private volatile boolean showing;
    private TimerTask showTask;
    private TimerTask autoRemoveTask;

    public WindowUIManager(OnWindowStateChangeListener listener) {
        this.listener = listener;
    }

    public synchronized void show(MessageVO msg) {
        System.out.println(Thread.currentThread().getName() + "-[WindowUIManager] show called " + msg.id);
        showTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "-[WindowUIManager] show " + msg.id);
                currentView = msg.id;
                showing = true;
                listener.onWindowStateChanged(true);

                System.out.println(Thread.currentThread().getName() + "-[WindowUIManager] auto remove after 5 sec " + msg.id);
                autoRemoveTask = new TimerTask() {
                    @Override
                    public void run() {
                        removeCurrent();
                    }
                };
                timer.schedule(autoRemoveTask, 5000);
            }
        };
        timer.schedule(showTask, 200);
    }

    public synchronized void update(MessageVO msg) {
        if (showing && currentView.equals(msg.id)) {
            System.out.println(Thread.currentThread().getName() + "-[WindowUIManager] update called " + msg);
        }
    }

    public synchronized void removeCurrent() {
        if (!showing) {
            System.out.println(Thread.currentThread().getName() + "-[WindowUIManager] removeCurrent failed, no showing window");
            return;
        }
        autoRemoveTask.cancel();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "-[WindowUIManager] remove " + currentView);
                currentView = null;
                showing = false;
                listener.onWindowStateChanged(false);
            }
        },200);
    }

    public synchronized void safeRemove() {
        System.out.println(Thread.currentThread().getName() + "-[WindowUIManager] safeRemove called showing="+showing +" showTask="+showTask);
        if (!showing && showTask != null) {
            showTask.cancel();
        }
    }
}
