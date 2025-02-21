package cloud.aquacloud.aquacloudapi.repository;

import cloud.aquacloud.aquacloudapi.type.VideoMetadata;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoMetadataRepository extends JpaRepository<VideoMetadata, String> {
    @Transactional
    @Modifying
    @Query("UPDATE Token t SET t.userFingerprint = :userFingerprint WHERE t.tokenId = :tokenId")
    void updateTokenByUserFingerprint(String userFingerprint, String tokenId);
}