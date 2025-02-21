package cloud.aquacloud.aquacloudapi.type.DTO;

import cloud.aquacloud.aquacloudapi.type.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class UserAccessibleUser {
    private String profileImage;
    private String tag;
    private String username;
    private String description;
    private Integer followerCount = 0;
    private Integer followingCount = 0;
    private Integer postedContentCount = 0;
    private Integer likedContentOfOthersCount = 0;
    private Integer likedContentCount = 0;
    private Integer savedContentCount = 0;
    private Integer savedContentOfOthersCount = 0;
    private boolean isFollowed = false;
    private boolean isBlocked = false;
    private boolean isPrivate = false;
    private boolean isFollowRequestPending = false;
    private List<Post> posts = new ArrayList<>();

    public UserAccessibleUser() {
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getTag() {
        return tag;
    }

    public String getUsername() {
        return username;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }

    public boolean isFollowRequestPending() {
        return isFollowRequestPending;
    }

    public void setFollowRequestPending(boolean followRequestPending) {
        isFollowRequestPending = followRequestPending;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Integer getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(Integer followerCount) {
        this.followerCount = followerCount;
    }

    public Integer getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(Integer followingCount) {
        this.followingCount = followingCount;
    }

    public Integer getPostedContentCount() {
        return postedContentCount;
    }

    public void setPostedContentCount(Integer postedContentCount) {
        this.postedContentCount = postedContentCount;
    }

    public Integer getLikedContentOfOthersCount() {
        return likedContentOfOthersCount;
    }

    public void setLikedContentOfOthersCount(Integer likedContentOfOthersCount) {
        this.likedContentOfOthersCount = likedContentOfOthersCount;
    }

    public Integer getLikedContentCount() {
        return likedContentCount;
    }

    public void setLikedContentCount(Integer likedContentCount) {
        this.likedContentCount = likedContentCount;
    }

    public Integer getSavedContentCount() {
        return savedContentCount;
    }

    public void setSavedContentCount(Integer savedContentCount) {
        this.savedContentCount = savedContentCount;
    }

    public Integer getSavedContentOfOthersCount() {
        return savedContentOfOthersCount;
    }

    public void setSavedContentOfOthersCount(Integer savedContentOfOthersCount) {
        this.savedContentOfOthersCount = savedContentOfOthersCount;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
