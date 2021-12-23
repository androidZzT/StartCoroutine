package msg;

import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class MessageCenterManager {
    
    private static final String TAG = "MessageCenterManager";
            
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

    private final BlockingQueue<MessageVO> blockingQueue;
    private final Consumer<MessageVO> messageConsumer;
    private final WindowUIManager windowUIManager;
    private final Thread consumerThread;
    private boolean showingWindow;

    private MessageCenterManager() {
        blockingQueue = new PriorityBlockingQueue<MessageVO>(128, new Comparator<MessageVO>() {
            @Override
            public int compare(MessageVO o1, MessageVO o2) {
                return o2.priority - o1.priority;
            }
        });
        messageConsumer = new Consumer<MessageVO>(blockingQueue);
        consumerThread = new Thread(messageConsumer);
        consumerThread.setName("consumer-thread");
        consumerThread.setDaemon(true);
        consumerThread.start();
        windowUIManager = new WindowUIManager(showing -> {
            Logger.i(TAG, "windowStateChanged showing=" + showing);
            showingWindow = showing;
            if (!showing) {
                messageConsumer.notifyTake();
            }
        });
        messageConsumer.setCallBack(windowUIManager::show);
    }

    public void add(MessageVO msg) {
        Logger.i(TAG, "add " + msg);
        if (msg == null) {
            return;
        }
        blockingQueue.offer(msg);
    }

    public void update(MessageVO msg) {
        if (showingWindow) {
            windowUIManager.update(msg);
        } else {

        }
    }

    public void remove(String id) {
        boolean removed = false;
        Iterator<MessageVO> it = blockingQueue.iterator();
        while (it.hasNext()) {
            MessageVO msg = it.next();
            if (msg.id.equals(id)) {
                it.remove();
                removed = true;
                Logger.i(TAG, "remove from queue " + id);
                break;
            }
        }
        if (removed) {
            return;
        }

        if (showingWindow) {
            Logger.i(TAG, "remove currentView " + id);
            windowUIManager.removeCurrent();
        } else {
            Logger.i(TAG, "remove when view still not showing " + id);
            windowUIManager.safeRemove();
        }
    }

    public void stop() {
        Logger.i(TAG, "stop called");
        messageConsumer.interrupt();
    }
}
