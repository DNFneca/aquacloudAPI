package cloud.aquacloud.aquacloudapi.type.CustomFields;

import cloud.aquacloud.aquacloudapi.type.Post;
import cloud.aquacloud.aquacloudapi.type.User;

import java.io.Serializable;

public class LikedId implements Serializable {
    private User liker;

    private Post liked;

    // default constructor

    public LikedId(User liker, Post liked) {
        this.liker = liker;
        this.liked = liked;
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
    // equals() and hashCode()
}
