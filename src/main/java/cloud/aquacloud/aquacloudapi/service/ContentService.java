package cloud.aquacloud.aquacloudapi.service;

import cloud.aquacloud.aquacloudapi.repository.ContentRepository;
import cloud.aquacloud.aquacloudapi.type.AccessRequirement;
import cloud.aquacloud.aquacloudapi.type.Content;
import cloud.aquacloud.aquacloudapi.type.User;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class ContentService {

    public static final Path uploads = Paths.get("uploads");
    public static final Path root = Paths.get("images");
    private ContentRepository contentRepository;

    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
        try {
            Files.createDirectories(uploads);
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    public Optional<Content> findByContentURL(String contentURL) {
        return contentRepository.findByContentURL(contentURL);
    }

    public void saveUploadedContent(MultipartFile file, User user, AccessRequirement accessRequirement) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path destinationFile = uploads.resolve(fileName);
        Content savedContent = contentRepository.save(new Content());
        savedContent.setContentURL(savedContent.getId() + file.getContentType());
        savedContent.setPostedOn(Instant.now().getEpochSecond());
        savedContent.setAuthor(user);
        savedContent.setAccessRequirement(accessRequirement);
        Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
    }

    public String saveContent(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path destinationFile = root.resolve(fileName);
        Content savedContent = contentRepository.save(new Content());
        savedContent.setContentURL(savedContent.getId() + file.getContentType());
        Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    public Resource loadContent(String fileName) {
        try {
            Path file = uploads.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }


    public Resource loadResource(String fileName) {
        try {
            Path file = root.resolve(fileName);
            System.out.println(file);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
