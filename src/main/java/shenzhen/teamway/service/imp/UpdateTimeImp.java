package shenzhen.teamway.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import shenzhen.teamway.mapper.CmsResCameraOnvifInfoMapper;
import shenzhen.teamway.mapper.CmsResCameraPresetInfoMapper;
import shenzhen.teamway.model.CmsResCameraOnvifInfo;
import shenzhen.teamway.pojo.GetPresetsMessageRequest;
import shenzhen.teamway.pojo.request.GetPresetsRequestBody;

import java.util.List;
import java.util.UUID;

/**
 * @program: onvifservice
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-03-06 16:47
 **/
@Service
//@PropertySource(value = "classpath:application.properties")
public class UpdateTimeImp {
    @Autowired
    CmsResCameraOnvifInfoMapper cmsResCameraOnvifInfoMapper;

    //@Scheduled(cron = "")
    public void updateTime() {
        final List<CmsResCameraOnvifInfo> cmsResCameraOnvifInfos = cmsResCameraOnvifInfoMapper.selectCamera(null);
        for (CmsResCameraOnvifInfo camera : cmsResCameraOnvifInfos) {
            GetPresetsMessageRequest request = new GetPresetsMessageRequest();
            GetPresetsRequestBody body = new GetPresetsRequestBody();
            final String uuid = UUID.randomUUID().toString();
            String cameraCode = camera.getCameraCode();
            body.setProfile(camera.getProfileMain());
            body.setPtzUrl(camera.getPtzUrl());
            request.setGetPresets(body);
            request.setAddress(camera.getCmsResCameraInfo().getIp());
            request.setCommand("getPresets");
            request.setPort(String.valueOf(camera.getCmsResCameraInfo().getPort()));
            request.setUser(camera.getCmsResCameraInfo().getUsername());
            request.setPassword(camera.getCmsResCameraInfo().getPassword());
            request.setVersion(1);
        }
    }
}