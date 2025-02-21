package cloud.aquacloud.aquacloudapi.controller;

import cloud.aquacloud.aquacloudapi.function.GenerateImage;
import cloud.aquacloud.aquacloudapi.function.Hash;
import cloud.aquacloud.aquacloudapi.service.FollowingService;
import cloud.aquacloud.aquacloudapi.service.TokenService;
import cloud.aquacloud.aquacloudapi.service.UserService;
import cloud.aquacloud.aquacloudapi.type.Following;
import cloud.aquacloud.aquacloudapi.type.Token;
import cloud.aquacloud.aquacloudapi.type.User;
import cloud.aquacloud.aquacloudapi.type.DTO.UserAccessibleUser;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final FollowingService followingService;

    public UserController(UserService userService, TokenService tokenService, FollowingService followingService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.followingService = followingService;
    }

    @CrossOrigin
    @PostMapping("/tag:{tag}")
    public ResponseEntity<Object> getUserByUsername(@PathVariable String tag, @RequestParam String fingerprint, @RequestParam String tokenId) {
        Optional<User> user = userService.getUserByTag(tag);
        System.out.println(tokenId);
        UserAccessibleUser userAccessibleUser = new UserAccessibleUser();
        if (tokenId.isBlank()) {
            userAccessibleUser.setTag(user.get().getTag());
            userAccessibleUser.setFollowed(false);
            userAccessibleUser.setUsername(user.get().getUsername());
            userAccessibleUser.setProfileImage(user.get().getProfileImage());
            userAccessibleUser.setDescription(user.get().getDescription());
            userAccessibleUser.setFollowerCount(null);
            userAccessibleUser.setFollowingCount(null);
            userAccessibleUser.setSavedContentCount(null);
            userAccessibleUser.setSavedContentOfOthersCount(null);
            userAccessibleUser.setLikedContentCount(null);
            userAccessibleUser.setLikedContentOfOthersCount(null);
            userAccessibleUser.setPostedContentCount(user.get().getPostedContentCount());
            userAccessibleUser.setFollowed(false);
            userAccessibleUser.setBlocked(false);
            userAccessibleUser.setFollowRequestPending(false);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(userAccessibleUser);
        }

        if (user.isEmpty()) return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new Gson().toJson("User doesn't exist"));
        Optional<Token> token = tokenService.getTokenById(tokenId);
        if (!followingService.isUserTagFollowingUserTag(token.get().getUser().getTag(), tag) || !token.get().getUserFingerprint().equals(fingerprint)) {
            userAccessibleUser.setFollowed(true);
            userAccessibleUser.setTag(user.get().getTag());
            userAccessibleUser.setUsername(user.get().getUsername());
            userAccessibleUser.setProfileImage(user.get().getProfileImage());
            userAccessibleUser.setDescription(user.get().getDescription());
            userAccessibleUser.setPrivate(user.get().isPrivate());
            userAccessibleUser.setFollowRequestPending(false);
            userAccessibleUser.setFollowed(false);
            userAccessibleUser.setFollowerCount(user.get().getFollowerCount());
            userAccessibleUser.setFollowingCount(user.get().getFollowingCount());
            userAccessibleUser.setSavedContentCount(user.get().getSavedContentCount());
            userAccessibleUser.setSavedContentOfOthersCount(user.get().getSavedContentOfOthersCount());
            userAccessibleUser.setLikedContentCount(user.get().getLikedContentCount());
            userAccessibleUser.setLikedContentOfOthersCount(user.get().getLikedContentOfOthersCount());
            userAccessibleUser.setPostedContentCount(user.get().getPostedContentCount());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(userAccessibleUser);
        }
        try {
        userAccessibleUser.setFollowed(true);
        userAccessibleUser.setTag(user.get().getTag());
        userAccessibleUser.setUsername(user.get().getUsername());
        userAccessibleUser.setProfileImage(user.get().getProfileImage());
        userAccessibleUser.setDescription(user.get().getDescription());
        userAccessibleUser.setPrivate(user.get().isPrivate());
        userAccessibleUser.setFollowRequestPending(followingService.isFollowPending(new Following(token.get().getUser(), user.orElse(null))));
        userAccessibleUser.setFollowed(followingService.isUserTagFollowingUserTag(token.get().getUser().getTag(), user.get().getTag()));
        userAccessibleUser.setFollowerCount(user.get().getFollowerCount());
        userAccessibleUser.setFollowingCount(user.get().getFollowingCount());
        userAccessibleUser.setSavedContentCount(user.get().getSavedContentCount());
        userAccessibleUser.setSavedContentOfOthersCount(user.get().getSavedContentOfOthersCount());
        userAccessibleUser.setLikedContentCount(user.get().getLikedContentCount());
        userAccessibleUser.setLikedContentOfOthersCount(user.get().getLikedContentOfOthersCount());
        userAccessibleUser.setPostedContentCount(user.get().getPostedContentCount());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userAccessibleUser);
        }
     catch (IOException e) {
         return ResponseEntity.ok()
                 .contentType(MediaType.APPLICATION_JSON)
                 .body(e);
        }
    }
}