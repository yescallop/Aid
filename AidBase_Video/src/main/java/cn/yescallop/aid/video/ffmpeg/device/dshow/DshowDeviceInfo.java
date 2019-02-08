package cn.yescallop.aid.video.ffmpeg.device.dshow;

/**
 * @author Scallop Ye
 */
public class DshowDeviceInfo {

    private final String friendlyName;
    private final String uniqueName;
    private final boolean audioOnly;

    DshowDeviceInfo(String friendlyName, String uniqueName, boolean audioOnly) {
        this.friendlyName = friendlyName;
        this.uniqueName = uniqueName;
        this.audioOnly = audioOnly;
    }

    public String friendlyName() {
        return friendlyName;
    }

    public String uniqueName() {
        return uniqueName;
    }

    public boolean isAudioOnly() {
        return audioOnly;
    }

    public String type() {
        return audioOnly ? "audio" : "video";
    }
}
