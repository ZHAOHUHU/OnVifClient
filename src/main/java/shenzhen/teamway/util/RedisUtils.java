package shenzhen.teamway.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import shenzhen.teamway.config.RestErrorHandler;
import shenzhen.teamway.model.CmsResCameraOnvifInfo;

/**
 * @program: onvifservice
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-03-04 10:58
 **/
@Component
public class RedisUtils {
    Logger log = LoggerFactory.getLogger(RedisUtils.class);
    @Autowired
    private RestTemplate restTemplate;

    public <T> String postSend(String url, T t) {
        //RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        //RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<String> responseEntity = null;
        //创建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonString = JSONObject.toJSONString(t);
        HttpEntity<String> entity = new HttpEntity<>(jsonString, headers);
        try {
            responseEntity = restTemplate.postForEntity(url, entity, String.class);
        } catch (Exception e) {
            log.error(e.toString());
            e.printStackTrace();
        }
        String body = responseEntity.getBody();//{"msg":"调用成功！","code":1}
        return body;
    }

    public <T> T string2Object(String s, Class<T> tClass) {
        T t = null;
        try {
            t = JSONObject.parseObject(s, tClass);
        } catch (Exception e) {
            log.error("转换对象失败" + s);
            e.printStackTrace();
        }
        return t;
    }

    public static void main(String[] args) {
        CmsResCameraOnvifInfo c = new CmsResCameraOnvifInfo();
        // RedisUtils.postSend("dsfsdf",c);
    }
}