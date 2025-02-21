package cloud.aquacloud.aquacloudapi.type;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VideoMetadataRepresentation {
    private String id;
    private double fps;
    private String description;
    private String contentType;
    private String previewUrl;
    private String streamUrl;

    public VideoMetadataRepresentation (VideoMetadata vmd) {
        this.id = vmd.getId();
        this.fps = vmd.getFps();
        this.description = vmd.getDescription();
        this.contentType = vmd.getContentType();
        this.setPreviewUrl("/content/preview/" + vmd.getId());
        this.setStreamUrl("/content/stream/" + vmd.getId());
    }


    public String getDescription() {
        return description;
    }

    public String getContentType() {
        return contentType;
    }

    public double getFps() {
        return fps;
    }

    public void setFps(double fps) {
        this.fps = fps;
    }

    public String getId() {
        return id;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }
}