package cloud.aquacloud.aquacloudapi.type;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class NewVideoRepresentation {
    private String description;
    private MultipartFile file;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getDescription() {
        return description;
    }

    public MultipartFile getFile() {
        return file;
    }
}