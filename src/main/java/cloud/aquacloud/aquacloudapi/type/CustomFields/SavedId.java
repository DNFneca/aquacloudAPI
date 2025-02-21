package cloud.aquacloud.aquacloudapi.type.CustomFields;

import cloud.aquacloud.aquacloudapi.type.Post;
import cloud.aquacloud.aquacloudapi.type.User;

import java.io.Serializable;

public class SavedId implements Serializable {
    private User saver;
    private Post saved;

    public SavedId() {}

    public SavedId(User saver, Post saved) {
        this.saver = saver;
        this.saved = saved;
    }

    public void setSaved(Post saved) {
        this.saved = saved;
    }

    public void setSaver(User saver) {
        this.saver = saver;
    }

    public Post getSaved() {
        return saved;
    }

    public User getSaver() {
        return saver;
    }
}
