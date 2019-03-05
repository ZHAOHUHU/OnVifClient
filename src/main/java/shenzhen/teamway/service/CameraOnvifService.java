package shenzhen.teamway.service;

import shenzhen.teamway.model.CmsResCameraOnvifInfo;

import java.util.List;

/**
 * @program: onvifservice
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-03-04 10:14
 **/
public interface CameraOnvifService {

    List<CmsResCameraOnvifInfo> getAllCamera();
}