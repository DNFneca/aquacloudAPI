package cloud.aquacloud.aquacloudapi.controller;

import cloud.aquacloud.aquacloudapi.function.GenerateImage;
import cloud.aquacloud.aquacloudapi.function.Hash;
import cloud.aquacloud.aquacloudapi.service.FollowingService;
import cloud.aquacloud.aquacloudapi.service.TokenService;
import cloud.aquacloud.aquacloudapi.service.UserService;
import cloud.aquacloud.aquacloudapi.type.Token;
import cloud.aquacloud.aquacloudapi.type.User;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/users")
public class AccountManagementController {

    private final UserService userService;
    private final TokenService tokenService;
    private final FollowingService followingService;

    public AccountManagementController(UserService userService, TokenService tokenService, FollowingService followingService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.followingService = followingService;
    }

    @Async
    @CrossOrigin
    @PostMapping("/register")
    public CompletableFuture<ResponseEntity<String>> createUser(@RequestParam String email, @RequestParam String username, @RequestParam String password) {
        try {
            if (userService.doesUserExistByEmail(email)) return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(new Gson().toJson("User with that email already exists")));
            if (userService.doesUserExistByTag(username.toLowerCase())) return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(new Gson().toJson("User with that email already exists")));
            User user = new User();
            if (user.getTag() == null) user.setTag(username.toLowerCase());
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(Hash.generateSHASHA3_512(password));
            user.setAccountCreated(Instant.now().getEpochSecond());
            user.setLastTimeActive(Instant.now().getEpochSecond());
            User savedUser = userService.saveUser(user);
            if (savedUser.getProfileImage() == null) {
                GenerateImage.generateImage(savedUser);
                savedUser.setProfileImage("images/root:"+savedUser.getId()+".jpg");
                userService.saveUser(savedUser);
            }
            return CompletableFuture.completedFuture(ResponseEntity.ok().body(new Gson().toJson("User created successfully")));
        } catch (IOException e) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Gson().toJson("Error uploading image: " + e.getMessage())));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Gson().toJson("Error hashing password: " + e.getMessage())));
        }
    }

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password, @RequestParam String fingerprint, @RequestParam boolean rememberMe) {
        if (!userService.doesUserExistByEmail(email)) return ResponseEntity.badRequest().body(new Gson().toJson("User doesn't exist"));
        try {
            Optional<User> user = userService.getUserByEmail(email);
            if (!user.get().getPassword().equals(Hash.generateSHASHA3_512(password))) return ResponseEntity.badRequest().body(new Gson().toJson("Incorrect password"));
            if (tokenService.getTokenByFingerprint(fingerprint).isEmpty()) {
                Token token = new Token();
                token.setUser(user.orElse(null));
                token.setUserFingerprint(fingerprint);
                long now = Instant.now().getEpochSecond();
                if (rememberMe) token.setExpiryDate(now + 1209600);
                else token.setExpiryDate(now + 86400);
                token = tokenService.saveToken(token);
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new Gson().toJson(token.getTokenId()));
            } else {
                Token token = tokenService.getTokenByFingerprint(fingerprint).get();
                if (rememberMe) token.setExpiryDate(Instant.now().getEpochSecond() + 1209600);
                else token.setExpiryDate(Instant.now().getEpochSecond() + 86400);
                token.setUser(user.orElse(null));
                token.setUserFingerprint(fingerprint);
                tokenService.updateTokenFingerprint(token.getTokenId(), fingerprint);
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new Gson().toJson(token.getTokenId()));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
