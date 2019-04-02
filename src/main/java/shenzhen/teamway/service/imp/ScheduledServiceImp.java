package shenzhen.teamway.service.imp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shenzhen.teamway.controller.PresentSettingController;
import shenzhen.teamway.mapper.CmsResCameraInfoMapper;
import shenzhen.teamway.mapper.CmsResCameraOnvifInfoMapper;
import shenzhen.teamway.mapper.CmsResCameraPresetInfoMapper;
import shenzhen.teamway.model.CmsResCameraInfo;
import shenzhen.teamway.model.CmsResCameraOnvifInfo;
import shenzhen.teamway.model.CmsResCameraPresetInfo;
import shenzhen.teamway.pojo.*;
import shenzhen.teamway.util.OtherUtils;
import shenzhen.teamway.util.RedisUtils;

import java.util.*;

/**
 * @program: onvifservice
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-03-07 14:26
 **/
@Service

public class ScheduledServiceImp {
    Logger log = LoggerFactory.getLogger(ScheduledServiceImp.class);
    private Queue<CmsResCameraOnvifInfo> q = new LinkedList();
    @Value("${request.url}")
    private String url;
    @Autowired
    CmsResCameraInfoMapper cmsResCameraInfoMapper;
    @Autowired
    CmsResCameraOnvifInfoMapper cmsResCameraOnvifInfoMapper;
    @Autowired
    private RedisUtils redisUtils;

     @Scheduled(cron = "${getMediaUrl}")
    public void getMediaUrl() {
        log.info("开始执行" + "getMediaUrl====================");
        final List<CmsResCameraInfo> cmsResCameraInfos = cmsResCameraInfoMapper.selectAll();
        for (CmsResCameraInfo camera : cmsResCameraInfos) {
            final GetMediaUrlMessageRequest request = new GetMediaUrlMessageRequest();
            final GetMediaUrlMessageRequest request1 = new GetMediaUrlMessageRequest();
            request.setAddress(camera.getIp());
            request.setCommand("getMediaUrl");
            request.setPort(String.valueOf(camera.getPort()));
            request.setUser(camera.getUsername());
            request.setPassword(camera.getPassword());
            request.setVersion(1);
            request.setUuid(UUID.randomUUID().toString());
            request.setGetMediaUrl(request1);
            final String s = redisUtils.postSend(url + "getMediaUrl", request);
            final GetMediaUrlMessageResponse res = redisUtils.string2Object(s, GetMediaUrlMessageResponse.class);
            OtherUtils.sleepThread(1000);
            if (res != null && request.getUuid().equals(res.getUuid())) {
                final int i = updatemediaUrlDB(res, camera.getCode());
                log.info("执行成功了的条数为：" + i);
                log.info("服务返回结果是：" + res.toString());
            } else {
                log.error("两次返回的uuid不一致,请求是getMediaUrl" + request + "服务返回的结果是" + res);
            }
        }
        log.info("执行完毕" + "getMediaUrl====================");
    }

    @Transactional
    public int updatemediaUrlDB(GetMediaUrlMessageResponse res, String cameraCode) {
        final CmsResCameraOnvifInfo info = new CmsResCameraOnvifInfo();
        if (!res.getCommandResultMessage().isResult()) {
            return 0;
        } else {
            final String url = res.getGetMediaUrlResp().getMediaUrl();
            info.setMediaUrl(url);
            info.setCameraCode(cameraCode);
            info.setUpdateTime(new Date());
            final int i = cmsResCameraOnvifInfoMapper.insertAndUpdate(info);
            return i;
        }
    }

