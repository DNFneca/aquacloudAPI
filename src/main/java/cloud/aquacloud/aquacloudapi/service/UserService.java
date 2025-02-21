package cloud.aquacloud.aquacloudapi.service;

import cloud.aquacloud.aquacloudapi.repository.TokenRepository;
import cloud.aquacloud.aquacloudapi.repository.UserRepository;
import cloud.aquacloud.aquacloudapi.type.Token;
import cloud.aquacloud.aquacloudapi.type.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

@Service
public class UserService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public UserService(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    public User saveUser(User user) throws IOException {
        return userRepository.save(user);
    }

    public boolean doesUserExistByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }
    public boolean doesUserExistByTag(String tag) {
        return userRepository.existsUserByTag(tag);
    }

    public Optional<User> getUserByToken(Token token) {
        return userRepository.findById(token.getUser().getId());
    }

    public boolean validateUser(String userId) {
        Optional<User> user = userRepository.findById(userId);
        return validateUserCore(user);
    }

    public boolean validateUser(Optional<User> user) {
        return validateUserCore(user);
    }

    private boolean validateUserCore(Optional<User> user) {
        return user.isPresent();
    }

    public Optional<User> getUserByTag(String tab) {
        return userRepository.findByTag(tab);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }
}