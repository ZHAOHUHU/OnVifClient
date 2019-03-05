package shenzhen.teamway.model;

import java.io.Serializable;
import java.util.Date;

public class CmsResCameraPresetInfo implements Serializable {
    private Long id;

    private String cameraCode;

    private Integer presetIndex;

    private String name;

    private Integer enable;

    private Date gmtCreate;

    private Date gmtModified;

    private String remark;

    private String presetToken;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCameraCode() {
        return cameraCode;
    }

    public void setCameraCode(String cameraCode) {
        this.cameraCode = cameraCode == null ? null : cameraCode.trim();
    }

    public Integer getPresetIndex() {
        return presetIndex;
    }

    public void setPresetIndex(Integer presetIndex) {
        this.presetIndex = presetIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getPresetToken() {
        return presetToken;
    }

    public void setPresetToken(String presetToken) {
        this.presetToken = presetToken == null ? null : presetToken.trim();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        CmsResCameraPresetInfo other = (CmsResCameraPresetInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCameraCode() == null ? other.getCameraCode() == null : this.getCameraCode().equals(other.getCameraCode()))
            && (this.getPresetIndex() == null ? other.getPresetIndex() == null : this.getPresetIndex().equals(other.getPresetIndex()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getEnable() == null ? other.getEnable() == null : this.getEnable().equals(other.getEnable()))
            && (this.getGmtCreate() == null ? other.getGmtCreate() == null : this.getGmtCreate().equals(other.getGmtCreate()))
            && (this.getGmtModified() == null ? other.getGmtModified() == null : this.getGmtModified().equals(other.getGmtModified()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getPresetToken() == null ? other.getPresetToken() == null : this.getPresetToken().equals(other.getPresetToken()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCameraCode() == null) ? 0 : getCameraCode().hashCode());
        result = prime * result + ((getPresetIndex() == null) ? 0 : getPresetIndex().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getEnable() == null) ? 0 : getEnable().hashCode());
        result = prime * result + ((getGmtCreate() == null) ? 0 : getGmtCreate().hashCode());
        result = prime * result + ((getGmtModified() == null) ? 0 : getGmtModified().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getPresetToken() == null) ? 0 : getPresetToken().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", cameraCode=").append(cameraCode);
        sb.append(", presetIndex=").append(presetIndex);
        sb.append(", name=").append(name);
        sb.append(", enable=").append(enable);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", gmtModified=").append(gmtModified);
        sb.append(", remark=").append(remark);
        sb.append(", presetToken=").append(presetToken);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}