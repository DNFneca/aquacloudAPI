package cloud.aquacloud.aquacloudapi.type;

import java.io.Serializable;

public class FollowingId implements Serializable {
    private User follower;

    private User following;

    // default constructor

    public FollowingId() {}

    public FollowingId(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }

    public void setFollowing(User following) {
        this.following = following;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowing() {
        return following;
    }

    public User getFollower() {
        return follower;
    }
    // equals() and hashCode()
}
