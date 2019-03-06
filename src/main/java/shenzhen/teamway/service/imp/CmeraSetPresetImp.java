package shenzhen.teamway.service.imp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shenzhen.teamway.mapper.CmsResCameraOnvifInfoMapper;
import shenzhen.teamway.model.CmsResCameraOnvifInfo;
import shenzhen.teamway.pojo.SetPrestMessageRequest;
import shenzhen.teamway.pojo.request.SetPresetRequestBody;
import shenzhen.teamway.service.CmeraSetPreset;

import java.util.List;
import java.util.UUID;

/**
 * @program: onvifservice
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-03-06 11:06
 **/
@Service
public class CmeraSetPresetImp implements CmeraSetPreset {
    Logger log = LoggerFactory.getLogger(CameraOnvifServiceIm.class);
    @Autowired
    CmsResCameraOnvifInfoMapper cmsResCameraOnvifInfoMapper;

    public SetPrestMessageRequest setPrestMessageRequest(String token, String cameraCode, String presetName) {
        SetPrestMessageRequest request = new SetPrestMessageRequest();
        final List<CmsResCameraOnvifInfo> cmsResCameraOnvifInfos = cmsResCameraOnvifInfoMapper.selectCamera(cameraCode);
        if (cmsResCameraOnvifInfos.size() == 1) {
            CmsResCameraOnvifInfo camera = cmsResCameraOnvifInfos.get(0);
            SetPresetRequestBody set = new SetPresetRequestBody();
            set.setName(presetName);
            set.setToken(token);
            set.setPtzUrl(camera.getPtzUrl());
            set.setProfile(camera.getProfileMain());
            request.setSetPreset(set);
            request.setAddress(camera.getCmsResCameraInfo().getIp());
            request.setCommand("getPresets");
            request.setPort(String.valueOf(camera.getCmsResCameraInfo().getPort()));
            request.setUser(camera.getCmsResCameraInfo().getUsername());
            request.setPassword(camera.getCmsResCameraInfo().getPassword());
            request.setVersion(1);
            request.setUuid(UUID.randomUUID().toString());
        } else {
            log.error("根据code为" + cameraCode + "查询到的摄像机结果为空");
            return null;
        }
        return request;
    }
}