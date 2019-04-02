package shenzhen.teamway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @program: onvifservice
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-03-04 16:33
 **/
public class UrlRequestMapping {
    public static final String getPresentUrl = "http://192.168.12.188:8081/";
    private String dateTIme;

    public String getDateTIme() {
        return dateTIme;
    }

    public void setDateTIme(String dateTIme) {
        this.dateTIme = dateTIme;
    }
}