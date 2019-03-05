package shenzhen.teamway.mapper;

import java.util.List;
import shenzhen.teamway.model.CmsResCameraPresetInfo;

public interface CmsResCameraPresetInfoMapper {
    int insert(CmsResCameraPresetInfo record);

    List<CmsResCameraPresetInfo> selectAll();
}