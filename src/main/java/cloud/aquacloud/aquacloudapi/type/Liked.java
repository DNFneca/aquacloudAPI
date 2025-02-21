package cloud.aquacloud.aquacloudapi.type;

import cloud.aquacloud.aquacloudapi.type.CustomFields.LikedId;
import jakarta.persistence.*;

@Entity
@Table
@IdClass(LikedId.class)
public class Liked {
    @Id
    @ManyToOne
    private User liker;
    @Id
    @ManyToOne
    private Post liked;
    private Long likedAt;

    public Liked() {}

    public Liked(User liker, Post liked) {
        this.liker = liker;
        this.liked = liked;
    }

    public void setLikedAt(Long likedAt) {
        this.likedAt = likedAt;
    }

    public Long getLikedAt() {
        return likedAt;
    }

    public void setLiked(Post liked) {
        this.liked = liked;
    }

    public void setLiker(User liker) {
        this.liker = liker;
    }

    public Post getLiked() {
        return liked;
    }

    public User getLiker() {
        return liker;
    }
}
