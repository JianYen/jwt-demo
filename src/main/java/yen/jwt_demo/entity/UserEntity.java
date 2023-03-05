package yen.jwt_demo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

@Data
public class UserEntity {

    public UserEntity(String username, String password, Collection<? extends GrantedAuthority> role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    private String password;

    private String token;

    private int attemptLoginCount;

    private LocalDateTime canLoginTime;

    private Collection<? extends GrantedAuthority> role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<? extends GrantedAuthority> getRole() {
        return role;
    }

    public void setRole(Collection<? extends GrantedAuthority> role) {
        this.role = role;
    }
}
