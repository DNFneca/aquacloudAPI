package cloud.aquacloud.aquacloudapi.type;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "token")
public class Token {
    @ManyToOne()
    private User user;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String tokenId;
    private String userFingerprint;
    private long expiryDate;

    public Token() {}

    public void setUser(User user) {
        this.user = user;
    }

    public void setExpiryDate(long expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setUserFingerprint(String userFingerprint) {
        this.userFingerprint = userFingerprint;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public long getExpiryDate() {
        return expiryDate;
    }

    public User getUser() {
        return user;
    }

    public String getUserFingerprint() {
        return userFingerprint;
    }
}
