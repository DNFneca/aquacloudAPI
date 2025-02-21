package cloud.aquacloud.aquacloudapi.controller;

import cloud.aquacloud.aquacloudapi.service.FollowingService;
import cloud.aquacloud.aquacloudapi.service.TokenService;
import cloud.aquacloud.aquacloudapi.service.UserService;
import cloud.aquacloud.aquacloudapi.type.Token;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/token")
public class TokenController {

    private final UserService userService;
    private final TokenService tokenService;
    private final FollowingService followingService;

    public TokenController(UserService userService, TokenService tokenService, FollowingService followingService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.followingService = followingService;
    }

    @CrossOrigin
    @PostMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam String tokenId, @RequestParam String fingerprint) {
        Optional<Token> token = tokenService.getTokenById(tokenId);
        if (token.get().getUserFingerprint().equals(fingerprint)) {
            return ResponseEntity.ok().body(new Gson().toJson("valid"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Gson().toJson("invalid"));
    }
}
