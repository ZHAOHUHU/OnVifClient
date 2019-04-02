package shenzhen.teamway.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import shenzhen.teamway.model.CameraBean;
import shenzhen.teamway.model.CmsResCameraInfo;

public interface CmsResCameraInfoMapper {
    int insert(CmsResCameraInfo record);

    List<CmsResCameraInfo> selectAll();

    CmsResCameraInfo selectByIP(@Param("ip") String ip);

    //a.ip = #{ip}
    //    AND a. CODE = b.camera_code  = c.camera_code and c.preset_index=#{index} and c.`name`=#{presetName}
    CameraBean selectByNameByipByPresentIndex(@Param("ip") String ip, @Param("index") Integer index, @Param("presetName") String presetName);


}