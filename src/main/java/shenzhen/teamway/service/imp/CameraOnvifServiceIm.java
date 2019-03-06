package shenzhen.teamway.service.imp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
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
@PropertySource(value = "classpath:application.properties")
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
        final List<CmsResCameraOnvifInfo> cmsResCameraOnvifInfos = cmsResCameraOnvifInfoMapper.selectCamera(null);
        return cmsResCameraOnvifInfos;
    }

    @Scheduled(fixedDelay = 100000)
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
    @Scheduled(fixedDelay = 5000)
    public void setPresent() {
        final CmsResCameraOnvifInfo camera = q.poll();
        if (camera != null) {
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
            request.setUuid(uuid);
            final String s = redisUtils.postSend(url + "getPresets", request);
            final GetPresetsMessageResponse getPresetsMessageResponse = redisUtils.string2Object(s, GetPresetsMessageResponse.class);
            final String uuid2 = getPresetsMessageResponse.getUuid();
            if (uuid.equals(uuid2)) {
                //执行删除操作
                cmsResCameraPresetInfoMapper.deleteByCode(cameraCode);
                //插入数据库
                final List<CmsResCameraPresetInfo> cmsResCameraPresetInfos = getCmsResCameraPresetInfos(getPresetsMessageResponse, cameraCode);
                for (CmsResCameraPresetInfo cmsResCameraPresetInfo : cmsResCameraPresetInfos) {
                    final int insert = cmsResCameraPresetInfoMapper.insert(cmsResCameraPresetInfo);
                    if (insert == 1) {
                        log.info("成功插入数据库" + insert + "条消息");
                    } else {
                        log.error("插入数据库失败");
                    }
                }
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

    public Queue<CmsResCameraOnvifInfo> getQ() {
        return q;
    }

    public void setQ(Queue<CmsResCameraOnvifInfo> q) {
        this.q = q;
    }
}