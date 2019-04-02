package shenzhen.teamway.service.imp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shenzhen.teamway.config.UrlRequestMapping;
import shenzhen.teamway.mapper.CmsResCameraOnvifInfoMapper;
import shenzhen.teamway.mapper.CmsResCameraPresetInfoMapper;
import shenzhen.teamway.model.CmsResCameraOnvifInfo;
import shenzhen.teamway.model.CmsResCameraPresetInfo;
import shenzhen.teamway.pojo.GetPresetsMessageRequest;
import shenzhen.teamway.pojo.GetPresetsMessageResponse;
import shenzhen.teamway.pojo.request.GetPresetsRequestBody;
import shenzhen.teamway.pojo.response.PresetInfo;
import shenzhen.teamway.service.CameraOnvifService;
import shenzhen.teamway.util.OtherUtils;
import shenzhen.teamway.util.RedisUtils;

import java.util.*;

/**
 * @program: onvifservice
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-03-04 10:15
 **/
@Service

public class CameraOnvifServiceIm implements CameraOnvifService {
    private Queue<CmsResCameraOnvifInfo> q = new LinkedList();
    @Value("${request.url}")
    private String url;

    Logger log = LoggerFactory.getLogger(CameraOnvifServiceIm.class);

    @Autowired
    CmsResCameraPresetInfoMapper cmsResCameraPresetInfoMapper;
    @Autowired
    OtherUtils otherUtils;
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
        final List<CmsResCameraOnvifInfo> cmsResCameraOnvifInfos = cmsResCameraOnvifInfoMapper.selectCamera(null, null);
        return cmsResCameraOnvifInfos;
    }

    /**
     * @Author: Zhao Hong Ning
     * @Description: //每天23点27分50秒时执行
     * @Scheduled(cron = "50 27 23 * * ?")
     * @Date: 2019/3/7
     * @param:
     * @return: void
     */
    @Scheduled(cron = "${getPresentFromDB}")
    public void getPresentFromDB() {
        final List<CmsResCameraOnvifInfo> cameras = getAllCamera();
        for (CmsResCameraOnvifInfo camera : cameras) {
            q.offer(camera);
        }
    }

    /**
     * @Author: Zhao Hong Ning
     * @Description:定时发送预置位的请求信息
     * @Date: 2019/3/4
     * @param:
     * @return: void
     */


    @Scheduled(fixedDelay = 10000)
    public void getPresent() {
        final CmsResCameraOnvifInfo camera = q.poll();
        if (camera != null) {
            final GetPresetsMessageRequest request = getPresetsMessageRequest(camera);
            if (request == null) {
                return;
            }
            final String s = redisUtils.postSend(url + "getPresets", request);
            final GetPresetsMessageResponse getPresetsMessageResponse = redisUtils.string2Object(s, GetPresetsMessageResponse.class);
            final String uuid2 = getPresetsMessageResponse.getUuid();
            if (request.getUuid().equals(uuid2)) {
                deleteAndInsert(camera.getCameraCode(), getPresetsMessageResponse);

            } else {
                log.error("返回的uuid不一致");
            }
            log.info(getPresetsMessageResponse.toString());
        }
    }


    public List<CmsResCameraPresetInfo> getCmsResCameraPresetInfos(GetPresetsMessageResponse g, String cameraCode) {
        List<CmsResCameraPresetInfo> list = new ArrayList<>();
        final List<PresetInfo> presets = g.getGetPresetsResp().getPresets();
        for (int i = 0; i < presets.size(); i++) {
            final CmsResCameraPresetInfo info = new CmsResCameraPresetInfo();
            info.setCameraCode(cameraCode);
            info.setGmtCreate(new Date());
            info.setGmtModified(new Date());
            info.setPresetIndex(i);
            info.setName(presets.get(i).getName());
            info.setPresetToken(presets.get(i).getToken());
            list.add(info);
        }
        return list;
    }

    /**
     * @Author: Zhao Hong Ning
     * @Description:封装请求体
     * @Date: 2019/3/7
     * @param: camera
     * @return: shenzhen.teamway.pojo.GetPresetsMessageRequest
     */
    public GetPresetsMessageRequest getPresetsMessageRequest(CmsResCameraOnvifInfo camera) {
        GetPresetsMessageRequest request = new GetPresetsMessageRequest();
        GetPresetsRequestBody body = new GetPresetsRequestBody();
        final String uuid = UUID.randomUUID().toString();
        if (camera.getProfileMain() != null && camera.getPtzUrl() != null) {
            body.setProfile(camera.getProfileMain());
            body.setPtzUrl(camera.getPtzUrl());
            request.setGetPresets(body);
            request.setAddress(camera.getCmsResCameraInfo().getIp());
            request.setCommand("getPresets");
            request.setPort(String.valueOf(camera.getCmsResCameraInfo().getPort()));
            request.setUser(camera.getCmsResCameraInfo().getUsername());
            request.setPassword(camera.getCmsResCameraInfo().getPassword());
            request.setVersion(1);
            request.setUuid(uuid);
            return request;
        } else {
            return null;
        }
    }

    public Queue<CmsResCameraOnvifInfo> getQ() {
        return q;
    }

    public void setQ(Queue<CmsResCameraOnvifInfo> q) {
        this.q = q;
    }

    /**
     * @Author: Zhao Hong Ning
     * @Description:就是执行更新操作有责更新无则插入
     * @Date: 2019/4/1
     * @param: code
     * @param: getPresetsMessageResponse
     * @return: void
     */
    @Transactional
    public void deleteAndInsert(String code, GetPresetsMessageResponse getPresetsMessageResponse) {
        //执行删除操作
        final int i = cmsResCameraPresetInfoMapper.deleteByCode(code);
        log.info("成功删除了数据的条数：" + i);
        //插入数据库
        final List<CmsResCameraPresetInfo> cmsResCameraPresetInfos = getCmsResCameraPresetInfos(getPresetsMessageResponse, code);
        for (CmsResCameraPresetInfo cmsResCameraPresetInfo : cmsResCameraPresetInfos) {
            final int insert = cmsResCameraPresetInfoMapper.insert(cmsResCameraPresetInfo);
            if (insert == 1) {
                log.info("成功插入数据库" + insert + "条消息");
            } else {
                log.error("插入数据库失败");
            }
        }
    }
}