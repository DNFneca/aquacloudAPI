package cloud.aquacloud.aquacloudapi.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

public class StreamBytesInfo {

    private final StreamingResponseBody responseBody;
    private final long fileSize;
    private final long rangeStart;
    private final long rangeEnd;
    private final String contentType;

    public StreamBytesInfo(StreamingResponseBody responseBody, long fileSize, long rangeStart, long rangeEnd, String contentType) {
        this.responseBody = responseBody;
        this.fileSize = fileSize;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.contentType = contentType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public long getRangeEnd() {
        return rangeEnd;
    }

    public long getRangeStart() {
        return rangeStart;
    }

    public StreamingResponseBody getResponseBody() {
        return responseBody;
    }

    public String getContentType() {
        return contentType;
    }
}