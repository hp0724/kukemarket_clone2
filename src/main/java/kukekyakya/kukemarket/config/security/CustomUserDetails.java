package kukekyakya.kukemarket.config.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Getter
@AllArgsConstructor
//인증된 사용자의 정보와 권한을 담고 있다
//spring security 에서 제공해주는 UserDetails 구현한 클래스
public class CustomUserDetails implements UserDetails {

    private final String userId;
    private final Set<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return userId;
    }


    //나머지 오버라이드된 메소드는 유효한 메소드가 아니므로 호출시 예외를 발생시키도록 함
    @Override
    public String getPassword() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAccountNonExpired() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAccountNonLocked() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEnabled() {
        throw new UnsupportedOperationException();
    }
}
