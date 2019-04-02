package shenzhen.teamway.model;

/**
 * @program: onvifservice
 * @description:三张表的自定义封装对象
 * @author: Zhao Hong Ning
 * @create: 2019-03-11 09:26
 **/
public class CameraBean {
    /*
    	a.ip,a.`name`,a.`password`,a.ptz_url, b.profile_main,
	c.preset_token,c.preset_index,c.name
     */
    private String ip;
    private String userName;
    private String password;
    private Integer port;
    private String ptzUrl;
    private String profileMain;
    private String presetToken;
    private Integer presetIndex;
    private String name;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPtzUrl() {
        return ptzUrl;
    }

    public void setPtzUrl(String ptzUrl) {
        this.ptzUrl = ptzUrl;
    }

    public String getProfileMain() {
        return profileMain;
    }

    public void setProfileMain(String profileMain) {
        this.profileMain = profileMain;
    }

    public String getPresetToken() {
        return presetToken;
    }

    public void setPresetToken(String presetToken) {
        this.presetToken = presetToken;
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
        this.name = name;
    }
}