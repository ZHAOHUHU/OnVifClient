package shenzhen.teamway.onvifservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import shenzhen.teamway.OnvifserviceApplication;
import shenzhen.teamway.mapper.CmsResCameraOnvifInfoMapper;
import shenzhen.teamway.model.CmsResCameraOnvifInfo;
import shenzhen.teamway.service.CameraOnvifServiceImp;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OnvifserviceApplication.class)
public class OnvifserviceApplicationTests {
    @Autowired
    CameraOnvifServiceImp cameraOnvifServiceImp;
    @Autowired
    CmsResCameraOnvifInfoMapper cmsResCameraOnvifInfoMapper;

    @Test
    public void contextLoads() {
       cmsResCameraOnvifInfoMapper.selectCamera("sdfds");
    }

}
