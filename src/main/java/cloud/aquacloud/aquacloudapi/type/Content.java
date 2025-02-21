package cloud.aquacloud.aquacloudapi.type;

import jakarta.persistence.*;

@Entity
@Table
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String contentURL;
    private Long postedOn;
    @ManyToOne
    private User author;
    private AccessRequirement accessRequirement;

    public Content() {}

    public Content(String contentURL) {
        this.contentURL = contentURL;
    }

    public void setAccessRequirement(AccessRequirement accessRequirement) {
        this.accessRequirement = accessRequirement;
    }

    public void setPostedOn(Long postedOn) {
        this.postedOn = postedOn;
    }

    public AccessRequirement getAccessRequirement() {
        return accessRequirement;
    }

    public Long getPostedOn() {
        return postedOn;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }


    public String getId() {
        return id;
    }
    public String getContentURL() {
        return contentURL;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContentURL(String contentURL) {
        this.contentURL = contentURL;
    }
}
