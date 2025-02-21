package cloud.aquacloud.aquacloudapi.service;

import cloud.aquacloud.aquacloudapi.function.Progress;
import cloud.aquacloud.aquacloudapi.repository.VideoMetadataRepository;
import cloud.aquacloud.aquacloudapi.type.NewVideoRepresentation;
import cloud.aquacloud.aquacloudapi.type.VideoMetadata;
import cloud.aquacloud.aquacloudapi.type.VideoMetadataRepresentation;
import jakarta.transaction.Transactional;
import jakarta.websocket.Decoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FFmpegLogCallback;
import org.bytedeco.javacv.Frame;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.SequenceEncoder;
import org.jcodec.codecs.h264.decode.FrameReader;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRange;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ws.schild.jave.*;
import ws.schild.jave.encode.*;
import ws.schild.jave.encode.enums.X264_PROFILE;
import ws.schild.jave.info.VideoSize;
import ws.schild.jave.progress.EncoderProgressListener;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;
import static org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_H264;
import static org.bytedeco.ffmpeg.global.avcodec.avcodec_default_get_format;

@Service
public class VideoService {

    private String dataFolder = String.valueOf(Paths.get("uploads"));


    private final VideoMetadataRepository videoRepo;
    private final FrameGrabberService frameGrabberService;

    public VideoService(VideoMetadataRepository videoRepo, FrameGrabberService frameGrabberService) {
        this.videoRepo = videoRepo;
        this.frameGrabberService = frameGrabberService;
    }

    public List<VideoMetadataRepresentation> findAllRepresentation() {
        return videoRepo.findAll()
                .stream()
                .map(VideoMetadataRepresentation::new)
                .collect(Collectors.toList());
    }

    public Optional<VideoMetadataRepresentation> findRepresentationById(String id) {
        return videoRepo.findById(id)
                .map(VideoMetadataRepresentation::new);
    }

