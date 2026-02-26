package site.ashenstation.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import site.ashenstation.entity.AdminUser;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class JwtUserDto implements UserDetails, Serializable {
    private AdminUser adminUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return adminUser.getPassword();
    }

    @Override
    public String getUsername() {
        return adminUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !adminUser.getExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !adminUser.getLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !adminUser.getCredentialsExpired();
    }

    @Override
    public boolean isEnabled() {
        return adminUser.getEnabled();
    }
}
