package shenzhen.teamway.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shenzhen.teamway.mapper.CmsResCameraInfoMapper;
import shenzhen.teamway.mapper.CmsResCameraOnvifInfoMapper;
import shenzhen.teamway.mapper.CmsResCameraPresetInfoMapper;
import shenzhen.teamway.model.CmsResCameraInfo;
import shenzhen.teamway.model.CmsResCameraOnvifInfo;
import shenzhen.teamway.pojo.CommandResultMessage;
import shenzhen.teamway.pojo.GetPresetsMessageRequest;
import shenzhen.teamway.pojo.SetDateAndTimeMessageRequest;
import shenzhen.teamway.pojo.request.GetPresetsRequestBody;
import shenzhen.teamway.pojo.request.SetDateAndTimeRequestBody;
import shenzhen.teamway.util.OtherUtils;
import shenzhen.teamway.util.RedisUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @program: onvifservice
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-03-06 16:47
 **/
@Service
public class UpdateTimeImp {
    @Value("${request.url}")
    private String url;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    CmsResCameraInfoMapper cmsResCameraInfoMapper;

    @Scheduled(cron = "${updateTime}")
    public void updateTime() {
        final List<CmsResCameraInfo> cmsResCameraInfos = cmsResCameraInfoMapper.selectAll();
        for (CmsResCameraInfo camera : cmsResCameraInfos) {
            final SetDateAndTimeMessageRequest request = new SetDateAndTimeMessageRequest();
            final SetDateAndTimeRequestBody requestBody = new SetDateAndTimeRequestBody();
            final String uuid = UUID.randomUUID().toString();
            request.setAddress(camera.getIp());
            request.setCommand("setDateAndTime");
            request.setPort(String.valueOf(camera.getPort()));
            request.setUser(camera.getUsername());
            request.setPassword(camera.getPassword());
            request.setVersion(1);
            request.setUuid(uuid);
            final String date2String = OtherUtils.getDate2String(new Date());
            requestBody.setDateTime(date2String);
            requestBody.setTimeZone("CST-8");
            request.setSetDateAndTime(requestBody);
            final String s = redisUtils.postSend(url + request.getCommand(), request);
            OtherUtils.sleepThread(1000);
            final CommandResultMessage commandResultMessage = redisUtils.string2Object(s, CommandResultMessage.class);
        }
    }
}