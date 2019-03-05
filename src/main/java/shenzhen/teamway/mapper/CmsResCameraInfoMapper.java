package shenzhen.teamway.mapper;

import java.util.List;
import shenzhen.teamway.model.CmsResCameraInfo;

public interface CmsResCameraInfoMapper {
    int insert(CmsResCameraInfo record);

    List<CmsResCameraInfo> selectAll();
}