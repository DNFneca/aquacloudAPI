package cloud.aquacloud.aquacloudapi.type;

import cloud.aquacloud.aquacloudapi.function.GenerateImage;
import jakarta.persistence.*;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(unique = true, nullable = false)
    private String email;
    private String profileImage;
    @Column(unique = true, nullable = false)
    private String tag;
    private String username;
    private String description;
    private boolean isPrivate = false;
    @Column(nullable = false)
    private Long lastTimeActive;
    @Column(nullable = false)
    private Long accountCreated;
    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer followerCount = 0;
    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer followingCount = 0;
    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer postedContentCount = 0;
    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer likedContentOfOthersCount = 0;
    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer likedContentCount = 0;
    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer savedContentCount = 0;
    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer savedContentOfOthersCount = 0;
    @Column(nullable = false)
    private String password;
    public User() {
    }
    /**
     * Gets the id of the user.
     * @return the id of the user.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id of the user.
     * @param id the id of the user.
     */
    public void setId(String id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public String getTag() {
        return tag;
    }

    public String getEmail() {
        return email;
    }

    public void setAccountCreated(Long accountCreated) {
        this.accountCreated = accountCreated;
    }

    public void setLastTimeActive(Long lastTimeActive) {
        this.lastTimeActive = lastTimeActive;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Long getAccountCreated() {
        return accountCreated;
    }

    public Long getLastTimeActive() {
        return lastTimeActive;
    }

    public String getPassword() {
        return password;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFollowerCount(Integer followerCount) {
        this.followerCount = followerCount;
    }

    public void setFollowingCount(Integer followingCount) {
        this.followingCount = followingCount;
    }

    public void setLikedContentCount(Integer likedContentCount) {
        this.likedContentCount = likedContentCount;
    }

    public void setLikedContentOfOthersCount(Integer likedContentOfOthersCount) {
        this.likedContentOfOthersCount = likedContentOfOthersCount;
    }

    public void setPostedContentCount(Integer postedContentCount) {
        this.postedContentCount = postedContentCount;
    }

    public void setSavedContentCount(Integer savedContentCount) {
        this.savedContentCount = savedContentCount;
    }

    public void setSavedContentOfOthersCount(Integer savedContentOfOthersCount) {
        this.savedContentOfOthersCount = savedContentOfOthersCount;
    }

    public Integer getFollowerCount() {
        return followerCount;
    }

    public Integer getLikedContentCount() {
        return likedContentCount;
    }

    public Integer getFollowingCount() {
        return followingCount;
    }

    public Integer getLikedContentOfOthersCount() {
        return likedContentOfOthersCount;
    }

    public Integer getPostedContentCount() {
        return postedContentCount;
    }

    public Integer getSavedContentCount() {
        return savedContentCount;
    }

    public Integer getSavedContentOfOthersCount() {
        return savedContentOfOthersCount;
    }

    public String getDescription() {
        return description;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
        System.out.println("called setEmail");
    }
}