package cn.yescallop.aid.video.device.dshow;

/**
 * @author Scallop Ye
 */
public class DshowDevice {

    private final String name;
    private final String alternativeName;
    private final boolean audioOnly;

    DshowDevice(String name, String alternativeName, boolean audioOnly) {
        this.name = name;
        this.alternativeName = alternativeName;
        this.audioOnly = audioOnly;
    }

    public String name() {
        return name;
    }

    public String alternativeName() {
        return alternativeName;
    }

    public boolean isAudioOnly() {
        return audioOnly;
    }

    @Override
    public String toString() {
        return "DshowDevice{" +
                "name='" + name + '\'' +
                ", alternativeName='" + alternativeName + '\'' +
                ", audioOnly=" + audioOnly +
                '}';
    }
}
