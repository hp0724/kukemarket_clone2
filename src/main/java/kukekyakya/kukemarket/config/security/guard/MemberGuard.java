package kukekyakya.kukemarket.config.security.guard;

import kukekyakya.kukemarket.entity.member.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberGuard extends Guard {

    private List<RoleType> roleTypes =List.of(RoleType.ROLE_ADMIN);
    //지금 요청한 사용자가 인증되었는지,
    // 액세스 토큰을 통한 요청인지,
    // 자원 접근 권한(관리자 또는 자원의 소유주)을 가지고 있는지를 검사

    @Override
    protected List<RoleType> getRoleTypes() {
        return roleTypes;
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        return id.equals(AuthHelper.extractMemberId());
    }


}
