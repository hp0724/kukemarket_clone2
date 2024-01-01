package kukekyakya.kukemarket.config.security.guard;

import kukekyakya.kukemarket.entity.member.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberGuard {

    private final AuthHelper authHelper;

    //지금 요청한 사용자가 인증되었는지,
    // 액세스 토큰을 통한 요청인지,
    // 자원 접근 권한(관리자 또는 자원의 소유주)을 가지고 있는지를 검사
    public boolean check(Long id) {
        return authHelper.isAuthenticated() && authHelper.isAccessTokenType() && hasAuthority(id);
    }

    private boolean hasAuthority(Long id) {
        Long memberId = authHelper.extractMemberId();
        Set<RoleType> memberRoles = authHelper.extractMemberRoles();
        return id.equals(memberId) || memberRoles.contains(RoleType.ROLE_ADMIN);
    }
}
