package shenzhen.teamway.service;

import shenzhen.teamway.model.CameraBean;
import shenzhen.teamway.pojo.CommandResultMessage;
import shenzhen.teamway.pojo.SetPrestMessageRequest;

/**
 * @program: onvifservice
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-03-06 11:06
 **/
public interface CmeraSetPreset {

    public SetPrestMessageRequest setPrestMessageRequest(String token, String cameraCode, String presetName);

    public CameraBean setUDPPrestMessageRequest(String ip, Integer index, String presetName);

    public CommandResultMessage gotoPreset(String cameraCode, String token);


}