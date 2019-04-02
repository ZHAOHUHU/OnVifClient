package shenzhen.teamway.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import shenzhen.teamway.service.imp.CmsResCameraInfoServiceImp;

import java.io.IOException;
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
    static Logger log = LoggerFactory.getLogger(OtherUtils.class);

    public static String getDate2String(Date date) {

        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String s = format.format(date);
        return s;

    }

    public static String getCommond(String data) {
        StringBuffer buf = new StringBuffer();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null; // 读取Json
        try {
            rootNode = mapper.readTree(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final String command = rootNode.path("command").asText();

        return command;
    }

    /**
     * @Author: Zhao Hong Ning
     * @Description:该方法只是用于二级json无数组嵌套
     * @Date: 2019/3/8
     * @param: data
     * @param: key
     * @return: java.lang.String
     */
    public static String getJsonValue(String data, String key) {
        String string = null;
        final JSONObject jsonObject = JSONObject.parseObject(data);
        for (String s : jsonObject.keySet()) {
            if (s.equals(key)) {
                string = jsonObject.getString(key);
                return string;
            }
        }
        for (String s : jsonObject.keySet()) {
            final JSONObject jsonObject1 = jsonObject.getJSONObject(s);
            string = jsonObject1.getString(key);
            return string;
        }


        return string;
    }

    public static void sleepThread(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error(e.toString());
        }
    }

    public static void main(String[] args) {
        String s = "{\"command\": \"getPresets\",\"cameraAddr\": \"192.168.12.103\",\"uuid\": \"random string\",\"setPreset\": {\"presetIndex\": 1,\"name\": \"preset1\"}}";
        final String value = OtherUtils.getJsonValue(s, "presetIndex");

        System.out.println(value);
    }

}