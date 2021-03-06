package shenzhen.teamway.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import shenzhen.teamway.model.CmsResCameraOnvifInfo;

public interface CmsResCameraOnvifInfoMapper {
    int insert(CmsResCameraOnvifInfo record);

    List<CmsResCameraOnvifInfo> selectAll();

    List<CmsResCameraOnvifInfo> selectCamera(@Param("code") String code,@Param("ip") String ip);

    CmsResCameraOnvifInfo selectOnvifCamera(@Param("code") String code);

    int insertAndUpdate(CmsResCameraOnvifInfo record);
}