      @Scheduled(cron = "${getMediaprofile}")
    public void getMediaprofile() {
        log.info("开始执行" + "getMediaprofile====================");
        final List<CmsResCameraInfo> cmsResCameraInfos = cmsResCameraInfoMapper.selectAll();
        for (CmsResCameraInfo camera : cmsResCameraInfos) {
            final GetMediaProfileMessageRequest request = new GetMediaProfileMessageRequest();
            final GetMediaProfileMessageRequest request1 = new GetMediaProfileMessageRequest();
            request.setAddress(camera.getIp());
            request.setCommand("getMediaProfile");
            request.setPort(String.valueOf(camera.getPort()));
            request.setUser(camera.getUsername());
            request.setPassword(camera.getPassword());
            request.setVersion(1);
            request.setUuid(UUID.randomUUID().toString());
            request.setGetMediaProfile(request1);
            final String s = redisUtils.postSend(url + "getMediaProfile", request);
            OtherUtils.sleepThread(1000);
            final GetMediaProfileMessageResponse res = redisUtils.string2Object(s, GetMediaProfileMessageResponse.class);
            if (res != null && request.getUuid().equals(res.getUuid())) {
                final int i = updatemediaprofileDB(res, camera.getCode());
                log.info("执行成功了的条数为：" + i);
                log.info("服务返回结果是：" + res.toString());
            } else {
                log.error("两次返回的uuid不一致,请求是getMediaProfile" + request + "服务返回的结果是" + res);
            }
        }
        log.info("执行完毕" + "getMediaprofile====================");
    }

      @Transactional
    public int updatemediaprofileDB(GetMediaProfileMessageResponse res, String cameraCode) {
        final CmsResCameraOnvifInfo info = new CmsResCameraOnvifInfo();
        if (!res.getCommandResp().isResult()) {
            return 0;
        } else {
            final Integer profileNumber = res.getGetMediaProfileResp().getProfileNumber();
            final String[] profiles = res.getGetMediaProfileResp().getProfiles();
            if (profiles.length == 1) {
                info.setProfileMain(profiles[0]);
            }
            if (profiles.length == 2) {
                info.setProfileMain(profiles[0]);
                info.setProfileSub(profiles[1]);
            }
            if (profiles.length == 3) {
                info.setProfileMain(profiles[0]);
                info.setProfileSub(profiles[1]);
                info.setProfileTest(profiles[2]);
            }
            info.setCameraCode(cameraCode);
            info.setUpdateTime(new Date());
            final int i = cmsResCameraOnvifInfoMapper.insertAndUpdate(info);
            log.info("执行成功了的条数为：" + i);
            return i;
        }
    }

       @Scheduled(cron = "${getPtzUrl}")
    public void getgetPtzUrl() {
        final List<CmsResCameraInfo> cmsResCameraInfos = cmsResCameraInfoMapper.selectAll();
        for (CmsResCameraInfo camera : cmsResCameraInfos) {
            final GetPtzUrlMessageRequest request = new GetPtzUrlMessageRequest();
            final GetPtzUrlMessageRequest request1 = new GetPtzUrlMessageRequest();
            request.setAddress(camera.getIp());
            request.setCommand("getPtzUrl");
            request.setPort(String.valueOf(camera.getPort()));
            request.setUser(camera.getUsername());
            request.setPassword(camera.getPassword());
            request.setVersion(1);
            request.setUuid(UUID.randomUUID().toString());
            request.setGetPtzUrl(request1);
            final String s = redisUtils.postSend(url + "getPtzUrl", request);
            OtherUtils.sleepThread(1000);
            final GetPtzUrlMessageResponse res = redisUtils.string2Object(s, GetPtzUrlMessageResponse.class);
            if (res != null && request.getUuid().equals(res.getUuid())) {
                final int i = updatePtzurlDB(res, camera.getCode());
                log.info("执行成功了的条数为：" + i);
                log.info("服务返回结果是：" + res.toString());
            } else {
                log.error("两次返回的uuid不一致,请求是getPtzUrl" + request + "服务返回的结果是" + res);
            }
        }
    }

    @Transactional
    public int updatePtzurlDB(GetPtzUrlMessageResponse res, String cameraCode) {
        final CmsResCameraOnvifInfo info = new CmsResCameraOnvifInfo();
        if (!res.getCommandResp().isResult()) {
            return 0;
        } else {
            final String ptzUrl = res.getGetPtzUrlResp().getPtzUrl();
            info.setCameraCode(cameraCode);
            info.setUpdateTime(new Date());
            info.setPtzUrl(ptzUrl);
            final int i = cmsResCameraOnvifInfoMapper.insertAndUpdate(info);
            return i;
        }
    }

}