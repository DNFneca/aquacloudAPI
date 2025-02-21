package cloud.aquacloud.aquacloudapi.service;

import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.IplImage;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static cloud.aquacloud.aquacloudapi.service.VideoService.getFileNameWithoutExt;
import static org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_H264;

@Service
public class FrameGrabberService {

    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

    /**
     * @param filePath - video file path.
     * @return optional video time length.
     * Using openCV library, take video preview and save into .JPEG file.
     */
    public Optional<Long> generatePreviewPictures(Path filePath, int fps) throws IOException {
        System.out.println(new File(filePath.toString()).exists());
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(filePath.toString()); OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();) {
            grabber.setImageWidth(1280);
            grabber.setImageHeight(720);
            grabber.setFormat("mp4");
            grabber.setVideoCodec(AV_CODEC_ID_H264);
            grabber.setAudioStream(-1);
            grabber.setOption("an", "1"); // Disable audio
            grabber.start();

            long lengthInTime = grabber.getLengthInTime();

            System.out.println(fps * 10);

            int totalFrames = grabber.getLengthInFrames();
            System.out.println(grabber.getLengthInFrames());

            ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

            for (int frameNumber = 0; frameNumber < totalFrames; frameNumber++) {
                final int currentFrame = frameNumber;
                    try {
                        if (currentFrame % (fps * 10) == 0) {
                            Frame frame = grabber.grabImage();
                            if (frame != null) {
                                executor.submit(() -> {

                                    Mat img = converter.convert(frame);
                                    if (img != null) {
                                        String filename = getFileNameWithoutExt(filePath.toString()) + "-frame_" + currentFrame + ".png";
                                        opencv_imgcodecs.imwrite(filename, img);
                                        System.out.println("Saved: " + filename);
                                    }
                                });

                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
            grabber.stop();
            grabber.release();

            return Optional.of(lengthInTime);
        } catch (InterruptedException e) {
            return Optional.empty();
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();

        // Set rendering hints for better quality
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();

        return resizedImage;
    }
}