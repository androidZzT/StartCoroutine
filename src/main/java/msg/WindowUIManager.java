package msg;

import java.util.Timer;
import java.util.TimerTask;

public class WindowUIManager {

    interface OnWindowStateChangeListener {
        void onWindowStateChanged(boolean showing);
    }

    private static final String TAG = "WindowUIManager";

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
        Logger.i(TAG, "show called " + msg.id);
        showTask = new TimerTask() {
            @Override
            public void run() {
                Logger.i(TAG, "show " + msg.id);
                currentView = msg.id;
                showing = true;
                listener.onWindowStateChanged(true);

                Logger.i(TAG, "auto remove after 5 sec " + msg.id);
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
            Logger.i(TAG, "update called " + msg);
        }
    }

    public synchronized void removeCurrent() {
        if (!showing) {
            Logger.i(TAG, "removeCurrent failed, no showing window");
            return;
        }
        autoRemoveTask.cancel();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Logger.i(TAG, "remove " + currentView);
                currentView = null;
                showing = false;
                listener.onWindowStateChanged(false);
            }
        },200);
    }

    public synchronized void safeRemove() {
        Logger.i(TAG, "safeRemove called showing="+showing +" showTask="+showTask);
        if (!showing) {
            if (showTask != null) {
                showTask.cancel();
            }
            listener.onWindowStateChanged(false);
        }
    }
}