    public void saveNewVideo(NewVideoRepresentation newVideoRepr) {
        System.out.println("Got data");

        try {


            VideoMetadata metadata = new VideoMetadata();

            

            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("aac");
            audio.setBitRate(12000); // Example bitrate for audio
            audio.setChannels(2);
            audio.setSamplingRate(44100);

            metadata = videoRepo.save(metadata);

            Path directory = Path.of(dataFolder, metadata.getId().toString());


            Files.createDirectory(directory);
            int i = newVideoRepr.getFile().getOriginalFilename().lastIndexOf('.');
            String endname = newVideoRepr.getFile().getOriginalFilename();
            if (i > 0) {
                endname = newVideoRepr.getFile().getOriginalFilename().substring(i + 1);
            }
            Path file = Path.of(directory.toString(), metadata.getId().toString() + "." + endname);
            Path tempFile = Path.of(directory.toString(), metadata.getId().toString() + ".temp.mp4");
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile.toFile());
            fileOutputStream.write(newVideoRepr.getFile().getInputStream().readAllBytes());
            fileOutputStream.flush();
            fileOutputStream.close();

            VideoAttributes video = new VideoAttributes();
            video.setCodec("h264");
            video.setX264Profile(X264_PROFILE.BASELINE);
            // Here 160 kbps video is 160000
            video.setBitRate(160000);
            // More the frames more quality and size, but keep it low based on devices like mobile
            video.setFrameRate(15);
            video.setSize(new VideoSize(400, 300));

            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setEncodingThreads(4);
            attrs.setAudioAttributes(audio);
            attrs.setVideoAttributes(video);

            Encoder encoder = new Encoder();

            VideoMetadata finalMetadata = metadata;
            new Thread(() -> {
                try {

                    encoder.encode(new MultimediaObject(tempFile.toFile()), file.toFile(), attrs);
                    MultimediaObject multimediaObject = new MultimediaObject(file.toFile());

                    float videoFPS = multimediaObject.getInfo().getVideo().getFrameRate();
                    float videoBitrate = multimediaObject.getInfo().getVideo().getBitRate();
                    float audioBitrate = multimediaObject.getInfo().getAudio().getBitRate();
                    float audioSamplingRate = multimediaObject.getInfo().getAudio().getSamplingRate();

                    System.out.println("temp written");


                    attrs.setInputFormat("mp4");

                    if (videoFPS > 60) {
                        video.setFrameRate(60);
                        finalMetadata.setFps(60);
                    } else {
                        video.setFrameRate((int) Math.floor(videoFPS));
                        finalMetadata.setFps((int) Math.floor(videoFPS));
                    }
                    System.out.println("Video fps is: " + videoFPS);
                    System.out.println("Video bitrate is: " + videoBitrate);
                    System.out.println("Audio bitrate is: " + audioBitrate);
                    System.out.println("Audio sampling rate is: " + audioSamplingRate);
                    System.out.println(multimediaObject.getInfo().getVideo().getDecoder());


                    finalMetadata.setFileName(newVideoRepr.getFile().getOriginalFilename());
                    finalMetadata.setContentType(newVideoRepr.getFile().getContentType());
                    finalMetadata.setFileSize(newVideoRepr.getFile().getSize());
                    finalMetadata.setDescription(newVideoRepr.getDescription());

                    videoRepo.save(finalMetadata);

                } catch (EncoderException e) {
                    throw new RuntimeException(e);
                }
            }).start();

            Optional<Long> videoLength = frameGrabberService.generatePreviewPictures(tempFile, Math.round(60));
            finalMetadata.setVideoLength(videoLength.get());
            finalMetadata.setFileName(finalMetadata.getId().toString() + "." + endname);


//            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(newVideoRepr.getFile().getInputStream());
//            grabber.setFormat("mp4"); // Adjust based on input format
//
//            grabber.start();
//
//            // Create a frame recorder for the output file
//            FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(
//                    file.toString(),
//                    grabber.getImageWidth(),
//                    grabber.getImageHeight(),
//                    2
//            );
//            recorder.setOption("threads", "4");
//            recorder.setVideoOption("preset", "fast"); // NVENC preset
//            recorder.setVideoOption("tune", "ll"); // Low latency mode
//            recorder.setVideoOption("hwaccel", "cuda"); // Enable CUDA
//            recorder.setVideoCodecName("h264_nvenc"); // Use NVIDIA NVENC encoder
//            recorder.setVideoOption("bufsize", "1000000");
//            recorder.setFormat("mp4");
//            recorder.setVideoCodec(AV_CODEC_ID_H264);
//            recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
//            recorder.setFrameRate(grabber.getFrameRate());
//            // Set tune for zero latency (optional, useful for real-time streaming)
//            recorder.setOption("tune", "zerolatency");
//            recorder.start();
//
//
//            // Transcode frames
//
//            System.out.println("Transcoding frames");
//
//            int totalFrames = grabber.getLengthInFrames();
//
//            for (int frameNumber = 0; frameNumber < totalFrames; frameNumber++) {
//                Frame frame = null;
//                try {
//                    frame = grabber.grabImage();
//                    if (frame == null) {
//                        break;
//                    }
//                    recorder.record(frame);
//                } catch (FFmpegFrameRecorder.Exception | FFmpegFrameGrabber.Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }
//
//            // Close the grabber and recorder
//            grabber.stop();
//            recorder.stop();
//

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            throw new IllegalStateException(ex);
        }
    }

    public Optional<InputStream> getPreviewInputStream(String id) {
        return videoRepo.findById(id)
                .flatMap(vmd -> {
                    Path previewPicturePath = Path.of(
                            dataFolder,
                            vmd.getId().toString(),
                            getFileNameWithoutExt(vmd.getFileName()) + "-frame_0.jpg");
                    if (!Files.exists(previewPicturePath)) {
                        return Optional.empty();
                    }
                    try {
                        return Optional.of(Files.newInputStream(previewPicturePath));
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                        return Optional.empty();
                    }
                });
    }

    public Optional<StreamBytesInfo> getStreamBytes(String id, HttpRange range) {
        Optional<VideoMetadata> vmdById = videoRepo.findById(id);
        if (vmdById.isEmpty()) {
            System.out.println("Video with id: " + id + " not found");
            return Optional.empty();
        }

        Path filePath = Path.of(dataFolder, String.valueOf(id), vmdById.get().getFileName());
        if (!Files.exists(filePath)) {
            System.out.println("File " + filePath + " not found");
            return Optional.empty();
        }

        try {
            long fileSize = Files.size(filePath);
            long chunkSize = (long) (fileSize * 0.01); // took 2.5MB of file size
            //If no range is specified
            if (range == null) {
                return Optional.of(new StreamBytesInfo(
                        out -> Files.newInputStream(filePath).transferTo(out),
                        fileSize,
                        0,
                        fileSize,
                        vmdById.get().getContentType()));
            }

            long rangeStart = range.getRangeStart(0); // Will be 0 if not specified
            long rangeEnd = rangeStart + chunkSize; // range.getRangeEnd(fileSize);

            if (rangeEnd >= fileSize) {
                rangeEnd = fileSize - 1;
            }

            final long finalRangeEnd = rangeEnd;

            return Optional.of(new StreamBytesInfo(
                    out -> {
                        try (InputStream inputStream = Files.newInputStream(filePath)) {
                            inputStream.skip(rangeStart);
                            byte[] bytes = inputStream.readNBytes((int) ((finalRangeEnd - rangeStart) + 1));
                            out.write(bytes);
                        }
                    },
                    fileSize, rangeStart, rangeEnd, vmdById.get().getContentType()));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return Optional.empty();
        }
    }

    public static String getFileNameWithoutExt(String fileName) {
        int extIndex = fileName.lastIndexOf(".");
        return fileName.substring(0, extIndex);
    }
}