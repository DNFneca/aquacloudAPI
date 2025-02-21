package cloud.aquacloud.aquacloudapi.repository;

import cloud.aquacloud.aquacloudapi.type.Token;
import cloud.aquacloud.aquacloudapi.type.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
    Optional<Token> findByTokenId(String tokenId);
    Optional<Token> findByTokenIdAndUserFingerprint(String tokenId, String userFingerprint);
    Optional<Set<Token>> findAllByUserId(String userId);
    Optional<Token> findByUserId(String userId);
    Optional<Token> findByUserFingerprint(String userFingerprint);
    @Transactional
    @Modifying
    void removeTokenByTokenId(String tokenId);
    @Transactional
    @Modifying
    @Query("UPDATE Token t SET t.userFingerprint = :userFingerprint WHERE t.tokenId = :tokenId")
    void updateTokenByUserFingerprint(String userFingerprint, String tokenId);
    @Transactional
    void deleteAllByExpiryDateBefore(long expiryDate);
}
