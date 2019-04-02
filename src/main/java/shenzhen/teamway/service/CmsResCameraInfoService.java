package shenzhen.teamway.service;

import shenzhen.teamway.pojo.CommandResultMessage;

/**
 * @program: onvifservice
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-03-08 10:55
 **/
public interface CmsResCameraInfoService {

     CommandResultMessage getResult(String ip);
}