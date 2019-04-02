package shenzhen.teamway.nettyserver;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import shenzhen.teamway.mapper.CmsResCameraInfoMapper;
import shenzhen.teamway.mapper.CmsResCameraOnvifInfoMapper;
import shenzhen.teamway.mapper.CmsResCameraPresetInfoMapper;
import shenzhen.teamway.model.CameraBean;
import shenzhen.teamway.model.CmsResCameraInfo;
import shenzhen.teamway.model.CmsResCameraOnvifInfo;
import shenzhen.teamway.pojo.*;
import shenzhen.teamway.pojo.request.GotoPresetRequestBody;
import shenzhen.teamway.pojo.request.SetPresetRequestBody;
import shenzhen.teamway.service.CmeraSetPreset;
import shenzhen.teamway.service.imp.CameraOnvifServiceIm;
import shenzhen.teamway.util.OtherUtils;
import shenzhen.teamway.util.RedisUtils;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @program: onvifservice
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-03-07 10:35
 **/
@Component
public class CommondHanlder {
    @Value("${request.url}")
    private String url;
    @Autowired
    private CameraOnvifServiceIm cameraOnvifServiceIm;
    @Autowired
    CmsResCameraInfoMapper cmsResCameraInfoMapper;
    @Autowired
    CmsResCameraPresetInfoMapper cmsResCameraPresetInfoMapper;
    @Autowired
    CmeraSetPreset cmeraSetPreset;
    @Autowired
    CmsResCameraOnvifInfoMapper cmsResCameraOnvifInfoMapper;
    @Autowired
    private RedisUtils redisUtils;
    Logger log = LoggerFactory.getLogger(CommondHanlder.class);

    public void chooseCommond(String c, ChannelHandlerContext ctx, DatagramPacket msg) throws IOException {
        String res = null;
        final String s = OtherUtils.getCommond(c);
        final ObjectMapper mapper = new ObjectMapper();
        if (s.equals("reboot")) {
            res = reboot(c);
        } else if (s.equals("setPreset")) {
            res = setPreset(c);
        } else if (s.equals("delPreset")) {
            res = delPreset(c);
        } else if (s.equals("gotoPreset")) {
            res = gotoPreset(c);
        } else if (s.equals("getPresets")) {
            res = getPresets(c);
        } else if (s.equals("getRtspUrl")) {
            res = getRtspUrl(c);
        }
        ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(res, CharsetUtil.UTF_8), msg.sender()));
    }

    private String getRtspUrl(String str) {
        final String ip = OtherUtils.getJsonValue(str, "cameraAddr");
        final List<CmsResCameraInfo> cmsResCameraInfos = cmsResCameraInfoMapper.selectAll();
        final GetPtzUrlMessageRequest request = new GetPtzUrlMessageRequest();
        final GetPtzUrlMessageRequest request1 = new GetPtzUrlMessageRequest();
        //request.setAddress(camera.getIp());
        //request.setCommand("getMediaProfile");
        //request.setPort(String.valueOf(camera.getPort()));
        //request.setUser(camera.getUsername());
        //request.setPassword(camera.getPassword());
        request.setVersion(1);
        request.setUuid(UUID.randomUUID().toString());
        request.setGetPtzUrl(request1);
        final String s = redisUtils.postSend(url + "getgetPtzUrl", request);

        return ip;
    }

    private String getPresets(String str) {
        final String ip = OtherUtils.getJsonValue(str, "cameraAddr");
        final List<CmsResCameraOnvifInfo> cmsResCameraOnvifInfos = cmsResCameraOnvifInfoMapper.selectCamera(null, ip);
        GetPresetsMessageRequest request = null;
        String s = null;
        if (cmsResCameraOnvifInfos.size() == 1) {
            request = cameraOnvifServiceIm.getPresetsMessageRequest(cmsResCameraOnvifInfos.get(0));
            redisUtils.postSend(url + "getPresets", request);
        } else {
            log.error("根据IP：" + ip + "查询到的结果不唯一或者为空");
        }
        return s;
    }

    public String setPreset(String str) {
        Integer presetIndex = Integer.valueOf(OtherUtils.getJsonValue(str, "presetIndex"));
        final String name = OtherUtils.getJsonValue(str, "name");
        final String cameraAddr = OtherUtils.getJsonValue(str, "cameraAddr");
        final CameraBean c = cmeraSetPreset.setUDPPrestMessageRequest(cameraAddr, presetIndex, name);
        SetPrestMessageRequest request = new SetPrestMessageRequest();
        SetPresetRequestBody set = new SetPresetRequestBody();
        set.setName(c.getName());
        set.setToken(c.getPresetToken());
        set.setPtzUrl(c.getPtzUrl());
        set.setProfile(c.getProfileMain());
        request.setSetPreset(set);
        request.setAddress(c.getIp());
        request.setCommand("setPresets");
        request.setPort(String.valueOf(c.getPort()));
        request.setUser(c.getUserName());
        request.setPassword(c.getPassword());
        request.setVersion(1);
        request.setUuid(UUID.randomUUID().toString());
        final String s = redisUtils.postSend(url + request.getCommand(), CommandResultMessage.class);
        return s;
    }

    public String reboot(String str) {
        final String ip = OtherUtils.getJsonValue(str, "cameraAddr");
        final CmsResCameraInfo cmsResCameraInfo = cmsResCameraInfoMapper.selectByIP(ip);
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
        return redisUtils.postSend(url + request.getCommand(), request);
    }

    public String delPreset(String str) {
        final String ip = OtherUtils.getJsonValue(str, "cameraAddr");
        final Integer presetIndex = Integer.valueOf(OtherUtils.getJsonValue(str, "presetIndex"));
        final CmsResCameraInfo cmsResCameraInfo = cmsResCameraInfoMapper.selectByIP(ip);
        final String code = cmsResCameraInfo.getCode();
        final int i = cmsResCameraPresetInfoMapper.deleteByCodeAndIndex(code, presetIndex);
        boolean flag = false;
        String temp = null;
        final CommandResultMessage message = new CommandResultMessage();
        if (i == 1) {
            log.info("成功删除了" + i + "条数据");
            flag = true;
            temp = "删除成功";
        } else {
            temp = "删除失败";
        }
        message.setResult(flag);
        message.setMessage(temp);
        final String json = JSON.toJSONString(message);
        return json;
    }

    public String gotoPreset(String str) {
        final String ip = OtherUtils.getJsonValue(str, "cameraAddr");
        final Integer presetIndex = Integer.valueOf(OtherUtils.getJsonValue(str, "presetIndex"));
        final CameraBean camera = cmsResCameraInfoMapper.selectByNameByipByPresentIndex(ip, presetIndex, null);
        GotoPresetMessageRequest request = new GotoPresetMessageRequest();
        GotoPresetRequestBody set = new GotoPresetRequestBody();
        set.setPtzUrl(camera.getPtzUrl());
        set.setProfile(camera.getProfileMain());
        set.setToken(camera.getPresetToken());
        request.setGotoPreset(set);
        request.setAddress(camera.getIp());
        request.setCommand("gotoPresets");
        request.setPort(String.valueOf(camera.getPort()));
        request.setUser(camera.getUserName());
        request.setPassword(camera.getPassword());
        request.setVersion(1);
        request.setUuid(UUID.randomUUID().toString());
        final String s = redisUtils.postSend(url + request.getCommand(), request);
        return s;
    }

}