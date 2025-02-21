package cloud.aquacloud.aquacloudapi.type;

import cloud.aquacloud.aquacloudapi.utils.NanoId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table()
@Data
@NoArgsConstructor
public class VideoMetadata {
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name = "id", type = NanoId.class)
    private String id;
    private double fps;
    private String fileName;
    private String contentType;
    private String description;
    private Long fileSize;
    private Long videoLength;


    public String getContentType() {
        return contentType;
    }

    public String getDescription() {
        return description;
    }

    public double getFps() {
        return fps;
    }

    public void setFps(double fps) {
        this.fps = fps;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public String getId() {
        return id;
    }

    public Long getVideoLength() {
        return videoLength;
    }

    public String getFileName() {
        return fileName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public void setVideoLength(Long videoLength) {
        this.videoLength = videoLength;
    }
}
