package shenzhen.teamway.util;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: onvifservice
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-03-05 18:16
 **/
@Component
public class OtherUtils {
    public  String getDate2String(Date date) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String s = format.format(date);
        return s;

    }

    public static void main(String[] args) {

    }
}