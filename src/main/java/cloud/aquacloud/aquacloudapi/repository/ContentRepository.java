package cloud.aquacloud.aquacloudapi.repository;

import cloud.aquacloud.aquacloudapi.type.Content;
import cloud.aquacloud.aquacloudapi.type.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentRepository extends JpaRepository<Content, String> {
    Optional<Content> findById(String id);
    Optional<Content> findByContentURL(String contentURL);
    boolean existsByContentURL(String contentURL);
    boolean existsById(String id);
}
