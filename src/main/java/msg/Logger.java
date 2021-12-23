package msg;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    public static void i(String tag, String msg) {
        Date d = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSSS");
        System.out.println(simpleDateFormat.format(d) + " " + Thread.currentThread().getName() + "-" + "["+ tag +"]" + " " + msg);
    }
}
