package shenzhen.teamway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shenzhen.teamway.mapper.CmsResCameraOnvifInfoMapper;
import shenzhen.teamway.model.CmsResCameraOnvifInfo;
import shenzhen.teamway.pojo.CommandResultMessage;
import shenzhen.teamway.pojo.SetPrestMessageRequest;
import shenzhen.teamway.service.CmeraSetPreset;
import shenzhen.teamway.service.CmsResCameraInfoService;
import shenzhen.teamway.service.imp.CameraOnvifServiceIm;
import shenzhen.teamway.util.OtherUtils;
import shenzhen.teamway.util.RedisUtils;

import java.util.List;
import java.util.Map;

/**
 * @program: onvifservice
 * @description:预置位的设置
 * @author: Zhao Hong Ning
 * @create: 2019-03-04 09:16
 **/
@Controller
public class PresentSettingController {
    @Value("${request.url}")
    private String url;
    @Autowired
    private CmeraSetPreset cmeraSetPreset;
    @Autowired
    CameraOnvifServiceIm cameraOnvifServiceIm;
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    CmsResCameraInfoService cmsResCameraInfoService;
    @Autowired
    CmsResCameraOnvifInfoMapper cmsResCameraOnvifInfoMapper;

    Logger log = LoggerFactory.getLogger(PresentSettingController.class);

    /**
     * @Author: Zhao Hong Ning
     * @Description:设置预置位 传参数的请求 content-type必须是application/json
     * @Date: 2019/3/6
     * @param:
     * @return: java.lang.String
     */
    @PostMapping(name = "setPreset")
    @ResponseBody
    public CommandResultMessage setPreset(@RequestBody Map<String, String> map) {
        final String token = map.get("token");
        final String cameraCode = map.get("cameraCode");
        final String presetName = map.get("presetName");
        final SetPrestMessageRequest request = cmeraSetPreset.setPrestMessageRequest(token, cameraCode, presetName);
        CommandResultMessage message = null;
        if (request != null) {
            final String send = redisUtils.postSend(url + "setPreset", request);
            OtherUtils.sleepThread(1000);
            message = redisUtils.string2Object(send, CommandResultMessage.class);
            final boolean result = message.isResult();
            if (result) {
                final List<CmsResCameraOnvifInfo> cmsResCameraOnvifInfos = cmsResCameraOnvifInfoMapper.selectCamera(cameraCode,null);
                if (cmsResCameraOnvifInfos.size() == 1) {
                    //添加到队列里
                    cameraOnvifServiceIm.getQ().offer(cmsResCameraOnvifInfos.get(0));

                } else {
                    log.error("根据code为" + cameraCode + "查询到的摄像机结果为空");
                }
            }
        } else {
            log.error("封装的发送的消息为空");
            return null;
        }
        return message;
    }

    @RequestMapping(value = "gotoPreset", method = RequestMethod.POST)
    @ResponseBody
    public CommandResultMessage gotoPreset(@RequestBody Map<String, String> m) {
        final String token = m.get("token");
        final String cameraCode = m.get("cameraCode");
        final CommandResultMessage commandResultMessage = cmeraSetPreset.gotoPreset(cameraCode, token);
        return commandResultMessage;
    }

    @RequestMapping(value = "reboot", method = RequestMethod.POST)
    @ResponseBody
    public CommandResultMessage reboot(@RequestBody Map<String, String> m) {
        final String ip = m.get("cameraAddr");
        final CommandResultMessage result = cmsResCameraInfoService.getResult(ip);
        return result;
    }
}