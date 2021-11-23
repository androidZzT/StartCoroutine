package msg;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;

public class MessageCenterManager {

    private volatile static MessageCenterManager sInstance;
    public static MessageCenterManager getInstance() {
        if (sInstance == null) {
            synchronized (MessageCenterManager.class) {
                if (sInstance == null) {
                    sInstance = new MessageCenterManager();
                }
            }
        }
        return sInstance;
    }

    private final LinkedBlockingDeque<MessageVO> blockingDeque = new LinkedBlockingDeque<>();
    private final Consumer<MessageVO> messageConsumer;
    private final WindowUIManager windowUIManager;
    private final Thread consumerThread;
    private boolean showingWindow;

    private MessageCenterManager() {
        messageConsumer = new Consumer<>(blockingDeque);
        consumerThread = new Thread(messageConsumer);
        consumerThread.setName("consumer-thread");
        consumerThread.setDaemon(true);
        consumerThread.start();
        windowUIManager = new WindowUIManager(showing -> {
            System.out.println(Thread.currentThread().getName()+ "-[MessageCenterManager] windowStateChanged showing=" + showing);
            showingWindow = showing;
            if (!showing) {
                messageConsumer.notifyTake();
            }
        });
        messageConsumer.setCallBack(windowUIManager::show);
    }

    public void add(MessageVO msg) {
        System.out.println(Thread.currentThread().getName() + "-[MessageCenterManager] add " + msg);
        if (msg == null) {
            return;
        }
        blockingDeque.offerFirst(msg);
    }

    public void update(MessageVO msg) {
        if (showingWindow) {
            windowUIManager.update(msg);
        } else {

        }
    }

    public void remove(String id) {
        boolean removed = false;
        Iterator<MessageVO> it = blockingDeque.iterator();
        while (it.hasNext()) {
            MessageVO msg = it.next();
            if (msg.id.equals(id)) {
                it.remove();
                removed = true;
                System.out.println(Thread.currentThread().getName() + "-[MessageCenterManager] remove from queue " + id);
                break;
            }
        }
        if (removed) {
            return;
        }

        if (showingWindow) {
            System.out.println(Thread.currentThread().getName() + "-[MessageCenterManager] remove currentView " + id);
            windowUIManager.removeCurrent();
        } else {
            System.out.println(Thread.currentThread().getName() + "-[MessageCenterManager] remove when view still not showing " + id);
            windowUIManager.safeRemove();
        }
    }

    public void stop() {
        System.out.println(Thread.currentThread().getName() + "-[MessageCenterManager] stop called");
        messageConsumer.interrupt();
    }
}
