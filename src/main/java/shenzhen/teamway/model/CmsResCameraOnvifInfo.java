package shenzhen.teamway.model;

import java.io.Serializable;
import java.util.Date;

public class CmsResCameraOnvifInfo implements Serializable {
    private Integer id;

    private String cameraCode;

    private String deviceUrl;

    private String mediaUrl;

    private String rtspUrl;

    private String ptzUrl;

    private String profileMain;

    private String profileSub;

    private String profileTest;

    private Date updateTime;
    private CmsResCameraInfo cmsResCameraInfo;//一对一

    public CmsResCameraInfo getCmsResCameraInfo() {
        return cmsResCameraInfo;
    }

    public void setCmsResCameraInfo(CmsResCameraInfo cmsResCameraInfo) {
        this.cmsResCameraInfo = cmsResCameraInfo;
    }

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCameraCode() {
        return cameraCode;
    }

    public void setCameraCode(String cameraCode) {
        this.cameraCode = cameraCode == null ? null : cameraCode.trim();
    }

    public String getDeviceUrl() {
        return deviceUrl;
    }

    public void setDeviceUrl(String deviceUrl) {
        this.deviceUrl = deviceUrl == null ? null : deviceUrl.trim();
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl == null ? null : mediaUrl.trim();
    }

    public String getRtspUrl() {
        return rtspUrl;
    }

    public void setRtspUrl(String rtspUrl) {
        this.rtspUrl = rtspUrl == null ? null : rtspUrl.trim();
    }

    public String getPtzUrl() {
        return ptzUrl;
    }

    public void setPtzUrl(String ptzUrl) {
        this.ptzUrl = ptzUrl == null ? null : ptzUrl.trim();
    }

    public String getProfileMain() {
        return profileMain;
    }

    public void setProfileMain(String profileMain) {
        this.profileMain = profileMain == null ? null : profileMain.trim();
    }

    public String getProfileSub() {
        return profileSub;
    }

    public void setProfileSub(String profileSub) {
        this.profileSub = profileSub == null ? null : profileSub.trim();
    }

    public String getProfileTest() {
        return profileTest;
    }

    public void setProfileTest(String profileTest) {
        this.profileTest = profileTest == null ? null : profileTest.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
        CmsResCameraOnvifInfo other = (CmsResCameraOnvifInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getCameraCode() == null ? other.getCameraCode() == null : this.getCameraCode().equals(other.getCameraCode()))
                && (this.getDeviceUrl() == null ? other.getDeviceUrl() == null : this.getDeviceUrl().equals(other.getDeviceUrl()))
                && (this.getMediaUrl() == null ? other.getMediaUrl() == null : this.getMediaUrl().equals(other.getMediaUrl()))
                && (this.getRtspUrl() == null ? other.getRtspUrl() == null : this.getRtspUrl().equals(other.getRtspUrl()))
                && (this.getPtzUrl() == null ? other.getPtzUrl() == null : this.getPtzUrl().equals(other.getPtzUrl()))
                && (this.getProfileMain() == null ? other.getProfileMain() == null : this.getProfileMain().equals(other.getProfileMain()))
                && (this.getProfileSub() == null ? other.getProfileSub() == null : this.getProfileSub().equals(other.getProfileSub()))
                && (this.getProfileTest() == null ? other.getProfileTest() == null : this.getProfileTest().equals(other.getProfileTest()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCameraCode() == null) ? 0 : getCameraCode().hashCode());
        result = prime * result + ((getDeviceUrl() == null) ? 0 : getDeviceUrl().hashCode());
        result = prime * result + ((getMediaUrl() == null) ? 0 : getMediaUrl().hashCode());
        result = prime * result + ((getRtspUrl() == null) ? 0 : getRtspUrl().hashCode());
        result = prime * result + ((getPtzUrl() == null) ? 0 : getPtzUrl().hashCode());
        result = prime * result + ((getProfileMain() == null) ? 0 : getProfileMain().hashCode());
        result = prime * result + ((getProfileSub() == null) ? 0 : getProfileSub().hashCode());
        result = prime * result + ((getProfileTest() == null) ? 0 : getProfileTest().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
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
        sb.append(", deviceUrl=").append(deviceUrl);
        sb.append(", mediaUrl=").append(mediaUrl);
        sb.append(", rtspUrl=").append(rtspUrl);
        sb.append(", ptzUrl=").append(ptzUrl);
        sb.append(", profileMain=").append(profileMain);
        sb.append(", profileSub=").append(profileSub);
        sb.append(", profileTest=").append(profileTest);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}