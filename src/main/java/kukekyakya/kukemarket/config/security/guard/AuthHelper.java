package kukekyakya.kukemarket.config.security.guard;

import kukekyakya.kukemarket.config.security.CustomAuthenticationToken;
import kukekyakya.kukemarket.config.security.CustomUserDetails;
import kukekyakya.kukemarket.entity.member.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
//사용자 정보를 통해 우리가 필요한 요청자의 id 나 인증 여부 , 권한 등급 , 요청 토큰의 타입을 추출하는데 도움
public class AuthHelper {

    public static boolean isAuthenticated() {
        return getAuthentication() instanceof CustomAuthenticationToken &&
                getAuthentication().isAuthenticated();
    }

    public static Long extractMemberId() {
        return Long.valueOf(getUserDetails().getUserId());
    }

    public static Set<RoleType> extractMemberRoles() {
        return getUserDetails().getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .map(strAuth -> RoleType.valueOf(strAuth))
                .collect(Collectors.toSet());
    }


    private static CustomUserDetails getUserDetails() {
        return (CustomUserDetails) getAuthentication().getPrincipal();
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}