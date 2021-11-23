package msg;

public class Tester {
    public static void main(String[] args) {
//        add_3_times();
//        add_remove_when_showing();
        add_remove_immediately();
    }

    private static void add_3_times() {
        for (int i=0; i < 3; i ++) {
            MessageVO msg = new MessageVO();
            msg.id = i + "";
            msg.title = "haha";
            msg.content = "test";
            MessageCenterManager.getInstance().add(msg);
        }
    }

    private static void add_remove_when_showing() {
        MessageVO msg = new MessageVO();
        msg.id = "001";
        msg.title = "haha";
        msg.content = "test";
        MessageCenterManager.getInstance().add(msg);

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                    MessageCenterManager.getInstance().remove(msg.id);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
        try {
            t.join();
            MessageVO msg2 = new MessageVO();
            msg2.id = "002";
            msg2.title = "xixi";
            msg2.content = "test";
            MessageCenterManager.getInstance().add(msg2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void add_remove_immediately() {
        MessageVO msg = new MessageVO();
        msg.id = "001";
        msg.title = "haha";
        msg.content = "test";
        MessageCenterManager.getInstance().add(msg);
        MessageCenterManager.getInstance().remove(msg.id);
    }

    private static void add_update_when_showing() {

    }

}
