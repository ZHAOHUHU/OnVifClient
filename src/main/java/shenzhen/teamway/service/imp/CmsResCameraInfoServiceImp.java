package shenzhen.teamway.service.imp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shenzhen.teamway.controller.PresentSettingController;
import shenzhen.teamway.mapper.CmsResCameraInfoMapper;
import shenzhen.teamway.model.CmsResCameraInfo;
import shenzhen.teamway.pojo.CommandResultMessage;
import shenzhen.teamway.pojo.RebootMessageRequest;
import shenzhen.teamway.service.CmsResCameraInfoService;
import shenzhen.teamway.util.OtherUtils;
import shenzhen.teamway.util.RedisUtils;

import java.util.UUID;

/**
 * @program: onvifservice
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-03-08 10:56
 **/
@Service
@Transactional
public class CmsResCameraInfoServiceImp implements CmsResCameraInfoService {
    Logger log = LoggerFactory.getLogger(CmsResCameraInfoServiceImp.class);
    @Autowired
    private CmsResCameraInfoMapper cmsResCameraInfoMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Value("${request.url}")
    private String url;

    @Override
    public CommandResultMessage getResult(String ip) {
        final CmsResCameraInfo cmsResCameraInfo = cmsResCameraInfoMapper.selectByIP(ip);
        if (cmsResCameraInfo != null) {
            final String username = cmsResCameraInfo.getUsername();
            final String password = cmsResCameraInfo.getPassword();
            final Integer port = cmsResCameraInfo.getPort();
            final RebootMessageRequest request = new RebootMessageRequest();
            final RebootMessageRequest request1 = new RebootMessageRequest();
            request.setReboot(request1);
            request.setCommand("reboot");
            request.setUuid(UUID.randomUUID().toString());
            request.setUser(username);
            request.setAddress(ip);
            request.setPassword(password);
            request.setPort(String.valueOf(port));
            final String postSend = redisUtils.postSend(url + "reboot", request);
            OtherUtils.sleepThread(1000);
            final CommandResultMessage commandResultMessage = redisUtils.string2Object(postSend, CommandResultMessage.class);
            return commandResultMessage;
        } else {
            log.error("根据ip" + ip + "查询不到摄像机");
            return null;
        }
    }
}