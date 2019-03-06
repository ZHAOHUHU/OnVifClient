package shenzhen.teamway.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import shenzhen.teamway.model.CmsResCameraPresetInfo;

public interface CmsResCameraPresetInfoMapper {
    int insert(CmsResCameraPresetInfo record);

    List<CmsResCameraPresetInfo> selectAll();

    List<CmsResCameraPresetInfo> selectCount(@Param("code") String code);

    int deleteByCode(@Param("code") String code);
}