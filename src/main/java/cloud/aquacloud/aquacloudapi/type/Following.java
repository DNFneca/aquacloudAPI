package cloud.aquacloud.aquacloudapi.type;

import jakarta.persistence.*;

@Entity
@Table(name = "following")
@IdClass(FollowingId.class)
public class Following {
    @ManyToOne()
    @Id
    private User follower;
    @ManyToOne()
    @Id
    private User following;
    private Long followingSince;
    private boolean isPending = false;

    public Following() {}

    public Following(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }

    public Following setFollower(User follower) {
        this.follower = follower;
        return this;
    }

    public Following setFollowing(User following) {
        this.following = following;
        return this;
    }

    public User getFollower() {
        return follower;
    }

    public User getFollowing() {
        return following;
    }

    public boolean isPending() {
        return isPending;
    }

    public Following setPending(boolean pending) {
        isPending = pending;
        return this;
    }
}
