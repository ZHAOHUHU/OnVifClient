package shenzhen.teamway.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import shenzhen.teamway.config.UrlRequestMapping;
import shenzhen.teamway.mapper.CmsResCameraOnvifInfoMapper;
import shenzhen.teamway.model.CmsResCameraInfo;
import shenzhen.teamway.model.CmsResCameraOnvifInfo;
import shenzhen.teamway.pojo.GetPresetsMessageRequest;
import shenzhen.teamway.pojo.GetPresetsMessageResponse;
import shenzhen.teamway.pojo.GotoPresetMessageRequest;
import shenzhen.teamway.pojo.request.GetPresetsRequestBody;
import shenzhen.teamway.util.RedisUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

/**
 * @program: onvifservice
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-03-04 10:15
 **/
@Service
public class CameraOnvifServiceImp implements CameraOnvifService {
    private Queue<GetPresetsMessageRequest> q = new LinkedList();
    private static final int delayTime = 1000 * 60;

    Logger log = LoggerFactory.getLogger(CameraOnvifServiceImp.class);
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    CmsResCameraOnvifInfoMapper cmsResCameraOnvifInfoMapper;

    /**
     * @Author: Zhao Hong Ning
     * @Description:获取所有摄像机的ptzurl
     * @Date: 2019/3/4
     * @param:
     * @return: java.util.List<shenzhen.teamway.model.CmsResCameraOnvifInfo>
     */
    @Override
    public List<CmsResCameraOnvifInfo> getAllCamera() {
        final List<CmsResCameraOnvifInfo> cmsResCameraOnvifInfos = cmsResCameraOnvifInfoMapper.selectCamera(null);
        return cmsResCameraOnvifInfos;
    }

    @Scheduled(fixedDelay = delayTime)
    public void getPresentFromDB() {
        final List<CmsResCameraOnvifInfo> cameras = getAllCamera();
        for (CmsResCameraOnvifInfo camera : cameras) {
            GetPresetsMessageRequest request = new GetPresetsMessageRequest();
            GetPresetsRequestBody body = new GetPresetsRequestBody();
            body.setProfile(camera.getProfileMain());
            body.setPtzUrl(camera.getPtzUrl());
            request.setGetPresets(body);
            request.setAddress(camera.getCmsResCameraInfo().getIp());
            request.setCommand("getPresets");
            request.setPort(String.valueOf(camera.getCmsResCameraInfo().getPort()));
            request.setUser(camera.getCmsResCameraInfo().getUsername());
            request.setPassword(camera.getCmsResCameraInfo().getPassword());
            request.setVersion(1);
            q.offer(request);
        }
    }

    /**
     * @Author: Zhao Hong Ning
     * @Description:定时发送预置位的请求信息
     * @Date: 2019/3/4
     * @param:
     * @return: void
     */
    @Scheduled(fixedDelay = delayTime)
    public void setPresent() {
        final String uuid = UUID.randomUUID().toString();
        final GetPresetsMessageRequest request = q.poll();
        request.setUuid(uuid);
        if (request != null) {
            final String s = redisUtils.postSend(UrlRequestMapping.getPresentUrl, request);
            final GetPresetsMessageResponse getPresetsMessageResponse = redisUtils.string2Object(s, GetPresetsMessageResponse.class);
            final String uuid2 = getPresetsMessageResponse.getUuid();
            if (uuid.equals(uuid2)) {
                //更新数据库
System.gc();
            }
            log.info(getPresetsMessageResponse.toString());
        }
    }
}