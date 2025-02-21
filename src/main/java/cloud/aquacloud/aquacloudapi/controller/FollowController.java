package cloud.aquacloud.aquacloudapi.controller;

import cloud.aquacloud.aquacloudapi.service.FollowingService;
import cloud.aquacloud.aquacloudapi.service.TokenService;
import cloud.aquacloud.aquacloudapi.service.UserService;
import cloud.aquacloud.aquacloudapi.type.Following;
import cloud.aquacloud.aquacloudapi.type.Token;
import cloud.aquacloud.aquacloudapi.type.User;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class FollowController {

    private final UserService userService;
    private final TokenService tokenService;
    private final FollowingService followingService;

    public FollowController(UserService userService, TokenService tokenService, FollowingService followingService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.followingService = followingService;
    }

    @CrossOrigin
    @PostMapping("/follow")
    public ResponseEntity<Object> followUserByTag(@RequestParam String followerToken, @RequestParam String fingerprint, @RequestParam String followingTag) {
        try {

            Optional<Token> _token = tokenService.getTokenById(followerToken);

            if (!tokenService.validateToken(_token, fingerprint)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Gson().toJson("Can't follow user, you're not logged in"));
            }
            Optional<User> _follower = userService.getUserByToken(_token.get());
            Optional<User> _following = userService.getUserByTag(followingTag);

            if (_follower.isEmpty()) {
                return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(new Gson().toJson("Can't follow user, you're not logged in"));
            }
            if (_following.isEmpty()) {
                return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(new Gson().toJson("Can't follow user, you're not logged in"));
            }
            User follower = _follower.get();
            User following = _following.get();

            if (!followingService.isUserTagFollowingUserTag(follower.getTag(), following.getTag())) {
                followingService.saveFollowing(new Following(follower, following).setPending(following.isPrivate()));
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Gson().toJson("Successfully followed @" + followingTag));
            }
            return ResponseEntity.badRequest().body(new Gson().toJson("Already following " + followingTag));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Gson().toJson("Error uploading image: " + e.getMessage()));
        }
    }

    @CrossOrigin
    @PostMapping("/unfollow")
    public ResponseEntity<Object> unfollowUserByTag(@RequestParam String followerToken, @RequestParam String fingerprint, @RequestParam String followingTag) {
        try {

            Optional<Token> _token = tokenService.getTokenById(followerToken);

            if (!tokenService.validateToken(_token, fingerprint)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Gson().toJson("Can't follow user, you're not logged in"));
            }
            Optional<User> _follower = userService.getUserByToken(_token.get());
            Optional<User> _following = userService.getUserByTag(followingTag);

            if (_follower.isEmpty()) {
                return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(new Gson().toJson("Can't follow user, you're not logged in"));
            }
            if (_following.isEmpty()) {
                return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(new Gson().toJson("Can't follow user, you're not logged in"));
            }
            User follower = _follower.get();
            User following = _following.get();

            if (followingService.isUserFollowingUser(follower, following)) {
                followingService.deleteFollowing(follower, following);
                return ResponseEntity.ok().body(new Gson().toJson(true));
            }
            return ResponseEntity.badRequest().body(new Gson().toJson("Unable to follow " + followingTag));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Gson().toJson("Error uploading image: " + e.getMessage()));
        }
    }
}
