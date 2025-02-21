package cloud.aquacloud.aquacloudapi.type;

import cloud.aquacloud.aquacloudapi.type.CustomFields.SavedId;
import jakarta.persistence.*;

@Entity
@Table
@IdClass(SavedId.class)
public class Saved {
    @Id
    @ManyToOne
    private User saver;
    @Id
    @ManyToOne
    private Post saved;
    private Long savedSince;

    public Saved() {}
    public Saved(User saver, Post saved) {
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
