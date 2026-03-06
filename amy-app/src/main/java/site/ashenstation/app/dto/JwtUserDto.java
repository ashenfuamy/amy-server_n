package site.ashenstation.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import site.ashenstation.entity.AppUser;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class JwtUserDto implements UserDetails, Serializable {

    private AppUser appUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return appUser.getPassword();
    }

    @Override
    public String getUsername() {
        return appUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !appUser.getExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !appUser.getLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !appUser.getCredentialsExpired();
    }

    @Override
    public boolean isEnabled() {
        return appUser.getEnabled();
    }
}
