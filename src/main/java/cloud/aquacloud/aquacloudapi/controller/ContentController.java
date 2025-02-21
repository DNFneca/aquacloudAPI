package cloud.aquacloud.aquacloudapi.controller;

import cloud.aquacloud.aquacloudapi.service.*;
import cloud.aquacloud.aquacloudapi.type.*;
import cloud.aquacloud.aquacloudapi.utils.URLUtils;
import com.google.gson.Gson;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/content")
public class ContentController {

    private final ContentService contentService;
    private final UserService userService;
    private final FollowingService followingService;
    private final TokenService tokenService;
    private final VideoService videoService;

    public ContentController(ContentService contentService, UserService userService, FollowingService followingService, TokenService tokenService, VideoService videoService) {
        this.contentService = contentService;
        this.userService = userService;
        this.followingService = followingService;
        this.tokenService = tokenService;
        this.videoService = videoService;
    }

    @CrossOrigin
    @PostMapping("/create")
    public ResponseEntity<String> uploadImage(@RequestParam MultipartFile file, @RequestParam String tokenId, @RequestParam String fingerprint, @RequestParam AccessRequirement accessRequirement, @RequestParam(required = false) Long validUntil, @RequestParam(required = false) String[] userIds) {
        Optional<Token> token = tokenService.getTokenById(tokenId);
        if (!token.get().getUserFingerprint().equals(fingerprint)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Gson().toJson("fingerprint invalid"));
        User user = token.get().getUser();
        try {
            contentService.saveUploadedContent(file, user, accessRequirement);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Gson().toJson("Internal Server Error"));
        }
        return ResponseEntity.ok().body(new Gson().toJson("Image uploaded successfully"));
    }

    @Async
    @CrossOrigin
    @PostMapping("/watch")
    public CompletableFuture<ResponseEntity<Resource>> getPrivateContent(@RequestParam String name, @RequestParam String tokenId, @RequestParam String fingerprint) {
        Resource image = contentService.loadContent(name);
        return CompletableFuture.completedFuture(ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image));
    }

    @CrossOrigin
    @GetMapping(value = "/preview/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<StreamingResponseBody> getPreviewPicture(@PathVariable("id") String id) {
        InputStream inputStream = videoService.getPreviewInputStream(id)
                .orElseThrow();

        return ResponseEntity.ok(inputStream::transferTo);
    }

//    @CrossOrigin
//    @GetMapping("/stream/{id}/{tick}")
//    public ResponseEntity<Resource> getVideoLowResForScroll(@PathVariable String id, @PathVariable int tick) {
//
//
//    }


    @CrossOrigin
    @GetMapping("/stream/{id}")
    @Async
    public CompletableFuture<ResponseEntity<StreamingResponseBody>> streamVideo(
            @RequestHeader(value = "Range", required = false) String httpRangeHeader,
            @PathVariable("id") String id
    ) {
        List<HttpRange> httpRangeList = HttpRange.parseRanges(httpRangeHeader);

        StreamBytesInfo streamBytesInfo = videoService
                .getStreamBytes(id, httpRangeList.size() > 0 ? httpRangeList.get(0) : null)
                .orElseThrow();

        long byteLength = streamBytesInfo.getRangeEnd() - streamBytesInfo.getRangeStart() + 1;

        ResponseEntity.BodyBuilder builder = ResponseEntity
                .status(httpRangeList.size() > 0 ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK)
                .header("Content-Type", streamBytesInfo.getContentType())
                .header("Accept-Ranges", "bytes")
                .header("Content-Length", Long.toString(byteLength));

        if (httpRangeList.size() > 0) {
            builder.header(
                    "Content-Range",
                    "bytes " + streamBytesInfo.getRangeStart() +
                            "-" + streamBytesInfo.getRangeEnd() +
                            "/" + streamBytesInfo.getFileSize());
        }
        System.out.println("Providing bytes from " + streamBytesInfo.getRangeStart() + " to " + streamBytesInfo.getRangeEnd() +  ". We are at " + new DecimalFormat("###.##").format(100.0 * streamBytesInfo.getRangeStart() / streamBytesInfo.getFileSize()) + "% of overall video.");
        return CompletableFuture.completedFuture(builder.body(streamBytesInfo.getResponseBody()));
    }

    @CrossOrigin
    @GetMapping("/stream/{id}/{tick}")
    @Async
    public CompletableFuture<ResponseEntity<Resource>> streamVideoTick(
            @PathVariable("id") String id,
            @PathVariable int tick) {
        int bgFrame = Math.round((float) tick /600) * 600;
        System.out.println("Providing frame " + bgFrame);
        System.out.println("Providing frame " + id + "-frame_" + bgFrame + ".jpg");
        return CompletableFuture.completedFuture(ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(contentService.loadContent( id + "/" + id + "-frame_" + bgFrame + ".jpg")));
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public VideoMetadataRepresentation findVideoMetadataById(@PathVariable("id") String id) {
        return videoService.findRepresentationById(id).orElseThrow();
    }


    @CrossOrigin
    @GetMapping("/{id}/profile")
    public Resource findProfilePicture(@PathVariable("id") String id) {
        System.out.println(id);
        return contentService.loadResource(id);
    }

    @CrossOrigin
    @GetMapping("/{id}/banner")
    public Resource findProfileBanner(@PathVariable("id") String id) {
        return contentService.loadResource(id);
    }

    @Async
    @CrossOrigin
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<ResponseEntity<Void>> uploadVideo(NewVideoRepresentation newVideoRepresentation) {
        try {
            videoService.saveNewVideo(newVideoRepresentation);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());
        }
        return CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
}