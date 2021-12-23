package msg;

public class Tester {
    public static void main(String[] args) {
//        add_3_times();
//        add_remove_when_showing();
        add_remove_after_show_before_attach();
//        add_remove_immediately();
    }

    private static void add_3_times() {
        for (int i=0; i < 3; i ++) {
            MessageVO msg = new MessageVO();
            msg.id = i + "";
            msg.title = "haha";
            msg.content = "test";
            msg.priority = i;
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
                    sleep(180);
                    MessageCenterManager.getInstance().remove(msg.id);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    private static void add_remove_after_show_before_attach() {
        MessageVO msg = new MessageVO();
        msg.id = "001";
        msg.title = "haha";
        msg.content = "test";
        MessageCenterManager.getInstance().add(msg);

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(210);
                    MessageCenterManager.getInstance().remove(msg.id);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
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
