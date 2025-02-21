package cloud.aquacloud.aquacloudapi.repository;

import cloud.aquacloud.aquacloudapi.type.Following;
import cloud.aquacloud.aquacloudapi.type.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowingRepository extends JpaRepository<Following, String> {
    boolean existsByFollower_TagAndFollowing_Tag(String followerTag, String followingTag);
    boolean existsByFollowerAndFollowing(User follower, User following);
    void deleteByFollowerAndFollowing(User follower, User following);
    void deleteByFollowerTagAndFollowingTag(String followerTag, String followingTag);
    Following findFollowingByFollower_TagAndFollowing_Tag(String followerTag, String followingTag);
}
