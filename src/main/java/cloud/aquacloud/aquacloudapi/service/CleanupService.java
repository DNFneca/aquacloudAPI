package cloud.aquacloud.aquacloudapi.service;

import cloud.aquacloud.aquacloudapi.repository.TokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CleanupService {

    private final TokenRepository repository;

    public CleanupService(TokenRepository repository) {
        this.repository = repository;
    }

    @Scheduled(cron = "0 */15 * * * ?")
    public void deleteExpiredEntities() {
        System.out.println("Deleting expired entities");
        repository.deleteAllByExpiryDateBefore(Instant.now().getEpochSecond());
    }
}