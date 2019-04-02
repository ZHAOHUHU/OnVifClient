package shenzhen.teamway.service.imp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shenzhen.teamway.mapper.CmsResCameraInfoMapper;
import shenzhen.teamway.mapper.CmsResCameraOnvifInfoMapper;
import shenzhen.teamway.model.CameraBean;
import shenzhen.teamway.model.CmsResCameraOnvifInfo;
import shenzhen.teamway.pojo.CommandResultMessage;
import shenzhen.teamway.pojo.GotoPresetMessageRequest;
import shenzhen.teamway.pojo.SetPrestMessageRequest;
import shenzhen.teamway.pojo.request.GotoPresetRequestBody;
import shenzhen.teamway.pojo.request.SetPresetRequestBody;
import shenzhen.teamway.service.CmeraSetPreset;
import shenzhen.teamway.util.OtherUtils;
import shenzhen.teamway.util.RedisUtils;

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
    Logger log = LoggerFactory.getLogger(CmeraSetPresetImp.class);
    @Value("${request.url}")
    private String url;
    @Autowired
    CmsResCameraInfoMapper cmsResCameraInfoMapper;
    @Autowired
    CmsResCameraOnvifInfoMapper cmsResCameraOnvifInfoMapper;
    @Autowired
    private RedisUtils redisUtils;

    public SetPrestMessageRequest setPrestMessageRequest(String token, String cameraCode, String presetName) {
        SetPrestMessageRequest request = new SetPrestMessageRequest();
        final List<CmsResCameraOnvifInfo> cmsResCameraOnvifInfos = cmsResCameraOnvifInfoMapper.selectCamera(cameraCode, null);
        if (cmsResCameraOnvifInfos.size() == 1) {
            CmsResCameraOnvifInfo camera = cmsResCameraOnvifInfos.get(0);
            SetPresetRequestBody set = new SetPresetRequestBody();
            set.setName(presetName);
            set.setToken(token);
            set.setPtzUrl(camera.getPtzUrl());
            set.setProfile(camera.getProfileMain());
            request.setSetPreset(set);
            request.setAddress(camera.getCmsResCameraInfo().getIp());
            request.setCommand("setPresets");
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

    /**
     * @Author: Zhao Hong Ning
     * @Description:走UDP的协议的设置预置位
     * @Date: 2019/3/11
     * @param: ip
     * @param: index
     * @param: presetName
     * @return: shenzhen.teamway.model.CameraBean
     */
    @Override
    public CameraBean setUDPPrestMessageRequest(String ip, Integer index, String presetName) {
        final CameraBean cameraBean = cmsResCameraInfoMapper.selectByNameByipByPresentIndex(ip, index, presetName);
        return cameraBean;
    }

    public CommandResultMessage gotoPreset(String cameraCode, String token) {
        final List<CmsResCameraOnvifInfo> cmsResCameraOnvifInfos = cmsResCameraOnvifInfoMapper.selectCamera(cameraCode, null);
        GotoPresetMessageRequest request = new GotoPresetMessageRequest();
        GotoPresetRequestBody set = new GotoPresetRequestBody();
        if (cmsResCameraOnvifInfos.size() == 1) {
            CmsResCameraOnvifInfo camera = cmsResCameraOnvifInfos.get(0);
            set.setPtzUrl(camera.getPtzUrl());
            set.setProfile(camera.getProfileMain());
            set.setToken(token);
            request.setGotoPreset(set);
            request.setAddress(camera.getCmsResCameraInfo().getIp());
            request.setCommand("gotoPresets");
            request.setPort(String.valueOf(camera.getCmsResCameraInfo().getPort()));
            request.setUser(camera.getCmsResCameraInfo().getUsername());
            request.setPassword(camera.getCmsResCameraInfo().getPassword());
            request.setVersion(1);
            request.setUuid(UUID.randomUUID().toString());
            final String s = redisUtils.postSend(url + "gotoPresets", request);
            OtherUtils.sleepThread(1000);
            final CommandResultMessage commandResultMessage = redisUtils.string2Object(s, CommandResultMessage.class);
            return commandResultMessage;
        } else {
            log.error("根据code为" + cameraCode + "查询到的摄像机结果为空或者不唯一");
            return null;
        }

    }
}