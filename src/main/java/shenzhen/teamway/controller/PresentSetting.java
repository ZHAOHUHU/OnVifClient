package shenzhen.teamway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: onvifservice
 * @description:预置位的设置
 * @author: Zhao Hong Ning
 * @create: 2019-03-04 09:16
 **/
@Controller
public class PresentSetting {


    //public

    @RequestMapping("index")
    @ResponseBody
    public String azhen() {
        System.out.println("gsdgsvgsrd");
        return "hekko";
    }
}